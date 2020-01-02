package miui.maml;

import android.app.slice.Slice;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewManager;
import android.view.WindowManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import miui.maml.FramerateTokenList.FramerateToken;
import miui.maml.RendererController.IRenderable;
import miui.maml.RendererController.Listener;
import miui.maml.SoundManager.Command;
import miui.maml.SoundManager.SoundOptions;
import miui.maml.StylesManager.Style;
import miui.maml.data.DateTimeVariableUpdater;
import miui.maml.data.IndexedVariable;
import miui.maml.data.VariableBinder;
import miui.maml.data.VariableBinderManager;
import miui.maml.data.VariableNames;
import miui.maml.data.VariableUpdaterManager;
import miui.maml.data.Variables;
import miui.maml.elements.AnimatedScreenElement;
import miui.maml.elements.ElementGroup;
import miui.maml.elements.ElementGroupRC;
import miui.maml.elements.FramerateController;
import miui.maml.elements.ITicker;
import miui.maml.elements.ScreenElement;
import miui.maml.elements.ScreenElementVisitor;
import miui.maml.util.ConfigFile;
import miui.maml.util.ConfigFile.Variable;
import miui.maml.util.SysProperties;
import miui.maml.util.Task;
import miui.maml.util.Utils;
import miui.os.SystemProperties;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ScreenElementRoot extends ScreenElement implements InteractiveListener {
    private static final boolean CALCULATE_FRAME_RATE = true;
    public static final int CAPABILITY_ALL = -1;
    public static final int CAPABILITY_CREATE_OBJ = 4;
    public static final int CAPABILITY_VAR_PERSISTENCE = 2;
    public static final int CAPABILITY_WEBSERVICE = 1;
    private static final int DEFAULT_RES_DENSITY = 240;
    private static final int DEFAULT_SCREEN_WIDTH = 480;
    private static final String EXTERNAL_COMMANDS_TAG_NAME = "ExternalCommands";
    private static final String LOG_TAG = "ScreenElementRoot";
    private static final int MAML_VERSION = 4;
    private static final String MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String RAW_DENSITY = "__raw_density";
    private static final String ROOT_NAME = "__root";
    private static final String SCALE_FACTOR = "__scale_factor";
    private static final String THEMEMANAGER_PACKAGE_NAME = "com.android.thememanager";
    private static final String VARIABLE_VIEW_HEIGHT = "view_height";
    private static final String VARIABLE_VIEW_WIDTH = "view_width";
    private static final String VAR_MAML_VERSION = "__maml_version";
    protected float DEFAULT_FRAME_RATE;
    private List<AnimatedScreenElement> mAccessibleElements;
    private boolean mAllowScreenRotation;
    private int mBgColor;
    private boolean mBlurWindow;
    private String mCacheDir;
    private int mCapability;
    private long mCheckPoint;
    private boolean mClearCanvas;
    private ConfigFile mConfig;
    private String mConfigPath;
    protected ScreenContext mContext;
    protected RendererController mController;
    private int mDefaultResourceDensity;
    private int mDefaultScreenWidth;
    private WeakReference<OnExternCommandListener> mExternCommandListener;
    private CommandTriggers mExternalCommandManager;
    private boolean mFinished;
    private float mFontScale;
    protected float mFrameRate;
    private IndexedVariable mFrameRateVar;
    private FramerateHelper mFramerateHelper;
    private int mFrames;
    private float mHeight;
    private WeakReference<OnHoverChangeListener> mHoverChangeListenerRef;
    private AnimatedScreenElement mHoverElement;
    private Matrix mHoverMatrix;
    protected ElementGroup mInnerGroup;
    private boolean mKeepResource;
    private boolean mNeedDisallowInterceptTouchEvent;
    private IndexedVariable mNeedDisallowInterceptTouchEventVar;
    private boolean mNeedReset;
    private ArrayList<ITicker> mPreTickers;
    protected HashMap<String, String> mRawAttrs;
    private int mRawDefaultResourceDensity;
    private int mRawHeight;
    private int mRawTargetDensity;
    private int mRawWidth;
    private ArrayList<RendererController> mRendererControllers;
    private String mRootTag;
    private float mScale;
    private boolean mScaleByDensity;
    public boolean mShowDebugLayout;
    private boolean mShowFramerate;
    private SoundManager mSoundManager;
    private StylesManager mStylesManager;
    private boolean mSupportAccessibilityService;
    private OnExternCommandListener mSystemExternCommandListener;
    private int mTargetDensity;
    protected int mTargetScreenHeight;
    protected int mTargetScreenWidth;
    private IndexedVariable mTouchBeginTime;
    private IndexedVariable mTouchBeginX;
    private IndexedVariable mTouchBeginY;
    private IndexedVariable mTouchX;
    private IndexedVariable mTouchY;
    protected VariableBinderManager mVariableBinderManager;
    private VariableUpdaterManager mVariableUpdaterManager;
    private int mVersion;
    private ViewManager mViewManager;
    private float mWidth;

    public interface OnHoverChangeListener {
        void onHoverChange(String str);
    }

    /* renamed from: miui.maml.ScreenElementRoot$3 */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$ScreenElementRoot$ExtraResource$MetricsType = new int[MetricsType.values().length];

        static {
            try {
                $SwitchMap$miui$maml$ScreenElementRoot$ExtraResource$MetricsType[MetricsType.DEN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$ScreenElementRoot$ExtraResource$MetricsType[MetricsType.SW.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$miui$maml$ScreenElementRoot$ExtraResource$MetricsType[MetricsType.SW_DEN.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private static class ExtraResource {
        private ArrayList<ScaleMetrics> mResources = new ArrayList();
        private ArrayList<ScaleMetrics> mScales = new ArrayList();

        enum MetricsType {
            DEN,
            SW,
            SW_DEN
        }

        class ScaleMetrics {
            int mDensity;
            float mScale = 1.0f;
            int mScreenWidth;
            int mSizeType;

            public ScaleMetrics(String str, MetricsType type) {
                try {
                    String[] info = str.split(":");
                    int i = AnonymousClass3.$SwitchMap$miui$maml$ScreenElementRoot$ExtraResource$MetricsType[type.ordinal()];
                    int i2 = 1;
                    if (i == 1) {
                        this.mDensity = Integer.parseInt(info[0]);
                        this.mScreenWidth = (ResourceManager.translateDensity(this.mDensity) * 480) / 240;
                        if (info.length > 1) {
                            this.mScale = Float.parseFloat(info[1]);
                        }
                    } else if (i == 2) {
                        this.mScreenWidth = Integer.parseInt(info[0]);
                        this.mDensity = ResourceManager.retranslateDensity((this.mScreenWidth * 240) / 480);
                        if (info.length > 1) {
                            this.mScale = Float.parseFloat(info[1]);
                        }
                    } else if (i == 3) {
                        String[] metrics = info[0].split("-");
                        this.mSizeType = 0;
                        String str2 = "invalid format: ";
                        StringBuilder stringBuilder;
                        if (metrics.length == 1) {
                            if (metrics[0].startsWith("sw")) {
                                this.mScreenWidth = Integer.parseInt(metrics[0].substring(2));
                                this.mDensity = ResourceManager.retranslateDensity((this.mScreenWidth * 240) / 480);
                            } else if (metrics[0].startsWith("den")) {
                                this.mDensity = Integer.parseInt(metrics[0].substring(3));
                                this.mScreenWidth = (ResourceManager.translateDensity(this.mDensity) * 480) / 240;
                            } else {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(str2);
                                stringBuilder.append(str);
                                throw new IllegalArgumentException(stringBuilder.toString());
                            }
                        } else if (metrics.length >= 2) {
                            this.mScreenWidth = Integer.parseInt(metrics[0].substring(2));
                            this.mDensity = Integer.parseInt(metrics[1].substring(3));
                            if (metrics.length == 3) {
                                this.mSizeType = ExtraResource.parseSizeType(metrics[2]);
                            }
                        } else {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str2);
                            stringBuilder.append(str);
                            throw new IllegalArgumentException(stringBuilder.toString());
                        }
                        if (info.length > 1) {
                            if (info.length != 2) {
                                i2 = 2;
                            }
                            this.mScale = Float.parseFloat(info[i2]);
                        }
                        onParseInfo(info);
                    }
                } catch (NumberFormatException e) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("format error of string: ");
                    stringBuilder2.append(str);
                    Log.w(ScreenElementRoot.LOG_TAG, stringBuilder2.toString());
                    throw new IllegalArgumentException("invalid format");
                }
            }

            /* Access modifiers changed, original: protected */
            public void onParseInfo(String[] info) {
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ScaleMetrics sw:");
                stringBuilder.append(this.mScreenWidth);
                stringBuilder.append(" den:");
                stringBuilder.append(this.mDensity);
                stringBuilder.append(" sizeType:");
                stringBuilder.append(this.mSizeType);
                stringBuilder.append(" scale:");
                stringBuilder.append(this.mScale);
                return stringBuilder.toString();
            }
        }

        class Resource extends ScaleMetrics {
            String mPath;

            public Resource() {
                super();
            }

            public Resource(String str, MetricsType type) {
                super(str, type);
                int i = AnonymousClass3.$SwitchMap$miui$maml$ScreenElementRoot$ExtraResource$MetricsType[type.ordinal()];
                StringBuilder stringBuilder;
                if (i == 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("den");
                    stringBuilder.append(this.mDensity);
                    this.mPath = stringBuilder.toString();
                } else if (i == 2) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("sw");
                    stringBuilder.append(this.mScreenWidth);
                    this.mPath = stringBuilder.toString();
                }
            }

            /* Access modifiers changed, original: protected */
            public void onParseInfo(String[] info) {
                this.mPath = info[info.length <= 2 ? 0 : 1];
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(super.toString());
                stringBuilder.append(" path:");
                stringBuilder.append(this.mPath);
                return stringBuilder.toString();
            }
        }

        public ExtraResource(Element root, int defaultDen) {
            Resource defaultRes = new Resource();
            defaultRes.mDensity = defaultDen;
            defaultRes.mScreenWidth = (ResourceManager.translateDensity(defaultDen) * 480) / 240;
            defaultRes.mSizeType = 0;
            defaultRes.mPath = null;
            defaultRes.mScale = 1.0f;
            this.mResources.add(defaultRes);
            inflateMetrics(this.mResources, root.getAttribute("extraResourcesDensity"), MetricsType.DEN);
            inflateMetrics(this.mResources, root.getAttribute("extraResourcesScreenWidth"), MetricsType.SW);
            inflateMetrics(this.mResources, root.getAttribute("extraResources"), MetricsType.SW_DEN);
            ScaleMetrics defaultScale = new ScaleMetrics();
            defaultScale.mDensity = defaultDen;
            defaultScale.mScreenWidth = (ResourceManager.translateDensity(defaultDen) * 480) / 240;
            defaultScale.mSizeType = 0;
            defaultScale.mScale = -1.0f;
            this.mScales.add(defaultScale);
            inflateMetrics(this.mScales, root.getAttribute("extraScaleByDensity"), MetricsType.DEN);
            inflateMetrics(this.mScales, root.getAttribute("extraScaleByScreenWidth"), MetricsType.SW);
            inflateMetrics(this.mScales, root.getAttribute("extraScales"), MetricsType.SW_DEN);
        }

        private void inflateMetrics(ArrayList<ScaleMetrics> metrics, String attr, MetricsType type) {
            if (!TextUtils.isEmpty(attr)) {
                for (String str : attr.split(",")) {
                    try {
                        if (metrics == this.mResources) {
                            metrics.add(new Resource(str.trim(), type));
                        } else if (metrics == this.mScales) {
                            metrics.add(new ScaleMetrics(str.trim(), type));
                        }
                    } catch (IllegalArgumentException e) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("format error of attribute: ");
                        stringBuilder.append(attr);
                        Log.w(ScreenElementRoot.LOG_TAG, stringBuilder.toString());
                    }
                }
            }
        }

        private static int parseSizeType(String str) {
            if ("small".equals(str)) {
                return 1;
            }
            if ("normal".equals(str)) {
                return 2;
            }
            if (Slice.HINT_LARGE.equals(str)) {
                return 3;
            }
            if ("xlarge".equals(str)) {
                return 4;
            }
            return 0;
        }

        public Resource findResource(int targetDen, int targetSw, int targetSizeType) {
            return (Resource) findMetrics(targetDen, targetSw, targetSizeType, this.mResources);
        }

        public ScaleMetrics findScale(int targetDen, int targetSw, int targetSizeType) {
            return findMetrics(targetDen, targetSw, targetSizeType, this.mScales);
        }

        /* Access modifiers changed, original: 0000 */
        public ScaleMetrics findMetrics(int targetDen, int targetSw, int targetSizeType, ArrayList<ScaleMetrics> metrics) {
            ScaleMetrics m;
            ScaleMetrics result = null;
            int minDiffDen = Integer.MAX_VALUE;
            int minDiffSw = Integer.MAX_VALUE;
            ArrayList<ScaleMetrics> candidates = new ArrayList();
            Iterator it = metrics.iterator();
            while (it.hasNext()) {
                m = (ScaleMetrics) it.next();
                if (m.mSizeType == 0 || m.mSizeType == targetSizeType) {
                    int diffDen = Math.abs(targetDen - m.mDensity);
                    if (diffDen < minDiffDen) {
                        minDiffDen = diffDen;
                        minDiffSw = Math.abs(targetSw - m.mScreenWidth);
                        candidates.clear();
                        candidates.add(m);
                    } else if (diffDen == minDiffDen) {
                        int diffSw = Math.abs(targetSw - m.mScreenWidth);
                        if (diffSw < minDiffSw) {
                            minDiffSw = diffSw;
                            candidates.clear();
                            candidates.add(m);
                        } else if (diffSw == minDiffSw) {
                            candidates.add(m);
                        }
                    }
                }
            }
            it = candidates.iterator();
            while (it.hasNext()) {
                m = (ScaleMetrics) it.next();
                if (m.mSizeType == targetSizeType) {
                    return m;
                }
                if (m.mSizeType == 0) {
                    result = m;
                }
            }
            return result;
        }
    }

    private static class FramerateHelper {
        private String mFramerateText;
        private TextPaint mPaint;
        private int mRealFrameRate;
        private int mShowingFramerate;
        private int mTextX;
        private int mTextY;

        public FramerateHelper() {
            this(-65536, 14, 10, 10);
        }

        public FramerateHelper(int color, int size, int x, int y) {
            this.mPaint = new TextPaint();
            this.mPaint.setColor(color);
            this.mPaint.setTextSize((float) size);
            this.mTextX = x;
            this.mTextY = y;
        }

        public void draw(Canvas c) {
            if (this.mFramerateText == null || this.mShowingFramerate != this.mRealFrameRate) {
                this.mShowingFramerate = this.mRealFrameRate;
                this.mFramerateText = String.format("FPS %d", new Object[]{Integer.valueOf(this.mShowingFramerate)});
            }
            c.drawText(this.mFramerateText, (float) this.mTextX, (float) this.mTextY, this.mPaint);
        }

        public void set(int framerate) {
            this.mRealFrameRate = framerate;
        }
    }

    private static class InnerGroup extends ElementGroup {
        public InnerGroup(Element node, ScreenElementRoot root) {
            super(node, root);
        }

        public final RendererController getRendererController() {
            return this.mRoot.getRendererController();
        }
    }

    public interface OnExternCommandListener {
        void onCommand(String str, Double d, String str2);
    }

    public ScreenElementRoot(ScreenContext c) {
        super(null, null);
        this.DEFAULT_FRAME_RATE = 30.0f;
        this.mPreTickers = new ArrayList();
        this.mRawAttrs = new HashMap();
        this.mSupportAccessibilityService = false;
        this.mBlurWindow = false;
        this.mHoverMatrix = new Matrix();
        this.mFramerateHelper = new FramerateHelper();
        this.mRendererControllers = new ArrayList();
        this.mCapability = -1;
        this.mAccessibleElements = new ArrayList();
        this.mRoot = this;
        this.mContext = c;
        this.mVariableUpdaterManager = new VariableUpdaterManager(this);
        this.mTouchX = new IndexedVariable(VariableNames.VAR_TOUCH_X, getContext().mVariables, true);
        this.mTouchY = new IndexedVariable(VariableNames.VAR_TOUCH_Y, getContext().mVariables, true);
        this.mTouchBeginX = new IndexedVariable(VariableNames.VAR_TOUCH_BEGIN_X, getContext().mVariables, true);
        this.mTouchBeginY = new IndexedVariable(VariableNames.VAR_TOUCH_BEGIN_Y, getContext().mVariables, true);
        this.mTouchBeginTime = new IndexedVariable(VariableNames.VAR_TOUCH_BEGIN_TIME, getContext().mVariables, true);
        this.mNeedDisallowInterceptTouchEventVar = new IndexedVariable(VariableNames.VAR_INTECEPT_SYS_TOUCH, getContext().mVariables, true);
        this.mSoundManager = new SoundManager(this.mContext);
        this.mSystemExternCommandListener = new SystemCommandListener(this);
    }

    public FramerateToken createFramerateToken(String name) {
        return createToken(name);
    }

    /* Access modifiers changed, original: protected */
    public void onAddVariableUpdater(VariableUpdaterManager m) {
        m.add(new DateTimeVariableUpdater(m));
    }

    public void setConfig(String path) {
        this.mConfigPath = path;
    }

    public void setCacheDir(String path) {
        this.mCacheDir = path;
    }

    public String getCacheDir() {
        return this.mCacheDir;
    }

    public void tick(long currentTime) {
        if (this.mNeedReset) {
            reset(currentTime);
            onCommand("init");
            this.mNeedReset = false;
        }
        doTick(currentTime);
    }

    public void addAccessibleElements(AnimatedScreenElement e) {
        this.mAccessibleElements.add(e);
    }

    public List<AnimatedScreenElement> getAccessibleElements() {
        return this.mAccessibleElements;
    }

    public void removeAccessibleElement(AnimatedScreenElement e) {
        this.mAccessibleElements.remove(e);
    }

    public void removeAllAccessibleElements() {
        this.mAccessibleElements.clear();
    }

    public void addAccessibleList(List<AnimatedScreenElement> list) {
        this.mAccessibleElements.addAll(list);
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        if (!this.mFinished) {
            VariableBinderManager variableBinderManager = this.mVariableBinderManager;
            if (variableBinderManager != null) {
                variableBinderManager.tick();
            }
            this.mVariableUpdaterManager.tick(currentTime);
            int N = this.mPreTickers.size();
            for (int i = 0; i < N; i++) {
                ((ITicker) this.mPreTickers.get(i)).tick(currentTime);
            }
            this.mInnerGroup.tick(currentTime);
            this.mNeedDisallowInterceptTouchEvent = this.mNeedDisallowInterceptTouchEventVar.getDouble() > 0.0d;
            if (this.mFrameRateVar == null) {
                this.mFrameRateVar = new IndexedVariable(VariableNames.FRAME_RATE, this.mContext.mVariables, true);
                this.mCheckPoint = 0;
            }
            long j = this.mCheckPoint;
            if (j == 0) {
                this.mCheckPoint = currentTime;
            } else {
                long t = currentTime - j;
                if (t >= 1000) {
                    int r = (int) (((long) (this.mFrames * 1000)) / t);
                    this.mFramerateHelper.set(r);
                    this.mFrameRateVar.set((double) r);
                    this.mFrames = 0;
                    this.mCheckPoint = currentTime;
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public String putRawAttr(String name, String value) {
        return (String) this.mRawAttrs.put(name, value);
    }

    public String getRawAttr(String name) {
        return (String) this.mRawAttrs.get(name);
    }

    public void setDefaultFramerate(float f) {
        this.DEFAULT_FRAME_RATE = f;
    }

    public ScreenContext getContext() {
        return this.mContext;
    }

    public boolean postDelayed(Runnable r, long delayMillis) {
        if (this.mFinished) {
            return false;
        }
        return this.mContext.postDelayed(r, delayMillis);
    }

    public void removeCallbacks(Runnable r) {
        this.mContext.removeCallbacks(r);
    }

    public ScreenElement findElement(String name) {
        if (ROOT_NAME.equals(name)) {
            return this;
        }
        return this.mInnerGroup.findElement(name);
    }

    public VariableBinder findBinder(String name) {
        VariableBinderManager variableBinderManager = this.mVariableBinderManager;
        return variableBinderManager != null ? variableBinderManager.findBinder(name) : null;
    }

    /* Access modifiers changed, original: protected */
    public void doRender(Canvas c) {
        if (!this.mFinished) {
            if (this.mClearCanvas) {
                c.drawColor(0, Mode.CLEAR);
            }
            int i = this.mBgColor;
            if (i != 0) {
                c.drawColor(i);
            }
            try {
                this.mInnerGroup.render(c);
            } catch (Exception e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e2) {
                e2.printStackTrace();
                Log.e(LOG_TAG, e2.toString());
            }
            if (this.mShowFramerate) {
                this.mFramerateHelper.draw(c);
            }
            this.mFrames++;
            this.mController.doneRender();
        }
    }

    public void showFramerate(boolean show) {
        this.mShowFramerate = show;
    }

    public void init() {
        Variables vars = this.mContext.mVariables;
        vars.put("__objRoot", (Object) this);
        vars.put("__objContext", this.mContext);
        super.init();
        String str = "init";
        String str2 = LOG_TAG;
        Log.d(str2, str);
        requestFramerate(this.mFrameRate);
        this.mCapability = -1;
        this.mShowDebugLayout = SysProperties.isShowDebugLayout();
        this.mFinished = false;
        this.mContext.mResourceManager.init();
        this.mFontScale = getContext().mContext.getResources().getConfiguration().fontScale;
        vars.put("__fontScale", (double) this.mFontScale);
        LanguageHelper.load(this.mContext.mContext.getResources().getConfiguration().locale, this.mContext.mResourceManager, this.mContext.mVariables);
        vars.put(VariableNames.RAW_SCREEN_WIDTH, (double) this.mTargetScreenWidth);
        vars.put(VariableNames.RAW_SCREEN_HEIGHT, (double) this.mTargetScreenHeight);
        vars.put(VariableNames.SCREEN_WIDTH, (double) (((float) this.mTargetScreenWidth) / this.mScale));
        vars.put(VariableNames.SCREEN_HEIGHT, (double) (((float) this.mTargetScreenHeight) / this.mScale));
        int i = this.mRawWidth;
        String str3 = "view_width";
        if (i > 0) {
            vars.put(str3, (double) i);
        }
        i = this.mRawHeight;
        String str4 = "view_height";
        if (i > 0) {
            vars.put(str4, (double) i);
        }
        vars.put(str3, (double) (((float) this.mTargetScreenWidth) / this.mScale));
        vars.put(str4, (double) (((float) this.mTargetScreenHeight) / this.mScale));
        vars.put(RAW_DENSITY, (double) this.mRawTargetDensity);
        vars.put(SCALE_FACTOR, (double) this.mScale);
        vars.put(VAR_MAML_VERSION, 4.0d);
        i = 0;
        try {
            if (!(this.mContext == null || this.mContext.mContext == null)) {
                PackageManager packageManager = this.mContext.mContext.getPackageManager();
                if (packageManager != null) {
                    PackageInfo themeManagerInfo = packageManager.getPackageInfo(THEMEMANAGER_PACKAGE_NAME, 0);
                    if (themeManagerInfo != null) {
                        i = themeManagerInfo.versionCode;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(str2, "thememanager not found");
        }
        vars.put(VariableNames.VAR_THEMEMANAGER_VERSION, (double) i);
        vars.put(VariableNames.VAR_MIUI_VERSION_NAME, SystemProperties.get(MIUI_VERSION_NAME));
        vars.put(VariableNames.VAR_MIUI_VERSION_CODE, SystemProperties.get(MIUI_VERSION_CODE));
        vars.put(VariableNames.VAR_ANDROID_VERSION, VERSION.RELEASE);
        vars.put(VariableNames.VAR_SYSTEM_VERSION, VERSION.INCREMENTAL);
        loadConfig();
        VariableUpdaterManager variableUpdaterManager = this.mVariableUpdaterManager;
        if (variableUpdaterManager != null) {
            variableUpdaterManager.init();
        }
        VariableBinderManager variableBinderManager = this.mVariableBinderManager;
        if (variableBinderManager != null) {
            variableBinderManager.init();
        }
        CommandTriggers commandTriggers = this.mExternalCommandManager;
        if (commandTriggers != null) {
            commandTriggers.init();
        }
        this.mInnerGroup.performAction(str);
        this.mInnerGroup.init();
        this.mInnerGroup.performAction("postInit");
        this.mRoot.mHoverElement = null;
        this.mNeedReset = true;
        requestUpdate();
    }

    public void loadConfig() {
        loadConfig(this.mConfigPath);
    }

    private void loadConfig(String path) {
        if (path != null) {
            this.mConfig = new ConfigFile();
            if (!this.mConfig.load(path)) {
                this.mConfig.loadDefaultSettings(this.mContext.mResourceManager.getConfigRoot());
            }
            for (Variable v : this.mConfig.getVariables()) {
                if (TextUtils.equals(v.type, "string")) {
                    Utils.putVariableString(v.name, this.mContext.mVariables, v.value);
                } else if (TextUtils.equals(v.type, "number")) {
                    try {
                        Utils.putVariableNumber(v.name, this.mContext.mVariables, Double.parseDouble(v.value));
                    } catch (NumberFormatException e) {
                    }
                }
            }
            for (Task t : this.mConfig.getTasks()) {
                Variables variables = this.mContext.mVariables;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(t.id);
                stringBuilder.append(".name");
                variables.put(stringBuilder.toString(), t.name);
                variables = this.mContext.mVariables;
                stringBuilder = new StringBuilder();
                stringBuilder.append(t.id);
                stringBuilder.append(".package");
                variables.put(stringBuilder.toString(), t.packageName);
                variables = this.mContext.mVariables;
                stringBuilder = new StringBuilder();
                stringBuilder.append(t.id);
                stringBuilder.append(".class");
                variables.put(stringBuilder.toString(), t.className);
            }
        }
    }

    public void pause() {
        super.pause();
        String str = "pause";
        Log.d(LOG_TAG, str);
        this.mInnerGroup.performAction(str);
        this.mInnerGroup.pause();
        this.mSoundManager.pause();
        VariableBinderManager variableBinderManager = this.mVariableBinderManager;
        if (variableBinderManager != null) {
            variableBinderManager.pause();
        }
        CommandTriggers commandTriggers = this.mExternalCommandManager;
        if (commandTriggers != null) {
            commandTriggers.pause();
        }
        VariableUpdaterManager variableUpdaterManager = this.mVariableUpdaterManager;
        if (variableUpdaterManager != null) {
            variableUpdaterManager.pause();
        }
        this.mContext.mResourceManager.pause();
        onHoverChange(null, null);
        ConfigFile configFile = this.mConfig;
        if (configFile != null) {
            configFile.save(this.mContext.mContext.getApplicationContext());
        }
    }

    public void resume() {
        super.resume();
        String str = "resume";
        Log.d(LOG_TAG, str);
        this.mShowDebugLayout = SysProperties.isShowDebugLayout();
        this.mInnerGroup.performAction(str);
        this.mInnerGroup.resume();
        VariableBinderManager variableBinderManager = this.mVariableBinderManager;
        if (variableBinderManager != null) {
            variableBinderManager.resume();
        }
        CommandTriggers commandTriggers = this.mExternalCommandManager;
        if (commandTriggers != null) {
            commandTriggers.resume();
        }
        VariableUpdaterManager variableUpdaterManager = this.mVariableUpdaterManager;
        if (variableUpdaterManager != null) {
            variableUpdaterManager.resume();
        }
        this.mContext.mResourceManager.resume();
    }

    public boolean onTouch(MotionEvent event) {
        if (this.mFinished) {
            return false;
        }
        float hoverWidth = this.mHoverElement;
        if (hoverWidth != null) {
            hoverWidth = hoverWidth.getWidth();
            this.mHoverMatrix.setTranslate((this.mHoverElement.getAbsoluteLeft() + (hoverWidth / 2.0f)) - event.getX(), (this.mHoverElement.getAbsoluteTop() + (this.mHoverElement.getHeight() / 2.0f)) - event.getY());
            event.transform(this.mHoverMatrix);
            this.mHoverElement.onTouch(event);
            if (event.getActionMasked() == 1 || event.getActionMasked() == 3) {
                this.mHoverElement = null;
            }
            return true;
        }
        double x = descale((double) event.getX());
        double y = descale((double) event.getY());
        this.mTouchX.set(x);
        this.mTouchY.set(y);
        int actionMasked = event.getActionMasked();
        if (actionMasked == 0) {
            this.mTouchBeginX.set(x);
            this.mTouchBeginY.set(y);
            this.mTouchBeginTime.set((double) System.currentTimeMillis());
            this.mNeedDisallowInterceptTouchEvent = false;
        } else if (actionMasked == 1) {
            this.mNeedDisallowInterceptTouchEvent = false;
        } else if (actionMasked != 2) {
        }
        boolean ret = this.mInnerGroup.onTouch(event);
        if (!ret) {
            this.mController.requestUpdate();
        }
        return ret;
    }

    public boolean onHover(MotionEvent event) {
        return this.mFinished ? false : this.mInnerGroup.onHover(event);
    }

    public boolean needDisallowInterceptTouchEvent() {
        return this.mNeedDisallowInterceptTouchEvent;
    }

    /* Access modifiers changed, original: protected */
    public boolean onLoad(Element root) {
        return true;
    }

    public final boolean load() {
        try {
            long start = SystemClock.elapsedRealtime();
            Element root = this.mContext.mResourceManager.getManifestRoot();
            String str = LOG_TAG;
            if (root == null) {
                Log.e(str, "load error, manifest root is null");
                return false;
            }
            this.mRootTag = root.getNodeName();
            loadRawAttrs(root);
            processUseVariableUpdater(root);
            setupScale(root);
            this.mVariableBinderManager = new VariableBinderManager(Utils.getChild(root, VariableBinderManager.TAG_NAME), this);
            Element commands = Utils.getChild(root, EXTERNAL_COMMANDS_TAG_NAME);
            if (commands != null) {
                this.mExternalCommandManager = new CommandTriggers(commands, this);
            }
            Element styles = Utils.getChild(root, "Styles");
            if (styles != null) {
                this.mStylesManager = new StylesManager(styles);
            }
            this.mFrameRate = Utils.getAttrAsFloat(root, "frameRate", this.DEFAULT_FRAME_RATE);
            this.mClearCanvas = Boolean.parseBoolean(root.getAttribute("clearCanvas"));
            this.mSupportAccessibilityService = Boolean.parseBoolean(root.getAttribute("supportAccessibityService"));
            this.mAllowScreenRotation = Boolean.parseBoolean(root.getAttribute("allowScreenRotation"));
            this.mBlurWindow = Boolean.parseBoolean(root.getAttribute("blurWindow"));
            this.mController = new RendererController();
            this.mInnerGroup = new InnerGroup(root, this);
            if (this.mInnerGroup.getElements().size() <= 0) {
                Log.e(str, "load error, no element loaded");
                return false;
            }
            this.mVersion = Utils.getAttrAsInt(root, "version", 1);
            if (onLoad(root)) {
                traverseElements();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("load finished, spent ");
                stringBuilder.append(SystemClock.elapsedRealtime() - start);
                stringBuilder.append(" ms");
                Log.d(str, stringBuilder.toString());
                return true;
            }
            Log.e(str, "load error, onLoad fail");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getRootTag() {
        return this.mRootTag;
    }

    public Style getStyle(String style) {
        Style style2 = null;
        if (TextUtils.isEmpty(style)) {
            return null;
        }
        StylesManager stylesManager = this.mStylesManager;
        if (stylesManager != null) {
            style2 = stylesManager.getStyle(style);
        }
        return style2;
    }

    private void traverseElements() {
        this.mRendererControllers.clear();
        acceptVisitor(new ScreenElementVisitor() {
            public void visit(ScreenElement e) {
                if (e instanceof FramerateController) {
                    RendererController c = e.getRendererController();
                    if (c != null) {
                        c.addFramerateController((FramerateController) e);
                    }
                }
                if ((e instanceof ElementGroupRC) || (e instanceof ScreenElementRoot)) {
                    ScreenElementRoot.this.mRendererControllers.add(e.getRendererController());
                }
            }
        });
    }

    public void setScaleByDensity(boolean b) {
        this.mScaleByDensity = b;
    }

    private void setupScale(Element root) {
        float scaleResourceToTarget;
        float scaleResourceToTarget2;
        Element element = root;
        String scaleByDensity = element.getAttribute("scaleByDensity");
        if (!TextUtils.isEmpty(scaleByDensity)) {
            this.mScaleByDensity = Boolean.parseBoolean(scaleByDensity);
        }
        this.mDefaultScreenWidth = Utils.getAttrAsInt(element, "defaultScreenWidth", 0);
        if (this.mDefaultScreenWidth == 0) {
            this.mDefaultScreenWidth = Utils.getAttrAsInt(element, "screenWidth", 0);
        }
        this.mRawDefaultResourceDensity = Utils.getAttrAsInt(element, "defaultResourceDensity", 0);
        if (this.mRawDefaultResourceDensity == 0) {
            this.mRawDefaultResourceDensity = Utils.getAttrAsInt(element, "resDensity", 0);
        }
        this.mDefaultResourceDensity = ResourceManager.translateDensity(this.mRawDefaultResourceDensity);
        if (this.mDefaultScreenWidth == 0 && this.mDefaultResourceDensity == 0) {
            this.mDefaultScreenWidth = 480;
            this.mDefaultResourceDensity = 240;
        } else {
            int i = this.mDefaultResourceDensity;
            if (i == 0) {
                this.mDefaultResourceDensity = (this.mDefaultScreenWidth * 240) / 480;
            } else if (this.mDefaultScreenWidth == 0) {
                this.mDefaultScreenWidth = (i * 480) / 240;
            }
        }
        this.mContext.mResourceManager.setDefaultResourceDensity(this.mDefaultResourceDensity);
        WindowManager wm = (WindowManager) this.mContext.mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int rotation = display.getRotation();
        boolean rotated = true;
        if (!(rotation == 1 || rotation == 3)) {
            rotated = false;
        }
        this.mTargetScreenWidth = rotated ? size.y : size.x;
        this.mTargetScreenHeight = rotated ? size.x : size.y;
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        this.mRawTargetDensity = outMetrics.densityDpi;
        int targetSizeType = this.mContext.mContext.getResources().getConfiguration().screenLayout & 15;
        int rawDefaultResourceDen = this.mRawDefaultResourceDensity;
        if (rawDefaultResourceDen == 0) {
            rawDefaultResourceDen = (this.mDefaultScreenWidth * 240) / 480;
        }
        ExtraResource resources = new ExtraResource(element, rawDefaultResourceDen);
        Resource res = resources.findResource(this.mRawTargetDensity, this.mTargetScreenWidth, targetSizeType);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("findResource: ");
        stringBuilder.append(res.toString());
        String stringBuilder2 = stringBuilder.toString();
        String str = LOG_TAG;
        Log.d(str, stringBuilder2);
        int extraResDen = (int) (((float) ResourceManager.translateDensity(res.mDensity)) / res.mScale);
        this.mContext.mResourceManager.setExtraResource(res.mPath, extraResDen);
        ScaleMetrics scale = resources.findScale(this.mRawTargetDensity, this.mTargetScreenWidth, targetSizeType);
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("findScale: ");
        stringBuilder3.append(scale.toString());
        Log.d(str, stringBuilder3.toString());
        if (this.mScaleByDensity) {
            this.mTargetDensity = ResourceManager.translateDensity(this.mRawTargetDensity);
            if (scale.mScale <= 0.0f) {
                scaleResourceToTarget = 1.0f;
                this.mScale = ((float) this.mTargetDensity) / ((float) this.mDefaultResourceDensity);
            } else {
                scaleResourceToTarget = 1.0f;
                scaleResourceToTarget2 = (((float) this.mRawTargetDensity) * 1.0f) / ((float) scale.mDensity);
                this.mScale = scale.mScale * scaleResourceToTarget2;
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("set scale: ");
                stringBuilder3.append(this.mScale);
                Log.i(str, stringBuilder3.toString());
                this.mContext.mResourceManager.setTargetDensity(this.mTargetDensity);
                this.mRawWidth = Utils.getAttrAsInt(element, "width", 0);
                this.mRawHeight = Utils.getAttrAsInt(element, "height", 0);
                this.mWidth = (float) Math.round(((float) this.mRawWidth) * this.mScale);
                this.mHeight = (float) Math.round(((float) this.mRawHeight) * this.mScale);
            }
        }
        scaleResourceToTarget = 1.0f;
        this.mScale = ((float) this.mTargetScreenWidth) / ((float) this.mDefaultScreenWidth);
        this.mTargetDensity = (int) (((float) this.mDefaultResourceDensity) * this.mScale);
        if (scale.mScale > 0.0f) {
            scaleResourceToTarget2 = (((float) this.mTargetScreenWidth) * 1.0f) / ((float) scale.mScreenWidth);
            this.mScale = scale.mScale * scaleResourceToTarget2;
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append("set scale: ");
            stringBuilder3.append(this.mScale);
            Log.i(str, stringBuilder3.toString());
            this.mContext.mResourceManager.setTargetDensity(this.mTargetDensity);
            this.mRawWidth = Utils.getAttrAsInt(element, "width", 0);
            this.mRawHeight = Utils.getAttrAsInt(element, "height", 0);
            this.mWidth = (float) Math.round(((float) this.mRawWidth) * this.mScale);
            this.mHeight = (float) Math.round(((float) this.mRawHeight) * this.mScale);
        }
        scaleResourceToTarget2 = scaleResourceToTarget;
        stringBuilder3 = new StringBuilder();
        stringBuilder3.append("set scale: ");
        stringBuilder3.append(this.mScale);
        Log.i(str, stringBuilder3.toString());
        this.mContext.mResourceManager.setTargetDensity(this.mTargetDensity);
        this.mRawWidth = Utils.getAttrAsInt(element, "width", 0);
        this.mRawHeight = Utils.getAttrAsInt(element, "height", 0);
        this.mWidth = (float) Math.round(((float) this.mRawWidth) * this.mScale);
        this.mHeight = (float) Math.round(((float) this.mRawHeight) * this.mScale);
    }

    private void loadRawAttrs(Element root) {
        NamedNodeMap nnm = root.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            Node item = nnm.item(i);
            this.mRawAttrs.put(item.getNodeName(), item.getNodeValue());
        }
    }

    private void processUseVariableUpdater(Element root) {
        String updater = root.getAttribute("useVariableUpdater");
        if (TextUtils.isEmpty(updater)) {
            onAddVariableUpdater(this.mVariableUpdaterManager);
        } else {
            this.mVariableUpdaterManager.addFromTag(updater);
        }
    }

    public int playSound(String sound) {
        return playSound(sound, new SoundOptions(false, false, 1.0f));
    }

    public int playSound(String sound, SoundOptions options) {
        if (!TextUtils.isEmpty(sound) && shouldPlaySound()) {
            return this.mSoundManager.playSound(sound, options);
        }
        return 0;
    }

    public void playSound(int streamId, Command command) {
        try {
            this.mSoundManager.playSound(streamId, command);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    public void haptic(int effectId) {
    }

    public Task findTask(String id) {
        ConfigFile configFile = this.mConfig;
        return configFile == null ? null : configFile.getTask(id);
    }

    /* Access modifiers changed, original: protected */
    public boolean shouldPlaySound() {
        return true;
    }

    public final float getScale() {
        float f = this.mScale;
        if (f != 0.0f) {
            return f;
        }
        Log.w(LOG_TAG, "scale not initialized!");
        return 1.0f;
    }

    public int getResourceDensity() {
        return this.mDefaultResourceDensity;
    }

    public int getTargetDensity() {
        return this.mTargetDensity;
    }

    public int getDefaultScreenWidth() {
        return this.mDefaultScreenWidth;
    }

    public final void setKeepResource(boolean b) {
        this.mKeepResource = b;
    }

    public void finish() {
        if (!this.mFinished) {
            super.finish();
            String str = "finish";
            Log.d(LOG_TAG, str);
            this.mInnerGroup.performAction("preFinish");
            this.mInnerGroup.finish();
            this.mInnerGroup.performAction(str);
            ConfigFile configFile = this.mConfig;
            if (configFile != null) {
                configFile.save(this.mContext.mContext.getApplicationContext());
            }
            VariableBinderManager variableBinderManager = this.mVariableBinderManager;
            if (variableBinderManager != null) {
                variableBinderManager.finish();
            }
            CommandTriggers commandTriggers = this.mExternalCommandManager;
            if (commandTriggers != null) {
                commandTriggers.finish();
            }
            VariableUpdaterManager variableUpdaterManager = this.mVariableUpdaterManager;
            if (variableUpdaterManager != null) {
                variableUpdaterManager.finish();
            }
            this.mSoundManager.release();
            this.mContext.mResourceManager.finish(this.mKeepResource);
            this.mFinished = true;
            this.mKeepResource = false;
        }
    }

    public void onCommand(final String command) {
        if (this.mExternalCommandManager != null) {
            postRunnable(new Runnable() {
                public void run() {
                    try {
                        ScreenElementRoot.this.mExternalCommandManager.onAction(command);
                    } catch (Exception e) {
                        Log.e(ScreenElementRoot.LOG_TAG, e.toString());
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void onUIInteractive(ScreenElement e, String action) {
    }

    public int getScreenWidth() {
        return this.mTargetScreenWidth;
    }

    public int getScreenHeight() {
        return this.mTargetScreenHeight;
    }

    public float getWidth() {
        return this.mWidth;
    }

    public float getHeight() {
        return this.mHeight;
    }

    public void addPreTicker(ITicker ticker) {
        this.mPreTickers.add(ticker);
    }

    public void removePreTicker(ITicker ticker) {
        this.mPreTickers.remove(ticker);
    }

    public void setOnExternCommandListener(OnExternCommandListener l) {
        this.mExternCommandListener = l == null ? null : new WeakReference(l);
    }

    public void issueExternCommand(String command, Double para1, String para2) {
        this.mSystemExternCommandListener.onCommand(command, para1, para2);
        WeakReference weakReference = this.mExternCommandListener;
        if (weakReference != null) {
            OnExternCommandListener l = (OnExternCommandListener) weakReference.get();
            if (l != null) {
                l.onCommand(command, para1, para2);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("issueExternCommand: ");
                stringBuilder.append(command);
                String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
                stringBuilder.append(str);
                stringBuilder.append(para1);
                stringBuilder.append(str);
                stringBuilder.append(para2);
                Log.d(LOG_TAG, stringBuilder.toString());
            }
        }
    }

    public void setOnHoverChangeListener(OnHoverChangeListener l) {
        this.mHoverChangeListenerRef = new WeakReference(l);
    }

    public void onHoverChange(AnimatedScreenElement hoverElement, String contentDescription) {
        this.mHoverElement = hoverElement;
        OnHoverChangeListener listener = null;
        WeakReference weakReference = this.mHoverChangeListenerRef;
        if (weakReference != null) {
            listener = (OnHoverChangeListener) weakReference.get();
        }
        if (listener != null) {
            listener.onHoverChange(contentDescription);
        }
    }

    public AnimatedScreenElement getHoverElement() {
        return this.mHoverElement;
    }

    public void saveVar(String name, Double value) {
        ConfigFile configFile = this.mConfig;
        if (configFile == null) {
            Log.w(LOG_TAG, "fail to saveVar, config file is null");
            return;
        }
        if (value == null) {
            configFile.putNumber(name, "null");
        } else {
            configFile.putNumber(name, value.doubleValue());
        }
    }

    public void saveVar(String name, String value) {
        ConfigFile configFile = this.mConfig;
        if (configFile == null) {
            Log.w(LOG_TAG, "fail to saveVar, config file is null");
        } else {
            configFile.putString(name, value);
        }
    }

    public void selfInit() {
        this.mController.init();
    }

    public void selfFinish() {
        this.mController.finish();
    }

    public void selfPause() {
        int N = this.mRendererControllers.size();
        for (int i = 0; i < N; i++) {
            ((RendererController) this.mRendererControllers.get(i)).selfPause();
        }
    }

    public void selfResume() {
        int N = this.mRendererControllers.size();
        for (int i = 0; i < N; i++) {
            ((RendererController) this.mRendererControllers.get(i)).selfResume();
        }
    }

    public void setRenderControllerListener(Listener l) {
        this.mController.setListener(l);
    }

    public void setRenderControllerRenderable(IRenderable r) {
        setRenderControllerListener(new SingleRootListener(this, r));
    }

    public void postMessage(MotionEvent e) {
        this.mController.postMessage(e);
    }

    public long updateIfNeeded(long currentTime) {
        long nextUpdateInterval = Long.MAX_VALUE;
        int N = this.mRendererControllers.size();
        for (int i = 0; i < N; i++) {
            RendererController c = (RendererController) this.mRendererControllers.get(i);
            if (!c.isSelfPaused() || c.hasRunnable()) {
                long l = c.updateIfNeeded(currentTime);
                if (l < nextUpdateInterval) {
                    nextUpdateInterval = l;
                }
            }
        }
        return nextUpdateInterval;
    }

    public long update(long currentTime) {
        long nextUpdateInterval = Long.MAX_VALUE;
        int N = this.mRendererControllers.size();
        for (int i = 0; i < N; i++) {
            RendererController c = (RendererController) this.mRendererControllers.get(i);
            if (!c.isSelfPaused() || c.hasRunnable()) {
                long l = c.update(currentTime);
                if (l < nextUpdateInterval) {
                    nextUpdateInterval = l;
                }
            }
        }
        return nextUpdateInterval;
    }

    public void requestUpdate() {
        int N = this.mRendererControllers.size();
        for (int i = 0; i < N; i++) {
            ((RendererController) this.mRendererControllers.get(i)).requestUpdate();
        }
    }

    public void attachToRenderThread(RenderThread t) {
        if (t == null || this.mController == null) {
            throw new NullPointerException("thread or controller is null, MUST load before attaching");
        }
        int N = this.mRendererControllers.size();
        for (int i = 0; i < N; i++) {
            t.addRendererController((RendererController) this.mRendererControllers.get(i));
        }
    }

    public void detachFromRenderThread(RenderThread t) {
        if (t == null || this.mController == null) {
            throw new NullPointerException("thread or controller is null, MUST load before detaching");
        }
        int N = this.mRendererControllers.size();
        for (int i = 0; i < N; i++) {
            t.removeRendererController((RendererController) this.mRendererControllers.get(i));
        }
    }

    public void doneRender() {
        this.mController.doneRender();
    }

    /* Access modifiers changed, original: protected */
    public void playAnim(long time, long startTime, long endTime, boolean isLoop, boolean isDelay) {
        super.playAnim(time, startTime, endTime, isLoop, isDelay);
        this.mInnerGroup.playAnim(time, startTime, endTime, isLoop, isDelay);
    }

    /* Access modifiers changed, original: protected */
    public void pauseAnim(long time) {
        super.pauseAnim(time);
        this.mInnerGroup.pauseAnim(time);
    }

    /* Access modifiers changed, original: protected */
    public void resumeAnim(long time) {
        super.resumeAnim(time);
        this.mInnerGroup.resumeAnim(time);
    }

    public void reset(long time) {
        super.reset(time);
        this.mInnerGroup.reset(time);
    }

    public void showCategory(String category, boolean show) {
        this.mInnerGroup.showCategory(category, show);
    }

    public void acceptVisitor(ScreenElementVisitor v) {
        super.acceptVisitor(v);
        this.mInnerGroup.acceptVisitor(v);
    }

    public RendererController getRendererController() {
        return this.mController;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.mAllowScreenRotation) {
            setConfiguration(newConfig);
            onCommand("orientationChange");
            requestUpdate();
        }
    }

    public boolean allowScreenRotation() {
        return this.mAllowScreenRotation;
    }

    public void setConfiguration(Configuration newConfig) {
        if (this.mAllowScreenRotation) {
            Variables vars = this.mContext.mVariables;
            Utils.putVariableNumber("orientation", this.mContext.mVariables, Double.valueOf((double) newConfig.orientation));
            int i = newConfig.orientation;
            String str = VariableNames.SCREEN_HEIGHT;
            String str2 = VariableNames.SCREEN_WIDTH;
            String str3 = VariableNames.RAW_SCREEN_HEIGHT;
            String str4 = VariableNames.RAW_SCREEN_WIDTH;
            if (i == 1) {
                vars.put(str4, (double) this.mTargetScreenWidth);
                vars.put(str3, (double) this.mTargetScreenHeight);
                vars.put(str2, (double) (((float) this.mTargetScreenWidth) / this.mScale));
                vars.put(str, (double) (((float) this.mTargetScreenHeight) / this.mScale));
            } else if (i == 2) {
                vars.put(str4, (double) this.mTargetScreenHeight);
                vars.put(str3, (double) this.mTargetScreenWidth);
                vars.put(str2, (double) (((float) this.mTargetScreenHeight) / this.mScale));
                vars.put(str, (double) (((float) this.mTargetScreenWidth) / this.mScale));
            }
        }
    }

    public void setCapability(int cap, boolean enable) {
        if (enable) {
            this.mCapability |= cap;
        } else {
            this.mCapability &= ~cap;
        }
    }

    public boolean isSupportAccessibilityService() {
        return this.mSupportAccessibilityService;
    }

    public boolean getCapability(int cap) {
        return (this.mCapability & cap) != 0;
    }

    public void setBgColor(int color) {
        this.mBgColor = color;
    }

    public void setClearCanvas(boolean clear) {
        this.mClearCanvas = clear;
    }

    public void setViewManager(ViewManager vm) {
        this.mViewManager = vm;
    }

    public ViewManager getViewManager() {
        return this.mViewManager;
    }

    public final int version() {
        return this.mVersion;
    }

    public final float getFontScale() {
        return this.mFontScale;
    }

    public boolean isMamlBlurWindow() {
        return this.mBlurWindow;
    }
}
