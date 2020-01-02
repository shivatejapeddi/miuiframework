package android.os.statistics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public final class E2EScenario implements Parcelable, Serializable {
    public static final Creator<E2EScenario> CREATOR = new Creator<E2EScenario>() {
        public E2EScenario createFromParcel(Parcel source) {
            return new E2EScenario(source);
        }

        public E2EScenario[] newArray(int size) {
            return new E2EScenario[size];
        }
    };
    public static final String DEFAULT_CATEGORY = "default";
    private static final long serialVersionUID = 1;
    public String category;
    public String name;
    public String namespace;

    protected E2EScenario() {
        String str = "";
        this.namespace = str;
        this.category = str;
        this.name = str;
    }

    public E2EScenario(String namespace, String category, String name) {
        String str = "";
        this.namespace = namespace == null ? str : namespace;
        this.category = category == null ? str : category;
        if (name != null) {
            str = name;
        }
        this.name = str;
    }

    public E2EScenario(Parcel source) {
        String _namespace = source.readString();
        String _category = source.readString();
        String _name = source.readString();
        String str = "";
        this.namespace = _namespace == null ? str : _namespace;
        this.category = _category == null ? str : _category;
        if (_name != null) {
            str = _name;
        }
        this.name = str;
    }

    public void normalize(String packageName) {
        if (TextUtils.isEmpty(this.namespace)) {
            this.namespace = TextUtils.isEmpty(packageName) ? "android" : packageName;
        }
        if (TextUtils.isEmpty(this.category)) {
            this.category = "default";
        }
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null || !(obj instanceof E2EScenario)) {
            return false;
        }
        E2EScenario other = (E2EScenario) obj;
        if (this.namespace.equals(other.namespace) && this.category.equals(other.category) && this.name.equals(other.name)) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return (this.namespace.hashCode() + this.category.hashCode()) + this.name.hashCode();
    }

    public boolean isValid() {
        return TextUtils.isEmpty(this.name) ^ 1;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.namespace);
        dest.writeString(this.category);
        dest.writeString(this.name);
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("namespace", this.namespace);
            json.put("category", this.category);
            json.put("name", this.name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
