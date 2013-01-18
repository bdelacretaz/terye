package ch.x42.terye.oak.mk.test.tests;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jackrabbit.mk.api.MicroKernel;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.x42.terye.oak.mk.test.ParameterizedPerformanceTestRunner;
import ch.x42.terye.oak.mk.test.fixtures.InMemoryMKTestFixture;
import ch.x42.terye.oak.mk.test.fixtures.MicroKernelTestFixture;

/**
 * This is the base class for all MicroKernel performance tests. When run, the
 * test is executed once for each parameter array returned by the
 * getParameters() method.
 */
@RunWith(ParameterizedPerformanceTestRunner.class)
public abstract class MicroKernelPerformanceTest {

    @Parameters
    public static Collection<Object[]> getParameters() {
        List<Object[]> parameters = new LinkedList<Object[]>();
        // execute tests with the MongoDB microkernel
        // parameters.add(new Object[] {
        // new MongoMKTestFixture()
        // });
        // execute tests with the in-memory microkernel
        parameters.add(new Object[] {
            new InMemoryMKTestFixture()
        });
        return parameters;
    }

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private MicroKernelTestFixture fixture;

    protected MicroKernelPerformanceTest(MicroKernelTestFixture fixture) {
        this.fixture = fixture;
    }

    protected MicroKernel createMicroKernel() throws Exception {
        return fixture.createMicroKernel();
    }

    @Before
    public final void setUp() throws Exception {
        logger.debug("calling test setup fixture method");
        fixture.setUpBeforeTest();
    }

    @After
    public final void tearDown() throws Exception {
        logger.debug("calling test teardown fixture method");
        fixture.tearDownAfterTest();
    }

}
