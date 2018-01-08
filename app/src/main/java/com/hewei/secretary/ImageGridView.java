package com.hewei.secretary;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by fengyinpeng on 2018/1/8.
 */

public class ImageGridView extends FrameLayout {
    private static final int DM_SINGLE = 1;
    private static final int DM_FOUR = 2;
    private static final int DM_NINE = 3;
    private static final int DM_NINE_PLUS = 4;

    private int mDisplayMode;
    private ImageLoader mImageLoader;
    private int mItemSize;
    private int mItemSpace = 15;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public ImageGridView(Context context) {
        super(context);
    }

    public ImageGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public interface ImageLoader {
        void loadImage(Uri uri, ImageView imageView);
    }

    public void setImageLoader(ImageLoader loader) {
        mImageLoader = loader;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setImages(List<Uri> images) {
        if (mImageLoader == null) {
            Util.logicError("set image loader first.");
            return;
        }

        final int SIZE = images.size();

        if (SIZE > 9) {
            mDisplayMode = DM_NINE_PLUS;
        } else if (SIZE > 4 && SIZE <= 9) {
            mDisplayMode = DM_NINE;
        } else if (SIZE <= 4 && SIZE > 1) {
            mDisplayMode = DM_FOUR;
        } else if (SIZE == 1) {
            mDisplayMode = DM_SINGLE;
        }

        removeAllViews();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer idx = (Integer) v.getTag();
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(null, v, idx, idx);
                }
            }
        };

        for (int i = 0; i < Math.min(SIZE, 9); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setOnClickListener(listener);
            imageView.setTag(i);
            addView(imageView);
            mImageLoader.loadImage(images.get(i), imageView);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mDisplayMode == DM_SINGLE) {
            super.onLayout(changed, left, top, right, bottom);
        }

        int NUM_PER_LINE;
        int pos_x = getPaddingLeft();
        int pos_y = getPaddingTop();

        if (mDisplayMode == DM_FOUR) {
            NUM_PER_LINE = 2;
        } else if (mDisplayMode == DM_NINE || mDisplayMode == DM_NINE_PLUS) {
            NUM_PER_LINE = 3;
        } else {
            return;
        }

        int SIZE = getChildCount();
        int idx = 0;
        for (int i = 0; i < NUM_PER_LINE; i++) {
            for (int j = 0; j < NUM_PER_LINE; j++) {
                idx = i * NUM_PER_LINE + j;
                if (idx >= SIZE) {
                    break;
                }

                getChildAt(idx).layout(pos_x, pos_y, pos_x + mItemSize, pos_y + mItemSize);
                pos_x += (mItemSize + mItemSpace);
            }

            if (idx >= SIZE) {
                break;
            }

            pos_x = 0;
            pos_y += (mItemSize + mItemSpace);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mDisplayMode == DM_SINGLE) {
            return;
        }

        int width = getMeasuredWidth();
        int height;
        int verticalPadding = getPaddingTop() + getPaddingBottom();
        if (mDisplayMode == DM_FOUR) {
            mItemSize = (width - mItemSpace - getPaddingLeft() - getPaddingRight()) / 2;
            height = 2 * mItemSize + verticalPadding + mItemSpace;
        } else if (mDisplayMode == DM_NINE || mDisplayMode == DM_NINE_PLUS) {
            mItemSize = (width - 2 * mItemSpace - getPaddingLeft() - getPaddingRight()) / 3;
            height = 3 * mItemSize + verticalPadding + 2 * mItemSpace;
        } else {
            return;
        }

        setMeasuredDimension(width, height);
    }
}
