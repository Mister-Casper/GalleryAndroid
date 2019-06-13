package com.journaldev.mvpdagger2.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.journaldev.mvpdagger2.MainContract;

public class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private final MainContract.ViewCallBack imageView;

    public OnSwipeTouchListener (MainContract.ViewCallBack imageView){
        gestureDetector = new GestureDetector((Context)imageView, new GestureListener());
        this.imageView = imageView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            imageView.onBackImageButton();
                        } else {
                            imageView.onNextImageButton();
                        }
                        result = true;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }
}