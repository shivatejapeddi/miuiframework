package android.view.contentcapture;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.view.autofill.AutofillId;
import com.android.internal.util.Preconditions;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

@SystemApi
public final class ContentCaptureEvent implements Parcelable {
    public static final Creator<ContentCaptureEvent> CREATOR = new Creator<ContentCaptureEvent>() {
        public ContentCaptureEvent createFromParcel(Parcel parcel) {
            int sessionId = parcel.readInt();
            int type = parcel.readInt();
            ContentCaptureEvent event = new ContentCaptureEvent(sessionId, type, parcel.readLong());
            AutofillId id = (AutofillId) parcel.readParcelable(null);
            if (id != null) {
                event.setAutofillId(id);
            }
            ArrayList<AutofillId> ids = parcel.createTypedArrayList(AutofillId.CREATOR);
            if (ids != null) {
                event.setAutofillIds(ids);
            }
            ViewNode node = ViewNode.readFromParcel(parcel);
            if (node != null) {
                event.setViewNode(node);
            }
            event.setText(parcel.readCharSequence());
            if (type == -1 || type == -2) {
                event.setParentSessionId(parcel.readInt());
            }
            if (type == -1 || type == 6) {
                event.setClientContext((ContentCaptureContext) parcel.readParcelable(null));
            }
            return event;
        }

        public ContentCaptureEvent[] newArray(int size) {
            return new ContentCaptureEvent[size];
        }
    };
    private static final String TAG = ContentCaptureEvent.class.getSimpleName();
    public static final int TYPE_CONTEXT_UPDATED = 6;
    public static final int TYPE_SESSION_FINISHED = -2;
    public static final int TYPE_SESSION_PAUSED = 8;
    public static final int TYPE_SESSION_RESUMED = 7;
    public static final int TYPE_SESSION_STARTED = -1;
    public static final int TYPE_VIEW_APPEARED = 1;
    public static final int TYPE_VIEW_DISAPPEARED = 2;
    public static final int TYPE_VIEW_TEXT_CHANGED = 3;
    public static final int TYPE_VIEW_TREE_APPEARED = 5;
    public static final int TYPE_VIEW_TREE_APPEARING = 4;
    private ContentCaptureContext mClientContext;
    private final long mEventTime;
    private AutofillId mId;
    private ArrayList<AutofillId> mIds;
    private ViewNode mNode;
    private int mParentSessionId;
    private final int mSessionId;
    private CharSequence mText;
    private final int mType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface EventType {
    }

    public ContentCaptureEvent(int sessionId, int type, long eventTime) {
        this.mParentSessionId = 0;
        this.mSessionId = sessionId;
        this.mType = type;
        this.mEventTime = eventTime;
    }

    public ContentCaptureEvent(int sessionId, int type) {
        this(sessionId, type, System.currentTimeMillis());
    }

    public ContentCaptureEvent setAutofillId(AutofillId id) {
        this.mId = (AutofillId) Preconditions.checkNotNull(id);
        return this;
    }

    public ContentCaptureEvent setAutofillIds(ArrayList<AutofillId> ids) {
        this.mIds = (ArrayList) Preconditions.checkNotNull(ids);
        return this;
    }

    public ContentCaptureEvent addAutofillId(AutofillId id) {
        Preconditions.checkNotNull(id);
        if (this.mIds == null) {
            this.mIds = new ArrayList();
            AutofillId autofillId = this.mId;
            if (autofillId == null) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("addAutofillId(");
                stringBuilder.append(id);
                stringBuilder.append(") called without an initial id");
                Log.w(str, stringBuilder.toString());
            } else {
                this.mIds.add(autofillId);
                this.mId = null;
            }
        }
        this.mIds.add(id);
        return this;
    }

    public ContentCaptureEvent setParentSessionId(int parentSessionId) {
        this.mParentSessionId = parentSessionId;
        return this;
    }

    public ContentCaptureEvent setClientContext(ContentCaptureContext clientContext) {
        this.mClientContext = clientContext;
        return this;
    }

    public int getSessionId() {
        return this.mSessionId;
    }

    public int getParentSessionId() {
        return this.mParentSessionId;
    }

    public ContentCaptureContext getContentCaptureContext() {
        return this.mClientContext;
    }

    public ContentCaptureEvent setViewNode(ViewNode node) {
        this.mNode = (ViewNode) Preconditions.checkNotNull(node);
        return this;
    }

    public ContentCaptureEvent setText(CharSequence text) {
        this.mText = text;
        return this;
    }

    public int getType() {
        return this.mType;
    }

    public long getEventTime() {
        return this.mEventTime;
    }

    public ViewNode getViewNode() {
        return this.mNode;
    }

    public AutofillId getId() {
        return this.mId;
    }

    public List<AutofillId> getIds() {
        return this.mIds;
    }

    public CharSequence getText() {
        return this.mText;
    }

    public void mergeEvent(ContentCaptureEvent event) {
        Preconditions.checkNotNull(event);
        int eventType = event.getType();
        String str = "mergeEvent(";
        String str2;
        StringBuilder stringBuilder;
        if (this.mType != eventType) {
            str2 = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(getTypeAsString(eventType));
            stringBuilder.append(") cannot be merged with different eventType=");
            stringBuilder.append(getTypeAsString(this.mType));
            Log.e(str2, stringBuilder.toString());
        } else if (eventType == 2) {
            List<AutofillId> ids = event.getIds();
            AutofillId id = event.getId();
            StringBuilder stringBuilder2;
            if (ids != null) {
                if (id != null) {
                    String str3 = TAG;
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("got TYPE_VIEW_DISAPPEARED event with both id and ids: ");
                    stringBuilder2.append(event);
                    Log.w(str3, stringBuilder2.toString());
                }
                for (int i = 0; i < ids.size(); i++) {
                    addAutofillId((AutofillId) ids.get(i));
                }
            } else if (id != null) {
                addAutofillId(id);
            } else {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("mergeEvent(): got TYPE_VIEW_DISAPPEARED event with neither id or ids: ");
                stringBuilder2.append(event);
                throw new IllegalArgumentException(stringBuilder2.toString());
            }
        } else {
            if (eventType == 3) {
                setText(event.getText());
            } else {
                str2 = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(getTypeAsString(eventType));
                stringBuilder.append(") does not support this event type.");
                Log.e(str2, stringBuilder.toString());
            }
        }
    }

    public void dump(PrintWriter pw) {
        pw.print("type=");
        pw.print(getTypeAsString(this.mType));
        pw.print(", time=");
        pw.print(this.mEventTime);
        if (this.mId != null) {
            pw.print(", id=");
            pw.print(this.mId);
        }
        if (this.mIds != null) {
            pw.print(", ids=");
            pw.print(this.mIds);
        }
        if (this.mNode != null) {
            pw.print(", mNode.id=");
            pw.print(this.mNode.getAutofillId());
        }
        if (this.mSessionId != 0) {
            pw.print(", sessionId=");
            pw.print(this.mSessionId);
        }
        if (this.mParentSessionId != 0) {
            pw.print(", parentSessionId=");
            pw.print(this.mParentSessionId);
        }
        if (this.mText != null) {
            pw.print(", text=");
            pw.println(ContentCaptureHelper.getSanitizedString(this.mText));
        }
        if (this.mClientContext != null) {
            pw.print(", context=");
            this.mClientContext.dump(pw);
            pw.println();
        }
    }

    public String toString() {
        StringBuilder string = new StringBuilder("ContentCaptureEvent[type=").append(getTypeAsString(this.mType));
        string.append(", session=");
        string.append(this.mSessionId);
        if (this.mType == -1 && this.mParentSessionId != 0) {
            string.append(", parent=");
            string.append(this.mParentSessionId);
        }
        String str = ", id=";
        if (this.mId != null) {
            string.append(str);
            string.append(this.mId);
        }
        if (this.mIds != null) {
            string.append(", ids=");
            string.append(this.mIds);
        }
        String className = this.mNode;
        if (className != null) {
            className = className.getClassName();
            if (this.mNode != null) {
                string.append(", class=");
                string.append(className);
            }
            string.append(str);
            string.append(this.mNode.getAutofillId());
        }
        if (this.mText != null) {
            string.append(", text=");
            string.append(ContentCaptureHelper.getSanitizedString(this.mText));
        }
        if (this.mClientContext != null) {
            string.append(", context=");
            string.append(this.mClientContext);
        }
        string.append(']');
        return string.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.mSessionId);
        parcel.writeInt(this.mType);
        parcel.writeLong(this.mEventTime);
        parcel.writeParcelable(this.mId, flags);
        parcel.writeTypedList(this.mIds);
        ViewNode.writeToParcel(parcel, this.mNode, flags);
        parcel.writeCharSequence(this.mText);
        int i = this.mType;
        if (i == -1 || i == -2) {
            parcel.writeInt(this.mParentSessionId);
        }
        i = this.mType;
        if (i == -1 || i == 6) {
            parcel.writeParcelable(this.mClientContext, flags);
        }
    }

    public static String getTypeAsString(int type) {
        switch (type) {
            case -2:
                return "SESSION_FINISHED";
            case -1:
                return "SESSION_STARTED";
            case 1:
                return "VIEW_APPEARED";
            case 2:
                return "VIEW_DISAPPEARED";
            case 3:
                return "VIEW_TEXT_CHANGED";
            case 4:
                return "VIEW_TREE_APPEARING";
            case 5:
                return "VIEW_TREE_APPEARED";
            case 6:
                return "CONTEXT_UPDATED";
            case 7:
                return "SESSION_RESUMED";
            case 8:
                return "SESSION_PAUSED";
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("UKNOWN_TYPE: ");
                stringBuilder.append(type);
                return stringBuilder.toString();
        }
    }
}
