package ch.x42.terye.persistence.hbase;

import org.apache.hadoop.hbase.util.Bytes;

public class Constants {

    public static final String ITEMS_TABLE_NAME = "items";

    public static final byte[] ITEMS_CFNAME_DATA = Bytes.toBytes("d");

    // common columns
    public static final byte[] ITEMS_COLNAME_ITEMTYPE = Bytes.toBytes("it");

    // node columns
    public static final byte[] ITEMS_COLNAME_NODETYPE = Bytes.toBytes("nt");
    public static final byte[] ITEMS_COLNAME_CHILDNODES = Bytes.toBytes("c");
    public static final byte[] ITEMS_COLNAME_PROPERTIES = Bytes.toBytes("p");

    // property columns
    public static final byte[] ITEMS_COLNAME_TYPE = Bytes.toBytes("t");
    public static final byte[] ITEMS_COLNAME_VALUE = Bytes.toBytes("v");

}
