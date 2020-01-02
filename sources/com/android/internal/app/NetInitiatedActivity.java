package com.android.internal.app;

import android.annotation.UnsupportedAppUsage;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.R;
import com.android.internal.app.AlertController.AlertParams;
import com.android.internal.location.GpsNetInitiatedHandler;

public class NetInitiatedActivity extends AlertActivity implements OnClickListener {
    private static final boolean DEBUG = true;
    private static final int GPS_NO_RESPONSE_TIME_OUT = 1;
    private static final int NEGATIVE_BUTTON = -2;
    private static final int POSITIVE_BUTTON = -1;
    private static final String TAG = "NetInitiatedActivity";
    private static final boolean VERBOSE = false;
    private int default_response = -1;
    private int default_response_timeout = 6;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (NetInitiatedActivity.this.notificationId != -1) {
                    NetInitiatedActivity netInitiatedActivity = NetInitiatedActivity.this;
                    netInitiatedActivity.sendUserResponse(netInitiatedActivity.default_response);
                }
                NetInitiatedActivity.this.finish();
            }
        }
    };
    private BroadcastReceiver mNetInitiatedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("NetInitiatedReceiver onReceive: ");
            stringBuilder.append(intent.getAction());
            Log.d(NetInitiatedActivity.TAG, stringBuilder.toString());
            if (intent.getAction() == GpsNetInitiatedHandler.ACTION_NI_VERIFY) {
                NetInitiatedActivity.this.handleNIVerify(intent);
            }
        }
    };
    private int notificationId = -1;
    private int timeout = -1;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        AlertParams p = this.mAlertParams;
        Context context = getApplicationContext();
        p.mTitle = intent.getStringExtra("title");
        p.mMessage = intent.getStringExtra("message");
        p.mPositiveButtonText = String.format(context.getString(R.string.gpsVerifYes), new Object[0]);
        p.mPositiveButtonListener = this;
        p.mNegativeButtonText = String.format(context.getString(R.string.gpsVerifNo), new Object[0]);
        p.mNegativeButtonListener = this;
        this.notificationId = intent.getIntExtra("notif_id", -1);
        this.timeout = intent.getIntExtra(GpsNetInitiatedHandler.NI_INTENT_KEY_TIMEOUT, this.default_response_timeout);
        this.default_response = intent.getIntExtra(GpsNetInitiatedHandler.NI_INTENT_KEY_DEFAULT_RESPONSE, 1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onCreate() : notificationId: ");
        stringBuilder.append(this.notificationId);
        stringBuilder.append(" timeout: ");
        stringBuilder.append(this.timeout);
        stringBuilder.append(" default_response:");
        stringBuilder.append(this.default_response);
        Log.d(TAG, stringBuilder.toString());
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(1), (long) (this.timeout * 1000));
        setupAlert();
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        registerReceiver(this.mNetInitiatedReceiver, new IntentFilter(GpsNetInitiatedHandler.ACTION_NI_VERIFY));
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        unregisterReceiver(this.mNetInitiatedReceiver);
    }

    public void onClick(DialogInterface dialog, int which) {
        if (which == -1) {
            sendUserResponse(1);
        }
        if (which == -2) {
            sendUserResponse(2);
        }
        finish();
        this.notificationId = -1;
    }

    private void sendUserResponse(int response) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("sendUserResponse, response: ");
        stringBuilder.append(response);
        Log.d(TAG, stringBuilder.toString());
        ((LocationManager) getSystemService("location")).sendNiResponse(this.notificationId, response);
    }

    @UnsupportedAppUsage
    private void handleNIVerify(Intent intent) {
        this.notificationId = intent.getIntExtra("notif_id", -1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("handleNIVerify action: ");
        stringBuilder.append(intent.getAction());
        Log.d(TAG, stringBuilder.toString());
    }

    private void showNIError() {
        Toast.makeText((Context) this, (CharSequence) "NI error", 1).show();
    }
}
