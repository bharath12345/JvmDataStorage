package com.appnomic.appsone.cassandra.dao;

import com.appnomic.appsone.cassandra.entity.MethodMetrics;
import com.appnomic.appsone.cassandra.query.CassandraQueryBuilder;
import com.datastax.driver.core.Row;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.List;

/**
 * User: bharadwaj
 * Date: 28/06/13
 * Time: 4:50 PM
 */

@Stateless(mappedName = "JvmMethodMetricsDaoImpl")
@Remote(JvmMethodMetricsDAO.class)
public class JvmMethodMetricsDaoImpl extends AbstractDAO implements JvmMethodMetricsDAO {

    public JvmMethodMetricsDaoImpl(String cassandraHost, int cassandraPort, String keyspaceName) {
        super(cassandraHost, cassandraPort, keyspaceName);
    }

    public List<MethodMetrics> findAll() {
        List<Row> rows = session.execute(CassandraQueryBuilder.getAll(null, null)).all();
        return null;
    }

    public List<MethodMetrics> findInTimeRange() {
        return null;
    }

    public boolean persistSingle(MethodMetrics mm) {
        return false;
    }

    public boolean persistList(List<MethodMetrics> mms) {
        return false;
    }
}
