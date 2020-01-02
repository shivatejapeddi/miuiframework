package miui.accounts;

import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.accounts.ChooseTypeAndAccountActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.miui.R;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import miui.content.res.IconCustomizer;

public class MiuiChooseAccountTypeActivity extends PreferenceActivity {
    private static final String TAG = "MiuiChooseAccountType";
    private ArrayList<AuthInfo> mAuthenticatorInfosToDisplay;
    private HashMap<String, AuthInfo> mTypeToAuthenticatorInfo = new HashMap();

    private static class AuthInfo {
        final AuthenticatorDescription desc;
        final Drawable drawable;
        final String name;

        AuthInfo(AuthenticatorDescription desc, String name, Drawable drawable) {
            this.desc = desc;
            this.name = name;
            this.drawable = drawable;
        }
    }

    private static class ProviderPreference extends Preference {
        private String mAccountType;

        public ProviderPreference(Context context, String accountType, Drawable icon, CharSequence providerName) {
            super(context);
            this.mAccountType = accountType;
            setIcon(icon);
            setPersistent(false);
            setTitle(providerName);
        }

        public String getAccountType() {
            return this.mAccountType;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        String type;
        Iterator it;
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.choose_account_type_preference);
        PreferenceCategory prefAddAccount = (PreferenceCategory) findPreference("pref_add_account");
        Set<String> setOfAllowableAccountTypes = null;
        String[] validAccountTypes = getIntent().getStringArrayExtra(ChooseTypeAndAccountActivity.EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY);
        if (validAccountTypes != null) {
            setOfAllowableAccountTypes = new HashSet(validAccountTypes.length);
            for (String type2 : validAccountTypes) {
                setOfAllowableAccountTypes.add(type2);
            }
        }
        buildTypeToAuthDescriptionMap();
        this.mAuthenticatorInfosToDisplay = new ArrayList(this.mTypeToAuthenticatorInfo.size());
        for (Entry<String, AuthInfo> entry : this.mTypeToAuthenticatorInfo.entrySet()) {
            type2 = (String) entry.getKey();
            AuthInfo info = (AuthInfo) entry.getValue();
            if (setOfAllowableAccountTypes == null || setOfAllowableAccountTypes.contains(type2)) {
                this.mAuthenticatorInfosToDisplay.add(info);
            }
        }
        if (this.mAuthenticatorInfosToDisplay.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "no allowable account types");
            setResult(-1, new Intent().putExtras(bundle));
            finish();
        } else if (this.mAuthenticatorInfosToDisplay.size() == 1) {
            setResultAndFinish(((AuthInfo) this.mAuthenticatorInfosToDisplay.get(0)).desc.type);
        } else {
            it = this.mAuthenticatorInfosToDisplay.iterator();
            while (it.hasNext()) {
                AuthInfo authInfo = (AuthInfo) it.next();
                ProviderPreference preference = new ProviderPreference(this, authInfo.desc.type, authInfo.drawable, authInfo.name);
                preference.setPersistent(false);
                prefAddAccount.addPreference(preference);
            }
        }
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (!(preference instanceof ProviderPreference)) {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
        ProviderPreference pref = (ProviderPreference) preference;
        String str = TAG;
        if (Log.isLoggable(str, 2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Attempting to add account of type ");
            stringBuilder.append(pref.getAccountType());
            Log.v(str, stringBuilder.toString());
        }
        setResultAndFinish(((ProviderPreference) preference).getAccountType());
        return true;
    }

    private boolean hasXiaomiAccount() {
        AccountManager am = (AccountManager) getSystemService("account");
        boolean z = true;
        if (am.getAccountsByType("com.xiaomi").length > 0) {
            return true;
        }
        if (am.getAccountsByType("com.xiaomi.unactivated").length <= 0) {
            z = false;
        }
        return z;
    }

    private void setResultAndFinish(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("accountType", type);
        setResult(-1, new Intent().putExtras(bundle));
        String str = TAG;
        if (Log.isLoggable(str, 2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ChooseAccountTypeActivity.setResultAndFinish: selected account type ");
            stringBuilder.append(type);
            Log.v(str, stringBuilder.toString());
        }
        finish();
    }

    /* Access modifiers changed, original: protected */
    public void buildTypeToAuthDescriptionMap() {
        StringBuilder stringBuilder;
        String str = TAG;
        boolean hasXiaomiAccount = hasXiaomiAccount();
        for (AuthenticatorDescription desc : AccountManager.get(this).getAuthenticatorTypes()) {
            String name = null;
            Drawable icon = null;
            try {
                Context authContext = createPackageContext(desc.packageName, 0);
                icon = IconCustomizer.generateIconStyleDrawable(authContext.getResources().getDrawable(desc.iconId));
                CharSequence sequence = authContext.getResources().getText(desc.labelId);
                if (sequence != null) {
                    name = sequence.toString();
                }
            } catch (NameNotFoundException e) {
                if (Log.isLoggable(str, 5)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("No icon name for account type ");
                    stringBuilder.append(desc.type);
                    Log.w(str, stringBuilder.toString());
                }
            } catch (NotFoundException e2) {
                if (Log.isLoggable(str, 5)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("No icon resource for account type ");
                    stringBuilder.append(desc.type);
                    Log.w(str, stringBuilder.toString());
                }
            }
            if (!"com.xiaomi.unactivated".equals(desc.type)) {
                if (!"com.xiaomi".equals(desc.type) || !hasXiaomiAccount) {
                    this.mTypeToAuthenticatorInfo.put(desc.type, new AuthInfo(desc, name, icon));
                }
            }
        }
    }
}
