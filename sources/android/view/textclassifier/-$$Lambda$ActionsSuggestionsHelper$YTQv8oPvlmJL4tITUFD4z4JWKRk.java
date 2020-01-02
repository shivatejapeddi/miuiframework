package android.view.textclassifier;

import android.view.textclassifier.ConversationActions.Message;
import java.util.function.ToIntFunction;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ActionsSuggestionsHelper$YTQv8oPvlmJL4tITUFD4z4JWKRk implements ToIntFunction {
    public static final /* synthetic */ -$$Lambda$ActionsSuggestionsHelper$YTQv8oPvlmJL4tITUFD4z4JWKRk INSTANCE = new -$$Lambda$ActionsSuggestionsHelper$YTQv8oPvlmJL4tITUFD4z4JWKRk();

    private /* synthetic */ -$$Lambda$ActionsSuggestionsHelper$YTQv8oPvlmJL4tITUFD4z4JWKRk() {
    }

    public final int applyAsInt(Object obj) {
        return ActionsSuggestionsHelper.hashMessage((Message) obj);
    }
}
