package ch.x42.terye.persistence.hbase;

import org.apache.hadoop.hbase.util.Bytes;

public class Constants {

    public static final String NODE_TABLE_NAME = "nodes";
    public static final byte[] NODE_COLNAME_NODETYPE = Bytes.toBytes("nt");

    public static final String PROPERTY_TABLE_NAME = "properties";
    public static final byte[] PROPERTY_COLNAME_TYPE = Bytes.toBytes("t");
    public static final byte[] PROPERTY_COLNAME_VALUE = Bytes.toBytes("v");

    /**
     * Name of the column family used for all tables and columns
     */
    public static final byte[] COLUMN_FAMILY = Bytes.toBytes("d");

}
