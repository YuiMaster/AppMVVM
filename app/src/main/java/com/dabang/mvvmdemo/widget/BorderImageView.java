package com.dabang.mvvmdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.dabang.mvvmdemo.R;
import com.dabang.mvvmdemo.Utils.ScreenUtil;

/**
 * Created by Jane on 2017/7/25.
 * 带边框的ImageView
 */

public class BorderImageView extends RatioImageView {
    private float sideBorder;  //边框厚度 dp
    private int borderColor;    //边框颜色

    public BorderImageView(Context context) {
        this(context, null);
    }

    public BorderImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BorderImageView);
        borderColor = a.getColor(R.styleable.BorderImageView_border_color, ContextCompat.getColor(getContext(), R.color.colorAccent));
        sideBorder = a.getDimension(R.styleable.BorderImageView_side_border, 2);
        final float sideOffset = sideBorder * 2;
        setPadding(getLeft(), getTop(), getRight() + (int) sideOffset, getBottom() + (int) sideOffset);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(sideBorder, sideBorder);
        super.onDraw(canvas);

        final int sideBorderPx = ScreenUtil.dip2px(getContext(), sideBorder);
        // 画边框
        Paint paint = new Paint();
        paint.setColor(borderColor); //颜色
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(sideBorderPx); //setStrokeWidth单位是像素
        canvas.drawRect(canvas.getClipBounds(), paint);
    }
}
