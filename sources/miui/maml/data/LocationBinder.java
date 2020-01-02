package miui.maml.data;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.miui.BiometricConnect;
import android.os.Bundle;
import java.util.Iterator;
import java.util.List;
import miui.maml.ScreenElementRoot;
import miui.maml.data.VariableBinder.Variable;
import miui.maml.util.Utils;
import org.w3c.dom.Element;

public class LocationBinder extends VariableBinder {
    private static final float DEFAULT_MIN_DISTANCE = 10.0f;
    private static final long DEFAULT_MIN_TIME = 30000;
    private static final int DEFAULT_PROVIDER_TYPE = 0;
    private static final int GPS = 2;
    private static final int NETWORK = 1;
    public static final String TAG_NAME = "LocationBinder";
    private static LocationManager sLocationManager;
    private boolean mEnable;
    private Expression mEnableExp;
    private LocationListener mLocationListener;
    private String mLocationProvider = null;
    private float mMinDistance;
    private long mMinTime;
    private int mProviderType;
    private boolean mRegistered;

    public LocationBinder(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mProviderType = Utils.getAttrAsInt(node, "type", 0);
        this.mMinTime = Utils.getAttrAsLong(node, "time", 30000);
        this.mMinDistance = Utils.getAttrAsFloat(node, BiometricConnect.MSG_CB_BUNDLE_FACE_DISTANCE, DEFAULT_MIN_DISTANCE);
        this.mEnableExp = Expression.build(getVariables(), node.getAttribute("enable"));
        loadVariables(node);
        if (sLocationManager == null) {
            sLocationManager = (LocationManager) getContext().mContext.getSystemService("location");
        }
        List<String> providers = sLocationManager.getProviders(true);
        if (providers == null) {
            updateLocation(null);
            return;
        }
        int i = this.mProviderType;
        String str = LocationManager.GPS_PROVIDER;
        if (i != 2) {
            String str2 = LocationManager.NETWORK_PROVIDER;
            if (i == 1) {
                if (providers.contains(str2)) {
                    this.mLocationProvider = str2;
                }
            } else if (providers.contains(str2)) {
                this.mLocationProvider = str2;
            } else if (providers.contains(str)) {
                this.mLocationProvider = str;
            }
        } else if (providers.contains(str)) {
            this.mLocationProvider = str;
        }
        if (this.mLocationProvider == null) {
            updateLocation(null);
            return;
        }
        this.mLocationListener = new LocationListener() {
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }

            public void onLocationChanged(Location location) {
                LocationBinder.this.updateLocation(location);
            }
        };
        updateLocation(sLocationManager.getLastKnownLocation(this.mLocationProvider));
    }

    private void updateLocation(Location location) {
        Object result = null;
        if (location != null) {
            result = new String[]{String.valueOf(location.getAccuracy()), String.valueOf(location.getAltitude()), String.valueOf(location.getBearing()), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), String.valueOf(location.getSpeed()), String.valueOf(location.getTime())};
        }
        Iterator it = this.mVariables.iterator();
        while (it.hasNext()) {
            ((Variable) it.next()).set(result);
        }
        onUpdateComplete();
    }

    public void init() {
        super.init();
        Expression expression = this.mEnableExp;
        boolean z = true;
        if (expression != null && expression.evaluate() <= 0.0d) {
            z = false;
        }
        this.mEnable = z;
        registerListener();
    }

    public void finish() {
        unregisterListener();
        super.finish();
    }

    public void pause() {
        super.pause();
        unregisterListener();
    }

    public void resume() {
        super.resume();
        registerListener();
    }

    private void registerListener() {
        if (!this.mRegistered && this.mEnable) {
            String str = this.mLocationProvider;
            if (str != null) {
                sLocationManager.requestLocationUpdates(str, this.mMinTime, this.mMinDistance, this.mLocationListener);
                this.mRegistered = true;
            }
        }
    }

    private void unregisterListener() {
        if (this.mRegistered) {
            sLocationManager.removeUpdates(this.mLocationListener);
            this.mRegistered = false;
        }
    }

    /* Access modifiers changed, original: protected */
    public Variable onLoadVariable(Element child) {
        return new Variable(child, getContext().mVariables);
    }
}
