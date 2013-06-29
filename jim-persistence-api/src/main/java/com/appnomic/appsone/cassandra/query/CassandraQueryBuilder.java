package com.appnomic.appsone.cassandra.query;

import com.datastax.driver.core.Query;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.*;

/**
 * User: bharadwaj
 * Date: 28/06/13
 * Time: 10:48 AM
 */
public class CassandraQueryBuilder {

    private static Builder builder;

    private Builder getSelectAll() {
        if(builder == null) {
            builder = QueryBuilder.select().all();
        }
        return builder;
    }

    public static Query getAll(String keyspace, String table) {
        return builder.from(keyspace, table);
    }

}
