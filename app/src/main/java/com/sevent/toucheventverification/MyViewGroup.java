package com.sevent.toucheventverification;

/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.DragEvent;
import android.view.FocusFinder;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroupOverlay;
import android.view.ViewManager;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.Transformation;

import com.android.internal.util.Predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;

/**
 * <p>
 * A <code>ViewGroup</code> is a special view that can contain other views
 * (called children.) The view group is the base class for layouts and views
 * containers. This class also defines the
 * {@link android.view.ViewGroup.LayoutParams} class which serves as the base
 * class for layouts parameters.
 * </p>
 *
 *
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about creating user interface layouts, read the
 * <a href="{@docRoot}guide/topics/ui/declaring-layout.html">XML Layouts</a> developer
 * guide.</p></div>
 *
 * <p>Here is a complete implementation of a custom ViewGroup that implements
 * a simple {@link android.widget.FrameLayout} along with the ability to stack
 * children in left and right gutters.</p>
 *
 * {@sample development/samples/ApiDemos/src/com/example/android/apis/view/CustomLayout.java
 *      Complete}
 *
 * <p>If you are implementing XML layout attributes as shown in the example, this is the
 * corresponding definition for them that would go in <code>res/values/attrs.xml</code>:</p>
 *
 * {@sample development/samples/ApiDemos/res/values/attrs.xml CustomLayout}
 *
 * <p>Finally the layout manager can be used in an XML layout like so:</p>
 *
 * {@sample development/samples/ApiDemos/res/layout/custom_layout.xml Complete}
 *
 * @attr ref android.R.styleable#ViewGroup_clipChildren
 * @attr ref android.R.styleable#ViewGroup_clipToPadding
 * @attr ref android.R.styleable#ViewGroup_layoutAnimation
 * @attr ref android.R.styleable#ViewGroup_animationCache
 * @attr ref android.R.styleable#ViewGroup_persistentDrawingCache
 * @attr ref android.R.styleable#ViewGroup_alwaysDrawnWithCache
 * @attr ref android.R.styleable#ViewGroup_addStatesFromChildren
 * @attr ref android.R.styleable#ViewGroup_descendantFocusability
 * @attr ref android.R.styleable#ViewGroup_animateLayoutChanges
 * @attr ref android.R.styleable#ViewGroup_splitMotionEvents
 * @attr ref android.R.styleable#ViewGroup_layoutMode
 */
public abstract class MyViewGroup extends View implements ViewParent, ViewManager {
    private static final String TAG = "ViewGroup";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        ///调试用的，暂时忽略
//        if (mInputEventConsistencyVerifier != null) {
//            mInputEventConsistencyVerifier.onTouchEvent(ev, 1);
//        }
//
//        boolean handled = false;
//        /// 过滤掉不符合安全规则的事件
//        if (onFilterTouchEventForSecurity(ev)) {
//            final int action = ev.getAction();
//            final int actionMasked = action & MotionEvent.ACTION_MASK;
//
//            /// 发生向下的点击事件，代表新的手势开始，讲以前的手势取消，同时做一些必要的重置，为新的手势做准备。
//            // Handle an initial down.
//            if (actionMasked == MotionEvent.ACTION_DOWN) {
//                // Throw away all previous state when starting a new touch gesture.
//                // The framework may have dropped the up or cancel event for the previous gesture
//                // due to an app switch, ANR, or some other state change.
//                cancelAndClearTouchTargets(ev);
//                resetTouchState();
//            }
//
//            // Check for interception.
//            /// 判断是否发生拦截, 如果要进行拦截，则连down事件都传递不到子view, 直接会走到当前view的listener或者onTouchEvent
//            final boolean intercepted;
//            if (actionMasked == MotionEvent.ACTION_DOWN
//                    || mFirstTouchTarget != null) { /// 什么叫第一touch target
//                final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
//                if (!disallowIntercept) {
//                    /// 如果允许拦截传递到当前的view的onInterceptTouchEvent方法。
//                    intercepted = onInterceptTouchEvent(ev);
//                    ev.setAction(action); // restore action in case it was changed
//                } else {
//                    intercepted = false;
//                }
//            } else {
//                // There are no touch targets and this action is not an initial down
//                // so this view group continues to intercept touches.
//                intercepted = true;
//            }
//
//            // Check for cancelation.
//            /// 一但cancel子都不能接收到任何事件
//            final boolean canceled = resetCancelNextUpFlag(this)
//                    || actionMasked == MotionEvent.ACTION_CANCEL;
//
//            // Update list of touch targets for pointer down, if needed.
//            ///　如果发生了按下事件，在必要的情况下更新touch target 列表,它是一个链表
//            final boolean split = (mGroupFlags & FLAG_SPLIT_MOTION_EVENTS) != 0; /// ???
//            TouchTarget newTouchTarget = null;
//            boolean alreadyDispatchedToNewTouchTarget = false;
//            if (!canceled && !intercepted) {
//                if (actionMasked == MotionEvent.ACTION_DOWN   /// 发生了按下
//                        || (split && actionMasked == MotionEvent.ACTION_POINTER_DOWN) /// 发生多点触控
//                         || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {
//                    final int actionIndex = ev.getActionIndex(); // always 0 for down
//                    /// 获取表示当前event的id的bit filed
//                    final int idBitsToAssign = split ? 1 << ev.getPointerId(actionIndex)
//                            : TouchTarget.ALL_POINTER_IDS;
//
//                    // Clean up earlier touch targets for this pointer id in case they
//                    // have become out of sync.
//                    /// 将pointerIdBits从所有TouchTarget中移除，要遍历链表
//                    removePointersFromTouchTargets(idBitsToAssign);
//
//                    /// ???
//                    final int childrenCount = mChildrenCount;
//                    if (newTouchTarget == null && childrenCount != 0) {
//                        final float x = ev.getX(actionIndex);
//                        final float y = ev.getY(actionIndex);
//                        // Find a child that can receive the event.
//                        // Scan children from front to back.
//                        ///查找可以接收点击事件的view
//                        final ArrayList<View> preorderedList = buildOrderedChildList(); /// 一般is null
//                        final boolean customOrder = preorderedList == null
//                                /// viewGoup 渲染的顺序是否通过　getChildDrawingOrder(int, int)来获得
//                                /// 如果是这样，那么就通过getChildDrawingOrder()来获取顺序。
//                                && isChildrenDrawingOrderEnabled();   /// default is false
//                        final View[] children = mChildren;
//                        for (int i = childrenCount - 1; i >= 0; i--) {
//                            final int childIndex = customOrder
//                                    ? getChildDrawingOrder(childrenCount, i) : i; /// 返回渲染顺序，返回i
//                            final View child = (preorderedList == null)
//                                    ? children[childIndex] : preorderedList.get(childIndex);
//                            /// view　不能接收点击事件或x,y不在view的显示区域内
//                            if (!canViewReceivePointerEvents(child)   /// 是否可以接收点击事件
//                                    /// x,y转为坐标后是否在child的区域内
//                                    || !isTransformedTouchPointInView(x, y, child, null)) {
//                                continue;
//                            }
//
//                            ///遍历touchTarget中获取包含child的节点
//                            newTouchTarget = getTouchTarget(child);
//                            //扎到了新的newTouchTarget，跳出查找。
//                            if (newTouchTarget != null) {
//                                // Child is already receiving touch within its bounds.
//                                // Give it the new pointer in addition to the ones it is handling.
//                                newTouchTarget.pointerIdBits |= idBitsToAssign;
//                                break;
//                            }
//
//                            resetCancelNextUpFlag(child);
//                            /// call child dispatch touch event
//                            /// if child use the event , set the touch target.
//                            if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {  /// ???
//                                // Child wants to receive touch within its bounds.
//                                mLastTouchDownTime = ev.getDownTime();
//                                if (preorderedList != null) {
//                                    // childIndex points into presorted list, find original index
//                                    for (int j = 0; j < childrenCount; j++) {
//                                        if (children[childIndex] == mChildren[j]) {
//                                            mLastTouchDownIndex = j;
//                                            break;
//                                        }
//                                    }
//                                } else {
//                                    mLastTouchDownIndex = childIndex;
//                                }
//                                mLastTouchDownX = ev.getX();
//                                mLastTouchDownY = ev.getY();
//                                newTouchTarget = addTouchTarget(child, idBitsToAssign);
//                                alreadyDispatchedToNewTouchTarget = true;
//                                break;
//                            }
//                        }
//                        if (preorderedList != null) preorderedList.clear();
//                    }
//
//                    // 用mFirstTouchTarget给newTarouchTarget赋值
//                    if (newTouchTarget == null && mFirstTouchTarget != null) {
//                        // Did not find a child to receive the event.
//                        // Assign the pointer to the least recently added target.
//                        newTouchTarget = mFirstTouchTarget;
//                        while (newTouchTarget.next != null) {
//                            newTouchTarget = newTouchTarget.next;
//                        }
//                        newTouchTarget.pointerIdBits |= idBitsToAssign;
//                    }
//                }
//            }
//
//            /// 将事件传递给childView
//            // Dispatch to touch targets.
//            if (mFirstTouchTarget == null) {
//                // No touch targets so treat this as an ordinary view.
//                handled = dispatchTransformedTouchEvent(ev, canceled, null,
//                        TouchTarget.ALL_POINTER_IDS);
//            } else {
//                // Dispatch to touch targets, excluding the new touch target if we already
//                // dispatched to it.  Cancel touch targets if necessary.
//                TouchTarget predecessor = null;
//                TouchTarget target = mFirstTouchTarget;
//                while (target != null) {
//                    final TouchTarget next = target.next;
//                    if (alreadyDispatchedToNewTouchTarget && target == newTouchTarget) {
//                        handled = true;
//                    } else {
//                        final boolean cancelChild = resetCancelNextUpFlag(target.child)
//                                || intercepted;
//                        /// call child dispatch touch event
//                        if (dispatchTransformedTouchEvent(ev, cancelChild,
//                                target.child, target.pointerIdBits)) {
//                            handled = true;
//                        }
//                        if (cancelChild) {
//                            if (predecessor == null) {
//                                mFirstTouchTarget = next;
//                            } else {
//                                predecessor.next = next;
//                            }
//                            target.recycle();
//                            target = next;
//                            continue;
//                        }
//                    }
//                    predecessor = target;
//                    target = next;
//                }
//            }
//
//            // Update list of touch targets for pointer up or cancel, if needed.
//            /// 针对抬起事件进行重新设置
//            if (canceled
//                    || actionMasked == MotionEvent.ACTION_UP
//                    || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {
//                resetTouchState();
//            } else if (split && actionMasked == MotionEvent.ACTION_POINTER_UP) {
//                final int actionIndex = ev.getActionIndex();
//                final int idBitsToRemove = 1 << ev.getPointerId(actionIndex);
//                removePointersFromTouchTargets(idBitsToRemove);
//            }
//        }
//
//        if (!handled && mInputEventConsistencyVerifier != null) {
//            mInputEventConsistencyVerifier.onUnhandledEvent(ev, 1);
//        }
//        return handled;
//    }
//
//    /* Describes a touched view and the ids of the pointers that it has captured.
//     *
//     * This code assumes that pointer ids are always in the range 0..31 such that
//     * it can use a bitfield to track which pointer ids are present.
//     * As it happens, the lower layers of the input dispatch pipeline also use the
//     * same trick so the assumption should be safe here...
//     *
//     */
//    private static final class TouchTarget {
//        private static final int MAX_RECYCLED = 32;
//        private static final Object sRecycleLock = new Object[0];
//        private static TouchTarget sRecycleBin;
//        private static int sRecycledCount;
//
//        public static final int ALL_POINTER_IDS = -1; // all ones
//
//        // The touched child view.
//        public View child;
//
//        // The combined bit mask of pointer ids for all pointers captured by the target.
//        public int pointerIdBits;
//
//        // The next target in the target list.
//        public TouchTarget next;
//
//        private TouchTarget() {
//        }
//
//        public static TouchTarget obtain(View child, int pointerIdBits) {
//            final TouchTarget target;
//            synchronized (sRecycleLock) {
//                if (sRecycleBin == null) {
//                    target = new TouchTarget();
//                } else {
//                    target = sRecycleBin;
//                    sRecycleBin = target.next;
//                     sRecycledCount--;
//                    target.next = null;
//                }
//            }
//            target.child = child;
//            target.pointerIdBits = pointerIdBits;
//            return target;
//        }
//
//        public void recycle() {
//            synchronized (sRecycleLock) {
//                if (sRecycledCount < MAX_RECYCLED) {
//                    next = sRecycleBin;
//                    sRecycleBin = this;
//                    sRecycledCount += 1;
//                } else {
//                    next = null;
//                }
//                child = null;
//            }
//        }
//    }
//
//      //Cancels and clears all touch targets.
//    private void cancelAndClearTouchTargets(MotionEvent event) {
//        if (mFirstTouchTarget != null) {
//            boolean syntheticEvent = false;
//            if (event == null) {
//                final long now = SystemClock.uptimeMillis();
//                event = MotionEvent.obtain(now, now,
//                        MotionEvent.ACTION_CANCEL, 0.0f, 0.0f, 0);
//                event.setSource(InputDevice.SOURCE_TOUCHSCREEN);
//                syntheticEvent = true;
//            }
//
//            for (TouchTarget target = mFirstTouchTarget; target != null; target = target.next) {
//                resetCancelNextUpFlag(target.child);
//                dispatchTransformedTouchEvent(event, true, target.child, target.pointerIdBits);
//            }
//            clearTouchTargets();
//
//            if (syntheticEvent) {
//                event.recycle();
//            }
//        }
//    }
//
////    /**
////     * Removes the pointer ids from consideration.
////     * 将pointerIdBits从所有TouchTarget中移除，要遍历链表
////     */
//    private void removePointersFromTouchTargets(int pointerIdBits) {
//        TouchTarget predecessor = null;
//        TouchTarget target = mFirstTouchTarget;
//        while (target != null) {
//            final TouchTarget next = target.next;
//            if ((target.pointerIdBits & pointerIdBits) != 0) {
//                target.pointerIdBits &= ~pointerIdBits;
//                if (target.pointerIdBits == 0) {
//                    if (predecessor == null) {
//                        mFirstTouchTarget = next;
//                    } else {
//                        predecessor.next = next;
//                    }
//                    target.recycle(); /// ???
//                    target = next;
//                    continue;
//                }
//            }
//            predecessor = target;
//            target = next;
//        }
//    }
//
//    /**
//     * Transforms a motion event into the coordinate space of a particular child view,
//     * filters out irrelevant pointer ids, and overrides its action if necessary.
//     * If child is null, assumes the MotionEvent will be sent to this ViewGroup instead.
//     *
//     * 讲motion event转换为坐标后传递给child, 如果有必要，过滤掉不相干的的事件或者覆写其动作。
//     * 如果child　is null, 假设事件是传递给上一级view group的
//     */
//    private boolean dispatchTransformedTouchEvent(MotionEvent event, boolean cancel,
//            View child, int desiredPointerIdBits) {
//        final boolean handled;
//
//        // Canceling motions is a special case.  We don't need to perform any transformations
//        // or filtering.  The important part is the action, not the contents.
//        /// 用cancel结束掉所有的后续事件，可以把ACTION_CANCEL理解成什么都不做的up事件。
//        final int oldAction = event.getAction();
//        if (cancel || oldAction == MotionEvent.ACTION_CANCEL) {
//            event.setAction(MotionEvent.ACTION_CANCEL);
//            if (child == null) {
//                handled = super.dispatchTouchEvent(event);
//            } else {
//                //　把消息分发给孩子
//                handled = child.dispatchTouchEvent(event);
//            }
//            event.setAction(oldAction);
//            return handled;
//        }
//
//        // Calculate the number of pointers to deliver.
//        ///　对事件进行覆写
//        final int oldPointerIdBits = event.getPointerIdBits();
//        final int newPointerIdBits = oldPointerIdBits & desiredPointerIdBits;
//
//        // If for some reason we ended up in an inconsistent state where it looks like we
//        // might produce a motion event with no pointers in it, then drop the event.
//        ///过滤掉错误的
//        if (newPointerIdBits == 0) {
//            return false;
//        }
//
//        // If the number of pointers is the same and we don't need to perform any fancy
//        // irreversible transformations, then we can reuse the motion event for this
//        // dispatch as long as we are careful to revert any changes we make.
//        // Otherwise we need to make a copy.
//        /// 不是特别名明白为什么会这样
//        final MotionEvent transformedEvent;
//        if (newPointerIdBits == oldPointerIdBits) { /// ??? 这代表什么
//            if (child == null || child.hasIdentityMatrix()) {   /// child的转换矩阵是单位矩阵
//                if (child == null) {
//                    handled = super.dispatchTouchEvent(event);
//                } else {
//                    final float offsetX = mScrollX - child.mLeft;   /// ???
//                    final float offsetY = mScrollY - child.mTop;
//                    event.offsetLocation(offsetX, offsetY); ///　猜测是点击的点相对与view的相对坐标。
//
//                    handled = child.dispatchTouchEvent(event);
//
//                    event.offsetLocation(-offsetX, -offsetY);
//                }
//                return handled;
//            }
//            transformedEvent = MotionEvent.obtain(event);
//        } else {
//            transformedEvent = event.split(newPointerIdBits);  /// ???
//        }
//
//        // Perform any necessary transformations and dispatch.
//        if (child == null) {
//            handled = super.dispatchTouchEvent(transformedEvent);
//        } else {
//            final float offsetX = mScrollX - child.mLeft;
//            final float offsetY = mScrollY - child.mTop;
//            transformedEvent.offsetLocation(offsetX, offsetY);
//            if (! child.hasIdentityMatrix()) {
//                transformedEvent.transform(child.getInverseMatrix());
//            }
//
//            handled = child.dispatchTouchEvent(transformedEvent);
//        }
//
//        // Done.
//        transformedEvent.recycle();
//        return handled;
//    }
//
//    /**
//     * Gets the touch target for specified child view.
//     * Returns null if not found.
//     *
//     * 遍历与child相匹配的TouchTarget
//     *
//     */
//    private TouchTarget getTouchTarget(View child) {
//        for (TouchTarget target = mFirstTouchTarget; target != null; target = target.next) {
//            if (target.child == child) {
//                return target;
//            }
//        }
//        return null;
//    }
}


