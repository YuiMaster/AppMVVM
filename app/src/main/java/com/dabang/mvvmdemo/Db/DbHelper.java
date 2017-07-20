package com.dabang.mvvmdemo.Db;

import android.content.Context;
import android.util.Log;

import com.dabang.mvvmdemo.Utils.AppUtils;
import com.dabang.mvvmdemo.base.Constants;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by Jane on 2017/7/20.
 */
public class DbHelper<T extends RealmObject> {
    private final String TAG = this.getClass().getSimpleName();
    Realm mRealm;
    RealmAsyncTask mRealmAsyncTask;
    RealmResults<T> results;

    //Realm 配置
    public RealmConfiguration getRealmConfiguration(Context cxt) {
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .name(Constants.DB_NAME)
//                .encryptionKey(new byte[64])
                .deleteRealmIfMigrationNeeded()
                .migration(new CustomerMigration())
                .schemaVersion(AppUtils.getVersionCode(cxt))
                .build();
        return configuration;
    }


    /**
     * 添加指定类到数据库Object以主键区别是否为同一个，存在相同就更新，不存在就添加
     *
     * @param t
     */
    public void insertOrUpdateRealmObject(final T t) {
        getRealm();
        if (mRealm == null)
            return;
        mRealmAsyncTask = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(t);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Logger.d("onSuccess: 回调成功");
                //成功回调
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Logger.d("onError: 回调失败");
                //错误回调
            }
        });

    }


    /**
     * 批量插入数据 Object以主键区别是否为同一个，存在相同就更新，不存在就添加
     *
     * @param objects
     */
    public void insertOrUpdateRealmObjects(final List<T> objects) {
        getRealm();
        if (mRealm == null)
            return;
        mRealmAsyncTask = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(objects);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                //成功回调
                Logger.d("onSuccess: 插入成功");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Logger.d("onError: 插入失败");
                //错误回调
            }
        });
    }

    /**
     * 批量插入数据 Object以主键区别是否为同一个，存在相同就更新，不存在就添加
     *
     * @param objects
     */
    public void insertOrUpdateRealmObjects(final List<T> objects, final DbInsertResultCallBack callback) {
        getRealm();
        if (mRealm == null)
            return;
        mRealmAsyncTask = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(objects);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                //成功回调
                callback.onSuccess();
                Logger.d("onSuccess: 插入成功");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Logger.d("onError: 插入失败");
                callback.onError();
                //错误回调
            }
        });
    }


    /**/
    public void insertOrUpdateRealmObjectThread(final List<T> objects) {
        getRealm();
        if (mRealm == null)
            return;
        mRealmAsyncTask = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(objects);
            }
        }, null, null);
    }

    /**/

    /**
     * 添加指定类到数据库(不存在主键)
     *
     * @param t
     */
    public void insertRealmObject(final T t) {
        getRealm();
        if (mRealm == null)
            return;
        mRealmAsyncTask = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(t);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                //成功回调
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                //错误回调
            }
        });

    }


    /**
     * 批量插入数据(不存在主键)
     *
     * @param objects
     */
    public void insertRealmObjects(final List<T> objects) {
        getRealm();
        if (mRealm == null)
            return;
        mRealmAsyncTask = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(objects);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                //成功回调
                Logger.d("insertRealmObjects onSuccess: 插入成功");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                //错误回调
                Logger.d("insertRealmObjects onError: 插入失败");
            }
        });
    }


    /**
     * 查询指定类的全部存储信息
     *
     * @param t
     * @return
     */
    public void queryRealmObjects(Class<T> t, final DbCallback callback) {
        getRealm();
        if (mRealm == null)
            return;

        results = mRealm.where(t).findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<T>>() {
            @Override
            public void onChange(RealmResults<T> element) {
                List<T> list = mRealm.copyFromRealm(element);
                callback.findAll(list);
            }
        });
    }

    /**
     * 查询指定类的全部存储信息(非主线程)
     *
     * @param t
     * @return
     */
    public void queryRealmObjectsThread(Class<T> t, final DbCallback callback) {
        final Realm realm = Realm.getDefaultInstance();
        RealmResults<T> results = realm.where(t).findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<T>>() {
            @Override
            public void onChange(RealmResults<T> element) {
                List<T> list = realm.copyFromRealm(element);
                callback.findAll(list);
                realm.close();
            }
        });

    }


    /**
     * 根据特定参数模糊查询(非主线程)
     *
     * @param t     类名
     * @param field
     * @param key
     * @return
     */
    public void fuzzyQueryRealmObjectByFieldThread(Class<T> t, String field, String key, final DbCallback callback) {
        final Realm realm = Realm.getDefaultInstance();

        RealmResults<T> results = realm.where(t).contains(field, key, Case.INSENSITIVE).findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<T>>() {
            @Override
            public void onChange(RealmResults<T> element) {
                List<T> list = realm.copyFromRealm(element);
                callback.findAll(list);
                realm.close();

            }
        });

    }


    /**
     * 根据特定参数查询
     *
     * @param t     类名
     * @param name
     * @param value
     * @return
     */
    public void queryRealmObjectByField(Class<T> t, String name, String value, final DbCallback callback) {
        getRealm();
        if (mRealm == null)
            return;

        results = mRealm.where(t).equalTo(name, value).findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<T>>() {
            @Override
            public void onChange(RealmResults<T> element) {
                List<T> list = mRealm.copyFromRealm(element);
                callback.findAll(list);

            }
        });

    }

    /**
     * 查询符合相等条件的对象
     *
     * @param t     类名
     * @param name
     * @param value 必须是int
     * @return
     */
    public void queryRealmObjectByIntEqual(Class<T> t, String name, int value, final DbCallback callback) {
        getRealm();
        if (mRealm == null)
            return;

        results = mRealm.where(t).equalTo(name, value).findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<T>>() {
            @Override
            public void onChange(RealmResults<T> element) {
                List<T> list = mRealm.copyFromRealm(element);
                callback.findAll(list);

            }
        });
    }


    /*模糊查询*/
    public void queryRealmObjectByContains(Class<T> t, String name, String value, final DbCallback callback) {
        getRealm();
        if (mRealm == null)
            return;

        results = mRealm.where(t).contains(name, value, Case.INSENSITIVE).findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<T>>() {
            @Override
            public void onChange(RealmResults<T> element) {
                List<T> list = mRealm.copyFromRealm(element);
                callback.findAll(list);

            }
        });
    }

    /*
    *
    * 查询最大值(参考下面的)
    * */
    public long queryRealmObjectTimeMax(Class<T> t, String name) {
        getRealm();
        if (mRealm == null)
            return 0l;
        Number numberMax = mRealm.where(t).max(name);
        return numberMax.longValue();
    }

    /**
     * 查并最大的版本号
     *
     * @return
     */
    public long getObjectsNumMax(Class<T> t, String parameKey, String parameValue, String maxParameValue) {
        getRealm();
        if (mRealm == null)
            return 0l;
        Number numberMax = mRealm.where(t).equalTo(parameKey, parameValue).max(maxParameValue);
        if (numberMax != null) {
            return numberMax.longValue();
        } else {
            return 0l;
        }
    }


    /**
     * 查询指定类的把结果与RxJava结合
     *
     * @param t
     * @return
     */
    public Observable rxQueryRealmObjects(Class<T> t) {
        getRealm();
        if (mRealm == null)
            return null;

        return mRealm.where(t).findAllAsync().asObservable()
                .filter(new Func1<RealmResults<T>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<T> ts) {
                        Logger.d("call: 是否加载完");
                        return ts.isLoaded();
                    }
                }).first().flatMap(new Func1<RealmResults<T>, Observable<RealmResults<T>>>() {
                    @Override
                    public Observable<RealmResults<T>> call(RealmResults<T> ts) {
                        return Observable.just(ts);
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * Rx根据特定参数查询
     *
     * @param t     类名
     * @param name  键
     * @param value 值
     * @return
     */
    public Observable rxQueryRealmObjectByField(Class<T> t, String name, String value) {
        getRealm();
        if (mRealm == null)
            return null;
        return mRealm.where(t).equalTo(name, value).findAllAsync().asObservable()
                .filter(new Func1<RealmResults<T>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<T> ts) {
                        Logger.d("call: 是否加载完");
                        return ts.isLoaded();
                    }
                }).first().flatMap(new Func1<RealmResults<T>, Observable<RealmResults<T>>>() {
                    @Override
                    public Observable<RealmResults<T>> call(RealmResults<T> ts) {
                        return Observable.just(ts);
                    }
                }).observeOn(AndroidSchedulers.mainThread());

    }


    /**
     * Rx查询某个参数的最大值
     *
     * @param t         类名
     * @param fieldName 变量名称
     * @return
     */
    public Observable rxQueryRealmObjectFieldMax(Class<T> t, String fieldName) {
        getRealm();
        if (mRealm == null)
            return null;

        Number number = mRealm.where(t).max(fieldName);
        if (number == null) {
            number = new Number() {
                @Override
                public int intValue() {
                    return -1;
                }

                @Override
                public long longValue() {
                    return 1;
                }

                @Override
                public float floatValue() {
                    return 0;
                }

                @Override
                public double doubleValue() {
                    return 0;
                }
            };
        }
        return Observable.just(number).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * Rx根据特定参数查询 2个参数
     *
     * @param t      类名
     * @param name
     * @param value
     * @param name1
     * @param value1
     * @return
     */
    public Observable rxQueryRealmObjectBy2Filed(Class<T> t, String name, String value, String name1, boolean value1) {
        getRealm();
        if (mRealm == null)
            return null;
        return mRealm.where(t).equalTo(name, value).equalTo(name1, value1).findAllAsync().asObservable()
                .filter(new Func1<RealmResults<T>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<T> ts) {
                        Logger.d("call: 是否加载完");
                        return ts.isLoaded();
                    }
                }).first().flatMap(new Func1<RealmResults<T>, Observable<List<T>>>() {
                    @Override
                    public Observable<List<T>> call(RealmResults<T> ts) {
                        List<T> list = mRealm.copyFromRealm(ts.subList(0, ts.size()));
                        return Observable.just(list);
                    }
                }).observeOn(AndroidSchedulers.mainThread());

    }


    /**
     * Rx根据特定参数查询 1个int 类型的参数
     *
     * @param t     类名
     * @param name
     * @param value
     * @return
     */
    public Observable rxQueryRealmObjectByFiledWithIntValue(Class<T> t, String name, int value) {
        getRealm();
        if (mRealm == null)
            return null;
        return mRealm.where(t).equalTo(name, value).findAllAsync().asObservable()
                .filter(new Func1<RealmResults<T>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<T> ts) {
                        Logger.d("call: 是否加载完");
                        return ts.isLoaded();
                    }
                }).first().flatMap(new Func1<RealmResults<T>, Observable<List<T>>>() {
                    @Override
                    public Observable<List<T>> call(RealmResults<T> ts) {
                        List<T> list = mRealm.copyFromRealm(ts.subList(0, ts.size()));
                        return Observable.just(list);
                    }
                }).observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * Rx根据特定参数查询 1个String 类型的参数
     *
     * @param t     类名
     * @param name
     * @param value
     * @return
     */
    public Observable rxQueryRealmObjectByFiledWithStringValue(Class<T> t, String name, String value) {
        getRealm();
        if (mRealm == null)
            return null;
        return mRealm.where(t).equalTo(name, value).findAllAsync().asObservable()
                .filter(new Func1<RealmResults<T>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<T> ts) {
                        Logger.d("call: 是否加载完");
                        return ts.isLoaded();
                    }
                }).first().flatMap(new Func1<RealmResults<T>, Observable<List<T>>>() {
                    @Override
                    public Observable<List<T>> call(RealmResults<T> ts) {
                        List<T> list = mRealm.copyFromRealm(ts.subList(0, ts.size()));
                        return Observable.just(list);
                    }
                }).observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * Rx根据特定参数查询 2个参数
     *
     * @param t      类名
     * @param name
     * @param value
     * @param name1
     * @param value1
     * @return
     */
    public Observable rxQueryRealmObjectBeforeSomeTiem(Class<T> t, String name, String value, String name1, long value1) {
        getRealm();
        if (mRealm == null)
            return null;
        return mRealm.where(t).equalTo(name, value).lessThan(name1, value1).findAllAsync().asObservable()
                .filter(new Func1<RealmResults<T>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<T> ts) {
                        Logger.d("call: 是否加载完");
                        return ts.isLoaded();
                    }
                }).first().flatMap(new Func1<RealmResults<T>, Observable<List<T>>>() {
                    @Override
                    public Observable<List<T>> call(RealmResults<T> ts) {
                        List<T> list = mRealm.copyFromRealm(ts.subList(0, ts.size()));
                        return Observable.just(list);
                    }
                }).observeOn(AndroidSchedulers.mainThread());

    }


    /**
     * 分页查找
     *
     * @param t        类名
     * @param page     第几页
     * @param pageSize 每页的数量
     * @return
     */
    public void queryRealmObjectPaginate(Class<T> t, final int page, final int pageSize, final DbCallback callback) {
        getRealm();
        if (mRealm == null)
            return;
        results = mRealm.where(t).findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<T>>() {
            @Override
            public void onChange(RealmResults<T> element) {
                List<T> list = mRealm.copyFromRealm(element);
                list = list.subList(Math.max((page - 1) * pageSize, 0), Math.min(page * pageSize, list.size()));
                callback.findAll(list);

            }
        });
    }


    /**
     * Rx分页查找
     *
     * @param t        类名
     * @param page     第几页
     * @param pageSize 每页的数量
     * @return
     */
    public Observable rxQueryRealmObjectPaginate(Class<T> t, final int page, final int pageSize) {
        getRealm();
        if (mRealm == null)
            return null;
        return mRealm.where(t).findAllAsync().asObservable()
                .filter(new Func1<RealmResults<T>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<T> ts) {
                        Logger.d("call: 是否加载完");
                        return ts.isLoaded();
                    }
                }).first().flatMap(new Func1<RealmResults<T>, Observable<List<T>>>() {
                    @Override
                    public Observable<List<T>> call(RealmResults<T> ts) {
                        List<T> list = ts.subList(Math.max((page - 1) * pageSize, 0), Math.min(page * pageSize, ts.size()));
                        return Observable.just(list);
                    }
                }).observeOn(AndroidSchedulers.mainThread());

    }


    /**
     * Rx分页查找同时降序排列
     *
     * @param t        类名
     * @param page     第几页
     * @param pageSize 每页的数量
     * @param sortName 按照什么排序
     * @return
     */
    public Observable rxQueryRealmObjectPaginateAndSort(final Class<T> t, final String sortName, final int page, final int pageSize) {
        getRealm();
        if (mRealm == null)
            return null;
        return mRealm.where(t).findAllAsync().sort(sortName, Sort.DESCENDING).asObservable()
                .filter(new Func1<RealmResults<T>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<T> ts) {
                        Logger.d("call: 是否加载完---00000----");
                        return ts.isLoaded();
                    }
                }).first().flatMap(new Func1<RealmResults<T>, Observable<List<T>>>() {
                    @Override
                    public Observable<List<T>> call(RealmResults<T> ts) {
                        Logger.d("call: 是否加载完-----11111---");
                        List<T> list = ts.subList(Math.max((page - 1) * pageSize, 0), Math.min(page * pageSize, ts.size()));
                        return Observable.just(list);
                    }
                }).observeOn(AndroidSchedulers.mainThread());

    }


    /**
     * Rx 模糊匹配
     *
     * @param t     类名
     * @param key   模糊查找关键字
     * @param field 模糊查找项
     * @return
     */
    public Observable rxFuzzyQueryRealmObject(Class<T> t, String field, String key) {
        getRealm();
        if (mRealm == null)
            return null;
        return mRealm.where(t).contains(field, key, Case.INSENSITIVE).findAllAsync().asObservable()
                .filter(new Func1<RealmResults<T>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<T> ts) {
                        Logger.d("call: 是否加载完");
                        return ts.isLoaded();
                    }
                }).first().flatMap(new Func1<RealmResults<T>, Observable<List<T>>>() {
                    @Override
                    public Observable<List<T>> call(RealmResults<T> ts) {
                        List<T> list = mRealm.copyFromRealm(ts.subList(0, ts.size()));

                        return Observable.just(list);
                    }
                }).observeOn(AndroidSchedulers.mainThread());

    }


    /**
     * 删除指定类的全部数据库信息
     */
    public void deleteRealmObjects(final Class<T> t) {
        getRealm();
        if (mRealm == null)
            return;
        mRealmAsyncTask = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(t);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Logger.d("deleteRealmObjects onSuccess: ");

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Logger.d("deleteRealmObjects onError: ");
            }
        });
    }


    public Realm getRealm() {
        if (mRealm == null) {
            Logger.d("test", "mRealm == null");
            mRealm = Realm.getDefaultInstance();
            Log.i("test", "mRealm.getPath() is " + mRealm.getPath());
            //mRealm.setAutoRefresh(false);
        }
        Log.i("test", "mRealm.getPath() is " + mRealm.getPath());
        return mRealm;
    }

    //获得异步任务
    public RealmAsyncTask getRealmAsyncTask() {
        return mRealmAsyncTask;
    }

    //realm 关闭
    public void closeRealm() {
        if (mRealm != null) {
            mRealm.close();
            mRealm = null;
        }
    }

    //取消异步任务(Activity/Fragment 销毁时候)
    public void closeRealmAsyncTask() {
        if (mRealmAsyncTask != null && !mRealmAsyncTask.isCancelled()) {
            mRealmAsyncTask.cancel();
        }
    }


}