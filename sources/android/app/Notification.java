package android.app;

import android.Manifest.permission;
import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.Intent;
import android.content.LocusId;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.AudioAttributes;
import android.media.PlayerBase;
import android.media.session.MediaSession.Token;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.BidiFormatter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import android.widget.RemoteViews;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.ContrastColorUtil;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class Notification implements Parcelable {
    public static final AudioAttributes AUDIO_ATTRIBUTES_DEFAULT = new android.media.AudioAttributes.Builder().setContentType(4).setUsage(5).build();
    public static final int BADGE_ICON_LARGE = 2;
    public static final int BADGE_ICON_NONE = 0;
    public static final int BADGE_ICON_SMALL = 1;
    public static final String CATEGORY_ALARM = "alarm";
    public static final String CATEGORY_CALL = "call";
    @SystemApi
    public static final String CATEGORY_CAR_EMERGENCY = "car_emergency";
    @SystemApi
    public static final String CATEGORY_CAR_INFORMATION = "car_information";
    @SystemApi
    public static final String CATEGORY_CAR_WARNING = "car_warning";
    public static final String CATEGORY_EMAIL = "email";
    public static final String CATEGORY_ERROR = "err";
    public static final String CATEGORY_EVENT = "event";
    public static final String CATEGORY_MESSAGE = "msg";
    public static final String CATEGORY_NAVIGATION = "navigation";
    public static final String CATEGORY_PROGRESS = "progress";
    public static final String CATEGORY_PROMO = "promo";
    public static final String CATEGORY_RECOMMENDATION = "recommendation";
    public static final String CATEGORY_REMINDER = "reminder";
    public static final String CATEGORY_SERVICE = "service";
    public static final String CATEGORY_SOCIAL = "social";
    public static final String CATEGORY_STATUS = "status";
    public static final String CATEGORY_SYSTEM = "sys";
    public static final String CATEGORY_TRANSPORT = "transport";
    public static final int COLOR_DEFAULT = 0;
    public static final int COLOR_INVALID = 1;
    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        public Notification createFromParcel(Parcel parcel) {
            return new Notification(parcel);
        }

        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
    public static final int DEFAULT_ALL = -1;
    public static final int DEFAULT_LIGHTS = 4;
    public static final int DEFAULT_SOUND = 1;
    public static final int DEFAULT_VIBRATE = 2;
    @SystemApi
    public static final String EXTRA_ALLOW_DURING_SETUP = "android.allowDuringSetup";
    public static final String EXTRA_AUDIO_CONTENTS_URI = "android.audioContents";
    public static final String EXTRA_BACKGROUND_IMAGE_URI = "android.backgroundImageUri";
    public static final String EXTRA_BIG_TEXT = "android.bigText";
    public static final String EXTRA_BUILDER_APPLICATION_INFO = "android.appInfo";
    public static final String EXTRA_CHANNEL_GROUP_ID = "android.intent.extra.CHANNEL_GROUP_ID";
    public static final String EXTRA_CHANNEL_ID = "android.intent.extra.CHANNEL_ID";
    public static final String EXTRA_CHRONOMETER_COUNT_DOWN = "android.chronometerCountDown";
    public static final String EXTRA_COLORIZED = "android.colorized";
    public static final String EXTRA_COMPACT_ACTIONS = "android.compactActions";
    public static final String EXTRA_CONTAINS_CUSTOM_VIEW = "android.contains.customView";
    public static final String EXTRA_CONVERSATION_TITLE = "android.conversationTitle";
    public static final String EXTRA_FOREGROUND_APPS = "android.foregroundApps";
    public static final String EXTRA_HIDE_SMART_REPLIES = "android.hideSmartReplies";
    public static final String EXTRA_HISTORIC_MESSAGES = "android.messages.historic";
    public static final String EXTRA_INFO_TEXT = "android.infoText";
    public static final String EXTRA_IS_GROUP_CONVERSATION = "android.isGroupConversation";
    @Deprecated
    public static final String EXTRA_LARGE_ICON = "android.largeIcon";
    public static final String EXTRA_LARGE_ICON_BIG = "android.largeIcon.big";
    public static final String EXTRA_MEDIA_SESSION = "android.mediaSession";
    public static final String EXTRA_MESSAGES = "android.messages";
    public static final String EXTRA_MESSAGING_PERSON = "android.messagingUser";
    public static final String EXTRA_NOTIFICATION_ID = "android.intent.extra.NOTIFICATION_ID";
    public static final String EXTRA_NOTIFICATION_TAG = "android.intent.extra.NOTIFICATION_TAG";
    public static final String EXTRA_PEOPLE = "android.people";
    public static final String EXTRA_PEOPLE_LIST = "android.people.list";
    public static final String EXTRA_PICTURE = "android.picture";
    public static final String EXTRA_PROGRESS = "android.progress";
    public static final String EXTRA_PROGRESS_INDETERMINATE = "android.progressIndeterminate";
    public static final String EXTRA_PROGRESS_MAX = "android.progressMax";
    public static final String EXTRA_REDUCED_IMAGES = "android.reduced.images";
    public static final String EXTRA_REMOTE_INPUT_DRAFT = "android.remoteInputDraft";
    public static final String EXTRA_REMOTE_INPUT_HISTORY = "android.remoteInputHistory";
    public static final String EXTRA_SELF_DISPLAY_NAME = "android.selfDisplayName";
    public static final String EXTRA_SHOW_CHRONOMETER = "android.showChronometer";
    public static final String EXTRA_SHOW_REMOTE_INPUT_SPINNER = "android.remoteInputSpinner";
    public static final String EXTRA_SHOW_WHEN = "android.showWhen";
    @Deprecated
    public static final String EXTRA_SMALL_ICON = "android.icon";
    @SystemApi
    public static final String EXTRA_SUBSTITUTE_APP_NAME = "android.substName";
    public static final String EXTRA_SUB_TEXT = "android.subText";
    public static final String EXTRA_SUMMARY_TEXT = "android.summaryText";
    public static final String EXTRA_TEMPLATE = "android.template";
    public static final String EXTRA_TEXT = "android.text";
    public static final String EXTRA_TEXT_LINES = "android.textLines";
    public static final String EXTRA_TITLE = "android.title";
    public static final String EXTRA_TITLE_BIG = "android.title.big";
    @SystemApi
    public static final int FLAG_AUTOGROUP_SUMMARY = 1024;
    public static final int FLAG_AUTO_CANCEL = 16;
    public static final int FLAG_BUBBLE = 4096;
    public static final int FLAG_CAN_COLORIZE = 2048;
    public static final int FLAG_FOREGROUND_SERVICE = 64;
    public static final int FLAG_GROUP_SUMMARY = 512;
    @Deprecated
    public static final int FLAG_HIGH_PRIORITY = 128;
    public static final int FLAG_INSISTENT = 4;
    public static final int FLAG_LOCAL_ONLY = 256;
    public static final int FLAG_NO_CLEAR = 32;
    public static final int FLAG_ONGOING_EVENT = 2;
    public static final int FLAG_ONLY_ALERT_ONCE = 8;
    @Deprecated
    public static final int FLAG_SHOW_LIGHTS = 1;
    public static final int GROUP_ALERT_ALL = 0;
    public static final int GROUP_ALERT_CHILDREN = 2;
    public static final int GROUP_ALERT_SUMMARY = 1;
    public static final String INTENT_CATEGORY_NOTIFICATION_PREFERENCES = "android.intent.category.NOTIFICATION_PREFERENCES";
    public static final int MAX_ACTION_BUTTONS = 3;
    private static final int MAX_CHARSEQUENCE_LENGTH = 5120;
    private static final int MAX_REPLY_HISTORY = 5;
    @Deprecated
    public static final int PRIORITY_DEFAULT = 0;
    @Deprecated
    public static final int PRIORITY_HIGH = 1;
    @Deprecated
    public static final int PRIORITY_LOW = -1;
    @Deprecated
    public static final int PRIORITY_MAX = 2;
    @Deprecated
    public static final int PRIORITY_MIN = -2;
    private static final ArraySet<Integer> STANDARD_LAYOUTS = new ArraySet();
    @Deprecated
    public static final int STREAM_DEFAULT = -1;
    private static final String TAG = "Notification";
    public static final int VISIBILITY_PRIVATE = 0;
    public static final int VISIBILITY_PUBLIC = 1;
    public static final int VISIBILITY_SECRET = -1;
    public static IBinder processWhitelistToken;
    public Action[] actions;
    @UnsupportedAppUsage
    public ArraySet<PendingIntent> allPendingIntents;
    @Deprecated
    public AudioAttributes audioAttributes;
    @Deprecated
    public int audioStreamType;
    @Deprecated
    public RemoteViews bigContentView;
    public String category;
    public int color;
    public PendingIntent contentIntent;
    @Deprecated
    public RemoteViews contentView;
    private long creationTime;
    @Deprecated
    public int defaults;
    public PendingIntent deleteIntent;
    public MiuiNotification extraNotification;
    public Bundle extras;
    public int flags;
    public PendingIntent fullScreenIntent;
    @Deprecated
    public RemoteViews headsUpContentView;
    @Deprecated
    public int icon;
    public int iconLevel;
    @Deprecated
    public Bitmap largeIcon;
    @Deprecated
    public int ledARGB;
    @Deprecated
    public int ledOffMS;
    @Deprecated
    public int ledOnMS;
    private boolean mAllowSystemGeneratedContextualActions;
    private int mBadgeIcon;
    private BubbleMetadata mBubbleMetadata;
    @UnsupportedAppUsage
    private String mChannelId;
    private int mGroupAlertBehavior;
    @UnsupportedAppUsage
    private String mGroupKey;
    @UnsupportedAppUsage
    private Icon mLargeIcon;
    private LocusId mLocusId;
    private CharSequence mSettingsText;
    private String mShortcutId;
    @UnsupportedAppUsage
    private Icon mSmallIcon;
    private String mSortKey;
    private long mTimeout;
    private boolean mUsesStandardHeader;
    private IBinder mWhitelistToken;
    public int number;
    @Deprecated
    public int priority;
    public Notification publicVersion;
    @Deprecated
    public Uri sound;
    public CharSequence tickerText;
    @Deprecated
    public RemoteViews tickerView;
    @Deprecated
    public long[] vibrate;
    public int visibility;
    public long when;

    public static class Action implements Parcelable {
        public static final Creator<Action> CREATOR = new Creator<Action>() {
            public Action createFromParcel(Parcel in) {
                return new Action(in, null);
            }

            public Action[] newArray(int size) {
                return new Action[size];
            }
        };
        private static final String EXTRA_DATA_ONLY_INPUTS = "android.extra.DATA_ONLY_INPUTS";
        public static final int SEMANTIC_ACTION_ARCHIVE = 5;
        public static final int SEMANTIC_ACTION_CALL = 10;
        public static final int SEMANTIC_ACTION_DELETE = 4;
        public static final int SEMANTIC_ACTION_MARK_AS_READ = 2;
        public static final int SEMANTIC_ACTION_MARK_AS_UNREAD = 3;
        public static final int SEMANTIC_ACTION_MUTE = 6;
        public static final int SEMANTIC_ACTION_NONE = 0;
        public static final int SEMANTIC_ACTION_REPLY = 1;
        public static final int SEMANTIC_ACTION_THUMBS_DOWN = 9;
        public static final int SEMANTIC_ACTION_THUMBS_UP = 8;
        public static final int SEMANTIC_ACTION_UNMUTE = 7;
        public PendingIntent actionIntent;
        @Deprecated
        public int icon;
        private boolean mAllowGeneratedReplies;
        private final Bundle mExtras;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        private Icon mIcon;
        private final boolean mIsContextual;
        private final RemoteInput[] mRemoteInputs;
        private final int mSemanticAction;
        public CharSequence title;

        public static final class Builder {
            private boolean mAllowGeneratedReplies;
            private final Bundle mExtras;
            private final Icon mIcon;
            private final PendingIntent mIntent;
            private boolean mIsContextual;
            private ArrayList<RemoteInput> mRemoteInputs;
            private int mSemanticAction;
            private final CharSequence mTitle;

            @Deprecated
            public Builder(int icon, CharSequence title, PendingIntent intent) {
                this(Icon.createWithResource("", icon), title, intent);
            }

            public Builder(Icon icon, CharSequence title, PendingIntent intent) {
                this(icon, title, intent, new Bundle(), null, true, 0);
            }

            public Builder(Action action) {
                this(action.getIcon(), action.title, action.actionIntent, new Bundle(action.mExtras), action.getRemoteInputs(), action.getAllowGeneratedReplies(), action.getSemanticAction());
            }

            private Builder(Icon icon, CharSequence title, PendingIntent intent, Bundle extras, RemoteInput[] remoteInputs, boolean allowGeneratedReplies, int semanticAction) {
                this.mAllowGeneratedReplies = true;
                this.mIcon = icon;
                this.mTitle = title;
                this.mIntent = intent;
                this.mExtras = extras;
                if (remoteInputs != null) {
                    this.mRemoteInputs = new ArrayList(remoteInputs.length);
                    Collections.addAll(this.mRemoteInputs, remoteInputs);
                }
                this.mAllowGeneratedReplies = allowGeneratedReplies;
                this.mSemanticAction = semanticAction;
            }

            public Builder addExtras(Bundle extras) {
                if (extras != null) {
                    this.mExtras.putAll(extras);
                }
                return this;
            }

            public Bundle getExtras() {
                return this.mExtras;
            }

            public Builder addRemoteInput(RemoteInput remoteInput) {
                if (this.mRemoteInputs == null) {
                    this.mRemoteInputs = new ArrayList();
                }
                this.mRemoteInputs.add(remoteInput);
                return this;
            }

            public Builder setAllowGeneratedReplies(boolean allowGeneratedReplies) {
                this.mAllowGeneratedReplies = allowGeneratedReplies;
                return this;
            }

            public Builder setSemanticAction(int semanticAction) {
                this.mSemanticAction = semanticAction;
                return this;
            }

            public Builder setContextual(boolean isContextual) {
                this.mIsContextual = isContextual;
                return this;
            }

            public Builder extend(Extender extender) {
                extender.extend(this);
                return this;
            }

            private void checkContextualActionNullFields() {
                if (!this.mIsContextual) {
                    return;
                }
                if (this.mIcon == null) {
                    throw new NullPointerException("Contextual Actions must contain a valid icon");
                } else if (this.mIntent == null) {
                    throw new NullPointerException("Contextual Actions must contain a valid PendingIntent");
                }
            }

            public Action build() {
                RemoteInput input;
                checkContextualActionNullFields();
                ArrayList<RemoteInput> dataOnlyInputs = new ArrayList();
                Bundle bundle = this.mExtras;
                String str = Action.EXTRA_DATA_ONLY_INPUTS;
                RemoteInput[] previousDataInputs = (RemoteInput[]) bundle.getParcelableArray(str);
                if (previousDataInputs != null) {
                    for (RemoteInput input2 : previousDataInputs) {
                        dataOnlyInputs.add(input2);
                    }
                }
                List<RemoteInput> textInputs = new ArrayList();
                ArrayList arrayList = this.mRemoteInputs;
                if (arrayList != null) {
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        input2 = (RemoteInput) it.next();
                        if (input2.isDataOnly()) {
                            dataOnlyInputs.add(input2);
                        } else {
                            textInputs.add(input2);
                        }
                    }
                }
                if (!dataOnlyInputs.isEmpty()) {
                    this.mExtras.putParcelableArray(str, (RemoteInput[]) dataOnlyInputs.toArray(new RemoteInput[dataOnlyInputs.size()]));
                }
                return new Action(this.mIcon, this.mTitle, this.mIntent, this.mExtras, textInputs.isEmpty() ? null : (RemoteInput[]) textInputs.toArray(new RemoteInput[textInputs.size()]), this.mAllowGeneratedReplies, this.mSemanticAction, this.mIsContextual, null);
            }
        }

        public interface Extender {
            Builder extend(Builder builder);
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface SemanticAction {
        }

        public static final class WearableExtender implements Extender {
            private static final int DEFAULT_FLAGS = 1;
            private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
            private static final int FLAG_AVAILABLE_OFFLINE = 1;
            private static final int FLAG_HINT_DISPLAY_INLINE = 4;
            private static final int FLAG_HINT_LAUNCHES_ACTIVITY = 2;
            private static final String KEY_CANCEL_LABEL = "cancelLabel";
            private static final String KEY_CONFIRM_LABEL = "confirmLabel";
            private static final String KEY_FLAGS = "flags";
            private static final String KEY_IN_PROGRESS_LABEL = "inProgressLabel";
            private CharSequence mCancelLabel;
            private CharSequence mConfirmLabel;
            private int mFlags = 1;
            private CharSequence mInProgressLabel;

            public WearableExtender(Action action) {
                Bundle wearableBundle = action.getExtras().getBundle(EXTRA_WEARABLE_EXTENSIONS);
                if (wearableBundle != null) {
                    this.mFlags = wearableBundle.getInt("flags", 1);
                    this.mInProgressLabel = wearableBundle.getCharSequence(KEY_IN_PROGRESS_LABEL);
                    this.mConfirmLabel = wearableBundle.getCharSequence(KEY_CONFIRM_LABEL);
                    this.mCancelLabel = wearableBundle.getCharSequence(KEY_CANCEL_LABEL);
                }
            }

            public Builder extend(Builder builder) {
                Bundle wearableBundle = new Bundle();
                int i = this.mFlags;
                if (i != 1) {
                    wearableBundle.putInt("flags", i);
                }
                CharSequence charSequence = this.mInProgressLabel;
                if (charSequence != null) {
                    wearableBundle.putCharSequence(KEY_IN_PROGRESS_LABEL, charSequence);
                }
                charSequence = this.mConfirmLabel;
                if (charSequence != null) {
                    wearableBundle.putCharSequence(KEY_CONFIRM_LABEL, charSequence);
                }
                charSequence = this.mCancelLabel;
                if (charSequence != null) {
                    wearableBundle.putCharSequence(KEY_CANCEL_LABEL, charSequence);
                }
                builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
                return builder;
            }

            public WearableExtender clone() {
                WearableExtender that = new WearableExtender();
                that.mFlags = this.mFlags;
                that.mInProgressLabel = this.mInProgressLabel;
                that.mConfirmLabel = this.mConfirmLabel;
                that.mCancelLabel = this.mCancelLabel;
                return that;
            }

            public WearableExtender setAvailableOffline(boolean availableOffline) {
                setFlag(1, availableOffline);
                return this;
            }

            public boolean isAvailableOffline() {
                return (this.mFlags & 1) != 0;
            }

            private void setFlag(int mask, boolean value) {
                if (value) {
                    this.mFlags |= mask;
                } else {
                    this.mFlags &= ~mask;
                }
            }

            @Deprecated
            public WearableExtender setInProgressLabel(CharSequence label) {
                this.mInProgressLabel = label;
                return this;
            }

            @Deprecated
            public CharSequence getInProgressLabel() {
                return this.mInProgressLabel;
            }

            @Deprecated
            public WearableExtender setConfirmLabel(CharSequence label) {
                this.mConfirmLabel = label;
                return this;
            }

            @Deprecated
            public CharSequence getConfirmLabel() {
                return this.mConfirmLabel;
            }

            @Deprecated
            public WearableExtender setCancelLabel(CharSequence label) {
                this.mCancelLabel = label;
                return this;
            }

            @Deprecated
            public CharSequence getCancelLabel() {
                return this.mCancelLabel;
            }

            public WearableExtender setHintLaunchesActivity(boolean hintLaunchesActivity) {
                setFlag(2, hintLaunchesActivity);
                return this;
            }

            public boolean getHintLaunchesActivity() {
                return (this.mFlags & 2) != 0;
            }

            public WearableExtender setHintDisplayActionInline(boolean hintDisplayInline) {
                setFlag(4, hintDisplayInline);
                return this;
            }

            public boolean getHintDisplayActionInline() {
                return (this.mFlags & 4) != 0;
            }
        }

        /* synthetic */ Action(Icon x0, CharSequence x1, PendingIntent x2, Bundle x3, RemoteInput[] x4, boolean x5, int x6, boolean x7, AnonymousClass1 x8) {
            this(x0, x1, x2, x3, x4, x5, x6, x7);
        }

        /* synthetic */ Action(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        private Action(Parcel in) {
            boolean z = true;
            this.mAllowGeneratedReplies = true;
            if (in.readInt() != 0) {
                this.mIcon = (Icon) Icon.CREATOR.createFromParcel(in);
                if (this.mIcon.getType() == 2) {
                    this.icon = this.mIcon.getResId();
                }
            }
            this.title = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            if (in.readInt() == 1) {
                this.actionIntent = (PendingIntent) PendingIntent.CREATOR.createFromParcel(in);
            }
            this.mExtras = Bundle.setDefusable(in.readBundle(), true);
            this.mRemoteInputs = (RemoteInput[]) in.createTypedArray(RemoteInput.CREATOR);
            this.mAllowGeneratedReplies = in.readInt() == 1;
            this.mSemanticAction = in.readInt();
            if (in.readInt() != 1) {
                z = false;
            }
            this.mIsContextual = z;
        }

        @Deprecated
        public Action(int icon, CharSequence title, PendingIntent intent) {
            this(Icon.createWithResource("", icon), title, intent, new Bundle(), null, true, 0, false);
        }

        private Action(Icon icon, CharSequence title, PendingIntent intent, Bundle extras, RemoteInput[] remoteInputs, boolean allowGeneratedReplies, int semanticAction, boolean isContextual) {
            this.mAllowGeneratedReplies = true;
            this.mIcon = icon;
            if (icon != null && icon.getType() == 2) {
                this.icon = icon.getResId();
            }
            this.title = title;
            this.actionIntent = intent;
            this.mExtras = extras != null ? extras : new Bundle();
            this.mRemoteInputs = remoteInputs;
            this.mAllowGeneratedReplies = allowGeneratedReplies;
            this.mSemanticAction = semanticAction;
            this.mIsContextual = isContextual;
        }

        public Icon getIcon() {
            if (this.mIcon == null) {
                int i = this.icon;
                if (i != 0) {
                    this.mIcon = Icon.createWithResource("", i);
                }
            }
            return this.mIcon;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public boolean getAllowGeneratedReplies() {
            return this.mAllowGeneratedReplies;
        }

        public RemoteInput[] getRemoteInputs() {
            return this.mRemoteInputs;
        }

        public int getSemanticAction() {
            return this.mSemanticAction;
        }

        public boolean isContextual() {
            return this.mIsContextual;
        }

        public RemoteInput[] getDataOnlyRemoteInputs() {
            return (RemoteInput[]) this.mExtras.getParcelableArray(EXTRA_DATA_ONLY_INPUTS);
        }

        public Action clone() {
            Icon icon = getIcon();
            CharSequence charSequence = this.title;
            PendingIntent pendingIntent = this.actionIntent;
            Bundle bundle = this.mExtras;
            return new Action(icon, charSequence, pendingIntent, bundle == null ? new Bundle() : new Bundle(bundle), getRemoteInputs(), getAllowGeneratedReplies(), getSemanticAction(), isContextual());
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            Icon ic = getIcon();
            if (ic != null) {
                out.writeInt(1);
                ic.writeToParcel(out, 0);
            } else {
                out.writeInt(0);
            }
            TextUtils.writeToParcel(this.title, out, flags);
            if (this.actionIntent != null) {
                out.writeInt(1);
                this.actionIntent.writeToParcel(out, flags);
            } else {
                out.writeInt(0);
            }
            out.writeBundle(this.mExtras);
            out.writeTypedArray(this.mRemoteInputs, flags);
            out.writeInt(this.mAllowGeneratedReplies);
            out.writeInt(this.mSemanticAction);
            out.writeInt(this.mIsContextual);
        }
    }

    public static abstract class Style {
        static final int MAX_REMOTE_INPUT_HISTORY_LINES = 3;
        private CharSequence mBigContentTitle;
        protected Builder mBuilder;
        protected CharSequence mSummaryText = null;
        protected boolean mSummaryTextSet = false;

        public abstract boolean areNotificationsVisiblyDifferent(Style style);

        /* Access modifiers changed, original: protected */
        public void internalSetBigContentTitle(CharSequence title) {
            this.mBigContentTitle = title;
        }

        /* Access modifiers changed, original: protected */
        public void internalSetSummaryText(CharSequence cs) {
            this.mSummaryText = cs;
            this.mSummaryTextSet = true;
        }

        public void setBuilder(Builder builder) {
            if (this.mBuilder != builder) {
                this.mBuilder = builder;
                Builder builder2 = this.mBuilder;
                if (builder2 != null) {
                    builder2.setStyle(this);
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void checkBuilder() {
            if (this.mBuilder == null) {
                throw new IllegalArgumentException("Style requires a valid Builder object");
            }
        }

        /* Access modifiers changed, original: protected */
        public RemoteViews getStandardView(int layoutId) {
            return getStandardView(layoutId, this.mBuilder.mParams.reset().fillTextsFrom(this.mBuilder), null);
        }

        /* Access modifiers changed, original: protected */
        public RemoteViews getStandardView(int layoutId, StandardTemplateParams p, TemplateBindResult result) {
            checkBuilder();
            CharSequence charSequence = this.mBigContentTitle;
            if (charSequence != null) {
                p.title = charSequence;
            }
            RemoteViews contentView = this.mBuilder.applyStandardTemplateWithActions(layoutId, p, result);
            CharSequence charSequence2 = this.mBigContentTitle;
            if (charSequence2 == null || !charSequence2.equals("")) {
                contentView.setViewVisibility(R.id.line1, 0);
            } else {
                contentView.setViewVisibility(R.id.line1, 8);
            }
            return contentView;
        }

        public RemoteViews makeContentView(boolean increasedHeight) {
            return null;
        }

        public RemoteViews makeBigContentView() {
            return null;
        }

        public RemoteViews makeHeadsUpContentView(boolean increasedHeight) {
            return null;
        }

        public void addExtras(Bundle extras) {
            if (this.mSummaryTextSet) {
                extras.putCharSequence(Notification.EXTRA_SUMMARY_TEXT, this.mSummaryText);
            }
            CharSequence charSequence = this.mBigContentTitle;
            if (charSequence != null) {
                extras.putCharSequence(Notification.EXTRA_TITLE_BIG, charSequence);
            }
            extras.putString(Notification.EXTRA_TEMPLATE, getClass().getName());
        }

        /* Access modifiers changed, original: protected */
        public void restoreFromExtras(Bundle extras) {
            String str = Notification.EXTRA_SUMMARY_TEXT;
            if (extras.containsKey(str)) {
                this.mSummaryText = extras.getCharSequence(str);
                this.mSummaryTextSet = true;
            }
            str = Notification.EXTRA_TITLE_BIG;
            if (extras.containsKey(str)) {
                this.mBigContentTitle = extras.getCharSequence(str);
            }
        }

        public Notification buildStyled(Notification wip) {
            addExtras(wip.extras);
            return wip;
        }

        public void purgeResources() {
        }

        public Notification build() {
            checkBuilder();
            return this.mBuilder.build();
        }

        /* Access modifiers changed, original: protected */
        public boolean hasProgress() {
            return true;
        }

        public boolean hasSummaryInHeader() {
            return true;
        }

        public boolean displayCustomViewInline() {
            return false;
        }

        public void reduceImageSizes(Context context) {
        }

        public void validate(Context context) {
        }

        public CharSequence getHeadsUpStatusBarText() {
            return null;
        }
    }

    public static class BigPictureStyle extends Style {
        public static final int MIN_ASHMEM_BITMAP_SIZE = 131072;
        private Icon mBigLargeIcon;
        private boolean mBigLargeIconSet = false;
        private Bitmap mPicture;

        @Deprecated
        public BigPictureStyle(Builder builder) {
            setBuilder(builder);
        }

        public BigPictureStyle setBigContentTitle(CharSequence title) {
            internalSetBigContentTitle(Notification.safeCharSequence(title));
            return this;
        }

        public BigPictureStyle setSummaryText(CharSequence cs) {
            internalSetSummaryText(Notification.safeCharSequence(cs));
            return this;
        }

        public Bitmap getBigPicture() {
            return this.mPicture;
        }

        public BigPictureStyle bigPicture(Bitmap b) {
            this.mPicture = b;
            return this;
        }

        public BigPictureStyle bigLargeIcon(Bitmap b) {
            return bigLargeIcon(b != null ? Icon.createWithBitmap(b) : null);
        }

        public BigPictureStyle bigLargeIcon(Icon icon) {
            this.mBigLargeIconSet = true;
            this.mBigLargeIcon = icon;
            return this;
        }

        public void purgeResources() {
            super.purgeResources();
            Bitmap bitmap = this.mPicture;
            if (bitmap != null && bitmap.isMutable() && this.mPicture.getAllocationByteCount() >= 131072) {
                this.mPicture = this.mPicture.createAshmemBitmap();
            }
            Icon icon = this.mBigLargeIcon;
            if (icon != null) {
                icon.convertToAshmem();
            }
        }

        public void reduceImageSizes(Context context) {
            int maxPictureWidth;
            super.reduceImageSizes(context);
            Resources resources = context.getResources();
            boolean isLowRam = ActivityManager.isLowRamDeviceStatic();
            if (this.mPicture != null) {
                int maxPictureHeight;
                if (isLowRam) {
                    maxPictureWidth = R.dimen.notification_big_picture_max_height_low_ram;
                } else {
                    maxPictureWidth = R.dimen.notification_big_picture_max_height;
                }
                maxPictureWidth = resources.getDimensionPixelSize(maxPictureWidth);
                if (isLowRam) {
                    maxPictureHeight = R.dimen.notification_big_picture_max_width_low_ram;
                } else {
                    maxPictureHeight = R.dimen.notification_big_picture_max_width;
                }
                this.mPicture = Icon.scaleDownIfNecessary(this.mPicture, maxPictureWidth, resources.getDimensionPixelSize(maxPictureHeight));
            }
            if (this.mBigLargeIcon != null) {
                if (isLowRam) {
                    maxPictureWidth = R.dimen.notification_right_icon_size_low_ram;
                } else {
                    maxPictureWidth = R.dimen.notification_right_icon_size;
                }
                maxPictureWidth = resources.getDimensionPixelSize(maxPictureWidth);
                this.mBigLargeIcon.scaleDownIfNecessary(maxPictureWidth, maxPictureWidth);
            }
        }

        public RemoteViews makeBigContentView() {
            Icon oldLargeIcon = null;
            Bitmap largeIconLegacy = null;
            if (this.mBigLargeIconSet) {
                oldLargeIcon = this.mBuilder.mN.mLargeIcon;
                this.mBuilder.mN.mLargeIcon = this.mBigLargeIcon;
                largeIconLegacy = this.mBuilder.mN.largeIcon;
                this.mBuilder.mN.largeIcon = null;
            }
            StandardTemplateParams p = this.mBuilder.mParams.reset().fillTextsFrom(this.mBuilder);
            RemoteViews contentView = getStandardView(this.mBuilder.getBigPictureLayoutResource(), p, null);
            if (this.mSummaryTextSet) {
                contentView.setTextViewText(R.id.text, this.mBuilder.processTextSpans(this.mBuilder.processLegacyText(this.mSummaryText)));
                this.mBuilder.setTextViewColorSecondary(contentView, R.id.text, p);
                contentView.setViewVisibility(R.id.text, 0);
            }
            this.mBuilder.setContentMinHeight(contentView, this.mBuilder.mN.hasLargeIcon());
            if (this.mBigLargeIconSet) {
                this.mBuilder.mN.mLargeIcon = oldLargeIcon;
                this.mBuilder.mN.largeIcon = largeIconLegacy;
            }
            contentView.setImageViewBitmap(R.id.big_picture, this.mPicture);
            return contentView;
        }

        public void addExtras(Bundle extras) {
            super.addExtras(extras);
            if (this.mBigLargeIconSet) {
                extras.putParcelable(Notification.EXTRA_LARGE_ICON_BIG, this.mBigLargeIcon);
            }
            extras.putParcelable(Notification.EXTRA_PICTURE, this.mPicture);
        }

        /* Access modifiers changed, original: protected */
        public void restoreFromExtras(Bundle extras) {
            super.restoreFromExtras(extras);
            String str = Notification.EXTRA_LARGE_ICON_BIG;
            if (extras.containsKey(str)) {
                this.mBigLargeIconSet = true;
                this.mBigLargeIcon = (Icon) extras.getParcelable(str);
            }
            this.mPicture = (Bitmap) extras.getParcelable(Notification.EXTRA_PICTURE);
        }

        public boolean hasSummaryInHeader() {
            return false;
        }

        public boolean areNotificationsVisiblyDifferent(Style other) {
            if (other == null || getClass() != other.getClass()) {
                return true;
            }
            return areBitmapsObviouslyDifferent(getBigPicture(), ((BigPictureStyle) other).getBigPicture());
        }

        private static boolean areBitmapsObviouslyDifferent(Bitmap a, Bitmap b) {
            boolean z = false;
            if (a == b) {
                return false;
            }
            if (a == null || b == null) {
                return true;
            }
            if (!(a.getWidth() == b.getWidth() && a.getHeight() == b.getHeight() && a.getConfig() == b.getConfig() && a.getGenerationId() == b.getGenerationId())) {
                z = true;
            }
            return z;
        }
    }

    public static class BigTextStyle extends Style {
        private CharSequence mBigText;

        @Deprecated
        public BigTextStyle(Builder builder) {
            setBuilder(builder);
        }

        public BigTextStyle setBigContentTitle(CharSequence title) {
            internalSetBigContentTitle(Notification.safeCharSequence(title));
            return this;
        }

        public BigTextStyle setSummaryText(CharSequence cs) {
            internalSetSummaryText(Notification.safeCharSequence(cs));
            return this;
        }

        public BigTextStyle bigText(CharSequence cs) {
            this.mBigText = Notification.safeCharSequence(cs);
            return this;
        }

        public CharSequence getBigText() {
            return this.mBigText;
        }

        public void addExtras(Bundle extras) {
            super.addExtras(extras);
            extras.putCharSequence(Notification.EXTRA_BIG_TEXT, this.mBigText);
        }

        /* Access modifiers changed, original: protected */
        public void restoreFromExtras(Bundle extras) {
            super.restoreFromExtras(extras);
            this.mBigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT);
        }

        public RemoteViews makeContentView(boolean increasedHeight) {
            if (!increasedHeight) {
                return super.makeContentView(increasedHeight);
            }
            this.mBuilder.mOriginalActions = this.mBuilder.mActions;
            this.mBuilder.mActions = new ArrayList();
            RemoteViews remoteViews = makeBigContentView();
            this.mBuilder.mActions = this.mBuilder.mOriginalActions;
            this.mBuilder.mOriginalActions = null;
            return remoteViews;
        }

        public RemoteViews makeHeadsUpContentView(boolean increasedHeight) {
            if (!increasedHeight || this.mBuilder.mActions.size() <= 0) {
                return super.makeHeadsUpContentView(increasedHeight);
            }
            return makeBigContentView();
        }

        public RemoteViews makeBigContentView() {
            StandardTemplateParams p = this.mBuilder.mParams.reset().fillTextsFrom(this.mBuilder).text(null);
            TemplateBindResult result = new TemplateBindResult();
            RemoteViews contentView = getStandardView(this.mBuilder.getBigTextLayoutResource(), p, result);
            contentView.setInt(R.id.big_text, "setImageEndMargin", result.getIconMarginEnd());
            CharSequence bigTextText = this.mBuilder.processLegacyText(this.mBigText);
            if (TextUtils.isEmpty(bigTextText)) {
                bigTextText = this.mBuilder.processLegacyText(this.mBuilder.getAllExtras().getCharSequence(Notification.EXTRA_TEXT));
            }
            contentView.setTextViewText(R.id.big_text, this.mBuilder.processTextSpans(bigTextText));
            this.mBuilder.setTextViewColorSecondary(contentView, R.id.big_text, p);
            contentView.setViewVisibility(R.id.big_text, TextUtils.isEmpty(bigTextText) ? 8 : 0);
            contentView.setBoolean(R.id.big_text, "setHasImage", result.isRightIconContainerVisible());
            return contentView;
        }

        public boolean areNotificationsVisiblyDifferent(Style other) {
            if (other == null || getClass() != other.getClass()) {
                return true;
            }
            return 1 ^ Objects.equals(String.valueOf(getBigText()), String.valueOf(((BigTextStyle) other).getBigText()));
        }
    }

    public static final class BubbleMetadata implements Parcelable {
        public static final Creator<BubbleMetadata> CREATOR = new Creator<BubbleMetadata>() {
            public BubbleMetadata createFromParcel(Parcel source) {
                return new BubbleMetadata(source, null);
            }

            public BubbleMetadata[] newArray(int size) {
                return new BubbleMetadata[size];
            }
        };
        private static final int FLAG_AUTO_EXPAND_BUBBLE = 1;
        private static final int FLAG_SUPPRESS_NOTIFICATION = 2;
        private PendingIntent mDeleteIntent;
        private int mDesiredHeight;
        private int mDesiredHeightResId;
        private int mFlags;
        private Icon mIcon;
        private PendingIntent mPendingIntent;

        public static final class Builder {
            private PendingIntent mDeleteIntent;
            private int mDesiredHeight;
            private int mDesiredHeightResId;
            private int mFlags;
            private Icon mIcon;
            private PendingIntent mPendingIntent;

            public Builder setIntent(PendingIntent intent) {
                if (intent != null) {
                    this.mPendingIntent = intent;
                    return this;
                }
                throw new IllegalArgumentException("Bubble requires non-null pending intent");
            }

            public Builder setIcon(Icon icon) {
                if (icon == null) {
                    throw new IllegalArgumentException("Bubbles require non-null icon");
                } else if (icon.getType() != 1) {
                    this.mIcon = icon;
                    return this;
                } else {
                    throw new IllegalArgumentException("When using bitmap based icons, Bubbles require TYPE_ADAPTIVE_BITMAP, please use Icon#createWithAdaptiveBitmap instead");
                }
            }

            public Builder setDesiredHeight(int height) {
                this.mDesiredHeight = Math.max(height, 0);
                this.mDesiredHeightResId = 0;
                return this;
            }

            public Builder setDesiredHeightResId(int heightResId) {
                this.mDesiredHeightResId = heightResId;
                this.mDesiredHeight = 0;
                return this;
            }

            public Builder setAutoExpandBubble(boolean shouldExpand) {
                setFlag(1, shouldExpand);
                return this;
            }

            public Builder setSuppressNotification(boolean shouldSuppressNotif) {
                setFlag(2, shouldSuppressNotif);
                return this;
            }

            public Builder setDeleteIntent(PendingIntent deleteIntent) {
                this.mDeleteIntent = deleteIntent;
                return this;
            }

            public BubbleMetadata build() {
                PendingIntent pendingIntent = this.mPendingIntent;
                if (pendingIntent != null) {
                    Icon icon = this.mIcon;
                    if (icon != null) {
                        BubbleMetadata data = new BubbleMetadata(pendingIntent, this.mDeleteIntent, icon, this.mDesiredHeight, this.mDesiredHeightResId, null);
                        data.setFlags(this.mFlags);
                        return data;
                    }
                    throw new IllegalStateException("Must supply an icon for the bubble");
                }
                throw new IllegalStateException("Must supply pending intent to bubble");
            }

            public Builder setFlag(int mask, boolean value) {
                if (value) {
                    this.mFlags |= mask;
                } else {
                    this.mFlags &= ~mask;
                }
                return this;
            }
        }

        /* synthetic */ BubbleMetadata(PendingIntent x0, PendingIntent x1, Icon x2, int x3, int x4, AnonymousClass1 x5) {
            this(x0, x1, x2, x3, x4);
        }

        /* synthetic */ BubbleMetadata(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        private BubbleMetadata(PendingIntent expandIntent, PendingIntent deleteIntent, Icon icon, int height, int heightResId) {
            this.mPendingIntent = expandIntent;
            this.mIcon = icon;
            this.mDesiredHeight = height;
            this.mDesiredHeightResId = heightResId;
            this.mDeleteIntent = deleteIntent;
        }

        private BubbleMetadata(Parcel in) {
            this.mPendingIntent = (PendingIntent) PendingIntent.CREATOR.createFromParcel(in);
            this.mIcon = (Icon) Icon.CREATOR.createFromParcel(in);
            this.mDesiredHeight = in.readInt();
            this.mFlags = in.readInt();
            if (in.readInt() != 0) {
                this.mDeleteIntent = (PendingIntent) PendingIntent.CREATOR.createFromParcel(in);
            }
            this.mDesiredHeightResId = in.readInt();
        }

        public PendingIntent getIntent() {
            return this.mPendingIntent;
        }

        public PendingIntent getDeleteIntent() {
            return this.mDeleteIntent;
        }

        public Icon getIcon() {
            return this.mIcon;
        }

        public int getDesiredHeight() {
            return this.mDesiredHeight;
        }

        public int getDesiredHeightResId() {
            return this.mDesiredHeightResId;
        }

        public boolean getAutoExpandBubble() {
            return (this.mFlags & 1) != 0;
        }

        public boolean isNotificationSuppressed() {
            return (this.mFlags & 2) != 0;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            this.mPendingIntent.writeToParcel(out, 0);
            this.mIcon.writeToParcel(out, 0);
            out.writeInt(this.mDesiredHeight);
            out.writeInt(this.mFlags);
            out.writeInt(this.mDeleteIntent != null ? 1 : 0);
            PendingIntent pendingIntent = this.mDeleteIntent;
            if (pendingIntent != null) {
                pendingIntent.writeToParcel(out, 0);
            }
            out.writeInt(this.mDesiredHeightResId);
        }

        private void setFlags(int flags) {
            this.mFlags = flags;
        }
    }

    public static class Builder {
        public static final String EXTRA_REBUILD_BIG_CONTENT_VIEW_ACTION_COUNT = "android.rebuild.bigViewActionCount";
        public static final String EXTRA_REBUILD_CONTENT_VIEW_ACTION_COUNT = "android.rebuild.contentViewActionCount";
        public static final String EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW_ACTION_COUNT = "android.rebuild.hudViewActionCount";
        private static final int LIGHTNESS_TEXT_DIFFERENCE_DARK = -10;
        private static final int LIGHTNESS_TEXT_DIFFERENCE_LIGHT = 20;
        private static final boolean USE_ONLY_TITLE_IN_LOW_PRIORITY_SUMMARY = SystemProperties.getBoolean("notifications.only_title", true);
        @UnsupportedAppUsage
        private ArrayList<Action> mActions;
        private int mBackgroundColor;
        private int mCachedAmbientColor;
        private int mCachedAmbientColorIsFor;
        private int mCachedContrastColor;
        private int mCachedContrastColorIsFor;
        private ContrastColorUtil mColorUtil;
        private Context mContext;
        private int mForegroundColor;
        private boolean mInNightMode;
        private boolean mIsLegacy;
        private boolean mIsLegacyInitialized;
        private Notification mN;
        private int mNeutralColor;
        private ArrayList<Action> mOriginalActions;
        StandardTemplateParams mParams;
        private ArrayList<Person> mPersonList;
        private int mPrimaryTextColor;
        private boolean mRebuildStyledRemoteViews;
        private int mSecondaryTextColor;
        private Style mStyle;
        private int mTextColorsAreForBackground;
        private boolean mTintActionButtons;
        private Bundle mUserExtras;

        public Builder(Context context, String channelId) {
            this(context, (Notification) null);
            this.mN.mChannelId = channelId;
        }

        @Deprecated
        public Builder(Context context) {
            this(context, (Notification) null);
        }

        public Builder(Context context, Notification toAdopt) {
            this.mUserExtras = new Bundle();
            this.mActions = new ArrayList(3);
            this.mPersonList = new ArrayList();
            this.mCachedContrastColor = 1;
            this.mCachedContrastColorIsFor = 1;
            this.mCachedAmbientColor = 1;
            this.mCachedAmbientColorIsFor = 1;
            this.mNeutralColor = 1;
            this.mParams = new StandardTemplateParams();
            this.mTextColorsAreForBackground = 1;
            this.mPrimaryTextColor = 1;
            this.mSecondaryTextColor = 1;
            this.mBackgroundColor = 1;
            this.mForegroundColor = 1;
            this.mContext = context;
            Resources res = this.mContext.getResources();
            this.mTintActionButtons = res.getBoolean(R.bool.config_tintNotificationActionButtons);
            if (res.getBoolean(R.bool.config_enableNightMode)) {
                this.mInNightMode = (res.getConfiguration().uiMode & 48) == 32;
            }
            if (toAdopt == null) {
                this.mN = new Notification();
                if (context.getApplicationInfo().targetSdkVersion < 24) {
                    this.mN.extras.putBoolean(Notification.EXTRA_SHOW_WHEN, true);
                }
                Notification notification = this.mN;
                notification.priority = 0;
                notification.visibility = 0;
                return;
            }
            this.mN = toAdopt;
            if (this.mN.actions != null) {
                Collections.addAll(this.mActions, this.mN.actions);
            }
            Bundle bundle = this.mN.extras;
            String str = Notification.EXTRA_PEOPLE_LIST;
            if (bundle.containsKey(str)) {
                this.mPersonList.addAll(this.mN.extras.getParcelableArrayList(str));
            }
            if (this.mN.getSmallIcon() == null && this.mN.icon != 0) {
                setSmallIcon(this.mN.icon);
            }
            if (this.mN.getLargeIcon() == null && this.mN.largeIcon != null) {
                setLargeIcon(this.mN.largeIcon);
            }
            String templateClass = this.mN.extras.getString(Notification.EXTRA_TEMPLATE);
            if (!TextUtils.isEmpty(templateClass)) {
                Class<? extends Style> styleClass = Notification.getNotificationStyleClass(templateClass);
                String str2 = Notification.TAG;
                if (styleClass == null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown style class: ");
                    stringBuilder.append(templateClass);
                    Log.d(str2, stringBuilder.toString());
                    return;
                }
                try {
                    Constructor<? extends Style> ctor = styleClass.getDeclaredConstructor(new Class[0]);
                    ctor.setAccessible(true);
                    Style style = (Style) ctor.newInstance(new Object[0]);
                    style.restoreFromExtras(this.mN.extras);
                    setStyle(style);
                } catch (Throwable t) {
                    Log.e(str2, "Could not create Style", t);
                }
            }
        }

        private ContrastColorUtil getColorUtil() {
            if (this.mColorUtil == null) {
                this.mColorUtil = ContrastColorUtil.getInstance(this.mContext);
            }
            return this.mColorUtil;
        }

        public Builder setShortcutId(String shortcutId) {
            this.mN.mShortcutId = shortcutId;
            return this;
        }

        public Builder setLocusId(LocusId locusId) {
            this.mN.mLocusId = locusId;
            return this;
        }

        public Builder setBadgeIconType(int icon) {
            this.mN.mBadgeIcon = icon;
            return this;
        }

        public Builder setGroupAlertBehavior(int groupAlertBehavior) {
            this.mN.mGroupAlertBehavior = groupAlertBehavior;
            return this;
        }

        public Builder setBubbleMetadata(BubbleMetadata data) {
            this.mN.mBubbleMetadata = data;
            return this;
        }

        @Deprecated
        public Builder setChannel(String channelId) {
            this.mN.mChannelId = channelId;
            return this;
        }

        public Builder setChannelId(String channelId) {
            this.mN.mChannelId = channelId;
            return this;
        }

        @Deprecated
        public Builder setTimeout(long durationMs) {
            this.mN.mTimeout = durationMs;
            return this;
        }

        public Builder setTimeoutAfter(long durationMs) {
            this.mN.mTimeout = durationMs;
            return this;
        }

        public Builder setWhen(long when) {
            this.mN.when = when;
            return this;
        }

        public Builder setShowWhen(boolean show) {
            this.mN.extras.putBoolean(Notification.EXTRA_SHOW_WHEN, show);
            return this;
        }

        public Builder setUsesChronometer(boolean b) {
            this.mN.extras.putBoolean(Notification.EXTRA_SHOW_CHRONOMETER, b);
            return this;
        }

        public Builder setChronometerCountDown(boolean countDown) {
            this.mN.extras.putBoolean(Notification.EXTRA_CHRONOMETER_COUNT_DOWN, countDown);
            return this;
        }

        public Builder setSmallIcon(int icon) {
            Icon createWithResource;
            if (icon != 0) {
                createWithResource = Icon.createWithResource(this.mContext, icon);
            } else {
                createWithResource = null;
            }
            return setSmallIcon(createWithResource);
        }

        public Builder setSmallIcon(int icon, int level) {
            this.mN.iconLevel = level;
            return setSmallIcon(icon);
        }

        public Builder setSmallIcon(Icon icon) {
            this.mN.setSmallIcon(icon);
            if (icon != null && icon.getType() == 2) {
                this.mN.icon = icon.getResId();
            }
            return this;
        }

        public Builder setContentTitle(CharSequence title) {
            this.mN.extras.putCharSequence(Notification.EXTRA_TITLE, Notification.safeCharSequence(title));
            return this;
        }

        public Builder setContentText(CharSequence text) {
            this.mN.extras.putCharSequence(Notification.EXTRA_TEXT, Notification.safeCharSequence(text));
            return this;
        }

        public Builder setSubText(CharSequence text) {
            this.mN.extras.putCharSequence(Notification.EXTRA_SUB_TEXT, Notification.safeCharSequence(text));
            return this;
        }

        public Builder setSettingsText(CharSequence text) {
            this.mN.mSettingsText = Notification.safeCharSequence(text);
            return this;
        }

        public Builder setRemoteInputHistory(CharSequence[] text) {
            String str = Notification.EXTRA_REMOTE_INPUT_HISTORY;
            if (text == null) {
                this.mN.extras.putCharSequenceArray(str, null);
            } else {
                int N = Math.min(5, text.length);
                CharSequence[] safe = new CharSequence[N];
                for (int i = 0; i < N; i++) {
                    safe[i] = Notification.safeCharSequence(text[i]);
                }
                this.mN.extras.putCharSequenceArray(str, safe);
            }
            return this;
        }

        public Builder setShowRemoteInputSpinner(boolean showSpinner) {
            this.mN.extras.putBoolean(Notification.EXTRA_SHOW_REMOTE_INPUT_SPINNER, showSpinner);
            return this;
        }

        public Builder setHideSmartReplies(boolean hideSmartReplies) {
            this.mN.extras.putBoolean(Notification.EXTRA_HIDE_SMART_REPLIES, hideSmartReplies);
            return this;
        }

        public Builder setNumber(int number) {
            this.mN.number = number;
            return this;
        }

        @Deprecated
        public Builder setContentInfo(CharSequence info) {
            this.mN.extras.putCharSequence(Notification.EXTRA_INFO_TEXT, Notification.safeCharSequence(info));
            return this;
        }

        public Builder setProgress(int max, int progress, boolean indeterminate) {
            this.mN.extras.putInt(Notification.EXTRA_PROGRESS, progress);
            this.mN.extras.putInt(Notification.EXTRA_PROGRESS_MAX, max);
            this.mN.extras.putBoolean(Notification.EXTRA_PROGRESS_INDETERMINATE, indeterminate);
            return this;
        }

        @Deprecated
        public Builder setContent(RemoteViews views) {
            return setCustomContentView(views);
        }

        public Builder setCustomContentView(RemoteViews contentView) {
            this.mN.contentView = contentView;
            return this;
        }

        public Builder setCustomBigContentView(RemoteViews contentView) {
            this.mN.bigContentView = contentView;
            return this;
        }

        public Builder setCustomHeadsUpContentView(RemoteViews contentView) {
            this.mN.headsUpContentView = contentView;
            return this;
        }

        public Builder setContentIntent(PendingIntent intent) {
            this.mN.contentIntent = intent;
            return this;
        }

        public Builder setDeleteIntent(PendingIntent intent) {
            this.mN.deleteIntent = intent;
            return this;
        }

        public Builder setFullScreenIntent(PendingIntent intent, boolean highPriority) {
            this.mN.fullScreenIntent = intent;
            setFlag(128, highPriority);
            return this;
        }

        public Builder setTicker(CharSequence tickerText) {
            this.mN.tickerText = Notification.safeCharSequence(tickerText);
            return this;
        }

        @Deprecated
        public Builder setTicker(CharSequence tickerText, RemoteViews views) {
            setTicker(tickerText);
            return this;
        }

        public Builder setLargeIcon(Bitmap b) {
            return setLargeIcon(b != null ? Icon.createWithBitmap(b) : null);
        }

        public Builder setLargeIcon(Icon icon) {
            this.mN.mLargeIcon = icon;
            this.mN.extras.putParcelable(Notification.EXTRA_LARGE_ICON, icon);
            return this;
        }

        @Deprecated
        public Builder setSound(Uri sound) {
            Notification notification = this.mN;
            notification.sound = sound;
            notification.audioAttributes = Notification.AUDIO_ATTRIBUTES_DEFAULT;
            return this;
        }

        @Deprecated
        public Builder setSound(Uri sound, int streamType) {
            PlayerBase.deprecateStreamTypeForPlayback(streamType, Notification.TAG, "setSound()");
            Notification notification = this.mN;
            notification.sound = sound;
            notification.audioStreamType = streamType;
            return this;
        }

        @Deprecated
        public Builder setSound(Uri sound, AudioAttributes audioAttributes) {
            Notification notification = this.mN;
            notification.sound = sound;
            notification.audioAttributes = audioAttributes;
            return this;
        }

        @Deprecated
        public Builder setVibrate(long[] pattern) {
            this.mN.vibrate = pattern;
            return this;
        }

        @Deprecated
        public Builder setLights(int argb, int onMs, int offMs) {
            Notification notification = this.mN;
            notification.ledARGB = argb;
            notification.ledOnMS = onMs;
            notification.ledOffMS = offMs;
            if (!(onMs == 0 && offMs == 0)) {
                notification = this.mN;
                notification.flags |= 1;
            }
            return this;
        }

        public Builder setOngoing(boolean ongoing) {
            setFlag(2, ongoing);
            return this;
        }

        public Builder setColorized(boolean colorize) {
            this.mN.extras.putBoolean(Notification.EXTRA_COLORIZED, colorize);
            return this;
        }

        public Builder setOnlyAlertOnce(boolean onlyAlertOnce) {
            setFlag(8, onlyAlertOnce);
            return this;
        }

        public Builder setAutoCancel(boolean autoCancel) {
            setFlag(16, autoCancel);
            return this;
        }

        public Builder setLocalOnly(boolean localOnly) {
            setFlag(256, localOnly);
            return this;
        }

        @Deprecated
        public Builder setDefaults(int defaults) {
            this.mN.defaults = defaults;
            return this;
        }

        @Deprecated
        public Builder setPriority(int pri) {
            this.mN.priority = pri;
            return this;
        }

        public Builder setCategory(String category) {
            this.mN.category = category;
            return this;
        }

        public Builder addPerson(String uri) {
            addPerson(new android.app.Person.Builder().setUri(uri).build());
            return this;
        }

        public Builder addPerson(Person person) {
            this.mPersonList.add(person);
            return this;
        }

        public Builder setGroup(String groupKey) {
            this.mN.mGroupKey = groupKey;
            return this;
        }

        public Builder setGroupSummary(boolean isGroupSummary) {
            setFlag(512, isGroupSummary);
            return this;
        }

        public Builder setSortKey(String sortKey) {
            this.mN.mSortKey = sortKey;
            return this;
        }

        public Builder addExtras(Bundle extras) {
            if (extras != null) {
                this.mUserExtras.putAll(extras);
            }
            return this;
        }

        public Builder setExtras(Bundle extras) {
            if (extras != null) {
                this.mUserExtras = extras;
            }
            return this;
        }

        public Bundle getExtras() {
            return this.mUserExtras;
        }

        private Bundle getAllExtras() {
            Bundle saveExtras = (Bundle) this.mUserExtras.clone();
            saveExtras.putAll(this.mN.extras);
            return saveExtras;
        }

        @Deprecated
        public Builder addAction(int icon, CharSequence title, PendingIntent intent) {
            this.mActions.add(new Action(icon, Notification.safeCharSequence(title), intent));
            return this;
        }

        public Builder addAction(Action action) {
            if (action != null) {
                this.mActions.add(action);
            }
            return this;
        }

        public Builder setActions(Action... actions) {
            this.mActions.clear();
            for (int i = 0; i < actions.length; i++) {
                if (actions[i] != null) {
                    this.mActions.add(actions[i]);
                }
            }
            return this;
        }

        public Builder setStyle(Style style) {
            if (this.mStyle != style) {
                this.mStyle = style;
                Style style2 = this.mStyle;
                String str = Notification.EXTRA_TEMPLATE;
                if (style2 != null) {
                    style2.setBuilder(this);
                    this.mN.extras.putString(str, style.getClass().getName());
                } else {
                    this.mN.extras.remove(str);
                }
            }
            return this;
        }

        public Style getStyle() {
            return this.mStyle;
        }

        public Builder setVisibility(int visibility) {
            this.mN.visibility = visibility;
            return this;
        }

        public Builder setPublicVersion(Notification n) {
            if (n != null) {
                this.mN.publicVersion = new Notification();
                n.cloneInto(this.mN.publicVersion, true);
            } else {
                this.mN.publicVersion = null;
            }
            return this;
        }

        public Builder extend(Extender extender) {
            extender.extend(this);
            return this;
        }

        public Builder setFlag(int mask, boolean value) {
            Notification notification;
            if (value) {
                notification = this.mN;
                notification.flags |= mask;
            } else {
                notification = this.mN;
                notification.flags &= ~mask;
            }
            return this;
        }

        public Builder setColor(int argb) {
            this.mN.color = argb;
            sanitizeColor();
            return this;
        }

        private Drawable getProfileBadgeDrawable() {
            if (this.mContext.getUserId() == 0) {
                return null;
            }
            return this.mContext.getPackageManager().getUserBadgeForDensityNoBackground(new UserHandle(this.mContext.getUserId()), 0);
        }

        private Bitmap getProfileBadge() {
            Drawable badge = getProfileBadgeDrawable();
            if (badge == null) {
                return null;
            }
            int size = this.mContext.getResources().getDimensionPixelSize(R.dimen.notification_badge_size);
            Bitmap bitmap = Bitmap.createBitmap(size, size, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            badge.setBounds(0, 0, size, size);
            badge.draw(canvas);
            return bitmap;
        }

        private void bindProfileBadge(RemoteViews contentView, StandardTemplateParams p) {
            Bitmap profileBadge = getProfileBadge();
            if (profileBadge != null) {
                contentView.setImageViewBitmap(R.id.profile_badge, profileBadge);
                contentView.setViewVisibility(R.id.profile_badge, 0);
                if (isColorized(p)) {
                    contentView.setDrawableTint(R.id.profile_badge, false, getPrimaryTextColor(p), Mode.SRC_ATOP);
                }
            }
        }

        private void bindAlertedIcon(RemoteViews contentView, StandardTemplateParams p) {
            contentView.setDrawableTint(R.id.alerted_icon, false, getNeutralColor(p), Mode.SRC_ATOP);
        }

        public boolean usesStandardHeader() {
            boolean z = true;
            if (this.mN.mUsesStandardHeader) {
                return true;
            }
            if (this.mContext.getApplicationInfo().targetSdkVersion >= 24 && this.mN.contentView == null && this.mN.bigContentView == null) {
                return true;
            }
            boolean contentViewUsesHeader = this.mN.contentView == null || Notification.STANDARD_LAYOUTS.contains(Integer.valueOf(this.mN.contentView.getLayoutId()));
            boolean bigContentViewUsesHeader = this.mN.bigContentView == null || Notification.STANDARD_LAYOUTS.contains(Integer.valueOf(this.mN.bigContentView.getLayoutId()));
            if (!(contentViewUsesHeader && bigContentViewUsesHeader)) {
                z = false;
            }
            return z;
        }

        private void resetStandardTemplate(RemoteViews contentView) {
            resetNotificationHeader(contentView);
            contentView.setViewVisibility(R.id.right_icon, 8);
            contentView.setViewVisibility(16908310, 8);
            contentView.setTextViewText(16908310, null);
            contentView.setViewVisibility(R.id.text, 8);
            contentView.setTextViewText(R.id.text, null);
            contentView.setViewVisibility(R.id.text_line_1, 8);
            contentView.setTextViewText(R.id.text_line_1, null);
        }

        private void resetNotificationHeader(RemoteViews contentView) {
            contentView.setBoolean(R.id.notification_header, "setExpanded", false);
            contentView.setTextViewText(R.id.app_name_text, null);
            contentView.setViewVisibility(R.id.chronometer, 8);
            contentView.setViewVisibility(R.id.header_text, 8);
            contentView.setTextViewText(R.id.header_text, null);
            contentView.setViewVisibility(R.id.header_text_secondary, 8);
            contentView.setTextViewText(R.id.header_text_secondary, null);
            contentView.setViewVisibility(R.id.header_text_divider, 8);
            contentView.setViewVisibility(R.id.header_text_secondary_divider, 8);
            contentView.setViewVisibility(R.id.time_divider, 8);
            contentView.setViewVisibility(R.id.time, 8);
            contentView.setImageViewIcon(R.id.profile_badge, null);
            contentView.setViewVisibility(R.id.profile_badge, 8);
            contentView.setViewVisibility(R.id.alerted_icon, 8);
            this.mN.mUsesStandardHeader = false;
        }

        private RemoteViews applyStandardTemplate(int resId, TemplateBindResult result) {
            return applyStandardTemplate(resId, this.mParams.reset().fillTextsFrom(this), result);
        }

        private RemoteViews applyStandardTemplate(int resId, StandardTemplateParams p, TemplateBindResult result) {
            RemoteViews contentView = new BuilderRemoteViews(this.mContext.getApplicationInfo(), resId);
            resetStandardTemplate(contentView);
            Bundle ex = this.mN.extras;
            updateBackgroundColor(contentView, p);
            bindNotificationHeader(contentView, p);
            bindLargeIconAndReply(contentView, p, result);
            boolean showProgress = handleProgressBar(contentView, ex, p);
            boolean z = false;
            if (p.title != null) {
                int i;
                contentView.setViewVisibility(16908310, 0);
                contentView.setTextViewText(16908310, processTextSpans(p.title));
                setTextViewColorPrimary(contentView, 16908310, p);
                if (showProgress) {
                    i = -2;
                } else {
                    i = -1;
                }
                contentView.setViewLayoutWidth(16908310, i);
            }
            if (p.text != null) {
                int textId;
                if (showProgress) {
                    textId = R.id.text_line_1;
                } else {
                    textId = R.id.text;
                }
                contentView.setTextViewText(textId, processTextSpans(p.text));
                setTextViewColorSecondary(contentView, textId, p);
                contentView.setViewVisibility(textId, 0);
            }
            if (showProgress || this.mN.hasLargeIcon()) {
                z = true;
            }
            setContentMinHeight(contentView, z);
            return contentView;
        }

        private CharSequence processTextSpans(CharSequence text) {
            if (hasForegroundColor() || this.mInNightMode) {
                return ContrastColorUtil.clearColorSpans(text);
            }
            return text;
        }

        private void setTextViewColorPrimary(RemoteViews contentView, int id, StandardTemplateParams p) {
            ensureColors(p);
            contentView.setTextColor(id, this.mPrimaryTextColor);
        }

        private boolean hasForegroundColor() {
            return this.mForegroundColor != 1;
        }

        @VisibleForTesting
        public int getPrimaryTextColor() {
            return getPrimaryTextColor(this.mParams);
        }

        @VisibleForTesting
        public int getPrimaryTextColor(StandardTemplateParams p) {
            ensureColors(p);
            return this.mPrimaryTextColor;
        }

        @VisibleForTesting
        public int getSecondaryTextColor() {
            return getSecondaryTextColor(this.mParams);
        }

        @VisibleForTesting
        public int getSecondaryTextColor(StandardTemplateParams p) {
            ensureColors(p);
            return this.mSecondaryTextColor;
        }

        private void setTextViewColorSecondary(RemoteViews contentView, int id, StandardTemplateParams p) {
            ensureColors(p);
            contentView.setTextColor(id, this.mSecondaryTextColor);
        }

        private void ensureColors(StandardTemplateParams p) {
            int backgroundColor = getBackgroundColor(p);
            if (this.mPrimaryTextColor == 1 || this.mSecondaryTextColor == 1 || this.mTextColorsAreForBackground != backgroundColor) {
                this.mTextColorsAreForBackground = backgroundColor;
                if (hasForegroundColor() && isColorized(p)) {
                    double backLum = ContrastColorUtil.calculateLuminance(backgroundColor);
                    double textLum = ContrastColorUtil.calculateLuminance(this.mForegroundColor);
                    double contrast = ContrastColorUtil.calculateContrast(this.mForegroundColor, backgroundColor);
                    boolean backgroundLight = (backLum > textLum && ContrastColorUtil.satisfiesTextContrast(backgroundColor, -16777216)) || (backLum <= textLum && !ContrastColorUtil.satisfiesTextContrast(backgroundColor, -1));
                    int i = 10;
                    if (contrast >= 4.5d) {
                        int i2;
                        this.mPrimaryTextColor = this.mForegroundColor;
                        int i3 = this.mPrimaryTextColor;
                        if (backgroundLight) {
                            i2 = 20;
                        } else {
                            i2 = -10;
                        }
                        this.mSecondaryTextColor = ContrastColorUtil.changeColorLightness(i3, i2);
                        if (ContrastColorUtil.calculateContrast(this.mSecondaryTextColor, backgroundColor) < 4.5d) {
                            if (backgroundLight) {
                                this.mSecondaryTextColor = ContrastColorUtil.findContrastColor(this.mSecondaryTextColor, backgroundColor, true, 4.5d);
                            } else {
                                this.mSecondaryTextColor = ContrastColorUtil.findContrastColorAgainstDark(this.mSecondaryTextColor, backgroundColor, true, 4.5d);
                            }
                            int i4 = this.mSecondaryTextColor;
                            if (backgroundLight) {
                                i = -20;
                            }
                            this.mPrimaryTextColor = ContrastColorUtil.changeColorLightness(i4, i);
                            return;
                        }
                        return;
                    } else if (backgroundLight) {
                        this.mSecondaryTextColor = ContrastColorUtil.findContrastColor(this.mForegroundColor, backgroundColor, true, 4.5d);
                        this.mPrimaryTextColor = ContrastColorUtil.changeColorLightness(this.mSecondaryTextColor, -20);
                        return;
                    } else {
                        this.mSecondaryTextColor = ContrastColorUtil.findContrastColorAgainstDark(this.mForegroundColor, backgroundColor, true, 4.5d);
                        this.mPrimaryTextColor = ContrastColorUtil.changeColorLightness(this.mSecondaryTextColor, 10);
                        return;
                    }
                }
                this.mPrimaryTextColor = ContrastColorUtil.resolvePrimaryColor(this.mContext, backgroundColor, this.mInNightMode);
                this.mSecondaryTextColor = ContrastColorUtil.resolveSecondaryColor(this.mContext, backgroundColor, this.mInNightMode);
                if (backgroundColor != 0 && isColorized(p)) {
                    this.mPrimaryTextColor = ContrastColorUtil.findAlphaToMeetContrast(this.mPrimaryTextColor, backgroundColor, 4.5d);
                    this.mSecondaryTextColor = ContrastColorUtil.findAlphaToMeetContrast(this.mSecondaryTextColor, backgroundColor, 4.5d);
                }
            }
        }

        private void updateBackgroundColor(RemoteViews contentView, StandardTemplateParams p) {
            if (isColorized(p)) {
                contentView.setInt(R.id.status_bar_latest_event_content, "setBackgroundColor", getBackgroundColor(p));
                return;
            }
            contentView.setInt(R.id.status_bar_latest_event_content, "setBackgroundResource", 0);
        }

        /* Access modifiers changed, original: 0000 */
        public void setContentMinHeight(RemoteViews remoteView, boolean hasMinHeight) {
            int minHeight = 0;
            if (hasMinHeight) {
                minHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.notification_min_content_height);
            }
            remoteView.setInt(R.id.notification_main_column, "setMinimumHeight", minHeight);
        }

        private boolean handleProgressBar(RemoteViews contentView, Bundle ex, StandardTemplateParams p) {
            int max = ex.getInt(Notification.EXTRA_PROGRESS_MAX, 0);
            int progress = ex.getInt(Notification.EXTRA_PROGRESS, 0);
            boolean ind = ex.getBoolean(Notification.EXTRA_PROGRESS_INDETERMINATE);
            if (!p.hasProgress || (max == 0 && !ind)) {
                contentView.setViewVisibility(16908301, 8);
                return false;
            }
            contentView.setViewVisibility(16908301, 0);
            contentView.setProgressBar(16908301, max, progress, ind);
            contentView.setProgressBackgroundTintList(16908301, ColorStateList.valueOf(this.mContext.getColor(R.color.notification_progress_background_color)));
            if (getRawColor(p) != 0) {
                ColorStateList colorStateList = ColorStateList.valueOf(isColorized(p) ? getPrimaryTextColor(p) : resolveContrastColor(p));
                contentView.setProgressTintList(16908301, colorStateList);
                contentView.setProgressIndeterminateTintList(16908301, colorStateList);
            }
            return true;
        }

        private void bindLargeIconAndReply(RemoteViews contentView, StandardTemplateParams p, TemplateBindResult result) {
            boolean largeIconShown = bindLargeIcon(contentView, p);
            boolean replyIconShown = bindReplyIcon(contentView, p);
            int i = 0;
            boolean iconContainerVisible = largeIconShown || replyIconShown;
            if (!iconContainerVisible) {
                i = 8;
            }
            contentView.setViewVisibility(R.id.right_icon_container, i);
            i = calculateMarginEnd(largeIconShown, replyIconShown);
            contentView.setViewLayoutMarginEnd(R.id.line1, i);
            contentView.setViewLayoutMarginEnd(R.id.text, i);
            contentView.setViewLayoutMarginEnd(16908301, i);
            if (result != null) {
                result.setIconMarginEnd(i);
                result.setRightIconContainerVisible(iconContainerVisible);
            }
        }

        private int calculateMarginEnd(boolean largeIconShown, boolean replyIconShown) {
            int marginEnd = 0;
            int contentMargin = this.mContext.getResources().getDimensionPixelSize(R.dimen.notification_content_margin_end);
            int iconSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.notification_right_icon_size);
            if (replyIconShown) {
                marginEnd = (0 + iconSize) - (this.mContext.getResources().getDimensionPixelSize(R.dimen.notification_reply_inset) * 2);
            }
            if (largeIconShown) {
                marginEnd += iconSize;
                if (replyIconShown) {
                    marginEnd += contentMargin;
                }
            }
            if (replyIconShown || largeIconShown) {
                return marginEnd + contentMargin;
            }
            return marginEnd;
        }

        private boolean bindLargeIcon(RemoteViews contentView, StandardTemplateParams p) {
            if (this.mN.mLargeIcon == null && this.mN.largeIcon != null) {
                Notification notification = this.mN;
                notification.mLargeIcon = Icon.createWithBitmap(notification.largeIcon);
            }
            boolean showLargeIcon = (this.mN.mLargeIcon == null || p.hideLargeIcon) ? false : true;
            if (showLargeIcon) {
                contentView.setViewVisibility(R.id.right_icon, 0);
                contentView.setImageViewIcon(R.id.right_icon, this.mN.mLargeIcon);
                processLargeLegacyIcon(this.mN.mLargeIcon, contentView, p);
            }
            return showLargeIcon;
        }

        private boolean bindReplyIcon(RemoteViews contentView, StandardTemplateParams p) {
            boolean z = true;
            boolean actionVisible = p.hideReplyIcon ^ true;
            Action action = null;
            int i = 0;
            if (actionVisible) {
                action = findReplyAction();
                if (action == null) {
                    z = false;
                }
                actionVisible = z;
            }
            if (actionVisible) {
                contentView.setViewVisibility(R.id.reply_icon_action, 0);
                contentView.setDrawableTint(R.id.reply_icon_action, false, getNeutralColor(p), Mode.SRC_ATOP);
                contentView.setOnClickPendingIntent(R.id.reply_icon_action, action.actionIntent);
                contentView.setRemoteInputs(R.id.reply_icon_action, action.mRemoteInputs);
            } else {
                contentView.setRemoteInputs(R.id.reply_icon_action, null);
            }
            if (!actionVisible) {
                i = 8;
            }
            contentView.setViewVisibility(R.id.reply_icon_action, i);
            return actionVisible;
        }

        private Action findReplyAction() {
            ArrayList<Action> actions = this.mActions;
            if (this.mOriginalActions != null) {
                actions = this.mOriginalActions;
            }
            int numActions = actions.size();
            for (int i = 0; i < numActions; i++) {
                Action action = (Action) actions.get(i);
                if (hasValidRemoteInput(action)) {
                    return action;
                }
            }
            return null;
        }

        private void bindNotificationHeader(RemoteViews contentView, StandardTemplateParams p) {
            bindSmallIcon(contentView, p);
            bindHeaderAppName(contentView, p);
            bindHeaderText(contentView, p);
            bindHeaderTextSecondary(contentView, p);
            bindHeaderChronometerAndTime(contentView, p);
            bindProfileBadge(contentView, p);
            bindAlertedIcon(contentView, p);
            bindActivePermissions(contentView, p);
            bindExpandButton(contentView, p);
            this.mN.mUsesStandardHeader = true;
        }

        private void bindActivePermissions(RemoteViews contentView, StandardTemplateParams p) {
            int color = getNeutralColor(p);
            contentView.setDrawableTint(R.id.camera, false, color, Mode.SRC_ATOP);
            contentView.setDrawableTint(R.id.mic, false, color, Mode.SRC_ATOP);
            contentView.setDrawableTint(R.id.overlay, false, color, Mode.SRC_ATOP);
        }

        private void bindExpandButton(RemoteViews contentView, StandardTemplateParams p) {
            int color = isColorized(p) ? getPrimaryTextColor(p) : getSecondaryTextColor(p);
            contentView.setDrawableTint(R.id.expand_button, false, color, Mode.SRC_ATOP);
            contentView.setInt(R.id.notification_header, "setOriginalNotificationColor", color);
        }

        private void bindHeaderChronometerAndTime(RemoteViews contentView, StandardTemplateParams p) {
            String str = "setTime";
            if (showsTimeOrChronometer()) {
                contentView.setViewVisibility(R.id.time_divider, 0);
                setTextViewColorSecondary(contentView, R.id.time_divider, p);
                if (this.mN.extras.getBoolean(Notification.EXTRA_SHOW_CHRONOMETER)) {
                    contentView.setViewVisibility(R.id.chronometer, 0);
                    contentView.setLong(R.id.chronometer, "setBase", this.mN.when + (SystemClock.elapsedRealtime() - System.currentTimeMillis()));
                    contentView.setBoolean(R.id.chronometer, "setStarted", true);
                    contentView.setChronometerCountDown(R.id.chronometer, this.mN.extras.getBoolean(Notification.EXTRA_CHRONOMETER_COUNT_DOWN));
                    setTextViewColorSecondary(contentView, R.id.chronometer, p);
                    return;
                }
                contentView.setViewVisibility(R.id.time, 0);
                contentView.setLong(R.id.time, str, this.mN.when);
                setTextViewColorSecondary(contentView, R.id.time, p);
                return;
            }
            contentView.setLong(R.id.time, str, this.mN.when != 0 ? this.mN.when : this.mN.creationTime);
        }

        private void bindHeaderText(RemoteViews contentView, StandardTemplateParams p) {
            CharSequence summaryText = p.summaryText;
            if (summaryText == null) {
                Style style = this.mStyle;
                if (style != null && style.mSummaryTextSet && this.mStyle.hasSummaryInHeader()) {
                    summaryText = this.mStyle.mSummaryText;
                }
            }
            if (summaryText == null && this.mContext.getApplicationInfo().targetSdkVersion < 24) {
                Bundle bundle = this.mN.extras;
                String str = Notification.EXTRA_INFO_TEXT;
                if (bundle.getCharSequence(str) != null) {
                    summaryText = this.mN.extras.getCharSequence(str);
                }
            }
            if (summaryText != null) {
                contentView.setTextViewText(R.id.header_text, processTextSpans(processLegacyText(summaryText)));
                setTextViewColorSecondary(contentView, R.id.header_text, p);
                contentView.setViewVisibility(R.id.header_text, 0);
                contentView.setViewVisibility(R.id.header_text_divider, 0);
                setTextViewColorSecondary(contentView, R.id.header_text_divider, p);
            }
        }

        private void bindHeaderTextSecondary(RemoteViews contentView, StandardTemplateParams p) {
            if (!TextUtils.isEmpty(p.headerTextSecondary)) {
                contentView.setTextViewText(R.id.header_text_secondary, processTextSpans(processLegacyText(p.headerTextSecondary)));
                setTextViewColorSecondary(contentView, R.id.header_text_secondary, p);
                contentView.setViewVisibility(R.id.header_text_secondary, 0);
                contentView.setViewVisibility(R.id.header_text_secondary_divider, 0);
                setTextViewColorSecondary(contentView, R.id.header_text_secondary_divider, p);
            }
        }

        @UnsupportedAppUsage
        public String loadHeaderAppName() {
            CharSequence name = null;
            PackageManager pm = this.mContext.getPackageManager();
            Bundle bundle = this.mN.extras;
            String subName = Notification.EXTRA_SUBSTITUTE_APP_NAME;
            if (bundle.containsKey(subName)) {
                String pkg = this.mContext.getPackageName();
                subName = this.mN.extras.getString(subName);
                String str = permission.SUBSTITUTE_NOTIFICATION_APP_NAME;
                if (pm.checkPermission(str, pkg) == 0) {
                    name = subName;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("warning: pkg ");
                    stringBuilder.append(pkg);
                    stringBuilder.append(" attempting to substitute app name '");
                    stringBuilder.append(subName);
                    stringBuilder.append("' without holding perm ");
                    stringBuilder.append(str);
                    Log.w(Notification.TAG, stringBuilder.toString());
                }
            }
            if (TextUtils.isEmpty(name)) {
                name = pm.getApplicationLabel(this.mContext.getApplicationInfo());
            }
            if (TextUtils.isEmpty(name)) {
                return null;
            }
            return String.valueOf(name);
        }

        private void bindHeaderAppName(RemoteViews contentView, StandardTemplateParams p) {
            contentView.setTextViewText(R.id.app_name_text, loadHeaderAppName());
            if (isColorized(p)) {
                setTextViewColorPrimary(contentView, R.id.app_name_text, p);
            } else {
                contentView.setTextColor((int) R.id.app_name_text, getSecondaryTextColor(p));
            }
        }

        private boolean isColorized(StandardTemplateParams p) {
            return p.allowColorization && this.mN.isColorized();
        }

        private void bindSmallIcon(RemoteViews contentView, StandardTemplateParams p) {
            if (this.mN.mSmallIcon == null && this.mN.icon != 0) {
                Notification notification = this.mN;
                notification.mSmallIcon = Icon.createWithResource(this.mContext, notification.icon);
            }
            contentView.setImageViewIcon(16908294, this.mN.mSmallIcon);
            contentView.setInt(16908294, "setImageLevel", this.mN.iconLevel);
            processSmallIconColor(this.mN.mSmallIcon, contentView, p);
        }

        private boolean showsTimeOrChronometer() {
            return this.mN.showsTime() || this.mN.showsChronometer();
        }

        private void resetStandardTemplateWithActions(RemoteViews big) {
            big.setViewVisibility(R.id.actions, 8);
            big.removeAllViews(R.id.actions);
            big.setViewVisibility(R.id.notification_material_reply_container, 8);
            big.setTextViewText(R.id.notification_material_reply_text_1, null);
            big.setViewVisibility(R.id.notification_material_reply_text_1_container, 8);
            big.setViewVisibility(R.id.notification_material_reply_progress, 8);
            big.setViewVisibility(R.id.notification_material_reply_text_2, 8);
            big.setTextViewText(R.id.notification_material_reply_text_2, null);
            big.setViewVisibility(R.id.notification_material_reply_text_3, 8);
            big.setTextViewText(R.id.notification_material_reply_text_3, null);
            big.setViewLayoutMarginBottomDimen(R.id.notification_action_list_margin_target, R.dimen.notification_content_margin);
        }

        private RemoteViews applyStandardTemplateWithActions(int layoutId, TemplateBindResult result) {
            return applyStandardTemplateWithActions(layoutId, this.mParams.reset().fillTextsFrom(this), result);
        }

        private static List<Action> filterOutContextualActions(List<Action> actions) {
            List<Action> nonContextualActions = new ArrayList();
            for (Action action : actions) {
                if (!action.isContextual()) {
                    nonContextualActions.add(action);
                }
            }
            return nonContextualActions;
        }

        private RemoteViews applyStandardTemplateWithActions(int layoutId, StandardTemplateParams p, TemplateBindResult result) {
            StandardTemplateParams standardTemplateParams = p;
            RemoteViews big = applyStandardTemplate(layoutId, p, result);
            resetStandardTemplateWithActions(big);
            boolean validRemoteInput = false;
            List<Action> nonContextualActions = filterOutContextualActions(this.mActions);
            int N = nonContextualActions.size();
            boolean emphazisedMode = this.mN.fullScreenIntent != null;
            big.setBoolean(R.id.actions, "setEmphasizedMode", emphazisedMode);
            int i = 8;
            if (N > 0) {
                big.setViewVisibility(R.id.actions_container, 0);
                big.setViewVisibility(R.id.actions, 0);
                big.setViewLayoutMarginBottomDimen(R.id.notification_action_list_margin_target, 0);
                if (N > 3) {
                    N = 3;
                }
                for (int i2 = 0; i2 < N; i2++) {
                    Action action = (Action) nonContextualActions.get(i2);
                    boolean actionHasValidInput = hasValidRemoteInput(action);
                    validRemoteInput |= actionHasValidInput;
                    RemoteViews button = generateActionButton(action, emphazisedMode, standardTemplateParams);
                    if (actionHasValidInput && !emphazisedMode) {
                        button.setInt(R.id.action0, "setBackgroundResource", 0);
                    }
                    big.addView(R.id.actions, button);
                }
            } else {
                big.setViewVisibility(R.id.actions_container, 8);
            }
            CharSequence[] replyText = this.mN.extras.getCharSequenceArray(Notification.EXTRA_REMOTE_INPUT_HISTORY);
            if (validRemoteInput && replyText != null && replyText.length > 0 && !TextUtils.isEmpty(replyText[0]) && standardTemplateParams.maxRemoteInputHistory > 0) {
                boolean showSpinner = this.mN.extras.getBoolean(Notification.EXTRA_SHOW_REMOTE_INPUT_SPINNER);
                big.setViewVisibility(R.id.notification_material_reply_container, 0);
                big.setViewVisibility(R.id.notification_material_reply_text_1_container, 0);
                big.setTextViewText(R.id.notification_material_reply_text_1, processTextSpans(replyText[0]));
                setTextViewColorSecondary(big, R.id.notification_material_reply_text_1, standardTemplateParams);
                if (showSpinner) {
                    i = 0;
                }
                big.setViewVisibility(R.id.notification_material_reply_progress, i);
                big.setProgressIndeterminateTintList(R.id.notification_material_reply_progress, ColorStateList.valueOf(isColorized(standardTemplateParams) ? getPrimaryTextColor(standardTemplateParams) : resolveContrastColor(standardTemplateParams)));
                if (replyText.length > 1 && !TextUtils.isEmpty(replyText[1]) && standardTemplateParams.maxRemoteInputHistory > 1) {
                    big.setViewVisibility(R.id.notification_material_reply_text_2, 0);
                    big.setTextViewText(R.id.notification_material_reply_text_2, processTextSpans(replyText[1]));
                    setTextViewColorSecondary(big, R.id.notification_material_reply_text_2, standardTemplateParams);
                    if (replyText.length > 2 && !TextUtils.isEmpty(replyText[2]) && standardTemplateParams.maxRemoteInputHistory > 2) {
                        big.setViewVisibility(R.id.notification_material_reply_text_3, 0);
                        big.setTextViewText(R.id.notification_material_reply_text_3, processTextSpans(replyText[2]));
                        setTextViewColorSecondary(big, R.id.notification_material_reply_text_3, standardTemplateParams);
                    }
                }
            }
            return big;
        }

        private boolean hasValidRemoteInput(Action action) {
            if (TextUtils.isEmpty(action.title) || action.actionIntent == null) {
                return false;
            }
            RemoteInput[] remoteInputs = action.getRemoteInputs();
            if (remoteInputs == null) {
                return false;
            }
            for (RemoteInput r : remoteInputs) {
                CharSequence[] choices = r.getChoices();
                if (r.getAllowFreeFormInput() || (choices != null && choices.length != 0)) {
                    return true;
                }
            }
            return false;
        }

        public RemoteViews createContentView() {
            return createContentView(false);
        }

        public RemoteViews createContentView(boolean increasedHeight) {
            if (this.mN.contentView != null && useExistingRemoteView()) {
                return this.mN.contentView;
            }
            RemoteViews styleView = this.mStyle;
            if (styleView != null) {
                styleView = styleView.makeContentView(increasedHeight);
                if (styleView != null) {
                    return styleView;
                }
            }
            return applyStandardTemplate(getBaseLayoutResource(), null);
        }

        private boolean useExistingRemoteView() {
            Style style = this.mStyle;
            return style == null || !(style.displayCustomViewInline() || this.mRebuildStyledRemoteViews);
        }

        public RemoteViews createBigContentView() {
            RemoteViews result = null;
            if (this.mN.bigContentView != null && useExistingRemoteView()) {
                return this.mN.bigContentView;
            }
            Style style = this.mStyle;
            if (style != null) {
                result = style.makeBigContentView();
                hideLine1Text(result);
            } else if (this.mActions.size() != 0) {
                result = applyStandardTemplateWithActions(getBigBaseLayoutResource(), null);
            }
            makeHeaderExpanded(result);
            return result;
        }

        public RemoteViews makeNotificationHeader() {
            return makeNotificationHeader(this.mParams.reset().fillTextsFrom(this));
        }

        private RemoteViews makeNotificationHeader(StandardTemplateParams p) {
            p.disallowColorization();
            RemoteViews header = new BuilderRemoteViews(this.mContext.getApplicationInfo(), R.layout.notification_template_header);
            resetNotificationHeader(header);
            bindNotificationHeader(header, p);
            return header;
        }

        public RemoteViews makeAmbientNotification() {
            RemoteViews headsUpContentView = createHeadsUpContentView(null);
            if (headsUpContentView != null) {
                return headsUpContentView;
            }
            return createContentView();
        }

        private void hideLine1Text(RemoteViews result) {
            if (result != null) {
                result.setViewVisibility(R.id.text_line_1, 8);
            }
        }

        public static void makeHeaderExpanded(RemoteViews result) {
            if (result != null) {
                result.setBoolean(R.id.notification_header, "setExpanded", true);
            }
        }

        public RemoteViews createHeadsUpContentView(boolean increasedHeight) {
            if (this.mN.headsUpContentView != null && useExistingRemoteView()) {
                return this.mN.headsUpContentView;
            }
            RemoteViews styleView = this.mStyle;
            if (styleView != null) {
                styleView = styleView.makeHeadsUpContentView(increasedHeight);
                if (styleView != null) {
                    return styleView;
                }
            } else if (this.mActions.size() == 0) {
                return null;
            }
            return applyStandardTemplateWithActions(getBigBaseLayoutResource(), this.mParams.reset().fillTextsFrom(this).setMaxRemoteInputHistory(1), null);
        }

        public RemoteViews createHeadsUpContentView() {
            return createHeadsUpContentView(false);
        }

        @UnsupportedAppUsage
        public RemoteViews makePublicContentView() {
            return makePublicView(false);
        }

        public RemoteViews makePublicAmbientNotification() {
            return makePublicView(true);
        }

        private RemoteViews makePublicView(boolean ambient) {
            if (this.mN.publicVersion != null) {
                Builder builder = recoverBuilder(this.mContext, this.mN.publicVersion);
                return ambient ? builder.makeAmbientNotification() : builder.createContentView();
            }
            Bundle savedBundle = this.mN.extras;
            Style style = this.mStyle;
            this.mStyle = null;
            Icon largeIcon = this.mN.mLargeIcon;
            this.mN.mLargeIcon = null;
            Bitmap largeIconLegacy = this.mN.largeIcon;
            this.mN.largeIcon = null;
            ArrayList<Action> actions = this.mActions;
            this.mActions = new ArrayList();
            Bundle publicExtras = new Bundle();
            String str = Notification.EXTRA_SHOW_WHEN;
            publicExtras.putBoolean(str, savedBundle.getBoolean(str));
            str = Notification.EXTRA_SHOW_CHRONOMETER;
            publicExtras.putBoolean(str, savedBundle.getBoolean(str));
            str = Notification.EXTRA_CHRONOMETER_COUNT_DOWN;
            publicExtras.putBoolean(str, savedBundle.getBoolean(str));
            str = Notification.EXTRA_SUBSTITUTE_APP_NAME;
            String appName = savedBundle.getString(str);
            if (appName != null) {
                publicExtras.putString(str, appName);
            }
            this.mN.extras = publicExtras;
            RemoteViews view = makeNotificationHeader();
            view.setBoolean(R.id.notification_header, "setExpandOnlyOnButton", true);
            Notification notification = this.mN;
            notification.extras = savedBundle;
            notification.mLargeIcon = largeIcon;
            this.mN.largeIcon = largeIconLegacy;
            this.mActions = actions;
            this.mStyle = style;
            return view;
        }

        public RemoteViews makeLowPriorityContentView(boolean useRegularSubtext) {
            StandardTemplateParams p = this.mParams.reset().forceDefaultColor().fillTextsFrom(this);
            if (!useRegularSubtext || TextUtils.isEmpty(this.mParams.summaryText)) {
                p.summaryText(createSummaryText());
            }
            RemoteViews header = makeNotificationHeader(p);
            header.setBoolean(R.id.notification_header, "setAcceptAllTouches", true);
            return header;
        }

        private CharSequence createSummaryText() {
            CharSequence titleText = this.mN.extras.getCharSequence(Notification.EXTRA_TITLE);
            if (USE_ONLY_TITLE_IN_LOW_PRIORITY_SUMMARY) {
                return titleText;
            }
            SpannableStringBuilder summary = new SpannableStringBuilder();
            if (titleText == null) {
                titleText = this.mN.extras.getCharSequence(Notification.EXTRA_TITLE_BIG);
            }
            BidiFormatter bidi = BidiFormatter.getInstance();
            if (titleText != null) {
                summary.append(bidi.unicodeWrap(titleText));
            }
            CharSequence contentText = this.mN.extras.getCharSequence(Notification.EXTRA_TEXT);
            if (!(titleText == null || contentText == null)) {
                summary.append(bidi.unicodeWrap(this.mContext.getText(R.string.notification_header_divider_symbol_with_spaces)));
            }
            if (contentText != null) {
                summary.append(bidi.unicodeWrap(contentText));
            }
            return summary;
        }

        private RemoteViews generateActionButton(Action action, boolean emphazisedMode, StandardTemplateParams p) {
            int emphasizedActionLayoutResource;
            boolean z = true;
            boolean tombstone = action.actionIntent == null;
            ApplicationInfo applicationInfo = this.mContext.getApplicationInfo();
            if (emphazisedMode) {
                emphasizedActionLayoutResource = getEmphasizedActionLayoutResource();
            } else if (tombstone) {
                emphasizedActionLayoutResource = getActionTombstoneLayoutResource();
            } else {
                emphasizedActionLayoutResource = getActionLayoutResource();
            }
            RemoteViews button = new BuilderRemoteViews(applicationInfo, emphasizedActionLayoutResource);
            if (!tombstone) {
                button.setOnClickPendingIntent(R.id.action0, action.actionIntent);
            }
            button.setContentDescription(R.id.action0, action.title);
            if (action.mRemoteInputs != null) {
                button.setRemoteInputs(R.id.action0, action.mRemoteInputs);
            }
            if (emphazisedMode) {
                int rippleColor;
                CharSequence title = action.title;
                ColorStateList[] outResultColor = null;
                int background = resolveBackgroundColor(p);
                if (isLegacy()) {
                    title = ContrastColorUtil.clearColorSpans(title);
                } else {
                    outResultColor = new ColorStateList[1];
                    title = ensureColorSpanContrast(title, background, outResultColor);
                }
                button.setTextViewText(R.id.action0, processTextSpans(title));
                setTextViewColorPrimary(button, R.id.action0, p);
                boolean hasColorOverride = (outResultColor == null || outResultColor[0] == null) ? false : true;
                if (hasColorOverride) {
                    background = outResultColor[0].getDefaultColor();
                    rippleColor = ContrastColorUtil.resolvePrimaryColor(this.mContext, background, this.mInNightMode);
                    button.setTextColor((int) R.id.action0, rippleColor);
                } else if (getRawColor(p) == 0 || isColorized(p) || !this.mTintActionButtons || this.mInNightMode) {
                    rippleColor = getPrimaryTextColor(p);
                } else {
                    rippleColor = resolveContrastColor(p);
                    button.setTextColor((int) R.id.action0, rippleColor);
                }
                button.setColorStateList(R.id.action0, "setRippleColor", ColorStateList.valueOf((16777215 & rippleColor) | 855638016));
                button.setColorStateList(R.id.action0, "setButtonBackground", ColorStateList.valueOf(background));
                if (hasColorOverride) {
                    z = false;
                }
                button.setBoolean(R.id.action0, "setHasStroke", z);
            } else {
                button.setTextViewText(R.id.action0, processTextSpans(processLegacyText(action.title)));
                if (isColorized(p)) {
                    setTextViewColorPrimary(button, R.id.action0, p);
                } else if (getRawColor(p) != 0 && this.mTintActionButtons) {
                    button.setTextColor((int) R.id.action0, resolveContrastColor(p));
                }
            }
            button.setIntTag(R.id.action0, R.id.notification_action_index_tag, this.mActions.indexOf(action));
            return button;
        }

        private CharSequence ensureColorSpanContrast(CharSequence charSequence, int background, ColorStateList[] outResultColor) {
            CharSequence charSequence2 = charSequence;
            int i = background;
            if (!(charSequence2 instanceof Spanned)) {
                return charSequence;
            }
            Spanned ss = (Spanned) charSequence2;
            boolean z = false;
            Object[] spans = ss.getSpans(0, ss.length(), Object.class);
            SpannableStringBuilder builder = new SpannableStringBuilder(ss.toString());
            int length = spans.length;
            int i2 = 0;
            while (i2 < length) {
                Object[] spans2;
                int i3;
                Object resultSpan;
                boolean z2;
                ForegroundColorSpan span = spans[i2];
                ForegroundColorSpan resultSpan2 = span;
                int spanStart = ss.getSpanStart(span);
                int spanEnd = ss.getSpanEnd(span);
                boolean fullLength = spanEnd - spanStart == charSequence.length() ? true : z;
                if (resultSpan2 instanceof CharacterStyle) {
                    resultSpan2 = span.getUnderlying();
                }
                if (resultSpan2 instanceof TextAppearanceSpan) {
                    TextAppearanceSpan originalSpan = (TextAppearanceSpan) resultSpan2;
                    ColorStateList textColor = originalSpan.getTextColor();
                    if (textColor != null) {
                        int[] colors = textColor.getColors();
                        spans2 = spans;
                        spans = new int[colors.length];
                        i3 = length;
                        length = 0;
                        while (length < spans.length) {
                            int[] colors2 = colors;
                            spans[length] = ContrastColorUtil.ensureLargeTextContrast(colors[length], i, this.mInNightMode);
                            length++;
                            charSequence2 = charSequence;
                            colors = colors2;
                        }
                        ColorStateList textColor2 = new ColorStateList((int[][]) textColor.getStates().clone(), spans);
                        if (fullLength) {
                            outResultColor[0] = textColor2;
                            textColor2 = null;
                        }
                        resultSpan = new TextAppearanceSpan(originalSpan.getFamily(), originalSpan.getTextStyle(), originalSpan.getTextSize(), textColor2, originalSpan.getLinkTextColor());
                    } else {
                        spans2 = spans;
                        i3 = length;
                    }
                    z2 = false;
                } else {
                    spans2 = spans;
                    i3 = length;
                    if (resultSpan2 instanceof ForegroundColorSpan) {
                        int foregroundColor = ContrastColorUtil.ensureLargeTextContrast(resultSpan2.getForegroundColor(), i, this.mInNightMode);
                        if (fullLength) {
                            z2 = false;
                            outResultColor[0] = ColorStateList.valueOf(foregroundColor);
                            resultSpan = null;
                        } else {
                            z2 = false;
                            resultSpan = new ForegroundColorSpan(foregroundColor);
                        }
                    } else {
                        z2 = false;
                        resultSpan = span;
                    }
                }
                if (resultSpan != null) {
                    builder.setSpan(resultSpan, spanStart, spanEnd, ss.getSpanFlags(span));
                }
                i2++;
                charSequence2 = charSequence;
                z = z2;
                spans = spans2;
                length = i3;
            }
            return builder;
        }

        private boolean isLegacy() {
            if (!this.mIsLegacyInitialized) {
                this.mIsLegacy = this.mContext.getApplicationInfo().targetSdkVersion < 21;
                this.mIsLegacyInitialized = true;
            }
            return this.mIsLegacy;
        }

        private CharSequence processLegacyText(CharSequence charSequence) {
            boolean isAlreadyLightText = isLegacy() || textColorsNeedInversion();
            if (isAlreadyLightText) {
                return getColorUtil().invertCharSequenceColors(charSequence);
            }
            return charSequence;
        }

        private void processSmallIconColor(Icon smallIcon, RemoteViews contentView, StandardTemplateParams p) {
            int color;
            int i = 1;
            boolean colorable = !isLegacy() || getColorUtil().isGrayscaleIcon(this.mContext, smallIcon);
            if (isColorized(p)) {
                color = getPrimaryTextColor(p);
            } else {
                color = resolveContrastColor(p);
            }
            if (colorable) {
                contentView.setDrawableTint(16908294, false, color, Mode.SRC_ATOP);
            }
            if (colorable) {
                i = color;
            }
            contentView.setInt(R.id.notification_header, "setOriginalIconColor", i);
        }

        private void processLargeLegacyIcon(Icon largeIcon, RemoteViews contentView, StandardTemplateParams p) {
            if (largeIcon != null && isLegacy() && getColorUtil().isGrayscaleIcon(this.mContext, largeIcon)) {
                contentView.setDrawableTint(16908294, false, resolveContrastColor(p), Mode.SRC_ATOP);
            }
        }

        private void sanitizeColor() {
            if (this.mN.color != 0) {
                Notification notification = this.mN;
                notification.color |= -16777216;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public int resolveContrastColor(StandardTemplateParams p) {
            int i;
            int color;
            int rawColor = getRawColor(p);
            if (this.mCachedContrastColorIsFor == rawColor) {
                i = this.mCachedContrastColor;
                if (i != 1) {
                    return i;
                }
            }
            i = this.mContext.getColor(R.color.notification_material_background_color);
            if (rawColor == 0) {
                ensureColors(p);
                color = ContrastColorUtil.resolveDefaultColor(this.mContext, i, this.mInNightMode);
            } else {
                color = ContrastColorUtil.resolveContrastColor(this.mContext, rawColor, i, this.mInNightMode);
            }
            if (Color.alpha(color) < 255) {
                color = ContrastColorUtil.compositeColors(color, i);
            }
            this.mCachedContrastColorIsFor = rawColor;
            this.mCachedContrastColor = color;
            return color;
        }

        private int getRawColor(StandardTemplateParams p) {
            if (p.forceDefaultColor) {
                return 0;
            }
            return this.mN.color;
        }

        /* Access modifiers changed, original: 0000 */
        public int resolveNeutralColor() {
            int i = this.mNeutralColor;
            if (i != 1) {
                return i;
            }
            i = this.mContext.getColor(R.color.notification_material_background_color);
            this.mNeutralColor = ContrastColorUtil.resolveDefaultColor(this.mContext, i, this.mInNightMode);
            if (Color.alpha(this.mNeutralColor) < 255) {
                this.mNeutralColor = ContrastColorUtil.compositeColors(this.mNeutralColor, i);
            }
            return this.mNeutralColor;
        }

        public Notification buildUnstyled() {
            if (this.mActions.size() > 0) {
                this.mN.actions = new Action[this.mActions.size()];
                this.mActions.toArray(this.mN.actions);
            }
            if (!this.mPersonList.isEmpty()) {
                this.mN.extras.putParcelableArrayList(Notification.EXTRA_PEOPLE_LIST, this.mPersonList);
            }
            if (!(this.mN.bigContentView == null && this.mN.contentView == null && this.mN.headsUpContentView == null)) {
                this.mN.extras.putBoolean(Notification.EXTRA_CONTAINS_CUSTOM_VIEW, true);
            }
            return this.mN;
        }

        public static Builder recoverBuilder(Context context, Notification n) {
            Context builderContext;
            ApplicationInfo applicationInfo = (ApplicationInfo) n.extras.getParcelable(Notification.EXTRA_BUILDER_APPLICATION_INFO);
            if (applicationInfo != null) {
                try {
                    builderContext = context.createApplicationContext(applicationInfo, 4);
                } catch (NameNotFoundException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("ApplicationInfo ");
                    stringBuilder.append(applicationInfo);
                    stringBuilder.append(" not found");
                    Log.e(Notification.TAG, stringBuilder.toString());
                    builderContext = context;
                }
            } else {
                builderContext = context;
            }
            return new Builder(builderContext, n);
        }

        public Builder setAllowSystemGeneratedContextualActions(boolean allowed) {
            this.mN.mAllowSystemGeneratedContextualActions = allowed;
            return this;
        }

        @Deprecated
        public Notification getNotification() {
            return build();
        }

        public Notification build() {
            Notification notification;
            if (this.mUserExtras != null) {
                this.mN.extras = getAllExtras();
            }
            this.mN.creationTime = System.currentTimeMillis();
            Notification.addFieldsFromContext(this.mContext, this.mN);
            buildUnstyled();
            Style style = this.mStyle;
            if (style != null) {
                style.reduceImageSizes(this.mContext);
                this.mStyle.purgeResources();
                this.mStyle.validate(this.mContext);
                this.mStyle.buildStyled(this.mN);
            }
            this.mN.reduceImageSizes(this.mContext);
            if (this.mContext.getApplicationInfo().targetSdkVersion < 24 && useExistingRemoteView()) {
                if (this.mN.contentView == null) {
                    this.mN.contentView = createContentView();
                    this.mN.extras.putInt(EXTRA_REBUILD_CONTENT_VIEW_ACTION_COUNT, this.mN.contentView.getSequenceNumber());
                }
                if (this.mN.bigContentView == null) {
                    this.mN.bigContentView = createBigContentView();
                    if (this.mN.bigContentView != null) {
                        this.mN.extras.putInt(EXTRA_REBUILD_BIG_CONTENT_VIEW_ACTION_COUNT, this.mN.bigContentView.getSequenceNumber());
                    }
                }
                if (this.mN.headsUpContentView == null) {
                    this.mN.headsUpContentView = createHeadsUpContentView();
                    if (this.mN.headsUpContentView != null) {
                        this.mN.extras.putInt(EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW_ACTION_COUNT, this.mN.headsUpContentView.getSequenceNumber());
                    }
                }
            }
            if ((this.mN.defaults & 4) != 0) {
                notification = this.mN;
                notification.flags |= 1;
            }
            notification = this.mN;
            notification.allPendingIntents = null;
            return notification;
        }

        public Notification buildInto(Notification n) {
            build().cloneInto(n, true);
            return n;
        }

        public static Notification maybeCloneStrippedForDelivery(Notification n, boolean isLowRam, Context context) {
            String templateClass = n.extras.getString(Notification.EXTRA_TEMPLATE);
            if (!isLowRam && !TextUtils.isEmpty(templateClass) && Notification.getNotificationStyleClass(templateClass) == null) {
                return n;
            }
            boolean z = n.contentView instanceof BuilderRemoteViews;
            String str = EXTRA_REBUILD_CONTENT_VIEW_ACTION_COUNT;
            boolean stripHeadsUpContentView = true;
            z = z && n.extras.getInt(str, -1) == n.contentView.getSequenceNumber();
            boolean z2 = n.bigContentView instanceof BuilderRemoteViews;
            String str2 = EXTRA_REBUILD_BIG_CONTENT_VIEW_ACTION_COUNT;
            z2 = z2 && n.extras.getInt(str2, -1) == n.bigContentView.getSequenceNumber();
            boolean z3 = n.headsUpContentView instanceof BuilderRemoteViews;
            String str3 = EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW_ACTION_COUNT;
            if (!(z3 && n.extras.getInt(str3, -1) == n.headsUpContentView.getSequenceNumber())) {
                stripHeadsUpContentView = false;
            }
            if (!isLowRam && !z && !z2 && !stripHeadsUpContentView) {
                return n;
            }
            Notification clone = n.clone();
            if (z) {
                clone.contentView = null;
                clone.extras.remove(str);
            }
            if (z2) {
                clone.bigContentView = null;
                clone.extras.remove(str2);
            }
            if (stripHeadsUpContentView) {
                clone.headsUpContentView = null;
                clone.extras.remove(str3);
            }
            if (isLowRam && context.getResources().getStringArray(R.array.config_allowedManagedServicesOnLowRamDevices).length == 0) {
                clone.extras.remove("android.tv.EXTENSIONS");
                clone.extras.remove("android.wearable.EXTENSIONS");
                clone.extras.remove("android.car.EXTENSIONS");
            }
            return clone;
        }

        @UnsupportedAppUsage
        private int getBaseLayoutResource() {
            return R.layout.notification_template_material_base;
        }

        private int getBigBaseLayoutResource() {
            return R.layout.notification_template_material_big_base;
        }

        private int getBigPictureLayoutResource() {
            return R.layout.notification_template_material_big_picture;
        }

        private int getBigTextLayoutResource() {
            return R.layout.notification_template_material_big_text;
        }

        private int getInboxLayoutResource() {
            return R.layout.notification_template_material_inbox;
        }

        private int getMessagingLayoutResource() {
            return R.layout.notification_template_material_messaging;
        }

        private int getActionLayoutResource() {
            return R.layout.notification_material_action;
        }

        private int getEmphasizedActionLayoutResource() {
            return R.layout.notification_material_action_emphasized;
        }

        private int getActionTombstoneLayoutResource() {
            return R.layout.notification_material_action_tombstone;
        }

        private int getBackgroundColor(StandardTemplateParams p) {
            if (!isColorized(p)) {
                return 0;
            }
            int i = this.mBackgroundColor;
            if (i == 1) {
                i = getRawColor(p);
            }
            return i;
        }

        private int getNeutralColor(StandardTemplateParams p) {
            if (isColorized(p)) {
                return getSecondaryTextColor(p);
            }
            return resolveNeutralColor();
        }

        private int resolveBackgroundColor(StandardTemplateParams p) {
            int backgroundColor = getBackgroundColor(p);
            if (backgroundColor == 0) {
                return this.mContext.getColor(R.color.notification_material_background_color);
            }
            return backgroundColor;
        }

        private boolean shouldTintActionButtons() {
            return this.mTintActionButtons;
        }

        private boolean textColorsNeedInversion() {
            Style style = this.mStyle;
            boolean z = false;
            if (style == null || !MediaStyle.class.equals(style.getClass())) {
                return false;
            }
            int targetSdkVersion = this.mContext.getApplicationInfo().targetSdkVersion;
            if (targetSdkVersion > 23 && targetSdkVersion < 26) {
                z = true;
            }
            return z;
        }

        public void setColorPalette(int backgroundColor, int foregroundColor) {
            this.mBackgroundColor = backgroundColor;
            this.mForegroundColor = foregroundColor;
            this.mTextColorsAreForBackground = 1;
            ensureColors(this.mParams.reset().fillTextsFrom(this));
        }

        public void setRebuildStyledRemoteViews(boolean rebuild) {
            this.mRebuildStyledRemoteViews = rebuild;
        }

        public CharSequence getHeadsUpStatusBarText(boolean publicMode) {
            CharSequence text = this.mStyle;
            if (!(text == null || publicMode)) {
                text = text.getHeadsUpStatusBarText();
                if (!TextUtils.isEmpty(text)) {
                    return text;
                }
            }
            return loadHeaderAppName();
        }
    }

    private static class BuilderRemoteViews extends RemoteViews {
        public BuilderRemoteViews(Parcel parcel) {
            super(parcel);
        }

        public BuilderRemoteViews(ApplicationInfo appInfo, int layoutId) {
            super(appInfo, layoutId);
        }

        public BuilderRemoteViews clone() {
            Parcel p = Parcel.obtain();
            writeToParcel(p, 0);
            p.setDataPosition(0);
            BuilderRemoteViews brv = new BuilderRemoteViews(p);
            p.recycle();
            return brv;
        }
    }

    public interface Extender {
        Builder extend(Builder builder);
    }

    public static final class CarExtender implements Extender {
        private static final String EXTRA_CAR_EXTENDER = "android.car.EXTENSIONS";
        private static final String EXTRA_COLOR = "app_color";
        private static final String EXTRA_CONVERSATION = "car_conversation";
        private static final String EXTRA_LARGE_ICON = "large_icon";
        private static final String TAG = "CarExtender";
        private int mColor = 0;
        private Bitmap mLargeIcon;
        private UnreadConversation mUnreadConversation;

        public static class Builder {
            private long mLatestTimestamp;
            private final List<String> mMessages = new ArrayList();
            private final String mParticipant;
            private PendingIntent mReadPendingIntent;
            private RemoteInput mRemoteInput;
            private PendingIntent mReplyPendingIntent;

            public Builder(String name) {
                this.mParticipant = name;
            }

            public Builder addMessage(String message) {
                this.mMessages.add(message);
                return this;
            }

            public Builder setReplyAction(PendingIntent pendingIntent, RemoteInput remoteInput) {
                this.mRemoteInput = remoteInput;
                this.mReplyPendingIntent = pendingIntent;
                return this;
            }

            public Builder setReadPendingIntent(PendingIntent pendingIntent) {
                this.mReadPendingIntent = pendingIntent;
                return this;
            }

            public Builder setLatestTimestamp(long timestamp) {
                this.mLatestTimestamp = timestamp;
                return this;
            }

            public UnreadConversation build() {
                List list = this.mMessages;
                return new UnreadConversation((String[]) list.toArray(new String[list.size()]), this.mRemoteInput, this.mReplyPendingIntent, this.mReadPendingIntent, new String[]{this.mParticipant}, this.mLatestTimestamp);
            }
        }

        public static class UnreadConversation {
            private static final String KEY_AUTHOR = "author";
            private static final String KEY_MESSAGES = "messages";
            private static final String KEY_ON_READ = "on_read";
            private static final String KEY_ON_REPLY = "on_reply";
            private static final String KEY_PARTICIPANTS = "participants";
            private static final String KEY_REMOTE_INPUT = "remote_input";
            private static final String KEY_TEXT = "text";
            private static final String KEY_TIMESTAMP = "timestamp";
            private final long mLatestTimestamp;
            private final String[] mMessages;
            private final String[] mParticipants;
            private final PendingIntent mReadPendingIntent;
            private final RemoteInput mRemoteInput;
            private final PendingIntent mReplyPendingIntent;

            UnreadConversation(String[] messages, RemoteInput remoteInput, PendingIntent replyPendingIntent, PendingIntent readPendingIntent, String[] participants, long latestTimestamp) {
                this.mMessages = messages;
                this.mRemoteInput = remoteInput;
                this.mReadPendingIntent = readPendingIntent;
                this.mReplyPendingIntent = replyPendingIntent;
                this.mParticipants = participants;
                this.mLatestTimestamp = latestTimestamp;
            }

            public String[] getMessages() {
                return this.mMessages;
            }

            public RemoteInput getRemoteInput() {
                return this.mRemoteInput;
            }

            public PendingIntent getReplyPendingIntent() {
                return this.mReplyPendingIntent;
            }

            public PendingIntent getReadPendingIntent() {
                return this.mReadPendingIntent;
            }

            public String[] getParticipants() {
                return this.mParticipants;
            }

            public String getParticipant() {
                String[] strArr = this.mParticipants;
                return strArr.length > 0 ? strArr[0] : null;
            }

            public long getLatestTimestamp() {
                return this.mLatestTimestamp;
            }

            /* Access modifiers changed, original: 0000 */
            public Bundle getBundleForUnreadConversation() {
                Bundle b = new Bundle();
                String author = null;
                String[] strArr = this.mParticipants;
                if (strArr != null && strArr.length > 1) {
                    author = strArr[0];
                }
                Parcelable[] messages = new Parcelable[this.mMessages.length];
                for (int i = 0; i < messages.length; i++) {
                    Bundle m = new Bundle();
                    m.putString("text", this.mMessages[i]);
                    m.putString("author", author);
                    messages[i] = m;
                }
                b.putParcelableArray(KEY_MESSAGES, messages);
                RemoteInput remoteInput = this.mRemoteInput;
                if (remoteInput != null) {
                    b.putParcelable(KEY_REMOTE_INPUT, remoteInput);
                }
                b.putParcelable(KEY_ON_REPLY, this.mReplyPendingIntent);
                b.putParcelable(KEY_ON_READ, this.mReadPendingIntent);
                b.putStringArray(KEY_PARTICIPANTS, this.mParticipants);
                b.putLong("timestamp", this.mLatestTimestamp);
                return b;
            }

            static UnreadConversation getUnreadConversationFromBundle(Bundle b) {
                if (b == null) {
                    return null;
                }
                Parcelable[] parcelableMessages = b.getParcelableArray(KEY_MESSAGES);
                String[] messages = null;
                if (parcelableMessages != null) {
                    String[] tmp = new String[parcelableMessages.length];
                    boolean success = true;
                    for (int i = 0; i < tmp.length; i++) {
                        if (!(parcelableMessages[i] instanceof Bundle)) {
                            success = false;
                            break;
                        }
                        tmp[i] = ((Bundle) parcelableMessages[i]).getString("text");
                        if (tmp[i] == null) {
                            success = false;
                            break;
                        }
                    }
                    if (!success) {
                        return null;
                    }
                    messages = tmp;
                }
                PendingIntent onRead = (PendingIntent) b.getParcelable(KEY_ON_READ);
                PendingIntent onReply = (PendingIntent) b.getParcelable(KEY_ON_REPLY);
                RemoteInput remoteInput = (RemoteInput) b.getParcelable(KEY_REMOTE_INPUT);
                String[] participants = b.getStringArray(KEY_PARTICIPANTS);
                if (participants == null || participants.length != 1) {
                    return null;
                }
                return new UnreadConversation(messages, remoteInput, onReply, onRead, participants, b.getLong("timestamp"));
            }
        }

        public CarExtender(Notification notif) {
            Bundle carBundle = notif.extras == null ? null : notif.extras.getBundle(EXTRA_CAR_EXTENDER);
            if (carBundle != null) {
                this.mLargeIcon = (Bitmap) carBundle.getParcelable(EXTRA_LARGE_ICON);
                this.mColor = carBundle.getInt(EXTRA_COLOR, 0);
                this.mUnreadConversation = UnreadConversation.getUnreadConversationFromBundle(carBundle.getBundle(EXTRA_CONVERSATION));
            }
        }

        public Builder extend(Builder builder) {
            Bundle carExtensions = new Bundle();
            Bitmap bitmap = this.mLargeIcon;
            if (bitmap != null) {
                carExtensions.putParcelable(EXTRA_LARGE_ICON, bitmap);
            }
            int i = this.mColor;
            if (i != 0) {
                carExtensions.putInt(EXTRA_COLOR, i);
            }
            Bundle b = this.mUnreadConversation;
            if (b != null) {
                carExtensions.putBundle(EXTRA_CONVERSATION, b.getBundleForUnreadConversation());
            }
            builder.getExtras().putBundle(EXTRA_CAR_EXTENDER, carExtensions);
            return builder;
        }

        public CarExtender setColor(int color) {
            this.mColor = color;
            return this;
        }

        public int getColor() {
            return this.mColor;
        }

        public CarExtender setLargeIcon(Bitmap largeIcon) {
            this.mLargeIcon = largeIcon;
            return this;
        }

        public Bitmap getLargeIcon() {
            return this.mLargeIcon;
        }

        public CarExtender setUnreadConversation(UnreadConversation unreadConversation) {
            this.mUnreadConversation = unreadConversation;
            return this;
        }

        public UnreadConversation getUnreadConversation() {
            return this.mUnreadConversation;
        }
    }

    public static class DecoratedCustomViewStyle extends Style {
        public boolean displayCustomViewInline() {
            return true;
        }

        public RemoteViews makeContentView(boolean increasedHeight) {
            return makeStandardTemplateWithCustomContent(this.mBuilder.mN.contentView);
        }

        public RemoteViews makeBigContentView() {
            return makeDecoratedBigContentView();
        }

        public RemoteViews makeHeadsUpContentView(boolean increasedHeight) {
            return makeDecoratedHeadsUpContentView();
        }

        private RemoteViews makeDecoratedHeadsUpContentView() {
            RemoteViews headsUpContentView;
            if (this.mBuilder.mN.headsUpContentView == null) {
                headsUpContentView = this.mBuilder.mN.contentView;
            } else {
                headsUpContentView = this.mBuilder.mN.headsUpContentView;
            }
            if (this.mBuilder.mActions.size() == 0) {
                return makeStandardTemplateWithCustomContent(headsUpContentView);
            }
            TemplateBindResult result = new TemplateBindResult();
            RemoteViews remoteViews = this.mBuilder.applyStandardTemplateWithActions(this.mBuilder.getBigBaseLayoutResource(), result);
            buildIntoRemoteViewContent(remoteViews, headsUpContentView, result);
            return remoteViews;
        }

        private RemoteViews makeStandardTemplateWithCustomContent(RemoteViews customContent) {
            TemplateBindResult result = new TemplateBindResult();
            RemoteViews remoteViews = this.mBuilder.applyStandardTemplate(this.mBuilder.getBaseLayoutResource(), result);
            buildIntoRemoteViewContent(remoteViews, customContent, result);
            return remoteViews;
        }

        private RemoteViews makeDecoratedBigContentView() {
            RemoteViews bigContentView;
            if (this.mBuilder.mN.bigContentView == null) {
                bigContentView = this.mBuilder.mN.contentView;
            } else {
                bigContentView = this.mBuilder.mN.bigContentView;
            }
            if (this.mBuilder.mActions.size() == 0) {
                return makeStandardTemplateWithCustomContent(bigContentView);
            }
            TemplateBindResult result = new TemplateBindResult();
            RemoteViews remoteViews = this.mBuilder.applyStandardTemplateWithActions(this.mBuilder.getBigBaseLayoutResource(), result);
            buildIntoRemoteViewContent(remoteViews, bigContentView, result);
            return remoteViews;
        }

        private void buildIntoRemoteViewContent(RemoteViews remoteViews, RemoteViews customContent, TemplateBindResult result) {
            int childIndex = -1;
            if (customContent != null) {
                customContent = customContent.clone();
                remoteViews.removeAllViewsExceptId(R.id.notification_main_column, 16908301);
                remoteViews.addView(R.id.notification_main_column, customContent, 0);
                remoteViews.addFlags(1);
                childIndex = 0;
            }
            remoteViews.setIntTag(R.id.notification_main_column, R.id.notification_custom_view_index_tag, childIndex);
            remoteViews.setViewLayoutMarginEnd(R.id.notification_main_column, this.mBuilder.mContext.getResources().getDimensionPixelSize(R.dimen.notification_content_margin_end) + result.getIconMarginEnd());
        }

        public boolean areNotificationsVisiblyDifferent(Style other) {
            if (other == null || getClass() != other.getClass()) {
                return true;
            }
            return false;
        }
    }

    public static class MediaStyle extends Style {
        static final int MAX_MEDIA_BUTTONS = 5;
        static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;
        private static final int[] MEDIA_BUTTON_IDS = new int[]{R.id.action0, R.id.action1, R.id.action2, R.id.action3, R.id.action4};
        private int[] mActionsToShowInCompact = null;
        private Token mToken;

        @Deprecated
        public MediaStyle(Builder builder) {
            setBuilder(builder);
        }

        public MediaStyle setShowActionsInCompactView(int... actions) {
            this.mActionsToShowInCompact = actions;
            return this;
        }

        public MediaStyle setMediaSession(Token token) {
            this.mToken = token;
            return this;
        }

        @UnsupportedAppUsage
        public Notification buildStyled(Notification wip) {
            super.buildStyled(wip);
            if (wip.category == null) {
                wip.category = Notification.CATEGORY_TRANSPORT;
            }
            return wip;
        }

        public RemoteViews makeContentView(boolean increasedHeight) {
            return makeMediaContentView();
        }

        public RemoteViews makeBigContentView() {
            return makeMediaBigContentView();
        }

        public RemoteViews makeHeadsUpContentView(boolean increasedHeight) {
            RemoteViews expanded = makeMediaBigContentView();
            return expanded != null ? expanded : makeMediaContentView();
        }

        public void addExtras(Bundle extras) {
            super.addExtras(extras);
            Token token = this.mToken;
            if (token != null) {
                extras.putParcelable(Notification.EXTRA_MEDIA_SESSION, token);
            }
            int[] iArr = this.mActionsToShowInCompact;
            if (iArr != null) {
                extras.putIntArray(Notification.EXTRA_COMPACT_ACTIONS, iArr);
            }
        }

        /* Access modifiers changed, original: protected */
        public void restoreFromExtras(Bundle extras) {
            super.restoreFromExtras(extras);
            String str = Notification.EXTRA_MEDIA_SESSION;
            if (extras.containsKey(str)) {
                this.mToken = (Token) extras.getParcelable(str);
            }
            str = Notification.EXTRA_COMPACT_ACTIONS;
            if (extras.containsKey(str)) {
                this.mActionsToShowInCompact = extras.getIntArray(str);
            }
        }

        public boolean areNotificationsVisiblyDifferent(Style other) {
            if (other == null || getClass() != other.getClass()) {
                return true;
            }
            return false;
        }

        private void bindMediaActionButton(RemoteViews container, int buttonId, Action action, StandardTemplateParams p) {
            int tintColor;
            boolean tombstone = action.actionIntent == null;
            container.setViewVisibility(buttonId, 0);
            container.setImageViewIcon(buttonId, action.getIcon());
            boolean inNightMode = (this.mBuilder.mContext.getResources().getConfiguration().uiMode & 48) == 32;
            if (this.mBuilder.shouldTintActionButtons() || this.mBuilder.isColorized(p)) {
                tintColor = getActionColor(p);
            } else {
                tintColor = ContrastColorUtil.resolveColor(this.mBuilder.mContext, 0, inNightMode);
            }
            container.setDrawableTint(buttonId, false, tintColor, Mode.SRC_ATOP);
            TypedArray typedArray = this.mBuilder.mContext.obtainStyledAttributes(new int[]{16843820});
            int rippleAlpha = Color.alpha(typedArray.getColor(0, 0));
            typedArray.recycle();
            container.setRippleDrawableColor(buttonId, ColorStateList.valueOf(Color.argb(rippleAlpha, Color.red(tintColor), Color.green(tintColor), Color.blue(tintColor))));
            if (!tombstone) {
                container.setOnClickPendingIntent(buttonId, action.actionIntent);
            }
            container.setContentDescription(buttonId, action.title);
        }

        private RemoteViews makeMediaContentView() {
            int numActionsToShow;
            StandardTemplateParams p = this.mBuilder.mParams.reset().hasProgress(false).fillTextsFrom(this.mBuilder);
            RemoteViews view = this.mBuilder.applyStandardTemplate(R.layout.notification_template_material_media, p, null);
            int numActions = this.mBuilder.mActions.size();
            int[] iArr = this.mActionsToShowInCompact;
            if (iArr == null) {
                numActionsToShow = 0;
            } else {
                numActionsToShow = Math.min(iArr.length, 3);
            }
            if (numActionsToShow <= numActions) {
                int i;
                for (i = 0; i < 3; i++) {
                    if (i < numActionsToShow) {
                        bindMediaActionButton(view, MEDIA_BUTTON_IDS[i], (Action) this.mBuilder.mActions.get(this.mActionsToShowInCompact[i]), p);
                    } else {
                        view.setViewVisibility(MEDIA_BUTTON_IDS[i], 8);
                    }
                }
                handleImage(view);
                i = R.dimen.notification_content_margin_end;
                if (this.mBuilder.mN.hasLargeIcon()) {
                    i = R.dimen.notification_media_image_margin_end;
                }
                view.setViewLayoutMarginEndDimen(R.id.notification_main_column, i);
                return view;
            }
            throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", new Object[]{Integer.valueOf(numActions), Integer.valueOf(numActions - 1)}));
        }

        private int getActionColor(StandardTemplateParams p) {
            if (this.mBuilder.isColorized(p)) {
                return this.mBuilder.getPrimaryTextColor(p);
            }
            return this.mBuilder.resolveContrastColor(p);
        }

        private RemoteViews makeMediaBigContentView() {
            int actionCount = Math.min(this.mBuilder.mActions.size(), 5);
            int[] iArr = this.mActionsToShowInCompact;
            int actionsInCompact;
            if (iArr == null) {
                actionsInCompact = 0;
            } else {
                actionsInCompact = Math.min(iArr.length, 3);
            }
            if (!this.mBuilder.mN.hasLargeIcon() && actionCount <= actionsInCompact) {
                return null;
            }
            StandardTemplateParams p = this.mBuilder.mParams.reset().hasProgress(false).fillTextsFrom(this.mBuilder);
            RemoteViews big = this.mBuilder.applyStandardTemplate(R.layout.notification_template_material_big_media, p, null);
            for (int i = 0; i < 5; i++) {
                if (i < actionCount) {
                    bindMediaActionButton(big, MEDIA_BUTTON_IDS[i], (Action) this.mBuilder.mActions.get(i), p);
                } else {
                    big.setViewVisibility(MEDIA_BUTTON_IDS[i], 8);
                }
            }
            bindMediaActionButton(big, R.id.media_seamless, new Action(R.drawable.ic_media_seamless, this.mBuilder.mContext.getString(R.string.ext_media_seamless_action), null), p);
            big.setViewVisibility(R.id.media_seamless, 8);
            handleImage(big);
            return big;
        }

        private void handleImage(RemoteViews contentView) {
            if (this.mBuilder.mN.hasLargeIcon()) {
                contentView.setViewLayoutMarginEndDimen(R.id.line1, 0);
                contentView.setViewLayoutMarginEndDimen(R.id.text, 0);
            }
        }

        /* Access modifiers changed, original: protected */
        public boolean hasProgress() {
            return false;
        }
    }

    public static class DecoratedMediaCustomViewStyle extends MediaStyle {
        public boolean displayCustomViewInline() {
            return true;
        }

        public RemoteViews makeContentView(boolean increasedHeight) {
            return buildIntoRemoteView(super.makeContentView(null), R.id.notification_content_container, this.mBuilder.mN.contentView);
        }

        public RemoteViews makeBigContentView() {
            RemoteViews customRemoteView;
            if (this.mBuilder.mN.bigContentView != null) {
                customRemoteView = this.mBuilder.mN.bigContentView;
            } else {
                customRemoteView = this.mBuilder.mN.contentView;
            }
            return makeBigContentViewWithCustomContent(customRemoteView);
        }

        private RemoteViews makeBigContentViewWithCustomContent(RemoteViews customRemoteView) {
            RemoteViews remoteViews = super.makeBigContentView();
            if (remoteViews != null) {
                return buildIntoRemoteView(remoteViews, R.id.notification_main_column, customRemoteView);
            }
            if (customRemoteView != this.mBuilder.mN.contentView) {
                return buildIntoRemoteView(super.makeContentView(false), R.id.notification_content_container, customRemoteView);
            }
            return null;
        }

        public RemoteViews makeHeadsUpContentView(boolean increasedHeight) {
            RemoteViews customRemoteView;
            if (this.mBuilder.mN.headsUpContentView != null) {
                customRemoteView = this.mBuilder.mN.headsUpContentView;
            } else {
                customRemoteView = this.mBuilder.mN.contentView;
            }
            return makeBigContentViewWithCustomContent(customRemoteView);
        }

        public boolean areNotificationsVisiblyDifferent(Style other) {
            if (other == null || getClass() != other.getClass()) {
                return true;
            }
            return false;
        }

        private RemoteViews buildIntoRemoteView(RemoteViews remoteViews, int id, RemoteViews customContent) {
            if (customContent != null) {
                customContent = customContent.clone();
                customContent.overrideTextColors(this.mBuilder.getPrimaryTextColor(this.mBuilder.mParams));
                remoteViews.removeAllViews(id);
                remoteViews.addView(id, customContent);
                remoteViews.addFlags(1);
            }
            return remoteViews;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface GroupAlertBehavior {
    }

    public static class InboxStyle extends Style {
        private static final int NUMBER_OF_HISTORY_ALLOWED_UNTIL_REDUCTION = 1;
        private ArrayList<CharSequence> mTexts = new ArrayList(5);

        @Deprecated
        public InboxStyle(Builder builder) {
            setBuilder(builder);
        }

        public InboxStyle setBigContentTitle(CharSequence title) {
            internalSetBigContentTitle(Notification.safeCharSequence(title));
            return this;
        }

        public InboxStyle setSummaryText(CharSequence cs) {
            internalSetSummaryText(Notification.safeCharSequence(cs));
            return this;
        }

        public InboxStyle addLine(CharSequence cs) {
            this.mTexts.add(Notification.safeCharSequence(cs));
            return this;
        }

        public ArrayList<CharSequence> getLines() {
            return this.mTexts;
        }

        public void addExtras(Bundle extras) {
            super.addExtras(extras);
            extras.putCharSequenceArray(Notification.EXTRA_TEXT_LINES, (CharSequence[]) this.mTexts.toArray(new CharSequence[this.mTexts.size()]));
        }

        /* Access modifiers changed, original: protected */
        public void restoreFromExtras(Bundle extras) {
            super.restoreFromExtras(extras);
            this.mTexts.clear();
            String str = Notification.EXTRA_TEXT_LINES;
            if (extras.containsKey(str)) {
                Collections.addAll(this.mTexts, extras.getCharSequenceArray(str));
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:27:0x00fe  */
        /* JADX WARNING: Removed duplicated region for block: B:22:0x00b8  */
        /* JADX WARNING: Removed duplicated region for block: B:31:0x0109  */
        public android.widget.RemoteViews makeBigContentView() {
            /*
            r20 = this;
            r0 = r20;
            r1 = r0.mBuilder;
            r1 = r1.mParams;
            r1 = r1.reset();
            r2 = r0.mBuilder;
            r1 = r1.fillTextsFrom(r2);
            r2 = 0;
            r1 = r1.text(r2);
            r3 = new android.app.Notification$TemplateBindResult;
            r3.<init>(r2);
            r2 = r3;
            r3 = r0.mBuilder;
            r3 = r3.getInboxLayoutResource();
            r3 = r0.getStandardView(r3, r1, r2);
            r4 = 7;
            r4 = new int[r4];
            r4 = {16909020, 16909021, 16909022, 16909023, 16909024, 16909025, 16909026};
            r10 = r4;
            r4 = r10.length;
            r11 = 0;
            r5 = r11;
        L_0x002f:
            if (r5 >= r4) goto L_0x003b;
        L_0x0031:
            r6 = r10[r5];
            r7 = 8;
            r3.setViewVisibility(r6, r7);
            r5 = r5 + 1;
            goto L_0x002f;
        L_0x003b:
            r4 = 0;
            r5 = r0.mBuilder;
            r5 = r5.mContext;
            r5 = r5.getResources();
            r6 = 17105358; // 0x10501ce float:2.4429537E-38 double:8.4511697E-317;
            r12 = r5.getDimensionPixelSize(r6);
            r5 = 1;
            r6 = 0;
            r7 = r10.length;
            r8 = r0.mBuilder;
            r8 = r8.mActions;
            r8 = r8.size();
            if (r8 <= 0) goto L_0x005e;
        L_0x005c:
            r7 = r7 + -1;
        L_0x005e:
            r8 = r0.mBuilder;
            r8 = r8.mN;
            r8 = r8.extras;
            r9 = "android.remoteInputHistory";
            r13 = r8.getCharSequenceArray(r9);
            if (r13 == 0) goto L_0x009a;
        L_0x006e:
            r8 = r13.length;
            r9 = 1;
            if (r8 <= r9) goto L_0x009a;
        L_0x0072:
            r8 = r13.length;
            r14 = 3;
            r8 = java.lang.Math.min(r8, r14);
            r14 = r0.mTexts;
            r14 = r14.size();
            r14 = r14 + r8;
            r14 = r14 - r9;
            if (r14 <= r7) goto L_0x009a;
        L_0x0082:
            r9 = r14 - r7;
            r15 = r0.mTexts;
            r15 = r15.size();
            if (r15 <= r7) goto L_0x0093;
        L_0x008c:
            r7 = r7 - r9;
            r14 = r4;
            r9 = r5;
            r16 = r6;
            r15 = r7;
            goto L_0x009f;
        L_0x0093:
            r4 = r9;
            r14 = r4;
            r9 = r5;
            r16 = r6;
            r15 = r7;
            goto L_0x009f;
        L_0x009a:
            r14 = r4;
            r9 = r5;
            r16 = r6;
            r15 = r7;
        L_0x009f:
            r4 = r0.mTexts;
            r4 = r4.size();
            if (r14 >= r4) goto L_0x0106;
        L_0x00a7:
            if (r14 >= r15) goto L_0x0106;
        L_0x00a9:
            r4 = r0.mTexts;
            r4 = r4.get(r14);
            r8 = r4;
            r8 = (java.lang.CharSequence) r8;
            r4 = android.text.TextUtils.isEmpty(r8);
            if (r4 != 0) goto L_0x00fe;
        L_0x00b8:
            r4 = r10[r14];
            r3.setViewVisibility(r4, r11);
            r4 = r10[r14];
            r5 = r0.mBuilder;
            r6 = r0.mBuilder;
            r6 = r6.processLegacyText(r8);
            r5 = r5.processTextSpans(r6);
            r3.setTextViewText(r4, r5);
            r4 = r0.mBuilder;
            r5 = r10[r14];
            r4.setTextViewColorSecondary(r3, r5, r1);
            r5 = r10[r14];
            r6 = 0;
            r17 = 0;
            r18 = 0;
            r4 = r3;
            r7 = r12;
            r19 = r8;
            r8 = r17;
            r11 = r9;
            r9 = r18;
            r4.setViewPadding(r5, r6, r7, r8, r9);
            r4 = r10[r14];
            r5 = r2.getIconMarginEnd();
            r0.handleInboxImageMargin(r3, r4, r11, r5);
            if (r11 == 0) goto L_0x00f8;
        L_0x00f3:
            r4 = r10[r14];
            r16 = r4;
            goto L_0x00fb;
        L_0x00f8:
            r4 = 0;
            r16 = r4;
        L_0x00fb:
            r4 = 0;
            r9 = r4;
            goto L_0x0101;
        L_0x00fe:
            r19 = r8;
            r11 = r9;
            r14 = r14 + 1;
            r11 = 0;
            goto L_0x009f;
        L_0x0106:
            r11 = r9;
            if (r16 == 0) goto L_0x0124;
        L_0x0109:
            r4 = r0.mBuilder;
            r4 = r4.mContext;
            r4 = r4.getResources();
            r5 = 17105382; // 0x10501e6 float:2.4429604E-38 double:8.4511816E-317;
            r12 = r4.getDimensionPixelSize(r5);
            r6 = 0;
            r8 = 0;
            r9 = 0;
            r4 = r3;
            r5 = r16;
            r7 = r12;
            r4.setViewPadding(r5, r6, r7, r8, r9);
        L_0x0124:
            return r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.Notification$InboxStyle.makeBigContentView():android.widget.RemoteViews");
        }

        public boolean areNotificationsVisiblyDifferent(Style other) {
            if (other == null || getClass() != other.getClass()) {
                return true;
            }
            InboxStyle newS = (InboxStyle) other;
            ArrayList<CharSequence> myLines = getLines();
            ArrayList<CharSequence> newLines = newS.getLines();
            int n = myLines.size();
            if (n != newLines.size()) {
                return true;
            }
            for (int i = 0; i < n; i++) {
                if (!Objects.equals(String.valueOf(myLines.get(i)), String.valueOf(newLines.get(i)))) {
                    return true;
                }
            }
            return false;
        }

        private void handleInboxImageMargin(RemoteViews contentView, int id, boolean first, int marginEndValue) {
            int endMargin = 0;
            if (first) {
                boolean hasProgress = false;
                int max = this.mBuilder.mN.extras.getInt(Notification.EXTRA_PROGRESS_MAX, 0);
                boolean ind = this.mBuilder.mN.extras.getBoolean(Notification.EXTRA_PROGRESS_INDETERMINATE);
                if (max != 0 || ind) {
                    hasProgress = true;
                }
                if (!hasProgress) {
                    endMargin = marginEndValue;
                }
            }
            contentView.setViewLayoutMarginEnd(id, endMargin);
        }
    }

    public static class MessagingStyle extends Style {
        public static final int MAXIMUM_RETAINED_MESSAGES = 25;
        CharSequence mConversationTitle;
        List<Message> mHistoricMessages;
        boolean mIsGroupConversation;
        List<Message> mMessages;
        Person mUser;

        public static final class Message {
            static final String KEY_DATA_MIME_TYPE = "type";
            static final String KEY_DATA_URI = "uri";
            static final String KEY_EXTRAS_BUNDLE = "extras";
            static final String KEY_REMOTE_INPUT_HISTORY = "remote_input_history";
            static final String KEY_SENDER = "sender";
            static final String KEY_SENDER_PERSON = "sender_person";
            public static final String KEY_TEXT = "text";
            static final String KEY_TIMESTAMP = "time";
            private String mDataMimeType;
            private Uri mDataUri;
            private Bundle mExtras;
            private final boolean mRemoteInputHistory;
            private final Person mSender;
            private final CharSequence mText;
            private final long mTimestamp;

            public Message(CharSequence text, long timestamp, CharSequence sender) {
                Person person;
                if (sender == null) {
                    person = null;
                } else {
                    person = new android.app.Person.Builder().setName(sender).build();
                }
                this(text, timestamp, person);
            }

            public Message(CharSequence text, long timestamp, Person sender) {
                this(text, timestamp, sender, false);
            }

            public Message(CharSequence text, long timestamp, Person sender, boolean remoteInputHistory) {
                this.mExtras = new Bundle();
                this.mText = text;
                this.mTimestamp = timestamp;
                this.mSender = sender;
                this.mRemoteInputHistory = remoteInputHistory;
            }

            public Message setData(String dataMimeType, Uri dataUri) {
                this.mDataMimeType = dataMimeType;
                this.mDataUri = dataUri;
                return this;
            }

            public CharSequence getText() {
                return this.mText;
            }

            public long getTimestamp() {
                return this.mTimestamp;
            }

            public Bundle getExtras() {
                return this.mExtras;
            }

            public CharSequence getSender() {
                Person person = this.mSender;
                return person == null ? null : person.getName();
            }

            public Person getSenderPerson() {
                return this.mSender;
            }

            public String getDataMimeType() {
                return this.mDataMimeType;
            }

            public Uri getDataUri() {
                return this.mDataUri;
            }

            public boolean isRemoteInputHistory() {
                return this.mRemoteInputHistory;
            }

            @VisibleForTesting
            public Bundle toBundle() {
                Bundle bundle = new Bundle();
                CharSequence charSequence = this.mText;
                if (charSequence != null) {
                    bundle.putCharSequence("text", charSequence);
                }
                bundle.putLong("time", this.mTimestamp);
                Person person = this.mSender;
                if (person != null) {
                    bundle.putCharSequence("sender", person.getName());
                    bundle.putParcelable(KEY_SENDER_PERSON, this.mSender);
                }
                String str = this.mDataMimeType;
                if (str != null) {
                    bundle.putString("type", str);
                }
                Uri uri = this.mDataUri;
                if (uri != null) {
                    bundle.putParcelable("uri", uri);
                }
                Bundle bundle2 = this.mExtras;
                if (bundle2 != null) {
                    bundle.putBundle("extras", bundle2);
                }
                boolean z = this.mRemoteInputHistory;
                if (z) {
                    bundle.putBoolean(KEY_REMOTE_INPUT_HISTORY, z);
                }
                return bundle;
            }

            static Bundle[] getBundleArrayForMessages(List<Message> messages) {
                Bundle[] bundles = new Bundle[messages.size()];
                int N = messages.size();
                for (int i = 0; i < N; i++) {
                    bundles[i] = ((Message) messages.get(i)).toBundle();
                }
                return bundles;
            }

            public static List<Message> getMessagesFromBundleArray(Parcelable[] bundles) {
                if (bundles == null) {
                    return new ArrayList();
                }
                List<Message> messages = new ArrayList(bundles.length);
                for (int i = 0; i < bundles.length; i++) {
                    if (bundles[i] instanceof Bundle) {
                        Message message = getMessageFromBundle((Bundle) bundles[i]);
                        if (message != null) {
                            messages.add(message);
                        }
                    }
                }
                return messages;
            }

            /* JADX WARNING: Removed duplicated region for block: B:20:0x0078 A:{Catch:{ ClassCastException -> 0x0085 }} */
            public static android.app.Notification.MessagingStyle.Message getMessageFromBundle(android.os.Bundle r14) {
                /*
                r0 = "uri";
                r1 = "extras";
                r2 = "type";
                r3 = "time";
                r4 = "text";
                r5 = 0;
                r6 = r14.containsKey(r4);	 Catch:{ ClassCastException -> 0x0085 }
                if (r6 == 0) goto L_0x0084;
            L_0x0015:
                r6 = r14.containsKey(r3);	 Catch:{ ClassCastException -> 0x0085 }
                if (r6 != 0) goto L_0x001c;
            L_0x001b:
                goto L_0x0084;
            L_0x001c:
                r6 = "sender_person";
                r6 = r14.getParcelable(r6);	 Catch:{ ClassCastException -> 0x0085 }
                r6 = (android.app.Person) r6;	 Catch:{ ClassCastException -> 0x0085 }
                if (r6 != 0) goto L_0x0040;
            L_0x0027:
                r7 = "sender";
                r7 = r14.getCharSequence(r7);	 Catch:{ ClassCastException -> 0x0085 }
                if (r7 == 0) goto L_0x0040;
            L_0x0030:
                r8 = new android.app.Person$Builder;	 Catch:{ ClassCastException -> 0x0085 }
                r8.<init>();	 Catch:{ ClassCastException -> 0x0085 }
                r8 = r8.setName(r7);	 Catch:{ ClassCastException -> 0x0085 }
                r8 = r8.build();	 Catch:{ ClassCastException -> 0x0085 }
                r6 = r8;
                r12 = r6;
                goto L_0x0041;
            L_0x0040:
                r12 = r6;
            L_0x0041:
                r13 = new android.app.Notification$MessagingStyle$Message;	 Catch:{ ClassCastException -> 0x0085 }
                r7 = r14.getCharSequence(r4);	 Catch:{ ClassCastException -> 0x0085 }
                r8 = r14.getLong(r3);	 Catch:{ ClassCastException -> 0x0085 }
                r3 = "remote_input_history";
                r4 = 0;
                r11 = r14.getBoolean(r3, r4);	 Catch:{ ClassCastException -> 0x0085 }
                r6 = r13;
                r10 = r12;
                r6.<init>(r7, r8, r10, r11);	 Catch:{ ClassCastException -> 0x0085 }
                r3 = r13;
                r4 = r14.containsKey(r2);	 Catch:{ ClassCastException -> 0x0085 }
                if (r4 == 0) goto L_0x0072;
            L_0x005f:
                r4 = r14.containsKey(r0);	 Catch:{ ClassCastException -> 0x0085 }
                if (r4 == 0) goto L_0x0072;
            L_0x0065:
                r2 = r14.getString(r2);	 Catch:{ ClassCastException -> 0x0085 }
                r0 = r14.getParcelable(r0);	 Catch:{ ClassCastException -> 0x0085 }
                r0 = (android.net.Uri) r0;	 Catch:{ ClassCastException -> 0x0085 }
                r3.setData(r2, r0);	 Catch:{ ClassCastException -> 0x0085 }
            L_0x0072:
                r0 = r14.containsKey(r1);	 Catch:{ ClassCastException -> 0x0085 }
                if (r0 == 0) goto L_0x0083;
            L_0x0078:
                r0 = r3.getExtras();	 Catch:{ ClassCastException -> 0x0085 }
                r1 = r14.getBundle(r1);	 Catch:{ ClassCastException -> 0x0085 }
                r0.putAll(r1);	 Catch:{ ClassCastException -> 0x0085 }
            L_0x0083:
                return r3;
            L_0x0084:
                return r5;
            L_0x0085:
                r0 = move-exception;
                return r5;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.app.Notification$MessagingStyle$Message.getMessageFromBundle(android.os.Bundle):android.app.Notification$MessagingStyle$Message");
            }
        }

        MessagingStyle() {
            this.mMessages = new ArrayList();
            this.mHistoricMessages = new ArrayList();
        }

        public MessagingStyle(CharSequence userDisplayName) {
            this(new android.app.Person.Builder().setName(userDisplayName).build());
        }

        public MessagingStyle(Person user) {
            this.mMessages = new ArrayList();
            this.mHistoricMessages = new ArrayList();
            this.mUser = user;
        }

        public void validate(Context context) {
            super.validate(context);
            if (context.getApplicationInfo().targetSdkVersion >= 28) {
                Person person = this.mUser;
                if (person == null || person.getName() == null) {
                    throw new RuntimeException("User must be valid and have a name.");
                }
            }
        }

        public CharSequence getHeadsUpStatusBarText() {
            CharSequence conversationTitle;
            if (TextUtils.isEmpty(this.mBigContentTitle)) {
                conversationTitle = this.mConversationTitle;
            } else {
                conversationTitle = this.mBigContentTitle;
            }
            if (TextUtils.isEmpty(conversationTitle) || hasOnlyWhiteSpaceSenders()) {
                return null;
            }
            return conversationTitle;
        }

        public Person getUser() {
            return this.mUser;
        }

        public CharSequence getUserDisplayName() {
            return this.mUser.getName();
        }

        public MessagingStyle setConversationTitle(CharSequence conversationTitle) {
            this.mConversationTitle = conversationTitle;
            return this;
        }

        public CharSequence getConversationTitle() {
            return this.mConversationTitle;
        }

        public MessagingStyle addMessage(CharSequence text, long timestamp, CharSequence sender) {
            return addMessage(text, timestamp, sender == null ? null : new android.app.Person.Builder().setName(sender).build());
        }

        public MessagingStyle addMessage(CharSequence text, long timestamp, Person sender) {
            return addMessage(new Message(text, timestamp, sender));
        }

        public MessagingStyle addMessage(Message message) {
            this.mMessages.add(message);
            if (this.mMessages.size() > 25) {
                this.mMessages.remove(0);
            }
            return this;
        }

        public MessagingStyle addHistoricMessage(Message message) {
            this.mHistoricMessages.add(message);
            if (this.mHistoricMessages.size() > 25) {
                this.mHistoricMessages.remove(0);
            }
            return this;
        }

        public List<Message> getMessages() {
            return this.mMessages;
        }

        public List<Message> getHistoricMessages() {
            return this.mHistoricMessages;
        }

        public MessagingStyle setGroupConversation(boolean isGroupConversation) {
            this.mIsGroupConversation = isGroupConversation;
            return this;
        }

        public boolean isGroupConversation() {
            if (this.mBuilder == null || this.mBuilder.mContext.getApplicationInfo().targetSdkVersion >= 28) {
                return this.mIsGroupConversation;
            }
            return this.mConversationTitle != null;
        }

        public void addExtras(Bundle extras) {
            super.addExtras(extras);
            Person person = this.mUser;
            if (person != null) {
                extras.putCharSequence(Notification.EXTRA_SELF_DISPLAY_NAME, person.getName());
                extras.putParcelable(Notification.EXTRA_MESSAGING_PERSON, this.mUser);
            }
            CharSequence charSequence = this.mConversationTitle;
            if (charSequence != null) {
                extras.putCharSequence(Notification.EXTRA_CONVERSATION_TITLE, charSequence);
            }
            if (!this.mMessages.isEmpty()) {
                extras.putParcelableArray(Notification.EXTRA_MESSAGES, Message.getBundleArrayForMessages(this.mMessages));
            }
            if (!this.mHistoricMessages.isEmpty()) {
                extras.putParcelableArray(Notification.EXTRA_HISTORIC_MESSAGES, Message.getBundleArrayForMessages(this.mHistoricMessages));
            }
            fixTitleAndTextExtras(extras);
            extras.putBoolean(Notification.EXTRA_IS_GROUP_CONVERSATION, this.mIsGroupConversation);
        }

        private void fixTitleAndTextExtras(Bundle extras) {
            CharSequence title;
            Message m = findLatestIncomingMessage();
            CharSequence sender = null;
            CharSequence text = m == null ? null : m.mText;
            if (m != null) {
                sender = (m.mSender == null || TextUtils.isEmpty(m.mSender.getName())) ? this.mUser : m.mSender;
                sender = sender.getName();
            }
            if (TextUtils.isEmpty(this.mConversationTitle)) {
                title = sender;
            } else if (TextUtils.isEmpty(sender)) {
                title = this.mConversationTitle;
            } else {
                BidiFormatter bidi = BidiFormatter.getInstance();
                title = this.mBuilder.mContext.getString(R.string.notification_messaging_title_template, bidi.unicodeWrap(this.mConversationTitle), bidi.unicodeWrap(sender));
            }
            if (title != null) {
                extras.putCharSequence(Notification.EXTRA_TITLE, title);
            }
            if (text != null) {
                extras.putCharSequence(Notification.EXTRA_TEXT, text);
            }
        }

        /* Access modifiers changed, original: protected */
        public void restoreFromExtras(Bundle extras) {
            super.restoreFromExtras(extras);
            this.mUser = (Person) extras.getParcelable(Notification.EXTRA_MESSAGING_PERSON);
            if (this.mUser == null) {
                this.mUser = new android.app.Person.Builder().setName(extras.getCharSequence(Notification.EXTRA_SELF_DISPLAY_NAME)).build();
            }
            this.mConversationTitle = extras.getCharSequence(Notification.EXTRA_CONVERSATION_TITLE);
            this.mMessages = Message.getMessagesFromBundleArray(extras.getParcelableArray(Notification.EXTRA_MESSAGES));
            this.mHistoricMessages = Message.getMessagesFromBundleArray(extras.getParcelableArray(Notification.EXTRA_HISTORIC_MESSAGES));
            this.mIsGroupConversation = extras.getBoolean(Notification.EXTRA_IS_GROUP_CONVERSATION);
        }

        public RemoteViews makeContentView(boolean increasedHeight) {
            this.mBuilder.mOriginalActions = this.mBuilder.mActions;
            this.mBuilder.mActions = new ArrayList();
            RemoteViews remoteViews = makeMessagingView(true, false);
            this.mBuilder.mActions = this.mBuilder.mOriginalActions;
            this.mBuilder.mOriginalActions = null;
            return remoteViews;
        }

        public boolean areNotificationsVisiblyDifferent(Style other) {
            if (other == null || getClass() != other.getClass()) {
                return true;
            }
            MessagingStyle newS = (MessagingStyle) other;
            List<Message> oldMs = getMessages();
            List<Message> newMs = newS.getMessages();
            if (oldMs == null || newMs == null) {
                newMs = new ArrayList();
            }
            int n = oldMs.size();
            if (n != newMs.size()) {
                return true;
            }
            for (int i = 0; i < n; i++) {
                Message oldM = (Message) oldMs.get(i);
                Message newM = (Message) newMs.get(i);
                if (!Objects.equals(String.valueOf(oldM.getText()), String.valueOf(newM.getText())) || !Objects.equals(oldM.getDataUri(), newM.getDataUri())) {
                    return true;
                }
                String oldSender;
                String newSender;
                if (oldM.getSenderPerson() == null) {
                    oldSender = oldM.getSender();
                } else {
                    oldSender = oldM.getSenderPerson().getName();
                }
                oldSender = String.valueOf(oldSender);
                if (newM.getSenderPerson() == null) {
                    newSender = newM.getSender();
                } else {
                    newSender = newM.getSenderPerson().getName();
                }
                if (!Objects.equals(oldSender, String.valueOf(newSender))) {
                    return true;
                }
                String newKey = null;
                String oldKey = oldM.getSenderPerson() == null ? null : oldM.getSenderPerson().getKey();
                if (newM.getSenderPerson() != null) {
                    newKey = newM.getSenderPerson().getKey();
                }
                if (!Objects.equals(oldKey, newKey)) {
                    return true;
                }
            }
            return false;
        }

        private Message findLatestIncomingMessage() {
            return findLatestIncomingMessage(this.mMessages);
        }

        public static Message findLatestIncomingMessage(List<Message> messages) {
            for (int i = messages.size() - 1; i >= 0; i--) {
                Message m = (Message) messages.get(i);
                if (m.mSender != null && !TextUtils.isEmpty(m.mSender.getName())) {
                    return m;
                }
            }
            if (messages.isEmpty()) {
                return null;
            }
            return (Message) messages.get(messages.size() - 1);
        }

        public RemoteViews makeBigContentView() {
            return makeMessagingView(false, true);
        }

        private RemoteViews makeMessagingView(boolean displayImagesAtEnd, boolean hideRightIcons) {
            CharSequence conversationTitle;
            boolean isOneToOne;
            int primaryTextColor;
            if (TextUtils.isEmpty(this.mBigContentTitle)) {
                conversationTitle = this.mConversationTitle;
            } else {
                conversationTitle = this.mBigContentTitle;
            }
            boolean z = true;
            CharSequence nameReplacement = null;
            Icon avatarReplacement = null;
            if (this.mBuilder.mContext.getApplicationInfo().targetSdkVersion >= 28) {
                isOneToOne = isGroupConversation() ^ true;
            } else {
                isOneToOne = TextUtils.isEmpty(conversationTitle);
                avatarReplacement = this.mBuilder.mN.mLargeIcon;
                if (hasOnlyWhiteSpaceSenders()) {
                    isOneToOne = true;
                    nameReplacement = conversationTitle;
                    conversationTitle = null;
                }
            }
            TemplateBindResult bindResult = new TemplateBindResult();
            StandardTemplateParams text = this.mBuilder.mParams.reset().hasProgress(false).title(conversationTitle).text(null);
            if (!(hideRightIcons || isOneToOne)) {
                z = false;
            }
            StandardTemplateParams p = text.hideLargeIcon(z).hideReplyIcon(hideRightIcons).headerTextSecondary(conversationTitle);
            RemoteViews contentView = this.mBuilder.applyStandardTemplateWithActions(this.mBuilder.getMessagingLayoutResource(), p, bindResult);
            addExtras(this.mBuilder.mN.extras);
            contentView.setViewLayoutMarginEnd(R.id.notification_messaging, bindResult.getIconMarginEnd());
            if (this.mBuilder.isColorized(p)) {
                primaryTextColor = this.mBuilder.getPrimaryTextColor(p);
            } else {
                primaryTextColor = this.mBuilder.resolveContrastColor(p);
            }
            contentView.setInt(R.id.status_bar_latest_event_content, "setLayoutColor", primaryTextColor);
            contentView.setInt(R.id.status_bar_latest_event_content, "setSenderTextColor", this.mBuilder.getPrimaryTextColor(p));
            contentView.setInt(R.id.status_bar_latest_event_content, "setMessageTextColor", this.mBuilder.getSecondaryTextColor(p));
            contentView.setBoolean(R.id.status_bar_latest_event_content, "setDisplayImagesAtEnd", displayImagesAtEnd);
            contentView.setIcon(R.id.status_bar_latest_event_content, "setAvatarReplacement", avatarReplacement);
            contentView.setCharSequence(R.id.status_bar_latest_event_content, "setNameReplacement", nameReplacement);
            contentView.setBoolean(R.id.status_bar_latest_event_content, "setIsOneToOne", isOneToOne);
            contentView.setBundle(R.id.status_bar_latest_event_content, "setData", this.mBuilder.mN.extras);
            return contentView;
        }

        private boolean hasOnlyWhiteSpaceSenders() {
            for (int i = 0; i < this.mMessages.size(); i++) {
                Person sender = ((Message) this.mMessages.get(i)).getSenderPerson();
                if (sender != null && !isWhiteSpace(sender.getName())) {
                    return false;
                }
            }
            return true;
        }

        private boolean isWhiteSpace(CharSequence sender) {
            if (TextUtils.isEmpty(sender) || sender.toString().matches("^\\s*$")) {
                return true;
            }
            for (int i = 0; i < sender.length(); i++) {
                if (sender.charAt(i) != 8203) {
                    return false;
                }
            }
            return true;
        }

        private CharSequence createConversationTitleFromMessages() {
            ArraySet<CharSequence> names = new ArraySet();
            for (int i = 0; i < this.mMessages.size(); i++) {
                Person sender = ((Message) this.mMessages.get(i)).getSenderPerson();
                if (sender != null) {
                    names.add(sender.getName());
                }
            }
            SpannableStringBuilder title = new SpannableStringBuilder();
            int size = names.size();
            for (int i2 = 0; i2 < size; i2++) {
                CharSequence name = (CharSequence) names.valueAt(i2);
                if (!TextUtils.isEmpty(title)) {
                    title.append((CharSequence) ", ");
                }
                title.append(BidiFormatter.getInstance().unicodeWrap(name));
            }
            return title;
        }

        public RemoteViews makeHeadsUpContentView(boolean increasedHeight) {
            RemoteViews remoteViews = makeMessagingView(true, true);
            remoteViews.setInt(R.id.notification_messaging, "setMaxDisplayedLines", 1);
            return remoteViews;
        }

        private static TextAppearanceSpan makeFontColorSpan(int color) {
            return new TextAppearanceSpan(null, 0, 0, ColorStateList.valueOf(color), null);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Priority {
    }

    private static class StandardTemplateParams {
        boolean allowColorization;
        boolean forceDefaultColor;
        boolean hasProgress;
        CharSequence headerTextSecondary;
        boolean hideLargeIcon;
        boolean hideReplyIcon;
        int maxRemoteInputHistory;
        CharSequence summaryText;
        CharSequence text;
        CharSequence title;

        private StandardTemplateParams() {
            this.hasProgress = true;
            this.maxRemoteInputHistory = 3;
            this.allowColorization = true;
            this.forceDefaultColor = false;
        }

        /* synthetic */ StandardTemplateParams(AnonymousClass1 x0) {
            this();
        }

        /* Access modifiers changed, original: final */
        public final StandardTemplateParams reset() {
            this.hasProgress = true;
            this.title = null;
            this.text = null;
            this.summaryText = null;
            this.headerTextSecondary = null;
            this.maxRemoteInputHistory = 3;
            this.allowColorization = true;
            this.forceDefaultColor = false;
            return this;
        }

        /* Access modifiers changed, original: final */
        public final StandardTemplateParams hasProgress(boolean hasProgress) {
            this.hasProgress = hasProgress;
            return this;
        }

        /* Access modifiers changed, original: final */
        public final StandardTemplateParams title(CharSequence title) {
            this.title = title;
            return this;
        }

        /* Access modifiers changed, original: final */
        public final StandardTemplateParams text(CharSequence text) {
            this.text = text;
            return this;
        }

        /* Access modifiers changed, original: final */
        public final StandardTemplateParams summaryText(CharSequence text) {
            this.summaryText = text;
            return this;
        }

        /* Access modifiers changed, original: final */
        public final StandardTemplateParams headerTextSecondary(CharSequence text) {
            this.headerTextSecondary = text;
            return this;
        }

        /* Access modifiers changed, original: final */
        public final StandardTemplateParams hideLargeIcon(boolean hideLargeIcon) {
            this.hideLargeIcon = hideLargeIcon;
            return this;
        }

        /* Access modifiers changed, original: final */
        public final StandardTemplateParams hideReplyIcon(boolean hideReplyIcon) {
            this.hideReplyIcon = hideReplyIcon;
            return this;
        }

        /* Access modifiers changed, original: final */
        public final StandardTemplateParams disallowColorization() {
            this.allowColorization = false;
            return this;
        }

        /* Access modifiers changed, original: final */
        public final StandardTemplateParams forceDefaultColor() {
            this.forceDefaultColor = true;
            return this;
        }

        /* Access modifiers changed, original: final */
        public final StandardTemplateParams fillTextsFrom(Builder b) {
            Bundle extras = b.mN.extras;
            this.title = b.processLegacyText(extras.getCharSequence(Notification.EXTRA_TITLE));
            CharSequence text = extras.getCharSequence(Notification.EXTRA_BIG_TEXT);
            if (TextUtils.isEmpty(text)) {
                text = extras.getCharSequence(Notification.EXTRA_TEXT);
            }
            this.text = b.processLegacyText(text);
            this.summaryText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
            return this;
        }

        public StandardTemplateParams setMaxRemoteInputHistory(int maxRemoteInputHistory) {
            this.maxRemoteInputHistory = maxRemoteInputHistory;
            return this;
        }
    }

    private static class TemplateBindResult {
        int mIconMarginEnd;
        boolean mRightIconContainerVisible;

        private TemplateBindResult() {
        }

        /* synthetic */ TemplateBindResult(AnonymousClass1 x0) {
            this();
        }

        public int getIconMarginEnd() {
            return this.mIconMarginEnd;
        }

        public boolean isRightIconContainerVisible() {
            return this.mRightIconContainerVisible;
        }

        public void setIconMarginEnd(int iconMarginEnd) {
            this.mIconMarginEnd = iconMarginEnd;
        }

        public void setRightIconContainerVisible(boolean iconContainerVisible) {
            this.mRightIconContainerVisible = iconContainerVisible;
        }
    }

    @SystemApi
    public static final class TvExtender implements Extender {
        private static final String EXTRA_CHANNEL_ID = "channel_id";
        private static final String EXTRA_CONTENT_INTENT = "content_intent";
        private static final String EXTRA_DELETE_INTENT = "delete_intent";
        private static final String EXTRA_FLAGS = "flags";
        private static final String EXTRA_SUPPRESS_SHOW_OVER_APPS = "suppressShowOverApps";
        private static final String EXTRA_TV_EXTENDER = "android.tv.EXTENSIONS";
        private static final int FLAG_AVAILABLE_ON_TV = 1;
        private static final String TAG = "TvExtender";
        private String mChannelId;
        private PendingIntent mContentIntent;
        private PendingIntent mDeleteIntent;
        private int mFlags;
        private boolean mSuppressShowOverApps;

        public TvExtender() {
            this.mFlags = 1;
        }

        public TvExtender(Notification notif) {
            Bundle bundle = notif.extras == null ? null : notif.extras.getBundle(EXTRA_TV_EXTENDER);
            if (bundle != null) {
                this.mFlags = bundle.getInt("flags");
                this.mChannelId = bundle.getString("channel_id");
                this.mSuppressShowOverApps = bundle.getBoolean(EXTRA_SUPPRESS_SHOW_OVER_APPS);
                this.mContentIntent = (PendingIntent) bundle.getParcelable(EXTRA_CONTENT_INTENT);
                this.mDeleteIntent = (PendingIntent) bundle.getParcelable(EXTRA_DELETE_INTENT);
            }
        }

        public Builder extend(Builder builder) {
            Bundle bundle = new Bundle();
            bundle.putInt("flags", this.mFlags);
            bundle.putString("channel_id", this.mChannelId);
            bundle.putBoolean(EXTRA_SUPPRESS_SHOW_OVER_APPS, this.mSuppressShowOverApps);
            PendingIntent pendingIntent = this.mContentIntent;
            if (pendingIntent != null) {
                bundle.putParcelable(EXTRA_CONTENT_INTENT, pendingIntent);
            }
            pendingIntent = this.mDeleteIntent;
            if (pendingIntent != null) {
                bundle.putParcelable(EXTRA_DELETE_INTENT, pendingIntent);
            }
            builder.getExtras().putBundle(EXTRA_TV_EXTENDER, bundle);
            return builder;
        }

        public boolean isAvailableOnTv() {
            return (this.mFlags & 1) != 0;
        }

        public TvExtender setChannel(String channelId) {
            this.mChannelId = channelId;
            return this;
        }

        public TvExtender setChannelId(String channelId) {
            this.mChannelId = channelId;
            return this;
        }

        @Deprecated
        public String getChannel() {
            return this.mChannelId;
        }

        public String getChannelId() {
            return this.mChannelId;
        }

        public TvExtender setContentIntent(PendingIntent intent) {
            this.mContentIntent = intent;
            return this;
        }

        public PendingIntent getContentIntent() {
            return this.mContentIntent;
        }

        public TvExtender setDeleteIntent(PendingIntent intent) {
            this.mDeleteIntent = intent;
            return this;
        }

        public PendingIntent getDeleteIntent() {
            return this.mDeleteIntent;
        }

        public TvExtender setSuppressShowOverApps(boolean suppress) {
            this.mSuppressShowOverApps = suppress;
            return this;
        }

        public boolean getSuppressShowOverApps() {
            return this.mSuppressShowOverApps;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {
    }

    public static final class WearableExtender implements Extender {
        private static final int DEFAULT_CONTENT_ICON_GRAVITY = 8388613;
        private static final int DEFAULT_FLAGS = 1;
        private static final int DEFAULT_GRAVITY = 80;
        private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
        private static final int FLAG_BIG_PICTURE_AMBIENT = 32;
        private static final int FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE = 1;
        private static final int FLAG_HINT_AVOID_BACKGROUND_CLIPPING = 16;
        private static final int FLAG_HINT_CONTENT_INTENT_LAUNCHES_ACTIVITY = 64;
        private static final int FLAG_HINT_HIDE_ICON = 2;
        private static final int FLAG_HINT_SHOW_BACKGROUND_ONLY = 4;
        private static final int FLAG_START_SCROLL_BOTTOM = 8;
        private static final String KEY_ACTIONS = "actions";
        private static final String KEY_BACKGROUND = "background";
        private static final String KEY_BRIDGE_TAG = "bridgeTag";
        private static final String KEY_CONTENT_ACTION_INDEX = "contentActionIndex";
        private static final String KEY_CONTENT_ICON = "contentIcon";
        private static final String KEY_CONTENT_ICON_GRAVITY = "contentIconGravity";
        private static final String KEY_CUSTOM_CONTENT_HEIGHT = "customContentHeight";
        private static final String KEY_CUSTOM_SIZE_PRESET = "customSizePreset";
        private static final String KEY_DISMISSAL_ID = "dismissalId";
        private static final String KEY_DISPLAY_INTENT = "displayIntent";
        private static final String KEY_FLAGS = "flags";
        private static final String KEY_GRAVITY = "gravity";
        private static final String KEY_HINT_SCREEN_TIMEOUT = "hintScreenTimeout";
        private static final String KEY_PAGES = "pages";
        @Deprecated
        public static final int SCREEN_TIMEOUT_LONG = -1;
        @Deprecated
        public static final int SCREEN_TIMEOUT_SHORT = 0;
        @Deprecated
        public static final int SIZE_DEFAULT = 0;
        @Deprecated
        public static final int SIZE_FULL_SCREEN = 5;
        @Deprecated
        public static final int SIZE_LARGE = 4;
        @Deprecated
        public static final int SIZE_MEDIUM = 3;
        @Deprecated
        public static final int SIZE_SMALL = 2;
        @Deprecated
        public static final int SIZE_XSMALL = 1;
        public static final int UNSET_ACTION_INDEX = -1;
        private ArrayList<Action> mActions = new ArrayList();
        private Bitmap mBackground;
        private String mBridgeTag;
        private int mContentActionIndex = -1;
        private int mContentIcon;
        private int mContentIconGravity = 8388613;
        private int mCustomContentHeight;
        private int mCustomSizePreset = 0;
        private String mDismissalId;
        private PendingIntent mDisplayIntent;
        private int mFlags = 1;
        private int mGravity = 80;
        private int mHintScreenTimeout;
        private ArrayList<Notification> mPages = new ArrayList();

        public WearableExtender(Notification notif) {
            Bundle wearableBundle = notif.extras.getBundle(EXTRA_WEARABLE_EXTENSIONS);
            if (wearableBundle != null) {
                List<Action> actions = wearableBundle.getParcelableArrayList("actions");
                if (actions != null) {
                    this.mActions.addAll(actions);
                }
                this.mFlags = wearableBundle.getInt("flags", 1);
                this.mDisplayIntent = (PendingIntent) wearableBundle.getParcelable(KEY_DISPLAY_INTENT);
                Notification[] pages = Notification.getNotificationArrayFromBundle(wearableBundle, KEY_PAGES);
                if (pages != null) {
                    Collections.addAll(this.mPages, pages);
                }
                this.mBackground = (Bitmap) wearableBundle.getParcelable(KEY_BACKGROUND);
                this.mContentIcon = wearableBundle.getInt(KEY_CONTENT_ICON);
                this.mContentIconGravity = wearableBundle.getInt(KEY_CONTENT_ICON_GRAVITY, 8388613);
                this.mContentActionIndex = wearableBundle.getInt(KEY_CONTENT_ACTION_INDEX, -1);
                this.mCustomSizePreset = wearableBundle.getInt(KEY_CUSTOM_SIZE_PRESET, 0);
                this.mCustomContentHeight = wearableBundle.getInt(KEY_CUSTOM_CONTENT_HEIGHT);
                this.mGravity = wearableBundle.getInt(KEY_GRAVITY, 80);
                this.mHintScreenTimeout = wearableBundle.getInt(KEY_HINT_SCREEN_TIMEOUT);
                this.mDismissalId = wearableBundle.getString(KEY_DISMISSAL_ID);
                this.mBridgeTag = wearableBundle.getString(KEY_BRIDGE_TAG);
            }
        }

        public Builder extend(Builder builder) {
            Bundle wearableBundle = new Bundle();
            if (!this.mActions.isEmpty()) {
                wearableBundle.putParcelableArrayList("actions", this.mActions);
            }
            int i = this.mFlags;
            if (i != 1) {
                wearableBundle.putInt("flags", i);
            }
            PendingIntent pendingIntent = this.mDisplayIntent;
            if (pendingIntent != null) {
                wearableBundle.putParcelable(KEY_DISPLAY_INTENT, pendingIntent);
            }
            if (!this.mPages.isEmpty()) {
                ArrayList arrayList = this.mPages;
                wearableBundle.putParcelableArray(KEY_PAGES, (Parcelable[]) arrayList.toArray(new Notification[arrayList.size()]));
            }
            Bitmap bitmap = this.mBackground;
            if (bitmap != null) {
                wearableBundle.putParcelable(KEY_BACKGROUND, bitmap);
            }
            i = this.mContentIcon;
            if (i != 0) {
                wearableBundle.putInt(KEY_CONTENT_ICON, i);
            }
            i = this.mContentIconGravity;
            if (i != 8388613) {
                wearableBundle.putInt(KEY_CONTENT_ICON_GRAVITY, i);
            }
            i = this.mContentActionIndex;
            if (i != -1) {
                wearableBundle.putInt(KEY_CONTENT_ACTION_INDEX, i);
            }
            i = this.mCustomSizePreset;
            if (i != 0) {
                wearableBundle.putInt(KEY_CUSTOM_SIZE_PRESET, i);
            }
            i = this.mCustomContentHeight;
            if (i != 0) {
                wearableBundle.putInt(KEY_CUSTOM_CONTENT_HEIGHT, i);
            }
            i = this.mGravity;
            if (i != 80) {
                wearableBundle.putInt(KEY_GRAVITY, i);
            }
            i = this.mHintScreenTimeout;
            if (i != 0) {
                wearableBundle.putInt(KEY_HINT_SCREEN_TIMEOUT, i);
            }
            String str = this.mDismissalId;
            if (str != null) {
                wearableBundle.putString(KEY_DISMISSAL_ID, str);
            }
            str = this.mBridgeTag;
            if (str != null) {
                wearableBundle.putString(KEY_BRIDGE_TAG, str);
            }
            builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
            return builder;
        }

        public WearableExtender clone() {
            WearableExtender that = new WearableExtender();
            that.mActions = new ArrayList(this.mActions);
            that.mFlags = this.mFlags;
            that.mDisplayIntent = this.mDisplayIntent;
            that.mPages = new ArrayList(this.mPages);
            that.mBackground = this.mBackground;
            that.mContentIcon = this.mContentIcon;
            that.mContentIconGravity = this.mContentIconGravity;
            that.mContentActionIndex = this.mContentActionIndex;
            that.mCustomSizePreset = this.mCustomSizePreset;
            that.mCustomContentHeight = this.mCustomContentHeight;
            that.mGravity = this.mGravity;
            that.mHintScreenTimeout = this.mHintScreenTimeout;
            that.mDismissalId = this.mDismissalId;
            that.mBridgeTag = this.mBridgeTag;
            return that;
        }

        public WearableExtender addAction(Action action) {
            this.mActions.add(action);
            return this;
        }

        public WearableExtender addActions(List<Action> actions) {
            this.mActions.addAll(actions);
            return this;
        }

        public WearableExtender clearActions() {
            this.mActions.clear();
            return this;
        }

        public List<Action> getActions() {
            return this.mActions;
        }

        @Deprecated
        public WearableExtender setDisplayIntent(PendingIntent intent) {
            this.mDisplayIntent = intent;
            return this;
        }

        @Deprecated
        public PendingIntent getDisplayIntent() {
            return this.mDisplayIntent;
        }

        @Deprecated
        public WearableExtender addPage(Notification page) {
            this.mPages.add(page);
            return this;
        }

        @Deprecated
        public WearableExtender addPages(List<Notification> pages) {
            this.mPages.addAll(pages);
            return this;
        }

        @Deprecated
        public WearableExtender clearPages() {
            this.mPages.clear();
            return this;
        }

        @Deprecated
        public List<Notification> getPages() {
            return this.mPages;
        }

        @Deprecated
        public WearableExtender setBackground(Bitmap background) {
            this.mBackground = background;
            return this;
        }

        @Deprecated
        public Bitmap getBackground() {
            return this.mBackground;
        }

        @Deprecated
        public WearableExtender setContentIcon(int icon) {
            this.mContentIcon = icon;
            return this;
        }

        @Deprecated
        public int getContentIcon() {
            return this.mContentIcon;
        }

        @Deprecated
        public WearableExtender setContentIconGravity(int contentIconGravity) {
            this.mContentIconGravity = contentIconGravity;
            return this;
        }

        @Deprecated
        public int getContentIconGravity() {
            return this.mContentIconGravity;
        }

        public WearableExtender setContentAction(int actionIndex) {
            this.mContentActionIndex = actionIndex;
            return this;
        }

        public int getContentAction() {
            return this.mContentActionIndex;
        }

        @Deprecated
        public WearableExtender setGravity(int gravity) {
            this.mGravity = gravity;
            return this;
        }

        @Deprecated
        public int getGravity() {
            return this.mGravity;
        }

        @Deprecated
        public WearableExtender setCustomSizePreset(int sizePreset) {
            this.mCustomSizePreset = sizePreset;
            return this;
        }

        @Deprecated
        public int getCustomSizePreset() {
            return this.mCustomSizePreset;
        }

        @Deprecated
        public WearableExtender setCustomContentHeight(int height) {
            this.mCustomContentHeight = height;
            return this;
        }

        @Deprecated
        public int getCustomContentHeight() {
            return this.mCustomContentHeight;
        }

        public WearableExtender setStartScrollBottom(boolean startScrollBottom) {
            setFlag(8, startScrollBottom);
            return this;
        }

        public boolean getStartScrollBottom() {
            return (this.mFlags & 8) != 0;
        }

        public WearableExtender setContentIntentAvailableOffline(boolean contentIntentAvailableOffline) {
            setFlag(1, contentIntentAvailableOffline);
            return this;
        }

        public boolean getContentIntentAvailableOffline() {
            return (this.mFlags & 1) != 0;
        }

        @Deprecated
        public WearableExtender setHintHideIcon(boolean hintHideIcon) {
            setFlag(2, hintHideIcon);
            return this;
        }

        @Deprecated
        public boolean getHintHideIcon() {
            return (this.mFlags & 2) != 0;
        }

        @Deprecated
        public WearableExtender setHintShowBackgroundOnly(boolean hintShowBackgroundOnly) {
            setFlag(4, hintShowBackgroundOnly);
            return this;
        }

        @Deprecated
        public boolean getHintShowBackgroundOnly() {
            return (this.mFlags & 4) != 0;
        }

        @Deprecated
        public WearableExtender setHintAvoidBackgroundClipping(boolean hintAvoidBackgroundClipping) {
            setFlag(16, hintAvoidBackgroundClipping);
            return this;
        }

        @Deprecated
        public boolean getHintAvoidBackgroundClipping() {
            return (this.mFlags & 16) != 0;
        }

        @Deprecated
        public WearableExtender setHintScreenTimeout(int timeout) {
            this.mHintScreenTimeout = timeout;
            return this;
        }

        @Deprecated
        public int getHintScreenTimeout() {
            return this.mHintScreenTimeout;
        }

        @Deprecated
        public WearableExtender setHintAmbientBigPicture(boolean hintAmbientBigPicture) {
            setFlag(32, hintAmbientBigPicture);
            return this;
        }

        @Deprecated
        public boolean getHintAmbientBigPicture() {
            return (this.mFlags & 32) != 0;
        }

        public WearableExtender setHintContentIntentLaunchesActivity(boolean hintContentIntentLaunchesActivity) {
            setFlag(64, hintContentIntentLaunchesActivity);
            return this;
        }

        public boolean getHintContentIntentLaunchesActivity() {
            return (this.mFlags & 64) != 0;
        }

        public WearableExtender setDismissalId(String dismissalId) {
            this.mDismissalId = dismissalId;
            return this;
        }

        public String getDismissalId() {
            return this.mDismissalId;
        }

        public WearableExtender setBridgeTag(String bridgeTag) {
            this.mBridgeTag = bridgeTag;
            return this;
        }

        public String getBridgeTag() {
            return this.mBridgeTag;
        }

        private void setFlag(int mask, boolean value) {
            if (value) {
                this.mFlags |= mask;
            } else {
                this.mFlags &= ~mask;
            }
        }
    }

    static {
        STANDARD_LAYOUTS.add(Integer.valueOf(R.layout.notification_template_material_base));
        STANDARD_LAYOUTS.add(Integer.valueOf(R.layout.notification_template_material_big_base));
        STANDARD_LAYOUTS.add(Integer.valueOf(R.layout.notification_template_material_big_picture));
        STANDARD_LAYOUTS.add(Integer.valueOf(R.layout.notification_template_material_big_text));
        STANDARD_LAYOUTS.add(Integer.valueOf(R.layout.notification_template_material_inbox));
        STANDARD_LAYOUTS.add(Integer.valueOf(R.layout.notification_template_material_messaging));
        STANDARD_LAYOUTS.add(Integer.valueOf(R.layout.notification_template_material_media));
        STANDARD_LAYOUTS.add(Integer.valueOf(R.layout.notification_template_material_big_media));
        STANDARD_LAYOUTS.add(Integer.valueOf(R.layout.notification_template_header));
    }

    public String getGroup() {
        return this.mGroupKey;
    }

    public String getSortKey() {
        return this.mSortKey;
    }

    public Notification() {
        this.number = 0;
        this.audioStreamType = -1;
        this.audioAttributes = AUDIO_ATTRIBUTES_DEFAULT;
        this.color = 0;
        this.extras = new Bundle();
        this.mGroupAlertBehavior = 0;
        this.mBadgeIcon = 0;
        this.mAllowSystemGeneratedContextualActions = true;
        this.extraNotification = new MiuiNotification();
        this.when = System.currentTimeMillis();
        this.creationTime = System.currentTimeMillis();
        this.priority = 0;
    }

    @UnsupportedAppUsage
    public Notification(Context context, int icon, CharSequence tickerText, long when, CharSequence contentTitle, CharSequence contentText, Intent contentIntent) {
        this.number = 0;
        this.audioStreamType = -1;
        this.audioAttributes = AUDIO_ATTRIBUTES_DEFAULT;
        this.color = 0;
        this.extras = new Bundle();
        this.mGroupAlertBehavior = 0;
        this.mBadgeIcon = 0;
        this.mAllowSystemGeneratedContextualActions = true;
        this.extraNotification = new MiuiNotification();
        new Builder(context).setWhen(when).setSmallIcon(icon).setTicker(tickerText).setContentTitle(contentTitle).setContentText(contentText).setContentIntent(PendingIntent.getActivity(context, 0, contentIntent, 0)).buildInto(this);
    }

    @Deprecated
    public Notification(int icon, CharSequence tickerText, long when) {
        this.number = 0;
        this.audioStreamType = -1;
        this.audioAttributes = AUDIO_ATTRIBUTES_DEFAULT;
        this.color = 0;
        this.extras = new Bundle();
        this.mGroupAlertBehavior = 0;
        this.mBadgeIcon = 0;
        this.mAllowSystemGeneratedContextualActions = true;
        this.extraNotification = new MiuiNotification();
        this.icon = icon;
        this.tickerText = tickerText;
        this.when = when;
        this.creationTime = System.currentTimeMillis();
    }

    public Notification(Parcel parcel) {
        this.number = 0;
        this.audioStreamType = -1;
        this.audioAttributes = AUDIO_ATTRIBUTES_DEFAULT;
        this.color = 0;
        this.extras = new Bundle();
        this.mGroupAlertBehavior = 0;
        this.mBadgeIcon = 0;
        this.mAllowSystemGeneratedContextualActions = true;
        this.extraNotification = new MiuiNotification();
        readFromParcelImpl(parcel);
        this.allPendingIntents = parcel.readArraySet(null);
    }

    private void readFromParcelImpl(Parcel parcel) {
        int version = parcel.readInt();
        this.mWhitelistToken = parcel.readStrongBinder();
        if (this.mWhitelistToken == null) {
            this.mWhitelistToken = processWhitelistToken;
        }
        parcel.setClassCookie(PendingIntent.class, this.mWhitelistToken);
        this.when = parcel.readLong();
        this.creationTime = parcel.readLong();
        if (parcel.readInt() != 0) {
            this.mSmallIcon = (Icon) Icon.CREATOR.createFromParcel(parcel);
            if (this.mSmallIcon.getType() == 2) {
                this.icon = this.mSmallIcon.getResId();
            }
        }
        this.number = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.contentIntent = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.deleteIntent = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.tickerText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.tickerView = (RemoteViews) RemoteViews.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.contentView = (RemoteViews) RemoteViews.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.mLargeIcon = (Icon) Icon.CREATOR.createFromParcel(parcel);
        }
        this.defaults = parcel.readInt();
        this.flags = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.sound = (Uri) Uri.CREATOR.createFromParcel(parcel);
        }
        this.audioStreamType = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.audioAttributes = (AudioAttributes) AudioAttributes.CREATOR.createFromParcel(parcel);
        }
        this.vibrate = parcel.createLongArray();
        this.ledARGB = parcel.readInt();
        this.ledOnMS = parcel.readInt();
        this.ledOffMS = parcel.readInt();
        this.iconLevel = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.fullScreenIntent = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
        }
        this.priority = parcel.readInt();
        this.category = parcel.readString();
        this.mGroupKey = parcel.readString();
        this.mSortKey = parcel.readString();
        this.extras = Bundle.setDefusable(parcel.readBundle(), true);
        fixDuplicateExtras();
        this.actions = (Action[]) parcel.createTypedArray(Action.CREATOR);
        if (parcel.readInt() != 0) {
            this.bigContentView = (RemoteViews) RemoteViews.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.headsUpContentView = (RemoteViews) RemoteViews.CREATOR.createFromParcel(parcel);
        }
        this.visibility = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.publicVersion = (Notification) CREATOR.createFromParcel(parcel);
        }
        this.color = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.mChannelId = parcel.readString();
        }
        this.mTimeout = parcel.readLong();
        if (parcel.readInt() != 0) {
            this.mShortcutId = parcel.readString();
        }
        if (parcel.readInt() != 0) {
            this.mLocusId = (LocusId) LocusId.CREATOR.createFromParcel(parcel);
        }
        this.mBadgeIcon = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.mSettingsText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        }
        this.mGroupAlertBehavior = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.mBubbleMetadata = (BubbleMetadata) BubbleMetadata.CREATOR.createFromParcel(parcel);
        }
        this.mAllowSystemGeneratedContextualActions = parcel.readBoolean();
        this.extraNotification.readFromParcel(parcel);
    }

    public Notification clone() {
        Notification that = new Notification();
        cloneInto(that, true);
        return that;
    }

    public void cloneInto(Notification that, boolean heavy) {
        RemoteViews remoteViews;
        int N;
        RemoteViews remoteViews2;
        that.mWhitelistToken = this.mWhitelistToken;
        that.when = this.when;
        that.creationTime = this.creationTime;
        that.mSmallIcon = this.mSmallIcon;
        that.number = this.number;
        that.contentIntent = this.contentIntent;
        that.deleteIntent = this.deleteIntent;
        that.fullScreenIntent = this.fullScreenIntent;
        CharSequence charSequence = this.tickerText;
        if (charSequence != null) {
            that.tickerText = charSequence.toString();
        }
        if (heavy) {
            remoteViews = this.tickerView;
            if (remoteViews != null) {
                that.tickerView = remoteViews.clone();
            }
        }
        if (heavy) {
            remoteViews = this.contentView;
            if (remoteViews != null) {
                that.contentView = remoteViews.clone();
            }
        }
        if (heavy) {
            Icon icon = this.mLargeIcon;
            if (icon != null) {
                that.mLargeIcon = icon;
            }
        }
        that.iconLevel = this.iconLevel;
        that.sound = this.sound;
        that.audioStreamType = this.audioStreamType;
        AudioAttributes audioAttributes = this.audioAttributes;
        if (audioAttributes != null) {
            that.audioAttributes = new android.media.AudioAttributes.Builder(audioAttributes).build();
        }
        long[] vibrate = this.vibrate;
        if (vibrate != null) {
            N = vibrate.length;
            long[] vib = new long[N];
            that.vibrate = vib;
            System.arraycopy(vibrate, 0, vib, 0, N);
        }
        that.ledARGB = this.ledARGB;
        that.ledOnMS = this.ledOnMS;
        that.ledOffMS = this.ledOffMS;
        that.defaults = this.defaults;
        that.flags = this.flags;
        that.priority = this.priority;
        that.category = this.category;
        that.mGroupKey = this.mGroupKey;
        that.mSortKey = this.mSortKey;
        Bundle bundle = this.extras;
        if (bundle != null) {
            try {
                that.extras = new Bundle(bundle);
                that.extras.size();
            } catch (BadParcelableException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("could not unparcel extras from notification: ");
                stringBuilder.append(this);
                Log.e(TAG, stringBuilder.toString(), e);
                that.extras = null;
            }
        }
        if (!ArrayUtils.isEmpty(this.allPendingIntents)) {
            that.allPendingIntents = new ArraySet(this.allPendingIntents);
        }
        Action[] actionArr = this.actions;
        if (actionArr != null) {
            that.actions = new Action[actionArr.length];
            N = 0;
            while (true) {
                Action[] actionArr2 = this.actions;
                if (N >= actionArr2.length) {
                    break;
                }
                if (actionArr2[N] != null) {
                    that.actions[N] = actionArr2[N].clone();
                }
                N++;
            }
        }
        if (heavy) {
            remoteViews2 = this.bigContentView;
            if (remoteViews2 != null) {
                that.bigContentView = remoteViews2.clone();
            }
        }
        if (heavy) {
            remoteViews2 = this.headsUpContentView;
            if (remoteViews2 != null) {
                that.headsUpContentView = remoteViews2.clone();
            }
        }
        that.visibility = this.visibility;
        if (this.publicVersion != null) {
            that.publicVersion = new Notification();
            this.publicVersion.cloneInto(that.publicVersion, heavy);
        }
        that.color = this.color;
        that.mChannelId = this.mChannelId;
        that.mTimeout = this.mTimeout;
        that.mShortcutId = this.mShortcutId;
        that.mLocusId = this.mLocusId;
        that.mBadgeIcon = this.mBadgeIcon;
        that.mSettingsText = this.mSettingsText;
        that.mGroupAlertBehavior = this.mGroupAlertBehavior;
        that.mBubbleMetadata = this.mBubbleMetadata;
        that.mAllowSystemGeneratedContextualActions = this.mAllowSystemGeneratedContextualActions;
        if (!heavy) {
            that.lightenPayload();
        }
        that.extraNotification.setTo(this.extraNotification);
    }

    public void visitUris(Consumer<Uri> visitor) {
        visitor.accept(this.sound);
        RemoteViews remoteViews = this.tickerView;
        if (remoteViews != null) {
            remoteViews.visitUris(visitor);
        }
        remoteViews = this.contentView;
        if (remoteViews != null) {
            remoteViews.visitUris(visitor);
        }
        remoteViews = this.bigContentView;
        if (remoteViews != null) {
            remoteViews.visitUris(visitor);
        }
        remoteViews = this.headsUpContentView;
        if (remoteViews != null) {
            remoteViews.visitUris(visitor);
        }
        Bundle bundle = this.extras;
        if (bundle != null) {
            visitor.accept((Uri) bundle.getParcelable(EXTRA_AUDIO_CONTENTS_URI));
            bundle = this.extras;
            String str = EXTRA_BACKGROUND_IMAGE_URI;
            if (bundle.containsKey(str)) {
                visitor.accept(Uri.parse(this.extras.getString(str)));
            }
        }
        if (MessagingStyle.class.equals(getNotificationStyle())) {
            Parcelable[] messages = this.extras;
            if (messages != null) {
                Object[] messages2 = messages.getParcelableArray(EXTRA_MESSAGES);
                if (!ArrayUtils.isEmpty(messages2)) {
                    for (Message message : Message.getMessagesFromBundleArray(messages2)) {
                        visitor.accept(message.getDataUri());
                    }
                }
                Object[] historic = this.extras.getParcelableArray(EXTRA_HISTORIC_MESSAGES);
                if (!ArrayUtils.isEmpty(historic)) {
                    for (Message message2 : Message.getMessagesFromBundleArray(historic)) {
                        visitor.accept(message2.getDataUri());
                    }
                }
            }
        }
    }

    public final void lightenPayload() {
        this.tickerView = null;
        this.contentView = null;
        this.bigContentView = null;
        this.headsUpContentView = null;
        this.mLargeIcon = null;
        Bundle bundle = this.extras;
        if (bundle != null && !bundle.isEmpty()) {
            Set<String> keyset = this.extras.keySet();
            int N = keyset.size();
            String[] keys = (String[]) keyset.toArray(new String[N]);
            for (int i = 0; i < N; i++) {
                String key = keys[i];
                if (!"android.tv.EXTENSIONS".equals(key)) {
                    Object obj = this.extras.get(key);
                    if (obj != null && ((obj instanceof Parcelable) || (obj instanceof Parcelable[]) || (obj instanceof SparseArray) || (obj instanceof ArrayList))) {
                        this.extras.remove(key);
                    }
                }
            }
        }
    }

    public static CharSequence safeCharSequence(CharSequence cs) {
        if (cs == null) {
            return cs;
        }
        Object cs2;
        if (cs2.length() > 5120) {
            cs2 = cs2.subSequence(0, 5120);
        }
        if (!(cs2 instanceof Parcelable)) {
            return removeTextSizeSpans(cs2);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("warning: ");
        stringBuilder.append(cs2.getClass().getCanonicalName());
        stringBuilder.append(" instance is a custom Parcelable and not allowed in Notification");
        Log.e(TAG, stringBuilder.toString());
        return cs2.toString();
    }

    private static CharSequence removeTextSizeSpans(CharSequence charSequence) {
        if (!(charSequence instanceof Spanned)) {
            return charSequence;
        }
        Spanned ss = (Spanned) charSequence;
        int i = 0;
        Object[] spans = ss.getSpans(0, ss.length(), Object.class);
        SpannableStringBuilder builder = new SpannableStringBuilder(ss.toString());
        int length = spans.length;
        while (i < length) {
            Object resultSpan;
            TextAppearanceSpan span = spans[i];
            TextAppearanceSpan resultSpan2 = span;
            if (resultSpan2 instanceof CharacterStyle) {
                resultSpan2 = span.getUnderlying();
            }
            if (resultSpan2 instanceof TextAppearanceSpan) {
                TextAppearanceSpan originalSpan = resultSpan2;
                resultSpan = new TextAppearanceSpan(originalSpan.getFamily(), originalSpan.getTextStyle(), -1, originalSpan.getTextColor(), originalSpan.getLinkTextColor());
            } else {
                if (!((resultSpan2 instanceof RelativeSizeSpan) || (resultSpan2 instanceof AbsoluteSizeSpan))) {
                    resultSpan = span;
                }
                i++;
            }
            builder.setSpan(resultSpan, ss.getSpanStart(span), ss.getSpanEnd(span), ss.getSpanFlags(span));
            i++;
        }
        return builder;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        boolean collectPendingIntents = this.allPendingIntents == null;
        if (collectPendingIntents) {
            PendingIntent.setOnMarshaledListener(new -$$Lambda$Notification$hOCsSZH8tWalFSbIzQ9x9IcPa9M(this, parcel));
        }
        try {
            writeToParcelImpl(parcel, flags);
            synchronized (this) {
                parcel.writeArraySet(this.allPendingIntents);
            }
            if (collectPendingIntents) {
                PendingIntent.setOnMarshaledListener(null);
            }
        } catch (Throwable th) {
            if (collectPendingIntents) {
                PendingIntent.setOnMarshaledListener(null);
            }
        }
    }

    public /* synthetic */ void lambda$writeToParcel$0$Notification(Parcel parcel, PendingIntent intent, Parcel out, int outFlags) {
        if (parcel == out) {
            synchronized (this) {
                if (this.allPendingIntents == null) {
                    this.allPendingIntents = new ArraySet();
                }
                this.allPendingIntents.add(intent);
            }
        }
    }

    private void writeToParcelImpl(Parcel parcel, int flags) {
        parcel.writeInt(1);
        parcel.writeStrongBinder(this.mWhitelistToken);
        parcel.writeLong(this.when);
        parcel.writeLong(this.creationTime);
        if (this.mSmallIcon == null) {
            int i = this.icon;
            if (i != 0) {
                this.mSmallIcon = Icon.createWithResource("", i);
            }
        }
        if (this.mSmallIcon != null) {
            parcel.writeInt(1);
            this.mSmallIcon.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.number);
        if (this.contentIntent != null) {
            parcel.writeInt(1);
            this.contentIntent.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        if (this.deleteIntent != null) {
            parcel.writeInt(1);
            this.deleteIntent.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        if (this.tickerText != null) {
            parcel.writeInt(1);
            TextUtils.writeToParcel(this.tickerText, parcel, flags);
        } else {
            parcel.writeInt(0);
        }
        if (this.tickerView != null) {
            parcel.writeInt(1);
            this.tickerView.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        if (this.contentView != null) {
            parcel.writeInt(1);
            this.contentView.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        if (this.mLargeIcon == null) {
            Bitmap bitmap = this.largeIcon;
            if (bitmap != null) {
                this.mLargeIcon = Icon.createWithBitmap(bitmap);
            }
        }
        if (this.mLargeIcon != null) {
            parcel.writeInt(1);
            this.mLargeIcon.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.defaults);
        parcel.writeInt(this.flags);
        if (this.sound != null) {
            parcel.writeInt(1);
            this.sound.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.audioStreamType);
        if (this.audioAttributes != null) {
            parcel.writeInt(1);
            this.audioAttributes.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeLongArray(this.vibrate);
        parcel.writeInt(this.ledARGB);
        parcel.writeInt(this.ledOnMS);
        parcel.writeInt(this.ledOffMS);
        parcel.writeInt(this.iconLevel);
        if (this.fullScreenIntent != null) {
            parcel.writeInt(1);
            this.fullScreenIntent.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.priority);
        parcel.writeString(this.category);
        parcel.writeString(this.mGroupKey);
        parcel.writeString(this.mSortKey);
        parcel.writeBundle(this.extras);
        parcel.writeTypedArray(this.actions, 0);
        if (this.bigContentView != null) {
            parcel.writeInt(1);
            this.bigContentView.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        if (this.headsUpContentView != null) {
            parcel.writeInt(1);
            this.headsUpContentView.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.visibility);
        if (this.publicVersion != null) {
            parcel.writeInt(1);
            this.publicVersion.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.color);
        if (this.mChannelId != null) {
            parcel.writeInt(1);
            parcel.writeString(this.mChannelId);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeLong(this.mTimeout);
        if (this.mShortcutId != null) {
            parcel.writeInt(1);
            parcel.writeString(this.mShortcutId);
        } else {
            parcel.writeInt(0);
        }
        if (this.mLocusId != null) {
            parcel.writeInt(1);
            this.mLocusId.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.mBadgeIcon);
        if (this.mSettingsText != null) {
            parcel.writeInt(1);
            TextUtils.writeToParcel(this.mSettingsText, parcel, flags);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.mGroupAlertBehavior);
        if (this.mBubbleMetadata != null) {
            parcel.writeInt(1);
            this.mBubbleMetadata.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeBoolean(this.mAllowSystemGeneratedContextualActions);
        this.extraNotification.writeToParcel(parcel, flags);
    }

    public static boolean areActionsVisiblyDifferent(Notification first, Notification second) {
        Action[] firstAs = first.actions;
        Action[] secondAs = second.actions;
        if ((firstAs == null && secondAs != null) || (firstAs != null && secondAs == null)) {
            return true;
        }
        if (!(firstAs == null || secondAs == null)) {
            if (firstAs.length != secondAs.length) {
                return true;
            }
            for (int i = 0; i < firstAs.length; i++) {
                if (!Objects.equals(String.valueOf(firstAs[i].title), String.valueOf(secondAs[i].title))) {
                    return true;
                }
                RemoteInput[] firstRs = firstAs[i].getRemoteInputs();
                RemoteInput[] secondRs = secondAs[i].getRemoteInputs();
                if (firstRs == null) {
                    firstRs = new RemoteInput[0];
                }
                if (secondRs == null) {
                    secondRs = new RemoteInput[0];
                }
                if (firstRs.length != secondRs.length) {
                    return true;
                }
                for (int j = 0; j < firstRs.length; j++) {
                    if (!Objects.equals(String.valueOf(firstRs[j].getLabel()), String.valueOf(secondRs[j].getLabel()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean areStyledNotificationsVisiblyDifferent(Builder first, Builder second) {
        boolean z = true;
        if (first.getStyle() == null) {
            if (second.getStyle() == null) {
                z = false;
            }
            return z;
        } else if (second.getStyle() == null) {
            return true;
        } else {
            return first.getStyle().areNotificationsVisiblyDifferent(second.getStyle());
        }
    }

    public static boolean areRemoteViewsChanged(Builder first, Builder second) {
        if (!Objects.equals(Boolean.valueOf(first.usesStandardHeader()), Boolean.valueOf(second.usesStandardHeader())) || areRemoteViewsChanged(first.mN.contentView, second.mN.contentView) || areRemoteViewsChanged(first.mN.bigContentView, second.mN.bigContentView) || areRemoteViewsChanged(first.mN.headsUpContentView, second.mN.headsUpContentView)) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: Missing block: B:9:0x000f, code skipped:
            return true;
     */
    private static boolean areRemoteViewsChanged(android.widget.RemoteViews r4, android.widget.RemoteViews r5) {
        /*
        r0 = 0;
        if (r4 != 0) goto L_0x0006;
    L_0x0003:
        if (r5 != 0) goto L_0x0006;
    L_0x0005:
        return r0;
    L_0x0006:
        r1 = 1;
        if (r4 != 0) goto L_0x000b;
    L_0x0009:
        if (r5 != 0) goto L_0x000f;
    L_0x000b:
        if (r4 == 0) goto L_0x0010;
    L_0x000d:
        if (r5 != 0) goto L_0x0010;
    L_0x000f:
        return r1;
    L_0x0010:
        r2 = r4.getLayoutId();
        r2 = java.lang.Integer.valueOf(r2);
        r3 = r5.getLayoutId();
        r3 = java.lang.Integer.valueOf(r3);
        r2 = java.util.Objects.equals(r2, r3);
        if (r2 != 0) goto L_0x0027;
    L_0x0026:
        return r1;
    L_0x0027:
        r2 = r4.getSequenceNumber();
        r2 = java.lang.Integer.valueOf(r2);
        r3 = r5.getSequenceNumber();
        r3 = java.lang.Integer.valueOf(r3);
        r2 = java.util.Objects.equals(r2, r3);
        if (r2 != 0) goto L_0x003e;
    L_0x003d:
        return r1;
    L_0x003e:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Notification.areRemoteViewsChanged(android.widget.RemoteViews, android.widget.RemoteViews):boolean");
    }

    private void fixDuplicateExtras() {
        if (this.extras != null) {
            fixDuplicateExtra(this.mLargeIcon, EXTRA_LARGE_ICON);
        }
    }

    private void fixDuplicateExtra(Parcelable original, String extraName) {
        if (original != null && this.extras.getParcelable(extraName) != null) {
            this.extras.putParcelable(extraName, original);
        }
    }

    @Deprecated
    public void setLatestEventInfo(Context context, CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent) {
        if (context.getApplicationInfo().targetSdkVersion > 22) {
            Log.e(TAG, "setLatestEventInfo() is deprecated and you should feel deprecated.", new Throwable());
        }
        if (context.getApplicationInfo().targetSdkVersion < 24) {
            this.extras.putBoolean(EXTRA_SHOW_WHEN, true);
        }
        Builder builder = new Builder(context, this);
        if (contentTitle != null) {
            builder.setContentTitle(contentTitle);
        }
        if (contentText != null) {
            builder.setContentText(contentText);
        }
        builder.setContentIntent(contentIntent);
        builder.build();
    }

    public static void addFieldsFromContext(Context context, Notification notification) {
        addFieldsFromContext(context.getApplicationInfo(), notification);
    }

    public static void addFieldsFromContext(ApplicationInfo ai, Notification notification) {
        notification.extras.putParcelable(EXTRA_BUILDER_APPLICATION_INFO, ai);
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1138166333441L, getChannelId());
        proto.write(1133871366146L, this.tickerText != null);
        proto.write(1120986464259L, this.flags);
        proto.write(1120986464260L, this.color);
        proto.write(1138166333445L, this.category);
        proto.write(1138166333446L, this.mGroupKey);
        proto.write(1138166333447L, this.mSortKey);
        Action[] actionArr = this.actions;
        if (actionArr != null) {
            proto.write(1120986464264L, actionArr.length);
        }
        int i = this.visibility;
        if (i >= -1 && i <= 1) {
            proto.write(1159641169929L, i);
        }
        Notification notification = this.publicVersion;
        if (notification != null) {
            notification.writeToProto(proto, 1146756268042L);
        }
        proto.end(token);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Notification(channel=");
        sb.append(getChannelId());
        sb.append(" pri=");
        sb.append(this.priority);
        sb.append(" contentView=");
        RemoteViews remoteViews = this.contentView;
        String str = "null";
        if (remoteViews != null) {
            sb.append(remoteViews.getPackage());
            sb.append("/0x");
            sb.append(Integer.toHexString(this.contentView.getLayoutId()));
        } else {
            sb.append(str);
        }
        sb.append(" vibrate=");
        String str2 = "default";
        if ((this.defaults & 2) != 0) {
            sb.append(str2);
        } else {
            long[] jArr = this.vibrate;
            if (jArr != null) {
                int N = jArr.length - 1;
                sb.append("[");
                for (int i = 0; i < N; i++) {
                    sb.append(this.vibrate[i]);
                    sb.append(',');
                }
                if (N != -1) {
                    sb.append(this.vibrate[N]);
                }
                sb.append("]");
            } else {
                sb.append(str);
            }
        }
        sb.append(" sound=");
        if ((this.defaults & 1) != 0) {
            sb.append(str2);
        } else {
            Uri uri = this.sound;
            if (uri != null) {
                sb.append(uri.toString());
            } else {
                sb.append(str);
            }
        }
        if (this.tickerText != null) {
            sb.append(" tick");
        }
        sb.append(" defaults=0x");
        sb.append(Integer.toHexString(this.defaults));
        sb.append(" flags=0x");
        sb.append(Integer.toHexString(this.flags));
        sb.append(String.format(" color=0x%08x", new Object[]{Integer.valueOf(this.color)}));
        if (this.category != null) {
            sb.append(" category=");
            sb.append(this.category);
        }
        if (this.mGroupKey != null) {
            sb.append(" groupKey=");
            sb.append(this.mGroupKey);
        }
        if (this.mSortKey != null) {
            sb.append(" sortKey=");
            sb.append(this.mSortKey);
        }
        if (this.actions != null) {
            sb.append(" actions=");
            sb.append(this.actions.length);
        }
        sb.append(" vis=");
        sb.append(visibilityToString(this.visibility));
        if (this.publicVersion != null) {
            sb.append(" publicVersion=");
            sb.append(this.publicVersion.toString());
        }
        if (this.mLocusId != null) {
            sb.append(" locusId=");
            sb.append(this.mLocusId);
        }
        sb.append(")");
        return sb.toString();
    }

    public static String visibilityToString(int vis) {
        if (vis == -1) {
            return "SECRET";
        }
        if (vis == 0) {
            return "PRIVATE";
        }
        if (vis == 1) {
            return "PUBLIC";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UNKNOWN(");
        stringBuilder.append(String.valueOf(vis));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public static String priorityToString(int pri) {
        if (pri == -2) {
            return "MIN";
        }
        if (pri == -1) {
            return "LOW";
        }
        if (pri == 0) {
            return "DEFAULT";
        }
        if (pri == 1) {
            return "HIGH";
        }
        if (pri == 2) {
            return "MAX";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UNKNOWN(");
        stringBuilder.append(String.valueOf(pri));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public boolean hasCompletedProgress() {
        Bundle bundle = this.extras;
        String str = EXTRA_PROGRESS;
        boolean z = false;
        if (bundle.containsKey(str)) {
            bundle = this.extras;
            String str2 = EXTRA_PROGRESS_MAX;
            if (!bundle.containsKey(str2) || this.extras.getInt(str2) == 0) {
                return false;
            }
            if (this.extras.getInt(str) == this.extras.getInt(str2)) {
                z = true;
            }
            return z;
        }
        return false;
    }

    @Deprecated
    public String getChannel() {
        return this.mChannelId;
    }

    public String getChannelId() {
        return this.mChannelId;
    }

    @Deprecated
    public long getTimeout() {
        return this.mTimeout;
    }

    public long getTimeoutAfter() {
        return this.mTimeout;
    }

    public int getBadgeIconType() {
        return this.mBadgeIcon;
    }

    public String getShortcutId() {
        return this.mShortcutId;
    }

    public LocusId getLocusId() {
        return this.mLocusId;
    }

    public CharSequence getSettingsText() {
        return this.mSettingsText;
    }

    public int getGroupAlertBehavior() {
        return this.mGroupAlertBehavior;
    }

    public BubbleMetadata getBubbleMetadata() {
        return this.mBubbleMetadata;
    }

    public boolean getAllowSystemGeneratedContextualActions() {
        return this.mAllowSystemGeneratedContextualActions;
    }

    public Icon getSmallIcon() {
        return this.mSmallIcon;
    }

    @UnsupportedAppUsage
    public void setSmallIcon(Icon icon) {
        this.mSmallIcon = icon;
    }

    public Icon getLargeIcon() {
        return this.mLargeIcon;
    }

    @UnsupportedAppUsage
    public boolean isGroupSummary() {
        return (this.mGroupKey == null || (this.flags & 512) == 0) ? false : true;
    }

    @UnsupportedAppUsage
    public boolean isGroupChild() {
        return this.mGroupKey != null && (this.flags & 512) == 0;
    }

    public boolean suppressAlertingDueToGrouping() {
        if (isGroupSummary() && getGroupAlertBehavior() == 2) {
            return true;
        }
        if (isGroupChild() && getGroupAlertBehavior() == 1) {
            return true;
        }
        return false;
    }

    public Pair<RemoteInput, Action> findRemoteInputActionPair(boolean requiresFreeform) {
        Action[] actionArr = this.actions;
        if (actionArr == null) {
            return null;
        }
        for (Action action : actionArr) {
            if (action.getRemoteInputs() != null) {
                RemoteInput resultRemoteInput = null;
                for (RemoteInput remoteInput : action.getRemoteInputs()) {
                    if (remoteInput.getAllowFreeFormInput() || !requiresFreeform) {
                        resultRemoteInput = remoteInput;
                    }
                }
                if (resultRemoteInput != null) {
                    return Pair.create(resultRemoteInput, action);
                }
            }
        }
        return null;
    }

    public List<Action> getContextualActions() {
        if (this.actions == null) {
            return Collections.emptyList();
        }
        List<Action> contextualActions = new ArrayList();
        for (Action action : this.actions) {
            if (action.isContextual()) {
                contextualActions.add(action);
            }
        }
        return contextualActions;
    }

    /* Access modifiers changed, original: 0000 */
    public void reduceImageSizes(Context context) {
        Bundle bundle = this.extras;
        String str = EXTRA_REDUCED_IMAGES;
        if (!bundle.getBoolean(str)) {
            boolean isLowRam = ActivityManager.isLowRamDeviceStatic();
            if (!(this.mLargeIcon == null && this.largeIcon == null)) {
                int maxWidth;
                Resources resources = context.getResources();
                Class<? extends Style> style = getNotificationStyle();
                if (isLowRam) {
                    maxWidth = R.dimen.notification_right_icon_size_low_ram;
                } else {
                    maxWidth = R.dimen.notification_right_icon_size;
                }
                maxWidth = resources.getDimensionPixelSize(maxWidth);
                int maxHeight = maxWidth;
                if (MediaStyle.class.equals(style) || DecoratedMediaCustomViewStyle.class.equals(style)) {
                    int i;
                    if (isLowRam) {
                        i = R.dimen.notification_media_image_max_height_low_ram;
                    } else {
                        i = R.dimen.notification_media_image_max_height;
                    }
                    maxHeight = resources.getDimensionPixelSize(i);
                    if (isLowRam) {
                        i = R.dimen.notification_media_image_max_width_low_ram;
                    } else {
                        i = R.dimen.notification_media_image_max_width;
                    }
                    maxWidth = resources.getDimensionPixelSize(i);
                }
                Icon icon = this.mLargeIcon;
                if (icon != null) {
                    icon.scaleDownIfNecessary(maxWidth, maxHeight);
                }
                Bitmap bitmap = this.largeIcon;
                if (bitmap != null) {
                    this.largeIcon = Icon.scaleDownIfNecessary(bitmap, maxWidth, maxHeight);
                }
            }
            reduceImageSizesForRemoteView(this.contentView, context, isLowRam);
            reduceImageSizesForRemoteView(this.headsUpContentView, context, isLowRam);
            reduceImageSizesForRemoteView(this.bigContentView, context, isLowRam);
            this.extras.putBoolean(str, true);
        }
    }

    private void reduceImageSizesForRemoteView(RemoteViews remoteView, Context context, boolean isLowRam) {
        if (remoteView != null) {
            int maxWidth;
            int maxHeight;
            Resources resources = context.getResources();
            if (isLowRam) {
                maxWidth = R.dimen.notification_custom_view_max_image_width_low_ram;
            } else {
                maxWidth = R.dimen.notification_custom_view_max_image_width;
            }
            maxWidth = resources.getDimensionPixelSize(maxWidth);
            if (isLowRam) {
                maxHeight = R.dimen.notification_custom_view_max_image_height_low_ram;
            } else {
                maxHeight = R.dimen.notification_custom_view_max_image_height;
            }
            remoteView.reduceImageSizes(maxWidth, resources.getDimensionPixelSize(maxHeight));
        }
    }

    public boolean isForegroundService() {
        return (this.flags & 64) != 0;
    }

    public boolean hasMediaSession() {
        return this.extras.getParcelable(EXTRA_MEDIA_SESSION) != null;
    }

    public Class<? extends Style> getNotificationStyle() {
        String templateClass = this.extras.getString(EXTRA_TEMPLATE);
        if (TextUtils.isEmpty(templateClass)) {
            return null;
        }
        return getNotificationStyleClass(templateClass);
    }

    public boolean isColorized() {
        boolean z = true;
        if (isColorizedMedia()) {
            return true;
        }
        if (!(this.extras.getBoolean(EXTRA_COLORIZED) && (hasColorizedPermission() || isForegroundService()))) {
            z = false;
        }
        return z;
    }

    private boolean hasColorizedPermission() {
        return (this.flags & 2048) != 0;
    }

    public boolean isColorizedMedia() {
        Class<? extends Style> style = getNotificationStyle();
        boolean equals = MediaStyle.class.equals(style);
        String str = EXTRA_COLORIZED;
        if (equals) {
            Boolean colorized = (Boolean) this.extras.get(str);
            if ((colorized == null || colorized.booleanValue()) && hasMediaSession()) {
                return true;
            }
        } else if (DecoratedMediaCustomViewStyle.class.equals(style) && this.extras.getBoolean(str) && hasMediaSession()) {
            return true;
        }
        return false;
    }

    public boolean isMediaNotification() {
        Class<? extends Style> style = getNotificationStyle();
        if (MediaStyle.class.equals(style) || DecoratedMediaCustomViewStyle.class.equals(style)) {
            return true;
        }
        return false;
    }

    public boolean isBubbleNotification() {
        return (this.flags & 4096) != 0;
    }

    private boolean hasLargeIcon() {
        return (this.mLargeIcon == null && this.largeIcon == null) ? false : true;
    }

    public boolean showsTime() {
        return this.when != 0 && this.extras.getBoolean(EXTRA_SHOW_WHEN);
    }

    public boolean showsChronometer() {
        return this.when != 0 && this.extras.getBoolean(EXTRA_SHOW_CHRONOMETER);
    }

    @SystemApi
    public static Class<? extends Style> getNotificationStyleClass(String templateClass) {
        classes = new Class[7];
        int i = 0;
        classes[0] = BigTextStyle.class;
        classes[1] = BigPictureStyle.class;
        classes[2] = InboxStyle.class;
        classes[3] = MediaStyle.class;
        classes[4] = DecoratedCustomViewStyle.class;
        classes[5] = DecoratedMediaCustomViewStyle.class;
        classes[6] = MessagingStyle.class;
        int length = classes.length;
        while (i < length) {
            Class<? extends Style> innerClass = classes[i];
            if (templateClass.equals(innerClass.getName())) {
                return innerClass;
            }
            i++;
        }
        return null;
    }

    private static Notification[] getNotificationArrayFromBundle(Bundle bundle, String key) {
        Parcelable[] array = bundle.getParcelableArray(key);
        if ((array instanceof Notification[]) || array == null) {
            return (Notification[]) array;
        }
        Notification[] typedArray = (Notification[]) Arrays.copyOf(array, array.length, Notification[].class);
        bundle.putParcelableArray(key, typedArray);
        return typedArray;
    }
}
