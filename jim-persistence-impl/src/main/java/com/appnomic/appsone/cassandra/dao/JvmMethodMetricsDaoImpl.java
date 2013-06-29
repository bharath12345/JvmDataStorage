package com.appnomic.appsone.cassandra.dao;

import com.appnomic.appsone.cassandra.entity.MethodMetrics;
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
public class JvmMethodMetricsDaoImpl extends CassandraDAO implements JvmMethodMetricsDAO {

    public JvmMethodMetricsDaoImpl() {
        keyspace = "JvmMethodMetrics";
        table = "JvmMethodMetricsRaw";
    }

    public List<MethodMetrics> findAll() {
        List<Row> rows = getAll();
        // convert raw rows to MethodMetrics objects
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
