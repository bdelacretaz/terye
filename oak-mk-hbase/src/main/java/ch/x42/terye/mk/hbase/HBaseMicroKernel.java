package ch.x42.terye.mk.hbase;

import static ch.x42.terye.mk.hbase.HBaseMicroKernelSchema.NODES;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.Set;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.jackrabbit.mk.api.MicroKernel;
import org.apache.jackrabbit.mk.api.MicroKernelException;
import org.apache.jackrabbit.oak.commons.PathUtils;

import ch.x42.terye.mk.hbase.HBaseMicroKernelSchema.NodeTable;

public class HBaseMicroKernel implements MicroKernel {

    private HBaseTableManager tableMgr;
    // XXX: temporarily use simple revision ids
    private long revisionCounter = 0;
    // cache the revision ids we know are valid
    private Set<Long> validRevisions = new HashSet<Long>();
    // cache the revision ids we know are not valid
    private Set<Long> invalidRevisions = new HashSet<Long>();

    public HBaseMicroKernel(HBaseAdmin admin) throws Exception {
        tableMgr = new HBaseTableManager(admin, HBaseMicroKernelSchema.TABLES);
    }

    @Override
    public String getHeadRevision() throws MicroKernelException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRevisionHistory(long since, int maxEntries, String path)
            throws MicroKernelException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String waitForCommit(String oldHeadRevisionId, long timeout)
            throws MicroKernelException, InterruptedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getJournal(String fromRevisionId, String toRevisionId,
            String path) throws MicroKernelException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String diff(String fromRevisionId, String toRevisionId, String path,
            int depth) throws MicroKernelException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean nodeExists(String path, String revisionId)
            throws MicroKernelException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public long getChildNodeCount(String path, String revisionId)
            throws MicroKernelException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getNodes(String path, String revisionId, int depth,
            long offset, int maxChildNodes, String filter)
            throws MicroKernelException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String commit(String path, String jsonDiff, String revisionId,
            String message) throws MicroKernelException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String branch(String trunkRevisionId) throws MicroKernelException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String merge(String branchRevisionId, String message)
            throws MicroKernelException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getLength(String blobId) throws MicroKernelException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int read(String blobId, long pos, byte[] buff, int off, int length)
            throws MicroKernelException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String write(InputStream in) throws MicroKernelException {
        // TODO Auto-generated method stub
        return null;
    }

    /* private methods */

    /**
     * This method generates a new revision id. A revision id is a signed (but
     * non-negative) long composed of the concatenation (left-to-right) of
     * following fields:
     * 
     * <pre>
     *  --------------------------------------------------------------
     * |                    timestamp |    machine_id |       counter |
     *  --------------------------------------------------------------
     *  
     * - timestamp: signed int, 4 bytes, [0, Integer.MAX_VALUE]
     * - machine_id: unsigned short, 2 bytes, [0, 65535]
     * - counter: unsigned short, 2 bytes, [0, 65535]
     * </pre>
     * 
     * The unit of the timestamp is seconds and since we only have 4 bytes
     * available, we base it on an artificial epoch (defined in the method) in
     * order to have more values available. The machine id is generated using
     * the hashcode of the MAC address string and not guaranteed to be unique
     * (but very likely so). If the same machine commits a revision within the
     * same second, then the counter field is used in order to create a unique
     * revision id.
     * 
     * @return a new and unique revision id
     */
    private long generateNewRevisionId() {
        // XXX: temporarily use simple revision ids
        if (true) {
            return ++revisionCounter;
        }

        // NEVER EVER CHANGE THIS VALUE
        final long epoch = 1358845000;

        // timestamp
        long seconds = System.currentTimeMillis() / 1000 - epoch;
        long timestamp = seconds << 32;

        // machine id
        long machineId;
        try {
            NetworkInterface network = NetworkInterface
                    .getByInetAddress(InetAddress.getLocalHost());
            byte[] address = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < address.length; i++) {
                sb.append(String.format("%02X", address[i]));
            }
            machineId = sb.hashCode();
        } catch (Throwable e) {
            machineId = new Random().nextInt();
        }
        machineId = (machineId << 16) & Long.decode("0x00000000FFFF0000");

        // counter
        // XXX: tbd
        long counter = 0;

        // assemble and return revision id
        return timestamp | machineId | counter;
    }

    private Result[] getNodeRows(Collection<String> paths) throws IOException {
        List<Get> batch = new LinkedList<Get>();
        for (String path : paths) {
            Get get = new Get(NodeTable.pathToRowKey(path));
            get.setMaxVersions();
            batch.add(get);
        }
        return tableMgr.get(NODES).get(batch);
    }

    private Result getNodeRow(String path) throws IOException {
        List<String> paths = new LinkedList<String>();
        paths.add(path);
        Result[] results = getNodeRows(paths);
        if (results.length == 0) {
            return null;
        }
        return results[0];
    }

    /**
     * This method parses the specified HBase results into nodes.
     * 
     * @param results the HBase results
     * @return a map mapping paths to the corresponding node
     */
    private Map<String, Node> parseNodes(Result[] results) throws IOException {
        Map<String, Node> nodes = new LinkedHashMap<String, Node>();
        // loop through all nodes
        for (Result result : results) {
            String path = NodeTable.rowKeyToPath(result.getRow());
            Node node = new Node(path);
            boolean nodeExists = false;
            NavigableMap<byte[], NavigableMap<Long, byte[]>> columns = result
                    .getMap().get(NodeTable.CF_DATA.toBytes());
            // if the node contains the "commit" column...
            if (columns.containsKey(NodeTable.COL_COMMIT.toBytes())) {
                // ...then cache all of its revisions as valid ones
                validRevisions.addAll(columns.get(
                        NodeTable.COL_COMMIT.toBytes()).keySet());
            }
            // loop through all columns
            for (byte[] column : columns.keySet()) {
                // skip columns we're not interested in
                if (Arrays.equals(column, NodeTable.COL_COMMIT.toBytes())
                        || Arrays.equals(column,
                                NodeTable.COL_COMMIT_POINTER.toBytes())) {
                    continue;
                }
                // get the most recent value with a valid revision
                byte[] value = null;
                NavigableMap<Long, byte[]> revisions = columns.get(column);
                // loop through all revisions, starting with the highest
                // XXX: verify order of keys in the key set
                for (Long revision : revisions.keySet()) {
                    if (revisionIsValid(revision, result)) {
                        // we have found a valid revision for that column
                        value = revisions.get(revision);
                        break;
                    }
                }
                // we haven't found a valid revision for that column
                if (value == null) {
                    continue;
                }
                nodeExists = true;
                if (Arrays
                        .equals(column, NodeTable.COL_LAST_REVISION.toBytes())) {
                    node.setLastRevision(Bytes.toLong(value));
                }
            }
            if (nodeExists) {
                nodes.put(path, node);
            }
        }
        return nodes;
    }

    /**
     * This method checks if a revision is valid. It first does a cache lookup
     * and if necessary then reads the specified commit root in order to verify
     * the validity of the revision.
     * 
     * @param revisionId the revision id to validate
     * @param result the result where
     * @return true if the revision is valid, false otherwise
     */
    private boolean revisionIsValid(long revisionId, Result result)
            throws IOException {
        // check cache of valid revisions
        if (validRevisions.contains(revisionId)) {
            return true;
        }
        // check cache of invalid revisions
        if (invalidRevisions.contains(revisionId)) {
            return false;
        }
        boolean valid = true;
        NavigableMap<Long, byte[]> commit = result.getMap()
                .get(NodeTable.CF_DATA.toBytes())
                .get(NodeTable.COL_COMMIT.toBytes());
        if (commit == null || !commit.containsKey(revisionId)) {
            valid = false;
        } else {
            // read commit root
            NavigableMap<Long, byte[]> pointer = result.getMap()
                    .get(NodeTable.CF_DATA.toBytes())
                    .get(NodeTable.COL_COMMIT_POINTER.toBytes());
            int ptr = Bytes.toInt(pointer.get(revisionId));
            String path = NodeTable.rowKeyToPath(result.getRow());
            String commitRoot = PathUtils.getAncestorPath(path, ptr);
            result = getNodeRow(commitRoot);
            // commit root node doesn't exist
            if (result == null) {
                valid = false;
            } else {
                commit = result.getMap().get(NodeTable.CF_DATA.toBytes())
                        .get(NodeTable.COL_COMMIT.toBytes());
                if (commit == null || !commit.containsKey(revisionId)) {
                    // no commit root for this revision
                    valid = false;
                }
            }
        }
        // update cache
        if (valid) {
            validRevisions.add(revisionId);
        } else {
            invalidRevisions.add(revisionId);
        }
        return valid;
    }

}
