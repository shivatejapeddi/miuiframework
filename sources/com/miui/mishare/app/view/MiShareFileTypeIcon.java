package com.miui.mishare.app.view;

import android.content.Context;
import android.miui.R;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.miui.mishare.app.util.MiShareFileHelper;
import com.miui.mishare.app.util.MiShareFileIconUtil;
import java.util.List;

public class MiShareFileTypeIcon extends FrameLayout {
    private ImageView mBottomView;
    private ImageView mTopView;

    public MiShareFileTypeIcon(Context context) {
        super(context);
        initView(context);
    }

    public MiShareFileTypeIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate((int) R.layout.view_file_type_icon, (ViewGroup) this);
        this.mTopView = (ImageView) findViewById(R.id.iv_top);
        this.mBottomView = (ImageView) findViewById(R.id.iv_bottom);
    }

    public void setFileTypes(List<Uri> files) {
        if (files == null || files.size() == 0) {
            setVisibility(8);
            return;
        }
        if (files.size() == 1) {
            Uri uri = (Uri) files.get(0);
            this.mBottomView.setVisibility(8);
            this.mTopView.setImageResource(MiShareFileIconUtil.getFileIconResId(MiShareFileHelper.getFileExtensionFromUri(getContext(), uri)));
        } else {
            this.mTopView.setImageResource(MiShareFileIconUtil.getFileIconResId(MiShareFileHelper.getFileExtensionFromUri(getContext(), (Uri) files.get(0))));
            String extSecond = MiShareFileHelper.getFileExtensionFromUri(getContext(), (Uri) files.get(1));
            this.mBottomView.setVisibility(0);
            this.mBottomView.setImageResource(MiShareFileIconUtil.getFileIconResId(extSecond));
        }
    }
}
