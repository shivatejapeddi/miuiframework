package com.android.internal.telephony.gsm;

import android.telephony.SmsCbCmasInfo;
import android.telephony.SmsCbEtwsInfo;
import java.util.Arrays;

public class SmsCbHeader {
    static final int FORMAT_ETWS_PRIMARY = 3;
    static final int FORMAT_GSM = 1;
    static final int FORMAT_UMTS = 2;
    private static final int MESSAGE_TYPE_CBS_MESSAGE = 1;
    static final int PDU_HEADER_LENGTH = 6;
    private static final int PDU_LENGTH_ETWS = 56;
    private static final int PDU_LENGTH_GSM = 88;
    private final SmsCbCmasInfo mCmasInfo;
    private final int mDataCodingScheme;
    private final SmsCbEtwsInfo mEtwsInfo;
    private final int mFormat;
    private final int mGeographicalScope;
    private final int mMessageIdentifier;
    private final int mNrOfPages;
    private final int mPageIndex;
    private final int mSerialNumber;

    public SmsCbHeader(byte[] pdu) throws IllegalArgumentException {
        byte[] bArr = pdu;
        if (bArr == null || bArr.length < 6) {
            throw new IllegalArgumentException("Illegal PDU");
        }
        int pageIndex;
        int nrOfPages;
        if (bArr.length <= 88) {
            this.mGeographicalScope = (bArr[0] & 192) >>> 6;
            this.mSerialNumber = ((bArr[0] & 255) << 8) | (bArr[1] & 255);
            this.mMessageIdentifier = ((bArr[2] & 255) << 8) | (bArr[3] & 255);
            if (!isEtwsMessage() || bArr.length > 56) {
                this.mFormat = 1;
                this.mDataCodingScheme = bArr[4] & 255;
                pageIndex = (bArr[5] & 240) >>> 4;
                nrOfPages = bArr[5] & 15;
                if (pageIndex == 0 || nrOfPages == 0 || pageIndex > nrOfPages) {
                    pageIndex = 1;
                    nrOfPages = 1;
                }
                this.mPageIndex = pageIndex;
                this.mNrOfPages = nrOfPages;
            } else {
                byte[] warningSecurityInfo;
                this.mFormat = 3;
                this.mDataCodingScheme = -1;
                this.mPageIndex = -1;
                this.mNrOfPages = -1;
                boolean emergencyUserAlert = (bArr[4] & 1) != 0;
                boolean activatePopup = (bArr[5] & 128) != 0;
                pageIndex = (bArr[4] & 254) >>> 1;
                if (bArr.length > 6) {
                    warningSecurityInfo = Arrays.copyOfRange(bArr, 6, bArr.length);
                } else {
                    warningSecurityInfo = null;
                }
                this.mEtwsInfo = new SmsCbEtwsInfo(pageIndex, emergencyUserAlert, activatePopup, true, warningSecurityInfo);
                this.mCmasInfo = null;
                return;
            }
        }
        this.mFormat = 2;
        pageIndex = bArr[0];
        if (pageIndex == 1) {
            this.mMessageIdentifier = ((bArr[1] & 255) << 8) | (bArr[2] & 255);
            this.mGeographicalScope = (bArr[3] & 192) >>> 6;
            this.mSerialNumber = ((bArr[3] & 255) << 8) | (bArr[4] & 255);
            this.mDataCodingScheme = bArr[5] & 255;
            this.mPageIndex = 1;
            this.mNrOfPages = 1;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported message type ");
            stringBuilder.append(pageIndex);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        if (isEtwsMessage()) {
            this.mEtwsInfo = new SmsCbEtwsInfo(getEtwsWarningType(), isEtwsEmergencyUserAlert(), isEtwsPopupAlert(), false, null);
            this.mCmasInfo = null;
        } else if (isCmasMessage()) {
            pageIndex = getCmasMessageClass();
            nrOfPages = getCmasSeverity();
            int urgency = getCmasUrgency();
            int certainty = getCmasCertainty();
            this.mEtwsInfo = null;
            this.mCmasInfo = new SmsCbCmasInfo(pageIndex, -1, -1, nrOfPages, urgency, certainty);
        } else {
            this.mEtwsInfo = null;
            this.mCmasInfo = null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int getGeographicalScope() {
        return this.mGeographicalScope;
    }

    /* Access modifiers changed, original: 0000 */
    public int getSerialNumber() {
        return this.mSerialNumber;
    }

    /* Access modifiers changed, original: 0000 */
    public int getServiceCategory() {
        return this.mMessageIdentifier;
    }

    /* Access modifiers changed, original: 0000 */
    public int getDataCodingScheme() {
        return this.mDataCodingScheme;
    }

    /* Access modifiers changed, original: 0000 */
    public int getPageIndex() {
        return this.mPageIndex;
    }

    /* Access modifiers changed, original: 0000 */
    public int getNumberOfPages() {
        return this.mNrOfPages;
    }

    /* Access modifiers changed, original: 0000 */
    public SmsCbEtwsInfo getEtwsInfo() {
        return this.mEtwsInfo;
    }

    /* Access modifiers changed, original: 0000 */
    public SmsCbCmasInfo getCmasInfo() {
        return this.mCmasInfo;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isEmergencyMessage() {
        int i = this.mMessageIdentifier;
        return i >= 4352 && i <= SmsCbConstants.MESSAGE_ID_PWS_LAST_IDENTIFIER;
    }

    private boolean isEtwsMessage() {
        return (this.mMessageIdentifier & SmsCbConstants.MESSAGE_ID_ETWS_TYPE_MASK) == 4352;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isEtwsPrimaryNotification() {
        return this.mFormat == 3;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isUmtsFormat() {
        return this.mFormat == 2;
    }

    private boolean isCmasMessage() {
        int i = this.mMessageIdentifier;
        return i >= 4370 && i <= 4399;
    }

    private boolean isEtwsPopupAlert() {
        return (this.mSerialNumber & 4096) != 0;
    }

    private boolean isEtwsEmergencyUserAlert() {
        return (this.mSerialNumber & 8192) != 0;
    }

    private int getEtwsWarningType() {
        return this.mMessageIdentifier - 4352;
    }

    private int getCmasMessageClass() {
        switch (this.mMessageIdentifier) {
            case 4370:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL_LANGUAGE /*4383*/:
                return 0;
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_OBSERVED /*4371*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_LIKELY /*4372*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_OBSERVED_LANGUAGE /*4384*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_LIKELY_LANGUAGE /*4385*/:
                return 1;
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_OBSERVED /*4373*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_LIKELY /*4374*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_OBSERVED /*4375*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_LIKELY /*4376*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_OBSERVED /*4377*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_LIKELY /*4378*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_OBSERVED_LANGUAGE /*4386*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_LIKELY_LANGUAGE /*4387*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_OBSERVED_LANGUAGE /*4388*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_LIKELY_LANGUAGE /*4389*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_OBSERVED_LANGUAGE /*4390*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_LIKELY_LANGUAGE /*4391*/:
                return 2;
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_CHILD_ABDUCTION_EMERGENCY /*4379*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_CHILD_ABDUCTION_EMERGENCY_LANGUAGE /*4392*/:
                return 3;
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_REQUIRED_MONTHLY_TEST /*4380*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_REQUIRED_MONTHLY_TEST_LANGUAGE /*4393*/:
                return 4;
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXERCISE /*4381*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXERCISE_LANGUAGE /*4394*/:
                return 5;
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_OPERATOR_DEFINED_USE /*4382*/:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_OPERATOR_DEFINED_USE_LANGUAGE /*4395*/:
                return 6;
            default:
                return -1;
        }
    }

    /* JADX WARNING: Missing block: B:6:0x000b, code skipped:
            return 1;
     */
    /* JADX WARNING: Missing block: B:8:0x000d, code skipped:
            return 0;
     */
    private int getCmasSeverity() {
        /*
        r1 = this;
        r0 = r1.mMessageIdentifier;
        switch(r0) {
            case 4371: goto L_0x000c;
            case 4372: goto L_0x000c;
            case 4373: goto L_0x000c;
            case 4374: goto L_0x000c;
            case 4375: goto L_0x000a;
            case 4376: goto L_0x000a;
            case 4377: goto L_0x000a;
            case 4378: goto L_0x000a;
            default: goto L_0x0005;
        };
    L_0x0005:
        switch(r0) {
            case 4384: goto L_0x000c;
            case 4385: goto L_0x000c;
            case 4386: goto L_0x000c;
            case 4387: goto L_0x000c;
            case 4388: goto L_0x000a;
            case 4389: goto L_0x000a;
            case 4390: goto L_0x000a;
            case 4391: goto L_0x000a;
            default: goto L_0x0008;
        };
    L_0x0008:
        r0 = -1;
        return r0;
    L_0x000a:
        r0 = 1;
        return r0;
    L_0x000c:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.telephony.gsm.SmsCbHeader.getCmasSeverity():int");
    }

    /* JADX WARNING: Missing block: B:6:0x000b, code skipped:
            return 1;
     */
    /* JADX WARNING: Missing block: B:8:0x000d, code skipped:
            return 0;
     */
    private int getCmasUrgency() {
        /*
        r1 = this;
        r0 = r1.mMessageIdentifier;
        switch(r0) {
            case 4371: goto L_0x000c;
            case 4372: goto L_0x000c;
            case 4373: goto L_0x000a;
            case 4374: goto L_0x000a;
            case 4375: goto L_0x000c;
            case 4376: goto L_0x000c;
            case 4377: goto L_0x000a;
            case 4378: goto L_0x000a;
            default: goto L_0x0005;
        };
    L_0x0005:
        switch(r0) {
            case 4384: goto L_0x000c;
            case 4385: goto L_0x000c;
            case 4386: goto L_0x000a;
            case 4387: goto L_0x000a;
            case 4388: goto L_0x000c;
            case 4389: goto L_0x000c;
            case 4390: goto L_0x000a;
            case 4391: goto L_0x000a;
            default: goto L_0x0008;
        };
    L_0x0008:
        r0 = -1;
        return r0;
    L_0x000a:
        r0 = 1;
        return r0;
    L_0x000c:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.telephony.gsm.SmsCbHeader.getCmasUrgency():int");
    }

    /* JADX WARNING: Missing block: B:6:0x000b, code skipped:
            return 1;
     */
    /* JADX WARNING: Missing block: B:8:0x000d, code skipped:
            return 0;
     */
    private int getCmasCertainty() {
        /*
        r1 = this;
        r0 = r1.mMessageIdentifier;
        switch(r0) {
            case 4371: goto L_0x000c;
            case 4372: goto L_0x000a;
            case 4373: goto L_0x000c;
            case 4374: goto L_0x000a;
            case 4375: goto L_0x000c;
            case 4376: goto L_0x000a;
            case 4377: goto L_0x000c;
            case 4378: goto L_0x000a;
            default: goto L_0x0005;
        };
    L_0x0005:
        switch(r0) {
            case 4384: goto L_0x000c;
            case 4385: goto L_0x000a;
            case 4386: goto L_0x000c;
            case 4387: goto L_0x000a;
            case 4388: goto L_0x000c;
            case 4389: goto L_0x000a;
            case 4390: goto L_0x000c;
            case 4391: goto L_0x000a;
            default: goto L_0x0008;
        };
    L_0x0008:
        r0 = -1;
        return r0;
    L_0x000a:
        r0 = 1;
        return r0;
    L_0x000c:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.telephony.gsm.SmsCbHeader.getCmasCertainty():int");
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SmsCbHeader{GS=");
        stringBuilder.append(this.mGeographicalScope);
        stringBuilder.append(", serialNumber=0x");
        stringBuilder.append(Integer.toHexString(this.mSerialNumber));
        stringBuilder.append(", messageIdentifier=0x");
        stringBuilder.append(Integer.toHexString(this.mMessageIdentifier));
        stringBuilder.append(", format=");
        stringBuilder.append(this.mFormat);
        stringBuilder.append(", DCS=0x");
        stringBuilder.append(Integer.toHexString(this.mDataCodingScheme));
        stringBuilder.append(", page ");
        stringBuilder.append(this.mPageIndex);
        stringBuilder.append(" of ");
        stringBuilder.append(this.mNrOfPages);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
