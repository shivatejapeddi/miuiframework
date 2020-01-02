package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.feature.CapabilityChangeRequest;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsEcbm;
import com.android.ims.internal.IImsMultiEndpoint;
import com.android.ims.internal.IImsUt;

public interface IImsMmTelFeature extends IInterface {

    public static class Default implements IImsMmTelFeature {
        public void setListener(IImsMmTelListener l) throws RemoteException {
        }

        public int getFeatureState() throws RemoteException {
            return 0;
        }

        public ImsCallProfile createCallProfile(int callSessionType, int callType) throws RemoteException {
            return null;
        }

        public IImsCallSession createCallSession(ImsCallProfile profile) throws RemoteException {
            return null;
        }

        public int shouldProcessCall(String[] uris) throws RemoteException {
            return 0;
        }

        public IImsUt getUtInterface() throws RemoteException {
            return null;
        }

        public IImsEcbm getEcbmInterface() throws RemoteException {
            return null;
        }

        public void setUiTtyMode(int uiTtyMode, Message onCompleteMessage) throws RemoteException {
        }

        public IImsMultiEndpoint getMultiEndpointInterface() throws RemoteException {
            return null;
        }

        public int queryCapabilityStatus() throws RemoteException {
            return 0;
        }

        public void addCapabilityCallback(IImsCapabilityCallback c) throws RemoteException {
        }

        public void removeCapabilityCallback(IImsCapabilityCallback c) throws RemoteException {
        }

        public void changeCapabilitiesConfiguration(CapabilityChangeRequest request, IImsCapabilityCallback c) throws RemoteException {
        }

        public void queryCapabilityConfiguration(int capability, int radioTech, IImsCapabilityCallback c) throws RemoteException {
        }

        public void setSmsListener(IImsSmsListener l) throws RemoteException {
        }

        public void sendSms(int token, int messageRef, String format, String smsc, boolean retry, byte[] pdu) throws RemoteException {
        }

        public void acknowledgeSms(int token, int messageRef, int result) throws RemoteException {
        }

        public void acknowledgeSmsReport(int token, int messageRef, int result) throws RemoteException {
        }

        public String getSmsFormat() throws RemoteException {
            return null;
        }

        public void onSmsReady() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IImsMmTelFeature {
        private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsMmTelFeature";
        static final int TRANSACTION_acknowledgeSms = 17;
        static final int TRANSACTION_acknowledgeSmsReport = 18;
        static final int TRANSACTION_addCapabilityCallback = 11;
        static final int TRANSACTION_changeCapabilitiesConfiguration = 13;
        static final int TRANSACTION_createCallProfile = 3;
        static final int TRANSACTION_createCallSession = 4;
        static final int TRANSACTION_getEcbmInterface = 7;
        static final int TRANSACTION_getFeatureState = 2;
        static final int TRANSACTION_getMultiEndpointInterface = 9;
        static final int TRANSACTION_getSmsFormat = 19;
        static final int TRANSACTION_getUtInterface = 6;
        static final int TRANSACTION_onSmsReady = 20;
        static final int TRANSACTION_queryCapabilityConfiguration = 14;
        static final int TRANSACTION_queryCapabilityStatus = 10;
        static final int TRANSACTION_removeCapabilityCallback = 12;
        static final int TRANSACTION_sendSms = 16;
        static final int TRANSACTION_setListener = 1;
        static final int TRANSACTION_setSmsListener = 15;
        static final int TRANSACTION_setUiTtyMode = 8;
        static final int TRANSACTION_shouldProcessCall = 5;

        private static class Proxy implements IImsMmTelFeature {
            public static IImsMmTelFeature sDefaultImpl;
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

            public void setListener(IImsMmTelListener l) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(l != null ? l.asBinder() : null);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setListener(l);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getFeatureState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getFeatureState();
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

            public ImsCallProfile createCallProfile(int callSessionType, int callType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callSessionType);
                    _data.writeInt(callType);
                    ImsCallProfile imsCallProfile = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        imsCallProfile = Stub.getDefaultImpl();
                        if (imsCallProfile != 0) {
                            imsCallProfile = Stub.getDefaultImpl().createCallProfile(callSessionType, callType);
                            return imsCallProfile;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        imsCallProfile = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(_reply);
                    } else {
                        imsCallProfile = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return imsCallProfile;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsCallSession createCallSession(ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    IImsCallSession iImsCallSession = this.mRemote;
                    if (!iImsCallSession.transact(4, _data, _reply, 0)) {
                        iImsCallSession = Stub.getDefaultImpl();
                        if (iImsCallSession != null) {
                            iImsCallSession = Stub.getDefaultImpl().createCallSession(profile);
                            return iImsCallSession;
                        }
                    }
                    _reply.readException();
                    iImsCallSession = com.android.ims.internal.IImsCallSession.Stub.asInterface(_reply.readStrongBinder());
                    IImsCallSession _result = iImsCallSession;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int shouldProcessCall(String[] uris) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(uris);
                    int i = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().shouldProcessCall(uris);
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

            public IImsUt getUtInterface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IImsUt iImsUt = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        iImsUt = Stub.getDefaultImpl();
                        if (iImsUt != 0) {
                            iImsUt = Stub.getDefaultImpl().getUtInterface();
                            return iImsUt;
                        }
                    }
                    _reply.readException();
                    iImsUt = com.android.ims.internal.IImsUt.Stub.asInterface(_reply.readStrongBinder());
                    IImsUt _result = iImsUt;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsEcbm getEcbmInterface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IImsEcbm iImsEcbm = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        iImsEcbm = Stub.getDefaultImpl();
                        if (iImsEcbm != 0) {
                            iImsEcbm = Stub.getDefaultImpl().getEcbmInterface();
                            return iImsEcbm;
                        }
                    }
                    _reply.readException();
                    iImsEcbm = com.android.ims.internal.IImsEcbm.Stub.asInterface(_reply.readStrongBinder());
                    IImsEcbm _result = iImsEcbm;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUiTtyMode(int uiTtyMode, Message onCompleteMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uiTtyMode);
                    if (onCompleteMessage != null) {
                        _data.writeInt(1);
                        onCompleteMessage.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUiTtyMode(uiTtyMode, onCompleteMessage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsMultiEndpoint getMultiEndpointInterface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IImsMultiEndpoint iImsMultiEndpoint = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        iImsMultiEndpoint = Stub.getDefaultImpl();
                        if (iImsMultiEndpoint != 0) {
                            iImsMultiEndpoint = Stub.getDefaultImpl().getMultiEndpointInterface();
                            return iImsMultiEndpoint;
                        }
                    }
                    _reply.readException();
                    iImsMultiEndpoint = com.android.ims.internal.IImsMultiEndpoint.Stub.asInterface(_reply.readStrongBinder());
                    IImsMultiEndpoint _result = iImsMultiEndpoint;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int queryCapabilityStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().queryCapabilityStatus();
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

            public void addCapabilityCallback(IImsCapabilityCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().addCapabilityCallback(c);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void removeCapabilityCallback(IImsCapabilityCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().removeCapabilityCallback(c);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void changeCapabilitiesConfiguration(CapabilityChangeRequest request, IImsCapabilityCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().changeCapabilitiesConfiguration(request, c);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void queryCapabilityConfiguration(int capability, int radioTech, IImsCapabilityCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(capability);
                    _data.writeInt(radioTech);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().queryCapabilityConfiguration(capability, radioTech, c);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setSmsListener(IImsSmsListener l) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(l != null ? l.asBinder() : null);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSmsListener(l);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendSms(int token, int messageRef, String format, String smsc, boolean retry, byte[] pdu) throws RemoteException {
                Throwable th;
                int i;
                String str;
                String str2;
                byte[] bArr;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(token);
                    } catch (Throwable th2) {
                        th = th2;
                        i = messageRef;
                        str = format;
                        str2 = smsc;
                        bArr = pdu;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(messageRef);
                        try {
                            _data.writeString(format);
                            try {
                                _data.writeString(smsc);
                                _data.writeInt(retry ? 1 : 0);
                            } catch (Throwable th3) {
                                th = th3;
                                bArr = pdu;
                                _data.recycle();
                                throw th;
                            }
                            try {
                                _data.writeByteArray(pdu);
                                try {
                                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                        _data.recycle();
                                        return;
                                    }
                                    Stub.getDefaultImpl().sendSms(token, messageRef, format, smsc, retry, pdu);
                                    _data.recycle();
                                } catch (Throwable th4) {
                                    th = th4;
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th5) {
                                th = th5;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th6) {
                            th = th6;
                            str2 = smsc;
                            bArr = pdu;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        str = format;
                        str2 = smsc;
                        bArr = pdu;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    int i2 = token;
                    i = messageRef;
                    str = format;
                    str2 = smsc;
                    bArr = pdu;
                    _data.recycle();
                    throw th;
                }
            }

            public void acknowledgeSms(int token, int messageRef, int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeInt(messageRef);
                    _data.writeInt(result);
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().acknowledgeSms(token, messageRef, result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void acknowledgeSmsReport(int token, int messageRef, int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeInt(messageRef);
                    _data.writeInt(result);
                    if (this.mRemote.transact(18, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().acknowledgeSmsReport(token, messageRef, result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public String getSmsFormat() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSmsFormat();
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

            public void onSmsReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(20, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSmsReady();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IImsMmTelFeature asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IImsMmTelFeature)) {
                return new Proxy(obj);
            }
            return (IImsMmTelFeature) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setListener";
                case 2:
                    return "getFeatureState";
                case 3:
                    return "createCallProfile";
                case 4:
                    return "createCallSession";
                case 5:
                    return "shouldProcessCall";
                case 6:
                    return "getUtInterface";
                case 7:
                    return "getEcbmInterface";
                case 8:
                    return "setUiTtyMode";
                case 9:
                    return "getMultiEndpointInterface";
                case 10:
                    return "queryCapabilityStatus";
                case 11:
                    return "addCapabilityCallback";
                case 12:
                    return "removeCapabilityCallback";
                case 13:
                    return "changeCapabilitiesConfiguration";
                case 14:
                    return "queryCapabilityConfiguration";
                case 15:
                    return "setSmsListener";
                case 16:
                    return "sendSms";
                case 17:
                    return "acknowledgeSms";
                case 18:
                    return "acknowledgeSmsReport";
                case 19:
                    return "getSmsFormat";
                case 20:
                    return "onSmsReady";
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
                IBinder iBinder = null;
                int _result;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        setListener(android.telephony.ims.aidl.IImsMmTelListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _result = getFeatureState();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        ImsCallProfile _result2 = createCallProfile(data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 4:
                        ImsCallProfile _arg0;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        IImsCallSession _result3 = createCallSession(_arg0);
                        reply.writeNoException();
                        if (_result3 != null) {
                            iBinder = _result3.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        int _result4 = shouldProcessCall(data.createStringArray());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        IImsUt _result5 = getUtInterface();
                        reply.writeNoException();
                        if (_result5 != null) {
                            iBinder = _result5.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        IImsEcbm _result6 = getEcbmInterface();
                        reply.writeNoException();
                        if (_result6 != null) {
                            iBinder = _result6.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return true;
                    case 8:
                        Message _arg1;
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (Message) Message.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        setUiTtyMode(_result, _arg1);
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        IImsMultiEndpoint _result7 = getMultiEndpointInterface();
                        reply.writeNoException();
                        if (_result7 != null) {
                            iBinder = _result7.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _result = queryCapabilityStatus();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        addCapabilityCallback(android.telephony.ims.aidl.IImsCapabilityCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        removeCapabilityCallback(android.telephony.ims.aidl.IImsCapabilityCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 13:
                        CapabilityChangeRequest _arg02;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (CapabilityChangeRequest) CapabilityChangeRequest.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        changeCapabilitiesConfiguration(_arg02, android.telephony.ims.aidl.IImsCapabilityCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        queryCapabilityConfiguration(data.readInt(), data.readInt(), android.telephony.ims.aidl.IImsCapabilityCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        setSmsListener(android.telephony.ims.aidl.IImsSmsListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        sendSms(data.readInt(), data.readInt(), data.readString(), data.readString(), data.readInt() != 0, data.createByteArray());
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        acknowledgeSms(data.readInt(), data.readInt(), data.readInt());
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        acknowledgeSmsReport(data.readInt(), data.readInt(), data.readInt());
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        String _result8 = getSmsFormat();
                        reply.writeNoException();
                        parcel2.writeString(_result8);
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        onSmsReady();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IImsMmTelFeature impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IImsMmTelFeature getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void acknowledgeSms(int i, int i2, int i3) throws RemoteException;

    void acknowledgeSmsReport(int i, int i2, int i3) throws RemoteException;

    void addCapabilityCallback(IImsCapabilityCallback iImsCapabilityCallback) throws RemoteException;

    void changeCapabilitiesConfiguration(CapabilityChangeRequest capabilityChangeRequest, IImsCapabilityCallback iImsCapabilityCallback) throws RemoteException;

    ImsCallProfile createCallProfile(int i, int i2) throws RemoteException;

    IImsCallSession createCallSession(ImsCallProfile imsCallProfile) throws RemoteException;

    IImsEcbm getEcbmInterface() throws RemoteException;

    int getFeatureState() throws RemoteException;

    IImsMultiEndpoint getMultiEndpointInterface() throws RemoteException;

    String getSmsFormat() throws RemoteException;

    IImsUt getUtInterface() throws RemoteException;

    void onSmsReady() throws RemoteException;

    void queryCapabilityConfiguration(int i, int i2, IImsCapabilityCallback iImsCapabilityCallback) throws RemoteException;

    int queryCapabilityStatus() throws RemoteException;

    void removeCapabilityCallback(IImsCapabilityCallback iImsCapabilityCallback) throws RemoteException;

    void sendSms(int i, int i2, String str, String str2, boolean z, byte[] bArr) throws RemoteException;

    void setListener(IImsMmTelListener iImsMmTelListener) throws RemoteException;

    void setSmsListener(IImsSmsListener iImsSmsListener) throws RemoteException;

    void setUiTtyMode(int i, Message message) throws RemoteException;

    int shouldProcessCall(String[] strArr) throws RemoteException;
}
