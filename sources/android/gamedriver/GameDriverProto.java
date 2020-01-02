package android.gamedriver;

import com.android.framework.protobuf.AbstractMessageLite;
import com.android.framework.protobuf.ByteString;
import com.android.framework.protobuf.CodedInputStream;
import com.android.framework.protobuf.CodedOutputStream;
import com.android.framework.protobuf.ExtensionRegistryLite;
import com.android.framework.protobuf.GeneratedMessageLite;
import com.android.framework.protobuf.GeneratedMessageLite.MethodToInvoke;
import com.android.framework.protobuf.Internal.ProtobufList;
import com.android.framework.protobuf.InvalidProtocolBufferException;
import com.android.framework.protobuf.MessageLite;
import com.android.framework.protobuf.MessageLiteOrBuilder;
import com.android.framework.protobuf.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public final class GameDriverProto {

    public interface BlacklistOrBuilder extends MessageLiteOrBuilder {
        String getPackageNames(int i);

        ByteString getPackageNamesBytes(int i);

        int getPackageNamesCount();

        List<String> getPackageNamesList();

        long getVersionCode();

        boolean hasVersionCode();
    }

    public static final class Blacklist extends GeneratedMessageLite<Blacklist, Builder> implements BlacklistOrBuilder {
        private static final Blacklist DEFAULT_INSTANCE = new Blacklist();
        public static final int PACKAGE_NAMES_FIELD_NUMBER = 2;
        private static volatile Parser<Blacklist> PARSER = null;
        public static final int VERSION_CODE_FIELD_NUMBER = 1;
        private int bitField0_;
        private ProtobufList<String> packageNames_ = GeneratedMessageLite.emptyProtobufList();
        private long versionCode_ = 0;

        public static final class Builder extends com.android.framework.protobuf.GeneratedMessageLite.Builder<Blacklist, Builder> implements BlacklistOrBuilder {
            private Builder() {
                super(Blacklist.DEFAULT_INSTANCE);
            }

            public boolean hasVersionCode() {
                return ((Blacklist) this.instance).hasVersionCode();
            }

            public long getVersionCode() {
                return ((Blacklist) this.instance).getVersionCode();
            }

            public Builder setVersionCode(long value) {
                copyOnWrite();
                ((Blacklist) this.instance).setVersionCode(value);
                return this;
            }

            public Builder clearVersionCode() {
                copyOnWrite();
                ((Blacklist) this.instance).clearVersionCode();
                return this;
            }

            public List<String> getPackageNamesList() {
                return Collections.unmodifiableList(((Blacklist) this.instance).getPackageNamesList());
            }

            public int getPackageNamesCount() {
                return ((Blacklist) this.instance).getPackageNamesCount();
            }

            public String getPackageNames(int index) {
                return ((Blacklist) this.instance).getPackageNames(index);
            }

            public ByteString getPackageNamesBytes(int index) {
                return ((Blacklist) this.instance).getPackageNamesBytes(index);
            }

            public Builder setPackageNames(int index, String value) {
                copyOnWrite();
                ((Blacklist) this.instance).setPackageNames(index, value);
                return this;
            }

            public Builder addPackageNames(String value) {
                copyOnWrite();
                ((Blacklist) this.instance).addPackageNames(value);
                return this;
            }

            public Builder addAllPackageNames(Iterable<String> values) {
                copyOnWrite();
                ((Blacklist) this.instance).addAllPackageNames(values);
                return this;
            }

            public Builder clearPackageNames() {
                copyOnWrite();
                ((Blacklist) this.instance).clearPackageNames();
                return this;
            }

            public Builder addPackageNamesBytes(ByteString value) {
                copyOnWrite();
                ((Blacklist) this.instance).addPackageNamesBytes(value);
                return this;
            }
        }

        private Blacklist() {
        }

        public boolean hasVersionCode() {
            return (this.bitField0_ & 1) == 1;
        }

        public long getVersionCode() {
            return this.versionCode_;
        }

        private void setVersionCode(long value) {
            this.bitField0_ |= 1;
            this.versionCode_ = value;
        }

        private void clearVersionCode() {
            this.bitField0_ &= -2;
            this.versionCode_ = 0;
        }

        public List<String> getPackageNamesList() {
            return this.packageNames_;
        }

        public int getPackageNamesCount() {
            return this.packageNames_.size();
        }

        public String getPackageNames(int index) {
            return (String) this.packageNames_.get(index);
        }

        public ByteString getPackageNamesBytes(int index) {
            return ByteString.copyFromUtf8((String) this.packageNames_.get(index));
        }

        private void ensurePackageNamesIsMutable() {
            if (!this.packageNames_.isModifiable()) {
                this.packageNames_ = GeneratedMessageLite.mutableCopy(this.packageNames_);
            }
        }

        private void setPackageNames(int index, String value) {
            if (value != null) {
                ensurePackageNamesIsMutable();
                this.packageNames_.set(index, value);
                return;
            }
            throw new NullPointerException();
        }

        private void addPackageNames(String value) {
            if (value != null) {
                ensurePackageNamesIsMutable();
                this.packageNames_.add(value);
                return;
            }
            throw new NullPointerException();
        }

        private void addAllPackageNames(Iterable<String> values) {
            ensurePackageNamesIsMutable();
            AbstractMessageLite.addAll(values, this.packageNames_);
        }

        private void clearPackageNames() {
            this.packageNames_ = GeneratedMessageLite.emptyProtobufList();
        }

        private void addPackageNamesBytes(ByteString value) {
            if (value != null) {
                ensurePackageNamesIsMutable();
                this.packageNames_.add(value.toStringUtf8());
                return;
            }
            throw new NullPointerException();
        }

        public void writeTo(CodedOutputStream output) throws IOException {
            if ((this.bitField0_ & 1) == 1) {
                output.writeInt64(1, this.versionCode_);
            }
            for (int i = 0; i < this.packageNames_.size(); i++) {
                output.writeString(2, (String) this.packageNames_.get(i));
            }
            this.unknownFields.writeTo(output);
        }

        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            size = 0;
            if ((this.bitField0_ & 1) == 1) {
                size = 0 + CodedOutputStream.computeInt64Size(1, this.versionCode_);
            }
            int dataSize = 0;
            for (int i = 0; i < this.packageNames_.size(); i++) {
                dataSize += CodedOutputStream.computeStringSizeNoTag((String) this.packageNames_.get(i));
            }
            size = ((size + dataSize) + (getPackageNamesList().size() * 1)) + this.unknownFields.getSerializedSize();
            this.memoizedSerializedSize = size;
            return size;
        }

        public static Blacklist parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static Blacklist parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static Blacklist parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static Blacklist parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static Blacklist parseFrom(InputStream input) throws IOException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static Blacklist parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static Blacklist parseDelimitedFrom(InputStream input) throws IOException {
            return (Blacklist) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static Blacklist parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Blacklist) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static Blacklist parseFrom(CodedInputStream input) throws IOException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static Blacklist parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Blacklist) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return (Builder) DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(Blacklist prototype) {
            return (Builder) ((Builder) DEFAULT_INSTANCE.toBuilder()).mergeFrom(prototype);
        }

        /* Access modifiers changed, original: protected|final */
        public final Object dynamicMethod(MethodToInvoke method, Object arg0, Object arg1) {
            switch (method) {
                case NEW_MUTABLE_INSTANCE:
                    return new Blacklist();
                case IS_INITIALIZED:
                    return DEFAULT_INSTANCE;
                case MAKE_IMMUTABLE:
                    this.packageNames_.makeImmutable();
                    return null;
                case NEW_BUILDER:
                    return new Builder();
                case VISIT:
                    Visitor visitor = (Visitor) arg0;
                    Blacklist other = (Blacklist) arg1;
                    this.versionCode_ = visitor.visitLong(hasVersionCode(), this.versionCode_, other.hasVersionCode(), other.versionCode_);
                    this.packageNames_ = visitor.visitList(this.packageNames_, other.packageNames_);
                    if (visitor == MergeFromVisitor.INSTANCE) {
                        this.bitField0_ |= other.bitField0_;
                    }
                    return this;
                case MERGE_FROM_STREAM:
                    CodedInputStream input = (CodedInputStream) arg0;
                    ExtensionRegistryLite extensionRegistry = (ExtensionRegistryLite) arg1;
                    boolean done = false;
                    while (!done) {
                        try {
                            int tag = input.readTag();
                            if (tag == 0) {
                                done = true;
                            } else if (tag == 8) {
                                this.bitField0_ |= 1;
                                this.versionCode_ = input.readInt64();
                            } else if (tag == 18) {
                                String s = input.readString();
                                if (!this.packageNames_.isModifiable()) {
                                    this.packageNames_ = GeneratedMessageLite.mutableCopy(this.packageNames_);
                                }
                                this.packageNames_.add(s);
                            } else if (!parseUnknownField(tag, input)) {
                                done = true;
                            }
                        } catch (InvalidProtocolBufferException e) {
                            throw new RuntimeException(e.setUnfinishedMessage(this));
                        } catch (IOException e2) {
                            throw new RuntimeException(new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this));
                        }
                    }
                    break;
                case GET_DEFAULT_INSTANCE:
                    break;
                case GET_PARSER:
                    if (PARSER == null) {
                        synchronized (Blacklist.class) {
                            if (PARSER == null) {
                                PARSER = new DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                            }
                        }
                    }
                    return PARSER;
                default:
                    throw new UnsupportedOperationException();
            }
            return DEFAULT_INSTANCE;
        }

        static {
            DEFAULT_INSTANCE.makeImmutable();
        }

        public static Blacklist getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<Blacklist> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }

    public interface BlacklistsOrBuilder extends MessageLiteOrBuilder {
        Blacklist getBlacklists(int i);

        int getBlacklistsCount();

        List<Blacklist> getBlacklistsList();
    }

    public static final class Blacklists extends GeneratedMessageLite<Blacklists, Builder> implements BlacklistsOrBuilder {
        public static final int BLACKLISTS_FIELD_NUMBER = 1;
        private static final Blacklists DEFAULT_INSTANCE = new Blacklists();
        private static volatile Parser<Blacklists> PARSER;
        private ProtobufList<Blacklist> blacklists_ = GeneratedMessageLite.emptyProtobufList();

        public static final class Builder extends com.android.framework.protobuf.GeneratedMessageLite.Builder<Blacklists, Builder> implements BlacklistsOrBuilder {
            private Builder() {
                super(Blacklists.DEFAULT_INSTANCE);
            }

            public List<Blacklist> getBlacklistsList() {
                return Collections.unmodifiableList(((Blacklists) this.instance).getBlacklistsList());
            }

            public int getBlacklistsCount() {
                return ((Blacklists) this.instance).getBlacklistsCount();
            }

            public Blacklist getBlacklists(int index) {
                return ((Blacklists) this.instance).getBlacklists(index);
            }

            public Builder setBlacklists(int index, Blacklist value) {
                copyOnWrite();
                ((Blacklists) this.instance).setBlacklists(index, value);
                return this;
            }

            public Builder setBlacklists(int index, Builder builderForValue) {
                copyOnWrite();
                ((Blacklists) this.instance).setBlacklists(index, builderForValue);
                return this;
            }

            public Builder addBlacklists(Blacklist value) {
                copyOnWrite();
                ((Blacklists) this.instance).addBlacklists(value);
                return this;
            }

            public Builder addBlacklists(int index, Blacklist value) {
                copyOnWrite();
                ((Blacklists) this.instance).addBlacklists(index, value);
                return this;
            }

            public Builder addBlacklists(Builder builderForValue) {
                copyOnWrite();
                ((Blacklists) this.instance).addBlacklists(builderForValue);
                return this;
            }

            public Builder addBlacklists(int index, Builder builderForValue) {
                copyOnWrite();
                ((Blacklists) this.instance).addBlacklists(index, builderForValue);
                return this;
            }

            public Builder addAllBlacklists(Iterable<? extends Blacklist> values) {
                copyOnWrite();
                ((Blacklists) this.instance).addAllBlacklists(values);
                return this;
            }

            public Builder clearBlacklists() {
                copyOnWrite();
                ((Blacklists) this.instance).clearBlacklists();
                return this;
            }

            public Builder removeBlacklists(int index) {
                copyOnWrite();
                ((Blacklists) this.instance).removeBlacklists(index);
                return this;
            }
        }

        private Blacklists() {
        }

        public List<Blacklist> getBlacklistsList() {
            return this.blacklists_;
        }

        public List<? extends BlacklistOrBuilder> getBlacklistsOrBuilderList() {
            return this.blacklists_;
        }

        public int getBlacklistsCount() {
            return this.blacklists_.size();
        }

        public Blacklist getBlacklists(int index) {
            return (Blacklist) this.blacklists_.get(index);
        }

        public BlacklistOrBuilder getBlacklistsOrBuilder(int index) {
            return (BlacklistOrBuilder) this.blacklists_.get(index);
        }

        private void ensureBlacklistsIsMutable() {
            if (!this.blacklists_.isModifiable()) {
                this.blacklists_ = GeneratedMessageLite.mutableCopy(this.blacklists_);
            }
        }

        private void setBlacklists(int index, Blacklist value) {
            if (value != null) {
                ensureBlacklistsIsMutable();
                this.blacklists_.set(index, value);
                return;
            }
            throw new NullPointerException();
        }

        private void setBlacklists(int index, Builder builderForValue) {
            ensureBlacklistsIsMutable();
            this.blacklists_.set(index, (Blacklist) builderForValue.build());
        }

        private void addBlacklists(Blacklist value) {
            if (value != null) {
                ensureBlacklistsIsMutable();
                this.blacklists_.add(value);
                return;
            }
            throw new NullPointerException();
        }

        private void addBlacklists(int index, Blacklist value) {
            if (value != null) {
                ensureBlacklistsIsMutable();
                this.blacklists_.add(index, value);
                return;
            }
            throw new NullPointerException();
        }

        private void addBlacklists(Builder builderForValue) {
            ensureBlacklistsIsMutable();
            this.blacklists_.add((Blacklist) builderForValue.build());
        }

        private void addBlacklists(int index, Builder builderForValue) {
            ensureBlacklistsIsMutable();
            this.blacklists_.add(index, (Blacklist) builderForValue.build());
        }

        private void addAllBlacklists(Iterable<? extends Blacklist> values) {
            ensureBlacklistsIsMutable();
            AbstractMessageLite.addAll(values, this.blacklists_);
        }

        private void clearBlacklists() {
            this.blacklists_ = GeneratedMessageLite.emptyProtobufList();
        }

        private void removeBlacklists(int index) {
            ensureBlacklistsIsMutable();
            this.blacklists_.remove(index);
        }

        public void writeTo(CodedOutputStream output) throws IOException {
            for (int i = 0; i < this.blacklists_.size(); i++) {
                output.writeMessage(1, (MessageLite) this.blacklists_.get(i));
            }
            this.unknownFields.writeTo(output);
        }

        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            size = 0;
            for (int i = 0; i < this.blacklists_.size(); i++) {
                size += CodedOutputStream.computeMessageSize(1, (MessageLite) this.blacklists_.get(i));
            }
            size += this.unknownFields.getSerializedSize();
            this.memoizedSerializedSize = size;
            return size;
        }

        public static Blacklists parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static Blacklists parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static Blacklists parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static Blacklists parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static Blacklists parseFrom(InputStream input) throws IOException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static Blacklists parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static Blacklists parseDelimitedFrom(InputStream input) throws IOException {
            return (Blacklists) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static Blacklists parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Blacklists) GeneratedMessageLite.parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static Blacklists parseFrom(CodedInputStream input) throws IOException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static Blacklists parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (Blacklists) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return (Builder) DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(Blacklists prototype) {
            return (Builder) ((Builder) DEFAULT_INSTANCE.toBuilder()).mergeFrom(prototype);
        }

        /* Access modifiers changed, original: protected|final */
        public final Object dynamicMethod(MethodToInvoke method, Object arg0, Object arg1) {
            switch (method) {
                case NEW_MUTABLE_INSTANCE:
                    return new Blacklists();
                case IS_INITIALIZED:
                    return DEFAULT_INSTANCE;
                case MAKE_IMMUTABLE:
                    this.blacklists_.makeImmutable();
                    return null;
                case NEW_BUILDER:
                    return new Builder();
                case VISIT:
                    this.blacklists_ = ((Visitor) arg0).visitList(this.blacklists_, ((Blacklists) arg1).blacklists_);
                    MergeFromVisitor mergeFromVisitor = MergeFromVisitor.INSTANCE;
                    return this;
                case MERGE_FROM_STREAM:
                    CodedInputStream input = (CodedInputStream) arg0;
                    ExtensionRegistryLite extensionRegistry = (ExtensionRegistryLite) arg1;
                    boolean done = false;
                    while (!done) {
                        try {
                            int tag = input.readTag();
                            if (tag == 0) {
                                done = true;
                            } else if (tag == 10) {
                                if (!this.blacklists_.isModifiable()) {
                                    this.blacklists_ = GeneratedMessageLite.mutableCopy(this.blacklists_);
                                }
                                this.blacklists_.add((Blacklist) input.readMessage(Blacklist.parser(), extensionRegistry));
                            } else if (!parseUnknownField(tag, input)) {
                                done = true;
                            }
                        } catch (InvalidProtocolBufferException e) {
                            throw new RuntimeException(e.setUnfinishedMessage(this));
                        } catch (IOException e2) {
                            throw new RuntimeException(new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this));
                        }
                    }
                    break;
                case GET_DEFAULT_INSTANCE:
                    break;
                case GET_PARSER:
                    if (PARSER == null) {
                        synchronized (Blacklists.class) {
                            if (PARSER == null) {
                                PARSER = new DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                            }
                        }
                    }
                    return PARSER;
                default:
                    throw new UnsupportedOperationException();
            }
            return DEFAULT_INSTANCE;
        }

        static {
            DEFAULT_INSTANCE.makeImmutable();
        }

        public static Blacklists getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<Blacklists> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }

    private GameDriverProto() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }
}
