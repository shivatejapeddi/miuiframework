package android.os;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StrictModeInjector {
    private static ArrayList<String> mWhiteList = new ArrayList();

    static {
        mWhiteList.add("org.apache.http.impl.client.AbstractHttpClient");
        mWhiteList.add("miui.content.res.ThemeZipFile");
        mWhiteList.add("miui.text.ChinesePinyinConverter");
        mWhiteList.add("miui.telephony.phonenumber.ChineseTelocationConverter.java");
        mWhiteList.add("com.android.okhttp.internal.http.HttpURLConnectionImpl");
    }

    public static boolean isSkipStrictModeCheck(Throwable originStack) {
        List<String> classNames = parseStack(originStack);
        Iterator it = mWhiteList.iterator();
        while (it.hasNext()) {
            if (classNames.contains((String) it.next())) {
                return true;
            }
        }
        return false;
    }

    private static List<String> parseStack(Throwable originStack) {
        List<String> classNames = new ArrayList();
        StackTraceElement[] elements = originStack.getStackTrace();
        for (StackTraceElement className : elements) {
            classNames.add(className.getClassName());
        }
        return classNames;
    }
}
