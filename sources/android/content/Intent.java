package android.content;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.backup.FullBackup;
import android.content.ClipData.Item;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IncidentManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.os.ShellCommand;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import android.util.proto.ProtoOutputStream;
import com.android.internal.R;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import org.apache.miui.commons.lang3.ClassUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class Intent implements Parcelable, Cloneable {
    public static final String ACTION_ADVANCED_SETTINGS_CHANGED = "android.intent.action.ADVANCED_SETTINGS";
    public static final String ACTION_AIRPLANE_MODE_CHANGED = "android.intent.action.AIRPLANE_MODE";
    @UnsupportedAppUsage
    public static final String ACTION_ALARM_CHANGED = "android.intent.action.ALARM_CHANGED";
    public static final String ACTION_ALL_APPS = "android.intent.action.ALL_APPS";
    public static final String ACTION_ANSWER = "android.intent.action.ANSWER";
    public static final String ACTION_APPLICATION_PREFERENCES = "android.intent.action.APPLICATION_PREFERENCES";
    public static final String ACTION_APPLICATION_RESTRICTIONS_CHANGED = "android.intent.action.APPLICATION_RESTRICTIONS_CHANGED";
    public static final String ACTION_APP_ERROR = "android.intent.action.APP_ERROR";
    public static final String ACTION_ASSIST = "android.intent.action.ASSIST";
    public static final String ACTION_ATTACH_DATA = "android.intent.action.ATTACH_DATA";
    public static final String ACTION_BATTERY_CHANGED = "android.intent.action.BATTERY_CHANGED";
    @SystemApi
    public static final String ACTION_BATTERY_LEVEL_CHANGED = "android.intent.action.BATTERY_LEVEL_CHANGED";
    public static final String ACTION_BATTERY_LOW = "android.intent.action.BATTERY_LOW";
    public static final String ACTION_BATTERY_OKAY = "android.intent.action.BATTERY_OKAY";
    public static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    public static final String ACTION_BUG_REPORT = "android.intent.action.BUG_REPORT";
    public static final String ACTION_CALL = "android.intent.action.CALL";
    public static final String ACTION_CALL_BUTTON = "android.intent.action.CALL_BUTTON";
    @SystemApi
    public static final String ACTION_CALL_EMERGENCY = "android.intent.action.CALL_EMERGENCY";
    @SystemApi
    public static final String ACTION_CALL_PRIVILEGED = "android.intent.action.CALL_PRIVILEGED";
    public static final String ACTION_CAMERA_BUTTON = "android.intent.action.CAMERA_BUTTON";
    public static final String ACTION_CANCEL_ENABLE_ROLLBACK = "android.intent.action.CANCEL_ENABLE_ROLLBACK";
    public static final String ACTION_CARRIER_SETUP = "android.intent.action.CARRIER_SETUP";
    public static final String ACTION_CHOOSER = "android.intent.action.CHOOSER";
    public static final String ACTION_CLEAR_DNS_CACHE = "android.intent.action.CLEAR_DNS_CACHE";
    public static final String ACTION_CLOSE_SYSTEM_DIALOGS = "android.intent.action.CLOSE_SYSTEM_DIALOGS";
    public static final String ACTION_CONFIGURATION_CHANGED = "android.intent.action.CONFIGURATION_CHANGED";
    public static final String ACTION_CREATE_DOCUMENT = "android.intent.action.CREATE_DOCUMENT";
    public static final String ACTION_CREATE_SHORTCUT = "android.intent.action.CREATE_SHORTCUT";
    public static final String ACTION_DATE_CHANGED = "android.intent.action.DATE_CHANGED";
    public static final String ACTION_DEFAULT = "android.intent.action.VIEW";
    public static final String ACTION_DEFINE = "android.intent.action.DEFINE";
    public static final String ACTION_DELETE = "android.intent.action.DELETE";
    @SystemApi
    public static final String ACTION_DEVICE_CUSTOMIZATION_READY = "android.intent.action.DEVICE_CUSTOMIZATION_READY";
    @SystemApi
    @Deprecated
    public static final String ACTION_DEVICE_INITIALIZATION_WIZARD = "android.intent.action.DEVICE_INITIALIZATION_WIZARD";
    public static final String ACTION_DEVICE_LOCKED_CHANGED = "android.intent.action.DEVICE_LOCKED_CHANGED";
    @Deprecated
    public static final String ACTION_DEVICE_STORAGE_FULL = "android.intent.action.DEVICE_STORAGE_FULL";
    @Deprecated
    public static final String ACTION_DEVICE_STORAGE_LOW = "android.intent.action.DEVICE_STORAGE_LOW";
    @Deprecated
    public static final String ACTION_DEVICE_STORAGE_NOT_FULL = "android.intent.action.DEVICE_STORAGE_NOT_FULL";
    @Deprecated
    public static final String ACTION_DEVICE_STORAGE_OK = "android.intent.action.DEVICE_STORAGE_OK";
    public static final String ACTION_DIAL = "android.intent.action.DIAL";
    public static final String ACTION_DISMISS_KEYBOARD_SHORTCUTS = "com.android.intent.action.DISMISS_KEYBOARD_SHORTCUTS";
    public static final String ACTION_DISTRACTING_PACKAGES_CHANGED = "android.intent.action.DISTRACTING_PACKAGES_CHANGED";
    public static final String ACTION_DOCK_ACTIVE = "android.intent.action.DOCK_ACTIVE";
    public static final String ACTION_DOCK_EVENT = "android.intent.action.DOCK_EVENT";
    public static final String ACTION_DOCK_IDLE = "android.intent.action.DOCK_IDLE";
    public static final String ACTION_DREAMING_STARTED = "android.intent.action.DREAMING_STARTED";
    public static final String ACTION_DREAMING_STOPPED = "android.intent.action.DREAMING_STOPPED";
    public static final String ACTION_DYNAMIC_SENSOR_CHANGED = "android.intent.action.DYNAMIC_SENSOR_CHANGED";
    public static final String ACTION_EDIT = "android.intent.action.EDIT";
    public static final String ACTION_EXTERNAL_APPLICATIONS_AVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE";
    public static final String ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE";
    @SystemApi
    public static final String ACTION_FACTORY_RESET = "android.intent.action.FACTORY_RESET";
    public static final String ACTION_FACTORY_TEST = "android.intent.action.FACTORY_TEST";
    public static final String ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT";
    public static final String ACTION_GET_RESTRICTION_ENTRIES = "android.intent.action.GET_RESTRICTION_ENTRIES";
    @SystemApi
    public static final String ACTION_GLOBAL_BUTTON = "android.intent.action.GLOBAL_BUTTON";
    public static final String ACTION_GTALK_SERVICE_CONNECTED = "android.intent.action.GTALK_CONNECTED";
    public static final String ACTION_GTALK_SERVICE_DISCONNECTED = "android.intent.action.GTALK_DISCONNECTED";
    public static final String ACTION_HEADSET_PLUG = "android.intent.action.HEADSET_PLUG";
    public static final String ACTION_IDLE_MAINTENANCE_END = "android.intent.action.ACTION_IDLE_MAINTENANCE_END";
    public static final String ACTION_IDLE_MAINTENANCE_START = "android.intent.action.ACTION_IDLE_MAINTENANCE_START";
    @SystemApi
    public static final String ACTION_INCIDENT_REPORT_READY = "android.intent.action.INCIDENT_REPORT_READY";
    public static final String ACTION_INPUT_METHOD_CHANGED = "android.intent.action.INPUT_METHOD_CHANGED";
    public static final String ACTION_INSERT = "android.intent.action.INSERT";
    public static final String ACTION_INSERT_OR_EDIT = "android.intent.action.INSERT_OR_EDIT";
    public static final String ACTION_INSTALL_FAILURE = "android.intent.action.INSTALL_FAILURE";
    @SystemApi
    public static final String ACTION_INSTALL_INSTANT_APP_PACKAGE = "android.intent.action.INSTALL_INSTANT_APP_PACKAGE";
    @Deprecated
    public static final String ACTION_INSTALL_PACKAGE = "android.intent.action.INSTALL_PACKAGE";
    @SystemApi
    public static final String ACTION_INSTANT_APP_RESOLVER_SETTINGS = "android.intent.action.INSTANT_APP_RESOLVER_SETTINGS";
    @SystemApi
    public static final String ACTION_INTENT_FILTER_NEEDS_VERIFICATION = "android.intent.action.INTENT_FILTER_NEEDS_VERIFICATION";
    public static final String ACTION_LOCALE_CHANGED = "android.intent.action.LOCALE_CHANGED";
    public static final String ACTION_LOCKED_BOOT_COMPLETED = "android.intent.action.LOCKED_BOOT_COMPLETED";
    public static final String ACTION_MAIN = "android.intent.action.MAIN";
    public static final String ACTION_MANAGED_PROFILE_ADDED = "android.intent.action.MANAGED_PROFILE_ADDED";
    public static final String ACTION_MANAGED_PROFILE_AVAILABLE = "android.intent.action.MANAGED_PROFILE_AVAILABLE";
    public static final String ACTION_MANAGED_PROFILE_REMOVED = "android.intent.action.MANAGED_PROFILE_REMOVED";
    public static final String ACTION_MANAGED_PROFILE_UNAVAILABLE = "android.intent.action.MANAGED_PROFILE_UNAVAILABLE";
    public static final String ACTION_MANAGED_PROFILE_UNLOCKED = "android.intent.action.MANAGED_PROFILE_UNLOCKED";
    @SystemApi
    public static final String ACTION_MANAGE_APP_PERMISSION = "android.intent.action.MANAGE_APP_PERMISSION";
    @SystemApi
    public static final String ACTION_MANAGE_APP_PERMISSIONS = "android.intent.action.MANAGE_APP_PERMISSIONS";
    @SystemApi
    public static final String ACTION_MANAGE_DEFAULT_APP = "android.intent.action.MANAGE_DEFAULT_APP";
    public static final String ACTION_MANAGE_NETWORK_USAGE = "android.intent.action.MANAGE_NETWORK_USAGE";
    public static final String ACTION_MANAGE_PACKAGE_STORAGE = "android.intent.action.MANAGE_PACKAGE_STORAGE";
    @SystemApi
    public static final String ACTION_MANAGE_PERMISSIONS = "android.intent.action.MANAGE_PERMISSIONS";
    @SystemApi
    public static final String ACTION_MANAGE_PERMISSION_APPS = "android.intent.action.MANAGE_PERMISSION_APPS";
    @SystemApi
    public static final String ACTION_MANAGE_SPECIAL_APP_ACCESSES = "android.intent.action.MANAGE_SPECIAL_APP_ACCESSES";
    @SystemApi
    @Deprecated
    public static final String ACTION_MASTER_CLEAR = "android.intent.action.MASTER_CLEAR";
    @SystemApi
    public static final String ACTION_MASTER_CLEAR_NOTIFICATION = "android.intent.action.MASTER_CLEAR_NOTIFICATION";
    public static final String ACTION_MEDIA_BAD_REMOVAL = "android.intent.action.MEDIA_BAD_REMOVAL";
    public static final String ACTION_MEDIA_BUTTON = "android.intent.action.MEDIA_BUTTON";
    public static final String ACTION_MEDIA_CHECKING = "android.intent.action.MEDIA_CHECKING";
    public static final String ACTION_MEDIA_EJECT = "android.intent.action.MEDIA_EJECT";
    public static final String ACTION_MEDIA_MOUNTED = "android.intent.action.MEDIA_MOUNTED";
    public static final String ACTION_MEDIA_NOFS = "android.intent.action.MEDIA_NOFS";
    public static final String ACTION_MEDIA_REMOVED = "android.intent.action.MEDIA_REMOVED";
    public static final String ACTION_MEDIA_RESOURCE_GRANTED = "android.intent.action.MEDIA_RESOURCE_GRANTED";
    public static final String ACTION_MEDIA_SCANNER_FINISHED = "android.intent.action.MEDIA_SCANNER_FINISHED";
    @Deprecated
    public static final String ACTION_MEDIA_SCANNER_SCAN_FILE = "android.intent.action.MEDIA_SCANNER_SCAN_FILE";
    public static final String ACTION_MEDIA_SCANNER_STARTED = "android.intent.action.MEDIA_SCANNER_STARTED";
    public static final String ACTION_MEDIA_SHARED = "android.intent.action.MEDIA_SHARED";
    public static final String ACTION_MEDIA_UNMOUNTABLE = "android.intent.action.MEDIA_UNMOUNTABLE";
    public static final String ACTION_MEDIA_UNMOUNTED = "android.intent.action.MEDIA_UNMOUNTED";
    public static final String ACTION_MEDIA_UNSHARED = "android.intent.action.MEDIA_UNSHARED";
    public static final String ACTION_MY_PACKAGE_REPLACED = "android.intent.action.MY_PACKAGE_REPLACED";
    public static final String ACTION_MY_PACKAGE_SUSPENDED = "android.intent.action.MY_PACKAGE_SUSPENDED";
    public static final String ACTION_MY_PACKAGE_UNSUSPENDED = "android.intent.action.MY_PACKAGE_UNSUSPENDED";
    @Deprecated
    public static final String ACTION_NEW_OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL";
    public static final String ACTION_OPEN_DOCUMENT = "android.intent.action.OPEN_DOCUMENT";
    public static final String ACTION_OPEN_DOCUMENT_TREE = "android.intent.action.OPEN_DOCUMENT_TREE";
    public static final String ACTION_OVERLAY_CHANGED = "android.intent.action.OVERLAY_CHANGED";
    public static final String ACTION_PACKAGES_SUSPENDED = "android.intent.action.PACKAGES_SUSPENDED";
    public static final String ACTION_PACKAGES_UNSUSPENDED = "android.intent.action.PACKAGES_UNSUSPENDED";
    public static final String ACTION_PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED";
    public static final String ACTION_PACKAGE_CHANGED = "android.intent.action.PACKAGE_CHANGED";
    public static final String ACTION_PACKAGE_DATA_CLEARED = "android.intent.action.PACKAGE_DATA_CLEARED";
    public static final String ACTION_PACKAGE_ENABLE_ROLLBACK = "android.intent.action.PACKAGE_ENABLE_ROLLBACK";
    public static final String ACTION_PACKAGE_FIRST_LAUNCH = "android.intent.action.PACKAGE_FIRST_LAUNCH";
    public static final String ACTION_PACKAGE_FULLY_REMOVED = "android.intent.action.PACKAGE_FULLY_REMOVED";
    @Deprecated
    public static final String ACTION_PACKAGE_INSTALL = "android.intent.action.PACKAGE_INSTALL";
    public static final String ACTION_PACKAGE_NEEDS_OPTIONAL_VERIFICATION = "com.qualcomm.qti.intent.action.PACKAGE_NEEDS_OPTIONAL_VERIFICATION";
    public static final String ACTION_PACKAGE_NEEDS_VERIFICATION = "android.intent.action.PACKAGE_NEEDS_VERIFICATION";
    public static final String ACTION_PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED";
    public static final String ACTION_PACKAGE_REPLACED = "android.intent.action.PACKAGE_REPLACED";
    public static final String ACTION_PACKAGE_RESTARTED = "android.intent.action.PACKAGE_RESTARTED";
    public static final String ACTION_PACKAGE_VERIFIED = "android.intent.action.PACKAGE_VERIFIED";
    public static final String ACTION_PASTE = "android.intent.action.PASTE";
    @SystemApi
    public static final String ACTION_PENDING_INCIDENT_REPORTS_CHANGED = "android.intent.action.PENDING_INCIDENT_REPORTS_CHANGED";
    public static final String ACTION_PICK = "android.intent.action.PICK";
    public static final String ACTION_PICK_ACTIVITY = "android.intent.action.PICK_ACTIVITY";
    public static final String ACTION_POWER_CONNECTED = "android.intent.action.ACTION_POWER_CONNECTED";
    public static final String ACTION_POWER_DISCONNECTED = "android.intent.action.ACTION_POWER_DISCONNECTED";
    public static final String ACTION_POWER_USAGE_SUMMARY = "android.intent.action.POWER_USAGE_SUMMARY";
    public static final String ACTION_PREFERRED_ACTIVITY_CHANGED = "android.intent.action.ACTION_PREFERRED_ACTIVITY_CHANGED";
    @SystemApi
    public static final String ACTION_PRE_BOOT_COMPLETED = "android.intent.action.PRE_BOOT_COMPLETED";
    public static final String ACTION_PROCESS_TEXT = "android.intent.action.PROCESS_TEXT";
    public static final String ACTION_PROVIDER_CHANGED = "android.intent.action.PROVIDER_CHANGED";
    @SystemApi
    public static final String ACTION_QUERY_PACKAGE_RESTART = "android.intent.action.QUERY_PACKAGE_RESTART";
    public static final String ACTION_QUICK_CLOCK = "android.intent.action.QUICK_CLOCK";
    public static final String ACTION_QUICK_VIEW = "android.intent.action.QUICK_VIEW";
    public static final String ACTION_REBOOT = "android.intent.action.REBOOT";
    public static final String ACTION_REMOTE_INTENT = "com.google.android.c2dm.intent.RECEIVE";
    public static final String ACTION_REQUEST_SHUTDOWN = "com.android.internal.intent.action.REQUEST_SHUTDOWN";
    @SystemApi
    public static final String ACTION_RESOLVE_INSTANT_APP_PACKAGE = "android.intent.action.RESOLVE_INSTANT_APP_PACKAGE";
    @SystemApi
    public static final String ACTION_REVIEW_ACCESSIBILITY_SERVICES = "android.intent.action.REVIEW_ACCESSIBILITY_SERVICES";
    @SystemApi
    public static final String ACTION_REVIEW_ONGOING_PERMISSION_USAGE = "android.intent.action.REVIEW_ONGOING_PERMISSION_USAGE";
    @SystemApi
    public static final String ACTION_REVIEW_PERMISSIONS = "android.intent.action.REVIEW_PERMISSIONS";
    @SystemApi
    public static final String ACTION_REVIEW_PERMISSION_USAGE = "android.intent.action.REVIEW_PERMISSION_USAGE";
    @SystemApi
    public static final String ACTION_ROLLBACK_COMMITTED = "android.intent.action.ROLLBACK_COMMITTED";
    public static final String ACTION_RUN = "android.intent.action.RUN";
    public static final String ACTION_SCREEN_OFF = "android.intent.action.SCREEN_OFF";
    public static final String ACTION_SCREEN_ON = "android.intent.action.SCREEN_ON";
    public static final String ACTION_SEARCH = "android.intent.action.SEARCH";
    public static final String ACTION_SEARCH_LONG_PRESS = "android.intent.action.SEARCH_LONG_PRESS";
    public static final String ACTION_SEND = "android.intent.action.SEND";
    public static final String ACTION_SENDTO = "android.intent.action.SENDTO";
    public static final String ACTION_SEND_MULTIPLE = "android.intent.action.SEND_MULTIPLE";
    @SystemApi
    @Deprecated
    public static final String ACTION_SERVICE_STATE = "android.intent.action.SERVICE_STATE";
    public static final String ACTION_SETTING_RESTORED = "android.os.action.SETTING_RESTORED";
    public static final String ACTION_SET_WALLPAPER = "android.intent.action.SET_WALLPAPER";
    public static final String ACTION_SHOW_APP_INFO = "android.intent.action.SHOW_APP_INFO";
    public static final String ACTION_SHOW_BRIGHTNESS_DIALOG = "com.android.intent.action.SHOW_BRIGHTNESS_DIALOG";
    public static final String ACTION_SHOW_KEYBOARD_SHORTCUTS = "com.android.intent.action.SHOW_KEYBOARD_SHORTCUTS";
    @SystemApi
    public static final String ACTION_SHOW_SUSPENDED_APP_DETAILS = "android.intent.action.SHOW_SUSPENDED_APP_DETAILS";
    public static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";
    @SystemApi
    @Deprecated
    public static final String ACTION_SIM_STATE_CHANGED = "android.intent.action.SIM_STATE_CHANGED";
    @SystemApi
    public static final String ACTION_SPLIT_CONFIGURATION_CHANGED = "android.intent.action.SPLIT_CONFIGURATION_CHANGED";
    public static final String ACTION_SYNC = "android.intent.action.SYNC";
    public static final String ACTION_SYSTEM_TUTORIAL = "android.intent.action.SYSTEM_TUTORIAL";
    public static final String ACTION_THERMAL_EVENT = "android.intent.action.THERMAL_EVENT";
    public static final String ACTION_TIMEZONE_CHANGED = "android.intent.action.TIMEZONE_CHANGED";
    public static final String ACTION_TIME_CHANGED = "android.intent.action.TIME_SET";
    public static final String ACTION_TIME_TICK = "android.intent.action.TIME_TICK";
    public static final String ACTION_TRANSLATE = "android.intent.action.TRANSLATE";
    public static final String ACTION_UID_REMOVED = "android.intent.action.UID_REMOVED";
    @Deprecated
    public static final String ACTION_UMS_CONNECTED = "android.intent.action.UMS_CONNECTED";
    @Deprecated
    public static final String ACTION_UMS_DISCONNECTED = "android.intent.action.UMS_DISCONNECTED";
    @Deprecated
    public static final String ACTION_UNINSTALL_PACKAGE = "android.intent.action.UNINSTALL_PACKAGE";
    @SystemApi
    public static final String ACTION_UPGRADE_SETUP = "android.intent.action.UPGRADE_SETUP";
    @SystemApi
    public static final String ACTION_USER_ADDED = "android.intent.action.USER_ADDED";
    public static final String ACTION_USER_BACKGROUND = "android.intent.action.USER_BACKGROUND";
    public static final String ACTION_USER_FOREGROUND = "android.intent.action.USER_FOREGROUND";
    public static final String ACTION_USER_INFO_CHANGED = "android.intent.action.USER_INFO_CHANGED";
    public static final String ACTION_USER_INITIALIZE = "android.intent.action.USER_INITIALIZE";
    public static final String ACTION_USER_PRESENT = "android.intent.action.USER_PRESENT";
    @SystemApi
    public static final String ACTION_USER_REMOVED = "android.intent.action.USER_REMOVED";
    public static final String ACTION_USER_STARTED = "android.intent.action.USER_STARTED";
    public static final String ACTION_USER_STARTING = "android.intent.action.USER_STARTING";
    public static final String ACTION_USER_STOPPED = "android.intent.action.USER_STOPPED";
    public static final String ACTION_USER_STOPPING = "android.intent.action.USER_STOPPING";
    @UnsupportedAppUsage
    public static final String ACTION_USER_SWITCHED = "android.intent.action.USER_SWITCHED";
    public static final String ACTION_USER_UNLOCKED = "android.intent.action.USER_UNLOCKED";
    public static final String ACTION_VIEW = "android.intent.action.VIEW";
    public static final String ACTION_VIEW_LOCUS = "android.intent.action.VIEW_LOCUS";
    public static final String ACTION_VIEW_PERMISSION_USAGE = "android.intent.action.VIEW_PERMISSION_USAGE";
    @SystemApi
    public static final String ACTION_VOICE_ASSIST = "android.intent.action.VOICE_ASSIST";
    public static final String ACTION_VOICE_COMMAND = "android.intent.action.VOICE_COMMAND";
    @Deprecated
    public static final String ACTION_WALLPAPER_CHANGED = "android.intent.action.WALLPAPER_CHANGED";
    public static final String ACTION_WEB_SEARCH = "android.intent.action.WEB_SEARCH";
    private static final String ATTR_ACTION = "action";
    private static final String ATTR_CATEGORY = "category";
    private static final String ATTR_COMPONENT = "component";
    private static final String ATTR_DATA = "data";
    private static final String ATTR_FLAGS = "flags";
    private static final String ATTR_IDENTIFIER = "ident";
    private static final String ATTR_TYPE = "type";
    public static final String CATEGORY_ALTERNATIVE = "android.intent.category.ALTERNATIVE";
    public static final String CATEGORY_APP_BROWSER = "android.intent.category.APP_BROWSER";
    public static final String CATEGORY_APP_CALCULATOR = "android.intent.category.APP_CALCULATOR";
    public static final String CATEGORY_APP_CALENDAR = "android.intent.category.APP_CALENDAR";
    public static final String CATEGORY_APP_CONTACTS = "android.intent.category.APP_CONTACTS";
    public static final String CATEGORY_APP_EMAIL = "android.intent.category.APP_EMAIL";
    public static final String CATEGORY_APP_FILES = "android.intent.category.APP_FILES";
    public static final String CATEGORY_APP_GALLERY = "android.intent.category.APP_GALLERY";
    public static final String CATEGORY_APP_MAPS = "android.intent.category.APP_MAPS";
    public static final String CATEGORY_APP_MARKET = "android.intent.category.APP_MARKET";
    public static final String CATEGORY_APP_MESSAGING = "android.intent.category.APP_MESSAGING";
    public static final String CATEGORY_APP_MUSIC = "android.intent.category.APP_MUSIC";
    public static final String CATEGORY_BROWSABLE = "android.intent.category.BROWSABLE";
    public static final String CATEGORY_CAR_DOCK = "android.intent.category.CAR_DOCK";
    public static final String CATEGORY_CAR_LAUNCHER = "android.intent.category.CAR_LAUNCHER";
    public static final String CATEGORY_CAR_MODE = "android.intent.category.CAR_MODE";
    public static final String CATEGORY_DEFAULT = "android.intent.category.DEFAULT";
    public static final String CATEGORY_DESK_DOCK = "android.intent.category.DESK_DOCK";
    public static final String CATEGORY_DEVELOPMENT_PREFERENCE = "android.intent.category.DEVELOPMENT_PREFERENCE";
    public static final String CATEGORY_EMBED = "android.intent.category.EMBED";
    public static final String CATEGORY_FRAMEWORK_INSTRUMENTATION_TEST = "android.intent.category.FRAMEWORK_INSTRUMENTATION_TEST";
    public static final String CATEGORY_HE_DESK_DOCK = "android.intent.category.HE_DESK_DOCK";
    public static final String CATEGORY_HOME = "android.intent.category.HOME";
    public static final String CATEGORY_HOME_MAIN = "android.intent.category.HOME_MAIN";
    public static final String CATEGORY_INFO = "android.intent.category.INFO";
    public static final String CATEGORY_LAUNCHER = "android.intent.category.LAUNCHER";
    public static final String CATEGORY_LAUNCHER_APP = "android.intent.category.LAUNCHER_APP";
    public static final String CATEGORY_LEANBACK_LAUNCHER = "android.intent.category.LEANBACK_LAUNCHER";
    @SystemApi
    public static final String CATEGORY_LEANBACK_SETTINGS = "android.intent.category.LEANBACK_SETTINGS";
    public static final String CATEGORY_LE_DESK_DOCK = "android.intent.category.LE_DESK_DOCK";
    public static final String CATEGORY_MONKEY = "android.intent.category.MONKEY";
    public static final String CATEGORY_OPENABLE = "android.intent.category.OPENABLE";
    public static final String CATEGORY_PREFERENCE = "android.intent.category.PREFERENCE";
    public static final String CATEGORY_SAMPLE_CODE = "android.intent.category.SAMPLE_CODE";
    public static final String CATEGORY_SECONDARY_HOME = "android.intent.category.SECONDARY_HOME";
    public static final String CATEGORY_SELECTED_ALTERNATIVE = "android.intent.category.SELECTED_ALTERNATIVE";
    public static final String CATEGORY_SETUP_WIZARD = "android.intent.category.SETUP_WIZARD";
    public static final String CATEGORY_TAB = "android.intent.category.TAB";
    public static final String CATEGORY_TEST = "android.intent.category.TEST";
    public static final String CATEGORY_TYPED_OPENABLE = "android.intent.category.TYPED_OPENABLE";
    public static final String CATEGORY_UNIT_TEST = "android.intent.category.UNIT_TEST";
    public static final String CATEGORY_VOICE = "android.intent.category.VOICE";
    public static final String CATEGORY_VR_HOME = "android.intent.category.VR_HOME";
    private static final int COPY_MODE_ALL = 0;
    private static final int COPY_MODE_FILTER = 1;
    private static final int COPY_MODE_HISTORY = 2;
    public static final Creator<Intent> CREATOR = new Creator<Intent>() {
        public Intent createFromParcel(Parcel in) {
            return new Intent(in);
        }

        public Intent[] newArray(int size) {
            return new Intent[size];
        }
    };
    public static final String EXTRA_ALARM_COUNT = "android.intent.extra.ALARM_COUNT";
    public static final String EXTRA_ALLOW_MULTIPLE = "android.intent.extra.ALLOW_MULTIPLE";
    @Deprecated
    public static final String EXTRA_ALLOW_REPLACE = "android.intent.extra.ALLOW_REPLACE";
    public static final String EXTRA_ALTERNATE_INTENTS = "android.intent.extra.ALTERNATE_INTENTS";
    public static final String EXTRA_ASSIST_CONTEXT = "android.intent.extra.ASSIST_CONTEXT";
    public static final String EXTRA_ASSIST_INPUT_DEVICE_ID = "android.intent.extra.ASSIST_INPUT_DEVICE_ID";
    public static final String EXTRA_ASSIST_INPUT_HINT_KEYBOARD = "android.intent.extra.ASSIST_INPUT_HINT_KEYBOARD";
    public static final String EXTRA_ASSIST_PACKAGE = "android.intent.extra.ASSIST_PACKAGE";
    public static final String EXTRA_ASSIST_UID = "android.intent.extra.ASSIST_UID";
    public static final String EXTRA_AUTO_LAUNCH_SINGLE_CHOICE = "android.intent.extra.AUTO_LAUNCH_SINGLE_CHOICE";
    public static final String EXTRA_BCC = "android.intent.extra.BCC";
    public static final String EXTRA_BUG_REPORT = "android.intent.extra.BUG_REPORT";
    @SystemApi
    public static final String EXTRA_CALLING_PACKAGE = "android.intent.extra.CALLING_PACKAGE";
    public static final String EXTRA_CC = "android.intent.extra.CC";
    @SystemApi
    @Deprecated
    public static final String EXTRA_CDMA_DEFAULT_ROAMING_INDICATOR = "cdmaDefaultRoamingIndicator";
    @SystemApi
    @Deprecated
    public static final String EXTRA_CDMA_ROAMING_INDICATOR = "cdmaRoamingIndicator";
    @Deprecated
    public static final String EXTRA_CHANGED_COMPONENT_NAME = "android.intent.extra.changed_component_name";
    public static final String EXTRA_CHANGED_COMPONENT_NAME_LIST = "android.intent.extra.changed_component_name_list";
    public static final String EXTRA_CHANGED_PACKAGE_LIST = "android.intent.extra.changed_package_list";
    public static final String EXTRA_CHANGED_UID_LIST = "android.intent.extra.changed_uid_list";
    public static final String EXTRA_CHOOSER_REFINEMENT_INTENT_SENDER = "android.intent.extra.CHOOSER_REFINEMENT_INTENT_SENDER";
    public static final String EXTRA_CHOOSER_TARGETS = "android.intent.extra.CHOOSER_TARGETS";
    public static final String EXTRA_CHOSEN_COMPONENT = "android.intent.extra.CHOSEN_COMPONENT";
    public static final String EXTRA_CHOSEN_COMPONENT_INTENT_SENDER = "android.intent.extra.CHOSEN_COMPONENT_INTENT_SENDER";
    public static final String EXTRA_CLIENT_INTENT = "android.intent.extra.client_intent";
    public static final String EXTRA_CLIENT_LABEL = "android.intent.extra.client_label";
    public static final String EXTRA_COMPONENT_NAME = "android.intent.extra.COMPONENT_NAME";
    public static final String EXTRA_CONTENT_ANNOTATIONS = "android.intent.extra.CONTENT_ANNOTATIONS";
    public static final String EXTRA_CONTENT_QUERY = "android.intent.extra.CONTENT_QUERY";
    @SystemApi
    @Deprecated
    public static final String EXTRA_CSS_INDICATOR = "cssIndicator";
    @SystemApi
    @Deprecated
    public static final String EXTRA_DATA_OPERATOR_ALPHA_LONG = "data-operator-alpha-long";
    @SystemApi
    @Deprecated
    public static final String EXTRA_DATA_OPERATOR_ALPHA_SHORT = "data-operator-alpha-short";
    @SystemApi
    @Deprecated
    public static final String EXTRA_DATA_OPERATOR_NUMERIC = "data-operator-numeric";
    @SystemApi
    @Deprecated
    public static final String EXTRA_DATA_RADIO_TECH = "dataRadioTechnology";
    @SystemApi
    @Deprecated
    public static final String EXTRA_DATA_REG_STATE = "dataRegState";
    public static final String EXTRA_DATA_REMOVED = "android.intent.extra.DATA_REMOVED";
    @SystemApi
    @Deprecated
    public static final String EXTRA_DATA_ROAMING_TYPE = "dataRoamingType";
    public static final String EXTRA_DISTRACTION_RESTRICTIONS = "android.intent.extra.distraction_restrictions";
    public static final String EXTRA_DOCK_STATE = "android.intent.extra.DOCK_STATE";
    public static final int EXTRA_DOCK_STATE_CAR = 2;
    public static final int EXTRA_DOCK_STATE_DESK = 1;
    public static final int EXTRA_DOCK_STATE_HE_DESK = 4;
    public static final int EXTRA_DOCK_STATE_LE_DESK = 3;
    public static final int EXTRA_DOCK_STATE_UNDOCKED = 0;
    public static final String EXTRA_DONT_KILL_APP = "android.intent.extra.DONT_KILL_APP";
    public static final String EXTRA_DURATION_MILLIS = "android.intent.extra.DURATION_MILLIS";
    public static final String EXTRA_EMAIL = "android.intent.extra.EMAIL";
    @SystemApi
    @Deprecated
    public static final String EXTRA_EMERGENCY_ONLY = "emergencyOnly";
    public static final String EXTRA_EXCLUDE_COMPONENTS = "android.intent.extra.EXCLUDE_COMPONENTS";
    @SystemApi
    public static final String EXTRA_FORCE_FACTORY_RESET = "android.intent.extra.FORCE_FACTORY_RESET";
    @Deprecated
    public static final String EXTRA_FORCE_MASTER_CLEAR = "android.intent.extra.FORCE_MASTER_CLEAR";
    public static final String EXTRA_FROM_STORAGE = "android.intent.extra.FROM_STORAGE";
    public static final String EXTRA_HTML_TEXT = "android.intent.extra.HTML_TEXT";
    public static final String EXTRA_INDEX = "android.intent.extra.INDEX";
    public static final String EXTRA_INITIAL_INTENTS = "android.intent.extra.INITIAL_INTENTS";
    public static final String EXTRA_INSTALLER_PACKAGE_NAME = "android.intent.extra.INSTALLER_PACKAGE_NAME";
    public static final String EXTRA_INSTALL_RESULT = "android.intent.extra.INSTALL_RESULT";
    @SystemApi
    public static final String EXTRA_INSTANT_APP_ACTION = "android.intent.extra.INSTANT_APP_ACTION";
    @SystemApi
    public static final String EXTRA_INSTANT_APP_BUNDLES = "android.intent.extra.INSTANT_APP_BUNDLES";
    @SystemApi
    public static final String EXTRA_INSTANT_APP_EXTRAS = "android.intent.extra.INSTANT_APP_EXTRAS";
    @SystemApi
    public static final String EXTRA_INSTANT_APP_FAILURE = "android.intent.extra.INSTANT_APP_FAILURE";
    @SystemApi
    public static final String EXTRA_INSTANT_APP_HOSTNAME = "android.intent.extra.INSTANT_APP_HOSTNAME";
    @SystemApi
    public static final String EXTRA_INSTANT_APP_SUCCESS = "android.intent.extra.INSTANT_APP_SUCCESS";
    @SystemApi
    public static final String EXTRA_INSTANT_APP_TOKEN = "android.intent.extra.INSTANT_APP_TOKEN";
    public static final String EXTRA_INTENT = "android.intent.extra.INTENT";
    @SystemApi
    @Deprecated
    public static final String EXTRA_IS_DATA_ROAMING_FROM_REGISTRATION = "isDataRoamingFromRegistration";
    @SystemApi
    @Deprecated
    public static final String EXTRA_IS_USING_CARRIER_AGGREGATION = "isUsingCarrierAggregation";
    public static final String EXTRA_KEY_CONFIRM = "android.intent.extra.KEY_CONFIRM";
    public static final String EXTRA_KEY_EVENT = "android.intent.extra.KEY_EVENT";
    public static final String EXTRA_LAUNCHER_EXTRAS = "android.intent.extra.LAUNCHER_EXTRAS";
    public static final String EXTRA_LOCAL_ONLY = "android.intent.extra.LOCAL_ONLY";
    public static final String EXTRA_LOCUS_ID = "android.intent.extra.LOCUS_ID";
    @SystemApi
    public static final String EXTRA_LONG_VERSION_CODE = "android.intent.extra.LONG_VERSION_CODE";
    @SystemApi
    @Deprecated
    public static final String EXTRA_LTE_EARFCN_RSRP_BOOST = "LteEarfcnRsrpBoost";
    @SystemApi
    @Deprecated
    public static final String EXTRA_MANUAL = "manual";
    public static final String EXTRA_MEDIA_RESOURCE_TYPE = "android.intent.extra.MEDIA_RESOURCE_TYPE";
    public static final int EXTRA_MEDIA_RESOURCE_TYPE_AUDIO_CODEC = 1;
    public static final int EXTRA_MEDIA_RESOURCE_TYPE_VIDEO_CODEC = 0;
    public static final String EXTRA_MIME_TYPES = "android.intent.extra.MIME_TYPES";
    @SystemApi
    @Deprecated
    public static final String EXTRA_NETWORK_ID = "networkId";
    public static final String EXTRA_NOT_UNKNOWN_SOURCE = "android.intent.extra.NOT_UNKNOWN_SOURCE";
    @SystemApi
    @Deprecated
    public static final String EXTRA_OPERATOR_ALPHA_LONG = "operator-alpha-long";
    @SystemApi
    @Deprecated
    public static final String EXTRA_OPERATOR_ALPHA_SHORT = "operator-alpha-short";
    @SystemApi
    @Deprecated
    public static final String EXTRA_OPERATOR_NUMERIC = "operator-numeric";
    @SystemApi
    public static final String EXTRA_ORIGINATING_UID = "android.intent.extra.ORIGINATING_UID";
    public static final String EXTRA_ORIGINATING_URI = "android.intent.extra.ORIGINATING_URI";
    @SystemApi
    public static final String EXTRA_PACKAGES = "android.intent.extra.PACKAGES";
    public static final String EXTRA_PACKAGE_NAME = "android.intent.extra.PACKAGE_NAME";
    @SystemApi
    public static final String EXTRA_PERMISSION_GROUP_NAME = "android.intent.extra.PERMISSION_GROUP_NAME";
    @SystemApi
    public static final String EXTRA_PERMISSION_NAME = "android.intent.extra.PERMISSION_NAME";
    public static final String EXTRA_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";
    public static final String EXTRA_PROCESS_TEXT = "android.intent.extra.PROCESS_TEXT";
    public static final String EXTRA_PROCESS_TEXT_READONLY = "android.intent.extra.PROCESS_TEXT_READONLY";
    @Deprecated
    public static final String EXTRA_QUICK_VIEW_ADVANCED = "android.intent.extra.QUICK_VIEW_ADVANCED";
    public static final String EXTRA_QUICK_VIEW_FEATURES = "android.intent.extra.QUICK_VIEW_FEATURES";
    public static final String EXTRA_QUIET_MODE = "android.intent.extra.QUIET_MODE";
    @SystemApi
    public static final String EXTRA_REASON = "android.intent.extra.REASON";
    public static final String EXTRA_REFERRER = "android.intent.extra.REFERRER";
    public static final String EXTRA_REFERRER_NAME = "android.intent.extra.REFERRER_NAME";
    @SystemApi
    public static final String EXTRA_REMOTE_CALLBACK = "android.intent.extra.REMOTE_CALLBACK";
    public static final String EXTRA_REMOTE_INTENT_TOKEN = "android.intent.extra.remote_intent_token";
    public static final String EXTRA_REMOVED_FOR_ALL_USERS = "android.intent.extra.REMOVED_FOR_ALL_USERS";
    public static final String EXTRA_REPLACEMENT_EXTRAS = "android.intent.extra.REPLACEMENT_EXTRAS";
    public static final String EXTRA_REPLACING = "android.intent.extra.REPLACING";
    public static final String EXTRA_RESTRICTIONS_BUNDLE = "android.intent.extra.restrictions_bundle";
    public static final String EXTRA_RESTRICTIONS_INTENT = "android.intent.extra.restrictions_intent";
    public static final String EXTRA_RESTRICTIONS_LIST = "android.intent.extra.restrictions_list";
    @SystemApi
    public static final String EXTRA_RESULT_NEEDED = "android.intent.extra.RESULT_NEEDED";
    public static final String EXTRA_RESULT_RECEIVER = "android.intent.extra.RESULT_RECEIVER";
    public static final String EXTRA_RETURN_RESULT = "android.intent.extra.RETURN_RESULT";
    @SystemApi
    public static final String EXTRA_ROLE_NAME = "android.intent.extra.ROLE_NAME";
    public static final String EXTRA_SERVICE_STATE = "android.intent.extra.SERVICE_STATE";
    public static final String EXTRA_SETTING_NAME = "setting_name";
    public static final String EXTRA_SETTING_NEW_VALUE = "new_value";
    public static final String EXTRA_SETTING_PREVIOUS_VALUE = "previous_value";
    public static final String EXTRA_SETTING_RESTORED_FROM_SDK_INT = "restored_from_sdk_int";
    @Deprecated
    public static final String EXTRA_SHORTCUT_ICON = "android.intent.extra.shortcut.ICON";
    @Deprecated
    public static final String EXTRA_SHORTCUT_ICON_RESOURCE = "android.intent.extra.shortcut.ICON_RESOURCE";
    public static final String EXTRA_SHORTCUT_ID = "android.intent.extra.shortcut.ID";
    @Deprecated
    public static final String EXTRA_SHORTCUT_INTENT = "android.intent.extra.shortcut.INTENT";
    @Deprecated
    public static final String EXTRA_SHORTCUT_NAME = "android.intent.extra.shortcut.NAME";
    public static final String EXTRA_SHUTDOWN_USERSPACE_ONLY = "android.intent.extra.SHUTDOWN_USERSPACE_ONLY";
    public static final String EXTRA_SIM_ACTIVATION_RESPONSE = "android.intent.extra.SIM_ACTIVATION_RESPONSE";
    public static final String EXTRA_SPLIT_NAME = "android.intent.extra.SPLIT_NAME";
    public static final String EXTRA_STREAM = "android.intent.extra.STREAM";
    public static final String EXTRA_SUBJECT = "android.intent.extra.SUBJECT";
    public static final String EXTRA_SUSPENDED_PACKAGE_EXTRAS = "android.intent.extra.SUSPENDED_PACKAGE_EXTRAS";
    @SystemApi
    @Deprecated
    public static final String EXTRA_SYSTEM_ID = "systemId";
    public static final String EXTRA_TASK_ID = "android.intent.extra.TASK_ID";
    public static final String EXTRA_TEMPLATE = "android.intent.extra.TEMPLATE";
    public static final String EXTRA_TEXT = "android.intent.extra.TEXT";
    public static final String EXTRA_THERMAL_STATE = "android.intent.extra.THERMAL_STATE";
    public static final int EXTRA_THERMAL_STATE_EXCEEDED = 2;
    public static final int EXTRA_THERMAL_STATE_NORMAL = 0;
    public static final int EXTRA_THERMAL_STATE_WARNING = 1;
    public static final String EXTRA_TIME_PREF_24_HOUR_FORMAT = "android.intent.extra.TIME_PREF_24_HOUR_FORMAT";
    public static final int EXTRA_TIME_PREF_VALUE_USE_12_HOUR = 0;
    public static final int EXTRA_TIME_PREF_VALUE_USE_24_HOUR = 1;
    public static final int EXTRA_TIME_PREF_VALUE_USE_LOCALE_DEFAULT = 2;
    public static final String EXTRA_TITLE = "android.intent.extra.TITLE";
    public static final String EXTRA_UID = "android.intent.extra.UID";
    public static final String EXTRA_UNINSTALL_ALL_USERS = "android.intent.extra.UNINSTALL_ALL_USERS";
    @SystemApi
    public static final String EXTRA_UNKNOWN_INSTANT_APP = "android.intent.extra.UNKNOWN_INSTANT_APP";
    public static final String EXTRA_USER = "android.intent.extra.USER";
    public static final String EXTRA_USER_HANDLE = "android.intent.extra.user_handle";
    public static final String EXTRA_USER_ID = "android.intent.extra.USER_ID";
    public static final String EXTRA_USER_REQUESTED_SHUTDOWN = "android.intent.extra.USER_REQUESTED_SHUTDOWN";
    @SystemApi
    public static final String EXTRA_VERIFICATION_BUNDLE = "android.intent.extra.VERIFICATION_BUNDLE";
    @Deprecated
    public static final String EXTRA_VERSION_CODE = "android.intent.extra.VERSION_CODE";
    @SystemApi
    @Deprecated
    public static final String EXTRA_VOICE_RADIO_TECH = "radioTechnology";
    @SystemApi
    @Deprecated
    public static final String EXTRA_VOICE_REG_STATE = "voiceRegState";
    @SystemApi
    @Deprecated
    public static final String EXTRA_VOICE_ROAMING_TYPE = "voiceRoamingType";
    public static final String EXTRA_WIPE_ESIMS = "com.android.internal.intent.extra.WIPE_ESIMS";
    public static final String EXTRA_WIPE_EXTERNAL_STORAGE = "android.intent.extra.WIPE_EXTERNAL_STORAGE";
    public static final int FILL_IN_ACTION = 1;
    public static final int FILL_IN_CATEGORIES = 4;
    public static final int FILL_IN_CLIP_DATA = 128;
    public static final int FILL_IN_COMPONENT = 8;
    public static final int FILL_IN_DATA = 2;
    public static final int FILL_IN_IDENTIFIER = 256;
    public static final int FILL_IN_PACKAGE = 16;
    public static final int FILL_IN_SELECTOR = 64;
    public static final int FILL_IN_SOURCE_BOUNDS = 32;
    public static final int FLAG_ACTIVITY_BROUGHT_TO_FRONT = 4194304;
    public static final int FLAG_ACTIVITY_CLEAR_TASK = 32768;
    public static final int FLAG_ACTIVITY_CLEAR_TOP = 67108864;
    @Deprecated
    public static final int FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET = 524288;
    public static final int FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS = 8388608;
    public static final int FLAG_ACTIVITY_FORWARD_RESULT = 33554432;
    public static final int FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY = 1048576;
    public static final int FLAG_ACTIVITY_LAUNCH_ADJACENT = 4096;
    public static final int FLAG_ACTIVITY_MATCH_EXTERNAL = 2048;
    public static final int FLAG_ACTIVITY_MULTIPLE_TASK = 134217728;
    public static final int FLAG_ACTIVITY_NEW_DOCUMENT = 524288;
    public static final int FLAG_ACTIVITY_NEW_TASK = 268435456;
    public static final int FLAG_ACTIVITY_NO_ANIMATION = 65536;
    public static final int FLAG_ACTIVITY_NO_HISTORY = 1073741824;
    public static final int FLAG_ACTIVITY_NO_USER_ACTION = 262144;
    public static final int FLAG_ACTIVITY_PREVIOUS_IS_TOP = 16777216;
    public static final int FLAG_ACTIVITY_REORDER_TO_FRONT = 131072;
    public static final int FLAG_ACTIVITY_RESET_TASK_IF_NEEDED = 2097152;
    public static final int FLAG_ACTIVITY_RETAIN_IN_RECENTS = 8192;
    public static final int FLAG_ACTIVITY_SINGLE_TOP = 536870912;
    public static final int FLAG_ACTIVITY_TASK_ON_HOME = 16384;
    public static final int FLAG_DEBUG_LOG_RESOLUTION = 8;
    @Deprecated
    public static final int FLAG_DEBUG_TRIAGED_MISSING = 256;
    public static final int FLAG_DIRECT_BOOT_AUTO = 256;
    public static final int FLAG_EXCLUDE_STOPPED_PACKAGES = 16;
    public static final int FLAG_FROM_BACKGROUND = 4;
    public static final int FLAG_GRANT_PERSISTABLE_URI_PERMISSION = 64;
    public static final int FLAG_GRANT_PREFIX_URI_PERMISSION = 128;
    public static final int FLAG_GRANT_READ_URI_PERMISSION = 1;
    public static final int FLAG_GRANT_WRITE_URI_PERMISSION = 2;
    public static final int FLAG_IGNORE_EPHEMERAL = 512;
    public static final int FLAG_INCLUDE_STOPPED_PACKAGES = 32;
    public static final int FLAG_RECEIVER_BOOT_UPGRADE = 33554432;
    public static final int FLAG_RECEIVER_EXCLUDE_BACKGROUND = 8388608;
    public static final int FLAG_RECEIVER_FOREGROUND = 268435456;
    public static final int FLAG_RECEIVER_FROM_SHELL = 4194304;
    public static final int FLAG_RECEIVER_INCLUDE_BACKGROUND = 16777216;
    public static final int FLAG_RECEIVER_NO_ABORT = 134217728;
    public static final int FLAG_RECEIVER_OFFLOAD = Integer.MIN_VALUE;
    public static final int FLAG_RECEIVER_REGISTERED_ONLY = 1073741824;
    @UnsupportedAppUsage
    public static final int FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT = 67108864;
    public static final int FLAG_RECEIVER_REPLACE_PENDING = 536870912;
    public static final int FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS = 2097152;
    public static final int IMMUTABLE_FLAGS = 195;
    public static final String METADATA_DOCK_HOME = "android.dock_home";
    @SystemApi
    public static final String METADATA_SETUP_VERSION = "android.SETUP_VERSION";
    private static final String TAG = "Intent";
    private static final String TAG_CATEGORIES = "categories";
    private static final String TAG_EXTRA = "extra";
    public static final int URI_ALLOW_UNSAFE = 4;
    public static final int URI_ANDROID_APP_SCHEME = 2;
    public static final int URI_INTENT_SCHEME = 1;
    private String mAction;
    private ArraySet<String> mCategories;
    private ClipData mClipData;
    private ComponentName mComponent;
    private int mContentUserHint;
    private Uri mData;
    @UnsupportedAppUsage
    private Bundle mExtras;
    private int mFlags;
    private String mIdentifier;
    private String mLaunchToken;
    private int mMiuiFlags;
    private String mPackage;
    private Intent mSelector;
    private String mSenderPackageName;
    private Rect mSourceBounds;
    private String mType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AccessUriMode {
    }

    public interface CommandOptionHandler {
        boolean handleOption(String str, ShellCommand shellCommand);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CopyMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FillInFlags {
    }

    public static final class FilterComparison {
        private final int mHashCode;
        private final Intent mIntent;

        public FilterComparison(Intent intent) {
            this.mIntent = intent;
            this.mHashCode = intent.filterHashCode();
        }

        public Intent getIntent() {
            return this.mIntent;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof FilterComparison)) {
                return false;
            }
            return this.mIntent.filterEquals(((FilterComparison) obj).mIntent);
        }

        public int hashCode() {
            return this.mHashCode;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface GrantUriMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MutableFlags {
    }

    public static class ShortcutIconResource implements Parcelable {
        public static final Creator<ShortcutIconResource> CREATOR = new Creator<ShortcutIconResource>() {
            public ShortcutIconResource createFromParcel(Parcel source) {
                ShortcutIconResource icon = new ShortcutIconResource();
                icon.packageName = source.readString();
                icon.resourceName = source.readString();
                return icon;
            }

            public ShortcutIconResource[] newArray(int size) {
                return new ShortcutIconResource[size];
            }
        };
        public String packageName;
        public String resourceName;

        public static ShortcutIconResource fromContext(Context context, int resourceId) {
            ShortcutIconResource icon = new ShortcutIconResource();
            icon.packageName = context.getPackageName();
            icon.resourceName = context.getResources().getResourceName(resourceId);
            return icon;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.packageName);
            dest.writeString(this.resourceName);
        }

        public String toString() {
            return this.resourceName;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface UriFlags {
    }

    public static Intent createChooser(Intent target, CharSequence title) {
        return createChooser(target, title, null);
    }

    public static Intent createChooser(Intent target, CharSequence title, IntentSender sender) {
        Intent intent = new Intent(ACTION_CHOOSER);
        intent.putExtra(EXTRA_INTENT, (Parcelable) target);
        if (title != null) {
            intent.putExtra(EXTRA_TITLE, title);
        }
        if (sender != null) {
            intent.putExtra(EXTRA_CHOSEN_COMPONENT_INTENT_SENDER, (Parcelable) sender);
        }
        int permFlags = target.getFlags() & 195;
        if (permFlags != 0) {
            ClipData targetClipData = target.getClipData();
            if (targetClipData == null && target.getData() != null) {
                targetClipData = new ClipData(null, target.getType() != null ? new String[]{target.getType()} : new String[0], new Item(target.getData()));
            }
            if (targetClipData != null) {
                intent.setClipData(targetClipData);
                intent.addFlags(permFlags);
            }
        }
        return intent;
    }

    public static boolean isAccessUriMode(int modeFlags) {
        return (modeFlags & 3) != 0;
    }

    public Intent() {
        this.mContentUserHint = -2;
    }

    public Intent(Intent o) {
        this(o, 0);
    }

    private Intent(Intent o, int copyMode) {
        this.mContentUserHint = -2;
        this.mAction = o.mAction;
        this.mData = o.mData;
        this.mType = o.mType;
        this.mIdentifier = o.mIdentifier;
        this.mPackage = o.mPackage;
        this.mComponent = o.mComponent;
        ArraySet arraySet = o.mCategories;
        if (arraySet != null) {
            this.mCategories = new ArraySet(arraySet);
        }
        if (copyMode != 1) {
            this.mFlags = o.mFlags;
            this.mContentUserHint = o.mContentUserHint;
            this.mLaunchToken = o.mLaunchToken;
            Rect rect = o.mSourceBounds;
            if (rect != null) {
                this.mSourceBounds = new Rect(rect);
            }
            Intent intent = o.mSelector;
            if (intent != null) {
                this.mSelector = new Intent(intent);
            }
            Bundle bundle;
            if (copyMode != 2) {
                bundle = o.mExtras;
                if (bundle != null) {
                    this.mExtras = new Bundle(bundle);
                }
                ClipData clipData = o.mClipData;
                if (clipData != null) {
                    this.mClipData = new ClipData(clipData);
                }
            } else {
                bundle = o.mExtras;
                if (!(bundle == null || bundle.maybeIsEmpty())) {
                    this.mExtras = Bundle.STRIPPED;
                }
            }
        }
        this.mSenderPackageName = o.mSenderPackageName;
        this.mMiuiFlags = o.mMiuiFlags;
    }

    public Object clone() {
        return new Intent(this);
    }

    public Intent cloneFilter() {
        return new Intent(this, 1);
    }

    public Intent(String action) {
        this.mContentUserHint = -2;
        setAction(action);
    }

    public Intent(String action, Uri uri) {
        this.mContentUserHint = -2;
        setAction(action);
        this.mData = uri;
    }

    public Intent(Context packageContext, Class<?> cls) {
        this.mContentUserHint = -2;
        this.mComponent = new ComponentName(packageContext, (Class) cls);
    }

    public Intent(String action, Uri uri, Context packageContext, Class<?> cls) {
        this.mContentUserHint = -2;
        setAction(action);
        this.mData = uri;
        this.mComponent = new ComponentName(packageContext, (Class) cls);
    }

    public static Intent makeMainActivity(ComponentName mainActivity) {
        Intent intent = new Intent(ACTION_MAIN);
        intent.setComponent(mainActivity);
        intent.addCategory(CATEGORY_LAUNCHER);
        return intent;
    }

    public static Intent makeMainSelectorActivity(String selectorAction, String selectorCategory) {
        Intent intent = new Intent(ACTION_MAIN);
        intent.addCategory(CATEGORY_LAUNCHER);
        Intent selector = new Intent();
        selector.setAction(selectorAction);
        selector.addCategory(selectorCategory);
        intent.setSelector(selector);
        return intent;
    }

    public static Intent makeRestartActivityTask(ComponentName mainActivity) {
        Intent intent = makeMainActivity(mainActivity);
        intent.addFlags(268468224);
        return intent;
    }

    @Deprecated
    public static Intent getIntent(String uri) throws URISyntaxException {
        return parseUri(uri, 0);
    }

    public static Intent parseUri(String uri, int flags) throws URISyntaxException {
        String str = uri;
        String data = "android-app:";
        int i = 0;
        try {
            Intent intent;
            String data2;
            String str2;
            String value;
            int semi;
            Intent intent2;
            String key;
            boolean androidApp = str.startsWith(data);
            String str3 = "intent:";
            String str4 = "android.intent.action.VIEW";
            if ((flags & 3) != 0) {
                if (!(str.startsWith(str3) || androidApp)) {
                    intent = new Intent(str4);
                    intent.setData(Uri.parse(uri));
                    return intent;
                }
            }
            i = str.lastIndexOf("#");
            if (i == -1) {
                if (!androidApp) {
                    return new Intent(str4, Uri.parse(uri));
                }
            } else if (!str.startsWith("#Intent;", i)) {
                if (!androidApp) {
                    return getIntentOld(uri, flags);
                }
                i = -1;
            }
            intent = new Intent(str4);
            Intent baseIntent = intent;
            boolean explicitAction = false;
            boolean inSelector = false;
            String scheme = null;
            if (i >= 0) {
                data2 = str.substring(0, i);
                i += 8;
            } else {
                data2 = uri;
            }
            while (true) {
                str2 = ":";
                value = "";
                if (i >= 0) {
                    if (str.startsWith("end", i)) {
                        break;
                    }
                    boolean androidApp2;
                    int eq = str.indexOf(61, i);
                    if (eq < 0) {
                        eq = i - 1;
                    }
                    semi = str.indexOf(59, i);
                    if (eq < semi) {
                        value = Uri.decode(str.substring(eq + 1, semi));
                    }
                    int i2;
                    if (str.startsWith("action=", i)) {
                        intent.setAction(value);
                        if (inSelector) {
                            androidApp2 = androidApp;
                            i2 = eq;
                            intent2 = intent;
                        } else {
                            explicitAction = true;
                            androidApp2 = androidApp;
                            i2 = eq;
                            i = semi + 1;
                            androidApp = androidApp2;
                        }
                    } else if (str.startsWith("category=", i)) {
                        intent.addCategory(value);
                        androidApp2 = androidApp;
                        i2 = eq;
                        intent2 = intent;
                    } else if (str.startsWith("type=", i)) {
                        intent.mType = value;
                        androidApp2 = androidApp;
                        i2 = eq;
                        intent2 = intent;
                    } else if (str.startsWith("identifier=", i)) {
                        intent.mIdentifier = value;
                        androidApp2 = androidApp;
                        i2 = eq;
                        intent2 = intent;
                    } else if (str.startsWith("launchFlags=", i)) {
                        intent.mFlags = Integer.decode(value).intValue();
                        if ((flags & 4) == 0) {
                            intent.mFlags &= -196;
                            androidApp2 = androidApp;
                            i2 = eq;
                            intent2 = intent;
                        } else {
                            androidApp2 = androidApp;
                            i2 = eq;
                            intent2 = intent;
                        }
                    } else if (str.startsWith("package=", i)) {
                        intent.mPackage = value;
                        androidApp2 = androidApp;
                        i2 = eq;
                        intent2 = intent;
                    } else if (str.startsWith("component=", i)) {
                        intent.mComponent = ComponentName.unflattenFromString(value);
                        androidApp2 = androidApp;
                        i2 = eq;
                        intent2 = intent;
                    } else if (str.startsWith("scheme=", i)) {
                        if (inSelector) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(value);
                            stringBuilder.append(str2);
                            intent.mData = Uri.parse(stringBuilder.toString());
                            androidApp2 = androidApp;
                            i2 = eq;
                            intent2 = intent;
                        } else {
                            scheme = value;
                            androidApp2 = androidApp;
                            i2 = eq;
                            i = semi + 1;
                            androidApp = androidApp2;
                        }
                    } else if (str.startsWith("sourceBounds=", i)) {
                        intent.mSourceBounds = Rect.unflattenFromString(value);
                        androidApp2 = androidApp;
                        i2 = eq;
                        intent2 = intent;
                    } else if (semi == i + 3 && str.startsWith("SEL", i)) {
                        intent = new Intent();
                        inSelector = true;
                        androidApp2 = androidApp;
                        i2 = eq;
                        i = semi + 1;
                        androidApp = androidApp2;
                    } else {
                        key = Uri.decode(str.substring(i + 2, eq));
                        if (intent.mExtras == null) {
                            intent.mExtras = new Bundle();
                        }
                        Bundle b = intent.mExtras;
                        androidApp2 = androidApp;
                        if (str.startsWith("S.", i)) {
                            b.putString(key, value);
                            i2 = eq;
                            intent2 = intent;
                        } else if (str.startsWith("B.", i)) {
                            b.putBoolean(key, Boolean.parseBoolean(value));
                            i2 = eq;
                            intent2 = intent;
                        } else if (str.startsWith("b.", i)) {
                            b.putByte(key, Byte.parseByte(value));
                            i2 = eq;
                            intent2 = intent;
                        } else if (str.startsWith("c.", i)) {
                            b.putChar(key, value.charAt(0));
                            intent2 = intent;
                        } else {
                            if (str.startsWith("d.", i)) {
                                intent2 = intent;
                                b.putDouble(key, Double.parseDouble(value));
                            } else {
                                intent2 = intent;
                                if (str.startsWith("f.", i)) {
                                    b.putFloat(key, Float.parseFloat(value));
                                } else if (str.startsWith("i.", i)) {
                                    b.putInt(key, Integer.parseInt(value));
                                } else if (str.startsWith("l.", i)) {
                                    b.putLong(key, Long.parseLong(value));
                                } else if (str.startsWith("s.", i)) {
                                    b.putShort(key, Short.parseShort(value));
                                } else {
                                    throw new URISyntaxException(str, "unknown EXTRA type", i);
                                }
                            }
                        }
                    }
                    intent = intent2;
                    i = semi + 1;
                    androidApp = androidApp2;
                } else {
                    break;
                }
            }
            intent2 = intent;
            if (inSelector) {
                if (baseIntent.mPackage == null) {
                    baseIntent.setSelector(intent2);
                }
                intent = baseIntent;
            } else {
                intent = intent2;
            }
            if (data2 != null) {
                if (data2.startsWith(str3)) {
                    data = data2.substring(7);
                    if (scheme != null) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(scheme);
                        stringBuilder2.append(':');
                        stringBuilder2.append(data);
                        data2 = stringBuilder2.toString();
                    } else {
                        data2 = data;
                    }
                } else if (data2.startsWith(data)) {
                    if (data2.charAt(12) == '/' && data2.charAt(13) == '/') {
                        int end = data2.indexOf(47, 14);
                        key = ACTION_MAIN;
                        if (end < 0) {
                            intent.mPackage = data2.substring(14);
                            if (!explicitAction) {
                                intent.setAction(key);
                            }
                            data = value;
                        } else {
                            String authority = null;
                            intent.mPackage = data2.substring(14, end);
                            if (end + 1 < data2.length()) {
                                int indexOf = data2.indexOf(47, end + 1);
                                semi = indexOf;
                                if (indexOf >= 0) {
                                    scheme = data2.substring(end + 1, semi);
                                    end = semi;
                                    if (end < data2.length()) {
                                        indexOf = data2.indexOf(47, end + 1);
                                        int newEnd = indexOf;
                                        if (indexOf >= 0) {
                                            authority = data2.substring(end + 1, newEnd);
                                            end = newEnd;
                                        }
                                    }
                                } else {
                                    scheme = data2.substring(end + 1);
                                }
                            }
                            StringBuilder stringBuilder3;
                            if (scheme == null) {
                                if (!explicitAction) {
                                    intent.setAction(key);
                                }
                                data = value;
                            } else if (authority == null) {
                                stringBuilder3 = new StringBuilder();
                                stringBuilder3.append(scheme);
                                stringBuilder3.append(str2);
                                data = stringBuilder3.toString();
                            } else {
                                stringBuilder3 = new StringBuilder();
                                stringBuilder3.append(scheme);
                                stringBuilder3.append("://");
                                stringBuilder3.append(authority);
                                stringBuilder3.append(data2.substring(end));
                                data = stringBuilder3.toString();
                            }
                        }
                        data2 = data;
                    } else {
                        data2 = value;
                    }
                }
                if (data2.length() > 0) {
                    intent.mData = Uri.parse(data2);
                }
            }
            return intent;
        } catch (IllegalArgumentException e) {
            throw new URISyntaxException(str, e.getMessage());
        } catch (IllegalArgumentException e2) {
            throw new URISyntaxException(str, e2.getMessage());
        } catch (IndexOutOfBoundsException e3) {
            throw new URISyntaxException(str, "illegal Intent URI format", i);
        }
    }

    public static Intent getIntentOld(String uri) throws URISyntaxException {
        return getIntentOld(uri, 0);
    }

    private static Intent getIntentOld(String uri, int flags) throws URISyntaxException {
        char c;
        String str = uri;
        int i = str.lastIndexOf(35);
        String str2 = "android.intent.action.VIEW";
        if (i < 0) {
            return new Intent(str2, Uri.parse(uri));
        }
        int j;
        int sep;
        String action = null;
        int intentFragmentStart = i;
        boolean isIntentFragment = false;
        i++;
        if (str.regionMatches(i, "action(", 0, 7)) {
            isIntentFragment = true;
            i += 7;
            int j2 = str.indexOf(41, i);
            action = str.substring(i, j2);
            i = j2 + 1;
        }
        Intent intent = new Intent(action);
        int i2 = 33;
        if (str.regionMatches(i, "categories(", 0, 11)) {
            isIntentFragment = true;
            i += 11;
            j = str.indexOf(41, i);
            while (i < j) {
                sep = str.indexOf(33, i);
                if (sep < 0 || sep > j) {
                    sep = j;
                }
                if (i < sep) {
                    intent.addCategory(str.substring(i, sep));
                }
                i = sep + 1;
            }
            i = j + 1;
        }
        if (str.regionMatches(i, "type(", 0, 5)) {
            isIntentFragment = true;
            i += 5;
            j = str.indexOf(41, i);
            intent.mType = str.substring(i, j);
            i = j + 1;
        }
        if (str.regionMatches(i, "launchFlags(", 0, 12)) {
            isIntentFragment = true;
            i += 12;
            j = str.indexOf(41, i);
            intent.mFlags = Integer.decode(str.substring(i, j)).intValue();
            if ((flags & 4) == 0) {
                intent.mFlags &= -196;
            }
            i = j + 1;
        }
        if (str.regionMatches(i, "component(", 0, 10)) {
            isIntentFragment = true;
            i += 10;
            j = str.indexOf(41, i);
            sep = str.indexOf(33, i);
            if (sep >= 0 && sep < j) {
                intent.mComponent = new ComponentName(str.substring(i, sep), str.substring(sep + 1, j));
            }
            i = j + 1;
        }
        if (str.regionMatches(i, "extras(", 0, 7)) {
            isIntentFragment = true;
            char i3 = i + 7;
            char closeParen = str.indexOf(41, i3);
            char i4 = 65535;
            if (closeParen != 65535) {
                while (i3 < closeParen) {
                    sep = str.indexOf(61, i3);
                    if (sep <= i3 + 1 || i3 >= closeParen) {
                        throw new URISyntaxException(str, "EXTRA missing '='", i3);
                    }
                    char type = str.charAt(i3);
                    String key = str.substring(i3 + 1, sep);
                    i3 = sep + 1;
                    char j3 = str.indexOf(i2, i3);
                    if (j3 == i4 || j3 >= closeParen) {
                        j3 = closeParen;
                    }
                    String str3 = "EXTRA missing '!'";
                    if (i3 < j3) {
                        String value = str.substring(i3, j3);
                        char i5 = j3;
                        if (intent.mExtras == null) {
                            intent.mExtras = new Bundle();
                        }
                        if (type == 'B') {
                            i4 = i5;
                            intent.mExtras.putBoolean(key, Boolean.parseBoolean(value));
                        } else if (type == 'S') {
                            i4 = i5;
                            intent.mExtras.putString(key, Uri.decode(value));
                        } else if (type == 'f') {
                            i4 = i5;
                            intent.mExtras.putFloat(key, Float.parseFloat(value));
                        } else if (type == 'i') {
                            i4 = i5;
                            intent.mExtras.putInt(key, Integer.parseInt(value));
                        } else if (type == 'l') {
                            i4 = i5;
                            intent.mExtras.putLong(key, Long.parseLong(value));
                        } else if (type != 's') {
                            switch (type) {
                                case 'b':
                                    i4 = i5;
                                    intent.mExtras.putByte(key, Byte.parseByte(value));
                                    break;
                                case 'c':
                                    i4 = i5;
                                    intent.mExtras.putChar(key, Uri.decode(value).charAt(0));
                                    break;
                                case 'd':
                                    j = i5;
                                    try {
                                        try {
                                            intent.mExtras.putDouble(key, Double.parseDouble(value));
                                            break;
                                        } catch (NumberFormatException e) {
                                            throw new URISyntaxException(str, "EXTRA value can't be parsed", j);
                                        }
                                    } catch (NumberFormatException e2) {
                                        c = j3;
                                        throw new URISyntaxException(str, "EXTRA value can't be parsed", j);
                                    }
                                default:
                                    try {
                                        char i6 = i5;
                                        try {
                                            throw new URISyntaxException(str, "EXTRA has unknown type", i6);
                                        } catch (NumberFormatException e3) {
                                            j = i6;
                                            c = j3;
                                            throw new URISyntaxException(str, "EXTRA value can't be parsed", j);
                                        }
                                    } catch (NumberFormatException e4) {
                                        c = j3;
                                        j = i5;
                                        throw new URISyntaxException(str, "EXTRA value can't be parsed", j);
                                    }
                            }
                        } else {
                            i4 = i5;
                            intent.mExtras.putShort(key, Short.parseShort(value));
                        }
                        i3 = str.charAt(i4);
                        if (i3 == ')') {
                            i3 = i4;
                        } else {
                            i2 = 33;
                            if (i3 == '!') {
                                i3 = i4 + 1;
                                i4 = 65535;
                            } else {
                                throw new URISyntaxException(str, str3, i4);
                            }
                        }
                    }
                    throw new URISyntaxException(str, str3, i3);
                }
            }
            throw new URISyntaxException(str, "EXTRA missing trailing ')'", i3);
        }
        if (isIntentFragment) {
            intent.mData = Uri.parse(str.substring(0, intentFragmentStart));
        } else {
            intent.mData = Uri.parse(uri);
        }
        if (intent.mAction != null) {
            return intent;
        }
        intent.mAction = str2;
        return intent;
    }

    @UnsupportedAppUsage
    public static Intent parseCommandArgs(ShellCommand cmd, CommandOptionHandler optionHandler) throws URISyntaxException {
        String opt;
        CommandOptionHandler commandOptionHandler = optionHandler;
        Intent intent = new Intent();
        Intent baseIntent = intent;
        Uri data = null;
        String type = null;
        boolean hasIntentInfo = false;
        Intent intent2 = intent;
        while (true) {
            String nextOption = cmd.getNextOption();
            opt = nextOption;
            ShellCommand shellCommand;
            String value;
            boolean arg;
            if (nextOption != null) {
                int i = -1;
                switch (opt.hashCode()) {
                    case -2147394086:
                        if (opt.equals("--grant-prefix-uri-permission")) {
                            i = 28;
                            break;
                        }
                        break;
                    case -2118172637:
                        if (opt.equals("--activity-task-on-home")) {
                            i = 46;
                            break;
                        }
                        break;
                    case -1630559130:
                        if (opt.equals("--activity-no-history")) {
                            i = 39;
                            break;
                        }
                        break;
                    case -1252939549:
                        if (opt.equals("--activity-clear-task")) {
                            i = 45;
                            break;
                        }
                        break;
                    case -1069446353:
                        if (opt.equals("--debug-log-resolution")) {
                            i = 31;
                            break;
                        }
                        break;
                    case -848214457:
                        if (opt.equals("--activity-reorder-to-front")) {
                            i = 42;
                            break;
                        }
                        break;
                    case -833172539:
                        if (opt.equals("--activity-brought-to-front")) {
                            i = 32;
                            break;
                        }
                        break;
                    case -792169302:
                        if (opt.equals("--activity-previous-is-top")) {
                            i = 41;
                            break;
                        }
                        break;
                    case -780160399:
                        if (opt.equals("--receiver-include-background")) {
                            i = 52;
                            break;
                        }
                        break;
                    case 1492:
                        if (opt.equals("-a")) {
                            i = 0;
                            break;
                        }
                        break;
                    case 1494:
                        if (opt.equals("-c")) {
                            i = 4;
                            break;
                        }
                        break;
                    case 1495:
                        if (opt.equals("-d")) {
                            i = 1;
                            break;
                        }
                        break;
                    case 1496:
                        if (opt.equals("-e")) {
                            i = 5;
                            break;
                        }
                        break;
                    case 1497:
                        if (opt.equals("-f")) {
                            i = 24;
                            break;
                        }
                        break;
                    case 1500:
                        if (opt.equals("-i")) {
                            i = 3;
                            break;
                        }
                        break;
                    case 1505:
                        if (opt.equals("-n")) {
                            i = 22;
                            break;
                        }
                        break;
                    case 1507:
                        if (opt.equals("-p")) {
                            i = 23;
                            break;
                        }
                        break;
                    case 1511:
                        if (opt.equals("-t")) {
                            i = 2;
                            break;
                        }
                        break;
                    case 1387073:
                        if (opt.equals("--ef")) {
                            i = 16;
                            break;
                        }
                        break;
                    case 1387076:
                        if (opt.equals("--ei")) {
                            i = 8;
                            break;
                        }
                        break;
                    case 1387079:
                        if (opt.equals("--el")) {
                            i = 13;
                            break;
                        }
                        break;
                    case 1387086:
                        if (opt.equals("--es")) {
                            i = 6;
                            break;
                        }
                        break;
                    case 1387088:
                        if (opt.equals("--eu")) {
                            i = 9;
                            break;
                        }
                        break;
                    case 1387093:
                        if (opt.equals("--ez")) {
                            i = 21;
                            break;
                        }
                        break;
                    case 42999280:
                        if (opt.equals("--ecn")) {
                            i = 10;
                            break;
                        }
                        break;
                    case 42999360:
                        if (opt.equals("--efa")) {
                            i = 17;
                            break;
                        }
                        break;
                    case 42999453:
                        if (opt.equals("--eia")) {
                            i = 11;
                            break;
                        }
                        break;
                    case 42999546:
                        if (opt.equals("--ela")) {
                            i = 14;
                            break;
                        }
                        break;
                    case 42999763:
                        if (opt.equals("--esa")) {
                            i = 19;
                            break;
                        }
                        break;
                    case 42999776:
                        if (opt.equals("--esn")) {
                            i = 7;
                            break;
                        }
                        break;
                    case 69120454:
                        if (opt.equals("--activity-exclude-from-recents")) {
                            i = 35;
                            break;
                        }
                        break;
                    case 88747734:
                        if (opt.equals("--activity-no-animation")) {
                            i = 38;
                            break;
                        }
                        break;
                    case 190913209:
                        if (opt.equals("--activity-reset-task-if-needed")) {
                            i = 43;
                            break;
                        }
                        break;
                    case 236677687:
                        if (opt.equals("--activity-clear-top")) {
                            i = 33;
                            break;
                        }
                        break;
                    case 429439306:
                        if (opt.equals("--activity-no-user-action")) {
                            i = 40;
                            break;
                        }
                        break;
                    case 436286937:
                        if (opt.equals("--receiver-registered-only")) {
                            i = 48;
                            break;
                        }
                        break;
                    case 438531630:
                        if (opt.equals("--activity-single-top")) {
                            i = 44;
                            break;
                        }
                        break;
                    case 527014976:
                        if (opt.equals("--grant-persistable-uri-permission")) {
                            i = 27;
                            break;
                        }
                        break;
                    case 580418080:
                        if (opt.equals("--exclude-stopped-packages")) {
                            i = 29;
                            break;
                        }
                        break;
                    case 749648146:
                        if (opt.equals("--include-stopped-packages")) {
                            i = 30;
                            break;
                        }
                        break;
                    case 775126336:
                        if (opt.equals("--receiver-replace-pending")) {
                            i = 49;
                            break;
                        }
                        break;
                    case 1110195121:
                        if (opt.equals("--activity-match-external")) {
                            i = 47;
                            break;
                        }
                        break;
                    case 1207327103:
                        if (opt.equals("--selector")) {
                            i = 53;
                            break;
                        }
                        break;
                    case 1332980268:
                        if (opt.equals("--efal")) {
                            i = 18;
                            break;
                        }
                        break;
                    case 1332983151:
                        if (opt.equals("--eial")) {
                            i = 12;
                            break;
                        }
                        break;
                    case 1332986034:
                        if (opt.equals("--elal")) {
                            i = 15;
                            break;
                        }
                        break;
                    case 1332992761:
                        if (opt.equals("--esal")) {
                            i = 20;
                            break;
                        }
                        break;
                    case 1353919836:
                        if (opt.equals("--activity-clear-when-task-reset")) {
                            i = 34;
                            break;
                        }
                        break;
                    case 1398403374:
                        if (opt.equals("--activity-launched-from-history")) {
                            i = 36;
                            break;
                        }
                        break;
                    case 1453225122:
                        if (opt.equals("--receiver-no-abort")) {
                            i = 51;
                            break;
                        }
                        break;
                    case 1652786753:
                        if (opt.equals("--receiver-foreground")) {
                            i = 50;
                            break;
                        }
                        break;
                    case 1742380566:
                        if (opt.equals("--grant-read-uri-permission")) {
                            i = 25;
                            break;
                        }
                        break;
                    case 1765369476:
                        if (opt.equals("--activity-multiple-task")) {
                            i = 37;
                            break;
                        }
                        break;
                    case 1816558127:
                        if (opt.equals("--grant-write-uri-permission")) {
                            i = 26;
                            break;
                        }
                        break;
                }
                String str = "(?<!\\\\),";
                String str2 = ",";
                String value2;
                StringBuilder stringBuilder;
                String[] strings;
                int i2;
                Serializable list;
                switch (i) {
                    case 0:
                        intent2.setAction(cmd.getNextArgRequired());
                        if (intent2 != baseIntent) {
                            shellCommand = cmd;
                            break;
                        }
                        shellCommand = cmd;
                        hasIntentInfo = true;
                        break;
                    case 1:
                        Uri data2 = Uri.parse(cmd.getNextArgRequired());
                        if (intent2 != baseIntent) {
                            shellCommand = cmd;
                            data = data2;
                            break;
                        }
                        shellCommand = cmd;
                        hasIntentInfo = true;
                        data = data2;
                        break;
                    case 2:
                        nextOption = cmd.getNextArgRequired();
                        if (intent2 != baseIntent) {
                            shellCommand = cmd;
                            type = nextOption;
                            break;
                        }
                        shellCommand = cmd;
                        hasIntentInfo = true;
                        type = nextOption;
                        break;
                    case 3:
                        intent2.setIdentifier(cmd.getNextArgRequired());
                        if (intent2 != baseIntent) {
                            shellCommand = cmd;
                            break;
                        }
                        shellCommand = cmd;
                        hasIntentInfo = true;
                        break;
                    case 4:
                        intent2.addCategory(cmd.getNextArgRequired());
                        if (intent2 != baseIntent) {
                            shellCommand = cmd;
                            break;
                        }
                        shellCommand = cmd;
                        hasIntentInfo = true;
                        break;
                    case 5:
                    case 6:
                        intent2.putExtra(cmd.getNextArgRequired(), cmd.getNextArgRequired());
                        shellCommand = cmd;
                        break;
                    case 7:
                        intent2.putExtra(cmd.getNextArgRequired(), (String) null);
                        shellCommand = cmd;
                        break;
                    case 8:
                        intent2.putExtra(cmd.getNextArgRequired(), Integer.decode(cmd.getNextArgRequired()));
                        shellCommand = cmd;
                        break;
                    case 9:
                        intent2.putExtra(cmd.getNextArgRequired(), Uri.parse(cmd.getNextArgRequired()));
                        shellCommand = cmd;
                        break;
                    case 10:
                        nextOption = cmd.getNextArgRequired();
                        value2 = cmd.getNextArgRequired();
                        Parcelable cn = ComponentName.unflattenFromString(value2);
                        if (cn != null) {
                            intent2.putExtra(nextOption, cn);
                            shellCommand = cmd;
                            break;
                        }
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Bad component name: ");
                        stringBuilder.append(value2);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    case 11:
                        nextOption = cmd.getNextArgRequired();
                        strings = cmd.getNextArgRequired().split(str2);
                        int[] list2 = new int[strings.length];
                        for (i2 = 0; i2 < strings.length; i2++) {
                            list2[i2] = Integer.decode(strings[i2]).intValue();
                        }
                        intent2.putExtra(nextOption, list2);
                        shellCommand = cmd;
                        break;
                    case 12:
                        nextOption = cmd.getNextArgRequired();
                        strings = cmd.getNextArgRequired().split(str2);
                        list = new ArrayList(strings.length);
                        for (String decode : strings) {
                            list.add(Integer.decode(decode));
                        }
                        intent2.putExtra(nextOption, list);
                        shellCommand = cmd;
                        break;
                    case 13:
                        intent2.putExtra(cmd.getNextArgRequired(), Long.valueOf(cmd.getNextArgRequired()));
                        shellCommand = cmd;
                        break;
                    case 14:
                        nextOption = cmd.getNextArgRequired();
                        strings = cmd.getNextArgRequired().split(str2);
                        long[] list3 = new long[strings.length];
                        for (i2 = 0; i2 < strings.length; i2++) {
                            list3[i2] = Long.valueOf(strings[i2]).longValue();
                        }
                        intent2.putExtra(nextOption, list3);
                        shellCommand = cmd;
                        hasIntentInfo = true;
                        break;
                    case 15:
                        nextOption = cmd.getNextArgRequired();
                        strings = cmd.getNextArgRequired().split(str2);
                        list = new ArrayList(strings.length);
                        for (String decode2 : strings) {
                            list.add(Long.valueOf(decode2));
                        }
                        intent2.putExtra(nextOption, list);
                        shellCommand = cmd;
                        hasIntentInfo = true;
                        break;
                    case 16:
                        intent2.putExtra(cmd.getNextArgRequired(), Float.valueOf(cmd.getNextArgRequired()));
                        shellCommand = cmd;
                        hasIntentInfo = true;
                        break;
                    case 17:
                        nextOption = cmd.getNextArgRequired();
                        strings = cmd.getNextArgRequired().split(str2);
                        float[] list4 = new float[strings.length];
                        for (i2 = 0; i2 < strings.length; i2++) {
                            list4[i2] = Float.valueOf(strings[i2]).floatValue();
                        }
                        intent2.putExtra(nextOption, list4);
                        shellCommand = cmd;
                        hasIntentInfo = true;
                        break;
                    case 18:
                        nextOption = cmd.getNextArgRequired();
                        strings = cmd.getNextArgRequired().split(str2);
                        list = new ArrayList(strings.length);
                        for (String decode22 : strings) {
                            list.add(Float.valueOf(decode22));
                        }
                        intent2.putExtra(nextOption, list);
                        shellCommand = cmd;
                        hasIntentInfo = true;
                        break;
                    case 19:
                        intent2.putExtra(cmd.getNextArgRequired(), cmd.getNextArgRequired().split(str));
                        shellCommand = cmd;
                        hasIntentInfo = true;
                        break;
                    case 20:
                        nextOption = cmd.getNextArgRequired();
                        strings = cmd.getNextArgRequired().split(str);
                        list = new ArrayList(strings.length);
                        for (Object add : strings) {
                            list.add(add);
                        }
                        intent2.putExtra(nextOption, list);
                        shellCommand = cmd;
                        hasIntentInfo = true;
                        break;
                    case 21:
                        value2 = cmd.getNextArgRequired();
                        value = cmd.getNextArgRequired().toLowerCase();
                        if ("true".equals(value) || IncidentManager.URI_PARAM_TIMESTAMP.equals(value)) {
                            arg = true;
                        } else if ("false".equals(value) || FullBackup.FILES_TREE_TOKEN.equals(value)) {
                            arg = false;
                        } else {
                            try {
                                arg = Integer.decode(value).intValue() != 0;
                            } catch (NumberFormatException e) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Invalid boolean value: ");
                                stringBuilder.append(value);
                                throw new IllegalArgumentException(stringBuilder.toString());
                            }
                        }
                        intent2.putExtra(value2, arg);
                        shellCommand = cmd;
                        break;
                    case 22:
                        nextOption = cmd.getNextArgRequired();
                        ComponentName cn2 = ComponentName.unflattenFromString(nextOption);
                        if (cn2 != null) {
                            intent2.setComponent(cn2);
                            if (intent2 == baseIntent) {
                                hasIntentInfo = true;
                            }
                            shellCommand = cmd;
                            break;
                        }
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Bad component name: ");
                        stringBuilder2.append(nextOption);
                        throw new IllegalArgumentException(stringBuilder2.toString());
                    case 23:
                        intent2.setPackage(cmd.getNextArgRequired());
                        if (intent2 == baseIntent) {
                            hasIntentInfo = true;
                        }
                        shellCommand = cmd;
                        break;
                    case 24:
                        intent2.setFlags(Integer.decode(cmd.getNextArgRequired()).intValue());
                        shellCommand = cmd;
                        break;
                    case 25:
                        intent2.addFlags(1);
                        shellCommand = cmd;
                        break;
                    case 26:
                        intent2.addFlags(2);
                        shellCommand = cmd;
                        break;
                    case 27:
                        intent2.addFlags(64);
                        shellCommand = cmd;
                        break;
                    case 28:
                        intent2.addFlags(128);
                        shellCommand = cmd;
                        break;
                    case 29:
                        intent2.addFlags(16);
                        shellCommand = cmd;
                        break;
                    case 30:
                        intent2.addFlags(32);
                        shellCommand = cmd;
                        break;
                    case 31:
                        intent2.addFlags(8);
                        shellCommand = cmd;
                        break;
                    case 32:
                        intent2.addFlags(4194304);
                        shellCommand = cmd;
                        break;
                    case 33:
                        intent2.addFlags(67108864);
                        shellCommand = cmd;
                        break;
                    case 34:
                        intent2.addFlags(524288);
                        shellCommand = cmd;
                        break;
                    case 35:
                        intent2.addFlags(8388608);
                        shellCommand = cmd;
                        break;
                    case 36:
                        intent2.addFlags(1048576);
                        shellCommand = cmd;
                        break;
                    case 37:
                        intent2.addFlags(134217728);
                        shellCommand = cmd;
                        break;
                    case 38:
                        intent2.addFlags(65536);
                        shellCommand = cmd;
                        break;
                    case 39:
                        intent2.addFlags(1073741824);
                        shellCommand = cmd;
                        break;
                    case 40:
                        intent2.addFlags(262144);
                        shellCommand = cmd;
                        break;
                    case 41:
                        intent2.addFlags(16777216);
                        shellCommand = cmd;
                        break;
                    case 42:
                        intent2.addFlags(131072);
                        shellCommand = cmd;
                        break;
                    case 43:
                        intent2.addFlags(2097152);
                        shellCommand = cmd;
                        break;
                    case 44:
                        intent2.addFlags(536870912);
                        shellCommand = cmd;
                        break;
                    case 45:
                        intent2.addFlags(32768);
                        shellCommand = cmd;
                        break;
                    case 46:
                        intent2.addFlags(16384);
                        shellCommand = cmd;
                        break;
                    case 47:
                        intent2.addFlags(2048);
                        shellCommand = cmd;
                        break;
                    case 48:
                        intent2.addFlags(1073741824);
                        shellCommand = cmd;
                        break;
                    case 49:
                        intent2.addFlags(536870912);
                        shellCommand = cmd;
                        break;
                    case 50:
                        intent2.addFlags(268435456);
                        shellCommand = cmd;
                        break;
                    case 51:
                        intent2.addFlags(134217728);
                        shellCommand = cmd;
                        break;
                    case 52:
                        intent2.addFlags(16777216);
                        shellCommand = cmd;
                        break;
                    case 53:
                        intent2.setDataAndType(data, type);
                        shellCommand = cmd;
                        intent2 = new Intent();
                        break;
                    default:
                        if (commandOptionHandler != null) {
                            if (!commandOptionHandler.handleOption(opt, cmd)) {
                                break;
                            }
                            break;
                        }
                        shellCommand = cmd;
                        break;
                }
            }
            shellCommand = cmd;
            intent2.setDataAndType(data, type);
            arg = intent2 != baseIntent;
            if (arg) {
                baseIntent.setSelector(intent2);
                intent2 = baseIntent;
            }
            value = cmd.getNextArg();
            baseIntent = null;
            String str3 = CATEGORY_LAUNCHER;
            String str4 = ACTION_MAIN;
            if (value == null) {
                if (arg) {
                    baseIntent = new Intent(str4);
                    baseIntent.addCategory(str3);
                }
            } else if (value.indexOf(58) >= 0) {
                baseIntent = parseUri(value, 7);
            } else if (value.indexOf(47) >= 0) {
                baseIntent = new Intent(str4);
                baseIntent.addCategory(str3);
                baseIntent.setComponent(ComponentName.unflattenFromString(value));
            } else {
                baseIntent = new Intent(str4);
                baseIntent.addCategory(str3);
                baseIntent.setPackage(value);
            }
            if (baseIntent != null) {
                Bundle extras = intent2.getExtras();
                Bundle bundle = (Bundle) null;
                intent2.replaceExtras(bundle);
                Bundle uriExtras = baseIntent.getExtras();
                baseIntent.replaceExtras(bundle);
                if (!(intent2.getAction() == null || baseIntent.getCategories() == null)) {
                    Iterator it = new HashSet(baseIntent.getCategories()).iterator();
                    while (it.hasNext()) {
                        baseIntent.removeCategory((String) it.next());
                    }
                }
                intent2.fillIn(baseIntent, 72);
                if (extras == null) {
                    extras = uriExtras;
                } else if (uriExtras != null) {
                    uriExtras.putAll(extras);
                    extras = uriExtras;
                }
                intent2.replaceExtras(extras);
                hasIntentInfo = true;
            }
            if (hasIntentInfo) {
                return intent2;
            }
            throw new IllegalArgumentException("No intent supplied");
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("Unknown option: ");
        stringBuilder3.append(opt);
        throw new IllegalArgumentException(stringBuilder3.toString());
    }

    @UnsupportedAppUsage
    public static void printIntentArgsHelp(PrintWriter pw, String prefix) {
        for (String line : new String[]{"<INTENT> specifications include these flags and arguments:", "    [-a <ACTION>] [-d <DATA_URI>] [-t <MIME_TYPE>] [-i <IDENTIFIER>]", "    [-c <CATEGORY> [-c <CATEGORY>] ...]", "    [-n <COMPONENT_NAME>]", "    [-e|--es <EXTRA_KEY> <EXTRA_STRING_VALUE> ...]", "    [--esn <EXTRA_KEY> ...]", "    [--ez <EXTRA_KEY> <EXTRA_BOOLEAN_VALUE> ...]", "    [--ei <EXTRA_KEY> <EXTRA_INT_VALUE> ...]", "    [--el <EXTRA_KEY> <EXTRA_LONG_VALUE> ...]", "    [--ef <EXTRA_KEY> <EXTRA_FLOAT_VALUE> ...]", "    [--eu <EXTRA_KEY> <EXTRA_URI_VALUE> ...]", "    [--ecn <EXTRA_KEY> <EXTRA_COMPONENT_NAME_VALUE>]", "    [--eia <EXTRA_KEY> <EXTRA_INT_VALUE>[,<EXTRA_INT_VALUE...]]", "        (mutiple extras passed as Integer[])", "    [--eial <EXTRA_KEY> <EXTRA_INT_VALUE>[,<EXTRA_INT_VALUE...]]", "        (mutiple extras passed as List<Integer>)", "    [--ela <EXTRA_KEY> <EXTRA_LONG_VALUE>[,<EXTRA_LONG_VALUE...]]", "        (mutiple extras passed as Long[])", "    [--elal <EXTRA_KEY> <EXTRA_LONG_VALUE>[,<EXTRA_LONG_VALUE...]]", "        (mutiple extras passed as List<Long>)", "    [--efa <EXTRA_KEY> <EXTRA_FLOAT_VALUE>[,<EXTRA_FLOAT_VALUE...]]", "        (mutiple extras passed as Float[])", "    [--efal <EXTRA_KEY> <EXTRA_FLOAT_VALUE>[,<EXTRA_FLOAT_VALUE...]]", "        (mutiple extras passed as List<Float>)", "    [--esa <EXTRA_KEY> <EXTRA_STRING_VALUE>[,<EXTRA_STRING_VALUE...]]", "        (mutiple extras passed as String[]; to embed a comma into a string,", "         escape it using \"\\,\")", "    [--esal <EXTRA_KEY> <EXTRA_STRING_VALUE>[,<EXTRA_STRING_VALUE...]]", "        (mutiple extras passed as List<String>; to embed a comma into a string,", "         escape it using \"\\,\")", "    [-f <FLAG>]", "    [--grant-read-uri-permission] [--grant-write-uri-permission]", "    [--grant-persistable-uri-permission] [--grant-prefix-uri-permission]", "    [--debug-log-resolution] [--exclude-stopped-packages]", "    [--include-stopped-packages]", "    [--activity-brought-to-front] [--activity-clear-top]", "    [--activity-clear-when-task-reset] [--activity-exclude-from-recents]", "    [--activity-launched-from-history] [--activity-multiple-task]", "    [--activity-no-animation] [--activity-no-history]", "    [--activity-no-user-action] [--activity-previous-is-top]", "    [--activity-reorder-to-front] [--activity-reset-task-if-needed]", "    [--activity-single-top] [--activity-clear-task]", "    [--activity-task-on-home] [--activity-match-external]", "    [--receiver-registered-only] [--receiver-replace-pending]", "    [--receiver-foreground] [--receiver-no-abort]", "    [--receiver-include-background]", "    [--selector]", "    [<URI> | <PACKAGE> | <COMPONENT>]"}) {
            pw.print(prefix);
            pw.println(line);
        }
        PrintWriter printWriter = pw;
    }

    public String getAction() {
        return this.mAction;
    }

    public Uri getData() {
        return this.mData;
    }

    public String getDataString() {
        Uri uri = this.mData;
        return uri != null ? uri.toString() : null;
    }

    public String getScheme() {
        Uri uri = this.mData;
        return uri != null ? uri.getScheme() : null;
    }

    public String getType() {
        return this.mType;
    }

    public String resolveType(Context context) {
        return resolveType(context.getContentResolver());
    }

    public String resolveType(ContentResolver resolver) {
        String str = this.mType;
        if (str != null) {
            return str;
        }
        Uri uri = this.mData;
        if (uri != null) {
            if ("content".equals(uri.getScheme())) {
                return resolver.getType(this.mData);
            }
        }
        return null;
    }

    public String resolveTypeIfNeeded(ContentResolver resolver) {
        if (this.mComponent != null) {
            return this.mType;
        }
        return resolveType(resolver);
    }

    public String getIdentifier() {
        return this.mIdentifier;
    }

    public boolean hasCategory(String category) {
        ArraySet arraySet = this.mCategories;
        return arraySet != null && arraySet.contains(category);
    }

    public Set<String> getCategories() {
        return this.mCategories;
    }

    public Intent getSelector() {
        return this.mSelector;
    }

    public ClipData getClipData() {
        return this.mClipData;
    }

    public int getContentUserHint() {
        return this.mContentUserHint;
    }

    public String getLaunchToken() {
        return this.mLaunchToken;
    }

    public void setLaunchToken(String launchToken) {
        this.mLaunchToken = launchToken;
    }

    public void setExtrasClassLoader(ClassLoader loader) {
        Bundle bundle = this.mExtras;
        if (bundle != null) {
            bundle.setClassLoader(loader);
        }
    }

    public boolean hasExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle != null && bundle.containsKey(name);
    }

    public boolean hasFileDescriptors() {
        Bundle bundle = this.mExtras;
        return bundle != null && bundle.hasFileDescriptors();
    }

    @UnsupportedAppUsage
    public void setAllowFds(boolean allowFds) {
        Bundle bundle = this.mExtras;
        if (bundle != null) {
            bundle.setAllowFds(allowFds);
        }
    }

    public void setDefusable(boolean defusable) {
        Bundle bundle = this.mExtras;
        if (bundle != null) {
            bundle.setDefusable(defusable);
        }
    }

    @Deprecated
    @UnsupportedAppUsage
    public Object getExtra(String name) {
        return getExtra(name, null);
    }

    public boolean getBooleanExtra(String name, boolean defaultValue) {
        Bundle bundle = this.mExtras;
        if (bundle == null) {
            return defaultValue;
        }
        return bundle.getBoolean(name, defaultValue);
    }

    public byte getByteExtra(String name, byte defaultValue) {
        Bundle bundle = this.mExtras;
        if (bundle == null) {
            return defaultValue;
        }
        return bundle.getByte(name, defaultValue).byteValue();
    }

    public short getShortExtra(String name, short defaultValue) {
        Bundle bundle = this.mExtras;
        if (bundle == null) {
            return defaultValue;
        }
        return bundle.getShort(name, defaultValue);
    }

    public char getCharExtra(String name, char defaultValue) {
        Bundle bundle = this.mExtras;
        if (bundle == null) {
            return defaultValue;
        }
        return bundle.getChar(name, defaultValue);
    }

    public int getIntExtra(String name, int defaultValue) {
        Bundle bundle = this.mExtras;
        if (bundle == null) {
            return defaultValue;
        }
        return bundle.getInt(name, defaultValue);
    }

    public long getLongExtra(String name, long defaultValue) {
        Bundle bundle = this.mExtras;
        if (bundle == null) {
            return defaultValue;
        }
        return bundle.getLong(name, defaultValue);
    }

    public float getFloatExtra(String name, float defaultValue) {
        Bundle bundle = this.mExtras;
        if (bundle == null) {
            return defaultValue;
        }
        return bundle.getFloat(name, defaultValue);
    }

    public double getDoubleExtra(String name, double defaultValue) {
        Bundle bundle = this.mExtras;
        if (bundle == null) {
            return defaultValue;
        }
        return bundle.getDouble(name, defaultValue);
    }

    public String getStringExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getString(name);
    }

    public CharSequence getCharSequenceExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getCharSequence(name);
    }

    public <T extends Parcelable> T getParcelableExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getParcelable(name);
    }

    public Parcelable[] getParcelableArrayExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getParcelableArray(name);
    }

    public <T extends Parcelable> ArrayList<T> getParcelableArrayListExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getParcelableArrayList(name);
    }

    public Serializable getSerializableExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getSerializable(name);
    }

    public ArrayList<Integer> getIntegerArrayListExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getIntegerArrayList(name);
    }

    public ArrayList<String> getStringArrayListExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getStringArrayList(name);
    }

    public ArrayList<CharSequence> getCharSequenceArrayListExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getCharSequenceArrayList(name);
    }

    public boolean[] getBooleanArrayExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getBooleanArray(name);
    }

    public byte[] getByteArrayExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getByteArray(name);
    }

    public short[] getShortArrayExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getShortArray(name);
    }

    public char[] getCharArrayExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getCharArray(name);
    }

    public int[] getIntArrayExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getIntArray(name);
    }

    public long[] getLongArrayExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getLongArray(name);
    }

    public float[] getFloatArrayExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getFloatArray(name);
    }

    public double[] getDoubleArrayExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getDoubleArray(name);
    }

    public String[] getStringArrayExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getStringArray(name);
    }

    public CharSequence[] getCharSequenceArrayExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getCharSequenceArray(name);
    }

    public Bundle getBundleExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getBundle(name);
    }

    @Deprecated
    @UnsupportedAppUsage
    public IBinder getIBinderExtra(String name) {
        Bundle bundle = this.mExtras;
        return bundle == null ? null : bundle.getIBinder(name);
    }

    @Deprecated
    @UnsupportedAppUsage
    public Object getExtra(String name, Object defaultValue) {
        Object result = defaultValue;
        Object result2 = this.mExtras;
        if (result2 == null) {
            return result;
        }
        result2 = result2.get(name);
        if (result2 != null) {
            return result2;
        }
        return result;
    }

    public Bundle getExtras() {
        Bundle bundle = this.mExtras;
        if (bundle != null) {
            return new Bundle(bundle);
        }
        return null;
    }

    public void removeUnsafeExtras() {
        Bundle bundle = this.mExtras;
        if (bundle != null) {
            this.mExtras = bundle.filterValues();
        }
    }

    public boolean canStripForHistory() {
        Bundle bundle = this.mExtras;
        return (bundle != null && bundle.isParcelled()) || this.mClipData != null;
    }

    public Intent maybeStripForHistory() {
        if (canStripForHistory()) {
            return new Intent(this, 2);
        }
        return this;
    }

    public int getFlags() {
        return this.mFlags;
    }

    @UnsupportedAppUsage
    public boolean isExcludingStopped() {
        return (this.mFlags & 48) == 16;
    }

    public String getPackage() {
        return this.mPackage;
    }

    public ComponentName getComponent() {
        return this.mComponent;
    }

    public Rect getSourceBounds() {
        return this.mSourceBounds;
    }

    public ComponentName resolveActivity(PackageManager pm) {
        ComponentName componentName = this.mComponent;
        if (componentName != null) {
            return componentName;
        }
        ResolveInfo info = pm.resolveActivity(this, 65536);
        if (info != null) {
            return new ComponentName(info.activityInfo.applicationInfo.packageName, info.activityInfo.name);
        }
        return null;
    }

    public ActivityInfo resolveActivityInfo(PackageManager pm, int flags) {
        ComponentName componentName = this.mComponent;
        if (componentName != null) {
            try {
                return pm.getActivityInfo(componentName, flags);
            } catch (NameNotFoundException e) {
                return null;
            }
        }
        ResolveInfo info = pm.resolveActivity(this, 65536 | flags);
        if (info != null) {
            return info.activityInfo;
        }
        return null;
    }

    @UnsupportedAppUsage
    public ComponentName resolveSystemService(PackageManager pm, int flags) {
        ComponentName componentName = this.mComponent;
        if (componentName != null) {
            return componentName;
        }
        List<ResolveInfo> results = pm.queryIntentServices(this, flags);
        if (results == null) {
            return null;
        }
        ComponentName comp = null;
        for (int i = 0; i < results.size(); i++) {
            ResolveInfo ri = (ResolveInfo) results.get(i);
            if ((ri.serviceInfo.applicationInfo.flags & 1) != 0) {
                ComponentName foundComp = new ComponentName(ri.serviceInfo.applicationInfo.packageName, ri.serviceInfo.name);
                if (comp == null) {
                    comp = foundComp;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Multiple system services handle ");
                    stringBuilder.append(this);
                    stringBuilder.append(": ");
                    stringBuilder.append(comp);
                    stringBuilder.append(", ");
                    stringBuilder.append(foundComp);
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
        }
        return comp;
    }

    public Intent setAction(String action) {
        this.mAction = action != null ? action.intern() : null;
        return this;
    }

    public Intent setData(Uri data) {
        this.mData = data;
        this.mType = null;
        return this;
    }

    public Intent setDataAndNormalize(Uri data) {
        return setData(data.normalizeScheme());
    }

    public Intent setType(String type) {
        this.mData = null;
        this.mType = type;
        return this;
    }

    public Intent setTypeAndNormalize(String type) {
        return setType(normalizeMimeType(type));
    }

    public Intent setDataAndType(Uri data, String type) {
        this.mData = data;
        this.mType = type;
        return this;
    }

    public Intent setDataAndTypeAndNormalize(Uri data, String type) {
        return setDataAndType(data.normalizeScheme(), normalizeMimeType(type));
    }

    public Intent setIdentifier(String identifier) {
        this.mIdentifier = identifier;
        return this;
    }

    public Intent addCategory(String category) {
        if (this.mCategories == null) {
            this.mCategories = new ArraySet();
        }
        this.mCategories.add(category.intern());
        return this;
    }

    public void removeCategory(String category) {
        ArraySet arraySet = this.mCategories;
        if (arraySet != null) {
            arraySet.remove(category);
            if (this.mCategories.size() == 0) {
                this.mCategories = null;
            }
        }
    }

    public void setSelector(Intent selector) {
        if (selector == this) {
            throw new IllegalArgumentException("Intent being set as a selector of itself");
        } else if (selector == null || this.mPackage == null) {
            this.mSelector = selector;
        } else {
            throw new IllegalArgumentException("Can't set selector when package name is already set");
        }
    }

    public void setClipData(ClipData clip) {
        this.mClipData = clip;
    }

    public void prepareToLeaveUser(int userId) {
        if (this.mContentUserHint == -2) {
            this.mContentUserHint = userId;
        }
    }

    public Intent putExtra(String name, boolean value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putBoolean(name, value);
        return this;
    }

    public Intent putExtra(String name, byte value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putByte(name, value);
        return this;
    }

    public Intent putExtra(String name, char value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putChar(name, value);
        return this;
    }

    public Intent putExtra(String name, short value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putShort(name, value);
        return this;
    }

    public Intent putExtra(String name, int value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putInt(name, value);
        return this;
    }

    public Intent putExtra(String name, long value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putLong(name, value);
        return this;
    }

    public Intent putExtra(String name, float value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putFloat(name, value);
        return this;
    }

    public Intent putExtra(String name, double value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putDouble(name, value);
        return this;
    }

    public Intent putExtra(String name, String value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putString(name, value);
        return this;
    }

    public Intent putExtra(String name, CharSequence value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putCharSequence(name, value);
        return this;
    }

    public Intent putExtra(String name, Parcelable value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putParcelable(name, value);
        return this;
    }

    public Intent putExtra(String name, Parcelable[] value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putParcelableArray(name, value);
        return this;
    }

    public Intent putParcelableArrayListExtra(String name, ArrayList<? extends Parcelable> value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putParcelableArrayList(name, value);
        return this;
    }

    public Intent putIntegerArrayListExtra(String name, ArrayList<Integer> value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putIntegerArrayList(name, value);
        return this;
    }

    public Intent putStringArrayListExtra(String name, ArrayList<String> value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putStringArrayList(name, value);
        return this;
    }

    public Intent putCharSequenceArrayListExtra(String name, ArrayList<CharSequence> value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putCharSequenceArrayList(name, value);
        return this;
    }

    public Intent putExtra(String name, Serializable value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putSerializable(name, value);
        return this;
    }

    public Intent putExtra(String name, boolean[] value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putBooleanArray(name, value);
        return this;
    }

    public Intent putExtra(String name, byte[] value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putByteArray(name, value);
        return this;
    }

    public Intent putExtra(String name, short[] value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putShortArray(name, value);
        return this;
    }

    public Intent putExtra(String name, char[] value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putCharArray(name, value);
        return this;
    }

    public Intent putExtra(String name, int[] value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putIntArray(name, value);
        return this;
    }

    public Intent putExtra(String name, long[] value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putLongArray(name, value);
        return this;
    }

    public Intent putExtra(String name, float[] value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putFloatArray(name, value);
        return this;
    }

    public Intent putExtra(String name, double[] value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putDoubleArray(name, value);
        return this;
    }

    public Intent putExtra(String name, String[] value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putStringArray(name, value);
        return this;
    }

    public Intent putExtra(String name, CharSequence[] value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putCharSequenceArray(name, value);
        return this;
    }

    public Intent putExtra(String name, Bundle value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putBundle(name, value);
        return this;
    }

    @Deprecated
    @UnsupportedAppUsage
    public Intent putExtra(String name, IBinder value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putIBinder(name, value);
        return this;
    }

    public Intent putExtras(Intent src) {
        Bundle bundle = src.mExtras;
        if (bundle != null) {
            Bundle bundle2 = this.mExtras;
            if (bundle2 == null) {
                this.mExtras = new Bundle(bundle);
            } else {
                bundle2.putAll(bundle);
            }
        }
        return this;
    }

    public Intent putExtras(Bundle extras) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putAll(extras);
        return this;
    }

    public Intent replaceExtras(Intent src) {
        Bundle bundle = src.mExtras;
        this.mExtras = bundle != null ? new Bundle(bundle) : null;
        return this;
    }

    public Intent replaceExtras(Bundle extras) {
        this.mExtras = extras != null ? new Bundle(extras) : null;
        return this;
    }

    public void removeExtra(String name) {
        Bundle bundle = this.mExtras;
        if (bundle != null) {
            bundle.remove(name);
            if (this.mExtras.size() == 0) {
                this.mExtras = null;
            }
        }
    }

    public Intent setFlags(int flags) {
        this.mFlags = flags;
        return this;
    }

    public Intent addFlags(int flags) {
        this.mFlags |= flags;
        return this;
    }

    public void removeFlags(int flags) {
        this.mFlags &= ~flags;
    }

    public Intent setPackage(String packageName) {
        if (packageName == null || this.mSelector == null) {
            this.mPackage = packageName;
            return this;
        }
        throw new IllegalArgumentException("Can't set package name when selector is already set");
    }

    public Intent setComponent(ComponentName component) {
        this.mComponent = component;
        return this;
    }

    public Intent setClassName(Context packageContext, String className) {
        this.mComponent = new ComponentName(packageContext, className);
        return this;
    }

    public Intent setClassName(String packageName, String className) {
        this.mComponent = new ComponentName(packageName, className);
        return this;
    }

    public Intent setClass(Context packageContext, Class<?> cls) {
        this.mComponent = new ComponentName(packageContext, (Class) cls);
        return this;
    }

    public void setSourceBounds(Rect r) {
        if (r != null) {
            this.mSourceBounds = new Rect(r);
        } else {
            this.mSourceBounds = null;
        }
    }

    public int fillIn(Intent other, int flags) {
        int changes = 0;
        boolean mayHaveCopiedUris = false;
        if (other.mAction != null && (this.mAction == null || (flags & 1) != 0)) {
            this.mAction = other.mAction;
            changes = 0 | 1;
        }
        if (!(other.mData == null && other.mType == null) && ((this.mData == null && this.mType == null) || (flags & 2) != 0)) {
            this.mData = other.mData;
            this.mType = other.mType;
            changes |= 2;
            mayHaveCopiedUris = true;
        }
        if (other.mIdentifier != null && (this.mIdentifier == null || (flags & 256) != 0)) {
            this.mIdentifier = other.mIdentifier;
            changes |= 256;
        }
        if (other.mCategories != null && (this.mCategories == null || (flags & 4) != 0)) {
            ArraySet arraySet = other.mCategories;
            if (arraySet != null) {
                this.mCategories = new ArraySet(arraySet);
            }
            changes |= 4;
        }
        if (other.mPackage != null && ((this.mPackage == null || (flags & 16) != 0) && this.mSelector == null)) {
            this.mPackage = other.mPackage;
            changes |= 16;
        }
        Intent intent = other.mSelector;
        if (!(intent == null || (flags & 64) == 0 || this.mPackage != null)) {
            this.mSelector = new Intent(intent);
            this.mPackage = null;
            changes |= 64;
        }
        if (other.mClipData != null && (this.mClipData == null || (flags & 128) != 0)) {
            this.mClipData = other.mClipData;
            changes |= 128;
            mayHaveCopiedUris = true;
        }
        ComponentName componentName = other.mComponent;
        if (!(componentName == null || (flags & 8) == 0)) {
            this.mComponent = componentName;
            changes |= 8;
        }
        this.mFlags |= other.mFlags;
        if (other.mSourceBounds != null && (this.mSourceBounds == null || (flags & 32) != 0)) {
            this.mSourceBounds = new Rect(other.mSourceBounds);
            changes |= 32;
        }
        Bundle bundle;
        if (this.mExtras == null) {
            bundle = other.mExtras;
            if (bundle != null) {
                this.mExtras = new Bundle(bundle);
                mayHaveCopiedUris = true;
            }
        } else {
            bundle = other.mExtras;
            if (bundle != null) {
                try {
                    bundle = new Bundle(bundle);
                    bundle.putAll(this.mExtras);
                    this.mExtras = bundle;
                    mayHaveCopiedUris = true;
                } catch (RuntimeException e) {
                    Log.w(TAG, "Failure filling in extras", e);
                }
            }
        }
        if (mayHaveCopiedUris && this.mContentUserHint == -2) {
            int i = other.mContentUserHint;
            if (i != -2) {
                this.mContentUserHint = i;
            }
        }
        return changes;
    }

    public boolean filterEquals(Intent other) {
        if (other != null && Objects.equals(this.mAction, other.mAction) && Objects.equals(this.mData, other.mData) && Objects.equals(this.mType, other.mType) && Objects.equals(this.mIdentifier, other.mIdentifier) && Objects.equals(this.mPackage, other.mPackage) && Objects.equals(this.mComponent, other.mComponent) && Objects.equals(this.mCategories, other.mCategories)) {
            return true;
        }
        return false;
    }

    public int filterHashCode() {
        int code = 0;
        String str = this.mAction;
        if (str != null) {
            code = 0 + str.hashCode();
        }
        Uri uri = this.mData;
        if (uri != null) {
            code += uri.hashCode();
        }
        str = this.mType;
        if (str != null) {
            code += str.hashCode();
        }
        str = this.mIdentifier;
        if (str != null) {
            code += str.hashCode();
        }
        str = this.mPackage;
        if (str != null) {
            code += str.hashCode();
        }
        ComponentName componentName = this.mComponent;
        if (componentName != null) {
            code += componentName.hashCode();
        }
        ArraySet arraySet = this.mCategories;
        if (arraySet != null) {
            return code + arraySet.hashCode();
        }
        return code;
    }

    public String toString() {
        StringBuilder b = new StringBuilder(128);
        b.append("Intent { ");
        toShortString(b, true, true, true, false);
        b.append(" }");
        return b.toString();
    }

    @UnsupportedAppUsage
    public String toInsecureString() {
        StringBuilder b = new StringBuilder(128);
        b.append("Intent { ");
        toShortString(b, false, true, true, false);
        b.append(" }");
        return b.toString();
    }

    public String toInsecureStringWithClip() {
        StringBuilder b = new StringBuilder(128);
        b.append("Intent { ");
        toShortString(b, false, true, true, true);
        b.append(" }");
        return b.toString();
    }

    public String toShortString(boolean secure, boolean comp, boolean extras, boolean clip) {
        StringBuilder b = new StringBuilder(128);
        toShortString(b, secure, comp, extras, clip);
        return b.toString();
    }

    public void toShortString(StringBuilder b, boolean secure, boolean comp, boolean extras, boolean clip) {
        boolean first = true;
        if (this.mAction != null) {
            b.append("act=");
            b.append(this.mAction);
            first = false;
        }
        if (this.mCategories != null) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append("cat=[");
            for (int i = 0; i < this.mCategories.size(); i++) {
                if (i > 0) {
                    b.append(',');
                }
                b.append((String) this.mCategories.valueAt(i));
            }
            b.append("]");
        }
        if (this.mData != null) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append("dat=");
            if (secure) {
                b.append(this.mData.toSafeString());
            } else {
                b.append(this.mData);
            }
        }
        if (this.mType != null) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append("typ=");
            b.append(this.mType);
        }
        if (this.mIdentifier != null) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append("id=");
            b.append(this.mIdentifier);
        }
        if (this.mFlags != 0) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append("flg=0x");
            b.append(Integer.toHexString(this.mFlags));
        }
        if (this.mPackage != null) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append("pkg=");
            b.append(this.mPackage);
        }
        if (comp && this.mComponent != null) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append("cmp=");
            b.append(this.mComponent.flattenToShortString());
        }
        if (this.mSourceBounds != null) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append("bnds=");
            b.append(this.mSourceBounds.toShortString());
        }
        if (this.mClipData != null) {
            if (!first) {
                b.append(' ');
            }
            b.append("clip={");
            if (clip) {
                this.mClipData.toShortString(b);
            } else {
                if (this.mClipData.getDescription() != null) {
                    first = this.mClipData.getDescription().toShortStringTypesOnly(b) ^ 1;
                } else {
                    first = true;
                }
                this.mClipData.toShortStringShortItems(b, first);
            }
            first = false;
            b.append('}');
        }
        if (extras && this.mExtras != null) {
            if (!first) {
                b.append(' ');
            }
            first = false;
            b.append("(has extras)");
        }
        if (this.mContentUserHint != -2) {
            if (!first) {
                b.append(' ');
            }
            b.append("u=");
            b.append(this.mContentUserHint);
        }
        if (this.mSelector != null) {
            b.append(" sel=");
            this.mSelector.toShortString(b, secure, comp, extras, clip);
            b.append("}");
        }
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        writeToProto(proto, fieldId, true, true, true, false);
    }

    public void writeToProto(ProtoOutputStream proto) {
        writeToProtoWithoutFieldId(proto, true, true, true, false);
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId, boolean secure, boolean comp, boolean extras, boolean clip) {
        long token = proto.start(fieldId);
        writeToProtoWithoutFieldId(proto, secure, comp, extras, clip);
        proto.end(token);
    }

    private void writeToProtoWithoutFieldId(ProtoOutputStream proto, boolean secure, boolean comp, boolean extras, boolean clip) {
        String str = this.mAction;
        if (str != null) {
            proto.write(1138166333441L, str);
        }
        ArraySet arraySet = this.mCategories;
        if (arraySet != null) {
            Iterator it = arraySet.iterator();
            while (it.hasNext()) {
                proto.write(2237677961218L, (String) it.next());
            }
        }
        Uri uri = this.mData;
        if (uri != null) {
            proto.write(1138166333443L, secure ? uri.toSafeString() : uri.toString());
        }
        str = this.mType;
        if (str != null) {
            proto.write(1138166333444L, str);
        }
        str = this.mIdentifier;
        if (str != null) {
            proto.write(1138166333453L, str);
        }
        if (this.mFlags != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("0x");
            stringBuilder.append(Integer.toHexString(this.mFlags));
            proto.write(1138166333445L, stringBuilder.toString());
        }
        str = this.mPackage;
        if (str != null) {
            proto.write(1138166333446L, str);
        }
        if (comp) {
            ComponentName componentName = this.mComponent;
            if (componentName != null) {
                componentName.writeToProto(proto, 1146756268039L);
            }
        }
        Rect rect = this.mSourceBounds;
        if (rect != null) {
            proto.write(1138166333448L, rect.toShortString());
        }
        if (this.mClipData != null) {
            StringBuilder b = new StringBuilder();
            if (clip) {
                this.mClipData.toShortString(b);
            } else {
                this.mClipData.toShortStringShortItems(b, false);
            }
            proto.write(1138166333449L, b.toString());
        }
        if (extras) {
            Bundle bundle = this.mExtras;
            if (bundle != null) {
                proto.write(1138166333450L, bundle.toShortString());
            }
        }
        int i = this.mContentUserHint;
        if (i != 0) {
            proto.write(1120986464267L, i);
        }
        Intent intent = this.mSelector;
        if (intent != null) {
            proto.write(1138166333452L, intent.toShortString(secure, comp, extras, clip));
        }
    }

    @Deprecated
    public String toURI() {
        return toUri(0);
    }

    public String toUri(int flags) {
        StringBuilder uri = new StringBuilder(128);
        String scheme;
        String data;
        String str;
        if ((flags & 2) == 0) {
            scheme = null;
            data = this.mData;
            str = "intent:";
            if (data != null) {
                data = data.toString();
                if ((flags & 1) != 0) {
                    int N = data.length();
                    int i = 0;
                    while (i < N) {
                        char c = data.charAt(i);
                        if ((c >= DateFormat.AM_PM && c <= DateFormat.TIME_ZONE) || ((c >= DateFormat.CAPITAL_AM_PM && c <= 'Z') || ((c >= '0' && c <= '9') || c == ClassUtils.PACKAGE_SEPARATOR_CHAR || c == '-' || c == '+'))) {
                            i++;
                        } else if (c == ':' && i > 0) {
                            scheme = data.substring(0, i);
                            uri.append(str);
                            data = data.substring(i + 1);
                        }
                    }
                }
                uri.append(data);
            } else if ((flags & 1) != 0) {
                uri.append(str);
            }
            toUriFragment(uri, scheme, "android.intent.action.VIEW", null, flags);
            return uri.toString();
        } else if (this.mPackage != null) {
            uri.append("android-app://");
            uri.append(this.mPackage);
            scheme = null;
            Uri uri2 = this.mData;
            if (uri2 != null) {
                scheme = uri2.getScheme();
                if (scheme != null) {
                    uri.append('/');
                    uri.append(scheme);
                    str = this.mData.getEncodedAuthority();
                    if (str != null) {
                        uri.append('/');
                        uri.append(str);
                        data = this.mData.getEncodedPath();
                        if (data != null) {
                            uri.append(data);
                        }
                        String queryParams = this.mData.getEncodedQuery();
                        if (queryParams != null) {
                            uri.append('?');
                            uri.append(queryParams);
                        }
                        String fragment = this.mData.getEncodedFragment();
                        if (fragment != null) {
                            uri.append('#');
                            uri.append(fragment);
                        }
                    }
                }
            }
            toUriFragment(uri, null, scheme == null ? ACTION_MAIN : "android.intent.action.VIEW", this.mPackage, flags);
            return uri.toString();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Intent must include an explicit package name to build an android-app: ");
            stringBuilder.append(this);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    private void toUriFragment(StringBuilder uri, String scheme, String defAction, String defPackage, int flags) {
        StringBuilder frag = new StringBuilder(128);
        toUriInner(frag, scheme, defAction, defPackage, flags);
        if (this.mSelector != null) {
            frag.append("SEL;");
            Intent intent = this.mSelector;
            Uri uri2 = intent.mData;
            intent.toUriInner(frag, uri2 != null ? uri2.getScheme() : null, null, null, flags);
        }
        if (frag.length() > 0) {
            uri.append("#Intent;");
            uri.append(frag);
            uri.append("end");
        }
    }

    private void toUriInner(StringBuilder uri, String scheme, String defAction, String defPackage, int flags) {
        if (scheme != null) {
            uri.append("scheme=");
            uri.append(scheme);
            uri.append(';');
        }
        String str = this.mAction;
        if (!(str == null || str.equals(defAction))) {
            uri.append("action=");
            uri.append(Uri.encode(this.mAction));
            uri.append(';');
        }
        if (this.mCategories != null) {
            for (int i = 0; i < this.mCategories.size(); i++) {
                uri.append("category=");
                uri.append(Uri.encode((String) this.mCategories.valueAt(i)));
                uri.append(';');
            }
        }
        String str2 = "/";
        if (this.mType != null) {
            uri.append("type=");
            uri.append(Uri.encode(this.mType, str2));
            uri.append(';');
        }
        if (this.mIdentifier != null) {
            uri.append("identifier=");
            uri.append(Uri.encode(this.mIdentifier, str2));
            uri.append(';');
        }
        if (this.mFlags != 0) {
            uri.append("launchFlags=0x");
            uri.append(Integer.toHexString(this.mFlags));
            uri.append(';');
        }
        str = this.mPackage;
        if (!(str == null || str.equals(defPackage))) {
            uri.append("package=");
            uri.append(Uri.encode(this.mPackage));
            uri.append(';');
        }
        if (this.mComponent != null) {
            uri.append("component=");
            uri.append(Uri.encode(this.mComponent.flattenToShortString(), str2));
            uri.append(';');
        }
        if (this.mSourceBounds != null) {
            uri.append("sourceBounds=");
            uri.append(Uri.encode(this.mSourceBounds.flattenToString()));
            uri.append(';');
        }
        Bundle bundle = this.mExtras;
        if (bundle != null) {
            for (String str22 : bundle.keySet()) {
                char entryType;
                Object value = this.mExtras.get(str22);
                if (value instanceof String) {
                    entryType = 'S';
                } else if (value instanceof Boolean) {
                    entryType = 'B';
                } else if (value instanceof Byte) {
                    entryType = 'b';
                } else if (value instanceof Character) {
                    entryType = 'c';
                } else if (value instanceof Double) {
                    entryType = DateFormat.DATE;
                } else if (value instanceof Float) {
                    entryType = 'f';
                } else if (value instanceof Integer) {
                    entryType = 'i';
                } else if (value instanceof Long) {
                    entryType = 'l';
                } else if (value instanceof Short) {
                    entryType = 's';
                } else {
                    entryType = 0;
                }
                if (entryType != 0) {
                    uri.append(entryType);
                    uri.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
                    uri.append(Uri.encode(str22));
                    uri.append('=');
                    uri.append(Uri.encode(value.toString()));
                    uri.append(';');
                }
            }
        }
    }

    public int describeContents() {
        Bundle bundle = this.mExtras;
        return bundle != null ? bundle.describeContents() : 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mAction);
        Uri.writeToParcel(out, this.mData);
        out.writeString(this.mType);
        out.writeString(this.mIdentifier);
        out.writeInt(this.mFlags);
        out.writeString(this.mPackage);
        ComponentName.writeToParcel(this.mComponent, out);
        if (this.mSourceBounds != null) {
            out.writeInt(1);
            this.mSourceBounds.writeToParcel(out, flags);
        } else {
            out.writeInt(0);
        }
        int N = this.mCategories;
        if (N != 0) {
            N = N.size();
            out.writeInt(N);
            for (int i = 0; i < N; i++) {
                out.writeString((String) this.mCategories.valueAt(i));
            }
        } else {
            out.writeInt(0);
        }
        if (this.mSelector != null) {
            out.writeInt(1);
            this.mSelector.writeToParcel(out, flags);
        } else {
            out.writeInt(0);
        }
        if (this.mClipData != null) {
            out.writeInt(1);
            this.mClipData.writeToParcel(out, flags);
        } else {
            out.writeInt(0);
        }
        out.writeInt(this.mContentUserHint);
        out.writeBundle(this.mExtras);
        out.writeString(this.mSenderPackageName);
        out.writeInt(this.mMiuiFlags);
    }

    protected Intent(Parcel in) {
        this.mContentUserHint = -2;
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        setAction(in.readString());
        this.mData = (Uri) Uri.CREATOR.createFromParcel(in);
        this.mType = in.readString();
        this.mIdentifier = in.readString();
        this.mFlags = in.readInt();
        this.mPackage = in.readString();
        this.mComponent = ComponentName.readFromParcel(in);
        if (in.readInt() != 0) {
            this.mSourceBounds = (Rect) Rect.CREATOR.createFromParcel(in);
        }
        int N = in.readInt();
        if (N > 0) {
            this.mCategories = new ArraySet();
            for (int i = 0; i < N; i++) {
                this.mCategories.add(in.readString().intern());
            }
        } else {
            this.mCategories = null;
        }
        if (in.readInt() != 0) {
            this.mSelector = new Intent(in);
        }
        if (in.readInt() != 0) {
            this.mClipData = new ClipData(in);
        }
        this.mContentUserHint = in.readInt();
        this.mExtras = in.readBundle();
        this.mSenderPackageName = in.readString();
        this.mMiuiFlags = in.readInt();
    }

    public static Intent parseIntent(Resources resources, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        Resources resources2 = resources;
        AttributeSet attributeSet = attrs;
        Intent intent = new Intent();
        TypedArray sa = resources2.obtainAttributes(attributeSet, R.styleable.Intent);
        intent.setAction(sa.getString(2));
        int i = 3;
        String data = sa.getString(3);
        intent.setDataAndType(data != null ? Uri.parse(data) : null, sa.getString(1));
        intent.setIdentifier(sa.getString(5));
        String packageName = sa.getString(0);
        String className = sa.getString(4);
        if (!(packageName == null || className == null)) {
            intent.setComponent(new ComponentName(packageName, className));
        }
        sa.recycle();
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == 1 || (type == i && parser.getDepth() <= outerDepth)) {
                return intent;
            }
            if (type == i || type == 4) {
                i = 3;
            } else {
                String nodeName = parser.getName();
                String cat;
                if (nodeName.equals(TAG_CATEGORIES)) {
                    sa = resources2.obtainAttributes(attributeSet, R.styleable.IntentCategory);
                    cat = sa.getString(0);
                    sa.recycle();
                    if (cat != null) {
                        intent.addCategory(cat);
                    }
                    XmlUtils.skipCurrentTag(parser);
                } else {
                    cat = TAG_EXTRA;
                    if (nodeName.equals(cat)) {
                        if (intent.mExtras == null) {
                            intent.mExtras = new Bundle();
                        }
                        resources2.parseBundleExtra(cat, attributeSet, intent.mExtras);
                        XmlUtils.skipCurrentTag(parser);
                    } else {
                        XmlUtils.skipCurrentTag(parser);
                    }
                }
                i = 3;
            }
        }
        return intent;
    }

    public void saveToXml(XmlSerializer out) throws IOException {
        String str = this.mAction;
        if (str != null) {
            out.attribute(null, "action", str);
        }
        Uri uri = this.mData;
        if (uri != null) {
            out.attribute(null, "data", uri.toString());
        }
        str = this.mType;
        if (str != null) {
            out.attribute(null, "type", str);
        }
        str = this.mIdentifier;
        if (str != null) {
            out.attribute(null, ATTR_IDENTIFIER, str);
        }
        ComponentName componentName = this.mComponent;
        if (componentName != null) {
            out.attribute(null, "component", componentName.flattenToShortString());
        }
        out.attribute(null, "flags", Integer.toHexString(getFlags()));
        if (this.mCategories != null) {
            str = TAG_CATEGORIES;
            out.startTag(null, str);
            for (int categoryNdx = this.mCategories.size() - 1; categoryNdx >= 0; categoryNdx--) {
                out.attribute(null, "category", (String) this.mCategories.valueAt(categoryNdx));
            }
            out.endTag(null, str);
        }
    }

    public static Intent restoreFromXml(XmlPullParser in) throws IOException, XmlPullParserException {
        String str;
        Intent intent = new Intent();
        int outerDepth = in.getDepth();
        int attrNdx = in.getAttributeCount() - 1;
        while (true) {
            str = TAG;
            if (attrNdx < 0) {
                break;
            }
            String attrName = in.getAttributeName(attrNdx);
            String attrValue = in.getAttributeValue(attrNdx);
            if ("action".equals(attrName)) {
                intent.setAction(attrValue);
            } else if ("data".equals(attrName)) {
                intent.setData(Uri.parse(attrValue));
            } else if ("type".equals(attrName)) {
                intent.setType(attrValue);
            } else if (ATTR_IDENTIFIER.equals(attrName)) {
                intent.setIdentifier(attrValue);
            } else if ("component".equals(attrName)) {
                intent.setComponent(ComponentName.unflattenFromString(attrValue));
            } else if ("flags".equals(attrName)) {
                intent.setFlags(Integer.parseInt(attrValue, 16));
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("restoreFromXml: unknown attribute=");
                stringBuilder.append(attrName);
                Log.e(str, stringBuilder.toString());
            }
            attrNdx--;
        }
        while (true) {
            attrNdx = in.next();
            int event = attrNdx;
            if (attrNdx == 1 || (event == 3 && in.getDepth() >= outerDepth)) {
                return intent;
            }
            if (event == 2) {
                String name = in.getName();
                if (TAG_CATEGORIES.equals(name)) {
                    for (int attrNdx2 = in.getAttributeCount() - 1; attrNdx2 >= 0; attrNdx2--) {
                        intent.addCategory(in.getAttributeValue(attrNdx2));
                    }
                } else {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("restoreFromXml: unknown name=");
                    stringBuilder2.append(name);
                    Log.w(str, stringBuilder2.toString());
                    XmlUtils.skipCurrentTag(in);
                }
            }
        }
        return intent;
    }

    public static String normalizeMimeType(String type) {
        if (type == null) {
            return null;
        }
        type = type.trim().toLowerCase(Locale.ROOT);
        int semicolonIndex = type.indexOf(59);
        if (semicolonIndex != -1) {
            type = type.substring(0, semicolonIndex);
        }
        return type;
    }

    @UnsupportedAppUsage
    public void prepareToLeaveProcess(Context context) {
        ComponentName componentName = this.mComponent;
        boolean leavingPackage = componentName == null || !Objects.equals(componentName.getPackageName(), context.getPackageName());
        prepareToLeaveProcess(leavingPackage);
    }

    /* JADX WARNING: Missing block: B:91:0x0149, code skipped:
            if (r1.equals(ACTION_PROVIDER_CHANGED) == false) goto L_0x0156;
     */
    public void prepareToLeaveProcess(boolean r9) {
        /*
        r8 = this;
        r0 = 0;
        r8.setAllowFds(r0);
        r1 = r8.mSelector;
        if (r1 == 0) goto L_0x000b;
    L_0x0008:
        r1.prepareToLeaveProcess(r9);
    L_0x000b:
        r1 = r8.mClipData;
        if (r1 == 0) goto L_0x0016;
    L_0x000f:
        r2 = r8.getFlags();
        r1.prepareToLeaveProcess(r9, r2);
    L_0x0016:
        r1 = r8.mExtras;
        if (r1 == 0) goto L_0x0032;
    L_0x001a:
        r1 = r1.isParcelled();
        if (r1 != 0) goto L_0x0032;
    L_0x0020:
        r1 = r8.mExtras;
        r2 = "android.intent.extra.INTENT";
        r1 = r1.get(r2);
        r2 = r1 instanceof android.content.Intent;
        if (r2 == 0) goto L_0x0032;
    L_0x002c:
        r2 = r1;
        r2 = (android.content.Intent) r2;
        r2.prepareToLeaveProcess(r9);
    L_0x0032:
        r1 = r8.mAction;
        r2 = "Intent.getData()";
        r3 = -1;
        r4 = "android.intent.action.MEDIA_SCANNER_SCAN_FILE";
        r5 = 1;
        if (r1 == 0) goto L_0x0122;
    L_0x003c:
        r1 = r8.mData;
        if (r1 == 0) goto L_0x0122;
    L_0x0040:
        r1 = android.os.StrictMode.vmFileUriExposureEnabled();
        if (r1 == 0) goto L_0x0122;
    L_0x0046:
        if (r9 == 0) goto L_0x0122;
    L_0x0048:
        r1 = r8.mAction;
        r6 = r1.hashCode();
        switch(r6) {
            case -1823790459: goto L_0x010e;
            case -1665311200: goto L_0x0104;
            case -1514214344: goto L_0x00fa;
            case -1142424621: goto L_0x00ef;
            case -963871873: goto L_0x00e5;
            case -808646005: goto L_0x00da;
            case -625887599: goto L_0x00cf;
            case -180699344: goto L_0x00c4;
            case 257177710: goto L_0x00ba;
            case 410719838: goto L_0x00af;
            case 582421979: goto L_0x00a3;
            case 852070077: goto L_0x0099;
            case 1412829408: goto L_0x008d;
            case 1431947322: goto L_0x0081;
            case 1599438242: goto L_0x0075;
            case 1920444806: goto L_0x0069;
            case 1964681210: goto L_0x005e;
            case 2045140818: goto L_0x0053;
            default: goto L_0x0051;
        };
    L_0x0051:
        goto L_0x0118;
    L_0x0053:
        r6 = "android.intent.action.MEDIA_BAD_REMOVAL";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x005b:
        r1 = 7;
        goto L_0x0119;
    L_0x005e:
        r6 = "android.intent.action.MEDIA_CHECKING";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x0066:
        r1 = 2;
        goto L_0x0119;
    L_0x0069:
        r6 = "android.intent.action.PACKAGE_VERIFIED";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x0071:
        r1 = 15;
        goto L_0x0119;
    L_0x0075:
        r6 = "android.intent.action.PACKAGE_ENABLE_ROLLBACK";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x007d:
        r1 = 16;
        goto L_0x0119;
    L_0x0081:
        r6 = "android.intent.action.MEDIA_UNMOUNTABLE";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x0089:
        r1 = 8;
        goto L_0x0119;
    L_0x008d:
        r6 = "android.intent.action.MEDIA_SCANNER_STARTED";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x0095:
        r1 = 10;
        goto L_0x0119;
    L_0x0099:
        r1 = r1.equals(r4);
        if (r1 == 0) goto L_0x0051;
    L_0x009f:
        r1 = 12;
        goto L_0x0119;
    L_0x00a3:
        r6 = "android.intent.action.PACKAGE_NEEDS_VERIFICATION";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x00ab:
        r1 = 13;
        goto L_0x0119;
    L_0x00af:
        r6 = "android.intent.action.MEDIA_UNSHARED";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x00b7:
        r1 = 6;
        goto L_0x0119;
    L_0x00ba:
        r6 = "android.intent.action.MEDIA_NOFS";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x00c2:
        r1 = 3;
        goto L_0x0119;
    L_0x00c4:
        r6 = "miui.intent.action.MEDIA_SCANNER_SCAN_FOLDER";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x00cc:
        r1 = 17;
        goto L_0x0119;
    L_0x00cf:
        r6 = "android.intent.action.MEDIA_EJECT";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x00d7:
        r1 = 9;
        goto L_0x0119;
    L_0x00da:
        r6 = "com.qualcomm.qti.intent.action.PACKAGE_NEEDS_OPTIONAL_VERIFICATION";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x00e2:
        r1 = 14;
        goto L_0x0119;
    L_0x00e5:
        r6 = "android.intent.action.MEDIA_UNMOUNTED";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x00ed:
        r1 = r5;
        goto L_0x0119;
    L_0x00ef:
        r6 = "android.intent.action.MEDIA_SCANNER_FINISHED";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x00f7:
        r1 = 11;
        goto L_0x0119;
    L_0x00fa:
        r6 = "android.intent.action.MEDIA_MOUNTED";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x0102:
        r1 = 4;
        goto L_0x0119;
    L_0x0104:
        r6 = "android.intent.action.MEDIA_REMOVED";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x010c:
        r1 = r0;
        goto L_0x0119;
    L_0x010e:
        r6 = "android.intent.action.MEDIA_SHARED";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0051;
    L_0x0116:
        r1 = 5;
        goto L_0x0119;
    L_0x0118:
        r1 = r3;
    L_0x0119:
        switch(r1) {
            case 0: goto L_0x0122;
            case 1: goto L_0x0122;
            case 2: goto L_0x0122;
            case 3: goto L_0x0122;
            case 4: goto L_0x0122;
            case 5: goto L_0x0122;
            case 6: goto L_0x0122;
            case 7: goto L_0x0122;
            case 8: goto L_0x0122;
            case 9: goto L_0x0122;
            case 10: goto L_0x0122;
            case 11: goto L_0x0122;
            case 12: goto L_0x0122;
            case 13: goto L_0x0122;
            case 14: goto L_0x0122;
            case 15: goto L_0x0122;
            case 16: goto L_0x0122;
            case 17: goto L_0x0122;
            default: goto L_0x011c;
        };
    L_0x011c:
        r1 = r8.mData;
        r1.checkFileUriExposed(r2);
    L_0x0122:
        r1 = r8.mAction;
        if (r1 == 0) goto L_0x0165;
    L_0x0126:
        r1 = r8.mData;
        if (r1 == 0) goto L_0x0165;
    L_0x012a:
        r1 = android.os.StrictMode.vmContentUriWithoutPermissionEnabled();
        if (r1 == 0) goto L_0x0165;
    L_0x0130:
        if (r9 == 0) goto L_0x0165;
    L_0x0132:
        r1 = r8.mAction;
        r6 = r1.hashCode();
        r7 = -577088908; // 0xffffffffdd9a5274 float:-1.39000975E18 double:NaN;
        if (r6 == r7) goto L_0x014c;
    L_0x013d:
        r7 = 1662413067; // 0x6316690b float:2.7745808E21 double:8.213411856E-315;
        if (r6 == r7) goto L_0x0143;
    L_0x0142:
        goto L_0x0156;
    L_0x0143:
        r6 = "android.intent.action.PROVIDER_CHANGED";
        r1 = r1.equals(r6);
        if (r1 == 0) goto L_0x0142;
    L_0x014b:
        goto L_0x0157;
    L_0x014c:
        r0 = "android.provider.action.QUICK_CONTACT";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x0142;
    L_0x0154:
        r0 = r5;
        goto L_0x0157;
    L_0x0156:
        r0 = r3;
    L_0x0157:
        if (r0 == 0) goto L_0x0165;
    L_0x0159:
        if (r0 == r5) goto L_0x0165;
    L_0x015b:
        r0 = r8.mData;
        r1 = r8.getFlags();
        r0.checkContentUriWithoutPermission(r2, r1);
    L_0x0165:
        r0 = r8.mAction;
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x01cd;
    L_0x016d:
        r0 = r8.mData;
        if (r0 == 0) goto L_0x01cd;
    L_0x0171:
        r0 = r0.getScheme();
        r1 = "file";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x01cd;
    L_0x017d:
        if (r9 == 0) goto L_0x01cd;
    L_0x017f:
        r0 = android.app.AppGlobals.getInitialApplication();
        r1 = android.os.storage.StorageManager.class;
        r0 = r0.getSystemService(r1);
        r0 = (android.os.storage.StorageManager) r0;
        r1 = new java.io.File;
        r2 = r8.mData;
        r2 = r2.getPath();
        r1.<init>(r2);
        r2 = android.os.Process.myPid();
        r3 = android.os.Process.myUid();
        r2 = r0.translateAppToSystem(r1, r2, r3);
        r3 = java.util.Objects.equals(r1, r2);
        if (r3 != 0) goto L_0x01cd;
    L_0x01a9:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Translated ";
        r3.append(r4);
        r3.append(r1);
        r4 = " to ";
        r3.append(r4);
        r3.append(r2);
        r3 = r3.toString();
        r4 = "Intent";
        android.util.Log.v(r4, r3);
        r3 = android.net.Uri.fromFile(r2);
        r8.mData = r3;
    L_0x01cd:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.Intent.prepareToLeaveProcess(boolean):void");
    }

    public void prepareToEnterProcess() {
        setDefusable(true);
        Intent intent = this.mSelector;
        if (intent != null) {
            intent.prepareToEnterProcess();
        }
        ClipData clipData = this.mClipData;
        if (clipData != null) {
            clipData.prepareToEnterProcess();
        }
        if (this.mContentUserHint != -2 && UserHandle.getAppId(Process.myUid()) != 1000) {
            fixUris(this.mContentUserHint);
            this.mContentUserHint = -2;
        }
    }

    public boolean hasWebURI() {
        boolean z = false;
        if (getData() == null) {
            return false;
        }
        String scheme = getScheme();
        if (TextUtils.isEmpty(scheme)) {
            return false;
        }
        if (scheme.equals(IntentFilter.SCHEME_HTTP) || scheme.equals(IntentFilter.SCHEME_HTTPS)) {
            z = true;
        }
        return z;
    }

    public boolean isWebIntent() {
        return "android.intent.action.VIEW".equals(this.mAction) && hasWebURI();
    }

    public void fixUris(int contentUserHint) {
        Uri data = getData();
        if (data != null) {
            this.mData = ContentProvider.maybeAddUserId(data, contentUserHint);
        }
        ClipData clipData = this.mClipData;
        if (clipData != null) {
            clipData.fixUris(contentUserHint);
        }
        String action = getAction();
        boolean equals = ACTION_SEND.equals(action);
        String str = EXTRA_STREAM;
        if (equals) {
            Uri stream = (Uri) getParcelableExtra(str);
            if (stream != null) {
                putExtra(str, ContentProvider.maybeAddUserId(stream, contentUserHint));
            }
        } else if (ACTION_SEND_MULTIPLE.equals(action)) {
            ArrayList<Uri> streams = getParcelableArrayListExtra(str);
            if (streams != null) {
                ArrayList<Uri> newStreams = new ArrayList();
                for (int i = 0; i < streams.size(); i++) {
                    newStreams.add(ContentProvider.maybeAddUserId((Uri) streams.get(i), contentUserHint));
                }
                putParcelableArrayListExtra(str, newStreams);
            }
        } else if (MediaStore.ACTION_IMAGE_CAPTURE.equals(action) || MediaStore.ACTION_IMAGE_CAPTURE_SECURE.equals(action) || MediaStore.ACTION_VIDEO_CAPTURE.equals(action)) {
            String str2 = MediaStore.EXTRA_OUTPUT;
            Uri output = (Uri) getParcelableExtra(str2);
            if (output != null) {
                putExtra(str2, ContentProvider.maybeAddUserId(output, contentUserHint));
            }
        }
    }

    public boolean migrateExtraStreamToClipData() {
        Bundle bundle = this.mExtras;
        int i = 0;
        if ((bundle != null && bundle.isParcelled()) || getClipData() != null) {
            return false;
        }
        String action = getAction();
        boolean migrated;
        if (ACTION_CHOOSER.equals(action)) {
            migrated = false;
            try {
                Intent intent = (Intent) getParcelableExtra(EXTRA_INTENT);
                if (intent != null) {
                    migrated = false | intent.migrateExtraStreamToClipData();
                }
            } catch (ClassCastException e) {
            }
            try {
                Parcelable[] intents = getParcelableArrayExtra(EXTRA_INITIAL_INTENTS);
                if (intents != null) {
                    while (i < intents.length) {
                        Intent intent2 = intents[i];
                        if (intent2 != null) {
                            migrated |= intent2.migrateExtraStreamToClipData();
                        }
                        i++;
                    }
                }
            } catch (ClassCastException e2) {
            }
            return migrated;
        }
        migrated = ACTION_SEND.equals(action);
        ArrayList<String> htmlTexts = EXTRA_HTML_TEXT;
        ArrayList<CharSequence> texts = EXTRA_TEXT;
        String str = EXTRA_STREAM;
        Uri stream;
        if (migrated) {
            try {
                stream = (Uri) getParcelableExtra(str);
                CharSequence text = getCharSequenceExtra(texts);
                String htmlText = getStringExtra(htmlTexts);
                if (stream == null && text == null) {
                    if (htmlText != null) {
                    }
                }
                setClipData(new ClipData(null, new String[]{getType()}, new Item(text, htmlText, null, stream)));
                addFlags(1);
                return true;
            } catch (ClassCastException e3) {
            }
        } else if (ACTION_SEND_MULTIPLE.equals(action)) {
            try {
                ArrayList<Uri> streams = getParcelableArrayListExtra(str);
                texts = getCharSequenceArrayListExtra(texts);
                htmlTexts = getStringArrayListExtra(htmlTexts);
                int num = -1;
                if (streams != null) {
                    num = streams.size();
                }
                if (texts != null) {
                    if (num >= 0 && num != texts.size()) {
                        return false;
                    }
                    num = texts.size();
                }
                if (htmlTexts != null) {
                    if (num >= 0 && num != htmlTexts.size()) {
                        return false;
                    }
                    num = htmlTexts.size();
                }
                if (num > 0) {
                    ClipData clipData = new ClipData(null, new String[]{getType()}, makeClipItem(streams, texts, htmlTexts, 0));
                    for (int i2 = 1; i2 < num; i2++) {
                        clipData.addItem(makeClipItem(streams, texts, htmlTexts, i2));
                    }
                    setClipData(clipData);
                    addFlags(1);
                    return true;
                }
            } catch (ClassCastException e4) {
            }
        } else if (MediaStore.ACTION_IMAGE_CAPTURE.equals(action) || MediaStore.ACTION_IMAGE_CAPTURE_SECURE.equals(action) || MediaStore.ACTION_VIDEO_CAPTURE.equals(action)) {
            try {
                stream = (Uri) getParcelableExtra(MediaStore.EXTRA_OUTPUT);
                if (stream != null) {
                    setClipData(ClipData.newRawUri("", stream));
                    addFlags(3);
                    return true;
                }
            } catch (ClassCastException e5) {
                return false;
            }
        }
        return false;
    }

    public static String dockStateToString(int dock) {
        if (dock == 0) {
            return "EXTRA_DOCK_STATE_UNDOCKED";
        }
        if (dock == 1) {
            return "EXTRA_DOCK_STATE_DESK";
        }
        if (dock == 2) {
            return "EXTRA_DOCK_STATE_CAR";
        }
        if (dock == 3) {
            return "EXTRA_DOCK_STATE_LE_DESK";
        }
        if (dock != 4) {
            return Integer.toString(dock);
        }
        return "EXTRA_DOCK_STATE_HE_DESK";
    }

    private static Item makeClipItem(ArrayList<Uri> streams, ArrayList<CharSequence> texts, ArrayList<String> htmlTexts, int which) {
        return new Item(texts != null ? (CharSequence) texts.get(which) : null, htmlTexts != null ? (String) htmlTexts.get(which) : null, null, streams != null ? (Uri) streams.get(which) : null);
    }

    public boolean isDocument() {
        return (this.mFlags & 524288) == 524288;
    }

    public void setSender(String packageName) {
        this.mSenderPackageName = packageName;
    }

    public String getSender() {
        return this.mSenderPackageName;
    }

    public Intent addMiuiFlags(int flags) {
        this.mMiuiFlags |= flags;
        return this;
    }

    public Intent setMiuiFlags(int flags) {
        this.mMiuiFlags = flags;
        return this;
    }

    public int getMiuiFlags() {
        return this.mMiuiFlags;
    }
}
