package ch.x42.terye.test;

import junit.framework.TestSuite;

import org.apache.jackrabbit.test.ConcurrentTestSuite;
import org.apache.jackrabbit.test.JCRTestSuite;

public class FilteredTestSuite {

    public static TestSuite suite() {
        TestSuite jcrTestSuite = new JCRTestSuite();
        ConcurrentTestSuite suite = new ConcurrentTestSuite(jcrTestSuite.getName());
        
        return jcrTestSuite;
    }

}
