package android.view.contentcapture;

import android.annotation.SystemApi;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewStructure;
import android.view.ViewStructure.HtmlInfo;
import android.view.ViewStructure.HtmlInfo.Builder;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import com.android.internal.util.Preconditions;

@SystemApi
public final class ViewNode extends android.app.assist.AssistStructure.ViewNode {
    private static final long FLAGS_ACCESSIBILITY_FOCUSED = 131072;
    private static final long FLAGS_ACTIVATED = 2097152;
    private static final long FLAGS_ASSIST_BLOCKED = 1024;
    private static final long FLAGS_CHECKABLE = 262144;
    private static final long FLAGS_CHECKED = 524288;
    private static final long FLAGS_CLICKABLE = 4096;
    private static final long FLAGS_CONTEXT_CLICKABLE = 16384;
    private static final long FLAGS_DISABLED = 2048;
    private static final long FLAGS_FOCUSABLE = 32768;
    private static final long FLAGS_FOCUSED = 65536;
    private static final long FLAGS_HAS_AUTOFILL_HINTS = 8589934592L;
    private static final long FLAGS_HAS_AUTOFILL_ID = 32;
    private static final long FLAGS_HAS_AUTOFILL_OPTIONS = 17179869184L;
    private static final long FLAGS_HAS_AUTOFILL_PARENT_ID = 64;
    private static final long FLAGS_HAS_AUTOFILL_TYPE = 2147483648L;
    private static final long FLAGS_HAS_AUTOFILL_VALUE = 4294967296L;
    private static final long FLAGS_HAS_CLASSNAME = 16;
    private static final long FLAGS_HAS_COMPLEX_TEXT = 2;
    private static final long FLAGS_HAS_CONTENT_DESCRIPTION = 8388608;
    private static final long FLAGS_HAS_EXTRAS = 16777216;
    private static final long FLAGS_HAS_ID = 128;
    private static final long FLAGS_HAS_INPUT_TYPE = 67108864;
    private static final long FLAGS_HAS_LARGE_COORDS = 256;
    private static final long FLAGS_HAS_LOCALE_LIST = 33554432;
    private static final long FLAGS_HAS_MAX_TEXT_EMS = 268435456;
    private static final long FLAGS_HAS_MAX_TEXT_LENGTH = 536870912;
    private static final long FLAGS_HAS_MIN_TEXT_EMS = 134217728;
    private static final long FLAGS_HAS_SCROLL = 512;
    private static final long FLAGS_HAS_TEXT = 1;
    private static final long FLAGS_HAS_TEXT_ID_ENTRY = 1073741824;
    private static final long FLAGS_LONG_CLICKABLE = 8192;
    private static final long FLAGS_OPAQUE = 4194304;
    private static final long FLAGS_SELECTED = 1048576;
    private static final long FLAGS_VISIBILITY_MASK = 12;
    private static final String TAG = ViewNode.class.getSimpleName();
    private String[] mAutofillHints;
    private AutofillId mAutofillId;
    private CharSequence[] mAutofillOptions;
    private int mAutofillType;
    private AutofillValue mAutofillValue;
    private String mClassName;
    private CharSequence mContentDescription;
    private Bundle mExtras;
    private long mFlags;
    private int mHeight;
    private int mId;
    private String mIdEntry;
    private String mIdPackage;
    private String mIdType;
    private int mInputType;
    private LocaleList mLocaleList;
    private int mMaxEms;
    private int mMaxLength;
    private int mMinEms;
    private AutofillId mParentAutofillId;
    private int mScrollX;
    private int mScrollY;
    private ViewNodeText mText;
    private String mTextIdEntry;
    private int mWidth;
    private int mX;
    private int mY;

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
        public void writeToParcel(Parcel out, boolean simple) {
            TextUtils.writeToParcel(this.mText, out, 0);
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

    public static final class ViewStructureImpl extends ViewStructure {
        final ViewNode mNode = new ViewNode();

        public ViewStructureImpl(View view) {
            this.mNode.mAutofillId = ((View) Preconditions.checkNotNull(view)).getAutofillId();
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                this.mNode.mParentAutofillId = ((View) parent).getAutofillId();
            }
        }

        public ViewStructureImpl(AutofillId parentId, long virtualId, int sessionId) {
            this.mNode.mParentAutofillId = (AutofillId) Preconditions.checkNotNull(parentId);
            this.mNode.mAutofillId = new AutofillId(parentId, virtualId, sessionId);
        }

        public ViewNode getNode() {
            return this.mNode;
        }

        public void setId(int id, String packageName, String typeName, String entryName) {
            this.mNode.mId = id;
            this.mNode.mIdPackage = packageName;
            this.mNode.mIdType = typeName;
            this.mNode.mIdEntry = entryName;
        }

        public void setDimens(int left, int top, int scrollX, int scrollY, int width, int height) {
            this.mNode.mX = left;
            this.mNode.mY = top;
            this.mNode.mScrollX = scrollX;
            this.mNode.mScrollY = scrollY;
            this.mNode.mWidth = width;
            this.mNode.mHeight = height;
        }

        public void setTransformation(Matrix matrix) {
            Log.w(ViewNode.TAG, "setTransformation() is not supported");
        }

        public void setElevation(float elevation) {
            Log.w(ViewNode.TAG, "setElevation() is not supported");
        }

        public void setAlpha(float alpha) {
            Log.w(ViewNode.TAG, "setAlpha() is not supported");
        }

        public void setVisibility(int visibility) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -13) | (((long) visibility) & ViewNode.FLAGS_VISIBILITY_MASK);
        }

        public void setAssistBlocked(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -1025) | (state ? 1024 : 0);
        }

        public void setEnabled(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -2049) | (state ? 0 : 2048);
        }

        public void setClickable(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -4097) | (state ? 4096 : 0);
        }

        public void setLongClickable(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -8193) | (state ? 8192 : 0);
        }

        public void setContextClickable(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -16385) | (state ? 16384 : 0);
        }

        public void setFocusable(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -32769) | (state ? 32768 : 0);
        }

        public void setFocused(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -65537) | (state ? 65536 : 0);
        }

        public void setAccessibilityFocused(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -131073) | (state ? 131072 : 0);
        }

        public void setCheckable(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -262145) | (state ? 262144 : 0);
        }

        public void setChecked(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -524289) | (state ? 524288 : 0);
        }

        public void setSelected(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -1048577) | (state ? 1048576 : 0);
        }

        public void setActivated(boolean state) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -2097153) | (state ? 2097152 : 0);
        }

        public void setOpaque(boolean opaque) {
            ViewNode viewNode = this.mNode;
            viewNode.mFlags = (viewNode.mFlags & -4194305) | (opaque ? 4194304 : 0);
        }

        public void setClassName(String className) {
            this.mNode.mClassName = className;
        }

        public void setContentDescription(CharSequence contentDescription) {
            this.mNode.mContentDescription = contentDescription;
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
            return this.mNode.getText();
        }

        public int getTextSelectionStart() {
            return this.mNode.getTextSelectionStart();
        }

        public int getTextSelectionEnd() {
            return this.mNode.getTextSelectionEnd();
        }

        public CharSequence getHint() {
            return this.mNode.getHint();
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
            Log.w(ViewNode.TAG, "setChildCount() is not supported");
        }

        public int addChildCount(int num) {
            Log.w(ViewNode.TAG, "addChildCount() is not supported");
            return 0;
        }

        public int getChildCount() {
            Log.w(ViewNode.TAG, "getChildCount() is not supported");
            return 0;
        }

        public ViewStructure newChild(int index) {
            Log.w(ViewNode.TAG, "newChild() is not supported");
            return null;
        }

        public ViewStructure asyncNewChild(int index) {
            Log.w(ViewNode.TAG, "asyncNewChild() is not supported");
            return null;
        }

        public AutofillId getAutofillId() {
            return this.mNode.mAutofillId;
        }

        public void setAutofillId(AutofillId id) {
            this.mNode.mAutofillId = (AutofillId) Preconditions.checkNotNull(id);
        }

        public void setAutofillId(AutofillId parentId, int virtualId) {
            this.mNode.mParentAutofillId = (AutofillId) Preconditions.checkNotNull(parentId);
            this.mNode.mAutofillId = new AutofillId(parentId, virtualId);
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
            Log.w(ViewNode.TAG, "setDataIsSensitive() is not supported");
        }

        public void asyncCommit() {
            Log.w(ViewNode.TAG, "asyncCommit() is not supported");
        }

        public Rect getTempRect() {
            Log.w(ViewNode.TAG, "getTempRect() is not supported");
            return null;
        }

        public void setWebDomain(String domain) {
            Log.w(ViewNode.TAG, "setWebDomain() is not supported");
        }

        public void setLocaleList(LocaleList localeList) {
            this.mNode.mLocaleList = localeList;
        }

        public Builder newHtmlInfoBuilder(String tagName) {
            Log.w(ViewNode.TAG, "newHtmlInfoBuilder() is not supported");
            return null;
        }

        public void setHtmlInfo(HtmlInfo htmlInfo) {
            Log.w(ViewNode.TAG, "setHtmlInfo() is not supported");
        }

        private ViewNodeText getNodeText() {
            if (this.mNode.mText != null) {
                return this.mNode.mText;
            }
            this.mNode.mText = new ViewNodeText();
            return this.mNode.mText;
        }
    }

    public ViewNode() {
        this.mId = -1;
        this.mMinEms = -1;
        this.mMaxEms = -1;
        this.mMaxLength = -1;
        this.mAutofillType = 0;
    }

    private ViewNode(long nodeFlags, Parcel parcel) {
        this.mId = -1;
        this.mMinEms = -1;
        this.mMaxEms = -1;
        this.mMaxLength = -1;
        boolean z = false;
        this.mAutofillType = 0;
        this.mFlags = nodeFlags;
        if ((32 & nodeFlags) != 0) {
            this.mAutofillId = (AutofillId) parcel.readParcelable(null);
        }
        if ((64 & nodeFlags) != 0) {
            this.mParentAutofillId = (AutofillId) parcel.readParcelable(null);
        }
        if ((1 & nodeFlags) != 0) {
            if ((2 & nodeFlags) == 0) {
                z = true;
            }
            this.mText = new ViewNodeText(parcel, z);
        }
        if ((16 & nodeFlags) != 0) {
            this.mClassName = parcel.readString();
        }
        if ((128 & nodeFlags) != 0) {
            this.mId = parcel.readInt();
            if (this.mId != -1) {
                this.mIdEntry = parcel.readString();
                if (this.mIdEntry != null) {
                    this.mIdType = parcel.readString();
                    this.mIdPackage = parcel.readString();
                }
            }
        }
        if ((256 & nodeFlags) != 0) {
            this.mX = parcel.readInt();
            this.mY = parcel.readInt();
            this.mWidth = parcel.readInt();
            this.mHeight = parcel.readInt();
        } else {
            int val = parcel.readInt();
            this.mX = val & 32767;
            this.mY = (val >> 16) & 32767;
            val = parcel.readInt();
            this.mWidth = val & 32767;
            this.mHeight = (val >> 16) & 32767;
        }
        if ((512 & nodeFlags) != 0) {
            this.mScrollX = parcel.readInt();
            this.mScrollY = parcel.readInt();
        }
        if ((8388608 & nodeFlags) != 0) {
            this.mContentDescription = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        }
        if ((16777216 & nodeFlags) != 0) {
            this.mExtras = parcel.readBundle();
        }
        if ((33554432 & nodeFlags) != 0) {
            this.mLocaleList = (LocaleList) parcel.readParcelable(null);
        }
        if ((67108864 & nodeFlags) != 0) {
            this.mInputType = parcel.readInt();
        }
        if ((FLAGS_HAS_MIN_TEXT_EMS & nodeFlags) != 0) {
            this.mMinEms = parcel.readInt();
        }
        if ((FLAGS_HAS_MAX_TEXT_EMS & nodeFlags) != 0) {
            this.mMaxEms = parcel.readInt();
        }
        if ((FLAGS_HAS_MAX_TEXT_LENGTH & nodeFlags) != 0) {
            this.mMaxLength = parcel.readInt();
        }
        if ((1073741824 & nodeFlags) != 0) {
            this.mTextIdEntry = parcel.readString();
        }
        if ((FLAGS_HAS_AUTOFILL_TYPE & nodeFlags) != 0) {
            this.mAutofillType = parcel.readInt();
        }
        if ((8589934592L & nodeFlags) != 0) {
            this.mAutofillHints = parcel.readStringArray();
        }
        if ((4294967296L & nodeFlags) != 0) {
            this.mAutofillValue = (AutofillValue) parcel.readParcelable(null);
        }
        if ((17179869184L & nodeFlags) != 0) {
            this.mAutofillOptions = parcel.readCharSequenceArray();
        }
    }

    public AutofillId getParentAutofillId() {
        return this.mParentAutofillId;
    }

    public AutofillId getAutofillId() {
        return this.mAutofillId;
    }

    public CharSequence getText() {
        ViewNodeText viewNodeText = this.mText;
        return viewNodeText != null ? viewNodeText.mText : null;
    }

    public String getClassName() {
        return this.mClassName;
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

    public boolean isAssistBlocked() {
        return (this.mFlags & 1024) != 0;
    }

    public boolean isEnabled() {
        return (this.mFlags & 2048) == 0;
    }

    public boolean isClickable() {
        return (this.mFlags & 4096) != 0;
    }

    public boolean isLongClickable() {
        return (this.mFlags & 8192) != 0;
    }

    public boolean isContextClickable() {
        return (this.mFlags & 16384) != 0;
    }

    public boolean isFocusable() {
        return (this.mFlags & 32768) != 0;
    }

    public boolean isFocused() {
        return (this.mFlags & 65536) != 0;
    }

    public boolean isAccessibilityFocused() {
        return (this.mFlags & 131072) != 0;
    }

    public boolean isCheckable() {
        return (this.mFlags & 262144) != 0;
    }

    public boolean isChecked() {
        return (this.mFlags & 524288) != 0;
    }

    public boolean isSelected() {
        return (this.mFlags & 1048576) != 0;
    }

    public boolean isActivated() {
        return (this.mFlags & 2097152) != 0;
    }

    public boolean isOpaque() {
        return (this.mFlags & 4194304) != 0;
    }

    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public String getHint() {
        ViewNodeText viewNodeText = this.mText;
        return viewNodeText != null ? viewNodeText.mHint : null;
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

    public int getVisibility() {
        return (int) (this.mFlags & FLAGS_VISIBILITY_MASK);
    }

    public int getInputType() {
        return this.mInputType;
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

    public String getTextIdEntry() {
        return this.mTextIdEntry;
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

    public CharSequence[] getAutofillOptions() {
        return this.mAutofillOptions;
    }

    public LocaleList getLocaleList() {
        return this.mLocaleList;
    }

    /* JADX WARNING: Missing block: B:30:0x0057, code skipped:
            if ((((r0.mWidth & -32768) != 0 ? 1 : 0) | ((r0.mHeight & -32768) != 0 ? 1 : 0)) != 0) goto L_0x0059;
     */
    private void writeSelfToParcel(android.os.Parcel r29, int r30) {
        /*
        r28 = this;
        r0 = r28;
        r1 = r29;
        r2 = r30;
        r3 = r0.mFlags;
        r5 = r0.mAutofillId;
        r6 = 32;
        if (r5 == 0) goto L_0x000f;
    L_0x000e:
        r3 = r3 | r6;
    L_0x000f:
        r5 = r0.mParentAutofillId;
        r8 = 64;
        if (r5 == 0) goto L_0x0016;
    L_0x0015:
        r3 = r3 | r8;
    L_0x0016:
        r5 = r0.mText;
        r10 = 2;
        r12 = 1;
        if (r5 == 0) goto L_0x0026;
    L_0x001e:
        r3 = r3 | r12;
        r5 = r5.isSimple();
        if (r5 != 0) goto L_0x0026;
    L_0x0025:
        r3 = r3 | r10;
    L_0x0026:
        r5 = r0.mClassName;
        r14 = 16;
        if (r5 == 0) goto L_0x002d;
    L_0x002c:
        r3 = r3 | r14;
    L_0x002d:
        r5 = r0.mId;
        r16 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r14 = -1;
        if (r5 == r14) goto L_0x0036;
    L_0x0034:
        r3 = r3 | r16;
    L_0x0036:
        r5 = r0.mX;
        r5 = r5 & -32768;
        r18 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        if (r5 != 0) goto L_0x0059;
    L_0x003e:
        r5 = r0.mY;
        r5 = r5 & -32768;
        if (r5 != 0) goto L_0x0059;
    L_0x0044:
        r5 = r0.mWidth;
        r5 = r5 & -32768;
        if (r5 == 0) goto L_0x004c;
    L_0x004a:
        r5 = 1;
        goto L_0x004d;
    L_0x004c:
        r5 = 0;
    L_0x004d:
        r15 = r0.mHeight;
        r15 = r15 & -32768;
        if (r15 == 0) goto L_0x0055;
    L_0x0053:
        r15 = 1;
        goto L_0x0056;
    L_0x0055:
        r15 = 0;
    L_0x0056:
        r5 = r5 | r15;
        if (r5 == 0) goto L_0x005b;
    L_0x0059:
        r3 = r3 | r18;
    L_0x005b:
        r5 = r0.mScrollX;
        r20 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        if (r5 != 0) goto L_0x0065;
    L_0x0061:
        r5 = r0.mScrollY;
        if (r5 == 0) goto L_0x0067;
    L_0x0065:
        r3 = r3 | r20;
    L_0x0067:
        r5 = r0.mContentDescription;
        r22 = 8388608; // 0x800000 float:1.17549435E-38 double:4.144523E-317;
        if (r5 == 0) goto L_0x0070;
    L_0x006e:
        r3 = r3 | r22;
    L_0x0070:
        r5 = r0.mExtras;
        r24 = 16777216; // 0x1000000 float:2.3509887E-38 double:8.289046E-317;
        if (r5 == 0) goto L_0x0079;
    L_0x0077:
        r3 = r3 | r24;
    L_0x0079:
        r5 = r0.mLocaleList;
        if (r5 == 0) goto L_0x0082;
    L_0x007d:
        r26 = 33554432; // 0x2000000 float:9.403955E-38 double:1.6578092E-316;
        r3 = r3 | r26;
    L_0x0082:
        r5 = r0.mInputType;
        if (r5 == 0) goto L_0x008b;
    L_0x0086:
        r26 = 67108864; // 0x4000000 float:1.5046328E-36 double:3.31561842E-316;
        r3 = r3 | r26;
    L_0x008b:
        r5 = r0.mMinEms;
        if (r5 <= r14) goto L_0x0094;
    L_0x008f:
        r26 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r3 = r3 | r26;
    L_0x0094:
        r5 = r0.mMaxEms;
        if (r5 <= r14) goto L_0x009d;
    L_0x0098:
        r26 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r3 = r3 | r26;
    L_0x009d:
        r5 = r0.mMaxLength;
        if (r5 <= r14) goto L_0x00a6;
    L_0x00a1:
        r26 = 536870912; // 0x20000000 float:1.0842022E-19 double:2.652494739E-315;
        r3 = r3 | r26;
    L_0x00a6:
        r5 = r0.mTextIdEntry;
        if (r5 == 0) goto L_0x00af;
    L_0x00aa:
        r26 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r3 = r3 | r26;
    L_0x00af:
        r5 = r0.mAutofillValue;
        if (r5 == 0) goto L_0x00ba;
    L_0x00b3:
        r26 = 4294967296; // 0x100000000 float:0.0 double:2.121995791E-314;
        r3 = r3 | r26;
    L_0x00ba:
        r5 = r0.mAutofillType;
        if (r5 == 0) goto L_0x00c5;
    L_0x00be:
        r26 = 2147483648; // 0x80000000 float:-0.0 double:1.0609978955E-314;
        r3 = r3 | r26;
    L_0x00c5:
        r5 = r0.mAutofillHints;
        if (r5 == 0) goto L_0x00d0;
    L_0x00c9:
        r26 = 8589934592; // 0x200000000 float:0.0 double:4.243991582E-314;
        r3 = r3 | r26;
    L_0x00d0:
        r5 = r0.mAutofillOptions;
        if (r5 == 0) goto L_0x00db;
    L_0x00d4:
        r26 = 17179869184; // 0x400000000 float:0.0 double:8.4879831639E-314;
        r3 = r3 | r26;
    L_0x00db:
        r1.writeLong(r3);
        r5 = r3 & r6;
        r26 = 0;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x00eb;
    L_0x00e6:
        r5 = r0.mAutofillId;
        r1.writeParcelable(r5, r2);
    L_0x00eb:
        r5 = r3 & r8;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x00f6;
    L_0x00f1:
        r5 = r0.mParentAutofillId;
        r1.writeParcelable(r5, r2);
    L_0x00f6:
        r5 = r3 & r12;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x010a;
    L_0x00fc:
        r5 = r0.mText;
        r6 = r3 & r10;
        r6 = (r6 > r26 ? 1 : (r6 == r26 ? 0 : -1));
        if (r6 != 0) goto L_0x0106;
    L_0x0104:
        r6 = 1;
        goto L_0x0107;
    L_0x0106:
        r6 = 0;
    L_0x0107:
        r5.writeToParcel(r1, r6);
    L_0x010a:
        r5 = 16;
        r5 = r5 & r3;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x0116;
    L_0x0111:
        r5 = r0.mClassName;
        r1.writeString(r5);
    L_0x0116:
        r5 = r3 & r16;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x0138;
    L_0x011c:
        r5 = r0.mId;
        r1.writeInt(r5);
        r5 = r0.mId;
        if (r5 == r14) goto L_0x0138;
    L_0x0125:
        r5 = r0.mIdEntry;
        r1.writeString(r5);
        r5 = r0.mIdEntry;
        if (r5 == 0) goto L_0x0138;
    L_0x012e:
        r5 = r0.mIdType;
        r1.writeString(r5);
        r5 = r0.mIdPackage;
        r1.writeString(r5);
    L_0x0138:
        r5 = r3 & r18;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x0153;
    L_0x013e:
        r5 = r0.mX;
        r1.writeInt(r5);
        r5 = r0.mY;
        r1.writeInt(r5);
        r5 = r0.mWidth;
        r1.writeInt(r5);
        r5 = r0.mHeight;
        r1.writeInt(r5);
        goto L_0x0167;
    L_0x0153:
        r5 = r0.mY;
        r5 = r5 << 16;
        r6 = r0.mX;
        r5 = r5 | r6;
        r1.writeInt(r5);
        r5 = r0.mHeight;
        r5 = r5 << 16;
        r6 = r0.mWidth;
        r5 = r5 | r6;
        r1.writeInt(r5);
    L_0x0167:
        r5 = r3 & r20;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x0177;
    L_0x016d:
        r5 = r0.mScrollX;
        r1.writeInt(r5);
        r5 = r0.mScrollY;
        r1.writeInt(r5);
    L_0x0177:
        r5 = r3 & r22;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x0183;
    L_0x017d:
        r5 = r0.mContentDescription;
        r6 = 0;
        android.text.TextUtils.writeToParcel(r5, r1, r6);
    L_0x0183:
        r5 = r3 & r24;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x018e;
    L_0x0189:
        r5 = r0.mExtras;
        r1.writeBundle(r5);
    L_0x018e:
        r5 = 33554432; // 0x2000000 float:9.403955E-38 double:1.6578092E-316;
        r5 = r5 & r3;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x019c;
    L_0x0196:
        r5 = r0.mLocaleList;
        r6 = 0;
        r1.writeParcelable(r5, r6);
    L_0x019c:
        r5 = 67108864; // 0x4000000 float:1.5046328E-36 double:3.31561842E-316;
        r5 = r5 & r3;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x01a9;
    L_0x01a4:
        r5 = r0.mInputType;
        r1.writeInt(r5);
    L_0x01a9:
        r5 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r5 = r5 & r3;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x01b6;
    L_0x01b1:
        r5 = r0.mMinEms;
        r1.writeInt(r5);
    L_0x01b6:
        r5 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r5 = r5 & r3;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x01c3;
    L_0x01be:
        r5 = r0.mMaxEms;
        r1.writeInt(r5);
    L_0x01c3:
        r5 = 536870912; // 0x20000000 float:1.0842022E-19 double:2.652494739E-315;
        r5 = r5 & r3;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x01d0;
    L_0x01cb:
        r5 = r0.mMaxLength;
        r1.writeInt(r5);
    L_0x01d0:
        r5 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r5 = r5 & r3;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x01dd;
    L_0x01d8:
        r5 = r0.mTextIdEntry;
        r1.writeString(r5);
    L_0x01dd:
        r5 = 2147483648; // 0x80000000 float:-0.0 double:1.0609978955E-314;
        r5 = r5 & r3;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x01ec;
    L_0x01e7:
        r5 = r0.mAutofillType;
        r1.writeInt(r5);
    L_0x01ec:
        r5 = 8589934592; // 0x200000000 float:0.0 double:4.243991582E-314;
        r5 = r5 & r3;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x01fb;
    L_0x01f6:
        r5 = r0.mAutofillHints;
        r1.writeStringArray(r5);
    L_0x01fb:
        r5 = 4294967296; // 0x100000000 float:0.0 double:2.121995791E-314;
        r5 = r5 & r3;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x020b;
    L_0x0205:
        r5 = r0.mAutofillValue;
        r6 = 0;
        r1.writeParcelable(r5, r6);
    L_0x020b:
        r5 = 17179869184; // 0x400000000 float:0.0 double:8.4879831639E-314;
        r5 = r5 & r3;
        r5 = (r5 > r26 ? 1 : (r5 == r26 ? 0 : -1));
        if (r5 == 0) goto L_0x021a;
    L_0x0215:
        r5 = r0.mAutofillOptions;
        r1.writeCharSequenceArray(r5);
    L_0x021a:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.contentcapture.ViewNode.writeSelfToParcel(android.os.Parcel, int):void");
    }

    public static void writeToParcel(Parcel parcel, ViewNode node, int flags) {
        if (node == null) {
            parcel.writeLong(0);
        } else {
            node.writeSelfToParcel(parcel, flags);
        }
    }

    public static ViewNode readFromParcel(Parcel parcel) {
        long nodeFlags = parcel.readLong();
        return nodeFlags == 0 ? null : new ViewNode(nodeFlags, parcel);
    }
}
