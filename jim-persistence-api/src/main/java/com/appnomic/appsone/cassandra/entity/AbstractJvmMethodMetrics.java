package com.appnomic.appsone.cassandra.entity;

/**
 * User: bharadwaj
 * Date: 30/06/13
 * Time: 1:58 PM
 */
public abstract class AbstractJvmMethodMetrics extends AbstractJimEntity {

    private long methodId;
    private long invocations;
    private float responseTime;

    public long getMethodId() {
        return methodId;
    }

    public void setMethodId(long methodId) {
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
