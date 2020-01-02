package android.view.textclassifier;

import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TextClassifierImpl$ftq-sQqJYwUdrdbbr9jz3p4AWos implements Function {
    private final /* synthetic */ TextClassifierImpl f$0;

    public /* synthetic */ -$$Lambda$TextClassifierImpl$ftq-sQqJYwUdrdbbr9jz3p4AWos(TextClassifierImpl textClassifierImpl) {
        this.f$0 = textClassifierImpl;
    }

    public final Object apply(Object obj) {
        return this.f$0.detectLanguageTagsFromText((CharSequence) obj);
    }
}
