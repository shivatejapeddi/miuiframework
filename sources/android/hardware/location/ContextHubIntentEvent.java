package android.hardware.location;

import android.annotation.SystemApi;
import android.content.Intent;
import com.android.internal.util.Preconditions;

@SystemApi
public class ContextHubIntentEvent {
    private final ContextHubInfo mContextHubInfo;
    private final int mEventType;
    private final int mNanoAppAbortCode;
    private final long mNanoAppId;
    private final NanoAppMessage mNanoAppMessage;

    private ContextHubIntentEvent(ContextHubInfo contextHubInfo, int eventType, long nanoAppId, NanoAppMessage nanoAppMessage, int nanoAppAbortCode) {
        this.mContextHubInfo = contextHubInfo;
        this.mEventType = eventType;
        this.mNanoAppId = nanoAppId;
        this.mNanoAppMessage = nanoAppMessage;
        this.mNanoAppAbortCode = nanoAppAbortCode;
    }

    private ContextHubIntentEvent(ContextHubInfo contextHubInfo, int eventType) {
        this(contextHubInfo, eventType, -1, null, -1);
    }

    private ContextHubIntentEvent(ContextHubInfo contextHubInfo, int eventType, long nanoAppId) {
        this(contextHubInfo, eventType, nanoAppId, null, -1);
    }

    private ContextHubIntentEvent(ContextHubInfo contextHubInfo, int eventType, long nanoAppId, NanoAppMessage nanoAppMessage) {
        this(contextHubInfo, eventType, nanoAppId, nanoAppMessage, -1);
    }

    private ContextHubIntentEvent(ContextHubInfo contextHubInfo, int eventType, long nanoAppId, int nanoAppAbortCode) {
        this(contextHubInfo, eventType, nanoAppId, null, nanoAppAbortCode);
    }

    public static ContextHubIntentEvent fromIntent(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null");
        String str = ContextHubManager.EXTRA_CONTEXT_HUB_INFO;
        hasExtraOrThrow(intent, str);
        ContextHubInfo info = (ContextHubInfo) intent.getParcelableExtra(str);
        if (info != null) {
            int eventType = getIntExtraOrThrow(intent, ContextHubManager.EXTRA_EVENT_TYPE);
            switch (eventType) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    long nanoAppId = getLongExtraOrThrow(intent, ContextHubManager.EXTRA_NANOAPP_ID);
                    if (eventType == 5) {
                        String str2 = ContextHubManager.EXTRA_MESSAGE;
                        hasExtraOrThrow(intent, str2);
                        NanoAppMessage message = (NanoAppMessage) intent.getParcelableExtra(str2);
                        if (message != null) {
                            return new ContextHubIntentEvent(info, eventType, nanoAppId, message);
                        }
                        throw new IllegalArgumentException("NanoAppMessage extra was null");
                    } else if (eventType == 4) {
                        return new ContextHubIntentEvent(info, eventType, nanoAppId, getIntExtraOrThrow(intent, ContextHubManager.EXTRA_NANOAPP_ABORT_CODE));
                    } else {
                        return new ContextHubIntentEvent(info, eventType, nanoAppId);
                    }
                case 6:
                    return new ContextHubIntentEvent(info, eventType);
                default:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown intent event type ");
                    stringBuilder.append(eventType);
                    throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
        throw new IllegalArgumentException("ContextHubInfo extra was null");
    }

    public int getEventType() {
        return this.mEventType;
    }

    public ContextHubInfo getContextHubInfo() {
        return this.mContextHubInfo;
    }

    public long getNanoAppId() {
        if (this.mEventType != 6) {
            return this.mNanoAppId;
        }
        throw new UnsupportedOperationException("Cannot invoke getNanoAppId() on Context Hub reset event");
    }

    public int getNanoAppAbortCode() {
        if (this.mEventType == 4) {
            return this.mNanoAppAbortCode;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot invoke getNanoAppAbortCode() on non-abort event: ");
        stringBuilder.append(this.mEventType);
        throw new UnsupportedOperationException(stringBuilder.toString());
    }

    public NanoAppMessage getNanoAppMessage() {
        if (this.mEventType == 5) {
            return this.mNanoAppMessage;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot invoke getNanoAppMessage() on non-message event: ");
        stringBuilder.append(this.mEventType);
        throw new UnsupportedOperationException(stringBuilder.toString());
    }

    public String toString() {
        StringBuilder stringBuilder;
        String out = new StringBuilder();
        out.append("ContextHubIntentEvent[eventType = ");
        out.append(this.mEventType);
        out.append(", contextHubId = ");
        out.append(this.mContextHubInfo.getId());
        out = out.toString();
        if (this.mEventType != 6) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(out);
            stringBuilder.append(", nanoAppId = 0x");
            stringBuilder.append(Long.toHexString(this.mNanoAppId));
            out = stringBuilder.toString();
        }
        if (this.mEventType == 4) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(out);
            stringBuilder.append(", nanoAppAbortCode = ");
            stringBuilder.append(this.mNanoAppAbortCode);
            out = stringBuilder.toString();
        }
        if (this.mEventType == 5) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(out);
            stringBuilder.append(", nanoAppMessage = ");
            stringBuilder.append(this.mNanoAppMessage);
            out = stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(out);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public boolean equals(Object object) {
        int i = 1;
        if (object == this) {
            return true;
        }
        boolean isEqual = false;
        if (object instanceof ContextHubIntentEvent) {
            ContextHubIntentEvent other = (ContextHubIntentEvent) object;
            if (other.getEventType() == this.mEventType && other.getContextHubInfo().equals(this.mContextHubInfo)) {
                isEqual = true;
                try {
                    if (this.mEventType != 6) {
                        isEqual = true & (other.getNanoAppId() == this.mNanoAppId ? 1 : 0);
                    }
                    if (this.mEventType == 4) {
                        if (other.getNanoAppAbortCode() != this.mNanoAppAbortCode) {
                            i = 0;
                        }
                        isEqual = i & isEqual;
                    }
                    if (this.mEventType == 5) {
                        isEqual = other.getNanoAppMessage().equals(this.mNanoAppMessage) & isEqual;
                    }
                } catch (UnsupportedOperationException e) {
                    isEqual = false;
                }
            }
        }
        return isEqual;
    }

    private static void hasExtraOrThrow(Intent intent, String extra) {
        if (!intent.hasExtra(extra)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Intent did not have extra: ");
            stringBuilder.append(extra);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    private static int getIntExtraOrThrow(Intent intent, String extra) {
        hasExtraOrThrow(intent, extra);
        return intent.getIntExtra(extra, -1);
    }

    private static long getLongExtraOrThrow(Intent intent, String extra) {
        hasExtraOrThrow(intent, extra);
        return intent.getLongExtra(extra, -1);
    }
}
