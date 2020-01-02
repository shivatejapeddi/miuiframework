package com.miui.internal.search;

import android.content.Context;
import android.provider.MiuiSettings.SettingsCloudData;
import android.provider.MiuiSettings.SettingsCloudData.CloudData;
import android.util.ArrayMap;
import android.util.Log;
import com.miui.internal.search.CloudKeywordsHolder.Builder;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class KeywordsCloudConfigHelper {
    private static final String CONFIG_VERSION = "version";
    private static final String KEYWORDS = "keywords";
    private static final String KEYWORDS_CLOUD_CONFIG_MODULE_NAME = "Search";
    private static final String KEYWORDS_FILENAME = "search_keyswords.json";
    private static final String KEYWORDS_ZH_CH_FILENAME = "search_keywords_zh_cn.json";
    private static final String LOCALE = "locale";
    private static final String TAG = "KeywordCloudHelper";
    private static final String ZH_CN = "zh_CN";
    private static volatile KeywordsCloudConfigHelper sCloudConfigHelper = null;
    private Context mContext;
    private File mFile;
    private CloudKeywordsHolder mKeywordsHolder;
    private String mLocale = Locale.getDefault().toString();

    public static KeywordsCloudConfigHelper getInstance(Context context) {
        if (sCloudConfigHelper == null || localeHasChange(sCloudConfigHelper.mLocale)) {
            synchronized (KeywordsCloudConfigHelper.class) {
                if (sCloudConfigHelper == null || localeHasChange(sCloudConfigHelper.mLocale)) {
                    sCloudConfigHelper = new KeywordsCloudConfigHelper(context.getApplicationContext());
                }
            }
        }
        return sCloudConfigHelper;
    }

    private static boolean localeHasChange(String locale) {
        return Locale.getDefault().toString().equals(locale) ^ 1;
    }

    private KeywordsCloudConfigHelper(Context context) {
        this.mContext = context;
        initConfig();
    }

    public void initConfig() {
        JSONObject configJson = parseKeywordsCloudFile(this.mLocale);
        if (configJson != null) {
            this.mKeywordsHolder = buildCloudKeywordsHolderFromConfig(configJson);
        }
    }

    private JSONObject parseKeywordsCloudFile(String locale) {
        String fileName = ZH_CN.equals(locale) ? KEYWORDS_ZH_CH_FILENAME : KEYWORDS_FILENAME;
        File path = this.mContext.getFilesDir();
        if (!path.exists()) {
            path.mkdirs();
        }
        this.mFile = new File(path, fileName);
        if (this.mFile.exists()) {
            try {
                return SearchUtils.readJSONObject(new FileInputStream(this.mFile));
            } catch (Exception e) {
                Log.e(TAG, "parse Keywords cloud file fail!", e);
            }
        }
        return null;
    }

    private synchronized CloudKeywordsHolder buildCloudKeywordsHolderFromConfig(JSONObject json) {
        String version;
        String locale;
        JSONObject itemsJson;
        version = json.optString("version");
        locale = json.optString("locale");
        itemsJson = json.optJSONObject("keywords");
        ArrayMap<String, String> keywords = new ArrayMap();
        try {
        } catch (JSONException e) {
            Log.e(TAG, "parse CloudKeywords fail!", e);
            return null;
        }
        return new Builder().setVersion(version).setLocale(locale).setKeywords(SearchUtils.jsonToMap(itemsJson)).build();
    }

    public synchronized void updateKeywordsCloudConfig(Context context) {
        try {
            List<CloudData> cloudDatas = SettingsCloudData.getCloudDataList(context.getContentResolver(), KEYWORDS_CLOUD_CONFIG_MODULE_NAME);
            if (cloudDatas != null) {
                for (CloudData cloudData : cloudDatas) {
                    JSONObject jsonObject = cloudData.json();
                    if (jsonObject != null) {
                        if (needToUpdate(cloudData)) {
                            this.mKeywordsHolder = buildCloudKeywordsHolderFromConfig(jsonObject);
                            if (this.mKeywordsHolder != null) {
                                SearchUtils.writeJsonToFile(jsonObject, this.mFile);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "update Keywords cloud config fail!", e);
        }
        return;
    }

    private boolean needToUpdate(CloudData cloudData) {
        boolean z = false;
        if (!cloudData.getString("locale", "").contains(this.mLocale)) {
            return false;
        }
        int nowVersion = SearchUtils.parseInt(cloudData.getString("version", "-1"));
        CloudKeywordsHolder cloudKeywordsHolder = this.mKeywordsHolder;
        if (cloudKeywordsHolder == null) {
            return true;
        }
        if (nowVersion > SearchUtils.parseInt(cloudKeywordsHolder.getVersion())) {
            z = true;
        }
        return z;
    }

    public synchronized String getKeywords(String key) {
        if (this.mKeywordsHolder != null) {
            if (!this.mKeywordsHolder.isEmpty()) {
                return this.mKeywordsHolder.getKeywords(key);
            }
        }
        return "";
    }
}
