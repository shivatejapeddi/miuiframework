package android.media;

import android.media.SubtitleTrack.Cue;
import android.util.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: TtmlRenderer */
class TtmlTrack extends SubtitleTrack implements TtmlNodeListener {
    private static final String TAG = "TtmlTrack";
    private Long mCurrentRunID;
    private final TtmlParser mParser = new TtmlParser(this);
    private String mParsingData;
    private final TtmlRenderingWidget mRenderingWidget;
    private TtmlNode mRootNode;
    private final TreeSet<Long> mTimeEvents = new TreeSet();
    private final LinkedList<TtmlNode> mTtmlNodes = new LinkedList();

    TtmlTrack(TtmlRenderingWidget renderingWidget, MediaFormat format) {
        super(format);
        this.mRenderingWidget = renderingWidget;
        this.mParsingData = "";
    }

    public TtmlRenderingWidget getRenderingWidget() {
        return this.mRenderingWidget;
    }

    public void onData(byte[] data, boolean eos, long runID) {
        try {
            String str = new String(data, "UTF-8");
            synchronized (this.mParser) {
                if (this.mCurrentRunID != null) {
                    if (runID != this.mCurrentRunID.longValue()) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Run #");
                        stringBuilder.append(this.mCurrentRunID);
                        stringBuilder.append(" in progress.  Cannot process run #");
                        stringBuilder.append(runID);
                        throw new IllegalStateException(stringBuilder.toString());
                    }
                }
                this.mCurrentRunID = Long.valueOf(runID);
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(this.mParsingData);
                stringBuilder2.append(str);
                this.mParsingData = stringBuilder2.toString();
                if (eos) {
                    try {
                        this.mParser.parse(this.mParsingData, this.mCurrentRunID.longValue());
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    finishedRun(runID);
                    this.mParsingData = "";
                    this.mCurrentRunID = null;
                }
            }
        } catch (UnsupportedEncodingException e3) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("subtitle data is not UTF-8 encoded: ");
            stringBuilder3.append(e3);
            Log.w(TAG, stringBuilder3.toString());
        }
    }

    public void onTtmlNodeParsed(TtmlNode node) {
        this.mTtmlNodes.addLast(node);
        addTimeEvents(node);
    }

    public void onRootNodeParsed(TtmlNode node) {
        this.mRootNode = node;
        while (true) {
            TtmlCue nextResult = getNextResult();
            TtmlCue cue = nextResult;
            if (nextResult != null) {
                addCue(cue);
            } else {
                this.mRootNode = null;
                this.mTtmlNodes.clear();
                this.mTimeEvents.clear();
                return;
            }
        }
    }

    public void updateView(Vector<Cue> activeCues) {
        String str = TAG;
        if (this.mVisible) {
            if (this.DEBUG && this.mTimeProvider != null) {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("at ");
                    stringBuilder.append(this.mTimeProvider.getCurrentTimeUs(false, true) / 1000);
                    stringBuilder.append(" ms the active cues are:");
                    Log.d(str, stringBuilder.toString());
                } catch (IllegalStateException e) {
                    Log.d(str, "at (illegal state) the active cues are:");
                }
            }
            this.mRenderingWidget.setActiveCues(activeCues);
        }
    }

    public TtmlCue getNextResult() {
        while (this.mTimeEvents.size() >= 2) {
            long start = ((Long) this.mTimeEvents.pollFirst()).longValue();
            long end = ((Long) this.mTimeEvents.first()).longValue();
            if (!getActiveNodes(start, end).isEmpty()) {
                return new TtmlCue(start, end, TtmlUtils.applySpacePolicy(TtmlUtils.extractText(this.mRootNode, start, end), false), TtmlUtils.extractTtmlFragment(this.mRootNode, start, end));
            }
        }
        return null;
    }

    private void addTimeEvents(TtmlNode node) {
        this.mTimeEvents.add(Long.valueOf(node.mStartTimeMs));
        this.mTimeEvents.add(Long.valueOf(node.mEndTimeMs));
        for (int i = 0; i < node.mChildren.size(); i++) {
            addTimeEvents((TtmlNode) node.mChildren.get(i));
        }
    }

    private List<TtmlNode> getActiveNodes(long startTimeUs, long endTimeUs) {
        List<TtmlNode> activeNodes = new ArrayList();
        for (int i = 0; i < this.mTtmlNodes.size(); i++) {
            TtmlNode node = (TtmlNode) this.mTtmlNodes.get(i);
            if (node.isActive(startTimeUs, endTimeUs)) {
                activeNodes.add(node);
            }
        }
        return activeNodes;
    }
}
