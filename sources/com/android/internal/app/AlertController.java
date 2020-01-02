package com.android.internal.app;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.android.internal.R;
import java.lang.ref.WeakReference;

public class AlertController {
    public static final int MICRO = 1;
    private ListAdapter mAdapter;
    private int mAlertDialogLayout;
    private final OnClickListener mButtonHandler = new OnClickListener() {
        public void onClick(View v) {
            Message m;
            if (v == AlertController.this.mButtonPositive && AlertController.this.mButtonPositiveMessage != null) {
                m = Message.obtain(AlertController.this.mButtonPositiveMessage);
            } else if (v == AlertController.this.mButtonNegative && AlertController.this.mButtonNegativeMessage != null) {
                m = Message.obtain(AlertController.this.mButtonNegativeMessage);
            } else if (v != AlertController.this.mButtonNeutral || AlertController.this.mButtonNeutralMessage == null) {
                m = null;
            } else {
                m = Message.obtain(AlertController.this.mButtonNeutralMessage);
            }
            if (m != null) {
                m.sendToTarget();
            }
            AlertController.this.mHandler.obtainMessage(1, AlertController.this.mDialogInterface).sendToTarget();
        }
    };
    private Button mButtonNegative;
    private Message mButtonNegativeMessage;
    private CharSequence mButtonNegativeText;
    private Button mButtonNeutral;
    private Message mButtonNeutralMessage;
    private CharSequence mButtonNeutralText;
    private int mButtonPanelLayoutHint = 0;
    private int mButtonPanelSideLayout;
    private Button mButtonPositive;
    private Message mButtonPositiveMessage;
    private CharSequence mButtonPositiveText;
    private int mCheckedItem = -1;
    private final Context mContext;
    @UnsupportedAppUsage
    private View mCustomTitleView;
    private final DialogInterface mDialogInterface;
    @UnsupportedAppUsage
    private boolean mForceInverseBackground;
    private Handler mHandler;
    private Drawable mIcon;
    private int mIconId = 0;
    private ImageView mIconView;
    private int mListItemLayout;
    private int mListLayout;
    protected ListView mListView;
    protected CharSequence mMessage;
    private Integer mMessageHyphenationFrequency;
    private MovementMethod mMessageMovementMethod;
    protected TextView mMessageView;
    private int mMultiChoiceItemLayout;
    protected ScrollView mScrollView;
    private boolean mShowTitle;
    private int mSingleChoiceItemLayout;
    @UnsupportedAppUsage
    private CharSequence mTitle;
    private TextView mTitleView;
    @UnsupportedAppUsage
    private View mView;
    private int mViewLayoutResId;
    private int mViewSpacingBottom;
    private int mViewSpacingLeft;
    private int mViewSpacingRight;
    private boolean mViewSpacingSpecified = false;
    private int mViewSpacingTop;
    protected final Window mWindow;

    public static class AlertParams {
        @UnsupportedAppUsage
        public ListAdapter mAdapter;
        @UnsupportedAppUsage
        public boolean mCancelable;
        @UnsupportedAppUsage
        public int mCheckedItem = -1;
        @UnsupportedAppUsage
        public boolean[] mCheckedItems;
        @UnsupportedAppUsage
        public final Context mContext;
        @UnsupportedAppUsage
        public Cursor mCursor;
        @UnsupportedAppUsage
        public View mCustomTitleView;
        public boolean mForceInverseBackground;
        @UnsupportedAppUsage
        public Drawable mIcon;
        public int mIconAttrId = 0;
        @UnsupportedAppUsage
        public int mIconId = 0;
        @UnsupportedAppUsage
        public final LayoutInflater mInflater;
        @UnsupportedAppUsage
        public String mIsCheckedColumn;
        @UnsupportedAppUsage
        public boolean mIsMultiChoice;
        @UnsupportedAppUsage
        public boolean mIsSingleChoice;
        @UnsupportedAppUsage
        public CharSequence[] mItems;
        @UnsupportedAppUsage
        public String mLabelColumn;
        @UnsupportedAppUsage
        public CharSequence mMessage;
        @UnsupportedAppUsage
        public DialogInterface.OnClickListener mNegativeButtonListener;
        @UnsupportedAppUsage
        public CharSequence mNegativeButtonText;
        @UnsupportedAppUsage
        public DialogInterface.OnClickListener mNeutralButtonListener;
        @UnsupportedAppUsage
        public CharSequence mNeutralButtonText;
        @UnsupportedAppUsage
        public OnCancelListener mOnCancelListener;
        @UnsupportedAppUsage
        public OnMultiChoiceClickListener mOnCheckboxClickListener;
        @UnsupportedAppUsage
        public DialogInterface.OnClickListener mOnClickListener;
        @UnsupportedAppUsage
        public OnDismissListener mOnDismissListener;
        @UnsupportedAppUsage
        public OnItemSelectedListener mOnItemSelectedListener;
        @UnsupportedAppUsage
        public OnKeyListener mOnKeyListener;
        public OnPrepareListViewListener mOnPrepareListViewListener;
        @UnsupportedAppUsage
        public DialogInterface.OnClickListener mPositiveButtonListener;
        @UnsupportedAppUsage
        public CharSequence mPositiveButtonText;
        public boolean mRecycleOnMeasure = true;
        @UnsupportedAppUsage
        public CharSequence mTitle;
        @UnsupportedAppUsage
        public View mView;
        public int mViewLayoutResId;
        public int mViewSpacingBottom;
        public int mViewSpacingLeft;
        public int mViewSpacingRight;
        public boolean mViewSpacingSpecified = false;
        public int mViewSpacingTop;

        public interface OnPrepareListViewListener {
            void onPrepareListView(ListView listView);
        }

        @UnsupportedAppUsage
        public AlertParams(Context context) {
            this.mContext = context;
            this.mCancelable = true;
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @UnsupportedAppUsage
        public void apply(AlertController dialog) {
            CharSequence charSequence;
            int i;
            View view = this.mCustomTitleView;
            if (view != null) {
                dialog.setCustomTitle(view);
            } else {
                charSequence = this.mTitle;
                if (charSequence != null) {
                    dialog.setTitle(charSequence);
                }
                Drawable drawable = this.mIcon;
                if (drawable != null) {
                    dialog.setIcon(drawable);
                }
                i = this.mIconId;
                if (i != 0) {
                    dialog.setIcon(i);
                }
                i = this.mIconAttrId;
                if (i != 0) {
                    dialog.setIcon(dialog.getIconAttributeResId(i));
                }
            }
            charSequence = this.mMessage;
            if (charSequence != null) {
                dialog.setMessage(charSequence);
            }
            charSequence = this.mPositiveButtonText;
            if (charSequence != null) {
                dialog.setButton(-1, charSequence, this.mPositiveButtonListener, null);
            }
            charSequence = this.mNegativeButtonText;
            if (charSequence != null) {
                dialog.setButton(-2, charSequence, this.mNegativeButtonListener, null);
            }
            charSequence = this.mNeutralButtonText;
            if (charSequence != null) {
                dialog.setButton(-3, charSequence, this.mNeutralButtonListener, null);
            }
            if (this.mForceInverseBackground) {
                dialog.setInverseBackgroundForced(true);
            }
            if (!(this.mItems == null && this.mCursor == null && this.mAdapter == null)) {
                createListView(dialog);
            }
            View view2 = this.mView;
            if (view2 == null) {
                i = this.mViewLayoutResId;
                if (i != 0) {
                    dialog.setView(i);
                }
            } else if (this.mViewSpacingSpecified) {
                dialog.setView(view2, this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
            } else {
                dialog.setView(view2);
            }
        }

        private void createListView(final AlertController dialog) {
            ListAdapter adapter;
            final RecycleListView listView = (RecycleListView) this.mInflater.inflate(dialog.mListLayout, null);
            if (this.mIsMultiChoice) {
                Cursor cursor = this.mCursor;
                if (cursor == null) {
                    final RecycleListView recycleListView = listView;
                    adapter = new ArrayAdapter<CharSequence>(this.mContext, dialog.mMultiChoiceItemLayout, 16908308, this.mItems) {
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            if (AlertParams.this.mCheckedItems != null && AlertParams.this.mCheckedItems[position]) {
                                recycleListView.setItemChecked(position, true);
                            }
                            return view;
                        }
                    };
                } else {
                    final RecycleListView recycleListView2 = listView;
                    final AlertController alertController = dialog;
                    adapter = new CursorAdapter(this.mContext, cursor, false) {
                        private final int mIsCheckedIndex;
                        private final int mLabelIndex;

                        public void bindView(View view, Context context, Cursor cursor) {
                            ((CheckedTextView) view.findViewById(16908308)).setText((CharSequence) cursor.getString(this.mLabelIndex));
                            RecycleListView recycleListView = recycleListView2;
                            int position = cursor.getPosition();
                            boolean z = true;
                            if (cursor.getInt(this.mIsCheckedIndex) != 1) {
                                z = false;
                            }
                            recycleListView.setItemChecked(position, z);
                        }

                        public View newView(Context context, Cursor cursor, ViewGroup parent) {
                            return AlertParams.this.mInflater.inflate(alertController.mMultiChoiceItemLayout, parent, false);
                        }
                    };
                }
            } else {
                int layout;
                if (this.mIsSingleChoice) {
                    layout = dialog.mSingleChoiceItemLayout;
                } else {
                    layout = dialog.mListItemLayout;
                }
                Cursor cursor2 = this.mCursor;
                if (cursor2 != null) {
                    adapter = new SimpleCursorAdapter(this.mContext, layout, cursor2, new String[]{this.mLabelColumn}, new int[]{16908308});
                } else if (this.mAdapter != null) {
                    adapter = this.mAdapter;
                } else {
                    adapter = new CheckedItemAdapter(this.mContext, layout, 16908308, this.mItems);
                }
            }
            OnPrepareListViewListener onPrepareListViewListener = this.mOnPrepareListViewListener;
            if (onPrepareListViewListener != null) {
                onPrepareListViewListener.onPrepareListView(listView);
            }
            dialog.mAdapter = adapter;
            dialog.mCheckedItem = this.mCheckedItem;
            if (this.mOnClickListener != null) {
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                        AlertParams.this.mOnClickListener.onClick(dialog.mDialogInterface, position);
                        if (!AlertParams.this.mIsSingleChoice) {
                            dialog.mDialogInterface.dismiss();
                        }
                    }
                });
            } else if (this.mOnCheckboxClickListener != null) {
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                        if (AlertParams.this.mCheckedItems != null) {
                            AlertParams.this.mCheckedItems[position] = listView.isItemChecked(position);
                        }
                        AlertParams.this.mOnCheckboxClickListener.onClick(dialog.mDialogInterface, position, listView.isItemChecked(position));
                    }
                });
            }
            OnItemSelectedListener onItemSelectedListener = this.mOnItemSelectedListener;
            if (onItemSelectedListener != null) {
                listView.setOnItemSelectedListener(onItemSelectedListener);
            }
            if (this.mIsSingleChoice) {
                listView.setChoiceMode(1);
            } else if (this.mIsMultiChoice) {
                listView.setChoiceMode(2);
            }
            listView.mRecycleOnMeasure = this.mRecycleOnMeasure;
            dialog.mListView = listView;
        }
    }

    private static final class ButtonHandler extends Handler {
        private static final int MSG_DISMISS_DIALOG = 1;
        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialog) {
            this.mDialog = new WeakReference(dialog);
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == -3 || i == -2 || i == -1) {
                ((DialogInterface.OnClickListener) msg.obj).onClick((DialogInterface) this.mDialog.get(), msg.what);
            } else if (i == 1) {
                ((DialogInterface) msg.obj).dismiss();
            }
        }
    }

    private static class CheckedItemAdapter extends ArrayAdapter<CharSequence> {
        public CheckedItemAdapter(Context context, int resource, int textViewResourceId, CharSequence[] objects) {
            super(context, resource, textViewResourceId, (Object[]) objects);
        }

        public boolean hasStableIds() {
            return true;
        }

        public long getItemId(int position) {
            return (long) position;
        }
    }

    public static class RecycleListView extends ListView {
        private final int mPaddingBottomNoButtons;
        private final int mPaddingTopNoTitle;
        boolean mRecycleOnMeasure;

        @UnsupportedAppUsage
        public RecycleListView(Context context) {
            this(context, null);
        }

        @UnsupportedAppUsage
        public RecycleListView(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.mRecycleOnMeasure = true;
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RecycleListView);
            this.mPaddingBottomNoButtons = ta.getDimensionPixelOffset(0, -1);
            this.mPaddingTopNoTitle = ta.getDimensionPixelOffset(1, -1);
        }

        public void setHasDecor(boolean hasTitle, boolean hasButtons) {
            if (!hasButtons || !hasTitle) {
                setPadding(getPaddingLeft(), hasTitle ? getPaddingTop() : this.mPaddingTopNoTitle, getPaddingRight(), hasButtons ? getPaddingBottom() : this.mPaddingBottomNoButtons);
            }
        }

        /* Access modifiers changed, original: protected */
        public boolean recycleOnMeasure() {
            return this.mRecycleOnMeasure;
        }
    }

    private static boolean shouldCenterSingleButton(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.alertDialogCenterButtons, outValue, true);
        if (outValue.data != 0) {
            return true;
        }
        return false;
    }

    public static final AlertController create(Context context, DialogInterface di, Window window) {
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.AlertDialog, 16842845, 16974371);
        int controllerType = a.getInt(12, 0);
        a.recycle();
        if (controllerType != 1) {
            return new AlertController(context, di, window);
        }
        return new MicroAlertController(context, di, window);
    }

    @UnsupportedAppUsage
    protected AlertController(Context context, DialogInterface di, Window window) {
        this.mContext = context;
        this.mDialogInterface = di;
        this.mWindow = window;
        this.mHandler = new ButtonHandler(di);
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.AlertDialog, 16842845, 0);
        this.mAlertDialogLayout = a.getResourceId(10, R.layout.alert_dialog);
        this.mButtonPanelSideLayout = a.getResourceId(11, 0);
        this.mListLayout = a.getResourceId(15, R.layout.select_dialog);
        this.mMultiChoiceItemLayout = a.getResourceId(16, 17367059);
        this.mSingleChoiceItemLayout = a.getResourceId(21, 17367058);
        this.mListItemLayout = a.getResourceId(14, 17367057);
        this.mShowTitle = a.getBoolean(20, true);
        a.recycle();
        window.requestFeature(1);
    }

    static boolean canTextInput(View v) {
        if (v.onCheckIsTextEditor()) {
            return true;
        }
        if (!(v instanceof ViewGroup)) {
            return false;
        }
        ViewGroup vg = (ViewGroup) v;
        int i = vg.getChildCount();
        while (i > 0) {
            i--;
            if (canTextInput(vg.getChildAt(i))) {
                return true;
            }
        }
        return false;
    }

    public void installContent(AlertParams params) {
        params.apply(this);
        installContent();
    }

    @UnsupportedAppUsage
    public void installContent() {
        this.mWindow.setContentView(selectContentView());
        setupView();
    }

    private int selectContentView() {
        int i = this.mButtonPanelSideLayout;
        if (i == 0) {
            return this.mAlertDialogLayout;
        }
        if (this.mButtonPanelLayoutHint == 1) {
            return i;
        }
        return this.mAlertDialogLayout;
    }

    @UnsupportedAppUsage
    public void setTitle(CharSequence title) {
        this.mTitle = title;
        TextView textView = this.mTitleView;
        if (textView != null) {
            textView.setText(title);
        }
    }

    @UnsupportedAppUsage
    public void setCustomTitle(View customTitleView) {
        this.mCustomTitleView = customTitleView;
    }

    @UnsupportedAppUsage
    public void setMessage(CharSequence message) {
        this.mMessage = message;
        TextView textView = this.mMessageView;
        if (textView != null) {
            textView.setText(message);
        }
    }

    public void setMessageMovementMethod(MovementMethod movementMethod) {
        this.mMessageMovementMethod = movementMethod;
        TextView textView = this.mMessageView;
        if (textView != null) {
            textView.setMovementMethod(movementMethod);
        }
    }

    public void setMessageHyphenationFrequency(int hyphenationFrequency) {
        this.mMessageHyphenationFrequency = Integer.valueOf(hyphenationFrequency);
        TextView textView = this.mMessageView;
        if (textView != null) {
            textView.setHyphenationFrequency(hyphenationFrequency);
        }
    }

    public void setView(int layoutResId) {
        this.mView = null;
        this.mViewLayoutResId = layoutResId;
        this.mViewSpacingSpecified = false;
    }

    @UnsupportedAppUsage
    public void setView(View view) {
        this.mView = view;
        this.mViewLayoutResId = 0;
        this.mViewSpacingSpecified = false;
    }

    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        this.mView = view;
        this.mViewLayoutResId = 0;
        this.mViewSpacingSpecified = true;
        this.mViewSpacingLeft = viewSpacingLeft;
        this.mViewSpacingTop = viewSpacingTop;
        this.mViewSpacingRight = viewSpacingRight;
        this.mViewSpacingBottom = viewSpacingBottom;
    }

    public void setButtonPanelLayoutHint(int layoutHint) {
        this.mButtonPanelLayoutHint = layoutHint;
    }

    @UnsupportedAppUsage
    public void setButton(int whichButton, CharSequence text, DialogInterface.OnClickListener listener, Message msg) {
        if (msg == null && listener != null) {
            msg = this.mHandler.obtainMessage(whichButton, listener);
        }
        if (whichButton == -3) {
            this.mButtonNeutralText = text;
            this.mButtonNeutralMessage = msg;
        } else if (whichButton == -2) {
            this.mButtonNegativeText = text;
            this.mButtonNegativeMessage = msg;
        } else if (whichButton == -1) {
            this.mButtonPositiveText = text;
            this.mButtonPositiveMessage = msg;
        } else {
            throw new IllegalArgumentException("Button does not exist");
        }
    }

    @UnsupportedAppUsage
    public void setIcon(int resId) {
        this.mIcon = null;
        this.mIconId = resId;
        ImageView imageView = this.mIconView;
        if (imageView == null) {
            return;
        }
        if (resId != 0) {
            imageView.setVisibility(0);
            this.mIconView.setImageResource(this.mIconId);
            return;
        }
        imageView.setVisibility(8);
    }

    @UnsupportedAppUsage
    public void setIcon(Drawable icon) {
        this.mIcon = icon;
        this.mIconId = 0;
        ImageView imageView = this.mIconView;
        if (imageView == null) {
            return;
        }
        if (icon != null) {
            imageView.setVisibility(0);
            this.mIconView.setImageDrawable(icon);
            return;
        }
        imageView.setVisibility(8);
    }

    public int getIconAttributeResId(int attrId) {
        TypedValue out = new TypedValue();
        this.mContext.getTheme().resolveAttribute(attrId, out, true);
        return out.resourceId;
    }

    public void setInverseBackgroundForced(boolean forceInverseBackground) {
        this.mForceInverseBackground = forceInverseBackground;
    }

    @UnsupportedAppUsage
    public ListView getListView() {
        return this.mListView;
    }

    @UnsupportedAppUsage
    public Button getButton(int whichButton) {
        if (whichButton == -3) {
            return this.mButtonNeutral;
        }
        if (whichButton == -2) {
            return this.mButtonNegative;
        }
        if (whichButton != -1) {
            return null;
        }
        return this.mButtonPositive;
    }

    @UnsupportedAppUsage
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ScrollView scrollView = this.mScrollView;
        return scrollView != null && scrollView.executeKeyEvent(event);
    }

    @UnsupportedAppUsage
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        ScrollView scrollView = this.mScrollView;
        return scrollView != null && scrollView.executeKeyEvent(event);
    }

    private ViewGroup resolvePanel(View customPanel, View defaultPanel) {
        if (customPanel == null) {
            if (defaultPanel instanceof ViewStub) {
                defaultPanel = ((ViewStub) defaultPanel).inflate();
            }
            return (ViewGroup) defaultPanel;
        }
        if (defaultPanel != null) {
            ViewParent parent = defaultPanel.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(defaultPanel);
            }
        }
        if (customPanel instanceof ViewStub) {
            customPanel = ((ViewStub) customPanel).inflate();
        }
        return (ViewGroup) customPanel;
    }

    private void setupView() {
        boolean z;
        View spacer;
        boolean hasTopPanel;
        View parentPanel = this.mWindow.findViewById(R.id.parentPanel);
        View defaultTopPanel = parentPanel.findViewById(R.id.topPanel);
        View defaultContentPanel = parentPanel.findViewById(R.id.contentPanel);
        View defaultButtonPanel = parentPanel.findViewById(R.id.buttonPanel);
        View customPanel = (ViewGroup) parentPanel.findViewById(R.id.customPanel);
        setupCustomContent(customPanel);
        View customTopPanel = customPanel.findViewById(R.id.topPanel);
        View customContentPanel = customPanel.findViewById(R.id.contentPanel);
        View customButtonPanel = customPanel.findViewById(R.id.buttonPanel);
        View topPanel = resolvePanel(customTopPanel, defaultTopPanel);
        View contentPanel = resolvePanel(customContentPanel, defaultContentPanel);
        View buttonPanel = resolvePanel(customButtonPanel, defaultButtonPanel);
        setupContent(contentPanel);
        setupButtons(buttonPanel);
        setupTitle(topPanel);
        boolean hasCustomPanel = customPanel.getVisibility() != 8;
        boolean hasTopPanel2 = (topPanel == null || topPanel.getVisibility() == 8) ? false : true;
        boolean z2 = (buttonPanel == null || buttonPanel.getVisibility() == 8) ? false : true;
        boolean hasButtonPanel = z2;
        if (hasButtonPanel) {
            z = true;
        } else {
            if (contentPanel != null) {
                spacer = contentPanel.findViewById(R.id.textSpacerNoButtons);
                if (spacer != null) {
                    spacer.setVisibility(0);
                }
            }
            z = true;
            this.mWindow.setCloseOnTouchOutsideIfNotSet(true);
        }
        if (hasTopPanel2) {
            ScrollView scrollView = this.mScrollView;
            if (scrollView != null) {
                scrollView.setClipToPadding(z);
            }
            spacer = null;
            if (this.mMessage == null && this.mListView == null && !hasCustomPanel) {
                spacer = topPanel.findViewById(R.id.titleDividerTop);
            } else {
                if (!hasCustomPanel) {
                    spacer = topPanel.findViewById(R.id.titleDividerNoCustom);
                }
                if (spacer == null) {
                    spacer = topPanel.findViewById(R.id.titleDivider);
                }
            }
            if (spacer != null) {
                spacer.setVisibility(0);
            }
        } else if (contentPanel != null) {
            spacer = contentPanel.findViewById(R.id.textSpacerNoTitle);
            if (spacer != null) {
                spacer.setVisibility(0);
            }
        }
        ListView listView = this.mListView;
        if (listView instanceof RecycleListView) {
            ((RecycleListView) listView).setHasDecor(hasTopPanel2, hasButtonPanel);
        }
        if (hasCustomPanel) {
            hasTopPanel = hasTopPanel2;
        } else {
            spacer = this.mListView;
            if (spacer == null) {
                spacer = this.mScrollView;
            }
            if (spacer != null) {
                hasTopPanel = hasTopPanel2;
                spacer.setScrollIndicators((hasTopPanel2 ? 1 : 0) | (hasButtonPanel ? 2 : 0), 3);
            } else {
                hasTopPanel = hasTopPanel2;
            }
        }
        boolean hasButtonPanel2 = hasButtonPanel;
        View buttonPanel2 = buttonPanel;
        TypedArray a = this.mContext.obtainStyledAttributes(null, R.styleable.AlertDialog, 16842845, null);
        setBackground(a, topPanel, contentPanel, customPanel, buttonPanel2, hasTopPanel, hasCustomPanel, hasButtonPanel2);
        a.recycle();
    }

    private void setupCustomContent(ViewGroup customPanel) {
        View customView;
        boolean hasCustomView = false;
        if (this.mView != null) {
            customView = this.mView;
        } else if (this.mViewLayoutResId != 0) {
            customView = LayoutInflater.from(this.mContext).inflate(this.mViewLayoutResId, customPanel, false);
        } else {
            customView = null;
        }
        if (customView != null) {
            hasCustomView = true;
        }
        if (!(hasCustomView && canTextInput(customView))) {
            this.mWindow.setFlags(131072, 131072);
        }
        if (hasCustomView) {
            FrameLayout custom = (FrameLayout) this.mWindow.findViewById(16908331);
            custom.addView(customView, new LayoutParams(-1, -1));
            if (this.mViewSpacingSpecified) {
                custom.setPadding(this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
            }
            if (this.mListView != null) {
                ((LinearLayout.LayoutParams) customPanel.getLayoutParams()).weight = 0.0f;
                return;
            }
            return;
        }
        customPanel.setVisibility(8);
    }

    /* Access modifiers changed, original: protected */
    public void setupTitle(ViewGroup topPanel) {
        if (this.mCustomTitleView == null || !this.mShowTitle) {
            this.mIconView = (ImageView) this.mWindow.findViewById(16908294);
            if ((TextUtils.isEmpty(this.mTitle) ^ 1) && this.mShowTitle) {
                this.mTitleView = (TextView) this.mWindow.findViewById(R.id.alertTitle);
                this.mTitleView.setText(this.mTitle);
                int i = this.mIconId;
                if (i != 0) {
                    this.mIconView.setImageResource(i);
                    return;
                }
                Drawable drawable = this.mIcon;
                if (drawable != null) {
                    this.mIconView.setImageDrawable(drawable);
                    return;
                }
                this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
                this.mIconView.setVisibility(8);
                return;
            }
            this.mWindow.findViewById(R.id.title_template).setVisibility(8);
            this.mIconView.setVisibility(8);
            topPanel.setVisibility(8);
            return;
        }
        topPanel.addView(this.mCustomTitleView, 0, new LayoutParams(-1, -2));
        this.mWindow.findViewById(R.id.title_template).setVisibility(8);
    }

    /* Access modifiers changed, original: protected */
    public void setupContent(ViewGroup contentPanel) {
        this.mScrollView = (ScrollView) contentPanel.findViewById(R.id.scrollView);
        this.mScrollView.setFocusable(false);
        this.mMessageView = (TextView) contentPanel.findViewById(16908299);
        TextView textView = this.mMessageView;
        if (textView != null) {
            CharSequence charSequence = this.mMessage;
            if (charSequence != null) {
                textView.setText(charSequence);
                MovementMethod movementMethod = this.mMessageMovementMethod;
                if (movementMethod != null) {
                    this.mMessageView.setMovementMethod(movementMethod);
                }
                Integer num = this.mMessageHyphenationFrequency;
                if (num != null) {
                    this.mMessageView.setHyphenationFrequency(num.intValue());
                }
            } else {
                textView.setVisibility(8);
                this.mScrollView.removeView(this.mMessageView);
                if (this.mListView != null) {
                    ViewGroup scrollParent = (ViewGroup) this.mScrollView.getParent();
                    int childIndex = scrollParent.indexOfChild(this.mScrollView);
                    scrollParent.removeViewAt(childIndex);
                    scrollParent.addView(this.mListView, childIndex, new LayoutParams(-1, -1));
                } else {
                    contentPanel.setVisibility(8);
                }
            }
        }
    }

    private static void manageScrollIndicators(View v, View upIndicator, View downIndicator) {
        int i = 0;
        if (upIndicator != null) {
            upIndicator.setVisibility(v.canScrollVertically(-1) ? 0 : 4);
        }
        if (downIndicator != null) {
            if (!v.canScrollVertically(1)) {
                i = 4;
            }
            downIndicator.setVisibility(i);
        }
    }

    /* Access modifiers changed, original: protected */
    public void setupButtons(ViewGroup buttonPanel) {
        int whichButtons = 0;
        this.mButtonPositive = (Button) buttonPanel.findViewById(16908313);
        this.mButtonPositive.setOnClickListener(this.mButtonHandler);
        boolean z = false;
        if (TextUtils.isEmpty(this.mButtonPositiveText)) {
            this.mButtonPositive.setVisibility(8);
        } else {
            this.mButtonPositive.setText(this.mButtonPositiveText);
            this.mButtonPositive.setVisibility(0);
            whichButtons = 0 | 1;
        }
        this.mButtonNegative = (Button) buttonPanel.findViewById(16908314);
        this.mButtonNegative.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonNegativeText)) {
            this.mButtonNegative.setVisibility(8);
        } else {
            this.mButtonNegative.setText(this.mButtonNegativeText);
            this.mButtonNegative.setVisibility(0);
            whichButtons |= 2;
        }
        this.mButtonNeutral = (Button) buttonPanel.findViewById(16908315);
        this.mButtonNeutral.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonNeutralText)) {
            this.mButtonNeutral.setVisibility(8);
        } else {
            this.mButtonNeutral.setText(this.mButtonNeutralText);
            this.mButtonNeutral.setVisibility(0);
            whichButtons |= 4;
        }
        if (shouldCenterSingleButton(this.mContext)) {
            if (whichButtons == 1) {
                centerButton(this.mButtonPositive);
            } else if (whichButtons == 2) {
                centerButton(this.mButtonNegative);
            } else if (whichButtons == 4) {
                centerButton(this.mButtonNeutral);
            }
        }
        if (whichButtons != 0) {
            z = true;
        }
        if (!z) {
            buttonPanel.setVisibility(8);
        }
    }

    private void centerButton(Button button) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
        params.gravity = 1;
        params.weight = 0.5f;
        button.setLayoutParams(params);
        View leftSpacer = this.mWindow.findViewById(R.id.leftSpacer);
        if (leftSpacer != null) {
            leftSpacer.setVisibility(0);
        }
        View rightSpacer = this.mWindow.findViewById(R.id.rightSpacer);
        if (rightSpacer != null) {
            rightSpacer.setVisibility(0);
        }
    }

    private void setBackground(TypedArray a, View topPanel, View contentPanel, View customPanel, View buttonPanel, boolean hasTitle, boolean hasCustomView, boolean hasButtons) {
        TypedArray typedArray = a;
        int fullDark = 0;
        int topDark = 0;
        int centerDark = 0;
        int bottomDark = 0;
        int fullBright = 0;
        int topBright = 0;
        int centerBright = 0;
        int bottomBright = 0;
        int bottomMedium = 0;
        if (typedArray.getBoolean(true, true)) {
            fullDark = R.drawable.popup_full_dark;
            topDark = R.drawable.popup_top_dark;
            centerDark = R.drawable.popup_center_dark;
            bottomDark = 17303299;
            fullBright = R.drawable.popup_full_bright;
            topBright = R.drawable.popup_top_bright;
            centerBright = R.drawable.popup_center_bright;
            bottomBright = R.drawable.popup_bottom_bright;
            bottomMedium = R.drawable.popup_bottom_medium;
        }
        topBright = typedArray.getResourceId(5, topBright);
        topDark = typedArray.getResourceId(1, topDark);
        centerBright = typedArray.getResourceId(6, centerBright);
        centerDark = typedArray.getResourceId(2, centerDark);
        View[] views = new View[4];
        boolean[] light = new boolean[4];
        int pos = 0;
        if (hasTitle) {
            views[0] = topPanel;
            light[0] = false;
            pos = 0 + 1;
        }
        views[pos] = contentPanel.getVisibility() == 8 ? null : contentPanel;
        light[pos] = this.mListView != null;
        pos++;
        if (hasCustomView) {
            views[pos] = customPanel;
            light[pos] = this.mForceInverseBackground;
            pos++;
        }
        if (hasButtons) {
            views[pos] = buttonPanel;
            light[pos] = true;
        }
        View lastView = null;
        boolean setView = false;
        int pos2 = 0;
        boolean lastLight = false;
        while (true) {
            int topDark2 = topDark;
            if (pos2 >= views.length) {
                break;
            }
            int centerDark2;
            View v = views[pos2];
            if (v == null) {
                centerDark2 = centerDark;
            } else {
                if (lastView != null) {
                    if (setView) {
                        centerDark2 = centerDark;
                        lastView.setBackgroundResource(lastLight ? centerBright : centerDark2);
                    } else {
                        centerDark2 = centerDark;
                        lastView.setBackgroundResource(lastLight ? topBright : topDark2);
                    }
                    setView = true;
                } else {
                    centerDark2 = centerDark;
                }
                centerDark = v;
                lastLight = light[pos2];
                lastView = centerDark;
            }
            pos2++;
            topDark = topDark2;
            centerDark = centerDark2;
        }
        if (lastView != null) {
            if (setView) {
                topDark = lastLight ? hasButtons ? typedArray.getResourceId(8, bottomMedium) : typedArray.getResourceId(7, bottomBright) : typedArray.getResourceId(3, bottomDark);
                lastView.setBackgroundResource(topDark);
            } else {
                fullBright = typedArray.getResourceId(4, fullBright);
                fullDark = typedArray.getResourceId(0, fullDark);
                lastView.setBackgroundResource(lastLight ? fullBright : fullDark);
            }
        }
        ListView listView = this.mListView;
        if (listView != null) {
            ListAdapter listAdapter = this.mAdapter;
            if (listAdapter != null) {
                listView.setAdapter(listAdapter);
                centerDark = this.mCheckedItem;
                if (centerDark > -1) {
                    listView.setItemChecked(centerDark, true);
                    listView.setSelectionFromTop(centerDark, typedArray.getDimensionPixelSize(19, 0));
                    return;
                }
                return;
            }
        }
    }
}
