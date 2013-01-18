package ch.x42.terye.oak.mk.test.fixtures;

import org.apache.jackrabbit.mk.api.MicroKernel;
import org.apache.jackrabbit.mk.core.MicroKernelImpl;

public class InMemoryMKTestFixture implements MicroKernelTestFixture {

    @Override
    public MicroKernel createMicroKernel() throws Exception {
        return new MicroKernelImpl();
    }

    @Override
    public void setUpBeforeTest() throws Exception {
        // nothing to do
    }

    @Override
    public void tearDownAfterTest() throws Exception {
        // nothing to do
    }

    @Override
    public String toString() {
        return "MicroKernelImpl";
    }

}
