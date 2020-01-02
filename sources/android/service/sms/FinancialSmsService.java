package android.service.sms;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.service.sms.IFinancialSmsService.Stub;
import com.android.internal.util.function.pooled.PooledLambda;

@SystemApi
public abstract class FinancialSmsService extends Service {
    public static final String ACTION_FINANCIAL_SERVICE_INTENT = "android.service.sms.action.FINANCIAL_SERVICE_INTENT";
    public static final String EXTRA_SMS_MSGS = "sms_messages";
    private static final String TAG = "FinancialSmsService";
    private final Handler mHandler = new Handler(Looper.getMainLooper(), null, true);
    private FinancialSmsServiceWrapper mWrapper;

    private final class FinancialSmsServiceWrapper extends Stub {
        private FinancialSmsServiceWrapper() {
        }

        public void getSmsMessages(RemoteCallback callback, Bundle params) throws RemoteException {
            FinancialSmsService.this.mHandler.sendMessage(PooledLambda.obtainMessage(-$$Lambda$FinancialSmsService$FinancialSmsServiceWrapper$XFtzKfY0m01I8W-d6pG7NlmdfiQ.INSTANCE, FinancialSmsService.this, callback, params));
        }
    }

    @SystemApi
    public abstract CursorWindow onGetSmsMessages(Bundle bundle);

    private void getSmsMessages(RemoteCallback callback, Bundle params) {
        Bundle data = new Bundle();
        CursorWindow smsMessages = onGetSmsMessages(params);
        if (smsMessages != null) {
            data.putParcelable(EXTRA_SMS_MSGS, smsMessages);
        }
        callback.sendResult(data);
    }

    public void onCreate() {
        super.onCreate();
        this.mWrapper = new FinancialSmsServiceWrapper();
    }

    public IBinder onBind(Intent intent) {
        return this.mWrapper;
    }
}
