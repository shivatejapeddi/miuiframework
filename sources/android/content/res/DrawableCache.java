package android.content.res;

import android.annotation.UnsupportedAppUsage;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;

class DrawableCache extends ThemedResourceCache<ConstantState> {
    DrawableCache() {
    }

    @UnsupportedAppUsage
    public Drawable getInstance(long key, Resources resources, Theme theme) {
        ConstantState entry = (ConstantState) get(key, theme);
        if (entry != null) {
            return entry.newDrawable(resources, theme);
        }
        return null;
    }

    public boolean shouldInvalidateEntry(ConstantState entry, int configChanges) {
        return Configuration.needNewResources(configChanges, entry.getChangingConfigurations());
    }
}
