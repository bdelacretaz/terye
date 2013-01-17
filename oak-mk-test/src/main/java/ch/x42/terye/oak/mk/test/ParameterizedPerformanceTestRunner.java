package ch.x42.terye.oak.mk.test;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;

/**
 * JUnit test runner that executes one performance test runner for each of the
 * parameter arrays defined by the method annotated with
 * <code>@Parameters</code>.
 */
public class ParameterizedPerformanceTestRunner extends Suite {

    // list of runners
    private List<Runner> runners;

    public ParameterizedPerformanceTestRunner(Class<?> klass) throws Throwable {
        // call super constructor with empty runner list
        super(klass, Collections.<Runner> emptyList());
        runners = new LinkedList<Runner>();
        List<Object[]> paramsList = getParametersList();
        // create one performance test runner for each parameter list
        for (Object[] params : paramsList) {
            PerformanceTestRunner r = new PerformanceTestRunner(klass);
            r.setParameters(params);
            runners.add(r);
        }
    }

    /**
     * Reads and returns a list of parameter arrays defined by the method
     * annotated with <code>@Parameters</code>.
     */
    @SuppressWarnings("unchecked")
    private List<Object[]> getParametersList() throws Throwable {
        List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(
                Parameters.class);
        if (methods.isEmpty()) {
            throw new Exception("There is no method annotated with @Parameter");
        }
        FrameworkMethod method = methods.get(0);
        int modifiers = method.getMethod().getModifiers();
        if (!Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers)) {
            throw new Exception("The method annotated with @Parameter needs "
                    + "to be public and static");
        }
        return (List<Object[]>) method.invokeExplosively(null);
    }

    @Override
    protected List<Runner> getChildren() {
        return runners;
    }

}
