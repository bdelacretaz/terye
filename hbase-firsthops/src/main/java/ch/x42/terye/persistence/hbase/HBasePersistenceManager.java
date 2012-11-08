package ch.x42.terye.persistence.hbase;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;

import ch.x42.terye.persistence.PersistenceManager;

public class HBasePersistenceManager implements PersistenceManager {

    private static final String NODE_TABLE_NAME = "nodes";
    private static final String PROPERTY_TABLE_NAME = "properties";
    private static final String COLUMN_FAMILY = "d";

    private static HBasePersistenceManager instance;
    private Configuration config;
    private HBaseAdmin admin;
    private HTable nodeTable;
    private HTable propertyTable;

    private HBasePersistenceManager() throws RepositoryException {
        config = HBaseConfiguration.create();
        try {
            admin = new HBaseAdmin(config);
            nodeTable = getOrCreateTable(NODE_TABLE_NAME);
            propertyTable = getOrCreateTable(PROPERTY_TABLE_NAME);
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
                Bytes.toBytes(COLUMN_FAMILY));
        tableDesc.addFamily(colDesc);
        admin.createTable(tableDesc);
    }

}
