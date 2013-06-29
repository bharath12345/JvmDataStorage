package com.appnomic.appsone.cassandra.dao;

import com.appnomic.appsone.cassandra.query.CassandraQueryBuilder;
import com.datastax.driver.core.Query;
import com.datastax.driver.core.Row;
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

    protected List<Row> getAll() {
        CassandraQueryBuilder.connect();
        Query query = CassandraQueryBuilder.getAll(keyspace, table);
        List<Row> rows = CassandraQueryBuilder.execute(query).all();
        CassandraQueryBuilder.shutdown();
        return rows;
    }
}
