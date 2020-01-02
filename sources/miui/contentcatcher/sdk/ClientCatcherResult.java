package miui.contentcatcher.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ClientCatcherResult implements Parcelable {
    public static final Creator<ClientCatcherResult> CREATOR = new Creator<ClientCatcherResult>() {
        public ClientCatcherResult createFromParcel(Parcel in) {
            return new ClientCatcherResult(in, null);
        }

        public ClientCatcherResult[] newArray(int size) {
            return new ClientCatcherResult[size];
        }
    };
    private Token mInjectorToken;
    private Map<String, Object> mPropertyMap;
    private Map<String, Map<String, Object>> mResults;

    /* synthetic */ ClientCatcherResult(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public ClientCatcherResult() {
        this.mResults = new HashMap();
        this.mPropertyMap = new HashMap();
    }

    public Token getInjectorToken() {
        return this.mInjectorToken;
    }

    public void setInjectorToken(Token injectorToken) {
        this.mInjectorToken = injectorToken;
    }

    public Map<String, Map<String, Object>> getResults() {
        return this.mResults;
    }

    public Map<String, Object> getPropertyMap() {
        return this.mPropertyMap;
    }

    public Map<String, Object> getParams(String jobTag) {
        return (Map) this.mResults.get(jobTag);
    }

    public void addContent(String jobTag, String key, Object value) {
        if (!this.mResults.containsKey(jobTag)) {
            this.mResults.put(jobTag, new HashMap());
        }
        ((Map) this.mResults.get(jobTag)).put(key, value);
    }

    public void addProperty(String key, Object value) {
        this.mPropertyMap.put(key, value);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(this.mInjectorToken, flags);
        parcel.writeMap(this.mResults);
        parcel.writeMap(this.mPropertyMap);
    }

    private ClientCatcherResult(Parcel parcel) {
        this.mResults = new HashMap();
        this.mPropertyMap = new HashMap();
        this.mInjectorToken = (Token) parcel.readParcelable(ClassLoader.getSystemClassLoader());
        this.mResults = parcel.readHashMap(ClassLoader.getSystemClassLoader());
        this.mPropertyMap = parcel.readHashMap(ClassLoader.getSystemClassLoader());
    }

    public String toString() {
        String str;
        StringBuffer buff = new StringBuffer();
        buff.append("result {");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("injector token : ");
        stringBuilder.append(this.mInjectorToken.toString());
        String str2 = "\n";
        stringBuilder.append(str2);
        buff.append(stringBuilder.toString());
        Iterator it = this.mResults.entrySet().iterator();
        while (true) {
            str = "}";
            if (!it.hasNext()) {
                break;
            }
            Entry<String, Map<String, Object>> entry = (Entry) it.next();
            String jobTag = (String) entry.getKey();
            HashMap<String, Object> entryValue = (HashMap) entry.getValue();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("jobTag : ");
            stringBuilder2.append(jobTag == null ? "null" : jobTag);
            stringBuilder2.append(str2);
            buff.append(stringBuilder2.toString());
            buff.append("params is : {");
            for (Entry<String, Object> param : entryValue.entrySet()) {
                String key = (String) param.getKey();
                Object value = param.getValue();
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("key :");
                stringBuilder3.append(key);
                stringBuilder3.append(" value : ");
                stringBuilder3.append(value);
                buff.append(stringBuilder3.toString());
            }
            buff.append(str);
        }
        buff.append(str);
        buff.append("; property{");
        for (Entry<String, Object> propertyEntry : this.mPropertyMap.entrySet()) {
            String propertyKey = (String) propertyEntry.getKey();
            Object propertyValue = propertyEntry.getValue();
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append("propertykey:");
            stringBuilder4.append(propertyKey);
            stringBuilder4.append(",propertyValue:");
            stringBuilder4.append(propertyValue);
            buff.append(stringBuilder4.toString());
            buff.append("; ");
        }
        buff.append(str);
        return buff.toString();
    }
}
