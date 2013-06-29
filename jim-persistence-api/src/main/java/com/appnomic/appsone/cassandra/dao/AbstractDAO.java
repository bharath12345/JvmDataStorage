package com.appnomic.appsone.cassandra.dao;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: bharadwaj
 * Date: 28/06/13
 * Time: 4:41 PM
 */
public abstract class AbstractDAO {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractDAO.class);

    protected final String m_cassandraHost;
    protected final int m_cassandraPort;
    protected final String m_keyspaceName;
    protected final Cluster cluster;
    protected final Session session;

    protected AbstractDAO(String cassandraHost, int cassandraPort, String keyspaceName) {
        m_cassandraHost = cassandraHost;
        m_cassandraPort = cassandraPort;
        m_keyspaceName = keyspaceName;

        LOG.info("Connecting to {}:{}...", cassandraHost, cassandraPort);

        cluster = Cluster.builder().withPort(m_cassandraPort).addContactPoint(cassandraHost).build();
        session = cluster.connect(m_keyspaceName);

        Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
        for ( Host host : metadata.getAllHosts() ) {
            System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(), host.getRack());
        }

        LOG.info("Connected.");
    }

    protected void close() {
        cluster.shutdown();
    }

}
