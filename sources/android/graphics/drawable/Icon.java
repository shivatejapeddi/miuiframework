package android.graphics.drawable;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BlendMode;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.IccCardConstants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;

public final class Icon implements Parcelable {
    public static final Creator<Icon> CREATOR = new Creator<Icon>() {
        public Icon createFromParcel(Parcel in) {
            return new Icon(in, null);
        }

        public Icon[] newArray(int size) {
            return new Icon[size];
        }
    };
    static final BlendMode DEFAULT_BLEND_MODE = Drawable.DEFAULT_BLEND_MODE;
    public static final int MIN_ASHMEM_ICON_SIZE = 131072;
    private static final String TAG = "Icon";
    public static final int TYPE_ADAPTIVE_BITMAP = 5;
    public static final int TYPE_BITMAP = 1;
    public static final int TYPE_DATA = 3;
    public static final int TYPE_RESOURCE = 2;
    public static final int TYPE_URI = 4;
    private static final int VERSION_STREAM_SERIALIZER = 1;
    private BlendMode mBlendMode;
    private int mInt1;
    private int mInt2;
    private Object mObj1;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private String mString1;
    private ColorStateList mTintList;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final int mType;

    public @interface IconType {
    }

    private class LoadDrawableTask implements Runnable {
        final Context mContext;
        final Message mMessage;

        public LoadDrawableTask(Context context, Handler handler, final OnDrawableLoadedListener listener) {
            this.mContext = context;
            this.mMessage = Message.obtain(handler, new Runnable(Icon.this) {
                public void run() {
                    listener.onDrawableLoaded((Drawable) LoadDrawableTask.this.mMessage.obj);
                }
            });
        }

        public LoadDrawableTask(Context context, Message message) {
            this.mContext = context;
            this.mMessage = message;
        }

        public void run() {
            this.mMessage.obj = Icon.this.loadDrawable(this.mContext);
            this.mMessage.sendToTarget();
        }

        public void runAsync() {
            AsyncTask.THREAD_POOL_EXECUTOR.execute(this);
        }
    }

    public interface OnDrawableLoadedListener {
        void onDrawableLoaded(Drawable drawable);
    }

    /* synthetic */ Icon(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    @IconType
    public int getType() {
        return this.mType;
    }

    @UnsupportedAppUsage
    public Bitmap getBitmap() {
        int i = this.mType;
        if (i == 1 || i == 5) {
            return (Bitmap) this.mObj1;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("called getBitmap() on ");
        stringBuilder.append(this);
        throw new IllegalStateException(stringBuilder.toString());
    }

    private void setBitmap(Bitmap b) {
        this.mObj1 = b;
    }

    @UnsupportedAppUsage
    public int getDataLength() {
        if (this.mType == 3) {
            int i;
            synchronized (this) {
                i = this.mInt1;
            }
            return i;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("called getDataLength() on ");
        stringBuilder.append(this);
        throw new IllegalStateException(stringBuilder.toString());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public int getDataOffset() {
        if (this.mType == 3) {
            int i;
            synchronized (this) {
                i = this.mInt2;
            }
            return i;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("called getDataOffset() on ");
        stringBuilder.append(this);
        throw new IllegalStateException(stringBuilder.toString());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public byte[] getDataBytes() {
        if (this.mType == 3) {
            byte[] bArr;
            synchronized (this) {
                bArr = (byte[]) this.mObj1;
            }
            return bArr;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("called getDataBytes() on ");
        stringBuilder.append(this);
        throw new IllegalStateException(stringBuilder.toString());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public Resources getResources() {
        if (this.mType == 2) {
            return (Resources) this.mObj1;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("called getResources() on ");
        stringBuilder.append(this);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public String getResPackage() {
        if (this.mType == 2) {
            return this.mString1;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("called getResPackage() on ");
        stringBuilder.append(this);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public int getResId() {
        if (this.mType == 2) {
            return this.mInt1;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("called getResId() on ");
        stringBuilder.append(this);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public String getUriString() {
        if (this.mType == 4) {
            return this.mString1;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("called getUriString() on ");
        stringBuilder.append(this);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public Uri getUri() {
        return Uri.parse(getUriString());
    }

    private static final String typeToString(int x) {
        if (x == 1) {
            return "BITMAP";
        }
        if (x == 2) {
            return "RESOURCE";
        }
        if (x == 3) {
            return "DATA";
        }
        if (x == 4) {
            return "URI";
        }
        if (x != 5) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        return "BITMAP_MASKABLE";
    }

    public void loadDrawableAsync(Context context, Message andThen) {
        if (andThen.getTarget() != null) {
            new LoadDrawableTask(context, andThen).runAsync();
            return;
        }
        throw new IllegalArgumentException("callback message must have a target handler");
    }

    public void loadDrawableAsync(Context context, OnDrawableLoadedListener listener, Handler handler) {
        new LoadDrawableTask(context, handler, listener).runAsync();
    }

    public Drawable loadDrawable(Context context) {
        Drawable result = loadDrawableInner(context);
        if (!(result == null || (this.mTintList == null && this.mBlendMode == DEFAULT_BLEND_MODE))) {
            result.mutate();
            result.setTintList(this.mTintList);
            result.setTintBlendMode(this.mBlendMode);
        }
        return result;
    }

    private Drawable loadDrawableInner(Context context) {
        StringBuilder stringBuilder;
        int i = this.mType;
        if (i == 1) {
            return new BitmapDrawable(context.getResources(), getBitmap());
        }
        String str = TAG;
        if (i == 2) {
            if (getResources() == null) {
                String resPackage = getResPackage();
                if (TextUtils.isEmpty(resPackage)) {
                    resPackage = context.getPackageName();
                }
                if ("android".equals(resPackage)) {
                    this.mObj1 = Resources.getSystem();
                } else {
                    PackageManager pm = context.getPackageManager();
                    try {
                        ApplicationInfo ai = pm.getApplicationInfo(resPackage, 8192);
                        if (ai != null) {
                            this.mObj1 = pm.getResourcesForApplication(ai);
                        }
                    } catch (NameNotFoundException e) {
                        Log.e(str, String.format("Unable to find pkg=%s for icon %s", new Object[]{resPackage, this}), e);
                    }
                }
            }
            try {
                return getResources().getDrawable(getResId(), context.getTheme());
            } catch (RuntimeException e2) {
                Log.e(str, String.format("Unable to load resource 0x%08x from pkg=%s", new Object[]{Integer.valueOf(getResId()), getResPackage()}), e2);
            }
        } else if (i == 3) {
            return new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray(getDataBytes(), getDataOffset(), getDataLength()));
        } else {
            if (i == 4) {
                Uri uri = getUri();
                String scheme = uri.getScheme();
                InputStream is = null;
                if ("content".equals(scheme) || ContentResolver.SCHEME_FILE.equals(scheme)) {
                    try {
                        str = context.getContentResolver().openInputStream(uri);
                        is = str;
                    } catch (Exception e3) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unable to load image from URI: ");
                        stringBuilder.append(uri);
                        Log.w(str, stringBuilder.toString(), e3);
                    }
                } else {
                    try {
                        is = new FileInputStream(new File(this.mString1));
                    } catch (FileNotFoundException e4) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unable to load image from path: ");
                        stringBuilder.append(uri);
                        Log.w(str, stringBuilder.toString(), e4);
                    }
                }
                if (is != null) {
                    return new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(is));
                }
            } else if (i == 5) {
                return new AdaptiveIconDrawable(null, new BitmapDrawable(context.getResources(), getBitmap()));
            }
        }
        return null;
    }

    public Drawable loadDrawableAsUser(Context context, int userId) {
        if (this.mType == 2) {
            String resPackage = getResPackage();
            if (TextUtils.isEmpty(resPackage)) {
                resPackage = context.getPackageName();
            }
            if (getResources() == null && !getResPackage().equals("android")) {
                try {
                    this.mObj1 = context.getPackageManager().getResourcesForApplicationAsUser(resPackage, userId);
                } catch (NameNotFoundException e) {
                    Log.e(TAG, String.format("Unable to find pkg=%s user=%d", new Object[]{getResPackage(), Integer.valueOf(userId)}), e);
                }
            }
        }
        return loadDrawable(context);
    }

    public void convertToAshmem() {
        int i = this.mType;
        if ((i == 1 || i == 5) && getBitmap().isMutable() && getBitmap().getAllocationByteCount() >= 131072) {
            setBitmap(getBitmap().createAshmemBitmap());
        }
    }

    public void writeToStream(OutputStream stream) throws IOException {
        DataOutputStream dataStream = new DataOutputStream(stream);
        dataStream.writeInt(1);
        dataStream.writeByte(this.mType);
        int i = this.mType;
        if (i != 1) {
            if (i == 2) {
                dataStream.writeUTF(getResPackage());
                dataStream.writeInt(getResId());
                return;
            } else if (i == 3) {
                dataStream.writeInt(getDataLength());
                dataStream.write(getDataBytes(), getDataOffset(), getDataLength());
                return;
            } else if (i == 4) {
                dataStream.writeUTF(getUriString());
                return;
            } else if (i != 5) {
                return;
            }
        }
        getBitmap().compress(CompressFormat.PNG, 100, dataStream);
    }

    private Icon(int mType) {
        this.mBlendMode = Drawable.DEFAULT_BLEND_MODE;
        this.mType = mType;
    }

    public static Icon createFromStream(InputStream stream) throws IOException {
        DataInputStream inputStream = new DataInputStream(stream);
        if (inputStream.readInt() >= 1) {
            int type = inputStream.readByte();
            if (type == 1) {
                return createWithBitmap(BitmapFactory.decodeStream(inputStream));
            }
            if (type == 2) {
                return createWithResource(inputStream.readUTF(), inputStream.readInt());
            }
            if (type == 3) {
                int length = inputStream.readInt();
                byte[] data = new byte[length];
                inputStream.read(data, 0, length);
                return createWithData(data, 0, length);
            } else if (type == 4) {
                return createWithContentUri(inputStream.readUTF());
            } else {
                if (type == 5) {
                    return createWithAdaptiveBitmap(BitmapFactory.decodeStream(inputStream));
                }
            }
        }
        return null;
    }

    public boolean sameAs(Icon otherIcon) {
        boolean z = true;
        if (otherIcon == this) {
            return true;
        }
        if (this.mType != otherIcon.getType()) {
            return false;
        }
        int i = this.mType;
        if (i != 1) {
            if (i == 2) {
                if (!(getResId() == otherIcon.getResId() && Objects.equals(getResPackage(), otherIcon.getResPackage()))) {
                    z = false;
                }
                return z;
            } else if (i == 3) {
                if (!(getDataLength() == otherIcon.getDataLength() && getDataOffset() == otherIcon.getDataOffset() && Arrays.equals(getDataBytes(), otherIcon.getDataBytes()))) {
                    z = false;
                }
                return z;
            } else if (i == 4) {
                return Objects.equals(getUriString(), otherIcon.getUriString());
            } else {
                if (i != 5) {
                    return false;
                }
            }
        }
        if (getBitmap() != otherIcon.getBitmap()) {
            z = false;
        }
        return z;
    }

    public static Icon createWithResource(Context context, int resId) {
        if (context != null) {
            Icon rep = new Icon(2);
            rep.mInt1 = resId;
            rep.mString1 = context.getPackageName();
            return rep;
        }
        throw new IllegalArgumentException("Context must not be null.");
    }

    @UnsupportedAppUsage
    public static Icon createWithResource(Resources res, int resId) {
        if (res != null) {
            Icon rep = new Icon(2);
            rep.mInt1 = resId;
            rep.mString1 = res.getResourcePackageName(resId);
            return rep;
        }
        throw new IllegalArgumentException("Resource must not be null.");
    }

    public static Icon createWithResource(String resPackage, int resId) {
        if (resPackage != null) {
            Icon rep = new Icon(2);
            rep.mInt1 = resId;
            rep.mString1 = resPackage;
            return rep;
        }
        throw new IllegalArgumentException("Resource package name must not be null.");
    }

    public static Icon createWithBitmap(Bitmap bits) {
        if (bits != null) {
            Icon rep = new Icon(1);
            rep.setBitmap(bits);
            return rep;
        }
        throw new IllegalArgumentException("Bitmap must not be null.");
    }

    public static Icon createWithAdaptiveBitmap(Bitmap bits) {
        if (bits != null) {
            Icon rep = new Icon(5);
            rep.setBitmap(bits);
            return rep;
        }
        throw new IllegalArgumentException("Bitmap must not be null.");
    }

    public static Icon createWithData(byte[] data, int offset, int length) {
        if (data != null) {
            Icon rep = new Icon(3);
            rep.mObj1 = data;
            rep.mInt1 = length;
            rep.mInt2 = offset;
            return rep;
        }
        throw new IllegalArgumentException("Data must not be null.");
    }

    public static Icon createWithContentUri(String uri) {
        if (uri != null) {
            Icon rep = new Icon(4);
            rep.mString1 = uri;
            return rep;
        }
        throw new IllegalArgumentException("Uri must not be null.");
    }

    public static Icon createWithContentUri(Uri uri) {
        if (uri != null) {
            Icon rep = new Icon(4);
            rep.mString1 = uri.toString();
            return rep;
        }
        throw new IllegalArgumentException("Uri must not be null.");
    }

    public Icon setTint(int tint) {
        return setTintList(ColorStateList.valueOf(tint));
    }

    public Icon setTintList(ColorStateList tintList) {
        this.mTintList = tintList;
        return this;
    }

    public Icon setTintMode(Mode mode) {
        this.mBlendMode = BlendMode.fromValue(mode.nativeInt);
        return this;
    }

    public Icon setTintBlendMode(BlendMode mode) {
        this.mBlendMode = mode;
        return this;
    }

    @UnsupportedAppUsage
    public boolean hasTint() {
        return (this.mTintList == null && this.mBlendMode == DEFAULT_BLEND_MODE) ? false : true;
    }

    public static Icon createWithFilePath(String path) {
        if (path != null) {
            Icon rep = new Icon(4);
            rep.mString1 = path;
            return rep;
        }
        throw new IllegalArgumentException("Path must not be null.");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x009d  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x00d0  */
    /* JADX WARNING: Missing block: B:8:0x0021, code skipped:
            if (r1 != 5) goto L_0x0099;
     */
    public java.lang.String toString() {
        /*
        r11 = this;
        r0 = new java.lang.StringBuilder;
        r1 = "Icon(typ=";
        r0.<init>(r1);
        r1 = r11.mType;
        r1 = typeToString(r1);
        r0 = r0.append(r1);
        r1 = r11.mType;
        r2 = 2;
        r3 = 0;
        r4 = 1;
        if (r1 == r4) goto L_0x0077;
    L_0x0018:
        if (r1 == r2) goto L_0x0050;
    L_0x001a:
        r5 = 3;
        if (r1 == r5) goto L_0x0031;
    L_0x001d:
        r5 = 4;
        if (r1 == r5) goto L_0x0024;
    L_0x0020:
        r5 = 5;
        if (r1 == r5) goto L_0x0077;
    L_0x0023:
        goto L_0x0099;
    L_0x0024:
        r1 = " uri=";
        r0.append(r1);
        r1 = r11.getUriString();
        r0.append(r1);
        goto L_0x0099;
    L_0x0031:
        r1 = " len=";
        r0.append(r1);
        r1 = r11.getDataLength();
        r0.append(r1);
        r1 = r11.getDataOffset();
        if (r1 == 0) goto L_0x0099;
    L_0x0043:
        r1 = " off=";
        r0.append(r1);
        r1 = r11.getDataOffset();
        r0.append(r1);
        goto L_0x0099;
    L_0x0050:
        r1 = " pkg=";
        r0.append(r1);
        r1 = r11.getResPackage();
        r0.append(r1);
        r1 = " id=";
        r0.append(r1);
        r1 = new java.lang.Object[r4];
        r5 = r11.getResId();
        r5 = java.lang.Integer.valueOf(r5);
        r1[r3] = r5;
        r5 = "0x%08x";
        r1 = java.lang.String.format(r5, r1);
        r0.append(r1);
        goto L_0x0099;
    L_0x0077:
        r1 = " size=";
        r0.append(r1);
        r1 = r11.getBitmap();
        r1 = r1.getWidth();
        r0.append(r1);
        r1 = "x";
        r0.append(r1);
        r1 = r11.getBitmap();
        r1 = r1.getHeight();
        r0.append(r1);
    L_0x0099:
        r1 = r11.mTintList;
        if (r1 == 0) goto L_0x00ca;
    L_0x009d:
        r1 = " tint=";
        r0.append(r1);
        r1 = "";
        r5 = r11.mTintList;
        r5 = r5.getColors();
        r6 = r5.length;
        r7 = r1;
        r1 = r3;
    L_0x00ad:
        if (r1 >= r6) goto L_0x00ca;
    L_0x00af:
        r8 = r5[r1];
        r9 = new java.lang.Object[r2];
        r9[r3] = r7;
        r10 = java.lang.Integer.valueOf(r8);
        r9[r4] = r10;
        r10 = "%s0x%08x";
        r9 = java.lang.String.format(r10, r9);
        r0.append(r9);
        r7 = "|";
        r1 = r1 + 1;
        goto L_0x00ad;
    L_0x00ca:
        r1 = r11.mBlendMode;
        r2 = DEFAULT_BLEND_MODE;
        if (r1 == r2) goto L_0x00da;
    L_0x00d0:
        r1 = " mode=";
        r0.append(r1);
        r1 = r11.mBlendMode;
        r0.append(r1);
    L_0x00da:
        r1 = ")";
        r0.append(r1);
        r1 = r0.toString();
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.Icon.toString():java.lang.String");
    }

    public int describeContents() {
        int i = this.mType;
        if (i == 1 || i == 5 || i == 3) {
            return 1;
        }
        return 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x009c  */
    private Icon(android.os.Parcel r6) {
        /*
        r5 = this;
        r0 = r6.readInt();
        r5.<init>(r0);
        r0 = r5.mType;
        r1 = 1;
        if (r0 == r1) goto L_0x008b;
    L_0x000c:
        r2 = 2;
        if (r0 == r2) goto L_0x007e;
    L_0x000f:
        r2 = 3;
        if (r0 == r2) goto L_0x0049;
    L_0x0012:
        r2 = 4;
        if (r0 == r2) goto L_0x0042;
    L_0x0015:
        r2 = 5;
        if (r0 != r2) goto L_0x0019;
    L_0x0018:
        goto L_0x008b;
    L_0x0019:
        r0 = new java.lang.RuntimeException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "invalid ";
        r1.append(r2);
        r2 = r5.getClass();
        r2 = r2.getSimpleName();
        r1.append(r2);
        r2 = " type in parcel: ";
        r1.append(r2);
        r2 = r5.mType;
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0042:
        r0 = r6.readString();
        r5.mString1 = r0;
        goto L_0x0096;
    L_0x0049:
        r0 = r6.readInt();
        r2 = r6.readBlob();
        r3 = r2.length;
        if (r0 != r3) goto L_0x0059;
    L_0x0054:
        r5.mInt1 = r0;
        r5.mObj1 = r2;
        goto L_0x0096;
    L_0x0059:
        r1 = new java.lang.RuntimeException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "internal unparceling error: blob length (";
        r3.append(r4);
        r4 = r2.length;
        r3.append(r4);
        r4 = ") != expected length (";
        r3.append(r4);
        r3.append(r0);
        r4 = ")";
        r3.append(r4);
        r3 = r3.toString();
        r1.<init>(r3);
        throw r1;
    L_0x007e:
        r0 = r6.readString();
        r2 = r6.readInt();
        r5.mString1 = r0;
        r5.mInt1 = r2;
        goto L_0x0096;
    L_0x008b:
        r0 = android.graphics.Bitmap.CREATOR;
        r0 = r0.createFromParcel(r6);
        r0 = (android.graphics.Bitmap) r0;
        r5.mObj1 = r0;
    L_0x0096:
        r0 = r6.readInt();
        if (r0 != r1) goto L_0x00a6;
    L_0x009c:
        r0 = android.content.res.ColorStateList.CREATOR;
        r0 = r0.createFromParcel(r6);
        r0 = (android.content.res.ColorStateList) r0;
        r5.mTintList = r0;
    L_0x00a6:
        r0 = r6.readInt();
        r0 = android.graphics.BlendMode.fromValue(r0);
        r5.mBlendMode = r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.Icon.<init>(android.os.Parcel):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0055  */
    /* JADX WARNING: Missing block: B:9:0x0014, code skipped:
            if (r0 != 5) goto L_0x0051;
     */
    public void writeToParcel(android.os.Parcel r5, int r6) {
        /*
        r4 = this;
        r0 = r4.mType;
        r5.writeInt(r0);
        r0 = r4.mType;
        r1 = 1;
        if (r0 == r1) goto L_0x0045;
    L_0x000a:
        r2 = 2;
        if (r0 == r2) goto L_0x0036;
    L_0x000d:
        r2 = 3;
        if (r0 == r2) goto L_0x001f;
    L_0x0010:
        r2 = 4;
        if (r0 == r2) goto L_0x0017;
    L_0x0013:
        r2 = 5;
        if (r0 == r2) goto L_0x0045;
    L_0x0016:
        goto L_0x0051;
    L_0x0017:
        r0 = r4.getUriString();
        r5.writeString(r0);
        goto L_0x0051;
    L_0x001f:
        r0 = r4.getDataLength();
        r5.writeInt(r0);
        r0 = r4.getDataBytes();
        r2 = r4.getDataOffset();
        r3 = r4.getDataLength();
        r5.writeBlob(r0, r2, r3);
        goto L_0x0051;
    L_0x0036:
        r0 = r4.getResPackage();
        r5.writeString(r0);
        r0 = r4.getResId();
        r5.writeInt(r0);
        goto L_0x0051;
    L_0x0045:
        r0 = r4.getBitmap();
        r2 = r4.getBitmap();
        r2.writeToParcel(r5, r6);
    L_0x0051:
        r0 = r4.mTintList;
        if (r0 != 0) goto L_0x005a;
    L_0x0055:
        r0 = 0;
        r5.writeInt(r0);
        goto L_0x0062;
    L_0x005a:
        r5.writeInt(r1);
        r0 = r4.mTintList;
        r0.writeToParcel(r5, r6);
    L_0x0062:
        r0 = r4.mBlendMode;
        r0 = android.graphics.BlendMode.toValue(r0);
        r5.writeInt(r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.Icon.writeToParcel(android.os.Parcel, int):void");
    }

    public static Bitmap scaleDownIfNecessary(Bitmap bitmap, int maxWidth, int maxHeight) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        if (bitmapWidth <= maxWidth && bitmapHeight <= maxHeight) {
            return bitmap;
        }
        float scale = Math.min(((float) maxWidth) / ((float) bitmapWidth), ((float) maxHeight) / ((float) bitmapHeight));
        return Bitmap.createScaledBitmap(bitmap, Math.max(1, (int) (((float) bitmapWidth) * scale)), Math.max(1, (int) (((float) bitmapHeight) * scale)), true);
    }

    public void scaleDownIfNecessary(int maxWidth, int maxHeight) {
        int i = this.mType;
        if (i == 1 || i == 5) {
            setBitmap(scaleDownIfNecessary(getBitmap(), maxWidth, maxHeight));
        }
    }
}
