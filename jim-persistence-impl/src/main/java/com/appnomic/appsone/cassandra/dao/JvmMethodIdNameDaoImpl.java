package com.appnomic.appsone.cassandra.dao;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.List;

/**
 * User: bharadwaj
 * Date: 30/06/13
 * Time: 9:15 PM
 */
@Stateless(mappedName = "JvmMethodIdNameDaoImpl")
@Remote(JvmMethodMetricsDAO.class)
public class JvmMethodIdNameDaoImpl extends CassandraDAO implements JvmMethodIdNameDAO {

    enum ColumnNames {
        jvm_id,
        method_id,
        method_name
    }

    public JvmMethodIdNameDaoImpl() {
        keyspace = "JvmMethodMetrics";
        table = "JvmMethodIdNameMap";

        cqlInsert = "INSERT INTO "+ keyspace + "." + table + " (" +
                ColumnNames.jvm_id.name() + ", " +
                ColumnNames.method_id.name() + ", " +
                ColumnNames.method_name + ") " +
                "VALUES (?, ?, ?);";

        /*
            CREATE TABLE JvmMethodIdNameMap (
                jvm_id int,
                method_id bigint,
                method_name varchar,
                PRIMARY KEY (jvm_id)
            );
         */
    }

    public long getMethodId(int jvmId, String methodName) {
        return getMethodId(jvmId, methodName);
    }

    public long setMethodIdName(int jvmId, String methodName) {
        PreparedStatement preparedStatement = getPreparedStatementNoShutdown(cqlInsert);
        BoundStatement boundStatement = new BoundStatement(preparedStatement);

        long maxMethodId = getMaxId(jvmId);
        boundStatement.bind(jvmId, methodName, maxMethodId);

        executeBoundStatement(boundStatement);

        finishBoundExecution();
        return maxMethodId;
    }

    public long getMaxId(int jvmId) {
        List<Row> rows = getAllForJvmId(keyspace, table, jvmId);
        long maxMethodId = -1;
        for(Row row: rows) {
            long methodId = row.getLong(ColumnNames.method_id.name());
            if(methodId > maxMethodId) {
                maxMethodId = methodId;
            }
        }
        return maxMethodId;
    }

}
