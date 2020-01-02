package android.service.autofill.augmented;

import android.annotation.SystemApi;
import android.util.Log;

@SystemApi
public final class FillCallback {
    private static final String TAG = FillCallback.class.getSimpleName();
    private final AutofillProxy mProxy;

    FillCallback(AutofillProxy proxy) {
        this.mProxy = proxy;
    }

    public void onSuccess(FillResponse response) {
        if (AugmentedAutofillService.sDebug) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onSuccess(): ");
            stringBuilder.append(response);
            Log.d(str, stringBuilder.toString());
        }
        if (response == null) {
            this.mProxy.report(1);
            return;
        }
        FillWindow fillWindow = response.getFillWindow();
        if (fillWindow != null) {
            fillWindow.show();
        }
    }
}
