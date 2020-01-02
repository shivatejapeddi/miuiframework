package android.net.wifi.hotspot2;

import android.annotation.SystemApi;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.net.wifi.WifiSsid;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@SystemApi
public final class OsuProvider implements Parcelable {
    public static final Creator<OsuProvider> CREATOR = new Creator<OsuProvider>() {
        public OsuProvider createFromParcel(Parcel in) {
            Parcel parcel = in;
            WifiSsid osuSsid = (WifiSsid) parcel.readParcelable(null);
            String serviceDescription = in.readString();
            Uri serverUri = (Uri) parcel.readParcelable(null);
            String nai = in.readString();
            List methodList = new ArrayList();
            parcel.readList(methodList, null);
            return new OsuProvider(osuSsid, (HashMap) in.readBundle().getSerializable("friendlyNameMap"), serviceDescription, serverUri, nai, methodList, (Icon) parcel.readParcelable(null));
        }

        public OsuProvider[] newArray(int size) {
            return new OsuProvider[size];
        }
    };
    public static final int METHOD_OMA_DM = 0;
    public static final int METHOD_SOAP_XML_SPP = 1;
    private final Map<String, String> mFriendlyNames;
    private final Icon mIcon;
    private final List<Integer> mMethodList;
    private final String mNetworkAccessIdentifier;
    private WifiSsid mOsuSsid;
    private final Uri mServerUri;
    private final String mServiceDescription;

    public OsuProvider(WifiSsid osuSsid, Map<String, String> friendlyNames, String serviceDescription, Uri serverUri, String nai, List<Integer> methodList, Icon icon) {
        this.mOsuSsid = osuSsid;
        this.mFriendlyNames = friendlyNames;
        this.mServiceDescription = serviceDescription;
        this.mServerUri = serverUri;
        this.mNetworkAccessIdentifier = nai;
        if (methodList == null) {
            this.mMethodList = new ArrayList();
        } else {
            this.mMethodList = new ArrayList(methodList);
        }
        this.mIcon = icon;
    }

    public OsuProvider(OsuProvider source) {
        if (source == null) {
            this.mOsuSsid = null;
            this.mFriendlyNames = null;
            this.mServiceDescription = null;
            this.mServerUri = null;
            this.mNetworkAccessIdentifier = null;
            this.mMethodList = new ArrayList();
            this.mIcon = null;
            return;
        }
        this.mOsuSsid = source.mOsuSsid;
        this.mFriendlyNames = source.mFriendlyNames;
        this.mServiceDescription = source.mServiceDescription;
        this.mServerUri = source.mServerUri;
        this.mNetworkAccessIdentifier = source.mNetworkAccessIdentifier;
        List list = source.mMethodList;
        if (list == null) {
            this.mMethodList = new ArrayList();
        } else {
            this.mMethodList = new ArrayList(list);
        }
        this.mIcon = source.mIcon;
    }

    public WifiSsid getOsuSsid() {
        return this.mOsuSsid;
    }

    public void setOsuSsid(WifiSsid osuSsid) {
        this.mOsuSsid = osuSsid;
    }

    public String getFriendlyName() {
        Map map = this.mFriendlyNames;
        if (map == null || map.isEmpty()) {
            return null;
        }
        String friendlyName = (String) this.mFriendlyNames.get(Locale.getDefault().getLanguage());
        if (friendlyName != null) {
            return friendlyName;
        }
        friendlyName = (String) this.mFriendlyNames.get("en");
        if (friendlyName != null) {
            return friendlyName;
        }
        Map map2 = this.mFriendlyNames;
        return (String) map2.get(map2.keySet().stream().findFirst().get());
    }

    public Map<String, String> getFriendlyNameList() {
        return this.mFriendlyNames;
    }

    public String getServiceDescription() {
        return this.mServiceDescription;
    }

    public Uri getServerUri() {
        return this.mServerUri;
    }

    public String getNetworkAccessIdentifier() {
        return this.mNetworkAccessIdentifier;
    }

    public List<Integer> getMethodList() {
        return this.mMethodList;
    }

    public Icon getIcon() {
        return this.mIcon;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mOsuSsid, flags);
        dest.writeString(this.mServiceDescription);
        dest.writeParcelable(this.mServerUri, flags);
        dest.writeString(this.mNetworkAccessIdentifier);
        dest.writeList(this.mMethodList);
        dest.writeParcelable(this.mIcon, flags);
        Bundle bundle = new Bundle();
        bundle.putSerializable("friendlyNameMap", (HashMap) this.mFriendlyNames);
        dest.writeBundle(bundle);
    }

    public boolean equals(Object thatObject) {
        boolean z = true;
        if (this == thatObject) {
            return true;
        }
        if (!(thatObject instanceof OsuProvider)) {
            return false;
        }
        OsuProvider that = (OsuProvider) thatObject;
        WifiSsid wifiSsid = this.mOsuSsid;
        if (wifiSsid != null ? !wifiSsid.equals(that.mOsuSsid) : that.mOsuSsid != null) {
            if (this.mFriendlyNames == null) {
                if (that.mFriendlyNames != null) {
                    z = false;
                }
                return z;
            }
        }
        if (this.mFriendlyNames.equals(that.mFriendlyNames) && TextUtils.equals(this.mServiceDescription, that.mServiceDescription)) {
            Uri uri = this.mServerUri;
            if (uri != null ? !uri.equals(that.mServerUri) : that.mServerUri != null) {
                if (TextUtils.equals(this.mNetworkAccessIdentifier, that.mNetworkAccessIdentifier)) {
                    List list = this.mMethodList;
                    if (list != null ? !list.equals(that.mMethodList) : that.mMethodList != null) {
                        Icon icon = this.mIcon;
                        if (icon != null) {
                        }
                    }
                }
            }
        }
        z = false;
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mOsuSsid, this.mServiceDescription, this.mFriendlyNames, this.mServerUri, this.mNetworkAccessIdentifier, this.mMethodList});
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("OsuProvider{mOsuSsid=");
        stringBuilder.append(this.mOsuSsid);
        stringBuilder.append(" mFriendlyNames=");
        stringBuilder.append(this.mFriendlyNames);
        stringBuilder.append(" mServiceDescription=");
        stringBuilder.append(this.mServiceDescription);
        stringBuilder.append(" mServerUri=");
        stringBuilder.append(this.mServerUri);
        stringBuilder.append(" mNetworkAccessIdentifier=");
        stringBuilder.append(this.mNetworkAccessIdentifier);
        stringBuilder.append(" mMethodList=");
        stringBuilder.append(this.mMethodList);
        stringBuilder.append(" mIcon=");
        stringBuilder.append(this.mIcon);
        return stringBuilder.toString();
    }
}
