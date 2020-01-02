package android.media;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.media.SubtitleTrack.RenderingWidget;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.view.accessibility.CaptioningManager;
import android.view.accessibility.CaptioningManager.CaptioningChangeListener;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

public class SubtitleController {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int WHAT_HIDE = 2;
    private static final int WHAT_SELECT_DEFAULT_TRACK = 4;
    private static final int WHAT_SELECT_TRACK = 3;
    private static final int WHAT_SHOW = 1;
    private Anchor mAnchor;
    private final Callback mCallback = new Callback() {
        public boolean handleMessage(Message msg) {
            int i = msg.what;
            if (i == 1) {
                SubtitleController.this.doShow();
                return true;
            } else if (i == 2) {
                SubtitleController.this.doHide();
                return true;
            } else if (i == 3) {
                SubtitleController.this.doSelectTrack((SubtitleTrack) msg.obj);
                return true;
            } else if (i != 4) {
                return false;
            } else {
                SubtitleController.this.doSelectDefaultTrack();
                return true;
            }
        }
    };
    private CaptioningChangeListener mCaptioningChangeListener = new CaptioningChangeListener() {
        public void onEnabledChanged(boolean enabled) {
            SubtitleController.this.selectDefaultTrack();
        }

        public void onLocaleChanged(Locale locale) {
            SubtitleController.this.selectDefaultTrack();
        }
    };
    private CaptioningManager mCaptioningManager;
    @UnsupportedAppUsage
    private Handler mHandler;
    private Listener mListener;
    private Vector<Renderer> mRenderers;
    private SubtitleTrack mSelectedTrack;
    private boolean mShowing;
    private MediaTimeProvider mTimeProvider;
    private boolean mTrackIsExplicit = false;
    private Vector<SubtitleTrack> mTracks;
    private boolean mVisibilityIsExplicit = false;

    public static abstract class Renderer {
        public abstract SubtitleTrack createTrack(MediaFormat mediaFormat);

        public abstract boolean supports(MediaFormat mediaFormat);
    }

    public interface Anchor {
        Looper getSubtitleLooper();

        void setSubtitleWidget(RenderingWidget renderingWidget);
    }

    public interface Listener {
        void onSubtitleTrackSelected(SubtitleTrack subtitleTrack);
    }

    @UnsupportedAppUsage
    public SubtitleController(Context context, MediaTimeProvider timeProvider, Listener listener) {
        this.mTimeProvider = timeProvider;
        this.mListener = listener;
        this.mRenderers = new Vector();
        this.mShowing = false;
        this.mTracks = new Vector();
        this.mCaptioningManager = (CaptioningManager) context.getSystemService(Context.CAPTIONING_SERVICE);
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        this.mCaptioningManager.removeCaptioningChangeListener(this.mCaptioningChangeListener);
        super.finalize();
    }

    public SubtitleTrack[] getTracks() {
        SubtitleTrack[] tracks;
        synchronized (this.mTracks) {
            tracks = new SubtitleTrack[this.mTracks.size()];
            this.mTracks.toArray(tracks);
        }
        return tracks;
    }

    public SubtitleTrack getSelectedTrack() {
        return this.mSelectedTrack;
    }

    private RenderingWidget getRenderingWidget() {
        SubtitleTrack subtitleTrack = this.mSelectedTrack;
        if (subtitleTrack == null) {
            return null;
        }
        return subtitleTrack.getRenderingWidget();
    }

    public boolean selectTrack(SubtitleTrack track) {
        if (track != null && !this.mTracks.contains(track)) {
            return false;
        }
        processOnAnchor(this.mHandler.obtainMessage(3, track));
        return true;
    }

    private void doSelectTrack(SubtitleTrack track) {
        this.mTrackIsExplicit = true;
        SubtitleTrack subtitleTrack = this.mSelectedTrack;
        if (subtitleTrack != track) {
            if (subtitleTrack != null) {
                subtitleTrack.hide();
                this.mSelectedTrack.setTimeProvider(null);
            }
            this.mSelectedTrack = track;
            Anchor anchor = this.mAnchor;
            if (anchor != null) {
                anchor.setSubtitleWidget(getRenderingWidget());
            }
            subtitleTrack = this.mSelectedTrack;
            if (subtitleTrack != null) {
                subtitleTrack.setTimeProvider(this.mTimeProvider);
                this.mSelectedTrack.show();
            }
            Listener listener = this.mListener;
            if (listener != null) {
                listener.onSubtitleTrackSelected(track);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x008c  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x009b  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00a4  */
    public android.media.SubtitleTrack getDefaultTrack() {
        /*
        r18 = this;
        r1 = r18;
        r2 = 0;
        r3 = -1;
        r0 = r1.mCaptioningManager;
        r4 = r0.getLocale();
        r0 = r4;
        if (r0 != 0) goto L_0x0013;
    L_0x000d:
        r0 = java.util.Locale.getDefault();
        r5 = r0;
        goto L_0x0014;
    L_0x0013:
        r5 = r0;
    L_0x0014:
        r0 = r1.mCaptioningManager;
        r0 = r0.isEnabled();
        r6 = 1;
        r0 = r0 ^ r6;
        r7 = r0;
        r8 = r1.mTracks;
        monitor-enter(r8);
        r0 = r1.mTracks;	 Catch:{ all -> 0x00c7 }
        r0 = r0.iterator();	 Catch:{ all -> 0x00c7 }
    L_0x0026:
        r9 = r0.hasNext();	 Catch:{ all -> 0x00c7 }
        if (r9 == 0) goto L_0x00c5;
    L_0x002c:
        r9 = r0.next();	 Catch:{ all -> 0x00c7 }
        r9 = (android.media.SubtitleTrack) r9;	 Catch:{ all -> 0x00c7 }
        r10 = r9.getFormat();	 Catch:{ all -> 0x00c7 }
        r11 = "language";
        r11 = r10.getString(r11);	 Catch:{ all -> 0x00c7 }
        r12 = "is-forced-subtitle";
        r13 = 0;
        r12 = r10.getInteger(r12, r13);	 Catch:{ all -> 0x00c7 }
        if (r12 == 0) goto L_0x0049;
    L_0x0047:
        r12 = r6;
        goto L_0x004a;
    L_0x0049:
        r12 = r13;
    L_0x004a:
        r14 = "is-autoselect";
        r14 = r10.getInteger(r14, r6);	 Catch:{ all -> 0x00c7 }
        if (r14 == 0) goto L_0x0055;
    L_0x0053:
        r14 = r6;
        goto L_0x0056;
    L_0x0055:
        r14 = r13;
    L_0x0056:
        r15 = "is-default";
        r15 = r10.getInteger(r15, r13);	 Catch:{ all -> 0x00c7 }
        if (r15 == 0) goto L_0x0061;
    L_0x005f:
        r15 = r6;
        goto L_0x0062;
    L_0x0061:
        r15 = r13;
    L_0x0062:
        if (r5 == 0) goto L_0x0087;
    L_0x0064:
        r6 = r5.getLanguage();	 Catch:{ all -> 0x00c7 }
        r13 = "";
        r6 = r6.equals(r13);	 Catch:{ all -> 0x00c7 }
        if (r6 != 0) goto L_0x0087;
    L_0x0070:
        r6 = r5.getISO3Language();	 Catch:{ all -> 0x00c7 }
        r6 = r6.equals(r11);	 Catch:{ all -> 0x00c7 }
        if (r6 != 0) goto L_0x0087;
    L_0x007a:
        r6 = r5.getLanguage();	 Catch:{ all -> 0x00c7 }
        r6 = r6.equals(r11);	 Catch:{ all -> 0x00c7 }
        if (r6 == 0) goto L_0x0085;
    L_0x0084:
        goto L_0x0087;
    L_0x0085:
        r6 = 0;
        goto L_0x0088;
    L_0x0087:
        r6 = 1;
    L_0x0088:
        if (r12 == 0) goto L_0x008c;
    L_0x008a:
        r13 = 0;
        goto L_0x008e;
    L_0x008c:
        r13 = 8;
    L_0x008e:
        if (r4 != 0) goto L_0x0095;
    L_0x0090:
        if (r15 == 0) goto L_0x0095;
    L_0x0092:
        r17 = 4;
        goto L_0x0097;
    L_0x0095:
        r17 = 0;
    L_0x0097:
        r13 = r13 + r17;
        if (r14 == 0) goto L_0x009e;
    L_0x009b:
        r17 = 0;
        goto L_0x00a0;
    L_0x009e:
        r17 = 2;
    L_0x00a0:
        r13 = r13 + r17;
        if (r6 == 0) goto L_0x00a7;
    L_0x00a4:
        r16 = 1;
        goto L_0x00a9;
    L_0x00a7:
        r16 = 0;
    L_0x00a9:
        r13 = r13 + r16;
        if (r7 == 0) goto L_0x00b2;
    L_0x00ad:
        if (r12 != 0) goto L_0x00b2;
    L_0x00af:
        r6 = 1;
        goto L_0x0026;
    L_0x00b2:
        if (r4 != 0) goto L_0x00b6;
    L_0x00b4:
        if (r15 != 0) goto L_0x00be;
    L_0x00b6:
        if (r6 == 0) goto L_0x00c2;
    L_0x00b8:
        if (r14 != 0) goto L_0x00be;
    L_0x00ba:
        if (r12 != 0) goto L_0x00be;
    L_0x00bc:
        if (r4 == 0) goto L_0x00c2;
    L_0x00be:
        if (r13 <= r3) goto L_0x00c2;
    L_0x00c0:
        r3 = r13;
        r2 = r9;
    L_0x00c2:
        r6 = 1;
        goto L_0x0026;
    L_0x00c5:
        monitor-exit(r8);	 Catch:{ all -> 0x00c7 }
        return r2;
    L_0x00c7:
        r0 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x00c7 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.SubtitleController.getDefaultTrack():android.media.SubtitleTrack");
    }

    public void selectDefaultTrack() {
        processOnAnchor(this.mHandler.obtainMessage(4));
    }

    private void doSelectDefaultTrack() {
        SubtitleTrack subtitleTrack;
        if (this.mTrackIsExplicit) {
            if (!this.mVisibilityIsExplicit) {
                if (!this.mCaptioningManager.isEnabled()) {
                    subtitleTrack = this.mSelectedTrack;
                    if (subtitleTrack == null || subtitleTrack.getFormat().getInteger(MediaFormat.KEY_IS_FORCED_SUBTITLE, 0) == 0) {
                        subtitleTrack = this.mSelectedTrack;
                        if (subtitleTrack != null && subtitleTrack.getTrackType() == 4) {
                            hide();
                        }
                        this.mVisibilityIsExplicit = false;
                    }
                }
                show();
                this.mVisibilityIsExplicit = false;
            }
            return;
        }
        subtitleTrack = getDefaultTrack();
        if (subtitleTrack != null) {
            selectTrack(subtitleTrack);
            this.mTrackIsExplicit = false;
            if (!this.mVisibilityIsExplicit) {
                show();
                this.mVisibilityIsExplicit = false;
            }
        }
    }

    @UnsupportedAppUsage
    public void reset() {
        checkAnchorLooper();
        hide();
        selectTrack(null);
        this.mTracks.clear();
        this.mTrackIsExplicit = false;
        this.mVisibilityIsExplicit = false;
        this.mCaptioningManager.removeCaptioningChangeListener(this.mCaptioningChangeListener);
    }

    public SubtitleTrack addTrack(MediaFormat format) {
        synchronized (this.mRenderers) {
            Iterator it = this.mRenderers.iterator();
            while (it.hasNext()) {
                Renderer renderer = (Renderer) it.next();
                if (renderer.supports(format)) {
                    SubtitleTrack track = renderer.createTrack(format);
                    if (track != null) {
                        synchronized (this.mTracks) {
                            if (this.mTracks.size() == 0) {
                                this.mCaptioningManager.addCaptioningChangeListener(this.mCaptioningChangeListener);
                            }
                            this.mTracks.add(track);
                        }
                        return track;
                    }
                }
            }
            return null;
        }
    }

    @UnsupportedAppUsage
    public void show() {
        processOnAnchor(this.mHandler.obtainMessage(1));
    }

    private void doShow() {
        this.mShowing = true;
        this.mVisibilityIsExplicit = true;
        SubtitleTrack subtitleTrack = this.mSelectedTrack;
        if (subtitleTrack != null) {
            subtitleTrack.show();
        }
    }

    @UnsupportedAppUsage
    public void hide() {
        processOnAnchor(this.mHandler.obtainMessage(2));
    }

    private void doHide() {
        this.mVisibilityIsExplicit = true;
        SubtitleTrack subtitleTrack = this.mSelectedTrack;
        if (subtitleTrack != null) {
            subtitleTrack.hide();
        }
        this.mShowing = false;
    }

    @UnsupportedAppUsage
    public void registerRenderer(Renderer renderer) {
        synchronized (this.mRenderers) {
            if (!this.mRenderers.contains(renderer)) {
                this.mRenderers.add(renderer);
            }
        }
    }

    public boolean hasRendererFor(MediaFormat format) {
        synchronized (this.mRenderers) {
            Iterator it = this.mRenderers.iterator();
            while (it.hasNext()) {
                if (((Renderer) it.next()).supports(format)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void setAnchor(Anchor anchor) {
        Anchor anchor2 = this.mAnchor;
        if (anchor2 != anchor) {
            if (anchor2 != null) {
                checkAnchorLooper();
                this.mAnchor.setSubtitleWidget(null);
            }
            this.mAnchor = anchor;
            this.mHandler = null;
            anchor2 = this.mAnchor;
            if (anchor2 != null) {
                this.mHandler = new Handler(anchor2.getSubtitleLooper(), this.mCallback);
                checkAnchorLooper();
                this.mAnchor.setSubtitleWidget(getRenderingWidget());
            }
        }
    }

    private void checkAnchorLooper() {
    }

    private void processOnAnchor(Message m) {
        if (Looper.myLooper() == this.mHandler.getLooper()) {
            this.mHandler.dispatchMessage(m);
        } else {
            this.mHandler.sendMessage(m);
        }
    }
}
