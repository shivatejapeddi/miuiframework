package android.accounts;

import android.accounts.IAccountAuthenticatorResponse.Stub;
import android.annotation.UnsupportedAppUsage;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.util.Log;

public class AccountAuthenticatorResponse implements Parcelable {
    public static final Creator<AccountAuthenticatorResponse> CREATOR = new Creator<AccountAuthenticatorResponse>() {
        public AccountAuthenticatorResponse createFromParcel(Parcel source) {
            return new AccountAuthenticatorResponse(source);
        }

        public AccountAuthenticatorResponse[] newArray(int size) {
            return new AccountAuthenticatorResponse[size];
        }
    };
    private static final String TAG = "AccountAuthenticator";
    private IAccountAuthenticatorResponse mAccountAuthenticatorResponse;

    @UnsupportedAppUsage
    public AccountAuthenticatorResponse(IAccountAuthenticatorResponse response) {
        this.mAccountAuthenticatorResponse = response;
    }

    public AccountAuthenticatorResponse(Parcel parcel) {
        this.mAccountAuthenticatorResponse = Stub.asInterface(parcel.readStrongBinder());
    }

    public void onResult(Bundle result) {
        String str = TAG;
        if (Log.isLoggable(str, 2)) {
            result.keySet();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AccountAuthenticatorResponse.onResult: ");
            stringBuilder.append(AccountManager.sanitizeResult(result));
            Log.v(str, stringBuilder.toString());
        }
        try {
            this.mAccountAuthenticatorResponse.onResult(result);
        } catch (RemoteException e) {
        }
    }

    public void onRequestContinued() {
        String str = TAG;
        if (Log.isLoggable(str, 2)) {
            Log.v(str, "AccountAuthenticatorResponse.onRequestContinued");
        }
        try {
            this.mAccountAuthenticatorResponse.onRequestContinued();
        } catch (RemoteException e) {
        }
    }

    public void onError(int errorCode, String errorMessage) {
        String str = TAG;
        if (Log.isLoggable(str, 2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AccountAuthenticatorResponse.onError: ");
            stringBuilder.append(errorCode);
            stringBuilder.append(", ");
            stringBuilder.append(errorMessage);
            Log.v(str, stringBuilder.toString());
        }
        try {
            this.mAccountAuthenticatorResponse.onError(errorCode, errorMessage);
        } catch (RemoteException e) {
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(this.mAccountAuthenticatorResponse.asBinder());
    }
}
