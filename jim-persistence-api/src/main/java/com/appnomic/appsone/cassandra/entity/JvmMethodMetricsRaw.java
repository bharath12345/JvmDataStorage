package com.appnomic.appsone.cassandra.entity;

/**
 * User: bharadwaj
 * Date: 30/06/13
 * Time: 1:51 PM
 */
public class JvmMethodMetricsRaw extends AbstractJvmMethodMetrics {

    private String date;
    private long dayTime;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDayTime() {
        return dayTime;
    }

    public void setDayTime(long dayTime) {
        this.dayTime = dayTime;
    }
}
