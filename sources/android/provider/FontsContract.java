package android.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.Typeface.CustomFallbackBuilder;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontFamily.Builder;
import android.graphics.fonts.FontStyle;
import android.graphics.fonts.FontVariationAxis;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.LruCache;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class FontsContract {
    private static final long SYNC_FONT_FETCH_TIMEOUT_MS = 500;
    private static final String TAG = "FontsContract";
    private static final int THREAD_RENEWAL_THRESHOLD_MS = 10000;
    private static final Comparator<byte[]> sByteArrayComparator = -$$Lambda$FontsContract$3FDNQd-WsglsyDhif-aHVbzkfrA.INSTANCE;
    private static volatile Context sContext;
    @GuardedBy({"sLock"})
    private static Handler sHandler;
    @GuardedBy({"sLock"})
    private static Set<String> sInQueueSet;
    private static final Object sLock = new Object();
    private static final Runnable sReplaceDispatcherThreadRunnable = new Runnable() {
        public void run() {
            synchronized (FontsContract.sLock) {
                if (FontsContract.sThread != null) {
                    FontsContract.sThread.quitSafely();
                    FontsContract.sThread = null;
                    FontsContract.sHandler = null;
                }
            }
        }
    };
    @GuardedBy({"sLock"})
    private static HandlerThread sThread;
    private static final LruCache<String, Typeface> sTypefaceCache = new LruCache(16);

    public static final class Columns implements BaseColumns {
        public static final String FILE_ID = "file_id";
        public static final String ITALIC = "font_italic";
        public static final String RESULT_CODE = "result_code";
        public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
        public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
        public static final int RESULT_CODE_MALFORMED_QUERY = 3;
        public static final int RESULT_CODE_OK = 0;
        public static final String TTC_INDEX = "font_ttc_index";
        public static final String VARIATION_SETTINGS = "font_variation_settings";
        public static final String WEIGHT = "font_weight";

        private Columns() {
        }
    }

    public static class FontFamilyResult {
        public static final int STATUS_OK = 0;
        public static final int STATUS_REJECTED = 3;
        public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
        public static final int STATUS_WRONG_CERTIFICATES = 1;
        private final FontInfo[] mFonts;
        private final int mStatusCode;

        @Retention(RetentionPolicy.SOURCE)
        @interface FontResultStatus {
        }

        public FontFamilyResult(int statusCode, FontInfo[] fonts) {
            this.mStatusCode = statusCode;
            this.mFonts = fonts;
        }

        public int getStatusCode() {
            return this.mStatusCode;
        }

        public FontInfo[] getFonts() {
            return this.mFonts;
        }
    }

    public static class FontInfo {
        private final FontVariationAxis[] mAxes;
        private final boolean mItalic;
        private final int mResultCode;
        private final int mTtcIndex;
        private final Uri mUri;
        private final int mWeight;

        public FontInfo(Uri uri, int ttcIndex, FontVariationAxis[] axes, int weight, boolean italic, int resultCode) {
            this.mUri = (Uri) Preconditions.checkNotNull(uri);
            this.mTtcIndex = ttcIndex;
            this.mAxes = axes;
            this.mWeight = weight;
            this.mItalic = italic;
            this.mResultCode = resultCode;
        }

        public Uri getUri() {
            return this.mUri;
        }

        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        public FontVariationAxis[] getAxes() {
            return this.mAxes;
        }

        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }

        public int getResultCode() {
            return this.mResultCode;
        }
    }

    public static class FontRequestCallback {
        public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
        public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
        public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
        public static final int FAIL_REASON_MALFORMED_QUERY = 3;
        public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
        public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;

        @Retention(RetentionPolicy.SOURCE)
        @interface FontRequestFailReason {
        }

        public void onTypefaceRetrieved(Typeface typeface) {
        }

        public void onTypefaceRequestFailed(int reason) {
        }
    }

    private FontsContract() {
    }

    public static void setApplicationContextForResources(Context context) {
        sContext = context.getApplicationContext();
    }

    /* JADX WARNING: Missing block: B:16:0x008c, code skipped:
            return r0;
     */
    public static android.graphics.Typeface getFontSync(android.provider.FontRequest r17) {
        /*
        r9 = r17.getIdentifier();
        r0 = sTypefaceCache;
        r0 = r0.get(r9);
        r10 = r0;
        r10 = (android.graphics.Typeface) r10;
        if (r10 == 0) goto L_0x0010;
    L_0x000f:
        return r10;
    L_0x0010:
        r11 = sLock;
        monitor-enter(r11);
        r0 = sHandler;	 Catch:{ all -> 0x00e0 }
        if (r0 != 0) goto L_0x0034;
    L_0x0017:
        r0 = new android.os.HandlerThread;	 Catch:{ all -> 0x00e0 }
        r1 = "fonts";
        r2 = 10;
        r0.<init>(r1, r2);	 Catch:{ all -> 0x00e0 }
        sThread = r0;	 Catch:{ all -> 0x00e0 }
        r0 = sThread;	 Catch:{ all -> 0x00e0 }
        r0.start();	 Catch:{ all -> 0x00e0 }
        r0 = new android.os.Handler;	 Catch:{ all -> 0x00e0 }
        r1 = sThread;	 Catch:{ all -> 0x00e0 }
        r1 = r1.getLooper();	 Catch:{ all -> 0x00e0 }
        r0.<init>(r1);	 Catch:{ all -> 0x00e0 }
        sHandler = r0;	 Catch:{ all -> 0x00e0 }
    L_0x0034:
        r5 = new java.util.concurrent.locks.ReentrantLock;	 Catch:{ all -> 0x00e0 }
        r5.<init>();	 Catch:{ all -> 0x00e0 }
        r0 = r5.newCondition();	 Catch:{ all -> 0x00e0 }
        r12 = r0;
        r4 = new java.util.concurrent.atomic.AtomicReference;	 Catch:{ all -> 0x00e0 }
        r4.<init>();	 Catch:{ all -> 0x00e0 }
        r7 = new java.util.concurrent.atomic.AtomicBoolean;	 Catch:{ all -> 0x00e0 }
        r13 = 1;
        r7.<init>(r13);	 Catch:{ all -> 0x00e0 }
        r0 = new java.util.concurrent.atomic.AtomicBoolean;	 Catch:{ all -> 0x00e0 }
        r1 = 0;
        r0.<init>(r1);	 Catch:{ all -> 0x00e0 }
        r14 = r0;
        r0 = sHandler;	 Catch:{ all -> 0x00e0 }
        r15 = new android.provider.-$$Lambda$FontsContract$rqfIZKvP1frnI9vP1hVA8jQN_RE;	 Catch:{ all -> 0x00e0 }
        r1 = r15;
        r2 = r17;
        r3 = r9;
        r6 = r14;
        r8 = r12;
        r1.<init>(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ all -> 0x00e0 }
        r0.post(r15);	 Catch:{ all -> 0x00e0 }
        r0 = sHandler;	 Catch:{ all -> 0x00e0 }
        r1 = sReplaceDispatcherThreadRunnable;	 Catch:{ all -> 0x00e0 }
        r0.removeCallbacks(r1);	 Catch:{ all -> 0x00e0 }
        r0 = sHandler;	 Catch:{ all -> 0x00e0 }
        r1 = sReplaceDispatcherThreadRunnable;	 Catch:{ all -> 0x00e0 }
        r2 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r0.postDelayed(r1, r2);	 Catch:{ all -> 0x00e0 }
        r0 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ all -> 0x00e0 }
        r1 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        r0 = r0.toNanos(r1);	 Catch:{ all -> 0x00e0 }
        r1 = r0;
        r5.lock();	 Catch:{ all -> 0x00e0 }
        r0 = r7.get();	 Catch:{ all -> 0x00db }
        if (r0 != 0) goto L_0x008d;
    L_0x0082:
        r0 = r4.get();	 Catch:{ all -> 0x00db }
        r0 = (android.graphics.Typeface) r0;	 Catch:{ all -> 0x00db }
        r5.unlock();	 Catch:{ all -> 0x00e0 }
        monitor-exit(r11);	 Catch:{ all -> 0x00e0 }
        return r0;
    L_0x008d:
        r15 = r12.awaitNanos(r1);	 Catch:{ InterruptedException -> 0x0094 }
        r0 = r15;
        r1 = r0;
        goto L_0x0095;
    L_0x0094:
        r0 = move-exception;
    L_0x0095:
        r0 = r7.get();	 Catch:{ all -> 0x00db }
        if (r0 != 0) goto L_0x00a6;
    L_0x009b:
        r0 = r4.get();	 Catch:{ all -> 0x00db }
        r0 = (android.graphics.Typeface) r0;	 Catch:{ all -> 0x00db }
        r5.unlock();	 Catch:{ all -> 0x00e0 }
        monitor-exit(r11);	 Catch:{ all -> 0x00e0 }
        return r0;
    L_0x00a6:
        r15 = 0;
        r0 = (r1 > r15 ? 1 : (r1 == r15 ? 0 : -1));
        if (r0 > 0) goto L_0x008d;
    L_0x00ac:
        r14.set(r13);	 Catch:{ all -> 0x00db }
        r0 = "FontsContract";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00db }
        r3.<init>();	 Catch:{ all -> 0x00db }
        r6 = "Remote font fetch timed out: ";
        r3.append(r6);	 Catch:{ all -> 0x00db }
        r6 = r17.getProviderAuthority();	 Catch:{ all -> 0x00db }
        r3.append(r6);	 Catch:{ all -> 0x00db }
        r6 = "/";
        r3.append(r6);	 Catch:{ all -> 0x00db }
        r6 = r17.getQuery();	 Catch:{ all -> 0x00db }
        r3.append(r6);	 Catch:{ all -> 0x00db }
        r3 = r3.toString();	 Catch:{ all -> 0x00db }
        android.util.Log.w(r0, r3);	 Catch:{ all -> 0x00db }
        r0 = 0;
        r5.unlock();	 Catch:{ all -> 0x00e0 }
        monitor-exit(r11);	 Catch:{ all -> 0x00e0 }
        return r0;
    L_0x00db:
        r0 = move-exception;
        r5.unlock();	 Catch:{ all -> 0x00e0 }
        throw r0;	 Catch:{ all -> 0x00e0 }
    L_0x00e0:
        r0 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x00e0 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.provider.FontsContract.getFontSync(android.provider.FontRequest):android.graphics.Typeface");
    }

    static /* synthetic */ void lambda$getFontSync$0(FontRequest request, String id, AtomicReference holder, Lock lock, AtomicBoolean timeout, AtomicBoolean waiting, Condition cond) {
        try {
            FontFamilyResult result = fetchFonts(sContext, null, request);
            if (result.getStatusCode() == 0) {
                Typeface typeface = buildTypeface(sContext, null, result.getFonts());
                if (typeface != null) {
                    sTypefaceCache.put(id, typeface);
                }
                holder.set(typeface);
            }
        } catch (NameNotFoundException e) {
        }
        lock.lock();
        try {
            if (!timeout.get()) {
                waiting.set(false);
                cond.signal();
            }
            lock.unlock();
        } catch (Throwable th) {
            lock.unlock();
        }
    }

    public static void requestFonts(Context context, FontRequest request, Handler handler, CancellationSignal cancellationSignal, FontRequestCallback callback) {
        Handler callerThreadHandler = new Handler();
        Typeface cachedTypeface = (Typeface) sTypefaceCache.get(request.getIdentifier());
        if (cachedTypeface != null) {
            callerThreadHandler.post(new -$$Lambda$FontsContract$p_tsXYYYpEH0-EJSp2uPrJ33dkU(callback, cachedTypeface));
        } else {
            handler.post(new -$$Lambda$FontsContract$dFs2m4XF5xdir4W3T-ncUQAVX8k(context, cancellationSignal, request, callerThreadHandler, callback));
        }
    }

    static /* synthetic */ void lambda$requestFonts$12(Context context, CancellationSignal cancellationSignal, FontRequest request, Handler callerThreadHandler, FontRequestCallback callback) {
        try {
            FontFamilyResult result = fetchFonts(context, cancellationSignal, request);
            Typeface anotherCachedTypeface = (Typeface) sTypefaceCache.get(request.getIdentifier());
            if (anotherCachedTypeface != null) {
                callerThreadHandler.post(new -$$Lambda$FontsContract$xDMhIK5JxjXFDIXBeQbZ_hdXTBc(callback, anotherCachedTypeface));
            } else if (result.getStatusCode() != 0) {
                int statusCode = result.getStatusCode();
                if (statusCode == 1) {
                    callerThreadHandler.post(new -$$Lambda$FontsContract$YhiTIVckhFBdgNR2V1bGY3Q1Nqg(callback));
                } else if (statusCode != 2) {
                    callerThreadHandler.post(new -$$Lambda$FontsContract$DV4gvjPxJzdQvcfoIJqGrzFtTQs(callback));
                } else {
                    callerThreadHandler.post(new -$$Lambda$FontsContract$FCawscMFN_8Qxcb2EdA5gdE-O2k(callback));
                }
            } else {
                FontInfo[] fonts = result.getFonts();
                if (fonts == null || fonts.length == 0) {
                    callerThreadHandler.post(new -$$Lambda$FontsContract$LJ3jfZobcxq5xTMmb88GlM1r9Jk(callback));
                    return;
                }
                int resultCode;
                for (FontInfo font : fonts) {
                    if (font.getResultCode() != 0) {
                        resultCode = font.getResultCode();
                        if (resultCode < 0) {
                            callerThreadHandler.post(new -$$Lambda$FontsContract$Qvl9aVA7txTF3tFcFbbKD_nWpuM(callback));
                        } else {
                            callerThreadHandler.post(new -$$Lambda$FontsContract$rvEOORTXb3mMYTLkoH9nlHQr9Iw(callback, resultCode));
                        }
                        return;
                    }
                }
                Typeface typeface = buildTypeface(context, cancellationSignal, fonts);
                if (typeface == null) {
                    callerThreadHandler.post(new -$$Lambda$FontsContract$rqmVfWYeZ5NL5MtBx5LOdhNAOP4(callback));
                    return;
                }
                sTypefaceCache.put(request.getIdentifier(), typeface);
                callerThreadHandler.post(new -$$Lambda$FontsContract$gJeQYFM3pOm-NcWmWnWDAEk3vlM(callback, typeface));
            }
        } catch (NameNotFoundException e) {
            callerThreadHandler.post(new -$$Lambda$FontsContract$bLFahJqnd9gkPbDqB-OCiChzm_E(callback));
        }
    }

    public static FontFamilyResult fetchFonts(Context context, CancellationSignal cancellationSignal, FontRequest request) throws NameNotFoundException {
        if (context.isRestricted()) {
            return new FontFamilyResult(3, null);
        }
        ProviderInfo providerInfo = getProvider(context.getPackageManager(), request);
        if (providerInfo == null) {
            return new FontFamilyResult(1, null);
        }
        try {
            return new FontFamilyResult(0, getFontFromProvider(context, request, providerInfo.authority, cancellationSignal));
        } catch (IllegalArgumentException e) {
            return new FontFamilyResult(2, null);
        }
    }

    public static Typeface buildTypeface(Context context, CancellationSignal cancellationSignal, FontInfo[] fonts) {
        if (context.isRestricted()) {
            return null;
        }
        Map<Uri, ByteBuffer> uriBuffer = prepareFontData(context, fonts, cancellationSignal);
        if (uriBuffer.isEmpty()) {
            return null;
        }
        Builder familyBuilder = null;
        for (FontInfo fontInfo : fonts) {
            ByteBuffer buffer = (ByteBuffer) uriBuffer.get(fontInfo.getUri());
            if (buffer != null) {
                try {
                    Font font = new Font.Builder(buffer).setWeight(fontInfo.getWeight()).setSlant(fontInfo.isItalic() ? 1 : 0).setTtcIndex(fontInfo.getTtcIndex()).setFontVariationSettings(fontInfo.getAxes()).build();
                    if (familyBuilder == null) {
                        familyBuilder = new Builder(font);
                    } else {
                        familyBuilder.addFont(font);
                    }
                } catch (IllegalArgumentException e) {
                    return null;
                } catch (IOException e2) {
                }
            }
        }
        if (familyBuilder == null) {
            return null;
        }
        FontFamily family = familyBuilder.build();
        FontStyle normal = new FontStyle(400, 0);
        Font bestFont = family.getFont(0);
        int bestScore = normal.getMatchScore(bestFont.getStyle());
        for (int i = 1; i < family.getSize(); i++) {
            Font candidate = family.getFont(i);
            int score = normal.getMatchScore(candidate.getStyle());
            if (score < bestScore) {
                bestFont = candidate;
                bestScore = score;
            }
        }
        return new CustomFallbackBuilder(family).setStyle(bestFont.getStyle()).build();
    }

    /* JADX WARNING: Removed duplicated region for block: B:44:0x0072 A:{SYNTHETIC, Splitter:B:44:0x0072} */
    private static java.util.Map<android.net.Uri, java.nio.ByteBuffer> prepareFontData(android.content.Context r19, android.provider.FontsContract.FontInfo[] r20, android.os.CancellationSignal r21) {
        /*
        r1 = r20;
        r0 = new java.util.HashMap;
        r0.<init>();
        r2 = r0;
        r3 = r19.getContentResolver();
        r4 = r1.length;
        r0 = 0;
        r5 = r0;
    L_0x000f:
        if (r5 >= r4) goto L_0x0081;
    L_0x0011:
        r6 = r1[r5];
        r0 = r6.getResultCode();
        if (r0 == 0) goto L_0x001d;
    L_0x0019:
        r9 = r21;
        goto L_0x007e;
    L_0x001d:
        r7 = r6.getUri();
        r0 = r2.containsKey(r7);
        if (r0 == 0) goto L_0x002a;
    L_0x0027:
        r9 = r21;
        goto L_0x007e;
    L_0x002a:
        r8 = 0;
        r0 = "r";
        r9 = r21;
        r0 = r3.openFileDescriptor(r7, r0, r9);	 Catch:{ IOException -> 0x0076 }
        r10 = r0;
        r11 = 0;
        if (r10 == 0) goto L_0x0070;
    L_0x0039:
        r0 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x006f, all -> 0x0062 }
        r12 = r10.getFileDescriptor();	 Catch:{ IOException -> 0x006f, all -> 0x0062 }
        r0.<init>(r12);	 Catch:{ IOException -> 0x006f, all -> 0x0062 }
        r12 = r0;
        r13 = r12.getChannel();	 Catch:{ all -> 0x0059 }
        r17 = r13.size();	 Catch:{ all -> 0x0059 }
        r14 = java.nio.channels.FileChannel.MapMode.READ_ONLY;	 Catch:{ all -> 0x0059 }
        r15 = 0;
        r0 = r13.map(r14, r15, r17);	 Catch:{ all -> 0x0059 }
        r8 = r0;
        $closeResource(r11, r12);	 Catch:{ IOException -> 0x006f, all -> 0x0062 }
        goto L_0x0070;
    L_0x0059:
        r0 = move-exception;
        r13 = r0;
        throw r13;	 Catch:{ all -> 0x005c }
    L_0x005c:
        r0 = move-exception;
        r14 = r0;
        $closeResource(r13, r12);	 Catch:{ IOException -> 0x006f, all -> 0x0062 }
        throw r14;	 Catch:{ IOException -> 0x006f, all -> 0x0062 }
    L_0x0062:
        r0 = move-exception;
        r11 = r8;
        r8 = r0;
        throw r8;	 Catch:{ all -> 0x0066 }
    L_0x0066:
        r0 = move-exception;
        r12 = r0;
        $closeResource(r8, r10);	 Catch:{ IOException -> 0x006c }
        throw r12;	 Catch:{ IOException -> 0x006c }
    L_0x006c:
        r0 = move-exception;
        r8 = r11;
        goto L_0x007b;
    L_0x006f:
        r0 = move-exception;
    L_0x0070:
        if (r10 == 0) goto L_0x0075;
    L_0x0072:
        $closeResource(r11, r10);	 Catch:{ IOException -> 0x0076 }
    L_0x0075:
        goto L_0x007b;
    L_0x0076:
        r0 = move-exception;
        goto L_0x007b;
    L_0x0078:
        r0 = move-exception;
        r9 = r21;
    L_0x007b:
        r2.put(r7, r8);
    L_0x007e:
        r5 = r5 + 1;
        goto L_0x000f;
    L_0x0081:
        r9 = r21;
        r0 = java.util.Collections.unmodifiableMap(r2);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.provider.FontsContract.prepareFontData(android.content.Context, android.provider.FontsContract$FontInfo[], android.os.CancellationSignal):java.util.Map");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
                return;
            } catch (Throwable th) {
                x0.addSuppressed(th);
                return;
            }
        }
        x1.close();
    }

    @VisibleForTesting
    public static ProviderInfo getProvider(PackageManager packageManager, FontRequest request) throws NameNotFoundException {
        String providerAuthority = request.getProviderAuthority();
        ProviderInfo info = packageManager.resolveContentProvider(providerAuthority, null);
        StringBuilder stringBuilder;
        if (info == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("No package found for authority: ");
            stringBuilder.append(providerAuthority);
            throw new NameNotFoundException(stringBuilder.toString());
        } else if (!info.packageName.equals(request.getProviderPackage())) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Found content provider ");
            stringBuilder.append(providerAuthority);
            stringBuilder.append(", but package was not ");
            stringBuilder.append(request.getProviderPackage());
            throw new NameNotFoundException(stringBuilder.toString());
        } else if (info.applicationInfo.isSystemApp()) {
            return info;
        } else {
            List<byte[]> signatures = convertToByteArrayList(packageManager.getPackageInfo(info.packageName, 64).signatures);
            Collections.sort(signatures, sByteArrayComparator);
            List<List<byte[]>> requestCertificatesList = request.getCertificates();
            for (int i = 0; i < requestCertificatesList.size(); i++) {
                List<byte[]> requestSignatures = new ArrayList((Collection) requestCertificatesList.get(i));
                Collections.sort(requestSignatures, sByteArrayComparator);
                if (equalsByteArrayList(signatures, requestSignatures)) {
                    return info;
                }
            }
            return null;
        }
    }

    static /* synthetic */ int lambda$static$13(byte[] l, byte[] r) {
        if (l.length != r.length) {
            return l.length - r.length;
        }
        for (int i = 0; i < l.length; i++) {
            if (l[i] != r[i]) {
                return l[i] - r[i];
            }
        }
        return 0;
    }

    private static boolean equalsByteArrayList(List<byte[]> signatures, List<byte[]> requestSignatures) {
        if (signatures.size() != requestSignatures.size()) {
            return false;
        }
        for (int i = 0; i < signatures.size(); i++) {
            if (!Arrays.equals((byte[]) signatures.get(i), (byte[]) requestSignatures.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static List<byte[]> convertToByteArrayList(Signature[] signatures) {
        List<byte[]> shas = new ArrayList();
        for (Signature toByteArray : signatures) {
            shas.add(toByteArray.toByteArray());
        }
        return shas;
    }

    @VisibleForTesting
    public static FontInfo[] getFontFromProvider(Context context, FontRequest request, String authority, CancellationSignal cancellationSignal) {
        Throwable th;
        String str = authority;
        ArrayList<FontInfo> result = new ArrayList();
        String str2 = "content";
        Uri uri = new Uri.Builder().scheme(str2).authority(str).build();
        Uri fileBaseUri = new Uri.Builder().scheme(str2).authority(str).appendPath(ContentResolver.SCHEME_FILE).build();
        boolean z = true;
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"_id", "file_id", Columns.TTC_INDEX, Columns.VARIATION_SETTINGS, Columns.WEIGHT, Columns.ITALIC, Columns.RESULT_CODE}, "query = ?", new String[]{request.getQuery()}, null, cancellationSignal);
        if (cursor != null) {
            try {
                if (cursor.getCount() > 0) {
                    int resultCodeColumnIndex = cursor.getColumnIndex(Columns.RESULT_CODE);
                    result = new ArrayList();
                    int idColumnIndex = cursor.getColumnIndexOrThrow("_id");
                    int fileIdColumnIndex = cursor.getColumnIndex("file_id");
                    int ttcIndexColumnIndex = cursor.getColumnIndex(Columns.TTC_INDEX);
                    int vsColumnIndex = cursor.getColumnIndex(Columns.VARIATION_SETTINGS);
                    int weightColumnIndex = cursor.getColumnIndex(Columns.WEIGHT);
                    int italicColumnIndex = cursor.getColumnIndex(Columns.ITALIC);
                    while (cursor.moveToNext()) {
                        int resultCodeColumnIndex2;
                        Uri fileUri;
                        boolean italic;
                        int resultCode = resultCodeColumnIndex != -1 ? cursor.getInt(resultCodeColumnIndex) : 0;
                        int ttcIndex = ttcIndexColumnIndex != -1 ? cursor.getInt(ttcIndexColumnIndex) : 0;
                        String variationSettings = vsColumnIndex != -1 ? cursor.getString(vsColumnIndex) : null;
                        if (fileIdColumnIndex == -1) {
                            resultCodeColumnIndex2 = resultCodeColumnIndex;
                            fileUri = ContentUris.withAppendedId(uri, cursor.getLong(idColumnIndex));
                        } else {
                            resultCodeColumnIndex2 = resultCodeColumnIndex;
                            fileUri = ContentUris.withAppendedId(fileBaseUri, cursor.getLong(fileIdColumnIndex));
                        }
                        if (weightColumnIndex == -1 || italicColumnIndex == -1) {
                            resultCodeColumnIndex = 400;
                            italic = false;
                        } else {
                            resultCodeColumnIndex = cursor.getInt(weightColumnIndex);
                            italic = cursor.getInt(italicColumnIndex) == z ? z : false;
                        }
                        result.add(new FontInfo(fileUri, ttcIndex, FontVariationAxis.fromFontVariationSettings(variationSettings), resultCodeColumnIndex, italic, resultCode));
                        resultCodeColumnIndex = resultCodeColumnIndex2;
                        z = true;
                    }
                }
            } catch (Throwable th2) {
                Throwable th3 = th2;
                $closeResource(th, cursor);
            }
        }
        if (cursor != null) {
            $closeResource(null, cursor);
        }
        return (FontInfo[]) result.toArray(new FontInfo[0]);
    }
}
