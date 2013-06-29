package com.appnomic.cassandra.play;

import me.prettyprint.cassandra.serializers.FloatSerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

import java.util.List;

/**
 * User: sumanthn
 * Date: 31/5/13
 */
public class DataQuery {


    /**
     *
     1008:201361521
     1009:201361521
     1010:201361521
     1011:201361521
     1012:201361521
     1013:201361521
     1015:201361521
     1014:201361521
     1016:201361521
     1017:201361521
     1018:201361521
     1019:201361521
     1000:201361522
     1001:201361522
     1002:201361522
     1003:201361522
     1004:201361522
     1005:201361522
     1006:201361522
     1007:201361522
     1008:201361522
     1009:201361522
     1010:201361522
     1011:201361522
     1012:201361522
     1013:201361522
     1014:201361522
     1015:201361522
     1016:201361522
     1017:201361522
     1018:201361522
     1019:201361522
     1000:201361523
     1001:201361523
     1002:201361523
     */

    private Keyspace keyspace;

    static final IntegerSerializer intSerializer =  IntegerSerializer.get();
    static final FloatSerializer floatSerializer =  FloatSerializer.get();
    static final StringSerializer stringSerializer = StringSerializer.get();
    private long MILLI_SECONDS_CONV = 1000L;
    public void initKeySpace() {
        Cluster cluster = HFactory.getOrCreateCluster(NamesUtil.CLUSTER_NAME, "127.0.0.1:9160");
        keyspace = HFactory.createKeyspace(NamesUtil.KEY_SPACE_NAME, cluster);


    }

    public void querySpecificColumn(){
        ColumnQuery<String,String,Integer> columnQuery =
                HFactory.createColumnQuery(keyspace, stringSerializer, stringSerializer, intSerializer).setKey("1008:201361321").setColumnFamily(NamesUtil.CF_NAME).setName("12_I");
        QueryResult<HColumn<String,Integer>> colData = columnQuery.execute();
        System.out.println("Fetched:" + colData.get().getName() + " " + colData.get().getValue());
        System.out.println("Time for query:" + colData.getExecutionTimeMicro()/MILLI_SECONDS_CONV  +" ms");

    }

    public void getAllKeys (){
        RangeSlicesQuery<String, String, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(keyspace,
                stringSerializer, stringSerializer, stringSerializer).setKeys("1008:201361421","1008:201361521").setColumnFamily(NamesUtil.CF_NAME).setColumnNames("I_10");

       //setRange("1008:201361421","1008:201361521",false,Integer.MAX_VALUE)
       // QueryResult<OrderedRows<String,String,String>> queryResult = rangeSlicesQuery.execute();

       // System.out.println(rangeSlicesQuery.getColumnNames());
        List<Row<String,String,String>> allRows = rangeSlicesQuery.execute().get().getList();

       System.out.println("rows are " +  allRows);
        for(Row<String,String,String> row : allRows){
            System.out.println("All columns size :"+ row.getColumnSlice().getColumns().size());

        }

        /*int rowCount =
                rangeSlicesQuery.setColumnFamily(NamesUtil.CF_NAME).setKeys("", "").getRowCount();
*/

       /* if (queryResult!=null){
            System.out.print(queryResult.getExecutionTimeMicro() + "  with row as "+  queryResult.get().getList().size());
        }*/

    }


    public void getRowsInRange(){
        //RangeSlicesQuery<String,String,String> rangeSlicesQuery = HFactory.cre

    }



    public void queryForRangeofRows(){

        RangeSlicesQuery<String, String, String> rangeSlicesQuery =
                HFactory.createRangeSlicesQuery(keyspace, stringSerializer, stringSerializer, stringSerializer);
        rangeSlicesQuery.setColumnFamily(NamesUtil.CF_NAME);
        //one hour data
        //rangeSlicesQuery.setKeys("1008:201361421","1008:201361521"); //set the keys will work only wit Ordered Partitioner


        rangeSlicesQuery.setKeys("1008:201361421","1008:201362421"); //set the keys will work only wit Ordered Partitioner
      //  rangeSlicesQuery.setRange("", "", false, 20);      //check out for columns this one for first 3 columns

        rangeSlicesQuery.setRange("I_", "", false, 20); //return all the columns with common prefix, in this case the Invocation counts
        QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute();
     //   System.out.println(result.get().getCount());
        OrderedRows<String,String,String> allRows = result.get();
        List<Row<String,String,String>> allRowSets = allRows.getList();
        for(Row<String,String,String> row : allRowSets){
          // System.out.println(row.getKey() );
           // System.out.println(row.getColumnSlice().getColumns().size());
        }

        System.out.println(result.getExecutionTimeMicro()/MILLI_SECONDS_CONV + " ms" + " rows count " + result.get().getCount());

    }




    public static void main(String [] args){
        DataQuery dataQuery =  new DataQuery();
        dataQuery.initKeySpace();
        //dataQuery.getAllKeys();
        //dataQuery.querySpecificColumn();
        dataQuery.queryForRangeofRows();

    }
}
