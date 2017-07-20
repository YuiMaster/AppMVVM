package com.dabang.mvvmdemo.Http.bean;

import com.dabang.mvvmdemo.Http.HttpResponseEntity;

/**
 * Created by Jane on 2017/7/20.
 */

public class NativeHttpZipBean<T,T1> {
    T t;
    HttpResponseEntity<T1> entity;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public HttpResponseEntity<T1> getEntity() {
        return entity;
    }

    public void setEntity(HttpResponseEntity<T1> entity) {
        this.entity = entity;
    }
}