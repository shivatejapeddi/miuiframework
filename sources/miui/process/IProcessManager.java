package miui.process;

import android.os.IInterface;
import android.os.RemoteException;
import java.util.List;

public interface IProcessManager extends IInterface {
    public static final int ADD_MIUI_APPLICATION_THREAD_TRANSACTION = 13;
    public static final int BOOST_CAMERA_TRANSACTION = 18;
    public static final int GET_FOREGROUND_APPLICATION_TRANSACTION = 14;
    public static final int GET_FOREGROUND_INFO_TRANSACTION = 12;
    public static final int GET_LOCKED_APPLICATION = 4;
    public static final int GET_RUNNING_PROCESS_INFO = 17;
    public static final int IS_LOCKED_APPLICATION_TRANSACTION = 9;
    public static final int KILL_TRANSACTION = 2;
    public static final int PROCESS_ADJ_BOOST_TRANSACTION = 19;
    public static final int PROTECT_CURRENT_PROCESS_TRANSACTION = 7;
    public static final int REGISTER_ACTIVITY_CHANGE_TRANSACTION = 15;
    public static final int REGISTER_FOREGROUND_INFO_LISTENER = 10;
    public static final int START_PROCESSES_TRANSACTION = 6;
    public static final int UNREGISTER_ACTIVITY_CHANGE_TRANSACTION = 16;
    public static final int UNREGISTER_FOREGROUND_INFO_LISTENER = 11;
    public static final int UPDATE_APPLICATION_LOCKED_STATE = 3;
    public static final int UPDATE_CLOUD_DATA_TRANSACTION = 8;
    public static final int UPDATE_CONFIG_TRANSACTION = 5;
    public static final String descriptor = "miui.IProcessManager";

    void addMiuiApplicationThread(IMiuiApplicationThread iMiuiApplicationThread, int i) throws RemoteException;

    void adjBoost(String str, int i, long j, int i2) throws RemoteException;

    void boostCameraIfNeeded() throws RemoteException;

    IMiuiApplicationThread getForegroundApplicationThread() throws RemoteException;

    ForegroundInfo getForegroundInfo() throws RemoteException;

    List<String> getLockedApplication(int i) throws RemoteException;

    List<RunningProcessInfo> getRunningProcessInfo(int i, int i2, String str, String str2, int i3) throws RemoteException;

    boolean isLockedApplication(String str, int i) throws RemoteException;

    boolean kill(ProcessConfig processConfig) throws RemoteException;

    boolean protectCurrentProcess(boolean z, int i) throws RemoteException;

    void registerActivityChangeListener(List<String> list, List<String> list2, IActivityChangeListener iActivityChangeListener) throws RemoteException;

    void registerForegroundInfoListener(IForegroundInfoListener iForegroundInfoListener) throws RemoteException;

    int startProcesses(List<PreloadProcessData> list, int i, boolean z, int i2, int i3) throws RemoteException;

    void unregisterActivityChangeListener(IActivityChangeListener iActivityChangeListener) throws RemoteException;

    void unregisterForegroundInfoListener(IForegroundInfoListener iForegroundInfoListener) throws RemoteException;

    void updateApplicationLockedState(String str, int i, boolean z) throws RemoteException;

    void updateCloudData(ProcessCloudData processCloudData) throws RemoteException;

    void updateConfig(ProcessConfig processConfig) throws RemoteException;
}
