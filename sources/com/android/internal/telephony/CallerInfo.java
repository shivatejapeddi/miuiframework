package com.android.internal.telephony;

import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Country;
import android.location.CountryDetector;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SeempLog;
import com.android.i18n.phonenumbers.NumberParseException;
import com.android.i18n.phonenumbers.PhoneNumberUtil;
import com.android.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.android.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;
import com.android.internal.R;
import java.util.Locale;

public class CallerInfo {
    private static final String TAG = "CallerInfo";
    public static final long USER_TYPE_CURRENT = 0;
    public static final long USER_TYPE_WORK = 1;
    private static final boolean VDBG = Rlog.isLoggable(TAG, 2);
    public Drawable cachedPhoto;
    public Bitmap cachedPhotoIcon;
    public String cnapName;
    public Uri contactDisplayPhotoUri;
    public boolean contactExists;
    @UnsupportedAppUsage
    public long contactIdOrZero;
    public Uri contactRefUri;
    public Uri contactRingtoneUri;
    public String geoDescription;
    public boolean isCachedPhotoCurrent;
    public String lookupKey;
    private boolean mIsEmergency = false;
    private boolean mIsVoiceMail = false;
    @UnsupportedAppUsage
    public String name;
    public int namePresentation;
    public boolean needUpdate;
    public String normalizedNumber;
    @UnsupportedAppUsage
    public String numberLabel;
    public int numberPresentation;
    @UnsupportedAppUsage
    public int numberType;
    public String phoneLabel;
    @UnsupportedAppUsage
    public String phoneNumber;
    public int photoResource;
    public ComponentName preferredPhoneAccountComponent;
    public String preferredPhoneAccountId;
    public boolean shouldSendToVoicemail;
    public long userType = 0;

    public static CallerInfo getCallerInfo(Context context, Uri contactRef, Cursor cursor) {
        SeempLog.record_uri(12, contactRef);
        CallerInfo info = new CallerInfo();
        info.photoResource = 0;
        info.phoneLabel = null;
        info.numberType = 0;
        info.numberLabel = null;
        info.cachedPhoto = null;
        info.isCachedPhotoCurrent = false;
        info.contactExists = false;
        info.userType = 0;
        boolean z = VDBG;
        String str = TAG;
        if (z) {
            Rlog.v(str, "getCallerInfo() based on cursor...");
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("display_name");
                if (columnIndex != -1) {
                    info.name = cursor.getString(columnIndex);
                }
                columnIndex = cursor.getColumnIndex("number");
                if (columnIndex != -1) {
                    info.phoneNumber = cursor.getString(columnIndex);
                }
                columnIndex = cursor.getColumnIndex("normalized_number");
                if (columnIndex != -1) {
                    info.normalizedNumber = cursor.getString(columnIndex);
                }
                columnIndex = cursor.getColumnIndex("label");
                if (columnIndex != -1) {
                    int typeColumnIndex = cursor.getColumnIndex("type");
                    if (typeColumnIndex != -1) {
                        info.numberType = cursor.getInt(typeColumnIndex);
                        info.numberLabel = cursor.getString(columnIndex);
                        info.phoneLabel = Phone.getDisplayLabel(context, info.numberType, info.numberLabel).toString();
                    }
                }
                columnIndex = getColumnIndexForPersonId(contactRef, cursor);
                StringBuilder stringBuilder;
                if (columnIndex != -1) {
                    long contactId = cursor.getLong(columnIndex);
                    if (!(contactId == 0 || Contacts.isEnterpriseContactId(contactId))) {
                        info.contactIdOrZero = contactId;
                        if (VDBG) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("==> got info.contactIdOrZero: ");
                            stringBuilder.append(info.contactIdOrZero);
                            Rlog.v(str, stringBuilder.toString());
                        }
                    }
                    if (Contacts.isEnterpriseContactId(contactId)) {
                        info.userType = 1;
                    }
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Couldn't find contact_id column for ");
                    stringBuilder.append(contactRef);
                    Rlog.w(str, stringBuilder.toString());
                }
                int columnIndex2 = cursor.getColumnIndex(ContactsColumns.LOOKUP_KEY);
                if (columnIndex2 != -1) {
                    info.lookupKey = cursor.getString(columnIndex2);
                }
                columnIndex2 = cursor.getColumnIndex("photo_uri");
                if (columnIndex2 == -1 || cursor.getString(columnIndex2) == null) {
                    info.contactDisplayPhotoUri = null;
                } else {
                    info.contactDisplayPhotoUri = Uri.parse(cursor.getString(columnIndex2));
                }
                columnIndex2 = cursor.getColumnIndex(DataColumns.PREFERRED_PHONE_ACCOUNT_COMPONENT_NAME);
                if (!(columnIndex2 == -1 || cursor.getString(columnIndex2) == null)) {
                    info.preferredPhoneAccountComponent = ComponentName.unflattenFromString(cursor.getString(columnIndex2));
                }
                columnIndex2 = cursor.getColumnIndex(DataColumns.PREFERRED_PHONE_ACCOUNT_ID);
                if (!(columnIndex2 == -1 || cursor.getString(columnIndex2) == null)) {
                    info.preferredPhoneAccountId = cursor.getString(columnIndex2);
                }
                columnIndex2 = cursor.getColumnIndex("custom_ringtone");
                if (columnIndex2 == -1 || cursor.getString(columnIndex2) == null) {
                    info.contactRingtoneUri = null;
                } else if (TextUtils.isEmpty(cursor.getString(columnIndex2))) {
                    info.contactRingtoneUri = Uri.EMPTY;
                } else {
                    info.contactRingtoneUri = Uri.parse(cursor.getString(columnIndex2));
                }
                int columnIndex3 = cursor.getColumnIndex("send_to_voicemail");
                boolean z2 = columnIndex3 != -1 && cursor.getInt(columnIndex3) == 1;
                info.shouldSendToVoicemail = z2;
                info.contactExists = true;
            }
            cursor.close();
        }
        info.needUpdate = false;
        info.name = normalize(info.name);
        info.contactRefUri = contactRef;
        return info;
    }

    @UnsupportedAppUsage
    public static CallerInfo getCallerInfo(Context context, Uri contactRef) {
        ContentResolver cr = CallerInfoAsyncQuery.getCurrentProfileContentResolver(context);
        if (cr == null) {
            return null;
        }
        try {
            return getCallerInfo(context, contactRef, cr.query(contactRef, null, null, null, null));
        } catch (RuntimeException re) {
            Rlog.e(TAG, "Error getting caller info.", re);
            return null;
        }
    }

    @UnsupportedAppUsage
    public static CallerInfo getCallerInfo(Context context, String number) {
        if (VDBG) {
            Rlog.v(TAG, "getCallerInfo() based on number...");
        }
        return getCallerInfo(context, number, SubscriptionManager.getDefaultSubscriptionId());
    }

    @UnsupportedAppUsage
    public static CallerInfo getCallerInfo(Context context, String number, int subId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("number=");
        stringBuilder.append(number);
        stringBuilder.append(",subId=");
        stringBuilder.append(subId);
        SeempLog.record_str(12, stringBuilder.toString());
        if (TextUtils.isEmpty(number)) {
            return null;
        }
        if (PhoneNumberUtils.isLocalEmergencyNumber(context, number)) {
            return new CallerInfo().markAsEmergency(context);
        }
        if (PhoneNumberUtils.isVoiceMailNumber(subId, number)) {
            return new CallerInfo().markAsVoiceMail();
        }
        CallerInfo info = doSecondaryLookupIfNecessary(context, number, getCallerInfo(context, Uri.withAppendedPath(PhoneLookup.ENTERPRISE_CONTENT_FILTER_URI, Uri.encode(number))));
        if (TextUtils.isEmpty(info.phoneNumber)) {
            info.phoneNumber = number;
        }
        return info;
    }

    static CallerInfo doSecondaryLookupIfNecessary(Context context, String number, CallerInfo previousResult) {
        if (previousResult.contactExists || !PhoneNumberUtils.isUriNumber(number)) {
            return previousResult;
        }
        String username = PhoneNumberUtils.getUsernameFromUriNumber(number);
        if (PhoneNumberUtils.isGlobalPhoneNumber(username)) {
            return getCallerInfo(context, Uri.withAppendedPath(PhoneLookup.ENTERPRISE_CONTENT_FILTER_URI, Uri.encode(username)));
        }
        return previousResult;
    }

    public boolean isEmergencyNumber() {
        return this.mIsEmergency;
    }

    public boolean isVoiceMailNumber() {
        return this.mIsVoiceMail;
    }

    /* Access modifiers changed, original: 0000 */
    public CallerInfo markAsEmergency(Context context) {
        this.phoneNumber = context.getString(R.string.emergency_call_dialog_number_for_display);
        this.photoResource = R.drawable.picture_emergency;
        this.mIsEmergency = true;
        return this;
    }

    /* Access modifiers changed, original: 0000 */
    public CallerInfo markAsVoiceMail() {
        return markAsVoiceMail(SubscriptionManager.getDefaultSubscriptionId());
    }

    /* Access modifiers changed, original: 0000 */
    public CallerInfo markAsVoiceMail(int subId) {
        this.mIsVoiceMail = true;
        try {
            this.phoneNumber = TelephonyManager.getDefault().getVoiceMailAlphaTag(subId);
        } catch (SecurityException se) {
            Rlog.e(TAG, "Cannot access VoiceMail.", se);
        }
        return this;
    }

    private static String normalize(String s) {
        if (s == null || s.length() > 0) {
            return s;
        }
        return null;
    }

    private static int getColumnIndexForPersonId(Uri contactRef, Cursor cursor) {
        boolean z = VDBG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("- getColumnIndexForPersonId: contactRef URI = '");
            stringBuilder.append(contactRef);
            stringBuilder.append("'...");
            Rlog.v(str, stringBuilder.toString());
        }
        String url = contactRef.toString();
        String columnName = null;
        if (url.startsWith("content://com.android.contacts/data/phones")) {
            if (VDBG) {
                Rlog.v(str, "'data/phones' URI; using RawContacts.CONTACT_ID");
            }
            columnName = "contact_id";
        } else if (url.startsWith("content://com.android.contacts/data")) {
            if (VDBG) {
                Rlog.v(str, "'data' URI; using Data.CONTACT_ID");
            }
            columnName = "contact_id";
        } else if (url.startsWith("content://com.android.contacts/phone_lookup")) {
            if (VDBG) {
                Rlog.v(str, "'phone_lookup' URI; using PhoneLookup._ID");
            }
            columnName = "_id";
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unexpected prefix for contactRef '");
            stringBuilder2.append(url);
            stringBuilder2.append("'");
            Rlog.w(str, stringBuilder2.toString());
        }
        int columnIndex = columnName != null ? cursor.getColumnIndex(columnName) : -1;
        if (VDBG) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("==> Using column '");
            stringBuilder3.append(columnName);
            stringBuilder3.append("' (columnIndex = ");
            stringBuilder3.append(columnIndex);
            stringBuilder3.append(") for person_id lookup...");
            Rlog.v(str, stringBuilder3.toString());
        }
        return columnIndex;
    }

    public void updateGeoDescription(Context context, String fallbackNumber) {
        this.geoDescription = getGeoDescription(context, TextUtils.isEmpty(this.phoneNumber) ? fallbackNumber : this.phoneNumber);
    }

    public static String getGeoDescription(Context context, String number) {
        StringBuilder stringBuilder;
        String str = "'";
        boolean z = VDBG;
        String str2 = TAG;
        if (z) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("getGeoDescription('");
            stringBuilder2.append(number);
            stringBuilder2.append("')...");
            Rlog.v(str2, stringBuilder2.toString());
        }
        if (TextUtils.isEmpty(number)) {
            return null;
        }
        StringBuilder stringBuilder3;
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
        Locale locale = context.getResources().getConfiguration().locale;
        String countryIso = getCurrentCountryIso(context, locale);
        PhoneNumber pn = null;
        try {
            if (VDBG) {
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("parsing '");
                stringBuilder3.append(number);
                stringBuilder3.append("' for countryIso '");
                stringBuilder3.append(countryIso);
                stringBuilder3.append("'...");
                Rlog.v(str2, stringBuilder3.toString());
            }
            pn = util.parse(number, countryIso);
            if (VDBG) {
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("- parsed number: ");
                stringBuilder3.append(pn);
                Rlog.v(str2, stringBuilder3.toString());
            }
        } catch (NumberParseException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("getGeoDescription: NumberParseException for incoming number '");
            stringBuilder.append(miui.telephony.PhoneNumberUtils.toLogSafePhoneNumber(number));
            stringBuilder.append(str);
            Rlog.w(str2, stringBuilder.toString());
        }
        if (pn == null) {
            return null;
        }
        String description = null;
        try {
            description = geocoder.getDescriptionForNumber(pn, locale);
        } catch (Exception e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("getDescriptionForNumber exception ");
            stringBuilder.append(e2);
            Rlog.e(str2, stringBuilder.toString());
        }
        if (VDBG) {
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append("- got description: '");
            stringBuilder3.append(description);
            stringBuilder3.append(str);
            Rlog.v(str2, stringBuilder3.toString());
        }
        return description;
    }

    private static String getCurrentCountryIso(Context context, Locale locale) {
        String countryIso = null;
        CountryDetector detector = (CountryDetector) context.getSystemService(Context.COUNTRY_DETECTOR);
        String str = TAG;
        if (detector != null) {
            Country country = detector.detectCountry();
            if (country != null) {
                countryIso = country.getCountryIso();
            } else {
                Rlog.e(str, "CountryDetector.detectCountry() returned null.");
            }
        }
        if (countryIso != null) {
            return countryIso;
        }
        countryIso = locale.getCountry();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No CountryDetector; falling back to countryIso based on locale: ");
        stringBuilder.append(countryIso);
        Rlog.w(str, stringBuilder.toString());
        return countryIso;
    }

    protected static String getCurrentCountryIso(Context context) {
        return getCurrentCountryIso(context, Locale.getDefault());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(128);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(super.toString());
        stringBuilder2.append(" { ");
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("name ");
        String str = "null";
        String str2 = "non-null";
        stringBuilder2.append(this.name == null ? str : str2);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(", phoneNumber ");
        if (this.phoneNumber != null) {
            str = str2;
        }
        stringBuilder2.append(str);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }
}
