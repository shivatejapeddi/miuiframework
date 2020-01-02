package android.preference;

import android.annotation.UnsupportedAppUsage;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.preference.Preference.BaseSavedState;
import android.preference.PreferenceManager.OnActivityDestroyListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.android.internal.R;

@Deprecated
public abstract class DialogPreference extends Preference implements OnClickListener, OnDismissListener, OnActivityDestroyListener {
    @UnsupportedAppUsage
    private Builder mBuilder;
    @UnsupportedAppUsage
    private Dialog mDialog;
    @UnsupportedAppUsage
    private Drawable mDialogIcon;
    private int mDialogLayoutResId;
    @UnsupportedAppUsage
    private CharSequence mDialogMessage;
    @UnsupportedAppUsage
    private CharSequence mDialogTitle;
    private final Runnable mDismissRunnable;
    @UnsupportedAppUsage
    private CharSequence mNegativeButtonText;
    @UnsupportedAppUsage
    private CharSequence mPositiveButtonText;
    @UnsupportedAppUsage
    private int mWhichButtonClicked;

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        Bundle dialogBundle;
        boolean isDialogShowing;

        public SavedState(Parcel source) {
            super(source);
            boolean z = true;
            if (source.readInt() != 1) {
                z = false;
            }
            this.isDialogShowing = z;
            this.dialogBundle = source.readBundle();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.isDialogShowing);
            dest.writeBundle(this.dialogBundle);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }

    public DialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mDismissRunnable = new Runnable() {
            public void run() {
                DialogPreference.this.mDialog.dismiss();
            }
        };
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DialogPreference, defStyleAttr, defStyleRes);
        this.mDialogTitle = a.getString(0);
        if (this.mDialogTitle == null) {
            this.mDialogTitle = getTitle();
        }
        this.mDialogMessage = a.getString(1);
        this.mDialogIcon = a.getDrawable(2);
        this.mPositiveButtonText = a.getString(3);
        this.mNegativeButtonText = a.getString(4);
        this.mDialogLayoutResId = a.getResourceId(5, this.mDialogLayoutResId);
        a.recycle();
    }

    public DialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DialogPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 16842897);
    }

    public DialogPreference(Context context) {
        this(context, null);
    }

    public void setDialogTitle(CharSequence dialogTitle) {
        this.mDialogTitle = dialogTitle;
    }

    public void setDialogTitle(int dialogTitleResId) {
        setDialogTitle(getContext().getString(dialogTitleResId));
    }

    public CharSequence getDialogTitle() {
        return this.mDialogTitle;
    }

    public void setDialogMessage(CharSequence dialogMessage) {
        this.mDialogMessage = dialogMessage;
    }

    public void setDialogMessage(int dialogMessageResId) {
        setDialogMessage(getContext().getString(dialogMessageResId));
    }

    public CharSequence getDialogMessage() {
        return this.mDialogMessage;
    }

    public void setDialogIcon(Drawable dialogIcon) {
        this.mDialogIcon = dialogIcon;
    }

    public void setDialogIcon(int dialogIconRes) {
        this.mDialogIcon = getContext().getDrawable(dialogIconRes);
    }

    public Drawable getDialogIcon() {
        return this.mDialogIcon;
    }

    public void setPositiveButtonText(CharSequence positiveButtonText) {
        this.mPositiveButtonText = positiveButtonText;
    }

    public void setPositiveButtonText(int positiveButtonTextResId) {
        setPositiveButtonText(getContext().getString(positiveButtonTextResId));
    }

    public CharSequence getPositiveButtonText() {
        return this.mPositiveButtonText;
    }

    public void setNegativeButtonText(CharSequence negativeButtonText) {
        this.mNegativeButtonText = negativeButtonText;
    }

    public void setNegativeButtonText(int negativeButtonTextResId) {
        setNegativeButtonText(getContext().getString(negativeButtonTextResId));
    }

    public CharSequence getNegativeButtonText() {
        return this.mNegativeButtonText;
    }

    public void setDialogLayoutResource(int dialogLayoutResId) {
        this.mDialogLayoutResId = dialogLayoutResId;
    }

    public int getDialogLayoutResource() {
        return this.mDialogLayoutResId;
    }

    /* Access modifiers changed, original: protected */
    public void onPrepareDialogBuilder(Builder builder) {
    }

    /* Access modifiers changed, original: protected */
    public void onClick() {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            showDialog(null);
        }
    }

    /* Access modifiers changed, original: protected */
    public void showDialog(Bundle state) {
        Context context = getContext();
        this.mWhichButtonClicked = -2;
        this.mBuilder = new Builder(context).setTitle(this.mDialogTitle).setIcon(this.mDialogIcon).setPositiveButton(this.mPositiveButtonText, (OnClickListener) this).setNegativeButton(this.mNegativeButtonText, (OnClickListener) this);
        View contentView = onCreateDialogView();
        if (contentView != null) {
            onBindDialogView(contentView);
            this.mBuilder.setView(contentView);
        } else {
            this.mBuilder.setMessage(this.mDialogMessage);
        }
        onPrepareDialogBuilder(this.mBuilder);
        getPreferenceManager().registerOnActivityDestroyListener(this);
        Dialog dialog = this.mBuilder.create();
        this.mDialog = dialog;
        if (state != null) {
            dialog.onRestoreInstanceState(state);
        }
        if (needInputMethod()) {
            requestInputMethod(dialog);
        }
        dialog.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialog) {
                DialogPreference.this.removeDismissCallbacks();
            }
        });
        dialog.setOnDismissListener(this);
        dialog.show();
    }

    private View getDecorView() {
        Dialog dialog = this.mDialog;
        if (dialog == null || dialog.getWindow() == null) {
            return null;
        }
        return this.mDialog.getWindow().getDecorView();
    }

    /* Access modifiers changed, original: 0000 */
    public void postDismiss() {
        removeDismissCallbacks();
        View decorView = getDecorView();
        if (decorView != null) {
            decorView.post(this.mDismissRunnable);
        }
    }

    private void removeDismissCallbacks() {
        View decorView = getDecorView();
        if (decorView != null) {
            decorView.removeCallbacks(this.mDismissRunnable);
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean needInputMethod() {
        return false;
    }

    private void requestInputMethod(Dialog dialog) {
        dialog.getWindow().setSoftInputMode(5);
    }

    /* Access modifiers changed, original: protected */
    public View onCreateDialogView() {
        if (this.mDialogLayoutResId == 0) {
            return null;
        }
        return LayoutInflater.from(this.mBuilder.getContext()).inflate(this.mDialogLayoutResId, null);
    }

    /* Access modifiers changed, original: protected */
    public void onBindDialogView(View view) {
        View dialogMessageView = view.findViewById(16908299);
        if (dialogMessageView != null) {
            CharSequence message = getDialogMessage();
            int newVisibility = 8;
            if (!TextUtils.isEmpty(message)) {
                if (dialogMessageView instanceof TextView) {
                    ((TextView) dialogMessageView).setText(message);
                }
                newVisibility = 0;
            }
            if (dialogMessageView.getVisibility() != newVisibility) {
                dialogMessageView.setVisibility(newVisibility);
            }
        }
    }

    public void onClick(DialogInterface dialog, int which) {
        this.mWhichButtonClicked = which;
    }

    public void onDismiss(DialogInterface dialog) {
        removeDismissCallbacks();
        getPreferenceManager().unregisterOnActivityDestroyListener(this);
        this.mDialog = null;
        onDialogClosed(this.mWhichButtonClicked == -1);
    }

    /* Access modifiers changed, original: protected */
    public void onDialogClosed(boolean positiveResult) {
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    public void onActivityDestroy() {
        Dialog dialog = this.mDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mDialog.dismiss();
        }
    }

    /* Access modifiers changed, original: protected */
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            return superState;
        }
        SavedState myState = new SavedState(superState);
        myState.isDialogShowing = true;
        myState.dialogBundle = this.mDialog.onSaveInstanceState();
        return myState;
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        if (myState.isDialogShowing) {
            showDialog(myState.dialogBundle);
        }
    }
}
