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

/**
 * JUnit test runner measuring test performance.
 */
public class PerformanceTestRunner extends BlockJUnit4ClassRunner {

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
            int n = annotation.warmUpInvocations();
            for (int i = 0; i < n; i++) {
                statement.evaluate();
            }
            // test phase
            Timer timer = new Timer();
            n = annotation.invocations();
            for (int i = 0; i < n; i++) {
                timer.start();
                statement.evaluate();
                timer.stop();
                System.out.println("Duration: " + timer.getDuration());
            }
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
