package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Slog;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;

public final class VisibilitySetterAction extends InternalOnClickAction implements OnClickAction, Parcelable {
    public static final Creator<VisibilitySetterAction> CREATOR = new Creator<VisibilitySetterAction>() {
        public VisibilitySetterAction createFromParcel(Parcel parcel) {
            SparseIntArray visibilities = parcel.readSparseIntArray();
            Builder builder = null;
            for (int i = 0; i < visibilities.size(); i++) {
                int id = visibilities.keyAt(i);
                int visibility = visibilities.valueAt(i);
                if (builder == null) {
                    builder = new Builder(id, visibility);
                } else {
                    builder.setVisibility(id, visibility);
                }
            }
            return builder == null ? null : builder.build();
        }

        public VisibilitySetterAction[] newArray(int size) {
            return new VisibilitySetterAction[size];
        }
    };
    private static final String TAG = "VisibilitySetterAction";
    private final SparseIntArray mVisibilities;

    public static final class Builder {
        private boolean mDestroyed;
        private final SparseIntArray mVisibilities = new SparseIntArray();

        public Builder(int id, int visibility) {
            setVisibility(id, visibility);
        }

        public Builder setVisibility(int id, int visibility) {
            throwIfDestroyed();
            if (visibility == 0 || visibility == 4 || visibility == 8) {
                this.mVisibilities.put(id, visibility);
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid visibility: ");
            stringBuilder.append(visibility);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public VisibilitySetterAction build() {
            throwIfDestroyed();
            this.mDestroyed = true;
            return new VisibilitySetterAction(this, null);
        }

        private void throwIfDestroyed() {
            Preconditions.checkState(this.mDestroyed ^ 1, "Already called build()");
        }
    }

    /* synthetic */ VisibilitySetterAction(Builder x0, AnonymousClass1 x1) {
        this(x0);
    }

    private VisibilitySetterAction(Builder builder) {
        this.mVisibilities = builder.mVisibilities;
    }

    public void onClick(ViewGroup rootView) {
        for (int i = 0; i < this.mVisibilities.size(); i++) {
            int id = this.mVisibilities.keyAt(i);
            View child = rootView.findViewById(id);
            String str = TAG;
            if (child == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Skipping view id ");
                stringBuilder.append(id);
                stringBuilder.append(" because it's not found on ");
                stringBuilder.append(rootView);
                Slog.w(str, stringBuilder.toString());
            } else {
                int visibility = this.mVisibilities.valueAt(i);
                if (Helper.sVerbose) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Changing visibility of view ");
                    stringBuilder2.append(child);
                    stringBuilder2.append(" from ");
                    stringBuilder2.append(child.getVisibility());
                    stringBuilder2.append(" to  ");
                    stringBuilder2.append(visibility);
                    Slog.v(str, stringBuilder2.toString());
                }
                child.setVisibility(visibility);
            }
        }
    }

    public String toString() {
        if (!Helper.sDebug) {
            return super.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("VisibilitySetterAction: [");
        stringBuilder.append(this.mVisibilities);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeSparseIntArray(this.mVisibilities);
    }
}
