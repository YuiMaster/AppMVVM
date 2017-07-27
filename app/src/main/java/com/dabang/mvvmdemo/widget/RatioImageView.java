package com.dabang.mvvmdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.dabang.mvvmdemo.R;

/**
 * Created by Jane on 2017/7/26.
 * 根据固定宽高比调节宽高的ImageView
 * 同时设置宽高，只有宽度生效。
 * 默认不使用宽高比
 */

public class RatioImageView extends AppCompatImageView {
    protected float measureRatio;    //宽高比
    protected boolean measureByWidth;    //以宽度为基准
    protected boolean measureByHeight;   //以高度为基准

    public RatioImageView(Context context) {
        this(context, null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
        measureByHeight = a.getBoolean(R.styleable.RatioImageView_measure_by_height, false);
        measureByWidth = a.getBoolean(R.styleable.RatioImageView_measure_by_width, false);
        measureRatio = a.getFloat(R.styleable.RatioImageView_measure_ratio, 1f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (measureByWidth) {
            final int width = MeasureSpec.getSize(widthMeasureSpec);
            super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec((int) (width / measureRatio), MeasureSpec.EXACTLY));
        } else if (measureByHeight) {
            final int height = MeasureSpec.getSize(heightMeasureSpec);
            super.onMeasure(MeasureSpec.makeMeasureSpec((int) (height / measureRatio), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 设置宽高比
     *
     * @param ratio
     */
    public void setMeasureRatio(float ratio) {
        if (ratio > 0) {
            measureRatio = ratio;
        }
    }

    /**
     * 宽高比以宽度为基准
     */
    public void setMeasureByWidth() {
        measureByWidth = true;
        measureByHeight = false;
    }

    /**
     * 宽高比以高度为基准
     */
    public void setMeasureByHeight() {
        measureByHeight = true;
        measureByWidth = false;
    }

}
