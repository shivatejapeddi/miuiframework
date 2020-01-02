package android.media;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes.Builder;
import android.media.AudioRouting.OnRoutingChangedListener;
import android.media.IAudioService.Stub;
import android.media.MediaDrm.KeyRequest;
import android.media.MediaDrm.ProvisionRequest;
import android.media.MediaTimeProvider.OnMediaTimeListener;
import android.media.SubtitleController.Anchor;
import android.media.SubtitleController.Listener;
import android.media.SubtitleTrack.RenderingWidget;
import android.media.VolumeShaper.Configuration;
import android.media.VolumeShaper.Operation;
import android.media.VolumeShaper.State;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.util.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import libcore.io.IoBridge;
import libcore.io.Streams;

public class MediaPlayer extends PlayerBase implements Listener, VolumeAutomation, AudioRouting {
    public static final boolean APPLY_METADATA_FILTER = true;
    @UnsupportedAppUsage
    public static final boolean BYPASS_METADATA_FILTER = false;
    private static final String CALENDAR_URI = "content://settings/system/calendar_alert";
    private static final String IMEDIA_PLAYER = "android.media.IMediaPlayer";
    private static final int INVOKE_ID_ADD_EXTERNAL_SOURCE = 2;
    private static final int INVOKE_ID_ADD_EXTERNAL_SOURCE_FD = 3;
    private static final int INVOKE_ID_DESELECT_TRACK = 5;
    private static final int INVOKE_ID_GET_SELECTED_TRACK = 7;
    private static final int INVOKE_ID_GET_TRACK_INFO = 1;
    private static final int INVOKE_ID_SELECT_TRACK = 4;
    private static final int INVOKE_ID_SET_VIDEO_SCALE_MODE = 6;
    private static final int KEY_PARAMETER_AUDIO_ATTRIBUTES = 1400;
    private static final int MEDIA_AUDIO_ROUTING_CHANGED = 10000;
    private static final int MEDIA_BUFFERING_UPDATE = 3;
    private static final int MEDIA_DRM_INFO = 210;
    private static final int MEDIA_ERROR = 100;
    public static final int MEDIA_ERROR_IO = -1004;
    public static final int MEDIA_ERROR_MALFORMED = -1007;
    public static final int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;
    public static final int MEDIA_ERROR_SERVER_DIED = 100;
    public static final int MEDIA_ERROR_SYSTEM = Integer.MIN_VALUE;
    public static final int MEDIA_ERROR_TIMED_OUT = -110;
    public static final int MEDIA_ERROR_UNKNOWN = 1;
    public static final int MEDIA_ERROR_UNSUPPORTED = -1010;
    private static final int MEDIA_INFO = 200;
    public static final int MEDIA_INFO_AUDIO_NOT_PLAYING = 804;
    public static final int MEDIA_INFO_BAD_INTERLEAVING = 800;
    public static final int MEDIA_INFO_BUFFERING_END = 702;
    public static final int MEDIA_INFO_BUFFERING_START = 701;
    @UnsupportedAppUsage
    public static final int MEDIA_INFO_EXTERNAL_METADATA_UPDATE = 803;
    public static final int MEDIA_INFO_METADATA_UPDATE = 802;
    public static final int MEDIA_INFO_NETWORK_BANDWIDTH = 703;
    public static final int MEDIA_INFO_NOT_SEEKABLE = 801;
    public static final int MEDIA_INFO_STARTED_AS_NEXT = 2;
    public static final int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;
    @UnsupportedAppUsage
    public static final int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
    public static final int MEDIA_INFO_UNKNOWN = 1;
    public static final int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;
    public static final int MEDIA_INFO_VIDEO_NOT_PLAYING = 805;
    public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;
    public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;
    private static final int MEDIA_META_DATA = 202;
    public static final String MEDIA_MIMETYPE_TEXT_CEA_608 = "text/cea-608";
    public static final String MEDIA_MIMETYPE_TEXT_CEA_708 = "text/cea-708";
    public static final String MEDIA_MIMETYPE_TEXT_SUBRIP = "application/x-subrip";
    public static final String MEDIA_MIMETYPE_TEXT_VTT = "text/vtt";
    private static final int MEDIA_NOP = 0;
    private static final int MEDIA_NOTIFY_TIME = 98;
    private static final int MEDIA_PAUSED = 7;
    private static final int MEDIA_PLAYBACK_COMPLETE = 2;
    private static final int MEDIA_PREPARED = 1;
    private static final int MEDIA_SEEK_COMPLETE = 4;
    private static final int MEDIA_SET_VIDEO_SIZE = 5;
    private static final int MEDIA_SKIPPED = 9;
    private static final int MEDIA_STARTED = 6;
    private static final int MEDIA_STOPPED = 8;
    private static final int MEDIA_SUBTITLE_DATA = 201;
    private static final int MEDIA_TIMED_TEXT = 99;
    private static final int MEDIA_TIME_DISCONTINUITY = 211;
    @UnsupportedAppUsage
    public static final boolean METADATA_ALL = false;
    public static final boolean METADATA_UPDATE_ONLY = true;
    private static final String NOTIFICATION_URI = "content://settings/system/notification_sound";
    public static final int PLAYBACK_RATE_AUDIO_MODE_DEFAULT = 0;
    public static final int PLAYBACK_RATE_AUDIO_MODE_RESAMPLE = 2;
    public static final int PLAYBACK_RATE_AUDIO_MODE_STRETCH = 1;
    public static final int PREPARE_DRM_STATUS_PREPARATION_ERROR = 3;
    public static final int PREPARE_DRM_STATUS_PROVISIONING_NETWORK_ERROR = 1;
    public static final int PREPARE_DRM_STATUS_PROVISIONING_SERVER_ERROR = 2;
    public static final int PREPARE_DRM_STATUS_SUCCESS = 0;
    public static final int SEEK_CLOSEST = 3;
    public static final int SEEK_CLOSEST_SYNC = 2;
    public static final int SEEK_NEXT_SYNC = 1;
    public static final int SEEK_PREVIOUS_SYNC = 0;
    private static final String SMS_SLOT1_URI = "content://settings/system/sms_received_sound_slot_1";
    private static final String SMS_SLOT2_URI = "content://settings/system/sms_received_sound_slot_2";
    private static final String SMS_URI = "content://settings/system/sms_received_sound";
    private static final String TAG = "MediaPlayer";
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT = 1;
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING = 2;
    private boolean mActiveDrmScheme;
    private boolean mDrmConfigAllowed;
    private DrmInfo mDrmInfo;
    private boolean mDrmInfoResolved;
    private final Object mDrmLock = new Object();
    private MediaDrm mDrmObj;
    private boolean mDrmProvisioningInProgress;
    private ProvisioningThread mDrmProvisioningThread;
    private byte[] mDrmSessionId;
    private UUID mDrmUUID;
    @UnsupportedAppUsage
    private EventHandler mEventHandler;
    private Handler mExtSubtitleDataHandler;
    private OnSubtitleDataListener mExtSubtitleDataListener;
    private BitSet mInbandTrackIndices = new BitSet();
    private Vector<Pair<Integer, SubtitleTrack>> mIndexTrackPairs = new Vector();
    private final OnSubtitleDataListener mIntSubtitleDataListener = new OnSubtitleDataListener() {
        public void onSubtitleData(MediaPlayer mp, SubtitleData data) {
            int index = data.getTrackIndex();
            synchronized (MediaPlayer.this.mIndexTrackPairs) {
                Iterator it = MediaPlayer.this.mIndexTrackPairs.iterator();
                while (it.hasNext()) {
                    Pair<Integer, SubtitleTrack> p = (Pair) it.next();
                    if (!(p.first == null || ((Integer) p.first).intValue() != index || p.second == null)) {
                        p.second.onData(data);
                    }
                }
            }
        }
    };
    private int mListenerContext;
    private long mNativeContext;
    private long mNativeSurfaceTexture;
    private boolean mNeedMuteNotification;
    private OnBufferingUpdateListener mOnBufferingUpdateListener;
    private final OnCompletionListener mOnCompletionInternalListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
            MediaPlayer.this.baseStop();
        }
    };
    @UnsupportedAppUsage
    private OnCompletionListener mOnCompletionListener;
    private OnDrmConfigHelper mOnDrmConfigHelper;
    private OnDrmInfoHandlerDelegate mOnDrmInfoHandlerDelegate;
    private OnDrmPreparedHandlerDelegate mOnDrmPreparedHandlerDelegate;
    @UnsupportedAppUsage
    private OnErrorListener mOnErrorListener;
    @UnsupportedAppUsage
    private OnInfoListener mOnInfoListener;
    private Handler mOnMediaTimeDiscontinuityHandler;
    private OnMediaTimeDiscontinuityListener mOnMediaTimeDiscontinuityListener;
    @UnsupportedAppUsage
    private OnPreparedListener mOnPreparedListener;
    @UnsupportedAppUsage
    private OnSeekCompleteListener mOnSeekCompleteListener;
    private OnTimedMetaDataAvailableListener mOnTimedMetaDataAvailableListener;
    @UnsupportedAppUsage
    private OnTimedTextListener mOnTimedTextListener;
    private OnVideoSizeChangedListener mOnVideoSizeChangedListener;
    private Vector<InputStream> mOpenSubtitleSources;
    private AudioDeviceInfo mPreferredDevice = null;
    private boolean mPrepareDrmInProgress;
    @GuardedBy({"mRoutingChangeListeners"})
    private ArrayMap<OnRoutingChangedListener, NativeRoutingEventHandlerDelegate> mRoutingChangeListeners = new ArrayMap();
    private boolean mScreenOnWhilePlaying;
    private int mSelectedSubtitleTrackIndex = -1;
    private boolean mStayAwake;
    private int mStreamType = Integer.MIN_VALUE;
    private SubtitleController mSubtitleController;
    private boolean mSubtitleDataListenerDisabled;
    private SurfaceHolder mSurfaceHolder;
    private TimeProvider mTimeProvider;
    private final Object mTimeProviderLock = new Object();
    private int mUsage = -1;
    private WakeLock mWakeLock = null;

    public interface OnSubtitleDataListener {
        void onSubtitleData(MediaPlayer mediaPlayer, SubtitleData subtitleData);
    }

    public interface OnCompletionListener {
        void onCompletion(MediaPlayer mediaPlayer);
    }

    public static final class DrmInfo {
        private Map<UUID, byte[]> mapPssh;
        private UUID[] supportedSchemes;

        public Map<UUID, byte[]> getPssh() {
            return this.mapPssh;
        }

        public UUID[] getSupportedSchemes() {
            return this.supportedSchemes;
        }

        private DrmInfo(Map<UUID, byte[]> Pssh, UUID[] SupportedSchemes) {
            this.mapPssh = Pssh;
            this.supportedSchemes = SupportedSchemes;
        }

        private DrmInfo(Parcel parcel) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DrmInfo(");
            stringBuilder.append(parcel);
            stringBuilder.append(") size ");
            stringBuilder.append(parcel.dataSize());
            String stringBuilder2 = stringBuilder.toString();
            String str = MediaPlayer.TAG;
            Log.v(str, stringBuilder2);
            int psshsize = parcel.readInt();
            byte[] pssh = new byte[psshsize];
            parcel.readByteArray(pssh);
            StringBuilder stringBuilder3 = new StringBuilder();
            String str2 = "DrmInfo() PSSH: ";
            stringBuilder3.append(str2);
            stringBuilder3.append(arrToHex(pssh));
            Log.v(str, stringBuilder3.toString());
            this.mapPssh = parsePSSH(pssh, psshsize);
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append(str2);
            stringBuilder3.append(this.mapPssh);
            Log.v(str, stringBuilder3.toString());
            int supportedDRMsCount = parcel.readInt();
            this.supportedSchemes = new UUID[supportedDRMsCount];
            for (int i = 0; i < supportedDRMsCount; i++) {
                byte[] uuid = new byte[16];
                parcel.readByteArray(uuid);
                this.supportedSchemes[i] = bytesToUUID(uuid);
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append("DrmInfo() supportedScheme[");
                stringBuilder4.append(i);
                stringBuilder4.append("]: ");
                stringBuilder4.append(this.supportedSchemes[i]);
                Log.v(str, stringBuilder4.toString());
            }
            StringBuilder stringBuilder5 = new StringBuilder();
            stringBuilder5.append("DrmInfo() Parcel psshsize: ");
            stringBuilder5.append(psshsize);
            stringBuilder5.append(" supportedDRMsCount: ");
            stringBuilder5.append(supportedDRMsCount);
            Log.v(str, stringBuilder5.toString());
        }

        private DrmInfo makeCopy() {
            return new DrmInfo(this.mapPssh, this.supportedSchemes);
        }

        private String arrToHex(byte[] bytes) {
            String out = "0x";
            for (int i = 0; i < bytes.length; i++) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(out);
                stringBuilder.append(String.format("%02x", new Object[]{Byte.valueOf(bytes[i])}));
                out = stringBuilder.toString();
            }
            return out;
        }

        private UUID bytesToUUID(byte[] uuid) {
            long msb = 0;
            long lsb = 0;
            for (int i = 0; i < 8; i++) {
                msb |= (((long) uuid[i]) & 255) << ((7 - i) * 8);
                lsb |= (((long) uuid[i + 8]) & 255) << ((7 - i) * 8);
            }
            return new UUID(msb, lsb);
        }

        private Map<UUID, byte[]> parsePSSH(byte[] pssh, int psshsize) {
            byte[] bArr = pssh;
            Map<UUID, byte[]> result = new HashMap();
            int len = psshsize;
            int numentries = 0;
            int i = 0;
            while (len > 0) {
                String str = MediaPlayer.TAG;
                if (len < 16) {
                    Log.w(str, String.format("parsePSSH: len is too short to parse UUID: (%d < 16) pssh: %d", new Object[]{Integer.valueOf(len), Integer.valueOf(psshsize)}));
                    return null;
                }
                UUID uuid = bytesToUUID(Arrays.copyOfRange(bArr, i, i + 16));
                i += 16;
                len -= 16;
                if (len < 4) {
                    Log.w(str, String.format("parsePSSH: len is too short to parse datalen: (%d < 4) pssh: %d", new Object[]{Integer.valueOf(len), Integer.valueOf(psshsize)}));
                    return null;
                }
                int datalen;
                byte[] subset = Arrays.copyOfRange(bArr, i, i + 4);
                if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                    datalen = ((((subset[3] & 255) << 24) | ((subset[2] & 255) << 16)) | ((subset[1] & 255) << 8)) | (subset[0] & 255);
                } else {
                    datalen = ((((subset[0] & 255) << 24) | ((subset[1] & 255) << 16)) | ((subset[2] & 255) << 8)) | (subset[3] & 255);
                }
                i += 4;
                len -= 4;
                if (len < datalen) {
                    Log.w(str, String.format("parsePSSH: len is too short to parse data: (%d < %d) pssh: %d", new Object[]{Integer.valueOf(len), Integer.valueOf(datalen), Integer.valueOf(psshsize)}));
                    return null;
                }
                byte[] data = Arrays.copyOfRange(bArr, i, i + datalen);
                i += datalen;
                len -= datalen;
                Log.v(str, String.format("parsePSSH[%d]: <%s, %s> pssh: %d", new Object[]{Integer.valueOf(numentries), uuid, arrToHex(data), Integer.valueOf(psshsize)}));
                numentries++;
                result.put(uuid, data);
            }
            return result;
        }
    }

    private class EventHandler extends Handler {
        private MediaPlayer mMediaPlayer;

        public EventHandler(MediaPlayer mp, Looper looper) {
            super(looper);
            this.mMediaPlayer = mp;
        }

        /* JADX WARNING: Removed duplicated region for block: B:64:0x012d  */
        /* JADX WARNING: Missing block: B:32:0x008c, code skipped:
            if ((r2.obj instanceof android.os.Parcel) == false) goto L_0x00b7;
     */
        /* JADX WARNING: Missing block: B:33:0x008e, code skipped:
            r4 = r2.obj;
            r5 = new android.media.SubtitleData(r4);
            r4.recycle();
            android.media.MediaPlayer.access$2700(r1.this$0).onSubtitleData(r1.mMediaPlayer, r5);
     */
        /* JADX WARNING: Missing block: B:34:0x00a5, code skipped:
            if (r0 == null) goto L_0x00b7;
     */
        /* JADX WARNING: Missing block: B:35:0x00a7, code skipped:
            if (r3 != null) goto L_0x00af;
     */
        /* JADX WARNING: Missing block: B:36:0x00a9, code skipped:
            r0.onSubtitleData(r1.mMediaPlayer, r5);
     */
        /* JADX WARNING: Missing block: B:37:0x00af, code skipped:
            r3.post(new android.media.MediaPlayer.EventHandler.AnonymousClass1(r1));
     */
        /* JADX WARNING: Missing block: B:38:0x00b7, code skipped:
            return;
     */
        /* JADX WARNING: Missing block: B:105:0x0207, code skipped:
            r0 = android.media.MediaPlayer.access$600(r1.this$0);
     */
        /* JADX WARNING: Missing block: B:106:0x020d, code skipped:
            if (r0 == null) goto L_0x0214;
     */
        /* JADX WARNING: Missing block: B:107:0x020f, code skipped:
            r0.onSeekComplete(r1.mMediaPlayer);
     */
        /* JADX WARNING: Missing block: B:108:0x0214, code skipped:
            return;
     */
        /* JADX WARNING: Missing block: B:126:0x0260, code skipped:
            return;
     */
        public void handleMessage(android.os.Message r18) {
            /*
            r17 = this;
            r1 = r17;
            r2 = r18;
            r0 = r1.mMediaPlayer;
            r3 = r0.mNativeContext;
            r5 = 0;
            r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
            if (r0 != 0) goto L_0x0019;
        L_0x0010:
            r0 = "MediaPlayer";
            r3 = "mediaplayer went away with unhandled events";
            android.util.Log.w(r0, r3);
            return;
        L_0x0019:
            r0 = r2.what;
            r3 = 210; // 0xd2 float:2.94E-43 double:1.04E-321;
            if (r0 == r3) goto L_0x02e9;
        L_0x001f:
            r3 = 211; // 0xd3 float:2.96E-43 double:1.042E-321;
            r4 = 0;
            if (r0 == r3) goto L_0x028e;
        L_0x0024:
            r3 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
            if (r0 == r3) goto L_0x0261;
        L_0x0028:
            r3 = -1010; // 0xfffffffffffffc0e float:NaN double:NaN;
            r5 = 100;
            r6 = 0;
            r7 = 1;
            switch(r0) {
                case 0: goto L_0x025f;
                case 1: goto L_0x0243;
                case 2: goto L_0x0225;
                case 3: goto L_0x0215;
                case 4: goto L_0x01fa;
                case 5: goto L_0x01e8;
                case 6: goto L_0x01d5;
                case 7: goto L_0x01d5;
                case 8: goto L_0x01c8;
                case 9: goto L_0x0207;
                default: goto L_0x0031;
            };
        L_0x0031:
            switch(r0) {
                case 98: goto L_0x01bc;
                case 99: goto L_0x0191;
                case 100: goto L_0x0137;
                default: goto L_0x0034;
            };
        L_0x0034:
            switch(r0) {
                case 200: goto L_0x00bb;
                case 201: goto L_0x0070;
                case 202: goto L_0x0050;
                default: goto L_0x0037;
            };
        L_0x0037:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r3 = "Unknown message type ";
            r0.append(r3);
            r3 = r2.what;
            r0.append(r3);
            r0 = r0.toString();
            r3 = "MediaPlayer";
            android.util.Log.e(r3, r0);
            return;
        L_0x0050:
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mOnTimedMetaDataAvailableListener;
            if (r0 != 0) goto L_0x0059;
        L_0x0058:
            return;
        L_0x0059:
            r3 = r2.obj;
            r3 = r3 instanceof android.os.Parcel;
            if (r3 == 0) goto L_0x006f;
        L_0x005f:
            r3 = r2.obj;
            r3 = (android.os.Parcel) r3;
            r4 = android.media.TimedMetaData.createTimedMetaDataFromParcel(r3);
            r3.recycle();
            r5 = r1.mMediaPlayer;
            r0.onTimedMetaDataAvailable(r5, r4);
        L_0x006f:
            return;
        L_0x0070:
            monitor-enter(r17);
            r0 = android.media.MediaPlayer.this;	 Catch:{ all -> 0x00b8 }
            r0 = r0.mSubtitleDataListenerDisabled;	 Catch:{ all -> 0x00b8 }
            if (r0 == 0) goto L_0x007b;
        L_0x0079:
            monitor-exit(r17);	 Catch:{ all -> 0x00b8 }
            return;
        L_0x007b:
            r0 = android.media.MediaPlayer.this;	 Catch:{ all -> 0x00b8 }
            r0 = r0.mExtSubtitleDataListener;	 Catch:{ all -> 0x00b8 }
            r3 = android.media.MediaPlayer.this;	 Catch:{ all -> 0x00b8 }
            r3 = r3.mExtSubtitleDataHandler;	 Catch:{ all -> 0x00b8 }
            monitor-exit(r17);	 Catch:{ all -> 0x00b8 }
            r4 = r2.obj;
            r4 = r4 instanceof android.os.Parcel;
            if (r4 == 0) goto L_0x00b7;
        L_0x008e:
            r4 = r2.obj;
            r4 = (android.os.Parcel) r4;
            r5 = new android.media.SubtitleData;
            r5.<init>(r4);
            r4.recycle();
            r6 = android.media.MediaPlayer.this;
            r6 = r6.mIntSubtitleDataListener;
            r7 = r1.mMediaPlayer;
            r6.onSubtitleData(r7, r5);
            if (r0 == 0) goto L_0x00b7;
        L_0x00a7:
            if (r3 != 0) goto L_0x00af;
        L_0x00a9:
            r6 = r1.mMediaPlayer;
            r0.onSubtitleData(r6, r5);
            goto L_0x00b7;
        L_0x00af:
            r6 = new android.media.MediaPlayer$EventHandler$1;
            r6.<init>(r0, r5);
            r3.post(r6);
        L_0x00b7:
            return;
        L_0x00b8:
            r0 = move-exception;
            monitor-exit(r17);	 Catch:{ all -> 0x00b8 }
            throw r0;
        L_0x00bb:
            r0 = r2.arg1;
            r8 = 802; // 0x322 float:1.124E-42 double:3.96E-321;
            if (r0 == r8) goto L_0x0104;
        L_0x00c1:
            r3 = 803; // 0x323 float:1.125E-42 double:3.967E-321;
            if (r0 == r3) goto L_0x0112;
        L_0x00c5:
            switch(r0) {
                case 700: goto L_0x00dc;
                case 701: goto L_0x00c9;
                case 702: goto L_0x00c9;
                default: goto L_0x00c8;
            };
        L_0x00c8:
            goto L_0x0125;
        L_0x00c9:
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mTimeProvider;
            if (r0 == 0) goto L_0x0125;
        L_0x00d1:
            r3 = r2.arg1;
            r5 = 701; // 0x2bd float:9.82E-43 double:3.463E-321;
            if (r3 != r5) goto L_0x00d8;
        L_0x00d7:
            r4 = r7;
        L_0x00d8:
            r0.onBuffering(r4);
            goto L_0x0125;
        L_0x00dc:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r3 = "Info (";
            r0.append(r3);
            r3 = r2.arg1;
            r0.append(r3);
            r3 = ",";
            r0.append(r3);
            r3 = r2.arg2;
            r0.append(r3);
            r3 = ")";
            r0.append(r3);
            r0 = r0.toString();
            r3 = "MediaPlayer";
            android.util.Log.i(r3, r0);
            goto L_0x0125;
        L_0x0104:
            r0 = android.media.MediaPlayer.this;	 Catch:{ RuntimeException -> 0x010a }
            r0.scanInternalSubtitleTracks();	 Catch:{ RuntimeException -> 0x010a }
            goto L_0x0112;
        L_0x010a:
            r0 = move-exception;
            r3 = r1.obtainMessage(r5, r7, r3, r6);
            r1.sendMessage(r3);
        L_0x0112:
            r2.arg1 = r8;
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mSubtitleController;
            if (r0 == 0) goto L_0x0125;
        L_0x011c:
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mSubtitleController;
            r0.selectDefaultTrack();
        L_0x0125:
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mOnInfoListener;
            if (r0 == 0) goto L_0x0136;
        L_0x012d:
            r3 = r1.mMediaPlayer;
            r4 = r2.arg1;
            r5 = r2.arg2;
            r0.onInfo(r3, r4, r5);
        L_0x0136:
            return;
        L_0x0137:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r3 = "Error (";
            r0.append(r3);
            r3 = r2.arg1;
            r0.append(r3);
            r3 = ",";
            r0.append(r3);
            r3 = r2.arg2;
            r0.append(r3);
            r3 = ")";
            r0.append(r3);
            r0 = r0.toString();
            r3 = "MediaPlayer";
            android.util.Log.e(r3, r0);
            r0 = 0;
            r3 = android.media.MediaPlayer.this;
            r3 = r3.mOnErrorListener;
            if (r3 == 0) goto L_0x0171;
        L_0x0167:
            r5 = r1.mMediaPlayer;
            r6 = r2.arg1;
            r7 = r2.arg2;
            r0 = r3.onError(r5, r6, r7);
        L_0x0171:
            r5 = android.media.MediaPlayer.this;
            r5 = r5.mOnCompletionInternalListener;
            r6 = r1.mMediaPlayer;
            r5.onCompletion(r6);
            r5 = android.media.MediaPlayer.this;
            r5 = r5.mOnCompletionListener;
            if (r5 == 0) goto L_0x018b;
        L_0x0184:
            if (r0 != 0) goto L_0x018b;
        L_0x0186:
            r6 = r1.mMediaPlayer;
            r5.onCompletion(r6);
        L_0x018b:
            r5 = android.media.MediaPlayer.this;
            r5.stayAwake(r4);
            return;
        L_0x0191:
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mOnTimedTextListener;
            if (r0 != 0) goto L_0x019a;
        L_0x0199:
            return;
        L_0x019a:
            r3 = r2.obj;
            if (r3 != 0) goto L_0x01a4;
        L_0x019e:
            r3 = r1.mMediaPlayer;
            r0.onTimedText(r3, r6);
            goto L_0x01bb;
        L_0x01a4:
            r3 = r2.obj;
            r3 = r3 instanceof android.os.Parcel;
            if (r3 == 0) goto L_0x01bb;
        L_0x01aa:
            r3 = r2.obj;
            r3 = (android.os.Parcel) r3;
            r4 = new android.media.TimedText;
            r4.<init>(r3);
            r3.recycle();
            r5 = r1.mMediaPlayer;
            r0.onTimedText(r5, r4);
        L_0x01bb:
            return;
        L_0x01bc:
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mTimeProvider;
            if (r0 == 0) goto L_0x01c7;
        L_0x01c4:
            r0.onNotifyTime();
        L_0x01c7:
            return;
        L_0x01c8:
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mTimeProvider;
            if (r0 == 0) goto L_0x01d3;
        L_0x01d0:
            r0.onStopped();
        L_0x01d3:
            goto L_0x0260;
        L_0x01d5:
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mTimeProvider;
            if (r0 == 0) goto L_0x01e6;
        L_0x01dd:
            r3 = r2.what;
            r5 = 7;
            if (r3 != r5) goto L_0x01e3;
        L_0x01e2:
            r4 = r7;
        L_0x01e3:
            r0.onPaused(r4);
        L_0x01e6:
            goto L_0x0260;
        L_0x01e8:
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mOnVideoSizeChangedListener;
            if (r0 == 0) goto L_0x01f9;
        L_0x01f0:
            r3 = r1.mMediaPlayer;
            r4 = r2.arg1;
            r5 = r2.arg2;
            r0.onVideoSizeChanged(r3, r4, r5);
        L_0x01f9:
            return;
        L_0x01fa:
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mOnSeekCompleteListener;
            if (r0 == 0) goto L_0x0207;
        L_0x0202:
            r3 = r1.mMediaPlayer;
            r0.onSeekComplete(r3);
        L_0x0207:
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mTimeProvider;
            if (r0 == 0) goto L_0x0214;
        L_0x020f:
            r3 = r1.mMediaPlayer;
            r0.onSeekComplete(r3);
        L_0x0214:
            return;
        L_0x0215:
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mOnBufferingUpdateListener;
            if (r0 == 0) goto L_0x0224;
        L_0x021d:
            r3 = r1.mMediaPlayer;
            r4 = r2.arg1;
            r0.onBufferingUpdate(r3, r4);
        L_0x0224:
            return;
        L_0x0225:
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mOnCompletionInternalListener;
            r3 = r1.mMediaPlayer;
            r0.onCompletion(r3);
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mOnCompletionListener;
            if (r0 == 0) goto L_0x023d;
        L_0x0238:
            r3 = r1.mMediaPlayer;
            r0.onCompletion(r3);
        L_0x023d:
            r0 = android.media.MediaPlayer.this;
            r0.stayAwake(r4);
            return;
        L_0x0243:
            r0 = android.media.MediaPlayer.this;	 Catch:{ RuntimeException -> 0x0249 }
            r0.scanInternalSubtitleTracks();	 Catch:{ RuntimeException -> 0x0249 }
            goto L_0x0251;
        L_0x0249:
            r0 = move-exception;
            r3 = r1.obtainMessage(r5, r7, r3, r6);
            r1.sendMessage(r3);
        L_0x0251:
            r0 = android.media.MediaPlayer.this;
            r0 = r0.mOnPreparedListener;
            if (r0 == 0) goto L_0x025e;
        L_0x0259:
            r3 = r1.mMediaPlayer;
            r0.onPrepared(r3);
        L_0x025e:
            return;
        L_0x0260:
            return;
        L_0x0261:
            android.media.AudioManager.resetAudioPortGeneration();
            r0 = android.media.MediaPlayer.this;
            r3 = r0.mRoutingChangeListeners;
            monitor-enter(r3);
            r0 = android.media.MediaPlayer.this;	 Catch:{ all -> 0x028b }
            r0 = r0.mRoutingChangeListeners;	 Catch:{ all -> 0x028b }
            r0 = r0.values();	 Catch:{ all -> 0x028b }
            r0 = r0.iterator();	 Catch:{ all -> 0x028b }
        L_0x0279:
            r4 = r0.hasNext();	 Catch:{ all -> 0x028b }
            if (r4 == 0) goto L_0x0289;
        L_0x027f:
            r4 = r0.next();	 Catch:{ all -> 0x028b }
            r4 = (android.media.NativeRoutingEventHandlerDelegate) r4;	 Catch:{ all -> 0x028b }
            r4.notifyClient();	 Catch:{ all -> 0x028b }
            goto L_0x0279;
        L_0x0289:
            monitor-exit(r3);	 Catch:{ all -> 0x028b }
            return;
        L_0x028b:
            r0 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x028b }
            throw r0;
        L_0x028e:
            monitor-enter(r17);
            r0 = android.media.MediaPlayer.this;	 Catch:{ all -> 0x02e6 }
            r0 = r0.mOnMediaTimeDiscontinuityListener;	 Catch:{ all -> 0x02e6 }
            r3 = android.media.MediaPlayer.this;	 Catch:{ all -> 0x02e6 }
            r3 = r3.mOnMediaTimeDiscontinuityHandler;	 Catch:{ all -> 0x02e6 }
            monitor-exit(r17);	 Catch:{ all -> 0x02e6 }
            if (r0 != 0) goto L_0x029f;
        L_0x029e:
            return;
        L_0x029f:
            r5 = r2.obj;
            r5 = r5 instanceof android.os.Parcel;
            if (r5 == 0) goto L_0x02e5;
        L_0x02a5:
            r5 = r2.obj;
            r5 = (android.os.Parcel) r5;
            r5.setDataPosition(r4);
            r12 = r5.readLong();
            r14 = r5.readLong();
            r4 = r5.readFloat();
            r5.recycle();
            r6 = -1;
            r8 = (r12 > r6 ? 1 : (r12 == r6 ? 0 : -1));
            if (r8 == 0) goto L_0x02d3;
        L_0x02c1:
            r6 = (r14 > r6 ? 1 : (r14 == r6 ? 0 : -1));
            if (r6 == 0) goto L_0x02d3;
        L_0x02c5:
            r16 = new android.media.MediaTimestamp;
            r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            r9 = r14 * r6;
            r6 = r16;
            r7 = r12;
            r11 = r4;
            r6.<init>(r7, r9, r11);
            goto L_0x02d5;
        L_0x02d3:
            r6 = android.media.MediaTimestamp.TIMESTAMP_UNKNOWN;
        L_0x02d5:
            if (r3 != 0) goto L_0x02dd;
        L_0x02d7:
            r7 = r1.mMediaPlayer;
            r0.onMediaTimeDiscontinuity(r7, r6);
            goto L_0x02e5;
        L_0x02dd:
            r7 = new android.media.MediaPlayer$EventHandler$2;
            r7.<init>(r0, r6);
            r3.post(r7);
        L_0x02e5:
            return;
        L_0x02e6:
            r0 = move-exception;
            monitor-exit(r17);	 Catch:{ all -> 0x02e6 }
            throw r0;
        L_0x02e9:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r3 = "MEDIA_DRM_INFO ";
            r0.append(r3);
            r3 = android.media.MediaPlayer.this;
            r3 = r3.mOnDrmInfoHandlerDelegate;
            r0.append(r3);
            r0 = r0.toString();
            r3 = "MediaPlayer";
            android.util.Log.v(r3, r0);
            r0 = r2.obj;
            if (r0 != 0) goto L_0x0311;
        L_0x0309:
            r0 = "MediaPlayer";
            r3 = "MEDIA_DRM_INFO msg.obj=NULL";
            android.util.Log.w(r0, r3);
            goto L_0x0362;
        L_0x0311:
            r0 = r2.obj;
            r0 = r0 instanceof android.os.Parcel;
            if (r0 == 0) goto L_0x034a;
        L_0x0317:
            r3 = 0;
            r0 = android.media.MediaPlayer.this;
            r4 = r0.mDrmLock;
            monitor-enter(r4);
            r0 = android.media.MediaPlayer.this;	 Catch:{ all -> 0x0347 }
            r0 = r0.mOnDrmInfoHandlerDelegate;	 Catch:{ all -> 0x0347 }
            if (r0 == 0) goto L_0x033a;
        L_0x0327:
            r0 = android.media.MediaPlayer.this;	 Catch:{ all -> 0x0347 }
            r0 = r0.mDrmInfo;	 Catch:{ all -> 0x0347 }
            if (r0 == 0) goto L_0x033a;
        L_0x032f:
            r0 = android.media.MediaPlayer.this;	 Catch:{ all -> 0x0347 }
            r0 = r0.mDrmInfo;	 Catch:{ all -> 0x0347 }
            r0 = r0.makeCopy();	 Catch:{ all -> 0x0347 }
            r3 = r0;
        L_0x033a:
            r0 = android.media.MediaPlayer.this;	 Catch:{ all -> 0x0347 }
            r0 = r0.mOnDrmInfoHandlerDelegate;	 Catch:{ all -> 0x0347 }
            monitor-exit(r4);	 Catch:{ all -> 0x0347 }
            if (r0 == 0) goto L_0x0346;
        L_0x0343:
            r0.notifyClient(r3);
        L_0x0346:
            goto L_0x0362;
        L_0x0347:
            r0 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0347 }
            throw r0;
        L_0x034a:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r3 = "MEDIA_DRM_INFO msg.obj of unexpected type ";
            r0.append(r3);
            r3 = r2.obj;
            r0.append(r3);
            r0 = r0.toString();
            r3 = "MediaPlayer";
            android.util.Log.w(r3, r0);
        L_0x0362:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.MediaPlayer$EventHandler.handleMessage(android.os.Message):void");
        }
    }

    public static final class MetricsConstants {
        public static final String CODEC_AUDIO = "android.media.mediaplayer.audio.codec";
        public static final String CODEC_VIDEO = "android.media.mediaplayer.video.codec";
        public static final String DURATION = "android.media.mediaplayer.durationMs";
        public static final String ERRORS = "android.media.mediaplayer.err";
        public static final String ERROR_CODE = "android.media.mediaplayer.errcode";
        public static final String FRAMES = "android.media.mediaplayer.frames";
        public static final String FRAMES_DROPPED = "android.media.mediaplayer.dropped";
        public static final String HEIGHT = "android.media.mediaplayer.height";
        public static final String MIME_TYPE_AUDIO = "android.media.mediaplayer.audio.mime";
        public static final String MIME_TYPE_VIDEO = "android.media.mediaplayer.video.mime";
        public static final String PLAYING = "android.media.mediaplayer.playingMs";
        public static final String WIDTH = "android.media.mediaplayer.width";

        private MetricsConstants() {
        }
    }

    public static final class NoDrmSchemeException extends MediaDrmException {
        public NoDrmSchemeException(String detailMessage) {
            super(detailMessage);
        }
    }

    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(MediaPlayer mediaPlayer, int i);
    }

    public interface OnDrmConfigHelper {
        void onDrmConfig(MediaPlayer mediaPlayer);
    }

    private class OnDrmInfoHandlerDelegate {
        private Handler mHandler;
        private MediaPlayer mMediaPlayer;
        private OnDrmInfoListener mOnDrmInfoListener;

        OnDrmInfoHandlerDelegate(MediaPlayer mp, OnDrmInfoListener listener, Handler handler) {
            this.mMediaPlayer = mp;
            this.mOnDrmInfoListener = listener;
            if (handler != null) {
                this.mHandler = handler;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void notifyClient(final DrmInfo drmInfo) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.post(new Runnable() {
                    public void run() {
                        OnDrmInfoHandlerDelegate.this.mOnDrmInfoListener.onDrmInfo(OnDrmInfoHandlerDelegate.this.mMediaPlayer, drmInfo);
                    }
                });
            } else {
                this.mOnDrmInfoListener.onDrmInfo(this.mMediaPlayer, drmInfo);
            }
        }
    }

    public interface OnDrmInfoListener {
        void onDrmInfo(MediaPlayer mediaPlayer, DrmInfo drmInfo);
    }

    private class OnDrmPreparedHandlerDelegate {
        private Handler mHandler;
        private MediaPlayer mMediaPlayer;
        private OnDrmPreparedListener mOnDrmPreparedListener;

        OnDrmPreparedHandlerDelegate(MediaPlayer mp, OnDrmPreparedListener listener, Handler handler) {
            this.mMediaPlayer = mp;
            this.mOnDrmPreparedListener = listener;
            if (handler != null) {
                this.mHandler = handler;
            } else if (MediaPlayer.this.mEventHandler != null) {
                this.mHandler = MediaPlayer.this.mEventHandler;
            } else {
                Log.e(MediaPlayer.TAG, "OnDrmPreparedHandlerDelegate: Unexpected null mEventHandler");
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void notifyClient(final int status) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.post(new Runnable() {
                    public void run() {
                        OnDrmPreparedHandlerDelegate.this.mOnDrmPreparedListener.onDrmPrepared(OnDrmPreparedHandlerDelegate.this.mMediaPlayer, status);
                    }
                });
            } else {
                Log.e(MediaPlayer.TAG, "OnDrmPreparedHandlerDelegate:notifyClient: Unexpected null mHandler");
            }
        }
    }

    public interface OnDrmPreparedListener {
        void onDrmPrepared(MediaPlayer mediaPlayer, int i);
    }

    public interface OnErrorListener {
        boolean onError(MediaPlayer mediaPlayer, int i, int i2);
    }

    public interface OnInfoListener {
        boolean onInfo(MediaPlayer mediaPlayer, int i, int i2);
    }

    public interface OnMediaTimeDiscontinuityListener {
        void onMediaTimeDiscontinuity(MediaPlayer mediaPlayer, MediaTimestamp mediaTimestamp);
    }

    public interface OnPreparedListener {
        void onPrepared(MediaPlayer mediaPlayer);
    }

    public interface OnSeekCompleteListener {
        void onSeekComplete(MediaPlayer mediaPlayer);
    }

    public interface OnTimedMetaDataAvailableListener {
        void onTimedMetaDataAvailable(MediaPlayer mediaPlayer, TimedMetaData timedMetaData);
    }

    public interface OnTimedTextListener {
        void onTimedText(MediaPlayer mediaPlayer, TimedText timedText);
    }

    public interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PlaybackRateAudioMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PrepareDrmStatusCode {
    }

    public static final class ProvisioningNetworkErrorException extends MediaDrmException {
        public ProvisioningNetworkErrorException(String detailMessage) {
            super(detailMessage);
        }
    }

    public static final class ProvisioningServerErrorException extends MediaDrmException {
        public ProvisioningServerErrorException(String detailMessage) {
            super(detailMessage);
        }
    }

    private class ProvisioningThread extends Thread {
        public static final int TIMEOUT_MS = 60000;
        private Object drmLock;
        private boolean finished;
        private MediaPlayer mediaPlayer;
        private OnDrmPreparedHandlerDelegate onDrmPreparedHandlerDelegate;
        private int status;
        private String urlStr;
        private UUID uuid;

        private ProvisioningThread() {
        }

        /* synthetic */ ProvisioningThread(MediaPlayer x0, AnonymousClass1 x1) {
            this();
        }

        public int status() {
            return this.status;
        }

        public ProvisioningThread initialize(ProvisionRequest request, UUID uuid, MediaPlayer mediaPlayer) {
            this.drmLock = mediaPlayer.mDrmLock;
            this.onDrmPreparedHandlerDelegate = mediaPlayer.mOnDrmPreparedHandlerDelegate;
            this.mediaPlayer = mediaPlayer;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(request.getDefaultUrl());
            stringBuilder.append("&signedRequest=");
            stringBuilder.append(new String(request.getData()));
            this.urlStr = stringBuilder.toString();
            this.uuid = uuid;
            this.status = 3;
            stringBuilder = new StringBuilder();
            stringBuilder.append("HandleProvisioninig: Thread is initialised url: ");
            stringBuilder.append(this.urlStr);
            Log.v(MediaPlayer.TAG, stringBuilder.toString());
            return this;
        }

        public void run() {
            StringBuilder stringBuilder;
            byte[] response = null;
            boolean provisioningSucceeded = false;
            HttpURLConnection connection;
            try {
                URL url = new URL(this.urlStr);
                connection = (HttpURLConnection) url.openConnection();
                try {
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(false);
                    connection.setDoInput(true);
                    connection.setConnectTimeout(60000);
                    connection.setReadTimeout(60000);
                    connection.connect();
                    response = Streams.readFully(connection.getInputStream());
                    String str = MediaPlayer.TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("HandleProvisioninig: Thread run: response ");
                    stringBuilder2.append(response.length);
                    stringBuilder2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    stringBuilder2.append(response);
                    Log.v(str, stringBuilder2.toString());
                    connection.disconnect();
                } catch (Exception e) {
                    this.status = 1;
                    String str2 = MediaPlayer.TAG;
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("HandleProvisioninig: Thread run: connect ");
                    stringBuilder3.append(e);
                    stringBuilder3.append(" url: ");
                    stringBuilder3.append(url);
                    Log.w(str2, stringBuilder3.toString());
                    connection.disconnect();
                }
            } catch (Exception e2) {
                this.status = 1;
                stringBuilder = new StringBuilder();
                stringBuilder.append("HandleProvisioninig: Thread run: openConnection ");
                stringBuilder.append(e2);
                Log.w(MediaPlayer.TAG, stringBuilder.toString());
            } catch (Throwable th) {
                connection.disconnect();
            }
            if (response != null) {
                try {
                    MediaPlayer.this.mDrmObj.provideProvisionResponse(response);
                    Log.v(MediaPlayer.TAG, "HandleProvisioninig: Thread run: provideProvisionResponse SUCCEEDED!");
                    provisioningSucceeded = true;
                } catch (Exception e22) {
                    this.status = 2;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("HandleProvisioninig: Thread run: provideProvisionResponse ");
                    stringBuilder.append(e22);
                    Log.w(MediaPlayer.TAG, stringBuilder.toString());
                }
            }
            boolean succeeded = false;
            int i = 3;
            if (this.onDrmPreparedHandlerDelegate != null) {
                synchronized (this.drmLock) {
                    if (provisioningSucceeded) {
                        succeeded = this.mediaPlayer.resumePrepareDrm(this.uuid);
                        if (succeeded) {
                            i = 0;
                        }
                        this.status = i;
                    }
                    this.mediaPlayer.mDrmProvisioningInProgress = false;
                    this.mediaPlayer.mPrepareDrmInProgress = false;
                    if (!succeeded) {
                        MediaPlayer.this.cleanDrmObj();
                    }
                }
                this.onDrmPreparedHandlerDelegate.notifyClient(this.status);
            } else {
                if (provisioningSucceeded) {
                    succeeded = this.mediaPlayer.resumePrepareDrm(this.uuid);
                    if (succeeded) {
                        i = 0;
                    }
                    this.status = i;
                }
                this.mediaPlayer.mDrmProvisioningInProgress = false;
                this.mediaPlayer.mPrepareDrmInProgress = false;
                if (!succeeded) {
                    MediaPlayer.this.cleanDrmObj();
                }
            }
            this.finished = true;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SeekMode {
    }

    static class TimeProvider implements OnSeekCompleteListener, MediaTimeProvider {
        private static final long MAX_EARLY_CALLBACK_US = 1000;
        private static final long MAX_NS_WITHOUT_POSITION_CHECK = 5000000000L;
        private static final int NOTIFY = 1;
        private static final int NOTIFY_SEEK = 3;
        private static final int NOTIFY_STOP = 2;
        private static final int NOTIFY_TIME = 0;
        private static final int NOTIFY_TRACK_DATA = 4;
        private static final String TAG = "MTP";
        private static final long TIME_ADJUSTMENT_RATE = 2;
        public boolean DEBUG = false;
        private boolean mBuffering;
        private Handler mEventHandler;
        private HandlerThread mHandlerThread;
        private long mLastReportedTime;
        private long mLastTimeUs = 0;
        private OnMediaTimeListener[] mListeners;
        private boolean mPaused = true;
        private boolean mPausing = false;
        private MediaPlayer mPlayer;
        private boolean mRefresh = false;
        private boolean mSeeking = false;
        private boolean mStopped = true;
        private long[] mTimes;

        private class EventHandler extends Handler {
            public EventHandler(Looper looper) {
                super(looper);
            }

            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    int i = msg.arg1;
                    if (i == 0) {
                        TimeProvider.this.notifyTimedEvent(true);
                    } else if (i == 2) {
                        TimeProvider.this.notifyStop();
                    } else if (i == 3) {
                        TimeProvider.this.notifySeek();
                    } else if (i == 4) {
                        TimeProvider.this.notifyTrackData((Pair) msg.obj);
                    }
                }
            }
        }

        public TimeProvider(MediaPlayer mp) {
            this.mPlayer = mp;
            try {
                getCurrentTimeUs(true, false);
            } catch (IllegalStateException e) {
                this.mRefresh = true;
            }
            Looper myLooper = Looper.myLooper();
            Looper looper = myLooper;
            if (myLooper == null) {
                myLooper = Looper.getMainLooper();
                looper = myLooper;
                if (myLooper == null) {
                    this.mHandlerThread = new HandlerThread("MediaPlayerMTPEventThread", -2);
                    this.mHandlerThread.start();
                    looper = this.mHandlerThread.getLooper();
                }
            }
            this.mEventHandler = new EventHandler(looper);
            this.mListeners = new OnMediaTimeListener[0];
            this.mTimes = new long[0];
            this.mLastTimeUs = 0;
        }

        private void scheduleNotification(int type, long delayUs) {
            if (!this.mSeeking || type != 0) {
                if (this.DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("scheduleNotification ");
                    stringBuilder.append(type);
                    stringBuilder.append(" in ");
                    stringBuilder.append(delayUs);
                    Log.v(TAG, stringBuilder.toString());
                }
                this.mEventHandler.removeMessages(1);
                this.mEventHandler.sendMessageDelayed(this.mEventHandler.obtainMessage(1, type, 0), (long) ((int) (delayUs / 1000)));
            }
        }

        public void close() {
            this.mEventHandler.removeMessages(1);
            HandlerThread handlerThread = this.mHandlerThread;
            if (handlerThread != null) {
                handlerThread.quitSafely();
                this.mHandlerThread = null;
            }
        }

        /* Access modifiers changed, original: protected */
        public void finalize() {
            HandlerThread handlerThread = this.mHandlerThread;
            if (handlerThread != null) {
                handlerThread.quitSafely();
            }
        }

        public void onNotifyTime() {
            synchronized (this) {
                if (this.DEBUG) {
                    Log.d(TAG, "onNotifyTime: ");
                }
                scheduleNotification(0, 0);
            }
        }

        public void onPaused(boolean paused) {
            synchronized (this) {
                if (this.DEBUG) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("onPaused: ");
                    stringBuilder.append(paused);
                    Log.d(str, stringBuilder.toString());
                }
                if (this.mStopped) {
                    this.mStopped = false;
                    this.mSeeking = true;
                    scheduleNotification(3, 0);
                } else {
                    this.mPausing = paused;
                    this.mSeeking = false;
                    scheduleNotification(0, 0);
                }
            }
        }

        public void onBuffering(boolean buffering) {
            synchronized (this) {
                if (this.DEBUG) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("onBuffering: ");
                    stringBuilder.append(buffering);
                    Log.d(str, stringBuilder.toString());
                }
                this.mBuffering = buffering;
                scheduleNotification(0, 0);
            }
        }

        public void onStopped() {
            synchronized (this) {
                if (this.DEBUG) {
                    Log.d(TAG, "onStopped");
                }
                this.mPaused = true;
                this.mStopped = true;
                this.mSeeking = false;
                this.mBuffering = false;
                scheduleNotification(2, 0);
            }
        }

        public void onSeekComplete(MediaPlayer mp) {
            synchronized (this) {
                this.mStopped = false;
                this.mSeeking = true;
                scheduleNotification(3, 0);
            }
        }

        public void onNewPlayer() {
            if (this.mRefresh) {
                synchronized (this) {
                    this.mStopped = false;
                    this.mSeeking = true;
                    this.mBuffering = false;
                    scheduleNotification(3, 0);
                }
            }
        }

        private synchronized void notifySeek() {
            this.mSeeking = false;
            try {
                long timeUs = getCurrentTimeUs(true, false);
                if (this.DEBUG) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("onSeekComplete at ");
                    stringBuilder.append(timeUs);
                    Log.d(str, stringBuilder.toString());
                }
                for (OnMediaTimeListener listener : this.mListeners) {
                    if (listener == null) {
                        break;
                    }
                    listener.onSeek(timeUs);
                }
            } catch (IllegalStateException e) {
                if (this.DEBUG) {
                    Log.d(TAG, "onSeekComplete but no player");
                }
                this.mPausing = true;
                notifyTimedEvent(false);
            }
        }

        private synchronized void notifyTrackData(Pair<SubtitleTrack, byte[]> trackData) {
            trackData.first.onData(trackData.second, true, -1);
        }

        private synchronized void notifyStop() {
            for (OnMediaTimeListener listener : this.mListeners) {
                if (listener == null) {
                    break;
                }
                listener.onStop();
            }
        }

        private int registerListener(OnMediaTimeListener listener) {
            OnMediaTimeListener[] onMediaTimeListenerArr;
            int i = 0;
            while (true) {
                onMediaTimeListenerArr = this.mListeners;
                if (i >= onMediaTimeListenerArr.length || onMediaTimeListenerArr[i] == listener || onMediaTimeListenerArr[i] == null) {
                    onMediaTimeListenerArr = this.mListeners;
                } else {
                    i++;
                }
            }
            onMediaTimeListenerArr = this.mListeners;
            if (i >= onMediaTimeListenerArr.length) {
                OnMediaTimeListener[] newListeners = new OnMediaTimeListener[(i + 1)];
                long[] newTimes = new long[(i + 1)];
                System.arraycopy(onMediaTimeListenerArr, 0, newListeners, 0, onMediaTimeListenerArr.length);
                long[] jArr = this.mTimes;
                System.arraycopy(jArr, 0, newTimes, 0, jArr.length);
                this.mListeners = newListeners;
                this.mTimes = newTimes;
            }
            onMediaTimeListenerArr = this.mListeners;
            if (onMediaTimeListenerArr[i] == null) {
                onMediaTimeListenerArr[i] = listener;
                this.mTimes[i] = -1;
            }
            return i;
        }

        public void notifyAt(long timeUs, OnMediaTimeListener listener) {
            synchronized (this) {
                if (this.DEBUG) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("notifyAt ");
                    stringBuilder.append(timeUs);
                    Log.d(str, stringBuilder.toString());
                }
                this.mTimes[registerListener(listener)] = timeUs;
                scheduleNotification(0, 0);
            }
        }

        public void scheduleUpdate(OnMediaTimeListener listener) {
            synchronized (this) {
                if (this.DEBUG) {
                    Log.d(TAG, "scheduleUpdate");
                }
                int i = registerListener(listener);
                if (!this.mStopped) {
                    this.mTimes[i] = 0;
                    scheduleNotification(0, 0);
                }
            }
        }

        public void cancelNotifications(OnMediaTimeListener listener) {
            synchronized (this) {
                int i = 0;
                while (i < this.mListeners.length) {
                    if (this.mListeners[i] == listener) {
                        System.arraycopy(this.mListeners, i + 1, this.mListeners, i, (this.mListeners.length - i) - 1);
                        System.arraycopy(this.mTimes, i + 1, this.mTimes, i, (this.mTimes.length - i) - 1);
                        this.mListeners[this.mListeners.length - 1] = null;
                        this.mTimes[this.mTimes.length - 1] = -1;
                        break;
                    } else if (this.mListeners[i] == null) {
                        break;
                    } else {
                        i++;
                    }
                }
                scheduleNotification(0, 0);
            }
        }

        private synchronized void notifyTimedEvent(boolean refreshTime) {
            boolean z = refreshTime;
            synchronized (this) {
                long nowUs;
                try {
                    nowUs = getCurrentTimeUs(z, true);
                } catch (IllegalStateException e) {
                    this.mRefresh = true;
                    this.mPausing = true;
                    nowUs = getCurrentTimeUs(z, true);
                }
                long nextTimeUs = nowUs;
                if (this.mSeeking) {
                    return;
                }
                long nowUs2;
                long nowUs3;
                if (this.DEBUG) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("notifyTimedEvent(");
                    sb.append(this.mLastTimeUs);
                    sb.append(" -> ");
                    sb.append(nowUs);
                    sb.append(") from {");
                    long[] jArr = this.mTimes;
                    int length = jArr.length;
                    boolean first = true;
                    int first2 = 0;
                    while (first2 < length) {
                        nowUs2 = nowUs;
                        long time = jArr[first2];
                        if (time != -1) {
                            if (!first) {
                                sb.append(", ");
                            }
                            sb.append(time);
                            first = false;
                        }
                        first2++;
                        nowUs = nowUs2;
                    }
                    nowUs2 = nowUs;
                    sb.append("}");
                    Log.d(TAG, sb.toString());
                } else {
                    nowUs2 = nowUs;
                }
                Vector<OnMediaTimeListener> activatedListeners = new Vector();
                int ix = 0;
                while (ix < this.mTimes.length) {
                    if (this.mListeners[ix] == null) {
                        break;
                    }
                    if (this.mTimes[ix] > -1) {
                        if (this.mTimes[ix] <= nowUs2 + 1000) {
                            activatedListeners.add(this.mListeners[ix]);
                            if (this.DEBUG) {
                                Log.d(TAG, Environment.MEDIA_REMOVED);
                            }
                            this.mTimes[ix] = -1;
                        } else if (nextTimeUs == nowUs2 || this.mTimes[ix] < nextTimeUs) {
                            nextTimeUs = this.mTimes[ix];
                        }
                    }
                    ix++;
                }
                if (nextTimeUs <= nowUs2 || this.mPaused) {
                    nowUs3 = nowUs2;
                    this.mEventHandler.removeMessages(1);
                } else {
                    if (this.DEBUG) {
                        String str = TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("scheduling for ");
                        stringBuilder.append(nextTimeUs);
                        stringBuilder.append(" and ");
                        nowUs3 = nowUs2;
                        stringBuilder.append(nowUs3);
                        Log.d(str, stringBuilder.toString());
                    } else {
                        nowUs3 = nowUs2;
                    }
                    this.mPlayer.notifyAt(nextTimeUs);
                }
                Iterator it = activatedListeners.iterator();
                while (it.hasNext()) {
                    ((OnMediaTimeListener) it.next()).onTimedEvent(nowUs3);
                }
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:19:0x002f A:{Catch:{ IllegalStateException -> 0x007f }} */
        /* JADX WARNING: Removed duplicated region for block: B:25:0x0058 A:{SYNTHETIC, Splitter:B:25:0x0058} */
        public long getCurrentTimeUs(boolean r8, boolean r9) throws java.lang.IllegalStateException {
            /*
            r7 = this;
            monitor-enter(r7);
            r0 = r7.mPaused;	 Catch:{ all -> 0x00b9 }
            if (r0 == 0) goto L_0x000b;
        L_0x0005:
            if (r8 != 0) goto L_0x000b;
        L_0x0007:
            r0 = r7.mLastReportedTime;	 Catch:{ all -> 0x00b9 }
            monitor-exit(r7);	 Catch:{ all -> 0x00b9 }
            return r0;
        L_0x000b:
            r0 = 1;
            r1 = 0;
            r2 = r7.mPlayer;	 Catch:{ IllegalStateException -> 0x007f }
            r2 = r2.getCurrentPosition();	 Catch:{ IllegalStateException -> 0x007f }
            r2 = (long) r2;	 Catch:{ IllegalStateException -> 0x007f }
            r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            r2 = r2 * r4;
            r7.mLastTimeUs = r2;	 Catch:{ IllegalStateException -> 0x007f }
            r2 = r7.mPlayer;	 Catch:{ IllegalStateException -> 0x007f }
            r2 = r2.isPlaying();	 Catch:{ IllegalStateException -> 0x007f }
            if (r2 == 0) goto L_0x0028;
        L_0x0021:
            r2 = r7.mBuffering;	 Catch:{ IllegalStateException -> 0x007f }
            if (r2 == 0) goto L_0x0026;
        L_0x0025:
            goto L_0x0028;
        L_0x0026:
            r2 = r1;
            goto L_0x0029;
        L_0x0028:
            r2 = r0;
        L_0x0029:
            r7.mPaused = r2;	 Catch:{ IllegalStateException -> 0x007f }
            r2 = r7.DEBUG;	 Catch:{ IllegalStateException -> 0x007f }
            if (r2 == 0) goto L_0x0055;
        L_0x002f:
            r2 = "MTP";
            r3 = new java.lang.StringBuilder;	 Catch:{ IllegalStateException -> 0x007f }
            r3.<init>();	 Catch:{ IllegalStateException -> 0x007f }
            r4 = r7.mPaused;	 Catch:{ IllegalStateException -> 0x007f }
            if (r4 == 0) goto L_0x003e;
        L_0x003a:
            r4 = "paused";
            goto L_0x0041;
        L_0x003e:
            r4 = "playing";
        L_0x0041:
            r3.append(r4);	 Catch:{ IllegalStateException -> 0x007f }
            r4 = " at ";
            r3.append(r4);	 Catch:{ IllegalStateException -> 0x007f }
            r4 = r7.mLastTimeUs;	 Catch:{ IllegalStateException -> 0x007f }
            r3.append(r4);	 Catch:{ IllegalStateException -> 0x007f }
            r3 = r3.toString();	 Catch:{ IllegalStateException -> 0x007f }
            android.util.Log.v(r2, r3);	 Catch:{ IllegalStateException -> 0x007f }
            if (r9 == 0) goto L_0x0077;
        L_0x0058:
            r2 = r7.mLastTimeUs;	 Catch:{ all -> 0x00b9 }
            r4 = r7.mLastReportedTime;	 Catch:{ all -> 0x00b9 }
            r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r2 >= 0) goto L_0x0077;
        L_0x0060:
            r2 = r7.mLastReportedTime;	 Catch:{ all -> 0x00b9 }
            r4 = r7.mLastTimeUs;	 Catch:{ all -> 0x00b9 }
            r2 = r2 - r4;
            r4 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
            r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r2 <= 0) goto L_0x007b;
        L_0x006c:
            r7.mStopped = r1;	 Catch:{ all -> 0x00b9 }
            r7.mSeeking = r0;	 Catch:{ all -> 0x00b9 }
            r0 = 3;
            r1 = 0;
            r7.scheduleNotification(r0, r1);	 Catch:{ all -> 0x00b9 }
            goto L_0x007b;
        L_0x0077:
            r0 = r7.mLastTimeUs;	 Catch:{ all -> 0x00b9 }
            r7.mLastReportedTime = r0;	 Catch:{ all -> 0x00b9 }
        L_0x007b:
            r0 = r7.mLastReportedTime;	 Catch:{ all -> 0x00b9 }
            monitor-exit(r7);	 Catch:{ all -> 0x00b9 }
            return r0;
        L_0x007f:
            r2 = move-exception;
            r3 = r7.mPausing;	 Catch:{ all -> 0x00b9 }
            if (r3 == 0) goto L_0x00b7;
        L_0x0084:
            r7.mPausing = r1;	 Catch:{ all -> 0x00b9 }
            if (r9 == 0) goto L_0x0090;
        L_0x0088:
            r3 = r7.mLastReportedTime;	 Catch:{ all -> 0x00b9 }
            r5 = r7.mLastTimeUs;	 Catch:{ all -> 0x00b9 }
            r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
            if (r1 >= 0) goto L_0x0094;
        L_0x0090:
            r3 = r7.mLastTimeUs;	 Catch:{ all -> 0x00b9 }
            r7.mLastReportedTime = r3;	 Catch:{ all -> 0x00b9 }
        L_0x0094:
            r7.mPaused = r0;	 Catch:{ all -> 0x00b9 }
            r0 = r7.DEBUG;	 Catch:{ all -> 0x00b9 }
            if (r0 == 0) goto L_0x00b3;
        L_0x009a:
            r0 = "MTP";
            r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b9 }
            r1.<init>();	 Catch:{ all -> 0x00b9 }
            r3 = "illegal state, but pausing: estimating at ";
            r1.append(r3);	 Catch:{ all -> 0x00b9 }
            r3 = r7.mLastReportedTime;	 Catch:{ all -> 0x00b9 }
            r1.append(r3);	 Catch:{ all -> 0x00b9 }
            r1 = r1.toString();	 Catch:{ all -> 0x00b9 }
            android.util.Log.d(r0, r1);	 Catch:{ all -> 0x00b9 }
        L_0x00b3:
            r0 = r7.mLastReportedTime;	 Catch:{ all -> 0x00b9 }
            monitor-exit(r7);	 Catch:{ all -> 0x00b9 }
            return r0;
            throw r2;	 Catch:{ all -> 0x00b9 }
        L_0x00b9:
            r0 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x00b9 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.MediaPlayer$TimeProvider.getCurrentTimeUs(boolean, boolean):long");
        }
    }

    public static class TrackInfo implements Parcelable {
        @UnsupportedAppUsage
        static final Creator<TrackInfo> CREATOR = new Creator<TrackInfo>() {
            public TrackInfo createFromParcel(Parcel in) {
                return new TrackInfo(in);
            }

            public TrackInfo[] newArray(int size) {
                return new TrackInfo[size];
            }
        };
        public static final int MEDIA_TRACK_TYPE_AUDIO = 2;
        public static final int MEDIA_TRACK_TYPE_METADATA = 5;
        public static final int MEDIA_TRACK_TYPE_SUBTITLE = 4;
        public static final int MEDIA_TRACK_TYPE_TIMEDTEXT = 3;
        public static final int MEDIA_TRACK_TYPE_UNKNOWN = 0;
        public static final int MEDIA_TRACK_TYPE_VIDEO = 1;
        final MediaFormat mFormat;
        final int mTrackType;

        @Retention(RetentionPolicy.SOURCE)
        public @interface TrackType {
        }

        public int getTrackType() {
            return this.mTrackType;
        }

        public String getLanguage() {
            String language = this.mFormat.getString("language");
            return language == null ? "und" : language;
        }

        public MediaFormat getFormat() {
            int i = this.mTrackType;
            if (i == 3 || i == 4) {
                return this.mFormat;
            }
            return null;
        }

        TrackInfo(Parcel in) {
            this.mTrackType = in.readInt();
            this.mFormat = MediaFormat.createSubtitleFormat(in.readString(), in.readString());
            if (this.mTrackType == 4) {
                this.mFormat.setInteger(MediaFormat.KEY_IS_AUTOSELECT, in.readInt());
                this.mFormat.setInteger(MediaFormat.KEY_IS_DEFAULT, in.readInt());
                this.mFormat.setInteger(MediaFormat.KEY_IS_FORCED_SUBTITLE, in.readInt());
            }
        }

        TrackInfo(int type, MediaFormat format) {
            this.mTrackType = type;
            this.mFormat = format;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mTrackType);
            dest.writeString(this.mFormat.getString(MediaFormat.KEY_MIME));
            dest.writeString(getLanguage());
            if (this.mTrackType == 4) {
                dest.writeInt(this.mFormat.getInteger(MediaFormat.KEY_IS_AUTOSELECT));
                dest.writeInt(this.mFormat.getInteger(MediaFormat.KEY_IS_DEFAULT));
                dest.writeInt(this.mFormat.getInteger(MediaFormat.KEY_IS_FORCED_SUBTITLE));
            }
        }

        public String toString() {
            StringBuilder out = new StringBuilder(128);
            out.append(getClass().getName());
            out.append('{');
            int i = this.mTrackType;
            if (i == 1) {
                out.append("VIDEO");
            } else if (i == 2) {
                out.append("AUDIO");
            } else if (i == 3) {
                out.append("TIMEDTEXT");
            } else if (i != 4) {
                out.append(IccCardConstants.INTENT_VALUE_ICC_UNKNOWN);
            } else {
                out.append("SUBTITLE");
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(", ");
            stringBuilder.append(this.mFormat.toString());
            out.append(stringBuilder.toString());
            out.append("}");
            return out.toString();
        }
    }

    private native int _getAudioStreamType() throws IllegalStateException;

    private native void _notifyAt(long j);

    private native void _pause() throws IllegalStateException;

    private native void _prepare() throws IOException, IllegalStateException;

    private native void _prepareDrm(byte[] bArr, byte[] bArr2);

    private native void _release();

    private native void _releaseDrm();

    private native void _reset();

    private final native void _seekTo(long j, int i);

    private native void _setAudioStreamType(int i);

    private native void _setAuxEffectSendLevel(float f);

    private native void _setDataSource(MediaDataSource mediaDataSource) throws IllegalArgumentException, IllegalStateException;

    private native void _setDataSource(FileDescriptor fileDescriptor, long j, long j2) throws IOException, IllegalArgumentException, IllegalStateException;

    private native void _setVideoSurface(Surface surface);

    private native void _setVolume(float f, float f2);

    private native void _start() throws IllegalStateException;

    private native void _stop() throws IllegalStateException;

    private native void nativeSetDataSource(IBinder iBinder, String str, String[] strArr, String[] strArr2) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    private native int native_applyVolumeShaper(Configuration configuration, Operation operation);

    private final native void native_enableDeviceCallback(boolean z);

    private final native void native_finalize();

    private final native boolean native_getMetadata(boolean z, boolean z2, Parcel parcel);

    private native PersistableBundle native_getMetrics();

    private final native int native_getRoutedDeviceId();

    private native State native_getVolumeShaperState(int i);

    private static final native void native_init();

    private final native int native_invoke(Parcel parcel, Parcel parcel2);

    public static native int native_pullBatteryData(Parcel parcel);

    private final native int native_setMetadataFilter(Parcel parcel);

    private final native boolean native_setOutputDevice(int i);

    private final native int native_setRetransmitEndpoint(String str, int i);

    private final native void native_setup(Object obj);

    @UnsupportedAppUsage
    private native boolean setParameter(int i, Parcel parcel);

    public native void attachAuxEffect(int i);

    public native int getAudioSessionId();

    public native int getCurrentPosition();

    public native int getDuration();

    public native PlaybackParams getPlaybackParams();

    public native SyncParams getSyncParams();

    public native int getVideoHeight();

    public native int getVideoWidth();

    public native boolean isLooping();

    public native boolean isPlaying();

    public native void prepareAsync() throws IllegalStateException;

    public native void setAudioSessionId(int i) throws IllegalArgumentException, IllegalStateException;

    public native void setLooping(boolean z);

    public native void setNextMediaPlayer(MediaPlayer mediaPlayer);

    public native void setPlaybackParams(PlaybackParams playbackParams);

    public native void setSyncParams(SyncParams syncParams);

    static {
        System.loadLibrary("media_jni");
        native_init();
    }

    public MediaPlayer() {
        super(new Builder().build(), 2);
        Looper myLooper = Looper.myLooper();
        Looper looper = myLooper;
        if (myLooper != null) {
            this.mEventHandler = new EventHandler(this, looper);
        } else {
            myLooper = Looper.getMainLooper();
            looper = myLooper;
            if (myLooper != null) {
                this.mEventHandler = new EventHandler(this, looper);
            } else {
                this.mEventHandler = null;
            }
        }
        this.mTimeProvider = new TimeProvider(this);
        this.mOpenSubtitleSources = new Vector();
        native_setup(new WeakReference(this));
        baseRegisterPlayer();
    }

    @UnsupportedAppUsage
    public Parcel newRequest() {
        Parcel parcel = Parcel.obtain();
        parcel.writeInterfaceToken(IMEDIA_PLAYER);
        return parcel;
    }

    @UnsupportedAppUsage
    public void invoke(Parcel request, Parcel reply) {
        int retcode = native_invoke(request, reply);
        reply.setDataPosition(0);
        if (retcode != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("failure code: ");
            stringBuilder.append(retcode);
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    public void setDisplay(SurfaceHolder sh) {
        Surface surface;
        this.mSurfaceHolder = sh;
        if (sh != null) {
            surface = sh.getSurface();
        } else {
            surface = null;
        }
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    public void setSurface(Surface surface) {
        if (this.mScreenOnWhilePlaying && surface != null) {
            Log.w(TAG, "setScreenOnWhilePlaying(true) is ineffective for Surface");
        }
        this.mSurfaceHolder = null;
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    public void setVideoScalingMode(int mode) {
        if (isVideoScalingModeSupported(mode)) {
            Parcel request = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                request.writeInterfaceToken(IMEDIA_PLAYER);
                request.writeInt(6);
                request.writeInt(mode);
                invoke(request, reply);
            } finally {
                request.recycle();
                reply.recycle();
            }
        } else {
            String msg = new StringBuilder();
            msg.append("Scaling mode ");
            msg.append(mode);
            msg.append(" is not supported");
            throw new IllegalArgumentException(msg.toString());
        }
    }

    public static MediaPlayer create(Context context, Uri uri) {
        return create(context, uri, null);
    }

    public static MediaPlayer create(Context context, Uri uri, SurfaceHolder holder) {
        int s = AudioSystem.newAudioSessionId();
        return create(context, uri, holder, null, s > 0 ? s : 0);
    }

    public static MediaPlayer create(Context context, Uri uri, SurfaceHolder holder, AudioAttributes audioAttributes, int audioSessionId) {
        String str = "create failed:";
        String str2 = TAG;
        try {
            AudioAttributes aa;
            MediaPlayer mp = new MediaPlayer();
            if (audioAttributes != null) {
                aa = audioAttributes;
            } else {
                aa = new Builder().build();
            }
            mp.setAudioAttributes(aa);
            mp.setAudioSessionId(audioSessionId);
            mp.setDataSource(context, uri);
            if (holder != null) {
                mp.setDisplay(holder);
            }
            mp.prepare();
            return mp;
        } catch (IOException ex) {
            Log.d(str2, str, ex);
            return null;
        } catch (IllegalArgumentException ex2) {
            Log.d(str2, str, ex2);
            return null;
        } catch (SecurityException ex3) {
            Log.d(str2, str, ex3);
            return null;
        }
    }

    public static MediaPlayer create(Context context, int resid) {
        int s = AudioSystem.newAudioSessionId();
        return create(context, resid, null, s > 0 ? s : 0);
    }

    public static MediaPlayer create(Context context, int resid, AudioAttributes audioAttributes, int audioSessionId) {
        String str = "create failed:";
        String str2 = TAG;
        try {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(resid);
            if (afd == null) {
                return null;
            }
            AudioAttributes audioAttributes2;
            MediaPlayer mp = new MediaPlayer();
            if (audioAttributes != null) {
                audioAttributes2 = audioAttributes;
            } else {
                audioAttributes2 = new Builder().build();
            }
            mp.setAudioAttributes(audioAttributes2);
            mp.setAudioSessionId(audioSessionId);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepare();
            return mp;
        } catch (IOException ex) {
            Log.d(str2, str, ex);
            return null;
        } catch (IllegalArgumentException ex2) {
            Log.d(str2, str, ex2);
            return null;
        } catch (SecurityException ex3) {
            Log.d(str2, str, ex3);
            return null;
        }
    }

    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        setDataSource(context, uri, null, null);
    }

    /* JADX WARNING: Missing block: B:26:0x009a, code skipped:
            if (SMS_SLOT2_URI.equals(r12.toString()) != false) goto L_0x009c;
     */
    public void setDataSource(android.content.Context r11, android.net.Uri r12, java.util.Map<java.lang.String, java.lang.String> r13, java.util.List<java.net.HttpCookie> r14) throws java.io.IOException {
        /*
        r10 = this;
        if (r11 == 0) goto L_0x0102;
    L_0x0002:
        if (r12 == 0) goto L_0x00f9;
    L_0x0004:
        if (r14 == 0) goto L_0x0019;
    L_0x0006:
        r0 = java.net.CookieHandler.getDefault();
        if (r0 == 0) goto L_0x0019;
    L_0x000c:
        r1 = r0 instanceof java.net.CookieManager;
        if (r1 == 0) goto L_0x0011;
    L_0x0010:
        goto L_0x0019;
    L_0x0011:
        r1 = new java.lang.IllegalArgumentException;
        r2 = "The cookie handler has to be of CookieManager type when cookies are provided.";
        r1.<init>(r2);
        throw r1;
    L_0x0019:
        r0 = android.media.MediaPlayerInjector.needMuteNotification(r11);
        r10.mNeedMuteNotification = r0;
        r0 = r11.getContentResolver();
        r1 = r12.getScheme();
        r2 = r12.getAuthority();
        r2 = android.content.ContentProvider.getAuthorityWithoutUserId(r2);
        r3 = "file";
        r3 = r3.equals(r1);
        if (r3 == 0) goto L_0x003f;
    L_0x0037:
        r3 = r12.getPath();
        r10.setDataSource(r3);
        return;
    L_0x003f:
        r3 = "content";
        r3 = r3.equals(r1);
        if (r3 == 0) goto L_0x00ea;
    L_0x0047:
        r3 = "settings";
        r3 = r3.equals(r2);
        if (r3 == 0) goto L_0x00ea;
    L_0x0050:
        r3 = android.media.ExtraRingtoneManager.getDefaultSoundType(r12);
        r4 = r11.getUserId();
        r4 = android.media.RingtoneManager.getCacheForType(r3, r4);
        r5 = android.media.ExtraRingtoneManager.getActualDefaultRingtoneUri(r11, r3);
        r6 = r12.toString();
        r7 = "content://settings/system/notification_sound";
        r6 = r7.equals(r6);
        if (r6 != 0) goto L_0x009c;
    L_0x006c:
        r6 = r12.toString();
        r7 = "content://settings/system/calendar_alert";
        r6 = r7.equals(r6);
        if (r6 != 0) goto L_0x009c;
    L_0x0078:
        r6 = r12.toString();
        r7 = "content://settings/system/sms_received_sound";
        r6 = r7.equals(r6);
        if (r6 != 0) goto L_0x009c;
    L_0x0084:
        r6 = r12.toString();
        r7 = "content://settings/system/sms_received_sound_slot_1";
        r6 = r7.equals(r6);
        if (r6 != 0) goto L_0x009c;
    L_0x0090:
        r6 = r12.toString();
        r7 = "content://settings/system/sms_received_sound_slot_2";
        r6 = r7.equals(r6);
        if (r6 == 0) goto L_0x00d4;
    L_0x009c:
        r6 = r12.toString();
        r6 = r10.getNotificationUri(r6);
        r7 = "MediaPlayer";
        if (r6 != 0) goto L_0x00b2;
    L_0x00a8:
        r8 = 1;
        r10.mNeedMuteNotification = r8;
        r8 = "setDataSource() randomUri = null";
        android.util.Log.w(r7, r8);
        goto L_0x00bf;
    L_0x00b2:
        r8 = "normal_notification";
        r8 = r8.equals(r6);
        if (r8 != 0) goto L_0x00bf;
    L_0x00bb:
        r5 = android.net.Uri.parse(r6);
    L_0x00bf:
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "setDataSource() actualUri=";
        r8.append(r9);
        r8.append(r5);
        r8 = r8.toString();
        android.util.Log.d(r7, r8);
    L_0x00d4:
        r6 = r10.attemptDataSource(r0, r4);
        if (r6 == 0) goto L_0x00db;
    L_0x00da:
        return;
    L_0x00db:
        r6 = r10.attemptDataSource(r0, r5);
        if (r6 == 0) goto L_0x00e2;
    L_0x00e1:
        return;
    L_0x00e2:
        r6 = r12.toString();
        r10.setDataSource(r6, r13, r14);
        goto L_0x00f8;
    L_0x00ea:
        r3 = r10.attemptDataSource(r0, r12);
        if (r3 == 0) goto L_0x00f1;
    L_0x00f0:
        return;
    L_0x00f1:
        r3 = r12.toString();
        r10.setDataSource(r3, r13, r14);
    L_0x00f8:
        return;
    L_0x00f9:
        r0 = new java.lang.NullPointerException;
        r1 = "uri param can not be null.";
        r0.<init>(r1);
        throw r0;
    L_0x0102:
        r0 = new java.lang.NullPointerException;
        r1 = "context param can not be null.";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaPlayer.setDataSource(android.content.Context, android.net.Uri, java.util.Map, java.util.List):void");
    }

    private String getNotificationUri(String type) {
        try {
            return Stub.asInterface(ServiceManager.getService("audio")).getNotificationUri(type);
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("cannot getNotificationUri(): ");
            stringBuilder.append(e);
            Log.e(TAG, stringBuilder.toString());
            return null;
        }
    }

    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        setDataSource(context, uri, (Map) headers, null);
    }

    /* JADX WARNING: Missing block: B:14:0x0015, code skipped:
            if (r0 != null) goto L_0x0017;
     */
    /* JADX WARNING: Missing block: B:16:?, code skipped:
            $closeResource(r1, r0);
     */
    private boolean attemptDataSource(android.content.ContentResolver r4, android.net.Uri r5) {
        /*
        r3 = this;
        r0 = "r";
        r0 = r4.openAssetFileDescriptor(r5, r0);	 Catch:{ IOException | NullPointerException | SecurityException -> 0x001b, IOException | NullPointerException | SecurityException -> 0x001b, IOException | NullPointerException | SecurityException -> 0x001b }
        r1 = 0;
        r3.setDataSource(r0);	 Catch:{ all -> 0x0012 }
        r2 = 1;
        if (r0 == 0) goto L_0x0011;
    L_0x000e:
        $closeResource(r1, r0);	 Catch:{ IOException | NullPointerException | SecurityException -> 0x001b, IOException | NullPointerException | SecurityException -> 0x001b, IOException | NullPointerException | SecurityException -> 0x001b }
    L_0x0011:
        return r2;
    L_0x0012:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0014 }
    L_0x0014:
        r2 = move-exception;
        if (r0 == 0) goto L_0x001a;
    L_0x0017:
        $closeResource(r1, r0);	 Catch:{ IOException | NullPointerException | SecurityException -> 0x001b, IOException | NullPointerException | SecurityException -> 0x001b, IOException | NullPointerException | SecurityException -> 0x001b }
    L_0x001a:
        throw r2;	 Catch:{ IOException | NullPointerException | SecurityException -> 0x001b, IOException | NullPointerException | SecurityException -> 0x001b, IOException | NullPointerException | SecurityException -> 0x001b }
    L_0x001b:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Couldn't open ";
        r1.append(r2);
        if (r5 != 0) goto L_0x002c;
    L_0x0028:
        r2 = "null uri";
        goto L_0x0030;
    L_0x002c:
        r2 = r5.toSafeString();
    L_0x0030:
        r1.append(r2);
        r1 = r1.toString();
        r2 = "MediaPlayer";
        android.util.Log.w(r2, r1, r0);
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaPlayer.attemptDataSource(android.content.ContentResolver, android.net.Uri):boolean");
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

    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        setDataSource(path, null, null);
    }

    @UnsupportedAppUsage
    public void setDataSource(String path, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        setDataSource(path, (Map) headers, null);
    }

    @UnsupportedAppUsage
    private void setDataSource(String path, Map<String, String> headers, List<HttpCookie> cookies) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        String[] keys = null;
        String[] values = null;
        if (headers != null) {
            keys = new String[headers.size()];
            values = new String[headers.size()];
            int i = 0;
            for (Entry<String, String> entry : headers.entrySet()) {
                keys[i] = (String) entry.getKey();
                values[i] = (String) entry.getValue();
                i++;
            }
        }
        setDataSource(path, keys, values, (List) cookies);
    }

    /* JADX WARNING: Missing block: B:15:0x0039, code skipped:
            $closeResource(r4, r3);
     */
    @android.annotation.UnsupportedAppUsage
    private void setDataSource(java.lang.String r7, java.lang.String[] r8, java.lang.String[] r9, java.util.List<java.net.HttpCookie> r10) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.SecurityException, java.lang.IllegalStateException {
        /*
        r6 = this;
        r0 = android.net.Uri.parse(r7);
        r1 = r0.getScheme();
        r2 = "file";
        r2 = r2.equals(r1);
        if (r2 == 0) goto L_0x0015;
    L_0x0010:
        r7 = r0.getPath();
        goto L_0x0020;
    L_0x0015:
        if (r1 == 0) goto L_0x0020;
        r2 = android.media.MediaHTTPService.createHttpServiceBinderIfNecessary(r7, r10);
        r6.nativeSetDataSource(r2, r7, r8, r9);
        return;
    L_0x0020:
        r2 = new java.io.File;
        r2.<init>(r7);
        r3 = new java.io.FileInputStream;
        r3.<init>(r2);
        r4 = 0;
        r5 = r3.getFD();	 Catch:{ all -> 0x0036 }
        r6.setDataSource(r5);	 Catch:{ all -> 0x0036 }
        $closeResource(r4, r3);
        return;
    L_0x0036:
        r4 = move-exception;
        throw r4;	 Catch:{ all -> 0x0038 }
    L_0x0038:
        r5 = move-exception;
        $closeResource(r4, r3);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaPlayer.setDataSource(java.lang.String, java.lang.String[], java.lang.String[], java.util.List):void");
    }

    public void setDataSource(AssetFileDescriptor afd) throws IOException, IllegalArgumentException, IllegalStateException {
        Preconditions.checkNotNull(afd);
        if (afd.getDeclaredLength() < 0) {
            setDataSource(afd.getFileDescriptor());
            return;
        }
        setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
    }

    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
        setDataSource(fd, 0, 576460752303423487L);
    }

    public void setDataSource(FileDescriptor fd, long offset, long length) throws IOException, IllegalArgumentException, IllegalStateException {
        _setDataSource(fd, offset, length);
    }

    public void setDataSource(MediaDataSource dataSource) throws IllegalArgumentException, IllegalStateException {
        _setDataSource(dataSource);
    }

    public void prepare() throws IOException, IllegalStateException {
        _prepare();
        scanInternalSubtitleTracks();
        synchronized (this.mDrmLock) {
            this.mDrmInfoResolved = true;
        }
    }

    public void start() throws IllegalStateException {
        final int delay = getStartDelayMs();
        if (delay == 0) {
            startImpl();
        } else {
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep((long) delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MediaPlayer.this.baseSetStartDelayMs(0);
                    try {
                        MediaPlayer.this.startImpl();
                    } catch (IllegalStateException e2) {
                    }
                }
            }.start();
        }
    }

    private void startImpl() {
        if (this.mNeedMuteNotification) {
            setVolume(0.0f);
            this.mNeedMuteNotification = false;
            Log.d(TAG, "startImpl(), mNeedMuteNotification=true");
        }
        baseStart();
        stayAwake(true);
        _start();
    }

    private int getAudioStreamType() {
        if (this.mStreamType == Integer.MIN_VALUE) {
            this.mStreamType = _getAudioStreamType();
        }
        return this.mStreamType;
    }

    public void stop() throws IllegalStateException {
        stayAwake(false);
        _stop();
        baseStop();
    }

    public void pause() throws IllegalStateException {
        stayAwake(false);
        _pause();
        basePause();
    }

    /* Access modifiers changed, original: 0000 */
    public void playerStart() {
        start();
    }

    /* Access modifiers changed, original: 0000 */
    public void playerPause() {
        pause();
    }

    /* Access modifiers changed, original: 0000 */
    public void playerStop() {
        stop();
    }

    /* Access modifiers changed, original: 0000 */
    public int playerApplyVolumeShaper(Configuration configuration, Operation operation) {
        return native_applyVolumeShaper(configuration, operation);
    }

    /* Access modifiers changed, original: 0000 */
    public State playerGetVolumeShaperState(int id) {
        return native_getVolumeShaperState(id);
    }

    public VolumeShaper createVolumeShaper(Configuration configuration) {
        return new VolumeShaper(configuration, this);
    }

    public boolean setPreferredDevice(AudioDeviceInfo deviceInfo) {
        int preferredDeviceId = 0;
        if (deviceInfo != null && !deviceInfo.isSink()) {
            return false;
        }
        if (deviceInfo != null) {
            preferredDeviceId = deviceInfo.getId();
        }
        boolean status = native_setOutputDevice(preferredDeviceId);
        if (status) {
            synchronized (this) {
                this.mPreferredDevice = deviceInfo;
            }
        }
        return status;
    }

    public AudioDeviceInfo getPreferredDevice() {
        AudioDeviceInfo audioDeviceInfo;
        synchronized (this) {
            audioDeviceInfo = this.mPreferredDevice;
        }
        return audioDeviceInfo;
    }

    public AudioDeviceInfo getRoutedDevice() {
        int deviceId = native_getRoutedDeviceId();
        if (deviceId == 0) {
            return null;
        }
        AudioDeviceInfo[] devices = AudioManager.getDevicesStatic(2);
        for (int i = 0; i < devices.length; i++) {
            if (devices[i].getId() == deviceId) {
                return devices[i];
            }
        }
        return null;
    }

    @GuardedBy({"mRoutingChangeListeners"})
    private void enableNativeRoutingCallbacksLocked(boolean enabled) {
        if (this.mRoutingChangeListeners.size() == 0) {
            native_enableDeviceCallback(enabled);
        }
    }

    public void addOnRoutingChangedListener(OnRoutingChangedListener listener, Handler handler) {
        synchronized (this.mRoutingChangeListeners) {
            if (listener != null) {
                if (!this.mRoutingChangeListeners.containsKey(listener)) {
                    enableNativeRoutingCallbacksLocked(true);
                    this.mRoutingChangeListeners.put(listener, new NativeRoutingEventHandlerDelegate(this, listener, handler != null ? handler : this.mEventHandler));
                }
            }
        }
    }

    public void removeOnRoutingChangedListener(OnRoutingChangedListener listener) {
        synchronized (this.mRoutingChangeListeners) {
            if (this.mRoutingChangeListeners.containsKey(listener)) {
                this.mRoutingChangeListeners.remove(listener);
                enableNativeRoutingCallbacksLocked(false);
            }
        }
    }

    public void setWakeMode(Context context, int mode) {
        boolean washeld = false;
        if (SystemProperties.getBoolean("audio.offload.ignore_setawake", false)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("IGNORING setWakeMode ");
            stringBuilder.append(mode);
            Log.w(TAG, stringBuilder.toString());
            return;
        }
        WakeLock wakeLock = this.mWakeLock;
        if (wakeLock != null) {
            if (wakeLock.isHeld()) {
                washeld = true;
                this.mWakeLock.release();
            }
            this.mWakeLock = null;
        }
        this.mWakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(536870912 | mode, MediaPlayer.class.getName());
        this.mWakeLock.setReferenceCounted(false);
        if (washeld) {
            this.mWakeLock.acquire();
        }
    }

    public void setScreenOnWhilePlaying(boolean screenOn) {
        if (this.mScreenOnWhilePlaying != screenOn) {
            if (screenOn && this.mSurfaceHolder == null) {
                Log.w(TAG, "setScreenOnWhilePlaying(true) is ineffective without a SurfaceHolder");
            }
            this.mScreenOnWhilePlaying = screenOn;
            updateSurfaceScreenOn();
        }
    }

    private void stayAwake(boolean awake) {
        WakeLock wakeLock = this.mWakeLock;
        if (wakeLock != null) {
            if (awake && !wakeLock.isHeld()) {
                this.mWakeLock.acquire();
            } else if (!awake && this.mWakeLock.isHeld()) {
                this.mWakeLock.release();
            }
        }
        MediaPlayerInjector.updateActiveProcessStatus(this.mStayAwake, awake, Process.myPid(), Process.myUid());
        this.mStayAwake = awake;
        updateSurfaceScreenOn();
    }

    private void updateSurfaceScreenOn() {
        SurfaceHolder surfaceHolder = this.mSurfaceHolder;
        if (surfaceHolder != null) {
            boolean z = this.mScreenOnWhilePlaying && this.mStayAwake;
            surfaceHolder.setKeepScreenOn(z);
        }
    }

    public PersistableBundle getMetrics() {
        return native_getMetrics();
    }

    public PlaybackParams easyPlaybackParams(float rate, int audioMode) {
        PlaybackParams params = new PlaybackParams();
        params.allowDefaults();
        if (audioMode == 0) {
            params.setSpeed(rate).setPitch(1.0f);
        } else if (audioMode == 1) {
            params.setSpeed(rate).setPitch(1.0f).setAudioFallbackMode(2);
        } else if (audioMode == 2) {
            params.setSpeed(rate).setPitch(rate);
        } else {
            String msg = new StringBuilder();
            msg.append("Audio playback mode ");
            msg.append(audioMode);
            msg.append(" is not supported");
            throw new IllegalArgumentException(msg.toString());
        }
        return params;
    }

    public void seekTo(long msec, int mode) {
        if (mode < 0 || mode > 3) {
            String msg = new StringBuilder();
            msg.append("Illegal seek mode: ");
            msg.append(mode);
            throw new IllegalArgumentException(msg.toString());
        }
        int i = (msec > 2147483647L ? 1 : (msec == 2147483647L ? 0 : -1));
        String str = "seekTo offset ";
        String str2 = TAG;
        StringBuilder stringBuilder;
        if (i > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(msec);
            stringBuilder.append(" is too large, cap to ");
            stringBuilder.append(Integer.MAX_VALUE);
            Log.w(str2, stringBuilder.toString());
            msec = 2147483647L;
        } else if (msec < -2147483648L) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(msec);
            stringBuilder.append(" is too small, cap to ");
            stringBuilder.append(Integer.MIN_VALUE);
            Log.w(str2, stringBuilder.toString());
            msec = -2147483648L;
        }
        _seekTo(msec, mode);
    }

    public void seekTo(int msec) throws IllegalStateException {
        seekTo((long) msec, 0);
    }

    public MediaTimestamp getTimestamp() {
        try {
            return new MediaTimestamp(((long) getCurrentPosition()) * 1000, System.nanoTime(), isPlaying() ? getPlaybackParams().getSpeed() : 0.0f);
        } catch (IllegalStateException e) {
            return null;
        }
    }

    @UnsupportedAppUsage
    public Metadata getMetadata(boolean update_only, boolean apply_filter) {
        Parcel reply = Parcel.obtain();
        Metadata data = new Metadata();
        if (!native_getMetadata(update_only, apply_filter, reply)) {
            reply.recycle();
            return null;
        } else if (data.parse(reply)) {
            return data;
        } else {
            reply.recycle();
            return null;
        }
    }

    public int setMetadataFilter(Set<Integer> allow, Set<Integer> block) {
        Parcel request = newRequest();
        int capacity = request.dataSize() + ((((allow.size() + 1) + 1) + block.size()) * 4);
        if (request.dataCapacity() < capacity) {
            request.setDataCapacity(capacity);
        }
        request.writeInt(allow.size());
        for (Integer t : allow) {
            request.writeInt(t.intValue());
        }
        request.writeInt(block.size());
        for (Integer t2 : block) {
            request.writeInt(t2.intValue());
        }
        return native_setMetadataFilter(request);
    }

    public void release() {
        baseRelease();
        stayAwake(false);
        updateSurfaceScreenOn();
        this.mOnPreparedListener = null;
        this.mOnBufferingUpdateListener = null;
        this.mOnCompletionListener = null;
        this.mOnSeekCompleteListener = null;
        this.mOnErrorListener = null;
        this.mOnInfoListener = null;
        this.mOnVideoSizeChangedListener = null;
        this.mOnTimedTextListener = null;
        synchronized (this.mTimeProviderLock) {
            if (this.mTimeProvider != null) {
                this.mTimeProvider.close();
                this.mTimeProvider = null;
            }
        }
        synchronized (this) {
            this.mSubtitleDataListenerDisabled = false;
            this.mExtSubtitleDataListener = null;
            this.mExtSubtitleDataHandler = null;
            this.mOnMediaTimeDiscontinuityListener = null;
            this.mOnMediaTimeDiscontinuityHandler = null;
        }
        this.mOnDrmConfigHelper = null;
        this.mOnDrmInfoHandlerDelegate = null;
        this.mOnDrmPreparedHandlerDelegate = null;
        resetDrmState();
        _release();
    }

    public void reset() {
        this.mSelectedSubtitleTrackIndex = -1;
        synchronized (this.mOpenSubtitleSources) {
            Iterator it = this.mOpenSubtitleSources.iterator();
            while (it.hasNext()) {
                try {
                    ((InputStream) it.next()).close();
                } catch (IOException e) {
                }
            }
            this.mOpenSubtitleSources.clear();
        }
        SubtitleController subtitleController = this.mSubtitleController;
        if (subtitleController != null) {
            subtitleController.reset();
        }
        synchronized (this.mTimeProviderLock) {
            if (this.mTimeProvider != null) {
                this.mTimeProvider.close();
                this.mTimeProvider = null;
            }
        }
        stayAwake(false);
        _reset();
        EventHandler eventHandler = this.mEventHandler;
        if (eventHandler != null) {
            eventHandler.removeCallbacksAndMessages(null);
        }
        synchronized (this.mIndexTrackPairs) {
            this.mIndexTrackPairs.clear();
            this.mInbandTrackIndices.clear();
        }
        resetDrmState();
    }

    public void notifyAt(long mediaTimeUs) {
        _notifyAt(mediaTimeUs);
    }

    public void setAudioStreamType(int streamtype) {
        PlayerBase.deprecateStreamTypeForPlayback(streamtype, TAG, "setAudioStreamType()");
        baseUpdateAudioAttributes(new Builder().setInternalLegacyStreamType(streamtype).build());
        _setAudioStreamType(streamtype);
        this.mStreamType = streamtype;
    }

    public void setAudioAttributes(AudioAttributes attributes) throws IllegalArgumentException {
        if (attributes != null) {
            baseUpdateAudioAttributes(attributes);
            this.mUsage = attributes.getUsage();
            Parcel pattributes = Parcel.obtain();
            attributes.writeToParcel(pattributes, 1);
            setParameter(1400, pattributes);
            pattributes.recycle();
            return;
        }
        String msg = "Cannot set AudioAttributes to null";
        throw new IllegalArgumentException("Cannot set AudioAttributes to null");
    }

    public void setVolume(float leftVolume, float rightVolume) {
        baseSetVolume(leftVolume, rightVolume);
    }

    /* Access modifiers changed, original: 0000 */
    public void playerSetVolume(boolean muting, float leftVolume, float rightVolume) {
        float f = 0.0f;
        float f2 = muting ? 0.0f : leftVolume;
        if (!muting) {
            f = rightVolume;
        }
        _setVolume(f2, f);
    }

    public void setVolume(float volume) {
        setVolume(volume, volume);
    }

    public void setAuxEffectSendLevel(float level) {
        baseSetAuxEffectSendLevel(level);
    }

    /* Access modifiers changed, original: 0000 */
    public int playerSetAuxEffectSendLevel(boolean muting, float level) {
        _setAuxEffectSendLevel(muting ? 0.0f : level);
        return 0;
    }

    public TrackInfo[] getTrackInfo() throws IllegalStateException {
        TrackInfo[] allTrackInfo;
        TrackInfo[] trackInfo = getInbandTrackInfo();
        synchronized (this.mIndexTrackPairs) {
            allTrackInfo = new TrackInfo[this.mIndexTrackPairs.size()];
            for (int i = 0; i < allTrackInfo.length; i++) {
                Pair<Integer, SubtitleTrack> p = (Pair) this.mIndexTrackPairs.get(i);
                if (p.first != null) {
                    allTrackInfo[i] = trackInfo[((Integer) p.first).intValue()];
                } else {
                    SubtitleTrack track = p.second;
                    allTrackInfo[i] = new TrackInfo(track.getTrackType(), track.getFormat());
                }
            }
        }
        return allTrackInfo;
    }

    private TrackInfo[] getInbandTrackInfo() throws IllegalStateException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            request.writeInterfaceToken(IMEDIA_PLAYER);
            request.writeInt(1);
            invoke(request, reply);
            TrackInfo[] trackInfo = (TrackInfo[]) reply.createTypedArray(TrackInfo.CREATOR);
            return trackInfo;
        } finally {
            request.recycle();
            reply.recycle();
        }
    }

    private static boolean availableMimeTypeForExternalSource(String mimeType) {
        if ("application/x-subrip".equals(mimeType)) {
            return true;
        }
        return false;
    }

    @UnsupportedAppUsage
    public void setSubtitleAnchor(SubtitleController controller, Anchor anchor) {
        this.mSubtitleController = controller;
        this.mSubtitleController.setAnchor(anchor);
    }

    private synchronized void setSubtitleAnchor() {
        if (this.mSubtitleController == null && ActivityThread.currentApplication() != null) {
            final TimeProvider timeProvider = (TimeProvider) getMediaTimeProvider();
            final HandlerThread thread = new HandlerThread("SetSubtitleAnchorThread");
            thread.start();
            new Handler(thread.getLooper()).post(new Runnable() {
                public void run() {
                    Context context = ActivityThread.currentApplication();
                    MediaPlayer mediaPlayer = MediaPlayer.this;
                    mediaPlayer.mSubtitleController = new SubtitleController(context, timeProvider, mediaPlayer);
                    MediaPlayer.this.mSubtitleController.setAnchor(new Anchor() {
                        public void setSubtitleWidget(RenderingWidget subtitleWidget) {
                        }

                        public Looper getSubtitleLooper() {
                            return timeProvider.mEventHandler.getLooper();
                        }
                    });
                    thread.getLooper().quitSafely();
                }
            });
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Log.w(TAG, "failed to join SetSubtitleAnchorThread");
            }
        }
        return;
    }

    public void onSubtitleTrackSelected(SubtitleTrack track) {
        int i = this.mSelectedSubtitleTrackIndex;
        if (i >= 0) {
            try {
                selectOrDeselectInbandTrack(i, false);
            } catch (IllegalStateException e) {
            }
            this.mSelectedSubtitleTrackIndex = -1;
        }
        synchronized (this) {
            this.mSubtitleDataListenerDisabled = true;
        }
        if (track != null) {
            synchronized (this.mIndexTrackPairs) {
                Iterator it = this.mIndexTrackPairs.iterator();
                while (it.hasNext()) {
                    Pair<Integer, SubtitleTrack> p = (Pair) it.next();
                    if (p.first != null && p.second == track) {
                        this.mSelectedSubtitleTrackIndex = ((Integer) p.first).intValue();
                        break;
                    }
                }
            }
            int i2 = this.mSelectedSubtitleTrackIndex;
            if (i2 >= 0) {
                try {
                    selectOrDeselectInbandTrack(i2, true);
                } catch (IllegalStateException e2) {
                }
                synchronized (this) {
                    this.mSubtitleDataListenerDisabled = false;
                }
            }
        }
    }

    @UnsupportedAppUsage
    public void addSubtitleSource(InputStream is, MediaFormat format) throws IllegalStateException {
        final InputStream fIs = is;
        final MediaFormat fFormat = format;
        if (is != null) {
            synchronized (this.mOpenSubtitleSources) {
                this.mOpenSubtitleSources.add(is);
            }
        } else {
            Log.w(TAG, "addSubtitleSource called with null InputStream");
        }
        getMediaTimeProvider();
        final HandlerThread thread = new HandlerThread("SubtitleReadThread", 9);
        thread.start();
        new Handler(thread.getLooper()).post(new Runnable() {
            private int addTrack() {
                if (fIs == null || MediaPlayer.this.mSubtitleController == null) {
                    return 901;
                }
                SubtitleTrack track = MediaPlayer.this.mSubtitleController.addTrack(fFormat);
                if (track == null) {
                    return 901;
                }
                Scanner scanner = new Scanner(fIs, "UTF-8");
                String contents = scanner.useDelimiter("\\A").next();
                synchronized (MediaPlayer.this.mOpenSubtitleSources) {
                    MediaPlayer.this.mOpenSubtitleSources.remove(fIs);
                }
                scanner.close();
                synchronized (MediaPlayer.this.mIndexTrackPairs) {
                    MediaPlayer.this.mIndexTrackPairs.add(Pair.create(null, track));
                }
                synchronized (MediaPlayer.this.mTimeProviderLock) {
                    if (MediaPlayer.this.mTimeProvider != null) {
                        Handler h = MediaPlayer.this.mTimeProvider.mEventHandler;
                        h.sendMessage(h.obtainMessage(1, 4, null, Pair.create(track, contents.getBytes())));
                    }
                }
                return 803;
            }

            public void run() {
                int res = addTrack();
                if (MediaPlayer.this.mEventHandler != null) {
                    MediaPlayer.this.mEventHandler.sendMessage(MediaPlayer.this.mEventHandler.obtainMessage(200, res, 0, null));
                }
                thread.getLooper().quitSafely();
            }
        });
    }

    private void scanInternalSubtitleTracks() {
        setSubtitleAnchor();
        populateInbandTracks();
        SubtitleController subtitleController = this.mSubtitleController;
        if (subtitleController != null) {
            subtitleController.selectDefaultTrack();
        }
    }

    private void populateInbandTracks() {
        TrackInfo[] tracks = getInbandTrackInfo();
        synchronized (this.mIndexTrackPairs) {
            int i = 0;
            while (i < tracks.length) {
                if (!this.mInbandTrackIndices.get(i)) {
                    this.mInbandTrackIndices.set(i);
                    if (tracks[i] == null) {
                        String str = TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("unexpected NULL track at index ");
                        stringBuilder.append(i);
                        Log.w(str, stringBuilder.toString());
                    }
                    if (tracks[i] == null || tracks[i].getTrackType() != 4) {
                        this.mIndexTrackPairs.add(Pair.create(Integer.valueOf(i), null));
                    } else {
                        this.mIndexTrackPairs.add(Pair.create(Integer.valueOf(i), this.mSubtitleController.addTrack(tracks[i].getFormat())));
                    }
                }
                i++;
            }
        }
    }

    /* JADX WARNING: Missing block: B:11:0x001f, code skipped:
            $closeResource(r2, r1);
     */
    public void addTimedTextSource(java.lang.String r5, java.lang.String r6) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException {
        /*
        r4 = this;
        r0 = availableMimeTypeForExternalSource(r6);
        if (r0 == 0) goto L_0x0023;
    L_0x0006:
        r0 = new java.io.File;
        r0.<init>(r5);
        r1 = new java.io.FileInputStream;
        r1.<init>(r0);
        r2 = 0;
        r3 = r1.getFD();	 Catch:{ all -> 0x001c }
        r4.addTimedTextSource(r3, r6);	 Catch:{ all -> 0x001c }
        $closeResource(r2, r1);
        return;
    L_0x001c:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x001e }
    L_0x001e:
        r3 = move-exception;
        $closeResource(r2, r1);
        throw r3;
    L_0x0023:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Illegal mimeType for timed text source: ";
        r0.append(r1);
        r0.append(r6);
        r0 = r0.toString();
        r1 = new java.lang.IllegalArgumentException;
        r1.<init>(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaPlayer.addTimedTextSource(java.lang.String, java.lang.String):void");
    }

    /* JADX WARNING: Missing block: B:21:0x0038, code skipped:
            if (r1 == null) goto L_0x0042;
     */
    /* JADX WARNING: Missing block: B:22:0x003a, code skipped:
            r1.close();
     */
    /* JADX WARNING: Missing block: B:24:0x003f, code skipped:
            if (r1 == null) goto L_0x0042;
     */
    /* JADX WARNING: Missing block: B:25:0x0042, code skipped:
            return;
     */
    public void addTimedTextSource(android.content.Context r5, android.net.Uri r6, java.lang.String r7) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException {
        /*
        r4 = this;
        r0 = r6.getScheme();
        if (r0 == 0) goto L_0x0043;
    L_0x0006:
        r1 = "file";
        r1 = r0.equals(r1);
        if (r1 == 0) goto L_0x000f;
    L_0x000e:
        goto L_0x0043;
    L_0x000f:
        r1 = 0;
        r2 = r5.getContentResolver();	 Catch:{ SecurityException -> 0x003e, IOException -> 0x0037, all -> 0x0030 }
        r3 = "r";
        r3 = r2.openAssetFileDescriptor(r6, r3);	 Catch:{ SecurityException -> 0x003e, IOException -> 0x0037, all -> 0x0030 }
        r1 = r3;
        if (r1 != 0) goto L_0x0024;
    L_0x001e:
        if (r1 == 0) goto L_0x0023;
    L_0x0020:
        r1.close();
    L_0x0023:
        return;
    L_0x0024:
        r3 = r1.getFileDescriptor();	 Catch:{ SecurityException -> 0x003e, IOException -> 0x0037, all -> 0x0030 }
        r4.addTimedTextSource(r3, r7);	 Catch:{ SecurityException -> 0x003e, IOException -> 0x0037, all -> 0x0030 }
        r1.close();
        return;
    L_0x0030:
        r2 = move-exception;
        if (r1 == 0) goto L_0x0036;
    L_0x0033:
        r1.close();
    L_0x0036:
        throw r2;
    L_0x0037:
        r2 = move-exception;
        if (r1 == 0) goto L_0x0042;
    L_0x003a:
        r1.close();
        goto L_0x0042;
    L_0x003e:
        r2 = move-exception;
        if (r1 == 0) goto L_0x0042;
    L_0x0041:
        goto L_0x003a;
    L_0x0042:
        return;
    L_0x0043:
        r1 = r6.getPath();
        r4.addTimedTextSource(r1, r7);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaPlayer.addTimedTextSource(android.content.Context, android.net.Uri, java.lang.String):void");
    }

    public void addTimedTextSource(FileDescriptor fd, String mimeType) throws IllegalArgumentException, IllegalStateException {
        addTimedTextSource(fd, 0, 576460752303423487L, mimeType);
    }

    public void addTimedTextSource(FileDescriptor fd, long offset, long length, String mime) throws IllegalArgumentException, IllegalStateException {
        String str = mime;
        if (availableMimeTypeForExternalSource(mime)) {
            try {
                final FileDescriptor dupedFd = Os.dup(fd);
                MediaFormat fFormat = new MediaFormat();
                fFormat.setString(MediaFormat.KEY_MIME, str);
                fFormat.setInteger(MediaFormat.KEY_IS_TIMED_TEXT, 1);
                if (this.mSubtitleController == null) {
                    setSubtitleAnchor();
                }
                if (!this.mSubtitleController.hasRendererFor(fFormat)) {
                    this.mSubtitleController.registerRenderer(new SRTRenderer(ActivityThread.currentApplication(), this.mEventHandler));
                }
                SubtitleTrack track = this.mSubtitleController.addTrack(fFormat);
                synchronized (this.mIndexTrackPairs) {
                    this.mIndexTrackPairs.add(Pair.create(null, track));
                }
                getMediaTimeProvider();
                final long offset2 = offset;
                final long length2 = length;
                final HandlerThread thread = new HandlerThread("TimedTextReadThread", 9);
                thread.start();
                final SubtitleTrack subtitleTrack = track;
                new Handler(thread.getLooper()).post(new Runnable() {
                    private int addTrack() {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        try {
                            Os.lseek(dupedFd, offset2, OsConstants.SEEK_SET);
                            byte[] buffer = new byte[4096];
                            long total = 0;
                            while (total < length2) {
                                int bytes = IoBridge.read(dupedFd, buffer, 0, (int) Math.min((long) buffer.length, length2 - total));
                                if (bytes < 0) {
                                    break;
                                }
                                bos.write(buffer, 0, bytes);
                                total += (long) bytes;
                            }
                            synchronized (MediaPlayer.this.mTimeProviderLock) {
                                if (MediaPlayer.this.mTimeProvider != null) {
                                    Handler h = MediaPlayer.this.mTimeProvider.mEventHandler;
                                    h.sendMessage(h.obtainMessage(1, 4, 0, Pair.create(subtitleTrack, bos.toByteArray())));
                                }
                            }
                            try {
                                Os.close(dupedFd);
                            } catch (ErrnoException e) {
                                Log.e(MediaPlayer.TAG, e.getMessage(), e);
                            }
                            return 803;
                        } catch (Exception e2) {
                            int e3;
                            try {
                                Log.e(MediaPlayer.TAG, e2.getMessage(), e2);
                                e3 = 900;
                                return e3;
                            } finally {
                                try {
                                    e3 = dupedFd;
                                    Os.close(e3);
                                } catch (ErrnoException e4) {
                                    e3 = e4;
                                    Log.e(MediaPlayer.TAG, e3.getMessage(), e3);
                                }
                            }
                        }
                    }

                    public void run() {
                        int res = addTrack();
                        if (MediaPlayer.this.mEventHandler != null) {
                            MediaPlayer.this.mEventHandler.sendMessage(MediaPlayer.this.mEventHandler.obtainMessage(200, res, 0, null));
                        }
                        thread.getLooper().quitSafely();
                    }
                });
                return;
            } catch (ErrnoException ex) {
                ErrnoException ex2 = ex2;
                Log.e(TAG, ex2.getMessage(), ex2);
                throw new RuntimeException(ex2);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Illegal mimeType for timed text source: ");
        stringBuilder.append(str);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public int getSelectedTrack(int trackType) throws IllegalStateException {
        int i;
        int i2 = 0;
        if (this.mSubtitleController != null && (trackType == 4 || trackType == 3)) {
            SubtitleTrack subtitleTrack = this.mSubtitleController.getSelectedTrack();
            if (subtitleTrack != null) {
                synchronized (this.mIndexTrackPairs) {
                    for (i = 0; i < this.mIndexTrackPairs.size(); i++) {
                        if (((Pair) this.mIndexTrackPairs.get(i)).second == subtitleTrack && subtitleTrack.getTrackType() == trackType) {
                            return i;
                        }
                    }
                }
            }
        }
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            request.writeInterfaceToken(IMEDIA_PLAYER);
            request.writeInt(7);
            request.writeInt(trackType);
            invoke(request, reply);
            i = reply.readInt();
            synchronized (this.mIndexTrackPairs) {
                while (i2 < this.mIndexTrackPairs.size()) {
                    Pair<Integer, SubtitleTrack> p = (Pair) this.mIndexTrackPairs.get(i2);
                    if (p.first == null || ((Integer) p.first).intValue() != i) {
                        i2++;
                    } else {
                        request.recycle();
                        reply.recycle();
                        return i2;
                    }
                }
                request.recycle();
                reply.recycle();
                return -1;
            }
        } catch (Throwable th) {
            request.recycle();
            reply.recycle();
        }
    }

    public void selectTrack(int index) throws IllegalStateException {
        selectOrDeselectTrack(index, true);
    }

    public void deselectTrack(int index) throws IllegalStateException {
        selectOrDeselectTrack(index, false);
    }

    private void selectOrDeselectTrack(int index, boolean select) throws IllegalStateException {
        populateInbandTracks();
        try {
            Pair<Integer, SubtitleTrack> p = (Pair) this.mIndexTrackPairs.get(index);
            SubtitleTrack track = p.second;
            if (track == null) {
                selectOrDeselectInbandTrack(((Integer) p.first).intValue(), select);
                return;
            }
            SubtitleController subtitleController = this.mSubtitleController;
            if (subtitleController != null) {
                if (select) {
                    if (track.getTrackType() == 3) {
                        int ttIndex = getSelectedTrack(3);
                        synchronized (this.mIndexTrackPairs) {
                            if (ttIndex >= 0) {
                                if (ttIndex < this.mIndexTrackPairs.size()) {
                                    Pair<Integer, SubtitleTrack> p2 = (Pair) this.mIndexTrackPairs.get(ttIndex);
                                    if (p2.first != null && p2.second == null) {
                                        selectOrDeselectInbandTrack(((Integer) p2.first).intValue(), false);
                                    }
                                }
                            }
                        }
                    }
                    this.mSubtitleController.selectTrack(track);
                    return;
                }
                if (subtitleController.getSelectedTrack() == track) {
                    this.mSubtitleController.selectTrack(null);
                } else {
                    Log.w(TAG, "trying to deselect track that was not selected");
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    private void selectOrDeselectInbandTrack(int index, boolean select) throws IllegalStateException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            request.writeInterfaceToken(IMEDIA_PLAYER);
            request.writeInt(select ? 4 : 5);
            request.writeInt(index);
            invoke(request, reply);
        } finally {
            request.recycle();
            reply.recycle();
        }
    }

    @UnsupportedAppUsage
    public void setRetransmitEndpoint(InetSocketAddress endpoint) throws IllegalStateException, IllegalArgumentException {
        String addrString = null;
        int port = 0;
        if (endpoint != null) {
            addrString = endpoint.getAddress().getHostAddress();
            port = endpoint.getPort();
        }
        int ret = native_setRetransmitEndpoint(addrString, port);
        if (ret != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Illegal re-transmit endpoint; native ret ");
            stringBuilder.append(ret);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() {
        baseRelease();
        native_finalize();
    }

    @UnsupportedAppUsage
    public MediaTimeProvider getMediaTimeProvider() {
        TimeProvider timeProvider;
        synchronized (this.mTimeProviderLock) {
            if (this.mTimeProvider == null) {
                this.mTimeProvider = new TimeProvider(this);
            }
            timeProvider = this.mTimeProvider;
        }
        return timeProvider;
    }

    private static void postEventFromNative(Object mediaplayer_ref, int what, int arg1, int arg2, Object obj) {
        MediaPlayer mp = (MediaPlayer) ((WeakReference) mediaplayer_ref).get();
        if (mp != null) {
            if (what == 1) {
                synchronized (mp.mDrmLock) {
                    mp.mDrmInfoResolved = true;
                }
            } else if (what != 200) {
                if (what == 210) {
                    Log.v(TAG, "postEventFromNative MEDIA_DRM_INFO");
                    if (obj instanceof Parcel) {
                        DrmInfo drmInfo = new DrmInfo((Parcel) obj, null);
                        synchronized (mp.mDrmLock) {
                            mp.mDrmInfo = drmInfo;
                        }
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("MEDIA_DRM_INFO msg.obj of unexpected type ");
                        stringBuilder.append(obj);
                        Log.w(TAG, stringBuilder.toString());
                    }
                }
            } else if (arg1 == 2) {
                new Thread(new Runnable() {
                    public void run() {
                        MediaPlayer.this.start();
                    }
                }).start();
                Thread.yield();
            }
            Message m = mp.mEventHandler;
            if (m != null) {
                mp.mEventHandler.sendMessage(m.obtainMessage(what, arg1, arg2, obj));
            }
        }
    }

    public void setOnPreparedListener(OnPreparedListener listener) {
        this.mOnPreparedListener = listener;
    }

    public void setOnCompletionListener(OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }

    public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
        this.mOnBufferingUpdateListener = listener;
    }

    public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
        this.mOnSeekCompleteListener = listener;
    }

    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {
        this.mOnVideoSizeChangedListener = listener;
    }

    public void setOnTimedTextListener(OnTimedTextListener listener) {
        this.mOnTimedTextListener = listener;
    }

    public void setOnSubtitleDataListener(OnSubtitleDataListener listener, Handler handler) {
        if (listener == null) {
            throw new IllegalArgumentException("Illegal null listener");
        } else if (handler != null) {
            setOnSubtitleDataListenerInt(listener, handler);
        } else {
            throw new IllegalArgumentException("Illegal null handler");
        }
    }

    public void setOnSubtitleDataListener(OnSubtitleDataListener listener) {
        if (listener != null) {
            setOnSubtitleDataListenerInt(listener, null);
            return;
        }
        throw new IllegalArgumentException("Illegal null listener");
    }

    public void clearOnSubtitleDataListener() {
        setOnSubtitleDataListenerInt(null, null);
    }

    private void setOnSubtitleDataListenerInt(OnSubtitleDataListener listener, Handler handler) {
        synchronized (this) {
            this.mExtSubtitleDataListener = listener;
            this.mExtSubtitleDataHandler = handler;
        }
    }

    public void setOnMediaTimeDiscontinuityListener(OnMediaTimeDiscontinuityListener listener, Handler handler) {
        if (listener == null) {
            throw new IllegalArgumentException("Illegal null listener");
        } else if (handler != null) {
            setOnMediaTimeDiscontinuityListenerInt(listener, handler);
        } else {
            throw new IllegalArgumentException("Illegal null handler");
        }
    }

    public void setOnMediaTimeDiscontinuityListener(OnMediaTimeDiscontinuityListener listener) {
        if (listener != null) {
            setOnMediaTimeDiscontinuityListenerInt(listener, null);
            return;
        }
        throw new IllegalArgumentException("Illegal null listener");
    }

    public void clearOnMediaTimeDiscontinuityListener() {
        setOnMediaTimeDiscontinuityListenerInt(null, null);
    }

    private void setOnMediaTimeDiscontinuityListenerInt(OnMediaTimeDiscontinuityListener listener, Handler handler) {
        synchronized (this) {
            this.mOnMediaTimeDiscontinuityListener = listener;
            this.mOnMediaTimeDiscontinuityHandler = handler;
        }
    }

    public void setOnTimedMetaDataAvailableListener(OnTimedMetaDataAvailableListener listener) {
        this.mOnTimedMetaDataAvailableListener = listener;
    }

    public void setOnErrorListener(OnErrorListener listener) {
        this.mOnErrorListener = listener;
    }

    public void setOnInfoListener(OnInfoListener listener) {
        this.mOnInfoListener = listener;
    }

    public void setOnDrmConfigHelper(OnDrmConfigHelper listener) {
        synchronized (this.mDrmLock) {
            this.mOnDrmConfigHelper = listener;
        }
    }

    public void setOnDrmInfoListener(OnDrmInfoListener listener) {
        setOnDrmInfoListener(listener, null);
    }

    public void setOnDrmInfoListener(OnDrmInfoListener listener, Handler handler) {
        synchronized (this.mDrmLock) {
            if (listener != null) {
                this.mOnDrmInfoHandlerDelegate = new OnDrmInfoHandlerDelegate(this, listener, handler);
            } else {
                this.mOnDrmInfoHandlerDelegate = null;
            }
        }
    }

    public void setOnDrmPreparedListener(OnDrmPreparedListener listener) {
        setOnDrmPreparedListener(listener, null);
    }

    public void setOnDrmPreparedListener(OnDrmPreparedListener listener, Handler handler) {
        synchronized (this.mDrmLock) {
            if (listener != null) {
                this.mOnDrmPreparedHandlerDelegate = new OnDrmPreparedHandlerDelegate(this, listener, handler);
            } else {
                this.mOnDrmPreparedHandlerDelegate = null;
            }
        }
    }

    public DrmInfo getDrmInfo() {
        DrmInfo drmInfo = null;
        synchronized (this.mDrmLock) {
            if (!this.mDrmInfoResolved) {
                if (this.mDrmInfo == null) {
                    String msg = "The Player has not been prepared yet";
                    Log.v(TAG, "The Player has not been prepared yet");
                    throw new IllegalStateException("The Player has not been prepared yet");
                }
            }
            if (this.mDrmInfo != null) {
                drmInfo = this.mDrmInfo.makeCopy();
            }
        }
        return drmInfo;
    }

    /* JADX WARNING: Missing block: B:33:0x0062, code skipped:
            if (null != null) goto L_0x0064;
     */
    public void prepareDrm(java.util.UUID r10) throws android.media.UnsupportedSchemeException, android.media.ResourceBusyException, android.media.MediaPlayer.ProvisioningNetworkErrorException, android.media.MediaPlayer.ProvisioningServerErrorException {
        /*
        r9 = this;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "prepareDrm: uuid: ";
        r0.append(r1);
        r0.append(r10);
        r1 = " mOnDrmConfigHelper: ";
        r0.append(r1);
        r1 = r9.mOnDrmConfigHelper;
        r0.append(r1);
        r0 = r0.toString();
        r1 = "MediaPlayer";
        android.util.Log.v(r1, r0);
        r0 = 0;
        r1 = 0;
        r2 = r9.mDrmLock;
        monitor-enter(r2);
        r3 = r9.mDrmInfo;	 Catch:{ all -> 0x0165 }
        if (r3 == 0) goto L_0x0151;
    L_0x002a:
        r3 = r9.mActiveDrmScheme;	 Catch:{ all -> 0x0165 }
        if (r3 != 0) goto L_0x0132;
    L_0x002e:
        r3 = r9.mPrepareDrmInProgress;	 Catch:{ all -> 0x0165 }
        if (r3 != 0) goto L_0x011e;
    L_0x0032:
        r3 = r9.mDrmProvisioningInProgress;	 Catch:{ all -> 0x0165 }
        if (r3 != 0) goto L_0x010a;
    L_0x0036:
        r9.cleanDrmObj();	 Catch:{ all -> 0x0165 }
        r3 = 1;
        r9.mPrepareDrmInProgress = r3;	 Catch:{ all -> 0x0165 }
        r4 = r9.mOnDrmPreparedHandlerDelegate;	 Catch:{ all -> 0x0165 }
        r1 = r4;
        r4 = 0;
        r9.prepareDrm_createDrmStep(r10);	 Catch:{ Exception -> 0x00fd }
        r9.mDrmConfigAllowed = r3;	 Catch:{ all -> 0x0165 }
        monitor-exit(r2);	 Catch:{ all -> 0x0165 }
        r2 = r9.mOnDrmConfigHelper;
        if (r2 == 0) goto L_0x004e;
    L_0x004b:
        r2.onDrmConfig(r9);
    L_0x004e:
        r5 = r9.mDrmLock;
        monitor-enter(r5);
        r9.mDrmConfigAllowed = r4;	 Catch:{ all -> 0x00fa }
        r2 = 0;
        r9.prepareDrm_openSessionStep(r10);	 Catch:{ IllegalStateException -> 0x00d7, NotProvisionedException -> 0x0086, Exception -> 0x006b }
        r9.mDrmUUID = r10;	 Catch:{ IllegalStateException -> 0x00d7, NotProvisionedException -> 0x0086, Exception -> 0x006b }
        r9.mActiveDrmScheme = r3;	 Catch:{ IllegalStateException -> 0x00d7, NotProvisionedException -> 0x0086, Exception -> 0x006b }
        r0 = 1;
        r3 = r9.mDrmProvisioningInProgress;	 Catch:{ all -> 0x00fa }
        if (r3 != 0) goto L_0x0062;
    L_0x0060:
        r9.mPrepareDrmInProgress = r4;	 Catch:{ all -> 0x00fa }
    L_0x0062:
        if (r2 == 0) goto L_0x00ce;
    L_0x0064:
        r9.cleanDrmObj();	 Catch:{ all -> 0x00fa }
        goto L_0x00ce;
    L_0x0068:
        r3 = move-exception;
        goto L_0x00ed;
    L_0x006b:
        r3 = move-exception;
        r6 = "MediaPlayer";
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0068 }
        r7.<init>();	 Catch:{ all -> 0x0068 }
        r8 = "prepareDrm: Exception ";
        r7.append(r8);	 Catch:{ all -> 0x0068 }
        r7.append(r3);	 Catch:{ all -> 0x0068 }
        r7 = r7.toString();	 Catch:{ all -> 0x0068 }
        android.util.Log.e(r6, r7);	 Catch:{ all -> 0x0068 }
        r2 = 1;
        throw r3;	 Catch:{ all -> 0x0068 }
    L_0x0086:
        r6 = move-exception;
        r7 = "MediaPlayer";
        r8 = "prepareDrm: NotProvisionedException";
        android.util.Log.w(r7, r8);	 Catch:{ all -> 0x0068 }
        r7 = r9.HandleProvisioninig(r10);	 Catch:{ all -> 0x0068 }
        if (r7 == 0) goto L_0x00c5;
    L_0x0095:
        r2 = 1;
        if (r7 == r3) goto L_0x00b7;
    L_0x0098:
        r3 = 2;
        if (r7 == r3) goto L_0x00a9;
    L_0x009b:
        r3 = "prepareDrm: Post-provisioning preparation failed.";
        r8 = "MediaPlayer";
        android.util.Log.e(r8, r3);	 Catch:{ all -> 0x0068 }
        r8 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0068 }
        r8.<init>(r3);	 Catch:{ all -> 0x0068 }
        throw r8;	 Catch:{ all -> 0x0068 }
    L_0x00a9:
        r3 = "prepareDrm: Provisioning was required but the request was denied by the server.";
        r8 = "MediaPlayer";
        android.util.Log.e(r8, r3);	 Catch:{ all -> 0x0068 }
        r8 = new android.media.MediaPlayer$ProvisioningServerErrorException;	 Catch:{ all -> 0x0068 }
        r8.<init>(r3);	 Catch:{ all -> 0x0068 }
        throw r8;	 Catch:{ all -> 0x0068 }
    L_0x00b7:
        r3 = "prepareDrm: Provisioning was required but failed due to a network error.";
        r8 = "MediaPlayer";
        android.util.Log.e(r8, r3);	 Catch:{ all -> 0x0068 }
        r8 = new android.media.MediaPlayer$ProvisioningNetworkErrorException;	 Catch:{ all -> 0x0068 }
        r8.<init>(r3);	 Catch:{ all -> 0x0068 }
        throw r8;	 Catch:{ all -> 0x0068 }
    L_0x00c5:
        r3 = r9.mDrmProvisioningInProgress;	 Catch:{ all -> 0x00fa }
        if (r3 != 0) goto L_0x00cb;
    L_0x00c9:
        r9.mPrepareDrmInProgress = r4;	 Catch:{ all -> 0x00fa }
    L_0x00cb:
        if (r2 == 0) goto L_0x00ce;
    L_0x00cd:
        goto L_0x0064;
    L_0x00ce:
        monitor-exit(r5);	 Catch:{ all -> 0x00fa }
        if (r0 == 0) goto L_0x00d6;
    L_0x00d1:
        if (r1 == 0) goto L_0x00d6;
    L_0x00d3:
        r1.notifyClient(r4);
    L_0x00d6:
        return;
    L_0x00d7:
        r3 = move-exception;
        r6 = "prepareDrm(): Wrong usage: The player must be in the prepared state to call prepareDrm().";
        r7 = "MediaPlayer";
        r8 = "prepareDrm(): Wrong usage: The player must be in the prepared state to call prepareDrm().";
        android.util.Log.e(r7, r8);	 Catch:{ all -> 0x0068 }
        r2 = 1;
        r7 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0068 }
        r8 = "prepareDrm(): Wrong usage: The player must be in the prepared state to call prepareDrm().";
        r7.<init>(r8);	 Catch:{ all -> 0x0068 }
        throw r7;	 Catch:{ all -> 0x0068 }
    L_0x00ed:
        r6 = r9.mDrmProvisioningInProgress;	 Catch:{ all -> 0x00fa }
        if (r6 != 0) goto L_0x00f3;
    L_0x00f1:
        r9.mPrepareDrmInProgress = r4;	 Catch:{ all -> 0x00fa }
    L_0x00f3:
        if (r2 == 0) goto L_0x00f8;
    L_0x00f5:
        r9.cleanDrmObj();	 Catch:{ all -> 0x00fa }
        throw r3;	 Catch:{ all -> 0x00fa }
    L_0x00fa:
        r2 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x00fa }
        throw r2;
    L_0x00fd:
        r3 = move-exception;
        r5 = "MediaPlayer";
        r6 = "prepareDrm(): Exception ";
        android.util.Log.w(r5, r6, r3);	 Catch:{ all -> 0x0165 }
        r9.mPrepareDrmInProgress = r4;	 Catch:{ all -> 0x0165 }
        throw r3;	 Catch:{ all -> 0x0165 }
    L_0x010a:
        r3 = "prepareDrm(): Unexpectd: Provisioning is already in progress.";
        r4 = "MediaPlayer";
        r5 = "prepareDrm(): Unexpectd: Provisioning is already in progress.";
        android.util.Log.e(r4, r5);	 Catch:{ all -> 0x0165 }
        r4 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0165 }
        r5 = "prepareDrm(): Unexpectd: Provisioning is already in progress.";
        r4.<init>(r5);	 Catch:{ all -> 0x0165 }
        throw r4;	 Catch:{ all -> 0x0165 }
    L_0x011e:
        r3 = "prepareDrm(): Wrong usage: There is already a pending prepareDrm call.";
        r4 = "MediaPlayer";
        r5 = "prepareDrm(): Wrong usage: There is already a pending prepareDrm call.";
        android.util.Log.e(r4, r5);	 Catch:{ all -> 0x0165 }
        r4 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0165 }
        r5 = "prepareDrm(): Wrong usage: There is already a pending prepareDrm call.";
        r4.<init>(r5);	 Catch:{ all -> 0x0165 }
        throw r4;	 Catch:{ all -> 0x0165 }
    L_0x0132:
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0165 }
        r3.<init>();	 Catch:{ all -> 0x0165 }
        r4 = "prepareDrm(): Wrong usage: There is already an active DRM scheme with ";
        r3.append(r4);	 Catch:{ all -> 0x0165 }
        r4 = r9.mDrmUUID;	 Catch:{ all -> 0x0165 }
        r3.append(r4);	 Catch:{ all -> 0x0165 }
        r3 = r3.toString();	 Catch:{ all -> 0x0165 }
        r4 = "MediaPlayer";
        android.util.Log.e(r4, r3);	 Catch:{ all -> 0x0165 }
        r4 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0165 }
        r4.<init>(r3);	 Catch:{ all -> 0x0165 }
        throw r4;	 Catch:{ all -> 0x0165 }
    L_0x0151:
        r3 = "prepareDrm(): Wrong usage: The player must be prepared and DRM info be retrieved before this call.";
        r4 = "MediaPlayer";
        r5 = "prepareDrm(): Wrong usage: The player must be prepared and DRM info be retrieved before this call.";
        android.util.Log.e(r4, r5);	 Catch:{ all -> 0x0165 }
        r4 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0165 }
        r5 = "prepareDrm(): Wrong usage: The player must be prepared and DRM info be retrieved before this call.";
        r4.<init>(r5);	 Catch:{ all -> 0x0165 }
        throw r4;	 Catch:{ all -> 0x0165 }
    L_0x0165:
        r3 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0165 }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaPlayer.prepareDrm(java.util.UUID):void");
    }

    public void releaseDrm() throws NoDrmSchemeException {
        Log.v(TAG, "releaseDrm:");
        synchronized (this.mDrmLock) {
            if (this.mActiveDrmScheme) {
                try {
                    _releaseDrm();
                    cleanDrmObj();
                    this.mActiveDrmScheme = false;
                } catch (IllegalStateException e) {
                    Log.w(TAG, "releaseDrm: Exception ", e);
                    throw new IllegalStateException("releaseDrm: The player is not in a valid state.");
                } catch (Exception e2) {
                    Log.e(TAG, "releaseDrm: Exception ", e2);
                }
            } else {
                Log.e(TAG, "releaseDrm(): No active DRM scheme to release.");
                throw new NoDrmSchemeException("releaseDrm: No active DRM scheme to release.");
            }
        }
    }

    public KeyRequest getKeyRequest(byte[] keySetId, byte[] initData, String mimeType, int keyType, Map<String, String> optionalParameters) throws NoDrmSchemeException {
        String str;
        NotProvisionedException e;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getKeyRequest:  keySetId: ");
        stringBuilder.append(keySetId);
        stringBuilder.append(" initData:");
        stringBuilder.append(initData);
        stringBuilder.append(" mimeType: ");
        stringBuilder.append(mimeType);
        stringBuilder.append(" keyType: ");
        stringBuilder.append(keyType);
        stringBuilder.append(" optionalParameters: ");
        stringBuilder.append(optionalParameters);
        Log.v(TAG, stringBuilder.toString());
        synchronized (this.mDrmLock) {
            if (this.mActiveDrmScheme) {
                byte[] scope;
                HashMap<String, String> hmapOptionalParameters;
                if (keyType != 3) {
                    try {
                        scope = this.mDrmSessionId;
                    } catch (NotProvisionedException e2) {
                        Log.w(TAG, "getKeyRequest NotProvisionedException: Unexpected. Shouldn't have reached here.");
                        throw new IllegalStateException("getKeyRequest: Unexpected provisioning error.");
                    } catch (Exception e3) {
                        str = TAG;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("getKeyRequest Exception ");
                        stringBuilder2.append(e3);
                        Log.w(str, stringBuilder2.toString());
                        throw e3;
                    }
                }
                scope = keySetId;
                if (optionalParameters != null) {
                    hmapOptionalParameters = new HashMap(optionalParameters);
                } else {
                    hmapOptionalParameters = null;
                }
                e3 = this.mDrmObj.getKeyRequest(scope, initData, mimeType, keyType, hmapOptionalParameters);
                str = TAG;
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("getKeyRequest:   --> request: ");
                stringBuilder3.append(e3);
                Log.v(str, stringBuilder3.toString());
            } else {
                Log.e(TAG, "getKeyRequest NoDrmSchemeException");
                throw new NoDrmSchemeException("getKeyRequest: Has to set a DRM scheme first.");
            }
        }
        return e3;
    }

    public byte[] provideKeyResponse(byte[] keySetId, byte[] response) throws NoDrmSchemeException, DeniedByServerException {
        NotProvisionedException e;
        byte[] keySetResult;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("provideKeyResponse: keySetId: ");
        stringBuilder.append(keySetId);
        stringBuilder.append(" response: ");
        stringBuilder.append(response);
        Log.v(TAG, stringBuilder.toString());
        synchronized (this.mDrmLock) {
            if (this.mActiveDrmScheme) {
                if (keySetId == null) {
                    try {
                        e = this.mDrmSessionId;
                    } catch (NotProvisionedException e2) {
                        Log.w(TAG, "provideKeyResponse NotProvisionedException: Unexpected. Shouldn't have reached here.");
                        throw new IllegalStateException("provideKeyResponse: Unexpected provisioning error.");
                    } catch (Exception e3) {
                        String str = TAG;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("provideKeyResponse Exception ");
                        stringBuilder2.append(e3);
                        Log.w(str, stringBuilder2.toString());
                        throw e3;
                    }
                }
                e3 = keySetId;
                keySetResult = this.mDrmObj.provideKeyResponse(e3, response);
                String str2 = TAG;
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("provideKeyResponse: keySetId: ");
                stringBuilder3.append(keySetId);
                stringBuilder3.append(" response: ");
                stringBuilder3.append(response);
                stringBuilder3.append(" --> ");
                stringBuilder3.append(keySetResult);
                Log.v(str2, stringBuilder3.toString());
            } else {
                Log.e(TAG, "getKeyRequest NoDrmSchemeException");
                throw new NoDrmSchemeException("getKeyRequest: Has to set a DRM scheme first.");
            }
        }
        return keySetResult;
    }

    public void restoreKeys(byte[] keySetId) throws NoDrmSchemeException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("restoreKeys: keySetId: ");
        stringBuilder.append(keySetId);
        Log.v(TAG, stringBuilder.toString());
        synchronized (this.mDrmLock) {
            if (this.mActiveDrmScheme) {
                try {
                    this.mDrmObj.restoreKeys(this.mDrmSessionId, keySetId);
                } catch (Exception e) {
                    String str = TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("restoreKeys Exception ");
                    stringBuilder2.append(e);
                    Log.w(str, stringBuilder2.toString());
                    throw e;
                }
            }
            Log.w(TAG, "restoreKeys NoDrmSchemeException");
            throw new NoDrmSchemeException("restoreKeys: Has to set a DRM scheme first.");
        }
    }

    public String getDrmPropertyString(String propertyName) throws NoDrmSchemeException {
        String value;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getDrmPropertyString: propertyName: ");
        stringBuilder.append(propertyName);
        Log.v(TAG, stringBuilder.toString());
        synchronized (this.mDrmLock) {
            if (!this.mActiveDrmScheme) {
                if (!this.mDrmConfigAllowed) {
                    Log.w(TAG, "getDrmPropertyString NoDrmSchemeException");
                    throw new NoDrmSchemeException("getDrmPropertyString: Has to prepareDrm() first.");
                }
            }
            try {
                value = this.mDrmObj.getPropertyString(propertyName);
            } catch (Exception e) {
                String str = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("getDrmPropertyString Exception ");
                stringBuilder2.append(e);
                Log.w(str, stringBuilder2.toString());
                throw e;
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("getDrmPropertyString: propertyName: ");
        stringBuilder.append(propertyName);
        stringBuilder.append(" --> value: ");
        stringBuilder.append(value);
        Log.v(TAG, stringBuilder.toString());
        return value;
    }

    public void setDrmPropertyString(String propertyName, String value) throws NoDrmSchemeException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setDrmPropertyString: propertyName: ");
        stringBuilder.append(propertyName);
        stringBuilder.append(" value: ");
        stringBuilder.append(value);
        Log.v(TAG, stringBuilder.toString());
        synchronized (this.mDrmLock) {
            if (!this.mActiveDrmScheme) {
                if (!this.mDrmConfigAllowed) {
                    Log.w(TAG, "setDrmPropertyString NoDrmSchemeException");
                    throw new NoDrmSchemeException("setDrmPropertyString: Has to prepareDrm() first.");
                }
            }
            try {
                this.mDrmObj.setPropertyString(propertyName, value);
            } catch (Exception e) {
                String str = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("setDrmPropertyString Exception ");
                stringBuilder2.append(e);
                Log.w(str, stringBuilder2.toString());
                throw e;
            }
        }
    }

    private void prepareDrm_createDrmStep(UUID uuid) throws UnsupportedSchemeException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("prepareDrm_createDrmStep: UUID: ");
        stringBuilder.append(uuid);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.v(str, stringBuilder2);
        try {
            this.mDrmObj = new MediaDrm(uuid);
            stringBuilder = new StringBuilder();
            stringBuilder.append("prepareDrm_createDrmStep: Created mDrmObj=");
            stringBuilder.append(this.mDrmObj);
            Log.v(str, stringBuilder.toString());
        } catch (Exception e) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("prepareDrm_createDrmStep: MediaDrm failed with ");
            stringBuilder3.append(e);
            Log.e(str, stringBuilder3.toString());
            throw e;
        }
    }

    private void prepareDrm_openSessionStep(UUID uuid) throws NotProvisionedException, ResourceBusyException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("prepareDrm_openSessionStep: uuid: ");
        stringBuilder.append(uuid);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.v(str, stringBuilder2);
        try {
            this.mDrmSessionId = this.mDrmObj.openSession();
            stringBuilder = new StringBuilder();
            stringBuilder.append("prepareDrm_openSessionStep: mDrmSessionId=");
            stringBuilder.append(this.mDrmSessionId);
            Log.v(str, stringBuilder.toString());
            _prepareDrm(getByteArrayFromUUID(uuid), this.mDrmSessionId);
            Log.v(str, "prepareDrm_openSessionStep: _prepareDrm/Crypto succeeded");
        } catch (Exception e) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("prepareDrm_openSessionStep: open/crypto failed with ");
            stringBuilder3.append(e);
            Log.e(str, stringBuilder3.toString());
            throw e;
        }
    }

    private int HandleProvisioninig(UUID uuid) {
        boolean z = this.mDrmProvisioningInProgress;
        String str = TAG;
        if (z) {
            Log.e(str, "HandleProvisioninig: Unexpected mDrmProvisioningInProgress");
            return 3;
        }
        ProvisionRequest provReq = this.mDrmObj.getProvisionRequest();
        if (provReq == null) {
            Log.e(str, "HandleProvisioninig: getProvisionRequest returned null.");
            return 3;
        }
        int result;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HandleProvisioninig provReq  data: ");
        stringBuilder.append(provReq.getData());
        stringBuilder.append(" url: ");
        stringBuilder.append(provReq.getDefaultUrl());
        Log.v(str, stringBuilder.toString());
        this.mDrmProvisioningInProgress = true;
        this.mDrmProvisioningThread = new ProvisioningThread(this, null).initialize(provReq, uuid, this);
        this.mDrmProvisioningThread.start();
        if (this.mOnDrmPreparedHandlerDelegate != null) {
            result = 0;
        } else {
            try {
                this.mDrmProvisioningThread.join();
            } catch (Exception e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("HandleProvisioninig: Thread.join Exception ");
                stringBuilder2.append(e);
                Log.w(str, stringBuilder2.toString());
            }
            result = this.mDrmProvisioningThread.status();
            this.mDrmProvisioningThread = null;
        }
        return result;
    }

    private boolean resumePrepareDrm(UUID uuid) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("resumePrepareDrm: uuid: ");
        stringBuilder.append(uuid);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.v(str, stringBuilder2);
        try {
            prepareDrm_openSessionStep(uuid);
            this.mDrmUUID = uuid;
            this.mActiveDrmScheme = true;
            return true;
        } catch (Exception e) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("HandleProvisioninig: Thread run _prepareDrm resume failed with ");
            stringBuilder3.append(e);
            Log.w(str, stringBuilder3.toString());
            return false;
        }
    }

    private void resetDrmState() {
        synchronized (this.mDrmLock) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("resetDrmState:  mDrmInfo=");
            stringBuilder.append(this.mDrmInfo);
            stringBuilder.append(" mDrmProvisioningThread=");
            stringBuilder.append(this.mDrmProvisioningThread);
            stringBuilder.append(" mPrepareDrmInProgress=");
            stringBuilder.append(this.mPrepareDrmInProgress);
            stringBuilder.append(" mActiveDrmScheme=");
            stringBuilder.append(this.mActiveDrmScheme);
            Log.v(str, stringBuilder.toString());
            this.mDrmInfoResolved = false;
            this.mDrmInfo = null;
            if (this.mDrmProvisioningThread != null) {
                try {
                    this.mDrmProvisioningThread.join();
                } catch (InterruptedException e) {
                    String str2 = TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("resetDrmState: ProvThread.join Exception ");
                    stringBuilder2.append(e);
                    Log.w(str2, stringBuilder2.toString());
                }
                this.mDrmProvisioningThread = null;
            }
            this.mPrepareDrmInProgress = false;
            this.mActiveDrmScheme = false;
            cleanDrmObj();
        }
    }

    private void cleanDrmObj() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("cleanDrmObj: mDrmObj=");
        stringBuilder.append(this.mDrmObj);
        stringBuilder.append(" mDrmSessionId=");
        stringBuilder.append(this.mDrmSessionId);
        Log.v(TAG, stringBuilder.toString());
        byte[] bArr = this.mDrmSessionId;
        if (bArr != null) {
            this.mDrmObj.closeSession(bArr);
            this.mDrmSessionId = null;
        }
        MediaDrm mediaDrm = this.mDrmObj;
        if (mediaDrm != null) {
            mediaDrm.release();
            this.mDrmObj = null;
        }
    }

    private static final byte[] getByteArrayFromUUID(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] uuidBytes = new byte[16];
        for (int i = 0; i < 8; i++) {
            uuidBytes[i] = (byte) ((int) (msb >>> ((7 - i) * 8)));
            uuidBytes[i + 8] = (byte) ((int) (lsb >>> ((7 - i) * 8)));
        }
        return uuidBytes;
    }

    private boolean isVideoScalingModeSupported(int mode) {
        return mode == 1 || mode == 2;
    }
}
