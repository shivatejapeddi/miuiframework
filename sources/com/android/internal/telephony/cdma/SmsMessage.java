package com.android.internal.telephony.cdma;

import android.content.res.Resources;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SmsCbLocation;
import android.telephony.SmsCbMessage;
import android.telephony.cdma.CdmaSmsCbProgramData;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.R;
import com.android.internal.telephony.GsmAlphabet.TextEncodingDetails;
import com.android.internal.telephony.Sms7BitEncodingTranslator;
import com.android.internal.telephony.SmsAddress;
import com.android.internal.telephony.SmsConstants.MessageClass;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsHeader.PortAddrs;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsMessageBase.SubmitPduBase;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.cdma.sms.BearerData;
import com.android.internal.telephony.cdma.sms.CdmaSmsAddress;
import com.android.internal.telephony.cdma.sms.CdmaSmsSubaddress;
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.BitwiseInputStream;
import com.android.internal.util.BitwiseInputStream.AccessException;
import com.android.internal.util.HexDump;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SmsMessage extends SmsMessageBase {
    private static final byte BEARER_DATA = (byte) 8;
    private static final byte BEARER_REPLY_OPTION = (byte) 6;
    private static final byte CAUSE_CODES = (byte) 7;
    private static final byte DESTINATION_ADDRESS = (byte) 4;
    private static final byte DESTINATION_SUB_ADDRESS = (byte) 5;
    private static final String LOGGABLE_TAG = "CDMA:SMS";
    static final String LOG_TAG = "SmsMessage";
    private static final byte ORIGINATING_ADDRESS = (byte) 2;
    private static final byte ORIGINATING_SUB_ADDRESS = (byte) 3;
    private static final int PRIORITY_EMERGENCY = 3;
    private static final int PRIORITY_INTERACTIVE = 1;
    private static final int PRIORITY_NORMAL = 0;
    private static final int PRIORITY_URGENT = 2;
    private static final int RETURN_ACK = 1;
    private static final int RETURN_NO_ACK = 0;
    private static final byte SERVICE_CATEGORY = (byte) 1;
    private static final byte TELESERVICE_IDENTIFIER = (byte) 0;
    private static final boolean VDBG = false;
    private BearerData mBearerData;
    private SmsEnvelope mEnvelope;
    private int status;

    public static class SubmitPdu extends SubmitPduBase {
    }

    public SmsMessage(SmsAddress addr, SmsEnvelope env) {
        this.mOriginatingAddress = addr;
        this.mEnvelope = env;
        createPdu();
    }

    public static SmsMessage createFromPdu(byte[] pdu) {
        String str = LOG_TAG;
        SmsMessage msg = new SmsMessage();
        try {
            msg.parsePdu(pdu);
            return msg;
        } catch (RuntimeException ex) {
            Rlog.e(str, "SMS PDU parsing failed: ", ex);
            return null;
        } catch (OutOfMemoryError e) {
            Log.e(str, "SMS PDU parsing failed with out of memory: ", e);
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
            int size = data[1] & 255;
            byte[] pdu = new byte[size];
            System.arraycopy(data, 2, pdu, 0, size);
            msg.parsePduFromEfRecord(pdu);
            return msg;
        } catch (RuntimeException ex) {
            Rlog.e(str, "SMS PDU parsing failed: ", ex);
            return null;
        }
    }

    public static int getTPLayerLengthForPDU(String pdu) {
        Rlog.w(LOG_TAG, "getTPLayerLengthForPDU: is not supported in CDMA mode.");
        return 0;
    }

    public static SubmitPdu getSubmitPdu(String scAddr, String destAddr, String message, boolean statusReportRequested, SmsHeader smsHeader) {
        return getSubmitPdu(scAddr, destAddr, message, statusReportRequested, smsHeader, -1);
    }

    public static SubmitPdu getSubmitPdu(String scAddr, String destAddr, String message, boolean statusReportRequested, SmsHeader smsHeader, int priority) {
        if (message == null || destAddr == null) {
            return null;
        }
        UserData uData = new UserData();
        uData.payloadStr = message;
        uData.userDataHeader = smsHeader;
        return privateGetSubmitPdu(destAddr, statusReportRequested, uData, priority);
    }

    public static SubmitPdu getSubmitPdu(String scAddr, String destAddr, int destPort, byte[] data, boolean statusReportRequested) {
        PortAddrs portAddrs = new PortAddrs();
        portAddrs.destPort = destPort;
        portAddrs.origPort = 0;
        portAddrs.areEightBits = false;
        SmsHeader smsHeader = new SmsHeader();
        smsHeader.portAddrs = portAddrs;
        UserData uData = new UserData();
        uData.userDataHeader = smsHeader;
        uData.msgEncoding = 0;
        uData.msgEncodingSet = true;
        uData.payload = data;
        return privateGetSubmitPdu(destAddr, statusReportRequested, uData);
    }

    public static SubmitPdu getSubmitPdu(String destAddr, UserData userData, boolean statusReportRequested) {
        return privateGetSubmitPdu(destAddr, statusReportRequested, userData);
    }

    public static SubmitPdu getSubmitPdu(String destAddr, UserData userData, boolean statusReportRequested, int priority) {
        return privateGetSubmitPdu(destAddr, statusReportRequested, userData, priority);
    }

    public int getProtocolIdentifier() {
        Rlog.w(LOG_TAG, "getProtocolIdentifier: is not supported in CDMA mode.");
        return 0;
    }

    public boolean isReplace() {
        Rlog.w(LOG_TAG, "isReplace: is not supported in CDMA mode.");
        return false;
    }

    public boolean isCphsMwiMessage() {
        Rlog.w(LOG_TAG, "isCphsMwiMessage: is not supported in CDMA mode.");
        return false;
    }

    public boolean isMWIClearMessage() {
        BearerData bearerData = this.mBearerData;
        return bearerData != null && bearerData.numberOfMessages == 0;
    }

    public boolean isMWISetMessage() {
        BearerData bearerData = this.mBearerData;
        return bearerData != null && bearerData.numberOfMessages > 0;
    }

    public boolean isMwiDontStore() {
        BearerData bearerData = this.mBearerData;
        return bearerData != null && bearerData.numberOfMessages > 0 && this.mBearerData.userData == null;
    }

    public int getStatus() {
        return this.status << 16;
    }

    public boolean isStatusReportMessage() {
        return this.mBearerData.messageType == 4;
    }

    public boolean isReplyPathPresent() {
        Rlog.w(LOG_TAG, "isReplyPathPresent: is not supported in CDMA mode.");
        return false;
    }

    public static TextEncodingDetails calculateLength(CharSequence messageBody, boolean use7bitOnly, boolean isEntireMsg) {
        CharSequence newMsgBody = null;
        if (Resources.getSystem().getBoolean(R.bool.config_sms_force_7bit_encoding)) {
            newMsgBody = Sms7BitEncodingTranslator.translate(messageBody, true);
        }
        if (TextUtils.isEmpty(newMsgBody)) {
            newMsgBody = messageBody;
        }
        return BearerData.calcTextEncodingDetails(newMsgBody, use7bitOnly, isEntireMsg);
    }

    public int getTeleService() {
        return this.mEnvelope.teleService;
    }

    public int getMessageType() {
        if (this.mEnvelope.serviceCategory != 0) {
            return 1;
        }
        return 0;
    }

    private void parsePdu(byte[] pdu) {
        String str = "createFromPdu: conversion from byte array to object failed: ";
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(pdu));
        SmsEnvelope env = new SmsEnvelope();
        CdmaSmsAddress addr = new CdmaSmsAddress();
        CdmaSmsSubaddress subaddr = new CdmaSmsSubaddress();
        try {
            env.messageType = dis.readInt();
            env.teleService = dis.readInt();
            env.serviceCategory = dis.readInt();
            addr.digitMode = dis.readByte();
            addr.numberMode = dis.readByte();
            addr.ton = dis.readByte();
            addr.numberPlan = dis.readByte();
            int length = dis.readUnsignedByte();
            addr.numberOfDigits = length;
            String str2 = " > pdu len ";
            if (length <= pdu.length) {
                addr.origBytes = new byte[length];
                dis.read(addr.origBytes, 0, length);
                env.bearerReply = dis.readInt();
                env.replySeqNo = dis.readByte();
                env.errorClass = dis.readByte();
                env.causeCode = dis.readByte();
                int bearerDataLength = dis.readInt();
                if (bearerDataLength <= pdu.length) {
                    env.bearerData = new byte[bearerDataLength];
                    dis.read(env.bearerData, 0, bearerDataLength);
                    dis.close();
                    this.mOriginatingAddress = addr;
                    env.origAddress = addr;
                    env.origSubaddress = subaddr;
                    this.mEnvelope = env;
                    this.mPdu = pdu;
                    parseSms();
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("createFromPdu: Invalid pdu, bearerDataLength ");
                stringBuilder.append(bearerDataLength);
                stringBuilder.append(str2);
                stringBuilder.append(pdu.length);
                throw new RuntimeException(stringBuilder.toString());
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("createFromPdu: Invalid pdu, addr.numberOfDigits ");
            stringBuilder2.append(length);
            stringBuilder2.append(str2);
            stringBuilder2.append(pdu.length);
            throw new RuntimeException(stringBuilder2.toString());
        } catch (IOException ex) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(str);
            stringBuilder3.append(ex);
            throw new RuntimeException(stringBuilder3.toString(), ex);
        } catch (Exception ex2) {
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(str);
            stringBuilder4.append(ex2);
            Rlog.e(LOG_TAG, stringBuilder4.toString());
        }
    }

    private void parsePduFromEfRecord(byte[] pdu) {
        byte[] bArr = pdu;
        String str = LOG_TAG;
        ByteArrayInputStream bais = new ByteArrayInputStream(bArr);
        DataInputStream dis = new DataInputStream(bais);
        SmsEnvelope env = new SmsEnvelope();
        CdmaSmsAddress addr = new CdmaSmsAddress();
        CdmaSmsSubaddress subAddr = new CdmaSmsSubaddress();
        StringBuilder stringBuilder;
        try {
            env.messageType = dis.readByte();
            while (dis.available() > 0) {
                int parameterId = dis.readByte();
                int parameterLen = dis.readUnsignedByte();
                byte[] parameterData = new byte[parameterLen];
                byte[] bArr2;
                int numberType;
                switch (parameterId) {
                    case 0:
                        bArr2 = parameterData;
                        env.teleService = dis.readUnsignedShort();
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("teleservice = ");
                        stringBuilder.append(env.teleService);
                        Rlog.i(str, stringBuilder.toString());
                        break;
                    case 1:
                        bArr2 = parameterData;
                        env.serviceCategory = dis.readUnsignedShort();
                        break;
                    case 2:
                    case 4:
                        dis.read(parameterData, 0, parameterLen);
                        BitwiseInputStream addrBis = new BitwiseInputStream(parameterData);
                        addr.digitMode = addrBis.read(1);
                        addr.numberMode = addrBis.read(1);
                        numberType = 0;
                        if (addr.digitMode == 1) {
                            numberType = addrBis.read(3);
                            addr.ton = numberType;
                            if (addr.numberMode == 0) {
                                addr.numberPlan = addrBis.read(4);
                            }
                        }
                        addr.numberOfDigits = addrBis.read(8);
                        byte[] data = new byte[addr.numberOfDigits];
                        if (addr.digitMode == 0) {
                            int index = 0;
                            while (true) {
                                int parameterLen2 = parameterLen;
                                if (index < addr.numberOfDigits) {
                                    data[index] = convertDtmfToAscii((byte) (addrBis.read(4) & 15));
                                    index++;
                                    parameterLen = parameterLen2;
                                } else {
                                    bArr2 = parameterData;
                                }
                            }
                        } else {
                            if (addr.digitMode != 1) {
                                Rlog.e(str, "Incorrect Digit mode");
                            } else if (addr.numberMode == 0) {
                                parameterLen = 0;
                                while (parameterLen < addr.numberOfDigits) {
                                    bArr2 = parameterData;
                                    data[parameterLen] = (byte) (addrBis.read(8) & 255);
                                    parameterLen++;
                                    parameterData = bArr2;
                                }
                            } else {
                                if (addr.numberMode != 1) {
                                    Rlog.e(str, "Addr is of incorrect type");
                                } else if (numberType == 2) {
                                    Rlog.e(str, "TODO: Addr is email id");
                                } else {
                                    Rlog.e(str, "TODO: Addr is data network address");
                                }
                            }
                        }
                        addr.origBytes = data;
                        parameterLen = new StringBuilder();
                        parameterLen.append("Addr=");
                        parameterLen.append(addr.toString());
                        Rlog.pii(str, parameterLen.toString());
                        this.mOriginatingAddress = addr;
                        if (parameterId != 4) {
                            break;
                        }
                        env.destAddress = addr;
                        this.mRecipientAddress = addr;
                        break;
                    case 3:
                    case 5:
                        dis.read(parameterData, 0, parameterLen);
                        BitwiseInputStream subAddrBis = new BitwiseInputStream(parameterData);
                        subAddr.type = subAddrBis.read(3);
                        subAddr.odd = subAddrBis.readByteArray(1)[0];
                        int subAddrLen = subAddrBis.read(8);
                        byte[] subdata = new byte[subAddrLen];
                        numberType = 0;
                        while (numberType < subAddrLen) {
                            int subAddrLen2 = subAddrLen;
                            subdata[numberType] = convertDtmfToAscii((byte) (subAddrBis.read(4) & 255));
                            numberType++;
                            subAddrLen = subAddrLen2;
                        }
                        subAddr.origBytes = subdata;
                        break;
                    case 6:
                        dis.read(parameterData, 0, parameterLen);
                        env.bearerReply = new BitwiseInputStream(parameterData).read(6);
                        break;
                    case 7:
                        dis.read(parameterData, 0, parameterLen);
                        BitwiseInputStream ccBis = new BitwiseInputStream(parameterData);
                        env.replySeqNo = ccBis.readByteArray(6)[0];
                        env.errorClass = ccBis.readByteArray(2)[0];
                        if (env.errorClass == (byte) 0) {
                            break;
                        }
                        env.causeCode = ccBis.readByteArray(8)[0];
                        break;
                    case 8:
                        dis.read(parameterData, 0, parameterLen);
                        env.bearerData = parameterData;
                        break;
                    default:
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("unsupported parameterId (");
                        stringBuilder2.append(parameterId);
                        stringBuilder2.append(")");
                        throw new Exception(stringBuilder2.toString());
                }
            }
            bais.close();
            dis.close();
        } catch (Exception ex) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("parsePduFromEfRecord: conversion from pdu to SmsMessage failed");
            stringBuilder.append(ex);
            Rlog.e(str, stringBuilder.toString());
        }
        this.mOriginatingAddress = addr;
        env.origAddress = addr;
        env.origSubaddress = subAddr;
        this.mEnvelope = env;
        this.mPdu = bArr;
        parseSms();
    }

    public void parseSms() {
        if (this.mEnvelope.teleService == 262144) {
            this.mBearerData = new BearerData();
            if (this.mEnvelope.bearerData != null) {
                this.mBearerData.numberOfMessages = this.mEnvelope.bearerData[0] & 255;
            }
            return;
        }
        this.mBearerData = BearerData.decode(this.mEnvelope.bearerData);
        boolean isLoggable = Rlog.isLoggable(LOGGABLE_TAG, 2);
        String str = LOG_TAG;
        if (isLoggable) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MT raw BearerData = '");
            stringBuilder.append(HexDump.toHexString(this.mEnvelope.bearerData));
            stringBuilder.append("'");
            Rlog.d(str, stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("MT (decoded) BearerData = ");
            stringBuilder.append(this.mBearerData);
            Rlog.d(str, stringBuilder.toString());
        }
        this.mMessageRef = this.mBearerData.messageId;
        if (this.mBearerData.userData != null) {
            this.mUserData = this.mBearerData.userData.payload;
            this.mUserDataHeader = this.mBearerData.userData.userDataHeader;
            this.mMessageBody = this.mBearerData.userData.payloadStr;
        }
        if (this.mOriginatingAddress != null) {
            decodeSmsDisplayAddress(this.mOriginatingAddress);
        }
        if (this.mRecipientAddress != null) {
            decodeSmsDisplayAddress(this.mRecipientAddress);
        }
        if (this.mBearerData.msgCenterTimeStamp != null) {
            this.mScTimeMillis = this.mBearerData.msgCenterTimeStamp.toMillis(true);
        }
        if (this.mBearerData.messageType == 4) {
            if (this.mBearerData.messageStatusSet) {
                this.status = this.mBearerData.errorClass << 8;
                this.status |= this.mBearerData.messageStatus;
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("DELIVERY_ACK message without msgStatus (");
                stringBuilder2.append(this.mUserData == null ? "also missing" : "does have");
                stringBuilder2.append(" userData).");
                Rlog.d(str, stringBuilder2.toString());
                this.status = 0;
            }
        } else if (!(this.mBearerData.messageType == 1 || this.mBearerData.messageType == 2)) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Unsupported message type: ");
            stringBuilder3.append(this.mBearerData.messageType);
            throw new RuntimeException(stringBuilder3.toString());
        }
        if (this.mMessageBody != null) {
            parseMessageBody();
        } else {
            byte[] bArr = this.mUserData;
        }
    }

    private void decodeSmsDisplayAddress(SmsAddress addr) {
        StringBuilder stringBuilder;
        addr.address = new String(addr.origBytes);
        if (addr.ton == 1 && addr.address.charAt(0) != '+') {
            stringBuilder = new StringBuilder();
            stringBuilder.append("+");
            stringBuilder.append(addr.address);
            addr.address = stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(" decodeSmsDisplayAddress = ");
        stringBuilder.append(addr.address);
        Rlog.pii(LOG_TAG, stringBuilder.toString());
    }

    public SmsCbMessage parseBroadcastSms(String plmn) {
        BearerData bData = BearerData.decode(this.mEnvelope.bearerData, this.mEnvelope.serviceCategory);
        String str = LOG_TAG;
        if (bData == null) {
            Rlog.w(str, "BearerData.decode() returned null");
            return null;
        }
        if (Rlog.isLoggable(LOGGABLE_TAG, 2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MT raw BearerData = ");
            stringBuilder.append(HexDump.toHexString(this.mEnvelope.bearerData));
            Rlog.d(str, stringBuilder.toString());
        }
        return new SmsCbMessage(2, 1, bData.messageId, new SmsCbLocation(plmn), this.mEnvelope.serviceCategory, bData.getLanguage(), bData.userData.payloadStr, bData.priority, null, bData.cmasWarningInfo);
    }

    public MessageClass getMessageClass() {
        if (this.mBearerData.displayMode == 0) {
            return MessageClass.CLASS_0;
        }
        return MessageClass.UNKNOWN;
    }

    public static synchronized int getNextMessageId() {
        int msgId;
        synchronized (SmsMessage.class) {
            msgId = SystemProperties.getInt(TelephonyProperties.PROPERTY_CDMA_MSG_ID, 1);
            String nextMsgId = Integer.toString((msgId % 65535) + 1);
            try {
                SystemProperties.set(TelephonyProperties.PROPERTY_CDMA_MSG_ID, nextMsgId);
                if (Rlog.isLoggable(LOGGABLE_TAG, 2)) {
                    String str = LOG_TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("next persist.radio.cdma.msgid = ");
                    stringBuilder.append(nextMsgId);
                    Rlog.d(str, stringBuilder.toString());
                    str = LOG_TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("readback gets ");
                    stringBuilder.append(SystemProperties.get(TelephonyProperties.PROPERTY_CDMA_MSG_ID));
                    Rlog.d(str, stringBuilder.toString());
                }
            } catch (RuntimeException ex) {
                String str2 = LOG_TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("set nextMessage ID failed: ");
                stringBuilder2.append(ex);
                Rlog.e(str2, stringBuilder2.toString());
            }
        }
        return msgId;
    }

    private static SubmitPdu privateGetSubmitPdu(String destAddrStr, boolean statusReportRequested, UserData userData) {
        return privateGetSubmitPdu(destAddrStr, statusReportRequested, userData, -1);
    }

    private static SubmitPdu privateGetSubmitPdu(String destAddrStr, boolean statusReportRequested, UserData userData, int priority) {
        CdmaSmsAddress destAddr = CdmaSmsAddress.parse(PhoneNumberUtils.cdmaCheckAndProcessPlusCodeForSms(destAddrStr));
        if (destAddr == null) {
            return null;
        }
        BearerData bearerData = new BearerData();
        bearerData.messageType = 2;
        bearerData.messageId = getNextMessageId();
        bearerData.deliveryAckReq = statusReportRequested;
        bearerData.userAckReq = false;
        bearerData.readAckReq = false;
        bearerData.reportReq = false;
        if (priority >= 0 && priority <= 3) {
            bearerData.priorityIndicatorSet = true;
            bearerData.priority = priority;
        }
        bearerData.userData = userData;
        byte[] encodedBearerData = BearerData.encode(bearerData);
        if (encodedBearerData == null) {
            return null;
        }
        boolean isLoggable = Rlog.isLoggable(LOGGABLE_TAG, 2);
        String str = LOG_TAG;
        if (isLoggable) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MO (encoded) BearerData = ");
            stringBuilder.append(bearerData);
            Rlog.d(str, stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("MO raw BearerData = '");
            stringBuilder.append(HexDump.toHexString(encodedBearerData));
            stringBuilder.append("'");
            Rlog.d(str, stringBuilder.toString());
        }
        int teleservice = (!bearerData.hasUserDataHeader || userData.msgEncoding == 2) ? 4098 : 4101;
        SmsEnvelope envelope = new SmsEnvelope();
        envelope.messageType = 0;
        envelope.teleService = teleservice;
        envelope.destAddress = destAddr;
        envelope.bearerReply = 1;
        envelope.bearerData = encodedBearerData;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(envelope.teleService);
            dos.writeInt(0);
            dos.writeInt(0);
            dos.write(destAddr.digitMode);
            dos.write(destAddr.numberMode);
            dos.write(destAddr.ton);
            dos.write(destAddr.numberPlan);
            dos.write(destAddr.numberOfDigits);
            dos.write(destAddr.origBytes, 0, destAddr.origBytes.length);
            dos.write(0);
            dos.write(0);
            dos.write(0);
            dos.write(encodedBearerData.length);
            dos.write(encodedBearerData, 0, encodedBearerData.length);
            dos.close();
            SubmitPdu pdu = new SubmitPdu();
            pdu.encodedMessage = baos.toByteArray();
            pdu.encodedScAddress = null;
            return pdu;
        } catch (IOException ex) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("creating SubmitPdu failed: ");
            stringBuilder2.append(ex);
            Rlog.e(str, stringBuilder2.toString());
            return null;
        }
    }

    public void createPdu() {
        SmsEnvelope env = this.mEnvelope;
        CdmaSmsAddress addr = env.origAddress;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(baos));
        try {
            dos.writeInt(env.messageType);
            dos.writeInt(env.teleService);
            dos.writeInt(env.serviceCategory);
            dos.writeByte(addr.digitMode);
            dos.writeByte(addr.numberMode);
            dos.writeByte(addr.ton);
            dos.writeByte(addr.numberPlan);
            dos.writeByte(addr.numberOfDigits);
            dos.write(addr.origBytes, 0, addr.origBytes.length);
            dos.writeInt(env.bearerReply);
            dos.writeByte(env.replySeqNo);
            dos.writeByte(env.errorClass);
            dos.writeByte(env.causeCode);
            dos.writeInt(env.bearerData.length);
            dos.write(env.bearerData, 0, env.bearerData.length);
            dos.close();
            this.mPdu = baos.toByteArray();
        } catch (IOException ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("createPdu: conversion from object to byte array failed: ");
            stringBuilder.append(ex);
            Rlog.e(LOG_TAG, stringBuilder.toString());
        }
    }

    public static byte convertDtmfToAscii(byte dtmfDigit) {
        switch (dtmfDigit) {
            case (byte) 0:
                return (byte) 68;
            case (byte) 1:
                return (byte) 49;
            case (byte) 2:
                return (byte) 50;
            case (byte) 3:
                return (byte) 51;
            case (byte) 4:
                return (byte) 52;
            case (byte) 5:
                return (byte) 53;
            case (byte) 6:
                return (byte) 54;
            case (byte) 7:
                return (byte) 55;
            case (byte) 8:
                return (byte) 56;
            case (byte) 9:
                return (byte) 57;
            case (byte) 10:
                return (byte) 48;
            case (byte) 11:
                return (byte) 42;
            case (byte) 12:
                return (byte) 35;
            case (byte) 13:
                return (byte) 65;
            case (byte) 14:
                return (byte) 66;
            case (byte) 15:
                return (byte) 67;
            default:
                return (byte) 32;
        }
    }

    public int getNumOfVoicemails() {
        return this.mBearerData.numberOfMessages;
    }

    public byte[] getIncomingSmsFingerprint() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(this.mEnvelope.serviceCategory);
        output.write(this.mEnvelope.teleService);
        output.write(this.mEnvelope.origAddress.origBytes, 0, this.mEnvelope.origAddress.origBytes.length);
        output.write(this.mEnvelope.bearerData, 0, this.mEnvelope.bearerData.length);
        if (!(this.mEnvelope.origSubaddress == null || this.mEnvelope.origSubaddress.origBytes == null)) {
            output.write(this.mEnvelope.origSubaddress.origBytes, 0, this.mEnvelope.origSubaddress.origBytes.length);
        }
        return output.toByteArray();
    }

    public ArrayList<CdmaSmsCbProgramData> getSmsCbProgramData() {
        return this.mBearerData.serviceCategoryProgramData;
    }

    /* Access modifiers changed, original: protected */
    public boolean processCdmaCTWdpHeader(SmsMessage sms) {
        String str = LOG_TAG;
        boolean decodeSuccess = false;
        try {
            BitwiseInputStream inStream = new BitwiseInputStream(sms.getUserData());
            if (inStream.read(8) != 0) {
                Rlog.e(str, "Invalid WDP SubparameterId");
                return false;
            } else if (inStream.read(8) != 3) {
                Rlog.e(str, "Invalid WDP subparameter length");
                return false;
            } else {
                sms.mBearerData.messageType = inStream.read(4);
                int msgID = (inStream.read(8) << 8) | inStream.read(8);
                BearerData bearerData = sms.mBearerData;
                boolean z = true;
                if (inStream.read(1) != 1) {
                    z = false;
                }
                bearerData.hasUserDataHeader = z;
                if (sms.mBearerData.hasUserDataHeader) {
                    Rlog.e(str, "Invalid WDP UserData header value");
                    return false;
                }
                inStream.skip(3);
                sms.mBearerData.messageId = msgID;
                sms.mMessageRef = msgID;
                int subparamId = inStream.read(8);
                int subParamLen = inStream.read(8) * 8;
                sms.mBearerData.userData.msgEncoding = inStream.read(5);
                if (sms.mBearerData.userData.msgEncoding != 0) {
                    Rlog.e(str, "Invalid WDP encoding");
                    return false;
                }
                sms.mBearerData.userData.numFields = inStream.read(8);
                int remainingBits = subParamLen - (5 + 8);
                int dataBits = 8 * sms.mBearerData.userData.numFields;
                sms.mBearerData.userData.payload = inStream.readByteArray(dataBits < remainingBits ? dataBits : remainingBits);
                sms.mUserData = sms.mBearerData.userData.payload;
                decodeSuccess = true;
                return decodeSuccess;
            }
        } catch (AccessException ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CT WDP Header decode failed: ");
            stringBuilder.append(ex);
            Rlog.e(str, stringBuilder.toString());
        }
    }
}
