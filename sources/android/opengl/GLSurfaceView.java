package android.opengl;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback2;
import android.view.SurfaceView;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class GLSurfaceView extends SurfaceView implements Callback2 {
    public static final int DEBUG_CHECK_GL_ERROR = 1;
    public static final int DEBUG_LOG_GL_CALLS = 2;
    private static final boolean LOG_ATTACH_DETACH = false;
    private static final boolean LOG_EGL = false;
    private static final boolean LOG_PAUSE_RESUME = false;
    private static final boolean LOG_RENDERER = false;
    private static final boolean LOG_RENDERER_DRAW_FRAME = false;
    private static final boolean LOG_SURFACE = false;
    private static final boolean LOG_THREADS = false;
    public static final int RENDERMODE_CONTINUOUSLY = 1;
    public static final int RENDERMODE_WHEN_DIRTY = 0;
    private static final String TAG = "GLSurfaceView";
    private static final GLThreadManager sGLThreadManager = new GLThreadManager();
    private int mDebugFlags;
    private boolean mDetached;
    private EGLConfigChooser mEGLConfigChooser;
    private int mEGLContextClientVersion;
    private EGLContextFactory mEGLContextFactory;
    private EGLWindowSurfaceFactory mEGLWindowSurfaceFactory;
    @UnsupportedAppUsage
    private GLThread mGLThread;
    private GLWrapper mGLWrapper;
    private boolean mPreserveEGLContextOnPause;
    @UnsupportedAppUsage
    private Renderer mRenderer;
    private final WeakReference<GLSurfaceView> mThisWeakRef = new WeakReference(this);

    public interface EGLConfigChooser {
        EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay);
    }

    private abstract class BaseConfigChooser implements EGLConfigChooser {
        protected int[] mConfigSpec;

        public abstract EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr);

        public BaseConfigChooser(int[] configSpec) {
            this.mConfigSpec = filterConfigSpec(configSpec);
        }

        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
            int[] num_config = new int[1];
            if (egl.eglChooseConfig(display, this.mConfigSpec, null, 0, num_config)) {
                int numConfigs = num_config[0];
                if (numConfigs > 0) {
                    EGLConfig[] configs = new EGLConfig[numConfigs];
                    if (egl.eglChooseConfig(display, this.mConfigSpec, configs, numConfigs, num_config)) {
                        EGLConfig config = chooseConfig(egl, display, configs);
                        if (config != null) {
                            return config;
                        }
                        throw new IllegalArgumentException("No config chosen");
                    }
                    throw new IllegalArgumentException("eglChooseConfig#2 failed");
                }
                throw new IllegalArgumentException("No configs match configSpec");
            }
            throw new IllegalArgumentException("eglChooseConfig failed");
        }

        private int[] filterConfigSpec(int[] configSpec) {
            if (GLSurfaceView.this.mEGLContextClientVersion != 2 && GLSurfaceView.this.mEGLContextClientVersion != 3) {
                return configSpec;
            }
            int len = configSpec.length;
            int[] newConfigSpec = new int[(len + 2)];
            System.arraycopy(configSpec, 0, newConfigSpec, 0, len - 1);
            newConfigSpec[len - 1] = 12352;
            if (GLSurfaceView.this.mEGLContextClientVersion == 2) {
                newConfigSpec[len] = 4;
            } else {
                newConfigSpec[len] = 64;
            }
            newConfigSpec[len + 1] = 12344;
            return newConfigSpec;
        }
    }

    private class ComponentSizeChooser extends BaseConfigChooser {
        protected int mAlphaSize;
        protected int mBlueSize;
        protected int mDepthSize;
        protected int mGreenSize;
        protected int mRedSize;
        protected int mStencilSize;
        private int[] mValue = new int[1];

        public ComponentSizeChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize) {
            super(new int[]{12324, redSize, 12323, greenSize, 12322, blueSize, 12321, alphaSize, 12325, depthSize, 12326, stencilSize, 12344});
            this.mRedSize = redSize;
            this.mGreenSize = greenSize;
            this.mBlueSize = blueSize;
            this.mAlphaSize = alphaSize;
            this.mDepthSize = depthSize;
            this.mStencilSize = stencilSize;
        }

        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
            for (EGLConfig config : configs) {
                EGL10 egl10 = egl;
                EGLDisplay eGLDisplay = display;
                EGLConfig eGLConfig = config;
                int d = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12325, 0);
                int s = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12326, 0);
                if (d >= this.mDepthSize && s >= this.mStencilSize) {
                    egl10 = egl;
                    eGLDisplay = display;
                    eGLConfig = config;
                    int r = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12324, 0);
                    int g = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12323, 0);
                    int b = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12322, 0);
                    int thisR = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12321, 0);
                    if (r == this.mRedSize && g == this.mGreenSize && b == this.mBlueSize && thisR == this.mAlphaSize) {
                        return config;
                    }
                }
            }
            return null;
        }

        private int findConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue) {
            if (egl.eglGetConfigAttrib(display, config, attribute, this.mValue)) {
                return this.mValue[0];
            }
            return defaultValue;
        }
    }

    public interface EGLContextFactory {
        EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig);

        void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext);
    }

    private class DefaultContextFactory implements EGLContextFactory {
        private int EGL_CONTEXT_CLIENT_VERSION;

        private DefaultContextFactory() {
            this.EGL_CONTEXT_CLIENT_VERSION = 12440;
        }

        public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig config) {
            return egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT, GLSurfaceView.this.mEGLContextClientVersion != 0 ? new int[]{this.EGL_CONTEXT_CLIENT_VERSION, GLSurfaceView.this.mEGLContextClientVersion, 12344} : null);
        }

        public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
            if (!egl.eglDestroyContext(display, context)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("display:");
                stringBuilder.append(display);
                stringBuilder.append(" context: ");
                stringBuilder.append(context);
                Log.e("DefaultContextFactory", stringBuilder.toString());
                EglHelper.throwEglException("eglDestroyContex", egl.eglGetError());
            }
        }
    }

    public interface EGLWindowSurfaceFactory {
        EGLSurface createWindowSurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj);

        void destroySurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface);
    }

    private static class DefaultWindowSurfaceFactory implements EGLWindowSurfaceFactory {
        private DefaultWindowSurfaceFactory() {
        }

        public EGLSurface createWindowSurface(EGL10 egl, EGLDisplay display, EGLConfig config, Object nativeWindow) {
            try {
                return egl.eglCreateWindowSurface(display, config, nativeWindow, null);
            } catch (IllegalArgumentException e) {
                Log.e(GLSurfaceView.TAG, "eglCreateWindowSurface", e);
                return null;
            }
        }

        public void destroySurface(EGL10 egl, EGLDisplay display, EGLSurface surface) {
            egl.eglDestroySurface(display, surface);
        }
    }

    private static class EglHelper {
        EGL10 mEgl;
        EGLConfig mEglConfig;
        @UnsupportedAppUsage
        EGLContext mEglContext;
        EGLDisplay mEglDisplay;
        EGLSurface mEglSurface;
        private WeakReference<GLSurfaceView> mGLSurfaceViewWeakRef;

        public EglHelper(WeakReference<GLSurfaceView> glSurfaceViewWeakRef) {
            this.mGLSurfaceViewWeakRef = glSurfaceViewWeakRef;
        }

        public void start() {
            this.mEgl = (EGL10) EGLContext.getEGL();
            this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (this.mEglDisplay != EGL10.EGL_NO_DISPLAY) {
                if (this.mEgl.eglInitialize(this.mEglDisplay, new int[2])) {
                    GLSurfaceView view = (GLSurfaceView) this.mGLSurfaceViewWeakRef.get();
                    if (view == null) {
                        this.mEglConfig = null;
                        this.mEglContext = null;
                    } else {
                        this.mEglConfig = view.mEGLConfigChooser.chooseConfig(this.mEgl, this.mEglDisplay);
                        this.mEglContext = view.mEGLContextFactory.createContext(this.mEgl, this.mEglDisplay, this.mEglConfig);
                    }
                    EGLContext eGLContext = this.mEglContext;
                    if (eGLContext == null || eGLContext == EGL10.EGL_NO_CONTEXT) {
                        this.mEglContext = null;
                        throwEglException("createContext");
                    }
                    this.mEglSurface = null;
                    return;
                }
                throw new RuntimeException("eglInitialize failed");
            }
            throw new RuntimeException("eglGetDisplay failed");
        }

        public boolean createSurface() {
            if (this.mEgl == null) {
                throw new RuntimeException("egl not initialized");
            } else if (this.mEglDisplay == null) {
                throw new RuntimeException("eglDisplay not initialized");
            } else if (this.mEglConfig != null) {
                destroySurfaceImp();
                GLSurfaceView view = (GLSurfaceView) this.mGLSurfaceViewWeakRef.get();
                if (view != null) {
                    this.mEglSurface = view.mEGLWindowSurfaceFactory.createWindowSurface(this.mEgl, this.mEglDisplay, this.mEglConfig, view.getHolder());
                } else {
                    this.mEglSurface = null;
                }
                EGLSurface eGLSurface = this.mEglSurface;
                if (eGLSurface == null || eGLSurface == EGL10.EGL_NO_SURFACE) {
                    if (this.mEgl.eglGetError() == 12299) {
                        Log.e("EglHelper", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
                    }
                    return false;
                }
                EGL10 egl10 = this.mEgl;
                EGLDisplay eGLDisplay = this.mEglDisplay;
                EGLSurface eGLSurface2 = this.mEglSurface;
                if (egl10.eglMakeCurrent(eGLDisplay, eGLSurface2, eGLSurface2, this.mEglContext)) {
                    return true;
                }
                logEglErrorAsWarning("EGLHelper", "eglMakeCurrent", this.mEgl.eglGetError());
                return false;
            } else {
                throw new RuntimeException("mEglConfig not initialized");
            }
        }

        /* Access modifiers changed, original: 0000 */
        public GL createGL() {
            GL gl = this.mEglContext.getGL();
            GLSurfaceView view = (GLSurfaceView) this.mGLSurfaceViewWeakRef.get();
            if (view == null) {
                return gl;
            }
            if (view.mGLWrapper != null) {
                gl = view.mGLWrapper.wrap(gl);
            }
            if ((view.mDebugFlags & 3) == 0) {
                return gl;
            }
            int configFlags = 0;
            Writer log = null;
            if ((view.mDebugFlags & 1) != 0) {
                configFlags = 0 | 1;
            }
            if ((view.mDebugFlags & 2) != 0) {
                log = new LogWriter();
            }
            return GLDebugHelper.wrap(gl, configFlags, log);
        }

        public int swap() {
            if (this.mEgl.eglSwapBuffers(this.mEglDisplay, this.mEglSurface)) {
                return 12288;
            }
            return this.mEgl.eglGetError();
        }

        public void destroySurface() {
            destroySurfaceImp();
        }

        private void destroySurfaceImp() {
            EGLSurface eGLSurface = this.mEglSurface;
            if (eGLSurface != null && eGLSurface != EGL10.EGL_NO_SURFACE) {
                this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                GLSurfaceView view = (GLSurfaceView) this.mGLSurfaceViewWeakRef.get();
                if (view != null) {
                    view.mEGLWindowSurfaceFactory.destroySurface(this.mEgl, this.mEglDisplay, this.mEglSurface);
                }
                this.mEglSurface = null;
            }
        }

        public void finish() {
            if (this.mEglContext != null) {
                GLSurfaceView view = (GLSurfaceView) this.mGLSurfaceViewWeakRef.get();
                if (view != null) {
                    view.mEGLContextFactory.destroyContext(this.mEgl, this.mEglDisplay, this.mEglContext);
                }
                this.mEglContext = null;
            }
            EGLDisplay eGLDisplay = this.mEglDisplay;
            if (eGLDisplay != null) {
                this.mEgl.eglTerminate(eGLDisplay);
                this.mEglDisplay = null;
            }
        }

        private void throwEglException(String function) {
            throwEglException(function, this.mEgl.eglGetError());
        }

        public static void throwEglException(String function, int error) {
            throw new RuntimeException(formatEglError(function, error));
        }

        public static void logEglErrorAsWarning(String tag, String function, int error) {
            Log.w(tag, formatEglError(function, error));
        }

        public static String formatEglError(String function, int error) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(function);
            stringBuilder.append(" failed: ");
            stringBuilder.append(EGLLogWrapper.getErrorString(error));
            return stringBuilder.toString();
        }
    }

    static class GLThread extends Thread {
        @UnsupportedAppUsage
        private EglHelper mEglHelper;
        private ArrayList<Runnable> mEventQueue = new ArrayList();
        private boolean mExited;
        private Runnable mFinishDrawingRunnable = null;
        private boolean mFinishedCreatingEglSurface;
        private WeakReference<GLSurfaceView> mGLSurfaceViewWeakRef;
        private boolean mHasSurface;
        private boolean mHaveEglContext;
        private boolean mHaveEglSurface;
        private int mHeight = 0;
        private boolean mPaused;
        private boolean mRenderComplete;
        private int mRenderMode = 1;
        private boolean mRequestPaused;
        private boolean mRequestRender = true;
        private boolean mShouldExit;
        private boolean mShouldReleaseEglContext;
        private boolean mSizeChanged = true;
        private boolean mSurfaceIsBad;
        private boolean mWaitingForSurface;
        private boolean mWantRenderNotification = false;
        private int mWidth = 0;

        GLThread(WeakReference<GLSurfaceView> glSurfaceViewWeakRef) {
            this.mGLSurfaceViewWeakRef = glSurfaceViewWeakRef;
        }

        public void run() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("GLThread ");
            stringBuilder.append(getId());
            setName(stringBuilder.toString());
            try {
                guardedRun();
            } catch (InterruptedException e) {
            } catch (Throwable th) {
                GLSurfaceView.sGLThreadManager.threadExiting(this);
            }
            GLSurfaceView.sGLThreadManager.threadExiting(this);
        }

        private void stopEglSurfaceLocked() {
            if (this.mHaveEglSurface) {
                this.mHaveEglSurface = false;
                this.mEglHelper.destroySurface();
            }
        }

        private void stopEglContextLocked() {
            if (this.mHaveEglContext) {
                this.mEglHelper.finish();
                this.mHaveEglContext = false;
                GLSurfaceView.sGLThreadManager.releaseEglContextLocked(this);
            }
        }

        /* JADX WARNING: Missing block: B:89:0x014b, code skipped:
            if (r13 == null) goto L_0x0154;
     */
        /* JADX WARNING: Missing block: B:91:?, code skipped:
            r13.run();
            r13 = null;
     */
        /* JADX WARNING: Missing block: B:92:0x0154, code skipped:
            if (r4 == false) goto L_0x018d;
     */
        /* JADX WARNING: Missing block: B:94:0x015c, code skipped:
            if (r1.mEglHelper.createSurface() == false) goto L_0x0174;
     */
        /* JADX WARNING: Missing block: B:95:0x015e, code skipped:
            r14 = android.opengl.GLSurfaceView.access$800();
     */
        /* JADX WARNING: Missing block: B:96:0x0162, code skipped:
            monitor-enter(r14);
     */
        /* JADX WARNING: Missing block: B:99:?, code skipped:
            r1.mFinishedCreatingEglSurface = true;
            android.opengl.GLSurfaceView.access$800().notifyAll();
     */
        /* JADX WARNING: Missing block: B:100:0x016d, code skipped:
            monitor-exit(r14);
     */
        /* JADX WARNING: Missing block: B:101:0x016e, code skipped:
            r4 = false;
     */
        /* JADX WARNING: Missing block: B:106:0x0174, code skipped:
            r14 = android.opengl.GLSurfaceView.access$800();
     */
        /* JADX WARNING: Missing block: B:107:0x0178, code skipped:
            monitor-enter(r14);
     */
        /* JADX WARNING: Missing block: B:110:?, code skipped:
            r1.mFinishedCreatingEglSurface = true;
            r1.mSurfaceIsBad = true;
            android.opengl.GLSurfaceView.access$800().notifyAll();
     */
        /* JADX WARNING: Missing block: B:111:0x0185, code skipped:
            monitor-exit(r14);
     */
        /* JADX WARNING: Missing block: B:117:0x018d, code skipped:
            if (r5 == false) goto L_0x019a;
     */
        /* JADX WARNING: Missing block: B:118:0x018f, code skipped:
            r5 = false;
            r2 = (javax.microedition.khronos.opengles.GL10) r1.mEglHelper.createGL();
     */
        /* JADX WARNING: Missing block: B:119:0x019a, code skipped:
            r14 = r4;
            r16 = r5;
     */
        /* JADX WARNING: Missing block: B:120:0x019f, code skipped:
            if (r3 == false) goto L_0x01cd;
     */
        /* JADX WARNING: Missing block: B:122:0x01a9, code skipped:
            r17 = (android.opengl.GLSurfaceView) r1.mGLSurfaceViewWeakRef.get();
     */
        /* JADX WARNING: Missing block: B:123:0x01ab, code skipped:
            if (r17 == null) goto L_0x01cb;
     */
        /* JADX WARNING: Missing block: B:125:?, code skipped:
            android.os.Trace.traceBegin(8, "onSurfaceCreated");
            android.opengl.GLSurfaceView.access$1000(r17).onSurfaceCreated(r2, r1.mEglHelper.mEglConfig);
     */
        /* JADX WARNING: Missing block: B:128:?, code skipped:
            android.os.Trace.traceEnd(8);
     */
        /* JADX WARNING: Missing block: B:132:0x01cb, code skipped:
            r3 = false;
     */
        /* JADX WARNING: Missing block: B:133:0x01cd, code skipped:
            if (r7 == false) goto L_0x01ff;
     */
        /* JADX WARNING: Missing block: B:135:0x01d7, code skipped:
            r4 = (android.opengl.GLSurfaceView) r1.mGLSurfaceViewWeakRef.get();
     */
        /* JADX WARNING: Missing block: B:136:0x01d8, code skipped:
            if (r4 == null) goto L_0x01fa;
     */
        /* JADX WARNING: Missing block: B:139:0x01dd, code skipped:
            r17 = r6;
     */
        /* JADX WARNING: Missing block: B:141:?, code skipped:
            android.os.Trace.traceBegin(8, "onSurfaceChanged");
            android.opengl.GLSurfaceView.access$1000(r4).onSurfaceChanged(r2, r11, r12);
     */
        /* JADX WARNING: Missing block: B:143:?, code skipped:
            android.os.Trace.traceEnd(8);
     */
        /* JADX WARNING: Missing block: B:144:0x01ef, code skipped:
            r0 = th;
     */
        /* JADX WARNING: Missing block: B:146:0x01f1, code skipped:
            r0 = th;
     */
        /* JADX WARNING: Missing block: B:147:0x01f2, code skipped:
            r17 = r6;
     */
        /* JADX WARNING: Missing block: B:148:0x01f4, code skipped:
            android.os.Trace.traceEnd(8);
     */
        /* JADX WARNING: Missing block: B:149:0x01f9, code skipped:
            throw r0;
     */
        /* JADX WARNING: Missing block: B:150:0x01fa, code skipped:
            r17 = r6;
     */
        /* JADX WARNING: Missing block: B:151:0x01fc, code skipped:
            r7 = false;
     */
        /* JADX WARNING: Missing block: B:152:0x01ff, code skipped:
            r17 = r6;
     */
        /* JADX WARNING: Missing block: B:154:0x0209, code skipped:
            r4 = (android.opengl.GLSurfaceView) r1.mGLSurfaceViewWeakRef.get();
     */
        /* JADX WARNING: Missing block: B:155:0x020a, code skipped:
            if (r4 == null) goto L_0x023a;
     */
        /* JADX WARNING: Missing block: B:157:?, code skipped:
            android.os.Trace.traceBegin(8, "onDrawFrame");
     */
        /* JADX WARNING: Missing block: B:158:0x0216, code skipped:
            if (r1.mFinishDrawingRunnable == null) goto L_0x021f;
     */
        /* JADX WARNING: Missing block: B:159:0x0218, code skipped:
            r15 = r1.mFinishDrawingRunnable;
            r0 = null;
            r1.mFinishDrawingRunnable = null;
     */
        /* JADX WARNING: Missing block: B:160:0x021f, code skipped:
            r0 = null;
     */
        /* JADX WARNING: Missing block: B:161:0x0220, code skipped:
            android.opengl.GLSurfaceView.access$1000(r4).onDrawFrame(r2);
     */
        /* JADX WARNING: Missing block: B:162:0x0227, code skipped:
            if (r15 == null) goto L_0x022d;
     */
        /* JADX WARNING: Missing block: B:163:0x0229, code skipped:
            r15.run();
     */
        /* JADX WARNING: Missing block: B:164:0x022c, code skipped:
            r15 = null;
     */
        /* JADX WARNING: Missing block: B:167:?, code skipped:
            android.os.Trace.traceEnd(8);
     */
        /* JADX WARNING: Missing block: B:171:0x023a, code skipped:
            r0 = null;
     */
        /* JADX WARNING: Missing block: B:172:0x023b, code skipped:
            r4 = r1.mEglHelper.swap();
     */
        /* JADX WARNING: Missing block: B:173:0x0243, code skipped:
            if (r4 == 12288) goto L_0x0268;
     */
        /* JADX WARNING: Missing block: B:175:0x0247, code skipped:
            if (r4 == 12302) goto L_0x0264;
     */
        /* JADX WARNING: Missing block: B:176:0x0249, code skipped:
            android.opengl.GLSurfaceView.EglHelper.logEglErrorAsWarning("GLThread", "eglSwapBuffers", r4);
            r5 = android.opengl.GLSurfaceView.access$800();
     */
        /* JADX WARNING: Missing block: B:177:0x0254, code skipped:
            monitor-enter(r5);
     */
        /* JADX WARNING: Missing block: B:180:?, code skipped:
            r1.mSurfaceIsBad = true;
            android.opengl.GLSurfaceView.access$800().notifyAll();
     */
        /* JADX WARNING: Missing block: B:181:0x025f, code skipped:
            monitor-exit(r5);
     */
        /* JADX WARNING: Missing block: B:187:0x0264, code skipped:
            r6 = true;
     */
        /* JADX WARNING: Missing block: B:188:0x0268, code skipped:
            r6 = r17;
     */
        /* JADX WARNING: Missing block: B:189:0x026a, code skipped:
            if (r8 == false) goto L_0x026f;
     */
        /* JADX WARNING: Missing block: B:190:0x026c, code skipped:
            r8 = false;
            r9 = true;
     */
        /* JADX WARNING: Missing block: B:191:0x026f, code skipped:
            r4 = r14;
            r5 = r16;
            r14 = r0;
     */
        private void guardedRun() throws java.lang.InterruptedException {
            /*
            r18 = this;
            r1 = r18;
            r0 = new android.opengl.GLSurfaceView$EglHelper;
            r2 = r1.mGLSurfaceViewWeakRef;
            r0.<init>(r2);
            r1.mEglHelper = r0;
            r0 = 0;
            r1.mHaveEglContext = r0;
            r1.mHaveEglSurface = r0;
            r1.mWantRenderNotification = r0;
            r2 = 0;
            r3 = 0;
            r4 = 0;
            r5 = 0;
            r6 = 0;
            r7 = 0;
            r8 = 0;
            r9 = 0;
            r10 = 0;
            r11 = 0;
            r12 = 0;
            r13 = 0;
            r14 = 0;
            r15 = r14;
        L_0x0020:
            r16 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0299 }
            monitor-enter(r16);	 Catch:{ all -> 0x0299 }
        L_0x0025:
            r14 = r1.mShouldExit;	 Catch:{ all -> 0x0296 }
            if (r14 == 0) goto L_0x003a;
        L_0x0029:
            monitor-exit(r16);	 Catch:{ all -> 0x0296 }
            r14 = android.opengl.GLSurfaceView.sGLThreadManager;
            monitor-enter(r14);
            r18.stopEglSurfaceLocked();	 Catch:{ all -> 0x0037 }
            r18.stopEglContextLocked();	 Catch:{ all -> 0x0037 }
            monitor-exit(r14);	 Catch:{ all -> 0x0037 }
            return;
        L_0x0037:
            r0 = move-exception;
            monitor-exit(r14);	 Catch:{ all -> 0x0037 }
            throw r0;
        L_0x003a:
            r14 = r1.mEventQueue;	 Catch:{ all -> 0x0296 }
            r14 = r14.isEmpty();	 Catch:{ all -> 0x0296 }
            if (r14 != 0) goto L_0x0050;
        L_0x0042:
            r14 = r1.mEventQueue;	 Catch:{ all -> 0x0296 }
            r0 = 0;
            r14 = r14.remove(r0);	 Catch:{ all -> 0x0296 }
            r14 = (java.lang.Runnable) r14;	 Catch:{ all -> 0x0296 }
            r0 = r14;
            r13 = r0;
            r0 = 0;
            goto L_0x014a;
        L_0x0050:
            r0 = 0;
            r14 = r1.mPaused;	 Catch:{ all -> 0x0296 }
            r17 = r0;
            r0 = r1.mRequestPaused;	 Catch:{ all -> 0x0296 }
            if (r14 == r0) goto L_0x0068;
        L_0x0059:
            r0 = r1.mRequestPaused;	 Catch:{ all -> 0x0296 }
            r14 = r1.mRequestPaused;	 Catch:{ all -> 0x0296 }
            r1.mPaused = r14;	 Catch:{ all -> 0x0296 }
            r14 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0296 }
            r14.notifyAll();	 Catch:{ all -> 0x0296 }
            r17 = r0;
        L_0x0068:
            r0 = r1.mShouldReleaseEglContext;	 Catch:{ all -> 0x0296 }
            if (r0 == 0) goto L_0x0076;
        L_0x006c:
            r18.stopEglSurfaceLocked();	 Catch:{ all -> 0x0296 }
            r18.stopEglContextLocked();	 Catch:{ all -> 0x0296 }
            r0 = 0;
            r1.mShouldReleaseEglContext = r0;	 Catch:{ all -> 0x0296 }
            r10 = 1;
        L_0x0076:
            if (r6 == 0) goto L_0x0080;
        L_0x0078:
            r18.stopEglSurfaceLocked();	 Catch:{ all -> 0x0296 }
            r18.stopEglContextLocked();	 Catch:{ all -> 0x0296 }
            r0 = 0;
            r6 = r0;
        L_0x0080:
            if (r17 == 0) goto L_0x0089;
        L_0x0082:
            r0 = r1.mHaveEglSurface;	 Catch:{ all -> 0x0296 }
            if (r0 == 0) goto L_0x0089;
        L_0x0086:
            r18.stopEglSurfaceLocked();	 Catch:{ all -> 0x0296 }
        L_0x0089:
            if (r17 == 0) goto L_0x00a4;
        L_0x008b:
            r0 = r1.mHaveEglContext;	 Catch:{ all -> 0x0296 }
            if (r0 == 0) goto L_0x00a4;
        L_0x008f:
            r0 = r1.mGLSurfaceViewWeakRef;	 Catch:{ all -> 0x0296 }
            r0 = r0.get();	 Catch:{ all -> 0x0296 }
            r0 = (android.opengl.GLSurfaceView) r0;	 Catch:{ all -> 0x0296 }
            if (r0 != 0) goto L_0x009b;
        L_0x0099:
            r14 = 0;
            goto L_0x009f;
        L_0x009b:
            r14 = r0.mPreserveEGLContextOnPause;	 Catch:{ all -> 0x0296 }
        L_0x009f:
            if (r14 != 0) goto L_0x00a4;
        L_0x00a1:
            r18.stopEglContextLocked();	 Catch:{ all -> 0x0296 }
        L_0x00a4:
            r0 = r1.mHasSurface;	 Catch:{ all -> 0x0296 }
            if (r0 != 0) goto L_0x00c0;
        L_0x00a8:
            r0 = r1.mWaitingForSurface;	 Catch:{ all -> 0x0296 }
            if (r0 != 0) goto L_0x00c0;
        L_0x00ac:
            r0 = r1.mHaveEglSurface;	 Catch:{ all -> 0x0296 }
            if (r0 == 0) goto L_0x00b3;
        L_0x00b0:
            r18.stopEglSurfaceLocked();	 Catch:{ all -> 0x0296 }
        L_0x00b3:
            r0 = 1;
            r1.mWaitingForSurface = r0;	 Catch:{ all -> 0x0296 }
            r0 = 0;
            r1.mSurfaceIsBad = r0;	 Catch:{ all -> 0x0296 }
            r0 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0296 }
            r0.notifyAll();	 Catch:{ all -> 0x0296 }
        L_0x00c0:
            r0 = r1.mHasSurface;	 Catch:{ all -> 0x0296 }
            if (r0 == 0) goto L_0x00d2;
        L_0x00c4:
            r0 = r1.mWaitingForSurface;	 Catch:{ all -> 0x0296 }
            if (r0 == 0) goto L_0x00d2;
        L_0x00c8:
            r0 = 0;
            r1.mWaitingForSurface = r0;	 Catch:{ all -> 0x0296 }
            r0 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0296 }
            r0.notifyAll();	 Catch:{ all -> 0x0296 }
        L_0x00d2:
            if (r9 == 0) goto L_0x00e2;
        L_0x00d4:
            r0 = 0;
            r1.mWantRenderNotification = r0;	 Catch:{ all -> 0x0296 }
            r9 = 0;
            r0 = 1;
            r1.mRenderComplete = r0;	 Catch:{ all -> 0x0296 }
            r0 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0296 }
            r0.notifyAll();	 Catch:{ all -> 0x0296 }
        L_0x00e2:
            r0 = r1.mFinishDrawingRunnable;	 Catch:{ all -> 0x0296 }
            if (r0 == 0) goto L_0x00ec;
        L_0x00e6:
            r0 = r1.mFinishDrawingRunnable;	 Catch:{ all -> 0x0296 }
            r15 = r0;
            r0 = 0;
            r1.mFinishDrawingRunnable = r0;	 Catch:{ all -> 0x0296 }
        L_0x00ec:
            r0 = r18.readyToDraw();	 Catch:{ all -> 0x0296 }
            if (r0 == 0) goto L_0x027c;
        L_0x00f2:
            r0 = r1.mHaveEglContext;	 Catch:{ all -> 0x0296 }
            if (r0 != 0) goto L_0x0117;
        L_0x00f6:
            if (r10 == 0) goto L_0x00fb;
        L_0x00f8:
            r0 = 0;
            r10 = r0;
            goto L_0x0117;
        L_0x00fb:
            r0 = r1.mEglHelper;	 Catch:{ RuntimeException -> 0x010d }
            r0.start();	 Catch:{ RuntimeException -> 0x010d }
            r0 = 1;
            r1.mHaveEglContext = r0;	 Catch:{ all -> 0x0296 }
            r3 = 1;
            r0 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0296 }
            r0.notifyAll();	 Catch:{ all -> 0x0296 }
            goto L_0x0117;
        L_0x010d:
            r0 = move-exception;
            r14 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0296 }
            r14.releaseEglContextLocked(r1);	 Catch:{ all -> 0x0296 }
            throw r0;	 Catch:{ all -> 0x0296 }
        L_0x0117:
            r0 = r1.mHaveEglContext;	 Catch:{ all -> 0x0296 }
            if (r0 == 0) goto L_0x0125;
        L_0x011b:
            r0 = r1.mHaveEglSurface;	 Catch:{ all -> 0x0296 }
            if (r0 != 0) goto L_0x0125;
        L_0x011f:
            r0 = 1;
            r1.mHaveEglSurface = r0;	 Catch:{ all -> 0x0296 }
            r4 = 1;
            r5 = 1;
            r7 = 1;
        L_0x0125:
            r0 = r1.mHaveEglSurface;	 Catch:{ all -> 0x0296 }
            if (r0 == 0) goto L_0x027a;
        L_0x0129:
            r0 = r1.mSizeChanged;	 Catch:{ all -> 0x0296 }
            if (r0 == 0) goto L_0x013b;
        L_0x012d:
            r7 = 1;
            r0 = r1.mWidth;	 Catch:{ all -> 0x0296 }
            r11 = r0;
            r0 = r1.mHeight;	 Catch:{ all -> 0x0296 }
            r12 = r0;
            r0 = 1;
            r1.mWantRenderNotification = r0;	 Catch:{ all -> 0x0296 }
            r4 = 1;
            r0 = 0;
            r1.mSizeChanged = r0;	 Catch:{ all -> 0x0296 }
        L_0x013b:
            r0 = 0;
            r1.mRequestRender = r0;	 Catch:{ all -> 0x0296 }
            r14 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0296 }
            r14.notifyAll();	 Catch:{ all -> 0x0296 }
            r14 = r1.mWantRenderNotification;	 Catch:{ all -> 0x0296 }
            if (r14 == 0) goto L_0x014a;
        L_0x0149:
            r8 = 1;
        L_0x014a:
            monitor-exit(r16);	 Catch:{ all -> 0x0276 }
            if (r13 == 0) goto L_0x0154;
        L_0x014d:
            r13.run();	 Catch:{ all -> 0x0299 }
            r13 = 0;
            r14 = 0;
            goto L_0x0020;
        L_0x0154:
            if (r4 == 0) goto L_0x018d;
        L_0x0156:
            r14 = r1.mEglHelper;	 Catch:{ all -> 0x0299 }
            r14 = r14.createSurface();	 Catch:{ all -> 0x0299 }
            if (r14 == 0) goto L_0x0174;
        L_0x015e:
            r14 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0299 }
            monitor-enter(r14);	 Catch:{ all -> 0x0299 }
            r0 = 1;
            r1.mFinishedCreatingEglSurface = r0;	 Catch:{ all -> 0x0171 }
            r0 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0171 }
            r0.notifyAll();	 Catch:{ all -> 0x0171 }
            monitor-exit(r14);	 Catch:{ all -> 0x0171 }
            r0 = 0;
            r4 = r0;
            goto L_0x018d;
        L_0x0171:
            r0 = move-exception;
            monitor-exit(r14);	 Catch:{ all -> 0x0171 }
            throw r0;	 Catch:{ all -> 0x0299 }
        L_0x0174:
            r14 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0299 }
            monitor-enter(r14);	 Catch:{ all -> 0x0299 }
            r0 = 1;
            r1.mFinishedCreatingEglSurface = r0;	 Catch:{ all -> 0x018a }
            r1.mSurfaceIsBad = r0;	 Catch:{ all -> 0x018a }
            r0 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x018a }
            r0.notifyAll();	 Catch:{ all -> 0x018a }
            monitor-exit(r14);	 Catch:{ all -> 0x018a }
            r0 = 0;
            r14 = 0;
            goto L_0x0020;
        L_0x018a:
            r0 = move-exception;
            monitor-exit(r14);	 Catch:{ all -> 0x018a }
            throw r0;	 Catch:{ all -> 0x0299 }
        L_0x018d:
            if (r5 == 0) goto L_0x019a;
        L_0x018f:
            r0 = r1.mEglHelper;	 Catch:{ all -> 0x0299 }
            r0 = r0.createGL();	 Catch:{ all -> 0x0299 }
            r0 = (javax.microedition.khronos.opengles.GL10) r0;	 Catch:{ all -> 0x0299 }
            r2 = 0;
            r5 = r2;
            r2 = r0;
        L_0x019a:
            r14 = r4;
            r16 = r5;
            r4 = 8;
            if (r3 == 0) goto L_0x01cd;
        L_0x01a1:
            r0 = r1.mGLSurfaceViewWeakRef;	 Catch:{ all -> 0x0299 }
            r0 = r0.get();	 Catch:{ all -> 0x0299 }
            r0 = (android.opengl.GLSurfaceView) r0;	 Catch:{ all -> 0x0299 }
            r17 = r0;
            if (r17 == 0) goto L_0x01cb;
        L_0x01ad:
            r0 = "onSurfaceCreated";
            android.os.Trace.traceBegin(r4, r0);	 Catch:{ all -> 0x01c4 }
            r0 = r17.mRenderer;	 Catch:{ all -> 0x01c4 }
            r4 = r1.mEglHelper;	 Catch:{ all -> 0x01c4 }
            r4 = r4.mEglConfig;	 Catch:{ all -> 0x01c4 }
            r0.onSurfaceCreated(r2, r4);	 Catch:{ all -> 0x01c4 }
            r4 = 8;
            android.os.Trace.traceEnd(r4);	 Catch:{ all -> 0x0299 }
            goto L_0x01cb;
        L_0x01c4:
            r0 = move-exception;
            r4 = 8;
            android.os.Trace.traceEnd(r4);	 Catch:{ all -> 0x0299 }
            throw r0;	 Catch:{ all -> 0x0299 }
        L_0x01cb:
            r0 = 0;
            r3 = r0;
        L_0x01cd:
            if (r7 == 0) goto L_0x01ff;
        L_0x01cf:
            r0 = r1.mGLSurfaceViewWeakRef;	 Catch:{ all -> 0x0299 }
            r0 = r0.get();	 Catch:{ all -> 0x0299 }
            r0 = (android.opengl.GLSurfaceView) r0;	 Catch:{ all -> 0x0299 }
            r4 = r0;
            if (r4 == 0) goto L_0x01fa;
        L_0x01da:
            r0 = "onSurfaceChanged";
            r17 = r6;
            r5 = 8;
            android.os.Trace.traceBegin(r5, r0);	 Catch:{ all -> 0x01ef }
            r0 = r4.mRenderer;	 Catch:{ all -> 0x01ef }
            r0.onSurfaceChanged(r2, r11, r12);	 Catch:{ all -> 0x01ef }
            android.os.Trace.traceEnd(r5);	 Catch:{ all -> 0x0299 }
            goto L_0x01fc;
        L_0x01ef:
            r0 = move-exception;
            goto L_0x01f4;
        L_0x01f1:
            r0 = move-exception;
            r17 = r6;
        L_0x01f4:
            r5 = 8;
            android.os.Trace.traceEnd(r5);	 Catch:{ all -> 0x0299 }
            throw r0;	 Catch:{ all -> 0x0299 }
        L_0x01fa:
            r17 = r6;
        L_0x01fc:
            r0 = 0;
            r7 = r0;
            goto L_0x0201;
        L_0x01ff:
            r17 = r6;
        L_0x0201:
            r0 = r1.mGLSurfaceViewWeakRef;	 Catch:{ all -> 0x0299 }
            r0 = r0.get();	 Catch:{ all -> 0x0299 }
            r0 = (android.opengl.GLSurfaceView) r0;	 Catch:{ all -> 0x0299 }
            r4 = r0;
            if (r4 == 0) goto L_0x023a;
        L_0x020c:
            r0 = "onDrawFrame";
            r5 = 8;
            android.os.Trace.traceBegin(r5, r0);	 Catch:{ all -> 0x0233 }
            r0 = r1.mFinishDrawingRunnable;	 Catch:{ all -> 0x0233 }
            if (r0 == 0) goto L_0x021f;
        L_0x0218:
            r0 = r1.mFinishDrawingRunnable;	 Catch:{ all -> 0x0233 }
            r15 = r0;
            r0 = 0;
            r1.mFinishDrawingRunnable = r0;	 Catch:{ all -> 0x0233 }
            goto L_0x0220;
        L_0x021f:
            r0 = 0;
        L_0x0220:
            r5 = r4.mRenderer;	 Catch:{ all -> 0x0233 }
            r5.onDrawFrame(r2);	 Catch:{ all -> 0x0233 }
            if (r15 == 0) goto L_0x022d;
        L_0x0229:
            r15.run();	 Catch:{ all -> 0x0233 }
            r15 = 0;
        L_0x022d:
            r5 = 8;
            android.os.Trace.traceEnd(r5);	 Catch:{ all -> 0x0299 }
            goto L_0x023b;
        L_0x0233:
            r0 = move-exception;
            r5 = 8;
            android.os.Trace.traceEnd(r5);	 Catch:{ all -> 0x0299 }
            throw r0;	 Catch:{ all -> 0x0299 }
        L_0x023a:
            r0 = 0;
        L_0x023b:
            r4 = r1.mEglHelper;	 Catch:{ all -> 0x0299 }
            r4 = r4.swap();	 Catch:{ all -> 0x0299 }
            r5 = 12288; // 0x3000 float:1.7219E-41 double:6.071E-320;
            if (r4 == r5) goto L_0x0267;
        L_0x0245:
            r5 = 12302; // 0x300e float:1.7239E-41 double:6.078E-320;
            if (r4 == r5) goto L_0x0264;
        L_0x0249:
            r5 = "GLThread";
            r6 = "eglSwapBuffers";
            android.opengl.GLSurfaceView.EglHelper.logEglErrorAsWarning(r5, r6, r4);	 Catch:{ all -> 0x0299 }
            r5 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0299 }
            monitor-enter(r5);	 Catch:{ all -> 0x0299 }
            r6 = 1;
            r1.mSurfaceIsBad = r6;	 Catch:{ all -> 0x0261 }
            r6 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0261 }
            r6.notifyAll();	 Catch:{ all -> 0x0261 }
            monitor-exit(r5);	 Catch:{ all -> 0x0261 }
            goto L_0x0268;
        L_0x0261:
            r0 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x0261 }
            throw r0;	 Catch:{ all -> 0x0299 }
        L_0x0264:
            r5 = 1;
            r6 = r5;
            goto L_0x026a;
        L_0x0268:
            r6 = r17;
        L_0x026a:
            if (r8 == 0) goto L_0x026f;
        L_0x026c:
            r5 = 1;
            r8 = 0;
            r9 = r5;
        L_0x026f:
            r4 = r14;
            r5 = r16;
            r14 = r0;
            r0 = 0;
            goto L_0x0020;
        L_0x0276:
            r0 = move-exception;
            r17 = r6;
            goto L_0x0297;
        L_0x027a:
            r0 = 0;
            goto L_0x028b;
        L_0x027c:
            r0 = 0;
            if (r15 == 0) goto L_0x028b;
        L_0x027f:
            r14 = "GLSurfaceView";
            r0 = "Warning, !readyToDraw() but waiting for draw finished! Early reporting draw finished.";
            android.util.Log.w(r14, r0);	 Catch:{ all -> 0x0296 }
            r15.run();	 Catch:{ all -> 0x0296 }
            r0 = 0;
            r15 = r0;
        L_0x028b:
            r0 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0296 }
            r0.wait();	 Catch:{ all -> 0x0296 }
            r0 = 0;
            r14 = 0;
            goto L_0x0025;
        L_0x0296:
            r0 = move-exception;
        L_0x0297:
            monitor-exit(r16);	 Catch:{ all -> 0x0296 }
            throw r0;	 Catch:{ all -> 0x0299 }
        L_0x0299:
            r0 = move-exception;
            r2 = android.opengl.GLSurfaceView.sGLThreadManager;
            monitor-enter(r2);
            r18.stopEglSurfaceLocked();	 Catch:{ all -> 0x02a7 }
            r18.stopEglContextLocked();	 Catch:{ all -> 0x02a7 }
            monitor-exit(r2);	 Catch:{ all -> 0x02a7 }
            throw r0;
        L_0x02a7:
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x02a7 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.opengl.GLSurfaceView$GLThread.guardedRun():void");
        }

        public boolean ableToDraw() {
            return this.mHaveEglContext && this.mHaveEglSurface && readyToDraw();
        }

        private boolean readyToDraw() {
            return !this.mPaused && this.mHasSurface && !this.mSurfaceIsBad && this.mWidth > 0 && this.mHeight > 0 && (this.mRequestRender || this.mRenderMode == 1);
        }

        public void setRenderMode(int renderMode) {
            if (renderMode < 0 || renderMode > 1) {
                throw new IllegalArgumentException("renderMode");
            }
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mRenderMode = renderMode;
                GLSurfaceView.sGLThreadManager.notifyAll();
            }
        }

        public int getRenderMode() {
            int i;
            synchronized (GLSurfaceView.sGLThreadManager) {
                i = this.mRenderMode;
            }
            return i;
        }

        public void requestRender() {
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mRequestRender = true;
                GLSurfaceView.sGLThreadManager.notifyAll();
            }
        }

        public void requestRenderAndNotify(Runnable finishDrawing) {
            synchronized (GLSurfaceView.sGLThreadManager) {
                if (Thread.currentThread() == this) {
                    return;
                }
                this.mWantRenderNotification = true;
                this.mRequestRender = true;
                this.mRenderComplete = false;
                this.mFinishDrawingRunnable = finishDrawing;
                GLSurfaceView.sGLThreadManager.notifyAll();
            }
        }

        public void surfaceCreated() {
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mHasSurface = true;
                this.mFinishedCreatingEglSurface = false;
                GLSurfaceView.sGLThreadManager.notifyAll();
                while (this.mWaitingForSurface && !this.mFinishedCreatingEglSurface && !this.mExited) {
                    try {
                        GLSurfaceView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void surfaceDestroyed() {
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mHasSurface = false;
                GLSurfaceView.sGLThreadManager.notifyAll();
                while (!this.mWaitingForSurface && !this.mExited) {
                    try {
                        GLSurfaceView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void onPause() {
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mRequestPaused = true;
                GLSurfaceView.sGLThreadManager.notifyAll();
                while (!this.mExited && !this.mPaused) {
                    try {
                        GLSurfaceView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void onResume() {
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mRequestPaused = false;
                this.mRequestRender = true;
                this.mRenderComplete = false;
                GLSurfaceView.sGLThreadManager.notifyAll();
                while (!this.mExited && this.mPaused && !this.mRenderComplete) {
                    try {
                        GLSurfaceView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        /* JADX WARNING: Missing block: B:23:0x0044, code skipped:
            return;
     */
        public void onWindowResize(int r4, int r5) {
            /*
            r3 = this;
            r0 = android.opengl.GLSurfaceView.sGLThreadManager;
            monitor-enter(r0);
            r3.mWidth = r4;	 Catch:{ all -> 0x0045 }
            r3.mHeight = r5;	 Catch:{ all -> 0x0045 }
            r1 = 1;
            r3.mSizeChanged = r1;	 Catch:{ all -> 0x0045 }
            r3.mRequestRender = r1;	 Catch:{ all -> 0x0045 }
            r1 = 0;
            r3.mRenderComplete = r1;	 Catch:{ all -> 0x0045 }
            r1 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0045 }
            if (r1 != r3) goto L_0x0019;
        L_0x0017:
            monitor-exit(r0);	 Catch:{ all -> 0x0045 }
            return;
        L_0x0019:
            r1 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0045 }
            r1.notifyAll();	 Catch:{ all -> 0x0045 }
        L_0x0020:
            r1 = r3.mExited;	 Catch:{ all -> 0x0045 }
            if (r1 != 0) goto L_0x0043;
        L_0x0024:
            r1 = r3.mPaused;	 Catch:{ all -> 0x0045 }
            if (r1 != 0) goto L_0x0043;
        L_0x0028:
            r1 = r3.mRenderComplete;	 Catch:{ all -> 0x0045 }
            if (r1 != 0) goto L_0x0043;
        L_0x002c:
            r1 = r3.ableToDraw();	 Catch:{ all -> 0x0045 }
            if (r1 == 0) goto L_0x0043;
        L_0x0032:
            r1 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ InterruptedException -> 0x003a }
            r1.wait();	 Catch:{ InterruptedException -> 0x003a }
        L_0x0039:
            goto L_0x0020;
        L_0x003a:
            r1 = move-exception;
            r2 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0045 }
            r2.interrupt();	 Catch:{ all -> 0x0045 }
            goto L_0x0039;
        L_0x0043:
            monitor-exit(r0);	 Catch:{ all -> 0x0045 }
            return;
        L_0x0045:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0045 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.opengl.GLSurfaceView$GLThread.onWindowResize(int, int):void");
        }

        public void requestExitAndWait() {
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mShouldExit = true;
                GLSurfaceView.sGLThreadManager.notifyAll();
                while (!this.mExited) {
                    try {
                        GLSurfaceView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void requestReleaseEglContextLocked() {
            this.mShouldReleaseEglContext = true;
            GLSurfaceView.sGLThreadManager.notifyAll();
        }

        public void queueEvent(Runnable r) {
            if (r != null) {
                synchronized (GLSurfaceView.sGLThreadManager) {
                    this.mEventQueue.add(r);
                    GLSurfaceView.sGLThreadManager.notifyAll();
                }
                return;
            }
            throw new IllegalArgumentException("r must not be null");
        }
    }

    private static class GLThreadManager {
        private static String TAG = "GLThreadManager";

        private GLThreadManager() {
        }

        public synchronized void threadExiting(GLThread thread) {
            thread.mExited = true;
            notifyAll();
        }

        public void releaseEglContextLocked(GLThread thread) {
            notifyAll();
        }
    }

    public interface GLWrapper {
        GL wrap(GL gl);
    }

    static class LogWriter extends Writer {
        private StringBuilder mBuilder = new StringBuilder();

        LogWriter() {
        }

        public void close() {
            flushBuilder();
        }

        public void flush() {
            flushBuilder();
        }

        public void write(char[] buf, int offset, int count) {
            for (int i = 0; i < count; i++) {
                char c = buf[offset + i];
                if (c == 10) {
                    flushBuilder();
                } else {
                    this.mBuilder.append(c);
                }
            }
        }

        private void flushBuilder() {
            if (this.mBuilder.length() > 0) {
                Log.v(GLSurfaceView.TAG, this.mBuilder.toString());
                StringBuilder stringBuilder = this.mBuilder;
                stringBuilder.delete(0, stringBuilder.length());
            }
        }
    }

    public interface Renderer {
        void onDrawFrame(GL10 gl10);

        void onSurfaceChanged(GL10 gl10, int i, int i2);

        void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig);
    }

    private class SimpleEGLConfigChooser extends ComponentSizeChooser {
        public SimpleEGLConfigChooser(boolean withDepthBuffer) {
            super(8, 8, 8, 0, withDepthBuffer ? 16 : 0, 0);
        }
    }

    public GLSurfaceView(Context context) {
        super(context);
        init();
    }

    public GLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mGLThread != null) {
                this.mGLThread.requestExitAndWait();
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    private void init() {
        getHolder().addCallback(this);
    }

    public void setGLWrapper(GLWrapper glWrapper) {
        this.mGLWrapper = glWrapper;
    }

    public void setDebugFlags(int debugFlags) {
        this.mDebugFlags = debugFlags;
    }

    public int getDebugFlags() {
        return this.mDebugFlags;
    }

    public void setPreserveEGLContextOnPause(boolean preserveOnPause) {
        this.mPreserveEGLContextOnPause = preserveOnPause;
    }

    public boolean getPreserveEGLContextOnPause() {
        return this.mPreserveEGLContextOnPause;
    }

    public void setRenderer(Renderer renderer) {
        checkRenderThreadState();
        if (this.mEGLConfigChooser == null) {
            this.mEGLConfigChooser = new SimpleEGLConfigChooser(true);
        }
        if (this.mEGLContextFactory == null) {
            this.mEGLContextFactory = new DefaultContextFactory();
        }
        if (this.mEGLWindowSurfaceFactory == null) {
            this.mEGLWindowSurfaceFactory = new DefaultWindowSurfaceFactory();
        }
        this.mRenderer = renderer;
        this.mGLThread = new GLThread(this.mThisWeakRef);
        this.mGLThread.start();
    }

    public void setEGLContextFactory(EGLContextFactory factory) {
        checkRenderThreadState();
        this.mEGLContextFactory = factory;
    }

    public void setEGLWindowSurfaceFactory(EGLWindowSurfaceFactory factory) {
        checkRenderThreadState();
        this.mEGLWindowSurfaceFactory = factory;
    }

    public void setEGLConfigChooser(EGLConfigChooser configChooser) {
        checkRenderThreadState();
        this.mEGLConfigChooser = configChooser;
    }

    public void setEGLConfigChooser(boolean needDepth) {
        setEGLConfigChooser(new SimpleEGLConfigChooser(needDepth));
    }

    public void setEGLConfigChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize) {
        setEGLConfigChooser(new ComponentSizeChooser(redSize, greenSize, blueSize, alphaSize, depthSize, stencilSize));
    }

    public void setEGLContextClientVersion(int version) {
        checkRenderThreadState();
        this.mEGLContextClientVersion = version;
    }

    public void setRenderMode(int renderMode) {
        this.mGLThread.setRenderMode(renderMode);
    }

    public int getRenderMode() {
        return this.mGLThread.getRenderMode();
    }

    public void requestRender() {
        this.mGLThread.requestRender();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.mGLThread.surfaceCreated();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.mGLThread.surfaceDestroyed();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        this.mGLThread.onWindowResize(w, h);
    }

    public void surfaceRedrawNeededAsync(SurfaceHolder holder, Runnable finishDrawing) {
        GLThread gLThread = this.mGLThread;
        if (gLThread != null) {
            gLThread.requestRenderAndNotify(finishDrawing);
        }
    }

    @Deprecated
    public void surfaceRedrawNeeded(SurfaceHolder holder) {
    }

    public void onPause() {
        this.mGLThread.onPause();
    }

    public void onResume() {
        this.mGLThread.onResume();
    }

    public void queueEvent(Runnable r) {
        this.mGLThread.queueEvent(r);
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mDetached && this.mRenderer != null) {
            int renderMode = 1;
            GLThread gLThread = this.mGLThread;
            if (gLThread != null) {
                renderMode = gLThread.getRenderMode();
            }
            this.mGLThread = new GLThread(this.mThisWeakRef);
            if (renderMode != 1) {
                this.mGLThread.setRenderMode(renderMode);
            }
            this.mGLThread.start();
        }
        this.mDetached = false;
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        GLThread gLThread = this.mGLThread;
        if (gLThread != null) {
            gLThread.requestExitAndWait();
        }
        this.mDetached = true;
        super.onDetachedFromWindow();
    }

    private void checkRenderThreadState() {
        if (this.mGLThread != null) {
            throw new IllegalStateException("setRenderer has already been called for this instance.");
        }
    }
}
