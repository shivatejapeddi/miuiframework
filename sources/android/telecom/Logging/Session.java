package android.telecom.Logging;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telecom.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Iterator;

public class Session {
    public static final String CONTINUE_SUBSESSION = "CONTINUE_SUBSESSION";
    public static final String CREATE_SUBSESSION = "CREATE_SUBSESSION";
    public static final String END_SESSION = "END_SESSION";
    public static final String END_SUBSESSION = "END_SUBSESSION";
    public static final String EXTERNAL_INDICATOR = "E-";
    public static final String SESSION_SEPARATION_CHAR_CHILD = "_";
    public static final String START_EXTERNAL_SESSION = "START_EXTERNAL_SESSION";
    public static final String START_SESSION = "START_SESSION";
    public static final String SUBSESSION_SEPARATION_CHAR = "->";
    public static final String TRUNCATE_STRING = "...";
    public static final int UNDEFINED = -1;
    private int mChildCounter = 0;
    private ArrayList<Session> mChildSessions;
    private long mExecutionEndTimeMs = -1;
    private long mExecutionStartTimeMs;
    private String mFullMethodPathCache;
    private boolean mIsCompleted = false;
    private boolean mIsExternal = false;
    private boolean mIsStartedFromActiveSession = false;
    private String mOwnerInfo;
    private Session mParentSession;
    private String mSessionId;
    private String mShortMethodName;

    public static class Info implements Parcelable {
        public static final Creator<Info> CREATOR = new Creator<Info>() {
            public Info createFromParcel(Parcel source) {
                return new Info(source.readString(), source.readString());
            }

            public Info[] newArray(int size) {
                return new Info[size];
            }
        };
        public final String methodPath;
        public final String sessionId;

        private Info(String id, String path) {
            this.sessionId = id;
            this.methodPath = path;
        }

        public static Info getInfo(Session s) {
            String access$000 = s.getFullSessionId();
            boolean z = !Log.DEBUG && s.isSessionExternal();
            return new Info(access$000, s.getFullMethodPath(z));
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel destination, int flags) {
            destination.writeString(this.sessionId);
            destination.writeString(this.methodPath);
        }
    }

    public Session(String sessionId, String shortMethodName, long startTimeMs, boolean isStartedFromActiveSession, String ownerInfo) {
        setSessionId(sessionId);
        setShortMethodName(shortMethodName);
        this.mExecutionStartTimeMs = startTimeMs;
        this.mParentSession = null;
        this.mChildSessions = new ArrayList(5);
        this.mIsStartedFromActiveSession = isStartedFromActiveSession;
        this.mOwnerInfo = ownerInfo;
    }

    public void setSessionId(String sessionId) {
        if (sessionId == null) {
            this.mSessionId = "?";
        }
        this.mSessionId = sessionId;
    }

    public String getShortMethodName() {
        return this.mShortMethodName;
    }

    public void setShortMethodName(String shortMethodName) {
        if (shortMethodName == null) {
            shortMethodName = "";
        }
        this.mShortMethodName = shortMethodName;
    }

    public void setIsExternal(boolean isExternal) {
        this.mIsExternal = isExternal;
    }

    public boolean isExternal() {
        return this.mIsExternal;
    }

    public void setParentSession(Session parentSession) {
        this.mParentSession = parentSession;
    }

    public void addChild(Session childSession) {
        if (childSession != null) {
            this.mChildSessions.add(childSession);
        }
    }

    public void removeChild(Session child) {
        if (child != null) {
            this.mChildSessions.remove(child);
        }
    }

    public long getExecutionStartTimeMilliseconds() {
        return this.mExecutionStartTimeMs;
    }

    public void setExecutionStartTimeMs(long startTimeMs) {
        this.mExecutionStartTimeMs = startTimeMs;
    }

    public Session getParentSession() {
        return this.mParentSession;
    }

    public ArrayList<Session> getChildSessions() {
        return this.mChildSessions;
    }

    public boolean isSessionCompleted() {
        return this.mIsCompleted;
    }

    public boolean isStartedFromActiveSession() {
        return this.mIsStartedFromActiveSession;
    }

    public Info getInfo() {
        return Info.getInfo(this);
    }

    @VisibleForTesting
    public String getSessionId() {
        return this.mSessionId;
    }

    public void markSessionCompleted(long executionEndTimeMs) {
        this.mExecutionEndTimeMs = executionEndTimeMs;
        this.mIsCompleted = true;
    }

    public long getLocalExecutionTime() {
        long j = this.mExecutionEndTimeMs;
        if (j == -1) {
            return -1;
        }
        return j - this.mExecutionStartTimeMs;
    }

    public synchronized String getNextChildId() {
        int i;
        i = this.mChildCounter;
        this.mChildCounter = i + 1;
        return String.valueOf(i);
    }

    private String getFullSessionId() {
        Session parentSession = this.mParentSession;
        if (parentSession == null) {
            return this.mSessionId;
        }
        if (!Log.VERBOSE) {
            return parentSession.getFullSessionId();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(parentSession.getFullSessionId());
        stringBuilder.append(SESSION_SEPARATION_CHAR_CHILD);
        stringBuilder.append(this.mSessionId);
        return stringBuilder.toString();
    }

    public String printFullSessionTree() {
        Session topNode = this;
        while (topNode.getParentSession() != null) {
            topNode = topNode.getParentSession();
        }
        return topNode.printSessionTree();
    }

    public String printSessionTree() {
        StringBuilder sb = new StringBuilder();
        printSessionTree(0, sb);
        return sb.toString();
    }

    private void printSessionTree(int tabI, StringBuilder sb) {
        sb.append(toString());
        Iterator it = this.mChildSessions.iterator();
        while (it.hasNext()) {
            Session child = (Session) it.next();
            sb.append("\n");
            for (int i = 0; i <= tabI; i++) {
                sb.append("\t");
            }
            child.printSessionTree(tabI + 1, sb);
        }
    }

    public String getFullMethodPath(boolean truncatePath) {
        StringBuilder sb = new StringBuilder();
        getFullMethodPath(sb, truncatePath);
        return sb.toString();
    }

    /* JADX WARNING: Missing block: B:26:0x005d, code skipped:
            return;
     */
    private synchronized void getFullMethodPath(java.lang.StringBuilder r5, boolean r6) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = r4.mFullMethodPathCache;	 Catch:{ all -> 0x005e }
        r0 = android.text.TextUtils.isEmpty(r0);	 Catch:{ all -> 0x005e }
        if (r0 != 0) goto L_0x0012;
    L_0x0009:
        if (r6 != 0) goto L_0x0012;
    L_0x000b:
        r0 = r4.mFullMethodPathCache;	 Catch:{ all -> 0x005e }
        r5.append(r0);	 Catch:{ all -> 0x005e }
        monitor-exit(r4);
        return;
    L_0x0012:
        r0 = r4.getParentSession();	 Catch:{ all -> 0x005e }
        r1 = 0;
        if (r0 == 0) goto L_0x002f;
    L_0x0019:
        r2 = r4.mShortMethodName;	 Catch:{ all -> 0x005e }
        r3 = r0.mShortMethodName;	 Catch:{ all -> 0x005e }
        r2 = r2.equals(r3);	 Catch:{ all -> 0x005e }
        if (r2 != 0) goto L_0x0025;
    L_0x0023:
        r2 = 1;
        goto L_0x0026;
    L_0x0025:
        r2 = 0;
    L_0x0026:
        r1 = r2;
        r0.getFullMethodPath(r5, r6);	 Catch:{ all -> 0x005e }
        r2 = "->";
        r5.append(r2);	 Catch:{ all -> 0x005e }
    L_0x002f:
        r2 = r4.isExternal();	 Catch:{ all -> 0x005e }
        if (r2 == 0) goto L_0x004d;
    L_0x0035:
        if (r6 == 0) goto L_0x003d;
    L_0x0037:
        r2 = "...";
        r5.append(r2);	 Catch:{ all -> 0x005e }
        goto L_0x0052;
    L_0x003d:
        r2 = "(";
        r5.append(r2);	 Catch:{ all -> 0x005e }
        r2 = r4.mShortMethodName;	 Catch:{ all -> 0x005e }
        r5.append(r2);	 Catch:{ all -> 0x005e }
        r2 = ")";
        r5.append(r2);	 Catch:{ all -> 0x005e }
        goto L_0x0052;
    L_0x004d:
        r2 = r4.mShortMethodName;	 Catch:{ all -> 0x005e }
        r5.append(r2);	 Catch:{ all -> 0x005e }
    L_0x0052:
        if (r1 == 0) goto L_0x005c;
    L_0x0054:
        if (r6 != 0) goto L_0x005c;
    L_0x0056:
        r2 = r5.toString();	 Catch:{ all -> 0x005e }
        r4.mFullMethodPathCache = r2;	 Catch:{ all -> 0x005e }
    L_0x005c:
        monitor-exit(r4);
        return;
    L_0x005e:
        r5 = move-exception;
        monitor-exit(r4);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.telecom.Logging.Session.getFullMethodPath(java.lang.StringBuilder, boolean):void");
    }

    private boolean isSessionExternal() {
        if (getParentSession() == null) {
            return isExternal();
        }
        return getParentSession().isSessionExternal();
    }

    public int hashCode() {
        String str = this.mSessionId;
        int i = 0;
        int result = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.mShortMethodName;
        int hashCode = (result + (str2 != null ? str2.hashCode() : 0)) * 31;
        long j = this.mExecutionStartTimeMs;
        result = (hashCode + ((int) (j ^ (j >>> 32)))) * 31;
        j = this.mExecutionEndTimeMs;
        hashCode = (result + ((int) (j ^ (j >>> 32)))) * 31;
        Session session = this.mParentSession;
        result = (hashCode + (session != null ? session.hashCode() : 0)) * 31;
        ArrayList arrayList = this.mChildSessions;
        result = (((((((result + (arrayList != null ? arrayList.hashCode() : 0)) * 31) + this.mIsCompleted) * 31) + this.mChildCounter) * 31) + this.mIsStartedFromActiveSession) * 31;
        str2 = this.mOwnerInfo;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return result + i;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        if (this.mExecutionStartTimeMs != session.mExecutionStartTimeMs || this.mExecutionEndTimeMs != session.mExecutionEndTimeMs || this.mIsCompleted != session.mIsCompleted || this.mChildCounter != session.mChildCounter || this.mIsStartedFromActiveSession != session.mIsStartedFromActiveSession) {
            return false;
        }
        String str = this.mSessionId;
        if (!str == null ? str.equals(session.mSessionId) : session.mSessionId == null) {
            return false;
        }
        str = this.mShortMethodName;
        if (!str == null ? str.equals(session.mShortMethodName) : session.mShortMethodName == null) {
            return false;
        }
        Session session2 = this.mParentSession;
        if (!session2 == null ? session2.equals(session.mParentSession) : session.mParentSession == null) {
            return false;
        }
        ArrayList arrayList = this.mChildSessions;
        if (!arrayList == null ? arrayList.equals(session.mChildSessions) : session.mChildSessions == null) {
            return false;
        }
        str = this.mOwnerInfo;
        if (str != null) {
            z = str.equals(session.mOwnerInfo);
        } else if (session.mOwnerInfo != null) {
            z = false;
        }
        return z;
    }

    public String toString() {
        Session session = this.mParentSession;
        if (session != null && this.mIsStartedFromActiveSession) {
            return session.toString();
        }
        StringBuilder methodName = new StringBuilder();
        methodName.append(getFullMethodPath(false));
        String str = this.mOwnerInfo;
        if (!(str == null || str.isEmpty())) {
            methodName.append("(InCall package: ");
            methodName.append(this.mOwnerInfo);
            methodName.append(")");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(methodName.toString());
        stringBuilder.append("@");
        stringBuilder.append(getFullSessionId());
        return stringBuilder.toString();
    }
}
