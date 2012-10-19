package ch.x42.terye;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import org.junit.Before;
import org.junit.Test;

import ch.x42.terye.utils.DateUtils;

public class QueryTest extends BaseTest {

    private QueryManager qm;
    private Node post1;
    private Node post2;
    private Node post3;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        qm = session.getWorkspace().getQueryManager();

        // add some items using the API (so that they are indexed)

        post1 = root.addNode("post1");
        post1.setProperty("content", "This is a blog post");
        post1.setProperty("published", true);
        post1.setProperty("comments", 10);
        Calendar cal1 = new GregorianCalendar(2012, Calendar.OCTOBER, 11);
        post1.setProperty("date", cal1);
        post1.setProperty("rating", 4.5);

        post2 = root.addNode("post2");
        post2.setProperty("content",
                "The quick brown fox jumps over the lazy dog");
        post2.setProperty("published", true);
        post2.setProperty("comments", 3);
        Calendar cal2 = new GregorianCalendar(2012, Calendar.OCTOBER, 15);
        post2.setProperty("date", cal2);
        post2.setProperty("rating", 3.9);

        post3 = root.addNode("post3");
        post3.setProperty("content", "A draft for another blog post");
        post3.setProperty("published", false);
        post3.setProperty("comments", 0);
        Calendar cal3 = new GregorianCalendar(2012, Calendar.OCTOBER, 19);
        post3.setProperty("date", cal3);
        post3.setProperty("rating", 0.0);
        post3.setProperty("description", "hot dog");

        session.save();
    }

    @Test
    public void testStringQuery() throws InvalidQueryException,
            RepositoryException {
        NodeIterator iterator = executeQuery("content:dog");
        assertEquals(1, iterator.getSize());
        assertNodesEqual(post2, iterator.nextNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testStringQuery2() throws InvalidQueryException,
            RepositoryException {
        NodeIterator iterator = executeQuery("content:(blog post)");
        assertEquals(2, iterator.getSize());
        assertNodesEqual(post1, iterator.nextNode());
        assertNodesEqual(post3, iterator.nextNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testStringQuery3() throws InvalidQueryException,
            RepositoryException {
        NodeIterator iterator = executeQuery("content:\"a blog\"");
        assertEquals(1, iterator.getSize());
        assertNodesEqual(post1, iterator.nextNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testStringQuery4() throws InvalidQueryException,
            RepositoryException {
        NodeIterator iterator = executeQuery("content:*row*");
        assertEquals(1, iterator.getSize());
        assertNodesEqual(post2, iterator.nextNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testLongQuery() throws InvalidQueryException,
            RepositoryException {
        NodeIterator iterator = executeQuery("comments:10");
        assertEquals(1, iterator.getSize());
        assertNodesEqual(post1, iterator.nextNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDoubleQuery() throws InvalidQueryException,
            RepositoryException {
        NodeIterator iterator = executeQuery("rating:3.9");
        assertEquals(1, iterator.getSize());
        assertNodesEqual(post2, iterator.nextNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testBooleanQuery() throws InvalidQueryException,
            RepositoryException {
        NodeIterator iterator = executeQuery("published:false");
        assertEquals(1, iterator.getSize());
        assertNodesEqual(post3, iterator.nextNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testBooleanQuery2() throws InvalidQueryException,
            RepositoryException {
        NodeIterator iterator = executeQuery("published:True");
        assertEquals(2, iterator.getSize());
        assertNodesEqual(post1, iterator.nextNode());
        assertNodesEqual(post2, iterator.nextNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDateQuery() throws InvalidQueryException,
            RepositoryException {
        Calendar cal = new GregorianCalendar(2012, Calendar.OCTOBER, 11);
        DateFormat format = new SimpleDateFormat(DateUtils.FORMAT);
        // Solr stores dates in UTC
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateStr = format.format(cal.getTime());
        NodeIterator iterator = executeQuery("date:\"" + dateStr + "\"");
        assertEquals(1, iterator.getSize());
        assertNodesEqual(post1, iterator.nextNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAny() throws InvalidQueryException, RepositoryException {
        NodeIterator iterator = executeQuery("any:dog");
        assertEquals(2, iterator.getSize());
        assertNodesEqual(post2, iterator.nextNode());
        assertNodesEqual(post3, iterator.nextNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNegated() throws InvalidQueryException, RepositoryException {
        NodeIterator iterator = executeQuery("-content:post");
        assertEquals(1, iterator.getSize());
        assertNodesEqual(post2, iterator.nextNode());
        assertFalse(iterator.hasNext());
    }

    private NodeIterator executeQuery(String statement)
            throws InvalidQueryException, RepositoryException {
        Query query = qm.createQuery(statement, null);
        return query.execute().getNodes();
    }

    private void assertNodesEqual(Node expected, Node actual)
            throws RepositoryException {
        assertEquals(expected.getPath(), actual.getPath());
        assertEquals(expected.getName(), actual.getName());
    }

}
