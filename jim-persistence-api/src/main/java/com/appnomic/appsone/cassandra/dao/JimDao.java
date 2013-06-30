package com.appnomic.appsone.cassandra.dao;

import com.appnomic.appsone.cassandra.entity.AbstractJimEntity;
import com.appnomic.appsone.cassandra.entity.JvmMethodMetricsRaw;

import java.util.*;

/**
 * User: bharadwaj
 * Date: 28/06/13
 * Time: 4:51 PM
 */
public interface JimDao <E extends AbstractJimEntity> {

    public List<E> findAll();
    public List<E> findAllInTimeRange(long epochStartTime, long epochEndTime);

    public List<E> findForJvm(int jvmId);
    public List<E> findInTimeRangeForJvm(int jvmId, long epochStartTime, long epochEndTime);

}
