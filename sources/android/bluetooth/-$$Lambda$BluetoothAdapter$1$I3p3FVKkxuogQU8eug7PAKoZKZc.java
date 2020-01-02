package android.bluetooth;

import android.bluetooth.BluetoothAdapter.OnMetadataChangedListener;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$BluetoothAdapter$1$I3p3FVKkxuogQU8eug7PAKoZKZc implements Runnable {
    private final /* synthetic */ OnMetadataChangedListener f$0;
    private final /* synthetic */ BluetoothDevice f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ byte[] f$3;

    public /* synthetic */ -$$Lambda$BluetoothAdapter$1$I3p3FVKkxuogQU8eug7PAKoZKZc(OnMetadataChangedListener onMetadataChangedListener, BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
        this.f$0 = onMetadataChangedListener;
        this.f$1 = bluetoothDevice;
        this.f$2 = i;
        this.f$3 = bArr;
    }

    public final void run() {
        this.f$0.onMetadataChanged(this.f$1, this.f$2, this.f$3);
    }
}
