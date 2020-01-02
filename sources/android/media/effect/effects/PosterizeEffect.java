package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.PosterizeFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class PosterizeEffect extends SingleFilterEffect {
    public PosterizeEffect(EffectContext context, String name) {
        EffectContext effectContext = context;
        String str = name;
        super(effectContext, str, PosterizeFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
