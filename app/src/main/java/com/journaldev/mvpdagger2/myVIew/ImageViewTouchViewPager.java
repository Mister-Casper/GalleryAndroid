package com.journaldev.mvpdagger2.myVIew;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.journaldev.mvpdagger2.Data.AppPreference;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class ImageViewTouchViewPager extends android.support.v4.view.ViewPager {


    public ImageViewTouchViewPager(Context context) {
        super(context);
    }

    public ImageViewTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ImageViewTouch) {
            ImageViewTouch imageViewTouch = (ImageViewTouch) v;
            if (imageViewTouch.getScale() == imageViewTouch.getMinScale()) {
                return super.canScroll(v, checkV, dx, x, y);
            }
            return imageViewTouchCanScroll(imageViewTouch, dx);
        } else {
            return super.canScroll(v, checkV, dx, x, y);
        }
    }


    private boolean imageViewTouchCanScroll(ImageViewTouch v, int direction) {
        RectF bitmapRect = v.getBitmapRect();
        Rect imageViewRect = new Rect();
        getGlobalVisibleRect(imageViewRect);

        if (null == bitmapRect) {
            return false;
        }

        if (direction < 0) {
            return Math.abs(bitmapRect.right - imageViewRect.right) > 1.0f;
        } else {
            return Math.abs(bitmapRect.left - imageViewRect.left) > 1.0f;
        }

    }
}
