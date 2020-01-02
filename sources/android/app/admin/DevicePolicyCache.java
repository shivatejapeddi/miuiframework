package android.app.admin;

import com.android.server.LocalServices;

public abstract class DevicePolicyCache {

    private static class EmptyDevicePolicyCache extends DevicePolicyCache {
        private static final EmptyDevicePolicyCache INSTANCE = new EmptyDevicePolicyCache();

        private EmptyDevicePolicyCache() {
        }

        public boolean getScreenCaptureDisabled(int userHandle) {
            return false;
        }

        public int getPasswordQuality(int userHandle) {
            return 0;
        }
    }

    public abstract int getPasswordQuality(int i);

    public abstract boolean getScreenCaptureDisabled(int i);

    protected DevicePolicyCache() {
    }

    public static DevicePolicyCache getInstance() {
        DevicePolicyManagerInternal dpmi = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
        return dpmi != null ? dpmi.getDevicePolicyCache() : EmptyDevicePolicyCache.INSTANCE;
    }
}
