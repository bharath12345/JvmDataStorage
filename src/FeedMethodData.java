package com.appnomic.cassandra.play;

import me.prettyprint.cassandra.serializers.FloatSerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import org.joda.time.DateTime;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: sumanthn
 * Date: 31/5/13
 */
public class FeedMethodData {

    private Keyspace keyspace;
    private Executor taskPool = Executors.newFixedThreadPool(10);
    private ScheduledExecutorService taskMonitor = Executors.newSingleThreadScheduledExecutor();
    static final IntegerSerializer intSerializer = IntegerSerializer.get();
    static final FloatSerializer floatSerializer = FloatSerializer.get();
    static final StringSerializer stringSerializer = StringSerializer.get();
    private AtomicLong createdRows = new AtomicLong(0);

    public void initKeySpace() {
        Cluster cluster = HFactory.getOrCreateCluster(NamesUtil.CLUSTER_NAME, "127.0.0.1:9160");
        keyspace = HFactory.createKeyspace(NamesUtil.KEY_SPACE_NAME, cluster);
    }

    List<Integer> jvmIds = new LinkedList<Integer>();
    Map<Integer, List<Integer>> methodIdMap = new LinkedHashMap<Integer, List<Integer>>();

    public void initMethodMetaData() {
        int jvmIdStart = 1000;
        int methodIdStart = 10;
        int jvmCount = 20;
        int methodsCount = 5;

        for (int i = 0; i < jvmCount; i++) {
            int jvmId = jvmIdStart + i;
            jvmIds.add(jvmIdStart + i);
            methodIdMap.put(jvmId, new LinkedList<Integer>());
        }

        for (int jvmId : jvmIds) {
            List<Integer> methodIds = methodIdMap.get(jvmId);
            for (int i = 0; i < methodsCount; i++) {
                int methodId = methodIdStart + i;
                methodIds.add(methodId);
            }
        }

        taskMonitor.scheduleAtFixedRate(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Created rows " + createdRows);
            }
        }), 1L, 1L, TimeUnit.SECONDS);
    }


    public void feedData() {
        int count = 0;
        int maxCounts = 30 * 1440;

        /**Epoch timestamp: 1369958400
         Timestamp in milliseconds: 1369958400000
         Human time (GMT): Fri, 31 May 2013 00:00:00 GMT
         Human time (your time zone): Friday 31 May 2013 05:30:00 AM IST
         */
        DateTime now = new DateTime(1369958400000L);

        while (true) {
            String nowStr = now.getYear() + "" + now.getMonthOfYear() + "" + now.getDayOfMonth() + "" + now.getHourOfDay() + "" + now.getMinuteOfHour();

            for (int jvmId : jvmIds) {
                taskPool.execute(new InsertDataTask(jvmId, Long.valueOf(nowStr)));
                // System.out.println("execute for the time stamp " + Long.valueOf(nowStr));
            }
            // break;
            now = now.plusMinutes(1);
            count++;
            if (count >= maxCounts)
                break;
        }
    }

    private class InsertDataTask implements Runnable {

        private final int jvmId;
        private final long ts;

        private InsertDataTask(int jvmId, long ts) {
            this.jvmId = jvmId;
            this.ts = ts;
        }

        public void run() {
            List<Integer> methodIds = methodIdMap.get(jvmId);
            Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());

            final String key = jvmId + ":" + ts;
            Random random = new Random();

            for (Integer methodId : methodIds) {
                //  System.out.println(methodId);
                mutator.addInsertion(key, NamesUtil.CF_NAME, HFactory.
                        createColumn(NamesUtil.INVOCATION_COL_SUFFIX + "_" + methodId, Math.abs(random.nextInt()),
                                stringSerializer, intSerializer));
                mutator.addInsertion(key, NamesUtil.CF_NAME, HFactory.
                        createColumn(NamesUtil.RESPONSE_TIME_COL_SUFFIX + "_" + methodId, Math.abs(random.nextFloat()),
                                stringSerializer, floatSerializer));

            }

            //System.out.println("Key:" + key);
            mutator.execute();

            createdRows.getAndIncrement();
            //System.out.println("finished feed data JVM for " + jvmId + " ts " +  ts);
        }
    }

    public static void main(String[] args) {
        FeedMethodData feedMethodData = new FeedMethodData();
        feedMethodData.initKeySpace();
        feedMethodData.initMethodMetaData();
        feedMethodData.feedData();
    }
}
