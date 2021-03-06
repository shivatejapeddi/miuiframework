package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.SepiaFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class SepiaEffect extends SingleFilterEffect {
    public SepiaEffect(EffectContext context, String name) {
        EffectContext effectContext = context;
        String str = name;
        super(effectContext, str, SepiaFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
