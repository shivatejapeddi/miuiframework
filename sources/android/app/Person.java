package android.app;

import android.graphics.drawable.Icon;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class Person implements Parcelable {
    public static final Creator<Person> CREATOR = new Creator<Person>() {
        public Person createFromParcel(Parcel in) {
            return new Person(in, null);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
    private Icon mIcon;
    private boolean mIsBot;
    private boolean mIsImportant;
    private String mKey;
    private CharSequence mName;
    private String mUri;

    public static class Builder {
        private Icon mIcon;
        private boolean mIsBot;
        private boolean mIsImportant;
        private String mKey;
        private CharSequence mName;
        private String mUri;

        /* synthetic */ Builder(Person x0, AnonymousClass1 x1) {
            this(x0);
        }

        private Builder(Person person) {
            this.mName = person.mName;
            this.mIcon = person.mIcon;
            this.mUri = person.mUri;
            this.mKey = person.mKey;
            this.mIsBot = person.mIsBot;
            this.mIsImportant = person.mIsImportant;
        }

        public Builder setName(CharSequence name) {
            this.mName = name;
            return this;
        }

        public Builder setIcon(Icon icon) {
            this.mIcon = icon;
            return this;
        }

        public Builder setUri(String uri) {
            this.mUri = uri;
            return this;
        }

        public Builder setKey(String key) {
            this.mKey = key;
            return this;
        }

        public Builder setImportant(boolean isImportant) {
            this.mIsImportant = isImportant;
            return this;
        }

        public Builder setBot(boolean isBot) {
            this.mIsBot = isBot;
            return this;
        }

        public Person build() {
            return new Person(this, null);
        }
    }

    private Person(Parcel in) {
        this.mName = in.readCharSequence();
        if (in.readInt() != 0) {
            this.mIcon = (Icon) Icon.CREATOR.createFromParcel(in);
        }
        this.mUri = in.readString();
        this.mKey = in.readString();
        this.mIsImportant = in.readBoolean();
        this.mIsBot = in.readBoolean();
    }

    private Person(Builder builder) {
        this.mName = builder.mName;
        this.mIcon = builder.mIcon;
        this.mUri = builder.mUri;
        this.mKey = builder.mKey;
        this.mIsBot = builder.mIsBot;
        this.mIsImportant = builder.mIsImportant;
    }

    public Builder toBuilder() {
        return new Builder(this, null);
    }

    public String getUri() {
        return this.mUri;
    }

    public CharSequence getName() {
        return this.mName;
    }

    public Icon getIcon() {
        return this.mIcon;
    }

    public String getKey() {
        return this.mKey;
    }

    public boolean isBot() {
        return this.mIsBot;
    }

    public boolean isImportant() {
        return this.mIsImportant;
    }

    public String resolveToLegacyUri() {
        String str = this.mUri;
        if (str != null) {
            return str;
        }
        if (this.mName == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("name:");
        stringBuilder.append(this.mName);
        return stringBuilder.toString();
    }

    /* JADX WARNING: Missing block: B:7:0x0018, code skipped:
            if (r0.mIcon == null) goto L_0x0025;
     */
    /* JADX WARNING: Missing block: B:11:0x0023, code skipped:
            if (r2.sameAs(r3) != false) goto L_0x0025;
     */
    public boolean equals(java.lang.Object r5) {
        /*
        r4 = this;
        r0 = r5 instanceof android.app.Person;
        r1 = 0;
        if (r0 == 0) goto L_0x0049;
    L_0x0005:
        r0 = r5;
        r0 = (android.app.Person) r0;
        r2 = r4.mName;
        r3 = r0.mName;
        r2 = java.util.Objects.equals(r2, r3);
        if (r2 == 0) goto L_0x0047;
    L_0x0012:
        r2 = r4.mIcon;
        if (r2 != 0) goto L_0x001b;
    L_0x0016:
        r2 = r0.mIcon;
        if (r2 != 0) goto L_0x0047;
    L_0x001a:
        goto L_0x0025;
    L_0x001b:
        r3 = r0.mIcon;
        if (r3 == 0) goto L_0x0047;
    L_0x001f:
        r2 = r2.sameAs(r3);
        if (r2 == 0) goto L_0x0047;
    L_0x0025:
        r2 = r4.mUri;
        r3 = r0.mUri;
        r2 = java.util.Objects.equals(r2, r3);
        if (r2 == 0) goto L_0x0047;
    L_0x002f:
        r2 = r4.mKey;
        r3 = r0.mKey;
        r2 = java.util.Objects.equals(r2, r3);
        if (r2 == 0) goto L_0x0047;
    L_0x0039:
        r2 = r4.mIsBot;
        r3 = r0.mIsBot;
        if (r2 != r3) goto L_0x0047;
    L_0x003f:
        r2 = r4.mIsImportant;
        r3 = r0.mIsImportant;
        if (r2 != r3) goto L_0x0047;
    L_0x0045:
        r1 = 1;
        goto L_0x0048;
    L_0x0048:
        return r1;
    L_0x0049:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Person.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mName, this.mIcon, this.mUri, this.mKey, Boolean.valueOf(this.mIsBot), Boolean.valueOf(this.mIsImportant)});
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeCharSequence(this.mName);
        if (this.mIcon != null) {
            dest.writeInt(1);
            this.mIcon.writeToParcel(dest, 0);
        } else {
            dest.writeInt(0);
        }
        dest.writeString(this.mUri);
        dest.writeString(this.mKey);
        dest.writeBoolean(this.mIsImportant);
        dest.writeBoolean(this.mIsBot);
    }
}
