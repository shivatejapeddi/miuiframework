package com.android.framework.protobuf.nano;

import java.io.IOException;

public abstract class ExtendableMessageNano<M extends ExtendableMessageNano<M>> extends MessageNano {
    protected FieldArray unknownFieldData;

    /* Access modifiers changed, original: protected */
    public int computeSerializedSize() {
        int size = 0;
        if (this.unknownFieldData != null) {
            for (int i = 0; i < this.unknownFieldData.size(); i++) {
                size += this.unknownFieldData.dataAt(i).computeSerializedSize();
            }
        }
        return size;
    }

    public void writeTo(CodedOutputByteBufferNano output) throws IOException {
        if (this.unknownFieldData != null) {
            for (int i = 0; i < this.unknownFieldData.size(); i++) {
                this.unknownFieldData.dataAt(i).writeTo(output);
            }
        }
    }

    public final boolean hasExtension(Extension<M, ?> extension) {
        FieldData field = this.unknownFieldData;
        boolean z = false;
        if (field == null) {
            return false;
        }
        if (field.get(WireFormatNano.getTagFieldNumber(extension.tag)) != null) {
            z = true;
        }
        return z;
    }

    public final <T> T getExtension(Extension<M, T> extension) {
        FieldData field = this.unknownFieldData;
        T t = null;
        if (field == null) {
            return null;
        }
        field = field.get(WireFormatNano.getTagFieldNumber(extension.tag));
        if (field != null) {
            t = field.getValue(extension);
        }
        return t;
    }

    public final <T> M setExtension(Extension<M, T> extension, T value) {
        int fieldNumber = WireFormatNano.getTagFieldNumber(extension.tag);
        if (value == null) {
            FieldArray fieldArray = this.unknownFieldData;
            if (fieldArray != null) {
                fieldArray.remove(fieldNumber);
                if (this.unknownFieldData.isEmpty()) {
                    this.unknownFieldData = null;
                }
            }
        } else {
            FieldData field = null;
            FieldArray fieldArray2 = this.unknownFieldData;
            if (fieldArray2 == null) {
                this.unknownFieldData = new FieldArray();
            } else {
                field = fieldArray2.get(fieldNumber);
            }
            if (field == null) {
                this.unknownFieldData.put(fieldNumber, new FieldData(extension, value));
            } else {
                field.setValue(extension, value);
            }
        }
        return this;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean storeUnknownField(CodedInputByteBufferNano input, int tag) throws IOException {
        int startPos = input.getPosition();
        if (!input.skipField(tag)) {
            return false;
        }
        int fieldNumber = WireFormatNano.getTagFieldNumber(tag);
        UnknownFieldData unknownField = new UnknownFieldData(tag, input.getData(startPos, input.getPosition() - startPos));
        FieldData field = null;
        FieldArray fieldArray = this.unknownFieldData;
        if (fieldArray == null) {
            this.unknownFieldData = new FieldArray();
        } else {
            field = fieldArray.get(fieldNumber);
        }
        if (field == null) {
            field = new FieldData();
            this.unknownFieldData.put(fieldNumber, field);
        }
        field.addUnknownField(unknownField);
        return true;
    }

    public M clone() throws CloneNotSupportedException {
        ExtendableMessageNano cloned = (ExtendableMessageNano) super.clone();
        InternalNano.cloneUnknownFieldData(this, cloned);
        return cloned;
    }
}
