package android.hardware.location;

import android.Manifest.permission;
import android.content.Context;
import android.location.IFusedGeofenceHardware;
import android.location.IGpsGeofenceHardware;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.IInterface;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Iterator;

public final class GeofenceHardwareImpl {
    private static final int ADD_GEOFENCE_CALLBACK = 2;
    private static final int CALLBACK_ADD = 2;
    private static final int CALLBACK_REMOVE = 3;
    private static final int CAPABILITY_GNSS = 1;
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final int FIRST_VERSION_WITH_CAPABILITIES = 2;
    private static final int GEOFENCE_CALLBACK_BINDER_DIED = 6;
    private static final int GEOFENCE_STATUS = 1;
    private static final int GEOFENCE_TRANSITION_CALLBACK = 1;
    private static final int LOCATION_HAS_ACCURACY = 16;
    private static final int LOCATION_HAS_ALTITUDE = 2;
    private static final int LOCATION_HAS_BEARING = 8;
    private static final int LOCATION_HAS_LAT_LONG = 1;
    private static final int LOCATION_HAS_SPEED = 4;
    private static final int LOCATION_INVALID = 0;
    private static final int MONITOR_CALLBACK_BINDER_DIED = 4;
    private static final int PAUSE_GEOFENCE_CALLBACK = 4;
    private static final int REAPER_GEOFENCE_ADDED = 1;
    private static final int REAPER_MONITOR_CALLBACK_ADDED = 2;
    private static final int REAPER_REMOVED = 3;
    private static final int REMOVE_GEOFENCE_CALLBACK = 3;
    private static final int RESOLUTION_LEVEL_COARSE = 2;
    private static final int RESOLUTION_LEVEL_FINE = 3;
    private static final int RESOLUTION_LEVEL_NONE = 1;
    private static final int RESUME_GEOFENCE_CALLBACK = 5;
    private static final String TAG = "GeofenceHardwareImpl";
    private static GeofenceHardwareImpl sInstance;
    private final ArrayList<IGeofenceHardwareMonitorCallback>[] mCallbacks = new ArrayList[2];
    private Handler mCallbacksHandler = new Handler() {
        public void handleMessage(Message msg) {
            int i = msg.what;
            String str = GeofenceHardwareImpl.TAG;
            ArrayList<IGeofenceHardwareMonitorCallback> callbackList;
            IGeofenceHardwareMonitorCallback callback;
            ArrayList<IGeofenceHardwareMonitorCallback> callbackList2;
            if (i == 1) {
                GeofenceHardwareMonitorEvent event = msg.obj;
                callbackList = GeofenceHardwareImpl.this.mCallbacks[event.getMonitoringType()];
                if (callbackList != null) {
                    if (GeofenceHardwareImpl.DEBUG) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("MonitoringSystemChangeCallback: ");
                        stringBuilder.append(event);
                        Log.d(str, stringBuilder.toString());
                    }
                    Iterator it = callbackList.iterator();
                    while (it.hasNext()) {
                        try {
                            ((IGeofenceHardwareMonitorCallback) it.next()).onMonitoringSystemChange(event);
                        } catch (RemoteException e) {
                            Log.d(str, "Error reporting onMonitoringSystemChange.", e);
                        }
                    }
                }
                GeofenceHardwareImpl.this.releaseWakeLock();
            } else if (i == 2) {
                i = msg.arg1;
                callback = (IGeofenceHardwareMonitorCallback) msg.obj;
                callbackList2 = GeofenceHardwareImpl.this.mCallbacks[i];
                if (callbackList2 == null) {
                    callbackList2 = new ArrayList();
                    GeofenceHardwareImpl.this.mCallbacks[i] = callbackList2;
                }
                if (!callbackList2.contains(callback)) {
                    callbackList2.add(callback);
                }
            } else if (i == 3) {
                callback = msg.obj;
                callbackList2 = GeofenceHardwareImpl.this.mCallbacks[msg.arg1];
                if (callbackList2 != null) {
                    callbackList2.remove(callback);
                }
            } else if (i == 4) {
                IGeofenceHardwareMonitorCallback callback2 = msg.obj;
                if (GeofenceHardwareImpl.DEBUG) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Monitor callback reaped:");
                    stringBuilder2.append(callback2);
                    Log.d(str, stringBuilder2.toString());
                }
                callbackList = GeofenceHardwareImpl.this.mCallbacks[msg.arg1];
                if (callbackList != null && callbackList.contains(callback2)) {
                    callbackList.remove(callback2);
                }
            }
        }
    };
    private int mCapabilities;
    private final Context mContext;
    private IFusedGeofenceHardware mFusedService;
    private Handler mGeofenceHandler = new Handler() {
        public void handleMessage(Message msg) {
            int i = 0;
            IGeofenceHardwareCallback callback;
            int geofenceId;
            switch (msg.what) {
                case 1:
                    GeofenceTransition geofenceTransition = msg.obj;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        callback = (IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.get(geofenceTransition.mGeofenceId);
                        if (GeofenceHardwareImpl.DEBUG) {
                            String str = GeofenceHardwareImpl.TAG;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("GeofenceTransistionCallback: GPS : GeofenceId: ");
                            stringBuilder.append(geofenceTransition.mGeofenceId);
                            stringBuilder.append(" Transition: ");
                            stringBuilder.append(geofenceTransition.mTransition);
                            stringBuilder.append(" Location: ");
                            stringBuilder.append(geofenceTransition.mLocation);
                            stringBuilder.append(":");
                            stringBuilder.append(GeofenceHardwareImpl.this.mGeofences);
                            Log.d(str, stringBuilder.toString());
                        }
                    }
                    if (callback != null) {
                        try {
                            callback.onGeofenceTransition(geofenceTransition.mGeofenceId, geofenceTransition.mTransition, geofenceTransition.mLocation, geofenceTransition.mTimestamp, geofenceTransition.mMonitoringType);
                        } catch (RemoteException e) {
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 2:
                    geofenceId = msg.arg1;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        callback = (IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.get(geofenceId);
                    }
                    if (callback != null) {
                        try {
                            callback.onGeofenceAdd(geofenceId, msg.arg2);
                        } catch (RemoteException e2) {
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("Remote Exception:");
                            stringBuilder2.append(e2);
                            Log.i(GeofenceHardwareImpl.TAG, stringBuilder2.toString());
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 3:
                    IGeofenceHardwareCallback callback2;
                    geofenceId = msg.arg1;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        callback2 = (IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.get(geofenceId);
                    }
                    if (callback2 != null) {
                        try {
                            callback2.onGeofenceRemove(geofenceId, msg.arg2);
                        } catch (RemoteException e3) {
                        }
                        IBinder callbackBinder = callback2.asBinder();
                        boolean callbackInUse = false;
                        synchronized (GeofenceHardwareImpl.this.mGeofences) {
                            GeofenceHardwareImpl.this.mGeofences.remove(geofenceId);
                            int i2 = 0;
                            while (i2 < GeofenceHardwareImpl.this.mGeofences.size()) {
                                if (((IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.valueAt(i2)).asBinder() == callbackBinder) {
                                    callbackInUse = true;
                                } else {
                                    i2++;
                                }
                            }
                        }
                        if (!callbackInUse) {
                            Iterator<Reaper> iterator = GeofenceHardwareImpl.this.mReapers.iterator();
                            while (iterator.hasNext()) {
                                Reaper reaper = (Reaper) iterator.next();
                                if (reaper.mCallback != null && reaper.mCallback.asBinder() == callbackBinder) {
                                    iterator.remove();
                                    reaper.unlinkToDeath();
                                    if (GeofenceHardwareImpl.DEBUG) {
                                        Log.d(GeofenceHardwareImpl.TAG, String.format("Removed reaper %s because binder %s is no longer needed.", new Object[]{reaper, callbackBinder}));
                                    }
                                }
                            }
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 4:
                    IGeofenceHardwareCallback callback3;
                    geofenceId = msg.arg1;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        callback3 = (IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.get(geofenceId);
                    }
                    if (callback3 != null) {
                        try {
                            callback3.onGeofencePause(geofenceId, msg.arg2);
                        } catch (RemoteException e4) {
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 5:
                    geofenceId = msg.arg1;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        callback = (IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.get(geofenceId);
                    }
                    if (callback != null) {
                        try {
                            callback.onGeofenceResume(geofenceId, msg.arg2);
                        } catch (RemoteException e5) {
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 6:
                    IGeofenceHardwareCallback callback4 = msg.obj;
                    if (GeofenceHardwareImpl.DEBUG) {
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("Geofence callback reaped:");
                        stringBuilder3.append(callback4);
                        Log.d(GeofenceHardwareImpl.TAG, stringBuilder3.toString());
                    }
                    int monitoringType = msg.arg1;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        while (i < GeofenceHardwareImpl.this.mGeofences.size()) {
                            if (((IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.valueAt(i)).equals(callback4)) {
                                int geofenceId2 = GeofenceHardwareImpl.this.mGeofences.keyAt(i);
                                GeofenceHardwareImpl.this.removeGeofence(GeofenceHardwareImpl.this.mGeofences.keyAt(i), monitoringType);
                                GeofenceHardwareImpl.this.mGeofences.remove(geofenceId2);
                            }
                            i++;
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private final SparseArray<IGeofenceHardwareCallback> mGeofences = new SparseArray();
    private IGpsGeofenceHardware mGpsService;
    private Handler mReaperHandler = new Handler() {
        public void handleMessage(Message msg) {
            int i = msg.what;
            Reaper r;
            if (i == 1) {
                IGeofenceHardwareCallback callback = msg.obj;
                r = new Reaper(callback, msg.arg1);
                if (!GeofenceHardwareImpl.this.mReapers.contains(r)) {
                    GeofenceHardwareImpl.this.mReapers.add(r);
                    try {
                        callback.asBinder().linkToDeath(r, 0);
                    } catch (RemoteException e) {
                    }
                }
            } else if (i == 2) {
                IGeofenceHardwareMonitorCallback monitorCallback = msg.obj;
                r = new Reaper(monitorCallback, msg.arg1);
                if (!GeofenceHardwareImpl.this.mReapers.contains(r)) {
                    GeofenceHardwareImpl.this.mReapers.add(r);
                    try {
                        monitorCallback.asBinder().linkToDeath(r, 0);
                    } catch (RemoteException e2) {
                    }
                }
            } else if (i == 3) {
                GeofenceHardwareImpl.this.mReapers.remove(msg.obj);
            }
        }
    };
    private final ArrayList<Reaper> mReapers = new ArrayList();
    private int[] mSupportedMonitorTypes = new int[2];
    private int mVersion = 1;
    private WakeLock mWakeLock;

    private class GeofenceTransition {
        private int mGeofenceId;
        private Location mLocation;
        private int mMonitoringType;
        private int mSourcesUsed;
        private long mTimestamp;
        private int mTransition;

        GeofenceTransition(int geofenceId, int transition, long timestamp, Location location, int monitoringType, int sourcesUsed) {
            this.mGeofenceId = geofenceId;
            this.mTransition = transition;
            this.mTimestamp = timestamp;
            this.mLocation = location;
            this.mMonitoringType = monitoringType;
            this.mSourcesUsed = sourcesUsed;
        }
    }

    class Reaper implements DeathRecipient {
        private IGeofenceHardwareCallback mCallback;
        private IGeofenceHardwareMonitorCallback mMonitorCallback;
        private int mMonitoringType;

        Reaper(IGeofenceHardwareCallback c, int monitoringType) {
            this.mCallback = c;
            this.mMonitoringType = monitoringType;
        }

        Reaper(IGeofenceHardwareMonitorCallback c, int monitoringType) {
            this.mMonitorCallback = c;
            this.mMonitoringType = monitoringType;
        }

        public void binderDied() {
            Message m;
            if (this.mCallback != null) {
                m = GeofenceHardwareImpl.this.mGeofenceHandler.obtainMessage(6, this.mCallback);
                m.arg1 = this.mMonitoringType;
                GeofenceHardwareImpl.this.mGeofenceHandler.sendMessage(m);
            } else if (this.mMonitorCallback != null) {
                m = GeofenceHardwareImpl.this.mCallbacksHandler.obtainMessage(4, this.mMonitorCallback);
                m.arg1 = this.mMonitoringType;
                GeofenceHardwareImpl.this.mCallbacksHandler.sendMessage(m);
            }
            GeofenceHardwareImpl.this.mReaperHandler.sendMessage(GeofenceHardwareImpl.this.mReaperHandler.obtainMessage(3, this));
        }

        public int hashCode() {
            int result = 17 * 31;
            IGeofenceHardwareCallback iGeofenceHardwareCallback = this.mCallback;
            int i = 0;
            int hashCode = (result + (iGeofenceHardwareCallback != null ? iGeofenceHardwareCallback.asBinder().hashCode() : 0)) * 31;
            IGeofenceHardwareMonitorCallback iGeofenceHardwareMonitorCallback = this.mMonitorCallback;
            if (iGeofenceHardwareMonitorCallback != null) {
                i = iGeofenceHardwareMonitorCallback.asBinder().hashCode();
            }
            return ((hashCode + i) * 31) + this.mMonitoringType;
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            Reaper rhs = (Reaper) obj;
            if (binderEquals(rhs.mCallback, this.mCallback) && binderEquals(rhs.mMonitorCallback, this.mMonitorCallback) && rhs.mMonitoringType == this.mMonitoringType) {
                z = true;
            }
            return z;
        }

        private boolean binderEquals(IInterface left, IInterface right) {
            boolean z = true;
            boolean z2 = false;
            if (left == null) {
                if (right != null) {
                    z = false;
                }
                return z;
            }
            if (right != null && left.asBinder() == right.asBinder()) {
                z2 = true;
            }
            return z2;
        }

        private boolean unlinkToDeath() {
            IGeofenceHardwareMonitorCallback iGeofenceHardwareMonitorCallback = this.mMonitorCallback;
            if (iGeofenceHardwareMonitorCallback != null) {
                return iGeofenceHardwareMonitorCallback.asBinder().unlinkToDeath(this, 0);
            }
            IGeofenceHardwareCallback iGeofenceHardwareCallback = this.mCallback;
            if (iGeofenceHardwareCallback != null) {
                return iGeofenceHardwareCallback.asBinder().unlinkToDeath(this, 0);
            }
            return true;
        }

        private boolean callbackEquals(IGeofenceHardwareCallback cb) {
            IGeofenceHardwareCallback iGeofenceHardwareCallback = this.mCallback;
            return iGeofenceHardwareCallback != null && iGeofenceHardwareCallback.asBinder() == cb.asBinder();
        }
    }

    public static synchronized GeofenceHardwareImpl getInstance(Context context) {
        GeofenceHardwareImpl geofenceHardwareImpl;
        synchronized (GeofenceHardwareImpl.class) {
            if (sInstance == null) {
                sInstance = new GeofenceHardwareImpl(context);
            }
            geofenceHardwareImpl = sInstance;
        }
        return geofenceHardwareImpl;
    }

    private GeofenceHardwareImpl(Context context) {
        this.mContext = context;
        setMonitorAvailability(0, 2);
        setMonitorAvailability(1, 2);
    }

    private void acquireWakeLock() {
        if (this.mWakeLock == null) {
            this.mWakeLock = ((PowerManager) this.mContext.getSystemService(Context.POWER_SERVICE)).newWakeLock(1, TAG);
        }
        this.mWakeLock.acquire();
    }

    private void releaseWakeLock() {
        if (this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
    }

    private void updateGpsHardwareAvailability() {
        boolean gpsSupported;
        try {
            gpsSupported = this.mGpsService.isHardwareGeofenceSupported();
        } catch (RemoteException e) {
            Log.e(TAG, "Remote Exception calling LocationManagerService");
            gpsSupported = false;
        }
        if (gpsSupported) {
            setMonitorAvailability(0, 0);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0022  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0014 A:{Catch:{ RemoteException -> 0x0025 }} */
    /* JADX WARNING: Removed duplicated region for block: B:23:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0030  */
    private void updateFusedHardwareAvailability() {
        /*
        r5 = this;
        r0 = 0;
        r1 = 1;
        r2 = r5.mVersion;	 Catch:{ RemoteException -> 0x0025 }
        r3 = 2;
        if (r2 < r3) goto L_0x000f;
    L_0x0007:
        r2 = r5.mCapabilities;	 Catch:{ RemoteException -> 0x0025 }
        r2 = r2 & r1;
        if (r2 == 0) goto L_0x000d;
    L_0x000c:
        goto L_0x000f;
    L_0x000d:
        r2 = r0;
        goto L_0x0010;
    L_0x000f:
        r2 = r1;
    L_0x0010:
        r3 = r5.mFusedService;	 Catch:{ RemoteException -> 0x0025 }
        if (r3 == 0) goto L_0x0022;
    L_0x0014:
        r3 = r5.mFusedService;	 Catch:{ RemoteException -> 0x0025 }
        r3 = r3.isSupported();	 Catch:{ RemoteException -> 0x0025 }
        if (r3 == 0) goto L_0x0020;
    L_0x001c:
        if (r2 == 0) goto L_0x0020;
    L_0x001e:
        r3 = r1;
        goto L_0x0023;
    L_0x0020:
        r3 = r0;
        goto L_0x0023;
    L_0x0022:
        r3 = r0;
    L_0x0023:
        r2 = r3;
        goto L_0x002e;
    L_0x0025:
        r2 = move-exception;
        r3 = "GeofenceHardwareImpl";
        r4 = "RemoteException calling LocationManagerService";
        android.util.Log.e(r3, r4);
        r2 = 0;
    L_0x002e:
        if (r2 == 0) goto L_0x0033;
    L_0x0030:
        r5.setMonitorAvailability(r1, r0);
    L_0x0033:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.location.GeofenceHardwareImpl.updateFusedHardwareAvailability():void");
    }

    public void setGpsHardwareGeofence(IGpsGeofenceHardware service) {
        if (this.mGpsService == null) {
            this.mGpsService = service;
            updateGpsHardwareAvailability();
            return;
        }
        String str = TAG;
        if (service == null) {
            this.mGpsService = null;
            Log.w(str, "GPS Geofence Hardware service seems to have crashed");
            return;
        }
        Log.e(str, "Error: GpsService being set again.");
    }

    public void onCapabilities(int capabilities) {
        this.mCapabilities = capabilities;
        updateFusedHardwareAvailability();
    }

    public void setVersion(int version) {
        this.mVersion = version;
        updateFusedHardwareAvailability();
    }

    public void setFusedGeofenceHardware(IFusedGeofenceHardware service) {
        if (this.mFusedService == null) {
            this.mFusedService = service;
            updateFusedHardwareAvailability();
            return;
        }
        String str = TAG;
        if (service == null) {
            this.mFusedService = null;
            Log.w(str, "Fused Geofence Hardware service seems to have crashed");
            return;
        }
        Log.e(str, "Error: FusedService being set again");
    }

    public int[] getMonitoringTypes() {
        boolean gpsSupported;
        boolean fusedSupported;
        synchronized (this.mSupportedMonitorTypes) {
            gpsSupported = this.mSupportedMonitorTypes[0] != 2;
            fusedSupported = this.mSupportedMonitorTypes[1] != 2;
        }
        if (gpsSupported) {
            if (fusedSupported) {
                return new int[]{0, 1};
            }
            return new int[]{0};
        } else if (!fusedSupported) {
            return new int[0];
        } else {
            return new int[]{1};
        }
    }

    public int getStatusOfMonitoringType(int monitoringType) {
        int i;
        synchronized (this.mSupportedMonitorTypes) {
            if (monitoringType >= this.mSupportedMonitorTypes.length || monitoringType < 0) {
                throw new IllegalArgumentException("Unknown monitoring type");
            }
            i = this.mSupportedMonitorTypes[monitoringType];
        }
        return i;
    }

    public int getCapabilitiesForMonitoringType(int monitoringType) {
        if (this.mSupportedMonitorTypes[monitoringType] == 0) {
            if (monitoringType == 0) {
                return 1;
            }
            if (monitoringType == 1) {
                if (this.mVersion >= 2) {
                    return this.mCapabilities;
                }
                return 1;
            }
        }
        return 0;
    }

    public boolean addCircularFence(int monitoringType, GeofenceHardwareRequestParcelable request, IGeofenceHardwareCallback callback) {
        boolean result;
        int i = monitoringType;
        IGeofenceHardwareCallback iGeofenceHardwareCallback = callback;
        int geofenceId = request.getId();
        if (DEBUG) {
            Log.d(TAG, String.format("addCircularFence: monitoringType=%d, %s", new Object[]{Integer.valueOf(monitoringType), request}));
        }
        synchronized (this.mGeofences) {
            this.mGeofences.put(geofenceId, iGeofenceHardwareCallback);
        }
        if (i == 0) {
            IGpsGeofenceHardware iGpsGeofenceHardware = this.mGpsService;
            if (iGpsGeofenceHardware == null) {
                return false;
            }
            try {
                result = iGpsGeofenceHardware.addCircularHardwareGeofence(request.getId(), request.getLatitude(), request.getLongitude(), request.getRadius(), request.getLastTransition(), request.getMonitorTransitions(), request.getNotificationResponsiveness(), request.getUnknownTimer());
            } catch (RemoteException e) {
                Log.e(TAG, "AddGeofence: Remote Exception calling LocationManagerService");
                result = false;
            }
        } else if (i != 1) {
            result = false;
        } else {
            IFusedGeofenceHardware iFusedGeofenceHardware = this.mFusedService;
            if (iFusedGeofenceHardware == null) {
                return false;
            }
            try {
                iFusedGeofenceHardware.addGeofences(new GeofenceHardwareRequestParcelable[]{request});
                result = true;
            } catch (RemoteException e2) {
                Log.e(TAG, "AddGeofence: RemoteException calling LocationManagerService");
                result = false;
            }
        }
        if (result) {
            Message m = this.mReaperHandler.obtainMessage(1, iGeofenceHardwareCallback);
            m.arg1 = i;
            this.mReaperHandler.sendMessage(m);
        } else {
            synchronized (this.mGeofences) {
                this.mGeofences.remove(geofenceId);
            }
        }
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("addCircularFence: Result is: ");
            stringBuilder.append(result);
            Log.d(TAG, stringBuilder.toString());
        }
        return result;
    }

    public boolean removeGeofence(int geofenceId, int monitoringType) {
        boolean result;
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remove Geofence: GeofenceId: ");
            stringBuilder.append(geofenceId);
            Log.d(TAG, stringBuilder.toString());
        }
        synchronized (this.mGeofences) {
            if (this.mGeofences.get(geofenceId) != null) {
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Geofence ");
                stringBuilder2.append(geofenceId);
                stringBuilder2.append(" not registered.");
                throw new IllegalArgumentException(stringBuilder2.toString());
            }
        }
        if (monitoringType == 0) {
            IGpsGeofenceHardware iGpsGeofenceHardware = this.mGpsService;
            if (iGpsGeofenceHardware == null) {
                return false;
            }
            try {
                result = iGpsGeofenceHardware.removeHardwareGeofence(geofenceId);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoveGeofence: Remote Exception calling LocationManagerService");
                result = false;
            }
        } else if (monitoringType != 1) {
            result = false;
        } else {
            IFusedGeofenceHardware iFusedGeofenceHardware = this.mFusedService;
            if (iFusedGeofenceHardware == null) {
                return false;
            }
            try {
                iFusedGeofenceHardware.removeGeofences(new int[]{geofenceId});
                result = true;
            } catch (RemoteException e2) {
                Log.e(TAG, "RemoveGeofence: RemoteException calling LocationManagerService");
                result = false;
            }
        }
        if (DEBUG) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("removeGeofence: Result is: ");
            stringBuilder3.append(result);
            Log.d(TAG, stringBuilder3.toString());
        }
        return result;
    }

    public boolean pauseGeofence(int geofenceId, int monitoringType) {
        boolean result;
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Pause Geofence: GeofenceId: ");
            stringBuilder.append(geofenceId);
            Log.d(TAG, stringBuilder.toString());
        }
        synchronized (this.mGeofences) {
            if (this.mGeofences.get(geofenceId) != null) {
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Geofence ");
                stringBuilder2.append(geofenceId);
                stringBuilder2.append(" not registered.");
                throw new IllegalArgumentException(stringBuilder2.toString());
            }
        }
        if (monitoringType == 0) {
            IGpsGeofenceHardware iGpsGeofenceHardware = this.mGpsService;
            if (iGpsGeofenceHardware == null) {
                return false;
            }
            try {
                result = iGpsGeofenceHardware.pauseHardwareGeofence(geofenceId);
            } catch (RemoteException e) {
                Log.e(TAG, "PauseGeofence: Remote Exception calling LocationManagerService");
                result = false;
            }
        } else if (monitoringType != 1) {
            result = false;
        } else {
            IFusedGeofenceHardware iFusedGeofenceHardware = this.mFusedService;
            if (iFusedGeofenceHardware == null) {
                return false;
            }
            try {
                iFusedGeofenceHardware.pauseMonitoringGeofence(geofenceId);
                result = true;
            } catch (RemoteException e2) {
                Log.e(TAG, "PauseGeofence: RemoteException calling LocationManagerService");
                result = false;
            }
        }
        if (DEBUG) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("pauseGeofence: Result is: ");
            stringBuilder3.append(result);
            Log.d(TAG, stringBuilder3.toString());
        }
        return result;
    }

    public boolean resumeGeofence(int geofenceId, int monitoringType, int monitorTransition) {
        boolean result;
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resume Geofence: GeofenceId: ");
            stringBuilder.append(geofenceId);
            Log.d(TAG, stringBuilder.toString());
        }
        synchronized (this.mGeofences) {
            if (this.mGeofences.get(geofenceId) != null) {
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Geofence ");
                stringBuilder2.append(geofenceId);
                stringBuilder2.append(" not registered.");
                throw new IllegalArgumentException(stringBuilder2.toString());
            }
        }
        if (monitoringType == 0) {
            IGpsGeofenceHardware iGpsGeofenceHardware = this.mGpsService;
            if (iGpsGeofenceHardware == null) {
                return false;
            }
            try {
                result = iGpsGeofenceHardware.resumeHardwareGeofence(geofenceId, monitorTransition);
            } catch (RemoteException e) {
                Log.e(TAG, "ResumeGeofence: Remote Exception calling LocationManagerService");
                result = false;
            }
        } else if (monitoringType != 1) {
            result = false;
        } else {
            IFusedGeofenceHardware iFusedGeofenceHardware = this.mFusedService;
            if (iFusedGeofenceHardware == null) {
                return false;
            }
            try {
                iFusedGeofenceHardware.resumeMonitoringGeofence(geofenceId, monitorTransition);
                result = true;
            } catch (RemoteException e2) {
                Log.e(TAG, "ResumeGeofence: RemoteException calling LocationManagerService");
                result = false;
            }
        }
        if (DEBUG) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("resumeGeofence: Result is: ");
            stringBuilder3.append(result);
            Log.d(TAG, stringBuilder3.toString());
        }
        return result;
    }

    public boolean registerForMonitorStateChangeCallback(int monitoringType, IGeofenceHardwareMonitorCallback callback) {
        Message reaperMessage = this.mReaperHandler.obtainMessage(2, callback);
        reaperMessage.arg1 = monitoringType;
        this.mReaperHandler.sendMessage(reaperMessage);
        Message m = this.mCallbacksHandler.obtainMessage(2, callback);
        m.arg1 = monitoringType;
        this.mCallbacksHandler.sendMessage(m);
        return true;
    }

    public boolean unregisterForMonitorStateChangeCallback(int monitoringType, IGeofenceHardwareMonitorCallback callback) {
        Message m = this.mCallbacksHandler.obtainMessage(3, callback);
        m.arg1 = monitoringType;
        this.mCallbacksHandler.sendMessage(m);
        return true;
    }

    public void reportGeofenceTransition(int geofenceId, Location location, int transition, long transitionTimestamp, int monitoringType, int sourcesUsed) {
        Location location2 = location;
        String str = TAG;
        if (location2 == null) {
            Log.e(str, String.format("Invalid Geofence Transition: location=null", new Object[0]));
            return;
        }
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("GeofenceTransition| ");
            stringBuilder.append(location2);
            stringBuilder.append(", transition:");
            stringBuilder.append(transition);
            stringBuilder.append(", transitionTimestamp:");
            stringBuilder.append(transitionTimestamp);
            stringBuilder.append(", monitoringType:");
            stringBuilder.append(monitoringType);
            stringBuilder.append(", sourcesUsed:");
            stringBuilder.append(sourcesUsed);
            Log.d(str, stringBuilder.toString());
        } else {
            int i = transition;
            long j = transitionTimestamp;
            int i2 = monitoringType;
            int i3 = sourcesUsed;
        }
        GeofenceTransition geofenceTransition = new GeofenceTransition(geofenceId, transition, transitionTimestamp, location, monitoringType, sourcesUsed);
        acquireWakeLock();
        this.mGeofenceHandler.obtainMessage(1, geofenceTransition).sendToTarget();
    }

    public void reportGeofenceMonitorStatus(int monitoringType, int monitoringStatus, Location location, int source) {
        setMonitorAvailability(monitoringType, monitoringStatus);
        acquireWakeLock();
        this.mCallbacksHandler.obtainMessage(1, new GeofenceHardwareMonitorEvent(monitoringType, monitoringStatus, source, location)).sendToTarget();
    }

    private void reportGeofenceOperationStatus(int operation, int geofenceId, int operationStatus) {
        acquireWakeLock();
        Message message = this.mGeofenceHandler.obtainMessage(operation);
        message.arg1 = geofenceId;
        message.arg2 = operationStatus;
        message.sendToTarget();
    }

    public void reportGeofenceAddStatus(int geofenceId, int status) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AddCallback| id:");
            stringBuilder.append(geofenceId);
            stringBuilder.append(", status:");
            stringBuilder.append(status);
            Log.d(TAG, stringBuilder.toString());
        }
        reportGeofenceOperationStatus(2, geofenceId, status);
    }

    public void reportGeofenceRemoveStatus(int geofenceId, int status) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("RemoveCallback| id:");
            stringBuilder.append(geofenceId);
            stringBuilder.append(", status:");
            stringBuilder.append(status);
            Log.d(TAG, stringBuilder.toString());
        }
        reportGeofenceOperationStatus(3, geofenceId, status);
    }

    public void reportGeofencePauseStatus(int geofenceId, int status) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("PauseCallbac| id:");
            stringBuilder.append(geofenceId);
            stringBuilder.append(", status");
            stringBuilder.append(status);
            Log.d(TAG, stringBuilder.toString());
        }
        reportGeofenceOperationStatus(4, geofenceId, status);
    }

    public void reportGeofenceResumeStatus(int geofenceId, int status) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ResumeCallback| id:");
            stringBuilder.append(geofenceId);
            stringBuilder.append(", status:");
            stringBuilder.append(status);
            Log.d(TAG, stringBuilder.toString());
        }
        reportGeofenceOperationStatus(5, geofenceId, status);
    }

    private void setMonitorAvailability(int monitor, int val) {
        synchronized (this.mSupportedMonitorTypes) {
            this.mSupportedMonitorTypes[monitor] = val;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int getMonitoringResolutionLevel(int monitoringType) {
        return (monitoringType == 0 || monitoringType == 1) ? 3 : 1;
    }

    /* Access modifiers changed, original: 0000 */
    public int getAllowedResolutionLevel(int pid, int uid) {
        if (this.mContext.checkPermission(permission.ACCESS_FINE_LOCATION, pid, uid) == 0) {
            return 3;
        }
        if (this.mContext.checkPermission(permission.ACCESS_COARSE_LOCATION, pid, uid) == 0) {
            return 2;
        }
        return 1;
    }
}
