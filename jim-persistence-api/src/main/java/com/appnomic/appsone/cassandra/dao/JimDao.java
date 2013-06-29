package com.appnomic.appsone.cassandra.dao;

import com.appnomic.appsone.cassandra.entity.AbstractEntity;

import java.util.*;

/**
 * User: bharadwaj
 * Date: 28/06/13
 * Time: 4:51 PM
 */
public interface JimDao <E extends AbstractEntity> {

    public List<E> findAll();
    public List<E> findInTimeRange();

}
