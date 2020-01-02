package android.accounts;

import android.Manifest.permission;
import android.accounts.IAccountAuthenticator.Stub;
import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import java.util.Arrays;

public abstract class AbstractAccountAuthenticator {
    private static final String KEY_ACCOUNT = "android.accounts.AbstractAccountAuthenticator.KEY_ACCOUNT";
    private static final String KEY_AUTH_TOKEN_TYPE = "android.accounts.AbstractAccountAuthenticato.KEY_AUTH_TOKEN_TYPE";
    public static final String KEY_CUSTOM_TOKEN_EXPIRY = "android.accounts.expiry";
    private static final String KEY_OPTIONS = "android.accounts.AbstractAccountAuthenticator.KEY_OPTIONS";
    private static final String KEY_REQUIRED_FEATURES = "android.accounts.AbstractAccountAuthenticator.KEY_REQUIRED_FEATURES";
    private static final String TAG = "AccountAuthenticator";
    private final Context mContext;
    private Transport mTransport = new Transport(this, null);

    private class Transport extends Stub {
        private Transport() {
        }

        /* synthetic */ Transport(AbstractAccountAuthenticator x0, AnonymousClass1 x1) {
            this();
        }

        public void addAccount(IAccountAuthenticatorResponse response, String accountType, String authTokenType, String[] features, Bundle options) throws RemoteException {
            String str = AbstractAccountAuthenticator.TAG;
            if (Log.isLoggable(str, 2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("addAccount: accountType ");
                stringBuilder.append(accountType);
                stringBuilder.append(", authTokenType ");
                stringBuilder.append(authTokenType);
                stringBuilder.append(", features ");
                stringBuilder.append(features == null ? "[]" : Arrays.toString(features));
                Log.v(str, stringBuilder.toString());
            }
            AbstractAccountAuthenticator.this.checkBinderPermission();
            try {
                Bundle result = AbstractAccountAuthenticator.this.addAccount(new AccountAuthenticatorResponse(response), accountType, authTokenType, features, options);
                if (Log.isLoggable(str, 2)) {
                    if (result != null) {
                        result.keySet();
                    }
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("addAccount: result ");
                    stringBuilder2.append(AccountManager.sanitizeResult(result));
                    Log.v(str, stringBuilder2.toString());
                }
                if (result != null) {
                    response.onResult(result);
                } else {
                    response.onError(5, "null bundle returned");
                }
            } catch (Exception e) {
                AbstractAccountAuthenticator.this.handleException(response, "addAccount", accountType, e);
            }
        }

        public void confirmCredentials(IAccountAuthenticatorResponse response, Account account, Bundle options) throws RemoteException {
            String str = AbstractAccountAuthenticator.TAG;
            if (Log.isLoggable(str, 2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("confirmCredentials: ");
                stringBuilder.append(account);
                Log.v(str, stringBuilder.toString());
            }
            AbstractAccountAuthenticator.this.checkBinderPermission();
            try {
                Bundle result = AbstractAccountAuthenticator.this.confirmCredentials(new AccountAuthenticatorResponse(response), account, options);
                if (Log.isLoggable(str, 2)) {
                    if (result != null) {
                        result.keySet();
                    }
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("confirmCredentials: result ");
                    stringBuilder2.append(AccountManager.sanitizeResult(result));
                    Log.v(str, stringBuilder2.toString());
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                AbstractAccountAuthenticator.this.handleException(response, "confirmCredentials", account.toString(), e);
            }
        }

        public void getAuthTokenLabel(IAccountAuthenticatorResponse response, String authTokenType) throws RemoteException {
            String str = AbstractAccountAuthenticator.TAG;
            if (Log.isLoggable(str, 2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("getAuthTokenLabel: authTokenType ");
                stringBuilder.append(authTokenType);
                Log.v(str, stringBuilder.toString());
            }
            AbstractAccountAuthenticator.this.checkBinderPermission();
            try {
                Bundle result = new Bundle();
                result.putString(AccountManager.KEY_AUTH_TOKEN_LABEL, AbstractAccountAuthenticator.this.getAuthTokenLabel(authTokenType));
                if (Log.isLoggable(str, 2)) {
                    result.keySet();
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("getAuthTokenLabel: result ");
                    stringBuilder2.append(AccountManager.sanitizeResult(result));
                    Log.v(str, stringBuilder2.toString());
                }
                response.onResult(result);
            } catch (Exception e) {
                AbstractAccountAuthenticator.this.handleException(response, "getAuthTokenLabel", authTokenType, e);
            }
        }

        public void getAuthToken(IAccountAuthenticatorResponse response, Account account, String authTokenType, Bundle loginOptions) throws RemoteException {
            StringBuilder stringBuilder;
            String str = AbstractAccountAuthenticator.TAG;
            if (Log.isLoggable(str, 2)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("getAuthToken: ");
                stringBuilder.append(account);
                stringBuilder.append(", authTokenType ");
                stringBuilder.append(authTokenType);
                Log.v(str, stringBuilder.toString());
            }
            AbstractAccountAuthenticator.this.checkBinderPermission();
            try {
                Bundle result = AbstractAccountAuthenticator.this.getAuthToken(new AccountAuthenticatorResponse(response), account, authTokenType, loginOptions);
                if (Log.isLoggable(str, 2)) {
                    if (result != null) {
                        result.keySet();
                    }
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("getAuthToken: result ");
                    stringBuilder2.append(AccountManager.sanitizeResult(result));
                    Log.v(str, stringBuilder2.toString());
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                AbstractAccountAuthenticator abstractAccountAuthenticator = AbstractAccountAuthenticator.this;
                stringBuilder = new StringBuilder();
                stringBuilder.append(account.toString());
                stringBuilder.append(",");
                stringBuilder.append(authTokenType);
                abstractAccountAuthenticator.handleException(response, "getAuthToken", stringBuilder.toString(), e);
            }
        }

        public void updateCredentials(IAccountAuthenticatorResponse response, Account account, String authTokenType, Bundle loginOptions) throws RemoteException {
            StringBuilder stringBuilder;
            String str = AbstractAccountAuthenticator.TAG;
            if (Log.isLoggable(str, 2)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("updateCredentials: ");
                stringBuilder.append(account);
                stringBuilder.append(", authTokenType ");
                stringBuilder.append(authTokenType);
                Log.v(str, stringBuilder.toString());
            }
            AbstractAccountAuthenticator.this.checkBinderPermission();
            try {
                Bundle result = AbstractAccountAuthenticator.this.updateCredentials(new AccountAuthenticatorResponse(response), account, authTokenType, loginOptions);
                if (Log.isLoggable(str, 2)) {
                    if (result != null) {
                        result.keySet();
                    }
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("updateCredentials: result ");
                    stringBuilder2.append(AccountManager.sanitizeResult(result));
                    Log.v(str, stringBuilder2.toString());
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                AbstractAccountAuthenticator abstractAccountAuthenticator = AbstractAccountAuthenticator.this;
                stringBuilder = new StringBuilder();
                stringBuilder.append(account.toString());
                stringBuilder.append(",");
                stringBuilder.append(authTokenType);
                abstractAccountAuthenticator.handleException(response, "updateCredentials", stringBuilder.toString(), e);
            }
        }

        public void editProperties(IAccountAuthenticatorResponse response, String accountType) throws RemoteException {
            AbstractAccountAuthenticator.this.checkBinderPermission();
            try {
                Bundle result = AbstractAccountAuthenticator.this.editProperties(new AccountAuthenticatorResponse(response), accountType);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                AbstractAccountAuthenticator.this.handleException(response, "editProperties", accountType, e);
            }
        }

        public void hasFeatures(IAccountAuthenticatorResponse response, Account account, String[] features) throws RemoteException {
            AbstractAccountAuthenticator.this.checkBinderPermission();
            try {
                Bundle result = AbstractAccountAuthenticator.this.hasFeatures(new AccountAuthenticatorResponse(response), account, features);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                AbstractAccountAuthenticator.this.handleException(response, "hasFeatures", account.toString(), e);
            }
        }

        public void getAccountRemovalAllowed(IAccountAuthenticatorResponse response, Account account) throws RemoteException {
            AbstractAccountAuthenticator.this.checkBinderPermission();
            try {
                Bundle result = AbstractAccountAuthenticator.this.getAccountRemovalAllowed(new AccountAuthenticatorResponse(response), account);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                AbstractAccountAuthenticator.this.handleException(response, "getAccountRemovalAllowed", account.toString(), e);
            }
        }

        public void getAccountCredentialsForCloning(IAccountAuthenticatorResponse response, Account account) throws RemoteException {
            AbstractAccountAuthenticator.this.checkBinderPermission();
            try {
                Bundle result = AbstractAccountAuthenticator.this.getAccountCredentialsForCloning(new AccountAuthenticatorResponse(response), account);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                AbstractAccountAuthenticator.this.handleException(response, "getAccountCredentialsForCloning", account.toString(), e);
            }
        }

        public void addAccountFromCredentials(IAccountAuthenticatorResponse response, Account account, Bundle accountCredentials) throws RemoteException {
            AbstractAccountAuthenticator.this.checkBinderPermission();
            try {
                Bundle result = AbstractAccountAuthenticator.this.addAccountFromCredentials(new AccountAuthenticatorResponse(response), account, accountCredentials);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                AbstractAccountAuthenticator.this.handleException(response, "addAccountFromCredentials", account.toString(), e);
            }
        }

        public void startAddAccountSession(IAccountAuthenticatorResponse response, String accountType, String authTokenType, String[] features, Bundle options) throws RemoteException {
            String str = AbstractAccountAuthenticator.TAG;
            if (Log.isLoggable(str, 2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("startAddAccountSession: accountType ");
                stringBuilder.append(accountType);
                stringBuilder.append(", authTokenType ");
                stringBuilder.append(authTokenType);
                stringBuilder.append(", features ");
                stringBuilder.append(features == null ? "[]" : Arrays.toString(features));
                Log.v(str, stringBuilder.toString());
            }
            AbstractAccountAuthenticator.this.checkBinderPermission();
            try {
                Bundle result = AbstractAccountAuthenticator.this.startAddAccountSession(new AccountAuthenticatorResponse(response), accountType, authTokenType, features, options);
                if (Log.isLoggable(str, 2)) {
                    if (result != null) {
                        result.keySet();
                    }
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("startAddAccountSession: result ");
                    stringBuilder2.append(AccountManager.sanitizeResult(result));
                    Log.v(str, stringBuilder2.toString());
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                AbstractAccountAuthenticator.this.handleException(response, "startAddAccountSession", accountType, e);
            }
        }

        public void startUpdateCredentialsSession(IAccountAuthenticatorResponse response, Account account, String authTokenType, Bundle loginOptions) throws RemoteException {
            StringBuilder stringBuilder;
            String str = AbstractAccountAuthenticator.TAG;
            if (Log.isLoggable(str, 2)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("startUpdateCredentialsSession: ");
                stringBuilder.append(account);
                stringBuilder.append(", authTokenType ");
                stringBuilder.append(authTokenType);
                Log.v(str, stringBuilder.toString());
            }
            AbstractAccountAuthenticator.this.checkBinderPermission();
            try {
                Bundle result = AbstractAccountAuthenticator.this.startUpdateCredentialsSession(new AccountAuthenticatorResponse(response), account, authTokenType, loginOptions);
                if (Log.isLoggable(str, 2)) {
                    if (result != null) {
                        result.keySet();
                    }
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("startUpdateCredentialsSession: result ");
                    stringBuilder2.append(AccountManager.sanitizeResult(result));
                    Log.v(str, stringBuilder2.toString());
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                AbstractAccountAuthenticator abstractAccountAuthenticator = AbstractAccountAuthenticator.this;
                stringBuilder = new StringBuilder();
                stringBuilder.append(account.toString());
                stringBuilder.append(",");
                stringBuilder.append(authTokenType);
                abstractAccountAuthenticator.handleException(response, "startUpdateCredentialsSession", stringBuilder.toString(), e);
            }
        }

        public void finishSession(IAccountAuthenticatorResponse response, String accountType, Bundle sessionBundle) throws RemoteException {
            String str = AbstractAccountAuthenticator.TAG;
            if (Log.isLoggable(str, 2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("finishSession: accountType ");
                stringBuilder.append(accountType);
                Log.v(str, stringBuilder.toString());
            }
            AbstractAccountAuthenticator.this.checkBinderPermission();
            try {
                Bundle result = AbstractAccountAuthenticator.this.finishSession(new AccountAuthenticatorResponse(response), accountType, sessionBundle);
                if (result != null) {
                    result.keySet();
                }
                if (Log.isLoggable(str, 2)) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("finishSession: result ");
                    stringBuilder2.append(AccountManager.sanitizeResult(result));
                    Log.v(str, stringBuilder2.toString());
                }
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                AbstractAccountAuthenticator.this.handleException(response, "finishSession", accountType, e);
            }
        }

        public void isCredentialsUpdateSuggested(IAccountAuthenticatorResponse response, Account account, String statusToken) throws RemoteException {
            AbstractAccountAuthenticator.this.checkBinderPermission();
            try {
                Bundle result = AbstractAccountAuthenticator.this.isCredentialsUpdateSuggested(new AccountAuthenticatorResponse(response), account, statusToken);
                if (result != null) {
                    response.onResult(result);
                }
            } catch (Exception e) {
                AbstractAccountAuthenticator.this.handleException(response, "isCredentialsUpdateSuggested", account.toString(), e);
            }
        }
    }

    public abstract Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse, String str, String str2, String[] strArr, Bundle bundle) throws NetworkErrorException;

    public abstract Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException;

    public abstract Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String str);

    public abstract Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String str, Bundle bundle) throws NetworkErrorException;

    public abstract String getAuthTokenLabel(String str);

    public abstract Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strArr) throws NetworkErrorException;

    public abstract Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String str, Bundle bundle) throws NetworkErrorException;

    public AbstractAccountAuthenticator(Context context) {
        this.mContext = context;
    }

    private void handleException(IAccountAuthenticatorResponse response, String method, String data, Exception e) throws RemoteException {
        boolean z = e instanceof NetworkErrorException;
        String str = ")";
        String str2 = "(";
        String str3 = TAG;
        StringBuilder stringBuilder;
        if (z) {
            if (Log.isLoggable(str3, 2)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(method);
                stringBuilder.append(str2);
                stringBuilder.append(data);
                stringBuilder.append(str);
                Log.v(str3, stringBuilder.toString(), e);
            }
            response.onError(3, e.getMessage());
            return;
        }
        String str4 = " not supported";
        StringBuilder stringBuilder2;
        if (e instanceof UnsupportedOperationException) {
            if (Log.isLoggable(str3, 2)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(method);
                stringBuilder.append(str2);
                stringBuilder.append(data);
                stringBuilder.append(str);
                Log.v(str3, stringBuilder.toString(), e);
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(method);
            stringBuilder2.append(str4);
            response.onError(6, stringBuilder2.toString());
        } else if (e instanceof IllegalArgumentException) {
            if (Log.isLoggable(str3, 2)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(method);
                stringBuilder.append(str2);
                stringBuilder.append(data);
                stringBuilder.append(str);
                Log.v(str3, stringBuilder.toString(), e);
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(method);
            stringBuilder2.append(str4);
            response.onError(7, stringBuilder2.toString());
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(method);
            stringBuilder.append(str2);
            stringBuilder.append(data);
            stringBuilder.append(str);
            Log.w(str3, stringBuilder.toString(), e);
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(method);
            stringBuilder2.append(" failed");
            response.onError(1, stringBuilder2.toString());
        }
    }

    private void checkBinderPermission() {
        int uid = Binder.getCallingUid();
        String perm = permission.ACCOUNT_MANAGER;
        Context context = this.mContext;
        String str = permission.ACCOUNT_MANAGER;
        if (context.checkCallingOrSelfPermission(str) != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("caller uid ");
            stringBuilder.append(uid);
            stringBuilder.append(" lacks ");
            stringBuilder.append(str);
            throw new SecurityException(stringBuilder.toString());
        }
    }

    public final IBinder getIBinder() {
        return this.mTransport.asBinder();
    }

    public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response, Account account) throws NetworkErrorException {
        Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);
        return result;
    }

    public Bundle getAccountCredentialsForCloning(final AccountAuthenticatorResponse response, Account account) throws NetworkErrorException {
        new Thread(new Runnable() {
            public void run() {
                Bundle result = new Bundle();
                result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
                response.onResult(result);
            }
        }).start();
        return null;
    }

    public Bundle addAccountFromCredentials(final AccountAuthenticatorResponse response, Account account, Bundle accountCredentials) throws NetworkErrorException {
        new Thread(new Runnable() {
            public void run() {
                Bundle result = new Bundle();
                result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
                response.onResult(result);
            }
        }).start();
        return null;
    }

    public Bundle startAddAccountSession(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        final String str = authTokenType;
        final String[] strArr = requiredFeatures;
        final Bundle bundle = options;
        final AccountAuthenticatorResponse accountAuthenticatorResponse = response;
        new Thread(new Runnable() {
            public void run() {
                Bundle sessionBundle = new Bundle();
                sessionBundle.putString(AbstractAccountAuthenticator.KEY_AUTH_TOKEN_TYPE, str);
                sessionBundle.putStringArray(AbstractAccountAuthenticator.KEY_REQUIRED_FEATURES, strArr);
                sessionBundle.putBundle(AbstractAccountAuthenticator.KEY_OPTIONS, bundle);
                Bundle result = new Bundle();
                result.putBundle(AccountManager.KEY_ACCOUNT_SESSION_BUNDLE, sessionBundle);
                accountAuthenticatorResponse.onResult(result);
            }
        }).start();
        return null;
    }

    public Bundle startUpdateCredentialsSession(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        final String str = authTokenType;
        final Account account2 = account;
        final Bundle bundle = options;
        final AccountAuthenticatorResponse accountAuthenticatorResponse = response;
        new Thread(new Runnable() {
            public void run() {
                Bundle sessionBundle = new Bundle();
                sessionBundle.putString(AbstractAccountAuthenticator.KEY_AUTH_TOKEN_TYPE, str);
                sessionBundle.putParcelable(AbstractAccountAuthenticator.KEY_ACCOUNT, account2);
                sessionBundle.putBundle(AbstractAccountAuthenticator.KEY_OPTIONS, bundle);
                Bundle result = new Bundle();
                result.putBundle(AccountManager.KEY_ACCOUNT_SESSION_BUNDLE, sessionBundle);
                accountAuthenticatorResponse.onResult(result);
            }
        }).start();
        return null;
    }

    public Bundle finishSession(AccountAuthenticatorResponse response, String accountType, Bundle sessionBundle) throws NetworkErrorException {
        AccountAuthenticatorResponse accountAuthenticatorResponse = response;
        Bundle bundle = sessionBundle;
        boolean isEmpty = TextUtils.isEmpty(accountType);
        String str = TAG;
        String str2 = AccountManager.KEY_ERROR_MESSAGE;
        String str3 = "errorCode";
        Bundle result;
        if (isEmpty) {
            Log.e(str, "Account type cannot be empty.");
            result = new Bundle();
            result.putInt(str3, 7);
            result.putString(str2, "accountType cannot be empty.");
            return result;
        } else if (bundle == null) {
            Log.e(str, "Session bundle cannot be null.");
            result = new Bundle();
            result.putInt(str3, 7);
            result.putString(str2, "sessionBundle cannot be null.");
            return result;
        } else {
            String str4 = KEY_AUTH_TOKEN_TYPE;
            if (bundle.containsKey(str4)) {
                Bundle sessionOptions;
                String authTokenType = bundle.getString(str4);
                String str5 = KEY_OPTIONS;
                Bundle options = bundle.getBundle(str5);
                str = KEY_REQUIRED_FEATURES;
                String[] requiredFeatures = bundle.getStringArray(str);
                str2 = KEY_ACCOUNT;
                Account account = (Account) bundle.getParcelable(str2);
                boolean containsKeyAccount = bundle.containsKey(str2);
                Bundle sessionOptions2 = new Bundle(bundle);
                sessionOptions2.remove(str4);
                sessionOptions2.remove(str);
                sessionOptions2.remove(str5);
                sessionOptions2.remove(str2);
                if (options != null) {
                    options.putAll(sessionOptions2);
                    sessionOptions = options;
                } else {
                    sessionOptions = sessionOptions2;
                }
                if (containsKeyAccount) {
                    return updateCredentials(accountAuthenticatorResponse, account, authTokenType, options);
                }
                return addAccount(response, accountType, authTokenType, requiredFeatures, sessionOptions);
            }
            result = new Bundle();
            result.putInt(str3, 6);
            result.putString(str2, "Authenticator must override finishSession if startAddAccountSession or startUpdateCredentialsSession is overridden.");
            accountAuthenticatorResponse.onResult(result);
            return result;
        }
    }

    public Bundle isCredentialsUpdateSuggested(AccountAuthenticatorResponse response, Account account, String statusToken) throws NetworkErrorException {
        Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }
}
