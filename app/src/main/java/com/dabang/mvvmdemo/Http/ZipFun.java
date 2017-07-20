package com.dabang.mvvmdemo.Http;

import com.dabang.mvvmdemo.Http.bean.ZipBean;
import com.dabang.mvvmdemo.base.ApiExecption;
import com.dabang.mvvmdemo.base.Constants;

import rx.functions.Func1;

/**
 * author: heshantao
 * data: 2017/5/4.
 * 2个合并网络请求函数
 */

public class ZipFun implements Func1<ZipBean, ZipBean> {
    @Override
    public ZipBean call(ZipBean zipBean) {
        int code = zipBean.gettEntity().getCode();
        int code1 = zipBean.getT1Entity().getCode();
        if (code != Constants.Http.HTTP_OK_STATUS
                && code != Constants.Http.HTTP_OK_STATUS) {
            throw new ApiExecption(code, zipBean.gettEntity().getMsg() != null ? zipBean.gettEntity().getMsg() : "");
        }
        return zipBean;
    }
}
