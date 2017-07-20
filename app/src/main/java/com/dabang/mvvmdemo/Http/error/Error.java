package com.dabang.mvvmdemo.Http.error;


/**
 * author: heshantao
 * data: 2017/5/3.
 * 错误码
 */

public class Error {
    //自定义

    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;
    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;
    /**
     * 网络错误
     */
    public static final int NETWORD_ERROR = 1002;
    /**
     * 协议出错
     */
    public static final int HTTP_ERROR = 1003;

    /**
     * 证书出错
     */
    public static final int SSL_ERROR = 1005;

    /**
     * 连接超时
     */
    public static final int TIMEOUT_ERROR = 1006;
    //自定义


    //Retrofit 异常码
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;

    /*
    *
    * 业务相关
    *
    * ## 全局
    -99=服务器异常
    100=请求成功
    101=参数格式不正确
    102=token不能为空
    103=token已过期
    104=token异常

    ## 自定义
    ## 帖子相关
    3001=帖子类型不正确
    3002=帖子内容必须为3到20个字符

    ## 用户相关
    ### 参数校验 ###

    4101=用户名不能为空
    4102=手机号不能为空
    4103=密码不能为空
    4104=短信验证码不能为空
    4105=图形验证码不能为空
    4106=执照编码不能为空
    4107=执照照片不能为空
    4108=邮箱不能为空

    4201=邮箱格式不正确
    4202=手机格式不正确
    4203=密码格式不正确
    4204=账号格式不正确


    ### 结果反馈 ###
    4301=密码错误
    4302=账号不存在
    4303=短信验证码错误
    4304=图形验证码错误
    4305=token记录生成失败

    4001=token记录生成失败
    4002=短信验证码为空
    4003=参数不匹配
    4004=登陆类型为空
    4005=手机号为空
    4010=用户属性不能为空
    *
    *
    *
    *
    */


}