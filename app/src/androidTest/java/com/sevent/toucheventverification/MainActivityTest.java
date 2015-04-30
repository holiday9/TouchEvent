package com.sevent.toucheventverification;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.ViewGroup;
import android.widget.EditText;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by htyuan on 15-4-29.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Instrumentation mInstrumentation;
    private MainActivity mActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        mInstrumentation = getInstrumentation();

        mActivity = getActivity();
    }

    public void testBuildOrderedChildList() {
        Exception ee = null;
//        ArrayList<View> buildOrderedChildList() {
        ArrayList<ViewGroup> childs = null;
        MyChildFrameLayout childFrameLayout = (MyChildFrameLayout) mActivity.findViewById(R.id.frame_child);
        try {
             childs = (ArrayList<ViewGroup>) ReflectUtil.invokeMethod(childFrameLayout, "buildOrderedChildList", null, null);
        } catch (Exception e) {
            ee = e;
            System.out.println(ee.toString());
        }

        assertNull(ee);
        assertNull(childs);
    }
}
