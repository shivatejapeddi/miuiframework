package miui.log;

import android.util.Slog;

public final class MiuiSlog {
    public static void v(int tagID, Object data, String msg) {
        v(Tags.getMiuiTag(tagID), data, msg);
    }

    public static void v(MiuiTag tag, Object data, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.v(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Slog.v(tag.name, msg);
    }

    public static void v(int tagID, Object data, String msg, Throwable tr) {
        v(Tags.getMiuiTag(tagID), data, msg, tr);
    }

    public static void v(MiuiTag tag, Object data, String msg, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.v(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}), tr);
            return;
        }
        Slog.v(tag.name, msg, tr);
    }

    public static void v(String tag, String msg) {
        Slog.v(tag, msg);
    }

    public static void d(int tagID, Object data, String msg) {
        d(Tags.getMiuiTag(tagID), data, msg);
    }

    public static void d(MiuiTag tag, Object data, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.d(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Slog.d(tag.name, msg);
    }

    public static void d(int tagID, Object data, String msg, Throwable tr) {
        d(Tags.getMiuiTag(tagID), data, msg, tr);
    }

    public static void d(MiuiTag tag, Object data, String msg, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.d(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}), tr);
            return;
        }
        Slog.d(tag.name, msg, tr);
    }

    public static void d(String tag, String msg) {
        Slog.d(tag, msg);
    }

    public static void i(int tagID, Object data, String msg) {
        i(Tags.getMiuiTag(tagID), data, msg);
    }

    public static void i(MiuiTag tag, Object data, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.i(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Slog.i(tag.name, msg);
    }

    public static void i(int tagID, Object data, String msg, Throwable tr) {
        i(Tags.getMiuiTag(tagID), data, msg, tr);
    }

    public static void i(MiuiTag tag, Object data, String msg, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.i(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}), tr);
            return;
        }
        Slog.i(tag.name, msg, tr);
    }

    public static void i(String tag, String msg) {
        Slog.i(tag, msg);
    }

    public static void w(int tagID, Object data, String msg) {
        w(Tags.getMiuiTag(tagID), data, msg);
    }

    public static void w(MiuiTag tag, Object data, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.w(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Slog.w(tag.name, msg);
    }

    public static void w(int tagID, Object data, String msg, Throwable tr) {
        w(Tags.getMiuiTag(tagID), data, msg, tr);
    }

    public static void w(MiuiTag tag, Object data, String msg, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.w(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}), tr);
            return;
        }
        Slog.w(tag.name, msg, tr);
    }

    public static void w(int tagID, Object data, Throwable tr) {
        w(Tags.getMiuiTag(tagID), data, tr);
    }

    public static void w(MiuiTag tag, Object data, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.w(tag.name, String.format("%s", new Object[]{data.toString()}), tr);
            return;
        }
        Slog.w(tag.name, tr);
    }

    public static void w(String tag, String msg) {
        Slog.w(tag, msg);
    }

    public static void e(int tagID, Object data, String msg) {
        e(Tags.getMiuiTag(tagID), data, msg);
    }

    public static void e(MiuiTag tag, Object data, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.e(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Slog.e(tag.name, msg);
    }

    public static void e(int tagID, Object data, String msg, Throwable tr) {
        e(Tags.getMiuiTag(tagID), data, msg, tr);
    }

    public static void e(MiuiTag tag, Object data, String msg, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.e(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}), tr);
            return;
        }
        Slog.e(tag.name, msg, tr);
    }

    public static void e(String tag, String msg) {
        Slog.e(tag, msg);
    }

    public static void wtf(int tagID, Object data, String msg) {
        wtf(Tags.getMiuiTag(tagID), data, msg);
    }

    public static void wtf(MiuiTag tag, Object data, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.wtf(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Slog.wtf(tag.name, msg);
    }

    public static void wtfStack(int tagID, Object data, String msg) {
        wtfStack(Tags.getMiuiTag(tagID), data, msg);
    }

    public static void wtfStack(MiuiTag tag, Object data, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.wtfStack(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Slog.wtfStack(tag.name, msg);
    }

    public static void wtf(int tagID, Object data, Throwable tr) {
        wtf(Tags.getMiuiTag(tagID), data, tr);
    }

    public static void wtf(MiuiTag tag, Object data, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.wtf(tag.name, String.format("%s", new Object[]{data.toString()}), tr);
            return;
        }
        Slog.wtf(tag.name, tr);
    }

    public static void wtf(int tagID, Object data, String msg, Throwable tr) {
        wtf(Tags.getMiuiTag(tagID), data, msg, tr);
    }

    public static void wtf(MiuiTag tag, Object data, String msg, Throwable tr) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.wtf(tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}), tr);
            return;
        }
        Slog.wtf(tag.name, msg, tr);
    }

    public static void println(int priority, Object data, int tagID, String msg) {
        println(priority, data, Tags.getMiuiTag(tagID), msg);
    }

    public static void println(int priority, Object data, MiuiTag tag, String msg) {
        if (!tag.isOn()) {
            return;
        }
        if (data != null) {
            Slog.println(priority, tag.name, String.format("[%s] %s", new Object[]{data.toString(), msg}));
            return;
        }
        Slog.println(priority, tag.name, msg);
    }
}
