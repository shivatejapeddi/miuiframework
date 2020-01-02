package android.content.pm;

import android.Manifest.permission;
import android.annotation.UnsupportedAppUsage;
import android.apex.ApexInfo;
import android.app.ActivityTaskManager;
import android.app.ActivityThread;
import android.app.ResourcesManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo.WindowLayout;
import android.content.pm.PackageParserCacheHelper.ReadHelper;
import android.content.pm.PackageParserCacheHelper.WriteHelper;
import android.content.pm.split.DefaultSplitAssetLoader;
import android.content.pm.split.SplitAssetDependencyLoader;
import android.content.pm.split.SplitAssetLoader;
import android.content.pm.split.SplitDependencyLoader;
import android.content.pm.split.SplitDependencyLoader.IllegalDependencyException;
import android.content.res.AssetManager;
import android.content.res.MiuiResources;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.IncidentManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PatternMatcher;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.security.keystore.KeyProperties;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.ByteStringUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.PackageUtils;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TypedValue;
import android.util.apk.ApkSignatureVerifier;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.DumpHeapActivity;
import com.android.internal.os.ClassLoaderFactory;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.XmlUtils;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import libcore.io.IoUtils;
import libcore.util.EmptyArray;
import org.apache.miui.commons.lang3.ClassUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class PackageParser {
    public static final String ANDROID_MANIFEST_FILENAME = "AndroidManifest.xml";
    private static final String ANDROID_RESOURCES = "http://schemas.android.com/apk/res/android";
    public static final String APK_FILE_EXTENSION = ".apk";
    private static final Set<String> CHILD_PACKAGE_TAGS = new ArraySet();
    private static final boolean DEBUG_BACKUP = false;
    private static final boolean DEBUG_JAR = false;
    private static final boolean DEBUG_PARSER = false;
    private static final int DEFAULT_MIN_SDK_VERSION = 1;
    private static final float DEFAULT_PRE_O_MAX_ASPECT_RATIO = 1.86f;
    private static final float DEFAULT_PRE_Q_MIN_ASPECT_RATIO = 1.333f;
    private static final float DEFAULT_PRE_Q_MIN_ASPECT_RATIO_WATCH = 1.0f;
    private static final int DEFAULT_TARGET_SDK_VERSION = 0;
    private static final boolean LOG_PARSE_TIMINGS = Build.IS_DEBUGGABLE;
    private static final int LOG_PARSE_TIMINGS_THRESHOLD_MS = 100;
    private static final boolean LOG_UNSAFE_BROADCASTS = false;
    private static final String METADATA_MAX_ASPECT_RATIO = "android.max_aspect";
    private static final String MNT_EXPAND = "/mnt/expand/";
    private static final boolean MULTI_PACKAGE_APK_ENABLED;
    @UnsupportedAppUsage
    public static final NewPermissionInfo[] NEW_PERMISSIONS = new NewPermissionInfo[]{new NewPermissionInfo(permission.WRITE_EXTERNAL_STORAGE, 4, 0), new NewPermissionInfo(permission.READ_PHONE_STATE, 4, 0)};
    public static final int PARSE_CHATTY = Integer.MIN_VALUE;
    public static final int PARSE_COLLECT_CERTIFICATES = 32;
    private static final int PARSE_DEFAULT_INSTALL_LOCATION = -1;
    private static final int PARSE_DEFAULT_TARGET_SANDBOX = 1;
    public static final int PARSE_ENFORCE_CODE = 64;
    public static final int PARSE_EXTERNAL_STORAGE = 8;
    public static final int PARSE_IGNORE_PROCESSES = 2;
    public static final int PARSE_IS_SYSTEM_DIR = 16;
    public static final int PARSE_MUST_BE_APK = 1;
    private static final String PROPERTY_CHILD_PACKAGES_ENABLED = "persist.sys.child_packages_enabled";
    private static final int RECREATE_ON_CONFIG_CHANGES_MASK = 3;
    private static final boolean RIGID_PARSER = false;
    private static final Set<String> SAFE_BROADCASTS = new ArraySet();
    private static final String[] SDK_CODENAMES = VERSION.ACTIVE_CODENAMES;
    private static final int SDK_VERSION = VERSION.SDK_INT;
    private static final String TAG = "PackageParser";
    private static final String TAG_ADOPT_PERMISSIONS = "adopt-permissions";
    private static final String TAG_APPLICATION = "application";
    private static final String TAG_COMPATIBLE_SCREENS = "compatible-screens";
    private static final String TAG_EAT_COMMENT = "eat-comment";
    private static final String TAG_FEATURE_GROUP = "feature-group";
    private static final String TAG_INSTRUMENTATION = "instrumentation";
    private static final String TAG_KEY_SETS = "key-sets";
    private static final String TAG_MANIFEST = "manifest";
    private static final String TAG_ORIGINAL_PACKAGE = "original-package";
    private static final String TAG_OVERLAY = "overlay";
    private static final String TAG_PACKAGE = "package";
    private static final String TAG_PACKAGE_VERIFIER = "package-verifier";
    private static final String TAG_PERMISSION = "permission";
    private static final String TAG_PERMISSION_GROUP = "permission-group";
    private static final String TAG_PERMISSION_TREE = "permission-tree";
    private static final String TAG_PROTECTED_BROADCAST = "protected-broadcast";
    private static final String TAG_RESTRICT_UPDATE = "restrict-update";
    private static final String TAG_SUPPORTS_INPUT = "supports-input";
    private static final String TAG_SUPPORT_SCREENS = "supports-screens";
    private static final String TAG_USES_CONFIGURATION = "uses-configuration";
    private static final String TAG_USES_FEATURE = "uses-feature";
    private static final String TAG_USES_GL_TEXTURE = "uses-gl-texture";
    private static final String TAG_USES_PERMISSION = "uses-permission";
    private static final String TAG_USES_PERMISSION_SDK_23 = "uses-permission-sdk-23";
    private static final String TAG_USES_PERMISSION_SDK_M = "uses-permission-sdk-m";
    private static final String TAG_USES_SDK = "uses-sdk";
    private static final String TAG_USES_SPLIT = "uses-split";
    public static final AtomicInteger sCachedPackageReadCount = new AtomicInteger();
    private static boolean sCompatibilityModeEnabled = true;
    private static final Comparator<String> sSplitNameComparator = new SplitNameComparator();
    private static boolean sUseRoundIcon = false;
    @Deprecated
    private String mArchiveSourcePath;
    private File mCacheDir;
    @UnsupportedAppUsage
    private Callback mCallback;
    private DisplayMetrics mMetrics = new DisplayMetrics();
    private boolean mOnlyCoreApps;
    private int mParseError = 1;
    private ParsePackageItemArgs mParseInstrumentationArgs;
    private String[] mSeparateProcesses;

    public static abstract class Component<II extends IntentInfo> {
        @UnsupportedAppUsage
        public final String className;
        ComponentName componentName;
        String componentShortName;
        @UnsupportedAppUsage
        public final ArrayList<II> intents;
        @UnsupportedAppUsage
        public Bundle metaData;
        public int order;
        @UnsupportedAppUsage
        public Package owner;

        public Component(Package owner, ArrayList<II> intents, String className) {
            this.owner = owner;
            this.intents = intents;
            this.className = className;
        }

        public Component(Package owner) {
            this.owner = owner;
            this.intents = null;
            this.className = null;
        }

        public Component(ParsePackageItemArgs args, PackageItemInfo outInfo) {
            ParsePackageItemArgs parsePackageItemArgs = args;
            this.owner = parsePackageItemArgs.owner;
            this.intents = new ArrayList(0);
            if (PackageParser.parsePackageItemInfo(parsePackageItemArgs.owner, outInfo, parsePackageItemArgs.outError, parsePackageItemArgs.tag, parsePackageItemArgs.sa, true, parsePackageItemArgs.nameRes, parsePackageItemArgs.labelRes, parsePackageItemArgs.iconRes, parsePackageItemArgs.roundIconRes, parsePackageItemArgs.logoRes, parsePackageItemArgs.bannerRes)) {
                this.className = outInfo.name;
                return;
            }
            PackageItemInfo packageItemInfo = outInfo;
            this.className = null;
        }

        public Component(ParseComponentArgs args, ComponentInfo outInfo) {
            this((ParsePackageItemArgs) args, (PackageItemInfo) outInfo);
            if (args.outError[0] == null) {
                if (args.processRes != 0) {
                    CharSequence pname;
                    if (this.owner.applicationInfo.targetSdkVersion >= 8) {
                        pname = args.sa.getNonConfigurationString(args.processRes, 1024);
                    } else {
                        pname = args.sa.getNonResourceString(args.processRes);
                    }
                    outInfo.processName = PackageParser.buildProcessName(this.owner.applicationInfo.packageName, this.owner.applicationInfo.processName, pname, args.flags, args.sepProcesses, args.outError);
                }
                if (args.descriptionRes != 0) {
                    outInfo.descriptionRes = args.sa.getResourceId(args.descriptionRes, 0);
                }
                outInfo.enabled = args.sa.getBoolean(args.enabledRes, true);
            }
        }

        public Component(Component<II> clone) {
            this.owner = clone.owner;
            this.intents = clone.intents;
            this.className = clone.className;
            this.componentName = clone.componentName;
            this.componentShortName = clone.componentShortName;
        }

        @UnsupportedAppUsage
        public ComponentName getComponentName() {
            ComponentName componentName = this.componentName;
            if (componentName != null) {
                return componentName;
            }
            if (this.className != null) {
                this.componentName = new ComponentName(this.owner.applicationInfo.packageName, this.className);
            }
            return this.componentName;
        }

        protected Component(Parcel in) {
            this.className = in.readString();
            this.metaData = in.readBundle();
            this.intents = createIntentsList(in);
            this.owner = null;
        }

        /* Access modifiers changed, original: protected */
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.className);
            dest.writeBundle(this.metaData);
            writeIntentsList(this.intents, dest, flags);
        }

        private static void writeIntentsList(ArrayList<? extends IntentInfo> list, Parcel out, int flags) {
            if (list == null) {
                out.writeInt(-1);
                return;
            }
            int N = list.size();
            out.writeInt(N);
            if (N > 0) {
                out.writeString(((IntentInfo) list.get(0)).getClass().getName());
                for (int i = 0; i < N; i++) {
                    ((IntentInfo) list.get(i)).writeIntentInfoToParcel(out, flags);
                }
            }
        }

        private static <T extends IntentInfo> ArrayList<T> createIntentsList(Parcel in) {
            int N = in.readInt();
            if (N == -1) {
                return null;
            }
            if (N == 0) {
                return new ArrayList(0);
            }
            String componentName = in.readString();
            try {
                Constructor<T> cons = Class.forName(componentName).getConstructor(new Class[]{Parcel.class});
                ArrayList<T> intentsList = new ArrayList(N);
                for (int i = 0; i < N; i++) {
                    intentsList.add((IntentInfo) cons.newInstance(new Object[]{in}));
                }
                return intentsList;
            } catch (ReflectiveOperationException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to construct intent list for: ");
                stringBuilder.append(componentName);
                throw new AssertionError(stringBuilder.toString());
            }
        }

        public void appendComponentShortName(StringBuilder sb) {
            ComponentName.appendShortString(sb, this.owner.applicationInfo.packageName, this.className);
        }

        public void printComponentShortName(PrintWriter pw) {
            ComponentName.printShortString(pw, this.owner.applicationInfo.packageName, this.className);
        }

        public void setPackageName(String packageName) {
            this.componentName = null;
            this.componentShortName = null;
        }
    }

    public static final class Activity extends Component<ActivityIntentInfo> implements Parcelable {
        public static final Creator CREATOR = new Creator<Activity>() {
            public Activity createFromParcel(Parcel in) {
                return new Activity(in, null);
            }

            public Activity[] newArray(int size) {
                return new Activity[size];
            }
        };
        @UnsupportedAppUsage
        public final ActivityInfo info;
        private boolean mHasMaxAspectRatio;
        private boolean mHasMinAspectRatio;

        private boolean hasMaxAspectRatio() {
            return this.mHasMaxAspectRatio;
        }

        private boolean hasMinAspectRatio() {
            return this.mHasMinAspectRatio;
        }

        Activity(Package owner, String className, ActivityInfo info) {
            super(owner, new ArrayList(0), className);
            this.info = info;
            this.info.applicationInfo = owner.applicationInfo;
        }

        public Activity(ParseComponentArgs args, ActivityInfo _info) {
            super(args, (ComponentInfo) _info);
            this.info = _info;
            this.info.applicationInfo = args.owner.applicationInfo;
        }

        public void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        private void setMaxAspectRatio(float maxAspectRatio) {
            if (this.info.resizeMode != 2 && this.info.resizeMode != 1) {
                if (maxAspectRatio >= 1.0f || maxAspectRatio == 0.0f) {
                    this.info.maxAspectRatio = maxAspectRatio;
                    this.mHasMaxAspectRatio = true;
                }
            }
        }

        private void setMinAspectRatio(float minAspectRatio) {
            if (this.info.resizeMode != 2 && this.info.resizeMode != 1) {
                if (minAspectRatio >= 1.0f || minAspectRatio == 0.0f) {
                    this.info.minAspectRatio = minAspectRatio;
                    this.mHasMinAspectRatio = true;
                }
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Activity{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.info, flags | 2);
            dest.writeBoolean(this.mHasMaxAspectRatio);
            dest.writeBoolean(this.mHasMinAspectRatio);
        }

        private Activity(Parcel in) {
            super(in);
            this.info = (ActivityInfo) in.readParcelable(Object.class.getClassLoader());
            this.mHasMaxAspectRatio = in.readBoolean();
            this.mHasMinAspectRatio = in.readBoolean();
            Iterator it = this.intents.iterator();
            while (it.hasNext()) {
                ActivityIntentInfo aii = (ActivityIntentInfo) it.next();
                aii.activity = this;
                this.order = Math.max(aii.getOrder(), this.order);
            }
            if (this.info.permission != null) {
                ActivityInfo activityInfo = this.info;
                activityInfo.permission = activityInfo.permission.intern();
            }
        }
    }

    public static abstract class IntentInfo extends IntentFilter {
        @UnsupportedAppUsage
        public int banner;
        @UnsupportedAppUsage
        public boolean hasDefault;
        @UnsupportedAppUsage
        public int icon;
        @UnsupportedAppUsage
        public int labelRes;
        @UnsupportedAppUsage
        public int logo;
        @UnsupportedAppUsage
        public CharSequence nonLocalizedLabel;
        public int preferred;

        @UnsupportedAppUsage
        protected IntentInfo() {
        }

        protected IntentInfo(Parcel dest) {
            super(dest);
            boolean z = true;
            if (dest.readInt() != 1) {
                z = false;
            }
            this.hasDefault = z;
            this.labelRes = dest.readInt();
            this.nonLocalizedLabel = dest.readCharSequence();
            this.icon = dest.readInt();
            this.logo = dest.readInt();
            this.banner = dest.readInt();
            this.preferred = dest.readInt();
        }

        public void writeIntentInfoToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.hasDefault);
            dest.writeInt(this.labelRes);
            dest.writeCharSequence(this.nonLocalizedLabel);
            dest.writeInt(this.icon);
            dest.writeInt(this.logo);
            dest.writeInt(this.banner);
            dest.writeInt(this.preferred);
        }
    }

    public static final class ActivityIntentInfo extends IntentInfo {
        @UnsupportedAppUsage
        public Activity activity;

        public ActivityIntentInfo(Activity _activity) {
            this.activity = _activity;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("ActivityIntentInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            this.activity.appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        public ActivityIntentInfo(Parcel in) {
            super(in);
        }
    }

    public static class ApkLite {
        public final String codePath;
        public final String configForSplit;
        public final boolean coreApp;
        public final boolean debuggable;
        public final boolean extractNativeLibs;
        public final int installLocation;
        public boolean isFeatureSplit;
        public final boolean isSplitRequired;
        public final boolean isolatedSplits;
        public final int minSdkVersion;
        public final boolean multiArch;
        public final String packageName;
        public final int revisionCode;
        public final SigningDetails signingDetails;
        public final String splitName;
        public final int targetSdkVersion;
        public final boolean use32bitAbi;
        public final boolean useEmbeddedDex;
        public final String usesSplitName;
        public final VerifierInfo[] verifiers;
        public final int versionCode;
        public final int versionCodeMajor;

        public ApkLite(String codePath, String packageName, String splitName, boolean isFeatureSplit, String configForSplit, String usesSplitName, boolean isSplitRequired, int versionCode, int versionCodeMajor, int revisionCode, int installLocation, List<VerifierInfo> verifiers, SigningDetails signingDetails, boolean coreApp, boolean debuggable, boolean multiArch, boolean use32bitAbi, boolean useEmbeddedDex, boolean extractNativeLibs, boolean isolatedSplits, int minSdkVersion, int targetSdkVersion) {
            this.codePath = codePath;
            this.packageName = packageName;
            this.splitName = splitName;
            this.isFeatureSplit = isFeatureSplit;
            this.configForSplit = configForSplit;
            this.usesSplitName = usesSplitName;
            this.versionCode = versionCode;
            this.versionCodeMajor = versionCodeMajor;
            this.revisionCode = revisionCode;
            this.installLocation = installLocation;
            this.signingDetails = signingDetails;
            this.verifiers = (VerifierInfo[]) verifiers.toArray(new VerifierInfo[verifiers.size()]);
            this.coreApp = coreApp;
            this.debuggable = debuggable;
            this.multiArch = multiArch;
            this.use32bitAbi = use32bitAbi;
            this.useEmbeddedDex = useEmbeddedDex;
            this.extractNativeLibs = extractNativeLibs;
            this.isolatedSplits = isolatedSplits;
            this.isSplitRequired = isSplitRequired;
            this.minSdkVersion = minSdkVersion;
            this.targetSdkVersion = targetSdkVersion;
        }

        public long getLongVersionCode() {
            return PackageInfo.composeLongVersionCode(this.versionCodeMajor, this.versionCode);
        }
    }

    private static class CachedComponentArgs {
        ParseComponentArgs mActivityAliasArgs;
        ParseComponentArgs mActivityArgs;
        ParseComponentArgs mProviderArgs;
        ParseComponentArgs mServiceArgs;

        private CachedComponentArgs() {
        }
    }

    public interface Callback {
        String[] getOverlayApks(String str);

        String[] getOverlayPaths(String str, String str2);

        boolean hasFeature(String str);
    }

    public static final class CallbackImpl implements Callback {
        private final PackageManager mPm;

        public CallbackImpl(PackageManager pm) {
            this.mPm = pm;
        }

        public boolean hasFeature(String feature) {
            return this.mPm.hasSystemFeature(feature);
        }

        public String[] getOverlayPaths(String targetPackageName, String targetPath) {
            return null;
        }

        public String[] getOverlayApks(String targetPackageName) {
            return null;
        }
    }

    public static final class Instrumentation extends Component<IntentInfo> implements Parcelable {
        public static final Creator CREATOR = new Creator<Instrumentation>() {
            public Instrumentation createFromParcel(Parcel in) {
                return new Instrumentation(in, null);
            }

            public Instrumentation[] newArray(int size) {
                return new Instrumentation[size];
            }
        };
        @UnsupportedAppUsage
        public final InstrumentationInfo info;

        public Instrumentation(ParsePackageItemArgs args, InstrumentationInfo _info) {
            super(args, (PackageItemInfo) _info);
            this.info = _info;
        }

        public void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Instrumentation{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.info, flags);
        }

        private Instrumentation(Parcel in) {
            InstrumentationInfo instrumentationInfo;
            super(in);
            this.info = (InstrumentationInfo) in.readParcelable(Object.class.getClassLoader());
            if (this.info.targetPackage != null) {
                instrumentationInfo = this.info;
                instrumentationInfo.targetPackage = instrumentationInfo.targetPackage.intern();
            }
            if (this.info.targetProcesses != null) {
                instrumentationInfo = this.info;
                instrumentationInfo.targetProcesses = instrumentationInfo.targetProcesses.intern();
            }
        }
    }

    public static class NewPermissionInfo {
        public final int fileVersion;
        @UnsupportedAppUsage
        public final String name;
        @UnsupportedAppUsage
        public final int sdkVersion;

        public NewPermissionInfo(String name, int sdkVersion, int fileVersion) {
            this.name = name;
            this.sdkVersion = sdkVersion;
            this.fileVersion = fileVersion;
        }
    }

    public static final class Package implements Parcelable {
        public static final Creator CREATOR = new Creator<Package>() {
            public Package createFromParcel(Parcel in) {
                return new Package(in);
            }

            public Package[] newArray(int size) {
                return new Package[size];
            }
        };
        @UnsupportedAppUsage
        public final ArrayList<Activity> activities;
        @UnsupportedAppUsage
        public ApplicationInfo applicationInfo;
        public String baseCodePath;
        public boolean baseHardwareAccelerated;
        public int baseRevisionCode;
        public ArrayList<Package> childPackages;
        public String codePath;
        @UnsupportedAppUsage
        public ArrayList<ConfigurationInfo> configPreferences;
        public boolean coreApp;
        public String cpuAbiOverride;
        public ArrayList<FeatureGroupInfo> featureGroups;
        public final ArrayList<String> implicitPermissions;
        @UnsupportedAppUsage
        public int installLocation;
        @UnsupportedAppUsage
        public final ArrayList<Instrumentation> instrumentation;
        public boolean isStub;
        public ArrayList<String> libraryNames;
        public ArrayList<String> mAdoptPermissions;
        @UnsupportedAppUsage
        public Bundle mAppMetaData;
        public int mCompileSdkVersion;
        public String mCompileSdkVersionCodename;
        @UnsupportedAppUsage
        public Object mExtras;
        @UnsupportedAppUsage
        public ArrayMap<String, ArraySet<PublicKey>> mKeySetMapping;
        public long[] mLastPackageUsageTimeInMills;
        public ArrayList<String> mOriginalPackages;
        public String mOverlayCategory;
        public boolean mOverlayIsStatic;
        public int mOverlayPriority;
        public String mOverlayTarget;
        public String mOverlayTargetName;
        @UnsupportedAppUsage
        public int mPreferredOrder;
        public String mRealPackage;
        public String mRequiredAccountType;
        public boolean mRequiredForAllUsers;
        public String mRestrictedAccountType;
        @UnsupportedAppUsage
        public String mSharedUserId;
        @UnsupportedAppUsage
        public int mSharedUserLabel;
        @UnsupportedAppUsage
        public SigningDetails mSigningDetails;
        @UnsupportedAppUsage
        public ArraySet<String> mUpgradeKeySets;
        @UnsupportedAppUsage
        public int mVersionCode;
        public int mVersionCodeMajor;
        @UnsupportedAppUsage
        public String mVersionName;
        public String manifestPackageName;
        @UnsupportedAppUsage
        public String packageName;
        public Package parentPackage;
        @UnsupportedAppUsage
        public final ArrayList<PermissionGroup> permissionGroups;
        @UnsupportedAppUsage
        public final ArrayList<Permission> permissions;
        public ArrayList<ActivityIntentInfo> preferredActivityFilters;
        @UnsupportedAppUsage
        public ArrayList<String> protectedBroadcasts;
        @UnsupportedAppUsage
        public final ArrayList<Provider> providers;
        @UnsupportedAppUsage
        public final ArrayList<Activity> receivers;
        @UnsupportedAppUsage
        public ArrayList<FeatureInfo> reqFeatures;
        @UnsupportedAppUsage
        public final ArrayList<String> requestedPermissions;
        public byte[] restrictUpdateHash;
        @UnsupportedAppUsage
        public final ArrayList<Service> services;
        public String[] splitCodePaths;
        public int[] splitFlags;
        public String[] splitNames;
        public int[] splitPrivateFlags;
        public int[] splitRevisionCodes;
        public String staticSharedLibName;
        public long staticSharedLibVersion;
        public boolean use32bitAbi;
        @UnsupportedAppUsage
        public ArrayList<String> usesLibraries;
        @UnsupportedAppUsage
        public String[] usesLibraryFiles;
        public ArrayList<SharedLibraryInfo> usesLibraryInfos;
        @UnsupportedAppUsage
        public ArrayList<String> usesOptionalLibraries;
        public ArrayList<String> usesStaticLibraries;
        public String[][] usesStaticLibrariesCertDigests;
        public long[] usesStaticLibrariesVersions;
        public boolean visibleToInstantApps;
        public String volumeUuid;

        public long getLongVersionCode() {
            return PackageInfo.composeLongVersionCode(this.mVersionCodeMajor, this.mVersionCode);
        }

        @UnsupportedAppUsage
        public Package(String packageName) {
            this.applicationInfo = new ApplicationInfo();
            this.permissions = new ArrayList(0);
            this.permissionGroups = new ArrayList(0);
            this.activities = new ArrayList(0);
            this.receivers = new ArrayList(0);
            this.providers = new ArrayList(0);
            this.services = new ArrayList(0);
            this.instrumentation = new ArrayList(0);
            this.requestedPermissions = new ArrayList();
            this.implicitPermissions = new ArrayList();
            this.staticSharedLibName = null;
            this.staticSharedLibVersion = 0;
            this.libraryNames = null;
            this.usesLibraries = null;
            this.usesStaticLibraries = null;
            this.usesStaticLibrariesVersions = null;
            this.usesStaticLibrariesCertDigests = null;
            this.usesOptionalLibraries = null;
            this.usesLibraryFiles = null;
            this.usesLibraryInfos = null;
            this.preferredActivityFilters = null;
            this.mOriginalPackages = null;
            this.mRealPackage = null;
            this.mAdoptPermissions = null;
            this.mAppMetaData = null;
            this.mSigningDetails = SigningDetails.UNKNOWN;
            this.mPreferredOrder = 0;
            this.mLastPackageUsageTimeInMills = new long[8];
            this.configPreferences = null;
            this.reqFeatures = null;
            this.featureGroups = null;
            this.packageName = packageName;
            this.manifestPackageName = packageName;
            ApplicationInfo applicationInfo = this.applicationInfo;
            applicationInfo.packageName = packageName;
            applicationInfo.uid = -1;
        }

        public void setApplicationVolumeUuid(String volumeUuid) {
            UUID storageUuid = StorageManager.convert(volumeUuid);
            ApplicationInfo applicationInfo = this.applicationInfo;
            applicationInfo.volumeUuid = volumeUuid;
            applicationInfo.storageUuid = storageUuid;
            int packageCount = this.childPackages;
            if (packageCount != 0) {
                packageCount = packageCount.size();
                for (int i = 0; i < packageCount; i++) {
                    ((Package) this.childPackages.get(i)).applicationInfo.volumeUuid = volumeUuid;
                    ((Package) this.childPackages.get(i)).applicationInfo.storageUuid = storageUuid;
                }
            }
        }

        public void setApplicationInfoCodePath(String codePath) {
            this.applicationInfo.setCodePath(codePath);
            int packageCount = this.childPackages;
            if (packageCount != 0) {
                packageCount = packageCount.size();
                for (int i = 0; i < packageCount; i++) {
                    ((Package) this.childPackages.get(i)).applicationInfo.setCodePath(codePath);
                }
            }
        }

        @Deprecated
        public void setApplicationInfoResourcePath(String resourcePath) {
            this.applicationInfo.setResourcePath(resourcePath);
            int packageCount = this.childPackages;
            if (packageCount != 0) {
                packageCount = packageCount.size();
                for (int i = 0; i < packageCount; i++) {
                    ((Package) this.childPackages.get(i)).applicationInfo.setResourcePath(resourcePath);
                }
            }
        }

        @Deprecated
        public void setApplicationInfoBaseResourcePath(String resourcePath) {
            this.applicationInfo.setBaseResourcePath(resourcePath);
            int packageCount = this.childPackages;
            if (packageCount != 0) {
                packageCount = packageCount.size();
                for (int i = 0; i < packageCount; i++) {
                    ((Package) this.childPackages.get(i)).applicationInfo.setBaseResourcePath(resourcePath);
                }
            }
        }

        public void setApplicationInfoBaseCodePath(String baseCodePath) {
            this.applicationInfo.setBaseCodePath(baseCodePath);
            int packageCount = this.childPackages;
            if (packageCount != 0) {
                packageCount = packageCount.size();
                for (int i = 0; i < packageCount; i++) {
                    ((Package) this.childPackages.get(i)).applicationInfo.setBaseCodePath(baseCodePath);
                }
            }
        }

        public List<String> getChildPackageNames() {
            int childCount = this.childPackages;
            if (childCount == 0) {
                return null;
            }
            childCount = childCount.size();
            List<String> childPackageNames = new ArrayList(childCount);
            for (int i = 0; i < childCount; i++) {
                childPackageNames.add(((Package) this.childPackages.get(i)).packageName);
            }
            return childPackageNames;
        }

        public boolean hasChildPackage(String packageName) {
            ArrayList arrayList = this.childPackages;
            int childCount = arrayList != null ? arrayList.size() : 0;
            for (int i = 0; i < childCount; i++) {
                if (((Package) this.childPackages.get(i)).packageName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }

        public void setApplicationInfoSplitCodePaths(String[] splitCodePaths) {
            this.applicationInfo.setSplitCodePaths(splitCodePaths);
        }

        @Deprecated
        public void setApplicationInfoSplitResourcePaths(String[] resroucePaths) {
            this.applicationInfo.setSplitResourcePaths(resroucePaths);
        }

        public void setSplitCodePaths(String[] codePaths) {
            this.splitCodePaths = codePaths;
        }

        public void setCodePath(String codePath) {
            this.codePath = codePath;
            int packageCount = this.childPackages;
            if (packageCount != 0) {
                packageCount = packageCount.size();
                for (int i = 0; i < packageCount; i++) {
                    ((Package) this.childPackages.get(i)).codePath = codePath;
                }
            }
        }

        public void setBaseCodePath(String baseCodePath) {
            this.baseCodePath = baseCodePath;
            int packageCount = this.childPackages;
            if (packageCount != 0) {
                packageCount = packageCount.size();
                for (int i = 0; i < packageCount; i++) {
                    ((Package) this.childPackages.get(i)).baseCodePath = baseCodePath;
                }
            }
        }

        public void setSigningDetails(SigningDetails signingDetails) {
            this.mSigningDetails = signingDetails;
            int packageCount = this.childPackages;
            if (packageCount != 0) {
                packageCount = packageCount.size();
                for (int i = 0; i < packageCount; i++) {
                    ((Package) this.childPackages.get(i)).mSigningDetails = signingDetails;
                }
            }
        }

        public void setVolumeUuid(String volumeUuid) {
            this.volumeUuid = volumeUuid;
            int packageCount = this.childPackages;
            if (packageCount != 0) {
                packageCount = packageCount.size();
                for (int i = 0; i < packageCount; i++) {
                    ((Package) this.childPackages.get(i)).volumeUuid = volumeUuid;
                }
            }
        }

        public void setApplicationInfoFlags(int mask, int flags) {
            ApplicationInfo applicationInfo = this.applicationInfo;
            applicationInfo.flags = (applicationInfo.flags & (~mask)) | (mask & flags);
            int packageCount = this.childPackages;
            if (packageCount != 0) {
                packageCount = packageCount.size();
                for (int i = 0; i < packageCount; i++) {
                    ((Package) this.childPackages.get(i)).applicationInfo.flags = (this.applicationInfo.flags & (~mask)) | (mask & flags);
                }
            }
        }

        public void setUse32bitAbi(boolean use32bitAbi) {
            this.use32bitAbi = use32bitAbi;
            int packageCount = this.childPackages;
            if (packageCount != 0) {
                packageCount = packageCount.size();
                for (int i = 0; i < packageCount; i++) {
                    ((Package) this.childPackages.get(i)).use32bitAbi = use32bitAbi;
                }
            }
        }

        public boolean isLibrary() {
            return (this.staticSharedLibName == null && ArrayUtils.isEmpty(this.libraryNames)) ? false : true;
        }

        public List<String> getAllCodePaths() {
            ArrayList<String> paths = new ArrayList();
            paths.add(this.baseCodePath);
            if (!ArrayUtils.isEmpty(this.splitCodePaths)) {
                Collections.addAll(paths, this.splitCodePaths);
            }
            return paths;
        }

        public List<String> getAllCodePathsExcludingResourceOnly() {
            ArrayList<String> paths = new ArrayList();
            if ((this.applicationInfo.flags & 4) != 0) {
                paths.add(this.baseCodePath);
            }
            if (!ArrayUtils.isEmpty(this.splitCodePaths)) {
                int i = 0;
                while (true) {
                    String[] strArr = this.splitCodePaths;
                    if (i >= strArr.length) {
                        break;
                    }
                    if ((this.splitFlags[i] & 4) != 0) {
                        paths.add(strArr[i]);
                    }
                    i++;
                }
            }
            return paths;
        }

        @UnsupportedAppUsage
        public void setPackageName(String newName) {
            int i;
            this.packageName = newName;
            this.applicationInfo.packageName = newName;
            for (i = this.permissions.size() - 1; i >= 0; i--) {
                ((Permission) this.permissions.get(i)).setPackageName(newName);
            }
            for (i = this.permissionGroups.size() - 1; i >= 0; i--) {
                ((PermissionGroup) this.permissionGroups.get(i)).setPackageName(newName);
            }
            for (i = this.activities.size() - 1; i >= 0; i--) {
                ((Activity) this.activities.get(i)).setPackageName(newName);
            }
            for (i = this.receivers.size() - 1; i >= 0; i--) {
                ((Activity) this.receivers.get(i)).setPackageName(newName);
            }
            for (i = this.providers.size() - 1; i >= 0; i--) {
                ((Provider) this.providers.get(i)).setPackageName(newName);
            }
            for (i = this.services.size() - 1; i >= 0; i--) {
                ((Service) this.services.get(i)).setPackageName(newName);
            }
            for (i = this.instrumentation.size() - 1; i >= 0; i--) {
                ((Instrumentation) this.instrumentation.get(i)).setPackageName(newName);
            }
        }

        public boolean hasComponentClassName(String name) {
            int i;
            for (i = this.activities.size() - 1; i >= 0; i--) {
                if (name.equals(((Activity) this.activities.get(i)).className)) {
                    return true;
                }
            }
            for (i = this.receivers.size() - 1; i >= 0; i--) {
                if (name.equals(((Activity) this.receivers.get(i)).className)) {
                    return true;
                }
            }
            for (i = this.providers.size() - 1; i >= 0; i--) {
                if (name.equals(((Provider) this.providers.get(i)).className)) {
                    return true;
                }
            }
            for (i = this.services.size() - 1; i >= 0; i--) {
                if (name.equals(((Service) this.services.get(i)).className)) {
                    return true;
                }
            }
            for (i = this.instrumentation.size() - 1; i >= 0; i--) {
                if (name.equals(((Instrumentation) this.instrumentation.get(i)).className)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isExternal() {
            return this.applicationInfo.isExternal();
        }

        public boolean isForwardLocked() {
            return false;
        }

        public boolean isOem() {
            return this.applicationInfo.isOem();
        }

        public boolean isVendor() {
            return this.applicationInfo.isVendor();
        }

        public boolean isProduct() {
            return this.applicationInfo.isProduct();
        }

        public boolean isProductServices() {
            return this.applicationInfo.isProductServices();
        }

        public boolean isOdm() {
            return this.applicationInfo.isOdm();
        }

        public boolean isPrivileged() {
            return this.applicationInfo.isPrivilegedApp();
        }

        public boolean isSystem() {
            return this.applicationInfo.isSystemApp();
        }

        public boolean isUpdatedSystemApp() {
            return this.applicationInfo.isUpdatedSystemApp();
        }

        public boolean canHaveOatDir() {
            return !isSystem() || isUpdatedSystemApp();
        }

        public boolean isMatch(int flags) {
            if ((1048576 & flags) != 0) {
                return isSystem();
            }
            return true;
        }

        public long getLatestPackageUseTimeInMills() {
            long latestUse = 0;
            for (long use : this.mLastPackageUsageTimeInMills) {
                latestUse = Math.max(latestUse, use);
            }
            return latestUse;
        }

        public long getLatestForegroundPackageUseTimeInMills() {
            long latestUse = 0;
            for (int reason : new int[]{0, 2}) {
                latestUse = Math.max(latestUse, this.mLastPackageUsageTimeInMills[reason]);
            }
            return latestUse;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Package{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(this.packageName);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        public int describeContents() {
            return 0;
        }

        public Package(Parcel dest) {
            this.applicationInfo = new ApplicationInfo();
            boolean z = false;
            this.permissions = new ArrayList(0);
            this.permissionGroups = new ArrayList(0);
            this.activities = new ArrayList(0);
            this.receivers = new ArrayList(0);
            this.providers = new ArrayList(0);
            this.services = new ArrayList(0);
            this.instrumentation = new ArrayList(0);
            this.requestedPermissions = new ArrayList();
            this.implicitPermissions = new ArrayList();
            this.staticSharedLibName = null;
            this.staticSharedLibVersion = 0;
            this.libraryNames = null;
            this.usesLibraries = null;
            this.usesStaticLibraries = null;
            this.usesStaticLibrariesVersions = null;
            this.usesStaticLibrariesCertDigests = null;
            this.usesOptionalLibraries = null;
            this.usesLibraryFiles = null;
            this.usesLibraryInfos = null;
            this.preferredActivityFilters = null;
            this.mOriginalPackages = null;
            this.mRealPackage = null;
            this.mAdoptPermissions = null;
            this.mAppMetaData = null;
            this.mSigningDetails = SigningDetails.UNKNOWN;
            this.mPreferredOrder = 0;
            this.mLastPackageUsageTimeInMills = new long[8];
            this.configPreferences = null;
            this.reqFeatures = null;
            this.featureGroups = null;
            ClassLoader boot = Object.class.getClassLoader();
            this.packageName = dest.readString().intern();
            this.manifestPackageName = dest.readString();
            this.splitNames = dest.readStringArray();
            this.volumeUuid = dest.readString();
            this.codePath = dest.readString();
            this.baseCodePath = dest.readString();
            this.splitCodePaths = dest.readStringArray();
            this.baseRevisionCode = dest.readInt();
            this.splitRevisionCodes = dest.createIntArray();
            this.splitFlags = dest.createIntArray();
            this.splitPrivateFlags = dest.createIntArray();
            this.baseHardwareAccelerated = dest.readInt() == 1;
            this.applicationInfo = (ApplicationInfo) dest.readParcelable(boot);
            if (this.applicationInfo.permission != null) {
                ApplicationInfo applicationInfo = this.applicationInfo;
                applicationInfo.permission = applicationInfo.permission.intern();
            }
            dest.readParcelableList(this.permissions, boot);
            fixupOwner(this.permissions);
            dest.readParcelableList(this.permissionGroups, boot);
            fixupOwner(this.permissionGroups);
            dest.readParcelableList(this.activities, boot);
            fixupOwner(this.activities);
            dest.readParcelableList(this.receivers, boot);
            fixupOwner(this.receivers);
            dest.readParcelableList(this.providers, boot);
            fixupOwner(this.providers);
            dest.readParcelableList(this.services, boot);
            fixupOwner(this.services);
            dest.readParcelableList(this.instrumentation, boot);
            fixupOwner(this.instrumentation);
            dest.readStringList(this.requestedPermissions);
            internStringArrayList(this.requestedPermissions);
            dest.readStringList(this.implicitPermissions);
            internStringArrayList(this.implicitPermissions);
            this.protectedBroadcasts = dest.createStringArrayList();
            internStringArrayList(this.protectedBroadcasts);
            this.parentPackage = (Package) dest.readParcelable(boot);
            this.childPackages = new ArrayList();
            dest.readParcelableList(this.childPackages, boot);
            if (this.childPackages.size() == 0) {
                this.childPackages = null;
            }
            this.staticSharedLibName = dest.readString();
            String str = this.staticSharedLibName;
            if (str != null) {
                this.staticSharedLibName = str.intern();
            }
            this.staticSharedLibVersion = dest.readLong();
            this.libraryNames = dest.createStringArrayList();
            internStringArrayList(this.libraryNames);
            this.usesLibraries = dest.createStringArrayList();
            internStringArrayList(this.usesLibraries);
            this.usesOptionalLibraries = dest.createStringArrayList();
            internStringArrayList(this.usesOptionalLibraries);
            this.usesLibraryFiles = dest.readStringArray();
            this.usesLibraryInfos = dest.createTypedArrayList(SharedLibraryInfo.CREATOR);
            int libCount = dest.readInt();
            if (libCount > 0) {
                this.usesStaticLibraries = new ArrayList(libCount);
                dest.readStringList(this.usesStaticLibraries);
                internStringArrayList(this.usesStaticLibraries);
                this.usesStaticLibrariesVersions = new long[libCount];
                dest.readLongArray(this.usesStaticLibrariesVersions);
                this.usesStaticLibrariesCertDigests = new String[libCount][];
                for (int i = 0; i < libCount; i++) {
                    this.usesStaticLibrariesCertDigests[i] = dest.createStringArray();
                }
            }
            this.preferredActivityFilters = new ArrayList();
            dest.readParcelableList(this.preferredActivityFilters, boot);
            if (this.preferredActivityFilters.size() == 0) {
                this.preferredActivityFilters = null;
            }
            this.mOriginalPackages = dest.createStringArrayList();
            this.mRealPackage = dest.readString();
            this.mAdoptPermissions = dest.createStringArrayList();
            this.mAppMetaData = dest.readBundle();
            this.mVersionCode = dest.readInt();
            this.mVersionCodeMajor = dest.readInt();
            this.mVersionName = dest.readString();
            String str2 = this.mVersionName;
            if (str2 != null) {
                this.mVersionName = str2.intern();
            }
            this.mSharedUserId = dest.readString();
            str2 = this.mSharedUserId;
            if (str2 != null) {
                this.mSharedUserId = str2.intern();
            }
            this.mSharedUserLabel = dest.readInt();
            this.mSigningDetails = (SigningDetails) dest.readParcelable(boot);
            this.mPreferredOrder = dest.readInt();
            this.configPreferences = new ArrayList();
            dest.readParcelableList(this.configPreferences, boot);
            if (this.configPreferences.size() == 0) {
                this.configPreferences = null;
            }
            this.reqFeatures = new ArrayList();
            dest.readParcelableList(this.reqFeatures, boot);
            if (this.reqFeatures.size() == 0) {
                this.reqFeatures = null;
            }
            this.featureGroups = new ArrayList();
            dest.readParcelableList(this.featureGroups, boot);
            if (this.featureGroups.size() == 0) {
                this.featureGroups = null;
            }
            this.installLocation = dest.readInt();
            this.coreApp = dest.readInt() == 1;
            this.mRequiredForAllUsers = dest.readInt() == 1;
            this.mRestrictedAccountType = dest.readString();
            this.mRequiredAccountType = dest.readString();
            this.mOverlayTarget = dest.readString();
            this.mOverlayTargetName = dest.readString();
            this.mOverlayCategory = dest.readString();
            this.mOverlayPriority = dest.readInt();
            this.mOverlayIsStatic = dest.readInt() == 1;
            this.mCompileSdkVersion = dest.readInt();
            this.mCompileSdkVersionCodename = dest.readString();
            this.mUpgradeKeySets = dest.readArraySet(boot);
            this.mKeySetMapping = readKeySetMapping(dest);
            this.cpuAbiOverride = dest.readString();
            this.use32bitAbi = dest.readInt() == 1;
            this.restrictUpdateHash = dest.createByteArray();
            if (dest.readInt() == 1) {
                z = true;
            }
            this.visibleToInstantApps = z;
        }

        private static void internStringArrayList(List<String> list) {
            if (list != null) {
                int N = list.size();
                for (int i = 0; i < N; i++) {
                    list.set(i, ((String) list.get(i)).intern());
                }
            }
        }

        private void fixupOwner(List<? extends Component<?>> list) {
            if (list != null) {
                for (Component<?> c : list) {
                    c.owner = this;
                    if (c instanceof Activity) {
                        ((Activity) c).info.applicationInfo = this.applicationInfo;
                    } else if (c instanceof Service) {
                        ((Service) c).info.applicationInfo = this.applicationInfo;
                    } else if (c instanceof Provider) {
                        ((Provider) c).info.applicationInfo = this.applicationInfo;
                    }
                }
            }
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.packageName);
            dest.writeString(this.manifestPackageName);
            dest.writeStringArray(this.splitNames);
            dest.writeString(this.volumeUuid);
            dest.writeString(this.codePath);
            dest.writeString(this.baseCodePath);
            dest.writeStringArray(this.splitCodePaths);
            dest.writeInt(this.baseRevisionCode);
            dest.writeIntArray(this.splitRevisionCodes);
            dest.writeIntArray(this.splitFlags);
            dest.writeIntArray(this.splitPrivateFlags);
            dest.writeInt(this.baseHardwareAccelerated);
            dest.writeParcelable(this.applicationInfo, flags);
            dest.writeParcelableList(this.permissions, flags);
            dest.writeParcelableList(this.permissionGroups, flags);
            dest.writeParcelableList(this.activities, flags);
            dest.writeParcelableList(this.receivers, flags);
            dest.writeParcelableList(this.providers, flags);
            dest.writeParcelableList(this.services, flags);
            dest.writeParcelableList(this.instrumentation, flags);
            dest.writeStringList(this.requestedPermissions);
            dest.writeStringList(this.implicitPermissions);
            dest.writeStringList(this.protectedBroadcasts);
            dest.writeParcelable(this.parentPackage, flags);
            dest.writeParcelableList(this.childPackages, flags);
            dest.writeString(this.staticSharedLibName);
            dest.writeLong(this.staticSharedLibVersion);
            dest.writeStringList(this.libraryNames);
            dest.writeStringList(this.usesLibraries);
            dest.writeStringList(this.usesOptionalLibraries);
            dest.writeStringArray(this.usesLibraryFiles);
            dest.writeTypedList(this.usesLibraryInfos);
            if (ArrayUtils.isEmpty(this.usesStaticLibraries)) {
                dest.writeInt(-1);
            } else {
                dest.writeInt(this.usesStaticLibraries.size());
                dest.writeStringList(this.usesStaticLibraries);
                dest.writeLongArray(this.usesStaticLibrariesVersions);
                for (String[] usesStaticLibrariesCertDigest : this.usesStaticLibrariesCertDigests) {
                    dest.writeStringArray(usesStaticLibrariesCertDigest);
                }
            }
            dest.writeParcelableList(this.preferredActivityFilters, flags);
            dest.writeStringList(this.mOriginalPackages);
            dest.writeString(this.mRealPackage);
            dest.writeStringList(this.mAdoptPermissions);
            dest.writeBundle(this.mAppMetaData);
            dest.writeInt(this.mVersionCode);
            dest.writeInt(this.mVersionCodeMajor);
            dest.writeString(this.mVersionName);
            dest.writeString(this.mSharedUserId);
            dest.writeInt(this.mSharedUserLabel);
            dest.writeParcelable(this.mSigningDetails, flags);
            dest.writeInt(this.mPreferredOrder);
            dest.writeParcelableList(this.configPreferences, flags);
            dest.writeParcelableList(this.reqFeatures, flags);
            dest.writeParcelableList(this.featureGroups, flags);
            dest.writeInt(this.installLocation);
            dest.writeInt(this.coreApp);
            dest.writeInt(this.mRequiredForAllUsers);
            dest.writeString(this.mRestrictedAccountType);
            dest.writeString(this.mRequiredAccountType);
            dest.writeString(this.mOverlayTarget);
            dest.writeString(this.mOverlayTargetName);
            dest.writeString(this.mOverlayCategory);
            dest.writeInt(this.mOverlayPriority);
            dest.writeInt(this.mOverlayIsStatic);
            dest.writeInt(this.mCompileSdkVersion);
            dest.writeString(this.mCompileSdkVersionCodename);
            dest.writeArraySet(this.mUpgradeKeySets);
            writeKeySetMapping(dest, this.mKeySetMapping);
            dest.writeString(this.cpuAbiOverride);
            dest.writeInt(this.use32bitAbi);
            dest.writeByteArray(this.restrictUpdateHash);
            dest.writeInt(this.visibleToInstantApps);
        }

        private static void writeKeySetMapping(Parcel dest, ArrayMap<String, ArraySet<PublicKey>> keySetMapping) {
            if (keySetMapping == null) {
                dest.writeInt(-1);
                return;
            }
            int N = keySetMapping.size();
            dest.writeInt(N);
            for (int i = 0; i < N; i++) {
                dest.writeString((String) keySetMapping.keyAt(i));
                ArraySet<PublicKey> keys = (ArraySet) keySetMapping.valueAt(i);
                if (keys == null) {
                    dest.writeInt(-1);
                } else {
                    int M = keys.size();
                    dest.writeInt(M);
                    for (int j = 0; j < M; j++) {
                        dest.writeSerializable((Serializable) keys.valueAt(j));
                    }
                }
            }
        }

        private static ArrayMap<String, ArraySet<PublicKey>> readKeySetMapping(Parcel in) {
            int N = in.readInt();
            if (N == -1) {
                return null;
            }
            ArrayMap<String, ArraySet<PublicKey>> keySetMapping = new ArrayMap();
            for (int i = 0; i < N; i++) {
                String key = in.readString();
                int M = in.readInt();
                if (M == -1) {
                    keySetMapping.put(key, null);
                } else {
                    ArraySet<PublicKey> keys = new ArraySet(M);
                    for (int j = 0; j < M; j++) {
                        keys.add((PublicKey) in.readSerializable());
                    }
                    keySetMapping.put(key, keys);
                }
            }
            return keySetMapping;
        }
    }

    public static class PackageLite {
        public final String baseCodePath;
        public final int baseRevisionCode;
        public final String codePath;
        public final String[] configForSplit;
        public final boolean coreApp;
        public final boolean debuggable;
        public final boolean extractNativeLibs;
        @UnsupportedAppUsage
        public final int installLocation;
        public final boolean[] isFeatureSplits;
        public final boolean isolatedSplits;
        public final boolean multiArch;
        @UnsupportedAppUsage
        public final String packageName;
        public final String[] splitCodePaths;
        public final String[] splitNames;
        public final int[] splitRevisionCodes;
        public final boolean use32bitAbi;
        public final String[] usesSplitNames;
        public final VerifierInfo[] verifiers;
        public final int versionCode;
        public final int versionCodeMajor;

        public PackageLite(String codePath, ApkLite baseApk, String[] splitNames, boolean[] isFeatureSplits, String[] usesSplitNames, String[] configForSplit, String[] splitCodePaths, int[] splitRevisionCodes) {
            this.packageName = baseApk.packageName;
            this.versionCode = baseApk.versionCode;
            this.versionCodeMajor = baseApk.versionCodeMajor;
            this.installLocation = baseApk.installLocation;
            this.verifiers = baseApk.verifiers;
            this.splitNames = splitNames;
            this.isFeatureSplits = isFeatureSplits;
            this.usesSplitNames = usesSplitNames;
            this.configForSplit = configForSplit;
            this.codePath = codePath;
            this.baseCodePath = baseApk.codePath;
            this.splitCodePaths = splitCodePaths;
            this.baseRevisionCode = baseApk.revisionCode;
            this.splitRevisionCodes = splitRevisionCodes;
            this.coreApp = baseApk.coreApp;
            this.debuggable = baseApk.debuggable;
            this.multiArch = baseApk.multiArch;
            this.use32bitAbi = baseApk.use32bitAbi;
            this.extractNativeLibs = baseApk.extractNativeLibs;
            this.isolatedSplits = baseApk.isolatedSplits;
        }

        public List<String> getAllCodePaths() {
            ArrayList<String> paths = new ArrayList();
            paths.add(this.baseCodePath);
            if (!ArrayUtils.isEmpty(this.splitCodePaths)) {
                Collections.addAll(paths, this.splitCodePaths);
            }
            return paths;
        }
    }

    public static class PackageParserException extends Exception {
        public final int error;

        public PackageParserException(int error, String detailMessage) {
            super(detailMessage);
            this.error = error;
        }

        public PackageParserException(int error, String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
            this.error = error;
        }
    }

    static class ParsePackageItemArgs {
        final int bannerRes;
        final int iconRes;
        final int labelRes;
        final int logoRes;
        final int nameRes;
        final String[] outError;
        final Package owner;
        final int roundIconRes;
        TypedArray sa;
        String tag;

        ParsePackageItemArgs(Package _owner, String[] _outError, int _nameRes, int _labelRes, int _iconRes, int _roundIconRes, int _logoRes, int _bannerRes) {
            this.owner = _owner;
            this.outError = _outError;
            this.nameRes = _nameRes;
            this.labelRes = _labelRes;
            this.iconRes = _iconRes;
            this.logoRes = _logoRes;
            this.bannerRes = _bannerRes;
            this.roundIconRes = _roundIconRes;
        }
    }

    @VisibleForTesting
    public static class ParseComponentArgs extends ParsePackageItemArgs {
        final int descriptionRes;
        final int enabledRes;
        int flags;
        final int processRes;
        final String[] sepProcesses;

        public ParseComponentArgs(Package _owner, String[] _outError, int _nameRes, int _labelRes, int _iconRes, int _roundIconRes, int _logoRes, int _bannerRes, String[] _sepProcesses, int _processRes, int _descriptionRes, int _enabledRes) {
            super(_owner, _outError, _nameRes, _labelRes, _iconRes, _roundIconRes, _logoRes, _bannerRes);
            this.sepProcesses = _sepProcesses;
            this.processRes = _processRes;
            this.descriptionRes = _descriptionRes;
            this.enabledRes = _enabledRes;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ParseFlags {
    }

    public static final class Permission extends Component<IntentInfo> implements Parcelable {
        public static final Creator CREATOR = new Creator<Permission>() {
            public Permission createFromParcel(Parcel in) {
                return new Permission(in, null);
            }

            public Permission[] newArray(int size) {
                return new Permission[size];
            }
        };
        @UnsupportedAppUsage
        public PermissionGroup group;
        @UnsupportedAppUsage
        public final PermissionInfo info;
        @UnsupportedAppUsage
        public boolean tree;

        public Permission(Package owner, String backgroundPermission) {
            super(owner);
            this.info = new PermissionInfo(backgroundPermission);
        }

        @UnsupportedAppUsage
        public Permission(Package _owner, PermissionInfo _info) {
            super(_owner);
            this.info = _info;
        }

        public void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Permission{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(this.info.name);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.info, flags);
            dest.writeInt(this.tree);
            dest.writeParcelable(this.group, flags);
        }

        public boolean isAppOp() {
            return this.info.isAppOp();
        }

        private Permission(Parcel in) {
            super(in);
            ClassLoader boot = Object.class.getClassLoader();
            this.info = (PermissionInfo) in.readParcelable(boot);
            if (this.info.group != null) {
                PermissionInfo permissionInfo = this.info;
                permissionInfo.group = permissionInfo.group.intern();
            }
            boolean z = true;
            if (in.readInt() != 1) {
                z = false;
            }
            this.tree = z;
            this.group = (PermissionGroup) in.readParcelable(boot);
        }
    }

    public static final class PermissionGroup extends Component<IntentInfo> implements Parcelable {
        public static final Creator CREATOR = new Creator<PermissionGroup>() {
            public PermissionGroup createFromParcel(Parcel in) {
                return new PermissionGroup(in, null);
            }

            public PermissionGroup[] newArray(int size) {
                return new PermissionGroup[size];
            }
        };
        @UnsupportedAppUsage
        public final PermissionGroupInfo info;

        public PermissionGroup(Package owner, int requestDetailResourceId, int backgroundRequestResourceId, int backgroundRequestDetailResourceId) {
            super(owner);
            this.info = new PermissionGroupInfo(requestDetailResourceId, backgroundRequestResourceId, backgroundRequestDetailResourceId);
        }

        public PermissionGroup(Package _owner, PermissionGroupInfo _info) {
            super(_owner);
            this.info = _info;
        }

        public void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("PermissionGroup{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(this.info.name);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.info, flags);
        }

        private PermissionGroup(Parcel in) {
            super(in);
            this.info = (PermissionGroupInfo) in.readParcelable(Object.class.getClassLoader());
        }
    }

    public static final class Provider extends Component<ProviderIntentInfo> implements Parcelable {
        public static final Creator CREATOR = new Creator<Provider>() {
            public Provider createFromParcel(Parcel in) {
                return new Provider(in, null);
            }

            public Provider[] newArray(int size) {
                return new Provider[size];
            }
        };
        @UnsupportedAppUsage
        public final ProviderInfo info;
        @UnsupportedAppUsage
        public boolean syncable;

        public Provider(ParseComponentArgs args, ProviderInfo _info) {
            super(args, (ComponentInfo) _info);
            this.info = _info;
            this.info.applicationInfo = args.owner.applicationInfo;
            this.syncable = false;
        }

        @UnsupportedAppUsage
        public Provider(Provider existingProvider) {
            super((Component) existingProvider);
            this.info = existingProvider.info;
            this.syncable = existingProvider.syncable;
        }

        public void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Provider{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.info, flags | 2);
            dest.writeInt(this.syncable);
        }

        private Provider(Parcel in) {
            ProviderInfo providerInfo;
            super(in);
            this.info = (ProviderInfo) in.readParcelable(Object.class.getClassLoader());
            boolean z = true;
            if (in.readInt() != 1) {
                z = false;
            }
            this.syncable = z;
            Iterator it = this.intents.iterator();
            while (it.hasNext()) {
                ((ProviderIntentInfo) it.next()).provider = this;
            }
            if (this.info.readPermission != null) {
                providerInfo = this.info;
                providerInfo.readPermission = providerInfo.readPermission.intern();
            }
            if (this.info.writePermission != null) {
                providerInfo = this.info;
                providerInfo.writePermission = providerInfo.writePermission.intern();
            }
            if (this.info.authority != null) {
                providerInfo = this.info;
                providerInfo.authority = providerInfo.authority.intern();
            }
        }
    }

    public static final class ProviderIntentInfo extends IntentInfo {
        @UnsupportedAppUsage
        public Provider provider;

        public ProviderIntentInfo(Provider provider) {
            this.provider = provider;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("ProviderIntentInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            this.provider.appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        public ProviderIntentInfo(Parcel in) {
            super(in);
        }
    }

    public static final class Service extends Component<ServiceIntentInfo> implements Parcelable {
        public static final Creator CREATOR = new Creator<Service>() {
            public Service createFromParcel(Parcel in) {
                return new Service(in, null);
            }

            public Service[] newArray(int size) {
                return new Service[size];
            }
        };
        @UnsupportedAppUsage
        public final ServiceInfo info;

        public Service(ParseComponentArgs args, ServiceInfo _info) {
            super(args, (ComponentInfo) _info);
            this.info = _info;
            this.info.applicationInfo = args.owner.applicationInfo;
        }

        public void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Service{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.info, flags | 2);
        }

        private Service(Parcel in) {
            super(in);
            this.info = (ServiceInfo) in.readParcelable(Object.class.getClassLoader());
            Iterator it = this.intents.iterator();
            while (it.hasNext()) {
                ServiceIntentInfo aii = (ServiceIntentInfo) it.next();
                aii.service = this;
                this.order = Math.max(aii.getOrder(), this.order);
            }
            if (this.info.permission != null) {
                ServiceInfo serviceInfo = this.info;
                serviceInfo.permission = serviceInfo.permission.intern();
            }
        }
    }

    public static final class ServiceIntentInfo extends IntentInfo {
        @UnsupportedAppUsage
        public Service service;

        public ServiceIntentInfo(Service _service) {
            this.service = _service;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("ServiceIntentInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            this.service.appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        public ServiceIntentInfo(Parcel in) {
            super(in);
        }
    }

    public static final class SigningDetails implements Parcelable {
        public static final Creator<SigningDetails> CREATOR = new Creator<SigningDetails>() {
            public SigningDetails createFromParcel(Parcel source) {
                if (source.readBoolean()) {
                    return SigningDetails.UNKNOWN;
                }
                return new SigningDetails(source);
            }

            public SigningDetails[] newArray(int size) {
                return new SigningDetails[size];
            }
        };
        private static final int PAST_CERT_EXISTS = 0;
        public static final SigningDetails UNKNOWN = new SigningDetails(null, 0, null, null);
        public final Signature[] pastSigningCertificates;
        public final ArraySet<PublicKey> publicKeys;
        @SignatureSchemeVersion
        public final int signatureSchemeVersion;
        @UnsupportedAppUsage
        public final Signature[] signatures;

        public static class Builder {
            private Signature[] mPastSigningCertificates;
            private int mSignatureSchemeVersion = 0;
            private Signature[] mSignatures;

            @UnsupportedAppUsage
            public Builder setSignatures(Signature[] signatures) {
                this.mSignatures = signatures;
                return this;
            }

            @UnsupportedAppUsage
            public Builder setSignatureSchemeVersion(int signatureSchemeVersion) {
                this.mSignatureSchemeVersion = signatureSchemeVersion;
                return this;
            }

            @UnsupportedAppUsage
            public Builder setPastSigningCertificates(Signature[] pastSigningCertificates) {
                this.mPastSigningCertificates = pastSigningCertificates;
                return this;
            }

            private void checkInvariants() {
                if (this.mSignatures == null) {
                    throw new IllegalStateException("SigningDetails requires the current signing certificates.");
                }
            }

            @UnsupportedAppUsage
            public SigningDetails build() throws CertificateException {
                checkInvariants();
                return new SigningDetails(this.mSignatures, this.mSignatureSchemeVersion, this.mPastSigningCertificates);
            }
        }

        public @interface CertCapabilities {
            public static final int AUTH = 16;
            public static final int INSTALLED_DATA = 1;
            public static final int PERMISSION = 4;
            public static final int ROLLBACK = 8;
            public static final int SHARED_USER_ID = 2;
        }

        public @interface SignatureSchemeVersion {
            public static final int JAR = 1;
            public static final int SIGNING_BLOCK_V2 = 2;
            public static final int SIGNING_BLOCK_V3 = 3;
            public static final int UNKNOWN = 0;
        }

        @VisibleForTesting
        public SigningDetails(Signature[] signatures, @SignatureSchemeVersion int signatureSchemeVersion, ArraySet<PublicKey> keys, Signature[] pastSigningCertificates) {
            this.signatures = signatures;
            this.signatureSchemeVersion = signatureSchemeVersion;
            this.publicKeys = keys;
            this.pastSigningCertificates = pastSigningCertificates;
        }

        public SigningDetails(Signature[] signatures, @SignatureSchemeVersion int signatureSchemeVersion, Signature[] pastSigningCertificates) throws CertificateException {
            this(signatures, signatureSchemeVersion, PackageParser.toSigningKeys(signatures), pastSigningCertificates);
        }

        public SigningDetails(Signature[] signatures, @SignatureSchemeVersion int signatureSchemeVersion) throws CertificateException {
            this(signatures, signatureSchemeVersion, null);
        }

        public SigningDetails(SigningDetails orig) {
            if (orig != null) {
                Signature[] signatureArr = orig.signatures;
                if (signatureArr != null) {
                    this.signatures = (Signature[]) signatureArr.clone();
                } else {
                    this.signatures = null;
                }
                this.signatureSchemeVersion = orig.signatureSchemeVersion;
                this.publicKeys = new ArraySet(orig.publicKeys);
                signatureArr = orig.pastSigningCertificates;
                if (signatureArr != null) {
                    this.pastSigningCertificates = (Signature[]) signatureArr.clone();
                    return;
                } else {
                    this.pastSigningCertificates = null;
                    return;
                }
            }
            this.signatures = null;
            this.signatureSchemeVersion = 0;
            this.publicKeys = null;
            this.pastSigningCertificates = null;
        }

        public boolean hasSignatures() {
            Signature[] signatureArr = this.signatures;
            return signatureArr != null && signatureArr.length > 0;
        }

        public boolean hasPastSigningCertificates() {
            Signature[] signatureArr = this.pastSigningCertificates;
            return signatureArr != null && signatureArr.length > 0;
        }

        public boolean hasAncestorOrSelf(SigningDetails oldDetails) {
            SigningDetails signingDetails = UNKNOWN;
            if (this == signingDetails || oldDetails == signingDetails) {
                return false;
            }
            Signature[] signatureArr = oldDetails.signatures;
            if (signatureArr.length > 1) {
                return signaturesMatchExactly(oldDetails);
            }
            return hasCertificate(signatureArr[0]);
        }

        /* JADX WARNING: Missing block: B:15:0x002c, code skipped:
            return false;
     */
        public boolean hasAncestor(android.content.pm.PackageParser.SigningDetails r6) {
            /*
            r5 = this;
            r0 = UNKNOWN;
            r1 = 0;
            if (r5 == r0) goto L_0x002c;
        L_0x0005:
            if (r6 != r0) goto L_0x0008;
        L_0x0007:
            goto L_0x002c;
        L_0x0008:
            r0 = r5.hasPastSigningCertificates();
            if (r0 == 0) goto L_0x002b;
        L_0x000e:
            r0 = r6.signatures;
            r0 = r0.length;
            r2 = 1;
            if (r0 != r2) goto L_0x002b;
        L_0x0014:
            r0 = 0;
        L_0x0015:
            r3 = r5.pastSigningCertificates;
            r4 = r3.length;
            r4 = r4 - r2;
            if (r0 >= r4) goto L_0x002b;
        L_0x001b:
            r3 = r3[r0];
            r4 = r6.signatures;
            r4 = r4[r0];
            r3 = r3.equals(r4);
            if (r3 == 0) goto L_0x0028;
        L_0x0027:
            return r2;
        L_0x0028:
            r0 = r0 + 1;
            goto L_0x0015;
        L_0x002b:
            return r1;
        L_0x002c:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser$SigningDetails.hasAncestor(android.content.pm.PackageParser$SigningDetails):boolean");
        }

        public boolean checkCapability(SigningDetails oldDetails, @CertCapabilities int flags) {
            SigningDetails signingDetails = UNKNOWN;
            if (this == signingDetails || oldDetails == signingDetails) {
                return false;
            }
            Signature[] signatureArr = oldDetails.signatures;
            if (signatureArr.length > 1) {
                return signaturesMatchExactly(oldDetails);
            }
            return hasCertificate(signatureArr[0], flags);
        }

        public boolean checkCapabilityRecover(SigningDetails oldDetails, @CertCapabilities int flags) throws CertificateException {
            SigningDetails signingDetails = UNKNOWN;
            if (oldDetails == signingDetails || this == signingDetails) {
                return false;
            }
            if (!hasPastSigningCertificates() || oldDetails.signatures.length != 1) {
                return Signature.areEffectiveMatch(oldDetails.signatures, this.signatures);
            }
            int i = 0;
            while (true) {
                Signature[] signatureArr = this.pastSigningCertificates;
                if (i >= signatureArr.length) {
                    return false;
                }
                if (Signature.areEffectiveMatch(oldDetails.signatures[0], signatureArr[i]) && this.pastSigningCertificates[i].getFlags() == flags) {
                    return true;
                }
                i++;
            }
        }

        public boolean hasCertificate(Signature signature) {
            return hasCertificateInternal(signature, 0);
        }

        public boolean hasCertificate(Signature signature, @CertCapabilities int flags) {
            return hasCertificateInternal(signature, flags);
        }

        public boolean hasCertificate(byte[] certificate) {
            return hasCertificate(new Signature(certificate));
        }

        private boolean hasCertificateInternal(Signature signature, int flags) {
            boolean z = false;
            if (this == UNKNOWN) {
                return false;
            }
            if (hasPastSigningCertificates()) {
                int i = 0;
                while (true) {
                    Signature[] signatureArr = this.pastSigningCertificates;
                    if (i >= signatureArr.length - 1) {
                        break;
                    } else if (!(signatureArr[i].equals(signature) && (flags == 0 || (this.pastSigningCertificates[i].getFlags() & flags) == flags))) {
                        i++;
                    }
                }
                return true;
            }
            Signature[] signatureArr2 = this.signatures;
            if (signatureArr2.length == 1 && signatureArr2[0].equals(signature)) {
                z = true;
            }
            return z;
        }

        public boolean checkCapability(String sha256String, @CertCapabilities int flags) {
            if (this == UNKNOWN) {
                return false;
            }
            if (hasSha256Certificate(ByteStringUtils.fromHexToByteArray(sha256String), flags)) {
                return true;
            }
            return PackageUtils.computeSignaturesSha256Digest(PackageUtils.computeSignaturesSha256Digests(this.signatures)).equals(sha256String);
        }

        public boolean hasSha256Certificate(byte[] sha256Certificate) {
            return hasSha256CertificateInternal(sha256Certificate, 0);
        }

        public boolean hasSha256Certificate(byte[] sha256Certificate, @CertCapabilities int flags) {
            return hasSha256CertificateInternal(sha256Certificate, flags);
        }

        private boolean hasSha256CertificateInternal(byte[] sha256Certificate, int flags) {
            if (this == UNKNOWN) {
                return false;
            }
            if (hasPastSigningCertificates()) {
                int i = 0;
                while (true) {
                    Signature[] signatureArr = this.pastSigningCertificates;
                    if (i >= signatureArr.length - 1) {
                        break;
                    } else if (!(Arrays.equals(sha256Certificate, PackageUtils.computeSha256DigestBytes(signatureArr[i].toByteArray())) && (flags == 0 || (this.pastSigningCertificates[i].getFlags() & flags) == flags))) {
                        i++;
                    }
                }
                return true;
            }
            Signature[] signatureArr2 = this.signatures;
            if (signatureArr2.length == 1) {
                return Arrays.equals(sha256Certificate, PackageUtils.computeSha256DigestBytes(signatureArr2[0].toByteArray()));
            }
            return false;
        }

        public boolean signaturesMatchExactly(SigningDetails other) {
            return Signature.areExactMatch(this.signatures, other.signatures);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            boolean isUnknown = UNKNOWN == this;
            dest.writeBoolean(isUnknown);
            if (!isUnknown) {
                dest.writeTypedArray(this.signatures, flags);
                dest.writeInt(this.signatureSchemeVersion);
                dest.writeArraySet(this.publicKeys);
                dest.writeTypedArray(this.pastSigningCertificates, flags);
            }
        }

        protected SigningDetails(Parcel in) {
            ClassLoader boot = Object.class.getClassLoader();
            this.signatures = (Signature[]) in.createTypedArray(Signature.CREATOR);
            this.signatureSchemeVersion = in.readInt();
            this.publicKeys = in.readArraySet(boot);
            this.pastSigningCertificates = (Signature[]) in.createTypedArray(Signature.CREATOR);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SigningDetails)) {
                return false;
            }
            SigningDetails that = (SigningDetails) o;
            if (this.signatureSchemeVersion != that.signatureSchemeVersion || !Signature.areExactMatch(this.signatures, that.signatures)) {
                return false;
            }
            ArraySet arraySet = this.publicKeys;
            if (arraySet != null) {
                if (!arraySet.equals(that.publicKeys)) {
                    return false;
                }
            } else if (that.publicKeys != null) {
                return false;
            }
            if (Arrays.equals(this.pastSigningCertificates, that.pastSigningCertificates)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int hashCode = ((Arrays.hashCode(this.signatures) * 31) + this.signatureSchemeVersion) * 31;
            ArraySet arraySet = this.publicKeys;
            return ((hashCode + (arraySet != null ? arraySet.hashCode() : 0)) * 31) + Arrays.hashCode(this.pastSigningCertificates);
        }
    }

    private static class SplitNameComparator implements Comparator<String> {
        private SplitNameComparator() {
        }

        public int compare(String lhs, String rhs) {
            if (lhs == null) {
                return -1;
            }
            if (rhs == null) {
                return 1;
            }
            return lhs.compareTo(rhs);
        }
    }

    static {
        boolean z = Build.IS_DEBUGGABLE && SystemProperties.getBoolean(PROPERTY_CHILD_PACKAGES_ENABLED, false);
        MULTI_PACKAGE_APK_ENABLED = z;
        CHILD_PACKAGE_TAGS.add(TAG_APPLICATION);
        CHILD_PACKAGE_TAGS.add(TAG_USES_PERMISSION);
        CHILD_PACKAGE_TAGS.add(TAG_USES_PERMISSION_SDK_M);
        CHILD_PACKAGE_TAGS.add(TAG_USES_PERMISSION_SDK_23);
        CHILD_PACKAGE_TAGS.add(TAG_USES_CONFIGURATION);
        CHILD_PACKAGE_TAGS.add(TAG_USES_FEATURE);
        CHILD_PACKAGE_TAGS.add(TAG_FEATURE_GROUP);
        CHILD_PACKAGE_TAGS.add(TAG_USES_SDK);
        CHILD_PACKAGE_TAGS.add(TAG_SUPPORT_SCREENS);
        CHILD_PACKAGE_TAGS.add(TAG_INSTRUMENTATION);
        CHILD_PACKAGE_TAGS.add(TAG_USES_GL_TEXTURE);
        CHILD_PACKAGE_TAGS.add(TAG_COMPATIBLE_SCREENS);
        CHILD_PACKAGE_TAGS.add(TAG_SUPPORTS_INPUT);
        CHILD_PACKAGE_TAGS.add(TAG_EAT_COMMENT);
        SAFE_BROADCASTS.add(Intent.ACTION_BOOT_COMPLETED);
    }

    @UnsupportedAppUsage
    public PackageParser() {
        this.mMetrics.setToDefaults();
    }

    @UnsupportedAppUsage
    public void setSeparateProcesses(String[] procs) {
        this.mSeparateProcesses = procs;
    }

    public void setOnlyCoreApps(boolean onlyCoreApps) {
        this.mOnlyCoreApps = onlyCoreApps;
    }

    public void setDisplayMetrics(DisplayMetrics metrics) {
        this.mMetrics = metrics;
    }

    public void setCacheDir(File cacheDir) {
        this.mCacheDir = cacheDir;
    }

    public void setCallback(Callback cb) {
        this.mCallback = cb;
    }

    public static final boolean isApkFile(File file) {
        return isApkPath(file.getName());
    }

    public static boolean isApkPath(String path) {
        return path.endsWith(APK_FILE_EXTENSION);
    }

    @UnsupportedAppUsage
    public static PackageInfo generatePackageInfo(Package p, int[] gids, int flags, long firstInstallTime, long lastUpdateTime, Set<String> grantedPermissions, PackageUserState state) {
        return generatePackageInfo(p, gids, flags, firstInstallTime, lastUpdateTime, grantedPermissions, state, UserHandle.getCallingUserId());
    }

    private static boolean checkUseInstalledOrHidden(int flags, PackageUserState state, ApplicationInfo appInfo) {
        boolean z = false;
        if ((flags & 536870912) == 0 && !state.installed && appInfo != null && appInfo.hiddenUntilInstalled) {
            return false;
        }
        if (state.isAvailable(flags) || !(appInfo == null || !appInfo.isSystemApp() || ((PackageManager.MATCH_KNOWN_PACKAGES & flags) == 0 && (536870912 & flags) == 0))) {
            z = true;
        }
        return z;
    }

    public static boolean isAvailable(PackageUserState state) {
        return checkUseInstalledOrHidden(0, state, null);
    }

    @UnsupportedAppUsage
    public static PackageInfo generatePackageInfo(Package p, int[] gids, int flags, long firstInstallTime, long lastUpdateTime, Set<String> grantedPermissions, PackageUserState state, int userId) {
        PackageInfo packageInfo;
        Package packageR = p;
        int i = flags;
        Set<String> set = grantedPermissions;
        PackageUserState packageUserState = state;
        int i2 = userId;
        long j;
        long j2;
        if (!checkUseInstalledOrHidden(i, packageUserState, packageR.applicationInfo)) {
            j = firstInstallTime;
            j2 = lastUpdateTime;
            packageInfo = null;
        } else if (packageR.isMatch(i)) {
            int[] iArr;
            int N;
            int N2;
            int num;
            int num2;
            PackageInfo pi = new PackageInfo();
            pi.packageName = packageR.packageName;
            pi.splitNames = packageR.splitNames;
            pi.versionCode = packageR.mVersionCode;
            pi.versionCodeMajor = packageR.mVersionCodeMajor;
            pi.baseRevisionCode = packageR.baseRevisionCode;
            pi.splitRevisionCodes = packageR.splitRevisionCodes;
            pi.versionName = packageR.mVersionName;
            pi.sharedUserId = packageR.mSharedUserId;
            pi.sharedUserLabel = packageR.mSharedUserLabel;
            pi.applicationInfo = generateApplicationInfo(packageR, i, packageUserState, i2);
            pi.installLocation = packageR.installLocation;
            pi.isStub = packageR.isStub;
            pi.coreApp = packageR.coreApp;
            if (!((pi.applicationInfo.flags & 1) == 0 && (pi.applicationInfo.flags & 128) == 0)) {
                pi.requiredForAllUsers = packageR.mRequiredForAllUsers;
            }
            pi.restrictedAccountType = packageR.mRestrictedAccountType;
            pi.requiredAccountType = packageR.mRequiredAccountType;
            pi.overlayTarget = packageR.mOverlayTarget;
            pi.targetOverlayableName = packageR.mOverlayTargetName;
            pi.overlayCategory = packageR.mOverlayCategory;
            pi.overlayPriority = packageR.mOverlayPriority;
            pi.mOverlayIsStatic = packageR.mOverlayIsStatic;
            pi.compileSdkVersion = packageR.mCompileSdkVersion;
            pi.compileSdkVersionCodename = packageR.mCompileSdkVersionCodename;
            pi.firstInstallTime = firstInstallTime;
            pi.lastUpdateTime = lastUpdateTime;
            if ((i & 256) != 0) {
                pi.gids = gids;
            } else {
                iArr = gids;
            }
            if ((i & 16384) != 0) {
                N = packageR.configPreferences != null ? packageR.configPreferences.size() : 0;
                if (N > 0) {
                    pi.configPreferences = new ConfigurationInfo[N];
                    packageR.configPreferences.toArray(pi.configPreferences);
                }
                N2 = packageR.reqFeatures != null ? packageR.reqFeatures.size() : 0;
                if (N2 > 0) {
                    pi.reqFeatures = new FeatureInfo[N2];
                    packageR.reqFeatures.toArray(pi.reqFeatures);
                }
                N2 = packageR.featureGroups != null ? packageR.featureGroups.size() : 0;
                if (N2 > 0) {
                    pi.featureGroups = new FeatureGroupInfo[N2];
                    packageR.featureGroups.toArray(pi.featureGroups);
                }
            }
            if ((i & 1) != 0) {
                N2 = packageR.activities.size();
                if (N2 > 0) {
                    ActivityInfo[] res = new ActivityInfo[N2];
                    int num3 = 0;
                    N = 0;
                    while (N < N2) {
                        Activity a = (Activity) packageR.activities.get(N);
                        int N3 = N2;
                        if (packageUserState.isMatch(a.info, i) != 0 && PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME.equals(a.className) == 0) {
                            N2 = num3 + 1;
                            res[num3] = generateActivityInfo(a, i, packageUserState, i2);
                            num3 = N2;
                        }
                        N++;
                        iArr = gids;
                        N2 = N3;
                    }
                    pi.activities = (ActivityInfo[]) ArrayUtils.trimToSize(res, num3);
                }
            }
            if ((i & 2) != 0) {
                N2 = packageR.receivers.size();
                if (N2 > 0) {
                    num = 0;
                    ActivityInfo[] res2 = new ActivityInfo[N2];
                    for (N = 0; N < N2; N++) {
                        Activity a2 = (Activity) packageR.receivers.get(N);
                        if (packageUserState.isMatch(a2.info, i)) {
                            num2 = num + 1;
                            res2[num] = generateActivityInfo(a2, i, packageUserState, i2);
                            num = num2;
                        }
                    }
                    pi.receivers = (ActivityInfo[]) ArrayUtils.trimToSize(res2, num);
                }
            }
            if ((i & 4) != 0) {
                N2 = packageR.services.size();
                if (N2 > 0) {
                    num = 0;
                    ServiceInfo[] res3 = new ServiceInfo[N2];
                    for (N = 0; N < N2; N++) {
                        Service s = (Service) packageR.services.get(N);
                        if (packageUserState.isMatch(s.info, i)) {
                            num2 = num + 1;
                            res3[num] = generateServiceInfo(s, i, packageUserState, i2);
                            num = num2;
                        }
                    }
                    pi.services = (ServiceInfo[]) ArrayUtils.trimToSize(res3, num);
                }
            }
            if ((i & 8) != 0) {
                N2 = packageR.providers.size();
                if (N2 > 0) {
                    num = 0;
                    ProviderInfo[] res4 = new ProviderInfo[N2];
                    for (N = 0; N < N2; N++) {
                        Provider pr = (Provider) packageR.providers.get(N);
                        if (packageUserState.isMatch(pr.info, i)) {
                            num2 = num + 1;
                            res4[num] = generateProviderInfo(pr, i, packageUserState, i2);
                            num = num2;
                        }
                    }
                    pi.providers = (ProviderInfo[]) ArrayUtils.trimToSize(res4, num);
                }
            }
            if ((i & 16) != 0) {
                N2 = packageR.instrumentation.size();
                if (N2 > 0) {
                    pi.instrumentation = new InstrumentationInfo[N2];
                    for (num = 0; num < N2; num++) {
                        pi.instrumentation[num] = generateInstrumentationInfo((Instrumentation) packageR.instrumentation.get(num), i);
                    }
                }
            }
            if ((i & 4096) != 0) {
                N2 = packageR.permissions.size();
                if (N2 > 0) {
                    pi.permissions = new PermissionInfo[N2];
                    for (num = 0; num < N2; num++) {
                        pi.permissions[num] = generatePermissionInfo((Permission) packageR.permissions.get(num), i);
                    }
                }
                N2 = packageR.requestedPermissions.size();
                if (N2 > 0) {
                    pi.requestedPermissions = new String[N2];
                    pi.requestedPermissionsFlags = new int[N2];
                    for (num = 0; num < N2; num++) {
                        String perm = (String) packageR.requestedPermissions.get(num);
                        pi.requestedPermissions[num] = perm;
                        int[] iArr2 = pi.requestedPermissionsFlags;
                        iArr2[num] = iArr2[num] | 1;
                        if (set != null && set.contains(perm)) {
                            iArr2 = pi.requestedPermissionsFlags;
                            iArr2[num] = iArr2[num] | 2;
                        }
                    }
                }
            }
            if ((i & 64) != 0) {
                if (packageR.mSigningDetails.hasPastSigningCertificates()) {
                    pi.signatures = new Signature[1];
                    pi.signatures[0] = packageR.mSigningDetails.pastSigningCertificates[0];
                } else if (packageR.mSigningDetails.hasSignatures()) {
                    N2 = packageR.mSigningDetails.signatures.length;
                    pi.signatures = new Signature[N2];
                    System.arraycopy(packageR.mSigningDetails.signatures, 0, pi.signatures, 0, N2);
                }
            }
            if ((134217728 & i) != 0) {
                if (packageR.mSigningDetails != SigningDetails.UNKNOWN) {
                    pi.signingInfo = new SigningInfo(packageR.mSigningDetails);
                } else {
                    pi.signingInfo = null;
                }
            }
            return pi;
        } else {
            j = firstInstallTime;
            j2 = lastUpdateTime;
            packageInfo = null;
        }
        return packageInfo;
    }

    @UnsupportedAppUsage
    public static PackageLite parsePackageLite(File packageFile, int flags) throws PackageParserException {
        if (packageFile.isDirectory()) {
            return parseClusterPackageLite(packageFile, flags);
        }
        return parseMonolithicPackageLite(packageFile, flags);
    }

    private static PackageLite parseMonolithicPackageLite(File packageFile, int flags) throws PackageParserException {
        Trace.traceBegin(262144, "parseApkLite");
        ApkLite baseApk = parseApkLite(packageFile, flags);
        String packagePath = packageFile.getAbsolutePath();
        Trace.traceEnd(262144);
        return new PackageLite(packagePath, baseApk, null, null, null, null, null, null);
    }

    static PackageLite parseClusterPackageLite(File packageDir, int flags) throws PackageParserException {
        Object[] files = packageDir.listFiles();
        int i;
        if (ArrayUtils.isEmpty(files)) {
            File file = packageDir;
            i = flags;
            throw new PackageParserException(-100, "No packages found in split");
        }
        String packageName = null;
        int versionCode = 0;
        Trace.traceBegin(262144, "parseApkLite");
        ArrayMap<String, ApkLite> apks = new ArrayMap();
        for (File file2 : files) {
            if (isApkFile(file2)) {
                StringBuilder stringBuilder;
                ApkLite lite = parseApkLite(file2, flags);
                if (packageName == null) {
                    packageName = lite.packageName;
                    versionCode = lite.versionCode;
                } else {
                    String str = "; expected ";
                    String str2 = " in ";
                    if (!packageName.equals(lite.packageName)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Inconsistent package ");
                        stringBuilder.append(lite.packageName);
                        stringBuilder.append(str2);
                        stringBuilder.append(file2);
                        stringBuilder.append(str);
                        stringBuilder.append(packageName);
                        throw new PackageParserException(-101, stringBuilder.toString());
                    } else if (versionCode != lite.versionCode) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Inconsistent version ");
                        stringBuilder.append(lite.versionCode);
                        stringBuilder.append(str2);
                        stringBuilder.append(file2);
                        stringBuilder.append(str);
                        stringBuilder.append(versionCode);
                        throw new PackageParserException(-101, stringBuilder.toString());
                    }
                }
                if (apks.put(lite.splitName, lite) != null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Split name ");
                    stringBuilder.append(lite.splitName);
                    stringBuilder.append(" defined more than once; most recent was ");
                    stringBuilder.append(file2);
                    throw new PackageParserException(-101, stringBuilder.toString());
                }
            } else {
                i = flags;
            }
        }
        i = flags;
        Trace.traceEnd(262144);
        ApkLite baseApk = (ApkLite) apks.remove(null);
        if (baseApk != null) {
            String[] splitCodePaths;
            int[] splitRevisionCodes;
            int size = apks.size();
            String[] splitNames = null;
            boolean[] isFeatureSplits = null;
            String[] usesSplitNames = null;
            String[] configForSplits = null;
            if (size > 0) {
                isFeatureSplits = new boolean[size];
                usesSplitNames = new String[size];
                configForSplits = new String[size];
                String[] splitCodePaths2 = new String[size];
                int[] splitRevisionCodes2 = new int[size];
                splitNames = (String[]) apks.keySet().toArray(new String[size]);
                Arrays.sort(splitNames, sSplitNameComparator);
                for (int i2 = 0; i2 < size; i2++) {
                    ApkLite apk = (ApkLite) apks.get(splitNames[i2]);
                    usesSplitNames[i2] = apk.usesSplitName;
                    isFeatureSplits[i2] = apk.isFeatureSplit;
                    configForSplits[i2] = apk.configForSplit;
                    splitCodePaths2[i2] = apk.codePath;
                    splitRevisionCodes2[i2] = apk.revisionCode;
                }
                splitCodePaths = splitCodePaths2;
                splitRevisionCodes = splitRevisionCodes2;
            } else {
                splitCodePaths = null;
                splitRevisionCodes = null;
            }
            return new PackageLite(packageDir.getAbsolutePath(), baseApk, splitNames, isFeatureSplits, usesSplitNames, configForSplits, splitCodePaths, splitRevisionCodes);
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Missing base APK in ");
        stringBuilder2.append(packageDir);
        throw new PackageParserException(-101, stringBuilder2.toString());
    }

    @UnsupportedAppUsage
    public Package parsePackage(File packageFile, int flags, boolean useCaches) throws PackageParserException {
        Package parsed = useCaches ? getCachedResult(packageFile, flags) : null;
        if (parsed != null) {
            return parsed;
        }
        long j = 0;
        long parseTime = LOG_PARSE_TIMINGS ? SystemClock.uptimeMillis() : 0;
        if (packageFile.isDirectory()) {
            parsed = parseClusterPackage(packageFile, flags);
        } else {
            parsed = parseMonolithicPackage(packageFile, flags);
        }
        if (LOG_PARSE_TIMINGS) {
            j = SystemClock.uptimeMillis();
        }
        long cacheTime = j;
        cacheResult(packageFile, flags, parsed);
        if (LOG_PARSE_TIMINGS) {
            parseTime = cacheTime - parseTime;
            cacheTime = SystemClock.uptimeMillis() - cacheTime;
            if (parseTime + cacheTime > 100) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Parse times for '");
                stringBuilder.append(packageFile);
                stringBuilder.append("': parse=");
                stringBuilder.append(parseTime);
                stringBuilder.append("ms, update_cache=");
                stringBuilder.append(cacheTime);
                stringBuilder.append(" ms");
                Slog.i(TAG, stringBuilder.toString());
            }
        }
        return parsed;
    }

    @UnsupportedAppUsage
    public Package parsePackage(File packageFile, int flags) throws PackageParserException {
        return parsePackage(packageFile, flags, false);
    }

    private String getCacheKey(File packageFile, int flags) {
        StringBuilder sb = new StringBuilder(packageFile.getName());
        sb.append('-');
        sb.append(flags);
        return sb.toString();
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public Package fromCacheEntry(byte[] bytes) {
        return fromCacheEntryStatic(bytes);
    }

    @VisibleForTesting
    public static Package fromCacheEntryStatic(byte[] bytes) {
        Parcel p = Parcel.obtain();
        p.unmarshall(bytes, 0, bytes.length);
        p.setDataPosition(0);
        new ReadHelper(p).startAndInstall();
        Package pkg = new Package(p);
        p.recycle();
        sCachedPackageReadCount.incrementAndGet();
        return pkg;
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public byte[] toCacheEntry(Package pkg) {
        return toCacheEntryStatic(pkg);
    }

    @VisibleForTesting
    public static byte[] toCacheEntryStatic(Package pkg) {
        Parcel p = Parcel.obtain();
        WriteHelper helper = new WriteHelper(p);
        pkg.writeToParcel(p, 0);
        helper.finishAndUninstall();
        byte[] serialized = p.marshall();
        p.recycle();
        return serialized;
    }

    private static boolean isCacheUpToDate(File packageFile, File cacheFile) {
        boolean z = false;
        try {
            if (Os.stat(packageFile.getAbsolutePath()).st_mtime < Os.stat(cacheFile.getAbsolutePath()).st_mtime) {
                z = true;
            }
            return z;
        } catch (ErrnoException ee) {
            if (ee.errno != OsConstants.ENOENT) {
                Slog.w("Error while stating package cache : ", ee);
            }
            return false;
        }
    }

    private Package getCachedResult(File packageFile, int flags) {
        if (this.mCacheDir == null) {
            return null;
        }
        File cacheFile = new File(this.mCacheDir, getCacheKey(packageFile, flags));
        try {
            if (!isCacheUpToDate(packageFile, cacheFile)) {
                return null;
            }
            Package p = fromCacheEntry(IoUtils.readFileAsByteArray(cacheFile.getAbsolutePath()));
            if (this.mCallback != null) {
                String[] overlayApks = this.mCallback.getOverlayApks(p.packageName);
                if (overlayApks != null && overlayApks.length > 0) {
                    for (String overlayApk : overlayApks) {
                        if (!isCacheUpToDate(new File(overlayApk), cacheFile)) {
                            return null;
                        }
                    }
                }
            }
            return p;
        } catch (Throwable e) {
            Slog.w(TAG, "Error reading package cache: ", e);
            cacheFile.delete();
            return null;
        }
    }

    /* JADX WARNING: Missing block: B:23:?, code skipped:
            r4.close();
     */
    private void cacheResult(java.io.File r9, int r10, android.content.pm.PackageParser.Package r11) {
        /*
        r8 = this;
        r0 = "PackageParser";
        r1 = r8.mCacheDir;
        if (r1 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r1 = r8.getCacheKey(r9, r10);	 Catch:{ all -> 0x005b }
        r2 = new java.io.File;	 Catch:{ all -> 0x005b }
        r3 = r8.mCacheDir;	 Catch:{ all -> 0x005b }
        r2.<init>(r3, r1);	 Catch:{ all -> 0x005b }
        r3 = r2.exists();	 Catch:{ all -> 0x005b }
        if (r3 == 0) goto L_0x0032;
    L_0x0018:
        r3 = r2.delete();	 Catch:{ all -> 0x005b }
        if (r3 != 0) goto L_0x0032;
    L_0x001e:
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x005b }
        r3.<init>();	 Catch:{ all -> 0x005b }
        r4 = "Unable to delete cache file: ";
        r3.append(r4);	 Catch:{ all -> 0x005b }
        r3.append(r2);	 Catch:{ all -> 0x005b }
        r3 = r3.toString();	 Catch:{ all -> 0x005b }
        android.util.Slog.e(r0, r3);	 Catch:{ all -> 0x005b }
    L_0x0032:
        r3 = r8.toCacheEntry(r11);	 Catch:{ all -> 0x005b }
        if (r3 != 0) goto L_0x0039;
    L_0x0038:
        return;
    L_0x0039:
        r4 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0051 }
        r4.<init>(r2);	 Catch:{ IOException -> 0x0051 }
        r4.write(r3);	 Catch:{ all -> 0x0045 }
        r4.close();	 Catch:{ IOException -> 0x0051 }
        goto L_0x005a;
    L_0x0045:
        r5 = move-exception;
        throw r5;	 Catch:{ all -> 0x0047 }
    L_0x0047:
        r6 = move-exception;
        r4.close();	 Catch:{ all -> 0x004c }
        goto L_0x0050;
    L_0x004c:
        r7 = move-exception;
        r5.addSuppressed(r7);	 Catch:{ IOException -> 0x0051 }
    L_0x0050:
        throw r6;	 Catch:{ IOException -> 0x0051 }
    L_0x0051:
        r4 = move-exception;
        r5 = "Error writing cache entry.";
        android.util.Slog.w(r0, r5, r4);	 Catch:{ all -> 0x005b }
        r2.delete();	 Catch:{ all -> 0x005b }
    L_0x005a:
        goto L_0x0061;
    L_0x005b:
        r1 = move-exception;
        r2 = "Error saving package cache.";
        android.util.Slog.w(r0, r2, r1);
    L_0x0061:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.cacheResult(java.io.File, int, android.content.pm.PackageParser$Package):void");
    }

    private Package parseClusterPackage(File packageDir, int flags) throws PackageParserException {
        PackageLite lite = parseClusterPackageLite(packageDir, null);
        if (!this.mOnlyCoreApps || lite.coreApp) {
            SplitAssetLoader assetLoader;
            SparseArray<int[]> splitDependencies = null;
            if (!lite.isolatedSplits || ArrayUtils.isEmpty(lite.splitNames)) {
                assetLoader = new DefaultSplitAssetLoader(lite, flags);
            } else {
                try {
                    splitDependencies = SplitDependencyLoader.createDependenciesFromPackage(lite);
                    assetLoader = new SplitAssetDependencyLoader(lite, splitDependencies, flags);
                } catch (IllegalDependencyException e) {
                    throw new PackageParserException(-101, e.getMessage());
                }
            }
            try {
                AssetManager assets = assetLoader.getBaseAssetManager();
                File baseApk = new File(lite.baseCodePath);
                Package pkg = parseBaseApk(baseApk, assets, flags);
                if (pkg != null) {
                    if (!ArrayUtils.isEmpty(lite.splitNames)) {
                        int num = lite.splitNames.length;
                        pkg.splitNames = lite.splitNames;
                        pkg.splitCodePaths = lite.splitCodePaths;
                        pkg.splitRevisionCodes = lite.splitRevisionCodes;
                        pkg.splitFlags = new int[num];
                        pkg.splitPrivateFlags = new int[num];
                        pkg.applicationInfo.splitNames = pkg.splitNames;
                        pkg.applicationInfo.splitDependencies = splitDependencies;
                        pkg.applicationInfo.splitClassLoaderNames = new String[num];
                        for (int i = 0; i < num; i++) {
                            parseSplitApk(pkg, i, assetLoader.getSplitAssetManager(i), flags);
                        }
                    }
                    pkg.setCodePath(packageDir.getCanonicalPath());
                    pkg.setUse32bitAbi(lite.use32bitAbi);
                    IoUtils.closeQuietly(assetLoader);
                    return pkg;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to parse base APK: ");
                stringBuilder.append(baseApk);
                throw new PackageParserException(-100, stringBuilder.toString());
            } catch (IOException e2) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Failed to get path: ");
                stringBuilder2.append(lite.baseCodePath);
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, stringBuilder2.toString(), e2);
            } catch (Throwable th) {
                IoUtils.closeQuietly(assetLoader);
            }
        } else {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Not a coreApp: ");
            stringBuilder3.append(packageDir);
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, stringBuilder3.toString());
        }
    }

    @Deprecated
    @UnsupportedAppUsage
    public Package parseMonolithicPackage(File apkFile, int flags) throws PackageParserException {
        PackageLite lite = parseMonolithicPackageLite(apkFile, flags);
        if (!this.mOnlyCoreApps || lite.coreApp) {
            SplitAssetLoader assetLoader = new DefaultSplitAssetLoader(lite, flags);
            try {
                Package pkg = parseBaseApk(apkFile, assetLoader.getBaseAssetManager(), flags);
                pkg.setCodePath(apkFile.getCanonicalPath());
                pkg.setUse32bitAbi(lite.use32bitAbi);
                IoUtils.closeQuietly(assetLoader);
                return pkg;
            } catch (IOException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to get path: ");
                stringBuilder.append(apkFile);
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, stringBuilder.toString(), e);
            } catch (Throwable th) {
                IoUtils.closeQuietly(assetLoader);
            }
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Not a coreApp: ");
            stringBuilder2.append(apkFile);
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, stringBuilder2.toString());
        }
    }

    private Package parseBaseApk(File apkFile, AssetManager assets, int flags) throws PackageParserException {
        String volumeUuid;
        PackageParserException e;
        Resources resources;
        Exception e2;
        Throwable th;
        String apkPath = apkFile.getAbsolutePath();
        String str = MNT_EXPAND;
        if (apkPath.startsWith(str)) {
            volumeUuid = apkPath.substring(str.length(), apkPath.indexOf(47, str.length()));
        } else {
            volumeUuid = null;
        }
        this.mParseError = 1;
        this.mArchiveSourcePath = apkFile.getAbsolutePath();
        XmlResourceParser parser = null;
        XmlResourceParser parser2;
        StringBuilder stringBuilder;
        try {
            int cookie = assets.findCookieForPath(apkPath);
            if (cookie != 0) {
                parser2 = assets.openXmlResourceParser(cookie, ANDROID_MANIFEST_FILENAME);
                try {
                    Resources res = new MiuiResources(assets, this.mMetrics, null);
                    try {
                        String[] outError = new String[1];
                        Package pkg = parseBaseApk(apkPath, res, parser2, flags, outError);
                        if (pkg != null) {
                            pkg.setVolumeUuid(volumeUuid);
                            pkg.setApplicationVolumeUuid(volumeUuid);
                            pkg.setBaseCodePath(apkPath);
                            pkg.setSigningDetails(SigningDetails.UNKNOWN);
                            IoUtils.closeQuietly(parser2);
                            return pkg;
                        }
                        int i = this.mParseError;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(apkPath);
                        stringBuilder.append(" (at ");
                        stringBuilder.append(parser2.getPositionDescription());
                        stringBuilder.append("): ");
                        stringBuilder.append(outError[0]);
                        throw new PackageParserException(i, stringBuilder.toString());
                    } catch (PackageParserException e3) {
                        e = e3;
                        resources = res;
                        parser = parser2;
                        throw e;
                    } catch (Exception e4) {
                        e2 = e4;
                        resources = res;
                        parser = parser2;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed to read manifest from ");
                        stringBuilder.append(apkPath);
                        throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, stringBuilder.toString(), e2);
                    } catch (Throwable th2) {
                        th = th2;
                        resources = res;
                        IoUtils.closeQuietly(parser2);
                        throw th;
                    }
                } catch (PackageParserException e5) {
                    e = e5;
                    throw e;
                } catch (Exception e6) {
                    e2 = e6;
                    parser = parser2;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to read manifest from ");
                    stringBuilder.append(apkPath);
                    throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, stringBuilder.toString(), e2);
                } catch (Throwable th3) {
                    th = th3;
                    IoUtils.closeQuietly(parser2);
                    throw th;
                }
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Failed adding asset path: ");
            stringBuilder2.append(apkPath);
            throw new PackageParserException(-101, stringBuilder2.toString());
        } catch (PackageParserException e7) {
            e = e7;
            throw e;
        } catch (Exception e8) {
            e2 = e8;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to read manifest from ");
            stringBuilder.append(apkPath);
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, stringBuilder.toString(), e2);
        } catch (Throwable th4) {
            th = th4;
            parser2 = parser;
            IoUtils.closeQuietly(parser2);
            throw th;
        }
    }

    private void parseSplitApk(Package pkg, int splitIndex, AssetManager assets, int flags) throws PackageParserException {
        PackageParserException e;
        Exception e2;
        Package packageR;
        StringBuilder stringBuilder;
        Throwable th;
        AssetManager assetManager = assets;
        String apkPath = pkg.splitCodePaths[splitIndex];
        this.mParseError = 1;
        this.mArchiveSourcePath = apkPath;
        XmlResourceParser parser = null;
        try {
            int cookie = assetManager.findCookieForPath(apkPath);
            if (cookie != 0) {
                XmlResourceParser parser2 = assetManager.openXmlResourceParser(cookie, ANDROID_MANIFEST_FILENAME);
                try {
                    String[] outError = new String[1];
                    Package pkg2 = parseSplitApk(pkg, new Resources(assetManager, this.mMetrics, null), parser2, flags, splitIndex, outError);
                    if (pkg2 != null) {
                        IoUtils.closeQuietly(parser2);
                        return;
                    }
                    try {
                        int i = this.mParseError;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(apkPath);
                        stringBuilder2.append(" (at ");
                        stringBuilder2.append(parser2.getPositionDescription());
                        stringBuilder2.append("): ");
                        stringBuilder2.append(outError[0]);
                        throw new PackageParserException(i, stringBuilder2.toString());
                    } catch (PackageParserException e3) {
                        e = e3;
                        parser = parser2;
                        throw e;
                    } catch (Exception e4) {
                        e2 = e4;
                        packageR = pkg2;
                        parser = parser2;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed to read manifest from ");
                        stringBuilder.append(apkPath);
                        throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, stringBuilder.toString(), e2);
                    } catch (Throwable th2) {
                        th = th2;
                        packageR = pkg2;
                        parser = parser2;
                        IoUtils.closeQuietly(parser);
                        throw th;
                    }
                } catch (PackageParserException e5) {
                    e = e5;
                    throw e;
                } catch (Exception e6) {
                    e2 = e6;
                    parser = parser2;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to read manifest from ");
                    stringBuilder.append(apkPath);
                    throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, stringBuilder.toString(), e2);
                } catch (Throwable th3) {
                    th = th3;
                    parser = parser2;
                    IoUtils.closeQuietly(parser);
                    throw th;
                }
            }
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Failed adding asset path: ");
            stringBuilder3.append(apkPath);
            throw new PackageParserException(-101, stringBuilder3.toString());
        } catch (PackageParserException e7) {
            e = e7;
            throw e;
        } catch (Exception e8) {
            e2 = e8;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to read manifest from ");
            stringBuilder.append(apkPath);
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, stringBuilder.toString(), e2);
        } catch (Throwable th4) {
            th = th4;
            IoUtils.closeQuietly(parser);
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0079  */
    private android.content.pm.PackageParser.Package parseSplitApk(android.content.pm.PackageParser.Package r10, android.content.res.Resources r11, android.content.res.XmlResourceParser r12, int r13, int r14, java.lang.String[] r15) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException, android.content.pm.PackageParser.PackageParserException {
        /*
        r9 = this;
        r0 = r12;
        parsePackageSplitNames(r12, r0);
        r1 = 0;
        r9.mParseInstrumentationArgs = r1;
        r2 = 0;
        r3 = r12.getDepth();
    L_0x000c:
        r4 = r12.next();
        r5 = r4;
        r6 = 1;
        if (r4 == r6) goto L_0x0077;
    L_0x0014:
        r4 = 3;
        if (r5 != r4) goto L_0x001d;
    L_0x0017:
        r6 = r12.getDepth();
        if (r6 <= r3) goto L_0x0077;
    L_0x001d:
        if (r5 == r4) goto L_0x000c;
    L_0x001f:
        r4 = 4;
        if (r5 != r4) goto L_0x0023;
    L_0x0022:
        goto L_0x000c;
    L_0x0023:
        r4 = r12.getName();
        r6 = "application";
        r6 = r4.equals(r6);
        r7 = "PackageParser";
        if (r6 == 0) goto L_0x0045;
    L_0x0031:
        if (r2 == 0) goto L_0x003c;
    L_0x0033:
        r6 = "<manifest> has more than one <application>";
        android.util.Slog.w(r7, r6);
        com.android.internal.util.XmlUtils.skipCurrentTag(r12);
        goto L_0x000c;
    L_0x003c:
        r2 = 1;
        r6 = r9.parseSplitApplication(r10, r11, r12, r13, r14, r15);
        if (r6 != 0) goto L_0x0044;
    L_0x0043:
        return r1;
    L_0x0044:
        goto L_0x000c;
    L_0x0045:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r8 = "Unknown element under <manifest>: ";
        r6.append(r8);
        r8 = r12.getName();
        r6.append(r8);
        r8 = " at ";
        r6.append(r8);
        r8 = r9.mArchiveSourcePath;
        r6.append(r8);
        r8 = " ";
        r6.append(r8);
        r8 = r12.getPositionDescription();
        r6.append(r8);
        r6 = r6.toString();
        android.util.Slog.w(r7, r6);
        com.android.internal.util.XmlUtils.skipCurrentTag(r12);
        goto L_0x000c;
    L_0x0077:
        if (r2 != 0) goto L_0x0082;
    L_0x0079:
        r1 = 0;
        r4 = "<manifest> does not contain an <application>";
        r15[r1] = r4;
        r1 = -109; // 0xffffffffffffff93 float:NaN double:NaN;
        r9.mParseError = r1;
    L_0x0082:
        return r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseSplitApk(android.content.pm.PackageParser$Package, android.content.res.Resources, android.content.res.XmlResourceParser, int, int, java.lang.String[]):android.content.pm.PackageParser$Package");
    }

    public static ArraySet<PublicKey> toSigningKeys(Signature[] signatures) throws CertificateException {
        ArraySet<PublicKey> keys = new ArraySet(signatures.length);
        for (Signature publicKey : signatures) {
            keys.add(publicKey.getPublicKey());
        }
        return keys;
    }

    @UnsupportedAppUsage
    public static void collectCertificates(Package pkg, boolean skipVerify) throws PackageParserException {
        collectCertificatesInternal(pkg, skipVerify);
        int childCount = pkg.childPackages != null ? pkg.childPackages.size() : 0;
        for (int i = 0; i < childCount; i++) {
            ((Package) pkg.childPackages.get(i)).mSigningDetails = pkg.mSigningDetails;
        }
    }

    private static void collectCertificatesInternal(Package pkg, boolean skipVerify) throws PackageParserException {
        pkg.mSigningDetails = SigningDetails.UNKNOWN;
        Trace.traceBegin(262144, "collectCertificates");
        try {
            collectCertificates(pkg, new File(pkg.baseCodePath), skipVerify);
            if (!ArrayUtils.isEmpty(pkg.splitCodePaths)) {
                for (String file : pkg.splitCodePaths) {
                    collectCertificates(pkg, new File(file), skipVerify);
                }
            }
            Trace.traceEnd(262144);
        } catch (Throwable th) {
            Trace.traceEnd(262144);
        }
    }

    @UnsupportedAppUsage
    private static void collectCertificates(Package pkg, File apkFile, boolean skipVerify) throws PackageParserException {
        SigningDetails verified;
        String apkPath = apkFile.getAbsolutePath();
        int minSignatureScheme = 1;
        if (pkg.applicationInfo.isStaticSharedLibrary()) {
            minSignatureScheme = 2;
        }
        if (skipVerify) {
            verified = ApkSignatureVerifier.unsafeGetCertsWithoutVerification(apkPath, minSignatureScheme);
        } else {
            verified = ApkSignatureVerifier.verify(apkPath, minSignatureScheme);
        }
        if (pkg.mSigningDetails == SigningDetails.UNKNOWN) {
            pkg.mSigningDetails = verified;
        } else if (!Signature.areExactMatch(pkg.mSigningDetails.signatures, verified.signatures)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(apkPath);
            stringBuilder.append(" has mismatched certificates");
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES, stringBuilder.toString());
        }
    }

    private static AssetManager newConfiguredAssetManager() {
        AssetManager assetManager = new AssetManager();
        assetManager.setConfiguration(0, 0, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, VERSION.RESOURCES_SDK_INT);
        return assetManager;
    }

    public static ApkLite parseApkLite(File apkFile, int flags) throws PackageParserException {
        return parseApkLiteInner(apkFile, null, null, flags);
    }

    public static ApkLite parseApkLite(FileDescriptor fd, String debugPathName, int flags) throws PackageParserException {
        return parseApkLiteInner(null, fd, debugPathName, flags);
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x005d A:{Splitter:B:9:0x001a, PHI: r1 r2 , ExcHandler: RuntimeException | XmlPullParserException (r4_7 'e' java.lang.Exception)} */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x005d A:{Splitter:B:9:0x001a, PHI: r1 r2 , ExcHandler: RuntimeException | XmlPullParserException (r4_7 'e' java.lang.Exception)} */
    /* JADX WARNING: Missing block: B:31:0x005d, code skipped:
            r4 = move-exception;
     */
    /* JADX WARNING: Missing block: B:37:?, code skipped:
            r5 = TAG;
            r6 = new java.lang.StringBuilder();
            r6.append(r3);
            r6.append(r0);
            android.util.Slog.w(r5, r6.toString(), r4);
            r7 = new java.lang.StringBuilder();
            r7.append(r3);
            r7.append(r0);
     */
    /* JADX WARNING: Missing block: B:38:0x00a1, code skipped:
            throw new android.content.pm.PackageParser.PackageParserException(android.content.pm.PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, r7.toString(), r4);
     */
    /* JADX WARNING: Missing block: B:39:0x00a2, code skipped:
            libcore.io.IoUtils.closeQuietly(r1);
     */
    /* JADX WARNING: Missing block: B:40:0x00a5, code skipped:
            if (r2 != null) goto L_0x00a7;
     */
    /* JADX WARNING: Missing block: B:42:?, code skipped:
            r2.close();
     */
    private static android.content.pm.PackageParser.ApkLite parseApkLiteInner(java.io.File r9, java.io.FileDescriptor r10, java.lang.String r11, int r12) throws android.content.pm.PackageParser.PackageParserException {
        /*
        if (r10 == 0) goto L_0x0004;
    L_0x0002:
        r0 = r11;
        goto L_0x0008;
    L_0x0004:
        r0 = r9.getAbsolutePath();
    L_0x0008:
        r1 = 0;
        r2 = 0;
        r3 = "Failed to parse ";
        r4 = 0;
        if (r10 == 0) goto L_0x0014;
    L_0x000f:
        r5 = android.content.res.ApkAssets.loadFromFd(r10, r11, r4, r4);	 Catch:{ IOException -> 0x005f }
        goto L_0x0018;
    L_0x0014:
        r5 = android.content.res.ApkAssets.loadFromPath(r0);	 Catch:{ IOException -> 0x005f }
    L_0x0018:
        r2 = r5;
        r5 = "AndroidManifest.xml";
        r5 = r2.openXml(r5);	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        r1 = r5;
        r5 = r12 & 32;
        if (r5 == 0) goto L_0x004a;
    L_0x0025:
        r5 = new android.content.pm.PackageParser$Package;	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        r6 = 0;
        r6 = (java.lang.String) r6;	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        r5.<init>(r6);	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        r6 = r12 & 16;
        if (r6 == 0) goto L_0x0032;
    L_0x0031:
        r4 = 1;
    L_0x0032:
        r6 = "collectCertificates";
        r7 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        android.os.Trace.traceBegin(r7, r6);	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        collectCertificates(r5, r9, r4);	 Catch:{ all -> 0x0045 }
        android.os.Trace.traceEnd(r7);	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        r6 = r5.mSigningDetails;	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        r4 = r6;
        goto L_0x004c;
    L_0x0045:
        r6 = move-exception;
        android.os.Trace.traceEnd(r7);	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        throw r6;	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
    L_0x004a:
        r4 = android.content.pm.PackageParser.SigningDetails.UNKNOWN;	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
    L_0x004c:
        r5 = r1;
        r3 = parseApkLite(r0, r1, r5, r4);	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        libcore.io.IoUtils.closeQuietly(r1);
        r2.close();	 Catch:{ all -> 0x0059 }
        goto L_0x005a;
    L_0x0059:
        r6 = move-exception;
    L_0x005a:
        return r3;
    L_0x005b:
        r3 = move-exception;
        goto L_0x00a2;
    L_0x005d:
        r4 = move-exception;
        goto L_0x0077;
    L_0x005f:
        r4 = move-exception;
        r5 = new android.content.pm.PackageParser$PackageParserException;	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        r6 = -100;
        r7 = new java.lang.StringBuilder;	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        r7.<init>();	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        r7.append(r3);	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        r7.append(r0);	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        r7 = r7.toString();	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        r5.<init>(r6, r7);	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
        throw r5;	 Catch:{ RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d, RuntimeException | XmlPullParserException -> 0x005d }
    L_0x0077:
        r5 = "PackageParser";
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x005b }
        r6.<init>();	 Catch:{ all -> 0x005b }
        r6.append(r3);	 Catch:{ all -> 0x005b }
        r6.append(r0);	 Catch:{ all -> 0x005b }
        r6 = r6.toString();	 Catch:{ all -> 0x005b }
        android.util.Slog.w(r5, r6, r4);	 Catch:{ all -> 0x005b }
        r5 = new android.content.pm.PackageParser$PackageParserException;	 Catch:{ all -> 0x005b }
        r6 = -102; // 0xffffffffffffff9a float:NaN double:NaN;
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x005b }
        r7.<init>();	 Catch:{ all -> 0x005b }
        r7.append(r3);	 Catch:{ all -> 0x005b }
        r7.append(r0);	 Catch:{ all -> 0x005b }
        r3 = r7.toString();	 Catch:{ all -> 0x005b }
        r5.<init>(r6, r3, r4);	 Catch:{ all -> 0x005b }
        throw r5;	 Catch:{ all -> 0x005b }
    L_0x00a2:
        libcore.io.IoUtils.closeQuietly(r1);
        if (r2 == 0) goto L_0x00ac;
    L_0x00a7:
        r2.close();	 Catch:{ all -> 0x00ab }
        goto L_0x00ac;
    L_0x00ab:
        r4 = move-exception;
    L_0x00ac:
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseApkLiteInner(java.io.File, java.io.FileDescriptor, java.lang.String, int):android.content.pm.PackageParser$ApkLite");
    }

    private static String validateName(String name, boolean requireSeparator, boolean requireFilename) {
        int N = name.length();
        boolean hasSep = false;
        boolean front = true;
        for (int i = 0; i < N; i++) {
            char c = name.charAt(i);
            if ((c >= DateFormat.AM_PM && c <= DateFormat.TIME_ZONE) || (c >= DateFormat.CAPITAL_AM_PM && c <= 'Z')) {
                front = false;
            } else if (front || ((c < '0' || c > '9') && c != '_')) {
                if (c == ClassUtils.PACKAGE_SEPARATOR_CHAR) {
                    hasSep = true;
                    front = true;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("bad character '");
                    stringBuilder.append(c);
                    stringBuilder.append("'");
                    return stringBuilder.toString();
                }
            }
        }
        if (requireFilename && !FileUtils.isValidExtFilename(name)) {
            return "Invalid filename";
        }
        String str = (hasSep || !requireSeparator) ? null : "must have at least one '.' separator";
        return str;
    }

    private static Pair<String, String> parsePackageSplitNames(XmlPullParser parser, AttributeSet attrs) throws IOException, XmlPullParserException, PackageParserException {
        int type;
        while (true) {
            int next = parser.next();
            type = next;
            if (next == 2 || type == 1) {
            }
        }
        if (type != 2) {
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "No start tag found");
        } else if (parser.getName().equals(TAG_MANIFEST)) {
            String error;
            String packageName = attrs.getAttributeValue(null, "package");
            if (!"android".equals(packageName)) {
                error = validateName(packageName, true, true);
                if (error != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid manifest package: ");
                    stringBuilder.append(error);
                    throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME, stringBuilder.toString());
                }
            }
            String splitName = attrs.getAttributeValue(null, "split");
            if (splitName != null) {
                if (splitName.length() == 0) {
                    splitName = null;
                } else {
                    error = validateName(splitName, false, false);
                    if (error != null) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Invalid manifest split: ");
                        stringBuilder2.append(error);
                        throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME, stringBuilder2.toString());
                    }
                }
            }
            return Pair.create(packageName.intern(), splitName != null ? splitName.intern() : splitName);
        } else {
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "No <manifest> tag");
        }
    }

    private static ApkLite parseApkLite(String codePath, XmlPullParser parser, AttributeSet attrs, SigningDetails signingDetails) throws IOException, XmlPullParserException, PackageParserException {
        int targetSdkVersion;
        int minSdkVersion;
        String attr;
        int targetSdkVersion2;
        AttributeSet attributeSet = attrs;
        Pair<String, String> packageSplit = parsePackageSplitNames(parser, attrs);
        int targetSdkVersion3 = 0;
        int minSdkVersion2 = 1;
        String configForSplit = null;
        String usesSplitName = null;
        boolean isSplitRequired = false;
        boolean isFeatureSplit = false;
        boolean isolatedSplits = false;
        boolean coreApp = false;
        int revisionCode = 0;
        int versionCodeMajor = 0;
        int versionCode = 0;
        int installLocation = -1;
        int i = 0;
        while (true) {
            targetSdkVersion = targetSdkVersion3;
            minSdkVersion = minSdkVersion2;
            if (i >= attrs.getAttributeCount()) {
                break;
            }
            attr = attributeSet.getAttributeName(i);
            if (attr.equals("installLocation")) {
                installLocation = attributeSet.getAttributeIntValue(i, -1);
            } else if (attr.equals("versionCode")) {
                versionCode = attributeSet.getAttributeIntValue(i, 0);
            } else if (attr.equals("versionCodeMajor")) {
                versionCodeMajor = attributeSet.getAttributeIntValue(i, 0);
            } else if (attr.equals("revisionCode")) {
                revisionCode = attributeSet.getAttributeIntValue(i, 0);
            } else if (attr.equals("coreApp")) {
                coreApp = attributeSet.getAttributeBooleanValue(i, false);
            } else if (attr.equals("isolatedSplits")) {
                isolatedSplits = attributeSet.getAttributeBooleanValue(i, false);
            } else if (attr.equals("configForSplit")) {
                configForSplit = attributeSet.getAttributeValue(i);
            } else if (attr.equals("isFeatureSplit")) {
                isFeatureSplit = attributeSet.getAttributeBooleanValue(i, false);
            } else if (attr.equals("isSplitRequired")) {
                isSplitRequired = attributeSet.getAttributeBooleanValue(i, false);
            }
            i++;
            targetSdkVersion3 = targetSdkVersion;
            minSdkVersion2 = minSdkVersion;
        }
        targetSdkVersion3 = 1;
        i = parser.getDepth() + 1;
        List<VerifierInfo> verifiers = new ArrayList();
        boolean extractNativeLibs = true;
        boolean useEmbeddedDex = false;
        boolean multiArch = false;
        boolean use32bitAbi = false;
        int minSdkVersion3 = minSdkVersion;
        boolean debuggable = false;
        int targetSdkVersion4 = targetSdkVersion;
        while (true) {
            targetSdkVersion2 = targetSdkVersion4;
            targetSdkVersion4 = parser.next();
            int type = targetSdkVersion4;
            int i2;
            if (targetSdkVersion4 == targetSdkVersion3) {
                i2 = type;
                break;
            }
            targetSdkVersion3 = type;
            if (targetSdkVersion3 == 3 && parser.getDepth() < i) {
                type = i;
                i2 = targetSdkVersion3;
                break;
            }
            if (targetSdkVersion3 == 3) {
                type = i;
                i2 = targetSdkVersion3;
            } else if (targetSdkVersion3 == 4) {
                type = i;
            } else if (parser.getDepth() != i) {
                type = i;
            } else {
                type = i;
                if (TAG_PACKAGE_VERIFIER.equals(parser.getName()) != 0) {
                    i = parseVerifier(attrs);
                    if (i != 0) {
                        verifiers.add(i);
                    }
                } else {
                    if (TAG_APPLICATION.equals(parser.getName()) != 0) {
                        i = 0;
                        while (i < attrs.getAttributeCount()) {
                            String attr2 = attributeSet.getAttributeName(i);
                            i2 = targetSdkVersion3;
                            if ("debuggable".equals(attr2)) {
                                debuggable = attributeSet.getAttributeBooleanValue(i, false);
                            }
                            if ("multiArch".equals(attr2)) {
                                multiArch = attributeSet.getAttributeBooleanValue(i, false);
                            }
                            if ("use32bitAbi".equals(attr2)) {
                                use32bitAbi = attributeSet.getAttributeBooleanValue(i, false);
                            }
                            if ("extractNativeLibs".equals(attr2)) {
                                extractNativeLibs = attributeSet.getAttributeBooleanValue(i, true);
                            }
                            if ("useEmbeddedDex".equals(attr2)) {
                                useEmbeddedDex = attributeSet.getAttributeBooleanValue(i, false);
                            }
                            i++;
                            targetSdkVersion3 = i2;
                        }
                        targetSdkVersion4 = targetSdkVersion2;
                        i = type;
                        targetSdkVersion3 = 1;
                    } else {
                        if (TAG_USES_SPLIT.equals(parser.getName()) == 0) {
                            if (TAG_USES_SDK.equals(parser.getName()) != 0) {
                                i = 0;
                                targetSdkVersion4 = targetSdkVersion2;
                                while (i < attrs.getAttributeCount()) {
                                    attr = attributeSet.getAttributeName(i);
                                    minSdkVersion = targetSdkVersion4;
                                    if ("targetSdkVersion".equals(attr)) {
                                        minSdkVersion = attributeSet.getAttributeIntValue(i, 0);
                                    }
                                    if ("minSdkVersion".equals(attr)) {
                                        minSdkVersion3 = attributeSet.getAttributeIntValue(i, 1);
                                    }
                                    i++;
                                    targetSdkVersion4 = minSdkVersion;
                                }
                                targetSdkVersion3 = 1;
                                targetSdkVersion4 = targetSdkVersion4;
                                i = type;
                            }
                        } else if (usesSplitName != null) {
                            Slog.w(TAG, "Only one <uses-split> permitted. Ignoring others.");
                        } else {
                            usesSplitName = attributeSet.getAttributeValue(ANDROID_RESOURCES, "name");
                            if (usesSplitName != null) {
                                targetSdkVersion4 = targetSdkVersion2;
                                i = type;
                                targetSdkVersion3 = 1;
                            } else {
                                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "<uses-split> tag requires 'android:name' attribute");
                            }
                        }
                    }
                }
            }
            targetSdkVersion3 = 1;
            targetSdkVersion4 = targetSdkVersion2;
            i = type;
        }
        return new ApkLite(codePath, (String) packageSplit.first, (String) packageSplit.second, isFeatureSplit, configForSplit, usesSplitName, isSplitRequired, versionCode, versionCodeMajor, revisionCode, installLocation, verifiers, signingDetails, coreApp, debuggable, multiArch, use32bitAbi, useEmbeddedDex, extractNativeLibs, isolatedSplits, minSdkVersion3, targetSdkVersion2);
    }

    private boolean parseBaseApkChild(Package parentPkg, Resources res, XmlResourceParser parser, int flags, String[] outError) throws XmlPullParserException, IOException {
        Package packageR = parentPkg;
        String childPackageName = parser.getAttributeValue(null, "package");
        if (validateName(childPackageName, true, false) != null) {
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
            return false;
        }
        boolean equals = childPackageName.equals(packageR.packageName);
        String str = TAG;
        String message;
        if (equals) {
            message = new StringBuilder();
            message.append("Child package name cannot be equal to parent package name: ");
            message.append(packageR.packageName);
            message = message.toString();
            Slog.w(str, message);
            outError[0] = message;
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        } else if (parentPkg.hasChildPackage(childPackageName)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Duplicate child package:");
            stringBuilder.append(childPackageName);
            message = stringBuilder.toString();
            Slog.w(str, message);
            outError[0] = message;
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        } else {
            Package childPkg = new Package(childPackageName);
            childPkg.mVersionCode = packageR.mVersionCode;
            childPkg.baseRevisionCode = packageR.baseRevisionCode;
            childPkg.mVersionName = packageR.mVersionName;
            childPkg.applicationInfo.targetSdkVersion = packageR.applicationInfo.targetSdkVersion;
            childPkg.applicationInfo.minSdkVersion = packageR.applicationInfo.minSdkVersion;
            Package childPkg2 = parseBaseApkCommon(childPkg, CHILD_PACKAGE_TAGS, res, parser, flags, outError);
            if (childPkg2 == null) {
                return false;
            }
            if (packageR.childPackages == null) {
                packageR.childPackages = new ArrayList();
            }
            packageR.childPackages.add(childPkg2);
            childPkg2.parentPackage = packageR;
            return true;
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private Package parseBaseApk(String apkPath, Resources res, XmlResourceParser parser, int flags, String[] outError) throws XmlPullParserException, IOException {
        XmlResourceParser xmlResourceParser = parser;
        String str;
        try {
            Pair<String, String> packageSplit = parsePackageSplitNames(xmlResourceParser, xmlResourceParser);
            String pkgName = (String) packageSplit.first;
            String splitName = (String) packageSplit.second;
            if (TextUtils.isEmpty(splitName)) {
                String[] overlayPaths = this.mCallback;
                if (overlayPaths != null) {
                    overlayPaths = overlayPaths.getOverlayPaths(pkgName, apkPath);
                    if (overlayPaths != null && overlayPaths.length > 0) {
                        for (String overlayPath : overlayPaths) {
                            res.getAssets().addOverlayPath(overlayPath);
                        }
                    }
                } else {
                    str = apkPath;
                }
                Package pkg = new Package(pkgName);
                TypedArray sa = res.obtainAttributes(xmlResourceParser, R.styleable.AndroidManifest);
                pkg.mVersionCode = sa.getInteger(1, 0);
                pkg.mVersionCodeMajor = sa.getInteger(11, 0);
                pkg.applicationInfo.setVersionCode(pkg.getLongVersionCode());
                pkg.baseRevisionCode = sa.getInteger(5, 0);
                pkg.mVersionName = sa.getNonConfigurationString(2, 0);
                if (pkg.mVersionName != null) {
                    pkg.mVersionName = pkg.mVersionName.intern();
                }
                pkg.coreApp = xmlResourceParser.getAttributeBooleanValue(null, "coreApp", false);
                pkg.mCompileSdkVersion = sa.getInteger(9, 0);
                pkg.applicationInfo.compileSdkVersion = pkg.mCompileSdkVersion;
                pkg.mCompileSdkVersionCodename = sa.getNonConfigurationString(10, 0);
                if (pkg.mCompileSdkVersionCodename != null) {
                    pkg.mCompileSdkVersionCodename = pkg.mCompileSdkVersionCodename.intern();
                }
                pkg.applicationInfo.compileSdkVersionCodename = pkg.mCompileSdkVersionCodename;
                sa.recycle();
                return parseBaseApkCommon(pkg, null, res, parser, flags, outError);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Expected base APK, but found split ");
            stringBuilder.append(splitName);
            outError[0] = stringBuilder.toString();
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
            return null;
        } catch (PackageParserException e) {
            str = apkPath;
            Resources resources = res;
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
            return null;
        }
    }

    /* JADX WARNING: Missing block: B:60:0x0286, code skipped:
            return null;
     */
    private android.content.pm.PackageParser.Package parseBaseApkCommon(android.content.pm.PackageParser.Package r34, java.util.Set<java.lang.String> r35, android.content.res.Resources r36, android.content.res.XmlResourceParser r37, int r38, java.lang.String[] r39) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r33 = this;
        r6 = r33;
        r7 = r34;
        r8 = r35;
        r9 = r36;
        r10 = r37;
        r11 = r39;
        r12 = 0;
        r6.mParseInstrumentationArgs = r12;
        r0 = 0;
        r1 = com.android.internal.R.styleable.AndroidManifest;
        r1 = r9.obtainAttributes(r10, r1);
        r13 = 0;
        r14 = r1.getNonConfigurationString(r13, r13);
        r15 = 3;
        r5 = 1;
        if (r14 == 0) goto L_0x0061;
    L_0x001f:
        r2 = r14.length();
        if (r2 <= 0) goto L_0x0061;
    L_0x0025:
        r2 = validateName(r14, r5, r5);
        if (r2 == 0) goto L_0x0055;
    L_0x002b:
        r3 = r7.packageName;
        r4 = "android";
        r3 = r4.equals(r3);
        if (r3 != 0) goto L_0x0055;
    L_0x0035:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "<manifest> specifies bad sharedUserId name \"";
        r3.append(r4);
        r3.append(r14);
        r4 = "\": ";
        r3.append(r4);
        r3.append(r2);
        r3 = r3.toString();
        r11[r13] = r3;
        r3 = -107; // 0xffffffffffffff95 float:NaN double:NaN;
        r6.mParseError = r3;
        return r12;
    L_0x0055:
        r3 = r14.intern();
        r7.mSharedUserId = r3;
        r3 = r1.getResourceId(r15, r13);
        r7.mSharedUserLabel = r3;
    L_0x0061:
        r2 = -1;
        r4 = 4;
        r2 = r1.getInteger(r4, r2);
        r7.installLocation = r2;
        r2 = r7.applicationInfo;
        r3 = r7.installLocation;
        r2.installLocation = r3;
        r3 = 7;
        r2 = r1.getInteger(r3, r5);
        r3 = r7.applicationInfo;
        r3.targetSandboxVersion = r2;
        r3 = r38 & 8;
        if (r3 == 0) goto L_0x0086;
    L_0x007c:
        r3 = r7.applicationInfo;
        r12 = r3.flags;
        r17 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        r12 = r12 | r17;
        r3.flags = r12;
    L_0x0086:
        r12 = 6;
        r3 = r1.getBoolean(r12, r13);
        if (r3 == 0) goto L_0x0098;
    L_0x008d:
        r3 = r7.applicationInfo;
        r12 = r3.privateFlags;
        r18 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r12 = r12 | r18;
        r3.privateFlags = r12;
    L_0x0098:
        r3 = 1;
        r12 = 1;
        r18 = 1;
        r19 = 1;
        r20 = 1;
        r21 = 1;
        r13 = r37.getDepth();
        r22 = r19;
        r23 = r20;
        r24 = r21;
        r32 = r12;
        r12 = r0;
        r0 = r18;
        r18 = r1;
        r1 = r32;
    L_0x00b5:
        r4 = r37.next();
        r20 = r4;
        r15 = "PackageParser";
        if (r4 == r5) goto L_0x084b;
    L_0x00bf:
        r4 = r20;
        r5 = 3;
        if (r4 != r5) goto L_0x00e1;
    L_0x00c4:
        r5 = r37.getDepth();
        if (r5 <= r13) goto L_0x00cb;
    L_0x00ca:
        goto L_0x00e1;
    L_0x00cb:
        r17 = r1;
        r26 = r2;
        r8 = r3;
        r25 = r4;
        r31 = r12;
        r19 = r13;
        r16 = r14;
        r21 = r22;
        r22 = r23;
        r27 = r24;
        r14 = r0;
        goto L_0x085f;
    L_0x00e1:
        r5 = 3;
        if (r4 == r5) goto L_0x0815;
    L_0x00e4:
        r5 = 4;
        if (r4 != r5) goto L_0x0102;
    L_0x00e7:
        r17 = r1;
        r26 = r2;
        r8 = r3;
        r31 = r12;
        r19 = r13;
        r16 = r14;
        r21 = r22;
        r22 = r23;
        r27 = r24;
        r23 = 7;
        r24 = 3;
        r28 = 1;
        r14 = r0;
        r0 = 0;
        goto L_0x0830;
    L_0x0102:
        r19 = r13;
        r13 = r37.getName();
        r5 = " ";
        r26 = r0;
        r0 = " at ";
        if (r8 == 0) goto L_0x015d;
    L_0x0110:
        r27 = r8.contains(r13);
        if (r27 != 0) goto L_0x015d;
    L_0x0116:
        r27 = r1;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r28 = r2;
        r2 = "Skipping unsupported element under <manifest>: ";
        r1.append(r2);
        r1.append(r13);
        r1.append(r0);
        r0 = r6.mArchiveSourcePath;
        r1.append(r0);
        r1.append(r5);
        r0 = r37.getPositionDescription();
        r1.append(r0);
        r0 = r1.toString();
        android.util.Slog.w(r15, r0);
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r8 = r3;
        r31 = r12;
        r16 = r14;
        r21 = r22;
        r22 = r23;
        r14 = r26;
        r17 = r27;
        r26 = r28;
        r0 = 0;
        r23 = 7;
        r28 = 1;
        r27 = r24;
        r24 = 3;
        goto L_0x0830;
    L_0x015d:
        r27 = r1;
        r28 = r2;
        r1 = "application";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x01c3;
    L_0x0169:
        if (r12 == 0) goto L_0x018d;
    L_0x016b:
        r0 = "<manifest> has more than one <application>";
        android.util.Slog.w(r15, r0);
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r8 = r3;
        r31 = r12;
        r16 = r14;
        r21 = r22;
        r22 = r23;
        r14 = r26;
        r17 = r27;
        r26 = r28;
        r0 = 0;
        r23 = 7;
        r28 = 1;
        r27 = r24;
        r24 = 3;
        goto L_0x0830;
    L_0x018d:
        r12 = 1;
        r15 = r26;
        r0 = r33;
        r5 = r27;
        r1 = r34;
        r26 = r28;
        r2 = r36;
        r8 = r3;
        r16 = r14;
        r14 = 7;
        r3 = r37;
        r25 = r4;
        r14 = 4;
        r4 = r38;
        r29 = r5;
        r14 = 1;
        r5 = r39;
        r0 = r0.parseBaseApplication(r1, r2, r3, r4, r5);
        if (r0 != 0) goto L_0x01b2;
    L_0x01b0:
        r0 = 0;
        return r0;
    L_0x01b2:
        r3 = r8;
        r28 = r14;
        r14 = r15;
        r15 = r23;
        r4 = r24;
        r1 = r29;
        r0 = 0;
        r23 = 7;
        r24 = 3;
        goto L_0x0801;
    L_0x01c3:
        r8 = r3;
        r25 = r4;
        r16 = r14;
        r4 = r26;
        r29 = r27;
        r26 = r28;
        r14 = 1;
        r1 = "overlay";
        r1 = r13.equals(r1);
        r2 = 5;
        r3 = 2;
        if (r1 == 0) goto L_0x0287;
    L_0x01da:
        r0 = com.android.internal.R.styleable.AndroidManifestResourceOverlay;
        r0 = r9.obtainAttributes(r10, r0);
        r1 = r0.getString(r14);
        r7.mOverlayTarget = r1;
        r1 = 3;
        r5 = r0.getString(r1);
        r7.mOverlayTargetName = r5;
        r1 = r0.getString(r3);
        r7.mOverlayCategory = r1;
        r1 = 0;
        r3 = r0.getInt(r1, r1);
        r7.mOverlayPriority = r3;
        r3 = 4;
        r5 = r0.getBoolean(r3, r1);
        r7.mOverlayIsStatic = r5;
        r1 = r0.getString(r2);
        r2 = 6;
        r3 = r0.getString(r2);
        r0.recycle();
        r2 = r7.mOverlayTarget;
        r5 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        if (r2 != 0) goto L_0x021c;
    L_0x0213:
        r2 = "<overlay> does not specify a target package";
        r14 = 0;
        r11[r14] = r2;
        r6.mParseError = r5;
        r2 = 0;
        return r2;
    L_0x021c:
        r2 = r7.mOverlayPriority;
        if (r2 < 0) goto L_0x027e;
    L_0x0220:
        r2 = r7.mOverlayPriority;
        r14 = 9999; // 0x270f float:1.4012E-41 double:4.94E-320;
        if (r2 <= r14) goto L_0x0227;
    L_0x0226:
        goto L_0x027e;
    L_0x0227:
        r2 = r6.checkOverlayRequiredSystemProperty(r1, r3);
        if (r2 != 0) goto L_0x025f;
    L_0x022d:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r5 = "Skipping target and overlay pair ";
        r2.append(r5);
        r5 = r7.mOverlayTarget;
        r2.append(r5);
        r5 = " and ";
        r2.append(r5);
        r5 = r7.baseCodePath;
        r2.append(r5);
        r5 = ": overlay ignored due to required system property: ";
        r2.append(r5);
        r2.append(r1);
        r5 = " with value: ";
        r2.append(r5);
        r2.append(r3);
        r2 = r2.toString();
        android.util.Slog.i(r15, r2);
        r2 = 0;
        return r2;
    L_0x025f:
        r2 = r7.applicationInfo;
        r5 = r2.privateFlags;
        r14 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r5 = r5 | r14;
        r2.privateFlags = r5;
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r18 = r0;
        r14 = r4;
        r3 = r8;
        r15 = r23;
        r4 = r24;
        r1 = r29;
        r0 = 0;
        r23 = 7;
        r24 = 3;
        r28 = 1;
        goto L_0x0801;
    L_0x027e:
        r2 = "<overlay> priority must be between 0 and 9999";
        r14 = 0;
        r11[r14] = r2;
        r6.mParseError = r5;
        r14 = 0;
        return r14;
    L_0x0287:
        r14 = 0;
        r1 = "key-sets";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x02ab;
    L_0x0290:
        r0 = r6.parseKeySets(r7, r9, r10, r11);
        if (r0 != 0) goto L_0x0297;
    L_0x0296:
        return r14;
    L_0x0297:
        r14 = r4;
        r31 = r12;
        r21 = r22;
        r22 = r23;
        r27 = r24;
        r17 = r29;
        r0 = 0;
        r23 = 7;
        r24 = 3;
        r28 = 1;
        goto L_0x07f6;
    L_0x02ab:
        r1 = "permission-group";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x02dd;
    L_0x02b4:
        r0 = r33;
        r1 = r34;
        r2 = r38;
        r3 = r36;
        r14 = r4;
        r4 = r37;
        r5 = r39;
        r0 = r0.parsePermissionGroup(r1, r2, r3, r4, r5);
        if (r0 != 0) goto L_0x02c9;
    L_0x02c7:
        r1 = 0;
        return r1;
    L_0x02c9:
        r1 = 0;
        r0 = r1;
        r31 = r12;
        r21 = r22;
        r22 = r23;
        r27 = r24;
        r17 = r29;
        r23 = 7;
        r24 = 3;
        r28 = 1;
        goto L_0x07f6;
    L_0x02dd:
        r14 = r4;
        r1 = 0;
        r4 = "permission";
        r4 = r13.equals(r4);
        if (r4 == 0) goto L_0x0302;
    L_0x02e8:
        r0 = r6.parsePermission(r7, r9, r10, r11);
        if (r0 != 0) goto L_0x02ef;
    L_0x02ee:
        return r1;
    L_0x02ef:
        r0 = r1;
        r31 = r12;
        r21 = r22;
        r22 = r23;
        r27 = r24;
        r17 = r29;
        r23 = 7;
        r24 = 3;
        r28 = 1;
        goto L_0x07f6;
    L_0x0302:
        r4 = "permission-tree";
        r4 = r13.equals(r4);
        if (r4 == 0) goto L_0x0325;
    L_0x030b:
        r0 = r6.parsePermissionTree(r7, r9, r10, r11);
        if (r0 != 0) goto L_0x0312;
    L_0x0311:
        return r1;
    L_0x0312:
        r0 = r1;
        r31 = r12;
        r21 = r22;
        r22 = r23;
        r27 = r24;
        r17 = r29;
        r23 = 7;
        r24 = 3;
        r28 = 1;
        goto L_0x07f6;
    L_0x0325:
        r4 = "uses-permission";
        r4 = r13.equals(r4);
        if (r4 == 0) goto L_0x0348;
    L_0x032e:
        r0 = r6.parseUsesPermission(r7, r9, r10);
        if (r0 != 0) goto L_0x0335;
    L_0x0334:
        return r1;
    L_0x0335:
        r31 = r12;
        r21 = r22;
        r22 = r23;
        r27 = r24;
        r17 = r29;
        r0 = 0;
        r23 = 7;
        r24 = 3;
        r28 = 1;
        goto L_0x07f6;
    L_0x0348:
        r1 = "uses-permission-sdk-m";
        r1 = r13.equals(r1);
        if (r1 != 0) goto L_0x07dd;
    L_0x0351:
        r1 = "uses-permission-sdk-23";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x036c;
    L_0x035a:
        r31 = r12;
        r21 = r22;
        r22 = r23;
        r27 = r24;
        r17 = r29;
        r23 = 7;
        r24 = 3;
        r28 = 1;
        goto L_0x07ed;
    L_0x036c:
        r1 = "uses-configuration";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x03cc;
    L_0x0375:
        r0 = new android.content.pm.ConfigurationInfo;
        r0.<init>();
        r1 = com.android.internal.R.styleable.AndroidManifestUsesConfiguration;
        r1 = r9.obtainAttributes(r10, r1);
        r2 = 0;
        r4 = r1.getInt(r2, r2);
        r0.reqTouchScreen = r4;
        r4 = 1;
        r5 = r1.getInt(r4, r2);
        r0.reqKeyboardType = r5;
        r5 = r1.getBoolean(r3, r2);
        if (r5 == 0) goto L_0x0399;
    L_0x0394:
        r5 = r0.reqInputFeatures;
        r5 = r5 | r4;
        r0.reqInputFeatures = r5;
    L_0x0399:
        r4 = 3;
        r5 = r1.getInt(r4, r2);
        r0.reqNavigation = r5;
        r4 = 4;
        r5 = r1.getBoolean(r4, r2);
        if (r5 == 0) goto L_0x03ac;
    L_0x03a7:
        r2 = r0.reqInputFeatures;
        r2 = r2 | r3;
        r0.reqInputFeatures = r2;
    L_0x03ac:
        r1.recycle();
        r2 = r7.configPreferences;
        r2 = com.android.internal.util.ArrayUtils.add(r2, r0);
        r7.configPreferences = r2;
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r18 = r1;
        r3 = r8;
        r15 = r23;
        r4 = r24;
        r1 = r29;
        r0 = 0;
        r23 = 7;
        r24 = 3;
        r28 = 1;
        goto L_0x0801;
    L_0x03cc:
        r1 = "uses-feature";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x040c;
    L_0x03d5:
        r0 = r6.parseUsesFeature(r9, r10);
        r1 = r7.reqFeatures;
        r1 = com.android.internal.util.ArrayUtils.add(r1, r0);
        r7.reqFeatures = r1;
        r1 = r0.name;
        if (r1 != 0) goto L_0x03f6;
    L_0x03e5:
        r1 = new android.content.pm.ConfigurationInfo;
        r1.<init>();
        r2 = r0.reqGlEsVersion;
        r1.reqGlEsVersion = r2;
        r2 = r7.configPreferences;
        r2 = com.android.internal.util.ArrayUtils.add(r2, r1);
        r7.configPreferences = r2;
    L_0x03f6:
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r31 = r12;
        r21 = r22;
        r22 = r23;
        r27 = r24;
        r17 = r29;
        r0 = 0;
        r23 = 7;
        r24 = 3;
        r28 = 1;
        goto L_0x07f6;
    L_0x040c:
        r1 = "feature-group";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x04d4;
    L_0x0414:
        r1 = new android.content.pm.FeatureGroupInfo;
        r1.<init>();
        r2 = 0;
        r3 = r37.getDepth();
    L_0x041e:
        r4 = r37.next();
        r30 = r4;
        r31 = r12;
        r12 = 1;
        if (r4 == r12) goto L_0x04a2;
    L_0x0429:
        r4 = r30;
        r12 = 3;
        if (r4 != r12) goto L_0x043b;
    L_0x042e:
        r12 = r37.getDepth();
        if (r12 <= r3) goto L_0x0435;
    L_0x0434:
        goto L_0x043b;
    L_0x0435:
        r30 = r3;
        r25 = r4;
        goto L_0x04a6;
    L_0x043b:
        r12 = 3;
        if (r4 == r12) goto L_0x0498;
    L_0x043e:
        r12 = 4;
        if (r4 != r12) goto L_0x0446;
    L_0x0441:
        r30 = r3;
        r25 = r4;
        goto L_0x049c;
    L_0x0446:
        r12 = r37.getName();
        r30 = r3;
        r3 = "uses-feature";
        r3 = r12.equals(r3);
        if (r3 == 0) goto L_0x0468;
    L_0x0455:
        r3 = r6.parseUsesFeature(r9, r10);
        r25 = r4;
        r4 = r3.flags;
        r28 = 1;
        r4 = r4 | 1;
        r3.flags = r4;
        r2 = com.android.internal.util.ArrayUtils.add(r2, r3);
        goto L_0x0490;
    L_0x0468:
        r25 = r4;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Unknown element under <feature-group>: ";
        r3.append(r4);
        r3.append(r12);
        r3.append(r0);
        r4 = r6.mArchiveSourcePath;
        r3.append(r4);
        r3.append(r5);
        r4 = r37.getPositionDescription();
        r3.append(r4);
        r3 = r3.toString();
        android.util.Slog.w(r15, r3);
    L_0x0490:
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r3 = r30;
        r12 = r31;
        goto L_0x041e;
    L_0x0498:
        r30 = r3;
        r25 = r4;
    L_0x049c:
        r3 = r30;
        r12 = r31;
        goto L_0x041e;
    L_0x04a2:
        r25 = r30;
        r30 = r3;
    L_0x04a6:
        if (r2 == 0) goto L_0x04ba;
    L_0x04a8:
        r0 = r2.size();
        r0 = new android.content.pm.FeatureInfo[r0];
        r1.features = r0;
        r0 = r1.features;
        r0 = r2.toArray(r0);
        r0 = (android.content.pm.FeatureInfo[]) r0;
        r1.features = r0;
    L_0x04ba:
        r0 = r7.featureGroups;
        r0 = com.android.internal.util.ArrayUtils.add(r0, r1);
        r7.featureGroups = r0;
        r3 = r8;
        r15 = r23;
        r4 = r24;
        r1 = r29;
        r12 = r31;
        r0 = 0;
        r23 = 7;
        r24 = 3;
        r28 = 1;
        goto L_0x0801;
    L_0x04d4:
        r31 = r12;
        r1 = "uses-sdk";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x0568;
    L_0x04df:
        r0 = SDK_VERSION;
        if (r0 <= 0) goto L_0x0553;
    L_0x04e3:
        r0 = com.android.internal.R.styleable.AndroidManifestUsesSdk;
        r0 = r9.obtainAttributes(r10, r0);
        r1 = 1;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r12 = r0.peekValue(r5);
        if (r12 == 0) goto L_0x0506;
    L_0x04f4:
        r5 = r12.type;
        r15 = 3;
        if (r5 != r15) goto L_0x0504;
    L_0x04f9:
        r5 = r12.string;
        if (r5 == 0) goto L_0x0504;
    L_0x04fd:
        r5 = r12.string;
        r2 = r5.toString();
        goto L_0x0506;
    L_0x0504:
        r1 = r12.data;
    L_0x0506:
        r5 = 1;
        r12 = r0.peekValue(r5);
        if (r12 == 0) goto L_0x0523;
    L_0x050d:
        r5 = r12.type;
        r15 = 3;
        if (r5 != r15) goto L_0x0520;
    L_0x0512:
        r5 = r12.string;
        if (r5 == 0) goto L_0x0520;
    L_0x0516:
        r5 = r12.string;
        r4 = r5.toString();
        if (r2 != 0) goto L_0x0525;
    L_0x051e:
        r2 = r4;
        goto L_0x0525;
    L_0x0520:
        r3 = r12.data;
        goto L_0x0525;
    L_0x0523:
        r3 = r1;
        r4 = r2;
    L_0x0525:
        r0.recycle();
        r5 = SDK_VERSION;
        r15 = SDK_CODENAMES;
        r5 = computeMinSdkVersion(r1, r2, r5, r15, r11);
        if (r5 >= 0) goto L_0x0538;
    L_0x0532:
        r15 = -12;
        r6.mParseError = r15;
        r15 = 0;
        return r15;
    L_0x0538:
        r15 = 0;
        r15 = SDK_CODENAMES;
        r15 = computeTargetSdkVersion(r3, r4, r15, r11);
        if (r15 >= 0) goto L_0x0549;
    L_0x0541:
        r18 = r0;
        r0 = -12;
        r6.mParseError = r0;
        r0 = 0;
        return r0;
    L_0x0549:
        r18 = r0;
        r0 = r7.applicationInfo;
        r0.minSdkVersion = r5;
        r0 = r7.applicationInfo;
        r0.targetSdkVersion = r15;
    L_0x0553:
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r3 = r8;
        r15 = r23;
        r4 = r24;
        r1 = r29;
        r12 = r31;
        r0 = 0;
        r23 = 7;
        r24 = 3;
        r28 = 1;
        goto L_0x0801;
    L_0x0568:
        r1 = "supports-screens";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x05d2;
    L_0x0571:
        r0 = com.android.internal.R.styleable.AndroidManifestSupportsScreens;
        r0 = r9.obtainAttributes(r10, r0);
        r1 = r7.applicationInfo;
        r4 = 0;
        r12 = 6;
        r5 = r0.getInteger(r12, r4);
        r1.requiresSmallestWidthDp = r5;
        r1 = r7.applicationInfo;
        r5 = 7;
        r15 = r0.getInteger(r5, r4);
        r1.compatibleWidthLimitDp = r15;
        r1 = r7.applicationInfo;
        r15 = 8;
        r15 = r0.getInteger(r15, r4);
        r1.largestWidthLimitDp = r15;
        r4 = 1;
        r1 = r0.getInteger(r4, r8);
        r8 = r29;
        r3 = r0.getInteger(r3, r8);
        r8 = 3;
        r14 = r0.getInteger(r8, r14);
        r15 = r22;
        r2 = r0.getInteger(r2, r15);
        r15 = r23;
        r4 = 4;
        r15 = r0.getInteger(r4, r15);
        r4 = r24;
        r5 = 0;
        r4 = r0.getInteger(r5, r4);
        r0.recycle();
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r18 = r0;
        r22 = r2;
        r24 = r8;
        r12 = r31;
        r0 = 0;
        r23 = 7;
        r28 = 1;
        r32 = r3;
        r3 = r1;
        r1 = r32;
        goto L_0x0801;
    L_0x05d2:
        r21 = r22;
        r22 = r23;
        r4 = r24;
        r17 = r29;
        r12 = 6;
        r23 = 7;
        r24 = 3;
        r1 = "protected-broadcast";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x0627;
    L_0x05e8:
        r0 = com.android.internal.R.styleable.AndroidManifestProtectedBroadcast;
        r0 = r9.obtainAttributes(r10, r0);
        r1 = 0;
        r2 = r0.getNonResourceString(r1);
        r0.recycle();
        if (r2 == 0) goto L_0x0614;
    L_0x05f8:
        r1 = r7.protectedBroadcasts;
        if (r1 != 0) goto L_0x0603;
    L_0x05fc:
        r1 = new java.util.ArrayList;
        r1.<init>();
        r7.protectedBroadcasts = r1;
    L_0x0603:
        r1 = r7.protectedBroadcasts;
        r1 = r1.contains(r2);
        if (r1 != 0) goto L_0x0614;
    L_0x060b:
        r1 = r7.protectedBroadcasts;
        r3 = r2.intern();
        r1.add(r3);
    L_0x0614:
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r18 = r0;
        r3 = r8;
        r1 = r17;
        r15 = r22;
        r12 = r31;
        r0 = 0;
        r28 = 1;
        r22 = r21;
        goto L_0x0801;
    L_0x0627:
        r1 = "instrumentation";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x063e;
    L_0x062f:
        r0 = r6.parseInstrumentation(r7, r9, r10, r11);
        if (r0 != 0) goto L_0x0637;
    L_0x0635:
        r0 = 0;
        return r0;
    L_0x0637:
        r27 = r4;
        r0 = 0;
        r28 = 1;
        goto L_0x07f6;
    L_0x063e:
        r1 = "original-package";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x0684;
    L_0x0647:
        r0 = com.android.internal.R.styleable.AndroidManifestOriginalPackage;
        r0 = r9.obtainAttributes(r10, r0);
        r1 = 0;
        r2 = r0.getNonConfigurationString(r1, r1);
        r1 = r7.packageName;
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x066e;
    L_0x065a:
        r1 = r7.mOriginalPackages;
        if (r1 != 0) goto L_0x0669;
    L_0x065e:
        r1 = new java.util.ArrayList;
        r1.<init>();
        r7.mOriginalPackages = r1;
        r1 = r7.packageName;
        r7.mRealPackage = r1;
    L_0x0669:
        r1 = r7.mOriginalPackages;
        r1.add(r2);
    L_0x066e:
        r0.recycle();
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r18 = r0;
        r3 = r8;
        r1 = r17;
        r15 = r22;
        r12 = r31;
        r0 = 0;
        r28 = 1;
        r22 = r21;
        goto L_0x0801;
    L_0x0684:
        r1 = "adopt-permissions";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x06bf;
    L_0x068c:
        r0 = com.android.internal.R.styleable.AndroidManifestOriginalPackage;
        r0 = r9.obtainAttributes(r10, r0);
        r1 = 0;
        r2 = r0.getNonConfigurationString(r1, r1);
        r0.recycle();
        if (r2 == 0) goto L_0x06ac;
    L_0x069c:
        r1 = r7.mAdoptPermissions;
        if (r1 != 0) goto L_0x06a7;
    L_0x06a0:
        r1 = new java.util.ArrayList;
        r1.<init>();
        r7.mAdoptPermissions = r1;
    L_0x06a7:
        r1 = r7.mAdoptPermissions;
        r1.add(r2);
    L_0x06ac:
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r18 = r0;
        r3 = r8;
        r1 = r17;
        r15 = r22;
        r12 = r31;
        r0 = 0;
        r28 = 1;
        r22 = r21;
        goto L_0x0801;
    L_0x06bf:
        r1 = "uses-gl-texture";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x06d2;
    L_0x06c8:
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r27 = r4;
        r0 = 0;
        r28 = 1;
        goto L_0x0830;
    L_0x06d2:
        r1 = "compatible-screens";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x06e4;
    L_0x06da:
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r27 = r4;
        r0 = 0;
        r28 = 1;
        goto L_0x0830;
    L_0x06e4:
        r1 = "supports-input";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x06f7;
    L_0x06ed:
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r27 = r4;
        r0 = 0;
        r28 = 1;
        goto L_0x0830;
    L_0x06f7:
        r1 = "eat-comment";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x0709;
    L_0x06ff:
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r27 = r4;
        r0 = 0;
        r28 = 1;
        goto L_0x0830;
    L_0x0709:
        r1 = "package";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x073b;
    L_0x0712:
        r0 = MULTI_PACKAGE_APK_ENABLED;
        if (r0 != 0) goto L_0x0720;
    L_0x0716:
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r27 = r4;
        r0 = 0;
        r28 = 1;
        goto L_0x0830;
    L_0x0720:
        r0 = r33;
        r1 = r34;
        r2 = r36;
        r3 = r37;
        r27 = r4;
        r28 = 1;
        r4 = r38;
        r5 = r39;
        r0 = r0.parseBaseApkChild(r1, r2, r3, r4, r5);
        if (r0 != 0) goto L_0x0738;
    L_0x0736:
        r0 = 0;
        return r0;
    L_0x0738:
        r0 = 0;
        goto L_0x07f6;
    L_0x073b:
        r27 = r4;
        r28 = 1;
        r1 = "restrict-update";
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x07ae;
    L_0x0748:
        r0 = r38 & 16;
        if (r0 == 0) goto L_0x079a;
    L_0x074c:
        r0 = com.android.internal.R.styleable.AndroidManifestRestrictUpdate;
        r0 = r9.obtainAttributes(r10, r0);
        r1 = 0;
        r2 = r0.getNonConfigurationString(r1, r1);
        r0.recycle();
        r1 = 0;
        r7.restrictUpdateHash = r1;
        if (r2 == 0) goto L_0x0797;
    L_0x075f:
        r1 = r2.length();
        r3 = r1 / 2;
        r3 = new byte[r3];
        r4 = 0;
    L_0x0768:
        if (r4 >= r1) goto L_0x0792;
    L_0x076a:
        r5 = r4 / 2;
        r15 = r2.charAt(r4);
        r12 = 16;
        r15 = java.lang.Character.digit(r15, r12);
        r18 = 4;
        r15 = r15 << 4;
        r12 = r4 + 1;
        r12 = r2.charAt(r12);
        r30 = r0;
        r0 = 16;
        r0 = java.lang.Character.digit(r12, r0);
        r15 = r15 + r0;
        r0 = (byte) r15;
        r3[r5] = r0;
        r4 = r4 + 2;
        r0 = r30;
        r12 = 6;
        goto L_0x0768;
    L_0x0792:
        r30 = r0;
        r7.restrictUpdateHash = r3;
        goto L_0x079c;
    L_0x0797:
        r30 = r0;
        goto L_0x079c;
    L_0x079a:
        r30 = r18;
    L_0x079c:
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r3 = r8;
        r1 = r17;
        r15 = r22;
        r4 = r27;
        r18 = r30;
        r12 = r31;
        r0 = 0;
        r22 = r21;
        goto L_0x0801;
    L_0x07ae:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unknown element under <manifest>: ";
        r1.append(r2);
        r2 = r37.getName();
        r1.append(r2);
        r1.append(r0);
        r0 = r6.mArchiveSourcePath;
        r1.append(r0);
        r1.append(r5);
        r0 = r37.getPositionDescription();
        r1.append(r0);
        r0 = r1.toString();
        android.util.Slog.w(r15, r0);
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        r0 = 0;
        goto L_0x0830;
    L_0x07dd:
        r31 = r12;
        r21 = r22;
        r22 = r23;
        r27 = r24;
        r17 = r29;
        r23 = 7;
        r24 = 3;
        r28 = 1;
    L_0x07ed:
        r0 = r6.parseUsesPermission(r7, r9, r10);
        if (r0 != 0) goto L_0x07f5;
    L_0x07f3:
        r0 = 0;
        return r0;
    L_0x07f5:
        r0 = 0;
    L_0x07f6:
        r3 = r8;
        r1 = r17;
        r15 = r22;
        r4 = r27;
        r12 = r31;
        r22 = r21;
    L_0x0801:
        r8 = r35;
        r0 = r14;
        r23 = r15;
        r14 = r16;
        r13 = r19;
        r15 = r24;
        r2 = r26;
        r5 = r28;
        r24 = r4;
        r4 = 4;
        goto L_0x00b5;
    L_0x0815:
        r17 = r1;
        r26 = r2;
        r8 = r3;
        r25 = r4;
        r31 = r12;
        r19 = r13;
        r16 = r14;
        r21 = r22;
        r22 = r23;
        r27 = r24;
        r23 = 7;
        r28 = 1;
        r14 = r0;
        r24 = r5;
        r0 = 0;
    L_0x0830:
        r3 = r8;
        r0 = r14;
        r14 = r16;
        r1 = r17;
        r13 = r19;
        r23 = r22;
        r15 = r24;
        r2 = r26;
        r24 = r27;
        r5 = r28;
        r12 = r31;
        r4 = 4;
        r8 = r35;
        r22 = r21;
        goto L_0x00b5;
    L_0x084b:
        r17 = r1;
        r26 = r2;
        r8 = r3;
        r31 = r12;
        r19 = r13;
        r16 = r14;
        r25 = r20;
        r21 = r22;
        r22 = r23;
        r27 = r24;
        r14 = r0;
    L_0x085f:
        if (r31 != 0) goto L_0x0872;
    L_0x0861:
        r0 = r7.instrumentation;
        r0 = r0.size();
        if (r0 != 0) goto L_0x0872;
    L_0x0869:
        r0 = "<manifest> does not contain an <application> or <instrumentation>";
        r1 = 0;
        r11[r1] = r0;
        r0 = -109; // 0xffffffffffffff93 float:NaN double:NaN;
        r6.mParseError = r0;
    L_0x0872:
        r0 = NEW_PERMISSIONS;
        r0 = r0.length;
        r1 = 0;
        r2 = 0;
    L_0x0877:
        if (r2 >= r0) goto L_0x08c0;
    L_0x0879:
        r3 = NEW_PERMISSIONS;
        r3 = r3[r2];
        r4 = r7.applicationInfo;
        r4 = r4.targetSdkVersion;
        r5 = r3.sdkVersion;
        if (r4 < r5) goto L_0x0886;
    L_0x0885:
        goto L_0x08c0;
    L_0x0886:
        r4 = r7.requestedPermissions;
        r5 = r3.name;
        r4 = r4.contains(r5);
        if (r4 != 0) goto L_0x08bd;
    L_0x0890:
        if (r1 != 0) goto L_0x08a5;
    L_0x0892:
        r4 = new java.lang.StringBuilder;
        r5 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r4.<init>(r5);
        r1 = r4;
        r4 = r7.packageName;
        r1.append(r4);
        r4 = ": compat added ";
        r1.append(r4);
        goto L_0x08aa;
    L_0x08a5:
        r4 = 32;
        r1.append(r4);
    L_0x08aa:
        r4 = r3.name;
        r1.append(r4);
        r4 = r7.requestedPermissions;
        r5 = r3.name;
        r4.add(r5);
        r4 = r7.implicitPermissions;
        r5 = r3.name;
        r4.add(r5);
    L_0x08bd:
        r2 = r2 + 1;
        goto L_0x0877;
    L_0x08c0:
        if (r1 == 0) goto L_0x08c9;
    L_0x08c2:
        r2 = r1.toString();
        android.util.Slog.i(r15, r2);
    L_0x08c9:
        r2 = android.permission.PermissionManager.SPLIT_PERMISSIONS;
        r2 = r2.size();
        r3 = 0;
    L_0x08d0:
        if (r3 >= r2) goto L_0x091a;
    L_0x08d2:
        r4 = android.permission.PermissionManager.SPLIT_PERMISSIONS;
        r4 = r4.get(r3);
        r4 = (android.permission.PermissionManager.SplitPermissionInfo) r4;
        r5 = r7.applicationInfo;
        r5 = r5.targetSdkVersion;
        r12 = r4.getTargetSdk();
        if (r5 >= r12) goto L_0x0917;
    L_0x08e4:
        r5 = r7.requestedPermissions;
        r12 = r4.getSplitPermission();
        r5 = r5.contains(r12);
        if (r5 != 0) goto L_0x08f1;
    L_0x08f0:
        goto L_0x0917;
    L_0x08f1:
        r5 = r4.getNewPermissions();
        r12 = 0;
    L_0x08f6:
        r13 = r5.size();
        if (r12 >= r13) goto L_0x0917;
    L_0x08fc:
        r13 = r5.get(r12);
        r13 = (java.lang.String) r13;
        r15 = r7.requestedPermissions;
        r15 = r15.contains(r13);
        if (r15 != 0) goto L_0x0914;
    L_0x090a:
        r15 = r7.requestedPermissions;
        r15.add(r13);
        r15 = r7.implicitPermissions;
        r15.add(r13);
    L_0x0914:
        r12 = r12 + 1;
        goto L_0x08f6;
    L_0x0917:
        r3 = r3 + 1;
        goto L_0x08d0;
    L_0x091a:
        if (r8 < 0) goto L_0x0925;
    L_0x091c:
        if (r8 <= 0) goto L_0x092d;
    L_0x091e:
        r3 = r7.applicationInfo;
        r3 = r3.targetSdkVersion;
        r4 = 4;
        if (r3 < r4) goto L_0x092d;
    L_0x0925:
        r3 = r7.applicationInfo;
        r4 = r3.flags;
        r4 = r4 | 512;
        r3.flags = r4;
    L_0x092d:
        if (r17 == 0) goto L_0x0937;
    L_0x092f:
        r3 = r7.applicationInfo;
        r4 = r3.flags;
        r4 = r4 | 1024;
        r3.flags = r4;
    L_0x0937:
        if (r14 < 0) goto L_0x0942;
    L_0x0939:
        if (r14 <= 0) goto L_0x094a;
    L_0x093b:
        r3 = r7.applicationInfo;
        r3 = r3.targetSdkVersion;
        r4 = 4;
        if (r3 < r4) goto L_0x094a;
    L_0x0942:
        r3 = r7.applicationInfo;
        r4 = r3.flags;
        r4 = r4 | 2048;
        r3.flags = r4;
    L_0x094a:
        if (r21 < 0) goto L_0x0956;
    L_0x094c:
        if (r21 <= 0) goto L_0x095f;
    L_0x094e:
        r3 = r7.applicationInfo;
        r3 = r3.targetSdkVersion;
        r4 = 9;
        if (r3 < r4) goto L_0x095f;
    L_0x0956:
        r3 = r7.applicationInfo;
        r4 = r3.flags;
        r5 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
        r4 = r4 | r5;
        r3.flags = r4;
    L_0x095f:
        if (r22 < 0) goto L_0x096a;
    L_0x0961:
        if (r22 <= 0) goto L_0x0972;
    L_0x0963:
        r3 = r7.applicationInfo;
        r3 = r3.targetSdkVersion;
        r4 = 4;
        if (r3 < r4) goto L_0x0972;
    L_0x096a:
        r3 = r7.applicationInfo;
        r4 = r3.flags;
        r4 = r4 | 4096;
        r3.flags = r4;
    L_0x0972:
        if (r27 < 0) goto L_0x097d;
    L_0x0974:
        if (r27 <= 0) goto L_0x0985;
    L_0x0976:
        r3 = r7.applicationInfo;
        r3 = r3.targetSdkVersion;
        r4 = 4;
        if (r3 < r4) goto L_0x0985;
    L_0x097d:
        r3 = r7.applicationInfo;
        r4 = r3.flags;
        r4 = r4 | 8192;
        r3.flags = r4;
    L_0x0985:
        r3 = r7.applicationInfo;
        r3 = r3.usesCompatibilityMode();
        if (r3 == 0) goto L_0x0990;
    L_0x098d:
        r33.adjustPackageToBeUnresizeableAndUnpipable(r34);
    L_0x0990:
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseBaseApkCommon(android.content.pm.PackageParser$Package, java.util.Set, android.content.res.Resources, android.content.res.XmlResourceParser, int, java.lang.String[]):android.content.pm.PackageParser$Package");
    }

    private boolean checkOverlayRequiredSystemProperty(String propName, String propValue) {
        boolean z = true;
        if (!TextUtils.isEmpty(propName) && !TextUtils.isEmpty(propValue)) {
            String currValue = SystemProperties.get(propName);
            if (currValue == null || !currValue.equals(propValue)) {
                z = false;
            }
            return z;
        } else if (TextUtils.isEmpty(propName) && TextUtils.isEmpty(propValue)) {
            return true;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Disabling overlay - incomplete property :'");
            stringBuilder.append(propName);
            stringBuilder.append("=");
            stringBuilder.append(propValue);
            stringBuilder.append("' - require both requiredSystemPropertyName AND requiredSystemPropertyValue to be specified.");
            Slog.w(TAG, stringBuilder.toString());
            return false;
        }
    }

    private void adjustPackageToBeUnresizeableAndUnpipable(Package pkg) {
        Iterator it = pkg.activities.iterator();
        while (it.hasNext()) {
            Activity a = (Activity) it.next();
            a.info.resizeMode = 0;
            ActivityInfo activityInfo = a.info;
            activityInfo.flags &= -4194305;
        }
    }

    private static boolean matchTargetCode(String[] codeNames, String targetCode) {
        Object targetCodeName;
        int targetCodeIdx = targetCode.indexOf(46);
        if (targetCodeIdx == -1) {
            targetCodeName = targetCode;
        } else {
            targetCodeName = targetCode.substring(0, targetCodeIdx);
        }
        return ArrayUtils.contains((Object[]) codeNames, targetCodeName);
    }

    public static int computeTargetSdkVersion(int targetVers, String targetCode, String[] platformSdkCodenames, String[] outError) {
        if (targetCode == null) {
            return targetVers;
        }
        if (matchTargetCode(platformSdkCodenames, targetCode)) {
            return 10000;
        }
        String str = "Requires development platform ";
        StringBuilder stringBuilder;
        if (platformSdkCodenames.length > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(targetCode);
            stringBuilder.append(" (current platform is any of ");
            stringBuilder.append(Arrays.toString(platformSdkCodenames));
            stringBuilder.append(")");
            outError[0] = stringBuilder.toString();
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(targetCode);
            stringBuilder.append(" but this is a release platform.");
            outError[0] = stringBuilder.toString();
        }
        return -1;
    }

    public static int computeMinSdkVersion(int minVers, String minCode, int platformSdkVersion, String[] platformSdkCodenames, String[] outError) {
        String str = ")";
        StringBuilder stringBuilder;
        if (minCode == null) {
            if (minVers <= platformSdkVersion) {
                return minVers;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Requires newer sdk version #");
            stringBuilder.append(minVers);
            stringBuilder.append(" (current version is #");
            stringBuilder.append(platformSdkVersion);
            stringBuilder.append(str);
            outError[0] = stringBuilder.toString();
            return -1;
        } else if (matchTargetCode(platformSdkCodenames, minCode)) {
            return 10000;
        } else {
            String str2 = "Requires development platform ";
            if (platformSdkCodenames.length > 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str2);
                stringBuilder.append(minCode);
                stringBuilder.append(" (current platform is any of ");
                stringBuilder.append(Arrays.toString(platformSdkCodenames));
                stringBuilder.append(str);
                outError[0] = stringBuilder.toString();
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str2);
                stringBuilder2.append(minCode);
                stringBuilder2.append(" but this is a release platform.");
                outError[0] = stringBuilder2.toString();
            }
            return -1;
        }
    }

    private FeatureInfo parseUsesFeature(Resources res, AttributeSet attrs) {
        FeatureInfo fi = new FeatureInfo();
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestUsesFeature);
        fi.name = sa.getNonResourceString(0);
        fi.version = sa.getInt(3, 0);
        if (fi.name == null) {
            fi.reqGlEsVersion = sa.getInt(1, 0);
        }
        if (sa.getBoolean(2, true)) {
            fi.flags |= 1;
        }
        sa.recycle();
        return fi;
    }

    private boolean parseUsesStaticLibrary(Package pkg, Resources res, XmlResourceParser parser, String[] outError) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUsesStaticLibrary);
        String lname = sa.getNonResourceString(0);
        int version = sa.getInt(1, -1);
        String certSha256Digest = sa.getNonResourceString(2);
        sa.recycle();
        StringBuilder stringBuilder;
        if (lname == null || version < 0 || certSha256Digest == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Bad uses-static-library declaration name: ");
            stringBuilder.append(lname);
            stringBuilder.append(" version: ");
            stringBuilder.append(version);
            stringBuilder.append(" certDigest");
            stringBuilder.append(certSha256Digest);
            outError[0] = stringBuilder.toString();
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            XmlUtils.skipCurrentTag(parser);
            return false;
        } else if (pkg.usesStaticLibraries == null || !pkg.usesStaticLibraries.contains(lname)) {
            Object lname2 = lname.intern();
            certSha256Digest = certSha256Digest.replace(":", "").toLowerCase();
            String[] additionalCertSha256Digests = EmptyArray.STRING;
            if (pkg.applicationInfo.targetSdkVersion >= 27) {
                additionalCertSha256Digests = parseAdditionalCertificates(res, parser, outError);
                if (additionalCertSha256Digests == null) {
                    return false;
                }
            }
            XmlUtils.skipCurrentTag(parser);
            String[] certSha256Digests = new String[(additionalCertSha256Digests.length + 1)];
            certSha256Digests[0] = certSha256Digest;
            System.arraycopy(additionalCertSha256Digests, 0, certSha256Digests, 1, additionalCertSha256Digests.length);
            pkg.usesStaticLibraries = ArrayUtils.add(pkg.usesStaticLibraries, lname2);
            pkg.usesStaticLibrariesVersions = ArrayUtils.appendLong(pkg.usesStaticLibrariesVersions, (long) version, true);
            pkg.usesStaticLibrariesCertDigests = (String[][]) ArrayUtils.appendElement(String[].class, pkg.usesStaticLibrariesCertDigests, certSha256Digests, true);
            return true;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Depending on multiple versions of static library ");
            stringBuilder.append(lname);
            outError[0] = stringBuilder.toString();
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            XmlUtils.skipCurrentTag(parser);
            return false;
        }
    }

    private String[] parseAdditionalCertificates(Resources resources, XmlResourceParser parser, String[] outError) throws XmlPullParserException, IOException {
        String[] certSha256Digests = EmptyArray.STRING;
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                return certSha256Digests;
            }
            if (type != 3) {
                if (type != 4) {
                    if (parser.getName().equals("additional-certificate")) {
                        TypedArray sa = resources.obtainAttributes(parser, R.styleable.AndroidManifestAdditionalCertificate);
                        String certSha256Digest = sa.getNonResourceString(0);
                        sa.recycle();
                        if (TextUtils.isEmpty(certSha256Digest)) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Bad additional-certificate declaration with empty certDigest:");
                            stringBuilder.append(certSha256Digest);
                            outError[0] = stringBuilder.toString();
                            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                            XmlUtils.skipCurrentTag(parser);
                            sa.recycle();
                            return null;
                        }
                        certSha256Digests = (String[]) ArrayUtils.appendElement(String.class, certSha256Digests, certSha256Digest.replace(":", "").toLowerCase());
                    } else {
                        XmlUtils.skipCurrentTag(parser);
                    }
                }
            }
        }
        return certSha256Digests;
    }

    private boolean parseUsesPermission(Package pkg, Resources res, XmlResourceParser parser) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUsesPermission);
        String name = sa.getNonResourceString(0);
        int maxSdkVersion = 0;
        TypedValue val = sa.peekValue(1);
        if (val != null && val.type >= 16 && val.type <= 31) {
            maxSdkVersion = val.data;
        }
        String requiredFeature = sa.getNonConfigurationString(2, 0);
        String requiredNotfeature = sa.getNonConfigurationString(3, 0);
        sa.recycle();
        XmlUtils.skipCurrentTag(parser);
        if (name == null) {
            return true;
        }
        if (maxSdkVersion != 0 && maxSdkVersion < VERSION.RESOURCES_SDK_INT) {
            return true;
        }
        Callback callback;
        if (requiredFeature != null) {
            callback = this.mCallback;
            if (!(callback == null || callback.hasFeature(requiredFeature))) {
                return true;
            }
        }
        if (requiredNotfeature != null) {
            callback = this.mCallback;
            if (callback != null && callback.hasFeature(requiredNotfeature)) {
                return true;
            }
        }
        if (pkg.requestedPermissions.indexOf(name) == -1) {
            pkg.requestedPermissions.add(name.intern());
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Ignoring duplicate uses-permissions/uses-permissions-sdk-m: ");
            stringBuilder.append(name);
            stringBuilder.append(" in package: ");
            stringBuilder.append(pkg.packageName);
            stringBuilder.append(" at: ");
            stringBuilder.append(parser.getPositionDescription());
            Slog.w(TAG, stringBuilder.toString());
        }
        return true;
    }

    private static String buildClassName(String pkg, CharSequence clsSeq, String[] outError) {
        if (clsSeq == null || clsSeq.length() <= 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Empty class name in package ");
            stringBuilder.append(pkg);
            outError[0] = stringBuilder.toString();
            return null;
        }
        String cls = clsSeq.toString();
        if (cls.charAt(0) == ClassUtils.PACKAGE_SEPARATOR_CHAR) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(pkg);
            stringBuilder2.append(cls);
            return stringBuilder2.toString();
        } else if (cls.indexOf(46) >= 0) {
            return cls;
        } else {
            StringBuilder b = new StringBuilder(pkg);
            b.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            b.append(cls);
            return b.toString();
        }
    }

    private static String buildCompoundName(String pkg, CharSequence procSeq, String type, String[] outError) {
        String proc = procSeq.toString();
        char c = proc.charAt(0);
        String str = ": ";
        String str2 = "Invalid ";
        String str3 = " in package ";
        String str4 = " name ";
        if (pkg == null || c != ':') {
            String nameError = validateName(proc, true, false);
            if (nameError == null || "system".equals(proc)) {
                return proc;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(type);
            stringBuilder.append(str4);
            stringBuilder.append(proc);
            stringBuilder.append(str3);
            stringBuilder.append(pkg);
            stringBuilder.append(str);
            stringBuilder.append(nameError);
            outError[0] = stringBuilder.toString();
            return null;
        } else if (proc.length() < 2) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Bad ");
            stringBuilder2.append(type);
            stringBuilder2.append(str4);
            stringBuilder2.append(proc);
            stringBuilder2.append(str3);
            stringBuilder2.append(pkg);
            stringBuilder2.append(": must be at least two characters");
            outError[0] = stringBuilder2.toString();
            return null;
        } else {
            String nameError2 = validateName(proc.substring(1), false, false);
            if (nameError2 != null) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str2);
                stringBuilder3.append(type);
                stringBuilder3.append(str4);
                stringBuilder3.append(proc);
                stringBuilder3.append(str3);
                stringBuilder3.append(pkg);
                stringBuilder3.append(str);
                stringBuilder3.append(nameError2);
                outError[0] = stringBuilder3.toString();
                return null;
            }
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(pkg);
            stringBuilder4.append(proc);
            return stringBuilder4.toString();
        }
    }

    private static String buildProcessName(String pkg, String defProc, CharSequence procSeq, int flags, String[] separateProcesses, String[] outError) {
        if ((flags & 2) == 0 || "system".equals(procSeq)) {
            if (separateProcesses != null) {
                for (int i = separateProcesses.length - 1; i >= 0; i--) {
                    String sp = separateProcesses[i];
                    if (sp.equals(pkg) || sp.equals(defProc) || sp.equals(procSeq)) {
                        return pkg;
                    }
                }
            }
            if (procSeq == null || procSeq.length() <= 0) {
                return defProc;
            }
            return TextUtils.safeIntern(buildCompoundName(pkg, procSeq, DumpHeapActivity.KEY_PROCESS, outError));
        }
        return defProc != null ? defProc : pkg;
    }

    private static String buildTaskAffinityName(String pkg, String defProc, CharSequence procSeq, String[] outError) {
        if (procSeq == null) {
            return defProc;
        }
        if (procSeq.length() <= 0) {
            return null;
        }
        return buildCompoundName(pkg, procSeq, "taskAffinity", outError);
    }

    private boolean parseKeySets(Package owner, Resources res, XmlResourceParser parser, String[] outError) throws XmlPullParserException, IOException {
        String str;
        String tagName;
        Package packageR = owner;
        Resources resources = res;
        XmlResourceParser xmlResourceParser = parser;
        int outerDepth = parser.getDepth();
        int currentKeySetDepth = -1;
        String currentKeySet = null;
        ArrayMap<String, PublicKey> publicKeys = new ArrayMap();
        ArraySet<String> upgradeKeySets = new ArraySet();
        ArrayMap<String, ArraySet<String>> definedKeySets = new ArrayMap();
        ArraySet<String> improperKeySets = new ArraySet();
        while (true) {
            int next = parser.next();
            int type = next;
            str = TAG;
            int i;
            int i2;
            if (next != 1) {
                int i3;
                if (type == 3 && parser.getDepth() <= outerDepth) {
                    i3 = outerDepth;
                    i = currentKeySetDepth;
                    i2 = type;
                    break;
                }
                if (type != 3) {
                    tagName = parser.getName();
                    String str2 = "Improperly nested 'key-set' tag at ";
                    StringBuilder stringBuilder;
                    TypedArray sa;
                    if (!tagName.equals("key-set")) {
                        if (!tagName.equals("public-key")) {
                            i3 = outerDepth;
                            i = currentKeySetDepth;
                            i2 = type;
                            if (tagName.equals("upgrade-key-set")) {
                                TypedArray sa2 = resources.obtainAttributes(xmlResourceParser, R.styleable.AndroidManifestUpgradeKeySet);
                                upgradeKeySets.add(sa2.getNonResourceString(null));
                                sa2.recycle();
                                XmlUtils.skipCurrentTag(parser);
                            } else {
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("Unknown element under <key-sets>: ");
                                stringBuilder2.append(parser.getName());
                                stringBuilder2.append(" at ");
                                stringBuilder2.append(this.mArchiveSourcePath);
                                stringBuilder2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                                stringBuilder2.append(parser.getPositionDescription());
                                Slog.w(str, stringBuilder2.toString());
                                XmlUtils.skipCurrentTag(parser);
                            }
                        } else if (currentKeySet == null) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str2);
                            stringBuilder.append(parser.getPositionDescription());
                            outError[0] = stringBuilder.toString();
                            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                            return false;
                        } else {
                            sa = resources.obtainAttributes(xmlResourceParser, R.styleable.AndroidManifestPublicKey);
                            i3 = outerDepth;
                            outerDepth = sa.getNonResourceString(0);
                            str2 = sa.getNonResourceString(1);
                            if (str2 == null && publicKeys.get(outerDepth) == null) {
                                StringBuilder stringBuilder3 = new StringBuilder();
                                stringBuilder3.append("'public-key' ");
                                stringBuilder3.append(outerDepth);
                                stringBuilder3.append(" must define a public-key value on first use at ");
                                stringBuilder3.append(parser.getPositionDescription());
                                outError[0] = stringBuilder3.toString();
                                this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                sa.recycle();
                                return false;
                            }
                            i = currentKeySetDepth;
                            String str3;
                            if (str2 != null) {
                                PublicKey currentKey = parsePublicKey(str2);
                                StringBuilder stringBuilder4;
                                if (currentKey == null) {
                                    stringBuilder4 = new StringBuilder();
                                    stringBuilder4.append("No recognized valid key in 'public-key' tag at ");
                                    stringBuilder4.append(parser.getPositionDescription());
                                    stringBuilder4.append(" key-set ");
                                    stringBuilder4.append(currentKeySet);
                                    stringBuilder4.append(" will not be added to the package's defined key-sets.");
                                    Slog.w(str, stringBuilder4.toString());
                                    sa.recycle();
                                    improperKeySets.add(currentKeySet);
                                    XmlUtils.skipCurrentTag(parser);
                                } else {
                                    str3 = str2;
                                    if (publicKeys.get(outerDepth) == null || ((PublicKey) publicKeys.get(outerDepth)).equals(currentKey)) {
                                        publicKeys.put(outerDepth, currentKey);
                                    } else {
                                        stringBuilder4 = new StringBuilder();
                                        stringBuilder4.append("Value of 'public-key' ");
                                        stringBuilder4.append(outerDepth);
                                        stringBuilder4.append(" conflicts with previously defined value at ");
                                        stringBuilder4.append(parser.getPositionDescription());
                                        outError[0] = stringBuilder4.toString();
                                        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                        sa.recycle();
                                        return false;
                                    }
                                }
                            }
                            str3 = str2;
                            ((ArraySet) definedKeySets.get(currentKeySet)).add(outerDepth);
                            sa.recycle();
                            XmlUtils.skipCurrentTag(parser);
                        }
                        currentKeySetDepth = i;
                    } else if (currentKeySet != null) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str2);
                        stringBuilder.append(parser.getPositionDescription());
                        outError[0] = stringBuilder.toString();
                        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                        return false;
                    } else {
                        sa = resources.obtainAttributes(xmlResourceParser, R.styleable.AndroidManifestKeySet);
                        str = sa.getNonResourceString(null);
                        definedKeySets.put(str, new ArraySet());
                        currentKeySet = str;
                        currentKeySetDepth = parser.getDepth();
                        sa.recycle();
                        i3 = outerDepth;
                        i2 = type;
                    }
                    outerDepth = i3;
                } else if (parser.getDepth() == currentKeySetDepth) {
                    currentKeySet = null;
                    currentKeySetDepth = -1;
                } else {
                    i3 = outerDepth;
                    i = currentKeySetDepth;
                }
                outerDepth = i3;
                currentKeySetDepth = i;
            } else {
                i = currentKeySetDepth;
                i2 = type;
                break;
            }
        }
        tagName = "Package";
        if (publicKeys.keySet().removeAll(definedKeySets.keySet())) {
            StringBuilder stringBuilder5 = new StringBuilder();
            stringBuilder5.append(tagName);
            stringBuilder5.append(packageR.packageName);
            stringBuilder5.append(" AndroidManifext.xml 'key-set' and 'public-key' names must be distinct.");
            outError[0] = stringBuilder5.toString();
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        packageR.mKeySetMapping = new ArrayMap();
        for (Entry<String, ArraySet<String>> e : definedKeySets.entrySet()) {
            String keySetName = (String) e.getKey();
            String str4 = " AndroidManifext.xml 'key-set' ";
            if (((ArraySet) e.getValue()).size() == 0) {
                StringBuilder stringBuilder6 = new StringBuilder();
                stringBuilder6.append(tagName);
                stringBuilder6.append(packageR.packageName);
                stringBuilder6.append(str4);
                stringBuilder6.append(keySetName);
                stringBuilder6.append(" has no valid associated 'public-key'. Not including in package's defined key-sets.");
                Slog.w(str, stringBuilder6.toString());
                resources = res;
                xmlResourceParser = parser;
            } else if (improperKeySets.contains(keySetName)) {
                StringBuilder stringBuilder7 = new StringBuilder();
                stringBuilder7.append(tagName);
                stringBuilder7.append(packageR.packageName);
                stringBuilder7.append(str4);
                stringBuilder7.append(keySetName);
                stringBuilder7.append(" contained improper 'public-key' tags. Not including in package's defined key-sets.");
                Slog.w(str, stringBuilder7.toString());
                resources = res;
                xmlResourceParser = parser;
            } else {
                packageR.mKeySetMapping.put(keySetName, new ArraySet());
                Iterator it = ((ArraySet) e.getValue()).iterator();
                while (it.hasNext()) {
                    Iterator it2 = it;
                    ((ArraySet) packageR.mKeySetMapping.get(keySetName)).add((PublicKey) publicKeys.get((String) it.next()));
                    it = it2;
                }
                resources = res;
                xmlResourceParser = parser;
            }
        }
        if (packageR.mKeySetMapping.keySet().containsAll(upgradeKeySets)) {
            packageR.mUpgradeKeySets = upgradeKeySets;
            return true;
        }
        StringBuilder stringBuilder8 = new StringBuilder();
        stringBuilder8.append(tagName);
        stringBuilder8.append(packageR.packageName);
        stringBuilder8.append(" AndroidManifext.xml does not define all 'upgrade-key-set's .");
        outError[0] = stringBuilder8.toString();
        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        return false;
    }

    private boolean parsePermissionGroup(Package owner, int flags, Resources res, XmlResourceParser parser, String[] outError) throws XmlPullParserException, IOException {
        Package packageR = owner;
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestPermissionGroup);
        int requestDetailResourceId = sa.getResourceId(12, 0);
        int backgroundRequestResourceId = sa.getResourceId(9, 0);
        int backgroundRequestDetailResourceId = sa.getResourceId(10, 0);
        Component perm = new PermissionGroup(packageR, requestDetailResourceId, backgroundRequestResourceId, backgroundRequestDetailResourceId);
        Component perm2 = perm;
        if (parsePackageItemInfo(owner, perm.info, outError, "<permission-group>", sa, true, 2, 0, 1, 8, 5, 7)) {
            Component perm3 = perm2;
            perm3.info.descriptionRes = sa.getResourceId(4, 0);
            perm3.info.requestRes = sa.getResourceId(11, 0);
            perm3.info.flags = sa.getInt(6, 0);
            perm3.info.priority = sa.getInt(3, 0);
            sa.recycle();
            boolean z = false;
            Package packageR2 = packageR;
            if (parseAllMetaData(res, parser, "<permission-group>", perm3, outError)) {
                packageR2.permissionGroups.add(perm3);
                return true;
            }
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return z;
        }
        sa.recycle();
        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0113  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x010c  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0072  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x006c  */
    private boolean parsePermission(android.content.pm.PackageParser.Package r22, android.content.res.Resources r23, android.content.res.XmlResourceParser r24, java.lang.String[] r25) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r21 = this;
        r6 = r21;
        r5 = r22;
        r0 = com.android.internal.R.styleable.AndroidManifestPermission;
        r4 = r23;
        r3 = r24;
        r2 = r4.obtainAttributes(r3, r0);
        r0 = 0;
        r1 = 10;
        r7 = r2.hasValue(r1);
        r15 = "android";
        if (r7 == 0) goto L_0x003f;
    L_0x0019:
        r7 = r5.packageName;
        r7 = r15.equals(r7);
        if (r7 == 0) goto L_0x0027;
    L_0x0021:
        r0 = r2.getNonResourceString(r1);
        r1 = r0;
        goto L_0x0040;
    L_0x0027:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r7 = r5.packageName;
        r1.append(r7);
        r7 = " defines a background permission. Only the 'android' package can do that.";
        r1.append(r7);
        r1 = r1.toString();
        r7 = "PackageParser";
        android.util.Slog.w(r7, r1);
    L_0x003f:
        r1 = r0;
    L_0x0040:
        r0 = new android.content.pm.PackageParser$Permission;
        r0.<init>(r5, r1);
        r8 = r0.info;
        r12 = 1;
        r13 = 2;
        r14 = 0;
        r16 = 1;
        r17 = 9;
        r18 = 6;
        r19 = 8;
        r10 = "<permission>";
        r7 = r22;
        r9 = r25;
        r11 = r2;
        r20 = r15;
        r15 = r16;
        r16 = r17;
        r17 = r18;
        r18 = r19;
        r7 = parsePackageItemInfo(r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18);
        r8 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r9 = 0;
        if (r7 != 0) goto L_0x0072;
    L_0x006c:
        r2.recycle();
        r6.mParseError = r8;
        return r9;
    L_0x0072:
        r7 = r0.info;
        r10 = 4;
        r11 = r2.getNonResourceString(r10);
        r7.group = r11;
        r7 = r0.info;
        r7 = r7.group;
        if (r7 == 0) goto L_0x008d;
    L_0x0081:
        r7 = r0.info;
        r11 = r0.info;
        r11 = r11.group;
        r11 = r11.intern();
        r7.group = r11;
    L_0x008d:
        r7 = r0.info;
        r11 = 5;
        r11 = r2.getResourceId(r11, r9);
        r7.descriptionRes = r11;
        r7 = r0.info;
        r11 = 11;
        r11 = r2.getResourceId(r11, r9);
        r7.requestRes = r11;
        r7 = r0.info;
        r11 = 3;
        r11 = r2.getInt(r11, r9);
        r7.protectionLevel = r11;
        r7 = r0.info;
        r11 = 7;
        r11 = r2.getInt(r11, r9);
        r7.flags = r11;
        r7 = r0.info;
        r7 = r7.isRuntime();
        if (r7 == 0) goto L_0x00f2;
    L_0x00ba:
        r7 = r0.info;
        r7 = r7.packageName;
        r11 = r20;
        r7 = r11.equals(r7);
        if (r7 != 0) goto L_0x00c7;
    L_0x00c6:
        goto L_0x00f2;
    L_0x00c7:
        r7 = r0.info;
        r7 = r7.flags;
        r7 = r7 & r10;
        if (r7 == 0) goto L_0x0102;
    L_0x00ce:
        r7 = r0.info;
        r7 = r7.flags;
        r7 = r7 & 8;
        if (r7 != 0) goto L_0x00d7;
    L_0x00d6:
        goto L_0x0102;
    L_0x00d7:
        r7 = new java.lang.IllegalStateException;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "Permission cannot be both soft and hard restricted: ";
        r8.append(r9);
        r9 = r0.info;
        r9 = r9.name;
        r8.append(r9);
        r8 = r8.toString();
        r7.<init>(r8);
        throw r7;
    L_0x00f2:
        r7 = r0.info;
        r10 = r7.flags;
        r10 = r10 & -5;
        r7.flags = r10;
        r7 = r0.info;
        r10 = r7.flags;
        r10 = r10 & -9;
        r7.flags = r10;
    L_0x0102:
        r2.recycle();
        r7 = r0.info;
        r7 = r7.protectionLevel;
        r10 = -1;
        if (r7 != r10) goto L_0x0113;
    L_0x010c:
        r7 = "<permission> does not specify protectionLevel";
        r25[r9] = r7;
        r6.mParseError = r8;
        return r9;
    L_0x0113:
        r7 = r0.info;
        r10 = r0.info;
        r10 = r10.protectionLevel;
        r10 = android.content.pm.PermissionInfo.fixProtectionLevel(r10);
        r7.protectionLevel = r10;
        r7 = r0.info;
        r7 = r7.getProtectionFlags();
        if (r7 == 0) goto L_0x0147;
    L_0x0127:
        r7 = r0.info;
        r7 = r7.protectionLevel;
        r7 = r7 & 4096;
        if (r7 != 0) goto L_0x0147;
    L_0x012f:
        r7 = r0.info;
        r7 = r7.protectionLevel;
        r7 = r7 & 8192;
        if (r7 != 0) goto L_0x0147;
    L_0x0137:
        r7 = r0.info;
        r7 = r7.protectionLevel;
        r7 = r7 & 15;
        r10 = 2;
        if (r7 == r10) goto L_0x0147;
    L_0x0140:
        r7 = "<permission>  protectionLevel specifies a non-instant flag but is not based on signature type";
        r25[r9] = r7;
        r6.mParseError = r8;
        return r9;
    L_0x0147:
        r7 = "<permission>";
        r10 = r0;
        r0 = r21;
        r11 = r1;
        r1 = r23;
        r12 = r2;
        r2 = r24;
        r3 = r7;
        r4 = r10;
        r7 = r5;
        r5 = r25;
        r0 = r0.parseAllMetaData(r1, r2, r3, r4, r5);
        if (r0 != 0) goto L_0x0160;
    L_0x015d:
        r6.mParseError = r8;
        return r9;
    L_0x0160:
        r0 = r7.permissions;
        r0.add(r10);
        r0 = 1;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parsePermission(android.content.pm.PackageParser$Package, android.content.res.Resources, android.content.res.XmlResourceParser, java.lang.String[]):boolean");
    }

    private boolean parsePermissionTree(Package owner, Resources res, XmlResourceParser parser, String[] outError) throws XmlPullParserException, IOException {
        Package packageR = owner;
        Permission permission = new Permission(packageR, (String) null);
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestPermissionTree);
        if (parsePackageItemInfo(owner, permission.info, outError, "<permission-tree>", sa, true, 2, 0, 1, 5, 3, 4)) {
            int index;
            sa.recycle();
            int index2 = permission.info.name.indexOf(46);
            if (index2 > 0) {
                index = permission.info.name.indexOf(46, index2 + 1);
            } else {
                index = index2;
            }
            if (index < 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("<permission-tree> name has less than three segments: ");
                stringBuilder.append(permission.info.name);
                outError[0] = stringBuilder.toString();
                this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                return false;
            }
            permission.info.descriptionRes = 0;
            permission.info.requestRes = 0;
            permission.info.protectionLevel = 0;
            permission.tree = true;
            Permission perm = permission;
            Package packageR2 = packageR;
            if (parseAllMetaData(res, parser, "<permission-tree>", permission, outError)) {
                packageR2.permissions.add(perm);
                return true;
            }
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        sa.recycle();
        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        return false;
    }

    private Instrumentation parseInstrumentation(Package owner, Resources res, XmlResourceParser parser, String[] outError) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestInstrumentation);
        if (this.mParseInstrumentationArgs == null) {
            this.mParseInstrumentationArgs = new ParsePackageItemArgs(owner, outError, 2, 0, 1, 8, 6, 7);
            this.mParseInstrumentationArgs.tag = "<instrumentation>";
        }
        ParsePackageItemArgs parsePackageItemArgs = this.mParseInstrumentationArgs;
        parsePackageItemArgs.sa = sa;
        Component a = new Instrumentation(parsePackageItemArgs, new InstrumentationInfo());
        if (outError[0] != null) {
            sa.recycle();
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return null;
        }
        String str = sa.getNonResourceString(3);
        a.info.targetPackage = str != null ? str.intern() : null;
        String str2 = sa.getNonResourceString(9);
        a.info.targetProcesses = str2 != null ? str2.intern() : null;
        a.info.handleProfiling = sa.getBoolean(4, false);
        a.info.functionalTest = sa.getBoolean(5, false);
        sa.recycle();
        if (a.info.targetPackage == null) {
            outError[0] = "<instrumentation> does not specify targetPackage";
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return null;
        }
        if (parseAllMetaData(res, parser, "<instrumentation>", a, outError)) {
            owner.instrumentation.add(a);
            return a;
        }
        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        return null;
    }

    /* JADX WARNING: Missing block: B:275:0x0668, code skipped:
            r2 = new java.lang.StringBuilder();
            r2.append("Bad static-library declaration name: ");
            r2.append(r10);
            r2.append(" version: ");
            r2.append(r13);
            r9[r1] = r2.toString();
            r0.mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            com.android.internal.util.XmlUtils.skipCurrentTag(r40);
     */
    /* JADX WARNING: Missing block: B:276:0x068a, code skipped:
            return r1;
     */
    @android.annotation.UnsupportedAppUsage
    private boolean parseBaseApplication(android.content.pm.PackageParser.Package r38, android.content.res.Resources r39, android.content.res.XmlResourceParser r40, int r41, java.lang.String[] r42) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r37 = this;
        r15 = r37;
        r14 = r38;
        r13 = r39;
        r12 = r40;
        r11 = r42;
        r10 = r14.applicationInfo;
        r0 = r14.applicationInfo;
        r9 = r0.packageName;
        r0 = com.android.internal.R.styleable.AndroidManifestApplication;
        r8 = r13.obtainAttributes(r12, r0);
        r7 = 2;
        r6 = 0;
        r0 = r8.getResourceId(r7, r6);
        r10.iconRes = r0;
        r0 = 42;
        r0 = r8.getResourceId(r0, r6);
        r10.roundIconRes = r0;
        r3 = "<application>";
        r5 = 0;
        r16 = 3;
        r17 = 1;
        r18 = 2;
        r19 = 42;
        r20 = 22;
        r21 = 30;
        r0 = r38;
        r1 = r10;
        r2 = r42;
        r4 = r8;
        r6 = r16;
        r7 = r17;
        r16 = r8;
        r8 = r18;
        r24 = r9;
        r9 = r19;
        r25 = r10;
        r10 = r20;
        r13 = r11;
        r11 = r21;
        r0 = parsePackageItemInfo(r0, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);
        r11 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        if (r0 != 0) goto L_0x005d;
    L_0x0056:
        r16.recycle();
        r15.mParseError = r11;
        r10 = 0;
        return r10;
    L_0x005d:
        r10 = 0;
        r9 = r25;
        r0 = r9.name;
        if (r0 == 0) goto L_0x0068;
    L_0x0064:
        r0 = r9.name;
        r9.className = r0;
    L_0x0068:
        r8 = 4;
        r0 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r7 = r16;
        r6 = r7.getNonConfigurationString(r8, r0);
        if (r6 == 0) goto L_0x007c;
    L_0x0073:
        r5 = r24;
        r1 = buildClassName(r5, r6, r13);
        r9.manageSpaceActivityName = r1;
        goto L_0x007e;
    L_0x007c:
        r5 = r24;
    L_0x007e:
        r1 = 17;
        r4 = 1;
        r16 = r7.getBoolean(r1, r4);
        r1 = 67108864; // 0x4000000 float:1.5046328E-36 double:3.31561842E-316;
        r3 = 16;
        r11 = 32;
        if (r16 == 0) goto L_0x00f2;
    L_0x008d:
        r2 = r9.flags;
        r19 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r2 = r2 | r19;
        r9.flags = r2;
        r2 = r7.getNonConfigurationString(r3, r0);
        if (r2 == 0) goto L_0x00db;
    L_0x009c:
        r3 = buildClassName(r5, r2, r13);
        r9.backupAgentName = r3;
        r3 = 18;
        r3 = r7.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x00b2;
    L_0x00aa:
        r3 = r9.flags;
        r20 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r3 = r3 | r20;
        r9.flags = r3;
    L_0x00b2:
        r3 = 21;
        r3 = r7.getBoolean(r3, r10);
        if (r3 == 0) goto L_0x00c2;
    L_0x00ba:
        r3 = r9.flags;
        r20 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;
        r3 = r3 | r20;
        r9.flags = r3;
    L_0x00c2:
        r3 = r7.getBoolean(r11, r10);
        if (r3 == 0) goto L_0x00cd;
    L_0x00c8:
        r3 = r9.flags;
        r3 = r3 | r1;
        r9.flags = r3;
    L_0x00cd:
        r3 = 40;
        r3 = r7.getBoolean(r3, r10);
        if (r3 == 0) goto L_0x00db;
    L_0x00d5:
        r3 = r9.privateFlags;
        r3 = r3 | 8192;
        r9.privateFlags = r3;
    L_0x00db:
        r3 = 35;
        r3 = r7.peekValue(r3);
        if (r3 == 0) goto L_0x00f2;
    L_0x00e3:
        r1 = r3.resourceId;
        r9.fullBackupContent = r1;
        if (r1 != 0) goto L_0x00f2;
    L_0x00e9:
        r1 = r3.data;
        if (r1 != 0) goto L_0x00ef;
    L_0x00ed:
        r1 = -1;
        goto L_0x00f0;
    L_0x00ef:
        r1 = r10;
    L_0x00f0:
        r9.fullBackupContent = r1;
    L_0x00f2:
        r1 = r7.getResourceId(r10, r10);
        r9.theme = r1;
        r1 = 13;
        r1 = r7.getResourceId(r1, r10);
        r9.descriptionRes = r1;
        r1 = 8;
        r2 = r7.getBoolean(r1, r10);
        if (r2 == 0) goto L_0x011d;
    L_0x0108:
        r2 = 45;
        r2 = r7.getNonResourceString(r2);
        if (r2 == 0) goto L_0x0118;
    L_0x0110:
        r3 = r15.mCallback;
        r3 = r3.hasFeature(r2);
        if (r3 == 0) goto L_0x011d;
    L_0x0118:
        r3 = r9.flags;
        r3 = r3 | r1;
        r9.flags = r3;
    L_0x011d:
        r2 = 27;
        r2 = r7.getBoolean(r2, r10);
        if (r2 == 0) goto L_0x0127;
    L_0x0125:
        r14.mRequiredForAllUsers = r4;
    L_0x0127:
        r2 = 28;
        r3 = r7.getString(r2);
        if (r3 == 0) goto L_0x0137;
    L_0x012f:
        r21 = r3.length();
        if (r21 <= 0) goto L_0x0137;
    L_0x0135:
        r14.mRestrictedAccountType = r3;
    L_0x0137:
        r1 = 29;
        r0 = r7.getString(r1);
        if (r0 == 0) goto L_0x0147;
    L_0x013f:
        r24 = r0.length();
        if (r24 <= 0) goto L_0x0147;
    L_0x0145:
        r14.mRequiredAccountType = r0;
    L_0x0147:
        r1 = 10;
        r1 = r7.getBoolean(r1, r10);
        r25 = 8388608; // 0x800000 float:1.17549435E-38 double:4.144523E-317;
        if (r1 == 0) goto L_0x015e;
    L_0x0151:
        r1 = r9.flags;
        r2 = 2;
        r1 = r1 | r2;
        r9.flags = r1;
        r1 = r9.privateFlags;
        r1 = r1 | r25;
        r9.privateFlags = r1;
        goto L_0x015f;
    L_0x015e:
        r2 = 2;
    L_0x015f:
        r1 = 20;
        r1 = r7.getBoolean(r1, r10);
        if (r1 == 0) goto L_0x016d;
    L_0x0167:
        r1 = r9.flags;
        r1 = r1 | 16384;
        r9.flags = r1;
    L_0x016d:
        r1 = 23;
        r2 = r14.applicationInfo;
        r2 = r2.targetSdkVersion;
        r11 = 14;
        if (r2 < r11) goto L_0x0179;
    L_0x0177:
        r2 = r4;
        goto L_0x017a;
    L_0x0179:
        r2 = r10;
    L_0x017a:
        r1 = r7.getBoolean(r1, r2);
        r14.baseHardwareAccelerated = r1;
        r1 = r14.baseHardwareAccelerated;
        if (r1 == 0) goto L_0x018b;
    L_0x0184:
        r1 = r9.flags;
        r2 = 536870912; // 0x20000000 float:1.0842022E-19 double:2.652494739E-315;
        r1 = r1 | r2;
        r9.flags = r1;
    L_0x018b:
        r1 = 7;
        r1 = r7.getBoolean(r1, r4);
        if (r1 == 0) goto L_0x0197;
    L_0x0192:
        r1 = r9.flags;
        r1 = r1 | r8;
        r9.flags = r1;
    L_0x0197:
        r1 = r7.getBoolean(r11, r10);
        if (r1 == 0) goto L_0x01a4;
    L_0x019d:
        r1 = r9.flags;
        r2 = 32;
        r1 = r1 | r2;
        r9.flags = r1;
    L_0x01a4:
        r1 = 5;
        r1 = r7.getBoolean(r1, r4);
        if (r1 == 0) goto L_0x01b1;
    L_0x01ab:
        r1 = r9.flags;
        r1 = r1 | 64;
        r9.flags = r1;
    L_0x01b1:
        r1 = r14.parentPackage;
        if (r1 != 0) goto L_0x01c3;
    L_0x01b5:
        r1 = 15;
        r1 = r7.getBoolean(r1, r10);
        if (r1 == 0) goto L_0x01c3;
    L_0x01bd:
        r1 = r9.flags;
        r1 = r1 | 256;
        r9.flags = r1;
    L_0x01c3:
        r1 = 24;
        r1 = r7.getBoolean(r1, r10);
        if (r1 == 0) goto L_0x01d2;
    L_0x01cb:
        r1 = r9.flags;
        r2 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r1 = r1 | r2;
        r9.flags = r1;
    L_0x01d2:
        r1 = 36;
        r2 = r14.applicationInfo;
        r2 = r2.targetSdkVersion;
        r11 = 28;
        if (r2 >= r11) goto L_0x01de;
    L_0x01dc:
        r2 = r4;
        goto L_0x01df;
    L_0x01de:
        r2 = r10;
    L_0x01df:
        r1 = r7.getBoolean(r1, r2);
        if (r1 == 0) goto L_0x01ec;
    L_0x01e5:
        r1 = r9.flags;
        r2 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r1 = r1 | r2;
        r9.flags = r1;
    L_0x01ec:
        r1 = 26;
        r1 = r7.getBoolean(r1, r10);
        if (r1 == 0) goto L_0x01fb;
    L_0x01f4:
        r1 = r9.flags;
        r2 = 4194304; // 0x400000 float:5.877472E-39 double:2.0722615E-317;
        r1 = r1 | r2;
        r9.flags = r1;
    L_0x01fb:
        r1 = 33;
        r1 = r7.getBoolean(r1, r10);
        if (r1 == 0) goto L_0x020a;
    L_0x0203:
        r1 = r9.flags;
        r2 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r1 = r1 | r2;
        r9.flags = r1;
    L_0x020a:
        r1 = 34;
        r1 = r7.getBoolean(r1, r4);
        if (r1 == 0) goto L_0x0219;
    L_0x0212:
        r1 = r9.flags;
        r2 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r1 = r1 | r2;
        r9.flags = r1;
    L_0x0219:
        r1 = 53;
        r1 = r7.getBoolean(r1, r10);
        if (r1 == 0) goto L_0x0228;
    L_0x0221:
        r1 = r9.privateFlags;
        r2 = 33554432; // 0x2000000 float:9.403955E-38 double:1.6578092E-316;
        r1 = r1 | r2;
        r9.privateFlags = r1;
    L_0x0228:
        r1 = 38;
        r1 = r7.getBoolean(r1, r10);
        if (r1 == 0) goto L_0x0237;
    L_0x0230:
        r1 = r9.privateFlags;
        r2 = 32;
        r1 = r1 | r2;
        r9.privateFlags = r1;
    L_0x0237:
        r1 = 39;
        r1 = r7.getBoolean(r1, r10);
        if (r1 == 0) goto L_0x0245;
    L_0x023f:
        r1 = r9.privateFlags;
        r1 = r1 | 64;
        r9.privateFlags = r1;
    L_0x0245:
        r1 = 37;
        r1 = r7.hasValueOrEmpty(r1);
        if (r1 == 0) goto L_0x0264;
    L_0x024d:
        r1 = 37;
        r1 = r7.getBoolean(r1, r4);
        if (r1 == 0) goto L_0x025d;
    L_0x0255:
        r1 = r9.privateFlags;
        r2 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r1 = r1 | r2;
        r9.privateFlags = r1;
        goto L_0x0272;
    L_0x025d:
        r1 = r9.privateFlags;
        r1 = r1 | 2048;
        r9.privateFlags = r1;
        goto L_0x0272;
    L_0x0264:
        r1 = r14.applicationInfo;
        r1 = r1.targetSdkVersion;
        r2 = 24;
        if (r1 < r2) goto L_0x0272;
    L_0x026c:
        r1 = r9.privateFlags;
        r1 = r1 | 4096;
        r9.privateFlags = r1;
    L_0x0272:
        r1 = 54;
        r1 = r7.getBoolean(r1, r4);
        if (r1 == 0) goto L_0x0281;
    L_0x027a:
        r1 = r9.privateFlags;
        r2 = 67108864; // 0x4000000 float:1.5046328E-36 double:3.31561842E-316;
        r1 = r1 | r2;
        r9.privateFlags = r1;
    L_0x0281:
        r1 = 55;
        r2 = r14.applicationInfo;
        r2 = r2.targetSdkVersion;
        r11 = 29;
        if (r2 < r11) goto L_0x028d;
    L_0x028b:
        r2 = r4;
        goto L_0x028e;
    L_0x028d:
        r2 = r10;
    L_0x028e:
        r1 = r7.getBoolean(r1, r2);
        if (r1 == 0) goto L_0x029b;
    L_0x0294:
        r1 = r9.privateFlags;
        r2 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r1 = r1 | r2;
        r9.privateFlags = r1;
    L_0x029b:
        r1 = 56;
        r2 = r14.applicationInfo;
        r2 = r2.targetSdkVersion;
        r11 = 29;
        if (r2 >= r11) goto L_0x02a7;
    L_0x02a5:
        r2 = r4;
        goto L_0x02a8;
    L_0x02a7:
        r2 = r10;
    L_0x02a8:
        r1 = r7.getBoolean(r1, r2);
        if (r1 == 0) goto L_0x02b5;
    L_0x02ae:
        r1 = r9.privateFlags;
        r2 = 536870912; // 0x20000000 float:1.0842022E-19 double:2.652494739E-315;
        r1 = r1 | r2;
        r9.privateFlags = r1;
    L_0x02b5:
        r1 = 44;
        r2 = 0;
        r1 = r7.getFloat(r1, r2);
        r9.maxAspectRatio = r1;
        r1 = 51;
        r1 = r7.getFloat(r1, r2);
        r9.minAspectRatio = r1;
        r1 = 41;
        r1 = r7.getResourceId(r1, r10);
        r9.networkSecurityConfigRes = r1;
        r1 = 43;
        r2 = -1;
        r1 = r7.getInt(r1, r2);
        r9.category = r1;
        r1 = 6;
        r1 = r7.getNonConfigurationString(r1, r10);
        if (r1 == 0) goto L_0x02e9;
    L_0x02de:
        r11 = r1.length();
        if (r11 <= 0) goto L_0x02e9;
    L_0x02e4:
        r11 = r1.intern();
        goto L_0x02ea;
    L_0x02e9:
        r11 = 0;
    L_0x02ea:
        r9.permission = r11;
        r11 = r14.applicationInfo;
        r11 = r11.targetSdkVersion;
        r2 = 8;
        if (r11 < r2) goto L_0x02fe;
    L_0x02f4:
        r2 = 12;
        r11 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r1 = r7.getNonConfigurationString(r2, r11);
        r11 = r1;
        goto L_0x0305;
    L_0x02fe:
        r2 = 12;
        r1 = r7.getNonResourceString(r2);
        r11 = r1;
    L_0x0305:
        r1 = r9.packageName;
        r2 = r9.packageName;
        r1 = buildTaskAffinityName(r1, r2, r11, r13);
        r9.taskAffinity = r1;
        r1 = 48;
        r2 = r7.getNonResourceString(r1);
        if (r2 == 0) goto L_0x031f;
    L_0x0317:
        r1 = r9.packageName;
        r1 = buildClassName(r1, r2, r13);
        r9.appComponentFactory = r1;
    L_0x031f:
        r1 = 49;
        r1 = r7.getBoolean(r1, r10);
        if (r1 == 0) goto L_0x032f;
    L_0x0327:
        r1 = r9.privateFlags;
        r20 = 4194304; // 0x400000 float:5.877472E-39 double:2.0722615E-317;
        r1 = r1 | r20;
        r9.privateFlags = r1;
    L_0x032f:
        r1 = 50;
        r1 = r7.getBoolean(r1, r10);
        if (r1 == 0) goto L_0x033f;
    L_0x0337:
        r1 = r9.privateFlags;
        r20 = 16777216; // 0x1000000 float:2.3509887E-38 double:8.289046E-317;
        r1 = r1 | r20;
        r9.privateFlags = r1;
    L_0x033f:
        r1 = r13[r10];
        if (r1 != 0) goto L_0x03ba;
    L_0x0343:
        r1 = r14.applicationInfo;
        r1 = r1.targetSdkVersion;
        r4 = 8;
        if (r1 < r4) goto L_0x0356;
    L_0x034b:
        r1 = 11;
        r4 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r1 = r7.getNonConfigurationString(r1, r4);
        r21 = r1;
        goto L_0x035e;
    L_0x0356:
        r1 = 11;
        r1 = r7.getNonResourceString(r1);
        r21 = r1;
    L_0x035e:
        r1 = r9.packageName;
        r4 = 0;
        r8 = r15.mSeparateProcesses;
        r24 = r0;
        r0 = r1;
        r1 = r4;
        r18 = r2;
        r4 = -1;
        r2 = r21;
        r19 = r3;
        r23 = 16;
        r3 = r41;
        r10 = 1;
        r4 = r8;
        r8 = r5;
        r5 = r42;
        r0 = buildProcessName(r0, r1, r2, r3, r4, r5);
        r9.processName = r0;
        r0 = 9;
        r0 = r7.getBoolean(r0, r10);
        r9.enabled = r0;
        r0 = 31;
        r1 = 0;
        r0 = r7.getBoolean(r0, r1);
        if (r0 == 0) goto L_0x0395;
    L_0x038e:
        r0 = r9.flags;
        r1 = 33554432; // 0x2000000 float:9.403955E-38 double:1.6578092E-316;
        r0 = r0 | r1;
        r9.flags = r0;
    L_0x0395:
        r0 = 47;
        r1 = 0;
        r0 = r7.getBoolean(r0, r1);
        if (r0 == 0) goto L_0x03b8;
    L_0x039e:
        r0 = r9.privateFlags;
        r5 = 2;
        r0 = r0 | r5;
        r9.privateFlags = r0;
        r0 = r9.processName;
        if (r0 == 0) goto L_0x03c5;
    L_0x03a8:
        r0 = r9.processName;
        r1 = r9.packageName;
        r0 = r0.equals(r1);
        if (r0 != 0) goto L_0x03c5;
    L_0x03b2:
        r0 = "cantSaveState applications can not use custom processes";
        r1 = 0;
        r13[r1] = r0;
        goto L_0x03c5;
    L_0x03b8:
        r5 = 2;
        goto L_0x03c5;
    L_0x03ba:
        r24 = r0;
        r18 = r2;
        r19 = r3;
        r10 = r4;
        r8 = r5;
        r5 = 2;
        r23 = 16;
    L_0x03c5:
        r0 = 25;
        r1 = 0;
        r0 = r7.getInt(r0, r1);
        r9.uiOptions = r0;
        r0 = 46;
        r0 = r7.getString(r0);
        r9.classLoaderName = r0;
        r0 = r9.classLoaderName;
        if (r0 == 0) goto L_0x03f8;
    L_0x03da:
        r0 = r9.classLoaderName;
        r0 = com.android.internal.os.ClassLoaderFactory.isValidClassLoaderName(r0);
        if (r0 != 0) goto L_0x03f8;
    L_0x03e2:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Invalid class loader name: ";
        r0.append(r1);
        r1 = r9.classLoaderName;
        r0.append(r1);
        r0 = r0.toString();
        r1 = 0;
        r13[r1] = r0;
    L_0x03f8:
        r0 = 52;
        r0 = r7.getString(r0);
        r9.zygotePreloadName = r0;
        r7.recycle();
        r0 = 0;
        r1 = r13[r0];
        if (r1 == 0) goto L_0x040d;
    L_0x0408:
        r1 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r15.mParseError = r1;
        return r0;
    L_0x040d:
        r4 = r40.getDepth();
        r0 = new android.content.pm.PackageParser$CachedComponentArgs;
        r1 = 0;
        r0.<init>();
        r21 = r6;
        r6 = r0;
        r0 = 0;
        r1 = 0;
        r2 = 0;
        r26 = r0;
        r27 = r1;
        r28 = r2;
        r29 = r7;
    L_0x0425:
        r0 = r40.next();
        r7 = r0;
        if (r0 == r10) goto L_0x0796;
    L_0x042c:
        r0 = 3;
        if (r7 != r0) goto L_0x0445;
    L_0x042f:
        r0 = r40.getDepth();
        if (r0 <= r4) goto L_0x0436;
    L_0x0435:
        goto L_0x0445;
    L_0x0436:
        r33 = r4;
        r34 = r7;
        r5 = r9;
        r17 = r11;
        r9 = r13;
        r0 = r15;
        r7 = r39;
        r11 = r8;
        r8 = r12;
        goto L_0x07a3;
    L_0x0445:
        r0 = 3;
        if (r7 == r0) goto L_0x0777;
    L_0x0448:
        r3 = 4;
        if (r7 != r3) goto L_0x045d;
    L_0x044b:
        r7 = r39;
        r22 = r3;
        r33 = r4;
        r5 = r9;
        r17 = r11;
        r9 = r13;
        r0 = r15;
        r1 = 0;
        r2 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r11 = r8;
        r8 = r12;
        goto L_0x0789;
    L_0x045d:
        r2 = r40.getName();
        r0 = "activity";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x04bb;
    L_0x0469:
        r22 = 0;
        r1 = r14.baseHardwareAccelerated;
        r0 = r37;
        r30 = r1;
        r1 = r38;
        r31 = r2;
        r2 = r39;
        r32 = r3;
        r3 = r40;
        r33 = r4;
        r4 = r41;
        r5 = r42;
        r34 = r7;
        r7 = r22;
        r35 = r8;
        r22 = r32;
        r8 = r30;
        r0 = r0.parseActivity(r1, r2, r3, r4, r5, r6, r7, r8);
        if (r0 != 0) goto L_0x0497;
    L_0x0491:
        r1 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r15.mParseError = r1;
        r2 = 0;
        return r2;
    L_0x0497:
        r1 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r2 = 0;
        r3 = r0.order;
        if (r3 == 0) goto L_0x04a0;
    L_0x049e:
        r3 = r10;
        goto L_0x04a1;
    L_0x04a0:
        r3 = r2;
    L_0x04a1:
        r3 = r26 | r3;
        r4 = r14.activities;
        r4.add(r0);
        r7 = r39;
        r26 = r3;
        r5 = r9;
        r17 = r11;
        r8 = r12;
        r9 = r13;
        r0 = r15;
        r11 = r35;
        r36 = r2;
        r2 = r1;
        r1 = r36;
        goto L_0x073a;
    L_0x04bb:
        r31 = r2;
        r22 = r3;
        r33 = r4;
        r34 = r7;
        r35 = r8;
        r1 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r2 = 0;
        r0 = "receiver";
        r3 = r31;
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0511;
    L_0x04d3:
        r0 = 1;
        r4 = 0;
        r7 = r37;
        r8 = r38;
        r5 = r9;
        r9 = r39;
        r1 = r2;
        r2 = r10;
        r10 = r40;
        r17 = r11;
        r2 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r11 = r41;
        r12 = r42;
        r13 = r6;
        r14 = r0;
        r0 = r15;
        r15 = r4;
        r4 = r7.parseActivity(r8, r9, r10, r11, r12, r13, r14, r15);
        if (r4 != 0) goto L_0x04f5;
    L_0x04f2:
        r0.mParseError = r2;
        return r1;
    L_0x04f5:
        r7 = r4.order;
        if (r7 == 0) goto L_0x04fb;
    L_0x04f9:
        r7 = 1;
        goto L_0x04fc;
    L_0x04fb:
        r7 = r1;
    L_0x04fc:
        r7 = r27 | r7;
        r14 = r38;
        r8 = r14.receivers;
        r8.add(r4);
        r8 = r40;
        r9 = r42;
        r27 = r7;
        r11 = r35;
        r7 = r39;
        goto L_0x073a;
    L_0x0511:
        r5 = r9;
        r17 = r11;
        r0 = r15;
        r36 = r2;
        r2 = r1;
        r1 = r36;
        r4 = "service";
        r4 = r3.equals(r4);
        if (r4 == 0) goto L_0x0553;
    L_0x0523:
        r7 = r37;
        r8 = r38;
        r9 = r39;
        r10 = r40;
        r11 = r41;
        r12 = r42;
        r13 = r6;
        r4 = r7.parseService(r8, r9, r10, r11, r12, r13);
        if (r4 != 0) goto L_0x0539;
    L_0x0536:
        r0.mParseError = r2;
        return r1;
    L_0x0539:
        r7 = r4.order;
        if (r7 == 0) goto L_0x053f;
    L_0x053d:
        r7 = 1;
        goto L_0x0540;
    L_0x053f:
        r7 = r1;
    L_0x0540:
        r7 = r28 | r7;
        r8 = r14.services;
        r8.add(r4);
        r8 = r40;
        r9 = r42;
        r28 = r7;
        r11 = r35;
        r7 = r39;
        goto L_0x073a;
    L_0x0553:
        r4 = "provider";
        r4 = r3.equals(r4);
        if (r4 == 0) goto L_0x0581;
    L_0x055c:
        r7 = r37;
        r8 = r38;
        r9 = r39;
        r10 = r40;
        r11 = r41;
        r12 = r42;
        r13 = r6;
        r4 = r7.parseProvider(r8, r9, r10, r11, r12, r13);
        if (r4 != 0) goto L_0x0572;
    L_0x056f:
        r0.mParseError = r2;
        return r1;
    L_0x0572:
        r7 = r14.providers;
        r7.add(r4);
        r7 = r39;
        r8 = r40;
        r9 = r42;
        r11 = r35;
        goto L_0x073a;
    L_0x0581:
        r4 = "activity-alias";
        r4 = r3.equals(r4);
        if (r4 == 0) goto L_0x05b9;
    L_0x0589:
        r7 = r37;
        r8 = r38;
        r9 = r39;
        r10 = r40;
        r11 = r41;
        r12 = r42;
        r13 = r6;
        r4 = r7.parseActivityAlias(r8, r9, r10, r11, r12, r13);
        if (r4 != 0) goto L_0x059f;
    L_0x059c:
        r0.mParseError = r2;
        return r1;
    L_0x059f:
        r7 = r4.order;
        if (r7 == 0) goto L_0x05a5;
    L_0x05a3:
        r7 = 1;
        goto L_0x05a6;
    L_0x05a5:
        r7 = r1;
    L_0x05a6:
        r7 = r26 | r7;
        r8 = r14.activities;
        r8.add(r4);
        r8 = r40;
        r9 = r42;
        r26 = r7;
        r11 = r35;
        r7 = r39;
        goto L_0x073a;
    L_0x05b9:
        r4 = r40.getName();
        r7 = "meta-data";
        r4 = r4.equals(r7);
        if (r4 == 0) goto L_0x05dc;
    L_0x05c5:
        r4 = r14.mAppMetaData;
        r7 = r39;
        r8 = r40;
        r9 = r42;
        r4 = r0.parseMetaData(r7, r8, r4, r9);
        r14.mAppMetaData = r4;
        if (r4 != 0) goto L_0x05d8;
    L_0x05d5:
        r0.mParseError = r2;
        return r1;
    L_0x05d8:
        r11 = r35;
        goto L_0x073a;
    L_0x05dc:
        r7 = r39;
        r8 = r40;
        r9 = r42;
        r4 = "static-library";
        r4 = r3.equals(r4);
        if (r4 == 0) goto L_0x068b;
    L_0x05eb:
        r4 = com.android.internal.R.styleable.AndroidManifestStaticLibrary;
        r4 = r7.obtainAttributes(r8, r4);
        r10 = r4.getNonResourceString(r1);
        r11 = -1;
        r12 = 1;
        r13 = r4.getInt(r12, r11);
        r12 = 2;
        r15 = r4.getInt(r12, r1);
        r4.recycle();
        if (r10 == 0) goto L_0x0664;
    L_0x0605:
        if (r13 >= 0) goto L_0x060c;
    L_0x0607:
        r31 = r3;
        r11 = r35;
        goto L_0x0668;
    L_0x060c:
        r11 = r14.mSharedUserId;
        if (r11 == 0) goto L_0x061d;
    L_0x0610:
        r2 = "sharedUserId not allowed in static shared library";
        r9[r1] = r2;
        r2 = -107; // 0xffffffffffffff95 float:NaN double:NaN;
        r0.mParseError = r2;
        com.android.internal.util.XmlUtils.skipCurrentTag(r40);
        return r1;
    L_0x061d:
        r11 = r14.staticSharedLibName;
        if (r11 == 0) goto L_0x063c;
    L_0x0621:
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r12 = "Multiple static-shared libs for package ";
        r11.append(r12);
        r12 = r35;
        r11.append(r12);
        r11 = r11.toString();
        r9[r1] = r11;
        r0.mParseError = r2;
        com.android.internal.util.XmlUtils.skipCurrentTag(r40);
        return r1;
    L_0x063c:
        r11 = r35;
        r12 = r10.intern();
        r14.staticSharedLibName = r12;
        if (r13 < 0) goto L_0x0650;
        r31 = r3;
        r2 = android.content.pm.PackageInfo.composeLongVersionCode(r15, r13);
        r14.staticSharedLibVersion = r2;
        goto L_0x0655;
    L_0x0650:
        r31 = r3;
        r2 = (long) r13;
        r14.staticSharedLibVersion = r2;
    L_0x0655:
        r2 = r5.privateFlags;
        r2 = r2 | 16384;
        r5.privateFlags = r2;
        com.android.internal.util.XmlUtils.skipCurrentTag(r40);
        r29 = r4;
        r2 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        goto L_0x073a;
    L_0x0664:
        r31 = r3;
        r11 = r35;
    L_0x0668:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Bad static-library declaration name: ";
        r2.append(r3);
        r2.append(r10);
        r3 = " version: ";
        r2.append(r3);
        r2.append(r13);
        r2 = r2.toString();
        r9[r1] = r2;
        r2 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r0.mParseError = r2;
        com.android.internal.util.XmlUtils.skipCurrentTag(r40);
        return r1;
    L_0x068b:
        r31 = r3;
        r11 = r35;
        r3 = "library";
        r4 = r31;
        r3 = r4.equals(r3);
        if (r3 == 0) goto L_0x06c3;
    L_0x0699:
        r3 = com.android.internal.R.styleable.AndroidManifestLibrary;
        r3 = r7.obtainAttributes(r8, r3);
        r10 = r3.getNonResourceString(r1);
        r3.recycle();
        if (r10 == 0) goto L_0x06bc;
    L_0x06a8:
        r10 = r10.intern();
        r12 = r14.libraryNames;
        r12 = com.android.internal.util.ArrayUtils.contains(r12, r10);
        if (r12 != 0) goto L_0x06bc;
    L_0x06b4:
        r12 = r14.libraryNames;
        r12 = com.android.internal.util.ArrayUtils.add(r12, r10);
        r14.libraryNames = r12;
    L_0x06bc:
        com.android.internal.util.XmlUtils.skipCurrentTag(r40);
        r29 = r3;
        goto L_0x073a;
    L_0x06c3:
        r3 = "uses-static-library";
        r3 = r4.equals(r3);
        if (r3 == 0) goto L_0x06d3;
    L_0x06cc:
        r3 = r0.parseUsesStaticLibrary(r14, r7, r8, r9);
        if (r3 != 0) goto L_0x073a;
    L_0x06d2:
        return r1;
    L_0x06d3:
        r3 = "uses-library";
        r3 = r4.equals(r3);
        if (r3 == 0) goto L_0x070d;
    L_0x06dc:
        r3 = com.android.internal.R.styleable.AndroidManifestUsesLibrary;
        r3 = r7.obtainAttributes(r8, r3);
        r10 = r3.getNonResourceString(r1);
        r12 = 1;
        r13 = r3.getBoolean(r12, r12);
        r3.recycle();
        if (r10 == 0) goto L_0x0707;
    L_0x06f0:
        r10 = r10.intern();
        if (r13 == 0) goto L_0x06ff;
    L_0x06f6:
        r12 = r14.usesLibraries;
        r12 = com.android.internal.util.ArrayUtils.add(r12, r10);
        r14.usesLibraries = r12;
        goto L_0x0707;
    L_0x06ff:
        r12 = r14.usesOptionalLibraries;
        r12 = com.android.internal.util.ArrayUtils.add(r12, r10);
        r14.usesOptionalLibraries = r12;
    L_0x0707:
        com.android.internal.util.XmlUtils.skipCurrentTag(r40);
        r29 = r3;
        goto L_0x073a;
    L_0x070d:
        r3 = "uses-package";
        r3 = r4.equals(r3);
        if (r3 == 0) goto L_0x071a;
    L_0x0716:
        com.android.internal.util.XmlUtils.skipCurrentTag(r40);
        goto L_0x073a;
    L_0x071a:
        r3 = "profileable";
        r3 = r4.equals(r3);
        if (r3 == 0) goto L_0x0747;
    L_0x0723:
        r3 = com.android.internal.R.styleable.AndroidManifestProfileable;
        r3 = r7.obtainAttributes(r8, r3);
        r10 = r3.getBoolean(r1, r1);
        if (r10 == 0) goto L_0x0735;
    L_0x072f:
        r10 = r5.privateFlags;
        r10 = r10 | r25;
        r5.privateFlags = r10;
    L_0x0735:
        com.android.internal.util.XmlUtils.skipCurrentTag(r40);
        r29 = r3;
    L_0x073a:
        r15 = r0;
        r12 = r8;
        r13 = r9;
        r8 = r11;
        r11 = r17;
        r4 = r33;
        r10 = 1;
        r9 = r5;
        r5 = 2;
        goto L_0x0425;
    L_0x0747:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r10 = "Unknown element under <application>: ";
        r3.append(r10);
        r3.append(r4);
        r10 = " at ";
        r3.append(r10);
        r10 = r0.mArchiveSourcePath;
        r3.append(r10);
        r10 = " ";
        r3.append(r10);
        r10 = r40.getPositionDescription();
        r3.append(r10);
        r3 = r3.toString();
        r10 = "PackageParser";
        android.util.Slog.w(r10, r3);
        com.android.internal.util.XmlUtils.skipCurrentTag(r40);
        goto L_0x0789;
    L_0x0777:
        r33 = r4;
        r34 = r7;
        r5 = r9;
        r17 = r11;
        r9 = r13;
        r0 = r15;
        r1 = 0;
        r2 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r22 = 4;
        r7 = r39;
        r11 = r8;
        r8 = r12;
    L_0x0789:
        r15 = r0;
        r12 = r8;
        r13 = r9;
        r8 = r11;
        r11 = r17;
        r4 = r33;
        r10 = 1;
        r9 = r5;
        r5 = 2;
        goto L_0x0425;
    L_0x0796:
        r33 = r4;
        r34 = r7;
        r5 = r9;
        r17 = r11;
        r9 = r13;
        r0 = r15;
        r7 = r39;
        r11 = r8;
        r8 = r12;
    L_0x07a3:
        r1 = r14.staticSharedLibName;
        r1 = android.text.TextUtils.isEmpty(r1);
        if (r1 == 0) goto L_0x07b9;
    L_0x07ab:
        r1 = r14.baseHardwareAccelerated;
        r2 = r41;
        r1 = r0.generateAppDetailsHiddenActivity(r14, r2, r9, r1);
        r3 = r14.activities;
        r3.add(r1);
        goto L_0x07bb;
    L_0x07b9:
        r2 = r41;
    L_0x07bb:
        if (r26 == 0) goto L_0x07c4;
    L_0x07bd:
        r1 = r14.activities;
        r3 = android.content.pm.-$$Lambda$PackageParser$0aobsT7Zf7WVZCqMZ5z2clAuQf4.INSTANCE;
        java.util.Collections.sort(r1, r3);
    L_0x07c4:
        if (r27 == 0) goto L_0x07cd;
    L_0x07c6:
        r1 = r14.receivers;
        r3 = android.content.pm.-$$Lambda$PackageParser$0DZRgzfgaIMpCOhJqjw6PUiU5vw.INSTANCE;
        java.util.Collections.sort(r1, r3);
    L_0x07cd:
        if (r28 == 0) goto L_0x07d6;
    L_0x07cf:
        r1 = r14.services;
        r3 = android.content.pm.-$$Lambda$PackageParser$M-9fHqS_eEp1oYkuKJhRHOGUxf8.INSTANCE;
        java.util.Collections.sort(r1, r3);
    L_0x07d6:
        r37.setMaxAspectRatio(r38);
        r37.setMinAspectRatio(r38);
        r1 = hasDomainURLs(r38);
        if (r1 == 0) goto L_0x07eb;
    L_0x07e2:
        r1 = r14.applicationInfo;
        r3 = r1.privateFlags;
        r3 = r3 | 16;
        r1.privateFlags = r3;
        goto L_0x07f3;
    L_0x07eb:
        r1 = r14.applicationInfo;
        r3 = r1.privateFlags;
        r3 = r3 & -17;
        r1.privateFlags = r3;
    L_0x07f3:
        r1 = 1;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseBaseApplication(android.content.pm.PackageParser$Package, android.content.res.Resources, android.content.res.XmlResourceParser, int, java.lang.String[]):boolean");
    }

    private static boolean hasDomainURLs(Package pkg) {
        if (pkg == null || pkg.activities == null) {
            return false;
        }
        ArrayList<Activity> activities = pkg.activities;
        int countActivities = activities.size();
        for (int n = 0; n < countActivities; n++) {
            ArrayList<ActivityIntentInfo> filters = ((Activity) activities.get(n)).intents;
            if (filters != null) {
                int countFilters = filters.size();
                for (int m = 0; m < countFilters; m++) {
                    ActivityIntentInfo aii = (ActivityIntentInfo) filters.get(m);
                    String str = "android.intent.action.VIEW";
                    if (aii.hasAction(str) && aii.hasAction(str) && (aii.hasDataScheme(IntentFilter.SCHEME_HTTP) || aii.hasDataScheme(IntentFilter.SCHEME_HTTPS))) {
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    private boolean parseSplitApplication(Package owner, Resources res, XmlResourceParser parser, int flags, int splitIndex, String[] outError) throws XmlPullParserException, IOException {
        PackageParser packageParser = this;
        Package packageR = owner;
        Resources resources = res;
        AttributeSet attributeSet = parser;
        String[] strArr = outError;
        TypedArray sa = resources.obtainAttributes(attributeSet, R.styleable.AndroidManifestApplication);
        boolean z = true;
        boolean z2 = true;
        if (sa.getBoolean(7, true)) {
            int[] iArr = packageR.splitFlags;
            iArr[splitIndex] = iArr[splitIndex] | 4;
        }
        String classLoaderName = sa.getString(46);
        int i = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        boolean z3 = false;
        if (classLoaderName == null || ClassLoaderFactory.isValidClassLoaderName(classLoaderName)) {
            packageR.applicationInfo.splitClassLoaderNames[splitIndex] = classLoaderName;
            int innerDepth = parser.getDepth();
            while (true) {
                boolean next = parser.next();
                boolean type = next;
                boolean z4;
                int i2;
                String str;
                String[] strArr2;
                Resources resources2;
                Package packageR2;
                PackageParser packageParser2;
                if (next != z) {
                    if (type && parser.getDepth() <= innerDepth) {
                        z4 = type;
                        i2 = innerDepth;
                        str = classLoaderName;
                        strArr2 = strArr;
                        innerDepth = attributeSet;
                        resources2 = resources;
                        packageR2 = packageR;
                        packageParser2 = packageParser;
                        break;
                    }
                    boolean z5;
                    int i3;
                    if (type) {
                        i2 = innerDepth;
                        str = classLoaderName;
                        innerDepth = attributeSet;
                        packageR2 = packageR;
                        packageParser2 = packageParser;
                        z5 = z3;
                        i3 = i;
                        strArr2 = strArr;
                        resources2 = resources;
                    } else if (type == z2) {
                        i2 = innerDepth;
                        str = classLoaderName;
                        innerDepth = attributeSet;
                        packageR2 = packageR;
                        packageParser2 = packageParser;
                        z5 = z3;
                        i3 = i;
                        strArr2 = strArr;
                        resources2 = resources;
                    } else {
                        int type2;
                        CachedComponentArgs cachedArgs = new CachedComponentArgs();
                        String tagName = parser.getName();
                        int i4;
                        if (tagName.equals(Context.ACTIVITY_SERVICE)) {
                            String tagName2 = tagName;
                            i2 = innerDepth;
                            boolean z6 = z3;
                            i4 = i;
                            str = classLoaderName;
                            ComponentInfo parsedComponent = parseActivity(owner, res, parser, flags, outError, cachedArgs, false, packageR.baseHardwareAccelerated);
                            if (parsedComponent == null) {
                                packageParser.mParseError = i4;
                                return false;
                            }
                            packageR.activities.add(parsedComponent);
                            resources2 = res;
                            type2 = parsedComponent.info;
                            strArr2 = strArr;
                            innerDepth = attributeSet;
                            packageParser2 = packageParser;
                            tagName = tagName2;
                            z5 = false;
                            packageR2 = packageR;
                            i3 = i4;
                        } else {
                            z4 = type;
                            i2 = innerDepth;
                            boolean z7 = z3;
                            i4 = i;
                            str = classLoaderName;
                            if (tagName.equals(IncidentManager.URI_PARAM_RECEIVER_CLASS)) {
                                boolean z8 = z2;
                                z3 = z;
                                String[] strArr3 = strArr;
                                innerDepth = attributeSet;
                                resources2 = res;
                                packageR2 = packageR;
                                packageParser2 = packageParser;
                                type2 = parseActivity(owner, res, parser, flags, outError, cachedArgs, true, false);
                                if (type2 == 0) {
                                    packageParser2.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                    return false;
                                }
                                i3 = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                z5 = false;
                                packageR2.receivers.add(type2);
                                type2 = type2.info;
                                strArr2 = outError;
                            } else {
                                resources2 = res;
                                innerDepth = attributeSet;
                                packageParser2 = packageParser;
                                z5 = z7;
                                packageR2 = packageR;
                                i3 = i4;
                                if (tagName.equals("service") != 0) {
                                    type2 = parseService(owner, res, parser, flags, outError, cachedArgs);
                                    if (type2 == 0) {
                                        packageParser2.mParseError = i3;
                                        return z5;
                                    }
                                    packageR2.services.add(type2);
                                    type2 = type2.info;
                                    strArr2 = outError;
                                } else if (tagName.equals("provider") != 0) {
                                    type2 = parseProvider(owner, res, parser, flags, outError, cachedArgs);
                                    if (type2 == 0) {
                                        packageParser2.mParseError = i3;
                                        return z5;
                                    }
                                    packageR2.providers.add(type2);
                                    type2 = type2.info;
                                    strArr2 = outError;
                                } else if (tagName.equals("activity-alias") != 0) {
                                    type2 = parseActivityAlias(owner, res, parser, flags, outError, cachedArgs);
                                    if (type2 == 0) {
                                        packageParser2.mParseError = i3;
                                        return z5;
                                    }
                                    packageR2.activities.add(type2);
                                    type2 = type2.info;
                                    strArr2 = outError;
                                } else {
                                    if (parser.getName().equals("meta-data") != 0) {
                                        strArr2 = outError;
                                        type2 = packageParser2.parseMetaData(resources2, innerDepth, packageR2.mAppMetaData, strArr2);
                                        packageR2.mAppMetaData = type2;
                                        if (type2 == 0) {
                                            packageParser2.mParseError = i3;
                                            return z5;
                                        }
                                    }
                                    strArr2 = outError;
                                    if (tagName.equals("uses-static-library") != 0) {
                                        if (packageParser2.parseUsesStaticLibrary(packageR2, resources2, innerDepth, strArr2) == 0) {
                                            return z5;
                                        }
                                    } else if (tagName.equals("uses-library") != 0) {
                                        type2 = resources2.obtainAttributes(innerDepth, R.styleable.AndroidManifestUsesLibrary);
                                        classLoaderName = type2.getNonResourceString(z5);
                                        z = type2.getBoolean(1, true);
                                        type2.recycle();
                                        if (classLoaderName != null) {
                                            Object lname = classLoaderName.intern();
                                            if (z) {
                                                packageR2.usesLibraries = ArrayUtils.add(packageR2.usesLibraries, lname);
                                                packageR2.usesOptionalLibraries = ArrayUtils.remove(packageR2.usesOptionalLibraries, lname);
                                            } else if (!ArrayUtils.contains(packageR2.usesLibraries, lname)) {
                                                packageR2.usesOptionalLibraries = ArrayUtils.add(packageR2.usesOptionalLibraries, lname);
                                            }
                                        }
                                        XmlUtils.skipCurrentTag(parser);
                                        Object obj = type2;
                                        type2 = 0;
                                    } else if (tagName.equals("uses-package") != 0) {
                                        XmlUtils.skipCurrentTag(parser);
                                    } else {
                                        type2 = new StringBuilder();
                                        type2.append("Unknown element under <application>: ");
                                        type2.append(tagName);
                                        type2.append(" at ");
                                        type2.append(packageParser2.mArchiveSourcePath);
                                        type2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                                        type2.append(parser.getPositionDescription());
                                        Slog.w(TAG, type2.toString());
                                        XmlUtils.skipCurrentTag(parser);
                                    }
                                    type2 = 0;
                                }
                            }
                        }
                        if (type2 != 0 && type2.splitName == null) {
                            type2.splitName = packageR2.splitNames[splitIndex];
                        }
                        attributeSet = innerDepth;
                        resources = resources2;
                        strArr = strArr2;
                        i = i3;
                        z3 = z5;
                        classLoaderName = str;
                        innerDepth = i2;
                        z2 = true;
                        z = true;
                        packageParser = packageParser2;
                        packageR = packageR2;
                    }
                    attributeSet = innerDepth;
                    resources = resources2;
                    strArr = strArr2;
                    i = i3;
                    z3 = z5;
                    classLoaderName = str;
                    innerDepth = i2;
                    z2 = true;
                    z = true;
                    packageParser = packageParser2;
                    packageR = packageR2;
                } else {
                    z4 = type;
                    i2 = innerDepth;
                    str = classLoaderName;
                    strArr2 = strArr;
                    innerDepth = attributeSet;
                    resources2 = resources;
                    packageR2 = packageR;
                    packageParser2 = packageParser;
                    break;
                }
            }
            return true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid class loader name: ");
        stringBuilder.append(classLoaderName);
        strArr[0] = stringBuilder.toString();
        packageParser.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        return false;
    }

    private static boolean parsePackageItemInfo(Package owner, PackageItemInfo outInfo, String[] outError, String tag, TypedArray sa, boolean nameRequired, int nameRes, int labelRes, int iconRes, int roundIconRes, int logoRes, int bannerRes) {
        Package packageR = owner;
        PackageItemInfo packageItemInfo = outInfo;
        String[] strArr = outError;
        String str = tag;
        TypedArray typedArray = sa;
        if (typedArray == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(" does not contain any attributes");
            strArr[0] = stringBuilder.toString();
            return false;
        }
        int roundIconVal;
        int iconVal;
        String name = typedArray.getNonConfigurationString(nameRes, 0);
        if (name != null) {
            String outInfoName = buildClassName(packageR.applicationInfo.packageName, name, strArr);
            if (PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME.equals(outInfoName)) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(" invalid android:name");
                strArr[0] = stringBuilder2.toString();
                return false;
            }
            packageItemInfo.name = outInfoName;
            if (outInfoName == null) {
                return false;
            }
        } else if (nameRequired) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(str);
            stringBuilder3.append(" does not specify android:name");
            strArr[0] = stringBuilder3.toString();
            return false;
        }
        if (sUseRoundIcon) {
            roundIconVal = typedArray.getResourceId(roundIconRes, 0);
        } else {
            int i = roundIconRes;
            roundIconVal = 0;
        }
        if (roundIconVal != 0) {
            packageItemInfo.icon = roundIconVal;
            packageItemInfo.nonLocalizedLabel = null;
            int i2 = iconRes;
        } else {
            iconVal = typedArray.getResourceId(iconRes, 0);
            if (iconVal != 0) {
                packageItemInfo.icon = iconVal;
                packageItemInfo.nonLocalizedLabel = null;
            }
        }
        iconVal = typedArray.getResourceId(logoRes, 0);
        if (iconVal != 0) {
            packageItemInfo.logo = iconVal;
        }
        int bannerVal = typedArray.getResourceId(bannerRes, 0);
        if (bannerVal != 0) {
            packageItemInfo.banner = bannerVal;
        }
        TypedValue v = typedArray.peekValue(labelRes);
        if (v != null) {
            int i3 = v.resourceId;
            packageItemInfo.labelRes = i3;
            if (i3 == 0) {
                packageItemInfo.nonLocalizedLabel = v.coerceToString();
            }
        }
        packageItemInfo.packageName = packageR.packageName;
        return true;
    }

    private Activity generateAppDetailsHiddenActivity(Package owner, int flags, String[] outError, boolean hardwareAccelerated) {
        Activity a = new Activity(owner, PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME, new ActivityInfo());
        a.owner = owner;
        a.setPackageName(owner.packageName);
        a.info.theme = 16973909;
        a.info.exported = true;
        a.info.name = PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME;
        a.info.processName = owner.applicationInfo.processName;
        a.info.uiOptions = a.info.applicationInfo.uiOptions;
        a.info.taskAffinity = buildTaskAffinityName(owner.packageName, owner.packageName, ":app_details", outError);
        a.info.enabled = true;
        a.info.launchMode = 0;
        a.info.documentLaunchMode = 0;
        a.info.maxRecents = ActivityTaskManager.getDefaultAppRecentsLimitStatic();
        a.info.configChanges = getActivityConfigChanges(0, 0);
        a.info.softInputMode = 0;
        a.info.persistableMode = 1;
        a.info.screenOrientation = -1;
        a.info.resizeMode = 4;
        a.info.lockTaskLaunchMode = 0;
        ActivityInfo activityInfo = a.info;
        a.info.directBootAware = false;
        activityInfo.encryptionAware = false;
        a.info.rotationAnimation = -1;
        a.info.colorMode = 0;
        if (hardwareAccelerated) {
            activityInfo = a.info;
            activityInfo.flags |= 512;
        }
        return a;
    }

    private Activity parseActivity(Package owner, Resources res, XmlResourceParser parser, int flags, String[] outError, CachedComponentArgs cachedArgs, boolean receiver, boolean hardwareAccelerated) throws XmlPullParserException, IOException {
        Package packageR = owner;
        String[] strArr = outError;
        CachedComponentArgs cachedComponentArgs = cachedArgs;
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestActivity);
        if (cachedComponentArgs.mActivityArgs == null) {
            cachedComponentArgs.mActivityArgs = new ParseComponentArgs(owner, outError, 3, 1, 2, 44, 23, 30, this.mSeparateProcesses, 7, 17, 5);
        }
        cachedComponentArgs.mActivityArgs.tag = receiver ? "<receiver>" : "<activity>";
        cachedComponentArgs.mActivityArgs.sa = sa;
        cachedComponentArgs.mActivityArgs.flags = flags;
        Activity a = new Activity(cachedComponentArgs.mActivityArgs, new ActivityInfo());
        if (strArr[0] != null) {
            sa.recycle();
            return null;
        }
        String parentClassName;
        int i;
        ActivityInfo activityInfo;
        boolean z;
        int type;
        ActivityInfo activityInfo2;
        boolean z2;
        boolean setExported = sa.hasValue(6);
        if (setExported) {
            a.info.exported = sa.getBoolean(6, false);
        }
        a.info.theme = sa.getResourceId(0, 0);
        a.info.uiOptions = sa.getInt(26, a.info.applicationInfo.uiOptions);
        String parentName = sa.getNonConfigurationString(27, 1024);
        String str = "Activity ";
        String str2 = TAG;
        if (parentName != null) {
            parentClassName = buildClassName(a.info.packageName, parentName, strArr);
            if (strArr[0] == null) {
                a.info.parentActivityName = parentClassName;
                i = 0;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(a.info.name);
                stringBuilder.append(" specified invalid parentActivityName ");
                stringBuilder.append(parentName);
                Log.e(str2, stringBuilder.toString());
                i = 0;
                strArr[0] = null;
            }
        } else {
            i = 0;
        }
        parentClassName = sa.getNonConfigurationString(4, i);
        if (parentClassName == null) {
            a.info.permission = packageR.applicationInfo.permission;
        } else {
            a.info.permission = parentClassName.length() > 0 ? parentClassName.toString().intern() : null;
        }
        String str3 = sa.getNonConfigurationString(8, 1024);
        a.info.taskAffinity = buildTaskAffinityName(packageR.applicationInfo.packageName, packageR.applicationInfo.taskAffinity, str3, strArr);
        parentClassName = sa.getNonConfigurationString(48, 0);
        String str4 = " at ";
        if (!TextUtils.isEmpty(parentClassName)) {
            if (validateName(parentClassName, false, false) != null) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(a.info.name);
                stringBuilder2.append(" specified invalid splitName ");
                stringBuilder2.append(parentClassName);
                stringBuilder2.append(str4);
                stringBuilder2.append(this.mArchiveSourcePath);
                Slog.w(str2, stringBuilder2.toString());
            } else {
                a.info.splitName = parentClassName;
            }
        }
        a.info.flags = 0;
        if (sa.getBoolean(9, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 1;
        }
        if (sa.getBoolean(10, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 2;
        }
        if (sa.getBoolean(11, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 4;
        }
        if (sa.getBoolean(21, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 128;
        }
        if (sa.getBoolean(18, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 8;
        }
        if (sa.getBoolean(12, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 16;
        }
        if (sa.getBoolean(13, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 32;
        }
        if (sa.getBoolean(19, (packageR.applicationInfo.flags & 32) != 0)) {
            activityInfo = a.info;
            activityInfo.flags |= 64;
        }
        if (sa.getBoolean(22, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 256;
        }
        if (sa.getBoolean(29, false) || sa.getBoolean(39, false)) {
            activityInfo = a.info;
            activityInfo.flags = 1024 | activityInfo.flags;
        }
        if (sa.getBoolean(24, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 2048;
        }
        if (sa.getBoolean(56, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 536870912;
        }
        if (receiver) {
            z = hardwareAccelerated;
            type = str4;
            a.info.launchMode = 0;
            a.info.configChanges = 0;
            if (sa.getBoolean(28, false)) {
                activityInfo = a.info;
                activityInfo.flags |= 1073741824;
            }
            activityInfo = a.info;
            activityInfo2 = a.info;
            boolean z3 = sa.getBoolean(42, false);
            activityInfo2.directBootAware = z3;
            activityInfo.encryptionAware = z3;
        } else {
            if (sa.getBoolean(25, hardwareAccelerated)) {
                activityInfo = a.info;
                activityInfo.flags |= 512;
            }
            type = str4;
            a.info.launchMode = sa.getInt(14, 0);
            a.info.documentLaunchMode = sa.getInt(33, 0);
            a.info.maxRecents = sa.getInt(34, ActivityTaskManager.getDefaultAppRecentsLimitStatic());
            a.info.configChanges = getActivityConfigChanges(sa.getInt(16, 0), sa.getInt(47, 0));
            a.info.softInputMode = sa.getInt(20, 0);
            a.info.persistableMode = sa.getInteger(32, 0);
            if (sa.getBoolean(31, false)) {
                activityInfo = a.info;
                activityInfo.flags |= Integer.MIN_VALUE;
            }
            if (sa.getBoolean(35, false)) {
                activityInfo = a.info;
                activityInfo.flags |= 8192;
            }
            if (sa.getBoolean(36, false)) {
                activityInfo = a.info;
                activityInfo.flags |= 4096;
            }
            if (sa.getBoolean(37, false)) {
                activityInfo = a.info;
                activityInfo.flags |= 16384;
            }
            a.info.screenOrientation = sa.getInt(15, -1);
            setActivityResizeMode(a.info, sa, packageR);
            if (sa.getBoolean(41, false)) {
                activityInfo = a.info;
                activityInfo.flags |= 4194304;
            }
            if (sa.getBoolean(55, false)) {
                activityInfo = a.info;
                activityInfo.flags |= 262144;
            }
            if (sa.hasValue(50) && sa.getType(50) == 4) {
                a.setMaxAspectRatio(sa.getFloat(50, 0.0f));
            }
            if (sa.hasValue(53) && sa.getType(53) == 4) {
                a.setMinAspectRatio(sa.getFloat(53, 0.0f));
            }
            a.info.lockTaskLaunchMode = sa.getInt(38, 0);
            activityInfo = a.info;
            activityInfo2 = a.info;
            boolean z4 = sa.getBoolean(42, false);
            activityInfo2.directBootAware = z4;
            activityInfo.encryptionAware = z4;
            a.info.requestedVrComponent = sa.getString(43);
            a.info.rotationAnimation = sa.getInt(46, -1);
            a.info.colorMode = sa.getInt(49, 0);
            if (sa.getBoolean(51, false)) {
                activityInfo = a.info;
                activityInfo.flags |= 8388608;
            }
            if (sa.getBoolean(52, false)) {
                activityInfo = a.info;
                activityInfo.flags |= 16777216;
            }
            if (sa.getBoolean(54, false)) {
                activityInfo = a.info;
                activityInfo.privateFlags |= 1;
            }
        }
        if (a.info.directBootAware) {
            ApplicationInfo applicationInfo = packageR.applicationInfo;
            applicationInfo.privateFlags |= 256;
        }
        boolean visibleToEphemeral = sa.getBoolean(45, false);
        if (visibleToEphemeral) {
            activityInfo = a.info;
            activityInfo.flags |= 1048576;
            packageR.visibleToInstantApps = true;
        }
        sa.recycle();
        if (!receiver || (packageR.applicationInfo.privateFlags & 2) == 0) {
            z2 = false;
        } else if (a.info.processName == packageR.packageName) {
            z2 = false;
            strArr[0] = "Heavy-weight applications can not have receivers in main process";
        } else {
            z2 = false;
        }
        if (strArr[z2] != null) {
            return null;
        }
        boolean z5;
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type2 = next;
            String[] strArr2;
            int i2;
            String str5;
            Resources sa2;
            XmlResourceParser xmlResourceParser;
            Package str6;
            if (next != 1) {
                TypedArray typedArray;
                int i3;
                if (type2 == 3 && parser.getDepth() <= outerDepth) {
                    typedArray = sa;
                    z5 = true;
                    strArr2 = strArr;
                    type = type2;
                    i2 = outerDepth;
                    str5 = str3;
                    sa2 = res;
                    xmlResourceParser = parser;
                    str6 = packageR;
                    break;
                } else if (type2 == 3) {
                    typedArray = sa;
                    int i4 = 1;
                    strArr2 = strArr;
                    i2 = outerDepth;
                    str5 = str3;
                    sa2 = res;
                    xmlResourceParser = parser;
                    str6 = packageR;
                    Object obj = type;
                    type = type2;
                    z = hardwareAccelerated;
                    type = obj;
                    str3 = str5;
                    sa = typedArray;
                    i3 = flags;
                } else if (type2 != 4) {
                    boolean equals = parser.getName().equals("intent-filter");
                    String str7 = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
                    IntentInfo intent;
                    StringBuilder stringBuilder3;
                    if (equals) {
                        intent = new ActivityIntentInfo(a);
                        typedArray = sa;
                        String str8 = str7;
                        strArr2 = strArr;
                        Object obj2 = type;
                        i2 = outerDepth;
                        str5 = str3;
                        str6 = packageR;
                        if (!parseIntent(res, parser, true, true, intent, outError)) {
                            return null;
                        }
                        if (intent.countActions() == 0) {
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("No actions in intent filter at ");
                            stringBuilder3.append(this.mArchiveSourcePath);
                            stringBuilder3.append(str8);
                            stringBuilder3.append(parser.getPositionDescription());
                            Slog.w(str2, stringBuilder3.toString());
                            sa = intent;
                        } else {
                            a.order = Math.max(intent.getOrder(), a.order);
                            sa = intent;
                            a.intents.add(sa);
                        }
                        if (visibleToEphemeral) {
                            next = 1;
                        } else if (receiver || !isImplicitlyExposedIntent(sa)) {
                            next = z2;
                        } else {
                            next = 2;
                        }
                        sa.setVisibilityToInstantApp(next);
                        if (sa.isVisibleToInstantApp()) {
                            activityInfo2 = a.info;
                            activityInfo2.flags |= 1048576;
                        }
                        if (sa.isImplicitlyVisibleToInstantApp()) {
                            activityInfo2 = a.info;
                            activityInfo2.flags |= 2097152;
                        }
                        strArr = strArr2;
                        packageR = str6;
                        outerDepth = i2;
                        str3 = str5;
                        sa = typedArray;
                        type = obj2;
                        i3 = flags;
                    } else {
                        typedArray = sa;
                        strArr2 = strArr;
                        i2 = outerDepth;
                        str5 = str3;
                        String str9 = type;
                        type = type2;
                        str6 = packageR;
                        String str10 = str7;
                        String str11;
                        if (receiver || !parser.getName().equals("preferred")) {
                            str11 = str10;
                            if (parser.getName().equals("meta-data")) {
                                Bundle parseMetaData = parseMetaData(res, parser, a.metaData, strArr2);
                                a.metaData = parseMetaData;
                                if (parseMetaData == null) {
                                    return null;
                                }
                                z = hardwareAccelerated;
                                strArr = strArr2;
                                packageR = str6;
                                outerDepth = i2;
                                str3 = str5;
                                sa = typedArray;
                                type = str9;
                                i3 = flags;
                            } else {
                                sa2 = res;
                                xmlResourceParser = parser;
                                if (receiver || !parser.getName().equals(TtmlUtils.TAG_LAYOUT)) {
                                    StringBuilder stringBuilder4 = new StringBuilder();
                                    stringBuilder4.append("Problem in package ");
                                    stringBuilder4.append(this.mArchiveSourcePath);
                                    stringBuilder4.append(":");
                                    Slog.w(str2, stringBuilder4.toString());
                                    if (receiver) {
                                        stringBuilder4 = new StringBuilder();
                                        stringBuilder4.append("Unknown element under <receiver>: ");
                                        stringBuilder4.append(parser.getName());
                                        str4 = str9;
                                        stringBuilder4.append(str4);
                                        stringBuilder4.append(this.mArchiveSourcePath);
                                        stringBuilder4.append(str11);
                                        stringBuilder4.append(parser.getPositionDescription());
                                        Slog.w(str2, stringBuilder4.toString());
                                    } else {
                                        str4 = str9;
                                        stringBuilder4 = new StringBuilder();
                                        stringBuilder4.append("Unknown element under <activity>: ");
                                        stringBuilder4.append(parser.getName());
                                        stringBuilder4.append(str4);
                                        stringBuilder4.append(this.mArchiveSourcePath);
                                        stringBuilder4.append(str11);
                                        stringBuilder4.append(parser.getPositionDescription());
                                        Slog.w(str2, stringBuilder4.toString());
                                    }
                                    XmlUtils.skipCurrentTag(parser);
                                    z = hardwareAccelerated;
                                    type = str4;
                                    strArr = strArr2;
                                    packageR = str6;
                                    outerDepth = i2;
                                    str3 = str5;
                                    sa = typedArray;
                                    i3 = flags;
                                } else {
                                    parseLayout(sa2, xmlResourceParser, a);
                                    z = hardwareAccelerated;
                                    strArr = strArr2;
                                    packageR = str6;
                                    outerDepth = i2;
                                    str3 = str5;
                                    sa = typedArray;
                                    type = str9;
                                    i3 = flags;
                                }
                            }
                        } else {
                            IntentInfo activityIntentInfo = new ActivityIntentInfo(a);
                            intent = activityIntentInfo;
                            str11 = str10;
                            if (!parseIntent(res, parser, false, false, activityIntentInfo, outError)) {
                                return null;
                            }
                            if (intent.countActions() == 0) {
                                stringBuilder3 = new StringBuilder();
                                stringBuilder3.append("No actions in preferred at ");
                                stringBuilder3.append(this.mArchiveSourcePath);
                                stringBuilder3.append(str11);
                                stringBuilder3.append(parser.getPositionDescription());
                                Slog.w(str2, stringBuilder3.toString());
                                sa = intent;
                            } else {
                                if (str6.preferredActivityFilters == null) {
                                    str6.preferredActivityFilters = new ArrayList();
                                }
                                sa = intent;
                                str6.preferredActivityFilters.add(sa);
                            }
                            if (visibleToEphemeral) {
                                next = 1;
                            } else if (receiver || !isImplicitlyExposedIntent(sa)) {
                                next = z2;
                            } else {
                                next = 2;
                            }
                            sa.setVisibilityToInstantApp(next);
                            if (sa.isVisibleToInstantApp()) {
                                activityInfo2 = a.info;
                                activityInfo2.flags |= 1048576;
                            }
                            if (sa.isImplicitlyVisibleToInstantApp()) {
                                activityInfo2 = a.info;
                                activityInfo2.flags |= 2097152;
                            }
                            z = hardwareAccelerated;
                            strArr = strArr2;
                            packageR = str6;
                            outerDepth = i2;
                            str3 = str5;
                            sa = typedArray;
                            type = str9;
                            i3 = flags;
                        }
                    }
                }
            } else {
                z5 = true;
                strArr2 = strArr;
                type = type2;
                i2 = outerDepth;
                str5 = str3;
                sa2 = res;
                xmlResourceParser = parser;
                str6 = packageR;
                break;
            }
        }
        if (!setExported) {
            a.info.exported = a.intents.size() > 0 ? z5 : z2;
        }
        return a;
    }

    private void setActivityResizeMode(ActivityInfo aInfo, TypedArray sa, Package owner) {
        boolean appResizeable = true;
        boolean appExplicitDefault = (owner.applicationInfo.privateFlags & 3072) != 0;
        if (sa.hasValue(40) || appExplicitDefault) {
            if ((owner.applicationInfo.privateFlags & 1024) == 0) {
                appResizeable = false;
            }
            if (sa.getBoolean(40, appResizeable)) {
                aInfo.resizeMode = 2;
            } else {
                aInfo.resizeMode = 0;
            }
        } else if ((owner.applicationInfo.privateFlags & 4096) != 0) {
            aInfo.resizeMode = 1;
        } else {
            if (aInfo.isFixedOrientationPortrait()) {
                aInfo.resizeMode = 6;
            } else if (aInfo.isFixedOrientationLandscape()) {
                aInfo.resizeMode = 5;
            } else if (aInfo.isFixedOrientation()) {
                aInfo.resizeMode = 7;
            } else {
                aInfo.resizeMode = 4;
            }
        }
    }

    private void setMaxAspectRatio(Package owner) {
        float maxAspectRatio = owner.applicationInfo.targetSdkVersion < 26 ? DEFAULT_PRE_O_MAX_ASPECT_RATIO : 0.0f;
        String str = "android.max_aspect";
        if (owner.applicationInfo.maxAspectRatio != 0.0f) {
            maxAspectRatio = owner.applicationInfo.maxAspectRatio;
        } else if (owner.mAppMetaData != null && owner.mAppMetaData.containsKey(str)) {
            maxAspectRatio = owner.mAppMetaData.getFloat(str, maxAspectRatio);
        }
        Iterator it = owner.activities.iterator();
        while (it.hasNext()) {
            Activity activity = (Activity) it.next();
            if (!activity.hasMaxAspectRatio()) {
                float activityAspectRatio;
                if (activity.metaData != null) {
                    activityAspectRatio = activity.metaData.getFloat(str, maxAspectRatio);
                } else {
                    activityAspectRatio = maxAspectRatio;
                }
                activity.setMaxAspectRatio(activityAspectRatio);
            }
        }
    }

    private void setMinAspectRatio(Package owner) {
        float minAspectRatio;
        float f = 0.0f;
        if (owner.applicationInfo.minAspectRatio != 0.0f) {
            minAspectRatio = owner.applicationInfo.minAspectRatio;
        } else {
            if (owner.applicationInfo.targetSdkVersion < 29) {
                Callback callback = this.mCallback;
                if (callback == null || !callback.hasFeature(PackageManager.FEATURE_WATCH)) {
                    f = DEFAULT_PRE_Q_MIN_ASPECT_RATIO;
                } else {
                    f = 1.0f;
                }
            }
            minAspectRatio = f;
        }
        Iterator it = owner.activities.iterator();
        while (it.hasNext()) {
            Activity activity = (Activity) it.next();
            if (!activity.hasMinAspectRatio()) {
                activity.setMinAspectRatio(minAspectRatio);
            }
        }
    }

    public static int getActivityConfigChanges(int configChanges, int recreateOnConfigChanges) {
        return ((~recreateOnConfigChanges) & 3) | configChanges;
    }

    private void parseLayout(Resources res, AttributeSet attrs, Activity a) {
        TypedArray sw = res.obtainAttributes(attrs, R.styleable.AndroidManifestLayout);
        int width = -1;
        float widthFraction = -1.0f;
        int height = -1;
        float heightFraction = -1.0f;
        int widthType = sw.getType(3);
        if (widthType == 6) {
            widthFraction = sw.getFraction(3, 1, 1, -1.0f);
        } else if (widthType == 5) {
            width = sw.getDimensionPixelSize(3, -1);
        }
        int heightType = sw.getType(4);
        if (heightType == 6) {
            heightFraction = sw.getFraction(4, 1, 1, -1.0f);
        } else if (heightType == 5) {
            height = sw.getDimensionPixelSize(4, -1);
        }
        int gravity = sw.getInt(0, 17);
        int minWidth = sw.getDimensionPixelSize(1, -1);
        int minHeight = sw.getDimensionPixelSize(2, -1);
        sw.recycle();
        a.info.windowLayout = new WindowLayout(width, widthFraction, height, heightFraction, gravity, minWidth, minHeight);
    }

    private Activity parseActivityAlias(Package owner, Resources res, XmlResourceParser parser, int flags, String[] outError, CachedComponentArgs cachedArgs) throws XmlPullParserException, IOException {
        Package packageR = owner;
        Resources resources = res;
        String[] strArr = outError;
        CachedComponentArgs cachedComponentArgs = cachedArgs;
        TypedArray sa = resources.obtainAttributes(parser, R.styleable.AndroidManifestActivityAlias);
        String targetActivity = sa.getNonConfigurationString(7, 1024);
        if (targetActivity == null) {
            strArr[0] = "<activity-alias> does not specify android:targetActivity";
            sa.recycle();
            return null;
        }
        String targetActivity2 = buildClassName(packageR.applicationInfo.packageName, targetActivity, strArr);
        if (targetActivity2 == null) {
            sa.recycle();
            return null;
        }
        String targetActivity3;
        Activity target;
        if (cachedComponentArgs.mActivityAliasArgs == null) {
            ParseComponentArgs parseComponentArgs = r8;
            targetActivity3 = targetActivity2;
            ParseComponentArgs parseComponentArgs2 = new ParseComponentArgs(owner, outError, 2, 0, 1, 11, 8, 10, this.mSeparateProcesses, 0, 6, 4);
            cachedComponentArgs.mActivityAliasArgs = parseComponentArgs;
            cachedComponentArgs.mActivityAliasArgs.tag = "<activity-alias>";
        } else {
            targetActivity3 = targetActivity2;
        }
        cachedComponentArgs.mActivityAliasArgs.sa = sa;
        cachedComponentArgs.mActivityAliasArgs.flags = flags;
        int NA = packageR.activities.size();
        for (int i = 0; i < NA; i++) {
            Activity t = (Activity) packageR.activities.get(i);
            if (targetActivity3.equals(t.info.name)) {
                target = t;
                break;
            }
        }
        target = null;
        StringBuilder stringBuilder;
        if (target == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("<activity-alias> target activity ");
            stringBuilder.append(targetActivity3);
            stringBuilder.append(" not found in manifest");
            strArr[0] = stringBuilder.toString();
            sa.recycle();
            return null;
        }
        ActivityInfo info = new ActivityInfo();
        info.targetActivity = targetActivity3;
        info.configChanges = target.info.configChanges;
        info.flags = target.info.flags;
        info.privateFlags = target.info.privateFlags;
        info.icon = target.info.icon;
        info.logo = target.info.logo;
        info.banner = target.info.banner;
        info.labelRes = target.info.labelRes;
        info.nonLocalizedLabel = target.info.nonLocalizedLabel;
        info.launchMode = target.info.launchMode;
        info.lockTaskLaunchMode = target.info.lockTaskLaunchMode;
        info.processName = target.info.processName;
        if (info.descriptionRes == 0) {
            info.descriptionRes = target.info.descriptionRes;
        }
        info.screenOrientation = target.info.screenOrientation;
        info.taskAffinity = target.info.taskAffinity;
        info.theme = target.info.theme;
        info.softInputMode = target.info.softInputMode;
        info.uiOptions = target.info.uiOptions;
        info.parentActivityName = target.info.parentActivityName;
        info.maxRecents = target.info.maxRecents;
        info.windowLayout = target.info.windowLayout;
        info.resizeMode = target.info.resizeMode;
        info.maxAspectRatio = target.info.maxAspectRatio;
        info.minAspectRatio = target.info.minAspectRatio;
        info.requestedVrComponent = target.info.requestedVrComponent;
        boolean z = target.info.directBootAware;
        info.directBootAware = z;
        info.encryptionAware = z;
        Activity a = new Activity(cachedComponentArgs.mActivityAliasArgs, info);
        if (strArr[0] != null) {
            sa.recycle();
            return null;
        }
        String targetActivity4;
        boolean setExported = sa.hasValue(5);
        if (setExported) {
            a.info.exported = sa.getBoolean(5, false);
        }
        String str = sa.getNonConfigurationString(3, 0);
        if (str != null) {
            a.info.permission = str.length() > 0 ? str.toString().intern() : null;
        }
        String parentName = sa.getNonConfigurationString(9, 1024);
        String str2 = TAG;
        if (parentName != null) {
            String parentClassName = buildClassName(a.info.packageName, parentName, strArr);
            if (strArr[0] == null) {
                a.info.parentActivityName = parentClassName;
                targetActivity4 = targetActivity3;
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                targetActivity4 = targetActivity3;
                stringBuilder2.append("Activity alias ");
                stringBuilder2.append(a.info.name);
                stringBuilder2.append(" specified invalid parentActivityName ");
                stringBuilder2.append(parentName);
                Log.e(str2, stringBuilder2.toString());
                strArr[0] = null;
            }
        } else {
            targetActivity4 = targetActivity3;
        }
        boolean z2 = true;
        boolean visibleToEphemeral = (a.info.flags & 1048576) != 0;
        sa.recycle();
        if (strArr[0] != null) {
            return null;
        }
        boolean type;
        int outerDepth = parser.getDepth();
        while (true) {
            boolean next = parser.next();
            type = next;
            int i2;
            String[] strArr2;
            Resources resources2;
            String str3;
            XmlResourceParser xmlResourceParser;
            if (next == z2) {
                i2 = outerDepth;
                strArr2 = strArr;
                resources2 = resources;
                str3 = targetActivity4;
                next = type;
                type = z2;
                xmlResourceParser = parser;
                break;
            }
            next = type;
            TypedArray typedArray;
            int type2;
            int i3;
            if (next && parser.getDepth() <= outerDepth) {
                xmlResourceParser = parser;
                typedArray = sa;
                i2 = outerDepth;
                strArr2 = strArr;
                resources2 = resources;
                str3 = targetActivity4;
                type = true;
                break;
            } else if (next) {
                i2 = outerDepth;
                str3 = targetActivity4;
                outerDepth = str2;
                targetActivity4 = 3;
                xmlResourceParser = parser;
                packageR = owner;
                type2 = 1048576;
                targetActivity4 = str3;
                z2 = true;
                outerDepth = i2;
                resources = resources;
                strArr = strArr;
                i3 = flags;
            } else if (next) {
                z2 = true;
            } else {
                String str4 = str2;
                z = parser.getName().equals("intent-filter");
                targetActivity3 = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
                String str5;
                if (z) {
                    IntentInfo intent = new ActivityIntentInfo(a);
                    String str6 = str4;
                    typedArray = sa;
                    i2 = outerDepth;
                    strArr2 = strArr;
                    String str7 = targetActivity3;
                    str3 = targetActivity4;
                    if (!parseIntent(res, parser, true, true, intent, outError)) {
                        return null;
                    }
                    ActivityIntentInfo intent2;
                    int visibility;
                    ActivityInfo activityInfo;
                    int i4;
                    if (intent.countActions() == 0) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("No actions in intent filter at ");
                        stringBuilder.append(this.mArchiveSourcePath);
                        stringBuilder.append(str7);
                        stringBuilder.append(parser.getPositionDescription());
                        str5 = str6;
                        Slog.w(str5, stringBuilder.toString());
                        intent2 = intent;
                    } else {
                        str5 = str6;
                        a.order = Math.max(intent.getOrder(), a.order);
                        intent2 = intent;
                        a.intents.add(intent2);
                    }
                    if (visibleToEphemeral) {
                        visibility = 1;
                    } else if (isImplicitlyExposedIntent(intent2)) {
                        visibility = 2;
                    } else {
                        visibility = 0;
                    }
                    intent2.setVisibilityToInstantApp(visibility);
                    if (intent2.isVisibleToInstantApp()) {
                        activityInfo = a.info;
                        i4 = 1048576;
                        activityInfo.flags |= 1048576;
                    } else {
                        i4 = 1048576;
                    }
                    if (intent2.isImplicitlyVisibleToInstantApp()) {
                        activityInfo = a.info;
                        activityInfo.flags |= 2097152;
                    }
                    packageR = owner;
                    str2 = str5;
                    type2 = i4;
                    strArr = strArr2;
                    targetActivity4 = str3;
                    z2 = true;
                    sa = typedArray;
                    outerDepth = i2;
                    resources = res;
                    i3 = flags;
                } else {
                    i2 = outerDepth;
                    strArr2 = strArr;
                    str3 = targetActivity4;
                    str5 = str4;
                    typedArray = sa;
                    String str8 = targetActivity3;
                    if (parser.getName().equals("meta-data")) {
                        resources2 = res;
                        Bundle parseMetaData = parseMetaData(resources2, parser, a.metaData, strArr2);
                        a.metaData = parseMetaData;
                        if (parseMetaData == null) {
                            return null;
                        }
                        packageR = owner;
                        str2 = str5;
                        type2 = 1048576;
                        targetActivity4 = str3;
                        z2 = true;
                        sa = typedArray;
                        outerDepth = i2;
                        resources = resources2;
                        strArr = strArr2;
                        i3 = flags;
                    } else {
                        resources2 = res;
                        xmlResourceParser = parser;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown element under <activity-alias>: ");
                        stringBuilder.append(parser.getName());
                        stringBuilder.append(" at ");
                        stringBuilder.append(this.mArchiveSourcePath);
                        stringBuilder.append(str8);
                        stringBuilder.append(parser.getPositionDescription());
                        Slog.w(str5, stringBuilder.toString());
                        XmlUtils.skipCurrentTag(parser);
                        packageR = owner;
                        str2 = str5;
                        type2 = 1048576;
                        targetActivity4 = str3;
                        z2 = true;
                        sa = typedArray;
                        outerDepth = i2;
                        resources = resources2;
                        strArr = strArr2;
                        i3 = flags;
                    }
                }
            }
        }
        if (!setExported) {
            a.info.exported = a.intents.size() > 0 ? type : false;
        }
        return a;
    }

    private Provider parseProvider(Package owner, Resources res, XmlResourceParser parser, int flags, String[] outError, CachedComponentArgs cachedArgs) throws XmlPullParserException, IOException {
        Package packageR = owner;
        CachedComponentArgs cachedComponentArgs = cachedArgs;
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestProvider);
        if (cachedComponentArgs.mProviderArgs == null) {
            cachedComponentArgs.mProviderArgs = new ParseComponentArgs(owner, outError, 2, 0, 1, 19, 15, 17, this.mSeparateProcesses, 8, 14, 6);
            cachedComponentArgs.mProviderArgs.tag = "<provider>";
        }
        cachedComponentArgs.mProviderArgs.sa = sa;
        cachedComponentArgs.mProviderArgs.flags = flags;
        Provider p = new Provider(cachedComponentArgs.mProviderArgs, new ProviderInfo());
        if (outError[0] != null) {
            sa.recycle();
            return null;
        }
        ProviderInfo providerInfo;
        Provider provider;
        int i;
        boolean providerExportedDefault = false;
        if (packageR.applicationInfo.targetSdkVersion < 17) {
            providerExportedDefault = true;
        }
        p.info.exported = sa.getBoolean(7, providerExportedDefault);
        String cpname = sa.getNonConfigurationString(10, 0);
        p.info.isSyncable = sa.getBoolean(11, false);
        String permission = sa.getNonConfigurationString(3, 0);
        String str = sa.getNonConfigurationString(4, 0);
        if (str == null) {
            str = permission;
        }
        if (str == null) {
            p.info.readPermission = packageR.applicationInfo.permission;
        } else {
            p.info.readPermission = str.length() > 0 ? str.toString().intern() : null;
        }
        str = sa.getNonConfigurationString(5, 0);
        if (str == null) {
            str = permission;
        }
        if (str == null) {
            p.info.writePermission = packageR.applicationInfo.permission;
        } else {
            p.info.writePermission = str.length() > 0 ? str.toString().intern() : null;
        }
        p.info.grantUriPermissions = sa.getBoolean(13, false);
        p.info.forceUriPermissions = sa.getBoolean(22, false);
        p.info.multiprocess = sa.getBoolean(9, false);
        p.info.initOrder = sa.getInt(12, 0);
        String splitName = sa.getNonConfigurationString(21, 0);
        if (!TextUtils.isEmpty(splitName)) {
            if (validateName(splitName, false, false) != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Provider ");
                stringBuilder.append(p.info.name);
                stringBuilder.append(" specified invalid splitName ");
                stringBuilder.append(splitName);
                stringBuilder.append(" at ");
                stringBuilder.append(this.mArchiveSourcePath);
                Slog.w(TAG, stringBuilder.toString());
            } else {
                p.info.splitName = splitName;
            }
        }
        p.info.flags = 0;
        if (sa.getBoolean(16, false)) {
            providerInfo = p.info;
            providerInfo.flags |= 1073741824;
        }
        providerInfo = p.info;
        ProviderInfo providerInfo2 = p.info;
        boolean z = sa.getBoolean(18, false);
        providerInfo2.directBootAware = z;
        providerInfo.encryptionAware = z;
        if (p.info.directBootAware) {
            ApplicationInfo applicationInfo = packageR.applicationInfo;
            applicationInfo.privateFlags |= 256;
        }
        boolean visibleToEphemeral = sa.getBoolean(20, false);
        if (visibleToEphemeral) {
            providerInfo = p.info;
            providerInfo.flags |= 1048576;
            packageR.visibleToInstantApps = true;
        }
        sa.recycle();
        if ((packageR.applicationInfo.privateFlags & 2) == 0) {
            provider = null;
            i = 0;
        } else if (p.info.processName == packageR.packageName) {
            outError[0] = "Heavy-weight applications can not have providers in main process";
            return null;
        } else {
            provider = null;
            i = 0;
        }
        if (cpname == null) {
            outError[i] = "<provider> does not include authorities attribute";
            return provider;
        } else if (cpname.length() <= 0) {
            outError[i] = "<provider> has empty authorities attribute";
            return provider;
        } else {
            p.info.authority = cpname.intern();
            if (parseProviderTags(res, parser, visibleToEphemeral, p, outError)) {
                return p;
            }
            return null;
        }
    }

    private boolean parseProviderTags(Resources res, XmlResourceParser parser, boolean visibleToEphemeral, Provider outInfo, String[] outError) throws XmlPullParserException, IOException {
        Resources resources = res;
        XmlResourceParser xmlResourceParser = parser;
        Provider provider = outInfo;
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if (next != 1 && (type != 3 || parser.getDepth() > outerDepth)) {
                if (type != 3) {
                    if (type != 4) {
                        if (parser.getName().equals("intent-filter")) {
                            IntentInfo intent = new ProviderIntentInfo(provider);
                            if (!parseIntent(res, parser, true, false, intent, outError)) {
                                return false;
                            }
                            if (visibleToEphemeral) {
                                intent.setVisibilityToInstantApp(1);
                                ProviderInfo providerInfo = provider.info;
                                providerInfo.flags |= 1048576;
                            }
                            provider.order = Math.max(intent.getOrder(), provider.order);
                            provider.intents.add(intent);
                        } else if (parser.getName().equals("meta-data")) {
                            Bundle parseMetaData = parseMetaData(resources, xmlResourceParser, provider.metaData, outError);
                            provider.metaData = parseMetaData;
                            if (parseMetaData == null) {
                                return false;
                            }
                        } else {
                            String[] strArr = outError;
                            boolean equals = parser.getName().equals("grant-uri-permission");
                            String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
                            String str2 = " at ";
                            String str3 = TAG;
                            StringBuilder stringBuilder;
                            if (equals) {
                                TypedArray sa = resources.obtainAttributes(xmlResourceParser, R.styleable.AndroidManifestGrantUriPermission);
                                PatternMatcher pa = null;
                                String str4 = sa.getNonConfigurationString(0, 0);
                                if (str4 != null) {
                                    pa = new PatternMatcher(str4, 0);
                                }
                                str4 = sa.getNonConfigurationString(1, 0);
                                if (str4 != null) {
                                    pa = new PatternMatcher(str4, 1);
                                }
                                str4 = sa.getNonConfigurationString(2, 0);
                                if (str4 != null) {
                                    pa = new PatternMatcher(str4, 2);
                                }
                                sa.recycle();
                                if (pa != null) {
                                    if (provider.info.uriPermissionPatterns == null) {
                                        provider.info.uriPermissionPatterns = new PatternMatcher[1];
                                        provider.info.uriPermissionPatterns[0] = pa;
                                    } else {
                                        int N = provider.info.uriPermissionPatterns.length;
                                        PatternMatcher[] newp = new PatternMatcher[(N + 1)];
                                        System.arraycopy(provider.info.uriPermissionPatterns, 0, newp, 0, N);
                                        newp[N] = pa;
                                        provider.info.uriPermissionPatterns = newp;
                                    }
                                    provider.info.grantUriPermissions = true;
                                    XmlUtils.skipCurrentTag(parser);
                                } else {
                                    StringBuilder stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append("Unknown element under <path-permission>: ");
                                    stringBuilder2.append(parser.getName());
                                    stringBuilder2.append(str2);
                                    stringBuilder2.append(this.mArchiveSourcePath);
                                    stringBuilder2.append(str);
                                    stringBuilder2.append(parser.getPositionDescription());
                                    Slog.w(str3, stringBuilder2.toString());
                                    XmlUtils.skipCurrentTag(parser);
                                }
                            } else if (parser.getName().equals("path-permission")) {
                                String readPermission;
                                String readPermission2;
                                String writePermission;
                                TypedArray sa2 = resources.obtainAttributes(xmlResourceParser, R.styleable.AndroidManifestPathPermission);
                                String permission = sa2.getNonConfigurationString(0, 0);
                                String readPermission3 = sa2.getNonConfigurationString(1, 0);
                                if (readPermission3 == null) {
                                    readPermission = permission;
                                } else {
                                    readPermission = readPermission3;
                                }
                                String writePermission2 = sa2.getNonConfigurationString(2, 0);
                                if (writePermission2 == null) {
                                    writePermission2 = permission;
                                }
                                boolean havePerm = false;
                                if (readPermission != null) {
                                    havePerm = true;
                                    readPermission2 = readPermission.intern();
                                } else {
                                    readPermission2 = readPermission;
                                }
                                if (writePermission2 != null) {
                                    havePerm = true;
                                    writePermission = writePermission2.intern();
                                } else {
                                    writePermission = writePermission2;
                                }
                                if (havePerm) {
                                    PathPermission pa2;
                                    PathPermission pa3;
                                    PathPermission pa4 = null;
                                    String path = sa2.getNonConfigurationString(3, 0);
                                    if (path != null) {
                                        pa2 = new PathPermission(path, 0, readPermission2, writePermission);
                                    } else {
                                        pa2 = pa4;
                                    }
                                    PathPermission pa5 = pa2;
                                    String path2 = sa2.getNonConfigurationString(4, 0);
                                    if (path2 != null) {
                                        pa3 = new PathPermission(path2, 1, readPermission2, writePermission);
                                    } else {
                                        pa3 = pa5;
                                    }
                                    path = sa2.getNonConfigurationString(5, null);
                                    if (path != null) {
                                        pa3 = new PathPermission(path, 2, readPermission2, writePermission);
                                    } else {
                                        PathPermission pathPermission = pa3;
                                    }
                                    PathPermission pa6 = pa3;
                                    path2 = sa2.getNonConfigurationString(6, null);
                                    if (path2 != null) {
                                        pa6 = new PathPermission(path2, 3, readPermission2, writePermission);
                                    }
                                    sa2.recycle();
                                    if (pa6 != null) {
                                        if (provider.info.pathPermissions == null) {
                                            provider.info.pathPermissions = new PathPermission[1];
                                            provider.info.pathPermissions[0] = pa6;
                                        } else {
                                            int N2 = provider.info.pathPermissions.length;
                                            PathPermission[] newp2 = new PathPermission[(N2 + 1)];
                                            System.arraycopy(provider.info.pathPermissions, 0, newp2, 0, N2);
                                            newp2[N2] = pa6;
                                            provider.info.pathPermissions = newp2;
                                        }
                                        XmlUtils.skipCurrentTag(parser);
                                    } else {
                                        StringBuilder stringBuilder3 = new StringBuilder();
                                        stringBuilder3.append("No path, pathPrefix, or pathPattern for <path-permission>: ");
                                        stringBuilder3.append(parser.getName());
                                        stringBuilder3.append(str2);
                                        stringBuilder3.append(this.mArchiveSourcePath);
                                        stringBuilder3.append(str);
                                        stringBuilder3.append(parser.getPositionDescription());
                                        Slog.w(str3, stringBuilder3.toString());
                                        XmlUtils.skipCurrentTag(parser);
                                    }
                                } else {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("No readPermission or writePermssion for <path-permission>: ");
                                    stringBuilder.append(parser.getName());
                                    stringBuilder.append(str2);
                                    stringBuilder.append(this.mArchiveSourcePath);
                                    stringBuilder.append(str);
                                    stringBuilder.append(parser.getPositionDescription());
                                    Slog.w(str3, stringBuilder.toString());
                                    XmlUtils.skipCurrentTag(parser);
                                }
                            } else {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Unknown element under <provider>: ");
                                stringBuilder.append(parser.getName());
                                stringBuilder.append(str2);
                                stringBuilder.append(this.mArchiveSourcePath);
                                stringBuilder.append(str);
                                stringBuilder.append(parser.getPositionDescription());
                                Slog.w(str3, stringBuilder.toString());
                                XmlUtils.skipCurrentTag(parser);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private Service parseService(Package owner, Resources res, XmlResourceParser parser, int flags, String[] outError, CachedComponentArgs cachedArgs) throws XmlPullParserException, IOException {
        Package packageR = owner;
        Resources resources = res;
        String[] strArr = outError;
        CachedComponentArgs cachedComponentArgs = cachedArgs;
        TypedArray sa = resources.obtainAttributes(parser, R.styleable.AndroidManifestService);
        if (cachedComponentArgs.mServiceArgs == null) {
            cachedComponentArgs.mServiceArgs = new ParseComponentArgs(owner, outError, 2, 0, 1, 15, 8, 12, this.mSeparateProcesses, 6, 7, 4);
            cachedComponentArgs.mServiceArgs.tag = "<service>";
        }
        cachedComponentArgs.mServiceArgs.sa = sa;
        cachedComponentArgs.mServiceArgs.flags = flags;
        Service s = new Service(cachedComponentArgs.mServiceArgs, new ServiceInfo());
        if (strArr[0] != null) {
            sa.recycle();
            return null;
        }
        ServiceInfo serviceInfo;
        boolean z;
        boolean sa2;
        boolean setExported = sa.hasValue(5);
        if (setExported) {
            s.info.exported = sa.getBoolean(5, false);
        }
        String str = sa.getNonConfigurationString(3, 0);
        if (str == null) {
            s.info.permission = packageR.applicationInfo.permission;
        } else {
            s.info.permission = str.length() > 0 ? str.toString().intern() : null;
        }
        String splitName = sa.getNonConfigurationString(17, 0);
        boolean isEmpty = TextUtils.isEmpty(splitName);
        String str2 = " at ";
        String str3 = TAG;
        if (!isEmpty) {
            if (validateName(splitName, false, false) != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Service ");
                stringBuilder.append(s.info.name);
                stringBuilder.append(" specified invalid splitName ");
                stringBuilder.append(splitName);
                stringBuilder.append(str2);
                stringBuilder.append(this.mArchiveSourcePath);
                Slog.w(str3, stringBuilder.toString());
            } else {
                s.info.splitName = splitName;
            }
        }
        s.info.mForegroundServiceType = sa.getInt(19, 0);
        s.info.flags = 0;
        if (sa.getBoolean(9, false)) {
            serviceInfo = s.info;
            serviceInfo.flags |= 1;
        }
        if (sa.getBoolean(10, false)) {
            serviceInfo = s.info;
            serviceInfo.flags |= 2;
        }
        if (sa.getBoolean(14, false)) {
            serviceInfo = s.info;
            serviceInfo.flags |= 4;
        }
        if (sa.getBoolean(18, false)) {
            serviceInfo = s.info;
            serviceInfo.flags |= 8;
        }
        if (sa.getBoolean(11, false)) {
            serviceInfo = s.info;
            serviceInfo.flags |= 1073741824;
        }
        serviceInfo = s.info;
        ServiceInfo serviceInfo2 = s.info;
        boolean z2 = sa.getBoolean(13, false);
        serviceInfo2.directBootAware = z2;
        serviceInfo.encryptionAware = z2;
        if (s.info.directBootAware) {
            ApplicationInfo applicationInfo = packageR.applicationInfo;
            applicationInfo.privateFlags |= 256;
        }
        boolean visibleToEphemeral = sa.getBoolean(16, false);
        if (visibleToEphemeral) {
            serviceInfo = s.info;
            serviceInfo.flags |= 1048576;
            packageR.visibleToInstantApps = true;
        }
        sa.recycle();
        if ((packageR.applicationInfo.privateFlags & 2) == 0) {
            z = false;
        } else if (s.info.processName == packageR.packageName) {
            strArr[0] = "Heavy-weight applications can not have services in main process";
            return null;
        } else {
            z = false;
        }
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            int i;
            String[] strArr2;
            int i2;
            XmlResourceParser xmlResourceParser;
            if (next == 1) {
                i = outerDepth;
                strArr2 = strArr;
                outerDepth = resources;
                sa2 = true;
                i2 = type;
                xmlResourceParser = parser;
                break;
            }
            i2 = type;
            TypedArray typedArray;
            int i3;
            if (i2 == 3 && parser.getDepth() <= outerDepth) {
                typedArray = sa;
                i = outerDepth;
                strArr2 = strArr;
                outerDepth = resources;
                sa2 = true;
                xmlResourceParser = parser;
                break;
            } else if (i2 != 3) {
                String str4 = str2;
                String str5;
                if (i2 == 4) {
                    str2 = str4;
                } else if (parser.getName().equals("intent-filter")) {
                    typedArray = sa;
                    i = outerDepth;
                    strArr2 = strArr;
                    str5 = str4;
                    IntentInfo intent = new ServiceIntentInfo(s);
                    if (!parseIntent(res, parser, true, false, intent, outError)) {
                        return null;
                    }
                    ServiceIntentInfo intent2;
                    int i4;
                    if (visibleToEphemeral) {
                        intent2 = intent;
                        intent2.setVisibilityToInstantApp(1);
                        ServiceInfo serviceInfo3 = s.info;
                        i4 = 1048576;
                        serviceInfo3.flags |= 1048576;
                    } else {
                        intent2 = intent;
                        i4 = 1048576;
                    }
                    s.order = Math.max(intent2.getOrder(), s.order);
                    s.intents.add(intent2);
                    packageR = owner;
                    resources = res;
                    i2 = i4;
                    strArr = strArr2;
                    sa = typedArray;
                    outerDepth = i;
                    str2 = str5;
                    i3 = flags;
                } else {
                    typedArray = sa;
                    i = outerDepth;
                    strArr2 = strArr;
                    str5 = str4;
                    Resources resources2;
                    if (parser.getName().equals("meta-data")) {
                        resources2 = res;
                        Bundle parseMetaData = parseMetaData(resources2, parser, s.metaData, strArr2);
                        s.metaData = parseMetaData;
                        if (parseMetaData == null) {
                            return null;
                        }
                        packageR = owner;
                        resources = resources2;
                        i2 = 1048576;
                        strArr = strArr2;
                        sa = typedArray;
                        outerDepth = i;
                        str2 = str5;
                        i3 = flags;
                    } else {
                        resources2 = res;
                        xmlResourceParser = parser;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Unknown element under <service>: ");
                        stringBuilder2.append(parser.getName());
                        String str6 = str5;
                        stringBuilder2.append(str6);
                        stringBuilder2.append(this.mArchiveSourcePath);
                        stringBuilder2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        stringBuilder2.append(parser.getPositionDescription());
                        Slog.w(str3, stringBuilder2.toString());
                        XmlUtils.skipCurrentTag(parser);
                        resources = resources2;
                        i2 = 1048576;
                        str2 = str6;
                        strArr = strArr2;
                        sa = typedArray;
                        outerDepth = i;
                        packageR = owner;
                        i3 = flags;
                    }
                }
            } else {
                int i5 = 3;
                i = outerDepth;
                strArr2 = strArr;
                outerDepth = resources;
                xmlResourceParser = parser;
                i2 = 1048576;
                str2 = str2;
                strArr = strArr2;
                sa = sa;
                outerDepth = i;
                packageR = owner;
                i3 = flags;
            }
        }
        if (!setExported) {
            serviceInfo = s.info;
            if (s.intents.size() <= 0) {
                sa2 = z;
            }
            serviceInfo.exported = sa2;
        }
        return s;
    }

    private boolean isImplicitlyExposedIntent(IntentInfo intent) {
        return intent.hasCategory(Intent.CATEGORY_BROWSABLE) || intent.hasAction(Intent.ACTION_SEND) || intent.hasAction(Intent.ACTION_SENDTO) || intent.hasAction(Intent.ACTION_SEND_MULTIPLE);
    }

    private boolean parseAllMetaData(Resources res, XmlResourceParser parser, String tag, Component<?> outInfo, String[] outError) throws XmlPullParserException, IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                return true;
            }
            if (type != 3) {
                if (type != 4) {
                    if (parser.getName().equals("meta-data")) {
                        Bundle parseMetaData = parseMetaData(res, parser, outInfo.metaData, outError);
                        outInfo.metaData = parseMetaData;
                        if (parseMetaData == null) {
                            return false;
                        }
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown element under ");
                        stringBuilder.append(tag);
                        stringBuilder.append(": ");
                        stringBuilder.append(parser.getName());
                        stringBuilder.append(" at ");
                        stringBuilder.append(this.mArchiveSourcePath);
                        stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        stringBuilder.append(parser.getPositionDescription());
                        Slog.w(TAG, stringBuilder.toString());
                        XmlUtils.skipCurrentTag(parser);
                    }
                }
            }
        }
        return true;
    }

    private Bundle parseMetaData(Resources res, XmlResourceParser parser, Bundle data, String[] outError) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestMetaData);
        if (data == null) {
            data = new Bundle();
        }
        boolean z = false;
        String name = sa.getNonConfigurationString(0, 0);
        String str = null;
        if (name == null) {
            outError[0] = "<meta-data> requires an android:name attribute";
            sa.recycle();
            return null;
        }
        name = name.intern();
        TypedValue v = sa.peekValue(2);
        if (v == null || v.resourceId == 0) {
            v = sa.peekValue(1);
            if (v == null) {
                outError[0] = "<meta-data> requires an android:value or android:resource attribute";
                data = null;
            } else if (v.type == 3) {
                CharSequence cs = v.coerceToString();
                if (cs != null) {
                    str = cs.toString();
                }
                data.putString(name, str);
            } else if (v.type == 18) {
                if (v.data != 0) {
                    z = true;
                }
                data.putBoolean(name, z);
            } else if (v.type >= 16 && v.type <= 31) {
                data.putInt(name, v.data);
            } else if (v.type == 4) {
                data.putFloat(name, v.getFloat());
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("<meta-data> only supports string, integer, float, color, boolean, and resource reference types: ");
                stringBuilder.append(parser.getName());
                stringBuilder.append(" at ");
                stringBuilder.append(this.mArchiveSourcePath);
                stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                stringBuilder.append(parser.getPositionDescription());
                Slog.w(TAG, stringBuilder.toString());
            }
        } else {
            data.putInt(name, v.resourceId);
        }
        sa.recycle();
        XmlUtils.skipCurrentTag(parser);
        return data;
    }

    private static VerifierInfo parseVerifier(AttributeSet attrs) {
        String packageName = null;
        String encodedPublicKey = null;
        int attrCount = attrs.getAttributeCount();
        for (int i = 0; i < attrCount; i++) {
            int attrResId = attrs.getAttributeNameResource(i);
            if (attrResId == 16842755) {
                packageName = attrs.getAttributeValue(i);
            } else if (attrResId == 16843686) {
                encodedPublicKey = attrs.getAttributeValue(i);
            }
        }
        String str = TAG;
        if (packageName == null || packageName.length() == 0) {
            Slog.i(str, "verifier package name was null; skipping");
            return null;
        }
        PublicKey publicKey = parsePublicKey(encodedPublicKey);
        if (publicKey != null) {
            return new VerifierInfo(packageName, publicKey);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to parse verifier public key for ");
        stringBuilder.append(packageName);
        Slog.i(str, stringBuilder.toString());
        return null;
    }

    public static final PublicKey parsePublicKey(String encodedPublicKey) {
        PublicKey publicKey = null;
        String str = TAG;
        if (encodedPublicKey == null) {
            Slog.w(str, "Could not parse null public key");
            return null;
        }
        try {
            EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(encodedPublicKey, (int) null));
            try {
                publicKey = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_RSA).generatePublic(keySpec);
                return publicKey;
            } catch (NoSuchAlgorithmException e) {
                Slog.wtf(str, "Could not parse public key: RSA KeyFactory not included in build");
                try {
                    publicKey = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_EC).generatePublic(keySpec);
                    return publicKey;
                } catch (NoSuchAlgorithmException e2) {
                    Slog.wtf(str, "Could not parse public key: EC KeyFactory not included in build");
                    try {
                        publicKey = KeyFactory.getInstance("DSA").generatePublic(keySpec);
                        return publicKey;
                    } catch (NoSuchAlgorithmException e3) {
                        Slog.wtf(str, "Could not parse public key: DSA KeyFactory not included in build");
                        return publicKey;
                    } catch (InvalidKeySpecException e4) {
                        return publicKey;
                    }
                } catch (InvalidKeySpecException e5) {
                    publicKey = KeyFactory.getInstance("DSA").generatePublic(keySpec);
                    return publicKey;
                }
            } catch (InvalidKeySpecException e6) {
                publicKey = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_EC).generatePublic(keySpec);
                return publicKey;
            }
        } catch (IllegalArgumentException e7) {
            Slog.w(str, "Could not parse verifier public key; invalid Base64");
            return null;
        }
    }

    /* JADX WARNING: Missing block: B:33:0x00b0, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:41:0x00cf, code skipped:
            return false;
     */
    private boolean parseIntent(android.content.res.Resources r19, android.content.res.XmlResourceParser r20, boolean r21, boolean r22, android.content.pm.PackageParser.IntentInfo r23, java.lang.String[] r24) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r18 = this;
        r1 = r19;
        r2 = r20;
        r3 = r23;
        r0 = com.android.internal.R.styleable.AndroidManifestIntentFilter;
        r0 = r1.obtainAttributes(r2, r0);
        r4 = 2;
        r5 = 0;
        r6 = r0.getInt(r4, r5);
        r3.setPriority(r6);
        r7 = 3;
        r8 = r0.getInt(r7, r5);
        r3.setOrder(r8);
        r9 = r0.peekValue(r5);
        if (r9 == 0) goto L_0x002f;
    L_0x0023:
        r10 = r9.resourceId;
        r3.labelRes = r10;
        if (r10 != 0) goto L_0x002f;
    L_0x0029:
        r10 = r9.coerceToString();
        r3.nonLocalizedLabel = r10;
    L_0x002f:
        r10 = sUseRoundIcon;
        r11 = 7;
        if (r10 == 0) goto L_0x0039;
    L_0x0034:
        r10 = r0.getResourceId(r11, r5);
        goto L_0x003a;
    L_0x0039:
        r10 = r5;
        r12 = 1;
        if (r10 == 0) goto L_0x0041;
    L_0x003e:
        r3.icon = r10;
        goto L_0x0047;
    L_0x0041:
        r13 = r0.getResourceId(r12, r5);
        r3.icon = r13;
    L_0x0047:
        r13 = 4;
        r14 = r0.getResourceId(r13, r5);
        r3.logo = r14;
        r14 = 5;
        r15 = r0.getResourceId(r14, r5);
        r3.banner = r15;
        r15 = 6;
        if (r22 == 0) goto L_0x005f;
    L_0x0058:
        r14 = r0.getBoolean(r15, r5);
        r3.setAutoVerify(r14);
    L_0x005f:
        r0.recycle();
        r14 = r20.getDepth();
    L_0x0066:
        r15 = r20.next();
        r16 = r15;
        if (r15 == r12) goto L_0x01cc;
    L_0x006e:
        r15 = r16;
        if (r15 != r7) goto L_0x007d;
    L_0x0072:
        r4 = r20.getDepth();
        if (r4 <= r14) goto L_0x0079;
    L_0x0078:
        goto L_0x007d;
    L_0x0079:
        r12 = r18;
        goto L_0x01d0;
    L_0x007d:
        if (r15 == r7) goto L_0x01c1;
    L_0x007f:
        if (r15 != r13) goto L_0x0086;
    L_0x0081:
        r12 = r18;
        r11 = r7;
        goto L_0x01c4;
    L_0x0086:
        r4 = r20.getName();
        r13 = "action";
        r13 = r4.equals(r13);
        r7 = "";
        r17 = "No value supplied for <android:name>";
        r11 = "name";
        r12 = "http://schemas.android.com/apk/res/android";
        if (r13 == 0) goto L_0x00b1;
    L_0x009a:
        r11 = r2.getAttributeValue(r12, r11);
        if (r11 == 0) goto L_0x00ae;
    L_0x00a0:
        if (r11 != r7) goto L_0x00a3;
    L_0x00a2:
        goto L_0x00ae;
    L_0x00a3:
        com.android.internal.util.XmlUtils.skipCurrentTag(r20);
        r3.addAction(r11);
        r11 = 3;
        r12 = r18;
        goto L_0x01b9;
    L_0x00ae:
        r24[r5] = r17;
        return r5;
    L_0x00b1:
        r13 = "category";
        r13 = r4.equals(r13);
        if (r13 == 0) goto L_0x00d0;
    L_0x00b9:
        r11 = r2.getAttributeValue(r12, r11);
        if (r11 == 0) goto L_0x00cd;
    L_0x00bf:
        if (r11 != r7) goto L_0x00c2;
    L_0x00c1:
        goto L_0x00cd;
    L_0x00c2:
        com.android.internal.util.XmlUtils.skipCurrentTag(r20);
        r3.addCategory(r11);
        r11 = 3;
        r12 = r18;
        goto L_0x01b9;
    L_0x00cd:
        r24[r5] = r17;
        return r5;
    L_0x00d0:
        r7 = "data";
        r7 = r4.equals(r7);
        if (r7 == 0) goto L_0x0183;
    L_0x00d8:
        r7 = com.android.internal.R.styleable.AndroidManifestData;
        r7 = r1.obtainAttributes(r2, r7);
        r11 = r7.getNonConfigurationString(r5, r5);
        if (r11 == 0) goto L_0x00f5;
    L_0x00e4:
        r3.addDataType(r11);	 Catch:{ MalformedMimeTypeException -> 0x00e8 }
        goto L_0x00f5;
    L_0x00e8:
        r0 = move-exception;
        r12 = r0;
        r0 = r12;
        r12 = r0.toString();
        r24[r5] = r12;
        r7.recycle();
        return r5;
    L_0x00f5:
        r0 = 1;
        r11 = r7.getNonConfigurationString(r0, r5);
        if (r11 == 0) goto L_0x00ff;
    L_0x00fc:
        r3.addDataScheme(r11);
    L_0x00ff:
        r12 = 7;
        r0 = r7.getNonConfigurationString(r12, r5);
        if (r0 == 0) goto L_0x0109;
    L_0x0106:
        r3.addDataSchemeSpecificPart(r0, r5);
    L_0x0109:
        r11 = 8;
        r0 = r7.getNonConfigurationString(r11, r5);
        if (r0 == 0) goto L_0x0115;
    L_0x0111:
        r11 = 1;
        r3.addDataSchemeSpecificPart(r0, r11);
    L_0x0115:
        r11 = 9;
        r0 = r7.getNonConfigurationString(r11, r5);
        if (r0 == 0) goto L_0x012a;
    L_0x011d:
        if (r21 != 0) goto L_0x0125;
    L_0x011f:
        r11 = "sspPattern not allowed here; ssp must be literal";
        r24[r5] = r11;
        return r5;
    L_0x0125:
        r11 = 2;
        r3.addDataSchemeSpecificPart(r0, r11);
        goto L_0x012b;
    L_0x012a:
        r11 = 2;
    L_0x012b:
        r13 = r7.getNonConfigurationString(r11, r5);
        r11 = 3;
        r12 = r7.getNonConfigurationString(r11, r5);
        if (r13 == 0) goto L_0x0139;
    L_0x0136:
        r3.addDataAuthority(r13, r12);
    L_0x0139:
        r11 = 4;
        r0 = r7.getNonConfigurationString(r11, r5);
        if (r0 == 0) goto L_0x0143;
    L_0x0140:
        r3.addDataPath(r0, r5);
    L_0x0143:
        r11 = 5;
        r0 = r7.getNonConfigurationString(r11, r5);
        if (r0 == 0) goto L_0x014e;
    L_0x014a:
        r11 = 1;
        r3.addDataPath(r0, r11);
    L_0x014e:
        r11 = 6;
        r0 = r7.getNonConfigurationString(r11, r5);
        if (r0 == 0) goto L_0x0162;
    L_0x0155:
        if (r21 != 0) goto L_0x015d;
    L_0x0157:
        r11 = "pathPattern not allowed here; path must be literal";
        r24[r5] = r11;
        return r5;
    L_0x015d:
        r11 = 2;
        r3.addDataPath(r0, r11);
        goto L_0x0163;
    L_0x0162:
        r11 = 2;
    L_0x0163:
        r11 = 10;
        r0 = r7.getNonConfigurationString(r11, r5);
        if (r0 == 0) goto L_0x0178;
    L_0x016b:
        if (r21 != 0) goto L_0x0173;
    L_0x016d:
        r11 = "pathAdvancedPattern not allowed here; path must be literal";
        r24[r5] = r11;
        return r5;
    L_0x0173:
        r11 = 3;
        r3.addDataPath(r0, r11);
        goto L_0x0179;
    L_0x0178:
        r11 = 3;
    L_0x0179:
        r7.recycle();
        com.android.internal.util.XmlUtils.skipCurrentTag(r20);
        r12 = r18;
        r0 = r7;
        goto L_0x01b9;
    L_0x0183:
        r11 = 3;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r12 = "Unknown element under <intent-filter>: ";
        r7.append(r12);
        r12 = r20.getName();
        r7.append(r12);
        r12 = " at ";
        r7.append(r12);
        r12 = r18;
        r13 = r12.mArchiveSourcePath;
        r7.append(r13);
        r13 = " ";
        r7.append(r13);
        r13 = r20.getPositionDescription();
        r7.append(r13);
        r7 = r7.toString();
        r13 = "PackageParser";
        android.util.Slog.w(r13, r7);
        com.android.internal.util.XmlUtils.skipCurrentTag(r20);
    L_0x01b9:
        r7 = r11;
        r4 = 2;
        r11 = 7;
        r12 = 1;
        r13 = 4;
        r15 = 6;
        goto L_0x0066;
    L_0x01c1:
        r12 = r18;
        r11 = r7;
    L_0x01c4:
        r7 = r11;
        r4 = 2;
        r11 = 7;
        r12 = 1;
        r13 = 4;
        r15 = 6;
        goto L_0x0066;
    L_0x01cc:
        r12 = r18;
        r15 = r16;
    L_0x01d0:
        r4 = "android.intent.category.DEFAULT";
        r4 = r3.hasCategory(r4);
        r3.hasDefault = r4;
        r4 = 1;
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseIntent(android.content.res.Resources, android.content.res.XmlResourceParser, boolean, boolean, android.content.pm.PackageParser$IntentInfo, java.lang.String[]):boolean");
    }

    /* JADX WARNING: Missing block: B:49:0x0064, code skipped:
            return true;
     */
    private static boolean copyNeeded(int r5, android.content.pm.PackageParser.Package r6, android.content.pm.PackageUserState r7, android.os.Bundle r8, int r9) {
        /*
        r0 = 1;
        if (r9 == 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = r7.enabled;
        r2 = 0;
        if (r1 == 0) goto L_0x0017;
    L_0x0009:
        r1 = r7.enabled;
        if (r1 != r0) goto L_0x000f;
    L_0x000d:
        r1 = r0;
        goto L_0x0010;
    L_0x000f:
        r1 = r2;
    L_0x0010:
        r3 = r6.applicationInfo;
        r3 = r3.enabled;
        if (r3 == r1) goto L_0x0017;
    L_0x0016:
        return r0;
    L_0x0017:
        r1 = r6.applicationInfo;
        r1 = r1.flags;
        r3 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r1 = r1 & r3;
        if (r1 == 0) goto L_0x0022;
    L_0x0020:
        r1 = r0;
        goto L_0x0023;
    L_0x0022:
        r1 = r2;
    L_0x0023:
        r3 = r7.suspended;
        if (r3 == r1) goto L_0x0028;
    L_0x0027:
        return r0;
    L_0x0028:
        r3 = r7.installed;
        if (r3 == 0) goto L_0x0064;
    L_0x002c:
        r3 = r7.hidden;
        if (r3 == 0) goto L_0x0031;
    L_0x0030:
        goto L_0x0064;
    L_0x0031:
        r3 = r7.stopped;
        if (r3 == 0) goto L_0x0036;
    L_0x0035:
        return r0;
    L_0x0036:
        r3 = r7.instantApp;
        r4 = r6.applicationInfo;
        r4 = r4.isInstantApp();
        if (r3 == r4) goto L_0x0041;
    L_0x0040:
        return r0;
    L_0x0041:
        r3 = r5 & 128;
        if (r3 == 0) goto L_0x004c;
    L_0x0045:
        if (r8 != 0) goto L_0x004b;
    L_0x0047:
        r3 = r6.mAppMetaData;
        if (r3 == 0) goto L_0x004c;
    L_0x004b:
        return r0;
    L_0x004c:
        r3 = r5 & 1024;
        if (r3 == 0) goto L_0x0055;
    L_0x0050:
        r3 = r6.usesLibraryFiles;
        if (r3 == 0) goto L_0x0055;
    L_0x0054:
        return r0;
    L_0x0055:
        r3 = r5 & 1024;
        if (r3 == 0) goto L_0x005e;
    L_0x0059:
        r3 = r6.usesLibraryInfos;
        if (r3 == 0) goto L_0x005e;
    L_0x005d:
        return r0;
    L_0x005e:
        r3 = r6.staticSharedLibName;
        if (r3 == 0) goto L_0x0063;
    L_0x0062:
        return r0;
    L_0x0063:
        return r2;
    L_0x0064:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.copyNeeded(int, android.content.pm.PackageParser$Package, android.content.pm.PackageUserState, android.os.Bundle, int):boolean");
    }

    @UnsupportedAppUsage
    public static ApplicationInfo generateApplicationInfo(Package p, int flags, PackageUserState state) {
        return generateApplicationInfo(p, flags, state, UserHandle.getCallingUserId());
    }

    private static void updateApplicationInfo(ApplicationInfo ai, int flags, PackageUserState state) {
        if (!sCompatibilityModeEnabled) {
            ai.disableCompatibilityMode();
        }
        if (state.installed) {
            ai.flags |= 8388608;
        } else {
            ai.flags &= -8388609;
        }
        if (state.suspended) {
            ai.flags |= 1073741824;
        } else {
            ai.flags &= -1073741825;
        }
        if (state.instantApp) {
            ai.privateFlags |= 128;
        } else {
            ai.privateFlags &= -129;
        }
        if (state.virtualPreload) {
            ai.privateFlags |= 65536;
        } else {
            ai.privateFlags &= -65537;
        }
        boolean z = true;
        if (state.hidden) {
            ai.privateFlags |= 1;
        } else {
            ai.privateFlags &= -2;
        }
        if (state.enabled == 1) {
            ai.enabled = true;
        } else if (state.enabled == 4) {
            if ((32768 & flags) == 0) {
                z = false;
            }
            ai.enabled = z;
        } else if (state.enabled == 2 || state.enabled == 3) {
            ai.enabled = false;
        }
        ai.enabledSetting = state.enabled;
        if (ai.category == -1) {
            ai.category = state.categoryHint;
        }
        if (ai.category == -1) {
            ai.category = FallbackCategoryProvider.getFallbackCategory(ai.packageName);
        }
        ai.seInfoUser = SELinuxUtil.assignSeinfoUser(state);
        ai.resourceDirs = state.overlayPaths;
        int i = (!sUseRoundIcon || ai.roundIconRes == 0) ? ai.iconRes : ai.roundIconRes;
        ai.icon = i;
    }

    @UnsupportedAppUsage
    public static ApplicationInfo generateApplicationInfo(Package p, int flags, PackageUserState state, int userId) {
        if (p == null || !checkUseInstalledOrHidden(flags, state, p.applicationInfo) || !p.isMatch(flags)) {
            return null;
        }
        if (copyNeeded(flags, p, state, null, userId) || ((32768 & flags) != 0 && state.enabled == 4)) {
            ApplicationInfo ai = new ApplicationInfo(p.applicationInfo);
            ai.initForUser(userId);
            if ((flags & 128) != 0) {
                ai.metaData = p.mAppMetaData;
            }
            if ((flags & 1024) != 0) {
                ai.sharedLibraryFiles = p.usesLibraryFiles;
                ai.sharedLibraryInfos = p.usesLibraryInfos;
            }
            if (state.stopped) {
                ai.flags |= 2097152;
            } else {
                ai.flags &= -2097153;
            }
            updateApplicationInfo(ai, flags, state);
            return ai;
        }
        updateApplicationInfo(p.applicationInfo, flags, state);
        return p.applicationInfo;
    }

    public static ApplicationInfo generateApplicationInfo(ApplicationInfo ai, int flags, PackageUserState state, int userId) {
        if (ai == null || !checkUseInstalledOrHidden(flags, state, ai)) {
            return null;
        }
        ai = new ApplicationInfo(ai);
        ai.initForUser(userId);
        if (state.stopped) {
            ai.flags |= 2097152;
        } else {
            ai.flags &= -2097153;
        }
        updateApplicationInfo(ai, flags, state);
        return ai;
    }

    @UnsupportedAppUsage
    public static final PermissionInfo generatePermissionInfo(Permission p, int flags) {
        if (p == null) {
            return null;
        }
        if ((flags & 128) == 0) {
            return p.info;
        }
        PermissionInfo pi = new PermissionInfo(p.info);
        pi.metaData = p.metaData;
        return pi;
    }

    @UnsupportedAppUsage
    public static final PermissionGroupInfo generatePermissionGroupInfo(PermissionGroup pg, int flags) {
        if (pg == null) {
            return null;
        }
        if ((flags & 128) == 0) {
            return pg.info;
        }
        PermissionGroupInfo pgi = new PermissionGroupInfo(pg.info);
        pgi.metaData = pg.metaData;
        return pgi;
    }

    @UnsupportedAppUsage
    public static final ActivityInfo generateActivityInfo(Activity a, int flags, PackageUserState state, int userId) {
        if (a == null || !checkUseInstalledOrHidden(flags, state, a.owner.applicationInfo)) {
            return null;
        }
        if (copyNeeded(flags, a.owner, state, a.metaData, userId)) {
            ActivityInfo ai = new ActivityInfo(a.info);
            ai.metaData = a.metaData;
            ai.applicationInfo = generateApplicationInfo(a.owner, flags, state, userId);
            return ai;
        }
        updateApplicationInfo(a.info.applicationInfo, flags, state);
        return a.info;
    }

    public static final ActivityInfo generateActivityInfo(ActivityInfo ai, int flags, PackageUserState state, int userId) {
        if (ai == null || !checkUseInstalledOrHidden(flags, state, ai.applicationInfo)) {
            return null;
        }
        ai = new ActivityInfo(ai);
        ai.applicationInfo = generateApplicationInfo(ai.applicationInfo, flags, state, userId);
        return ai;
    }

    @UnsupportedAppUsage
    public static final ServiceInfo generateServiceInfo(Service s, int flags, PackageUserState state, int userId) {
        if (s == null || !checkUseInstalledOrHidden(flags, state, s.owner.applicationInfo)) {
            return null;
        }
        if (copyNeeded(flags, s.owner, state, s.metaData, userId)) {
            ServiceInfo si = new ServiceInfo(s.info);
            si.metaData = s.metaData;
            si.applicationInfo = generateApplicationInfo(s.owner, flags, state, userId);
            return si;
        }
        updateApplicationInfo(s.info.applicationInfo, flags, state);
        return s.info;
    }

    @UnsupportedAppUsage
    public static final ProviderInfo generateProviderInfo(Provider p, int flags, PackageUserState state, int userId) {
        if (p == null || !checkUseInstalledOrHidden(flags, state, p.owner.applicationInfo)) {
            return null;
        }
        if (copyNeeded(flags, p.owner, state, p.metaData, userId) || ((flags & 2048) == 0 && p.info.uriPermissionPatterns != null)) {
            ProviderInfo pi = new ProviderInfo(p.info);
            pi.metaData = p.metaData;
            if ((flags & 2048) == 0) {
                pi.uriPermissionPatterns = null;
            }
            pi.applicationInfo = generateApplicationInfo(p.owner, flags, state, userId);
            return pi;
        }
        updateApplicationInfo(p.info.applicationInfo, flags, state);
        return p.info;
    }

    @UnsupportedAppUsage
    public static final InstrumentationInfo generateInstrumentationInfo(Instrumentation i, int flags) {
        if (i == null) {
            return null;
        }
        if ((flags & 128) == 0) {
            return i.info;
        }
        InstrumentationInfo ii = new InstrumentationInfo(i.info);
        ii.metaData = i.metaData;
        return ii;
    }

    @UnsupportedAppUsage
    public static void setCompatibilityModeEnabled(boolean compatibilityModeEnabled) {
        sCompatibilityModeEnabled = compatibilityModeEnabled;
    }

    public static void readConfigUseRoundIcon(Resources r) {
        if (r != null) {
            sUseRoundIcon = r.getBoolean(R.bool.config_useRoundIcon);
            return;
        }
        try {
            ApplicationInfo androidAppInfo = ActivityThread.getPackageManager().getApplicationInfo("android", 0, UserHandle.myUserId());
            Resources systemResources = Resources.getSystem();
            sUseRoundIcon = ResourcesManager.getInstance().getResources(null, null, null, androidAppInfo.resourceDirs, androidAppInfo.sharedLibraryFiles, 0, null, systemResources.getCompatibilityInfo(), systemResources.getClassLoader()).getBoolean(R.bool.config_useRoundIcon);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static PackageInfo generatePackageInfoFromApex(ApexInfo apexInfo, int flags) throws PackageParserException {
        ApplicationInfo applicationInfo;
        PackageParser pp = new PackageParser();
        File apexFile = new File(apexInfo.packagePath);
        Package p = pp.parsePackage(apexFile, flags, false);
        Package pi = p;
        int i = flags;
        PackageInfo pi2 = generatePackageInfo(pi, EmptyArray.INT, i, 0, 0, Collections.emptySet(), new PackageUserState());
        pi2.applicationInfo.sourceDir = apexFile.getPath();
        pi2.applicationInfo.publicSourceDir = apexFile.getPath();
        if (apexInfo.isFactory) {
            applicationInfo = pi2.applicationInfo;
            applicationInfo.flags |= 1;
        } else {
            applicationInfo = pi2.applicationInfo;
            applicationInfo.flags &= -2;
        }
        if (apexInfo.isActive) {
            applicationInfo = pi2.applicationInfo;
            applicationInfo.flags |= 8388608;
        } else {
            applicationInfo = pi2.applicationInfo;
            applicationInfo.flags &= -8388609;
        }
        pi2.isApex = true;
        if ((134217728 & flags) != 0) {
            collectCertificates(p, apexFile, false);
            if (p.mSigningDetails.hasPastSigningCertificates()) {
                pi2.signatures = new Signature[1];
                pi2.signatures[0] = p.mSigningDetails.pastSigningCertificates[0];
            } else if (p.mSigningDetails.hasSignatures()) {
                int numberOfSigs = p.mSigningDetails.signatures.length;
                pi2.signatures = new Signature[numberOfSigs];
                System.arraycopy(p.mSigningDetails.signatures, 0, pi2.signatures, 0, numberOfSigs);
            }
            if (p.mSigningDetails != SigningDetails.UNKNOWN) {
                pi2.signingInfo = new SigningInfo(p.mSigningDetails);
            } else {
                pi2.signingInfo = null;
            }
        }
        return pi2;
    }
}
