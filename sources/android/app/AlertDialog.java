package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.ResourceId;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.method.MovementMethod;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.android.internal.R;
import com.android.internal.app.AlertController;
import com.android.internal.app.AlertController.AlertParams;
import com.miui.internal.variable.api.v29.Android_App_AlertDialog;
import com.miui.internal.variable.api.v29.Android_App_AlertDialog_Builder.Extension;
import com.miui.internal.variable.api.v29.Android_App_AlertDialog_Builder.Interface;

public class AlertDialog extends Dialog implements DialogInterface {
    public static final int LAYOUT_HINT_NONE = 0;
    public static final int LAYOUT_HINT_SIDE = 1;
    @Deprecated
    public static final int THEME_DEVICE_DEFAULT_DARK = 4;
    @Deprecated
    public static final int THEME_DEVICE_DEFAULT_LIGHT = 5;
    @Deprecated
    public static final int THEME_HOLO_DARK = 2;
    @Deprecated
    public static final int THEME_HOLO_LIGHT = 3;
    @Deprecated
    public static final int THEME_TRADITIONAL = 1;
    @UnsupportedAppUsage
    private AlertController mAlert;

    public static class Builder {
        @UnsupportedAppUsage
        private final AlertParams P;

        public Builder(Context context) {
            this(context, AlertDialog.resolveDialogTheme(context, 0));
        }

        public Builder(Context context, int themeResId) {
            this.P = new AlertParams(new ContextThemeWrapper(context, AlertDialog.resolveDialogTheme(context, themeResId)));
            if (Extension.get().getExtension() != null) {
                ((Interface) Extension.get().getExtension().asInterface()).init(this, context, themeResId);
            }
        }

        public Context getContext() {
            return this.P.mContext;
        }

        public Builder setTitle(int titleId) {
            AlertParams alertParams = this.P;
            alertParams.mTitle = alertParams.mContext.getText(titleId);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.P.mTitle = title;
            return this;
        }

        public Builder setCustomTitle(View customTitleView) {
            this.P.mCustomTitleView = customTitleView;
            return this;
        }

        public Builder setMessage(int messageId) {
            AlertParams alertParams = this.P;
            alertParams.mMessage = alertParams.mContext.getText(messageId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.P.mMessage = message;
            return this;
        }

        public Builder setIcon(int iconId) {
            this.P.mIconId = iconId;
            return this;
        }

        public Builder setIcon(Drawable icon) {
            this.P.mIcon = icon;
            return this;
        }

        public Builder setIconAttribute(int attrId) {
            TypedValue out = new TypedValue();
            this.P.mContext.getTheme().resolveAttribute(attrId, out, true);
            this.P.mIconId = out.resourceId;
            return this;
        }

        public Builder setPositiveButton(int textId, OnClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mPositiveButtonText = alertParams.mContext.getText(textId);
            this.P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, OnClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mPositiveButtonText = text;
            alertParams.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(int textId, OnClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mNegativeButtonText = alertParams.mContext.getText(textId);
            this.P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mNegativeButtonText = text;
            alertParams.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(int textId, OnClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mNeutralButtonText = alertParams.mContext.getText(textId);
            this.P.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(CharSequence text, OnClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mNeutralButtonText = text;
            alertParams.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.P.mCancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            this.P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            this.P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            this.P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setItems(int itemsId, OnClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mItems = alertParams.mContext.getResources().getTextArray(itemsId);
            this.P.mOnClickListener = listener;
            return this;
        }

        public Builder setItems(CharSequence[] items, OnClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mItems = items;
            alertParams.mOnClickListener = listener;
            return this;
        }

        public Builder setAdapter(ListAdapter adapter, OnClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mAdapter = adapter;
            alertParams.mOnClickListener = listener;
            return this;
        }

        public Builder setCursor(Cursor cursor, OnClickListener listener, String labelColumn) {
            AlertParams alertParams = this.P;
            alertParams.mCursor = cursor;
            alertParams.mLabelColumn = labelColumn;
            alertParams.mOnClickListener = listener;
            return this;
        }

        public Builder setMultiChoiceItems(int itemsId, boolean[] checkedItems, OnMultiChoiceClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mItems = alertParams.mContext.getResources().getTextArray(itemsId);
            alertParams = this.P;
            alertParams.mOnCheckboxClickListener = listener;
            alertParams.mCheckedItems = checkedItems;
            alertParams.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, OnMultiChoiceClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mItems = items;
            alertParams.mOnCheckboxClickListener = listener;
            alertParams.mCheckedItems = checkedItems;
            alertParams.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn, OnMultiChoiceClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mCursor = cursor;
            alertParams.mOnCheckboxClickListener = listener;
            alertParams.mIsCheckedColumn = isCheckedColumn;
            alertParams.mLabelColumn = labelColumn;
            alertParams.mIsMultiChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(int itemsId, int checkedItem, OnClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mItems = alertParams.mContext.getResources().getTextArray(itemsId);
            alertParams = this.P;
            alertParams.mOnClickListener = listener;
            alertParams.mCheckedItem = checkedItem;
            alertParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn, OnClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mCursor = cursor;
            alertParams.mOnClickListener = listener;
            alertParams.mCheckedItem = checkedItem;
            alertParams.mLabelColumn = labelColumn;
            alertParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem, OnClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mItems = items;
            alertParams.mOnClickListener = listener;
            alertParams.mCheckedItem = checkedItem;
            alertParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem, OnClickListener listener) {
            AlertParams alertParams = this.P;
            alertParams.mAdapter = adapter;
            alertParams.mOnClickListener = listener;
            alertParams.mCheckedItem = checkedItem;
            alertParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setOnItemSelectedListener(OnItemSelectedListener listener) {
            this.P.mOnItemSelectedListener = listener;
            return this;
        }

        public Builder setView(int layoutResId) {
            AlertParams alertParams = this.P;
            alertParams.mView = null;
            alertParams.mViewLayoutResId = layoutResId;
            alertParams.mViewSpacingSpecified = false;
            return this;
        }

        public Builder setView(View view) {
            AlertParams alertParams = this.P;
            alertParams.mView = view;
            alertParams.mViewLayoutResId = 0;
            alertParams.mViewSpacingSpecified = false;
            return this;
        }

        @Deprecated
        @UnsupportedAppUsage
        public Builder setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
            AlertParams alertParams = this.P;
            alertParams.mView = view;
            alertParams.mViewLayoutResId = 0;
            alertParams.mViewSpacingSpecified = true;
            alertParams.mViewSpacingLeft = viewSpacingLeft;
            alertParams.mViewSpacingTop = viewSpacingTop;
            alertParams.mViewSpacingRight = viewSpacingRight;
            alertParams.mViewSpacingBottom = viewSpacingBottom;
            return this;
        }

        @Deprecated
        public Builder setInverseBackgroundForced(boolean useInverseBackground) {
            this.P.mForceInverseBackground = useInverseBackground;
            return this;
        }

        @UnsupportedAppUsage
        public Builder setRecycleOnMeasureEnabled(boolean enabled) {
            this.P.mRecycleOnMeasure = enabled;
            return this;
        }

        public AlertDialog create() {
            AlertDialog dialog = new AlertDialog(this.P.mContext, 0, false);
            this.P.apply(dialog.mAlert);
            dialog.setCancelable(this.P.mCancelable);
            if (this.P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(this.P.mOnCancelListener);
            dialog.setOnDismissListener(this.P.mOnDismissListener);
            if (this.P.mOnKeyListener != null) {
                dialog.setOnKeyListener(this.P.mOnKeyListener);
            }
            return dialog;
        }

        public AlertDialog show() {
            AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }

        static {
            Extension.get().bindOriginal(new Interface() {
                public void init(Builder builder, Context context, int i) {
                }
            });
        }
    }

    protected AlertDialog(Context context) {
        this(context, 0);
    }

    protected AlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        this(context, 0);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
        Android_App_AlertDialog.Extension ext = Android_App_AlertDialog.Extension.get();
        if (ext.getExtension() != null) {
            ((Android_App_AlertDialog.Interface) ext.getExtension().asInterface()).init(this, context, cancelable, cancelListener);
        }
    }

    protected AlertDialog(Context context, int themeResId) {
        this(context, themeResId, true);
    }

    AlertDialog(Context context, int themeResId, boolean createContextThemeWrapper) {
        super(context, createContextThemeWrapper ? resolveDialogTheme(context, themeResId) : 0, createContextThemeWrapper);
        this.mWindow.alwaysReadCloseOnTouchAttr();
        this.mAlert = AlertController.create(getContext(), this, getWindow());
        Android_App_AlertDialog.Extension ext = Android_App_AlertDialog.Extension.get();
        if (ext.getExtension() != null) {
            ((Android_App_AlertDialog.Interface) ext.getExtension().asInterface()).init(this, context, themeResId, createContextThemeWrapper);
        }
    }

    static int resolveDialogTheme(Context context, int themeResId) {
        if (themeResId == 1) {
            return R.style.Theme_Dialog_Alert;
        }
        if (themeResId == 2) {
            return R.style.Theme_Holo_Dialog_Alert;
        }
        if (themeResId == 3) {
            return R.style.Theme_Holo_Light_Dialog_Alert;
        }
        if (themeResId == 4) {
            return 16974545;
        }
        if (themeResId == 5) {
            return 16974546;
        }
        if (ResourceId.isValid(themeResId)) {
            return themeResId;
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(16843529, outValue, true);
        return outValue.resourceId;
    }

    public Button getButton(int whichButton) {
        return this.mAlert.getButton(whichButton);
    }

    public ListView getListView() {
        return this.mAlert.getListView();
    }

    public void setTitle(CharSequence title) {
        super.setTitle(title);
        this.mAlert.setTitle(title);
    }

    public void setCustomTitle(View customTitleView) {
        this.mAlert.setCustomTitle(customTitleView);
    }

    public void setMessage(CharSequence message) {
        this.mAlert.setMessage(message);
    }

    public void setMessageMovementMethod(MovementMethod movementMethod) {
        this.mAlert.setMessageMovementMethod(movementMethod);
    }

    public void setMessageHyphenationFrequency(int hyphenationFrequency) {
        this.mAlert.setMessageHyphenationFrequency(hyphenationFrequency);
    }

    public void setView(View view) {
        this.mAlert.setView(view);
    }

    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        this.mAlert.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
    }

    /* Access modifiers changed, original: 0000 */
    public void setButtonPanelLayoutHint(int layoutHint) {
        this.mAlert.setButtonPanelLayoutHint(layoutHint);
    }

    public void setButton(int whichButton, CharSequence text, Message msg) {
        this.mAlert.setButton(whichButton, text, null, msg);
    }

    public void setButton(int whichButton, CharSequence text, OnClickListener listener) {
        this.mAlert.setButton(whichButton, text, listener, null);
    }

    @Deprecated
    public void setButton(CharSequence text, Message msg) {
        setButton(-1, text, msg);
    }

    @Deprecated
    public void setButton2(CharSequence text, Message msg) {
        setButton(-2, text, msg);
    }

    @Deprecated
    public void setButton3(CharSequence text, Message msg) {
        setButton(-3, text, msg);
    }

    @Deprecated
    public void setButton(CharSequence text, OnClickListener listener) {
        setButton(-1, text, listener);
    }

    @Deprecated
    public void setButton2(CharSequence text, OnClickListener listener) {
        setButton(-2, text, listener);
    }

    @Deprecated
    public void setButton3(CharSequence text, OnClickListener listener) {
        setButton(-3, text, listener);
    }

    public void setIcon(int resId) {
        this.mAlert.setIcon(resId);
    }

    public void setIcon(Drawable icon) {
        this.mAlert.setIcon(icon);
    }

    public void setIconAttribute(int attrId) {
        TypedValue out = new TypedValue();
        this.mContext.getTheme().resolveAttribute(attrId, out, true);
        this.mAlert.setIcon(out.resourceId);
    }

    public void setInverseBackgroundForced(boolean forceInverseBackground) {
        this.mAlert.setInverseBackgroundForced(forceInverseBackground);
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAlert.installContent();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mAlert.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mAlert.onKeyUp(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    static {
        Android_App_AlertDialog.Extension.get().bindOriginal(new Android_App_AlertDialog.Interface() {
            public void init(AlertDialog alertDialog, Context context, int i, boolean b) {
            }

            public void init(AlertDialog alertDialog, Context context, boolean b, OnCancelListener onCancelListener) {
            }
        });
    }
}
