package ch.x42.terye.mk.hbase;

import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * This class defines the schema of a HBase table.
 */
public class HBaseTableDefinition {

    // the table qualifier
    private final Qualifier qualifier;
    private final Qualifier[] columnFamilies;
    // the initial content of the table
    private final List<KeyValue[]> rows;

    public HBaseTableDefinition(String name, Qualifier[] columnFamilies,
            List<KeyValue[]> rows) {
        this.qualifier = new Qualifier(name);
        this.columnFamilies = columnFamilies;
        this.rows = rows;
    }

    public Qualifier getQualifier() {
        return qualifier;
    }

    public Qualifier[] getColumnFamilies() {
        return columnFamilies;
    }

    public List<KeyValue[]> getRows() {
        return rows;
    }

    /**
     * This class represents an HBase qualifier, i.e. a table, column family or
     * column qualifier. It is defined by a name which can, optionally, by
     * prefixed by a byte (useful for distinguishing system and user columns).
     */
    public static class Qualifier {

        private final String name;
        private final byte[] bytes;

        public Qualifier(String name, byte prefix) {
            this.name = name;
            byte[] bytes = Bytes.toBytes(name);
            this.bytes = new byte[bytes.length + 1];
            this.bytes[0] = prefix;
            System.arraycopy(bytes, 0, this.bytes, 1, bytes.length);
        }

        public Qualifier(String name) {
            this.name = name;
            this.bytes = Bytes.toBytes(name);
        }

        public String getString() {
            return name;
        }

        public byte[] getBytes() {
            return bytes;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(bytes);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Qualifier other = (Qualifier) obj;
            if (!Arrays.equals(bytes, other.bytes))
                return false;
            return true;
        }

    }

}
