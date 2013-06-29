package com.appnomic.appsone.cassandra.service;

import com.appnomic.appsone.cassandra.dao.JvmMethodMetricsDAO;
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
public class JvmMethodMetricsServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(JvmMethodMetricsServiceImpl.class);

    public static final String NAME = "com.appnomic.service.api.impl:service=AlertServiceTest";
    private MBeanServer server;
    private ObjectName objectName;

    @EJB(mappedName = "java:global/jim-ear/appsone-service-api-impl/AlertDataDaoImpl!com.appnomic.service.api.AlertDataDao")
    private JvmMethodMetricsDAO jvmMethodMetricsDAO;


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
}
