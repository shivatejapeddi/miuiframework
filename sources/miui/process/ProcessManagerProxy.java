package miui.process;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;
import miui.process.IMiuiApplicationThread.Stub;

/* compiled from: ProcessManagerNative */
class ProcessManagerProxy implements IProcessManager {
    private IBinder mRemote;

    public ProcessManagerProxy(IBinder remote) {
        this.mRemote = remote;
    }

    public IBinder asBinder() {
        return this.mRemote;
    }

    public boolean kill(ProcessConfig config) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        boolean success = false;
        config.writeToParcel(data, 0);
        this.mRemote.transact(2, data, reply, 0);
        reply.readException();
        if (reply.readInt() != 0) {
            success = true;
        }
        data.recycle();
        reply.recycle();
        return success;
    }

    public void updateApplicationLockedState(String packageName, int userId, boolean isLocked) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        data.writeString(packageName);
        data.writeInt(userId);
        data.writeInt(isLocked);
        this.mRemote.transact(3, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public List<String> getLockedApplication(int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        data.writeInt(userId);
        this.mRemote.transact(4, data, reply, 0);
        reply.readException();
        ArrayList<String> list = null;
        int N = reply.readInt();
        if (N >= 0) {
            list = new ArrayList();
            while (N > 0) {
                list.add(reply.readString());
                N--;
            }
        }
        data.recycle();
        reply.recycle();
        return list;
    }

    public void updateConfig(ProcessConfig config) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        config.writeToParcel(data, 0);
        this.mRemote.transact(5, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public int startProcesses(List<PreloadProcessData> dataList, int startProcessCount, boolean ignoreMemory, int userId, int flag) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        data.writeList(dataList);
        data.writeInt(startProcessCount);
        data.writeInt(ignoreMemory);
        data.writeInt(userId);
        data.writeInt(flag);
        this.mRemote.transact(6, data, reply, 0);
        reply.readException();
        int result = reply.readInt();
        data.recycle();
        reply.recycle();
        return result;
    }

    public boolean protectCurrentProcess(boolean isProtected, int timeout) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        data.writeInt(isProtected);
        data.writeInt(timeout);
        boolean z = false;
        this.mRemote.transact(7, data, reply, 0);
        reply.readException();
        if (reply.readInt() != 0) {
            z = true;
        }
        boolean success = z;
        data.recycle();
        reply.recycle();
        return success;
    }

    public void updateCloudData(ProcessCloudData cloudData) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        cloudData.writeToParcel(data, 0);
        this.mRemote.transact(8, data, reply, 1);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean isLockedApplication(String packageName, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        data.writeString(packageName);
        data.writeInt(userId);
        boolean z = false;
        this.mRemote.transact(9, data, reply, 0);
        reply.readException();
        if (reply.readInt() != 0) {
            z = true;
        }
        boolean isLocked = z;
        data.recycle();
        reply.recycle();
        return isLocked;
    }

    public void registerForegroundInfoListener(IForegroundInfoListener listener) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        data.writeStrongBinder(listener != null ? listener.asBinder() : null);
        this.mRemote.transact(10, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void unregisterForegroundInfoListener(IForegroundInfoListener listener) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        data.writeStrongBinder(listener != null ? listener.asBinder() : null);
        this.mRemote.transact(11, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public ForegroundInfo getForegroundInfo() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        this.mRemote.transact(12, data, reply, 0);
        reply.readException();
        ForegroundInfo foregroundInfo = (ForegroundInfo) ForegroundInfo.CREATOR.createFromParcel(reply);
        data.recycle();
        reply.recycle();
        return foregroundInfo;
    }

    public void addMiuiApplicationThread(IMiuiApplicationThread applicationThread, int pid) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        data.writeStrongBinder(applicationThread != null ? applicationThread.asBinder() : null);
        data.writeInt(pid);
        this.mRemote.transact(13, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public IMiuiApplicationThread getForegroundApplicationThread() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        this.mRemote.transact(14, data, reply, 0);
        reply.readException();
        IBinder b = reply.readStrongBinder();
        IMiuiApplicationThread applicationThread = b != null ? Stub.asInterface(b) : null;
        data.recycle();
        reply.recycle();
        return applicationThread;
    }

    public void registerActivityChangeListener(List<String> targetPackages, List<String> targetActivities, IActivityChangeListener listener) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        data.writeStringList(targetPackages);
        data.writeStringList(targetActivities);
        data.writeStrongBinder(listener != null ? listener.asBinder() : null);
        this.mRemote.transact(15, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void unregisterActivityChangeListener(IActivityChangeListener listener) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        data.writeStrongBinder(listener != null ? listener.asBinder() : null);
        this.mRemote.transact(16, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public List<RunningProcessInfo> getRunningProcessInfo(int pid, int uid, String packageName, String processName, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        data.writeInt(pid);
        data.writeInt(uid);
        data.writeString(packageName);
        data.writeString(processName);
        data.writeInt(userId);
        this.mRemote.transact(17, data, reply, 0);
        reply.readException();
        ArrayList<RunningProcessInfo> list = reply.createTypedArrayList(RunningProcessInfo.CREATOR);
        data.recycle();
        reply.recycle();
        return list;
    }

    public void boostCameraIfNeeded() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        this.mRemote.transact(18, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void adjBoost(String processName, int targetAdj, long timeout, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IProcessManager.descriptor);
        data.writeString(processName);
        data.writeInt(targetAdj);
        data.writeLong(timeout);
        data.writeInt(userId);
        this.mRemote.transact(19, data, reply, 0);
        reply.readException();
        reply.recycle();
        data.recycle();
    }
}
