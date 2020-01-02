package android.hardware.display;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Secure;
import java.time.LocalTime;

public class NightDisplayListener {
    private Callback mCallback;
    private final ContentObserver mContentObserver;
    private final Context mContext;
    private final Handler mHandler;
    private final ColorDisplayManager mManager;
    private final int mUserId;

    public interface Callback {
        void onActivated(boolean activated) {
        }

        void onAutoModeChanged(int autoMode) {
        }

        void onCustomStartTimeChanged(LocalTime startTime) {
        }

        void onCustomEndTimeChanged(LocalTime endTime) {
        }

        void onColorTemperatureChanged(int colorTemperature) {
        }
    }

    public NightDisplayListener(Context context) {
        this(context, ActivityManager.getCurrentUser(), new Handler(Looper.getMainLooper()));
    }

    public NightDisplayListener(Context context, Handler handler) {
        this(context, ActivityManager.getCurrentUser(), handler);
    }

    public NightDisplayListener(Context context, int userId, Handler handler) {
        this.mContext = context.getApplicationContext();
        this.mManager = (ColorDisplayManager) this.mContext.getSystemService(ColorDisplayManager.class);
        this.mUserId = userId;
        this.mHandler = handler;
        this.mContentObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                String setting = uri == null ? null : uri.getLastPathSegment();
                if (setting != null && NightDisplayListener.this.mCallback != null) {
                    Object obj = -1;
                    switch (setting.hashCode()) {
                        case -2038150513:
                            if (setting.equals(Secure.NIGHT_DISPLAY_AUTO_MODE)) {
                                obj = 1;
                                break;
                            }
                            break;
                        case -1761668069:
                            if (setting.equals(Secure.NIGHT_DISPLAY_CUSTOM_END_TIME)) {
                                obj = 3;
                                break;
                            }
                            break;
                        case -969458956:
                            if (setting.equals(Secure.NIGHT_DISPLAY_COLOR_TEMPERATURE)) {
                                obj = 4;
                                break;
                            }
                            break;
                        case 800115245:
                            if (setting.equals(Secure.NIGHT_DISPLAY_ACTIVATED)) {
                                obj = null;
                                break;
                            }
                            break;
                        case 1578271348:
                            if (setting.equals(Secure.NIGHT_DISPLAY_CUSTOM_START_TIME)) {
                                obj = 2;
                                break;
                            }
                            break;
                    }
                    if (obj == null) {
                        NightDisplayListener.this.mCallback.onActivated(NightDisplayListener.this.mManager.isNightDisplayActivated());
                    } else if (obj == 1) {
                        NightDisplayListener.this.mCallback.onAutoModeChanged(NightDisplayListener.this.mManager.getNightDisplayAutoMode());
                    } else if (obj == 2) {
                        NightDisplayListener.this.mCallback.onCustomStartTimeChanged(NightDisplayListener.this.mManager.getNightDisplayCustomStartTime());
                    } else if (obj == 3) {
                        NightDisplayListener.this.mCallback.onCustomEndTimeChanged(NightDisplayListener.this.mManager.getNightDisplayCustomEndTime());
                    } else if (obj == 4) {
                        NightDisplayListener.this.mCallback.onColorTemperatureChanged(NightDisplayListener.this.mManager.getNightDisplayColorTemperature());
                    }
                }
            }
        };
    }

    public void setCallback(Callback callback) {
        if (Looper.myLooper() != this.mHandler.getLooper()) {
            this.mHandler.post(new -$$Lambda$NightDisplayListener$sOK1HmSbMnFLzc4SdDD1WpVWJiI(this, callback));
        }
        setCallbackInternal(callback);
    }

    public /* synthetic */ void lambda$setCallback$0$NightDisplayListener(Callback callback) {
        setCallbackInternal(callback);
    }

    private void setCallbackInternal(Callback newCallback) {
        Callback oldCallback = this.mCallback;
        if (oldCallback != newCallback) {
            this.mCallback = newCallback;
            if (this.mCallback == null) {
                this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
            } else if (oldCallback == null) {
                ContentResolver cr = this.mContext.getContentResolver();
                cr.registerContentObserver(Secure.getUriFor(Secure.NIGHT_DISPLAY_ACTIVATED), false, this.mContentObserver, this.mUserId);
                cr.registerContentObserver(Secure.getUriFor(Secure.NIGHT_DISPLAY_AUTO_MODE), false, this.mContentObserver, this.mUserId);
                cr.registerContentObserver(Secure.getUriFor(Secure.NIGHT_DISPLAY_CUSTOM_START_TIME), false, this.mContentObserver, this.mUserId);
                cr.registerContentObserver(Secure.getUriFor(Secure.NIGHT_DISPLAY_CUSTOM_END_TIME), false, this.mContentObserver, this.mUserId);
                cr.registerContentObserver(Secure.getUriFor(Secure.NIGHT_DISPLAY_COLOR_TEMPERATURE), false, this.mContentObserver, this.mUserId);
            }
        }
    }
}
