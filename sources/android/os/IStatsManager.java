package android.os;

public interface IStatsManager extends IInterface {
    public static final int FLAG_REQUIRE_LOW_LATENCY_MONITOR = 4;
    public static final int FLAG_REQUIRE_STAGING = 1;
    public static final int FLAG_ROLLBACK_ENABLED = 2;

    public static class Default implements IStatsManager {
        public void systemRunning() throws RemoteException {
        }

        public void statsCompanionReady() throws RemoteException {
        }

        public void informAnomalyAlarmFired() throws RemoteException {
        }

        public void informPollAlarmFired() throws RemoteException {
        }

        public void informAlarmForSubscriberTriggeringFired() throws RemoteException {
        }

        public void informDeviceShutdown() throws RemoteException {
        }

        public void informAllUidData(ParcelFileDescriptor fd) throws RemoteException {
        }

        public void informOnePackage(String app, int uid, long version, String version_string, String installer) throws RemoteException {
        }

        public void informOnePackageRemoved(String app, int uid) throws RemoteException {
        }

        public byte[] getData(long key, String packageName) throws RemoteException {
            return null;
        }

        public byte[] getMetadata(String packageName) throws RemoteException {
            return null;
        }

        public void addConfiguration(long configKey, byte[] config, String packageName) throws RemoteException {
        }

        public void setDataFetchOperation(long configKey, IBinder intentSender, String packageName) throws RemoteException {
        }

        public void removeDataFetchOperation(long configKey, String packageName) throws RemoteException {
        }

        public long[] setActiveConfigsChangedOperation(IBinder intentSender, String packageName) throws RemoteException {
            return null;
        }

        public void removeActiveConfigsChangedOperation(String packageName) throws RemoteException {
        }

        public void removeConfiguration(long configKey, String packageName) throws RemoteException {
        }

        public void setBroadcastSubscriber(long configKey, long subscriberId, IBinder intentSender, String packageName) throws RemoteException {
        }

        public void unsetBroadcastSubscriber(long configKey, long subscriberId, String packageName) throws RemoteException {
        }

        public void sendAppBreadcrumbAtom(int label, int state) throws RemoteException {
        }

        public void registerPullerCallback(int atomTag, IStatsPullerCallback pullerCallback, String packageName) throws RemoteException {
        }

        public void unregisterPullerCallback(int atomTag, String packageName) throws RemoteException {
        }

        public void sendBinaryPushStateChangedAtom(String trainName, long trainVersionCode, int options, int state, long[] experimentId) throws RemoteException {
        }

        public void sendWatchdogRollbackOccurredAtom(int rollbackType, String packageName, long packageVersionCode) throws RemoteException {
        }

        public long[] getRegisteredExperimentIds() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IStatsManager {
        private static final String DESCRIPTOR = "android.os.IStatsManager";
        static final int TRANSACTION_addConfiguration = 12;
        static final int TRANSACTION_getData = 10;
        static final int TRANSACTION_getMetadata = 11;
        static final int TRANSACTION_getRegisteredExperimentIds = 25;
        static final int TRANSACTION_informAlarmForSubscriberTriggeringFired = 5;
        static final int TRANSACTION_informAllUidData = 7;
        static final int TRANSACTION_informAnomalyAlarmFired = 3;
        static final int TRANSACTION_informDeviceShutdown = 6;
        static final int TRANSACTION_informOnePackage = 8;
        static final int TRANSACTION_informOnePackageRemoved = 9;
        static final int TRANSACTION_informPollAlarmFired = 4;
        static final int TRANSACTION_registerPullerCallback = 21;
        static final int TRANSACTION_removeActiveConfigsChangedOperation = 16;
        static final int TRANSACTION_removeConfiguration = 17;
        static final int TRANSACTION_removeDataFetchOperation = 14;
        static final int TRANSACTION_sendAppBreadcrumbAtom = 20;
        static final int TRANSACTION_sendBinaryPushStateChangedAtom = 23;
        static final int TRANSACTION_sendWatchdogRollbackOccurredAtom = 24;
        static final int TRANSACTION_setActiveConfigsChangedOperation = 15;
        static final int TRANSACTION_setBroadcastSubscriber = 18;
        static final int TRANSACTION_setDataFetchOperation = 13;
        static final int TRANSACTION_statsCompanionReady = 2;
        static final int TRANSACTION_systemRunning = 1;
        static final int TRANSACTION_unregisterPullerCallback = 22;
        static final int TRANSACTION_unsetBroadcastSubscriber = 19;

        private static class Proxy implements IStatsManager {
            public static IStatsManager sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void systemRunning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().systemRunning();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void statsCompanionReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().statsCompanionReady();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void informAnomalyAlarmFired() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().informAnomalyAlarmFired();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void informPollAlarmFired() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().informPollAlarmFired();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void informAlarmForSubscriberTriggeringFired() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().informAlarmForSubscriberTriggeringFired();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void informDeviceShutdown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().informDeviceShutdown();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void informAllUidData(ParcelFileDescriptor fd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().informAllUidData(fd);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void informOnePackage(String app, int uid, long version, String version_string, String installer) throws RemoteException {
                Throwable th;
                int i;
                long j;
                String str;
                String str2;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(app);
                    } catch (Throwable th2) {
                        th = th2;
                        i = uid;
                        j = version;
                        str = version_string;
                        str2 = installer;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(uid);
                        try {
                            _data.writeLong(version);
                            try {
                                _data.writeString(version_string);
                                try {
                                    _data.writeString(installer);
                                } catch (Throwable th3) {
                                    th = th3;
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                str2 = installer;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            str = version_string;
                            str2 = installer;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().informOnePackage(app, uid, version, version_string, installer);
                            _data.recycle();
                        } catch (Throwable th6) {
                            th = th6;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        j = version;
                        str = version_string;
                        str2 = installer;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    String str3 = app;
                    i = uid;
                    j = version;
                    str = version_string;
                    str2 = installer;
                    _data.recycle();
                    throw th;
                }
            }

            public void informOnePackageRemoved(String app, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(app);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().informOnePackageRemoved(app, uid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public byte[] getData(long key, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(key);
                    _data.writeString(packageName);
                    byte[] bArr = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().getData(key, packageName);
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getMetadata(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    byte[] bArr = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().getMetadata(packageName);
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addConfiguration(long configKey, byte[] config, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(configKey);
                    _data.writeByteArray(config);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addConfiguration(configKey, config, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDataFetchOperation(long configKey, IBinder intentSender, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(configKey);
                    _data.writeStrongBinder(intentSender);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDataFetchOperation(configKey, intentSender, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeDataFetchOperation(long configKey, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(configKey);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeDataFetchOperation(configKey, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long[] setActiveConfigsChangedOperation(IBinder intentSender, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(intentSender);
                    _data.writeString(packageName);
                    long[] jArr = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        jArr = Stub.getDefaultImpl();
                        if (jArr != 0) {
                            jArr = Stub.getDefaultImpl().setActiveConfigsChangedOperation(intentSender, packageName);
                            return jArr;
                        }
                    }
                    _reply.readException();
                    jArr = _reply.createLongArray();
                    long[] _result = jArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeActiveConfigsChangedOperation(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeActiveConfigsChangedOperation(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeConfiguration(long configKey, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(configKey);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeConfiguration(configKey, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBroadcastSubscriber(long configKey, long subscriberId, IBinder intentSender, String packageName) throws RemoteException {
                Throwable th;
                long j;
                IBinder iBinder;
                String str;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeLong(configKey);
                    } catch (Throwable th2) {
                        th = th2;
                        j = subscriberId;
                        iBinder = intentSender;
                        str = packageName;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(subscriberId);
                    } catch (Throwable th3) {
                        th = th3;
                        iBinder = intentSender;
                        str = packageName;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeStrongBinder(intentSender);
                        try {
                            _data.writeString(packageName);
                            if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().setBroadcastSubscriber(configKey, subscriberId, intentSender, packageName);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        str = packageName;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    long j2 = configKey;
                    j = subscriberId;
                    iBinder = intentSender;
                    str = packageName;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void unsetBroadcastSubscriber(long configKey, long subscriberId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(configKey);
                    _data.writeLong(subscriberId);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unsetBroadcastSubscriber(configKey, subscriberId, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendAppBreadcrumbAtom(int label, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(label);
                    _data.writeInt(state);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendAppBreadcrumbAtom(label, state);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerPullerCallback(int atomTag, IStatsPullerCallback pullerCallback, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(atomTag);
                    _data.writeStrongBinder(pullerCallback != null ? pullerCallback.asBinder() : null);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(21, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registerPullerCallback(atomTag, pullerCallback, packageName);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void unregisterPullerCallback(int atomTag, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(atomTag);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(22, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unregisterPullerCallback(atomTag, packageName);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void sendBinaryPushStateChangedAtom(String trainName, long trainVersionCode, int options, int state, long[] experimentId) throws RemoteException {
                Throwable th;
                long j;
                int i;
                int i2;
                long[] jArr;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(trainName);
                    } catch (Throwable th2) {
                        th = th2;
                        j = trainVersionCode;
                        i = options;
                        i2 = state;
                        jArr = experimentId;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(trainVersionCode);
                        try {
                            _data.writeInt(options);
                            try {
                                _data.writeInt(state);
                                try {
                                    _data.writeLongArray(experimentId);
                                } catch (Throwable th3) {
                                    th = th3;
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                jArr = experimentId;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            i2 = state;
                            jArr = experimentId;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            if (this.mRemote.transact(23, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().sendBinaryPushStateChangedAtom(trainName, trainVersionCode, options, state, experimentId);
                            _data.recycle();
                        } catch (Throwable th6) {
                            th = th6;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        i = options;
                        i2 = state;
                        jArr = experimentId;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    String str = trainName;
                    j = trainVersionCode;
                    i = options;
                    i2 = state;
                    jArr = experimentId;
                    _data.recycle();
                    throw th;
                }
            }

            public void sendWatchdogRollbackOccurredAtom(int rollbackType, String packageName, long packageVersionCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rollbackType);
                    _data.writeString(packageName);
                    _data.writeLong(packageVersionCode);
                    if (this.mRemote.transact(24, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().sendWatchdogRollbackOccurredAtom(rollbackType, packageName, packageVersionCode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public long[] getRegisteredExperimentIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long[] jArr = 25;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        jArr = Stub.getDefaultImpl();
                        if (jArr != 0) {
                            jArr = Stub.getDefaultImpl().getRegisteredExperimentIds();
                            return jArr;
                        }
                    }
                    _reply.readException();
                    jArr = _reply.createLongArray();
                    long[] _result = jArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IStatsManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IStatsManager)) {
                return new Proxy(obj);
            }
            return (IStatsManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "systemRunning";
                case 2:
                    return "statsCompanionReady";
                case 3:
                    return "informAnomalyAlarmFired";
                case 4:
                    return "informPollAlarmFired";
                case 5:
                    return "informAlarmForSubscriberTriggeringFired";
                case 6:
                    return "informDeviceShutdown";
                case 7:
                    return "informAllUidData";
                case 8:
                    return "informOnePackage";
                case 9:
                    return "informOnePackageRemoved";
                case 10:
                    return "getData";
                case 11:
                    return "getMetadata";
                case 12:
                    return "addConfiguration";
                case 13:
                    return "setDataFetchOperation";
                case 14:
                    return "removeDataFetchOperation";
                case 15:
                    return "setActiveConfigsChangedOperation";
                case 16:
                    return "removeActiveConfigsChangedOperation";
                case 17:
                    return "removeConfiguration";
                case 18:
                    return "setBroadcastSubscriber";
                case 19:
                    return "unsetBroadcastSubscriber";
                case 20:
                    return "sendAppBreadcrumbAtom";
                case 21:
                    return "registerPullerCallback";
                case 22:
                    return "unregisterPullerCallback";
                case 23:
                    return "sendBinaryPushStateChangedAtom";
                case 24:
                    return "sendWatchdogRollbackOccurredAtom";
                case 25:
                    return "getRegisteredExperimentIds";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        systemRunning();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        statsCompanionReady();
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        informAnomalyAlarmFired();
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        informPollAlarmFired();
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        informAlarmForSubscriberTriggeringFired();
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        informDeviceShutdown();
                        reply.writeNoException();
                        return true;
                    case 7:
                        ParcelFileDescriptor _arg0;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        informAllUidData(_arg0);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        informOnePackage(data.readString(), data.readInt(), data.readLong(), data.readString(), data.readString());
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        informOnePackageRemoved(data.readString(), data.readInt());
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        byte[] _result = getData(data.readLong(), data.readString());
                        reply.writeNoException();
                        parcel2.writeByteArray(_result);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        byte[] _result2 = getMetadata(data.readString());
                        reply.writeNoException();
                        parcel2.writeByteArray(_result2);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        addConfiguration(data.readLong(), data.createByteArray(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        setDataFetchOperation(data.readLong(), data.readStrongBinder(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        removeDataFetchOperation(data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        long[] _result3 = setActiveConfigsChangedOperation(data.readStrongBinder(), data.readString());
                        reply.writeNoException();
                        parcel2.writeLongArray(_result3);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        removeActiveConfigsChangedOperation(data.readString());
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        removeConfiguration(data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        setBroadcastSubscriber(data.readLong(), data.readLong(), data.readStrongBinder(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        unsetBroadcastSubscriber(data.readLong(), data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        sendAppBreadcrumbAtom(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        registerPullerCallback(data.readInt(), android.os.IStatsPullerCallback.Stub.asInterface(data.readStrongBinder()), data.readString());
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        unregisterPullerCallback(data.readInt(), data.readString());
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        sendBinaryPushStateChangedAtom(data.readString(), data.readLong(), data.readInt(), data.readInt(), data.createLongArray());
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        sendWatchdogRollbackOccurredAtom(data.readInt(), data.readString(), data.readLong());
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        long[] _result4 = getRegisteredExperimentIds();
                        reply.writeNoException();
                        parcel2.writeLongArray(_result4);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IStatsManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IStatsManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addConfiguration(long j, byte[] bArr, String str) throws RemoteException;

    byte[] getData(long j, String str) throws RemoteException;

    byte[] getMetadata(String str) throws RemoteException;

    long[] getRegisteredExperimentIds() throws RemoteException;

    void informAlarmForSubscriberTriggeringFired() throws RemoteException;

    void informAllUidData(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void informAnomalyAlarmFired() throws RemoteException;

    void informDeviceShutdown() throws RemoteException;

    void informOnePackage(String str, int i, long j, String str2, String str3) throws RemoteException;

    void informOnePackageRemoved(String str, int i) throws RemoteException;

    void informPollAlarmFired() throws RemoteException;

    void registerPullerCallback(int i, IStatsPullerCallback iStatsPullerCallback, String str) throws RemoteException;

    void removeActiveConfigsChangedOperation(String str) throws RemoteException;

    void removeConfiguration(long j, String str) throws RemoteException;

    void removeDataFetchOperation(long j, String str) throws RemoteException;

    void sendAppBreadcrumbAtom(int i, int i2) throws RemoteException;

    void sendBinaryPushStateChangedAtom(String str, long j, int i, int i2, long[] jArr) throws RemoteException;

    void sendWatchdogRollbackOccurredAtom(int i, String str, long j) throws RemoteException;

    long[] setActiveConfigsChangedOperation(IBinder iBinder, String str) throws RemoteException;

    void setBroadcastSubscriber(long j, long j2, IBinder iBinder, String str) throws RemoteException;

    void setDataFetchOperation(long j, IBinder iBinder, String str) throws RemoteException;

    void statsCompanionReady() throws RemoteException;

    void systemRunning() throws RemoteException;

    void unregisterPullerCallback(int i, String str) throws RemoteException;

    void unsetBroadcastSubscriber(long j, long j2, String str) throws RemoteException;
}
