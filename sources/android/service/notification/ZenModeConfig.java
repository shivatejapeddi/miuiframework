package android.service.notification;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlarmManager.AlarmClockInfo;
import android.app.NotificationManager.Policy;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.MiuiWifiManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Contacts;
import android.provider.Settings.Global;
import android.provider.Telephony.BaseMmsColumns;
import android.service.notification.ZenPolicy.Builder;
import android.telecom.Logging.Session;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.R;
import com.android.internal.telephony.IccCardConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import miui.securityspace.XSpaceUtils;
import org.apache.miui.commons.lang3.ClassUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class ZenModeConfig implements Parcelable {
    private static final String ALLOW_ATT_ALARMS = "alarms";
    private static final String ALLOW_ATT_CALLS = "calls";
    private static final String ALLOW_ATT_CALLS_FROM = "callsFrom";
    private static final String ALLOW_ATT_EVENTS = "events";
    private static final String ALLOW_ATT_FROM = "from";
    private static final String ALLOW_ATT_MEDIA = "media";
    private static final String ALLOW_ATT_MESSAGES = "messages";
    private static final String ALLOW_ATT_MESSAGES_FROM = "messagesFrom";
    private static final String ALLOW_ATT_REMINDERS = "reminders";
    private static final String ALLOW_ATT_REPEAT_CALLERS = "repeatCallers";
    private static final String ALLOW_ATT_SCREEN_OFF = "visualScreenOff";
    private static final String ALLOW_ATT_SCREEN_ON = "visualScreenOn";
    private static final String ALLOW_ATT_SYSTEM = "system";
    private static final String ALLOW_TAG = "allow";
    public static final int[] ALL_DAYS = new int[]{1, 2, 3, 4, 5, 6, 7};
    private static final String AUTOMATIC_TAG = "automatic";
    private static final String CONDITION_ATT_FLAGS = "flags";
    private static final String CONDITION_ATT_ICON = "icon";
    private static final String CONDITION_ATT_ID = "id";
    private static final String CONDITION_ATT_LINE1 = "line1";
    private static final String CONDITION_ATT_LINE2 = "line2";
    private static final String CONDITION_ATT_STATE = "state";
    private static final String CONDITION_ATT_SUMMARY = "summary";
    public static final String COUNTDOWN_PATH = "countdown";
    public static final Creator<ZenModeConfig> CREATOR = new Creator<ZenModeConfig>() {
        public ZenModeConfig createFromParcel(Parcel source) {
            return new ZenModeConfig(source);
        }

        public ZenModeConfig[] newArray(int size) {
            return new ZenModeConfig[size];
        }
    };
    private static final int DAY_MINUTES = 1440;
    private static final boolean DEFAULT_ALLOW_ALARMS = true;
    private static final boolean DEFAULT_ALLOW_CALLS = true;
    private static final boolean DEFAULT_ALLOW_EVENTS = false;
    private static final boolean DEFAULT_ALLOW_MEDIA = true;
    private static final boolean DEFAULT_ALLOW_MESSAGES = false;
    private static final boolean DEFAULT_ALLOW_REMINDERS = false;
    private static final boolean DEFAULT_ALLOW_REPEAT_CALLERS = true;
    private static final boolean DEFAULT_ALLOW_SYSTEM = false;
    private static final int DEFAULT_CALLS_SOURCE = 2;
    private static final boolean DEFAULT_CHANNELS_BYPASSING_DND = false;
    public static final List<String> DEFAULT_RULE_IDS = Arrays.asList(new String[]{EVERY_NIGHT_DEFAULT_RULE_ID, EVENTS_DEFAULT_RULE_ID});
    private static final int DEFAULT_SOURCE = 1;
    private static final int DEFAULT_SUPPRESSED_VISUAL_EFFECTS = 0;
    private static final String DISALLOW_ATT_VISUAL_EFFECTS = "visualEffects";
    private static final String DISALLOW_TAG = "disallow";
    public static final String EVENTS_DEFAULT_RULE_ID = "EVENTS_DEFAULT_RULE";
    public static final String EVENT_PATH = "event";
    public static final String EVERY_NIGHT_DEFAULT_RULE_ID = "EVERY_NIGHT_DEFAULT_RULE";
    public static final String IS_ALARM_PATH = "alarm";
    private static final String MANUAL_TAG = "manual";
    public static final int MAX_SOURCE = 2;
    private static final int MINUTES_MS = 60000;
    public static final int[] MINUTE_BUCKETS = generateMinuteBuckets();
    private static final String RULE_ATT_COMPONENT = "component";
    private static final String RULE_ATT_CONDITION_ID = "conditionId";
    private static final String RULE_ATT_CONFIG_ACTIVITY = "configActivity";
    private static final String RULE_ATT_CREATION_TIME = "creationTime";
    private static final String RULE_ATT_ENABLED = "enabled";
    private static final String RULE_ATT_ENABLER = "enabler";
    private static final String RULE_ATT_ID = "ruleId";
    private static final String RULE_ATT_MODIFIED = "modified";
    private static final String RULE_ATT_NAME = "name";
    private static final String RULE_ATT_SNOOZING = "snoozing";
    private static final String RULE_ATT_ZEN = "zen";
    public static final String SCHEDULE_PATH = "schedule";
    private static final int SECONDS_MS = 1000;
    private static final String SHOW_ATT_AMBIENT = "showAmbient";
    private static final String SHOW_ATT_BADGES = "showBadges";
    private static final String SHOW_ATT_FULL_SCREEN_INTENT = "showFullScreenIntent";
    private static final String SHOW_ATT_LIGHTS = "showLights";
    private static final String SHOW_ATT_NOTIFICATION_LIST = "showNotificationList";
    private static final String SHOW_ATT_PEEK = "shoePeek";
    private static final String SHOW_ATT_STATUS_BAR_ICONS = "showStatusBarIcons";
    public static final int SOURCE_ANYONE = 0;
    public static final int SOURCE_CONTACT = 1;
    public static final int SOURCE_STAR = 2;
    private static final String STATE_ATT_CHANNELS_BYPASSING_DND = "areChannelsBypassingDnd";
    private static final String STATE_TAG = "state";
    public static final String SYSTEM_AUTHORITY = "android";
    private static String TAG = "ZenModeConfig";
    public static final int XML_VERSION = 8;
    private static final String ZEN_ATT_USER = "user";
    private static final String ZEN_ATT_VERSION = "version";
    private static final String ZEN_POLICY_TAG = "zen_policy";
    public static final String ZEN_TAG = "zen";
    private static final int ZERO_VALUE_MS = 10000;
    @UnsupportedAppUsage
    public boolean allowAlarms = true;
    public boolean allowCalls = true;
    public int allowCallsFrom = 2;
    public boolean allowEvents = false;
    public boolean allowMedia = true;
    public boolean allowMessages = false;
    public int allowMessagesFrom = 1;
    public boolean allowReminders = false;
    public boolean allowRepeatCallers = true;
    public boolean allowSystem = false;
    public boolean areChannelsBypassingDnd = false;
    @UnsupportedAppUsage
    public ArrayMap<String, ZenRule> automaticRules = new ArrayMap();
    public ZenRule manualRule;
    public int suppressedVisualEffects = 0;
    public int user = 0;
    public int version;

    public static class Diff {
        private final ArrayList<String> lines = new ArrayList();

        public String toString() {
            StringBuilder sb = new StringBuilder("Diff[");
            int N = this.lines.size();
            for (int i = 0; i < N; i++) {
                if (i > 0) {
                    sb.append(",\n");
                }
                sb.append((String) this.lines.get(i));
            }
            sb.append(']');
            return sb.toString();
        }

        private Diff addLine(String item, String action) {
            ArrayList arrayList = this.lines;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(item);
            stringBuilder.append(":");
            stringBuilder.append(action);
            arrayList.add(stringBuilder.toString());
            return this;
        }

        public Diff addLine(String item, String subitem, Object from, Object to) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(item);
            stringBuilder.append(".");
            stringBuilder.append(subitem);
            return addLine(stringBuilder.toString(), from, to);
        }

        public Diff addLine(String item, Object from, Object to) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(from);
            stringBuilder.append(Session.SUBSESSION_SEPARATION_CHAR);
            stringBuilder.append(to);
            return addLine(item, stringBuilder.toString());
        }
    }

    public static class EventInfo {
        public static final int REPLY_ANY_EXCEPT_NO = 0;
        public static final int REPLY_YES = 2;
        public static final int REPLY_YES_OR_MAYBE = 1;
        public String calName;
        public Long calendarId;
        public int reply;
        public int userId = -10000;

        public int hashCode() {
            return Objects.hash(new Object[]{Integer.valueOf(this.userId), this.calName, this.calendarId, Integer.valueOf(this.reply)});
        }

        public boolean equals(Object o) {
            boolean z = false;
            if (!(o instanceof EventInfo)) {
                return false;
            }
            EventInfo other = (EventInfo) o;
            if (this.userId == other.userId && Objects.equals(this.calName, other.calName) && this.reply == other.reply && Objects.equals(this.calendarId, other.calendarId)) {
                z = true;
            }
            return z;
        }

        public EventInfo copy() {
            EventInfo rt = new EventInfo();
            rt.userId = this.userId;
            rt.calName = this.calName;
            rt.reply = this.reply;
            rt.calendarId = this.calendarId;
            return rt;
        }

        public static int resolveUserId(int userId) {
            return userId == -10000 ? ActivityManager.getCurrentUser() : userId;
        }
    }

    public static class ScheduleInfo {
        @UnsupportedAppUsage
        public int[] days;
        @UnsupportedAppUsage
        public int endHour;
        @UnsupportedAppUsage
        public int endMinute;
        public boolean exitAtAlarm;
        public long nextAlarm;
        @UnsupportedAppUsage
        public int startHour;
        @UnsupportedAppUsage
        public int startMinute;

        public int hashCode() {
            return 0;
        }

        public boolean equals(Object o) {
            boolean z = false;
            if (!(o instanceof ScheduleInfo)) {
                return false;
            }
            ScheduleInfo other = (ScheduleInfo) o;
            if (ZenModeConfig.toDayList(this.days).equals(ZenModeConfig.toDayList(other.days)) && this.startHour == other.startHour && this.startMinute == other.startMinute && this.endHour == other.endHour && this.endMinute == other.endMinute && this.exitAtAlarm == other.exitAtAlarm) {
                z = true;
            }
            return z;
        }

        public ScheduleInfo copy() {
            ScheduleInfo rt = new ScheduleInfo();
            int[] iArr = this.days;
            if (iArr != null) {
                rt.days = new int[iArr.length];
                iArr = this.days;
                System.arraycopy(iArr, 0, rt.days, 0, iArr.length);
            }
            rt.startHour = this.startHour;
            rt.startMinute = this.startMinute;
            rt.endHour = this.endHour;
            rt.endMinute = this.endMinute;
            rt.exitAtAlarm = this.exitAtAlarm;
            rt.nextAlarm = this.nextAlarm;
            return rt;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ScheduleInfo{days=");
            stringBuilder.append(Arrays.toString(this.days));
            stringBuilder.append(", startHour=");
            stringBuilder.append(this.startHour);
            stringBuilder.append(", startMinute=");
            stringBuilder.append(this.startMinute);
            stringBuilder.append(", endHour=");
            stringBuilder.append(this.endHour);
            stringBuilder.append(", endMinute=");
            stringBuilder.append(this.endMinute);
            stringBuilder.append(", exitAtAlarm=");
            stringBuilder.append(this.exitAtAlarm);
            stringBuilder.append(", nextAlarm=");
            stringBuilder.append(ts(this.nextAlarm));
            stringBuilder.append('}');
            return stringBuilder.toString();
        }

        protected static String ts(long time) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(new Date(time));
            stringBuilder.append(" (");
            stringBuilder.append(time);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    public static class ZenRule implements Parcelable {
        public static final Creator<ZenRule> CREATOR = new Creator<ZenRule>() {
            public ZenRule createFromParcel(Parcel source) {
                return new ZenRule(source);
            }

            public ZenRule[] newArray(int size) {
                return new ZenRule[size];
            }
        };
        public ComponentName component;
        public Condition condition;
        @UnsupportedAppUsage
        public Uri conditionId;
        public ComponentName configurationActivity;
        @UnsupportedAppUsage
        public long creationTime;
        @UnsupportedAppUsage
        public boolean enabled;
        public String enabler;
        public String id;
        public boolean modified;
        @UnsupportedAppUsage
        public String name;
        public String pkg;
        @UnsupportedAppUsage
        public boolean snoozing;
        @UnsupportedAppUsage
        public int zenMode;
        public ZenPolicy zenPolicy;

        public ZenRule(Parcel source) {
            boolean z = false;
            this.enabled = source.readInt() == 1;
            this.snoozing = source.readInt() == 1;
            if (source.readInt() == 1) {
                this.name = source.readString();
            }
            this.zenMode = source.readInt();
            this.conditionId = (Uri) source.readParcelable(null);
            this.condition = (Condition) source.readParcelable(null);
            this.component = (ComponentName) source.readParcelable(null);
            this.configurationActivity = (ComponentName) source.readParcelable(null);
            if (source.readInt() == 1) {
                this.id = source.readString();
            }
            this.creationTime = source.readLong();
            if (source.readInt() == 1) {
                this.enabler = source.readString();
            }
            this.zenPolicy = (ZenPolicy) source.readParcelable(null);
            if (source.readInt() == 1) {
                z = true;
            }
            this.modified = z;
            this.pkg = source.readString();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.enabled);
            dest.writeInt(this.snoozing);
            if (this.name != null) {
                dest.writeInt(1);
                dest.writeString(this.name);
            } else {
                dest.writeInt(0);
            }
            dest.writeInt(this.zenMode);
            dest.writeParcelable(this.conditionId, 0);
            dest.writeParcelable(this.condition, 0);
            dest.writeParcelable(this.component, 0);
            dest.writeParcelable(this.configurationActivity, 0);
            if (this.id != null) {
                dest.writeInt(1);
                dest.writeString(this.id);
            } else {
                dest.writeInt(0);
            }
            dest.writeLong(this.creationTime);
            if (this.enabler != null) {
                dest.writeInt(1);
                dest.writeString(this.enabler);
            } else {
                dest.writeInt(0);
            }
            dest.writeParcelable(this.zenPolicy, 0);
            dest.writeInt(this.modified);
            dest.writeString(this.pkg);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(ZenRule.class.getSimpleName());
            stringBuilder.append('[');
            stringBuilder.append("id=");
            stringBuilder.append(this.id);
            stringBuilder.append(",enabled=");
            stringBuilder.append(String.valueOf(this.enabled).toUpperCase());
            stringBuilder.append(",snoozing=");
            stringBuilder.append(this.snoozing);
            stringBuilder.append(",name=");
            stringBuilder.append(this.name);
            stringBuilder.append(",zenMode=");
            stringBuilder.append(Global.zenModeToString(this.zenMode));
            stringBuilder.append(",conditionId=");
            stringBuilder.append(this.conditionId);
            stringBuilder.append(",condition=");
            stringBuilder.append(this.condition);
            stringBuilder.append(",pkg=");
            stringBuilder.append(this.pkg);
            stringBuilder.append(",component=");
            stringBuilder.append(this.component);
            stringBuilder.append(",configActivity=");
            stringBuilder.append(this.configurationActivity);
            stringBuilder.append(",creationTime=");
            stringBuilder.append(this.creationTime);
            stringBuilder.append(",enabler=");
            stringBuilder.append(this.enabler);
            stringBuilder.append(",zenPolicy=");
            stringBuilder.append(this.zenPolicy);
            stringBuilder.append(",modified=");
            stringBuilder.append(this.modified);
            stringBuilder.append(']');
            return stringBuilder.toString();
        }

        public void writeToProto(ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1138166333441L, this.id);
            proto.write(1138166333442L, this.name);
            proto.write(1112396529667L, this.creationTime);
            proto.write(1133871366148L, this.enabled);
            proto.write(1138166333445L, this.enabler);
            proto.write(1133871366150L, this.snoozing);
            proto.write(1159641169927L, this.zenMode);
            Uri uri = this.conditionId;
            if (uri != null) {
                proto.write(1138166333448L, uri.toString());
            }
            Condition condition = this.condition;
            if (condition != null) {
                condition.writeToProto(proto, 1146756268041L);
            }
            ComponentName componentName = this.component;
            if (componentName != null) {
                componentName.writeToProto(proto, 1146756268042L);
            }
            ZenPolicy zenPolicy = this.zenPolicy;
            if (zenPolicy != null) {
                zenPolicy.writeToProto(proto, 1146756268043L);
            }
            proto.write(1133871366156L, this.modified);
            proto.end(token);
        }

        private static void appendDiff(Diff d, String item, ZenRule from, ZenRule to) {
            if (d != null) {
                if (from == null) {
                    if (to != null) {
                        d.addLine(item, "insert");
                    }
                    return;
                }
                from.appendDiff(d, item, to);
            }
        }

        private void appendDiff(Diff d, String item, ZenRule to) {
            if (to == null) {
                d.addLine(item, "delete");
                return;
            }
            boolean z = this.enabled;
            if (z != to.enabled) {
                d.addLine(item, "enabled", Boolean.valueOf(z), Boolean.valueOf(to.enabled));
            }
            z = this.snoozing;
            if (z != to.snoozing) {
                d.addLine(item, ZenModeConfig.RULE_ATT_SNOOZING, Boolean.valueOf(z), Boolean.valueOf(to.snoozing));
            }
            if (!Objects.equals(this.name, to.name)) {
                d.addLine(item, "name", this.name, to.name);
            }
            int i = this.zenMode;
            if (i != to.zenMode) {
                d.addLine(item, "zenMode", Integer.valueOf(i), Integer.valueOf(to.zenMode));
            }
            if (!Objects.equals(this.conditionId, to.conditionId)) {
                d.addLine(item, ZenModeConfig.RULE_ATT_CONDITION_ID, this.conditionId, to.conditionId);
            }
            if (!Objects.equals(this.condition, to.condition)) {
                d.addLine(item, Condition.SCHEME, this.condition, to.condition);
            }
            if (!Objects.equals(this.component, to.component)) {
                d.addLine(item, "component", this.component, to.component);
            }
            if (!Objects.equals(this.configurationActivity, to.configurationActivity)) {
                d.addLine(item, ZenModeConfig.RULE_ATT_CONFIG_ACTIVITY, this.configurationActivity, to.configurationActivity);
            }
            if (!Objects.equals(this.id, to.id)) {
                d.addLine(item, "id", this.id, to.id);
            }
            long j = this.creationTime;
            if (j != to.creationTime) {
                d.addLine(item, "creationTime", Long.valueOf(j), Long.valueOf(to.creationTime));
            }
            if (!Objects.equals(this.enabler, to.enabler)) {
                d.addLine(item, ZenModeConfig.RULE_ATT_ENABLER, this.enabler, to.enabler);
            }
            if (!Objects.equals(this.zenPolicy, to.zenPolicy)) {
                d.addLine(item, "zenPolicy", this.zenPolicy, to.zenPolicy);
            }
            z = this.modified;
            if (z != to.modified) {
                d.addLine(item, "modified", Boolean.valueOf(z), Boolean.valueOf(to.modified));
            }
            String str = this.pkg;
            String str2 = to.pkg;
            if (str != str2) {
                d.addLine(item, "pkg", str, str2);
            }
        }

        public boolean equals(Object o) {
            if (!(o instanceof ZenRule)) {
                return false;
            }
            boolean z = true;
            if (o == this) {
                return true;
            }
            ZenRule other = (ZenRule) o;
            if (!(other.enabled == this.enabled && other.snoozing == this.snoozing && Objects.equals(other.name, this.name) && other.zenMode == this.zenMode && Objects.equals(other.conditionId, this.conditionId) && Objects.equals(other.condition, this.condition) && Objects.equals(other.component, this.component) && Objects.equals(other.configurationActivity, this.configurationActivity) && Objects.equals(other.id, this.id) && Objects.equals(other.enabler, this.enabler) && Objects.equals(other.zenPolicy, this.zenPolicy) && Objects.equals(other.pkg, this.pkg) && other.modified == this.modified)) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{Boolean.valueOf(this.enabled), Boolean.valueOf(this.snoozing), this.name, Integer.valueOf(this.zenMode), this.conditionId, this.condition, this.component, this.configurationActivity, this.pkg, this.id, this.enabler, this.zenPolicy, Boolean.valueOf(this.modified)});
        }

        public boolean isAutomaticActive() {
            return this.enabled && !this.snoozing && this.pkg != null && isTrueOrUnknown();
        }

        public boolean isTrueOrUnknown() {
            Condition condition = this.condition;
            return condition != null && (condition.state == 1 || this.condition.state == 2);
        }
    }

    public ZenModeConfig(Parcel source) {
        boolean z = true;
        this.allowCalls = source.readInt() == 1;
        this.allowRepeatCallers = source.readInt() == 1;
        this.allowMessages = source.readInt() == 1;
        this.allowReminders = source.readInt() == 1;
        this.allowEvents = source.readInt() == 1;
        this.allowCallsFrom = source.readInt();
        this.allowMessagesFrom = source.readInt();
        this.user = source.readInt();
        this.manualRule = (ZenRule) source.readParcelable(null);
        int len = source.readInt();
        if (len > 0) {
            String[] ids = new String[len];
            ZenRule[] rules = new ZenRule[len];
            source.readStringArray(ids);
            source.readTypedArray(rules, ZenRule.CREATOR);
            for (int i = 0; i < len; i++) {
                this.automaticRules.put(ids[i], rules[i]);
            }
        }
        this.allowAlarms = source.readInt() == 1;
        this.allowMedia = source.readInt() == 1;
        this.allowSystem = source.readInt() == 1;
        this.suppressedVisualEffects = source.readInt();
        if (source.readInt() != 1) {
            z = false;
        }
        this.areChannelsBypassingDnd = z;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.allowCalls);
        dest.writeInt(this.allowRepeatCallers);
        dest.writeInt(this.allowMessages);
        dest.writeInt(this.allowReminders);
        dest.writeInt(this.allowEvents);
        dest.writeInt(this.allowCallsFrom);
        dest.writeInt(this.allowMessagesFrom);
        dest.writeInt(this.user);
        dest.writeParcelable(this.manualRule, 0);
        if (this.automaticRules.isEmpty()) {
            dest.writeInt(0);
        } else {
            int len = this.automaticRules.size();
            String[] ids = new String[len];
            ZenRule[] rules = new ZenRule[len];
            for (int i = 0; i < len; i++) {
                ids[i] = (String) this.automaticRules.keyAt(i);
                rules[i] = (ZenRule) this.automaticRules.valueAt(i);
            }
            dest.writeInt(len);
            dest.writeStringArray(ids);
            dest.writeTypedArray(rules, 0);
        }
        dest.writeInt(this.allowAlarms);
        dest.writeInt(this.allowMedia);
        dest.writeInt(this.allowSystem);
        dest.writeInt(this.suppressedVisualEffects);
        dest.writeInt(this.areChannelsBypassingDnd);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(ZenModeConfig.class.getSimpleName());
        stringBuilder.append('[');
        stringBuilder.append("user=");
        stringBuilder.append(this.user);
        stringBuilder.append(",allowAlarms=");
        stringBuilder.append(this.allowAlarms);
        stringBuilder.append(",allowMedia=");
        stringBuilder.append(this.allowMedia);
        stringBuilder.append(",allowSystem=");
        stringBuilder.append(this.allowSystem);
        stringBuilder.append(",allowReminders=");
        stringBuilder.append(this.allowReminders);
        stringBuilder.append(",allowEvents=");
        stringBuilder.append(this.allowEvents);
        stringBuilder.append(",allowCalls=");
        stringBuilder.append(this.allowCalls);
        stringBuilder.append(",allowRepeatCallers=");
        stringBuilder.append(this.allowRepeatCallers);
        stringBuilder.append(",allowMessages=");
        stringBuilder.append(this.allowMessages);
        stringBuilder.append(",allowCallsFrom=");
        stringBuilder.append(sourceToString(this.allowCallsFrom));
        stringBuilder.append(",allowMessagesFrom=");
        stringBuilder.append(sourceToString(this.allowMessagesFrom));
        stringBuilder.append(",suppressedVisualEffects=");
        stringBuilder.append(this.suppressedVisualEffects);
        stringBuilder.append(",areChannelsBypassingDnd=");
        stringBuilder.append(this.areChannelsBypassingDnd);
        stringBuilder.append(",\nautomaticRules=");
        stringBuilder.append(rulesToString());
        stringBuilder.append(",\nmanualRule=");
        stringBuilder.append(this.manualRule);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    private String rulesToString() {
        if (this.automaticRules.isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(this.automaticRules.size() * 28);
        buffer.append('{');
        for (int i = 0; i < this.automaticRules.size(); i++) {
            if (i > 0) {
                buffer.append(",\n");
            }
            buffer.append(this.automaticRules.valueAt(i));
        }
        buffer.append('}');
        return buffer.toString();
    }

    public Diff diff(ZenModeConfig to) {
        Diff d = new Diff();
        if (to == null) {
            return d.addLine(MiuiWifiManager.EXTRA_CONFIG, "delete");
        }
        int i = this.user;
        if (i != to.user) {
            d.addLine("user", Integer.valueOf(i), Integer.valueOf(to.user));
        }
        boolean z = this.allowAlarms;
        if (z != to.allowAlarms) {
            d.addLine("allowAlarms", Boolean.valueOf(z), Boolean.valueOf(to.allowAlarms));
        }
        z = this.allowMedia;
        if (z != to.allowMedia) {
            d.addLine("allowMedia", Boolean.valueOf(z), Boolean.valueOf(to.allowMedia));
        }
        z = this.allowSystem;
        if (z != to.allowSystem) {
            d.addLine("allowSystem", Boolean.valueOf(z), Boolean.valueOf(to.allowSystem));
        }
        z = this.allowCalls;
        if (z != to.allowCalls) {
            d.addLine("allowCalls", Boolean.valueOf(z), Boolean.valueOf(to.allowCalls));
        }
        z = this.allowReminders;
        if (z != to.allowReminders) {
            d.addLine("allowReminders", Boolean.valueOf(z), Boolean.valueOf(to.allowReminders));
        }
        z = this.allowEvents;
        if (z != to.allowEvents) {
            d.addLine("allowEvents", Boolean.valueOf(z), Boolean.valueOf(to.allowEvents));
        }
        z = this.allowRepeatCallers;
        if (z != to.allowRepeatCallers) {
            d.addLine("allowRepeatCallers", Boolean.valueOf(z), Boolean.valueOf(to.allowRepeatCallers));
        }
        z = this.allowMessages;
        if (z != to.allowMessages) {
            d.addLine("allowMessages", Boolean.valueOf(z), Boolean.valueOf(to.allowMessages));
        }
        i = this.allowCallsFrom;
        if (i != to.allowCallsFrom) {
            d.addLine("allowCallsFrom", Integer.valueOf(i), Integer.valueOf(to.allowCallsFrom));
        }
        i = this.allowMessagesFrom;
        if (i != to.allowMessagesFrom) {
            d.addLine("allowMessagesFrom", Integer.valueOf(i), Integer.valueOf(to.allowMessagesFrom));
        }
        i = this.suppressedVisualEffects;
        if (i != to.suppressedVisualEffects) {
            d.addLine("suppressedVisualEffects", Integer.valueOf(i), Integer.valueOf(to.suppressedVisualEffects));
        }
        ArraySet<String> allRules = new ArraySet();
        addKeys(allRules, this.automaticRules);
        addKeys(allRules, to.automaticRules);
        int N = allRules.size();
        for (int i2 = 0; i2 < N; i2++) {
            String rule = (String) allRules.valueAt(i2);
            ArrayMap arrayMap = this.automaticRules;
            ZenRule toRule = null;
            ZenRule fromRule = arrayMap != null ? (ZenRule) arrayMap.get(rule) : null;
            ArrayMap arrayMap2 = to.automaticRules;
            if (arrayMap2 != null) {
                toRule = (ZenRule) arrayMap2.get(rule);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("automaticRule[");
            stringBuilder.append(rule);
            stringBuilder.append("]");
            ZenRule.appendDiff(d, stringBuilder.toString(), fromRule, toRule);
        }
        ZenRule.appendDiff(d, "manualRule", this.manualRule, to.manualRule);
        boolean z2 = this.areChannelsBypassingDnd;
        if (z2 != to.areChannelsBypassingDnd) {
            d.addLine(STATE_ATT_CHANNELS_BYPASSING_DND, Boolean.valueOf(z2), Boolean.valueOf(to.areChannelsBypassingDnd));
        }
        return d;
    }

    public static Diff diff(ZenModeConfig from, ZenModeConfig to) {
        if (from != null) {
            return from.diff(to);
        }
        Diff d = new Diff();
        if (to != null) {
            d.addLine(MiuiWifiManager.EXTRA_CONFIG, "insert");
        }
        return d;
    }

    private static <T> void addKeys(ArraySet<T> set, ArrayMap<T, ?> map) {
        if (map != null) {
            for (int i = 0; i < map.size(); i++) {
                set.add(map.keyAt(i));
            }
        }
    }

    public boolean isValid() {
        if (!isValidManualRule(this.manualRule)) {
            return false;
        }
        int N = this.automaticRules.size();
        for (int i = 0; i < N; i++) {
            if (!isValidAutomaticRule((ZenRule) this.automaticRules.valueAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidManualRule(ZenRule rule) {
        return rule == null || (Global.isValidZenMode(rule.zenMode) && sameCondition(rule));
    }

    private static boolean isValidAutomaticRule(ZenRule rule) {
        return (rule == null || TextUtils.isEmpty(rule.name) || !Global.isValidZenMode(rule.zenMode) || rule.conditionId == null || !sameCondition(rule)) ? false : true;
    }

    private static boolean sameCondition(ZenRule rule) {
        boolean z = false;
        if (rule == null) {
            return false;
        }
        if (rule.conditionId == null) {
            if (rule.condition == null) {
                z = true;
            }
            return z;
        }
        if (rule.condition == null || rule.conditionId.equals(rule.condition.id)) {
            z = true;
        }
        return z;
    }

    private static int[] generateMinuteBuckets() {
        int[] buckets = new int[15];
        buckets[0] = 15;
        buckets[1] = 30;
        buckets[2] = 45;
        for (int i = 1; i <= 12; i++) {
            buckets[i + 2] = i * 60;
        }
        return buckets;
    }

    public static String sourceToString(int source) {
        if (source == 0) {
            return "anyone";
        }
        if (source == 1) {
            return Contacts.AUTHORITY;
        }
        if (source != 2) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        return "stars";
    }

    public boolean equals(Object o) {
        if (!(o instanceof ZenModeConfig)) {
            return false;
        }
        boolean z = true;
        if (o == this) {
            return true;
        }
        ZenModeConfig other = (ZenModeConfig) o;
        if (!(other.allowAlarms == this.allowAlarms && other.allowMedia == this.allowMedia && other.allowSystem == this.allowSystem && other.allowCalls == this.allowCalls && other.allowRepeatCallers == this.allowRepeatCallers && other.allowMessages == this.allowMessages && other.allowCallsFrom == this.allowCallsFrom && other.allowMessagesFrom == this.allowMessagesFrom && other.allowReminders == this.allowReminders && other.allowEvents == this.allowEvents && other.user == this.user && Objects.equals(other.automaticRules, this.automaticRules) && Objects.equals(other.manualRule, this.manualRule) && other.suppressedVisualEffects == this.suppressedVisualEffects && other.areChannelsBypassingDnd == this.areChannelsBypassingDnd)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Boolean.valueOf(this.allowAlarms), Boolean.valueOf(this.allowMedia), Boolean.valueOf(this.allowSystem), Boolean.valueOf(this.allowCalls), Boolean.valueOf(this.allowRepeatCallers), Boolean.valueOf(this.allowMessages), Integer.valueOf(this.allowCallsFrom), Integer.valueOf(this.allowMessagesFrom), Boolean.valueOf(this.allowReminders), Boolean.valueOf(this.allowEvents), Integer.valueOf(this.user), this.automaticRules, this.manualRule, Integer.valueOf(this.suppressedVisualEffects), Boolean.valueOf(this.areChannelsBypassingDnd)});
    }

    private static String toDayList(int[] days) {
        if (days == null || days.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < days.length; i++) {
            if (i > 0) {
                sb.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            }
            sb.append(days[i]);
        }
        return sb.toString();
    }

    private static int[] tryParseDayList(String dayList, String sep) {
        if (dayList == null) {
            return null;
        }
        String[] tokens = dayList.split(sep);
        if (tokens.length == 0) {
            return null;
        }
        int[] rt = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            int day = tryParseInt(tokens[i], -1);
            if (day == -1) {
                return null;
            }
            rt[i] = day;
        }
        return rt;
    }

    private static int tryParseInt(String value, int defValue) {
        if (TextUtils.isEmpty(value)) {
            return defValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    private static long tryParseLong(String value, long defValue) {
        if (TextUtils.isEmpty(value)) {
            return defValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    private static Long tryParseLong(String value, Long defValue) {
        if (TextUtils.isEmpty(value)) {
            return defValue;
        }
        try {
            return Long.valueOf(Long.parseLong(value));
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    public static ZenModeConfig readXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != 2) {
            return null;
        }
        String str = "zen";
        if (!str.equals(parser.getName())) {
            return null;
        }
        ZenModeConfig rt = new ZenModeConfig();
        rt.version = safeInt(parser, "version", 8);
        rt.user = safeInt(parser, "user", rt.user);
        boolean readSuppressedEffects = false;
        while (true) {
            int next = parser.next();
            int type = next;
            if (next != 1) {
                String tag = parser.getName();
                if (type == 3 && str.equals(tag)) {
                    return rt;
                }
                if (type == 2) {
                    if (ALLOW_TAG.equals(tag)) {
                        String str2;
                        StringBuilder stringBuilder;
                        rt.allowCalls = safeBoolean(parser, ALLOW_ATT_CALLS, true);
                        rt.allowRepeatCallers = safeBoolean(parser, ALLOW_ATT_REPEAT_CALLERS, true);
                        rt.allowMessages = safeBoolean(parser, ALLOW_ATT_MESSAGES, false);
                        rt.allowReminders = safeBoolean(parser, ALLOW_ATT_REMINDERS, false);
                        rt.allowEvents = safeBoolean(parser, ALLOW_ATT_EVENTS, false);
                        int from = safeInt(parser, ALLOW_ATT_FROM, -1);
                        int callsFrom = safeInt(parser, ALLOW_ATT_CALLS_FROM, -1);
                        next = safeInt(parser, ALLOW_ATT_MESSAGES_FROM, -1);
                        if (isValidSource(callsFrom) && isValidSource(next)) {
                            rt.allowCallsFrom = callsFrom;
                            rt.allowMessagesFrom = next;
                        } else if (isValidSource(from)) {
                            str2 = TAG;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Migrating existing shared 'from': ");
                            stringBuilder.append(sourceToString(from));
                            Slog.i(str2, stringBuilder.toString());
                            rt.allowCallsFrom = from;
                            rt.allowMessagesFrom = from;
                        } else {
                            rt.allowCallsFrom = 2;
                            rt.allowMessagesFrom = 1;
                        }
                        rt.allowAlarms = safeBoolean(parser, ALLOW_ATT_ALARMS, true);
                        rt.allowMedia = safeBoolean(parser, "media", true);
                        rt.allowSystem = safeBoolean(parser, "system", false);
                        Boolean allowWhenScreenOff = unsafeBoolean(parser, ALLOW_ATT_SCREEN_OFF);
                        if (allowWhenScreenOff != null) {
                            readSuppressedEffects = true;
                            if (!allowWhenScreenOff.booleanValue()) {
                                rt.suppressedVisualEffects |= 12;
                            }
                        }
                        Boolean allowWhenScreenOn = unsafeBoolean(parser, ALLOW_ATT_SCREEN_ON);
                        if (allowWhenScreenOn != null) {
                            readSuppressedEffects = true;
                            if (!allowWhenScreenOn.booleanValue()) {
                                rt.suppressedVisualEffects |= 16;
                            }
                        }
                        if (readSuppressedEffects) {
                            str2 = TAG;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Migrated visual effects to ");
                            stringBuilder.append(rt.suppressedVisualEffects);
                            Slog.d(str2, stringBuilder.toString());
                        }
                    } else if (DISALLOW_TAG.equals(tag) && !readSuppressedEffects) {
                        rt.suppressedVisualEffects = safeInt(parser, DISALLOW_ATT_VISUAL_EFFECTS, 0);
                    } else if ("manual".equals(tag)) {
                        rt.manualRule = readRuleXml(parser);
                    } else if (AUTOMATIC_TAG.equals(tag)) {
                        String id = parser.getAttributeValue(null, RULE_ATT_ID);
                        ZenRule automaticRule = readRuleXml(parser);
                        if (!(id == null || automaticRule == null)) {
                            automaticRule.id = id;
                            rt.automaticRules.put(id, automaticRule);
                        }
                    } else if ("state".equals(tag)) {
                        rt.areChannelsBypassingDnd = safeBoolean(parser, STATE_ATT_CHANNELS_BYPASSING_DND, false);
                    }
                }
            } else {
                throw new IllegalStateException("Failed to reach END_DOCUMENT");
            }
        }
    }

    public void writeXml(XmlSerializer out, Integer version) throws IOException {
        String str = "zen";
        out.startTag(null, str);
        out.attribute(null, "version", Integer.toString(version == null ? 8 : version.intValue()));
        out.attribute(null, "user", Integer.toString(this.user));
        String str2 = ALLOW_TAG;
        out.startTag(null, str2);
        out.attribute(null, ALLOW_ATT_CALLS, Boolean.toString(this.allowCalls));
        out.attribute(null, ALLOW_ATT_REPEAT_CALLERS, Boolean.toString(this.allowRepeatCallers));
        out.attribute(null, ALLOW_ATT_MESSAGES, Boolean.toString(this.allowMessages));
        out.attribute(null, ALLOW_ATT_REMINDERS, Boolean.toString(this.allowReminders));
        out.attribute(null, ALLOW_ATT_EVENTS, Boolean.toString(this.allowEvents));
        out.attribute(null, ALLOW_ATT_CALLS_FROM, Integer.toString(this.allowCallsFrom));
        out.attribute(null, ALLOW_ATT_MESSAGES_FROM, Integer.toString(this.allowMessagesFrom));
        out.attribute(null, ALLOW_ATT_ALARMS, Boolean.toString(this.allowAlarms));
        out.attribute(null, "media", Boolean.toString(this.allowMedia));
        out.attribute(null, "system", Boolean.toString(this.allowSystem));
        out.endTag(null, str2);
        str2 = DISALLOW_TAG;
        out.startTag(null, str2);
        out.attribute(null, DISALLOW_ATT_VISUAL_EFFECTS, Integer.toString(this.suppressedVisualEffects));
        out.endTag(null, str2);
        if (this.manualRule != null) {
            str2 = "manual";
            out.startTag(null, str2);
            writeRuleXml(this.manualRule, out);
            out.endTag(null, str2);
        }
        int N = this.automaticRules.size();
        for (int i = 0; i < N; i++) {
            String id = (String) this.automaticRules.keyAt(i);
            ZenRule automaticRule = (ZenRule) this.automaticRules.valueAt(i);
            String str3 = AUTOMATIC_TAG;
            out.startTag(null, str3);
            out.attribute(null, RULE_ATT_ID, id);
            writeRuleXml(automaticRule, out);
            out.endTag(null, str3);
        }
        String str4 = "state";
        out.startTag(null, str4);
        out.attribute(null, STATE_ATT_CHANNELS_BYPASSING_DND, Boolean.toString(this.areChannelsBypassingDnd));
        out.endTag(null, str4);
        out.endTag(null, str);
    }

    public static ZenRule readRuleXml(XmlPullParser parser) {
        ZenRule rt = new ZenRule();
        rt.enabled = safeBoolean(parser, "enabled", true);
        rt.name = parser.getAttributeValue(null, "name");
        String zen = parser.getAttributeValue(null, "zen");
        rt.zenMode = tryParseZenMode(zen, -1);
        StringBuilder stringBuilder;
        if (rt.zenMode == -1) {
            String str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Bad zen mode in rule xml:");
            stringBuilder.append(zen);
            Slog.w(str, stringBuilder.toString());
            return null;
        }
        String packageName;
        rt.conditionId = safeUri(parser, RULE_ATT_CONDITION_ID);
        rt.component = safeComponentName(parser, "component");
        rt.configurationActivity = safeComponentName(parser, RULE_ATT_CONFIG_ACTIVITY);
        if (rt.component != null) {
            packageName = rt.component.getPackageName();
        } else if (rt.configurationActivity != null) {
            packageName = rt.configurationActivity.getPackageName();
        } else {
            packageName = null;
        }
        rt.pkg = packageName;
        rt.creationTime = safeLong(parser, "creationTime", 0);
        rt.enabler = parser.getAttributeValue(null, RULE_ATT_ENABLER);
        rt.condition = readConditionXml(parser);
        if (!(rt.zenMode == 1 || rt.zenMode == 4 || !Condition.isValidId(rt.conditionId, "android"))) {
            String str2 = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Updating zenMode of automatic rule ");
            stringBuilder.append(rt.name);
            Slog.i(str2, stringBuilder.toString());
            rt.zenMode = 1;
        }
        rt.modified = safeBoolean(parser, "modified", false);
        rt.zenPolicy = readZenPolicyXml(parser);
        return rt;
    }

    public static void writeRuleXml(ZenRule rule, XmlSerializer out) throws IOException {
        out.attribute(null, "enabled", Boolean.toString(rule.enabled));
        if (rule.name != null) {
            out.attribute(null, "name", rule.name);
        }
        out.attribute(null, "zen", Integer.toString(rule.zenMode));
        if (rule.component != null) {
            out.attribute(null, "component", rule.component.flattenToString());
        }
        if (rule.configurationActivity != null) {
            out.attribute(null, RULE_ATT_CONFIG_ACTIVITY, rule.configurationActivity.flattenToString());
        }
        if (rule.conditionId != null) {
            out.attribute(null, RULE_ATT_CONDITION_ID, rule.conditionId.toString());
        }
        out.attribute(null, "creationTime", Long.toString(rule.creationTime));
        if (rule.enabler != null) {
            out.attribute(null, RULE_ATT_ENABLER, rule.enabler);
        }
        if (rule.condition != null) {
            writeConditionXml(rule.condition, out);
        }
        if (rule.zenPolicy != null) {
            writeZenPolicyXml(rule.zenPolicy, out);
        }
        out.attribute(null, "modified", Boolean.toString(rule.modified));
    }

    public static Condition readConditionXml(XmlPullParser parser) {
        XmlPullParser xmlPullParser = parser;
        Uri id = safeUri(xmlPullParser, "id");
        if (id == null) {
            return null;
        }
        try {
            return new Condition(id, xmlPullParser.getAttributeValue(null, "summary"), xmlPullParser.getAttributeValue(null, CONDITION_ATT_LINE1), xmlPullParser.getAttributeValue(null, CONDITION_ATT_LINE2), safeInt(xmlPullParser, "icon", -1), safeInt(xmlPullParser, "state", -1), safeInt(xmlPullParser, "flags", -1));
        } catch (IllegalArgumentException e) {
            Slog.w(TAG, "Unable to read condition xml", e);
            return null;
        }
    }

    public static void writeConditionXml(Condition c, XmlSerializer out) throws IOException {
        out.attribute(null, "id", c.id.toString());
        out.attribute(null, "summary", c.summary);
        out.attribute(null, CONDITION_ATT_LINE1, c.line1);
        out.attribute(null, CONDITION_ATT_LINE2, c.line2);
        out.attribute(null, "icon", Integer.toString(c.icon));
        out.attribute(null, "state", Integer.toString(c.state));
        out.attribute(null, "flags", Integer.toString(c.flags));
    }

    public static ZenPolicy readZenPolicyXml(XmlPullParser parser) {
        XmlPullParser xmlPullParser = parser;
        boolean policySet = false;
        Builder builder = new Builder();
        int calls = safeInt(xmlPullParser, ALLOW_ATT_CALLS_FROM, 0);
        int messages = safeInt(xmlPullParser, ALLOW_ATT_MESSAGES_FROM, 0);
        int repeatCallers = safeInt(xmlPullParser, ALLOW_ATT_REPEAT_CALLERS, 0);
        int alarms = safeInt(xmlPullParser, ALLOW_ATT_ALARMS, 0);
        int media = safeInt(xmlPullParser, "media", 0);
        int system = safeInt(xmlPullParser, "system", 0);
        int events = safeInt(xmlPullParser, ALLOW_ATT_EVENTS, 0);
        int reminders = safeInt(xmlPullParser, ALLOW_ATT_REMINDERS, 0);
        if (calls != 0) {
            builder.allowCalls(calls);
            policySet = true;
        }
        if (messages != 0) {
            builder.allowMessages(messages);
            policySet = true;
        }
        if (repeatCallers != 0) {
            builder.allowRepeatCallers(repeatCallers == 1);
            policySet = true;
        }
        if (alarms != 0) {
            builder.allowAlarms(alarms == 1);
            policySet = true;
        }
        if (media != 0) {
            builder.allowMedia(media == 1);
            policySet = true;
        }
        if (system != 0) {
            builder.allowSystem(system == 1);
            policySet = true;
        }
        if (events != 0) {
            builder.allowEvents(events == 1);
            policySet = true;
        }
        if (reminders != 0) {
            builder.allowReminders(reminders == 1);
            policySet = true;
        }
        int fullScreenIntent = safeInt(xmlPullParser, SHOW_ATT_FULL_SCREEN_INTENT, 0);
        int lights = safeInt(xmlPullParser, SHOW_ATT_LIGHTS, 0);
        int peek = safeInt(xmlPullParser, SHOW_ATT_PEEK, 0);
        int statusBar = safeInt(xmlPullParser, SHOW_ATT_STATUS_BAR_ICONS, 0);
        boolean policySet2 = policySet;
        int badges = safeInt(xmlPullParser, SHOW_ATT_BADGES, 0);
        calls = safeInt(xmlPullParser, SHOW_ATT_AMBIENT, 0);
        messages = safeInt(xmlPullParser, SHOW_ATT_NOTIFICATION_LIST, 0);
        if (fullScreenIntent != 0) {
            builder.showFullScreenIntent(fullScreenIntent == 1);
            policySet2 = true;
        }
        if (lights != 0) {
            builder.showLights(lights == 1);
            policySet2 = true;
        }
        if (peek != 0) {
            builder.showPeeking(peek == 1);
            policySet2 = true;
        }
        if (statusBar != 0) {
            builder.showStatusBarIcons(statusBar == 1);
            policySet2 = true;
        }
        if (badges != 0) {
            builder.showBadges(badges == 1);
            policySet2 = true;
        }
        if (calls != 0) {
            builder.showInAmbientDisplay(calls == 1);
            policySet2 = true;
        }
        if (messages != 0) {
            boolean z = true;
            if (messages != 1) {
                z = false;
            }
            builder.showInNotificationList(z);
            policySet2 = true;
        }
        if (policySet2) {
            return builder.build();
        }
        return null;
    }

    public static void writeZenPolicyXml(ZenPolicy policy, XmlSerializer out) throws IOException {
        writeZenPolicyState(ALLOW_ATT_CALLS_FROM, policy.getPriorityCallSenders(), out);
        writeZenPolicyState(ALLOW_ATT_MESSAGES_FROM, policy.getPriorityMessageSenders(), out);
        writeZenPolicyState(ALLOW_ATT_REPEAT_CALLERS, policy.getPriorityCategoryRepeatCallers(), out);
        writeZenPolicyState(ALLOW_ATT_ALARMS, policy.getPriorityCategoryAlarms(), out);
        writeZenPolicyState("media", policy.getPriorityCategoryMedia(), out);
        writeZenPolicyState("system", policy.getPriorityCategorySystem(), out);
        writeZenPolicyState(ALLOW_ATT_REMINDERS, policy.getPriorityCategoryReminders(), out);
        writeZenPolicyState(ALLOW_ATT_EVENTS, policy.getPriorityCategoryEvents(), out);
        writeZenPolicyState(SHOW_ATT_FULL_SCREEN_INTENT, policy.getVisualEffectFullScreenIntent(), out);
        writeZenPolicyState(SHOW_ATT_LIGHTS, policy.getVisualEffectLights(), out);
        writeZenPolicyState(SHOW_ATT_PEEK, policy.getVisualEffectPeek(), out);
        writeZenPolicyState(SHOW_ATT_STATUS_BAR_ICONS, policy.getVisualEffectStatusBar(), out);
        writeZenPolicyState(SHOW_ATT_BADGES, policy.getVisualEffectBadge(), out);
        writeZenPolicyState(SHOW_ATT_AMBIENT, policy.getVisualEffectAmbient(), out);
        writeZenPolicyState(SHOW_ATT_NOTIFICATION_LIST, policy.getVisualEffectNotificationList(), out);
    }

    private static void writeZenPolicyState(String attr, int val, XmlSerializer out) throws IOException {
        if (Objects.equals(attr, ALLOW_ATT_CALLS_FROM) || Objects.equals(attr, ALLOW_ATT_MESSAGES_FROM)) {
            if (val != 0) {
                out.attribute(null, attr, Integer.toString(val));
            }
        } else if (val != 0) {
            out.attribute(null, attr, Integer.toString(val));
        }
    }

    public static boolean isValidHour(int val) {
        return val >= 0 && val < 24;
    }

    public static boolean isValidMinute(int val) {
        return val >= 0 && val < 60;
    }

    private static boolean isValidSource(int source) {
        return source >= 0 && source <= 2;
    }

    private static Boolean unsafeBoolean(XmlPullParser parser, String att) {
        String val = parser.getAttributeValue(null, att);
        if (TextUtils.isEmpty(val)) {
            return null;
        }
        return Boolean.valueOf(Boolean.parseBoolean(val));
    }

    private static boolean safeBoolean(XmlPullParser parser, String att, boolean defValue) {
        return safeBoolean(parser.getAttributeValue(null, att), defValue);
    }

    private static boolean safeBoolean(String val, boolean defValue) {
        if (TextUtils.isEmpty(val)) {
            return defValue;
        }
        return Boolean.parseBoolean(val);
    }

    private static int safeInt(XmlPullParser parser, String att, int defValue) {
        return tryParseInt(parser.getAttributeValue(null, att), defValue);
    }

    private static ComponentName safeComponentName(XmlPullParser parser, String att) {
        String val = parser.getAttributeValue(null, att);
        if (TextUtils.isEmpty(val)) {
            return null;
        }
        return ComponentName.unflattenFromString(val);
    }

    private static Uri safeUri(XmlPullParser parser, String att) {
        String val = parser.getAttributeValue(null, att);
        if (TextUtils.isEmpty(val)) {
            return null;
        }
        return Uri.parse(val);
    }

    private static long safeLong(XmlPullParser parser, String att, long defValue) {
        return tryParseLong(parser.getAttributeValue(null, att), defValue);
    }

    public int describeContents() {
        return 0;
    }

    public ZenModeConfig copy() {
        Parcel parcel = Parcel.obtain();
        try {
            writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            ZenModeConfig zenModeConfig = new ZenModeConfig(parcel);
            return zenModeConfig;
        } finally {
            parcel.recycle();
        }
    }

    public Policy toNotificationPolicy(ZenPolicy zenPolicy) {
        ZenPolicy zenPolicy2 = zenPolicy;
        Policy defaultPolicy = toNotificationPolicy();
        int priorityCategories = 0;
        int suppressedVisualEffects = 0;
        int callSenders = defaultPolicy.priorityCallSenders;
        int messageSenders = defaultPolicy.priorityMessageSenders;
        if (zenPolicy2.isCategoryAllowed(0, isPriorityCategoryEnabled(1, defaultPolicy))) {
            priorityCategories = 0 | 1;
        }
        if (zenPolicy2.isCategoryAllowed(1, isPriorityCategoryEnabled(2, defaultPolicy))) {
            priorityCategories |= 2;
        }
        if (zenPolicy2.isCategoryAllowed(2, isPriorityCategoryEnabled(4, defaultPolicy))) {
            priorityCategories |= 4;
            messageSenders = getNotificationPolicySenders(zenPolicy.getPriorityMessageSenders(), messageSenders);
        }
        if (zenPolicy2.isCategoryAllowed(3, isPriorityCategoryEnabled(8, defaultPolicy))) {
            priorityCategories |= 8;
            callSenders = getNotificationPolicySenders(zenPolicy.getPriorityCallSenders(), callSenders);
        }
        if (zenPolicy2.isCategoryAllowed(4, isPriorityCategoryEnabled(16, defaultPolicy))) {
            priorityCategories |= 16;
        }
        if (zenPolicy2.isCategoryAllowed(5, isPriorityCategoryEnabled(32, defaultPolicy))) {
            priorityCategories |= 32;
        }
        if (zenPolicy2.isCategoryAllowed(6, isPriorityCategoryEnabled(64, defaultPolicy))) {
            priorityCategories |= 64;
        }
        if (zenPolicy2.isCategoryAllowed(7, isPriorityCategoryEnabled(128, defaultPolicy))) {
            priorityCategories |= 128;
        }
        boolean suppressFullScreenIntent = zenPolicy2.isVisualEffectAllowed(0, isVisualEffectAllowed(4, defaultPolicy)) ^ true;
        boolean suppressLights = zenPolicy2.isVisualEffectAllowed(1, isVisualEffectAllowed(8, defaultPolicy)) ^ true;
        boolean suppressAmbient = true ^ zenPolicy2.isVisualEffectAllowed(5, isVisualEffectAllowed(128, defaultPolicy));
        if (suppressFullScreenIntent && suppressLights && suppressAmbient) {
            suppressedVisualEffects = 0 | 1;
        }
        if (suppressFullScreenIntent) {
            suppressedVisualEffects |= 4;
        }
        if (suppressLights) {
            suppressedVisualEffects |= 8;
        }
        if (!zenPolicy2.isVisualEffectAllowed(2, isVisualEffectAllowed(16, defaultPolicy))) {
            suppressedVisualEffects = (suppressedVisualEffects | 16) | 2;
        }
        if (!zenPolicy2.isVisualEffectAllowed(3, isVisualEffectAllowed(32, defaultPolicy))) {
            suppressedVisualEffects |= 32;
        }
        if (!zenPolicy2.isVisualEffectAllowed(4, isVisualEffectAllowed(64, defaultPolicy))) {
            suppressedVisualEffects |= 64;
        }
        if (suppressAmbient) {
            suppressedVisualEffects |= 128;
        }
        if (!zenPolicy2.isVisualEffectAllowed(6, isVisualEffectAllowed(256, defaultPolicy))) {
            suppressedVisualEffects |= 256;
        }
        return new Policy(priorityCategories, callSenders, messageSenders, suppressedVisualEffects, defaultPolicy.state);
    }

    private boolean isPriorityCategoryEnabled(int categoryType, Policy policy) {
        return (policy.priorityCategories & categoryType) != 0;
    }

    private boolean isVisualEffectAllowed(int visualEffect, Policy policy) {
        return (policy.suppressedVisualEffects & visualEffect) == 0;
    }

    private int getNotificationPolicySenders(int senders, int defaultPolicySender) {
        if (senders == 1) {
            return 0;
        }
        if (senders == 2) {
            return 1;
        }
        if (senders != 3) {
            return defaultPolicySender;
        }
        return 2;
    }

    public static int getZenPolicySenders(int senders) {
        if (senders == 0) {
            return 1;
        }
        if (senders != 1) {
            return 3;
        }
        return 2;
    }

    public Policy toNotificationPolicy() {
        int priorityCategories = 0;
        if (this.allowCalls) {
            priorityCategories = 0 | 8;
        }
        if (this.allowMessages) {
            priorityCategories |= 4;
        }
        if (this.allowEvents) {
            priorityCategories |= 2;
        }
        if (this.allowReminders) {
            priorityCategories |= 1;
        }
        if (this.allowRepeatCallers) {
            priorityCategories |= 16;
        }
        if (this.allowAlarms) {
            priorityCategories |= 32;
        }
        if (this.allowMedia) {
            priorityCategories |= 64;
        }
        if (this.allowSystem) {
            priorityCategories |= 128;
        }
        return new Policy(priorityCategories, sourceToPrioritySenders(this.allowCallsFrom, 1), sourceToPrioritySenders(this.allowMessagesFrom, 1), this.suppressedVisualEffects, this.areChannelsBypassingDnd ? 1 : 0);
    }

    public static ScheduleCalendar toScheduleCalendar(Uri conditionId) {
        ScheduleInfo schedule = tryParseScheduleConditionId(conditionId);
        if (schedule == null || schedule.days == null || schedule.days.length == 0) {
            return null;
        }
        ScheduleCalendar sc = new ScheduleCalendar();
        sc.setSchedule(schedule);
        sc.setTimeZone(TimeZone.getDefault());
        return sc;
    }

    private static int sourceToPrioritySenders(int source, int def) {
        if (source == 0) {
            return 0;
        }
        if (source == 1) {
            return 1;
        }
        if (source != 2) {
            return def;
        }
        return 2;
    }

    private static int prioritySendersToSource(int prioritySenders, int def) {
        if (prioritySenders == 0) {
            return 0;
        }
        if (prioritySenders == 1) {
            return 1;
        }
        if (prioritySenders != 2) {
            return def;
        }
        return 2;
    }

    public void applyNotificationPolicy(Policy policy) {
        if (policy != null) {
            boolean z = false;
            this.allowAlarms = (policy.priorityCategories & 32) != 0;
            this.allowMedia = (policy.priorityCategories & 64) != 0;
            this.allowSystem = (policy.priorityCategories & 128) != 0;
            this.allowEvents = (policy.priorityCategories & 2) != 0;
            this.allowReminders = (policy.priorityCategories & 1) != 0;
            this.allowCalls = (policy.priorityCategories & 8) != 0;
            this.allowMessages = (policy.priorityCategories & 4) != 0;
            this.allowRepeatCallers = (policy.priorityCategories & 16) != 0;
            this.allowCallsFrom = prioritySendersToSource(policy.priorityCallSenders, this.allowCallsFrom);
            this.allowMessagesFrom = prioritySendersToSource(policy.priorityMessageSenders, this.allowMessagesFrom);
            if (policy.suppressedVisualEffects != -1) {
                this.suppressedVisualEffects = policy.suppressedVisualEffects;
            }
            if (policy.state != -1) {
                if ((policy.state & 1) != 0) {
                    z = true;
                }
                this.areChannelsBypassingDnd = z;
            }
        }
    }

    public static Condition toTimeCondition(Context context, int minutesFromNow, int userHandle) {
        return toTimeCondition(context, minutesFromNow, userHandle, false);
    }

    public static Condition toTimeCondition(Context context, int minutesFromNow, int userHandle, boolean shortVersion) {
        return toTimeCondition(context, System.currentTimeMillis() + (minutesFromNow == 0 ? JobInfo.MIN_BACKOFF_MILLIS : (long) (60000 * minutesFromNow)), minutesFromNow, userHandle, shortVersion);
    }

    public static Condition toTimeCondition(Context context, long time, int minutes, int userHandle, boolean shortVersion) {
        String summary;
        String line1;
        String line2;
        long j = time;
        int i = minutes;
        CharSequence formattedTime = getFormattedTime(context, j, isToday(time), userHandle);
        Resources res = context.getResources();
        int num;
        int summaryResId;
        int line1ResId;
        if (i < 60) {
            num = minutes;
            if (shortVersion) {
                summaryResId = R.plurals.zen_mode_duration_minutes_summary_short;
            } else {
                summaryResId = R.plurals.zen_mode_duration_minutes_summary;
            }
            summary = res.getQuantityString(summaryResId, num, new Object[]{Integer.valueOf(num), formattedTime});
            if (shortVersion) {
                line1ResId = R.plurals.zen_mode_duration_minutes_short;
            } else {
                line1ResId = R.plurals.zen_mode_duration_minutes;
            }
            line1 = res.getQuantityString(line1ResId, num, new Object[]{Integer.valueOf(num), formattedTime});
            line2 = res.getString(R.string.zen_mode_until, formattedTime);
        } else if (i < 1440) {
            num = Math.round(((float) i) / 60.0f);
            if (shortVersion) {
                summaryResId = R.plurals.zen_mode_duration_hours_summary_short;
            } else {
                summaryResId = R.plurals.zen_mode_duration_hours_summary;
            }
            summary = res.getQuantityString(summaryResId, num, Integer.valueOf(num), formattedTime);
            if (shortVersion) {
                line1ResId = R.plurals.zen_mode_duration_hours_short;
            } else {
                line1ResId = R.plurals.zen_mode_duration_hours;
            }
            line1 = res.getQuantityString(line1ResId, num, Integer.valueOf(num), formattedTime);
            line2 = res.getString(R.string.zen_mode_until, formattedTime);
        } else {
            line2 = res.getString(R.string.zen_mode_until, formattedTime);
            summary = line2;
            line1 = line2;
        }
        return new Condition(toCountdownConditionId(j, false), summary, line1, line2, 0, 1, 1);
    }

    public static Condition toNextAlarmCondition(Context context, long alarm, int userHandle) {
        long j = alarm;
        CharSequence formattedTime = getFormattedTime(context, j, isToday(alarm), userHandle);
        String line1 = context.getResources().getString(R.string.zen_mode_until, new Object[]{formattedTime});
        return new Condition(toCountdownConditionId(j, true), "", line1, "", 0, 1, 1);
    }

    public static CharSequence getFormattedTime(Context context, long time, boolean isSameDay, int userHandle) {
        String skeleton = new StringBuilder();
        skeleton.append(!isSameDay ? "EEE " : "");
        skeleton.append(DateFormat.is24HourFormat(context, userHandle) ? "Hm" : "hma");
        return DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), skeleton.toString()), time);
    }

    public static boolean isToday(long time) {
        GregorianCalendar now = new GregorianCalendar();
        GregorianCalendar endTime = new GregorianCalendar();
        endTime.setTimeInMillis(time);
        if (now.get(1) == endTime.get(1) && now.get(2) == endTime.get(2) && now.get(5) == endTime.get(5)) {
            return true;
        }
        return false;
    }

    public static Uri toCountdownConditionId(long time, boolean alarm) {
        return new Uri.Builder().scheme(Condition.SCHEME).authority("android").appendPath(COUNTDOWN_PATH).appendPath(Long.toString(time)).appendPath("alarm").appendPath(Boolean.toString(alarm)).build();
    }

    public static long tryParseCountdownConditionId(Uri conditionId) {
        if (Condition.isValidId(conditionId, "android") && conditionId.getPathSegments().size() >= 2) {
            if (COUNTDOWN_PATH.equals(conditionId.getPathSegments().get(0))) {
                try {
                    return Long.parseLong((String) conditionId.getPathSegments().get(1));
                } catch (RuntimeException e) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Error parsing countdown condition: ");
                    stringBuilder.append(conditionId);
                    Slog.w(str, stringBuilder.toString(), e);
                    return 0;
                }
            }
        }
        return 0;
    }

    public static boolean isValidCountdownConditionId(Uri conditionId) {
        return tryParseCountdownConditionId(conditionId) != 0;
    }

    public static boolean isValidCountdownToAlarmConditionId(Uri conditionId) {
        if (tryParseCountdownConditionId(conditionId) != 0 && conditionId.getPathSegments().size() >= 4) {
            if ("alarm".equals(conditionId.getPathSegments().get(2))) {
                try {
                    return Boolean.parseBoolean((String) conditionId.getPathSegments().get(3));
                } catch (RuntimeException e) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Error parsing countdown alarm condition: ");
                    stringBuilder.append(conditionId);
                    Slog.w(str, stringBuilder.toString(), e);
                    return false;
                }
            }
        }
        return false;
    }

    public static Uri toScheduleConditionId(ScheduleInfo schedule) {
        Uri.Builder appendQueryParameter = new Uri.Builder().scheme(Condition.SCHEME).authority("android").appendPath(SCHEDULE_PATH).appendQueryParameter("days", toDayList(schedule.days));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(schedule.startHour);
        String str = ".";
        stringBuilder.append(str);
        stringBuilder.append(schedule.startMinute);
        appendQueryParameter = appendQueryParameter.appendQueryParameter(BaseMmsColumns.START, stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(schedule.endHour);
        stringBuilder.append(str);
        stringBuilder.append(schedule.endMinute);
        return appendQueryParameter.appendQueryParameter("end", stringBuilder.toString()).appendQueryParameter("exitAtAlarm", String.valueOf(schedule.exitAtAlarm)).build();
    }

    public static boolean isValidScheduleConditionId(Uri conditionId) {
        try {
            ScheduleInfo info = tryParseScheduleConditionId(conditionId);
            if (info == null || info.days == null || info.days.length == 0) {
                return false;
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return false;
        }
    }

    public static boolean isValidScheduleConditionId(Uri conditionId, boolean allowNever) {
        try {
            ScheduleInfo info = tryParseScheduleConditionId(conditionId);
            if (info == null || (!allowNever && (info.days == null || info.days.length == 0))) {
                return false;
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x003d A:{RETURN} */
    @android.annotation.UnsupportedAppUsage
    public static android.service.notification.ZenModeConfig.ScheduleInfo tryParseScheduleConditionId(android.net.Uri r8) {
        /*
        r0 = 1;
        r1 = 0;
        if (r8 == 0) goto L_0x0039;
    L_0x0004:
        r2 = r8.getScheme();
        r3 = "condition";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x0039;
    L_0x0010:
        r2 = r8.getAuthority();
        r3 = "android";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x0039;
    L_0x001c:
        r2 = r8.getPathSegments();
        r2 = r2.size();
        if (r2 != r0) goto L_0x0039;
    L_0x0026:
        r2 = r8.getPathSegments();
        r2 = r2.get(r1);
        r3 = "schedule";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x0039;
    L_0x0037:
        r2 = r0;
        goto L_0x003a;
    L_0x0039:
        r2 = r1;
    L_0x003a:
        r3 = 0;
        if (r2 != 0) goto L_0x003e;
    L_0x003d:
        return r3;
    L_0x003e:
        r4 = "start";
        r4 = r8.getQueryParameter(r4);
        r4 = tryParseHourAndMinute(r4);
        r5 = "end";
        r5 = r8.getQueryParameter(r5);
        r5 = tryParseHourAndMinute(r5);
        if (r4 == 0) goto L_0x0088;
    L_0x0055:
        if (r5 != 0) goto L_0x0058;
    L_0x0057:
        goto L_0x0088;
    L_0x0058:
        r3 = new android.service.notification.ZenModeConfig$ScheduleInfo;
        r3.<init>();
        r6 = "days";
        r6 = r8.getQueryParameter(r6);
        r7 = "\\.";
        r6 = tryParseDayList(r6, r7);
        r3.days = r6;
        r6 = r4[r1];
        r3.startHour = r6;
        r6 = r4[r0];
        r3.startMinute = r6;
        r6 = r5[r1];
        r3.endHour = r6;
        r0 = r5[r0];
        r3.endMinute = r0;
        r0 = "exitAtAlarm";
        r0 = r8.getQueryParameter(r0);
        r0 = safeBoolean(r0, r1);
        r3.exitAtAlarm = r0;
        return r3;
    L_0x0088:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.service.notification.ZenModeConfig.tryParseScheduleConditionId(android.net.Uri):android.service.notification.ZenModeConfig$ScheduleInfo");
    }

    public static ComponentName getScheduleConditionProvider() {
        return new ComponentName("android", "ScheduleConditionProvider");
    }

    public static Uri toEventConditionId(EventInfo event) {
        String str = "";
        Uri.Builder appendQueryParameter = new Uri.Builder().scheme(Condition.SCHEME).authority("android").appendPath("event").appendQueryParameter(XSpaceUtils.EXTRA_XSPACE_ACTUAL_USERID, Long.toString((long) event.userId)).appendQueryParameter("calendar", event.calName != null ? event.calName : str);
        if (event.calendarId != null) {
            str = event.calendarId.toString();
        }
        return appendQueryParameter.appendQueryParameter("calendarId", str).appendQueryParameter("reply", Integer.toString(event.reply)).build();
    }

    public static boolean isValidEventConditionId(Uri conditionId) {
        return tryParseEventConditionId(conditionId) != null;
    }

    /* JADX WARNING: Missing block: B:9:0x0034, code skipped:
            if ("event".equals(r6.getPathSegments().get(0)) != false) goto L_0x0038;
     */
    public static android.service.notification.ZenModeConfig.EventInfo tryParseEventConditionId(android.net.Uri r6) {
        /*
        r0 = 1;
        r1 = 0;
        if (r6 == 0) goto L_0x0037;
    L_0x0004:
        r2 = r6.getScheme();
        r3 = "condition";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x0037;
    L_0x0010:
        r2 = r6.getAuthority();
        r3 = "android";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x0037;
    L_0x001c:
        r2 = r6.getPathSegments();
        r2 = r2.size();
        if (r2 != r0) goto L_0x0037;
    L_0x0026:
        r2 = r6.getPathSegments();
        r2 = r2.get(r1);
        r3 = "event";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x0037;
    L_0x0036:
        goto L_0x0038;
    L_0x0037:
        r0 = r1;
    L_0x0038:
        r2 = 0;
        if (r0 != 0) goto L_0x003c;
    L_0x003b:
        return r2;
    L_0x003c:
        r3 = new android.service.notification.ZenModeConfig$EventInfo;
        r3.<init>();
        r4 = "userId";
        r4 = r6.getQueryParameter(r4);
        r5 = -10000; // 0xffffffffffffd8f0 float:NaN double:NaN;
        r4 = tryParseInt(r4, r5);
        r3.userId = r4;
        r4 = "calendar";
        r4 = r6.getQueryParameter(r4);
        r3.calName = r4;
        r4 = r3.calName;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 == 0) goto L_0x0062;
    L_0x0060:
        r3.calName = r2;
    L_0x0062:
        r4 = "calendarId";
        r4 = r6.getQueryParameter(r4);
        r2 = tryParseLong(r4, r2);
        r3.calendarId = r2;
        r2 = "reply";
        r2 = r6.getQueryParameter(r2);
        r1 = tryParseInt(r2, r1);
        r3.reply = r1;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.service.notification.ZenModeConfig.tryParseEventConditionId(android.net.Uri):android.service.notification.ZenModeConfig$EventInfo");
    }

    public static ComponentName getEventConditionProvider() {
        return new ComponentName("android", "EventConditionProvider");
    }

    private static int[] tryParseHourAndMinute(String value) {
        int[] iArr = null;
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        int i = value.indexOf(46);
        if (i < 1 || i >= value.length() - 1) {
            return null;
        }
        int hour = tryParseInt(value.substring(0, i), -1);
        int minute = tryParseInt(value.substring(i + 1), -1);
        if (isValidHour(hour) && isValidMinute(minute)) {
            iArr = new int[]{hour, minute};
        }
        return iArr;
    }

    private static int tryParseZenMode(String value, int defValue) {
        int rt = tryParseInt(value, defValue);
        return Global.isValidZenMode(rt) ? rt : defValue;
    }

    public static String newRuleId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getOwnerCaption(Context context, String owner) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(owner, null);
            if (info != null) {
                CharSequence seq = info.loadLabel(pm);
                if (seq != null) {
                    String str = seq.toString().trim();
                    if (str.length() > 0) {
                        return str;
                    }
                }
            }
        } catch (Throwable e) {
            Slog.w(TAG, "Error loading owner caption", e);
        }
        return "";
    }

    public static String getConditionSummary(Context context, ZenModeConfig config, int userHandle, boolean shortVersion) {
        return getConditionLine(context, config, userHandle, false, shortVersion);
    }

    private static String getConditionLine(Context context, ZenModeConfig config, int userHandle, boolean useLine1, boolean shortVersion) {
        Context context2 = context;
        ZenModeConfig zenModeConfig = config;
        String str = "";
        if (zenModeConfig == null) {
            return str;
        }
        String summary = "";
        ZenRule zenRule = zenModeConfig.manualRule;
        if (zenRule != null) {
            Uri id = zenRule.conditionId;
            if (zenModeConfig.manualRule.enabler != null) {
                summary = getOwnerCaption(context2, zenModeConfig.manualRule.enabler);
            } else if (id == null) {
                summary = context2.getString(R.string.zen_mode_forever);
            } else {
                long time = tryParseCountdownConditionId(id);
                Condition c = zenModeConfig.manualRule.condition;
                if (time > 0) {
                    long span = time - System.currentTimeMillis();
                    c = toTimeCondition(context, time, Math.round(((float) span) / 60000.0f), userHandle, shortVersion);
                }
                String rt = c == null ? str : useLine1 ? c.line1 : c.summary;
                if (!TextUtils.isEmpty(rt)) {
                    str = rt;
                }
                summary = str;
            }
        }
        for (ZenRule automaticRule : zenModeConfig.automaticRules.values()) {
            if (automaticRule.isAutomaticActive()) {
                if (summary.isEmpty()) {
                    summary = automaticRule.name;
                } else {
                    summary = context.getResources().getString(R.string.zen_mode_rule_name_combination, summary, automaticRule.name);
                }
            }
        }
        return summary;
    }

    public static boolean areAllPriorityOnlyNotificationZenSoundsMuted(Policy policy) {
        boolean allowReminders = (policy.priorityCategories & 1) != 0;
        boolean allowCalls = (policy.priorityCategories & 8) != 0;
        boolean allowMessages = (policy.priorityCategories & 4) != 0;
        boolean allowEvents = (policy.priorityCategories & 2) != 0;
        boolean allowRepeatCallers = (policy.priorityCategories & 16) != 0;
        boolean areChannelsBypassingDnd = (policy.state & 1) != 0;
        if (allowReminders || allowCalls || allowMessages || allowEvents || allowRepeatCallers || areChannelsBypassingDnd) {
            return false;
        }
        return true;
    }

    public static boolean areAllZenBehaviorSoundsMuted(Policy policy) {
        boolean allowAlarms = (policy.priorityCategories & 32) != 0;
        boolean allowMedia = (policy.priorityCategories & 64) != 0;
        boolean allowSystem = (policy.priorityCategories & 128) != 0;
        if (allowAlarms || allowMedia || allowSystem || !areAllPriorityOnlyNotificationZenSoundsMuted(policy)) {
            return false;
        }
        return true;
    }

    public static boolean isZenOverridingRinger(int zen, Policy consolidatedPolicy) {
        if (zen == 2 || zen == 3) {
            return true;
        }
        if (zen == 1 && areAllPriorityOnlyNotificationZenSoundsMuted(consolidatedPolicy)) {
            return true;
        }
        return false;
    }

    public static boolean areAllPriorityOnlyNotificationZenSoundsMuted(ZenModeConfig config) {
        return (config.allowReminders || config.allowCalls || config.allowMessages || config.allowEvents || config.allowRepeatCallers || config.areChannelsBypassingDnd) ? false : true;
    }

    public static boolean areAllZenBehaviorSoundsMuted(ZenModeConfig config) {
        return (config.allowAlarms || config.allowMedia || config.allowSystem || !areAllPriorityOnlyNotificationZenSoundsMuted(config)) ? false : true;
    }

    public static String getDescription(Context context, boolean zenOn, ZenModeConfig config, boolean describeForeverCondition) {
        String str = null;
        if (!zenOn || config == null) {
            return null;
        }
        String secondaryText = "";
        long latestEndTime = -1;
        Uri id = config.manualRule;
        if (id != null) {
            id = id.conditionId;
            if (config.manualRule.enabler != null) {
                String appName = getOwnerCaption(context, config.manualRule.enabler);
                if (!appName.isEmpty()) {
                    secondaryText = appName;
                }
            } else if (id != null) {
                latestEndTime = tryParseCountdownConditionId(id);
                if (latestEndTime > 0) {
                    secondaryText = context.getString(R.string.zen_mode_until, getFormattedTime(context, latestEndTime, isToday(latestEndTime), context.getUserId()));
                }
            } else if (describeForeverCondition) {
                return context.getString(R.string.zen_mode_forever);
            } else {
                return null;
            }
        }
        for (ZenRule automaticRule : config.automaticRules.values()) {
            if (automaticRule.isAutomaticActive()) {
                if (!isValidEventConditionId(automaticRule.conditionId) && !isValidScheduleConditionId(automaticRule.conditionId)) {
                    return automaticRule.name;
                }
                long endTime = parseAutomaticRuleEndTime(context, automaticRule.conditionId);
                if (endTime > latestEndTime) {
                    latestEndTime = endTime;
                    secondaryText = automaticRule.name;
                }
            }
        }
        if (!secondaryText.equals("")) {
            str = secondaryText;
        }
        return str;
    }

    private static long parseAutomaticRuleEndTime(Context context, Uri id) {
        if (isValidEventConditionId(id)) {
            return Long.MAX_VALUE;
        }
        if (!isValidScheduleConditionId(id)) {
            return -1;
        }
        ScheduleCalendar schedule = toScheduleCalendar(id);
        long endTimeMs = schedule.getNextChangeTime(System.currentTimeMillis());
        if (schedule.exitAtAlarm()) {
            long nextAlarm = getNextAlarm(context);
            schedule.maybeSetNextAlarm(System.currentTimeMillis(), nextAlarm);
            if (schedule.shouldExitForAlarm(endTimeMs)) {
                return nextAlarm;
            }
        }
        return endTimeMs;
    }

    private static long getNextAlarm(Context context) {
        AlarmClockInfo info = ((AlarmManager) context.getSystemService("alarm")).getNextAlarmClock(context.getUserId());
        return info != null ? info.getTriggerTime() : 0;
    }
}
