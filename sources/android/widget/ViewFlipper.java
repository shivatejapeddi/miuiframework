package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Process;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R;

@RemoteView
public class ViewFlipper extends ViewAnimator {
    private static final int DEFAULT_INTERVAL = 3000;
    private static final boolean LOGD = false;
    private static final String TAG = "ViewFlipper";
    private boolean mAutoStart = false;
    private int mFlipInterval = 3000;
    private final Runnable mFlipRunnable = new Runnable() {
        public void run() {
            if (ViewFlipper.this.mRunning) {
                ViewFlipper.this.showNext();
                ViewFlipper viewFlipper = ViewFlipper.this;
                viewFlipper.postDelayed(viewFlipper.mFlipRunnable, (long) ViewFlipper.this.mFlipInterval);
            }
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                ViewFlipper.this.mUserPresent = false;
                ViewFlipper.this.updateRunning();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                ViewFlipper.this.mUserPresent = true;
                ViewFlipper.this.updateRunning(false);
            }
        }
    };
    private boolean mRunning = false;
    private boolean mStarted = false;
    @UnsupportedAppUsage
    private boolean mUserPresent = true;
    private boolean mVisible = false;

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<ViewFlipper> {
        private int mAutoStartId;
        private int mFlipIntervalId;
        private int mFlippingId;
        private boolean mPropertiesMapped = false;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mAutoStartId = propertyMapper.mapBoolean("autoStart", 16843445);
            this.mFlipIntervalId = propertyMapper.mapInt("flipInterval", 16843129);
            this.mFlippingId = propertyMapper.mapBoolean("flipping", 0);
            this.mPropertiesMapped = true;
        }

        public void readProperties(ViewFlipper node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readBoolean(this.mAutoStartId, node.isAutoStart());
                propertyReader.readInt(this.mFlipIntervalId, node.getFlipInterval());
                propertyReader.readBoolean(this.mFlippingId, node.isFlipping());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    public ViewFlipper(Context context) {
        super(context);
    }

    public ViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewFlipper);
        this.mFlipInterval = a.getInt(0, 3000);
        this.mAutoStart = a.getBoolean(1, false);
        a.recycle();
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        getContext().registerReceiverAsUser(this.mReceiver, Process.myUserHandle(), filter, null, getHandler());
        if (this.mAutoStart) {
            startFlipping();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mVisible = false;
        getContext().unregisterReceiver(this.mReceiver);
        updateRunning();
    }

    /* Access modifiers changed, original: protected */
    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        this.mVisible = visibility == 0;
        updateRunning(false);
    }

    @RemotableViewMethod
    public void setFlipInterval(int milliseconds) {
        this.mFlipInterval = milliseconds;
    }

    public int getFlipInterval() {
        return this.mFlipInterval;
    }

    public void startFlipping() {
        this.mStarted = true;
        updateRunning();
    }

    public void stopFlipping() {
        this.mStarted = false;
        updateRunning();
    }

    public CharSequence getAccessibilityClassName() {
        return ViewFlipper.class.getName();
    }

    private void updateRunning() {
        updateRunning(true);
    }

    @UnsupportedAppUsage
    private void updateRunning(boolean flipNow) {
        boolean running = this.mVisible && this.mStarted && this.mUserPresent;
        if (running != this.mRunning) {
            if (running) {
                showOnly(this.mWhichChild, flipNow);
                postDelayed(this.mFlipRunnable, (long) this.mFlipInterval);
            } else {
                removeCallbacks(this.mFlipRunnable);
            }
            this.mRunning = running;
        }
    }

    public boolean isFlipping() {
        return this.mStarted;
    }

    public void setAutoStart(boolean autoStart) {
        this.mAutoStart = autoStart;
    }

    public boolean isAutoStart() {
        return this.mAutoStart;
    }
}
