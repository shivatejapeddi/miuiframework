package android.telephony;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.BaseBundle;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SeempLog;
import com.android.internal.telephony.IIntegerConsumer;
import com.android.internal.telephony.IMms;
import com.android.internal.telephony.ISms;
import com.android.internal.telephony.ISms.Stub;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.SmsRawData;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public final class SmsManager {
    public static final int CDMA_SMS_RECORD_LENGTH = 255;
    public static final int CELL_BROADCAST_RAN_TYPE_CDMA = 1;
    public static final int CELL_BROADCAST_RAN_TYPE_GSM = 0;
    public static final String EXTRA_MMS_DATA = "android.telephony.extra.MMS_DATA";
    public static final String EXTRA_MMS_HTTP_STATUS = "android.telephony.extra.MMS_HTTP_STATUS";
    public static final String EXTRA_SIM_SUBSCRIPTION_ID = "android.telephony.extra.SIM_SUBSCRIPTION_ID";
    public static final String EXTRA_SMS_MESSAGE = "android.telephony.extra.SMS_MESSAGE";
    public static final String EXTRA_STATUS = "android.telephony.extra.STATUS";
    public static final String MESSAGE_STATUS_READ = "read";
    public static final String MESSAGE_STATUS_SEEN = "seen";
    public static final String MMS_CONFIG_ALIAS_ENABLED = "aliasEnabled";
    public static final String MMS_CONFIG_ALIAS_MAX_CHARS = "aliasMaxChars";
    public static final String MMS_CONFIG_ALIAS_MIN_CHARS = "aliasMinChars";
    public static final String MMS_CONFIG_ALLOW_ATTACH_AUDIO = "allowAttachAudio";
    public static final String MMS_CONFIG_APPEND_TRANSACTION_ID = "enabledTransID";
    public static final String MMS_CONFIG_CLOSE_CONNECTION = "mmsCloseConnection";
    public static final String MMS_CONFIG_EMAIL_GATEWAY_NUMBER = "emailGatewayNumber";
    public static final String MMS_CONFIG_GROUP_MMS_ENABLED = "enableGroupMms";
    public static final String MMS_CONFIG_HTTP_PARAMS = "httpParams";
    public static final String MMS_CONFIG_HTTP_SOCKET_TIMEOUT = "httpSocketTimeout";
    public static final String MMS_CONFIG_MAX_IMAGE_HEIGHT = "maxImageHeight";
    public static final String MMS_CONFIG_MAX_IMAGE_WIDTH = "maxImageWidth";
    public static final String MMS_CONFIG_MAX_MESSAGE_SIZE = "maxMessageSize";
    public static final String MMS_CONFIG_MESSAGE_TEXT_MAX_SIZE = "maxMessageTextSize";
    public static final String MMS_CONFIG_MMS_DELIVERY_REPORT_ENABLED = "enableMMSDeliveryReports";
    public static final String MMS_CONFIG_MMS_ENABLED = "enabledMMS";
    public static final String MMS_CONFIG_MMS_READ_REPORT_ENABLED = "enableMMSReadReports";
    public static final String MMS_CONFIG_MULTIPART_SMS_ENABLED = "enableMultipartSMS";
    public static final String MMS_CONFIG_NAI_SUFFIX = "naiSuffix";
    public static final String MMS_CONFIG_NOTIFY_WAP_MMSC_ENABLED = "enabledNotifyWapMMSC";
    public static final String MMS_CONFIG_RECIPIENT_LIMIT = "recipientLimit";
    public static final String MMS_CONFIG_SEND_MULTIPART_SMS_AS_SEPARATE_MESSAGES = "sendMultipartSmsAsSeparateMessages";
    public static final String MMS_CONFIG_SHOW_CELL_BROADCAST_APP_LINKS = "config_cellBroadcastAppLinks";
    public static final String MMS_CONFIG_SMS_DELIVERY_REPORT_ENABLED = "enableSMSDeliveryReports";
    public static final String MMS_CONFIG_SMS_TO_MMS_TEXT_LENGTH_THRESHOLD = "smsToMmsTextLengthThreshold";
    public static final String MMS_CONFIG_SMS_TO_MMS_TEXT_THRESHOLD = "smsToMmsTextThreshold";
    public static final String MMS_CONFIG_SUBJECT_MAX_LENGTH = "maxSubjectLength";
    public static final String MMS_CONFIG_SUPPORT_HTTP_CHARSET_HEADER = "supportHttpCharsetHeader";
    public static final String MMS_CONFIG_SUPPORT_MMS_CONTENT_DISPOSITION = "supportMmsContentDisposition";
    public static final String MMS_CONFIG_UA_PROF_TAG_NAME = "uaProfTagName";
    public static final String MMS_CONFIG_UA_PROF_URL = "uaProfUrl";
    public static final String MMS_CONFIG_USER_AGENT = "userAgent";
    public static final int MMS_ERROR_CONFIGURATION_ERROR = 7;
    public static final int MMS_ERROR_HTTP_FAILURE = 4;
    public static final int MMS_ERROR_INVALID_APN = 2;
    public static final int MMS_ERROR_IO_ERROR = 5;
    public static final int MMS_ERROR_NO_DATA_NETWORK = 8;
    public static final int MMS_ERROR_RETRY = 6;
    public static final int MMS_ERROR_UNABLE_CONNECT_MMS = 3;
    public static final int MMS_ERROR_UNSPECIFIED = 1;
    private static final String NO_DEFAULT_EXTRA = "noDefault";
    private static final String PHONE_PACKAGE_NAME = "com.android.phone";
    public static final String REGEX_PREFIX_DELIMITER = ",";
    @SystemApi
    public static final int RESULT_CANCELLED = 23;
    @SystemApi
    public static final int RESULT_ENCODING_ERROR = 18;
    @SystemApi
    public static final int RESULT_ERROR_FDN_CHECK_FAILURE = 6;
    public static final int RESULT_ERROR_GENERIC_FAILURE = 1;
    public static final int RESULT_ERROR_LIMIT_EXCEEDED = 5;
    @SystemApi
    public static final int RESULT_ERROR_NONE = 0;
    public static final int RESULT_ERROR_NO_SERVICE = 4;
    public static final int RESULT_ERROR_NULL_PDU = 3;
    public static final int RESULT_ERROR_RADIO_OFF = 2;
    public static final int RESULT_ERROR_SHORT_CODE_NEVER_ALLOWED = 8;
    public static final int RESULT_ERROR_SHORT_CODE_NOT_ALLOWED = 7;
    @SystemApi
    public static final int RESULT_INTERNAL_ERROR = 21;
    @SystemApi
    public static final int RESULT_INVALID_ARGUMENTS = 11;
    @SystemApi
    public static final int RESULT_INVALID_SMSC_ADDRESS = 19;
    @SystemApi
    public static final int RESULT_INVALID_SMS_FORMAT = 14;
    @SystemApi
    public static final int RESULT_INVALID_STATE = 12;
    @SystemApi
    public static final int RESULT_MODEM_ERROR = 16;
    @SystemApi
    public static final int RESULT_NETWORK_ERROR = 17;
    @SystemApi
    public static final int RESULT_NETWORK_REJECT = 10;
    @SystemApi
    public static final int RESULT_NO_MEMORY = 13;
    @SystemApi
    public static final int RESULT_NO_RESOURCES = 22;
    @SystemApi
    public static final int RESULT_OPERATION_NOT_ALLOWED = 20;
    @SystemApi
    public static final int RESULT_RADIO_NOT_AVAILABLE = 9;
    @SystemApi
    public static final int RESULT_REQUEST_NOT_SUPPORTED = 24;
    public static final int RESULT_STATUS_SUCCESS = 0;
    public static final int RESULT_STATUS_TIMEOUT = 1;
    @SystemApi
    public static final int RESULT_SYSTEM_ERROR = 15;
    public static final int SMS_CATEGORY_FREE_SHORT_CODE = 1;
    public static final int SMS_CATEGORY_NOT_SHORT_CODE = 0;
    public static final int SMS_CATEGORY_POSSIBLE_PREMIUM_SHORT_CODE = 3;
    public static final int SMS_CATEGORY_PREMIUM_SHORT_CODE = 4;
    public static final int SMS_CATEGORY_STANDARD_SHORT_CODE = 2;
    public static final int SMS_MESSAGE_PERIOD_NOT_SPECIFIED = -1;
    public static final int SMS_MESSAGE_PRIORITY_NOT_SPECIFIED = -1;
    public static final int SMS_RECORD_LENGTH = 176;
    public static final int SMS_TYPE_INCOMING = 0;
    public static final int SMS_TYPE_OUTGOING = 1;
    public static final int STATUS_ON_ICC_FREE = 0;
    public static final int STATUS_ON_ICC_READ = 1;
    public static final int STATUS_ON_ICC_SENT = 5;
    public static final int STATUS_ON_ICC_UNREAD = 3;
    public static final int STATUS_ON_ICC_UNSENT = 7;
    private static final String TAG = "SmsManager";
    private static final SmsManager sInstance = new SmsManager(Integer.MAX_VALUE);
    private static final Object sLockObject = new Object();
    private static final Map<Integer, SmsManager> sSubInstances = new ArrayMap();
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mSubId;

    private interface SubscriptionResolverResult {
        void onFailure();

        void onSuccess(int i);
    }

    public static abstract class FinancialSmsCallback {
        public abstract void onFinancialSmsMessages(CursorWindow cursorWindow);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SmsShortCodeCategory {
    }

    public void sendTextMessage(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        SeempLog.record_str(75, destinationAddress);
        sendTextMessageInternal(destinationAddress, scAddress, text, sentIntent, deliveryIntent, true, ActivityThread.currentPackageName());
    }

    private void sendTextMessageInternal(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessage, String packageName) {
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        } else if (TextUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Invalid message body");
        } else {
            Context context = ActivityThread.currentApplication().getApplicationContext();
            if (persistMessage) {
                final String str = packageName;
                final String str2 = destinationAddress;
                final String str3 = scAddress;
                final String str4 = text;
                final PendingIntent pendingIntent = sentIntent;
                final PendingIntent pendingIntent2 = deliveryIntent;
                final boolean z = persistMessage;
                final Context context2 = context;
                AnonymousClass1 anonymousClass1 = new SubscriptionResolverResult() {
                    public void onSuccess(int subId) {
                        try {
                            SmsManager.getISmsServiceOrThrow().sendTextForSubscriber(subId, str, str2, str3, str4, pendingIntent, pendingIntent2, z);
                        } catch (RemoteException e) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("sendTextMessageInternal: Couldn't send SMS, exception - ");
                            stringBuilder.append(e.getMessage());
                            Log.e(SmsManager.TAG, stringBuilder.toString());
                            SmsManager.notifySmsGenericError(pendingIntent);
                        }
                    }

                    public void onFailure() {
                        SmsManager.notifySmsErrorNoDefaultSet(context2, pendingIntent);
                    }
                };
                resolveSubscriptionForOperation(anonymousClass1);
                return;
            }
            try {
                getISmsServiceOrThrow().sendTextForSubscriber(getSubscriptionId(), packageName, destinationAddress, scAddress, text, sentIntent, deliveryIntent, persistMessage);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("sendTextMessageInternal (no persist): Couldn't send SMS, exception - ");
                stringBuilder.append(e.getMessage());
                Log.e(TAG, stringBuilder.toString());
                notifySmsGenericError(sentIntent);
            }
        }
    }

    public void sendTextMessageWithoutPersisting(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        sendTextMessageInternal(destinationAddress, scAddress, text, sentIntent, deliveryIntent, false, ActivityThread.currentPackageName());
    }

    public void sendTextMessageWithSelfPermissions(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessage) {
        String str = destinationAddress;
        SeempLog.record_str(75, destinationAddress);
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        } else if (TextUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Invalid message body");
        } else {
            try {
                getISmsServiceOrThrow().sendTextForSubscriberWithSelfPermissions(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, text, sentIntent, deliveryIntent, persistMessage);
            } catch (RemoteException e) {
                notifySmsGenericError(sentIntent);
            }
        }
    }

    @UnsupportedAppUsage
    public void sendTextMessage(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, int priority, boolean expectMore, int validityPeriod) {
        sendTextMessageInternal(destinationAddress, scAddress, text, sentIntent, deliveryIntent, true, priority, expectMore, validityPeriod);
    }

    private void sendTextMessageInternal(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        int i = priority;
        int i2 = validityPeriod;
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        } else if (TextUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Invalid message body");
        } else {
            int priority2;
            int validityPeriod2;
            if (i < 0 || i > 3) {
                priority2 = -1;
            } else {
                priority2 = i;
            }
            if (i2 < 5 || i2 > 635040) {
                validityPeriod2 = -1;
            } else {
                validityPeriod2 = i2;
            }
            final int finalPriority = priority2;
            final int finalValidity = validityPeriod2;
            Context context = ActivityThread.currentApplication().getApplicationContext();
            if (persistMessage) {
                final String str = destinationAddress;
                final String str2 = scAddress;
                final String str3 = text;
                final PendingIntent pendingIntent = sentIntent;
                final PendingIntent pendingIntent2 = deliveryIntent;
                final boolean z = persistMessage;
                final boolean z2 = expectMore;
                final Context context2 = context;
                resolveSubscriptionForOperation(new SubscriptionResolverResult() {
                    public void onSuccess(int subId) {
                        try {
                            ISms iSms = SmsManager.getISmsServiceOrThrow();
                            if (iSms != null) {
                                iSms.sendTextForSubscriberWithOptions(subId, ActivityThread.currentPackageName(), str, str2, str3, pendingIntent, pendingIntent2, z, finalPriority, z2, finalValidity);
                            }
                        } catch (RemoteException e) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("sendTextMessageInternal: Couldn't send SMS, exception - ");
                            stringBuilder.append(e.getMessage());
                            Log.e(SmsManager.TAG, stringBuilder.toString());
                            SmsManager.notifySmsGenericError(pendingIntent);
                        }
                    }

                    public void onFailure() {
                        SmsManager.notifySmsErrorNoDefaultSet(context2, pendingIntent);
                    }
                });
                return;
            }
            try {
                ISms iSms = getISmsServiceOrThrow();
                if (iSms != null) {
                    iSms.sendTextForSubscriberWithOptions(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, text, sentIntent, deliveryIntent, persistMessage, finalPriority, expectMore, finalValidity);
                }
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("sendTextMessageInternal(no persist): Couldn't send SMS, exception - ");
                stringBuilder.append(e.getMessage());
                Log.e(TAG, stringBuilder.toString());
                notifySmsGenericError(sentIntent);
            }
        }
    }

    @UnsupportedAppUsage
    public void sendTextMessageWithoutPersisting(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, int priority, boolean expectMore, int validityPeriod) {
        sendTextMessageInternal(destinationAddress, scAddress, text, sentIntent, deliveryIntent, false, priority, expectMore, validityPeriod);
    }

    public void injectSmsPdu(byte[] pdu, String format, PendingIntent receivedIntent) {
        if (format.equals("3gpp") || format.equals("3gpp2")) {
            try {
                ISms iSms = Stub.asInterface(ServiceManager.getService("isms"));
                if (iSms != null) {
                    iSms.injectSmsPduForSubscriber(getSubscriptionId(), pdu, format, receivedIntent);
                    return;
                }
                return;
            } catch (RemoteException e) {
                if (receivedIntent != null) {
                    try {
                        receivedIntent.send(2);
                        return;
                    } catch (CanceledException e2) {
                        return;
                    }
                }
                return;
            }
        }
        throw new IllegalArgumentException("Invalid pdu format. format must be either 3gpp or 3gpp2");
    }

    public ArrayList<String> divideMessage(String text) {
        if (text != null) {
            return SmsMessage.fragmentText(text, getSubscriptionId());
        }
        throw new IllegalArgumentException("text is null");
    }

    public void sendMultipartTextMessage(String destinationAddress, String scAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents) {
        sendMultipartTextMessageInternal(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, true, ActivityThread.currentPackageName());
    }

    public void sendMultipartTextMessageExternal(String destinationAddress, String scAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, String packageName) {
        sendMultipartTextMessageInternal(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, true, ActivityThread.currentPackageName() == null ? packageName : ActivityThread.currentPackageName());
    }

    private void sendMultipartTextMessageInternal(String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessage, String packageName) {
        List<String> list = parts;
        List<PendingIntent> list2 = sentIntents;
        List<PendingIntent> list3 = deliveryIntents;
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        } else if (list == null || parts.size() < 1) {
            throw new IllegalArgumentException("Invalid message body");
        } else if (parts.size() > 1) {
            Context context = ActivityThread.currentApplication().getApplicationContext();
            if (persistMessage) {
                final String str = packageName;
                final String str2 = destinationAddress;
                final String str3 = scAddress;
                final List<String> list4 = parts;
                final List<PendingIntent> list5 = sentIntents;
                final List<PendingIntent> list6 = deliveryIntents;
                final boolean z = persistMessage;
                final Context context2 = context;
                AnonymousClass3 anonymousClass3 = new SubscriptionResolverResult() {
                    public void onSuccess(int subId) {
                        try {
                            SmsManager.getISmsServiceOrThrow().sendMultipartTextForSubscriber(subId, str, str2, str3, list4, list5, list6, z);
                        } catch (RemoteException e) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("sendMultipartTextMessageInternal: Couldn't send SMS - ");
                            stringBuilder.append(e.getMessage());
                            Log.e(SmsManager.TAG, stringBuilder.toString());
                            SmsManager.notifySmsGenericError(list5);
                        }
                    }

                    public void onFailure() {
                        SmsManager.notifySmsErrorNoDefaultSet(context2, list5);
                    }
                };
                resolveSubscriptionForOperation(anonymousClass3);
                return;
            }
            try {
                ISms iSms = getISmsServiceOrThrow();
                if (iSms != null) {
                    iSms.sendMultipartTextForSubscriber(getSubscriptionId(), packageName, destinationAddress, scAddress, parts, sentIntents, deliveryIntents, persistMessage);
                }
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("sendMultipartTextMessageInternal: Couldn't send SMS - ");
                stringBuilder.append(e.getMessage());
                Log.e(TAG, stringBuilder.toString());
                notifySmsGenericError((List) sentIntents);
            }
        } else {
            PendingIntent deliveryIntent;
            PendingIntent sentIntent = null;
            if (list2 != null && sentIntents.size() > 0) {
                sentIntent = (PendingIntent) list2.get(0);
            }
            if (list3 == null || deliveryIntents.size() <= 0) {
                deliveryIntent = null;
            } else {
                deliveryIntent = (PendingIntent) list3.get(0);
            }
            sendTextMessageInternal(destinationAddress, scAddress, (String) list.get(0), sentIntent, deliveryIntent, true, packageName);
        }
    }

    @SystemApi
    public void sendMultipartTextMessageWithoutPersisting(String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents) {
        sendMultipartTextMessageInternal(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, false, ActivityThread.currentPackageName());
    }

    @UnsupportedAppUsage
    public void sendMultipartTextMessage(String destinationAddress, String scAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, int priority, boolean expectMore, int validityPeriod) {
        sendMultipartTextMessageInternal(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, true, priority, expectMore, validityPeriod);
    }

    private void sendMultipartTextMessageInternal(String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        RemoteException e;
        List<String> list = parts;
        List<PendingIntent> list2 = sentIntents;
        List<PendingIntent> list3 = deliveryIntents;
        int i = priority;
        int i2 = validityPeriod;
        List<String> list4;
        if (TextUtils.isEmpty(destinationAddress)) {
            list4 = list;
            throw new IllegalArgumentException("Invalid destinationAddress");
        } else if (list == null || parts.size() < 1) {
            list4 = list;
            throw new IllegalArgumentException("Invalid message body");
        } else {
            int priority2;
            int validityPeriod2;
            if (i < 0 || i > 3) {
                priority2 = -1;
            } else {
                priority2 = i;
            }
            if (i2 < 5 || i2 > 635040) {
                validityPeriod2 = -1;
            } else {
                validityPeriod2 = i2;
            }
            List<PendingIntent> list5;
            List<PendingIntent> list6;
            if (parts.size() > 1) {
                final int finalPriority = priority2;
                final int finalValidity = validityPeriod2;
                Context context = ActivityThread.currentApplication().getApplicationContext();
                if (persistMessage) {
                    final String str = destinationAddress;
                    final String str2 = scAddress;
                    final List<String> list7 = parts;
                    final List<PendingIntent> list8 = sentIntents;
                    final List<PendingIntent> list9 = deliveryIntents;
                    final boolean z = persistMessage;
                    final boolean z2 = expectMore;
                    final Context context2 = context;
                    resolveSubscriptionForOperation(new SubscriptionResolverResult() {
                        public void onSuccess(int subId) {
                            try {
                                ISms iSms = SmsManager.getISmsServiceOrThrow();
                                if (iSms != null) {
                                    iSms.sendMultipartTextForSubscriberWithOptions(subId, ActivityThread.currentPackageName(), str, str2, list7, list8, list9, z, finalPriority, z2, finalValidity);
                                }
                            } catch (RemoteException e) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("sendMultipartTextMessageInternal: Couldn't send SMS - ");
                                stringBuilder.append(e.getMessage());
                                Log.e(SmsManager.TAG, stringBuilder.toString());
                                SmsManager.notifySmsGenericError(list8);
                            }
                        }

                        public void onFailure() {
                            SmsManager.notifySmsErrorNoDefaultSet(context2, list8);
                        }
                    });
                    list5 = list3;
                    list6 = list2;
                    list4 = list;
                    return;
                }
                try {
                    ISms iSms = getISmsServiceOrThrow();
                    if (iSms != null) {
                        list5 = list3;
                        list6 = list2;
                        list4 = list;
                        try {
                            iSms.sendMultipartTextForSubscriberWithOptions(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, parts, sentIntents, deliveryIntents, persistMessage, finalPriority, expectMore, finalValidity);
                            return;
                        } catch (RemoteException e2) {
                            e = e2;
                        }
                    } else {
                        list5 = list3;
                        list6 = list2;
                        list4 = list;
                        return;
                    }
                } catch (RemoteException e3) {
                    e = e3;
                    list5 = list3;
                    list6 = list2;
                    list4 = list;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("sendMultipartTextMessageInternal (no persist): Couldn't send SMS - ");
                    stringBuilder.append(e.getMessage());
                    Log.e(TAG, stringBuilder.toString());
                    notifySmsGenericError((List) sentIntents);
                    return;
                }
            }
            PendingIntent deliveryIntent;
            list5 = list3;
            list6 = list2;
            list4 = list;
            PendingIntent sentIntent = null;
            if (list6 != null && sentIntents.size() > 0) {
                sentIntent = (PendingIntent) list6.get(0);
            }
            if (list5 == null || deliveryIntents.size() <= 0) {
                deliveryIntent = null;
            } else {
                deliveryIntent = (PendingIntent) list5.get(0);
            }
            sendTextMessageInternal(destinationAddress, scAddress, (String) list4.get(0), sentIntent, deliveryIntent, persistMessage, priority2, expectMore, validityPeriod2);
        }
    }

    public void sendMultipartTextMessageWithoutPersisting(String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, int priority, boolean expectMore, int validityPeriod) {
        sendMultipartTextMessageInternal(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, false, priority, expectMore, validityPeriod);
    }

    public void sendDataMessage(String destinationAddress, String scAddress, short destinationPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        byte[] bArr = data;
        String str = destinationAddress;
        SeempLog.record_str(73, destinationAddress);
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        } else if (bArr == null || bArr.length == 0) {
            throw new IllegalArgumentException("Invalid message data");
        } else {
            final String str2 = destinationAddress;
            final String str3 = scAddress;
            final short s = destinationPort;
            final byte[] bArr2 = data;
            final PendingIntent pendingIntent = sentIntent;
            final PendingIntent pendingIntent2 = deliveryIntent;
            final Context applicationContext = ActivityThread.currentApplication().getApplicationContext();
            AnonymousClass5 anonymousClass5 = new SubscriptionResolverResult() {
                public void onSuccess(int subId) {
                    try {
                        SmsManager.getISmsServiceOrThrow().sendDataForSubscriber(subId, ActivityThread.currentPackageName(), str2, str3, 65535 & s, bArr2, pendingIntent, pendingIntent2);
                    } catch (RemoteException e) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("sendDataMessage: Couldn't send SMS - Exception: ");
                        stringBuilder.append(e.getMessage());
                        Log.e(SmsManager.TAG, stringBuilder.toString());
                        SmsManager.notifySmsGenericError(pendingIntent);
                    }
                }

                public void onFailure() {
                    SmsManager.notifySmsErrorNoDefaultSet(applicationContext, pendingIntent);
                }
            };
            resolveSubscriptionForOperation(anonymousClass5);
        }
    }

    public void sendDataMessageWithSelfPermissions(String destinationAddress, String scAddress, short destinationPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        byte[] bArr = data;
        String str = destinationAddress;
        SeempLog.record_str(73, destinationAddress);
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        } else if (bArr == null || bArr.length == 0) {
            throw new IllegalArgumentException("Invalid message data");
        } else {
            try {
                getISmsServiceOrThrow().sendDataForSubscriberWithSelfPermissions(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, destinationPort & 65535, data, sentIntent, deliveryIntent);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("sendDataMessageWithSelfPermissions: Couldn't send SMS - Exception: ");
                stringBuilder.append(e.getMessage());
                Log.e(TAG, stringBuilder.toString());
                notifySmsGenericError(sentIntent);
            }
        }
    }

    public static SmsManager getDefault() {
        return sInstance;
    }

    public static SmsManager getSmsManagerForSubscriptionId(int subId) {
        SmsManager smsManager;
        synchronized (sLockObject) {
            smsManager = (SmsManager) sSubInstances.get(Integer.valueOf(subId));
            if (smsManager == null) {
                smsManager = new SmsManager(subId);
                sSubInstances.put(Integer.valueOf(subId), smsManager);
            }
        }
        return smsManager;
    }

    private SmsManager(int subId) {
        this.mSubId = subId;
    }

    public int getSubscriptionId() {
        try {
            return this.mSubId == Integer.MAX_VALUE ? getISmsServiceOrThrow().getPreferredSmsSubscription() : this.mSubId;
        } catch (RemoteException e) {
            return -1;
        }
    }

    private void resolveSubscriptionForOperation(final SubscriptionResolverResult resolverResult) {
        String str = TAG;
        int subId = getSubscriptionId();
        Context context = ActivityThread.currentApplication().getApplicationContext();
        try {
            getISmsService();
        } catch (RemoteException ex) {
            Log.e(str, "resolveSubscriptionForOperation", ex);
        }
        if (false) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("resolveSubscriptionForOperation isSmsSimPickActivityNeeded is true for package ");
            stringBuilder.append(context.getPackageName());
            Log.d(str, stringBuilder.toString());
            try {
                getITelephony().enqueueSmsPickResult(context.getOpPackageName(), new IIntegerConsumer.Stub() {
                    public void accept(int subId) {
                        SmsManager.this.sendResolverResult(resolverResult, subId, true);
                    }
                });
            } catch (RemoteException ex2) {
                Log.e(str, "Unable to launch activity", ex2);
                sendResolverResult(resolverResult, subId, true);
            }
            return;
        }
        sendResolverResult(resolverResult, subId, false);
    }

    private void sendResolverResult(SubscriptionResolverResult resolverResult, int subId, boolean pickActivityShown) {
        if (SubscriptionManager.isValidSubscriptionId(subId)) {
            resolverResult.onSuccess(subId);
            return;
        }
        if (getTargetSdkVersion() > 28 || pickActivityShown) {
            resolverResult.onFailure();
        } else {
            resolverResult.onSuccess(subId);
        }
    }

    private static int getTargetSdkVersion() {
        Context context = ActivityThread.currentApplication().getApplicationContext();
        try {
            return context.getPackageManager().getApplicationInfo(context.getOpPackageName(), 0).targetSdkVersion;
        } catch (NameNotFoundException e) {
            return -1;
        }
    }

    private static ITelephony getITelephony() {
        ITelephony binder = ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
        if (binder != null) {
            return binder;
        }
        throw new RuntimeException("Could not find Telephony Service.");
    }

    private static void notifySmsErrorNoDefaultSet(Context context, PendingIntent pendingIntent) {
        if (pendingIntent != null) {
            Intent errorMessage = new Intent();
            errorMessage.putExtra(NO_DEFAULT_EXTRA, true);
            try {
                pendingIntent.send(context, 1, errorMessage);
            } catch (CanceledException e) {
            }
        }
    }

    private static void notifySmsErrorNoDefaultSet(Context context, List<PendingIntent> pendingIntents) {
        if (pendingIntents != null) {
            for (PendingIntent pendingIntent : pendingIntents) {
                Intent errorMessage = new Intent();
                errorMessage.putExtra(NO_DEFAULT_EXTRA, true);
                try {
                    pendingIntent.send(context, 1, errorMessage);
                } catch (CanceledException e) {
                }
            }
        }
    }

    private static void notifySmsGenericError(PendingIntent pendingIntent) {
        if (pendingIntent != null) {
            try {
                pendingIntent.send(1);
            } catch (CanceledException e) {
            }
        }
    }

    private static void notifySmsGenericError(List<PendingIntent> pendingIntents) {
        if (pendingIntents != null) {
            for (PendingIntent pendingIntent : pendingIntents) {
                try {
                    pendingIntent.send(1);
                } catch (CanceledException e) {
                }
            }
        }
    }

    private static ISms getISmsServiceOrThrow() {
        ISms iSms = getISmsService();
        if (iSms != null) {
            return iSms;
        }
        throw new UnsupportedOperationException("Sms is not supported");
    }

    private static ISms getISmsService() {
        return Stub.asInterface(ServiceManager.getService("isms"));
    }

    @UnsupportedAppUsage
    public boolean copyMessageToIcc(byte[] smsc, byte[] pdu, int status) {
        SeempLog.record(79);
        if (pdu != null) {
            try {
                ISms iSms = getISmsService();
                if (iSms == null) {
                    return false;
                }
                return iSms.copyMessageToIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), status, pdu, smsc);
            } catch (RemoteException e) {
                return false;
            }
        }
        throw new IllegalArgumentException("pdu is NULL");
    }

    @UnsupportedAppUsage
    public boolean deleteMessageFromIcc(int messageIndex) {
        SeempLog.record(80);
        byte[] pdu = new byte[175];
        Arrays.fill(pdu, (byte) -1);
        try {
            ISms iSms = getISmsService();
            if (iSms == null) {
                return false;
            }
            return iSms.updateMessageOnIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), messageIndex, 0, pdu);
        } catch (RemoteException e) {
            return false;
        }
    }

    @UnsupportedAppUsage
    public boolean updateMessageOnIcc(int messageIndex, int newStatus, byte[] pdu) {
        SeempLog.record(81);
        try {
            ISms iSms = getISmsService();
            if (iSms == null) {
                return false;
            }
            return iSms.updateMessageOnIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), messageIndex, newStatus, pdu);
        } catch (RemoteException e) {
            return false;
        }
    }

    @UnsupportedAppUsage
    public ArrayList<SmsMessage> getAllMessagesFromIcc() {
        List<SmsRawData> records = null;
        try {
            ISms iSms = getISmsService();
            if (iSms != null) {
                records = iSms.getAllMessagesFromIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName());
            }
        } catch (RemoteException e) {
        }
        return createMessageListFromRawRecords(records);
    }

    public boolean enableCellBroadcast(int messageIdentifier, int ranType) {
        try {
            ISms iSms = getISmsService();
            if (iSms != null) {
                return iSms.enableCellBroadcastForSubscriber(getSubscriptionId(), messageIdentifier, ranType);
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean disableCellBroadcast(int messageIdentifier, int ranType) {
        try {
            ISms iSms = getISmsService();
            if (iSms != null) {
                return iSms.disableCellBroadcastForSubscriber(getSubscriptionId(), messageIdentifier, ranType);
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    @UnsupportedAppUsage
    public boolean enableCellBroadcastRange(int startMessageId, int endMessageId, int ranType) {
        if (endMessageId >= startMessageId) {
            try {
                ISms iSms = getISmsService();
                if (iSms != null) {
                    return iSms.enableCellBroadcastRangeForSubscriber(getSubscriptionId(), startMessageId, endMessageId, ranType);
                }
                return false;
            } catch (RemoteException e) {
                return false;
            }
        }
        throw new IllegalArgumentException("endMessageId < startMessageId");
    }

    @UnsupportedAppUsage
    public boolean disableCellBroadcastRange(int startMessageId, int endMessageId, int ranType) {
        if (endMessageId >= startMessageId) {
            try {
                ISms iSms = getISmsService();
                if (iSms != null) {
                    return iSms.disableCellBroadcastRangeForSubscriber(getSubscriptionId(), startMessageId, endMessageId, ranType);
                }
                return false;
            } catch (RemoteException e) {
                return false;
            }
        }
        throw new IllegalArgumentException("endMessageId < startMessageId");
    }

    private ArrayList<SmsMessage> createMessageListFromRawRecords(List<SmsRawData> records) {
        ArrayList<SmsMessage> messages = new ArrayList();
        if (records != null) {
            int count = records.size();
            for (int i = 0; i < count; i++) {
                SmsRawData data = (SmsRawData) records.get(i);
                if (data != null) {
                    SmsMessage sms = SmsMessage.createFromEfRecord(i + 1, data.getBytes(), getSubscriptionId());
                    if (sms != null) {
                        messages.add(sms);
                    }
                }
            }
        }
        return messages;
    }

    public boolean isImsSmsSupported() {
        try {
            ISms iSms = getISmsService();
            if (iSms != null) {
                return iSms.isImsSmsSupportedForSubscriber(getSubscriptionId());
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public String getImsSmsFormat() {
        String format = "unknown";
        try {
            ISms iSms = getISmsService();
            if (iSms != null) {
                return iSms.getImsSmsFormatForSubscriber(getSubscriptionId());
            }
            return format;
        } catch (RemoteException e) {
            return format;
        }
    }

    public static int getDefaultSmsSubscriptionId() {
        int i = -1;
        try {
            i = getISmsService().getPreferredSmsSubscription();
            return i;
        } catch (RemoteException e) {
            return i;
        } catch (NullPointerException e2) {
            return i;
        }
    }

    @UnsupportedAppUsage
    public boolean isSMSPromptEnabled() {
        boolean z = false;
        try {
            z = Stub.asInterface(ServiceManager.getService("isms")).isSMSPromptEnabled();
            return z;
        } catch (RemoteException e) {
            return z;
        } catch (NullPointerException e2) {
            return z;
        }
    }

    public int getSmsCapacityOnIcc() {
        try {
            ISms iccISms = getISmsService();
            if (iccISms != null) {
                return iccISms.getSmsCapacityOnIccForSubscriber(getSubscriptionId());
            }
            return -1;
        } catch (RemoteException e) {
            return -1;
        }
    }

    public void sendMultimediaMessage(Context context, Uri contentUri, String locationUrl, Bundle configOverrides, PendingIntent sentIntent) {
        if (contentUri != null) {
            try {
                IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
                if (iMms != null) {
                    iMms.sendMessage(getSubscriptionId(), ActivityThread.currentPackageName(), contentUri, locationUrl, configOverrides, sentIntent);
                }
            } catch (RemoteException e) {
            }
        } else {
            throw new IllegalArgumentException("Uri contentUri null");
        }
    }

    public void downloadMultimediaMessage(Context context, String locationUrl, Uri contentUri, Bundle configOverrides, PendingIntent downloadedIntent) {
        if (TextUtils.isEmpty(locationUrl)) {
            throw new IllegalArgumentException("Empty MMS location URL");
        } else if (contentUri != null) {
            try {
                IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
                if (iMms != null) {
                    iMms.downloadMessage(getSubscriptionId(), ActivityThread.currentPackageName(), locationUrl, contentUri, configOverrides, downloadedIntent);
                }
            } catch (RemoteException e) {
            }
        } else {
            throw new IllegalArgumentException("Uri contentUri null");
        }
    }

    public Uri importTextMessage(String address, int type, String text, long timestampMillis, boolean seen, boolean read) {
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.importTextMessage(ActivityThread.currentPackageName(), address, type, text, timestampMillis, seen, read);
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    public Uri importMultimediaMessage(Uri contentUri, String messageId, long timestampSecs, boolean seen, boolean read) {
        if (contentUri != null) {
            try {
                IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
                if (iMms != null) {
                    return iMms.importMultimediaMessage(ActivityThread.currentPackageName(), contentUri, messageId, timestampSecs, seen, read);
                }
            } catch (RemoteException e) {
            }
            return null;
        }
        throw new IllegalArgumentException("Uri contentUri null");
    }

    public boolean deleteStoredMessage(Uri messageUri) {
        if (messageUri != null) {
            try {
                IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
                if (iMms != null) {
                    return iMms.deleteStoredMessage(ActivityThread.currentPackageName(), messageUri);
                }
            } catch (RemoteException e) {
            }
            return false;
        }
        throw new IllegalArgumentException("Empty message URI");
    }

    public boolean deleteStoredConversation(long conversationId) {
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.deleteStoredConversation(ActivityThread.currentPackageName(), conversationId);
            }
        } catch (RemoteException e) {
        }
        return false;
    }

    public boolean updateStoredMessageStatus(Uri messageUri, ContentValues statusValues) {
        if (messageUri != null) {
            try {
                IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
                if (iMms != null) {
                    return iMms.updateStoredMessageStatus(ActivityThread.currentPackageName(), messageUri, statusValues);
                }
            } catch (RemoteException e) {
            }
            return false;
        }
        throw new IllegalArgumentException("Empty message URI");
    }

    public boolean archiveStoredConversation(long conversationId, boolean archived) {
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.archiveStoredConversation(ActivityThread.currentPackageName(), conversationId, archived);
            }
        } catch (RemoteException e) {
        }
        return false;
    }

    public Uri addTextMessageDraft(String address, String text) {
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.addTextMessageDraft(ActivityThread.currentPackageName(), address, text);
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    public Uri addMultimediaMessageDraft(Uri contentUri) {
        if (contentUri != null) {
            try {
                IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
                if (iMms != null) {
                    return iMms.addMultimediaMessageDraft(ActivityThread.currentPackageName(), contentUri);
                }
            } catch (RemoteException e) {
            }
            return null;
        }
        throw new IllegalArgumentException("Uri contentUri null");
    }

    public void sendStoredTextMessage(Uri messageUri, String scAddress, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        if (messageUri != null) {
            final Uri uri = messageUri;
            final String str = scAddress;
            final PendingIntent pendingIntent = sentIntent;
            final PendingIntent pendingIntent2 = deliveryIntent;
            final Context applicationContext = ActivityThread.currentApplication().getApplicationContext();
            resolveSubscriptionForOperation(new SubscriptionResolverResult() {
                public void onSuccess(int subId) {
                    try {
                        SmsManager.getISmsServiceOrThrow().sendStoredText(subId, ActivityThread.currentPackageName(), uri, str, pendingIntent, pendingIntent2);
                    } catch (RemoteException e) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("sendStoredTextMessage: Couldn't send SMS - Exception: ");
                        stringBuilder.append(e.getMessage());
                        Log.e(SmsManager.TAG, stringBuilder.toString());
                        SmsManager.notifySmsGenericError(pendingIntent);
                    }
                }

                public void onFailure() {
                    SmsManager.notifySmsErrorNoDefaultSet(applicationContext, pendingIntent);
                }
            });
            return;
        }
        throw new IllegalArgumentException("Empty message URI");
    }

    public void sendStoredMultipartTextMessage(Uri messageUri, String scAddress, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents) {
        if (messageUri != null) {
            final Uri uri = messageUri;
            final String str = scAddress;
            final ArrayList<PendingIntent> arrayList = sentIntents;
            final ArrayList<PendingIntent> arrayList2 = deliveryIntents;
            final Context applicationContext = ActivityThread.currentApplication().getApplicationContext();
            resolveSubscriptionForOperation(new SubscriptionResolverResult() {
                public void onSuccess(int subId) {
                    try {
                        SmsManager.getISmsServiceOrThrow().sendStoredMultipartText(subId, ActivityThread.currentPackageName(), uri, str, arrayList, arrayList2);
                    } catch (RemoteException e) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("sendStoredTextMessage: Couldn't send SMS - Exception: ");
                        stringBuilder.append(e.getMessage());
                        Log.e(SmsManager.TAG, stringBuilder.toString());
                        SmsManager.notifySmsGenericError((List) arrayList);
                    }
                }

                public void onFailure() {
                    SmsManager.notifySmsErrorNoDefaultSet(applicationContext, (List) arrayList);
                }
            });
            return;
        }
        throw new IllegalArgumentException("Empty message URI");
    }

    public void sendStoredMultimediaMessage(Uri messageUri, Bundle configOverrides, PendingIntent sentIntent) {
        if (messageUri != null) {
            try {
                IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
                if (iMms != null) {
                    iMms.sendStoredMessage(getSubscriptionId(), ActivityThread.currentPackageName(), messageUri, configOverrides, sentIntent);
                    return;
                }
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        throw new IllegalArgumentException("Empty message URI");
    }

    public void setAutoPersisting(boolean enabled) {
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                iMms.setAutoPersisting(ActivityThread.currentPackageName(), enabled);
            }
        } catch (RemoteException e) {
        }
    }

    public boolean getAutoPersisting() {
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.getAutoPersisting();
            }
        } catch (RemoteException e) {
        }
        return false;
    }

    public Bundle getCarrierConfigValues() {
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.getCarrierConfigValues(getSubscriptionId());
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    public String createAppSpecificSmsToken(PendingIntent intent) {
        try {
            return getISmsServiceOrThrow().createAppSpecificSmsToken(getSubscriptionId(), ActivityThread.currentPackageName(), intent);
        } catch (RemoteException ex) {
            ex.rethrowFromSystemServer();
            return null;
        }
    }

    public void getSmsMessagesForFinancialApp(Bundle params, final Executor executor, final FinancialSmsCallback callback) {
        try {
            getISmsServiceOrThrow().getSmsMessagesForFinancialApp(getSubscriptionId(), ActivityThread.currentPackageName(), params, new IFinancialSmsCallback.Stub() {
                public void onGetSmsMessagesForFinancialApp(CursorWindow msgs) {
                    Binder.withCleanCallingIdentity(new -$$Lambda$SmsManager$9$rvckWwRKQKxMC1PhWEkHayc_gf8(executor, callback, msgs));
                }
            });
        } catch (RemoteException ex) {
            ex.rethrowFromSystemServer();
        }
    }

    public String createAppSpecificSmsTokenWithPackageInfo(String prefixes, PendingIntent intent) {
        try {
            return getISmsServiceOrThrow().createAppSpecificSmsTokenWithPackageInfo(getSubscriptionId(), ActivityThread.currentPackageName(), prefixes, intent);
        } catch (RemoteException ex) {
            ex.rethrowFromSystemServer();
            return null;
        }
    }

    public static Bundle getMmsConfig(BaseBundle config) {
        Bundle filtered = new Bundle();
        String str = "enabledTransID";
        filtered.putBoolean(str, config.getBoolean(str));
        str = "enabledMMS";
        filtered.putBoolean(str, config.getBoolean(str));
        str = "enableGroupMms";
        filtered.putBoolean(str, config.getBoolean(str));
        str = "enabledNotifyWapMMSC";
        filtered.putBoolean(str, config.getBoolean(str));
        str = "aliasEnabled";
        filtered.putBoolean(str, config.getBoolean(str));
        str = "allowAttachAudio";
        filtered.putBoolean(str, config.getBoolean(str));
        str = "enableMultipartSMS";
        filtered.putBoolean(str, config.getBoolean(str));
        str = "enableSMSDeliveryReports";
        filtered.putBoolean(str, config.getBoolean(str));
        str = "supportMmsContentDisposition";
        filtered.putBoolean(str, config.getBoolean(str));
        str = "sendMultipartSmsAsSeparateMessages";
        filtered.putBoolean(str, config.getBoolean(str));
        str = "enableMMSReadReports";
        filtered.putBoolean(str, config.getBoolean(str));
        str = "enableMMSDeliveryReports";
        filtered.putBoolean(str, config.getBoolean(str));
        str = "mmsCloseConnection";
        filtered.putBoolean(str, config.getBoolean(str));
        str = "maxMessageSize";
        filtered.putInt(str, config.getInt(str));
        str = "maxImageWidth";
        filtered.putInt(str, config.getInt(str));
        filtered.putInt("maxImageHeight", config.getInt("maxImageHeight"));
        filtered.putInt("recipientLimit", config.getInt("recipientLimit"));
        filtered.putInt("aliasMinChars", config.getInt("aliasMinChars"));
        filtered.putInt("aliasMaxChars", config.getInt("aliasMaxChars"));
        filtered.putInt("smsToMmsTextThreshold", config.getInt("smsToMmsTextThreshold"));
        filtered.putInt("smsToMmsTextLengthThreshold", config.getInt("smsToMmsTextLengthThreshold"));
        filtered.putInt("maxMessageTextSize", config.getInt("maxMessageTextSize"));
        filtered.putInt("maxSubjectLength", config.getInt("maxSubjectLength"));
        filtered.putInt("httpSocketTimeout", config.getInt("httpSocketTimeout"));
        filtered.putString("uaProfTagName", config.getString("uaProfTagName"));
        filtered.putString("userAgent", config.getString("userAgent"));
        filtered.putString("uaProfUrl", config.getString("uaProfUrl"));
        filtered.putString("httpParams", config.getString("httpParams"));
        filtered.putString("emailGatewayNumber", config.getString("emailGatewayNumber"));
        filtered.putString("naiSuffix", config.getString("naiSuffix"));
        filtered.putBoolean("config_cellBroadcastAppLinks", config.getBoolean("config_cellBroadcastAppLinks"));
        filtered.putBoolean("supportHttpCharsetHeader", config.getBoolean("supportHttpCharsetHeader"));
        return filtered;
    }

    public int checkSmsShortCodeDestination(String destAddress, String countryIso) {
        try {
            ISms iccISms = getISmsServiceOrThrow();
            if (iccISms != null) {
                return iccISms.checkSmsShortCodeDestination(getSubscriptionId(), ActivityThread.currentPackageName(), destAddress, countryIso);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "checkSmsShortCodeDestination() RemoteException", e);
        }
        return 0;
    }
}
