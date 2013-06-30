package com.appnomic.appsone.cassandra.dao;

import com.appnomic.appsone.cassandra.entity.JvmMethodMetricsRaw;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * User: bharadwaj
 * Date: 28/06/13
 * Time: 4:50 PM
 */

@Stateless(mappedName = "JvmMethodMetricsDaoImpl")
@Remote(JvmMethodMetricsDAO.class)
public class JvmMethodMetricsDaoImpl extends CassandraDAO implements JvmMethodMetricsDAO {

    enum ColumnNames {
        jvm_id,
        date,
        day_time,
        method_id,
        invocations,
        response_time
    }

    public JvmMethodMetricsDaoImpl() {
        keyspace = "JvmMethodMetrics";
        table = "JvmMethodMetricsRaw";

        cqlInsert = "INSERT INTO "+ keyspace + "." + table + " (" +
                ColumnNames.jvm_id.name() + ", " +
                ColumnNames.date.name() + ", " +
                ColumnNames.day_time.name() + ", " +
                ColumnNames.method_id.name() + ", " +
                ColumnNames.invocations.name() + ", " +
                ColumnNames.response_time + ") " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        /*
            Table definition -
                CREATE TABLE JvmMethodMetricsRaw (
                    jvm_id int,
                    date varchar,
                    day_time int,
                    method_id bigint,
                    invocations bigint,
                    response_time float,
                    PRIMARY KEY (jvm_id, date)
                );
         */
    }

    @Override
    public List<JvmMethodMetricsRaw> findAll() {
        List<Row> rows = getAll();

        // convert raw rows to JvmMethodMetricsRaw objects
        // Since a huge number of objects can be retrieved, its better to use a object-pool instead of
        //  doing a 'new' of so many objects which is a very expensive operation for a running query

        List<JvmMethodMetricsRaw> jvmMethodMetricsRawList = new ArrayList<JvmMethodMetricsRaw>();
        for(Row row: rows) {
            JvmMethodMetricsRaw jvmMethodMetricsRaw = new JvmMethodMetricsRaw();
            jvmMethodMetricsRaw.setJvmId(row.getInt(ColumnNames.jvm_id.name()));
            jvmMethodMetricsRaw.setDate(row.getString(ColumnNames.date.name()));
            jvmMethodMetricsRaw.setDayTime(row.getInt(ColumnNames.day_time.name()));
            jvmMethodMetricsRaw.setMethodId(row.getLong(ColumnNames.method_id.name()));
            jvmMethodMetricsRaw.setInvocations(row.getLong(ColumnNames.invocations.name()));
            jvmMethodMetricsRaw.setResponseTime(row.getFloat(ColumnNames.response_time.name()));
            jvmMethodMetricsRawList.add(jvmMethodMetricsRaw);
        }
        return jvmMethodMetricsRawList;
    }

    @Override
    public List<JvmMethodMetricsRaw> findAllInTimeRange(long epochStartTime, long epochEndTime) {
        return null;  
    }

    @Override
    public List<JvmMethodMetricsRaw> findForJvm(int jvmId) {
        return null;  
    }

    @Override
    public List<JvmMethodMetricsRaw> findInTimeRangeForJvm(int jvmId, long epochStartTime, long epochEndTime) {
        return null;  
    }


    public void persistSingle(JvmMethodMetricsRaw jvmMethodMetricsRaw) {
        PreparedStatement preparedStatement = getPreparedStatementNoShutdown(cqlInsert);
        BoundStatement boundStatement = new BoundStatement(preparedStatement);

        boundStatement.bind(jvmMethodMetricsRaw.getJvmId(), jvmMethodMetricsRaw.getDate(),
            jvmMethodMetricsRaw.getDayTime(), jvmMethodMetricsRaw.getMethodId(),
            jvmMethodMetricsRaw.getInvocations(), jvmMethodMetricsRaw.getResponseTime());

        executeBoundStatement(boundStatement);

        finishBoundExecution();
    }

    public void persistList(List<JvmMethodMetricsRaw> jvmMethodMetricsRawList) {
        PreparedStatement preparedStatement = getPreparedStatementNoShutdown(cqlInsert);
        BoundStatement boundStatement = new BoundStatement(preparedStatement);

        for(JvmMethodMetricsRaw jvmMethodMetricsRaw: jvmMethodMetricsRawList) {
            boundStatement.bind(jvmMethodMetricsRaw.getJvmId(), jvmMethodMetricsRaw.getDate(),
                    jvmMethodMetricsRaw.getDayTime(), jvmMethodMetricsRaw.getMethodId(),
                    jvmMethodMetricsRaw.getInvocations(), jvmMethodMetricsRaw.getResponseTime());
            executeBoundStatement(boundStatement);
        }

        finishBoundExecution();
    }
}
