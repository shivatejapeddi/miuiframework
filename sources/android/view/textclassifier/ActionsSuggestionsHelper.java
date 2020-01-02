package android.view.textclassifier;

import android.app.Person;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.util.ArrayMap;
import android.util.Pair;
import android.view.textclassifier.ConversationActions.Message;
import android.view.textclassifier.SelectionSessionLogger.SignatureParser;
import android.view.textclassifier.intent.LabeledIntent;
import android.view.textclassifier.intent.LabeledIntent.Result;
import android.view.textclassifier.intent.LabeledIntent.TitleChooser;
import android.view.textclassifier.intent.TemplateIntentFactory;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.google.android.textclassifier.ActionsSuggestionsModel.ActionSuggestion;
import com.google.android.textclassifier.ActionsSuggestionsModel.ConversationMessage;
import com.google.android.textclassifier.RemoteActionTemplate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

@VisibleForTesting(visibility = Visibility.PACKAGE)
public final class ActionsSuggestionsHelper {
    private static final int FIRST_NON_LOCAL_USER = 1;
    private static final String TAG = "ActionsSuggestions";
    private static final int USER_LOCAL = 0;

    private static final class PersonEncoder {
        private final Map<Person, Integer> mMapping;
        private int mNextUserId;

        private PersonEncoder() {
            this.mMapping = new ArrayMap();
            this.mNextUserId = 1;
        }

        private int encode(Person person) {
            if (Message.PERSON_USER_SELF.equals(person)) {
                return 0;
            }
            Integer result = (Integer) this.mMapping.get(person);
            if (result == null) {
                this.mMapping.put(person, Integer.valueOf(this.mNextUserId));
                result = Integer.valueOf(this.mNextUserId);
                this.mNextUserId++;
            }
            return result.intValue();
        }
    }

    private ActionsSuggestionsHelper() {
    }

    public static ConversationMessage[] toNativeMessages(List<Message> messages, Function<CharSequence, String> languageDetector) {
        List<Message> messagesWithText = (List) messages.stream().filter(-$$Lambda$ActionsSuggestionsHelper$6oTtcn9bDE-u-8FbiyGdntqoQG0.INSTANCE).collect(Collectors.toCollection(-$$Lambda$OGSS2qx6njxlnp0dnKb4lA3jnw8.INSTANCE));
        if (messagesWithText.isEmpty()) {
            return new ConversationMessage[0];
        }
        Deque<ConversationMessage> nativeMessages = new ArrayDeque();
        PersonEncoder personEncoder = new PersonEncoder();
        for (int i = messagesWithText.size() - 1; i >= 0; i--) {
            long referenceTime;
            String timeZone;
            Message message = (Message) messagesWithText.get(i);
            if (message.getReferenceTime() == null) {
                referenceTime = 0;
            } else {
                referenceTime = message.getReferenceTime().toInstant().toEpochMilli();
            }
            if (message.getReferenceTime() == null) {
                timeZone = null;
            } else {
                timeZone = message.getReferenceTime().getZone().getId();
            }
            nativeMessages.push(new ConversationMessage(personEncoder.encode(message.getAuthor()), message.getText().toString(), referenceTime, timeZone, (String) languageDetector.apply(message.getText())));
        }
        return (ConversationMessage[]) nativeMessages.toArray(new ConversationMessage[nativeMessages.size()]);
    }

    public static String createResultId(Context context, List<Message> messages, int modelVersion, List<Locale> modelLocales) {
        StringJoiner localesJoiner = new StringJoiner(",");
        for (Locale locale : modelLocales) {
            localesJoiner.add(locale.toLanguageTag());
        }
        return SignatureParser.createSignature(TextClassifier.DEFAULT_LOG_TAG, String.format(Locale.US, "%s_v%d", new Object[]{localesJoiner.toString(), Integer.valueOf(modelVersion)}), Objects.hash(new Object[]{messages.stream().mapToInt(-$$Lambda$ActionsSuggestionsHelper$YTQv8oPvlmJL4tITUFD4z4JWKRk.INSTANCE), context.getPackageName(), Long.valueOf(System.currentTimeMillis())}));
    }

    public static Result createLabeledIntentResult(Context context, TemplateIntentFactory templateIntentFactory, ActionSuggestion nativeSuggestion) {
        RemoteActionTemplate[] remoteActionTemplates = nativeSuggestion.getRemoteActionTemplates();
        if (remoteActionTemplates == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("createRemoteAction: Missing template for type ");
            stringBuilder.append(nativeSuggestion.getActionType());
            Log.w(TAG, stringBuilder.toString());
            return null;
        }
        List<LabeledIntent> labeledIntents = templateIntentFactory.create(remoteActionTemplates);
        if (labeledIntents.isEmpty()) {
            return null;
        }
        return ((LabeledIntent) labeledIntents.get(0)).resolve(context, createTitleChooser(nativeSuggestion.getActionType()), null);
    }

    public static TitleChooser createTitleChooser(String actionType) {
        if (ConversationAction.TYPE_OPEN_URL.equals(actionType)) {
            return -$$Lambda$ActionsSuggestionsHelper$sY0w9od2zcl4YFel0lG4VB3vf7I.INSTANCE;
        }
        return null;
    }

    static /* synthetic */ CharSequence lambda$createTitleChooser$1(LabeledIntent labeledIntent, ResolveInfo resolveInfo) {
        if (resolveInfo.handleAllWebDataURI) {
            return labeledIntent.titleWithEntity;
        }
        if ("android".equals(resolveInfo.activityInfo.packageName)) {
            return labeledIntent.titleWithEntity;
        }
        return labeledIntent.titleWithoutEntity;
    }

    public static List<ConversationAction> removeActionsWithDuplicates(List<ConversationAction> conversationActions) {
        Map<Pair<String, String>, Integer> counter = new ArrayMap();
        for (ConversationAction conversationAction : conversationActions) {
            Pair<String, String> representation = getRepresentation(conversationAction);
            if (representation != null) {
                counter.put(representation, Integer.valueOf(((Integer) counter.getOrDefault(representation, Integer.valueOf(0))).intValue() + 1));
            }
        }
        List<ConversationAction> result = new ArrayList();
        for (ConversationAction conversationAction2 : conversationActions) {
            Pair<String, String> representation2 = getRepresentation(conversationAction2);
            if (representation2 == null || ((Integer) counter.getOrDefault(representation2, Integer.valueOf(0))).intValue() == 1) {
                result.add(conversationAction2);
            }
        }
        return result;
    }

    private static Pair<String, String> getRepresentation(ConversationAction conversationAction) {
        String packageName = null;
        if (conversationAction.getAction() == null) {
            return null;
        }
        ComponentName componentName = ExtrasUtils.getActionIntent(conversationAction.getExtras()).getComponent();
        if (componentName != null) {
            packageName = componentName.getPackageName();
        }
        return new Pair(conversationAction.getAction().getTitle().toString(), packageName);
    }

    private static int hashMessage(Message message) {
        return Objects.hash(new Object[]{message.getAuthor(), message.getText(), message.getReferenceTime()});
    }
}
