package android.accounts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class ChooseAccountTypeActivity extends Activity {
    private static final String TAG = "AccountChooser";
    private ArrayList<AuthInfo> mAuthenticatorInfosToDisplay;
    private HashMap<String, AuthInfo> mTypeToAuthenticatorInfo = new HashMap();

    private static class AccountArrayAdapter extends ArrayAdapter<AuthInfo> {
        private ArrayList<AuthInfo> mInfos;
        private LayoutInflater mLayoutInflater;

        public AccountArrayAdapter(Context context, int textViewResourceId, ArrayList<AuthInfo> infos) {
            super(context, textViewResourceId, (List) infos);
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
            holder.text.setText(((AuthInfo) this.mInfos.get(position)).name);
            holder.icon.setImageDrawable(((AuthInfo) this.mInfos.get(position)).drawable);
            return convertView;
        }
    }

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
        String type;
        super.onCreate(savedInstanceState);
        String str = TAG;
        if (Log.isLoggable(str, 2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ChooseAccountTypeActivity.onCreate(savedInstanceState=");
            stringBuilder.append(savedInstanceState);
            stringBuilder.append(")");
            Log.v(str, stringBuilder.toString());
        }
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
            setContentView((int) R.layout.choose_account_type);
            ListView list = (ListView) findViewById(16908298);
            list.setAdapter(new AccountArrayAdapter(this, 17367043, this.mAuthenticatorInfosToDisplay));
            list.setChoiceMode(0);
            list.setTextFilterEnabled(false);
            list.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                    ChooseAccountTypeActivity chooseAccountTypeActivity = ChooseAccountTypeActivity.this;
                    chooseAccountTypeActivity.setResultAndFinish(((AuthInfo) chooseAccountTypeActivity.mAuthenticatorInfosToDisplay.get(position)).desc.type);
                }
            });
        }
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

    private void buildTypeToAuthDescriptionMap() {
        StringBuilder stringBuilder;
        String str = TAG;
        for (AuthenticatorDescription desc : AccountManager.get(this).getAuthenticatorTypes()) {
            String name = null;
            Drawable icon = null;
            try {
                Context authContext = createPackageContext(desc.packageName, 0);
                icon = authContext.getDrawable(desc.iconId);
                CharSequence sequence = authContext.getResources().getText(desc.labelId);
                if (sequence != null) {
                    name = sequence.toString();
                }
                name = sequence.toString();
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
            this.mTypeToAuthenticatorInfo.put(desc.type, new AuthInfo(desc, name, icon));
        }
    }
}
