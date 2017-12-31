package com.liushuai.wechatgane;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liushuai on 2017/12/29.
 */

public class TouchView extends View {
    public final static String TAG = "TouchView";
    private Paint mPaint;

    public final static int PARAM_Y = 0;
    private int mInterval;
    private AtomicInteger mAtomicInteger = new AtomicInteger(0);

    private Point[] mPoint = new Point[2];

    public TouchView(Context context) {
        super(context);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#FF0000"));
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (mAtomicInteger.intValue() == 1) {
            int y = mPoint[0].y - PARAM_Y;
            canvas.drawCircle(mPoint[0].x, y, 20, mPaint);
        }else if (mAtomicInteger.intValue() == 2) {
            int y1 = mPoint[0].y - PARAM_Y;
            canvas.drawCircle(mPoint[0].x, y1, 20, mPaint);

            int y2 = mPoint[1].y - PARAM_Y;
            canvas.drawCircle(mPoint[1].x, y2, 20, mPaint);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                //手势按下位置
                float x = MotionEventCompat.getX(event, 0);
                float y = MotionEventCompat.getY(event, 0);
                postInvalidate();
                if (mRunnable != null) {
                    removeCallbacks(mRunnable);
                }

                Point point = new Point((int) x, (int) y);
                mPoint[mAtomicInteger.intValue()] = point;
                postInvalidate();

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float x = MotionEventCompat.getX(event, 0);
                float y = MotionEventCompat.getY(event, 0);
                mPoint[mAtomicInteger.intValue()].set((int) x, (int) y);
                postInvalidate();
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mAtomicInteger.incrementAndGet();
                if (mAtomicInteger.intValue() == 2) {
                    //如何已经有两个点则执行adb命令实现跳转
                    mInterval = (int) (Math.sqrt(Math.pow(mPoint[0].y -mPoint[1].y, 2) + Math.pow(mPoint[0].x -mPoint[1].x, 2)) * 1.39);
                    Log.d(TAG, mInterval + "");
                    postDelayed(mRunnable, 100);
                }
                postInvalidate();
                break;

        }

        return false;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mAtomicInteger.set(0);
                    execShellClickCmd();
                    postInvalidate();
                }
            }).start();
        }
    };

    private void execShellClickCmd() {
        try {
            ShellUtils.execCommand("input swipe 400 400 400 500 " + mInterval, true);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
