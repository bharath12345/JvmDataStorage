package com.appnomic.appsone.cassandra.service;

import javax.management.MXBean;

/**
 * User: bharadwaj
 * Date: 29/06/13
 * Time: 3:34 PM
 */
@MXBean
public interface JvmMethodMetricsService {

    public void saveMethodStat(int jvmId, long epochTime, String methodName, long invocationCount, float responseTime);

}
