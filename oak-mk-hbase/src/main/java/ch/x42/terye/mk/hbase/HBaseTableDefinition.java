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
    // the number of versions that should be kept for rows of this table
    private final int maxVersions;

    public HBaseTableDefinition(String name, Qualifier[] columnFamilies,
            List<KeyValue[]> rows, int maxVersions) {
        this.qualifier = new Qualifier(name);
        this.columnFamilies = columnFamilies;
        this.rows = rows;
        this.maxVersions = maxVersions;
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

    public int getMaxVersions() {
        return maxVersions;
    }

    /**
     * This class represents an HBase qualifier, i.e. a table, column family or
     * column qualifier. It is defined by a sequence of bytes which can,
     * optionally, be prefixed by another sequence of bytes (useful e.g. for
     * distinguishing between system and user columns).
     */
    public static class Qualifier {

        private final byte[] prefix;
        private final byte[] qualifier;
        // concatenation of prefix and qualifier
        private byte[] bytes;
        // holds the string representation if this qualifier was constructed
        // with a human-readable qualifier (only used in toString())
        private final String qualifierString;

        private Qualifier(byte[] prefix, byte[] qualifier,
                String qualifierString) {
            this.prefix = prefix;
            this.qualifier = qualifier;
            this.qualifierString = qualifierString;
        }

        public Qualifier(byte[] prefix, String qualifier) {
            this(prefix, Bytes.toBytes(qualifier), qualifier);
        }

        public Qualifier(byte prefix, String qualifier) {
            this(new byte[] {
                prefix
            }, Bytes.toBytes(qualifier), qualifier);
        }

        public Qualifier(String qualifier) {
            this(new byte[0], Bytes.toBytes(qualifier), qualifier);
        }

        public String toString() {
            return qualifierString == null ? qualifierString : super.toString();
        }

        public byte[] toBytes() {
            if (bytes == null) {
                bytes = new byte[prefix.length + qualifier.length];
                System.arraycopy(prefix, 0, bytes, 0, prefix.length);
                System.arraycopy(qualifier, 0, bytes, prefix.length,
                        qualifier.length);
            }
            return bytes;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(toBytes());
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
            if (!Arrays.equals(toBytes(), other.toBytes()))
                return false;
            return true;
        }

    }

}
