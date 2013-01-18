package ch.x42.terye.oak.mk.test.fixtures;

import org.apache.jackrabbit.mk.api.MicroKernel;

/**
 * Interface definition for test fixtures that are passed as a parameter to
 * parameterized tests.
 */
public interface MicroKernelTestFixture {

    public MicroKernel createMicroKernel() throws Exception;

    public void setUpBeforeTest() throws Exception;

    public void tearDownAfterTest() throws Exception;

}
