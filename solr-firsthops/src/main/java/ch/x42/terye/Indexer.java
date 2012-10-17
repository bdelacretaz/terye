package ch.x42.terye;

import java.io.IOException;
import java.util.Iterator;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import ch.x42.terye.persistence.ChangeLog;
import ch.x42.terye.persistence.ChangeLog.AddOperation;
import ch.x42.terye.persistence.ChangeLog.ModifyOperation;
import ch.x42.terye.persistence.ChangeLog.Operation;
import ch.x42.terye.persistence.ChangeLog.RemoveOperation;
import ch.x42.terye.value.ValueImpl;

public class Indexer {

    private static Indexer instance;

    private Indexer() {
    }

    public static Indexer getInstance() {
        if (instance == null) {
            instance = new Indexer();
        }
        return instance;
    }

    public void index(ChangeLog log) throws RepositoryException {
        Iterator<Operation> iterator = log.iterator();
        try {
            while (iterator.hasNext()) {
                Operation op = iterator.next();
                if (op instanceof AddOperation || op instanceof ModifyOperation) {
                    addOrUpdate(op.getItem());
                } else if (op instanceof RemoveOperation) {
                    remove(op.getItem());
                }
            }
            Solr.getServer().commit();
        } catch (Exception e) {
            throw new RepositoryException("Could not update index", e);
        }
    }

    private void addOrUpdate(Item item) throws RepositoryException,
            SolrServerException, IOException {
        // only add nodes to index:
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
        Solr.getServer().add(doc);
    }

    private void remove(Item item) throws SolrServerException, IOException,
            RepositoryException {
        // index docs correspond to nodes, so we skip properties here:
        // if a property is removed, then its parent node is modified,
        // which is handled in addOrUpdate
        if (!item.isNode()) {
            return;
        }
        Solr.getServer().deleteById(item.getPath());
    }

}
