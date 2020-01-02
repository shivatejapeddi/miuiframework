package miui.maml.elements;

import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import miui.maml.ScreenElementRoot;
import miui.maml.util.Utils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FramerateController extends ScreenElement {
    public static final String INNER_TAG = "ControlPoint";
    public static final String TAG_NAME = "FramerateController";
    private ArrayList<ControlPoint> mControlPoints = new ArrayList();
    private long mDelay;
    private long mLastUpdateTime;
    private Object mLock = new Object();
    private boolean mLoop;
    private long mNextUpdateInterval;
    private long mStartTime;
    private boolean mStopped;
    private String mTag;
    private long mTimeRange;

    public static class ControlPoint {
        public int mFramerate;
        public long mTime;

        public ControlPoint(Element node) {
            this.mTime = Utils.getAttrAsLongThrows(node, "time");
            this.mFramerate = Utils.getAttrAsInt(node, "frameRate", -1);
        }
    }

    public FramerateController(Element ele, ScreenElementRoot root) {
        super(ele, root);
        this.mLoop = Boolean.parseBoolean(ele.getAttribute("loop"));
        this.mTag = ele.getAttribute("tag");
        String strDelay = ele.getAttribute("delay");
        if (!TextUtils.isEmpty(strDelay)) {
            try {
                this.mDelay = Long.parseLong(strDelay);
            } catch (NumberFormatException e) {
                Log.w(TAG_NAME, "invalid delay attribute");
            }
        }
        NodeList nodeList = ele.getElementsByTagName(INNER_TAG);
        for (int i = 0; i < nodeList.getLength(); i++) {
            this.mControlPoints.add(new ControlPoint((Element) nodeList.item(i)));
        }
        ArrayList arrayList = this.mControlPoints;
        boolean z = true;
        this.mTimeRange = ((ControlPoint) arrayList.get(arrayList.size() - 1)).mTime;
        if (!this.mLoop || this.mTimeRange == 0) {
            z = false;
        }
        this.mLoop = z;
    }

    /* Access modifiers changed, original: protected */
    public void doRender(Canvas c) {
    }

    private void restart(long startTime) {
        synchronized (this.mLock) {
            this.mStartTime = this.mDelay + startTime;
            this.mStopped = false;
            this.mLastUpdateTime = 0;
            this.mNextUpdateInterval = 0;
            requestUpdate();
        }
    }

    public void reset(long time) {
        super.reset(time);
        restart(time);
    }

    /* Access modifiers changed, original: protected */
    public void playAnim(long time, long startTime, long endTime, boolean isLoop, boolean isDelay) {
        if (isVisible()) {
            super.playAnim(time, startTime, endTime, isLoop, isDelay);
            restart(time - startTime);
        }
    }

    public void setAnim(String[] tags) {
        show(ScreenElement.isTagEnable(tags, this.mTag));
    }

    public long updateFramerate(long currentTime) {
        long j = currentTime;
        updateVisibility();
        if (!isVisible()) {
            return Long.MAX_VALUE;
        }
        synchronized (this.mLock) {
            if (this.mStopped) {
                return Long.MAX_VALUE;
            }
            long elapsedTime;
            long j2;
            if (this.mLastUpdateTime > 0) {
                elapsedTime = j - this.mLastUpdateTime;
                if (elapsedTime >= 0 && elapsedTime < this.mNextUpdateInterval) {
                    this.mNextUpdateInterval -= elapsedTime;
                    this.mLastUpdateTime = j;
                    j2 = this.mNextUpdateInterval;
                    return j2;
                }
            }
            elapsedTime = j - this.mStartTime;
            if (elapsedTime < 0) {
                elapsedTime = 0;
            }
            long time = this.mLoop ? elapsedTime % (this.mTimeRange + 1) : elapsedTime;
            long nextUpdateTime = 0;
            int i = this.mControlPoints.size() - 1;
            while (i >= 0) {
                ControlPoint cp = (ControlPoint) this.mControlPoints.get(i);
                if (time >= cp.mTime) {
                    requestFramerate((float) cp.mFramerate);
                    if (!this.mLoop && i == this.mControlPoints.size() - 1) {
                        this.mStopped = true;
                    }
                    this.mLastUpdateTime = j;
                    this.mNextUpdateInterval = this.mStopped ? Long.MAX_VALUE : nextUpdateTime - time;
                    j2 = this.mNextUpdateInterval;
                    return j2;
                }
                nextUpdateTime = cp.mTime;
                i--;
            }
            return Long.MAX_VALUE;
        }
    }
}
