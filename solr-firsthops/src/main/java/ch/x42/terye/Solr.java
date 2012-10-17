package ch.x42.terye;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

public class Solr {

    public static final String URL = "http://localhost:1234/solr-example/";

    private static SolrServer server;

    public static SolrServer getServer() {
        if (server == null) {
            server = new HttpSolrServer(Solr.URL);
        }
        return server;
    }

}
