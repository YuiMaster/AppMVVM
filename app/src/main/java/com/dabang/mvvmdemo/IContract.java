package com.dabang.mvvmdemo;

import com.dabang.mvvmdemo.Http.bean.ZipBean;
import com.dabang.mvvmdemo.base.BaseApi;
import com.dabang.mvvmdemo.Http.callback.HttpErroCallback;
import com.dabang.mvvmdemo.base.IBaseView;
import com.dabang.mvvmdemo.base.IBaseViewModle;


/**
 * Created by Jane on 2017/7/20.
 */

public interface IContract {

    interface View<T, T1> extends IBaseView, HttpErroCallback {
        void updateContentList(ZipBean bean);
    }


    abstract class ViewModule extends IBaseViewModle<View> {
        public ViewModule(IBaseView controllerView) {
            super(controllerView);
        }

        //请求内容信息
        public abstract void requestContentList(BaseApi api);
    }
}
