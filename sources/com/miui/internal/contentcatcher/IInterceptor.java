package com.miui.internal.contentcatcher;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public interface IInterceptor {
    boolean dispatchKeyEvent(KeyEvent keyEvent, View view, Activity activity);

    boolean dispatchTouchEvent(MotionEvent motionEvent, View view, Activity activity);

    void notifyActivityCreate();

    void notifyActivityDestroy();

    void notifyActivityPause();

    void notifyActivityResume();

    void notifyActivityStart();

    void notifyActivityStop();

    void notifyContentChange();

    void notifyWebView(View view, boolean z);

    void setWebView(View view, boolean z);
}
