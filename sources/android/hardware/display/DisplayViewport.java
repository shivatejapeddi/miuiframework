package android.hardware.display;

import android.graphics.Rect;
import android.text.TextUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class DisplayViewport {
    public static final int VIEWPORT_EXTERNAL = 2;
    public static final int VIEWPORT_INTERNAL = 1;
    public static final int VIEWPORT_VIRTUAL = 3;
    public int deviceHeight;
    public int deviceWidth;
    public int displayId;
    public final Rect logicalFrame = new Rect();
    public int orientation;
    public final Rect physicalFrame = new Rect();
    public Byte physicalPort;
    public int type;
    public String uniqueId;
    public boolean valid;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewportType {
    }

    public void copyFrom(DisplayViewport viewport) {
        this.valid = viewport.valid;
        this.displayId = viewport.displayId;
        this.orientation = viewport.orientation;
        this.logicalFrame.set(viewport.logicalFrame);
        this.physicalFrame.set(viewport.physicalFrame);
        this.deviceWidth = viewport.deviceWidth;
        this.deviceHeight = viewport.deviceHeight;
        this.uniqueId = viewport.uniqueId;
        this.physicalPort = viewport.physicalPort;
        this.type = viewport.type;
    }

    public DisplayViewport makeCopy() {
        DisplayViewport dv = new DisplayViewport();
        dv.copyFrom(this);
        return dv;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof DisplayViewport)) {
            return false;
        }
        DisplayViewport other = (DisplayViewport) o;
        if (!(this.valid == other.valid && this.displayId == other.displayId && this.orientation == other.orientation && this.logicalFrame.equals(other.logicalFrame) && this.physicalFrame.equals(other.physicalFrame) && this.deviceWidth == other.deviceWidth && this.deviceHeight == other.deviceHeight && TextUtils.equals(this.uniqueId, other.uniqueId) && this.physicalPort == other.physicalPort && this.type == other.type)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result = 1 + ((1 * 31) + this.valid);
        result += (result * 31) + this.displayId;
        result += (result * 31) + this.orientation;
        result += (result * 31) + this.logicalFrame.hashCode();
        result += (result * 31) + this.physicalFrame.hashCode();
        result += (result * 31) + this.deviceWidth;
        result += (result * 31) + this.deviceHeight;
        result += (result * 31) + this.uniqueId.hashCode();
        result += (result * 31) + this.physicalPort.byteValue();
        return result + ((result * 31) + this.type);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DisplayViewport{type=");
        stringBuilder.append(typeToString(this.type));
        stringBuilder.append(", valid=");
        stringBuilder.append(this.valid);
        stringBuilder.append(", displayId=");
        stringBuilder.append(this.displayId);
        stringBuilder.append(", uniqueId='");
        stringBuilder.append(this.uniqueId);
        stringBuilder.append("', physicalPort=");
        stringBuilder.append(this.physicalPort);
        stringBuilder.append(", orientation=");
        stringBuilder.append(this.orientation);
        stringBuilder.append(", logicalFrame=");
        stringBuilder.append(this.logicalFrame);
        stringBuilder.append(", physicalFrame=");
        stringBuilder.append(this.physicalFrame);
        stringBuilder.append(", deviceWidth=");
        stringBuilder.append(this.deviceWidth);
        stringBuilder.append(", deviceHeight=");
        stringBuilder.append(this.deviceHeight);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static String typeToString(int viewportType) {
        if (viewportType == 1) {
            return "INTERNAL";
        }
        if (viewportType == 2) {
            return "EXTERNAL";
        }
        if (viewportType == 3) {
            return "VIRTUAL";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UNKNOWN (");
        stringBuilder.append(viewportType);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
