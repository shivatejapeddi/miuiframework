package miui.app;

import android.accounts.Account;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Entity;
import android.content.Entity.NamedContentValues;
import android.content.EntityIterator;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.provider.ContactsContract.CommonDataKinds.SipAddress;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContactsEntity;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import miui.provider.BatchOperation;

public class RemoveDuplicateContacts {
    public static final String CALLER_IS_REMOVE_DUPLICATE = "caller_is_remove_duplicate";
    private static final Uri CONTENT_URI = RawContactsEntity.CONTENT_URI;
    private static final boolean DBG = false;
    private static final String NAME_SELECTION = "deleted=0 AND account_name=? AND account_type=? AND data_set IS NULL ";
    public static final String TAG = "RemoveDuplicateContacts";
    private static final HashMap<Long, String> sGroups = new HashMap();
    private static final HashSet<String> sOtherMimeTypes = new HashSet();

    public static class ContactsInfo {
        private int mCount;
        private List<String> mEmails;
        private String mName;
        private List<String> mPhones;
        private long mPhotoId;
        private long mRawContactId;

        public int getCount() {
            return this.mCount;
        }

        public String getName() {
            return this.mName;
        }

        public List<String> getPhones() {
            return this.mPhones;
        }

        public long getPhotoId() {
            return this.mPhotoId;
        }

        public long getRawContactId() {
            return this.mRawContactId;
        }

        public List<String> getEmails() {
            return this.mEmails;
        }

        public ContactsInfo(RawContactData rawContact, int count) {
            this.mPhotoId = rawContact.mPhotoId;
            this.mName = TextUtils.isEmpty(rawContact.mName) ? "" : rawContact.mName;
            this.mPhones = (List) rawContact.getDatas().get(Phone.CONTENT_ITEM_TYPE);
            this.mCount = count;
            this.mRawContactId = rawContact.getRawContactId();
            this.mEmails = (List) rawContact.getDatas().get(Email.CONTENT_ITEM_TYPE);
        }

        public ContactsInfo(long photoId, String name, List<String> phones, List<String> emails, long rawContactId) {
            this.mPhotoId = photoId;
            this.mName = TextUtils.isEmpty(name) ? "" : name;
            this.mPhones = phones;
            this.mEmails = emails;
            this.mRawContactId = rawContactId;
        }
    }

    public static class GroupsData {
        public long id;
        public String sourceid;
        public String title;
    }

    public static class MergeContacts {
        private boolean mChecked = true;
        private ArrayList<ContactsInfo> mContacts;
        private String mName;

        public ArrayList<ContactsInfo> getContacts() {
            return this.mContacts;
        }

        public String getName() {
            return this.mName;
        }

        public MergeContacts(ArrayList<ContactsInfo> list, String name) {
            this.mContacts = list;
            this.mName = name;
        }

        public boolean isChecked() {
            return this.mChecked;
        }

        public void setChecked(boolean checked) {
            this.mChecked = checked;
        }
    }

    public static class RawContactData {
        public static final int HAS_DISPLAY_PHOTO = 100;
        public static final int HAS_PHOTO = 10;
        private HashMap<String, List<String>> mDatas = new HashMap();
        private boolean mDeleted;
        public int mN;
        public String mName;
        public long mPhotoId;
        public long mRawContactId;
        public String mSourceId;

        public HashMap<String, List<String>> getDatas() {
            return this.mDatas;
        }

        public boolean isDeleted() {
            return this.mDeleted;
        }

        public void setDeleted(boolean deleted) {
            this.mDeleted = deleted;
        }

        public long getRawContactId() {
            return this.mRawContactId;
        }

        public void setRawContactId(long rawContactId) {
            this.mRawContactId = rawContactId;
        }

        public void addData(String mimeType, String value) {
            if (value != null) {
                List<String> dataList = (List) this.mDatas.get(mimeType);
                if (dataList == null) {
                    ArrayList dataList2 = new ArrayList();
                    dataList2.add(value);
                    this.mDatas.put(mimeType, dataList2);
                } else if (!dataList.contains(value)) {
                    dataList.add(value);
                }
            }
        }

        public boolean compare(RawContactData another) {
            if (another == null || another.mDatas.size() != this.mDatas.size()) {
                return false;
            }
            for (String mimeType : this.mDatas.keySet()) {
                if (!another.mDatas.containsKey(mimeType)) {
                    return false;
                }
                List<String> dataListA = (List) this.mDatas.get(mimeType);
                List<String> dataListB = new ArrayList();
                dataListB.addAll((Collection) another.mDatas.get(mimeType));
                if (dataListA.size() != dataListB.size()) {
                    return false;
                }
                if (Phone.CONTENT_ITEM_TYPE.equals(mimeType)) {
                    for (String numberA : dataListA) {
                        String number = null;
                        for (String numberB : dataListB) {
                            if (numberA.equals(dataListB)) {
                                number = numberB;
                                break;
                            } else if (PhoneNumberUtils.compare(numberA, numberB)) {
                                number = numberB;
                                break;
                            }
                        }
                        if (number == null) {
                            return false;
                        }
                        dataListB.remove(number);
                    }
                    continue;
                } else if (!dataListA.equals(dataListB)) {
                    return false;
                }
            }
            return true;
        }
    }

    public interface RemoveDuplicateContactsListener {
        void onBegin(int i);

        void onEnd(boolean z);

        void onProgress(int i);
    }

    static {
        sOtherMimeTypes.add(Phone.CONTENT_ITEM_TYPE);
        sOtherMimeTypes.add(Email.CONTENT_ITEM_TYPE);
        sOtherMimeTypes.add(StructuredPostal.CONTENT_ITEM_TYPE);
        sOtherMimeTypes.add(Organization.CONTENT_ITEM_TYPE);
        sOtherMimeTypes.add(Website.CONTENT_ITEM_TYPE);
        sOtherMimeTypes.add(Event.CONTENT_ITEM_TYPE);
        sOtherMimeTypes.add(SipAddress.CONTENT_ITEM_TYPE);
        sOtherMimeTypes.add(Relation.CONTENT_ITEM_TYPE);
        sOtherMimeTypes.add(Note.CONTENT_ITEM_TYPE);
        sOtherMimeTypes.add(Nickname.CONTENT_ITEM_TYPE);
        sOtherMimeTypes.add("vnd.com.miui.cursor.item/gender");
        sOtherMimeTypes.add("vnd.com.miui.cursor.item/bloodType");
        sOtherMimeTypes.add("vnd.com.miui.cursor.item/constellation");
        sOtherMimeTypes.add("vnd.com.miui.cursor.item/animalSign");
        sOtherMimeTypes.add("vnd.com.miui.cursor.item/emotionStatus");
        sOtherMimeTypes.add("vnd.com.miui.cursor.item/interest");
        sOtherMimeTypes.add("vnd.com.miui.cursor.item/hobby");
        sOtherMimeTypes.add("vnd.com.miui.cursor.item/degree");
        sOtherMimeTypes.add("vnd.com.miui.cursor.item/schools");
        sOtherMimeTypes.add("vnd.com.miui.cursor.item/characteristic");
        sOtherMimeTypes.add("vnd.com.miui.cursor.item/xiaomiId");
        sOtherMimeTypes.add("vnd.com.miui.cursor.item/lunarBirthday");
    }

    /* JADX WARNING: Missing block: B:49:0x00f8, code skipped:
            return 0;
     */
    public static synchronized int remove(android.accounts.Account[] r17, android.content.ContentResolver r18, miui.app.RemoveDuplicateContacts.RemoveDuplicateContactsListener r19, boolean r20) {
        /*
        r0 = r17;
        r1 = r18;
        r2 = r19;
        r3 = miui.app.RemoveDuplicateContacts.class;
        monitor-enter(r3);
        r4 = 0;
        if (r0 == 0) goto L_0x00f7;
    L_0x000c:
        if (r1 != 0) goto L_0x0010;
    L_0x000e:
        goto L_0x00f7;
    L_0x0010:
        r5 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x00f4 }
        r7 = 0;
        r8 = new java.util.ArrayList;	 Catch:{ all -> 0x00f4 }
        r8.<init>();	 Catch:{ all -> 0x00f4 }
        if (r2 == 0) goto L_0x001f;
    L_0x001c:
        r2.onBegin(r4);	 Catch:{ all -> 0x00f4 }
    L_0x001f:
        r9 = r0.length;	 Catch:{ all -> 0x00f4 }
    L_0x0020:
        if (r4 >= r9) goto L_0x005a;
    L_0x0022:
        r10 = r0[r4];	 Catch:{ all -> 0x00f4 }
        r11 = sGroups;	 Catch:{ all -> 0x00f4 }
        r11.clear();	 Catch:{ all -> 0x00f4 }
        r11 = getGroups(r10, r1);	 Catch:{ all -> 0x00f4 }
        r12 = r11.iterator();	 Catch:{ all -> 0x00f4 }
    L_0x0031:
        r13 = r12.hasNext();	 Catch:{ all -> 0x00f4 }
        if (r13 == 0) goto L_0x004d;
    L_0x0037:
        r13 = r12.next();	 Catch:{ all -> 0x00f4 }
        r13 = (miui.app.RemoveDuplicateContacts.GroupsData) r13;	 Catch:{ all -> 0x00f4 }
        r14 = sGroups;	 Catch:{ all -> 0x00f4 }
        r15 = r5;
        r5 = r13.id;	 Catch:{ all -> 0x00f4 }
        r5 = java.lang.Long.valueOf(r5);	 Catch:{ all -> 0x00f4 }
        r6 = r13.title;	 Catch:{ all -> 0x00f4 }
        r14.put(r5, r6);	 Catch:{ all -> 0x00f4 }
        r5 = r15;
        goto L_0x0031;
    L_0x004d:
        r15 = r5;
        r5 = getDeletedRawContacts(r10, r1);	 Catch:{ all -> 0x00f4 }
        r8.addAll(r5);	 Catch:{ all -> 0x00f4 }
        r4 = r4 + 1;
        r5 = r15;
        goto L_0x0020;
    L_0x005a:
        r15 = r5;
        r4 = r8.size();	 Catch:{ all -> 0x00f4 }
        if (r4 <= 0) goto L_0x00e8;
    L_0x0061:
        r4 = new miui.provider.BatchOperation;	 Catch:{ all -> 0x00f4 }
        r5 = "com.android.contacts";
        r4.<init>(r1, r5);	 Catch:{ all -> 0x00f4 }
        if (r2 == 0) goto L_0x0071;
    L_0x006a:
        r5 = r8.size();	 Catch:{ all -> 0x00f4 }
        r2.onBegin(r5);	 Catch:{ all -> 0x00f4 }
    L_0x0071:
        r5 = r8.iterator();	 Catch:{ all -> 0x00f4 }
    L_0x0075:
        r6 = r5.hasNext();	 Catch:{ all -> 0x00f4 }
        if (r6 == 0) goto L_0x00df;
    L_0x007b:
        r6 = r5.next();	 Catch:{ all -> 0x00f4 }
        r6 = (miui.app.RemoveDuplicateContacts.RawContactData) r6;	 Catch:{ all -> 0x00f4 }
        r9 = android.provider.ContactsContract.RawContacts.CONTENT_URI;	 Catch:{ all -> 0x00f4 }
        r10 = r6.mRawContactId;	 Catch:{ all -> 0x00f4 }
        r9 = android.content.ContentUris.withAppendedId(r9, r10);	 Catch:{ all -> 0x00f4 }
        r9 = r9.buildUpon();	 Catch:{ all -> 0x00f4 }
        r10 = "caller_is_remove_duplicate";
        r11 = "true";
        r9 = r9.appendQueryParameter(r10, r11);	 Catch:{ all -> 0x00f4 }
        r9 = r9.build();	 Catch:{ all -> 0x00f4 }
        r10 = 0;
        r11 = r6.mSourceId;	 Catch:{ all -> 0x00f4 }
        if (r11 != 0) goto L_0x00bb;
        r11 = r9.buildUpon();	 Catch:{ all -> 0x00f4 }
        r12 = "caller_is_syncadapter";
        r13 = "true";
        r11 = r11.appendQueryParameter(r12, r13);	 Catch:{ all -> 0x00f4 }
        r11 = r11.build();	 Catch:{ all -> 0x00f4 }
        r11 = android.content.ContentProviderOperation.newDelete(r11);	 Catch:{ all -> 0x00f4 }
        r11 = r11.build();	 Catch:{ all -> 0x00f4 }
        r10 = r11;
        goto L_0x00c6;
    L_0x00bb:
        if (r20 != 0) goto L_0x00c6;
    L_0x00bd:
        r11 = android.content.ContentProviderOperation.newDelete(r9);	 Catch:{ all -> 0x00f4 }
        r11 = r11.build();	 Catch:{ all -> 0x00f4 }
        r10 = r11;
    L_0x00c6:
        if (r10 == 0) goto L_0x00cb;
    L_0x00c8:
        r4.add(r10);	 Catch:{ all -> 0x00f4 }
    L_0x00cb:
        r11 = r4.size();	 Catch:{ all -> 0x00f4 }
        r12 = 100;
        if (r11 <= r12) goto L_0x00d6;
    L_0x00d3:
        r4.execute();	 Catch:{ all -> 0x00f4 }
    L_0x00d6:
        if (r2 == 0) goto L_0x00db;
    L_0x00d8:
        r2.onProgress(r7);	 Catch:{ all -> 0x00f4 }
        r7 = r7 + 1;
        goto L_0x0075;
    L_0x00df:
        r5 = r4.size();	 Catch:{ all -> 0x00f4 }
        if (r5 <= 0) goto L_0x00e8;
    L_0x00e5:
        r4.execute();	 Catch:{ all -> 0x00f4 }
    L_0x00e8:
        if (r2 == 0) goto L_0x00ee;
    L_0x00ea:
        r4 = 1;
        r2.onEnd(r4);	 Catch:{ all -> 0x00f4 }
    L_0x00ee:
        r4 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x00f4 }
        monitor-exit(r3);
        return r7;
    L_0x00f4:
        r0 = move-exception;
        monitor-exit(r3);
        throw r0;
    L_0x00f7:
        monitor-exit(r3);
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.app.RemoveDuplicateContacts.remove(android.accounts.Account[], android.content.ContentResolver, miui.app.RemoveDuplicateContacts$RemoveDuplicateContactsListener, boolean):int");
    }

    public static List<RawContactData> getDeletedRawContacts(Account account, ContentResolver resolver) {
        String[] selectionArgs = new String[]{account.name, account.type};
        ArrayList<RawContactData> result = new ArrayList();
        HashMap<String, List<Long>> nameWithRawContactIds = getNameWithRawContactIds(resolver, selectionArgs);
        if (nameWithRawContactIds.size() > 0) {
            for (String name : nameWithRawContactIds.keySet()) {
                List<Long> rawContactIds = (List) nameWithRawContactIds.get(name);
                if (rawContactIds.size() >= 2) {
                    ArrayList<RawContactData> rawContactsIds = getNeedDeletedRawContacts(resolver, rawContactIds, name);
                    if (rawContactsIds != null) {
                        result.addAll(rawContactsIds);
                    }
                }
            }
        }
        return result;
    }

    private static HashMap<String, List<Long>> getNameWithRawContactIds(ContentResolver resolver, String[] selectionArgs) {
        HashMap<String, List<Long>> nameWithRawContactIds = new HashMap();
        Cursor c = resolver.query(RawContacts.CONTENT_URI, new String[]{"display_name", "_id"}, NAME_SELECTION, selectionArgs, null);
        if (c == null) {
            return null;
        }
        while (c.moveToNext()) {
            try {
                String name = c.getString(null);
                long rawContactId = c.getLong(1);
                List<Long> rawContactIds = (List) nameWithRawContactIds.get(name);
                if (rawContactIds == null) {
                    ArrayList rawContactIds2 = new ArrayList();
                    rawContactIds2.add(Long.valueOf(rawContactId));
                    nameWithRawContactIds.put(name, rawContactIds2);
                } else {
                    rawContactIds.add(Long.valueOf(rawContactId));
                }
            } finally {
                c.close();
            }
        }
        return nameWithRawContactIds;
    }

    /* JADX WARNING: Removed duplicated region for block: B:70:0x0153  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0151  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0151  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0153  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0153  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0151  */
    private static java.util.ArrayList<miui.app.RemoveDuplicateContacts.RawContactData> getNeedDeletedRawContacts(android.content.ContentResolver r18, java.util.List<java.lang.Long> r19, java.lang.String r20) {
        /*
        r0 = "_id";
        r1 = "RemoveDuplicateContacts";
        r2 = 0;
        if (r19 == 0) goto L_0x01be;
    L_0x0007:
        r3 = r19.size();
        if (r3 != 0) goto L_0x0011;
    L_0x000d:
        r10 = r20;
        goto L_0x01c0;
    L_0x0011:
        r3 = java.lang.System.currentTimeMillis();
        r5 = getEntityByIds(r18, r19);
        if (r5 != 0) goto L_0x001c;
    L_0x001b:
        return r2;
    L_0x001c:
        r6 = new java.util.ArrayList;
        r6.<init>();
    L_0x0021:
        r7 = r5.hasNext();	 Catch:{ Exception -> 0x0125, all -> 0x011e }
        if (r7 == 0) goto L_0x0116;
    L_0x0027:
        r7 = r5.next();	 Catch:{ Exception -> 0x0125, all -> 0x011e }
        r7 = (android.content.Entity) r7;	 Catch:{ Exception -> 0x0125, all -> 0x011e }
        r8 = new miui.app.RemoveDuplicateContacts$RawContactData;	 Catch:{ Exception -> 0x0125, all -> 0x011e }
        r8.<init>();	 Catch:{ Exception -> 0x0125, all -> 0x011e }
        r9 = r7.getEntityValues();	 Catch:{ Exception -> 0x0125, all -> 0x011e }
        r10 = r9.getAsLong(r0);	 Catch:{ Exception -> 0x0125, all -> 0x011e }
        r10 = r10.longValue();	 Catch:{ Exception -> 0x0125, all -> 0x011e }
        r8.setRawContactId(r10);	 Catch:{ Exception -> 0x0125, all -> 0x011e }
        r10 = r20;
        r8.mName = r10;	 Catch:{ Exception -> 0x0114, all -> 0x0112 }
        r11 = "sourceid";
        r11 = r9.getAsString(r11);	 Catch:{ Exception -> 0x0114, all -> 0x0112 }
        r8.mSourceId = r11;	 Catch:{ Exception -> 0x0114, all -> 0x0112 }
        r11 = r7.getSubValues();	 Catch:{ Exception -> 0x0114, all -> 0x0112 }
        r11 = r11.iterator();	 Catch:{ Exception -> 0x0114, all -> 0x0112 }
    L_0x0056:
        r12 = r11.hasNext();	 Catch:{ Exception -> 0x0114, all -> 0x0112 }
        if (r12 == 0) goto L_0x0106;
    L_0x005c:
        r12 = r11.next();	 Catch:{ Exception -> 0x0114, all -> 0x0112 }
        r12 = (android.content.Entity.NamedContentValues) r12;	 Catch:{ Exception -> 0x0114, all -> 0x0112 }
        r13 = r12.values;	 Catch:{ Exception -> 0x0114, all -> 0x0112 }
        r14 = "mimetype";
        r14 = r13.getAsString(r14);	 Catch:{ Exception -> 0x0114, all -> 0x0112 }
        r15 = "vnd.android.cursor.item/photo";
        r15 = r15.equals(r14);	 Catch:{ Exception -> 0x0114, all -> 0x0112 }
        if (r15 == 0) goto L_0x0091;
    L_0x0074:
        r15 = r13.getAsLong(r0);	 Catch:{ Exception -> 0x0114, all -> 0x0112 }
        r16 = r3;
        r2 = r15.longValue();	 Catch:{ Exception -> 0x0110 }
        r8.mPhotoId = r2;	 Catch:{ Exception -> 0x0110 }
        r2 = "data14";
        r2 = r13.containsKey(r2);	 Catch:{ Exception -> 0x0110 }
        if (r2 == 0) goto L_0x008b;
    L_0x0088:
        r2 = 100;
        goto L_0x008d;
    L_0x008b:
        r2 = 10;
    L_0x008d:
        r8.mN = r2;	 Catch:{ Exception -> 0x0110 }
        goto L_0x0101;
    L_0x0091:
        r16 = r3;
        r2 = "vnd.android.cursor.item/group_membership";
        r2 = r2.equals(r14);	 Catch:{ Exception -> 0x0110 }
        r3 = "data1";
        if (r2 == 0) goto L_0x00bc;
    L_0x009e:
        r2 = r13.getAsLong(r3);	 Catch:{ Exception -> 0x0110 }
        r2 = r2.longValue();	 Catch:{ Exception -> 0x0110 }
        r4 = sGroups;	 Catch:{ Exception -> 0x0110 }
        r15 = java.lang.Long.valueOf(r2);	 Catch:{ Exception -> 0x0110 }
        r4 = r4.get(r15);	 Catch:{ Exception -> 0x0110 }
        r4 = (java.lang.String) r4;	 Catch:{ Exception -> 0x0110 }
        if (r4 != 0) goto L_0x00b7;
    L_0x00b4:
        r15 = "";
        goto L_0x00b8;
    L_0x00b7:
        r15 = r4;
    L_0x00b8:
        r8.addData(r14, r15);	 Catch:{ Exception -> 0x0110 }
        goto L_0x0101;
    L_0x00bc:
        r2 = "vnd.android.cursor.item/im";
        r2 = r2.equals(r14);	 Catch:{ Exception -> 0x0110 }
        if (r2 == 0) goto L_0x00cd;
    L_0x00c5:
        r2 = r13.getAsString(r3);	 Catch:{ Exception -> 0x0110 }
        r8.addData(r14, r2);	 Catch:{ Exception -> 0x0110 }
        goto L_0x00d6;
    L_0x00cd:
        r2 = "vnd.android.cursor.item/name";
        r2 = r2.equals(r14);	 Catch:{ Exception -> 0x0110 }
        if (r2 == 0) goto L_0x00d7;
    L_0x00d6:
        goto L_0x0101;
    L_0x00d7:
        r2 = sOtherMimeTypes;	 Catch:{ Exception -> 0x0110 }
        r2 = r2.contains(r14);	 Catch:{ Exception -> 0x0110 }
        if (r2 == 0) goto L_0x00ed;
    L_0x00df:
        r2 = r13.getAsString(r3);	 Catch:{ Exception -> 0x0110 }
        r3 = android.text.TextUtils.isEmpty(r2);	 Catch:{ Exception -> 0x0110 }
        if (r3 != 0) goto L_0x00ec;
    L_0x00e9:
        r8.addData(r14, r2);	 Catch:{ Exception -> 0x0110 }
    L_0x00ec:
        goto L_0x0101;
    L_0x00ed:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0110 }
        r2.<init>();	 Catch:{ Exception -> 0x0110 }
        r3 = "ignore unknown mimetype ";
        r2.append(r3);	 Catch:{ Exception -> 0x0110 }
        r2.append(r14);	 Catch:{ Exception -> 0x0110 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0110 }
        android.util.Log.d(r1, r2);	 Catch:{ Exception -> 0x0110 }
    L_0x0101:
        r3 = r16;
        r2 = 0;
        goto L_0x0056;
    L_0x0106:
        r16 = r3;
        r6.add(r8);	 Catch:{ Exception -> 0x0110 }
        r3 = r16;
        r2 = 0;
        goto L_0x0021;
    L_0x0110:
        r0 = move-exception;
        goto L_0x012a;
    L_0x0112:
        r0 = move-exception;
        goto L_0x0121;
    L_0x0114:
        r0 = move-exception;
        goto L_0x0128;
    L_0x0116:
        r10 = r20;
        r16 = r3;
    L_0x011a:
        r5.close();
        goto L_0x0146;
    L_0x011e:
        r0 = move-exception;
        r10 = r20;
    L_0x0121:
        r16 = r3;
        goto L_0x01ba;
    L_0x0125:
        r0 = move-exception;
        r10 = r20;
    L_0x0128:
        r16 = r3;
    L_0x012a:
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01b9 }
        r2.<init>();	 Catch:{ all -> 0x01b9 }
        r3 = "getNeedDeletedRawContacts(): ";
        r2.append(r3);	 Catch:{ all -> 0x01b9 }
        r3 = r0.getMessage();	 Catch:{ all -> 0x01b9 }
        r2.append(r3);	 Catch:{ all -> 0x01b9 }
        r2 = r2.toString();	 Catch:{ all -> 0x01b9 }
        android.util.Log.w(r1, r2);	 Catch:{ all -> 0x01b9 }
        r0.printStackTrace();	 Catch:{ all -> 0x01b9 }
        goto L_0x011a;
    L_0x0146:
        r0 = java.lang.System.currentTimeMillis();
        r2 = r6.size();
        r3 = 2;
        if (r2 >= r3) goto L_0x0153;
    L_0x0151:
        r3 = 0;
        return r3;
    L_0x0153:
        r3 = new java.util.ArrayList;
        r3.<init>();
        r4 = 0;
    L_0x0159:
        r7 = r2 + -1;
        if (r4 >= r7) goto L_0x01b4;
    L_0x015d:
        r7 = r6.get(r4);
        r7 = (miui.app.RemoveDuplicateContacts.RawContactData) r7;
        r8 = r7.isDeleted();
        if (r8 == 0) goto L_0x016a;
    L_0x0169:
        goto L_0x01b1;
    L_0x016a:
        r8 = r4 + 1;
    L_0x016c:
        if (r8 >= r2) goto L_0x01b1;
    L_0x016e:
        r9 = r6.get(r8);
        r9 = (miui.app.RemoveDuplicateContacts.RawContactData) r9;
        r11 = r9.isDeleted();
        if (r11 == 0) goto L_0x017b;
    L_0x017a:
        goto L_0x01ae;
    L_0x017b:
        r11 = r7.compare(r9);
        if (r11 == 0) goto L_0x01ae;
    L_0x0181:
        r11 = r9.mSourceId;
        r12 = 1;
        if (r11 != 0) goto L_0x018d;
    L_0x0186:
        r9.setDeleted(r12);
        r3.add(r9);
        goto L_0x01ae;
    L_0x018d:
        r11 = r7.mN;
        r13 = r9.mN;
        if (r11 <= r13) goto L_0x019a;
    L_0x0193:
        r9.setDeleted(r12);
        r3.add(r9);
        goto L_0x01ae;
    L_0x019a:
        r11 = r7.mN;
        r13 = r9.mN;
        if (r11 >= r13) goto L_0x01a7;
    L_0x01a0:
        r7.setDeleted(r12);
        r3.add(r7);
        goto L_0x01b1;
    L_0x01a7:
        r7.setDeleted(r12);
        r3.add(r7);
        goto L_0x01b1;
    L_0x01ae:
        r8 = r8 + 1;
        goto L_0x016c;
    L_0x01b1:
        r4 = r4 + 1;
        goto L_0x0159;
    L_0x01b4:
        r7 = java.lang.System.currentTimeMillis();
        return r3;
    L_0x01b9:
        r0 = move-exception;
    L_0x01ba:
        r5.close();
        throw r0;
    L_0x01be:
        r10 = r20;
    L_0x01c0:
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.app.RemoveDuplicateContacts.getNeedDeletedRawContacts(android.content.ContentResolver, java.util.List, java.lang.String):java.util.ArrayList");
    }

    private static EntityIterator getEntityByIds(ContentResolver resolver, List<Long> rawContactIds) {
        Uri uri = CONTENT_URI;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("_id IN (");
        stringBuilder.append(TextUtils.join((CharSequence) ",", (Iterable) rawContactIds));
        stringBuilder.append(")");
        Cursor c = resolver.query(uri, null, stringBuilder.toString(), null, null);
        if (c == null) {
            return null;
        }
        return RawContacts.newEntityIterator(c);
    }

    private static List<GroupsData> getGroups(Account account, ContentResolver resolver) {
        Account account2 = account;
        String selection = "account_name = ? AND account_type = ? ";
        String ORDER_BY = "title,sourceid DESC";
        ContentResolver cursor = resolver;
        Cursor cursor2 = cursor.query(Groups.CONTENT_URI, new String[]{"_id", "title", "sourceid"}, "account_name = ? AND account_type = ? ", new String[]{account2.name, account2.type}, "title,sourceid DESC");
        List<GroupsData> groups = new ArrayList();
        if (cursor2 != null) {
            while (cursor2.moveToNext()) {
                try {
                    GroupsData group = new GroupsData();
                    group.id = cursor2.getLong(0);
                    group.title = cursor2.getString(1);
                    group.sourceid = cursor2.getString(2);
                    groups.add(group);
                    account2 = account;
                } finally {
                    cursor2.close();
                }
            }
        }
        return groups;
    }

    public static void removeGroups(Account account, ContentResolver resolver) {
        ContentResolver contentResolver = resolver;
        List<GroupsData> groups = getGroups(account, resolver);
        int i = 1;
        if (groups.size() > 1) {
            String str;
            int deleteDataId;
            HashSet<Long> deleteDataId2;
            String where = "mimetype=? AND data1=?";
            int i2 = 0;
            long groupId = 0;
            while (true) {
                int size = groups.size();
                str = GroupMembership.CONTENT_ITEM_TYPE;
                deleteDataId = 2;
                if (i2 >= size) {
                    break;
                }
                if (i2 == 0) {
                    groupId = ((GroupsData) groups.get(i2)).id;
                } else {
                    String previousTitle = ((GroupsData) groups.get(i2 - 1)).title;
                    String title = ((GroupsData) groups.get(i2)).title;
                    long id = ((GroupsData) groups.get(i2)).id;
                    String sourceId = ((GroupsData) groups.get(i2)).sourceid;
                    String str2;
                    if (TextUtils.equals(previousTitle, title)) {
                        String[] selectionArgs = new String[]{str, String.valueOf(id)};
                        ContentValues values = new ContentValues();
                        values.put("data1", Long.valueOf(groupId));
                        contentResolver.update(Data.CONTENT_URI, values, "mimetype=? AND data1=?", selectionArgs);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("update contacts group from ");
                        stringBuilder.append(id);
                        stringBuilder.append(" to ");
                        stringBuilder.append(groupId);
                        String stringBuilder2 = stringBuilder.toString();
                        String str3 = TAG;
                        Log.d(str3, stringBuilder2);
                        if (sourceId == null) {
                            previousTitle = ContentUris.withAppendedId(Groups.CONTENT_URI, id).buildUpon().appendQueryParameter("caller_is_syncadapter", "true").build();
                        } else {
                            str2 = title;
                            previousTitle = ContentUris.withAppendedId(Groups.CONTENT_URI, id);
                        }
                        contentResolver.delete(previousTitle, null, null);
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("delete group ");
                        stringBuilder3.append(id);
                        Log.d(str3, stringBuilder3.toString());
                    } else {
                        str2 = title;
                        groupId = ((GroupsData) groups.get(i2)).id;
                    }
                }
                i2++;
                i = 1;
            }
            List<GroupsData> groups2 = getGroups(account, resolver);
            HashSet<Long> rawContactIds = new HashSet();
            HashSet<Long> deleteDataId3 = new HashSet();
            String[] projection = new String[]{"_id", "raw_contact_id"};
            Iterator it = groups2.iterator();
            while (it.hasNext()) {
                GroupsData group = (GroupsData) it.next();
                rawContactIds.clear();
                String[] selectionArgs2 = new String[deleteDataId];
                selectionArgs2[0] = str;
                selectionArgs2[1] = String.valueOf(group.id);
                deleteDataId2 = deleteDataId3;
                Cursor cursor = resolver.query(Data.CONTENT_URI, projection, "mimetype=? AND data1=?", selectionArgs2, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        try {
                            long dataId = cursor.getLong(0);
                            long rawContactId = cursor.getLong(1);
                            if (rawContactIds.contains(Long.valueOf(rawContactId))) {
                                deleteDataId2.add(Long.valueOf(dataId));
                            } else {
                                rawContactIds.add(Long.valueOf(rawContactId));
                            }
                        } finally {
                            cursor.close();
                        }
                    }
                }
                deleteDataId3 = deleteDataId2;
                deleteDataId = 2;
            }
            deleteDataId2 = deleteDataId3;
            BatchOperation operations = new BatchOperation(contentResolver, ContactsContract.AUTHORITY);
            Iterator it2 = deleteDataId2.iterator();
            while (it2.hasNext()) {
                operations.add(ContentProviderOperation.newDelete(ContentUris.withAppendedId(Data.CONTENT_URI, ((Long) it2.next()).longValue())).build());
                if (operations.size() > 100) {
                    operations.execute();
                }
            }
            if (operations.size() > 0) {
                operations.execute();
            }
        }
    }

    public static ArrayList<MergeContacts> getMergeRawContacts(Account[] accounts, ContentResolver resolver) {
        Throwable th;
        Account[] accountArr = accounts;
        ContentResolver contentResolver = resolver;
        String str = "_id";
        ArrayList<MergeContacts> result = new ArrayList();
        int phones = accountArr.length;
        int i = 0;
        int i2 = 0;
        while (i2 < phones) {
            String str2;
            int i3;
            Account account = accountArr[i2];
            int i4 = 2;
            HashMap<String, List<Long>> nameWithRawContactIds = getNameWithRawContactIds(contentResolver, new String[]{account.name, account.type});
            if (nameWithRawContactIds.size() == 0) {
                str2 = str;
                i3 = phones;
            } else {
                Account account2;
                for (String name : nameWithRawContactIds.keySet()) {
                    List<Long> rawContactIds = (List) nameWithRawContactIds.get(name);
                    if (rawContactIds.size() >= i4) {
                        List<Long> list;
                        ArrayList<ContactsInfo> arrayList;
                        EntityIterator entities = getEntityByIds(contentResolver, rawContactIds);
                        ArrayList<ContactsInfo> rawContacts = new ArrayList();
                        while (entities.hasNext()) {
                            try {
                                List<String> phones2;
                                Entity entity = (Entity) entities.next();
                                long id = entity.getEntityValues().getAsLong(str).longValue();
                                List<String> phones3 = new ArrayList();
                                List<String> emails = new ArrayList();
                                Iterator it = entity.getSubValues().iterator();
                                long photoId = 0;
                                while (it.hasNext()) {
                                    try {
                                        List<String> emails2;
                                        ContentValues cv = ((NamedContentValues) it.next()).values;
                                        String mimeType = cv.getAsString("mimetype");
                                        i3 = phones;
                                        if (Photo.CONTENT_ITEM_TYPE.equals(mimeType)) {
                                            try {
                                                photoId = cv.getAsLong(str).longValue();
                                                str2 = str;
                                                phones2 = phones3;
                                                account2 = account;
                                                emails2 = emails;
                                            } catch (Throwable th2) {
                                                th = th2;
                                                account2 = account;
                                                list = rawContactIds;
                                                arrayList = rawContacts;
                                                entities.close();
                                                throw th;
                                            }
                                        }
                                        str2 = str;
                                        str = "data1";
                                        if (Phone.CONTENT_ITEM_TYPE.equals(mimeType)) {
                                            phones2 = phones3;
                                            phones2.add(cv.getAsString(str));
                                            account2 = account;
                                            emails2 = emails;
                                        } else {
                                            phones2 = phones3;
                                            account2 = account;
                                            try {
                                                if (Email.CONTENT_ITEM_TYPE.equals(mimeType)) {
                                                    emails2 = emails;
                                                    emails2.add(cv.getAsString(str));
                                                } else {
                                                    emails2 = emails;
                                                }
                                            } catch (Throwable th3) {
                                                th = th3;
                                                list = rawContactIds;
                                                arrayList = rawContacts;
                                                entities.close();
                                                throw th;
                                            }
                                        }
                                        cv = accounts;
                                        contentResolver = resolver;
                                        emails = emails2;
                                        account = account2;
                                        str = str2;
                                        phones3 = phones2;
                                        phones = i3;
                                    } catch (Throwable th4) {
                                        th = th4;
                                        account2 = account;
                                        list = rawContactIds;
                                        arrayList = rawContacts;
                                        entities.close();
                                        throw th;
                                    }
                                }
                                str2 = str;
                                i3 = phones;
                                phones2 = phones3;
                                account2 = account;
                                try {
                                    list = rawContactIds;
                                    ContactsInfo contactsInfo = contactsInfo;
                                    try {
                                        arrayList = rawContacts;
                                    } catch (Throwable th5) {
                                        th = th5;
                                        arrayList = rawContacts;
                                        entities.close();
                                        throw th;
                                    }
                                    try {
                                        arrayList.add(new ContactsInfo(photoId, name, phones2, emails, id));
                                        rawContactIds = list;
                                        rawContacts = arrayList;
                                        account = account2;
                                        phones = i3;
                                        str = str2;
                                        list = accounts;
                                        contentResolver = resolver;
                                    } catch (Throwable th6) {
                                        th = th6;
                                        entities.close();
                                        throw th;
                                    }
                                } catch (Throwable th7) {
                                    th = th7;
                                    list = rawContactIds;
                                    arrayList = rawContacts;
                                    entities.close();
                                    throw th;
                                }
                            } catch (Throwable th8) {
                                th = th8;
                                account2 = account;
                                list = rawContactIds;
                                arrayList = rawContacts;
                                entities.close();
                                throw th;
                            }
                        }
                        str2 = str;
                        i3 = phones;
                        account2 = account;
                        list = rawContactIds;
                        arrayList = rawContacts;
                        entities.close();
                        if (arrayList.size() > 1) {
                            result.add(new MergeContacts(arrayList, name));
                        }
                        list = accounts;
                        contentResolver = resolver;
                        int i5 = 1;
                        account = account2;
                        phones = i3;
                        str = str2;
                        i4 = 2;
                    }
                }
                str2 = str;
                i3 = phones;
                account2 = account;
            }
            i2++;
            accountArr = accounts;
            contentResolver = resolver;
            phones = i3;
            str = str2;
            i = 0;
        }
        return result;
    }
}
