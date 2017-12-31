package com.liushuai.wechatgane;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by liushuai on 2017/12/29.
 */

public class DetectinService extends AccessibilityService {
    public final static String TAG = "DetectinService";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

        int type = accessibilityEvent.getEventType();
        Log.d(TAG, "DetectinService ===" + type);
        Log.d(TAG, "className ===" + accessibilityEvent.getClassName());
        //Log.d(TAG, "className ===" + accessibilityEvent.get);
    }

    private void initTouchWindow() {

    }

    @Override
    public void onInterrupt() {

    }
}
