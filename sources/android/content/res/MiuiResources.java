package android.content.res;

import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import java.io.InputStream;

public final class MiuiResources extends Resources {
    private MiuiResourcesImpl mMiuiImpl;
    private String mPackage;

    public static class ThemeFileInfoOption {
        public int inCookie;
        public int inDensity;
        public boolean inRequestStream;
        public String inResourcePath;
        public int outDensity;
        public String outFilterPath;
        public InputStream outInputStream;
        public long outSize;

        public ThemeFileInfoOption(boolean requestStream) {
            this.inRequestStream = requestStream;
        }

        public ThemeFileInfoOption(int cookie, String resourcePath, boolean requestStream) {
            this.inCookie = cookie;
            this.inResourcePath = resourcePath;
            this.inRequestStream = requestStream;
        }

        public ThemeFileInfoOption(TypedValue value, boolean requestStream) {
            this.inCookie = value.assetCookie;
            this.inDensity = value.density;
            this.inResourcePath = value.string.toString();
            this.inRequestStream = requestStream;
        }
    }

    MiuiResources() {
        updateMiuiImpl();
    }

    public MiuiResources(ClassLoader classLoader) {
        super(classLoader);
        updateMiuiImpl();
    }

    public MiuiResources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
        updateMiuiImpl();
    }

    public void setImpl(ResourcesImpl impl) {
        super.setImpl(impl);
        updateMiuiImpl();
    }

    public CharSequence getText(int id) throws NotFoundException {
        CharSequence cs = this.mMiuiImpl;
        if (cs != null) {
            cs = cs.getText(id);
            if (cs != null) {
                return cs;
            }
        }
        return super.getText(id);
    }

    public CharSequence getText(int id, CharSequence def) {
        CharSequence cs = this.mMiuiImpl;
        if (cs != null) {
            cs = cs.getText(id, def);
            if (cs != null) {
                return cs;
            }
        }
        return super.getText(id, def);
    }

    /* Access modifiers changed, original: 0000 */
    public void loadOverlayValue(TypedValue outValue, int id) {
        MiuiResourcesImpl miuiResourcesImpl = this.mMiuiImpl;
        if (miuiResourcesImpl != null) {
            miuiResourcesImpl.loadOverlayValue(outValue, id);
        }
    }

    public int[] getIntArray(int id) throws NotFoundException {
        int[] array = this.mMiuiImpl;
        if (array != null) {
            array = array.getIntArray(id);
            if (array != null) {
                return array;
            }
        }
        return super.getIntArray(id);
    }

    public CharSequence[] getTextArray(int id) throws NotFoundException {
        CharSequence[] array = this.mMiuiImpl;
        if (array != null) {
            array = array.getTextArray(id);
            if (array != null) {
                return array;
            }
        }
        return super.getTextArray(id);
    }

    public String[] getStringArray(int id) throws NotFoundException {
        String[] array = this.mMiuiImpl;
        if (array != null) {
            array = array.getStringArray(id);
            if (array != null) {
                return array;
            }
        }
        return super.getStringArray(id);
    }

    /* Access modifiers changed, original: 0000 */
    public Drawable loadOverlayDrawable(TypedValue value, int id) {
        Drawable d = this.mMiuiImpl;
        if (d != null) {
            d = d.loadOverlayDrawable(this, value, id);
            if (d != null) {
                return d;
            }
        }
        return super.loadOverlayDrawable(value, id);
    }

    public static boolean isPreloadedCacheEmpty() {
        return Resources.getSystem().getPreloadedDrawables().size() == 0;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isPreloadOverlayed(int id) {
        Boolean isPreload = this.mMiuiImpl;
        if (isPreload != null) {
            isPreload = isPreload.isPreloadOverlayed(id);
            if (isPreload != null) {
                return isPreload.booleanValue();
            }
        }
        return super.isPreloadOverlayed(id);
    }

    public void init(String packageName) {
        MiuiResourcesImpl miuiResourcesImpl = this.mMiuiImpl;
        if (miuiResourcesImpl != null) {
            this.mPackage = packageName;
            miuiResourcesImpl.init(this, packageName);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public TypedArray loadOverlayTypedArray(TypedArray array) {
        TypedArray typeArray = this.mMiuiImpl;
        if (typeArray != null) {
            typeArray = typeArray.loadOverlayTypedArray(array);
            if (typeArray != null) {
                return typeArray;
            }
        }
        return super.loadOverlayTypedArray(array);
    }

    /* Access modifiers changed, original: 0000 */
    public CharSequence getThemeString(int id) {
        MiuiResourcesImpl miuiResourcesImpl = this.mMiuiImpl;
        if (miuiResourcesImpl != null) {
            return miuiResourcesImpl.getThemeString(id);
        }
        return null;
    }

    private void updateMiuiImpl() {
        ResourcesImpl impl = getImpl();
        if (impl == null || !(impl instanceof MiuiResourcesImpl)) {
            this.mMiuiImpl = null;
            return;
        }
        this.mMiuiImpl = (MiuiResourcesImpl) impl;
        String str = this.mPackage;
        if (str != null) {
            this.mMiuiImpl.init(this, str);
        }
    }
}
