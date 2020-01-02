package miui.log;

import android.util.Log;

public final class MiuiLog {
    public static boolean isLoggable(MiuiTag tag, int level) {
        if (tag.isOn()) {
            return Log.isLoggable(tag.name, level);
        }
        return false;
    }

    public static void v(int tagID, Object data, String msg) {
        v(Tags.getMiuiTag(tagID), data, msg);
    }

    public static void v(MiuiTag tag, Object data, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.v(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Log.v(tag.name, msg);
    }

    public static void v(int tagID, Object data, String msg, Throwable tr) {
        v(Tags.getMiuiTag(tagID), data, msg, tr);
    }

    public static void v(MiuiTag tag, Object data, String msg, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.v(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}), tr);
            return;
        }
        Log.v(tag.name, msg, tr);
    }

    public static void d(int tagID, Object data, String msg) {
        d(Tags.getMiuiTag(tagID), data, msg);
    }

    public static void d(MiuiTag tag, Object data, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.d(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Log.d(tag.name, msg);
    }

    public static void d(int tagID, Object data, String msg, Throwable tr) {
        d(Tags.getMiuiTag(tagID), data, msg, tr);
    }

    public static void d(MiuiTag tag, Object data, String msg, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.d(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}), tr);
            return;
        }
        Log.d(tag.name, msg, tr);
    }

    public static void i(int tagID, Object data, String msg) {
        i(Tags.getMiuiTag(tagID), data, msg);
    }

    public static void i(MiuiTag tag, Object data, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.i(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Log.i(tag.name, msg);
    }

    public static void i(int tagID, Object data, String msg, Throwable tr) {
        i(Tags.getMiuiTag(tagID), data, msg, tr);
    }

    public static void i(MiuiTag tag, Object data, String msg, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.i(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}), tr);
            return;
        }
        Log.i(tag.name, msg, tr);
    }

    public static void w(int tagID, Object data, String msg) {
        w(Tags.getMiuiTag(tagID), data, msg);
    }

    public static void w(MiuiTag tag, Object data, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.w(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Log.w(tag.name, msg);
    }

    public static void w(int tagID, Object data, String msg, Throwable tr) {
        w(Tags.getMiuiTag(tagID), data, msg, tr);
    }

    public static void w(MiuiTag tag, Object data, String msg, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.w(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}), tr);
            return;
        }
        Log.w(tag.name, msg, tr);
    }

    public static void w(int tagID, Object data, Throwable tr) {
        w(Tags.getMiuiTag(tagID), data, tr);
    }

    public static void w(MiuiTag tag, Object data, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.w(tag.name, String.format("%s", new Object[]{data.toString()}), tr);
            return;
        }
        Log.w(tag.name, tr);
    }

    public static void e(int tagID, Object data, String msg) {
        e(Tags.getMiuiTag(tagID), data, msg);
    }

    public static void e(MiuiTag tag, Object data, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.e(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Log.e(tag.name, msg);
    }

    public static void e(int tagID, Object data, String msg, Throwable tr) {
        e(Tags.getMiuiTag(tagID), data, msg, tr);
    }

    public static void e(MiuiTag tag, Object data, String msg, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.e(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}), tr);
            return;
        }
        Log.e(tag.name, msg, tr);
    }

    public static void wtf(int tagID, Object data, String msg) {
        wtf(Tags.getMiuiTag(tagID), data, msg);
    }

    public static void wtf(MiuiTag tag, Object data, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.wtf(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Log.wtf(tag.name, msg);
    }

    public static void wtfStack(int tagID, Object data, String msg) {
        wtfStack(Tags.getMiuiTag(tagID), data, msg);
    }

    public static void wtfStack(MiuiTag tag, Object data, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.wtfStack(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Log.wtfStack(tag.name, msg);
    }

    public static void wtf(int tagID, Object data, Throwable tr) {
        wtf(Tags.getMiuiTag(tagID), data, tr);
    }

    public static void wtf(MiuiTag tag, Object data, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.wtf(tag.name, String.format("%s", new Object[]{data.toString()}), tr);
            return;
        }
        Log.wtf(tag.name, tr);
    }

    public static void wtf(int tagID, Object data, String msg, Throwable tr) {
        wtf(Tags.getMiuiTag(tagID), data, msg, tr);
    }

    public static void wtf(MiuiTag tag, Object data, String msg, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.wtf(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}), tr);
            return;
        }
        Log.wtf(tag.name, msg, tr);
    }

    public static void println(int priority, Object data, int tagID, String msg) {
        println(priority, data, Tags.getMiuiTag(tagID), msg);
    }

    public static void println(int priority, Object data, MiuiTag tag, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Log.println(priority, tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Log.println(priority, tag.name, msg);
    }
}
