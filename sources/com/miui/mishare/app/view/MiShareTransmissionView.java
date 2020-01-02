package com.miui.mishare.app.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipDescription;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.media.AudioSystem;
import android.miui.R;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.print.PrintManager;
import android.provider.MiuiSettings.Secure;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.widget.LinearLayoutManager;
import com.android.internal.widget.RecyclerView;
import com.miui.mishare.DeviceModel;
import com.miui.mishare.DeviceModel.Oppo;
import com.miui.mishare.DeviceModel.Vivo;
import com.miui.mishare.IMiShareDiscoverCallback.Stub;
import com.miui.mishare.IMiShareStateListener;
import com.miui.mishare.IMiShareTaskStateListener;
import com.miui.mishare.IScreenThrowListener;
import com.miui.mishare.MiShareTask;
import com.miui.mishare.RemoteDevice;
import com.miui.mishare.app.adapter.MiShareDeviceAdapter;
import com.miui.mishare.app.adapter.MiShareDeviceAdapter.OnDeviceClickListener;
import com.miui.mishare.app.connect.MiShareConnectivity;
import com.miui.mishare.app.connect.MiShareConnectivity.ServiceBindCallBack;
import com.miui.mishare.app.model.MiShareDevice;
import com.miui.mishare.app.util.MiShareFileHelper;
import com.miui.mishare.app.util.PrintHelper;
import com.miui.mishare.app.util.ScreenUtil;
import java.util.List;

public class MiShareTransmissionView extends LinearLayout implements OnClickListener, OnDeviceClickListener, ServiceBindCallBack {
    private static final String MI_APP_STORE_AUTHORITY = "com.xiaomi.market.fileprovider";
    private static final String PRINT_ACTION = "com.miui.mishare.action.PRINT_CONTROL";
    private static final String SETTINGS_NAME_SCREEN_PROJECT_IN_SCREENING = "screen_project_in_screening";
    private static final String TAG = "MiShareTransmissionView";
    private MiShareDeviceAdapter mAdapter;
    private MiShareConnectivity mConnectivity;
    private ScreenStateContentObserver mContentObserver;
    private RecyclerView mDeviceLv;
    private final Stub mDiscoverCallback = new Stub() {
        public void onDeviceLost(final String deviceId) throws RemoteException {
            MiShareTransmissionView.this.mHandler.post(new Runnable() {
                public void run() {
                    MiShareTransmissionView.this.mAdapter.removeDevice(deviceId);
                    MiShareTransmissionView.this.mAdapter.notifyDataSetChanged();
                    if (MiShareTransmissionView.this.mAdapter.getItemCount() == 0) {
                        MiShareTransmissionView.this.setDiscoveringView();
                    }
                }
            });
        }

        public void onDeviceUpdated(final RemoteDevice remoteDevice) throws RemoteException {
            MiShareTransmissionView.this.mHandler.post(new Runnable() {
                public void run() {
                    RemoteDevice remoteDevice = remoteDevice;
                    if (remoteDevice != null && remoteDevice.getExtras() != null) {
                        Bundle bundle = remoteDevice.getExtras();
                        bundle.setClassLoader(getClass().getClassLoader());
                        MiShareDevice device = new MiShareDevice(bundle.getInt(RemoteDevice.KEY_SUPPORTED_GUIDING_NETWORK_TYPE));
                        device.files = MiShareTransmissionView.this.mFiles;
                        RemoteDevice remoteDevice2 = remoteDevice;
                        device.remoteDevice = remoteDevice2;
                        device.deviceId = remoteDevice2.getDeviceId();
                        Context context = MiShareTransmissionView.this.getContext();
                        String str = RemoteDevice.KEY_MANUFACTURE_CODE;
                        device.deviceModelName = DeviceModel.getDeviceName(context, bundle.getByte(str), bundle.getInt(RemoteDevice.KEY_DEVICE_CODE));
                        device.deviceType = MiShareTransmissionView.this.getDeviceType(bundle.getByte(str));
                        device.deviceName = bundle.getString(RemoteDevice.KEY_NICKNAME);
                        MiShareTransmissionView.this.mMiShareDisabled.setVisibility(8);
                        MiShareTransmissionView.this.mDeviceLv.setVisibility(0);
                        MiShareTransmissionView.this.mAdapter.addOrUpdateDevice(device);
                        MiShareTransmissionView.this.mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };
    private View mEnableMiShareView;
    private ViewGroup mFileTypeContainer;
    private List<Uri> mFiles;
    private Handler mHandler = new Handler();
    private HasNoFilesListener mHasNoFilesListener;
    private Intent mIntent;
    private boolean mIsScreenThrowConnected;
    private OnClickListener mMiPrintClick;
    private View mMiShareDisabled;
    private ImageButton mPrintBtn;
    private ImageButton mScreenThrow;
    private OnClickListener mScreenThrowClick;
    private IScreenThrowListener mScreenThrowListener = new IScreenThrowListener.Stub() {
        public void onConnectSuccess() throws RemoteException {
            ((Activity) MiShareTransmissionView.this.getContext()).runOnUiThread(new Runnable() {
                public void run() {
                    Log.d(MiShareTransmissionView.TAG, "onScreenConnectSuccess");
                    MiShareTransmissionView.this.setScreenThrowHighLight(true);
                }
            });
        }

        public void onConnectFail() throws RemoteException {
            ((Activity) MiShareTransmissionView.this.getContext()).runOnUiThread(new Runnable() {
                public void run() {
                    Log.d(MiShareTransmissionView.TAG, "onScreenConnectFail");
                    MiShareTransmissionView.this.setScreenThrowHighLight(false);
                }
            });
        }
    };
    private MiShareTaskStateReceiver mShareTaskStateReceive;
    private int mState = 0;
    private IMiShareStateListener mStateListener = new IMiShareStateListener.Stub() {
        public void onStateChanged(final int newState) throws RemoteException {
            ((Activity) MiShareTransmissionView.this.getContext()).runOnUiThread(new Runnable() {
                public void run() {
                    MiShareTransmissionView.this.refreshView(MiShareTransmissionView.this.mState, newState);
                    MiShareTransmissionView.this.mState = newState;
                }
            });
        }
    };
    private final IMiShareTaskStateListener.Stub mTaskStateListener = new IMiShareTaskStateListener.Stub() {
        public void onTaskStateChanged(String taskId, int state) throws RemoteException {
        }

        public void onTaskIdChanged(final MiShareTask task) throws RemoteException {
            MiShareTransmissionView.this.mHandler.post(new Runnable() {
                public void run() {
                    MiShareTransmissionView.this.mAdapter.replaceTaskId(task);
                }
            });
        }
    };
    private TextView mTipsTv;

    public interface HasNoFilesListener {
        void hasNoFiles();
    }

    private final class MiShareTaskStateReceiver extends BroadcastReceiver {
        private MiShareTaskStateReceiver() {
        }

        /* synthetic */ MiShareTaskStateReceiver(MiShareTransmissionView x0, AnonymousClass1 x1) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (MiShareConnectivity.ACTION_TASK_STATE.equals(intent.getAction())) {
                    MiShareTransmissionView.this.mAdapter.setDeviceState(intent.getStringExtra(MiShareConnectivity.EXTRA_MISHARE_DEVICE), intent.getIntExtra("state", 1));
                }
            }
        }
    }

    private class ScreenStateContentObserver extends ContentObserver {
        public ScreenStateContentObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            MiShareTransmissionView miShareTransmissionView = MiShareTransmissionView.this;
            miShareTransmissionView.mIsScreenThrowConnected = Secure.getBoolean(miShareTransmissionView.getContext().getContentResolver(), "screen_project_in_screening", false);
            miShareTransmissionView = MiShareTransmissionView.this;
            miShareTransmissionView.setScreenThrowHighLight(miShareTransmissionView.mIsScreenThrowConnected);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Screening is ");
            stringBuilder.append(MiShareTransmissionView.this.mIsScreenThrowConnected);
            Log.d(MiShareTransmissionView.TAG, stringBuilder.toString());
        }
    }

    public MiShareTransmissionView(Context context) {
        super(context);
        init();
    }

    public MiShareTransmissionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate((int) R.layout.view_midrop_transmission, (ViewGroup) this);
        this.mConnectivity = MiShareConnectivity.getInstance(getContext());
        this.mFileTypeContainer = (ViewGroup) findViewById(R.id.ll_file_type_container);
        this.mTipsTv = (TextView) findViewById(R.id.tv_prompt_tips);
        this.mScreenThrow = (ImageButton) findViewById(R.id.iv_throwing_screen);
        this.mScreenThrow.setOnClickListener(this);
        initScreenView();
        this.mPrintBtn = (ImageButton) findViewById(R.id.iv_printer);
        this.mPrintBtn.setOnClickListener(this);
        this.mPrintBtn.setVisibility(hasPrinter() ? 0 : 8);
        this.mMiShareDisabled = findViewById(R.id.rl_prompt_switch);
        this.mEnableMiShareView = findViewById(R.id.btn_enable_midrop);
        this.mAdapter = new MiShareDeviceAdapter(getContext());
        this.mAdapter.registerListener(this);
        this.mDeviceLv = (RecyclerView) findViewById(R.id.lv_scanned_device);
        this.mDeviceLv.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.mDeviceLv.setAdapter(this.mAdapter);
        this.mEnableMiShareView.setOnClickListener(this);
        initMiShareStatus();
        registerStateReceiver();
    }

    private void registerStateReceiver() {
        this.mShareTaskStateReceive = new MiShareTaskStateReceiver(this, null);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MiShareConnectivity.ACTION_TASK_STATE);
        getContext().registerReceiver(this.mShareTaskStateReceive, filter);
    }

    private void unregisterStateReceiver() {
        if (this.mShareTaskStateReceive != null) {
            getContext().unregisterReceiver(this.mShareTaskStateReceive);
        }
    }

    public void showFileDetailGroup(boolean show) {
        this.mFileTypeContainer.setVisibility(show ? 0 : 8);
    }

    private void initMiShareStatus() {
        this.mMiShareDisabled.setVisibility(0);
        this.mDeviceLv.setVisibility(8);
    }

    private void initScreenView() {
        String str = "screen_project_in_screening";
        this.mIsScreenThrowConnected = Secure.getBoolean(getContext().getContentResolver(), str, false);
        setScreenThrowHighLight(this.mIsScreenThrowConnected);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("initScreenView is ");
        stringBuilder.append(this.mIsScreenThrowConnected);
        Log.d(TAG, stringBuilder.toString());
        this.mContentObserver = new ScreenStateContentObserver(this.mHandler);
        getContext().getContentResolver().registerContentObserver(Settings.Secure.getUriFor(str), true, this.mContentObserver);
    }

    private void refreshView(int oldState, int newState) {
        boolean enabling = true;
        boolean disabling = true;
        switch (oldState) {
            case 1:
            case 2:
            case 3:
            case 7:
                disabling = false;
                break;
            case 4:
            case 5:
            case 6:
                enabling = false;
                break;
            default:
                return;
        }
        switch (newState) {
            case 1:
            case 2:
            case 3:
            case 7:
                enabling = false;
                break;
            case 4:
            case 5:
            case 6:
                disabling = false;
                break;
            default:
                return;
        }
        if (enabling) {
            setDiscoveringView();
        } else if (disabling) {
            this.mAdapter.stopSort();
            this.mMiShareDisabled.setVisibility(0);
            this.mDeviceLv.setVisibility(8);
            this.mTipsTv.setText((int) R.string.tips_prompt_enable_midrop);
            this.mEnableMiShareView.setVisibility(0);
        }
    }

    private void setDiscoveringView() {
        this.mEnableMiShareView.setVisibility(8);
        this.mTipsTv.setText((int) R.string.scanning_device_tips);
        this.mMiShareDisabled.setVisibility(0);
        this.mDeviceLv.setVisibility(4);
        this.mAdapter.setInitialAddDevice();
    }

    private void startDiscover() {
        Intent intent = this.mIntent;
        if (intent != null) {
            this.mConnectivity.startDiscoverWithIntent(this.mDiscoverCallback, intent);
        } else {
            this.mConnectivity.startDiscover(this.mDiscoverCallback);
        }
        this.mConnectivity.registerTaskStateListener(this.mTaskStateListener);
    }

    private boolean hasPrinter() {
        Intent intent = new Intent();
        intent.setClassName(PrintManager.PRINT_SPOOLER_PACKAGE_NAME, "com.android.printspooler.ui.MiuiPrintActivity");
        PackageManager pm = getContext().getPackageManager();
        boolean z = false;
        if (pm == null) {
            return false;
        }
        if (pm.queryIntentActivities(intent, 0).size() > 0) {
            z = true;
        }
        return z;
    }

    public static boolean isServiceAvailable(Context context) {
        return MiShareConnectivity.isAvailable(context);
    }

    public void setIntent(Intent intent) {
        this.mIntent = intent;
    }

    public void bind() {
        this.mConnectivity.bind(this);
    }

    public void unbind() {
        if (this.mConnectivity.checkServiceBound()) {
            this.mConnectivity.unregisterStateListener(this.mStateListener);
            this.mConnectivity.unregisterTaskStateListener(this.mTaskStateListener);
            this.mConnectivity.unregisterScreenThrowListener();
            stopDiscover();
        }
        this.mConnectivity.unbind();
        this.mAdapter.unregister();
        this.mAdapter.stopSort();
        unregisterStateReceiver();
        getContext().getContentResolver().unregisterContentObserver(this.mContentObserver);
    }

    public void setFiles(List<Uri> files) {
        if (isMiAppStore(files)) {
            setVisibility(8);
            return;
        }
        this.mFiles = files;
        addType2Container(getFileIcon(files));
        addType2Container(getFileTypeDetailGroup(files));
        if (files == null || files.size() != 1) {
            setMiPrintEnable(false);
        } else {
            setMiPrintEnable(isFileCanPrint((Uri) files.get(0)));
        }
    }

    private boolean isMiAppStore(List<Uri> files) {
        boolean z = false;
        if (files == null || files.size() < 1) {
            return false;
        }
        Uri uri = (Uri) files.get(0);
        if (uri != null && TextUtils.equals(uri.getAuthority(), MI_APP_STORE_AUTHORITY)) {
            z = true;
        }
        return z;
    }

    public void registerHasNoFilesListener(HasNoFilesListener listener) {
        this.mHasNoFilesListener = listener;
    }

    public void setDeliveryTitle(String title) {
        ((TextView) findViewById(R.id.tv_send_file_to)).setText((CharSequence) title);
    }

    public void setSpaceHeight(int height) {
        View deviceView = findViewById(R.id.rl_device);
        LayoutParams lp = (LayoutParams) deviceView.getLayoutParams();
        if (lp != null) {
            lp.setMargins(lp.leftMargin, ScreenUtil.dip2px(getContext(), (float) height), lp.rightMargin, lp.bottomMargin);
            deviceView.setLayoutParams(lp);
        }
    }

    private boolean isFileCanPrint(Uri uri) {
        return MiShareFileHelper.isImageCanPrint(getContext(), uri) || MiShareFileHelper.isFilePdf(getContext(), uri);
    }

    private void addType2Container(View view) {
        if (view != null) {
            this.mFileTypeContainer.addView(view);
        }
    }

    private MiShareFileTypeIcon getFileIcon(List<Uri> files) {
        MiShareFileTypeIcon icon = new MiShareFileTypeIcon(getContext());
        icon.setFileTypes(files);
        return icon;
    }

    private View getFileTypeDetailGroup(List<Uri> files) {
        if (files == null || files.isEmpty()) {
            return new View(getContext());
        }
        int fileCount = files.size();
        View view = LayoutInflater.from(getContext()).inflate((int) R.layout.layout_file_type_detail, (ViewGroup) this, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -1);
        lp.setMargins(ScreenUtil.dip2px(getContext(), 5.0f), 0, 0, 0);
        lp.gravity = 16;
        view.setLayoutParams(lp);
        TextView singleFileNameTv = (TextView) view.findViewById(R.id.tv_single_file_name);
        TextView fileCountTv = (TextView) view.findViewById(R.id.tv_file_count);
        if (fileCount == 1) {
            singleFileNameTv.setVisibility(0);
            fileCountTv.setVisibility(8);
            singleFileNameTv.setText(MiShareFileHelper.getFileNameFromUri(getContext(), (Uri) files.get(0)));
        } else {
            singleFileNameTv.setVisibility(0);
            fileCountTv.setVisibility(0);
            singleFileNameTv.setText(getMultiFileName(files));
            fileCountTv.setText(getResources().getQuantityString(R.plurals.file_type_detail_count, fileCount, Integer.valueOf(fileCount)));
        }
        return view;
    }

    private String getMultiFileName(List<Uri> files) {
        String firstName = MiShareFileHelper.getFileNameFromUri(getContext(), (Uri) files.get(0));
        String secondName = MiShareFileHelper.getFileNameFromUri(getContext(), (Uri) files.get(1));
        return getResources().getString(R.string.multi_file_name, getFileName(firstName), getFileName(secondName));
    }

    private String getFileName(String fileName) {
        if (fileName.length() < 10) {
            return fileName;
        }
        String head = fileName.substring(0, 6);
        String end = fileName.substring(fileName.length() - 6);
        return getContext().getResources().getString(R.string.name_with_ellipsize, head, end);
    }

    public void setScreenThrowClickListener(OnClickListener listener) {
        this.mScreenThrowClick = listener;
    }

    public void setScreenThrowEnable(boolean enable) {
        this.mScreenThrow.setImageResource(enable ? R.drawable.bg_ic_screen : R.drawable.ic_screen_pressed);
    }

    public void setMiPrintEnable(boolean enable) {
        this.mPrintBtn.setImageResource(enable ? R.drawable.bg_ic_printer : R.drawable.ic_printer_pressed);
    }

    public void setScreenThrowHighLight(boolean highLight) {
        if (highLight) {
            this.mScreenThrow.setBackground(getContext().getResources().getDrawable(R.drawable.bg_screen_throw_hight_light));
            this.mScreenThrow.setImageResource(R.drawable.ic_screen_throwing);
        } else {
            this.mScreenThrow.setBackground(getContext().getResources().getDrawable(R.drawable.bg_enable_midrop));
            this.mScreenThrow.setImageResource(R.drawable.bg_ic_screen);
        }
        this.mIsScreenThrowConnected = highLight;
    }

    public void setMiPrintClickListener(OnClickListener listener) {
        this.mMiPrintClick = listener;
    }

    public void onDeviceTaskRetry(MiShareDevice device, int deviceX, int deviceY) {
        if (device.isPC()) {
            device.taskId = MiShareDevice.generatePCTaskId();
        }
        send(device, deviceX, deviceY);
    }

    public void onCancelTask(MiShareDevice device) {
        MiShareTask task = new MiShareTask();
        task.device = device.remoteDevice;
        task.taskId = device.taskId;
        this.mConnectivity.cancel(task);
    }

    private void send(MiShareDevice device, int deviceX, int deviceY) {
        if (device != null) {
            List list = this.mFiles;
            if ((list == null || list.isEmpty()) && this.mHasNoFilesListener != null) {
                device.deviceStatus = 1;
                this.mAdapter.notifyDataSetChanged();
                this.mHasNoFilesListener.hasNoFiles();
                return;
            }
            if (device.files == null) {
                device.files = this.mFiles;
            }
            if (device.files != null) {
                MiShareTask task = new MiShareTask();
                task.device = device.remoteDevice;
                task.clipData = getClipData(device.files);
                task.taskId = device.taskId;
                task.deviceX = deviceX;
                task.deviceY = deviceY;
                task.count = device.files.size();
                this.mConnectivity.sendData(task);
            }
        }
    }

    public void onDeviceTaskStart(MiShareDevice device, int deviceX, int deviceY) {
        send(device, deviceX, deviceY);
    }

    public void stopDiscover() {
        this.mConnectivity.stopDiscover(this.mDiscoverCallback);
    }

    private ClipData getClipData(List<Uri> files) {
        if (files == null || files.size() <= 0) {
            return null;
        }
        ClipData data = new ClipData(new ClipDescription("mishare data", new String[]{""}), new Item((Uri) files.get(0)));
        int count = files.size();
        for (int i = 1; i < count; i++) {
            Uri uriItem = (Uri) files.get(i);
            if (uriItem != null) {
                data.addItem(new Item(uriItem));
            }
        }
        return data;
    }

    public void onClick(View v) {
        int id = v.getId();
        OnClickListener onClickListener;
        if (id == R.id.btn_enable_midrop) {
            startDiscover();
            v.setVisibility(8);
        } else if (id == R.id.iv_throwing_screen) {
            screenThrow();
            onClickListener = this.mScreenThrowClick;
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        } else if (id != R.id.iv_printer) {
        } else {
            if (canPrint()) {
                print();
                onClickListener = this.mMiPrintClick;
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            } else if (isOfficeFile()) {
                showToast(getResources().getString(R.string.functions_are_being_developed));
            } else {
                showToast(getResources().getString(R.string.file_cannot_print));
            }
        }
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), (CharSequence) msg, 0).show();
    }

    private boolean isOfficeFile() {
        List list = this.mFiles;
        boolean z = false;
        if (list == null || list.size() > 1 || this.mFiles.size() == 0) {
            return false;
        }
        Uri uri = (Uri) this.mFiles.get(0);
        if (uri != null && MiShareFileHelper.isOfficeFile(getContext(), uri)) {
            z = true;
        }
        return z;
    }

    private boolean canPrint() {
        List list = this.mFiles;
        boolean z = false;
        if (list == null || list.size() > 1 || this.mFiles.size() == 0) {
            return false;
        }
        Uri uri = (Uri) this.mFiles.get(0);
        if (uri != null && (MiShareFileHelper.isImageCanPrint(getContext(), uri) || MiShareFileHelper.isFilePdf(getContext(), uri))) {
            z = true;
        }
        return z;
    }

    private void print() {
        List list = this.mFiles;
        if (list != null && list.size() == 1) {
            new PrintHelper(getContext()).setScaleMode(1);
            Uri uri = (Uri) this.mFiles.get(0);
            Intent intent = new Intent(PRINT_ACTION);
            ActivityInfo activityInfo = getActivityInfo(intent);
            if (activityInfo != null && !TextUtils.isEmpty(activityInfo.packageName) && !TextUtils.isEmpty(activityInfo.name)) {
                intent.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
                intent.addFlags(AudioSystem.DEVICE_IN_COMMUNICATION);
                intent.setData(uri);
                ((Activity) getContext()).startActivityAsCaller(intent, null, null, true, -10000);
            }
        }
    }

    public ActivityInfo getActivityInfo(Intent intent) {
        List<ResolveInfo> resInfoList = getContext().getPackageManager().queryIntentActivities(intent, 65536);
        ActivityInfo activityInfo = null;
        if (resInfoList == null || resInfoList.size() <= 0) {
            return null;
        }
        ResolveInfo resolveInfo = (ResolveInfo) resInfoList.get(0);
        if (resolveInfo != null) {
            activityInfo = resolveInfo.activityInfo;
        }
        return activityInfo;
    }

    private void screenThrow() {
        if (this.mIsScreenThrowConnected) {
            this.mConnectivity.closeScreenThrow();
        } else {
            this.mConnectivity.openScreenThrow();
        }
    }

    private int getDeviceType(byte manufactureCode) {
        if (manufactureCode >= Vivo.MANUFACTURE_START && manufactureCode <= Vivo.MANUFACTURE_END) {
            return 2;
        }
        if (manufactureCode < (byte) 10 || manufactureCode > Oppo.MANUFACTURE_END) {
            return 1;
        }
        if (manufactureCode == Oppo.MANUFACTURE_REALME) {
            return 4;
        }
        return 3;
    }

    public void onServiceBound() {
        this.mState = this.mConnectivity.getServiceState();
        this.mConnectivity.registerStateListener(this.mStateListener);
        this.mConnectivity.registerScreenThrowListener(this.mScreenThrowListener);
        int i = this.mState;
        if (i == 3 || i == 4 || i == 5 || i == 6) {
            startDiscover();
        }
    }
}
