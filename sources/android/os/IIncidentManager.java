package android.os;

import android.os.IncidentManager.IncidentReport;
import java.io.FileDescriptor;
import java.util.List;

public interface IIncidentManager extends IInterface {

    public static class Default implements IIncidentManager {
        public void reportIncident(IncidentReportArgs args) throws RemoteException {
        }

        public void reportIncidentToStream(IncidentReportArgs args, IIncidentReportStatusListener listener, FileDescriptor stream) throws RemoteException {
        }

        public void systemRunning() throws RemoteException {
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

    public static abstract class Stub extends Binder implements IIncidentManager {
        private static final String DESCRIPTOR = "android.os.IIncidentManager";
        static final int TRANSACTION_deleteAllIncidentReports = 7;
        static final int TRANSACTION_deleteIncidentReports = 6;
        static final int TRANSACTION_getIncidentReport = 5;
        static final int TRANSACTION_getIncidentReportList = 4;
        static final int TRANSACTION_reportIncident = 1;
        static final int TRANSACTION_reportIncidentToStream = 2;
        static final int TRANSACTION_systemRunning = 3;

        private static class Proxy implements IIncidentManager {
            public static IIncidentManager sDefaultImpl;
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

            public void reportIncident(IncidentReportArgs args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportIncident(args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportIncidentToStream(IncidentReportArgs args, IIncidentReportStatusListener listener, FileDescriptor stream) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeRawFileDescriptor(stream);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportIncidentToStream(args, listener, stream);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void systemRunning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().systemRunning();
                    }
                } finally {
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
                    List<String> list = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
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
                    IncidentReport incidentReport = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
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
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

        public static IIncidentManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IIncidentManager)) {
                return new Proxy(obj);
            }
            return (IIncidentManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "reportIncident";
                case 2:
                    return "reportIncidentToStream";
                case 3:
                    return "systemRunning";
                case 4:
                    return "getIncidentReportList";
                case 5:
                    return "getIncidentReport";
                case 6:
                    return "deleteIncidentReports";
                case 7:
                    return "deleteAllIncidentReports";
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
                IncidentReportArgs _arg0;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (IncidentReportArgs) IncidentReportArgs.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        reportIncident(_arg0);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (IncidentReportArgs) IncidentReportArgs.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        reportIncidentToStream(_arg0, android.os.IIncidentReportStatusListener.Stub.asInterface(data.readStrongBinder()), data.readRawFileDescriptor());
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        systemRunning();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        List<String> _result = getIncidentReportList(data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeStringList(_result);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        IncidentReport _result2 = getIncidentReport(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        deleteIncidentReports(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        deleteAllIncidentReports(data.readString());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IIncidentManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IIncidentManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void deleteAllIncidentReports(String str) throws RemoteException;

    void deleteIncidentReports(String str, String str2, String str3) throws RemoteException;

    IncidentReport getIncidentReport(String str, String str2, String str3) throws RemoteException;

    List<String> getIncidentReportList(String str, String str2) throws RemoteException;

    void reportIncident(IncidentReportArgs incidentReportArgs) throws RemoteException;

    void reportIncidentToStream(IncidentReportArgs incidentReportArgs, IIncidentReportStatusListener iIncidentReportStatusListener, FileDescriptor fileDescriptor) throws RemoteException;

    void systemRunning() throws RemoteException;
}
