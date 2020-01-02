package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.DocumentaryFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;

public class DocumentaryEffect extends SingleFilterEffect {
    public DocumentaryEffect(EffectContext context, String name) {
        EffectContext effectContext = context;
        String str = name;
        super(effectContext, str, DocumentaryFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
