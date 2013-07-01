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

    public static final String NAME = "com.appnomic.appsone.cassandra.service=JvmMethodMetrics";
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
            logger.info("Registered " +  NAME + " Mbean");
        } catch (Exception e) {
            logger.error("Can't register " + NAME,e);
        }
    }

    @PreDestroy
    public void stop() {
        try {
            if(server != null && objectName != null) {
                server.unregisterMBean(objectName);
            }
        } catch (Exception e) {
            logger.error("Can't unregister " + NAME,e);
        }
    }

    @Override
    public void saveMethodStat(int jvmId, long epochTime, String methodName, long invocationCount, float responseTime) {
        long methodId = jvmMethodIdNameDAO.getMethodId(jvmId, methodName);
        if(methodId == -1) {
            methodId = jvmMethodIdNameDAO.setMethodIdName(jvmId, methodName);
        }

        logger.info("method name = " + methodName + " method id = " + methodId);

        JvmMethodMetricsRaw jvmMethodMetricsRaw = new JvmMethodMetricsRaw();
        jvmMethodMetricsRaw.setJvmId(jvmId);
        jvmMethodMetricsRaw.setMethodId(methodId);
        jvmMethodMetricsRaw.setInvocations(invocationCount);
        jvmMethodMetricsRaw.setResponseTime(responseTime);
        jvmMethodMetricsRaw.setDate(CassandraDateTime.getDate(epochTime));
        jvmMethodMetricsRaw.setDayTime(CassandraDateTime.getDayTime(epochTime));
        jvmMethodMetricsDAO.persistSingle(jvmMethodMetricsRaw);
    }

}
