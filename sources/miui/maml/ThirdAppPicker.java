package miui.maml;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ResolveInfo.DisplayNameComparator;
import android.miui.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;
import miui.app.Activity;

public class ThirdAppPicker extends Activity implements OnItemClickListener {
    private List<ResolveInfo> mAllApps;
    private FileListAdapter mListAdapter;
    private ListView mListView;
    private PackageManager mPackageManager;

    private class FileListAdapter extends ArrayAdapter<ResolveInfo> {
        private LayoutInflater mInflater;

        public FileListAdapter(Context context, int resource, List<ResolveInfo> objects) {
            super(context, resource, (List) objects);
            this.mInflater = LayoutInflater.from(context);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView != null) {
                view = convertView;
            } else {
                view = this.mInflater.inflate((int) R.layout.app_list_item, parent, false);
            }
            ResolveInfo item = (ResolveInfo) ThirdAppPicker.this.mAllApps.get(position);
            TextView name = (TextView) view.findViewById(R.id.app_name);
            ((ImageView) view.findViewById(R.id.app_icon)).setImageDrawable(item == null ? null : item.loadIcon(ThirdAppPicker.this.mPackageManager));
            if (item != null) {
                name.setText(item.loadLabel(ThirdAppPicker.this.mPackageManager));
            } else {
                name.setText((int) R.string.default_name);
            }
            return view;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_picker);
        getActionBar().setTitle(R.string.resource_select);
        getActionBar().setHomeButtonEnabled(true);
        this.mPackageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        this.mAllApps = this.mPackageManager.queryIntentActivities(intent, 0);
        Collections.sort(this.mAllApps, new DisplayNameComparator(this.mPackageManager));
        this.mAllApps.add(null);
        this.mListView = (ListView) findViewById(R.id.list);
        this.mListAdapter = new FileListAdapter(this, R.layout.app_list_item, this.mAllApps);
        this.mListView.setAdapter(this.mListAdapter);
        this.mListView.setOnItemClickListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        ResolveInfo item = (ResolveInfo) this.mAllApps.get(position);
        Intent intent = new Intent();
        if (item != null) {
            intent.putExtra("name", item.loadLabel(this.mPackageManager));
            intent.setClassName(item.activityInfo.packageName, item.activityInfo.name);
        }
        setResult(-1, item != null ? intent : null);
        finish();
    }
}
