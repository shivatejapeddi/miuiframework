package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.VignetteFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class VignetteEffect extends SingleFilterEffect {
    public VignetteEffect(EffectContext context, String name) {
        EffectContext effectContext = context;
        String str = name;
        super(effectContext, str, VignetteFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
