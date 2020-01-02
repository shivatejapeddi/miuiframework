package android.content;

import android.annotation.UnsupportedAppUsage;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

public final class ContentValues implements Parcelable {
    public static final Creator<ContentValues> CREATOR = new Creator<ContentValues>() {
        public ContentValues createFromParcel(Parcel in) {
            return new ContentValues(in, null);
        }

        public ContentValues[] newArray(int size) {
            return new ContentValues[size];
        }
    };
    public static final String TAG = "ContentValues";
    private final ArrayMap<String, Object> mMap;
    @Deprecated
    @UnsupportedAppUsage
    private HashMap<String, Object> mValues;

    /* synthetic */ ContentValues(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public ContentValues() {
        this.mMap = new ArrayMap();
    }

    public ContentValues(int size) {
        Preconditions.checkArgumentNonnegative(size);
        this.mMap = new ArrayMap(size);
    }

    public ContentValues(ContentValues from) {
        Objects.requireNonNull(from);
        this.mMap = new ArrayMap(from.mMap);
    }

    @Deprecated
    @UnsupportedAppUsage
    private ContentValues(HashMap<String, Object> from) {
        this.mMap = new ArrayMap();
        this.mMap.putAll((Map) from);
    }

    private ContentValues(Parcel in) {
        this.mMap = new ArrayMap(in.readInt());
        in.readArrayMap(this.mMap, null);
    }

    public boolean equals(Object object) {
        if (object instanceof ContentValues) {
            return this.mMap.equals(((ContentValues) object).mMap);
        }
        return false;
    }

    public ArrayMap<String, Object> getValues() {
        return this.mMap;
    }

    public int hashCode() {
        return this.mMap.hashCode();
    }

    public void put(String key, String value) {
        this.mMap.put(key, value);
    }

    public void putAll(ContentValues other) {
        this.mMap.putAll(other.mMap);
    }

    public void put(String key, Byte value) {
        this.mMap.put(key, value);
    }

    public void put(String key, Short value) {
        this.mMap.put(key, value);
    }

    public void put(String key, Integer value) {
        this.mMap.put(key, value);
    }

    public void put(String key, Long value) {
        this.mMap.put(key, value);
    }

    public void put(String key, Float value) {
        this.mMap.put(key, value);
    }

    public void put(String key, Double value) {
        this.mMap.put(key, value);
    }

    public void put(String key, Boolean value) {
        this.mMap.put(key, value);
    }

    public void put(String key, byte[] value) {
        this.mMap.put(key, value);
    }

    public void putNull(String key) {
        this.mMap.put(key, null);
    }

    public int size() {
        return this.mMap.size();
    }

    public boolean isEmpty() {
        return this.mMap.isEmpty();
    }

    public void remove(String key) {
        this.mMap.remove(key);
    }

    public void clear() {
        this.mMap.clear();
    }

    public boolean containsKey(String key) {
        return this.mMap.containsKey(key);
    }

    public Object get(String key) {
        return this.mMap.get(key);
    }

    public String getAsString(String key) {
        Object value = this.mMap.get(key);
        return value != null ? value.toString() : null;
    }

    public Long getAsLong(String key) {
        Object value = this.mMap.get(key);
        Long l = null;
        if (value != null) {
            try {
                l = Long.valueOf(((Number) value).longValue());
            } catch (ClassCastException e) {
                boolean z = value instanceof CharSequence;
                String str = TAG;
                if (z) {
                    try {
                        l = Long.valueOf(value.toString());
                        return l;
                    } catch (NumberFormatException e2) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Cannot parse Long value for ");
                        stringBuilder.append(value);
                        stringBuilder.append(" at key ");
                        stringBuilder.append(key);
                        Log.e(str, stringBuilder.toString());
                        return l;
                    }
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Cannot cast value for ");
                stringBuilder2.append(key);
                stringBuilder2.append(" to a Long: ");
                stringBuilder2.append(value);
                Log.e(str, stringBuilder2.toString(), e);
                return l;
            }
        }
        return l;
    }

    public Integer getAsInteger(String key) {
        Object value = this.mMap.get(key);
        Integer num = null;
        if (value != null) {
            try {
                num = Integer.valueOf(((Number) value).intValue());
            } catch (ClassCastException e) {
                boolean z = value instanceof CharSequence;
                String str = TAG;
                if (z) {
                    try {
                        num = Integer.valueOf(value.toString());
                        return num;
                    } catch (NumberFormatException e2) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Cannot parse Integer value for ");
                        stringBuilder.append(value);
                        stringBuilder.append(" at key ");
                        stringBuilder.append(key);
                        Log.e(str, stringBuilder.toString());
                        return num;
                    }
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Cannot cast value for ");
                stringBuilder2.append(key);
                stringBuilder2.append(" to a Integer: ");
                stringBuilder2.append(value);
                Log.e(str, stringBuilder2.toString(), e);
                return num;
            }
        }
        return num;
    }

    public Short getAsShort(String key) {
        Object value = this.mMap.get(key);
        Short sh = null;
        if (value != null) {
            try {
                sh = Short.valueOf(((Number) value).shortValue());
            } catch (ClassCastException e) {
                boolean z = value instanceof CharSequence;
                String str = TAG;
                if (z) {
                    try {
                        sh = Short.valueOf(value.toString());
                        return sh;
                    } catch (NumberFormatException e2) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Cannot parse Short value for ");
                        stringBuilder.append(value);
                        stringBuilder.append(" at key ");
                        stringBuilder.append(key);
                        Log.e(str, stringBuilder.toString());
                        return sh;
                    }
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Cannot cast value for ");
                stringBuilder2.append(key);
                stringBuilder2.append(" to a Short: ");
                stringBuilder2.append(value);
                Log.e(str, stringBuilder2.toString(), e);
                return sh;
            }
        }
        return sh;
    }

    public Byte getAsByte(String key) {
        Object value = this.mMap.get(key);
        Byte b = null;
        if (value != null) {
            try {
                b = Byte.valueOf(((Number) value).byteValue());
            } catch (ClassCastException e) {
                boolean z = value instanceof CharSequence;
                String str = TAG;
                if (z) {
                    try {
                        b = Byte.valueOf(value.toString());
                        return b;
                    } catch (NumberFormatException e2) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Cannot parse Byte value for ");
                        stringBuilder.append(value);
                        stringBuilder.append(" at key ");
                        stringBuilder.append(key);
                        Log.e(str, stringBuilder.toString());
                        return b;
                    }
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Cannot cast value for ");
                stringBuilder2.append(key);
                stringBuilder2.append(" to a Byte: ");
                stringBuilder2.append(value);
                Log.e(str, stringBuilder2.toString(), e);
                return b;
            }
        }
        return b;
    }

    public Double getAsDouble(String key) {
        Object value = this.mMap.get(key);
        Double d = null;
        if (value != null) {
            try {
                d = Double.valueOf(((Number) value).doubleValue());
            } catch (ClassCastException e) {
                boolean z = value instanceof CharSequence;
                String str = TAG;
                if (z) {
                    try {
                        d = Double.valueOf(value.toString());
                        return d;
                    } catch (NumberFormatException e2) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Cannot parse Double value for ");
                        stringBuilder.append(value);
                        stringBuilder.append(" at key ");
                        stringBuilder.append(key);
                        Log.e(str, stringBuilder.toString());
                        return d;
                    }
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Cannot cast value for ");
                stringBuilder2.append(key);
                stringBuilder2.append(" to a Double: ");
                stringBuilder2.append(value);
                Log.e(str, stringBuilder2.toString(), e);
                return d;
            }
        }
        return d;
    }

    public Float getAsFloat(String key) {
        Object value = this.mMap.get(key);
        Float f = null;
        if (value != null) {
            try {
                f = Float.valueOf(((Number) value).floatValue());
            } catch (ClassCastException e) {
                boolean z = value instanceof CharSequence;
                String str = TAG;
                if (z) {
                    try {
                        f = Float.valueOf(value.toString());
                        return f;
                    } catch (NumberFormatException e2) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Cannot parse Float value for ");
                        stringBuilder.append(value);
                        stringBuilder.append(" at key ");
                        stringBuilder.append(key);
                        Log.e(str, stringBuilder.toString());
                        return f;
                    }
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Cannot cast value for ");
                stringBuilder2.append(key);
                stringBuilder2.append(" to a Float: ");
                stringBuilder2.append(value);
                Log.e(str, stringBuilder2.toString(), e);
                return f;
            }
        }
        return f;
    }

    public Boolean getAsBoolean(String key) {
        Object value = this.mMap.get(key);
        try {
            return (Boolean) value;
        } catch (ClassCastException e) {
            boolean z = false;
            if (value instanceof CharSequence) {
                if (Boolean.valueOf(value.toString()).booleanValue() || "1".equals(value)) {
                    z = true;
                }
                return Boolean.valueOf(z);
            } else if (value instanceof Number) {
                if (((Number) value).intValue() != 0) {
                    z = true;
                }
                return Boolean.valueOf(z);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Cannot cast value for ");
                stringBuilder.append(key);
                stringBuilder.append(" to a Boolean: ");
                stringBuilder.append(value);
                Log.e(TAG, stringBuilder.toString(), e);
                return null;
            }
        }
    }

    public byte[] getAsByteArray(String key) {
        Object value = this.mMap.get(key);
        if (value instanceof byte[]) {
            return (byte[]) value;
        }
        return null;
    }

    public Set<Entry<String, Object>> valueSet() {
        return this.mMap.entrySet();
    }

    public Set<String> keySet() {
        return this.mMap.keySet();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.mMap.size());
        parcel.writeArrayMap(this.mMap);
    }

    @Deprecated
    @UnsupportedAppUsage
    public void putStringArrayList(String key, ArrayList<String> value) {
        this.mMap.put(key, value);
    }

    @Deprecated
    @UnsupportedAppUsage
    public ArrayList<String> getStringArrayList(String key) {
        return (ArrayList) this.mMap.get(key);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String name : this.mMap.keySet()) {
            String value = getAsString(name);
            if (sb.length() > 0) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(name);
            stringBuilder.append("=");
            stringBuilder.append(value);
            sb.append(stringBuilder.toString());
        }
        return sb.toString();
    }
}
