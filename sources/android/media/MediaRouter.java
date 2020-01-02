package android.media;

import android.Manifest.permission;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.hardware.display.WifiDisplay;
import android.hardware.display.WifiDisplayStatus;
import android.media.AudioAttributes.Builder;
import android.media.IRemoteVolumeObserver.Stub;
import android.media.session.MediaSession;
import android.os.Handler;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import com.android.internal.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class MediaRouter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE = 1;
    public static final int CALLBACK_FLAG_PASSIVE_DISCOVERY = 8;
    public static final int CALLBACK_FLAG_PERFORM_ACTIVE_SCAN = 1;
    public static final int CALLBACK_FLAG_REQUEST_DISCOVERY = 4;
    public static final int CALLBACK_FLAG_UNFILTERED_EVENTS = 2;
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    public static final String MIRRORING_GROUP_ID = "android.media.mirroring_group";
    static final int ROUTE_TYPE_ANY = 8388615;
    public static final int ROUTE_TYPE_LIVE_AUDIO = 1;
    public static final int ROUTE_TYPE_LIVE_VIDEO = 2;
    public static final int ROUTE_TYPE_REMOTE_DISPLAY = 4;
    public static final int ROUTE_TYPE_USER = 8388608;
    private static final String TAG = "MediaRouter";
    static final HashMap<Context, MediaRouter> sRouters = new HashMap();
    static Static sStatic;

    public static abstract class Callback {
        public abstract void onRouteAdded(MediaRouter mediaRouter, RouteInfo routeInfo);

        public abstract void onRouteChanged(MediaRouter mediaRouter, RouteInfo routeInfo);

        public abstract void onRouteGrouped(MediaRouter mediaRouter, RouteInfo routeInfo, RouteGroup routeGroup, int i);

        public abstract void onRouteRemoved(MediaRouter mediaRouter, RouteInfo routeInfo);

        public abstract void onRouteSelected(MediaRouter mediaRouter, int i, RouteInfo routeInfo);

        public abstract void onRouteUngrouped(MediaRouter mediaRouter, RouteInfo routeInfo, RouteGroup routeGroup);

        public abstract void onRouteUnselected(MediaRouter mediaRouter, int i, RouteInfo routeInfo);

        public abstract void onRouteVolumeChanged(MediaRouter mediaRouter, RouteInfo routeInfo);

        public void onRoutePresentationDisplayChanged(MediaRouter router, RouteInfo info) {
        }
    }

    static class CallbackInfo {
        public final Callback cb;
        public int flags;
        public final MediaRouter router;
        public int type;

        public CallbackInfo(Callback cb, int type, int flags, MediaRouter router) {
            this.cb = cb;
            this.type = type;
            this.flags = flags;
            this.router = router;
        }

        public boolean filterRouteEvent(RouteInfo route) {
            return filterRouteEvent(route.mSupportedTypes);
        }

        public boolean filterRouteEvent(int supportedTypes) {
            return ((this.flags & 2) == 0 && (this.type & supportedTypes) == 0) ? false : true;
        }
    }

    public static class RouteCategory {
        final boolean mGroupable;
        boolean mIsSystem;
        CharSequence mName;
        int mNameResId;
        int mTypes;

        RouteCategory(CharSequence name, int types, boolean groupable) {
            this.mName = name;
            this.mTypes = types;
            this.mGroupable = groupable;
        }

        RouteCategory(int nameResId, int types, boolean groupable) {
            this.mNameResId = nameResId;
            this.mTypes = types;
            this.mGroupable = groupable;
        }

        public CharSequence getName() {
            return getName(MediaRouter.sStatic.mResources);
        }

        public CharSequence getName(Context context) {
            return getName(context.getResources());
        }

        /* Access modifiers changed, original: 0000 */
        public CharSequence getName(Resources res) {
            int i = this.mNameResId;
            if (i != 0) {
                return res.getText(i);
            }
            return this.mName;
        }

        public List<RouteInfo> getRoutes(List<RouteInfo> out) {
            if (out == null) {
                out = new ArrayList();
            } else {
                out.clear();
            }
            int count = MediaRouter.getRouteCountStatic();
            for (int i = 0; i < count; i++) {
                RouteInfo route = MediaRouter.getRouteAtStatic(i);
                if (route.mCategory == this) {
                    out.add(route);
                }
            }
            return out;
        }

        public int getSupportedTypes() {
            return this.mTypes;
        }

        public boolean isGroupable() {
            return this.mGroupable;
        }

        public boolean isSystem() {
            return this.mIsSystem;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("RouteCategory{ name=");
            stringBuilder.append(getName());
            stringBuilder.append(" types=");
            stringBuilder.append(MediaRouter.typesToString(this.mTypes));
            stringBuilder.append(" groupable=");
            stringBuilder.append(this.mGroupable);
            stringBuilder.append(" }");
            return stringBuilder.toString();
        }
    }

    public static class RouteInfo {
        public static final int DEVICE_TYPE_BLUETOOTH = 3;
        public static final int DEVICE_TYPE_SPEAKER = 2;
        public static final int DEVICE_TYPE_TV = 1;
        public static final int DEVICE_TYPE_UNKNOWN = 0;
        public static final int PLAYBACK_TYPE_LOCAL = 0;
        public static final int PLAYBACK_TYPE_REMOTE = 1;
        public static final int PLAYBACK_VOLUME_FIXED = 0;
        public static final int PLAYBACK_VOLUME_VARIABLE = 1;
        public static final int STATUS_AVAILABLE = 3;
        public static final int STATUS_CONNECTED = 6;
        @UnsupportedAppUsage
        public static final int STATUS_CONNECTING = 2;
        public static final int STATUS_IN_USE = 5;
        public static final int STATUS_NONE = 0;
        public static final int STATUS_NOT_AVAILABLE = 4;
        public static final int STATUS_SCANNING = 1;
        final RouteCategory mCategory;
        CharSequence mDescription;
        String mDeviceAddress;
        int mDeviceType;
        boolean mEnabled = true;
        String mGlobalRouteId;
        RouteGroup mGroup;
        Drawable mIcon;
        CharSequence mName;
        @UnsupportedAppUsage
        int mNameResId;
        int mPlaybackStream = 3;
        int mPlaybackType = 0;
        Display mPresentationDisplay;
        int mPresentationDisplayId = -1;
        private int mRealStatusCode;
        final Stub mRemoteVolObserver = new Stub() {
            public void dispatchRemoteVolumeUpdate(final int direction, final int value) {
                MediaRouter.sStatic.mHandler.post(new Runnable() {
                    public void run() {
                        if (RouteInfo.this.mVcb == null) {
                            return;
                        }
                        if (direction != 0) {
                            RouteInfo.this.mVcb.vcb.onVolumeUpdateRequest(RouteInfo.this.mVcb.route, direction);
                        } else {
                            RouteInfo.this.mVcb.vcb.onVolumeSetRequest(RouteInfo.this.mVcb.route, value);
                        }
                    }
                });
            }
        };
        private int mResolvedStatusCode;
        private CharSequence mStatus;
        int mSupportedTypes;
        private Object mTag;
        VolumeCallbackInfo mVcb;
        int mVolume = 15;
        int mVolumeHandling = 1;
        int mVolumeMax = 15;

        @Retention(RetentionPolicy.SOURCE)
        public @interface DeviceType {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface PlaybackType {
        }

        @Retention(RetentionPolicy.SOURCE)
        private @interface PlaybackVolume {
        }

        RouteInfo(RouteCategory category) {
            this.mCategory = category;
            this.mDeviceType = 0;
        }

        public CharSequence getName() {
            return getName(MediaRouter.sStatic.mResources);
        }

        public CharSequence getName(Context context) {
            return getName(context.getResources());
        }

        /* Access modifiers changed, original: 0000 */
        @UnsupportedAppUsage
        public CharSequence getName(Resources res) {
            int i = this.mNameResId;
            if (i != 0) {
                return res.getText(i);
            }
            return this.mName;
        }

        public CharSequence getDescription() {
            return this.mDescription;
        }

        public CharSequence getStatus() {
            return this.mStatus;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean setRealStatusCode(int statusCode) {
            if (this.mRealStatusCode == statusCode) {
                return false;
            }
            this.mRealStatusCode = statusCode;
            return resolveStatusCode();
        }

        /* Access modifiers changed, original: 0000 */
        public boolean resolveStatusCode() {
            int statusCode = this.mRealStatusCode;
            if (isSelected() && (statusCode == 1 || statusCode == 3)) {
                statusCode = 2;
            }
            if (this.mResolvedStatusCode == statusCode) {
                return false;
            }
            int resId;
            this.mResolvedStatusCode = statusCode;
            if (statusCode == 1) {
                resId = R.string.media_route_status_scanning;
            } else if (statusCode == 2) {
                resId = R.string.media_route_status_connecting;
            } else if (statusCode == 3) {
                resId = R.string.media_route_status_available;
            } else if (statusCode == 4) {
                resId = R.string.media_route_status_not_available;
            } else if (statusCode != 5) {
                resId = 0;
            } else {
                resId = R.string.media_route_status_in_use;
            }
            this.mStatus = resId != 0 ? MediaRouter.sStatic.mResources.getText(resId) : null;
            return true;
        }

        @UnsupportedAppUsage
        public int getStatusCode() {
            return this.mResolvedStatusCode;
        }

        public int getSupportedTypes() {
            return this.mSupportedTypes;
        }

        public int getDeviceType() {
            return this.mDeviceType;
        }

        @UnsupportedAppUsage
        public boolean matchesTypes(int types) {
            return (this.mSupportedTypes & types) != 0;
        }

        public RouteGroup getGroup() {
            return this.mGroup;
        }

        public RouteCategory getCategory() {
            return this.mCategory;
        }

        public Drawable getIconDrawable() {
            return this.mIcon;
        }

        public void setTag(Object tag) {
            this.mTag = tag;
            routeUpdated();
        }

        public Object getTag() {
            return this.mTag;
        }

        public int getPlaybackType() {
            return this.mPlaybackType;
        }

        public int getPlaybackStream() {
            return this.mPlaybackStream;
        }

        public int getVolume() {
            if (this.mPlaybackType != 0) {
                return this.mVolume;
            }
            int vol = 0;
            try {
                vol = MediaRouter.sStatic.mAudioService.getStreamVolume(this.mPlaybackStream);
            } catch (RemoteException e) {
                Log.e(MediaRouter.TAG, "Error getting local stream volume", e);
            }
            return vol;
        }

        public void requestSetVolume(int volume) {
            if (this.mPlaybackType == 0) {
                try {
                    MediaRouter.sStatic.mAudioService.setStreamVolume(this.mPlaybackStream, volume, 0, ActivityThread.currentPackageName());
                    return;
                } catch (RemoteException e) {
                    Log.e(MediaRouter.TAG, "Error setting local stream volume", e);
                    return;
                }
            }
            MediaRouter.sStatic.requestSetVolume(this, volume);
        }

        public void requestUpdateVolume(int direction) {
            if (this.mPlaybackType == 0) {
                try {
                    MediaRouter.sStatic.mAudioService.setStreamVolume(this.mPlaybackStream, Math.max(0, Math.min(getVolume() + direction, getVolumeMax())), 0, ActivityThread.currentPackageName());
                    return;
                } catch (RemoteException e) {
                    Log.e(MediaRouter.TAG, "Error setting local stream volume", e);
                    return;
                }
            }
            MediaRouter.sStatic.requestUpdateVolume(this, direction);
        }

        public int getVolumeMax() {
            if (this.mPlaybackType != 0) {
                return this.mVolumeMax;
            }
            int volMax = 0;
            try {
                volMax = MediaRouter.sStatic.mAudioService.getStreamMaxVolume(this.mPlaybackStream);
            } catch (RemoteException e) {
                Log.e(MediaRouter.TAG, "Error getting local stream volume", e);
            }
            return volMax;
        }

        public int getVolumeHandling() {
            return this.mVolumeHandling;
        }

        public Display getPresentationDisplay() {
            return this.mPresentationDisplay;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean updatePresentationDisplay() {
            Display display = choosePresentationDisplay();
            if (this.mPresentationDisplay == display) {
                return false;
            }
            this.mPresentationDisplay = display;
            return true;
        }

        private Display choosePresentationDisplay() {
            if ((this.mSupportedTypes & 2) != 0) {
                Display[] displays = MediaRouter.sStatic.getAllPresentationDisplays();
                int i = 0;
                int length;
                Display display;
                if (this.mPresentationDisplayId >= 0) {
                    length = displays.length;
                    while (i < length) {
                        display = displays[i];
                        if (display.getDisplayId() == this.mPresentationDisplayId) {
                            return display;
                        }
                        i++;
                    }
                    return null;
                } else if (this.mDeviceAddress != null) {
                    length = displays.length;
                    while (i < length) {
                        display = displays[i];
                        if (display.getType() == 3 && this.mDeviceAddress.equals(display.getAddress())) {
                            return display;
                        }
                        i++;
                    }
                    return null;
                } else if (this == MediaRouter.sStatic.mDefaultAudioVideo && displays.length > 0) {
                    return displays[0];
                }
            }
            return null;
        }

        @UnsupportedAppUsage
        public String getDeviceAddress() {
            return this.mDeviceAddress;
        }

        public boolean isEnabled() {
            return this.mEnabled;
        }

        public boolean isConnecting() {
            return this.mResolvedStatusCode == 2;
        }

        @UnsupportedAppUsage
        public boolean isSelected() {
            return this == MediaRouter.sStatic.mSelectedRoute;
        }

        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        public boolean isDefault() {
            return this == MediaRouter.sStatic.mDefaultAudioVideo;
        }

        public boolean isBluetooth() {
            return this == MediaRouter.sStatic.mBluetoothA2dpRoute;
        }

        @UnsupportedAppUsage
        public void select() {
            MediaRouter.selectRouteStatic(this.mSupportedTypes, this, true);
        }

        /* Access modifiers changed, original: 0000 */
        public void setStatusInt(CharSequence status) {
            if (!status.equals(this.mStatus)) {
                this.mStatus = status;
                RouteGroup routeGroup = this.mGroup;
                if (routeGroup != null) {
                    routeGroup.memberStatusChanged(this, status);
                }
                routeUpdated();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void routeUpdated() {
            MediaRouter.updateRoute(this);
        }

        public String toString() {
            String supportedTypes = MediaRouter.typesToString(getSupportedTypes());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getClass().getSimpleName());
            stringBuilder.append("{ name=");
            stringBuilder.append(getName());
            stringBuilder.append(", description=");
            stringBuilder.append(getDescription());
            stringBuilder.append(", status=");
            stringBuilder.append(getStatus());
            stringBuilder.append(", category=");
            stringBuilder.append(getCategory());
            stringBuilder.append(", supportedTypes=");
            stringBuilder.append(supportedTypes);
            stringBuilder.append(", presentationDisplay=");
            stringBuilder.append(this.mPresentationDisplay);
            stringBuilder.append(" }");
            return stringBuilder.toString();
        }
    }

    public static class RouteGroup extends RouteInfo {
        final ArrayList<RouteInfo> mRoutes;
        private boolean mUpdateName;

        RouteGroup(RouteCategory category) {
            super(category);
            this.mRoutes = new ArrayList();
            this.mGroup = this;
            this.mVolumeHandling = 0;
        }

        /* Access modifiers changed, original: 0000 */
        public CharSequence getName(Resources res) {
            if (this.mUpdateName) {
                updateName();
            }
            return super.getName(res);
        }

        public void addRoute(RouteInfo route) {
            StringBuilder stringBuilder;
            if (route.getGroup() != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Route ");
                stringBuilder.append(route);
                stringBuilder.append(" is already part of a group.");
                throw new IllegalStateException(stringBuilder.toString());
            } else if (route.getCategory() == this.mCategory) {
                int at = this.mRoutes.size();
                this.mRoutes.add(route);
                route.mGroup = this;
                this.mUpdateName = true;
                updateVolume();
                routeUpdated();
                MediaRouter.dispatchRouteGrouped(route, this, at);
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Route cannot be added to a group with a different category. (Route category=");
                stringBuilder.append(route.getCategory());
                stringBuilder.append(" group category=");
                stringBuilder.append(this.mCategory);
                stringBuilder.append(")");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }

        public void addRoute(RouteInfo route, int insertAt) {
            StringBuilder stringBuilder;
            if (route.getGroup() != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Route ");
                stringBuilder.append(route);
                stringBuilder.append(" is already part of a group.");
                throw new IllegalStateException(stringBuilder.toString());
            } else if (route.getCategory() == this.mCategory) {
                this.mRoutes.add(insertAt, route);
                route.mGroup = this;
                this.mUpdateName = true;
                updateVolume();
                routeUpdated();
                MediaRouter.dispatchRouteGrouped(route, this, insertAt);
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Route cannot be added to a group with a different category. (Route category=");
                stringBuilder.append(route.getCategory());
                stringBuilder.append(" group category=");
                stringBuilder.append(this.mCategory);
                stringBuilder.append(")");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }

        public void removeRoute(RouteInfo route) {
            if (route.getGroup() == this) {
                this.mRoutes.remove(route);
                route.mGroup = null;
                this.mUpdateName = true;
                updateVolume();
                MediaRouter.dispatchRouteUngrouped(route, this);
                routeUpdated();
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Route ");
            stringBuilder.append(route);
            stringBuilder.append(" is not a member of this group.");
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public void removeRoute(int index) {
            RouteInfo route = (RouteInfo) this.mRoutes.remove(index);
            route.mGroup = null;
            this.mUpdateName = true;
            updateVolume();
            MediaRouter.dispatchRouteUngrouped(route, this);
            routeUpdated();
        }

        public int getRouteCount() {
            return this.mRoutes.size();
        }

        public RouteInfo getRouteAt(int index) {
            return (RouteInfo) this.mRoutes.get(index);
        }

        public void setIconDrawable(Drawable icon) {
            this.mIcon = icon;
        }

        public void setIconResource(int resId) {
            setIconDrawable(MediaRouter.sStatic.mResources.getDrawable(resId));
        }

        public void requestSetVolume(int volume) {
            int maxVol = getVolumeMax();
            if (maxVol != 0) {
                float scaledVolume = ((float) volume) / ((float) maxVol);
                int routeCount = getRouteCount();
                for (int i = 0; i < routeCount; i++) {
                    RouteInfo route = getRouteAt(i);
                    route.requestSetVolume((int) (((float) route.getVolumeMax()) * scaledVolume));
                }
                if (volume != this.mVolume) {
                    this.mVolume = volume;
                    MediaRouter.dispatchRouteVolumeChanged(this);
                }
            }
        }

        public void requestUpdateVolume(int direction) {
            if (getVolumeMax() != 0) {
                int routeCount = getRouteCount();
                int volume = 0;
                for (int i = 0; i < routeCount; i++) {
                    RouteInfo route = getRouteAt(i);
                    route.requestUpdateVolume(direction);
                    int routeVol = route.getVolume();
                    if (routeVol > volume) {
                        volume = routeVol;
                    }
                }
                if (volume != this.mVolume) {
                    this.mVolume = volume;
                    MediaRouter.dispatchRouteVolumeChanged(this);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void memberNameChanged(RouteInfo info, CharSequence name) {
            this.mUpdateName = true;
            routeUpdated();
        }

        /* Access modifiers changed, original: 0000 */
        public void memberStatusChanged(RouteInfo info, CharSequence status) {
            setStatusInt(status);
        }

        /* Access modifiers changed, original: 0000 */
        public void memberVolumeChanged(RouteInfo info) {
            updateVolume();
        }

        /* Access modifiers changed, original: 0000 */
        public void updateVolume() {
            int routeCount = getRouteCount();
            int volume = 0;
            for (int i = 0; i < routeCount; i++) {
                int routeVol = getRouteAt(i).getVolume();
                if (routeVol > volume) {
                    volume = routeVol;
                }
            }
            if (volume != this.mVolume) {
                this.mVolume = volume;
                MediaRouter.dispatchRouteVolumeChanged(this);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void routeUpdated() {
            int types = 0;
            int count = this.mRoutes.size();
            if (count == 0) {
                MediaRouter.removeRouteStatic(this);
                return;
            }
            int maxVolume = 0;
            boolean isLocal = true;
            boolean isFixedVolume = true;
            int i = 0;
            while (true) {
                int i2 = 0;
                if (i >= count) {
                    break;
                }
                RouteInfo route = (RouteInfo) this.mRoutes.get(i);
                types |= route.mSupportedTypes;
                int routeMaxVolume = route.getVolumeMax();
                if (routeMaxVolume > maxVolume) {
                    maxVolume = routeMaxVolume;
                }
                isLocal &= route.getPlaybackType() == 0 ? 1 : 0;
                if (route.getVolumeHandling() == 0) {
                    i2 = 1;
                }
                isFixedVolume &= i2;
                i++;
            }
            this.mPlaybackType = isLocal ? 0 : 1;
            this.mVolumeHandling = isFixedVolume ? 0 : 1;
            this.mSupportedTypes = types;
            this.mVolumeMax = maxVolume;
            this.mIcon = count == 1 ? ((RouteInfo) this.mRoutes.get(0)).getIconDrawable() : null;
            super.routeUpdated();
        }

        /* Access modifiers changed, original: 0000 */
        public void updateName() {
            StringBuilder sb = new StringBuilder();
            int count = this.mRoutes.size();
            for (int i = 0; i < count; i++) {
                RouteInfo info = (RouteInfo) this.mRoutes.get(i);
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(info.getName());
            }
            this.mName = sb.toString();
            this.mUpdateName = false;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(super.toString());
            sb.append('[');
            int count = this.mRoutes.size();
            for (int i = 0; i < count; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(this.mRoutes.get(i));
            }
            sb.append(']');
            return sb.toString();
        }
    }

    public static class SimpleCallback extends Callback {
        public void onRouteSelected(MediaRouter router, int type, RouteInfo info) {
        }

        public void onRouteUnselected(MediaRouter router, int type, RouteInfo info) {
        }

        public void onRouteAdded(MediaRouter router, RouteInfo info) {
        }

        public void onRouteRemoved(MediaRouter router, RouteInfo info) {
        }

        public void onRouteChanged(MediaRouter router, RouteInfo info) {
        }

        public void onRouteGrouped(MediaRouter router, RouteInfo info, RouteGroup group, int index) {
        }

        public void onRouteUngrouped(MediaRouter router, RouteInfo info, RouteGroup group) {
        }

        public void onRouteVolumeChanged(MediaRouter router, RouteInfo info) {
        }
    }

    static class Static implements DisplayListener {
        boolean mActivelyScanningWifiDisplays;
        final IAudioRoutesObserver.Stub mAudioRoutesObserver = new IAudioRoutesObserver.Stub() {
            public void dispatchAudioRoutesChanged(final AudioRoutesInfo newRoutes) {
                Static.this.mHandler.post(new Runnable() {
                    public void run() {
                        Static.this.updateAudioRoutes(newRoutes);
                    }
                });
            }
        };
        final IAudioService mAudioService;
        RouteInfo mBluetoothA2dpRoute;
        final CopyOnWriteArrayList<CallbackInfo> mCallbacks = new CopyOnWriteArrayList();
        final boolean mCanConfigureWifiDisplays;
        final ArrayList<RouteCategory> mCategories = new ArrayList();
        IMediaRouterClient mClient;
        MediaRouterClientState mClientState;
        final AudioRoutesInfo mCurAudioRoutesInfo = new AudioRoutesInfo();
        int mCurrentUserId = -1;
        RouteInfo mDefaultAudioVideo;
        boolean mDiscoverRequestActiveScan;
        int mDiscoveryRequestRouteTypes;
        final DisplayManager mDisplayService;
        final Handler mHandler;
        final IMediaRouterService mMediaRouterService;
        final String mPackageName;
        String mPreviousActiveWifiDisplayAddress;
        final Resources mResources;
        final ArrayList<RouteInfo> mRoutes = new ArrayList();
        RouteInfo mSelectedRoute;
        final RouteCategory mSystemCategory;

        final class Client extends IMediaRouterClient.Stub {
            Client() {
            }

            public void onStateChanged() {
                Static.this.mHandler.post(new Runnable() {
                    public void run() {
                        Client client = Client.this;
                        if (client == Static.this.mClient) {
                            Static.this.updateClientState();
                        }
                    }
                });
            }

            public void onRestoreRoute() {
                Static.this.mHandler.post(new Runnable() {
                    public void run() {
                        Client client = Client.this;
                        if (client == Static.this.mClient && Static.this.mSelectedRoute != null && (Static.this.mSelectedRoute == Static.this.mDefaultAudioVideo || Static.this.mSelectedRoute == Static.this.mBluetoothA2dpRoute)) {
                            if (MediaRouter.DEBUG) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("onRestoreRoute() : route=");
                                stringBuilder.append(Static.this.mSelectedRoute);
                                Log.d(MediaRouter.TAG, stringBuilder.toString());
                            }
                            Static.this.mSelectedRoute.select();
                        }
                    }
                });
            }

            public void onSelectedRouteChanged(String routeId) {
                Static.this.mHandler.post(new -$$Lambda$MediaRouter$Static$Client$xd63sG7fVJYYu49J1xqN8JYQvo4(this, routeId));
            }

            public /* synthetic */ void lambda$onSelectedRouteChanged$0$MediaRouter$Static$Client(String routeId) {
                if (this == Static.this.mClient) {
                    Static.this.updateSelectedRouteForId(routeId);
                }
            }
        }

        Static(Context appContext) {
            this.mPackageName = appContext.getPackageName();
            this.mResources = appContext.getResources();
            this.mHandler = new Handler(appContext.getMainLooper());
            this.mAudioService = IAudioService.Stub.asInterface(ServiceManager.getService("audio"));
            this.mDisplayService = (DisplayManager) appContext.getSystemService(Context.DISPLAY_SERVICE);
            this.mMediaRouterService = IMediaRouterService.Stub.asInterface(ServiceManager.getService(Context.MEDIA_ROUTER_SERVICE));
            boolean z = false;
            this.mSystemCategory = new RouteCategory((int) R.string.default_audio_route_category_name, 3, false);
            this.mSystemCategory.mIsSystem = true;
            if (appContext.checkPermission(permission.CONFIGURE_WIFI_DISPLAY, Process.myPid(), Process.myUid()) == 0) {
                z = true;
            }
            this.mCanConfigureWifiDisplays = z;
        }

        /* Access modifiers changed, original: 0000 */
        public void startMonitoringRoutes(Context appContext) {
            this.mDefaultAudioVideo = new RouteInfo(this.mSystemCategory);
            RouteInfo routeInfo = this.mDefaultAudioVideo;
            routeInfo.mNameResId = R.string.default_audio_route_name;
            routeInfo.mSupportedTypes = 3;
            routeInfo.updatePresentationDisplay();
            if (((AudioManager) appContext.getSystemService("audio")).isVolumeFixed()) {
                this.mDefaultAudioVideo.mVolumeHandling = 0;
            }
            MediaRouter.addRouteStatic(this.mDefaultAudioVideo);
            MediaRouter.updateWifiDisplayStatus(this.mDisplayService.getWifiDisplayStatus());
            appContext.registerReceiver(new WifiDisplayStatusChangedReceiver(), new IntentFilter(DisplayManager.ACTION_WIFI_DISPLAY_STATUS_CHANGED));
            appContext.registerReceiver(new VolumeChangeReceiver(), new IntentFilter(AudioManager.VOLUME_CHANGED_ACTION));
            this.mDisplayService.registerDisplayListener(this, this.mHandler);
            AudioRoutesInfo newAudioRoutes = null;
            try {
                newAudioRoutes = this.mAudioService.startWatchingRoutes(this.mAudioRoutesObserver);
            } catch (RemoteException e) {
            }
            if (newAudioRoutes != null) {
                updateAudioRoutes(newAudioRoutes);
            }
            rebindAsUser(UserHandle.myUserId());
            if (this.mSelectedRoute == null) {
                MediaRouter.selectDefaultRouteStatic();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void updateAudioRoutes(AudioRoutesInfo newRoutes) {
            RouteInfo routeInfo;
            RouteInfo routeInfo2;
            boolean audioRoutesChanged = false;
            boolean forceUseDefaultRoute = false;
            if (newRoutes.mainType != this.mCurAudioRoutesInfo.mainType) {
                int name;
                this.mCurAudioRoutesInfo.mainType = newRoutes.mainType;
                if ((newRoutes.mainType & 2) != 0 || (newRoutes.mainType & 1) != 0) {
                    name = R.string.default_audio_route_name_headphones;
                } else if ((newRoutes.mainType & 4) != 0) {
                    name = R.string.default_audio_route_name_dock_speakers;
                } else if ((newRoutes.mainType & 8) != 0) {
                    name = R.string.default_audio_route_name_hdmi;
                } else if ((newRoutes.mainType & 16) != 0) {
                    name = R.string.default_audio_route_name_usb;
                } else {
                    name = R.string.default_audio_route_name;
                }
                routeInfo = this.mDefaultAudioVideo;
                routeInfo.mNameResId = name;
                MediaRouter.dispatchRouteChanged(routeInfo);
                if ((newRoutes.mainType & 19) != 0) {
                    forceUseDefaultRoute = true;
                }
                audioRoutesChanged = true;
            }
            if (!TextUtils.equals(newRoutes.bluetoothName, this.mCurAudioRoutesInfo.bluetoothName)) {
                forceUseDefaultRoute = false;
                this.mCurAudioRoutesInfo.bluetoothName = newRoutes.bluetoothName;
                if (this.mCurAudioRoutesInfo.bluetoothName != null) {
                    routeInfo2 = this.mBluetoothA2dpRoute;
                    if (routeInfo2 == null) {
                        routeInfo2 = new RouteInfo(this.mSystemCategory);
                        routeInfo2.mName = this.mCurAudioRoutesInfo.bluetoothName;
                        routeInfo2.mDescription = this.mResources.getText(R.string.bluetooth_a2dp_audio_route_name);
                        routeInfo2.mSupportedTypes = 1;
                        routeInfo2.mDeviceType = 3;
                        this.mBluetoothA2dpRoute = routeInfo2;
                        MediaRouter.addRouteStatic(this.mBluetoothA2dpRoute);
                    } else {
                        routeInfo2.mName = this.mCurAudioRoutesInfo.bluetoothName;
                        MediaRouter.dispatchRouteChanged(this.mBluetoothA2dpRoute);
                    }
                } else {
                    routeInfo2 = this.mBluetoothA2dpRoute;
                    if (routeInfo2 != null) {
                        MediaRouter.removeRouteStatic(routeInfo2);
                        this.mBluetoothA2dpRoute = null;
                    }
                }
                audioRoutesChanged = true;
            }
            if (audioRoutesChanged) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Audio routes updated: ");
                stringBuilder.append(newRoutes);
                stringBuilder.append(", a2dp=");
                stringBuilder.append(isBluetoothA2dpOn());
                Log.v(MediaRouter.TAG, stringBuilder.toString());
                routeInfo2 = this.mSelectedRoute;
                if (routeInfo2 == null || routeInfo2 == this.mDefaultAudioVideo || routeInfo2 == this.mBluetoothA2dpRoute) {
                    if (!forceUseDefaultRoute) {
                        routeInfo = this.mBluetoothA2dpRoute;
                        if (routeInfo != null) {
                            MediaRouter.selectRouteStatic(1, routeInfo, false);
                            return;
                        }
                    }
                    MediaRouter.selectRouteStatic(1, this.mDefaultAudioVideo, false);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isBluetoothA2dpOn() {
            boolean z = false;
            try {
                if (this.mBluetoothA2dpRoute != null && this.mAudioService.isBluetoothA2dpOn()) {
                    z = true;
                }
                return z;
            } catch (RemoteException e) {
                Log.e(MediaRouter.TAG, "Error querying Bluetooth A2DP state", e);
                return false;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void updateDiscoveryRequest() {
            int routeTypes = 0;
            int passiveRouteTypes = 0;
            boolean activeScan = false;
            boolean activeScanWifiDisplay = false;
            int count = this.mCallbacks.size();
            for (int i = 0; i < count; i++) {
                CallbackInfo cbi = (CallbackInfo) this.mCallbacks.get(i);
                if ((cbi.flags & 5) != 0) {
                    routeTypes |= cbi.type;
                } else if ((cbi.flags & 8) != 0) {
                    passiveRouteTypes |= cbi.type;
                } else {
                    routeTypes |= cbi.type;
                }
                if ((1 & cbi.flags) != 0) {
                    activeScan = true;
                    if ((4 & cbi.type) != 0) {
                        activeScanWifiDisplay = true;
                    }
                }
            }
            if (routeTypes != 0 || activeScan) {
                routeTypes |= passiveRouteTypes;
            }
            if (this.mCanConfigureWifiDisplays) {
                RouteInfo routeInfo = this.mSelectedRoute;
                if (routeInfo != null && routeInfo.matchesTypes(4)) {
                    activeScanWifiDisplay = false;
                }
                if (activeScanWifiDisplay) {
                    if (!this.mActivelyScanningWifiDisplays) {
                        this.mActivelyScanningWifiDisplays = true;
                        this.mDisplayService.startWifiDisplayScan();
                    }
                } else if (this.mActivelyScanningWifiDisplays) {
                    this.mActivelyScanningWifiDisplays = false;
                    this.mDisplayService.stopWifiDisplayScan();
                }
            }
            if (routeTypes != this.mDiscoveryRequestRouteTypes || activeScan != this.mDiscoverRequestActiveScan) {
                this.mDiscoveryRequestRouteTypes = routeTypes;
                this.mDiscoverRequestActiveScan = activeScan;
                publishClientDiscoveryRequest();
            }
        }

        public void onDisplayAdded(int displayId) {
            updatePresentationDisplays(displayId);
        }

        public void onDisplayChanged(int displayId) {
            updatePresentationDisplays(displayId);
        }

        public void onDisplayRemoved(int displayId) {
            updatePresentationDisplays(displayId);
        }

        public void setRouterGroupId(String groupId) {
            IMediaRouterClient iMediaRouterClient = this.mClient;
            if (iMediaRouterClient != null) {
                try {
                    this.mMediaRouterService.registerClientGroupId(iMediaRouterClient, groupId);
                } catch (RemoteException ex) {
                    Log.e(MediaRouter.TAG, "Unable to register group ID of the client.", ex);
                }
            }
        }

        public Display[] getAllPresentationDisplays() {
            return this.mDisplayService.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
        }

        private void updatePresentationDisplays(int changedDisplayId) {
            int count = this.mRoutes.size();
            for (int i = 0; i < count; i++) {
                RouteInfo route = (RouteInfo) this.mRoutes.get(i);
                if (route.updatePresentationDisplay() || (route.mPresentationDisplay != null && route.mPresentationDisplay.getDisplayId() == changedDisplayId)) {
                    MediaRouter.dispatchRoutePresentationDisplayChanged(route);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void updateSelectedRouteForId(String routeId) {
            RouteInfo selectedRoute = isBluetoothA2dpOn() ? this.mBluetoothA2dpRoute : this.mDefaultAudioVideo;
            int count = this.mRoutes.size();
            for (int i = 0; i < count; i++) {
                RouteInfo route = (RouteInfo) this.mRoutes.get(i);
                if (TextUtils.equals(route.mGlobalRouteId, routeId)) {
                    selectedRoute = route;
                }
            }
            if (selectedRoute != this.mSelectedRoute) {
                MediaRouter.selectRouteStatic(selectedRoute.mSupportedTypes, selectedRoute, false);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void setSelectedRoute(RouteInfo info, boolean explicit) {
            this.mSelectedRoute = info;
            publishClientSelectedRoute(explicit);
        }

        /* Access modifiers changed, original: 0000 */
        public void rebindAsUser(int userId) {
            if (this.mCurrentUserId != userId || userId < 0 || this.mClient == null) {
                IMediaRouterClient iMediaRouterClient = this.mClient;
                String str = MediaRouter.TAG;
                if (iMediaRouterClient != null) {
                    try {
                        this.mMediaRouterService.unregisterClient(iMediaRouterClient);
                    } catch (RemoteException ex) {
                        Log.e(str, "Unable to unregister media router client.", ex);
                    }
                    this.mClient = null;
                }
                this.mCurrentUserId = userId;
                try {
                    Client client = new Client();
                    this.mMediaRouterService.registerClientAsUser(client, this.mPackageName, userId);
                    this.mClient = client;
                } catch (RemoteException ex2) {
                    Log.e(str, "Unable to register media router client.", ex2);
                }
                publishClientDiscoveryRequest();
                publishClientSelectedRoute(false);
                updateClientState();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void publishClientDiscoveryRequest() {
            IMediaRouterClient iMediaRouterClient = this.mClient;
            if (iMediaRouterClient != null) {
                try {
                    this.mMediaRouterService.setDiscoveryRequest(iMediaRouterClient, this.mDiscoveryRequestRouteTypes, this.mDiscoverRequestActiveScan);
                } catch (RemoteException ex) {
                    Log.e(MediaRouter.TAG, "Unable to publish media router client discovery request.", ex);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void publishClientSelectedRoute(boolean explicit) {
            IMediaRouterClient iMediaRouterClient = this.mClient;
            if (iMediaRouterClient != null) {
                try {
                    this.mMediaRouterService.setSelectedRoute(iMediaRouterClient, this.mSelectedRoute != null ? this.mSelectedRoute.mGlobalRouteId : null, explicit);
                } catch (RemoteException ex) {
                    Log.e(MediaRouter.TAG, "Unable to publish media router client selected route.", ex);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void updateClientState() {
            int i;
            ArrayList<android.media.MediaRouterClientState.RouteInfo> globalRoutes = null;
            this.mClientState = null;
            IMediaRouterClient iMediaRouterClient = this.mClient;
            if (iMediaRouterClient != null) {
                try {
                    this.mClientState = this.mMediaRouterService.getState(iMediaRouterClient);
                } catch (RemoteException ex) {
                    Log.e(MediaRouter.TAG, "Unable to retrieve media router client state.", ex);
                }
            }
            MediaRouterClientState mediaRouterClientState = this.mClientState;
            if (mediaRouterClientState != null) {
                globalRoutes = mediaRouterClientState.routes;
            }
            int globalRouteCount = globalRoutes != null ? globalRoutes.size() : 0;
            for (i = 0; i < globalRouteCount; i++) {
                android.media.MediaRouterClientState.RouteInfo globalRoute = (android.media.MediaRouterClientState.RouteInfo) globalRoutes.get(i);
                RouteInfo route = findGlobalRoute(globalRoute.id);
                if (route == null) {
                    MediaRouter.addRouteStatic(makeGlobalRoute(globalRoute));
                } else {
                    updateGlobalRoute(route, globalRoute);
                }
            }
            i = this.mRoutes.size();
            while (true) {
                int i2 = i - 1;
                if (i > 0) {
                    RouteInfo route2 = (RouteInfo) this.mRoutes.get(i2);
                    String globalRouteId = route2.mGlobalRouteId;
                    if (globalRouteId != null) {
                        for (int j = 0; j < globalRouteCount; j++) {
                            if (globalRouteId.equals(((android.media.MediaRouterClientState.RouteInfo) globalRoutes.get(j)).id)) {
                                break;
                            }
                        }
                        MediaRouter.removeRouteStatic(route2);
                    }
                    i = i2;
                } else {
                    return;
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void requestSetVolume(RouteInfo route, int volume) {
            if (route.mGlobalRouteId != null) {
                IMediaRouterClient iMediaRouterClient = this.mClient;
                if (iMediaRouterClient != null) {
                    try {
                        this.mMediaRouterService.requestSetVolume(iMediaRouterClient, route.mGlobalRouteId, volume);
                    } catch (RemoteException ex) {
                        Log.w(MediaRouter.TAG, "Unable to request volume change.", ex);
                    }
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void requestUpdateVolume(RouteInfo route, int direction) {
            if (route.mGlobalRouteId != null) {
                IMediaRouterClient iMediaRouterClient = this.mClient;
                if (iMediaRouterClient != null) {
                    try {
                        this.mMediaRouterService.requestUpdateVolume(iMediaRouterClient, route.mGlobalRouteId, direction);
                    } catch (RemoteException ex) {
                        Log.w(MediaRouter.TAG, "Unable to request volume change.", ex);
                    }
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public RouteInfo makeGlobalRoute(android.media.MediaRouterClientState.RouteInfo globalRoute) {
            RouteInfo route = new RouteInfo(this.mSystemCategory);
            route.mGlobalRouteId = globalRoute.id;
            route.mName = globalRoute.name;
            route.mDescription = globalRoute.description;
            route.mSupportedTypes = globalRoute.supportedTypes;
            route.mDeviceType = globalRoute.deviceType;
            route.mEnabled = globalRoute.enabled;
            route.setRealStatusCode(globalRoute.statusCode);
            route.mPlaybackType = globalRoute.playbackType;
            route.mPlaybackStream = globalRoute.playbackStream;
            route.mVolume = globalRoute.volume;
            route.mVolumeMax = globalRoute.volumeMax;
            route.mVolumeHandling = globalRoute.volumeHandling;
            route.mPresentationDisplayId = globalRoute.presentationDisplayId;
            route.updatePresentationDisplay();
            return route;
        }

        /* Access modifiers changed, original: 0000 */
        public void updateGlobalRoute(RouteInfo route, android.media.MediaRouterClientState.RouteInfo globalRoute) {
            boolean changed = false;
            boolean volumeChanged = false;
            boolean presentationDisplayChanged = false;
            if (!Objects.equals(route.mName, globalRoute.name)) {
                route.mName = globalRoute.name;
                changed = true;
            }
            if (!Objects.equals(route.mDescription, globalRoute.description)) {
                route.mDescription = globalRoute.description;
                changed = true;
            }
            int oldSupportedTypes = route.mSupportedTypes;
            if (oldSupportedTypes != globalRoute.supportedTypes) {
                route.mSupportedTypes = globalRoute.supportedTypes;
                changed = true;
            }
            if (route.mEnabled != globalRoute.enabled) {
                route.mEnabled = globalRoute.enabled;
                changed = true;
            }
            if (route.mRealStatusCode != globalRoute.statusCode) {
                route.setRealStatusCode(globalRoute.statusCode);
                changed = true;
            }
            if (route.mPlaybackType != globalRoute.playbackType) {
                route.mPlaybackType = globalRoute.playbackType;
                changed = true;
            }
            if (route.mPlaybackStream != globalRoute.playbackStream) {
                route.mPlaybackStream = globalRoute.playbackStream;
                changed = true;
            }
            if (route.mVolume != globalRoute.volume) {
                route.mVolume = globalRoute.volume;
                changed = true;
                volumeChanged = true;
            }
            if (route.mVolumeMax != globalRoute.volumeMax) {
                route.mVolumeMax = globalRoute.volumeMax;
                changed = true;
                volumeChanged = true;
            }
            if (route.mVolumeHandling != globalRoute.volumeHandling) {
                route.mVolumeHandling = globalRoute.volumeHandling;
                changed = true;
                volumeChanged = true;
            }
            if (route.mPresentationDisplayId != globalRoute.presentationDisplayId) {
                route.mPresentationDisplayId = globalRoute.presentationDisplayId;
                route.updatePresentationDisplay();
                changed = true;
                presentationDisplayChanged = true;
            }
            if (changed) {
                MediaRouter.dispatchRouteChanged(route, oldSupportedTypes);
            }
            if (volumeChanged) {
                MediaRouter.dispatchRouteVolumeChanged(route);
            }
            if (presentationDisplayChanged) {
                MediaRouter.dispatchRoutePresentationDisplayChanged(route);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public RouteInfo findGlobalRoute(String globalRouteId) {
            int count = this.mRoutes.size();
            for (int i = 0; i < count; i++) {
                RouteInfo route = (RouteInfo) this.mRoutes.get(i);
                if (globalRouteId.equals(route.mGlobalRouteId)) {
                    return route;
                }
            }
            return null;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isPlaybackActive() {
            IMediaRouterClient iMediaRouterClient = this.mClient;
            if (iMediaRouterClient != null) {
                try {
                    return this.mMediaRouterService.isPlaybackActive(iMediaRouterClient);
                } catch (RemoteException ex) {
                    Log.e(MediaRouter.TAG, "Unable to retrieve playback active state.", ex);
                }
            }
            return false;
        }
    }

    public static class UserRouteInfo extends RouteInfo {
        RemoteControlClient mRcc;
        SessionVolumeProvider mSvp;

        class SessionVolumeProvider extends VolumeProvider {
            SessionVolumeProvider(int volumeControl, int maxVolume, int currentVolume) {
                super(volumeControl, maxVolume, currentVolume);
            }

            public void onSetVolumeTo(final int volume) {
                MediaRouter.sStatic.mHandler.post(new Runnable() {
                    public void run() {
                        if (UserRouteInfo.this.mVcb != null) {
                            UserRouteInfo.this.mVcb.vcb.onVolumeSetRequest(UserRouteInfo.this.mVcb.route, volume);
                        }
                    }
                });
            }

            public void onAdjustVolume(final int direction) {
                MediaRouter.sStatic.mHandler.post(new Runnable() {
                    public void run() {
                        if (UserRouteInfo.this.mVcb != null) {
                            UserRouteInfo.this.mVcb.vcb.onVolumeUpdateRequest(UserRouteInfo.this.mVcb.route, direction);
                        }
                    }
                });
            }
        }

        UserRouteInfo(RouteCategory category) {
            super(category);
            this.mSupportedTypes = 8388608;
            this.mPlaybackType = 1;
            this.mVolumeHandling = 0;
        }

        public void setName(CharSequence name) {
            this.mNameResId = 0;
            this.mName = name;
            routeUpdated();
        }

        public void setName(int resId) {
            this.mNameResId = resId;
            this.mName = null;
            routeUpdated();
        }

        public void setDescription(CharSequence description) {
            this.mDescription = description;
            routeUpdated();
        }

        public void setStatus(CharSequence status) {
            setStatusInt(status);
        }

        public void setRemoteControlClient(RemoteControlClient rcc) {
            this.mRcc = rcc;
            updatePlaybackInfoOnRcc();
        }

        public RemoteControlClient getRemoteControlClient() {
            return this.mRcc;
        }

        public void setIconDrawable(Drawable icon) {
            this.mIcon = icon;
        }

        public void setIconResource(int resId) {
            setIconDrawable(MediaRouter.sStatic.mResources.getDrawable(resId));
        }

        public void setVolumeCallback(VolumeCallback vcb) {
            this.mVcb = new VolumeCallbackInfo(vcb, this);
        }

        public void setPlaybackType(int type) {
            if (this.mPlaybackType != type) {
                this.mPlaybackType = type;
                configureSessionVolume();
            }
        }

        public void setVolumeHandling(int volumeHandling) {
            if (this.mVolumeHandling != volumeHandling) {
                this.mVolumeHandling = volumeHandling;
                configureSessionVolume();
            }
        }

        public void setVolume(int volume) {
            volume = Math.max(0, Math.min(volume, getVolumeMax()));
            if (this.mVolume != volume) {
                this.mVolume = volume;
                SessionVolumeProvider sessionVolumeProvider = this.mSvp;
                if (sessionVolumeProvider != null) {
                    sessionVolumeProvider.setCurrentVolume(this.mVolume);
                }
                MediaRouter.dispatchRouteVolumeChanged(this);
                if (this.mGroup != null) {
                    this.mGroup.memberVolumeChanged(this);
                }
            }
        }

        public void requestSetVolume(int volume) {
            if (this.mVolumeHandling == 1) {
                if (this.mVcb == null) {
                    Log.e(MediaRouter.TAG, "Cannot requestSetVolume on user route - no volume callback set");
                    return;
                }
                this.mVcb.vcb.onVolumeSetRequest(this, volume);
            }
        }

        public void requestUpdateVolume(int direction) {
            if (this.mVolumeHandling == 1) {
                if (this.mVcb == null) {
                    Log.e(MediaRouter.TAG, "Cannot requestChangeVolume on user route - no volumec callback set");
                    return;
                }
                this.mVcb.vcb.onVolumeUpdateRequest(this, direction);
            }
        }

        public void setVolumeMax(int volumeMax) {
            if (this.mVolumeMax != volumeMax) {
                this.mVolumeMax = volumeMax;
                configureSessionVolume();
            }
        }

        public void setPlaybackStream(int stream) {
            if (this.mPlaybackStream != stream) {
                this.mPlaybackStream = stream;
                configureSessionVolume();
            }
        }

        private void updatePlaybackInfoOnRcc() {
            configureSessionVolume();
        }

        private void configureSessionVolume() {
            MediaSession session = this.mRcc;
            String str = MediaRouter.TAG;
            if (session == null) {
                if (MediaRouter.DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("No Rcc to configure volume for route ");
                    stringBuilder.append(getName());
                    Log.d(str, stringBuilder.toString());
                }
                return;
            }
            session = session.getMediaSession();
            if (session == null) {
                if (MediaRouter.DEBUG) {
                    Log.d(str, "Rcc has no session to configure volume");
                }
                return;
            }
            if (this.mPlaybackType == 1) {
                int volumeControl = 0;
                if (this.mVolumeHandling == 1) {
                    volumeControl = 2;
                }
                SessionVolumeProvider sessionVolumeProvider = this.mSvp;
                if (!(sessionVolumeProvider != null && sessionVolumeProvider.getVolumeControl() == volumeControl && this.mSvp.getMaxVolume() == this.mVolumeMax)) {
                    this.mSvp = new SessionVolumeProvider(volumeControl, this.mVolumeMax, this.mVolume);
                    session.setPlaybackToRemote(this.mSvp);
                }
            } else {
                Builder bob = new Builder();
                bob.setLegacyStreamType(this.mPlaybackStream);
                session.setPlaybackToLocal(bob.build());
                this.mSvp = null;
            }
        }
    }

    public static abstract class VolumeCallback {
        public abstract void onVolumeSetRequest(RouteInfo routeInfo, int i);

        public abstract void onVolumeUpdateRequest(RouteInfo routeInfo, int i);
    }

    static class VolumeCallbackInfo {
        public final RouteInfo route;
        public final VolumeCallback vcb;

        public VolumeCallbackInfo(VolumeCallback vcb, RouteInfo route) {
            this.vcb = vcb;
            this.route = route;
        }
    }

    static class VolumeChangeReceiver extends BroadcastReceiver {
        VolumeChangeReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AudioManager.VOLUME_CHANGED_ACTION) && intent.getIntExtra(AudioManager.EXTRA_VOLUME_STREAM_TYPE, -1) == 3) {
                int newVolume = intent.getIntExtra(AudioManager.EXTRA_VOLUME_STREAM_VALUE, 0);
                if (newVolume != intent.getIntExtra(AudioManager.EXTRA_PREV_VOLUME_STREAM_VALUE, 0)) {
                    MediaRouter.systemVolumeChanged(newVolume);
                }
            }
        }
    }

    static class WifiDisplayStatusChangedReceiver extends BroadcastReceiver {
        WifiDisplayStatusChangedReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DisplayManager.ACTION_WIFI_DISPLAY_STATUS_CHANGED)) {
                MediaRouter.updateWifiDisplayStatus((WifiDisplayStatus) intent.getParcelableExtra(DisplayManager.EXTRA_WIFI_DISPLAY_STATUS));
            }
        }
    }

    static String typesToString(int types) {
        StringBuilder result = new StringBuilder();
        if ((types & 1) != 0) {
            result.append("ROUTE_TYPE_LIVE_AUDIO ");
        }
        if ((types & 2) != 0) {
            result.append("ROUTE_TYPE_LIVE_VIDEO ");
        }
        if ((types & 4) != 0) {
            result.append("ROUTE_TYPE_REMOTE_DISPLAY ");
        }
        if ((8388608 & types) != 0) {
            result.append("ROUTE_TYPE_USER ");
        }
        return result.toString();
    }

    public MediaRouter(Context context) {
        synchronized (Static.class) {
            if (sStatic == null) {
                Context appContext = context.getApplicationContext();
                sStatic = new Static(appContext);
                sStatic.startMonitoringRoutes(appContext);
            }
        }
    }

    public RouteInfo getDefaultRoute() {
        return sStatic.mDefaultAudioVideo;
    }

    public RouteInfo getFallbackRoute() {
        return sStatic.mBluetoothA2dpRoute != null ? sStatic.mBluetoothA2dpRoute : sStatic.mDefaultAudioVideo;
    }

    public RouteCategory getSystemCategory() {
        return sStatic.mSystemCategory;
    }

    @UnsupportedAppUsage
    public RouteInfo getSelectedRoute() {
        return getSelectedRoute(8388615);
    }

    public RouteInfo getSelectedRoute(int type) {
        if (sStatic.mSelectedRoute != null && (sStatic.mSelectedRoute.mSupportedTypes & type) != 0) {
            return sStatic.mSelectedRoute;
        }
        if (type == 8388608) {
            return null;
        }
        return sStatic.mDefaultAudioVideo;
    }

    public boolean isRouteAvailable(int types, int flags) {
        int count = sStatic.mRoutes.size();
        for (int i = 0; i < count; i++) {
            RouteInfo route = (RouteInfo) sStatic.mRoutes.get(i);
            if (route.matchesTypes(types) && ((flags & 1) == 0 || route != sStatic.mDefaultAudioVideo)) {
                return true;
            }
        }
        return false;
    }

    public void setRouterGroupId(String groupId) {
        sStatic.setRouterGroupId(groupId);
    }

    public void addCallback(int types, Callback cb) {
        addCallback(types, cb, 0);
    }

    public void addCallback(int types, Callback cb, int flags) {
        int index = findCallbackInfo(cb);
        if (index >= 0) {
            CallbackInfo info = (CallbackInfo) sStatic.mCallbacks.get(index);
            info.type |= types;
            info.flags |= flags;
        } else {
            sStatic.mCallbacks.add(new CallbackInfo(cb, types, flags, this));
        }
        sStatic.updateDiscoveryRequest();
    }

    public void removeCallback(Callback cb) {
        int index = findCallbackInfo(cb);
        if (index >= 0) {
            sStatic.mCallbacks.remove(index);
            sStatic.updateDiscoveryRequest();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("removeCallback(");
        stringBuilder.append(cb);
        stringBuilder.append("): callback not registered");
        Log.w(TAG, stringBuilder.toString());
    }

    private int findCallbackInfo(Callback cb) {
        int count = sStatic.mCallbacks.size();
        for (int i = 0; i < count; i++) {
            if (((CallbackInfo) sStatic.mCallbacks.get(i)).cb == cb) {
                return i;
            }
        }
        return -1;
    }

    public void selectRoute(int types, RouteInfo route) {
        if (route != null) {
            selectRouteStatic(types, route, true);
            return;
        }
        throw new IllegalArgumentException("Route cannot be null.");
    }

    @UnsupportedAppUsage
    public void selectRouteInt(int types, RouteInfo route, boolean explicit) {
        selectRouteStatic(types, route, explicit);
    }

    static void selectRouteStatic(int types, RouteInfo route, boolean explicit) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Selecting route: ");
        stringBuilder.append(route);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.v(str, stringBuilder2);
        RouteInfo oldRoute = sStatic.mSelectedRoute;
        RouteInfo currentSystemRoute = sStatic.isBluetoothA2dpOn() ? sStatic.mBluetoothA2dpRoute : sStatic.mDefaultAudioVideo;
        boolean newRouteHasAddress = false;
        boolean wasDefaultOrBluetoothRoute = oldRoute == sStatic.mDefaultAudioVideo || oldRoute == sStatic.mBluetoothA2dpRoute;
        if (oldRoute == route && (!wasDefaultOrBluetoothRoute || route == currentSystemRoute)) {
            return;
        }
        if (route.matchesTypes(types)) {
            RouteInfo btRoute = sStatic.mBluetoothA2dpRoute;
            if (sStatic.isPlaybackActive() && btRoute != null && (types & 1) != 0 && (route == btRoute || route == sStatic.mDefaultAudioVideo)) {
                try {
                    sStatic.mAudioService.setBluetoothA2dpOn(route == btRoute);
                } catch (RemoteException e) {
                    Log.e(str, "Error changing Bluetooth A2DP state", e);
                }
            }
            WifiDisplay activeDisplay = sStatic.mDisplayService.getWifiDisplayStatus().getActiveDisplay();
            boolean oldRouteHasAddress = (oldRoute == null || oldRoute.mDeviceAddress == null) ? false : true;
            if (route.mDeviceAddress != null) {
                newRouteHasAddress = true;
            }
            if (activeDisplay != null || oldRouteHasAddress || newRouteHasAddress) {
                if (!newRouteHasAddress || matchesDeviceAddress(activeDisplay, route)) {
                    if (!(activeDisplay == null || newRouteHasAddress)) {
                        sStatic.mDisplayService.disconnectWifiDisplay();
                    }
                } else if (sStatic.mCanConfigureWifiDisplays) {
                    sStatic.mDisplayService.connectWifiDisplay(route.mDeviceAddress);
                } else {
                    Log.e(str, "Cannot connect to wifi displays because this process is not allowed to do so.");
                }
            }
            sStatic.setSelectedRoute(route, explicit);
            if (oldRoute != null) {
                dispatchRouteUnselected(oldRoute.getSupportedTypes() & types, oldRoute);
                if (oldRoute.resolveStatusCode()) {
                    dispatchRouteChanged(oldRoute);
                }
            }
            if (route.resolveStatusCode()) {
                dispatchRouteChanged(route);
            }
            dispatchRouteSelected(route.getSupportedTypes() & types, route);
            sStatic.updateDiscoveryRequest();
            return;
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("selectRoute ignored; cannot select route with supported types ");
        stringBuilder3.append(typesToString(route.getSupportedTypes()));
        stringBuilder3.append(" into route types ");
        stringBuilder3.append(typesToString(types));
        Log.w(str, stringBuilder3.toString());
    }

    static void selectDefaultRouteStatic() {
        if (sStatic.mSelectedRoute == sStatic.mBluetoothA2dpRoute || !sStatic.isBluetoothA2dpOn()) {
            selectRouteStatic(8388615, sStatic.mDefaultAudioVideo, false);
        } else {
            selectRouteStatic(8388615, sStatic.mBluetoothA2dpRoute, false);
        }
    }

    static boolean matchesDeviceAddress(WifiDisplay display, RouteInfo info) {
        boolean routeHasAddress = (info == null || info.mDeviceAddress == null) ? false : true;
        if (display == null && !routeHasAddress) {
            return true;
        }
        if (display == null || !routeHasAddress) {
            return false;
        }
        return display.getDeviceAddress().equals(info.mDeviceAddress);
    }

    public void addUserRoute(UserRouteInfo info) {
        addRouteStatic(info);
    }

    public void addRouteInt(RouteInfo info) {
        addRouteStatic(info);
    }

    static void addRouteStatic(RouteInfo info) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Adding route: ");
            stringBuilder.append(info);
            Log.d(TAG, stringBuilder.toString());
        }
        RouteCategory cat = info.getCategory();
        if (!sStatic.mCategories.contains(cat)) {
            sStatic.mCategories.add(cat);
        }
        if (!cat.isGroupable() || (info instanceof RouteGroup)) {
            sStatic.mRoutes.add(info);
            dispatchRouteAdded(info);
            return;
        }
        RouteInfo group = new RouteGroup(info.getCategory());
        group.mSupportedTypes = info.mSupportedTypes;
        sStatic.mRoutes.add(group);
        dispatchRouteAdded(group);
        group.addRoute(info);
        info = group;
    }

    public void removeUserRoute(UserRouteInfo info) {
        removeRouteStatic(info);
    }

    public void clearUserRoutes() {
        int i = 0;
        while (i < sStatic.mRoutes.size()) {
            RouteInfo info = (RouteInfo) sStatic.mRoutes.get(i);
            if ((info instanceof UserRouteInfo) || (info instanceof RouteGroup)) {
                removeRouteStatic(info);
                i--;
            }
            i++;
        }
    }

    public void removeRouteInt(RouteInfo info) {
        removeRouteStatic(info);
    }

    static void removeRouteStatic(RouteInfo info) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Removing route: ");
            stringBuilder.append(info);
            Log.d(TAG, stringBuilder.toString());
        }
        if (sStatic.mRoutes.remove(info)) {
            RouteCategory removingCat = info.getCategory();
            int count = sStatic.mRoutes.size();
            boolean found = false;
            for (int i = 0; i < count; i++) {
                if (removingCat == ((RouteInfo) sStatic.mRoutes.get(i)).getCategory()) {
                    found = true;
                    break;
                }
            }
            if (info.isSelected()) {
                selectDefaultRouteStatic();
            }
            if (!found) {
                sStatic.mCategories.remove(removingCat);
            }
            dispatchRouteRemoved(info);
        }
    }

    public int getCategoryCount() {
        return sStatic.mCategories.size();
    }

    public RouteCategory getCategoryAt(int index) {
        return (RouteCategory) sStatic.mCategories.get(index);
    }

    public int getRouteCount() {
        return sStatic.mRoutes.size();
    }

    public RouteInfo getRouteAt(int index) {
        return (RouteInfo) sStatic.mRoutes.get(index);
    }

    static int getRouteCountStatic() {
        return sStatic.mRoutes.size();
    }

    static RouteInfo getRouteAtStatic(int index) {
        return (RouteInfo) sStatic.mRoutes.get(index);
    }

    public UserRouteInfo createUserRoute(RouteCategory category) {
        return new UserRouteInfo(category);
    }

    public RouteCategory createRouteCategory(CharSequence name, boolean isGroupable) {
        return new RouteCategory(name, 8388608, isGroupable);
    }

    public RouteCategory createRouteCategory(int nameResId, boolean isGroupable) {
        return new RouteCategory(nameResId, 8388608, isGroupable);
    }

    public void rebindAsUser(int userId) {
        sStatic.rebindAsUser(userId);
    }

    static void updateRoute(RouteInfo info) {
        dispatchRouteChanged(info);
    }

    static void dispatchRouteSelected(int type, RouteInfo info) {
        Iterator it = sStatic.mCallbacks.iterator();
        while (it.hasNext()) {
            CallbackInfo cbi = (CallbackInfo) it.next();
            if (cbi.filterRouteEvent(info)) {
                cbi.cb.onRouteSelected(cbi.router, type, info);
            }
        }
    }

    static void dispatchRouteUnselected(int type, RouteInfo info) {
        Iterator it = sStatic.mCallbacks.iterator();
        while (it.hasNext()) {
            CallbackInfo cbi = (CallbackInfo) it.next();
            if (cbi.filterRouteEvent(info)) {
                cbi.cb.onRouteUnselected(cbi.router, type, info);
            }
        }
    }

    static void dispatchRouteChanged(RouteInfo info) {
        dispatchRouteChanged(info, info.mSupportedTypes);
    }

    static void dispatchRouteChanged(RouteInfo info, int oldSupportedTypes) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Dispatching route change: ");
            stringBuilder.append(info);
            Log.d(TAG, stringBuilder.toString());
        }
        int newSupportedTypes = info.mSupportedTypes;
        Iterator it = sStatic.mCallbacks.iterator();
        while (it.hasNext()) {
            CallbackInfo cbi = (CallbackInfo) it.next();
            boolean oldVisibility = cbi.filterRouteEvent(oldSupportedTypes);
            boolean newVisibility = cbi.filterRouteEvent(newSupportedTypes);
            if (!oldVisibility && newVisibility) {
                cbi.cb.onRouteAdded(cbi.router, info);
                if (info.isSelected()) {
                    cbi.cb.onRouteSelected(cbi.router, newSupportedTypes, info);
                }
            }
            if (oldVisibility || newVisibility) {
                cbi.cb.onRouteChanged(cbi.router, info);
            }
            if (oldVisibility && !newVisibility) {
                if (info.isSelected()) {
                    cbi.cb.onRouteUnselected(cbi.router, oldSupportedTypes, info);
                }
                cbi.cb.onRouteRemoved(cbi.router, info);
            }
        }
    }

    static void dispatchRouteAdded(RouteInfo info) {
        Iterator it = sStatic.mCallbacks.iterator();
        while (it.hasNext()) {
            CallbackInfo cbi = (CallbackInfo) it.next();
            if (cbi.filterRouteEvent(info)) {
                cbi.cb.onRouteAdded(cbi.router, info);
            }
        }
    }

    static void dispatchRouteRemoved(RouteInfo info) {
        Iterator it = sStatic.mCallbacks.iterator();
        while (it.hasNext()) {
            CallbackInfo cbi = (CallbackInfo) it.next();
            if (cbi.filterRouteEvent(info)) {
                cbi.cb.onRouteRemoved(cbi.router, info);
            }
        }
    }

    static void dispatchRouteGrouped(RouteInfo info, RouteGroup group, int index) {
        Iterator it = sStatic.mCallbacks.iterator();
        while (it.hasNext()) {
            CallbackInfo cbi = (CallbackInfo) it.next();
            if (cbi.filterRouteEvent((RouteInfo) group)) {
                cbi.cb.onRouteGrouped(cbi.router, info, group, index);
            }
        }
    }

    static void dispatchRouteUngrouped(RouteInfo info, RouteGroup group) {
        Iterator it = sStatic.mCallbacks.iterator();
        while (it.hasNext()) {
            CallbackInfo cbi = (CallbackInfo) it.next();
            if (cbi.filterRouteEvent((RouteInfo) group)) {
                cbi.cb.onRouteUngrouped(cbi.router, info, group);
            }
        }
    }

    static void dispatchRouteVolumeChanged(RouteInfo info) {
        Iterator it = sStatic.mCallbacks.iterator();
        while (it.hasNext()) {
            CallbackInfo cbi = (CallbackInfo) it.next();
            if (cbi.filterRouteEvent(info)) {
                cbi.cb.onRouteVolumeChanged(cbi.router, info);
            }
        }
    }

    static void dispatchRoutePresentationDisplayChanged(RouteInfo info) {
        Iterator it = sStatic.mCallbacks.iterator();
        while (it.hasNext()) {
            CallbackInfo cbi = (CallbackInfo) it.next();
            if (cbi.filterRouteEvent(info)) {
                cbi.cb.onRoutePresentationDisplayChanged(cbi.router, info);
            }
        }
    }

    static void systemVolumeChanged(int newValue) {
        RouteInfo selectedRoute = sStatic.mSelectedRoute;
        if (selectedRoute != null) {
            if (selectedRoute == sStatic.mBluetoothA2dpRoute || selectedRoute == sStatic.mDefaultAudioVideo) {
                dispatchRouteVolumeChanged(selectedRoute);
            } else if (sStatic.mBluetoothA2dpRoute != null) {
                try {
                    dispatchRouteVolumeChanged(sStatic.mAudioService.isBluetoothA2dpOn() ? sStatic.mBluetoothA2dpRoute : sStatic.mDefaultAudioVideo);
                } catch (RemoteException e) {
                    Log.e(TAG, "Error checking Bluetooth A2DP state to report volume change", e);
                }
            } else {
                dispatchRouteVolumeChanged(sStatic.mDefaultAudioVideo);
            }
        }
    }

    static void updateWifiDisplayStatus(WifiDisplayStatus status) {
        WifiDisplay[] displays;
        WifiDisplay activeDisplay;
        if (status.getFeatureState() == 3) {
            displays = status.getDisplays();
            activeDisplay = status.getActiveDisplay();
            if (!sStatic.mCanConfigureWifiDisplays) {
                displays = activeDisplay != null ? new WifiDisplay[]{activeDisplay} : WifiDisplay.EMPTY_ARRAY;
            }
        } else {
            displays = WifiDisplay.EMPTY_ARRAY;
            activeDisplay = null;
        }
        String activeDisplayAddress = activeDisplay != null ? activeDisplay.getDeviceAddress() : null;
        for (WifiDisplay d : displays) {
            if (shouldShowWifiDisplay(d, activeDisplay)) {
                RouteInfo route = findWifiDisplayRoute(d);
                if (route == null) {
                    route = makeWifiDisplayRoute(d, status);
                    addRouteStatic(route);
                } else {
                    String address = d.getDeviceAddress();
                    boolean disconnected = !address.equals(activeDisplayAddress) && address.equals(sStatic.mPreviousActiveWifiDisplayAddress);
                    updateWifiDisplayRoute(route, d, status, disconnected);
                }
                if (d.equals(activeDisplay)) {
                    selectRouteStatic(route.getSupportedTypes(), route, false);
                }
            }
        }
        int i = sStatic.mRoutes.size();
        while (true) {
            int i2 = i - 1;
            if (i > 0) {
                RouteInfo route2 = (RouteInfo) sStatic.mRoutes.get(i2);
                if (route2.mDeviceAddress != null) {
                    WifiDisplay d2 = findWifiDisplay(displays, route2.mDeviceAddress);
                    if (d2 == null || !shouldShowWifiDisplay(d2, activeDisplay)) {
                        removeRouteStatic(route2);
                    }
                }
                i = i2;
            } else {
                sStatic.mPreviousActiveWifiDisplayAddress = activeDisplayAddress;
                return;
            }
        }
    }

    private static boolean shouldShowWifiDisplay(WifiDisplay d, WifiDisplay activeDisplay) {
        return d.isRemembered() || d.equals(activeDisplay);
    }

    static int getWifiDisplayStatusCode(WifiDisplay d, WifiDisplayStatus wfdStatus) {
        int newStatus = wfdStatus.getScanState() == 1 ? 1 : d.isAvailable() ? d.canConnect() ? 3 : 5 : 4;
        if (!d.equals(wfdStatus.getActiveDisplay())) {
            return newStatus;
        }
        int activeState = wfdStatus.getActiveDisplayState();
        if (activeState == 0) {
            Log.e(TAG, "Active display is not connected!");
            return newStatus;
        } else if (activeState == 1) {
            return 2;
        } else {
            if (activeState != 2) {
                return newStatus;
            }
            return 6;
        }
    }

    static boolean isWifiDisplayEnabled(WifiDisplay d, WifiDisplayStatus wfdStatus) {
        return d.isAvailable() && (d.canConnect() || d.equals(wfdStatus.getActiveDisplay()));
    }

    static RouteInfo makeWifiDisplayRoute(WifiDisplay display, WifiDisplayStatus wfdStatus) {
        RouteInfo newRoute = new RouteInfo(sStatic.mSystemCategory);
        newRoute.mDeviceAddress = display.getDeviceAddress();
        newRoute.mSupportedTypes = 7;
        newRoute.mVolumeHandling = 0;
        newRoute.mPlaybackType = 1;
        newRoute.setRealStatusCode(getWifiDisplayStatusCode(display, wfdStatus));
        newRoute.mEnabled = isWifiDisplayEnabled(display, wfdStatus);
        newRoute.mName = display.getFriendlyDisplayName();
        newRoute.mDescription = sStatic.mResources.getText(R.string.wireless_display_route_description);
        newRoute.updatePresentationDisplay();
        newRoute.mDeviceType = 1;
        return newRoute;
    }

    private static void updateWifiDisplayRoute(RouteInfo route, WifiDisplay display, WifiDisplayStatus wfdStatus, boolean disconnected) {
        boolean changed = false;
        String newName = display.getFriendlyDisplayName();
        if (!route.getName().equals(newName)) {
            route.mName = newName;
            changed = true;
        }
        boolean enabled = isWifiDisplayEnabled(display, wfdStatus);
        changed |= route.mEnabled != enabled ? 1 : 0;
        route.mEnabled = enabled;
        if (changed | route.setRealStatusCode(getWifiDisplayStatusCode(display, wfdStatus))) {
            dispatchRouteChanged(route);
        }
        if ((!enabled || disconnected) && route.isSelected()) {
            selectDefaultRouteStatic();
        }
    }

    private static WifiDisplay findWifiDisplay(WifiDisplay[] displays, String deviceAddress) {
        for (WifiDisplay d : displays) {
            if (d.getDeviceAddress().equals(deviceAddress)) {
                return d;
            }
        }
        return null;
    }

    private static RouteInfo findWifiDisplayRoute(WifiDisplay d) {
        int count = sStatic.mRoutes.size();
        for (int i = 0; i < count; i++) {
            RouteInfo info = (RouteInfo) sStatic.mRoutes.get(i);
            if (d.getDeviceAddress().equals(info.mDeviceAddress)) {
                return info;
            }
        }
        return null;
    }
}
