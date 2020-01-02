package com.android.internal.policy;

import android.annotation.UnsupportedAppUsage;
import android.app.KeyguardManager;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.session.MediaSessionManager;
import android.os.Parcelable;
import android.os.UserHandle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.FallbackEventHandler;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.View;

public class PhoneFallbackEventHandler implements FallbackEventHandler {
    private static final boolean DEBUG = false;
    private static String TAG = "PhoneFallbackEventHandler";
    AudioManager mAudioManager;
    @UnsupportedAppUsage
    Context mContext;
    KeyguardManager mKeyguardManager;
    MediaSessionManager mMediaSessionManager;
    SearchManager mSearchManager;
    TelephonyManager mTelephonyManager;
    @UnsupportedAppUsage
    View mView;

    @UnsupportedAppUsage
    public PhoneFallbackEventHandler(Context context) {
        this.mContext = context;
    }

    public void setView(View v) {
        this.mView = v;
    }

    public void preDispatchKeyEvent(KeyEvent event) {
        getAudioManager().preDispatchKeyEvent(event, Integer.MIN_VALUE);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        if (action == 0) {
            return onKeyDown(keyCode, event);
        }
        return onKeyUp(keyCode, event);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int i = keyCode;
        Parcelable parcelable = event;
        DispatcherState dispatcher = this.mView.getKeyDispatcherState();
        if (i != 5) {
            if (i != 27) {
                if (!(i == 79 || i == 130)) {
                    if (i != 164) {
                        if (i != 222) {
                            if (!(i == 24 || i == 25)) {
                                if (!(i == 126 || i == 127)) {
                                    switch (i) {
                                        case 84:
                                            if (!isNotInstantAppAndKeyguardRestricted(dispatcher)) {
                                                if (event.getRepeatCount() != 0) {
                                                    if (event.isLongPress() && dispatcher.isTracking(parcelable)) {
                                                        Configuration config = this.mContext.getResources().getConfiguration();
                                                        if (config.keyboard == 1 || config.hardKeyboardHidden == 2) {
                                                            if (!isUserSetupComplete()) {
                                                                Log.i(TAG, "Not dispatching SEARCH long press because user setup is in progress.");
                                                                break;
                                                            }
                                                            Intent intent = new Intent(Intent.ACTION_SEARCH_LONG_PRESS);
                                                            intent.setFlags(268435456);
                                                            try {
                                                                this.mView.performHapticFeedback(0);
                                                                sendCloseSystemWindows();
                                                                getSearchManager().stopSearch();
                                                                this.mContext.startActivity(intent);
                                                                dispatcher.performedLongPress(parcelable);
                                                                return true;
                                                            } catch (ActivityNotFoundException e) {
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                dispatcher.startTracking(parcelable, this);
                                                break;
                                            }
                                            break;
                                        case 85:
                                            break;
                                        case 86:
                                        case 87:
                                        case 88:
                                        case 89:
                                        case 90:
                                        case 91:
                                            break;
                                    }
                                }
                                if (getTelephonyManager().getCallState() != 0) {
                                    return true;
                                }
                            }
                        }
                    }
                    handleVolumeKeyEvent(parcelable);
                    return true;
                }
                handleMediaKeyEvent(parcelable);
                return true;
            } else if (!isNotInstantAppAndKeyguardRestricted(dispatcher)) {
                if (event.getRepeatCount() == 0) {
                    dispatcher.startTracking(parcelable, this);
                } else if (event.isLongPress() && dispatcher.isTracking(parcelable)) {
                    dispatcher.performedLongPress(parcelable);
                    if (isUserSetupComplete()) {
                        this.mView.performHapticFeedback(0);
                        sendCloseSystemWindows();
                        Intent intent2 = new Intent(Intent.ACTION_CAMERA_BUTTON, null);
                        intent2.addFlags(268435456);
                        intent2.putExtra(Intent.EXTRA_KEY_EVENT, parcelable);
                        this.mContext.sendOrderedBroadcastAsUser(intent2, UserHandle.CURRENT_OR_SELF, null, null, null, 0, null, null);
                    } else {
                        Log.i(TAG, "Not dispatching CAMERA long press because user setup is in progress.");
                    }
                }
                return true;
            }
        } else if (!isNotInstantAppAndKeyguardRestricted(dispatcher)) {
            if (event.getRepeatCount() == 0) {
                dispatcher.startTracking(parcelable, this);
            } else if (event.isLongPress() && dispatcher.isTracking(parcelable)) {
                dispatcher.performedLongPress(parcelable);
                if (isUserSetupComplete()) {
                    this.mView.performHapticFeedback(0);
                    Intent intent3 = new Intent(Intent.ACTION_VOICE_COMMAND);
                    intent3.setFlags(268435456);
                    try {
                        sendCloseSystemWindows();
                        this.mContext.startActivity(intent3);
                    } catch (ActivityNotFoundException e2) {
                        startCallActivity();
                    }
                } else {
                    Log.i(TAG, "Not starting call activity because user setup is in progress.");
                }
            }
            return true;
        }
        return false;
    }

    private boolean isNotInstantAppAndKeyguardRestricted(DispatcherState dispatcher) {
        return !this.mContext.getPackageManager().isInstantApp() && (getKeyguardManager().inKeyguardRestrictedInputMode() || dispatcher == null);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        DispatcherState dispatcher = this.mView.getKeyDispatcherState();
        if (dispatcher != null) {
            dispatcher.handleUpEvent(event);
        }
        if (keyCode != 5) {
            if (keyCode != 27) {
                if (!(keyCode == 79 || keyCode == 130)) {
                    if (keyCode != 164) {
                        if (keyCode != 222) {
                            if (!(keyCode == 24 || keyCode == 25)) {
                                if (!(keyCode == 126 || keyCode == 127)) {
                                    switch (keyCode) {
                                        case 85:
                                        case 86:
                                        case 87:
                                        case 88:
                                        case 89:
                                        case 90:
                                        case 91:
                                            break;
                                    }
                                }
                            }
                        }
                    }
                    if (!event.isCanceled()) {
                        handleVolumeKeyEvent(event);
                    }
                    return true;
                }
                handleMediaKeyEvent(event);
                return true;
            } else if (!isNotInstantAppAndKeyguardRestricted(dispatcher)) {
                if (event.isTracking()) {
                    event.isCanceled();
                }
                return true;
            }
        } else if (!isNotInstantAppAndKeyguardRestricted(dispatcher)) {
            if (event.isTracking() && !event.isCanceled()) {
                if (isUserSetupComplete()) {
                    startCallActivity();
                } else {
                    Log.i(TAG, "Not starting call activity because user setup is in progress.");
                }
            }
            return true;
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void startCallActivity() {
        sendCloseSystemWindows();
        Intent intent = new Intent(Intent.ACTION_CALL_BUTTON);
        intent.setFlags(268435456);
        try {
            this.mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.w(TAG, "No activity found for android.intent.action.CALL_BUTTON.");
        }
    }

    /* Access modifiers changed, original: 0000 */
    public SearchManager getSearchManager() {
        if (this.mSearchManager == null) {
            this.mSearchManager = (SearchManager) this.mContext.getSystemService("search");
        }
        return this.mSearchManager;
    }

    /* Access modifiers changed, original: 0000 */
    public TelephonyManager getTelephonyManager() {
        if (this.mTelephonyManager == null) {
            this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        }
        return this.mTelephonyManager;
    }

    /* Access modifiers changed, original: 0000 */
    public KeyguardManager getKeyguardManager() {
        if (this.mKeyguardManager == null) {
            this.mKeyguardManager = (KeyguardManager) this.mContext.getSystemService(Context.KEYGUARD_SERVICE);
        }
        return this.mKeyguardManager;
    }

    /* Access modifiers changed, original: 0000 */
    public AudioManager getAudioManager() {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        }
        return this.mAudioManager;
    }

    /* Access modifiers changed, original: 0000 */
    public MediaSessionManager getMediaSessionManager() {
        if (this.mMediaSessionManager == null) {
            this.mMediaSessionManager = (MediaSessionManager) this.mContext.getSystemService(Context.MEDIA_SESSION_SERVICE);
        }
        return this.mMediaSessionManager;
    }

    /* Access modifiers changed, original: 0000 */
    public void sendCloseSystemWindows() {
        PhoneWindow.sendCloseSystemWindows(this.mContext, null);
    }

    private void handleVolumeKeyEvent(KeyEvent keyEvent) {
        getMediaSessionManager().dispatchVolumeKeyEventAsSystemService(keyEvent, Integer.MIN_VALUE);
    }

    private void handleMediaKeyEvent(KeyEvent keyEvent) {
        getMediaSessionManager().dispatchMediaKeyEventAsSystemService(keyEvent);
    }

    private boolean isUserSetupComplete() {
        return Secure.getInt(this.mContext.getContentResolver(), Secure.USER_SETUP_COMPLETE, 0) != 0;
    }
}
