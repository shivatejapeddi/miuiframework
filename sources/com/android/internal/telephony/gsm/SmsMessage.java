package com.android.internal.telephony.gsm;

import android.content.res.Resources;
import android.net.wifi.WifiEnterpriseConfig;
import android.telephony.PhoneNumberUtils;
import android.telephony.PreciseDisconnectCause;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.text.format.Time;
import com.android.internal.R;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.GsmAlphabet.TextEncodingDetails;
import com.android.internal.telephony.Sms7BitEncodingTranslator;
import com.android.internal.telephony.SmsAddress;
import com.android.internal.telephony.SmsConstants.MessageClass;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsHeader.PortAddrs;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsMessageBase.SubmitPduBase;
import com.android.internal.telephony.uicc.IccUtils;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public class SmsMessage extends SmsMessageBase {
    private static final int INVALID_VALIDITY_PERIOD = -1;
    static final String LOG_TAG = "SmsMessage";
    private static final int VALIDITY_PERIOD_FORMAT_ABSOLUTE = 3;
    private static final int VALIDITY_PERIOD_FORMAT_ENHANCED = 1;
    private static final int VALIDITY_PERIOD_FORMAT_NONE = 0;
    private static final int VALIDITY_PERIOD_FORMAT_RELATIVE = 2;
    private static final int VALIDITY_PERIOD_MAX = 635040;
    private static final int VALIDITY_PERIOD_MIN = 5;
    private static final boolean VDBG = false;
    private int mDataCodingScheme;
    private boolean mIsStatusReportMessage = false;
    private int mMti;
    private int mProtocolIdentifier;
    private boolean mReplyPathPresent = false;
    private int mStatus;
    private int mVoiceMailCount = 0;
    private MessageClass messageClass;

    private static class PduParser {
        int mCur = 0;
        byte[] mPdu;
        byte[] mUserData;
        SmsHeader mUserDataHeader;
        int mUserDataSeptetPadding = 0;

        PduParser(byte[] pdu) {
            this.mPdu = pdu;
        }

        /* Access modifiers changed, original: 0000 */
        public String getSCAddress() {
            String ret;
            int len = getByte();
            if (len == 0) {
                ret = null;
            } else {
                try {
                    ret = PhoneNumberUtils.calledPartyBCDToString(this.mPdu, this.mCur, len, 2);
                } catch (RuntimeException tr) {
                    Rlog.d(SmsMessage.LOG_TAG, "invalid SC address: ", tr);
                    ret = null;
                }
            }
            this.mCur += len;
            return ret;
        }

        /* Access modifiers changed, original: 0000 */
        public int getByte() {
            byte[] bArr = this.mPdu;
            int i = this.mCur;
            this.mCur = i + 1;
            return bArr[i] & 255;
        }

        /* Access modifiers changed, original: 0000 */
        public GsmSmsAddress getAddress() {
            byte[] bArr = this.mPdu;
            int i = this.mCur;
            int lengthBytes = (((bArr[i] & 255) + 1) / 2) + 2;
            try {
                GsmSmsAddress ret = new GsmSmsAddress(bArr, i, lengthBytes);
                this.mCur += lengthBytes;
                return ret;
            } catch (ParseException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        /* Access modifiers changed, original: 0000 */
        public long getSCTimestampMillis() {
            byte[] bArr = this.mPdu;
            int i = this.mCur;
            this.mCur = i + 1;
            int year = IccUtils.gsmBcdByteToInt(bArr[i]);
            byte[] bArr2 = this.mPdu;
            int i2 = this.mCur;
            this.mCur = i2 + 1;
            i = IccUtils.gsmBcdByteToInt(bArr2[i2]);
            byte[] bArr3 = this.mPdu;
            int i3 = this.mCur;
            this.mCur = i3 + 1;
            i2 = IccUtils.gsmBcdByteToInt(bArr3[i3]);
            byte[] bArr4 = this.mPdu;
            int i4 = this.mCur;
            this.mCur = i4 + 1;
            i3 = IccUtils.gsmBcdByteToInt(bArr4[i4]);
            byte[] bArr5 = this.mPdu;
            int i5 = this.mCur;
            this.mCur = i5 + 1;
            i4 = IccUtils.gsmBcdByteToInt(bArr5[i5]);
            byte[] bArr6 = this.mPdu;
            int i6 = this.mCur;
            this.mCur = i6 + 1;
            i5 = IccUtils.gsmBcdByteToInt(bArr6[i6]);
            byte tzByte = this.mPdu;
            int i7 = this.mCur;
            this.mCur = i7 + 1;
            tzByte = tzByte[i7];
            i7 = IccUtils.gsmBcdByteToInt((byte) (tzByte & -9));
            i7 = (tzByte & 8) == 0 ? i7 : -i7;
            Time time = new Time(Time.TIMEZONE_UTC);
            time.year = year >= 90 ? year + PreciseDisconnectCause.ECBM_NOT_SUPPORTED : year + 2000;
            time.month = i - 1;
            time.monthDay = i2;
            time.hour = i3;
            time.minute = i4;
            time.second = i5;
            return time.toMillis(true) - ((long) (((i7 * 15) * 60) * 1000));
        }

        /* Access modifiers changed, original: 0000 */
        public int constructUserData(boolean hasUserDataHeader, boolean dataInSeptets) {
            int offset;
            int offset2;
            int offset3 = this.mCur;
            byte[] bArr = this.mPdu;
            int offset4 = offset3 + 1;
            offset3 = bArr[offset3] & 255;
            int headerSeptets = 0;
            int userDataHeaderLength = 0;
            int i = 0;
            if (hasUserDataHeader) {
                offset = offset4 + 1;
                userDataHeaderLength = bArr[offset4] & 255;
                offset4 = new byte[userDataHeaderLength];
                System.arraycopy(bArr, offset, offset4, 0, userDataHeaderLength);
                this.mUserDataHeader = SmsHeader.fromByteArray(offset4);
                offset2 = offset + userDataHeaderLength;
                offset = (userDataHeaderLength + 1) * 8;
                headerSeptets = (offset / 7) + (offset % 7 > 0 ? 1 : 0);
                this.mUserDataSeptetPadding = (headerSeptets * 7) - offset;
            } else {
                offset2 = offset4;
            }
            if (dataInSeptets) {
                offset4 = this.mPdu.length - offset2;
            } else {
                offset4 = offset3 - (hasUserDataHeader ? userDataHeaderLength + 1 : 0);
                if (offset4 < 0) {
                    offset4 = 0;
                }
            }
            this.mUserData = new byte[offset4];
            byte[] bArr2 = this.mPdu;
            byte[] bArr3 = this.mUserData;
            System.arraycopy(bArr2, offset2, bArr3, 0, bArr3.length);
            this.mCur = offset2;
            if (!dataInSeptets) {
                return this.mUserData.length;
            }
            offset = offset3 - headerSeptets;
            if (offset >= 0) {
                i = offset;
            }
            return i;
        }

        /* Access modifiers changed, original: 0000 */
        public byte[] getUserData() {
            return this.mUserData;
        }

        /* Access modifiers changed, original: 0000 */
        public SmsHeader getUserDataHeader() {
            return this.mUserDataHeader;
        }

        /* Access modifiers changed, original: 0000 */
        public String getUserDataGSM7Bit(int septetCount, int languageTable, int languageShiftTable) {
            String ret = GsmAlphabet.gsm7BitPackedToString(this.mPdu, this.mCur, septetCount, this.mUserDataSeptetPadding, languageTable, languageShiftTable);
            this.mCur += (septetCount * 7) / 8;
            return ret;
        }

        /* Access modifiers changed, original: 0000 */
        public String getUserDataGSM8bit(int byteCount) {
            String ret = GsmAlphabet.gsm8BitUnpackedToString(this.mPdu, this.mCur, byteCount);
            this.mCur += byteCount;
            return ret;
        }

        /* Access modifiers changed, original: 0000 */
        public String getUserDataUCS2(int byteCount) {
            String ret;
            try {
                ret = new String(this.mPdu, this.mCur, byteCount, "utf-16");
            } catch (UnsupportedEncodingException ex) {
                Rlog.e(SmsMessage.LOG_TAG, "implausible UnsupportedEncodingException", ex);
                ret = "";
            }
            this.mCur += byteCount;
            return ret;
        }

        /* Access modifiers changed, original: 0000 */
        public String getUserDataKSC5601(int byteCount) {
            String ret;
            try {
                ret = new String(this.mPdu, this.mCur, byteCount, "KSC5601");
            } catch (UnsupportedEncodingException ex) {
                Rlog.e(SmsMessage.LOG_TAG, "implausible UnsupportedEncodingException", ex);
                ret = "";
            }
            this.mCur += byteCount;
            return ret;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean moreDataPresent() {
            return this.mPdu.length > this.mCur;
        }
    }

    public static class SubmitPdu extends SubmitPduBase {
    }

    public static SmsMessage createFromPdu(byte[] pdu) {
        String str = LOG_TAG;
        try {
            SmsMessage msg = new SmsMessage();
            msg.parsePdu(pdu);
            return msg;
        } catch (RuntimeException ex) {
            Rlog.e(str, "SMS PDU parsing failed: ", ex);
            return null;
        } catch (OutOfMemoryError e) {
            Rlog.e(str, "SMS PDU parsing failed with out of memory: ", e);
            return null;
        }
    }

    public boolean isTypeZero() {
        return this.mProtocolIdentifier == 64;
    }

    public static SmsMessage newFromCMT(byte[] pdu) {
        try {
            SmsMessage msg = new SmsMessage();
            msg.parsePdu(pdu);
            return msg;
        } catch (RuntimeException ex) {
            Rlog.e(LOG_TAG, "SMS PDU parsing failed: ", ex);
            return null;
        }
    }

    public static SmsMessage newFromCDS(byte[] pdu) {
        try {
            SmsMessage msg = new SmsMessage();
            msg.parsePdu(pdu);
            return msg;
        } catch (RuntimeException ex) {
            Rlog.e(LOG_TAG, "CDS SMS PDU parsing failed: ", ex);
            return null;
        }
    }

    public static SmsMessage createFromEfRecord(int index, byte[] data) {
        String str = LOG_TAG;
        try {
            SmsMessage msg = new SmsMessage();
            msg.mIndexOnIcc = index;
            if ((data[0] & 1) == 0) {
                Rlog.w(str, "SMS parsing failed: Trying to parse a free record");
                return null;
            }
            msg.mStatusOnIcc = data[0] & 7;
            int size = data.length - 1;
            byte[] pdu = new byte[size];
            System.arraycopy(data, 1, pdu, 0, size);
            msg.parsePdu(pdu);
            return msg;
        } catch (RuntimeException ex) {
            Rlog.e(str, "SMS PDU parsing failed: ", ex);
            return null;
        }
    }

    public static int getTPLayerLengthForPDU(String pdu) {
        return ((pdu.length() / 2) - Integer.parseInt(pdu.substring(0, 2), 16)) - 1;
    }

    public static int getRelativeValidityPeriod(int validityPeriod) {
        int relValidityPeriod = -1;
        if (validityPeriod < 5 || validityPeriod > VALIDITY_PERIOD_MAX) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid Validity Period");
            stringBuilder.append(validityPeriod);
            Rlog.e(LOG_TAG, stringBuilder.toString());
            return -1;
        }
        if (validityPeriod <= MetricsEvent.ACTION_PERMISSION_DENIED_RECEIVE_WAP_PUSH) {
            relValidityPeriod = (validityPeriod / 5) - 1;
        } else if (validityPeriod <= MetricsEvent.ACTION_HUSH_GESTURE) {
            relValidityPeriod = ((validityPeriod - 720) / 30) + 143;
        } else if (validityPeriod <= 43200) {
            relValidityPeriod = (validityPeriod / MetricsEvent.ACTION_HUSH_GESTURE) + 166;
        } else if (validityPeriod <= VALIDITY_PERIOD_MAX) {
            relValidityPeriod = (validityPeriod / 10080) + 192;
        }
        return relValidityPeriod;
    }

    public static SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, String message, boolean statusReportRequested, byte[] header) {
        return getSubmitPdu(scAddress, destinationAddress, message, statusReportRequested, header, 0, 0, 0);
    }

    public static SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, String message, boolean statusReportRequested, byte[] header, int encoding, int languageTable, int languageShiftTable) {
        return getSubmitPdu(scAddress, destinationAddress, message, statusReportRequested, header, encoding, languageTable, languageShiftTable, -1);
    }

    public static SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, String message, boolean statusReportRequested, byte[] header, int encoding, int languageTable, int languageShiftTable, int validityPeriod) {
        SubmitPdu submitPdu;
        String str = destinationAddress;
        String str2 = message;
        String str3;
        if (str2 == null) {
            submitPdu = null;
            str3 = scAddress;
        } else if (str == null) {
            submitPdu = null;
            str3 = scAddress;
        } else {
            int encoding2;
            int languageTable2;
            int languageShiftTable2;
            byte[] header2;
            String str4 = LOG_TAG;
            if (encoding == 0) {
                TextEncodingDetails ted = calculateLength(str2, false);
                encoding2 = ted.codeUnitSize;
                languageTable2 = ted.languageTable;
                languageShiftTable2 = ted.languageShiftTable;
                SmsHeader smsHeader;
                if (encoding2 != 1 || (languageTable2 == 0 && languageShiftTable2 == 0)) {
                    header2 = header;
                } else if (header != null) {
                    smsHeader = SmsHeader.fromByteArray(header);
                    if (smsHeader.languageTable == languageTable2 && smsHeader.languageShiftTable == languageShiftTable2) {
                        header2 = header;
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Updating language table in SMS header: ");
                        stringBuilder.append(smsHeader.languageTable);
                        String str5 = " -> ";
                        stringBuilder.append(str5);
                        stringBuilder.append(languageTable2);
                        stringBuilder.append(", ");
                        stringBuilder.append(smsHeader.languageShiftTable);
                        stringBuilder.append(str5);
                        stringBuilder.append(languageShiftTable2);
                        Rlog.w(str4, stringBuilder.toString());
                        smsHeader.languageTable = languageTable2;
                        smsHeader.languageShiftTable = languageShiftTable2;
                        header2 = SmsHeader.toByteArray(smsHeader);
                    }
                } else {
                    smsHeader = new SmsHeader();
                    smsHeader.languageTable = languageTable2;
                    smsHeader.languageShiftTable = languageShiftTable2;
                    header2 = SmsHeader.toByteArray(smsHeader);
                }
            } else {
                header2 = header;
                encoding2 = encoding;
                languageTable2 = languageTable;
                languageShiftTable2 = languageShiftTable;
            }
            SubmitPdu ret = new SubmitPdu();
            int relativeValidityPeriod = getRelativeValidityPeriod(validityPeriod);
            int relativeValidityPeriod2 = relativeValidityPeriod;
            if (relativeValidityPeriod >= 0) {
                relativeValidityPeriod = 2;
            } else {
                relativeValidityPeriod = 0;
            }
            ByteArrayOutputStream bo = getSubmitPduHead(scAddress, str, (byte) (((relativeValidityPeriod << 3) | 1) | (header2 != null ? 64 : 0)), statusReportRequested, ret);
            if (bo == null) {
                return ret;
            }
            byte[] userData;
            str = "Implausible UnsupportedEncodingException ";
            if (encoding2 == 1) {
                try {
                    userData = GsmAlphabet.stringToGsm7BitPackedWithHeader(str2, header2, languageTable2, languageShiftTable2);
                } catch (EncodeException uex) {
                    header = uex;
                    String str6 = "Exceed size limitation EncodeException";
                    if (header.getError() == 1) {
                        Rlog.e(str4, str6, header);
                        return null;
                    }
                    EncodeException ex = header;
                    try {
                        userData = encodeUCS2(str2, header2);
                        encoding2 = 3;
                    } catch (EncodeException e) {
                        Rlog.e(str4, str6, e);
                        return null;
                    } catch (UnsupportedEncodingException uex2) {
                        Rlog.e(str4, str, uex2);
                        return null;
                    }
                }
            }
            try {
                userData = encodeUCS2(str2, header2);
            } catch (UnsupportedEncodingException uex22) {
                Rlog.e(str4, str, uex22);
                return null;
            }
            str = "Message too long (";
            StringBuilder stringBuilder2;
            if (encoding2 == 1) {
                if ((userData[0] & 255) > 160) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str);
                    stringBuilder2.append(userData[0] & 255);
                    stringBuilder2.append(" septets)");
                    Rlog.e(str4, stringBuilder2.toString());
                    return null;
                }
                bo.write(0);
            } else if ((userData[0] & 255) > 140) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(userData[0] & 255);
                stringBuilder2.append(" bytes)");
                Rlog.e(str4, stringBuilder2.toString());
                return null;
            } else {
                bo.write(8);
            }
            if (relativeValidityPeriod == 2) {
                bo.write(relativeValidityPeriod2);
            }
            bo.write(userData, 0, userData.length);
            ret.encodedMessage = bo.toByteArray();
            return ret;
        }
        return submitPdu;
    }

    private static byte[] encodeUCS2(String message, byte[] header) throws UnsupportedEncodingException, EncodeException {
        byte[] userData;
        byte[] textPart = message.getBytes("utf-16be");
        if (header != null) {
            userData = new byte[((header.length + textPart.length) + 1)];
            userData[0] = (byte) header.length;
            System.arraycopy(header, 0, userData, 1, header.length);
            System.arraycopy(textPart, 0, userData, header.length + 1, textPart.length);
        } else {
            userData = textPart;
        }
        if (userData.length <= 255) {
            byte[] ret = new byte[(userData.length + 1)];
            ret[0] = (byte) (255 & userData.length);
            System.arraycopy(userData, 0, ret, 1, userData.length);
            return ret;
        }
        throw new EncodeException("Payload cannot exceed 255 bytes", 1);
    }

    public static SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, String message, boolean statusReportRequested) {
        return getSubmitPdu(scAddress, destinationAddress, message, statusReportRequested, null);
    }

    public static SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, String message, boolean statusReportRequested, int validityPeriod) {
        return getSubmitPdu(scAddress, destinationAddress, message, statusReportRequested, null, 0, 0, 0, validityPeriod);
    }

    public static SubmitPdu getSubmitPdu(String scAddress, String destinationAddress, int destinationPort, byte[] data, boolean statusReportRequested) {
        PortAddrs portAddrs = new PortAddrs();
        portAddrs.destPort = destinationPort;
        portAddrs.origPort = 0;
        portAddrs.areEightBits = false;
        SmsHeader smsHeader = new SmsHeader();
        smsHeader.portAddrs = portAddrs;
        byte[] smsHeaderData = SmsHeader.toByteArray(smsHeader);
        if ((data.length + smsHeaderData.length) + 1 > 140) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SMS data message may only contain ");
            stringBuilder.append((140 - smsHeaderData.length) - 1);
            stringBuilder.append(" bytes");
            Rlog.e(LOG_TAG, stringBuilder.toString());
            return null;
        }
        SubmitPdu ret = new SubmitPdu();
        ByteArrayOutputStream bo = getSubmitPduHead(scAddress, destinationAddress, (byte) 65, statusReportRequested, ret);
        if (bo == null) {
            return ret;
        }
        bo.write(4);
        bo.write((data.length + smsHeaderData.length) + 1);
        bo.write(smsHeaderData.length);
        bo.write(smsHeaderData, 0, smsHeaderData.length);
        bo.write(data, 0, data.length);
        ret.encodedMessage = bo.toByteArray();
        return ret;
    }

    private static ByteArrayOutputStream getSubmitPduHead(String scAddress, String destinationAddress, byte mtiByte, boolean statusReportRequested, SubmitPdu ret) {
        ByteArrayOutputStream bo = new ByteArrayOutputStream(180);
        if (scAddress == null) {
            ret.encodedScAddress = null;
        } else {
            ret.encodedScAddress = PhoneNumberUtils.networkPortionToCalledPartyBCDWithLength(scAddress);
        }
        if (statusReportRequested) {
            mtiByte = (byte) (mtiByte | 32);
        }
        bo.write(mtiByte);
        bo.write(0);
        byte[] daBytes = PhoneNumberUtils.networkPortionToCalledPartyBCD(destinationAddress);
        if (daBytes == null) {
            return null;
        }
        int i = 1;
        int length = (daBytes.length - 1) * 2;
        if ((daBytes[daBytes.length - 1] & 240) != 240) {
            i = 0;
        }
        bo.write(length - i);
        bo.write(daBytes, 0, daBytes.length);
        bo.write(0);
        return bo;
    }

    public static TextEncodingDetails calculateLength(CharSequence msgBody, boolean use7bitOnly) {
        CharSequence newMsgBody = null;
        if (Resources.getSystem().getBoolean(R.bool.config_sms_force_7bit_encoding)) {
            newMsgBody = Sms7BitEncodingTranslator.translate(msgBody, false);
        }
        if (TextUtils.isEmpty(newMsgBody)) {
            newMsgBody = msgBody;
        }
        TextEncodingDetails ted = GsmAlphabet.countGsmSeptets(newMsgBody, use7bitOnly);
        if (ted == null) {
            return SmsMessageBase.calcUnicodeEncodingDetails(newMsgBody);
        }
        return ted;
    }

    public int getProtocolIdentifier() {
        return this.mProtocolIdentifier;
    }

    /* Access modifiers changed, original: 0000 */
    public int getDataCodingScheme() {
        return this.mDataCodingScheme;
    }

    public boolean isReplace() {
        int i = this.mProtocolIdentifier;
        return (i & 192) == 64 && (i & 63) > 0 && (i & 63) < 8;
    }

    public boolean isCphsMwiMessage() {
        return ((GsmSmsAddress) this.mOriginatingAddress).isCphsVoiceMessageClear() || ((GsmSmsAddress) this.mOriginatingAddress).isCphsVoiceMessageSet();
    }

    public boolean isMWIClearMessage() {
        boolean z = true;
        if (this.mIsMwi && !this.mMwiSense) {
            return true;
        }
        if (this.mOriginatingAddress == null || !((GsmSmsAddress) this.mOriginatingAddress).isCphsVoiceMessageClear()) {
            z = false;
        }
        return z;
    }

    public boolean isMWISetMessage() {
        boolean z = true;
        if (this.mIsMwi && this.mMwiSense) {
            return true;
        }
        if (this.mOriginatingAddress == null || !((GsmSmsAddress) this.mOriginatingAddress).isCphsVoiceMessageSet()) {
            z = false;
        }
        return z;
    }

    public boolean isMwiDontStore() {
        if (this.mIsMwi && this.mMwiDontStore) {
            return true;
        }
        if (isCphsMwiMessage()) {
            if (WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER.equals(getMessageBody())) {
                return true;
            }
        }
        return false;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public boolean isStatusReportMessage() {
        return this.mIsStatusReportMessage;
    }

    public boolean isReplyPathPresent() {
        return this.mReplyPathPresent;
    }

    private void parsePdu(byte[] pdu) {
        this.mPdu = pdu;
        PduParser p = new PduParser(pdu);
        this.mScAddress = p.getSCAddress();
        String str = this.mScAddress;
        int firstByte = p.getByte();
        this.mMti = firstByte & 3;
        int i = this.mMti;
        if (i != 0) {
            if (i == 1) {
                parseSmsSubmit(p, firstByte);
                return;
            } else if (i == 2) {
                parseSmsStatusReport(p, firstByte);
                return;
            } else if (i != 3) {
                throw new RuntimeException("Unsupported message type");
            }
        }
        parseSmsDeliver(p, firstByte);
    }

    private void parseSmsStatusReport(PduParser p, int firstByte) {
        boolean hasUserDataHeader = true;
        this.mIsStatusReportMessage = true;
        this.mMessageRef = p.getByte();
        this.mRecipientAddress = p.getAddress();
        this.mScTimeMillis = p.getSCTimestampMillis();
        p.getSCTimestampMillis();
        this.mStatus = p.getByte();
        if (p.moreDataPresent()) {
            int extraParams = p.getByte();
            int moreExtraParams = extraParams;
            while ((moreExtraParams & 128) != 0) {
                moreExtraParams = p.getByte();
            }
            if ((extraParams & 120) == 0) {
                if ((extraParams & 1) != 0) {
                    this.mProtocolIdentifier = p.getByte();
                }
                if ((extraParams & 2) != 0) {
                    this.mDataCodingScheme = p.getByte();
                }
                if ((extraParams & 4) != 0) {
                    if ((firstByte & 64) != 64) {
                        hasUserDataHeader = false;
                    }
                    parseUserData(p, hasUserDataHeader);
                }
            }
        }
    }

    private void parseSmsDeliver(PduParser p, int firstByte) {
        boolean z = true;
        this.mReplyPathPresent = (firstByte & 128) == 128;
        this.mOriginatingAddress = p.getAddress();
        SmsAddress smsAddress = this.mOriginatingAddress;
        this.mProtocolIdentifier = p.getByte();
        this.mDataCodingScheme = p.getByte();
        this.mScTimeMillis = p.getSCTimestampMillis();
        if ((firstByte & 64) != 64) {
            z = false;
        }
        parseUserData(p, z);
    }

    private void parseSmsSubmit(PduParser p, int firstByte) {
        int validityPeriodLength;
        boolean z = true;
        this.mReplyPathPresent = (firstByte & 128) == 128;
        this.mMessageRef = p.getByte();
        this.mRecipientAddress = p.getAddress();
        SmsAddress smsAddress = this.mRecipientAddress;
        this.mProtocolIdentifier = p.getByte();
        this.mDataCodingScheme = p.getByte();
        int validityPeriodFormat = (firstByte >> 3) & 3;
        if (validityPeriodFormat == 0) {
            validityPeriodLength = 0;
        } else if (2 == validityPeriodFormat) {
            validityPeriodLength = 1;
        } else {
            validityPeriodLength = 7;
        }
        while (true) {
            int validityPeriodLength2 = validityPeriodLength - 1;
            if (validityPeriodLength <= 0) {
                break;
            }
            p.getByte();
            validityPeriodLength = validityPeriodLength2;
        }
        if ((firstByte & 64) != 64) {
            z = false;
        }
        parseUserData(p, z);
    }

    /* JADX WARNING: Missing block: B:17:0x0054, code skipped:
            if (r6 != 3) goto L_0x015b;
     */
    private void parseUserData(com.android.internal.telephony.gsm.SmsMessage.PduParser r18, boolean r19) {
        /*
        r17 = this;
        r0 = r17;
        r1 = r18;
        r2 = r19;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = r0.mDataCodingScheme;
        r7 = r6 & 128;
        r8 = 17891527; // 0x11100c7 float:2.6632852E-38 double:8.839589E-317;
        r9 = 208; // 0xd0 float:2.91E-43 double:1.03E-321;
        r10 = 4;
        r11 = 224; // 0xe0 float:3.14E-43 double:1.107E-321;
        r12 = 2;
        r14 = 3;
        r15 = "SmsMessage";
        r13 = 1;
        if (r7 != 0) goto L_0x0083;
    L_0x001c:
        r6 = r6 & 32;
        if (r6 == 0) goto L_0x0022;
    L_0x0020:
        r6 = r13;
        goto L_0x0023;
    L_0x0022:
        r6 = 0;
    L_0x0023:
        r4 = r6;
        r6 = r0.mDataCodingScheme;
        r6 = r6 & 16;
        if (r6 == 0) goto L_0x002c;
    L_0x002a:
        r6 = r13;
        goto L_0x002d;
    L_0x002c:
        r6 = 0;
    L_0x002d:
        r3 = r6;
        if (r4 == 0) goto L_0x004a;
    L_0x0030:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "4 - Unsupported SMS data coding scheme (compression) ";
        r6.append(r7);
        r7 = r0.mDataCodingScheme;
        r7 = r7 & 255;
        r6.append(r7);
        r6 = r6.toString();
        android.telephony.Rlog.w(r15, r6);
        goto L_0x015b;
    L_0x004a:
        r6 = r0.mDataCodingScheme;
        r6 = r6 >> r12;
        r6 = r6 & r14;
        if (r6 == 0) goto L_0x007f;
    L_0x0050:
        if (r6 == r13) goto L_0x0059;
    L_0x0052:
        if (r6 == r12) goto L_0x0057;
    L_0x0054:
        if (r6 == r14) goto L_0x0065;
    L_0x0056:
        goto L_0x0081;
    L_0x0057:
        r5 = 3;
        goto L_0x0081;
    L_0x0059:
        r6 = android.content.res.Resources.getSystem();
        r7 = r6.getBoolean(r8);
        if (r7 == 0) goto L_0x0065;
    L_0x0063:
        r5 = 2;
        goto L_0x0081;
    L_0x0065:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "1 - Unsupported SMS data coding scheme ";
        r6.append(r7);
        r7 = r0.mDataCodingScheme;
        r7 = r7 & 255;
        r6.append(r7);
        r6 = r6.toString();
        android.telephony.Rlog.w(r15, r6);
        r5 = 2;
        goto L_0x0081;
    L_0x007f:
        r5 = 1;
    L_0x0081:
        goto L_0x015b;
    L_0x0083:
        r7 = r6 & 240;
        r8 = 240; // 0xf0 float:3.36E-43 double:1.186E-321;
        if (r7 != r8) goto L_0x0094;
    L_0x0089:
        r3 = 1;
        r4 = 0;
        r6 = r6 & r10;
        if (r6 != 0) goto L_0x0091;
    L_0x008e:
        r5 = 1;
        goto L_0x015b;
    L_0x0091:
        r5 = 2;
        goto L_0x015b;
    L_0x0094:
        r7 = r6 & 240;
        r10 = 192; // 0xc0 float:2.69E-43 double:9.5E-322;
        if (r7 == r10) goto L_0x00e4;
    L_0x009a:
        r7 = r6 & 240;
        if (r7 == r9) goto L_0x00e4;
    L_0x009e:
        r7 = r6 & 240;
        if (r7 != r11) goto L_0x00a3;
    L_0x00a2:
        goto L_0x00e4;
    L_0x00a3:
        r7 = r6 & 192;
        r8 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r7 != r8) goto L_0x00ca;
    L_0x00a9:
        r7 = 132; // 0x84 float:1.85E-43 double:6.5E-322;
        if (r6 != r7) goto L_0x00b0;
    L_0x00ad:
        r5 = 4;
        goto L_0x015b;
    L_0x00b0:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "5 - Unsupported SMS data coding scheme ";
        r6.append(r7);
        r7 = r0.mDataCodingScheme;
        r7 = r7 & 255;
        r6.append(r7);
        r6 = r6.toString();
        android.telephony.Rlog.w(r15, r6);
        goto L_0x015b;
    L_0x00ca:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "3 - Unsupported SMS data coding scheme ";
        r6.append(r7);
        r7 = r0.mDataCodingScheme;
        r7 = r7 & 255;
        r6.append(r7);
        r6 = r6.toString();
        android.telephony.Rlog.w(r15, r6);
        goto L_0x015b;
    L_0x00e4:
        r6 = r0.mDataCodingScheme;
        r6 = r6 & r8;
        if (r6 != r11) goto L_0x00eb;
    L_0x00e9:
        r5 = 3;
        goto L_0x00ec;
    L_0x00eb:
        r5 = 1;
    L_0x00ec:
        r4 = 0;
        r6 = r0.mDataCodingScheme;
        r7 = 8;
        r6 = r6 & r7;
        if (r6 != r7) goto L_0x00f6;
    L_0x00f4:
        r6 = r13;
        goto L_0x00f7;
    L_0x00f6:
        r6 = 0;
    L_0x00f7:
        r7 = r0.mDataCodingScheme;
        r16 = r7 & 3;
        if (r16 != 0) goto L_0x013f;
    L_0x00fd:
        r0.mIsMwi = r13;
        r0.mMwiSense = r6;
        r7 = r7 & r8;
        if (r7 != r10) goto L_0x0106;
    L_0x0104:
        r7 = r13;
        goto L_0x0107;
    L_0x0106:
        r7 = 0;
    L_0x0107:
        r0.mMwiDontStore = r7;
        if (r6 != r13) goto L_0x010f;
    L_0x010b:
        r7 = -1;
        r0.mVoiceMailCount = r7;
        goto L_0x0112;
    L_0x010f:
        r7 = 0;
        r0.mVoiceMailCount = r7;
    L_0x0112:
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "MWI in DCS for Vmail. DCS = ";
        r7.append(r8);
        r8 = r0.mDataCodingScheme;
        r8 = r8 & 255;
        r7.append(r8);
        r8 = " Dont store = ";
        r7.append(r8);
        r8 = r0.mMwiDontStore;
        r7.append(r8);
        r8 = " vmail count = ";
        r7.append(r8);
        r8 = r0.mVoiceMailCount;
        r7.append(r8);
        r7 = r7.toString();
        android.telephony.Rlog.w(r15, r7);
        goto L_0x015a;
    L_0x013f:
        r7 = 0;
        r0.mIsMwi = r7;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "MWI in DCS for fax/email/other: ";
        r7.append(r8);
        r8 = r0.mDataCodingScheme;
        r8 = r8 & 255;
        r7.append(r8);
        r7 = r7.toString();
        android.telephony.Rlog.w(r15, r7);
    L_0x015b:
        if (r5 != r13) goto L_0x015f;
    L_0x015d:
        r6 = r13;
        goto L_0x0160;
    L_0x015f:
        r6 = 0;
    L_0x0160:
        r6 = r1.constructUserData(r2, r6);
        r7 = r18.getUserData();
        r0.mUserData = r7;
        r7 = r18.getUserDataHeader();
        r0.mUserDataHeader = r7;
        if (r2 == 0) goto L_0x0212;
    L_0x0172:
        r7 = r0.mUserDataHeader;
        r7 = r7.specialSmsMsgList;
        r7 = r7.size();
        if (r7 == 0) goto L_0x0212;
    L_0x017c:
        r7 = r0.mUserDataHeader;
        r7 = r7.specialSmsMsgList;
        r7 = r7.iterator();
    L_0x0184:
        r8 = r7.hasNext();
        if (r8 == 0) goto L_0x0210;
    L_0x018a:
        r8 = r7.next();
        r8 = (com.android.internal.telephony.SmsHeader.SpecialSmsMsg) r8;
        r10 = r8.msgIndType;
        r10 = r10 & 255;
        if (r10 == 0) goto L_0x01b1;
    L_0x0196:
        r12 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r10 != r12) goto L_0x019b;
    L_0x019a:
        goto L_0x01b1;
    L_0x019b:
        r12 = new java.lang.StringBuilder;
        r12.<init>();
        r14 = "TP_UDH fax/email/extended msg/multisubscriber profile. Msg Ind = ";
        r12.append(r14);
        r12.append(r10);
        r12 = r12.toString();
        android.telephony.Rlog.w(r15, r12);
        r12 = 0;
        goto L_0x020a;
    L_0x01b1:
        r0.mIsMwi = r13;
        r12 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r10 != r12) goto L_0x01bb;
    L_0x01b7:
        r14 = 0;
        r0.mMwiDontStore = r14;
        goto L_0x01d1;
    L_0x01bb:
        r14 = r0.mMwiDontStore;
        if (r14 != 0) goto L_0x01d1;
    L_0x01bf:
        r14 = r0.mDataCodingScheme;
        r12 = r14 & 240;
        if (r12 == r9) goto L_0x01c9;
    L_0x01c5:
        r12 = r14 & 240;
        if (r12 != r11) goto L_0x01cf;
    L_0x01c9:
        r12 = r0.mDataCodingScheme;
        r14 = 3;
        r12 = r12 & r14;
        if (r12 == 0) goto L_0x01d1;
    L_0x01cf:
        r0.mMwiDontStore = r13;
    L_0x01d1:
        r12 = r8.msgCount;
        r12 = r12 & 255;
        r0.mVoiceMailCount = r12;
        r12 = r0.mVoiceMailCount;
        if (r12 <= 0) goto L_0x01df;
    L_0x01db:
        r0.mMwiSense = r13;
        r12 = 0;
        goto L_0x01e2;
    L_0x01df:
        r12 = 0;
        r0.mMwiSense = r12;
    L_0x01e2:
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r9 = "MWI in TP-UDH for Vmail. Msg Ind = ";
        r14.append(r9);
        r14.append(r10);
        r9 = " Dont store = ";
        r14.append(r9);
        r9 = r0.mMwiDontStore;
        r14.append(r9);
        r9 = " Vmail count = ";
        r14.append(r9);
        r9 = r0.mVoiceMailCount;
        r14.append(r9);
        r9 = r14.toString();
        android.telephony.Rlog.w(r15, r9);
    L_0x020a:
        r9 = 208; // 0xd0 float:2.91E-43 double:1.03E-321;
        r12 = 2;
        r14 = 3;
        goto L_0x0184;
    L_0x0210:
        r12 = 0;
        goto L_0x0213;
    L_0x0212:
        r12 = 0;
    L_0x0213:
        r7 = 0;
        if (r5 == 0) goto L_0x025f;
    L_0x0216:
        if (r5 == r13) goto L_0x0247;
    L_0x0218:
        r8 = 2;
        if (r5 == r8) goto L_0x0230;
    L_0x021b:
        r8 = 3;
        if (r5 == r8) goto L_0x0229;
    L_0x021e:
        r7 = 4;
        if (r5 == r7) goto L_0x0222;
    L_0x0221:
        goto L_0x0262;
    L_0x0222:
        r7 = r1.getUserDataKSC5601(r6);
        r0.mMessageBody = r7;
        goto L_0x0262;
    L_0x0229:
        r7 = r1.getUserDataUCS2(r6);
        r0.mMessageBody = r7;
        goto L_0x0262;
    L_0x0230:
        r8 = android.content.res.Resources.getSystem();
        r9 = 17891527; // 0x11100c7 float:2.6632852E-38 double:8.839589E-317;
        r9 = r8.getBoolean(r9);
        if (r9 == 0) goto L_0x0244;
    L_0x023d:
        r7 = r1.getUserDataGSM8bit(r6);
        r0.mMessageBody = r7;
        goto L_0x0262;
    L_0x0244:
        r0.mMessageBody = r7;
        goto L_0x0262;
        if (r2 == 0) goto L_0x024f;
    L_0x024a:
        r7 = r0.mUserDataHeader;
        r7 = r7.languageTable;
        goto L_0x0250;
    L_0x024f:
        r7 = r12;
    L_0x0250:
        if (r2 == 0) goto L_0x0257;
    L_0x0252:
        r8 = r0.mUserDataHeader;
        r8 = r8.languageShiftTable;
        goto L_0x0258;
    L_0x0257:
        r8 = r12;
    L_0x0258:
        r7 = r1.getUserDataGSM7Bit(r6, r7, r8);
        r0.mMessageBody = r7;
        goto L_0x0262;
    L_0x025f:
        r0.mMessageBody = r7;
    L_0x0262:
        r7 = r0.mMessageBody;
        if (r7 == 0) goto L_0x0269;
    L_0x0266:
        r17.parseMessageBody();
    L_0x0269:
        if (r3 != 0) goto L_0x0270;
    L_0x026b:
        r7 = com.android.internal.telephony.SmsConstants.MessageClass.UNKNOWN;
        r0.messageClass = r7;
        goto L_0x0292;
    L_0x0270:
        r7 = r0.mDataCodingScheme;
        r8 = 3;
        r7 = r7 & r8;
        if (r7 == 0) goto L_0x028d;
    L_0x0276:
        if (r7 == r13) goto L_0x0288;
    L_0x0278:
        r9 = 2;
        if (r7 == r9) goto L_0x0283;
    L_0x027b:
        if (r7 == r8) goto L_0x027e;
    L_0x027d:
        goto L_0x0292;
    L_0x027e:
        r7 = com.android.internal.telephony.SmsConstants.MessageClass.CLASS_3;
        r0.messageClass = r7;
        goto L_0x0292;
    L_0x0283:
        r7 = com.android.internal.telephony.SmsConstants.MessageClass.CLASS_2;
        r0.messageClass = r7;
        goto L_0x0292;
    L_0x0288:
        r7 = com.android.internal.telephony.SmsConstants.MessageClass.CLASS_1;
        r0.messageClass = r7;
        goto L_0x0292;
    L_0x028d:
        r7 = com.android.internal.telephony.SmsConstants.MessageClass.CLASS_0;
        r0.messageClass = r7;
    L_0x0292:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.telephony.gsm.SmsMessage.parseUserData(com.android.internal.telephony.gsm.SmsMessage$PduParser, boolean):void");
    }

    public MessageClass getMessageClass() {
        return this.messageClass;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isUsimDataDownload() {
        if (this.messageClass == MessageClass.CLASS_2) {
            int i = this.mProtocolIdentifier;
            if (i == 127 || i == 124) {
                return true;
            }
        }
        return false;
    }

    public int getNumOfVoicemails() {
        if (!this.mIsMwi && isCphsMwiMessage()) {
            if (this.mOriginatingAddress == null || !((GsmSmsAddress) this.mOriginatingAddress).isCphsVoiceMessageSet()) {
                this.mVoiceMailCount = 0;
            } else {
                this.mVoiceMailCount = 255;
            }
            Rlog.v(LOG_TAG, "CPHS voice mail message");
        }
        return this.mVoiceMailCount;
    }
}
