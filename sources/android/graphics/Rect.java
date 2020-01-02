package android.graphics;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.proto.ProtoInputStream;
import android.util.proto.ProtoOutputStream;
import android.util.proto.WireTypeMismatchException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Rect implements Parcelable {
    public static final Creator<Rect> CREATOR = new Creator<Rect>() {
        public Rect createFromParcel(Parcel in) {
            Rect r = new Rect();
            r.readFromParcel(in);
            return r;
        }

        public Rect[] newArray(int size) {
            return new Rect[size];
        }
    };
    public int bottom;
    public int left;
    public int right;
    public int top;

    private static final class UnflattenHelper {
        private static final Pattern FLATTENED_PATTERN = Pattern.compile("(-?\\d+) (-?\\d+) (-?\\d+) (-?\\d+)");

        private UnflattenHelper() {
        }

        static Matcher getMatcher(String str) {
            return FLATTENED_PATTERN.matcher(str);
        }
    }

    public Rect(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public Rect(Rect r) {
        if (r == null) {
            this.bottom = 0;
            this.right = 0;
            this.top = 0;
            this.left = 0;
            return;
        }
        this.left = r.left;
        this.top = r.top;
        this.right = r.right;
        this.bottom = r.bottom;
    }

    public Rect(Insets r) {
        if (r == null) {
            this.bottom = 0;
            this.right = 0;
            this.top = 0;
            this.left = 0;
            return;
        }
        this.left = r.left;
        this.top = r.top;
        this.right = r.right;
        this.bottom = r.bottom;
    }

    public static Rect copyOrNull(Rect r) {
        return r == null ? null : new Rect(r);
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rect r = (Rect) o;
        if (!(this.left == r.left && this.top == r.top && this.right == r.right && this.bottom == r.bottom)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((this.left * 31) + this.top) * 31) + this.right) * 31) + this.bottom;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append("Rect(");
        sb.append(this.left);
        String str = ", ";
        sb.append(str);
        sb.append(this.top);
        sb.append(" - ");
        sb.append(this.right);
        sb.append(str);
        sb.append(this.bottom);
        sb.append(")");
        return sb.toString();
    }

    public String toShortString() {
        return toShortString(new StringBuilder(32));
    }

    public String toShortString(StringBuilder sb) {
        sb.setLength(0);
        sb.append('[');
        sb.append(this.left);
        sb.append(',');
        sb.append(this.top);
        sb.append("][");
        sb.append(this.right);
        sb.append(',');
        sb.append(this.bottom);
        sb.append(']');
        return sb.toString();
    }

    public String flattenToString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append(this.left);
        sb.append(' ');
        sb.append(this.top);
        sb.append(' ');
        sb.append(this.right);
        sb.append(' ');
        sb.append(this.bottom);
        return sb.toString();
    }

    public static Rect unflattenFromString(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Matcher matcher = UnflattenHelper.getMatcher(str);
        if (matcher.matches()) {
            return new Rect(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
        }
        return null;
    }

    @UnsupportedAppUsage
    public void printShortString(PrintWriter pw) {
        pw.print('[');
        pw.print(this.left);
        pw.print(',');
        pw.print(this.top);
        pw.print("][");
        pw.print(this.right);
        pw.print(',');
        pw.print(this.bottom);
        pw.print(']');
    }

    public void writeToProto(ProtoOutputStream protoOutputStream, long fieldId) {
        long token = protoOutputStream.start(fieldId);
        protoOutputStream.write(1120986464257L, this.left);
        protoOutputStream.write(1120986464258L, this.top);
        protoOutputStream.write(1120986464259L, this.right);
        protoOutputStream.write(1120986464260L, this.bottom);
        protoOutputStream.end(token);
    }

    public void readFromProto(ProtoInputStream proto, long fieldId) throws IOException, WireTypeMismatchException {
        long token = proto.start(fieldId);
        while (proto.nextField() != -1) {
            try {
                int fieldNumber = proto.getFieldNumber();
                if (fieldNumber == 1) {
                    this.left = proto.readInt(1120986464257L);
                } else if (fieldNumber == 2) {
                    this.top = proto.readInt(1120986464258L);
                } else if (fieldNumber == 3) {
                    this.right = proto.readInt(1120986464259L);
                } else if (fieldNumber == 4) {
                    this.bottom = proto.readInt(1120986464260L);
                }
            } finally {
                proto.end(token);
            }
        }
    }

    public final boolean isEmpty() {
        return this.left >= this.right || this.top >= this.bottom;
    }

    public final int width() {
        return this.right - this.left;
    }

    public final int height() {
        return this.bottom - this.top;
    }

    public final int centerX() {
        return (this.left + this.right) >> 1;
    }

    public final int centerY() {
        return (this.top + this.bottom) >> 1;
    }

    public final float exactCenterX() {
        return ((float) (this.left + this.right)) * 0.5f;
    }

    public final float exactCenterY() {
        return ((float) (this.top + this.bottom)) * 0.5f;
    }

    public void setEmpty() {
        this.bottom = 0;
        this.top = 0;
        this.right = 0;
        this.left = 0;
    }

    public void set(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void set(Rect src) {
        this.left = src.left;
        this.top = src.top;
        this.right = src.right;
        this.bottom = src.bottom;
    }

    public void offset(int dx, int dy) {
        this.left += dx;
        this.top += dy;
        this.right += dx;
        this.bottom += dy;
    }

    public void offsetTo(int newLeft, int newTop) {
        this.right += newLeft - this.left;
        this.bottom += newTop - this.top;
        this.left = newLeft;
        this.top = newTop;
    }

    public void inset(int dx, int dy) {
        this.left += dx;
        this.top += dy;
        this.right -= dx;
        this.bottom -= dy;
    }

    public void inset(Rect insets) {
        this.left += insets.left;
        this.top += insets.top;
        this.right -= insets.right;
        this.bottom -= insets.bottom;
    }

    public void inset(Insets insets) {
        this.left += insets.left;
        this.top += insets.top;
        this.right -= insets.right;
        this.bottom -= insets.bottom;
    }

    public void inset(int left, int top, int right, int bottom) {
        this.left += left;
        this.top += top;
        this.right -= right;
        this.bottom -= bottom;
    }

    public boolean contains(int x, int y) {
        int i = this.left;
        int i2 = this.right;
        if (i < i2) {
            int i3 = this.top;
            int i4 = this.bottom;
            if (i3 < i4 && x >= i && x < i2 && y >= i3 && y < i4) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(int left, int top, int right, int bottom) {
        int i = this.left;
        int i2 = this.right;
        if (i < i2) {
            int i3 = this.top;
            int i4 = this.bottom;
            if (i3 < i4 && i <= left && i3 <= top && i2 >= right && i4 >= bottom) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Rect r) {
        int i = this.left;
        int i2 = this.right;
        if (i < i2) {
            int i3 = this.top;
            int i4 = this.bottom;
            if (i3 < i4 && i <= r.left && i3 <= r.top && i2 >= r.right && i4 >= r.bottom) {
                return true;
            }
        }
        return false;
    }

    public boolean intersect(int left, int top, int right, int bottom) {
        int i = this.left;
        if (i >= right || left >= this.right || this.top >= bottom || top >= this.bottom) {
            return false;
        }
        if (i < left) {
            this.left = left;
        }
        if (this.top < top) {
            this.top = top;
        }
        if (this.right > right) {
            this.right = right;
        }
        if (this.bottom > bottom) {
            this.bottom = bottom;
        }
        return true;
    }

    public boolean intersect(Rect r) {
        return intersect(r.left, r.top, r.right, r.bottom);
    }

    public void intersectUnchecked(Rect other) {
        this.left = Math.max(this.left, other.left);
        this.top = Math.max(this.top, other.top);
        this.right = Math.min(this.right, other.right);
        this.bottom = Math.min(this.bottom, other.bottom);
    }

    public boolean setIntersect(Rect a, Rect b) {
        int i = a.left;
        if (i < b.right) {
            int i2 = b.left;
            if (i2 < a.right && a.top < b.bottom && b.top < a.bottom) {
                this.left = Math.max(i, i2);
                this.top = Math.max(a.top, b.top);
                this.right = Math.min(a.right, b.right);
                this.bottom = Math.min(a.bottom, b.bottom);
                return true;
            }
        }
        return false;
    }

    public boolean intersects(int left, int top, int right, int bottom) {
        return this.left < right && left < this.right && this.top < bottom && top < this.bottom;
    }

    public static boolean intersects(Rect a, Rect b) {
        return a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom;
    }

    public void union(int left, int top, int right, int bottom) {
        if (left < right && top < bottom) {
            int i = this.left;
            if (i >= this.right || this.top >= this.bottom) {
                this.left = left;
                this.top = top;
                this.right = right;
                this.bottom = bottom;
                return;
            }
            if (i > left) {
                this.left = left;
            }
            if (this.top > top) {
                this.top = top;
            }
            if (this.right < right) {
                this.right = right;
            }
            if (this.bottom < bottom) {
                this.bottom = bottom;
            }
        }
    }

    public void union(Rect r) {
        union(r.left, r.top, r.right, r.bottom);
    }

    public void union(int x, int y) {
        if (x < this.left) {
            this.left = x;
        } else if (x > this.right) {
            this.right = x;
        }
        if (y < this.top) {
            this.top = y;
        } else if (y > this.bottom) {
            this.bottom = y;
        }
    }

    public void sort() {
        int i = this.left;
        int i2 = this.right;
        if (i > i2) {
            i = this.left;
            this.left = i2;
            this.right = i;
        }
        i = this.top;
        i2 = this.bottom;
        if (i > i2) {
            i = this.top;
            this.top = i2;
            this.bottom = i;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.left);
        out.writeInt(this.top);
        out.writeInt(this.right);
        out.writeInt(this.bottom);
    }

    public void readFromParcel(Parcel in) {
        this.left = in.readInt();
        this.top = in.readInt();
        this.right = in.readInt();
        this.bottom = in.readInt();
    }

    @UnsupportedAppUsage
    public void scale(float scale) {
        if (scale != 1.0f) {
            this.left = (int) ((((float) this.left) * scale) + 0.5f);
            this.top = (int) ((((float) this.top) * scale) + 0.5f);
            this.right = (int) ((((float) this.right) * scale) + 0.5f);
            this.bottom = (int) ((((float) this.bottom) * scale) + 0.5f);
        }
    }
}
