package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Pair;
import android.util.SparseArray;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;

public final class CustomDescription implements Parcelable {
    public static final Creator<CustomDescription> CREATOR = new Creator<CustomDescription>() {
        public CustomDescription createFromParcel(Parcel parcel) {
            RemoteViews parentPresentation = (RemoteViews) parcel.readParcelable(null);
            if (parentPresentation == null) {
                return null;
            }
            int i;
            int i2;
            Builder builder = new Builder(parentPresentation);
            int[] transformationIds = parcel.createIntArray();
            if (transformationIds != null) {
                InternalTransformation[] values = (InternalTransformation[]) parcel.readParcelableArray(null, InternalTransformation.class);
                int size = transformationIds.length;
                for (i = 0; i < size; i++) {
                    builder.addChild(transformationIds[i], values[i]);
                }
            }
            InternalValidator[] conditions = (InternalValidator[]) parcel.readParcelableArray(null, InternalValidator.class);
            if (conditions != null) {
                BatchUpdates[] updates = (BatchUpdates[]) parcel.readParcelableArray(null, BatchUpdates.class);
                i = conditions.length;
                for (i2 = 0; i2 < i; i2++) {
                    builder.batchUpdate(conditions[i2], updates[i2]);
                }
            }
            int[] actionIds = parcel.createIntArray();
            if (actionIds != null) {
                InternalOnClickAction[] values2 = (InternalOnClickAction[]) parcel.readParcelableArray(null, InternalOnClickAction.class);
                i = actionIds.length;
                for (i2 = 0; i2 < i; i2++) {
                    builder.addOnClickAction(actionIds[i2], values2[i2]);
                }
            }
            return builder.build();
        }

        public CustomDescription[] newArray(int size) {
            return new CustomDescription[size];
        }
    };
    private final SparseArray<InternalOnClickAction> mActions;
    private final RemoteViews mPresentation;
    private final ArrayList<Pair<Integer, InternalTransformation>> mTransformations;
    private final ArrayList<Pair<InternalValidator, BatchUpdates>> mUpdates;

    public static class Builder {
        private SparseArray<InternalOnClickAction> mActions;
        private boolean mDestroyed;
        private final RemoteViews mPresentation;
        private ArrayList<Pair<Integer, InternalTransformation>> mTransformations;
        private ArrayList<Pair<InternalValidator, BatchUpdates>> mUpdates;

        public Builder(RemoteViews parentPresentation) {
            this.mPresentation = (RemoteViews) Preconditions.checkNotNull(parentPresentation);
        }

        public Builder addChild(int id, Transformation transformation) {
            throwIfDestroyed();
            boolean z = transformation instanceof InternalTransformation;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("not provided by Android System: ");
            stringBuilder.append(transformation);
            Preconditions.checkArgument(z, stringBuilder.toString());
            if (this.mTransformations == null) {
                this.mTransformations = new ArrayList();
            }
            this.mTransformations.add(new Pair(Integer.valueOf(id), (InternalTransformation) transformation));
            return this;
        }

        public Builder batchUpdate(Validator condition, BatchUpdates updates) {
            throwIfDestroyed();
            boolean z = condition instanceof InternalValidator;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("not provided by Android System: ");
            stringBuilder.append(condition);
            Preconditions.checkArgument(z, stringBuilder.toString());
            Preconditions.checkNotNull(updates);
            if (this.mUpdates == null) {
                this.mUpdates = new ArrayList();
            }
            this.mUpdates.add(new Pair((InternalValidator) condition, updates));
            return this;
        }

        public Builder addOnClickAction(int id, OnClickAction action) {
            throwIfDestroyed();
            boolean z = action instanceof InternalOnClickAction;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("not provided by Android System: ");
            stringBuilder.append(action);
            Preconditions.checkArgument(z, stringBuilder.toString());
            if (this.mActions == null) {
                this.mActions = new SparseArray();
            }
            this.mActions.put(id, (InternalOnClickAction) action);
            return this;
        }

        public CustomDescription build() {
            throwIfDestroyed();
            this.mDestroyed = true;
            return new CustomDescription(this, null);
        }

        private void throwIfDestroyed() {
            if (this.mDestroyed) {
                throw new IllegalStateException("Already called #build()");
            }
        }
    }

    /* synthetic */ CustomDescription(Builder x0, AnonymousClass1 x1) {
        this(x0);
    }

    private CustomDescription(Builder builder) {
        this.mPresentation = builder.mPresentation;
        this.mTransformations = builder.mTransformations;
        this.mUpdates = builder.mUpdates;
        this.mActions = builder.mActions;
    }

    public RemoteViews getPresentation() {
        return this.mPresentation;
    }

    public ArrayList<Pair<Integer, InternalTransformation>> getTransformations() {
        return this.mTransformations;
    }

    public ArrayList<Pair<InternalValidator, BatchUpdates>> getUpdates() {
        return this.mUpdates;
    }

    public SparseArray<InternalOnClickAction> getActions() {
        return this.mActions;
    }

    public String toString() {
        if (!Helper.sDebug) {
            return super.toString();
        }
        StringBuilder stringBuilder = new StringBuilder("CustomDescription: [presentation=");
        stringBuilder.append(this.mPresentation);
        stringBuilder.append(", transformations=");
        ArrayList arrayList = this.mTransformations;
        Object obj = "N/A";
        stringBuilder.append(arrayList == null ? obj : Integer.valueOf(arrayList.size()));
        stringBuilder.append(", updates=");
        arrayList = this.mUpdates;
        stringBuilder.append(arrayList == null ? obj : Integer.valueOf(arrayList.size()));
        stringBuilder.append(", actions=");
        SparseArray sparseArray = this.mActions;
        if (sparseArray != null) {
            obj = Integer.valueOf(sparseArray.size());
        }
        stringBuilder.append(obj);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mPresentation, flags);
        if (this.mPresentation != null) {
            int i;
            int size = this.mTransformations;
            if (size == 0) {
                dest.writeIntArray(null);
            } else {
                size = size.size();
                int[] ids = new int[size];
                InternalTransformation[] values = new InternalTransformation[size];
                for (i = 0; i < size; i++) {
                    Pair<Integer, InternalTransformation> pair = (Pair) this.mTransformations.get(i);
                    ids[i] = ((Integer) pair.first).intValue();
                    values[i] = (InternalTransformation) pair.second;
                }
                dest.writeIntArray(ids);
                dest.writeParcelableArray(values, flags);
            }
            ArrayList arrayList = this.mUpdates;
            if (arrayList == null) {
                dest.writeParcelableArray(null, flags);
            } else {
                size = arrayList.size();
                InternalValidator[] conditions = new InternalValidator[size];
                BatchUpdates[] updates = new BatchUpdates[size];
                for (i = 0; i < size; i++) {
                    Pair<InternalValidator, BatchUpdates> pair2 = (Pair) this.mUpdates.get(i);
                    conditions[i] = (InternalValidator) pair2.first;
                    updates[i] = (BatchUpdates) pair2.second;
                }
                dest.writeParcelableArray(conditions, flags);
                dest.writeParcelableArray(updates, flags);
            }
            SparseArray sparseArray = this.mActions;
            if (sparseArray == null) {
                dest.writeIntArray(null);
            } else {
                size = sparseArray.size();
                int[] ids2 = new int[size];
                InternalOnClickAction[] values2 = new InternalOnClickAction[size];
                for (int i2 = 0; i2 < size; i2++) {
                    ids2[i2] = this.mActions.keyAt(i2);
                    values2[i2] = (InternalOnClickAction) this.mActions.valueAt(i2);
                }
                dest.writeIntArray(ids2);
                dest.writeParcelableArray(values2, flags);
            }
        }
    }
}
