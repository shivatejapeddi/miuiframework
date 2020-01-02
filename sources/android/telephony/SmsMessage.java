package android.telephony;

import android.annotation.UnsupportedAppUsage;
import android.content.res.Resources;
import android.os.Binder;
import android.text.TextUtils;
import com.android.internal.R;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.GsmAlphabet.TextEncodingDetails;
import com.android.internal.telephony.Sms7BitEncodingTranslator;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsMessageBase.SubmitPduBase;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;

public class SmsMessage {
    public static final int ENCODING_16BIT = 3;
    public static final int ENCODING_7BIT = 1;
    public static final int ENCODING_8BIT = 2;
    public static final int ENCODING_KSC5601 = 4;
    public static final int ENCODING_UNKNOWN = 0;
    public static final String FORMAT_3GPP = "3gpp";
    public static final String FORMAT_3GPP2 = "3gpp2";
    private static final String LOG_TAG = "SmsMessage";
    public static final int MAX_USER_DATA_BYTES = 140;
    public static final int MAX_USER_DATA_BYTES_WITH_HEADER = 134;
    public static final int MAX_USER_DATA_SEPTETS = 160;
    public static final int MAX_USER_DATA_SEPTETS_WITH_HEADER = 153;
    private static boolean mIsNoEmsSupportConfigListLoaded = false;
    private static NoEmsSupportConfig[] mNoEmsSupportConfigList = null;
    @UnsupportedAppUsage
    private int mSubId = 0;
    @UnsupportedAppUsage
    public SmsMessageBase mWrappedSmsMessage;

    /* renamed from: android.telephony.SmsMessage$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$SmsConstants$MessageClass = new int[com.android.internal.telephony.SmsConstants.MessageClass.values().length];

        static {
            try {
                $SwitchMap$com$android$internal$telephony$SmsConstants$MessageClass[com.android.internal.telephony.SmsConstants.MessageClass.CLASS_0.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$SmsConstants$MessageClass[com.android.internal.telephony.SmsConstants.MessageClass.CLASS_1.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$SmsConstants$MessageClass[com.android.internal.telephony.SmsConstants.MessageClass.CLASS_2.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$SmsConstants$MessageClass[com.android.internal.telephony.SmsConstants.MessageClass.CLASS_3.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Format {
    }

    public enum MessageClass {
        UNKNOWN,
        CLASS_0,
        CLASS_1,
        CLASS_2,
        CLASS_3
    }

    private static class NoEmsSupportConfig {
        String mGid1;
        boolean mIsPrefix;
        String mOperatorNumber;

        public NoEmsSupportConfig(String[] config) {
            this.mOperatorNumber = config[0];
            this.mIsPrefix = "prefix".equals(config[1]);
            this.mGid1 = config.length > 2 ? config[2] : null;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("NoEmsSupportConfig { mOperatorNumber = ");
            stringBuilder.append(this.mOperatorNumber);
            stringBuilder.append(", mIsPrefix = ");
            stringBuilder.append(this.mIsPrefix);
            stringBuilder.append(", mGid1 = ");
            stringBuilder.append(this.mGid1);
            stringBuilder.append(" }");
            return stringBuilder.toString();
        }
    }

    public static class SubmitPdu {
        public byte[] encodedMessage;
        public byte[] encodedScAddress;

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SubmitPdu: encodedScAddress = ");
            stringBuilder.append(Arrays.toString(this.encodedScAddress));
            stringBuilder.append(", encodedMessage = ");
            stringBuilder.append(Arrays.toString(this.encodedMessage));
            return stringBuilder.toString();
        }

        protected SubmitPdu(SubmitPduBase spb) {
            this.encodedMessage = spb.encodedMessage;
            this.encodedScAddress = spb.encodedScAddress;
        }
    }

    @UnsupportedAppUsage
    public void setSubId(int subId) {
        this.mSubId = subId;
    }

    @UnsupportedAppUsage
    public int getSubId() {
        return this.mSubId;
    }

    public SmsMessage(SmsMessageBase smb) {
        this.mWrappedSmsMessage = smb;
    }

    @Deprecated
    public static SmsMessage createFromPdu(byte[] pdu) {
        return createFromPdu(pdu, 2 == TelephonyManager.getDefault().getCurrentPhoneType() ? "3gpp2" : "3gpp");
    }

    public static SmsMessage createFromPdu(byte[] pdu, String format) {
        return createFromPdu(pdu, format, true);
    }

    private static SmsMessage createFromPdu(byte[] pdu, String format, boolean fallbackToOtherFormat) {
        String str = LOG_TAG;
        if (pdu == null) {
            Rlog.i(str, "createFromPdu(): pdu is null");
            return null;
        }
        String otherFormat;
        SmsMessageBase wrappedMessage;
        String str2 = "3gpp2";
        String str3 = "3gpp";
        if (str2.equals(format)) {
            otherFormat = str3;
        } else {
            otherFormat = str2;
        }
        if (str2.equals(format)) {
            wrappedMessage = com.android.internal.telephony.cdma.SmsMessage.createFromPdu(pdu);
        } else if (str3.equals(format)) {
            wrappedMessage = com.android.internal.telephony.gsm.SmsMessage.createFromPdu(pdu);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("createFromPdu(): unsupported message format ");
            stringBuilder.append(format);
            Rlog.e(str, stringBuilder.toString());
            return null;
        }
        if (wrappedMessage != null) {
            return new SmsMessage(wrappedMessage);
        }
        if (fallbackToOtherFormat) {
            return createFromPdu(pdu, otherFormat, false);
        }
        Rlog.e(str, "createFromPdu(): wrappedMessage is null");
        return null;
    }

    public static SmsMessage newFromCMT(byte[] pdu) {
        SmsMessageBase wrappedMessage = com.android.internal.telephony.gsm.SmsMessage.newFromCMT(pdu);
        if (wrappedMessage != null) {
            return new SmsMessage(wrappedMessage);
        }
        Rlog.e(LOG_TAG, "newFromCMT(): wrappedMessage is null");
        return null;
    }

    public static SmsMessage createFromEfRecord(int index, byte[] data) {
        SmsMessageBase wrappedMessage;
        if (isCdmaVoice()) {
            wrappedMessage = com.android.internal.telephony.cdma.SmsMessage.createFromEfRecord(index, data);
        } else {
            wrappedMessage = com.android.internal.telephony.gsm.SmsMessage.createFromEfRecord(index, data);
        }
        if (wrappedMessage != null) {
            return new SmsMessage(wrappedMessage);
        }
        Rlog.e(LOG_TAG, "createFromEfRecord(): wrappedMessage is null");
        return null;
    }

    public static SmsMessage createFromEfRecord(int index, byte[] data, int subId) {
        SmsMessageBase wrappedMessage;
        if (isCdmaVoice(subId)) {
            wrappedMessage = com.android.internal.telephony.cdma.SmsMessage.createFromEfRecord(index, data);
        } else {
            wrappedMessage = com.android.internal.telephony.gsm.SmsMessage.createFromEfRecord(index, data);
        }
        return wrappedMessage != null ? new SmsMessage(wrappedMessage) : null;
    }

    public static int getTPLayerLengthForPDU(String pdu) {
        if (isCdmaVoice()) {
            return com.android.internal.telephony.cdma.SmsMessage.getTPLayerLengthForPDU(pdu);
        }
        return com.android.internal.telephony.gsm.SmsMessage.getTPLayerLengthForPDU(pdu);
    }

    public static int[] calculateLength(CharSequence msgBody, boolean use7bitOnly) {
        return calculateLength(msgBody, use7bitOnly, SmsManager.getDefaultSmsSubscriptionId());
    }

    public static int[] calculateLength(CharSequence msgBody, boolean use7bitOnly, int subId) {
        TextEncodingDetails ted;
        if (useCdmaFormatForMoSms(subId)) {
            ted = com.android.internal.telephony.cdma.SmsMessage.calculateLength(msgBody, use7bitOnly, true);
        } else {
            ted = com.android.internal.telephony.gsm.SmsMessage.calculateLength(msgBody, use7bitOnly);
        }
        return new int[]{ted.msgCount, ted.codeUnitCount, ted.codeUnitsRemaining, ted.codeUnitSize};
    }

    @UnsupportedAppUsage
    public static ArrayList<String> fragmentText(String text) {
        return fragmentText(text, SmsManager.getDefaultSmsSubscriptionId());
    }

    public static ArrayList<String> fragmentText(String text, int subId) {
        TextEncodingDetails ted;
        int udhLength;
        boolean isCdma = useCdmaFormatForMoSms(subId);
        if (isCdma) {
            ted = com.android.internal.telephony.cdma.SmsMessage.calculateLength(text, false, true);
        } else {
            ted = com.android.internal.telephony.gsm.SmsMessage.calculateLength(text, false);
        }
        if (ted.codeUnitSize == 1) {
            if (ted.languageTable != 0 && ted.languageShiftTable != 0) {
                udhLength = 7;
            } else if (ted.languageTable == 0 && ted.languageShiftTable == 0) {
                udhLength = 0;
            } else {
                udhLength = 4;
            }
            if (ted.msgCount > 1) {
                udhLength += 6;
            }
            if (udhLength != 0) {
                udhLength++;
            }
            udhLength = 160 - udhLength;
        } else if (ted.msgCount > 1) {
            udhLength = 134;
            if (!hasEmsSupport() && ted.msgCount < 10) {
                udhLength = 134 - 2;
            }
        } else {
            udhLength = 140;
        }
        String newMsgBody = null;
        if (Resources.getSystem().getBoolean(R.bool.config_sms_force_7bit_encoding)) {
            newMsgBody = Sms7BitEncodingTranslator.translate(text, isCdma);
        }
        if (TextUtils.isEmpty(newMsgBody)) {
            newMsgBody = text;
        }
        int pos = 0;
        int textLen = newMsgBody.length();
        ArrayList<String> result = new ArrayList(ted.msgCount);
        while (pos < textLen) {
            int nextPos;
            if (ted.codeUnitSize != 1) {
                nextPos = SmsMessageBase.findNextUnicodePosition(pos, udhLength, newMsgBody);
            } else if (isCdma && ted.msgCount == 1) {
                nextPos = Math.min(udhLength, textLen - pos) + pos;
            } else {
                nextPos = GsmAlphabet.findGsmSeptetLimitIndex(newMsgBody, pos, udhLength, ted.languageTable, ted.languageShiftTable);
            }
            if (nextPos <= pos || nextPos > textLen) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("fragmentText failed (");
                stringBuilder.append(pos);
                String str = " >= ";
                stringBuilder.append(str);
                stringBuilder.append(nextPos);
                stringBuilder.append(" or ");
                stringBuilder.append(nextPos);
                stringBuilder.append(str);
                stringBuilder.append(textLen);
                stringBuilder.append(")");
                Rlog.e(LOG_TAG, stringBuilder.toString());
                break;
            }
            result.add(newMsgBody.substring(pos, nextPos));
            pos = nextPos;
        }
        return result;
    }

    public static int[] calculateLength(String messageBody, boolean use7bitOnly) {
        return calculateLength((CharSequence) messageBody, use7bitOnly);
    }

    public static int[] calculateLength(String messageBody, boolean use7bitOnly, int subId) {
        return calculateLength((CharSequence) messageBody, use7bitOnly, subId);
    }

    public static SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, String message, boolean statusReportRequested) {
        return getSubmitPdu(scAddress, destinationAddress, message, statusReportRequested, SmsManager.getDefaultSmsSubscriptionId());
    }

    public static SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, String message, boolean statusReportRequested, int subId) {
        SubmitPduBase spb;
        if (useCdmaFormatForMoSms(subId)) {
            spb = com.android.internal.telephony.cdma.SmsMessage.getSubmitPdu(scAddress, destinationAddress, message, statusReportRequested, (SmsHeader) null);
        } else {
            spb = com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(scAddress, destinationAddress, message, statusReportRequested);
        }
        return new SubmitPdu(spb);
    }

    public static SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, short destinationPort, byte[] data, boolean statusReportRequested) {
        SubmitPduBase spb;
        if (useCdmaFormatForMoSms()) {
            spb = com.android.internal.telephony.cdma.SmsMessage.getSubmitPdu(scAddress, destinationAddress, (int) destinationPort, data, statusReportRequested);
        } else {
            spb = com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(scAddress, destinationAddress, (int) destinationPort, data, statusReportRequested);
        }
        return new SubmitPdu(spb);
    }

    public String getServiceCenterAddress() {
        return this.mWrappedSmsMessage.getServiceCenterAddress();
    }

    public String getOriginatingAddress() {
        return this.mWrappedSmsMessage.getOriginatingAddress();
    }

    public String getDisplayOriginatingAddress() {
        return this.mWrappedSmsMessage.getDisplayOriginatingAddress();
    }

    public String getMessageBody() {
        return this.mWrappedSmsMessage.getMessageBody();
    }

    public MessageClass getMessageClass() {
        int i = AnonymousClass1.$SwitchMap$com$android$internal$telephony$SmsConstants$MessageClass[this.mWrappedSmsMessage.getMessageClass().ordinal()];
        if (i == 1) {
            return MessageClass.CLASS_0;
        }
        if (i == 2) {
            return MessageClass.CLASS_1;
        }
        if (i == 3) {
            return MessageClass.CLASS_2;
        }
        if (i != 4) {
            return MessageClass.UNKNOWN;
        }
        return MessageClass.CLASS_3;
    }

    public String getDisplayMessageBody() {
        return this.mWrappedSmsMessage.getDisplayMessageBody();
    }

    public String getPseudoSubject() {
        return this.mWrappedSmsMessage.getPseudoSubject();
    }

    public long getTimestampMillis() {
        return this.mWrappedSmsMessage.getTimestampMillis();
    }

    public boolean isEmail() {
        return this.mWrappedSmsMessage.isEmail();
    }

    public String getEmailBody() {
        return this.mWrappedSmsMessage.getEmailBody();
    }

    public String getEmailFrom() {
        return this.mWrappedSmsMessage.getEmailFrom();
    }

    public int getProtocolIdentifier() {
        return this.mWrappedSmsMessage.getProtocolIdentifier();
    }

    public boolean isReplace() {
        return this.mWrappedSmsMessage.isReplace();
    }

    public boolean isCphsMwiMessage() {
        return this.mWrappedSmsMessage.isCphsMwiMessage();
    }

    public boolean isMWIClearMessage() {
        return this.mWrappedSmsMessage.isMWIClearMessage();
    }

    public boolean isMWISetMessage() {
        return this.mWrappedSmsMessage.isMWISetMessage();
    }

    public boolean isMwiDontStore() {
        return this.mWrappedSmsMessage.isMwiDontStore();
    }

    public byte[] getUserData() {
        return this.mWrappedSmsMessage.getUserData();
    }

    public byte[] getPdu() {
        return this.mWrappedSmsMessage.getPdu();
    }

    @Deprecated
    public int getStatusOnSim() {
        return this.mWrappedSmsMessage.getStatusOnIcc();
    }

    public int getStatusOnIcc() {
        return this.mWrappedSmsMessage.getStatusOnIcc();
    }

    @Deprecated
    public int getIndexOnSim() {
        return this.mWrappedSmsMessage.getIndexOnIcc();
    }

    public int getIndexOnIcc() {
        return this.mWrappedSmsMessage.getIndexOnIcc();
    }

    public int getStatus() {
        return this.mWrappedSmsMessage.getStatus();
    }

    public boolean isStatusReportMessage() {
        return this.mWrappedSmsMessage.isStatusReportMessage();
    }

    public boolean isReplyPathPresent() {
        return this.mWrappedSmsMessage.isReplyPathPresent();
    }

    @UnsupportedAppUsage
    private static boolean useCdmaFormatForMoSms() {
        return useCdmaFormatForMoSms(SmsManager.getDefaultSmsSubscriptionId());
    }

    @UnsupportedAppUsage
    private static boolean useCdmaFormatForMoSms(int subId) {
        SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(subId);
        if (!smsManager.isImsSmsSupported()) {
            return isCdmaVoice(subId);
        }
        return "3gpp2".equals(smsManager.getImsSmsFormat());
    }

    private static boolean isCdmaVoice() {
        return isCdmaVoice(SmsManager.getDefaultSmsSubscriptionId());
    }

    private static boolean isCdmaVoice(int subId) {
        return 2 == TelephonyManager.getDefault().getCurrentPhoneType(subId);
    }

    public static boolean hasEmsSupport() {
        if (!isNoEmsSupportConfigListExisted()) {
            return true;
        }
        long identity = Binder.clearCallingIdentity();
        try {
            String simOperator = TelephonyManager.getDefault().getSimOperatorNumeric();
            String gid = TelephonyManager.getDefault().getGroupIdLevel1();
            if (!TextUtils.isEmpty(simOperator)) {
                for (NoEmsSupportConfig currentConfig : mNoEmsSupportConfigList) {
                    if (simOperator.startsWith(currentConfig.mOperatorNumber) && (TextUtils.isEmpty(currentConfig.mGid1) || (!TextUtils.isEmpty(currentConfig.mGid1) && currentConfig.mGid1.equalsIgnoreCase(gid)))) {
                        return false;
                    }
                }
            }
            return true;
        } finally {
            Binder.restoreCallingIdentity(identity);
        }
    }

    public static boolean shouldAppendPageNumberAsPrefix() {
        if (!isNoEmsSupportConfigListExisted()) {
            return false;
        }
        long identity = Binder.clearCallingIdentity();
        try {
            String simOperator = TelephonyManager.getDefault().getSimOperatorNumeric();
            String gid = TelephonyManager.getDefault().getGroupIdLevel1();
            for (NoEmsSupportConfig currentConfig : mNoEmsSupportConfigList) {
                if (simOperator.startsWith(currentConfig.mOperatorNumber) && (TextUtils.isEmpty(currentConfig.mGid1) || (!TextUtils.isEmpty(currentConfig.mGid1) && currentConfig.mGid1.equalsIgnoreCase(gid)))) {
                    return currentConfig.mIsPrefix;
                }
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(identity);
        }
    }

    private static boolean isNoEmsSupportConfigListExisted() {
        if (!mIsNoEmsSupportConfigListLoaded) {
            Resources r = Resources.getSystem();
            if (r != null) {
                String[] listArray = r.getStringArray(17236106);
                if (listArray != null && listArray.length > 0) {
                    mNoEmsSupportConfigList = new NoEmsSupportConfig[listArray.length];
                    for (int i = 0; i < listArray.length; i++) {
                        mNoEmsSupportConfigList[i] = new NoEmsSupportConfig(listArray[i].split(";"));
                    }
                }
                mIsNoEmsSupportConfigListLoaded = true;
            }
        }
        NoEmsSupportConfig[] noEmsSupportConfigArr = mNoEmsSupportConfigList;
        if (noEmsSupportConfigArr == null || noEmsSupportConfigArr.length == 0) {
            return false;
        }
        return true;
    }

    public String getRecipientAddress() {
        return this.mWrappedSmsMessage.getRecipientAddress();
    }

    public int getEncodingType() {
        return this.mWrappedSmsMessage.getEncodingType();
    }
}
