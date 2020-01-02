package android.view.textclassifier;

import android.text.TextUtils;
import android.view.textclassifier.ConversationActions.Message;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ActionsSuggestionsHelper$6oTtcn9bDE-u-8FbiyGdntqoQG0 implements Predicate {
    public static final /* synthetic */ -$$Lambda$ActionsSuggestionsHelper$6oTtcn9bDE-u-8FbiyGdntqoQG0 INSTANCE = new -$$Lambda$ActionsSuggestionsHelper$6oTtcn9bDE-u-8FbiyGdntqoQG0();

    private /* synthetic */ -$$Lambda$ActionsSuggestionsHelper$6oTtcn9bDE-u-8FbiyGdntqoQG0() {
    }

    public final boolean test(Object obj) {
        return (TextUtils.isEmpty(((Message) obj).getText()) ^ 1);
    }
}
