package com.dabang.mvvmdemo.Http.error;

/**
 * Created by Jane on 2017/7/20.
 */

/**
 * author: heshantao
 * data: 2017/5/3.
 * 统一的返回异常
 */

public class ResponseException extends Exception {
    public int code;
    public String message;

    public ResponseException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;

    }

}