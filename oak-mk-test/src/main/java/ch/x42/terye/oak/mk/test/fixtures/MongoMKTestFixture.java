package ch.x42.terye.oak.mk.test.fixtures;

import org.apache.jackrabbit.mk.api.MicroKernel;
import org.apache.jackrabbit.mongomk.impl.MongoConnection;
import org.apache.jackrabbit.mongomk.impl.MongoMicroKernel;
import org.apache.jackrabbit.mongomk.impl.MongoNodeStore;
import org.apache.jackrabbit.mongomk.impl.blob.MongoGridFSBlobStore;

import com.mongodb.DB;

public class MongoMKTestFixture implements MicroKernelTestFixture {

    private static final String MONGODB_HOST = "localhost";
    private static final int MONGODB_PORT = 27017;
    private static final String MONGODB_DB = "mktest";

    private MongoConnection connection;

    public MongoMKTestFixture() {
        this.connection = null;
    }

    @Override
    public MicroKernel createMicroKernel() throws Exception {
        if (connection == null) {
            connection = new MongoConnection(MONGODB_HOST, MONGODB_PORT,
                    MONGODB_DB);
        }
        DB mongoDB = connection.getDB();
        MongoNodeStore nodeStore = new MongoNodeStore(mongoDB);
        MongoGridFSBlobStore blobStore = new MongoGridFSBlobStore(mongoDB);
        return new MongoMicroKernel(connection, nodeStore, blobStore);
    }

    @Override
    public void setUpBeforeTest() throws Exception {
        // nothing to do
    }

    @Override
    public void tearDownAfterTest() throws Exception {
        // drop the database and close the connection
        if (connection != null) {
            connection.getDB().dropDatabase();
            connection.close();
        }
    }

    @Override
    public String toString() {
        return "MongoMicroKernel";
    }

}
