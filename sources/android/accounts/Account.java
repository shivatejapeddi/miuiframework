package android.accounts;

import android.accounts.IAccountManager.Stub;
import android.annotation.UnsupportedAppUsage;
import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.util.Set;

public class Account implements Parcelable {
    public static final Creator<Account> CREATOR = new Creator<Account>() {
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
    @UnsupportedAppUsage
    private static final String TAG = "Account";
    @GuardedBy({"sAccessedAccounts"})
    private static final Set<Account> sAccessedAccounts = new ArraySet();
    @UnsupportedAppUsage
    private final String accessId;
    private String mSafeName;
    public final String name;
    public final String type;

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof Account)) {
            return false;
        }
        Account other = (Account) o;
        if (!(this.name.equals(other.name) && this.type.equals(other.type))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((17 * 31) + this.name.hashCode()) * 31) + this.type.hashCode();
    }

    public Account(String name, String type) {
        this(name, type, null);
    }

    public Account(Account other, String accessId) {
        this(other.name, other.type, accessId);
    }

    public Account(String name, String type, String accessId) {
        StringBuilder stringBuilder;
        if (TextUtils.isEmpty(name)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("the name must not be empty: ");
            stringBuilder.append(name);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (TextUtils.isEmpty(type)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("the type must not be empty: ");
            stringBuilder.append(type);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else {
            this.name = name;
            this.type = type;
            this.accessId = accessId;
        }
    }

    public Account(Parcel in) {
        this.name = in.readString();
        this.type = in.readString();
        StringBuilder stringBuilder;
        if (TextUtils.isEmpty(this.name)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("the name must not be empty: ");
            stringBuilder.append(this.name);
            throw new BadParcelableException(stringBuilder.toString());
        } else if (TextUtils.isEmpty(this.type)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("the type must not be empty: ");
            stringBuilder.append(this.type);
            throw new BadParcelableException(stringBuilder.toString());
        } else {
            this.accessId = in.readString();
            if (this.accessId != null) {
                synchronized (sAccessedAccounts) {
                    if (sAccessedAccounts.add(this)) {
                        try {
                            Stub.asInterface(ServiceManager.getService("account")).onAccountAccessed(this.accessId);
                        } catch (RemoteException e) {
                            Log.e(TAG, "Error noting account access", e);
                        }
                    }
                }
            }
        }
    }

    public String getAccessId() {
        return this.accessId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.accessId);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Account {name=");
        stringBuilder.append(this.name);
        stringBuilder.append(", type=");
        stringBuilder.append(this.type);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public String toSafeString() {
        if (this.mSafeName == null) {
            this.mSafeName = toSafeName(this.name, StateProperty.TARGET_X);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Account {name=");
        stringBuilder.append(this.mSafeName);
        stringBuilder.append(", type=");
        stringBuilder.append(this.type);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static String toSafeName(String name, char replacement) {
        StringBuilder builder = new StringBuilder(64);
        int len = name.length();
        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                builder.append(replacement);
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public String hideNameToString() {
        String safetyName = this.name;
        if (safetyName != null) {
            int nameLength = safetyName.length();
            int mid = nameLength / 2;
            if (nameLength > 0) {
                String str = "***";
                safetyName = nameLength > 3 ? safetyName.replace(safetyName.substring(mid - 1, mid + 2), str) : safetyName.replace(safetyName.substring(0, nameLength), str);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Account {name=");
        stringBuilder.append(safetyName);
        stringBuilder.append(", type=");
        stringBuilder.append(this.type);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
