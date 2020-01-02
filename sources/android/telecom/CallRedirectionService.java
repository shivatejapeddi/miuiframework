package android.telecom;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.android.internal.os.SomeArgs;
import com.android.internal.telecom.ICallRedirectionAdapter;
import com.android.internal.telecom.ICallRedirectionService.Stub;

public abstract class CallRedirectionService extends Service {
    private static final int MSG_PLACE_CALL = 1;
    public static final String SERVICE_INTERFACE = "android.telecom.CallRedirectionService";
    private ICallRedirectionAdapter mCallRedirectionAdapter;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                SomeArgs args = msg.obj;
                try {
                    CallRedirectionService.this.mCallRedirectionAdapter = (ICallRedirectionAdapter) args.arg1;
                    CallRedirectionService.this.onPlaceCall((Uri) args.arg2, (PhoneAccountHandle) args.arg3, ((Boolean) args.arg4).booleanValue());
                } finally {
                    args.recycle();
                }
            }
        }
    };

    private final class CallRedirectionBinder extends Stub {
        private CallRedirectionBinder() {
        }

        /* synthetic */ CallRedirectionBinder(CallRedirectionService x0, AnonymousClass1 x1) {
            this();
        }

        public void placeCall(ICallRedirectionAdapter adapter, Uri handle, PhoneAccountHandle initialPhoneAccount, boolean allowInteractiveResponse) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = adapter;
            args.arg2 = handle;
            args.arg3 = initialPhoneAccount;
            args.arg4 = Boolean.valueOf(allowInteractiveResponse);
            CallRedirectionService.this.mHandler.obtainMessage(1, args).sendToTarget();
        }
    }

    public abstract void onPlaceCall(Uri uri, PhoneAccountHandle phoneAccountHandle, boolean z);

    public final void placeCallUnmodified() {
        try {
            this.mCallRedirectionAdapter.placeCallUnmodified();
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
    }

    public final void redirectCall(Uri gatewayUri, PhoneAccountHandle targetPhoneAccount, boolean confirmFirst) {
        try {
            this.mCallRedirectionAdapter.redirectCall(gatewayUri, targetPhoneAccount, confirmFirst);
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
    }

    public final void cancelCall() {
        try {
            this.mCallRedirectionAdapter.cancelCall();
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
    }

    public final IBinder onBind(Intent intent) {
        return new CallRedirectionBinder(this, null);
    }

    public final boolean onUnbind(Intent intent) {
        return false;
    }
}
