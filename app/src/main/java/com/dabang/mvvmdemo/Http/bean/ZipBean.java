package com.dabang.mvvmdemo.Http.bean;

import com.dabang.mvvmdemo.Http.HttpResponseEntity;

/**
 * Created by Jane on 2017/7/20.
 */

public class ZipBean<T, T1> {
    HttpResponseEntity<T> tEntity;
    HttpResponseEntity<T1> t1Entity;

    public HttpResponseEntity<T> gettEntity() {
        return tEntity;
    }

    public void settEntity(HttpResponseEntity<T> tEntity) {
        this.tEntity = tEntity;
    }

    public HttpResponseEntity<T1> getT1Entity() {
        return t1Entity;
    }

    public void setT1Entity(HttpResponseEntity<T1> t1Entity) {
        this.t1Entity = t1Entity;
    }
}
