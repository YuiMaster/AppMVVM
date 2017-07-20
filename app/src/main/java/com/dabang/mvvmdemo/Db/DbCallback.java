package com.dabang.mvvmdemo.Db;

import java.util.List;

/**
 * Created by Jane on 2017/7/20.
 */

public interface DbCallback<T> {

    void findAll(List<T> list);
}