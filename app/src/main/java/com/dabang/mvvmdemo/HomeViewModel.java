package com.dabang.mvvmdemo;

import com.dabang.mvvmdemo.Http.bean.ZipBean;
import com.dabang.mvvmdemo.base.BaseApi;
import com.dabang.mvvmdemo.Http.callback.HttpOnNextCallback;
import com.dabang.mvvmdemo.base.IBaseView;

import javax.inject.Inject;

/**
 * Created by Jane on 2017/7/20.
 */

public class HomeViewModel extends IContract.ViewModule {

    HttpOnNextCallback<ZipBean> contentCallBack = new HttpOnNextCallback<ZipBean>() {
        @Override
        public void onNext(ZipBean zipBean) {
            getControllerView().updateContentList(zipBean);
        }

        @Override
        public void onError(Throwable e) {
            getControllerView().doHttpErro(e);
        }
    };

    private Repository mRepository;

    @Inject
    public HomeViewModel(Repository repository, IBaseView view) {
        super(view);
        this.mRepository = repository;
    }

    @Override
    public void requestContentList(BaseApi api) {
        api.setCallback(contentCallBack);
        mRepository.requestRemoteData(api);
    }
}
