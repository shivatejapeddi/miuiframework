package android.inputmethodservice;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.miui.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputConnection;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class InputMethodCloudClipboardPopupView extends PopupWindow implements OnClickListener {
    private static final String TAG = "CloudClipboardPopupView";
    private int mBottomHeight;
    private String mCloudContent;
    private Context mContext;
    private InputConnection mInputConnection;
    private boolean mIsLeft;

    public InputMethodCloudClipboardPopupView(Context context, InputConnection inputConnection, int bottomHeight, boolean isLeft, String cloudContent) {
        super(context);
        this.mIsLeft = isLeft;
        this.mContext = context;
        this.mBottomHeight = bottomHeight;
        this.mCloudContent = cloudContent;
        this.mInputConnection = inputConnection;
    }

    public void initPopupWindow() {
        setHeight(-1);
        setWidth(-1);
        setBackgroundDrawable(new ColorDrawable(0));
        View contentView = LayoutInflater.from(this.mContext).inflate((int) R.layout.input_method_cloud_clipboard_view, null, false);
        ((FrameLayout) contentView.findViewById(R.id.cloud_outside_view)).setOnClickListener(this);
        LinearLayout insideView = (LinearLayout) contentView.findViewById(R.id.cloud_inside_view);
        insideView.setOnClickListener(this);
        ((TextView) contentView.findViewById(R.id.cloud_content)).setText(this.mCloudContent);
        LayoutParams lp = (LayoutParams) insideView.getLayoutParams();
        if (this.mIsLeft) {
            lp.gravity = 8388691;
            insideView.setBackgroundResource(R.drawable.input_method_cloud_clipboard_pop_bg_left);
            setAnimationStyle(R.style.InputMethodWindowAnimationLeft);
        } else {
            lp.gravity = 8388693;
            insideView.setBackgroundResource(R.drawable.input_method_cloud_clipboard_pop_bg_right);
            setAnimationStyle(R.style.InputMethodWindowAnimationRight);
        }
        int shadowHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.input_method_pop_view_shadow_height);
        int marginBottom = this.mContext.getResources().getDimensionPixelSize(R.dimen.input_method_pop_view_margin_bottom);
        lp.bottomMargin = ((this.mBottomHeight - shadowHeight) + marginBottom) - this.mContext.getResources().getDimensionPixelSize(R.dimen.input_method_cloud_clipboard_bg_pop_margin);
        insideView.setLayoutParams(lp);
        setContentView(contentView);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cloud_inside_view /*285802557*/:
                this.mInputConnection.commitText(this.mCloudContent, 1);
                dismiss();
                return;
            case R.id.cloud_outside_view /*285802558*/:
                dismiss();
                return;
            default:
                return;
        }
    }
}
