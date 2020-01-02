package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.os.Process;
import android.text.TextUtils;
import com.android.internal.content.PackageMonitor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityChooserModel extends DataSetObservable {
    private static final String ATTRIBUTE_ACTIVITY = "activity";
    private static final String ATTRIBUTE_TIME = "time";
    private static final String ATTRIBUTE_WEIGHT = "weight";
    private static final boolean DEBUG = false;
    private static final int DEFAULT_ACTIVITY_INFLATION = 5;
    private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0f;
    public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
    public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
    private static final String HISTORY_FILE_EXTENSION = ".xml";
    private static final int INVALID_INDEX = -1;
    private static final String LOG_TAG = ActivityChooserModel.class.getSimpleName();
    private static final String TAG_HISTORICAL_RECORD = "historical-record";
    private static final String TAG_HISTORICAL_RECORDS = "historical-records";
    private static final Map<String, ActivityChooserModel> sDataModelRegistry = new HashMap();
    private static final Object sRegistryLock = new Object();
    private final List<ActivityResolveInfo> mActivities = new ArrayList();
    private OnChooseActivityListener mActivityChoserModelPolicy;
    private ActivitySorter mActivitySorter = new DefaultSorter();
    private boolean mCanReadHistoricalData = true;
    private final Context mContext;
    private final List<HistoricalRecord> mHistoricalRecords = new ArrayList();
    private boolean mHistoricalRecordsChanged = true;
    private final String mHistoryFileName;
    private int mHistoryMaxSize = 50;
    private final Object mInstanceLock = new Object();
    private Intent mIntent;
    private final PackageMonitor mPackageMonitor = new DataModelPackageMonitor();
    private boolean mReadShareHistoryCalled = false;
    private boolean mReloadActivities = false;

    public interface ActivityChooserModelClient {
        void setActivityChooserModel(ActivityChooserModel activityChooserModel);
    }

    public final class ActivityResolveInfo implements Comparable<ActivityResolveInfo> {
        public final ResolveInfo resolveInfo;
        public float weight;

        public ActivityResolveInfo(ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
        }

        public int hashCode() {
            return Float.floatToIntBits(this.weight) + 31;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            if (Float.floatToIntBits(this.weight) != Float.floatToIntBits(((ActivityResolveInfo) obj).weight)) {
                return false;
            }
            return true;
        }

        public int compareTo(ActivityResolveInfo another) {
            return Float.floatToIntBits(another.weight) - Float.floatToIntBits(this.weight);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            builder.append("resolveInfo:");
            builder.append(this.resolveInfo.toString());
            builder.append("; weight:");
            builder.append(new BigDecimal((double) this.weight));
            builder.append("]");
            return builder.toString();
        }
    }

    public interface ActivitySorter {
        void sort(Intent intent, List<ActivityResolveInfo> list, List<HistoricalRecord> list2);
    }

    private final class DataModelPackageMonitor extends PackageMonitor {
        private DataModelPackageMonitor() {
        }

        public void onSomePackagesChanged() {
            ActivityChooserModel.this.mReloadActivities = true;
        }
    }

    private final class DefaultSorter implements ActivitySorter {
        private static final float WEIGHT_DECAY_COEFFICIENT = 0.95f;
        private final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap;

        private DefaultSorter() {
            this.mPackageNameToActivityMap = new HashMap();
        }

        public void sort(Intent intent, List<ActivityResolveInfo> activities, List<HistoricalRecord> historicalRecords) {
            Map<ComponentName, ActivityResolveInfo> componentNameToActivityMap = this.mPackageNameToActivityMap;
            componentNameToActivityMap.clear();
            int activityCount = activities.size();
            for (int i = 0; i < activityCount; i++) {
                ActivityResolveInfo activity = (ActivityResolveInfo) activities.get(i);
                activity.weight = 0.0f;
                componentNameToActivityMap.put(new ComponentName(activity.resolveInfo.activityInfo.packageName, activity.resolveInfo.activityInfo.name), activity);
            }
            float nextRecordWeight = 1.0f;
            for (int i2 = historicalRecords.size() - 1; i2 >= 0; i2--) {
                HistoricalRecord historicalRecord = (HistoricalRecord) historicalRecords.get(i2);
                ActivityResolveInfo activity2 = (ActivityResolveInfo) componentNameToActivityMap.get(historicalRecord.activity);
                if (activity2 != null) {
                    activity2.weight += historicalRecord.weight * nextRecordWeight;
                    nextRecordWeight *= WEIGHT_DECAY_COEFFICIENT;
                }
            }
            Collections.sort(activities);
        }
    }

    public static final class HistoricalRecord {
        public final ComponentName activity;
        public final long time;
        public final float weight;

        public HistoricalRecord(String activityName, long time, float weight) {
            this(ComponentName.unflattenFromString(activityName), time, weight);
        }

        public HistoricalRecord(ComponentName activityName, long time, float weight) {
            this.activity = activityName;
            this.time = time;
            this.weight = weight;
        }

        public int hashCode() {
            int result = 1 * 31;
            ComponentName componentName = this.activity;
            int hashCode = (result + (componentName == null ? 0 : componentName.hashCode())) * 31;
            long j = this.time;
            return ((hashCode + ((int) (j ^ (j >>> 32)))) * 31) + Float.floatToIntBits(this.weight);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            HistoricalRecord other = (HistoricalRecord) obj;
            ComponentName componentName = this.activity;
            if (componentName == null) {
                if (other.activity != null) {
                    return false;
                }
            } else if (!componentName.equals(other.activity)) {
                return false;
            }
            if (this.time == other.time && Float.floatToIntBits(this.weight) == Float.floatToIntBits(other.weight)) {
                return true;
            }
            return false;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            builder.append("; activity:");
            builder.append(this.activity);
            builder.append("; time:");
            builder.append(this.time);
            builder.append("; weight:");
            builder.append(new BigDecimal((double) this.weight));
            builder.append("]");
            return builder.toString();
        }
    }

    public interface OnChooseActivityListener {
        boolean onChooseActivity(ActivityChooserModel activityChooserModel, Intent intent);
    }

    private final class PersistHistoryAsyncTask extends AsyncTask<Object, Void, Void> {
        private PersistHistoryAsyncTask() {
        }

        /* JADX WARNING: Unknown top exception splitter block from list: {B:31:0x00c4=Splitter:B:31:0x00c4, B:23:0x0099=Splitter:B:23:0x0099, B:39:0x00ef=Splitter:B:39:0x00ef} */
        /* JADX WARNING: Removed duplicated region for block: B:43:0x0113 A:{SYNTHETIC, Splitter:B:43:0x0113} */
        /* JADX WARNING: Removed duplicated region for block: B:35:0x00e8 A:{SYNTHETIC, Splitter:B:35:0x00e8} */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x00bd A:{SYNTHETIC, Splitter:B:27:0x00bd} */
        /* JADX WARNING: Removed duplicated region for block: B:51:0x0123 A:{SYNTHETIC, Splitter:B:51:0x0123} */
        public java.lang.Void doInBackground(java.lang.Object... r18) {
            /*
            r17 = this;
            r1 = r17;
            r0 = "historical-record";
            r2 = "historical-records";
            r3 = "Error writing historical recrod file: ";
            r4 = 0;
            r5 = r18[r4];
            r5 = (java.util.List) r5;
            r6 = 1;
            r7 = r18[r6];
            r7 = (java.lang.String) r7;
            r8 = 0;
            r9 = 0;
            r10 = android.widget.ActivityChooserModel.this;	 Catch:{ FileNotFoundException -> 0x0129 }
            r10 = r10.mContext;	 Catch:{ FileNotFoundException -> 0x0129 }
            r10 = r10.openFileOutput(r7, r4);	 Catch:{ FileNotFoundException -> 0x0129 }
            r8 = r10;
            r10 = android.util.Xml.newSerializer();
            r10.setOutput(r8, r9);	 Catch:{ IllegalArgumentException -> 0x00ec, IllegalStateException -> 0x00c1, IOException -> 0x0096, all -> 0x0090 }
            r11 = java.nio.charset.StandardCharsets.UTF_8;	 Catch:{ IllegalArgumentException -> 0x00ec, IllegalStateException -> 0x00c1, IOException -> 0x0096, all -> 0x0090 }
            r11 = r11.name();	 Catch:{ IllegalArgumentException -> 0x00ec, IllegalStateException -> 0x00c1, IOException -> 0x0096, all -> 0x0090 }
            r12 = java.lang.Boolean.valueOf(r6);	 Catch:{ IllegalArgumentException -> 0x00ec, IllegalStateException -> 0x00c1, IOException -> 0x0096, all -> 0x0090 }
            r10.startDocument(r11, r12);	 Catch:{ IllegalArgumentException -> 0x00ec, IllegalStateException -> 0x00c1, IOException -> 0x0096, all -> 0x0090 }
            r10.startTag(r9, r2);	 Catch:{ IllegalArgumentException -> 0x00ec, IllegalStateException -> 0x00c1, IOException -> 0x0096, all -> 0x0090 }
            r11 = r5.size();	 Catch:{ IllegalArgumentException -> 0x00ec, IllegalStateException -> 0x00c1, IOException -> 0x0096, all -> 0x0090 }
            r12 = 0;
        L_0x003c:
            if (r12 >= r11) goto L_0x0076;
        L_0x003e:
            r13 = r5.remove(r4);	 Catch:{ IllegalArgumentException -> 0x00ec, IllegalStateException -> 0x00c1, IOException -> 0x0096, all -> 0x0090 }
            r13 = (android.widget.ActivityChooserModel.HistoricalRecord) r13;	 Catch:{ IllegalArgumentException -> 0x00ec, IllegalStateException -> 0x00c1, IOException -> 0x0096, all -> 0x0090 }
            r10.startTag(r9, r0);	 Catch:{ IllegalArgumentException -> 0x00ec, IllegalStateException -> 0x00c1, IOException -> 0x0096, all -> 0x0090 }
            r14 = "activity";
            r15 = r13.activity;	 Catch:{ IllegalArgumentException -> 0x00ec, IllegalStateException -> 0x00c1, IOException -> 0x0096, all -> 0x0090 }
            r15 = r15.flattenToString();	 Catch:{ IllegalArgumentException -> 0x00ec, IllegalStateException -> 0x00c1, IOException -> 0x0096, all -> 0x0090 }
            r10.attribute(r9, r14, r15);	 Catch:{ IllegalArgumentException -> 0x00ec, IllegalStateException -> 0x00c1, IOException -> 0x0096, all -> 0x0090 }
            r14 = "time";
            r16 = r5;
            r4 = r13.time;	 Catch:{ IllegalArgumentException -> 0x008e, IllegalStateException -> 0x008c, IOException -> 0x008a }
            r4 = java.lang.String.valueOf(r4);	 Catch:{ IllegalArgumentException -> 0x008e, IllegalStateException -> 0x008c, IOException -> 0x008a }
            r10.attribute(r9, r14, r4);	 Catch:{ IllegalArgumentException -> 0x008e, IllegalStateException -> 0x008c, IOException -> 0x008a }
            r4 = "weight";
            r5 = r13.weight;	 Catch:{ IllegalArgumentException -> 0x008e, IllegalStateException -> 0x008c, IOException -> 0x008a }
            r5 = java.lang.String.valueOf(r5);	 Catch:{ IllegalArgumentException -> 0x008e, IllegalStateException -> 0x008c, IOException -> 0x008a }
            r10.attribute(r9, r4, r5);	 Catch:{ IllegalArgumentException -> 0x008e, IllegalStateException -> 0x008c, IOException -> 0x008a }
            r10.endTag(r9, r0);	 Catch:{ IllegalArgumentException -> 0x008e, IllegalStateException -> 0x008c, IOException -> 0x008a }
            r12 = r12 + 1;
            r5 = r16;
            r4 = 0;
            goto L_0x003c;
        L_0x0076:
            r16 = r5;
            r10.endTag(r9, r2);	 Catch:{ IllegalArgumentException -> 0x008e, IllegalStateException -> 0x008c, IOException -> 0x008a }
            r10.endDocument();	 Catch:{ IllegalArgumentException -> 0x008e, IllegalStateException -> 0x008c, IOException -> 0x008a }
            r0 = android.widget.ActivityChooserModel.this;
            r0.mCanReadHistoricalData = r6;
            if (r8 == 0) goto L_0x0119;
        L_0x0085:
            r8.close();	 Catch:{ IOException -> 0x0117 }
            goto L_0x0116;
        L_0x008a:
            r0 = move-exception;
            goto L_0x0099;
        L_0x008c:
            r0 = move-exception;
            goto L_0x00c4;
        L_0x008e:
            r0 = move-exception;
            goto L_0x00ef;
        L_0x0090:
            r0 = move-exception;
            r16 = r5;
            r2 = r0;
            goto L_0x011c;
        L_0x0096:
            r0 = move-exception;
            r16 = r5;
        L_0x0099:
            r2 = android.widget.ActivityChooserModel.LOG_TAG;	 Catch:{ all -> 0x011a }
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x011a }
            r4.<init>();	 Catch:{ all -> 0x011a }
            r4.append(r3);	 Catch:{ all -> 0x011a }
            r3 = android.widget.ActivityChooserModel.this;	 Catch:{ all -> 0x011a }
            r3 = r3.mHistoryFileName;	 Catch:{ all -> 0x011a }
            r4.append(r3);	 Catch:{ all -> 0x011a }
            r3 = r4.toString();	 Catch:{ all -> 0x011a }
            android.util.Log.e(r2, r3, r0);	 Catch:{ all -> 0x011a }
            r0 = android.widget.ActivityChooserModel.this;
            r0.mCanReadHistoricalData = r6;
            if (r8 == 0) goto L_0x0119;
        L_0x00bd:
            r8.close();	 Catch:{ IOException -> 0x0117 }
            goto L_0x0116;
        L_0x00c1:
            r0 = move-exception;
            r16 = r5;
        L_0x00c4:
            r2 = android.widget.ActivityChooserModel.LOG_TAG;	 Catch:{ all -> 0x011a }
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x011a }
            r4.<init>();	 Catch:{ all -> 0x011a }
            r4.append(r3);	 Catch:{ all -> 0x011a }
            r3 = android.widget.ActivityChooserModel.this;	 Catch:{ all -> 0x011a }
            r3 = r3.mHistoryFileName;	 Catch:{ all -> 0x011a }
            r4.append(r3);	 Catch:{ all -> 0x011a }
            r3 = r4.toString();	 Catch:{ all -> 0x011a }
            android.util.Log.e(r2, r3, r0);	 Catch:{ all -> 0x011a }
            r0 = android.widget.ActivityChooserModel.this;
            r0.mCanReadHistoricalData = r6;
            if (r8 == 0) goto L_0x0119;
        L_0x00e8:
            r8.close();	 Catch:{ IOException -> 0x0117 }
            goto L_0x0116;
        L_0x00ec:
            r0 = move-exception;
            r16 = r5;
        L_0x00ef:
            r2 = android.widget.ActivityChooserModel.LOG_TAG;	 Catch:{ all -> 0x011a }
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x011a }
            r4.<init>();	 Catch:{ all -> 0x011a }
            r4.append(r3);	 Catch:{ all -> 0x011a }
            r3 = android.widget.ActivityChooserModel.this;	 Catch:{ all -> 0x011a }
            r3 = r3.mHistoryFileName;	 Catch:{ all -> 0x011a }
            r4.append(r3);	 Catch:{ all -> 0x011a }
            r3 = r4.toString();	 Catch:{ all -> 0x011a }
            android.util.Log.e(r2, r3, r0);	 Catch:{ all -> 0x011a }
            r0 = android.widget.ActivityChooserModel.this;
            r0.mCanReadHistoricalData = r6;
            if (r8 == 0) goto L_0x0119;
        L_0x0113:
            r8.close();	 Catch:{ IOException -> 0x0117 }
        L_0x0116:
            goto L_0x0119;
        L_0x0117:
            r0 = move-exception;
            goto L_0x0116;
        L_0x0119:
            return r9;
        L_0x011a:
            r0 = move-exception;
            r2 = r0;
        L_0x011c:
            r0 = android.widget.ActivityChooserModel.this;
            r0.mCanReadHistoricalData = r6;
            if (r8 == 0) goto L_0x0128;
        L_0x0123:
            r8.close();	 Catch:{ IOException -> 0x0127 }
            goto L_0x0128;
        L_0x0127:
            r0 = move-exception;
        L_0x0128:
            throw r2;
        L_0x0129:
            r0 = move-exception;
            r16 = r5;
            r2 = android.widget.ActivityChooserModel.LOG_TAG;
            r4 = new java.lang.StringBuilder;
            r4.<init>();
            r4.append(r3);
            r4.append(r7);
            r3 = r4.toString();
            android.util.Log.e(r2, r3, r0);
            return r9;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.ActivityChooserModel$PersistHistoryAsyncTask.doInBackground(java.lang.Object[]):java.lang.Void");
        }
    }

    @UnsupportedAppUsage
    public static ActivityChooserModel get(Context context, String historyFileName) {
        ActivityChooserModel dataModel;
        synchronized (sRegistryLock) {
            dataModel = (ActivityChooserModel) sDataModelRegistry.get(historyFileName);
            if (dataModel == null) {
                dataModel = new ActivityChooserModel(context, historyFileName);
                sDataModelRegistry.put(historyFileName, dataModel);
            }
        }
        return dataModel;
    }

    private ActivityChooserModel(Context context, String historyFileName) {
        this.mContext = context.getApplicationContext();
        if (!TextUtils.isEmpty(historyFileName)) {
            String str = HISTORY_FILE_EXTENSION;
            if (!historyFileName.endsWith(str)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(historyFileName);
                stringBuilder.append(str);
                this.mHistoryFileName = stringBuilder.toString();
                this.mPackageMonitor.register(this.mContext, null, true);
            }
        }
        this.mHistoryFileName = historyFileName;
        this.mPackageMonitor.register(this.mContext, null, true);
    }

    @UnsupportedAppUsage
    public void setIntent(Intent intent) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent == intent) {
                return;
            }
            this.mIntent = intent;
            this.mReloadActivities = true;
            ensureConsistentState();
        }
    }

    public Intent getIntent() {
        Intent intent;
        synchronized (this.mInstanceLock) {
            intent = this.mIntent;
        }
        return intent;
    }

    @UnsupportedAppUsage
    public int getActivityCount() {
        int size;
        synchronized (this.mInstanceLock) {
            ensureConsistentState();
            size = this.mActivities.size();
        }
        return size;
    }

    @UnsupportedAppUsage
    public ResolveInfo getActivity(int index) {
        ResolveInfo resolveInfo;
        synchronized (this.mInstanceLock) {
            ensureConsistentState();
            resolveInfo = ((ActivityResolveInfo) this.mActivities.get(index)).resolveInfo;
        }
        return resolveInfo;
    }

    public int getActivityIndex(ResolveInfo activity) {
        synchronized (this.mInstanceLock) {
            ensureConsistentState();
            List<ActivityResolveInfo> activities = this.mActivities;
            int activityCount = activities.size();
            for (int i = 0; i < activityCount; i++) {
                if (((ActivityResolveInfo) activities.get(i)).resolveInfo == activity) {
                    return i;
                }
            }
            return -1;
        }
    }

    @UnsupportedAppUsage
    public Intent chooseActivity(int index) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent == null) {
                return null;
            }
            ensureConsistentState();
            ActivityResolveInfo chosenActivity = (ActivityResolveInfo) this.mActivities.get(index);
            ComponentName chosenName = new ComponentName(chosenActivity.resolveInfo.activityInfo.packageName, chosenActivity.resolveInfo.activityInfo.name);
            Intent choiceIntent = new Intent(this.mIntent);
            choiceIntent.setComponent(chosenName);
            if (this.mActivityChoserModelPolicy != null) {
                if (this.mActivityChoserModelPolicy.onChooseActivity(this, new Intent(choiceIntent))) {
                    return null;
                }
            }
            addHisoricalRecord(new HistoricalRecord(chosenName, System.currentTimeMillis(), 1.0f));
            return choiceIntent;
        }
    }

    @UnsupportedAppUsage
    public void setOnChooseActivityListener(OnChooseActivityListener listener) {
        synchronized (this.mInstanceLock) {
            this.mActivityChoserModelPolicy = listener;
        }
    }

    public ResolveInfo getDefaultActivity() {
        synchronized (this.mInstanceLock) {
            ensureConsistentState();
            if (this.mActivities.isEmpty()) {
                return null;
            }
            ResolveInfo resolveInfo = ((ActivityResolveInfo) this.mActivities.get(0)).resolveInfo;
            return resolveInfo;
        }
    }

    public void setDefaultActivity(int index) {
        synchronized (this.mInstanceLock) {
            float weight;
            ensureConsistentState();
            ActivityResolveInfo newDefaultActivity = (ActivityResolveInfo) this.mActivities.get(index);
            ActivityResolveInfo oldDefaultActivity = (ActivityResolveInfo) this.mActivities.get(0);
            if (oldDefaultActivity != null) {
                weight = (oldDefaultActivity.weight - newDefaultActivity.weight) + 5.0f;
            } else {
                weight = 1.0f;
            }
            addHisoricalRecord(new HistoricalRecord(new ComponentName(newDefaultActivity.resolveInfo.activityInfo.packageName, newDefaultActivity.resolveInfo.activityInfo.name), System.currentTimeMillis(), weight));
        }
    }

    private void persistHistoricalDataIfNeeded() {
        if (!this.mReadShareHistoryCalled) {
            throw new IllegalStateException("No preceding call to #readHistoricalData");
        } else if (this.mHistoricalRecordsChanged) {
            this.mHistoricalRecordsChanged = false;
            if (!TextUtils.isEmpty(this.mHistoryFileName)) {
                new PersistHistoryAsyncTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new ArrayList(this.mHistoricalRecords), this.mHistoryFileName);
            }
        }
    }

    /* JADX WARNING: Missing block: B:11:0x0015, code skipped:
            return;
     */
    public void setActivitySorter(android.widget.ActivityChooserModel.ActivitySorter r3) {
        /*
        r2 = this;
        r0 = r2.mInstanceLock;
        monitor-enter(r0);
        r1 = r2.mActivitySorter;	 Catch:{ all -> 0x0016 }
        if (r1 != r3) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);	 Catch:{ all -> 0x0016 }
        return;
    L_0x0009:
        r2.mActivitySorter = r3;	 Catch:{ all -> 0x0016 }
        r1 = r2.sortActivitiesIfNeeded();	 Catch:{ all -> 0x0016 }
        if (r1 == 0) goto L_0x0014;
    L_0x0011:
        r2.notifyChanged();	 Catch:{ all -> 0x0016 }
    L_0x0014:
        monitor-exit(r0);	 Catch:{ all -> 0x0016 }
        return;
    L_0x0016:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0016 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.ActivityChooserModel.setActivitySorter(android.widget.ActivityChooserModel$ActivitySorter):void");
    }

    /* JADX WARNING: Missing block: B:11:0x0018, code skipped:
            return;
     */
    public void setHistoryMaxSize(int r3) {
        /*
        r2 = this;
        r0 = r2.mInstanceLock;
        monitor-enter(r0);
        r1 = r2.mHistoryMaxSize;	 Catch:{ all -> 0x0019 }
        if (r1 != r3) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        return;
    L_0x0009:
        r2.mHistoryMaxSize = r3;	 Catch:{ all -> 0x0019 }
        r2.pruneExcessiveHistoricalRecordsIfNeeded();	 Catch:{ all -> 0x0019 }
        r1 = r2.sortActivitiesIfNeeded();	 Catch:{ all -> 0x0019 }
        if (r1 == 0) goto L_0x0017;
    L_0x0014:
        r2.notifyChanged();	 Catch:{ all -> 0x0019 }
    L_0x0017:
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        return;
    L_0x0019:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.ActivityChooserModel.setHistoryMaxSize(int):void");
    }

    public int getHistoryMaxSize() {
        int i;
        synchronized (this.mInstanceLock) {
            i = this.mHistoryMaxSize;
        }
        return i;
    }

    public int getHistorySize() {
        int size;
        synchronized (this.mInstanceLock) {
            ensureConsistentState();
            size = this.mHistoricalRecords.size();
        }
        return size;
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        super.finalize();
        this.mPackageMonitor.unregister();
    }

    private void ensureConsistentState() {
        boolean stateChanged = loadActivitiesIfNeeded() | readHistoricalDataIfNeeded();
        pruneExcessiveHistoricalRecordsIfNeeded();
        if (stateChanged) {
            sortActivitiesIfNeeded();
            notifyChanged();
        }
    }

    private boolean sortActivitiesIfNeeded() {
        if (this.mActivitySorter == null || this.mIntent == null || this.mActivities.isEmpty() || this.mHistoricalRecords.isEmpty()) {
            return false;
        }
        this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList(this.mHistoricalRecords));
        return true;
    }

    private boolean loadActivitiesIfNeeded() {
        if (!this.mReloadActivities || this.mIntent == null) {
            return false;
        }
        this.mReloadActivities = false;
        this.mActivities.clear();
        List<ResolveInfo> resolveInfos = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
        int resolveInfoCount = resolveInfos.size();
        for (int i = 0; i < resolveInfoCount; i++) {
            ResolveInfo resolveInfo = (ResolveInfo) resolveInfos.get(i);
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (ActivityManager.checkComponentPermission(activityInfo.permission, Process.myUid(), activityInfo.applicationInfo.uid, activityInfo.exported) == 0) {
                this.mActivities.add(new ActivityResolveInfo(resolveInfo));
            }
        }
        return true;
    }

    private boolean readHistoricalDataIfNeeded() {
        if (!this.mCanReadHistoricalData || !this.mHistoricalRecordsChanged || TextUtils.isEmpty(this.mHistoryFileName)) {
            return false;
        }
        this.mCanReadHistoricalData = false;
        this.mReadShareHistoryCalled = true;
        readHistoricalDataImpl();
        return true;
    }

    private boolean addHisoricalRecord(HistoricalRecord historicalRecord) {
        boolean added = this.mHistoricalRecords.add(historicalRecord);
        if (added) {
            this.mHistoricalRecordsChanged = true;
            pruneExcessiveHistoricalRecordsIfNeeded();
            persistHistoricalDataIfNeeded();
            sortActivitiesIfNeeded();
            notifyChanged();
        }
        return added;
    }

    private void pruneExcessiveHistoricalRecordsIfNeeded() {
        int pruneCount = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
        if (pruneCount > 0) {
            this.mHistoricalRecordsChanged = true;
            for (int i = 0; i < pruneCount; i++) {
                HistoricalRecord historicalRecord = (HistoricalRecord) this.mHistoricalRecords.remove(0);
            }
        }
    }

    /* JADX WARNING: Missing block: B:17:0x0040, code skipped:
            if (r1 == null) goto L_0x00d1;
     */
    /* JADX WARNING: Missing block: B:19:?, code skipped:
            r1.close();
     */
    private void readHistoricalDataImpl() {
        /*
        r12 = this;
        r0 = "Error reading historical recrod file: ";
        r1 = 0;
        r2 = r12.mContext;	 Catch:{ FileNotFoundException -> 0x00da }
        r3 = r12.mHistoryFileName;	 Catch:{ FileNotFoundException -> 0x00da }
        r2 = r2.openFileInput(r3);	 Catch:{ FileNotFoundException -> 0x00da }
        r1 = r2;
        r2 = android.util.Xml.newPullParser();	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r3 = java.nio.charset.StandardCharsets.UTF_8;	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r3 = r3.name();	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r2.setInput(r1, r3);	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r3 = 0;
    L_0x001b:
        r4 = 1;
        if (r3 == r4) goto L_0x0027;
    L_0x001e:
        r5 = 2;
        if (r3 == r5) goto L_0x0027;
    L_0x0021:
        r4 = r2.next();	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r3 = r4;
        goto L_0x001b;
    L_0x0027:
        r5 = "historical-records";
        r6 = r2.getName();	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r5 = r5.equals(r6);	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        if (r5 == 0) goto L_0x0089;
    L_0x0033:
        r5 = r12.mHistoricalRecords;	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r5.clear();	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
    L_0x0038:
        r6 = r2.next();	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r3 = r6;
        if (r3 != r4) goto L_0x0047;
        if (r1 == 0) goto L_0x00d1;
    L_0x0042:
        r1.close();	 Catch:{ IOException -> 0x00cf }
        goto L_0x00ce;
    L_0x0047:
        r6 = 3;
        if (r3 == r6) goto L_0x0038;
    L_0x004a:
        r6 = 4;
        if (r3 != r6) goto L_0x004e;
    L_0x004d:
        goto L_0x0038;
    L_0x004e:
        r6 = r2.getName();	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r7 = "historical-record";
        r7 = r7.equals(r6);	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        if (r7 == 0) goto L_0x0081;
    L_0x005a:
        r7 = "activity";
        r8 = 0;
        r7 = r2.getAttributeValue(r8, r7);	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r9 = "time";
        r9 = r2.getAttributeValue(r8, r9);	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r9 = java.lang.Long.parseLong(r9);	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r11 = "weight";
        r8 = r2.getAttributeValue(r8, r11);	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r8 = java.lang.Float.parseFloat(r8);	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r11 = new android.widget.ActivityChooserModel$HistoricalRecord;	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r11.<init>(r7, r9, r8);	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r5.add(r11);	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        goto L_0x0038;
    L_0x0081:
        r4 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r7 = "Share records file not well-formed.";
        r4.<init>(r7);	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        throw r4;	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
    L_0x0089:
        r4 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        r5 = "Share records file does not start with historical-records tag.";
        r4.<init>(r5);	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
        throw r4;	 Catch:{ XmlPullParserException -> 0x00b1, IOException -> 0x0093 }
    L_0x0091:
        r0 = move-exception;
        goto L_0x00d2;
    L_0x0093:
        r2 = move-exception;
        r3 = LOG_TAG;	 Catch:{ all -> 0x0091 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0091 }
        r4.<init>();	 Catch:{ all -> 0x0091 }
        r4.append(r0);	 Catch:{ all -> 0x0091 }
        r0 = r12.mHistoryFileName;	 Catch:{ all -> 0x0091 }
        r4.append(r0);	 Catch:{ all -> 0x0091 }
        r0 = r4.toString();	 Catch:{ all -> 0x0091 }
        android.util.Log.e(r3, r0, r2);	 Catch:{ all -> 0x0091 }
        if (r1 == 0) goto L_0x00d1;
    L_0x00ad:
        r1.close();	 Catch:{ IOException -> 0x00cf }
        goto L_0x00ce;
    L_0x00b1:
        r2 = move-exception;
        r3 = LOG_TAG;	 Catch:{ all -> 0x0091 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0091 }
        r4.<init>();	 Catch:{ all -> 0x0091 }
        r4.append(r0);	 Catch:{ all -> 0x0091 }
        r0 = r12.mHistoryFileName;	 Catch:{ all -> 0x0091 }
        r4.append(r0);	 Catch:{ all -> 0x0091 }
        r0 = r4.toString();	 Catch:{ all -> 0x0091 }
        android.util.Log.e(r3, r0, r2);	 Catch:{ all -> 0x0091 }
        if (r1 == 0) goto L_0x00d1;
    L_0x00cb:
        r1.close();	 Catch:{ IOException -> 0x00cf }
    L_0x00ce:
        goto L_0x00d1;
    L_0x00cf:
        r0 = move-exception;
        goto L_0x00ce;
    L_0x00d1:
        return;
    L_0x00d2:
        if (r1 == 0) goto L_0x00d9;
    L_0x00d4:
        r1.close();	 Catch:{ IOException -> 0x00d8 }
        goto L_0x00d9;
    L_0x00d8:
        r2 = move-exception;
    L_0x00d9:
        throw r0;
    L_0x00da:
        r0 = move-exception;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.ActivityChooserModel.readHistoricalDataImpl():void");
    }
}
