package com.android.framework.protobuf;

import com.android.framework.protobuf.WireFormat.FieldType;

public abstract class ExtensionLite<ContainingType extends MessageLite, Type> {
    public abstract Type getDefaultValue();

    public abstract FieldType getLiteType();

    public abstract MessageLite getMessageDefaultInstance();

    public abstract int getNumber();

    public abstract boolean isRepeated();

    /* Access modifiers changed, original: 0000 */
    public boolean isLite() {
        return true;
    }
}
