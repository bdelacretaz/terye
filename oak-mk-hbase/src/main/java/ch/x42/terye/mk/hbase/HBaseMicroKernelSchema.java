package ch.x42.terye.mk.hbase;

import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * This class defines the HBase schema (i.e. the tables and their schemas) used
 * by the HBase microkernel.
 */
public class HBaseMicroKernelSchema {

    public static final class NodeTable extends HBaseTableDefinition {

        // table name
        public static final String TABLE_NAME = "nodes";

        // column families
        public static final Qualifier CF_DATA = new Qualifier("data");
        private static final Qualifier[] COLUMN_FAMILIES = new Qualifier[] {
            CF_DATA
        };

        // column prefixes
        public static final byte SYSTEM_PROPERTY_PREFIX = (byte) 0;
        public static final byte DATA_PROPERTY_PREFIX = (byte) 1;

        // columns
        public static final Qualifier COL_DELETED = new Qualifier(
                SYSTEM_PROPERTY_PREFIX, "deleted");

        // initial content
        private static final List<KeyValue[]> ROWS = new LinkedList<KeyValue[]>();
        static {
            KeyValue[] row = {
                new KeyValue(Bytes.toBytes("/"), CF_DATA.toBytes(),
                        COL_DELETED.toBytes(), 0L, Bytes.toBytes(false)),
            };
            ROWS.add(row);
        };

        private NodeTable() {
            super(TABLE_NAME, COLUMN_FAMILIES, ROWS, Integer.MAX_VALUE);
        }

    }

    public static final NodeTable NODES = new NodeTable();

    public static final HBaseTableDefinition[] TABLES = {
        NODES
    };

}
