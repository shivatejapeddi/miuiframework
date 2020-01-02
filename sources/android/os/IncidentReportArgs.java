package android.os;

import android.annotation.SystemApi;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcelable.Creator;
import android.util.IntArray;
import java.util.ArrayList;

@SystemApi
public final class IncidentReportArgs implements Parcelable {
    public static final Creator<IncidentReportArgs> CREATOR = new Creator<IncidentReportArgs>() {
        public IncidentReportArgs createFromParcel(Parcel in) {
            return new IncidentReportArgs(in);
        }

        public IncidentReportArgs[] newArray(int size) {
            return new IncidentReportArgs[size];
        }
    };
    private boolean mAll;
    private final ArrayList<byte[]> mHeaders;
    private int mPrivacyPolicy;
    private String mReceiverCls;
    private String mReceiverPkg;
    private final IntArray mSections;

    public IncidentReportArgs() {
        this.mSections = new IntArray();
        this.mHeaders = new ArrayList();
        this.mPrivacyPolicy = 200;
    }

    public IncidentReportArgs(Parcel in) {
        this.mSections = new IntArray();
        this.mHeaders = new ArrayList();
        readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        int i;
        out.writeInt(this.mAll);
        int N = this.mSections.size();
        out.writeInt(N);
        for (i = 0; i < N; i++) {
            out.writeInt(this.mSections.get(i));
        }
        N = this.mHeaders.size();
        out.writeInt(N);
        for (i = 0; i < N; i++) {
            out.writeByteArray((byte[]) this.mHeaders.get(i));
        }
        out.writeInt(this.mPrivacyPolicy);
        out.writeString(this.mReceiverPkg);
        out.writeString(this.mReceiverCls);
    }

    public void readFromParcel(Parcel in) {
        int i;
        this.mAll = in.readInt() != 0;
        this.mSections.clear();
        int N = in.readInt();
        for (i = 0; i < N; i++) {
            this.mSections.add(in.readInt());
        }
        this.mHeaders.clear();
        N = in.readInt();
        for (i = 0; i < N; i++) {
            this.mHeaders.add(in.createByteArray());
        }
        this.mPrivacyPolicy = in.readInt();
        this.mReceiverPkg = in.readString();
        this.mReceiverCls = in.readString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Incident(");
        if (this.mAll) {
            sb.append("all");
        } else {
            int N = this.mSections.size();
            if (N > 0) {
                sb.append(this.mSections.get(0));
            }
            for (int i = 1; i < N; i++) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                sb.append(this.mSections.get(i));
            }
        }
        sb.append(", ");
        sb.append(this.mHeaders.size());
        sb.append(" headers), ");
        sb.append("privacy: ");
        sb.append(this.mPrivacyPolicy);
        sb.append("receiver pkg: ");
        sb.append(this.mReceiverPkg);
        sb.append("receiver cls: ");
        sb.append(this.mReceiverCls);
        return sb.toString();
    }

    public void setAll(boolean all) {
        this.mAll = all;
        if (all) {
            this.mSections.clear();
        }
    }

    public void setPrivacyPolicy(int privacyPolicy) {
        if (privacyPolicy == 0 || privacyPolicy == 100 || privacyPolicy == 200) {
            this.mPrivacyPolicy = privacyPolicy;
        } else {
            this.mPrivacyPolicy = 200;
        }
    }

    public void addSection(int section) {
        if (!this.mAll && section > 1) {
            this.mSections.add(section);
        }
    }

    public boolean isAll() {
        return this.mAll;
    }

    public boolean containsSection(int section) {
        return this.mAll || this.mSections.indexOf(section) >= 0;
    }

    public int sectionCount() {
        return this.mSections.size();
    }

    public void addHeader(byte[] header) {
        this.mHeaders.add(header);
    }
}
