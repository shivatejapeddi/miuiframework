package android.os;

public final class MiuiBinderTransaction {

    public final class IActivityManager {
        public static final int TRANSACT_ID_SET_PACKAGE_HOLD_ON = 16777214;
    }

    public final class IDisplayManager {
        public static final int TRANSACT_ID_RESET_SHORT_MODEL = 16777214;
    }

    public final class ITelephonyRegistry {
        public static final int TRANSACT_ID_GET_MIUI_TELEPHONY = 16777214;
    }

    public final class IWindowManager {
        public static final int TRANSACT_ID_SIMULATE_SLIDE_EVENT = 16777214;
        public static final int TRANSACT_ID_SWITCH_RESOLUTION = 255;
    }
}
