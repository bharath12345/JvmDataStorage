package com.appnomic.appsone.cassandra.dao;

import com.appnomic.appsone.cassandra.entity.MethodMetrics;

import javax.ejb.Remote;
import java.util.List;

/**
 * User: bharadwaj
 * Date: 28/06/13
 * Time: 4:45 PM
 */

@Remote
public interface JvmMethodMetricsDAO extends JimDao {

    public List<MethodMetrics> findAll();
    public List<MethodMetrics> findInTimeRange();

}
