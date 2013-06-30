package com.appnomic.appsone.cassandra.entity;

/**
 * User: bharadwaj
 * Date: 30/06/13
 * Time: 1:51 PM
 */
public class JvmMethodIdNameMap extends AbstractJimEntity {

    private long methodId;
    private String methodName;

    public long getMethodId() {
        return methodId;
    }

    public void setMethodId(long methodId) {
        this.methodId = methodId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
