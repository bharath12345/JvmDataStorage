package com.appnomic.appsone.cassandra.query;

import com.appnomic.appsone.cassandra.utility.Constants;
import com.datastax.driver.core.*;
import com.datastax.driver.core.exceptions.AlreadyExistsException;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
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
    private static final Builder select = QueryBuilder.select();

    private static final Logger LOG = LoggerFactory.getLogger(CassandraQueryBuilder.class);

    static {
        connect();
        createKeyspaces();
        createTables();
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

    public static List<Row> getAllForJvmId(String keyspace, String table, int jvmId) {
        session = cluster.connect(keyspace);
        //return select.from(keyspace, table).where().;
        ResultSet result = session.execute("SELECT * from " + keyspace + "." + table + " WHERE jvm_id = " + jvmId + ";");
        return result.all();
    }

    public static long getMethodId(String keyspace, String table, int jvmId, String methodName) {
        System.out.println("Querying keyspace = " + keyspace + " column family = " + table);
        cluster.shutdown();

        connect();
        session = cluster.connect(keyspace);

        //return select.from(keyspace, table).where().;
        ResultSet result = session.execute("SELECT method_id from " + keyspace + "." + table +
                " WHERE jvm_id = " + jvmId +
                " AND method_name = '" + methodName + "';");

        if (result.all().size() > 0) {
            return result.all().get(0).getLong("method_id");
        } else {
            return -1;
        }
    }

    public static PreparedStatement getPreparedStatement(String keyspace, String cql) {
        session = cluster.connect(keyspace);
        return session.prepare(cql);
    }

    public static ResultSet execute(Query query) {
        return session.execute(query);
    }

    public static void executeBoundStatement(BoundStatement boundStatement) {
        session.execute(boundStatement);
    }

    public static void createKeyspaces() {
        session = cluster.connect();

        try {
            session.execute("CREATE KEYSPACE " + Constants.Keyspaces.JvmMethodMetrics.toString() + " WITH " +
                    "replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};");
        } catch (AlreadyExistsException e) {
            System.out.println("Keyspace = " + Constants.Keyspaces.JvmMethodMetrics.toString() + " already exists");
        }
        session.shutdown();
    }

    public static void createTables() {
        session = cluster.connect(Constants.Keyspaces.JvmMethodMetrics.toString());

        try {
            session.execute("CREATE TABLE " + Constants.ColumnFamilies.JvmMethodIdNameMap.toString()
                    + " (jvm_id int," +
                    "method_id int," +
                    "method_name varchar," +
                    "PRIMARY KEY (jvm_id));");
        } catch (AlreadyExistsException e) {
            System.out.println("Table = " + Constants.ColumnFamilies.JvmMethodIdNameMap.toString() + " already exists");
        }

        try {
            session.execute("CREATE TABLE " + Constants.ColumnFamilies.JvmMethodMetricsRaw.toString() + " (" +
                    "jvm_id int," +
                    "date varchar," +
                    "day_time int," +
                    "method_id int," +
                    "invocations bigint," +
                    "response_time float," +
                    "PRIMARY KEY (jvm_id, date));");
        } catch (AlreadyExistsException e) {
            System.out.println("Keyspace = " + Constants.ColumnFamilies.JvmMethodMetricsRaw.toString() + " already exists");
        }

    }

    public static void createIndexes() {
        try {
            session.execute("CREATE INDEX jvm_method_id ON " +
                    Constants.ColumnFamilies.JvmMethodMetricsRaw.toString() + " (method_id);");

            session.execute("CREATE INDEX jvm_method_name ON " +
                    Constants.ColumnFamilies.JvmMethodIdNameMap.toString() + " (method_name);");

        } catch (InvalidQueryException e) {
            System.out.println("Index jvm_method_name on table = " +
                    Constants.ColumnFamilies.JvmMethodMetricsRaw.toString() + " already exists");
        }
    }

}
