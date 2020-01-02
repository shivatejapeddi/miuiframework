package miui.app;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

public class ToggleDrawable extends LayerDrawable {
    public ToggleDrawable(Drawable bgDrawable, Drawable toggle) {
        super(getArray(bgDrawable, toggle));
        setBounds(0, 0, bgDrawable.getIntrinsicWidth(), bgDrawable.getIntrinsicHeight());
    }

    private static Drawable[] getArray(Drawable bgDrawable, Drawable toggle) {
        return new Drawable[]{bgDrawable, toggle};
    }
}
