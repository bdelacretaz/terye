package ch.x42.terye.mk.hbase;

import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.jackrabbit.oak.commons.PathUtils;

/**
 * This class defines the HBase schema (i.e. the tables and their schemas) used
 * by the HBase microkernel and provides schema-related helper methods.
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

        // data type prefixes
        public static final byte TYPE_STRING_PREFIX = (byte) 0;
        public static final byte TYPE_LONG_PREFIX = (byte) 1;
        public static final byte TYPE_BOOLEAN_PREFIX = (byte) 2;

        // columns
        public static final Qualifier COL_COMMIT = new Qualifier(
                SYSTEM_PROPERTY_PREFIX, "commit");
        public static final Qualifier COL_COMMIT_POINTER = new Qualifier(
                SYSTEM_PROPERTY_PREFIX, "commitPointer");
        public static final Qualifier COL_LAST_REVISION = new Qualifier(
                SYSTEM_PROPERTY_PREFIX, "lastRevision");
        public static final Qualifier COL_CHILD_COUNT = new Qualifier(
                SYSTEM_PROPERTY_PREFIX, "childCount");

        // initial content
        private static final List<KeyValue[]> ROWS = new LinkedList<KeyValue[]>();
        static {
            // root node
            long revId = 0L;
            byte[] rowKey = Bytes.toBytes("/");
            KeyValue[] row = {
                    new KeyValue(rowKey, CF_DATA.toBytes(),
                            COL_COMMIT.toBytes(), revId, Bytes.toBytes(true)),
                    new KeyValue(rowKey, CF_DATA.toBytes(),
                            COL_LAST_REVISION.toBytes(), revId,
                            Bytes.toBytes(revId)),
                    new KeyValue(rowKey, CF_DATA.toBytes(),
                            COL_CHILD_COUNT.toBytes(), revId, Bytes.toBytes(0L))
            };
            ROWS.add(row);
        };

        private NodeTable() {
            super(TABLE_NAME, COLUMN_FAMILIES, ROWS, Integer.MAX_VALUE);
        }

        public static String pathToRowKeyString(String path) {
            // add trailing slash to path (simplifies prefix scan)
            return PathUtils.denotesRoot(path) ? path : path + "/";
        }

        public static byte[] pathToRowKey(String path) {
            return Bytes.toBytes(pathToRowKeyString(path));
        }

        public static String rowKeyToPath(byte[] rowKey) {
            String rowKeyStr = Bytes.toString(rowKey);
            return PathUtils.denotesRoot(rowKeyStr) ? rowKeyStr : rowKeyStr
                    .substring(0, rowKeyStr.length() - 1);
        }

    }

    public static final NodeTable NODES = new NodeTable();

    public static final HBaseTableDefinition[] TABLES = {
        NODES
    };

}
