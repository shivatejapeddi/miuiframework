package android.view;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.graphics.Region;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.SeempLog;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager.KeyboardShortcutsReceiver;
import com.android.internal.os.IResultReceiver.Stub;

public final class WindowManagerImpl implements WindowManager {
    private final Context mContext;
    private IBinder mDefaultToken;
    @UnsupportedAppUsage
    private final WindowManagerGlobal mGlobal;
    private final Window mParentWindow;

    public WindowManagerImpl(Context context) {
        this(context, null);
    }

    private WindowManagerImpl(Context context, Window parentWindow) {
        this.mGlobal = WindowManagerGlobal.getInstance();
        this.mContext = context;
        this.mParentWindow = parentWindow;
    }

    public WindowManagerImpl createLocalWindowManager(Window parentWindow) {
        return new WindowManagerImpl(this.mContext, parentWindow);
    }

    public WindowManagerImpl createPresentationWindowManager(Context displayContext) {
        return new WindowManagerImpl(displayContext, this.mParentWindow);
    }

    public void setDefaultToken(IBinder token) {
        this.mDefaultToken = token;
    }

    public void addView(View view, LayoutParams params) {
        SeempLog.record_vg_layout(383, params);
        applyDefaultToken(params);
        this.mGlobal.addView(view, params, this.mContext.getDisplay(), this.mParentWindow);
    }

    public void updateViewLayout(View view, LayoutParams params) {
        SeempLog.record_vg_layout(384, params);
        applyDefaultToken(params);
        this.mGlobal.updateViewLayout(view, params);
    }

    private void applyDefaultToken(LayoutParams params) {
        if (this.mDefaultToken != null && this.mParentWindow == null) {
            if (params instanceof WindowManager.LayoutParams) {
                WindowManager.LayoutParams wparams = (WindowManager.LayoutParams) params;
                if (wparams.token == null) {
                    wparams.token = this.mDefaultToken;
                    return;
                }
                return;
            }
            throw new IllegalArgumentException("Params must be WindowManager.LayoutParams");
        }
    }

    public void removeView(View view) {
        this.mGlobal.removeView(view, false);
    }

    public void removeViewImmediate(View view) {
        this.mGlobal.removeView(view, true);
    }

    public void requestAppKeyboardShortcuts(final KeyboardShortcutsReceiver receiver, int deviceId) {
        try {
            WindowManagerGlobal.getWindowManagerService().requestAppKeyboardShortcuts(new Stub() {
                public void send(int resultCode, Bundle resultData) throws RemoteException {
                    receiver.onKeyboardShortcutsReceived(resultData.getParcelableArrayList(WindowManager.PARCEL_KEY_SHORTCUTS_ARRAY));
                }
            }, deviceId);
        } catch (RemoteException e) {
        }
    }

    public Display getDefaultDisplay() {
        return this.mContext.getDisplay();
    }

    public Region getCurrentImeTouchRegion() {
        try {
            return WindowManagerGlobal.getWindowManagerService().getCurrentImeTouchRegion();
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setShouldShowWithInsecureKeyguard(int displayId, boolean shouldShow) {
        try {
            WindowManagerGlobal.getWindowManagerService().setShouldShowWithInsecureKeyguard(displayId, shouldShow);
        } catch (RemoteException e) {
        }
    }

    public void setShouldShowSystemDecors(int displayId, boolean shouldShow) {
        try {
            WindowManagerGlobal.getWindowManagerService().setShouldShowSystemDecors(displayId, shouldShow);
        } catch (RemoteException e) {
        }
    }

    public boolean shouldShowSystemDecors(int displayId) {
        try {
            return WindowManagerGlobal.getWindowManagerService().shouldShowSystemDecors(displayId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setShouldShowIme(int displayId, boolean shouldShow) {
        try {
            WindowManagerGlobal.getWindowManagerService().setShouldShowIme(displayId, shouldShow);
        } catch (RemoteException e) {
        }
    }

    public boolean shouldShowIme(int displayId) {
        try {
            return WindowManagerGlobal.getWindowManagerService().shouldShowIme(displayId);
        } catch (RemoteException e) {
            return false;
        }
    }
}
