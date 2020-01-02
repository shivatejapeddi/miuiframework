package com.miui.mishare.app.adapter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.miui.R;
import android.os.AnrMonitor;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.android.internal.widget.RecyclerView.Adapter;
import com.miui.mishare.MiShareTask;
import com.miui.mishare.RemoteDevice;
import com.miui.mishare.app.connect.MiShareConstant;
import com.miui.mishare.app.model.MiShareDevice;
import com.miui.mishare.app.util.MiShareFileHelper;
import com.miui.mishare.app.view.MiShareDeviceView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import miui.app.AlertDialog;
import miui.app.AlertDialog.Builder;

public class MiShareDeviceAdapter extends Adapter<ViewHolder> implements OnClickListener {
    private static final int IDLE_DELAY = 3000;
    private static final long INTERVAL_SORT_TIMES = 2000;
    private static final int MSG_DEVICE_IDLE = 1;
    private DeviceRssiComparator mComparator = new DeviceRssiComparator(this, null);
    private Context mContext;
    private List<MiShareDevice> mDevices = new ArrayList();
    private AlertDialog mDialog;
    private Handler mHandler = new IdleHandler(this);
    private boolean mIsInitialAddDevice = true;
    private OnDeviceClickListener mListener;
    private final Receiver mReceiver = new Receiver();
    private Handler mSortHandler = new Handler();
    private final Runnable mSortRunnable = new Runnable() {
        public void run() {
            if (!MiShareDeviceAdapter.this.mStopSort && MiShareDeviceAdapter.this.mDevices != null && !MiShareDeviceAdapter.this.mDevices.isEmpty()) {
                Collections.sort(MiShareDeviceAdapter.this.mDevices, MiShareDeviceAdapter.this.mComparator);
                MiShareDeviceAdapter.this.notifyDataSetChanged();
                MiShareDeviceAdapter.this.startSort();
            }
        }
    };
    private boolean mStopSort;

    private final class DeviceRssiComparator implements Comparator<MiShareDevice> {
        private DeviceRssiComparator() {
        }

        /* synthetic */ DeviceRssiComparator(MiShareDeviceAdapter x0, AnonymousClass1 x1) {
            this();
        }

        public int compare(MiShareDevice arg0, MiShareDevice arg1) {
            int rssiFirst;
            String str = "rssi";
            int i = 0;
            if (arg0 == null || arg0.remoteDevice == null || arg0.remoteDevice.getExtras() == null) {
                rssiFirst = 0;
            } else {
                rssiFirst = arg0.remoteDevice.getExtras().getInt(str);
            }
            if (!(arg1 == null || arg1.remoteDevice == null || arg1.remoteDevice.getExtras() == null)) {
                i = arg1.remoteDevice.getExtras().getInt(str);
            }
            return i - rssiFirst;
        }
    }

    private static class IdleHandler extends Handler {
        private WeakReference<MiShareDeviceAdapter> mOuter;

        IdleHandler(MiShareDeviceAdapter mOuter) {
            this.mOuter = new WeakReference(mOuter);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String taskId = msg.obj;
                if (this.mOuter.get() != null) {
                    ((MiShareDeviceAdapter) this.mOuter.get()).setDeviceState(taskId, 1);
                }
            }
        }
    }

    public interface OnDeviceClickListener {
        void onCancelTask(MiShareDevice miShareDevice);

        void onDeviceTaskRetry(MiShareDevice miShareDevice, int i, int i2);

        void onDeviceTaskStart(MiShareDevice miShareDevice, int i, int i2);
    }

    public final class Receiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (MiShareDeviceAdapter.this.mDialog != null && (MiShareDeviceAdapter.this.mContext instanceof Activity) && !((Activity) MiShareDeviceAdapter.this.mContext).isFinishing()) {
                MiShareDeviceAdapter.this.mDialog.dismiss();
            }
        }
    }

    static class ViewHolder extends com.android.internal.widget.RecyclerView.ViewHolder {
        MiShareDeviceView mDeviceView;

        ViewHolder(MiShareDeviceView itemView) {
            super(itemView);
            this.mDeviceView = itemView;
        }
    }

    public MiShareDeviceAdapter(Context context) {
        this.mContext = context;
        this.mContext.registerReceiver(this.mReceiver, new IntentFilter(MiShareConstant.ACTION_REMOTE_NO_NEED_CANCEL_TASK));
    }

    public MiShareDevice getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return (MiShareDevice) this.mDevices.get(position);
    }

    public void setInitialAddDevice() {
        this.mIsInitialAddDevice = true;
    }

    private void startSort() {
        this.mStopSort = false;
        this.mSortHandler.postDelayed(this.mSortRunnable, 2000);
    }

    public void registerListener(OnDeviceClickListener listener) {
        this.mListener = listener;
    }

    public void setDeviceState(String taskId, int state) {
        for (MiShareDevice device : this.mDevices) {
            if (device != null && device.remoteDevice != null && TextUtils.equals(taskId, device.taskId)) {
                if (device.deviceStatus != 2 || state != 1) {
                    device.deviceStatus = state;
                    notifyDataSetChanged();
                    delayIdle(taskId, state);
                    return;
                }
                return;
            }
        }
    }

    public void unregister() {
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    public void stopSort() {
        this.mStopSort = true;
        this.mSortHandler.removeCallbacks(this.mSortRunnable);
    }

    private void delayIdle(String taskId, int status) {
        if (status == 4 || status == 3) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = taskId;
            this.mHandler.sendMessageDelayed(msg, AnrMonitor.PERF_EVENT_LOGGING_TIMEOUT);
        }
    }

    public void clear() {
        this.mDevices.clear();
    }

    public void addOrUpdateDevice(MiShareDevice device) {
        for (int i = 0; i < this.mDevices.size(); i++) {
            MiShareDevice d = (MiShareDevice) this.mDevices.get(i);
            if (TextUtils.equals(d.deviceId, device.deviceId)) {
                device.deviceStatus = d.deviceStatus;
                device.taskId = d.taskId;
                this.mDevices.set(i, device);
                return;
            }
        }
        this.mDevices.add(device);
        if (this.mIsInitialAddDevice) {
            this.mIsInitialAddDevice = false;
            startSort();
        }
    }

    public void replaceTaskId(MiShareTask task) {
        if (!(task == null || task.device == null)) {
            for (int i = 0; i < this.mDevices.size(); i++) {
                MiShareDevice d = (MiShareDevice) this.mDevices.get(i);
                if (TextUtils.equals(d.deviceId, task.device.getDeviceId())) {
                    d.taskId = task.taskId;
                    return;
                }
            }
        }
    }

    public void removeDevice(String deviceId) {
        for (int i = 0; i < this.mDevices.size(); i++) {
            if (TextUtils.equals(((MiShareDevice) this.mDevices.get(i)).deviceId, deviceId)) {
                this.mDevices.remove(i);
                return;
            }
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(new MiShareDeviceView(this.mContext));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        MiShareDevice device = getItem(position);
        boolean z = false;
        if (device != null) {
            boolean nickNameHasMore = (device.remoteDevice == null || device.remoteDevice.getExtras() == null || !device.remoteDevice.getExtras().getBoolean(RemoteDevice.KEY_NICKNAME_HAS_MORE)) ? false : true;
            viewHolder.mDeviceView.setDeviceType(device.deviceType);
            viewHolder.mDeviceView.setDeviceName(device.deviceName, nickNameHasMore);
            viewHolder.mDeviceView.setDeviceStatus(device.deviceStatus);
            boolean isPhone = device.isPC() ^ true;
            viewHolder.mDeviceView.setDevicePhoneOrPc(isPhone);
            if (isPhone) {
                viewHolder.mDeviceView.setDeviceModelName(device.deviceModelName);
            } else {
                viewHolder.mDeviceView.setDeviceModelName(this.mContext.getString(R.string.pc_device_model_name));
            }
        }
        viewHolder.mDeviceView.showDividerStart(position == 0);
        viewHolder.mDeviceView.showDivider(true);
        MiShareDeviceView miShareDeviceView = viewHolder.mDeviceView;
        if (position == getItemCount() - 1) {
            z = true;
        }
        miShareDeviceView.showDividerEnd(z);
        viewHolder.mDeviceView.getIconView().setTag(R.id.position, Integer.valueOf(position));
        viewHolder.mDeviceView.getIconView().setOnClickListener(this);
    }

    public long getItemId(int position) {
        return 0;
    }

    public int getItemCount() {
        List list = this.mDevices;
        return list != null ? list.size() : 0;
    }

    public void onClick(View v) {
        int position = ((Integer) v.getTag(R.id.position)).intValue();
        int[] p = new int[2];
        v.getLocationOnScreen(p);
        int x = p[0];
        int y = p[1];
        MiShareDevice device = getItem(position);
        if (device != null) {
            int i = device.deviceStatus;
            OnDeviceClickListener onDeviceClickListener;
            if (i == 1) {
                device.deviceStatus = 2;
                onDeviceClickListener = this.mListener;
                if (onDeviceClickListener != null) {
                    onDeviceClickListener.onDeviceTaskStart(device, x, y);
                }
            } else if (i == 2) {
                showCancelConfirm(device);
            } else if (i == 3) {
                device.deviceStatus = 2;
                onDeviceClickListener = this.mListener;
                if (onDeviceClickListener != null) {
                    onDeviceClickListener.onDeviceTaskRetry(device, x, y);
                }
            } else if (i == 4) {
                device.deviceStatus = 2;
                onDeviceClickListener = this.mListener;
                if (onDeviceClickListener != null) {
                    onDeviceClickListener.onDeviceTaskStart(device, x, y);
                }
            }
            notifyDataSetChanged();
        }
    }

    private void showCancelConfirm(final MiShareDevice device) {
        if (device != null && device.remoteDevice != null && device.remoteDevice.getExtras() != null) {
            String message;
            String nickName = device.remoteDevice.getExtras().getString(RemoteDevice.KEY_NICKNAME);
            if (MiShareFileHelper.isAllImageOverview(this.mContext, device.files)) {
                message = this.mContext.getResources().getString(R.string.confirm_cancel_task_images, nickName);
            } else {
                message = this.mContext.getResources().getString(R.string.confirm_cancel_task_files, new Object[]{nickName});
            }
            this.mDialog = new Builder(this.mContext).setMessage(message).setCancelable(false).setNegativeButton(this.mContext.getResources().getString(R.string.btn_cancel), null).setPositiveButton(this.mContext.getResources().getString(R.string.btn_sure), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    device.deviceStatus = 1;
                    MiShareDeviceAdapter.this.notifyDataSetChanged();
                    if (MiShareDeviceAdapter.this.mListener != null) {
                        MiShareDeviceAdapter.this.mListener.onCancelTask(device);
                    }
                }
            }).create();
            this.mDialog.show();
        }
    }
}
