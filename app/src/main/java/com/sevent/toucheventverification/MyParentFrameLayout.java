package com.sevent.toucheventverification;

import android.content.Context;
import android.support.v4.util.Pools;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by htyuan on 15-4-27.
 */
public class MyParentFrameLayout extends FrameLayout{
    private static final String TAG = "MyParentFrameLayout";

    public MyParentFrameLayout(Context context) {
        super(context);
    }

    public MyParentFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public boolean dispatchTouchEvent(MotionEvent ev) {
        log("dispatchTouchEvent", ev);
        return super.dispatchTouchEvent(ev);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        log("onInterceptTouchEvent", ev);
        return super.onInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event) {
        log("onTouchEvent", event);

        return super.onTouchEvent(event);
    }

    private void log(String string , MotionEvent ev) {
        Log.d(TAG, "-------------MyParentFrameLayout--------" + string + "..." + MyChildFrameLayout.getEventDesc(ev));
    }
}
