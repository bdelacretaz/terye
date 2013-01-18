package ch.x42.terye.oak.mk.test.tests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
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
 * TREE_BRANCHING_FACTOR.
 */
public class MicroKernelConcurrentAddTest extends MicroKernelPerformanceTest {

    private static final int NB_THREADS = Runtime.getRuntime()
            .availableProcessors() * 2;
    private static final int TREE_HEIGHT = 6;
    private static final int TREE_BRANCHING_FACTOR = 4;

    public List<CommitWorker> workers;

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
        logger.debug("Creating diff statements");

        // create a set of diff statements for each worker
        ArrayList<Set<String>> sets = new ArrayList<Set<String>>(NB_THREADS);
        // keep track of first level nodes
        Set<String> nodes = new HashSet<String>();
        for (int i = 0; i < NB_THREADS; i++) {
            SortedSet<String> statements = new TreeSet<String>();
            // generate unique name for first level node
            String name;
            do {
                name = generateRandomString(3, 6);
            } while (nodes.contains(name));
            // diff statement for first level node
            statements.add("+\"" + name + "\":{}");
            // generate diff statements for the subtree
            generateTreeDiffStatements(name, TREE_HEIGHT - 1,
                    TREE_BRANCHING_FACTOR, statements);
            sets.add(statements);
        }

        // create workers
        workers = new LinkedList<CommitWorker>();
        for (int i = 0; i < NB_THREADS; i++) {
            MicroKernel mk = createMicroKernel();
            CommitWorker cw = new CommitWorker(mk, sets.get(i), 1);
            workers.add(cw);
        }
    }

    @PerformanceTest
    public void test() throws Exception {
        // run them
        logger.debug("Starting concurrent worker execution");
        ExecutorService executor = Executors.newFixedThreadPool(NB_THREADS);
        List<Future<String>> futures = new LinkedList<Future<String>>();
        for (CommitWorker worker : workers) {
            futures.add(executor.submit(worker));
        }
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
        // get return value of workers (rethrows exceptions that might have
        // happened during execution)
        for (Future<String> future : futures) {
            future.get();
        }
        logger.debug("Worker execution done");
    }

    @After
    public void tearDownTest() {
        workers = null;
        System.gc();
    }

    /**
     * Recursively generates diff statements for adding a subtree of specified
     * height and with branching factor. The resulting diff statements are
     * collected in the specified set.
     */
    private void generateTreeDiffStatements(String prefix, int height, int k,
            Set<String> stmts) {
        if (height == 0) {
            // done
            return;
        }
        // keep track of node names
        Set<String> nodes = new HashSet<String>();
        // generate k children
        for (int i = 0; i < k; i++) {
            // generate unique node name
            String name;
            do {
                name = generateRandomString(3, 6);
            } while (nodes.contains(name));
            String path = prefix + "/" + name;
            // diff statement
            stmts.add("+\"" + path + "\":{}");
            // call recursively
            generateTreeDiffStatements(path, height - 1, k, stmts);
        }
    }

    /**
     * Generates random strings within a given length range and composed of
     * lowercase alphabetic characters.
     */
    private String generateRandomString(int minLength, int maxLength) {
        int length = minLength
                + (int) (Math.round(Math.random() * (maxLength - minLength)));
        String str = "";
        for (int i = 0; i < length; i++) {
            // random char a-z
            char c = (char) (97 + (int) (Math.random() * 26));
            str += c;
        }
        return str;
    }

}
