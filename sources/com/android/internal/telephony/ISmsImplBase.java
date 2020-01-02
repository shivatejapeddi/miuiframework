package com.android.internal.telephony;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.IFinancialSmsCallback;
import com.android.internal.telephony.ISms.Stub;
import java.util.List;

public class ISmsImplBase extends Stub {
    public List<SmsRawData> getAllMessagesFromIccEfForSubscriber(int subId, String callingPkg) {
        throw new UnsupportedOperationException();
    }

    public boolean updateMessageOnIccEfForSubscriber(int subId, String callingPkg, int messageIndex, int newStatus, byte[] pdu) {
        throw new UnsupportedOperationException();
    }

    public boolean copyMessageToIccEfForSubscriber(int subId, String callingPkg, int status, byte[] pdu, byte[] smsc) {
        throw new UnsupportedOperationException();
    }

    public void sendDataForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, int destPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        throw new UnsupportedOperationException();
    }

    public void sendDataForSubscriberWithSelfPermissions(int subId, String callingPkg, String destAddr, String scAddr, int destPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        throw new UnsupportedOperationException();
    }

    public void sendTextForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp) {
        throw new UnsupportedOperationException();
    }

    public void sendTextForSubscriberWithSelfPermissions(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessage) {
        throw new UnsupportedOperationException();
    }

    public void sendTextForSubscriberWithOptions(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp, int priority, boolean expectMore, int validityPeriod) {
        throw new UnsupportedOperationException();
    }

    public void injectSmsPduForSubscriber(int subId, byte[] pdu, String format, PendingIntent receivedIntent) {
        throw new UnsupportedOperationException();
    }

    public void sendMultipartTextForSubscriber(int subId, String callingPkg, String destinationAddress, String scAddress, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean persistMessageForNonDefaultSmsApp) {
        throw new UnsupportedOperationException();
    }

    public void sendMultipartTextForSubscriberWithOptions(int subId, String callingPkg, String destinationAddress, String scAddress, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean persistMessageForNonDefaultSmsApp, int priority, boolean expectMore, int validityPeriod) {
        throw new UnsupportedOperationException();
    }

    public boolean enableCellBroadcastForSubscriber(int subId, int messageIdentifier, int ranType) {
        throw new UnsupportedOperationException();
    }

    public boolean disableCellBroadcastForSubscriber(int subId, int messageIdentifier, int ranType) {
        throw new UnsupportedOperationException();
    }

    public boolean enableCellBroadcastRangeForSubscriber(int subId, int startMessageId, int endMessageId, int ranType) {
        throw new UnsupportedOperationException();
    }

    public boolean disableCellBroadcastRangeForSubscriber(int subId, int startMessageId, int endMessageId, int ranType) {
        throw new UnsupportedOperationException();
    }

    public int getPremiumSmsPermission(String packageName) {
        throw new UnsupportedOperationException();
    }

    public int getPremiumSmsPermissionForSubscriber(int subId, String packageName) {
        throw new UnsupportedOperationException();
    }

    public void setPremiumSmsPermission(String packageName, int permission) {
        throw new UnsupportedOperationException();
    }

    public void setPremiumSmsPermissionForSubscriber(int subId, String packageName, int permission) {
        throw new UnsupportedOperationException();
    }

    public boolean isImsSmsSupportedForSubscriber(int subId) {
        throw new UnsupportedOperationException();
    }

    public boolean isSmsSimPickActivityNeeded(int subId) {
        throw new UnsupportedOperationException();
    }

    public int getPreferredSmsSubscription() {
        throw new UnsupportedOperationException();
    }

    public String getImsSmsFormatForSubscriber(int subId) {
        throw new UnsupportedOperationException();
    }

    public boolean isSMSPromptEnabled() {
        throw new UnsupportedOperationException();
    }

    public void sendStoredText(int subId, String callingPkg, Uri messageUri, String scAddress, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        throw new UnsupportedOperationException();
    }

    public void sendStoredMultipartText(int subId, String callingPkg, Uri messageUri, String scAddress, List<PendingIntent> list, List<PendingIntent> list2) {
        throw new UnsupportedOperationException();
    }

    public String createAppSpecificSmsToken(int subId, String callingPkg, PendingIntent intent) {
        throw new UnsupportedOperationException();
    }

    public String createAppSpecificSmsTokenWithPackageInfo(int subId, String callingPkg, String prefixes, PendingIntent intent) {
        throw new UnsupportedOperationException();
    }

    public void getSmsMessagesForFinancialApp(int subId, String callingPkg, Bundle params, IFinancialSmsCallback callback) {
        throw new UnsupportedOperationException();
    }

    public int getSmsCapacityOnIccForSubscriber(int subId) {
        throw new UnsupportedOperationException();
    }

    public int checkSmsShortCodeDestination(int subid, String callingApk, String destAddress, String countryIso) {
        throw new UnsupportedOperationException();
    }
}
