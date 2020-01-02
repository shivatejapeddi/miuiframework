package android.provider;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.UserInfo;
import android.database.Cursor;
import android.location.Country;
import android.location.CountryDetector;
import android.net.Uri;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.ContactsContract.CommonDataKinds.Callable;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.DataUsageFeedback;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.CallerInfo;
import java.util.List;

public class CallLog {
    public static final String AUTHORITY = "call_log";
    public static final Uri CONTENT_URI = Uri.parse("content://call_log");
    private static final String LOG_TAG = "CallLog";
    public static final String SHADOW_AUTHORITY = "call_log_shadow";
    private static final boolean VERBOSE_LOG = false;

    public static class Calls implements BaseColumns {
        public static final String ADD_FOR_ALL_USERS = "add_for_all_users";
        public static final String ALLOW_VOICEMAILS_PARAM_KEY = "allow_voicemails";
        public static final int ANSWERED_EXTERNALLY_TYPE = 7;
        public static final int BLOCKED_TYPE = 6;
        public static final String BLOCK_REASON = "block_reason";
        public static final int BLOCK_REASON_BLOCKED_NUMBER = 3;
        public static final int BLOCK_REASON_CALL_SCREENING_SERVICE = 1;
        public static final int BLOCK_REASON_DIRECT_TO_VOICEMAIL = 2;
        public static final int BLOCK_REASON_NOT_BLOCKED = 0;
        public static final int BLOCK_REASON_NOT_IN_CONTACTS = 7;
        public static final int BLOCK_REASON_PAY_PHONE = 6;
        public static final int BLOCK_REASON_RESTRICTED_NUMBER = 5;
        public static final int BLOCK_REASON_UNKNOWN_NUMBER = 4;
        public static final String CACHED_FORMATTED_NUMBER = "formatted_number";
        public static final String CACHED_LOOKUP_URI = "lookup_uri";
        public static final String CACHED_MATCHED_NUMBER = "matched_number";
        public static final String CACHED_NAME = "name";
        public static final String CACHED_NORMALIZED_NUMBER = "normalized_number";
        public static final String CACHED_NUMBER_LABEL = "numberlabel";
        public static final String CACHED_NUMBER_TYPE = "numbertype";
        public static final String CACHED_PHOTO_ID = "photo_id";
        public static final String CACHED_PHOTO_URI = "photo_uri";
        public static final String CALL_SCREENING_APP_NAME = "call_screening_app_name";
        public static final String CALL_SCREENING_COMPONENT_NAME = "call_screening_component_name";
        public static final Uri CONTENT_FILTER_URI = Uri.parse("content://call_log/calls/filter");
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/calls";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/calls";
        public static final Uri CONTENT_URI = Uri.parse("content://call_log/calls");
        public static final Uri CONTENT_URI_WITH_VOICEMAIL = CONTENT_URI.buildUpon().appendQueryParameter(ALLOW_VOICEMAILS_PARAM_KEY, "true").build();
        public static final String COUNTRY_ISO = "countryiso";
        public static final String DATA_USAGE = "data_usage";
        public static final String DATE = "date";
        public static final String DEFAULT_SORT_ORDER = "date DESC";
        public static final String DURATION = "duration";
        public static final String EXTRA_CALL_TYPE_FILTER = "android.provider.extra.CALL_TYPE_FILTER";
        public static final String FEATURES = "features";
        public static final int FEATURES_ASSISTED_DIALING_USED = 16;
        public static final int FEATURES_HD_CALL = 4;
        public static final int FEATURES_PULLED_EXTERNALLY = 2;
        public static final int FEATURES_RTT = 32;
        public static final int FEATURES_VIDEO = 1;
        public static final int FEATURES_WIFI = 8;
        public static final String GEOCODED_LOCATION = "geocoded_location";
        public static final int INCOMING_TYPE = 1;
        public static final String IS_READ = "is_read";
        public static final String LAST_MODIFIED = "last_modified";
        public static final String LIMIT_PARAM_KEY = "limit";
        private static final int MIN_DURATION_FOR_NORMALIZED_NUMBER_UPDATE_MS = 10000;
        public static final int MISSED_TYPE = 3;
        public static final String NEW = "new";
        public static final String NUMBER = "number";
        public static final String NUMBER_PRESENTATION = "presentation";
        public static final String OFFSET_PARAM_KEY = "offset";
        public static final int OUTGOING_TYPE = 2;
        public static final String PHONE_ACCOUNT_ADDRESS = "phone_account_address";
        public static final String PHONE_ACCOUNT_COMPONENT_NAME = "subscription_component_name";
        public static final String PHONE_ACCOUNT_HIDDEN = "phone_account_hidden";
        public static final String PHONE_ACCOUNT_ID = "subscription_id";
        public static final String POST_DIAL_DIGITS = "post_dial_digits";
        public static final int PRESENTATION_ALLOWED = 1;
        public static final int PRESENTATION_PAYPHONE = 4;
        public static final int PRESENTATION_RESTRICTED = 2;
        public static final int PRESENTATION_UNKNOWN = 3;
        public static final int REJECTED_TYPE = 5;
        public static final Uri SHADOW_CONTENT_URI = Uri.parse("content://call_log_shadow/calls");
        public static final String SUB_ID = "sub_id";
        public static final String TRANSCRIPTION = "transcription";
        public static final String TRANSCRIPTION_STATE = "transcription_state";
        public static final String TYPE = "type";
        public static final String VIA_NUMBER = "via_number";
        public static final int VOICEMAIL_TYPE = 4;
        public static final String VOICEMAIL_URI = "voicemail_uri";

        public static Uri addCall(CallerInfo ci, Context context, String number, int presentation, int callType, int features, PhoneAccountHandle accountHandle, long start, int duration, Long dataUsage) {
            return addCall(ci, context, number, "", "", presentation, callType, features, accountHandle, start, duration, dataUsage, false, null, false, 0, null, null);
        }

        public static Uri addCall(CallerInfo ci, Context context, String number, String postDialDigits, String viaNumber, int presentation, int callType, int features, PhoneAccountHandle accountHandle, long start, int duration, Long dataUsage, boolean addForAllUsers, UserHandle userToBeInsertedTo) {
            return addCall(ci, context, number, postDialDigits, viaNumber, presentation, callType, features, accountHandle, start, duration, dataUsage, addForAllUsers, userToBeInsertedTo, false, 0, null, null);
        }

        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        public static Uri addCall(CallerInfo ci, Context context, String number, String postDialDigits, String viaNumber, int presentation, int callType, int features, PhoneAccountHandle accountHandle, long start, int duration, Long dataUsage, boolean addForAllUsers, UserHandle userToBeInsertedTo, boolean isRead, int callBlockReason, CharSequence callScreeningAppName, String callScreeningComponentName) {
            String number2;
            String accountComponentString;
            String accountId;
            ContentValues values;
            List<UserInfo> users;
            CallerInfo callerInfo = ci;
            Context context2 = context;
            int i = callType;
            PhoneAccountHandle phoneAccountHandle = accountHandle;
            int i2 = duration;
            Long l = dataUsage;
            ContentResolver resolver = context.getContentResolver();
            String accountAddress = getLogAccountAddress(context2, phoneAccountHandle);
            String number3 = number;
            int numberPresentation = getLogNumberPresentation(number3, presentation);
            if (numberPresentation != 1) {
                number3 = "";
                if (callerInfo != null) {
                    callerInfo.name = "";
                }
                number2 = number3;
            } else {
                number2 = number3;
            }
            if (phoneAccountHandle != null) {
                accountComponentString = accountHandle.getComponentName().flattenToString();
                accountId = accountHandle.getId();
            } else {
                accountComponentString = null;
                accountId = null;
            }
            ContentValues values2 = new ContentValues(6);
            values2.put("number", number2);
            values2.put(POST_DIAL_DIGITS, postDialDigits);
            values2.put(VIA_NUMBER, viaNumber);
            values2.put(NUMBER_PRESENTATION, Integer.valueOf(numberPresentation));
            values2.put("type", Integer.valueOf(callType));
            values2.put(FEATURES, Integer.valueOf(features));
            values2.put("date", Long.valueOf(start));
            String number4 = number2;
            values2.put("duration", Long.valueOf((long) i2));
            if (l != null) {
                values2.put(DATA_USAGE, l);
            }
            values2.put("subscription_component_name", accountComponentString);
            values2.put("subscription_id", accountId);
            values2.put(PHONE_ACCOUNT_ADDRESS, accountAddress);
            values2.put("new", Integer.valueOf(1));
            if (!(callerInfo == null || callerInfo.name == null)) {
                values2.put("name", callerInfo.name);
            }
            values2.put(ADD_FOR_ALL_USERS, Integer.valueOf(addForAllUsers));
            if (i == 3) {
                values2.put("is_read", Integer.valueOf(isRead));
            }
            values2.put(BLOCK_REASON, Integer.valueOf(callBlockReason));
            values2.put(CALL_SCREENING_APP_NAME, charSequenceToString(callScreeningAppName));
            values2.put(CALL_SCREENING_COMPONENT_NAME, callScreeningComponentName);
            String normalizedPhoneNumber;
            String str;
            String str2;
            int i3;
            if (callerInfo != null) {
                ContentValues values3 = values2;
                if (callerInfo.contactIdOrZero > 0) {
                    int i4;
                    values2 = "_id";
                    String[] strArr;
                    if (callerInfo.normalizedNumber != null) {
                        String[] strArr2 = new String[]{String.valueOf(callerInfo.contactIdOrZero), callerInfo.normalizedNumber};
                        values = values3;
                        users = true;
                        strArr = strArr2;
                        values2 = resolver.query(Phone.CONTENT_URI, new String[]{values2}, "contact_id =? AND data4 =?", strArr, null);
                        normalizedPhoneNumber = accountAddress;
                        i4 = 0;
                    } else {
                        values = values3;
                        str = accountId;
                        str2 = accountComponentString;
                        i3 = numberPresentation;
                        users = true;
                        Uri withAppendedPath = Uri.withAppendedPath(Callable.CONTENT_FILTER_URI, Uri.encode(callerInfo.phoneNumber != null ? callerInfo.phoneNumber : number4));
                        String[] strArr3 = new String[]{values2};
                        strArr = new String[1];
                        i4 = 0;
                        strArr[0] = String.valueOf(callerInfo.contactIdOrZero);
                        values2 = resolver.query(withAppendedPath, strArr3, "contact_id =?", strArr, null);
                    }
                    if (values2 != null) {
                        try {
                            if (values2.getCount() > 0 && values2.moveToFirst()) {
                                accountId = values2.getString(i4);
                                updateDataUsageStatForData(resolver, accountId);
                                if (i2 >= 10000 && i == 2 && TextUtils.isEmpty(callerInfo.normalizedNumber)) {
                                    updateNormalizedNumber(context2, resolver, accountId, number4);
                                }
                            }
                            values2.close();
                        } catch (Throwable th) {
                            values2.close();
                        }
                    }
                } else {
                    values = values3;
                    str = accountId;
                    str2 = accountComponentString;
                    i3 = numberPresentation;
                    normalizedPhoneNumber = accountAddress;
                    users = true;
                }
            } else {
                values = values2;
                str = accountId;
                str2 = accountComponentString;
                i3 = numberPresentation;
                normalizedPhoneNumber = accountAddress;
                users = true;
            }
            Uri result = null;
            UserManager userManager = (UserManager) context2.getSystemService(UserManager.class);
            int currentUserId = userManager.getUserHandle();
            if (addForAllUsers) {
                Uri uriForSystem = addEntryAndRemoveExpiredEntries(context2, userManager, UserHandle.SYSTEM, values);
                if (uriForSystem != null) {
                    if (!CallLog.SHADOW_AUTHORITY.equals(uriForSystem.getAuthority())) {
                        if (currentUserId == 0) {
                            result = uriForSystem;
                        }
                        users = userManager.getUsers(users);
                        int count = users.size();
                        numberPresentation = 0;
                        while (numberPresentation < count) {
                            UserHandle userHandle = ((UserInfo) users.get(numberPresentation)).getUserHandle();
                            List<UserInfo> users2 = users;
                            users = userHandle.getIdentifier();
                            if (!userHandle.isSystem() && shouldHaveSharedCallLogEntries(context2, userManager, users) && userManager.isUserRunning(userHandle) && userManager.isUserUnlocked(userHandle)) {
                                Uri uri = addEntryAndRemoveExpiredEntries(context2, userManager, userHandle, values);
                                if (users == currentUserId) {
                                    result = uri;
                                }
                            }
                            numberPresentation++;
                            users = users2;
                        }
                    }
                }
                return null;
            }
            UserHandle targetUserHandle;
            if (userToBeInsertedTo != null) {
                targetUserHandle = userToBeInsertedTo;
            } else {
                targetUserHandle = UserHandle.of(currentUserId);
            }
            result = addEntryAndRemoveExpiredEntries(context2, userManager, targetUserHandle, values);
            return result;
        }

        private static String charSequenceToString(CharSequence sequence) {
            return sequence == null ? null : sequence.toString();
        }

        public static boolean shouldHaveSharedCallLogEntries(Context context, UserManager userManager, int userId) {
            boolean z = false;
            if (userManager.hasUserRestriction(UserManager.DISALLOW_OUTGOING_CALLS, UserHandle.of(userId))) {
                return false;
            }
            UserInfo userInfo = userManager.getUserInfo(userId);
            if (!(userInfo == null || userInfo.isManagedProfile())) {
                z = true;
            }
            return z;
        }

        public static String getLastOutgoingCall(Context context) {
            Cursor c = null;
            try {
                String string;
                c = context.getContentResolver().query(CONTENT_URI, new String[]{"number"}, "type = 2", null, "date DESC LIMIT 1");
                if (c != null) {
                    if (c.moveToFirst()) {
                        string = c.getString(0);
                        c.close();
                        return string;
                    }
                }
                string = "";
                return string;
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }

        private static Uri addEntryAndRemoveExpiredEntries(Context context, UserManager userManager, UserHandle user, ContentValues values) {
            String str = "subscription_component_name";
            String str2 = "subscription_id";
            ContentResolver resolver = context.getContentResolver();
            Uri uri = ContentProvider.maybeAddUserId(userManager.isUserUnlocked(user) ? CONTENT_URI : SHADOW_CONTENT_URI, user.getIdentifier());
            try {
                Uri result = resolver.insert(uri, values);
                if (!values.containsKey(str2) || TextUtils.isEmpty(values.getAsString(str2)) || !values.containsKey(str) || TextUtils.isEmpty(values.getAsString(str))) {
                    resolver.delete(uri, "_id IN (SELECT _id FROM calls ORDER BY date DESC LIMIT -1 OFFSET 500)", null);
                } else {
                    resolver.delete(uri, "_id IN (SELECT _id FROM calls WHERE subscription_component_name = ? AND subscription_id = ? ORDER BY date DESC LIMIT -1 OFFSET 500)", new String[]{values.getAsString(str), values.getAsString(str2)});
                }
                return result;
            } catch (IllegalArgumentException e) {
                Log.w(CallLog.LOG_TAG, "Failed to insert calllog", e);
                return null;
            }
        }

        private static void updateDataUsageStatForData(ContentResolver resolver, String dataId) {
            resolver.update(DataUsageFeedback.FEEDBACK_URI.buildUpon().appendPath(dataId).appendQueryParameter("type", "call").build(), new ContentValues(), null, null);
        }

        private static void updateNormalizedNumber(Context context, ContentResolver resolver, String dataId, String number) {
            if (!TextUtils.isEmpty(number) && !TextUtils.isEmpty(dataId)) {
                String countryIso = getCurrentCountryIso(context);
                if (!TextUtils.isEmpty(countryIso)) {
                    String normalizedNumber = PhoneNumberUtils.formatNumberToE164(number, countryIso);
                    if (!TextUtils.isEmpty(normalizedNumber)) {
                        ContentValues values = new ContentValues();
                        values.put("data4", normalizedNumber);
                        resolver.update(Data.CONTENT_URI, values, "_id=?", new String[]{dataId});
                    }
                }
            }
        }

        private static int getLogNumberPresentation(String number, int presentation) {
            if (presentation == 2 || presentation == 4) {
                return presentation;
            }
            if (TextUtils.isEmpty(number) || presentation == 3) {
                return 3;
            }
            return 1;
        }

        private static String getLogAccountAddress(Context context, PhoneAccountHandle accountHandle) {
            TelecomManager tm = null;
            try {
                tm = TelecomManager.from(context);
            } catch (UnsupportedOperationException e) {
            }
            if (tm == null || accountHandle == null) {
                return null;
            }
            PhoneAccount account = tm.getPhoneAccount(accountHandle);
            if (account == null) {
                return null;
            }
            Uri address = account.getSubscriptionAddress();
            if (address != null) {
                return address.getSchemeSpecificPart();
            }
            return null;
        }

        private static String getCurrentCountryIso(Context context) {
            CountryDetector detector = (CountryDetector) context.getSystemService(Context.COUNTRY_DETECTOR);
            if (detector == null) {
                return null;
            }
            Country country = detector.detectCountry();
            if (country != null) {
                return country.getCountryIso();
            }
            return null;
        }
    }
}
