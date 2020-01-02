package com.miui.whetstone;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.json.JSONException;
import org.json.JSONObject;

public class WhetstoneAction implements Parcelable {
    public static final Creator<WhetstoneAction> CREATOR = new Creator<WhetstoneAction>() {
        public WhetstoneAction createFromParcel(Parcel source) {
            return new WhetstoneAction(source, null);
        }

        public WhetstoneAction[] newArray(int size) {
            return new WhetstoneAction[size];
        }
    };
    private String DT;
    private JSONObject content;

    public WhetstoneAction(String dt, JSONObject jb) {
        this.DT = dt;
        this.content = jb;
    }

    public String getKey() {
        return this.DT;
    }

    public JSONObject getContent() {
        return this.content;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.DT);
        dest.writeString(this.content.toString());
    }

    private WhetstoneAction(Parcel in) {
        this.DT = in.readString();
        this.content = convertStringToJson(in.readString());
    }

    private JSONObject convertStringToJson(String str) {
        try {
            return new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
