package com.android.internal.app;

import android.annotation.UnsupportedAppUsage;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.app.AlertController.AlertParams;

public abstract class AlertActivity extends Activity implements DialogInterface {
    @UnsupportedAppUsage
    protected AlertController mAlert;
    @UnsupportedAppUsage
    protected AlertParams mAlertParams;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAlert = AlertController.create(this, this, getWindow());
        this.mAlertParams = new AlertParams(this);
    }

    public void cancel() {
        finish();
    }

    public void dismiss() {
        if (!isFinishing()) {
            finish();
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return dispatchPopulateAccessibilityEvent(this, event);
    }

    public static boolean dispatchPopulateAccessibilityEvent(Activity act, AccessibilityEvent event) {
        event.setClassName(Dialog.class.getName());
        event.setPackageName(act.getPackageName());
        LayoutParams params = act.getWindow().getAttributes();
        boolean isFullScreen = params.width == -1 && params.height == -1;
        event.setFullScreen(isFullScreen);
        return false;
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void setupAlert() {
        this.mAlert.installContent(this.mAlertParams);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mAlert.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mAlert.onKeyUp(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
