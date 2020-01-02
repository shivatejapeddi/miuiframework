package android.net;

import android.Manifest.permission;
import android.annotation.SystemApi;
import android.content.Context;
import java.util.ArrayList;
import java.util.Arrays;

@SystemApi
public class NetworkStack {
    @SystemApi
    public static final String PERMISSION_MAINLINE_NETWORK_STACK = "android.permission.MAINLINE_NETWORK_STACK";

    private NetworkStack() {
    }

    public static void checkNetworkStackPermission(Context context) {
        checkNetworkStackPermissionOr(context, new String[0]);
    }

    public static void checkNetworkStackPermissionOr(Context context, String... otherPermissions) {
        ArrayList<String> permissions = new ArrayList(Arrays.asList(otherPermissions));
        permissions.add(permission.NETWORK_STACK);
        permissions.add(PERMISSION_MAINLINE_NETWORK_STACK);
        enforceAnyPermissionOf(context, (String[]) permissions.toArray(new String[0]));
    }

    private static void enforceAnyPermissionOf(Context context, String... permissions) {
        if (!checkAnyPermissionOf(context, permissions)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Requires one of the following permissions: ");
            stringBuilder.append(String.join(", ", permissions));
            stringBuilder.append(".");
            throw new SecurityException(stringBuilder.toString());
        }
    }

    private static boolean checkAnyPermissionOf(Context context, String... permissions) {
        for (String permission : permissions) {
            if (context.checkCallingOrSelfPermission(permission) == 0) {
                return true;
            }
        }
        return false;
    }
}
