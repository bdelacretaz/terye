package ch.x42.terye.mk.hbase;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;

import ch.x42.terye.mk.hbase.HBaseTableDefinition.Qualifier;

/**
 * This class manages a set of tables specified by their table definitions.
 */
public class HBaseTableManager {

    private final HBaseAdmin admin;
    private final Map<Qualifier, HTable> tables;

    public HBaseTableManager(HBaseAdmin admin, HBaseTableDefinition[] tables)
            throws Exception {
        this.admin = admin;
        this.tables = new HashMap<Qualifier, HTable>();
        for (HBaseTableDefinition table : tables) {
            this.tables.put(table.getQualifier(), initTable(table));
        }
    }

    private HTable initTable(HBaseTableDefinition table) throws Exception {
        HTable hTable;
        try {
            hTable = new HTable(admin.getConfiguration(), table.getQualifier()
                    .toBytes());
        } catch (TableNotFoundException e) {
            // create table
            admin.createTable(createDescriptor(table));
            hTable = new HTable(admin.getConfiguration(), table.getQualifier()
                    .toBytes());
            // add initial content
            List<Put> batch = new LinkedList<Put>();
            for (KeyValue[] row : table.getRows()) {
                Put put = new Put(row[0].getRow());
                for (KeyValue keyValue : row) {
                    put.add(keyValue);
                }
                batch.add(put);
            }
            hTable.batch(batch);
        }
        return hTable;
    }

    private HTableDescriptor createDescriptor(HBaseTableDefinition table) {
        HTableDescriptor tableDesc = new HTableDescriptor(table.getQualifier()
                .toBytes());
        for (Qualifier columnFamily : table.getColumnFamilies()) {
            HColumnDescriptor colDesc = new HColumnDescriptor(
                    columnFamily.toBytes());
            colDesc.setMaxVersions(table.getMaxVersions());
            tableDesc.addFamily(colDesc);
        }
        return tableDesc;
    }

    /**
     * Returns the HTable corresponding to the specified table definition.
     */
    public HTable get(HBaseTableDefinition table) {
        return tables.get(table.getQualifier());
    }

    public void dropAllTables() throws IOException {
        for (HBaseTableDefinition schema : HBaseMicroKernelSchema.TABLES) {
            admin.disableTable(schema.getQualifier().toBytes());
            admin.deleteTable(schema.getQualifier().toBytes());
        }
    }

    public void dispose() throws IOException {
        for (HBaseTableDefinition schema : HBaseMicroKernelSchema.TABLES) {
            tables.get(schema.getQualifier()).close();
        }
        admin.close();
    }

}
