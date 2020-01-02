package com.android.server.connectivity.metrics.nano;

import com.android.framework.protobuf.nano.CodedInputByteBufferNano;
import com.android.framework.protobuf.nano.CodedOutputByteBufferNano;
import com.android.framework.protobuf.nano.InternalNano;
import com.android.framework.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.framework.protobuf.nano.MessageNano;
import com.android.framework.protobuf.nano.WireFormatNano;
import java.io.IOException;

public interface IpConnectivityLogClass {
    public static final int BLUETOOTH = 1;
    public static final int CELLULAR = 2;
    public static final int ETHERNET = 3;
    public static final int LOWPAN = 9;
    public static final int MULTIPLE = 6;
    public static final int NONE = 5;
    public static final int UNKNOWN = 0;
    public static final int WIFI = 4;
    public static final int WIFI_NAN = 8;
    public static final int WIFI_P2P = 7;

    public static final class ApfProgramEvent extends MessageNano {
        private static volatile ApfProgramEvent[] _emptyArray;
        public int currentRas;
        public boolean dropMulticast;
        public long effectiveLifetime;
        public int filteredRas;
        public boolean hasIpv4Addr;
        public long lifetime;
        public int programLength;

        public static ApfProgramEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new ApfProgramEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public ApfProgramEvent() {
            clear();
        }

        public ApfProgramEvent clear() {
            this.lifetime = 0;
            this.effectiveLifetime = 0;
            this.filteredRas = 0;
            this.currentRas = 0;
            this.programLength = 0;
            this.dropMulticast = false;
            this.hasIpv4Addr = false;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            long j = this.lifetime;
            if (j != 0) {
                output.writeInt64(1, j);
            }
            int i = this.filteredRas;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.currentRas;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.programLength;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            boolean z = this.dropMulticast;
            if (z) {
                output.writeBool(5, z);
            }
            z = this.hasIpv4Addr;
            if (z) {
                output.writeBool(6, z);
            }
            j = this.effectiveLifetime;
            if (j != 0) {
                output.writeInt64(7, j);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            long j = this.lifetime;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, j);
            }
            int i = this.filteredRas;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.currentRas;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.programLength;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            boolean z = this.dropMulticast;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(5, z);
            }
            z = this.hasIpv4Addr;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(6, z);
            }
            j = this.effectiveLifetime;
            if (j != 0) {
                return size + CodedOutputByteBufferNano.computeInt64Size(7, j);
            }
            return size;
        }

        public ApfProgramEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.lifetime = input.readInt64();
                } else if (tag == 16) {
                    this.filteredRas = input.readInt32();
                } else if (tag == 24) {
                    this.currentRas = input.readInt32();
                } else if (tag == 32) {
                    this.programLength = input.readInt32();
                } else if (tag == 40) {
                    this.dropMulticast = input.readBool();
                } else if (tag == 48) {
                    this.hasIpv4Addr = input.readBool();
                } else if (tag == 56) {
                    this.effectiveLifetime = input.readInt64();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static ApfProgramEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (ApfProgramEvent) MessageNano.mergeFrom(new ApfProgramEvent(), data);
        }

        public static ApfProgramEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new ApfProgramEvent().mergeFrom(input);
        }
    }

    public static final class ApfStatistics extends MessageNano {
        private static volatile ApfStatistics[] _emptyArray;
        public int droppedRas;
        public long durationMs;
        public Pair[] hardwareCounters;
        public int matchingRas;
        public int maxProgramSize;
        public int parseErrors;
        public int programUpdates;
        public int programUpdatesAll;
        public int programUpdatesAllowingMulticast;
        public int receivedRas;
        public int totalPacketDropped;
        public int totalPacketProcessed;
        public int zeroLifetimeRas;

        public static ApfStatistics[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new ApfStatistics[0];
                    }
                }
            }
            return _emptyArray;
        }

        public ApfStatistics() {
            clear();
        }

        public ApfStatistics clear() {
            this.durationMs = 0;
            this.receivedRas = 0;
            this.matchingRas = 0;
            this.droppedRas = 0;
            this.zeroLifetimeRas = 0;
            this.parseErrors = 0;
            this.programUpdates = 0;
            this.maxProgramSize = 0;
            this.programUpdatesAll = 0;
            this.programUpdatesAllowingMulticast = 0;
            this.totalPacketProcessed = 0;
            this.totalPacketDropped = 0;
            this.hardwareCounters = Pair.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            long j = this.durationMs;
            if (j != 0) {
                output.writeInt64(1, j);
            }
            int i = this.receivedRas;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.matchingRas;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.droppedRas;
            if (i != 0) {
                output.writeInt32(5, i);
            }
            i = this.zeroLifetimeRas;
            if (i != 0) {
                output.writeInt32(6, i);
            }
            i = this.parseErrors;
            if (i != 0) {
                output.writeInt32(7, i);
            }
            i = this.programUpdates;
            if (i != 0) {
                output.writeInt32(8, i);
            }
            i = this.maxProgramSize;
            if (i != 0) {
                output.writeInt32(9, i);
            }
            i = this.programUpdatesAll;
            if (i != 0) {
                output.writeInt32(10, i);
            }
            i = this.programUpdatesAllowingMulticast;
            if (i != 0) {
                output.writeInt32(11, i);
            }
            i = this.totalPacketProcessed;
            if (i != 0) {
                output.writeInt32(12, i);
            }
            i = this.totalPacketDropped;
            if (i != 0) {
                output.writeInt32(13, i);
            }
            Pair[] pairArr = this.hardwareCounters;
            if (pairArr != null && pairArr.length > 0) {
                i = 0;
                while (true) {
                    Pair element = this.hardwareCounters;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        output.writeMessage(14, element);
                    }
                    i++;
                }
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            long j = this.durationMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, j);
            }
            int i = this.receivedRas;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.matchingRas;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.droppedRas;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(5, i);
            }
            i = this.zeroLifetimeRas;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(6, i);
            }
            i = this.parseErrors;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(7, i);
            }
            i = this.programUpdates;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(8, i);
            }
            i = this.maxProgramSize;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(9, i);
            }
            i = this.programUpdatesAll;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(10, i);
            }
            i = this.programUpdatesAllowingMulticast;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(11, i);
            }
            i = this.totalPacketProcessed;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(12, i);
            }
            i = this.totalPacketDropped;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(13, i);
            }
            Pair[] pairArr = this.hardwareCounters;
            if (pairArr != null && pairArr.length > 0) {
                i = 0;
                while (true) {
                    Pair element = this.hardwareCounters;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(14, element);
                    }
                    i++;
                }
            }
            return size;
        }

        public ApfStatistics mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        this.durationMs = input.readInt64();
                        break;
                    case 16:
                        this.receivedRas = input.readInt32();
                        break;
                    case 24:
                        this.matchingRas = input.readInt32();
                        break;
                    case 40:
                        this.droppedRas = input.readInt32();
                        break;
                    case 48:
                        this.zeroLifetimeRas = input.readInt32();
                        break;
                    case 56:
                        this.parseErrors = input.readInt32();
                        break;
                    case 64:
                        this.programUpdates = input.readInt32();
                        break;
                    case 72:
                        this.maxProgramSize = input.readInt32();
                        break;
                    case 80:
                        this.programUpdatesAll = input.readInt32();
                        break;
                    case 88:
                        this.programUpdatesAllowingMulticast = input.readInt32();
                        break;
                    case 96:
                        this.totalPacketProcessed = input.readInt32();
                        break;
                    case 104:
                        this.totalPacketDropped = input.readInt32();
                        break;
                    case 114:
                        int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 114);
                        Pair[] pairArr = this.hardwareCounters;
                        int i = pairArr == null ? 0 : pairArr.length;
                        Pair[] newArray = new Pair[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.hardwareCounters, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new Pair();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new Pair();
                        input.readMessage(newArray[i]);
                        this.hardwareCounters = newArray;
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        }
                        return this;
                }
            }
        }

        public static ApfStatistics parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (ApfStatistics) MessageNano.mergeFrom(new ApfStatistics(), data);
        }

        public static ApfStatistics parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new ApfStatistics().mergeFrom(input);
        }
    }

    public static final class ConnectStatistics extends MessageNano {
        private static volatile ConnectStatistics[] _emptyArray;
        public int connectBlockingCount;
        public int connectCount;
        public Pair[] errnosCounters;
        public int ipv6AddrCount;
        public int[] latenciesMs;
        public int[] nonBlockingLatenciesMs;

        public static ConnectStatistics[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new ConnectStatistics[0];
                    }
                }
            }
            return _emptyArray;
        }

        public ConnectStatistics() {
            clear();
        }

        public ConnectStatistics clear() {
            this.connectCount = 0;
            this.connectBlockingCount = 0;
            this.ipv6AddrCount = 0;
            this.latenciesMs = WireFormatNano.EMPTY_INT_ARRAY;
            this.nonBlockingLatenciesMs = WireFormatNano.EMPTY_INT_ARRAY;
            this.errnosCounters = Pair.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int[] iArr;
            int i = this.connectCount;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.ipv6AddrCount;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            int[] iArr2 = this.latenciesMs;
            if (iArr2 != null && iArr2.length > 0) {
                i = 0;
                while (true) {
                    iArr = this.latenciesMs;
                    if (i >= iArr.length) {
                        break;
                    }
                    output.writeInt32(3, iArr[i]);
                    i++;
                }
            }
            Pair[] pairArr = this.errnosCounters;
            if (pairArr != null && pairArr.length > 0) {
                i = 0;
                while (true) {
                    Pair element = this.errnosCounters;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        output.writeMessage(4, element);
                    }
                    i++;
                }
            }
            i = this.connectBlockingCount;
            if (i != 0) {
                output.writeInt32(5, i);
            }
            iArr2 = this.nonBlockingLatenciesMs;
            if (iArr2 != null && iArr2.length > 0) {
                i = 0;
                while (true) {
                    iArr = this.nonBlockingLatenciesMs;
                    if (i >= iArr.length) {
                        break;
                    }
                    output.writeInt32(6, iArr[i]);
                    i++;
                }
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int i;
            int[] iArr;
            int size = super.computeSerializedSize();
            int i2 = this.connectCount;
            if (i2 != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i2);
            }
            i2 = this.ipv6AddrCount;
            if (i2 != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i2);
            }
            int[] iArr2 = this.latenciesMs;
            if (iArr2 != null && iArr2.length > 0) {
                i2 = 0;
                i = 0;
                while (true) {
                    iArr = this.latenciesMs;
                    if (i >= iArr.length) {
                        break;
                    }
                    i2 += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr[i]);
                    i++;
                }
                size = (size + i2) + (iArr.length * 1);
            }
            Pair[] pairArr = this.errnosCounters;
            if (pairArr != null && pairArr.length > 0) {
                i2 = 0;
                while (true) {
                    Pair element = this.errnosCounters;
                    if (i2 >= element.length) {
                        break;
                    }
                    element = element[i2];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(4, element);
                    }
                    i2++;
                }
            }
            i2 = this.connectBlockingCount;
            if (i2 != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(5, i2);
            }
            iArr2 = this.nonBlockingLatenciesMs;
            if (iArr2 == null || iArr2.length <= 0) {
                return size;
            }
            i2 = 0;
            i = 0;
            while (true) {
                iArr = this.nonBlockingLatenciesMs;
                if (i >= iArr.length) {
                    return (size + i2) + (iArr.length * 1);
                }
                i2 += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr[i]);
                i++;
            }
        }

        public ConnectStatistics mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                int arrayLength;
                int[] iArr;
                int i;
                int[] newArray;
                int arrayLength2;
                int startPos;
                int[] iArr2;
                int i2;
                int[] newArray2;
                if (tag == 8) {
                    this.connectCount = input.readInt32();
                } else if (tag == 16) {
                    this.ipv6AddrCount = input.readInt32();
                } else if (tag == 24) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 24);
                    iArr = this.latenciesMs;
                    i = iArr == null ? 0 : iArr.length;
                    newArray = new int[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.latenciesMs, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = input.readInt32();
                        input.readTag();
                        i++;
                    }
                    newArray[i] = input.readInt32();
                    this.latenciesMs = newArray;
                } else if (tag == 26) {
                    i = input.pushLimit(input.readRawVarint32());
                    arrayLength2 = 0;
                    startPos = input.getPosition();
                    while (input.getBytesUntilLimit() > 0) {
                        input.readInt32();
                        arrayLength2++;
                    }
                    input.rewindToPosition(startPos);
                    iArr2 = this.latenciesMs;
                    i2 = iArr2 == null ? 0 : iArr2.length;
                    newArray2 = new int[(i2 + arrayLength2)];
                    if (i2 != 0) {
                        System.arraycopy(this.latenciesMs, 0, newArray2, 0, i2);
                    }
                    while (i2 < newArray2.length) {
                        newArray2[i2] = input.readInt32();
                        i2++;
                    }
                    this.latenciesMs = newArray2;
                    input.popLimit(i);
                } else if (tag == 34) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                    Pair[] pairArr = this.errnosCounters;
                    i = pairArr == null ? 0 : pairArr.length;
                    Pair[] newArray3 = new Pair[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.errnosCounters, 0, newArray3, 0, i);
                    }
                    while (i < newArray3.length - 1) {
                        newArray3[i] = new Pair();
                        input.readMessage(newArray3[i]);
                        input.readTag();
                        i++;
                    }
                    newArray3[i] = new Pair();
                    input.readMessage(newArray3[i]);
                    this.errnosCounters = newArray3;
                } else if (tag == 40) {
                    this.connectBlockingCount = input.readInt32();
                } else if (tag == 48) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 48);
                    iArr = this.nonBlockingLatenciesMs;
                    i = iArr == null ? 0 : iArr.length;
                    newArray = new int[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.nonBlockingLatenciesMs, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = input.readInt32();
                        input.readTag();
                        i++;
                    }
                    newArray[i] = input.readInt32();
                    this.nonBlockingLatenciesMs = newArray;
                } else if (tag == 50) {
                    i = input.pushLimit(input.readRawVarint32());
                    arrayLength2 = 0;
                    startPos = input.getPosition();
                    while (input.getBytesUntilLimit() > 0) {
                        input.readInt32();
                        arrayLength2++;
                    }
                    input.rewindToPosition(startPos);
                    iArr2 = this.nonBlockingLatenciesMs;
                    i2 = iArr2 == null ? 0 : iArr2.length;
                    newArray2 = new int[(i2 + arrayLength2)];
                    if (i2 != 0) {
                        System.arraycopy(this.nonBlockingLatenciesMs, 0, newArray2, 0, i2);
                    }
                    while (i2 < newArray2.length) {
                        newArray2[i2] = input.readInt32();
                        i2++;
                    }
                    this.nonBlockingLatenciesMs = newArray2;
                    input.popLimit(i);
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static ConnectStatistics parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (ConnectStatistics) MessageNano.mergeFrom(new ConnectStatistics(), data);
        }

        public static ConnectStatistics parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new ConnectStatistics().mergeFrom(input);
        }
    }

    public static final class DHCPEvent extends MessageNano {
        public static final int ERROR_CODE_FIELD_NUMBER = 3;
        public static final int STATE_TRANSITION_FIELD_NUMBER = 2;
        private static volatile DHCPEvent[] _emptyArray;
        public int durationMs;
        public String ifName;
        private int valueCase_ = 0;
        private Object value_;

        public int getValueCase() {
            return this.valueCase_;
        }

        public DHCPEvent clearValue() {
            this.valueCase_ = 0;
            this.value_ = null;
            return this;
        }

        public static DHCPEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new DHCPEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public boolean hasStateTransition() {
            return this.valueCase_ == 2;
        }

        public String getStateTransition() {
            if (this.valueCase_ == 2) {
                return (String) this.value_;
            }
            return "";
        }

        public DHCPEvent setStateTransition(String value) {
            this.valueCase_ = 2;
            this.value_ = value;
            return this;
        }

        public boolean hasErrorCode() {
            return this.valueCase_ == 3;
        }

        public int getErrorCode() {
            if (this.valueCase_ == 3) {
                return ((Integer) this.value_).intValue();
            }
            return 0;
        }

        public DHCPEvent setErrorCode(int value) {
            this.valueCase_ = 3;
            this.value_ = Integer.valueOf(value);
            return this;
        }

        public DHCPEvent() {
            clear();
        }

        public DHCPEvent clear() {
            this.ifName = "";
            this.durationMs = 0;
            clearValue();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (!this.ifName.equals("")) {
                output.writeString(1, this.ifName);
            }
            if (this.valueCase_ == 2) {
                output.writeString(2, (String) this.value_);
            }
            if (this.valueCase_ == 3) {
                output.writeInt32(3, ((Integer) this.value_).intValue());
            }
            int i = this.durationMs;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (!this.ifName.equals("")) {
                size += CodedOutputByteBufferNano.computeStringSize(1, this.ifName);
            }
            if (this.valueCase_ == 2) {
                size += CodedOutputByteBufferNano.computeStringSize(2, (String) this.value_);
            }
            if (this.valueCase_ == 3) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, ((Integer) this.value_).intValue());
            }
            int i = this.durationMs;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            return size;
        }

        public DHCPEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.ifName = input.readString();
                } else if (tag == 18) {
                    this.value_ = input.readString();
                    this.valueCase_ = 2;
                } else if (tag == 24) {
                    this.value_ = Integer.valueOf(input.readInt32());
                    this.valueCase_ = 3;
                } else if (tag == 32) {
                    this.durationMs = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static DHCPEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (DHCPEvent) MessageNano.mergeFrom(new DHCPEvent(), data);
        }

        public static DHCPEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new DHCPEvent().mergeFrom(input);
        }
    }

    public static final class DNSLatencies extends MessageNano {
        private static volatile DNSLatencies[] _emptyArray;
        public int aCount;
        public int aaaaCount;
        public int[] latenciesMs;
        public int queryCount;
        public int returnCode;
        public int type;

        public static DNSLatencies[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new DNSLatencies[0];
                    }
                }
            }
            return _emptyArray;
        }

        public DNSLatencies() {
            clear();
        }

        public DNSLatencies clear() {
            this.type = 0;
            this.returnCode = 0;
            this.queryCount = 0;
            this.aCount = 0;
            this.aaaaCount = 0;
            this.latenciesMs = WireFormatNano.EMPTY_INT_ARRAY;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.type;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.returnCode;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.queryCount;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.aCount;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            i = this.aaaaCount;
            if (i != 0) {
                output.writeInt32(5, i);
            }
            int[] iArr = this.latenciesMs;
            if (iArr != null && iArr.length > 0) {
                i = 0;
                while (true) {
                    int[] iArr2 = this.latenciesMs;
                    if (i >= iArr2.length) {
                        break;
                    }
                    output.writeInt32(6, iArr2[i]);
                    i++;
                }
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.type;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.returnCode;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.queryCount;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.aCount;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            i = this.aaaaCount;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(5, i);
            }
            int[] iArr = this.latenciesMs;
            if (iArr == null || iArr.length <= 0) {
                return size;
            }
            i = 0;
            int i2 = 0;
            while (true) {
                int[] iArr2 = this.latenciesMs;
                if (i2 >= iArr2.length) {
                    return (size + i) + (iArr2.length * 1);
                }
                i += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr2[i2]);
                i2++;
            }
        }

        public DNSLatencies mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                int i;
                if (tag == 8) {
                    this.type = input.readInt32();
                } else if (tag == 16) {
                    this.returnCode = input.readInt32();
                } else if (tag == 24) {
                    this.queryCount = input.readInt32();
                } else if (tag == 32) {
                    this.aCount = input.readInt32();
                } else if (tag == 40) {
                    this.aaaaCount = input.readInt32();
                } else if (tag == 48) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 48);
                    int[] iArr = this.latenciesMs;
                    i = iArr == null ? 0 : iArr.length;
                    int[] newArray = new int[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.latenciesMs, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = input.readInt32();
                        input.readTag();
                        i++;
                    }
                    newArray[i] = input.readInt32();
                    this.latenciesMs = newArray;
                } else if (tag == 50) {
                    i = input.pushLimit(input.readRawVarint32());
                    int arrayLength2 = 0;
                    int startPos = input.getPosition();
                    while (input.getBytesUntilLimit() > 0) {
                        input.readInt32();
                        arrayLength2++;
                    }
                    input.rewindToPosition(startPos);
                    int[] iArr2 = this.latenciesMs;
                    int i2 = iArr2 == null ? 0 : iArr2.length;
                    int[] newArray2 = new int[(i2 + arrayLength2)];
                    if (i2 != 0) {
                        System.arraycopy(this.latenciesMs, 0, newArray2, 0, i2);
                    }
                    while (i2 < newArray2.length) {
                        newArray2[i2] = input.readInt32();
                        i2++;
                    }
                    this.latenciesMs = newArray2;
                    input.popLimit(i);
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static DNSLatencies parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (DNSLatencies) MessageNano.mergeFrom(new DNSLatencies(), data);
        }

        public static DNSLatencies parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new DNSLatencies().mergeFrom(input);
        }
    }

    public static final class DNSLookupBatch extends MessageNano {
        private static volatile DNSLookupBatch[] _emptyArray;
        public int[] eventTypes;
        public long getaddrinfoErrorCount;
        public Pair[] getaddrinfoErrors;
        public long getaddrinfoQueryCount;
        public long gethostbynameErrorCount;
        public Pair[] gethostbynameErrors;
        public long gethostbynameQueryCount;
        public int[] latenciesMs;
        public NetworkId networkId;
        public int[] returnCodes;

        public static DNSLookupBatch[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new DNSLookupBatch[0];
                    }
                }
            }
            return _emptyArray;
        }

        public DNSLookupBatch() {
            clear();
        }

        public DNSLookupBatch clear() {
            this.latenciesMs = WireFormatNano.EMPTY_INT_ARRAY;
            this.getaddrinfoQueryCount = 0;
            this.gethostbynameQueryCount = 0;
            this.getaddrinfoErrorCount = 0;
            this.gethostbynameErrorCount = 0;
            this.getaddrinfoErrors = Pair.emptyArray();
            this.gethostbynameErrors = Pair.emptyArray();
            this.networkId = null;
            this.eventTypes = WireFormatNano.EMPTY_INT_ARRAY;
            this.returnCodes = WireFormatNano.EMPTY_INT_ARRAY;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i;
            int[] iArr;
            Pair element;
            NetworkId networkId = this.networkId;
            if (networkId != null) {
                output.writeMessage(1, networkId);
            }
            int[] iArr2 = this.eventTypes;
            if (iArr2 != null && iArr2.length > 0) {
                i = 0;
                while (true) {
                    iArr = this.eventTypes;
                    if (i >= iArr.length) {
                        break;
                    }
                    output.writeInt32(2, iArr[i]);
                    i++;
                }
            }
            iArr2 = this.returnCodes;
            if (iArr2 != null && iArr2.length > 0) {
                i = 0;
                while (true) {
                    iArr = this.returnCodes;
                    if (i >= iArr.length) {
                        break;
                    }
                    output.writeInt32(3, iArr[i]);
                    i++;
                }
            }
            iArr2 = this.latenciesMs;
            if (iArr2 != null && iArr2.length > 0) {
                i = 0;
                while (true) {
                    iArr = this.latenciesMs;
                    if (i >= iArr.length) {
                        break;
                    }
                    output.writeInt32(4, iArr[i]);
                    i++;
                }
            }
            long j = this.getaddrinfoQueryCount;
            if (j != 0) {
                output.writeInt64(5, j);
            }
            j = this.gethostbynameQueryCount;
            if (j != 0) {
                output.writeInt64(6, j);
            }
            j = this.getaddrinfoErrorCount;
            if (j != 0) {
                output.writeInt64(7, j);
            }
            j = this.gethostbynameErrorCount;
            if (j != 0) {
                output.writeInt64(8, j);
            }
            Pair[] pairArr = this.getaddrinfoErrors;
            if (pairArr != null && pairArr.length > 0) {
                i = 0;
                while (true) {
                    element = this.getaddrinfoErrors;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        output.writeMessage(9, element);
                    }
                    i++;
                }
            }
            pairArr = this.gethostbynameErrors;
            if (pairArr != null && pairArr.length > 0) {
                i = 0;
                while (true) {
                    Pair[] pairArr2 = this.gethostbynameErrors;
                    if (i >= pairArr2.length) {
                        break;
                    }
                    element = pairArr2[i];
                    if (element != null) {
                        output.writeMessage(10, element);
                    }
                    i++;
                }
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int dataSize;
            int i;
            int[] iArr;
            Pair element;
            int size = super.computeSerializedSize();
            NetworkId networkId = this.networkId;
            if (networkId != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(1, networkId);
            }
            int[] iArr2 = this.eventTypes;
            if (iArr2 != null && iArr2.length > 0) {
                dataSize = 0;
                i = 0;
                while (true) {
                    iArr = this.eventTypes;
                    if (i >= iArr.length) {
                        break;
                    }
                    dataSize += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr[i]);
                    i++;
                }
                size = (size + dataSize) + (iArr.length * 1);
            }
            iArr2 = this.returnCodes;
            if (iArr2 != null && iArr2.length > 0) {
                dataSize = 0;
                i = 0;
                while (true) {
                    iArr = this.returnCodes;
                    if (i >= iArr.length) {
                        break;
                    }
                    dataSize += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr[i]);
                    i++;
                }
                size = (size + dataSize) + (iArr.length * 1);
            }
            iArr2 = this.latenciesMs;
            if (iArr2 != null && iArr2.length > 0) {
                dataSize = 0;
                i = 0;
                while (true) {
                    iArr = this.latenciesMs;
                    if (i >= iArr.length) {
                        break;
                    }
                    dataSize += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr[i]);
                    i++;
                }
                size = (size + dataSize) + (iArr.length * 1);
            }
            long j = this.getaddrinfoQueryCount;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(5, j);
            }
            j = this.gethostbynameQueryCount;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(6, j);
            }
            j = this.getaddrinfoErrorCount;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(7, j);
            }
            j = this.gethostbynameErrorCount;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(8, j);
            }
            Pair[] pairArr = this.getaddrinfoErrors;
            if (pairArr != null && pairArr.length > 0) {
                dataSize = 0;
                while (true) {
                    element = this.getaddrinfoErrors;
                    if (dataSize >= element.length) {
                        break;
                    }
                    element = element[dataSize];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(9, element);
                    }
                    dataSize++;
                }
            }
            pairArr = this.gethostbynameErrors;
            if (pairArr != null && pairArr.length > 0) {
                dataSize = 0;
                while (true) {
                    Pair[] pairArr2 = this.gethostbynameErrors;
                    if (dataSize >= pairArr2.length) {
                        break;
                    }
                    element = pairArr2[dataSize];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(10, element);
                    }
                    dataSize++;
                }
            }
            return size;
        }

        public DNSLookupBatch mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                int arrayLength;
                int[] iArr;
                int i;
                int[] newArray;
                int arrayLength2;
                int startPos;
                int[] iArr2;
                int i2;
                int[] newArray2;
                Pair[] pairArr;
                Pair[] newArray3;
                switch (tag) {
                    case 0:
                        return this;
                    case 10:
                        if (this.networkId == null) {
                            this.networkId = new NetworkId();
                        }
                        input.readMessage(this.networkId);
                        break;
                    case 16:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 16);
                        iArr = this.eventTypes;
                        i = iArr == null ? 0 : iArr.length;
                        newArray = new int[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.eventTypes, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = input.readInt32();
                            input.readTag();
                            i++;
                        }
                        newArray[i] = input.readInt32();
                        this.eventTypes = newArray;
                        break;
                    case 18:
                        i = input.pushLimit(input.readRawVarint32());
                        arrayLength2 = 0;
                        startPos = input.getPosition();
                        while (input.getBytesUntilLimit() > 0) {
                            input.readInt32();
                            arrayLength2++;
                        }
                        input.rewindToPosition(startPos);
                        iArr2 = this.eventTypes;
                        i2 = iArr2 == null ? 0 : iArr2.length;
                        newArray2 = new int[(i2 + arrayLength2)];
                        if (i2 != 0) {
                            System.arraycopy(this.eventTypes, 0, newArray2, 0, i2);
                        }
                        while (i2 < newArray2.length) {
                            newArray2[i2] = input.readInt32();
                            i2++;
                        }
                        this.eventTypes = newArray2;
                        input.popLimit(i);
                        break;
                    case 24:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 24);
                        iArr = this.returnCodes;
                        i = iArr == null ? 0 : iArr.length;
                        newArray = new int[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.returnCodes, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = input.readInt32();
                            input.readTag();
                            i++;
                        }
                        newArray[i] = input.readInt32();
                        this.returnCodes = newArray;
                        break;
                    case 26:
                        i = input.pushLimit(input.readRawVarint32());
                        arrayLength2 = 0;
                        startPos = input.getPosition();
                        while (input.getBytesUntilLimit() > 0) {
                            input.readInt32();
                            arrayLength2++;
                        }
                        input.rewindToPosition(startPos);
                        iArr2 = this.returnCodes;
                        i2 = iArr2 == null ? 0 : iArr2.length;
                        newArray2 = new int[(i2 + arrayLength2)];
                        if (i2 != 0) {
                            System.arraycopy(this.returnCodes, 0, newArray2, 0, i2);
                        }
                        while (i2 < newArray2.length) {
                            newArray2[i2] = input.readInt32();
                            i2++;
                        }
                        this.returnCodes = newArray2;
                        input.popLimit(i);
                        break;
                    case 32:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 32);
                        iArr = this.latenciesMs;
                        i = iArr == null ? 0 : iArr.length;
                        newArray = new int[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.latenciesMs, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = input.readInt32();
                            input.readTag();
                            i++;
                        }
                        newArray[i] = input.readInt32();
                        this.latenciesMs = newArray;
                        break;
                    case 34:
                        i = input.pushLimit(input.readRawVarint32());
                        arrayLength2 = 0;
                        startPos = input.getPosition();
                        while (input.getBytesUntilLimit() > 0) {
                            input.readInt32();
                            arrayLength2++;
                        }
                        input.rewindToPosition(startPos);
                        iArr2 = this.latenciesMs;
                        i2 = iArr2 == null ? 0 : iArr2.length;
                        newArray2 = new int[(i2 + arrayLength2)];
                        if (i2 != 0) {
                            System.arraycopy(this.latenciesMs, 0, newArray2, 0, i2);
                        }
                        while (i2 < newArray2.length) {
                            newArray2[i2] = input.readInt32();
                            i2++;
                        }
                        this.latenciesMs = newArray2;
                        input.popLimit(i);
                        break;
                    case 40:
                        this.getaddrinfoQueryCount = input.readInt64();
                        break;
                    case 48:
                        this.gethostbynameQueryCount = input.readInt64();
                        break;
                    case 56:
                        this.getaddrinfoErrorCount = input.readInt64();
                        break;
                    case 64:
                        this.gethostbynameErrorCount = input.readInt64();
                        break;
                    case 74:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 74);
                        pairArr = this.getaddrinfoErrors;
                        i = pairArr == null ? 0 : pairArr.length;
                        newArray3 = new Pair[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.getaddrinfoErrors, 0, newArray3, 0, i);
                        }
                        while (i < newArray3.length - 1) {
                            newArray3[i] = new Pair();
                            input.readMessage(newArray3[i]);
                            input.readTag();
                            i++;
                        }
                        newArray3[i] = new Pair();
                        input.readMessage(newArray3[i]);
                        this.getaddrinfoErrors = newArray3;
                        break;
                    case 82:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 82);
                        pairArr = this.gethostbynameErrors;
                        i = pairArr == null ? 0 : pairArr.length;
                        newArray3 = new Pair[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.gethostbynameErrors, 0, newArray3, 0, i);
                        }
                        while (i < newArray3.length - 1) {
                            newArray3[i] = new Pair();
                            input.readMessage(newArray3[i]);
                            input.readTag();
                            i++;
                        }
                        newArray3[i] = new Pair();
                        input.readMessage(newArray3[i]);
                        this.gethostbynameErrors = newArray3;
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        }
                        return this;
                }
            }
        }

        public static DNSLookupBatch parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (DNSLookupBatch) MessageNano.mergeFrom(new DNSLookupBatch(), data);
        }

        public static DNSLookupBatch parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new DNSLookupBatch().mergeFrom(input);
        }
    }

    public static final class DefaultNetworkEvent extends MessageNano {
        public static final int DISCONNECT = 3;
        public static final int DUAL = 3;
        public static final int INVALIDATION = 2;
        public static final int IPV4 = 1;
        public static final int IPV6 = 2;
        public static final int NONE = 0;
        public static final int OUTSCORED = 1;
        public static final int UNKNOWN = 0;
        private static volatile DefaultNetworkEvent[] _emptyArray;
        public long defaultNetworkDurationMs;
        public long finalScore;
        public long initialScore;
        public int ipSupport;
        public NetworkId networkId;
        public long noDefaultNetworkDurationMs;
        public int previousDefaultNetworkLinkLayer;
        public NetworkId previousNetworkId;
        public int previousNetworkIpSupport;
        public int[] transportTypes;
        public long validationDurationMs;

        public static DefaultNetworkEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new DefaultNetworkEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public DefaultNetworkEvent() {
            clear();
        }

        public DefaultNetworkEvent clear() {
            this.defaultNetworkDurationMs = 0;
            this.validationDurationMs = 0;
            this.initialScore = 0;
            this.finalScore = 0;
            this.ipSupport = 0;
            this.previousDefaultNetworkLinkLayer = 0;
            this.networkId = null;
            this.previousNetworkId = null;
            this.previousNetworkIpSupport = 0;
            this.transportTypes = WireFormatNano.EMPTY_INT_ARRAY;
            this.noDefaultNetworkDurationMs = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            NetworkId networkId = this.networkId;
            if (networkId != null) {
                output.writeMessage(1, networkId);
            }
            networkId = this.previousNetworkId;
            if (networkId != null) {
                output.writeMessage(2, networkId);
            }
            int i = this.previousNetworkIpSupport;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            int[] iArr = this.transportTypes;
            if (iArr != null && iArr.length > 0) {
                i = 0;
                while (true) {
                    int[] iArr2 = this.transportTypes;
                    if (i >= iArr2.length) {
                        break;
                    }
                    output.writeInt32(4, iArr2[i]);
                    i++;
                }
            }
            long j = this.defaultNetworkDurationMs;
            if (j != 0) {
                output.writeInt64(5, j);
            }
            j = this.noDefaultNetworkDurationMs;
            if (j != 0) {
                output.writeInt64(6, j);
            }
            j = this.initialScore;
            if (j != 0) {
                output.writeInt64(7, j);
            }
            j = this.finalScore;
            if (j != 0) {
                output.writeInt64(8, j);
            }
            i = this.ipSupport;
            if (i != 0) {
                output.writeInt32(9, i);
            }
            i = this.previousDefaultNetworkLinkLayer;
            if (i != 0) {
                output.writeInt32(10, i);
            }
            j = this.validationDurationMs;
            if (j != 0) {
                output.writeInt64(11, j);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            NetworkId networkId = this.networkId;
            if (networkId != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(1, networkId);
            }
            networkId = this.previousNetworkId;
            if (networkId != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(2, networkId);
            }
            int i = this.previousNetworkIpSupport;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            int[] iArr = this.transportTypes;
            if (iArr != null && iArr.length > 0) {
                int[] iArr2;
                i = 0;
                int i2 = 0;
                while (true) {
                    iArr2 = this.transportTypes;
                    if (i2 >= iArr2.length) {
                        break;
                    }
                    i += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr2[i2]);
                    i2++;
                }
                size = (size + i) + (iArr2.length * 1);
            }
            long j = this.defaultNetworkDurationMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(5, j);
            }
            j = this.noDefaultNetworkDurationMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(6, j);
            }
            j = this.initialScore;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(7, j);
            }
            j = this.finalScore;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(8, j);
            }
            i = this.ipSupport;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(9, i);
            }
            i = this.previousDefaultNetworkLinkLayer;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(10, i);
            }
            j = this.validationDurationMs;
            if (j != 0) {
                return size + CodedOutputByteBufferNano.computeInt64Size(11, j);
            }
            return size;
        }

        public DefaultNetworkEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                int value;
                int arrayLength;
                int i;
                switch (tag) {
                    case 0:
                        return this;
                    case 10:
                        if (this.networkId == null) {
                            this.networkId = new NetworkId();
                        }
                        input.readMessage(this.networkId);
                        break;
                    case 18:
                        if (this.previousNetworkId == null) {
                            this.previousNetworkId = new NetworkId();
                        }
                        input.readMessage(this.previousNetworkId);
                        break;
                    case 24:
                        value = input.readInt32();
                        if (value != 0 && value != 1 && value != 2 && value != 3) {
                            break;
                        }
                        this.previousNetworkIpSupport = value;
                        break;
                    case 32:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 32);
                        int[] iArr = this.transportTypes;
                        i = iArr == null ? 0 : iArr.length;
                        int[] newArray = new int[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.transportTypes, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = input.readInt32();
                            input.readTag();
                            i++;
                        }
                        newArray[i] = input.readInt32();
                        this.transportTypes = newArray;
                        break;
                    case 34:
                        i = input.pushLimit(input.readRawVarint32());
                        int arrayLength2 = 0;
                        int startPos = input.getPosition();
                        while (input.getBytesUntilLimit() > 0) {
                            input.readInt32();
                            arrayLength2++;
                        }
                        input.rewindToPosition(startPos);
                        int[] iArr2 = this.transportTypes;
                        int i2 = iArr2 == null ? 0 : iArr2.length;
                        int[] newArray2 = new int[(i2 + arrayLength2)];
                        if (i2 != 0) {
                            System.arraycopy(this.transportTypes, 0, newArray2, 0, i2);
                        }
                        while (i2 < newArray2.length) {
                            newArray2[i2] = input.readInt32();
                            i2++;
                        }
                        this.transportTypes = newArray2;
                        input.popLimit(i);
                        break;
                    case 40:
                        this.defaultNetworkDurationMs = input.readInt64();
                        break;
                    case 48:
                        this.noDefaultNetworkDurationMs = input.readInt64();
                        break;
                    case 56:
                        this.initialScore = input.readInt64();
                        break;
                    case 64:
                        this.finalScore = input.readInt64();
                        break;
                    case 72:
                        value = input.readInt32();
                        if (value != 0 && value != 1 && value != 2 && value != 3) {
                            break;
                        }
                        this.ipSupport = value;
                        break;
                        break;
                    case 80:
                        arrayLength = input.readInt32();
                        switch (arrayLength) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                                this.previousDefaultNetworkLinkLayer = arrayLength;
                                break;
                            default:
                                break;
                        }
                    case 88:
                        this.validationDurationMs = input.readInt64();
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        }
                        return this;
                }
            }
        }

        public static DefaultNetworkEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (DefaultNetworkEvent) MessageNano.mergeFrom(new DefaultNetworkEvent(), data);
        }

        public static DefaultNetworkEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new DefaultNetworkEvent().mergeFrom(input);
        }
    }

    public static final class IpConnectivityEvent extends MessageNano {
        public static final int APF_PROGRAM_EVENT_FIELD_NUMBER = 9;
        public static final int APF_STATISTICS_FIELD_NUMBER = 10;
        public static final int CONNECT_STATISTICS_FIELD_NUMBER = 14;
        public static final int DEFAULT_NETWORK_EVENT_FIELD_NUMBER = 2;
        public static final int DHCP_EVENT_FIELD_NUMBER = 6;
        public static final int DNS_LATENCIES_FIELD_NUMBER = 13;
        public static final int DNS_LOOKUP_BATCH_FIELD_NUMBER = 5;
        public static final int IP_PROVISIONING_EVENT_FIELD_NUMBER = 7;
        public static final int IP_REACHABILITY_EVENT_FIELD_NUMBER = 3;
        public static final int NETWORK_EVENT_FIELD_NUMBER = 4;
        public static final int NETWORK_STATS_FIELD_NUMBER = 19;
        public static final int RA_EVENT_FIELD_NUMBER = 11;
        public static final int VALIDATION_PROBE_EVENT_FIELD_NUMBER = 8;
        public static final int WAKEUP_STATS_FIELD_NUMBER = 20;
        private static volatile IpConnectivityEvent[] _emptyArray;
        private int eventCase_ = 0;
        private Object event_;
        public String ifName;
        public int linkLayer;
        public int networkId;
        public long timeMs;
        public long transports;

        public int getEventCase() {
            return this.eventCase_;
        }

        public IpConnectivityEvent clearEvent() {
            this.eventCase_ = 0;
            this.event_ = null;
            return this;
        }

        public static IpConnectivityEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new IpConnectivityEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public boolean hasDefaultNetworkEvent() {
            return this.eventCase_ == 2;
        }

        public DefaultNetworkEvent getDefaultNetworkEvent() {
            if (this.eventCase_ == 2) {
                return (DefaultNetworkEvent) this.event_;
            }
            return null;
        }

        public IpConnectivityEvent setDefaultNetworkEvent(DefaultNetworkEvent value) {
            if (value != null) {
                this.eventCase_ = 2;
                this.event_ = value;
                return this;
            }
            throw new NullPointerException();
        }

        public boolean hasIpReachabilityEvent() {
            return this.eventCase_ == 3;
        }

        public IpReachabilityEvent getIpReachabilityEvent() {
            if (this.eventCase_ == 3) {
                return (IpReachabilityEvent) this.event_;
            }
            return null;
        }

        public IpConnectivityEvent setIpReachabilityEvent(IpReachabilityEvent value) {
            if (value != null) {
                this.eventCase_ = 3;
                this.event_ = value;
                return this;
            }
            throw new NullPointerException();
        }

        public boolean hasNetworkEvent() {
            return this.eventCase_ == 4;
        }

        public NetworkEvent getNetworkEvent() {
            if (this.eventCase_ == 4) {
                return (NetworkEvent) this.event_;
            }
            return null;
        }

        public IpConnectivityEvent setNetworkEvent(NetworkEvent value) {
            if (value != null) {
                this.eventCase_ = 4;
                this.event_ = value;
                return this;
            }
            throw new NullPointerException();
        }

        public boolean hasDnsLookupBatch() {
            return this.eventCase_ == 5;
        }

        public DNSLookupBatch getDnsLookupBatch() {
            if (this.eventCase_ == 5) {
                return (DNSLookupBatch) this.event_;
            }
            return null;
        }

        public IpConnectivityEvent setDnsLookupBatch(DNSLookupBatch value) {
            if (value != null) {
                this.eventCase_ = 5;
                this.event_ = value;
                return this;
            }
            throw new NullPointerException();
        }

        public boolean hasDnsLatencies() {
            return this.eventCase_ == 13;
        }

        public DNSLatencies getDnsLatencies() {
            if (this.eventCase_ == 13) {
                return (DNSLatencies) this.event_;
            }
            return null;
        }

        public IpConnectivityEvent setDnsLatencies(DNSLatencies value) {
            if (value != null) {
                this.eventCase_ = 13;
                this.event_ = value;
                return this;
            }
            throw new NullPointerException();
        }

        public boolean hasConnectStatistics() {
            return this.eventCase_ == 14;
        }

        public ConnectStatistics getConnectStatistics() {
            if (this.eventCase_ == 14) {
                return (ConnectStatistics) this.event_;
            }
            return null;
        }

        public IpConnectivityEvent setConnectStatistics(ConnectStatistics value) {
            if (value != null) {
                this.eventCase_ = 14;
                this.event_ = value;
                return this;
            }
            throw new NullPointerException();
        }

        public boolean hasDhcpEvent() {
            return this.eventCase_ == 6;
        }

        public DHCPEvent getDhcpEvent() {
            if (this.eventCase_ == 6) {
                return (DHCPEvent) this.event_;
            }
            return null;
        }

        public IpConnectivityEvent setDhcpEvent(DHCPEvent value) {
            if (value != null) {
                this.eventCase_ = 6;
                this.event_ = value;
                return this;
            }
            throw new NullPointerException();
        }

        public boolean hasIpProvisioningEvent() {
            return this.eventCase_ == 7;
        }

        public IpProvisioningEvent getIpProvisioningEvent() {
            if (this.eventCase_ == 7) {
                return (IpProvisioningEvent) this.event_;
            }
            return null;
        }

        public IpConnectivityEvent setIpProvisioningEvent(IpProvisioningEvent value) {
            if (value != null) {
                this.eventCase_ = 7;
                this.event_ = value;
                return this;
            }
            throw new NullPointerException();
        }

        public boolean hasValidationProbeEvent() {
            return this.eventCase_ == 8;
        }

        public ValidationProbeEvent getValidationProbeEvent() {
            if (this.eventCase_ == 8) {
                return (ValidationProbeEvent) this.event_;
            }
            return null;
        }

        public IpConnectivityEvent setValidationProbeEvent(ValidationProbeEvent value) {
            if (value != null) {
                this.eventCase_ = 8;
                this.event_ = value;
                return this;
            }
            throw new NullPointerException();
        }

        public boolean hasApfProgramEvent() {
            return this.eventCase_ == 9;
        }

        public ApfProgramEvent getApfProgramEvent() {
            if (this.eventCase_ == 9) {
                return (ApfProgramEvent) this.event_;
            }
            return null;
        }

        public IpConnectivityEvent setApfProgramEvent(ApfProgramEvent value) {
            if (value != null) {
                this.eventCase_ = 9;
                this.event_ = value;
                return this;
            }
            throw new NullPointerException();
        }

        public boolean hasApfStatistics() {
            return this.eventCase_ == 10;
        }

        public ApfStatistics getApfStatistics() {
            if (this.eventCase_ == 10) {
                return (ApfStatistics) this.event_;
            }
            return null;
        }

        public IpConnectivityEvent setApfStatistics(ApfStatistics value) {
            if (value != null) {
                this.eventCase_ = 10;
                this.event_ = value;
                return this;
            }
            throw new NullPointerException();
        }

        public boolean hasRaEvent() {
            return this.eventCase_ == 11;
        }

        public RaEvent getRaEvent() {
            if (this.eventCase_ == 11) {
                return (RaEvent) this.event_;
            }
            return null;
        }

        public IpConnectivityEvent setRaEvent(RaEvent value) {
            if (value != null) {
                this.eventCase_ = 11;
                this.event_ = value;
                return this;
            }
            throw new NullPointerException();
        }

        public boolean hasNetworkStats() {
            return this.eventCase_ == 19;
        }

        public NetworkStats getNetworkStats() {
            if (this.eventCase_ == 19) {
                return (NetworkStats) this.event_;
            }
            return null;
        }

        public IpConnectivityEvent setNetworkStats(NetworkStats value) {
            if (value != null) {
                this.eventCase_ = 19;
                this.event_ = value;
                return this;
            }
            throw new NullPointerException();
        }

        public boolean hasWakeupStats() {
            return this.eventCase_ == 20;
        }

        public WakeupStats getWakeupStats() {
            if (this.eventCase_ == 20) {
                return (WakeupStats) this.event_;
            }
            return null;
        }

        public IpConnectivityEvent setWakeupStats(WakeupStats value) {
            if (value != null) {
                this.eventCase_ = 20;
                this.event_ = value;
                return this;
            }
            throw new NullPointerException();
        }

        public IpConnectivityEvent() {
            clear();
        }

        public IpConnectivityEvent clear() {
            this.timeMs = 0;
            this.linkLayer = 0;
            this.networkId = 0;
            this.ifName = "";
            this.transports = 0;
            clearEvent();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            long j = this.timeMs;
            if (j != 0) {
                output.writeInt64(1, j);
            }
            if (this.eventCase_ == 2) {
                output.writeMessage(2, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 3) {
                output.writeMessage(3, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 4) {
                output.writeMessage(4, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 5) {
                output.writeMessage(5, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 6) {
                output.writeMessage(6, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 7) {
                output.writeMessage(7, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 8) {
                output.writeMessage(8, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 9) {
                output.writeMessage(9, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 10) {
                output.writeMessage(10, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 11) {
                output.writeMessage(11, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 13) {
                output.writeMessage(13, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 14) {
                output.writeMessage(14, (MessageNano) this.event_);
            }
            int i = this.linkLayer;
            if (i != 0) {
                output.writeInt32(15, i);
            }
            i = this.networkId;
            if (i != 0) {
                output.writeInt32(16, i);
            }
            if (!this.ifName.equals("")) {
                output.writeString(17, this.ifName);
            }
            j = this.transports;
            if (j != 0) {
                output.writeInt64(18, j);
            }
            if (this.eventCase_ == 19) {
                output.writeMessage(19, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 20) {
                output.writeMessage(20, (MessageNano) this.event_);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            long j = this.timeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, j);
            }
            if (this.eventCase_ == 2) {
                size += CodedOutputByteBufferNano.computeMessageSize(2, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 3) {
                size += CodedOutputByteBufferNano.computeMessageSize(3, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 4) {
                size += CodedOutputByteBufferNano.computeMessageSize(4, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 5) {
                size += CodedOutputByteBufferNano.computeMessageSize(5, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 6) {
                size += CodedOutputByteBufferNano.computeMessageSize(6, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 7) {
                size += CodedOutputByteBufferNano.computeMessageSize(7, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 8) {
                size += CodedOutputByteBufferNano.computeMessageSize(8, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 9) {
                size += CodedOutputByteBufferNano.computeMessageSize(9, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 10) {
                size += CodedOutputByteBufferNano.computeMessageSize(10, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 11) {
                size += CodedOutputByteBufferNano.computeMessageSize(11, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 13) {
                size += CodedOutputByteBufferNano.computeMessageSize(13, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 14) {
                size += CodedOutputByteBufferNano.computeMessageSize(14, (MessageNano) this.event_);
            }
            int i = this.linkLayer;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(15, i);
            }
            i = this.networkId;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(16, i);
            }
            if (!this.ifName.equals("")) {
                size += CodedOutputByteBufferNano.computeStringSize(17, this.ifName);
            }
            j = this.transports;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(18, j);
            }
            if (this.eventCase_ == 19) {
                size += CodedOutputByteBufferNano.computeMessageSize(19, (MessageNano) this.event_);
            }
            if (this.eventCase_ == 20) {
                return size + CodedOutputByteBufferNano.computeMessageSize(20, (MessageNano) this.event_);
            }
            return size;
        }

        public IpConnectivityEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        this.timeMs = input.readInt64();
                        break;
                    case 18:
                        if (this.eventCase_ != 2) {
                            this.event_ = new DefaultNetworkEvent();
                        }
                        input.readMessage((MessageNano) this.event_);
                        this.eventCase_ = 2;
                        break;
                    case 26:
                        if (this.eventCase_ != 3) {
                            this.event_ = new IpReachabilityEvent();
                        }
                        input.readMessage((MessageNano) this.event_);
                        this.eventCase_ = 3;
                        break;
                    case 34:
                        if (this.eventCase_ != 4) {
                            this.event_ = new NetworkEvent();
                        }
                        input.readMessage((MessageNano) this.event_);
                        this.eventCase_ = 4;
                        break;
                    case 42:
                        if (this.eventCase_ != 5) {
                            this.event_ = new DNSLookupBatch();
                        }
                        input.readMessage((MessageNano) this.event_);
                        this.eventCase_ = 5;
                        break;
                    case 50:
                        if (this.eventCase_ != 6) {
                            this.event_ = new DHCPEvent();
                        }
                        input.readMessage((MessageNano) this.event_);
                        this.eventCase_ = 6;
                        break;
                    case 58:
                        if (this.eventCase_ != 7) {
                            this.event_ = new IpProvisioningEvent();
                        }
                        input.readMessage((MessageNano) this.event_);
                        this.eventCase_ = 7;
                        break;
                    case 66:
                        if (this.eventCase_ != 8) {
                            this.event_ = new ValidationProbeEvent();
                        }
                        input.readMessage((MessageNano) this.event_);
                        this.eventCase_ = 8;
                        break;
                    case 74:
                        if (this.eventCase_ != 9) {
                            this.event_ = new ApfProgramEvent();
                        }
                        input.readMessage((MessageNano) this.event_);
                        this.eventCase_ = 9;
                        break;
                    case 82:
                        if (this.eventCase_ != 10) {
                            this.event_ = new ApfStatistics();
                        }
                        input.readMessage((MessageNano) this.event_);
                        this.eventCase_ = 10;
                        break;
                    case 90:
                        if (this.eventCase_ != 11) {
                            this.event_ = new RaEvent();
                        }
                        input.readMessage((MessageNano) this.event_);
                        this.eventCase_ = 11;
                        break;
                    case 106:
                        if (this.eventCase_ != 13) {
                            this.event_ = new DNSLatencies();
                        }
                        input.readMessage((MessageNano) this.event_);
                        this.eventCase_ = 13;
                        break;
                    case 114:
                        if (this.eventCase_ != 14) {
                            this.event_ = new ConnectStatistics();
                        }
                        input.readMessage((MessageNano) this.event_);
                        this.eventCase_ = 14;
                        break;
                    case 120:
                        int value = input.readInt32();
                        switch (value) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                                this.linkLayer = value;
                                break;
                            default:
                                break;
                        }
                    case 128:
                        this.networkId = input.readInt32();
                        break;
                    case 138:
                        this.ifName = input.readString();
                        break;
                    case 144:
                        this.transports = input.readInt64();
                        break;
                    case 154:
                        if (this.eventCase_ != 19) {
                            this.event_ = new NetworkStats();
                        }
                        input.readMessage((MessageNano) this.event_);
                        this.eventCase_ = 19;
                        break;
                    case 162:
                        if (this.eventCase_ != 20) {
                            this.event_ = new WakeupStats();
                        }
                        input.readMessage((MessageNano) this.event_);
                        this.eventCase_ = 20;
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        }
                        return this;
                }
            }
        }

        public static IpConnectivityEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (IpConnectivityEvent) MessageNano.mergeFrom(new IpConnectivityEvent(), data);
        }

        public static IpConnectivityEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new IpConnectivityEvent().mergeFrom(input);
        }
    }

    public static final class IpConnectivityLog extends MessageNano {
        private static volatile IpConnectivityLog[] _emptyArray;
        public int droppedEvents;
        public IpConnectivityEvent[] events;
        public int version;

        public static IpConnectivityLog[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new IpConnectivityLog[0];
                    }
                }
            }
            return _emptyArray;
        }

        public IpConnectivityLog() {
            clear();
        }

        public IpConnectivityLog clear() {
            this.events = IpConnectivityEvent.emptyArray();
            this.droppedEvents = 0;
            this.version = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i;
            IpConnectivityEvent[] ipConnectivityEventArr = this.events;
            if (ipConnectivityEventArr != null && ipConnectivityEventArr.length > 0) {
                i = 0;
                while (true) {
                    IpConnectivityEvent element = this.events;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        output.writeMessage(1, element);
                    }
                    i++;
                }
            }
            i = this.droppedEvents;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.version;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int i;
            int size = super.computeSerializedSize();
            IpConnectivityEvent[] ipConnectivityEventArr = this.events;
            if (ipConnectivityEventArr != null && ipConnectivityEventArr.length > 0) {
                i = 0;
                while (true) {
                    IpConnectivityEvent element = this.events;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(1, element);
                    }
                    i++;
                }
            }
            i = this.droppedEvents;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.version;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            return size;
        }

        public IpConnectivityLog mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 10);
                    IpConnectivityEvent[] ipConnectivityEventArr = this.events;
                    int i = ipConnectivityEventArr == null ? 0 : ipConnectivityEventArr.length;
                    IpConnectivityEvent[] newArray = new IpConnectivityEvent[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.events, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new IpConnectivityEvent();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new IpConnectivityEvent();
                    input.readMessage(newArray[i]);
                    this.events = newArray;
                } else if (tag == 16) {
                    this.droppedEvents = input.readInt32();
                } else if (tag == 24) {
                    this.version = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static IpConnectivityLog parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (IpConnectivityLog) MessageNano.mergeFrom(new IpConnectivityLog(), data);
        }

        public static IpConnectivityLog parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new IpConnectivityLog().mergeFrom(input);
        }
    }

    public static final class IpProvisioningEvent extends MessageNano {
        private static volatile IpProvisioningEvent[] _emptyArray;
        public int eventType;
        public String ifName;
        public int latencyMs;

        public static IpProvisioningEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new IpProvisioningEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public IpProvisioningEvent() {
            clear();
        }

        public IpProvisioningEvent clear() {
            this.ifName = "";
            this.eventType = 0;
            this.latencyMs = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (!this.ifName.equals("")) {
                output.writeString(1, this.ifName);
            }
            int i = this.eventType;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.latencyMs;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (!this.ifName.equals("")) {
                size += CodedOutputByteBufferNano.computeStringSize(1, this.ifName);
            }
            int i = this.eventType;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.latencyMs;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            return size;
        }

        public IpProvisioningEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.ifName = input.readString();
                } else if (tag == 16) {
                    this.eventType = input.readInt32();
                } else if (tag == 24) {
                    this.latencyMs = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static IpProvisioningEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (IpProvisioningEvent) MessageNano.mergeFrom(new IpProvisioningEvent(), data);
        }

        public static IpProvisioningEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new IpProvisioningEvent().mergeFrom(input);
        }
    }

    public static final class IpReachabilityEvent extends MessageNano {
        private static volatile IpReachabilityEvent[] _emptyArray;
        public int eventType;
        public String ifName;

        public static IpReachabilityEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new IpReachabilityEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public IpReachabilityEvent() {
            clear();
        }

        public IpReachabilityEvent clear() {
            this.ifName = "";
            this.eventType = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (!this.ifName.equals("")) {
                output.writeString(1, this.ifName);
            }
            int i = this.eventType;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (!this.ifName.equals("")) {
                size += CodedOutputByteBufferNano.computeStringSize(1, this.ifName);
            }
            int i = this.eventType;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            return size;
        }

        public IpReachabilityEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.ifName = input.readString();
                } else if (tag == 16) {
                    this.eventType = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static IpReachabilityEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (IpReachabilityEvent) MessageNano.mergeFrom(new IpReachabilityEvent(), data);
        }

        public static IpReachabilityEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new IpReachabilityEvent().mergeFrom(input);
        }
    }

    public static final class NetworkEvent extends MessageNano {
        private static volatile NetworkEvent[] _emptyArray;
        public int eventType;
        public int latencyMs;
        public NetworkId networkId;

        public static NetworkEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new NetworkEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public NetworkEvent() {
            clear();
        }

        public NetworkEvent clear() {
            this.networkId = null;
            this.eventType = 0;
            this.latencyMs = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            NetworkId networkId = this.networkId;
            if (networkId != null) {
                output.writeMessage(1, networkId);
            }
            int i = this.eventType;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.latencyMs;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            NetworkId networkId = this.networkId;
            if (networkId != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(1, networkId);
            }
            int i = this.eventType;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.latencyMs;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            return size;
        }

        public NetworkEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    if (this.networkId == null) {
                        this.networkId = new NetworkId();
                    }
                    input.readMessage(this.networkId);
                } else if (tag == 16) {
                    this.eventType = input.readInt32();
                } else if (tag == 24) {
                    this.latencyMs = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static NetworkEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (NetworkEvent) MessageNano.mergeFrom(new NetworkEvent(), data);
        }

        public static NetworkEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new NetworkEvent().mergeFrom(input);
        }
    }

    public static final class NetworkId extends MessageNano {
        private static volatile NetworkId[] _emptyArray;
        public int networkId;

        public static NetworkId[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new NetworkId[0];
                    }
                }
            }
            return _emptyArray;
        }

        public NetworkId() {
            clear();
        }

        public NetworkId clear() {
            this.networkId = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.networkId;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.networkId;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            return size;
        }

        public NetworkId mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.networkId = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static NetworkId parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (NetworkId) MessageNano.mergeFrom(new NetworkId(), data);
        }

        public static NetworkId parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new NetworkId().mergeFrom(input);
        }
    }

    public static final class NetworkStats extends MessageNano {
        private static volatile NetworkStats[] _emptyArray;
        public long durationMs;
        public boolean everValidated;
        public int ipSupport;
        public int noConnectivityReports;
        public boolean portalFound;
        public int validationAttempts;
        public Pair[] validationEvents;
        public Pair[] validationStates;

        public static NetworkStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new NetworkStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public NetworkStats() {
            clear();
        }

        public NetworkStats clear() {
            this.durationMs = 0;
            this.ipSupport = 0;
            this.everValidated = false;
            this.portalFound = false;
            this.noConnectivityReports = 0;
            this.validationAttempts = 0;
            this.validationEvents = Pair.emptyArray();
            this.validationStates = Pair.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            Pair element;
            long j = this.durationMs;
            if (j != 0) {
                output.writeInt64(1, j);
            }
            int i = this.ipSupport;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            boolean z = this.everValidated;
            if (z) {
                output.writeBool(3, z);
            }
            z = this.portalFound;
            if (z) {
                output.writeBool(4, z);
            }
            i = this.noConnectivityReports;
            if (i != 0) {
                output.writeInt32(5, i);
            }
            i = this.validationAttempts;
            if (i != 0) {
                output.writeInt32(6, i);
            }
            Pair[] pairArr = this.validationEvents;
            if (pairArr != null && pairArr.length > 0) {
                i = 0;
                while (true) {
                    element = this.validationEvents;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        output.writeMessage(7, element);
                    }
                    i++;
                }
            }
            pairArr = this.validationStates;
            if (pairArr != null && pairArr.length > 0) {
                i = 0;
                while (true) {
                    Pair[] pairArr2 = this.validationStates;
                    if (i >= pairArr2.length) {
                        break;
                    }
                    element = pairArr2[i];
                    if (element != null) {
                        output.writeMessage(8, element);
                    }
                    i++;
                }
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            Pair element;
            int size = super.computeSerializedSize();
            long j = this.durationMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, j);
            }
            int i = this.ipSupport;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            boolean z = this.everValidated;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(3, z);
            }
            z = this.portalFound;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(4, z);
            }
            i = this.noConnectivityReports;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(5, i);
            }
            i = this.validationAttempts;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(6, i);
            }
            Pair[] pairArr = this.validationEvents;
            if (pairArr != null && pairArr.length > 0) {
                i = 0;
                while (true) {
                    element = this.validationEvents;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(7, element);
                    }
                    i++;
                }
            }
            pairArr = this.validationStates;
            if (pairArr != null && pairArr.length > 0) {
                i = 0;
                while (true) {
                    Pair[] pairArr2 = this.validationStates;
                    if (i >= pairArr2.length) {
                        break;
                    }
                    element = pairArr2[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(8, element);
                    }
                    i++;
                }
            }
            return size;
        }

        public NetworkStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                int value;
                Pair[] pairArr;
                int i;
                Pair[] newArray;
                if (tag == 8) {
                    this.durationMs = input.readInt64();
                } else if (tag == 16) {
                    value = input.readInt32();
                    if (value == 0 || value == 1 || value == 2 || value == 3) {
                        this.ipSupport = value;
                    }
                } else if (tag == 24) {
                    this.everValidated = input.readBool();
                } else if (tag == 32) {
                    this.portalFound = input.readBool();
                } else if (tag == 40) {
                    this.noConnectivityReports = input.readInt32();
                } else if (tag == 48) {
                    this.validationAttempts = input.readInt32();
                } else if (tag == 58) {
                    value = WireFormatNano.getRepeatedFieldArrayLength(input, 58);
                    pairArr = this.validationEvents;
                    i = pairArr == null ? 0 : pairArr.length;
                    newArray = new Pair[(i + value)];
                    if (i != 0) {
                        System.arraycopy(this.validationEvents, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new Pair();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new Pair();
                    input.readMessage(newArray[i]);
                    this.validationEvents = newArray;
                } else if (tag == 66) {
                    value = WireFormatNano.getRepeatedFieldArrayLength(input, 66);
                    pairArr = this.validationStates;
                    i = pairArr == null ? 0 : pairArr.length;
                    newArray = new Pair[(i + value)];
                    if (i != 0) {
                        System.arraycopy(this.validationStates, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new Pair();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new Pair();
                    input.readMessage(newArray[i]);
                    this.validationStates = newArray;
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static NetworkStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (NetworkStats) MessageNano.mergeFrom(new NetworkStats(), data);
        }

        public static NetworkStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new NetworkStats().mergeFrom(input);
        }
    }

    public static final class Pair extends MessageNano {
        private static volatile Pair[] _emptyArray;
        public int key;
        public int value;

        public static Pair[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new Pair[0];
                    }
                }
            }
            return _emptyArray;
        }

        public Pair() {
            clear();
        }

        public Pair clear() {
            this.key = 0;
            this.value = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.key;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.value;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.key;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.value;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            return size;
        }

        public Pair mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.key = input.readInt32();
                } else if (tag == 16) {
                    this.value = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static Pair parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (Pair) MessageNano.mergeFrom(new Pair(), data);
        }

        public static Pair parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new Pair().mergeFrom(input);
        }
    }

    public static final class RaEvent extends MessageNano {
        private static volatile RaEvent[] _emptyArray;
        public long dnsslLifetime;
        public long prefixPreferredLifetime;
        public long prefixValidLifetime;
        public long rdnssLifetime;
        public long routeInfoLifetime;
        public long routerLifetime;

        public static RaEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new RaEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public RaEvent() {
            clear();
        }

        public RaEvent clear() {
            this.routerLifetime = 0;
            this.prefixValidLifetime = 0;
            this.prefixPreferredLifetime = 0;
            this.routeInfoLifetime = 0;
            this.rdnssLifetime = 0;
            this.dnsslLifetime = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            long j = this.routerLifetime;
            if (j != 0) {
                output.writeInt64(1, j);
            }
            j = this.prefixValidLifetime;
            if (j != 0) {
                output.writeInt64(2, j);
            }
            j = this.prefixPreferredLifetime;
            if (j != 0) {
                output.writeInt64(3, j);
            }
            j = this.routeInfoLifetime;
            if (j != 0) {
                output.writeInt64(4, j);
            }
            j = this.rdnssLifetime;
            if (j != 0) {
                output.writeInt64(5, j);
            }
            j = this.dnsslLifetime;
            if (j != 0) {
                output.writeInt64(6, j);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            long j = this.routerLifetime;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, j);
            }
            j = this.prefixValidLifetime;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(2, j);
            }
            j = this.prefixPreferredLifetime;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(3, j);
            }
            j = this.routeInfoLifetime;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(4, j);
            }
            j = this.rdnssLifetime;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(5, j);
            }
            j = this.dnsslLifetime;
            if (j != 0) {
                return size + CodedOutputByteBufferNano.computeInt64Size(6, j);
            }
            return size;
        }

        public RaEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.routerLifetime = input.readInt64();
                } else if (tag == 16) {
                    this.prefixValidLifetime = input.readInt64();
                } else if (tag == 24) {
                    this.prefixPreferredLifetime = input.readInt64();
                } else if (tag == 32) {
                    this.routeInfoLifetime = input.readInt64();
                } else if (tag == 40) {
                    this.rdnssLifetime = input.readInt64();
                } else if (tag == 48) {
                    this.dnsslLifetime = input.readInt64();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static RaEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (RaEvent) MessageNano.mergeFrom(new RaEvent(), data);
        }

        public static RaEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new RaEvent().mergeFrom(input);
        }
    }

    public static final class ValidationProbeEvent extends MessageNano {
        private static volatile ValidationProbeEvent[] _emptyArray;
        public int latencyMs;
        public NetworkId networkId;
        public int probeResult;
        public int probeType;

        public static ValidationProbeEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new ValidationProbeEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public ValidationProbeEvent() {
            clear();
        }

        public ValidationProbeEvent clear() {
            this.networkId = null;
            this.latencyMs = 0;
            this.probeType = 0;
            this.probeResult = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            NetworkId networkId = this.networkId;
            if (networkId != null) {
                output.writeMessage(1, networkId);
            }
            int i = this.latencyMs;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.probeType;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.probeResult;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            NetworkId networkId = this.networkId;
            if (networkId != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(1, networkId);
            }
            int i = this.latencyMs;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.probeType;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.probeResult;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            return size;
        }

        public ValidationProbeEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    if (this.networkId == null) {
                        this.networkId = new NetworkId();
                    }
                    input.readMessage(this.networkId);
                } else if (tag == 16) {
                    this.latencyMs = input.readInt32();
                } else if (tag == 24) {
                    this.probeType = input.readInt32();
                } else if (tag == 32) {
                    this.probeResult = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static ValidationProbeEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (ValidationProbeEvent) MessageNano.mergeFrom(new ValidationProbeEvent(), data);
        }

        public static ValidationProbeEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new ValidationProbeEvent().mergeFrom(input);
        }
    }

    public static final class WakeupStats extends MessageNano {
        private static volatile WakeupStats[] _emptyArray;
        public long applicationWakeups;
        public long durationSec;
        public Pair[] ethertypeCounts;
        public Pair[] ipNextHeaderCounts;
        public long l2BroadcastCount;
        public long l2MulticastCount;
        public long l2UnicastCount;
        public long noUidWakeups;
        public long nonApplicationWakeups;
        public long rootWakeups;
        public long systemWakeups;
        public long totalWakeups;

        public static WakeupStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WakeupStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WakeupStats() {
            clear();
        }

        public WakeupStats clear() {
            this.durationSec = 0;
            this.totalWakeups = 0;
            this.rootWakeups = 0;
            this.systemWakeups = 0;
            this.applicationWakeups = 0;
            this.nonApplicationWakeups = 0;
            this.noUidWakeups = 0;
            this.ethertypeCounts = Pair.emptyArray();
            this.ipNextHeaderCounts = Pair.emptyArray();
            this.l2UnicastCount = 0;
            this.l2MulticastCount = 0;
            this.l2BroadcastCount = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i;
            Pair element;
            long j = this.durationSec;
            if (j != 0) {
                output.writeInt64(1, j);
            }
            j = this.totalWakeups;
            if (j != 0) {
                output.writeInt64(2, j);
            }
            j = this.rootWakeups;
            if (j != 0) {
                output.writeInt64(3, j);
            }
            j = this.systemWakeups;
            if (j != 0) {
                output.writeInt64(4, j);
            }
            j = this.applicationWakeups;
            if (j != 0) {
                output.writeInt64(5, j);
            }
            j = this.nonApplicationWakeups;
            if (j != 0) {
                output.writeInt64(6, j);
            }
            j = this.noUidWakeups;
            if (j != 0) {
                output.writeInt64(7, j);
            }
            Pair[] pairArr = this.ethertypeCounts;
            if (pairArr != null && pairArr.length > 0) {
                i = 0;
                while (true) {
                    element = this.ethertypeCounts;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        output.writeMessage(8, element);
                    }
                    i++;
                }
            }
            pairArr = this.ipNextHeaderCounts;
            if (pairArr != null && pairArr.length > 0) {
                i = 0;
                while (true) {
                    Pair[] pairArr2 = this.ipNextHeaderCounts;
                    if (i >= pairArr2.length) {
                        break;
                    }
                    element = pairArr2[i];
                    if (element != null) {
                        output.writeMessage(9, element);
                    }
                    i++;
                }
            }
            j = this.l2UnicastCount;
            if (j != 0) {
                output.writeInt64(10, j);
            }
            j = this.l2MulticastCount;
            if (j != 0) {
                output.writeInt64(11, j);
            }
            j = this.l2BroadcastCount;
            if (j != 0) {
                output.writeInt64(12, j);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int i;
            Pair element;
            int size = super.computeSerializedSize();
            long j = this.durationSec;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, j);
            }
            j = this.totalWakeups;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(2, j);
            }
            j = this.rootWakeups;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(3, j);
            }
            j = this.systemWakeups;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(4, j);
            }
            j = this.applicationWakeups;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(5, j);
            }
            j = this.nonApplicationWakeups;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(6, j);
            }
            j = this.noUidWakeups;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(7, j);
            }
            Pair[] pairArr = this.ethertypeCounts;
            if (pairArr != null && pairArr.length > 0) {
                i = 0;
                while (true) {
                    element = this.ethertypeCounts;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(8, element);
                    }
                    i++;
                }
            }
            pairArr = this.ipNextHeaderCounts;
            if (pairArr != null && pairArr.length > 0) {
                i = 0;
                while (true) {
                    Pair[] pairArr2 = this.ipNextHeaderCounts;
                    if (i >= pairArr2.length) {
                        break;
                    }
                    element = pairArr2[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(9, element);
                    }
                    i++;
                }
            }
            j = this.l2UnicastCount;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(10, j);
            }
            j = this.l2MulticastCount;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(11, j);
            }
            j = this.l2BroadcastCount;
            if (j != 0) {
                return size + CodedOutputByteBufferNano.computeInt64Size(12, j);
            }
            return size;
        }

        public WakeupStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                int arrayLength;
                Pair[] pairArr;
                int i;
                Pair[] newArray;
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        this.durationSec = input.readInt64();
                        break;
                    case 16:
                        this.totalWakeups = input.readInt64();
                        break;
                    case 24:
                        this.rootWakeups = input.readInt64();
                        break;
                    case 32:
                        this.systemWakeups = input.readInt64();
                        break;
                    case 40:
                        this.applicationWakeups = input.readInt64();
                        break;
                    case 48:
                        this.nonApplicationWakeups = input.readInt64();
                        break;
                    case 56:
                        this.noUidWakeups = input.readInt64();
                        break;
                    case 66:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 66);
                        pairArr = this.ethertypeCounts;
                        i = pairArr == null ? 0 : pairArr.length;
                        newArray = new Pair[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.ethertypeCounts, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new Pair();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new Pair();
                        input.readMessage(newArray[i]);
                        this.ethertypeCounts = newArray;
                        break;
                    case 74:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 74);
                        pairArr = this.ipNextHeaderCounts;
                        i = pairArr == null ? 0 : pairArr.length;
                        newArray = new Pair[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.ipNextHeaderCounts, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new Pair();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new Pair();
                        input.readMessage(newArray[i]);
                        this.ipNextHeaderCounts = newArray;
                        break;
                    case 80:
                        this.l2UnicastCount = input.readInt64();
                        break;
                    case 88:
                        this.l2MulticastCount = input.readInt64();
                        break;
                    case 96:
                        this.l2BroadcastCount = input.readInt64();
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        }
                        return this;
                }
            }
        }

        public static WakeupStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WakeupStats) MessageNano.mergeFrom(new WakeupStats(), data);
        }

        public static WakeupStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WakeupStats().mergeFrom(input);
        }
    }
}
