package android.view.inputmethod;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.SpannedString;
import android.text.TextUtils;
import android.view.inputmethod.SparseRectFArray.SparseRectFArrayBuilder;
import java.util.Arrays;
import java.util.Objects;

public final class CursorAnchorInfo implements Parcelable {
    public static final Creator<CursorAnchorInfo> CREATOR = new Creator<CursorAnchorInfo>() {
        public CursorAnchorInfo createFromParcel(Parcel source) {
            return new CursorAnchorInfo(source);
        }

        public CursorAnchorInfo[] newArray(int size) {
            return new CursorAnchorInfo[size];
        }
    };
    public static final int FLAG_HAS_INVISIBLE_REGION = 2;
    public static final int FLAG_HAS_VISIBLE_REGION = 1;
    public static final int FLAG_IS_RTL = 4;
    private final SparseRectFArray mCharacterBoundsArray;
    private final CharSequence mComposingText;
    private final int mComposingTextStart;
    private final int mHashCode;
    private final float mInsertionMarkerBaseline;
    private final float mInsertionMarkerBottom;
    private final int mInsertionMarkerFlags;
    private final float mInsertionMarkerHorizontal;
    private final float mInsertionMarkerTop;
    private final float[] mMatrixValues;
    private final int mSelectionEnd;
    private final int mSelectionStart;

    public static final class Builder {
        private SparseRectFArrayBuilder mCharacterBoundsArrayBuilder = null;
        private CharSequence mComposingText = null;
        private int mComposingTextStart = -1;
        private float mInsertionMarkerBaseline = Float.NaN;
        private float mInsertionMarkerBottom = Float.NaN;
        private int mInsertionMarkerFlags = 0;
        private float mInsertionMarkerHorizontal = Float.NaN;
        private float mInsertionMarkerTop = Float.NaN;
        private boolean mMatrixInitialized = false;
        private float[] mMatrixValues = null;
        private int mSelectionEnd = -1;
        private int mSelectionStart = -1;

        public Builder setSelectionRange(int newStart, int newEnd) {
            this.mSelectionStart = newStart;
            this.mSelectionEnd = newEnd;
            return this;
        }

        public Builder setComposingText(int composingTextStart, CharSequence composingText) {
            this.mComposingTextStart = composingTextStart;
            if (composingText == null) {
                this.mComposingText = null;
            } else {
                this.mComposingText = new SpannedString(composingText);
            }
            return this;
        }

        public Builder setInsertionMarkerLocation(float horizontalPosition, float lineTop, float lineBaseline, float lineBottom, int flags) {
            this.mInsertionMarkerHorizontal = horizontalPosition;
            this.mInsertionMarkerTop = lineTop;
            this.mInsertionMarkerBaseline = lineBaseline;
            this.mInsertionMarkerBottom = lineBottom;
            this.mInsertionMarkerFlags = flags;
            return this;
        }

        public Builder addCharacterBounds(int index, float left, float top, float right, float bottom, int flags) {
            if (index >= 0) {
                if (this.mCharacterBoundsArrayBuilder == null) {
                    this.mCharacterBoundsArrayBuilder = new SparseRectFArrayBuilder();
                }
                this.mCharacterBoundsArrayBuilder.append(index, left, top, right, bottom, flags);
                return this;
            }
            throw new IllegalArgumentException("index must not be a negative integer.");
        }

        public Builder setMatrix(Matrix matrix) {
            if (this.mMatrixValues == null) {
                this.mMatrixValues = new float[9];
            }
            (matrix != null ? matrix : Matrix.IDENTITY_MATRIX).getValues(this.mMatrixValues);
            this.mMatrixInitialized = true;
            return this;
        }

        public CursorAnchorInfo build() {
            if (!this.mMatrixInitialized) {
                SparseRectFArrayBuilder sparseRectFArrayBuilder = this.mCharacterBoundsArrayBuilder;
                boolean hasCharacterBounds = (sparseRectFArrayBuilder == null || sparseRectFArrayBuilder.isEmpty()) ? false : true;
                if (!(!hasCharacterBounds && Float.isNaN(this.mInsertionMarkerHorizontal) && Float.isNaN(this.mInsertionMarkerTop) && Float.isNaN(this.mInsertionMarkerBaseline) && Float.isNaN(this.mInsertionMarkerBottom))) {
                    throw new IllegalArgumentException("Coordinate transformation matrix is required when positional parameters are specified.");
                }
            }
            return CursorAnchorInfo.create(this);
        }

        public void reset() {
            this.mSelectionStart = -1;
            this.mSelectionEnd = -1;
            this.mComposingTextStart = -1;
            this.mComposingText = null;
            this.mInsertionMarkerFlags = 0;
            this.mInsertionMarkerHorizontal = Float.NaN;
            this.mInsertionMarkerTop = Float.NaN;
            this.mInsertionMarkerBaseline = Float.NaN;
            this.mInsertionMarkerBottom = Float.NaN;
            this.mMatrixInitialized = false;
            SparseRectFArrayBuilder sparseRectFArrayBuilder = this.mCharacterBoundsArrayBuilder;
            if (sparseRectFArrayBuilder != null) {
                sparseRectFArrayBuilder.reset();
            }
        }
    }

    public CursorAnchorInfo(Parcel source) {
        this.mHashCode = source.readInt();
        this.mSelectionStart = source.readInt();
        this.mSelectionEnd = source.readInt();
        this.mComposingTextStart = source.readInt();
        this.mComposingText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
        this.mInsertionMarkerFlags = source.readInt();
        this.mInsertionMarkerHorizontal = source.readFloat();
        this.mInsertionMarkerTop = source.readFloat();
        this.mInsertionMarkerBaseline = source.readFloat();
        this.mInsertionMarkerBottom = source.readFloat();
        this.mCharacterBoundsArray = (SparseRectFArray) source.readParcelable(SparseRectFArray.class.getClassLoader());
        this.mMatrixValues = source.createFloatArray();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mHashCode);
        dest.writeInt(this.mSelectionStart);
        dest.writeInt(this.mSelectionEnd);
        dest.writeInt(this.mComposingTextStart);
        TextUtils.writeToParcel(this.mComposingText, dest, flags);
        dest.writeInt(this.mInsertionMarkerFlags);
        dest.writeFloat(this.mInsertionMarkerHorizontal);
        dest.writeFloat(this.mInsertionMarkerTop);
        dest.writeFloat(this.mInsertionMarkerBaseline);
        dest.writeFloat(this.mInsertionMarkerBottom);
        dest.writeParcelable(this.mCharacterBoundsArray, flags);
        dest.writeFloatArray(this.mMatrixValues);
    }

    public int hashCode() {
        return this.mHashCode;
    }

    private static boolean areSameFloatImpl(float a, float b) {
        boolean z = true;
        if (Float.isNaN(a) && Float.isNaN(b)) {
            return true;
        }
        if (a != b) {
            z = false;
        }
        return z;
    }

    /* JADX WARNING: Missing block: B:44:0x0091, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:45:0x0092, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:46:0x0093, code skipped:
            return false;
     */
    public boolean equals(java.lang.Object r7) {
        /*
        r6 = this;
        r0 = 0;
        if (r7 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = 1;
        if (r6 != r7) goto L_0x0008;
    L_0x0007:
        return r1;
    L_0x0008:
        r2 = r7 instanceof android.view.inputmethod.CursorAnchorInfo;
        if (r2 != 0) goto L_0x000d;
    L_0x000c:
        return r0;
    L_0x000d:
        r2 = r7;
        r2 = (android.view.inputmethod.CursorAnchorInfo) r2;
        r3 = r6.hashCode();
        r4 = r2.hashCode();
        if (r3 == r4) goto L_0x001b;
    L_0x001a:
        return r0;
    L_0x001b:
        r3 = r6.mSelectionStart;
        r4 = r2.mSelectionStart;
        if (r3 != r4) goto L_0x0093;
    L_0x0021:
        r3 = r6.mSelectionEnd;
        r4 = r2.mSelectionEnd;
        if (r3 == r4) goto L_0x0028;
    L_0x0027:
        goto L_0x0093;
    L_0x0028:
        r3 = r6.mInsertionMarkerFlags;
        r4 = r2.mInsertionMarkerFlags;
        if (r3 != r4) goto L_0x0092;
    L_0x002e:
        r3 = r6.mInsertionMarkerHorizontal;
        r4 = r2.mInsertionMarkerHorizontal;
        r3 = areSameFloatImpl(r3, r4);
        if (r3 == 0) goto L_0x0092;
    L_0x0038:
        r3 = r6.mInsertionMarkerTop;
        r4 = r2.mInsertionMarkerTop;
        r3 = areSameFloatImpl(r3, r4);
        if (r3 == 0) goto L_0x0092;
    L_0x0042:
        r3 = r6.mInsertionMarkerBaseline;
        r4 = r2.mInsertionMarkerBaseline;
        r3 = areSameFloatImpl(r3, r4);
        if (r3 == 0) goto L_0x0092;
    L_0x004c:
        r3 = r6.mInsertionMarkerBottom;
        r4 = r2.mInsertionMarkerBottom;
        r3 = areSameFloatImpl(r3, r4);
        if (r3 != 0) goto L_0x0057;
    L_0x0056:
        goto L_0x0092;
    L_0x0057:
        r3 = r6.mCharacterBoundsArray;
        r4 = r2.mCharacterBoundsArray;
        r3 = java.util.Objects.equals(r3, r4);
        if (r3 != 0) goto L_0x0062;
    L_0x0061:
        return r0;
    L_0x0062:
        r3 = r6.mComposingTextStart;
        r4 = r2.mComposingTextStart;
        if (r3 != r4) goto L_0x0091;
    L_0x0068:
        r3 = r6.mComposingText;
        r4 = r2.mComposingText;
        r3 = java.util.Objects.equals(r3, r4);
        if (r3 != 0) goto L_0x0073;
    L_0x0072:
        goto L_0x0091;
    L_0x0073:
        r3 = r6.mMatrixValues;
        r3 = r3.length;
        r4 = r2.mMatrixValues;
        r4 = r4.length;
        if (r3 == r4) goto L_0x007c;
    L_0x007b:
        return r0;
    L_0x007c:
        r3 = 0;
    L_0x007d:
        r4 = r6.mMatrixValues;
        r5 = r4.length;
        if (r3 >= r5) goto L_0x0090;
    L_0x0082:
        r4 = r4[r3];
        r5 = r2.mMatrixValues;
        r5 = r5[r3];
        r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1));
        if (r4 == 0) goto L_0x008d;
    L_0x008c:
        return r0;
    L_0x008d:
        r3 = r3 + 1;
        goto L_0x007d;
    L_0x0090:
        return r1;
    L_0x0091:
        return r0;
    L_0x0092:
        return r0;
    L_0x0093:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.CursorAnchorInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CursorAnchorInfo{mHashCode=");
        stringBuilder.append(this.mHashCode);
        stringBuilder.append(" mSelection=");
        stringBuilder.append(this.mSelectionStart);
        stringBuilder.append(",");
        stringBuilder.append(this.mSelectionEnd);
        stringBuilder.append(" mComposingTextStart=");
        stringBuilder.append(this.mComposingTextStart);
        stringBuilder.append(" mComposingText=");
        stringBuilder.append(Objects.toString(this.mComposingText));
        stringBuilder.append(" mInsertionMarkerFlags=");
        stringBuilder.append(this.mInsertionMarkerFlags);
        stringBuilder.append(" mInsertionMarkerHorizontal=");
        stringBuilder.append(this.mInsertionMarkerHorizontal);
        stringBuilder.append(" mInsertionMarkerTop=");
        stringBuilder.append(this.mInsertionMarkerTop);
        stringBuilder.append(" mInsertionMarkerBaseline=");
        stringBuilder.append(this.mInsertionMarkerBaseline);
        stringBuilder.append(" mInsertionMarkerBottom=");
        stringBuilder.append(this.mInsertionMarkerBottom);
        stringBuilder.append(" mCharacterBoundsArray=");
        stringBuilder.append(Objects.toString(this.mCharacterBoundsArray));
        stringBuilder.append(" mMatrix=");
        stringBuilder.append(Arrays.toString(this.mMatrixValues));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private static CursorAnchorInfo create(Builder builder) {
        SparseRectFArray characterBoundsArray;
        if (builder.mCharacterBoundsArrayBuilder != null) {
            characterBoundsArray = builder.mCharacterBoundsArrayBuilder.build();
        } else {
            characterBoundsArray = null;
        }
        float[] matrixValues = new float[9];
        if (builder.mMatrixInitialized) {
            System.arraycopy(builder.mMatrixValues, 0, matrixValues, 0, 9);
        } else {
            Matrix.IDENTITY_MATRIX.getValues(matrixValues);
        }
        return new CursorAnchorInfo(builder.mSelectionStart, builder.mSelectionEnd, builder.mComposingTextStart, builder.mComposingText, builder.mInsertionMarkerFlags, builder.mInsertionMarkerHorizontal, builder.mInsertionMarkerTop, builder.mInsertionMarkerBaseline, builder.mInsertionMarkerBottom, characterBoundsArray, matrixValues);
    }

    private CursorAnchorInfo(int selectionStart, int selectionEnd, int composingTextStart, CharSequence composingText, int insertionMarkerFlags, float insertionMarkerHorizontal, float insertionMarkerTop, float insertionMarkerBaseline, float insertionMarkerBottom, SparseRectFArray characterBoundsArray, float[] matrixValues) {
        this.mSelectionStart = selectionStart;
        this.mSelectionEnd = selectionEnd;
        this.mComposingTextStart = composingTextStart;
        this.mComposingText = composingText;
        this.mInsertionMarkerFlags = insertionMarkerFlags;
        this.mInsertionMarkerHorizontal = insertionMarkerHorizontal;
        this.mInsertionMarkerTop = insertionMarkerTop;
        this.mInsertionMarkerBaseline = insertionMarkerBaseline;
        this.mInsertionMarkerBottom = insertionMarkerBottom;
        this.mCharacterBoundsArray = characterBoundsArray;
        this.mMatrixValues = matrixValues;
        this.mHashCode = (Objects.hashCode(this.mComposingText) * 31) + Arrays.hashCode(matrixValues);
    }

    public static CursorAnchorInfo createForAdditionalParentMatrix(CursorAnchorInfo original, Matrix parentMatrix) {
        return new CursorAnchorInfo(original.mSelectionStart, original.mSelectionEnd, original.mComposingTextStart, original.mComposingText, original.mInsertionMarkerFlags, original.mInsertionMarkerHorizontal, original.mInsertionMarkerTop, original.mInsertionMarkerBaseline, original.mInsertionMarkerBottom, original.mCharacterBoundsArray, computeMatrixValues(parentMatrix, original));
    }

    private static float[] computeMatrixValues(Matrix parentMatrix, CursorAnchorInfo info) {
        if (parentMatrix.isIdentity()) {
            return info.mMatrixValues;
        }
        Matrix newMatrix = new Matrix();
        newMatrix.setValues(info.mMatrixValues);
        newMatrix.postConcat(parentMatrix);
        float[] matrixValues = new float[9];
        newMatrix.getValues(matrixValues);
        return matrixValues;
    }

    public int getSelectionStart() {
        return this.mSelectionStart;
    }

    public int getSelectionEnd() {
        return this.mSelectionEnd;
    }

    public int getComposingTextStart() {
        return this.mComposingTextStart;
    }

    public CharSequence getComposingText() {
        return this.mComposingText;
    }

    public int getInsertionMarkerFlags() {
        return this.mInsertionMarkerFlags;
    }

    public float getInsertionMarkerHorizontal() {
        return this.mInsertionMarkerHorizontal;
    }

    public float getInsertionMarkerTop() {
        return this.mInsertionMarkerTop;
    }

    public float getInsertionMarkerBaseline() {
        return this.mInsertionMarkerBaseline;
    }

    public float getInsertionMarkerBottom() {
        return this.mInsertionMarkerBottom;
    }

    public RectF getCharacterBounds(int index) {
        SparseRectFArray sparseRectFArray = this.mCharacterBoundsArray;
        if (sparseRectFArray == null) {
            return null;
        }
        return sparseRectFArray.get(index);
    }

    public int getCharacterBoundsFlags(int index) {
        SparseRectFArray sparseRectFArray = this.mCharacterBoundsArray;
        if (sparseRectFArray == null) {
            return 0;
        }
        return sparseRectFArray.getFlags(index, 0);
    }

    public Matrix getMatrix() {
        Matrix matrix = new Matrix();
        matrix.setValues(this.mMatrixValues);
        return matrix;
    }

    public int describeContents() {
        return 0;
    }
}
