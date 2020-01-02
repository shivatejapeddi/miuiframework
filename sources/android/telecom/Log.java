package android.telecom;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.telecom.Logging.EventManager;
import android.telecom.Logging.EventManager.EventListener;
import android.telecom.Logging.EventManager.Loggable;
import android.telecom.Logging.EventManager.TimedEventPair;
import android.telecom.Logging.Session;
import android.telecom.Logging.Session.Info;
import android.telecom.Logging.SessionManager;
import android.telecom.Logging.SessionManager.ISessionListener;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import java.util.IllegalFormatException;
import java.util.Locale;
import miui.telephony.PhoneDebug;
import org.apache.miui.commons.lang3.ClassUtils;

public class Log {
    public static boolean DEBUG = isLoggable(3);
    public static boolean ERROR = isLoggable(6);
    private static final int EVENTS_TO_CACHE = 10;
    private static final int EVENTS_TO_CACHE_DEBUG = 20;
    private static final long EXTENDED_LOGGING_DURATION_MILLIS = 1800000;
    private static final boolean FORCE_LOGGING = false;
    public static boolean INFO = isLoggable(4);
    private static final int NUM_DIALABLE_DIGITS_TO_LOG = (Build.IS_USER ? 0 : 2);
    @VisibleForTesting
    public static String TAG = "TelecomFramework";
    private static final boolean USER_BUILD = Build.IS_USER;
    public static boolean VERBOSE = isLoggable(2);
    public static boolean WARN = isLoggable(5);
    private static EventManager sEventManager;
    private static boolean sIsUserExtendedLoggingEnabled = false;
    private static SessionManager sSessionManager;
    private static final Object sSingletonSync = new Object();
    private static long sUserExtendedLoggingStopTime = 0;

    private Log() {
    }

    public static void d(String prefix, String format, Object... args) {
        if (sIsUserExtendedLoggingEnabled) {
            maybeDisableLogging();
            Slog.i(TAG, buildMessage(prefix, format, args));
        } else if (DEBUG) {
            Slog.d(TAG, buildMessage(prefix, format, args));
        }
    }

    public static void d(Object objectPrefix, String format, Object... args) {
        if (sIsUserExtendedLoggingEnabled) {
            maybeDisableLogging();
            Slog.i(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args));
        } else if (DEBUG) {
            Slog.d(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args));
        }
    }

    @UnsupportedAppUsage
    public static void i(String prefix, String format, Object... args) {
        if (INFO) {
            Slog.i(TAG, buildMessage(prefix, format, args));
        }
    }

    public static void i(Object objectPrefix, String format, Object... args) {
        if (INFO) {
            Slog.i(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args));
        }
    }

    public static void v(String prefix, String format, Object... args) {
        if (sIsUserExtendedLoggingEnabled) {
            maybeDisableLogging();
            Slog.i(TAG, buildMessage(prefix, format, args));
        } else if (VERBOSE) {
            Slog.v(TAG, buildMessage(prefix, format, args));
        }
    }

    public static void v(Object objectPrefix, String format, Object... args) {
        if (sIsUserExtendedLoggingEnabled) {
            maybeDisableLogging();
            Slog.i(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args));
        } else if (VERBOSE) {
            Slog.v(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args));
        }
    }

    @UnsupportedAppUsage
    public static void w(String prefix, String format, Object... args) {
        if (WARN) {
            Slog.w(TAG, buildMessage(prefix, format, args));
        }
    }

    public static void w(Object objectPrefix, String format, Object... args) {
        if (WARN) {
            Slog.w(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args));
        }
    }

    public static void e(String prefix, Throwable tr, String format, Object... args) {
        if (ERROR) {
            Slog.e(TAG, buildMessage(prefix, format, args), tr);
        }
    }

    public static void e(Object objectPrefix, Throwable tr, String format, Object... args) {
        if (ERROR) {
            Slog.e(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args), tr);
        }
    }

    public static void wtf(String prefix, Throwable tr, String format, Object... args) {
        Slog.wtf(TAG, buildMessage(prefix, format, args), tr);
    }

    public static void wtf(Object objectPrefix, Throwable tr, String format, Object... args) {
        Slog.wtf(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args), tr);
    }

    public static void wtf(String prefix, String format, Object... args) {
        String msg = buildMessage(prefix, format, args);
        Slog.wtf(TAG, msg, new IllegalStateException(msg));
    }

    public static void wtf(Object objectPrefix, String format, Object... args) {
        String msg = buildMessage(getPrefixFromObject(objectPrefix), format, args);
        Slog.wtf(TAG, msg, new IllegalStateException(msg));
    }

    public static void setSessionContext(Context context) {
        getSessionManager().setContext(context);
    }

    public static void startSession(String shortMethodName) {
        getSessionManager().startSession(shortMethodName, null);
    }

    public static void startSession(Info info, String shortMethodName) {
        getSessionManager().startSession(info, shortMethodName, null);
    }

    public static void startSession(String shortMethodName, String callerIdentification) {
        getSessionManager().startSession(shortMethodName, callerIdentification);
    }

    public static void startSession(Info info, String shortMethodName, String callerIdentification) {
        getSessionManager().startSession(info, shortMethodName, callerIdentification);
    }

    public static Session createSubsession() {
        return getSessionManager().createSubsession();
    }

    public static Info getExternalSession() {
        return getSessionManager().getExternalSession();
    }

    public static void cancelSubsession(Session subsession) {
        getSessionManager().cancelSubsession(subsession);
    }

    public static void continueSession(Session subsession, String shortMethodName) {
        getSessionManager().continueSession(subsession, shortMethodName);
    }

    public static void endSession() {
        getSessionManager().endSession();
    }

    public static void registerSessionListener(ISessionListener l) {
        getSessionManager().registerSessionListener(l);
    }

    public static String getSessionId() {
        synchronized (sSingletonSync) {
            String sessionId;
            if (sSessionManager != null) {
                sessionId = getSessionManager().getSessionId();
                return sessionId;
            }
            sessionId = "";
            return sessionId;
        }
    }

    public static void addEvent(Loggable recordEntry, String event) {
        getEventManager().event(recordEntry, event, null);
    }

    public static void addEvent(Loggable recordEntry, String event, Object data) {
        getEventManager().event(recordEntry, event, data);
    }

    public static void addEvent(Loggable recordEntry, String event, String format, Object... args) {
        getEventManager().event(recordEntry, event, format, args);
    }

    public static void registerEventListener(EventListener e) {
        getEventManager().registerEventListener(e);
    }

    public static void addRequestResponsePair(TimedEventPair p) {
        getEventManager().addRequestResponsePair(p);
    }

    public static void dumpEvents(IndentingPrintWriter pw) {
        synchronized (sSingletonSync) {
            if (sEventManager != null) {
                getEventManager().dumpEvents(pw);
            } else {
                pw.println("No Historical Events Logged.");
            }
        }
    }

    public static void dumpEventsTimeline(IndentingPrintWriter pw) {
        synchronized (sSingletonSync) {
            if (sEventManager != null) {
                getEventManager().dumpEventsTimeline(pw);
            } else {
                pw.println("No Historical Events Logged.");
            }
        }
    }

    public static void setIsExtendedLoggingEnabled(boolean isExtendedLoggingEnabled) {
        if (sIsUserExtendedLoggingEnabled != isExtendedLoggingEnabled) {
            EventManager eventManager = sEventManager;
            if (eventManager != null) {
                eventManager.changeEventCacheSize(isExtendedLoggingEnabled ? 20 : 10);
            }
            sIsUserExtendedLoggingEnabled = isExtendedLoggingEnabled;
            if (sIsUserExtendedLoggingEnabled) {
                sUserExtendedLoggingStopTime = System.currentTimeMillis() + 1800000;
            } else {
                sUserExtendedLoggingStopTime = 0;
            }
        }
    }

    private static EventManager getEventManager() {
        if (sEventManager == null) {
            synchronized (sSingletonSync) {
                if (sEventManager == null) {
                    sEventManager = new EventManager(-$$Lambda$qa4s1Fm2YuohEunaJUJcmJXDXG0.INSTANCE);
                    EventManager eventManager = sEventManager;
                    return eventManager;
                }
            }
        }
        return sEventManager;
    }

    @VisibleForTesting
    public static SessionManager getSessionManager() {
        if (sSessionManager == null) {
            synchronized (sSingletonSync) {
                if (sSessionManager == null) {
                    sSessionManager = new SessionManager();
                    SessionManager sessionManager = sSessionManager;
                    return sessionManager;
                }
            }
        }
        return sSessionManager;
    }

    public static void setTag(String tag) {
        TAG = tag;
        boolean z = false;
        boolean z2 = isLoggable(3) || PhoneDebug.VDBG;
        DEBUG = z2;
        z2 = isLoggable(4) || PhoneDebug.VDBG;
        INFO = z2;
        if (isLoggable(2) || PhoneDebug.VDBG) {
            z = true;
        }
        VERBOSE = z;
        WARN = isLoggable(5);
        ERROR = isLoggable(6);
    }

    private static void maybeDisableLogging() {
        if (sIsUserExtendedLoggingEnabled && sUserExtendedLoggingStopTime < System.currentTimeMillis()) {
            sUserExtendedLoggingStopTime = 0;
            sIsUserExtendedLoggingEnabled = false;
        }
    }

    public static boolean isLoggable(int level) {
        return android.util.Log.isLoggable(TAG, level);
    }

    public static String piiHandle(Object pii) {
        if (pii == null || PhoneDebug.VDBG) {
            return String.valueOf(pii);
        }
        StringBuilder sb = new StringBuilder();
        if (pii instanceof Uri) {
            Uri uri = (Uri) pii;
            String scheme = uri.getScheme();
            if (!TextUtils.isEmpty(scheme)) {
                sb.append(scheme);
                sb.append(":");
            }
            String textToObfuscate = uri.getSchemeSpecificPart();
            if (PhoneAccount.SCHEME_TEL.equals(scheme)) {
                obfuscatePhoneNumber(sb, textToObfuscate);
            } else if ("sip".equals(scheme)) {
                for (int i = 0; i < textToObfuscate.length(); i++) {
                    char c = textToObfuscate.charAt(i);
                    if (!(c == '@' || c == ClassUtils.PACKAGE_SEPARATOR_CHAR)) {
                        c = '*';
                    }
                    sb.append(c);
                }
            } else {
                sb.append(pii(pii));
            }
        } else if (pii instanceof String) {
            obfuscatePhoneNumber(sb, (String) pii);
        }
        return sb.toString();
    }

    private static void obfuscatePhoneNumber(StringBuilder sb, String phoneNumber) {
        int numDigitsToObfuscate = getDialableCount(phoneNumber) - NUM_DIALABLE_DIGITS_TO_LOG;
        for (int i = 0; i < phoneNumber.length(); i++) {
            char c = phoneNumber.charAt(i);
            boolean isDialable = PhoneNumberUtils.isDialable(c);
            if (isDialable) {
                numDigitsToObfuscate--;
            }
            Object valueOf = (!isDialable || numDigitsToObfuscate < 0) ? Character.valueOf(c) : "*";
            sb.append(valueOf);
        }
    }

    private static int getDialableCount(String toCount) {
        int numDialable = 0;
        for (char c : toCount.toCharArray()) {
            if (PhoneNumberUtils.isDialable(c)) {
                numDialable++;
            }
        }
        return numDialable;
    }

    public static String pii(Object pii) {
        if (pii == null || VERBOSE) {
            return String.valueOf(pii);
        }
        return "***";
    }

    private static String getPrefixFromObject(Object obj) {
        return obj == null ? "<null>" : obj.getClass().getSimpleName();
    }

    private static String buildMessage(String prefix, String format, Object... args) {
        String sessionPostfix;
        String msg;
        String sessionName = getSessionId();
        if (TextUtils.isEmpty(sessionName)) {
            sessionPostfix = "";
        } else {
            sessionPostfix = new StringBuilder();
            sessionPostfix.append(": ");
            sessionPostfix.append(sessionName);
            sessionPostfix = sessionPostfix.toString();
        }
        if (args != null) {
            try {
                if (args.length != 0) {
                    msg = String.format(Locale.US, format, args);
                    return String.format(Locale.US, "%s: %s%s", new Object[]{prefix, msg, sessionPostfix});
                }
            } catch (IllegalFormatException ife) {
                e(TAG, ife, "Log: IllegalFormatException: formatString='%s' numArgs=%d", format, Integer.valueOf(args.length));
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(format);
                stringBuilder.append(" (An error occurred while formatting the message.)");
                msg = stringBuilder.toString();
            }
        }
        msg = format;
        return String.format(Locale.US, "%s: %s%s", new Object[]{prefix, msg, sessionPostfix});
    }
}
