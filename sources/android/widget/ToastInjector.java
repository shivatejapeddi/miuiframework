package android.widget;

import android.content.Context;
import miui.os.Build;

class ToastInjector {
    ToastInjector() {
    }

    static boolean interceptBackgroundToast(Toast toast, Context context) {
        return true;
    }

    static CharSequence addAppName(Context context, CharSequence text) {
        try {
            if (!Build.IS_INTERNATIONAL_BUILD && (context.getApplicationInfo().flags & 1) == 0) {
                String appName;
                int labelRes = context.getApplicationInfo().labelRes;
                if (labelRes != 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(context.getResources().getString(labelRes));
                    stringBuilder.append("：");
                    appName = stringBuilder.toString();
                } else {
                    appName = "";
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(appName);
                stringBuilder2.append(text.toString());
                text = stringBuilder2.toString();
            }
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
        }
        return text;
    }
}
