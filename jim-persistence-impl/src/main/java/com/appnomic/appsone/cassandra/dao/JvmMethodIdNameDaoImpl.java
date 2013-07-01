package com.appnomic.appsone.cassandra.dao;

import com.appnomic.appsone.cassandra.utility.Constants;
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
@Remote(JvmMethodIdNameDAO.class)
public class JvmMethodIdNameDaoImpl extends CassandraDAO implements JvmMethodIdNameDAO {

    public JvmMethodIdNameDaoImpl() {
        keyspace = Constants.Keyspaces.JvmMethodMetrics.toString();
        table = Constants.ColumnFamilies.JvmMethodIdNameMap.toString();

        cqlInsert = "INSERT INTO "+ keyspace + "." + table + " (" +
                Constants.JvmMethodIdNameMap.jvm_id.toString() + ", " +
                Constants.JvmMethodIdNameMap.method_id.toString() + ", " +
                Constants.JvmMethodIdNameMap.method_name.toString() + ") " +
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

        return super.getMethodId(jvmId, methodName);
    }

    public long setMethodIdName(int jvmId, String methodName) {
        PreparedStatement preparedStatement = getPreparedStatementNoShutdown(cqlInsert);
        BoundStatement boundStatement = new BoundStatement(preparedStatement);

        int maxMethodId = getMaxId(jvmId);
        boundStatement.bind(jvmId, maxMethodId, methodName);

        //System.out.println("statement = " + boundStatement.);
        executeBoundStatement(boundStatement);

        finishBoundExecution();
        return maxMethodId;
    }

    public int getMaxId(int jvmId) {
        List<Row> rows = getAllForJvmId(keyspace, table, jvmId);
        int maxMethodId = -1;
        for(Row row: rows) {
            int methodId = row.getInt(Constants.JvmMethodIdNameMap.method_id.toString());
            if(methodId > maxMethodId) {
                maxMethodId = methodId;
            }
        }
        return maxMethodId;
    }

}
