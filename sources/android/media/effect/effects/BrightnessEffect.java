package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.BrightnessFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class BrightnessEffect extends SingleFilterEffect {
    public BrightnessEffect(EffectContext context, String name) {
        EffectContext effectContext = context;
        String str = name;
        super(effectContext, str, BrightnessFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
