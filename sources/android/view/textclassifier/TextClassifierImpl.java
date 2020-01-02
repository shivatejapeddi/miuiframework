package android.view.textclassifier;

import android.app.RemoteAction;
import android.content.Context;
import android.content.Intent;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.ParcelFileDescriptor;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Pair;
import android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams;
import android.view.textclassifier.ModelFileManager.ModelFile;
import android.view.textclassifier.ModelFileManager.ModelFileSupplierImpl;
import android.view.textclassifier.TextClassifier.Utils;
import android.view.textclassifier.TextSelection.Builder;
import android.view.textclassifier.TextSelection.Request;
import android.view.textclassifier.intent.ClassificationIntentFactory;
import android.view.textclassifier.intent.LabeledIntent;
import android.view.textclassifier.intent.LabeledIntent.Result;
import android.view.textclassifier.intent.LabeledIntent.TitleChooser;
import android.view.textclassifier.intent.LegacyClassificationIntentFactory;
import android.view.textclassifier.intent.TemplateClassificationIntentFactory;
import android.view.textclassifier.intent.TemplateIntentFactory;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import com.google.android.textclassifier.ActionsSuggestionsModel;
import com.google.android.textclassifier.ActionsSuggestionsModel.ActionSuggestion;
import com.google.android.textclassifier.ActionsSuggestionsModel.Conversation;
import com.google.android.textclassifier.ActionsSuggestionsModel.ConversationMessage;
import com.google.android.textclassifier.AnnotatorModel;
import com.google.android.textclassifier.AnnotatorModel.AnnotatedSpan;
import com.google.android.textclassifier.AnnotatorModel.AnnotationOptions;
import com.google.android.textclassifier.AnnotatorModel.AnnotationUsecase;
import com.google.android.textclassifier.AnnotatorModel.ClassificationOptions;
import com.google.android.textclassifier.AnnotatorModel.ClassificationResult;
import com.google.android.textclassifier.AnnotatorModel.SelectionOptions;
import com.google.android.textclassifier.LangIdModel;
import com.google.android.textclassifier.LangIdModel.LanguageResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public final class TextClassifierImpl implements TextClassifier {
    private static final String ACTIONS_FACTORY_MODEL_FILENAME_REGEX = "actions_suggestions\\.(.*)\\.model";
    private static final String ANNOTATOR_FACTORY_MODEL_FILENAME_REGEX = "textclassifier\\.(.*)\\.model";
    private static final File ANNOTATOR_UPDATED_MODEL_FILE = new File("/data/misc/textclassifier/textclassifier.model");
    private static final boolean DEBUG = false;
    private static final File FACTORY_MODEL_DIR = new File("/etc/textclassifier/");
    private static final String LANG_ID_FACTORY_MODEL_FILENAME_REGEX = "lang_id.model";
    private static final String LOG_TAG = "androidtc";
    private static final File UPDATED_ACTIONS_MODEL = new File("/data/misc/textclassifier/actions_suggestions.model");
    private static final File UPDATED_LANG_ID_MODEL_FILE = new File("/data/misc/textclassifier/lang_id.model");
    @GuardedBy({"mLock"})
    private ModelFile mActionModelInUse;
    @GuardedBy({"mLock"})
    private ActionsSuggestionsModel mActionsImpl;
    private final ModelFileManager mActionsModelFileManager;
    private final Supplier<ActionsModelParams> mActionsModelParamsSupplier;
    @GuardedBy({"mLock"})
    private AnnotatorModel mAnnotatorImpl;
    private final ModelFileManager mAnnotatorModelFileManager;
    @GuardedBy({"mLock"})
    private ModelFile mAnnotatorModelInUse;
    private final ClassificationIntentFactory mClassificationIntentFactory;
    private final Context mContext;
    private final TextClassifier mFallback;
    private final GenerateLinksLogger mGenerateLinksLogger;
    @GuardedBy({"mLock"})
    private LangIdModel mLangIdImpl;
    private final ModelFileManager mLangIdModelFileManager;
    @GuardedBy({"mLock"})
    private ModelFile mLangIdModelInUse;
    private final Object mLock;
    private final SelectionSessionLogger mSessionLogger;
    private final TextClassificationConstants mSettings;
    private final TemplateIntentFactory mTemplateIntentFactory;
    private final TextClassifierEventTronLogger mTextClassifierEventTronLogger;

    public TextClassifierImpl(Context context, TextClassificationConstants settings, TextClassifier fallback) {
        ClassificationIntentFactory templateClassificationIntentFactory;
        this.mLock = new Object();
        this.mSessionLogger = new SelectionSessionLogger();
        this.mTextClassifierEventTronLogger = new TextClassifierEventTronLogger();
        this.mContext = (Context) Preconditions.checkNotNull(context);
        this.mFallback = (TextClassifier) Preconditions.checkNotNull(fallback);
        this.mSettings = (TextClassificationConstants) Preconditions.checkNotNull(settings);
        this.mGenerateLinksLogger = new GenerateLinksLogger(this.mSettings.getGenerateLinksLogSampleRate());
        this.mAnnotatorModelFileManager = new ModelFileManager(new ModelFileSupplierImpl(FACTORY_MODEL_DIR, ANNOTATOR_FACTORY_MODEL_FILENAME_REGEX, ANNOTATOR_UPDATED_MODEL_FILE, -$$Lambda$jJq8RXuVdjYF3lPq-77PEw1NJLM.INSTANCE, -$$Lambda$NxwbyZSxofZ4Z5SQhfXmtLQ1nxk.INSTANCE));
        this.mLangIdModelFileManager = new ModelFileManager(new ModelFileSupplierImpl(FACTORY_MODEL_DIR, LANG_ID_FACTORY_MODEL_FILENAME_REGEX, UPDATED_LANG_ID_MODEL_FILE, -$$Lambda$0biFK4yZBmWN1EO2wtnXskzuEcE.INSTANCE, -$$Lambda$TextClassifierImpl$RRbXefHgcUymI9-P95ArUyMvfbw.INSTANCE));
        this.mActionsModelFileManager = new ModelFileManager(new ModelFileSupplierImpl(FACTORY_MODEL_DIR, ACTIONS_FACTORY_MODEL_FILENAME_REGEX, UPDATED_ACTIONS_MODEL, -$$Lambda$9N8WImc0VBjy2oxI_Gk5_Pbye_A.INSTANCE, -$$Lambda$XeE_KI7QgMKzF9vYRSoFWAolyuA.INSTANCE));
        this.mTemplateIntentFactory = new TemplateIntentFactory();
        if (this.mSettings.isTemplateIntentFactoryEnabled()) {
            templateClassificationIntentFactory = new TemplateClassificationIntentFactory(this.mTemplateIntentFactory, new LegacyClassificationIntentFactory());
        } else {
            templateClassificationIntentFactory = new LegacyClassificationIntentFactory();
        }
        this.mClassificationIntentFactory = templateClassificationIntentFactory;
        this.mActionsModelParamsSupplier = new ActionsModelParamsSupplier(this.mContext, new -$$Lambda$TextClassifierImpl$iSt_Guet-O6Vtdk0MA4z-Z4lzaM(this));
    }

    public /* synthetic */ void lambda$new$1$TextClassifierImpl() {
        synchronized (this.mLock) {
            this.mActionsImpl = null;
            this.mActionModelInUse = null;
        }
    }

    public TextClassifierImpl(Context context, TextClassificationConstants settings) {
        this(context, settings, TextClassifier.NO_OP);
    }

    public TextSelection suggestSelection(Request request) {
        String str = "androidtc";
        Preconditions.checkNotNull(request);
        Utils.checkMainThread();
        try {
            int rangeLength = request.getEndIndex() - request.getStartIndex();
            String string = request.getText().toString();
            if (string.length() <= 0) {
            } else if (rangeLength <= this.mSettings.getSuggestSelectionMaxRangeLength()) {
                int start;
                int end;
                int start2;
                String localesString = concatenateLocales(request.getDefaultLocales());
                String detectLanguageTags = detectLanguageTagsFromText(request.getText());
                ZonedDateTime refTime = ZonedDateTime.now();
                AnnotatorModel annotatorImpl = getAnnotatorImpl(request.getDefaultLocales());
                if (!this.mSettings.isModelDarkLaunchEnabled() || request.isDarkLaunchAllowed()) {
                    int[] startEnd = annotatorImpl.suggestSelection(string, request.getStartIndex(), request.getEndIndex(), new SelectionOptions(localesString, detectLanguageTags));
                    start = startEnd[0];
                    end = startEnd[1];
                    start2 = start;
                } else {
                    start2 = request.getStartIndex();
                    end = request.getEndIndex();
                }
                int i;
                if (start2 >= end || start2 < 0) {
                    i = end;
                } else if (end > string.length()) {
                    i = end;
                } else if (start2 > request.getStartIndex() || end < request.getEndIndex()) {
                    i = end;
                } else {
                    rangeLength = new Builder(start2, end);
                    i = end;
                    end = annotatorImpl.classifyText(string, start2, i, new ClassificationOptions(refTime.toInstant().toEpochMilli(), refTime.getZone().getId(), localesString, detectLanguageTags), null, null);
                    start = end.length;
                    for (int i2 = 0; i2 < start; i2++) {
                        rangeLength.setEntityType(end[i2].getCollection(), end[i2].getScore());
                    }
                    return rangeLength.setId(createId(string, request.getStartIndex(), request.getEndIndex())).build();
                }
                Log.d(str, "Got bad indices for input text. Ignoring result.");
            }
        } catch (Throwable t) {
            Log.e(str, "Error suggesting selection for text. No changes to selection suggested.", t);
        }
        return this.mFallback.suggestSelection(request);
    }

    public TextClassification classifyText(TextClassification.Request request) {
        Preconditions.checkNotNull(request);
        Utils.checkMainThread();
        try {
            int rangeLength = request.getEndIndex() - request.getStartIndex();
            String string = request.getText().toString();
            if (string.length() > 0 && rangeLength <= this.mSettings.getClassifyTextMaxRangeLength()) {
                String localesString = concatenateLocales(request.getDefaultLocales());
                String detectLanguageTags = detectLanguageTagsFromText(request.getText());
                ZonedDateTime refTime = request.getReferenceTime() != null ? request.getReferenceTime() : ZonedDateTime.now();
                ClassificationResult[] results = getAnnotatorImpl(request.getDefaultLocales()).classifyText(string, request.getStartIndex(), request.getEndIndex(), new ClassificationOptions(refTime.toInstant().toEpochMilli(), refTime.getZone().getId(), localesString, detectLanguageTags), this.mContext, getResourceLocalesString());
                if (results.length > 0) {
                    return createClassificationResult(results, string, request.getStartIndex(), request.getEndIndex(), refTime.toInstant());
                }
            }
        } catch (Throwable t) {
            Log.e("androidtc", "Error getting text classification info.", t);
        }
        return this.mFallback.classifyText(request);
    }

    public TextLinks generateLinks(TextLinks.Request request) {
        Preconditions.checkNotNull(request);
        Utils.checkTextLength(request.getText(), getMaxGenerateLinksTextLength());
        Utils.checkMainThread();
        if (!this.mSettings.isSmartLinkifyEnabled() && request.isLegacyFallback()) {
            return Utils.generateLegacyLinks(request);
        }
        String textString = request.getText().toString();
        TextLinks.Builder builder = new TextLinks.Builder(textString);
        try {
            Collection<String> entitiesToIdentify;
            String callingPackageName;
            long startTimeMs = System.currentTimeMillis();
            ZonedDateTime refTime = ZonedDateTime.now();
            if (request.getEntityConfig() != null) {
                entitiesToIdentify = request.getEntityConfig().resolveEntityListModifications(getEntitiesForHints(request.getEntityConfig().getHints()));
            } else {
                entitiesToIdentify = this.mSettings.getEntityListDefault();
            }
            String localesString = concatenateLocales(request.getDefaultLocales());
            String detectLanguageTags = detectLanguageTagsFromText(request.getText());
            AnnotatorModel annotatorImpl = getAnnotatorImpl(request.getDefaultLocales());
            boolean isSerializedEntityDataEnabled = ExtrasUtils.isSerializedEntityDataEnabled(request);
            AnnotatedSpan[] annotations = r7;
            long startTimeMs2 = startTimeMs;
            AnnotatorModel annotatorImpl2 = annotatorImpl;
            AnnotationOptions annotationOptions = new AnnotationOptions(refTime.toInstant().toEpochMilli(), refTime.getZone().getId(), localesString, detectLanguageTags, entitiesToIdentify, AnnotationUsecase.SMART.getValue(), isSerializedEntityDataEnabled);
            annotations = annotatorImpl2.annotate(textString, annotations);
            int length = annotations.length;
            int i = 0;
            int i2 = 0;
            while (i2 < length) {
                AnnotatedSpan span = annotations[i2];
                ClassificationResult[] results = span.getClassification();
                if (results.length != 0) {
                    if (entitiesToIdentify.contains(results[i].getCollection())) {
                        Map entityScores = new ArrayMap();
                        for (int i3 = i; i3 < results.length; i3++) {
                            entityScores.put(results[i3].getCollection(), Float.valueOf(results[i3].getScore()));
                        }
                        Bundle extras = new Bundle();
                        if (isSerializedEntityDataEnabled) {
                            ExtrasUtils.putEntities(extras, results);
                        }
                        builder.addLink(span.getStartIndex(), span.getEndIndex(), entityScores, extras);
                    }
                }
                i2++;
                i = 0;
            }
            TextLinks links = builder.build();
            long endTimeMs = System.currentTimeMillis();
            if (request.getCallingPackageName() == null) {
                callingPackageName = this.mContext.getPackageName();
            } else {
                callingPackageName = request.getCallingPackageName();
            }
            this.mGenerateLinksLogger.logGenerateLinks(request.getText(), links, callingPackageName, endTimeMs - startTimeMs2);
            return links;
        } catch (Throwable t) {
            Log.e("androidtc", "Error getting links info.", t);
            return this.mFallback.generateLinks(request);
        }
    }

    public int getMaxGenerateLinksTextLength() {
        return this.mSettings.getGenerateLinksMaxTextLength();
    }

    private Collection<String> getEntitiesForHints(Collection<String> hints) {
        boolean editable = hints.contains(TextClassifier.HINT_TEXT_IS_EDITABLE);
        if (editable == hints.contains(TextClassifier.HINT_TEXT_IS_NOT_EDITABLE)) {
            return this.mSettings.getEntityListDefault();
        }
        if (editable) {
            return this.mSettings.getEntityListEditable();
        }
        return this.mSettings.getEntityListNotEditable();
    }

    public void onSelectionEvent(SelectionEvent event) {
        this.mSessionLogger.writeEvent(event);
    }

    public void onTextClassifierEvent(TextClassifierEvent event) {
        try {
            SelectionEvent selEvent = event.toSelectionEvent();
            if (selEvent != null) {
                this.mSessionLogger.writeEvent(selEvent);
            } else {
                this.mTextClassifierEventTronLogger.writeEvent(event);
            }
        } catch (Exception e) {
            Log.e("androidtc", "Error writing event", e);
        }
    }

    public TextLanguage detectLanguage(TextLanguage.Request request) {
        Preconditions.checkNotNull(request);
        Utils.checkMainThread();
        try {
            TextLanguage.Builder builder = new TextLanguage.Builder();
            LanguageResult[] langResults = getLangIdImpl().detectLanguages(request.getText().toString());
            for (int i = 0; i < langResults.length; i++) {
                builder.putLocale(ULocale.forLanguageTag(langResults[i].getLanguage()), langResults[i].getScore());
            }
            return builder.build();
        } catch (Throwable t) {
            Log.e("androidtc", "Error detecting text language.", t);
            return this.mFallback.detectLanguage(request);
        }
    }

    public ConversationActions suggestConversationActions(ConversationActions.Request request) {
        Preconditions.checkNotNull(request);
        Utils.checkMainThread();
        try {
            ActionsSuggestionsModel actionsImpl = getActionsImpl();
            if (actionsImpl == null) {
                return this.mFallback.suggestConversationActions(request);
            }
            ConversationMessage[] nativeMessages = ActionsSuggestionsHelper.toNativeMessages(request.getConversation(), new -$$Lambda$TextClassifierImpl$ftq-sQqJYwUdrdbbr9jz3p4AWos(this));
            if (nativeMessages.length == 0) {
                return this.mFallback.suggestConversationActions(request);
            }
            return createConversationActionResult(request, actionsImpl.suggestActionsWithIntents(new Conversation(nativeMessages), null, this.mContext, getResourceLocalesString(), getAnnotatorImpl(LocaleList.getDefault())));
        } catch (Throwable t) {
            Log.e("androidtc", "Error suggesting conversation actions.", t);
            return this.mFallback.suggestConversationActions(request);
        }
    }

    private ConversationActions createConversationActionResult(ConversationActions.Request request, ActionSuggestion[] nativeSuggestions) {
        Collection<String> expectedTypes = resolveActionTypesFromRequest(request);
        List<ConversationAction> conversationActions = new ArrayList();
        for (ActionSuggestion nativeSuggestion : nativeSuggestions) {
            String actionType = nativeSuggestion.getActionType();
            if (expectedTypes.contains(actionType)) {
                Result labeledIntentResult = ActionsSuggestionsHelper.createLabeledIntentResult(this.mContext, this.mTemplateIntentFactory, nativeSuggestion);
                RemoteAction remoteAction = null;
                Bundle extras = new Bundle();
                if (labeledIntentResult != null) {
                    remoteAction = labeledIntentResult.remoteAction;
                    ExtrasUtils.putActionIntent(extras, labeledIntentResult.resolvedIntent);
                }
                ExtrasUtils.putSerializedEntityData(extras, nativeSuggestion.getSerializedEntityData());
                ExtrasUtils.putEntitiesExtras(extras, TemplateIntentFactory.nameVariantsToBundle(nativeSuggestion.getEntityData()));
                conversationActions.add(new ConversationAction.Builder(actionType).setConfidenceScore(nativeSuggestion.getScore()).setTextReply(nativeSuggestion.getResponseText()).setAction(remoteAction).setExtras(extras).build());
            }
        }
        List conversationActions2 = ActionsSuggestionsHelper.removeActionsWithDuplicates(conversationActions);
        if (request.getMaxSuggestions() >= 0 && conversationActions2.size() > request.getMaxSuggestions()) {
            conversationActions2 = conversationActions2.subList(0, request.getMaxSuggestions());
        }
        return new ConversationActions(conversationActions2, ActionsSuggestionsHelper.createResultId(this.mContext, request.getConversation(), this.mActionModelInUse.getVersion(), this.mActionModelInUse.getSupportedLocales()));
    }

    private String detectLanguageTagsFromText(CharSequence text) {
        if (!this.mSettings.isDetectLanguagesFromTextEnabled()) {
            return null;
        }
        float threshold = getLangIdThreshold();
        if (threshold < 0.0f || threshold > 1.0f) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[detectLanguageTagsFromText] unexpected threshold is found: ");
            stringBuilder.append(threshold);
            Log.w("androidtc", stringBuilder.toString());
            return null;
        }
        TextLanguage textLanguage = detectLanguage(new TextLanguage.Request.Builder(text).build());
        int localeHypothesisCount = textLanguage.getLocaleHypothesisCount();
        List<String> languageTags = new ArrayList();
        for (int i = 0; i < localeHypothesisCount; i++) {
            ULocale locale = textLanguage.getLocale(i);
            if (textLanguage.getConfidenceScore(locale) < threshold) {
                break;
            }
            languageTags.add(locale.toLanguageTag());
        }
        if (languageTags.isEmpty()) {
            return null;
        }
        return String.join(",", languageTags);
    }

    private Collection<String> resolveActionTypesFromRequest(ConversationActions.Request request) {
        List<String> defaultActionTypes;
        if (request.getHints().contains("notification")) {
            defaultActionTypes = this.mSettings.getNotificationConversationActionTypes();
        } else {
            defaultActionTypes = this.mSettings.getInAppConversationActionTypes();
        }
        return request.getTypeConfig().resolveEntityListModifications(defaultActionTypes);
    }

    private AnnotatorModel getAnnotatorImpl(LocaleList localeList) throws FileNotFoundException {
        ParcelFileDescriptor pfd;
        AnnotatorModel annotatorModel;
        synchronized (this.mLock) {
            LocaleList localeList2;
            if (localeList == null) {
                try {
                    localeList2 = LocaleList.getDefault();
                } catch (Throwable th) {
                }
            } else {
                localeList2 = localeList;
            }
            localeList = localeList2;
            ModelFile bestModel = this.mAnnotatorModelFileManager.findBestModelFile(localeList);
            StringBuilder stringBuilder;
            if (bestModel != null) {
                if (this.mAnnotatorImpl == null || !Objects.equals(this.mAnnotatorModelInUse, bestModel)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Loading ");
                    stringBuilder.append(bestModel);
                    Log.d("androidtc", stringBuilder.toString());
                    pfd = ParcelFileDescriptor.open(new File(bestModel.getPath()), 268435456);
                    if (pfd != null) {
                        this.mAnnotatorImpl = new AnnotatorModel(pfd.getFd());
                        this.mAnnotatorModelInUse = bestModel;
                    }
                    maybeCloseAndLogError(pfd);
                }
                annotatorModel = this.mAnnotatorImpl;
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("No annotator model for ");
                stringBuilder.append(localeList.toLanguageTags());
                throw new FileNotFoundException(stringBuilder.toString());
            }
        }
        return annotatorModel;
    }

    private LangIdModel getLangIdImpl() throws FileNotFoundException {
        LangIdModel langIdModel;
        synchronized (this.mLock) {
            ModelFile bestModel = this.mLangIdModelFileManager.findBestModelFile(null);
            if (bestModel != null) {
                if (this.mLangIdImpl == null || !Objects.equals(this.mLangIdModelInUse, bestModel)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Loading ");
                    stringBuilder.append(bestModel);
                    Log.d("androidtc", stringBuilder.toString());
                    ParcelFileDescriptor pfd = ParcelFileDescriptor.open(new File(bestModel.getPath()), 268435456);
                    if (pfd != null) {
                        try {
                            this.mLangIdImpl = new LangIdModel(pfd.getFd());
                            this.mLangIdModelInUse = bestModel;
                        } catch (Throwable th) {
                            maybeCloseAndLogError(pfd);
                        }
                    }
                    maybeCloseAndLogError(pfd);
                }
                langIdModel = this.mLangIdImpl;
            } else {
                throw new FileNotFoundException("No LangID model is found");
            }
        }
        return langIdModel;
    }

    /* JADX WARNING: Missing block: B:17:0x0064, code skipped:
            return null;
     */
    private com.google.android.textclassifier.ActionsSuggestionsModel getActionsImpl() throws java.io.FileNotFoundException {
        /*
        r7 = this;
        r0 = r7.mLock;
        monitor-enter(r0);
        r1 = r7.mActionsModelFileManager;	 Catch:{ all -> 0x008b }
        r2 = android.os.LocaleList.getDefault();	 Catch:{ all -> 0x008b }
        r1 = r1.findBestModelFile(r2);	 Catch:{ all -> 0x008b }
        r2 = 0;
        if (r1 != 0) goto L_0x0012;
    L_0x0010:
        monitor-exit(r0);	 Catch:{ all -> 0x008b }
        return r2;
    L_0x0012:
        r3 = r7.mActionsImpl;	 Catch:{ all -> 0x008b }
        if (r3 == 0) goto L_0x001e;
    L_0x0016:
        r3 = r7.mActionModelInUse;	 Catch:{ all -> 0x008b }
        r3 = java.util.Objects.equals(r3, r1);	 Catch:{ all -> 0x008b }
        if (r3 != 0) goto L_0x0082;
    L_0x001e:
        r3 = "androidtc";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008b }
        r4.<init>();	 Catch:{ all -> 0x008b }
        r5 = "Loading ";
        r4.append(r5);	 Catch:{ all -> 0x008b }
        r4.append(r1);	 Catch:{ all -> 0x008b }
        r4 = r4.toString();	 Catch:{ all -> 0x008b }
        android.view.textclassifier.Log.d(r3, r4);	 Catch:{ all -> 0x008b }
        r3 = new java.io.File;	 Catch:{ all -> 0x008b }
        r4 = r1.getPath();	 Catch:{ all -> 0x008b }
        r3.<init>(r4);	 Catch:{ all -> 0x008b }
        r4 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r3 = android.os.ParcelFileDescriptor.open(r3, r4);	 Catch:{ all -> 0x008b }
        if (r3 != 0) goto L_0x0065;
    L_0x0045:
        r4 = "androidtc";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0086 }
        r5.<init>();	 Catch:{ all -> 0x0086 }
        r6 = "Failed to read the model file: ";
        r5.append(r6);	 Catch:{ all -> 0x0086 }
        r6 = r1.getPath();	 Catch:{ all -> 0x0086 }
        r5.append(r6);	 Catch:{ all -> 0x0086 }
        r5 = r5.toString();	 Catch:{ all -> 0x0086 }
        android.view.textclassifier.Log.d(r4, r5);	 Catch:{ all -> 0x0086 }
        maybeCloseAndLogError(r3);	 Catch:{ all -> 0x008b }
        monitor-exit(r0);	 Catch:{ all -> 0x008b }
        return r2;
    L_0x0065:
        r2 = r7.mActionsModelParamsSupplier;	 Catch:{ all -> 0x0086 }
        r2 = r2.get();	 Catch:{ all -> 0x0086 }
        r2 = (android.view.textclassifier.ActionsModelParamsSupplier.ActionsModelParams) r2;	 Catch:{ all -> 0x0086 }
        r4 = new com.google.android.textclassifier.ActionsSuggestionsModel;	 Catch:{ all -> 0x0086 }
        r5 = r3.getFd();	 Catch:{ all -> 0x0086 }
        r6 = r2.getSerializedPreconditions(r1);	 Catch:{ all -> 0x0086 }
        r4.<init>(r5, r6);	 Catch:{ all -> 0x0086 }
        r7.mActionsImpl = r4;	 Catch:{ all -> 0x0086 }
        r7.mActionModelInUse = r1;	 Catch:{ all -> 0x0086 }
        maybeCloseAndLogError(r3);	 Catch:{ all -> 0x008b }
    L_0x0082:
        r2 = r7.mActionsImpl;	 Catch:{ all -> 0x008b }
        monitor-exit(r0);	 Catch:{ all -> 0x008b }
        return r2;
    L_0x0086:
        r2 = move-exception;
        maybeCloseAndLogError(r3);	 Catch:{ all -> 0x008b }
        throw r2;	 Catch:{ all -> 0x008b }
    L_0x008b:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x008b }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.textclassifier.TextClassifierImpl.getActionsImpl():com.google.android.textclassifier.ActionsSuggestionsModel");
    }

    private String createId(String text, int start, int end) {
        String createId;
        synchronized (this.mLock) {
            createId = SelectionSessionLogger.createId(text, start, end, this.mContext, this.mAnnotatorModelInUse.getVersion(), this.mAnnotatorModelInUse.getSupportedLocales());
        }
        return createId;
    }

    private static String concatenateLocales(LocaleList locales) {
        return locales == null ? "" : locales.toLanguageTags();
    }

    private TextClassification createClassificationResult(ClassificationResult[] classifications, String text, int start, int end, Instant referenceTime) {
        ClassificationResult[] classificationResultArr = classifications;
        String str = text;
        int i = start;
        int i2 = end;
        String classifiedText = text.substring(start, end);
        TextClassification.Builder builder = new TextClassification.Builder().setText(classifiedText);
        int typeCount = classificationResultArr.length;
        boolean z = false;
        ClassificationResult highestScoringResult = typeCount > 0 ? classificationResultArr[0] : null;
        for (int i3 = 0; i3 < typeCount; i3++) {
            builder.setEntityType(classificationResultArr[i3]);
            if (classificationResultArr[i3].getScore() > highestScoringResult.getScore()) {
                highestScoringResult = classificationResultArr[i3];
            }
        }
        Pair<Bundle, Bundle> languagesBundles = generateLanguageBundles(str, i, i2);
        Bundle textLanguagesBundle = languagesBundles.first;
        Bundle foreignLanguageBundle = languagesBundles.second;
        builder.setForeignLanguageExtra(foreignLanguageBundle);
        boolean isPrimaryAction = true;
        ClassificationIntentFactory classificationIntentFactory = this.mClassificationIntentFactory;
        Context context = this.mContext;
        if (foreignLanguageBundle != null) {
            z = true;
        }
        Bundle textLanguagesBundle2 = textLanguagesBundle;
        List<LabeledIntent> labeledIntents = classificationIntentFactory.create(context, classifiedText, z, referenceTime, highestScoringResult);
        TitleChooser titleChooser = -$$Lambda$TextClassifierImpl$naj1VfHYH1Qfut8yLHu8DlsggQE.INSTANCE;
        for (LabeledIntent labeledIntent : labeledIntents) {
            Result result = labeledIntent.resolve(this.mContext, titleChooser, textLanguagesBundle2);
            if (result != null) {
                List<LabeledIntent> labeledIntents2;
                TitleChooser titleChooser2;
                Intent intent = result.resolvedIntent;
                Bundle textLanguagesBundle3 = textLanguagesBundle2;
                textLanguagesBundle2 = result.remoteAction;
                if (isPrimaryAction) {
                    labeledIntents2 = labeledIntents;
                    titleChooser2 = titleChooser;
                    builder.setIcon(textLanguagesBundle2.getIcon().loadDrawable(this.mContext));
                    builder.setLabel(textLanguagesBundle2.getTitle().toString());
                    builder.setIntent(intent);
                    builder.setOnClickListener(TextClassification.createIntentOnClickListener(TextClassification.createPendingIntent(this.mContext, intent, labeledIntent.requestCode)));
                    isPrimaryAction = null;
                } else {
                    labeledIntents2 = labeledIntents;
                    titleChooser2 = titleChooser;
                }
                builder.addAction(textLanguagesBundle2, intent);
                textLanguagesBundle2 = textLanguagesBundle3;
                labeledIntents = labeledIntents2;
                titleChooser = titleChooser2;
            }
        }
        return builder.setId(createId(str, i, i2)).build();
    }

    private Pair<Bundle, Bundle> generateLanguageBundles(String context, int start, int end) {
        String str = "androidtc";
        if (!this.mSettings.isTranslateInClassificationEnabled()) {
            return null;
        }
        try {
            float threshold = getLangIdThreshold();
            if (threshold >= 0.0f) {
                if (threshold <= 1.0f) {
                    EntityConfidence languageScores = detectLanguages(context, start, end);
                    if (languageScores.getEntities().isEmpty()) {
                        return Pair.create(null, null);
                    }
                    Bundle textLanguagesBundle = new Bundle();
                    ExtrasUtils.putTopLanguageScores(textLanguagesBundle, languageScores);
                    String language = (String) languageScores.getEntities().get(0);
                    float score = languageScores.getConfidenceScore(language);
                    if (score < threshold) {
                        return Pair.create(textLanguagesBundle, null);
                    }
                    Log.v(str, String.format(Locale.US, "Language detected: <%s:%.2f>", new Object[]{language, Float.valueOf(score)}));
                    Locale detected = new Locale(language);
                    LocaleList deviceLocales = LocaleList.getDefault();
                    int size = deviceLocales.size();
                    for (int i = 0; i < size; i++) {
                        if (deviceLocales.get(i).getLanguage().equals(detected.getLanguage())) {
                            return Pair.create(textLanguagesBundle, null);
                        }
                    }
                    return Pair.create(textLanguagesBundle, ExtrasUtils.createForeignLanguageExtra(detected.getLanguage(), score, getLangIdImpl().getVersion()));
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[detectForeignLanguage] unexpected threshold is found: ");
            stringBuilder.append(threshold);
            Log.w(str, stringBuilder.toString());
            return Pair.create(null, null);
        } catch (Throwable t) {
            Log.e(str, "Error generating language bundles.", t);
            return Pair.create(null, null);
        }
    }

    private EntityConfidence detectLanguages(String text, int start, int end) throws FileNotFoundException {
        int i = start;
        int i2 = end;
        Preconditions.checkArgument(i >= 0);
        Preconditions.checkArgument(i2 <= text.length());
        Preconditions.checkArgument(i <= i2);
        float[] langIdContextSettings = this.mSettings.getLangIdContextSettings();
        int minimumTextSize = (int) langIdContextSettings[0];
        float penalizeRatio = langIdContextSettings[1];
        float subjectTextScoreRatio = langIdContextSettings[2];
        float moreTextScoreRatio = 1.0f - subjectTextScoreRatio;
        Log.v("androidtc", String.format(Locale.US, "LangIdContextSettings: minimumTextSize=%d, penalizeRatio=%.2f, subjectTextScoreRatio=%.2f, moreTextScoreRatio=%.2f", new Object[]{Integer.valueOf(minimumTextSize), Float.valueOf(penalizeRatio), Float.valueOf(subjectTextScoreRatio), Float.valueOf(moreTextScoreRatio)}));
        if (i2 - i < minimumTextSize && penalizeRatio <= 0.0f) {
            return new EntityConfidence(Collections.emptyMap());
        }
        String subject = text.substring(start, end);
        EntityConfidence scores = detectLanguages(subject);
        String str;
        if (subject.length() >= minimumTextSize) {
            str = text;
        } else if (subject.length() == text.length()) {
            str = text;
        } else if (subjectTextScoreRatio * penalizeRatio >= 1.0f) {
            str = text;
        } else {
            EntityConfidence moreTextScores;
            if (moreTextScoreRatio >= 0.0f) {
                moreTextScores = detectLanguages(Utils.getSubString(text, i, i2, minimumTextSize));
            } else {
                str = text;
                moreTextScores = new EntityConfidence(Collections.emptyMap());
            }
            Map newScores = new ArrayMap();
            Set<String> languages = new ArraySet();
            languages.addAll(scores.getEntities());
            languages.addAll(moreTextScores.getEntities());
            for (String language : languages) {
                newScores.put(language, Float.valueOf(((scores.getConfidenceScore(language) * subjectTextScoreRatio) + (moreTextScores.getConfidenceScore(language) * moreTextScoreRatio)) * penalizeRatio));
            }
            return new EntityConfidence(newScores);
        }
        return scores;
    }

    private EntityConfidence detectLanguages(String text) throws FileNotFoundException {
        LanguageResult[] langResults = getLangIdImpl().detectLanguages(text);
        Map languagesMap = new ArrayMap();
        for (LanguageResult langResult : langResults) {
            languagesMap.put(langResult.getLanguage(), Float.valueOf(langResult.getScore()));
        }
        return new EntityConfidence(languagesMap);
    }

    private float getLangIdThreshold() {
        try {
            float langIdThresholdOverride;
            if (this.mSettings.getLangIdThresholdOverride() >= 0.0f) {
                langIdThresholdOverride = this.mSettings.getLangIdThresholdOverride();
            } else {
                langIdThresholdOverride = getLangIdImpl().getLangIdThreshold();
            }
            return langIdThresholdOverride;
        } catch (FileNotFoundException e) {
            Log.v("androidtc", "Using default foreign language threshold: 0.5");
            return 0.5f;
        }
    }

    public void dump(IndentingPrintWriter printWriter) {
        synchronized (this.mLock) {
            printWriter.println("TextClassifierImpl:");
            printWriter.increaseIndent();
            printWriter.println("Annotator model file(s):");
            printWriter.increaseIndent();
            for (ModelFile modelFile : this.mAnnotatorModelFileManager.listModelFiles()) {
                printWriter.println(modelFile.toString());
            }
            printWriter.decreaseIndent();
            printWriter.println("LangID model file(s):");
            printWriter.increaseIndent();
            for (ModelFile modelFile2 : this.mLangIdModelFileManager.listModelFiles()) {
                printWriter.println(modelFile2.toString());
            }
            printWriter.decreaseIndent();
            printWriter.println("Actions model file(s):");
            printWriter.increaseIndent();
            for (ModelFile modelFile22 : this.mActionsModelFileManager.listModelFiles()) {
                printWriter.println(modelFile22.toString());
            }
            printWriter.decreaseIndent();
            printWriter.printPair("mFallback", this.mFallback);
            printWriter.decreaseIndent();
            printWriter.println();
        }
    }

    private static void maybeCloseAndLogError(ParcelFileDescriptor fd) {
        if (fd != null) {
            try {
                fd.close();
            } catch (IOException e) {
                Log.e("androidtc", "Error closing file.", e);
            }
        }
    }

    private String getResourceLocalesString() {
        try {
            return this.mContext.getResources().getConfiguration().getLocales().toLanguageTags();
        } catch (NullPointerException e) {
            return LocaleList.getDefault().toLanguageTags();
        }
    }
}
