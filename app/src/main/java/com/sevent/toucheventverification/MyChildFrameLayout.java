package com.sevent.toucheventverification;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by htyuan on 15-4-27.
 */
public class MyChildFrameLayout extends FrameLayout{
    private static final String TAG = "MyChildFrameLayout";

    public MyChildFrameLayout(Context context) {
        super(context);
    }

    public MyChildFrameLayout(Context context, AttributeSet attrs) {
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

    private void log(String string, MotionEvent ev) {
        Log.d(TAG,"-------MyChildFrameLayout------" + string + "..." + getEventDesc(ev));
    }

    public static String getEventDesc(MotionEvent ev) {
        if (ev == null) {
            return "";
        }

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return "DOWN";
            case MotionEvent.ACTION_UP:
                return "UP";
            case MotionEvent.ACTION_MOVE:
                return "MOVE";
            default:
                return "OTHER";
        }
    }
}
