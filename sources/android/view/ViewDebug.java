package android.view;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.HardwareRenderer;
import android.graphics.HardwareRenderer.PictureCapturedCallback;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RenderNode;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import miui.maml.util.net.SimpleRequest;

public class ViewDebug {
    private static final int CAPTURE_TIMEOUT = 4000;
    public static final boolean DEBUG_DRAG = false;
    public static final boolean DEBUG_POSITIONING = false;
    private static final String REMOTE_COMMAND_CAPTURE = "CAPTURE";
    private static final String REMOTE_COMMAND_CAPTURE_LAYERS = "CAPTURE_LAYERS";
    private static final String REMOTE_COMMAND_DUMP = "DUMP";
    private static final String REMOTE_COMMAND_DUMP_THEME = "DUMP_THEME";
    private static final String REMOTE_COMMAND_INVALIDATE = "INVALIDATE";
    private static final String REMOTE_COMMAND_OUTPUT_DISPLAYLIST = "OUTPUT_DISPLAYLIST";
    private static final String REMOTE_COMMAND_REQUEST_LAYOUT = "REQUEST_LAYOUT";
    private static final String REMOTE_PROFILE = "PROFILE";
    @Deprecated
    public static final boolean TRACE_HIERARCHY = false;
    @Deprecated
    public static final boolean TRACE_RECYCLER = false;
    private static HashMap<Class<?>, Field[]> mCapturedViewFieldsForClasses = null;
    private static HashMap<Class<?>, Method[]> mCapturedViewMethodsForClasses = null;
    private static HashMap<AccessibleObject, ExportedProperty> sAnnotations;
    private static HashMap<Class<?>, Field[]> sFieldsForClasses;
    private static HashMap<Class<?>, Method[]> sMethodsForClasses;

    interface ViewOperation {
        void run();

        void pre() {
        }
    }

    public interface CanvasProvider {
        Bitmap createBitmap();

        Canvas getCanvas(View view, int i, int i2);
    }

    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CapturedViewProperty {
        boolean retrieveReturn() default false;
    }

    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExportedProperty {
        String category() default "";

        boolean deepExport() default false;

        FlagToString[] flagMapping() default {};

        boolean formatToHexString() default false;

        boolean hasAdjacentMapping() default false;

        IntToString[] indexMapping() default {};

        IntToString[] mapping() default {};

        String prefix() default "";

        boolean resolveId() default false;
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface FlagToString {
        int equals();

        int mask();

        String name();

        boolean outputIf() default true;
    }

    public static class HardwareCanvasProvider implements CanvasProvider {
        private Picture mPicture;

        public Canvas getCanvas(View view, int width, int height) {
            this.mPicture = new Picture();
            return this.mPicture.beginRecording(width, height);
        }

        public Bitmap createBitmap() {
            this.mPicture.endRecording();
            return Bitmap.createBitmap(this.mPicture);
        }
    }

    public interface HierarchyHandler {
        void dumpViewHierarchyWithProperties(BufferedWriter bufferedWriter, int i);

        View findHierarchyView(String str, int i);
    }

    @Deprecated
    public enum HierarchyTraceType {
        INVALIDATE,
        INVALIDATE_CHILD,
        INVALIDATE_CHILD_IN_PARENT,
        REQUEST_LAYOUT,
        ON_LAYOUT,
        ON_MEASURE,
        DRAW,
        BUILD_CACHE
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface IntToString {
        int from();

        String to();
    }

    private static class PictureCallbackHandler implements AutoCloseable, PictureCapturedCallback, Runnable {
        private final Function<Picture, Boolean> mCallback;
        private final Executor mExecutor;
        private final ReentrantLock mLock;
        private final ArrayDeque<Picture> mQueue;
        private Thread mRenderThread;
        private final HardwareRenderer mRenderer;
        private boolean mStopListening;

        /* synthetic */ PictureCallbackHandler(HardwareRenderer x0, Function x1, Executor x2, AnonymousClass1 x3) {
            this(x0, x1, x2);
        }

        private PictureCallbackHandler(HardwareRenderer renderer, Function<Picture, Boolean> callback, Executor executor) {
            this.mLock = new ReentrantLock(false);
            this.mQueue = new ArrayDeque(3);
            this.mRenderer = renderer;
            this.mCallback = callback;
            this.mExecutor = executor;
            this.mRenderer.setPictureCaptureCallback(this);
        }

        public void close() {
            this.mLock.lock();
            this.mStopListening = true;
            this.mLock.unlock();
            this.mRenderer.setPictureCaptureCallback(null);
        }

        public void onPictureCaptured(Picture picture) {
            this.mLock.lock();
            if (this.mStopListening) {
                this.mLock.unlock();
                this.mRenderer.setPictureCaptureCallback(null);
                return;
            }
            if (this.mRenderThread == null) {
                this.mRenderThread = Thread.currentThread();
            }
            Picture toDestroy = null;
            if (this.mQueue.size() == 3) {
                toDestroy = (Picture) this.mQueue.removeLast();
            }
            this.mQueue.add(picture);
            this.mLock.unlock();
            if (toDestroy == null) {
                this.mExecutor.execute(this);
            } else {
                toDestroy.close();
            }
        }

        public void run() {
            this.mLock.lock();
            Picture picture = (Picture) this.mQueue.poll();
            boolean isStopped = this.mStopListening;
            this.mLock.unlock();
            if (Thread.currentThread() == this.mRenderThread) {
                close();
                throw new IllegalStateException("ViewDebug#startRenderingCommandsCapture must be given an executor that invokes asynchronously");
            } else if (isStopped) {
                picture.close();
            } else {
                if (!((Boolean) this.mCallback.apply(picture)).booleanValue()) {
                    close();
                }
            }
        }
    }

    @Deprecated
    public enum RecyclerTraceType {
        NEW_VIEW,
        BIND_VIEW,
        RECYCLE_FROM_ACTIVE_HEAP,
        RECYCLE_FROM_SCRAP_HEAP,
        MOVE_TO_SCRAP_HEAP,
        MOVE_FROM_ACTIVE_TO_SCRAP_HEAP
    }

    public static class SoftwareCanvasProvider implements CanvasProvider {
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private boolean mEnabledHwBitmapsInSwMode;

        public Canvas getCanvas(View view, int width, int height) {
            this.mBitmap = Bitmap.createBitmap(view.getResources().getDisplayMetrics(), width, height, Config.ARGB_8888);
            Bitmap bitmap = this.mBitmap;
            if (bitmap != null) {
                bitmap.setDensity(view.getResources().getDisplayMetrics().densityDpi);
                if (view.mAttachInfo != null) {
                    this.mCanvas = view.mAttachInfo.mCanvas;
                }
                if (this.mCanvas == null) {
                    this.mCanvas = new Canvas();
                }
                this.mEnabledHwBitmapsInSwMode = this.mCanvas.isHwBitmapsInSwModeEnabled();
                this.mCanvas.setBitmap(this.mBitmap);
                return this.mCanvas;
            }
            throw new OutOfMemoryError();
        }

        public Bitmap createBitmap() {
            this.mCanvas.setBitmap(null);
            this.mCanvas.setHwBitmapsInSwModeEnabled(this.mEnabledHwBitmapsInSwMode);
            return this.mBitmap;
        }
    }

    @UnsupportedAppUsage
    public static long getViewInstanceCount() {
        return Debug.countInstancesOfClass(View.class);
    }

    @UnsupportedAppUsage
    public static long getViewRootImplCount() {
        return Debug.countInstancesOfClass(ViewRootImpl.class);
    }

    @Deprecated
    public static void trace(View view, RecyclerTraceType type, int... parameters) {
    }

    @Deprecated
    public static void startRecyclerTracing(String prefix, View view) {
    }

    @Deprecated
    public static void stopRecyclerTracing() {
    }

    @Deprecated
    public static void trace(View view, HierarchyTraceType type) {
    }

    @Deprecated
    public static void startHierarchyTracing(String prefix, View view) {
    }

    @Deprecated
    public static void stopHierarchyTracing() {
    }

    @UnsupportedAppUsage
    static void dispatchCommand(View view, String command, String parameters, OutputStream clientStream) throws IOException {
        view = view.getRootView();
        if (REMOTE_COMMAND_DUMP.equalsIgnoreCase(command)) {
            dump(view, false, true, clientStream);
        } else if (REMOTE_COMMAND_DUMP_THEME.equalsIgnoreCase(command)) {
            dumpTheme(view, clientStream);
        } else if (REMOTE_COMMAND_CAPTURE_LAYERS.equalsIgnoreCase(command)) {
            captureLayers(view, new DataOutputStream(clientStream));
        } else {
            String[] params = parameters.split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            if (REMOTE_COMMAND_CAPTURE.equalsIgnoreCase(command)) {
                capture(view, clientStream, params[0]);
            } else if (REMOTE_COMMAND_OUTPUT_DISPLAYLIST.equalsIgnoreCase(command)) {
                outputDisplayList(view, params[0]);
            } else if (REMOTE_COMMAND_INVALIDATE.equalsIgnoreCase(command)) {
                invalidate(view, params[0]);
            } else if (REMOTE_COMMAND_REQUEST_LAYOUT.equalsIgnoreCase(command)) {
                requestLayout(view, params[0]);
            } else if (REMOTE_PROFILE.equalsIgnoreCase(command)) {
                profile(view, clientStream, params[0]);
            }
        }
    }

    public static View findView(View root, String parameter) {
        if (parameter.indexOf(64) != -1) {
            String[] ids = parameter.split("@");
            String className = ids[null];
            int hashCode = (int) Long.parseLong(ids[1], 16);
            View view = root.getRootView();
            if (view instanceof ViewGroup) {
                return findView((ViewGroup) view, className, hashCode);
            }
            return null;
        }
        return root.getRootView().findViewById(root.getResources().getIdentifier(parameter, null, null));
    }

    private static void invalidate(View root, String parameter) {
        View view = findView(root, parameter);
        if (view != null) {
            view.postInvalidate();
        }
    }

    private static void requestLayout(View root, String parameter) {
        final View view = findView(root, parameter);
        if (view != null) {
            root.post(new Runnable() {
                public void run() {
                    view.requestLayout();
                }
            });
        }
    }

    private static void profile(View root, OutputStream clientStream, String parameter) throws IOException {
        View view = findView(root, parameter);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(clientStream), 32768);
            if (view != null) {
                profileViewAndChildren(view, out);
            } else {
                out.write("-1 -1 -1");
                out.newLine();
            }
            out.write("DONE.");
            out.newLine();
        } catch (Exception e) {
            Log.w("View", "Problem profiling the view:", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static void profileViewAndChildren(View view, BufferedWriter out) throws IOException {
        profileViewAndChildren(view, RenderNode.create("ViewDebug", null), out, true);
    }

    private static void profileViewAndChildren(View view, RenderNode node, BufferedWriter out, boolean root) throws IOException {
        long durationDraw = 0;
        long durationMeasure = (root || (view.mPrivateFlags & 2048) != 0) ? profileViewMeasure(view) : 0;
        long durationLayout = (root || (view.mPrivateFlags & 8192) != 0) ? profileViewLayout(view) : 0;
        if (!(!root && view.willNotDraw() && (view.mPrivateFlags & 32) == 0)) {
            durationDraw = profileViewDraw(view, node);
        }
        out.write(String.valueOf(durationMeasure));
        out.write(32);
        out.write(String.valueOf(durationLayout));
        out.write(32);
        out.write(String.valueOf(durationDraw));
        out.newLine();
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                profileViewAndChildren(group.getChildAt(i), node, out, false);
            }
        }
    }

    private static long profileViewMeasure(final View view) {
        return profileViewOperation(view, new ViewOperation() {
            public void pre() {
                forceLayout(view);
            }

            private void forceLayout(View view) {
                view.forceLayout();
                if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    int count = group.getChildCount();
                    for (int i = 0; i < count; i++) {
                        forceLayout(group.getChildAt(i));
                    }
                }
            }

            public void run() {
                View view = view;
                view.measure(view.mOldWidthMeasureSpec, view.mOldHeightMeasureSpec);
            }
        });
    }

    private static long profileViewLayout(View view) {
        return profileViewOperation(view, new -$$Lambda$ViewDebug$inOytI2zZEgp1DJv8Cu4GjQVNiE(view));
    }

    private static long profileViewDraw(View view, RenderNode node) {
        DisplayMetrics dm = view.getResources().getDisplayMetrics();
        if (dm == null) {
            return 0;
        }
        if (view.isHardwareAccelerated()) {
            try {
                long profileViewOperation = profileViewOperation(view, new -$$Lambda$ViewDebug$flFXZc7_CjFXx7_tYT59WSbUNjI(view, node.beginRecording(dm.widthPixels, dm.heightPixels)));
                return profileViewOperation;
            } finally {
                node.endRecording();
            }
        } else {
            Bitmap bitmap = Bitmap.createBitmap(dm, dm.widthPixels, dm.heightPixels, Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            try {
                long profileViewOperation2 = profileViewOperation(view, new -$$Lambda$ViewDebug$w986pBwzwNi77yEgLa3IWusjPNw(view, canvas));
                return profileViewOperation2;
            } finally {
                canvas.setBitmap(null);
                bitmap.recycle();
            }
        }
    }

    private static long profileViewOperation(View view, ViewOperation operation) {
        String str = "Could not complete the profiling of the view ";
        String str2 = "View";
        CountDownLatch latch = new CountDownLatch(1);
        long[] duration = new long[1];
        view.post(new -$$Lambda$ViewDebug$5rTN0pemwbr3I3IL2E-xDBeDTDg(operation, duration, latch));
        try {
            if (latch.await(4000, TimeUnit.MILLISECONDS)) {
                return duration[0];
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(view);
            Log.w(str2, stringBuilder.toString());
            return -1;
        } catch (InterruptedException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append(view);
            Log.w(str2, stringBuilder2.toString());
            Thread.currentThread().interrupt();
            return -1;
        }
    }

    static /* synthetic */ void lambda$profileViewOperation$3(ViewOperation operation, long[] duration, CountDownLatch latch) {
        try {
            operation.pre();
            long start = Debug.threadCpuTimeNanos();
            operation.run();
            duration[0] = Debug.threadCpuTimeNanos() - start;
        } finally {
            latch.countDown();
        }
    }

    public static void captureLayers(View root, DataOutputStream clientStream) throws IOException {
        try {
            Rect outRect = new Rect();
            try {
                root.mAttachInfo.mSession.getDisplayFrame(root.mAttachInfo.mWindow, outRect);
            } catch (RemoteException e) {
            }
            clientStream.writeInt(outRect.width());
            clientStream.writeInt(outRect.height());
            captureViewLayer(root, clientStream, true);
            clientStream.write(2);
        } finally {
            clientStream.close();
        }
    }

    private static void captureViewLayer(View view, DataOutputStream clientStream, boolean visible) throws IOException {
        int id;
        boolean localVisible = view.getVisibility() == 0 && visible;
        if ((view.mPrivateFlags & 128) != 128) {
            id = view.getId();
            String name = view.getClass().getSimpleName();
            if (id != -1) {
                name = resolveId(view.getContext(), id).toString();
            }
            clientStream.write(1);
            clientStream.writeUTF(name);
            clientStream.writeByte(localVisible ? 1 : 0);
            int[] position = new int[2];
            view.getLocationInWindow(position);
            clientStream.writeInt(position[0]);
            clientStream.writeInt(position[1]);
            clientStream.flush();
            Bitmap b = performViewCapture(view, true);
            if (b != null) {
                ByteArrayOutputStream arrayOut = new ByteArrayOutputStream((b.getWidth() * b.getHeight()) * 2);
                b.compress(CompressFormat.PNG, 100, arrayOut);
                clientStream.writeInt(arrayOut.size());
                arrayOut.writeTo(clientStream);
            }
            clientStream.flush();
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (id = 0; id < count; id++) {
                captureViewLayer(group.getChildAt(id), clientStream, localVisible);
            }
        }
        if (view.mOverlay != null) {
            captureViewLayer(view.getOverlay().mOverlayViewGroup, clientStream, localVisible);
        }
    }

    private static void outputDisplayList(View root, String parameter) throws IOException {
        View view = findView(root, parameter);
        view.getViewRootImpl().outputDisplayList(view);
    }

    public static void outputDisplayList(View root, View target) {
        root.getViewRootImpl().outputDisplayList(target);
    }

    @Deprecated
    public static AutoCloseable startRenderingCommandsCapture(View tree, Executor executor, Function<Picture, Boolean> callback) {
        AttachInfo attachInfo = tree.mAttachInfo;
        if (attachInfo == null) {
            throw new IllegalArgumentException("Given view isn't attached");
        } else if (attachInfo.mHandler.getLooper() == Looper.myLooper()) {
            HardwareRenderer renderer = attachInfo.mThreadedRenderer;
            if (renderer != null) {
                return new PictureCallbackHandler(renderer, callback, executor, null);
            }
            return null;
        } else {
            throw new IllegalStateException("Called on the wrong thread. Must be called on the thread that owns the given View");
        }
    }

    public static AutoCloseable startRenderingCommandsCapture(View tree, Executor executor, Callable<OutputStream> callback) {
        AttachInfo attachInfo = tree.mAttachInfo;
        if (attachInfo == null) {
            throw new IllegalArgumentException("Given view isn't attached");
        } else if (attachInfo.mHandler.getLooper() == Looper.myLooper()) {
            HardwareRenderer renderer = attachInfo.mThreadedRenderer;
            if (renderer != null) {
                return new PictureCallbackHandler(renderer, new -$$Lambda$ViewDebug$hyDSYptlxuUTTyRIONqWzWWVDB0(callback), executor, null);
            }
            return null;
        } else {
            throw new IllegalStateException("Called on the wrong thread. Must be called on the thread that owns the given View");
        }
    }

    static /* synthetic */ Boolean lambda$startRenderingCommandsCapture$4(Callable callback, Picture picture) {
        try {
            OutputStream stream = (OutputStream) callback.call();
            if (stream != null) {
                picture.writeToStream(stream);
                return Boolean.valueOf(true);
            }
        } catch (Exception e) {
        }
        return Boolean.valueOf(false);
    }

    private static void capture(View root, OutputStream clientStream, String parameter) throws IOException {
        capture(root, clientStream, findView(root, parameter));
    }

    public static void capture(View root, OutputStream clientStream, View captureView) throws IOException {
        Bitmap b = performViewCapture(captureView, null);
        if (b == null) {
            Log.w("View", "Failed to create capture bitmap!");
            b = Bitmap.createBitmap(root.getResources().getDisplayMetrics(), 1, 1, Config.ARGB_8888);
        }
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(clientStream, 32768);
            b.compress(CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            b.recycle();
        } catch (Throwable th) {
            if (out != null) {
                out.close();
            }
            b.recycle();
        }
    }

    private static Bitmap performViewCapture(View captureView, boolean skipChildren) {
        if (captureView != null) {
            CountDownLatch latch = new CountDownLatch(1);
            Bitmap[] cache = new Bitmap[1];
            captureView.post(new -$$Lambda$ViewDebug$1iDmmthcZt_8LsYI6VndkxasPEs(captureView, cache, skipChildren, latch));
            try {
                latch.await(4000, TimeUnit.MILLISECONDS);
                return cache[0];
            } catch (InterruptedException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Could not complete the capture of the view ");
                stringBuilder.append(captureView);
                Log.w("View", stringBuilder.toString());
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }

    static /* synthetic */ void lambda$performViewCapture$5(View captureView, Bitmap[] cache, boolean skipChildren, CountDownLatch latch) {
        try {
            cache[0] = captureView.createSnapshot(captureView.isHardwareAccelerated() ? new HardwareCanvasProvider() : new SoftwareCanvasProvider(), skipChildren);
        } catch (OutOfMemoryError e) {
            Log.w("View", "Out of memory for bitmap");
        } catch (Throwable th) {
            latch.countDown();
        }
        latch.countDown();
    }

    @Deprecated
    @UnsupportedAppUsage
    public static void dump(View root, boolean skipChildren, boolean includeProperties, OutputStream clientStream) throws IOException {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(clientStream, SimpleRequest.UTF8), 32768);
            View view = root.getRootView();
            if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;
                dumpViewHierarchy(group.getContext(), group, out, 0, skipChildren, includeProperties);
            }
            out.write("DONE.");
            out.newLine();
        } catch (Exception e) {
            Log.w("View", "Problem dumping the view:", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static void dumpv2(final View view, ByteArrayOutputStream out) throws InterruptedException {
        final ViewHierarchyEncoder encoder = new ViewHierarchyEncoder(out);
        final CountDownLatch latch = new CountDownLatch(1);
        view.post(new Runnable() {
            public void run() {
                encoder.addProperty("window:left", view.mAttachInfo.mWindowLeft);
                encoder.addProperty("window:top", view.mAttachInfo.mWindowTop);
                view.encode(encoder);
                latch.countDown();
            }
        });
        latch.await(2, TimeUnit.SECONDS);
        encoder.endStream();
    }

    public static void dumpTheme(View view, OutputStream clientStream) throws IOException {
        String str = "\n";
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(clientStream, SimpleRequest.UTF8), 32768);
            String[] attributes = getStyleAttributesDump(view.getContext().getResources(), view.getContext().getTheme());
            if (attributes != null) {
                for (int i = 0; i < attributes.length; i += 2) {
                    if (attributes[i] != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(attributes[i]);
                        stringBuilder.append(str);
                        out.write(stringBuilder.toString());
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(attributes[i + 1]);
                        stringBuilder.append(str);
                        out.write(stringBuilder.toString());
                    }
                }
            }
            out.write("DONE.");
            out.newLine();
        } catch (Exception e) {
            Log.w("View", "Problem dumping View Theme:", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private static String[] getStyleAttributesDump(Resources resources, Theme theme) {
        TypedValue outValue = new TypedValue();
        String nullString = "null";
        int i = 0;
        int[] attributes = theme.getAllAttributes();
        String[] data = new String[(attributes.length * 2)];
        for (int attributeId : attributes) {
            try {
                data[i] = resources.getResourceName(attributeId);
                data[i + 1] = theme.resolveAttribute(attributeId, outValue, true) ? outValue.coerceToString().toString() : nullString;
                i += 2;
                if (outValue.type == 1) {
                    data[i - 1] = resources.getResourceName(outValue.resourceId);
                }
            } catch (NotFoundException e) {
            }
        }
        return data;
    }

    private static View findView(ViewGroup group, String className, int hashCode) {
        if (isRequestedView(group, className, hashCode)) {
            return group;
        }
        int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            View found;
            View view = group.getChildAt(i);
            if (view instanceof ViewGroup) {
                found = findView((ViewGroup) view, className, hashCode);
                if (found != null) {
                    return found;
                }
            } else if (isRequestedView(view, className, hashCode)) {
                return view;
            }
            if (view.mOverlay != null) {
                found = findView(view.mOverlay.mOverlayViewGroup, className, hashCode);
                if (found != null) {
                    return found;
                }
            }
            if (view instanceof HierarchyHandler) {
                found = ((HierarchyHandler) view).findHierarchyView(className, hashCode);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private static boolean isRequestedView(View view, String className, int hashCode) {
        if (view.hashCode() != hashCode) {
            return false;
        }
        String viewClassName = view.getClass().getName();
        if (className.equals("ViewOverlay")) {
            return viewClassName.equals("android.view.ViewOverlay$OverlayViewGroup");
        }
        return className.equals(viewClassName);
    }

    private static void dumpViewHierarchy(Context context, ViewGroup group, BufferedWriter out, int level, boolean skipChildren, boolean includeProperties) {
        Context context2 = context;
        View view = group;
        BufferedWriter bufferedWriter = out;
        int i = level;
        boolean z = includeProperties;
        if (dumpView(context2, view, bufferedWriter, i, z) && !skipChildren) {
            int count = group.getChildCount();
            for (int i2 = 0; i2 < count; i2++) {
                View view2 = view.getChildAt(i2);
                if (view2 instanceof ViewGroup) {
                    dumpViewHierarchy(context, (ViewGroup) view2, out, i + 1, skipChildren, includeProperties);
                } else {
                    dumpView(context2, view2, bufferedWriter, i + 1, z);
                }
                if (view2.mOverlay != null) {
                    dumpViewHierarchy(context, view2.getOverlay().mOverlayViewGroup, out, i + 2, skipChildren, includeProperties);
                }
            }
            if (view instanceof HierarchyHandler) {
                ((HierarchyHandler) view).dumpViewHierarchyWithProperties(bufferedWriter, i + 1);
            }
        }
    }

    private static boolean dumpView(Context context, View view, BufferedWriter out, int level, boolean includeProperties) {
        int i = 0;
        while (i < level) {
            try {
                out.write(32);
                i++;
            } catch (IOException e) {
                Log.w("View", "Error while dumping hierarchy tree");
                return false;
            }
        }
        String className = view.getClass().getName();
        if (className.equals("android.view.ViewOverlay$OverlayViewGroup")) {
            className = "ViewOverlay";
        }
        out.write(className);
        out.write(64);
        out.write(Integer.toHexString(view.hashCode()));
        out.write(32);
        if (includeProperties) {
            dumpViewProperties(context, view, out);
        }
        out.newLine();
        return true;
    }

    private static Field[] getExportedPropertyFields(Class<?> klass) {
        if (sFieldsForClasses == null) {
            sFieldsForClasses = new HashMap();
        }
        if (sAnnotations == null) {
            sAnnotations = new HashMap(512);
        }
        HashMap<Class<?>, Field[]> map = sFieldsForClasses;
        Field[] fields = (Field[]) map.get(klass);
        if (fields != null) {
            return fields;
        }
        int i = 0;
        try {
            Field[] declaredFields = klass.getDeclaredFieldsUnchecked(false);
            ArrayList<Field> foundFields = new ArrayList();
            int length = declaredFields.length;
            while (i < length) {
                Field field = declaredFields[i];
                if (field.getType() != null && field.isAnnotationPresent(ExportedProperty.class)) {
                    field.setAccessible(true);
                    foundFields.add(field);
                    sAnnotations.put(field, (ExportedProperty) field.getAnnotation(ExportedProperty.class));
                }
                i++;
            }
            fields = (Field[]) foundFields.toArray(new Field[foundFields.size()]);
            map.put(klass, fields);
            return fields;
        } catch (NoClassDefFoundError e) {
            throw new AssertionError(e);
        }
    }

    private static Method[] getExportedPropertyMethods(Class<?> klass) {
        if (sMethodsForClasses == null) {
            sMethodsForClasses = new HashMap(100);
        }
        if (sAnnotations == null) {
            sAnnotations = new HashMap(512);
        }
        HashMap<Class<?>, Method[]> map = sMethodsForClasses;
        Method[] methods = (Method[]) map.get(klass);
        if (methods != null) {
            return methods;
        }
        int i = 0;
        methods = klass.getDeclaredMethodsUnchecked(false);
        ArrayList<Method> foundMethods = new ArrayList();
        int length = methods.length;
        while (i < length) {
            Method method = methods[i];
            try {
                method.getReturnType();
                method.getParameterTypes();
                if (method.getParameterTypes().length == 0 && method.isAnnotationPresent(ExportedProperty.class) && method.getReturnType() != Void.class) {
                    method.setAccessible(true);
                    foundMethods.add(method);
                    sAnnotations.put(method, (ExportedProperty) method.getAnnotation(ExportedProperty.class));
                }
            } catch (NoClassDefFoundError e) {
            }
            i++;
        }
        methods = (Method[]) foundMethods.toArray(new Method[foundMethods.size()]);
        map.put(klass, methods);
        return methods;
    }

    private static void dumpViewProperties(Context context, Object view, BufferedWriter out) throws IOException {
        dumpViewProperties(context, view, out, "");
    }

    private static void dumpViewProperties(Context context, Object view, BufferedWriter out, String prefix) throws IOException {
        if (view == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("=4,null ");
            out.write(stringBuilder.toString());
            return;
        }
        Class<?> klass = view.getClass();
        while (true) {
            exportFields(context, view, out, klass, prefix);
            exportMethods(context, view, out, klass, prefix);
            klass = klass.getSuperclass();
            if (klass == Object.class) {
                return;
            }
        }
    }

    private static Object callMethodOnAppropriateTheadBlocking(final Method method, Object object) throws IllegalAccessException, InvocationTargetException, TimeoutException {
        if (!(object instanceof View)) {
            return method.invoke(object, (Object[]) null);
        }
        final View view = (View) object;
        FutureTask<Object> future = new FutureTask(new Callable<Object>() {
            public Object call() throws IllegalAccessException, InvocationTargetException {
                return method.invoke(view, (Object[]) null);
            }
        });
        Handler handler = view.getHandler();
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        handler.post(future);
        while (true) {
            try {
                return future.get(4000, TimeUnit.MILLISECONDS);
            } catch (ExecutionException e) {
                Throwable t = e.getCause();
                if (t instanceof IllegalAccessException) {
                    throw ((IllegalAccessException) t);
                } else if (t instanceof InvocationTargetException) {
                    throw ((InvocationTargetException) t);
                } else {
                    throw new RuntimeException("Unexpected exception", t);
                }
            } catch (InterruptedException e2) {
            } catch (CancellationException e3) {
                throw new RuntimeException("Unexpected cancellation exception", e3);
            }
        }
    }

    private static String formatIntToHexString(int value) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("0x");
        stringBuilder.append(Integer.toHexString(value).toUpperCase());
        return stringBuilder.toString();
    }

    private static void exportMethods(Context context, Object view, BufferedWriter out, Class<?> klass, String prefix) throws IOException {
        Context context2 = context;
        BufferedWriter bufferedWriter = out;
        String str = prefix;
        Method[] methods = getExportedPropertyMethods(klass);
        int count = methods.length;
        int i = 0;
        while (i < count) {
            Method[] methods2;
            Method method = methods[i];
            try {
                StringBuilder stringBuilder;
                String stringBuilder2;
                Object methodValue = callMethodOnAppropriateTheadBlocking(method, view);
                Class<?> returnType = method.getReturnType();
                ExportedProperty property = (ExportedProperty) sAnnotations.get(method);
                if (property.category().length() != 0) {
                    try {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(property.category());
                        stringBuilder.append(":");
                        stringBuilder2 = stringBuilder.toString();
                    } catch (IllegalAccessException e) {
                        methods2 = methods;
                    } catch (InvocationTargetException e2) {
                        methods2 = methods;
                    } catch (TimeoutException e3) {
                        methods2 = methods;
                    }
                } else {
                    stringBuilder2 = "";
                }
                String categoryPrefix = stringBuilder2;
                String str2 = "()";
                String str3;
                int j;
                if (returnType != Integer.TYPE) {
                    str3 = str2;
                    if (returnType == int[].class) {
                        int[] array = (int[]) methodValue;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(categoryPrefix);
                        stringBuilder.append(str);
                        stringBuilder.append(method.getName());
                        stringBuilder.append('_');
                        String suffix = str3;
                        methods2 = methods;
                        methods = categoryPrefix;
                        try {
                            exportUnrolledArray(context, out, property, array, stringBuilder.toString(), "()");
                        } catch (IllegalAccessException | InvocationTargetException | TimeoutException e4) {
                        }
                    } else {
                        methods2 = methods;
                        stringBuilder2 = str3;
                        methods = categoryPrefix;
                        if (returnType == String[].class) {
                            String[] array2 = (String[]) methodValue;
                            if (!property.hasAdjacentMapping() || array2 == null) {
                            } else {
                                j = 0;
                                while (j < array2.length) {
                                    String[] array3;
                                    if (array2[j] != null) {
                                        StringBuilder stringBuilder3 = new StringBuilder();
                                        stringBuilder3.append(methods);
                                        stringBuilder3.append(str);
                                        array3 = array2;
                                        writeEntry(bufferedWriter, stringBuilder3.toString(), array2[j], stringBuilder2, array2[j + 1] == null ? "null" : array2[j + 1]);
                                    } else {
                                        array3 = array2;
                                    }
                                    j += 2;
                                    array2 = array3;
                                }
                            }
                        } else if (!returnType.isPrimitive() && property.deepExport()) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str);
                            stringBuilder.append(property.prefix());
                            dumpViewProperties(context2, methodValue, bufferedWriter, stringBuilder.toString());
                        }
                    }
                    i++;
                    methods = methods2;
                } else if (!property.resolveId() || context2 == null) {
                    FlagToString[] flagsMapping = property.flagMapping();
                    if (flagsMapping.length > 0) {
                        j = ((Integer) methodValue).intValue();
                        String valuePrefix = new StringBuilder();
                        valuePrefix.append(categoryPrefix);
                        valuePrefix.append(str);
                        str3 = str2;
                        valuePrefix.append(method.getName());
                        valuePrefix.append('_');
                        exportUnrolledFlags(bufferedWriter, flagsMapping, j, valuePrefix.toString());
                    } else {
                        str3 = str2;
                    }
                    IntToString[] mapping = property.mapping();
                    if (mapping.length > 0) {
                        int mappingCount;
                        j = ((Integer) methodValue).intValue();
                        flagsMapping = mapping.length;
                        boolean mapped = false;
                        int j2 = 0;
                        while (j2 < flagsMapping) {
                            IntToString mapper = mapping[j2];
                            mappingCount = flagsMapping;
                            if (mapper.from() == j) {
                                methodValue = mapper.to();
                                flagsMapping = 1;
                                break;
                            }
                            j2++;
                            flagsMapping = mappingCount;
                        }
                        mappingCount = flagsMapping;
                        flagsMapping = mapped;
                        if (flagsMapping == null) {
                            methodValue = Integer.valueOf(j);
                        }
                    }
                    methods2 = methods;
                    stringBuilder2 = str3;
                    methods = categoryPrefix;
                } else {
                    methodValue = resolveId(context2, ((Integer) methodValue).intValue());
                    stringBuilder2 = str2;
                    methods2 = methods;
                    methods = categoryPrefix;
                }
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append(methods);
                stringBuilder4.append(str);
                writeEntry(bufferedWriter, stringBuilder4.toString(), method.getName(), stringBuilder2, methodValue);
            } catch (IllegalAccessException e5) {
                methods2 = methods;
            } catch (InvocationTargetException e6) {
                methods2 = methods;
            } catch (TimeoutException e7) {
                methods2 = methods;
            }
            i++;
            methods = methods2;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:75:0x01c4 A:{Catch:{ IllegalAccessException -> 0x01e0 }} */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x01c4 A:{Catch:{ IllegalAccessException -> 0x01e0 }} */
    private static void exportFields(android.content.Context r20, java.lang.Object r21, java.io.BufferedWriter r22, java.lang.Class<?> r23, java.lang.String r24) throws java.io.IOException {
        /*
        r7 = r20;
        r8 = r21;
        r9 = r22;
        r10 = r24;
        r11 = getExportedPropertyFields(r23);
        r12 = r11.length;
        r0 = 0;
        r13 = r0;
    L_0x000f:
        if (r13 >= r12) goto L_0x01e5;
    L_0x0011:
        r14 = r11[r13];
        r0 = 0;
        r1 = r14.getType();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r15 = r1;
        r1 = sAnnotations;	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1 = r1.get(r14);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r3 = r1;
        r3 = (android.view.ViewDebug.ExportedProperty) r3;	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1 = r3.category();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1 = r1.length();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r2 = "";
        if (r1 == 0) goto L_0x0044;
    L_0x002e:
        r1 = new java.lang.StringBuilder;	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1.<init>();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4 = r3.category();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1.append(r4);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4 = ":";
        r1.append(r4);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1 = r1.toString();	 Catch:{ IllegalAccessException -> 0x01e0 }
        goto L_0x0045;
    L_0x0044:
        r1 = r2;
    L_0x0045:
        r6 = r1;
        r1 = java.lang.Integer.TYPE;	 Catch:{ IllegalAccessException -> 0x01e0 }
        if (r15 == r1) goto L_0x0109;
    L_0x004a:
        r1 = java.lang.Byte.TYPE;	 Catch:{ IllegalAccessException -> 0x01e0 }
        if (r15 != r1) goto L_0x0053;
    L_0x004e:
        r18 = r0;
        r0 = r6;
        goto L_0x010c;
    L_0x0053:
        r1 = int[].class;
        if (r15 != r1) goto L_0x008b;
    L_0x0057:
        r1 = r14.get(r8);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4 = r1;
        r4 = (int[]) r4;	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1 = new java.lang.StringBuilder;	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1.<init>();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1.append(r6);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1.append(r10);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5 = r14.getName();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1.append(r5);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5 = 95;
        r1.append(r5);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5 = r1.toString();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r16 = r2;
        r17 = "";
        r1 = r20;
        r2 = r22;
        r18 = r0;
        r0 = r6;
        r6 = r17;
        exportUnrolledArray(r1, r2, r3, r4, r5, r6);	 Catch:{ IllegalAccessException -> 0x01e0 }
        goto L_0x01e1;
    L_0x008b:
        r18 = r0;
        r0 = r6;
        r1 = java.lang.String[].class;
        if (r15 != r1) goto L_0x00dd;
    L_0x0092:
        r1 = r14.get(r8);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1 = (java.lang.String[]) r1;	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4 = r3.hasAdjacentMapping();	 Catch:{ IllegalAccessException -> 0x01e0 }
        if (r4 == 0) goto L_0x00d9;
    L_0x009e:
        if (r1 == 0) goto L_0x00d9;
    L_0x00a0:
        r4 = 0;
    L_0x00a1:
        r5 = r1.length;	 Catch:{ IllegalAccessException -> 0x01e0 }
        if (r4 >= r5) goto L_0x00d6;
    L_0x00a4:
        r5 = r1[r4];	 Catch:{ IllegalAccessException -> 0x01e0 }
        if (r5 == 0) goto L_0x00cf;
    L_0x00a8:
        r5 = new java.lang.StringBuilder;	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5.<init>();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5.append(r0);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5.append(r10);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5 = r5.toString();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r6 = r1[r4];	 Catch:{ IllegalAccessException -> 0x01e0 }
        r16 = r4 + 1;
        r16 = r1[r16];	 Catch:{ IllegalAccessException -> 0x01e0 }
        if (r16 != 0) goto L_0x00c3;
    L_0x00bf:
        r16 = "null";
        goto L_0x00c7;
    L_0x00c3:
        r16 = r4 + 1;
        r16 = r1[r16];	 Catch:{ IllegalAccessException -> 0x01e0 }
    L_0x00c7:
        r17 = r1;
        r1 = r16;
        writeEntry(r9, r5, r6, r2, r1);	 Catch:{ IllegalAccessException -> 0x01e0 }
        goto L_0x00d1;
    L_0x00cf:
        r17 = r1;
    L_0x00d1:
        r4 = r4 + 2;
        r1 = r17;
        goto L_0x00a1;
    L_0x00d6:
        r17 = r1;
        goto L_0x00db;
    L_0x00d9:
        r17 = r1;
    L_0x00db:
        goto L_0x01e1;
    L_0x00dd:
        r1 = r15.isPrimitive();	 Catch:{ IllegalAccessException -> 0x01e0 }
        if (r1 != 0) goto L_0x0105;
    L_0x00e3:
        r1 = r3.deepExport();	 Catch:{ IllegalAccessException -> 0x01e0 }
        if (r1 == 0) goto L_0x0105;
    L_0x00e9:
        r1 = r14.get(r8);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r2 = new java.lang.StringBuilder;	 Catch:{ IllegalAccessException -> 0x01e0 }
        r2.<init>();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r2.append(r10);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4 = r3.prefix();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r2.append(r4);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r2 = r2.toString();	 Catch:{ IllegalAccessException -> 0x01e0 }
        dumpViewProperties(r7, r1, r9, r2);	 Catch:{ IllegalAccessException -> 0x01e0 }
        goto L_0x01e1;
    L_0x0105:
        r1 = r18;
        goto L_0x01c2;
    L_0x0109:
        r18 = r0;
        r0 = r6;
    L_0x010c:
        r1 = r3.resolveId();	 Catch:{ IllegalAccessException -> 0x01e0 }
        if (r1 == 0) goto L_0x011f;
    L_0x0112:
        if (r7 == 0) goto L_0x011f;
    L_0x0114:
        r1 = r14.getInt(r8);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4 = resolveId(r7, r1);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1 = r4;
        goto L_0x01c2;
    L_0x011f:
        r1 = r3.flagMapping();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4 = r1.length;	 Catch:{ IllegalAccessException -> 0x01e0 }
        if (r4 <= 0) goto L_0x0148;
    L_0x0126:
        r4 = r14.getInt(r8);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5 = new java.lang.StringBuilder;	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5.<init>();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5.append(r0);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5.append(r10);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r6 = r14.getName();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5.append(r6);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r6 = 95;
        r5.append(r6);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5 = r5.toString();	 Catch:{ IllegalAccessException -> 0x01e0 }
        exportUnrolledFlags(r9, r1, r4, r5);	 Catch:{ IllegalAccessException -> 0x01e0 }
    L_0x0148:
        r4 = r3.mapping();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5 = r4.length;	 Catch:{ IllegalAccessException -> 0x01e0 }
        if (r5 <= 0) goto L_0x017d;
    L_0x014f:
        r5 = r14.getInt(r8);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r6 = r4.length;	 Catch:{ IllegalAccessException -> 0x01e0 }
        r16 = 0;
        r17 = r1;
        r1 = r16;
    L_0x015a:
        if (r1 >= r6) goto L_0x0172;
    L_0x015c:
        r16 = r4[r1];	 Catch:{ IllegalAccessException -> 0x01e0 }
        r19 = r4;
        r4 = r16.from();	 Catch:{ IllegalAccessException -> 0x01e0 }
        if (r4 != r5) goto L_0x016d;
    L_0x0166:
        r4 = r16.to();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r18 = r4;
        goto L_0x0174;
    L_0x016d:
        r1 = r1 + 1;
        r4 = r19;
        goto L_0x015a;
    L_0x0172:
        r19 = r4;
    L_0x0174:
        if (r18 != 0) goto L_0x0181;
    L_0x0176:
        r1 = java.lang.Integer.valueOf(r5);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r18 = r1;
        goto L_0x0181;
    L_0x017d:
        r17 = r1;
        r19 = r4;
    L_0x0181:
        r1 = r3.formatToHexString();	 Catch:{ IllegalAccessException -> 0x01e0 }
        if (r1 == 0) goto L_0x01bf;
    L_0x0187:
        r1 = r14.get(r8);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4 = java.lang.Integer.TYPE;	 Catch:{ IllegalAccessException -> 0x01e0 }
        if (r15 != r4) goto L_0x019c;
    L_0x018f:
        r4 = r1;
        r4 = (java.lang.Integer) r4;	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4 = r4.intValue();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4 = formatIntToHexString(r4);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1 = r4;
        goto L_0x01c1;
    L_0x019c:
        r4 = java.lang.Byte.TYPE;	 Catch:{ IllegalAccessException -> 0x01e0 }
        if (r15 != r4) goto L_0x01c1;
    L_0x01a0:
        r4 = new java.lang.StringBuilder;	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4.<init>();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5 = "0x";
        r4.append(r5);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5 = r1;
        r5 = (java.lang.Byte) r5;	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5 = r5.byteValue();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r6 = 1;
        r5 = java.lang.Byte.toHexString(r5, r6);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4.append(r5);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4 = r4.toString();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1 = r4;
        goto L_0x01c1;
    L_0x01bf:
        r1 = r18;
    L_0x01c2:
        if (r1 != 0) goto L_0x01c9;
    L_0x01c4:
        r4 = r14.get(r8);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r1 = r4;
    L_0x01c9:
        r4 = new java.lang.StringBuilder;	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4.<init>();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4.append(r0);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4.append(r10);	 Catch:{ IllegalAccessException -> 0x01e0 }
        r4 = r4.toString();	 Catch:{ IllegalAccessException -> 0x01e0 }
        r5 = r14.getName();	 Catch:{ IllegalAccessException -> 0x01e0 }
        writeEntry(r9, r4, r5, r2, r1);	 Catch:{ IllegalAccessException -> 0x01e0 }
        goto L_0x01e1;
    L_0x01e0:
        r0 = move-exception;
    L_0x01e1:
        r13 = r13 + 1;
        goto L_0x000f;
    L_0x01e5:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewDebug.exportFields(android.content.Context, java.lang.Object, java.io.BufferedWriter, java.lang.Class, java.lang.String):void");
    }

    private static void writeEntry(BufferedWriter out, String prefix, String name, String suffix, Object value) throws IOException {
        out.write(prefix);
        out.write(name);
        out.write(suffix);
        out.write("=");
        writeValue(out, value);
        out.write(32);
    }

    private static void exportUnrolledFlags(BufferedWriter out, FlagToString[] mapping, int intValue, String prefix) throws IOException {
        for (FlagToString flagMapping : mapping) {
            boolean ifTrue = flagMapping.outputIf();
            int maskResult = flagMapping.mask() & intValue;
            boolean test = maskResult == flagMapping.equals();
            if ((test && ifTrue) || !(test || ifTrue)) {
                writeEntry(out, prefix, flagMapping.name(), "", formatIntToHexString(maskResult));
            }
        }
    }

    public static String intToString(Class<?> clazz, String field, int integer) {
        IntToString[] mapping = getMapping(clazz, field);
        if (mapping == null) {
            return Integer.toString(integer);
        }
        for (IntToString map : mapping) {
            if (map.from() == integer) {
                return map.to();
            }
        }
        return Integer.toString(integer);
    }

    public static String flagsToString(Class<?> clazz, String field, int flags) {
        FlagToString[] mapping = getFlagMapping(clazz, field);
        if (mapping == null) {
            return Integer.toHexString(flags);
        }
        StringBuilder result = new StringBuilder();
        int count = mapping.length;
        int j = 0;
        while (true) {
            boolean test = true;
            if (j >= count) {
                break;
            }
            FlagToString flagMapping = mapping[j];
            boolean ifTrue = flagMapping.outputIf();
            if ((flagMapping.mask() & flags) != flagMapping.equals()) {
                test = false;
            }
            if (test && ifTrue) {
                result.append(flagMapping.name());
                result.append(' ');
            }
            j++;
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    private static FlagToString[] getFlagMapping(Class<?> clazz, String field) {
        try {
            return ((ExportedProperty) clazz.getDeclaredField(field).getAnnotation(ExportedProperty.class)).flagMapping();
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private static IntToString[] getMapping(Class<?> clazz, String field) {
        try {
            return ((ExportedProperty) clazz.getDeclaredField(field).getAnnotation(ExportedProperty.class)).mapping();
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private static void exportUnrolledArray(Context context, BufferedWriter out, ExportedProperty property, int[] array, String prefix, String suffix) throws IOException {
        Context context2 = context;
        int[] iArr = array;
        IntToString[] indexMapping = property.indexMapping();
        boolean resolveId = false;
        boolean hasIndexMapping = indexMapping.length > 0;
        IntToString[] mapping = property.mapping();
        boolean hasMapping = mapping.length > 0;
        if (property.resolveId() && context2 != null) {
            resolveId = true;
        }
        int valuesCount = iArr.length;
        for (int j = 0; j < valuesCount; j++) {
            String value = null;
            int intValue = iArr[j];
            String name = String.valueOf(j);
            if (hasIndexMapping) {
                for (IntToString mapped : indexMapping) {
                    if (mapped.from() == j) {
                        name = mapped.to();
                        break;
                    }
                }
            }
            if (hasMapping) {
                for (IntToString mapped2 : mapping) {
                    if (mapped2.from() == intValue) {
                        value = mapped2.to();
                        break;
                    }
                }
            }
            if (!resolveId) {
                value = String.valueOf(intValue);
            } else if (value == null) {
                value = (String) resolveId(context2, intValue);
            }
            writeEntry(out, prefix, name, suffix, value);
        }
        BufferedWriter bufferedWriter = out;
        String str = prefix;
        String str2 = suffix;
    }

    static Object resolveId(Context context, int id) {
        Resources resources = context.getResources();
        if (id < 0) {
            return "NO_ID";
        }
        try {
            Object fieldValue = new StringBuilder();
            fieldValue.append(resources.getResourceTypeName(id));
            fieldValue.append('/');
            fieldValue.append(resources.getResourceEntryName(id));
            return fieldValue.toString();
        } catch (NotFoundException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("id/");
            stringBuilder.append(formatIntToHexString(id));
            return stringBuilder.toString();
        }
    }

    private static void writeValue(BufferedWriter out, Object value) throws IOException {
        String str = ",";
        if (value != null) {
            String output = "[EXCEPTION]";
            String replace;
            try {
                replace = value.toString().replace("\n", "\\n").replace("\r", "\\r");
                output = replace;
            } finally {
                String str2 = replace;
                out.write(String.valueOf(output.length()));
                out.write(str);
                out.write(output);
                return;
            }
            return;
        }
        out.write("4,null");
    }

    private static Field[] capturedViewGetPropertyFields(Class<?> klass) {
        if (mCapturedViewFieldsForClasses == null) {
            mCapturedViewFieldsForClasses = new HashMap();
        }
        HashMap<Class<?>, Field[]> map = mCapturedViewFieldsForClasses;
        Field[] fields = (Field[]) map.get(klass);
        if (fields != null) {
            return fields;
        }
        ArrayList<Field> foundFields = new ArrayList();
        for (Field field : klass.getFields()) {
            if (field.isAnnotationPresent(CapturedViewProperty.class)) {
                field.setAccessible(true);
                foundFields.add(field);
            }
        }
        fields = (Field[]) foundFields.toArray(new Field[foundFields.size()]);
        map.put(klass, fields);
        return fields;
    }

    private static Method[] capturedViewGetPropertyMethods(Class<?> klass) {
        if (mCapturedViewMethodsForClasses == null) {
            mCapturedViewMethodsForClasses = new HashMap();
        }
        HashMap<Class<?>, Method[]> map = mCapturedViewMethodsForClasses;
        Method[] methods = (Method[]) map.get(klass);
        if (methods != null) {
            return methods;
        }
        ArrayList<Method> foundMethods = new ArrayList();
        for (Method method : klass.getMethods()) {
            if (method.getParameterTypes().length == 0 && method.isAnnotationPresent(CapturedViewProperty.class) && method.getReturnType() != Void.class) {
                method.setAccessible(true);
                foundMethods.add(method);
            }
        }
        methods = (Method[]) foundMethods.toArray(new Method[foundMethods.size()]);
        map.put(klass, methods);
        return methods;
    }

    private static String capturedViewExportMethods(Object obj, Class<?> klass, String prefix) {
        String str = "null";
        if (obj == null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (Method method : capturedViewGetPropertyMethods(klass)) {
            try {
                Object methodValue = method.invoke(obj, (Object[]) null);
                Class<?> returnType = method.getReturnType();
                if (((CapturedViewProperty) method.getAnnotation(CapturedViewProperty.class)).retrieveReturn()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(method.getName());
                    stringBuilder.append("#");
                    sb.append(capturedViewExportMethods(methodValue, returnType, stringBuilder.toString()));
                } else {
                    sb.append(prefix);
                    sb.append(method.getName());
                    sb.append("()=");
                    if (methodValue != null) {
                        sb.append(methodValue.toString().replace("\n", "\\n"));
                    } else {
                        sb.append(str);
                    }
                    sb.append("; ");
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
            }
        }
        return sb.toString();
    }

    private static String capturedViewExportFields(Object obj, Class<?> klass, String prefix) {
        String str = "null";
        if (obj == null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (Field field : capturedViewGetPropertyFields(klass)) {
            try {
                Object fieldValue = field.get(obj);
                sb.append(prefix);
                sb.append(field.getName());
                sb.append("=");
                if (fieldValue != null) {
                    sb.append(fieldValue.toString().replace("\n", "\\n"));
                } else {
                    sb.append(str);
                }
                sb.append(' ');
            } catch (IllegalAccessException e) {
            }
        }
        return sb.toString();
    }

    public static void dumpCapturedView(String tag, Object view) {
        Class<?> klass = view.getClass();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(klass.getName());
        stringBuilder.append(": ");
        StringBuilder sb = new StringBuilder(stringBuilder.toString());
        String str = "";
        sb.append(capturedViewExportFields(view, klass, str));
        sb.append(capturedViewExportMethods(view, klass, str));
        Log.d(tag, sb.toString());
    }

    public static Object invokeViewMethod(View view, Method method, Object[] args) {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Object> result = new AtomicReference();
        final AtomicReference<Throwable> exception = new AtomicReference();
        final Method method2 = method;
        final View view2 = view;
        final Object[] objArr = args;
        view.post(new Runnable() {
            public void run() {
                try {
                    result.set(method2.invoke(view2, objArr));
                } catch (InvocationTargetException e) {
                    exception.set(e.getCause());
                } catch (Exception e2) {
                    exception.set(e2);
                }
                latch.countDown();
            }
        });
        try {
            latch.await();
            if (exception.get() == null) {
                return result.get();
            }
            throw new RuntimeException((Throwable) exception.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setLayoutParameter(final View view, String param, int value) throws NoSuchFieldException, IllegalAccessException {
        final LayoutParams p = view.getLayoutParams();
        Field f = p.getClass().getField(param);
        if (f.getType() == Integer.TYPE) {
            f.set(p, Integer.valueOf(value));
            view.post(new Runnable() {
                public void run() {
                    view.setLayoutParams(p);
                }
            });
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Only integer layout parameters can be set. Field ");
        stringBuilder.append(param);
        stringBuilder.append(" is of type ");
        stringBuilder.append(f.getType().getSimpleName());
        throw new RuntimeException(stringBuilder.toString());
    }
}
