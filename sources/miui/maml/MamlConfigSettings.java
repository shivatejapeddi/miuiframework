package miui.maml;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.miui.R;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import miui.app.constants.ResourceBrowserConstants;
import miui.content.res.ThemeResources;
import miui.maml.util.ConfigFile;
import miui.maml.util.Task;
import miui.maml.util.Utils;
import miui.maml.util.Utils.XmlTraverseListener;
import miui.preference.PreferenceActivity;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class MamlConfigSettings extends PreferenceActivity implements OnPreferenceChangeListener, OnPreferenceClickListener {
    private static final String CONFIG_NAME = "config.xml";
    public static final String EXTRA_MAML_CODE = "maml_code";
    public static final String EXTRA_MAML_ID = "maml_id";
    public static final String EXTRA_MAML_PATH = "maml_path";
    private static final String LOG_TAG = "MamlConfigSettings";
    private static final String TAG_ROOT = "Config";
    private ConfigFileHelper mConfigFileHelper;
    private int mNextRequestCode = 100;
    private HashMap<String, Item> mPreferenceMap = new HashMap();
    private HashMap<Integer, Object> mRequestCodeObjMap = new HashMap();

    private abstract class Item {
        protected String mDefaultValue;
        protected String mId;
        protected Preference mPreference;
        protected String mSummary;
        protected String mTitle;

        public abstract boolean OnValueChange(Object obj);

        public abstract Preference createPreference(Context context);

        public abstract void updateValue();

        private Item() {
        }

        /* synthetic */ Item(MamlConfigSettings x0, AnonymousClass1 x1) {
            this();
        }

        public final boolean build(PreferenceCategory category, Element ele) {
            this.mId = ele.getAttribute("id");
            this.mTitle = ele.getAttribute("text");
            this.mSummary = ele.getAttribute("summary");
            this.mDefaultValue = ele.getAttribute("default");
            this.mPreference = createPreference(MamlConfigSettings.this);
            Preference preference = this.mPreference;
            if (preference == null) {
                return false;
            }
            category.addPreference(preference);
            this.mPreference.setTitle(this.mTitle);
            this.mPreference.setSummary(this.mSummary);
            this.mPreference.setKey(this.mId);
            this.mPreference.setOnPreferenceChangeListener(MamlConfigSettings.this);
            this.mPreference.setPersistent(false);
            onBuild(ele);
            updateValue();
            return true;
        }

        public boolean onClick() {
            return false;
        }

        /* Access modifiers changed, original: protected */
        public void onBuild(Element ele) {
        }

        /* Access modifiers changed, original: protected */
        public void setValuePreview(String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value == null ? "" : value);
            if (!TextUtils.isEmpty(this.mSummary)) {
                sb.append("\n");
                sb.append(this.mSummary);
            }
            this.mPreference.setSummary(sb.toString());
        }
    }

    private abstract class PickerItem extends Item {
        protected int mRequestCode;

        public abstract boolean onActivityResult(int i, Intent intent);

        private PickerItem() {
            super(MamlConfigSettings.this, null);
        }

        /* synthetic */ PickerItem(MamlConfigSettings x0, AnonymousClass1 x1) {
            this();
        }

        /* Access modifiers changed, original: protected */
        public Preference createPreference(Context c) {
            return new Preference(c);
        }

        /* Access modifiers changed, original: protected */
        public void onBuild(Element ele) {
            this.mPreference.setOnPreferenceClickListener(MamlConfigSettings.this);
            this.mRequestCode = MamlConfigSettings.this.getNextRequestCode();
            MamlConfigSettings.this.putRequestCodeObj(this.mRequestCode, this);
        }
    }

    private class AppPickerItem extends PickerItem {
        private Task mDefaultTask;

        private AppPickerItem() {
            super(MamlConfigSettings.this, null);
        }

        /* synthetic */ AppPickerItem(MamlConfigSettings x0, AnonymousClass1 x1) {
            this();
        }

        /* Access modifiers changed, original: protected */
        public void onBuild(Element ele) {
            super.onBuild(ele);
            this.mDefaultTask = Task.load(ele);
        }

        public boolean onClick() {
            MamlConfigSettings.this.startActivityForResult(new Intent(MamlConfigSettings.this, ThirdAppPicker.class), this.mRequestCode);
            return true;
        }

        public boolean OnValueChange(Object objValue) {
            Task task = (Task) objValue;
            MamlConfigSettings.this.mConfigFileHelper.getConfigFile().putTask(task);
            String str = (task == null || task.name == null) ? "" : task.name;
            setValuePreview(str);
            return true;
        }

        public void updateValue() {
            Task task = MamlConfigSettings.this.mConfigFileHelper.getConfigFile().getTask(this.mId);
            if (task != null) {
                setValuePreview(task.name);
            } else {
                OnValueChange(this.mDefaultTask);
            }
        }

        public boolean onActivityResult(int resultCode, Intent data) {
            if (resultCode != -1) {
                return false;
            }
            Task task = new Task();
            task.id = this.mId;
            if (data != null) {
                task.name = data.getStringExtra("name");
                task.packageName = data.getComponent().getPackageName();
                task.className = data.getComponent().getClassName();
                task.action = Intent.ACTION_MAIN;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("selected component: ");
                stringBuilder.append(task.packageName);
                stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                stringBuilder.append(task.className);
                Log.i("AppPickerItem", stringBuilder.toString());
            } else {
                task = this.mDefaultTask;
            }
            return OnValueChange(task);
        }
    }

    private abstract class VariableItem extends Item {
        public abstract void setValue(String str);

        private VariableItem() {
            super(MamlConfigSettings.this, null);
        }

        /* synthetic */ VariableItem(MamlConfigSettings x0, AnonymousClass1 x1) {
            this();
        }

        public void updateValue() {
            String str = MamlConfigSettings.this.mConfigFileHelper.getConfigFile().getVariable(this.mId);
            if (str != null) {
                setValue(str);
                return;
            }
            setValue(this.mDefaultValue);
            OnValueChange(this.mDefaultValue);
        }
    }

    private class CheckboxItem extends VariableItem {
        private CheckboxItem() {
            super(MamlConfigSettings.this, null);
        }

        /* synthetic */ CheckboxItem(MamlConfigSettings x0, AnonymousClass1 x1) {
            this();
        }

        /* Access modifiers changed, original: protected */
        public Preference createPreference(Context c) {
            return new CheckBoxPreference(c);
        }

        /* Access modifiers changed, original: protected */
        public void setValue(String value) {
            ((CheckBoxPreference) this.mPreference).setChecked("1".equals(value));
        }

        public boolean OnValueChange(Object objValue) {
            boolean isTrue = false;
            String str = "1";
            if (objValue instanceof Boolean) {
                isTrue = ((Boolean) objValue).booleanValue();
            } else if (objValue instanceof String) {
                isTrue = str.equals(objValue);
            }
            ConfigFile configFile = MamlConfigSettings.this.mConfigFileHelper.getConfigFile();
            String str2 = this.mId;
            if (!isTrue) {
                str = "0";
            }
            configFile.putNumber(str2, str);
            return true;
        }
    }

    private static class ConfigFileHelper {
        protected Context mAppContext;
        protected ConfigFile mConfigFile;
        private String mPath;
        private ZipFile mZipFile;

        public ConfigFileHelper(String mamlPath, Context context) {
            this.mPath = mamlPath;
            this.mAppContext = context;
            String str = this.mPath;
            if (str != null) {
                try {
                    this.mZipFile = new ZipFile(str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.mConfigFile = new ConfigFile();
            this.mConfigFile.load(getConfigPath());
        }

        public String getConfigPath() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.mPath);
            stringBuilder.append(".config");
            return stringBuilder.toString();
        }

        public InputStream getConfigFileStream(Locale locale) throws IOException {
            String str = MamlConfigSettings.CONFIG_NAME;
            if (locale != null) {
                InputStream is = getFileStream(Utils.addFileNameSuffix(str, locale.toString()));
                if (is != null) {
                    return is;
                }
                is = getFileStream(Utils.addFileNameSuffix(str, locale.getLanguage()));
                if (is != null) {
                    return is;
                }
            }
            return getFileStream(str);
        }

        public boolean containsFile(String file) {
            ZipFile zipFile = this.mZipFile;
            return (zipFile == null || zipFile.getEntry(file) == null) ? false : true;
        }

        /* Access modifiers changed, original: protected */
        public InputStream getFileStream(String file) throws IOException {
            ZipEntry ze = this.mZipFile;
            if (ze == null) {
                return null;
            }
            ze = ze.getEntry(file);
            if (ze == null) {
                return null;
            }
            return this.mZipFile.getInputStream(ze);
        }

        public ConfigFile getConfigFile() {
            return this.mConfigFile;
        }

        public void save() {
            this.mConfigFile.save(this.mAppContext);
        }

        public void close() {
            ZipFile zipFile = this.mZipFile;
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ImagePickerItem extends PickerItem {
        private ImagePickerItem() {
            super(MamlConfigSettings.this, null);
        }

        /* synthetic */ ImagePickerItem(MamlConfigSettings x0, AnonymousClass1 x1) {
            this();
        }

        public boolean onClick() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            MamlConfigSettings.this.startActivityForResult(Intent.createChooser(intent, null), this.mRequestCode);
            return true;
        }

        public boolean OnValueChange(Object objValue) {
            Uri uri = (Uri) objValue;
            String suri = uri == null ? null : uri.toString();
            MamlConfigSettings.this.mConfigFileHelper.getConfigFile().putString(this.mId, suri);
            setValuePreview(suri);
            return true;
        }

        public void updateValue() {
            setValuePreview(MamlConfigSettings.this.mConfigFileHelper.getConfigFile().getVariable(this.mId));
        }

        public boolean onActivityResult(int resultCode, Intent data) {
            return resultCode == -1 && OnValueChange(data.getData());
        }
    }

    private static class LockscreenConfigFileHelper extends ConfigFileHelper {
        private String mId;

        public LockscreenConfigFileHelper(String id, Context context) {
            super(null, context);
            this.mId = id;
        }

        public boolean containsFile(String file) {
            return ThemeResources.getSystem().containsAwesomeLockscreenEntry(file);
        }

        /* Access modifiers changed, original: protected */
        public InputStream getFileStream(String file) {
            return ThemeResources.getSystem().getAwesomeLockscreenFileStream(file, null);
        }

        public String getConfigPath() {
            return "/data/system/theme/config.config";
        }

        public void save() {
            super.save();
            String path = new StringBuilder();
            path.append(ResourceBrowserConstants.MAML_CONFIG_PATH);
            path.append("lockstyle");
            path.append("/");
            path.append(this.mId);
            path.append(".config");
            this.mConfigFile.save(path.toString(), this.mAppContext);
        }
    }

    private abstract class ValueChoiceItem extends VariableItem {
        protected ArrayList<String> mItemsText;
        protected ArrayList<String> mItemsValue;

        private ValueChoiceItem() {
            super(MamlConfigSettings.this, null);
            this.mItemsText = new ArrayList();
            this.mItemsValue = new ArrayList();
        }

        /* synthetic */ ValueChoiceItem(MamlConfigSettings x0, AnonymousClass1 x1) {
            this();
        }

        /* Access modifiers changed, original: protected */
        public Preference createPreference(Context c) {
            return new ListPreference(c);
        }

        /* Access modifiers changed, original: protected */
        public void onBuild(Element ele) {
            this.mItemsText.clear();
            this.mItemsValue.clear();
            Utils.traverseXmlElementChildren(ele, ListItemElement.TAG_NAME, new XmlTraverseListener() {
                public void onChild(Element child) {
                    ValueChoiceItem.this.mItemsText.add(child.getAttribute("text"));
                    ValueChoiceItem.this.mItemsValue.add(child.getAttribute("value"));
                }
            });
            ListPreference pre = this.mPreference;
            ArrayList arrayList = this.mItemsText;
            pre.setEntries((CharSequence[]) arrayList.toArray(new String[arrayList.size()]));
            arrayList = this.mItemsValue;
            pre.setEntryValues((CharSequence[]) arrayList.toArray(new String[arrayList.size()]));
            pre.setDialogTitle((CharSequence) this.mTitle);
        }

        /* Access modifiers changed, original: protected */
        public void setValue(String value) {
            ListPreference pre = this.mPreference;
            int index = pre.findIndexOfValue(value);
            if (index != -1) {
                pre.setValueIndex(index);
                setValuePreview(pre.getEntries()[index].toString());
            }
        }

        public boolean OnValueChange(Object objValue) {
            ListPreference pre = this.mPreference;
            int index = pre.findIndexOfValue((String) objValue);
            if (index == -1) {
                return false;
            }
            setValuePreview(pre.getEntries()[index].toString());
            return true;
        }
    }

    private class NumberChoiceItem extends ValueChoiceItem {
        private NumberChoiceItem() {
            super(MamlConfigSettings.this, null);
        }

        /* synthetic */ NumberChoiceItem(MamlConfigSettings x0, AnonymousClass1 x1) {
            this();
        }

        public boolean OnValueChange(Object objValue) {
            super.OnValueChange(objValue);
            MamlConfigSettings.this.mConfigFileHelper.getConfigFile().putNumber(this.mId, (String) objValue);
            return true;
        }
    }

    private class NumberInputItem extends VariableItem {
        private NumberInputItem() {
            super(MamlConfigSettings.this, null);
        }

        /* synthetic */ NumberInputItem(MamlConfigSettings x0, AnonymousClass1 x1) {
            this();
        }

        /* Access modifiers changed, original: protected */
        public Preference createPreference(Context c) {
            return new EditTextPreference(c);
        }

        /* Access modifiers changed, original: protected */
        public void setValue(String value) {
            value = getValueString(value);
            ((EditTextPreference) this.mPreference).setText(value);
            setValuePreview(value);
        }

        private String getValueString(String value) {
            double d = 0.0d;
            try {
                d = Double.parseDouble(value);
            } catch (NumberFormatException e) {
            }
            return Utils.doubleToString(d);
        }

        public boolean OnValueChange(Object objValue) {
            try {
                String value = Utils.doubleToString(Double.parseDouble((String) objValue));
                MamlConfigSettings.this.mConfigFileHelper.getConfigFile().putNumber(this.mId, value);
                setValuePreview(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        /* Access modifiers changed, original: protected */
        public void onBuild(Element ele) {
            super.onBuild(ele);
            ((EditTextPreference) this.mPreference).setDialogTitle((CharSequence) this.mTitle);
        }
    }

    private class StringChoiceItem extends ValueChoiceItem {
        private StringChoiceItem() {
            super(MamlConfigSettings.this, null);
        }

        /* synthetic */ StringChoiceItem(MamlConfigSettings x0, AnonymousClass1 x1) {
            this();
        }

        public boolean OnValueChange(Object objValue) {
            super.OnValueChange(objValue);
            MamlConfigSettings.this.mConfigFileHelper.getConfigFile().putString(this.mId, (String) objValue);
            return true;
        }
    }

    private class StringInputItem extends VariableItem {
        private StringInputItem() {
            super(MamlConfigSettings.this, null);
        }

        /* synthetic */ StringInputItem(MamlConfigSettings x0, AnonymousClass1 x1) {
            this();
        }

        /* Access modifiers changed, original: protected */
        public Preference createPreference(Context c) {
            return new EditTextPreference(c);
        }

        /* Access modifiers changed, original: protected */
        public void setValue(String value) {
            ((EditTextPreference) this.mPreference).setText(value);
            setValuePreview(value);
        }

        public boolean OnValueChange(Object objValue) {
            String value = (String) objValue;
            MamlConfigSettings.this.mConfigFileHelper.getConfigFile().putString(this.mId, value);
            setValuePreview(value);
            return true;
        }

        /* Access modifiers changed, original: protected */
        public void onBuild(Element ele) {
            super.onBuild(ele);
            ((EditTextPreference) this.mPreference).setDialogTitle((CharSequence) this.mTitle);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mConfigFileHelper = createConfigFileHelper(this, getIntent().getStringExtra(EXTRA_MAML_CODE), getIntent().getStringExtra(EXTRA_MAML_PATH), getIntent().getStringExtra(EXTRA_MAML_ID));
        if (this.mConfigFileHelper.containsFile(CONFIG_NAME)) {
            setContentView(R.layout.component_list);
            getActionBar().setTitle(R.string.maml_component_customization_title);
            getActionBar().setHomeButtonEnabled(true);
            createPreferenceScreen();
            return;
        }
        finish();
    }

    private static ConfigFileHelper createConfigFileHelper(Context context, String code, String path, String id) {
        if (!"lockstyle".equals(code)) {
            return new ConfigFileHelper(path, context.getApplicationContext());
        }
        if (TextUtils.isEmpty(id)) {
            id = getComponentId(context, code);
        }
        return new LockscreenConfigFileHelper(id, context.getApplicationContext());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPause() {
        super.onPause();
        this.mConfigFileHelper.save();
    }

    public void onDestroy() {
        this.mConfigFileHelper.save();
        this.mConfigFileHelper.close();
        super.onDestroy();
    }

    public static boolean containsConfig(String packagePath) {
        return containsConfig(packagePath, null);
    }

    public static boolean containsConfig(String packagePath, String innerPath) {
        boolean z = false;
        if (packagePath == null) {
            return false;
        }
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(packagePath);
            String entryName = new StringBuilder();
            entryName.append(innerPath == null ? "" : innerPath);
            entryName.append(CONFIG_NAME);
            if (zipfile.getEntry(entryName.toString()) != null) {
                z = true;
            }
            try {
                zipfile.close();
            } catch (IOException e) {
            }
            return z;
        } catch (IOException e2) {
            e2.printStackTrace();
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e3) {
                }
            }
            return false;
        } catch (Throwable th) {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e4) {
                }
            }
        }
    }

    public static boolean containsLockstyleConfig() {
        return ThemeResources.getSystem().containsAwesomeLockscreenEntry(CONFIG_NAME);
    }

    private void createPreferenceScreen() {
        final PreferenceScreen rootScreen = getPreferenceManager().createPreferenceScreen(this);
        setPreferenceScreen(rootScreen);
        InputStream is = null;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            is = this.mConfigFileHelper.getConfigFileStream(getResources().getConfiguration().locale);
            if (is == null) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
                return;
            }
            Element root = db.parse(is).getDocumentElement();
            if (root == null) {
                try {
                    is.close();
                } catch (IOException e2) {
                }
            } else if (root.getNodeName().equals(TAG_ROOT)) {
                Utils.traverseXmlElementChildren(root, "Group", new XmlTraverseListener() {
                    public void onChild(Element child) {
                        MamlConfigSettings.this.createGroup(rootScreen, child);
                    }
                });
                try {
                    is.close();
                } catch (IOException e3) {
                }
            } else {
                try {
                    is.close();
                } catch (IOException e4) {
                }
            }
        } catch (ParserConfigurationException e5) {
            e5.printStackTrace();
            if (is != null) {
                is.close();
            }
        } catch (SAXException e6) {
            e6.printStackTrace();
            if (is != null) {
                is.close();
            }
        } catch (IOException e7) {
            e7.printStackTrace();
            if (is != null) {
                is.close();
            }
        } catch (Exception e8) {
            e8.printStackTrace();
            if (is != null) {
                is.close();
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e9) {
                }
            }
        }
    }

    private void createGroup(PreferenceScreen rootScreen, Element group) {
        if (group != null) {
            final PreferenceCategory category = new PreferenceCategory(this);
            rootScreen.addPreference(category);
            category.setTitle((CharSequence) group.getAttribute("text"));
            category.setSummary((CharSequence) group.getAttribute("summary"));
            Utils.traverseXmlElementChildren(group, null, new XmlTraverseListener() {
                public void onChild(Element child) {
                    Item item = MamlConfigSettings.this.createItem(child.getNodeName());
                    if (item != null && item.build(category, child)) {
                        MamlConfigSettings.this.mPreferenceMap.put(item.mId, item);
                    }
                }
            });
        }
    }

    private Item createItem(String tag) {
        if (ConfigFile.TAG_STRING_INPUT.equals(tag)) {
            return new StringInputItem(this, null);
        }
        if (ConfigFile.TAG_CHECK_BOX.equals(tag)) {
            return new CheckboxItem(this, null);
        }
        if (ConfigFile.TAG_NUMBER_INPUT.equals(tag)) {
            return new NumberInputItem(this, null);
        }
        if (ConfigFile.TAG_STRING_CHOICE.equals(tag)) {
            return new StringChoiceItem(this, null);
        }
        if (ConfigFile.TAG_NUMBER_CHOICE.equals(tag)) {
            return new NumberChoiceItem(this, null);
        }
        if (ConfigFile.TAG_APP_PICKER.equals(tag)) {
            return new AppPickerItem(this, null);
        }
        if (ConfigFile.TAG_IMAGE_PICKER.equals(tag)) {
            return new ImagePickerItem(this, null);
        }
        return null;
    }

    private int getNextRequestCode() {
        int i = this.mNextRequestCode;
        this.mNextRequestCode = i + 1;
        return i;
    }

    private void putRequestCodeObj(int code, Object obj) {
        this.mRequestCodeObjMap.put(Integer.valueOf(code), obj);
    }

    private Object getRequestCodeObj(int code) {
        return this.mRequestCodeObjMap.get(Integer.valueOf(code));
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        Item item = (Item) this.mPreferenceMap.get(preference.getKey());
        if (item == null) {
            return false;
        }
        boolean ret = item.OnValueChange(objValue);
        if (ret) {
            this.mConfigFileHelper.save();
        }
        return ret;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        PickerItem obj = getRequestCodeObj(requestCode);
        if (obj != null && (obj instanceof PickerItem) && obj.onActivityResult(resultCode, data)) {
            this.mConfigFileHelper.save();
        }
    }

    public boolean onPreferenceClick(Preference preference) {
        Item item = (Item) this.mPreferenceMap.get(preference.getKey());
        return item != null && item.onClick();
    }

    public static String getComponentId(Context c, String code) {
        ContentResolver cr = c.getContentResolver();
        String[] projection = new String[]{"local_id"};
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("content://com.android.thememanager.provider/");
        stringBuilder.append(code);
        Cursor cs = cr.query(Uri.parse(stringBuilder.toString()), projection, null, null, null);
        if (cs != null) {
            cs.moveToFirst();
            if (cs.getCount() != 0) {
                return cs.getString(0);
            }
        }
        return null;
    }
}
