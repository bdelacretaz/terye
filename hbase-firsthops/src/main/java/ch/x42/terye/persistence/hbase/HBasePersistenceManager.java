package ch.x42.terye.persistence.hbase;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import ch.x42.terye.persistence.ItemState;
import ch.x42.terye.persistence.NodeState;
import ch.x42.terye.persistence.PersistenceManager;
import ch.x42.terye.persistence.id.ItemId;
import ch.x42.terye.persistence.id.NodeId;

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
        return null;
    }

    @Override
    public NodeState loadNode(NodeId id) throws RepositoryException {
        try {
            Result result = getRow(nodeTable, id.toString());
            String nodeTypeName = getString(result,
                    Constants.NODE_COLNAME_NODETYPE);
            return new NodeState(id, nodeTypeName);
        } catch (IOException e) {
            throw new RepositoryException("Could not load node " + id, e);
        }
    }

}
