package android.content.res;

import android.app.job.JobInfo;
import android.content.res.MiuiResources.ThemeFileInfoOption;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.DisplayAdjustments;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import miui.content.res.FixedSizeStringBuffer;
import miui.content.res.IconCustomizer;
import miui.content.res.SimulateNinePngUtil;
import miui.content.res.ThemeCompatibility;
import miui.content.res.ThemeNativeUtils;
import miui.content.res.ThemeResources;
import miui.content.res.ThemeResourcesEmpty;
import miui.content.res.ThemeResourcesPackage;
import miui.content.res.ThemeValues;

public final class MiuiResourcesImpl extends ResourcesImpl {
    private static final boolean sMiuiThemeEnabled = ThemeCompatibility.isThemeEnabled();
    private static final SparseArray<Set<PreloadDrawableSource>> sPreloadDrawableSources = new SparseArray();
    public static Long sUpdatedTimeSystem = Long.valueOf(0);
    private Map<Integer, Boolean> mIsPreloadOverlayed = new ConcurrentHashMap();
    private String mPackageName;
    private Stack<Integer> mPreloadingIds = new Stack();
    private ThemeFileInfoOption mPreloadingInfo;
    private Map<Integer, Boolean> mSkipFiles = new ConcurrentHashMap();
    private ThemeResources mThemeResources = ThemeResourcesEmpty.sInstance;
    private ThemeValues mThemeValues = new ThemeValues();
    private long mUpdatedTime = -1;
    private long mValueLoadedTime = -1;

    private class PreloadDrawableSource {
        int mCookie;
        String mResourcePath;
        String mThemePath;

        PreloadDrawableSource(int cookie, String resourcePath, String themePath) {
            this.mCookie = cookie;
            this.mResourcePath = resourcePath;
            this.mThemePath = themePath;
        }
    }

    public MiuiResourcesImpl(AssetManager assets, DisplayMetrics metrics, Configuration config, DisplayAdjustments displayAdjustments) {
        super(assets, metrics, config, displayAdjustments);
    }

    public CharSequence getText(int id) {
        if (sMiuiThemeEnabled) {
            CharSequence cs = getThemeString(id);
            if (cs != null) {
                return cs;
            }
        }
        return null;
    }

    public CharSequence getText(int id, CharSequence def) {
        if (sMiuiThemeEnabled) {
            CharSequence cs = getThemeString(id);
            if (cs != null) {
                return cs;
            }
        }
        return null;
    }

    public void getValue(int id, TypedValue outValue, boolean resolveRefs) {
        super.getValue(id, outValue, resolveRefs);
        resolveOverlayValue(id, outValue);
    }

    /* Access modifiers changed, original: 0000 */
    public void loadOverlayValue(TypedValue outValue, int id) {
        resolveOverlayValue(id, outValue);
    }

    private void resolveOverlayValue(int resId, TypedValue outValue) {
        if (sMiuiThemeEnabled && outValue.resourceId > 0) {
            if ((outValue.type >= 16 && outValue.type <= 31) || outValue.type == 5) {
                loadValues();
                Integer themeInteger = getThemeInt(resId);
                if (themeInteger == null) {
                    themeInteger = getThemeInt(outValue.resourceId);
                }
                if (themeInteger != null) {
                    outValue.data = themeInteger.intValue();
                }
            }
        }
    }

    public int[] getIntArray(int id) {
        if (sMiuiThemeEnabled) {
            int[] array = getThemeIntArray(id);
            if (array != null) {
                return array;
            }
        }
        return null;
    }

    public CharSequence[] getTextArray(int id) {
        if (sMiuiThemeEnabled) {
            CharSequence[] array = getThemeStringArray(id);
            if (array != null) {
                return array;
            }
        }
        return null;
    }

    public String[] getStringArray(int id) {
        if (sMiuiThemeEnabled) {
            String[] array = getThemeStringArray(id);
            if (array != null) {
                return array;
            }
        }
        return null;
    }

    public InputStream openRawResource(int id, TypedValue value) {
        if (sMiuiThemeEnabled && this.mSkipFiles.get(Integer.valueOf(id)) == null) {
            super.getValue(id, value, true);
            ThemeFileInfoOption info = new ThemeFileInfoOption(value, true);
            if (this.mThemeResources.getThemeFile(info)) {
                value.density = info.outDensity;
                return info.outInputStream;
            }
            this.mSkipFiles.put(Integer.valueOf(id), Boolean.valueOf(true));
        }
        return super.openRawResource(id, value);
    }

    /* Access modifiers changed, original: 0000 */
    public Drawable loadOverlayDrawable(MiuiResources wrapper, TypedValue value, int id) {
        if (!sMiuiThemeEnabled || this.mSkipFiles.get(Integer.valueOf(id)) != null) {
            return null;
        }
        ThemeFileInfoOption info = new ThemeFileInfoOption(value, true);
        if (isPreloading()) {
            this.mPreloadingInfo = info;
        }
        if (getThemeFile(info)) {
            try {
                value.density = info.outDensity;
                InputStream is = info.outInputStream;
                if (info.inResourcePath.endsWith(".9.png")) {
                    is = SimulateNinePngUtil.convertIntoNinePngStream(is);
                }
                Drawable dr = Drawable.createFromResourceStream(wrapper, value, is, info.inResourcePath);
                if (dr != null) {
                    dr.setChangingConfigurations(dr.getChangingConfigurations() | 512);
                    dr.getConstantState();
                }
                try {
                    info.outInputStream.close();
                } catch (Exception e) {
                }
                return dr;
            } catch (OutOfMemoryError e2) {
                try {
                    info.outInputStream.close();
                } catch (Exception e3) {
                }
            } catch (Throwable th) {
                try {
                    info.outInputStream.close();
                } catch (Exception e4) {
                }
                throw th;
            }
        }
        this.mSkipFiles.put(Integer.valueOf(id), Boolean.valueOf(true));
        return null;
    }

    private boolean getThemeFile(ThemeFileInfoOption info) {
        String str = ".xml";
        if (!info.inResourcePath.endsWith(str)) {
            return this.mThemeResources.getThemeFile(info);
        }
        FixedSizeStringBuffer buffer = FixedSizeStringBuffer.getBuffer();
        int withoutSuffixLength = info.inResourcePath.length() - str.length();
        buffer.assign(info.inResourcePath, withoutSuffixLength);
        buffer.append(".9.png");
        info.inResourcePath = buffer.toString();
        boolean themeOverlay = this.mThemeResources.getThemeFile(info);
        if (themeOverlay) {
            buffer.setLength(withoutSuffixLength);
            buffer.append(".png");
            info.inResourcePath = buffer.toString();
        }
        FixedSizeStringBuffer.freeBuffer(buffer);
        return themeOverlay;
    }

    public void updateConfiguration(Configuration config, DisplayMetrics metrics, CompatibilityInfo compat) {
        boolean nightModeEnable = false;
        int configChanges = config != null ? getConfiguration().diff(config) : 0;
        super.updateConfiguration(config, metrics, compat);
        if (sMiuiThemeEnabled && this.mThemeResources != null) {
            if ((configChanges & 4096) != 0) {
                IconCustomizer.clearCache();
            }
            if ((configChanges & 512) != 0) {
                if ((config.uiMode & 32) != 0) {
                    nightModeEnable = true;
                }
                this.mThemeResources.setNightModeEnable(nightModeEnable);
                ThemeResources.getSystem().setNightModeEnable(nightModeEnable);
            }
            long updatedTime = this.mUpdatedTime;
            if (MiuiConfiguration.needNewResources(configChanges)) {
                synchronized (sUpdatedTimeSystem) {
                    updatedTime = ThemeResources.getSystem().checkUpdate();
                    if (sUpdatedTimeSystem.longValue() < updatedTime) {
                        sUpdatedTimeSystem = Long.valueOf(updatedTime);
                        delayGCAtlasPreloadedBitmaps();
                        Resources.clearPreloadedCache();
                    }
                }
                updatedTime = Math.max(updatedTime, this.mThemeResources.checkUpdate());
            }
            if (!(this.mUpdatedTime >= updatedTime && (configChanges & 128) == 0 && (configChanges & 512) == 0)) {
                reset();
            }
        }
    }

    private static void delayGCAtlasPreloadedBitmaps() {
        final LongSparseArray<ConstantState> preload = Resources.getSystem().getPreloadedDrawables().clone();
        if (preload.size() > 0) {
            ThemeNativeUtils.terminateAtlas();
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
                    } catch (Exception e) {
                    }
                    preload.clear();
                }
            }.start();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Boolean isPreloadOverlayed(int id) {
        if (!sMiuiThemeEnabled) {
            return null;
        }
        Boolean isOverlayed = (Boolean) this.mIsPreloadOverlayed.get(Integer.valueOf(id));
        if (isOverlayed != null) {
            return isOverlayed;
        }
        isOverlayed = Boolean.valueOf(false);
        Set<PreloadDrawableSource> sources = (Set) sPreloadDrawableSources.get(id);
        if (sources != null) {
            for (PreloadDrawableSource source : sources) {
                ThemeFileInfoOption info = new ThemeFileInfoOption(source.mCookie, source.mResourcePath, false);
                getThemeFile(info);
                if (!TextUtils.equals(source.mThemePath, info.outFilterPath)) {
                    isOverlayed = Boolean.valueOf(true);
                    break;
                }
            }
        }
        this.mIsPreloadOverlayed.put(Integer.valueOf(id), isOverlayed);
        return isOverlayed;
    }

    /* Access modifiers changed, original: 0000 */
    public Drawable loadDrawable(Resources wrapper, TypedValue value, int id, int density, Theme theme) throws NotFoundException {
        if (!sMiuiThemeEnabled) {
            return super.loadDrawable(wrapper, value, id, density, theme);
        }
        if (isPreloading()) {
            this.mPreloadingIds.push(Integer.valueOf(id));
        }
        Drawable d = super.loadDrawable(wrapper, value, id, density, theme);
        if (isPreloading()) {
            if (value.type < 28 || value.type > 31) {
                PreloadDrawableSource source = new PreloadDrawableSource(value.assetCookie, value.string.toString(), null);
                ThemeFileInfoOption themeFileInfoOption = this.mPreloadingInfo;
                if (themeFileInfoOption != null) {
                    source.mThemePath = themeFileInfoOption.outFilterPath;
                    this.mPreloadingInfo = null;
                }
                Iterator it = this.mPreloadingIds.iterator();
                while (it.hasNext()) {
                    Integer loadingId = (Integer) it.next();
                    Set<PreloadDrawableSource> sources = (Set) sPreloadDrawableSources.get(loadingId.intValue());
                    if (sources == null) {
                        sources = new HashSet();
                        sPreloadDrawableSources.put(loadingId.intValue(), sources);
                    }
                    sources.add(source);
                }
            }
            this.mPreloadingIds.pop();
        }
        return d;
    }

    public void init(MiuiResources wrapper, String packageName) {
        if (this.mPackageName == null) {
            this.mPackageName = packageName;
            if (packageName == null) {
                this.mPackageName = "android";
                this.mThemeResources = ThemeResources.getSystem(wrapper);
            } else {
                this.mThemeResources = ThemeResourcesPackage.getThemeResources(wrapper, packageName);
                ThemeResources.getSystem();
            }
            reset();
        }
    }

    /* Access modifiers changed, original: protected */
    public void reset() {
        this.mUpdatedTime = System.currentTimeMillis();
        this.mSkipFiles = new ConcurrentHashMap();
        this.mIsPreloadOverlayed = new ConcurrentHashMap();
    }

    /* Access modifiers changed, original: 0000 */
    public TypedArray loadOverlayTypedArray(TypedArray array) {
        if (!sMiuiThemeEnabled) {
            return null;
        }
        loadValues();
        ThemeValues themeValues = this.mThemeValues;
        if (themeValues == null || themeValues.mIntegers.size() == 0) {
            return array;
        }
        int[] data = array.mData;
        for (int index = 0; index < data.length; index += 7) {
            int type = data[index + 0];
            int id = data[index + 3];
            if ((type >= 16 && type <= 31) || type == 5) {
                Integer themeInteger = getThemeInt(id);
                if (themeInteger != null) {
                    data[index + 1] = themeInteger.intValue();
                }
            }
        }
        return array;
    }

    private void loadValues() {
        if (this.mValueLoadedTime < this.mUpdatedTime) {
            ThemeValues tmp = new ThemeValues();
            this.mThemeResources.mergeThemeValues(this.mPackageName, tmp);
            this.mThemeValues = tmp;
            this.mValueLoadedTime = System.currentTimeMillis();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Integer getThemeInt(int id) {
        loadValues();
        return (Integer) this.mThemeValues.mIntegers.get(Integer.valueOf(id));
    }

    /* Access modifiers changed, original: 0000 */
    public CharSequence getThemeString(int id) {
        loadValues();
        return (CharSequence) this.mThemeValues.mStrings.get(Integer.valueOf(id));
    }

    /* Access modifiers changed, original: 0000 */
    public int[] getThemeIntArray(int id) {
        loadValues();
        return (int[]) this.mThemeValues.mIntegerArrays.get(Integer.valueOf(id));
    }

    /* Access modifiers changed, original: 0000 */
    public String[] getThemeStringArray(int id) {
        loadValues();
        return (String[]) this.mThemeValues.mStringArrays.get(Integer.valueOf(id));
    }
}
