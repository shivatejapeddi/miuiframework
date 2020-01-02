package android.view;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;

public class ContextThemeWrapper extends ContextWrapper {
    @UnsupportedAppUsage
    private LayoutInflater mInflater;
    private Configuration mOverrideConfiguration;
    @UnsupportedAppUsage
    private Resources mResources;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123768723)
    private Theme mTheme;
    @UnsupportedAppUsage
    private int mThemeResource;

    public ContextThemeWrapper() {
        super(null);
    }

    public ContextThemeWrapper(Context base, int themeResId) {
        super(base);
        this.mThemeResource = themeResId;
    }

    public ContextThemeWrapper(Context base, Theme theme) {
        super(base);
        this.mTheme = theme;
    }

    /* Access modifiers changed, original: protected */
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (this.mResources != null) {
            throw new IllegalStateException("getResources() or getAssets() has already been called");
        } else if (this.mOverrideConfiguration == null) {
            this.mOverrideConfiguration = new Configuration(overrideConfiguration);
        } else {
            throw new IllegalStateException("Override configuration has already been set");
        }
    }

    public Configuration getOverrideConfiguration() {
        return this.mOverrideConfiguration;
    }

    public AssetManager getAssets() {
        return getResourcesInternal().getAssets();
    }

    public Resources getResources() {
        return getResourcesInternal();
    }

    private Resources getResourcesInternal() {
        if (this.mResources == null) {
            Context resContext = this.mOverrideConfiguration;
            if (resContext == null) {
                this.mResources = super.getResources();
            } else {
                this.mResources = createConfigurationContext(resContext).getResources();
            }
        }
        return this.mResources;
    }

    public void setTheme(int resid) {
        if (this.mThemeResource != resid) {
            this.mThemeResource = resid;
            initializeTheme();
        }
    }

    public void setTheme(Theme theme) {
        this.mTheme = theme;
    }

    @UnsupportedAppUsage
    public int getThemeResId() {
        return this.mThemeResource;
    }

    public Theme getTheme() {
        Theme theme = this.mTheme;
        if (theme != null) {
            return theme;
        }
        this.mThemeResource = Resources.selectDefaultTheme(this.mThemeResource, getApplicationInfo().targetSdkVersion);
        initializeTheme();
        return this.mTheme;
    }

    public Object getSystemService(String name) {
        if (!Context.LAYOUT_INFLATER_SERVICE.equals(name)) {
            return getBaseContext().getSystemService(name);
        }
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
        }
        return this.mInflater;
    }

    /* Access modifiers changed, original: protected */
    public void onApplyThemeResource(Theme theme, int resId, boolean first) {
        theme.applyStyle(resId, true);
    }

    @UnsupportedAppUsage
    private void initializeTheme() {
        boolean first = this.mTheme == null;
        if (first) {
            this.mTheme = getResources().newTheme();
            Theme theme = getBaseContext().getTheme();
            if (theme != null) {
                this.mTheme.setTo(theme);
            }
        }
        onApplyThemeResource(this.mTheme, this.mThemeResource, first);
    }
}
