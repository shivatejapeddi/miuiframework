package android.telecom;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.android.internal.telecom.IPhoneAccountSuggestionCallback;
import com.android.internal.telecom.IPhoneAccountSuggestionService;
import com.android.internal.telecom.IPhoneAccountSuggestionService.Stub;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SystemApi
public class PhoneAccountSuggestionService extends Service {
    public static final String SERVICE_INTERFACE = "android.telecom.PhoneAccountSuggestionService";
    private final Map<String, IPhoneAccountSuggestionCallback> mCallbackMap = new HashMap();
    private IPhoneAccountSuggestionService mInterface = new Stub() {
        public void onAccountSuggestionRequest(IPhoneAccountSuggestionCallback callback, String number) {
            PhoneAccountSuggestionService.this.mCallbackMap.put(number, callback);
            PhoneAccountSuggestionService.this.onAccountSuggestionRequest(number);
        }
    };

    public IBinder onBind(Intent intent) {
        return this.mInterface.asBinder();
    }

    public void onAccountSuggestionRequest(String number) {
    }

    public final void suggestPhoneAccounts(String number, List<PhoneAccountSuggestion> suggestions) {
        IPhoneAccountSuggestionCallback callback = (IPhoneAccountSuggestionCallback) this.mCallbackMap.remove(number);
        if (callback == null) {
            Log.w((Object) this, "No suggestions requested for the number %s", Log.pii(number));
            return;
        }
        try {
            callback.suggestPhoneAccounts(number, suggestions);
        } catch (RemoteException e) {
            Log.w((Object) this, "Remote exception calling suggestPhoneAccounts", new Object[0]);
        }
    }
}
