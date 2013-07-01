package com.appnomic.appsone.cassandra.entity;

import java.io.Serializable;

/**
 * User: bharadwaj
 * Date: 28/06/13
 * Time: 10:48 AM
 */
public abstract class AbstractJimEntity implements Serializable {

    private int jvmId;

    public int getJvmId() {
        return jvmId;
    }

    public void setJvmId(int jvmId) {
        this.jvmId = jvmId;
    }
}
