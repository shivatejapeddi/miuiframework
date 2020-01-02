package android.bluetooth;

import android.annotation.UnsupportedAppUsage;
import android.net.LocalSocket;
import android.os.ParcelFileDescriptor;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

public final class BluetoothSocket implements Closeable {
    static final int BTSOCK_FLAG_NO_SDP = 4;
    private static final boolean DBG;
    @UnsupportedAppUsage
    static final int EADDRINUSE = 98;
    static final int EBADFD = 77;
    static final int MAX_L2CAP_PACKAGE_SIZE = 65535;
    public static final int MAX_RFCOMM_CHANNEL = 30;
    private static final int PROXY_CONNECTION_TIMEOUT = 5000;
    static final int SEC_FLAG_AUTH = 2;
    static final int SEC_FLAG_AUTH_16_DIGIT = 16;
    static final int SEC_FLAG_AUTH_MITM = 8;
    static final int SEC_FLAG_ENCRYPT = 1;
    private static final int SOCK_SIGNAL_SIZE = 20;
    private static final String TAG = "BluetoothSocket";
    public static final int TYPE_L2CAP = 3;
    public static final int TYPE_L2CAP_BREDR = 3;
    public static final int TYPE_L2CAP_LE = 4;
    public static final int TYPE_RFCOMM = 1;
    public static final int TYPE_SCO = 2;
    private static final boolean VDBG;
    private String mAddress;
    private final boolean mAuth;
    private boolean mAuthMitm;
    private BluetoothDevice mDevice;
    private final boolean mEncrypt;
    private boolean mExcludeSdp;
    private int mFd;
    private final BluetoothInputStream mInputStream;
    private ByteBuffer mL2capBuffer;
    private int mMaxRxPacketSize;
    private int mMaxTxPacketSize;
    private boolean mMin16DigitPin;
    private final BluetoothOutputStream mOutputStream;
    @UnsupportedAppUsage
    private ParcelFileDescriptor mPfd;
    @UnsupportedAppUsage
    private int mPort;
    private String mServiceName;
    @UnsupportedAppUsage
    private LocalSocket mSocket;
    private InputStream mSocketIS;
    private OutputStream mSocketOS;
    private volatile SocketState mSocketState;
    private final int mType;
    private final ParcelUuid mUuid;

    private enum SocketState {
        INIT,
        CONNECTED,
        LISTENING,
        CLOSED
    }

    static {
        String str = TAG;
        DBG = Log.isLoggable(str, 3);
        VDBG = Log.isLoggable(str, 2);
    }

    BluetoothSocket(int type, int fd, boolean auth, boolean encrypt, BluetoothDevice device, int port, ParcelUuid uuid) throws IOException {
        this(type, fd, auth, encrypt, device, port, uuid, false, false);
    }

    BluetoothSocket(int type, int fd, boolean auth, boolean encrypt, BluetoothDevice device, int port, ParcelUuid uuid, boolean mitm, boolean min16DigitPin) throws IOException {
        this.mExcludeSdp = false;
        this.mAuthMitm = false;
        this.mMin16DigitPin = false;
        this.mL2capBuffer = null;
        this.mMaxTxPacketSize = 0;
        this.mMaxRxPacketSize = 0;
        if (VDBG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Creating new BluetoothSocket of type: ");
            stringBuilder.append(type);
            Log.d(TAG, stringBuilder.toString());
        }
        if (type == 1 && uuid == null && fd == -1 && port != -2 && (port < 1 || port > 30)) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Invalid RFCOMM channel: ");
            stringBuilder2.append(port);
            throw new IOException(stringBuilder2.toString());
        }
        if (uuid != null) {
            this.mUuid = uuid;
        } else {
            this.mUuid = new ParcelUuid(new UUID(0, 0));
        }
        this.mType = type;
        this.mAuth = auth;
        this.mAuthMitm = mitm;
        this.mMin16DigitPin = min16DigitPin;
        this.mEncrypt = encrypt;
        this.mDevice = device;
        this.mPort = port;
        this.mFd = fd;
        this.mSocketState = SocketState.INIT;
        if (device == null) {
            this.mAddress = BluetoothAdapter.getDefaultAdapter().getAddress();
        } else {
            this.mAddress = device.getAddress();
        }
        this.mInputStream = new BluetoothInputStream(this);
        this.mOutputStream = new BluetoothOutputStream(this);
    }

    private BluetoothSocket(BluetoothSocket s) {
        this.mExcludeSdp = false;
        this.mAuthMitm = false;
        this.mMin16DigitPin = false;
        this.mL2capBuffer = null;
        this.mMaxTxPacketSize = 0;
        this.mMaxRxPacketSize = 0;
        if (VDBG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Creating new Private BluetoothSocket of type: ");
            stringBuilder.append(s.mType);
            Log.d(TAG, stringBuilder.toString());
        }
        this.mUuid = s.mUuid;
        this.mType = s.mType;
        this.mAuth = s.mAuth;
        this.mEncrypt = s.mEncrypt;
        this.mPort = s.mPort;
        this.mInputStream = new BluetoothInputStream(this);
        this.mOutputStream = new BluetoothOutputStream(this);
        this.mMaxRxPacketSize = s.mMaxRxPacketSize;
        this.mMaxTxPacketSize = s.mMaxTxPacketSize;
        this.mServiceName = s.mServiceName;
        this.mExcludeSdp = s.mExcludeSdp;
        this.mAuthMitm = s.mAuthMitm;
        this.mMin16DigitPin = s.mMin16DigitPin;
    }

    private BluetoothSocket acceptSocket(String remoteAddr) throws IOException {
        StringBuilder stringBuilder;
        BluetoothSocket as = new BluetoothSocket(this);
        as.mSocketState = SocketState.CONNECTED;
        FileDescriptor[] fds = this.mSocket.getAncillaryFileDescriptors();
        boolean z = DBG;
        String str = TAG;
        if (z) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("socket fd passed by stack fds: ");
            stringBuilder.append(Arrays.toString(fds));
            Log.d(str, stringBuilder.toString());
        }
        if (fds == null || fds.length != 1) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("socket fd passed from stack failed, fds: ");
            stringBuilder.append(Arrays.toString(fds));
            Log.e(str, stringBuilder.toString());
            as.close();
            throw new IOException("bt socket acept failed");
        }
        as.mPfd = new ParcelFileDescriptor(fds[0]);
        as.mSocket = LocalSocket.createConnectedLocalSocket(fds[0]);
        as.mSocketIS = as.mSocket.getInputStream();
        as.mSocketOS = as.mSocket.getOutputStream();
        as.mAddress = remoteAddr;
        as.mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(remoteAddr);
        as.mPort = this.mPort;
        return as;
    }

    private BluetoothSocket(int type, int fd, boolean auth, boolean encrypt, String address, int port) throws IOException {
        this(type, fd, auth, encrypt, new BluetoothDevice(address), port, null, false, false);
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    private int getSecurityFlags() {
        int flags = 0;
        if (this.mAuth) {
            flags = 0 | 2;
        }
        if (this.mEncrypt) {
            flags |= 1;
        }
        if (this.mExcludeSdp) {
            flags |= 4;
        }
        if (this.mAuthMitm) {
            flags |= 8;
        }
        if (this.mMin16DigitPin) {
            return flags | 16;
        }
        return flags;
    }

    public BluetoothDevice getRemoteDevice() {
        return this.mDevice;
    }

    public InputStream getInputStream() throws IOException {
        return this.mInputStream;
    }

    public OutputStream getOutputStream() throws IOException {
        return this.mOutputStream;
    }

    public boolean isConnected() {
        return this.mSocketState == SocketState.CONNECTED;
    }

    /* Access modifiers changed, original: 0000 */
    public void setServiceName(String name) {
        this.mServiceName = name;
    }

    public void connect() throws IOException {
        if (this.mDevice != null) {
            StringBuilder stringBuilder;
            try {
                if (this.mSocketState != SocketState.CLOSED) {
                    IBluetooth bluetoothProxy = BluetoothAdapter.getDefaultAdapter().getBluetoothService(null);
                    if (bluetoothProxy != null) {
                        this.mPfd = bluetoothProxy.getSocketManager().connectSocket(this.mDevice, this.mType, this.mUuid, this.mPort, getSecurityFlags());
                        synchronized (this) {
                            if (DBG) {
                                String str = TAG;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("connect(), SocketState: ");
                                stringBuilder.append(this.mSocketState);
                                stringBuilder.append(", mPfd: ");
                                stringBuilder.append(this.mPfd);
                                Log.d(str, stringBuilder.toString());
                            }
                            if (this.mSocketState == SocketState.CLOSED) {
                                throw new IOException("socket closed");
                            } else if (this.mPfd != null) {
                                this.mSocket = LocalSocket.createConnectedLocalSocket(this.mPfd.getFileDescriptor());
                                this.mSocketIS = this.mSocket.getInputStream();
                                this.mSocketOS = this.mSocket.getOutputStream();
                            } else {
                                throw new IOException("bt socket connect failed");
                            }
                        }
                        int channel = readInt(this.mSocketIS);
                        if (channel > 0) {
                            this.mPort = channel;
                            waitSocketSignal(this.mSocketIS);
                            synchronized (this) {
                                if (this.mSocketState != SocketState.CLOSED) {
                                    this.mSocketState = SocketState.CONNECTED;
                                } else {
                                    throw new IOException("bt socket closed");
                                }
                            }
                            return;
                        }
                        throw new IOException("bt socket connect failed");
                    }
                    throw new IOException("Bluetooth is off");
                }
                throw new IOException("socket closed");
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                stringBuilder = new StringBuilder();
                stringBuilder.append("unable to send RPC: ");
                stringBuilder.append(e.getMessage());
                throw new IOException(stringBuilder.toString());
            }
        }
        throw new IOException("Connect is called on null device");
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:41:0x00cf, code skipped:
            if (DBG == false) goto L_0x00e9;
     */
    /* JADX WARNING: Missing block: B:42:0x00d1, code skipped:
            r2 = TAG;
            r4 = new java.lang.StringBuilder();
            r4.append("bindListen(), readInt mSocketIS: ");
            r4.append(r10.mSocketIS);
            android.util.Log.d(r2, r4.toString());
     */
    /* JADX WARNING: Missing block: B:43:0x00e9, code skipped:
            r2 = readInt(r10.mSocketIS);
     */
    /* JADX WARNING: Missing block: B:44:0x00ef, code skipped:
            monitor-enter(r10);
     */
    /* JADX WARNING: Missing block: B:47:0x00f4, code skipped:
            if (r10.mSocketState != android.bluetooth.BluetoothSocket.SocketState.INIT) goto L_0x00fa;
     */
    /* JADX WARNING: Missing block: B:48:0x00f6, code skipped:
            r10.mSocketState = android.bluetooth.BluetoothSocket.SocketState.LISTENING;
     */
    /* JADX WARNING: Missing block: B:49:0x00fa, code skipped:
            monitor-exit(r10);
     */
    /* JADX WARNING: Missing block: B:52:0x00fd, code skipped:
            if (DBG == false) goto L_0x011f;
     */
    /* JADX WARNING: Missing block: B:53:0x00ff, code skipped:
            r4 = TAG;
            r5 = new java.lang.StringBuilder();
            r5.append("bindListen(): channel=");
            r5.append(r2);
            r5.append(", mPort=");
            r5.append(r10.mPort);
            android.util.Log.d(r4, r5.toString());
     */
    /* JADX WARNING: Missing block: B:55:0x0121, code skipped:
            if (r10.mPort > -1) goto L_0x0125;
     */
    /* JADX WARNING: Missing block: B:56:0x0123, code skipped:
            r10.mPort = r2;
     */
    /* JADX WARNING: Missing block: B:58:0x0127, code skipped:
            return 0;
     */
    public int bindListen() {
        /*
        r10 = this;
        r0 = r10.mSocketState;
        r1 = android.bluetooth.BluetoothSocket.SocketState.CLOSED;
        r2 = 77;
        if (r0 != r1) goto L_0x0009;
    L_0x0008:
        return r2;
    L_0x0009:
        r0 = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        r1 = 0;
        r0 = r0.getBluetoothService(r1);
        r3 = -1;
        if (r0 != 0) goto L_0x001d;
    L_0x0015:
        r1 = "BluetoothSocket";
        r2 = "bindListen fail, reason: bluetooth is off";
        android.util.Log.e(r1, r2);
        return r3;
    L_0x001d:
        r4 = DBG;	 Catch:{ RemoteException -> 0x0167 }
        if (r4 == 0) goto L_0x0043;
    L_0x0021:
        r4 = "BluetoothSocket";
        r5 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0167 }
        r5.<init>();	 Catch:{ RemoteException -> 0x0167 }
        r6 = "bindListen(): mPort=";
        r5.append(r6);	 Catch:{ RemoteException -> 0x0167 }
        r6 = r10.mPort;	 Catch:{ RemoteException -> 0x0167 }
        r5.append(r6);	 Catch:{ RemoteException -> 0x0167 }
        r6 = ", mType=";
        r5.append(r6);	 Catch:{ RemoteException -> 0x0167 }
        r6 = r10.mType;	 Catch:{ RemoteException -> 0x0167 }
        r5.append(r6);	 Catch:{ RemoteException -> 0x0167 }
        r5 = r5.toString();	 Catch:{ RemoteException -> 0x0167 }
        android.util.Log.d(r4, r5);	 Catch:{ RemoteException -> 0x0167 }
    L_0x0043:
        r4 = r0.getSocketManager();	 Catch:{ RemoteException -> 0x0167 }
        r5 = r10.mType;	 Catch:{ RemoteException -> 0x0167 }
        r6 = r10.mServiceName;	 Catch:{ RemoteException -> 0x0167 }
        r7 = r10.mUuid;	 Catch:{ RemoteException -> 0x0167 }
        r8 = r10.mPort;	 Catch:{ RemoteException -> 0x0167 }
        r9 = r10.getSecurityFlags();	 Catch:{ RemoteException -> 0x0167 }
        r4 = r4.createSocketChannel(r5, r6, r7, r8, r9);	 Catch:{ RemoteException -> 0x0167 }
        r10.mPfd = r4;	 Catch:{ RemoteException -> 0x0167 }
        monitor-enter(r10);	 Catch:{ IOException -> 0x012e }
        r4 = DBG;	 Catch:{ all -> 0x012b }
        if (r4 == 0) goto L_0x0081;
    L_0x005f:
        r4 = "BluetoothSocket";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x012b }
        r5.<init>();	 Catch:{ all -> 0x012b }
        r6 = "bindListen(), SocketState: ";
        r5.append(r6);	 Catch:{ all -> 0x012b }
        r6 = r10.mSocketState;	 Catch:{ all -> 0x012b }
        r5.append(r6);	 Catch:{ all -> 0x012b }
        r6 = ", mPfd: ";
        r5.append(r6);	 Catch:{ all -> 0x012b }
        r6 = r10.mPfd;	 Catch:{ all -> 0x012b }
        r5.append(r6);	 Catch:{ all -> 0x012b }
        r5 = r5.toString();	 Catch:{ all -> 0x012b }
        android.util.Log.d(r4, r5);	 Catch:{ all -> 0x012b }
    L_0x0081:
        r4 = r10.mSocketState;	 Catch:{ all -> 0x012b }
        r5 = android.bluetooth.BluetoothSocket.SocketState.INIT;	 Catch:{ all -> 0x012b }
        if (r4 == r5) goto L_0x0089;
    L_0x0087:
        monitor-exit(r10);	 Catch:{ all -> 0x012b }
        return r2;
    L_0x0089:
        r2 = r10.mPfd;	 Catch:{ all -> 0x012b }
        if (r2 != 0) goto L_0x008f;
    L_0x008d:
        monitor-exit(r10);	 Catch:{ all -> 0x012b }
        return r3;
    L_0x008f:
        r2 = r10.mPfd;	 Catch:{ all -> 0x012b }
        r2 = r2.getFileDescriptor();	 Catch:{ all -> 0x012b }
        if (r2 != 0) goto L_0x00a0;
    L_0x0097:
        r4 = "BluetoothSocket";
        r5 = "bindListen(), null file descriptor";
        android.util.Log.e(r4, r5);	 Catch:{ all -> 0x012b }
        monitor-exit(r10);	 Catch:{ all -> 0x012b }
        return r3;
    L_0x00a0:
        r4 = DBG;	 Catch:{ all -> 0x012b }
        if (r4 == 0) goto L_0x00ab;
    L_0x00a4:
        r4 = "BluetoothSocket";
        r5 = "bindListen(), Create LocalSocket";
        android.util.Log.d(r4, r5);	 Catch:{ all -> 0x012b }
    L_0x00ab:
        r4 = android.net.LocalSocket.createConnectedLocalSocket(r2);	 Catch:{ all -> 0x012b }
        r10.mSocket = r4;	 Catch:{ all -> 0x012b }
        r4 = DBG;	 Catch:{ all -> 0x012b }
        if (r4 == 0) goto L_0x00bc;
    L_0x00b5:
        r4 = "BluetoothSocket";
        r5 = "bindListen(), new LocalSocket.getInputStream()";
        android.util.Log.d(r4, r5);	 Catch:{ all -> 0x012b }
    L_0x00bc:
        r4 = r10.mSocket;	 Catch:{ all -> 0x012b }
        r4 = r4.getInputStream();	 Catch:{ all -> 0x012b }
        r10.mSocketIS = r4;	 Catch:{ all -> 0x012b }
        r4 = r10.mSocket;	 Catch:{ all -> 0x012b }
        r4 = r4.getOutputStream();	 Catch:{ all -> 0x012b }
        r10.mSocketOS = r4;	 Catch:{ all -> 0x012b }
        monitor-exit(r10);	 Catch:{ all -> 0x012b }
        r2 = DBG;	 Catch:{ IOException -> 0x012e }
        if (r2 == 0) goto L_0x00e9;
    L_0x00d1:
        r2 = "BluetoothSocket";
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x012e }
        r4.<init>();	 Catch:{ IOException -> 0x012e }
        r5 = "bindListen(), readInt mSocketIS: ";
        r4.append(r5);	 Catch:{ IOException -> 0x012e }
        r5 = r10.mSocketIS;	 Catch:{ IOException -> 0x012e }
        r4.append(r5);	 Catch:{ IOException -> 0x012e }
        r4 = r4.toString();	 Catch:{ IOException -> 0x012e }
        android.util.Log.d(r2, r4);	 Catch:{ IOException -> 0x012e }
    L_0x00e9:
        r2 = r10.mSocketIS;	 Catch:{ IOException -> 0x012e }
        r2 = r10.readInt(r2);	 Catch:{ IOException -> 0x012e }
        monitor-enter(r10);	 Catch:{ IOException -> 0x012e }
        r4 = r10.mSocketState;	 Catch:{ all -> 0x0128 }
        r5 = android.bluetooth.BluetoothSocket.SocketState.INIT;	 Catch:{ all -> 0x0128 }
        if (r4 != r5) goto L_0x00fa;
    L_0x00f6:
        r4 = android.bluetooth.BluetoothSocket.SocketState.LISTENING;	 Catch:{ all -> 0x0128 }
        r10.mSocketState = r4;	 Catch:{ all -> 0x0128 }
    L_0x00fa:
        monitor-exit(r10);	 Catch:{ all -> 0x0128 }
        r4 = DBG;	 Catch:{ IOException -> 0x012e }
        if (r4 == 0) goto L_0x011f;
    L_0x00ff:
        r4 = "BluetoothSocket";
        r5 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x012e }
        r5.<init>();	 Catch:{ IOException -> 0x012e }
        r6 = "bindListen(): channel=";
        r5.append(r6);	 Catch:{ IOException -> 0x012e }
        r5.append(r2);	 Catch:{ IOException -> 0x012e }
        r6 = ", mPort=";
        r5.append(r6);	 Catch:{ IOException -> 0x012e }
        r6 = r10.mPort;	 Catch:{ IOException -> 0x012e }
        r5.append(r6);	 Catch:{ IOException -> 0x012e }
        r5 = r5.toString();	 Catch:{ IOException -> 0x012e }
        android.util.Log.d(r4, r5);	 Catch:{ IOException -> 0x012e }
    L_0x011f:
        r4 = r10.mPort;	 Catch:{ IOException -> 0x012e }
        if (r4 > r3) goto L_0x0125;
    L_0x0123:
        r10.mPort = r2;	 Catch:{ IOException -> 0x012e }
    L_0x0125:
        r1 = 0;
        return r1;
    L_0x0128:
        r4 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x0128 }
        throw r4;	 Catch:{ IOException -> 0x012e }
    L_0x012b:
        r2 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x012b }
        throw r2;	 Catch:{ IOException -> 0x012e }
    L_0x012e:
        r2 = move-exception;
        r4 = r10.mPfd;
        if (r4 == 0) goto L_0x0150;
    L_0x0133:
        r4.close();	 Catch:{ IOException -> 0x0137 }
        goto L_0x014e;
    L_0x0137:
        r4 = move-exception;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "bindListen, close mPfd: ";
        r5.append(r6);
        r5.append(r4);
        r5 = r5.toString();
        r6 = "BluetoothSocket";
        android.util.Log.e(r6, r5);
    L_0x014e:
        r10.mPfd = r1;
    L_0x0150:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r4 = "bindListen, fail to get port number, exception: ";
        r1.append(r4);
        r1.append(r2);
        r1 = r1.toString();
        r4 = "BluetoothSocket";
        android.util.Log.e(r4, r1);
        return r3;
    L_0x0167:
        r1 = move-exception;
        r2 = new java.lang.Throwable;
        r2.<init>();
        r2 = android.util.Log.getStackTraceString(r2);
        r4 = "BluetoothSocket";
        android.util.Log.e(r4, r2);
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.BluetoothSocket.bindListen():int");
    }

    /* Access modifiers changed, original: 0000 */
    public BluetoothSocket accept(int timeout) throws IOException {
        if (this.mSocketState == SocketState.LISTENING) {
            BluetoothSocket acceptedSocket;
            if (timeout > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("accept() set timeout (ms):");
                stringBuilder.append(timeout);
                Log.d(TAG, stringBuilder.toString());
                this.mSocket.setSoTimeout(timeout);
            }
            String RemoteAddr = waitSocketSignal(this.mSocketIS);
            if (timeout > 0) {
                this.mSocket.setSoTimeout(0);
            }
            synchronized (this) {
                if (this.mSocketState == SocketState.LISTENING) {
                    acceptedSocket = acceptSocket(RemoteAddr);
                } else {
                    throw new IOException("bt socket is not in listen state");
                }
            }
            return acceptedSocket;
        }
        throw new IOException("bt socket is not in listen state");
    }

    /* Access modifiers changed, original: 0000 */
    public int available() throws IOException {
        if (VDBG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("available: ");
            stringBuilder.append(this.mSocketIS);
            Log.d(TAG, stringBuilder.toString());
        }
        return this.mSocketIS.available();
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void flush() throws IOException {
        if (this.mSocketOS != null) {
            if (VDBG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("flush: ");
                stringBuilder.append(this.mSocketOS);
                Log.d(TAG, stringBuilder.toString());
            }
            this.mSocketOS.flush();
            return;
        }
        throw new IOException("flush is called on null OutputStream");
    }

    /* Access modifiers changed, original: 0000 */
    public int read(byte[] b, int offset, int length) throws IOException {
        StringBuilder stringBuilder;
        int ret;
        boolean z = VDBG;
        String str = TAG;
        if (z) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("read in:  ");
            stringBuilder.append(this.mSocketIS);
            stringBuilder.append(" len: ");
            stringBuilder.append(length);
            Log.d(str, stringBuilder.toString());
        }
        int i = this.mType;
        String str2 = " length:";
        if (i == 3 || i == 4) {
            StringBuilder stringBuilder2;
            i = length;
            if (VDBG) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("l2cap: read(): offset: ");
                stringBuilder2.append(offset);
                stringBuilder2.append(str2);
                stringBuilder2.append(length);
                stringBuilder2.append("mL2capBuffer= ");
                stringBuilder2.append(this.mL2capBuffer);
                Log.v(str, stringBuilder2.toString());
            }
            if (this.mL2capBuffer == null) {
                createL2capRxBuffer();
            }
            if (this.mL2capBuffer.remaining() == 0) {
                if (VDBG) {
                    Log.v(str, "l2cap buffer empty, refilling...");
                }
                if (fillL2capRxBuffer() == -1) {
                    return -1;
                }
            }
            if (i > this.mL2capBuffer.remaining()) {
                i = this.mL2capBuffer.remaining();
            }
            if (VDBG) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("get(): offset: ");
                stringBuilder2.append(offset);
                stringBuilder2.append(" bytesToRead: ");
                stringBuilder2.append(i);
                Log.v(str, stringBuilder2.toString());
            }
            this.mL2capBuffer.get(b, offset, i);
            ret = i;
        } else {
            if (VDBG) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("default: read(): offset: ");
                stringBuilder.append(offset);
                stringBuilder.append(str2);
                stringBuilder.append(length);
                Log.v(str, stringBuilder.toString());
            }
            ret = this.mSocketIS.read(b, offset, length);
        }
        if (ret >= 0) {
            if (VDBG) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("read out:  ");
                stringBuilder.append(this.mSocketIS);
                stringBuilder.append(" ret: ");
                stringBuilder.append(ret);
                Log.d(str, stringBuilder.toString());
            }
            return ret;
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("bt socket closed, read return: ");
        stringBuilder3.append(ret);
        throw new IOException(stringBuilder3.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public int write(byte[] b, int offset, int length) throws IOException {
        StringBuilder stringBuilder;
        boolean z = VDBG;
        String str = " length: ";
        String str2 = TAG;
        if (z) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("write: ");
            stringBuilder.append(this.mSocketOS);
            stringBuilder.append(str);
            stringBuilder.append(length);
            Log.d(str2, stringBuilder.toString());
        }
        int i = this.mType;
        if (i != 3 && i != 4) {
            this.mSocketOS.write(b, offset, length);
        } else if (length <= this.mMaxTxPacketSize) {
            this.mSocketOS.write(b, offset, length);
        } else {
            if (DBG) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("WARNING: Write buffer larger than L2CAP packet size!\nPacket will be divided into SDU packets of size ");
                stringBuilder.append(this.mMaxTxPacketSize);
                Log.w(str2, stringBuilder.toString());
            }
            i = offset;
            int bytesToWrite = length;
            while (bytesToWrite > 0) {
                int tmpLength = this.mMaxTxPacketSize;
                if (bytesToWrite <= tmpLength) {
                    tmpLength = bytesToWrite;
                }
                this.mSocketOS.write(b, i, tmpLength);
                i += tmpLength;
                bytesToWrite -= tmpLength;
            }
        }
        if (VDBG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("write out: ");
            stringBuilder.append(this.mSocketOS);
            stringBuilder.append(str);
            stringBuilder.append(length);
            Log.d(str2, stringBuilder.toString());
        }
        return length;
    }

    /* JADX WARNING: Missing block: B:19:0x009a, code skipped:
            return;
     */
    public void close() throws java.io.IOException {
        /*
        r4 = this;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "close() this: ";
        r0.append(r1);
        r0.append(r4);
        r1 = ", channel: ";
        r0.append(r1);
        r1 = r4.mPort;
        r0.append(r1);
        r1 = ", mSocketIS: ";
        r0.append(r1);
        r1 = r4.mSocketIS;
        r0.append(r1);
        r1 = ", mSocketOS: ";
        r0.append(r1);
        r1 = r4.mSocketOS;
        r0.append(r1);
        r1 = "mSocket: ";
        r0.append(r1);
        r1 = r4.mSocket;
        r0.append(r1);
        r1 = ", mSocketState: ";
        r0.append(r1);
        r1 = r4.mSocketState;
        r0.append(r1);
        r0 = r0.toString();
        r1 = "BluetoothSocket";
        android.util.Log.d(r1, r0);
        r0 = r4.mSocketState;
        r1 = android.bluetooth.BluetoothSocket.SocketState.CLOSED;
        if (r0 != r1) goto L_0x004f;
    L_0x004e:
        return;
    L_0x004f:
        monitor-enter(r4);
        r0 = r4.mSocketState;	 Catch:{ all -> 0x009b }
        r1 = android.bluetooth.BluetoothSocket.SocketState.CLOSED;	 Catch:{ all -> 0x009b }
        if (r0 != r1) goto L_0x0058;
    L_0x0056:
        monitor-exit(r4);	 Catch:{ all -> 0x009b }
        return;
    L_0x0058:
        r0 = android.bluetooth.BluetoothSocket.SocketState.CLOSED;	 Catch:{ all -> 0x009b }
        r4.mSocketState = r0;	 Catch:{ all -> 0x009b }
        r0 = r4.mSocket;	 Catch:{ all -> 0x009b }
        r1 = 0;
        if (r0 == 0) goto L_0x008e;
    L_0x0061:
        r0 = DBG;	 Catch:{ all -> 0x009b }
        if (r0 == 0) goto L_0x007d;
    L_0x0065:
        r0 = "BluetoothSocket";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x009b }
        r2.<init>();	 Catch:{ all -> 0x009b }
        r3 = "Closing mSocket: ";
        r2.append(r3);	 Catch:{ all -> 0x009b }
        r3 = r4.mSocket;	 Catch:{ all -> 0x009b }
        r2.append(r3);	 Catch:{ all -> 0x009b }
        r2 = r2.toString();	 Catch:{ all -> 0x009b }
        android.util.Log.d(r0, r2);	 Catch:{ all -> 0x009b }
    L_0x007d:
        r0 = r4.mSocket;	 Catch:{ all -> 0x009b }
        r0.shutdownInput();	 Catch:{ all -> 0x009b }
        r0 = r4.mSocket;	 Catch:{ all -> 0x009b }
        r0.shutdownOutput();	 Catch:{ all -> 0x009b }
        r0 = r4.mSocket;	 Catch:{ all -> 0x009b }
        r0.close();	 Catch:{ all -> 0x009b }
        r4.mSocket = r1;	 Catch:{ all -> 0x009b }
    L_0x008e:
        r0 = r4.mPfd;	 Catch:{ all -> 0x009b }
        if (r0 == 0) goto L_0x0099;
    L_0x0092:
        r0 = r4.mPfd;	 Catch:{ all -> 0x009b }
        r0.close();	 Catch:{ all -> 0x009b }
        r4.mPfd = r1;	 Catch:{ all -> 0x009b }
    L_0x0099:
        monitor-exit(r4);	 Catch:{ all -> 0x009b }
        return;
    L_0x009b:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x009b }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.BluetoothSocket.close():void");
    }

    /* Access modifiers changed, original: 0000 */
    public void removeChannel() {
    }

    /* Access modifiers changed, original: 0000 */
    public int getPort() {
        return this.mPort;
    }

    public int getMaxTransmitPacketSize() {
        return this.mMaxTxPacketSize;
    }

    public int getMaxReceivePacketSize() {
        return this.mMaxRxPacketSize;
    }

    public int getConnectionType() {
        int i = this.mType;
        if (i == 4) {
            return 3;
        }
        return i;
    }

    public void setExcludeSdp(boolean excludeSdp) {
        this.mExcludeSdp = excludeSdp;
    }

    public void requestMaximumTxDataLength() throws IOException {
        String str = TAG;
        if (this.mDevice != null) {
            try {
                if (this.mSocketState != SocketState.CLOSED) {
                    IBluetooth bluetoothProxy = BluetoothAdapter.getDefaultAdapter().getBluetoothService(null);
                    if (bluetoothProxy != null) {
                        if (DBG) {
                            Log.d(str, "requestMaximumTxDataLength");
                        }
                        bluetoothProxy.getSocketManager().requestMaximumTxDataLength(this.mDevice);
                        return;
                    }
                    throw new IOException("Bluetooth is off");
                }
                throw new IOException("socket closed");
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unable to send RPC: ");
                stringBuilder.append(e.getMessage());
                throw new IOException(stringBuilder.toString());
            }
        }
        throw new IOException("requestMaximumTxDataLength is called on null device");
    }

    private String convertAddr(byte[] addr) {
        return String.format(Locale.US, "%02X:%02X:%02X:%02X:%02X:%02X", new Object[]{Byte.valueOf(addr[0]), Byte.valueOf(addr[1]), Byte.valueOf(addr[2]), Byte.valueOf(addr[3]), Byte.valueOf(addr[4]), Byte.valueOf(addr[5])});
    }

    private String waitSocketSignal(InputStream is) throws IOException {
        byte[] sig = new byte[20];
        int ret = readAll(is, sig);
        boolean z = VDBG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("waitSocketSignal read 20 bytes signal ret: ");
            stringBuilder.append(ret);
            Log.d(str, stringBuilder.toString());
        }
        ByteBuffer bb = ByteBuffer.wrap(sig);
        bb.order(ByteOrder.nativeOrder());
        int size = bb.getShort();
        if (size == 20) {
            StringBuilder stringBuilder2;
            byte[] addr = new byte[6];
            bb.get(addr);
            int channel = bb.getInt();
            int status = bb.getInt();
            this.mMaxTxPacketSize = bb.getShort() & 65535;
            this.mMaxRxPacketSize = bb.getShort() & 65535;
            String RemoteAddr = convertAddr(addr);
            if (VDBG) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("waitSocketSignal: sig size: ");
                stringBuilder2.append(size);
                stringBuilder2.append(", remote addr: ");
                stringBuilder2.append(RemoteAddr);
                stringBuilder2.append(", channel: ");
                stringBuilder2.append(channel);
                stringBuilder2.append(", status: ");
                stringBuilder2.append(status);
                stringBuilder2.append(" MaxRxPktSize: ");
                stringBuilder2.append(this.mMaxRxPacketSize);
                stringBuilder2.append(" MaxTxPktSize: ");
                stringBuilder2.append(this.mMaxTxPacketSize);
                Log.d(str, stringBuilder2.toString());
            }
            if (status == 0) {
                return RemoteAddr;
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Connection failure, status: ");
            stringBuilder2.append(status);
            throw new IOException(stringBuilder2.toString());
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("Connection failure, wrong signal size: ");
        stringBuilder3.append(size);
        throw new IOException(stringBuilder3.toString());
    }

    private void createL2capRxBuffer() {
        int i = this.mType;
        if (i == 3 || i == 4) {
            StringBuilder stringBuilder;
            boolean z = VDBG;
            String str = TAG;
            if (z) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("  Creating mL2capBuffer: mMaxPacketSize: ");
                stringBuilder.append(this.mMaxRxPacketSize);
                Log.v(str, stringBuilder.toString());
            }
            this.mL2capBuffer = ByteBuffer.wrap(new byte[this.mMaxRxPacketSize]);
            if (VDBG) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("mL2capBuffer.remaining()");
                stringBuilder.append(this.mL2capBuffer.remaining());
                Log.v(str, stringBuilder.toString());
            }
            this.mL2capBuffer.limit(0);
            if (VDBG) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("mL2capBuffer.remaining() after limit(0):");
                stringBuilder.append(this.mL2capBuffer.remaining());
                Log.v(str, stringBuilder.toString());
            }
        }
    }

    private int readAll(InputStream is, byte[] b) throws IOException {
        int left = b.length;
        while (left > 0) {
            int ret = is.read(b, b.length - left, left);
            if (ret > 0) {
                left -= ret;
                if (left != 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("readAll() looping, read partial size: ");
                    stringBuilder.append(b.length - left);
                    stringBuilder.append(", expect size: ");
                    stringBuilder.append(b.length);
                    Log.w(TAG, stringBuilder.toString());
                }
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("read failed, socket might closed or timeout, read ret: ");
                stringBuilder2.append(ret);
                throw new IOException(stringBuilder2.toString());
            }
        }
        return b.length;
    }

    private int readInt(InputStream is) throws IOException {
        byte[] ibytes = new byte[4];
        int ret = readAll(is, ibytes);
        if (VDBG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("inputStream.read ret: ");
            stringBuilder.append(ret);
            Log.d(TAG, stringBuilder.toString());
        }
        ByteBuffer bb = ByteBuffer.wrap(ibytes);
        bb.order(ByteOrder.nativeOrder());
        return bb.getInt();
    }

    private int fillL2capRxBuffer() throws IOException {
        this.mL2capBuffer.rewind();
        int ret = this.mSocketIS.read(this.mL2capBuffer.array());
        if (ret == -1) {
            this.mL2capBuffer.limit(0);
            return -1;
        }
        this.mL2capBuffer.limit(ret);
        return ret;
    }

    public int setSocketOpt(int optionName, byte[] optionVal, int optionLen) throws IOException {
        if (this.mSocketState != SocketState.CLOSED) {
            IBluetooth bluetoothProxy = BluetoothAdapter.getDefaultAdapter().getBluetoothService(null);
            String str = TAG;
            if (bluetoothProxy == null) {
                Log.e(str, "setSocketOpt fail, reason: bluetooth is off");
                return -1;
            }
            try {
                if (VDBG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("setSocketOpt(), mType: ");
                    stringBuilder.append(this.mType);
                    stringBuilder.append(" mPort: ");
                    stringBuilder.append(this.mPort);
                    Log.d(str, stringBuilder.toString());
                }
                return bluetoothProxy.setSocketOpt(this.mType, this.mPort, optionName, optionVal, optionLen);
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
                return -1;
            }
        }
        throw new IOException("socket closed");
    }

    public int getSocketOpt(int optionName, byte[] optionVal) throws IOException {
        if (this.mSocketState != SocketState.CLOSED) {
            IBluetooth bluetoothProxy = BluetoothAdapter.getDefaultAdapter().getBluetoothService(null);
            String str = TAG;
            if (bluetoothProxy == null) {
                Log.e(str, "getSocketOpt fail, reason: bluetooth is off");
                return -1;
            }
            try {
                if (VDBG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("getSocketOpt(), mType: ");
                    stringBuilder.append(this.mType);
                    stringBuilder.append(" mPort: ");
                    stringBuilder.append(this.mPort);
                    Log.d(str, stringBuilder.toString());
                }
                return bluetoothProxy.getSocketOpt(this.mType, this.mPort, optionName, optionVal);
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
                return -1;
            }
        }
        throw new IOException("socket closed");
    }
}
