package com.dabang.mvvmdemo.Http;

import com.dabang.mvvmdemo.Http.error.ExecptionManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * author: heshantao
 * data: 2017/5/3.
 */

public class HttpResponseExceptionFun<T> implements Func1<Throwable, Observable<T>> {
    @Override
    public Observable<T> call(Throwable throwable) {
        return Observable.error(ExecptionManager.getInstance().handleException(throwable));
    }
}
