package com.appnomic.appsone.cassandra.dao;

import com.appnomic.appsone.cassandra.query.CassandraQueryBuilder;
import com.datastax.driver.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * User: bharadwaj
 * Date: 28/06/13
 * Time: 4:41 PM
 */
public class CassandraDAO {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraDAO.class);

    protected String keyspace;
    protected String table;
    protected String cqlInsert;

    protected List<Row> getAll() {
        CassandraQueryBuilder.connect();
        Query query = CassandraQueryBuilder.getAll(keyspace, table);
        List<Row> rows = CassandraQueryBuilder.execute(query).all();
        CassandraQueryBuilder.shutdown();
        return rows;
    }

    protected PreparedStatement getPreparedStatementNoShutdown(String cql) {
        CassandraQueryBuilder.connect();
        PreparedStatement preparedStatement = CassandraQueryBuilder.getPreparedStatement(keyspace, cql);
        return preparedStatement;
    }

    protected void executeBoundStatement(BoundStatement boundStatement) {
        CassandraQueryBuilder.executeBoundStatement(boundStatement);
    }

    protected void finishBoundExecution() {
        CassandraQueryBuilder.shutdown();
    }


}
