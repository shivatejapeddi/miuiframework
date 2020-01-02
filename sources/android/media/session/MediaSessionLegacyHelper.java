package android.media.session;

import android.annotation.UnsupportedAppUsage;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.Rating;
import android.media.session.MediaSession.Callback;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;

public class MediaSessionLegacyHelper {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "MediaSessionHelper";
    private static MediaSessionLegacyHelper sInstance;
    private static final Object sLock = new Object();
    private Context mContext;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private MediaSessionManager mSessionManager;
    private ArrayMap<PendingIntent, SessionHolder> mSessions = new ArrayMap();

    private static final class MediaButtonListener extends Callback {
        private final Context mContext;
        private final PendingIntent mPendingIntent;

        public MediaButtonListener(PendingIntent pi, Context context) {
            this.mPendingIntent = pi;
            this.mContext = context;
        }

        public boolean onMediaButtonEvent(Intent mediaButtonIntent) {
            MediaSessionLegacyHelper.sendKeyEvent(this.mPendingIntent, this.mContext, mediaButtonIntent);
            return true;
        }

        public void onPlay() {
            sendKeyEvent(126);
        }

        public void onPause() {
            sendKeyEvent(127);
        }

        public void onSkipToNext() {
            sendKeyEvent(87);
        }

        public void onSkipToPrevious() {
            sendKeyEvent(88);
        }

        public void onFastForward() {
            sendKeyEvent(90);
        }

        public void onRewind() {
            sendKeyEvent(89);
        }

        public void onStop() {
            sendKeyEvent(86);
        }

        private void sendKeyEvent(int keyCode) {
            Parcelable ke = new KeyEvent(0, keyCode);
            Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            intent.addFlags(268435456);
            String str = Intent.EXTRA_KEY_EVENT;
            intent.putExtra(str, ke);
            MediaSessionLegacyHelper.sendKeyEvent(this.mPendingIntent, this.mContext, intent);
            intent.putExtra(str, (Parcelable) new KeyEvent(1, keyCode));
            MediaSessionLegacyHelper.sendKeyEvent(this.mPendingIntent, this.mContext, intent);
            if (MediaSessionLegacyHelper.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Sent ");
                stringBuilder.append(keyCode);
                stringBuilder.append(" to pending intent ");
                stringBuilder.append(this.mPendingIntent);
                Log.d(MediaSessionLegacyHelper.TAG, stringBuilder.toString());
            }
        }
    }

    private class SessionHolder {
        public SessionCallback mCb;
        public int mFlags;
        public MediaButtonListener mMediaButtonListener;
        public final PendingIntent mPi;
        public Callback mRccListener;
        public final MediaSession mSession;

        private class SessionCallback extends Callback {
            private SessionCallback() {
            }

            public boolean onMediaButtonEvent(Intent mediaButtonIntent) {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onMediaButtonEvent(mediaButtonIntent);
                }
                return true;
            }

            public void onPlay() {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onPlay();
                }
            }

            public void onPause() {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onPause();
                }
            }

            public void onSkipToNext() {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onSkipToNext();
                }
            }

            public void onSkipToPrevious() {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onSkipToPrevious();
                }
            }

            public void onFastForward() {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onFastForward();
                }
            }

            public void onRewind() {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onRewind();
                }
            }

            public void onStop() {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onStop();
                }
            }

            public void onSeekTo(long pos) {
                if (SessionHolder.this.mRccListener != null) {
                    SessionHolder.this.mRccListener.onSeekTo(pos);
                }
            }

            public void onSetRating(Rating rating) {
                if (SessionHolder.this.mRccListener != null) {
                    SessionHolder.this.mRccListener.onSetRating(rating);
                }
            }
        }

        public SessionHolder(MediaSession session, PendingIntent pi) {
            this.mSession = session;
            this.mPi = pi;
        }

        public void update() {
            if (this.mMediaButtonListener == null && this.mRccListener == null) {
                this.mSession.setCallback(null);
                this.mSession.release();
                this.mCb = null;
                MediaSessionLegacyHelper.this.mSessions.remove(this.mPi);
            } else if (this.mCb == null) {
                this.mCb = new SessionCallback();
                this.mSession.setCallback(this.mCb, new Handler(Looper.getMainLooper()));
            }
        }
    }

    private MediaSessionLegacyHelper(Context context) {
        this.mContext = context;
        this.mSessionManager = (MediaSessionManager) context.getSystemService(Context.MEDIA_SESSION_SERVICE);
    }

    @UnsupportedAppUsage
    public static MediaSessionLegacyHelper getHelper(Context context) {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new MediaSessionLegacyHelper(context.getApplicationContext());
            }
        }
        return sInstance;
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00b3  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00f0  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0105  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x011b  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0132  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0149  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0161  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0177  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x018d  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x01a4  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x01bb  */
    public static android.os.Bundle getOldMetadata(android.media.MediaMetadata r7, int r8, int r9) {
        /*
        r0 = 1;
        r1 = 0;
        r2 = -1;
        if (r8 == r2) goto L_0x0009;
    L_0x0005:
        if (r9 == r2) goto L_0x0009;
    L_0x0007:
        r2 = r0;
        goto L_0x000a;
    L_0x0009:
        r2 = r1;
    L_0x000a:
        r3 = new android.os.Bundle;
        r3.<init>();
        r4 = "android.media.metadata.ALBUM";
        r5 = r7.containsKey(r4);
        if (r5 == 0) goto L_0x0022;
    L_0x0017:
        r0 = java.lang.String.valueOf(r0);
        r4 = r7.getString(r4);
        r3.putString(r0, r4);
    L_0x0022:
        r0 = 100;
        if (r2 == 0) goto L_0x003e;
    L_0x0026:
        r4 = "android.media.metadata.ART";
        r5 = r7.containsKey(r4);
        if (r5 == 0) goto L_0x003e;
    L_0x002e:
        r4 = r7.getBitmap(r4);
        r0 = java.lang.String.valueOf(r0);
        r5 = scaleBitmapIfTooBig(r4, r8, r9);
        r3.putParcelable(r0, r5);
        goto L_0x0058;
    L_0x003e:
        if (r2 == 0) goto L_0x0058;
    L_0x0040:
        r4 = "android.media.metadata.ALBUM_ART";
        r5 = r7.containsKey(r4);
        if (r5 == 0) goto L_0x0058;
    L_0x0048:
        r4 = r7.getBitmap(r4);
        r0 = java.lang.String.valueOf(r0);
        r5 = scaleBitmapIfTooBig(r4, r8, r9);
        r3.putParcelable(r0, r5);
        goto L_0x0059;
    L_0x0059:
        r0 = "android.media.metadata.ALBUM_ARTIST";
        r4 = r7.containsKey(r0);
        if (r4 == 0) goto L_0x006e;
    L_0x0061:
        r4 = 13;
        r4 = java.lang.String.valueOf(r4);
        r0 = r7.getString(r0);
        r3.putString(r4, r0);
    L_0x006e:
        r0 = "android.media.metadata.ARTIST";
        r4 = r7.containsKey(r0);
        if (r4 == 0) goto L_0x0082;
    L_0x0076:
        r4 = 2;
        r4 = java.lang.String.valueOf(r4);
        r0 = r7.getString(r0);
        r3.putString(r4, r0);
    L_0x0082:
        r0 = "android.media.metadata.AUTHOR";
        r4 = r7.containsKey(r0);
        if (r4 == 0) goto L_0x0096;
    L_0x008a:
        r4 = 3;
        r4 = java.lang.String.valueOf(r4);
        r0 = r7.getString(r0);
        r3.putString(r4, r0);
    L_0x0096:
        r0 = "android.media.metadata.COMPILATION";
        r4 = r7.containsKey(r0);
        if (r4 == 0) goto L_0x00ab;
    L_0x009e:
        r4 = 15;
        r4 = java.lang.String.valueOf(r4);
        r0 = r7.getString(r0);
        r3.putString(r4, r0);
    L_0x00ab:
        r0 = "android.media.metadata.COMPOSER";
        r4 = r7.containsKey(r0);
        if (r4 == 0) goto L_0x00bf;
    L_0x00b3:
        r4 = 4;
        r4 = java.lang.String.valueOf(r4);
        r0 = r7.getString(r0);
        r3.putString(r4, r0);
    L_0x00bf:
        r0 = "android.media.metadata.DATE";
        r4 = r7.containsKey(r0);
        if (r4 == 0) goto L_0x00d3;
    L_0x00c7:
        r4 = 5;
        r4 = java.lang.String.valueOf(r4);
        r0 = r7.getString(r0);
        r3.putString(r4, r0);
    L_0x00d3:
        r0 = "android.media.metadata.DISC_NUMBER";
        r4 = r7.containsKey(r0);
        if (r4 == 0) goto L_0x00e8;
    L_0x00db:
        r4 = 14;
        r4 = java.lang.String.valueOf(r4);
        r5 = r7.getLong(r0);
        r3.putLong(r4, r5);
    L_0x00e8:
        r0 = "android.media.metadata.DURATION";
        r4 = r7.containsKey(r0);
        if (r4 == 0) goto L_0x00fd;
    L_0x00f0:
        r4 = 9;
        r4 = java.lang.String.valueOf(r4);
        r5 = r7.getLong(r0);
        r3.putLong(r4, r5);
    L_0x00fd:
        r0 = "android.media.metadata.GENRE";
        r0 = r7.containsKey(r0);
        if (r0 == 0) goto L_0x0113;
    L_0x0105:
        r0 = 6;
        r0 = java.lang.String.valueOf(r0);
        r4 = "android.media.metadata.GENRE";
        r4 = r7.getString(r4);
        r3.putString(r0, r4);
    L_0x0113:
        r0 = "android.media.metadata.NUM_TRACKS";
        r0 = r7.containsKey(r0);
        if (r0 == 0) goto L_0x012a;
    L_0x011b:
        r0 = 10;
        r0 = java.lang.String.valueOf(r0);
        r4 = "android.media.metadata.NUM_TRACKS";
        r4 = r7.getLong(r4);
        r3.putLong(r0, r4);
    L_0x012a:
        r0 = "android.media.metadata.RATING";
        r0 = r7.containsKey(r0);
        if (r0 == 0) goto L_0x0141;
    L_0x0132:
        r0 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r0 = java.lang.String.valueOf(r0);
        r4 = "android.media.metadata.RATING";
        r4 = r7.getRating(r4);
        r3.putParcelable(r0, r4);
    L_0x0141:
        r0 = "android.media.metadata.USER_RATING";
        r0 = r7.containsKey(r0);
        if (r0 == 0) goto L_0x0159;
    L_0x0149:
        r0 = 268435457; // 0x10000001 float:2.5243552E-29 double:1.326247374E-315;
        r0 = java.lang.String.valueOf(r0);
        r4 = "android.media.metadata.USER_RATING";
        r4 = r7.getRating(r4);
        r3.putParcelable(r0, r4);
    L_0x0159:
        r0 = "android.media.metadata.TITLE";
        r0 = r7.containsKey(r0);
        if (r0 == 0) goto L_0x016f;
    L_0x0161:
        r0 = 7;
        r0 = java.lang.String.valueOf(r0);
        r4 = "android.media.metadata.TITLE";
        r4 = r7.getString(r4);
        r3.putString(r0, r4);
    L_0x016f:
        r0 = "android.media.metadata.TRACK_NUMBER";
        r0 = r7.containsKey(r0);
        if (r0 == 0) goto L_0x0185;
        r0 = java.lang.String.valueOf(r1);
        r1 = "android.media.metadata.TRACK_NUMBER";
        r4 = r7.getLong(r1);
        r3.putLong(r0, r4);
    L_0x0185:
        r0 = "android.media.metadata.WRITER";
        r0 = r7.containsKey(r0);
        if (r0 == 0) goto L_0x019c;
    L_0x018d:
        r0 = 11;
        r0 = java.lang.String.valueOf(r0);
        r1 = "android.media.metadata.WRITER";
        r1 = r7.getString(r1);
        r3.putString(r0, r1);
    L_0x019c:
        r0 = "android.media.metadata.YEAR";
        r0 = r7.containsKey(r0);
        if (r0 == 0) goto L_0x01b3;
    L_0x01a4:
        r0 = 8;
        r0 = java.lang.String.valueOf(r0);
        r1 = "android.media.metadata.YEAR";
        r4 = r7.getLong(r1);
        r3.putLong(r0, r4);
    L_0x01b3:
        r0 = "android.media.metadata.LYRIC";
        r0 = r7.containsKey(r0);
        if (r0 == 0) goto L_0x01ca;
    L_0x01bb:
        r0 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r0 = java.lang.String.valueOf(r0);
        r1 = "android.media.metadata.LYRIC";
        r1 = r7.getString(r1);
        r3.putString(r0, r1);
    L_0x01ca:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.session.MediaSessionLegacyHelper.getOldMetadata(android.media.MediaMetadata, int, int):android.os.Bundle");
    }

    public MediaSession getSession(PendingIntent pi) {
        SessionHolder holder = (SessionHolder) this.mSessions.get(pi);
        return holder == null ? null : holder.mSession;
    }

    public void sendMediaButtonEvent(KeyEvent keyEvent, boolean needWakeLock) {
        String str = TAG;
        if (keyEvent == null) {
            Log.w(str, "Tried to send a null key event. Ignoring.");
            return;
        }
        this.mSessionManager.dispatchMediaKeyEvent(keyEvent, needWakeLock);
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("dispatched media key ");
            stringBuilder.append(keyEvent);
            Log.d(str, stringBuilder.toString());
        }
    }

    public void sendVolumeKeyEvent(KeyEvent keyEvent, int stream, boolean musicOnly) {
        if (keyEvent == null) {
            Log.w(TAG, "Tried to send a null key event. Ignoring.");
        } else {
            this.mSessionManager.dispatchVolumeKeyEvent(keyEvent, stream, musicOnly);
        }
    }

    public void sendAdjustVolumeBy(int suggestedStream, int delta, int flags) {
        this.mSessionManager.dispatchAdjustVolume(suggestedStream, delta, flags);
        if (DEBUG) {
            Log.d(TAG, "dispatched volume adjustment");
        }
    }

    public boolean isGlobalPriorityActive() {
        return this.mSessionManager.isGlobalPriorityActive();
    }

    public void addRccListener(PendingIntent pi, Callback listener) {
        String str = TAG;
        if (pi == null) {
            Log.w(str, "Pending intent was null, can't add rcc listener.");
            return;
        }
        SessionHolder holder = getHolder(pi, true);
        if (holder != null) {
            if (holder.mRccListener == null || holder.mRccListener != listener) {
                holder.mRccListener = listener;
                holder.mFlags |= 2;
                holder.mSession.setFlags(holder.mFlags);
                holder.update();
                if (DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Added rcc listener for ");
                    stringBuilder.append(pi);
                    stringBuilder.append(".");
                    Log.d(str, stringBuilder.toString());
                }
                return;
            }
            if (DEBUG) {
                Log.d(str, "addRccListener listener already added.");
            }
        }
    }

    public void removeRccListener(PendingIntent pi) {
        if (pi != null) {
            SessionHolder holder = getHolder(pi, null);
            if (!(holder == null || holder.mRccListener == null)) {
                holder.mRccListener = null;
                holder.mFlags &= -3;
                holder.mSession.setFlags(holder.mFlags);
                holder.update();
                if (DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Removed rcc listener for ");
                    stringBuilder.append(pi);
                    stringBuilder.append(".");
                    Log.d(TAG, stringBuilder.toString());
                }
            }
        }
    }

    public void addMediaButtonListener(PendingIntent pi, ComponentName mbrComponent, Context context) {
        String str = TAG;
        if (pi == null) {
            Log.w(str, "Pending intent was null, can't addMediaButtonListener.");
            return;
        }
        SessionHolder holder = getHolder(pi, true);
        if (holder != null) {
            if (holder.mMediaButtonListener != null && DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("addMediaButtonListener already added ");
                stringBuilder.append(pi);
                Log.d(str, stringBuilder.toString());
            }
            holder.mMediaButtonListener = new MediaButtonListener(pi, context);
            holder.mFlags = 1 | holder.mFlags;
            holder.mSession.setFlags(holder.mFlags);
            holder.mSession.setMediaButtonReceiver(pi);
            holder.update();
            if (DEBUG) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("addMediaButtonListener added ");
                stringBuilder2.append(pi);
                Log.d(str, stringBuilder2.toString());
            }
        }
    }

    public void removeMediaButtonListener(PendingIntent pi) {
        if (pi != null) {
            SessionHolder holder = getHolder(pi, null);
            if (!(holder == null || holder.mMediaButtonListener == null)) {
                holder.mFlags &= -2;
                holder.mSession.setFlags(holder.mFlags);
                holder.mMediaButtonListener = null;
                holder.update();
                if (DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("removeMediaButtonListener removed ");
                    stringBuilder.append(pi);
                    Log.d(TAG, stringBuilder.toString());
                }
            }
        }
    }

    private static Bitmap scaleBitmapIfTooBig(Bitmap bitmap, int maxWidth, int maxHeight) {
        Bitmap bitmap2 = bitmap;
        int i = maxWidth;
        int i2 = maxHeight;
        if (bitmap2 == null) {
            return bitmap2;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= i && height <= i2) {
            return bitmap2;
        }
        float scale = Math.min(((float) i) / ((float) width), ((float) i2) / ((float) height));
        int newWidth = Math.round(((float) width) * scale);
        int newHeight = Math.round(((float) height) * scale);
        Config newConfig = bitmap.getConfig();
        if (newConfig == null) {
            newConfig = Config.ARGB_8888;
        }
        Bitmap outBitmap = Bitmap.createBitmap(newWidth, newHeight, newConfig);
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap2, null, new RectF(0.0f, 0.0f, (float) outBitmap.getWidth(), (float) outBitmap.getHeight()), paint);
        return outBitmap;
    }

    private SessionHolder getHolder(PendingIntent pi, boolean createIfMissing) {
        SessionHolder holder = (SessionHolder) this.mSessions.get(pi);
        if (holder != null || !createIfMissing) {
            return holder;
        }
        Context context = this.mContext;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MediaSessionHelper-");
        stringBuilder.append(pi.getCreatorPackage());
        MediaSession session = new MediaSession(context, stringBuilder.toString());
        session.setActive(true);
        holder = new SessionHolder(session, pi);
        this.mSessions.put(pi, holder);
        return holder;
    }

    private static void sendKeyEvent(PendingIntent pi, Context context, Intent intent) {
        try {
            pi.send(context, 0, intent);
        } catch (CanceledException e) {
            Log.e(TAG, "Error sending media key down event:", e);
        }
    }
}
