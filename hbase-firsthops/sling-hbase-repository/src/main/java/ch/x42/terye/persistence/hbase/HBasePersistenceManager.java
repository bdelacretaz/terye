package ch.x42.terye.persistence.hbase;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.jcr.RepositoryException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.x42.terye.persistence.ChangeLog;
import ch.x42.terye.persistence.ItemState;
import ch.x42.terye.persistence.NodeState;
import ch.x42.terye.persistence.PersistenceManager;
import ch.x42.terye.persistence.PropertyState;
import ch.x42.terye.persistence.id.ItemId;
import ch.x42.terye.persistence.id.NodeId;
import ch.x42.terye.persistence.id.PropertyId;

public class HBasePersistenceManager implements PersistenceManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Configuration config;
    private HBaseAdmin admin;
    private HTable items;

    public HBasePersistenceManager() throws RepositoryException {
        ClassLoader ocl = Thread.currentThread().getContextClassLoader();
        try {
            // temporarily switch context class loader (needed so that
            // HBaseConfiguration can find hbase-default.xml and hbase-site.xml
            // in the hbase bundle jar file
            Thread.currentThread().setContextClassLoader(
                    HBaseConfiguration.class.getClassLoader());
            config = HBaseConfiguration.create();
            admin = new HBaseAdmin(config);
            items = getOrCreateTable(Constants.ITEMS_TABLE_NAME);
        } catch (Exception e) {
            throw new RepositoryException(
                    "Could not instantiate HBase persistence manager", e);
        } finally {
            // switch back class loader
            Thread.currentThread().setContextClassLoader(ocl);
        }

    }

    private HTable getOrCreateTable(String tableName) throws IOException {
        try {
            return new HTable(config, tableName);
        } catch (TableNotFoundException e) {
            createTable(tableName);
            return new HTable(config, tableName);
        }
    }

    private void createTable(String tableName) throws IOException {
        HTableDescriptor tableDesc = new HTableDescriptor(
                Bytes.toBytes(tableName));
        HColumnDescriptor colDesc = new HColumnDescriptor(
                Constants.ITEMS_CFNAME_DATA);
        tableDesc.addFamily(colDesc);
        admin.createTable(tableDesc);
    }

    private Result getItemRow(String key, Filter filter) throws IOException {
        Get get = new Get(Bytes.toBytes(key));
        get.addFamily(Constants.ITEMS_CFNAME_DATA);
        if (filter != null) {
            get.setFilter(filter);
        }
        return items.get(get);
    }

    private String getString(Result result, byte[] qualifier) {
        byte[] bytes = result.getValue(Constants.ITEMS_CFNAME_DATA, qualifier);
        return Bytes.toString(bytes);
    }

    private int getInt(Result result, byte[] qualifier) {
        byte[] bytes = result.getValue(Constants.ITEMS_CFNAME_DATA, qualifier);
        return Bytes.toInt(bytes);
    }

    private byte[] getBytes(Result result, byte[] qualifier) {
        return result.getValue(Constants.ITEMS_CFNAME_DATA, qualifier);
    }

    private NodeState createNewNodeState(Result result) throws IOException,
            ClassNotFoundException {
        NodeId id = new NodeId(Bytes.toString(result.getRow()));
        String nodeTypeName = getString(result,
                Constants.ITEMS_COLNAME_NODETYPE);
        List<String> childNodes = Utils.<List<String>> deserialize(getBytes(
                result, Constants.ITEMS_COLNAME_CHILDNODES));
        List<String> properties = Utils.<List<String>> deserialize(getBytes(
                result, Constants.ITEMS_COLNAME_PROPERTIES));
        return new NodeState(id, nodeTypeName, childNodes, properties);
    }

    private PropertyState createNewPropertyState(Result result) {
        PropertyId id = new PropertyId(Bytes.toString(result.getRow()));
        int type = getInt(result, Constants.ITEMS_COLNAME_TYPE);
        byte[] bytes = getBytes(result, Constants.ITEMS_COLNAME_VALUE);
        return new PropertyState(id, type, bytes);
    }

    @Override
    public synchronized ItemState loadItem(ItemId id)
            throws RepositoryException {
        if (id.denotesNode()) {
            return loadNode((NodeId) id);
        } else {
            return loadProperty((PropertyId) id);
        }
    }

    @Override
    public synchronized NodeState loadNode(NodeId id)
            throws RepositoryException {
        try {
            SingleColumnValueFilter filter = new SingleColumnValueFilter(
                    Constants.ITEMS_CFNAME_DATA,
                    Constants.ITEMS_COLNAME_ITEMTYPE, CompareOp.EQUAL,
                    Bytes.toBytes(0));
            Result result = getItemRow(id.toString(), filter);
            if (result.isEmpty()) {
                return null;
            }
            return createNewNodeState(result);
        } catch (Exception e) {
            throw new RepositoryException("Could not load node " + id, e);
        }
    }

    @Override
    public synchronized PropertyState loadProperty(PropertyId id)
            throws RepositoryException {
        try {
            SingleColumnValueFilter filter = new SingleColumnValueFilter(
                    Constants.ITEMS_CFNAME_DATA,
                    Constants.ITEMS_COLNAME_ITEMTYPE, CompareOp.EQUAL,
                    Bytes.toBytes(1));
            Result result = getItemRow(id.toString(), filter);
            if (result.isEmpty()) {
                return null;
            }
            return createNewPropertyState(result);
        } catch (IOException e) {
            throw new RepositoryException("Could not load property " + id, e);
        }
    }

    private Put createPut(ItemState state) throws RepositoryException {
        if (state.isNode()) {
            return createPut((NodeState) state);
        } else {
            return createPut((PropertyState) state);
        }
    }

    private Put createPut(NodeState state) throws RepositoryException {
        Put put = new Put(Bytes.toBytes(state.getId().toString()));
        // item type
        put.add(Constants.ITEMS_CFNAME_DATA, Constants.ITEMS_COLNAME_ITEMTYPE,
                Bytes.toBytes(0));
        // node type name
        put.add(Constants.ITEMS_CFNAME_DATA, Constants.ITEMS_COLNAME_NODETYPE,
                Bytes.toBytes(state.getNodeTypeName()));
        // children and properties
        try {
            put.add(Constants.ITEMS_CFNAME_DATA,
                    Constants.ITEMS_COLNAME_CHILDNODES,
                    Utils.serialize(state.getChildNodes()));
            put.add(Constants.ITEMS_CFNAME_DATA,
                    Constants.ITEMS_COLNAME_PROPERTIES,
                    Utils.serialize(state.getProperties()));
        } catch (IOException e) {
            throw new RepositoryException(
                    "Caught exception while serializing children ids", e);
        }
        return put;
    }

    private Put createPut(PropertyState state) {
        Put put = new Put(Bytes.toBytes(state.getId().toString()));
        // item type
        put.add(Constants.ITEMS_CFNAME_DATA, Constants.ITEMS_COLNAME_ITEMTYPE,
                Bytes.toBytes(1));
        // type
        put.add(Constants.ITEMS_CFNAME_DATA, Constants.ITEMS_COLNAME_TYPE,
                Bytes.toBytes(state.getType()));
        // value as byte array
        put.add(Constants.ITEMS_CFNAME_DATA, Constants.ITEMS_COLNAME_VALUE,
                state.getBytes());
        return put;
    }

    @Override
    public synchronized void store(ItemState state) throws RepositoryException {
        if (state.isNode()) {
            store((NodeState) state);
        } else {
            store((PropertyState) state);
        }
    }

    @Override
    public synchronized void store(NodeState state) throws RepositoryException {
        // store node
        try {
            items.put(createPut(state));
        } catch (IOException e) {
            throw new RepositoryException("Could not store node "
                    + state.getId(), e);
        }
    }

    @Override
    public synchronized void store(PropertyState state)
            throws RepositoryException {
        // store property
        try {
            items.put(createPut(state));
        } catch (IOException e) {
            throw new RepositoryException("Could not store property "
                    + state.getId(), e);
        }
    }

    private Delete createDelete(ItemId id) {
        Delete delete = new Delete(Bytes.toBytes(id.toString()));
        return delete;
    }

    @Override
    public synchronized void delete(ItemId id) throws RepositoryException {
        if (id.denotesNode()) {
            delete((NodeId) id);
        } else {
            delete((PropertyId) id);
        }
    }

    @Override
    public synchronized void delete(NodeId id) throws RepositoryException {
        try {
            // XXX: make sure we really delete a node
            items.delete(createDelete(id));
        } catch (IOException e) {
            throw new RepositoryException("Could not delete node " + id, e);
        }
    }

    @Override
    public synchronized void delete(PropertyId id) throws RepositoryException {
        try {
            // XXX: make sure we really delete a property
            items.delete(createDelete(id));
        } catch (IOException e) {
            throw new RepositoryException("Could not delete property " + id, e);
        }
    }

    /**
     * Deletes all rows (in node and property tables) with matching row key.
     */
    @SuppressWarnings("unused")
    private synchronized void deleteRange(String partialKey)
            throws RepositoryException {
        // XXX: inefficient (HBase does not support range deletes)
        // 1) do a range scan
        // 2) remove each matching row singly

        // scan range (amounts to a prefix scan)
        byte[] startRow = Bytes.toBytes(partialKey);
        byte[] stopRow = startRow.clone();
        stopRow[stopRow.length - 1]++;

        try {
            Scan scan = new Scan();
            scan.addFamily(Constants.ITEMS_CFNAME_DATA);
            scan.setStartRow(startRow);
            scan.setStopRow(stopRow);
            ResultScanner scanner = items.getScanner(scan);
            for (Result result : scanner) {
                Delete delete = new Delete(result.getRow());
                items.delete(delete);
            }
            scanner.close();
        } catch (Exception e) {
            throw new RepositoryException("Range delete failed", e);
        }
    }

    @Override
    public synchronized void persist(ChangeLog log) throws RepositoryException {
        List<Row> deleteBatch = new LinkedList<Row>();
        // delete removed states
        for (ItemState state : log.getRemovedStates()) {
            logger.debug("Delete: " + state.getPath());
            deleteBatch.add(createDelete(state.getId()));
        }
        List<Row> putBatch = new LinkedList<Row>();
        // add new states
        for (ItemState state : log.getAddedStates()) {
            logger.debug("Add: " + state.getPath());
            putBatch.add(createPut(state));
        }
        // modify existing states
        for (ItemState state : log.getModifiedStates()) {
            logger.debug("Modify: " + state.getPath());
            putBatch.add(createPut(state));
        }
        try {
            // we need to first execute the delete batch, as items might have
            // been removed and readded and the batch method does not provide
            // any ordering guarantees
            items.batch(deleteBatch);
            items.batch(putBatch);
        } catch (Exception e) {
            throw new RepositoryException(
                    "An error occurred persisting change log", e);
        }
    }

    @Override
    public void dispose() throws IOException {
        items.close();
        admin.close();
    }

}
