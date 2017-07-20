package com.dabang.mvvmdemo.base;

import com.dabang.mvvmdemo.base.IBaseView;

/**
 * Created by Jane on 2017/7/20.
 */

public abstract class IBaseViewModle<V extends IBaseView> {

    private V controllerView;

    public IBaseViewModle(IBaseView controllerView) {
        this.controllerView = (V) controllerView;
    }

    public V getControllerView() {
        return controllerView;
    }
}
