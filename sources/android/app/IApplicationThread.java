package android.app;

import android.annotation.UnsupportedAppUsage;
import android.app.servertransaction.ClientTransaction;
import android.content.AutofillOptions;
import android.content.ComponentName;
import android.content.ContentCaptureOptions;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ParceledListSlice;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug.MemoryInfo;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteCallback;
import android.os.RemoteException;
import com.android.internal.app.IVoiceInteractor;
import java.util.List;
import java.util.Map;

public interface IApplicationThread extends IInterface {

    public static abstract class Stub extends Binder implements IApplicationThread {
        private static final String DESCRIPTOR = "android.app.IApplicationThread";
        static final int TRANSACTION_attachAgent = 48;
        static final int TRANSACTION_bindApplication = 4;
        static final int TRANSACTION_clearDnsCache = 26;
        static final int TRANSACTION_dispatchPackageBroadcast = 22;
        static final int TRANSACTION_dumpActivity = 25;
        static final int TRANSACTION_dumpDbInfo = 35;
        static final int TRANSACTION_dumpGfxInfo = 33;
        static final int TRANSACTION_dumpHeap = 24;
        static final int TRANSACTION_dumpMemInfo = 31;
        static final int TRANSACTION_dumpMemInfoProto = 32;
        static final int TRANSACTION_dumpProvider = 34;
        static final int TRANSACTION_dumpService = 12;
        static final int TRANSACTION_handleTrustStorageUpdate = 47;
        static final int TRANSACTION_notifyCleartextNetwork = 43;
        static final int TRANSACTION_notifyPackageForeground = 54;
        static final int TRANSACTION_performDirectAction = 53;
        static final int TRANSACTION_processInBackground = 9;
        static final int TRANSACTION_profilerControl = 16;
        static final int TRANSACTION_requestAssistContextExtras = 37;
        static final int TRANSACTION_requestDirectActions = 52;
        static final int TRANSACTION_runIsolatedEntryPoint = 5;
        static final int TRANSACTION_scheduleApplicationInfoChanged = 49;
        static final int TRANSACTION_scheduleBindService = 10;
        static final int TRANSACTION_scheduleCrash = 23;
        static final int TRANSACTION_scheduleCreateBackupAgent = 18;
        static final int TRANSACTION_scheduleCreateService = 2;
        static final int TRANSACTION_scheduleDestroyBackupAgent = 19;
        static final int TRANSACTION_scheduleEnterAnimationComplete = 42;
        static final int TRANSACTION_scheduleExit = 6;
        static final int TRANSACTION_scheduleInstallProvider = 40;
        static final int TRANSACTION_scheduleLocalVoiceInteractionStarted = 46;
        static final int TRANSACTION_scheduleLowMemory = 14;
        static final int TRANSACTION_scheduleOnNewActivityOptions = 20;
        static final int TRANSACTION_scheduleReceiver = 1;
        static final int TRANSACTION_scheduleRegisteredReceiver = 13;
        static final int TRANSACTION_scheduleServiceArgs = 7;
        static final int TRANSACTION_scheduleSleeping = 15;
        static final int TRANSACTION_scheduleStopService = 3;
        static final int TRANSACTION_scheduleSuicide = 21;
        static final int TRANSACTION_scheduleTransaction = 51;
        static final int TRANSACTION_scheduleTranslucentConversionComplete = 38;
        static final int TRANSACTION_scheduleTrimMemory = 30;
        static final int TRANSACTION_scheduleUnbindService = 11;
        static final int TRANSACTION_setCoreSettings = 28;
        static final int TRANSACTION_setNetworkBlockSeq = 50;
        static final int TRANSACTION_setProcessState = 39;
        static final int TRANSACTION_setSchedulingGroup = 17;
        static final int TRANSACTION_startBinderTracking = 44;
        static final int TRANSACTION_stopBinderTrackingAndDump = 45;
        static final int TRANSACTION_unstableProviderDied = 36;
        static final int TRANSACTION_updateHttpProxy = 27;
        static final int TRANSACTION_updatePackageCompatibilityInfo = 29;
        static final int TRANSACTION_updateTimePrefs = 41;
        static final int TRANSACTION_updateTimeZone = 8;

        private static class Proxy implements IApplicationThread {
            public static IApplicationThread sDefaultImpl;
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

            public void scheduleReceiver(Intent intent, ActivityInfo info, CompatibilityInfo compatInfo, int resultCode, String data, Bundle extras, boolean sync, int sendingUser, int processState) throws RemoteException {
                Intent intent2 = intent;
                ActivityInfo activityInfo = info;
                CompatibilityInfo compatibilityInfo = compatInfo;
                Bundle bundle = extras;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (intent2 != null) {
                        _data.writeInt(1);
                        intent2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (activityInfo != null) {
                        _data.writeInt(1);
                        activityInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (compatibilityInfo != null) {
                        _data.writeInt(1);
                        compatibilityInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(resultCode);
                    _data.writeString(data);
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (sync) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(sendingUser);
                    _data.writeInt(processState);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleReceiver(intent, info, compatInfo, resultCode, data, extras, sync, sendingUser, processState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleCreateService(IBinder token, ServiceInfo info, CompatibilityInfo compatInfo, int processState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (compatInfo != null) {
                        _data.writeInt(1);
                        compatInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(processState);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleCreateService(token, info, compatInfo, processState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleStopService(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleStopService(token);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void bindApplication(String packageName, ApplicationInfo info, List<ProviderInfo> providers, ComponentName testName, ProfilerInfo profilerInfo, Bundle testArguments, IInstrumentationWatcher testWatcher, IUiAutomationConnection uiAutomationConnection, int debugMode, boolean enableBinderTracking, boolean trackAllocation, boolean restrictedBackupMode, boolean persistent, Configuration config, CompatibilityInfo compatInfo, Map services, Bundle coreSettings, String buildSerial, AutofillOptions autofillOptions, ContentCaptureOptions contentCaptureOptions) throws RemoteException {
                Throwable th;
                Parcel _data;
                ApplicationInfo applicationInfo = info;
                ComponentName componentName = testName;
                ProfilerInfo profilerInfo2 = profilerInfo;
                Bundle bundle = testArguments;
                Configuration configuration = config;
                CompatibilityInfo compatibilityInfo = compatInfo;
                Bundle bundle2 = coreSettings;
                AutofillOptions autofillOptions2 = autofillOptions;
                ContentCaptureOptions contentCaptureOptions2 = contentCaptureOptions;
                Parcel _data2 = Parcel.obtain();
                _data2.writeInterfaceToken(Stub.DESCRIPTOR);
                _data2.writeString(packageName);
                if (applicationInfo != null) {
                    try {
                        _data2.writeInt(1);
                        applicationInfo.writeToParcel(_data2, 0);
                    } catch (Throwable th2) {
                        th = th2;
                        _data = _data2;
                    }
                } else {
                    _data2.writeInt(0);
                }
                _data2.writeTypedList(providers);
                if (componentName != null) {
                    _data2.writeInt(1);
                    componentName.writeToParcel(_data2, 0);
                } else {
                    _data2.writeInt(0);
                }
                if (profilerInfo2 != null) {
                    _data2.writeInt(1);
                    profilerInfo2.writeToParcel(_data2, 0);
                } else {
                    _data2.writeInt(0);
                }
                if (bundle != null) {
                    _data2.writeInt(1);
                    bundle.writeToParcel(_data2, 0);
                } else {
                    _data2.writeInt(0);
                }
                try {
                    int i;
                    _data2.writeStrongBinder(testWatcher != null ? testWatcher.asBinder() : null);
                    _data2.writeStrongBinder(uiAutomationConnection != null ? uiAutomationConnection.asBinder() : null);
                    _data2.writeInt(debugMode);
                    _data2.writeInt(enableBinderTracking ? 1 : 0);
                    _data2.writeInt(trackAllocation ? 1 : 0);
                    _data2.writeInt(restrictedBackupMode ? 1 : 0);
                    _data2.writeInt(persistent ? 1 : 0);
                    if (configuration != null) {
                        _data2.writeInt(1);
                        configuration.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    if (compatibilityInfo != null) {
                        _data2.writeInt(1);
                        compatibilityInfo.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeMap(services);
                    if (bundle2 != null) {
                        _data2.writeInt(1);
                        bundle2.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeString(buildSerial);
                    if (autofillOptions2 != null) {
                        _data2.writeInt(1);
                        i = 0;
                        autofillOptions2.writeToParcel(_data2, 0);
                    } else {
                        i = 0;
                        _data2.writeInt(0);
                    }
                    if (contentCaptureOptions2 != null) {
                        _data2.writeInt(1);
                        contentCaptureOptions2.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(i);
                    }
                    if (this.mRemote.transact(4, _data2, null, 1) || Stub.getDefaultImpl() == null) {
                        _data2.recycle();
                        return;
                    }
                    _data = _data2;
                    try {
                        Stub.getDefaultImpl().bindApplication(packageName, info, providers, testName, profilerInfo, testArguments, testWatcher, uiAutomationConnection, debugMode, enableBinderTracking, trackAllocation, restrictedBackupMode, persistent, config, compatInfo, services, coreSettings, buildSerial, autofillOptions, contentCaptureOptions);
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _data = _data2;
                    _data.recycle();
                    throw th;
                }
            }

            public void runIsolatedEntryPoint(String entryPoint, String[] entryPointArgs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(entryPoint);
                    _data.writeStringArray(entryPointArgs);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().runIsolatedEntryPoint(entryPoint, entryPointArgs);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleExit() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleExit();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleServiceArgs(IBinder token, ParceledListSlice args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleServiceArgs(token, args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void updateTimeZone() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().updateTimeZone();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void processInBackground() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().processInBackground();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleBindService(IBinder token, Intent intent, boolean rebind, int processState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    int i = 0;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (rebind) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(processState);
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleBindService(token, intent, rebind, processState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleUnbindService(IBinder token, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleUnbindService(token, intent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dumpService(ParcelFileDescriptor fd, IBinder servicetoken, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(servicetoken);
                    _data.writeStringArray(args);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dumpService(fd, servicetoken, args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleRegisteredReceiver(IIntentReceiver receiver, Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser, int processState) throws RemoteException {
                Throwable th;
                String str;
                Intent intent2 = intent;
                Bundle bundle = extras;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    int i = 0;
                    if (intent2 != null) {
                        _data.writeInt(1);
                        intent2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(resultCode);
                        try {
                            _data.writeString(data);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeInt(ordered ? 1 : 0);
                            if (sticky) {
                                i = 1;
                            }
                            _data.writeInt(i);
                            _data.writeInt(sendingUser);
                            _data.writeInt(processState);
                            if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().scheduleRegisteredReceiver(receiver, intent, resultCode, data, extras, ordered, sticky, sendingUser, processState);
                            _data.recycle();
                        } catch (Throwable th2) {
                            th = th2;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        str = data;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    int i2 = resultCode;
                    str = data;
                    _data.recycle();
                    throw th;
                }
            }

            public void scheduleLowMemory() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleLowMemory();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleSleeping(IBinder token, boolean sleeping) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(sleeping ? 1 : 0);
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleSleeping(token, sleeping);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void profilerControl(boolean start, ProfilerInfo profilerInfo, int profileType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(start ? 1 : 0);
                    if (profilerInfo != null) {
                        _data.writeInt(1);
                        profilerInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(profileType);
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().profilerControl(start, profilerInfo, profileType);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setSchedulingGroup(int group) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(group);
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setSchedulingGroup(group);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleCreateBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo, int backupMode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (app != null) {
                        _data.writeInt(1);
                        app.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (compatInfo != null) {
                        _data.writeInt(1);
                        compatInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(backupMode);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(18, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleCreateBackupAgent(app, compatInfo, backupMode, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleDestroyBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (app != null) {
                        _data.writeInt(1);
                        app.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (compatInfo != null) {
                        _data.writeInt(1);
                        compatInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(19, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleDestroyBackupAgent(app, compatInfo, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleOnNewActivityOptions(IBinder token, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(20, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleOnNewActivityOptions(token, options);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleSuicide() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(21, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleSuicide();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchPackageBroadcast(int cmd, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmd);
                    _data.writeStringArray(packages);
                    if (this.mRemote.transact(22, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dispatchPackageBroadcast(cmd, packages);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleCrash(String msg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(msg);
                    if (this.mRemote.transact(23, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleCrash(msg);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dumpHeap(boolean managed, boolean mallocInfo, boolean runGc, String path, ParcelFileDescriptor fd, RemoteCallback finishCallback) throws RemoteException {
                Throwable th;
                ParcelFileDescriptor parcelFileDescriptor = fd;
                RemoteCallback remoteCallback = finishCallback;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(managed ? 1 : 0);
                    _data.writeInt(mallocInfo ? 1 : 0);
                    _data.writeInt(runGc ? 1 : 0);
                    try {
                        _data.writeString(path);
                        if (parcelFileDescriptor != null) {
                            _data.writeInt(1);
                            parcelFileDescriptor.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (remoteCallback != null) {
                            _data.writeInt(1);
                            remoteCallback.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(24, _data, null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().dumpHeap(managed, mallocInfo, runGc, path, fd, finishCallback);
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    String str = path;
                    _data.recycle();
                    throw th;
                }
            }

            public void dumpActivity(ParcelFileDescriptor fd, IBinder servicetoken, String prefix, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(servicetoken);
                    _data.writeString(prefix);
                    _data.writeStringArray(args);
                    if (this.mRemote.transact(25, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dumpActivity(fd, servicetoken, prefix, args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void clearDnsCache() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(26, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().clearDnsCache();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void updateHttpProxy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(27, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().updateHttpProxy();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setCoreSettings(Bundle coreSettings) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (coreSettings != null) {
                        _data.writeInt(1);
                        coreSettings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(28, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setCoreSettings(coreSettings);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void updatePackageCompatibilityInfo(String pkg, CompatibilityInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(29, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().updatePackageCompatibilityInfo(pkg, info);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleTrimMemory(int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(level);
                    if (this.mRemote.transact(30, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleTrimMemory(level);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dumpMemInfo(ParcelFileDescriptor fd, MemoryInfo mem, boolean checkin, boolean dumpInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) throws RemoteException {
                Throwable th;
                ParcelFileDescriptor parcelFileDescriptor = fd;
                MemoryInfo memoryInfo = mem;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (parcelFileDescriptor != null) {
                        _data.writeInt(1);
                        parcelFileDescriptor.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (memoryInfo != null) {
                        _data.writeInt(1);
                        memoryInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(checkin ? 1 : 0);
                    _data.writeInt(dumpInfo ? 1 : 0);
                    _data.writeInt(dumpDalvik ? 1 : 0);
                    _data.writeInt(dumpSummaryOnly ? 1 : 0);
                    if (dumpUnreachable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    try {
                        _data.writeStringArray(args);
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(31, _data, null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().dumpMemInfo(fd, mem, checkin, dumpInfo, dumpDalvik, dumpSummaryOnly, dumpUnreachable, args);
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    String[] strArr = args;
                    _data.recycle();
                    throw th;
                }
            }

            public void dumpMemInfoProto(ParcelFileDescriptor fd, MemoryInfo mem, boolean dumpInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) throws RemoteException {
                Throwable th;
                ParcelFileDescriptor parcelFileDescriptor = fd;
                MemoryInfo memoryInfo = mem;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (parcelFileDescriptor != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (memoryInfo != null) {
                        _data.writeInt(1);
                        memoryInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(dumpInfo ? 1 : 0);
                    _data.writeInt(dumpDalvik ? 1 : 0);
                    _data.writeInt(dumpSummaryOnly ? 1 : 0);
                    if (dumpUnreachable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    try {
                        _data.writeStringArray(args);
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(32, _data, null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().dumpMemInfoProto(fd, mem, dumpInfo, dumpDalvik, dumpSummaryOnly, dumpUnreachable, args);
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    String[] strArr = args;
                    _data.recycle();
                    throw th;
                }
            }

            public void dumpGfxInfo(ParcelFileDescriptor fd, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(args);
                    if (this.mRemote.transact(33, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dumpGfxInfo(fd, args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dumpProvider(ParcelFileDescriptor fd, IBinder servicetoken, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(servicetoken);
                    _data.writeStringArray(args);
                    if (this.mRemote.transact(34, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dumpProvider(fd, servicetoken, args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dumpDbInfo(ParcelFileDescriptor fd, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(args);
                    if (this.mRemote.transact(35, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dumpDbInfo(fd, args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void unstableProviderDied(IBinder provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(provider);
                    if (this.mRemote.transact(36, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unstableProviderDied(provider);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void requestAssistContextExtras(IBinder activityToken, IBinder requestToken, int requestType, int sessionId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    _data.writeStrongBinder(requestToken);
                    _data.writeInt(requestType);
                    _data.writeInt(sessionId);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(37, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().requestAssistContextExtras(activityToken, requestToken, requestType, sessionId, flags);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleTranslucentConversionComplete(IBinder token, boolean timeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(timeout ? 1 : 0);
                    if (this.mRemote.transact(38, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleTranslucentConversionComplete(token, timeout);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setProcessState(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    if (this.mRemote.transact(39, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setProcessState(state);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleInstallProvider(ProviderInfo provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (provider != null) {
                        _data.writeInt(1);
                        provider.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(40, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleInstallProvider(provider);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void updateTimePrefs(int timeFormatPreference) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(timeFormatPreference);
                    if (this.mRemote.transact(41, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().updateTimePrefs(timeFormatPreference);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleEnterAnimationComplete(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(42, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleEnterAnimationComplete(token);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyCleartextNetwork(byte[] firstPacket) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(firstPacket);
                    if (this.mRemote.transact(43, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyCleartextNetwork(firstPacket);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void startBinderTracking() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(44, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().startBinderTracking();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void stopBinderTrackingAndDump(ParcelFileDescriptor fd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(45, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().stopBinderTrackingAndDump(fd);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleLocalVoiceInteractionStarted(IBinder token, IVoiceInteractor voiceInteractor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeStrongBinder(voiceInteractor != null ? voiceInteractor.asBinder() : null);
                    if (this.mRemote.transact(46, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleLocalVoiceInteractionStarted(token, voiceInteractor);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void handleTrustStorageUpdate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(47, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().handleTrustStorageUpdate();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void attachAgent(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    if (this.mRemote.transact(48, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().attachAgent(path);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleApplicationInfoChanged(ApplicationInfo ai) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ai != null) {
                        _data.writeInt(1);
                        ai.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(49, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleApplicationInfoChanged(ai);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setNetworkBlockSeq(long procStateSeq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(procStateSeq);
                    if (this.mRemote.transact(50, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setNetworkBlockSeq(procStateSeq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void scheduleTransaction(ClientTransaction transaction) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (transaction != null) {
                        _data.writeInt(1);
                        transaction.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(51, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().scheduleTransaction(transaction);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void requestDirectActions(IBinder activityToken, IVoiceInteractor intractor, RemoteCallback cancellationCallback, RemoteCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    _data.writeStrongBinder(intractor != null ? intractor.asBinder() : null);
                    if (cancellationCallback != null) {
                        _data.writeInt(1);
                        cancellationCallback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (callback != null) {
                        _data.writeInt(1);
                        callback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(52, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().requestDirectActions(activityToken, intractor, cancellationCallback, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void performDirectAction(IBinder activityToken, String actionId, Bundle arguments, RemoteCallback cancellationCallback, RemoteCallback resultCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    _data.writeString(actionId);
                    if (arguments != null) {
                        _data.writeInt(1);
                        arguments.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (cancellationCallback != null) {
                        _data.writeInt(1);
                        cancellationCallback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (resultCallback != null) {
                        _data.writeInt(1);
                        resultCallback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(53, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().performDirectAction(activityToken, actionId, arguments, cancellationCallback, resultCallback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyPackageForeground() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(54, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyPackageForeground();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IApplicationThread asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IApplicationThread)) {
                return new Proxy(obj);
            }
            return (IApplicationThread) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "scheduleReceiver";
                case 2:
                    return "scheduleCreateService";
                case 3:
                    return "scheduleStopService";
                case 4:
                    return "bindApplication";
                case 5:
                    return "runIsolatedEntryPoint";
                case 6:
                    return "scheduleExit";
                case 7:
                    return "scheduleServiceArgs";
                case 8:
                    return "updateTimeZone";
                case 9:
                    return "processInBackground";
                case 10:
                    return "scheduleBindService";
                case 11:
                    return "scheduleUnbindService";
                case 12:
                    return "dumpService";
                case 13:
                    return "scheduleRegisteredReceiver";
                case 14:
                    return "scheduleLowMemory";
                case 15:
                    return "scheduleSleeping";
                case 16:
                    return "profilerControl";
                case 17:
                    return "setSchedulingGroup";
                case 18:
                    return "scheduleCreateBackupAgent";
                case 19:
                    return "scheduleDestroyBackupAgent";
                case 20:
                    return "scheduleOnNewActivityOptions";
                case 21:
                    return "scheduleSuicide";
                case 22:
                    return "dispatchPackageBroadcast";
                case 23:
                    return "scheduleCrash";
                case 24:
                    return "dumpHeap";
                case 25:
                    return "dumpActivity";
                case 26:
                    return "clearDnsCache";
                case 27:
                    return "updateHttpProxy";
                case 28:
                    return "setCoreSettings";
                case 29:
                    return "updatePackageCompatibilityInfo";
                case 30:
                    return "scheduleTrimMemory";
                case 31:
                    return "dumpMemInfo";
                case 32:
                    return "dumpMemInfoProto";
                case 33:
                    return "dumpGfxInfo";
                case 34:
                    return "dumpProvider";
                case 35:
                    return "dumpDbInfo";
                case 36:
                    return "unstableProviderDied";
                case 37:
                    return "requestAssistContextExtras";
                case 38:
                    return "scheduleTranslucentConversionComplete";
                case 39:
                    return "setProcessState";
                case 40:
                    return "scheduleInstallProvider";
                case 41:
                    return "updateTimePrefs";
                case 42:
                    return "scheduleEnterAnimationComplete";
                case 43:
                    return "notifyCleartextNetwork";
                case 44:
                    return "startBinderTracking";
                case 45:
                    return "stopBinderTrackingAndDump";
                case 46:
                    return "scheduleLocalVoiceInteractionStarted";
                case 47:
                    return "handleTrustStorageUpdate";
                case 48:
                    return "attachAgent";
                case 49:
                    return "scheduleApplicationInfoChanged";
                case 50:
                    return "setNetworkBlockSeq";
                case 51:
                    return "scheduleTransaction";
                case 52:
                    return "requestDirectActions";
                case 53:
                    return "performDirectAction";
                case 54:
                    return "notifyPackageForeground";
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
                boolean _arg2 = false;
                String descriptor2;
                Parcel parcel2;
                int _arg3;
                String _arg4;
                Bundle _arg5;
                IBinder _arg0;
                IBinder _arg02;
                ParcelFileDescriptor _arg03;
                ApplicationInfo _arg04;
                CompatibilityInfo _arg1;
                String _arg32;
                ParcelFileDescriptor _arg42;
                RemoteCallback _arg52;
                switch (i) {
                    case 1:
                        Intent _arg05;
                        ActivityInfo _arg12;
                        CompatibilityInfo _arg22;
                        descriptor2 = descriptor;
                        parcel2 = parcel;
                        parcel2.enforceInterface(descriptor2);
                        if (data.readInt() != 0) {
                            _arg05 = (Intent) Intent.CREATOR.createFromParcel(parcel2);
                        } else {
                            _arg05 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg12 = (ActivityInfo) ActivityInfo.CREATOR.createFromParcel(parcel2);
                        } else {
                            _arg12 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg22 = (CompatibilityInfo) CompatibilityInfo.CREATOR.createFromParcel(parcel2);
                        } else {
                            _arg22 = null;
                        }
                        _arg3 = data.readInt();
                        _arg4 = data.readString();
                        if (data.readInt() != 0) {
                            _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel2);
                        } else {
                            _arg5 = null;
                        }
                        scheduleReceiver(_arg05, _arg12, _arg22, _arg3, _arg4, _arg5, data.readInt() != 0, data.readInt(), data.readInt());
                        return true;
                    case 2:
                        ServiceInfo _arg13;
                        CompatibilityInfo _arg23;
                        descriptor2 = descriptor;
                        parcel2 = parcel;
                        parcel2.enforceInterface(descriptor2);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg13 = (ServiceInfo) ServiceInfo.CREATOR.createFromParcel(parcel2);
                        } else {
                            _arg13 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg23 = (CompatibilityInfo) CompatibilityInfo.CREATOR.createFromParcel(parcel2);
                        } else {
                            _arg23 = null;
                        }
                        scheduleCreateService(_arg0, _arg13, _arg23, data.readInt());
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        scheduleStopService(data.readStrongBinder());
                        return true;
                    case 4:
                        ApplicationInfo _arg14;
                        ComponentName _arg33;
                        ProfilerInfo _arg43;
                        Bundle _arg53;
                        Configuration _arg132;
                        CompatibilityInfo _arg142;
                        Bundle _arg16;
                        AutofillOptions _arg18;
                        ContentCaptureOptions _arg19;
                        parcel.enforceInterface(descriptor);
                        String _arg06 = data.readString();
                        if (data.readInt() != 0) {
                            _arg14 = (ApplicationInfo) ApplicationInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        List<ProviderInfo> _arg24 = parcel.createTypedArrayList(ProviderInfo.CREATOR);
                        if (data.readInt() != 0) {
                            _arg33 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg33 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg43 = (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg43 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg53 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg53 = null;
                        }
                        IInstrumentationWatcher _arg6 = android.app.IInstrumentationWatcher.Stub.asInterface(data.readStrongBinder());
                        IUiAutomationConnection _arg7 = android.app.IUiAutomationConnection.Stub.asInterface(data.readStrongBinder());
                        int _arg8 = data.readInt();
                        boolean _arg9 = data.readInt() != 0;
                        boolean _arg10 = data.readInt() != 0;
                        String descriptor3 = descriptor;
                        descriptor = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        Parcel parcel3 = parcel;
                        boolean _arg122 = _arg2;
                        if (data.readInt() != 0) {
                            _arg132 = (Configuration) Configuration.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg132 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg142 = (CompatibilityInfo) CompatibilityInfo.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg142 = null;
                        }
                        ClassLoader cl = getClass().getClassLoader();
                        Map _arg15 = parcel3.readHashMap(cl);
                        if (data.readInt() != 0) {
                            _arg16 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg16 = null;
                        }
                        String _arg17 = data.readString();
                        if (data.readInt() != 0) {
                            _arg18 = (AutofillOptions) AutofillOptions.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg18 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg19 = (ContentCaptureOptions) ContentCaptureOptions.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg19 = null;
                        }
                        bindApplication(_arg06, _arg14, _arg24, _arg33, _arg43, _arg53, _arg6, _arg7, _arg8, _arg9, _arg10, descriptor, _arg122, _arg132, _arg142, _arg15, _arg16, _arg17, _arg18, _arg19);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        runIsolatedEntryPoint(data.readString(), data.createStringArray());
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        scheduleExit();
                        return true;
                    case 7:
                        ParceledListSlice _arg110;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg110 = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg110 = null;
                        }
                        scheduleServiceArgs(_arg0, _arg110);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        updateTimeZone();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        processInBackground();
                        return true;
                    case 10:
                        Intent _arg111;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg111 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg111 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        scheduleBindService(_arg02, _arg111, _arg2, data.readInt());
                        return true;
                    case 11:
                        Intent _arg112;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg112 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg112 = null;
                        }
                        scheduleUnbindService(_arg0, _arg112);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        dumpService(_arg03, data.readStrongBinder(), data.createStringArray());
                        return true;
                    case 13:
                        Intent _arg113;
                        parcel.enforceInterface(descriptor);
                        IIntentReceiver _arg07 = android.content.IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg113 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg113 = null;
                        }
                        _arg3 = data.readInt();
                        _arg4 = data.readString();
                        if (data.readInt() != 0) {
                            _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        scheduleRegisteredReceiver(_arg07, _arg113, _arg3, _arg4, _arg5, data.readInt() != 0, data.readInt() != 0, data.readInt(), data.readInt());
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        scheduleLowMemory();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        scheduleSleeping(_arg02, _arg2);
                        return true;
                    case 16:
                        ProfilerInfo _arg114;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        if (data.readInt() != 0) {
                            _arg114 = (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg114 = null;
                        }
                        profilerControl(_arg2, _arg114, data.readInt());
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        setSchedulingGroup(data.readInt());
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ApplicationInfo) ApplicationInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = (CompatibilityInfo) CompatibilityInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        scheduleCreateBackupAgent(_arg04, _arg1, data.readInt(), data.readInt());
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ApplicationInfo) ApplicationInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = (CompatibilityInfo) CompatibilityInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        scheduleDestroyBackupAgent(_arg04, _arg1, data.readInt());
                        return true;
                    case 20:
                        Bundle _arg115;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg115 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg115 = null;
                        }
                        scheduleOnNewActivityOptions(_arg0, _arg115);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        scheduleSuicide();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        dispatchPackageBroadcast(data.readInt(), data.createStringArray());
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        scheduleCrash(data.readString());
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        boolean _arg08 = data.readInt() != 0;
                        boolean _arg116 = data.readInt() != 0;
                        boolean _arg25 = data.readInt() != 0;
                        _arg32 = data.readString();
                        if (data.readInt() != 0) {
                            _arg42 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg52 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg52 = null;
                        }
                        dumpHeap(_arg08, _arg116, _arg25, _arg32, _arg42, _arg52);
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        dumpActivity(_arg03, data.readStrongBinder(), data.readString(), data.createStringArray());
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        clearDnsCache();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        updateHttpProxy();
                        return true;
                    case 28:
                        Bundle _arg09;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg09 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg09 = null;
                        }
                        setCoreSettings(_arg09);
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        String _arg010 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = (CompatibilityInfo) CompatibilityInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        updatePackageCompatibilityInfo(_arg010, _arg1);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        scheduleTrimMemory(data.readInt());
                        return true;
                    case 31:
                        ParcelFileDescriptor _arg011;
                        MemoryInfo _arg117;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg011 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg011 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg117 = (MemoryInfo) MemoryInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg117 = null;
                        }
                        dumpMemInfo(_arg011, _arg117, data.readInt() != 0, data.readInt() != 0, data.readInt() != 0, data.readInt() != 0, data.readInt() != 0, data.createStringArray());
                        return true;
                    case 32:
                        MemoryInfo _arg118;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg42 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg118 = (MemoryInfo) MemoryInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg118 = null;
                        }
                        dumpMemInfoProto(_arg42, _arg118, data.readInt() != 0, data.readInt() != 0, data.readInt() != 0, data.readInt() != 0, data.createStringArray());
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        dumpGfxInfo(_arg03, data.createStringArray());
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        dumpProvider(_arg03, data.readStrongBinder(), data.createStringArray());
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        dumpDbInfo(_arg03, data.createStringArray());
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        unstableProviderDied(data.readStrongBinder());
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        requestAssistContextExtras(data.readStrongBinder(), data.readStrongBinder(), data.readInt(), data.readInt(), data.readInt());
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        scheduleTranslucentConversionComplete(_arg02, _arg2);
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        setProcessState(data.readInt());
                        return true;
                    case 40:
                        ProviderInfo _arg012;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg012 = (ProviderInfo) ProviderInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg012 = null;
                        }
                        scheduleInstallProvider(_arg012);
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        updateTimePrefs(data.readInt());
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        scheduleEnterAnimationComplete(data.readStrongBinder());
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        notifyCleartextNetwork(data.createByteArray());
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        startBinderTracking();
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        stopBinderTrackingAndDump(_arg03);
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        scheduleLocalVoiceInteractionStarted(data.readStrongBinder(), com.android.internal.app.IVoiceInteractor.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        handleTrustStorageUpdate();
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        attachAgent(data.readString());
                        return true;
                    case 49:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ApplicationInfo) ApplicationInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        scheduleApplicationInfoChanged(_arg04);
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        setNetworkBlockSeq(data.readLong());
                        return true;
                    case 51:
                        ClientTransaction _arg013;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg013 = (ClientTransaction) ClientTransaction.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg013 = null;
                        }
                        scheduleTransaction(_arg013);
                        return true;
                    case 52:
                        RemoteCallback _arg26;
                        RemoteCallback _arg34;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        IVoiceInteractor _arg119 = com.android.internal.app.IVoiceInteractor.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg26 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg26 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg34 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg34 = null;
                        }
                        requestDirectActions(_arg0, _arg119, _arg26, _arg34);
                        return true;
                    case 53:
                        Bundle _arg27;
                        RemoteCallback _arg44;
                        parcel.enforceInterface(descriptor);
                        IBinder _arg014 = data.readStrongBinder();
                        _arg32 = data.readString();
                        if (data.readInt() != 0) {
                            _arg27 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg27 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg52 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg52 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg44 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg44 = null;
                        }
                        performDirectAction(_arg014, _arg32, _arg27, _arg52, _arg44);
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        notifyPackageForeground();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IApplicationThread impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IApplicationThread getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IApplicationThread {
        public void scheduleReceiver(Intent intent, ActivityInfo info, CompatibilityInfo compatInfo, int resultCode, String data, Bundle extras, boolean sync, int sendingUser, int processState) throws RemoteException {
        }

        public void scheduleCreateService(IBinder token, ServiceInfo info, CompatibilityInfo compatInfo, int processState) throws RemoteException {
        }

        public void scheduleStopService(IBinder token) throws RemoteException {
        }

        public void bindApplication(String packageName, ApplicationInfo info, List<ProviderInfo> list, ComponentName testName, ProfilerInfo profilerInfo, Bundle testArguments, IInstrumentationWatcher testWatcher, IUiAutomationConnection uiAutomationConnection, int debugMode, boolean enableBinderTracking, boolean trackAllocation, boolean restrictedBackupMode, boolean persistent, Configuration config, CompatibilityInfo compatInfo, Map services, Bundle coreSettings, String buildSerial, AutofillOptions autofillOptions, ContentCaptureOptions contentCaptureOptions) throws RemoteException {
        }

        public void runIsolatedEntryPoint(String entryPoint, String[] entryPointArgs) throws RemoteException {
        }

        public void scheduleExit() throws RemoteException {
        }

        public void scheduleServiceArgs(IBinder token, ParceledListSlice args) throws RemoteException {
        }

        public void updateTimeZone() throws RemoteException {
        }

        public void processInBackground() throws RemoteException {
        }

        public void scheduleBindService(IBinder token, Intent intent, boolean rebind, int processState) throws RemoteException {
        }

        public void scheduleUnbindService(IBinder token, Intent intent) throws RemoteException {
        }

        public void dumpService(ParcelFileDescriptor fd, IBinder servicetoken, String[] args) throws RemoteException {
        }

        public void scheduleRegisteredReceiver(IIntentReceiver receiver, Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser, int processState) throws RemoteException {
        }

        public void scheduleLowMemory() throws RemoteException {
        }

        public void scheduleSleeping(IBinder token, boolean sleeping) throws RemoteException {
        }

        public void profilerControl(boolean start, ProfilerInfo profilerInfo, int profileType) throws RemoteException {
        }

        public void setSchedulingGroup(int group) throws RemoteException {
        }

        public void scheduleCreateBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo, int backupMode, int userId) throws RemoteException {
        }

        public void scheduleDestroyBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo, int userId) throws RemoteException {
        }

        public void scheduleOnNewActivityOptions(IBinder token, Bundle options) throws RemoteException {
        }

        public void scheduleSuicide() throws RemoteException {
        }

        public void dispatchPackageBroadcast(int cmd, String[] packages) throws RemoteException {
        }

        public void scheduleCrash(String msg) throws RemoteException {
        }

        public void dumpHeap(boolean managed, boolean mallocInfo, boolean runGc, String path, ParcelFileDescriptor fd, RemoteCallback finishCallback) throws RemoteException {
        }

        public void dumpActivity(ParcelFileDescriptor fd, IBinder servicetoken, String prefix, String[] args) throws RemoteException {
        }

        public void clearDnsCache() throws RemoteException {
        }

        public void updateHttpProxy() throws RemoteException {
        }

        public void setCoreSettings(Bundle coreSettings) throws RemoteException {
        }

        public void updatePackageCompatibilityInfo(String pkg, CompatibilityInfo info) throws RemoteException {
        }

        public void scheduleTrimMemory(int level) throws RemoteException {
        }

        public void dumpMemInfo(ParcelFileDescriptor fd, MemoryInfo mem, boolean checkin, boolean dumpInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) throws RemoteException {
        }

        public void dumpMemInfoProto(ParcelFileDescriptor fd, MemoryInfo mem, boolean dumpInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) throws RemoteException {
        }

        public void dumpGfxInfo(ParcelFileDescriptor fd, String[] args) throws RemoteException {
        }

        public void dumpProvider(ParcelFileDescriptor fd, IBinder servicetoken, String[] args) throws RemoteException {
        }

        public void dumpDbInfo(ParcelFileDescriptor fd, String[] args) throws RemoteException {
        }

        public void unstableProviderDied(IBinder provider) throws RemoteException {
        }

        public void requestAssistContextExtras(IBinder activityToken, IBinder requestToken, int requestType, int sessionId, int flags) throws RemoteException {
        }

        public void scheduleTranslucentConversionComplete(IBinder token, boolean timeout) throws RemoteException {
        }

        public void setProcessState(int state) throws RemoteException {
        }

        public void scheduleInstallProvider(ProviderInfo provider) throws RemoteException {
        }

        public void updateTimePrefs(int timeFormatPreference) throws RemoteException {
        }

        public void scheduleEnterAnimationComplete(IBinder token) throws RemoteException {
        }

        public void notifyCleartextNetwork(byte[] firstPacket) throws RemoteException {
        }

        public void startBinderTracking() throws RemoteException {
        }

        public void stopBinderTrackingAndDump(ParcelFileDescriptor fd) throws RemoteException {
        }

        public void scheduleLocalVoiceInteractionStarted(IBinder token, IVoiceInteractor voiceInteractor) throws RemoteException {
        }

        public void handleTrustStorageUpdate() throws RemoteException {
        }

        public void attachAgent(String path) throws RemoteException {
        }

        public void scheduleApplicationInfoChanged(ApplicationInfo ai) throws RemoteException {
        }

        public void setNetworkBlockSeq(long procStateSeq) throws RemoteException {
        }

        public void scheduleTransaction(ClientTransaction transaction) throws RemoteException {
        }

        public void requestDirectActions(IBinder activityToken, IVoiceInteractor intractor, RemoteCallback cancellationCallback, RemoteCallback callback) throws RemoteException {
        }

        public void performDirectAction(IBinder activityToken, String actionId, Bundle arguments, RemoteCallback cancellationCallback, RemoteCallback resultCallback) throws RemoteException {
        }

        public void notifyPackageForeground() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void attachAgent(String str) throws RemoteException;

    void bindApplication(String str, ApplicationInfo applicationInfo, List<ProviderInfo> list, ComponentName componentName, ProfilerInfo profilerInfo, Bundle bundle, IInstrumentationWatcher iInstrumentationWatcher, IUiAutomationConnection iUiAutomationConnection, int i, boolean z, boolean z2, boolean z3, boolean z4, Configuration configuration, CompatibilityInfo compatibilityInfo, Map map, Bundle bundle2, String str2, AutofillOptions autofillOptions, ContentCaptureOptions contentCaptureOptions) throws RemoteException;

    void clearDnsCache() throws RemoteException;

    void dispatchPackageBroadcast(int i, String[] strArr) throws RemoteException;

    void dumpActivity(ParcelFileDescriptor parcelFileDescriptor, IBinder iBinder, String str, String[] strArr) throws RemoteException;

    void dumpDbInfo(ParcelFileDescriptor parcelFileDescriptor, String[] strArr) throws RemoteException;

    void dumpGfxInfo(ParcelFileDescriptor parcelFileDescriptor, String[] strArr) throws RemoteException;

    void dumpHeap(boolean z, boolean z2, boolean z3, String str, ParcelFileDescriptor parcelFileDescriptor, RemoteCallback remoteCallback) throws RemoteException;

    void dumpMemInfo(ParcelFileDescriptor parcelFileDescriptor, MemoryInfo memoryInfo, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, String[] strArr) throws RemoteException;

    void dumpMemInfoProto(ParcelFileDescriptor parcelFileDescriptor, MemoryInfo memoryInfo, boolean z, boolean z2, boolean z3, boolean z4, String[] strArr) throws RemoteException;

    void dumpProvider(ParcelFileDescriptor parcelFileDescriptor, IBinder iBinder, String[] strArr) throws RemoteException;

    void dumpService(ParcelFileDescriptor parcelFileDescriptor, IBinder iBinder, String[] strArr) throws RemoteException;

    void handleTrustStorageUpdate() throws RemoteException;

    void notifyCleartextNetwork(byte[] bArr) throws RemoteException;

    void notifyPackageForeground() throws RemoteException;

    void performDirectAction(IBinder iBinder, String str, Bundle bundle, RemoteCallback remoteCallback, RemoteCallback remoteCallback2) throws RemoteException;

    void processInBackground() throws RemoteException;

    void profilerControl(boolean z, ProfilerInfo profilerInfo, int i) throws RemoteException;

    void requestAssistContextExtras(IBinder iBinder, IBinder iBinder2, int i, int i2, int i3) throws RemoteException;

    void requestDirectActions(IBinder iBinder, IVoiceInteractor iVoiceInteractor, RemoteCallback remoteCallback, RemoteCallback remoteCallback2) throws RemoteException;

    void runIsolatedEntryPoint(String str, String[] strArr) throws RemoteException;

    void scheduleApplicationInfoChanged(ApplicationInfo applicationInfo) throws RemoteException;

    @UnsupportedAppUsage
    void scheduleBindService(IBinder iBinder, Intent intent, boolean z, int i) throws RemoteException;

    void scheduleCrash(String str) throws RemoteException;

    void scheduleCreateBackupAgent(ApplicationInfo applicationInfo, CompatibilityInfo compatibilityInfo, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void scheduleCreateService(IBinder iBinder, ServiceInfo serviceInfo, CompatibilityInfo compatibilityInfo, int i) throws RemoteException;

    void scheduleDestroyBackupAgent(ApplicationInfo applicationInfo, CompatibilityInfo compatibilityInfo, int i) throws RemoteException;

    void scheduleEnterAnimationComplete(IBinder iBinder) throws RemoteException;

    void scheduleExit() throws RemoteException;

    void scheduleInstallProvider(ProviderInfo providerInfo) throws RemoteException;

    void scheduleLocalVoiceInteractionStarted(IBinder iBinder, IVoiceInteractor iVoiceInteractor) throws RemoteException;

    void scheduleLowMemory() throws RemoteException;

    void scheduleOnNewActivityOptions(IBinder iBinder, Bundle bundle) throws RemoteException;

    void scheduleReceiver(Intent intent, ActivityInfo activityInfo, CompatibilityInfo compatibilityInfo, int i, String str, Bundle bundle, boolean z, int i2, int i3) throws RemoteException;

    void scheduleRegisteredReceiver(IIntentReceiver iIntentReceiver, Intent intent, int i, String str, Bundle bundle, boolean z, boolean z2, int i2, int i3) throws RemoteException;

    void scheduleServiceArgs(IBinder iBinder, ParceledListSlice parceledListSlice) throws RemoteException;

    void scheduleSleeping(IBinder iBinder, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void scheduleStopService(IBinder iBinder) throws RemoteException;

    void scheduleSuicide() throws RemoteException;

    void scheduleTransaction(ClientTransaction clientTransaction) throws RemoteException;

    void scheduleTranslucentConversionComplete(IBinder iBinder, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void scheduleTrimMemory(int i) throws RemoteException;

    @UnsupportedAppUsage
    void scheduleUnbindService(IBinder iBinder, Intent intent) throws RemoteException;

    void setCoreSettings(Bundle bundle) throws RemoteException;

    void setNetworkBlockSeq(long j) throws RemoteException;

    void setProcessState(int i) throws RemoteException;

    void setSchedulingGroup(int i) throws RemoteException;

    void startBinderTracking() throws RemoteException;

    void stopBinderTrackingAndDump(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void unstableProviderDied(IBinder iBinder) throws RemoteException;

    void updateHttpProxy() throws RemoteException;

    void updatePackageCompatibilityInfo(String str, CompatibilityInfo compatibilityInfo) throws RemoteException;

    void updateTimePrefs(int i) throws RemoteException;

    void updateTimeZone() throws RemoteException;
}
