package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityOptions;
import android.app.ActivityThread;
import android.app.Application;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.appwidget.AppWidgetHostView;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.StrictMode;
import android.os.UserHandle;
import android.telecom.Logging.Session;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.IntArray;
import android.util.Log;
import android.util.Pair;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Filter;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewStub.ViewReplaceRunnable;
import android.widget.AdapterView.OnItemClickListener;
import com.android.internal.R;
import com.android.internal.util.ContrastColorUtil;
import com.android.internal.util.Preconditions;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class RemoteViews implements Parcelable, Filter {
    private static final Action ACTION_NOOP = new RuntimeAction() {
        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
        }
    };
    private static final int BITMAP_REFLECTION_ACTION_TAG = 12;
    public static final Creator<RemoteViews> CREATOR = new Creator<RemoteViews>() {
        public RemoteViews createFromParcel(Parcel parcel) {
            return new RemoteViews(parcel);
        }

        public RemoteViews[] newArray(int size) {
            return new RemoteViews[size];
        }
    };
    private static final OnClickHandler DEFAULT_ON_CLICK_HANDLER = -$$Lambda$RemoteViews$xYCMzfQwRCAW2azHo-bWqQ9R0Wk.INSTANCE;
    static final String EXTRA_REMOTEADAPTER_APPWIDGET_ID = "remoteAdapterAppWidgetId";
    static final String EXTRA_REMOTEADAPTER_ON_LIGHT_BACKGROUND = "remoteAdapterOnLightBackground";
    public static final String EXTRA_SHARED_ELEMENT_BOUNDS = "android.widget.extra.SHARED_ELEMENT_BOUNDS";
    public static final int FLAG_REAPPLY_DISALLOWED = 1;
    public static final int FLAG_USE_LIGHT_BACKGROUND_LAYOUT = 4;
    public static final int FLAG_WIDGET_IS_COLLECTION_CHILD = 2;
    private static final int LAYOUT_PARAM_ACTION_TAG = 19;
    private static final String LOG_TAG = "RemoteViews";
    private static final int MAX_NESTED_VIEWS = 10;
    private static final int MODE_HAS_LANDSCAPE_AND_PORTRAIT = 1;
    private static final int MODE_NORMAL = 0;
    private static final int OVERRIDE_TEXT_COLORS_TAG = 20;
    private static final int REFLECTION_ACTION_TAG = 2;
    private static final int SET_DRAWABLE_TINT_TAG = 3;
    private static final int SET_EMPTY_VIEW_ACTION_TAG = 6;
    private static final int SET_INT_TAG_TAG = 22;
    private static final int SET_ON_CLICK_RESPONSE_TAG = 1;
    private static final int SET_PENDING_INTENT_TEMPLATE_TAG = 8;
    private static final int SET_REMOTE_INPUTS_ACTION_TAG = 18;
    private static final int SET_REMOTE_VIEW_ADAPTER_INTENT_TAG = 10;
    private static final int SET_REMOTE_VIEW_ADAPTER_LIST_TAG = 15;
    private static final int SET_RIPPLE_DRAWABLE_COLOR_TAG = 21;
    private static final int TEXT_VIEW_DRAWABLE_ACTION_TAG = 11;
    private static final int TEXT_VIEW_SIZE_ACTION_TAG = 13;
    private static final int VIEW_CONTENT_NAVIGATION_TAG = 5;
    private static final int VIEW_GROUP_ACTION_ADD_TAG = 4;
    private static final int VIEW_GROUP_ACTION_REMOVE_TAG = 7;
    private static final int VIEW_PADDING_ACTION_TAG = 14;
    private static final MethodKey sLookupKey = new MethodKey();
    private static final ArrayMap<MethodKey, MethodArgs> sMethods = new ArrayMap();
    @UnsupportedAppUsage
    private ArrayList<Action> mActions;
    @UnsupportedAppUsage
    public ApplicationInfo mApplication;
    private int mApplyFlags;
    @UnsupportedAppUsage
    private BitmapCache mBitmapCache;
    private final Map<Class, Object> mClassCookies;
    private boolean mIsRoot;
    private RemoteViews mLandscape;
    @UnsupportedAppUsage
    private final int mLayoutId;
    private int mLightBackgroundLayoutId;
    @UnsupportedAppUsage
    private RemoteViews mPortrait;

    public interface OnClickHandler {
        boolean onClickHandler(View view, PendingIntent pendingIntent, RemoteResponse remoteResponse);
    }

    private static abstract class Action implements Parcelable {
        public static final int MERGE_APPEND = 1;
        public static final int MERGE_IGNORE = 2;
        public static final int MERGE_REPLACE = 0;
        @UnsupportedAppUsage
        int viewId;

        public abstract void apply(View view, ViewGroup viewGroup, OnClickHandler onClickHandler) throws ActionException;

        public abstract int getActionTag();

        private Action() {
        }

        /* synthetic */ Action(AnonymousClass1 x0) {
            this();
        }

        public int describeContents() {
            return 0;
        }

        public void setBitmapCache(BitmapCache bitmapCache) {
        }

        @UnsupportedAppUsage
        public int mergeBehavior() {
            return 0;
        }

        public String getUniqueKey() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getActionTag());
            stringBuilder.append(Session.SESSION_SEPARATION_CHAR_CHILD);
            stringBuilder.append(this.viewId);
            return stringBuilder.toString();
        }

        public Action initActionAsync(ViewTree root, ViewGroup rootParent, OnClickHandler handler) {
            return this;
        }

        public boolean prefersAsyncApply() {
            return false;
        }

        public boolean hasSameAppInfo(ApplicationInfo parentInfo) {
            return true;
        }

        public void visitUris(Consumer<Uri> consumer) {
        }
    }

    private static abstract class RuntimeAction extends Action {
        private RuntimeAction() {
            super();
        }

        /* synthetic */ RuntimeAction(AnonymousClass1 x0) {
            this();
        }

        public final int getActionTag() {
            return 0;
        }

        public final void writeToParcel(Parcel dest, int flags) {
            throw new UnsupportedOperationException();
        }
    }

    public static class ActionException extends RuntimeException {
        public ActionException(Exception ex) {
            super(ex);
        }

        public ActionException(String message) {
            super(message);
        }

        public ActionException(Throwable t) {
            super(t);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ApplyFlags {
    }

    private class AsyncApplyTask extends AsyncTask<Void, Void, ViewTree> implements OnCancelListener {
        private Action[] mActions;
        final Context mContext;
        private Exception mError;
        final OnClickHandler mHandler;
        final OnViewAppliedListener mListener;
        final ViewGroup mParent;
        final RemoteViews mRV;
        private View mResult;
        private ViewTree mTree;

        /* synthetic */ AsyncApplyTask(RemoteViews x0, RemoteViews x1, ViewGroup x2, Context x3, OnViewAppliedListener x4, OnClickHandler x5, View x6, AnonymousClass1 x7) {
            this(x1, x2, x3, x4, x5, x6);
        }

        private AsyncApplyTask(RemoteViews rv, ViewGroup parent, Context context, OnViewAppliedListener listener, OnClickHandler handler, View result) {
            this.mRV = rv;
            this.mParent = parent;
            this.mContext = context;
            this.mListener = listener;
            this.mHandler = handler;
            this.mResult = result;
        }

        /* Access modifiers changed, original: protected|varargs */
        public ViewTree doInBackground(Void... params) {
            try {
                if (this.mResult == null) {
                    this.mResult = RemoteViews.this.inflateView(this.mContext, this.mRV, this.mParent);
                }
                this.mTree = new ViewTree(this.mResult, null);
                if (this.mRV.mActions != null) {
                    int count = this.mRV.mActions.size();
                    this.mActions = new Action[count];
                    for (int i = 0; i < count && !isCancelled(); i++) {
                        this.mActions[i] = ((Action) this.mRV.mActions.get(i)).initActionAsync(this.mTree, this.mParent, this.mHandler);
                    }
                } else {
                    this.mActions = null;
                }
                return this.mTree;
            } catch (Exception e) {
                this.mError = e;
                return null;
            }
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(ViewTree viewTree) {
            OnViewAppliedListener onViewAppliedListener;
            Exception e;
            if (this.mError == null) {
                onViewAppliedListener = this.mListener;
                if (onViewAppliedListener != null) {
                    onViewAppliedListener.onViewInflated(viewTree.mRoot);
                }
                try {
                    if (this.mActions != null) {
                        OnClickHandler handler = this.mHandler == null ? RemoteViews.DEFAULT_ON_CLICK_HANDLER : this.mHandler;
                        for (Action a : this.mActions) {
                            a.apply(viewTree.mRoot, this.mParent, handler);
                        }
                    }
                } catch (Exception e2) {
                    this.mError = e2;
                }
            }
            onViewAppliedListener = this.mListener;
            if (onViewAppliedListener != null) {
                Exception exception = this.mError;
                if (exception != null) {
                    onViewAppliedListener.onError(exception);
                    return;
                } else {
                    onViewAppliedListener.onViewApplied(viewTree.mRoot);
                    return;
                }
            }
            e2 = this.mError;
            if (e2 == null) {
                return;
            }
            if (e2 instanceof ActionException) {
                throw ((ActionException) e2);
            }
            throw new ActionException(e2);
        }

        public void onCancel() {
            cancel(true);
        }
    }

    private static class BitmapCache {
        int mBitmapMemory;
        @UnsupportedAppUsage
        ArrayList<Bitmap> mBitmaps;

        public BitmapCache() {
            this.mBitmapMemory = -1;
            this.mBitmaps = new ArrayList();
        }

        public BitmapCache(Parcel source) {
            this.mBitmapMemory = -1;
            this.mBitmaps = source.createTypedArrayList(Bitmap.CREATOR);
        }

        public int getBitmapId(Bitmap b) {
            if (b == null) {
                return -1;
            }
            if (this.mBitmaps.contains(b)) {
                return this.mBitmaps.indexOf(b);
            }
            this.mBitmaps.add(b);
            this.mBitmapMemory = -1;
            return this.mBitmaps.size() - 1;
        }

        public Bitmap getBitmapForId(int id) {
            if (id == -1 || id >= this.mBitmaps.size()) {
                return null;
            }
            return (Bitmap) this.mBitmaps.get(id);
        }

        public void writeBitmapsToParcel(Parcel dest, int flags) {
            dest.writeTypedList(this.mBitmaps, flags);
        }

        public int getBitmapMemory() {
            if (this.mBitmapMemory < 0) {
                this.mBitmapMemory = 0;
                int count = this.mBitmaps.size();
                for (int i = 0; i < count; i++) {
                    this.mBitmapMemory += ((Bitmap) this.mBitmaps.get(i)).getAllocationByteCount();
                }
            }
            return this.mBitmapMemory;
        }
    }

    private class BitmapReflectionAction extends Action {
        @UnsupportedAppUsage
        Bitmap bitmap;
        int bitmapId;
        @UnsupportedAppUsage
        String methodName;

        BitmapReflectionAction(int viewId, String methodName, Bitmap bitmap) {
            super();
            this.bitmap = bitmap;
            this.viewId = viewId;
            this.methodName = methodName;
            this.bitmapId = RemoteViews.this.mBitmapCache.getBitmapId(bitmap);
        }

        BitmapReflectionAction(Parcel in) {
            super();
            this.viewId = in.readInt();
            this.methodName = in.readString();
            this.bitmapId = in.readInt();
            this.bitmap = RemoteViews.this.mBitmapCache.getBitmapForId(this.bitmapId);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.viewId);
            dest.writeString(this.methodName);
            dest.writeInt(this.bitmapId);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) throws ActionException {
            new ReflectionAction(this.viewId, this.methodName, 12, this.bitmap).apply(root, rootParent, handler);
        }

        public void setBitmapCache(BitmapCache bitmapCache) {
            this.bitmapId = bitmapCache.getBitmapId(this.bitmap);
        }

        public int getActionTag() {
            return 12;
        }
    }

    private static class LayoutParamAction extends Action {
        public static final int LAYOUT_MARGIN_BOTTOM_DIMEN = 3;
        public static final int LAYOUT_MARGIN_END = 4;
        public static final int LAYOUT_MARGIN_END_DIMEN = 1;
        public static final int LAYOUT_WIDTH = 2;
        final int mProperty;
        final int mValue;

        public LayoutParamAction(int viewId, int property, int value) {
            super();
            this.viewId = viewId;
            this.mProperty = property;
            this.mValue = value;
        }

        public LayoutParamAction(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.mProperty = parcel.readInt();
            this.mValue = parcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.viewId);
            dest.writeInt(this.mProperty);
            dest.writeInt(this.mValue);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                LayoutParams layoutParams = target.getLayoutParams();
                if (layoutParams != null) {
                    int value = this.mValue;
                    int i = this.mProperty;
                    if (i != 1) {
                        if (i == 2) {
                            layoutParams.width = this.mValue;
                            target.setLayoutParams(layoutParams);
                        } else if (i != 3) {
                            if (i != 4) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Unknown property ");
                                stringBuilder.append(this.mProperty);
                                throw new IllegalArgumentException(stringBuilder.toString());
                            }
                        } else if (layoutParams instanceof MarginLayoutParams) {
                            ((MarginLayoutParams) layoutParams).bottomMargin = resolveDimenPixelOffset(target, this.mValue);
                            target.setLayoutParams(layoutParams);
                        }
                    }
                    value = resolveDimenPixelOffset(target, this.mValue);
                    if (layoutParams instanceof MarginLayoutParams) {
                        ((MarginLayoutParams) layoutParams).setMarginEnd(value);
                        target.setLayoutParams(layoutParams);
                    }
                }
            }
        }

        private static int resolveDimenPixelOffset(View target, int value) {
            if (value == 0) {
                return 0;
            }
            return target.getContext().getResources().getDimensionPixelOffset(value);
        }

        public int getActionTag() {
            return 19;
        }

        public String getUniqueKey() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(super.getUniqueKey());
            stringBuilder.append(this.mProperty);
            return stringBuilder.toString();
        }
    }

    static class MethodArgs {
        public MethodHandle asyncMethod;
        public String asyncMethodName;
        public MethodHandle syncMethod;

        MethodArgs() {
        }
    }

    static class MethodKey {
        public String methodName;
        public Class paramClass;
        public Class targetClass;

        MethodKey() {
        }

        public boolean equals(Object o) {
            boolean z = false;
            if (!(o instanceof MethodKey)) {
                return false;
            }
            MethodKey p = (MethodKey) o;
            if (Objects.equals(p.targetClass, this.targetClass) && Objects.equals(p.paramClass, this.paramClass) && Objects.equals(p.methodName, this.methodName)) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return (Objects.hashCode(this.targetClass) ^ Objects.hashCode(this.paramClass)) ^ Objects.hashCode(this.methodName);
        }

        public void set(Class targetClass, Class paramClass, String methodName) {
            this.targetClass = targetClass;
            this.paramClass = paramClass;
            this.methodName = methodName;
        }
    }

    public interface OnViewAppliedListener {
        void onError(Exception exception);

        void onViewApplied(View view);

        void onViewInflated(View v) {
        }
    }

    private class OverrideTextColorsAction extends Action {
        private final int textColor;

        public OverrideTextColorsAction(int textColor) {
            super();
            this.textColor = textColor;
        }

        public OverrideTextColorsAction(Parcel parcel) {
            super();
            this.textColor = parcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.textColor);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            Stack<View> viewsToProcess = new Stack();
            viewsToProcess.add(root);
            while (!viewsToProcess.isEmpty()) {
                View v = (View) viewsToProcess.pop();
                if (v instanceof TextView) {
                    TextView textView = (TextView) v;
                    textView.setText(ContrastColorUtil.clearColorSpans(textView.getText()));
                    textView.setTextColor(this.textColor);
                }
                if (v instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) v;
                    for (int i = 0; i < viewGroup.getChildCount(); i++) {
                        viewsToProcess.push(viewGroup.getChildAt(i));
                    }
                }
            }
        }

        public int getActionTag() {
            return 20;
        }
    }

    private final class ReflectionAction extends Action {
        static final int BITMAP = 12;
        static final int BOOLEAN = 1;
        static final int BUNDLE = 13;
        static final int BYTE = 2;
        static final int CHAR = 8;
        static final int CHAR_SEQUENCE = 10;
        static final int COLOR_STATE_LIST = 15;
        static final int DOUBLE = 7;
        static final int FLOAT = 6;
        static final int ICON = 16;
        static final int INT = 4;
        static final int INTENT = 14;
        static final int LONG = 5;
        static final int SHORT = 3;
        static final int STRING = 9;
        static final int URI = 11;
        @UnsupportedAppUsage
        String methodName;
        int type;
        @UnsupportedAppUsage
        Object value;

        /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
            jadx.core.utils.exceptions.JadxRuntimeException: Incorrect register number in instruction: 0x001b: ARITH  (r15 float) = (r15 float) + (r1 float), expected to be less than 9
            	at jadx.core.dex.nodes.MethodNode.checkInstructions(MethodNode.java:137)
            	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.visit(BlockSplitter.java:43)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
            	at java.lang.Iterable.forEach(Unknown Source)
            	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
            	at jadx.core.ProcessClass.process(ProcessClass.java:37)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            */
        public void apply(android.view.View r6, android.view.ViewGroup r7, android.widget.RemoteViews.OnClickHandler r8) {
            /*
            r5 = this;
            r0 = r5.viewId;
            r0 = r6.findViewById(r0);
            if (r0 != 0) goto L_0x0009;
        L_0x0008:
            return;
        L_0x0009:
            r1 = r5.getParameterType();
            if (r1 == 0) goto L_0x0025;
        L_0x000f:
            r2 = android.widget.RemoteViews.this;	 Catch:{ all -> 0x001e }
            r3 = r5.methodName;	 Catch:{ all -> 0x001e }
            r4 = 0;	 Catch:{ all -> 0x001e }
            r2 = r2.getMethod(r0, r3, r1, r4);	 Catch:{ all -> 0x001e }
            r3 = r5.value;	 Catch:{ all -> 0x001e }
            r15 = r15 + r1;	 Catch:{ all -> 0x001e }
            return;
        L_0x001e:
            r2 = move-exception;
            r3 = new android.widget.RemoteViews$ActionException;
            r3.<init>(r2);
            throw r3;
        L_0x0025:
            r2 = new android.widget.RemoteViews$ActionException;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "bad type: ";
            r3.append(r4);
            r4 = r5.type;
            r3.append(r4);
            r3 = r3.toString();
            r2.<init>(r3);
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.RemoteViews$ReflectionAction.apply(android.view.View, android.view.ViewGroup, android.widget.RemoteViews$OnClickHandler):void");
        }

        ReflectionAction(int viewId, String methodName, int type, Object value) {
            super();
            this.viewId = viewId;
            this.methodName = methodName;
            this.type = type;
            this.value = value;
        }

        ReflectionAction(Parcel in) {
            super();
            this.viewId = in.readInt();
            this.methodName = in.readString();
            this.type = in.readInt();
            switch (this.type) {
                case 1:
                    this.value = Boolean.valueOf(in.readBoolean());
                    return;
                case 2:
                    this.value = Byte.valueOf(in.readByte());
                    return;
                case 3:
                    this.value = Short.valueOf((short) in.readInt());
                    return;
                case 4:
                    this.value = Integer.valueOf(in.readInt());
                    return;
                case 5:
                    this.value = Long.valueOf(in.readLong());
                    return;
                case 6:
                    this.value = Float.valueOf(in.readFloat());
                    return;
                case 7:
                    this.value = Double.valueOf(in.readDouble());
                    return;
                case 8:
                    this.value = Character.valueOf((char) in.readInt());
                    return;
                case 9:
                    this.value = in.readString();
                    return;
                case 10:
                    this.value = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
                    return;
                case 11:
                    this.value = in.readTypedObject(Uri.CREATOR);
                    return;
                case 12:
                    this.value = in.readTypedObject(Bitmap.CREATOR);
                    return;
                case 13:
                    this.value = in.readBundle();
                    return;
                case 14:
                    this.value = in.readTypedObject(Intent.CREATOR);
                    return;
                case 15:
                    this.value = in.readTypedObject(ColorStateList.CREATOR);
                    return;
                case 16:
                    this.value = in.readTypedObject(Icon.CREATOR);
                    return;
                default:
                    return;
            }
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(this.viewId);
            out.writeString(this.methodName);
            out.writeInt(this.type);
            switch (this.type) {
                case 1:
                    out.writeBoolean(((Boolean) this.value).booleanValue());
                    return;
                case 2:
                    out.writeByte(((Byte) this.value).byteValue());
                    return;
                case 3:
                    out.writeInt(((Short) this.value).shortValue());
                    return;
                case 4:
                    out.writeInt(((Integer) this.value).intValue());
                    return;
                case 5:
                    out.writeLong(((Long) this.value).longValue());
                    return;
                case 6:
                    out.writeFloat(((Float) this.value).floatValue());
                    return;
                case 7:
                    out.writeDouble(((Double) this.value).doubleValue());
                    return;
                case 8:
                    out.writeInt(((Character) this.value).charValue());
                    return;
                case 9:
                    out.writeString((String) this.value);
                    return;
                case 10:
                    TextUtils.writeToParcel((CharSequence) this.value, out, flags);
                    return;
                case 11:
                case 12:
                case 14:
                case 15:
                case 16:
                    out.writeTypedObject((Parcelable) this.value, flags);
                    return;
                case 13:
                    out.writeBundle((Bundle) this.value);
                    return;
                default:
                    return;
            }
        }

        private Class<?> getParameterType() {
            switch (this.type) {
                case 1:
                    return Boolean.TYPE;
                case 2:
                    return Byte.TYPE;
                case 3:
                    return Short.TYPE;
                case 4:
                    return Integer.TYPE;
                case 5:
                    return Long.TYPE;
                case 6:
                    return Float.TYPE;
                case 7:
                    return Double.TYPE;
                case 8:
                    return Character.TYPE;
                case 9:
                    return String.class;
                case 10:
                    return CharSequence.class;
                case 11:
                    return Uri.class;
                case 12:
                    return Bitmap.class;
                case 13:
                    return Bundle.class;
                case 14:
                    return Intent.class;
                case 15:
                    return ColorStateList.class;
                case 16:
                    return Icon.class;
                default:
                    return null;
            }
        }

        public Action initActionAsync(ViewTree root, ViewGroup rootParent, OnClickHandler handler) {
            View view = root.findViewById(this.viewId);
            if (view == null) {
                return RemoteViews.ACTION_NOOP;
            }
            Class<?> param = getParameterType();
            if (param != null) {
                try {
                    if (RemoteViews.this.getMethod(view, this.methodName, param, true) == null) {
                        return this;
                    }
                    Runnable endAction = this.value;
                    int length = param.length;
                    if (endAction == null) {
                        return RemoteViews.ACTION_NOOP;
                    }
                    if (endAction instanceof ViewReplaceRunnable) {
                        root.createTree();
                        root.findViewTreeById(this.viewId).replaceView(((ViewReplaceRunnable) endAction).view);
                    }
                    return new RunnableAction(endAction);
                } catch (Throwable ex) {
                    ActionException actionException = new ActionException(ex);
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("bad type: ");
                stringBuilder.append(this.type);
                throw new ActionException(stringBuilder.toString());
            }
        }

        public int mergeBehavior() {
            if (this.methodName.equals("smoothScrollBy")) {
                return 1;
            }
            return 0;
        }

        public int getActionTag() {
            return 2;
        }

        public String getUniqueKey() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(super.getUniqueKey());
            stringBuilder.append(this.methodName);
            stringBuilder.append(this.type);
            return stringBuilder.toString();
        }

        public boolean prefersAsyncApply() {
            int i = this.type;
            return i == 11 || i == 16;
        }

        public void visitUris(Consumer<Uri> visitor) {
            int i = this.type;
            if (i == 11) {
                visitor.accept(this.value);
            } else if (i == 16) {
                RemoteViews.visitIconUri(this.value, visitor);
            }
        }
    }

    public static class RemoteResponse {
        private ArrayList<String> mElementNames;
        private Intent mFillIntent;
        private PendingIntent mPendingIntent;
        private IntArray mViewIds;

        public static RemoteResponse fromPendingIntent(PendingIntent pendingIntent) {
            RemoteResponse response = new RemoteResponse();
            response.mPendingIntent = pendingIntent;
            return response;
        }

        public static RemoteResponse fromFillInIntent(Intent fillIntent) {
            RemoteResponse response = new RemoteResponse();
            response.mFillIntent = fillIntent;
            return response;
        }

        public RemoteResponse addSharedElement(int viewId, String sharedElementName) {
            if (this.mViewIds == null) {
                this.mViewIds = new IntArray();
                this.mElementNames = new ArrayList();
            }
            this.mViewIds.add(viewId);
            this.mElementNames.add(sharedElementName);
            return this;
        }

        private void writeToParcel(Parcel dest, int flags) {
            PendingIntent.writePendingIntentOrNullToParcel(this.mPendingIntent, dest);
            if (this.mPendingIntent == null) {
                dest.writeTypedObject(this.mFillIntent, flags);
            }
            IntArray intArray = this.mViewIds;
            dest.writeIntArray(intArray == null ? null : intArray.toArray());
            dest.writeStringList(this.mElementNames);
        }

        private void readFromParcel(Parcel parcel) {
            this.mPendingIntent = PendingIntent.readPendingIntentOrNullFromParcel(parcel);
            if (this.mPendingIntent == null) {
                this.mFillIntent = (Intent) parcel.readTypedObject(Intent.CREATOR);
            }
            int[] viewIds = parcel.createIntArray();
            this.mViewIds = viewIds == null ? null : IntArray.wrap(viewIds);
            this.mElementNames = parcel.createStringArrayList();
        }

        private void handleViewClick(View v, OnClickHandler handler) {
            PendingIntent pi;
            if (this.mPendingIntent != null) {
                pi = this.mPendingIntent;
            } else {
                Intent intent = this.mFillIntent;
                String str = RemoteViews.LOG_TAG;
                if (intent != null) {
                    View parent = (View) v.getParent();
                    while (parent != null && !(parent instanceof AdapterView) && (!(parent instanceof AppWidgetHostView) || (parent instanceof RemoteViewsFrameLayout))) {
                        parent = (View) parent.getParent();
                    }
                    if (!(parent instanceof AdapterView)) {
                        Log.e(str, "Collection item doesn't have AdapterView parent");
                        return;
                    } else if (parent.getTag() instanceof PendingIntent) {
                        pi = (PendingIntent) parent.getTag();
                    } else {
                        Log.e(str, "Attempting setOnClickFillInIntent without calling setPendingIntentTemplate on parent.");
                        return;
                    }
                }
                Log.e(str, "Response has neither pendingIntent nor fillInIntent");
                return;
            }
            handler.onClickHandler(v, pi, this);
        }

        public Pair<Intent, ActivityOptions> getLaunchOptions(View view) {
            Intent intent = this.mPendingIntent != null ? new Intent() : new Intent(this.mFillIntent);
            intent.setSourceBounds(RemoteViews.getSourceBounds(view));
            ActivityOptions opts = null;
            Context context = view.getContext();
            if (context.getResources().getBoolean(R.bool.config_overrideRemoteViewsActivityTransition)) {
                TypedArray windowStyle = context.getTheme().obtainStyledAttributes(R.styleable.Window);
                TypedArray windowAnimationStyle = context.obtainStyledAttributes(windowStyle.getResourceId(8, 0), R.styleable.WindowAnimation);
                int enterAnimationId = windowAnimationStyle.getResourceId(26, 0);
                windowStyle.recycle();
                windowAnimationStyle.recycle();
                if (enterAnimationId != 0) {
                    opts = ActivityOptions.makeCustomAnimation(context, enterAnimationId, 0);
                    opts.setPendingIntentLaunchFlags(268435456);
                }
            }
            if (!(opts != null || this.mViewIds == null || this.mElementNames == null)) {
                View parent = (View) view.getParent();
                while (parent != null && !(parent instanceof AppWidgetHostView)) {
                    parent = (View) parent.getParent();
                }
                if (parent instanceof AppWidgetHostView) {
                    AppWidgetHostView appWidgetHostView = (AppWidgetHostView) parent;
                    int[] toArray = this.mViewIds.toArray();
                    ArrayList arrayList = this.mElementNames;
                    opts = appWidgetHostView.createSharedElementActivityOptions(toArray, (String[]) arrayList.toArray(new String[arrayList.size()]), intent);
                }
            }
            if (opts == null) {
                opts = ActivityOptions.makeBasic();
                opts.setPendingIntentLaunchFlags(268435456);
            }
            return Pair.create(intent, opts);
        }
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RemoteView {
    }

    private static class RemoteViewsContextWrapper extends ContextWrapper {
        private final Context mContextForResources;

        RemoteViewsContextWrapper(Context context, Context contextForResources) {
            super(context);
            this.mContextForResources = contextForResources;
        }

        public Resources getResources() {
            return this.mContextForResources.getResources();
        }

        public Theme getTheme() {
            return this.mContextForResources.getTheme();
        }

        public String getPackageName() {
            return this.mContextForResources.getPackageName();
        }
    }

    private static final class RunnableAction extends RuntimeAction {
        private final Runnable mRunnable;

        RunnableAction(Runnable r) {
            super();
            this.mRunnable = r;
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            this.mRunnable.run();
        }
    }

    private class SetDrawableTint extends Action {
        int colorFilter;
        Mode filterMode;
        boolean targetBackground;

        SetDrawableTint(int id, boolean targetBackground, int colorFilter, Mode mode) {
            super();
            this.viewId = id;
            this.targetBackground = targetBackground;
            this.colorFilter = colorFilter;
            this.filterMode = mode;
        }

        SetDrawableTint(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.targetBackground = parcel.readInt() != 0;
            this.colorFilter = parcel.readInt();
            this.filterMode = PorterDuff.intToMode(parcel.readInt());
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.viewId);
            dest.writeInt(this.targetBackground);
            dest.writeInt(this.colorFilter);
            dest.writeInt(PorterDuff.modeToInt(this.filterMode));
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                Drawable targetDrawable = null;
                if (this.targetBackground) {
                    targetDrawable = target.getBackground();
                } else if (target instanceof ImageView) {
                    targetDrawable = ((ImageView) target).getDrawable();
                }
                if (targetDrawable != null) {
                    targetDrawable.mutate().setColorFilter(this.colorFilter, this.filterMode);
                }
            }
        }

        public int getActionTag() {
            return 3;
        }
    }

    private class SetEmptyView extends Action {
        int emptyViewId;

        SetEmptyView(int viewId, int emptyViewId) {
            super();
            this.viewId = viewId;
            this.emptyViewId = emptyViewId;
        }

        SetEmptyView(Parcel in) {
            super();
            this.viewId = in.readInt();
            this.emptyViewId = in.readInt();
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(this.viewId);
            out.writeInt(this.emptyViewId);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View view = root.findViewById(this.viewId);
            if (view instanceof AdapterView) {
                AdapterView<?> adapterView = (AdapterView) view;
                View emptyView = root.findViewById(this.emptyViewId);
                if (emptyView != null) {
                    adapterView.setEmptyView(emptyView);
                }
            }
        }

        public int getActionTag() {
            return 6;
        }
    }

    private class SetIntTagAction extends Action {
        private final int mKey;
        private final int mTag;
        private final int mViewId;

        SetIntTagAction(int viewId, int key, int tag) {
            super();
            this.mViewId = viewId;
            this.mKey = key;
            this.mTag = tag;
        }

        SetIntTagAction(Parcel parcel) {
            super();
            this.mViewId = parcel.readInt();
            this.mKey = parcel.readInt();
            this.mTag = parcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mViewId);
            dest.writeInt(this.mKey);
            dest.writeInt(this.mTag);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View target = root.findViewById(this.mViewId);
            if (target != null) {
                target.setTagInternal(this.mKey, Integer.valueOf(this.mTag));
            }
        }

        public int getActionTag() {
            return 22;
        }
    }

    private class SetOnClickResponse extends Action {
        final RemoteResponse mResponse;

        SetOnClickResponse(int id, RemoteResponse response) {
            super();
            this.viewId = id;
            this.mResponse = response;
        }

        SetOnClickResponse(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.mResponse = new RemoteResponse();
            this.mResponse.readFromParcel(parcel);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.viewId);
            this.mResponse.writeToParcel(dest, flags);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                PendingIntent access$500 = this.mResponse.mPendingIntent;
                String str = RemoteViews.LOG_TAG;
                if (access$500 != null) {
                    if (RemoteViews.this.hasFlags(2)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Cannot SetOnClickResponse for collection item (id: ");
                        stringBuilder.append(this.viewId);
                        stringBuilder.append(")");
                        Log.w(str, stringBuilder.toString());
                        ApplicationInfo appInfo = root.getContext().getApplicationInfo();
                        if (appInfo != null && appInfo.targetSdkVersion >= 16) {
                            return;
                        }
                    }
                    target.setTagInternal(R.id.pending_intent_tag, this.mResponse.mPendingIntent);
                } else if (this.mResponse.mFillIntent == null) {
                    target.setOnClickListener(null);
                    return;
                } else if (!RemoteViews.this.hasFlags(2)) {
                    Log.e(str, "The method setOnClickFillInIntent is available only from RemoteViewsFactory (ie. on collection items).");
                    return;
                } else if (target == root) {
                    target.setTagInternal(R.id.fillInIntent, this.mResponse);
                    return;
                }
                target.setOnClickListener(new -$$Lambda$RemoteViews$SetOnClickResponse$9rKnU2QqCzJhBC39ZrKYXob0-MA(this, handler));
            }
        }

        public /* synthetic */ void lambda$apply$0$RemoteViews$SetOnClickResponse(OnClickHandler handler, View v) {
            this.mResponse.handleViewClick(v, handler);
        }

        public int getActionTag() {
            return 1;
        }
    }

    private class SetPendingIntentTemplate extends Action {
        @UnsupportedAppUsage
        PendingIntent pendingIntentTemplate;

        public SetPendingIntentTemplate(int id, PendingIntent pendingIntentTemplate) {
            super();
            this.viewId = id;
            this.pendingIntentTemplate = pendingIntentTemplate;
        }

        public SetPendingIntentTemplate(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.pendingIntentTemplate = PendingIntent.readPendingIntentOrNullFromParcel(parcel);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.viewId);
            PendingIntent.writePendingIntentOrNullToParcel(this.pendingIntentTemplate, dest);
        }

        public void apply(View root, ViewGroup rootParent, final OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                if (target instanceof AdapterView) {
                    AdapterView<?> av = (AdapterView) target;
                    av.setOnItemClickListener(new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (view instanceof ViewGroup) {
                                ViewGroup vg = (ViewGroup) view;
                                if (parent instanceof AdapterViewAnimator) {
                                    vg = (ViewGroup) vg.getChildAt(0);
                                }
                                if (vg != null) {
                                    RemoteResponse response = null;
                                    int childCount = vg.getChildCount();
                                    for (int i = 0; i < childCount; i++) {
                                        RemoteResponse tag = vg.getChildAt(i).getTag(R.id.fillInIntent);
                                        if (tag instanceof RemoteResponse) {
                                            response = tag;
                                            break;
                                        }
                                    }
                                    if (response != null) {
                                        response.handleViewClick(view, handler);
                                    }
                                }
                            }
                        }
                    });
                    av.setTag(this.pendingIntentTemplate);
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Cannot setPendingIntentTemplate on a view which is notan AdapterView (id: ");
                stringBuilder.append(this.viewId);
                stringBuilder.append(")");
                Log.e(RemoteViews.LOG_TAG, stringBuilder.toString());
            }
        }

        public int getActionTag() {
            return 8;
        }
    }

    private class SetRemoteInputsAction extends Action {
        final Parcelable[] remoteInputs;

        public SetRemoteInputsAction(int viewId, RemoteInput[] remoteInputs) {
            super();
            this.viewId = viewId;
            this.remoteInputs = remoteInputs;
        }

        public SetRemoteInputsAction(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.remoteInputs = (Parcelable[]) parcel.createTypedArray(RemoteInput.CREATOR);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.viewId);
            dest.writeTypedArray(this.remoteInputs, flags);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                target.setTagInternal(R.id.remote_input_tag, this.remoteInputs);
            }
        }

        public int getActionTag() {
            return 18;
        }
    }

    private class SetRemoteViewsAdapterIntent extends Action {
        Intent intent;
        boolean isAsync = false;

        public SetRemoteViewsAdapterIntent(int id, Intent intent) {
            super();
            this.viewId = id;
            this.intent = intent;
        }

        public SetRemoteViewsAdapterIntent(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.intent = (Intent) parcel.readTypedObject(Intent.CREATOR);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.viewId);
            dest.writeTypedObject(this.intent, flags);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                boolean z = rootParent instanceof AppWidgetHostView;
                String str = ")";
                String str2 = RemoteViews.LOG_TAG;
                StringBuilder stringBuilder;
                if (!z) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("SetRemoteViewsAdapterIntent action can only be used for AppWidgets (root id: ");
                    stringBuilder.append(this.viewId);
                    stringBuilder.append(str);
                    Log.e(str2, stringBuilder.toString());
                } else if ((target instanceof AbsListView) || (target instanceof AdapterViewAnimator)) {
                    AppWidgetHostView host = (AppWidgetHostView) rootParent;
                    this.intent.putExtra(RemoteViews.EXTRA_REMOTEADAPTER_APPWIDGET_ID, host.getAppWidgetId()).putExtra(RemoteViews.EXTRA_REMOTEADAPTER_ON_LIGHT_BACKGROUND, RemoteViews.this.hasFlags(4));
                    if (target instanceof AbsListView) {
                        AbsListView v = (AbsListView) target;
                        v.setRemoteViewsAdapter(this.intent, this.isAsync);
                        v.setRemoteViewsOnClickHandler(handler);
                    } else if (target instanceof AdapterViewAnimator) {
                        AdapterViewAnimator v2 = (AdapterViewAnimator) target;
                        v2.setRemoteViewsAdapter(this.intent, this.isAsync);
                        v2.setRemoteViewsOnClickHandler(handler);
                    }
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Cannot setRemoteViewsAdapter on a view which is not an AbsListView or AdapterViewAnimator (id: ");
                    stringBuilder.append(this.viewId);
                    stringBuilder.append(str);
                    Log.e(str2, stringBuilder.toString());
                }
            }
        }

        public Action initActionAsync(ViewTree root, ViewGroup rootParent, OnClickHandler handler) {
            SetRemoteViewsAdapterIntent copy = new SetRemoteViewsAdapterIntent(this.viewId, this.intent);
            copy.isAsync = true;
            return copy;
        }

        public int getActionTag() {
            return 10;
        }
    }

    private class SetRemoteViewsAdapterList extends Action {
        ArrayList<RemoteViews> list;
        int viewTypeCount;

        public SetRemoteViewsAdapterList(int id, ArrayList<RemoteViews> list, int viewTypeCount) {
            super();
            this.viewId = id;
            this.list = list;
            this.viewTypeCount = viewTypeCount;
        }

        public SetRemoteViewsAdapterList(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.viewTypeCount = parcel.readInt();
            this.list = parcel.createTypedArrayList(RemoteViews.CREATOR);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.viewId);
            dest.writeInt(this.viewTypeCount);
            dest.writeTypedList(this.list, flags);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                boolean z = rootParent instanceof AppWidgetHostView;
                String str = ")";
                String str2 = RemoteViews.LOG_TAG;
                StringBuilder stringBuilder;
                if (!z) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("SetRemoteViewsAdapterIntent action can only be used for AppWidgets (root id: ");
                    stringBuilder.append(this.viewId);
                    stringBuilder.append(str);
                    Log.e(str2, stringBuilder.toString());
                } else if ((target instanceof AbsListView) || (target instanceof AdapterViewAnimator)) {
                    Adapter a;
                    if (target instanceof AbsListView) {
                        AbsListView v = (AbsListView) target;
                        a = v.getAdapter();
                        if (!(a instanceof RemoteViewsListAdapter) || this.viewTypeCount > a.getViewTypeCount()) {
                            v.setAdapter(new RemoteViewsListAdapter(v.getContext(), this.list, this.viewTypeCount));
                        } else {
                            ((RemoteViewsListAdapter) a).setViewsList(this.list);
                        }
                    } else if (target instanceof AdapterViewAnimator) {
                        AdapterViewAnimator v2 = (AdapterViewAnimator) target;
                        a = v2.getAdapter();
                        if (!(a instanceof RemoteViewsListAdapter) || this.viewTypeCount > a.getViewTypeCount()) {
                            v2.setAdapter(new RemoteViewsListAdapter(v2.getContext(), this.list, this.viewTypeCount));
                        } else {
                            ((RemoteViewsListAdapter) a).setViewsList(this.list);
                        }
                    }
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Cannot setRemoteViewsAdapter on a view which is not an AbsListView or AdapterViewAnimator (id: ");
                    stringBuilder.append(this.viewId);
                    stringBuilder.append(str);
                    Log.e(str2, stringBuilder.toString());
                }
            }
        }

        public int getActionTag() {
            return 15;
        }
    }

    private class SetRippleDrawableColor extends Action {
        ColorStateList mColorStateList;

        SetRippleDrawableColor(int id, ColorStateList colorStateList) {
            super();
            this.viewId = id;
            this.mColorStateList = colorStateList;
        }

        SetRippleDrawableColor(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.mColorStateList = (ColorStateList) parcel.readParcelable(null);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.viewId);
            dest.writeParcelable(this.mColorStateList, 0);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                Drawable targetDrawable = target.getBackground();
                if (targetDrawable instanceof RippleDrawable) {
                    ((RippleDrawable) targetDrawable.mutate()).setColor(this.mColorStateList);
                }
            }
        }

        public int getActionTag() {
            return 21;
        }
    }

    private class TextViewDrawableAction extends Action {
        int d1;
        int d2;
        int d3;
        int d4;
        boolean drawablesLoaded = false;
        Icon i1;
        Icon i2;
        Icon i3;
        Icon i4;
        Drawable id1;
        Drawable id2;
        Drawable id3;
        Drawable id4;
        boolean isRelative = false;
        boolean useIcons = false;

        public TextViewDrawableAction(int viewId, boolean isRelative, int d1, int d2, int d3, int d4) {
            super();
            this.viewId = viewId;
            this.isRelative = isRelative;
            this.useIcons = false;
            this.d1 = d1;
            this.d2 = d2;
            this.d3 = d3;
            this.d4 = d4;
        }

        public TextViewDrawableAction(int viewId, boolean isRelative, Icon i1, Icon i2, Icon i3, Icon i4) {
            super();
            this.viewId = viewId;
            this.isRelative = isRelative;
            this.useIcons = true;
            this.i1 = i1;
            this.i2 = i2;
            this.i3 = i3;
            this.i4 = i4;
        }

        public TextViewDrawableAction(Parcel parcel) {
            super();
            boolean z = false;
            this.viewId = parcel.readInt();
            this.isRelative = parcel.readInt() != 0;
            if (parcel.readInt() != 0) {
                z = true;
            }
            this.useIcons = z;
            if (this.useIcons) {
                this.i1 = (Icon) parcel.readTypedObject(Icon.CREATOR);
                this.i2 = (Icon) parcel.readTypedObject(Icon.CREATOR);
                this.i3 = (Icon) parcel.readTypedObject(Icon.CREATOR);
                this.i4 = (Icon) parcel.readTypedObject(Icon.CREATOR);
                return;
            }
            this.d1 = parcel.readInt();
            this.d2 = parcel.readInt();
            this.d3 = parcel.readInt();
            this.d4 = parcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.viewId);
            dest.writeInt(this.isRelative);
            dest.writeInt(this.useIcons);
            if (this.useIcons) {
                dest.writeTypedObject(this.i1, 0);
                dest.writeTypedObject(this.i2, 0);
                dest.writeTypedObject(this.i3, 0);
                dest.writeTypedObject(this.i4, 0);
                return;
            }
            dest.writeInt(this.d1);
            dest.writeInt(this.d2);
            dest.writeInt(this.d3);
            dest.writeInt(this.d4);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            TextView target = (TextView) root.findViewById(this.viewId);
            if (target != null) {
                if (this.drawablesLoaded) {
                    if (this.isRelative) {
                        target.setCompoundDrawablesRelativeWithIntrinsicBounds(this.id1, this.id2, this.id3, this.id4);
                    } else {
                        target.setCompoundDrawablesWithIntrinsicBounds(this.id1, this.id2, this.id3, this.id4);
                    }
                } else if (this.useIcons) {
                    Context ctx = target.getContext();
                    Drawable id1 = this.i1;
                    Drawable id4 = null;
                    id1 = id1 == null ? null : id1.loadDrawable(ctx);
                    Drawable id2 = this.i2;
                    id2 = id2 == null ? null : id2.loadDrawable(ctx);
                    Drawable id3 = this.i3;
                    id3 = id3 == null ? null : id3.loadDrawable(ctx);
                    Icon icon = this.i4;
                    if (icon != null) {
                        id4 = icon.loadDrawable(ctx);
                    }
                    if (this.isRelative) {
                        target.setCompoundDrawablesRelativeWithIntrinsicBounds(id1, id2, id3, id4);
                    } else {
                        target.setCompoundDrawablesWithIntrinsicBounds(id1, id2, id3, id4);
                    }
                } else if (this.isRelative) {
                    target.setCompoundDrawablesRelativeWithIntrinsicBounds(this.d1, this.d2, this.d3, this.d4);
                } else {
                    target.setCompoundDrawablesWithIntrinsicBounds(this.d1, this.d2, this.d3, this.d4);
                }
            }
        }

        public Action initActionAsync(ViewTree root, ViewGroup rootParent, OnClickHandler handler) {
            TextView target = (TextView) root.findViewById(this.viewId);
            if (target == null) {
                return RemoteViews.ACTION_NOOP;
            }
            if (this.useIcons) {
                TextViewDrawableAction textViewDrawableAction = new TextViewDrawableAction(this.viewId, this.isRelative, this.i1, this.i2, this.i3, this.i4);
            } else {
                TextViewDrawableAction textViewDrawableAction2 = new TextViewDrawableAction(this.viewId, this.isRelative, this.d1, this.d2, this.d3, this.d4);
            }
            copy.drawablesLoaded = true;
            Context ctx = target.getContext();
            Drawable drawable = null;
            if (this.useIcons) {
                Icon icon = this.i1;
                copy.id1 = icon == null ? null : icon.loadDrawable(ctx);
                icon = this.i2;
                copy.id2 = icon == null ? null : icon.loadDrawable(ctx);
                icon = this.i3;
                copy.id3 = icon == null ? null : icon.loadDrawable(ctx);
                icon = this.i4;
                if (icon != null) {
                    drawable = icon.loadDrawable(ctx);
                }
                copy.id4 = drawable;
            } else {
                int i = this.d1;
                copy.id1 = i == 0 ? null : ctx.getDrawable(i);
                i = this.d2;
                copy.id2 = i == 0 ? null : ctx.getDrawable(i);
                i = this.d3;
                copy.id3 = i == 0 ? null : ctx.getDrawable(i);
                i = this.d4;
                if (i != 0) {
                    drawable = ctx.getDrawable(i);
                }
                copy.id4 = drawable;
            }
            return copy;
        }

        public boolean prefersAsyncApply() {
            return this.useIcons;
        }

        public int getActionTag() {
            return 11;
        }

        public void visitUris(Consumer<Uri> visitor) {
            if (this.useIcons) {
                RemoteViews.visitIconUri(this.i1, visitor);
                RemoteViews.visitIconUri(this.i2, visitor);
                RemoteViews.visitIconUri(this.i3, visitor);
                RemoteViews.visitIconUri(this.i4, visitor);
            }
        }
    }

    private class TextViewSizeAction extends Action {
        float size;
        int units;

        public TextViewSizeAction(int viewId, int units, float size) {
            super();
            this.viewId = viewId;
            this.units = units;
            this.size = size;
        }

        public TextViewSizeAction(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.units = parcel.readInt();
            this.size = parcel.readFloat();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.viewId);
            dest.writeInt(this.units);
            dest.writeFloat(this.size);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            TextView target = (TextView) root.findViewById(this.viewId);
            if (target != null) {
                target.setTextSize(this.units, this.size);
            }
        }

        public int getActionTag() {
            return 13;
        }
    }

    private final class ViewContentNavigation extends Action {
        final boolean mNext;

        /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
            jadx.core.utils.exceptions.JadxRuntimeException: Incorrect register number in instruction: 0x001d: IPUT  (r15 int[]), (r1 android.R$styleable) android.R.styleable.HorizontalScrollView int[], expected to be less than 9
            	at jadx.core.dex.nodes.MethodNode.checkInstructions(MethodNode.java:137)
            	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.visit(BlockSplitter.java:43)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
            	at java.lang.Iterable.forEach(Unknown Source)
            	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
            	at jadx.core.ProcessClass.process(ProcessClass.java:37)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            */
        public void apply(android.view.View r6, android.view.ViewGroup r7, android.widget.RemoteViews.OnClickHandler r8) {
            /*
            r5 = this;
            r0 = r5.viewId;
            r0 = r6.findViewById(r0);
            if (r0 != 0) goto L_0x0009;
        L_0x0008:
            return;
        L_0x0009:
            r1 = android.widget.RemoteViews.this;	 Catch:{ all -> 0x0020 }
            r2 = r5.mNext;	 Catch:{ all -> 0x0020 }
            if (r2 == 0) goto L_0x0013;	 Catch:{ all -> 0x0020 }
        L_0x000f:
            r2 = "showNext";	 Catch:{ all -> 0x0020 }
            goto L_0x0016;	 Catch:{ all -> 0x0020 }
        L_0x0013:
            r2 = "showPrevious";	 Catch:{ all -> 0x0020 }
        L_0x0016:
            r3 = 0;	 Catch:{ all -> 0x0020 }
            r4 = 0;	 Catch:{ all -> 0x0020 }
            r1 = r1.getMethod(r0, r2, r3, r4);	 Catch:{ all -> 0x0020 }
            r1.HorizontalScrollView = r15;	 Catch:{ all -> 0x0020 }
            return;
        L_0x0020:
            r1 = move-exception;
            r2 = new android.widget.RemoteViews$ActionException;
            r2.<init>(r1);
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.RemoteViews$ViewContentNavigation.apply(android.view.View, android.view.ViewGroup, android.widget.RemoteViews$OnClickHandler):void");
        }

        ViewContentNavigation(int viewId, boolean next) {
            super();
            this.viewId = viewId;
            this.mNext = next;
        }

        ViewContentNavigation(Parcel in) {
            super();
            this.viewId = in.readInt();
            this.mNext = in.readBoolean();
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(this.viewId);
            out.writeBoolean(this.mNext);
        }

        public int mergeBehavior() {
            return 2;
        }

        public int getActionTag() {
            return 5;
        }
    }

    private class ViewGroupActionAdd extends Action {
        private int mIndex;
        @UnsupportedAppUsage
        private RemoteViews mNestedViews;

        ViewGroupActionAdd(RemoteViews remoteViews, int viewId, RemoteViews nestedViews) {
            this(viewId, nestedViews, -1);
        }

        ViewGroupActionAdd(int viewId, RemoteViews nestedViews, int index) {
            super();
            this.viewId = viewId;
            this.mNestedViews = nestedViews;
            this.mIndex = index;
            if (nestedViews != null) {
                RemoteViews.this.configureRemoteViewsAsChild(nestedViews);
            }
        }

        ViewGroupActionAdd(Parcel parcel, BitmapCache bitmapCache, ApplicationInfo info, int depth, Map<Class, Object> classCookies) {
            super();
            this.viewId = parcel.readInt();
            this.mIndex = parcel.readInt();
            this.mNestedViews = new RemoteViews(parcel, bitmapCache, info, depth, classCookies, null);
            this.mNestedViews.addFlags(RemoteViews.this.mApplyFlags);
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.viewId);
            dest.writeInt(this.mIndex);
            this.mNestedViews.writeToParcel(dest, flags);
        }

        public boolean hasSameAppInfo(ApplicationInfo parentInfo) {
            return this.mNestedViews.hasSameAppInfo(parentInfo);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            Context context = root.getContext();
            ViewGroup target = (ViewGroup) root.findViewById(this.viewId);
            if (target != null) {
                target.addView(this.mNestedViews.apply(context, target, handler), this.mIndex);
            }
        }

        public Action initActionAsync(ViewTree root, ViewGroup rootParent, OnClickHandler handler) {
            root.createTree();
            ViewTree target = root.findViewTreeById(this.viewId);
            if (target == null || !(target.mRoot instanceof ViewGroup)) {
                return RemoteViews.ACTION_NOOP;
            }
            final ViewGroup targetVg = (ViewGroup) target.mRoot;
            final AsyncApplyTask task = this.mNestedViews.getAsyncApplyTask(root.mRoot.getContext(), targetVg, null, handler);
            final ViewTree tree = task.doInBackground(new Void[0]);
            if (tree != null) {
                target.addChild(tree, this.mIndex);
                return new RuntimeAction() {
                    public void apply(View root, ViewGroup rootParent, OnClickHandler handler) throws ActionException {
                        task.onPostExecute(tree);
                        targetVg.addView(task.mResult, ViewGroupActionAdd.this.mIndex);
                    }
                };
            }
            throw new ActionException(task.mError);
        }

        public void setBitmapCache(BitmapCache bitmapCache) {
            this.mNestedViews.setBitmapCache(bitmapCache);
        }

        public int mergeBehavior() {
            return 1;
        }

        public boolean prefersAsyncApply() {
            return this.mNestedViews.prefersAsyncApply();
        }

        public int getActionTag() {
            return 4;
        }
    }

    private class ViewGroupActionRemove extends Action {
        private static final int REMOVE_ALL_VIEWS_ID = -2;
        private int mViewIdToKeep;

        ViewGroupActionRemove(RemoteViews remoteViews, int viewId) {
            this(viewId, -2);
        }

        ViewGroupActionRemove(int viewId, int viewIdToKeep) {
            super();
            this.viewId = viewId;
            this.mViewIdToKeep = viewIdToKeep;
        }

        ViewGroupActionRemove(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.mViewIdToKeep = parcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.viewId);
            dest.writeInt(this.mViewIdToKeep);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            ViewGroup target = (ViewGroup) root.findViewById(this.viewId);
            if (target != null) {
                if (this.mViewIdToKeep == -2) {
                    target.removeAllViews();
                } else {
                    removeAllViewsExceptIdToKeep(target);
                }
            }
        }

        public Action initActionAsync(ViewTree root, ViewGroup rootParent, OnClickHandler handler) {
            root.createTree();
            ViewTree target = root.findViewTreeById(this.viewId);
            if (target == null || !(target.mRoot instanceof ViewGroup)) {
                return RemoteViews.ACTION_NOOP;
            }
            final ViewGroup targetVg = (ViewGroup) target.mRoot;
            target.mChildren = null;
            return new RuntimeAction() {
                public void apply(View root, ViewGroup rootParent, OnClickHandler handler) throws ActionException {
                    if (ViewGroupActionRemove.this.mViewIdToKeep == -2) {
                        targetVg.removeAllViews();
                    } else {
                        ViewGroupActionRemove.this.removeAllViewsExceptIdToKeep(targetVg);
                    }
                }
            };
        }

        private void removeAllViewsExceptIdToKeep(ViewGroup viewGroup) {
            for (int index = viewGroup.getChildCount() - 1; index >= 0; index--) {
                if (viewGroup.getChildAt(index).getId() != this.mViewIdToKeep) {
                    viewGroup.removeViewAt(index);
                }
            }
        }

        public int getActionTag() {
            return 7;
        }

        public int mergeBehavior() {
            return 1;
        }
    }

    private class ViewPaddingAction extends Action {
        int bottom;
        int left;
        int right;
        int top;

        public ViewPaddingAction(int viewId, int left, int top, int right, int bottom) {
            super();
            this.viewId = viewId;
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public ViewPaddingAction(Parcel parcel) {
            super();
            this.viewId = parcel.readInt();
            this.left = parcel.readInt();
            this.top = parcel.readInt();
            this.right = parcel.readInt();
            this.bottom = parcel.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.viewId);
            dest.writeInt(this.left);
            dest.writeInt(this.top);
            dest.writeInt(this.right);
            dest.writeInt(this.bottom);
        }

        public void apply(View root, ViewGroup rootParent, OnClickHandler handler) {
            View target = root.findViewById(this.viewId);
            if (target != null) {
                target.setPadding(this.left, this.top, this.right, this.bottom);
            }
        }

        public int getActionTag() {
            return 14;
        }
    }

    private static class ViewTree {
        private static final int INSERT_AT_END_INDEX = -1;
        private ArrayList<ViewTree> mChildren;
        private View mRoot;

        /* synthetic */ ViewTree(View x0, AnonymousClass1 x1) {
            this(x0);
        }

        private ViewTree(View root) {
            this.mRoot = root;
        }

        public void createTree() {
            if (this.mChildren == null) {
                this.mChildren = new ArrayList();
                ViewGroup vg = this.mRoot;
                if (vg instanceof ViewGroup) {
                    vg = vg;
                    int count = vg.getChildCount();
                    for (int i = 0; i < count; i++) {
                        addViewChild(vg.getChildAt(i));
                    }
                }
            }
        }

        public ViewTree findViewTreeById(int id) {
            if (this.mRoot.getId() == id) {
                return this;
            }
            ArrayList arrayList = this.mChildren;
            if (arrayList == null) {
                return null;
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ViewTree result = ((ViewTree) it.next()).findViewTreeById(id);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }

        public void replaceView(View v) {
            this.mRoot = v;
            this.mChildren = null;
            createTree();
        }

        public <T extends View> T findViewById(int id) {
            if (this.mChildren == null) {
                return this.mRoot.findViewById(id);
            }
            ViewTree tree = findViewTreeById(id);
            return tree == null ? null : tree.mRoot;
        }

        public void addChild(ViewTree child) {
            addChild(child, -1);
        }

        public void addChild(ViewTree child, int index) {
            if (this.mChildren == null) {
                this.mChildren = new ArrayList();
            }
            child.createTree();
            if (index == -1) {
                this.mChildren.add(child);
            } else {
                this.mChildren.add(index, child);
            }
        }

        private void addViewChild(View v) {
            if (!v.isRootNamespace()) {
                ViewTree target;
                if (v.getId() != 0) {
                    target = new ViewTree(v);
                    this.mChildren.add(target);
                } else {
                    target = this;
                }
                if ((v instanceof ViewGroup) && target.mChildren == null) {
                    target.mChildren = new ArrayList();
                    ViewGroup vg = (ViewGroup) v;
                    int count = vg.getChildCount();
                    for (int i = 0; i < count; i++) {
                        target.addViewChild(vg.getChildAt(i));
                    }
                }
            }
        }
    }

    /* synthetic */ RemoteViews(Parcel x0, BitmapCache x1, ApplicationInfo x2, int x3, Map x4, AnonymousClass1 x5) {
        this(x0, x1, x2, x3, x4);
    }

    public void setRemoteInputs(int viewId, RemoteInput[] remoteInputs) {
        this.mActions.add(new SetRemoteInputsAction(viewId, remoteInputs));
    }

    public void reduceImageSizes(int maxWidth, int maxHeight) {
        ArrayList<Bitmap> cache = this.mBitmapCache.mBitmaps;
        for (int i = 0; i < cache.size(); i++) {
            cache.set(i, Icon.scaleDownIfNecessary((Bitmap) cache.get(i), maxWidth, maxHeight));
        }
    }

    public void overrideTextColors(int textColor) {
        addAction(new OverrideTextColorsAction(textColor));
    }

    public void setIntTag(int viewId, int key, int tag) {
        addAction(new SetIntTagAction(viewId, key, tag));
    }

    public void addFlags(int flags) {
        this.mApplyFlags |= flags;
    }

    public boolean hasFlags(int flag) {
        return (this.mApplyFlags & flag) == flag;
    }

    @UnsupportedAppUsage
    public void mergeRemoteViews(RemoteViews newRv) {
        if (newRv != null) {
            RemoteViews copy = new RemoteViews(newRv);
            HashMap<String, Action> map = new HashMap();
            if (this.mActions == null) {
                this.mActions = new ArrayList();
            }
            int count = this.mActions.size();
            for (int i = 0; i < count; i++) {
                Action a = (Action) this.mActions.get(i);
                map.put(a.getUniqueKey(), a);
            }
            ArrayList<Action> newActions = copy.mActions;
            if (newActions != null) {
                count = newActions.size();
                for (int i2 = 0; i2 < count; i2++) {
                    Action a2 = (Action) newActions.get(i2);
                    String key = ((Action) newActions.get(i2)).getUniqueKey();
                    int mergeBehavior = ((Action) newActions.get(i2)).mergeBehavior();
                    if (map.containsKey(key) && mergeBehavior == 0) {
                        this.mActions.remove(map.get(key));
                        map.remove(key);
                    }
                    if (mergeBehavior == 0 || mergeBehavior == 1) {
                        this.mActions.add(a2);
                    }
                }
                this.mBitmapCache = new BitmapCache();
                setBitmapCache(this.mBitmapCache);
            }
        }
    }

    public void visitUris(Consumer<Uri> visitor) {
        if (this.mActions != null) {
            for (int i = 0; i < this.mActions.size(); i++) {
                ((Action) this.mActions.get(i)).visitUris(visitor);
            }
        }
    }

    private static void visitIconUri(Icon icon, Consumer<Uri> visitor) {
        if (icon != null && icon.getType() == 4) {
            visitor.accept(icon.getUri());
        }
    }

    public static Rect getSourceBounds(View v) {
        float appScale = v.getContext().getResources().getCompatibilityInfo().applicationScale;
        int[] pos = new int[2];
        v.getLocationOnScreen(pos);
        Rect rect = new Rect();
        rect.left = (int) ((((float) pos[0]) * appScale) + 0.5f);
        rect.top = (int) ((((float) pos[1]) * appScale) + 0.5f);
        rect.right = (int) ((((float) (pos[0] + v.getWidth())) * appScale) + 0.5f);
        rect.bottom = (int) ((((float) (pos[1] + v.getHeight())) * appScale) + 0.5f);
        return rect;
    }

    private MethodHandle getMethod(View view, String methodName, Class<?> paramType, boolean async) {
        MethodType asyncType;
        Class<? extends View> klass = view.getClass();
        synchronized (sMethods) {
            sLookupKey.set(klass, paramType, methodName);
            MethodArgs result = (MethodArgs) sMethods.get(sLookupKey);
            if (result == null) {
                Method method;
                if (paramType == null) {
                    try {
                        method = klass.getMethod(methodName, new Class[0]);
                    } catch (IllegalAccessException | NoSuchMethodException e) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Async implementation declared as ");
                        stringBuilder.append(result.asyncMethodName);
                        stringBuilder.append(" but not defined for ");
                        stringBuilder.append(methodName);
                        stringBuilder.append(": public Runnable ");
                        stringBuilder.append(result.asyncMethodName);
                        stringBuilder.append(" (");
                        stringBuilder.append(TextUtils.join((CharSequence) ",", asyncType.parameterArray()));
                        stringBuilder.append(")");
                        throw new ActionException(stringBuilder.toString());
                    } catch (IllegalAccessException | NoSuchMethodException e2) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("view: ");
                        stringBuilder2.append(klass.getName());
                        stringBuilder2.append(" doesn't have method: ");
                        stringBuilder2.append(methodName);
                        stringBuilder2.append(getParameters(paramType));
                        throw new ActionException(stringBuilder2.toString());
                    }
                }
                method = klass.getMethod(methodName, new Class[]{paramType});
                if (method.isAnnotationPresent(RemotableViewMethod.class)) {
                    result = new MethodArgs();
                    result.syncMethod = MethodHandles.publicLookup().unreflect(method);
                    result.asyncMethodName = ((RemotableViewMethod) method.getAnnotation(RemotableViewMethod.class)).asyncImpl();
                    MethodKey key = new MethodKey();
                    key.set(klass, paramType, methodName);
                    sMethods.put(key, result);
                } else {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("view: ");
                    stringBuilder3.append(klass.getName());
                    stringBuilder3.append(" can't use method with RemoteViews: ");
                    stringBuilder3.append(methodName);
                    stringBuilder3.append(getParameters(paramType));
                    throw new ActionException(stringBuilder3.toString());
                }
            }
            MethodHandle methodHandle;
            if (!async) {
                methodHandle = result.syncMethod;
                return methodHandle;
            } else if (result.asyncMethodName.isEmpty()) {
                return null;
            } else {
                if (result.asyncMethod == null) {
                    asyncType = result.syncMethod.type().dropParameterTypes(0, 1).changeReturnType(Runnable.class);
                    result.asyncMethod = MethodHandles.publicLookup().findVirtual(klass, result.asyncMethodName, asyncType);
                }
                methodHandle = result.asyncMethod;
                return methodHandle;
            }
        }
    }

    private static String getParameters(Class<?> paramType) {
        if (paramType == null) {
            return "()";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        stringBuilder.append(paramType);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private void configureRemoteViewsAsChild(RemoteViews rv) {
        rv.setBitmapCache(this.mBitmapCache);
        rv.setNotRoot();
    }

    /* Access modifiers changed, original: 0000 */
    public void setNotRoot() {
        this.mIsRoot = false;
    }

    public RemoteViews(String packageName, int layoutId) {
        this(getApplicationInfo(packageName, UserHandle.myUserId()), layoutId);
    }

    public RemoteViews(String packageName, int userId, int layoutId) {
        this(getApplicationInfo(packageName, userId), layoutId);
    }

    protected RemoteViews(ApplicationInfo application, int layoutId) {
        this.mLightBackgroundLayoutId = 0;
        this.mIsRoot = true;
        this.mLandscape = null;
        this.mPortrait = null;
        this.mApplyFlags = 0;
        this.mApplication = application;
        this.mLayoutId = layoutId;
        this.mBitmapCache = new BitmapCache();
        this.mClassCookies = null;
    }

    private boolean hasLandscapeAndPortraitLayouts() {
        return (this.mLandscape == null || this.mPortrait == null) ? false : true;
    }

    public RemoteViews(RemoteViews landscape, RemoteViews portrait) {
        this.mLightBackgroundLayoutId = 0;
        this.mIsRoot = true;
        this.mLandscape = null;
        this.mPortrait = null;
        this.mApplyFlags = 0;
        if (landscape == null || portrait == null) {
            throw new RuntimeException("Both RemoteViews must be non-null");
        } else if (landscape.hasSameAppInfo(portrait.mApplication)) {
            this.mApplication = portrait.mApplication;
            this.mLayoutId = portrait.mLayoutId;
            this.mLightBackgroundLayoutId = portrait.mLightBackgroundLayoutId;
            this.mLandscape = landscape;
            this.mPortrait = portrait;
            this.mBitmapCache = new BitmapCache();
            configureRemoteViewsAsChild(landscape);
            configureRemoteViewsAsChild(portrait);
            Map map = portrait.mClassCookies;
            if (map == null) {
                map = landscape.mClassCookies;
            }
            this.mClassCookies = map;
        } else {
            throw new RuntimeException("Both RemoteViews must share the same package and user");
        }
    }

    public RemoteViews(RemoteViews src) {
        this.mLightBackgroundLayoutId = 0;
        this.mIsRoot = true;
        this.mLandscape = null;
        this.mPortrait = null;
        this.mApplyFlags = 0;
        this.mBitmapCache = src.mBitmapCache;
        this.mApplication = src.mApplication;
        this.mIsRoot = src.mIsRoot;
        this.mLayoutId = src.mLayoutId;
        this.mLightBackgroundLayoutId = src.mLightBackgroundLayoutId;
        this.mApplyFlags = src.mApplyFlags;
        this.mClassCookies = src.mClassCookies;
        if (src.hasLandscapeAndPortraitLayouts()) {
            this.mLandscape = new RemoteViews(src.mLandscape);
            this.mPortrait = new RemoteViews(src.mPortrait);
        }
        if (src.mActions != null) {
            Parcel p = Parcel.obtain();
            p.putClassCookies(this.mClassCookies);
            src.writeActionsToParcel(p);
            p.setDataPosition(0);
            readActionsFromParcel(p, 0);
            p.recycle();
        }
        setBitmapCache(new BitmapCache());
    }

    public RemoteViews(Parcel parcel) {
        this(parcel, null, null, 0, null);
    }

    private RemoteViews(Parcel parcel, BitmapCache bitmapCache, ApplicationInfo info, int depth, Map<Class, Object> classCookies) {
        this.mLightBackgroundLayoutId = 0;
        this.mIsRoot = true;
        this.mLandscape = null;
        this.mPortrait = null;
        this.mApplyFlags = 0;
        if (depth <= 10 || UserHandle.getAppId(Binder.getCallingUid()) == 1000) {
            depth++;
            int mode = parcel.readInt();
            if (bitmapCache == null) {
                this.mBitmapCache = new BitmapCache(parcel);
                this.mClassCookies = parcel.copyClassCookies();
            } else {
                setBitmapCache(bitmapCache);
                this.mClassCookies = classCookies;
                setNotRoot();
            }
            if (mode == 0) {
                ApplicationInfo applicationInfo;
                if (parcel.readInt() == 0) {
                    applicationInfo = info;
                } else {
                    applicationInfo = (ApplicationInfo) ApplicationInfo.CREATOR.createFromParcel(parcel);
                }
                this.mApplication = applicationInfo;
                this.mLayoutId = parcel.readInt();
                this.mLightBackgroundLayoutId = parcel.readInt();
                readActionsFromParcel(parcel, depth);
            } else {
                Parcel parcel2 = parcel;
                int i = depth;
                this.mLandscape = new RemoteViews(parcel2, this.mBitmapCache, info, i, this.mClassCookies);
                this.mPortrait = new RemoteViews(parcel2, this.mBitmapCache, this.mLandscape.mApplication, i, this.mClassCookies);
                RemoteViews remoteViews = this.mPortrait;
                this.mApplication = remoteViews.mApplication;
                this.mLayoutId = remoteViews.mLayoutId;
                this.mLightBackgroundLayoutId = remoteViews.mLightBackgroundLayoutId;
            }
            this.mApplyFlags = parcel.readInt();
            return;
        }
        throw new IllegalArgumentException("Too many nested views.");
    }

    private void readActionsFromParcel(Parcel parcel, int depth) {
        int count = parcel.readInt();
        if (count > 0) {
            this.mActions = new ArrayList(count);
            for (int i = 0; i < count; i++) {
                this.mActions.add(getActionFromParcel(parcel, depth));
            }
        }
    }

    private Action getActionFromParcel(Parcel parcel, int depth) {
        int tag = parcel.readInt();
        switch (tag) {
            case 1:
                return new SetOnClickResponse(parcel);
            case 2:
                return new ReflectionAction(parcel);
            case 3:
                return new SetDrawableTint(parcel);
            case 4:
                return new ViewGroupActionAdd(parcel, this.mBitmapCache, this.mApplication, depth, this.mClassCookies);
            case 5:
                return new ViewContentNavigation(parcel);
            case 6:
                return new SetEmptyView(parcel);
            case 7:
                return new ViewGroupActionRemove(parcel);
            case 8:
                return new SetPendingIntentTemplate(parcel);
            case 10:
                return new SetRemoteViewsAdapterIntent(parcel);
            case 11:
                return new TextViewDrawableAction(parcel);
            case 12:
                return new BitmapReflectionAction(parcel);
            case 13:
                return new TextViewSizeAction(parcel);
            case 14:
                return new ViewPaddingAction(parcel);
            case 15:
                return new SetRemoteViewsAdapterList(parcel);
            case 18:
                return new SetRemoteInputsAction(parcel);
            case 19:
                return new LayoutParamAction(parcel);
            case 20:
                return new OverrideTextColorsAction(parcel);
            case 21:
                return new SetRippleDrawableColor(parcel);
            case 22:
                return new SetIntTagAction(parcel);
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Tag ");
                stringBuilder.append(tag);
                stringBuilder.append(" not found");
                throw new ActionException(stringBuilder.toString());
        }
    }

    @Deprecated
    public RemoteViews clone() {
        Preconditions.checkState(this.mIsRoot, "RemoteView has been attached to another RemoteView. May only clone the root of a RemoteView hierarchy.");
        return new RemoteViews(this);
    }

    public String getPackage() {
        ApplicationInfo applicationInfo = this.mApplication;
        return applicationInfo != null ? applicationInfo.packageName : null;
    }

    public int getLayoutId() {
        if (hasFlags(4)) {
            int i = this.mLightBackgroundLayoutId;
            if (i != 0) {
                return i;
            }
        }
        return this.mLayoutId;
    }

    private void setBitmapCache(BitmapCache bitmapCache) {
        this.mBitmapCache = bitmapCache;
        if (hasLandscapeAndPortraitLayouts()) {
            this.mLandscape.setBitmapCache(bitmapCache);
            this.mPortrait.setBitmapCache(bitmapCache);
            return;
        }
        int count = this.mActions;
        if (count != 0) {
            count = count.size();
            for (int i = 0; i < count; i++) {
                ((Action) this.mActions.get(i)).setBitmapCache(bitmapCache);
            }
        }
    }

    @UnsupportedAppUsage
    public int estimateMemoryUsage() {
        return this.mBitmapCache.getBitmapMemory();
    }

    private void addAction(Action a) {
        if (hasLandscapeAndPortraitLayouts()) {
            throw new RuntimeException("RemoteViews specifying separate landscape and portrait layouts cannot be modified. Instead, fully configure the landscape and portrait layouts individually before constructing the combined layout.");
        }
        if (this.mActions == null) {
            this.mActions = new ArrayList();
        }
        this.mActions.add(a);
    }

    public void addView(int viewId, RemoteViews nestedView) {
        Action viewGroupActionRemove;
        if (nestedView == null) {
            viewGroupActionRemove = new ViewGroupActionRemove(this, viewId);
        } else {
            viewGroupActionRemove = new ViewGroupActionAdd(this, viewId, nestedView);
        }
        addAction(viewGroupActionRemove);
    }

    @UnsupportedAppUsage
    public void addView(int viewId, RemoteViews nestedView, int index) {
        addAction(new ViewGroupActionAdd(viewId, nestedView, index));
    }

    public void removeAllViews(int viewId) {
        addAction(new ViewGroupActionRemove(this, viewId));
    }

    public void removeAllViewsExceptId(int viewId, int viewIdToKeep) {
        addAction(new ViewGroupActionRemove(viewId, viewIdToKeep));
    }

    public void showNext(int viewId) {
        addAction(new ViewContentNavigation(viewId, true));
    }

    public void showPrevious(int viewId) {
        addAction(new ViewContentNavigation(viewId, false));
    }

    public void setDisplayedChild(int viewId, int childIndex) {
        setInt(viewId, "setDisplayedChild", childIndex);
    }

    public void setViewVisibility(int viewId, int visibility) {
        setInt(viewId, "setVisibility", visibility);
    }

    public void setTextViewText(int viewId, CharSequence text) {
        setCharSequence(viewId, "setText", text);
    }

    public void setTextViewTextSize(int viewId, int units, float size) {
        addAction(new TextViewSizeAction(viewId, units, size));
    }

    public void setTextViewCompoundDrawables(int viewId, int left, int top, int right, int bottom) {
        addAction(new TextViewDrawableAction(viewId, false, left, top, right, bottom));
    }

    public void setTextViewCompoundDrawablesRelative(int viewId, int start, int top, int end, int bottom) {
        addAction(new TextViewDrawableAction(viewId, true, start, top, end, bottom));
    }

    public void setTextViewCompoundDrawables(int viewId, Icon left, Icon top, Icon right, Icon bottom) {
        addAction(new TextViewDrawableAction(viewId, false, left, top, right, bottom));
    }

    public void setTextViewCompoundDrawablesRelative(int viewId, Icon start, Icon top, Icon end, Icon bottom) {
        addAction(new TextViewDrawableAction(viewId, true, start, top, end, bottom));
    }

    public void setImageViewResource(int viewId, int srcId) {
        setInt(viewId, "setImageResource", srcId);
    }

    public void setImageViewUri(int viewId, Uri uri) {
        setUri(viewId, "setImageURI", uri);
    }

    public void setImageViewBitmap(int viewId, Bitmap bitmap) {
        setBitmap(viewId, "setImageBitmap", bitmap);
    }

    public void setImageViewIcon(int viewId, Icon icon) {
        setIcon(viewId, "setImageIcon", icon);
    }

    public void setEmptyView(int viewId, int emptyViewId) {
        addAction(new SetEmptyView(viewId, emptyViewId));
    }

    public void setChronometer(int viewId, long base, String format, boolean started) {
        setLong(viewId, "setBase", base);
        setString(viewId, "setFormat", format);
        setBoolean(viewId, "setStarted", started);
    }

    public void setChronometerCountDown(int viewId, boolean isCountDown) {
        setBoolean(viewId, "setCountDown", isCountDown);
    }

    public void setProgressBar(int viewId, int max, int progress, boolean indeterminate) {
        setBoolean(viewId, "setIndeterminate", indeterminate);
        if (!indeterminate) {
            setInt(viewId, "setMax", max);
            setInt(viewId, "setProgress", progress);
        }
    }

    public void setOnClickPendingIntent(int viewId, PendingIntent pendingIntent) {
        setOnClickResponse(viewId, RemoteResponse.fromPendingIntent(pendingIntent));
    }

    public void setOnClickResponse(int viewId, RemoteResponse response) {
        addAction(new SetOnClickResponse(viewId, response));
    }

    public void setPendingIntentTemplate(int viewId, PendingIntent pendingIntentTemplate) {
        addAction(new SetPendingIntentTemplate(viewId, pendingIntentTemplate));
    }

    public void setOnClickFillInIntent(int viewId, Intent fillInIntent) {
        setOnClickResponse(viewId, RemoteResponse.fromFillInIntent(fillInIntent));
    }

    public void setDrawableTint(int viewId, boolean targetBackground, int colorFilter, Mode mode) {
        addAction(new SetDrawableTint(viewId, targetBackground, colorFilter, mode));
    }

    public void setRippleDrawableColor(int viewId, ColorStateList colorStateList) {
        addAction(new SetRippleDrawableColor(viewId, colorStateList));
    }

    public void setProgressTintList(int viewId, ColorStateList tint) {
        addAction(new ReflectionAction(viewId, "setProgressTintList", 15, tint));
    }

    public void setProgressBackgroundTintList(int viewId, ColorStateList tint) {
        addAction(new ReflectionAction(viewId, "setProgressBackgroundTintList", 15, tint));
    }

    public void setProgressIndeterminateTintList(int viewId, ColorStateList tint) {
        addAction(new ReflectionAction(viewId, "setIndeterminateTintList", 15, tint));
    }

    public void setTextColor(int viewId, int color) {
        setInt(viewId, "setTextColor", color);
    }

    public void setTextColor(int viewId, ColorStateList colors) {
        addAction(new ReflectionAction(viewId, "setTextColor", 15, colors));
    }

    @Deprecated
    public void setRemoteAdapter(int appWidgetId, int viewId, Intent intent) {
        setRemoteAdapter(viewId, intent);
    }

    public void setRemoteAdapter(int viewId, Intent intent) {
        addAction(new SetRemoteViewsAdapterIntent(viewId, intent));
    }

    @Deprecated
    @UnsupportedAppUsage
    public void setRemoteAdapter(int viewId, ArrayList<RemoteViews> list, int viewTypeCount) {
        addAction(new SetRemoteViewsAdapterList(viewId, list, viewTypeCount));
    }

    public void setScrollPosition(int viewId, int position) {
        setInt(viewId, "smoothScrollToPosition", position);
    }

    public void setRelativeScrollPosition(int viewId, int offset) {
        setInt(viewId, "smoothScrollByOffset", offset);
    }

    public void setViewPadding(int viewId, int left, int top, int right, int bottom) {
        addAction(new ViewPaddingAction(viewId, left, top, right, bottom));
    }

    public void setViewLayoutMarginEndDimen(int viewId, int endMarginDimen) {
        addAction(new LayoutParamAction(viewId, 1, endMarginDimen));
    }

    public void setViewLayoutMarginEnd(int viewId, int endMargin) {
        addAction(new LayoutParamAction(viewId, 4, endMargin));
    }

    public void setViewLayoutMarginBottomDimen(int viewId, int bottomMarginDimen) {
        addAction(new LayoutParamAction(viewId, 3, bottomMarginDimen));
    }

    public void setViewLayoutWidth(int viewId, int layoutWidth) {
        if (layoutWidth == 0 || layoutWidth == -1 || layoutWidth == -2) {
            this.mActions.add(new LayoutParamAction(viewId, 2, layoutWidth));
            return;
        }
        throw new IllegalArgumentException("Only supports 0, WRAP_CONTENT and MATCH_PARENT");
    }

    public void setBoolean(int viewId, String methodName, boolean value) {
        addAction(new ReflectionAction(viewId, methodName, 1, Boolean.valueOf(value)));
    }

    public void setByte(int viewId, String methodName, byte value) {
        addAction(new ReflectionAction(viewId, methodName, 2, Byte.valueOf(value)));
    }

    public void setShort(int viewId, String methodName, short value) {
        addAction(new ReflectionAction(viewId, methodName, 3, Short.valueOf(value)));
    }

    public void setInt(int viewId, String methodName, int value) {
        addAction(new ReflectionAction(viewId, methodName, 4, Integer.valueOf(value)));
    }

    public void setColorStateList(int viewId, String methodName, ColorStateList value) {
        addAction(new ReflectionAction(viewId, methodName, 15, value));
    }

    public void setLong(int viewId, String methodName, long value) {
        addAction(new ReflectionAction(viewId, methodName, 5, Long.valueOf(value)));
    }

    public void setFloat(int viewId, String methodName, float value) {
        addAction(new ReflectionAction(viewId, methodName, 6, Float.valueOf(value)));
    }

    public void setDouble(int viewId, String methodName, double value) {
        addAction(new ReflectionAction(viewId, methodName, 7, Double.valueOf(value)));
    }

    public void setChar(int viewId, String methodName, char value) {
        addAction(new ReflectionAction(viewId, methodName, 8, Character.valueOf(value)));
    }

    public void setString(int viewId, String methodName, String value) {
        addAction(new ReflectionAction(viewId, methodName, 9, value));
    }

    public void setCharSequence(int viewId, String methodName, CharSequence value) {
        addAction(new ReflectionAction(viewId, methodName, 10, value));
    }

    public void setUri(int viewId, String methodName, Uri value) {
        if (value != null) {
            value = value.getCanonicalUri();
            if (StrictMode.vmFileUriExposureEnabled()) {
                value.checkFileUriExposed("RemoteViews.setUri()");
            }
        }
        addAction(new ReflectionAction(viewId, methodName, 11, value));
    }

    public void setBitmap(int viewId, String methodName, Bitmap value) {
        addAction(new BitmapReflectionAction(viewId, methodName, value));
    }

    public void setBundle(int viewId, String methodName, Bundle value) {
        addAction(new ReflectionAction(viewId, methodName, 13, value));
    }

    public void setIntent(int viewId, String methodName, Intent value) {
        addAction(new ReflectionAction(viewId, methodName, 14, value));
    }

    public void setIcon(int viewId, String methodName, Icon value) {
        addAction(new ReflectionAction(viewId, methodName, 16, value));
    }

    public void setContentDescription(int viewId, CharSequence contentDescription) {
        setCharSequence(viewId, "setContentDescription", contentDescription);
    }

    public void setAccessibilityTraversalBefore(int viewId, int nextId) {
        setInt(viewId, "setAccessibilityTraversalBefore", nextId);
    }

    public void setAccessibilityTraversalAfter(int viewId, int nextId) {
        setInt(viewId, "setAccessibilityTraversalAfter", nextId);
    }

    public void setLabelFor(int viewId, int labeledId) {
        setInt(viewId, "setLabelFor", labeledId);
    }

    public void setLightBackgroundLayoutId(int layoutId) {
        this.mLightBackgroundLayoutId = layoutId;
    }

    public RemoteViews getDarkTextViews() {
        if (hasFlags(4)) {
            return this;
        }
        try {
            addFlags(4);
            RemoteViews remoteViews = new RemoteViews(this);
            return remoteViews;
        } finally {
            this.mApplyFlags &= -5;
        }
    }

    private RemoteViews getRemoteViewsToApply(Context context) {
        if (!hasLandscapeAndPortraitLayouts()) {
            return this;
        }
        if (context.getResources().getConfiguration().orientation == 2) {
            return this.mLandscape;
        }
        return this.mPortrait;
    }

    public View apply(Context context, ViewGroup parent) {
        return apply(context, parent, null);
    }

    public View apply(Context context, ViewGroup parent, OnClickHandler handler) {
        RemoteViews rvToApply = getRemoteViewsToApply(context);
        View result = inflateView(context, rvToApply, parent);
        rvToApply.performApply(result, parent, handler);
        return result;
    }

    public View applyWithTheme(Context context, ViewGroup parent, OnClickHandler handler, int applyThemeResId) {
        RemoteViews rvToApply = getRemoteViewsToApply(context);
        View result = inflateView(context, rvToApply, parent, applyThemeResId);
        rvToApply.performApply(result, parent, handler);
        return result;
    }

    private View inflateView(Context context, RemoteViews rv, ViewGroup parent) {
        return inflateView(context, rv, parent, 0);
    }

    private View inflateView(Context context, RemoteViews rv, ViewGroup parent, int applyThemeResId) {
        Context inflationContext = new RemoteViewsContextWrapper(context, getContextForResources(context));
        if (applyThemeResId != 0) {
            inflationContext = new ContextThemeWrapper(inflationContext, applyThemeResId);
        }
        LayoutInflater inflater = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).cloneInContext(inflationContext);
        inflater.setFilter(this);
        View v = inflater.inflate(rv.getLayoutId(), parent, false);
        v.setTagInternal(16908312, Integer.valueOf(rv.getLayoutId()));
        return v;
    }

    public CancellationSignal applyAsync(Context context, ViewGroup parent, Executor executor, OnViewAppliedListener listener) {
        return applyAsync(context, parent, executor, listener, null);
    }

    private CancellationSignal startTaskOnExecutor(AsyncApplyTask task, Executor executor) {
        CancellationSignal cancelSignal = new CancellationSignal();
        cancelSignal.setOnCancelListener(task);
        task.executeOnExecutor(executor == null ? AsyncTask.THREAD_POOL_EXECUTOR : executor, new Void[0]);
        return cancelSignal;
    }

    public CancellationSignal applyAsync(Context context, ViewGroup parent, Executor executor, OnViewAppliedListener listener, OnClickHandler handler) {
        return startTaskOnExecutor(getAsyncApplyTask(context, parent, listener, handler), executor);
    }

    private AsyncApplyTask getAsyncApplyTask(Context context, ViewGroup parent, OnViewAppliedListener listener, OnClickHandler handler) {
        return new AsyncApplyTask(this, getRemoteViewsToApply(context), parent, context, listener, handler, null, null);
    }

    public void reapply(Context context, View v) {
        reapply(context, v, null);
    }

    public void reapply(Context context, View v, OnClickHandler handler) {
        RemoteViews rvToApply = getRemoteViewsToApply(context);
        if (!hasLandscapeAndPortraitLayouts() || ((Integer) v.getTag(16908312)).intValue() == rvToApply.getLayoutId()) {
            rvToApply.performApply(v, (ViewGroup) v.getParent(), handler);
            return;
        }
        throw new RuntimeException("Attempting to re-apply RemoteViews to a view that that does not share the same root layout id.");
    }

    public CancellationSignal reapplyAsync(Context context, View v, Executor executor, OnViewAppliedListener listener) {
        return reapplyAsync(context, v, executor, listener, null);
    }

    public CancellationSignal reapplyAsync(Context context, View v, Executor executor, OnViewAppliedListener listener, OnClickHandler handler) {
        RemoteViews rvToApply = getRemoteViewsToApply(context);
        View view;
        if (hasLandscapeAndPortraitLayouts()) {
            view = v;
            if (((Integer) v.getTag(16908312)).intValue() != rvToApply.getLayoutId()) {
                throw new RuntimeException("Attempting to re-apply RemoteViews to a view that that does not share the same root layout id.");
            }
        }
        view = v;
        AsyncApplyTask asyncApplyTask = new AsyncApplyTask(this, rvToApply, (ViewGroup) v.getParent(), context, listener, handler, v, null);
        Executor executor2 = executor;
        return startTaskOnExecutor(asyncApplyTask, executor);
    }

    private void performApply(View v, ViewGroup parent, OnClickHandler handler) {
        if (this.mActions != null) {
            handler = handler == null ? DEFAULT_ON_CLICK_HANDLER : handler;
            int count = this.mActions.size();
            for (int i = 0; i < count; i++) {
                ((Action) this.mActions.get(i)).apply(v, parent, handler);
            }
        }
    }

    public boolean prefersAsyncApply() {
        int count = this.mActions;
        if (count != 0) {
            count = count.size();
            for (int i = 0; i < count; i++) {
                if (((Action) this.mActions.get(i)).prefersAsyncApply()) {
                    return true;
                }
            }
        }
        return false;
    }

    private Context getContextForResources(Context context) {
        if (this.mApplication != null) {
            if (context.getUserId() == UserHandle.getUserId(this.mApplication.uid) && context.getPackageName().equals(this.mApplication.packageName)) {
                return context;
            }
            try {
                return context.createApplicationContext(this.mApplication, 4);
            } catch (NameNotFoundException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Package name ");
                stringBuilder.append(this.mApplication.packageName);
                stringBuilder.append(" not found");
                Log.e(LOG_TAG, stringBuilder.toString());
            }
        }
        return context;
    }

    public int getSequenceNumber() {
        ArrayList arrayList = this.mActions;
        return arrayList == null ? 0 : arrayList.size();
    }

    public boolean onLoadClass(Class clazz) {
        return clazz.isAnnotationPresent(RemoteView.class);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (hasLandscapeAndPortraitLayouts()) {
            dest.writeInt(1);
            if (this.mIsRoot) {
                this.mBitmapCache.writeBitmapsToParcel(dest, flags);
            }
            this.mLandscape.writeToParcel(dest, flags);
            this.mPortrait.writeToParcel(dest, flags | 2);
        } else {
            dest.writeInt(0);
            if (this.mIsRoot) {
                this.mBitmapCache.writeBitmapsToParcel(dest, flags);
            }
            if (this.mIsRoot || (flags & 2) == 0) {
                dest.writeInt(1);
                this.mApplication.writeToParcel(dest, flags);
            } else {
                dest.writeInt(0);
            }
            dest.writeInt(this.mLayoutId);
            dest.writeInt(this.mLightBackgroundLayoutId);
            writeActionsToParcel(dest);
        }
        dest.writeInt(this.mApplyFlags);
    }

    private void writeActionsToParcel(Parcel parcel) {
        int count = this.mActions;
        if (count != 0) {
            count = count.size();
        } else {
            count = 0;
        }
        parcel.writeInt(count);
        for (int i = 0; i < count; i++) {
            Action a = (Action) this.mActions.get(i);
            parcel.writeInt(a.getActionTag());
            a.writeToParcel(parcel, a.hasSameAppInfo(this.mApplication) ? 2 : 0);
        }
    }

    private static ApplicationInfo getApplicationInfo(String packageName, int userId) {
        if (packageName == null) {
            return null;
        }
        Application application = ActivityThread.currentApplication();
        if (application != null) {
            ApplicationInfo applicationInfo = application.getApplicationInfo();
            if (!(UserHandle.getUserId(applicationInfo.uid) == userId && applicationInfo.packageName.equals(packageName))) {
                try {
                    applicationInfo = application.getBaseContext().createPackageContextAsUser(packageName, 0, new UserHandle(userId)).getApplicationInfo();
                } catch (NameNotFoundException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("No such package ");
                    stringBuilder.append(packageName);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            return applicationInfo;
        }
        throw new IllegalStateException("Cannot create remote views out of an aplication.");
    }

    public boolean hasSameAppInfo(ApplicationInfo info) {
        return this.mApplication.packageName.equals(info.packageName) && this.mApplication.uid == info.uid;
    }

    public static boolean startPendingIntent(View view, PendingIntent pendingIntent, Pair<Intent, ActivityOptions> options) {
        String str = LOG_TAG;
        try {
            view.getContext().startIntentSender(pendingIntent.getIntentSender(), (Intent) options.first, 0, 0, 0, ((ActivityOptions) options.second).toBundle());
            return true;
        } catch (SendIntentException e) {
            Log.e(str, "Cannot send pending intent: ", e);
            return false;
        } catch (Exception e2) {
            Log.e(str, "Cannot send pending intent due to unknown exception: ", e2);
            return false;
        }
    }
}
