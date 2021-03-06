package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.BitmapOverlayFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class BitmapOverlayEffect extends SingleFilterEffect {
    public BitmapOverlayEffect(EffectContext context, String name) {
        EffectContext effectContext = context;
        String str = name;
        super(effectContext, str, BitmapOverlayFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
