package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.internal.R;
import java.text.NumberFormat;

@Deprecated
public class ProgressDialog extends AlertDialog {
    public static final int STYLE_HORIZONTAL = 1;
    public static final int STYLE_SPINNER = 0;
    private boolean mHasStarted;
    private int mIncrementBy;
    private int mIncrementSecondaryBy;
    private boolean mIndeterminate;
    private Drawable mIndeterminateDrawable;
    private int mMax;
    private CharSequence mMessage;
    @UnsupportedAppUsage
    private TextView mMessageView;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private ProgressBar mProgress;
    private Drawable mProgressDrawable;
    @UnsupportedAppUsage
    private TextView mProgressNumber;
    private String mProgressNumberFormat;
    private TextView mProgressPercent;
    private NumberFormat mProgressPercentFormat;
    private int mProgressStyle = 0;
    private int mProgressVal;
    private int mSecondaryProgressVal;
    private Handler mViewUpdateHandler;

    public ProgressDialog(Context context) {
        super(context);
        initFormats();
    }

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
        initFormats();
    }

    private void initFormats() {
        this.mProgressNumberFormat = "%1d/%2d";
        this.mProgressPercentFormat = NumberFormat.getPercentInstance();
        this.mProgressPercentFormat.setMaximumFractionDigits(0);
    }

    public static ProgressDialog show(Context context, CharSequence title, CharSequence message) {
        return show(context, title, message, false);
    }

    public static ProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static ProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    public static ProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable, OnCancelListener cancelListener) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.show();
        return dialog;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(this.mContext);
        TypedArray a = this.mContext.obtainStyledAttributes(null, R.styleable.AlertDialog, 16842845, 0);
        View view;
        if (this.mProgressStyle == 1) {
            this.mViewUpdateHandler = new Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    int progress = ProgressDialog.this.mProgress.getProgress();
                    int max = ProgressDialog.this.mProgress.getMax();
                    CharSequence charSequence = "";
                    if (ProgressDialog.this.mProgressNumberFormat != null) {
                        String format = ProgressDialog.this.mProgressNumberFormat;
                        ProgressDialog.this.mProgressNumber.setText(String.format(format, new Object[]{Integer.valueOf(progress), Integer.valueOf(max)}));
                    } else {
                        ProgressDialog.this.mProgressNumber.setText(charSequence);
                    }
                    if (ProgressDialog.this.mProgressPercentFormat != null) {
                        CharSequence tmp = new SpannableString(ProgressDialog.this.mProgressPercentFormat.format(((double) progress) / ((double) max)));
                        tmp.setSpan(new StyleSpan(1), 0, tmp.length(), 33);
                        ProgressDialog.this.mProgressPercent.setText(tmp);
                        return;
                    }
                    ProgressDialog.this.mProgressPercent.setText(charSequence);
                }
            };
            view = inflater.inflate(a.getResourceId(13, R.layout.alert_dialog_progress), null);
            this.mProgress = (ProgressBar) view.findViewById(16908301);
            this.mProgressNumber = (TextView) view.findViewById(R.id.progress_number);
            this.mProgressPercent = (TextView) view.findViewById(R.id.progress_percent);
            setView(view);
        } else {
            view = inflater.inflate(a.getResourceId(18, R.layout.progress_dialog), null);
            this.mProgress = (ProgressBar) view.findViewById(16908301);
            this.mMessageView = (TextView) view.findViewById(16908299);
            setView(view);
        }
        a.recycle();
        int i = this.mMax;
        if (i > 0) {
            setMax(i);
        }
        i = this.mProgressVal;
        if (i > 0) {
            setProgress(i);
        }
        i = this.mSecondaryProgressVal;
        if (i > 0) {
            setSecondaryProgress(i);
        }
        i = this.mIncrementBy;
        if (i > 0) {
            incrementProgressBy(i);
        }
        i = this.mIncrementSecondaryBy;
        if (i > 0) {
            incrementSecondaryProgressBy(i);
        }
        Drawable drawable = this.mProgressDrawable;
        if (drawable != null) {
            setProgressDrawable(drawable);
        }
        drawable = this.mIndeterminateDrawable;
        if (drawable != null) {
            setIndeterminateDrawable(drawable);
        }
        CharSequence charSequence = this.mMessage;
        if (charSequence != null) {
            setMessage(charSequence);
        }
        setIndeterminate(this.mIndeterminate);
        onProgressChanged();
        super.onCreate(savedInstanceState);
    }

    public void onStart() {
        super.onStart();
        this.mHasStarted = true;
    }

    /* Access modifiers changed, original: protected */
    public void onStop() {
        super.onStop();
        this.mHasStarted = false;
    }

    public void setProgress(int value) {
        if (this.mHasStarted) {
            this.mProgress.setProgress(value);
            onProgressChanged();
            return;
        }
        this.mProgressVal = value;
    }

    public void setSecondaryProgress(int secondaryProgress) {
        ProgressBar progressBar = this.mProgress;
        if (progressBar != null) {
            progressBar.setSecondaryProgress(secondaryProgress);
            onProgressChanged();
            return;
        }
        this.mSecondaryProgressVal = secondaryProgress;
    }

    public int getProgress() {
        ProgressBar progressBar = this.mProgress;
        if (progressBar != null) {
            return progressBar.getProgress();
        }
        return this.mProgressVal;
    }

    public int getSecondaryProgress() {
        ProgressBar progressBar = this.mProgress;
        if (progressBar != null) {
            return progressBar.getSecondaryProgress();
        }
        return this.mSecondaryProgressVal;
    }

    public int getMax() {
        ProgressBar progressBar = this.mProgress;
        if (progressBar != null) {
            return progressBar.getMax();
        }
        return this.mMax;
    }

    public void setMax(int max) {
        ProgressBar progressBar = this.mProgress;
        if (progressBar != null) {
            progressBar.setMax(max);
            onProgressChanged();
            return;
        }
        this.mMax = max;
    }

    public void incrementProgressBy(int diff) {
        ProgressBar progressBar = this.mProgress;
        if (progressBar != null) {
            progressBar.incrementProgressBy(diff);
            onProgressChanged();
            return;
        }
        this.mIncrementBy += diff;
    }

    public void incrementSecondaryProgressBy(int diff) {
        ProgressBar progressBar = this.mProgress;
        if (progressBar != null) {
            progressBar.incrementSecondaryProgressBy(diff);
            onProgressChanged();
            return;
        }
        this.mIncrementSecondaryBy += diff;
    }

    public void setProgressDrawable(Drawable d) {
        ProgressBar progressBar = this.mProgress;
        if (progressBar != null) {
            progressBar.setProgressDrawable(d);
        } else {
            this.mProgressDrawable = d;
        }
    }

    public void setIndeterminateDrawable(Drawable d) {
        ProgressBar progressBar = this.mProgress;
        if (progressBar != null) {
            progressBar.setIndeterminateDrawable(d);
        } else {
            this.mIndeterminateDrawable = d;
        }
    }

    public void setIndeterminate(boolean indeterminate) {
        ProgressBar progressBar = this.mProgress;
        if (progressBar != null) {
            progressBar.setIndeterminate(indeterminate);
        } else {
            this.mIndeterminate = indeterminate;
        }
    }

    public boolean isIndeterminate() {
        ProgressBar progressBar = this.mProgress;
        if (progressBar != null) {
            return progressBar.isIndeterminate();
        }
        return this.mIndeterminate;
    }

    public void setMessage(CharSequence message) {
        if (this.mProgress == null) {
            this.mMessage = message;
        } else if (this.mProgressStyle == 1) {
            super.setMessage(message);
        } else {
            this.mMessageView.setText(message);
        }
    }

    public void setProgressStyle(int style) {
        this.mProgressStyle = style;
    }

    public void setProgressNumberFormat(String format) {
        this.mProgressNumberFormat = format;
        onProgressChanged();
    }

    public void setProgressPercentFormat(NumberFormat format) {
        this.mProgressPercentFormat = format;
        onProgressChanged();
    }

    private void onProgressChanged() {
        if (this.mProgressStyle == 1) {
            Handler handler = this.mViewUpdateHandler;
            if (handler != null && !handler.hasMessages(0)) {
                this.mViewUpdateHandler.sendEmptyMessage(0);
            }
        }
    }
}
