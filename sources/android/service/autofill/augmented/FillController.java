package android.service.autofill.augmented;

import android.annotation.SystemApi;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import com.android.internal.util.Preconditions;
import java.util.List;

@SystemApi
public final class FillController {
    private static final String TAG = FillController.class.getSimpleName();
    private final AutofillProxy mProxy;

    FillController(AutofillProxy proxy) {
        this.mProxy = proxy;
    }

    public void autofill(List<Pair<AutofillId, AutofillValue>> values) {
        Preconditions.checkNotNull(values);
        if (AugmentedAutofillService.sDebug) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("autofill() with ");
            stringBuilder.append(values.size());
            stringBuilder.append(" values");
            Log.d(str, stringBuilder.toString());
        }
        try {
            this.mProxy.autofill(values);
            FillWindow fillWindow = this.mProxy.getFillWindow();
            if (fillWindow != null) {
                fillWindow.destroy();
            }
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
    }
}
