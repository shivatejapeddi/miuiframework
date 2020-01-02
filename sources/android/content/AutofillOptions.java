package android.content;

import android.app.ActivityThread;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArraySet;
import android.util.Log;
import android.view.autofill.AutofillManager.AutofillClient;
import java.io.PrintWriter;

public final class AutofillOptions implements Parcelable {
    public static final Creator<AutofillOptions> CREATOR = new Creator<AutofillOptions>() {
        public AutofillOptions createFromParcel(Parcel parcel) {
            AutofillOptions options = new AutofillOptions(parcel.readInt(), parcel.readBoolean());
            options.augmentedAutofillEnabled = parcel.readBoolean();
            options.whitelistedActivitiesForAugmentedAutofill = parcel.readArraySet(null);
            return options;
        }

        public AutofillOptions[] newArray(int size) {
            return new AutofillOptions[size];
        }
    };
    private static final String TAG = AutofillOptions.class.getSimpleName();
    public boolean augmentedAutofillEnabled;
    public final boolean compatModeEnabled;
    public final int loggingLevel;
    public ArraySet<ComponentName> whitelistedActivitiesForAugmentedAutofill;

    public AutofillOptions(int loggingLevel, boolean compatModeEnabled) {
        this.loggingLevel = loggingLevel;
        this.compatModeEnabled = compatModeEnabled;
    }

    public boolean isAugmentedAutofillEnabled(Context context) {
        boolean z = false;
        if (!this.augmentedAutofillEnabled) {
            return false;
        }
        AutofillClient autofillClient = context.getAutofillClient();
        if (autofillClient == null) {
            return false;
        }
        ComponentName component = autofillClient.autofillClientGetComponentName();
        ArraySet arraySet = this.whitelistedActivitiesForAugmentedAutofill;
        if (arraySet == null || arraySet.contains(component)) {
            z = true;
        }
        return z;
    }

    public static AutofillOptions forWhitelistingItself() {
        ActivityThread at = ActivityThread.currentActivityThread();
        if (at != null) {
            String packageName = at.getApplication().getPackageName();
            if ("android.autofillservice.cts".equals(packageName)) {
                AutofillOptions options = new AutofillOptions(4, true);
                options.augmentedAutofillEnabled = true;
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("forWhitelistingItself(");
                stringBuilder.append(packageName);
                stringBuilder.append("): ");
                stringBuilder.append(options);
                Log.i(str, stringBuilder.toString());
                return options;
            }
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("forWhitelistingItself(): called by ");
            stringBuilder2.append(packageName);
            Log.e(str2, stringBuilder2.toString());
            throw new SecurityException("Thou shall not pass!");
        }
        throw new IllegalStateException("No ActivityThread");
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AutofillOptions [loggingLevel=");
        stringBuilder.append(this.loggingLevel);
        stringBuilder.append(", compatMode=");
        stringBuilder.append(this.compatModeEnabled);
        stringBuilder.append(", augmentedAutofillEnabled=");
        stringBuilder.append(this.augmentedAutofillEnabled);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public void dumpShort(PrintWriter pw) {
        pw.print("logLvl=");
        pw.print(this.loggingLevel);
        pw.print(", compatMode=");
        pw.print(this.compatModeEnabled);
        pw.print(", augmented=");
        pw.print(this.augmentedAutofillEnabled);
        if (this.whitelistedActivitiesForAugmentedAutofill != null) {
            pw.print(", whitelistedActivitiesForAugmentedAutofill=");
            pw.print(this.whitelistedActivitiesForAugmentedAutofill);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.loggingLevel);
        parcel.writeBoolean(this.compatModeEnabled);
        parcel.writeBoolean(this.augmentedAutofillEnabled);
        parcel.writeArraySet(this.whitelistedActivitiesForAugmentedAutofill);
    }
}
