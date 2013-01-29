package ch.x42.terye.oak.mk.test.tests;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.jackrabbit.mk.api.MicroKernel;
import org.junit.After;
import org.junit.Before;

import ch.x42.terye.oak.mk.test.PerformanceTest;
import ch.x42.terye.oak.mk.test.fixtures.MicroKernelTestFixture;

/**
 * This test measures the performance of many threads concurrently adding a
 * large number of nodes. The test creates NB_THREADS threads, each of which
 * commits a subtree of height TREE_HEIGHT and branching factor
 * TREE_BRANCHING_FACTOR. The workers commit their nodes in batches of
 * COMMIT_RATE nodes per commit call.
 */
public class MicroKernelConcurrentAddTest extends MicroKernelPerformanceTest {

    private static final int NB_THREADS = Runtime.getRuntime()
            .availableProcessors();
    private static final int TREE_HEIGHT = 5;
    private static final int TREE_BRANCHING_FACTOR = 6;
    private static final int COMMIT_RATE = 500;

    public List<Callable<String>> workers;

    public MicroKernelConcurrentAddTest(MicroKernelTestFixture ctx) {
        super(ctx);
    }

    @Before
    public void setUpTest() throws Exception {
        // log info
        int nbPerThread = (int) ((Math.pow(TREE_BRANCHING_FACTOR,
                TREE_HEIGHT + 1) - 1) / (TREE_BRANCHING_FACTOR - 1));
        int nbTotal = NB_THREADS * nbPerThread;
        logger.debug("Number of threads: " + NB_THREADS);
        logger.debug("Number of nodes per thread: " + nbPerThread);
        logger.debug("Total number of nodes: " + nbTotal);
        logger.debug("Creating workers");

        // create workers
        workers = new LinkedList<Callable<String>>();
        for (int i = 0; i < NB_THREADS; i++) {
            MicroKernel mk = createMicroKernel();
            Callable<String> worker = new SubtreeCommitter(mk, "node_" + i,
                    TREE_HEIGHT, TREE_BRANCHING_FACTOR, COMMIT_RATE);
            workers.add(worker);
            // XXX: temporarily commit first node here to avoid any conflict
            mk.commit("/", "+\"node_" + i + "\":{}", null, "");
        }
    }

    @PerformanceTest(nbWarmupRuns = 3, nbRuns = 3)
    public void test() throws Exception {
        // run them
        logger.debug("Starting concurrent worker execution");
        ExecutorService executor = Executors.newFixedThreadPool(NB_THREADS);
        List<Future<String>> futures = new LinkedList<Future<String>>();
        for (Callable<String> worker : workers) {
            futures.add(executor.submit(worker));
        }
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
        // get return value of workers (this forces exceptions that might have
        // happened during execution to be re-thrown)
        for (Future<String> future : futures) {
            future.get();
        }
        logger.debug("All workers are done");
    }

    @After
    public void tearDownTest() {
        workers = null;
        System.gc();
    }

    /**
     * This callable commits a subtree defined by the constructor arguments with
     * a specified commit rate. The name of the nodes will be the concatenation
     * of a constant prefix and a number corresponding to the zero-based
     * numbering of the child nodes of a given node.
     */
    private class SubtreeCommitter implements Callable<String> {

        private static final String NODE_NAME = "node_";

        private MicroKernel mk;
        private String root;
        private int height;
        private int branchingFactor;
        private int rate;

        public SubtreeCommitter(MicroKernel mk, String root, int height,
                int branchingFactor, int rate) {
            this.mk = mk;
            this.root = root;
            this.height = height;
            this.branchingFactor = branchingFactor;
            this.rate = rate;
        }

        @Override
        public String call() throws Exception {
            String r = null;
            String batch = "";
            int batchCount = 0;
            // loop through all levels
            // XXX: temporarily skip first level node
            for (int i = 1; i <= height; i++) {
                // number of nodes on this level
                int nbNodes = (int) Math.pow(branchingFactor, i);
                // loop through all nodes on this level
                for (int j = 0; j < nbNodes; j++) {
                    if (batchCount == rate) {
                        // commit batch
                        r = mk.commit("/", batch, null, "");
                        batch = "";
                        batchCount = 0;
                    }
                    // add new statement to batch for later commit
                    batch += "+\"" + generatePath(i, j) + "\":{} ";
                    batchCount++;
                }
            }
            // commit remaining statements, if any
            if (batchCount > 0) {
                // commit batch
                r = mk.commit("/", batch, null, "");
            }
            return r;
        }

        /**
         * This method generates the path for a node in the subtree rooted at
         * 'root'.
         * 
         * @param level the level of the node (0 being the same level as the
         *            root of the subtree)
         * @param index the zero-based index of the node on the specified level
         * @return the absolute path of the specified node
         */
        private String generatePath(int level, int index) {
            String path = root;
            if (level == 0) {
                return root;
            }
            // loop through all levels, starting at first sublevel of the root
            for (int i = 1; i <= level; i++) {
                // number of nodes at level 'level' that share the same ancestor
                // node on the current level
                int n = ((int) Math.pow(branchingFactor, level - i));
                path += "/" + NODE_NAME + ((index / n) % branchingFactor);
            }
            return path;
        }

    }

}
