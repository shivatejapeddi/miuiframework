package android.accounts;

import android.app.Activity;
import android.app.ActivityTaskManager;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.R;
import java.util.HashMap;

public class ChooseAccountActivity extends Activity {
    private static final String TAG = "AccountManager";
    private AccountManagerResponse mAccountManagerResponse = null;
    private Parcelable[] mAccounts = null;
    private String mCallingPackage;
    private int mCallingUid;
    private Bundle mResult;
    private HashMap<String, AuthenticatorDescription> mTypeToAuthDescription = new HashMap();

    private static class AccountArrayAdapter extends ArrayAdapter<AccountInfo> {
        private AccountInfo[] mInfos;
        private LayoutInflater mLayoutInflater;

        public AccountArrayAdapter(Context context, int textViewResourceId, AccountInfo[] infos) {
            super(context, textViewResourceId, (Object[]) infos);
            this.mInfos = infos;
            this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = this.mLayoutInflater.inflate((int) R.layout.choose_account_row, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.account_row_text);
                holder.icon = (ImageView) convertView.findViewById(R.id.account_row_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text.setText(this.mInfos[position].name);
            holder.icon.setImageDrawable(this.mInfos[position].drawable);
            return convertView;
        }
    }

    private static class AccountInfo {
        final Drawable drawable;
        final String name;

        AccountInfo(String name, Drawable drawable) {
            this.name = name;
            this.drawable = drawable;
        }
    }

    private static class ViewHolder {
        ImageView icon;
        TextView text;

        private ViewHolder() {
        }

        /* synthetic */ ViewHolder(AnonymousClass1 x0) {
            this();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAccounts = getIntent().getParcelableArrayExtra(AccountManager.KEY_ACCOUNTS);
        this.mAccountManagerResponse = (AccountManagerResponse) getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_MANAGER_RESPONSE);
        if (this.mAccounts == null) {
            setResult(0);
            finish();
            return;
        }
        try {
            IBinder activityToken = getActivityToken();
            this.mCallingUid = ActivityTaskManager.getService().getLaunchedFromUid(activityToken);
            this.mCallingPackage = ActivityTaskManager.getService().getLaunchedFromPackage(activityToken);
        } catch (RemoteException re) {
            String simpleName = getClass().getSimpleName();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to get caller identity \n");
            stringBuilder.append(re);
            Log.w(simpleName, stringBuilder.toString());
        }
        boolean isSameApp = UserHandle.isSameApp(this.mCallingUid, 1000);
        String str = AccountManager.KEY_ANDROID_PACKAGE_NAME;
        if (isSameApp && getIntent().getStringExtra(str) != null) {
            this.mCallingPackage = getIntent().getStringExtra(str);
        }
        if (!(UserHandle.isSameApp(this.mCallingUid, 1000) || getIntent().getStringExtra(str) == null)) {
            String simpleName2 = getClass().getSimpleName();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Non-system Uid: ");
            stringBuilder2.append(this.mCallingUid);
            stringBuilder2.append(" tried to override packageName \n");
            Log.w(simpleName2, stringBuilder2.toString());
        }
        getAuthDescriptions();
        AccountInfo[] mAccountInfos = new AccountInfo[this.mAccounts.length];
        int i = 0;
        while (true) {
            Parcelable[] parcelableArr = this.mAccounts;
            if (i < parcelableArr.length) {
                mAccountInfos[i] = new AccountInfo(((Account) parcelableArr[i]).name, getDrawableForType(((Account) this.mAccounts[i]).type));
                i++;
            } else {
                setContentView((int) R.layout.choose_account);
                ListView list = (ListView) findViewById(16908298);
                list.setAdapter(new AccountArrayAdapter(this, 17367043, mAccountInfos));
                list.setChoiceMode(1);
                list.setTextFilterEnabled(true);
                list.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        ChooseAccountActivity.this.onListItemClick((ListView) parent, v, position, id);
                    }
                });
                return;
            }
        }
    }

    private void getAuthDescriptions() {
        for (AuthenticatorDescription desc : AccountManager.get(this).getAuthenticatorTypes()) {
            this.mTypeToAuthDescription.put(desc.type, desc);
        }
    }

    private Drawable getDrawableForType(String accountType) {
        StringBuilder stringBuilder;
        String str = TAG;
        if (!this.mTypeToAuthDescription.containsKey(accountType)) {
            return null;
        }
        try {
            AuthenticatorDescription desc = (AuthenticatorDescription) this.mTypeToAuthDescription.get(accountType);
            str = createPackageContext(desc.packageName, 0).getDrawable(desc.iconId);
            return str;
        } catch (NameNotFoundException e) {
            if (!Log.isLoggable(str, 5)) {
                return null;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("No icon name for account type ");
            stringBuilder.append(accountType);
            Log.w(str, stringBuilder.toString());
            return null;
        } catch (NotFoundException e2) {
            if (!Log.isLoggable(str, 5)) {
                return null;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("No icon resource for account type ");
            stringBuilder.append(accountType);
            Log.w(str, stringBuilder.toString());
            return null;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onListItemClick(ListView l, View v, int position, long id) {
        Account account = this.mAccounts[position];
        AccountManager am = AccountManager.get(this);
        Integer oldVisibility = Integer.valueOf(am.getAccountVisibility(account, this.mCallingPackage));
        if (oldVisibility != null && oldVisibility.intValue() == 4) {
            am.setAccountVisibility(account, this.mCallingPackage, 2);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("selected account ");
        stringBuilder.append(account);
        Log.d(TAG, stringBuilder.toString());
        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        bundle.putString("accountType", account.type);
        this.mResult = bundle;
        finish();
    }

    public void finish() {
        AccountManagerResponse accountManagerResponse = this.mAccountManagerResponse;
        if (accountManagerResponse != null) {
            Bundle bundle = this.mResult;
            if (bundle != null) {
                accountManagerResponse.onResult(bundle);
            } else {
                accountManagerResponse.onError(4, "canceled");
            }
        }
        super.finish();
    }
}
