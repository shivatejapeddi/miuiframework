package android.hardware.soundtrigger;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Slog;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class KeyphraseEnrollmentInfo {
    public static final String ACTION_MANAGE_VOICE_KEYPHRASES = "com.android.intent.action.MANAGE_VOICE_KEYPHRASES";
    public static final String EXTRA_VOICE_KEYPHRASE_ACTION = "com.android.intent.extra.VOICE_KEYPHRASE_ACTION";
    public static final String EXTRA_VOICE_KEYPHRASE_HINT_TEXT = "com.android.intent.extra.VOICE_KEYPHRASE_HINT_TEXT";
    public static final String EXTRA_VOICE_KEYPHRASE_LOCALE = "com.android.intent.extra.VOICE_KEYPHRASE_LOCALE";
    private static final String TAG = "KeyphraseEnrollmentInfo";
    private static final String VOICE_KEYPHRASE_META_DATA = "android.voice_enrollment";
    private final Map<KeyphraseMetadata, String> mKeyphrasePackageMap;
    private final KeyphraseMetadata[] mKeyphrases;
    private String mParseError;

    public KeyphraseEnrollmentInfo(PackageManager pm) {
        List<ResolveInfo> ris = pm.queryIntentActivities(new Intent(ACTION_MANAGE_VOICE_KEYPHRASES), 65536);
        if (ris == null || ris.isEmpty()) {
            this.mParseError = "No enrollment applications found";
            this.mKeyphrasePackageMap = Collections.emptyMap();
            this.mKeyphrases = null;
            return;
        }
        String str;
        Iterable parseErrors = new LinkedList();
        this.mKeyphrasePackageMap = new HashMap();
        Iterator it = ris.iterator();
        while (true) {
            boolean hasNext = it.hasNext();
            str = TAG;
            if (!hasNext) {
                break;
            }
            ResolveInfo ri = (ResolveInfo) it.next();
            try {
                ApplicationInfo ai = pm.getApplicationInfo(ri.activityInfo.packageName, 128);
                StringBuilder stringBuilder;
                if ((ai.privateFlags & 8) == 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(ai.packageName);
                    stringBuilder.append("is not a privileged system app");
                    Slog.w(str, stringBuilder.toString());
                } else if (permission.MANAGE_VOICE_KEYPHRASES.equals(ai.permission)) {
                    KeyphraseMetadata metadata = getKeyphraseMetadataFromApplicationInfo(pm, ai, parseErrors);
                    if (metadata != null) {
                        this.mKeyphrasePackageMap.put(metadata, ai.packageName);
                    }
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(ai.packageName);
                    stringBuilder.append(" does not require MANAGE_VOICE_KEYPHRASES");
                    Slog.w(str, stringBuilder.toString());
                }
            } catch (NameNotFoundException e) {
                String error = new StringBuilder();
                error.append("error parsing voice enrollment meta-data for ");
                error.append(ri.activityInfo.packageName);
                error = error.toString();
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(error);
                stringBuilder2.append(": ");
                stringBuilder2.append(e);
                parseErrors.add(stringBuilder2.toString());
                Slog.w(str, error, e);
            }
        }
        if (this.mKeyphrasePackageMap.isEmpty()) {
            String error2 = "No suitable enrollment application found";
            parseErrors.add(error2);
            Slog.w(str, error2);
            this.mKeyphrases = null;
        } else {
            this.mKeyphrases = (KeyphraseMetadata[]) this.mKeyphrasePackageMap.keySet().toArray(new KeyphraseMetadata[this.mKeyphrasePackageMap.size()]);
        }
        if (!parseErrors.isEmpty()) {
            this.mParseError = TextUtils.join((CharSequence) "\n", parseErrors);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:52:0x0109 A:{Catch:{ XmlPullParserException -> 0x010b, IOException -> 0x00da, NameNotFoundException -> 0x00aa, all -> 0x00a3, all -> 0x013d }} */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00d9 A:{Catch:{ XmlPullParserException -> 0x010b, IOException -> 0x00da, NameNotFoundException -> 0x00aa, all -> 0x00a3, all -> 0x013d }} */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0109 A:{Catch:{ XmlPullParserException -> 0x010b, IOException -> 0x00da, NameNotFoundException -> 0x00aa, all -> 0x00a3, all -> 0x013d }} */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00d9 A:{Catch:{ XmlPullParserException -> 0x010b, IOException -> 0x00da, NameNotFoundException -> 0x00aa, all -> 0x00a3, all -> 0x013d }} */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0140  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0140  */
    /* JADX WARNING: Missing block: B:56:0x0138, code skipped:
            if (r6 != null) goto L_0x0086;
     */
    private android.hardware.soundtrigger.KeyphraseMetadata getKeyphraseMetadataFromApplicationInfo(android.content.pm.PackageManager r17, android.content.pm.ApplicationInfo r18, java.util.List<java.lang.String> r19) {
        /*
        r16 = this;
        r1 = r18;
        r2 = r19;
        r3 = ": ";
        r4 = "Error parsing keyphrase enrollment meta-data for ";
        r5 = "KeyphraseEnrollmentInfo";
        r6 = 0;
        r7 = r1.packageName;
        r8 = 0;
        r0 = "android.voice_enrollment";
        r9 = r17;
        r0 = r1.loadXmlMetaData(r9, r0);	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r6 = r0;
        r0 = 0;
        if (r6 != 0) goto L_0x0038;
    L_0x001a:
        r10 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r10.<init>();	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r11 = "No android.voice_enrollment meta-data for ";
        r10.append(r11);	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r10.append(r7);	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r10 = r10.toString();	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r2.add(r10);	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        android.util.Slog.w(r5, r10);	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        if (r6 == 0) goto L_0x0037;
    L_0x0034:
        r6.close();
    L_0x0037:
        return r0;
    L_0x0038:
        r10 = r17.getResourcesForApplication(r18);	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r11 = android.util.Xml.asAttributeSet(r6);	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
    L_0x0040:
        r12 = r6.next();	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r13 = r12;
        r14 = 1;
        if (r12 == r14) goto L_0x004c;
    L_0x0048:
        r12 = 2;
        if (r13 == r12) goto L_0x004c;
    L_0x004b:
        goto L_0x0040;
    L_0x004c:
        r12 = r6.getName();	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r14 = "voice-enrollment-application";
        r14 = r14.equals(r12);	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        if (r14 != 0) goto L_0x0075;
    L_0x0059:
        r14 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r14.<init>();	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r15 = "Meta-data does not start with voice-enrollment-application tag for ";
        r14.append(r15);	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r14.append(r7);	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r14 = r14.toString();	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r2.add(r14);	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        android.util.Slog.w(r5, r14);	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r6.close();
        return r0;
    L_0x0075:
        r0 = com.android.internal.R.styleable.VoiceEnrollmentApplication;	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r0 = r10.obtainAttributes(r11, r0);	 Catch:{ XmlPullParserException -> 0x009f, IOException -> 0x009b, NameNotFoundException -> 0x0097, all -> 0x0092 }
        r14 = r16;
        r15 = r14.getKeyphraseFromTypedArray(r0, r7, r2);	 Catch:{ XmlPullParserException -> 0x008f, IOException -> 0x008d, NameNotFoundException -> 0x008b }
        r8 = r15;
        r0.recycle();	 Catch:{ XmlPullParserException -> 0x008f, IOException -> 0x008d, NameNotFoundException -> 0x008b }
    L_0x0086:
        r6.close();
        goto L_0x013c;
    L_0x008b:
        r0 = move-exception;
        goto L_0x00af;
    L_0x008d:
        r0 = move-exception;
        goto L_0x00df;
    L_0x008f:
        r0 = move-exception;
        goto L_0x0110;
    L_0x0092:
        r0 = move-exception;
        r14 = r16;
        goto L_0x013e;
    L_0x0097:
        r0 = move-exception;
        r14 = r16;
        goto L_0x00af;
    L_0x009b:
        r0 = move-exception;
        r14 = r16;
        goto L_0x00df;
    L_0x009f:
        r0 = move-exception;
        r14 = r16;
        goto L_0x0110;
    L_0x00a3:
        r0 = move-exception;
        r14 = r16;
        r9 = r17;
        goto L_0x013e;
    L_0x00aa:
        r0 = move-exception;
        r14 = r16;
        r9 = r17;
    L_0x00af:
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013d }
        r10.<init>();	 Catch:{ all -> 0x013d }
        r10.append(r4);	 Catch:{ all -> 0x013d }
        r10.append(r7);	 Catch:{ all -> 0x013d }
        r4 = r10.toString();	 Catch:{ all -> 0x013d }
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013d }
        r10.<init>();	 Catch:{ all -> 0x013d }
        r10.append(r4);	 Catch:{ all -> 0x013d }
        r10.append(r3);	 Catch:{ all -> 0x013d }
        r10.append(r0);	 Catch:{ all -> 0x013d }
        r3 = r10.toString();	 Catch:{ all -> 0x013d }
        r2.add(r3);	 Catch:{ all -> 0x013d }
        android.util.Slog.w(r5, r4, r0);	 Catch:{ all -> 0x013d }
        if (r6 == 0) goto L_0x013c;
    L_0x00d9:
        goto L_0x0086;
    L_0x00da:
        r0 = move-exception;
        r14 = r16;
        r9 = r17;
    L_0x00df:
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013d }
        r10.<init>();	 Catch:{ all -> 0x013d }
        r10.append(r4);	 Catch:{ all -> 0x013d }
        r10.append(r7);	 Catch:{ all -> 0x013d }
        r4 = r10.toString();	 Catch:{ all -> 0x013d }
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013d }
        r10.<init>();	 Catch:{ all -> 0x013d }
        r10.append(r4);	 Catch:{ all -> 0x013d }
        r10.append(r3);	 Catch:{ all -> 0x013d }
        r10.append(r0);	 Catch:{ all -> 0x013d }
        r3 = r10.toString();	 Catch:{ all -> 0x013d }
        r2.add(r3);	 Catch:{ all -> 0x013d }
        android.util.Slog.w(r5, r4, r0);	 Catch:{ all -> 0x013d }
        if (r6 == 0) goto L_0x013c;
    L_0x0109:
        goto L_0x0086;
    L_0x010b:
        r0 = move-exception;
        r14 = r16;
        r9 = r17;
    L_0x0110:
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013d }
        r10.<init>();	 Catch:{ all -> 0x013d }
        r10.append(r4);	 Catch:{ all -> 0x013d }
        r10.append(r7);	 Catch:{ all -> 0x013d }
        r4 = r10.toString();	 Catch:{ all -> 0x013d }
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013d }
        r10.<init>();	 Catch:{ all -> 0x013d }
        r10.append(r4);	 Catch:{ all -> 0x013d }
        r10.append(r3);	 Catch:{ all -> 0x013d }
        r10.append(r0);	 Catch:{ all -> 0x013d }
        r3 = r10.toString();	 Catch:{ all -> 0x013d }
        r2.add(r3);	 Catch:{ all -> 0x013d }
        android.util.Slog.w(r5, r4, r0);	 Catch:{ all -> 0x013d }
        if (r6 == 0) goto L_0x013c;
    L_0x013a:
        goto L_0x0086;
    L_0x013c:
        return r8;
    L_0x013d:
        r0 = move-exception;
    L_0x013e:
        if (r6 == 0) goto L_0x0143;
    L_0x0140:
        r6.close();
    L_0x0143:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.soundtrigger.KeyphraseEnrollmentInfo.getKeyphraseMetadataFromApplicationInfo(android.content.pm.PackageManager, android.content.pm.ApplicationInfo, java.util.List):android.hardware.soundtrigger.KeyphraseMetadata");
    }

    private KeyphraseMetadata getKeyphraseFromTypedArray(TypedArray array, String packageName, List<String> parseErrors) {
        String error;
        int i = 0;
        int searchKeyphraseId = array.getInt(0, -1);
        String str = TAG;
        String error2;
        if (searchKeyphraseId <= 0) {
            error2 = new StringBuilder();
            error2.append("No valid searchKeyphraseId specified in meta-data for ");
            error2.append(packageName);
            error2 = error2.toString();
            parseErrors.add(error2);
            Slog.w(str, error2);
            return null;
        }
        String searchKeyphrase = array.getString(1);
        StringBuilder stringBuilder;
        if (searchKeyphrase == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("No valid searchKeyphrase specified in meta-data for ");
            stringBuilder.append(packageName);
            error2 = stringBuilder.toString();
            parseErrors.add(error2);
            Slog.w(str, error2);
            return null;
        }
        String searchKeyphraseSupportedLocales = array.getString(2);
        if (searchKeyphraseSupportedLocales == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("No valid searchKeyphraseSupportedLocales specified in meta-data for ");
            stringBuilder.append(packageName);
            error2 = stringBuilder.toString();
            parseErrors.add(error2);
            Slog.w(str, error2);
            return null;
        }
        ArraySet<Locale> locales = new ArraySet();
        if (!TextUtils.isEmpty(searchKeyphraseSupportedLocales)) {
            try {
                String[] supportedLocalesDelimited = searchKeyphraseSupportedLocales.split(",");
                while (i < supportedLocalesDelimited.length) {
                    locales.add(Locale.forLanguageTag(supportedLocalesDelimited[i]));
                    i++;
                }
            } catch (Exception e) {
                error = new StringBuilder();
                error.append("Error reading searchKeyphraseSupportedLocales from meta-data for ");
                error.append(packageName);
                error = error.toString();
                parseErrors.add(error);
                Slog.w(str, error);
                return null;
            }
        }
        int recognitionModes = array.getInt(3, -1);
        if (recognitionModes >= 0) {
            return new KeyphraseMetadata(searchKeyphraseId, searchKeyphrase, locales, recognitionModes);
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("No valid searchKeyphraseRecognitionFlags specified in meta-data for ");
        stringBuilder2.append(packageName);
        error = stringBuilder2.toString();
        parseErrors.add(error);
        Slog.w(str, error);
        return null;
    }

    public String getParseError() {
        return this.mParseError;
    }

    public KeyphraseMetadata[] listKeyphraseMetadata() {
        return this.mKeyphrases;
    }

    public Intent getManageKeyphraseIntent(int action, String keyphrase, Locale locale) {
        Map map = this.mKeyphrasePackageMap;
        if (map == null || map.isEmpty()) {
            Slog.w(TAG, "No enrollment application exists");
            return null;
        }
        KeyphraseMetadata keyphraseMetadata = getKeyphraseMetadata(keyphrase, locale);
        if (keyphraseMetadata == null) {
            return null;
        }
        return new Intent(ACTION_MANAGE_VOICE_KEYPHRASES).setPackage((String) this.mKeyphrasePackageMap.get(keyphraseMetadata)).putExtra(EXTRA_VOICE_KEYPHRASE_HINT_TEXT, keyphrase).putExtra(EXTRA_VOICE_KEYPHRASE_LOCALE, locale.toLanguageTag()).putExtra(EXTRA_VOICE_KEYPHRASE_ACTION, action);
    }

    public KeyphraseMetadata getKeyphraseMetadata(String keyphrase, Locale locale) {
        KeyphraseMetadata[] keyphraseMetadataArr = this.mKeyphrases;
        if (keyphraseMetadataArr != null && keyphraseMetadataArr.length > 0) {
            for (KeyphraseMetadata keyphraseMetadata : keyphraseMetadataArr) {
                if (keyphraseMetadata.supportsPhrase(keyphrase) && keyphraseMetadata.supportsLocale(locale)) {
                    return keyphraseMetadata;
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No enrollment application supports the given keyphrase/locale: '");
        stringBuilder.append(keyphrase);
        stringBuilder.append("'/");
        stringBuilder.append(locale);
        Slog.w(TAG, stringBuilder.toString());
        return null;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("KeyphraseEnrollmentInfo [Keyphrases=");
        stringBuilder.append(this.mKeyphrasePackageMap.toString());
        stringBuilder.append(", ParseError=");
        stringBuilder.append(this.mParseError);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
