package android.provider;

import android.annotation.UnsupportedAppUsage;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.UserHandle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Directory;
import android.text.TextUtils;
import android.widget.Toast;
import com.android.internal.R;
import java.util.List;

public class ContactsInternal {
    private static final int CONTACTS_URI_LOOKUP = 1001;
    private static final int CONTACTS_URI_LOOKUP_ID = 1000;
    private static final UriMatcher sContactsUriMatcher = new UriMatcher(-1);

    private ContactsInternal() {
    }

    static {
        UriMatcher matcher = sContactsUriMatcher;
        String str = ContactsContract.AUTHORITY;
        matcher.addURI(str, "contacts/lookup/*", 1001);
        matcher.addURI(str, "contacts/lookup/*/#", 1000);
    }

    @UnsupportedAppUsage
    public static void startQuickContactWithErrorToast(Context context, Intent intent) {
        int match = sContactsUriMatcher.match(intent.getData());
        if ((match != 1000 && match != 1001) || !maybeStartManagedQuickContact(context, intent)) {
            startQuickContactWithErrorToastForUser(context, intent, context.getUser());
        }
    }

    public static void startQuickContactWithErrorToastForUser(Context context, Intent intent, UserHandle user) {
        try {
            context.startActivityAsUser(intent, user);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, (int) R.string.quick_contacts_not_available, 0).show();
        }
    }

    private static boolean maybeStartManagedQuickContact(Context context, Intent originalIntent) {
        long j;
        long j2;
        Uri uri = originalIntent.getData();
        List<String> pathSegments = uri.getPathSegments();
        boolean isContactIdIgnored = pathSegments.size() < 4;
        if (isContactIdIgnored) {
            j = Contacts.ENTERPRISE_CONTACT_ID_BASE;
        } else {
            j = ContentUris.parseId(uri);
        }
        long contactId = j;
        String lookupKey = (String) pathSegments.get(2);
        String directoryIdStr = uri.getQueryParameter(ContactsContract.DIRECTORY_PARAM_KEY);
        if (directoryIdStr == null) {
            j2 = 1000000000;
        } else {
            j2 = Long.parseLong(directoryIdStr);
        }
        long directoryId = j2;
        StringBuilder stringBuilder;
        if (TextUtils.isEmpty(lookupKey)) {
            j = directoryId;
        } else if (!lookupKey.startsWith(Contacts.ENTERPRISE_CONTACT_LOOKUP_PREFIX)) {
            j = directoryId;
        } else if (!Contacts.isEnterpriseContactId(contactId)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid enterprise contact id: ");
            stringBuilder.append(contactId);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (Directory.isEnterpriseDirectoryId(directoryId)) {
            ((DevicePolicyManager) context.getSystemService(DevicePolicyManager.class)).startManagedQuickContact(lookupKey.substring(Contacts.ENTERPRISE_CONTACT_LOOKUP_PREFIX.length()), contactId - Contacts.ENTERPRISE_CONTACT_ID_BASE, isContactIdIgnored, directoryId - 1000000000, originalIntent);
            return true;
        } else {
            long directoryId2 = directoryId;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid enterprise directory id: ");
            stringBuilder.append(directoryId2);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        return false;
    }
}
