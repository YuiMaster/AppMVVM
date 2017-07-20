package com.dabang.mvvmdemo;

import com.dabang.mvvmdemo.Db.DbHelper;
import com.dabang.mvvmdemo.Db.DbInsertResultCallBack;
import com.dabang.mvvmdemo.Http.HttpManager;
import com.dabang.mvvmdemo.Http.RxSubscriber;
import com.dabang.mvvmdemo.base.BaseApi;

import java.util.List;

import io.realm.RealmObject;
import rx.Observable;

/**
 * Created by Jane on 2017/7/20.
 */

public class Repository<T extends RealmObject> {

    private DbHelper dbHelper;
    private HttpManager httpManager;

    public Repository(DbHelper mDbHelper, HttpManager mHttpManager) {
    }

    public void requestData(Class clazz, BaseApi api) {
        RxSubscriber subscriber = new RxSubscriber(api);
        Observable dbObservable = dbHelper.rxQueryRealmObjects(clazz);
        Observable netObservable = httpManager.doNormalHttp(api);
        Observable.amb(dbObservable, netObservable).subscribe(subscriber);
    }

    //普通请求远程网络数据
    public void requestRemoteData(BaseApi api) {
        RxSubscriber subscriber = new RxSubscriber(api);
        Observable netObservable = httpManager.doNormalHttp(api);
        netObservable.subscribe(subscriber);
    }

    //请求远程网络数据结果是原始的Response的结构
    public void requestOriginalRemoteDate(BaseApi api) {
        RxSubscriber subscriber = new RxSubscriber(api);
        Observable netObservable = httpManager.doOriginalResponseHttp(api);
        netObservable.subscribe(subscriber);
    }


    //zip 合并2个网络请求
    public void requestZipRemoteDate(BaseApi api, BaseApi api1) {
        RxSubscriber subscriber = new RxSubscriber(api);
        Observable netObservable = httpManager.doZipHttp(api, api1);
        netObservable.subscribe(subscriber);
    }

    //zip 合并2个请求一个本地，一个网络
    public void requestZipNativeRemoteDate(Observable nativeObservable, BaseApi api) {
        RxSubscriber subscriber = new RxSubscriber(api);
        Observable zipbservable = httpManager.doZipHttpNative(nativeObservable, api);
        zipbservable.subscribe(subscriber);
    }

    //zip 合并3个网络请求
    public void requestZipThreeRemoteDate(BaseApi api, BaseApi api1, BaseApi api2) {
        RxSubscriber subscriber = new RxSubscriber(api);
//        Observable netObservable = httpManager.doThreeZipHttp(api, api1, api2);
//        netObservable.subscribe(subscriber);
    }


    //存储数据(存在主键的)
    public void insertOrUpdateData(T t) {
        dbHelper.insertOrUpdateRealmObject(t);
    }

    //批量存储数据(存在主键的)
    public void inSertOrUpdateData(List<T> list) {
        dbHelper.insertOrUpdateRealmObjects(list);
    }

    //批量存储数据(存在主键的)
    public void inSertOrUpdateData(List<T> list, DbInsertResultCallBack callBack) {
        dbHelper.insertOrUpdateRealmObjects(list, callBack);
    }


    //通过数据库获得数据
    public void requestDataFromDb(Class clazz, BaseApi api) {
        RxSubscriber subscriber = new RxSubscriber(api);
        Observable dbObservable = dbHelper.rxQueryRealmObjects(clazz);
        dbObservable.subscribe(subscriber);
    }


    //通过数据库获得数据有2个参数
    public void requestDataFromDbBy2Field(Class clazz, BaseApi api, String name, String value, String name1, boolean value1) {
        RxSubscriber subscriber = new RxSubscriber(api);
        Observable dbObservable = dbHelper.rxQueryRealmObjectBy2Filed(clazz, name, value, name1, value1);
        dbObservable.subscribe(subscriber);
    }


    //通过数据库获得数据有1个Int类型的参数
    public void requestDataFromDbByFieldWithIntValue(Class clazz, BaseApi api, String name, int value) {
        RxSubscriber subscriber = new RxSubscriber(api);
        Observable dbObservable = dbHelper.rxQueryRealmObjectByFiledWithIntValue(clazz, name, value);
        dbObservable.subscribe(subscriber);
    }

    //通过数据库获得数据有1个String类型的参数
    public void requestDataFromDbByFieldWithStringValue(Class clazz, BaseApi api, String name, String value) {
        RxSubscriber subscriber = new RxSubscriber(api);
        Observable dbObservable = dbHelper.rxQueryRealmObjectByFiledWithStringValue(clazz, name, value);
        dbObservable.subscribe(subscriber);
    }

    //通过数据库获得数据(分页)
    public void requestDataFromDb(Class clazz, BaseApi api, int page, int pageSize) {
        RxSubscriber subscriber = new RxSubscriber(api);
        Observable dbObservable = dbHelper.rxQueryRealmObjectPaginate(clazz, page, pageSize);
        dbObservable.subscribe(subscriber);
    }

    //通过数据库获得数据(模糊查询)
    public void requestDataFromDb(Class clazz, BaseApi api, String field, String key) {
        RxSubscriber subscriber = new RxSubscriber(api);
        Observable dbObservable = dbHelper.rxFuzzyQueryRealmObject(clazz, field, key);
        dbObservable.subscribe(subscriber);
    }


    //通过数据库获得数据(特定某个参数下某个时间戳之前的数据)
    public void requestDataBeforeSomeTimeFromDb(Class clazz, BaseApi api, String field, String key, String field2, long timeStemp) {
        RxSubscriber subscriber = new RxSubscriber(api);
        Observable dbObservable = dbHelper.rxQueryRealmObjectBeforeSomeTiem(clazz, field, key, field2, timeStemp);
        dbObservable.subscribe(subscriber);
    }


    //通过数据库获得数据(分页,排序)
    public void requestDataFromDb(Class clazz, BaseApi api, String sortName, int page, int pageSize) {
        RxSubscriber subscriber = new RxSubscriber(api);
        Observable dbObservable = dbHelper.rxQueryRealmObjectPaginateAndSort(clazz, sortName, page, pageSize);
        dbObservable.subscribe(subscriber);
    }

    //请求某个参数的最大值
    public void requestMaxDataFromDb(Class clazz, BaseApi api, String fileName) {
        RxSubscriber subscriber = new RxSubscriber(api);
        Observable dbObservable = dbHelper.rxQueryRealmObjectFieldMax(clazz, fileName);
        dbObservable.subscribe(subscriber);
    }

    //清空表
    public void removeDbObjects(Class clzz) {
        dbHelper.deleteRealmObjects(clzz);
    }

    //返回数据库帮助类
    public DbHelper getDbHelper() {
        return dbHelper;
    }

}
