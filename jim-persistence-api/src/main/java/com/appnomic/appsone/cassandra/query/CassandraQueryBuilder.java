package com.appnomic.appsone.cassandra.query;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * User: bharadwaj
 * Date: 28/06/13
 * Time: 10:48 AM
 */
public class CassandraQueryBuilder {

    private static Cluster cluster;
    private static Session session;

    private static final Builder selectAll = QueryBuilder.select().all();

    private static final Logger LOG = LoggerFactory.getLogger(CassandraQueryBuilder.class);

    static {
        connect();
        createKeyspaces();
        createIndexes();
        shutdown();
    }

    public static void connect() {

        try {
            Properties properties = new Properties();
            properties.load(CassandraQueryBuilder.class.getClassLoader().getResourceAsStream("cassandra.properties"));

            final String cassandraHost = properties.getProperty("hostname");
            final int cassandraPort = Integer.parseInt(properties.getProperty("native_transport_port"));

            LOG.info("Connecting to {}:{}...", cassandraHost, cassandraPort);

            cluster = Cluster.builder().withPort(cassandraPort).addContactPoint(cassandraHost).build();

            Metadata metadata = cluster.getMetadata();
            System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
            for (Host host : metadata.getAllHosts()) {
                System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(), host.getRack());
            }

            LOG.info("Connected.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shutdown() {
        session.shutdown();
        cluster.shutdown();
    }

    public static Query getAll(String keyspace, String table) {
        session = cluster.connect(keyspace);
        return selectAll.from(keyspace, table);
    }

    public static ResultSet execute(Query query) {
        return session.execute(query);
    }

    public static Session createKeyspaces() {
        session = cluster.connect("system");

        String JvmMethodMetrics = "JvmMethodMetrics";
        session.execute("CREATE KEYSPACE " + JvmMethodMetrics + " WITH " +
                "replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};");
        session.shutdown();

        session = cluster.connect(JvmMethodMetrics);

        return session;
    }

    public static void createTables() {
        session.execute("CREATE TABLE JvmMethodIdNameMap (" +
                "jvm_id varchar," +
                "method_id bigint," +
                "method_name varchar," +
                "PRIMARY KEY (jvm_id));");

        session.execute("CREATE TABLE JvmMethodMetricsRaw (" +
                "jvm_id varchar," +
                "date varchar," +
                "ts timestamp," +
                "method_id bigint," +
                "invocations bigint," +
                "response_time bigint," +
                "PRIMARY KEY (jvm_id, date));");
    }

    public static void createIndexes() {
        session.execute("CREATE INDEX jvm_method_name ON JvmMethodMetricsRaw (method_id);");
    }

}
