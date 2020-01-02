package com.android.internal.telephony;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.IFinancialSmsCallback;
import java.util.List;

public interface ISms extends IInterface {

    public static class Default implements ISms {
        public List<SmsRawData> getAllMessagesFromIccEfForSubscriber(int subId, String callingPkg) throws RemoteException {
            return null;
        }

        public boolean updateMessageOnIccEfForSubscriber(int subId, String callingPkg, int messageIndex, int newStatus, byte[] pdu) throws RemoteException {
            return false;
        }

        public boolean copyMessageToIccEfForSubscriber(int subId, String callingPkg, int status, byte[] pdu, byte[] smsc) throws RemoteException {
            return false;
        }

        public void sendDataForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, int destPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
        }

        public void sendDataForSubscriberWithSelfPermissions(int subId, String callingPkg, String destAddr, String scAddr, int destPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
        }

        public void sendTextForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
        }

        public void sendTextForSubscriberWithSelfPermissions(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessage) throws RemoteException {
        }

        public void sendTextForSubscriberWithOptions(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp, int priority, boolean expectMore, int validityPeriod) throws RemoteException {
        }

        public void injectSmsPduForSubscriber(int subId, byte[] pdu, String format, PendingIntent receivedIntent) throws RemoteException {
        }

        public void sendMultipartTextForSubscriber(int subId, String callingPkg, String destinationAddress, String scAddress, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
        }

        public void sendMultipartTextForSubscriberWithOptions(int subId, String callingPkg, String destinationAddress, String scAddress, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean persistMessageForNonDefaultSmsApp, int priority, boolean expectMore, int validityPeriod) throws RemoteException {
        }

        public boolean enableCellBroadcastForSubscriber(int subId, int messageIdentifier, int ranType) throws RemoteException {
            return false;
        }

        public boolean disableCellBroadcastForSubscriber(int subId, int messageIdentifier, int ranType) throws RemoteException {
            return false;
        }

        public boolean enableCellBroadcastRangeForSubscriber(int subId, int startMessageId, int endMessageId, int ranType) throws RemoteException {
            return false;
        }

        public boolean disableCellBroadcastRangeForSubscriber(int subId, int startMessageId, int endMessageId, int ranType) throws RemoteException {
            return false;
        }

        public int getPremiumSmsPermission(String packageName) throws RemoteException {
            return 0;
        }

        public int getPremiumSmsPermissionForSubscriber(int subId, String packageName) throws RemoteException {
            return 0;
        }

        public void setPremiumSmsPermission(String packageName, int permission) throws RemoteException {
        }

        public void setPremiumSmsPermissionForSubscriber(int subId, String packageName, int permission) throws RemoteException {
        }

        public boolean isImsSmsSupportedForSubscriber(int subId) throws RemoteException {
            return false;
        }

        public boolean isSmsSimPickActivityNeeded(int subId) throws RemoteException {
            return false;
        }

        public int getPreferredSmsSubscription() throws RemoteException {
            return 0;
        }

        public String getImsSmsFormatForSubscriber(int subId) throws RemoteException {
            return null;
        }

        public boolean isSMSPromptEnabled() throws RemoteException {
            return false;
        }

        public void sendStoredText(int subId, String callingPkg, Uri messageUri, String scAddress, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
        }

        public void sendStoredMultipartText(int subId, String callingPkg, Uri messageUri, String scAddress, List<PendingIntent> list, List<PendingIntent> list2) throws RemoteException {
        }

        public String createAppSpecificSmsToken(int subId, String callingPkg, PendingIntent intent) throws RemoteException {
            return null;
        }

        public String createAppSpecificSmsTokenWithPackageInfo(int subId, String callingPkg, String prefixes, PendingIntent intent) throws RemoteException {
            return null;
        }

        public void getSmsMessagesForFinancialApp(int subId, String callingPkg, Bundle params, IFinancialSmsCallback callback) throws RemoteException {
        }

        public int getSmsCapacityOnIccForSubscriber(int subId) throws RemoteException {
            return 0;
        }

        public int checkSmsShortCodeDestination(int subId, String callingApk, String destAddress, String countryIso) throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISms {
        private static final String DESCRIPTOR = "com.android.internal.telephony.ISms";
        static final int TRANSACTION_checkSmsShortCodeDestination = 31;
        static final int TRANSACTION_copyMessageToIccEfForSubscriber = 3;
        static final int TRANSACTION_createAppSpecificSmsToken = 27;
        static final int TRANSACTION_createAppSpecificSmsTokenWithPackageInfo = 28;
        static final int TRANSACTION_disableCellBroadcastForSubscriber = 13;
        static final int TRANSACTION_disableCellBroadcastRangeForSubscriber = 15;
        static final int TRANSACTION_enableCellBroadcastForSubscriber = 12;
        static final int TRANSACTION_enableCellBroadcastRangeForSubscriber = 14;
        static final int TRANSACTION_getAllMessagesFromIccEfForSubscriber = 1;
        static final int TRANSACTION_getImsSmsFormatForSubscriber = 23;
        static final int TRANSACTION_getPreferredSmsSubscription = 22;
        static final int TRANSACTION_getPremiumSmsPermission = 16;
        static final int TRANSACTION_getPremiumSmsPermissionForSubscriber = 17;
        static final int TRANSACTION_getSmsCapacityOnIccForSubscriber = 30;
        static final int TRANSACTION_getSmsMessagesForFinancialApp = 29;
        static final int TRANSACTION_injectSmsPduForSubscriber = 9;
        static final int TRANSACTION_isImsSmsSupportedForSubscriber = 20;
        static final int TRANSACTION_isSMSPromptEnabled = 24;
        static final int TRANSACTION_isSmsSimPickActivityNeeded = 21;
        static final int TRANSACTION_sendDataForSubscriber = 4;
        static final int TRANSACTION_sendDataForSubscriberWithSelfPermissions = 5;
        static final int TRANSACTION_sendMultipartTextForSubscriber = 10;
        static final int TRANSACTION_sendMultipartTextForSubscriberWithOptions = 11;
        static final int TRANSACTION_sendStoredMultipartText = 26;
        static final int TRANSACTION_sendStoredText = 25;
        static final int TRANSACTION_sendTextForSubscriber = 6;
        static final int TRANSACTION_sendTextForSubscriberWithOptions = 8;
        static final int TRANSACTION_sendTextForSubscriberWithSelfPermissions = 7;
        static final int TRANSACTION_setPremiumSmsPermission = 18;
        static final int TRANSACTION_setPremiumSmsPermissionForSubscriber = 19;
        static final int TRANSACTION_updateMessageOnIccEfForSubscriber = 2;

        private static class Proxy implements ISms {
            public static ISms sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public List<SmsRawData> getAllMessagesFromIccEfForSubscriber(int subId, String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    List<SmsRawData> list = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAllMessagesFromIccEfForSubscriber(subId, callingPkg);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(SmsRawData.CREATOR);
                    List<SmsRawData> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean updateMessageOnIccEfForSubscriber(int subId, String callingPkg, int messageIndex, int newStatus, byte[] pdu) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                byte[] bArr;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        str = callingPkg;
                        i = messageIndex;
                        i2 = newStatus;
                        bArr = pdu;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPkg);
                        try {
                            _data.writeInt(messageIndex);
                            try {
                                _data.writeInt(newStatus);
                            } catch (Throwable th3) {
                                th = th3;
                                bArr = pdu;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            i2 = newStatus;
                            bArr = pdu;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i = messageIndex;
                        i2 = newStatus;
                        bArr = pdu;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeByteArray(pdu);
                        try {
                            boolean z = false;
                            if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() != 0) {
                                    z = true;
                                }
                                boolean _result = z;
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            boolean updateMessageOnIccEfForSubscriber = Stub.getDefaultImpl().updateMessageOnIccEfForSubscriber(subId, callingPkg, messageIndex, newStatus, pdu);
                            _reply.recycle();
                            _data.recycle();
                            return updateMessageOnIccEfForSubscriber;
                        } catch (Throwable th6) {
                            th = th6;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    int i3 = subId;
                    str = callingPkg;
                    i = messageIndex;
                    i2 = newStatus;
                    bArr = pdu;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean copyMessageToIccEfForSubscriber(int subId, String callingPkg, int status, byte[] pdu, byte[] smsc) throws RemoteException {
                Throwable th;
                String str;
                int i;
                byte[] bArr;
                byte[] bArr2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        str = callingPkg;
                        i = status;
                        bArr = pdu;
                        bArr2 = smsc;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPkg);
                        try {
                            _data.writeInt(status);
                            try {
                                _data.writeByteArray(pdu);
                            } catch (Throwable th3) {
                                th = th3;
                                bArr2 = smsc;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            bArr = pdu;
                            bArr2 = smsc;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i = status;
                        bArr = pdu;
                        bArr2 = smsc;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeByteArray(smsc);
                        try {
                            boolean z = false;
                            if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() != 0) {
                                    z = true;
                                }
                                boolean _result = z;
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            boolean copyMessageToIccEfForSubscriber = Stub.getDefaultImpl().copyMessageToIccEfForSubscriber(subId, callingPkg, status, pdu, smsc);
                            _reply.recycle();
                            _data.recycle();
                            return copyMessageToIccEfForSubscriber;
                        } catch (Throwable th6) {
                            th = th6;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    int i2 = subId;
                    str = callingPkg;
                    i = status;
                    bArr = pdu;
                    bArr2 = smsc;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void sendDataForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, int destPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Throwable th;
                String str;
                PendingIntent pendingIntent = sentIntent;
                PendingIntent pendingIntent2 = deliveryIntent;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        str = callingPkg;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPkg);
                        _data.writeString(destAddr);
                        _data.writeString(scAddr);
                        _data.writeInt(destPort);
                        _data.writeByteArray(data);
                        if (pendingIntent != null) {
                            _data.writeInt(1);
                            pendingIntent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (pendingIntent2 != null) {
                            _data.writeInt(1);
                            pendingIntent2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().sendDataForSubscriber(subId, callingPkg, destAddr, scAddr, destPort, data, sentIntent, deliveryIntent);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    int i = subId;
                    str = callingPkg;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void sendDataForSubscriberWithSelfPermissions(int subId, String callingPkg, String destAddr, String scAddr, int destPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Throwable th;
                String str;
                PendingIntent pendingIntent = sentIntent;
                PendingIntent pendingIntent2 = deliveryIntent;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        str = callingPkg;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPkg);
                        _data.writeString(destAddr);
                        _data.writeString(scAddr);
                        _data.writeInt(destPort);
                        _data.writeByteArray(data);
                        if (pendingIntent != null) {
                            _data.writeInt(1);
                            pendingIntent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (pendingIntent2 != null) {
                            _data.writeInt(1);
                            pendingIntent2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().sendDataForSubscriberWithSelfPermissions(subId, callingPkg, destAddr, scAddr, destPort, data, sentIntent, deliveryIntent);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    int i = subId;
                    str = callingPkg;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void sendTextForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
                Throwable th;
                String str;
                PendingIntent pendingIntent = sentIntent;
                PendingIntent pendingIntent2 = deliveryIntent;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        str = callingPkg;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPkg);
                        _data.writeString(destAddr);
                        _data.writeString(scAddr);
                        _data.writeString(text);
                        int i = 1;
                        if (pendingIntent != null) {
                            _data.writeInt(1);
                            pendingIntent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (pendingIntent2 != null) {
                            _data.writeInt(1);
                            pendingIntent2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (!persistMessageForNonDefaultSmsApp) {
                            i = 0;
                        }
                        _data.writeInt(i);
                        if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().sendTextForSubscriber(subId, callingPkg, destAddr, scAddr, text, sentIntent, deliveryIntent, persistMessageForNonDefaultSmsApp);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    int i2 = subId;
                    str = callingPkg;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void sendTextForSubscriberWithSelfPermissions(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessage) throws RemoteException {
                Throwable th;
                String str;
                PendingIntent pendingIntent = sentIntent;
                PendingIntent pendingIntent2 = deliveryIntent;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        str = callingPkg;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPkg);
                        _data.writeString(destAddr);
                        _data.writeString(scAddr);
                        _data.writeString(text);
                        int i = 1;
                        if (pendingIntent != null) {
                            _data.writeInt(1);
                            pendingIntent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (pendingIntent2 != null) {
                            _data.writeInt(1);
                            pendingIntent2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (!persistMessage) {
                            i = 0;
                        }
                        _data.writeInt(i);
                        if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().sendTextForSubscriberWithSelfPermissions(subId, callingPkg, destAddr, scAddr, text, sentIntent, deliveryIntent, persistMessage);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    int i2 = subId;
                    str = callingPkg;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void sendTextForSubscriberWithOptions(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp, int priority, boolean expectMore, int validityPeriod) throws RemoteException {
                Throwable th;
                PendingIntent pendingIntent = sentIntent;
                PendingIntent pendingIntent2 = deliveryIntent;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                Parcel _reply2;
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeString(text);
                    int i = 1;
                    if (pendingIntent != null) {
                        try {
                            _data.writeInt(1);
                            pendingIntent.writeToParcel(_data, 0);
                        } catch (Throwable th2) {
                            th = th2;
                            _reply2 = _reply;
                        }
                    } else {
                        _data.writeInt(0);
                    }
                    if (pendingIntent2 != null) {
                        _data.writeInt(1);
                        pendingIntent2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(persistMessageForNonDefaultSmsApp ? 1 : 0);
                    _data.writeInt(priority);
                    if (!expectMore) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(validityPeriod);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply2 = _reply;
                        _reply2.readException();
                        _reply2.recycle();
                        _data.recycle();
                        return;
                    }
                    _reply2 = _reply;
                    try {
                        Stub.getDefaultImpl().sendTextForSubscriberWithOptions(subId, callingPkg, destAddr, scAddr, text, sentIntent, deliveryIntent, persistMessageForNonDefaultSmsApp, priority, expectMore, validityPeriod);
                        _reply2.recycle();
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _reply2.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply2 = _reply;
                    _reply2.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void injectSmsPduForSubscriber(int subId, byte[] pdu, String format, PendingIntent receivedIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeByteArray(pdu);
                    _data.writeString(format);
                    if (receivedIntent != null) {
                        _data.writeInt(1);
                        receivedIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().injectSmsPduForSubscriber(subId, pdu, format, receivedIntent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendMultipartTextForSubscriber(int subId, String callingPkg, String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
                Throwable th;
                String str;
                String str2;
                String str3;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        str = callingPkg;
                        str2 = destinationAddress;
                        str3 = scAddress;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPkg);
                    } catch (Throwable th3) {
                        th = th3;
                        str2 = destinationAddress;
                        str3 = scAddress;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(destinationAddress);
                        try {
                            _data.writeString(scAddress);
                            _data.writeStringList(parts);
                            _data.writeTypedList(sentIntents);
                            _data.writeTypedList(deliveryIntents);
                            _data.writeInt(persistMessageForNonDefaultSmsApp ? 1 : 0);
                            if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().sendMultipartTextForSubscriber(subId, callingPkg, destinationAddress, scAddress, parts, sentIntents, deliveryIntents, persistMessageForNonDefaultSmsApp);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        str3 = scAddress;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i = subId;
                    str = callingPkg;
                    str2 = destinationAddress;
                    str3 = scAddress;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void sendMultipartTextForSubscriberWithOptions(int subId, String callingPkg, String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp, int priority, boolean expectMore, int validityPeriod) throws RemoteException {
                Throwable th;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                        _data.writeString(callingPkg);
                        _data.writeString(destinationAddress);
                        _data.writeString(scAddress);
                        _data.writeStringList(parts);
                        _data.writeTypedList(sentIntents);
                        _data.writeTypedList(deliveryIntents);
                        int i = 1;
                        _data.writeInt(persistMessageForNonDefaultSmsApp ? 1 : 0);
                        _data.writeInt(priority);
                        if (!expectMore) {
                            i = 0;
                        }
                        _data.writeInt(i);
                        _data.writeInt(validityPeriod);
                        if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().sendMultipartTextForSubscriberWithOptions(subId, callingPkg, destinationAddress, scAddress, parts, sentIntents, deliveryIntents, persistMessageForNonDefaultSmsApp, priority, expectMore, validityPeriod);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    int i2 = subId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean enableCellBroadcastForSubscriber(int subId, int messageIdentifier, int ranType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(messageIdentifier);
                    _data.writeInt(ranType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().enableCellBroadcastForSubscriber(subId, messageIdentifier, ranType);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean disableCellBroadcastForSubscriber(int subId, int messageIdentifier, int ranType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(messageIdentifier);
                    _data.writeInt(ranType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().disableCellBroadcastForSubscriber(subId, messageIdentifier, ranType);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean enableCellBroadcastRangeForSubscriber(int subId, int startMessageId, int endMessageId, int ranType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(startMessageId);
                    _data.writeInt(endMessageId);
                    _data.writeInt(ranType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().enableCellBroadcastRangeForSubscriber(subId, startMessageId, endMessageId, ranType);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean disableCellBroadcastRangeForSubscriber(int subId, int startMessageId, int endMessageId, int ranType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(startMessageId);
                    _data.writeInt(endMessageId);
                    _data.writeInt(ranType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().disableCellBroadcastRangeForSubscriber(subId, startMessageId, endMessageId, ranType);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPremiumSmsPermission(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    int i = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPremiumSmsPermission(packageName);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPremiumSmsPermissionForSubscriber(int subId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(packageName);
                    int i = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPremiumSmsPermissionForSubscriber(subId, packageName);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPremiumSmsPermission(String packageName, int permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(permission);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPremiumSmsPermission(packageName, permission);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPremiumSmsPermissionForSubscriber(int subId, String packageName, int permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(packageName);
                    _data.writeInt(permission);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPremiumSmsPermissionForSubscriber(subId, packageName, permission);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isImsSmsSupportedForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isImsSmsSupportedForSubscriber(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSmsSimPickActivityNeeded(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isSmsSimPickActivityNeeded(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPreferredSmsSubscription() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 22;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPreferredSmsSubscription();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getImsSmsFormatForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String str = 23;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getImsSmsFormatForSubscriber(subId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSMSPromptEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isSMSPromptEnabled();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendStoredText(int subId, String callingPkg, Uri messageUri, String scAddress, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Throwable th;
                String str;
                String str2;
                Uri uri = messageUri;
                PendingIntent pendingIntent = sentIntent;
                PendingIntent pendingIntent2 = deliveryIntent;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        str = callingPkg;
                        str2 = scAddress;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPkg);
                        if (uri != null) {
                            _data.writeInt(1);
                            uri.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeString(scAddress);
                            if (pendingIntent != null) {
                                _data.writeInt(1);
                                pendingIntent.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (pendingIntent2 != null) {
                                _data.writeInt(1);
                                pendingIntent2.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().sendStoredText(subId, callingPkg, messageUri, scAddress, sentIntent, deliveryIntent);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        str2 = scAddress;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    int i = subId;
                    str = callingPkg;
                    str2 = scAddress;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void sendStoredMultipartText(int subId, String callingPkg, Uri messageUri, String scAddress, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents) throws RemoteException {
                Throwable th;
                String str;
                List<PendingIntent> list;
                List<PendingIntent> list2;
                String str2;
                Uri uri = messageUri;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                        try {
                            _data.writeString(callingPkg);
                            if (uri != null) {
                                _data.writeInt(1);
                                uri.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            str = scAddress;
                            list = sentIntents;
                            list2 = deliveryIntents;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        str2 = callingPkg;
                        str = scAddress;
                        list = sentIntents;
                        list2 = deliveryIntents;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(scAddress);
                        try {
                            _data.writeTypedList(sentIntents);
                        } catch (Throwable th4) {
                            th = th4;
                            list2 = deliveryIntents;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeTypedList(deliveryIntents);
                            if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().sendStoredMultipartText(subId, callingPkg, messageUri, scAddress, sentIntents, deliveryIntents);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        list = sentIntents;
                        list2 = deliveryIntents;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i = subId;
                    str2 = callingPkg;
                    str = scAddress;
                    list = sentIntents;
                    list2 = deliveryIntents;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public String createAppSpecificSmsToken(int subId, String callingPkg, PendingIntent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String str = this.mRemote;
                    if (!str.transact(27, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().createAppSpecificSmsToken(subId, callingPkg, intent);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String createAppSpecificSmsTokenWithPackageInfo(int subId, String callingPkg, String prefixes, PendingIntent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(prefixes);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String str = this.mRemote;
                    if (!str.transact(28, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().createAppSpecificSmsTokenWithPackageInfo(subId, callingPkg, prefixes, intent);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getSmsMessagesForFinancialApp(int subId, String callingPkg, Bundle params, IFinancialSmsCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getSmsMessagesForFinancialApp(subId, callingPkg, params, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSmsCapacityOnIccForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    int i = 30;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getSmsCapacityOnIccForSubscriber(subId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkSmsShortCodeDestination(int subId, String callingApk, String destAddress, String countryIso) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingApk);
                    _data.writeString(destAddress);
                    _data.writeString(countryIso);
                    int i = 31;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkSmsShortCodeDestination(subId, callingApk, destAddress, countryIso);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISms asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISms)) {
                return new Proxy(obj);
            }
            return (ISms) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getAllMessagesFromIccEfForSubscriber";
                case 2:
                    return "updateMessageOnIccEfForSubscriber";
                case 3:
                    return "copyMessageToIccEfForSubscriber";
                case 4:
                    return "sendDataForSubscriber";
                case 5:
                    return "sendDataForSubscriberWithSelfPermissions";
                case 6:
                    return "sendTextForSubscriber";
                case 7:
                    return "sendTextForSubscriberWithSelfPermissions";
                case 8:
                    return "sendTextForSubscriberWithOptions";
                case 9:
                    return "injectSmsPduForSubscriber";
                case 10:
                    return "sendMultipartTextForSubscriber";
                case 11:
                    return "sendMultipartTextForSubscriberWithOptions";
                case 12:
                    return "enableCellBroadcastForSubscriber";
                case 13:
                    return "disableCellBroadcastForSubscriber";
                case 14:
                    return "enableCellBroadcastRangeForSubscriber";
                case 15:
                    return "disableCellBroadcastRangeForSubscriber";
                case 16:
                    return "getPremiumSmsPermission";
                case 17:
                    return "getPremiumSmsPermissionForSubscriber";
                case 18:
                    return "setPremiumSmsPermission";
                case 19:
                    return "setPremiumSmsPermissionForSubscriber";
                case 20:
                    return "isImsSmsSupportedForSubscriber";
                case 21:
                    return "isSmsSimPickActivityNeeded";
                case 22:
                    return "getPreferredSmsSubscription";
                case 23:
                    return "getImsSmsFormatForSubscriber";
                case 24:
                    return "isSMSPromptEnabled";
                case 25:
                    return "sendStoredText";
                case 26:
                    return "sendStoredMultipartText";
                case 27:
                    return "createAppSpecificSmsToken";
                case 28:
                    return "createAppSpecificSmsTokenWithPackageInfo";
                case 29:
                    return "getSmsMessagesForFinancialApp";
                case 30:
                    return "getSmsCapacityOnIccForSubscriber";
                case 31:
                    return "checkSmsShortCodeDestination";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                boolean _result;
                int _arg0;
                String _arg1;
                String _arg3;
                int _arg4;
                byte[] _arg5;
                PendingIntent _arg6;
                PendingIntent _arg7;
                String _arg42;
                PendingIntent _arg52;
                int _arg02;
                String _arg2;
                PendingIntent _arg32;
                boolean _result2;
                boolean _result3;
                int _result4;
                boolean _result5;
                String _result6;
                int _arg03;
                String _arg12;
                Uri _arg22;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        List<SmsRawData> _result7 = getAllMessagesFromIccEfForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result7);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _result = updateMessageOnIccEfForSubscriber(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _result = copyMessageToIccEfForSubscriber(data.readInt(), data.readString(), data.readInt(), data.createByteArray(), data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readString();
                        descriptor = data.readString();
                        _arg3 = data.readString();
                        _arg4 = data.readInt();
                        _arg5 = data.createByteArray();
                        if (data.readInt() != 0) {
                            _arg6 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg7 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg7 = null;
                        }
                        sendDataForSubscriber(_arg0, _arg1, descriptor, _arg3, _arg4, _arg5, _arg6, _arg7);
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readString();
                        descriptor = data.readString();
                        _arg3 = data.readString();
                        _arg4 = data.readInt();
                        _arg5 = data.createByteArray();
                        if (data.readInt() != 0) {
                            _arg6 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg7 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg7 = null;
                        }
                        sendDataForSubscriberWithSelfPermissions(_arg0, _arg1, descriptor, _arg3, _arg4, _arg5, _arg6, _arg7);
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readString();
                        descriptor = data.readString();
                        _arg3 = data.readString();
                        _arg42 = data.readString();
                        if (data.readInt() != 0) {
                            _arg52 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg52 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg6 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        sendTextForSubscriber(_arg0, _arg1, descriptor, _arg3, _arg42, _arg52, _arg6, data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readString();
                        descriptor = data.readString();
                        _arg3 = data.readString();
                        _arg42 = data.readString();
                        if (data.readInt() != 0) {
                            _arg52 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg52 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg6 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        sendTextForSubscriberWithSelfPermissions(_arg0, _arg1, descriptor, _arg3, _arg42, _arg52, _arg6, data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    case 8:
                        PendingIntent _arg53;
                        PendingIntent _arg62;
                        parcel.enforceInterface(descriptor);
                        int _arg04 = data.readInt();
                        _arg42 = data.readString();
                        String _arg23 = data.readString();
                        String _arg33 = data.readString();
                        String _arg43 = data.readString();
                        if (data.readInt() != 0) {
                            _arg53 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg53 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg62 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg62 = null;
                        }
                        sendTextForSubscriberWithOptions(_arg04, _arg42, _arg23, _arg33, _arg43, _arg53, _arg62, data.readInt() != 0, data.readInt(), data.readInt() != 0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        byte[] _arg13 = data.createByteArray();
                        _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg32 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        injectSmsPduForSubscriber(_arg02, _arg13, _arg2, _arg32);
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        sendMultipartTextForSubscriber(data.readInt(), data.readString(), data.readString(), data.readString(), data.createStringArrayList(), parcel.createTypedArrayList(PendingIntent.CREATOR), parcel.createTypedArrayList(PendingIntent.CREATOR), data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        sendMultipartTextForSubscriberWithOptions(data.readInt(), data.readString(), data.readString(), data.readString(), data.createStringArrayList(), parcel.createTypedArrayList(PendingIntent.CREATOR), parcel.createTypedArrayList(PendingIntent.CREATOR), data.readInt() != 0, data.readInt(), data.readInt() != 0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _result2 = enableCellBroadcastForSubscriber(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _result2 = disableCellBroadcastForSubscriber(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _result3 = enableCellBroadcastRangeForSubscriber(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _result3 = disableCellBroadcastRangeForSubscriber(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        _result4 = getPremiumSmsPermission(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        int _result8 = getPremiumSmsPermissionForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        setPremiumSmsPermission(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        setPremiumSmsPermissionForSubscriber(data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _result5 = isImsSmsSupportedForSubscriber(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _result5 = isSmsSimPickActivityNeeded(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        _arg02 = getPreferredSmsSubscription();
                        reply.writeNoException();
                        parcel2.writeInt(_arg02);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _result6 = getImsSmsFormatForSubscriber(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result6);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _result = isSMSPromptEnabled();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 25:
                        PendingIntent _arg44;
                        PendingIntent _arg54;
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg22 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg44 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg44 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg54 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg54 = null;
                        }
                        sendStoredText(_arg03, _arg12, _arg22, _arg1, _arg44, _arg54);
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        _arg03 = data.readInt();
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg22 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        sendStoredMultipartText(_arg03, _arg12, _arg22, data.readString(), parcel.createTypedArrayList(PendingIntent.CREATOR), parcel.createTypedArrayList(PendingIntent.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 27:
                        PendingIntent _arg24;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        _result6 = data.readString();
                        if (data.readInt() != 0) {
                            _arg24 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        String _result9 = createAppSpecificSmsToken(_arg02, _result6, _arg24);
                        reply.writeNoException();
                        parcel2.writeString(_result9);
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        _result6 = data.readString();
                        _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg32 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        String _result10 = createAppSpecificSmsTokenWithPackageInfo(_arg02, _result6, _arg2, _arg32);
                        reply.writeNoException();
                        parcel2.writeString(_result10);
                        return true;
                    case 29:
                        Bundle _arg25;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        _result6 = data.readString();
                        if (data.readInt() != 0) {
                            _arg25 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg25 = null;
                        }
                        getSmsMessagesForFinancialApp(_arg02, _result6, _arg25, android.telephony.IFinancialSmsCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        _result4 = getSmsCapacityOnIccForSubscriber(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        int _result11 = checkSmsShortCodeDestination(data.readInt(), data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ISms impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISms getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int checkSmsShortCodeDestination(int i, String str, String str2, String str3) throws RemoteException;

    boolean copyMessageToIccEfForSubscriber(int i, String str, int i2, byte[] bArr, byte[] bArr2) throws RemoteException;

    String createAppSpecificSmsToken(int i, String str, PendingIntent pendingIntent) throws RemoteException;

    String createAppSpecificSmsTokenWithPackageInfo(int i, String str, String str2, PendingIntent pendingIntent) throws RemoteException;

    boolean disableCellBroadcastForSubscriber(int i, int i2, int i3) throws RemoteException;

    boolean disableCellBroadcastRangeForSubscriber(int i, int i2, int i3, int i4) throws RemoteException;

    boolean enableCellBroadcastForSubscriber(int i, int i2, int i3) throws RemoteException;

    boolean enableCellBroadcastRangeForSubscriber(int i, int i2, int i3, int i4) throws RemoteException;

    List<SmsRawData> getAllMessagesFromIccEfForSubscriber(int i, String str) throws RemoteException;

    String getImsSmsFormatForSubscriber(int i) throws RemoteException;

    int getPreferredSmsSubscription() throws RemoteException;

    int getPremiumSmsPermission(String str) throws RemoteException;

    int getPremiumSmsPermissionForSubscriber(int i, String str) throws RemoteException;

    int getSmsCapacityOnIccForSubscriber(int i) throws RemoteException;

    void getSmsMessagesForFinancialApp(int i, String str, Bundle bundle, IFinancialSmsCallback iFinancialSmsCallback) throws RemoteException;

    void injectSmsPduForSubscriber(int i, byte[] bArr, String str, PendingIntent pendingIntent) throws RemoteException;

    boolean isImsSmsSupportedForSubscriber(int i) throws RemoteException;

    boolean isSMSPromptEnabled() throws RemoteException;

    boolean isSmsSimPickActivityNeeded(int i) throws RemoteException;

    void sendDataForSubscriber(int i, String str, String str2, String str3, int i2, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    void sendDataForSubscriberWithSelfPermissions(int i, String str, String str2, String str3, int i2, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    void sendMultipartTextForSubscriber(int i, String str, String str2, String str3, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean z) throws RemoteException;

    void sendMultipartTextForSubscriberWithOptions(int i, String str, String str2, String str3, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean z, int i2, boolean z2, int i3) throws RemoteException;

    void sendStoredMultipartText(int i, String str, Uri uri, String str2, List<PendingIntent> list, List<PendingIntent> list2) throws RemoteException;

    void sendStoredText(int i, String str, Uri uri, String str2, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    void sendTextForSubscriber(int i, String str, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z) throws RemoteException;

    void sendTextForSubscriberWithOptions(int i, String str, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, int i2, boolean z2, int i3) throws RemoteException;

    void sendTextForSubscriberWithSelfPermissions(int i, String str, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z) throws RemoteException;

    void setPremiumSmsPermission(String str, int i) throws RemoteException;

    void setPremiumSmsPermissionForSubscriber(int i, String str, int i2) throws RemoteException;

    boolean updateMessageOnIccEfForSubscriber(int i, String str, int i2, int i3, byte[] bArr) throws RemoteException;
}
