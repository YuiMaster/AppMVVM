package com.dabang.mvvmdemo.Http;

/**
 * Created by Jane on 2017/7/20.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;

import com.dabang.mvvmdemo.Http.error.ExecptionManager;
import com.dabang.mvvmdemo.base.BaseApi;
import com.dabang.mvvmdemo.Http.callback.HttpOnNextCallback;
import com.dabang.mvvmdemo.base.IBaseView;
import com.trello.rxlifecycle.LifecycleProvider;

import java.lang.ref.SoftReference;

import rx.Subscriber;

/**
 * author: heshantao
 * data: 2017/1/17.
 * Subscriber 封装实现类
 */

public class RxSubscriber<T> extends Subscriber<T> {
    //    HttpDialogManager dialogManager;
    /*是否弹框*/
    private boolean showPorgressDialog = false;
    /*是否显示LoadingView*/
    private boolean showVaryLoadingView = false;
    /* 软引用回调接口*/
    private SoftReference<HttpOnNextCallback> mSubscriberOnNextListener;
    /*软引用反正内存泄露*/
    private SoftReference<LifecycleProvider> lifeProvide;
    /*加载框可自己定义*/
    private AppCompatDialogFragment progressDialog;
    private IBaseView baseView;
    /*请求数据*/
    private BaseApi mApi;

    public RxSubscriber(BaseApi api) {
        this.mApi = api;
        this.mSubscriberOnNextListener = api.getCallback();
        this.lifeProvide = new SoftReference<>(api.getLifeProvider());
        setShowLoadingView(mApi.isShowVaryLoadingView());
        setShowPorgress(mApi.isShowProgressDialog());
        baseView = mApi.getVaryView();
        initHttpDialogManager();
        progressDialog = mApi.getProgressDialog();
        setShowPorgress(api.isShowProgressDialog());
    }


    private void initHttpDialogManager() {
        if (lifeProvide.get() != null) {
            FragmentManager manager = null;
            if (lifeProvide.get() instanceof Fragment) {
                if (!((Fragment) lifeProvide.get()).isAdded()) {// avoid Fragment has not been attached yet.
                    return;
                }
                manager = ((Fragment) lifeProvide.get()).getChildFragmentManager();
            } else if (lifeProvide.get() instanceof AppCompatActivity) {
                manager = ((AppCompatActivity) lifeProvide.get()).getSupportFragmentManager();
            }
            if (manager != null) {
//                dialogManager = new HttpDialogManager(manager);
            }
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }


    public boolean isShowPorgress() {
        return showPorgressDialog;
    }

    /**
     * 是否需要弹框设置
     *
     * @param showPorgress
     */
    public void setShowPorgress(boolean showPorgress) {
        this.showPorgressDialog = showPorgress;
    }

    public boolean isShowLoadingView() {
        return showVaryLoadingView;
    }

    /**
     * 是否需要loadingView
     *
     * @param showLoading
     */
    public void setShowLoadingView(boolean showLoading) {
        this.showVaryLoadingView = showLoading;
    }


    private void showVaryLoadingView() {
        if (!isShowLoadingView() || baseView == null) return;
        baseView.showLoading();
    }


    private void showVaryRestorView() {
        if (baseView == null) return;
        baseView.restorView();
    }


    private void showErroView() {
        if (baseView == null) return;
        baseView.showErro();
    }


    private void showNetErroView() {
        if (baseView == null) return;
        baseView.showNetWorkErro();
    }

    /**
     * 显示加载框
     */
    private void showProgressDialog() {
//        if (!isShowPorgress() || dialogManager == null || progressDialog == null) return;
//        dialogManager.showLoadingDialog(progressDialog);

    }

    /**
     * 隐藏加载框
     */
    private void dismissProgressDialog() {
//        if (!isShowPorgress() || dialogManager == null || dialogManager == null) return;
//        dialogManager.dissLoadingDialog();
    }

    private void dismissDialog() {
        dismissProgressDialog();
    }


    @Override
    public void onStart() {
        Log.d("TAG", "--------onStart:---1111---- ");
        showProgressDialog();
        showVaryLoadingView();
    }

    @Override
    public void onCompleted() {
        Log.d("TAG", "--------onCompleted:---1111---- ");
        dismissDialog();
        showVaryRestorView();
    }

    @Override
    public void onError(Throwable e) {
        Log.d("TAG", "--------onError:---1111---- ");
        dismissDialog();
        doErro(e);
    }

    /*错误统一处理*/
    private void doErro(Throwable e) {
        Throwable mThrowable = e.getCause();
        ExecptionManager manager = ExecptionManager.getInstance();
        if (manager.isNetException(mThrowable) || manager.isHttpException(mThrowable)) {
            showNetErroView();
        } else {
            showErroView();
        }

        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onError(mThrowable);
        }
    }

    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onNext(t);
        }
    }
}
