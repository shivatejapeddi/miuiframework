package android.telephony.mbms;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public interface MbmsGroupCallSessionCallback {

    @Retention(RetentionPolicy.SOURCE)
    public @interface GroupCallError {
    }

    void onError(int errorCode, String message) {
    }

    void onAvailableSaisUpdated(List<Integer> list, List<List<Integer>> list2) {
    }

    void onServiceInterfaceAvailable(String interfaceName, int index) {
    }

    void onMiddlewareReady() {
    }
}
