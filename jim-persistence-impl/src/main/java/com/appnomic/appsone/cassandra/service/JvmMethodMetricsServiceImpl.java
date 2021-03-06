package com.appnomic.appsone.cassandra.service;

import com.appnomic.appsone.cassandra.dao.JvmMethodIdNameDAO;
import com.appnomic.appsone.cassandra.dao.JvmMethodMetricsDAO;
import com.appnomic.appsone.cassandra.entity.JvmMethodMetricsRaw;
import com.appnomic.appsone.cassandra.utility.CassandraDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * User: bharadwaj
 * Date: 29/06/13
 * Time: 3:28 PM
 */

@Startup
@Singleton
public class JvmMethodMetricsServiceImpl implements JvmMethodMetricsService {

    private static final Logger logger = LoggerFactory.getLogger(JvmMethodMetricsServiceImpl.class);

    public static final String NAME = "com.appnomic.appsone.cassandra:service=JvmMethodMetrics";
    private MBeanServer server;
    private ObjectName objectName;

    @EJB(mappedName = "java:global/jim-ear/jim-persistence-impl/JvmMethodMetricsDaoImpl!com.appnomic.appsone.cassandra.dao.JvmMethodMetricsDAO")
    private JvmMethodMetricsDAO jvmMethodMetricsDAO;

    @EJB(mappedName = "java:global/jim-ear/jim-persistence-impl/JvmMethodIdNameDaoImpl!com.appnomic.appsone.cassandra.dao.JvmMethodIdNameDAO")
    private JvmMethodIdNameDAO jvmMethodIdNameDAO;

    @PostConstruct
    public void start() {
        try {
            objectName = new ObjectName(NAME);
            server = ManagementFactory.getPlatformMBeanServer();
            server.registerMBean(this, objectName);
            System.out.println("Registered " + NAME + " Mbean");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stop() {
        try {
            if(server != null && objectName != null) {
                server.unregisterMBean(objectName);
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    @Override
    public void saveMethodStat(int jvmId, long epochTime, String methodName, long invocationCount, float responseTime) {
        int methodId = jvmMethodIdNameDAO.getMethodId(jvmId, methodName);
        if(methodId == -1) {
            methodId = jvmMethodIdNameDAO.setMethodIdName(jvmId, methodName);
        }

        System.out.println("method name = " + methodName + " method id = " + methodId);

        JvmMethodMetricsRaw jvmMethodMetricsRaw = new JvmMethodMetricsRaw();
        jvmMethodMetricsRaw.setJvmId(jvmId);
        jvmMethodMetricsRaw.setMethodId(methodId);
        jvmMethodMetricsRaw.setInvocations(invocationCount);
        jvmMethodMetricsRaw.setResponseTime(responseTime);

        String date = CassandraDateTime.getDate(epochTime);
        jvmMethodMetricsRaw.setDate(date);
        int dayTime = CassandraDateTime.getDayTime(epochTime);
        jvmMethodMetricsRaw.setDayTime(dayTime);
        System.out.println("date = " + date + " dayTime = " + dayTime);

        try {
            jvmMethodMetricsDAO.persistSingle(jvmMethodMetricsRaw);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
