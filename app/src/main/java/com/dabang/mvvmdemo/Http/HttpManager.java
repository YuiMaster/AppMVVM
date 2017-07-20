package com.dabang.mvvmdemo.Http;

import android.content.Context;

import com.dabang.mvvmdemo.Utils.HttpsUtils;
import com.dabang.mvvmdemo.base.BaseApi;
import com.dabang.mvvmdemo.base.Constants;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.InputStream;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * author: heshantao
 * data: 2017/1/18.
 */

public class HttpManager<T, T1, T2> {
    private static final String TAG = HttpManager.class.getSimpleName();

    public OkHttpClient getOkhttpClient(Context context, Interceptor interceptor) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (Constants.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        long maxCacheSize = 1000 * 1000 * 50;
        Cache cache = new Cache(CacheUtils.getOkHttpFile(context), maxCacheSize);
        OkHttpClient.Builder builder = null;
        try {
            InputStream is = null;
            is = context.getResources().getAssets().open("server.cer");

            InputStream[] inputStreams = new InputStream[]{is};

            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(inputStreams, null, null);

            builder = new OkHttpClient.Builder()
                    .cookieJar(new PersistentCookieJar(new SetCookieCache(),
                            new SharedPrefsCookiePersistor(context)))//session,cookie 持久化
                    .addInterceptor(new HeaderInterceptor(context))
                    .cache(cache) //缓存 okhttp3 本来是存在的不设置不起作用
                    .addInterceptor(new CacheInterceptor(context))
                    .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                    .addInterceptor(new HttpLoggingInterceptor())
                    .addInterceptor(interceptor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public Retrofit getRetrofit(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.Http.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }


    public void doHttpDeal(BaseApi basePar) {
        RxSubscriber subscriber = new RxSubscriber(basePar);
        Observable observable = null;
        observable.subscribe(subscriber);
    }


    //普通的请求
    public Observable doNormalHttp(BaseApi basePar) {
        Observable observable = null;
        return observable;
    }

    //返回原始的Response的请求
    public Observable doOriginalResponseHttp(BaseApi basePar) {
        Observable observable = null;
        return observable;
    }


    //合并2个请求
    public Observable doZipHttp(BaseApi mApi, BaseApi mApi1) {
        Observable observable = null;
        return observable;
    }

    //合并请求一个是网络的一个是本地的
    public Observable doZipHttpNative(Observable nativeObservable, BaseApi mApi) {
        Observable observable = null;
        return observable;
    }
}
