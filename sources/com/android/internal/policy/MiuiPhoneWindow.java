package com.android.internal.policy;

import android.content.Context;
import android.view.KeyEvent;
import android.view.ViewRootImpl.ActivityConfigCallback;
import android.view.Window;

public class MiuiPhoneWindow extends PhoneWindow {
    private int mMenuDownCount;

    public MiuiPhoneWindow(Context context) {
        super(context);
    }

    public MiuiPhoneWindow(Context context, Window preservedWindow, ActivityConfigCallback activityConfigCallback) {
        super(context, preservedWindow, activityConfigCallback);
    }

    /* Access modifiers changed, original: protected */
    public boolean onKeyDown(int featureId, int keyCode, KeyEvent event) {
        KeyEvent event2;
        int keyCode2 = keyCode;
        if (keyCode2 == 187) {
            KeyEvent keyEvent = new KeyEvent(event.getDownTime(), event.getEventTime(), event.getAction(), 82, event.getRepeatCount(), event.getMetaState(), event.getDeviceId(), event.getScanCode(), event.getFlags());
            keyCode2 = keyEvent.getKeyCode();
        } else {
            event2 = event;
        }
        if (keyCode2 == 82) {
            if (this.mMenuDownCount == 0) {
                event2 = new KeyEvent(0, 82);
            }
            this.mMenuDownCount++;
        }
        return super.onKeyDown(featureId, keyCode2, event2);
    }

    /* Access modifiers changed, original: protected */
    public boolean onKeyUp(int featureId, int keyCode, KeyEvent event) {
        KeyEvent event2;
        int keyCode2 = keyCode;
        if (keyCode2 == 187) {
            KeyEvent keyEvent = new KeyEvent(event.getDownTime(), event.getEventTime(), event.getAction(), 82, event.getRepeatCount(), event.getMetaState(), event.getDeviceId(), event.getScanCode(), event.getFlags());
            keyCode2 = keyEvent.getKeyCode();
        } else {
            event2 = event;
        }
        if (keyCode2 == 82) {
            if (this.mMenuDownCount == 1) {
                event2 = new KeyEvent(1, 82);
            }
            this.mMenuDownCount = 0;
        }
        return super.onKeyUp(featureId, keyCode2, event2);
    }
}
