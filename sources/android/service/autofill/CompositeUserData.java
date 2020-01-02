package android.service.autofill;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;

public final class CompositeUserData implements FieldClassificationUserData, Parcelable {
    public static final Creator<CompositeUserData> CREATOR = new Creator<CompositeUserData>() {
        public CompositeUserData createFromParcel(Parcel parcel) {
            return new CompositeUserData((UserData) parcel.readParcelable(null), (UserData) parcel.readParcelable(null));
        }

        public CompositeUserData[] newArray(int size) {
            return new CompositeUserData[size];
        }
    };
    private final String[] mCategories;
    private final UserData mGenericUserData;
    private final UserData mPackageUserData;
    private final String[] mValues;

    public CompositeUserData(UserData genericUserData, UserData packageUserData) {
        this.mGenericUserData = genericUserData;
        this.mPackageUserData = packageUserData;
        String[] packageCategoryIds = this.mPackageUserData.getCategoryIds();
        String[] packageValues = this.mPackageUserData.getValues();
        ArrayList<String> categoryIds = new ArrayList(packageCategoryIds.length);
        ArrayList<String> values = new ArrayList(packageValues.length);
        Collections.addAll(categoryIds, packageCategoryIds);
        Collections.addAll(values, packageValues);
        String[] genericCategoryIds = this.mGenericUserData;
        if (genericCategoryIds != null) {
            genericCategoryIds = genericCategoryIds.getCategoryIds();
            String[] genericValues = this.mGenericUserData.getValues();
            int size = this.mGenericUserData.getCategoryIds().length;
            for (int i = 0; i < size; i++) {
                if (!categoryIds.contains(genericCategoryIds[i])) {
                    categoryIds.add(genericCategoryIds[i]);
                    values.add(genericValues[i]);
                }
            }
        }
        this.mCategories = new String[categoryIds.size()];
        categoryIds.toArray(this.mCategories);
        this.mValues = new String[values.size()];
        values.toArray(this.mValues);
    }

    public String getFieldClassificationAlgorithm() {
        String packageDefaultAlgo = this.mPackageUserData.getFieldClassificationAlgorithm();
        if (packageDefaultAlgo != null) {
            return packageDefaultAlgo;
        }
        String str;
        UserData userData = this.mGenericUserData;
        if (userData == null) {
            str = null;
        } else {
            str = userData.getFieldClassificationAlgorithm();
        }
        return str;
    }

    public Bundle getDefaultFieldClassificationArgs() {
        Bundle packageDefaultArgs = this.mPackageUserData.getDefaultFieldClassificationArgs();
        if (packageDefaultArgs != null) {
            return packageDefaultArgs;
        }
        Bundle bundle;
        UserData userData = this.mGenericUserData;
        if (userData == null) {
            bundle = null;
        } else {
            bundle = userData.getDefaultFieldClassificationArgs();
        }
        return bundle;
    }

    public String getFieldClassificationAlgorithmForCategory(String categoryId) {
        Preconditions.checkNotNull(categoryId);
        ArrayMap<String, String> categoryAlgorithms = getFieldClassificationAlgorithms();
        if (categoryAlgorithms == null || !categoryAlgorithms.containsKey(categoryId)) {
            return null;
        }
        return (String) categoryAlgorithms.get(categoryId);
    }

    public ArrayMap<String, String> getFieldClassificationAlgorithms() {
        ArrayMap genericAlgos;
        ArrayMap packageAlgos = this.mPackageUserData.getFieldClassificationAlgorithms();
        ArrayMap<String, String> genericAlgos2 = this.mGenericUserData;
        if (genericAlgos2 == null) {
            genericAlgos = null;
        } else {
            genericAlgos = genericAlgos2.getFieldClassificationAlgorithms();
        }
        ArrayMap<String, String> categoryAlgorithms = null;
        if (!(packageAlgos == null && genericAlgos == null)) {
            categoryAlgorithms = new ArrayMap();
            if (genericAlgos != null) {
                categoryAlgorithms.putAll(genericAlgos);
            }
            if (packageAlgos != null) {
                categoryAlgorithms.putAll(packageAlgos);
            }
        }
        return categoryAlgorithms;
    }

    public ArrayMap<String, Bundle> getFieldClassificationArgs() {
        ArrayMap genericArgs;
        ArrayMap packageArgs = this.mPackageUserData.getFieldClassificationArgs();
        ArrayMap<String, Bundle> genericArgs2 = this.mGenericUserData;
        if (genericArgs2 == null) {
            genericArgs = null;
        } else {
            genericArgs = genericArgs2.getFieldClassificationArgs();
        }
        ArrayMap<String, Bundle> categoryArgs = null;
        if (!(packageArgs == null && genericArgs == null)) {
            categoryArgs = new ArrayMap();
            if (genericArgs != null) {
                categoryArgs.putAll(genericArgs);
            }
            if (packageArgs != null) {
                categoryArgs.putAll(packageArgs);
            }
        }
        return categoryArgs;
    }

    public String[] getCategoryIds() {
        return this.mCategories;
    }

    public String[] getValues() {
        return this.mValues;
    }

    public String toString() {
        if (!Helper.sDebug) {
            return super.toString();
        }
        StringBuilder builder = new StringBuilder("genericUserData=");
        builder.append(this.mGenericUserData);
        builder.append(", packageUserData=");
        return builder.append(this.mPackageUserData).toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(this.mGenericUserData, 0);
        parcel.writeParcelable(this.mPackageUserData, 0);
    }
}
