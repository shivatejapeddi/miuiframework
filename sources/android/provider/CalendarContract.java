package android.provider;

import android.annotation.UnsupportedAppUsage;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorEntityIterator;
import android.content.Entity;
import android.content.EntityIterator;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.RemoteException;
import android.provider.SyncStateContract.Columns;
import android.util.SeempLog;
import com.android.internal.util.Preconditions;

public final class CalendarContract {
    public static final String ACCOUNT_TYPE_LOCAL = "LOCAL";
    public static final String ACTION_EVENT_REMINDER = "android.intent.action.EVENT_REMINDER";
    public static final String ACTION_HANDLE_CUSTOM_EVENT = "android.provider.calendar.action.HANDLE_CUSTOM_EVENT";
    public static final String ACTION_VIEW_MANAGED_PROFILE_CALENDAR_EVENT = "android.provider.calendar.action.VIEW_MANAGED_PROFILE_CALENDAR_EVENT";
    public static final String AUTHORITY = "com.android.calendar";
    public static final String CALLER_IS_SYNCADAPTER = "caller_is_syncadapter";
    public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar");
    public static final Uri ENTERPRISE_CONTENT_URI = Uri.parse("content://com.android.calendar/enterprise");
    public static final String EXTRA_CUSTOM_APP_URI = "customAppUri";
    public static final String EXTRA_EVENT_ALL_DAY = "allDay";
    public static final String EXTRA_EVENT_BEGIN_TIME = "beginTime";
    public static final String EXTRA_EVENT_END_TIME = "endTime";
    public static final String EXTRA_EVENT_ID = "id";
    private static final String TAG = "Calendar";

    protected interface AttendeesColumns {
        public static final String ATTENDEE_EMAIL = "attendeeEmail";
        public static final String ATTENDEE_IDENTITY = "attendeeIdentity";
        public static final String ATTENDEE_ID_NAMESPACE = "attendeeIdNamespace";
        public static final String ATTENDEE_NAME = "attendeeName";
        public static final String ATTENDEE_RELATIONSHIP = "attendeeRelationship";
        public static final String ATTENDEE_STATUS = "attendeeStatus";
        public static final int ATTENDEE_STATUS_ACCEPTED = 1;
        public static final int ATTENDEE_STATUS_DECLINED = 2;
        public static final int ATTENDEE_STATUS_INVITED = 3;
        public static final int ATTENDEE_STATUS_NONE = 0;
        public static final int ATTENDEE_STATUS_TENTATIVE = 4;
        public static final String ATTENDEE_TYPE = "attendeeType";
        public static final String EVENT_ID = "event_id";
        public static final int RELATIONSHIP_ATTENDEE = 1;
        public static final int RELATIONSHIP_NONE = 0;
        public static final int RELATIONSHIP_ORGANIZER = 2;
        public static final int RELATIONSHIP_PERFORMER = 3;
        public static final int RELATIONSHIP_SPEAKER = 4;
        public static final int TYPE_NONE = 0;
        public static final int TYPE_OPTIONAL = 2;
        public static final int TYPE_REQUIRED = 1;
        public static final int TYPE_RESOURCE = 3;
    }

    protected interface EventsColumns {
        public static final int ACCESS_CONFIDENTIAL = 1;
        public static final int ACCESS_DEFAULT = 0;
        public static final String ACCESS_LEVEL = "accessLevel";
        public static final int ACCESS_PRIVATE = 2;
        public static final int ACCESS_PUBLIC = 3;
        public static final String ALL_DAY = "allDay";
        public static final String AVAILABILITY = "availability";
        public static final int AVAILABILITY_BUSY = 0;
        public static final int AVAILABILITY_FREE = 1;
        public static final int AVAILABILITY_TENTATIVE = 2;
        public static final String CALENDAR_ID = "calendar_id";
        public static final String CAN_INVITE_OTHERS = "canInviteOthers";
        public static final String CUSTOM_APP_PACKAGE = "customAppPackage";
        public static final String CUSTOM_APP_URI = "customAppUri";
        public static final String DESCRIPTION = "description";
        public static final String DISPLAY_COLOR = "displayColor";
        public static final String DTEND = "dtend";
        public static final String DTSTART = "dtstart";
        public static final String DURATION = "duration";
        public static final String EVENT_COLOR = "eventColor";
        public static final String EVENT_COLOR_KEY = "eventColor_index";
        public static final String EVENT_END_TIMEZONE = "eventEndTimezone";
        public static final String EVENT_LOCATION = "eventLocation";
        public static final String EVENT_TIMEZONE = "eventTimezone";
        public static final String EXDATE = "exdate";
        public static final String EXRULE = "exrule";
        public static final String GUESTS_CAN_INVITE_OTHERS = "guestsCanInviteOthers";
        public static final String GUESTS_CAN_MODIFY = "guestsCanModify";
        public static final String GUESTS_CAN_SEE_GUESTS = "guestsCanSeeGuests";
        public static final String HAS_ALARM = "hasAlarm";
        public static final String HAS_ATTENDEE_DATA = "hasAttendeeData";
        public static final String HAS_EXTENDED_PROPERTIES = "hasExtendedProperties";
        public static final String IS_ORGANIZER = "isOrganizer";
        public static final String LAST_DATE = "lastDate";
        public static final String LAST_SYNCED = "lastSynced";
        public static final String ORGANIZER = "organizer";
        public static final String ORIGINAL_ALL_DAY = "originalAllDay";
        public static final String ORIGINAL_ID = "original_id";
        public static final String ORIGINAL_INSTANCE_TIME = "originalInstanceTime";
        public static final String ORIGINAL_SYNC_ID = "original_sync_id";
        public static final String RDATE = "rdate";
        public static final String RRULE = "rrule";
        public static final String SELF_ATTENDEE_STATUS = "selfAttendeeStatus";
        public static final String STATUS = "eventStatus";
        public static final int STATUS_CANCELED = 2;
        public static final int STATUS_CONFIRMED = 1;
        public static final int STATUS_TENTATIVE = 0;
        public static final String SYNC_DATA1 = "sync_data1";
        public static final String SYNC_DATA10 = "sync_data10";
        public static final String SYNC_DATA2 = "sync_data2";
        public static final String SYNC_DATA3 = "sync_data3";
        public static final String SYNC_DATA4 = "sync_data4";
        public static final String SYNC_DATA5 = "sync_data5";
        public static final String SYNC_DATA6 = "sync_data6";
        public static final String SYNC_DATA7 = "sync_data7";
        public static final String SYNC_DATA8 = "sync_data8";
        public static final String SYNC_DATA9 = "sync_data9";
        public static final String TITLE = "title";
        public static final String UID_2445 = "uid2445";
    }

    public static final class Attendees implements BaseColumns, AttendeesColumns, EventsColumns {
        private static final String ATTENDEES_WHERE = "event_id=?";
        public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/attendees");

        private Attendees() {
        }

        public static final Cursor query(ContentResolver cr, long eventId, String[] projection) {
            SeempLog.record(54);
            return cr.query(CONTENT_URI, projection, ATTENDEES_WHERE, new String[]{Long.toString(eventId)}, null);
        }
    }

    protected interface CalendarAlertsColumns {
        public static final String ALARM_TIME = "alarmTime";
        public static final String BEGIN = "begin";
        public static final String CREATION_TIME = "creationTime";
        public static final String DEFAULT_SORT_ORDER = "begin ASC,title ASC";
        public static final String END = "end";
        public static final String EVENT_ID = "event_id";
        public static final String MINUTES = "minutes";
        public static final String NOTIFY_TIME = "notifyTime";
        public static final String RECEIVED_TIME = "receivedTime";
        public static final String STATE = "state";
        public static final int STATE_DISMISSED = 2;
        public static final int STATE_FIRED = 1;
        public static final int STATE_SCHEDULED = 0;
    }

    protected interface CalendarColumns {
        public static final String ALLOWED_ATTENDEE_TYPES = "allowedAttendeeTypes";
        public static final String ALLOWED_AVAILABILITY = "allowedAvailability";
        public static final String ALLOWED_REMINDERS = "allowedReminders";
        public static final String CALENDAR_ACCESS_LEVEL = "calendar_access_level";
        public static final String CALENDAR_COLOR = "calendar_color";
        public static final String CALENDAR_COLOR_KEY = "calendar_color_index";
        public static final String CALENDAR_DISPLAY_NAME = "calendar_displayName";
        public static final String CALENDAR_TIME_ZONE = "calendar_timezone";
        public static final int CAL_ACCESS_CONTRIBUTOR = 500;
        public static final int CAL_ACCESS_EDITOR = 600;
        public static final int CAL_ACCESS_FREEBUSY = 100;
        public static final int CAL_ACCESS_NONE = 0;
        public static final int CAL_ACCESS_OVERRIDE = 400;
        public static final int CAL_ACCESS_OWNER = 700;
        public static final int CAL_ACCESS_READ = 200;
        public static final int CAL_ACCESS_RESPOND = 300;
        public static final int CAL_ACCESS_ROOT = 800;
        public static final String CAN_MODIFY_TIME_ZONE = "canModifyTimeZone";
        public static final String CAN_ORGANIZER_RESPOND = "canOrganizerRespond";
        public static final String IS_PRIMARY = "isPrimary";
        public static final String MAX_REMINDERS = "maxReminders";
        public static final String OWNER_ACCOUNT = "ownerAccount";
        public static final String SYNC_EVENTS = "sync_events";
        public static final String VISIBLE = "visible";
    }

    public static final class CalendarAlerts implements BaseColumns, CalendarAlertsColumns, EventsColumns, CalendarColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/calendar_alerts");
        public static final Uri CONTENT_URI_BY_INSTANCE = Uri.parse("content://com.android.calendar/calendar_alerts/by_instance");
        private static final boolean DEBUG = false;
        private static final String SORT_ORDER_ALARMTIME_ASC = "alarmTime ASC";
        public static final String TABLE_NAME = "CalendarAlerts";
        private static final String WHERE_ALARM_EXISTS = "event_id=? AND begin=? AND alarmTime=?";
        private static final String WHERE_FINDNEXTALARMTIME = "alarmTime>=?";
        private static final String WHERE_RESCHEDULE_MISSED_ALARMS = "state=0 AND alarmTime<? AND alarmTime>? AND end>=?";

        private CalendarAlerts() {
        }

        public static final Uri insert(ContentResolver cr, long eventId, long begin, long end, long alarmTime, int minutes) {
            SeempLog.record(51);
            ContentValues values = new ContentValues();
            values.put("event_id", Long.valueOf(eventId));
            values.put("begin", Long.valueOf(begin));
            values.put("end", Long.valueOf(end));
            values.put(CalendarAlertsColumns.ALARM_TIME, Long.valueOf(alarmTime));
            values.put(CalendarAlertsColumns.CREATION_TIME, Long.valueOf(System.currentTimeMillis()));
            Integer valueOf = Integer.valueOf(0);
            values.put(CalendarAlertsColumns.RECEIVED_TIME, valueOf);
            values.put(CalendarAlertsColumns.NOTIFY_TIME, valueOf);
            values.put("state", valueOf);
            values.put("minutes", Integer.valueOf(minutes));
            return cr.insert(CONTENT_URI, values);
        }

        @UnsupportedAppUsage
        public static final long findNextAlarmTime(ContentResolver cr, long millis) {
            SeempLog.record(53);
            String selection = new StringBuilder();
            selection.append("alarmTime>=");
            selection.append(millis);
            selection = selection.toString();
            String[] projection = new String[]{CalendarAlertsColumns.ALARM_TIME};
            ContentResolver contentResolver = cr;
            Cursor cursor = contentResolver.query(CONTENT_URI, projection, WHERE_FINDNEXTALARMTIME, new String[]{Long.toString(millis)}, SORT_ORDER_ALARMTIME_ASC);
            long alarmTime = -1;
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        alarmTime = cursor.getLong(0);
                    }
                } catch (Throwable th) {
                    cursor.close();
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            return alarmTime;
        }

        @UnsupportedAppUsage
        public static final void rescheduleMissedAlarms(ContentResolver cr, Context context, AlarmManager manager) {
            long ancient = System.currentTimeMillis() - 86400000;
            String[] projection = new String[]{CalendarAlertsColumns.ALARM_TIME};
            ContentResolver contentResolver = cr;
            Cursor cursor = contentResolver.query(CONTENT_URI, projection, WHERE_RESCHEDULE_MISSED_ALARMS, new String[]{Long.toString(now), Long.toString(ancient), Long.toString(now)}, SORT_ORDER_ALARMTIME_ASC);
            if (cursor != null) {
                long alarmTime = -1;
                while (cursor.moveToNext()) {
                    try {
                        long newAlarmTime = cursor.getLong(0);
                        if (alarmTime != newAlarmTime) {
                            scheduleAlarm(context, manager, newAlarmTime);
                            alarmTime = newAlarmTime;
                        }
                    } finally {
                        cursor.close();
                    }
                }
            }
        }

        @UnsupportedAppUsage
        public static void scheduleAlarm(Context context, AlarmManager manager, long alarmTime) {
            if (manager == null) {
                manager = (AlarmManager) context.getSystemService("alarm");
            }
            Intent intent = new Intent(CalendarContract.ACTION_EVENT_REMINDER);
            intent.setData(ContentUris.withAppendedId(CalendarContract.CONTENT_URI, alarmTime));
            intent.putExtra(CalendarAlertsColumns.ALARM_TIME, alarmTime);
            intent.setFlags(16777216);
            manager.setExactAndAllowWhileIdle(0, alarmTime, PendingIntent.getBroadcast(context, 0, intent, 0));
        }

        public static final boolean alarmExists(ContentResolver cr, long eventId, long begin, long alarmTime) {
            SeempLog.record(52);
            String[] projection = new String[]{CalendarAlertsColumns.ALARM_TIME};
            ContentResolver contentResolver = cr;
            Cursor cursor = contentResolver.query(CONTENT_URI, projection, WHERE_ALARM_EXISTS, new String[]{Long.toString(eventId), Long.toString(begin), Long.toString(alarmTime)}, null);
            boolean found = false;
            if (cursor != null) {
                try {
                    if (cursor.getCount() > 0) {
                        found = true;
                    }
                } catch (Throwable th) {
                    cursor.close();
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            return found;
        }
    }

    protected interface CalendarCacheColumns {
        public static final String KEY = "key";
        public static final String VALUE = "value";
    }

    public static final class CalendarCache implements CalendarCacheColumns {
        public static final String KEY_TIMEZONE_INSTANCES = "timezoneInstances";
        public static final String KEY_TIMEZONE_INSTANCES_PREVIOUS = "timezoneInstancesPrevious";
        public static final String KEY_TIMEZONE_TYPE = "timezoneType";
        public static final String TIMEZONE_TYPE_AUTO = "auto";
        public static final String TIMEZONE_TYPE_HOME = "home";
        public static final Uri URI = Uri.parse("content://com.android.calendar/properties");

        private CalendarCache() {
        }
    }

    protected interface CalendarSyncColumns {
        public static final String CAL_SYNC1 = "cal_sync1";
        public static final String CAL_SYNC10 = "cal_sync10";
        public static final String CAL_SYNC2 = "cal_sync2";
        public static final String CAL_SYNC3 = "cal_sync3";
        public static final String CAL_SYNC4 = "cal_sync4";
        public static final String CAL_SYNC5 = "cal_sync5";
        public static final String CAL_SYNC6 = "cal_sync6";
        public static final String CAL_SYNC7 = "cal_sync7";
        public static final String CAL_SYNC8 = "cal_sync8";
        public static final String CAL_SYNC9 = "cal_sync9";
    }

    protected interface SyncColumns extends CalendarSyncColumns {
        public static final String ACCOUNT_NAME = "account_name";
        public static final String ACCOUNT_TYPE = "account_type";
        public static final String CAN_PARTIALLY_UPDATE = "canPartiallyUpdate";
        public static final String DELETED = "deleted";
        public static final String DIRTY = "dirty";
        public static final String MUTATORS = "mutators";
        public static final String _SYNC_ID = "_sync_id";
    }

    public static final class CalendarEntity implements BaseColumns, SyncColumns, CalendarColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/calendar_entities");

        private static class EntityIteratorImpl extends CursorEntityIterator {
            public EntityIteratorImpl(Cursor cursor) {
                super(cursor);
            }

            public Entity getEntityAndIncrementCursor(Cursor cursor) throws RemoteException {
                String str = "_id";
                long calendarId = cursor.getLong(cursor.getColumnIndexOrThrow(str));
                ContentValues cv = new ContentValues();
                cv.put(str, Long.valueOf(calendarId));
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, "account_name");
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, "account_type");
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, "_sync_id");
                DatabaseUtils.cursorLongToContentValuesIfPresent(cursor, cv, "dirty");
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, SyncColumns.MUTATORS);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, CalendarSyncColumns.CAL_SYNC1);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, CalendarSyncColumns.CAL_SYNC2);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, CalendarSyncColumns.CAL_SYNC3);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, CalendarSyncColumns.CAL_SYNC4);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, CalendarSyncColumns.CAL_SYNC5);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, CalendarSyncColumns.CAL_SYNC6);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, CalendarSyncColumns.CAL_SYNC7);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, CalendarSyncColumns.CAL_SYNC8);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, CalendarSyncColumns.CAL_SYNC9);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, CalendarSyncColumns.CAL_SYNC10);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, "name");
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, "calendar_displayName");
                DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, CalendarColumns.CALENDAR_COLOR);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, CalendarColumns.CALENDAR_COLOR_KEY);
                DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, CalendarColumns.CALENDAR_ACCESS_LEVEL);
                DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, CalendarColumns.VISIBLE);
                DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, CalendarColumns.SYNC_EVENTS);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, Calendars.CALENDAR_LOCATION);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, CalendarColumns.CALENDAR_TIME_ZONE);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, CalendarColumns.OWNER_ACCOUNT);
                DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, CalendarColumns.CAN_ORGANIZER_RESPOND);
                DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, CalendarColumns.CAN_MODIFY_TIME_ZONE);
                DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, CalendarColumns.MAX_REMINDERS);
                DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, SyncColumns.CAN_PARTIALLY_UPDATE);
                DatabaseUtils.cursorStringToContentValuesIfPresent(cursor, cv, CalendarColumns.ALLOWED_REMINDERS);
                DatabaseUtils.cursorIntToContentValuesIfPresent(cursor, cv, "deleted");
                Entity entity = new Entity(cv);
                cursor.moveToNext();
                return entity;
            }
        }

        private CalendarEntity() {
        }

        public static EntityIterator newEntityIterator(Cursor cursor) {
            return new EntityIteratorImpl(cursor);
        }
    }

    protected interface CalendarMetaDataColumns {
        public static final String LOCAL_TIMEZONE = "localTimezone";
        public static final String MAX_EVENTDAYS = "maxEventDays";
        public static final String MAX_INSTANCE = "maxInstance";
        public static final String MIN_EVENTDAYS = "minEventDays";
        public static final String MIN_INSTANCE = "minInstance";
    }

    public static final class CalendarMetaData implements CalendarMetaDataColumns, BaseColumns {
        private CalendarMetaData() {
        }
    }

    public static final class Calendars implements BaseColumns, SyncColumns, CalendarColumns {
        public static final String CALENDAR_LOCATION = "calendar_location";
        public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/calendars");
        public static final String DEFAULT_SORT_ORDER = "calendar_displayName";
        public static final Uri ENTERPRISE_CONTENT_URI = Uri.parse("content://com.android.calendar/enterprise/calendars");
        public static final String NAME = "name";
        public static final String[] SYNC_WRITABLE_COLUMNS = new String[]{"account_name", "account_type", "_sync_id", "dirty", SyncColumns.MUTATORS, CalendarColumns.OWNER_ACCOUNT, CalendarColumns.MAX_REMINDERS, CalendarColumns.ALLOWED_REMINDERS, CalendarColumns.CAN_MODIFY_TIME_ZONE, CalendarColumns.CAN_ORGANIZER_RESPOND, SyncColumns.CAN_PARTIALLY_UPDATE, CALENDAR_LOCATION, CalendarColumns.CALENDAR_TIME_ZONE, CalendarColumns.CALENDAR_ACCESS_LEVEL, "deleted", CalendarSyncColumns.CAL_SYNC1, CalendarSyncColumns.CAL_SYNC2, CalendarSyncColumns.CAL_SYNC3, CalendarSyncColumns.CAL_SYNC4, CalendarSyncColumns.CAL_SYNC5, CalendarSyncColumns.CAL_SYNC6, CalendarSyncColumns.CAL_SYNC7, CalendarSyncColumns.CAL_SYNC8, CalendarSyncColumns.CAL_SYNC9, CalendarSyncColumns.CAL_SYNC10};

        private Calendars() {
        }
    }

    protected interface ColorsColumns extends Columns {
        public static final String COLOR = "color";
        public static final String COLOR_KEY = "color_index";
        public static final String COLOR_TYPE = "color_type";
        public static final int TYPE_CALENDAR = 0;
        public static final int TYPE_EVENT = 1;
    }

    public static final class Colors implements ColorsColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/colors");
        public static final String TABLE_NAME = "Colors";

        private Colors() {
        }
    }

    protected interface EventDaysColumns {
        public static final String ENDDAY = "endDay";
        public static final String STARTDAY = "startDay";
    }

    public static final class EventDays implements EventDaysColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/instances/groupbyday");
        private static final String SELECTION = "selected=1";

        private EventDays() {
        }

        public static final Cursor query(ContentResolver cr, int startDay, int numDays, String[] projection) {
            SeempLog.record(54);
            if (numDays < 1) {
                return null;
            }
            int endDay = (startDay + numDays) - 1;
            Builder builder = CONTENT_URI.buildUpon();
            ContentUris.appendId(builder, (long) startDay);
            ContentUris.appendId(builder, (long) endDay);
            return cr.query(builder.build(), projection, SELECTION, null, "startDay");
        }
    }

    public static final class Events implements BaseColumns, SyncColumns, EventsColumns, CalendarColumns {
        public static final Uri CONTENT_EXCEPTION_URI = Uri.parse("content://com.android.calendar/exception");
        public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/events");
        private static final String DEFAULT_SORT_ORDER = "";
        public static final Uri ENTERPRISE_CONTENT_URI = Uri.parse("content://com.android.calendar/enterprise/events");
        @UnsupportedAppUsage
        public static String[] PROVIDER_WRITABLE_COLUMNS = new String[]{"account_name", "account_type", CalendarSyncColumns.CAL_SYNC1, CalendarSyncColumns.CAL_SYNC2, CalendarSyncColumns.CAL_SYNC3, CalendarSyncColumns.CAL_SYNC4, CalendarSyncColumns.CAL_SYNC5, CalendarSyncColumns.CAL_SYNC6, CalendarSyncColumns.CAL_SYNC7, CalendarSyncColumns.CAL_SYNC8, CalendarSyncColumns.CAL_SYNC9, CalendarSyncColumns.CAL_SYNC10, CalendarColumns.ALLOWED_REMINDERS, CalendarColumns.ALLOWED_ATTENDEE_TYPES, CalendarColumns.ALLOWED_AVAILABILITY, CalendarColumns.CALENDAR_ACCESS_LEVEL, CalendarColumns.CALENDAR_COLOR, CalendarColumns.CALENDAR_TIME_ZONE, CalendarColumns.CAN_MODIFY_TIME_ZONE, CalendarColumns.CAN_ORGANIZER_RESPOND, "calendar_displayName", SyncColumns.CAN_PARTIALLY_UPDATE, CalendarColumns.SYNC_EVENTS, CalendarColumns.VISIBLE};
        public static final String[] SYNC_WRITABLE_COLUMNS = new String[]{"_sync_id", "dirty", SyncColumns.MUTATORS, EventsColumns.SYNC_DATA1, EventsColumns.SYNC_DATA2, EventsColumns.SYNC_DATA3, EventsColumns.SYNC_DATA4, EventsColumns.SYNC_DATA5, EventsColumns.SYNC_DATA6, EventsColumns.SYNC_DATA7, EventsColumns.SYNC_DATA8, EventsColumns.SYNC_DATA9, EventsColumns.SYNC_DATA10};

        private Events() {
        }
    }

    public static final class EventsEntity implements BaseColumns, SyncColumns, EventsColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/event_entities");

        private static class EntityIteratorImpl extends CursorEntityIterator {
            private static final String[] ATTENDEES_PROJECTION = new String[]{AttendeesColumns.ATTENDEE_NAME, AttendeesColumns.ATTENDEE_EMAIL, AttendeesColumns.ATTENDEE_RELATIONSHIP, AttendeesColumns.ATTENDEE_TYPE, AttendeesColumns.ATTENDEE_STATUS, AttendeesColumns.ATTENDEE_IDENTITY, AttendeesColumns.ATTENDEE_ID_NAMESPACE};
            private static final int COLUMN_ATTENDEE_EMAIL = 1;
            private static final int COLUMN_ATTENDEE_IDENTITY = 5;
            private static final int COLUMN_ATTENDEE_ID_NAMESPACE = 6;
            private static final int COLUMN_ATTENDEE_NAME = 0;
            private static final int COLUMN_ATTENDEE_RELATIONSHIP = 2;
            private static final int COLUMN_ATTENDEE_STATUS = 4;
            private static final int COLUMN_ATTENDEE_TYPE = 3;
            private static final int COLUMN_ID = 0;
            private static final int COLUMN_METHOD = 1;
            private static final int COLUMN_MINUTES = 0;
            private static final int COLUMN_NAME = 1;
            private static final int COLUMN_VALUE = 2;
            private static final String[] EXTENDED_PROJECTION = new String[]{"_id", "name", "value"};
            private static final String[] REMINDERS_PROJECTION = new String[]{"minutes", RemindersColumns.METHOD};
            private static final String WHERE_EVENT_ID = "event_id=?";
            private final ContentProviderClient mProvider;
            private final ContentResolver mResolver;

            /*  JADX ERROR: JadxRuntimeException in pass: SSATransform
                jadx.core.utils.exceptions.JadxRuntimeException: Unknown predecessor block by arg (r0_3 ?) in PHI: PHI: (r0_6 ?) = (r0_3 ?), (r0_0 ?) binds: {(r0_0 ?)=B:29:0x02d7}
                	at jadx.core.dex.instructions.PhiInsn.replaceArg(PhiInsn.java:78)
                	at jadx.core.dex.visitors.ssa.SSATransform.inlinePhiInsn(SSATransform.java:392)
                	at jadx.core.dex.visitors.ssa.SSATransform.replacePhiWithMove(SSATransform.java:360)
                	at jadx.core.dex.visitors.ssa.SSATransform.fixPhiWithSameArgs(SSATransform.java:300)
                	at jadx.core.dex.visitors.ssa.SSATransform.fixUselessPhi(SSATransform.java:275)
                	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:61)
                	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
                	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
                	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
                	at java.util.ArrayList.forEach(Unknown Source)
                	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
                	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
                	at java.util.ArrayList.forEach(Unknown Source)
                	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
                	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
                	at java.util.ArrayList.forEach(Unknown Source)
                	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
                	at jadx.core.ProcessClass.process(ProcessClass.java:32)
                	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
                	at java.lang.Iterable.forEach(Unknown Source)
                	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
                	at jadx.core.ProcessClass.process(ProcessClass.java:37)
                	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
                	at jadx.api.JavaClass.decompile(JavaClass.java:62)
                */
            public android.content.Entity getEntityAndIncrementCursor(android.database.Cursor r22) throws android.os.RemoteException {
                /*
                r21 = this;
                r1 = r21;
                r2 = r22;
                r0 = "_id";
                r3 = r2.getColumnIndexOrThrow(r0);
                r3 = r2.getLong(r3);
                r5 = new android.content.ContentValues;
                r5.<init>();
                r6 = java.lang.Long.valueOf(r3);
                r5.put((java.lang.String) r0, r6);
                r6 = "calendar_id";
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r2, r5, r6);
                r6 = "title";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "description";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "eventLocation";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "eventStatus";
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r2, r5, r6);
                r6 = "selfAttendeeStatus";
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r2, r5, r6);
                r6 = "dtstart";
                android.database.DatabaseUtils.cursorLongToContentValuesIfPresent(r2, r5, r6);
                r6 = "dtend";
                android.database.DatabaseUtils.cursorLongToContentValuesIfPresent(r2, r5, r6);
                r6 = "duration";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "eventTimezone";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "eventEndTimezone";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "allDay";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "accessLevel";
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r2, r5, r6);
                r6 = "availability";
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r2, r5, r6);
                r6 = "eventColor";
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r2, r5, r6);
                r6 = "eventColor_index";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "hasAlarm";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "hasExtendedProperties";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "rrule";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "rdate";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "exrule";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "exdate";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "original_sync_id";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "original_id";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "originalInstanceTime";
                android.database.DatabaseUtils.cursorLongToContentValuesIfPresent(r2, r5, r6);
                r6 = "originalAllDay";
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r2, r5, r6);
                r6 = "lastDate";
                android.database.DatabaseUtils.cursorLongToContentValuesIfPresent(r2, r5, r6);
                r6 = "hasAttendeeData";
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r2, r5, r6);
                r6 = "guestsCanInviteOthers";
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r2, r5, r6);
                r6 = "guestsCanModify";
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r2, r5, r6);
                r6 = "guestsCanSeeGuests";
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r2, r5, r6);
                r6 = "customAppPackage";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "customAppUri";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "uid2445";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "organizer";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "isOrganizer";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "_sync_id";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "dirty";
                android.database.DatabaseUtils.cursorLongToContentValuesIfPresent(r2, r5, r6);
                r6 = "mutators";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "lastSynced";
                android.database.DatabaseUtils.cursorLongToContentValuesIfPresent(r2, r5, r6);
                r6 = "deleted";
                android.database.DatabaseUtils.cursorIntToContentValuesIfPresent(r2, r5, r6);
                r6 = "sync_data1";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "sync_data2";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "sync_data3";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "sync_data4";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "sync_data5";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "sync_data6";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "sync_data7";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "sync_data8";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "sync_data9";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "sync_data10";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "cal_sync1";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "cal_sync2";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "cal_sync3";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "cal_sync4";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "cal_sync5";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "cal_sync6";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "cal_sync7";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "cal_sync8";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "cal_sync9";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = "cal_sync10";
                android.database.DatabaseUtils.cursorStringToContentValuesIfPresent(r2, r5, r6);
                r6 = new android.content.Entity;
                r6.<init>(r5);
                r7 = r1.mResolver;
                r13 = 0;
                r14 = 1;
                if (r7 == 0) goto L_0x0188;
                r8 = android.provider.CalendarContract.Reminders.CONTENT_URI;
                r9 = REMINDERS_PROJECTION;
                r11 = new java.lang.String[r14];
                r10 = java.lang.Long.toString(r3);
                r11[r13] = r10;
                r12 = 0;
                r10 = "event_id=?";
                r7 = r7.query(r8, r9, r10, r11, r12);
                goto L_0x01a0;
                r15 = r1.mProvider;
                r16 = android.provider.CalendarContract.Reminders.CONTENT_URI;
                r17 = REMINDERS_PROJECTION;
                r7 = new java.lang.String[r14];
                r8 = java.lang.Long.toString(r3);
                r7[r13] = r8;
                r20 = 0;
                r18 = "event_id=?";
                r19 = r7;
                r7 = r15.query(r16, r17, r18, r19, r20);
                r8 = r7.moveToNext();	 Catch:{ all -> 0x02db }
                if (r8 == 0) goto L_0x01cd;	 Catch:{ all -> 0x02db }
                r8 = new android.content.ContentValues;	 Catch:{ all -> 0x02db }
                r8.<init>();	 Catch:{ all -> 0x02db }
                r9 = "minutes";	 Catch:{ all -> 0x02db }
                r10 = r7.getInt(r13);	 Catch:{ all -> 0x02db }
                r10 = java.lang.Integer.valueOf(r10);	 Catch:{ all -> 0x02db }
                r8.put(r9, r10);	 Catch:{ all -> 0x02db }
                r9 = "method";	 Catch:{ all -> 0x02db }
                r10 = r7.getInt(r14);	 Catch:{ all -> 0x02db }
                r10 = java.lang.Integer.valueOf(r10);	 Catch:{ all -> 0x02db }
                r8.put(r9, r10);	 Catch:{ all -> 0x02db }
                r9 = android.provider.CalendarContract.Reminders.CONTENT_URI;	 Catch:{ all -> 0x02db }
                r6.addSubValue(r9, r8);	 Catch:{ all -> 0x02db }
                goto L_0x01a0;
                r7.close();
                r15 = r1.mResolver;
                if (r15 == 0) goto L_0x01ec;
                r16 = android.provider.CalendarContract.Attendees.CONTENT_URI;
                r17 = ATTENDEES_PROJECTION;
                r8 = new java.lang.String[r14];
                r9 = java.lang.Long.toString(r3);
                r8[r13] = r9;
                r20 = 0;
                r18 = "event_id=?";
                r19 = r8;
                r7 = r15.query(r16, r17, r18, r19, r20);
                goto L_0x0204;
                r15 = r1.mProvider;
                r16 = android.provider.CalendarContract.Attendees.CONTENT_URI;
                r17 = ATTENDEES_PROJECTION;
                r8 = new java.lang.String[r14];
                r9 = java.lang.Long.toString(r3);
                r8[r13] = r9;
                r20 = 0;
                r18 = "event_id=?";
                r19 = r8;
                r7 = r15.query(r16, r17, r18, r19, r20);
                r8 = r7.moveToNext();	 Catch:{ all -> 0x02d6 }
                r9 = 2;	 Catch:{ all -> 0x02d6 }
                if (r8 == 0) goto L_0x0265;	 Catch:{ all -> 0x02d6 }
                r8 = new android.content.ContentValues;	 Catch:{ all -> 0x02d6 }
                r8.<init>();	 Catch:{ all -> 0x02d6 }
                r10 = "attendeeName";	 Catch:{ all -> 0x02d6 }
                r11 = r7.getString(r13);	 Catch:{ all -> 0x02d6 }
                r8.put(r10, r11);	 Catch:{ all -> 0x02d6 }
                r10 = "attendeeEmail";	 Catch:{ all -> 0x02d6 }
                r11 = r7.getString(r14);	 Catch:{ all -> 0x02d6 }
                r8.put(r10, r11);	 Catch:{ all -> 0x02d6 }
                r10 = "attendeeRelationship";	 Catch:{ all -> 0x02d6 }
                r9 = r7.getInt(r9);	 Catch:{ all -> 0x02d6 }
                r9 = java.lang.Integer.valueOf(r9);	 Catch:{ all -> 0x02d6 }
                r8.put(r10, r9);	 Catch:{ all -> 0x02d6 }
                r9 = "attendeeType";	 Catch:{ all -> 0x02d6 }
                r10 = 3;	 Catch:{ all -> 0x02d6 }
                r10 = r7.getInt(r10);	 Catch:{ all -> 0x02d6 }
                r10 = java.lang.Integer.valueOf(r10);	 Catch:{ all -> 0x02d6 }
                r8.put(r9, r10);	 Catch:{ all -> 0x02d6 }
                r9 = "attendeeStatus";	 Catch:{ all -> 0x02d6 }
                r10 = 4;	 Catch:{ all -> 0x02d6 }
                r10 = r7.getInt(r10);	 Catch:{ all -> 0x02d6 }
                r10 = java.lang.Integer.valueOf(r10);	 Catch:{ all -> 0x02d6 }
                r8.put(r9, r10);	 Catch:{ all -> 0x02d6 }
                r9 = "attendeeIdentity";	 Catch:{ all -> 0x02d6 }
                r10 = 5;	 Catch:{ all -> 0x02d6 }
                r10 = r7.getString(r10);	 Catch:{ all -> 0x02d6 }
                r8.put(r9, r10);	 Catch:{ all -> 0x02d6 }
                r9 = "attendeeIdNamespace";	 Catch:{ all -> 0x02d6 }
                r10 = 6;	 Catch:{ all -> 0x02d6 }
                r10 = r7.getString(r10);	 Catch:{ all -> 0x02d6 }
                r8.put(r9, r10);	 Catch:{ all -> 0x02d6 }
                r9 = android.provider.CalendarContract.Attendees.CONTENT_URI;	 Catch:{ all -> 0x02d6 }
                r6.addSubValue(r9, r8);	 Catch:{ all -> 0x02d6 }
                goto L_0x0204;
                r7.close();
                r15 = r1.mResolver;
                if (r15 == 0) goto L_0x0284;
                r16 = android.provider.CalendarContract.ExtendedProperties.CONTENT_URI;
                r17 = EXTENDED_PROJECTION;
                r8 = new java.lang.String[r14];
                r10 = java.lang.Long.toString(r3);
                r8[r13] = r10;
                r20 = 0;
                r18 = "event_id=?";
                r19 = r8;
                r7 = r15.query(r16, r17, r18, r19, r20);
                goto L_0x029c;
                r15 = r1.mProvider;
                r16 = android.provider.CalendarContract.ExtendedProperties.CONTENT_URI;
                r17 = EXTENDED_PROJECTION;
                r8 = new java.lang.String[r14];
                r10 = java.lang.Long.toString(r3);
                r8[r13] = r10;
                r20 = 0;
                r18 = "event_id=?";
                r19 = r8;
                r7 = r15.query(r16, r17, r18, r19, r20);
                r8 = r7.moveToNext();	 Catch:{ all -> 0x02d1 }
                if (r8 == 0) goto L_0x02c9;	 Catch:{ all -> 0x02d1 }
                r8 = new android.content.ContentValues;	 Catch:{ all -> 0x02d1 }
                r8.<init>();	 Catch:{ all -> 0x02d1 }
                r10 = r7.getString(r13);	 Catch:{ all -> 0x02d1 }
                r8.put(r0, r10);	 Catch:{ all -> 0x02d1 }
                r10 = "name";	 Catch:{ all -> 0x02d1 }
                r11 = r7.getString(r14);	 Catch:{ all -> 0x02d1 }
                r8.put(r10, r11);	 Catch:{ all -> 0x02d1 }
                r10 = "value";	 Catch:{ all -> 0x02d1 }
                r11 = r7.getString(r9);	 Catch:{ all -> 0x02d1 }
                r8.put(r10, r11);	 Catch:{ all -> 0x02d1 }
                r10 = android.provider.CalendarContract.ExtendedProperties.CONTENT_URI;	 Catch:{ all -> 0x02d1 }
                r6.addSubValue(r10, r8);	 Catch:{ all -> 0x02d1 }
                goto L_0x029c;
                r7.close();
                r22.moveToNext();
                return r6;
                r0 = move-exception;
                r7.close();
                throw r0;
                r0 = move-exception;
                r7.close();
                throw r0;
                r0 = move-exception;
                r7.close();
                throw r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.provider.CalendarContract$EventsEntity$EntityIteratorImpl.getEntityAndIncrementCursor(android.database.Cursor):android.content.Entity");
            }

            public EntityIteratorImpl(Cursor cursor, ContentResolver resolver) {
                super(cursor);
                this.mResolver = resolver;
                this.mProvider = null;
            }

            public EntityIteratorImpl(Cursor cursor, ContentProviderClient provider) {
                super(cursor);
                this.mResolver = null;
                this.mProvider = provider;
            }
        }

        private EventsEntity() {
        }

        public static EntityIterator newEntityIterator(Cursor cursor, ContentResolver resolver) {
            return new EntityIteratorImpl(cursor, resolver);
        }

        public static EntityIterator newEntityIterator(Cursor cursor, ContentProviderClient provider) {
            return new EntityIteratorImpl(cursor, provider);
        }
    }

    protected interface EventsRawTimesColumns {
        public static final String DTEND_2445 = "dtend2445";
        public static final String DTSTART_2445 = "dtstart2445";
        public static final String EVENT_ID = "event_id";
        public static final String LAST_DATE_2445 = "lastDate2445";
        public static final String ORIGINAL_INSTANCE_TIME_2445 = "originalInstanceTime2445";
    }

    public static final class EventsRawTimes implements BaseColumns, EventsRawTimesColumns {
        private EventsRawTimes() {
        }
    }

    protected interface ExtendedPropertiesColumns {
        public static final String EVENT_ID = "event_id";
        public static final String NAME = "name";
        public static final String VALUE = "value";
    }

    public static final class ExtendedProperties implements BaseColumns, ExtendedPropertiesColumns, EventsColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/extendedproperties");

        private ExtendedProperties() {
        }
    }

    public static final class Instances implements BaseColumns, EventsColumns, CalendarColumns {
        public static final String BEGIN = "begin";
        public static final Uri CONTENT_BY_DAY_URI = Uri.parse("content://com.android.calendar/instances/whenbyday");
        public static final Uri CONTENT_SEARCH_BY_DAY_URI = Uri.parse("content://com.android.calendar/instances/searchbyday");
        public static final Uri CONTENT_SEARCH_URI = Uri.parse("content://com.android.calendar/instances/search");
        public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/instances/when");
        private static final String DEFAULT_SORT_ORDER = "begin ASC";
        public static final String END = "end";
        public static final String END_DAY = "endDay";
        public static final String END_MINUTE = "endMinute";
        public static final Uri ENTERPRISE_CONTENT_BY_DAY_URI = Uri.parse("content://com.android.calendar/enterprise/instances/whenbyday");
        public static final Uri ENTERPRISE_CONTENT_SEARCH_BY_DAY_URI = Uri.parse("content://com.android.calendar/enterprise/instances/searchbyday");
        public static final Uri ENTERPRISE_CONTENT_SEARCH_URI = Uri.parse("content://com.android.calendar/enterprise/instances/search");
        public static final Uri ENTERPRISE_CONTENT_URI = Uri.parse("content://com.android.calendar/enterprise/instances/when");
        public static final String EVENT_ID = "event_id";
        public static final String START_DAY = "startDay";
        public static final String START_MINUTE = "startMinute";
        private static final String[] WHERE_CALENDARS_ARGS = new String[]{"1"};
        private static final String WHERE_CALENDARS_SELECTED = "visible=?";

        private Instances() {
        }

        public static final Cursor query(ContentResolver cr, String[] projection, long begin, long end) {
            SeempLog.record(54);
            Builder builder = CONTENT_URI.buildUpon();
            ContentUris.appendId(builder, begin);
            ContentUris.appendId(builder, end);
            return cr.query(builder.build(), projection, WHERE_CALENDARS_SELECTED, WHERE_CALENDARS_ARGS, DEFAULT_SORT_ORDER);
        }

        public static final Cursor query(ContentResolver cr, String[] projection, long begin, long end, String searchQuery) {
            SeempLog.record(54);
            Builder builder = CONTENT_SEARCH_URI.buildUpon();
            ContentUris.appendId(builder, begin);
            ContentUris.appendId(builder, end);
            return cr.query(builder.appendPath(searchQuery).build(), projection, WHERE_CALENDARS_SELECTED, WHERE_CALENDARS_ARGS, DEFAULT_SORT_ORDER);
        }
    }

    protected interface RemindersColumns {
        public static final String EVENT_ID = "event_id";
        public static final String METHOD = "method";
        public static final int METHOD_ALARM = 4;
        public static final int METHOD_ALERT = 1;
        public static final int METHOD_DEFAULT = 0;
        public static final int METHOD_EMAIL = 2;
        public static final int METHOD_SMS = 3;
        public static final String MINUTES = "minutes";
        public static final int MINUTES_DEFAULT = -1;
    }

    public static final class Reminders implements BaseColumns, RemindersColumns, EventsColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://com.android.calendar/reminders");
        private static final String REMINDERS_WHERE = "event_id=?";

        private Reminders() {
        }

        public static final Cursor query(ContentResolver cr, long eventId, String[] projection) {
            SeempLog.record(54);
            return cr.query(CONTENT_URI, projection, REMINDERS_WHERE, new String[]{Long.toString(eventId)}, null);
        }
    }

    public static final class SyncState implements Columns {
        private static final String CONTENT_DIRECTORY = "syncstate";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(CalendarContract.CONTENT_URI, "syncstate");

        private SyncState() {
        }
    }

    private CalendarContract() {
    }

    public static boolean startViewCalendarEventInManagedProfile(Context context, long eventId, long startMs, long endMs, boolean allDay, int flags) {
        Context context2 = context;
        Preconditions.checkNotNull(context, "Context is null");
        return ((DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE)).startViewCalendarEventInManagedProfile(eventId, startMs, endMs, allDay, flags);
    }
}
