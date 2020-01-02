package android.os;

public interface IVibratorService extends IInterface {

    public static class Default implements IVibratorService {
        public boolean hasVibrator() throws RemoteException {
            return false;
        }

        public boolean hasAmplitudeControl() throws RemoteException {
            return false;
        }

        public void vibrate(int uid, String opPkg, VibrationEffect effect, int usageHint, String reason, IBinder token) throws RemoteException {
        }

        public void cancelVibrate(IBinder token) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IVibratorService {
        private static final String DESCRIPTOR = "android.os.IVibratorService";
        static final int TRANSACTION_cancelVibrate = 4;
        static final int TRANSACTION_hasAmplitudeControl = 2;
        static final int TRANSACTION_hasVibrator = 1;
        static final int TRANSACTION_vibrate = 3;

        private static class Proxy implements IVibratorService {
            public static IVibratorService sDefaultImpl;
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

            public boolean hasVibrator() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (this.mRemote.transact(1, _data, _reply, z) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            z = true;
                        }
                        boolean _result = z;
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    z = Stub.getDefaultImpl().hasVibrator();
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasAmplitudeControl() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasAmplitudeControl();
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

            public void vibrate(int uid, String opPkg, VibrationEffect effect, int usageHint, String reason, IBinder token) throws RemoteException {
                Throwable th;
                int i;
                String str;
                IBinder iBinder;
                String str2;
                VibrationEffect vibrationEffect = effect;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(uid);
                        try {
                            _data.writeString(opPkg);
                            if (vibrationEffect != null) {
                                _data.writeInt(1);
                                vibrationEffect.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            i = usageHint;
                            str = reason;
                            iBinder = token;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        str2 = opPkg;
                        i = usageHint;
                        str = reason;
                        iBinder = token;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(usageHint);
                        try {
                            _data.writeString(reason);
                        } catch (Throwable th4) {
                            th = th4;
                            iBinder = token;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeStrongBinder(token);
                            if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().vibrate(uid, opPkg, effect, usageHint, reason, token);
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
                        str = reason;
                        iBinder = token;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i2 = uid;
                    str2 = opPkg;
                    i = usageHint;
                    str = reason;
                    iBinder = token;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void cancelVibrate(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelVibrate(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IVibratorService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IVibratorService)) {
                return new Proxy(obj);
            }
            return (IVibratorService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "hasVibrator";
            }
            if (transactionCode == 2) {
                return "hasAmplitudeControl";
            }
            if (transactionCode == 3) {
                return "vibrate";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "cancelVibrate";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            boolean _result;
            if (i == 1) {
                parcel.enforceInterface(descriptor);
                _result = hasVibrator();
                reply.writeNoException();
                parcel2.writeInt(_result);
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(descriptor);
                _result = hasAmplitudeControl();
                reply.writeNoException();
                parcel2.writeInt(_result);
                return true;
            } else if (i == 3) {
                VibrationEffect _arg2;
                parcel.enforceInterface(descriptor);
                int _arg0 = data.readInt();
                String _arg1 = data.readString();
                if (data.readInt() != 0) {
                    _arg2 = (VibrationEffect) VibrationEffect.CREATOR.createFromParcel(parcel);
                } else {
                    _arg2 = null;
                }
                vibrate(_arg0, _arg1, _arg2, data.readInt(), data.readString(), data.readStrongBinder());
                reply.writeNoException();
                return true;
            } else if (i == 4) {
                parcel.enforceInterface(descriptor);
                cancelVibrate(data.readStrongBinder());
                reply.writeNoException();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                parcel2.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IVibratorService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IVibratorService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void cancelVibrate(IBinder iBinder) throws RemoteException;

    boolean hasAmplitudeControl() throws RemoteException;

    boolean hasVibrator() throws RemoteException;

    void vibrate(int i, String str, VibrationEffect vibrationEffect, int i2, String str2, IBinder iBinder) throws RemoteException;
}
