package miui.util;

import android.content.Context;
import android.os.UserHandle;
import com.android.internal.widget.LockPatternUtils;

public class LockPatternUtilsWrapper {
    public static int getActivePasswordQuality(Context context) {
        return new LockPatternUtils(context).getActivePasswordQuality(UserHandle.myUserId());
    }
}
