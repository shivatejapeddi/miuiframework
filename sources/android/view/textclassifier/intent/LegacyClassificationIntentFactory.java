package android.view.textclassifier.intent;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.Browser;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.ContactsContract.Contacts;
import android.view.textclassifier.Log;
import android.view.textclassifier.TextClassifier;
import com.android.internal.R;
import com.google.android.textclassifier.AnnotatorModel.ClassificationResult;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class LegacyClassificationIntentFactory implements ClassificationIntentFactory {
    private static final long DEFAULT_EVENT_DURATION = TimeUnit.HOURS.toMillis(1);
    private static final long MIN_EVENT_FUTURE_MILLIS = TimeUnit.MINUTES.toMillis(5);
    private static final String TAG = "LegacyClassificationIntentFactory";

    public List<LabeledIntent> create(Context context, String text, boolean foreignText, Instant referenceTime, ClassificationResult classification) {
        String type;
        List<LabeledIntent> actions;
        if (classification != null) {
            type = classification.getCollection().trim().toLowerCase(Locale.ENGLISH);
        } else {
            type = "";
        }
        text = text.trim();
        Object obj = -1;
        switch (type.hashCode()) {
            case -1271823248:
                if (type.equals(TextClassifier.TYPE_FLIGHT_NUMBER)) {
                    obj = 6;
                    break;
                }
                break;
            case -1147692044:
                if (type.equals("address")) {
                    obj = 2;
                    break;
                }
                break;
            case 116079:
                if (type.equals("url")) {
                    obj = 3;
                    break;
                }
                break;
            case 3076014:
                if (type.equals("date")) {
                    obj = 4;
                    break;
                }
                break;
            case 96619420:
                if (type.equals("email")) {
                    obj = null;
                    break;
                }
                break;
            case 106642798:
                if (type.equals("phone")) {
                    obj = 1;
                    break;
                }
                break;
            case 447049878:
                if (type.equals(TextClassifier.TYPE_DICTIONARY)) {
                    obj = 7;
                    break;
                }
                break;
            case 1793702779:
                if (type.equals(TextClassifier.TYPE_DATE_TIME)) {
                    obj = 5;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                actions = createForEmail(context, text);
                break;
            case 1:
                actions = createForPhone(context, text);
                break;
            case 2:
                actions = createForAddress(context, text);
                break;
            case 3:
                actions = createForUrl(context, text);
                break;
            case 4:
            case 5:
                if (classification.getDatetimeResult() == null) {
                    actions = new ArrayList();
                    break;
                }
                actions = createForDatetime(context, type, referenceTime, Instant.ofEpochMilli(classification.getDatetimeResult().getTimeMsUtc()));
                break;
            case 6:
                actions = createForFlight(context, text);
                break;
            case 7:
                actions = createForDictionary(context, text);
                break;
            default:
                actions = new ArrayList();
                break;
        }
        if (foreignText) {
            ClassificationIntentFactory.insertTranslateAction(actions, context, text);
        }
        return actions;
    }

    private static List<LabeledIntent> createForEmail(Context context, String text) {
        Context context2 = context;
        String str = text;
        List<LabeledIntent> actions = new ArrayList();
        actions.add(new LabeledIntent(context2.getString(R.string.email), null, context2.getString(R.string.email_desc), null, new Intent(Intent.ACTION_SENDTO).setData(Uri.parse(String.format("mailto:%s", new Object[]{str}))), 0));
        actions.add(new LabeledIntent(context2.getString(R.string.add_contact), null, context2.getString(R.string.add_contact_desc), null, new Intent(Intent.ACTION_INSERT_OR_EDIT).setType(Contacts.CONTENT_ITEM_TYPE).putExtra("email", str), text.hashCode()));
        return actions;
    }

    private static List<LabeledIntent> createForPhone(Context context, String text) {
        Context context2 = context;
        String str = text;
        List<LabeledIntent> actions = new ArrayList();
        UserManager userManager = (UserManager) context2.getSystemService(UserManager.class);
        Bundle userRestrictions = userManager != null ? userManager.getUserRestrictions() : new Bundle();
        if (!userRestrictions.getBoolean(UserManager.DISALLOW_OUTGOING_CALLS, false)) {
            actions.add(new LabeledIntent(context2.getString(R.string.dial), null, context2.getString(R.string.dial_desc), null, new Intent(Intent.ACTION_DIAL).setData(Uri.parse(String.format("tel:%s", new Object[]{str}))), 0));
        }
        actions.add(new LabeledIntent(context2.getString(R.string.add_contact), null, context2.getString(R.string.add_contact_desc), null, new Intent(Intent.ACTION_INSERT_OR_EDIT).setType(Contacts.CONTENT_ITEM_TYPE).putExtra("phone", str), text.hashCode()));
        if (!userRestrictions.getBoolean(UserManager.DISALLOW_SMS, false)) {
            actions.add(new LabeledIntent(context2.getString(R.string.sms), null, context2.getString(R.string.sms_desc), null, new Intent(Intent.ACTION_SENDTO).setData(Uri.parse(String.format("smsto:%s", new Object[]{str}))), 0));
        }
        return actions;
    }

    private static List<LabeledIntent> createForAddress(Context context, String text) {
        List<LabeledIntent> actions = new ArrayList();
        try {
            String encText = URLEncoder.encode(text, "UTF-8");
            actions.add(new LabeledIntent(context.getString(R.string.map), null, context.getString(R.string.map_desc), null, new Intent("android.intent.action.VIEW").setData(Uri.parse(String.format("geo:0,0?q=%s", new Object[]{encText}))), 0));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Could not encode address", e);
        }
        return actions;
    }

    private static List<LabeledIntent> createForUrl(Context context, String text) {
        if (Uri.parse(text).getScheme() == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://");
            stringBuilder.append(text);
            text = stringBuilder.toString();
        }
        List<LabeledIntent> actions = new ArrayList();
        actions.add(new LabeledIntent(context.getString(R.string.browse), null, context.getString(R.string.browse_desc), null, new Intent("android.intent.action.VIEW").setDataAndNormalize(Uri.parse(text)).putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName()), 0));
        return actions;
    }

    private static List<LabeledIntent> createForDatetime(Context context, String type, Instant referenceTime, Instant parsedTime) {
        if (referenceTime == null) {
            referenceTime = Instant.now();
        }
        List<LabeledIntent> actions = new ArrayList();
        actions.add(createCalendarViewIntent(context, parsedTime));
        if (referenceTime.until(parsedTime, ChronoUnit.MILLIS) > MIN_EVENT_FUTURE_MILLIS) {
            actions.add(createCalendarCreateEventIntent(context, parsedTime, type));
        }
        return actions;
    }

    private static List<LabeledIntent> createForFlight(Context context, String text) {
        List<LabeledIntent> actions = new ArrayList();
        actions.add(new LabeledIntent(context.getString(R.string.view_flight), null, context.getString(R.string.view_flight_desc), null, new Intent(Intent.ACTION_WEB_SEARCH).putExtra("query", text), text.hashCode()));
        return actions;
    }

    private static LabeledIntent createCalendarViewIntent(Context context, Instant parsedTime) {
        Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, parsedTime.toEpochMilli());
        return new LabeledIntent(context.getString(R.string.view_calendar), null, context.getString(R.string.view_calendar_desc), null, new Intent("android.intent.action.VIEW").setData(builder.build()), 0);
    }

    private static LabeledIntent createCalendarCreateEventIntent(Context context, Instant parsedTime, String type) {
        return new LabeledIntent(context.getString(R.string.add_calendar_event), null, context.getString(R.string.add_calendar_event_desc), null, new Intent("android.intent.action.INSERT").setData(Events.CONTENT_URI).putExtra("allDay", "date".equals(type)).putExtra("beginTime", parsedTime.toEpochMilli()).putExtra("endTime", parsedTime.toEpochMilli() + DEFAULT_EVENT_DURATION), parsedTime.hashCode());
    }

    private static List<LabeledIntent> createForDictionary(Context context, String text) {
        List<LabeledIntent> actions = new ArrayList();
        actions.add(new LabeledIntent(context.getString(R.string.define), null, context.getString(R.string.define_desc), null, new Intent(Intent.ACTION_DEFINE).putExtra(Intent.EXTRA_TEXT, text), text.hashCode()));
        return actions;
    }
}
