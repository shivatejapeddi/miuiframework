package com.miui.enterprise;

import android.content.Context;
import android.content.Intent;
import android.util.Slog;
import com.miui.enterprise.settings.EnterpriseSettings;
import com.miui.enterprise.settings.EnterpriseSettings.Phone;
import miui.telephony.PhoneNumberUtils;

public class PhoneHelper {
    private static final String TAG = "Enterprise";

    private PhoneHelper() {
    }

    public static boolean checkEntDataRestriction(Context context, boolean origin) {
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED) {
            return origin;
        }
        int dateState = EnterpriseSettings.getInt(context, Phone.CELLULAR_STATUS, 1);
        if (dateState == 1) {
            return origin;
        }
        if (dateState == 0) {
            return false;
        }
        if (dateState == 4) {
            return true;
        }
        return origin;
    }

    public static boolean outgoingCallRestricted(Context context, Intent intent) {
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED) {
            return false;
        }
        String number = PhoneNumberUtils.getNumberFromIntent(intent, context);
        int i = EnterpriseSettings.getInt(context, Phone.PHONECALL_STATUS, 0) & 2;
        String str = TAG;
        if (i != 0) {
            Slog.d(str, "outgoing call is restricted");
            return true;
        } else if (!checkCallRestriction(context, number)) {
            return false;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("outgoing call is restricted for number ");
            stringBuilder.append(number);
            Slog.d(str, stringBuilder.toString());
            return true;
        }
    }

    public static boolean inCallRestricted(Context context, String number) {
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED) {
            return false;
        }
        int i = EnterpriseSettings.getInt(context, Phone.PHONECALL_STATUS, 0) & 1;
        String str = TAG;
        if (i != 0) {
            Slog.d(str, "in call is restricted");
            return true;
        } else if (!checkCallRestriction(context, number)) {
            return false;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("in call from number ");
            stringBuilder.append(number);
            stringBuilder.append(" is restricted");
            Slog.d(str, stringBuilder.toString());
            return true;
        }
    }

    private static boolean checkCallRestriction(Context context, String number) {
        number = PhoneNumberUtils.normalizeNumber(number);
        int restrictionMode = EnterpriseSettings.getInt(context, Phone.CALL_RESTRICTION_MODE, 0);
        if (restrictionMode == 1) {
            return 1 ^ EnterpriseSettings.parseListSettings(EnterpriseSettings.getString(context, Phone.CALL_WHITE_LIST)).contains(number);
        }
        if (restrictionMode != 2) {
            return false;
        }
        return EnterpriseSettings.parseListSettings(EnterpriseSettings.getString(context, Phone.CALL_BLACK_LIST)).contains(number);
    }

    public static boolean smsSendRestricted(Context context, String destAddr) {
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED) {
            return false;
        }
        int i = EnterpriseSettings.getInt(context, Phone.SMS_STATUS, 0) & 2;
        String str = TAG;
        if (i != 0) {
            Slog.d(str, "SMS send abort: enterprise restricted");
            return true;
        } else if (!checkSMSRestriction(context, destAddr)) {
            return false;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SMS send abort: address ");
            stringBuilder.append(destAddr);
            stringBuilder.append(" is restricted");
            Slog.d(str, stringBuilder.toString());
            return true;
        }
    }

    public static boolean smsReceiveRestricted(Context context, String address) {
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED) {
            return false;
        }
        int i = EnterpriseSettings.getInt(context, Phone.SMS_STATUS, 0) & 1;
        String str = TAG;
        if (i != 0) {
            Slog.d(str, "SMS receive blocked");
            return true;
        } else if (!checkSMSRestriction(context, address)) {
            return false;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SMS from ");
            stringBuilder.append(address);
            stringBuilder.append(" blocked");
            Slog.d(str, stringBuilder.toString());
            return true;
        }
    }

    private static boolean checkSMSRestriction(Context context, String number) {
        int restrictionMode = EnterpriseSettings.getInt(context, Phone.SMS_RESTRICTION_MODE, 0);
        if (restrictionMode == 1) {
            return 1 ^ EnterpriseSettings.parseListSettings(EnterpriseSettings.getString(context, Phone.SMS_WHITE_LIST)).contains(number);
        }
        if (restrictionMode != 2) {
            return false;
        }
        return EnterpriseSettings.parseListSettings(EnterpriseSettings.getString(context, Phone.SMS_BLACK_LIST)).contains(number);
    }
}
