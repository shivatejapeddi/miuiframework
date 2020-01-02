package com.android.internal.telephony;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ImsiEncryptionInfo;

public interface IPhoneSubInfo extends IInterface {

    public static class Default implements IPhoneSubInfo {
        public String getDeviceId(String callingPackage) throws RemoteException {
            return null;
        }

        public String getNaiForSubscriber(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public String getDeviceIdForPhone(int phoneId, String callingPackage) throws RemoteException {
            return null;
        }

        public String getImeiForSubscriber(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public String getDeviceSvn(String callingPackage) throws RemoteException {
            return null;
        }

        public String getDeviceSvnUsingSubId(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public String getSubscriberId(String callingPackage) throws RemoteException {
            return null;
        }

        public String getSubscriberIdForSubscriber(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public String getGroupIdLevel1ForSubscriber(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public String getIccSerialNumber(String callingPackage) throws RemoteException {
            return null;
        }

        public String getIccSerialNumberForSubscriber(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public String getLine1Number(String callingPackage) throws RemoteException {
            return null;
        }

        public String getLine1NumberForSubscriber(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public String getLine1AlphaTag(String callingPackage) throws RemoteException {
            return null;
        }

        public String getLine1AlphaTagForSubscriber(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public String getMsisdn(String callingPackage) throws RemoteException {
            return null;
        }

        public String getMsisdnForSubscriber(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public String getVoiceMailNumber(String callingPackage) throws RemoteException {
            return null;
        }

        public String getVoiceMailNumberForSubscriber(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int subId, int keyType, String callingPackage) throws RemoteException {
            return null;
        }

        public void setCarrierInfoForImsiEncryption(int subId, String callingPackage, ImsiEncryptionInfo imsiEncryptionInfo) throws RemoteException {
        }

        public void resetCarrierKeysForImsiEncryption(int subId, String callingPackage) throws RemoteException {
        }

        public String getVoiceMailAlphaTag(String callingPackage) throws RemoteException {
            return null;
        }

        public String getVoiceMailAlphaTagForSubscriber(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public String getIsimImpi(int subId) throws RemoteException {
            return null;
        }

        public String getIsimDomain(int subId) throws RemoteException {
            return null;
        }

        public String[] getIsimImpu(int subId) throws RemoteException {
            return null;
        }

        public String getIsimIst(int subId) throws RemoteException {
            return null;
        }

        public String[] getIsimPcscf(int subId) throws RemoteException {
            return null;
        }

        public String getIccSimChallengeResponse(int subId, int appType, int authType, String data) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPhoneSubInfo {
        private static final String DESCRIPTOR = "com.android.internal.telephony.IPhoneSubInfo";
        static final int TRANSACTION_getCarrierInfoForImsiEncryption = 20;
        static final int TRANSACTION_getDeviceId = 1;
        static final int TRANSACTION_getDeviceIdForPhone = 3;
        static final int TRANSACTION_getDeviceSvn = 5;
        static final int TRANSACTION_getDeviceSvnUsingSubId = 6;
        static final int TRANSACTION_getGroupIdLevel1ForSubscriber = 9;
        static final int TRANSACTION_getIccSerialNumber = 10;
        static final int TRANSACTION_getIccSerialNumberForSubscriber = 11;
        static final int TRANSACTION_getIccSimChallengeResponse = 30;
        static final int TRANSACTION_getImeiForSubscriber = 4;
        static final int TRANSACTION_getIsimDomain = 26;
        static final int TRANSACTION_getIsimImpi = 25;
        static final int TRANSACTION_getIsimImpu = 27;
        static final int TRANSACTION_getIsimIst = 28;
        static final int TRANSACTION_getIsimPcscf = 29;
        static final int TRANSACTION_getLine1AlphaTag = 14;
        static final int TRANSACTION_getLine1AlphaTagForSubscriber = 15;
        static final int TRANSACTION_getLine1Number = 12;
        static final int TRANSACTION_getLine1NumberForSubscriber = 13;
        static final int TRANSACTION_getMsisdn = 16;
        static final int TRANSACTION_getMsisdnForSubscriber = 17;
        static final int TRANSACTION_getNaiForSubscriber = 2;
        static final int TRANSACTION_getSubscriberId = 7;
        static final int TRANSACTION_getSubscriberIdForSubscriber = 8;
        static final int TRANSACTION_getVoiceMailAlphaTag = 23;
        static final int TRANSACTION_getVoiceMailAlphaTagForSubscriber = 24;
        static final int TRANSACTION_getVoiceMailNumber = 18;
        static final int TRANSACTION_getVoiceMailNumberForSubscriber = 19;
        static final int TRANSACTION_resetCarrierKeysForImsiEncryption = 22;
        static final int TRANSACTION_setCarrierInfoForImsiEncryption = 21;

        private static class Proxy implements IPhoneSubInfo {
            public static IPhoneSubInfo sDefaultImpl;
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

            public String getDeviceId(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    String str = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDeviceId(callingPackage);
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

            public String getNaiForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    String str = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getNaiForSubscriber(subId, callingPackage);
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

            public String getDeviceIdForPhone(int phoneId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeString(callingPackage);
                    String str = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDeviceIdForPhone(phoneId, callingPackage);
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

            public String getImeiForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    String str = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getImeiForSubscriber(subId, callingPackage);
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

            public String getDeviceSvn(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    String str = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDeviceSvn(callingPackage);
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

            public String getDeviceSvnUsingSubId(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    String str = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDeviceSvnUsingSubId(subId, callingPackage);
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

            public String getSubscriberId(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    String str = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSubscriberId(callingPackage);
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

            public String getSubscriberIdForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    String str = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSubscriberIdForSubscriber(subId, callingPackage);
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

            public String getGroupIdLevel1ForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    String str = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getGroupIdLevel1ForSubscriber(subId, callingPackage);
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

            public String getIccSerialNumber(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    String str = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getIccSerialNumber(callingPackage);
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

            public String getIccSerialNumberForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    String str = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getIccSerialNumberForSubscriber(subId, callingPackage);
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

            public String getLine1Number(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    String str = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getLine1Number(callingPackage);
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

            public String getLine1NumberForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    String str = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getLine1NumberForSubscriber(subId, callingPackage);
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

            public String getLine1AlphaTag(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    String str = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getLine1AlphaTag(callingPackage);
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

            public String getLine1AlphaTagForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    String str = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getLine1AlphaTagForSubscriber(subId, callingPackage);
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

            public String getMsisdn(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    String str = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getMsisdn(callingPackage);
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

            public String getMsisdnForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    String str = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getMsisdnForSubscriber(subId, callingPackage);
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

            public String getVoiceMailNumber(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    String str = 18;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getVoiceMailNumber(callingPackage);
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

            public String getVoiceMailNumberForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    String str = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getVoiceMailNumberForSubscriber(subId, callingPackage);
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

            public ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int subId, int keyType, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(keyType);
                    _data.writeString(callingPackage);
                    ImsiEncryptionInfo imsiEncryptionInfo = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        imsiEncryptionInfo = Stub.getDefaultImpl();
                        if (imsiEncryptionInfo != 0) {
                            imsiEncryptionInfo = Stub.getDefaultImpl().getCarrierInfoForImsiEncryption(subId, keyType, callingPackage);
                            return imsiEncryptionInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        imsiEncryptionInfo = (ImsiEncryptionInfo) ImsiEncryptionInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        imsiEncryptionInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return imsiEncryptionInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCarrierInfoForImsiEncryption(int subId, String callingPackage, ImsiEncryptionInfo imsiEncryptionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    if (imsiEncryptionInfo != null) {
                        _data.writeInt(1);
                        imsiEncryptionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCarrierInfoForImsiEncryption(subId, callingPackage, imsiEncryptionInfo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resetCarrierKeysForImsiEncryption(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resetCarrierKeysForImsiEncryption(subId, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getVoiceMailAlphaTag(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    String str = 23;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getVoiceMailAlphaTag(callingPackage);
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

            public String getVoiceMailAlphaTagForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    String str = 24;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getVoiceMailAlphaTagForSubscriber(subId, callingPackage);
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

            public String getIsimImpi(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String str = 25;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getIsimImpi(subId);
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

            public String getIsimDomain(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String str = 26;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getIsimDomain(subId);
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

            public String[] getIsimImpu(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String[] strArr = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getIsimImpu(subId);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getIsimIst(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String str = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getIsimIst(subId);
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

            public String[] getIsimPcscf(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String[] strArr = 29;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getIsimPcscf(subId);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getIccSimChallengeResponse(int subId, int appType, int authType, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(appType);
                    _data.writeInt(authType);
                    _data.writeString(data);
                    String str = 30;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getIccSimChallengeResponse(subId, appType, authType, data);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPhoneSubInfo asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPhoneSubInfo)) {
                return new Proxy(obj);
            }
            return (IPhoneSubInfo) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getDeviceId";
                case 2:
                    return "getNaiForSubscriber";
                case 3:
                    return "getDeviceIdForPhone";
                case 4:
                    return "getImeiForSubscriber";
                case 5:
                    return "getDeviceSvn";
                case 6:
                    return "getDeviceSvnUsingSubId";
                case 7:
                    return "getSubscriberId";
                case 8:
                    return "getSubscriberIdForSubscriber";
                case 9:
                    return "getGroupIdLevel1ForSubscriber";
                case 10:
                    return "getIccSerialNumber";
                case 11:
                    return "getIccSerialNumberForSubscriber";
                case 12:
                    return "getLine1Number";
                case 13:
                    return "getLine1NumberForSubscriber";
                case 14:
                    return "getLine1AlphaTag";
                case 15:
                    return "getLine1AlphaTagForSubscriber";
                case 16:
                    return "getMsisdn";
                case 17:
                    return "getMsisdnForSubscriber";
                case 18:
                    return "getVoiceMailNumber";
                case 19:
                    return "getVoiceMailNumberForSubscriber";
                case 20:
                    return "getCarrierInfoForImsiEncryption";
                case 21:
                    return "setCarrierInfoForImsiEncryption";
                case 22:
                    return "resetCarrierKeysForImsiEncryption";
                case 23:
                    return "getVoiceMailAlphaTag";
                case 24:
                    return "getVoiceMailAlphaTagForSubscriber";
                case 25:
                    return "getIsimImpi";
                case 26:
                    return "getIsimDomain";
                case 27:
                    return "getIsimImpu";
                case 28:
                    return "getIsimIst";
                case 29:
                    return "getIsimPcscf";
                case 30:
                    return "getIccSimChallengeResponse";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code != IBinder.INTERFACE_TRANSACTION) {
                String _result;
                String _result2;
                String[] _result3;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _result = getDeviceId(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result2 = getNaiForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _result2 = getDeviceIdForPhone(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result2 = getImeiForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result = getDeviceSvn(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result2 = getDeviceSvnUsingSubId(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result = getSubscriberId(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result2 = getSubscriberIdForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _result2 = getGroupIdLevel1ForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _result = getIccSerialNumber(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        _result2 = getIccSerialNumberForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        _result = getLine1Number(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _result2 = getLine1NumberForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        _result = getLine1AlphaTag(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        _result2 = getLine1AlphaTagForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        _result = getMsisdn(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        _result2 = getMsisdnForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        _result = getVoiceMailNumber(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        _result2 = getVoiceMailNumberForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        ImsiEncryptionInfo _result4 = getCarrierInfoForImsiEncryption(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result4 != null) {
                            reply.writeInt(1);
                            _result4.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 21:
                        ImsiEncryptionInfo _arg2;
                        data.enforceInterface(descriptor);
                        int _arg0 = data.readInt();
                        _result = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (ImsiEncryptionInfo) ImsiEncryptionInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        setCarrierInfoForImsiEncryption(_arg0, _result, _arg2);
                        reply.writeNoException();
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        resetCarrierKeysForImsiEncryption(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        _result = getVoiceMailAlphaTag(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 24:
                        data.enforceInterface(descriptor);
                        _result2 = getVoiceMailAlphaTagForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 25:
                        data.enforceInterface(descriptor);
                        _result = getIsimImpi(data.readInt());
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        _result = getIsimDomain(data.readInt());
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        _result3 = getIsimImpu(data.readInt());
                        reply.writeNoException();
                        reply.writeStringArray(_result3);
                        return true;
                    case 28:
                        data.enforceInterface(descriptor);
                        _result = getIsimIst(data.readInt());
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 29:
                        data.enforceInterface(descriptor);
                        _result3 = getIsimPcscf(data.readInt());
                        reply.writeNoException();
                        reply.writeStringArray(_result3);
                        return true;
                    case 30:
                        data.enforceInterface(descriptor);
                        String _result5 = getIccSimChallengeResponse(data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result5);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IPhoneSubInfo impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPhoneSubInfo getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int i, int i2, String str) throws RemoteException;

    String getDeviceId(String str) throws RemoteException;

    String getDeviceIdForPhone(int i, String str) throws RemoteException;

    String getDeviceSvn(String str) throws RemoteException;

    String getDeviceSvnUsingSubId(int i, String str) throws RemoteException;

    String getGroupIdLevel1ForSubscriber(int i, String str) throws RemoteException;

    @UnsupportedAppUsage
    String getIccSerialNumber(String str) throws RemoteException;

    String getIccSerialNumberForSubscriber(int i, String str) throws RemoteException;

    String getIccSimChallengeResponse(int i, int i2, int i3, String str) throws RemoteException;

    String getImeiForSubscriber(int i, String str) throws RemoteException;

    String getIsimDomain(int i) throws RemoteException;

    String getIsimImpi(int i) throws RemoteException;

    String[] getIsimImpu(int i) throws RemoteException;

    String getIsimIst(int i) throws RemoteException;

    String[] getIsimPcscf(int i) throws RemoteException;

    String getLine1AlphaTag(String str) throws RemoteException;

    String getLine1AlphaTagForSubscriber(int i, String str) throws RemoteException;

    String getLine1Number(String str) throws RemoteException;

    String getLine1NumberForSubscriber(int i, String str) throws RemoteException;

    String getMsisdn(String str) throws RemoteException;

    String getMsisdnForSubscriber(int i, String str) throws RemoteException;

    String getNaiForSubscriber(int i, String str) throws RemoteException;

    @UnsupportedAppUsage
    String getSubscriberId(String str) throws RemoteException;

    String getSubscriberIdForSubscriber(int i, String str) throws RemoteException;

    String getVoiceMailAlphaTag(String str) throws RemoteException;

    String getVoiceMailAlphaTagForSubscriber(int i, String str) throws RemoteException;

    String getVoiceMailNumber(String str) throws RemoteException;

    String getVoiceMailNumberForSubscriber(int i, String str) throws RemoteException;

    void resetCarrierKeysForImsiEncryption(int i, String str) throws RemoteException;

    void setCarrierInfoForImsiEncryption(int i, String str, ImsiEncryptionInfo imsiEncryptionInfo) throws RemoteException;
}
