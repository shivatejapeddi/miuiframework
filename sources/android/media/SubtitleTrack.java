package android.media;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Canvas;
import android.media.MediaTimeProvider.OnMediaTimeListener;
import android.os.Handler;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Pair;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

public abstract class SubtitleTrack implements OnMediaTimeListener {
    private static final String TAG = "SubtitleTrack";
    public boolean DEBUG = false;
    protected final Vector<Cue> mActiveCues = new Vector();
    protected CueList mCues;
    private MediaFormat mFormat;
    protected Handler mHandler = new Handler();
    private long mLastTimeMs;
    private long mLastUpdateTimeMs;
    private long mNextScheduledTimeMs = -1;
    private Runnable mRunnable;
    protected final LongSparseArray<Run> mRunsByEndTime = new LongSparseArray();
    protected final LongSparseArray<Run> mRunsByID = new LongSparseArray();
    protected MediaTimeProvider mTimeProvider;
    protected boolean mVisible;

    public interface RenderingWidget {

        public interface OnChangedListener {
            void onChanged(RenderingWidget renderingWidget);
        }

        @UnsupportedAppUsage
        void draw(Canvas canvas);

        @UnsupportedAppUsage
        void onAttachedToWindow();

        @UnsupportedAppUsage
        void onDetachedFromWindow();

        @UnsupportedAppUsage
        void setOnChangedListener(OnChangedListener onChangedListener);

        @UnsupportedAppUsage
        void setSize(int i, int i2);

        void setVisible(boolean z);
    }

    public static class Cue {
        public long mEndTimeMs;
        public long[] mInnerTimesMs;
        public Cue mNextInRun;
        public long mRunID;
        public long mStartTimeMs;

        public void onTime(long timeMs) {
        }
    }

    static class CueList {
        private static final String TAG = "CueList";
        public boolean DEBUG = false;
        private SortedMap<Long, Vector<Cue>> mCues = new TreeMap();

        class EntryIterator implements Iterator<Pair<Long, Cue>> {
            private long mCurrentTimeMs;
            private boolean mDone;
            private Pair<Long, Cue> mLastEntry;
            private Iterator<Cue> mLastListIterator;
            private Iterator<Cue> mListIterator;
            private SortedMap<Long, Vector<Cue>> mRemainingCues;

            public boolean hasNext() {
                return this.mDone ^ 1;
            }

            public Pair<Long, Cue> next() {
                if (this.mDone) {
                    throw new NoSuchElementException("");
                }
                this.mLastEntry = new Pair(Long.valueOf(this.mCurrentTimeMs), (Cue) this.mListIterator.next());
                Iterator it = this.mListIterator;
                this.mLastListIterator = it;
                if (!it.hasNext()) {
                    nextKey();
                }
                return this.mLastEntry;
            }

            public void remove() {
                if (this.mLastListIterator == null || ((Cue) this.mLastEntry.second).mEndTimeMs != ((Long) this.mLastEntry.first).longValue()) {
                    throw new IllegalStateException("");
                }
                this.mLastListIterator.remove();
                this.mLastListIterator = null;
                if (((Vector) CueList.this.mCues.get(this.mLastEntry.first)).size() == 0) {
                    CueList.this.mCues.remove(this.mLastEntry.first);
                }
                Cue cue = this.mLastEntry.second;
                CueList.this.removeEvent(cue, cue.mStartTimeMs);
                if (cue.mInnerTimesMs != null) {
                    for (long timeMs : cue.mInnerTimesMs) {
                        CueList.this.removeEvent(cue, timeMs);
                    }
                }
            }

            public EntryIterator(SortedMap<Long, Vector<Cue>> cues) {
                if (CueList.this.DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(cues);
                    stringBuilder.append("");
                    Log.v(CueList.TAG, stringBuilder.toString());
                }
                this.mRemainingCues = cues;
                this.mLastListIterator = null;
                nextKey();
            }

            private void nextKey() {
                do {
                    try {
                        if (this.mRemainingCues != null) {
                            this.mCurrentTimeMs = ((Long) this.mRemainingCues.firstKey()).longValue();
                            this.mListIterator = ((Vector) this.mRemainingCues.get(Long.valueOf(this.mCurrentTimeMs))).iterator();
                            try {
                                this.mRemainingCues = this.mRemainingCues.tailMap(Long.valueOf(this.mCurrentTimeMs + 1));
                            } catch (IllegalArgumentException e) {
                                this.mRemainingCues = null;
                            }
                            this.mDone = false;
                        } else {
                            throw new NoSuchElementException("");
                        }
                    } catch (NoSuchElementException e2) {
                        this.mDone = true;
                        this.mRemainingCues = null;
                        this.mListIterator = null;
                        return;
                    }
                } while (!this.mListIterator.hasNext());
            }
        }

        private boolean addEvent(Cue cue, long timeMs) {
            Vector<Cue> cues = (Vector) this.mCues.get(Long.valueOf(timeMs));
            if (cues == null) {
                cues = new Vector(2);
                this.mCues.put(Long.valueOf(timeMs), cues);
            } else if (cues.contains(cue)) {
                return false;
            }
            cues.add(cue);
            return true;
        }

        private void removeEvent(Cue cue, long timeMs) {
            Vector<Cue> cues = (Vector) this.mCues.get(Long.valueOf(timeMs));
            if (cues != null) {
                cues.remove(cue);
                if (cues.size() == 0) {
                    this.mCues.remove(Long.valueOf(timeMs));
                }
            }
        }

        public void add(Cue cue) {
            if (cue.mStartTimeMs < cue.mEndTimeMs && addEvent(cue, cue.mStartTimeMs)) {
                long lastTimeMs = cue.mStartTimeMs;
                if (cue.mInnerTimesMs != null) {
                    for (long timeMs : cue.mInnerTimesMs) {
                        if (timeMs > lastTimeMs && timeMs < cue.mEndTimeMs) {
                            addEvent(cue, timeMs);
                            lastTimeMs = timeMs;
                        }
                    }
                }
                addEvent(cue, cue.mEndTimeMs);
            }
        }

        public void remove(Cue cue) {
            removeEvent(cue, cue.mStartTimeMs);
            if (cue.mInnerTimesMs != null) {
                for (long timeMs : cue.mInnerTimesMs) {
                    removeEvent(cue, timeMs);
                }
            }
            removeEvent(cue, cue.mEndTimeMs);
        }

        public Iterable<Pair<Long, Cue>> entriesBetween(long lastTimeMs, long timeMs) {
            final long j = lastTimeMs;
            final long j2 = timeMs;
            return new Iterable<Pair<Long, Cue>>() {
                public Iterator<Pair<Long, Cue>> iterator() {
                    if (CueList.this.DEBUG) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("slice (");
                        stringBuilder.append(j);
                        stringBuilder.append(", ");
                        stringBuilder.append(j2);
                        stringBuilder.append("]=");
                        Log.d(CueList.TAG, stringBuilder.toString());
                    }
                    try {
                        return new EntryIterator(CueList.this.mCues.subMap(Long.valueOf(j + 1), Long.valueOf(j2 + 1)));
                    } catch (IllegalArgumentException e) {
                        return new EntryIterator(null);
                    }
                }
            };
        }

        public long nextTimeAfter(long timeMs) {
            try {
                SortedMap<Long, Vector<Cue>> tail = this.mCues.tailMap(Long.valueOf(1 + timeMs));
                if (tail != null) {
                    return ((Long) tail.firstKey()).longValue();
                }
                return -1;
            } catch (IllegalArgumentException e) {
                return -1;
            } catch (NoSuchElementException e2) {
                return -1;
            }
        }

        CueList() {
        }
    }

    private static class Run {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        public long mEndTimeMs;
        public Cue mFirstCue;
        public Run mNextRunAtEndTimeMs;
        public Run mPrevRunAtEndTimeMs;
        public long mRunID;
        private long mStoredEndTimeMs;

        static {
            Class cls = SubtitleTrack.class;
        }

        private Run() {
            this.mEndTimeMs = -1;
            this.mRunID = 0;
            this.mStoredEndTimeMs = -1;
        }

        /* synthetic */ Run(AnonymousClass1 x0) {
            this();
        }

        public void storeByEndTimeMs(LongSparseArray<Run> runsByEndTime) {
            Run run;
            int ix = runsByEndTime.indexOfKey(this.mStoredEndTimeMs);
            if (ix >= 0) {
                if (this.mPrevRunAtEndTimeMs == null) {
                    run = this.mNextRunAtEndTimeMs;
                    if (run == null) {
                        runsByEndTime.removeAt(ix);
                    } else {
                        runsByEndTime.setValueAt(ix, run);
                    }
                }
                removeAtEndTimeMs();
            }
            long j = this.mEndTimeMs;
            if (j >= 0) {
                this.mPrevRunAtEndTimeMs = null;
                this.mNextRunAtEndTimeMs = (Run) runsByEndTime.get(j);
                run = this.mNextRunAtEndTimeMs;
                if (run != null) {
                    run.mPrevRunAtEndTimeMs = this;
                }
                runsByEndTime.put(this.mEndTimeMs, this);
                this.mStoredEndTimeMs = this.mEndTimeMs;
            }
        }

        public void removeAtEndTimeMs() {
            Run prev = this.mPrevRunAtEndTimeMs;
            Run run = this.mPrevRunAtEndTimeMs;
            if (run != null) {
                run.mNextRunAtEndTimeMs = this.mNextRunAtEndTimeMs;
                this.mPrevRunAtEndTimeMs = null;
            }
            run = this.mNextRunAtEndTimeMs;
            if (run != null) {
                run.mPrevRunAtEndTimeMs = prev;
                this.mNextRunAtEndTimeMs = null;
            }
        }
    }

    public abstract RenderingWidget getRenderingWidget();

    public abstract void onData(byte[] bArr, boolean z, long j);

    public abstract void updateView(Vector<Cue> vector);

    public SubtitleTrack(MediaFormat format) {
        this.mFormat = format;
        this.mCues = new CueList();
        clearActiveCues();
        this.mLastTimeMs = -1;
    }

    public final MediaFormat getFormat() {
        return this.mFormat;
    }

    /* Access modifiers changed, original: protected */
    public void onData(SubtitleData data) {
        long runID = data.getStartTimeUs() + 1;
        onData(data.getData(), true, runID);
        setRunDiscardTimeMs(runID, (data.getStartTimeUs() + data.getDurationUs()) / 1000);
    }

    /* Access modifiers changed, original: protected|declared_synchronized */
    /* JADX WARNING: Missing block: B:4:0x0007, code skipped:
            if (r7.mLastUpdateTimeMs > r9) goto L_0x0009;
     */
    public synchronized void updateActiveCues(boolean r8, long r9) {
        /*
        r7 = this;
        monitor-enter(r7);
        if (r8 != 0) goto L_0x0009;
    L_0x0003:
        r0 = r7.mLastUpdateTimeMs;	 Catch:{ all -> 0x00ba }
        r0 = (r0 > r9 ? 1 : (r0 == r9 ? 0 : -1));
        if (r0 <= 0) goto L_0x000c;
    L_0x0009:
        r7.clearActiveCues();	 Catch:{ all -> 0x00ba }
    L_0x000c:
        r0 = r7.mCues;	 Catch:{ all -> 0x00ba }
        r1 = r7.mLastUpdateTimeMs;	 Catch:{ all -> 0x00ba }
        r0 = r0.entriesBetween(r1, r9);	 Catch:{ all -> 0x00ba }
        r0 = r0.iterator();	 Catch:{ all -> 0x00ba }
    L_0x0018:
        r1 = r0.hasNext();	 Catch:{ all -> 0x00ba }
        if (r1 == 0) goto L_0x009f;
    L_0x001e:
        r1 = r0.next();	 Catch:{ all -> 0x00ba }
        r1 = (android.util.Pair) r1;	 Catch:{ all -> 0x00ba }
        r2 = r1.second;	 Catch:{ all -> 0x00ba }
        r2 = (android.media.SubtitleTrack.Cue) r2;	 Catch:{ all -> 0x00ba }
        r3 = r2.mEndTimeMs;	 Catch:{ all -> 0x00ba }
        r5 = r1.first;	 Catch:{ all -> 0x00ba }
        r5 = (java.lang.Long) r5;	 Catch:{ all -> 0x00ba }
        r5 = r5.longValue();	 Catch:{ all -> 0x00ba }
        r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r3 != 0) goto L_0x0061;
    L_0x0036:
        r3 = r7.DEBUG;	 Catch:{ all -> 0x00ba }
        if (r3 == 0) goto L_0x0050;
    L_0x003a:
        r3 = "SubtitleTrack";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ba }
        r4.<init>();	 Catch:{ all -> 0x00ba }
        r5 = "Removing ";
        r4.append(r5);	 Catch:{ all -> 0x00ba }
        r4.append(r2);	 Catch:{ all -> 0x00ba }
        r4 = r4.toString();	 Catch:{ all -> 0x00ba }
        android.util.Log.v(r3, r4);	 Catch:{ all -> 0x00ba }
    L_0x0050:
        r3 = r7.mActiveCues;	 Catch:{ all -> 0x00ba }
        r3.remove(r2);	 Catch:{ all -> 0x00ba }
        r3 = r2.mRunID;	 Catch:{ all -> 0x00ba }
        r5 = 0;
        r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r3 != 0) goto L_0x009d;
    L_0x005d:
        r0.remove();	 Catch:{ all -> 0x00ba }
        goto L_0x009d;
    L_0x0061:
        r3 = r2.mStartTimeMs;	 Catch:{ all -> 0x00ba }
        r5 = r1.first;	 Catch:{ all -> 0x00ba }
        r5 = (java.lang.Long) r5;	 Catch:{ all -> 0x00ba }
        r5 = r5.longValue();	 Catch:{ all -> 0x00ba }
        r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r3 != 0) goto L_0x0096;
    L_0x006f:
        r3 = r7.DEBUG;	 Catch:{ all -> 0x00ba }
        if (r3 == 0) goto L_0x0089;
    L_0x0073:
        r3 = "SubtitleTrack";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ba }
        r4.<init>();	 Catch:{ all -> 0x00ba }
        r5 = "Adding ";
        r4.append(r5);	 Catch:{ all -> 0x00ba }
        r4.append(r2);	 Catch:{ all -> 0x00ba }
        r4 = r4.toString();	 Catch:{ all -> 0x00ba }
        android.util.Log.v(r3, r4);	 Catch:{ all -> 0x00ba }
    L_0x0089:
        r3 = r2.mInnerTimesMs;	 Catch:{ all -> 0x00ba }
        if (r3 == 0) goto L_0x0090;
    L_0x008d:
        r2.onTime(r9);	 Catch:{ all -> 0x00ba }
    L_0x0090:
        r3 = r7.mActiveCues;	 Catch:{ all -> 0x00ba }
        r3.add(r2);	 Catch:{ all -> 0x00ba }
        goto L_0x009d;
    L_0x0096:
        r3 = r2.mInnerTimesMs;	 Catch:{ all -> 0x00ba }
        if (r3 == 0) goto L_0x009d;
    L_0x009a:
        r2.onTime(r9);	 Catch:{ all -> 0x00ba }
    L_0x009d:
        goto L_0x0018;
    L_0x009f:
        r0 = r7.mRunsByEndTime;	 Catch:{ all -> 0x00ba }
        r0 = r0.size();	 Catch:{ all -> 0x00ba }
        if (r0 <= 0) goto L_0x00b6;
    L_0x00a7:
        r0 = r7.mRunsByEndTime;	 Catch:{ all -> 0x00ba }
        r1 = 0;
        r2 = r0.keyAt(r1);	 Catch:{ all -> 0x00ba }
        r0 = (r2 > r9 ? 1 : (r2 == r9 ? 0 : -1));
        if (r0 > 0) goto L_0x00b6;
    L_0x00b2:
        r7.removeRunsByEndTimeIndex(r1);	 Catch:{ all -> 0x00ba }
        goto L_0x009f;
    L_0x00b6:
        r7.mLastUpdateTimeMs = r9;	 Catch:{ all -> 0x00ba }
        monitor-exit(r7);
        return;
    L_0x00ba:
        r8 = move-exception;
        monitor-exit(r7);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.SubtitleTrack.updateActiveCues(boolean, long):void");
    }

    private void removeRunsByEndTimeIndex(int ix) {
        Run run = (Run) this.mRunsByEndTime.valueAt(ix);
        while (run != null) {
            Cue cue = run.mFirstCue;
            while (cue != null) {
                this.mCues.remove(cue);
                Cue nextCue = cue.mNextInRun;
                cue.mNextInRun = null;
                cue = nextCue;
            }
            this.mRunsByID.remove(run.mRunID);
            Run nextRun = run.mNextRunAtEndTimeMs;
            run.mPrevRunAtEndTimeMs = null;
            run.mNextRunAtEndTimeMs = null;
            run = nextRun;
        }
        this.mRunsByEndTime.removeAt(ix);
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        for (int ix = this.mRunsByEndTime.size() - 1; ix >= 0; ix--) {
            removeRunsByEndTimeIndex(ix);
        }
        super.finalize();
    }

    private synchronized void takeTime(long timeMs) {
        this.mLastTimeMs = timeMs;
    }

    /* Access modifiers changed, original: protected|declared_synchronized */
    public synchronized void clearActiveCues() {
        if (this.DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Clearing ");
            stringBuilder.append(this.mActiveCues.size());
            stringBuilder.append(" active cues");
            Log.v(str, stringBuilder.toString());
        }
        this.mActiveCues.clear();
        this.mLastUpdateTimeMs = -1;
    }

    /* Access modifiers changed, original: protected */
    public void scheduleTimedEvents() {
        if (this.mTimeProvider != null) {
            this.mNextScheduledTimeMs = this.mCues.nextTimeAfter(this.mLastTimeMs);
            if (this.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("sched @");
                stringBuilder.append(this.mNextScheduledTimeMs);
                stringBuilder.append(" after ");
                stringBuilder.append(this.mLastTimeMs);
                Log.d(TAG, stringBuilder.toString());
            }
            MediaTimeProvider mediaTimeProvider = this.mTimeProvider;
            long j = this.mNextScheduledTimeMs;
            mediaTimeProvider.notifyAt(j >= 0 ? j * 1000 : -1, this);
        }
    }

    public void onTimedEvent(long timeUs) {
        if (this.DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onTimedEvent ");
            stringBuilder.append(timeUs);
            Log.d(TAG, stringBuilder.toString());
        }
        synchronized (this) {
            long timeMs = timeUs / 1000;
            updateActiveCues(false, timeMs);
            takeTime(timeMs);
        }
        updateView(this.mActiveCues);
        scheduleTimedEvents();
    }

    public void onSeek(long timeUs) {
        if (this.DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onSeek ");
            stringBuilder.append(timeUs);
            Log.d(TAG, stringBuilder.toString());
        }
        synchronized (this) {
            long timeMs = timeUs / 1000;
            updateActiveCues(true, timeMs);
            takeTime(timeMs);
        }
        updateView(this.mActiveCues);
        scheduleTimedEvents();
    }

    public void onStop() {
        synchronized (this) {
            if (this.DEBUG) {
                Log.d(TAG, "onStop");
            }
            clearActiveCues();
            this.mLastTimeMs = -1;
        }
        updateView(this.mActiveCues);
        this.mNextScheduledTimeMs = -1;
        MediaTimeProvider mediaTimeProvider = this.mTimeProvider;
        if (mediaTimeProvider != null) {
            mediaTimeProvider.notifyAt(-1, this);
        }
    }

    public void show() {
        if (!this.mVisible) {
            this.mVisible = true;
            RenderingWidget renderingWidget = getRenderingWidget();
            if (renderingWidget != null) {
                renderingWidget.setVisible(true);
            }
            MediaTimeProvider mediaTimeProvider = this.mTimeProvider;
            if (mediaTimeProvider != null) {
                mediaTimeProvider.scheduleUpdate(this);
            }
        }
    }

    public void hide() {
        if (this.mVisible) {
            MediaTimeProvider mediaTimeProvider = this.mTimeProvider;
            if (mediaTimeProvider != null) {
                mediaTimeProvider.cancelNotifications(this);
            }
            RenderingWidget renderingWidget = getRenderingWidget();
            if (renderingWidget != null) {
                renderingWidget.setVisible(false);
            }
            this.mVisible = false;
        }
    }

    /* Access modifiers changed, original: protected|declared_synchronized */
    /* JADX WARNING: Missing block: B:40:0x00e1, code skipped:
            return true;
     */
    /* JADX WARNING: Missing block: B:52:0x0100, code skipped:
            return false;
     */
    public synchronized boolean addCue(android.media.SubtitleTrack.Cue r12) {
        /*
        r11 = this;
        monitor-enter(r11);
        r0 = r11.mCues;	 Catch:{ all -> 0x0101 }
        r0.add(r12);	 Catch:{ all -> 0x0101 }
        r0 = r12.mRunID;	 Catch:{ all -> 0x0101 }
        r2 = 0;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 == 0) goto L_0x003f;
    L_0x000e:
        r0 = r11.mRunsByID;	 Catch:{ all -> 0x0101 }
        r4 = r12.mRunID;	 Catch:{ all -> 0x0101 }
        r0 = r0.get(r4);	 Catch:{ all -> 0x0101 }
        r0 = (android.media.SubtitleTrack.Run) r0;	 Catch:{ all -> 0x0101 }
        if (r0 != 0) goto L_0x002d;
    L_0x001a:
        r1 = new android.media.SubtitleTrack$Run;	 Catch:{ all -> 0x0101 }
        r4 = 0;
        r1.<init>(r4);	 Catch:{ all -> 0x0101 }
        r0 = r1;
        r1 = r11.mRunsByID;	 Catch:{ all -> 0x0101 }
        r4 = r12.mRunID;	 Catch:{ all -> 0x0101 }
        r1.put(r4, r0);	 Catch:{ all -> 0x0101 }
        r4 = r12.mEndTimeMs;	 Catch:{ all -> 0x0101 }
        r0.mEndTimeMs = r4;	 Catch:{ all -> 0x0101 }
        goto L_0x0039;
    L_0x002d:
        r4 = r0.mEndTimeMs;	 Catch:{ all -> 0x0101 }
        r6 = r12.mEndTimeMs;	 Catch:{ all -> 0x0101 }
        r1 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r1 >= 0) goto L_0x0039;
    L_0x0035:
        r4 = r12.mEndTimeMs;	 Catch:{ all -> 0x0101 }
        r0.mEndTimeMs = r4;	 Catch:{ all -> 0x0101 }
    L_0x0039:
        r1 = r0.mFirstCue;	 Catch:{ all -> 0x0101 }
        r12.mNextInRun = r1;	 Catch:{ all -> 0x0101 }
        r0.mFirstCue = r12;	 Catch:{ all -> 0x0101 }
    L_0x003f:
        r0 = -1;
        r4 = r11.mTimeProvider;	 Catch:{ all -> 0x0101 }
        r5 = 1;
        r6 = 0;
        if (r4 == 0) goto L_0x0053;
    L_0x0047:
        r4 = r11.mTimeProvider;	 Catch:{ IllegalStateException -> 0x0052 }
        r7 = r4.getCurrentTimeUs(r6, r5);	 Catch:{ IllegalStateException -> 0x0052 }
        r9 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r7 = r7 / r9;
        r0 = r7;
        goto L_0x0053;
    L_0x0052:
        r4 = move-exception;
    L_0x0053:
        r4 = r11.DEBUG;	 Catch:{ all -> 0x0101 }
        if (r4 == 0) goto L_0x0096;
    L_0x0057:
        r4 = "SubtitleTrack";
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0101 }
        r7.<init>();	 Catch:{ all -> 0x0101 }
        r8 = "mVisible=";
        r7.append(r8);	 Catch:{ all -> 0x0101 }
        r8 = r11.mVisible;	 Catch:{ all -> 0x0101 }
        r7.append(r8);	 Catch:{ all -> 0x0101 }
        r8 = ", ";
        r7.append(r8);	 Catch:{ all -> 0x0101 }
        r8 = r12.mStartTimeMs;	 Catch:{ all -> 0x0101 }
        r7.append(r8);	 Catch:{ all -> 0x0101 }
        r8 = " <= ";
        r7.append(r8);	 Catch:{ all -> 0x0101 }
        r7.append(r0);	 Catch:{ all -> 0x0101 }
        r8 = ", ";
        r7.append(r8);	 Catch:{ all -> 0x0101 }
        r8 = r12.mEndTimeMs;	 Catch:{ all -> 0x0101 }
        r7.append(r8);	 Catch:{ all -> 0x0101 }
        r8 = " >= ";
        r7.append(r8);	 Catch:{ all -> 0x0101 }
        r8 = r11.mLastTimeMs;	 Catch:{ all -> 0x0101 }
        r7.append(r8);	 Catch:{ all -> 0x0101 }
        r7 = r7.toString();	 Catch:{ all -> 0x0101 }
        android.util.Log.v(r4, r7);	 Catch:{ all -> 0x0101 }
    L_0x0096:
        r4 = r11.mVisible;	 Catch:{ all -> 0x0101 }
        if (r4 == 0) goto L_0x00e2;
    L_0x009a:
        r7 = r12.mStartTimeMs;	 Catch:{ all -> 0x0101 }
        r4 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1));
        if (r4 > 0) goto L_0x00e2;
    L_0x00a0:
        r7 = r12.mEndTimeMs;	 Catch:{ all -> 0x0101 }
        r9 = r11.mLastTimeMs;	 Catch:{ all -> 0x0101 }
        r4 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r4 < 0) goto L_0x00e2;
    L_0x00a8:
        r2 = r11.mRunnable;	 Catch:{ all -> 0x0101 }
        if (r2 == 0) goto L_0x00b3;
    L_0x00ac:
        r2 = r11.mHandler;	 Catch:{ all -> 0x0101 }
        r3 = r11.mRunnable;	 Catch:{ all -> 0x0101 }
        r2.removeCallbacks(r3);	 Catch:{ all -> 0x0101 }
    L_0x00b3:
        r2 = r11;
        r3 = r0;
        r6 = new android.media.SubtitleTrack$1;	 Catch:{ all -> 0x0101 }
        r6.<init>(r2, r3);	 Catch:{ all -> 0x0101 }
        r11.mRunnable = r6;	 Catch:{ all -> 0x0101 }
        r6 = r11.mHandler;	 Catch:{ all -> 0x0101 }
        r7 = r11.mRunnable;	 Catch:{ all -> 0x0101 }
        r8 = 10;
        r6 = r6.postDelayed(r7, r8);	 Catch:{ all -> 0x0101 }
        if (r6 == 0) goto L_0x00d5;
    L_0x00c8:
        r6 = r11.DEBUG;	 Catch:{ all -> 0x0101 }
        if (r6 == 0) goto L_0x00e0;
    L_0x00cc:
        r6 = "SubtitleTrack";
        r7 = "scheduling update";
        android.util.Log.v(r6, r7);	 Catch:{ all -> 0x0101 }
        goto L_0x00e0;
    L_0x00d5:
        r6 = r11.DEBUG;	 Catch:{ all -> 0x0101 }
        if (r6 == 0) goto L_0x00e0;
    L_0x00d9:
        r6 = "SubtitleTrack";
        r7 = "failed to schedule subtitle view update";
        android.util.Log.w(r6, r7);	 Catch:{ all -> 0x0101 }
    L_0x00e0:
        monitor-exit(r11);
        return r5;
    L_0x00e2:
        r4 = r11.mVisible;	 Catch:{ all -> 0x0101 }
        if (r4 == 0) goto L_0x00ff;
    L_0x00e6:
        r4 = r12.mEndTimeMs;	 Catch:{ all -> 0x0101 }
        r7 = r11.mLastTimeMs;	 Catch:{ all -> 0x0101 }
        r4 = (r4 > r7 ? 1 : (r4 == r7 ? 0 : -1));
        if (r4 < 0) goto L_0x00ff;
    L_0x00ee:
        r4 = r12.mStartTimeMs;	 Catch:{ all -> 0x0101 }
        r7 = r11.mNextScheduledTimeMs;	 Catch:{ all -> 0x0101 }
        r4 = (r4 > r7 ? 1 : (r4 == r7 ? 0 : -1));
        if (r4 < 0) goto L_0x00fc;
    L_0x00f6:
        r4 = r11.mNextScheduledTimeMs;	 Catch:{ all -> 0x0101 }
        r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1));
        if (r2 >= 0) goto L_0x00ff;
    L_0x00fc:
        r11.scheduleTimedEvents();	 Catch:{ all -> 0x0101 }
    L_0x00ff:
        monitor-exit(r11);
        return r6;
    L_0x0101:
        r12 = move-exception;
        monitor-exit(r11);
        throw r12;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.SubtitleTrack.addCue(android.media.SubtitleTrack$Cue):boolean");
    }

    /* JADX WARNING: Missing block: B:14:0x001c, code skipped:
            return;
     */
    public synchronized void setTimeProvider(android.media.MediaTimeProvider r2) {
        /*
        r1 = this;
        monitor-enter(r1);
        r0 = r1.mTimeProvider;	 Catch:{ all -> 0x001d }
        if (r0 != r2) goto L_0x0007;
    L_0x0005:
        monitor-exit(r1);
        return;
    L_0x0007:
        r0 = r1.mTimeProvider;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x0010;
    L_0x000b:
        r0 = r1.mTimeProvider;	 Catch:{ all -> 0x001d }
        r0.cancelNotifications(r1);	 Catch:{ all -> 0x001d }
    L_0x0010:
        r1.mTimeProvider = r2;	 Catch:{ all -> 0x001d }
        r0 = r1.mTimeProvider;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x001b;
    L_0x0016:
        r0 = r1.mTimeProvider;	 Catch:{ all -> 0x001d }
        r0.scheduleUpdate(r1);	 Catch:{ all -> 0x001d }
    L_0x001b:
        monitor-exit(r1);
        return;
    L_0x001d:
        r2 = move-exception;
        monitor-exit(r1);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.SubtitleTrack.setTimeProvider(android.media.MediaTimeProvider):void");
    }

    /* Access modifiers changed, original: protected */
    public void finishedRun(long runID) {
        if (runID != 0 && runID != -1) {
            Run run = (Run) this.mRunsByID.get(runID);
            if (run != null) {
                run.storeByEndTimeMs(this.mRunsByEndTime);
            }
        }
    }

    public void setRunDiscardTimeMs(long runID, long timeMs) {
        if (runID != 0 && runID != -1) {
            Run run = (Run) this.mRunsByID.get(runID);
            if (run != null) {
                run.mEndTimeMs = timeMs;
                run.storeByEndTimeMs(this.mRunsByEndTime);
            }
        }
    }

    public int getTrackType() {
        if (getRenderingWidget() == null) {
            return 3;
        }
        return 4;
    }
}
