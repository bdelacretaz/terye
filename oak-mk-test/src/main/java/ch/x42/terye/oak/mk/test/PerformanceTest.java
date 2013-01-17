package ch.x42.terye.oak.mk.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for test methods that are to be executed using a performance test
 * runner.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PerformanceTest {

    /**
     * The number of times the test should be executed in the warm-up phase.
     */
    int warmUpInvocations() default 0;

    /**
     * The number of times the test should be executed.
     */
    int invocations() default 1;

}