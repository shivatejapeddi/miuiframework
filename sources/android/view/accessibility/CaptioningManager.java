package android.view.accessibility;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.telecom.Logging.Session;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class CaptioningManager {
    private static final int DEFAULT_ENABLED = 0;
    private static final float DEFAULT_FONT_SCALE = 1.0f;
    private static final int DEFAULT_PRESET = 0;
    private final ContentObserver mContentObserver;
    private final ContentResolver mContentResolver;
    private final ArrayList<CaptioningChangeListener> mListeners = new ArrayList();
    private final Runnable mStyleChangedRunnable = new Runnable() {
        public void run() {
            CaptioningManager.this.notifyUserStyleChanged();
        }
    };

    public static final class CaptionStyle {
        private static final CaptionStyle BLACK_ON_WHITE = new CaptionStyle(-16777216, -1, 0, -16777216, 255, null);
        private static final int COLOR_NONE_OPAQUE = 255;
        public static final int COLOR_UNSPECIFIED = 16777215;
        public static final CaptionStyle DEFAULT;
        private static final CaptionStyle DEFAULT_CUSTOM;
        public static final int EDGE_TYPE_DEPRESSED = 4;
        public static final int EDGE_TYPE_DROP_SHADOW = 2;
        public static final int EDGE_TYPE_NONE = 0;
        public static final int EDGE_TYPE_OUTLINE = 1;
        public static final int EDGE_TYPE_RAISED = 3;
        public static final int EDGE_TYPE_UNSPECIFIED = -1;
        @UnsupportedAppUsage
        public static final CaptionStyle[] PRESETS;
        public static final int PRESET_CUSTOM = -1;
        private static final CaptionStyle UNSPECIFIED = new CaptionStyle(16777215, 16777215, -1, 16777215, 16777215, null);
        private static final CaptionStyle WHITE_ON_BLACK = new CaptionStyle(-1, -16777216, 0, -16777216, 255, null);
        private static final CaptionStyle YELLOW_ON_BLACK = new CaptionStyle(-256, -16777216, 0, -16777216, 255, null);
        private static final CaptionStyle YELLOW_ON_BLUE = new CaptionStyle(-256, -16776961, 0, -16777216, 255, null);
        public final int backgroundColor;
        public final int edgeColor;
        public final int edgeType;
        public final int foregroundColor;
        private final boolean mHasBackgroundColor;
        private final boolean mHasEdgeColor;
        private final boolean mHasEdgeType;
        private final boolean mHasForegroundColor;
        private final boolean mHasWindowColor;
        private Typeface mParsedTypeface;
        public final String mRawTypeface;
        public final int windowColor;

        private CaptionStyle(int foregroundColor, int backgroundColor, int edgeType, int edgeColor, int windowColor, String rawTypeface) {
            this.mHasForegroundColor = hasColor(foregroundColor);
            this.mHasBackgroundColor = hasColor(backgroundColor);
            int i = 0;
            int i2 = -1;
            this.mHasEdgeType = edgeType != -1;
            this.mHasEdgeColor = hasColor(edgeColor);
            this.mHasWindowColor = hasColor(windowColor);
            if (this.mHasForegroundColor) {
                i2 = foregroundColor;
            }
            this.foregroundColor = i2;
            int i3 = -16777216;
            this.backgroundColor = this.mHasBackgroundColor ? backgroundColor : -16777216;
            if (this.mHasEdgeType) {
                i = edgeType;
            }
            this.edgeType = i;
            if (this.mHasEdgeColor) {
                i3 = edgeColor;
            }
            this.edgeColor = i3;
            this.windowColor = this.mHasWindowColor ? windowColor : 255;
            this.mRawTypeface = rawTypeface;
        }

        public static boolean hasColor(int packedColor) {
            return (packedColor >>> 24) != 0 || (16776960 & packedColor) == 0;
        }

        public CaptionStyle applyStyle(CaptionStyle overlay) {
            int newForegroundColor = overlay.hasForegroundColor() ? overlay.foregroundColor : this.foregroundColor;
            int newBackgroundColor = overlay.hasBackgroundColor() ? overlay.backgroundColor : this.backgroundColor;
            int newEdgeType = overlay.hasEdgeType() ? overlay.edgeType : this.edgeType;
            int newEdgeColor = overlay.hasEdgeColor() ? overlay.edgeColor : this.edgeColor;
            int newWindowColor = overlay.hasWindowColor() ? overlay.windowColor : this.windowColor;
            String str = overlay.mRawTypeface;
            if (str == null) {
                str = this.mRawTypeface;
            }
            return new CaptionStyle(newForegroundColor, newBackgroundColor, newEdgeType, newEdgeColor, newWindowColor, str);
        }

        public boolean hasBackgroundColor() {
            return this.mHasBackgroundColor;
        }

        public boolean hasForegroundColor() {
            return this.mHasForegroundColor;
        }

        public boolean hasEdgeType() {
            return this.mHasEdgeType;
        }

        public boolean hasEdgeColor() {
            return this.mHasEdgeColor;
        }

        public boolean hasWindowColor() {
            return this.mHasWindowColor;
        }

        public Typeface getTypeface() {
            if (this.mParsedTypeface == null && !TextUtils.isEmpty(this.mRawTypeface)) {
                this.mParsedTypeface = Typeface.create(this.mRawTypeface, 0);
            }
            return this.mParsedTypeface;
        }

        public static CaptionStyle getCustomStyle(ContentResolver cr) {
            String rawTypeface;
            CaptionStyle defStyle = DEFAULT_CUSTOM;
            int foregroundColor = Secure.getInt(cr, Secure.ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR, defStyle.foregroundColor);
            int backgroundColor = Secure.getInt(cr, Secure.ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR, defStyle.backgroundColor);
            int edgeType = Secure.getInt(cr, Secure.ACCESSIBILITY_CAPTIONING_EDGE_TYPE, defStyle.edgeType);
            int edgeColor = Secure.getInt(cr, Secure.ACCESSIBILITY_CAPTIONING_EDGE_COLOR, defStyle.edgeColor);
            int windowColor = Secure.getInt(cr, Secure.ACCESSIBILITY_CAPTIONING_WINDOW_COLOR, defStyle.windowColor);
            String rawTypeface2 = Secure.getString(cr, Secure.ACCESSIBILITY_CAPTIONING_TYPEFACE);
            if (rawTypeface2 == null) {
                rawTypeface = defStyle.mRawTypeface;
            } else {
                rawTypeface = rawTypeface2;
            }
            return new CaptionStyle(foregroundColor, backgroundColor, edgeType, edgeColor, windowColor, rawTypeface);
        }

        static {
            r0 = new CaptionStyle[5];
            CaptionStyle captionStyle = WHITE_ON_BLACK;
            r0[0] = captionStyle;
            r0[1] = BLACK_ON_WHITE;
            r0[2] = YELLOW_ON_BLACK;
            r0[3] = YELLOW_ON_BLUE;
            r0[4] = UNSPECIFIED;
            PRESETS = r0;
            DEFAULT_CUSTOM = captionStyle;
            DEFAULT = captionStyle;
        }
    }

    public static abstract class CaptioningChangeListener {
        public void onEnabledChanged(boolean enabled) {
        }

        public void onUserStyleChanged(CaptionStyle userStyle) {
        }

        public void onLocaleChanged(Locale locale) {
        }

        public void onFontScaleChanged(float fontScale) {
        }
    }

    private class MyContentObserver extends ContentObserver {
        private final Handler mHandler;

        public MyContentObserver(Handler handler) {
            super(handler);
            this.mHandler = handler;
        }

        public void onChange(boolean selfChange, Uri uri) {
            String uriPath = uri.getPath();
            String name = uriPath.substring(uriPath.lastIndexOf(47) + 1);
            if (Secure.ACCESSIBILITY_CAPTIONING_ENABLED.equals(name)) {
                CaptioningManager.this.notifyEnabledChanged();
            } else if (Secure.ACCESSIBILITY_CAPTIONING_LOCALE.equals(name)) {
                CaptioningManager.this.notifyLocaleChanged();
            } else if (Secure.ACCESSIBILITY_CAPTIONING_FONT_SCALE.equals(name)) {
                CaptioningManager.this.notifyFontScaleChanged();
            } else {
                this.mHandler.removeCallbacks(CaptioningManager.this.mStyleChangedRunnable);
                this.mHandler.post(CaptioningManager.this.mStyleChangedRunnable);
            }
        }
    }

    public CaptioningManager(Context context) {
        this.mContentResolver = context.getContentResolver();
        this.mContentObserver = new MyContentObserver(new Handler(context.getMainLooper()));
    }

    public final boolean isEnabled() {
        return Secure.getInt(this.mContentResolver, Secure.ACCESSIBILITY_CAPTIONING_ENABLED, 0) == 1;
    }

    public final String getRawLocale() {
        return Secure.getString(this.mContentResolver, Secure.ACCESSIBILITY_CAPTIONING_LOCALE);
    }

    public final Locale getLocale() {
        String rawLocale = getRawLocale();
        if (!TextUtils.isEmpty(rawLocale)) {
            String[] splitLocale = rawLocale.split(Session.SESSION_SEPARATION_CHAR_CHILD);
            int length = splitLocale.length;
            if (length == 1) {
                return new Locale(splitLocale[0]);
            }
            if (length == 2) {
                return new Locale(splitLocale[0], splitLocale[1]);
            }
            if (length == 3) {
                return new Locale(splitLocale[0], splitLocale[1], splitLocale[2]);
            }
        }
        return null;
    }

    public final float getFontScale() {
        return Secure.getFloat(this.mContentResolver, Secure.ACCESSIBILITY_CAPTIONING_FONT_SCALE, 1.0f);
    }

    public int getRawUserStyle() {
        return Secure.getInt(this.mContentResolver, Secure.ACCESSIBILITY_CAPTIONING_PRESET, 0);
    }

    public CaptionStyle getUserStyle() {
        int preset = getRawUserStyle();
        if (preset == -1) {
            return CaptionStyle.getCustomStyle(this.mContentResolver);
        }
        return CaptionStyle.PRESETS[preset];
    }

    public void addCaptioningChangeListener(CaptioningChangeListener listener) {
        synchronized (this.mListeners) {
            if (this.mListeners.isEmpty()) {
                registerObserver(Secure.ACCESSIBILITY_CAPTIONING_ENABLED);
                registerObserver(Secure.ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR);
                registerObserver(Secure.ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR);
                registerObserver(Secure.ACCESSIBILITY_CAPTIONING_WINDOW_COLOR);
                registerObserver(Secure.ACCESSIBILITY_CAPTIONING_EDGE_TYPE);
                registerObserver(Secure.ACCESSIBILITY_CAPTIONING_EDGE_COLOR);
                registerObserver(Secure.ACCESSIBILITY_CAPTIONING_TYPEFACE);
                registerObserver(Secure.ACCESSIBILITY_CAPTIONING_FONT_SCALE);
                registerObserver(Secure.ACCESSIBILITY_CAPTIONING_LOCALE);
                registerObserver(Secure.ACCESSIBILITY_CAPTIONING_PRESET);
            }
            this.mListeners.add(listener);
        }
    }

    private void registerObserver(String key) {
        this.mContentResolver.registerContentObserver(Secure.getUriFor(key), false, this.mContentObserver);
    }

    public void removeCaptioningChangeListener(CaptioningChangeListener listener) {
        synchronized (this.mListeners) {
            this.mListeners.remove(listener);
            if (this.mListeners.isEmpty()) {
                this.mContentResolver.unregisterContentObserver(this.mContentObserver);
            }
        }
    }

    private void notifyEnabledChanged() {
        boolean enabled = isEnabled();
        synchronized (this.mListeners) {
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((CaptioningChangeListener) it.next()).onEnabledChanged(enabled);
            }
        }
    }

    private void notifyUserStyleChanged() {
        CaptionStyle userStyle = getUserStyle();
        synchronized (this.mListeners) {
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((CaptioningChangeListener) it.next()).onUserStyleChanged(userStyle);
            }
        }
    }

    private void notifyLocaleChanged() {
        Locale locale = getLocale();
        synchronized (this.mListeners) {
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((CaptioningChangeListener) it.next()).onLocaleChanged(locale);
            }
        }
    }

    private void notifyFontScaleChanged() {
        float fontScale = getFontScale();
        synchronized (this.mListeners) {
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((CaptioningChangeListener) it.next()).onFontScaleChanged(fontScale);
            }
        }
    }
}
