package android.provider;

import android.annotation.SystemApi;
import android.content.Context;
import java.util.Locale;

@SystemApi
public abstract class SearchIndexableData {
    public String className;
    public Context context;
    public boolean enabled;
    public int iconResId;
    public String intentAction;
    public String intentTargetClass;
    public String intentTargetPackage;
    public String key;
    public Locale locale;
    public String packageName;
    public int rank;
    public int userId;

    public SearchIndexableData() {
        this.userId = -1;
        this.locale = Locale.getDefault();
        this.enabled = true;
    }

    public SearchIndexableData(Context ctx) {
        this();
        this.context = ctx;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SearchIndexableData[context: ");
        sb.append(this.context);
        String str = ", ";
        sb.append(str);
        sb.append("locale: ");
        sb.append(this.locale);
        sb.append(str);
        sb.append("enabled: ");
        sb.append(this.enabled);
        sb.append(str);
        sb.append("rank: ");
        sb.append(this.rank);
        sb.append(str);
        sb.append("key: ");
        sb.append(this.key);
        sb.append(str);
        sb.append("userId: ");
        sb.append(this.userId);
        sb.append(str);
        sb.append("className: ");
        sb.append(this.className);
        sb.append(str);
        sb.append("packageName: ");
        sb.append(this.packageName);
        sb.append(str);
        sb.append("iconResId: ");
        sb.append(this.iconResId);
        sb.append(str);
        sb.append("intentAction: ");
        sb.append(this.intentAction);
        sb.append(str);
        sb.append("intentTargetPackage: ");
        sb.append(this.intentTargetPackage);
        sb.append(str);
        sb.append("intentTargetClass: ");
        sb.append(this.intentTargetClass);
        sb.append("]");
        return sb.toString();
    }
}
