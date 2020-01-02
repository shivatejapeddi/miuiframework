package android.content;

public class ContentProviderInjector {
    public static boolean isMmsProviderClass(String className) {
        return "com.android.providers.telephony.MmsProvider".equals(className);
    }

    public static boolean isCrossUserIncomingUri(Context context, int userId) {
        if (context.getUserId() == 0 && userId == 999) {
            return true;
        }
        return false;
    }
}
