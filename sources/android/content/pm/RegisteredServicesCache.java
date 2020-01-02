package android.content.pm;

import android.Manifest.permission;
import android.accounts.GrantCredentialsPermissionActivity;
import android.annotation.UnsupportedAppUsage;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Environment;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.AtomicFile;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FastXmlSerializer;
import com.google.android.collect.Maps;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public abstract class RegisteredServicesCache<V> {
    private static final boolean DEBUG = false;
    protected static final String REGISTERED_SERVICES_DIR = "registered_services";
    private static final String TAG = "PackageManager";
    private final String mAttributesName;
    public final Context mContext;
    private final BroadcastReceiver mExternalReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            RegisteredServicesCache.this.handlePackageEvent(intent, 0);
        }
    };
    private Handler mHandler;
    private final String mInterfaceName;
    private RegisteredServicesCacheListener<V> mListener;
    private final String mMetaDataName;
    private final BroadcastReceiver mPackageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int uid = intent.getIntExtra(Intent.EXTRA_UID, -1);
            if (uid != -1) {
                RegisteredServicesCache.this.handlePackageEvent(intent, UserHandle.getUserId(uid));
            }
        }
    };
    private final XmlSerializerAndParser<V> mSerializerAndParser;
    protected final Object mServicesLock = new Object();
    private final BroadcastReceiver mUserRemovedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            RegisteredServicesCache.this.onUserRemoved(intent.getIntExtra(Intent.EXTRA_USER_HANDLE, -1));
        }
    };
    @GuardedBy({"mServicesLock"})
    private final SparseArray<UserServices<V>> mUserServices = new SparseArray(2);

    public static class ServiceInfo<V> {
        public final ComponentInfo componentInfo;
        @UnsupportedAppUsage
        public final ComponentName componentName;
        @UnsupportedAppUsage
        public final V type;
        @UnsupportedAppUsage
        public final int uid;

        public ServiceInfo(V type, ComponentInfo componentInfo, ComponentName componentName) {
            this.type = type;
            this.componentInfo = componentInfo;
            this.componentName = componentName;
            this.uid = componentInfo != null ? componentInfo.applicationInfo.uid : -1;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ServiceInfo: ");
            stringBuilder.append(this.type);
            stringBuilder.append(", ");
            stringBuilder.append(this.componentName);
            stringBuilder.append(", uid ");
            stringBuilder.append(this.uid);
            return stringBuilder.toString();
        }
    }

    private static class UserServices<V> {
        @GuardedBy({"mServicesLock"})
        boolean mBindInstantServiceAllowed;
        @GuardedBy({"mServicesLock"})
        boolean mPersistentServicesFileDidNotExist;
        @GuardedBy({"mServicesLock"})
        final Map<V, Integer> persistentServices;
        @GuardedBy({"mServicesLock"})
        Map<V, ServiceInfo<V>> services;

        private UserServices() {
            this.persistentServices = Maps.newHashMap();
            this.services = null;
            this.mPersistentServicesFileDidNotExist = true;
            this.mBindInstantServiceAllowed = false;
        }

        /* synthetic */ UserServices(AnonymousClass1 x0) {
            this();
        }
    }

    public abstract V parseServiceAttributes(Resources resources, String str, AttributeSet attributeSet);

    @GuardedBy({"mServicesLock"})
    private UserServices<V> findOrCreateUserLocked(int userId) {
        return findOrCreateUserLocked(userId, true);
    }

    @GuardedBy({"mServicesLock"})
    private UserServices<V> findOrCreateUserLocked(int userId, boolean loadFromFileIfNew) {
        UserServices<V> services = (UserServices) this.mUserServices.get(userId);
        if (services == null) {
            services = new UserServices();
            this.mUserServices.put(userId, services);
            if (loadFromFileIfNew && this.mSerializerAndParser != null) {
                UserInfo user = getUser(userId);
                if (user != null) {
                    AtomicFile file = createFileForUser(user.id);
                    if (file.getBaseFile().exists()) {
                        InputStream is = null;
                        try {
                            is = file.openRead();
                            readPersistentServicesLocked(is);
                        } catch (Exception e) {
                            String str = TAG;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Error reading persistent services for user ");
                            stringBuilder.append(user.id);
                            Log.w(str, stringBuilder.toString(), e);
                        } catch (Throwable th) {
                            IoUtils.closeQuietly(is);
                        }
                        IoUtils.closeQuietly(is);
                    }
                }
            }
        }
        return services;
    }

    @UnsupportedAppUsage
    public RegisteredServicesCache(Context context, String interfaceName, String metaDataName, String attributeName, XmlSerializerAndParser<V> serializerAndParser) {
        this.mContext = context;
        this.mInterfaceName = interfaceName;
        this.mMetaDataName = metaDataName;
        this.mAttributesName = attributeName;
        this.mSerializerAndParser = serializerAndParser;
        migrateIfNecessaryLocked();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        this.mContext.registerReceiverAsUser(this.mPackageReceiver, UserHandle.ALL, intentFilter, null, null);
        IntentFilter sdFilter = new IntentFilter();
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        this.mContext.registerReceiver(this.mExternalReceiver, sdFilter);
        IntentFilter userFilter = new IntentFilter();
        sdFilter.addAction(Intent.ACTION_USER_REMOVED);
        this.mContext.registerReceiver(this.mUserRemovedReceiver, userFilter);
    }

    private void handlePackageEvent(Intent intent, int userId) {
        String action = intent.getAction();
        boolean equals = Intent.ACTION_PACKAGE_REMOVED.equals(action);
        String str = Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE;
        equals = equals || str.equals(action);
        boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
        if (!equals || !replacing) {
            int[] uids = null;
            if (Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE.equals(action) || str.equals(action)) {
                uids = intent.getIntArrayExtra(Intent.EXTRA_CHANGED_UID_LIST);
            } else {
                if (intent.getIntExtra(Intent.EXTRA_UID, -1) > 0) {
                    uids = new int[]{intent.getIntExtra(Intent.EXTRA_UID, -1)};
                }
            }
            generateServicesMap(uids, userId);
        }
    }

    public void invalidateCache(int userId) {
        synchronized (this.mServicesLock) {
            findOrCreateUserLocked(userId).services = null;
            onServicesChangedLocked(userId);
        }
    }

    public void dump(FileDescriptor fd, PrintWriter fout, String[] args, int userId) {
        synchronized (this.mServicesLock) {
            UserServices<V> user = findOrCreateUserLocked(userId);
            if (user.services != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("RegisteredServicesCache: ");
                stringBuilder.append(user.services.size());
                stringBuilder.append(" services");
                fout.println(stringBuilder.toString());
                for (ServiceInfo<?> info : user.services.values()) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("  ");
                    stringBuilder2.append(info);
                    fout.println(stringBuilder2.toString());
                }
            } else {
                fout.println("RegisteredServicesCache: services not loaded");
            }
        }
    }

    public RegisteredServicesCacheListener<V> getListener() {
        RegisteredServicesCacheListener registeredServicesCacheListener;
        synchronized (this) {
            registeredServicesCacheListener = this.mListener;
        }
        return registeredServicesCacheListener;
    }

    public void setListener(RegisteredServicesCacheListener<V> listener, Handler handler) {
        if (handler == null) {
            handler = new Handler(this.mContext.getMainLooper());
        }
        synchronized (this) {
            this.mHandler = handler;
            this.mListener = listener;
        }
    }

    private void notifyListener(V type, int userId, boolean removed) {
        RegisteredServicesCacheListener<V> listener;
        Handler handler;
        synchronized (this) {
            listener = this.mListener;
            handler = this.mHandler;
        }
        if (listener != null) {
            handler.post(new -$$Lambda$RegisteredServicesCache$lDXmLhKoG7lZpIyDOuPYOrjzDYY(listener, type, userId, removed));
        }
    }

    static /* synthetic */ void lambda$notifyListener$0(RegisteredServicesCacheListener listener2, Object type, int userId, boolean removed) {
        try {
            listener2.onServiceChanged(type, userId, removed);
        } catch (Throwable th) {
            Slog.wtf(TAG, "Exception from onServiceChanged", th);
        }
    }

    public ServiceInfo<V> getServiceInfo(V type, int userId) {
        ServiceInfo serviceInfo;
        synchronized (this.mServicesLock) {
            UserServices<V> user = findOrCreateUserLocked(userId);
            if (user.services == null) {
                generateServicesMap(null, userId);
            }
            serviceInfo = (ServiceInfo) user.services.get(type);
        }
        return serviceInfo;
    }

    public Collection<ServiceInfo<V>> getAllServices(int userId) {
        Collection unmodifiableCollection;
        synchronized (this.mServicesLock) {
            UserServices<V> user = findOrCreateUserLocked(userId);
            if (user.services == null) {
                generateServicesMap(null, userId);
            }
            unmodifiableCollection = Collections.unmodifiableCollection(new ArrayList(user.services.values()));
        }
        return unmodifiableCollection;
    }

    /* JADX WARNING: Missing block: B:9:0x001a, code skipped:
            r0 = null;
            r2 = r1.iterator();
     */
    /* JADX WARNING: Missing block: B:11:0x0023, code skipped:
            if (r2.hasNext() == false) goto L_0x005c;
     */
    /* JADX WARNING: Missing block: B:12:0x0025, code skipped:
            r3 = (android.content.pm.RegisteredServicesCache.ServiceInfo) r2.next();
            r4 = (long) r3.componentInfo.applicationInfo.versionCode;
            r7 = null;
     */
    /* JADX WARNING: Missing block: B:15:0x0042, code skipped:
            r7 = r10.mContext.getPackageManager().getApplicationInfoAsUser(r3.componentInfo.packageName, 0, r11);
     */
    /* JADX WARNING: Missing block: B:24:0x005c, code skipped:
            if (r0 == null) goto L_0x006b;
     */
    /* JADX WARNING: Missing block: B:26:0x0062, code skipped:
            if (r0.size() <= 0) goto L_0x006b;
     */
    /* JADX WARNING: Missing block: B:27:0x0064, code skipped:
            generateServicesMap(r0.toArray(), r11);
     */
    /* JADX WARNING: Missing block: B:28:0x006b, code skipped:
            return;
     */
    public void updateServices(int r11) {
        /*
        r10 = this;
        r0 = r10.mServicesLock;
        monitor-enter(r0);
        r1 = r10.findOrCreateUserLocked(r11);	 Catch:{ all -> 0x006c }
        r2 = r1.services;	 Catch:{ all -> 0x006c }
        if (r2 != 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r0);	 Catch:{ all -> 0x006c }
        return;
    L_0x000d:
        r2 = new java.util.ArrayList;	 Catch:{ all -> 0x006c }
        r3 = r1.services;	 Catch:{ all -> 0x006c }
        r3 = r3.values();	 Catch:{ all -> 0x006c }
        r2.<init>(r3);	 Catch:{ all -> 0x006c }
        r1 = r2;
        monitor-exit(r0);	 Catch:{ all -> 0x006c }
        r0 = 0;
        r2 = r1.iterator();
    L_0x001f:
        r3 = r2.hasNext();
        if (r3 == 0) goto L_0x005c;
    L_0x0025:
        r3 = r2.next();
        r3 = (android.content.pm.RegisteredServicesCache.ServiceInfo) r3;
        r4 = r3.componentInfo;
        r4 = r4.applicationInfo;
        r4 = r4.versionCode;
        r4 = (long) r4;
        r6 = r3.componentInfo;
        r6 = r6.packageName;
        r7 = 0;
        r8 = r10.mContext;	 Catch:{ NameNotFoundException -> 0x0044 }
        r8 = r8.getPackageManager();	 Catch:{ NameNotFoundException -> 0x0044 }
        r9 = 0;
        r8 = r8.getApplicationInfoAsUser(r6, r9, r11);	 Catch:{ NameNotFoundException -> 0x0044 }
        r7 = r8;
        goto L_0x0045;
    L_0x0044:
        r8 = move-exception;
    L_0x0045:
        if (r7 == 0) goto L_0x004e;
    L_0x0047:
        r8 = r7.versionCode;
        r8 = (long) r8;
        r8 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1));
        if (r8 == 0) goto L_0x005b;
    L_0x004e:
        if (r0 != 0) goto L_0x0056;
    L_0x0050:
        r8 = new android.util.IntArray;
        r8.<init>();
        r0 = r8;
    L_0x0056:
        r8 = r3.uid;
        r0.add(r8);
    L_0x005b:
        goto L_0x001f;
    L_0x005c:
        if (r0 == 0) goto L_0x006b;
    L_0x005e:
        r2 = r0.size();
        if (r2 <= 0) goto L_0x006b;
    L_0x0064:
        r2 = r0.toArray();
        r10.generateServicesMap(r2, r11);
    L_0x006b:
        return;
    L_0x006c:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x006c }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.RegisteredServicesCache.updateServices(int):void");
    }

    public boolean getBindInstantServiceAllowed(int userId) {
        boolean z;
        this.mContext.enforceCallingOrSelfPermission(permission.MANAGE_BIND_INSTANT_SERVICE, "getBindInstantServiceAllowed");
        synchronized (this.mServicesLock) {
            z = findOrCreateUserLocked(userId).mBindInstantServiceAllowed;
        }
        return z;
    }

    public void setBindInstantServiceAllowed(int userId, boolean allowed) {
        this.mContext.enforceCallingOrSelfPermission(permission.MANAGE_BIND_INSTANT_SERVICE, "setBindInstantServiceAllowed");
        synchronized (this.mServicesLock) {
            findOrCreateUserLocked(userId).mBindInstantServiceAllowed = allowed;
        }
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public boolean inSystemImage(int callerUid) {
        String[] packages = this.mContext.getPackageManager().getPackagesForUid(callerUid);
        if (packages != null) {
            int length = packages.length;
            int i = 0;
            while (i < length) {
                try {
                    if ((this.mContext.getPackageManager().getPackageInfo(packages[i], 0).applicationInfo.flags & 1) != 0) {
                        return true;
                    }
                    i++;
                } catch (NameNotFoundException e) {
                    return false;
                }
            }
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public List<ResolveInfo> queryIntentServices(int userId) {
        PackageManager pm = this.mContext.getPackageManager();
        int flags = 786560;
        synchronized (this.mServicesLock) {
            if (findOrCreateUserLocked(userId).mBindInstantServiceAllowed) {
                flags = 786560 | 8388608;
            }
        }
        return pm.queryIntentServicesAsUser(new Intent(this.mInterfaceName), flags, userId);
    }

    /* JADX WARNING: Missing block: B:59:0x015d, code skipped:
            return;
     */
    private void generateServicesMap(int[] r17, int r18) {
        /*
        r16 = this;
        r1 = r16;
        r2 = r18;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r3 = r0;
        r4 = r1.queryIntentServices(r2);
        r5 = r4.iterator();
    L_0x0012:
        r0 = r5.hasNext();
        if (r0 == 0) goto L_0x0061;
    L_0x0018:
        r0 = r5.next();
        r6 = r0;
        r6 = (android.content.pm.ResolveInfo) r6;
        r0 = r1.parseServiceInfo(r6);	 Catch:{ IOException | XmlPullParserException -> 0x0045, IOException | XmlPullParserException -> 0x0045 }
        if (r0 != 0) goto L_0x0040;
    L_0x0025:
        r7 = "PackageManager";
        r8 = new java.lang.StringBuilder;	 Catch:{ IOException | XmlPullParserException -> 0x0045, IOException | XmlPullParserException -> 0x0045 }
        r8.<init>();	 Catch:{ IOException | XmlPullParserException -> 0x0045, IOException | XmlPullParserException -> 0x0045 }
        r9 = "Unable to load service info ";
        r8.append(r9);	 Catch:{ IOException | XmlPullParserException -> 0x0045, IOException | XmlPullParserException -> 0x0045 }
        r9 = r6.toString();	 Catch:{ IOException | XmlPullParserException -> 0x0045, IOException | XmlPullParserException -> 0x0045 }
        r8.append(r9);	 Catch:{ IOException | XmlPullParserException -> 0x0045, IOException | XmlPullParserException -> 0x0045 }
        r8 = r8.toString();	 Catch:{ IOException | XmlPullParserException -> 0x0045, IOException | XmlPullParserException -> 0x0045 }
        android.util.Log.w(r7, r8);	 Catch:{ IOException | XmlPullParserException -> 0x0045, IOException | XmlPullParserException -> 0x0045 }
        goto L_0x0012;
    L_0x0040:
        r3.add(r0);	 Catch:{ IOException | XmlPullParserException -> 0x0045, IOException | XmlPullParserException -> 0x0045 }
        goto L_0x0060;
    L_0x0045:
        r0 = move-exception;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "Unable to load service info ";
        r7.append(r8);
        r8 = r6.toString();
        r7.append(r8);
        r7 = r7.toString();
        r8 = "PackageManager";
        android.util.Log.w(r8, r7, r0);
    L_0x0060:
        goto L_0x0012;
    L_0x0061:
        r5 = r1.mServicesLock;
        monitor-enter(r5);
        r0 = r1.findOrCreateUserLocked(r2);	 Catch:{ all -> 0x015e }
        r6 = r0.services;	 Catch:{ all -> 0x015e }
        r8 = 0;
        if (r6 != 0) goto L_0x006f;
    L_0x006d:
        r6 = 1;
        goto L_0x0070;
    L_0x006f:
        r6 = r8;
    L_0x0070:
        if (r6 == 0) goto L_0x0078;
    L_0x0072:
        r9 = com.google.android.collect.Maps.newHashMap();	 Catch:{ all -> 0x015e }
        r0.services = r9;	 Catch:{ all -> 0x015e }
    L_0x0078:
        r9 = new java.lang.StringBuilder;	 Catch:{ all -> 0x015e }
        r9.<init>();	 Catch:{ all -> 0x015e }
        r10 = 0;
        r11 = r3.iterator();	 Catch:{ all -> 0x015e }
    L_0x0082:
        r12 = r11.hasNext();	 Catch:{ all -> 0x015e }
        if (r12 == 0) goto L_0x00fb;
    L_0x0088:
        r12 = r11.next();	 Catch:{ all -> 0x015e }
        r12 = (android.content.pm.RegisteredServicesCache.ServiceInfo) r12;	 Catch:{ all -> 0x015e }
        r13 = r0.persistentServices;	 Catch:{ all -> 0x015e }
        r14 = r12.type;	 Catch:{ all -> 0x015e }
        r13 = r13.get(r14);	 Catch:{ all -> 0x015e }
        r13 = (java.lang.Integer) r13;	 Catch:{ all -> 0x015e }
        if (r13 != 0) goto L_0x00bb;
    L_0x009a:
        r10 = 1;
        r14 = r0.services;	 Catch:{ all -> 0x015e }
        r15 = r12.type;	 Catch:{ all -> 0x015e }
        r14.put(r15, r12);	 Catch:{ all -> 0x015e }
        r14 = r0.persistentServices;	 Catch:{ all -> 0x015e }
        r15 = r12.type;	 Catch:{ all -> 0x015e }
        r7 = r12.uid;	 Catch:{ all -> 0x015e }
        r7 = java.lang.Integer.valueOf(r7);	 Catch:{ all -> 0x015e }
        r14.put(r15, r7);	 Catch:{ all -> 0x015e }
        r7 = r0.mPersistentServicesFileDidNotExist;	 Catch:{ all -> 0x015e }
        if (r7 == 0) goto L_0x00b5;
    L_0x00b3:
        if (r6 != 0) goto L_0x00fa;
    L_0x00b5:
        r7 = r12.type;	 Catch:{ all -> 0x015e }
        r1.notifyListener(r7, r2, r8);	 Catch:{ all -> 0x015e }
        goto L_0x00fa;
    L_0x00bb:
        r7 = r13.intValue();	 Catch:{ all -> 0x015e }
        r14 = r12.uid;	 Catch:{ all -> 0x015e }
        if (r7 != r14) goto L_0x00cb;
    L_0x00c3:
        r7 = r0.services;	 Catch:{ all -> 0x015e }
        r14 = r12.type;	 Catch:{ all -> 0x015e }
        r7.put(r14, r12);	 Catch:{ all -> 0x015e }
        goto L_0x00fa;
    L_0x00cb:
        r7 = r12.uid;	 Catch:{ all -> 0x015e }
        r7 = r1.inSystemImage(r7);	 Catch:{ all -> 0x015e }
        if (r7 != 0) goto L_0x00df;
    L_0x00d3:
        r7 = r12.type;	 Catch:{ all -> 0x015e }
        r14 = r13.intValue();	 Catch:{ all -> 0x015e }
        r7 = r1.containsTypeAndUid(r3, r7, r14);	 Catch:{ all -> 0x015e }
        if (r7 != 0) goto L_0x00fa;
    L_0x00df:
        r7 = 1;
        r10 = r0.services;	 Catch:{ all -> 0x015e }
        r14 = r12.type;	 Catch:{ all -> 0x015e }
        r10.put(r14, r12);	 Catch:{ all -> 0x015e }
        r10 = r0.persistentServices;	 Catch:{ all -> 0x015e }
        r14 = r12.type;	 Catch:{ all -> 0x015e }
        r15 = r12.uid;	 Catch:{ all -> 0x015e }
        r15 = java.lang.Integer.valueOf(r15);	 Catch:{ all -> 0x015e }
        r10.put(r14, r15);	 Catch:{ all -> 0x015e }
        r10 = r12.type;	 Catch:{ all -> 0x015e }
        r1.notifyListener(r10, r2, r8);	 Catch:{ all -> 0x015e }
        r10 = r7;
    L_0x00fa:
        goto L_0x0082;
    L_0x00fb:
        r7 = com.google.android.collect.Lists.newArrayList();	 Catch:{ all -> 0x015e }
        r8 = r0.persistentServices;	 Catch:{ all -> 0x015e }
        r8 = r8.keySet();	 Catch:{ all -> 0x015e }
        r8 = r8.iterator();	 Catch:{ all -> 0x015e }
    L_0x0109:
        r11 = r8.hasNext();	 Catch:{ all -> 0x015e }
        if (r11 == 0) goto L_0x0134;
    L_0x010f:
        r11 = r8.next();	 Catch:{ all -> 0x015e }
        r12 = r1.containsType(r3, r11);	 Catch:{ all -> 0x015e }
        if (r12 != 0) goto L_0x0131;
    L_0x0119:
        r12 = r0.persistentServices;	 Catch:{ all -> 0x015e }
        r12 = r12.get(r11);	 Catch:{ all -> 0x015e }
        r12 = (java.lang.Integer) r12;	 Catch:{ all -> 0x015e }
        r12 = r12.intValue();	 Catch:{ all -> 0x015e }
        r13 = r17;
        r12 = r1.containsUid(r13, r12);	 Catch:{ all -> 0x0163 }
        if (r12 == 0) goto L_0x0133;
    L_0x012d:
        r7.add(r11);	 Catch:{ all -> 0x0163 }
        goto L_0x0133;
    L_0x0131:
        r13 = r17;
    L_0x0133:
        goto L_0x0109;
    L_0x0134:
        r13 = r17;
        r8 = r7.iterator();	 Catch:{ all -> 0x0163 }
    L_0x013a:
        r11 = r8.hasNext();	 Catch:{ all -> 0x0163 }
        if (r11 == 0) goto L_0x0154;
    L_0x0140:
        r11 = r8.next();	 Catch:{ all -> 0x0163 }
        r10 = 1;
        r12 = r0.persistentServices;	 Catch:{ all -> 0x0163 }
        r12.remove(r11);	 Catch:{ all -> 0x0163 }
        r12 = r0.services;	 Catch:{ all -> 0x0163 }
        r12.remove(r11);	 Catch:{ all -> 0x0163 }
        r12 = 1;
        r1.notifyListener(r11, r2, r12);	 Catch:{ all -> 0x0163 }
        goto L_0x013a;
    L_0x0154:
        if (r10 == 0) goto L_0x015c;
    L_0x0156:
        r1.onServicesChangedLocked(r2);	 Catch:{ all -> 0x0163 }
        r1.writePersistentServicesLocked(r0, r2);	 Catch:{ all -> 0x0163 }
    L_0x015c:
        monitor-exit(r5);	 Catch:{ all -> 0x0163 }
        return;
    L_0x015e:
        r0 = move-exception;
        r13 = r17;
    L_0x0161:
        monitor-exit(r5);	 Catch:{ all -> 0x0163 }
        throw r0;
    L_0x0163:
        r0 = move-exception;
        goto L_0x0161;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.RegisteredServicesCache.generateServicesMap(int[], int):void");
    }

    /* Access modifiers changed, original: protected */
    public void onServicesChangedLocked(int userId) {
    }

    private boolean containsUid(int[] changedUids, int uid) {
        return changedUids == null || ArrayUtils.contains(changedUids, uid);
    }

    private boolean containsType(ArrayList<ServiceInfo<V>> serviceInfos, V type) {
        int N = serviceInfos.size();
        for (int i = 0; i < N; i++) {
            if (((ServiceInfo) serviceInfos.get(i)).type.equals(type)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsTypeAndUid(ArrayList<ServiceInfo<V>> serviceInfos, V type, int uid) {
        int N = serviceInfos.size();
        for (int i = 0; i < N; i++) {
            ServiceInfo<V> serviceInfo = (ServiceInfo) serviceInfos.get(i);
            if (serviceInfo.type.equals(type) && serviceInfo.uid == uid) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public ServiceInfo<V> parseServiceInfo(ResolveInfo service) throws XmlPullParserException, IOException {
        ServiceInfo si = service.serviceInfo;
        ComponentName componentName = new ComponentName(si.packageName, si.name);
        PackageManager pm = this.mContext.getPackageManager();
        XmlResourceParser parser = null;
        try {
            parser = si.loadXmlMetaData(pm, this.mMetaDataName);
            if (parser != null) {
                AttributeSet attrs = Xml.asAttributeSet(parser);
                while (true) {
                    int next = parser.next();
                    int type = next;
                    if (next == 1 || type == 2) {
                    }
                }
                if (this.mAttributesName.equals(parser.getName())) {
                    V v = parseServiceAttributes(pm.getResourcesForApplication(si.applicationInfo), si.packageName, attrs);
                    if (v == null) {
                        parser.close();
                        return null;
                    }
                    ServiceInfo serviceInfo = new ServiceInfo(v, service.serviceInfo, componentName);
                    parser.close();
                    return serviceInfo;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Meta-data does not start with ");
                stringBuilder.append(this.mAttributesName);
                stringBuilder.append(" tag");
                throw new XmlPullParserException(stringBuilder.toString());
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("No ");
            stringBuilder2.append(this.mMetaDataName);
            stringBuilder2.append(" meta-data");
            throw new XmlPullParserException(stringBuilder2.toString());
        } catch (NameNotFoundException e) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Unable to load resources for pacakge ");
            stringBuilder3.append(si.packageName);
            throw new XmlPullParserException(stringBuilder3.toString());
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
        }
    }

    private void readPersistentServicesLocked(InputStream is) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, StandardCharsets.UTF_8.name());
        int eventType = parser.getEventType();
        while (eventType != 2 && eventType != 1) {
            eventType = parser.next();
        }
        if ("services".equals(parser.getName())) {
            eventType = parser.next();
            do {
                if (eventType == 2 && parser.getDepth() == 2) {
                    if ("service".equals(parser.getName())) {
                        V service = this.mSerializerAndParser.createFromXml(parser);
                        if (service != null) {
                            int uid = Integer.parseInt(parser.getAttributeValue(null, GrantCredentialsPermissionActivity.EXTRAS_REQUESTING_UID));
                            findOrCreateUserLocked(UserHandle.getUserId(uid), null).persistentServices.put(service, Integer.valueOf(uid));
                        } else {
                            return;
                        }
                    }
                }
                eventType = parser.next();
            } while (eventType != 1);
        }
    }

    private void migrateIfNecessaryLocked() {
        String str = TAG;
        if (this.mSerializerAndParser != null) {
            File syncDir = new File(new File(getDataDirectory(), "system"), REGISTERED_SERVICES_DIR);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.mInterfaceName);
            stringBuilder.append(".xml");
            AtomicFile oldFile = new AtomicFile(new File(syncDir, stringBuilder.toString()));
            if (oldFile.getBaseFile().exists()) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(this.mInterfaceName);
                stringBuilder2.append(".xml.migrated");
                File marker = new File(syncDir, stringBuilder2.toString());
                if (!marker.exists()) {
                    InputStream is = null;
                    try {
                        is = oldFile.openRead();
                        this.mUserServices.clear();
                        readPersistentServicesLocked(is);
                    } catch (Exception e) {
                        Log.w(str, "Error reading persistent services, starting from scratch", e);
                    } catch (Throwable th) {
                        IoUtils.closeQuietly(is);
                    }
                    IoUtils.closeQuietly(is);
                    try {
                        for (UserInfo user : getUsers()) {
                            UserServices<V> userServices = (UserServices) this.mUserServices.get(user.id);
                            if (userServices != null) {
                                writePersistentServicesLocked(userServices, user.id);
                            }
                        }
                        marker.createNewFile();
                    } catch (Exception e2) {
                        Log.w(str, "Migration failed", e2);
                    }
                    this.mUserServices.clear();
                }
            }
        }
    }

    private void writePersistentServicesLocked(UserServices<V> user, int userId) {
        String str = "service";
        String str2 = "services";
        if (this.mSerializerAndParser != null) {
            AtomicFile atomicFile = createFileForUser(userId);
            try {
                FileOutputStream fos = atomicFile.startWrite();
                XmlSerializer out = new FastXmlSerializer();
                out.setOutput(fos, StandardCharsets.UTF_8.name());
                out.startDocument(null, Boolean.valueOf(true));
                out.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                out.startTag(null, str2);
                for (Entry<V, Integer> service : user.persistentServices.entrySet()) {
                    out.startTag(null, str);
                    out.attribute(null, GrantCredentialsPermissionActivity.EXTRAS_REQUESTING_UID, Integer.toString(((Integer) service.getValue()).intValue()));
                    this.mSerializerAndParser.writeAsXml(service.getKey(), out);
                    out.endTag(null, str);
                }
                out.endTag(null, str2);
                out.endDocument();
                atomicFile.finishWrite(fos);
            } catch (IOException e1) {
                Log.w(TAG, "Error writing accounts", e1);
                if (null != null) {
                    atomicFile.failWrite(null);
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public void onUserRemoved(int userId) {
        synchronized (this.mServicesLock) {
            this.mUserServices.remove(userId);
        }
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public List<UserInfo> getUsers() {
        return UserManager.get(this.mContext).getUsers(true);
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public UserInfo getUser(int userId) {
        return UserManager.get(this.mContext).getUserInfo(userId);
    }

    private AtomicFile createFileForUser(int userId) {
        File userDir = getUserSystemDirectory(userId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("registered_services/");
        stringBuilder.append(this.mInterfaceName);
        stringBuilder.append(".xml");
        return new AtomicFile(new File(userDir, stringBuilder.toString()));
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public File getUserSystemDirectory(int userId) {
        return Environment.getUserSystemDirectory(userId);
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public File getDataDirectory() {
        return Environment.getDataDirectory();
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public Map<V, Integer> getPersistentServices(int userId) {
        return findOrCreateUserLocked(userId).persistentServices;
    }
}
