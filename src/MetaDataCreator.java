package com.appnomic.cassandra.play;

import me.prettyprint.cassandra.connection.HConnectionManager;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import java.util.List;

/**
 * User: sumanthn
 * Date: 31/5/13
 */
public class MetaDataCreator {

    protected HConnectionManager connectionManager;
    protected CassandraHostConfigurator cassandraHostConfigurator;

    private Keyspace keyspace;

    public void initConnection(){
        cassandraHostConfigurator = new CassandraHostConfigurator("127.0.0.1:9160");
        cassandraHostConfigurator.setMaxActive(10);
        connectionManager = new HConnectionManager(NamesUtil.CLUSTER_NAME,cassandraHostConfigurator);

    }


    public void createKeySpace(){

        Cluster cluster = HFactory.getOrCreateCluster(NamesUtil.CLUSTER_NAME, "127.0.0.1:9160");
        List<KeyspaceDefinition> keyspaceDefinitionList = cluster.describeKeyspaces();

        boolean isPresent = false;
       /* for(KeyspaceDefinition ksdef : keyspaceDefinitionList){
            System.out.println(ksdef.getName());
            if (ksdef.getName().equals(NamesUtil.KEY_SPACE_NAME)){
                isPresent = true;
                break;
            }
        }*/

            if (!isPresent){
                keyspace = HFactory.createKeyspace(NamesUtil.KEY_SPACE_NAME,cluster);
                System.out.println("Created keyspace:" + keyspace.getKeyspaceName());

            }


    }

    public void createData(){
        Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());


        //mutator.addInsertion("JVM_10-201305311112",NamesUtil.CF_NAME,HFactory.createScreateStringColumntringColumn("20_Count"),"");
        mutator.addInsertion("JVM_10-201305311112",NamesUtil.CF_NAME,HFactory.createColumn("10_Invoc",15,StringSerializer.get(), IntegerSerializer.get()));
        mutator.addInsertion("JVM_10-201305311112",NamesUtil.CF_NAME,HFactory.createColumn("11_Invoc",16,StringSerializer.get(), IntegerSerializer.get()));

        mutator.addInsertion("JVM_10-201305311112",NamesUtil.CF_NAME,HFactory.createColumn("12_Invoc",19,StringSerializer.get(), IntegerSerializer.get()));
        mutator.addInsertion("JVM_10-201305311112",NamesUtil.CF_NAME,HFactory.createColumn("13_Invoc",16,StringSerializer.get(), IntegerSerializer.get()));
        mutator.addInsertion("JVM_10-201305311112",NamesUtil.CF_NAME,HFactory.createColumn("14_Invoc",17,StringSerializer.get(), IntegerSerializer.get()));
        mutator.addInsertion("JVM_10-201305311112",NamesUtil.CF_NAME,HFactory.createColumn("15_Invoc",19,StringSerializer.get(), IntegerSerializer.get()));


        mutator.execute();

    }
    public void createCF(){

        //check if key space is present




    }


    public static void main(String [] args){

        //
        /**
         * Initializing a cluster
         *
         Cluster myCluster = HFactory.getOrCreateCluster("test-cluster","localhost:9160");
         set up the schema.

         ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition("MyKeyspace",
         "ColumnFamilyName",
         ComparatorType.BYTESTYPE);
         KeyspaceDefinition newKeyspace = HFactory.createKeyspaceDefinition("MyKeyspace",
         ThriftKsDef.DEF_STRATEGY_CLASS,
         replicationFactor,
         Arrays.asList(cfDef));
         Add the schema to the cluster.
         cluster.addKeyspace(newKeyspace, true);


         //check if it already exists
         KeyspaceDefinition keyspaceDef = cluster.describeKeyspace("MyKeyspace");
         else create it
         if (keyspaceDef == null) {
         createSchema();
         }

         //keep the keyspace alive


         Keyspace ksp = HFactory.createKeyspace("MyKeyspace", myCluster);

         Update
         ColumnFamilyUpdater<String, String> updater = template.createUpdater("a key");
         updater.setString("domain", "www.datastax.com");
         updater.setLong("time", System.currentTimeMillis());
         template.update(updater);
         */

        MetaDataCreator dataCreator = new MetaDataCreator();
       // dataCreator.initConnection();
        dataCreator.createKeySpace();
        dataCreator.createData();
    }

}
