package ch.x42.terye.persistence;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.jcr.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.x42.terye.ConfiguredMongo;
import ch.x42.terye.persistence.ChangeLog.AddOperation;
import ch.x42.terye.persistence.ChangeLog.ModifyOperation;
import ch.x42.terye.persistence.ChangeLog.Operation;
import ch.x42.terye.persistence.ChangeLog.RemoveOperation;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class PersistenceManager {

    private final DBCollection collection;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public PersistenceManager() throws RepositoryException,
            UnknownHostException, MongoException {
        collection = new ConfiguredMongo().getDB(ConfiguredMongo.MONGO_DB_NAME)
                .getCollection(ConfiguredMongo.TERYE_MONGO_COLLECTION);
    }

    /**
     * Fetches the state of an item from the database.
     * 
     * @param path canonical item path
     * @param type item type wanted or null if it doesn't matter
     */
    public ItemState load(String path, ItemType type) {
        logger.debug("Load item from MongoDB: {}", path);
        BasicDBObject query = new BasicDBObject();
        query.put("path", path);
        if (type != null) {
            query.put("type", type.ordinal());
        }
        DBObject result = collection.findOne(query);
        if (result == null) {
            return null;
        }
        ItemState state;
        if (result.get("type").equals(ItemType.NODE.ordinal())) {
            state = new NodeState();
        } else {
            state = new PropertyState();
        }
        state.putAll(result.toMap());
        return state;
    }

    private void store(ItemState state) {
        logger.debug("Persist item to MongoDB: {}", state.getPath());
        BasicDBObject dbo = new BasicDBObject();
        dbo.put("path", state.getPath());
        collection.update(dbo, state, true, false);
    }

    private void delete(String pathPrefix) {
        logger.debug("Remove item from MongoDB: {}", pathPrefix);
        Pattern pattern = Pattern.compile("^" + pathPrefix);
        BasicDBObject dbo = new BasicDBObject();
        dbo.put("path", pattern);
        collection.remove(dbo);
    }

    public void persist(ChangeLog log) throws RepositoryException {
        Iterable<Operation> operations = log.getOperations();
        Iterator<Operation> iterator = operations.iterator();
        while (iterator.hasNext()) {
            Operation op = iterator.next();
            if (op instanceof AddOperation) {
                store(op.getItem().getState());
            } else if (op instanceof ModifyOperation) {
                store(op.getItem().getState());
            } else if (op instanceof RemoveOperation) {
                delete(op.getItem().getPath());
            }
        }
    }

}
