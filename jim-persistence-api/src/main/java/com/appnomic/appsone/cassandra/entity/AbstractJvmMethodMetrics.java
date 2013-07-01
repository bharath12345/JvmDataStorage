package com.appnomic.appsone.cassandra.entity;

import java.io.Serializable;

/**
 * User: bharadwaj
 * Date: 30/06/13
 * Time: 1:58 PM
 */
public abstract class AbstractJvmMethodMetrics extends AbstractJimEntity implements Serializable {

    private int methodId;
    private long invocations;
    private float responseTime;

    public int getMethodId() {
        return methodId;
    }

    public void setMethodId(int methodId) {
        this.methodId = methodId;
    }

    public long getInvocations() {
        return invocations;
    }

    public void setInvocations(long invocations) {
        this.invocations = invocations;
    }

    public float getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(float responseTime) {
        this.responseTime = responseTime;
    }
}
