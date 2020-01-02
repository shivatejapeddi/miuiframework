package android.app.job;

import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.service.notification.ZenModeConfig;
import java.util.List;

public interface IJobScheduler extends IInterface {

    public static class Default implements IJobScheduler {
        public int schedule(JobInfo job) throws RemoteException {
            return 0;
        }

        public int enqueue(JobInfo job, JobWorkItem work) throws RemoteException {
            return 0;
        }

        public int scheduleAsPackage(JobInfo job, String packageName, int userId, String tag) throws RemoteException {
            return 0;
        }

        public void cancel(int jobId) throws RemoteException {
        }

        public void cancelAll() throws RemoteException {
        }

        public ParceledListSlice getAllPendingJobs() throws RemoteException {
            return null;
        }

        public JobInfo getPendingJob(int jobId) throws RemoteException {
            return null;
        }

        public List<JobInfo> getStartedJobs() throws RemoteException {
            return null;
        }

        public ParceledListSlice getAllJobSnapshots() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IJobScheduler {
        private static final String DESCRIPTOR = "android.app.job.IJobScheduler";
        static final int TRANSACTION_cancel = 4;
        static final int TRANSACTION_cancelAll = 5;
        static final int TRANSACTION_enqueue = 2;
        static final int TRANSACTION_getAllJobSnapshots = 9;
        static final int TRANSACTION_getAllPendingJobs = 6;
        static final int TRANSACTION_getPendingJob = 7;
        static final int TRANSACTION_getStartedJobs = 8;
        static final int TRANSACTION_schedule = 1;
        static final int TRANSACTION_scheduleAsPackage = 3;

        private static class Proxy implements IJobScheduler {
            public static IJobScheduler sDefaultImpl;
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

            public int schedule(JobInfo job) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (job != null) {
                        _data.writeInt(1);
                        job.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().schedule(job);
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

            public int enqueue(JobInfo job, JobWorkItem work) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (job != null) {
                        _data.writeInt(1);
                        job.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (work != null) {
                        _data.writeInt(1);
                        work.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().enqueue(job, work);
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

            public int scheduleAsPackage(JobInfo job, String packageName, int userId, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (job != null) {
                        _data.writeInt(1);
                        job.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeString(tag);
                    int i = this.mRemote;
                    if (!i.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().scheduleAsPackage(job, packageName, userId, tag);
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

            public void cancel(int jobId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(jobId);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancel(jobId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelAll() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelAll();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getAllPendingJobs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParceledListSlice parceledListSlice = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getAllPendingJobs();
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public JobInfo getPendingJob(int jobId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(jobId);
                    JobInfo jobInfo = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        jobInfo = Stub.getDefaultImpl();
                        if (jobInfo != 0) {
                            jobInfo = Stub.getDefaultImpl().getPendingJob(jobId);
                            return jobInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        jobInfo = (JobInfo) JobInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        jobInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return jobInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<JobInfo> getStartedJobs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<JobInfo> list = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getStartedJobs();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(JobInfo.CREATOR);
                    List<JobInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getAllJobSnapshots() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParceledListSlice parceledListSlice = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getAllJobSnapshots();
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IJobScheduler asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IJobScheduler)) {
                return new Proxy(obj);
            }
            return (IJobScheduler) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return ZenModeConfig.SCHEDULE_PATH;
                case 2:
                    return "enqueue";
                case 3:
                    return "scheduleAsPackage";
                case 4:
                    return "cancel";
                case 5:
                    return "cancelAll";
                case 6:
                    return "getAllPendingJobs";
                case 7:
                    return "getPendingJob";
                case 8:
                    return "getStartedJobs";
                case 9:
                    return "getAllJobSnapshots";
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
                JobInfo _arg0;
                ParceledListSlice _result;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (JobInfo) JobInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        int _result2 = schedule(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 2:
                        JobWorkItem _arg1;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (JobInfo) JobInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = (JobWorkItem) JobWorkItem.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        int _result3 = enqueue(_arg0, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (JobInfo) JobInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        int _result4 = scheduleAsPackage(_arg0, data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        cancel(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        cancelAll();
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result = getAllPendingJobs();
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        JobInfo _result5 = getPendingJob(data.readInt());
                        reply.writeNoException();
                        if (_result5 != null) {
                            reply.writeInt(1);
                            _result5.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        List<JobInfo> _result6 = getStartedJobs();
                        reply.writeNoException();
                        reply.writeTypedList(_result6);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _result = getAllJobSnapshots();
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IJobScheduler impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IJobScheduler getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void cancel(int i) throws RemoteException;

    void cancelAll() throws RemoteException;

    int enqueue(JobInfo jobInfo, JobWorkItem jobWorkItem) throws RemoteException;

    ParceledListSlice getAllJobSnapshots() throws RemoteException;

    ParceledListSlice getAllPendingJobs() throws RemoteException;

    JobInfo getPendingJob(int i) throws RemoteException;

    List<JobInfo> getStartedJobs() throws RemoteException;

    int schedule(JobInfo jobInfo) throws RemoteException;

    int scheduleAsPackage(JobInfo jobInfo, String str, int i, String str2) throws RemoteException;
}
