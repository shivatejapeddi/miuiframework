package android.filterfw.core;

import android.annotation.UnsupportedAppUsage;
import android.app.slice.SliceItem;
import android.net.wifi.WifiEnterpriseConfig;
import com.miui.enterprise.sdk.ApplicationManager;
import java.util.Arrays;
import java.util.Map.Entry;

public class FrameFormat {
    public static final int BYTES_PER_SAMPLE_UNSPECIFIED = 1;
    protected static final int SIZE_UNKNOWN = -1;
    public static final int SIZE_UNSPECIFIED = 0;
    public static final int TARGET_GPU = 3;
    public static final int TARGET_NATIVE = 2;
    public static final int TARGET_RS = 5;
    public static final int TARGET_SIMPLE = 1;
    public static final int TARGET_UNSPECIFIED = 0;
    public static final int TARGET_VERTEXBUFFER = 4;
    public static final int TYPE_BIT = 1;
    public static final int TYPE_BYTE = 2;
    public static final int TYPE_DOUBLE = 6;
    public static final int TYPE_FLOAT = 5;
    public static final int TYPE_INT16 = 3;
    public static final int TYPE_INT32 = 4;
    public static final int TYPE_OBJECT = 8;
    public static final int TYPE_POINTER = 7;
    public static final int TYPE_UNSPECIFIED = 0;
    protected int mBaseType = 0;
    protected int mBytesPerSample = 1;
    protected int[] mDimensions;
    protected KeyValueMap mMetaData;
    protected Class mObjectClass;
    protected int mSize = -1;
    protected int mTarget = 0;

    protected FrameFormat() {
    }

    public FrameFormat(int baseType, int target) {
        this.mBaseType = baseType;
        this.mTarget = target;
        initDefaults();
    }

    public static FrameFormat unspecified() {
        return new FrameFormat(0, 0);
    }

    public int getBaseType() {
        return this.mBaseType;
    }

    public boolean isBinaryDataType() {
        int i = this.mBaseType;
        return i >= 1 && i <= 6;
    }

    public int getBytesPerSample() {
        return this.mBytesPerSample;
    }

    public int getValuesPerSample() {
        return this.mBytesPerSample / bytesPerSampleOf(this.mBaseType);
    }

    @UnsupportedAppUsage
    public int getTarget() {
        return this.mTarget;
    }

    public int[] getDimensions() {
        return this.mDimensions;
    }

    public int getDimension(int i) {
        return this.mDimensions[i];
    }

    public int getDimensionCount() {
        int[] iArr = this.mDimensions;
        return iArr == null ? 0 : iArr.length;
    }

    public boolean hasMetaKey(String key) {
        KeyValueMap keyValueMap = this.mMetaData;
        return keyValueMap != null ? keyValueMap.containsKey(key) : false;
    }

    public boolean hasMetaKey(String key, Class expectedClass) {
        KeyValueMap keyValueMap = this.mMetaData;
        if (keyValueMap == null || !keyValueMap.containsKey(key)) {
            return false;
        }
        if (expectedClass.isAssignableFrom(this.mMetaData.get(key).getClass())) {
            return true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FrameFormat meta-key '");
        stringBuilder.append(key);
        stringBuilder.append("' is of type ");
        stringBuilder.append(this.mMetaData.get(key).getClass());
        stringBuilder.append(" but expected to be of type ");
        stringBuilder.append(expectedClass);
        stringBuilder.append("!");
        throw new RuntimeException(stringBuilder.toString());
    }

    public Object getMetaValue(String key) {
        KeyValueMap keyValueMap = this.mMetaData;
        return keyValueMap != null ? keyValueMap.get(key) : null;
    }

    public int getNumberOfDimensions() {
        int[] iArr = this.mDimensions;
        return iArr != null ? iArr.length : 0;
    }

    public int getLength() {
        int[] iArr = this.mDimensions;
        return (iArr == null || iArr.length < 1) ? -1 : iArr[0];
    }

    @UnsupportedAppUsage
    public int getWidth() {
        return getLength();
    }

    @UnsupportedAppUsage
    public int getHeight() {
        int[] iArr = this.mDimensions;
        return (iArr == null || iArr.length < 2) ? -1 : iArr[1];
    }

    public int getDepth() {
        int[] iArr = this.mDimensions;
        return (iArr == null || iArr.length < 3) ? -1 : iArr[2];
    }

    public int getSize() {
        if (this.mSize == -1) {
            this.mSize = calcSize(this.mDimensions);
        }
        return this.mSize;
    }

    public Class getObjectClass() {
        return this.mObjectClass;
    }

    @UnsupportedAppUsage
    public MutableFrameFormat mutableCopy() {
        MutableFrameFormat result = new MutableFrameFormat();
        result.setBaseType(getBaseType());
        result.setTarget(getTarget());
        result.setBytesPerSample(getBytesPerSample());
        result.setDimensions(getDimensions());
        result.setObjectClass(getObjectClass());
        KeyValueMap keyValueMap = this.mMetaData;
        result.mMetaData = keyValueMap == null ? null : (KeyValueMap) keyValueMap.clone();
        return result;
    }

    public boolean equals(Object object) {
        boolean z = true;
        if (this == object) {
            return true;
        }
        if (!(object instanceof FrameFormat)) {
            return false;
        }
        FrameFormat format = (FrameFormat) object;
        if (!(format.mBaseType == this.mBaseType && format.mTarget == this.mTarget && format.mBytesPerSample == this.mBytesPerSample && Arrays.equals(format.mDimensions, this.mDimensions) && format.mMetaData.equals(this.mMetaData))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return ((this.mBaseType ^ 4211) ^ this.mBytesPerSample) ^ getSize();
    }

    public boolean isCompatibleWith(FrameFormat specification) {
        if (specification.getBaseType() != 0 && getBaseType() != specification.getBaseType()) {
            return false;
        }
        if (specification.getTarget() != 0 && getTarget() != specification.getTarget()) {
            return false;
        }
        if (specification.getBytesPerSample() != 1 && getBytesPerSample() != specification.getBytesPerSample()) {
            return false;
        }
        if (specification.getDimensionCount() > 0 && getDimensionCount() != specification.getDimensionCount()) {
            return false;
        }
        int i = 0;
        while (i < specification.getDimensionCount()) {
            int specDim = specification.getDimension(i);
            if (specDim != 0 && getDimension(i) != specDim) {
                return false;
            }
            i++;
        }
        if (specification.getObjectClass() != null && (getObjectClass() == null || !specification.getObjectClass().isAssignableFrom(getObjectClass()))) {
            return false;
        }
        KeyValueMap keyValueMap = specification.mMetaData;
        if (keyValueMap != null) {
            for (String specKey : keyValueMap.keySet()) {
                KeyValueMap keyValueMap2 = this.mMetaData;
                if (keyValueMap2 == null || !keyValueMap2.containsKey(specKey) || !this.mMetaData.get(specKey).equals(specification.mMetaData.get(specKey))) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean mayBeCompatibleWith(FrameFormat specification) {
        if (specification.getBaseType() != 0 && getBaseType() != 0 && getBaseType() != specification.getBaseType()) {
            return false;
        }
        if (specification.getTarget() != 0 && getTarget() != 0 && getTarget() != specification.getTarget()) {
            return false;
        }
        if (specification.getBytesPerSample() != 1 && getBytesPerSample() != 1 && getBytesPerSample() != specification.getBytesPerSample()) {
            return false;
        }
        if (specification.getDimensionCount() > 0 && getDimensionCount() > 0 && getDimensionCount() != specification.getDimensionCount()) {
            return false;
        }
        int i = 0;
        while (i < specification.getDimensionCount()) {
            int specDim = specification.getDimension(i);
            if (specDim != 0 && getDimension(i) != 0 && getDimension(i) != specDim) {
                return false;
            }
            i++;
        }
        if (specification.getObjectClass() != null && getObjectClass() != null && !specification.getObjectClass().isAssignableFrom(getObjectClass())) {
            return false;
        }
        KeyValueMap keyValueMap = specification.mMetaData;
        if (!(keyValueMap == null || this.mMetaData == null)) {
            for (String specKey : keyValueMap.keySet()) {
                if (this.mMetaData.containsKey(specKey) && !this.mMetaData.get(specKey).equals(specification.mMetaData.get(specKey))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int bytesPerSampleOf(int baseType) {
        switch (baseType) {
            case 1:
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
            case 5:
            case 7:
                return 4;
            case 6:
                return 8;
            default:
                return 1;
        }
    }

    public static String dimensionsToString(int[] dimensions) {
        StringBuffer buffer = new StringBuffer();
        if (dimensions != null) {
            int n = dimensions.length;
            for (int i = 0; i < n; i++) {
                if (dimensions[i] == 0) {
                    buffer.append("[]");
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("[");
                    stringBuilder.append(String.valueOf(dimensions[i]));
                    stringBuilder.append("]");
                    buffer.append(stringBuilder.toString());
                }
            }
        }
        return buffer.toString();
    }

    public static String baseTypeToString(int baseType) {
        String str = SliceItem.FORMAT_INT;
        switch (baseType) {
            case 0:
                return "unspecified";
            case 1:
                return "bit";
            case 2:
                return "byte";
            case 3:
                return str;
            case 4:
                return str;
            case 5:
                return ApplicationManager.FLOAT;
            case 6:
                return "double";
            case 7:
                return "pointer";
            case 8:
                return "object";
            default:
                return "unknown";
        }
    }

    public static String targetToString(int target) {
        if (target == 0) {
            return "unspecified";
        }
        if (target == 1) {
            return "simple";
        }
        if (target == 2) {
            return "native";
        }
        if (target == 3) {
            return "gpu";
        }
        if (target == 4) {
            return "vbo";
        }
        if (target != 5) {
            return "unknown";
        }
        return "renderscript";
    }

    public static String metaDataToString(KeyValueMap metaData) {
        if (metaData == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        for (Entry<String, Object> entry : metaData.entrySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String) entry.getKey());
            stringBuilder.append(": ");
            stringBuilder.append(entry.getValue());
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            buffer.append(stringBuilder.toString());
        }
        buffer.append("}");
        return buffer.toString();
    }

    public static int readTargetString(String targetString) {
        if (targetString.equalsIgnoreCase("CPU") || targetString.equalsIgnoreCase("NATIVE")) {
            return 2;
        }
        if (targetString.equalsIgnoreCase("GPU")) {
            return 3;
        }
        if (targetString.equalsIgnoreCase("SIMPLE")) {
            return 1;
        }
        if (targetString.equalsIgnoreCase("VERTEXBUFFER")) {
            return 4;
        }
        if (targetString.equalsIgnoreCase("UNSPECIFIED")) {
            return 0;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown target type '");
        stringBuilder.append(targetString);
        stringBuilder.append("'!");
        throw new RuntimeException(stringBuilder.toString());
    }

    public String toString() {
        String targetString;
        int valuesPerSample = getValuesPerSample();
        String classString = "";
        String sampleCountString = valuesPerSample == 1 ? classString : String.valueOf(valuesPerSample);
        if (this.mTarget == 0) {
            targetString = classString;
        } else {
            targetString = new StringBuilder();
            targetString.append(targetToString(this.mTarget));
            targetString.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            targetString = targetString.toString();
        }
        if (this.mObjectClass != null) {
            classString = new StringBuilder();
            classString.append(" class(");
            classString.append(this.mObjectClass.getSimpleName());
            classString.append(") ");
            classString = classString.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(targetString);
        stringBuilder.append(baseTypeToString(this.mBaseType));
        stringBuilder.append(sampleCountString);
        stringBuilder.append(dimensionsToString(this.mDimensions));
        stringBuilder.append(classString);
        stringBuilder.append(metaDataToString(this.mMetaData));
        return stringBuilder.toString();
    }

    private void initDefaults() {
        this.mBytesPerSample = bytesPerSampleOf(this.mBaseType);
    }

    /* Access modifiers changed, original: 0000 */
    public int calcSize(int[] dimensions) {
        int i = 0;
        if (dimensions == null || dimensions.length <= 0) {
            return 0;
        }
        int size = getBytesPerSample();
        while (i < dimensions.length) {
            size *= dimensions[i];
            i++;
        }
        return size;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isReplaceableBy(FrameFormat format) {
        return this.mTarget == format.mTarget && getSize() == format.getSize() && Arrays.equals(format.mDimensions, this.mDimensions);
    }
}
