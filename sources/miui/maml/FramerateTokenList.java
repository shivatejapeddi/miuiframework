package miui.maml;

import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class FramerateTokenList {
    private static final String LOG_TAG = "FramerateTokenList";
    private float mCurFramerate;
    private FramerateChangeListener mFramerateChangeListener;
    private ArrayList<FramerateToken> mList = new ArrayList();

    public interface FramerateChangeListener {
        void onFrameRateChage(float f, float f2);
    }

    public class FramerateToken {
        public float mFramerate;
        public String mName;

        public FramerateToken(String name) {
            this.mName = name;
        }

        public float getFramerate() {
            return this.mFramerate;
        }

        public void requestFramerate(float f) {
            if (this.mFramerate != f) {
                if (FramerateTokenList.this.mFramerateChangeListener != null) {
                    FramerateTokenList.this.mFramerateChangeListener.onFrameRateChage(this.mFramerate, f);
                }
                this.mFramerate = f;
                FramerateTokenList.this.onChange();
            }
        }
    }

    public FramerateTokenList(FramerateChangeListener l) {
        this.mFramerateChangeListener = l;
    }

    public FramerateToken createToken(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("createToken: ");
        stringBuilder.append(name);
        Log.d(LOG_TAG, stringBuilder.toString());
        FramerateToken token = new FramerateToken(name);
        synchronized (this.mList) {
            this.mList.add(token);
        }
        return token;
    }

    private void onChange() {
        float r = 0.0f;
        synchronized (this.mList) {
            Iterator it = this.mList.iterator();
            while (it.hasNext()) {
                FramerateToken t = (FramerateToken) it.next();
                if (t.mFramerate > r) {
                    r = t.mFramerate;
                }
            }
        }
        this.mCurFramerate = r;
    }

    public void clear() {
        synchronized (this.mList) {
            this.mList.clear();
        }
    }

    public float getFramerate() {
        return this.mCurFramerate;
    }
}
