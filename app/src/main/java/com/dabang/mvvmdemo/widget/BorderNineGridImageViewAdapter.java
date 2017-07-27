package com.dabang.mvvmdemo.widget;

import android.content.Context;

import java.util.List;

/**
 * Created by Jane on 2017/7/26.
 */

public abstract class BorderNineGridImageViewAdapter<T> {
    protected abstract void onDisplayImage(Context context, BorderImageView imageView, T t);

    protected void onItemImageClick(Context context, BorderImageView imageView, int index, List<T> list) {
    }

    protected BorderImageView generateImageView(Context context) {
        BorderGridImageView imageView = new BorderGridImageView(context);
        imageView.setScaleType(BorderImageView.ScaleType.CENTER_CROP);
        return imageView;
    }
}
