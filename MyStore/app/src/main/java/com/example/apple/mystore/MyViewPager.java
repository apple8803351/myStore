package com.example.apple.mystore;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Apple on 2016/4/23.
 */
public class MyViewPager extends ViewPager {

    private boolean scrollEnable = true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.scrollEnable && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.scrollEnable && super.onInterceptTouchEvent(event);
    }

    public void setScrollEnable(boolean b) {
        this.scrollEnable = b;
    }
}