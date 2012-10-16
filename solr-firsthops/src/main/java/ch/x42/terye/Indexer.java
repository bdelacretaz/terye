package ch.x42.terye;

import java.util.Iterator;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import ch.x42.terye.persistence.ChangeLog;
import ch.x42.terye.persistence.ChangeLog.AddOperation;
import ch.x42.terye.persistence.ChangeLog.ModifyOperation;
import ch.x42.terye.persistence.ChangeLog.Operation;
import ch.x42.terye.persistence.ChangeLog.RemoveOperation;
import ch.x42.terye.value.ValueImpl;

public class Indexer {

    public static final String SOLR_URL = "http://localhost:1234/solr-example/";

    private static Indexer instance;
    private SolrServer server;

    private Indexer() {
        server = new HttpSolrServer(Indexer.SOLR_URL);
    }

    public static Indexer getInstance() {
        if (instance == null) {
            instance = new Indexer();
        }
        return instance;
    }

    public void index(ChangeLog log) throws RepositoryException {
        Iterator<Operation> iterator = log.iterator();
        while (iterator.hasNext()) {
            Operation op = iterator.next();
            if (op instanceof AddOperation || op instanceof ModifyOperation) {
                addOrUpdate(op.getItem());
            } else if (op instanceof RemoveOperation) {
                remove(op.getItem());
            }
        }
        try {
            server.commit();
        } catch (Exception e) {
            throw new RepositoryException(
                    "Could not update index, commit failed: " + e.getMessage());
        }
    }

    private void addOrUpdate(Item item) throws RepositoryException {
        // only add nodes to index
        // since creating a property modifies its parent node, we are
        // sure that we don't forget to index properties
        if (!item.isNode()) {
            return;
        }
        Node node = (Node) item;
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", node.getPath());
        doc.addField("name", node.getName());
        PropertyIterator iterator = node.getProperties();
        while (iterator.hasNext()) {
            Property property = iterator.nextProperty();
            String name = property.getName() + "_"
                    + PropertyType.nameFromValue(property.getType());
            Object value = ((ValueImpl) property.getValue()).getObject();
            doc.addField(name, value);
        }
        try {
            server.add(doc);
        } catch (Exception e) {
            throw new RepositoryException("Could not update index: "
                    + e.getMessage());
        }
    }

    private void remove(Item item) {

    }

}
