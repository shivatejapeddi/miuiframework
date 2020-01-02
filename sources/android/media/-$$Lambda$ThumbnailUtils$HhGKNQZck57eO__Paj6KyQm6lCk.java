package android.media;

import java.io.File;
import java.util.Comparator;
import java.util.function.ToIntFunction;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ThumbnailUtils$HhGKNQZck57eO__Paj6KyQm6lCk implements Comparator {
    private final /* synthetic */ ToIntFunction f$0;

    public /* synthetic */ -$$Lambda$ThumbnailUtils$HhGKNQZck57eO__Paj6KyQm6lCk(ToIntFunction toIntFunction) {
        this.f$0 = toIntFunction;
    }

    public final int compare(Object obj, Object obj2) {
        return (this.f$0.applyAsInt((File) obj) - this.f$0.applyAsInt((File) obj2));
    }
}
