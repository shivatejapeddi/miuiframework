package android.app;

import android.accounts.AccountManager;
import android.accounts.IAccountManager.Stub;
import android.app.admin.DevicePolicyManager;
import android.app.admin.IDevicePolicyManager;
import android.app.contentsuggestions.ContentSuggestionsManager;
import android.app.contentsuggestions.IContentSuggestionsManager;
import android.app.job.IJobScheduler;
import android.app.job.JobScheduler;
import android.app.prediction.AppPredictionManager;
import android.app.role.RoleControllerManager;
import android.app.role.RoleManager;
import android.app.slice.SliceManager;
import android.app.timedetector.TimeDetector;
import android.app.timezone.RulesManager;
import android.app.trust.TrustManager;
import android.app.usage.IStorageStatsManager;
import android.app.usage.IUsageStatsManager;
import android.app.usage.NetworkStatsManager;
import android.app.usage.StorageStatsManager;
import android.app.usage.UsageStatsManager;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothManager;
import android.companion.CompanionDeviceManager;
import android.companion.ICompanionDeviceManager;
import android.content.ClipboardManager;
import android.content.ContentCaptureOptions;
import android.content.Context;
import android.content.IRestrictionsManager;
import android.content.RestrictionsManager;
import android.content.om.IOverlayManager;
import android.content.om.OverlayManager;
import android.content.pm.CrossProfileApps;
import android.content.pm.ICrossProfileApps;
import android.content.pm.IShortcutService;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.content.rollback.IRollbackManager;
import android.content.rollback.RollbackManager;
import android.debug.AdbManager;
import android.debug.IAdbManager;
import android.gamepad.BsGamePadManager;
import android.gamepad.IBsGamePadService;
import android.hardware.ConsumerIrManager;
import android.hardware.ISerialManager;
import android.hardware.SensorManager;
import android.hardware.SensorPrivacyManager;
import android.hardware.SerialManager;
import android.hardware.SystemSensorManager;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.IBiometricService;
import android.hardware.camera2.CameraManager;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.DisplayManager;
import android.hardware.face.FaceManager;
import android.hardware.face.IFaceService;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.IFingerprintService;
import android.hardware.hdmi.HdmiControlManager;
import android.hardware.hdmi.IHdmiControlService;
import android.hardware.input.InputManager;
import android.hardware.iris.IIrisService;
import android.hardware.iris.IrisManager;
import android.hardware.location.ContextHubManager;
import android.hardware.radio.RadioManager;
import android.hardware.usb.IUsbManager;
import android.hardware.usb.UsbManager;
import android.location.CountryDetector;
import android.location.ICountryDetector;
import android.location.ILocationManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.media.midi.IMidiManager;
import android.media.midi.MidiManager;
import android.media.projection.MediaProjectionManager;
import android.media.session.MediaSessionManager;
import android.media.soundtrigger.SoundTriggerManager;
import android.media.tv.ITvInputManager;
import android.media.tv.TvInputManager;
import android.net.ConnectivityManager;
import android.net.ConnectivityThread;
import android.net.EthernetManager;
import android.net.IConnectivityManager;
import android.net.IEthernetManager;
import android.net.IIpSecService;
import android.net.INetworkPolicyManager;
import android.net.ITestNetworkManager;
import android.net.IpSecManager;
import android.net.NetworkPolicyManager;
import android.net.NetworkScoreManager;
import android.net.NetworkWatchlistManager;
import android.net.TestNetworkManager;
import android.net.lowpan.ILowpanManager;
import android.net.lowpan.LowpanManager;
import android.net.nsd.INsdManager;
import android.net.nsd.NsdManager;
import android.net.wifi.IWifiManager;
import android.net.wifi.IWifiScanner;
import android.net.wifi.RttManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiScanner;
import android.net.wifi.aware.IWifiAwareManager;
import android.net.wifi.aware.WifiAwareManager;
import android.net.wifi.p2p.IWifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.rtt.IWifiRttManager;
import android.net.wifi.rtt.WifiRttManager;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.BatteryStats;
import android.os.BugreportManager;
import android.os.DeviceIdleManager;
import android.os.DropBoxManager;
import android.os.HardwarePropertiesManager;
import android.os.IBatteryPropertiesRegistrar;
import android.os.IBinder;
import android.os.IDeviceIdleController;
import android.os.IDumpstate;
import android.os.IHardwarePropertiesManager;
import android.os.IPowerManager;
import android.os.IRecoverySystem;
import android.os.ISystemUpdateManager;
import android.os.IUserManager;
import android.os.IncidentManager;
import android.os.PowerManager;
import android.os.Process;
import android.os.RecoverySystem;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.os.SystemUpdateManager;
import android.os.SystemVibrator;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.Vibrator;
import android.os.health.SystemHealthManager;
import android.os.image.DynamicSystemManager;
import android.os.image.IDynamicSystemService;
import android.os.storage.StorageManager;
import android.permission.PermissionControllerManager;
import android.permission.PermissionManager;
import android.print.IPrintManager;
import android.print.PrintManager;
import android.service.oemlock.IOemLockService;
import android.service.oemlock.OemLockManager;
import android.service.persistentdata.IPersistentDataBlockService;
import android.service.persistentdata.PersistentDataBlockManager;
import android.service.vr.IVrManager;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.euicc.EuiccCardManager;
import android.telephony.euicc.EuiccManager;
import android.telephony.ims.RcsManager;
import android.util.ArrayMap;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.autofill.AutofillManager;
import android.view.autofill.IAutoFillManager;
import android.view.contentcapture.ContentCaptureManager;
import android.view.contentcapture.IContentCaptureManager;
import android.view.inputmethod.InputMethodManager;
import android.view.textclassifier.TextClassificationManager;
import android.view.textservice.TextServicesManager;
import com.android.internal.app.IAppOpsService;
import com.android.internal.app.IBatteryStats;
import com.android.internal.app.ISoundTriggerService;
import com.android.internal.appwidget.IAppWidgetService;
import com.android.internal.net.INetworkWatchlistManager;
import com.android.internal.os.IDropBoxManagerService;
import com.android.internal.policy.PhoneLayoutInflater;
import java.util.Map;

final class SystemServiceRegistry {
    private static final Map<String, ServiceFetcher<?>> SYSTEM_SERVICE_FETCHERS = new ArrayMap();
    private static final Map<Class<?>, String> SYSTEM_SERVICE_NAMES = new ArrayMap();
    private static final String TAG = "SystemServiceRegistry";
    private static int sServiceCacheSize;

    interface ServiceFetcher<T> {
        T getService(ContextImpl contextImpl);
    }

    static abstract class CachedServiceFetcher<T> implements ServiceFetcher<T> {
        private final int mCacheIndex = SystemServiceRegistry.access$008();

        public abstract T createService(ContextImpl contextImpl) throws ServiceNotFoundException;

        CachedServiceFetcher() {
        }

        /* JADX WARNING: Missing block: B:16:0x002e, code skipped:
            if (r2 == false) goto L_0x0073;
     */
        /* JADX WARNING: Missing block: B:17:0x0030, code skipped:
            r3 = null;
     */
        /* JADX WARNING: Missing block: B:19:?, code skipped:
            r5 = createService(r8);
     */
        /* JADX WARNING: Missing block: B:21:0x0037, code skipped:
            monitor-enter(r0);
     */
        /* JADX WARNING: Missing block: B:23:?, code skipped:
            r0[r7.mCacheIndex] = r5;
            r1[r7.mCacheIndex] = 2;
            r0.notifyAll();
     */
        /* JADX WARNING: Missing block: B:24:0x0043, code skipped:
            monitor-exit(r0);
     */
        /* JADX WARNING: Missing block: B:25:0x0044, code skipped:
            r3 = r5;
            r4 = 2;
     */
        /* JADX WARNING: Missing block: B:30:0x004c, code skipped:
            r5 = move-exception;
     */
        /* JADX WARNING: Missing block: B:32:?, code skipped:
            android.app.SystemServiceRegistry.onServiceNotFound(r5);
     */
        /* JADX WARNING: Missing block: B:33:0x0050, code skipped:
            monitor-enter(r0);
     */
        /* JADX WARNING: Missing block: B:35:?, code skipped:
            r0[r7.mCacheIndex] = null;
            r1[r7.mCacheIndex] = 3;
            r0.notifyAll();
     */
        /* JADX WARNING: Missing block: B:42:0x0062, code skipped:
            monitor-enter(r0);
     */
        /* JADX WARNING: Missing block: B:44:?, code skipped:
            r0[r7.mCacheIndex] = null;
            r1[r7.mCacheIndex] = 3;
            r0.notifyAll();
     */
        /* JADX WARNING: Missing block: B:51:0x0073, code skipped:
            monitor-enter(r0);
     */
        /* JADX WARNING: Missing block: B:54:0x0078, code skipped:
            if (r1[r7.mCacheIndex] >= 2) goto L_0x0090;
     */
        /* JADX WARNING: Missing block: B:56:?, code skipped:
            r0.wait();
     */
        /* JADX WARNING: Missing block: B:60:?, code skipped:
            android.util.Log.w(android.app.SystemServiceRegistry.TAG, "getService() interrupted");
            java.lang.Thread.currentThread().interrupt();
     */
        /* JADX WARNING: Missing block: B:62:0x008f, code skipped:
            return null;
     */
        /* JADX WARNING: Missing block: B:63:0x0090, code skipped:
            monitor-exit(r0);
     */
        public final T getService(android.app.ContextImpl r8) {
            /*
            r7 = this;
            r0 = r8.mServiceCache;
            r1 = r8.mServiceInitializationStateArray;
        L_0x0004:
            r2 = 0;
            monitor-enter(r0);
            r3 = r7.mCacheIndex;	 Catch:{ all -> 0x0098 }
            r3 = r0[r3];	 Catch:{ all -> 0x0098 }
            if (r3 != 0) goto L_0x0096;
        L_0x000c:
            r4 = r7.mCacheIndex;	 Catch:{ all -> 0x0098 }
            r4 = r1[r4];	 Catch:{ all -> 0x0098 }
            r5 = 3;
            if (r4 != r5) goto L_0x0015;
        L_0x0013:
            goto L_0x0096;
        L_0x0015:
            r4 = r7.mCacheIndex;	 Catch:{ all -> 0x0098 }
            r4 = r1[r4];	 Catch:{ all -> 0x0098 }
            r5 = 2;
            if (r4 != r5) goto L_0x0021;
        L_0x001c:
            r4 = r7.mCacheIndex;	 Catch:{ all -> 0x0098 }
            r6 = 0;
            r1[r4] = r6;	 Catch:{ all -> 0x0098 }
        L_0x0021:
            r4 = r7.mCacheIndex;	 Catch:{ all -> 0x0098 }
            r4 = r1[r4];	 Catch:{ all -> 0x0098 }
            if (r4 != 0) goto L_0x002d;
        L_0x0027:
            r2 = 1;
            r4 = r7.mCacheIndex;	 Catch:{ all -> 0x0098 }
            r6 = 1;
            r1[r4] = r6;	 Catch:{ all -> 0x0098 }
        L_0x002d:
            monitor-exit(r0);	 Catch:{ all -> 0x0098 }
            if (r2 == 0) goto L_0x0073;
        L_0x0030:
            r3 = 0;
            r4 = 3;
            r5 = r7.createService(r8);	 Catch:{ ServiceNotFoundException -> 0x004c }
            r6 = 2;
            monitor-enter(r0);
            r3 = r7.mCacheIndex;	 Catch:{ all -> 0x0047 }
            r0[r3] = r5;	 Catch:{ all -> 0x0047 }
            r3 = r7.mCacheIndex;	 Catch:{ all -> 0x0047 }
            r1[r3] = r6;	 Catch:{ all -> 0x0047 }
            r0.notifyAll();	 Catch:{ all -> 0x0047 }
            monitor-exit(r0);	 Catch:{ all -> 0x0047 }
            r3 = r5;
            r4 = r6;
            goto L_0x005e;
        L_0x0047:
            r3 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0047 }
            throw r3;
        L_0x004a:
            r5 = move-exception;
            goto L_0x0062;
        L_0x004c:
            r5 = move-exception;
            android.app.SystemServiceRegistry.onServiceNotFound(r5);	 Catch:{ all -> 0x004a }
            monitor-enter(r0);
            r5 = r7.mCacheIndex;	 Catch:{ all -> 0x005f }
            r0[r5] = r3;	 Catch:{ all -> 0x005f }
            r5 = r7.mCacheIndex;	 Catch:{ all -> 0x005f }
            r1[r5] = r4;	 Catch:{ all -> 0x005f }
            r0.notifyAll();	 Catch:{ all -> 0x005f }
            monitor-exit(r0);	 Catch:{ all -> 0x005f }
        L_0x005e:
            return r3;
        L_0x005f:
            r5 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x005f }
            throw r5;
        L_0x0062:
            monitor-enter(r0);
            r6 = r7.mCacheIndex;	 Catch:{ all -> 0x0070 }
            r0[r6] = r3;	 Catch:{ all -> 0x0070 }
            r6 = r7.mCacheIndex;	 Catch:{ all -> 0x0070 }
            r1[r6] = r4;	 Catch:{ all -> 0x0070 }
            r0.notifyAll();	 Catch:{ all -> 0x0070 }
            monitor-exit(r0);	 Catch:{ all -> 0x0070 }
            throw r5;
        L_0x0070:
            r5 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0070 }
            throw r5;
        L_0x0073:
            monitor-enter(r0);
        L_0x0074:
            r3 = r7.mCacheIndex;	 Catch:{ all -> 0x0093 }
            r3 = r1[r3];	 Catch:{ all -> 0x0093 }
            if (r3 >= r5) goto L_0x0090;
        L_0x007a:
            r0.wait();	 Catch:{ InterruptedException -> 0x007e }
            goto L_0x0074;
        L_0x007e:
            r3 = move-exception;
            r4 = "SystemServiceRegistry";
            r5 = "getService() interrupted";
            android.util.Log.w(r4, r5);	 Catch:{ all -> 0x0093 }
            r4 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0093 }
            r4.interrupt();	 Catch:{ all -> 0x0093 }
            r4 = 0;
            monitor-exit(r0);	 Catch:{ all -> 0x0093 }
            return r4;
        L_0x0090:
            monitor-exit(r0);	 Catch:{ all -> 0x0093 }
            goto L_0x0004;
        L_0x0093:
            r3 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0093 }
            throw r3;
        L_0x0096:
            monitor-exit(r0);	 Catch:{ all -> 0x0098 }
            return r3;
        L_0x0098:
            r3 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0098 }
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.SystemServiceRegistry$CachedServiceFetcher.getService(android.app.ContextImpl):java.lang.Object");
        }
    }

    static abstract class StaticServiceFetcher<T> implements ServiceFetcher<T> {
        private T mCachedInstance;

        public abstract T createService() throws ServiceNotFoundException;

        StaticServiceFetcher() {
        }

        public final T getService(ContextImpl ctx) {
            Object obj;
            synchronized (this) {
                if (this.mCachedInstance == null) {
                    try {
                        this.mCachedInstance = createService();
                    } catch (ServiceNotFoundException e) {
                        SystemServiceRegistry.onServiceNotFound(e);
                    }
                }
                obj = this.mCachedInstance;
            }
            return obj;
        }
    }

    static abstract class StaticApplicationContextServiceFetcher<T> implements ServiceFetcher<T> {
        private T mCachedInstance;

        public abstract T createService(Context context) throws ServiceNotFoundException;

        StaticApplicationContextServiceFetcher() {
        }

        public final T getService(ContextImpl ctx) {
            Object obj;
            synchronized (this) {
                if (this.mCachedInstance == null) {
                    Context appContext = ctx.getApplicationContext();
                    try {
                        this.mCachedInstance = createService(appContext != null ? appContext : ctx);
                    } catch (ServiceNotFoundException e) {
                        SystemServiceRegistry.onServiceNotFound(e);
                    }
                }
                obj = this.mCachedInstance;
            }
            return obj;
        }
    }

    static /* synthetic */ int access$008() {
        int i = sServiceCacheSize;
        sServiceCacheSize = i + 1;
        return i;
    }

    static {
        AnonymousClass1 anonymousClass1 = new CachedServiceFetcher<AccessibilityManager>() {
            public AccessibilityManager createService(ContextImpl ctx) {
                return AccessibilityManager.getInstance(ctx);
            }
        };
        registerService(Context.ACCESSIBILITY_SERVICE, AccessibilityManager.class, anonymousClass1);
        AnonymousClass2 anonymousClass2 = new CachedServiceFetcher<CaptioningManager>() {
            public CaptioningManager createService(ContextImpl ctx) {
                return new CaptioningManager(ctx);
            }
        };
        registerService(Context.CAPTIONING_SERVICE, CaptioningManager.class, anonymousClass2);
        registerService("account", AccountManager.class, new CachedServiceFetcher<AccountManager>() {
            public AccountManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new AccountManager(ctx, Stub.asInterface(ServiceManager.getServiceOrThrow("account")));
            }
        });
        AnonymousClass4 anonymousClass4 = new CachedServiceFetcher<ActivityManager>() {
            public ActivityManager createService(ContextImpl ctx) {
                return new ActivityManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        };
        registerService(Context.ACTIVITY_SERVICE, ActivityManager.class, anonymousClass4);
        AnonymousClass5 anonymousClass5 = new CachedServiceFetcher<ActivityTaskManager>() {
            public ActivityTaskManager createService(ContextImpl ctx) {
                return new ActivityTaskManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        };
        registerService(Context.ACTIVITY_TASK_SERVICE, ActivityTaskManager.class, anonymousClass5);
        AnonymousClass6 anonymousClass6 = new CachedServiceFetcher<UriGrantsManager>() {
            public UriGrantsManager createService(ContextImpl ctx) {
                return new UriGrantsManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        };
        registerService(Context.URI_GRANTS_SERVICE, UriGrantsManager.class, anonymousClass6);
        registerService("alarm", AlarmManager.class, new CachedServiceFetcher<AlarmManager>() {
            public AlarmManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new AlarmManager(IAlarmManager.Stub.asInterface(ServiceManager.getServiceOrThrow("alarm")), ctx);
            }
        });
        registerService("audio", AudioManager.class, new CachedServiceFetcher<AudioManager>() {
            public AudioManager createService(ContextImpl ctx) {
                return new AudioManager(ctx);
            }
        });
        AnonymousClass9 anonymousClass9 = new CachedServiceFetcher<MediaRouter>() {
            public MediaRouter createService(ContextImpl ctx) {
                return new MediaRouter(ctx);
            }
        };
        registerService(Context.MEDIA_ROUTER_SERVICE, MediaRouter.class, anonymousClass9);
        registerService("bluetooth", BluetoothManager.class, new CachedServiceFetcher<BluetoothManager>() {
            public BluetoothManager createService(ContextImpl ctx) {
                return new BluetoothManager(ctx);
            }
        });
        AnonymousClass11 anonymousClass11 = new StaticServiceFetcher<HdmiControlManager>() {
            public HdmiControlManager createService() throws ServiceNotFoundException {
                return new HdmiControlManager(IHdmiControlService.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.HDMI_CONTROL_SERVICE)));
            }
        };
        registerService(Context.HDMI_CONTROL_SERVICE, HdmiControlManager.class, anonymousClass11);
        AnonymousClass12 anonymousClass12 = new CachedServiceFetcher<TextClassificationManager>() {
            public TextClassificationManager createService(ContextImpl ctx) {
                return new TextClassificationManager(ctx);
            }
        };
        registerService(Context.TEXT_CLASSIFICATION_SERVICE, TextClassificationManager.class, anonymousClass12);
        AnonymousClass13 anonymousClass13 = new CachedServiceFetcher<ClipboardManager>() {
            public ClipboardManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new ClipboardManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        };
        String str = Context.CLIPBOARD_SERVICE;
        registerService(str, ClipboardManager.class, anonymousClass13);
        SYSTEM_SERVICE_NAMES.put(android.text.ClipboardManager.class, str);
        registerService("connectivity", ConnectivityManager.class, new StaticApplicationContextServiceFetcher<ConnectivityManager>() {
            public ConnectivityManager createService(Context context) throws ServiceNotFoundException {
                return new ConnectivityManager(context, IConnectivityManager.Stub.asInterface(ServiceManager.getServiceOrThrow("connectivity")));
            }
        });
        AnonymousClass15 anonymousClass15 = new StaticServiceFetcher<IBinder>() {
            public IBinder createService() throws ServiceNotFoundException {
                return ServiceManager.getServiceOrThrow(Context.NETD_SERVICE);
            }
        };
        registerService(Context.NETD_SERVICE, IBinder.class, anonymousClass15);
        AnonymousClass16 anonymousClass16 = new CachedServiceFetcher<IpSecManager>() {
            public IpSecManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new IpSecManager(ctx, IIpSecService.Stub.asInterface(ServiceManager.getService(Context.IPSEC_SERVICE)));
            }
        };
        registerService(Context.IPSEC_SERVICE, IpSecManager.class, anonymousClass16);
        AnonymousClass17 anonymousClass17 = new StaticApplicationContextServiceFetcher<TestNetworkManager>() {
            public TestNetworkManager createService(Context context) throws ServiceNotFoundException {
                try {
                    return new TestNetworkManager(ITestNetworkManager.Stub.asInterface(IConnectivityManager.Stub.asInterface(ServiceManager.getServiceOrThrow("connectivity")).startOrGetTestNetworkService()));
                } catch (RemoteException e) {
                    throw new ServiceNotFoundException(Context.TEST_NETWORK_SERVICE);
                }
            }
        };
        registerService(Context.TEST_NETWORK_SERVICE, TestNetworkManager.class, anonymousClass17);
        AnonymousClass18 anonymousClass18 = new StaticServiceFetcher<CountryDetector>() {
            public CountryDetector createService() throws ServiceNotFoundException {
                return new CountryDetector(ICountryDetector.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.COUNTRY_DETECTOR)));
            }
        };
        registerService(Context.COUNTRY_DETECTOR, CountryDetector.class, anonymousClass18);
        AnonymousClass19 anonymousClass19 = new CachedServiceFetcher<DevicePolicyManager>() {
            public DevicePolicyManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new DevicePolicyManager(ctx, IDevicePolicyManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.DEVICE_POLICY_SERVICE)));
            }
        };
        registerService(Context.DEVICE_POLICY_SERVICE, DevicePolicyManager.class, anonymousClass19);
        AnonymousClass20 anonymousClass20 = new CachedServiceFetcher<DownloadManager>() {
            public DownloadManager createService(ContextImpl ctx) {
                return new DownloadManager(ctx);
            }
        };
        registerService(Context.DOWNLOAD_SERVICE, DownloadManager.class, anonymousClass20);
        AnonymousClass21 anonymousClass21 = new CachedServiceFetcher<BatteryManager>() {
            public BatteryManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new BatteryManager(ctx, IBatteryStats.Stub.asInterface(ServiceManager.getServiceOrThrow(BatteryStats.SERVICE_NAME)), IBatteryPropertiesRegistrar.Stub.asInterface(ServiceManager.getServiceOrThrow("batteryproperties")));
            }
        };
        registerService(Context.BATTERY_SERVICE, BatteryManager.class, anonymousClass21);
        registerService("nfc", NfcManager.class, new CachedServiceFetcher<NfcManager>() {
            public NfcManager createService(ContextImpl ctx) {
                return new NfcManager(ctx);
            }
        });
        AnonymousClass23 anonymousClass23 = new CachedServiceFetcher<DropBoxManager>() {
            public DropBoxManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new DropBoxManager(ctx, IDropBoxManagerService.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.DROPBOX_SERVICE)));
            }
        };
        registerService(Context.DROPBOX_SERVICE, DropBoxManager.class, anonymousClass23);
        registerService("input", InputManager.class, new StaticServiceFetcher<InputManager>() {
            public InputManager createService() {
                return InputManager.getInstance();
            }
        });
        AnonymousClass25 anonymousClass25 = new CachedServiceFetcher<DisplayManager>() {
            public DisplayManager createService(ContextImpl ctx) {
                return new DisplayManager(ctx.getOuterContext());
            }
        };
        registerService(Context.DISPLAY_SERVICE, DisplayManager.class, anonymousClass25);
        AnonymousClass26 anonymousClass26 = new CachedServiceFetcher<ColorDisplayManager>() {
            public ColorDisplayManager createService(ContextImpl ctx) {
                return new ColorDisplayManager();
            }
        };
        registerService(Context.COLOR_DISPLAY_SERVICE, ColorDisplayManager.class, anonymousClass26);
        AnonymousClass27 anonymousClass27 = new ServiceFetcher<InputMethodManager>() {
            public InputMethodManager getService(ContextImpl ctx) {
                return InputMethodManager.forContext(ctx.getOuterContext());
            }
        };
        registerService(Context.INPUT_METHOD_SERVICE, InputMethodManager.class, anonymousClass27);
        AnonymousClass28 anonymousClass28 = new CachedServiceFetcher<TextServicesManager>() {
            public TextServicesManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return TextServicesManager.createInstance(ctx);
            }
        };
        registerService(Context.TEXT_SERVICES_MANAGER_SERVICE, TextServicesManager.class, anonymousClass28);
        AnonymousClass29 anonymousClass29 = new CachedServiceFetcher<KeyguardManager>() {
            public KeyguardManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new KeyguardManager(ctx);
            }
        };
        registerService(Context.KEYGUARD_SERVICE, KeyguardManager.class, anonymousClass29);
        AnonymousClass30 anonymousClass30 = new CachedServiceFetcher<LayoutInflater>() {
            public LayoutInflater createService(ContextImpl ctx) {
                return new PhoneLayoutInflater(ctx.getOuterContext());
            }
        };
        registerService(Context.LAYOUT_INFLATER_SERVICE, LayoutInflater.class, anonymousClass30);
        registerService("location", LocationManager.class, new CachedServiceFetcher<LocationManager>() {
            public LocationManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new LocationManager(ctx, ILocationManager.Stub.asInterface(ServiceManager.getServiceOrThrow("location")));
            }
        });
        AnonymousClass32 anonymousClass32 = new CachedServiceFetcher<NetworkPolicyManager>() {
            public NetworkPolicyManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new NetworkPolicyManager(ctx, INetworkPolicyManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.NETWORK_POLICY_SERVICE)));
            }
        };
        registerService(Context.NETWORK_POLICY_SERVICE, NetworkPolicyManager.class, anonymousClass32);
        registerService("notification", NotificationManager.class, new CachedServiceFetcher<NotificationManager>() {
            public NotificationManager createService(ContextImpl ctx) {
                Context outerContext = ctx.getOuterContext();
                return new NotificationManager(new ContextThemeWrapper(outerContext, Resources.selectSystemTheme(0, outerContext.getApplicationInfo().targetSdkVersion, 16973835, 16973935, 16974126, 16974130)), ctx.mMainThread.getHandler());
            }
        });
        AnonymousClass34 anonymousClass34 = new CachedServiceFetcher<NsdManager>() {
            public NsdManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new NsdManager(ctx.getOuterContext(), INsdManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.NSD_SERVICE)));
            }
        };
        registerService(Context.NSD_SERVICE, NsdManager.class, anonymousClass34);
        AnonymousClass35 anonymousClass35 = new CachedServiceFetcher<PowerManager>() {
            public PowerManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new PowerManager(ctx.getOuterContext(), IPowerManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.POWER_SERVICE)), ctx.mMainThread.getHandler());
            }
        };
        registerService(Context.POWER_SERVICE, PowerManager.class, anonymousClass35);
        registerService("recovery", RecoverySystem.class, new CachedServiceFetcher<RecoverySystem>() {
            public RecoverySystem createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new RecoverySystem(IRecoverySystem.Stub.asInterface(ServiceManager.getServiceOrThrow("recovery")));
            }
        });
        registerService("search", SearchManager.class, new CachedServiceFetcher<SearchManager>() {
            public SearchManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new SearchManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        AnonymousClass38 anonymousClass38 = new CachedServiceFetcher<SensorManager>() {
            public SensorManager createService(ContextImpl ctx) {
                return new SystemSensorManager(ctx.getOuterContext(), ctx.mMainThread.getHandler().getLooper());
            }
        };
        registerService(Context.SENSOR_SERVICE, SensorManager.class, anonymousClass38);
        AnonymousClass39 anonymousClass39 = new CachedServiceFetcher<SensorPrivacyManager>() {
            public SensorPrivacyManager createService(ContextImpl ctx) {
                return SensorPrivacyManager.getInstance(ctx);
            }
        };
        registerService(Context.SENSOR_PRIVACY_SERVICE, SensorPrivacyManager.class, anonymousClass39);
        AnonymousClass40 anonymousClass40 = new CachedServiceFetcher<StatsManager>() {
            public StatsManager createService(ContextImpl ctx) {
                return new StatsManager(ctx.getOuterContext());
            }
        };
        registerService(Context.STATS_MANAGER, StatsManager.class, anonymousClass40);
        AnonymousClass41 anonymousClass41 = new CachedServiceFetcher<StatusBarManager>() {
            public StatusBarManager createService(ContextImpl ctx) {
                return new StatusBarManager(ctx.getOuterContext());
            }
        };
        registerService(Context.STATUS_BAR_SERVICE, StatusBarManager.class, anonymousClass41);
        registerService("storage", StorageManager.class, new CachedServiceFetcher<StorageManager>() {
            public StorageManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new StorageManager(ctx, ctx.mMainThread.getHandler().getLooper());
            }
        });
        AnonymousClass43 anonymousClass43 = new CachedServiceFetcher<StorageStatsManager>() {
            public StorageStatsManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new StorageStatsManager(ctx, IStorageStatsManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.STORAGE_STATS_SERVICE)));
            }
        };
        registerService(Context.STORAGE_STATS_SERVICE, StorageStatsManager.class, anonymousClass43);
        AnonymousClass44 anonymousClass44 = new CachedServiceFetcher<SystemUpdateManager>() {
            public SystemUpdateManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new SystemUpdateManager(ISystemUpdateManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.SYSTEM_UPDATE_SERVICE)));
            }
        };
        registerService(Context.SYSTEM_UPDATE_SERVICE, SystemUpdateManager.class, anonymousClass44);
        registerService("phone", TelephonyManager.class, new CachedServiceFetcher<TelephonyManager>() {
            public TelephonyManager createService(ContextImpl ctx) {
                return new TelephonyManager(ctx.getOuterContext());
            }
        });
        AnonymousClass46 anonymousClass46 = new CachedServiceFetcher<SubscriptionManager>() {
            public SubscriptionManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new SubscriptionManager(ctx.getOuterContext());
            }
        };
        registerService(Context.TELEPHONY_SUBSCRIPTION_SERVICE, SubscriptionManager.class, anonymousClass46);
        AnonymousClass47 anonymousClass47 = new CachedServiceFetcher<RcsManager>() {
            public RcsManager createService(ContextImpl ctx) {
                return new RcsManager(ctx.getOuterContext());
            }
        };
        registerService(Context.TELEPHONY_RCS_SERVICE, RcsManager.class, anonymousClass47);
        AnonymousClass48 anonymousClass48 = new CachedServiceFetcher<CarrierConfigManager>() {
            public CarrierConfigManager createService(ContextImpl ctx) {
                return new CarrierConfigManager(ctx.getOuterContext());
            }
        };
        registerService(Context.CARRIER_CONFIG_SERVICE, CarrierConfigManager.class, anonymousClass48);
        AnonymousClass49 anonymousClass49 = new CachedServiceFetcher<TelecomManager>() {
            public TelecomManager createService(ContextImpl ctx) {
                return new TelecomManager(ctx.getOuterContext());
            }
        };
        registerService(Context.TELECOM_SERVICE, TelecomManager.class, anonymousClass49);
        AnonymousClass50 anonymousClass50 = new CachedServiceFetcher<EuiccManager>() {
            public EuiccManager createService(ContextImpl ctx) {
                return new EuiccManager(ctx.getOuterContext());
            }
        };
        registerService(Context.EUICC_SERVICE, EuiccManager.class, anonymousClass50);
        AnonymousClass51 anonymousClass51 = new CachedServiceFetcher<EuiccCardManager>() {
            public EuiccCardManager createService(ContextImpl ctx) {
                return new EuiccCardManager(ctx.getOuterContext());
            }
        };
        registerService(Context.EUICC_CARD_SERVICE, EuiccCardManager.class, anonymousClass51);
        AnonymousClass52 anonymousClass52 = new CachedServiceFetcher<UiModeManager>() {
            public UiModeManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new UiModeManager();
            }
        };
        registerService(Context.UI_MODE_SERVICE, UiModeManager.class, anonymousClass52);
        AnonymousClass53 anonymousClass53 = new CachedServiceFetcher<UsbManager>() {
            public UsbManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new UsbManager(ctx, IUsbManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.USB_SERVICE)));
            }
        };
        registerService(Context.USB_SERVICE, UsbManager.class, anonymousClass53);
        registerService("adb", AdbManager.class, new CachedServiceFetcher<AdbManager>() {
            public AdbManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new AdbManager(ctx, IAdbManager.Stub.asInterface(ServiceManager.getServiceOrThrow("adb")));
            }
        });
        AnonymousClass55 anonymousClass55 = new CachedServiceFetcher<SerialManager>() {
            public SerialManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new SerialManager(ctx, ISerialManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.SERIAL_SERVICE)));
            }
        };
        registerService(Context.SERIAL_SERVICE, SerialManager.class, anonymousClass55);
        AnonymousClass56 anonymousClass56 = new CachedServiceFetcher<Vibrator>() {
            public Vibrator createService(ContextImpl ctx) {
                return new SystemVibrator(ctx);
            }
        };
        registerService(Context.VIBRATOR_SERVICE, Vibrator.class, anonymousClass56);
        AnonymousClass57 anonymousClass57 = new CachedServiceFetcher<WallpaperManager>() {
            public WallpaperManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                IBinder b;
                int i = ctx.getApplicationInfo().targetSdkVersion;
                String str = Context.WALLPAPER_SERVICE;
                if (i >= 28) {
                    b = ServiceManager.getServiceOrThrow(str);
                } else {
                    b = ServiceManager.getService(str);
                }
                return new WallpaperManager(IWallpaperManager.Stub.asInterface(b), ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        };
        registerService(Context.WALLPAPER_SERVICE, WallpaperManager.class, anonymousClass57);
        registerService("lowpan", LowpanManager.class, new CachedServiceFetcher<LowpanManager>() {
            public LowpanManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new LowpanManager(ctx.getOuterContext(), ILowpanManager.Stub.asInterface(ServiceManager.getServiceOrThrow("lowpan")), ConnectivityThread.getInstanceLooper());
            }
        });
        registerService("wifi", WifiManager.class, new CachedServiceFetcher<WifiManager>() {
            public WifiManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new WifiManager(ctx.getOuterContext(), IWifiManager.Stub.asInterface(ServiceManager.getServiceOrThrow("wifi")), ConnectivityThread.getInstanceLooper());
            }
        });
        AnonymousClass60 anonymousClass60 = new StaticServiceFetcher<WifiP2pManager>() {
            public WifiP2pManager createService() throws ServiceNotFoundException {
                return new WifiP2pManager(IWifiP2pManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.WIFI_P2P_SERVICE)));
            }
        };
        registerService(Context.WIFI_P2P_SERVICE, WifiP2pManager.class, anonymousClass60);
        AnonymousClass61 anonymousClass61 = new CachedServiceFetcher<WifiAwareManager>() {
            public WifiAwareManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                IWifiAwareManager service = IWifiAwareManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.WIFI_AWARE_SERVICE));
                if (service == null) {
                    return null;
                }
                return new WifiAwareManager(ctx.getOuterContext(), service);
            }
        };
        registerService(Context.WIFI_AWARE_SERVICE, WifiAwareManager.class, anonymousClass61);
        AnonymousClass62 anonymousClass62 = new CachedServiceFetcher<WifiScanner>() {
            public WifiScanner createService(ContextImpl ctx) throws ServiceNotFoundException {
                IWifiScanner service = IWifiScanner.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.WIFI_SCANNING_SERVICE));
                if (service == null) {
                    return null;
                }
                return new WifiScanner(ctx.getOuterContext(), service, ConnectivityThread.getInstanceLooper());
            }
        };
        registerService(Context.WIFI_SCANNING_SERVICE, WifiScanner.class, anonymousClass62);
        AnonymousClass63 anonymousClass63 = new CachedServiceFetcher<RttManager>() {
            public RttManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new RttManager(ctx.getOuterContext(), new WifiRttManager(ctx.getOuterContext(), IWifiRttManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.WIFI_RTT_RANGING_SERVICE))));
            }
        };
        registerService(Context.WIFI_RTT_SERVICE, RttManager.class, anonymousClass63);
        AnonymousClass64 anonymousClass64 = new CachedServiceFetcher<WifiRttManager>() {
            public WifiRttManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new WifiRttManager(ctx.getOuterContext(), IWifiRttManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.WIFI_RTT_RANGING_SERVICE)));
            }
        };
        registerService(Context.WIFI_RTT_RANGING_SERVICE, WifiRttManager.class, anonymousClass64);
        AnonymousClass65 anonymousClass65 = new CachedServiceFetcher<EthernetManager>() {
            public EthernetManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new EthernetManager(ctx.getOuterContext(), IEthernetManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.ETHERNET_SERVICE)));
            }
        };
        registerService(Context.ETHERNET_SERVICE, EthernetManager.class, anonymousClass65);
        AnonymousClass66 anonymousClass66 = new CachedServiceFetcher<WindowManager>() {
            public WindowManager createService(ContextImpl ctx) {
                return new WindowManagerImpl(ctx);
            }
        };
        registerService(Context.WINDOW_SERVICE, WindowManager.class, anonymousClass66);
        registerService("user", UserManager.class, new CachedServiceFetcher<UserManager>() {
            public UserManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new UserManager(ctx, IUserManager.Stub.asInterface(ServiceManager.getServiceOrThrow("user")));
            }
        });
        AnonymousClass68 anonymousClass68 = new CachedServiceFetcher<AppOpsManager>() {
            public AppOpsManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new AppOpsManager(ctx, IAppOpsService.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.APP_OPS_SERVICE)));
            }
        };
        registerService(Context.APP_OPS_SERVICE, AppOpsManager.class, anonymousClass68);
        AnonymousClass69 anonymousClass69 = new CachedServiceFetcher<BsGamePadManager>() {
            public BsGamePadManager createService(ContextImpl ctx) {
                return new BsGamePadManager(ctx, IBsGamePadService.Stub.asInterface(ServiceManager.getService(Context.GAME_PAD_SERVICE)));
            }
        };
        registerService(Context.GAME_PAD_SERVICE, BsGamePadManager.class, anonymousClass69);
        AnonymousClass70 anonymousClass70 = new CachedServiceFetcher<CameraManager>() {
            public CameraManager createService(ContextImpl ctx) {
                return new CameraManager(ctx);
            }
        };
        registerService(Context.CAMERA_SERVICE, CameraManager.class, anonymousClass70);
        AnonymousClass71 anonymousClass71 = new CachedServiceFetcher<LauncherApps>() {
            public LauncherApps createService(ContextImpl ctx) {
                return new LauncherApps(ctx);
            }
        };
        registerService(Context.LAUNCHER_APPS_SERVICE, LauncherApps.class, anonymousClass71);
        AnonymousClass72 anonymousClass72 = new CachedServiceFetcher<RestrictionsManager>() {
            public RestrictionsManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new RestrictionsManager(ctx, IRestrictionsManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.RESTRICTIONS_SERVICE)));
            }
        };
        registerService(Context.RESTRICTIONS_SERVICE, RestrictionsManager.class, anonymousClass72);
        AnonymousClass73 anonymousClass73 = new CachedServiceFetcher<PrintManager>() {
            public PrintManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                IPrintManager service = null;
                if (ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_PRINTING)) {
                    service = IPrintManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.PRINT_SERVICE));
                }
                return new PrintManager(ctx.getOuterContext(), service, ctx.getUserId(), UserHandle.getAppId(ctx.getApplicationInfo().uid));
            }
        };
        registerService(Context.PRINT_SERVICE, PrintManager.class, anonymousClass73);
        AnonymousClass74 anonymousClass74 = new CachedServiceFetcher<CompanionDeviceManager>() {
            public CompanionDeviceManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                ICompanionDeviceManager service = null;
                if (ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_COMPANION_DEVICE_SETUP)) {
                    service = ICompanionDeviceManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.COMPANION_DEVICE_SERVICE));
                }
                return new CompanionDeviceManager(service, ctx.getOuterContext());
            }
        };
        registerService(Context.COMPANION_DEVICE_SERVICE, CompanionDeviceManager.class, anonymousClass74);
        AnonymousClass75 anonymousClass75 = new CachedServiceFetcher<ConsumerIrManager>() {
            public ConsumerIrManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new ConsumerIrManager(ctx);
            }
        };
        registerService(Context.CONSUMER_IR_SERVICE, ConsumerIrManager.class, anonymousClass75);
        AnonymousClass76 anonymousClass76 = new CachedServiceFetcher<MediaSessionManager>() {
            public MediaSessionManager createService(ContextImpl ctx) {
                return new MediaSessionManager(ctx);
            }
        };
        registerService(Context.MEDIA_SESSION_SERVICE, MediaSessionManager.class, anonymousClass76);
        AnonymousClass77 anonymousClass77 = new StaticServiceFetcher<TrustManager>() {
            public TrustManager createService() throws ServiceNotFoundException {
                return new TrustManager(ServiceManager.getServiceOrThrow(Context.TRUST_SERVICE));
            }
        };
        registerService(Context.TRUST_SERVICE, TrustManager.class, anonymousClass77);
        AnonymousClass78 anonymousClass78 = new CachedServiceFetcher<FingerprintManager>() {
            public FingerprintManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                IBinder binder;
                int i = ctx.getApplicationInfo().targetSdkVersion;
                String str = Context.FINGERPRINT_SERVICE;
                if (i >= 26) {
                    binder = ServiceManager.getServiceOrThrow(str);
                } else {
                    binder = ServiceManager.getService(str);
                }
                return new FingerprintManager(ctx.getOuterContext(), IFingerprintService.Stub.asInterface(binder));
            }
        };
        registerService(Context.FINGERPRINT_SERVICE, FingerprintManager.class, anonymousClass78);
        AnonymousClass79 anonymousClass79 = new CachedServiceFetcher<FaceManager>() {
            public FaceManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                IBinder binder;
                int i = ctx.getApplicationInfo().targetSdkVersion;
                String str = Context.FACE_SERVICE;
                if (i >= 26) {
                    binder = ServiceManager.getServiceOrThrow(str);
                } else {
                    binder = ServiceManager.getService(str);
                }
                return new FaceManager(ctx.getOuterContext(), IFaceService.Stub.asInterface(binder));
            }
        };
        registerService(Context.FACE_SERVICE, FaceManager.class, anonymousClass79);
        AnonymousClass80 anonymousClass80 = new CachedServiceFetcher<IrisManager>() {
            public IrisManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new IrisManager(ctx.getOuterContext(), IIrisService.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.IRIS_SERVICE)));
            }
        };
        registerService(Context.IRIS_SERVICE, IrisManager.class, anonymousClass80);
        AnonymousClass81 anonymousClass81 = new CachedServiceFetcher<BiometricManager>() {
            public BiometricManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                if (!BiometricManager.hasBiometrics(ctx)) {
                    return new BiometricManager(ctx.getOuterContext(), null);
                }
                return new BiometricManager(ctx.getOuterContext(), IBiometricService.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.BIOMETRIC_SERVICE)));
            }
        };
        registerService(Context.BIOMETRIC_SERVICE, BiometricManager.class, anonymousClass81);
        AnonymousClass82 anonymousClass82 = new CachedServiceFetcher<TvInputManager>() {
            public TvInputManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new TvInputManager(ITvInputManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.TV_INPUT_SERVICE)), ctx.getUserId());
            }
        };
        registerService(Context.TV_INPUT_SERVICE, TvInputManager.class, anonymousClass82);
        AnonymousClass83 anonymousClass83 = new CachedServiceFetcher<NetworkScoreManager>() {
            public NetworkScoreManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new NetworkScoreManager(ctx);
            }
        };
        registerService(Context.NETWORK_SCORE_SERVICE, NetworkScoreManager.class, anonymousClass83);
        AnonymousClass84 anonymousClass84 = new CachedServiceFetcher<UsageStatsManager>() {
            public UsageStatsManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new UsageStatsManager(ctx.getOuterContext(), IUsageStatsManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.USAGE_STATS_SERVICE)));
            }
        };
        registerService(Context.USAGE_STATS_SERVICE, UsageStatsManager.class, anonymousClass84);
        AnonymousClass85 anonymousClass85 = new CachedServiceFetcher<NetworkStatsManager>() {
            public NetworkStatsManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new NetworkStatsManager(ctx.getOuterContext());
            }
        };
        registerService(Context.NETWORK_STATS_SERVICE, NetworkStatsManager.class, anonymousClass85);
        AnonymousClass86 anonymousClass86 = new StaticServiceFetcher<JobScheduler>() {
            public JobScheduler createService() throws ServiceNotFoundException {
                return new JobSchedulerImpl(IJobScheduler.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.JOB_SCHEDULER_SERVICE)));
            }
        };
        registerService(Context.JOB_SCHEDULER_SERVICE, JobScheduler.class, anonymousClass86);
        AnonymousClass87 anonymousClass87 = new StaticServiceFetcher<PersistentDataBlockManager>() {
            public PersistentDataBlockManager createService() throws ServiceNotFoundException {
                IPersistentDataBlockService persistentDataBlockService = IPersistentDataBlockService.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.PERSISTENT_DATA_BLOCK_SERVICE));
                if (persistentDataBlockService != null) {
                    return new PersistentDataBlockManager(persistentDataBlockService);
                }
                return null;
            }
        };
        registerService(Context.PERSISTENT_DATA_BLOCK_SERVICE, PersistentDataBlockManager.class, anonymousClass87);
        AnonymousClass88 anonymousClass88 = new StaticServiceFetcher<OemLockManager>() {
            public OemLockManager createService() throws ServiceNotFoundException {
                IOemLockService oemLockService = IOemLockService.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.OEM_LOCK_SERVICE));
                if (oemLockService != null) {
                    return new OemLockManager(oemLockService);
                }
                return null;
            }
        };
        registerService(Context.OEM_LOCK_SERVICE, OemLockManager.class, anonymousClass88);
        AnonymousClass89 anonymousClass89 = new CachedServiceFetcher<MediaProjectionManager>() {
            public MediaProjectionManager createService(ContextImpl ctx) {
                return new MediaProjectionManager(ctx);
            }
        };
        registerService(Context.MEDIA_PROJECTION_SERVICE, MediaProjectionManager.class, anonymousClass89);
        AnonymousClass90 anonymousClass90 = new CachedServiceFetcher<AppWidgetManager>() {
            public AppWidgetManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new AppWidgetManager(ctx, IAppWidgetService.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.APPWIDGET_SERVICE)));
            }
        };
        registerService(Context.APPWIDGET_SERVICE, AppWidgetManager.class, anonymousClass90);
        registerService("midi", MidiManager.class, new CachedServiceFetcher<MidiManager>() {
            public MidiManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new MidiManager(IMidiManager.Stub.asInterface(ServiceManager.getServiceOrThrow("midi")));
            }
        });
        AnonymousClass92 anonymousClass92 = new CachedServiceFetcher<RadioManager>() {
            public RadioManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new RadioManager(ctx);
            }
        };
        registerService(Context.RADIO_SERVICE, RadioManager.class, anonymousClass92);
        AnonymousClass93 anonymousClass93 = new CachedServiceFetcher<HardwarePropertiesManager>() {
            public HardwarePropertiesManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new HardwarePropertiesManager(ctx, IHardwarePropertiesManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.HARDWARE_PROPERTIES_SERVICE)));
            }
        };
        registerService(Context.HARDWARE_PROPERTIES_SERVICE, HardwarePropertiesManager.class, anonymousClass93);
        AnonymousClass94 anonymousClass94 = new CachedServiceFetcher<SoundTriggerManager>() {
            public SoundTriggerManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new SoundTriggerManager(ctx, ISoundTriggerService.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.SOUND_TRIGGER_SERVICE)));
            }
        };
        registerService(Context.SOUND_TRIGGER_SERVICE, SoundTriggerManager.class, anonymousClass94);
        registerService("shortcut", ShortcutManager.class, new CachedServiceFetcher<ShortcutManager>() {
            public ShortcutManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new ShortcutManager(ctx, IShortcutService.Stub.asInterface(ServiceManager.getServiceOrThrow("shortcut")));
            }
        });
        AnonymousClass96 anonymousClass96 = new CachedServiceFetcher<OverlayManager>() {
            public OverlayManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new OverlayManager(ctx, IOverlayManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.OVERLAY_SERVICE)));
            }
        };
        registerService(Context.OVERLAY_SERVICE, OverlayManager.class, anonymousClass96);
        AnonymousClass97 anonymousClass97 = new CachedServiceFetcher<NetworkWatchlistManager>() {
            public NetworkWatchlistManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new NetworkWatchlistManager(ctx, INetworkWatchlistManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.NETWORK_WATCHLIST_SERVICE)));
            }
        };
        registerService(Context.NETWORK_WATCHLIST_SERVICE, NetworkWatchlistManager.class, anonymousClass97);
        AnonymousClass98 anonymousClass98 = new CachedServiceFetcher<SystemHealthManager>() {
            public SystemHealthManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new SystemHealthManager(IBatteryStats.Stub.asInterface(ServiceManager.getServiceOrThrow(BatteryStats.SERVICE_NAME)));
            }
        };
        registerService(Context.SYSTEM_HEALTH_SERVICE, SystemHealthManager.class, anonymousClass98);
        AnonymousClass99 anonymousClass99 = new CachedServiceFetcher<ContextHubManager>() {
            public ContextHubManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new ContextHubManager(ctx.getOuterContext(), ctx.mMainThread.getHandler().getLooper());
            }
        };
        registerService(Context.CONTEXTHUB_SERVICE, ContextHubManager.class, anonymousClass99);
        AnonymousClass100 anonymousClass100 = new CachedServiceFetcher<IncidentManager>() {
            public IncidentManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new IncidentManager(ctx);
            }
        };
        registerService(Context.INCIDENT_SERVICE, IncidentManager.class, anonymousClass100);
        AnonymousClass101 anonymousClass101 = new CachedServiceFetcher<BugreportManager>() {
            public BugreportManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new BugreportManager(ctx.getOuterContext(), IDumpstate.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.BUGREPORT_SERVICE)));
            }
        };
        registerService(Context.BUGREPORT_SERVICE, BugreportManager.class, anonymousClass101);
        registerService("autofill", AutofillManager.class, new CachedServiceFetcher<AutofillManager>() {
            public AutofillManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new AutofillManager(ctx.getOuterContext(), IAutoFillManager.Stub.asInterface(ServiceManager.getService("autofill")));
            }
        });
        registerService("content_capture", ContentCaptureManager.class, new CachedServiceFetcher<ContentCaptureManager>() {
            public ContentCaptureManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                Context outerContext = ctx.getOuterContext();
                ContentCaptureOptions options = outerContext.getContentCaptureOptions();
                if (options != null && (options.lite || options.isWhitelisted(outerContext))) {
                    IContentCaptureManager service = IContentCaptureManager.Stub.asInterface(ServiceManager.getService("content_capture"));
                    if (service != null) {
                        return new ContentCaptureManager(outerContext, service, options);
                    }
                }
                return null;
            }
        });
        AnonymousClass104 anonymousClass104 = new CachedServiceFetcher<AppPredictionManager>() {
            public AppPredictionManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new AppPredictionManager(ctx);
            }
        };
        registerService(Context.APP_PREDICTION_SERVICE, AppPredictionManager.class, anonymousClass104);
        AnonymousClass105 anonymousClass105 = new CachedServiceFetcher<ContentSuggestionsManager>() {
            public ContentSuggestionsManager createService(ContextImpl ctx) {
                return new ContentSuggestionsManager(ctx.getUserId(), IContentSuggestionsManager.Stub.asInterface(ServiceManager.getService(Context.CONTENT_SUGGESTIONS_SERVICE)));
            }
        };
        registerService(Context.CONTENT_SUGGESTIONS_SERVICE, ContentSuggestionsManager.class, anonymousClass105);
        AnonymousClass106 anonymousClass106 = new CachedServiceFetcher<VrManager>() {
            public VrManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new VrManager(IVrManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.VR_SERVICE)));
            }
        };
        registerService(Context.VR_SERVICE, VrManager.class, anonymousClass106);
        AnonymousClass107 anonymousClass107 = new CachedServiceFetcher<RulesManager>() {
            public RulesManager createService(ContextImpl ctx) {
                return new RulesManager(ctx.getOuterContext());
            }
        };
        registerService(Context.TIME_ZONE_RULES_MANAGER_SERVICE, RulesManager.class, anonymousClass107);
        AnonymousClass108 anonymousClass108 = new CachedServiceFetcher<CrossProfileApps>() {
            public CrossProfileApps createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new CrossProfileApps(ctx.getOuterContext(), ICrossProfileApps.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.CROSS_PROFILE_APPS_SERVICE)));
            }
        };
        registerService(Context.CROSS_PROFILE_APPS_SERVICE, CrossProfileApps.class, anonymousClass108);
        registerService("slice", SliceManager.class, new CachedServiceFetcher<SliceManager>() {
            public SliceManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new SliceManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        AnonymousClass110 anonymousClass110 = new CachedServiceFetcher<DeviceIdleManager>() {
            public DeviceIdleManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new DeviceIdleManager(ctx.getOuterContext(), IDeviceIdleController.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.DEVICE_IDLE_CONTROLLER)));
            }
        };
        registerService(Context.DEVICE_IDLE_CONTROLLER, DeviceIdleManager.class, anonymousClass110);
        AnonymousClass111 anonymousClass111 = new CachedServiceFetcher<TimeDetector>() {
            public TimeDetector createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new TimeDetector();
            }
        };
        registerService(Context.TIME_DETECTOR_SERVICE, TimeDetector.class, anonymousClass111);
        registerService("permission", PermissionManager.class, new CachedServiceFetcher<PermissionManager>() {
            public PermissionManager createService(ContextImpl ctx) {
                return new PermissionManager(ctx.getOuterContext(), AppGlobals.getPackageManager());
            }
        });
        AnonymousClass113 anonymousClass113 = new CachedServiceFetcher<PermissionControllerManager>() {
            public PermissionControllerManager createService(ContextImpl ctx) {
                return new PermissionControllerManager(ctx.getOuterContext(), ctx.getMainThreadHandler());
            }
        };
        registerService(Context.PERMISSION_CONTROLLER_SERVICE, PermissionControllerManager.class, anonymousClass113);
        AnonymousClass114 anonymousClass114 = new CachedServiceFetcher<RoleManager>() {
            public RoleManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new RoleManager(ctx.getOuterContext());
            }
        };
        registerService(Context.ROLE_SERVICE, RoleManager.class, anonymousClass114);
        AnonymousClass115 anonymousClass115 = new CachedServiceFetcher<RoleControllerManager>() {
            public RoleControllerManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new RoleControllerManager(ctx.getOuterContext());
            }
        };
        registerService(Context.ROLE_CONTROLLER_SERVICE, RoleControllerManager.class, anonymousClass115);
        registerService("rollback", RollbackManager.class, new CachedServiceFetcher<RollbackManager>() {
            public RollbackManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new RollbackManager(ctx.getOuterContext(), IRollbackManager.Stub.asInterface(ServiceManager.getServiceOrThrow("rollback")));
            }
        });
        AnonymousClass117 anonymousClass117 = new CachedServiceFetcher<DynamicSystemManager>() {
            public DynamicSystemManager createService(ContextImpl ctx) throws ServiceNotFoundException {
                return new DynamicSystemManager(IDynamicSystemService.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.DYNAMIC_SYSTEM_SERVICE)));
            }
        };
        registerService(Context.DYNAMIC_SYSTEM_SERVICE, DynamicSystemManager.class, anonymousClass117);
        ContextImplInjector.registerMiuiServices();
    }

    private SystemServiceRegistry() {
    }

    public static Object[] createServiceCache() {
        return new Object[sServiceCacheSize];
    }

    public static Object getSystemService(ContextImpl ctx, String name) {
        ServiceFetcher<?> fetcher = (ServiceFetcher) SYSTEM_SERVICE_FETCHERS.get(name);
        return fetcher != null ? fetcher.getService(ctx) : null;
    }

    public static String getSystemServiceName(Class<?> serviceClass) {
        return (String) SYSTEM_SERVICE_NAMES.get(serviceClass);
    }

    static <T> void registerService(String serviceName, Class<T> serviceClass, ServiceFetcher<T> serviceFetcher) {
        SYSTEM_SERVICE_NAMES.put(serviceClass, serviceName);
        SYSTEM_SERVICE_FETCHERS.put(serviceName, serviceFetcher);
    }

    public static void onServiceNotFound(ServiceNotFoundException e) {
        int myUid = Process.myUid();
        String str = TAG;
        if (myUid < 10000) {
            Log.wtf(str, e.getMessage(), e);
        } else {
            Log.w(str, e.getMessage());
        }
    }
}
