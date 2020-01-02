package android.view.textclassifier;

import android.app.Person;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.SpannedString;
import android.view.textclassifier.TextClassifier.EntityConfig;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ConversationActions implements Parcelable {
    public static final Creator<ConversationActions> CREATOR = new Creator<ConversationActions>() {
        public ConversationActions createFromParcel(Parcel in) {
            return new ConversationActions(in, null);
        }

        public ConversationActions[] newArray(int size) {
            return new ConversationActions[size];
        }
    };
    private final List<ConversationAction> mConversationActions;
    private final String mId;

    public static final class Message implements Parcelable {
        public static final Creator<Message> CREATOR = new Creator<Message>() {
            public Message createFromParcel(Parcel in) {
                return new Message(in, null);
            }

            public Message[] newArray(int size) {
                return new Message[size];
            }
        };
        public static final Person PERSON_USER_OTHERS = new android.app.Person.Builder().setKey("text-classifier-conversation-actions-user-others").build();
        public static final Person PERSON_USER_SELF = new android.app.Person.Builder().setKey("text-classifier-conversation-actions-user-self").build();
        private final Person mAuthor;
        private final Bundle mExtras;
        private final ZonedDateTime mReferenceTime;
        private final CharSequence mText;

        public static final class Builder {
            private Person mAuthor;
            private Bundle mExtras;
            private ZonedDateTime mReferenceTime;
            private CharSequence mText;

            public Builder(Person author) {
                this.mAuthor = (Person) Preconditions.checkNotNull(author);
            }

            public Builder setText(CharSequence text) {
                this.mText = text;
                return this;
            }

            public Builder setReferenceTime(ZonedDateTime referenceTime) {
                this.mReferenceTime = referenceTime;
                return this;
            }

            public Builder setExtras(Bundle bundle) {
                this.mExtras = bundle;
                return this;
            }

            public Message build() {
                Person person = this.mAuthor;
                ZonedDateTime zonedDateTime = this.mReferenceTime;
                CharSequence charSequence = this.mText;
                CharSequence spannedString = charSequence == null ? null : new SpannedString(charSequence);
                Bundle bundle = this.mExtras;
                if (bundle == null) {
                    bundle = Bundle.EMPTY;
                }
                return new Message(person, zonedDateTime, spannedString, bundle, null);
            }
        }

        /* synthetic */ Message(Person x0, ZonedDateTime x1, CharSequence x2, Bundle x3, AnonymousClass1 x4) {
            this(x0, x1, x2, x3);
        }

        /* synthetic */ Message(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        private Message(Person author, ZonedDateTime referenceTime, CharSequence text, Bundle bundle) {
            this.mAuthor = author;
            this.mReferenceTime = referenceTime;
            this.mText = text;
            this.mExtras = (Bundle) Preconditions.checkNotNull(bundle);
        }

        private Message(Parcel in) {
            ZonedDateTime zonedDateTime = null;
            this.mAuthor = (Person) in.readParcelable(null);
            if (in.readInt() != 0) {
                zonedDateTime = ZonedDateTime.parse(in.readString(), DateTimeFormatter.ISO_ZONED_DATE_TIME);
            }
            this.mReferenceTime = zonedDateTime;
            this.mText = in.readCharSequence();
            this.mExtras = in.readBundle();
        }

        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeParcelable(this.mAuthor, flags);
            parcel.writeInt(this.mReferenceTime != null ? 1 : 0);
            ZonedDateTime zonedDateTime = this.mReferenceTime;
            if (zonedDateTime != null) {
                parcel.writeString(zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
            }
            parcel.writeCharSequence(this.mText);
            parcel.writeBundle(this.mExtras);
        }

        public int describeContents() {
            return 0;
        }

        public Person getAuthor() {
            return this.mAuthor;
        }

        public ZonedDateTime getReferenceTime() {
            return this.mReferenceTime;
        }

        public CharSequence getText() {
            return this.mText;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }
    }

    public static final class Request implements Parcelable {
        public static final Creator<Request> CREATOR = new Creator<Request>() {
            public Request createFromParcel(Parcel in) {
                return Request.readFromParcel(in);
            }

            public Request[] newArray(int size) {
                return new Request[size];
            }
        };
        public static final String HINT_FOR_IN_APP = "in_app";
        public static final String HINT_FOR_NOTIFICATION = "notification";
        private String mCallingPackageName;
        private final List<Message> mConversation;
        private Bundle mExtras;
        private final List<String> mHints;
        private final int mMaxSuggestions;
        private final EntityConfig mTypeConfig;

        public static final class Builder {
            private List<Message> mConversation;
            private Bundle mExtras;
            private List<String> mHints;
            private int mMaxSuggestions = -1;
            private EntityConfig mTypeConfig;

            public Builder(List<Message> conversation) {
                this.mConversation = (List) Preconditions.checkNotNull(conversation);
            }

            public Builder setHints(List<String> hints) {
                this.mHints = hints;
                return this;
            }

            public Builder setTypeConfig(EntityConfig typeConfig) {
                this.mTypeConfig = typeConfig;
                return this;
            }

            public Builder setMaxSuggestions(int maxSuggestions) {
                this.mMaxSuggestions = Preconditions.checkArgumentNonnegative(maxSuggestions);
                return this;
            }

            public Builder setExtras(Bundle bundle) {
                this.mExtras = bundle;
                return this;
            }

            public Request build() {
                EntityConfig build;
                List emptyList;
                List unmodifiableList = Collections.unmodifiableList(this.mConversation);
                EntityConfig entityConfig = this.mTypeConfig;
                if (entityConfig == null) {
                    build = new android.view.textclassifier.TextClassifier.EntityConfig.Builder().build();
                } else {
                    build = entityConfig;
                }
                int i = this.mMaxSuggestions;
                List list = this.mHints;
                if (list == null) {
                    emptyList = Collections.emptyList();
                } else {
                    emptyList = Collections.unmodifiableList(list);
                }
                Bundle bundle = this.mExtras;
                if (bundle == null) {
                    bundle = Bundle.EMPTY;
                }
                return new Request(unmodifiableList, build, i, emptyList, bundle, null);
            }
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface Hint {
        }

        /* synthetic */ Request(List x0, EntityConfig x1, int x2, List x3, Bundle x4, AnonymousClass1 x5) {
            this(x0, x1, x2, x3, x4);
        }

        private Request(List<Message> conversation, EntityConfig typeConfig, int maxSuggestions, List<String> hints, Bundle extras) {
            this.mConversation = (List) Preconditions.checkNotNull(conversation);
            this.mTypeConfig = (EntityConfig) Preconditions.checkNotNull(typeConfig);
            this.mMaxSuggestions = maxSuggestions;
            this.mHints = hints;
            this.mExtras = extras;
        }

        private static Request readFromParcel(Parcel in) {
            List<Message> conversation = new ArrayList();
            in.readParcelableList(conversation, null);
            EntityConfig typeConfig = (EntityConfig) in.readParcelable(null);
            int maxSuggestions = in.readInt();
            List hints = new ArrayList();
            in.readStringList(hints);
            String callingPackageName = in.readString();
            Request request = new Request(conversation, typeConfig, maxSuggestions, hints, in.readBundle());
            request.setCallingPackageName(callingPackageName);
            return request;
        }

        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeParcelableList(this.mConversation, flags);
            parcel.writeParcelable(this.mTypeConfig, flags);
            parcel.writeInt(this.mMaxSuggestions);
            parcel.writeStringList(this.mHints);
            parcel.writeString(this.mCallingPackageName);
            parcel.writeBundle(this.mExtras);
        }

        public int describeContents() {
            return 0;
        }

        public EntityConfig getTypeConfig() {
            return this.mTypeConfig;
        }

        public List<Message> getConversation() {
            return this.mConversation;
        }

        public int getMaxSuggestions() {
            return this.mMaxSuggestions;
        }

        public List<String> getHints() {
            return this.mHints;
        }

        @VisibleForTesting(visibility = Visibility.PACKAGE)
        public void setCallingPackageName(String callingPackageName) {
            this.mCallingPackageName = callingPackageName;
        }

        public String getCallingPackageName() {
            return this.mCallingPackageName;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }
    }

    public ConversationActions(List<ConversationAction> conversationActions, String id) {
        this.mConversationActions = Collections.unmodifiableList((List) Preconditions.checkNotNull(conversationActions));
        this.mId = id;
    }

    private ConversationActions(Parcel in) {
        this.mConversationActions = Collections.unmodifiableList(in.createTypedArrayList(ConversationAction.CREATOR));
        this.mId = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeTypedList(this.mConversationActions);
        parcel.writeString(this.mId);
    }

    public List<ConversationAction> getConversationActions() {
        return this.mConversationActions;
    }

    public String getId() {
        return this.mId;
    }
}
