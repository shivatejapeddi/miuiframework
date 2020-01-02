package android.content.pm;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import java.util.Collections;
import java.util.List;

public class StringParceledListSlice extends BaseParceledListSlice<String> {
    public static final ClassLoaderCreator<StringParceledListSlice> CREATOR = new ClassLoaderCreator<StringParceledListSlice>() {
        public StringParceledListSlice createFromParcel(Parcel in) {
            return new StringParceledListSlice(in, null, null);
        }

        public StringParceledListSlice createFromParcel(Parcel in, ClassLoader loader) {
            return new StringParceledListSlice(in, loader, null);
        }

        public StringParceledListSlice[] newArray(int size) {
            return new StringParceledListSlice[size];
        }
    };

    /* synthetic */ StringParceledListSlice(Parcel x0, ClassLoader x1, AnonymousClass1 x2) {
        this(x0, x1);
    }

    @UnsupportedAppUsage
    public /* bridge */ /* synthetic */ List getList() {
        return super.getList();
    }

    public /* bridge */ /* synthetic */ void setInlineCountLimit(int i) {
        super.setInlineCountLimit(i);
    }

    public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    public StringParceledListSlice(List<String> list) {
        super(list);
    }

    private StringParceledListSlice(Parcel in, ClassLoader loader) {
        super(in, loader);
    }

    public static StringParceledListSlice emptyList() {
        return new StringParceledListSlice(Collections.emptyList());
    }

    public int describeContents() {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public void writeElement(String parcelable, Parcel reply, int callFlags) {
        reply.writeString(parcelable);
    }

    /* Access modifiers changed, original: protected */
    public void writeParcelableCreator(String parcelable, Parcel dest) {
    }

    /* Access modifiers changed, original: protected */
    public Creator<?> readParcelableCreator(Parcel from, ClassLoader loader) {
        return Parcel.STRING_CREATOR;
    }
}
