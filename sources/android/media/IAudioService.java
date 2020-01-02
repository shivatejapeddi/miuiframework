package android.media;

import android.annotation.UnsupportedAppUsage;
import android.bluetooth.BluetoothDevice;
import android.media.PlayerBase.PlayerIdCard;
import android.media.audiopolicy.AudioPolicyConfig;
import android.media.audiopolicy.AudioProductStrategy;
import android.media.audiopolicy.AudioVolumeGroup;
import android.media.audiopolicy.IAudioPolicyCallback;
import android.media.projection.IMediaProjection;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import java.util.List;

public interface IAudioService extends IInterface {

    public static class Default implements IAudioService {
        public int trackPlayer(PlayerIdCard pic) throws RemoteException {
            return 0;
        }

        public void playerAttributes(int piid, AudioAttributes attr) throws RemoteException {
        }

        public void playerEvent(int piid, int event) throws RemoteException {
        }

        public void releasePlayer(int piid) throws RemoteException {
        }

        public int trackRecorder(IBinder recorder) throws RemoteException {
            return 0;
        }

        public void recorderEvent(int riid, int event) throws RemoteException {
        }

        public void releaseRecorder(int riid) throws RemoteException {
        }

        public void adjustSuggestedStreamVolume(int direction, int suggestedStreamType, int flags, String callingPackage, String caller) throws RemoteException {
        }

        public void adjustStreamVolume(int streamType, int direction, int flags, String callingPackage) throws RemoteException {
        }

        public void setStreamVolume(int streamType, int index, int flags, String callingPackage) throws RemoteException {
        }

        public boolean isStreamMute(int streamType) throws RemoteException {
            return false;
        }

        public void forceRemoteSubmixFullVolume(boolean startForcing, IBinder cb) throws RemoteException {
        }

        public boolean isMasterMute() throws RemoteException {
            return false;
        }

        public void setMasterMute(boolean mute, int flags, String callingPackage, int userId) throws RemoteException {
        }

        public int getStreamVolume(int streamType) throws RemoteException {
            return 0;
        }

        public int getStreamMinVolume(int streamType) throws RemoteException {
            return 0;
        }

        public int getStreamMaxVolume(int streamType) throws RemoteException {
            return 0;
        }

        public List<AudioVolumeGroup> getAudioVolumeGroups() throws RemoteException {
            return null;
        }

        public void setVolumeIndexForAttributes(AudioAttributes aa, int index, int flags, String callingPackage) throws RemoteException {
        }

        public int getVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
            return 0;
        }

        public int getMaxVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
            return 0;
        }

        public int getMinVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
            return 0;
        }

        public int getLastAudibleStreamVolume(int streamType) throws RemoteException {
            return 0;
        }

        public List<AudioProductStrategy> getAudioProductStrategies() throws RemoteException {
            return null;
        }

        public void setMicrophoneMute(boolean on, String callingPackage, int userId) throws RemoteException {
        }

        public void setRingerModeExternal(int ringerMode, String caller) throws RemoteException {
        }

        public void setRingerModeInternal(int ringerMode, String caller) throws RemoteException {
        }

        public int getRingerModeExternal() throws RemoteException {
            return 0;
        }

        public int getRingerModeInternal() throws RemoteException {
            return 0;
        }

        public boolean isValidRingerMode(int ringerMode) throws RemoteException {
            return false;
        }

        public void setVibrateSetting(int vibrateType, int vibrateSetting) throws RemoteException {
        }

        public int getVibrateSetting(int vibrateType) throws RemoteException {
            return 0;
        }

        public boolean shouldVibrate(int vibrateType) throws RemoteException {
            return false;
        }

        public void setMode(int mode, IBinder cb, String callingPackage) throws RemoteException {
        }

        public int getMode() throws RemoteException {
            return 0;
        }

        public void playSoundEffect(int effectType) throws RemoteException {
        }

        public void playSoundEffectVolume(int effectType, float volume) throws RemoteException {
        }

        public boolean loadSoundEffects() throws RemoteException {
            return false;
        }

        public void unloadSoundEffects() throws RemoteException {
        }

        public void reloadAudioSettings() throws RemoteException {
        }

        public void avrcpSupportsAbsoluteVolume(String address, boolean support) throws RemoteException {
        }

        public void setSpeakerphoneOn(boolean on) throws RemoteException {
        }

        public boolean isSpeakerphoneOn() throws RemoteException {
            return false;
        }

        public void setBluetoothScoOn(boolean on) throws RemoteException {
        }

        public boolean isBluetoothScoOn() throws RemoteException {
            return false;
        }

        public void setBluetoothA2dpOn(boolean on) throws RemoteException {
        }

        public boolean isBluetoothA2dpOn() throws RemoteException {
            return false;
        }

        public int requestAudioFocus(AudioAttributes aa, int durationHint, IBinder cb, IAudioFocusDispatcher fd, String clientId, String callingPackageName, int flags, IAudioPolicyCallback pcb, int sdk) throws RemoteException {
            return 0;
        }

        public int abandonAudioFocus(IAudioFocusDispatcher fd, String clientId, AudioAttributes aa, String callingPackageName) throws RemoteException {
            return 0;
        }

        public void unregisterAudioFocusClient(String clientId) throws RemoteException {
        }

        public int getCurrentAudioFocus() throws RemoteException {
            return 0;
        }

        public void startBluetoothSco(IBinder cb, int targetSdkVersion) throws RemoteException {
        }

        public void startBluetoothScoVirtualCall(IBinder cb) throws RemoteException {
        }

        public void stopBluetoothSco(IBinder cb) throws RemoteException {
        }

        public void forceVolumeControlStream(int streamType, IBinder cb) throws RemoteException {
        }

        public void setRingtonePlayer(IRingtonePlayer player) throws RemoteException {
        }

        public IRingtonePlayer getRingtonePlayer() throws RemoteException {
            return null;
        }

        public int getUiSoundsStreamType() throws RemoteException {
            return 0;
        }

        public void setWiredDeviceConnectionState(int type, int state, String address, String name, String caller) throws RemoteException {
        }

        public void handleBluetoothA2dpDeviceConfigChange(BluetoothDevice device) throws RemoteException {
        }

        public void handleBluetoothA2dpActiveDeviceChange(BluetoothDevice device, int state, int profile, boolean suppressNoisyIntent, int a2dpVolume) throws RemoteException {
        }

        public AudioRoutesInfo startWatchingRoutes(IAudioRoutesObserver observer) throws RemoteException {
            return null;
        }

        public boolean isCameraSoundForced() throws RemoteException {
            return false;
        }

        public void setVolumeController(IVolumeController controller) throws RemoteException {
        }

        public void notifyVolumeControllerVisible(IVolumeController controller, boolean visible) throws RemoteException {
        }

        public boolean isStreamAffectedByRingerMode(int streamType) throws RemoteException {
            return false;
        }

        public boolean isStreamAffectedByMute(int streamType) throws RemoteException {
            return false;
        }

        public void disableSafeMediaVolume(String callingPackage) throws RemoteException {
        }

        public int setHdmiSystemAudioSupported(boolean on) throws RemoteException {
            return 0;
        }

        public boolean isHdmiSystemAudioSupported() throws RemoteException {
            return false;
        }

        public String registerAudioPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb, boolean hasFocusListener, boolean isFocusPolicy, boolean isTestFocusPolicy, boolean isVolumeController, IMediaProjection projection) throws RemoteException {
            return null;
        }

        public void unregisterAudioPolicyAsync(IAudioPolicyCallback pcb) throws RemoteException {
        }

        public void unregisterAudioPolicy(IAudioPolicyCallback pcb) throws RemoteException {
        }

        public int addMixForPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb) throws RemoteException {
            return 0;
        }

        public int removeMixForPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb) throws RemoteException {
            return 0;
        }

        public int setFocusPropertiesForPolicy(int duckingBehavior, IAudioPolicyCallback pcb) throws RemoteException {
            return 0;
        }

        public void setVolumePolicy(VolumePolicy policy) throws RemoteException {
        }

        public boolean hasRegisteredDynamicPolicy() throws RemoteException {
            return false;
        }

        public void registerRecordingCallback(IRecordingConfigDispatcher rcdb) throws RemoteException {
        }

        public void unregisterRecordingCallback(IRecordingConfigDispatcher rcdb) throws RemoteException {
        }

        public List<AudioRecordingConfiguration> getActiveRecordingConfigurations() throws RemoteException {
            return null;
        }

        public void registerPlaybackCallback(IPlaybackConfigDispatcher pcdb) throws RemoteException {
        }

        public void unregisterPlaybackCallback(IPlaybackConfigDispatcher pcdb) throws RemoteException {
        }

        public List<AudioPlaybackConfiguration> getActivePlaybackConfigurations() throws RemoteException {
            return null;
        }

        public void disableRingtoneSync(int userId) throws RemoteException {
        }

        public int getFocusRampTimeMs(int focusGain, AudioAttributes attr) throws RemoteException {
            return 0;
        }

        public int dispatchFocusChange(AudioFocusInfo afi, int focusChange, IAudioPolicyCallback pcb) throws RemoteException {
            return 0;
        }

        public void playerHasOpPlayAudio(int piid, boolean hasOpPlayAudio) throws RemoteException {
        }

        public void setBluetoothHearingAidDeviceConnectionState(BluetoothDevice device, int state, boolean suppressNoisyIntent, int musicDevice) throws RemoteException {
        }

        public void setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(BluetoothDevice device, int state, int profile, boolean suppressNoisyIntent, int a2dpVolume) throws RemoteException {
        }

        public void setFocusRequestResultFromExtPolicy(AudioFocusInfo afi, int requestResult, IAudioPolicyCallback pcb) throws RemoteException {
        }

        public void registerAudioServerStateDispatcher(IAudioServerStateDispatcher asd) throws RemoteException {
        }

        public void unregisterAudioServerStateDispatcher(IAudioServerStateDispatcher asd) throws RemoteException {
        }

        public boolean isAudioServerRunning() throws RemoteException {
            return false;
        }

        public int setUidDeviceAffinity(IAudioPolicyCallback pcb, int uid, int[] deviceTypes, String[] deviceAddresses) throws RemoteException {
            return 0;
        }

        public int removeUidDeviceAffinity(IAudioPolicyCallback pcb, int uid) throws RemoteException {
            return 0;
        }

        public boolean hasHapticChannels(Uri uri) throws RemoteException {
            return false;
        }

        public IBinder createAudioRecordForLoopback(ParcelFileDescriptor sharedMem, long size) throws RemoteException {
            return null;
        }

        public String getNotificationUri(String type) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAudioService {
        private static final String DESCRIPTOR = "android.media.IAudioService";
        static final int TRANSACTION_abandonAudioFocus = 49;
        static final int TRANSACTION_addMixForPolicy = 74;
        static final int TRANSACTION_adjustStreamVolume = 9;
        static final int TRANSACTION_adjustSuggestedStreamVolume = 8;
        static final int TRANSACTION_avrcpSupportsAbsoluteVolume = 41;
        static final int TRANSACTION_createAudioRecordForLoopback = 98;
        static final int TRANSACTION_disableRingtoneSync = 85;
        static final int TRANSACTION_disableSafeMediaVolume = 68;
        static final int TRANSACTION_dispatchFocusChange = 87;
        static final int TRANSACTION_forceRemoteSubmixFullVolume = 12;
        static final int TRANSACTION_forceVolumeControlStream = 55;
        static final int TRANSACTION_getActivePlaybackConfigurations = 84;
        static final int TRANSACTION_getActiveRecordingConfigurations = 81;
        static final int TRANSACTION_getAudioProductStrategies = 24;
        static final int TRANSACTION_getAudioVolumeGroups = 18;
        static final int TRANSACTION_getCurrentAudioFocus = 51;
        static final int TRANSACTION_getFocusRampTimeMs = 86;
        static final int TRANSACTION_getLastAudibleStreamVolume = 23;
        static final int TRANSACTION_getMaxVolumeIndexForAttributes = 21;
        static final int TRANSACTION_getMinVolumeIndexForAttributes = 22;
        static final int TRANSACTION_getMode = 35;
        static final int TRANSACTION_getNotificationUri = 99;
        static final int TRANSACTION_getRingerModeExternal = 28;
        static final int TRANSACTION_getRingerModeInternal = 29;
        static final int TRANSACTION_getRingtonePlayer = 57;
        static final int TRANSACTION_getStreamMaxVolume = 17;
        static final int TRANSACTION_getStreamMinVolume = 16;
        static final int TRANSACTION_getStreamVolume = 15;
        static final int TRANSACTION_getUiSoundsStreamType = 58;
        static final int TRANSACTION_getVibrateSetting = 32;
        static final int TRANSACTION_getVolumeIndexForAttributes = 20;
        static final int TRANSACTION_handleBluetoothA2dpActiveDeviceChange = 61;
        static final int TRANSACTION_handleBluetoothA2dpDeviceConfigChange = 60;
        static final int TRANSACTION_hasHapticChannels = 97;
        static final int TRANSACTION_hasRegisteredDynamicPolicy = 78;
        static final int TRANSACTION_isAudioServerRunning = 94;
        static final int TRANSACTION_isBluetoothA2dpOn = 47;
        static final int TRANSACTION_isBluetoothScoOn = 45;
        static final int TRANSACTION_isCameraSoundForced = 63;
        static final int TRANSACTION_isHdmiSystemAudioSupported = 70;
        static final int TRANSACTION_isMasterMute = 13;
        static final int TRANSACTION_isSpeakerphoneOn = 43;
        static final int TRANSACTION_isStreamAffectedByMute = 67;
        static final int TRANSACTION_isStreamAffectedByRingerMode = 66;
        static final int TRANSACTION_isStreamMute = 11;
        static final int TRANSACTION_isValidRingerMode = 30;
        static final int TRANSACTION_loadSoundEffects = 38;
        static final int TRANSACTION_notifyVolumeControllerVisible = 65;
        static final int TRANSACTION_playSoundEffect = 36;
        static final int TRANSACTION_playSoundEffectVolume = 37;
        static final int TRANSACTION_playerAttributes = 2;
        static final int TRANSACTION_playerEvent = 3;
        static final int TRANSACTION_playerHasOpPlayAudio = 88;
        static final int TRANSACTION_recorderEvent = 6;
        static final int TRANSACTION_registerAudioPolicy = 71;
        static final int TRANSACTION_registerAudioServerStateDispatcher = 92;
        static final int TRANSACTION_registerPlaybackCallback = 82;
        static final int TRANSACTION_registerRecordingCallback = 79;
        static final int TRANSACTION_releasePlayer = 4;
        static final int TRANSACTION_releaseRecorder = 7;
        static final int TRANSACTION_reloadAudioSettings = 40;
        static final int TRANSACTION_removeMixForPolicy = 75;
        static final int TRANSACTION_removeUidDeviceAffinity = 96;
        static final int TRANSACTION_requestAudioFocus = 48;
        static final int TRANSACTION_setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent = 90;
        static final int TRANSACTION_setBluetoothA2dpOn = 46;
        static final int TRANSACTION_setBluetoothHearingAidDeviceConnectionState = 89;
        static final int TRANSACTION_setBluetoothScoOn = 44;
        static final int TRANSACTION_setFocusPropertiesForPolicy = 76;
        static final int TRANSACTION_setFocusRequestResultFromExtPolicy = 91;
        static final int TRANSACTION_setHdmiSystemAudioSupported = 69;
        static final int TRANSACTION_setMasterMute = 14;
        static final int TRANSACTION_setMicrophoneMute = 25;
        static final int TRANSACTION_setMode = 34;
        static final int TRANSACTION_setRingerModeExternal = 26;
        static final int TRANSACTION_setRingerModeInternal = 27;
        static final int TRANSACTION_setRingtonePlayer = 56;
        static final int TRANSACTION_setSpeakerphoneOn = 42;
        static final int TRANSACTION_setStreamVolume = 10;
        static final int TRANSACTION_setUidDeviceAffinity = 95;
        static final int TRANSACTION_setVibrateSetting = 31;
        static final int TRANSACTION_setVolumeController = 64;
        static final int TRANSACTION_setVolumeIndexForAttributes = 19;
        static final int TRANSACTION_setVolumePolicy = 77;
        static final int TRANSACTION_setWiredDeviceConnectionState = 59;
        static final int TRANSACTION_shouldVibrate = 33;
        static final int TRANSACTION_startBluetoothSco = 52;
        static final int TRANSACTION_startBluetoothScoVirtualCall = 53;
        static final int TRANSACTION_startWatchingRoutes = 62;
        static final int TRANSACTION_stopBluetoothSco = 54;
        static final int TRANSACTION_trackPlayer = 1;
        static final int TRANSACTION_trackRecorder = 5;
        static final int TRANSACTION_unloadSoundEffects = 39;
        static final int TRANSACTION_unregisterAudioFocusClient = 50;
        static final int TRANSACTION_unregisterAudioPolicy = 73;
        static final int TRANSACTION_unregisterAudioPolicyAsync = 72;
        static final int TRANSACTION_unregisterAudioServerStateDispatcher = 93;
        static final int TRANSACTION_unregisterPlaybackCallback = 83;
        static final int TRANSACTION_unregisterRecordingCallback = 80;

        private static class Proxy implements IAudioService {
            public static IAudioService sDefaultImpl;
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

            public int trackPlayer(PlayerIdCard pic) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (pic != null) {
                        _data.writeInt(1);
                        pic.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().trackPlayer(pic);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void playerAttributes(int piid, AudioAttributes attr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    if (attr != null) {
                        _data.writeInt(1);
                        attr.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().playerAttributes(piid, attr);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void playerEvent(int piid, int event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    _data.writeInt(event);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().playerEvent(piid, event);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void releasePlayer(int piid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().releasePlayer(piid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public int trackRecorder(IBinder recorder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(recorder);
                    int i = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().trackRecorder(recorder);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void recorderEvent(int riid, int event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(riid);
                    _data.writeInt(event);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().recorderEvent(riid, event);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void releaseRecorder(int riid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(riid);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().releaseRecorder(riid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void adjustSuggestedStreamVolume(int direction, int suggestedStreamType, int flags, String callingPackage, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(direction);
                    _data.writeInt(suggestedStreamType);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    _data.writeString(caller);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().adjustSuggestedStreamVolume(direction, suggestedStreamType, flags, callingPackage, caller);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void adjustStreamVolume(int streamType, int direction, int flags, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    _data.writeInt(direction);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().adjustStreamVolume(streamType, direction, flags, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setStreamVolume(int streamType, int index, int flags, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    _data.writeInt(index);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setStreamVolume(streamType, index, flags, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isStreamMute(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isStreamMute(streamType);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forceRemoteSubmixFullVolume(boolean startForcing, IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(startForcing ? 1 : 0);
                    _data.writeStrongBinder(cb);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().forceRemoteSubmixFullVolume(startForcing, cb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isMasterMute() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isMasterMute();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMasterMute(boolean mute, int flags, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mute ? 1 : 0);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMasterMute(mute, flags, callingPackage, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getStreamVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    int i = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getStreamVolume(streamType);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getStreamMinVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    int i = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getStreamMinVolume(streamType);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getStreamMaxVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    int i = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getStreamMaxVolume(streamType);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<AudioVolumeGroup> getAudioVolumeGroups() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<AudioVolumeGroup> list = 18;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAudioVolumeGroups();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(AudioVolumeGroup.CREATOR);
                    List<AudioVolumeGroup> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVolumeIndexForAttributes(AudioAttributes aa, int index, int flags, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(index);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVolumeIndexForAttributes(aa, index, flags, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(20, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getVolumeIndexForAttributes(aa);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMaxVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(21, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getMaxVolumeIndexForAttributes(aa);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMinVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(22, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getMinVolumeIndexForAttributes(aa);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLastAudibleStreamVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    int i = 23;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getLastAudibleStreamVolume(streamType);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<AudioProductStrategy> getAudioProductStrategies() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<AudioProductStrategy> list = 24;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAudioProductStrategies();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(AudioProductStrategy.CREATOR);
                    List<AudioProductStrategy> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMicrophoneMute(boolean on, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMicrophoneMute(on, callingPackage, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRingerModeExternal(int ringerMode, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ringerMode);
                    _data.writeString(caller);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRingerModeExternal(ringerMode, caller);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRingerModeInternal(int ringerMode, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ringerMode);
                    _data.writeString(caller);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRingerModeInternal(ringerMode, caller);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRingerModeExternal() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getRingerModeExternal();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRingerModeInternal() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 29;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getRingerModeInternal();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isValidRingerMode(int ringerMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ringerMode);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isValidRingerMode(ringerMode);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVibrateSetting(int vibrateType, int vibrateSetting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vibrateType);
                    _data.writeInt(vibrateSetting);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVibrateSetting(vibrateType, vibrateSetting);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getVibrateSetting(int vibrateType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vibrateType);
                    int i = 32;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getVibrateSetting(vibrateType);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean shouldVibrate(int vibrateType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vibrateType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(33, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().shouldVibrate(vibrateType);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMode(int mode, IBinder cb, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeStrongBinder(cb);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMode(mode, cb, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 35;
                    if (!this.mRemote.transact(35, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getMode();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void playSoundEffect(int effectType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(effectType);
                    if (this.mRemote.transact(36, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().playSoundEffect(effectType);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void playSoundEffectVolume(int effectType, float volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(effectType);
                    _data.writeFloat(volume);
                    if (this.mRemote.transact(37, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().playSoundEffectVolume(effectType, volume);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public boolean loadSoundEffects() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(38, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().loadSoundEffects();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unloadSoundEffects() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(39, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unloadSoundEffects();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reloadAudioSettings() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(40, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reloadAudioSettings();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void avrcpSupportsAbsoluteVolume(String address, boolean support) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(support ? 1 : 0);
                    if (this.mRemote.transact(41, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().avrcpSupportsAbsoluteVolume(address, support);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setSpeakerphoneOn(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    if (this.mRemote.transact(42, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSpeakerphoneOn(on);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSpeakerphoneOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(43, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isSpeakerphoneOn();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBluetoothScoOn(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    if (this.mRemote.transact(44, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBluetoothScoOn(on);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isBluetoothScoOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(45, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isBluetoothScoOn();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBluetoothA2dpOn(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBluetoothA2dpOn(on);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isBluetoothA2dpOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(47, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isBluetoothA2dpOn();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int requestAudioFocus(AudioAttributes aa, int durationHint, IBinder cb, IAudioFocusDispatcher fd, String clientId, String callingPackageName, int flags, IAudioPolicyCallback pcb, int sdk) throws RemoteException {
                Throwable th;
                IBinder iBinder;
                AudioAttributes audioAttributes = aa;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (audioAttributes != null) {
                        _data.writeInt(1);
                        audioAttributes.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(durationHint);
                    } catch (Throwable th2) {
                        th = th2;
                        iBinder = cb;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeStrongBinder(cb);
                        IBinder iBinder2 = null;
                        _data.writeStrongBinder(fd != null ? fd.asBinder() : null);
                        _data.writeString(clientId);
                        _data.writeString(callingPackageName);
                        _data.writeInt(flags);
                        if (pcb != null) {
                            iBinder2 = pcb.asBinder();
                        }
                        _data.writeStrongBinder(iBinder2);
                        _data.writeInt(sdk);
                        if (this.mRemote.transact(48, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            int _result = _reply.readInt();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        int requestAudioFocus = Stub.getDefaultImpl().requestAudioFocus(aa, durationHint, cb, fd, clientId, callingPackageName, flags, pcb, sdk);
                        _reply.recycle();
                        _data.recycle();
                        return requestAudioFocus;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    int i = durationHint;
                    iBinder = cb;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int abandonAudioFocus(IAudioFocusDispatcher fd, String clientId, AudioAttributes aa, String callingPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(fd != null ? fd.asBinder() : null);
                    _data.writeString(clientId);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackageName);
                    int i = this.mRemote;
                    if (!i.transact(49, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().abandonAudioFocus(fd, clientId, aa, callingPackageName);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterAudioFocusClient(String clientId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(clientId);
                    if (this.mRemote.transact(50, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterAudioFocusClient(clientId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCurrentAudioFocus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 51;
                    if (!this.mRemote.transact(51, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCurrentAudioFocus();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startBluetoothSco(IBinder cb, int targetSdkVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb);
                    _data.writeInt(targetSdkVersion);
                    if (this.mRemote.transact(52, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startBluetoothSco(cb, targetSdkVersion);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startBluetoothScoVirtualCall(IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb);
                    if (this.mRemote.transact(53, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startBluetoothScoVirtualCall(cb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopBluetoothSco(IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb);
                    if (this.mRemote.transact(54, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopBluetoothSco(cb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forceVolumeControlStream(int streamType, IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    _data.writeStrongBinder(cb);
                    if (this.mRemote.transact(55, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().forceVolumeControlStream(streamType, cb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRingtonePlayer(IRingtonePlayer player) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(player != null ? player.asBinder() : null);
                    if (this.mRemote.transact(56, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRingtonePlayer(player);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IRingtonePlayer getRingtonePlayer() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IRingtonePlayer iRingtonePlayer = 57;
                    if (!this.mRemote.transact(57, _data, _reply, 0)) {
                        iRingtonePlayer = Stub.getDefaultImpl();
                        if (iRingtonePlayer != 0) {
                            iRingtonePlayer = Stub.getDefaultImpl().getRingtonePlayer();
                            return iRingtonePlayer;
                        }
                    }
                    _reply.readException();
                    iRingtonePlayer = android.media.IRingtonePlayer.Stub.asInterface(_reply.readStrongBinder());
                    IRingtonePlayer _result = iRingtonePlayer;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getUiSoundsStreamType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 58;
                    if (!this.mRemote.transact(58, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getUiSoundsStreamType();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWiredDeviceConnectionState(int type, int state, String address, String name, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(state);
                    _data.writeString(address);
                    _data.writeString(name);
                    _data.writeString(caller);
                    if (this.mRemote.transact(59, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setWiredDeviceConnectionState(type, state, address, name, caller);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void handleBluetoothA2dpDeviceConfigChange(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(60, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().handleBluetoothA2dpDeviceConfigChange(device);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void handleBluetoothA2dpActiveDeviceChange(BluetoothDevice device, int state, int profile, boolean suppressNoisyIntent, int a2dpVolume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(state);
                    _data.writeInt(profile);
                    if (!suppressNoisyIntent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(a2dpVolume);
                    if (this.mRemote.transact(61, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().handleBluetoothA2dpActiveDeviceChange(device, state, profile, suppressNoisyIntent, a2dpVolume);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public AudioRoutesInfo startWatchingRoutes(IAudioRoutesObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    AudioRoutesInfo audioRoutesInfo = 62;
                    if (!this.mRemote.transact(62, _data, _reply, 0)) {
                        audioRoutesInfo = Stub.getDefaultImpl();
                        if (audioRoutesInfo != 0) {
                            audioRoutesInfo = Stub.getDefaultImpl().startWatchingRoutes(observer);
                            return audioRoutesInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        audioRoutesInfo = (AudioRoutesInfo) AudioRoutesInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        audioRoutesInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return audioRoutesInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isCameraSoundForced() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(63, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isCameraSoundForced();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVolumeController(IVolumeController controller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(controller != null ? controller.asBinder() : null);
                    if (this.mRemote.transact(64, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVolumeController(controller);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyVolumeControllerVisible(IVolumeController controller, boolean visible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(controller != null ? controller.asBinder() : null);
                    _data.writeInt(visible ? 1 : 0);
                    if (this.mRemote.transact(65, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyVolumeControllerVisible(controller, visible);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isStreamAffectedByRingerMode(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(66, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isStreamAffectedByRingerMode(streamType);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isStreamAffectedByMute(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(67, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isStreamAffectedByMute(streamType);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableSafeMediaVolume(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(68, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disableSafeMediaVolume(callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setHdmiSystemAudioSupported(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    int i = this.mRemote;
                    if (!i.transact(69, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().setHdmiSystemAudioSupported(on);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isHdmiSystemAudioSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(70, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isHdmiSystemAudioSupported();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String registerAudioPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb, boolean hasFocusListener, boolean isFocusPolicy, boolean isTestFocusPolicy, boolean isVolumeController, IMediaProjection projection) throws RemoteException {
                Throwable th;
                AudioPolicyConfig audioPolicyConfig = policyConfig;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (audioPolicyConfig != null) {
                        _data.writeInt(1);
                        policyConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    IBinder iBinder = null;
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    _data.writeInt(hasFocusListener ? 1 : 0);
                    _data.writeInt(isFocusPolicy ? 1 : 0);
                    _data.writeInt(isTestFocusPolicy ? 1 : 0);
                    if (!isVolumeController) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (projection != null) {
                        iBinder = projection.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    try {
                        if (this.mRemote.transact(71, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            String _result = _reply.readString();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        String registerAudioPolicy = Stub.getDefaultImpl().registerAudioPolicy(policyConfig, pcb, hasFocusListener, isFocusPolicy, isTestFocusPolicy, isVolumeController, projection);
                        _reply.recycle();
                        _data.recycle();
                        return registerAudioPolicy;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void unregisterAudioPolicyAsync(IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    if (this.mRemote.transact(72, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unregisterAudioPolicyAsync(pcb);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void unregisterAudioPolicy(IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    if (this.mRemote.transact(73, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterAudioPolicy(pcb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int addMixForPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyConfig != null) {
                        _data.writeInt(1);
                        policyConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    int i = this.mRemote;
                    if (!i.transact(74, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().addMixForPolicy(policyConfig, pcb);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int removeMixForPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyConfig != null) {
                        _data.writeInt(1);
                        policyConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    int i = this.mRemote;
                    if (!i.transact(75, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().removeMixForPolicy(policyConfig, pcb);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setFocusPropertiesForPolicy(int duckingBehavior, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(duckingBehavior);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    int i = 76;
                    if (!this.mRemote.transact(76, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().setFocusPropertiesForPolicy(duckingBehavior, pcb);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVolumePolicy(VolumePolicy policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policy != null) {
                        _data.writeInt(1);
                        policy.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(77, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVolumePolicy(policy);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasRegisteredDynamicPolicy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(78, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasRegisteredDynamicPolicy();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerRecordingCallback(IRecordingConfigDispatcher rcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(rcdb != null ? rcdb.asBinder() : null);
                    if (this.mRemote.transact(79, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerRecordingCallback(rcdb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterRecordingCallback(IRecordingConfigDispatcher rcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(rcdb != null ? rcdb.asBinder() : null);
                    if (this.mRemote.transact(80, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unregisterRecordingCallback(rcdb);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public List<AudioRecordingConfiguration> getActiveRecordingConfigurations() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<AudioRecordingConfiguration> list = 81;
                    if (!this.mRemote.transact(81, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getActiveRecordingConfigurations();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(AudioRecordingConfiguration.CREATOR);
                    List<AudioRecordingConfiguration> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerPlaybackCallback(IPlaybackConfigDispatcher pcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcdb != null ? pcdb.asBinder() : null);
                    if (this.mRemote.transact(82, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerPlaybackCallback(pcdb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterPlaybackCallback(IPlaybackConfigDispatcher pcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcdb != null ? pcdb.asBinder() : null);
                    if (this.mRemote.transact(83, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unregisterPlaybackCallback(pcdb);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public List<AudioPlaybackConfiguration> getActivePlaybackConfigurations() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<AudioPlaybackConfiguration> list = 84;
                    if (!this.mRemote.transact(84, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getActivePlaybackConfigurations();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(AudioPlaybackConfiguration.CREATOR);
                    List<AudioPlaybackConfiguration> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableRingtoneSync(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(85, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disableRingtoneSync(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getFocusRampTimeMs(int focusGain, AudioAttributes attr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(focusGain);
                    if (attr != null) {
                        _data.writeInt(1);
                        attr.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(86, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getFocusRampTimeMs(focusGain, attr);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int dispatchFocusChange(AudioFocusInfo afi, int focusChange, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (afi != null) {
                        _data.writeInt(1);
                        afi.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(focusChange);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    int i = this.mRemote;
                    if (!i.transact(87, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().dispatchFocusChange(afi, focusChange, pcb);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void playerHasOpPlayAudio(int piid, boolean hasOpPlayAudio) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    _data.writeInt(hasOpPlayAudio ? 1 : 0);
                    if (this.mRemote.transact(88, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().playerHasOpPlayAudio(piid, hasOpPlayAudio);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setBluetoothHearingAidDeviceConnectionState(BluetoothDevice device, int state, boolean suppressNoisyIntent, int musicDevice) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(state);
                    if (!suppressNoisyIntent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(musicDevice);
                    if (this.mRemote.transact(89, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBluetoothHearingAidDeviceConnectionState(device, state, suppressNoisyIntent, musicDevice);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(BluetoothDevice device, int state, int profile, boolean suppressNoisyIntent, int a2dpVolume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(state);
                    _data.writeInt(profile);
                    if (!suppressNoisyIntent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(a2dpVolume);
                    if (this.mRemote.transact(90, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(device, state, profile, suppressNoisyIntent, a2dpVolume);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFocusRequestResultFromExtPolicy(AudioFocusInfo afi, int requestResult, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (afi != null) {
                        _data.writeInt(1);
                        afi.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(requestResult);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    if (this.mRemote.transact(91, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setFocusRequestResultFromExtPolicy(afi, requestResult, pcb);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void registerAudioServerStateDispatcher(IAudioServerStateDispatcher asd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(asd != null ? asd.asBinder() : null);
                    if (this.mRemote.transact(92, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerAudioServerStateDispatcher(asd);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterAudioServerStateDispatcher(IAudioServerStateDispatcher asd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(asd != null ? asd.asBinder() : null);
                    if (this.mRemote.transact(93, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unregisterAudioServerStateDispatcher(asd);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public boolean isAudioServerRunning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(94, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAudioServerRunning();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setUidDeviceAffinity(IAudioPolicyCallback pcb, int uid, int[] deviceTypes, String[] deviceAddresses) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    _data.writeInt(uid);
                    _data.writeIntArray(deviceTypes);
                    _data.writeStringArray(deviceAddresses);
                    int i = 95;
                    if (!this.mRemote.transact(95, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().setUidDeviceAffinity(pcb, uid, deviceTypes, deviceAddresses);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int removeUidDeviceAffinity(IAudioPolicyCallback pcb, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    _data.writeInt(uid);
                    int i = 96;
                    if (!this.mRemote.transact(96, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().removeUidDeviceAffinity(pcb, uid);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasHapticChannels(Uri uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(97, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().hasHapticChannels(uri);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IBinder createAudioRecordForLoopback(ParcelFileDescriptor sharedMem, long size) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sharedMem != null) {
                        _data.writeInt(1);
                        sharedMem.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(size);
                    IBinder iBinder = this.mRemote;
                    if (!iBinder.transact(98, _data, _reply, 0)) {
                        iBinder = Stub.getDefaultImpl();
                        if (iBinder != null) {
                            iBinder = Stub.getDefaultImpl().createAudioRecordForLoopback(sharedMem, size);
                            return iBinder;
                        }
                    }
                    _reply.readException();
                    iBinder = _reply.readStrongBinder();
                    IBinder _result = iBinder;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getNotificationUri(String type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(type);
                    String str = 99;
                    if (!this.mRemote.transact(99, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getNotificationUri(type);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
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

        public static IAudioService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAudioService)) {
                return new Proxy(obj);
            }
            return (IAudioService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "trackPlayer";
                case 2:
                    return "playerAttributes";
                case 3:
                    return "playerEvent";
                case 4:
                    return "releasePlayer";
                case 5:
                    return "trackRecorder";
                case 6:
                    return "recorderEvent";
                case 7:
                    return "releaseRecorder";
                case 8:
                    return "adjustSuggestedStreamVolume";
                case 9:
                    return "adjustStreamVolume";
                case 10:
                    return "setStreamVolume";
                case 11:
                    return "isStreamMute";
                case 12:
                    return "forceRemoteSubmixFullVolume";
                case 13:
                    return "isMasterMute";
                case 14:
                    return "setMasterMute";
                case 15:
                    return "getStreamVolume";
                case 16:
                    return "getStreamMinVolume";
                case 17:
                    return "getStreamMaxVolume";
                case 18:
                    return "getAudioVolumeGroups";
                case 19:
                    return "setVolumeIndexForAttributes";
                case 20:
                    return "getVolumeIndexForAttributes";
                case 21:
                    return "getMaxVolumeIndexForAttributes";
                case 22:
                    return "getMinVolumeIndexForAttributes";
                case 23:
                    return "getLastAudibleStreamVolume";
                case 24:
                    return "getAudioProductStrategies";
                case 25:
                    return "setMicrophoneMute";
                case 26:
                    return "setRingerModeExternal";
                case 27:
                    return "setRingerModeInternal";
                case 28:
                    return "getRingerModeExternal";
                case 29:
                    return "getRingerModeInternal";
                case 30:
                    return "isValidRingerMode";
                case 31:
                    return "setVibrateSetting";
                case 32:
                    return "getVibrateSetting";
                case 33:
                    return "shouldVibrate";
                case 34:
                    return "setMode";
                case 35:
                    return "getMode";
                case 36:
                    return "playSoundEffect";
                case 37:
                    return "playSoundEffectVolume";
                case 38:
                    return "loadSoundEffects";
                case 39:
                    return "unloadSoundEffects";
                case 40:
                    return "reloadAudioSettings";
                case 41:
                    return "avrcpSupportsAbsoluteVolume";
                case 42:
                    return "setSpeakerphoneOn";
                case 43:
                    return "isSpeakerphoneOn";
                case 44:
                    return "setBluetoothScoOn";
                case 45:
                    return "isBluetoothScoOn";
                case 46:
                    return "setBluetoothA2dpOn";
                case 47:
                    return "isBluetoothA2dpOn";
                case 48:
                    return "requestAudioFocus";
                case 49:
                    return "abandonAudioFocus";
                case 50:
                    return "unregisterAudioFocusClient";
                case 51:
                    return "getCurrentAudioFocus";
                case 52:
                    return "startBluetoothSco";
                case 53:
                    return "startBluetoothScoVirtualCall";
                case 54:
                    return "stopBluetoothSco";
                case 55:
                    return "forceVolumeControlStream";
                case 56:
                    return "setRingtonePlayer";
                case 57:
                    return "getRingtonePlayer";
                case 58:
                    return "getUiSoundsStreamType";
                case 59:
                    return "setWiredDeviceConnectionState";
                case 60:
                    return "handleBluetoothA2dpDeviceConfigChange";
                case 61:
                    return "handleBluetoothA2dpActiveDeviceChange";
                case 62:
                    return "startWatchingRoutes";
                case 63:
                    return "isCameraSoundForced";
                case 64:
                    return "setVolumeController";
                case 65:
                    return "notifyVolumeControllerVisible";
                case 66:
                    return "isStreamAffectedByRingerMode";
                case 67:
                    return "isStreamAffectedByMute";
                case 68:
                    return "disableSafeMediaVolume";
                case 69:
                    return "setHdmiSystemAudioSupported";
                case 70:
                    return "isHdmiSystemAudioSupported";
                case 71:
                    return "registerAudioPolicy";
                case 72:
                    return "unregisterAudioPolicyAsync";
                case 73:
                    return "unregisterAudioPolicy";
                case 74:
                    return "addMixForPolicy";
                case 75:
                    return "removeMixForPolicy";
                case 76:
                    return "setFocusPropertiesForPolicy";
                case 77:
                    return "setVolumePolicy";
                case 78:
                    return "hasRegisteredDynamicPolicy";
                case 79:
                    return "registerRecordingCallback";
                case 80:
                    return "unregisterRecordingCallback";
                case 81:
                    return "getActiveRecordingConfigurations";
                case 82:
                    return "registerPlaybackCallback";
                case 83:
                    return "unregisterPlaybackCallback";
                case 84:
                    return "getActivePlaybackConfigurations";
                case 85:
                    return "disableRingtoneSync";
                case 86:
                    return "getFocusRampTimeMs";
                case 87:
                    return "dispatchFocusChange";
                case 88:
                    return "playerHasOpPlayAudio";
                case 89:
                    return "setBluetoothHearingAidDeviceConnectionState";
                case 90:
                    return "setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent";
                case 91:
                    return "setFocusRequestResultFromExtPolicy";
                case 92:
                    return "registerAudioServerStateDispatcher";
                case 93:
                    return "unregisterAudioServerStateDispatcher";
                case 94:
                    return "isAudioServerRunning";
                case 95:
                    return "setUidDeviceAffinity";
                case 96:
                    return "removeUidDeviceAffinity";
                case 97:
                    return "hasHapticChannels";
                case 98:
                    return "createAudioRecordForLoopback";
                case 99:
                    return "getNotificationUri";
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
                boolean _arg0 = false;
                int _result;
                int _arg02;
                AudioAttributes _arg1;
                boolean _result2;
                AudioAttributes _arg03;
                String _arg04;
                int _result3;
                BluetoothDevice _arg05;
                AudioPolicyConfig _arg06;
                int _result4;
                AudioFocusInfo _arg07;
                switch (i) {
                    case 1:
                        PlayerIdCard _arg08;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg08 = (PlayerIdCard) PlayerIdCard.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg08 = null;
                        }
                        _result = trackPlayer(_arg08);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (AudioAttributes) AudioAttributes.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        playerAttributes(_arg02, _arg1);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        playerEvent(data.readInt(), data.readInt());
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        releasePlayer(data.readInt());
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result = trackRecorder(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        recorderEvent(data.readInt(), data.readInt());
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        releaseRecorder(data.readInt());
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        adjustSuggestedStreamVolume(data.readInt(), data.readInt(), data.readInt(), data.readString(), data.readString());
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        adjustStreamVolume(data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        setStreamVolume(data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _result2 = isStreamMute(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        forceRemoteSubmixFullVolume(_arg0, data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isMasterMute();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setMasterMute(_arg0, data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _result = getStreamVolume(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        _result = getStreamMinVolume(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _result = getStreamMaxVolume(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        List<AudioVolumeGroup> _result5 = getAudioVolumeGroups();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result5);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (AudioAttributes) AudioAttributes.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        setVolumeIndexForAttributes(_arg03, data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (AudioAttributes) AudioAttributes.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _result = getVolumeIndexForAttributes(_arg03);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (AudioAttributes) AudioAttributes.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _result = getMaxVolumeIndexForAttributes(_arg03);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (AudioAttributes) AudioAttributes.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _result = getMinVolumeIndexForAttributes(_arg03);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _result = getLastAudibleStreamVolume(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        List<AudioProductStrategy> _result6 = getAudioProductStrategies();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result6);
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setMicrophoneMute(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        setRingerModeExternal(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        setRingerModeInternal(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        _arg02 = getRingerModeExternal();
                        reply.writeNoException();
                        parcel2.writeInt(_arg02);
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        _arg02 = getRingerModeInternal();
                        reply.writeNoException();
                        parcel2.writeInt(_arg02);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        _result2 = isValidRingerMode(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        setVibrateSetting(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        _result = getVibrateSetting(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        _result2 = shouldVibrate(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        setMode(data.readInt(), data.readStrongBinder(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        _arg02 = getMode();
                        reply.writeNoException();
                        parcel2.writeInt(_arg02);
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        playSoundEffect(data.readInt());
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        playSoundEffectVolume(data.readInt(), data.readFloat());
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        _arg0 = loadSoundEffects();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        unloadSoundEffects();
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        reloadAudioSettings();
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        _arg04 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        avrcpSupportsAbsoluteVolume(_arg04, _arg0);
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setSpeakerphoneOn(_arg0);
                        reply.writeNoException();
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isSpeakerphoneOn();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setBluetoothScoOn(_arg0);
                        reply.writeNoException();
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isBluetoothScoOn();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setBluetoothA2dpOn(_arg0);
                        reply.writeNoException();
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isBluetoothA2dpOn();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 48:
                        AudioAttributes _arg09;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg09 = (AudioAttributes) AudioAttributes.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg09 = null;
                        }
                        _arg02 = requestAudioFocus(_arg09, data.readInt(), data.readStrongBinder(), android.media.IAudioFocusDispatcher.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readString(), data.readInt(), android.media.audiopolicy.IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg02);
                        return true;
                    case 49:
                        AudioAttributes _arg2;
                        parcel.enforceInterface(descriptor);
                        IAudioFocusDispatcher _arg010 = android.media.IAudioFocusDispatcher.Stub.asInterface(data.readStrongBinder());
                        _arg04 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (AudioAttributes) AudioAttributes.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _result3 = abandonAudioFocus(_arg010, _arg04, _arg2, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        unregisterAudioFocusClient(data.readString());
                        reply.writeNoException();
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        _arg02 = getCurrentAudioFocus();
                        reply.writeNoException();
                        parcel2.writeInt(_arg02);
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        startBluetoothSco(data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        startBluetoothScoVirtualCall(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        stopBluetoothSco(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        forceVolumeControlStream(data.readInt(), data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 56:
                        parcel.enforceInterface(descriptor);
                        setRingtonePlayer(android.media.IRingtonePlayer.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        IRingtonePlayer _result7 = getRingtonePlayer();
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result7 != null ? _result7.asBinder() : null);
                        return true;
                    case 58:
                        parcel.enforceInterface(descriptor);
                        _arg02 = getUiSoundsStreamType();
                        reply.writeNoException();
                        parcel2.writeInt(_arg02);
                        return true;
                    case 59:
                        parcel.enforceInterface(descriptor);
                        setWiredDeviceConnectionState(data.readInt(), data.readInt(), data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 60:
                        BluetoothDevice _arg011;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg011 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg011 = null;
                        }
                        handleBluetoothA2dpDeviceConfigChange(_arg011);
                        reply.writeNoException();
                        return true;
                    case 61:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        handleBluetoothA2dpActiveDeviceChange(_arg05, data.readInt(), data.readInt(), data.readInt() != 0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 62:
                        parcel.enforceInterface(descriptor);
                        AudioRoutesInfo _result8 = startWatchingRoutes(android.media.IAudioRoutesObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        if (_result8 != null) {
                            parcel2.writeInt(1);
                            _result8.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 63:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isCameraSoundForced();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 64:
                        parcel.enforceInterface(descriptor);
                        setVolumeController(android.media.IVolumeController.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 65:
                        parcel.enforceInterface(descriptor);
                        IVolumeController _arg012 = android.media.IVolumeController.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        notifyVolumeControllerVisible(_arg012, _arg0);
                        reply.writeNoException();
                        return true;
                    case 66:
                        parcel.enforceInterface(descriptor);
                        _result2 = isStreamAffectedByRingerMode(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 67:
                        parcel.enforceInterface(descriptor);
                        _result2 = isStreamAffectedByMute(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 68:
                        parcel.enforceInterface(descriptor);
                        disableSafeMediaVolume(data.readString());
                        reply.writeNoException();
                        return true;
                    case 69:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        _result = setHdmiSystemAudioSupported(_arg0);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 70:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isHdmiSystemAudioSupported();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 71:
                        AudioPolicyConfig _arg013;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg013 = (AudioPolicyConfig) AudioPolicyConfig.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg013 = null;
                        }
                        String _result9 = registerAudioPolicy(_arg013, android.media.audiopolicy.IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()), data.readInt() != 0, data.readInt() != 0, data.readInt() != 0, data.readInt() != 0, android.media.projection.IMediaProjection.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeString(_result9);
                        return true;
                    case 72:
                        parcel.enforceInterface(descriptor);
                        unregisterAudioPolicyAsync(android.media.audiopolicy.IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 73:
                        parcel.enforceInterface(descriptor);
                        unregisterAudioPolicy(android.media.audiopolicy.IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 74:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (AudioPolicyConfig) AudioPolicyConfig.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        _result4 = addMixForPolicy(_arg06, android.media.audiopolicy.IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 75:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (AudioPolicyConfig) AudioPolicyConfig.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        _result4 = removeMixForPolicy(_arg06, android.media.audiopolicy.IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 76:
                        parcel.enforceInterface(descriptor);
                        _result4 = setFocusPropertiesForPolicy(data.readInt(), android.media.audiopolicy.IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 77:
                        VolumePolicy _arg014;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg014 = (VolumePolicy) VolumePolicy.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg014 = null;
                        }
                        setVolumePolicy(_arg014);
                        reply.writeNoException();
                        return true;
                    case 78:
                        parcel.enforceInterface(descriptor);
                        _arg0 = hasRegisteredDynamicPolicy();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 79:
                        parcel.enforceInterface(descriptor);
                        registerRecordingCallback(android.media.IRecordingConfigDispatcher.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 80:
                        parcel.enforceInterface(descriptor);
                        unregisterRecordingCallback(android.media.IRecordingConfigDispatcher.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 81:
                        parcel.enforceInterface(descriptor);
                        List<AudioRecordingConfiguration> _result10 = getActiveRecordingConfigurations();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result10);
                        return true;
                    case 82:
                        parcel.enforceInterface(descriptor);
                        registerPlaybackCallback(android.media.IPlaybackConfigDispatcher.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 83:
                        parcel.enforceInterface(descriptor);
                        unregisterPlaybackCallback(android.media.IPlaybackConfigDispatcher.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 84:
                        parcel.enforceInterface(descriptor);
                        List<AudioPlaybackConfiguration> _result11 = getActivePlaybackConfigurations();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result11);
                        return true;
                    case 85:
                        parcel.enforceInterface(descriptor);
                        disableRingtoneSync(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 86:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (AudioAttributes) AudioAttributes.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        _result4 = getFocusRampTimeMs(_arg02, _arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 87:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (AudioFocusInfo) AudioFocusInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg07 = null;
                        }
                        int _result12 = dispatchFocusChange(_arg07, data.readInt(), android.media.audiopolicy.IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result12);
                        return true;
                    case 88:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        playerHasOpPlayAudio(_result, _arg0);
                        return true;
                    case 89:
                        BluetoothDevice _arg015;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg015 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg015 = null;
                        }
                        _result4 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setBluetoothHearingAidDeviceConnectionState(_arg015, _result4, _arg0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 90:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(_arg05, data.readInt(), data.readInt(), data.readInt() != 0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 91:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (AudioFocusInfo) AudioFocusInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg07 = null;
                        }
                        setFocusRequestResultFromExtPolicy(_arg07, data.readInt(), android.media.audiopolicy.IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 92:
                        parcel.enforceInterface(descriptor);
                        registerAudioServerStateDispatcher(android.media.IAudioServerStateDispatcher.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 93:
                        parcel.enforceInterface(descriptor);
                        unregisterAudioServerStateDispatcher(android.media.IAudioServerStateDispatcher.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 94:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isAudioServerRunning();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 95:
                        parcel.enforceInterface(descriptor);
                        _result3 = setUidDeviceAffinity(android.media.audiopolicy.IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.createIntArray(), data.createStringArray());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 96:
                        parcel.enforceInterface(descriptor);
                        _result4 = removeUidDeviceAffinity(android.media.audiopolicy.IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 97:
                        Uri _arg016;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg016 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg016 = null;
                        }
                        _result2 = hasHapticChannels(_arg016);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 98:
                        ParcelFileDescriptor _arg017;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg017 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg017 = null;
                        }
                        IBinder _result13 = createAudioRecordForLoopback(_arg017, data.readLong());
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result13);
                        return true;
                    case 99:
                        parcel.enforceInterface(descriptor);
                        _arg04 = getNotificationUri(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_arg04);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IAudioService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAudioService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int abandonAudioFocus(IAudioFocusDispatcher iAudioFocusDispatcher, String str, AudioAttributes audioAttributes, String str2) throws RemoteException;

    int addMixForPolicy(AudioPolicyConfig audioPolicyConfig, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void adjustStreamVolume(int i, int i2, int i3, String str) throws RemoteException;

    void adjustSuggestedStreamVolume(int i, int i2, int i3, String str, String str2) throws RemoteException;

    void avrcpSupportsAbsoluteVolume(String str, boolean z) throws RemoteException;

    IBinder createAudioRecordForLoopback(ParcelFileDescriptor parcelFileDescriptor, long j) throws RemoteException;

    void disableRingtoneSync(int i) throws RemoteException;

    void disableSafeMediaVolume(String str) throws RemoteException;

    int dispatchFocusChange(AudioFocusInfo audioFocusInfo, int i, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void forceRemoteSubmixFullVolume(boolean z, IBinder iBinder) throws RemoteException;

    void forceVolumeControlStream(int i, IBinder iBinder) throws RemoteException;

    List<AudioPlaybackConfiguration> getActivePlaybackConfigurations() throws RemoteException;

    List<AudioRecordingConfiguration> getActiveRecordingConfigurations() throws RemoteException;

    List<AudioProductStrategy> getAudioProductStrategies() throws RemoteException;

    List<AudioVolumeGroup> getAudioVolumeGroups() throws RemoteException;

    int getCurrentAudioFocus() throws RemoteException;

    int getFocusRampTimeMs(int i, AudioAttributes audioAttributes) throws RemoteException;

    int getLastAudibleStreamVolume(int i) throws RemoteException;

    int getMaxVolumeIndexForAttributes(AudioAttributes audioAttributes) throws RemoteException;

    int getMinVolumeIndexForAttributes(AudioAttributes audioAttributes) throws RemoteException;

    int getMode() throws RemoteException;

    String getNotificationUri(String str) throws RemoteException;

    int getRingerModeExternal() throws RemoteException;

    int getRingerModeInternal() throws RemoteException;

    IRingtonePlayer getRingtonePlayer() throws RemoteException;

    @UnsupportedAppUsage
    int getStreamMaxVolume(int i) throws RemoteException;

    int getStreamMinVolume(int i) throws RemoteException;

    @UnsupportedAppUsage
    int getStreamVolume(int i) throws RemoteException;

    int getUiSoundsStreamType() throws RemoteException;

    int getVibrateSetting(int i) throws RemoteException;

    int getVolumeIndexForAttributes(AudioAttributes audioAttributes) throws RemoteException;

    void handleBluetoothA2dpActiveDeviceChange(BluetoothDevice bluetoothDevice, int i, int i2, boolean z, int i3) throws RemoteException;

    void handleBluetoothA2dpDeviceConfigChange(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean hasHapticChannels(Uri uri) throws RemoteException;

    boolean hasRegisteredDynamicPolicy() throws RemoteException;

    boolean isAudioServerRunning() throws RemoteException;

    boolean isBluetoothA2dpOn() throws RemoteException;

    boolean isBluetoothScoOn() throws RemoteException;

    boolean isCameraSoundForced() throws RemoteException;

    boolean isHdmiSystemAudioSupported() throws RemoteException;

    boolean isMasterMute() throws RemoteException;

    boolean isSpeakerphoneOn() throws RemoteException;

    boolean isStreamAffectedByMute(int i) throws RemoteException;

    boolean isStreamAffectedByRingerMode(int i) throws RemoteException;

    boolean isStreamMute(int i) throws RemoteException;

    boolean isValidRingerMode(int i) throws RemoteException;

    boolean loadSoundEffects() throws RemoteException;

    void notifyVolumeControllerVisible(IVolumeController iVolumeController, boolean z) throws RemoteException;

    void playSoundEffect(int i) throws RemoteException;

    void playSoundEffectVolume(int i, float f) throws RemoteException;

    void playerAttributes(int i, AudioAttributes audioAttributes) throws RemoteException;

    void playerEvent(int i, int i2) throws RemoteException;

    void playerHasOpPlayAudio(int i, boolean z) throws RemoteException;

    void recorderEvent(int i, int i2) throws RemoteException;

    String registerAudioPolicy(AudioPolicyConfig audioPolicyConfig, IAudioPolicyCallback iAudioPolicyCallback, boolean z, boolean z2, boolean z3, boolean z4, IMediaProjection iMediaProjection) throws RemoteException;

    void registerAudioServerStateDispatcher(IAudioServerStateDispatcher iAudioServerStateDispatcher) throws RemoteException;

    void registerPlaybackCallback(IPlaybackConfigDispatcher iPlaybackConfigDispatcher) throws RemoteException;

    void registerRecordingCallback(IRecordingConfigDispatcher iRecordingConfigDispatcher) throws RemoteException;

    void releasePlayer(int i) throws RemoteException;

    void releaseRecorder(int i) throws RemoteException;

    void reloadAudioSettings() throws RemoteException;

    int removeMixForPolicy(AudioPolicyConfig audioPolicyConfig, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    int removeUidDeviceAffinity(IAudioPolicyCallback iAudioPolicyCallback, int i) throws RemoteException;

    int requestAudioFocus(AudioAttributes audioAttributes, int i, IBinder iBinder, IAudioFocusDispatcher iAudioFocusDispatcher, String str, String str2, int i2, IAudioPolicyCallback iAudioPolicyCallback, int i3) throws RemoteException;

    void setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(BluetoothDevice bluetoothDevice, int i, int i2, boolean z, int i3) throws RemoteException;

    void setBluetoothA2dpOn(boolean z) throws RemoteException;

    void setBluetoothHearingAidDeviceConnectionState(BluetoothDevice bluetoothDevice, int i, boolean z, int i2) throws RemoteException;

    void setBluetoothScoOn(boolean z) throws RemoteException;

    int setFocusPropertiesForPolicy(int i, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void setFocusRequestResultFromExtPolicy(AudioFocusInfo audioFocusInfo, int i, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    int setHdmiSystemAudioSupported(boolean z) throws RemoteException;

    void setMasterMute(boolean z, int i, String str, int i2) throws RemoteException;

    void setMicrophoneMute(boolean z, String str, int i) throws RemoteException;

    void setMode(int i, IBinder iBinder, String str) throws RemoteException;

    void setRingerModeExternal(int i, String str) throws RemoteException;

    void setRingerModeInternal(int i, String str) throws RemoteException;

    void setRingtonePlayer(IRingtonePlayer iRingtonePlayer) throws RemoteException;

    void setSpeakerphoneOn(boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void setStreamVolume(int i, int i2, int i3, String str) throws RemoteException;

    int setUidDeviceAffinity(IAudioPolicyCallback iAudioPolicyCallback, int i, int[] iArr, String[] strArr) throws RemoteException;

    void setVibrateSetting(int i, int i2) throws RemoteException;

    void setVolumeController(IVolumeController iVolumeController) throws RemoteException;

    void setVolumeIndexForAttributes(AudioAttributes audioAttributes, int i, int i2, String str) throws RemoteException;

    void setVolumePolicy(VolumePolicy volumePolicy) throws RemoteException;

    void setWiredDeviceConnectionState(int i, int i2, String str, String str2, String str3) throws RemoteException;

    boolean shouldVibrate(int i) throws RemoteException;

    void startBluetoothSco(IBinder iBinder, int i) throws RemoteException;

    void startBluetoothScoVirtualCall(IBinder iBinder) throws RemoteException;

    @UnsupportedAppUsage
    AudioRoutesInfo startWatchingRoutes(IAudioRoutesObserver iAudioRoutesObserver) throws RemoteException;

    void stopBluetoothSco(IBinder iBinder) throws RemoteException;

    int trackPlayer(PlayerIdCard playerIdCard) throws RemoteException;

    int trackRecorder(IBinder iBinder) throws RemoteException;

    void unloadSoundEffects() throws RemoteException;

    void unregisterAudioFocusClient(String str) throws RemoteException;

    void unregisterAudioPolicy(IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void unregisterAudioPolicyAsync(IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void unregisterAudioServerStateDispatcher(IAudioServerStateDispatcher iAudioServerStateDispatcher) throws RemoteException;

    void unregisterPlaybackCallback(IPlaybackConfigDispatcher iPlaybackConfigDispatcher) throws RemoteException;

    void unregisterRecordingCallback(IRecordingConfigDispatcher iRecordingConfigDispatcher) throws RemoteException;
}
