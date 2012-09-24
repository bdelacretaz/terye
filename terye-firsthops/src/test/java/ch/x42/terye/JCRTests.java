package ch.x42.terye;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ NodeTest.class, PropertyTest.class, SessionTest.class })
public class JCRTests {

}
