package android.view;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.MiuiSettings.ScreenEffect;
import android.provider.Settings.System;
import android.util.Slog;

public class ScreenOptimizeObserver extends ContentObserver {
    private static final String TAG = "ScreenOptimizeObserver";
    private Callback mCallback;
    private Context mContext;
    private ThreadedRenderer mThreadedRenderer;

    public interface Callback {
        void screenOptimizeSettingsChanged(ThreadedRenderer threadedRenderer, boolean z);
    }

    public ScreenOptimizeObserver(Context ctx, Handler handler, Callback cb) {
        super(handler);
        this.mContext = ctx;
        this.mCallback = cb;
        Context context = this.mContext;
        boolean z = false;
        this.mThreadedRenderer = ThreadedRenderer.create(context, false, context.getPackageName());
        Callback callback = this.mCallback;
        ThreadedRenderer threadedRenderer = this.mThreadedRenderer;
        if (getScreenMode() != 3) {
            z = true;
        }
        callback.screenOptimizeSettingsChanged(threadedRenderer, z);
        register();
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public Callback getCallback() {
        return this.mCallback;
    }

    public void register() {
        Slog.d(TAG, "register");
        this.mContext.getContentResolver().registerContentObserver(System.getUriFor(ScreenEffect.SCREEN_OPTIMIZE_MODE), false, this);
    }

    public void unregister() {
        Slog.d(TAG, "unregister");
        this.mContext.getContentResolver().unregisterContentObserver(this);
    }

    public void onChange(boolean selfChange) {
        Callback cb = getCallback();
        if (cb != null) {
            cb.screenOptimizeSettingsChanged(this.mThreadedRenderer, getScreenMode() != 3);
        }
    }

    public int getScreenMode() {
        int defMode;
        if ((ScreenEffect.SCREEN_EFFECT_SUPPORTED & 1) != 0) {
            defMode = 1;
        } else {
            defMode = 2;
        }
        return System.getInt(this.mContext.getContentResolver(), ScreenEffect.SCREEN_OPTIMIZE_MODE, defMode);
    }
}
