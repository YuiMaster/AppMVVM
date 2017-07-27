package com.dabang.mvvmdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.dabang.mvvmdemo.Http.bean.ZipBean;
import com.dabang.mvvmdemo.base.BaseApi;
import com.dabang.mvvmdemo.widget.BorderImageView;
import com.dabang.mvvmdemo.widget.BorderNineGridImageView;
import com.dabang.mvvmdemo.widget.BorderNineGridImageViewAdapter;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements IContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BorderNineGridImageView borderNineGridImageView = (BorderNineGridImageView) findViewById(R.id.nine_grid_image_view);
        setGridView(borderNineGridImageView);
    }

    /*
    *首页中列表九宫格图片的绑定
    */
    @SuppressWarnings("unchecked")
    public static void setGridView(BorderNineGridImageView nineGridImageView) {
        List<String> datas = new ArrayList<String>();
        for (int i = 0; i < 1; i++) {
            datas.add("asdfasdf");
        }
        BorderNineGridImageViewAdapter<String> imgAdapter = new BorderNineGridImageViewAdapter<String>() {

            @Override
            protected void onDisplayImage(Context context, BorderImageView imageView, String bean) {
                imageView.setImageResource(R.drawable.ic_test);
                imageView.setMeasureRatio(1.67f);
                imageView.setMeasureByWidth();
            }


            @Override
            protected void onItemImageClick(Context context, BorderImageView imageView, int index, List list) {
            }

            @Override
            protected BorderImageView generateImageView(Context context) {
                BorderImageView imageView = new BorderImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return imageView;
            }

        };
        //先设置adapter 后加载数据
        nineGridImageView.setAdapter(imgAdapter);
        nineGridImageView.setImagesData(datas);
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
