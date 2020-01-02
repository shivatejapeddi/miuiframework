package miui.content.pm;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.miui.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import miui.content.pm.IPackageDeleteConfirmObserver.Stub;

public class PackageDeleteConfirmActivity extends Activity implements OnClickListener {
    private static final int DELETE_MSG_NEXT_STEP_INTERVAL = 1000;
    private static final int DELETE_MSG_NEXT_STEP_WHAT = 100;
    public static final String EXTRA_PKGNAME = "extra_pkgname";
    private static final int MAX_STEPS = 3;
    private boolean delete = false;
    private CharSequence mAppLabel;
    private int mAutoNextStepTime = 5;
    private Button mCancelButton;
    private int mCurrentStep = 1;
    private Button mDeleteButton;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            PackageDeleteConfirmActivity.access$006(PackageDeleteConfirmActivity.this);
            if (PackageDeleteConfirmActivity.this.mCurrentStep == 3 && PackageDeleteConfirmActivity.this.mAutoNextStepTime == 0) {
                PackageDeleteConfirmActivity.this.mDeleteButton.setText((int) R.string.button_text_delete);
                PackageDeleteConfirmActivity.this.mDeleteButton.setEnabled(true);
            } else if (PackageDeleteConfirmActivity.this.mAutoNextStepTime == 0) {
                PackageDeleteConfirmActivity.this.mDeleteButton.setText((int) R.string.button_text_next_step);
                PackageDeleteConfirmActivity.this.mDeleteButton.setEnabled(true);
            } else {
                if (PackageDeleteConfirmActivity.this.mCurrentStep == 3) {
                    PackageDeleteConfirmActivity.this.mDeleteButton.setText((CharSequence) PackageDeleteConfirmActivity.this.getString(R.string.button_text_delete_timer, Integer.valueOf(PackageDeleteConfirmActivity.this.mAutoNextStepTime)));
                } else {
                    PackageDeleteConfirmActivity.this.mDeleteButton.setText((CharSequence) PackageDeleteConfirmActivity.this.getString(R.string.button_text_next_step_timer, Integer.valueOf(PackageDeleteConfirmActivity.this.mAutoNextStepTime)));
                }
                PackageDeleteConfirmActivity.this.mHandler.removeMessages(100);
                PackageDeleteConfirmActivity.this.mHandler.sendEmptyMessageDelayed(100, 1000);
            }
        }
    };
    private IPackageDeleteConfirmObserver mObs;
    private String mPkgName;
    private TextView mWarningInfoView;

    static /* synthetic */ int access$006(PackageDeleteConfirmActivity x0) {
        int i = x0.mAutoNextStepTime - 1;
        x0.mAutoNextStepTime = i;
        return i;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_package_delete_confirm);
        Intent intent = getIntent();
        try {
            this.mPkgName = intent.getStringExtra(EXTRA_PKGNAME);
        } catch (Exception e) {
            this.mPkgName = null;
        }
        if (TextUtils.isEmpty(this.mPkgName)) {
            finish();
            return;
        }
        this.mObs = Stub.asInterface((IBinder) intent.getExtra("observer"));
        this.mAppLabel = loadAppLabel();
        this.mWarningInfoView = (TextView) findViewById(R.id.warning_info);
        this.mCancelButton = (Button) findViewById(R.id.cancel);
        this.mCancelButton.setOnClickListener(this);
        this.mDeleteButton = (Button) findViewById(R.id.delete);
        this.mDeleteButton.setOnClickListener(this);
        this.mWarningInfoView.setText(getWarningInfo(this.mCurrentStep, this.mAppLabel));
        this.mDeleteButton.setText((CharSequence) getString(R.string.button_text_next_step_timer, Integer.valueOf(this.mAutoNextStepTime)));
        this.mDeleteButton.setEnabled(false);
        this.mHandler.sendEmptyMessageDelayed(100, 1000);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cancel) {
            this.mHandler.removeMessages(100);
            this.delete = false;
            finish();
        } else if (id == R.id.delete) {
            id = this.mCurrentStep;
            if (id == 3) {
                this.mHandler.removeMessages(100);
                this.delete = true;
                finish();
                return;
            }
            this.mCurrentStep = id + 1;
            this.mAutoNextStepTime = 5;
            this.mWarningInfoView.setText(getWarningInfo(this.mCurrentStep, this.mAppLabel));
            if (this.mCurrentStep == 3) {
                this.mDeleteButton.setText((CharSequence) getString(R.string.button_text_delete_timer, Integer.valueOf(this.mAutoNextStepTime)));
            } else {
                this.mDeleteButton.setText((CharSequence) getString(R.string.button_text_next_step_timer, Integer.valueOf(this.mAutoNextStepTime)));
            }
            this.mDeleteButton.setEnabled(false);
            this.mHandler.removeMessages(100);
            this.mHandler.sendEmptyMessageDelayed(100, 1000);
        }
    }

    public void onBackPressed() {
    }

    public void onStop() {
        this.mHandler.removeMessages(100);
        try {
            this.mObs.onConfirm(this.delete);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        super.onStop();
        finish();
    }

    private CharSequence loadAppLabel() {
        PackageManager pm = getPackageManager();
        try {
            return pm.getApplicationInfo(this.mPkgName, 0).loadLabel(pm);
        } catch (NameNotFoundException e) {
            return this.mPkgName;
        }
    }

    private String getWarningInfo(int step, CharSequence appLabel) {
        if (step == 1) {
            return getString(R.string.package_delete_confirm_step_1, appLabel);
        } else if (step == 2) {
            return getString(R.string.package_delete_confirm_step_2, appLabel);
        } else if (step != 3) {
            return null;
        } else {
            return getString(R.string.package_delete_confirm_step_3, appLabel);
        }
    }
}
