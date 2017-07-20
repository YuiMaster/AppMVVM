package com.dabang.mvvmdemo;

import android.app.Activity;
import android.os.Bundle;

import com.dabang.mvvmdemo.Http.bean.ZipBean;
import com.dabang.mvvmdemo.base.BaseApi;

import java.util.List;

public class MainActivity extends Activity implements IContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void doHttpErro(Throwable e) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showNetWorkErro() {

    }

    @Override
    public void showErro() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void retry() {

    }

    @Override
    public void restorView() {

    }

    @Override
    public void updateContentList(ZipBean list) {

    }
}
