package android.preference;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.preference.Preference.BaseSavedState;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.android.internal.R;
import com.miui.internal.variable.api.v29.Android_Preference_EditTextPreference.Extension;
import com.miui.internal.variable.api.v29.Android_Preference_EditTextPreference.Interface;

@Deprecated
public class EditTextPreference extends DialogPreference {
    @UnsupportedAppUsage
    private EditText mEditText;
    private String mText;
    private boolean mTextSet;

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        String text;

        public SavedState(Parcel source) {
            super(source);
            this.text = source.readString();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.text);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }

    public EditTextPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mEditText = new EditText(context, attrs);
        this.mEditText.setId(16908291);
        this.mEditText.setEnabled(true);
    }

    public EditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EditTextPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 16842898);
    }

    public EditTextPreference(Context context) {
        this(context, null);
    }

    public void setText(String text) {
        boolean changed = TextUtils.equals(this.mText, text) ^ true;
        if (changed || !this.mTextSet) {
            this.mText = text;
            this.mTextSet = true;
            persistString(text);
            if (changed) {
                notifyDependencyChange(shouldDisableDependents());
                notifyChanged();
            }
        }
    }

    public String getText() {
        return this.mText;
    }

    /* Access modifiers changed, original: protected */
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);
        EditText editText = this.mEditText;
        editText.setText((CharSequence) getText());
        View oldParent = editText.getParent();
        if (oldParent != view) {
            if (oldParent != null) {
                ((ViewGroup) oldParent).removeView(editText);
            }
            onAddEditTextToDialogView(view, editText);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAddEditTextToDialogView(View dialogView, EditText editText) {
        if (Extension.get().getExtension() != null) {
            ((Interface) Extension.get().getExtension().asInterface()).onAddEditTextToDialogView(this, dialogView, editText);
        } else {
            originalOnAddEditTextToDialogView(dialogView, editText);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void originalOnAddEditTextToDialogView(View dialogView, EditText editText) {
        ViewGroup container = (ViewGroup) dialogView.findViewById(R.id.edittext_container);
        if (container != null) {
            container.addView((View) editText, -1, -2);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            String value = this.mEditText.getText().toString();
            if (callChangeListener(value)) {
                setText(value);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    /* Access modifiers changed, original: protected */
    public void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setText(restoreValue ? getPersistedString(this.mText) : (String) defaultValue);
    }

    public boolean shouldDisableDependents() {
        return TextUtils.isEmpty(this.mText) || super.shouldDisableDependents();
    }

    public EditText getEditText() {
        return this.mEditText;
    }

    /* Access modifiers changed, original: protected */
    public boolean needInputMethod() {
        return true;
    }

    /* Access modifiers changed, original: protected */
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }
        SavedState myState = new SavedState(superState);
        myState.text = getText();
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
        setText(myState.text);
    }

    static {
        Extension.get().bindOriginal(new Interface() {
            public void onAddEditTextToDialogView(EditTextPreference editTextPreference, View view, EditText editText) {
                editTextPreference.originalOnAddEditTextToDialogView(view, editText);
            }
        });
    }
}
