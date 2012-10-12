package ch.x42.terye;

import java.net.UnknownHostException;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.junit.After;
import org.junit.Before;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class BaseTest {

    private DBCollection collection;
    protected Repository repository;
    protected Session session;
    protected Node root;

    @Before
    public void setUp() throws Exception {
        initDB();
        repository = new RepositoryImpl();
        session = repository.login();
        root = session.getRootNode();
    }

    /**
     * Populates the db with some test nodes and properties.
     */
    private void initDB() throws UnknownHostException {
        collection = new ConfiguredMongo()
            .getDB(ConfiguredMongo.MONGO_DB_NAME)
            .getCollection(ConfiguredMongo.TERYE_MONGO_COLLECTION);
        
        final String[] items = {
                "{ 'path' : '/', 'children' : ['/node1'], 'properties' : ['/property1'], 'type' : 0 }",
                "{ 'path' : '/node1', 'children' : ['/node1/node2', '/node1/node3'], 'properties' : ['/node1/property2', '/node1/property3'], 'type' : 0 }",
                "{ 'path' : '/node1/node2', 'children' : [], 'properties' : [], 'type' : 0 }",
                "{ 'path' : '/node1/node3', 'children' : [], 'properties' : [], 'type' : 0 }",
                "{ 'path' : '/property1', 'propertyType' : 1, 'value' : 'abcdef', 'type' : 1 }",
                "{ 'path' : '/node1/property2', 'propertyType' : 1, 'value' : 'ghijkl', 'type' : 1 }",
                "{ 'path' : '/node1/property3', 'propertyType' : 1, 'value' : 'mnopqr', 'type' : 1 }"
        };
        for (String item : items) {
            DBObject dbObject = (DBObject) JSON.parse(item);
            collection.insert(dbObject);
        }
    }

    @After
    public void tearDown() throws RepositoryException {
        if (session != null) {
            session.logout();
        }
        clearDB();
    }

    /**
     * Empties the db.
     */
    public void clearDB() {
        collection.drop();
    }

}
