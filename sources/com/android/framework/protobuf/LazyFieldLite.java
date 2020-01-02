package com.android.framework.protobuf;

import java.io.IOException;

public class LazyFieldLite {
    private static final ExtensionRegistryLite EMPTY_REGISTRY = ExtensionRegistryLite.getEmptyRegistry();
    private ByteString delayedBytes;
    private ExtensionRegistryLite extensionRegistry;
    private volatile ByteString memoizedBytes;
    protected volatile MessageLite value;

    public LazyFieldLite(ExtensionRegistryLite extensionRegistry, ByteString bytes) {
        checkArguments(extensionRegistry, bytes);
        this.extensionRegistry = extensionRegistry;
        this.delayedBytes = bytes;
    }

    public static LazyFieldLite fromValue(MessageLite value) {
        LazyFieldLite lf = new LazyFieldLite();
        lf.setValue(value);
        return lf;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LazyFieldLite)) {
            return false;
        }
        LazyFieldLite other = (LazyFieldLite) o;
        MessageLite value1 = this.value;
        MessageLite value2 = other.value;
        if (value1 == null && value2 == null) {
            return toByteString().equals(other.toByteString());
        }
        if (value1 != null && value2 != null) {
            return value1.equals(value2);
        }
        if (value1 != null) {
            return value1.equals(other.getValue(value1.getDefaultInstanceForType()));
        }
        return getValue(value2.getDefaultInstanceForType()).equals(value2);
    }

    public int hashCode() {
        return 1;
    }

    /* JADX WARNING: Missing block: B:7:0x0010, code skipped:
            if (r0 != com.android.framework.protobuf.ByteString.EMPTY) goto L_0x0013;
     */
    public boolean containsDefaultInstance() {
        /*
        r2 = this;
        r0 = r2.memoizedBytes;
        r1 = com.android.framework.protobuf.ByteString.EMPTY;
        if (r0 == r1) goto L_0x0015;
    L_0x0006:
        r0 = r2.value;
        if (r0 != 0) goto L_0x0013;
    L_0x000a:
        r0 = r2.delayedBytes;
        if (r0 == 0) goto L_0x0015;
    L_0x000e:
        r1 = com.android.framework.protobuf.ByteString.EMPTY;
        if (r0 != r1) goto L_0x0013;
    L_0x0012:
        goto L_0x0015;
    L_0x0013:
        r0 = 0;
        goto L_0x0016;
    L_0x0015:
        r0 = 1;
    L_0x0016:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.framework.protobuf.LazyFieldLite.containsDefaultInstance():boolean");
    }

    public void clear() {
        this.delayedBytes = null;
        this.value = null;
        this.memoizedBytes = null;
    }

    public void set(LazyFieldLite other) {
        this.delayedBytes = other.delayedBytes;
        this.value = other.value;
        this.memoizedBytes = other.memoizedBytes;
        ExtensionRegistryLite extensionRegistryLite = other.extensionRegistry;
        if (extensionRegistryLite != null) {
            this.extensionRegistry = extensionRegistryLite;
        }
    }

    public MessageLite getValue(MessageLite defaultInstance) {
        ensureInitialized(defaultInstance);
        return this.value;
    }

    public MessageLite setValue(MessageLite value) {
        MessageLite originalValue = this.value;
        this.delayedBytes = null;
        this.memoizedBytes = null;
        this.value = value;
        return originalValue;
    }

    public void merge(LazyFieldLite other) {
        if (!other.containsDefaultInstance()) {
            if (containsDefaultInstance()) {
                set(other);
                return;
            }
            if (this.extensionRegistry == null) {
                this.extensionRegistry = other.extensionRegistry;
            }
            ByteString byteString = this.delayedBytes;
            if (byteString != null) {
                ByteString byteString2 = other.delayedBytes;
                if (byteString2 != null) {
                    this.delayedBytes = byteString.concat(byteString2);
                    return;
                }
            }
            if (this.value == null && other.value != null) {
                setValue(mergeValueAndBytes(other.value, this.delayedBytes, this.extensionRegistry));
            } else if (this.value != null && other.value == null) {
                setValue(mergeValueAndBytes(this.value, other.delayedBytes, other.extensionRegistry));
            } else if (other.extensionRegistry != null) {
                setValue(mergeValueAndBytes(this.value, other.toByteString(), other.extensionRegistry));
            } else if (this.extensionRegistry != null) {
                setValue(mergeValueAndBytes(other.value, toByteString(), this.extensionRegistry));
            } else {
                setValue(mergeValueAndBytes(this.value, other.toByteString(), EMPTY_REGISTRY));
            }
        }
    }

    public void mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        if (containsDefaultInstance()) {
            setByteString(input.readBytes(), extensionRegistry);
            return;
        }
        if (this.extensionRegistry == null) {
            this.extensionRegistry = extensionRegistry;
        }
        ByteString byteString = this.delayedBytes;
        if (byteString != null) {
            setByteString(byteString.concat(input.readBytes()), this.extensionRegistry);
        } else {
            try {
                setValue(this.value.toBuilder().mergeFrom(input, extensionRegistry).build());
            } catch (InvalidProtocolBufferException e) {
            }
        }
    }

    private static MessageLite mergeValueAndBytes(MessageLite value, ByteString otherBytes, ExtensionRegistryLite extensionRegistry) {
        try {
            return value.toBuilder().mergeFrom(otherBytes, extensionRegistry).build();
        } catch (InvalidProtocolBufferException e) {
            return value;
        }
    }

    public void setByteString(ByteString bytes, ExtensionRegistryLite extensionRegistry) {
        checkArguments(extensionRegistry, bytes);
        this.delayedBytes = bytes;
        this.extensionRegistry = extensionRegistry;
        this.value = null;
        this.memoizedBytes = null;
    }

    public int getSerializedSize() {
        if (this.memoizedBytes != null) {
            return this.memoizedBytes.size();
        }
        ByteString byteString = this.delayedBytes;
        if (byteString != null) {
            return byteString.size();
        }
        if (this.value != null) {
            return this.value.getSerializedSize();
        }
        return 0;
    }

    public ByteString toByteString() {
        if (this.memoizedBytes != null) {
            return this.memoizedBytes;
        }
        ByteString byteString = this.delayedBytes;
        if (byteString != null) {
            return byteString;
        }
        synchronized (this) {
            if (this.memoizedBytes != null) {
                byteString = this.memoizedBytes;
                return byteString;
            }
            if (this.value == null) {
                this.memoizedBytes = ByteString.EMPTY;
            } else {
                this.memoizedBytes = this.value.toByteString();
            }
            byteString = this.memoizedBytes;
            return byteString;
        }
    }

    /* Access modifiers changed, original: protected */
    public void ensureInitialized(MessageLite defaultInstance) {
        if (this.value == null) {
            synchronized (this) {
                if (this.value != null) {
                    return;
                }
                try {
                    if (this.delayedBytes != null) {
                        this.value = (MessageLite) defaultInstance.getParserForType().parseFrom(this.delayedBytes, this.extensionRegistry);
                        this.memoizedBytes = this.delayedBytes;
                    } else {
                        this.value = defaultInstance;
                        this.memoizedBytes = ByteString.EMPTY;
                    }
                } catch (InvalidProtocolBufferException e) {
                    this.value = defaultInstance;
                    this.memoizedBytes = ByteString.EMPTY;
                }
            }
        } else {
            return;
        }
    }

    private static void checkArguments(ExtensionRegistryLite extensionRegistry, ByteString bytes) {
        if (extensionRegistry == null) {
            throw new NullPointerException("found null ExtensionRegistry");
        } else if (bytes == null) {
            throw new NullPointerException("found null ByteString");
        }
    }
}
