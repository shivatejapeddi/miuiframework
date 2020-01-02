package android.app;

import android.annotation.SystemApi;
import android.app.IInstantAppResolver.Stub;
import android.content.Context;
import android.content.Intent;
import android.content.pm.InstantAppResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.util.Slog;
import com.android.internal.os.SomeArgs;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SystemApi
public abstract class InstantAppResolverService extends Service {
    private static final boolean DEBUG_INSTANT = Build.IS_DEBUGGABLE;
    public static final String EXTRA_RESOLVE_INFO = "android.app.extra.RESOLVE_INFO";
    public static final String EXTRA_SEQUENCE = "android.app.extra.SEQUENCE";
    private static final String TAG = "PackageManager";
    Handler mHandler;

    public static final class InstantAppResolutionCallback {
        private final IRemoteCallback mCallback;
        private final int mSequence;

        InstantAppResolutionCallback(int sequence, IRemoteCallback callback) {
            this.mCallback = callback;
            this.mSequence = sequence;
        }

        public void onInstantAppResolveInfo(List<InstantAppResolveInfo> resolveInfo) {
            Bundle data = new Bundle();
            data.putParcelableList(InstantAppResolverService.EXTRA_RESOLVE_INFO, resolveInfo);
            data.putInt(InstantAppResolverService.EXTRA_SEQUENCE, this.mSequence);
            try {
                this.mCallback.sendResult(data);
            } catch (RemoteException e) {
            }
        }
    }

    private final class ServiceHandler extends Handler {
        public static final int MSG_GET_INSTANT_APP_INTENT_FILTER = 2;
        public static final int MSG_GET_INSTANT_APP_RESOLVE_INFO = 1;

        public ServiceHandler(Looper looper) {
            super(looper, null, true);
        }

        public void handleMessage(Message message) {
            Message message2 = message;
            int action = message2.what;
            String str = ", userId: ";
            String str2 = "[";
            String str3 = InstantAppResolverService.TAG;
            SomeArgs args;
            IRemoteCallback callback;
            int[] digestPrefix;
            int userId;
            String token;
            Intent intent;
            StringBuilder stringBuilder;
            if (action == 1) {
                args = (SomeArgs) message2.obj;
                callback = (IRemoteCallback) args.arg1;
                digestPrefix = (int[]) args.arg2;
                userId = ((Integer) args.arg3).intValue();
                token = (String) args.arg4;
                intent = (Intent) args.arg5;
                int sequence = message2.arg1;
                if (InstantAppResolverService.DEBUG_INSTANT) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str2);
                    stringBuilder.append(token);
                    stringBuilder.append("] Phase1 request; prefix: ");
                    stringBuilder.append(Arrays.toString(digestPrefix));
                    stringBuilder.append(str);
                    stringBuilder.append(userId);
                    Slog.d(str3, stringBuilder.toString());
                }
                InstantAppResolverService.this.onGetInstantAppResolveInfo(intent, digestPrefix, UserHandle.of(userId), token, new InstantAppResolutionCallback(sequence, callback));
            } else if (action == 2) {
                args = message2.obj;
                callback = args.arg1;
                digestPrefix = args.arg2;
                userId = ((Integer) args.arg3).intValue();
                token = args.arg4;
                intent = args.arg5;
                if (InstantAppResolverService.DEBUG_INSTANT) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str2);
                    stringBuilder.append(token);
                    stringBuilder.append("] Phase2 request; prefix: ");
                    stringBuilder.append(Arrays.toString(digestPrefix));
                    stringBuilder.append(str);
                    stringBuilder.append(userId);
                    Slog.d(str3, stringBuilder.toString());
                }
                InstantAppResolverService.this.onGetInstantAppIntentFilter(intent, digestPrefix, UserHandle.of(userId), token, new InstantAppResolutionCallback(-1, callback));
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Unknown message: ");
                stringBuilder2.append(action);
                throw new IllegalArgumentException(stringBuilder2.toString());
            }
        }
    }

    @Deprecated
    public void onGetInstantAppResolveInfo(int[] digestPrefix, String token, InstantAppResolutionCallback callback) {
        throw new IllegalStateException("Must define onGetInstantAppResolveInfo");
    }

    @Deprecated
    public void onGetInstantAppIntentFilter(int[] digestPrefix, String token, InstantAppResolutionCallback callback) {
        throw new IllegalStateException("Must define onGetInstantAppIntentFilter");
    }

    @Deprecated
    public void onGetInstantAppResolveInfo(Intent sanitizedIntent, int[] hostDigestPrefix, String token, InstantAppResolutionCallback callback) {
        if (sanitizedIntent.isWebIntent()) {
            onGetInstantAppResolveInfo(hostDigestPrefix, token, callback);
        } else {
            callback.onInstantAppResolveInfo(Collections.emptyList());
        }
    }

    @Deprecated
    public void onGetInstantAppIntentFilter(Intent sanitizedIntent, int[] hostDigestPrefix, String token, InstantAppResolutionCallback callback) {
        Log.e(TAG, "New onGetInstantAppIntentFilter is not overridden");
        if (sanitizedIntent.isWebIntent()) {
            onGetInstantAppIntentFilter(hostDigestPrefix, token, callback);
        } else {
            callback.onInstantAppResolveInfo(Collections.emptyList());
        }
    }

    public void onGetInstantAppResolveInfo(Intent sanitizedIntent, int[] hostDigestPrefix, UserHandle userHandle, String token, InstantAppResolutionCallback callback) {
        onGetInstantAppResolveInfo(sanitizedIntent, hostDigestPrefix, token, callback);
    }

    public void onGetInstantAppIntentFilter(Intent sanitizedIntent, int[] hostDigestPrefix, UserHandle userHandle, String token, InstantAppResolutionCallback callback) {
        onGetInstantAppIntentFilter(sanitizedIntent, hostDigestPrefix, token, callback);
    }

    /* Access modifiers changed, original: 0000 */
    public Looper getLooper() {
        return getBaseContext().getMainLooper();
    }

    public final void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mHandler = new ServiceHandler(getLooper());
    }

    public final IBinder onBind(Intent intent) {
        return new Stub() {
            public void getInstantAppResolveInfoList(Intent sanitizedIntent, int[] digestPrefix, int userId, String token, int sequence, IRemoteCallback callback) {
                if (InstantAppResolverService.DEBUG_INSTANT) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("[");
                    stringBuilder.append(token);
                    stringBuilder.append("] Phase1 called; posting");
                    Slog.v(InstantAppResolverService.TAG, stringBuilder.toString());
                }
                SomeArgs args = SomeArgs.obtain();
                args.arg1 = callback;
                args.arg2 = digestPrefix;
                args.arg3 = Integer.valueOf(userId);
                args.arg4 = token;
                args.arg5 = sanitizedIntent;
                InstantAppResolverService.this.mHandler.obtainMessage(1, sequence, 0, args).sendToTarget();
            }

            public void getInstantAppIntentFilterList(Intent sanitizedIntent, int[] digestPrefix, int userId, String token, IRemoteCallback callback) {
                if (InstantAppResolverService.DEBUG_INSTANT) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("[");
                    stringBuilder.append(token);
                    stringBuilder.append("] Phase2 called; posting");
                    Slog.v(InstantAppResolverService.TAG, stringBuilder.toString());
                }
                SomeArgs args = SomeArgs.obtain();
                args.arg1 = callback;
                args.arg2 = digestPrefix;
                args.arg3 = Integer.valueOf(userId);
                args.arg4 = token;
                args.arg5 = sanitizedIntent;
                InstantAppResolverService.this.mHandler.obtainMessage(2, args).sendToTarget();
            }
        };
    }
}
