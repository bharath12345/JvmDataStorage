package com.appnomic.appsone.cassandra.dao;

import com.appnomic.appsone.cassandra.entity.JvmMethodMetricsRaw;
import com.appnomic.appsone.cassandra.utility.Constants;
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

    public JvmMethodMetricsDaoImpl() {
        keyspace = Constants.Keyspaces.JvmMethodMetrics.toString();
        table = Constants.ColumnFamilies.JvmMethodMetricsRaw.toString();

        cqlInsert = "INSERT INTO "+ keyspace + "." + table + " (" +
                Constants.JvmMethodMetricsRaw.jvm_id.toString() + ", " +
                Constants.JvmMethodMetricsRaw.date.toString() + ", " +
                Constants.JvmMethodMetricsRaw.day_time.toString() + ", " +
                Constants.JvmMethodMetricsRaw.method_id.toString() + ", " +
                Constants.JvmMethodMetricsRaw.invocations.toString() + ", " +
                Constants.JvmMethodMetricsRaw.response_time.toString() + ") " +
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
            jvmMethodMetricsRaw.setJvmId(row.getInt(Constants.JvmMethodMetricsRaw.jvm_id.toString()));
            jvmMethodMetricsRaw.setDate(row.getString(Constants.JvmMethodMetricsRaw.date.toString()));
            jvmMethodMetricsRaw.setDayTime(row.getInt(Constants.JvmMethodMetricsRaw.day_time.toString()));
            jvmMethodMetricsRaw.setMethodId(row.getLong(Constants.JvmMethodMetricsRaw.method_id.toString()));
            jvmMethodMetricsRaw.setInvocations(row.getLong(Constants.JvmMethodMetricsRaw.invocations.toString()));
            jvmMethodMetricsRaw.setResponseTime(row.getFloat(Constants.JvmMethodMetricsRaw.response_time.toString()));
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

    @Override
    public void persistSingle(JvmMethodMetricsRaw jvmMethodMetricsRaw) {
        PreparedStatement preparedStatement = getPreparedStatementNoShutdown(cqlInsert);
        BoundStatement boundStatement = new BoundStatement(preparedStatement);

        boundStatement.bind(jvmMethodMetricsRaw.getJvmId(), jvmMethodMetricsRaw.getDate(),
            jvmMethodMetricsRaw.getDayTime(), jvmMethodMetricsRaw.getMethodId(),
            jvmMethodMetricsRaw.getInvocations(), jvmMethodMetricsRaw.getResponseTime());

        executeBoundStatement(boundStatement);

        finishBoundExecution();
    }

    @Override
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
