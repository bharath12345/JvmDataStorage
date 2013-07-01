package com.appnomic.appsone.cassandra.entity;

import java.io.Serializable;

/**
 * User: bharadwaj
 * Date: 30/06/13
 * Time: 1:51 PM
 */
public class JvmMethodMetricsRaw extends AbstractJvmMethodMetrics implements Serializable {

    private String date;
    private int dayTime;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDayTime() {
        return dayTime;
    }

    public void setDayTime(int dayTime) {
        this.dayTime = dayTime;
    }

    public String toString() {
        return "jvm_id = " + getJvmId() + " method_id = " + getMethodId() + " date = " + getDate() +
                " day_time = " + getDayTime() + " invocations = " + getInvocations() + " response_time = " + getResponseTime();
    }
}
