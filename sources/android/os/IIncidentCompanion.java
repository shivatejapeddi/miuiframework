package android.os;

import android.os.IncidentManager.IncidentReport;
import java.util.List;

public interface IIncidentCompanion extends IInterface {

    public static class Default implements IIncidentCompanion {
        public void authorizeReport(int callingUid, String callingPackage, String receiverClass, String reportId, int flags, IIncidentAuthListener callback) throws RemoteException {
        }

        public void cancelAuthorization(IIncidentAuthListener callback) throws RemoteException {
        }

        public void sendReportReadyBroadcast(String pkg, String cls) throws RemoteException {
        }

        public List<String> getPendingReports() throws RemoteException {
            return null;
        }

        public void approveReport(String uri) throws RemoteException {
        }

        public void denyReport(String uri) throws RemoteException {
        }

        public List<String> getIncidentReportList(String pkg, String cls) throws RemoteException {
            return null;
        }

        public IncidentReport getIncidentReport(String pkg, String cls, String id) throws RemoteException {
            return null;
        }

        public void deleteIncidentReports(String pkg, String cls, String id) throws RemoteException {
        }

        public void deleteAllIncidentReports(String pkg) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IIncidentCompanion {
        private static final String DESCRIPTOR = "android.os.IIncidentCompanion";
        static final int TRANSACTION_approveReport = 5;
        static final int TRANSACTION_authorizeReport = 1;
        static final int TRANSACTION_cancelAuthorization = 2;
        static final int TRANSACTION_deleteAllIncidentReports = 10;
        static final int TRANSACTION_deleteIncidentReports = 9;
        static final int TRANSACTION_denyReport = 6;
        static final int TRANSACTION_getIncidentReport = 8;
        static final int TRANSACTION_getIncidentReportList = 7;
        static final int TRANSACTION_getPendingReports = 4;
        static final int TRANSACTION_sendReportReadyBroadcast = 3;

        private static class Proxy implements IIncidentCompanion {
            public static IIncidentCompanion sDefaultImpl;
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

            public void authorizeReport(int callingUid, String callingPackage, String receiverClass, String reportId, int flags, IIncidentAuthListener callback) throws RemoteException {
                Throwable th;
                String str;
                String str2;
                String str3;
                int i;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(callingUid);
                    } catch (Throwable th2) {
                        th = th2;
                        str = callingPackage;
                        str2 = receiverClass;
                        str3 = reportId;
                        i = flags;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPackage);
                        try {
                            _data.writeString(receiverClass);
                            try {
                                _data.writeString(reportId);
                                try {
                                    _data.writeInt(flags);
                                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                                } catch (Throwable th3) {
                                    th = th3;
                                    _data.recycle();
                                    throw th;
                                }
                                try {
                                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                        _data.recycle();
                                        return;
                                    }
                                    Stub.getDefaultImpl().authorizeReport(callingUid, callingPackage, receiverClass, reportId, flags, callback);
                                    _data.recycle();
                                } catch (Throwable th4) {
                                    th = th4;
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th5) {
                                th = th5;
                                i = flags;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th6) {
                            th = th6;
                            str3 = reportId;
                            i = flags;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        str2 = receiverClass;
                        str3 = reportId;
                        i = flags;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    int i2 = callingUid;
                    str = callingPackage;
                    str2 = receiverClass;
                    str3 = reportId;
                    i = flags;
                    _data.recycle();
                    throw th;
                }
            }

            public void cancelAuthorization(IIncidentAuthListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().cancelAuthorization(callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void sendReportReadyBroadcast(String pkg, String cls) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(cls);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().sendReportReadyBroadcast(pkg, cls);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public List<String> getPendingReports() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<String> list = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getPendingReports();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void approveReport(String uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uri);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().approveReport(uri);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void denyReport(String uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uri);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().denyReport(uri);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getIncidentReportList(String pkg, String cls) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(cls);
                    List<String> list = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getIncidentReportList(pkg, cls);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IncidentReport getIncidentReport(String pkg, String cls, String id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(cls);
                    _data.writeString(id);
                    IncidentReport incidentReport = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        incidentReport = Stub.getDefaultImpl();
                        if (incidentReport != 0) {
                            incidentReport = Stub.getDefaultImpl().getIncidentReport(pkg, cls, id);
                            return incidentReport;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        incidentReport = (IncidentReport) IncidentReport.CREATOR.createFromParcel(_reply);
                    } else {
                        incidentReport = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return incidentReport;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteIncidentReports(String pkg, String cls, String id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(cls);
                    _data.writeString(id);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteIncidentReports(pkg, cls, id);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteAllIncidentReports(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteAllIncidentReports(pkg);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IIncidentCompanion asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IIncidentCompanion)) {
                return new Proxy(obj);
            }
            return (IIncidentCompanion) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "authorizeReport";
                case 2:
                    return "cancelAuthorization";
                case 3:
                    return "sendReportReadyBroadcast";
                case 4:
                    return "getPendingReports";
                case 5:
                    return "approveReport";
                case 6:
                    return "denyReport";
                case 7:
                    return "getIncidentReportList";
                case 8:
                    return "getIncidentReport";
                case 9:
                    return "deleteIncidentReports";
                case 10:
                    return "deleteAllIncidentReports";
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
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        authorizeReport(data.readInt(), data.readString(), data.readString(), data.readString(), data.readInt(), android.os.IIncidentAuthListener.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        cancelAuthorization(android.os.IIncidentAuthListener.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        sendReportReadyBroadcast(data.readString(), data.readString());
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        List<String> _result = getPendingReports();
                        reply.writeNoException();
                        parcel2.writeStringList(_result);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        approveReport(data.readString());
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        denyReport(data.readString());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        List<String> _result2 = getIncidentReportList(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeStringList(_result2);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        IncidentReport _result3 = getIncidentReport(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        deleteIncidentReports(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        deleteAllIncidentReports(data.readString());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IIncidentCompanion impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IIncidentCompanion getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void approveReport(String str) throws RemoteException;

    void authorizeReport(int i, String str, String str2, String str3, int i2, IIncidentAuthListener iIncidentAuthListener) throws RemoteException;

    void cancelAuthorization(IIncidentAuthListener iIncidentAuthListener) throws RemoteException;

    void deleteAllIncidentReports(String str) throws RemoteException;

    void deleteIncidentReports(String str, String str2, String str3) throws RemoteException;

    void denyReport(String str) throws RemoteException;

    IncidentReport getIncidentReport(String str, String str2, String str3) throws RemoteException;

    List<String> getIncidentReportList(String str, String str2) throws RemoteException;

    List<String> getPendingReports() throws RemoteException;

    void sendReportReadyBroadcast(String str, String str2) throws RemoteException;
}
