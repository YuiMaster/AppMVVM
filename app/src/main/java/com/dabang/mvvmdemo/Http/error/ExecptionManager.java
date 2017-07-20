package com.dabang.mvvmdemo.Http.error;

/**
 * Created by Jane on 2017/7/20.
 */

import android.net.ParseException;

import com.dabang.mvvmdemo.base.ApiExecption;
import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.lang.*;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * author: heshantao
 * data: 2017/1/17.
 * 异常管理类
 */

public class ExecptionManager {
    private volatile static ExecptionManager INSTANCE;
    private final String TAG = this.getClass().getSimpleName();

    //构造方法私有
    private ExecptionManager() {
    }

    //获取单例
    public static ExecptionManager getInstance() {
        if (INSTANCE == null) {
            synchronized (ExecptionManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ExecptionManager();
                }
            }
        }
        return INSTANCE;
    }

    //是否是网络异常
    public boolean isNetException(Throwable throwable) {
        if (throwable instanceof ConnectException) {
            return true;
        }

        if (throwable instanceof ConnectTimeoutException) {
            return true;
        }

        return throwable instanceof SocketTimeoutException;

    }


    //是否是服务器异常
    public boolean isApiException(Throwable throwable) {
        return throwable instanceof ApiExecption;
    }

    //是Http 请求异常
    public boolean isHttpException(Throwable throwable) {
        return throwable instanceof HttpException;
    }


    public ResponseException handleException(Throwable e) {
        ResponseException ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseException(e, Error.HTTP_ERROR);
            switch (httpException.code()) {
                case Error.UNAUTHORIZED:
                case Error.FORBIDDEN:
                case Error.NOT_FOUND:
                case Error.REQUEST_TIMEOUT:
                case Error.GATEWAY_TIMEOUT:
                case Error.INTERNAL_SERVER_ERROR:
                case Error.BAD_GATEWAY:
                case Error.SERVICE_UNAVAILABLE:
                default:
                    ex.message = "网络错误";
                    break;
            }
            return ex;
        } else if (e instanceof ApiExecption) {
            ApiExecption resultException = (ApiExecption) e;
            ex = new ResponseException(resultException, resultException.code);
            ex.message = resultException.msg;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ResponseException(e, Error.PARSE_ERROR);
            ex.message = "解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseException(e, Error.NETWORD_ERROR);
            ex.message = "连接失败";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponseException(e, Error.SSL_ERROR);
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex = new ResponseException(e, Error.TIMEOUT_ERROR);
            ex.message = "连接超时";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponseException(e, Error.TIMEOUT_ERROR);
            ex.message = "连接超时";
            return ex;
        } else {
            ex = new ResponseException(e, Error.UNKNOWN);
            ex.message = "未知错误";
            return ex;
        }
    }

}