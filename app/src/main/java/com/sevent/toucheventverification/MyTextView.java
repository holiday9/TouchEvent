package com.sevent.toucheventverification;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by htyuan on 15-4-27.
 */
public class MyTextView extends TextView{
    private static final String TAG = "MyTextView";

    public MyTextView(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        log("dispatchTouchEvent", ev);
        return super.dispatchTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event) {
        log("onTouchEvent", event);

        return super.onTouchEvent(event);
    }

    private void log(String string, MotionEvent ev) {
        Log.d(TAG, "-------------------------MyTextView----------------" +  string + "...." + MyChildFrameLayout.getEventDesc(ev));
    }
}
