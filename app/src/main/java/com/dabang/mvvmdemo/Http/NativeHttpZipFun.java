package com.dabang.mvvmdemo.Http;

import com.dabang.mvvmdemo.Http.bean.NativeHttpZipBean;
import com.dabang.mvvmdemo.base.ApiExecption;
import com.dabang.mvvmdemo.base.Constants;

import rx.functions.Func1;

/**
 * Created by Jane on 2017/7/20.
 */


public class NativeHttpZipFun implements Func1<NativeHttpZipBean, NativeHttpZipBean> {
    @Override
    public NativeHttpZipBean call(NativeHttpZipBean nativeHttpZipBean) {
        int code = nativeHttpZipBean.getEntity().getCode();
        if (code != Constants.Http.HTTP_OK_STATUS) {
            throw new ApiExecption(code, nativeHttpZipBean.getEntity().getMsg() != null ? nativeHttpZipBean.getEntity().getMsg() : "");
        }
        return nativeHttpZipBean;
    }
}