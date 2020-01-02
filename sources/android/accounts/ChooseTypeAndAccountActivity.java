package android.accounts;

import android.app.Activity;
import android.app.ActivityTaskManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.R;
import com.google.android.collect.Sets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ChooseTypeAndAccountActivity extends Activity implements AccountManagerCallback<Bundle> {
    public static final String EXTRA_ADD_ACCOUNT_AUTH_TOKEN_TYPE_STRING = "authTokenType";
    public static final String EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE = "addAccountOptions";
    public static final String EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY = "addAccountRequiredFeatures";
    public static final String EXTRA_ALLOWABLE_ACCOUNTS_ARRAYLIST = "allowableAccounts";
    public static final String EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY = "allowableAccountTypes";
    @Deprecated
    public static final String EXTRA_ALWAYS_PROMPT_FOR_ACCOUNT = "alwaysPromptForAccount";
    public static final String EXTRA_DESCRIPTION_TEXT_OVERRIDE = "descriptionTextOverride";
    public static final String EXTRA_SELECTED_ACCOUNT = "selectedAccount";
    private static final String KEY_INSTANCE_STATE_ACCOUNTS_LIST = "accountsList";
    private static final String KEY_INSTANCE_STATE_EXISTING_ACCOUNTS = "existingAccounts";
    private static final String KEY_INSTANCE_STATE_PENDING_REQUEST = "pendingRequest";
    private static final String KEY_INSTANCE_STATE_SELECTED_ACCOUNT_NAME = "selectedAccountName";
    private static final String KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT = "selectedAddAccount";
    private static final String KEY_INSTANCE_STATE_VISIBILITY_LIST = "visibilityList";
    public static final int REQUEST_ADD_ACCOUNT = 2;
    public static final int REQUEST_CHOOSE_TYPE = 1;
    public static final int REQUEST_NULL = 0;
    private static final int SELECTED_ITEM_NONE = -1;
    private static final String TAG = "AccountChooser";
    private LinkedHashMap<Account, Integer> mAccounts;
    private String mCallingPackage;
    private int mCallingUid;
    private String mDescriptionOverride;
    private boolean mDisallowAddAccounts;
    private boolean mDontShowPicker;
    private Parcelable[] mExistingAccounts = null;
    private Button mOkButton;
    private int mPendingRequest = 0;
    private ArrayList<Account> mPossiblyVisibleAccounts;
    private String mSelectedAccountName = null;
    private boolean mSelectedAddNewAccount = false;
    private int mSelectedItemIndex;
    private Set<Account> mSetOfAllowableAccounts;
    private Set<String> mSetOfRelevantAccountTypes;

    public void onCreate(Bundle savedInstanceState) {
        String str = TAG;
        if (Log.isLoggable(str, 2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ChooseTypeAndAccountActivity.onCreate(savedInstanceState=");
            stringBuilder.append(savedInstanceState);
            stringBuilder.append(")");
            Log.v(str, stringBuilder.toString());
        }
        try {
            IBinder activityToken = getActivityToken();
            this.mCallingUid = ActivityTaskManager.getService().getLaunchedFromUid(activityToken);
            this.mCallingPackage = ActivityTaskManager.getService().getLaunchedFromPackage(activityToken);
            if (!(this.mCallingUid == 0 || this.mCallingPackage == null)) {
                this.mDisallowAddAccounts = UserManager.get(this).getUserRestrictions(new UserHandle(UserHandle.getUserId(this.mCallingUid))).getBoolean(UserManager.DISALLOW_MODIFY_ACCOUNTS, false);
            }
        } catch (RemoteException re) {
            String simpleName = getClass().getSimpleName();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unable to get caller identity \n");
            stringBuilder2.append(re);
            Log.w(simpleName, stringBuilder2.toString());
        }
        Intent intent = getIntent();
        this.mSetOfAllowableAccounts = getAllowableAccountSet(intent);
        this.mSetOfRelevantAccountTypes = getReleventAccountTypes(intent);
        this.mDescriptionOverride = intent.getStringExtra(EXTRA_DESCRIPTION_TEXT_OVERRIDE);
        if (savedInstanceState != null) {
            this.mPendingRequest = savedInstanceState.getInt(KEY_INSTANCE_STATE_PENDING_REQUEST);
            this.mExistingAccounts = savedInstanceState.getParcelableArray(KEY_INSTANCE_STATE_EXISTING_ACCOUNTS);
            this.mSelectedAccountName = savedInstanceState.getString(KEY_INSTANCE_STATE_SELECTED_ACCOUNT_NAME);
            this.mSelectedAddNewAccount = savedInstanceState.getBoolean(KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT, false);
            Parcelable[] accounts = savedInstanceState.getParcelableArray(KEY_INSTANCE_STATE_ACCOUNTS_LIST);
            ArrayList<Integer> visibility = savedInstanceState.getIntegerArrayList(KEY_INSTANCE_STATE_VISIBILITY_LIST);
            this.mAccounts = new LinkedHashMap();
            for (int i = 0; i < accounts.length; i++) {
                this.mAccounts.put((Account) accounts[i], (Integer) visibility.get(i));
            }
        } else {
            this.mPendingRequest = 0;
            this.mExistingAccounts = null;
            Account selectedAccount = (Account) intent.getParcelableExtra(EXTRA_SELECTED_ACCOUNT);
            if (selectedAccount != null) {
                this.mSelectedAccountName = selectedAccount.name;
            }
            this.mAccounts = getAcceptableAccountChoices(AccountManager.get(this));
        }
        if (Log.isLoggable(str, 2)) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("selected account name is ");
            stringBuilder3.append(this.mSelectedAccountName);
            Log.v(str, stringBuilder3.toString());
        }
        this.mPossiblyVisibleAccounts = new ArrayList(this.mAccounts.size());
        for (Entry<Account, Integer> entry : this.mAccounts.entrySet()) {
            if (3 != ((Integer) entry.getValue()).intValue()) {
                this.mPossiblyVisibleAccounts.add((Account) entry.getKey());
            }
        }
        boolean z = true;
        if (this.mPossiblyVisibleAccounts.isEmpty() && this.mDisallowAddAccounts) {
            requestWindowFeature(1);
            setContentView((int) R.layout.app_not_authorized);
            this.mDontShowPicker = true;
        }
        if (this.mDontShowPicker) {
            super.onCreate(savedInstanceState);
            return;
        }
        if (this.mPendingRequest == 0 && this.mPossiblyVisibleAccounts.isEmpty()) {
            setNonLabelThemeAndCallSuperCreate(savedInstanceState);
            if (this.mSetOfRelevantAccountTypes.size() == 1) {
                this.mPendingRequest = 1;
                runAddAccountForAuthenticator((String) this.mSetOfRelevantAccountTypes.iterator().next());
            } else {
                startChooseAccountTypeActivity();
            }
        }
        String[] listItems = getListOfDisplayableOptions(this.mPossiblyVisibleAccounts);
        this.mSelectedItemIndex = getItemIndexToSelect(this.mPossiblyVisibleAccounts, this.mSelectedAccountName, this.mSelectedAddNewAccount);
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.choose_type_and_account);
        overrideDescriptionIfSupplied(this.mDescriptionOverride);
        populateUIAccountList(listItems);
        this.mOkButton = (Button) findViewById(16908314);
        Button button = this.mOkButton;
        if (this.mSelectedItemIndex == -1) {
            z = false;
        }
        button.setEnabled(z);
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        String str = TAG;
        if (Log.isLoggable(str, 2)) {
            Log.v(str, "ChooseTypeAndAccountActivity.onDestroy()");
        }
        super.onDestroy();
    }

    /* Access modifiers changed, original: protected */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INSTANCE_STATE_PENDING_REQUEST, this.mPendingRequest);
        if (this.mPendingRequest == 2) {
            outState.putParcelableArray(KEY_INSTANCE_STATE_EXISTING_ACCOUNTS, this.mExistingAccounts);
        }
        int i = this.mSelectedItemIndex;
        if (i != -1) {
            int size = this.mPossiblyVisibleAccounts.size();
            String str = KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT;
            if (i == size) {
                outState.putBoolean(str, true);
            } else {
                outState.putBoolean(str, false);
                outState.putString(KEY_INSTANCE_STATE_SELECTED_ACCOUNT_NAME, ((Account) this.mPossiblyVisibleAccounts.get(this.mSelectedItemIndex)).name);
            }
        }
        Parcelable[] accounts = new Parcelable[this.mAccounts.size()];
        ArrayList<Integer> visibility = new ArrayList(this.mAccounts.size());
        int i2 = 0;
        for (Entry<Account, Integer> e : this.mAccounts.entrySet()) {
            int i3 = i2 + 1;
            accounts[i2] = (Parcelable) e.getKey();
            visibility.add((Integer) e.getValue());
            i2 = i3;
        }
        outState.putParcelableArray(KEY_INSTANCE_STATE_ACCOUNTS_LIST, accounts);
        outState.putIntegerArrayList(KEY_INSTANCE_STATE_VISIBILITY_LIST, visibility);
    }

    public void onCancelButtonClicked(View view) {
        onBackPressed();
    }

    public void onOkButtonClicked(View view) {
        if (this.mSelectedItemIndex == this.mPossiblyVisibleAccounts.size()) {
            startChooseAccountTypeActivity();
            return;
        }
        int i = this.mSelectedItemIndex;
        if (i != -1) {
            onAccountSelected((Account) this.mPossiblyVisibleAccounts.get(i));
        }
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String str = TAG;
        if (Log.isLoggable(str, 2)) {
            if (!(data == null || data.getExtras() == null)) {
                data.getExtras().keySet();
            }
            Bundle extras = data != null ? data.getExtras() : null;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ChooseTypeAndAccountActivity.onActivityResult(reqCode=");
            stringBuilder.append(requestCode);
            stringBuilder.append(", resCode=");
            stringBuilder.append(resultCode);
            stringBuilder.append(", extras=");
            stringBuilder.append(extras);
            stringBuilder.append(")");
            Log.v(str, stringBuilder.toString());
        }
        this.mPendingRequest = 0;
        if (resultCode == 0) {
            if (this.mPossiblyVisibleAccounts.isEmpty()) {
                setResult(0);
                finish();
            }
            return;
        }
        if (resultCode == -1) {
            String str2 = "accountType";
            if (requestCode == 1) {
                if (data != null) {
                    str2 = data.getStringExtra(str2);
                    if (str2 != null) {
                        runAddAccountForAuthenticator(str2);
                        return;
                    }
                }
                Log.d(str, "ChooseTypeAndAccountActivity.onActivityResult: unable to find account type, pretending the request was canceled");
            } else if (requestCode == 2) {
                String accountName = null;
                String accountType = null;
                if (data != null) {
                    accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    accountType = data.getStringExtra(str2);
                }
                if (accountName == null || accountType == null) {
                    Account[] currentAccounts = AccountManager.get(this).getAccountsForPackage(this.mCallingPackage, this.mCallingUid);
                    Set<Account> preExistingAccounts = new HashSet();
                    for (Parcelable accountParcel : this.mExistingAccounts) {
                        preExistingAccounts.add((Account) accountParcel);
                    }
                    for (Account account : currentAccounts) {
                        if (!preExistingAccounts.contains(account)) {
                            accountName = account.name;
                            accountType = account.type;
                            break;
                        }
                    }
                }
                if (!(accountName == null && accountType == null)) {
                    setResultAndFinish(accountName, accountType);
                    return;
                }
            }
            Log.d(str, "ChooseTypeAndAccountActivity.onActivityResult: unable to find added account, pretending the request was canceled");
        }
        if (Log.isLoggable(str, 2)) {
            Log.v(str, "ChooseTypeAndAccountActivity.onActivityResult: canceled");
        }
        setResult(0);
        finish();
    }

    /* Access modifiers changed, original: protected */
    public void runAddAccountForAuthenticator(String type) {
        String str = TAG;
        if (Log.isLoggable(str, 2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("runAddAccountForAuthenticator: ");
            stringBuilder.append(type);
            Log.v(str, stringBuilder.toString());
        }
        Bundle options = getIntent().getBundleExtra(EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE);
        String[] requiredFeatures = getIntent().getStringArrayExtra(EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY);
        AccountManager.get(this).addAccount(type, getIntent().getStringExtra("authTokenType"), requiredFeatures, options, null, this, null);
    }

    public void run(AccountManagerFuture<Bundle> accountManagerFuture) {
        try {
            Intent intent = (Intent) ((Bundle) accountManagerFuture.getResult()).getParcelable("intent");
            if (intent != null) {
                this.mPendingRequest = 2;
                this.mExistingAccounts = AccountManager.get(this).getAccountsForPackage(this.mCallingPackage, this.mCallingUid);
                intent.setFlags(intent.getFlags() & -268435457);
                startActivityForResult(intent, 2);
                return;
            }
        } catch (OperationCanceledException e) {
            setResult(0);
            finish();
            return;
        } catch (AuthenticatorException | IOException e2) {
        }
        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "error communicating with server");
        setResult(-1, new Intent().putExtras(bundle));
        finish();
    }

    private void setNonLabelThemeAndCallSuperCreate(Bundle savedInstanceState) {
        setTheme(16974132);
        super.onCreate(savedInstanceState);
    }

    private void onAccountSelected(Account account) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("selected account ");
        stringBuilder.append(account);
        Log.d(TAG, stringBuilder.toString());
        setResultAndFinish(account.name, account.type);
    }

    private void setResultAndFinish(String accountName, String accountType) {
        Account account = new Account(accountName, accountType);
        Integer oldVisibility = Integer.valueOf(AccountManager.get(this).getAccountVisibility(account, this.mCallingPackage));
        if (oldVisibility != null && oldVisibility.intValue() == 4) {
            AccountManager.get(this).setAccountVisibility(account, this.mCallingPackage, 2);
        }
        if (oldVisibility == null || oldVisibility.intValue() != 3) {
            Bundle bundle = new Bundle();
            bundle.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
            bundle.putString("accountType", accountType);
            setResult(-1, new Intent().putExtras(bundle));
            String str = TAG;
            if (Log.isLoggable(str, 2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ChooseTypeAndAccountActivity.setResultAndFinish: selected account ");
                stringBuilder.append(accountName);
                stringBuilder.append(", ");
                stringBuilder.append(accountType);
                Log.v(str, stringBuilder.toString());
            }
            finish();
            return;
        }
        setResult(0);
        finish();
    }

    private void startChooseAccountTypeActivity() {
        String str = TAG;
        if (Log.isLoggable(str, 2)) {
            Log.v(str, "ChooseAccountTypeActivity.startChooseAccountTypeActivity()");
        }
        Intent intent = new Intent();
        ChooseTypeAndAccountActivityInjector.toMiuiChooseAccountTypeActivity(intent);
        intent.setFlags(524288);
        Intent intent2 = getIntent();
        String str2 = EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY;
        intent.putExtra(str2, intent2.getStringArrayExtra(str2));
        intent2 = getIntent();
        str2 = EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE;
        intent.putExtra(str2, intent2.getBundleExtra(str2));
        intent2 = getIntent();
        str2 = EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY;
        intent.putExtra(str2, intent2.getStringArrayExtra(str2));
        str2 = "authTokenType";
        intent.putExtra(str2, getIntent().getStringExtra(str2));
        startActivityForResult(intent, 1);
        this.mPendingRequest = 1;
    }

    private int getItemIndexToSelect(ArrayList<Account> accounts, String selectedAccountName, boolean selectedAddNewAccount) {
        if (selectedAddNewAccount) {
            return accounts.size();
        }
        for (int i = 0; i < accounts.size(); i++) {
            if (((Account) accounts.get(i)).name.equals(selectedAccountName)) {
                return i;
            }
        }
        return -1;
    }

    private String[] getListOfDisplayableOptions(ArrayList<Account> accounts) {
        String[] listItems = new String[(accounts.size() + (this.mDisallowAddAccounts ^ 1))];
        for (int i = 0; i < accounts.size(); i++) {
            listItems[i] = ((Account) accounts.get(i)).name;
        }
        if (!this.mDisallowAddAccounts) {
            listItems[accounts.size()] = getResources().getString(R.string.add_account_button_label);
        }
        return listItems;
    }

    private LinkedHashMap<Account, Integer> getAcceptableAccountChoices(AccountManager accountManager) {
        Map<Account, Integer> accountsAndVisibilityForCaller = accountManager.getAccountsAndVisibilityForPackage(this.mCallingPackage, null);
        Account[] allAccounts = accountManager.getAccounts();
        LinkedHashMap<Account, Integer> accountsToPopulate = new LinkedHashMap(accountsAndVisibilityForCaller.size());
        for (Account account : allAccounts) {
            Set set = this.mSetOfAllowableAccounts;
            if (set == null || set.contains(account)) {
                set = this.mSetOfRelevantAccountTypes;
                if ((set == null || set.contains(account.type)) && accountsAndVisibilityForCaller.get(account) != null) {
                    accountsToPopulate.put(account, (Integer) accountsAndVisibilityForCaller.get(account));
                }
            }
        }
        return accountsToPopulate;
    }

    private Set<String> getReleventAccountTypes(Intent intent) {
        String[] allowedAccountTypes = intent.getStringArrayExtra(EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY);
        AuthenticatorDescription[] descs = AccountManager.get(this).getAuthenticatorTypes();
        Set<String> supportedAccountTypes = new HashSet(descs.length);
        for (AuthenticatorDescription desc : descs) {
            supportedAccountTypes.add(desc.type);
        }
        if (allowedAccountTypes == null) {
            return supportedAccountTypes;
        }
        Set<String> setOfRelevantAccountTypes = Sets.newHashSet(allowedAccountTypes);
        setOfRelevantAccountTypes.retainAll(supportedAccountTypes);
        return setOfRelevantAccountTypes;
    }

    private Set<Account> getAllowableAccountSet(Intent intent) {
        Set<Account> setOfAllowableAccounts = null;
        ArrayList<Parcelable> validAccounts = intent.getParcelableArrayListExtra(EXTRA_ALLOWABLE_ACCOUNTS_ARRAYLIST);
        if (validAccounts != null) {
            setOfAllowableAccounts = new HashSet(validAccounts.size());
            Iterator it = validAccounts.iterator();
            while (it.hasNext()) {
                setOfAllowableAccounts.add((Account) ((Parcelable) it.next()));
            }
        }
        return setOfAllowableAccounts;
    }

    private void overrideDescriptionIfSupplied(String descriptionOverride) {
        TextView descriptionView = (TextView) findViewById(R.id.description);
        if (TextUtils.isEmpty(descriptionOverride)) {
            descriptionView.setVisibility(8);
        } else {
            descriptionView.setText((CharSequence) descriptionOverride);
        }
    }

    private final void populateUIAccountList(String[] listItems) {
        ListView list = (ListView) findViewById(16908298);
        list.setAdapter(new ArrayAdapter((Context) this, 17367055, (Object[]) listItems));
        list.setChoiceMode(1);
        list.setItemsCanFocus(false);
        list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                ChooseTypeAndAccountActivity.this.mSelectedItemIndex = position;
                ChooseTypeAndAccountActivity.this.mOkButton.setEnabled(true);
            }
        });
        int i = this.mSelectedItemIndex;
        if (i != -1) {
            list.setItemChecked(i, true);
            String str = TAG;
            if (Log.isLoggable(str, 2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("List item ");
                stringBuilder.append(this.mSelectedItemIndex);
                stringBuilder.append(" should be selected");
                Log.v(str, stringBuilder.toString());
            }
        }
    }
}
