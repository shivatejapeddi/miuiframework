package android.app;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemProperties;
import android.provider.CalendarContract.CalendarCache;
import android.provider.Settings.Secure;
import android.util.Log;
import android.util.proto.ProtoInputStream;
import android.util.proto.ProtoOutputStream;
import android.util.proto.WireTypeMismatchException;
import android.view.Surface;
import java.io.IOException;

public class WindowConfiguration implements Parcelable, Comparable<WindowConfiguration> {
    public static final int ACTIVITY_TYPE_ASSISTANT = 4;
    public static final int ACTIVITY_TYPE_HOME = 2;
    public static final int ACTIVITY_TYPE_RECENTS = 3;
    public static final int ACTIVITY_TYPE_STANDARD = 1;
    public static final int ACTIVITY_TYPE_UNDEFINED = 0;
    private static final int ALWAYS_ON_TOP_OFF = 2;
    private static final int ALWAYS_ON_TOP_ON = 1;
    private static final int ALWAYS_ON_TOP_UNDEFINED = 0;
    public static final Creator<WindowConfiguration> CREATOR = new Creator<WindowConfiguration>() {
        public WindowConfiguration createFromParcel(Parcel in) {
            return new WindowConfiguration(in, null);
        }

        public WindowConfiguration[] newArray(int size) {
            return new WindowConfiguration[size];
        }
    };
    public static final int PINNED_WINDOWING_MODE_ELEVATION_IN_DIP = 5;
    public static final int ROTATION_UNDEFINED = -1;
    public static final int WINDOWING_MODE_FREEFORM = 5;
    public static final int WINDOWING_MODE_FULLSCREEN = 1;
    public static final int WINDOWING_MODE_FULLSCREEN_OR_SPLIT_SCREEN_SECONDARY = 4;
    public static final int WINDOWING_MODE_PINNED = 2;
    public static final int WINDOWING_MODE_SPLIT_SCREEN_PRIMARY = 3;
    public static final int WINDOWING_MODE_SPLIT_SCREEN_SECONDARY = 4;
    public static final int WINDOWING_MODE_UNDEFINED = 0;
    public static final int WINDOW_CONFIG_ACTIVITY_TYPE = 8;
    public static final int WINDOW_CONFIG_ALWAYS_ON_TOP = 16;
    public static final int WINDOW_CONFIG_APP_BOUNDS = 2;
    public static final int WINDOW_CONFIG_BOUNDS = 1;
    public static final int WINDOW_CONFIG_DISPLAY_WINDOWING_MODE = 64;
    public static final int WINDOW_CONFIG_ROTATION = 32;
    public static final int WINDOW_CONFIG_WINDOWING_MODE = 4;
    @ActivityType
    private int mActivityType;
    @AlwaysOnTop
    private int mAlwaysOnTop;
    private Rect mAppBounds;
    private Rect mBounds;
    @WindowingMode
    private int mDisplayWindowingMode;
    private int mRotation;
    @WindowingMode
    private int mWindowingMode;

    public @interface ActivityType {
    }

    private @interface AlwaysOnTop {
    }

    public @interface WindowConfig {
    }

    public @interface WindowingMode {
    }

    /* synthetic */ WindowConfiguration(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public WindowConfiguration() {
        this.mBounds = new Rect();
        this.mRotation = -1;
        unset();
    }

    public WindowConfiguration(WindowConfiguration configuration) {
        this.mBounds = new Rect();
        this.mRotation = -1;
        setTo(configuration);
    }

    private WindowConfiguration(Parcel in) {
        this.mBounds = new Rect();
        this.mRotation = -1;
        readFromParcel(in);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mBounds, flags);
        dest.writeParcelable(this.mAppBounds, flags);
        dest.writeInt(this.mWindowingMode);
        dest.writeInt(this.mActivityType);
        dest.writeInt(this.mAlwaysOnTop);
        dest.writeInt(this.mRotation);
        dest.writeInt(this.mDisplayWindowingMode);
    }

    private void readFromParcel(Parcel source) {
        this.mBounds = (Rect) source.readParcelable(Rect.class.getClassLoader());
        this.mAppBounds = (Rect) source.readParcelable(Rect.class.getClassLoader());
        this.mWindowingMode = source.readInt();
        this.mActivityType = source.readInt();
        this.mAlwaysOnTop = source.readInt();
        this.mRotation = source.readInt();
        this.mDisplayWindowingMode = source.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void setBounds(Rect rect) {
        if (rect == null) {
            this.mBounds.setEmpty();
        } else {
            this.mBounds.set(rect);
        }
    }

    public void setAppBounds(Rect rect) {
        if (rect == null) {
            this.mAppBounds = null;
        } else {
            setAppBounds(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    public void setAlwaysOnTop(boolean alwaysOnTop) {
        this.mAlwaysOnTop = alwaysOnTop ? 1 : 2;
    }

    private void setAlwaysOnTop(@AlwaysOnTop int alwaysOnTop) {
        this.mAlwaysOnTop = alwaysOnTop;
    }

    public void setAppBounds(int left, int top, int right, int bottom) {
        if (this.mAppBounds == null) {
            this.mAppBounds = new Rect();
        }
        this.mAppBounds.set(left, top, right, bottom);
    }

    public Rect getAppBounds() {
        return this.mAppBounds;
    }

    public Rect getBounds() {
        return this.mBounds;
    }

    public int getRotation() {
        return this.mRotation;
    }

    public void setRotation(int rotation) {
        this.mRotation = rotation;
    }

    public void setWindowingMode(@WindowingMode int windowingMode) {
        this.mWindowingMode = windowingMode;
    }

    @WindowingMode
    public int getWindowingMode() {
        return this.mWindowingMode;
    }

    public void setDisplayWindowingMode(@WindowingMode int windowingMode) {
        this.mDisplayWindowingMode = windowingMode;
    }

    public void setActivityType(@ActivityType int activityType) {
        if (this.mActivityType != activityType) {
            if (!(!ActivityThread.isSystem() || this.mActivityType == 0 || activityType == 0)) {
                StringBuilder stringBuilder = new StringBuilder();
                String str = "Can't change activity type once set: ";
                stringBuilder.append(str);
                stringBuilder.append(this);
                String str2 = " activityType=";
                stringBuilder.append(str2);
                stringBuilder.append(activityTypeToString(activityType));
                Log.d("ActivityManager", stringBuilder.toString());
                if (!SystemProperties.getBoolean("persist.sys.miui_optimization", true)) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str);
                    stringBuilder2.append(this);
                    stringBuilder2.append(str2);
                    stringBuilder2.append(activityTypeToString(activityType));
                    throw new IllegalStateException(stringBuilder2.toString());
                }
            }
            this.mActivityType = activityType;
        }
    }

    @ActivityType
    public int getActivityType() {
        return this.mActivityType;
    }

    public void setTo(WindowConfiguration other) {
        setBounds(other.mBounds);
        setAppBounds(other.mAppBounds);
        setWindowingMode(other.mWindowingMode);
        setActivityType(other.mActivityType);
        setAlwaysOnTop(other.mAlwaysOnTop);
        setRotation(other.mRotation);
        setDisplayWindowingMode(other.mDisplayWindowingMode);
    }

    public void unset() {
        setToDefaults();
    }

    public void setToDefaults() {
        setAppBounds(null);
        setBounds(null);
        setWindowingMode(0);
        setActivityType(0);
        setAlwaysOnTop(0);
        setRotation(-1);
        setDisplayWindowingMode(0);
    }

    @WindowConfig
    public int updateFrom(WindowConfiguration delta) {
        int changed = 0;
        if (!(delta.mBounds.isEmpty() || delta.mBounds.equals(this.mBounds))) {
            changed = 0 | 1;
            setBounds(delta.mBounds);
        }
        Rect rect = delta.mAppBounds;
        if (!(rect == null || rect.equals(this.mAppBounds))) {
            changed |= 2;
            setAppBounds(delta.mAppBounds);
        }
        int i = delta.mWindowingMode;
        if (!(i == 0 || this.mWindowingMode == i)) {
            changed |= 4;
            setWindowingMode(i);
        }
        i = delta.mActivityType;
        if (!(i == 0 || this.mActivityType == i)) {
            changed |= 8;
            setActivityType(i);
        }
        i = delta.mAlwaysOnTop;
        if (!(i == 0 || this.mAlwaysOnTop == i)) {
            changed |= 16;
            setAlwaysOnTop(i);
        }
        i = delta.mRotation;
        if (!(i == -1 || i == this.mRotation)) {
            changed |= 32;
            setRotation(i);
        }
        i = delta.mDisplayWindowingMode;
        if (i == 0 || this.mDisplayWindowingMode == i) {
            return changed;
        }
        changed |= 64;
        setDisplayWindowingMode(i);
        return changed;
    }

    @WindowConfig
    public long diff(WindowConfiguration other, boolean compareUndefined) {
        long changes = 0;
        if (!this.mBounds.equals(other.mBounds)) {
            changes = 0 | 1;
        }
        if (compareUndefined || other.mAppBounds != null) {
            Rect rect = this.mAppBounds;
            Rect rect2 = other.mAppBounds;
            if (rect != rect2 && (rect == null || !rect.equals(rect2))) {
                changes |= 2;
            }
        }
        if ((compareUndefined || other.mWindowingMode != 0) && this.mWindowingMode != other.mWindowingMode) {
            changes |= 4;
        }
        if ((compareUndefined || other.mActivityType != 0) && this.mActivityType != other.mActivityType) {
            changes |= 8;
        }
        if ((compareUndefined || other.mAlwaysOnTop != 0) && this.mAlwaysOnTop != other.mAlwaysOnTop) {
            changes |= 16;
        }
        if ((compareUndefined || other.mRotation != -1) && this.mRotation != other.mRotation) {
            changes |= 32;
        }
        if ((compareUndefined || other.mDisplayWindowingMode != 0) && this.mDisplayWindowingMode != other.mDisplayWindowingMode) {
            return changes | 64;
        }
        return changes;
    }

    public int compareTo(WindowConfiguration that) {
        if (this.mAppBounds == null && that.mAppBounds != null) {
            return 1;
        }
        if (this.mAppBounds != null && that.mAppBounds == null) {
            return -1;
        }
        int n;
        int n2;
        Rect rect = this.mAppBounds;
        if (!(rect == null || that.mAppBounds == null)) {
            n = rect.left - that.mAppBounds.left;
            if (n != 0) {
                return n;
            }
            n2 = this.mAppBounds.top - that.mAppBounds.top;
            if (n2 != 0) {
                return n2;
            }
            n = this.mAppBounds.right - that.mAppBounds.right;
            if (n != 0) {
                return n;
            }
            n2 = this.mAppBounds.bottom - that.mAppBounds.bottom;
            if (n2 != 0) {
                return n2;
            }
        }
        n = this.mBounds.left - that.mBounds.left;
        if (n != 0) {
            return n;
        }
        n2 = this.mBounds.top - that.mBounds.top;
        if (n2 != 0) {
            return n2;
        }
        n = this.mBounds.right - that.mBounds.right;
        if (n != 0) {
            return n;
        }
        n2 = this.mBounds.bottom - that.mBounds.bottom;
        if (n2 != 0) {
            return n2;
        }
        n = this.mWindowingMode - that.mWindowingMode;
        if (n != 0) {
            return n;
        }
        n2 = this.mActivityType - that.mActivityType;
        if (n2 != 0) {
            return n2;
        }
        n = this.mAlwaysOnTop - that.mAlwaysOnTop;
        if (n != 0) {
            return n;
        }
        n2 = this.mRotation - that.mRotation;
        if (n2 != 0) {
            return n2;
        }
        n = this.mDisplayWindowingMode - that.mDisplayWindowingMode;
        if (n != 0) {
            return n;
        }
        return n;
    }

    public boolean equals(Object that) {
        boolean z = false;
        if (that == null) {
            return false;
        }
        if (that == this) {
            return true;
        }
        if (!(that instanceof WindowConfiguration)) {
            return false;
        }
        if (compareTo((WindowConfiguration) that) == 0) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        int result = 0;
        Rect rect = this.mAppBounds;
        if (rect != null) {
            result = (0 * 31) + rect.hashCode();
        }
        return (((((((((((result * 31) + this.mBounds.hashCode()) * 31) + this.mWindowingMode) * 31) + this.mActivityType) * 31) + this.mAlwaysOnTop) * 31) + this.mRotation) * 31) + this.mDisplayWindowingMode;
    }

    public String toString() {
        String str;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ mBounds=");
        stringBuilder.append(this.mBounds);
        stringBuilder.append(" mAppBounds=");
        stringBuilder.append(this.mAppBounds);
        stringBuilder.append(" mWindowingMode=");
        stringBuilder.append(windowingModeToString(this.mWindowingMode));
        stringBuilder.append(" mDisplayWindowingMode=");
        stringBuilder.append(windowingModeToString(this.mDisplayWindowingMode));
        stringBuilder.append(" mActivityType=");
        stringBuilder.append(activityTypeToString(this.mActivityType));
        stringBuilder.append(" mAlwaysOnTop=");
        stringBuilder.append(alwaysOnTopToString(this.mAlwaysOnTop));
        stringBuilder.append(" mRotation=");
        int i = this.mRotation;
        if (i == -1) {
            str = "undefined";
        } else {
            str = Surface.rotationToString(i);
        }
        stringBuilder.append(str);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public void writeToProto(ProtoOutputStream protoOutputStream, long fieldId) {
        long token = protoOutputStream.start(fieldId);
        Rect rect = this.mAppBounds;
        if (rect != null) {
            rect.writeToProto(protoOutputStream, 1146756268033L);
        }
        protoOutputStream.write(1120986464258L, this.mWindowingMode);
        protoOutputStream.write(1120986464259L, this.mActivityType);
        rect = this.mBounds;
        if (rect != null) {
            rect.writeToProto(protoOutputStream, 1146756268036L);
        }
        protoOutputStream.end(token);
    }

    public void readFromProto(ProtoInputStream proto, long fieldId) throws IOException, WireTypeMismatchException {
        long token = proto.start(fieldId);
        while (proto.nextField() != -1) {
            try {
                int fieldNumber = proto.getFieldNumber();
                if (fieldNumber == 1) {
                    this.mAppBounds = new Rect();
                    this.mAppBounds.readFromProto(proto, 1146756268033L);
                } else if (fieldNumber == 2) {
                    this.mWindowingMode = proto.readInt(1120986464258L);
                } else if (fieldNumber == 3) {
                    this.mActivityType = proto.readInt(1120986464259L);
                } else if (fieldNumber == 4) {
                    this.mBounds = new Rect();
                    this.mBounds.readFromProto(proto, 1146756268036L);
                }
            } finally {
                proto.end(token);
            }
        }
    }

    public boolean hasWindowShadow() {
        return tasksAreFloating();
    }

    public boolean hasWindowDecorCaption() {
        return this.mWindowingMode == 5 || this.mDisplayWindowingMode == 5;
    }

    public boolean canResizeTask() {
        return this.mWindowingMode == 5;
    }

    public boolean persistTaskBounds() {
        return this.mWindowingMode == 5;
    }

    public boolean tasksAreFloating() {
        return isFloating(this.mWindowingMode);
    }

    public static boolean isFloating(int windowingMode) {
        return windowingMode == 5 || windowingMode == 2;
    }

    public static boolean isSplitScreenWindowingMode(int windowingMode) {
        return windowingMode == 3 || windowingMode == 4;
    }

    public boolean canReceiveKeys() {
        return this.mWindowingMode != 2;
    }

    public boolean isAlwaysOnTop() {
        int i = this.mWindowingMode;
        return i == 2 || i == 5;
    }

    public boolean keepVisibleDeadAppWindowOnScreen() {
        return this.mWindowingMode != 2;
    }

    public boolean useWindowFrameForBackdrop() {
        int i = this.mWindowingMode;
        return i == 5 || i == 2;
    }

    public boolean windowsAreScaleable() {
        return this.mWindowingMode == 2;
    }

    public boolean hasMovementAnimations() {
        return this.mWindowingMode != 2;
    }

    public boolean supportSplitScreenWindowingMode() {
        return supportSplitScreenWindowingMode(this.mActivityType);
    }

    public static boolean supportSplitScreenWindowingMode(int activityType) {
        return activityType != 4;
    }

    public static String windowingModeToString(@WindowingMode int windowingMode) {
        if (windowingMode == 0) {
            return "undefined";
        }
        if (windowingMode == 1) {
            return "fullscreen";
        }
        if (windowingMode == 2) {
            return ContactOptionsColumns.PINNED;
        }
        if (windowingMode == 3) {
            return "split-screen-primary";
        }
        if (windowingMode == 4) {
            return "split-screen-secondary";
        }
        if (windowingMode != 5) {
            return String.valueOf(windowingMode);
        }
        return "freeform";
    }

    public static String activityTypeToString(@ActivityType int applicationType) {
        if (applicationType == 0) {
            return "undefined";
        }
        if (applicationType == 1) {
            return "standard";
        }
        if (applicationType == 2) {
            return CalendarCache.TIMEZONE_TYPE_HOME;
        }
        if (applicationType == 3) {
            return "recents";
        }
        if (applicationType != 4) {
            return String.valueOf(applicationType);
        }
        return Secure.ASSISTANT;
    }

    public static String alwaysOnTopToString(@AlwaysOnTop int alwaysOnTop) {
        if (alwaysOnTop == 0) {
            return "undefined";
        }
        if (alwaysOnTop == 1) {
            return "on";
        }
        if (alwaysOnTop != 2) {
            return String.valueOf(alwaysOnTop);
        }
        return "off";
    }
}
