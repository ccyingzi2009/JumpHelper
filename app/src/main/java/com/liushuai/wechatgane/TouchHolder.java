package com.liushuai.wechatgane;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

/**
 * @description:
 * @company: Netease
 * @author: ls
 * @version: Created on 17/4/18.
 */

public class TouchHolder {

    private WindowManager mWindowManager;
    private TouchView mTouchView;
    private View mControlView;

    private static TouchHolder mInstance;

    private TouchHolder() {
        init();
    }

    public static TouchHolder getInstance() {
        if (null == mInstance) {
            synchronized (TouchHolder.class) {
                if (null == mInstance) {
                    mInstance = new TouchHolder();
                }
            }
        }
        return mInstance;
    }

    private void init() {
        mWindowManager = (WindowManager) BaseApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        //addTouchView();
        addControlView();
    }

    public void addTouchView() {
        mTouchView = (TouchView) LayoutInflater.from(BaseApplication.getInstance()).inflate(R.layout.touch_layout, null);
        WindowManager.LayoutParams mParams;

        mParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST);
        mParams.height = Utils.getHeight() * 2 / 5;
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        if (Build.VERSION.SDK_INT > 24) {
            mParams.type = WindowManager.LayoutParams.TYPE_PHONE; //toast类型
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_TOAST; //toast类型
        }

        mParams.x = 0;
        mParams.y = 0;
        int gravity = Gravity.getAbsoluteGravity(Gravity.CENTER | Gravity.START, View.LAYOUT_DIRECTION_LTR);
        mParams.gravity = gravity;
        mParams.flags = mParams.flags | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mWindowManager.addView(mTouchView, mParams);
    }

    public void removeTouchView() {
        if (mTouchView != null) {
            mWindowManager.removeView(mTouchView);
            mTouchView = null;
        }
    }

    private void addControlView() {
        mControlView = LayoutInflater.from(BaseApplication.getInstance()).inflate(R.layout.back_layout, null);

        final WindowManager.LayoutParams mParams;

        mParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST);
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //解决Android 7.1.1起不能再用Toast的问题（先解决crash）
            if (Build.VERSION.SDK_INT > 24) {
                mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        mParams.x = 0;
        mParams.y = -100;
        int gravity = Gravity.getAbsoluteGravity(Gravity.BOTTOM | Gravity.START, View.LAYOUT_DIRECTION_LTR);
        mParams.gravity = gravity;
        mParams.flags = mParams.flags | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowManager.addView(mControlView, mParams);

//        mControlView.setOnTouchListener(new View.OnTouchListener() {
//
//            float mStartX = 0;
//            float mStartY = 0;
//            float mDownTime;
//
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                // 当前值以屏幕左上角为原点
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        mStartX = MotionEventCompat.getX(motionEvent, 0);
//                        mStartY = MotionEventCompat.getY(motionEvent, 0);
//                        mDownTime = System.currentTimeMillis();
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:
//                        mParams.x += MotionEventCompat.getX(motionEvent, 0) - mStartX;
//                        mParams.y += MotionEventCompat.getY(motionEvent, 0) - mStartY;
//                        Log.i("aax = ", mParams.x + "");
//                        Log.i("aay = ", mParams.y + "");
//                        mWindowManager.updateViewLayout(mControlView, mParams);
//                        mStartX = motionEvent.getRawX();
//                        mStartY = motionEvent.getRawY();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        return System.currentTimeMillis() - mDownTime > 200;
//                }
//
//                // 消耗触摸事件
//                return true;
//            }
//        });
        mControlView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTouchView != null) {
                    removeTouchView();
                } else {
                    addTouchView();
                }
            }
        });
    }

}
