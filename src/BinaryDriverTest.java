package com.appnomic.cassandra.play;

import com.datastax.driver.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * User: bharadwaj
 * Date: 25/06/13
 * Time: 8:56 AM
 */
public class BinaryDriverTest {
    private static final Logger LOG = LoggerFactory.getLogger(BinaryDriverTest.class);

    private final String m_cassandraHost;
    private final int m_cassandraPort;
    private final String m_keyspaceName;
    private final Cluster cluster;
    private final Session session;

    public BinaryDriverTest(String cassandraHost, int cassandraPort, String keyspaceName) {
        m_cassandraHost = cassandraHost;
        m_cassandraPort = cassandraPort;
        m_keyspaceName = keyspaceName;

        LOG.info("Connecting to {}:{}...", cassandraHost, cassandraPort);

        /*InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/
        cluster = Cluster.builder().withPort(m_cassandraPort).addContactPoint(cassandraHost).build();
        session = cluster.connect(m_keyspaceName);

        Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
        for ( Host host : metadata.getAllHosts() ) {
            System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(), host.getRack());
        }

        LOG.info("Connected.");
    }

    public void close() {
        cluster.shutdown();
    }

    private Row getOneRow(ResultSet result) {
        Row row = result.one();
        if (!result.isExhausted())
            throw new RuntimeException("ResultSet instance contained more than one row!");
        return row;
    }

    private ResultSet execute(String query, Object...parms) {
        String cql = String.format(query, parms);
        LOG.debug("Executing CQL: {}", cql);
        return session.execute(cql);
    }

    public List<String> getFollowers(String username) {
        ResultSet queryResult = execute("SELECT following FROM followers WHERE username = '%s'", username);
        List<String> followers = new ArrayList<String>();

        for (Row row : queryResult)
            followers.add(row.getString("following"));

        return followers;
    }

    public static void main(String[] args) {
        BinaryDriverTest bdt = new BinaryDriverTest("127.0.0.1", 9042, "\'Tutorial\'");
        bdt.close();
    }

}
