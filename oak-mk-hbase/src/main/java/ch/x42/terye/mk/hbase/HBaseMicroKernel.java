package ch.x42.terye.mk.hbase;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Random;

import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.jackrabbit.mk.api.MicroKernel;
import org.apache.jackrabbit.mk.api.MicroKernelException;

public class HBaseMicroKernel implements MicroKernel {

    private HBaseTableManager tableMgr;

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
    public long generateNewRevisionId() {
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

}
