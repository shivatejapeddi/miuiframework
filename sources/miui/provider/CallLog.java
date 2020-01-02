package miui.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.UserInfo;
import android.net.Uri;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Log;
import com.android.internal.telephony.CallerInfo;
import java.util.List;
import miui.provider.ExtraContacts.Calls;

public class CallLog {
    private static final String TAG = "CallLog";

    public static Uri addCall(Context context, Uri insertUri, ContentValues values, boolean addForAllUsers) {
        return addCall(context, insertUri, values, addForAllUsers, null);
    }

    public static Uri addCall(Context context, Uri insertUri, ContentValues values, boolean addForAllUsers, UserHandle userToBeInsertedTo) {
        Uri insertUri2;
        Context context2 = context;
        ContentValues contentValues = values;
        String format = String.format("addCall(): addForAllUsers=%s", new Object[]{Boolean.valueOf(addForAllUsers)});
        String str = TAG;
        Log.i(str, format);
        Uri result = null;
        UserManager userManager = (UserManager) context2.getSystemService("user");
        if (!StorageManager.isFileEncryptedNativeOrEmulated() || userManager.isUserUnlocked()) {
            insertUri2 = insertUri;
        } else {
            insertUri2 = Calls.SHADOW_CONTENT_URI;
        }
        int currentUserId = userManager.getUserHandle();
        if (addForAllUsers) {
            Uri uriForSystem = insertCall(context2, UserHandle.SYSTEM, insertUri2, contentValues);
            if (uriForSystem != null) {
                if (!android.provider.CallLog.SHADOW_AUTHORITY.equals(uriForSystem.getAuthority())) {
                    if (currentUserId == 0) {
                        result = uriForSystem;
                    }
                    List<UserInfo> users = userManager.getUsers(true);
                    int count = users.size();
                    for (int i = 0; i < count; i++) {
                        UserInfo userInfo = (UserInfo) users.get(i);
                        UserHandle userHandle = userInfo.getUserHandle();
                        int userId = userHandle.getIdentifier();
                        if (!userHandle.isSystem() && canInsertCalllog(userManager, userInfo)) {
                            Uri uri = insertCall(context2, userHandle, insertUri2, contentValues);
                            if (userId == currentUserId) {
                                result = uri;
                            }
                        }
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
        result = insertCall(context2, targetUserHandle, insertUri2, contentValues);
        Log.i(str, String.format("addCall(): result=%s", new Object[]{result}));
        return result;
    }

    private static Uri insertCall(Context context, UserHandle user, Uri insertUri, ContentValues values) {
        try {
            return context.getContentResolver().insert(ContentProvider.maybeAddUserId(insertUri, user.getIdentifier()), values);
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "Failed to insert calllog", e);
            return null;
        }
    }

    public static Uri addCall(CallerInfo ci, Context context, String number, int presentation, int callType, long start, int duration, int firewallType, int forwardedCall, long simInfoId, long phoneCallType, long feature, boolean addForAllUsers, PhoneAccountHandle accountHandle) {
        return addCall(ci, context, number, presentation, callType, start, duration, firewallType, forwardedCall, simInfoId, phoneCallType, feature, addForAllUsers, null, accountHandle, 0, null, null);
    }

    public static Uri addCall(CallerInfo ci, Context context, String number, int presentation, int callType, long start, int duration, int firewallType, int forwardedCall, long simInfoId, long phoneCallType, long feature, boolean addForAllUsers, UserHandle userToBeInsertedTo, PhoneAccountHandle accountHandle) {
        return addCall(ci, context, number, presentation, callType, start, duration, firewallType, forwardedCall, simInfoId, phoneCallType, feature, addForAllUsers, userToBeInsertedTo, accountHandle, 0, null, null);
    }

    public static Uri addCall(CallerInfo ci, Context context, String number, int presentation, int callType, long start, int duration, int firewallType, int forwardedCall, long simInfoId, long phoneCallType, long feature, boolean addForAllUsers, UserHandle userToBeInsertedTo, PhoneAccountHandle accountHandle, int callBlockReason, CharSequence callScreeningAppName, String callScreeningComponentName) {
        PhoneAccountHandle phoneAccountHandle = accountHandle;
        TelecomManager tm = null;
        try {
            tm = TelecomManager.from(context);
        } catch (UnsupportedOperationException e) {
        }
        String accountAddress = null;
        if (!(tm == null || phoneAccountHandle == null)) {
            PhoneAccount account = tm.getPhoneAccount(phoneAccountHandle);
            if (account != null) {
                Uri address = account.getSubscriptionAddress();
                if (address != null) {
                    accountAddress = address.getSchemeSpecificPart();
                }
            }
        }
        String accountComponentString = null;
        String accountId = null;
        if (phoneAccountHandle != null) {
            accountComponentString = accountHandle.getComponentName().flattenToString();
            accountId = accountHandle.getId();
        }
        return Calls.addCall(ci, context, number, presentation, callType, start, duration, firewallType, forwardedCall, simInfoId, phoneCallType, feature, addForAllUsers, userToBeInsertedTo, accountComponentString, accountId, accountAddress, callBlockReason, callScreeningAppName, callScreeningComponentName);
    }

    private static boolean canInsertCalllog(UserManager userManager, UserInfo user) {
        boolean canInsert = false;
        if (!(userManager == null || user == null)) {
            UserHandle userHandle = user.getUserHandle();
            boolean z = (999 == user.id || !userManager.isUserRunning(userHandle) || userManager.hasUserRestriction(UserManager.DISALLOW_OUTGOING_CALLS, userHandle) || user.isManagedProfile()) ? false : true;
            canInsert = z;
        }
        Log.i(TAG, String.format("canInsertCallLog(): user=%s, canInsert=%s", new Object[]{user, Boolean.valueOf(canInsert)}));
        return canInsert;
    }
}
