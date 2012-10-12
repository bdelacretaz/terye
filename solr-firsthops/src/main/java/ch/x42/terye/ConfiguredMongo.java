package ch.x42.terye;

import java.net.UnknownHostException;

import com.mongodb.Mongo;

public class ConfiguredMongo extends Mongo {
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 27018;
    
    public static final String MONGO_DB_NAME = "teryeMongoFirsthops";
    public static final String TERYE_MONGO_COLLECTION = "teryeRepository";

    public ConfiguredMongo() throws UnknownHostException {
        super(DEFAULT_HOST, DEFAULT_PORT);
    }
}