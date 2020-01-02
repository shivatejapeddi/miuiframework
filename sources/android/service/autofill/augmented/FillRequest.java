package android.service.autofill.augmented;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;

@SystemApi
public final class FillRequest {
    final AutofillProxy mProxy;

    FillRequest(AutofillProxy proxy) {
        this.mProxy = proxy;
    }

    public int getTaskId() {
        return this.mProxy.taskId;
    }

    public ComponentName getActivityComponent() {
        return this.mProxy.componentName;
    }

    public AutofillId getFocusedId() {
        return this.mProxy.getFocusedId();
    }

    public AutofillValue getFocusedValue() {
        return this.mProxy.getFocusedValue();
    }

    public PresentationParams getPresentationParams() {
        return this.mProxy.getSmartSuggestionParams();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FillRequest[act=");
        stringBuilder.append(getActivityComponent().flattenToShortString());
        stringBuilder.append(", id=");
        stringBuilder.append(this.mProxy.getFocusedId());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
