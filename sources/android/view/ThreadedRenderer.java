package android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.HardwareRenderer;
import android.graphics.HardwareRenderer.FrameDrawingCallback;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RenderNode;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.TimeUtils;
import android.view.Surface.OutOfResourcesException;
import android.view.animation.AnimationUtils;
import com.android.internal.R;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public final class ThreadedRenderer extends HardwareRenderer {
    public static final String DEBUG_DIRTY_REGIONS_PROPERTY = "debug.hwui.show_dirty_regions";
    public static final String DEBUG_FORCE_DARK = "debug.hwui.force_dark";
    public static final String DEBUG_FPS_DIVISOR = "debug.hwui.fps_divisor";
    public static final String DEBUG_OVERDRAW_PROPERTY = "debug.hwui.overdraw";
    public static final String DEBUG_SHOW_LAYERS_UPDATES_PROPERTY = "debug.hwui.show_layers_updates";
    public static final String DEBUG_SHOW_NON_RECTANGULAR_CLIP_PROPERTY = "debug.hwui.show_non_rect_clip";
    public static int EGL_CONTEXT_PRIORITY_HIGH_IMG = 12545;
    public static int EGL_CONTEXT_PRIORITY_LOW_IMG = 12547;
    public static int EGL_CONTEXT_PRIORITY_MEDIUM_IMG = 12546;
    public static final String OVERDRAW_PROPERTY_SHOW = "show";
    static final String PRINT_CONFIG_PROPERTY = "debug.hwui.print_config";
    static final String PROFILE_MAXFRAMES_PROPERTY = "debug.hwui.profile.maxframes";
    public static final String PROFILE_PROPERTY = "debug.hwui.profile";
    public static final String PROFILE_PROPERTY_VISUALIZE_BARS = "visual_bars";
    private static final String[] VISUALIZERS = new String[]{PROFILE_PROPERTY_VISUALIZE_BARS};
    public static boolean sRendererDisabled = false;
    private static Boolean sSupportsOpenGL;
    public static boolean sSystemRendererDisabled = false;
    public static boolean sTrimForeground = false;
    private boolean mEnabled;
    private boolean mHasInsets;
    private int mHeight;
    private boolean mInitialized = false;
    private int mInsetLeft;
    private int mInsetTop;
    private final float mLightRadius;
    private final float mLightY;
    private final float mLightZ;
    private FrameDrawingCallback mNextRtFrameCallback;
    private boolean mRequested = true;
    private boolean mRootNodeNeedsUpdate;
    private int mSurfaceHeight;
    private int mSurfaceWidth;
    private int mWidth;

    interface DrawCallbacks {
        void onPostDraw(RecordingCanvas recordingCanvas);

        void onPreDraw(RecordingCanvas recordingCanvas);
    }

    public static class SimpleRenderer extends HardwareRenderer {
        private final float mLightRadius;
        private final float mLightY;
        private final float mLightZ;

        public SimpleRenderer(Context context, String name, Surface surface) {
            setName(name);
            setOpaque(false);
            setSurface(surface);
            TypedArray a = context.obtainStyledAttributes(null, R.styleable.Lighting, 0, 0);
            this.mLightY = a.getDimension(3, 0.0f);
            this.mLightZ = a.getDimension(4, 0.0f);
            this.mLightRadius = a.getDimension(2, 0.0f);
            float ambientShadowAlpha = a.getFloat(0, 0.0f);
            float spotShadowAlpha = a.getFloat(1, 0.0f);
            a.recycle();
            setLightSourceAlpha(ambientShadowAlpha, spotShadowAlpha);
        }

        public void setLightCenter(Display display, int windowLeft, int windowTop) {
            Point displaySize = new Point();
            display.getRealSize(displaySize);
            setLightSourceGeometry((((float) displaySize.x) / 2.0f) - ((float) windowLeft), this.mLightY - ((float) windowTop), this.mLightZ, this.mLightRadius);
        }

        public RenderNode getRootNode() {
            return this.mRootNode;
        }

        public void draw(FrameDrawingCallback callback) {
            long vsync = AnimationUtils.currentAnimationTimeMillis() * TimeUtils.NANOS_PER_MS;
            if (callback != null) {
                setFrameCallback(callback);
            }
            createRenderRequest().setVsyncTime(vsync).syncAndDraw();
        }
    }

    static {
        isAvailable();
    }

    public static void disable(boolean system) {
        sRendererDisabled = true;
        if (system) {
            sSystemRendererDisabled = true;
        }
    }

    public static void enableForegroundTrimming() {
        sTrimForeground = true;
    }

    public static boolean isAvailable() {
        Boolean bool = sSupportsOpenGL;
        if (bool != null) {
            return bool.booleanValue();
        }
        boolean z = false;
        if (SystemProperties.getInt("ro.kernel.qemu", 0) == 0) {
            sSupportsOpenGL = Boolean.valueOf(true);
            return true;
        }
        int qemu_gles = SystemProperties.getInt("qemu.gles", -1);
        if (qemu_gles == -1) {
            return false;
        }
        if (qemu_gles > 0) {
            z = true;
        }
        sSupportsOpenGL = Boolean.valueOf(z);
        return sSupportsOpenGL.booleanValue();
    }

    public static ThreadedRenderer create(Context context, boolean translucent, String name) {
        if (isAvailable()) {
            return new ThreadedRenderer(context, translucent, name);
        }
        return null;
    }

    ThreadedRenderer(Context context, boolean translucent, String name) {
        setName(name);
        setOpaque(translucent ^ 1);
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.Lighting, 0, 0);
        this.mLightY = a.getDimension(3, 0.0f);
        this.mLightZ = a.getDimension(4, 0.0f);
        this.mLightRadius = a.getDimension(2, 0.0f);
        float ambientShadowAlpha = a.getFloat(0, 0.0f);
        float spotShadowAlpha = a.getFloat(1, 0.0f);
        a.recycle();
        setLightSourceAlpha(ambientShadowAlpha, spotShadowAlpha);
    }

    public void destroy() {
        this.mInitialized = false;
        updateEnabledState(null);
        super.destroy();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isEnabled() {
        return this.mEnabled;
    }

    /* Access modifiers changed, original: 0000 */
    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isRequested() {
        return this.mRequested;
    }

    /* Access modifiers changed, original: 0000 */
    public void setRequested(boolean requested) {
        this.mRequested = requested;
    }

    private void updateEnabledState(Surface surface) {
        if (surface == null || !surface.isValid()) {
            setEnabled(false);
        } else {
            setEnabled(this.mInitialized);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean initialize(Surface surface) throws OutOfResourcesException {
        boolean status = this.mInitialized ^ true;
        this.mInitialized = true;
        updateEnabledState(surface);
        setSurface(surface);
        return status;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean initializeIfNeeded(int width, int height, AttachInfo attachInfo, Surface surface, Rect surfaceInsets) throws OutOfResourcesException {
        if (!isRequested() || isEnabled() || !initialize(surface)) {
            return false;
        }
        setup(width, height, attachInfo, surfaceInsets);
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public void updateSurface(Surface surface) throws OutOfResourcesException {
        updateEnabledState(surface);
        setSurface(surface);
    }

    public void setSurface(Surface surface) {
        if (surface == null || !surface.isValid()) {
            super.setSurface(null);
        } else {
            super.setSurface(surface);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void registerRtFrameCallback(FrameDrawingCallback callback) {
        this.mNextRtFrameCallback = callback;
    }

    /* Access modifiers changed, original: 0000 */
    public void destroyHardwareResources(View view) {
        destroyResources(view);
        clearContent();
    }

    private static void destroyResources(View view) {
        view.destroyHardwareResources();
    }

    /* Access modifiers changed, original: 0000 */
    public void setup(int width, int height, AttachInfo attachInfo, Rect surfaceInsets) {
        this.mWidth = width;
        this.mHeight = height;
        if (surfaceInsets == null || (surfaceInsets.left == 0 && surfaceInsets.right == 0 && surfaceInsets.top == 0 && surfaceInsets.bottom == 0)) {
            this.mHasInsets = false;
            this.mInsetLeft = 0;
            this.mInsetTop = 0;
            this.mSurfaceWidth = width;
            this.mSurfaceHeight = height;
        } else {
            this.mHasInsets = true;
            this.mInsetLeft = surfaceInsets.left;
            this.mInsetTop = surfaceInsets.top;
            this.mSurfaceWidth = (this.mInsetLeft + width) + surfaceInsets.right;
            this.mSurfaceHeight = (this.mInsetTop + height) + surfaceInsets.bottom;
            setOpaque(false);
        }
        this.mRootNode.setLeftTopRightBottom(-this.mInsetLeft, -this.mInsetTop, this.mSurfaceWidth, this.mSurfaceHeight);
        setLightCenter(attachInfo);
    }

    /* Access modifiers changed, original: 0000 */
    public void setLightCenter(AttachInfo attachInfo) {
        Point displaySize = attachInfo.mPoint;
        attachInfo.mDisplay.getRealSize(displaySize);
        setLightSourceGeometry((((float) displaySize.x) / 2.0f) - ((float) attachInfo.mWindowLeft), this.mLightY - ((float) attachInfo.mWindowTop), this.mLightZ, this.mLightRadius);
    }

    /* Access modifiers changed, original: 0000 */
    public int getWidth() {
        return this.mWidth;
    }

    /* Access modifiers changed, original: 0000 */
    public int getHeight() {
        return this.mHeight;
    }

    /* Access modifiers changed, original: 0000 */
    public void dumpGfxInfo(PrintWriter pw, FileDescriptor fd, String[] args) {
        pw.flush();
        int flags = (args == null || args.length == 0) ? 1 : 0;
        for (String str : args) {
            Object obj = -1;
            int hashCode = str.hashCode();
            if (hashCode != -252053678) {
                if (hashCode != 1492) {
                    if (hashCode == 108404047 && str.equals("reset")) {
                        obj = 1;
                    }
                } else if (str.equals("-a")) {
                    obj = 2;
                }
            } else if (str.equals("framestats")) {
                obj = null;
            }
            if (obj == null) {
                flags |= 1;
            } else if (obj == 1) {
                flags |= 2;
            } else if (obj == 2) {
                flags = 1;
            }
        }
        dumpProfileInfo(fd, flags);
    }

    /* Access modifiers changed, original: 0000 */
    public Picture captureRenderingCommands() {
        return null;
    }

    public boolean loadSystemProperties() {
        boolean changed = super.loadSystemProperties();
        if (changed) {
            invalidateRoot();
        }
        return changed;
    }

    private void updateViewTreeDisplayList(View view) {
        view.mPrivateFlags |= 32;
        view.mRecreateDisplayList = (view.mPrivateFlags & Integer.MIN_VALUE) == Integer.MIN_VALUE;
        view.mPrivateFlags &= Integer.MAX_VALUE;
        view.updateDisplayListIfDirty();
        view.mRecreateDisplayList = false;
    }

    private void updateRootDisplayList(View view, DrawCallbacks callbacks) {
        Trace.traceBegin(8, "Record View#draw()");
        updateViewTreeDisplayList(view);
        FrameDrawingCallback callback = this.mNextRtFrameCallback;
        this.mNextRtFrameCallback = null;
        if (callback != null) {
            setFrameCallback(callback);
        }
        if (this.mRootNodeNeedsUpdate || !this.mRootNode.hasDisplayList()) {
            RecordingCanvas canvas = this.mRootNode.beginRecording(this.mSurfaceWidth, this.mSurfaceHeight);
            try {
                int saveCount = canvas.save();
                canvas.translate((float) this.mInsetLeft, (float) this.mInsetTop);
                callbacks.onPreDraw(canvas);
                canvas.enableZ();
                canvas.drawRenderNode(view.updateDisplayListIfDirty());
                canvas.disableZ();
                callbacks.onPostDraw(canvas);
                canvas.restoreToCount(saveCount);
                this.mRootNodeNeedsUpdate = false;
            } finally {
                this.mRootNode.endRecording();
            }
        }
        Trace.traceEnd(8);
    }

    /* Access modifiers changed, original: 0000 */
    public void invalidateRoot() {
        this.mRootNodeNeedsUpdate = true;
    }

    /* Access modifiers changed, original: 0000 */
    public void draw(View view, AttachInfo attachInfo, DrawCallbacks callbacks) {
        int count;
        Choreographer choreographer = attachInfo.mViewRootImpl.mChoreographer;
        choreographer.mFrameInfo.markDrawStart();
        updateRootDisplayList(view, callbacks);
        if (attachInfo.mPendingAnimatingRenderNodes != null) {
            count = attachInfo.mPendingAnimatingRenderNodes.size();
            for (int i = 0; i < count; i++) {
                registerAnimatingRenderNode((RenderNode) attachInfo.mPendingAnimatingRenderNodes.get(i));
            }
            attachInfo.mPendingAnimatingRenderNodes.clear();
            attachInfo.mPendingAnimatingRenderNodes = null;
        }
        count = syncAndDrawFrame(choreographer.mFrameInfo);
        if ((count & 2) != 0) {
            setEnabled(false);
            attachInfo.mViewRootImpl.mSurface.release();
            attachInfo.mViewRootImpl.invalidate();
        }
        if ((count & 1) != 0) {
            attachInfo.mViewRootImpl.invalidate();
        }
    }

    public RenderNode getRootNode() {
        return this.mRootNode;
    }

    public int getInsetTop() {
        return this.mInsetTop;
    }

    public int getInsetLeft() {
        return this.mInsetLeft;
    }
}
