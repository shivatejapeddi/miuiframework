package android.view.inputmethod;

import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Printer;
import java.io.IOException;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

public final class InputMethodInfo implements Parcelable {
    public static final Creator<InputMethodInfo> CREATOR = new Creator<InputMethodInfo>() {
        public InputMethodInfo createFromParcel(Parcel source) {
            return new InputMethodInfo(source);
        }

        public InputMethodInfo[] newArray(int size) {
            return new InputMethodInfo[size];
        }
    };
    static final String TAG = "InputMethodInfo";
    private final boolean mForceDefault;
    final String mId;
    private final boolean mIsAuxIme;
    final int mIsDefaultResId;
    final boolean mIsVrOnly;
    final ResolveInfo mService;
    final String mSettingsActivityName;
    @UnsupportedAppUsage
    private final InputMethodSubtypeArray mSubtypes;
    private final boolean mSupportsSwitchingToNextInputMethod;

    public static String computeId(ResolveInfo service) {
        ServiceInfo si = service.serviceInfo;
        return new ComponentName(si.packageName, si.name).flattenToShortString();
    }

    public InputMethodInfo(Context context, ResolveInfo service) throws XmlPullParserException, IOException {
        this(context, service, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:91:0x021c  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x021c  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x021c  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x021c  */
    public InputMethodInfo(android.content.Context r24, android.content.pm.ResolveInfo r25, java.util.List<android.view.inputmethod.InputMethodSubtype> r26) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r23 = this;
        r1 = r23;
        r2 = r25;
        r3 = r26;
        r23.<init>();
        r1.mService = r2;
        r4 = r2.serviceInfo;
        r0 = computeId(r25);
        r1.mId = r0;
        r5 = 1;
        r6 = 0;
        r0 = 0;
        r1.mForceDefault = r0;
        r7 = r24.getPackageManager();
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r11 = new java.util.ArrayList;
        r11.<init>();
        r12 = "android.view.im";
        r12 = r4.loadXmlMetaData(r7, r12);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, all -> 0x01f3 }
        r10 = r12;
        if (r10 == 0) goto L_0x01e1;
    L_0x002d:
        r12 = r4.applicationInfo;	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, all -> 0x01f3 }
        r12 = r7.getResourcesForApplication(r12);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, all -> 0x01f3 }
        r13 = android.util.Xml.asAttributeSet(r10);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, all -> 0x01f3 }
    L_0x0037:
        r14 = r10.next();	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, all -> 0x01f3 }
        r15 = r14;
        r0 = 1;
        if (r14 == r0) goto L_0x0044;
    L_0x003f:
        r14 = 2;
        if (r15 == r14) goto L_0x0044;
    L_0x0042:
        r0 = 0;
        goto L_0x0037;
    L_0x0044:
        r14 = r10.getName();	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, all -> 0x01f3 }
        r0 = "input-method";
        r0 = r0.equals(r14);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, all -> 0x01f3 }
        if (r0 == 0) goto L_0x01d3;
    L_0x0050:
        r0 = com.android.internal.R.styleable.InputMethod;	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, all -> 0x01f3 }
        r0 = r12.obtainAttributes(r13, r0);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, all -> 0x01f3 }
        r2 = 1;
        r18 = r0.getString(r2);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, all -> 0x01f3 }
        r8 = r18;
        r2 = 3;
        r18 = r5;
        r5 = 0;
        r16 = r0.getBoolean(r2, r5);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, all -> 0x01c9 }
        r19 = r16;
        r16 = r0.getResourceId(r5, r5);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, all -> 0x01c9 }
        r9 = r16;
        r2 = 2;
        r20 = r0.getBoolean(r2, r5);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, all -> 0x01c9 }
        r6 = r20;
        r0.recycle();	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, all -> 0x01c9 }
        r2 = r10.getDepth();	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, all -> 0x01c9 }
        r5 = r18;
    L_0x007d:
        r20 = r0;
        r0 = r10.next();	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01f9, all -> 0x01f3 }
        r15 = r0;
        r18 = r5;
        r5 = 3;
        if (r0 != r5) goto L_0x00a0;
    L_0x0089:
        r0 = r10.getDepth();	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x0099, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x0099, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x0099, all -> 0x0094 }
        if (r0 <= r2) goto L_0x0090;
    L_0x008f:
        goto L_0x00a0;
    L_0x0090:
        r22 = r7;
        goto L_0x0166;
    L_0x0094:
        r0 = move-exception;
        r22 = r7;
        goto L_0x021a;
    L_0x0099:
        r0 = move-exception;
        r22 = r7;
        r5 = r18;
        goto L_0x01fe;
    L_0x00a0:
        r0 = 1;
        if (r15 == r0) goto L_0x0160;
    L_0x00a3:
        r0 = 2;
        if (r15 != r0) goto L_0x014e;
    L_0x00a6:
        r0 = r10.getName();	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, all -> 0x01c9 }
        r14 = r0;
        r0 = "subtype";
        r0 = r0.equals(r14);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, all -> 0x01c9 }
        if (r0 == 0) goto L_0x0140;
    L_0x00b4:
        r0 = com.android.internal.R.styleable.InputMethod_Subtype;	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, all -> 0x01c9 }
        r0 = r12.obtainAttributes(r13, r0);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, all -> 0x01c9 }
        r5 = new android.view.inputmethod.InputMethodSubtype$InputMethodSubtypeBuilder;	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, all -> 0x01c9 }
        r5.<init>();	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01cd, all -> 0x01c9 }
        r21 = r2;
        r22 = r7;
        r2 = 0;
        r7 = r0.getResourceId(r2, r2);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r5 = r5.setSubtypeNameResId(r7);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r17 = r12;
        r7 = 1;
        r12 = r0.getResourceId(r7, r2);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r2 = r5.setSubtypeIconResId(r12);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r5 = 9;
        r5 = r0.getString(r5);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r2 = r2.setLanguageTag(r5);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r5 = 2;
        r12 = r0.getString(r5);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r2 = r2.setSubtypeLocale(r12);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r12 = 3;
        r5 = r0.getString(r12);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r2 = r2.setSubtypeMode(r5);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r5 = 4;
        r5 = r0.getString(r5);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r2 = r2.setSubtypeExtraValue(r5);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r5 = 5;
        r7 = 0;
        r5 = r0.getBoolean(r5, r7);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r2 = r2.setIsAuxiliary(r5);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r5 = 6;
        r5 = r0.getBoolean(r5, r7);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r2 = r2.setOverridesImplicitlyEnabledSubtype(r5);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r5 = 7;
        r5 = r0.getInt(r5, r7);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r2 = r2.setSubtypeId(r5);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r5 = 8;
        r5 = r0.getBoolean(r5, r7);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r2 = r2.setIsAsciiCapable(r5);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r2 = r2.build();	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r5 = r2.isAuxiliary();	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        if (r5 != 0) goto L_0x012e;
    L_0x012c:
        r5 = 0;
        goto L_0x0130;
    L_0x012e:
        r5 = r18;
    L_0x0130:
        r11.add(r2);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x013d, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x013d, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x013d }
        r12 = r17;
        r0 = r20;
        r2 = r21;
        r7 = r22;
        goto L_0x007d;
    L_0x013d:
        r0 = move-exception;
        goto L_0x01fe;
    L_0x0140:
        r21 = r2;
        r22 = r7;
        r17 = r12;
        r0 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r2 = "Meta-data in input-method does not start with subtype tag";
        r0.<init>(r2);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        throw r0;	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
    L_0x014e:
        r21 = r2;
        r22 = r7;
        r17 = r12;
        r7 = 0;
        r12 = 3;
        r12 = r17;
        r5 = r18;
        r0 = r20;
        r7 = r22;
        goto L_0x007d;
    L_0x0160:
        r21 = r2;
        r22 = r7;
        r17 = r12;
    L_0x0166:
        r10.close();
        r0 = r11.size();
        if (r0 != 0) goto L_0x0171;
    L_0x016f:
        r5 = 0;
        goto L_0x0173;
    L_0x0171:
        r5 = r18;
    L_0x0173:
        if (r3 == 0) goto L_0x01b5;
    L_0x0175:
        r0 = r26.size();
        r2 = 0;
    L_0x017a:
        if (r2 >= r0) goto L_0x01b5;
    L_0x017c:
        r7 = r3.get(r2);
        r7 = (android.view.inputmethod.InputMethodSubtype) r7;
        r12 = r11.contains(r7);
        if (r12 != 0) goto L_0x018c;
    L_0x0188:
        r11.add(r7);
        goto L_0x01b2;
    L_0x018c:
        r12 = new java.lang.StringBuilder;
        r12.<init>();
        r13 = "Duplicated subtype definition found: ";
        r12.append(r13);
        r13 = r7.getLocale();
        r12.append(r13);
        r13 = ", ";
        r12.append(r13);
        r13 = r7.getMode();
        r12.append(r13);
        r12 = r12.toString();
        r13 = "InputMethodInfo";
        android.util.Slog.w(r13, r12);
    L_0x01b2:
        r2 = r2 + 1;
        goto L_0x017a;
    L_0x01b5:
        r0 = new android.view.inputmethod.InputMethodSubtypeArray;
        r0.<init>(r11);
        r1.mSubtypes = r0;
        r1.mSettingsActivityName = r8;
        r1.mIsDefaultResId = r9;
        r1.mIsAuxIme = r5;
        r1.mSupportsSwitchingToNextInputMethod = r6;
        r0 = r19;
        r1.mIsVrOnly = r0;
        return;
    L_0x01c9:
        r0 = move-exception;
        r22 = r7;
        goto L_0x021a;
    L_0x01cd:
        r0 = move-exception;
        r22 = r7;
        r5 = r18;
        goto L_0x01fe;
    L_0x01d3:
        r18 = r5;
        r22 = r7;
        r17 = r12;
        r0 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r2 = "Meta-data does not start with input-method tag";
        r0.<init>(r2);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        throw r0;	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
    L_0x01e1:
        r18 = r5;
        r22 = r7;
        r0 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        r2 = "No android.view.im meta-data";
        r0.<init>(r2);	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
        throw r0;	 Catch:{ NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, NameNotFoundException | IndexOutOfBoundsException | NumberFormatException -> 0x01ef, all -> 0x01ed }
    L_0x01ed:
        r0 = move-exception;
        goto L_0x021a;
    L_0x01ef:
        r0 = move-exception;
        r5 = r18;
        goto L_0x01fe;
    L_0x01f3:
        r0 = move-exception;
        r18 = r5;
        r22 = r7;
        goto L_0x021a;
    L_0x01f9:
        r0 = move-exception;
        r18 = r5;
        r22 = r7;
    L_0x01fe:
        r2 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ all -> 0x0217 }
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0217 }
        r7.<init>();	 Catch:{ all -> 0x0217 }
        r12 = "Unable to create context for: ";
        r7.append(r12);	 Catch:{ all -> 0x0217 }
        r12 = r4.packageName;	 Catch:{ all -> 0x0217 }
        r7.append(r12);	 Catch:{ all -> 0x0217 }
        r7 = r7.toString();	 Catch:{ all -> 0x0217 }
        r2.<init>(r7);	 Catch:{ all -> 0x0217 }
        throw r2;	 Catch:{ all -> 0x0217 }
    L_0x0217:
        r0 = move-exception;
        r18 = r5;
    L_0x021a:
        if (r10 == 0) goto L_0x021f;
    L_0x021c:
        r10.close();
    L_0x021f:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodInfo.<init>(android.content.Context, android.content.pm.ResolveInfo, java.util.List):void");
    }

    InputMethodInfo(Parcel source) {
        this.mId = source.readString();
        this.mSettingsActivityName = source.readString();
        this.mIsDefaultResId = source.readInt();
        boolean z = true;
        this.mIsAuxIme = source.readInt() == 1;
        if (source.readInt() != 1) {
            z = false;
        }
        this.mSupportsSwitchingToNextInputMethod = z;
        this.mIsVrOnly = source.readBoolean();
        this.mService = (ResolveInfo) ResolveInfo.CREATOR.createFromParcel(source);
        this.mSubtypes = new InputMethodSubtypeArray(source);
        this.mForceDefault = false;
    }

    public InputMethodInfo(String packageName, String className, CharSequence label, String settingsActivity) {
        this(buildDummyResolveInfo(packageName, className, label), false, settingsActivity, null, 0, false, true, false);
    }

    public InputMethodInfo(ResolveInfo ri, boolean isAuxIme, String settingsActivity, List<InputMethodSubtype> subtypes, int isDefaultResId, boolean forceDefault) {
        this(ri, isAuxIme, settingsActivity, subtypes, isDefaultResId, forceDefault, true, false);
    }

    public InputMethodInfo(ResolveInfo ri, boolean isAuxIme, String settingsActivity, List<InputMethodSubtype> subtypes, int isDefaultResId, boolean forceDefault, boolean supportsSwitchingToNextInputMethod, boolean isVrOnly) {
        ServiceInfo si = ri.serviceInfo;
        this.mService = ri;
        this.mId = new ComponentName(si.packageName, si.name).flattenToShortString();
        this.mSettingsActivityName = settingsActivity;
        this.mIsDefaultResId = isDefaultResId;
        this.mIsAuxIme = isAuxIme;
        this.mSubtypes = new InputMethodSubtypeArray((List) subtypes);
        this.mForceDefault = forceDefault;
        this.mSupportsSwitchingToNextInputMethod = supportsSwitchingToNextInputMethod;
        this.mIsVrOnly = isVrOnly;
    }

    private static ResolveInfo buildDummyResolveInfo(String packageName, String className, CharSequence label) {
        ResolveInfo ri = new ResolveInfo();
        ServiceInfo si = new ServiceInfo();
        ApplicationInfo ai = new ApplicationInfo();
        ai.packageName = packageName;
        ai.enabled = true;
        si.applicationInfo = ai;
        si.enabled = true;
        si.packageName = packageName;
        si.name = className;
        si.exported = true;
        si.nonLocalizedLabel = label;
        ri.serviceInfo = si;
        return ri;
    }

    public String getId() {
        return this.mId;
    }

    public String getPackageName() {
        return this.mService.serviceInfo.packageName;
    }

    public String getServiceName() {
        return this.mService.serviceInfo.name;
    }

    public ServiceInfo getServiceInfo() {
        return this.mService.serviceInfo;
    }

    public ComponentName getComponent() {
        return new ComponentName(this.mService.serviceInfo.packageName, this.mService.serviceInfo.name);
    }

    public CharSequence loadLabel(PackageManager pm) {
        return this.mService.loadLabel(pm);
    }

    public Drawable loadIcon(PackageManager pm) {
        return this.mService.loadIcon(pm);
    }

    public String getSettingsActivity() {
        return this.mSettingsActivityName;
    }

    public boolean isVrOnly() {
        return this.mIsVrOnly;
    }

    public int getSubtypeCount() {
        return this.mSubtypes.getCount();
    }

    public InputMethodSubtype getSubtypeAt(int index) {
        return this.mSubtypes.get(index);
    }

    public int getIsDefaultResourceId() {
        return this.mIsDefaultResId;
    }

    @UnsupportedAppUsage
    public boolean isDefault(Context context) {
        if (this.mForceDefault) {
            return true;
        }
        try {
            if (getIsDefaultResourceId() == 0) {
                return false;
            }
            return context.createPackageContext(getPackageName(), 0).getResources().getBoolean(getIsDefaultResourceId());
        } catch (NameNotFoundException | NotFoundException e) {
            return false;
        }
    }

    public void dump(Printer pw, String prefix) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("mId=");
        stringBuilder.append(this.mId);
        stringBuilder.append(" mSettingsActivityName=");
        stringBuilder.append(this.mSettingsActivityName);
        stringBuilder.append(" mIsVrOnly=");
        stringBuilder.append(this.mIsVrOnly);
        stringBuilder.append(" mSupportsSwitchingToNextInputMethod=");
        stringBuilder.append(this.mSupportsSwitchingToNextInputMethod);
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("mIsDefaultResId=0x");
        stringBuilder.append(Integer.toHexString(this.mIsDefaultResId));
        pw.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("Service:");
        pw.println(stringBuilder.toString());
        ResolveInfo resolveInfo = this.mService;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(prefix);
        stringBuilder2.append("  ");
        resolveInfo.dump(pw, stringBuilder2.toString());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("InputMethodInfo{");
        stringBuilder.append(this.mId);
        stringBuilder.append(", settings: ");
        stringBuilder.append(this.mSettingsActivityName);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof InputMethodInfo)) {
            return false;
        }
        return this.mId.equals(((InputMethodInfo) o).mId);
    }

    public int hashCode() {
        return this.mId.hashCode();
    }

    public boolean isSystem() {
        return (this.mService.serviceInfo.applicationInfo.flags & 1) != 0;
    }

    public boolean isAuxiliaryIme() {
        return this.mIsAuxIme;
    }

    public boolean supportsSwitchingToNextInputMethod() {
        return this.mSupportsSwitchingToNextInputMethod;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mSettingsActivityName);
        dest.writeInt(this.mIsDefaultResId);
        dest.writeInt(this.mIsAuxIme);
        dest.writeInt(this.mSupportsSwitchingToNextInputMethod);
        dest.writeBoolean(this.mIsVrOnly);
        this.mService.writeToParcel(dest, flags);
        this.mSubtypes.writeToParcel(dest);
    }

    public int describeContents() {
        return 0;
    }
}
