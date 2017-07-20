package com.dabang.mvvmdemo.base;

/**
 * Created by Jane on 2017/7/20.
 */

public interface IBaseView<T> {
    //加载中
    void showLoading();

    //网络错误
    void showNetWorkErro();

    //错误
    void showErro();

    //无数据
    void showEmpty();

    //重试
    void retry();

    //视图恢复
    void restorView();

}