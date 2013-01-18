package ch.x42.terye.oak.mk.test.tests;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.jackrabbit.mk.api.MicroKernel;

/**
 * Callable that commits a set of diff statements with a certain commit rate. It
 * returns the revision id after the last commit.
 */
public class CommitWorker implements Callable<String> {

    private MicroKernel mk;
    // diff statements to commit
    private Set<String> statements;
    // number of statements per commit
    private int rate;

    public CommitWorker(MicroKernel mk, Set<String> statements, int rate) {
        this.mk = mk;
        this.statements = statements;
        this.rate = rate;
    }

    @Override
    public String call() throws Exception {
        String revision = null;
        Iterator<String> iterator = statements.iterator();
        int n = 1;
        int totalN = (int) Math.ceil(statements.size() / rate);
        // iterate over all statements
        while (iterator.hasNext()) {
            // assemble diff according to commit rate
            String diff = "";
            int count = 0;
            while (count < rate && iterator.hasNext()) {
                diff += iterator.next() + " ";
                count++;
            }
            // commit batch
            revision = mk.commit("/", diff, null, "commit " + n + " of "
                    + totalN + " from worker " + hashCode());
        }
        return revision;
    }

}
