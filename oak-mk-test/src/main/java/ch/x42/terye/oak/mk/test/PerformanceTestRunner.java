package ch.x42.terye.oak.mk.test;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JUnit test runner measuring test performance.
 */
public class PerformanceTestRunner extends BlockJUnit4ClassRunner {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // performance test methods
    private List<FrameworkMethod> methods;
    // arguments that should be passed to the constructor of the test class
    private Object[] parameters;

    public PerformanceTestRunner(Class<?> klass) throws InitializationError,
            InstantiationException, IllegalAccessException {
        super(klass);
        methods = new LinkedList<FrameworkMethod>();
        // by default use no arguments
        parameters = null;
        // get all methods annotated with @PerformanceTest
        for (FrameworkMethod method : getTestClass().getAnnotatedMethods(
                PerformanceTest.class)) {
            methods.add(method);
        }
    }

    /**
     * Set the parameters to be passed to the constructor of the test class. The
     * number of parameters and types must correspond to the signature of the
     * constructor.
     */
    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    /**
     * We need to override this method since otherwise the validation fails as
     * computeTestMethods returns an empty list while calling the super
     * constructor.
     */
    @Override
    protected void validateInstanceMethods(List<Throwable> errors) {
        validatePublicVoidNoArgMethods(After.class, false, errors);
        validatePublicVoidNoArgMethods(Before.class, false, errors);
        validateTestMethods(errors);
    }

    /**
     * Since we might pass parameters to the constructor of the test class we
     * have to override this validation method.
     */
    @Override
    protected void validateConstructor(List<Throwable> errors) {
        validateOnlyOneConstructor(errors);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return methods;
    }

    /**
     * We override this method in order to implement the performance test
     * behavior.
     */
    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        Description description = describeChild(method);
        // ignore methods annotated with @Ignore
        if (method.getAnnotation(Ignore.class) != null) {
            notifier.fireTestIgnored(description);
            return;
        }
        // get statement corresponding to the test method
        Statement statement = methodBlock(method);
        // get the annotation
        PerformanceTest annotation = method
                .getAnnotation(PerformanceTest.class);
        EachTestNotifier eachNotifier = new EachTestNotifier(notifier,
                description);
        eachNotifier.fireTestStarted();
        try {
            // warm-up phase
            Timer timer = new Timer();
            int n = annotation.nbWarmupRuns();
            logger.debug("-------------------------------------");
            logger.debug("WARMUP PHASE (" + n + " invocations):");
            logger.debug("-------------------------------------");
            for (int i = 0; i < n; i++) {
                logger.debug("Test run " + (i + 1) + " of " + n);
                timer.start();
                statement.evaluate();
                timer.stop();
                logger.debug("Execution time: " + timer.getLastDuration());
            }
            // test phase
            timer = new Timer();
            n = annotation.nbRuns();
            logger.debug("-----------------------------------");
            logger.debug("TEST PHASE (" + n + " invocations):");
            logger.debug("-----------------------------------");
            for (int i = 0; i < n; i++) {
                logger.debug("Test run " + (i + 1) + " of " + n);
                timer.start();
                statement.evaluate();
                timer.stop();
                logger.debug("Execution time: " + timer.getLastDuration());
            }
            logger.debug("--------");
            logger.debug("RESULTS:");
            logger.debug("--------");
            logger.debug("Number of test runs: " + n);
            logger.debug("Average execution time: "
                    + timer.getAverageDuration());
            logger.debug("Minimum execution time: " + timer.getMinDuration());
            logger.debug("Maximum execution time: " + timer.getMaxDuration());
        } catch (AssumptionViolatedException e) {
            eachNotifier.addFailedAssumption(e);
        } catch (Throwable e) {
            eachNotifier.addFailure(e);
        } finally {
            eachNotifier.fireTestFinished();
        }
    }

    private String getParameterString() {
        if (parameters == null) {
            return "";
        }
        String str = " [parameters=";
        for (int i = 0; i < parameters.length; i++) {
            str += parameters[i].toString();
            if (i + 1 < parameters.length) {
                str += ",";
            }
        }
        str += "]";
        return str;
    }

    @Override
    protected String getName() {
        return getTestClass().getJavaClass().getSimpleName()
                + getParameterString();
    }

    @Override
    protected String testName(FrameworkMethod method) {
        return super.testName(method) + getParameterString();
    }

    @Override
    protected Object createTest() throws Exception {
        if (parameters != null) {
            return getTestClass().getOnlyConstructor().newInstance(parameters);
        }
        return getTestClass().getOnlyConstructor().newInstance();
    }

}
