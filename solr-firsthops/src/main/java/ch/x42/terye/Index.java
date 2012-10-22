package ch.x42.terye;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.x42.terye.persistence.ChangeLog;
import ch.x42.terye.persistence.ChangeLog.AddOperation;
import ch.x42.terye.persistence.ChangeLog.ModifyOperation;
import ch.x42.terye.persistence.ChangeLog.Operation;
import ch.x42.terye.persistence.ChangeLog.RemoveOperation;
import ch.x42.terye.value.ValueImpl;

public class Index {

    public static final String SOLR_URL = "http://localhost:1234/solr/";
    public static final String ID_FIELD = "id";

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static SolrServer server;

    protected Index() {
        server = new HttpSolrServer(Index.SOLR_URL);
    }

    public void update(ChangeLog log) throws RepositoryException {
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
            server.commit();
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
        doc.addField(Index.ID_FIELD, node.getPath());
        PropertyIterator iterator = node.getProperties();
        while (iterator.hasNext()) {
            Property property = iterator.nextProperty();
            String name = property.getName() + "_"
                    + PropertyType.nameFromValue(property.getType());
            Object value = ((ValueImpl) property.getValue()).getObject();
            // XXX: Solr cannot handle BigDecimal
            if (value instanceof BigDecimal) {
                // store big decimals as their string representation
                value = value.toString();
            } else if (value instanceof Calendar) {
                value = ((Calendar) value).getTime();
            }
            doc.addField(name, value);
        }
        server.add(doc);
    }

    private void remove(Item item) throws SolrServerException, IOException,
            RepositoryException {
        // index docs correspond to nodes, so we skip properties here:
        // if a property is removed, then its parent node is modified,
        // which is handled in addOrUpdate
        if (!item.isNode()) {
            return;
        }
        server.deleteById(item.getPath());
    }

    public List<String> query(String statement) throws RepositoryException {
        List<String> nodes = new LinkedList<String>();
        SolrQuery query = new SolrQuery(statement);
        SolrDocumentList results;
        try {
            logger.debug("Solr query: " + statement);
            results = server.query(query).getResults();
        } catch (SolrServerException e) {
            throw new RepositoryException("Query execution failed: "
                    + statement, e);
        }
        Iterator<SolrDocument> iterator = results.iterator();
        while (iterator.hasNext()) {
            nodes.add((String) iterator.next().get(Index.ID_FIELD));
        }
        return nodes;
    }

}
