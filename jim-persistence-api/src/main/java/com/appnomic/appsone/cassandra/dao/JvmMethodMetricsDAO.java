package com.appnomic.appsone.cassandra.dao;

import com.appnomic.appsone.cassandra.entity.JvmMethodMetricsRaw;

import javax.ejb.Remote;
import java.util.List;

/**
 * User: bharadwaj
 * Date: 28/06/13
 * Time: 4:45 PM
 */

@Remote
public interface JvmMethodMetricsDAO extends JimDao {

    public List<JvmMethodMetricsRaw> findAll();
    public List<JvmMethodMetricsRaw> findAllInTimeRange(long epochStartTime, long epochEndTime);

    public List<JvmMethodMetricsRaw> findForJvm(int jvmId);
    public List<JvmMethodMetricsRaw> findInTimeRangeForJvm(int jvmId, long epochStartTime, long epochEndTime);


}
