package android.content.pm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public class LabeledIntent extends Intent {
    public static final Creator<LabeledIntent> CREATOR = new Creator<LabeledIntent>() {
        public LabeledIntent createFromParcel(Parcel source) {
            return new LabeledIntent(source);
        }

        public LabeledIntent[] newArray(int size) {
            return new LabeledIntent[size];
        }
    };
    private int mIcon;
    private int mLabelRes;
    private CharSequence mNonLocalizedLabel;
    private String mSourcePackage;

    public LabeledIntent(Intent origIntent, String sourcePackage, int labelRes, int icon) {
        super(origIntent);
        this.mSourcePackage = sourcePackage;
        this.mLabelRes = labelRes;
        this.mNonLocalizedLabel = null;
        this.mIcon = icon;
    }

    public LabeledIntent(Intent origIntent, String sourcePackage, CharSequence nonLocalizedLabel, int icon) {
        super(origIntent);
        this.mSourcePackage = sourcePackage;
        this.mLabelRes = 0;
        this.mNonLocalizedLabel = nonLocalizedLabel;
        this.mIcon = icon;
    }

    public LabeledIntent(String sourcePackage, int labelRes, int icon) {
        this.mSourcePackage = sourcePackage;
        this.mLabelRes = labelRes;
        this.mNonLocalizedLabel = null;
        this.mIcon = icon;
    }

    public LabeledIntent(String sourcePackage, CharSequence nonLocalizedLabel, int icon) {
        this.mSourcePackage = sourcePackage;
        this.mLabelRes = 0;
        this.mNonLocalizedLabel = nonLocalizedLabel;
        this.mIcon = icon;
    }

    public String getSourcePackage() {
        return this.mSourcePackage;
    }

    public int getLabelResource() {
        return this.mLabelRes;
    }

    public CharSequence getNonLocalizedLabel() {
        return this.mNonLocalizedLabel;
    }

    public int getIconResource() {
        return this.mIcon;
    }

    public CharSequence loadLabel(PackageManager pm) {
        CharSequence charSequence = this.mNonLocalizedLabel;
        if (charSequence != null) {
            return charSequence;
        }
        charSequence = this.mLabelRes;
        if (charSequence != null) {
            String str = this.mSourcePackage;
            if (str != null) {
                charSequence = pm.getText(str, charSequence, null);
                if (charSequence != null) {
                    return charSequence;
                }
            }
        }
        return null;
    }

    public Drawable loadIcon(PackageManager pm) {
        Drawable icon = this.mIcon;
        if (icon != null) {
            String str = this.mSourcePackage;
            if (str != null) {
                icon = pm.getDrawable(str, icon, null);
                if (icon != null) {
                    return icon;
                }
            }
        }
        return null;
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        super.writeToParcel(dest, parcelableFlags);
        dest.writeString(this.mSourcePackage);
        dest.writeInt(this.mLabelRes);
        TextUtils.writeToParcel(this.mNonLocalizedLabel, dest, parcelableFlags);
        dest.writeInt(this.mIcon);
    }

    protected LabeledIntent(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        this.mSourcePackage = in.readString();
        this.mLabelRes = in.readInt();
        this.mNonLocalizedLabel = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.mIcon = in.readInt();
    }
}
