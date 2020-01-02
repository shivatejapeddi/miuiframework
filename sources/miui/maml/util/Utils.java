package miui.maml.util;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.os.UserHandle;
import android.telecom.Logging.Session;
import android.text.TextUtils;
import java.util.ArrayList;
import miui.maml.data.IndexedVariable;
import miui.maml.data.Variables;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Utils {
    private static ArrayList<String> INTENT_BLACK_LIST = new ArrayList();
    private static final int INVALID = -2;
    private static int sAcrossUsersFullPermission = -2;
    private static int sAcrossUsersPermission = -2;

    public interface XmlTraverseListener {
        void onChild(Element element);
    }

    public static class GetChildWrapper {
        private Element mEle;

        public GetChildWrapper(Element ele) {
            this.mEle = ele;
        }

        public Element getElement() {
            return this.mEle;
        }

        public GetChildWrapper getChild(String name) {
            return new GetChildWrapper(Utils.getChild(this.mEle, name));
        }
    }

    public static class Point {
        public double x;
        public double y;

        public Point(double x0, double y0) {
            this.x = x0;
            this.y = y0;
        }

        public void Offset(Point a) {
            this.x += a.x;
            this.y += a.y;
        }

        /* Access modifiers changed, original: 0000 */
        public Point minus(Point a) {
            return new Point(this.x - a.x, this.y - a.y);
        }
    }

    static {
        INTENT_BLACK_LIST.add(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        INTENT_BLACK_LIST.add(Intent.ACTION_BATTERY_CHANGED);
        INTENT_BLACK_LIST.add(Intent.ACTION_BATTERY_LOW);
        INTENT_BLACK_LIST.add(Intent.ACTION_BATTERY_OKAY);
        INTENT_BLACK_LIST.add(Intent.ACTION_BOOT_COMPLETED);
        INTENT_BLACK_LIST.add(Intent.ACTION_CONFIGURATION_CHANGED);
        INTENT_BLACK_LIST.add(Intent.ACTION_DEVICE_STORAGE_LOW);
        INTENT_BLACK_LIST.add(Intent.ACTION_DEVICE_STORAGE_OK);
        INTENT_BLACK_LIST.add(Intent.ACTION_DREAMING_STARTED);
        INTENT_BLACK_LIST.add(Intent.ACTION_DREAMING_STOPPED);
        INTENT_BLACK_LIST.add(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        INTENT_BLACK_LIST.add(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        INTENT_BLACK_LIST.add(Intent.ACTION_LOCALE_CHANGED);
        INTENT_BLACK_LIST.add(Intent.ACTION_MY_PACKAGE_REPLACED);
        INTENT_BLACK_LIST.add(Intent.ACTION_NEW_OUTGOING_CALL);
        INTENT_BLACK_LIST.add(Intent.ACTION_PACKAGE_ADDED);
        INTENT_BLACK_LIST.add(Intent.ACTION_PACKAGE_CHANGED);
        INTENT_BLACK_LIST.add(Intent.ACTION_PACKAGE_DATA_CLEARED);
        INTENT_BLACK_LIST.add(Intent.ACTION_PACKAGE_FIRST_LAUNCH);
        INTENT_BLACK_LIST.add(Intent.ACTION_PACKAGE_FULLY_REMOVED);
        INTENT_BLACK_LIST.add(Intent.ACTION_PACKAGE_INSTALL);
        INTENT_BLACK_LIST.add(Intent.ACTION_PACKAGE_NEEDS_VERIFICATION);
        INTENT_BLACK_LIST.add(Intent.ACTION_PACKAGE_REMOVED);
        INTENT_BLACK_LIST.add(Intent.ACTION_PACKAGE_REPLACED);
        INTENT_BLACK_LIST.add(Intent.ACTION_PACKAGE_RESTARTED);
        INTENT_BLACK_LIST.add(Intent.ACTION_PACKAGE_VERIFIED);
        INTENT_BLACK_LIST.add(Intent.ACTION_POWER_CONNECTED);
        INTENT_BLACK_LIST.add(Intent.ACTION_POWER_DISCONNECTED);
        INTENT_BLACK_LIST.add(Intent.ACTION_REBOOT);
        INTENT_BLACK_LIST.add(Intent.ACTION_SCREEN_OFF);
        INTENT_BLACK_LIST.add(Intent.ACTION_SCREEN_ON);
        INTENT_BLACK_LIST.add(Intent.ACTION_SHUTDOWN);
        INTENT_BLACK_LIST.add(Intent.ACTION_TIMEZONE_CHANGED);
        INTENT_BLACK_LIST.add(Intent.ACTION_TIME_TICK);
        INTENT_BLACK_LIST.add(Intent.ACTION_UID_REMOVED);
        INTENT_BLACK_LIST.add(Intent.ACTION_USER_PRESENT);
    }

    public static void asserts(boolean t) throws Exception {
        asserts(t, "assert error");
    }

    public static void asserts(boolean t, String s) throws Exception {
        if (!t) {
            throw new Exception(s);
        }
    }

    public static int getAttrAsInt(Element ele, String name, int def) {
        try {
            return Integer.parseInt(ele.getAttribute(name));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static int getAttrAsIntThrows(Element ele, String name) throws NumberFormatException {
        return Integer.parseInt(ele.getAttribute(name));
    }

    public static long getAttrAsLong(Element ele, String name, long def) {
        try {
            return Long.parseLong(ele.getAttribute(name));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static long getAttrAsLongThrows(Element ele, String name) throws NumberFormatException {
        return Long.parseLong(ele.getAttribute(name));
    }

    public static float getAttrAsFloat(Element ele, String name, float def) {
        try {
            return Float.parseFloat(ele.getAttribute(name));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static float getAttrAsFloatThrows(Element ele, String name) throws NumberFormatException {
        return Float.parseFloat(ele.getAttribute(name));
    }

    public static Element getChild(Element ele, String name) {
        if (ele == null) {
            return null;
        }
        NodeList nl = ele.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node item = nl.item(i);
            if (item.getNodeType() == (short) 1 && item.getNodeName().equalsIgnoreCase(name)) {
                return (Element) item;
            }
        }
        return null;
    }

    public static double Dist(Point a, Point b, boolean sqr) {
        double x = a.x - b.x;
        double y = a.y - b.y;
        if (sqr) {
            return Math.sqrt((x * x) + (y * y));
        }
        return (x * x) + (y * y);
    }

    public static boolean equals(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static boolean arrayContains(String[] arr, String target) {
        for (String str : arr) {
            if (equals(str, target)) {
                return true;
            }
        }
        return false;
    }

    public static Point pointProjectionOnSegment(Point a, Point b, Point c, boolean nearestEnd) {
        Point AB = b.minus(a);
        Point AC = c.minus(a);
        double r = ((AB.x * AC.x) + (AB.y * AC.y)) / Dist(a, b, false);
        Point point;
        if (r < 0.0d || r > 1.0d) {
            point = !nearestEnd ? null : r < 0.0d ? a : b;
            return point;
        }
        point = AB;
        point.x *= r;
        point.y *= r;
        point.Offset(a);
        return point;
    }

    public static String addFileNameSuffix(String src, String separator, String suffix) {
        int dot = src.indexOf(46);
        if (dot == -1) {
            return src;
        }
        StringBuilder sb = new StringBuilder(src.substring(0, dot));
        sb.append(separator);
        sb.append(suffix);
        sb.append(src.substring(dot));
        return sb.toString();
    }

    public static String addFileNameSuffix(String src, String suffix) {
        return addFileNameSuffix(src, Session.SESSION_SEPARATION_CHAR_CHILD, suffix);
    }

    public static String doubleToString(double value) {
        String str = String.valueOf(value);
        return str.endsWith(".0") ? str.substring(0, str.length() - 2) : str;
    }

    public static String numberToString(Number value) {
        String str = String.valueOf(value);
        return str.endsWith(".0") ? str.substring(0, str.length() - 2) : str;
    }

    public static double parseDouble(String value) {
        if (value.startsWith("+") && value.length() > 1) {
            value = value.substring(1);
        }
        return Double.parseDouble(value);
    }

    public static double stringToDouble(String value, double def) {
        if (value == null) {
            return def;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static void traverseXmlElementChildrenTags(Element parent, String[] tags, XmlTraverseListener l) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            String name = node.getNodeName();
            if (node.getNodeType() == (short) 1 && (tags == null || arrContains(tags, name))) {
                l.onChild((Element) node);
            }
        }
    }

    public static void traverseXmlElementChildren(Element parent, String tag, XmlTraverseListener l) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == (short) 1 && (tag == null || TextUtils.equals(node.getNodeName(), tag))) {
                l.onChild((Element) node);
            }
        }
    }

    private static boolean arrContains(String[] arr, String tar) {
        for (String t : arr) {
            if (TextUtils.equals(t, tar)) {
                return true;
            }
        }
        return false;
    }

    public static int mixAlpha(int a1, int a2) {
        int alpha;
        if (a1 >= 255) {
            alpha = a2;
        } else if (a2 >= 255) {
            alpha = a1;
        } else {
            alpha = Math.round(((float) (a1 * a2)) / 255.0f);
        }
        return Math.min(255, Math.max(0, alpha));
    }

    public static void putVariableString(String name, Variables vars, String str) {
        vars.put(name, (Object) str);
    }

    public static void putVariableNumber(String name, Variables vars, Double value) {
        vars.put(name, value.doubleValue());
    }

    public static void putVariableNumber(String name, Variables vars, double value) {
        vars.put(name, value);
    }

    public static double getVariableNumber(String property, Variables vars) {
        return new IndexedVariable(property, vars, true).getDouble();
    }

    public static Mode getPorterDuffMode(String strMode) {
        if (TextUtils.isEmpty(strMode)) {
            return Mode.SRC_OVER;
        }
        Mode mode = Mode.SRC_OVER;
        for (Mode m : Mode.values()) {
            if (strMode.equalsIgnoreCase(m.name())) {
                mode = m;
                break;
            }
        }
        return mode;
    }

    public static Mode getPorterDuffMode(int modeNum) {
        Mode[] modes = Mode.values();
        if (modeNum < 0) {
            modeNum = 0;
        } else if (modeNum >= modes.length) {
            modeNum = modes.length - 1;
        }
        Mode mode = Mode.SRC_OVER;
        for (Mode m : modes) {
            if (m.ordinal() == modeNum) {
                return m;
            }
        }
        return mode;
    }

    public static boolean isProtectedIntent(String action) {
        return action == null ? false : INTENT_BLACK_LIST.contains(action.trim());
    }

    public String[] splitStringArray(String arr, String sep) {
        if (TextUtils.isEmpty(arr)) {
            return null;
        }
        return arr.split(sep);
    }

    public double[] splitDoubleArray(String arr) {
        if (TextUtils.isEmpty(arr)) {
            return null;
        }
        String[] sArray = arr.split(",");
        int length = sArray.length;
        double[] dArray = new double[length];
        for (int i = 0; i < length; i++) {
            try {
                dArray[i] = Double.parseDouble(sArray[i]);
            } catch (NumberFormatException e) {
            }
        }
        return dArray;
    }

    public int[] splitIntArray(String arr) {
        return splitIntArray(arr, 10);
    }

    public int[] splitIntArray(String arr, int radix) {
        if (TextUtils.isEmpty(arr)) {
            return null;
        }
        String[] sArray = arr.split(",");
        int length = sArray.length;
        int[] dArray = new int[length];
        for (int i = 0; i < length; i++) {
            try {
                dArray[i] = Integer.parseInt(sArray[i], radix);
            } catch (NumberFormatException e) {
            }
        }
        return dArray;
    }

    public byte[] splitByteArray(String arr) {
        return splitByteArray(arr, 10);
    }

    public byte[] splitByteArray(String arr, int radix) {
        if (TextUtils.isEmpty(arr)) {
            return null;
        }
        String[] sArray = arr.split(",");
        int length = sArray.length;
        byte[] dArray = new byte[length];
        for (int i = 0; i < length; i++) {
            try {
                dArray[i] = Byte.parseByte(sArray[i], radix);
            } catch (NumberFormatException e) {
            }
        }
        return dArray;
    }

    public static void startActivity(Context context, Intent intent, Bundle bundle) {
        if (sAcrossUsersFullPermission == -2) {
            sAcrossUsersFullPermission = context.checkSelfPermission(permission.INTERACT_ACROSS_USERS_FULL);
        }
        if (sAcrossUsersFullPermission == 0) {
            context.startActivityAsUser(intent, bundle, UserHandle.CURRENT);
        } else {
            context.startActivity(intent, bundle);
        }
    }

    public static void sendBroadcast(Context context, Intent intent) {
        if (sAcrossUsersPermission == -2) {
            sAcrossUsersPermission = context.checkSelfPermission(permission.INTERACT_ACROSS_USERS);
        }
        if (sAcrossUsersPermission == 0) {
            context.sendBroadcastAsUser(intent, UserHandle.CURRENT);
        } else {
            context.sendBroadcast(intent);
        }
    }

    public static void startService(Context context, Intent intent) {
        if (sAcrossUsersPermission == -2) {
            sAcrossUsersPermission = context.checkSelfPermission(permission.INTERACT_ACROSS_USERS);
        }
        if (sAcrossUsersPermission == 0) {
            context.startServiceAsUser(intent, UserHandle.CURRENT);
        } else {
            context.startService(intent);
        }
    }
}
