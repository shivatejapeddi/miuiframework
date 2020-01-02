package com.android.internal.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.statistics.E2EScenario;
import android.os.statistics.E2EScenarioPayload;
import android.os.statistics.E2EScenarioSettings;
import java.util.List;
import java.util.Map;

public interface IPerfShielder extends IInterface {

    public static class Default implements IPerfShielder {
        public void reportPerceptibleJank(int callingPid, int renderThreadTid, String windowName, long totalDuration, long maxFrameDuration, long endTs, int selfCause, long num_frames) throws RemoteException {
        }

        public void addActivityLaunchTime(String packageName, String activityName, long launchStartTime, long launchEndTime, boolean fromHome, boolean isColdStart) throws RemoteException {
        }

        public void setSchedFgPid(int pid) throws RemoteException {
        }

        public void killUnusedApp(int uid, int pid) throws RemoteException {
        }

        public String getPackageNameByPid(int pid) throws RemoteException {
            return null;
        }

        public void setForkedProcessGroup(int puid, int ppid, int group, String processName) throws RemoteException {
        }

        public List<Bundle> getAllRunningProcessMemInfos() throws RemoteException {
            return null;
        }

        public List<Bundle> updateProcessFullMemInfoByPids(int[] pids) throws RemoteException {
            return null;
        }

        public List<Bundle> updateProcessPartialMemInfoByPids(int[] pids) throws RemoteException {
            return null;
        }

        public void setServicePriority(List<MiuiServicePriority> list) throws RemoteException {
        }

        public void setServicePriorityWithNoProc(List<MiuiServicePriority> list, long noProcDelayTime) throws RemoteException {
        }

        public void removeServicePriority(MiuiServicePriority servicePriority, boolean inBlacklist) throws RemoteException {
        }

        public void closeCheckPriority() throws RemoteException {
        }

        public void setMiuiContentProviderControl(boolean enable) throws RemoteException {
        }

        public void setMiuiBroadcastDispatchEnable(boolean enable) throws RemoteException {
        }

        public void addTimeConsumingIntent(String[] actions) throws RemoteException {
        }

        public void removeTimeConsumingIntent(String[] actions) throws RemoteException {
        }

        public void clearTimeConsumingIntent() throws RemoteException {
        }

        public int getMemoryTrimLevel() throws RemoteException {
            return 0;
        }

        public boolean insertRedirectRule(String callingPkg, String destPkg, String redirectPkgname, Bundle clsNameMap) throws RemoteException {
            return false;
        }

        public boolean deleteRedirectRule(String callingPkg, String destPkg) throws RemoteException {
            return false;
        }

        public boolean insertPackageInfo(PackageInfo pInfo) throws RemoteException {
            return false;
        }

        public boolean deletePackageInfo(String pkgName) throws RemoteException {
            return false;
        }

        public long getFreeMemory() throws RemoteException {
            return 0;
        }

        public void reportAnr(int callingPid, String windowName, long totalDuration, long endTs, String cpuInfo) throws RemoteException {
        }

        public boolean addCallingPkgHookRule(String hostApp, String originCallingPkg, String hookCallingPkg) throws RemoteException {
            return false;
        }

        public boolean removeCallingPkgHookRule(String hostApp, String originCallingPkg) throws RemoteException {
            return false;
        }

        public ParcelFileDescriptor getPerfEventSocketFd() throws RemoteException {
            return null;
        }

        public Bundle beginScenario(E2EScenario scenario, E2EScenarioSettings settings, String tag, E2EScenarioPayload payload, int tid, long uptimeMillis, boolean needResultBundle) throws RemoteException {
            return null;
        }

        public void abortMatchingScenario(E2EScenario scenario, String tag, int tid, long uptimeMillis) throws RemoteException {
        }

        public void abortSpecificScenario(Bundle scenarioBundle, int tid, long uptimeMillis) throws RemoteException {
        }

        public void finishMatchingScenario(E2EScenario scenario, String tag, E2EScenarioPayload payload, int tid, long uptimeMillis) throws RemoteException {
        }

        public void finishSpecificScenario(Bundle scenarioBundle, E2EScenarioPayload payload, int tid, long uptimeMillis) throws RemoteException {
        }

        public void reportExcessiveCpuUsageRecords(List<Bundle> list) throws RemoteException {
        }

        public void reportNotificationClick(String postPackage, Intent intent, long uptimeMillis) throws RemoteException {
        }

        public boolean insertFilterInfo(String packageName, String name, Uri iconUri, List<Bundle> list) throws RemoteException {
            return false;
        }

        public boolean deleteFilterInfo(String packageName) throws RemoteException {
            return false;
        }

        public List<QuickAppResolveInfo> resolveQuickAppInfos(Intent targetIntent) throws RemoteException {
            return null;
        }

        public void setHapLinks(Map data, ActivityInfo activityInfo) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPerfShielder {
        private static final String DESCRIPTOR = "com.android.internal.app.IPerfShielder";
        static final int TRANSACTION_abortMatchingScenario = 30;
        static final int TRANSACTION_abortSpecificScenario = 31;
        static final int TRANSACTION_addActivityLaunchTime = 2;
        static final int TRANSACTION_addCallingPkgHookRule = 26;
        static final int TRANSACTION_addTimeConsumingIntent = 16;
        static final int TRANSACTION_beginScenario = 29;
        static final int TRANSACTION_clearTimeConsumingIntent = 18;
        static final int TRANSACTION_closeCheckPriority = 13;
        static final int TRANSACTION_deleteFilterInfo = 37;
        static final int TRANSACTION_deletePackageInfo = 23;
        static final int TRANSACTION_deleteRedirectRule = 21;
        static final int TRANSACTION_finishMatchingScenario = 32;
        static final int TRANSACTION_finishSpecificScenario = 33;
        static final int TRANSACTION_getAllRunningProcessMemInfos = 7;
        static final int TRANSACTION_getFreeMemory = 24;
        static final int TRANSACTION_getMemoryTrimLevel = 19;
        static final int TRANSACTION_getPackageNameByPid = 5;
        static final int TRANSACTION_getPerfEventSocketFd = 28;
        static final int TRANSACTION_insertFilterInfo = 36;
        static final int TRANSACTION_insertPackageInfo = 22;
        static final int TRANSACTION_insertRedirectRule = 20;
        static final int TRANSACTION_killUnusedApp = 4;
        static final int TRANSACTION_removeCallingPkgHookRule = 27;
        static final int TRANSACTION_removeServicePriority = 12;
        static final int TRANSACTION_removeTimeConsumingIntent = 17;
        static final int TRANSACTION_reportAnr = 25;
        static final int TRANSACTION_reportExcessiveCpuUsageRecords = 34;
        static final int TRANSACTION_reportNotificationClick = 35;
        static final int TRANSACTION_reportPerceptibleJank = 1;
        static final int TRANSACTION_resolveQuickAppInfos = 38;
        static final int TRANSACTION_setForkedProcessGroup = 6;
        static final int TRANSACTION_setHapLinks = 39;
        static final int TRANSACTION_setMiuiBroadcastDispatchEnable = 15;
        static final int TRANSACTION_setMiuiContentProviderControl = 14;
        static final int TRANSACTION_setSchedFgPid = 3;
        static final int TRANSACTION_setServicePriority = 10;
        static final int TRANSACTION_setServicePriorityWithNoProc = 11;
        static final int TRANSACTION_updateProcessFullMemInfoByPids = 8;
        static final int TRANSACTION_updateProcessPartialMemInfoByPids = 9;

        private static class Proxy implements IPerfShielder {
            public static IPerfShielder sDefaultImpl;
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

            public void reportPerceptibleJank(int callingPid, int renderThreadTid, String windowName, long totalDuration, long maxFrameDuration, long endTs, int selfCause, long num_frames) throws RemoteException {
                Throwable th;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(callingPid);
                        _data.writeInt(renderThreadTid);
                        _data.writeString(windowName);
                        _data.writeLong(totalDuration);
                        _data.writeLong(maxFrameDuration);
                        _data.writeLong(endTs);
                        _data.writeInt(selfCause);
                        _data.writeLong(num_frames);
                        if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().reportPerceptibleJank(callingPid, renderThreadTid, windowName, totalDuration, maxFrameDuration, endTs, selfCause, num_frames);
                        _data.recycle();
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    int i = callingPid;
                    _data.recycle();
                    throw th;
                }
            }

            public void addActivityLaunchTime(String packageName, String activityName, long launchStartTime, long launchEndTime, boolean fromHome, boolean isColdStart) throws RemoteException {
                Throwable th;
                String str;
                long j;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th2) {
                        th = th2;
                        str = activityName;
                        j = launchStartTime;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(activityName);
                        try {
                            _data.writeLong(launchStartTime);
                            _data.writeLong(launchEndTime);
                            int i = 0;
                            _data.writeInt(fromHome ? 1 : 0);
                            if (isColdStart) {
                                i = 1;
                            }
                            _data.writeInt(i);
                            try {
                                if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                    _data.recycle();
                                    return;
                                }
                                Stub.getDefaultImpl().addActivityLaunchTime(packageName, activityName, launchStartTime, launchEndTime, fromHome, isColdStart);
                                _data.recycle();
                            } catch (Throwable th3) {
                                th = th3;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        j = launchStartTime;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str2 = packageName;
                    str = activityName;
                    j = launchStartTime;
                    _data.recycle();
                    throw th;
                }
            }

            public void setSchedFgPid(int pid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setSchedFgPid(pid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void killUnusedApp(int uid, int pid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().killUnusedApp(uid, pid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public String getPackageNameByPid(int pid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    String str = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getPackageNameByPid(pid);
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

            public void setForkedProcessGroup(int puid, int ppid, int group, String processName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(puid);
                    _data.writeInt(ppid);
                    _data.writeInt(group);
                    _data.writeString(processName);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setForkedProcessGroup(puid, ppid, group, processName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<Bundle> getAllRunningProcessMemInfos() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<Bundle> list = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAllRunningProcessMemInfos();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(Bundle.CREATOR);
                    List<Bundle> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<Bundle> updateProcessFullMemInfoByPids(int[] pids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    List<Bundle> list = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().updateProcessFullMemInfoByPids(pids);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(Bundle.CREATOR);
                    List<Bundle> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<Bundle> updateProcessPartialMemInfoByPids(int[] pids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    List<Bundle> list = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().updateProcessPartialMemInfoByPids(pids);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(Bundle.CREATOR);
                    List<Bundle> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setServicePriority(List<MiuiServicePriority> servicePrioritys) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(servicePrioritys);
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setServicePriority(servicePrioritys);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setServicePriorityWithNoProc(List<MiuiServicePriority> servicePrioritys, long noProcDelayTime) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(servicePrioritys);
                    _data.writeLong(noProcDelayTime);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setServicePriorityWithNoProc(servicePrioritys, noProcDelayTime);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void removeServicePriority(MiuiServicePriority servicePriority, boolean inBlacklist) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (servicePriority != null) {
                        _data.writeInt(1);
                        servicePriority.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (inBlacklist) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().removeServicePriority(servicePriority, inBlacklist);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void closeCheckPriority() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().closeCheckPriority();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMiuiContentProviderControl(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setMiuiContentProviderControl(enable);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setMiuiBroadcastDispatchEnable(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setMiuiBroadcastDispatchEnable(enable);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void addTimeConsumingIntent(String[] actions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(actions);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addTimeConsumingIntent(actions);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeTimeConsumingIntent(String[] actions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(actions);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeTimeConsumingIntent(actions);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearTimeConsumingIntent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearTimeConsumingIntent();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMemoryTrimLevel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getMemoryTrimLevel();
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

            public boolean insertRedirectRule(String callingPkg, String destPkg, String redirectPkgname, Bundle clsNameMap) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(destPkg);
                    _data.writeString(redirectPkgname);
                    boolean _result = true;
                    if (clsNameMap != null) {
                        _data.writeInt(1);
                        clsNameMap.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().insertRedirectRule(callingPkg, destPkg, redirectPkgname, clsNameMap);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean deleteRedirectRule(String callingPkg, String destPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(destPkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().deleteRedirectRule(callingPkg, destPkg);
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

            public boolean insertPackageInfo(PackageInfo pInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (pInfo != null) {
                        _data.writeInt(1);
                        pInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().insertPackageInfo(pInfo);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean deletePackageInfo(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().deletePackageInfo(pkgName);
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

            public long getFreeMemory() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 24;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getFreeMemory();
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportAnr(int callingPid, String windowName, long totalDuration, long endTs, String cpuInfo) throws RemoteException {
                Throwable th;
                String str;
                long j;
                long j2;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(callingPid);
                    } catch (Throwable th2) {
                        th = th2;
                        str = windowName;
                        j = totalDuration;
                        j2 = endTs;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(windowName);
                        try {
                            _data.writeLong(totalDuration);
                        } catch (Throwable th3) {
                            th = th3;
                            j2 = endTs;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeLong(endTs);
                            _data.writeString(cpuInfo);
                            if (this.mRemote.transact(25, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().reportAnr(callingPid, windowName, totalDuration, endTs, cpuInfo);
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        j = totalDuration;
                        j2 = endTs;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i = callingPid;
                    str = windowName;
                    j = totalDuration;
                    j2 = endTs;
                    _data.recycle();
                    throw th;
                }
            }

            public boolean addCallingPkgHookRule(String hostApp, String originCallingPkg, String hookCallingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(hostApp);
                    _data.writeString(originCallingPkg);
                    _data.writeString(hookCallingPkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().addCallingPkgHookRule(hostApp, originCallingPkg, hookCallingPkg);
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

            public boolean removeCallingPkgHookRule(String hostApp, String originCallingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(hostApp);
                    _data.writeString(originCallingPkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().removeCallingPkgHookRule(hostApp, originCallingPkg);
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

            public ParcelFileDescriptor getPerfEventSocketFd() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParcelFileDescriptor parcelFileDescriptor = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != 0) {
                            parcelFileDescriptor = Stub.getDefaultImpl().getPerfEventSocketFd();
                            return parcelFileDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        parcelFileDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parcelFileDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle beginScenario(E2EScenario scenario, E2EScenarioSettings settings, String tag, E2EScenarioPayload payload, int tid, long uptimeMillis, boolean needResultBundle) throws RemoteException {
                Throwable th;
                E2EScenario e2EScenario = scenario;
                E2EScenarioSettings e2EScenarioSettings = settings;
                E2EScenarioPayload e2EScenarioPayload = payload;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (e2EScenario != null) {
                        _data.writeInt(1);
                        e2EScenario.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (e2EScenarioSettings != null) {
                        _data.writeInt(1);
                        e2EScenarioSettings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(tag);
                        if (e2EScenarioPayload != null) {
                            _data.writeInt(1);
                            e2EScenarioPayload.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeInt(tid);
                        _data.writeLong(uptimeMillis);
                        if (!needResultBundle) {
                            i = 0;
                        }
                        _data.writeInt(i);
                        Bundle _result;
                        if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() != 0) {
                                _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                            } else {
                                _result = null;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        _result = Stub.getDefaultImpl().beginScenario(scenario, settings, tag, payload, tid, uptimeMillis, needResultBundle);
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    String str = tag;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void abortMatchingScenario(E2EScenario scenario, String tag, int tid, long uptimeMillis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (scenario != null) {
                        _data.writeInt(1);
                        scenario.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(tag);
                    _data.writeInt(tid);
                    _data.writeLong(uptimeMillis);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().abortMatchingScenario(scenario, tag, tid, uptimeMillis);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void abortSpecificScenario(Bundle scenarioBundle, int tid, long uptimeMillis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (scenarioBundle != null) {
                        _data.writeInt(1);
                        scenarioBundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(tid);
                    _data.writeLong(uptimeMillis);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().abortSpecificScenario(scenarioBundle, tid, uptimeMillis);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void finishMatchingScenario(E2EScenario scenario, String tag, E2EScenarioPayload payload, int tid, long uptimeMillis) throws RemoteException {
                Throwable th;
                long j;
                int i;
                E2EScenario e2EScenario = scenario;
                E2EScenarioPayload e2EScenarioPayload = payload;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (e2EScenario != null) {
                        _data.writeInt(1);
                        e2EScenario.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(tag);
                        if (e2EScenarioPayload != null) {
                            _data.writeInt(1);
                            e2EScenarioPayload.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeInt(tid);
                            try {
                                _data.writeLong(uptimeMillis);
                                if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                    _reply.readException();
                                    _reply.recycle();
                                    _data.recycle();
                                    return;
                                }
                                Stub.getDefaultImpl().finishMatchingScenario(scenario, tag, payload, tid, uptimeMillis);
                                _reply.recycle();
                                _data.recycle();
                            } catch (Throwable th2) {
                                th = th2;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            j = uptimeMillis;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i = tid;
                        j = uptimeMillis;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    String str = tag;
                    i = tid;
                    j = uptimeMillis;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void finishSpecificScenario(Bundle scenarioBundle, E2EScenarioPayload payload, int tid, long uptimeMillis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (scenarioBundle != null) {
                        _data.writeInt(1);
                        scenarioBundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (payload != null) {
                        _data.writeInt(1);
                        payload.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(tid);
                    _data.writeLong(uptimeMillis);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finishSpecificScenario(scenarioBundle, payload, tid, uptimeMillis);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportExcessiveCpuUsageRecords(List<Bundle> records) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(records);
                    if (this.mRemote.transact(34, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportExcessiveCpuUsageRecords(records);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reportNotificationClick(String postPackage, Intent intent, long uptimeMillis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(postPackage);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(uptimeMillis);
                    if (this.mRemote.transact(35, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportNotificationClick(postPackage, intent, uptimeMillis);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public boolean insertFilterInfo(String packageName, String name, Uri iconUri, List<Bundle> filterInfos) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(name);
                    boolean _result = true;
                    if (iconUri != null) {
                        _data.writeInt(1);
                        iconUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedList(filterInfos);
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().insertFilterInfo(packageName, name, iconUri, filterInfos);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean deleteFilterInfo(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().deleteFilterInfo(packageName);
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

            public List<QuickAppResolveInfo> resolveQuickAppInfos(Intent targetIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (targetIntent != null) {
                        _data.writeInt(1);
                        targetIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    List<QuickAppResolveInfo> list = this.mRemote;
                    if (!list.transact(38, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().resolveQuickAppInfos(targetIntent);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(QuickAppResolveInfo.CREATOR);
                    List<QuickAppResolveInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setHapLinks(Map data, ActivityInfo activityInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeMap(data);
                    if (activityInfo != null) {
                        _data.writeInt(1);
                        activityInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(39, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setHapLinks(data, activityInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPerfShielder asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPerfShielder)) {
                return new Proxy(obj);
            }
            return (IPerfShielder) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "reportPerceptibleJank";
                case 2:
                    return "addActivityLaunchTime";
                case 3:
                    return "setSchedFgPid";
                case 4:
                    return "killUnusedApp";
                case 5:
                    return "getPackageNameByPid";
                case 6:
                    return "setForkedProcessGroup";
                case 7:
                    return "getAllRunningProcessMemInfos";
                case 8:
                    return "updateProcessFullMemInfoByPids";
                case 9:
                    return "updateProcessPartialMemInfoByPids";
                case 10:
                    return "setServicePriority";
                case 11:
                    return "setServicePriorityWithNoProc";
                case 12:
                    return "removeServicePriority";
                case 13:
                    return "closeCheckPriority";
                case 14:
                    return "setMiuiContentProviderControl";
                case 15:
                    return "setMiuiBroadcastDispatchEnable";
                case 16:
                    return "addTimeConsumingIntent";
                case 17:
                    return "removeTimeConsumingIntent";
                case 18:
                    return "clearTimeConsumingIntent";
                case 19:
                    return "getMemoryTrimLevel";
                case 20:
                    return "insertRedirectRule";
                case 21:
                    return "deleteRedirectRule";
                case 22:
                    return "insertPackageInfo";
                case 23:
                    return "deletePackageInfo";
                case 24:
                    return "getFreeMemory";
                case 25:
                    return "reportAnr";
                case 26:
                    return "addCallingPkgHookRule";
                case 27:
                    return "removeCallingPkgHookRule";
                case 28:
                    return "getPerfEventSocketFd";
                case 29:
                    return "beginScenario";
                case 30:
                    return "abortMatchingScenario";
                case 31:
                    return "abortSpecificScenario";
                case 32:
                    return "finishMatchingScenario";
                case 33:
                    return "finishSpecificScenario";
                case 34:
                    return "reportExcessiveCpuUsageRecords";
                case 35:
                    return "reportNotificationClick";
                case 36:
                    return "insertFilterInfo";
                case 37:
                    return "deleteFilterInfo";
                case 38:
                    return "resolveQuickAppInfos";
                case 39:
                    return "setHapLinks";
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
            boolean z;
            if (i != 1598968902) {
                boolean z2 = false;
                String _result;
                List<Bundle> _result2;
                String _arg0;
                boolean _result3;
                boolean _result4;
                boolean _result5;
                Bundle _result6;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        z = true;
                        Parcel parcel3 = parcel2;
                        reportPerceptibleJank(data.readInt(), data.readInt(), data.readString(), data.readLong(), data.readLong(), data.readLong(), data.readInt(), data.readLong());
                        return z;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        addActivityLaunchTime(data.readString(), data.readString(), data.readLong(), data.readLong(), data.readInt() != 0, data.readInt() != 0);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        setSchedFgPid(data.readInt());
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        killUnusedApp(data.readInt(), data.readInt());
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result = getPackageNameByPid(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        setForkedProcessGroup(data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        List<Bundle> _result7 = getAllRunningProcessMemInfos();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result7);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _result2 = updateProcessFullMemInfoByPids(data.createIntArray());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result2);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _result2 = updateProcessPartialMemInfoByPids(data.createIntArray());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result2);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        setServicePriority(parcel.createTypedArrayList(MiuiServicePriority.CREATOR));
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        setServicePriorityWithNoProc(parcel.createTypedArrayList(MiuiServicePriority.CREATOR), data.readLong());
                        return true;
                    case 12:
                        MiuiServicePriority _arg02;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (MiuiServicePriority) MiuiServicePriority.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        removeServicePriority(_arg02, z2);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        closeCheckPriority();
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        setMiuiContentProviderControl(z2);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        setMiuiBroadcastDispatchEnable(z2);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        addTimeConsumingIntent(data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        removeTimeConsumingIntent(data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        clearTimeConsumingIntent();
                        reply.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        int _result8 = getMemoryTrimLevel();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 20:
                        Bundle _arg3;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _result = data.readString();
                        String _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        _result3 = insertRedirectRule(_arg0, _result, _arg2, _arg3);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _result4 = deleteRedirectRule(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 22:
                        PackageInfo _arg03;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (PackageInfo) PackageInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _result5 = insertPackageInfo(_arg03);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _result5 = deletePackageInfo(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        long _result9 = getFreeMemory();
                        reply.writeNoException();
                        parcel2.writeLong(_result9);
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        reportAnr(data.readInt(), data.readString(), data.readLong(), data.readLong(), data.readString());
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        boolean _result10 = addCallingPkgHookRule(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        _result4 = removeCallingPkgHookRule(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        ParcelFileDescriptor _result11 = getPerfEventSocketFd();
                        reply.writeNoException();
                        if (_result11 != null) {
                            parcel2.writeInt(1);
                            _result11.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 29:
                        E2EScenario _arg04;
                        E2EScenarioSettings _arg1;
                        E2EScenarioPayload _arg32;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (E2EScenario) E2EScenario.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = (E2EScenarioSettings) E2EScenarioSettings.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        String _arg22 = data.readString();
                        if (data.readInt() != 0) {
                            _arg32 = (E2EScenarioPayload) E2EScenarioPayload.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        _result6 = beginScenario(_arg04, _arg1, _arg22, _arg32, data.readInt(), data.readLong(), data.readInt() != 0);
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 30:
                        E2EScenario _arg05;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (E2EScenario) E2EScenario.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        abortMatchingScenario(_arg05, data.readString(), data.readInt(), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result6 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _result6 = null;
                        }
                        abortSpecificScenario(_result6, data.readInt(), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 32:
                        E2EScenario _arg06;
                        E2EScenarioPayload _arg23;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (E2EScenario) E2EScenario.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        String _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (E2EScenarioPayload) E2EScenarioPayload.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        finishMatchingScenario(_arg06, _arg12, _arg23, data.readInt(), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 33:
                        Bundle _arg07;
                        E2EScenarioPayload _arg13;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg07 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg13 = (E2EScenarioPayload) E2EScenarioPayload.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        finishSpecificScenario(_arg07, _arg13, data.readInt(), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        reportExcessiveCpuUsageRecords(parcel.createTypedArrayList(Bundle.CREATOR));
                        return true;
                    case 35:
                        Intent _arg14;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg14 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        reportNotificationClick(_arg0, _arg14, data.readLong());
                        return true;
                    case 36:
                        Uri _arg24;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _result = data.readString();
                        if (data.readInt() != 0) {
                            _arg24 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        _result3 = insertFilterInfo(_arg0, _result, _arg24, parcel.createTypedArrayList(Bundle.CREATOR));
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        _result5 = deleteFilterInfo(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 38:
                        Intent _arg08;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg08 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg08 = null;
                        }
                        List<QuickAppResolveInfo> _result12 = resolveQuickAppInfos(_arg08);
                        reply.writeNoException();
                        parcel2.writeTypedList(_result12);
                        return true;
                    case 39:
                        ActivityInfo _arg15;
                        parcel.enforceInterface(descriptor);
                        Map _arg09 = parcel.readHashMap(getClass().getClassLoader());
                        if (data.readInt() != 0) {
                            _arg15 = (ActivityInfo) ActivityInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg15 = null;
                        }
                        setHapLinks(_arg09, _arg15);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            z = true;
            parcel2.writeString(descriptor);
            return z;
        }

        public static boolean setDefaultImpl(IPerfShielder impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPerfShielder getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void abortMatchingScenario(E2EScenario e2EScenario, String str, int i, long j) throws RemoteException;

    void abortSpecificScenario(Bundle bundle, int i, long j) throws RemoteException;

    void addActivityLaunchTime(String str, String str2, long j, long j2, boolean z, boolean z2) throws RemoteException;

    boolean addCallingPkgHookRule(String str, String str2, String str3) throws RemoteException;

    void addTimeConsumingIntent(String[] strArr) throws RemoteException;

    Bundle beginScenario(E2EScenario e2EScenario, E2EScenarioSettings e2EScenarioSettings, String str, E2EScenarioPayload e2EScenarioPayload, int i, long j, boolean z) throws RemoteException;

    void clearTimeConsumingIntent() throws RemoteException;

    void closeCheckPriority() throws RemoteException;

    boolean deleteFilterInfo(String str) throws RemoteException;

    boolean deletePackageInfo(String str) throws RemoteException;

    boolean deleteRedirectRule(String str, String str2) throws RemoteException;

    void finishMatchingScenario(E2EScenario e2EScenario, String str, E2EScenarioPayload e2EScenarioPayload, int i, long j) throws RemoteException;

    void finishSpecificScenario(Bundle bundle, E2EScenarioPayload e2EScenarioPayload, int i, long j) throws RemoteException;

    List<Bundle> getAllRunningProcessMemInfos() throws RemoteException;

    long getFreeMemory() throws RemoteException;

    int getMemoryTrimLevel() throws RemoteException;

    String getPackageNameByPid(int i) throws RemoteException;

    ParcelFileDescriptor getPerfEventSocketFd() throws RemoteException;

    boolean insertFilterInfo(String str, String str2, Uri uri, List<Bundle> list) throws RemoteException;

    boolean insertPackageInfo(PackageInfo packageInfo) throws RemoteException;

    boolean insertRedirectRule(String str, String str2, String str3, Bundle bundle) throws RemoteException;

    void killUnusedApp(int i, int i2) throws RemoteException;

    boolean removeCallingPkgHookRule(String str, String str2) throws RemoteException;

    void removeServicePriority(MiuiServicePriority miuiServicePriority, boolean z) throws RemoteException;

    void removeTimeConsumingIntent(String[] strArr) throws RemoteException;

    void reportAnr(int i, String str, long j, long j2, String str2) throws RemoteException;

    void reportExcessiveCpuUsageRecords(List<Bundle> list) throws RemoteException;

    void reportNotificationClick(String str, Intent intent, long j) throws RemoteException;

    void reportPerceptibleJank(int i, int i2, String str, long j, long j2, long j3, int i3, long j4) throws RemoteException;

    List<QuickAppResolveInfo> resolveQuickAppInfos(Intent intent) throws RemoteException;

    void setForkedProcessGroup(int i, int i2, int i3, String str) throws RemoteException;

    void setHapLinks(Map map, ActivityInfo activityInfo) throws RemoteException;

    void setMiuiBroadcastDispatchEnable(boolean z) throws RemoteException;

    void setMiuiContentProviderControl(boolean z) throws RemoteException;

    void setSchedFgPid(int i) throws RemoteException;

    void setServicePriority(List<MiuiServicePriority> list) throws RemoteException;

    void setServicePriorityWithNoProc(List<MiuiServicePriority> list, long j) throws RemoteException;

    List<Bundle> updateProcessFullMemInfoByPids(int[] iArr) throws RemoteException;

    List<Bundle> updateProcessPartialMemInfoByPids(int[] iArr) throws RemoteException;
}
