package android.net;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.Binder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.util.AndroidException;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public final class IpSecManager {
    public static final int DIRECTION_IN = 0;
    public static final int DIRECTION_OUT = 1;
    public static final int INVALID_RESOURCE_ID = -1;
    public static final int INVALID_SECURITY_PARAMETER_INDEX = 0;
    private static final String TAG = "IpSecManager";
    private final Context mContext;
    private final IIpSecService mService;

    @SystemApi
    public static final class IpSecTunnelInterface implements AutoCloseable {
        private final CloseGuard mCloseGuard;
        private String mInterfaceName;
        private final InetAddress mLocalAddress;
        private final String mOpPackageName;
        private final InetAddress mRemoteAddress;
        private int mResourceId;
        private final IIpSecService mService;
        private final Network mUnderlyingNetwork;

        public String getInterfaceName() {
            return this.mInterfaceName;
        }

        @SystemApi
        public void addAddress(InetAddress address, int prefixLen) throws IOException {
            try {
                this.mService.addAddressToTunnelInterface(this.mResourceId, new LinkAddress(address, prefixLen), this.mOpPackageName);
            } catch (ServiceSpecificException e) {
                throw IpSecManager.rethrowCheckedExceptionFromServiceSpecificException(e);
            } catch (RemoteException e2) {
                throw e2.rethrowFromSystemServer();
            }
        }

        @SystemApi
        public void removeAddress(InetAddress address, int prefixLen) throws IOException {
            try {
                this.mService.removeAddressFromTunnelInterface(this.mResourceId, new LinkAddress(address, prefixLen), this.mOpPackageName);
            } catch (ServiceSpecificException e) {
                throw IpSecManager.rethrowCheckedExceptionFromServiceSpecificException(e);
            } catch (RemoteException e2) {
                throw e2.rethrowFromSystemServer();
            }
        }

        private IpSecTunnelInterface(Context ctx, IIpSecService service, InetAddress localAddress, InetAddress remoteAddress, Network underlyingNetwork) throws ResourceUnavailableException, IOException {
            this.mCloseGuard = CloseGuard.get();
            this.mResourceId = -1;
            this.mOpPackageName = ctx.getOpPackageName();
            this.mService = service;
            this.mLocalAddress = localAddress;
            this.mRemoteAddress = remoteAddress;
            this.mUnderlyingNetwork = underlyingNetwork;
            try {
                IpSecTunnelInterfaceResponse result = this.mService.createTunnelInterface(localAddress.getHostAddress(), remoteAddress.getHostAddress(), underlyingNetwork, new Binder(), this.mOpPackageName);
                int i = result.status;
                if (i == 0) {
                    this.mResourceId = result.resourceId;
                    this.mInterfaceName = result.interfaceName;
                    this.mCloseGuard.open("constructor");
                } else if (i != 1) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown status returned by IpSecService: ");
                    stringBuilder.append(result.status);
                    throw new RuntimeException(stringBuilder.toString());
                } else {
                    throw new ResourceUnavailableException("No more tunnel interfaces may be allocated by this requester.");
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public void close() {
            try {
                this.mService.deleteTunnelInterface(this.mResourceId, this.mOpPackageName);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Exception e2) {
                String str = IpSecManager.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to close ");
                stringBuilder.append(this);
                stringBuilder.append(", Exception=");
                stringBuilder.append(e2);
                Log.e(str, stringBuilder.toString());
            } catch (Throwable th) {
                this.mResourceId = -1;
                this.mCloseGuard.close();
            }
            this.mResourceId = -1;
            this.mCloseGuard.close();
        }

        /* Access modifiers changed, original: protected */
        public void finalize() throws Throwable {
            CloseGuard closeGuard = this.mCloseGuard;
            if (closeGuard != null) {
                closeGuard.warnIfOpen();
            }
            close();
        }

        @VisibleForTesting
        public int getResourceId() {
            return this.mResourceId;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("IpSecTunnelInterface{ifname=");
            stringBuilder.append(this.mInterfaceName);
            stringBuilder.append(",resourceId=");
            stringBuilder.append(this.mResourceId);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PolicyDirection {
    }

    public static final class ResourceUnavailableException extends AndroidException {
        ResourceUnavailableException(String msg) {
            super(msg);
        }
    }

    public static final class SecurityParameterIndex implements AutoCloseable {
        private final CloseGuard mCloseGuard;
        private final InetAddress mDestinationAddress;
        private int mResourceId;
        private final IIpSecService mService;
        private int mSpi;

        public int getSpi() {
            return this.mSpi;
        }

        public void close() {
            try {
                this.mService.releaseSecurityParameterIndex(this.mResourceId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Exception e2) {
                String str = IpSecManager.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to close ");
                stringBuilder.append(this);
                stringBuilder.append(", Exception=");
                stringBuilder.append(e2);
                Log.e(str, stringBuilder.toString());
            } catch (Throwable th) {
                this.mResourceId = -1;
                this.mCloseGuard.close();
            }
            this.mResourceId = -1;
            this.mCloseGuard.close();
        }

        /* Access modifiers changed, original: protected */
        public void finalize() throws Throwable {
            CloseGuard closeGuard = this.mCloseGuard;
            if (closeGuard != null) {
                closeGuard.warnIfOpen();
            }
            close();
        }

        private SecurityParameterIndex(IIpSecService service, InetAddress destinationAddress, int spi) throws ResourceUnavailableException, SpiUnavailableException {
            this.mCloseGuard = CloseGuard.get();
            this.mSpi = 0;
            this.mResourceId = -1;
            this.mService = service;
            this.mDestinationAddress = destinationAddress;
            try {
                IpSecSpiResponse result = this.mService.allocateSecurityParameterIndex(destinationAddress.getHostAddress(), spi, new Binder());
                if (result != null) {
                    int status = result.status;
                    StringBuilder stringBuilder;
                    if (status == 0) {
                        this.mSpi = result.spi;
                        this.mResourceId = result.resourceId;
                        if (this.mSpi == 0) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Invalid SPI returned by IpSecService: ");
                            stringBuilder.append(status);
                            throw new RuntimeException(stringBuilder.toString());
                        } else if (this.mResourceId != -1) {
                            this.mCloseGuard.open("open");
                            return;
                        } else {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Invalid Resource ID returned by IpSecService: ");
                            stringBuilder.append(status);
                            throw new RuntimeException(stringBuilder.toString());
                        }
                    } else if (status == 1) {
                        throw new ResourceUnavailableException("No more SPIs may be allocated by this requester.");
                    } else if (status != 2) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown status returned by IpSecService: ");
                        stringBuilder.append(status);
                        throw new RuntimeException(stringBuilder.toString());
                    } else {
                        throw new SpiUnavailableException("Requested SPI is unavailable", spi);
                    }
                }
                throw new NullPointerException("Received null response from IpSecService");
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        @VisibleForTesting
        public int getResourceId() {
            return this.mResourceId;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SecurityParameterIndex{spi=");
            stringBuilder.append(this.mSpi);
            stringBuilder.append(",resourceId=");
            stringBuilder.append(this.mResourceId);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static final class SpiUnavailableException extends AndroidException {
        private final int mSpi;

        SpiUnavailableException(String msg, int spi) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(msg);
            stringBuilder.append(" (spi: ");
            stringBuilder.append(spi);
            stringBuilder.append(")");
            super(stringBuilder.toString());
            this.mSpi = spi;
        }

        public int getSpi() {
            return this.mSpi;
        }
    }

    public interface Status {
        public static final int OK = 0;
        public static final int RESOURCE_UNAVAILABLE = 1;
        public static final int SPI_UNAVAILABLE = 2;
    }

    public static final class UdpEncapsulationSocket implements AutoCloseable {
        private final CloseGuard mCloseGuard;
        private final ParcelFileDescriptor mPfd;
        private final int mPort;
        private int mResourceId;
        private final IIpSecService mService;

        private UdpEncapsulationSocket(IIpSecService service, int port) throws ResourceUnavailableException, IOException {
            this.mResourceId = -1;
            this.mCloseGuard = CloseGuard.get();
            this.mService = service;
            try {
                IpSecUdpEncapResponse result = this.mService.openUdpEncapsulationSocket(port, new Binder());
                int i = result.status;
                if (i == 0) {
                    this.mResourceId = result.resourceId;
                    this.mPort = result.port;
                    this.mPfd = result.fileDescriptor;
                    this.mCloseGuard.open("constructor");
                } else if (i != 1) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown status returned by IpSecService: ");
                    stringBuilder.append(result.status);
                    throw new RuntimeException(stringBuilder.toString());
                } else {
                    throw new ResourceUnavailableException("No more Sockets may be allocated by this requester.");
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public FileDescriptor getFileDescriptor() {
            ParcelFileDescriptor parcelFileDescriptor = this.mPfd;
            if (parcelFileDescriptor == null) {
                return null;
            }
            return parcelFileDescriptor.getFileDescriptor();
        }

        public int getPort() {
            return this.mPort;
        }

        public void close() throws IOException {
            String str = IpSecManager.TAG;
            try {
                this.mService.closeUdpEncapsulationSocket(this.mResourceId);
                this.mResourceId = -1;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Exception e2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to close ");
                stringBuilder.append(this);
                stringBuilder.append(", Exception=");
                stringBuilder.append(e2);
                Log.e(str, stringBuilder.toString());
            } catch (Throwable th) {
                this.mResourceId = -1;
                this.mCloseGuard.close();
            }
            this.mResourceId = -1;
            this.mCloseGuard.close();
            try {
                this.mPfd.close();
            } catch (IOException e3) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Failed to close UDP Encapsulation Socket with Port= ");
                stringBuilder2.append(this.mPort);
                Log.e(str, stringBuilder2.toString());
                throw e3;
            }
        }

        /* Access modifiers changed, original: protected */
        public void finalize() throws Throwable {
            CloseGuard closeGuard = this.mCloseGuard;
            if (closeGuard != null) {
                closeGuard.warnIfOpen();
            }
            close();
        }

        @VisibleForTesting
        public int getResourceId() {
            return this.mResourceId;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("UdpEncapsulationSocket{port=");
            stringBuilder.append(this.mPort);
            stringBuilder.append(",resourceId=");
            stringBuilder.append(this.mResourceId);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public SecurityParameterIndex allocateSecurityParameterIndex(InetAddress destinationAddress) throws ResourceUnavailableException {
        try {
            return new SecurityParameterIndex(this.mService, destinationAddress, 0);
        } catch (ServiceSpecificException e) {
            throw rethrowUncheckedExceptionFromServiceSpecificException(e);
        } catch (SpiUnavailableException e2) {
            throw new ResourceUnavailableException("No SPIs available");
        }
    }

    public SecurityParameterIndex allocateSecurityParameterIndex(InetAddress destinationAddress, int requestedSpi) throws SpiUnavailableException, ResourceUnavailableException {
        if (requestedSpi != 0) {
            try {
                return new SecurityParameterIndex(this.mService, destinationAddress, requestedSpi);
            } catch (ServiceSpecificException e) {
                throw rethrowUncheckedExceptionFromServiceSpecificException(e);
            }
        }
        throw new IllegalArgumentException("Requested SPI must be a valid (non-zero) SPI");
    }

    public void applyTransportModeTransform(Socket socket, int direction, IpSecTransform transform) throws IOException {
        socket.getSoLinger();
        applyTransportModeTransform(socket.getFileDescriptor$(), direction, transform);
    }

    public void applyTransportModeTransform(DatagramSocket socket, int direction, IpSecTransform transform) throws IOException {
        applyTransportModeTransform(socket.getFileDescriptor$(), direction, transform);
    }

    /* JADX WARNING: Missing block: B:12:0x0018, code skipped:
            if (r0 != null) goto L_0x001a;
     */
    /* JADX WARNING: Missing block: B:14:?, code skipped:
            $closeResource(r1, r0);
     */
    public void applyTransportModeTransform(java.io.FileDescriptor r5, int r6, android.net.IpSecTransform r7) throws java.io.IOException {
        /*
        r4 = this;
        r0 = android.os.ParcelFileDescriptor.dup(r5);	 Catch:{ ServiceSpecificException -> 0x0024, RemoteException -> 0x001e }
        r1 = 0;
        r2 = r4.mService;	 Catch:{ all -> 0x0015 }
        r3 = r7.getResourceId();	 Catch:{ all -> 0x0015 }
        r2.applyTransportModeTransform(r0, r6, r3);	 Catch:{ all -> 0x0015 }
        if (r0 == 0) goto L_0x0013;
    L_0x0010:
        $closeResource(r1, r0);	 Catch:{ ServiceSpecificException -> 0x0024, RemoteException -> 0x001e }
        return;
    L_0x0015:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0017 }
    L_0x0017:
        r2 = move-exception;
        if (r0 == 0) goto L_0x001d;
    L_0x001a:
        $closeResource(r1, r0);	 Catch:{ ServiceSpecificException -> 0x0024, RemoteException -> 0x001e }
    L_0x001d:
        throw r2;	 Catch:{ ServiceSpecificException -> 0x0024, RemoteException -> 0x001e }
    L_0x001e:
        r0 = move-exception;
        r1 = r0.rethrowFromSystemServer();
        throw r1;
    L_0x0024:
        r0 = move-exception;
        r1 = rethrowCheckedExceptionFromServiceSpecificException(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.IpSecManager.applyTransportModeTransform(java.io.FileDescriptor, int, android.net.IpSecTransform):void");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
                return;
            } catch (Throwable th) {
                x0.addSuppressed(th);
                return;
            }
        }
        x1.close();
    }

    public void removeTransportModeTransforms(Socket socket) throws IOException {
        socket.getSoLinger();
        removeTransportModeTransforms(socket.getFileDescriptor$());
    }

    public void removeTransportModeTransforms(DatagramSocket socket) throws IOException {
        removeTransportModeTransforms(socket.getFileDescriptor$());
    }

    /* JADX WARNING: Missing block: B:12:0x0014, code skipped:
            if (r0 != null) goto L_0x0016;
     */
    /* JADX WARNING: Missing block: B:14:?, code skipped:
            $closeResource(r1, r0);
     */
    public void removeTransportModeTransforms(java.io.FileDescriptor r4) throws java.io.IOException {
        /*
        r3 = this;
        r0 = android.os.ParcelFileDescriptor.dup(r4);	 Catch:{ ServiceSpecificException -> 0x0020, RemoteException -> 0x001a }
        r1 = 0;
        r2 = r3.mService;	 Catch:{ all -> 0x0011 }
        r2.removeTransportModeTransforms(r0);	 Catch:{ all -> 0x0011 }
        if (r0 == 0) goto L_0x000f;
    L_0x000c:
        $closeResource(r1, r0);	 Catch:{ ServiceSpecificException -> 0x0020, RemoteException -> 0x001a }
        return;
    L_0x0011:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0013 }
    L_0x0013:
        r2 = move-exception;
        if (r0 == 0) goto L_0x0019;
    L_0x0016:
        $closeResource(r1, r0);	 Catch:{ ServiceSpecificException -> 0x0020, RemoteException -> 0x001a }
    L_0x0019:
        throw r2;	 Catch:{ ServiceSpecificException -> 0x0020, RemoteException -> 0x001a }
    L_0x001a:
        r0 = move-exception;
        r1 = r0.rethrowFromSystemServer();
        throw r1;
    L_0x0020:
        r0 = move-exception;
        r1 = rethrowCheckedExceptionFromServiceSpecificException(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.IpSecManager.removeTransportModeTransforms(java.io.FileDescriptor):void");
    }

    public void removeTunnelModeTransform(Network net, IpSecTransform transform) {
    }

    public UdpEncapsulationSocket openUdpEncapsulationSocket(int port) throws IOException, ResourceUnavailableException {
        if (port != 0) {
            try {
                return new UdpEncapsulationSocket(this.mService, port);
            } catch (ServiceSpecificException e) {
                throw rethrowCheckedExceptionFromServiceSpecificException(e);
            }
        }
        throw new IllegalArgumentException("Specified port must be a valid port number!");
    }

    public UdpEncapsulationSocket openUdpEncapsulationSocket() throws IOException, ResourceUnavailableException {
        try {
            return new UdpEncapsulationSocket(this.mService, 0);
        } catch (ServiceSpecificException e) {
            throw rethrowCheckedExceptionFromServiceSpecificException(e);
        }
    }

    @SystemApi
    public IpSecTunnelInterface createIpSecTunnelInterface(InetAddress localAddress, InetAddress remoteAddress, Network underlyingNetwork) throws ResourceUnavailableException, IOException {
        try {
            return new IpSecTunnelInterface(this.mContext, this.mService, localAddress, remoteAddress, underlyingNetwork);
        } catch (ServiceSpecificException e) {
            throw rethrowCheckedExceptionFromServiceSpecificException(e);
        }
    }

    @SystemApi
    public void applyTunnelModeTransform(IpSecTunnelInterface tunnel, int direction, IpSecTransform transform) throws IOException {
        try {
            this.mService.applyTunnelModeTransform(tunnel.getResourceId(), direction, transform.getResourceId(), this.mContext.getOpPackageName());
        } catch (ServiceSpecificException e) {
            throw rethrowCheckedExceptionFromServiceSpecificException(e);
        } catch (RemoteException e2) {
            throw e2.rethrowFromSystemServer();
        }
    }

    public IpSecManager(Context ctx, IIpSecService service) {
        this.mContext = ctx;
        this.mService = (IIpSecService) Preconditions.checkNotNull(service, "missing service");
    }

    private static void maybeHandleServiceSpecificException(ServiceSpecificException sse) {
        if (sse.errorCode == OsConstants.EINVAL) {
            throw new IllegalArgumentException(sse);
        } else if (sse.errorCode == OsConstants.EAGAIN) {
            throw new IllegalStateException(sse);
        } else if (sse.errorCode == OsConstants.EOPNOTSUPP || sse.errorCode == OsConstants.EPROTONOSUPPORT) {
            throw new UnsupportedOperationException(sse);
        }
    }

    static RuntimeException rethrowUncheckedExceptionFromServiceSpecificException(ServiceSpecificException sse) {
        maybeHandleServiceSpecificException(sse);
        throw new RuntimeException(sse);
    }

    static IOException rethrowCheckedExceptionFromServiceSpecificException(ServiceSpecificException sse) throws IOException {
        maybeHandleServiceSpecificException(sse);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("IpSec encountered errno=");
        stringBuilder.append(sse.errorCode);
        throw new ErrnoException(stringBuilder.toString(), sse.errorCode).rethrowAsIOException();
    }
}
