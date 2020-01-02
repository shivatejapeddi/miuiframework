package com.miui.mishare.app.view;

import android.content.Context;
import android.miui.R;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MiShareDeviceView extends LinearLayout {
    public static final int DEVICE_STATUS_FAILED = 3;
    public static final int DEVICE_STATUS_IDLE = 1;
    public static final int DEVICE_STATUS_SENDING = 2;
    public static final int DEVICE_STATUS_SUCCESS = 4;
    public static final int TYPE_OPPO = 3;
    public static final int TYPE_REALME = 4;
    public static final int TYPE_VIVO = 2;
    public static final int TYPE_XIAOMI = 1;
    private View mDeviceIcon;
    private TextView mDeviceModelNameTv;
    private String mDeviceName;
    private TextView mDeviceNameTv;
    private View mDivider;
    private View mDividerEnd;
    private View mDividerStart;
    private View mPcView;
    private View mPhoneView;
    private ImageView mStatusIv;

    public MiShareDeviceView(Context context) {
        super(context);
        initView();
    }

    public MiShareDeviceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate((int) R.layout.view_midrop_device_view, (ViewGroup) this);
        this.mDeviceIcon = findViewById(R.id.rl_device_icon);
        this.mDeviceNameTv = (TextView) findViewById(R.id.tv_device_name);
        this.mDeviceModelNameTv = (TextView) findViewById(R.id.tv_device_model);
        this.mStatusIv = (ImageView) findViewById(R.id.iv_sending_status);
        this.mPhoneView = findViewById(R.id.view_phone);
        this.mPcView = findViewById(R.id.iv_pc);
        this.mDividerStart = findViewById(R.id.view_divider_start);
        this.mDivider = findViewById(R.id.view_divider);
        this.mDividerEnd = findViewById(R.id.view_divider_end);
    }

    public View getIconView() {
        return this.mDeviceIcon;
    }

    public void setDeviceType(int deviceType) {
        if (deviceType == 1) {
            this.mDeviceIcon.setBackgroundResource(R.drawable.bg_scanned_device_xiaomi);
        } else if (deviceType == 2) {
            this.mDeviceIcon.setBackgroundResource(R.drawable.bg_scanned_device_vivo);
        } else if (deviceType == 3) {
            this.mDeviceIcon.setBackgroundResource(R.drawable.bg_scanned_device_oppo);
        } else if (deviceType != 4) {
            this.mDeviceIcon.setBackgroundResource(R.drawable.bg_scanned_device_xiaomi);
        } else {
            this.mDeviceIcon.setBackgroundResource(R.drawable.bg_scanned_device_realme);
        }
    }

    public void showDividerStart(boolean show) {
        this.mDividerStart.setVisibility(show ? 0 : 8);
    }

    public void showDivider(boolean show) {
        this.mDivider.setVisibility(show ? 0 : 8);
    }

    public void showDividerEnd(boolean show) {
        this.mDividerEnd.setVisibility(show ? 0 : 8);
    }

    public void setDevicePhoneOrPc(boolean phone) {
        if (phone) {
            this.mPhoneView.setVisibility(0);
            this.mPcView.setVisibility(8);
            return;
        }
        this.mPhoneView.setVisibility(8);
        this.mPcView.setVisibility(0);
    }

    public void setDeviceName(String name, boolean hasMore) {
        this.mDeviceName = name;
        if (hasMore) {
            this.mDeviceNameTv.setText(getResources().getString(R.string.device_name_with_ellipsize, name));
            return;
        }
        if (TextUtils.isEmpty(name)) {
            this.mDeviceNameTv.setVisibility(8);
        } else {
            this.mDeviceNameTv.setVisibility(0);
            this.mDeviceNameTv.setText((CharSequence) name);
        }
    }

    public void setDeviceModelName(String modelName) {
        this.mDeviceModelNameTv.setText((CharSequence) modelName);
        if (TextUtils.isEmpty(this.mDeviceName)) {
            this.mDeviceModelNameTv.setMaxLines(2);
        } else {
            this.mDeviceModelNameTv.setMaxLines(1);
        }
        this.mDeviceModelNameTv.setEllipsize(TruncateAt.END);
    }

    public void setDeviceStatus(int status) {
        if (status == 1) {
            this.mStatusIv.setVisibility(8);
        } else if (status == 2) {
            this.mStatusIv.setVisibility(0);
            this.mStatusIv.setImageResource(R.drawable.ic_device_sending);
        } else if (status == 3) {
            this.mStatusIv.setVisibility(0);
            this.mStatusIv.setImageResource(R.drawable.ic_device_send_retry);
        } else if (status != 4) {
            this.mStatusIv.setVisibility(8);
        } else {
            this.mStatusIv.setVisibility(0);
            this.mStatusIv.setImageResource(R.drawable.ic_device_send_success);
        }
    }
}
