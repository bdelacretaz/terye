package ch.x42.terye;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        PathTest.class, SessionTest.class, RootNodeTest.class, NodeTest.class,
        PropertyTest.class
})
public class JCRTests {

}
