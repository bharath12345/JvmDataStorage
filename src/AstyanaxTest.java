package com.appnomic.cassandra.play;

import com.netflix.astyanax.*;
import com.netflix.astyanax.connectionpool.*;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.*;
import com.netflix.astyanax.impl.*;
import com.netflix.astyanax.model.*;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.*;

/**
 * User: bharadwaj
 * Date: 24/06/13
 * Time: 2:29 PM
 */
public class AstyanaxTest {

    public static void main(String[] args) {
        AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
                .forCluster("Test Cluster")
                .forKeyspace("Tutorial")
                .withAstyanaxConfiguration(new AstyanaxConfigurationImpl()
                        .setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
                        .setCqlVersion("3.0.0")
                        .setTargetCassandraVersion("1.2")
                )
                .withConnectionPoolConfiguration(new ConnectionPoolConfigurationImpl("MyConnectionPool")
                        .setPort(9160)
                        .setMaxConnsPerHost(1)
                        .setSeeds("127.0.0.1:9160")
                )
                .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
                .buildKeyspace(ThriftFamilyFactory.getInstance());

        context.start();
        Keyspace keyspace = context.getClient();

        ColumnFamily<String, String> CF_USER_INFO =
                new ColumnFamily<String, String>(
                        "Standard1",              // Column Family Name
                        StringSerializer.get(),   // Key Serializer
                        StringSerializer.get());  // Column Serializer

        // Inserting data
        MutationBatch m = keyspace.prepareMutationBatch();

        m.withRow(CF_USER_INFO, "acct1234")
                .putColumn("firstname", "john", null)
                .putColumn("lastname", "smith", null)
                .putColumn("address", "555 Elm St", null)
                .putColumn("age", 30, null);

        //m.withRow(CF_USER_STATS, "acct1234")
        //        .incrementCounterColumn("loginCount", 1);

        try {
            OperationResult<Void> result = m.execute();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

}
