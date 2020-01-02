package android.telephony;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SubscriptionInfo implements Parcelable {
    public static final Creator<SubscriptionInfo> CREATOR = new Creator<SubscriptionInfo>() {
        public SubscriptionInfo createFromParcel(Parcel source) {
            Parcel parcel = source;
            int readInt = source.readInt();
            String readString = source.readString();
            int readInt2 = source.readInt();
            CharSequence readCharSequence = source.readCharSequence();
            CharSequence readCharSequence2 = source.readCharSequence();
            int readInt3 = source.readInt();
            int readInt4 = source.readInt();
            String readString2 = source.readString();
            int readInt5 = source.readInt();
            String readString3 = source.readString();
            String readString4 = source.readString();
            String readString5 = source.readString();
            Bitmap bitmap = (Bitmap) parcel.readParcelable(Bitmap.class.getClassLoader());
            boolean readBoolean = source.readBoolean();
            UiccAccessRule[] uiccAccessRuleArr = (UiccAccessRule[]) parcel.createTypedArray(UiccAccessRule.CREATOR);
            String readString6 = source.readString();
            int readInt6 = source.readInt();
            boolean readBoolean2 = source.readBoolean();
            String readString7 = source.readString();
            boolean readBoolean3 = source.readBoolean();
            int readInt7 = source.readInt();
            int readInt8 = source.readInt();
            int readInt9 = source.readInt();
            String[] ehplmns = source.readStringArray();
            String[] hplmns = source.readStringArray();
            String[] ehplmns2 = ehplmns;
            SubscriptionInfo info = new SubscriptionInfo(readInt, readString, readInt2, readCharSequence, readCharSequence2, readInt3, readInt4, readString2, readInt5, bitmap, readString3, readString4, readString5, readBoolean, uiccAccessRuleArr, readString6, readInt6, readBoolean2, readString7, readBoolean3, readInt7, readInt8, readInt9, source.readString());
            info.setAssociatedPlmns(ehplmns2, hplmns);
            return info;
        }

        public SubscriptionInfo[] newArray(int size) {
            return new SubscriptionInfo[size];
        }
    };
    private static final int TEXT_SIZE = 16;
    private UiccAccessRule[] mAccessRules;
    private int mCardId;
    private String mCardString;
    private int mCarrierId;
    private CharSequence mCarrierName;
    private String mCountryIso;
    private int mDataRoaming;
    private CharSequence mDisplayName;
    private String[] mEhplmns;
    private String mGroupOwner;
    private ParcelUuid mGroupUUID;
    private String[] mHplmns;
    private String mIccId;
    private Bitmap mIconBitmap;
    private int mIconTint;
    private int mId;
    private boolean mIsEmbedded;
    private boolean mIsGroupDisabled;
    private boolean mIsOpportunistic;
    private String mMcc;
    private String mMnc;
    private int mNameSource;
    private String mNumber;
    private int mProfileClass;
    private int mSimSlotIndex;
    private int mSubscriptionType;

    public SubscriptionInfo(int id, String iccId, int simSlotIndex, CharSequence displayName, CharSequence carrierName, int nameSource, int iconTint, String number, int roaming, Bitmap icon, String mcc, String mnc, String countryIso, boolean isEmbedded, UiccAccessRule[] accessRules, String cardString) {
        this(id, iccId, simSlotIndex, displayName, carrierName, nameSource, iconTint, number, roaming, icon, mcc, mnc, countryIso, isEmbedded, accessRules, cardString, -1, false, null, false, -1, -1, 0, null);
    }

    public SubscriptionInfo(int id, String iccId, int simSlotIndex, CharSequence displayName, CharSequence carrierName, int nameSource, int iconTint, String number, int roaming, Bitmap icon, String mcc, String mnc, String countryIso, boolean isEmbedded, UiccAccessRule[] accessRules, String cardString, boolean isOpportunistic, String groupUUID, int carrierId, int profileClass) {
        this(id, iccId, simSlotIndex, displayName, carrierName, nameSource, iconTint, number, roaming, icon, mcc, mnc, countryIso, isEmbedded, accessRules, cardString, -1, isOpportunistic, groupUUID, false, carrierId, profileClass, 0, null);
    }

    public SubscriptionInfo(int id, String iccId, int simSlotIndex, CharSequence displayName, CharSequence carrierName, int nameSource, int iconTint, String number, int roaming, Bitmap icon, String mcc, String mnc, String countryIso, boolean isEmbedded, UiccAccessRule[] accessRules, String cardString, int cardId, boolean isOpportunistic, String groupUUID, boolean isGroupDisabled, int carrierId, int profileClass, int subType, String groupOwner) {
        this.mIsGroupDisabled = false;
        this.mId = id;
        this.mIccId = iccId;
        this.mSimSlotIndex = simSlotIndex;
        this.mDisplayName = displayName;
        this.mCarrierName = carrierName;
        this.mNameSource = nameSource;
        this.mIconTint = iconTint;
        this.mNumber = number;
        this.mDataRoaming = roaming;
        this.mIconBitmap = icon;
        this.mMcc = mcc;
        this.mMnc = mnc;
        this.mCountryIso = countryIso;
        this.mIsEmbedded = isEmbedded;
        this.mAccessRules = accessRules;
        this.mCardString = cardString;
        this.mCardId = cardId;
        this.mIsOpportunistic = isOpportunistic;
        this.mGroupUUID = groupUUID == null ? null : ParcelUuid.fromString(groupUUID);
        this.mIsGroupDisabled = isGroupDisabled;
        this.mCarrierId = carrierId;
        this.mProfileClass = profileClass;
        this.mSubscriptionType = subType;
        this.mGroupOwner = groupOwner;
    }

    public int getSubscriptionId() {
        return this.mId;
    }

    public String getIccId() {
        return this.mIccId;
    }

    public int getSimSlotIndex() {
        return this.mSimSlotIndex;
    }

    public int getCarrierId() {
        return this.mCarrierId;
    }

    public CharSequence getDisplayName() {
        return this.mDisplayName;
    }

    @UnsupportedAppUsage
    public void setDisplayName(CharSequence name) {
        this.mDisplayName = name;
    }

    public CharSequence getCarrierName() {
        return this.mCarrierName;
    }

    public void setCarrierName(CharSequence name) {
        this.mCarrierName = name;
    }

    @UnsupportedAppUsage
    public int getNameSource() {
        return this.mNameSource;
    }

    public void setAssociatedPlmns(String[] ehplmns, String[] hplmns) {
        this.mEhplmns = ehplmns;
        this.mHplmns = hplmns;
    }

    public Bitmap createIconBitmap(Context context) {
        int width = this.mIconBitmap.getWidth();
        int height = this.mIconBitmap.getHeight();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        Bitmap workingBitmap = Bitmap.createBitmap(metrics, width, height, this.mIconBitmap.getConfig());
        Canvas canvas = new Canvas(workingBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(this.mIconTint, Mode.SRC_ATOP));
        canvas.drawBitmap(this.mIconBitmap, 0.0f, 0.0f, paint);
        paint.setColorFilter(null);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.create("sans-serif", 0));
        paint.setColor(-1);
        paint.setTextSize(metrics.density * 16.0f);
        String index = String.format("%d", new Object[]{Integer.valueOf(this.mSimSlotIndex + 1)});
        Rect textBound = new Rect();
        paint.getTextBounds(index, 0, 1, textBound);
        canvas.drawText(index, (((float) width) / 2.0f) - ((float) textBound.centerX()), (((float) height) / 2.0f) - ((float) textBound.centerY()), paint);
        return workingBitmap;
    }

    public int getIconTint() {
        return this.mIconTint;
    }

    @UnsupportedAppUsage
    public void setIconTint(int iconTint) {
        this.mIconTint = iconTint;
    }

    public String getNumber() {
        return this.mNumber;
    }

    public int getDataRoaming() {
        return this.mDataRoaming;
    }

    @Deprecated
    public int getMcc() {
        int i = 0;
        try {
            if (this.mMcc != null) {
                i = Integer.valueOf(this.mMcc).intValue();
            }
            return i;
        } catch (NumberFormatException e) {
            Log.w(SubscriptionInfo.class.getSimpleName(), "MCC string is not a number");
            return 0;
        }
    }

    @Deprecated
    public int getMnc() {
        int i = 0;
        try {
            if (this.mMnc != null) {
                i = Integer.valueOf(this.mMnc).intValue();
            }
            return i;
        } catch (NumberFormatException e) {
            Log.w(SubscriptionInfo.class.getSimpleName(), "MNC string is not a number");
            return 0;
        }
    }

    public String getMccString() {
        return this.mMcc;
    }

    public String getMncString() {
        return this.mMnc;
    }

    public String getCountryIso() {
        return this.mCountryIso;
    }

    public boolean isEmbedded() {
        return this.mIsEmbedded;
    }

    public boolean isOpportunistic() {
        return this.mIsOpportunistic;
    }

    public ParcelUuid getGroupUuid() {
        return this.mGroupUUID;
    }

    public List<String> getEhplmns() {
        String[] strArr = this.mEhplmns;
        return strArr == null ? Collections.emptyList() : Arrays.asList(strArr);
    }

    public List<String> getHplmns() {
        String[] strArr = this.mHplmns;
        return strArr == null ? Collections.emptyList() : Arrays.asList(strArr);
    }

    public String getGroupOwner() {
        return this.mGroupOwner;
    }

    @SystemApi
    public int getProfileClass() {
        return this.mProfileClass;
    }

    public int getSubscriptionType() {
        return this.mSubscriptionType;
    }

    @Deprecated
    public boolean canManageSubscription(Context context) {
        return canManageSubscription(context, context.getPackageName());
    }

    @Deprecated
    public boolean canManageSubscription(Context context, String packageName) {
        if (!isEmbedded()) {
            throw new UnsupportedOperationException("Not an embedded subscription");
        } else if (this.mAccessRules == null) {
            return false;
        } else {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 64);
                for (UiccAccessRule rule : this.mAccessRules) {
                    if (rule.getCarrierPrivilegeStatus(packageInfo) == 1) {
                        return true;
                    }
                }
                return false;
            } catch (NameNotFoundException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown package: ");
                stringBuilder.append(packageName);
                throw new IllegalArgumentException(stringBuilder.toString(), e);
            }
        }
    }

    @SystemApi
    public List<UiccAccessRule> getAccessRules() {
        if (isEmbedded()) {
            UiccAccessRule[] uiccAccessRuleArr = this.mAccessRules;
            if (uiccAccessRuleArr == null) {
                return null;
            }
            return Arrays.asList(uiccAccessRuleArr);
        }
        throw new UnsupportedOperationException("Not an embedded subscription");
    }

    public String getCardString() {
        return this.mCardString;
    }

    public int getCardId() {
        return this.mCardId;
    }

    public void setGroupDisabled(boolean isGroupDisabled) {
        this.mIsGroupDisabled = isGroupDisabled;
    }

    public boolean isGroupDisabled() {
        return this.mIsGroupDisabled;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mIccId);
        dest.writeInt(this.mSimSlotIndex);
        dest.writeCharSequence(this.mDisplayName);
        dest.writeCharSequence(this.mCarrierName);
        dest.writeInt(this.mNameSource);
        dest.writeInt(this.mIconTint);
        dest.writeString(this.mNumber);
        dest.writeInt(this.mDataRoaming);
        dest.writeString(this.mMcc);
        dest.writeString(this.mMnc);
        dest.writeString(this.mCountryIso);
        dest.writeParcelable(this.mIconBitmap, flags);
        dest.writeBoolean(this.mIsEmbedded);
        dest.writeTypedArray(this.mAccessRules, flags);
        dest.writeString(this.mCardString);
        dest.writeInt(this.mCardId);
        dest.writeBoolean(this.mIsOpportunistic);
        ParcelUuid parcelUuid = this.mGroupUUID;
        dest.writeString(parcelUuid == null ? null : parcelUuid.toString());
        dest.writeBoolean(this.mIsGroupDisabled);
        dest.writeInt(this.mCarrierId);
        dest.writeInt(this.mProfileClass);
        dest.writeInt(this.mSubscriptionType);
        dest.writeStringArray(this.mEhplmns);
        dest.writeStringArray(this.mHplmns);
        dest.writeString(this.mGroupOwner);
    }

    public int describeContents() {
        return 0;
    }

    public static String givePrintableIccid(String iccId) {
        if (iccId == null) {
            return null;
        }
        if (iccId.length() <= 9 || Build.IS_DEBUGGABLE) {
            return iccId;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(iccId.substring(0, 9));
        stringBuilder.append(Rlog.pii(false, iccId.substring(9)));
        return stringBuilder.toString();
    }

    public String toString() {
        String iccIdToPrint = givePrintableIccid(this.mIccId);
        String cardStringToPrint = givePrintableIccid(this.mCardString);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{id=");
        stringBuilder.append(this.mId);
        stringBuilder.append(", iccId=");
        stringBuilder.append(iccIdToPrint);
        stringBuilder.append(" simSlotIndex=");
        stringBuilder.append(this.mSimSlotIndex);
        stringBuilder.append(" carrierId=");
        stringBuilder.append(this.mCarrierId);
        stringBuilder.append(" displayName=");
        stringBuilder.append(this.mDisplayName);
        stringBuilder.append(" carrierName=");
        stringBuilder.append(this.mCarrierName);
        stringBuilder.append(" nameSource=");
        stringBuilder.append(this.mNameSource);
        stringBuilder.append(" iconTint=");
        stringBuilder.append(this.mIconTint);
        stringBuilder.append(" mNumber=");
        stringBuilder.append(Rlog.pii(false, this.mNumber));
        stringBuilder.append(" dataRoaming=");
        stringBuilder.append(this.mDataRoaming);
        stringBuilder.append(" iconBitmap=");
        stringBuilder.append(this.mIconBitmap);
        stringBuilder.append(" mcc ");
        stringBuilder.append(this.mMcc);
        stringBuilder.append(" mnc ");
        stringBuilder.append(this.mMnc);
        stringBuilder.append("mCountryIso=");
        stringBuilder.append(this.mCountryIso);
        stringBuilder.append(" isEmbedded ");
        stringBuilder.append(this.mIsEmbedded);
        stringBuilder.append(" accessRules ");
        stringBuilder.append(Arrays.toString(this.mAccessRules));
        stringBuilder.append(" cardString=");
        stringBuilder.append(cardStringToPrint);
        stringBuilder.append(" cardId=");
        stringBuilder.append(this.mCardId);
        stringBuilder.append(" isOpportunistic ");
        stringBuilder.append(this.mIsOpportunistic);
        stringBuilder.append(" mGroupUUID=");
        stringBuilder.append(this.mGroupUUID);
        stringBuilder.append(" mIsGroupDisabled=");
        stringBuilder.append(this.mIsGroupDisabled);
        stringBuilder.append(" profileClass=");
        stringBuilder.append(this.mProfileClass);
        stringBuilder.append(" ehplmns = ");
        stringBuilder.append(Arrays.toString(this.mEhplmns));
        stringBuilder.append(" hplmns = ");
        stringBuilder.append(Arrays.toString(this.mHplmns));
        stringBuilder.append(" subscriptionType=");
        stringBuilder.append(this.mSubscriptionType);
        stringBuilder.append(" mGroupOwner=");
        stringBuilder.append(this.mGroupOwner);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mId), Integer.valueOf(this.mSimSlotIndex), Integer.valueOf(this.mNameSource), Integer.valueOf(this.mIconTint), Integer.valueOf(this.mDataRoaming), Boolean.valueOf(this.mIsEmbedded), Boolean.valueOf(this.mIsOpportunistic), this.mGroupUUID, this.mIccId, this.mNumber, this.mMcc, this.mMnc, this.mCountryIso, this.mCardString, Integer.valueOf(this.mCardId), this.mDisplayName, this.mCarrierName, this.mAccessRules, Boolean.valueOf(this.mIsGroupDisabled), Integer.valueOf(this.mCarrierId), Integer.valueOf(this.mProfileClass), this.mGroupOwner});
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        try {
            SubscriptionInfo toCompare = (SubscriptionInfo) obj;
            if (this.mId == toCompare.mId && this.mSimSlotIndex == toCompare.mSimSlotIndex && this.mNameSource == toCompare.mNameSource && this.mIconTint == toCompare.mIconTint && this.mDataRoaming == toCompare.mDataRoaming && this.mIsEmbedded == toCompare.mIsEmbedded && this.mIsOpportunistic == toCompare.mIsOpportunistic && this.mIsGroupDisabled == toCompare.mIsGroupDisabled && this.mCarrierId == toCompare.mCarrierId && Objects.equals(this.mGroupUUID, toCompare.mGroupUUID) && Objects.equals(this.mIccId, toCompare.mIccId) && Objects.equals(this.mNumber, toCompare.mNumber) && Objects.equals(this.mMcc, toCompare.mMcc) && Objects.equals(this.mMnc, toCompare.mMnc) && Objects.equals(this.mCountryIso, toCompare.mCountryIso) && Objects.equals(this.mCardString, toCompare.mCardString) && Objects.equals(Integer.valueOf(this.mCardId), Integer.valueOf(toCompare.mCardId)) && Objects.equals(this.mGroupOwner, toCompare.mGroupOwner) && TextUtils.equals(this.mDisplayName, toCompare.mDisplayName) && TextUtils.equals(this.mCarrierName, toCompare.mCarrierName) && Arrays.equals(this.mAccessRules, toCompare.mAccessRules) && this.mProfileClass == toCompare.mProfileClass && Arrays.equals(this.mEhplmns, toCompare.mEhplmns) && Arrays.equals(this.mHplmns, toCompare.mHplmns)) {
                z = true;
            }
            return z;
        } catch (ClassCastException e) {
            return false;
        }
    }
}
