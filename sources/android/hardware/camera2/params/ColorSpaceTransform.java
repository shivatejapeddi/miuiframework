package android.hardware.camera2.params;

import android.hardware.camera2.utils.HashCodeHelpers;
import android.util.Rational;
import com.android.internal.util.Preconditions;
import java.util.Arrays;

public final class ColorSpaceTransform {
    private static final int COLUMNS = 3;
    private static final int COUNT = 9;
    private static final int COUNT_INT = 18;
    private static final int OFFSET_DENOMINATOR = 1;
    private static final int OFFSET_NUMERATOR = 0;
    private static final int RATIONAL_SIZE = 2;
    private static final int ROWS = 3;
    private final int[] mElements;

    public ColorSpaceTransform(Rational[] elements) {
        Preconditions.checkNotNull(elements, "elements must not be null");
        if (elements.length == 9) {
            this.mElements = new int[18];
            for (int i = 0; i < elements.length; i++) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("element[");
                stringBuilder.append(i);
                stringBuilder.append("] must not be null");
                Preconditions.checkNotNull(elements, stringBuilder.toString());
                this.mElements[(i * 2) + 0] = elements[i].getNumerator();
                this.mElements[(i * 2) + 1] = elements[i].getDenominator();
            }
            return;
        }
        throw new IllegalArgumentException("elements must be 9 length");
    }

    public ColorSpaceTransform(int[] elements) {
        Preconditions.checkNotNull(elements, "elements must not be null");
        if (elements.length == 18) {
            for (int i = 0; i < elements.length; i++) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("element ");
                stringBuilder.append(i);
                stringBuilder.append(" must not be null");
                Preconditions.checkNotNull(elements, stringBuilder.toString());
            }
            this.mElements = Arrays.copyOf(elements, elements.length);
            return;
        }
        throw new IllegalArgumentException("elements must be 18 length");
    }

    public Rational getElement(int column, int row) {
        if (column < 0 || column >= 3) {
            throw new IllegalArgumentException("column out of range");
        } else if (row < 0 || row >= 3) {
            throw new IllegalArgumentException("row out of range");
        } else {
            int denominator = this.mElements;
            return new Rational(denominator[(((row * 3) + column) * 2) + 0], denominator[(((row * 3) + column) * 2) + 1]);
        }
    }

    public void copyElements(Rational[] destination, int offset) {
        Preconditions.checkArgumentNonnegative(offset, "offset must not be negative");
        Preconditions.checkNotNull(destination, "destination must not be null");
        if (destination.length - offset >= 9) {
            int i = 0;
            int j = 0;
            while (i < 9) {
                int denominator = this.mElements;
                destination[i + offset] = new Rational(denominator[j + 0], denominator[j + 1]);
                i++;
                j += 2;
            }
            return;
        }
        throw new ArrayIndexOutOfBoundsException("destination too small to fit elements");
    }

    public void copyElements(int[] destination, int offset) {
        Preconditions.checkArgumentNonnegative(offset, "offset must not be negative");
        Preconditions.checkNotNull(destination, "destination must not be null");
        if (destination.length - offset >= 18) {
            for (int i = 0; i < 18; i++) {
                destination[i + offset] = this.mElements[i];
            }
            return;
        }
        throw new ArrayIndexOutOfBoundsException("destination too small to fit elements");
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ColorSpaceTransform)) {
            return false;
        }
        ColorSpaceTransform other = (ColorSpaceTransform) obj;
        int i = 0;
        int j = 0;
        while (i < 9) {
            int denominator = this.mElements;
            int numerator = denominator[j + 0];
            denominator = denominator[j + 1];
            int denominatorOther = other.mElements;
            if (!new Rational(numerator, denominator).equals(new Rational(denominatorOther[j + 0], denominatorOther[j + 1]))) {
                return false;
            }
            i++;
            j += 2;
        }
        return true;
    }

    public int hashCode() {
        return HashCodeHelpers.hashCode(this.mElements);
    }

    public String toString() {
        return String.format("ColorSpaceTransform%s", new Object[]{toShortString()});
    }

    private String toShortString() {
        StringBuilder sb = new StringBuilder("(");
        int i = 0;
        for (int row = 0; row < 3; row++) {
            String str;
            sb.append("[");
            int col = 0;
            while (true) {
                str = ", ";
                if (col >= 3) {
                    break;
                }
                int denominator = this.mElements;
                int numerator = denominator[i + 0];
                denominator = denominator[i + 1];
                sb.append(numerator);
                sb.append("/");
                sb.append(denominator);
                if (col < 2) {
                    sb.append(str);
                }
                col++;
                i += 2;
            }
            sb.append("]");
            if (row < 2) {
                sb.append(str);
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
