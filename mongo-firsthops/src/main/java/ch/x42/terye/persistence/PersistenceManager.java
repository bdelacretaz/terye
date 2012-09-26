package ch.x42.terye.persistence;

import java.net.UnknownHostException;

import javax.jcr.RepositoryException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class PersistenceManager {

    private static PersistenceManager instance;
    private DBCollection collection;

    private PersistenceManager() throws RepositoryException {
        try {
            collection = new Mongo("localhost", 27018).getDB("test")
                    .getCollection("repo");
            // XXX: temporary
            collection.drop();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            throw new RepositoryException("Unable to access MongoDB collection");
        }
    }

    public static PersistenceManager getInstance() throws RepositoryException {
        if (instance == null) {
            instance = new PersistenceManager();
        }
        return instance;
    }

    public NodeState load(String path) {
        BasicDBObject dbo = new BasicDBObject();
        dbo.put("path", path);
        return (NodeState) collection.findOne(dbo);
    }
}
