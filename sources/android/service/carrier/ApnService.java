package android.service.carrier;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.android.internal.telephony.IApnSourceService.Stub;
import java.util.List;

@SystemApi
public abstract class ApnService extends Service {
    private static final String LOG_TAG = "ApnService";
    private final Stub mBinder = new Stub() {
        public ContentValues[] getApns(int subId) {
            try {
                List<ContentValues> apns = ApnService.this.onRestoreApns(subId);
                return (ContentValues[]) apns.toArray(new ContentValues[apns.size()]);
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error in getApns for subId=");
                stringBuilder.append(subId);
                stringBuilder.append(": ");
                stringBuilder.append(e.getMessage());
                Log.e(ApnService.LOG_TAG, stringBuilder.toString(), e);
                return null;
            }
        }
    };

    public abstract List<ContentValues> onRestoreApns(int i);

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }
}
