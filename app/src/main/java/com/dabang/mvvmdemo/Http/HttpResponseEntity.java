package com.dabang.mvvmdemo.Http;

/**
 * Created by Jane on 2017/7/20.
 */

public class HttpResponseEntity<T> {
    //提示信息
    private String msg;
    //返回码
    private int code;
    //是否有下一页
    private boolean hasNext;
    //服务器时间
    private long serverTime;
    //实体类
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }
}

