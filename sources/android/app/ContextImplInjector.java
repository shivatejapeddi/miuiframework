package android.app;

import android.content.Context;
import android.location.ILocationPolicyManager;
import android.location.LocationPolicyManager;
import android.os.IBinder;
import android.os.ServiceManager;
import miui.security.ISecurityManager.Stub;
import miui.security.SecurityManager;

class ContextImplInjector {
    ContextImplInjector() {
    }

    static void registerMiuiServices() {
        AnonymousClass1 anonymousClass1 = new CachedServiceFetcher<SecurityManager>() {
            public SecurityManager createService(ContextImpl ctx) {
                IBinder b = ServiceManager.getService(Context.SECURITY_SERVICE);
                if (b == null) {
                    return null;
                }
                return new SecurityManager(Stub.asInterface(b));
            }
        };
        SystemServiceRegistry.registerService(Context.SECURITY_SERVICE, SecurityManager.class, anonymousClass1);
        AnonymousClass2 anonymousClass2 = new CachedServiceFetcher<LocationPolicyManager>() {
            public LocationPolicyManager createService(ContextImpl ctx) {
                return new LocationPolicyManager(ILocationPolicyManager.Stub.asInterface(ServiceManager.getService(Context.LOCATION_POLICY_SERVICE)));
            }
        };
        SystemServiceRegistry.registerService(Context.LOCATION_POLICY_SERVICE, LocationPolicyManager.class, anonymousClass2);
    }
}
