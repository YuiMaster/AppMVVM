package com.dabang.mvvmdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.dabang.mvvmdemo.R;
import com.jaeger.ninegridimageview.ItemImageClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jane on 2017/7/25.
 */

public class BorderNineGridImageView<T> extends ViewGroup {

    public final static int STYLE_GRID = 0;     // 宫格布局
    public final static int STYLE_FILL = 1;     // 全填充布局

    private int mRowCount;       // 行数
    private int mColumnCount;    // 列数

    private int mMaxSize;        // 最大图片数
    private int mShowStyle;     // 显示风格
    private int mGap;           // 宫格间距
    private int mSingleImgSize; // 单张图片时的尺寸
    private int mGridSize;   // 宫格大小,即图片大小

    private List<BorderImageView> mImageViewList = new ArrayList<>();
    private List<T> mImgDataList;

    private BorderNineGridImageViewAdapter<T> mAdapter;
    private ItemImageClickListener<T> mItemImageClickListener;
    private float measureSingleRatio;
    private float measureMuliteRatio;

    public BorderNineGridImageView(Context context) {
        this(context, null);
    }

    public BorderNineGridImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridImageView);
        this.mGap = (int) typedArray.getDimension(R.styleable.NineGridImageView_imgGap, 0);
        this.mSingleImgSize = typedArray.getDimensionPixelSize(R.styleable.NineGridImageView_singleImgSize, -1);
        this.mShowStyle = typedArray.getInt(R.styleable.NineGridImageView_showStyle, STYLE_GRID);
        this.mMaxSize = typedArray.getInt(R.styleable.NineGridImageView_maxSize, 9);
        TypedArray borderArray = context.obtainStyledAttributes(attrs, R.styleable.BorderNineGridImageView);
        this.measureSingleRatio = borderArray.getFloat(R.styleable.BorderNineGridImageView_measure_single_ratio, 1f);
        this.measureMuliteRatio = borderArray.getFloat(R.styleable.BorderNineGridImageView_measure_mulite_ratio, 1f);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height;
        int totalWidth = width - getPaddingLeft() - getPaddingRight();
        if (mImgDataList != null && mImgDataList.size() > 0) {
            float ratio;
            if (mImgDataList.size() == 1) {
                if (mSingleImgSize == -1) {
                    mGridSize = totalWidth;
                } else {
                    mGridSize = mSingleImgSize > totalWidth ? totalWidth : mSingleImgSize;
                }
                ratio = measureSingleRatio;
            } else {
                mImageViewList.get(0).setScaleType(BorderImageView.ScaleType.CENTER_CROP);
                mGridSize = (totalWidth - mGap * (mColumnCount - 1)) / mColumnCount;
                ratio = measureMuliteRatio;
            }
            height = (int) (mGridSize / ratio) * mRowCount + mGap * (mRowCount - 1) + getPaddingTop() + getPaddingBottom();
            setMeasuredDimension(width, height);
        } else {
            height = width;
            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildrenView();
    }

    /**
     * 布局 ImageView
     */
    private void layoutChildrenView() {
        if (mImgDataList == null) {
            return;
        }
        int showCount = getNeedShowCount(mImgDataList.size());
        float ratio;
        if (showCount == 1) {
            ratio = measureSingleRatio;
        } else {
            ratio = measureMuliteRatio;
        }
        for (int i = 0; i < showCount; i++) {
            BorderImageView childrenView = (BorderImageView) getChildAt(i);
            if (mAdapter != null) {
                mAdapter.onDisplayImage(getContext(), childrenView, mImgDataList.get(i));
            }
            int rowNum = i / mColumnCount;
            int columnNum = i % mColumnCount;
            int left = (mGridSize + mGap) * columnNum + getPaddingLeft();
            int top = ((int) (mGridSize / ratio) + mGap) * rowNum + getPaddingTop();
            int right = left + mGridSize;
            int bottom = top + (int) (mGridSize / ratio);

            childrenView.layout(left, top, right, bottom);
        }
    }

    /**
     * 设置图片数据
     *
     * @param lists 图片数据集合
     */
    public void setImagesData(List lists) {
        if (lists == null || lists.isEmpty()) {
            this.setVisibility(GONE);
            return;
        } else {
            this.setVisibility(VISIBLE);
        }

        int newShowCount = getNeedShowCount(lists.size());

        int[] gridParam = calculateGridParam(newShowCount, mShowStyle);
        mRowCount = gridParam[0];
        mColumnCount = gridParam[1];
        if (mImgDataList == null) {
            int i = 0;
            while (i < newShowCount) {
                BorderImageView iv = getImageView(i);
                if (iv == null) {
                    return;
                }
                addView(iv, generateDefaultLayoutParams());
                i++;
            }
        } else {
            int oldShowCount = getNeedShowCount(mImgDataList.size());
            if (oldShowCount > newShowCount) {
                removeViews(newShowCount, oldShowCount - newShowCount);
            } else if (oldShowCount < newShowCount) {
                for (int i = oldShowCount; i < newShowCount; i++) {
                    BorderImageView iv = getImageView(i);
                    if (iv == null) {
                        return;
                    }
                    addView(iv, generateDefaultLayoutParams());
                }
            }
        }
        mImgDataList = lists;
        requestLayout();
    }

    private int getNeedShowCount(int size) {
        if (mMaxSize > 0 && size > mMaxSize) {
            return mMaxSize;
        } else {
            return size;
        }
    }

    /**
     * 获得 ImageView
     * 保证了 ImageView 的重用
     *
     * @param position 位置
     */
    private BorderImageView getImageView(final int position) {
        if (position < mImageViewList.size()) {
            return mImageViewList.get(position);
        } else {
            if (mAdapter != null) {
                BorderImageView imageView = mAdapter.generateImageView(getContext());
                mImageViewList.add(imageView);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapter.onItemImageClick(getContext(), (BorderImageView) v, position, mImgDataList);
                        if (mItemImageClickListener != null) {
                            mItemImageClickListener.onItemImageClick(getContext(), (BorderImageView) v, position, mImgDataList);
                        }
                    }
                });
                return imageView;
            } else {
                Log.e("NineGirdImageView", "Your must set a NineGridImageViewAdapter for NineGirdImageView");
                return null;
            }
        }
    }

    /**
     * 设置 宫格参数
     *
     * @param imagesSize 图片数量
     * @param showStyle  显示风格
     * @return 宫格参数 gridParam[0] 宫格行数 gridParam[1] 宫格列数
     */
    protected static int[] calculateGridParam(int imagesSize, int showStyle) {
        int[] gridParam = new int[2];
        switch (showStyle) {
            case STYLE_FILL:
                if (imagesSize < 3) {
                    gridParam[0] = 1;
                    gridParam[1] = imagesSize;
                } else if (imagesSize <= 4) {
                    gridParam[0] = 2;
                    gridParam[1] = 2;
                } else {
                    gridParam[0] = imagesSize / 3 + (imagesSize % 3 == 0 ? 0 : 1);
                    gridParam[1] = 3;
                }
                break;
            default:
            case STYLE_GRID:
                gridParam[0] = imagesSize / 3 + (imagesSize % 3 == 0 ? 0 : 1);
                gridParam[1] = 3;
        }
        return gridParam;
    }

    /**
     * 设置适配器
     *
     * @param adapter 适配器
     */
    public void setAdapter(BorderNineGridImageViewAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * 设置宫格间距
     *
     * @param gap 宫格间距 px
     */
    public void setGap(int gap) {
        mGap = gap;
    }

    /**
     * 设置显示风格
     *
     * @param showStyle 显示风格
     */
    public void setShowStyle(int showStyle) {
        mShowStyle = showStyle;
    }

    /**
     * 设置只有一张图片时的尺寸大小
     *
     * @param singleImgSize 单张图片的尺寸大小
     */
    public void setSingleImgSize(int singleImgSize) {
        mSingleImgSize = singleImgSize;
    }

    /**
     * 设置最大图片数
     *
     * @param maxSize 最大图片数
     */
    public void setMaxSize(int maxSize) {
        mMaxSize = maxSize;
    }

    public void setItemImageClickListener(ItemImageClickListener<T> itemImageViewClickListener) {
        mItemImageClickListener = itemImageViewClickListener;
    }
}
