package android.media.tv;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.hardware.hdmi.HdmiControlManager;
import android.hardware.hdmi.HdmiDeviceInfo;
import android.hardware.hdmi.HdmiUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.Xml;
import com.android.internal.R;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

public final class TvInputInfo implements Parcelable {
    public static final Creator<TvInputInfo> CREATOR = new Creator<TvInputInfo>() {
        public TvInputInfo createFromParcel(Parcel in) {
            return new TvInputInfo(in, null);
        }

        public TvInputInfo[] newArray(int size) {
            return new TvInputInfo[size];
        }
    };
    private static final boolean DEBUG = false;
    public static final String EXTRA_INPUT_ID = "android.media.tv.extra.INPUT_ID";
    private static final String TAG = "TvInputInfo";
    public static final int TYPE_COMPONENT = 1004;
    public static final int TYPE_COMPOSITE = 1001;
    public static final int TYPE_DISPLAY_PORT = 1008;
    public static final int TYPE_DVI = 1006;
    public static final int TYPE_HDMI = 1007;
    public static final int TYPE_OTHER = 1000;
    public static final int TYPE_SCART = 1003;
    public static final int TYPE_SVIDEO = 1002;
    public static final int TYPE_TUNER = 0;
    public static final int TYPE_VGA = 1005;
    private final boolean mCanRecord;
    private final Bundle mExtras;
    private final int mHdmiConnectionRelativePosition;
    private final HdmiDeviceInfo mHdmiDeviceInfo;
    private final Icon mIcon;
    private final Icon mIconDisconnected;
    private final Icon mIconStandby;
    private Uri mIconUri;
    private final String mId;
    private final boolean mIsConnectedToHdmiSwitch;
    private final boolean mIsHardwareInput;
    private final CharSequence mLabel;
    private final int mLabelResId;
    private final String mParentId;
    private final ResolveInfo mService;
    private final String mSetupActivity;
    private final int mTunerCount;
    private final int mType;

    public static final class Builder {
        private static final String DELIMITER_INFO_IN_ID = "/";
        private static final int LENGTH_HDMI_DEVICE_ID = 2;
        private static final int LENGTH_HDMI_PHYSICAL_ADDRESS = 4;
        private static final String PREFIX_HARDWARE_DEVICE = "HW";
        private static final String PREFIX_HDMI_DEVICE = "HDMI";
        private static final String XML_START_TAG_NAME = "tv-input";
        private static final SparseIntArray sHardwareTypeToTvInputType = new SparseIntArray();
        private Boolean mCanRecord;
        private final Context mContext;
        private Bundle mExtras;
        private HdmiDeviceInfo mHdmiDeviceInfo;
        private Icon mIcon;
        private Icon mIconDisconnected;
        private Icon mIconStandby;
        private CharSequence mLabel;
        private int mLabelResId;
        private String mParentId;
        private final ResolveInfo mResolveInfo;
        private String mSetupActivity;
        private Integer mTunerCount;
        private TvInputHardwareInfo mTvInputHardwareInfo;

        static {
            sHardwareTypeToTvInputType.put(1, 1000);
            sHardwareTypeToTvInputType.put(2, 0);
            sHardwareTypeToTvInputType.put(3, 1001);
            sHardwareTypeToTvInputType.put(4, 1002);
            sHardwareTypeToTvInputType.put(5, 1003);
            sHardwareTypeToTvInputType.put(6, 1004);
            sHardwareTypeToTvInputType.put(7, 1005);
            sHardwareTypeToTvInputType.put(8, 1006);
            sHardwareTypeToTvInputType.put(9, 1007);
            sHardwareTypeToTvInputType.put(10, 1008);
        }

        public Builder(Context context, ComponentName component) {
            if (context != null) {
                this.mResolveInfo = context.getPackageManager().resolveService(new Intent(TvInputService.SERVICE_INTERFACE).setComponent(component), 132);
                if (this.mResolveInfo != null) {
                    this.mContext = context;
                    return;
                }
                throw new IllegalArgumentException("Invalid component. Can't find the service.");
            }
            throw new IllegalArgumentException("context cannot be null.");
        }

        public Builder(Context context, ResolveInfo resolveInfo) {
            if (context == null) {
                throw new IllegalArgumentException("context cannot be null");
            } else if (resolveInfo != null) {
                this.mContext = context;
                this.mResolveInfo = resolveInfo;
            } else {
                throw new IllegalArgumentException("resolveInfo cannot be null");
            }
        }

        @SystemApi
        public Builder setIcon(Icon icon) {
            this.mIcon = icon;
            return this;
        }

        @SystemApi
        public Builder setIcon(Icon icon, int state) {
            if (state == 0) {
                this.mIcon = icon;
            } else if (state == 1) {
                this.mIconStandby = icon;
            } else if (state == 2) {
                this.mIconDisconnected = icon;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown state: ");
                stringBuilder.append(state);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            return this;
        }

        @SystemApi
        public Builder setLabel(CharSequence label) {
            if (this.mLabelResId == 0) {
                this.mLabel = label;
                return this;
            }
            throw new IllegalStateException("Resource ID for label is already set.");
        }

        @SystemApi
        public Builder setLabel(int resId) {
            if (this.mLabel == null) {
                this.mLabelResId = resId;
                return this;
            }
            throw new IllegalStateException("Label text is already set.");
        }

        @SystemApi
        public Builder setHdmiDeviceInfo(HdmiDeviceInfo hdmiDeviceInfo) {
            if (this.mTvInputHardwareInfo != null) {
                Log.w(TvInputInfo.TAG, "TvInputHardwareInfo will not be used to build this TvInputInfo");
                this.mTvInputHardwareInfo = null;
            }
            this.mHdmiDeviceInfo = hdmiDeviceInfo;
            return this;
        }

        @SystemApi
        public Builder setParentId(String parentId) {
            this.mParentId = parentId;
            return this;
        }

        @SystemApi
        public Builder setTvInputHardwareInfo(TvInputHardwareInfo tvInputHardwareInfo) {
            if (this.mHdmiDeviceInfo != null) {
                Log.w(TvInputInfo.TAG, "mHdmiDeviceInfo will not be used to build this TvInputInfo");
                this.mHdmiDeviceInfo = null;
            }
            this.mTvInputHardwareInfo = tvInputHardwareInfo;
            return this;
        }

        public Builder setTunerCount(int tunerCount) {
            this.mTunerCount = Integer.valueOf(tunerCount);
            return this;
        }

        public Builder setCanRecord(boolean canRecord) {
            this.mCanRecord = Boolean.valueOf(canRecord);
            return this;
        }

        public Builder setExtras(Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        public TvInputInfo build() {
            String id;
            int type;
            ComponentName componentName = new ComponentName(this.mResolveInfo.serviceInfo.packageName, this.mResolveInfo.serviceInfo.name);
            boolean isHardwareInput = false;
            boolean isConnectedToHdmiSwitch = false;
            int hdmiConnectionRelativePosition = 0;
            HdmiDeviceInfo id2 = this.mHdmiDeviceInfo;
            int i = 0;
            if (id2 != null) {
                id = generateInputId(componentName, id2);
                type = 1007;
                isHardwareInput = true;
                hdmiConnectionRelativePosition = getRelativePosition(this.mContext, this.mHdmiDeviceInfo);
                boolean z = true;
                if (hdmiConnectionRelativePosition == 1) {
                    z = false;
                }
                isConnectedToHdmiSwitch = z;
            } else {
                TvInputHardwareInfo tvInputHardwareInfo = this.mTvInputHardwareInfo;
                if (tvInputHardwareInfo != null) {
                    id = generateInputId(componentName, tvInputHardwareInfo);
                    type = sHardwareTypeToTvInputType.get(this.mTvInputHardwareInfo.getType(), 0);
                    isHardwareInput = true;
                } else {
                    id = generateInputId(componentName);
                    type = 0;
                }
            }
            parseServiceMetadata(type);
            ResolveInfo resolveInfo = this.mResolveInfo;
            CharSequence charSequence = this.mLabel;
            int i2 = this.mLabelResId;
            Icon icon = this.mIcon;
            Icon icon2 = this.mIconStandby;
            Icon icon3 = this.mIconDisconnected;
            String str = this.mSetupActivity;
            Boolean bool = this.mCanRecord;
            boolean booleanValue = bool == null ? false : bool.booleanValue();
            Integer num = this.mTunerCount;
            if (num != null) {
                i = num.intValue();
            }
            return new TvInputInfo(resolveInfo, id, type, isHardwareInput, charSequence, i2, icon, icon2, icon3, str, booleanValue, i, this.mHdmiDeviceInfo, isConnectedToHdmiSwitch, hdmiConnectionRelativePosition, this.mParentId, this.mExtras, null);
        }

        private static String generateInputId(ComponentName name) {
            return name.flattenToShortString();
        }

        private static String generateInputId(ComponentName name, HdmiDeviceInfo hdmiDeviceInfo) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(name.flattenToShortString());
            stringBuilder.append(String.format(Locale.ENGLISH, "/HDMI%04X%02X", new Object[]{Integer.valueOf(hdmiDeviceInfo.getPhysicalAddress()), Integer.valueOf(hdmiDeviceInfo.getId())}));
            return stringBuilder.toString();
        }

        private static String generateInputId(ComponentName name, TvInputHardwareInfo tvInputHardwareInfo) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(name.flattenToShortString());
            stringBuilder.append(DELIMITER_INFO_IN_ID);
            stringBuilder.append(PREFIX_HARDWARE_DEVICE);
            stringBuilder.append(tvInputHardwareInfo.getDeviceId());
            return stringBuilder.toString();
        }

        private static int getRelativePosition(Context context, HdmiDeviceInfo info) {
            HdmiControlManager hcm = (HdmiControlManager) context.getSystemService(Context.HDMI_CONTROL_SERVICE);
            if (hcm == null) {
                return 0;
            }
            return HdmiUtils.getHdmiAddressRelativePosition(info.getPhysicalAddress(), hcm.getPhysicalAddress());
        }

        private void parseServiceMetadata(int inputType) {
            ServiceInfo si = this.mResolveInfo.serviceInfo;
            PackageManager pm = this.mContext.getPackageManager();
            StringBuilder stringBuilder;
            try {
                XmlResourceParser parser = si.loadXmlMetaData(pm, TvInputService.SERVICE_META_DATA);
                if (parser != null) {
                    try {
                        Resources res = pm.getResourcesForApplication(si.applicationInfo);
                        AttributeSet attrs = Xml.asAttributeSet(parser);
                        while (true) {
                            int next = parser.next();
                            int type = next;
                            if (next == 1 || type == 2) {
                            }
                        }
                        if (XML_START_TAG_NAME.equals(parser.getName())) {
                            TypedArray sa = res.obtainAttributes(attrs, R.styleable.TvInputService);
                            this.mSetupActivity = sa.getString(1);
                            if (this.mCanRecord == null) {
                                this.mCanRecord = Boolean.valueOf(sa.getBoolean(2, false));
                            }
                            if (this.mTunerCount == null && inputType == 0) {
                                this.mTunerCount = Integer.valueOf(sa.getInt(3, 1));
                            }
                            sa.recycle();
                            parser.close();
                            return;
                        }
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Meta-data does not start with tv-input tag for ");
                        stringBuilder2.append(si.name);
                        throw new IllegalStateException(stringBuilder2.toString());
                    } catch (Throwable th) {
                        if (parser != null) {
                            parser.close();
                        }
                    }
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("No android.media.tv.input meta-data found for ");
                    stringBuilder.append(si.name);
                    throw new IllegalStateException(stringBuilder.toString());
                }
            } catch (IOException | XmlPullParserException e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed reading meta-data for ");
                stringBuilder.append(si.packageName);
                throw new IllegalStateException(stringBuilder.toString(), e);
            } catch (NameNotFoundException e2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("No resources found for ");
                stringBuilder.append(si.packageName);
                throw new IllegalStateException(stringBuilder.toString(), e2);
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        }
    }

    @SystemApi
    public static final class TvInputSettings {
        private static final String CUSTOM_NAME_SEPARATOR = ",";
        private static final String TV_INPUT_SEPARATOR = ":";

        private TvInputSettings() {
        }

        private static boolean isHidden(Context context, String inputId, int userId) {
            return getHiddenTvInputIds(context, userId).contains(inputId);
        }

        private static String getCustomLabel(Context context, String inputId, int userId) {
            return (String) getCustomLabels(context, userId).get(inputId);
        }

        @SystemApi
        public static Set<String> getHiddenTvInputIds(Context context, int userId) {
            String hiddenIdsString = Secure.getStringForUser(context.getContentResolver(), Secure.TV_INPUT_HIDDEN_INPUTS, userId);
            Set<String> set = new HashSet();
            if (TextUtils.isEmpty(hiddenIdsString)) {
                return set;
            }
            for (String id : hiddenIdsString.split(":")) {
                set.add(Uri.decode(id));
            }
            return set;
        }

        @SystemApi
        public static Map<String, String> getCustomLabels(Context context, int userId) {
            String labelsString = Secure.getStringForUser(context.getContentResolver(), Secure.TV_INPUT_CUSTOM_LABELS, userId);
            Map<String, String> map = new HashMap();
            if (TextUtils.isEmpty(labelsString)) {
                return map;
            }
            for (String pairString : labelsString.split(":")) {
                String[] pair = pairString.split(",");
                map.put(Uri.decode(pair[0]), Uri.decode(pair[1]));
            }
            return map;
        }

        @SystemApi
        public static void putHiddenTvInputs(Context context, Set<String> hiddenInputIds, int userId) {
            StringBuilder builder = new StringBuilder();
            boolean firstItem = true;
            for (String inputId : hiddenInputIds) {
                ensureValidField(inputId);
                if (firstItem) {
                    firstItem = false;
                } else {
                    builder.append(":");
                }
                builder.append(Uri.encode(inputId));
            }
            Secure.putStringForUser(context.getContentResolver(), Secure.TV_INPUT_HIDDEN_INPUTS, builder.toString(), userId);
            TvInputManager tm = (TvInputManager) context.getSystemService(Context.TV_INPUT_SERVICE);
            for (String inputId2 : hiddenInputIds) {
                TvInputInfo info = tm.getTvInputInfo(inputId2);
                if (info != null) {
                    tm.updateTvInputInfo(info);
                }
            }
        }

        @SystemApi
        public static void putCustomLabels(Context context, Map<String, String> customLabels, int userId) {
            StringBuilder builder = new StringBuilder();
            boolean firstItem = true;
            for (Entry<String, String> entry : customLabels.entrySet()) {
                ensureValidField((String) entry.getKey());
                ensureValidField((String) entry.getValue());
                if (firstItem) {
                    firstItem = false;
                } else {
                    builder.append(":");
                }
                builder.append(Uri.encode((String) entry.getKey()));
                builder.append(",");
                builder.append(Uri.encode((String) entry.getValue()));
            }
            Secure.putStringForUser(context.getContentResolver(), Secure.TV_INPUT_CUSTOM_LABELS, builder.toString(), userId);
            TvInputManager tm = (TvInputManager) context.getSystemService(Context.TV_INPUT_SERVICE);
            for (String inputId : customLabels.keySet()) {
                TvInputInfo info = tm.getTvInputInfo(inputId);
                if (info != null) {
                    tm.updateTvInputInfo(info);
                }
            }
        }

        private static void ensureValidField(String value) {
            if (TextUtils.isEmpty(value)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(value);
                stringBuilder.append(" should not empty ");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    /* synthetic */ TvInputInfo(ResolveInfo x0, String x1, int x2, boolean x3, CharSequence x4, int x5, Icon x6, Icon x7, Icon x8, String x9, boolean x10, int x11, HdmiDeviceInfo x12, boolean x13, int x14, String x15, Bundle x16, AnonymousClass1 x17) {
        this(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16);
    }

    /* synthetic */ TvInputInfo(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    @SystemApi
    @Deprecated
    public static TvInputInfo createTvInputInfo(Context context, ResolveInfo service, HdmiDeviceInfo hdmiDeviceInfo, String parentId, String label, Uri iconUri) throws XmlPullParserException, IOException {
        TvInputInfo info = new Builder(context, service).setHdmiDeviceInfo(hdmiDeviceInfo).setParentId(parentId).setLabel((CharSequence) label).build();
        info.mIconUri = iconUri;
        return info;
    }

    @SystemApi
    @Deprecated
    public static TvInputInfo createTvInputInfo(Context context, ResolveInfo service, HdmiDeviceInfo hdmiDeviceInfo, String parentId, int labelRes, Icon icon) throws XmlPullParserException, IOException {
        return new Builder(context, service).setHdmiDeviceInfo(hdmiDeviceInfo).setParentId(parentId).setLabel(labelRes).setIcon(icon).build();
    }

    @SystemApi
    @Deprecated
    public static TvInputInfo createTvInputInfo(Context context, ResolveInfo service, TvInputHardwareInfo hardwareInfo, String label, Uri iconUri) throws XmlPullParserException, IOException {
        TvInputInfo info = new Builder(context, service).setTvInputHardwareInfo(hardwareInfo).setLabel((CharSequence) label).build();
        info.mIconUri = iconUri;
        return info;
    }

    @SystemApi
    @Deprecated
    public static TvInputInfo createTvInputInfo(Context context, ResolveInfo service, TvInputHardwareInfo hardwareInfo, int labelRes, Icon icon) throws XmlPullParserException, IOException {
        return new Builder(context, service).setTvInputHardwareInfo(hardwareInfo).setLabel(labelRes).setIcon(icon).build();
    }

    private TvInputInfo(ResolveInfo service, String id, int type, boolean isHardwareInput, CharSequence label, int labelResId, Icon icon, Icon iconStandby, Icon iconDisconnected, String setupActivity, boolean canRecord, int tunerCount, HdmiDeviceInfo hdmiDeviceInfo, boolean isConnectedToHdmiSwitch, int hdmiConnectionRelativePosition, String parentId, Bundle extras) {
        this.mService = service;
        this.mId = id;
        this.mType = type;
        this.mIsHardwareInput = isHardwareInput;
        this.mLabel = label;
        this.mLabelResId = labelResId;
        this.mIcon = icon;
        this.mIconStandby = iconStandby;
        this.mIconDisconnected = iconDisconnected;
        this.mSetupActivity = setupActivity;
        this.mCanRecord = canRecord;
        this.mTunerCount = tunerCount;
        this.mHdmiDeviceInfo = hdmiDeviceInfo;
        this.mIsConnectedToHdmiSwitch = isConnectedToHdmiSwitch;
        this.mHdmiConnectionRelativePosition = hdmiConnectionRelativePosition;
        this.mParentId = parentId;
        this.mExtras = extras;
    }

    public String getId() {
        return this.mId;
    }

    public String getParentId() {
        return this.mParentId;
    }

    public ServiceInfo getServiceInfo() {
        return this.mService.serviceInfo;
    }

    @UnsupportedAppUsage
    public ComponentName getComponent() {
        return new ComponentName(this.mService.serviceInfo.packageName, this.mService.serviceInfo.name);
    }

    public Intent createSetupIntent() {
        if (TextUtils.isEmpty(this.mSetupActivity)) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName(this.mService.serviceInfo.packageName, this.mSetupActivity);
        intent.putExtra(EXTRA_INPUT_ID, getId());
        return intent;
    }

    @Deprecated
    public Intent createSettingsIntent() {
        return null;
    }

    public int getType() {
        return this.mType;
    }

    public int getTunerCount() {
        return this.mTunerCount;
    }

    public boolean canRecord() {
        return this.mCanRecord;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    @SystemApi
    public HdmiDeviceInfo getHdmiDeviceInfo() {
        if (this.mType == 1007) {
            return this.mHdmiDeviceInfo;
        }
        return null;
    }

    public boolean isPassthroughInput() {
        return this.mType != 0;
    }

    @SystemApi
    public boolean isHardwareInput() {
        return this.mIsHardwareInput;
    }

    @SystemApi
    public boolean isConnectedToHdmiSwitch() {
        return this.mIsConnectedToHdmiSwitch;
    }

    public int getHdmiConnectionRelativePosition() {
        return this.mHdmiConnectionRelativePosition;
    }

    public boolean isHidden(Context context) {
        return TvInputSettings.isHidden(context, this.mId, UserHandle.myUserId());
    }

    public CharSequence loadLabel(Context context) {
        if (this.mLabelResId != 0) {
            return context.getPackageManager().getText(this.mService.serviceInfo.packageName, this.mLabelResId, null);
        }
        if (TextUtils.isEmpty(this.mLabel)) {
            return this.mService.loadLabel(context.getPackageManager());
        }
        return this.mLabel;
    }

    public CharSequence loadCustomLabel(Context context) {
        return TvInputSettings.getCustomLabel(context, this.mId, UserHandle.myUserId());
    }

    /* JADX WARNING: Missing block: B:22:0x002e, code skipped:
            if (r0 != null) goto L_0x0030;
     */
    /* JADX WARNING: Missing block: B:24:?, code skipped:
            $closeResource(r1, r0);
     */
    public android.graphics.drawable.Drawable loadIcon(android.content.Context r4) {
        /*
        r3 = this;
        r0 = r3.mIcon;
        if (r0 == 0) goto L_0x0009;
    L_0x0004:
        r0 = r0.loadDrawable(r4);
        return r0;
    L_0x0009:
        r0 = r3.mIconUri;
        if (r0 == 0) goto L_0x004d;
    L_0x000d:
        r0 = r4.getContentResolver();	 Catch:{ IOException -> 0x0034 }
        r1 = r3.mIconUri;	 Catch:{ IOException -> 0x0034 }
        r0 = r0.openInputStream(r1);	 Catch:{ IOException -> 0x0034 }
        r1 = 0;
        r2 = android.graphics.drawable.Drawable.createFromStream(r0, r1);	 Catch:{ all -> 0x002b }
        if (r2 == 0) goto L_0x0025;
        if (r0 == 0) goto L_0x0024;
    L_0x0021:
        $closeResource(r1, r0);	 Catch:{ IOException -> 0x0034 }
    L_0x0024:
        return r2;
    L_0x0025:
        if (r0 == 0) goto L_0x002a;
    L_0x0027:
        $closeResource(r1, r0);	 Catch:{ IOException -> 0x0034 }
    L_0x002a:
        goto L_0x004d;
    L_0x002b:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x002d }
    L_0x002d:
        r2 = move-exception;
        if (r0 == 0) goto L_0x0033;
    L_0x0030:
        $closeResource(r1, r0);	 Catch:{ IOException -> 0x0034 }
    L_0x0033:
        throw r2;	 Catch:{ IOException -> 0x0034 }
    L_0x0034:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Loading the default icon due to a failure on loading ";
        r1.append(r2);
        r2 = r3.mIconUri;
        r1.append(r2);
        r1 = r1.toString();
        r2 = "TvInputInfo";
        android.util.Log.w(r2, r1, r0);
    L_0x004d:
        r0 = r3.loadServiceIcon(r4);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.tv.TvInputInfo.loadIcon(android.content.Context):android.graphics.drawable.Drawable");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
                return;
            } catch (Throwable th) {
                x0.addSuppressed(th);
                return;
            }
        }
        x1.close();
    }

    @SystemApi
    public Drawable loadIcon(Context context, int state) {
        if (state == 0) {
            return loadIcon(context);
        }
        Icon icon;
        if (state == 1) {
            icon = this.mIconStandby;
            if (icon != null) {
                return icon.loadDrawable(context);
            }
        } else if (state == 2) {
            icon = this.mIconDisconnected;
            if (icon != null) {
                return icon.loadDrawable(context);
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown state: ");
            stringBuilder.append(state);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        return null;
    }

    public int describeContents() {
        return 0;
    }

    public int hashCode() {
        return this.mId.hashCode();
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof TvInputInfo)) {
            return false;
        }
        TvInputInfo obj = (TvInputInfo) o;
        if (!(Objects.equals(this.mService, obj.mService) && TextUtils.equals(this.mId, obj.mId) && this.mType == obj.mType && this.mIsHardwareInput == obj.mIsHardwareInput && TextUtils.equals(this.mLabel, obj.mLabel) && Objects.equals(this.mIconUri, obj.mIconUri) && this.mLabelResId == obj.mLabelResId && Objects.equals(this.mIcon, obj.mIcon) && Objects.equals(this.mIconStandby, obj.mIconStandby) && Objects.equals(this.mIconDisconnected, obj.mIconDisconnected) && TextUtils.equals(this.mSetupActivity, obj.mSetupActivity) && this.mCanRecord == obj.mCanRecord && this.mTunerCount == obj.mTunerCount && Objects.equals(this.mHdmiDeviceInfo, obj.mHdmiDeviceInfo) && this.mIsConnectedToHdmiSwitch == obj.mIsConnectedToHdmiSwitch && this.mHdmiConnectionRelativePosition == obj.mHdmiConnectionRelativePosition && TextUtils.equals(this.mParentId, obj.mParentId) && Objects.equals(this.mExtras, obj.mExtras))) {
            z = false;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TvInputInfo{id=");
        stringBuilder.append(this.mId);
        stringBuilder.append(", pkg=");
        stringBuilder.append(this.mService.serviceInfo.packageName);
        stringBuilder.append(", service=");
        stringBuilder.append(this.mService.serviceInfo.name);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        this.mService.writeToParcel(dest, flags);
        dest.writeString(this.mId);
        dest.writeInt(this.mType);
        dest.writeByte(this.mIsHardwareInput);
        TextUtils.writeToParcel(this.mLabel, dest, flags);
        dest.writeParcelable(this.mIconUri, flags);
        dest.writeInt(this.mLabelResId);
        dest.writeParcelable(this.mIcon, flags);
        dest.writeParcelable(this.mIconStandby, flags);
        dest.writeParcelable(this.mIconDisconnected, flags);
        dest.writeString(this.mSetupActivity);
        dest.writeByte(this.mCanRecord);
        dest.writeInt(this.mTunerCount);
        dest.writeParcelable(this.mHdmiDeviceInfo, flags);
        dest.writeByte(this.mIsConnectedToHdmiSwitch);
        dest.writeInt(this.mHdmiConnectionRelativePosition);
        dest.writeString(this.mParentId);
        dest.writeBundle(this.mExtras);
    }

    private Drawable loadServiceIcon(Context context) {
        if (this.mService.serviceInfo.icon == 0 && this.mService.serviceInfo.applicationInfo.icon == 0) {
            return null;
        }
        return this.mService.serviceInfo.loadIcon(context.getPackageManager());
    }

    private TvInputInfo(Parcel in) {
        this.mService = (ResolveInfo) ResolveInfo.CREATOR.createFromParcel(in);
        this.mId = in.readString();
        this.mType = in.readInt();
        boolean z = false;
        this.mIsHardwareInput = in.readByte() == (byte) 1;
        this.mLabel = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.mIconUri = (Uri) in.readParcelable(null);
        this.mLabelResId = in.readInt();
        this.mIcon = (Icon) in.readParcelable(null);
        this.mIconStandby = (Icon) in.readParcelable(null);
        this.mIconDisconnected = (Icon) in.readParcelable(null);
        this.mSetupActivity = in.readString();
        this.mCanRecord = in.readByte() == (byte) 1;
        this.mTunerCount = in.readInt();
        this.mHdmiDeviceInfo = (HdmiDeviceInfo) in.readParcelable(null);
        if (in.readByte() == (byte) 1) {
            z = true;
        }
        this.mIsConnectedToHdmiSwitch = z;
        this.mHdmiConnectionRelativePosition = in.readInt();
        this.mParentId = in.readString();
        this.mExtras = in.readBundle();
    }
}
