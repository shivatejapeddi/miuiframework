package android.service.carrier;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface ICarrierMessagingService extends IInterface {

    public static abstract class Stub extends Binder implements ICarrierMessagingService {
        private static final String DESCRIPTOR = "android.service.carrier.ICarrierMessagingService";
        static final int TRANSACTION_downloadMms = 6;
        static final int TRANSACTION_filterSms = 1;
        static final int TRANSACTION_sendDataSms = 3;
        static final int TRANSACTION_sendMms = 5;
        static final int TRANSACTION_sendMultipartTextSms = 4;
        static final int TRANSACTION_sendTextSms = 2;

        private static class Proxy implements ICarrierMessagingService {
            public static ICarrierMessagingService sDefaultImpl;
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

            public void filterSms(MessagePdu pdu, String format, int destPort, int subId, ICarrierMessagingCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (pdu != null) {
                        _data.writeInt(1);
                        pdu.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(format);
                    _data.writeInt(destPort);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().filterSms(pdu, format, destPort, subId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void sendTextSms(String text, int subId, String destAddress, int sendSmsFlag, ICarrierMessagingCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(text);
                    _data.writeInt(subId);
                    _data.writeString(destAddress);
                    _data.writeInt(sendSmsFlag);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().sendTextSms(text, subId, destAddress, sendSmsFlag, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void sendDataSms(byte[] data, int subId, String destAddress, int destPort, int sendSmsFlag, ICarrierMessagingCallback callback) throws RemoteException {
                Throwable th;
                int i;
                String str;
                int i2;
                int i3;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeByteArray(data);
                    } catch (Throwable th2) {
                        th = th2;
                        i = subId;
                        str = destAddress;
                        i2 = destPort;
                        i3 = sendSmsFlag;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(subId);
                        try {
                            _data.writeString(destAddress);
                            try {
                                _data.writeInt(destPort);
                                try {
                                    _data.writeInt(sendSmsFlag);
                                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                                } catch (Throwable th3) {
                                    th = th3;
                                    _data.recycle();
                                    throw th;
                                }
                                try {
                                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                        _data.recycle();
                                        return;
                                    }
                                    Stub.getDefaultImpl().sendDataSms(data, subId, destAddress, destPort, sendSmsFlag, callback);
                                    _data.recycle();
                                } catch (Throwable th4) {
                                    th = th4;
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th5) {
                                th = th5;
                                i3 = sendSmsFlag;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th6) {
                            th = th6;
                            i2 = destPort;
                            i3 = sendSmsFlag;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        str = destAddress;
                        i2 = destPort;
                        i3 = sendSmsFlag;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    byte[] bArr = data;
                    i = subId;
                    str = destAddress;
                    i2 = destPort;
                    i3 = sendSmsFlag;
                    _data.recycle();
                    throw th;
                }
            }

            public void sendMultipartTextSms(List<String> parts, int subId, String destAddress, int sendSmsFlag, ICarrierMessagingCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(parts);
                    _data.writeInt(subId);
                    _data.writeString(destAddress);
                    _data.writeInt(sendSmsFlag);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().sendMultipartTextSms(parts, subId, destAddress, sendSmsFlag, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void sendMms(Uri pduUri, int subId, Uri location, ICarrierMessagingCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (pduUri != null) {
                        _data.writeInt(1);
                        pduUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(subId);
                    if (location != null) {
                        _data.writeInt(1);
                        location.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().sendMms(pduUri, subId, location, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void downloadMms(Uri pduUri, int subId, Uri location, ICarrierMessagingCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (pduUri != null) {
                        _data.writeInt(1);
                        pduUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(subId);
                    if (location != null) {
                        _data.writeInt(1);
                        location.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().downloadMms(pduUri, subId, location, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICarrierMessagingService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICarrierMessagingService)) {
                return new Proxy(obj);
            }
            return (ICarrierMessagingService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "filterSms";
                case 2:
                    return "sendTextSms";
                case 3:
                    return "sendDataSms";
                case 4:
                    return "sendMultipartTextSms";
                case 5:
                    return "sendMms";
                case 6:
                    return "downloadMms";
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
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                Uri _arg0;
                int _arg1;
                Uri _arg2;
                switch (i) {
                    case 1:
                        MessagePdu _arg02;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (MessagePdu) MessagePdu.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        filterSms(_arg02, data.readString(), data.readInt(), data.readInt(), android.service.carrier.ICarrierMessagingCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        sendTextSms(data.readString(), data.readInt(), data.readString(), data.readInt(), android.service.carrier.ICarrierMessagingCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        sendDataSms(data.createByteArray(), data.readInt(), data.readString(), data.readInt(), data.readInt(), android.service.carrier.ICarrierMessagingCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        sendMultipartTextSms(data.createStringArrayList(), data.readInt(), data.readString(), data.readInt(), android.service.carrier.ICarrierMessagingCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        sendMms(_arg0, _arg1, _arg2, android.service.carrier.ICarrierMessagingCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        downloadMms(_arg0, _arg1, _arg2, android.service.carrier.ICarrierMessagingCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ICarrierMessagingService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ICarrierMessagingService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements ICarrierMessagingService {
        public void filterSms(MessagePdu pdu, String format, int destPort, int subId, ICarrierMessagingCallback callback) throws RemoteException {
        }

        public void sendTextSms(String text, int subId, String destAddress, int sendSmsFlag, ICarrierMessagingCallback callback) throws RemoteException {
        }

        public void sendDataSms(byte[] data, int subId, String destAddress, int destPort, int sendSmsFlag, ICarrierMessagingCallback callback) throws RemoteException {
        }

        public void sendMultipartTextSms(List<String> list, int subId, String destAddress, int sendSmsFlag, ICarrierMessagingCallback callback) throws RemoteException {
        }

        public void sendMms(Uri pduUri, int subId, Uri location, ICarrierMessagingCallback callback) throws RemoteException {
        }

        public void downloadMms(Uri pduUri, int subId, Uri location, ICarrierMessagingCallback callback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void downloadMms(Uri uri, int i, Uri uri2, ICarrierMessagingCallback iCarrierMessagingCallback) throws RemoteException;

    void filterSms(MessagePdu messagePdu, String str, int i, int i2, ICarrierMessagingCallback iCarrierMessagingCallback) throws RemoteException;

    void sendDataSms(byte[] bArr, int i, String str, int i2, int i3, ICarrierMessagingCallback iCarrierMessagingCallback) throws RemoteException;

    void sendMms(Uri uri, int i, Uri uri2, ICarrierMessagingCallback iCarrierMessagingCallback) throws RemoteException;

    void sendMultipartTextSms(List<String> list, int i, String str, int i2, ICarrierMessagingCallback iCarrierMessagingCallback) throws RemoteException;

    void sendTextSms(String str, int i, String str2, int i2, ICarrierMessagingCallback iCarrierMessagingCallback) throws RemoteException;
}
