package ch.x42.terye.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.jackrabbit.test.ConcurrentTestSuite;
import org.apache.jackrabbit.test.JCRTestSuite;

public class FilteredTestSuite {

    private static SortedSet<String> excludedPrefixes = new TreeSet<String>();

    public static TestSuite suite() throws IOException {
        // read file of excluded prefixes
        InputStream is = FilteredTestSuite.class
                .getResourceAsStream("/exclude.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            String prefix = line.trim();
            if (!prefix.isEmpty()) {
                excludedPrefixes.add(prefix);
            }
        }

        // create original test suite containing all tests
        TestSuite suite = new JCRTestSuite();
        // create filtered test suite to be returned
        ConcurrentTestSuite filteredSuite = new ConcurrentTestSuite(
                suite.getName());

        // nested-loop through all tests
        Enumeration enum1 = suite.tests();
        while (enum1.hasMoreElements()) {
            TestSuite suite1 = (TestSuite) enum1.nextElement();
            TestSuite filteredSuite1 = new TestSuite(suite1.getName());
            Enumeration enum2 = suite1.tests();
            while (enum2.hasMoreElements()) {
                TestSuite suite2 = (TestSuite) enum2.nextElement();
                TestSuite filteredSuite2 = new TestSuite(suite2.getName());
                Enumeration enum3 = suite2.tests();
                while (enum3.hasMoreElements()) {
                    // here we are at test level
                    TestCase test = (TestCase) enum3.nextElement();
                    String fullName = suite2.getName() + "." + test.getName();
                    if (!isExcluded(fullName)) {
                        filteredSuite2.addTest(test);
                    }
                }
                // only add sub suite if it contains some tests
                if (filteredSuite2.countTestCases() > 0) {
                    filteredSuite1.addTest(filteredSuite2);
                }
            }
            // only add sub suite if it contains some tests
            if (filteredSuite1.countTestCases() > 0) {
                filteredSuite.addTest(filteredSuite1);
            }
        }

        return filteredSuite;
    }

    private static boolean isExcluded(String name) {
        Iterator<String> iterator = excludedPrefixes.iterator();
        while (iterator.hasNext()) {
            String prefix = iterator.next();
            if (name.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

}
