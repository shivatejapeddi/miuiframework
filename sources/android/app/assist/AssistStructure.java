package android.app.assist;

import android.annotation.SystemApi;
import android.app.Activity;
import android.app.slice.Slice;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.TrafficStats;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.BadParcelableException;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PooledStringReader;
import android.os.PooledStringWriter;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.TimedRemoteCaller;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.ViewStructure;
import android.view.ViewStructure.HtmlInfo;
import android.view.ViewStructure.HtmlInfo.Builder;
import android.view.WindowManagerGlobal;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssistStructure implements Parcelable {
    public static final Creator<AssistStructure> CREATOR = new Creator<AssistStructure>() {
        public AssistStructure createFromParcel(Parcel in) {
            return new AssistStructure(in);
        }

        public AssistStructure[] newArray(int size) {
            return new AssistStructure[size];
        }
    };
    private static final boolean DEBUG_PARCEL = false;
    private static final boolean DEBUG_PARCEL_CHILDREN = false;
    private static final boolean DEBUG_PARCEL_TREE = false;
    private static final String DESCRIPTOR = "android.app.AssistStructure";
    private static final String TAG = "AssistStructure";
    private static final int TRANSACTION_XFER = 2;
    private static final int VALIDATE_VIEW_TOKEN = 572662306;
    private static final int VALIDATE_WINDOW_TOKEN = 286331153;
    private long mAcquisitionEndTime;
    private long mAcquisitionStartTime;
    private ComponentName mActivityComponent;
    private int mAutofillFlags;
    private int mFlags;
    private boolean mHaveData;
    private boolean mIsHomeActivity;
    private final ArrayList<ViewNodeBuilder> mPendingAsyncChildren;
    private IBinder mReceiveChannel;
    private boolean mSanitizeOnWrite;
    private SendChannel mSendChannel;
    private int mTaskId;
    private Rect mTmpRect;
    private final ArrayList<WindowNode> mWindowNodes;

    public static class AutofillOverlay {
        public boolean focused;
        public AutofillValue value;
    }

    private static final class HtmlInfoNode extends HtmlInfo implements Parcelable {
        public static final Creator<HtmlInfoNode> CREATOR = new Creator<HtmlInfoNode>() {
            public HtmlInfoNode createFromParcel(Parcel parcel) {
                HtmlInfoNodeBuilder builder = new HtmlInfoNodeBuilder(parcel.readString());
                String[] names = parcel.readStringArray();
                String[] values = parcel.readStringArray();
                if (!(names == null || values == null)) {
                    if (names.length != values.length) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("HtmlInfo attributes mismatch: names=");
                        stringBuilder.append(names.length);
                        stringBuilder.append(", values=");
                        stringBuilder.append(values.length);
                        Log.w(AssistStructure.TAG, stringBuilder.toString());
                    } else {
                        for (int i = 0; i < names.length; i++) {
                            builder.addAttribute(names[i], values[i]);
                        }
                    }
                }
                return builder.build();
            }

            public HtmlInfoNode[] newArray(int size) {
                return new HtmlInfoNode[size];
            }
        };
        private ArrayList<Pair<String, String>> mAttributes;
        private final String[] mNames;
        private final String mTag;
        private final String[] mValues;

        /* synthetic */ HtmlInfoNode(HtmlInfoNodeBuilder x0, AnonymousClass1 x1) {
            this(x0);
        }

        private HtmlInfoNode(HtmlInfoNodeBuilder builder) {
            this.mTag = builder.mTag;
            if (builder.mNames == null) {
                this.mNames = null;
                this.mValues = null;
                return;
            }
            this.mNames = new String[builder.mNames.size()];
            this.mValues = new String[builder.mValues.size()];
            builder.mNames.toArray(this.mNames);
            builder.mValues.toArray(this.mValues);
        }

        public String getTag() {
            return this.mTag;
        }

        public List<Pair<String, String>> getAttributes() {
            if (this.mAttributes == null) {
                String[] strArr = this.mNames;
                if (strArr != null) {
                    this.mAttributes = new ArrayList(strArr.length);
                    int i = 0;
                    while (true) {
                        String[] strArr2 = this.mNames;
                        if (i >= strArr2.length) {
                            break;
                        }
                        this.mAttributes.add(i, new Pair(strArr2[i], this.mValues[i]));
                        i++;
                    }
                }
            }
            return this.mAttributes;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeString(this.mTag);
            parcel.writeStringArray(this.mNames);
            parcel.writeStringArray(this.mValues);
        }
    }

    private static final class HtmlInfoNodeBuilder extends Builder {
        private ArrayList<String> mNames;
        private final String mTag;
        private ArrayList<String> mValues;

        HtmlInfoNodeBuilder(String tag) {
            this.mTag = tag;
        }

        public Builder addAttribute(String name, String value) {
            if (this.mNames == null) {
                this.mNames = new ArrayList();
                this.mValues = new ArrayList();
            }
            this.mNames.add(name);
            this.mValues.add(value);
            return this;
        }

        public HtmlInfoNode build() {
            return new HtmlInfoNode(this, null);
        }
    }

    final class ParcelTransferReader {
        private final IBinder mChannel;
        private Parcel mCurParcel;
        int mNumReadViews;
        int mNumReadWindows;
        PooledStringReader mStringReader;
        final float[] mTmpMatrix = new float[9];
        private IBinder mTransferToken;

        ParcelTransferReader(IBinder channel) {
            this.mChannel = channel;
        }

        /* Access modifiers changed, original: 0000 */
        public void go() {
            fetchData();
            AssistStructure.this.mFlags = this.mCurParcel.readInt();
            AssistStructure.this.mAutofillFlags = this.mCurParcel.readInt();
            AssistStructure.this.mAcquisitionStartTime = this.mCurParcel.readLong();
            AssistStructure.this.mAcquisitionEndTime = this.mCurParcel.readLong();
            int N = this.mCurParcel.readInt();
            if (N > 0) {
                this.mStringReader = new PooledStringReader(this.mCurParcel);
                for (int i = 0; i < N; i++) {
                    AssistStructure.this.mWindowNodes.add(new WindowNode(this));
                }
            }
            this.mCurParcel.recycle();
            this.mCurParcel = null;
        }

        /* Access modifiers changed, original: 0000 */
        public Parcel readParcel(int validateToken, int level) {
            int token = this.mCurParcel.readInt();
            if (token == 0) {
                this.mTransferToken = this.mCurParcel.readStrongBinder();
                if (this.mTransferToken != null) {
                    fetchData();
                    this.mStringReader = new PooledStringReader(this.mCurParcel);
                    this.mCurParcel.readInt();
                    return this.mCurParcel;
                }
                throw new IllegalStateException("Reached end of partial data without transfer token");
            } else if (token == validateToken) {
                return this.mCurParcel;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Got token ");
                stringBuilder.append(Integer.toHexString(token));
                stringBuilder.append(", expected token ");
                stringBuilder.append(Integer.toHexString(validateToken));
                throw new BadParcelableException(stringBuilder.toString());
            }
        }

        private void fetchData() {
            Parcel data = Parcel.obtain();
            try {
                data.writeInterfaceToken(AssistStructure.DESCRIPTOR);
                data.writeStrongBinder(this.mTransferToken);
                if (this.mCurParcel != null) {
                    this.mCurParcel.recycle();
                }
                this.mCurParcel = Parcel.obtain();
                this.mChannel.transact(2, data, this.mCurParcel, 0);
                data.recycle();
                this.mNumReadViews = 0;
                this.mNumReadWindows = 0;
            } catch (RemoteException e) {
                Log.w(AssistStructure.TAG, "Failure reading AssistStructure data", e);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failure reading AssistStructure data: ");
                stringBuilder.append(e);
                throw new IllegalStateException(stringBuilder.toString());
            } catch (Throwable th) {
                data.recycle();
            }
        }
    }

    static final class ParcelTransferWriter extends Binder {
        ViewStackEntry mCurViewStackEntry;
        int mCurViewStackPos;
        int mCurWindow;
        int mNumWindows;
        int mNumWrittenViews;
        int mNumWrittenWindows;
        final boolean mSanitizeOnWrite;
        final float[] mTmpMatrix = new float[9];
        final ArrayList<ViewStackEntry> mViewStack = new ArrayList();
        final boolean mWriteStructure;

        ParcelTransferWriter(AssistStructure as, Parcel out) {
            this.mSanitizeOnWrite = as.mSanitizeOnWrite;
            this.mWriteStructure = as.waitForReady();
            out.writeInt(as.mFlags);
            out.writeInt(as.mAutofillFlags);
            out.writeLong(as.mAcquisitionStartTime);
            out.writeLong(as.mAcquisitionEndTime);
            this.mNumWindows = as.mWindowNodes.size();
            if (this.mWriteStructure) {
                int i = this.mNumWindows;
                if (i > 0) {
                    out.writeInt(i);
                    return;
                }
            }
            out.writeInt(0);
        }

        /* Access modifiers changed, original: 0000 */
        public void writeToParcel(AssistStructure as, Parcel out) {
            int start = out.dataPosition();
            this.mNumWrittenWindows = 0;
            this.mNumWrittenViews = 0;
            boolean more = writeToParcelInner(as, out);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Flattened ");
            stringBuilder.append(more ? Slice.HINT_PARTIAL : "final");
            stringBuilder.append(" assist data: ");
            stringBuilder.append(out.dataPosition() - start);
            stringBuilder.append(" bytes, containing ");
            stringBuilder.append(this.mNumWrittenWindows);
            stringBuilder.append(" windows, ");
            stringBuilder.append(this.mNumWrittenViews);
            stringBuilder.append(" views");
            Log.i(AssistStructure.TAG, stringBuilder.toString());
        }

        /* Access modifiers changed, original: 0000 */
        public boolean writeToParcelInner(AssistStructure as, Parcel out) {
            if (this.mNumWindows == 0) {
                return false;
            }
            PooledStringWriter pwriter = new PooledStringWriter(out);
            while (writeNextEntryToParcel(as, out, pwriter)) {
                if (out.dataSize() > 65536) {
                    out.writeInt(0);
                    out.writeStrongBinder(this);
                    pwriter.finish();
                    return true;
                }
            }
            pwriter.finish();
            this.mViewStack.clear();
            return false;
        }

        /* Access modifiers changed, original: 0000 */
        public void pushViewStackEntry(ViewNode node, int pos) {
            ViewStackEntry entry;
            if (pos >= this.mViewStack.size()) {
                entry = new ViewStackEntry();
                this.mViewStack.add(entry);
            } else {
                entry = (ViewStackEntry) this.mViewStack.get(pos);
            }
            entry.node = node;
            entry.numChildren = node.getChildCount();
            entry.curChild = 0;
            this.mCurViewStackEntry = entry;
        }

        /* Access modifiers changed, original: 0000 */
        public void writeView(ViewNode child, Parcel out, PooledStringWriter pwriter, int levelAdj) {
            out.writeInt(AssistStructure.VALIDATE_VIEW_TOKEN);
            int flags = child.writeSelfToParcel(out, pwriter, this.mSanitizeOnWrite, this.mTmpMatrix);
            this.mNumWrittenViews++;
            if ((1048576 & flags) != 0) {
                out.writeInt(child.mChildren.length);
                int pos = this.mCurViewStackPos + 1;
                this.mCurViewStackPos = pos;
                pushViewStackEntry(child, pos);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public boolean writeNextEntryToParcel(AssistStructure as, Parcel out, PooledStringWriter pwriter) {
            ViewStackEntry viewStackEntry = this.mCurViewStackEntry;
            int pos;
            if (viewStackEntry == null) {
                pos = this.mCurWindow;
                if (pos >= this.mNumWindows) {
                    return false;
                }
                WindowNode win = (WindowNode) as.mWindowNodes.get(pos);
                this.mCurWindow++;
                out.writeInt(AssistStructure.VALIDATE_WINDOW_TOKEN);
                win.writeSelfToParcel(out, pwriter, this.mTmpMatrix);
                this.mNumWrittenWindows++;
                ViewNode root = win.mRoot;
                this.mCurViewStackPos = 0;
                writeView(root, out, pwriter, 0);
                return true;
            } else if (viewStackEntry.curChild < this.mCurViewStackEntry.numChildren) {
                ViewNode child = this.mCurViewStackEntry.node.mChildren[this.mCurViewStackEntry.curChild];
                ViewStackEntry viewStackEntry2 = this.mCurViewStackEntry;
                viewStackEntry2.curChild++;
                writeView(child, out, pwriter, 1);
                return true;
            } else {
                do {
                    pos = this.mCurViewStackPos - 1;
                    this.mCurViewStackPos = pos;
                    if (pos < 0) {
                        this.mCurViewStackEntry = null;
                        break;
                    }
                    this.mCurViewStackEntry = (ViewStackEntry) this.mViewStack.get(pos);
                } while (this.mCurViewStackEntry.curChild >= this.mCurViewStackEntry.numChildren);
                return true;
            }
        }
    }

    static final class SendChannel extends Binder {
        volatile AssistStructure mAssistStructure;

        SendChannel(AssistStructure as) {
            this.mAssistStructure = as;
        }

        /* Access modifiers changed, original: protected */
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 2) {
                return super.onTransact(code, data, reply, flags);
            }
            AssistStructure as = this.mAssistStructure;
            if (as == null) {
                return true;
            }
            data.enforceInterface(AssistStructure.DESCRIPTOR);
            IBinder token = data.readStrongBinder();
            if (token == null) {
                new ParcelTransferWriter(as, reply).writeToParcel(as, reply);
                return true;
            } else if (token instanceof ParcelTransferWriter) {
                ((ParcelTransferWriter) token).writeToParcel(as, reply);
                return true;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Caller supplied bad token type: ");
                stringBuilder.append(token);
                Log.w(AssistStructure.TAG, stringBuilder.toString());
                return true;
            }
        }
    }

    public static class ViewNode {
        static final int AUTOFILL_FLAGS_HAS_AUTOFILL_HINTS = 16;
        static final int AUTOFILL_FLAGS_HAS_AUTOFILL_OPTIONS = 32;
        static final int AUTOFILL_FLAGS_HAS_AUTOFILL_SESSION_ID = 2048;
        static final int AUTOFILL_FLAGS_HAS_AUTOFILL_TYPE = 8;
        static final int AUTOFILL_FLAGS_HAS_AUTOFILL_VALUE = 4;
        static final int AUTOFILL_FLAGS_HAS_AUTOFILL_VIEW_ID = 1;
        static final int AUTOFILL_FLAGS_HAS_AUTOFILL_VIRTUAL_VIEW_ID = 2;
        static final int AUTOFILL_FLAGS_HAS_HTML_INFO = 64;
        static final int AUTOFILL_FLAGS_HAS_MAX_TEXT_EMS = 512;
        static final int AUTOFILL_FLAGS_HAS_MAX_TEXT_LENGTH = 1024;
        static final int AUTOFILL_FLAGS_HAS_MIN_TEXT_EMS = 256;
        static final int AUTOFILL_FLAGS_HAS_TEXT_ID_ENTRY = 128;
        static final int FLAGS_ACCESSIBILITY_FOCUSED = 4096;
        static final int FLAGS_ACTIVATED = 8192;
        static final int FLAGS_ALL_CONTROL = -1048576;
        static final int FLAGS_ASSIST_BLOCKED = 128;
        static final int FLAGS_CHECKABLE = 256;
        static final int FLAGS_CHECKED = 512;
        static final int FLAGS_CLICKABLE = 1024;
        static final int FLAGS_CONTEXT_CLICKABLE = 16384;
        static final int FLAGS_DISABLED = 1;
        static final int FLAGS_FOCUSABLE = 16;
        static final int FLAGS_FOCUSED = 32;
        static final int FLAGS_HAS_ALPHA = 536870912;
        static final int FLAGS_HAS_CHILDREN = 1048576;
        static final int FLAGS_HAS_COMPLEX_TEXT = 8388608;
        static final int FLAGS_HAS_CONTENT_DESCRIPTION = 33554432;
        static final int FLAGS_HAS_ELEVATION = 268435456;
        static final int FLAGS_HAS_EXTRAS = 4194304;
        static final int FLAGS_HAS_ID = 2097152;
        static final int FLAGS_HAS_INPUT_TYPE = 262144;
        static final int FLAGS_HAS_LARGE_COORDS = 67108864;
        static final int FLAGS_HAS_LOCALE_LIST = 65536;
        static final int FLAGS_HAS_MATRIX = 1073741824;
        static final int FLAGS_HAS_SCROLL = 134217728;
        static final int FLAGS_HAS_TEXT = 16777216;
        static final int FLAGS_HAS_URL = 524288;
        static final int FLAGS_LONG_CLICKABLE = 2048;
        static final int FLAGS_OPAQUE = 32768;
        static final int FLAGS_SELECTED = 64;
        static final int FLAGS_VISIBILITY_MASK = 12;
        public static final int TEXT_COLOR_UNDEFINED = 1;
        public static final int TEXT_STYLE_BOLD = 1;
        public static final int TEXT_STYLE_ITALIC = 2;
        public static final int TEXT_STYLE_STRIKE_THRU = 8;
        public static final int TEXT_STYLE_UNDERLINE = 4;
        float mAlpha;
        int mAutofillFlags;
        String[] mAutofillHints;
        AutofillId mAutofillId;
        CharSequence[] mAutofillOptions;
        AutofillOverlay mAutofillOverlay;
        int mAutofillType;
        AutofillValue mAutofillValue;
        ViewNode[] mChildren;
        String mClassName;
        CharSequence mContentDescription;
        float mElevation;
        Bundle mExtras;
        int mFlags;
        int mHeight;
        HtmlInfo mHtmlInfo;
        int mId;
        String mIdEntry;
        String mIdPackage;
        String mIdType;
        int mImportantForAutofill;
        int mInputType;
        LocaleList mLocaleList;
        Matrix mMatrix;
        int mMaxEms;
        int mMaxLength;
        int mMinEms;
        boolean mSanitized;
        int mScrollX;
        int mScrollY;
        ViewNodeText mText;
        String mTextIdEntry;
        String mWebDomain;
        String mWebScheme;
        int mWidth;
        int mX;
        int mY;

        @SystemApi
        public ViewNode() {
            this.mId = -1;
            this.mAutofillType = 0;
            this.mMinEms = -1;
            this.mMaxEms = -1;
            this.mMaxLength = -1;
            this.mAlpha = 1.0f;
        }

        ViewNode(ParcelTransferReader reader, int nestingLevel) {
            int autofillViewId;
            this.mId = -1;
            boolean z = false;
            this.mAutofillType = 0;
            this.mMinEms = -1;
            this.mMaxEms = -1;
            this.mMaxLength = -1;
            this.mAlpha = 1.0f;
            Parcel in = reader.readParcel(AssistStructure.VALIDATE_VIEW_TOKEN, nestingLevel);
            reader.mNumReadViews++;
            PooledStringReader preader = reader.mStringReader;
            this.mClassName = preader.readString();
            this.mFlags = in.readInt();
            int flags = this.mFlags;
            this.mAutofillFlags = in.readInt();
            int autofillFlags = this.mAutofillFlags;
            if ((2097152 & flags) != 0) {
                this.mId = in.readInt();
                if (this.mId != -1) {
                    this.mIdEntry = preader.readString();
                    if (this.mIdEntry != null) {
                        this.mIdType = preader.readString();
                        this.mIdPackage = preader.readString();
                    }
                }
            }
            if (autofillFlags != 0) {
                this.mSanitized = in.readInt() == 1;
                this.mImportantForAutofill = in.readInt();
                if ((autofillFlags & 1) != 0) {
                    autofillViewId = in.readInt();
                    if ((autofillFlags & 2) != 0) {
                        this.mAutofillId = new AutofillId(autofillViewId, in.readInt());
                    } else {
                        this.mAutofillId = new AutofillId(autofillViewId);
                    }
                    if ((autofillFlags & 2048) != 0) {
                        this.mAutofillId.setSessionId(in.readInt());
                    }
                }
                if ((autofillFlags & 8) != 0) {
                    this.mAutofillType = in.readInt();
                }
                if ((autofillFlags & 16) != 0) {
                    this.mAutofillHints = in.readStringArray();
                }
                if ((autofillFlags & 4) != 0) {
                    this.mAutofillValue = (AutofillValue) in.readParcelable(null);
                }
                if ((autofillFlags & 32) != 0) {
                    this.mAutofillOptions = in.readCharSequenceArray();
                }
                if ((autofillFlags & 64) != 0) {
                    this.mHtmlInfo = (HtmlInfo) in.readParcelable(null);
                }
                if ((autofillFlags & 256) != 0) {
                    this.mMinEms = in.readInt();
                }
                if ((autofillFlags & 512) != 0) {
                    this.mMaxEms = in.readInt();
                }
                if ((autofillFlags & 1024) != 0) {
                    this.mMaxLength = in.readInt();
                }
                if ((autofillFlags & 128) != 0) {
                    this.mTextIdEntry = preader.readString();
                }
            }
            if ((67108864 & flags) != 0) {
                this.mX = in.readInt();
                this.mY = in.readInt();
                this.mWidth = in.readInt();
                this.mHeight = in.readInt();
            } else {
                autofillViewId = in.readInt();
                this.mX = autofillViewId & 32767;
                this.mY = (autofillViewId >> 16) & 32767;
                autofillViewId = in.readInt();
                this.mWidth = autofillViewId & 32767;
                this.mHeight = (autofillViewId >> 16) & 32767;
            }
            if ((134217728 & flags) != 0) {
                this.mScrollX = in.readInt();
                this.mScrollY = in.readInt();
            }
            if ((1073741824 & flags) != 0) {
                this.mMatrix = new Matrix();
                in.readFloatArray(reader.mTmpMatrix);
                this.mMatrix.setValues(reader.mTmpMatrix);
            }
            if ((268435456 & flags) != 0) {
                this.mElevation = in.readFloat();
            }
            if ((536870912 & flags) != 0) {
                this.mAlpha = in.readFloat();
            }
            if ((33554432 & flags) != 0) {
                this.mContentDescription = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            }
            if ((16777216 & flags) != 0) {
                if ((8388608 & flags) == 0) {
                    z = true;
                }
                this.mText = new ViewNodeText(in, z);
            }
            if ((262144 & flags) != 0) {
                this.mInputType = in.readInt();
            }
            if ((524288 & flags) != 0) {
                this.mWebScheme = in.readString();
                this.mWebDomain = in.readString();
            }
            if ((65536 & flags) != 0) {
                this.mLocaleList = (LocaleList) in.readParcelable(null);
            }
            if ((4194304 & flags) != 0) {
                this.mExtras = in.readBundle();
            }
            if ((1048576 & flags) != 0) {
                int NCHILDREN = in.readInt();
                this.mChildren = new ViewNode[NCHILDREN];
                for (int i = 0; i < NCHILDREN; i++) {
                    this.mChildren[i] = new ViewNode(reader, nestingLevel + 1);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        /* JADX WARNING: Missing block: B:16:0x003a, code skipped:
            if ((((r0.mWidth & -32768) != 0 ? 1 : 0) | ((r0.mHeight & -32768) != 0 ? 1 : 0)) != 0) goto L_0x003c;
     */
        public int writeSelfToParcel(android.os.Parcel r21, android.os.PooledStringWriter r22, boolean r23, float[] r24) {
            /*
            r20 = this;
            r0 = r20;
            r1 = r21;
            r2 = r22;
            r3 = r24;
            r4 = 1;
            r5 = r0.mFlags;
            r6 = 1048575; // 0xfffff float:1.469367E-39 double:5.18065E-318;
            r5 = r5 & r6;
            r6 = 0;
            r7 = r0.mId;
            r8 = 2097152; // 0x200000 float:2.938736E-39 double:1.0361308E-317;
            r9 = -1;
            if (r7 == r9) goto L_0x0018;
        L_0x0017:
            r5 = r5 | r8;
        L_0x0018:
            r7 = r0.mX;
            r7 = r7 & -32768;
            r10 = 67108864; // 0x4000000 float:1.5046328E-36 double:3.31561842E-316;
            r12 = 0;
            if (r7 != 0) goto L_0x003c;
        L_0x0021:
            r7 = r0.mY;
            r7 = r7 & -32768;
            if (r7 != 0) goto L_0x003c;
        L_0x0027:
            r7 = r0.mWidth;
            r7 = r7 & -32768;
            if (r7 == 0) goto L_0x002f;
        L_0x002d:
            r7 = 1;
            goto L_0x0030;
        L_0x002f:
            r7 = r12;
        L_0x0030:
            r13 = r0.mHeight;
            r13 = r13 & -32768;
            if (r13 == 0) goto L_0x0038;
        L_0x0036:
            r13 = 1;
            goto L_0x0039;
        L_0x0038:
            r13 = r12;
        L_0x0039:
            r7 = r7 | r13;
            if (r7 == 0) goto L_0x003d;
        L_0x003c:
            r5 = r5 | r10;
        L_0x003d:
            r7 = r0.mScrollX;
            r13 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
            if (r7 != 0) goto L_0x0047;
        L_0x0043:
            r7 = r0.mScrollY;
            if (r7 == 0) goto L_0x0048;
        L_0x0047:
            r5 = r5 | r13;
        L_0x0048:
            r7 = r0.mMatrix;
            r14 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
            if (r7 == 0) goto L_0x004f;
        L_0x004e:
            r5 = r5 | r14;
        L_0x004f:
            r7 = r0.mElevation;
            r15 = 0;
            r7 = (r7 > r15 ? 1 : (r7 == r15 ? 0 : -1));
            r15 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
            if (r7 == 0) goto L_0x0059;
        L_0x0058:
            r5 = r5 | r15;
        L_0x0059:
            r7 = r0.mAlpha;
            r16 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r7 = (r7 > r16 ? 1 : (r7 == r16 ? 0 : -1));
            r16 = 536870912; // 0x20000000 float:1.0842022E-19 double:2.652494739E-315;
            if (r7 == 0) goto L_0x0065;
        L_0x0063:
            r5 = r5 | r16;
        L_0x0065:
            r7 = r0.mContentDescription;
            r17 = 33554432; // 0x2000000 float:9.403955E-38 double:1.6578092E-316;
            if (r7 == 0) goto L_0x006d;
        L_0x006b:
            r5 = r5 | r17;
        L_0x006d:
            r7 = r0.mText;
            r18 = 16777216; // 0x1000000 float:2.3509887E-38 double:8.289046E-317;
            if (r7 == 0) goto L_0x007e;
        L_0x0073:
            r5 = r5 | r18;
            r7 = r7.isSimple();
            if (r7 != 0) goto L_0x007e;
        L_0x007b:
            r7 = 8388608; // 0x800000 float:1.17549435E-38 double:4.144523E-317;
            r5 = r5 | r7;
        L_0x007e:
            r7 = r0.mInputType;
            r19 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
            if (r7 == 0) goto L_0x0086;
        L_0x0084:
            r5 = r5 | r19;
        L_0x0086:
            r7 = r0.mWebScheme;
            if (r7 != 0) goto L_0x008e;
        L_0x008a:
            r7 = r0.mWebDomain;
            if (r7 == 0) goto L_0x0091;
        L_0x008e:
            r7 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
            r5 = r5 | r7;
        L_0x0091:
            r7 = r0.mLocaleList;
            if (r7 == 0) goto L_0x0098;
        L_0x0095:
            r7 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
            r5 = r5 | r7;
        L_0x0098:
            r7 = r0.mExtras;
            if (r7 == 0) goto L_0x009f;
        L_0x009c:
            r7 = 4194304; // 0x400000 float:5.877472E-39 double:2.0722615E-317;
            r5 = r5 | r7;
        L_0x009f:
            r7 = r0.mChildren;
            if (r7 == 0) goto L_0x00a6;
        L_0x00a3:
            r7 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
            r5 = r5 | r7;
        L_0x00a6:
            r7 = r0.mAutofillId;
            if (r7 == 0) goto L_0x00be;
        L_0x00aa:
            r6 = r6 | 1;
            r7 = r7.isVirtualInt();
            if (r7 == 0) goto L_0x00b4;
        L_0x00b2:
            r6 = r6 | 2;
        L_0x00b4:
            r7 = r0.mAutofillId;
            r7 = r7.hasSession();
            if (r7 == 0) goto L_0x00be;
        L_0x00bc:
            r6 = r6 | 2048;
        L_0x00be:
            r7 = r0.mAutofillValue;
            if (r7 == 0) goto L_0x00c4;
        L_0x00c2:
            r6 = r6 | 4;
        L_0x00c4:
            r7 = r0.mAutofillType;
            if (r7 == 0) goto L_0x00ca;
        L_0x00c8:
            r6 = r6 | 8;
        L_0x00ca:
            r7 = r0.mAutofillHints;
            if (r7 == 0) goto L_0x00d0;
        L_0x00ce:
            r6 = r6 | 16;
        L_0x00d0:
            r7 = r0.mAutofillOptions;
            if (r7 == 0) goto L_0x00d6;
        L_0x00d4:
            r6 = r6 | 32;
        L_0x00d6:
            r7 = r0.mHtmlInfo;
            r7 = r7 instanceof android.os.Parcelable;
            if (r7 == 0) goto L_0x00de;
        L_0x00dc:
            r6 = r6 | 64;
        L_0x00de:
            r7 = r0.mMinEms;
            if (r7 <= r9) goto L_0x00e4;
        L_0x00e2:
            r6 = r6 | 256;
        L_0x00e4:
            r7 = r0.mMaxEms;
            if (r7 <= r9) goto L_0x00ea;
        L_0x00e8:
            r6 = r6 | 512;
        L_0x00ea:
            r7 = r0.mMaxLength;
            if (r7 <= r9) goto L_0x00f0;
        L_0x00ee:
            r6 = r6 | 1024;
        L_0x00f0:
            r7 = r0.mTextIdEntry;
            if (r7 == 0) goto L_0x00f6;
        L_0x00f4:
            r6 = r6 | 128;
        L_0x00f6:
            r7 = r0.mClassName;
            r2.writeString(r7);
            r7 = r5;
            if (r6 == 0) goto L_0x0106;
        L_0x00fe:
            r11 = r0.mSanitized;
            if (r11 != 0) goto L_0x0104;
        L_0x0102:
            if (r23 != 0) goto L_0x0106;
        L_0x0104:
            r7 = r5 & -513;
        L_0x0106:
            r11 = r0.mAutofillOverlay;
            if (r11 == 0) goto L_0x0113;
        L_0x010a:
            r11 = r11.focused;
            if (r11 == 0) goto L_0x0111;
        L_0x010e:
            r7 = r7 | 32;
            goto L_0x0113;
        L_0x0111:
            r7 = r7 & -33;
        L_0x0113:
            r1.writeInt(r7);
            r1.writeInt(r6);
            r8 = r8 & r5;
            if (r8 == 0) goto L_0x0138;
        L_0x011c:
            r8 = r0.mId;
            r1.writeInt(r8);
            r8 = r0.mId;
            if (r8 == r9) goto L_0x0138;
        L_0x0125:
            r8 = r0.mIdEntry;
            r2.writeString(r8);
            r8 = r0.mIdEntry;
            if (r8 == 0) goto L_0x0138;
        L_0x012e:
            r8 = r0.mIdType;
            r2.writeString(r8);
            r8 = r0.mIdPackage;
            r2.writeString(r8);
        L_0x0138:
            if (r6 == 0) goto L_0x01da;
        L_0x013a:
            r8 = r0.mSanitized;
            r1.writeInt(r8);
            r8 = r0.mImportantForAutofill;
            r1.writeInt(r8);
            r8 = r0.mSanitized;
            if (r8 != 0) goto L_0x014d;
        L_0x0148:
            if (r23 != 0) goto L_0x014b;
        L_0x014a:
            goto L_0x014d;
        L_0x014b:
            r8 = r12;
            goto L_0x014e;
        L_0x014d:
            r8 = 1;
        L_0x014e:
            r4 = r8;
            r8 = r6 & 1;
            if (r8 == 0) goto L_0x0176;
        L_0x0153:
            r8 = r0.mAutofillId;
            r8 = r8.getViewId();
            r1.writeInt(r8);
            r8 = r6 & 2;
            if (r8 == 0) goto L_0x0169;
        L_0x0160:
            r8 = r0.mAutofillId;
            r8 = r8.getVirtualChildIntId();
            r1.writeInt(r8);
        L_0x0169:
            r8 = r6 & 2048;
            if (r8 == 0) goto L_0x0176;
        L_0x016d:
            r8 = r0.mAutofillId;
            r8 = r8.getSessionId();
            r1.writeInt(r8);
        L_0x0176:
            r8 = r6 & 8;
            if (r8 == 0) goto L_0x017f;
        L_0x017a:
            r8 = r0.mAutofillType;
            r1.writeInt(r8);
        L_0x017f:
            r8 = r6 & 16;
            if (r8 == 0) goto L_0x0188;
        L_0x0183:
            r8 = r0.mAutofillHints;
            r1.writeStringArray(r8);
        L_0x0188:
            r8 = r6 & 4;
            if (r8 == 0) goto L_0x01a2;
        L_0x018c:
            if (r4 == 0) goto L_0x0191;
        L_0x018e:
            r8 = r0.mAutofillValue;
            goto L_0x019f;
        L_0x0191:
            r8 = r0.mAutofillOverlay;
            if (r8 == 0) goto L_0x019e;
        L_0x0195:
            r8 = r8.value;
            if (r8 == 0) goto L_0x019e;
        L_0x0199:
            r8 = r0.mAutofillOverlay;
            r8 = r8.value;
            goto L_0x019f;
        L_0x019e:
            r8 = 0;
        L_0x019f:
            r1.writeParcelable(r8, r12);
        L_0x01a2:
            r8 = r6 & 32;
            if (r8 == 0) goto L_0x01ab;
        L_0x01a6:
            r8 = r0.mAutofillOptions;
            r1.writeCharSequenceArray(r8);
        L_0x01ab:
            r8 = r6 & 64;
            if (r8 == 0) goto L_0x01b6;
        L_0x01af:
            r8 = r0.mHtmlInfo;
            r8 = (android.os.Parcelable) r8;
            r1.writeParcelable(r8, r12);
        L_0x01b6:
            r8 = r6 & 256;
            if (r8 == 0) goto L_0x01bf;
        L_0x01ba:
            r8 = r0.mMinEms;
            r1.writeInt(r8);
        L_0x01bf:
            r8 = r6 & 512;
            if (r8 == 0) goto L_0x01c8;
        L_0x01c3:
            r8 = r0.mMaxEms;
            r1.writeInt(r8);
        L_0x01c8:
            r8 = r6 & 1024;
            if (r8 == 0) goto L_0x01d1;
        L_0x01cc:
            r8 = r0.mMaxLength;
            r1.writeInt(r8);
        L_0x01d1:
            r8 = r6 & 128;
            if (r8 == 0) goto L_0x01da;
        L_0x01d5:
            r8 = r0.mTextIdEntry;
            r2.writeString(r8);
        L_0x01da:
            r8 = r5 & r10;
            if (r8 == 0) goto L_0x01f3;
        L_0x01de:
            r8 = r0.mX;
            r1.writeInt(r8);
            r8 = r0.mY;
            r1.writeInt(r8);
            r8 = r0.mWidth;
            r1.writeInt(r8);
            r8 = r0.mHeight;
            r1.writeInt(r8);
            goto L_0x0207;
        L_0x01f3:
            r8 = r0.mY;
            r8 = r8 << 16;
            r9 = r0.mX;
            r8 = r8 | r9;
            r1.writeInt(r8);
            r8 = r0.mHeight;
            r8 = r8 << 16;
            r9 = r0.mWidth;
            r8 = r8 | r9;
            r1.writeInt(r8);
        L_0x0207:
            r8 = r5 & r13;
            if (r8 == 0) goto L_0x0215;
        L_0x020b:
            r8 = r0.mScrollX;
            r1.writeInt(r8);
            r8 = r0.mScrollY;
            r1.writeInt(r8);
        L_0x0215:
            r8 = r5 & r14;
            if (r8 == 0) goto L_0x0221;
        L_0x0219:
            r8 = r0.mMatrix;
            r8.getValues(r3);
            r1.writeFloatArray(r3);
        L_0x0221:
            r8 = r5 & r15;
            if (r8 == 0) goto L_0x022a;
        L_0x0225:
            r8 = r0.mElevation;
            r1.writeFloat(r8);
        L_0x022a:
            r8 = r5 & r16;
            if (r8 == 0) goto L_0x0233;
        L_0x022e:
            r8 = r0.mAlpha;
            r1.writeFloat(r8);
        L_0x0233:
            r8 = r5 & r17;
            if (r8 == 0) goto L_0x023c;
        L_0x0237:
            r8 = r0.mContentDescription;
            android.text.TextUtils.writeToParcel(r8, r1, r12);
        L_0x023c:
            r8 = r5 & r18;
            if (r8 == 0) goto L_0x024d;
        L_0x0240:
            r8 = r0.mText;
            r9 = 8388608; // 0x800000 float:1.17549435E-38 double:4.144523E-317;
            r9 = r9 & r5;
            if (r9 != 0) goto L_0x0249;
        L_0x0247:
            r9 = 1;
            goto L_0x024a;
        L_0x0249:
            r9 = r12;
        L_0x024a:
            r8.writeToParcel(r1, r9, r4);
        L_0x024d:
            r8 = r5 & r19;
            if (r8 == 0) goto L_0x0256;
        L_0x0251:
            r8 = r0.mInputType;
            r1.writeInt(r8);
        L_0x0256:
            r8 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
            r8 = r8 & r5;
            if (r8 == 0) goto L_0x0265;
        L_0x025b:
            r8 = r0.mWebScheme;
            r1.writeString(r8);
            r8 = r0.mWebDomain;
            r1.writeString(r8);
        L_0x0265:
            r8 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
            r8 = r8 & r5;
            if (r8 == 0) goto L_0x026f;
        L_0x026a:
            r8 = r0.mLocaleList;
            r1.writeParcelable(r8, r12);
        L_0x026f:
            r8 = 4194304; // 0x400000 float:5.877472E-39 double:2.0722615E-317;
            r8 = r8 & r5;
            if (r8 == 0) goto L_0x0279;
        L_0x0274:
            r8 = r0.mExtras;
            r1.writeBundle(r8);
        L_0x0279:
            return r5;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.assist.AssistStructure$ViewNode.writeSelfToParcel(android.os.Parcel, android.os.PooledStringWriter, boolean, float[]):int");
        }

        public int getId() {
            return this.mId;
        }

        public String getIdPackage() {
            return this.mIdPackage;
        }

        public String getIdType() {
            return this.mIdType;
        }

        public String getIdEntry() {
            return this.mIdEntry;
        }

        public AutofillId getAutofillId() {
            return this.mAutofillId;
        }

        public int getAutofillType() {
            return this.mAutofillType;
        }

        public String[] getAutofillHints() {
            return this.mAutofillHints;
        }

        public AutofillValue getAutofillValue() {
            return this.mAutofillValue;
        }

        public void setAutofillOverlay(AutofillOverlay overlay) {
            this.mAutofillOverlay = overlay;
        }

        public CharSequence[] getAutofillOptions() {
            return this.mAutofillOptions;
        }

        public int getInputType() {
            return this.mInputType;
        }

        public boolean isSanitized() {
            return this.mSanitized;
        }

        public void updateAutofillValue(AutofillValue value) {
            this.mAutofillValue = value;
            if (value.isText()) {
                if (this.mText == null) {
                    this.mText = new ViewNodeText();
                }
                this.mText.mText = value.getTextValue();
            }
        }

        public int getLeft() {
            return this.mX;
        }

        public int getTop() {
            return this.mY;
        }

        public int getScrollX() {
            return this.mScrollX;
        }

        public int getScrollY() {
            return this.mScrollY;
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public Matrix getTransformation() {
            return this.mMatrix;
        }

        public float getElevation() {
            return this.mElevation;
        }

        public float getAlpha() {
            return this.mAlpha;
        }

        public int getVisibility() {
            return this.mFlags & 12;
        }

        public boolean isAssistBlocked() {
            return (this.mFlags & 128) != 0;
        }

        public boolean isEnabled() {
            return (this.mFlags & 1) == 0;
        }

        public boolean isClickable() {
            return (this.mFlags & 1024) != 0;
        }

        public boolean isFocusable() {
            return (this.mFlags & 16) != 0;
        }

        public boolean isFocused() {
            return (this.mFlags & 32) != 0;
        }

        public boolean isAccessibilityFocused() {
            return (this.mFlags & 4096) != 0;
        }

        public boolean isCheckable() {
            return (this.mFlags & 256) != 0;
        }

        public boolean isChecked() {
            return (this.mFlags & 512) != 0;
        }

        public boolean isSelected() {
            return (this.mFlags & 64) != 0;
        }

        public boolean isActivated() {
            return (this.mFlags & 8192) != 0;
        }

        public boolean isOpaque() {
            return (this.mFlags & 32768) != 0;
        }

        public boolean isLongClickable() {
            return (this.mFlags & 2048) != 0;
        }

        public boolean isContextClickable() {
            return (this.mFlags & 16384) != 0;
        }

        public String getClassName() {
            return this.mClassName;
        }

        public CharSequence getContentDescription() {
            return this.mContentDescription;
        }

        public String getWebDomain() {
            return this.mWebDomain;
        }

        public void setWebDomain(String domain) {
            if (domain != null) {
                Uri uri = Uri.parse(domain);
                if (uri == null) {
                    Log.w(AssistStructure.TAG, "Failed to parse web domain");
                    return;
                }
                this.mWebScheme = uri.getScheme();
                this.mWebDomain = uri.getHost();
            }
        }

        public String getWebScheme() {
            return this.mWebScheme;
        }

        public HtmlInfo getHtmlInfo() {
            return this.mHtmlInfo;
        }

        public LocaleList getLocaleList() {
            return this.mLocaleList;
        }

        public CharSequence getText() {
            ViewNodeText viewNodeText = this.mText;
            return viewNodeText != null ? viewNodeText.mText : null;
        }

        public int getTextSelectionStart() {
            ViewNodeText viewNodeText = this.mText;
            return viewNodeText != null ? viewNodeText.mTextSelectionStart : -1;
        }

        public int getTextSelectionEnd() {
            ViewNodeText viewNodeText = this.mText;
            return viewNodeText != null ? viewNodeText.mTextSelectionEnd : -1;
        }

        public int getTextColor() {
            ViewNodeText viewNodeText = this.mText;
            return viewNodeText != null ? viewNodeText.mTextColor : 1;
        }

        public int getTextBackgroundColor() {
            ViewNodeText viewNodeText = this.mText;
            return viewNodeText != null ? viewNodeText.mTextBackgroundColor : 1;
        }

        public float getTextSize() {
            ViewNodeText viewNodeText = this.mText;
            return viewNodeText != null ? viewNodeText.mTextSize : 0.0f;
        }

        public int getTextStyle() {
            ViewNodeText viewNodeText = this.mText;
            return viewNodeText != null ? viewNodeText.mTextStyle : 0;
        }

        public int[] getTextLineCharOffsets() {
            ViewNodeText viewNodeText = this.mText;
            return viewNodeText != null ? viewNodeText.mLineCharOffsets : null;
        }

        public int[] getTextLineBaselines() {
            ViewNodeText viewNodeText = this.mText;
            return viewNodeText != null ? viewNodeText.mLineBaselines : null;
        }

        public String getTextIdEntry() {
            return this.mTextIdEntry;
        }

        public String getHint() {
            ViewNodeText viewNodeText = this.mText;
            return viewNodeText != null ? viewNodeText.mHint : null;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public int getChildCount() {
            ViewNode[] viewNodeArr = this.mChildren;
            return viewNodeArr != null ? viewNodeArr.length : 0;
        }

        public ViewNode getChildAt(int index) {
            return this.mChildren[index];
        }

        public int getMinTextEms() {
            return this.mMinEms;
        }

        public int getMaxTextEms() {
            return this.mMaxEms;
        }

        public int getMaxTextLength() {
            return this.mMaxLength;
        }

        public int getImportantForAutofill() {
            return this.mImportantForAutofill;
        }
    }

    static class ViewNodeBuilder extends ViewStructure {
        final AssistStructure mAssist;
        final boolean mAsync;
        final ViewNode mNode;

        ViewNodeBuilder(AssistStructure assist, ViewNode node, boolean async) {
            this.mAssist = assist;
            this.mNode = node;
            this.mAsync = async;
        }

        public void setId(int id, String packageName, String typeName, String entryName) {
            ViewNode viewNode = this.mNode;
            viewNode.mId = id;
            viewNode.mIdPackage = packageName;
            viewNode.mIdType = typeName;
            viewNode.mIdEntry = entryName;
        }

        public void setDimens(int left, int top, int scrollX, int scrollY, int width, int height) {
            ViewNode viewNode = this.mNode;
            viewNode.mX = left;
            viewNode.mY = top;
            viewNode.mScrollX = scrollX;
            viewNode.mScrollY = scrollY;
            viewNode.mWidth = width;
            viewNode.mHeight = height;
        }

        public void setTransformation(Matrix matrix) {
            if (matrix == null) {
                this.mNode.mMatrix = null;
                return;
            }
            this.mNode.mMatrix = new Matrix(matrix);
        }

        public void setElevation(float elevation) {
            this.mNode.mElevation = elevation;
        }

        public void setAlpha(float alpha) {
            this.mNode.mAlpha = alpha;
        }

        public void setVisibility(int visibility) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -13) | (visibility & 12);
        }

        public void setAssistBlocked(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -129) | (state ? 128 : 0);
        }

        public void setEnabled(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -2) | (state ^ 1);
        }

        public void setClickable(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -1025) | (state ? 1024 : 0);
        }

        public void setLongClickable(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -2049) | (state ? 2048 : 0);
        }

        public void setContextClickable(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -16385) | (state ? 16384 : 0);
        }

        public void setFocusable(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -17) | (state ? 16 : 0);
        }

        public void setFocused(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -33) | (state ? 32 : 0);
        }

        public void setAccessibilityFocused(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -4097) | (state ? 4096 : 0);
        }

        public void setCheckable(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & TrafficStats.TAG_NETWORK_STACK_RANGE_END) | (state ? 256 : 0);
        }

        public void setChecked(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -513) | (state ? 512 : 0);
        }

        public void setSelected(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -65) | (state ? 64 : 0);
        }

        public void setActivated(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -8193) | (state ? 8192 : 0);
        }

        public void setOpaque(boolean opaque) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -32769) | (opaque ? 32768 : 0);
        }

        public void setClassName(String className) {
            this.mNode.mClassName = className;
        }

        public void setContentDescription(CharSequence contentDescription) {
            this.mNode.mContentDescription = contentDescription;
        }

        private final ViewNodeText getNodeText() {
            if (this.mNode.mText != null) {
                return this.mNode.mText;
            }
            this.mNode.mText = new ViewNodeText();
            return this.mNode.mText;
        }

        public void setText(CharSequence text) {
            ViewNodeText t = getNodeText();
            t.mText = TextUtils.trimNoCopySpans(text);
            t.mTextSelectionEnd = -1;
            t.mTextSelectionStart = -1;
        }

        public void setText(CharSequence text, int selectionStart, int selectionEnd) {
            ViewNodeText t = getNodeText();
            t.mText = TextUtils.trimNoCopySpans(text);
            t.mTextSelectionStart = selectionStart;
            t.mTextSelectionEnd = selectionEnd;
        }

        public void setTextStyle(float size, int fgColor, int bgColor, int style) {
            ViewNodeText t = getNodeText();
            t.mTextColor = fgColor;
            t.mTextBackgroundColor = bgColor;
            t.mTextSize = size;
            t.mTextStyle = style;
        }

        public void setTextLines(int[] charOffsets, int[] baselines) {
            ViewNodeText t = getNodeText();
            t.mLineCharOffsets = charOffsets;
            t.mLineBaselines = baselines;
        }

        public void setTextIdEntry(String entryName) {
            this.mNode.mTextIdEntry = (String) Preconditions.checkNotNull(entryName);
        }

        public void setHint(CharSequence hint) {
            getNodeText().mHint = hint != null ? hint.toString() : null;
        }

        public CharSequence getText() {
            return this.mNode.mText != null ? this.mNode.mText.mText : null;
        }

        public int getTextSelectionStart() {
            return this.mNode.mText != null ? this.mNode.mText.mTextSelectionStart : -1;
        }

        public int getTextSelectionEnd() {
            return this.mNode.mText != null ? this.mNode.mText.mTextSelectionEnd : -1;
        }

        public CharSequence getHint() {
            return this.mNode.mText != null ? this.mNode.mText.mHint : null;
        }

        public Bundle getExtras() {
            if (this.mNode.mExtras != null) {
                return this.mNode.mExtras;
            }
            this.mNode.mExtras = new Bundle();
            return this.mNode.mExtras;
        }

        public boolean hasExtras() {
            return this.mNode.mExtras != null;
        }

        public void setChildCount(int num) {
            this.mNode.mChildren = new ViewNode[num];
        }

        public int addChildCount(int num) {
            if (this.mNode.mChildren == null) {
                setChildCount(num);
                return 0;
            }
            int start = this.mNode.mChildren.length;
            ViewNode[] newArray = new ViewNode[(start + num)];
            System.arraycopy(this.mNode.mChildren, 0, newArray, 0, start);
            this.mNode.mChildren = newArray;
            return start;
        }

        public int getChildCount() {
            return this.mNode.mChildren != null ? this.mNode.mChildren.length : 0;
        }

        public ViewStructure newChild(int index) {
            ViewNode node = new ViewNode();
            this.mNode.mChildren[index] = node;
            return new ViewNodeBuilder(this.mAssist, node, false);
        }

        public ViewStructure asyncNewChild(int index) {
            ViewNodeBuilder builder;
            synchronized (this.mAssist) {
                ViewNode node = new ViewNode();
                this.mNode.mChildren[index] = node;
                builder = new ViewNodeBuilder(this.mAssist, node, true);
                this.mAssist.mPendingAsyncChildren.add(builder);
            }
            return builder;
        }

        public void asyncCommit() {
            synchronized (this.mAssist) {
                StringBuilder stringBuilder;
                if (!this.mAsync) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Child ");
                    stringBuilder.append(this);
                    stringBuilder.append(" was not created with ViewStructure.asyncNewChild");
                    throw new IllegalStateException(stringBuilder.toString());
                } else if (this.mAssist.mPendingAsyncChildren.remove(this)) {
                    this.mAssist.notifyAll();
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Child ");
                    stringBuilder.append(this);
                    stringBuilder.append(" already committed");
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
        }

        public Rect getTempRect() {
            return this.mAssist.mTmpRect;
        }

        public void setAutofillId(AutofillId id) {
            this.mNode.mAutofillId = id;
        }

        public void setAutofillId(AutofillId parentId, int virtualId) {
            this.mNode.mAutofillId = new AutofillId(parentId, virtualId);
        }

        public AutofillId getAutofillId() {
            return this.mNode.mAutofillId;
        }

        public void setAutofillType(int type) {
            this.mNode.mAutofillType = type;
        }

        public void setAutofillHints(String[] hints) {
            this.mNode.mAutofillHints = hints;
        }

        public void setAutofillValue(AutofillValue value) {
            this.mNode.mAutofillValue = value;
        }

        public void setAutofillOptions(CharSequence[] options) {
            this.mNode.mAutofillOptions = options;
        }

        public void setImportantForAutofill(int mode) {
            this.mNode.mImportantForAutofill = mode;
        }

        public void setInputType(int inputType) {
            this.mNode.mInputType = inputType;
        }

        public void setMinTextEms(int minEms) {
            this.mNode.mMinEms = minEms;
        }

        public void setMaxTextEms(int maxEms) {
            this.mNode.mMaxEms = maxEms;
        }

        public void setMaxTextLength(int maxLength) {
            this.mNode.mMaxLength = maxLength;
        }

        public void setDataIsSensitive(boolean sensitive) {
            this.mNode.mSanitized = sensitive ^ 1;
        }

        public void setWebDomain(String domain) {
            this.mNode.setWebDomain(domain);
        }

        public void setLocaleList(LocaleList localeList) {
            this.mNode.mLocaleList = localeList;
        }

        public Builder newHtmlInfoBuilder(String tagName) {
            return new HtmlInfoNodeBuilder(tagName);
        }

        public void setHtmlInfo(HtmlInfo htmlInfo) {
            this.mNode.mHtmlInfo = htmlInfo;
        }
    }

    static final class ViewNodeText {
        String mHint;
        int[] mLineBaselines;
        int[] mLineCharOffsets;
        CharSequence mText;
        int mTextBackgroundColor = 1;
        int mTextColor = 1;
        int mTextSelectionEnd;
        int mTextSelectionStart;
        float mTextSize;
        int mTextStyle;

        ViewNodeText() {
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isSimple() {
            return this.mTextBackgroundColor == 1 && this.mTextSelectionStart == 0 && this.mTextSelectionEnd == 0 && this.mLineCharOffsets == null && this.mLineBaselines == null && this.mHint == null;
        }

        ViewNodeText(Parcel in, boolean simple) {
            this.mText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.mTextSize = in.readFloat();
            this.mTextStyle = in.readInt();
            this.mTextColor = in.readInt();
            if (!simple) {
                this.mTextBackgroundColor = in.readInt();
                this.mTextSelectionStart = in.readInt();
                this.mTextSelectionEnd = in.readInt();
                this.mLineCharOffsets = in.createIntArray();
                this.mLineBaselines = in.createIntArray();
                this.mHint = in.readString();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void writeToParcel(Parcel out, boolean simple, boolean writeSensitive) {
            TextUtils.writeToParcel(writeSensitive ? this.mText : "", out, 0);
            out.writeFloat(this.mTextSize);
            out.writeInt(this.mTextStyle);
            out.writeInt(this.mTextColor);
            if (!simple) {
                out.writeInt(this.mTextBackgroundColor);
                out.writeInt(this.mTextSelectionStart);
                out.writeInt(this.mTextSelectionEnd);
                out.writeIntArray(this.mLineCharOffsets);
                out.writeIntArray(this.mLineBaselines);
                out.writeString(this.mHint);
            }
        }
    }

    static final class ViewStackEntry {
        int curChild;
        ViewNode node;
        int numChildren;

        ViewStackEntry() {
        }
    }

    public static class WindowNode {
        final int mDisplayId;
        final int mHeight;
        final ViewNode mRoot;
        final CharSequence mTitle;
        final int mWidth;
        final int mX;
        final int mY;

        WindowNode(AssistStructure assist, ViewRootImpl root, boolean forAutoFill, int flags) {
            View view = root.getView();
            Rect rect = new Rect();
            view.getBoundsOnScreen(rect);
            this.mX = rect.left - view.getLeft();
            this.mY = rect.top - view.getTop();
            this.mWidth = rect.width();
            this.mHeight = rect.height();
            this.mTitle = root.getTitle();
            this.mDisplayId = root.getDisplayId();
            this.mRoot = new ViewNode();
            ViewNodeBuilder builder = new ViewNodeBuilder(assist, this.mRoot, false);
            if ((root.getWindowFlags() & 8192) != 0) {
                if (forAutoFill) {
                    view.onProvideAutofillStructure(builder, resolveViewAutofillFlags(view.getContext(), flags));
                } else {
                    view.onProvideStructure(builder);
                    builder.setAssistBlocked(true);
                    return;
                }
            }
            if (forAutoFill) {
                view.dispatchProvideAutofillStructure(builder, resolveViewAutofillFlags(view.getContext(), flags));
            } else {
                view.dispatchProvideStructure(builder);
            }
        }

        WindowNode(ParcelTransferReader reader) {
            Parcel in = reader.readParcel(AssistStructure.VALIDATE_WINDOW_TOKEN, 0);
            reader.mNumReadWindows++;
            this.mX = in.readInt();
            this.mY = in.readInt();
            this.mWidth = in.readInt();
            this.mHeight = in.readInt();
            this.mTitle = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.mDisplayId = in.readInt();
            this.mRoot = new ViewNode(reader, 0);
        }

        /* Access modifiers changed, original: 0000 */
        public int resolveViewAutofillFlags(Context context, int fillRequestFlags) {
            return ((fillRequestFlags & 1) != 0 || context.isAutofillCompatibilityEnabled()) ? 1 : 0;
        }

        /* Access modifiers changed, original: 0000 */
        public void writeSelfToParcel(Parcel out, PooledStringWriter pwriter, float[] tmpMatrix) {
            out.writeInt(this.mX);
            out.writeInt(this.mY);
            out.writeInt(this.mWidth);
            out.writeInt(this.mHeight);
            TextUtils.writeToParcel(this.mTitle, out, 0);
            out.writeInt(this.mDisplayId);
        }

        public int getLeft() {
            return this.mX;
        }

        public int getTop() {
            return this.mY;
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public CharSequence getTitle() {
            return this.mTitle;
        }

        public int getDisplayId() {
            return this.mDisplayId;
        }

        public ViewNode getRootViewNode() {
            return this.mRoot;
        }
    }

    public void setAcquisitionStartTime(long acquisitionStartTime) {
        this.mAcquisitionStartTime = acquisitionStartTime;
    }

    public void setAcquisitionEndTime(long acquisitionEndTime) {
        this.mAcquisitionEndTime = acquisitionEndTime;
    }

    public void setHomeActivity(boolean isHomeActivity) {
        this.mIsHomeActivity = isHomeActivity;
    }

    public long getAcquisitionStartTime() {
        ensureData();
        return this.mAcquisitionStartTime;
    }

    public long getAcquisitionEndTime() {
        ensureData();
        return this.mAcquisitionEndTime;
    }

    public AssistStructure(Activity activity, boolean forAutoFill, int flags) {
        this.mWindowNodes = new ArrayList();
        this.mPendingAsyncChildren = new ArrayList();
        this.mTmpRect = new Rect();
        this.mSanitizeOnWrite = false;
        this.mHaveData = true;
        this.mFlags = flags;
        ArrayList<ViewRootImpl> views = WindowManagerGlobal.getInstance().getRootViews(activity.getActivityToken());
        for (int i = 0; i < views.size(); i++) {
            ViewRootImpl root = (ViewRootImpl) views.get(i);
            if (root.getView() == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Skipping window with dettached view: ");
                stringBuilder.append(root.getTitle());
                Log.w(TAG, stringBuilder.toString());
            } else {
                this.mWindowNodes.add(new WindowNode(this, root, forAutoFill, flags));
            }
        }
    }

    public AssistStructure() {
        this.mWindowNodes = new ArrayList();
        this.mPendingAsyncChildren = new ArrayList();
        this.mTmpRect = new Rect();
        this.mSanitizeOnWrite = false;
        this.mHaveData = true;
        this.mFlags = 0;
    }

    public AssistStructure(Parcel in) {
        this.mWindowNodes = new ArrayList();
        this.mPendingAsyncChildren = new ArrayList();
        this.mTmpRect = new Rect();
        boolean z = false;
        this.mSanitizeOnWrite = false;
        this.mTaskId = in.readInt();
        this.mActivityComponent = ComponentName.readFromParcel(in);
        if (in.readInt() == 1) {
            z = true;
        }
        this.mIsHomeActivity = z;
        this.mReceiveChannel = in.readStrongBinder();
    }

    public void sanitizeForParceling(boolean sanitize) {
        this.mSanitizeOnWrite = sanitize;
    }

    public void dump(boolean showSensitive) {
        String flattenToShortString;
        ComponentName componentName = this.mActivityComponent;
        String str = TAG;
        if (componentName == null) {
            Log.i(str, "dump(): calling ensureData() first");
            ensureData();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Task id: ");
        stringBuilder.append(this.mTaskId);
        Log.i(str, stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("Activity: ");
        ComponentName componentName2 = this.mActivityComponent;
        if (componentName2 != null) {
            flattenToShortString = componentName2.flattenToShortString();
        } else {
            flattenToShortString = null;
        }
        stringBuilder.append(flattenToShortString);
        Log.i(str, stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("Sanitize on write: ");
        stringBuilder.append(this.mSanitizeOnWrite);
        Log.i(str, stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("Flags: ");
        stringBuilder.append(this.mFlags);
        Log.i(str, stringBuilder.toString());
        int N = getWindowNodeCount();
        for (int i = 0; i < N; i++) {
            WindowNode node = getWindowNodeAt(i);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Window #");
            stringBuilder2.append(i);
            stringBuilder2.append(" [");
            stringBuilder2.append(node.getLeft());
            stringBuilder2.append(",");
            stringBuilder2.append(node.getTop());
            stringBuilder2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder2.append(node.getWidth());
            stringBuilder2.append("x");
            stringBuilder2.append(node.getHeight());
            stringBuilder2.append("] ");
            stringBuilder2.append(node.getTitle());
            Log.i(str, stringBuilder2.toString());
            dump("  ", node.getRootViewNode(), showSensitive);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dump(String prefix, ViewNode node, boolean showSensitive) {
        StringBuilder stringBuilder;
        String safeText;
        StringBuilder stringBuilder2;
        StringBuilder stringBuilder3;
        String str = prefix;
        boolean z = showSensitive;
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append(str);
        stringBuilder4.append("View [");
        stringBuilder4.append(node.getLeft());
        String str2 = ",";
        stringBuilder4.append(str2);
        stringBuilder4.append(node.getTop());
        String str3 = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        stringBuilder4.append(str3);
        stringBuilder4.append(node.getWidth());
        stringBuilder4.append("x");
        stringBuilder4.append(node.getHeight());
        stringBuilder4.append("] ");
        stringBuilder4.append(node.getClassName());
        String stringBuilder5 = stringBuilder4.toString();
        String str4 = TAG;
        Log.i(str4, stringBuilder5);
        int id = node.getId();
        if (id != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("  ID: #");
            sb.append(Integer.toHexString(id));
            String entry = node.getIdEntry();
            if (entry != null) {
                String type = node.getIdType();
                String pkg = node.getIdPackage();
                sb.append(str3);
                sb.append(pkg);
                sb.append(":");
                sb.append(type);
                sb.append("/");
                sb.append(entry);
            }
            Log.i(str4, sb.toString());
        }
        int scrollX = node.getScrollX();
        int scrollY = node.getScrollY();
        if (!(scrollX == 0 && scrollY == 0)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append("  Scroll: ");
            stringBuilder.append(scrollX);
            stringBuilder.append(str2);
            stringBuilder.append(scrollY);
            Log.i(str4, stringBuilder.toString());
        }
        Matrix matrix = node.getTransformation();
        if (matrix != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append("  Transformation: ");
            stringBuilder.append(matrix);
            Log.i(str4, stringBuilder.toString());
        }
        float elevation = node.getElevation();
        if (elevation != 0.0f) {
            StringBuilder stringBuilder6 = new StringBuilder();
            stringBuilder6.append(str);
            stringBuilder6.append("  Elevation: ");
            stringBuilder6.append(elevation);
            Log.i(str4, stringBuilder6.toString());
        }
        if (node.getAlpha() != 0.0f) {
            StringBuilder stringBuilder7 = new StringBuilder();
            stringBuilder7.append(str);
            stringBuilder7.append("  Alpha: ");
            stringBuilder7.append(elevation);
            Log.i(str4, stringBuilder7.toString());
        }
        CharSequence contentDescription = node.getContentDescription();
        if (contentDescription != null) {
            StringBuilder stringBuilder8 = new StringBuilder();
            stringBuilder8.append(str);
            stringBuilder8.append("  Content description: ");
            stringBuilder8.append(contentDescription);
            Log.i(str4, stringBuilder8.toString());
        }
        CharSequence text = node.getText();
        if (text != null) {
            if (node.isSanitized() || z) {
                safeText = text.toString();
            } else {
                StringBuilder stringBuilder9 = new StringBuilder();
                stringBuilder9.append("REDACTED[");
                stringBuilder9.append(text.length());
                stringBuilder9.append(" chars]");
                safeText = stringBuilder9.toString();
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append("  Text (sel ");
            stringBuilder2.append(node.getTextSelectionStart());
            stringBuilder2.append("-");
            stringBuilder2.append(node.getTextSelectionEnd());
            stringBuilder2.append("): ");
            stringBuilder2.append(safeText);
            Log.i(str4, stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append("  Text size: ");
            stringBuilder2.append(node.getTextSize());
            stringBuilder2.append(" , style: #");
            stringBuilder2.append(node.getTextStyle());
            Log.i(str4, stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append("  Text color fg: #");
            stringBuilder2.append(Integer.toHexString(node.getTextColor()));
            stringBuilder2.append(", bg: #");
            stringBuilder2.append(Integer.toHexString(node.getTextBackgroundColor()));
            Log.i(str4, stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append("  Input type: ");
            stringBuilder2.append(node.getInputType());
            Log.i(str4, stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append("  Resource id: ");
            stringBuilder2.append(node.getTextIdEntry());
            Log.i(str4, stringBuilder2.toString());
        }
        safeText = node.getWebDomain();
        if (safeText != null) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append("  Web domain: ");
            stringBuilder2.append(safeText);
            Log.i(str4, stringBuilder2.toString());
        }
        HtmlInfo htmlInfo = node.getHtmlInfo();
        if (htmlInfo != null) {
            StringBuilder stringBuilder10 = new StringBuilder();
            stringBuilder10.append(str);
            stringBuilder10.append("  HtmlInfo: tag=");
            stringBuilder10.append(htmlInfo.getTag());
            stringBuilder10.append(", attr=");
            stringBuilder10.append(htmlInfo.getAttributes());
            Log.i(str4, stringBuilder10.toString());
        }
        LocaleList localeList = node.getLocaleList();
        if (localeList != null) {
            StringBuilder stringBuilder11 = new StringBuilder();
            stringBuilder11.append(str);
            stringBuilder11.append("  LocaleList: ");
            stringBuilder11.append(localeList);
            Log.i(str4, stringBuilder11.toString());
        }
        String hint = node.getHint();
        if (hint != null) {
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append(str);
            stringBuilder3.append("  Hint: ");
            stringBuilder3.append(hint);
            Log.i(str4, stringBuilder3.toString());
        }
        Bundle extras = node.getExtras();
        if (extras != null) {
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append(str);
            stringBuilder3.append("  Extras: ");
            stringBuilder3.append(extras);
            Log.i(str4, stringBuilder3.toString());
        }
        if (node.isAssistBlocked()) {
            StringBuilder stringBuilder12 = new StringBuilder();
            stringBuilder12.append(str);
            stringBuilder12.append("  BLOCKED");
            Log.i(str4, stringBuilder12.toString());
        }
        AutofillId autofillId = node.getAutofillId();
        if (autofillId == null) {
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append(str);
            stringBuilder3.append(" NO autofill ID");
            Log.i(str4, stringBuilder3.toString());
        } else {
            stringBuilder4 = new StringBuilder();
            stringBuilder4.append(str);
            stringBuilder4.append("  Autofill info: id= ");
            stringBuilder4.append(autofillId);
            stringBuilder4.append(", type=");
            stringBuilder4.append(node.getAutofillType());
            stringBuilder4.append(", options=");
            stringBuilder4.append(Arrays.toString(node.getAutofillOptions()));
            stringBuilder4.append(", hints=");
            stringBuilder4.append(Arrays.toString(node.getAutofillHints()));
            stringBuilder4.append(", value=");
            stringBuilder4.append(node.getAutofillValue());
            stringBuilder4.append(", sanitized=");
            stringBuilder4.append(node.isSanitized());
            stringBuilder4.append(", important=");
            stringBuilder4.append(node.getImportantForAutofill());
            Log.i(str4, stringBuilder4.toString());
        }
        id = node.getChildCount();
        int NCHILDREN;
        ViewNode viewNode;
        if (id > 0) {
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append(str);
            stringBuilder3.append("  Children:");
            Log.i(str4, stringBuilder3.toString());
            autofillId = new StringBuilder();
            autofillId.append(str);
            autofillId.append("    ");
            autofillId = autofillId.toString();
            int i = 0;
            while (i < id) {
                NCHILDREN = id;
                dump(autofillId, node.getChildAt(i), z);
                i++;
                str = prefix;
                id = NCHILDREN;
            }
            viewNode = node;
            NCHILDREN = id;
            return;
        }
        viewNode = node;
        NCHILDREN = id;
        AutofillId autofillId2 = autofillId;
    }

    public void setTaskId(int taskId) {
        this.mTaskId = taskId;
    }

    public int getTaskId() {
        return this.mTaskId;
    }

    public void setActivityComponent(ComponentName componentName) {
        this.mActivityComponent = componentName;
    }

    public ComponentName getActivityComponent() {
        return this.mActivityComponent;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public boolean isHomeActivity() {
        return this.mIsHomeActivity;
    }

    public int getWindowNodeCount() {
        ensureData();
        return this.mWindowNodes.size();
    }

    public WindowNode getWindowNodeAt(int index) {
        ensureData();
        return (WindowNode) this.mWindowNodes.get(index);
    }

    public void ensureDataForAutofill() {
        if (!this.mHaveData) {
            this.mHaveData = true;
            Binder.allowBlocking(this.mReceiveChannel);
            try {
                new ParcelTransferReader(this.mReceiveChannel).go();
            } finally {
                Binder.defaultBlocking(this.mReceiveChannel);
            }
        }
    }

    public void ensureData() {
        if (!this.mHaveData) {
            this.mHaveData = true;
            new ParcelTransferReader(this.mReceiveChannel).go();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean waitForReady() {
        boolean skipStructure = false;
        synchronized (this) {
            long endTime = SystemClock.uptimeMillis() + TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS;
            while (this.mPendingAsyncChildren.size() > 0) {
                long uptimeMillis = SystemClock.uptimeMillis();
                long now = uptimeMillis;
                if (uptimeMillis < endTime) {
                    try {
                        wait(endTime - now);
                    } catch (InterruptedException e) {
                    }
                }
            }
            if (this.mPendingAsyncChildren.size() > 0) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Skipping assist structure, waiting too long for async children (have ");
                stringBuilder.append(this.mPendingAsyncChildren.size());
                stringBuilder.append(" remaining");
                Log.w(str, stringBuilder.toString());
                skipStructure = true;
            }
        }
        return !skipStructure;
    }

    public void clearSendChannel() {
        SendChannel sendChannel = this.mSendChannel;
        if (sendChannel != null) {
            sendChannel.mAssistStructure = null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mTaskId);
        ComponentName.writeToParcel(this.mActivityComponent, out);
        out.writeInt(this.mIsHomeActivity);
        if (this.mHaveData) {
            if (this.mSendChannel == null) {
                this.mSendChannel = new SendChannel(this);
            }
            out.writeStrongBinder(this.mSendChannel);
            return;
        }
        out.writeStrongBinder(this.mReceiveChannel);
    }
}
