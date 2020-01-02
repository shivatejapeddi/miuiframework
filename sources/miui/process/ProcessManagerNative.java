package miui.process;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import java.util.ArrayList;
import java.util.List;
import miui.process.IForegroundInfoListener.Stub;

public abstract class ProcessManagerNative extends Binder implements IProcessManager {
    private static volatile IProcessManager pm = null;

    public ProcessManagerNative() {
        attachInterface(this, IProcessManager.descriptor);
    }

    public static IProcessManager asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IProcessManager in = (IProcessManager) obj.queryLocalInterface(IProcessManager.descriptor);
        if (in != null) {
            return in;
        }
        return new ProcessManagerProxy(obj);
    }

    public IBinder asBinder() {
        return this;
    }

    public static IProcessManager getDefault() {
        if (pm == null) {
            synchronized (ProcessManagerNative.class) {
                if (pm == null) {
                    pm = asInterface(ServiceManager.getService("ProcessManager"));
                }
            }
        }
        return pm;
    }

    /* Access modifiers changed, original: protected */
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        Parcel parcel = data;
        Parcel parcel2 = reply;
        IMiuiApplicationThread iMiuiApplicationThread = null;
        boolean isLocked = false;
        int userId = IProcessManager.descriptor;
        List<String> list;
        boolean success;
        switch (code) {
            case 2:
                parcel.enforceInterface(userId);
                isLocked = kill((ProcessConfig) ProcessConfig.CREATOR.createFromParcel(parcel));
                reply.writeNoException();
                parcel2.writeInt(isLocked);
                return true;
            case 3:
                parcel.enforceInterface(userId);
                String packageName = data.readString();
                userId = data.readInt();
                if (data.readInt() != 0) {
                    isLocked = true;
                }
                updateApplicationLockedState(packageName, userId, isLocked);
                reply.writeNoException();
                return true;
            case 4:
                parcel.enforceInterface(userId);
                list = getLockedApplication(data.readInt());
                reply.writeNoException();
                userId = list != null ? list.size() : -1;
                parcel2.writeInt(userId);
                for (int i = 0; i < userId; i++) {
                    parcel2.writeString((String) list.get(i));
                }
                return true;
            case 5:
                parcel.enforceInterface(userId);
                updateConfig((ProcessConfig) ProcessConfig.CREATOR.createFromParcel(parcel));
                reply.writeNoException();
                return true;
            case 6:
                parcel.enforceInterface(userId);
                int result = startProcesses(parcel.readArrayList(List.class.getClassLoader()), data.readInt(), data.readInt() == 1, data.readInt(), data.readInt());
                reply.writeNoException();
                parcel2.writeInt(result);
                return true;
            case 7:
                parcel.enforceInterface(userId);
                if (data.readInt() != 0) {
                    isLocked = true;
                }
                success = protectCurrentProcess(isLocked, data.readInt());
                reply.writeNoException();
                parcel2.writeInt(success);
                return true;
            case 8:
                parcel.enforceInterface(userId);
                updateCloudData((ProcessCloudData) ProcessCloudData.CREATOR.createFromParcel(parcel));
                reply.writeNoException();
                return true;
            case 9:
                parcel.enforceInterface(userId);
                success = isLockedApplication(data.readString(), data.readInt());
                reply.writeNoException();
                parcel2.writeInt(success);
                return true;
            case 10:
                parcel.enforceInterface(userId);
                registerForegroundInfoListener(Stub.asInterface(data.readStrongBinder()));
                return true;
            case 11:
                parcel.enforceInterface(userId);
                unregisterForegroundInfoListener(Stub.asInterface(data.readStrongBinder()));
                return true;
            case 12:
                parcel.enforceInterface(userId);
                ForegroundInfo foregroundInfo = getForegroundInfo();
                reply.writeNoException();
                foregroundInfo.writeToParcel(parcel2, 0);
                return true;
            case 13:
                parcel.enforceInterface(userId);
                IBinder b = data.readStrongBinder();
                userId = data.readInt();
                if (b != null) {
                    iMiuiApplicationThread = IMiuiApplicationThread.Stub.asInterface(b);
                }
                addMiuiApplicationThread(iMiuiApplicationThread, userId);
                reply.writeNoException();
                return true;
            case 14:
                IBinder asBinder;
                parcel.enforceInterface(userId);
                IMiuiApplicationThread applicationThread = getForegroundApplicationThread();
                reply.writeNoException();
                if (applicationThread != null) {
                    asBinder = applicationThread.asBinder();
                }
                parcel2.writeStrongBinder(asBinder);
                return true;
            case 15:
                parcel.enforceInterface(userId);
                List<String> targetPackages = new ArrayList();
                parcel.readStringList(targetPackages);
                list = new ArrayList();
                parcel.readStringList(list);
                registerActivityChangeListener(targetPackages, list, IActivityChangeListener.Stub.asInterface(data.readStrongBinder()));
                return true;
            case 16:
                parcel.enforceInterface(userId);
                unregisterActivityChangeListener(IActivityChangeListener.Stub.asInterface(data.readStrongBinder()));
                return true;
            case 17:
                parcel.enforceInterface(userId);
                List<RunningProcessInfo> list2 = getRunningProcessInfo(data.readInt(), data.readInt(), data.readString(), data.readString(), data.readInt());
                reply.writeNoException();
                parcel2.writeTypedList(list2);
                return true;
            case 18:
                parcel.enforceInterface(userId);
                boostCameraIfNeeded();
                return true;
            case 19:
                parcel.enforceInterface(userId);
                adjBoost(data.readString(), data.readInt(), data.readLong(), data.readInt());
                reply.writeNoException();
                return true;
            default:
                return super.onTransact(code, data, reply, flags);
        }
    }
}
