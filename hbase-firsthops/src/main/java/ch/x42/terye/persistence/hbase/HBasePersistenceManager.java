package ch.x42.terye.persistence.hbase;

import java.io.IOException;
import java.util.Iterator;
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
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import ch.x42.terye.Path;
import ch.x42.terye.persistence.ChangeLog;
import ch.x42.terye.persistence.ChangeLog.Operation;
import ch.x42.terye.persistence.ItemState;
import ch.x42.terye.persistence.NodeState;
import ch.x42.terye.persistence.PersistenceManager;
import ch.x42.terye.persistence.PropertyState;
import ch.x42.terye.persistence.id.ItemId;
import ch.x42.terye.persistence.id.NodeId;
import ch.x42.terye.persistence.id.PropertyId;

public class HBasePersistenceManager implements PersistenceManager {

    private static HBasePersistenceManager instance;
    private Configuration config;
    private HBaseAdmin admin;
    private HTable nodeTable;
    private HTable propertyTable;

    private HBasePersistenceManager() throws RepositoryException {
        config = HBaseConfiguration.create();
        try {
            admin = new HBaseAdmin(config);
            nodeTable = getOrCreateTable(Constants.NODE_TABLE_NAME);
            propertyTable = getOrCreateTable(Constants.PROPERTY_TABLE_NAME);
        } catch (Exception e) {
            throw new RepositoryException(
                    "Could not instantiate HBase persistence manager", e);
        }

    }

    public static synchronized HBasePersistenceManager getInstance()
            throws RepositoryException {
        if (instance == null) {
            instance = new HBasePersistenceManager();
        }
        return instance;
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
                Constants.COLUMN_FAMILY);
        tableDesc.addFamily(colDesc);
        admin.createTable(tableDesc);
    }

    private Result getRow(HTable table, String key) throws IOException {
        Get get = new Get(Bytes.toBytes(key));
        get.addFamily(Constants.COLUMN_FAMILY);
        return table.get(get);
    }

    private String getString(Result result, byte[] qualifier) {
        byte[] bytes = result.getValue(Constants.COLUMN_FAMILY, qualifier);
        return Bytes.toString(bytes);
    }

    private int getInt(Result result, byte[] qualifier) {
        byte[] bytes = result.getValue(Constants.COLUMN_FAMILY, qualifier);
        return Bytes.toInt(bytes);
    }

    private byte[] getBytes(Result result, byte[] qualifier) {
        return result.getValue(Constants.COLUMN_FAMILY, qualifier);
    }

    @Override
    public ItemState loadItem(ItemId id) throws RepositoryException {
        if (id.denotesNode()) {
            return loadNode((NodeId) id);
        } else {
            return loadProperty((PropertyId) id);
        }
    }

    private NodeState createNewNodeState(Result result) throws IOException,
            ClassNotFoundException {
        NodeId id = new NodeId(Bytes.toString(result.getRow()));
        String nodeTypeName = getString(result, Constants.NODE_COLNAME_NODETYPE);
        List<NodeId> childNodes = Utils.<List<NodeId>> deserialize(getBytes(
                result, Constants.NODE_COLNAME_CHILDNODES));
        return new NodeState(id, nodeTypeName, childNodes);
    }

    private PropertyState createNewPropertyState(Result result) {
        PropertyId id = new PropertyId(Bytes.toString(result.getRow()));
        int type = getInt(result, Constants.PROPERTY_COLNAME_TYPE);
        byte[] bytes = getBytes(result, Constants.PROPERTY_COLNAME_VALUE);
        return new PropertyState(id, type, bytes);
    }

    @Override
    public NodeState loadNode(NodeId id) throws RepositoryException {
        try {
            Result result = getRow(nodeTable, id.toString());
            if (result.isEmpty()) {
                return null;
            }
            return createNewNodeState(result);
        } catch (Exception e) {
            throw new RepositoryException("Could not load node " + id, e);
        }
    }

    @Override
    public PropertyState loadProperty(PropertyId id) throws RepositoryException {
        try {
            Result result = getRow(propertyTable, id.toString());
            if (result.isEmpty()) {
                return null;
            }
            return createNewPropertyState(result);
        } catch (IOException e) {
            throw new RepositoryException("Could not load property " + id, e);
        }
    }

    @Override
    public List<PropertyState> loadProperties(NodeId parentId)
            throws RepositoryException {
        Scan scan = new Scan();
        scan.addFamily(Constants.COLUMN_FAMILY);

        // don't add trailing slash if it is the root node
        String partialKey = parentId.toString()
                + (parentId.toString().equals(Path.ROOT) ? "" : Path.DELIMITER);

        // set range (amounts to a prefix scan)
        byte[] startRow = Bytes.toBytes(partialKey);
        byte[] stopRow = startRow.clone();
        stopRow[stopRow.length - 1]++;
        scan.setStartRow(startRow);
        scan.setStopRow(stopRow);

        // filter nodes further down the tree
        String regex = partialKey + "[^/]+";
        Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL,
                new RegexStringComparator(regex));
        scan.setFilter(filter);

        List<PropertyState> properties = new LinkedList<PropertyState>();
        try {
            ResultScanner scanner = propertyTable.getScanner(scan);
            for (Result result : scanner) {
                properties.add(createNewPropertyState(result));
            }
            scanner.close();
            return properties;
        } catch (IOException e) {
            throw new RepositoryException("Scan failed", e);
        }
    }

    public void store(ItemState state) throws RepositoryException {
        if (state.isNode()) {
            store((NodeState) state);
        } else {
            store((PropertyState) state);
        }
    }

    public void store(NodeState state) throws RepositoryException {
        Put put = new Put(Bytes.toBytes(state.getId().toString()));
        // node type name
        put.add(Constants.COLUMN_FAMILY, Constants.NODE_COLNAME_NODETYPE,
                Bytes.toBytes(state.getNodeTypeName()));
        // children
        try {
            put.add(Constants.COLUMN_FAMILY, Constants.NODE_COLNAME_CHILDNODES,
                    Utils.serialize(state.getChildNodes()));
        } catch (IOException e) {
            throw new RepositoryException(
                    "Caught exception while serializing child nodes", e);
        }
        // store
        try {
            nodeTable.put(put);
        } catch (IOException e) {
            throw new RepositoryException("Could not store node "
                    + state.getId(), e);
        }
    }

    public void store(PropertyState state) throws RepositoryException {
        Put put = new Put(Bytes.toBytes(state.getId().toString()));
        put.add(Constants.COLUMN_FAMILY, Constants.PROPERTY_COLNAME_TYPE,
                Bytes.toBytes(state.getType()));
        put.add(Constants.COLUMN_FAMILY, Constants.PROPERTY_COLNAME_VALUE,
                state.getBytes());
        try {
            propertyTable.put(put);
        } catch (IOException e) {
            throw new RepositoryException("Could not store property "
                    + state.getId(), e);
        }
    }

    public void delete(ItemId id) throws RepositoryException {
        if (id.denotesNode()) {
            delete((NodeId) id);
        } else {
            delete((PropertyId) id);
        }
    }

    public void delete(NodeId id) throws RepositoryException {
        Delete delete = new Delete(Bytes.toBytes(id.toString()));
        try {
            nodeTable.delete(delete);
        } catch (IOException e) {
            throw new RepositoryException("Could not delete node " + id, e);
        }
    }

    public void delete(PropertyId id) throws RepositoryException {
        Delete delete = new Delete(Bytes.toBytes(id.toString()));
        try {
            propertyTable.delete(delete);
        } catch (IOException e) {
            throw new RepositoryException("Could not delete property " + id, e);
        }
    }

    @Override
    public void persist(ChangeLog log) throws RepositoryException {
        // XXX: batch processing
        Iterable<Operation> operations = log.getOperations();
        Iterator<Operation> iterator = operations.iterator();
        try {
            while (iterator.hasNext()) {
                Operation op = iterator.next();
                if (op.isAddOperation() || op.isModifyOperation()) {
                    store(op.getState());
                } else if (op.isRemoveOperation()) {
                    delete(op.getState().getId());
                }
            }
        } catch (RepositoryException e) {
            throw new RepositoryException("Error persisting change log", e);
        }
    }

}
