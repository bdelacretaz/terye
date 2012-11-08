package ch.x42.terye.persistence.hbase;

import java.io.IOException;
import java.util.Iterator;

import javax.jcr.RepositoryException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

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
                Bytes.toBytes(Constants.COLUMN_FAMILY));
        tableDesc.addFamily(colDesc);
        admin.createTable(tableDesc);
    }

    private Result getRow(HTable table, String key) throws IOException {
        Get get = new Get(Bytes.toBytes(key));
        get.addFamily(Bytes.toBytes(Constants.COLUMN_FAMILY));
        return table.get(get);
    }

    private String getString(Result result, String qualifier) {
        byte[] bytes = result.getValue(Bytes.toBytes(Constants.COLUMN_FAMILY),
                Bytes.toBytes(qualifier));
        return Bytes.toString(bytes);
    }

    @Override
    public ItemState loadItem(ItemId id) throws RepositoryException {
        if (id.denotesNode()) {
            return loadNode((NodeId) id);
        }
        return loadProperty((PropertyId) id);
    }

    @Override
    public NodeState loadNode(NodeId id) throws RepositoryException {
        try {
            Result result = getRow(nodeTable, id.toString());
            if (result.isEmpty()) {
                return null;
            }
            String nodeTypeName = getString(result,
                    Constants.NODE_COLNAME_NODETYPE);
            return new NodeState(id, nodeTypeName);
        } catch (IOException e) {
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
            return new PropertyState(id);
        } catch (IOException e) {
            throw new RepositoryException("Could not load property " + id, e);
        }
    }

    private void store(ItemState state) throws IOException {
        if (state.isNode()) {
            store((NodeState) state);
        }
    }

    private void store(NodeState state) throws IOException {
        Put put = new Put(Bytes.toBytes(state.getId().toString()));
        put.add(Bytes.toBytes(Constants.COLUMN_FAMILY),
                Bytes.toBytes(Constants.NODE_COLNAME_NODETYPE),
                Bytes.toBytes(state.getNodeTypeName()));
        nodeTable.put(put);
    }

    @Override
    public void persist(ChangeLog log) throws RepositoryException {
        // XXX: batch processing
        Iterable<Operation> operations = log.getOperations();
        Iterator<Operation> iterator = operations.iterator();
        try {
            while (iterator.hasNext()) {
                Operation op = iterator.next();
                if (op.isAddOperation()) {
                    store(op.getState());
                }
            }
        } catch (IOException e) {
            throw new RepositoryException("Error persisting change log", e);
        }
    }

}
