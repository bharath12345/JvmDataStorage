package com.appnomic.appsone.cassandra.dao;

import javax.ejb.Remote;

/**
 * User: bharadwaj
 * Date: 30/06/13
 * Time: 9:12 PM
 */

@Remote
public interface JvmMethodIdNameDAO {

    public long getMethodId(int jvmId, String methodName);
    public long setMethodIdName(int jvmId, String methodName);

}
