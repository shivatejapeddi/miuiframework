package com.android.server.wifi.nano;

import android.app.admin.DevicePolicyManager;
import com.android.framework.protobuf.nano.CodedInputByteBufferNano;
import com.android.framework.protobuf.nano.CodedOutputByteBufferNano;
import com.android.framework.protobuf.nano.InternalNano;
import com.android.framework.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.framework.protobuf.nano.MessageNano;
import com.android.framework.protobuf.nano.WireFormatNano;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.telephony.RILConstants;
import java.io.IOException;
import org.ifaa.android.manager.face.IFAAFaceManager;

public interface WifiMetricsProto {

    public static final class AlertReasonCount extends MessageNano {
        private static volatile AlertReasonCount[] _emptyArray;
        public int count;
        public int reason;

        public static AlertReasonCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new AlertReasonCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public AlertReasonCount() {
            clear();
        }

        public AlertReasonCount clear() {
            this.reason = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.reason;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.count;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.reason;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.count;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            return size;
        }

        public AlertReasonCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.reason = input.readInt32();
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static AlertReasonCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (AlertReasonCount) MessageNano.mergeFrom(new AlertReasonCount(), data);
        }

        public static AlertReasonCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new AlertReasonCount().mergeFrom(input);
        }
    }

    public static final class ConnectToNetworkNotificationAndActionCount extends MessageNano {
        public static final int ACTION_CONNECT_TO_NETWORK = 2;
        public static final int ACTION_PICK_WIFI_NETWORK = 3;
        public static final int ACTION_PICK_WIFI_NETWORK_AFTER_CONNECT_FAILURE = 4;
        public static final int ACTION_UNKNOWN = 0;
        public static final int ACTION_USER_DISMISSED_NOTIFICATION = 1;
        public static final int NOTIFICATION_CONNECTED_TO_NETWORK = 3;
        public static final int NOTIFICATION_CONNECTING_TO_NETWORK = 2;
        public static final int NOTIFICATION_FAILED_TO_CONNECT = 4;
        public static final int NOTIFICATION_RECOMMEND_NETWORK = 1;
        public static final int NOTIFICATION_UNKNOWN = 0;
        public static final int RECOMMENDER_OPEN = 1;
        public static final int RECOMMENDER_UNKNOWN = 0;
        private static volatile ConnectToNetworkNotificationAndActionCount[] _emptyArray;
        public int action;
        public int count;
        public int notification;
        public int recommender;

        public static ConnectToNetworkNotificationAndActionCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new ConnectToNetworkNotificationAndActionCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public ConnectToNetworkNotificationAndActionCount() {
            clear();
        }

        public ConnectToNetworkNotificationAndActionCount clear() {
            this.notification = 0;
            this.action = 0;
            this.recommender = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.notification;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.action;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.recommender;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.count;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.notification;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.action;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.recommender;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.count;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            return size;
        }

        public ConnectToNetworkNotificationAndActionCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                int value;
                if (tag == 8) {
                    value = input.readInt32();
                    if (value == 0 || value == 1 || value == 2 || value == 3 || value == 4) {
                        this.notification = value;
                    }
                } else if (tag == 16) {
                    value = input.readInt32();
                    if (value == 0 || value == 1 || value == 2 || value == 3 || value == 4) {
                        this.action = value;
                    }
                } else if (tag == 24) {
                    value = input.readInt32();
                    if (value == 0 || value == 1) {
                        this.recommender = value;
                    }
                } else if (tag == 32) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static ConnectToNetworkNotificationAndActionCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (ConnectToNetworkNotificationAndActionCount) MessageNano.mergeFrom(new ConnectToNetworkNotificationAndActionCount(), data);
        }

        public static ConnectToNetworkNotificationAndActionCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new ConnectToNetworkNotificationAndActionCount().mergeFrom(input);
        }
    }

    public static final class ConnectionEvent extends MessageNano {
        public static final int AUTH_FAILURE_EAP_FAILURE = 4;
        public static final int AUTH_FAILURE_NONE = 1;
        public static final int AUTH_FAILURE_TIMEOUT = 2;
        public static final int AUTH_FAILURE_WRONG_PSWD = 3;
        public static final int FAILURE_REASON_UNKNOWN = 0;
        public static final int HLF_DHCP = 2;
        public static final int HLF_NONE = 1;
        public static final int HLF_NO_INTERNET = 3;
        public static final int HLF_UNKNOWN = 0;
        public static final int HLF_UNWANTED = 4;
        public static final int NOMINATOR_CARRIER = 5;
        public static final int NOMINATOR_EXTERNAL_SCORED = 6;
        public static final int NOMINATOR_MANUAL = 1;
        public static final int NOMINATOR_OPEN_NETWORK_AVAILABLE = 9;
        public static final int NOMINATOR_PASSPOINT = 4;
        public static final int NOMINATOR_SAVED = 2;
        public static final int NOMINATOR_SAVED_USER_CONNECT_CHOICE = 8;
        public static final int NOMINATOR_SPECIFIER = 7;
        public static final int NOMINATOR_SUGGESTION = 3;
        public static final int NOMINATOR_UNKNOWN = 0;
        public static final int ROAM_DBDC = 2;
        public static final int ROAM_ENTERPRISE = 3;
        public static final int ROAM_NONE = 1;
        public static final int ROAM_UNKNOWN = 0;
        public static final int ROAM_UNRELATED = 5;
        public static final int ROAM_USER_SELECTED = 4;
        private static volatile ConnectionEvent[] _emptyArray;
        public boolean automaticBugReportTaken;
        public int connectionNominator;
        public int connectionResult;
        public int connectivityLevelFailureCode;
        public int durationTakenToConnectMillis;
        public int level2FailureCode;
        public int level2FailureReason;
        public int networkSelectorExperimentId;
        public int roamType;
        public RouterFingerPrint routerFingerprint;
        public int signalStrength;
        public long startTimeMillis;
        public boolean useRandomizedMac;

        public static ConnectionEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new ConnectionEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public ConnectionEvent() {
            clear();
        }

        public ConnectionEvent clear() {
            this.startTimeMillis = 0;
            this.durationTakenToConnectMillis = 0;
            this.routerFingerprint = null;
            this.signalStrength = 0;
            this.roamType = 0;
            this.connectionResult = 0;
            this.level2FailureCode = 0;
            this.connectivityLevelFailureCode = 0;
            this.automaticBugReportTaken = false;
            this.useRandomizedMac = false;
            this.connectionNominator = 0;
            this.networkSelectorExperimentId = 0;
            this.level2FailureReason = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            long j = this.startTimeMillis;
            if (j != 0) {
                output.writeInt64(1, j);
            }
            int i = this.durationTakenToConnectMillis;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            RouterFingerPrint routerFingerPrint = this.routerFingerprint;
            if (routerFingerPrint != null) {
                output.writeMessage(3, routerFingerPrint);
            }
            i = this.signalStrength;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            i = this.roamType;
            if (i != 0) {
                output.writeInt32(5, i);
            }
            i = this.connectionResult;
            if (i != 0) {
                output.writeInt32(6, i);
            }
            i = this.level2FailureCode;
            if (i != 0) {
                output.writeInt32(7, i);
            }
            i = this.connectivityLevelFailureCode;
            if (i != 0) {
                output.writeInt32(8, i);
            }
            boolean z = this.automaticBugReportTaken;
            if (z) {
                output.writeBool(9, z);
            }
            z = this.useRandomizedMac;
            if (z) {
                output.writeBool(10, z);
            }
            i = this.connectionNominator;
            if (i != 0) {
                output.writeInt32(11, i);
            }
            i = this.networkSelectorExperimentId;
            if (i != 0) {
                output.writeInt32(12, i);
            }
            i = this.level2FailureReason;
            if (i != 0) {
                output.writeInt32(13, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            long j = this.startTimeMillis;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, j);
            }
            int i = this.durationTakenToConnectMillis;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            RouterFingerPrint routerFingerPrint = this.routerFingerprint;
            if (routerFingerPrint != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(3, routerFingerPrint);
            }
            i = this.signalStrength;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            i = this.roamType;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(5, i);
            }
            i = this.connectionResult;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(6, i);
            }
            i = this.level2FailureCode;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(7, i);
            }
            i = this.connectivityLevelFailureCode;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(8, i);
            }
            boolean z = this.automaticBugReportTaken;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(9, z);
            }
            z = this.useRandomizedMac;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(10, z);
            }
            i = this.connectionNominator;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(11, i);
            }
            i = this.networkSelectorExperimentId;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(12, i);
            }
            i = this.level2FailureReason;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(13, i);
            }
            return size;
        }

        public ConnectionEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                int value;
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        this.startTimeMillis = input.readInt64();
                        break;
                    case 16:
                        this.durationTakenToConnectMillis = input.readInt32();
                        break;
                    case 26:
                        if (this.routerFingerprint == null) {
                            this.routerFingerprint = new RouterFingerPrint();
                        }
                        input.readMessage(this.routerFingerprint);
                        break;
                    case 32:
                        this.signalStrength = input.readInt32();
                        break;
                    case 40:
                        value = input.readInt32();
                        if (value != 0 && value != 1 && value != 2 && value != 3 && value != 4 && value != 5) {
                            break;
                        }
                        this.roamType = value;
                        break;
                    case 48:
                        this.connectionResult = input.readInt32();
                        break;
                    case 56:
                        this.level2FailureCode = input.readInt32();
                        break;
                    case 64:
                        value = input.readInt32();
                        if (value != 0 && value != 1 && value != 2 && value != 3 && value != 4) {
                            break;
                        }
                        this.connectivityLevelFailureCode = value;
                        break;
                    case 72:
                        this.automaticBugReportTaken = input.readBool();
                        break;
                    case 80:
                        this.useRandomizedMac = input.readBool();
                        break;
                    case 88:
                        int value2 = input.readInt32();
                        switch (value2) {
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
                                this.connectionNominator = value2;
                                break;
                            default:
                                break;
                        }
                    case 96:
                        this.networkSelectorExperimentId = input.readInt32();
                        break;
                    case 104:
                        value = input.readInt32();
                        if (value != 0 && value != 1 && value != 2 && value != 3 && value != 4) {
                            break;
                        }
                        this.level2FailureReason = value;
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        }
                        return this;
                }
            }
        }

        public static ConnectionEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (ConnectionEvent) MessageNano.mergeFrom(new ConnectionEvent(), data);
        }

        public static ConnectionEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new ConnectionEvent().mergeFrom(input);
        }
    }

    public static final class DeviceMobilityStatePnoScanStats extends MessageNano {
        public static final int HIGH_MVMT = 1;
        public static final int LOW_MVMT = 2;
        public static final int STATIONARY = 3;
        public static final int UNKNOWN = 0;
        private static volatile DeviceMobilityStatePnoScanStats[] _emptyArray;
        public int deviceMobilityState;
        public int numTimesEnteredState;
        public long pnoDurationMs;
        public long totalDurationMs;

        public static DeviceMobilityStatePnoScanStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new DeviceMobilityStatePnoScanStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public DeviceMobilityStatePnoScanStats() {
            clear();
        }

        public DeviceMobilityStatePnoScanStats clear() {
            this.deviceMobilityState = 0;
            this.numTimesEnteredState = 0;
            this.totalDurationMs = 0;
            this.pnoDurationMs = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.deviceMobilityState;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.numTimesEnteredState;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            long j = this.totalDurationMs;
            if (j != 0) {
                output.writeInt64(3, j);
            }
            j = this.pnoDurationMs;
            if (j != 0) {
                output.writeInt64(4, j);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.deviceMobilityState;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.numTimesEnteredState;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            long j = this.totalDurationMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(3, j);
            }
            j = this.pnoDurationMs;
            if (j != 0) {
                return size + CodedOutputByteBufferNano.computeInt64Size(4, j);
            }
            return size;
        }

        public DeviceMobilityStatePnoScanStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    int value = input.readInt32();
                    if (value == 0 || value == 1 || value == 2 || value == 3) {
                        this.deviceMobilityState = value;
                    }
                } else if (tag == 16) {
                    this.numTimesEnteredState = input.readInt32();
                } else if (tag == 24) {
                    this.totalDurationMs = input.readInt64();
                } else if (tag == 32) {
                    this.pnoDurationMs = input.readInt64();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static DeviceMobilityStatePnoScanStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (DeviceMobilityStatePnoScanStats) MessageNano.mergeFrom(new DeviceMobilityStatePnoScanStats(), data);
        }

        public static DeviceMobilityStatePnoScanStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new DeviceMobilityStatePnoScanStats().mergeFrom(input);
        }
    }

    public static final class ExperimentValues extends MessageNano {
        private static volatile ExperimentValues[] _emptyArray;
        public boolean linkSpeedCountsLoggingEnabled;
        public int wifiDataStallMinTxBad;
        public int wifiDataStallMinTxSuccessWithoutRx;
        public boolean wifiIsUnusableLoggingEnabled;

        public static ExperimentValues[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new ExperimentValues[0];
                    }
                }
            }
            return _emptyArray;
        }

        public ExperimentValues() {
            clear();
        }

        public ExperimentValues clear() {
            this.wifiIsUnusableLoggingEnabled = false;
            this.wifiDataStallMinTxBad = 0;
            this.wifiDataStallMinTxSuccessWithoutRx = 0;
            this.linkSpeedCountsLoggingEnabled = false;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            boolean z = this.wifiIsUnusableLoggingEnabled;
            if (z) {
                output.writeBool(1, z);
            }
            int i = this.wifiDataStallMinTxBad;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.wifiDataStallMinTxSuccessWithoutRx;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            z = this.linkSpeedCountsLoggingEnabled;
            if (z) {
                output.writeBool(4, z);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            boolean z = this.wifiIsUnusableLoggingEnabled;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(1, z);
            }
            int i = this.wifiDataStallMinTxBad;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.wifiDataStallMinTxSuccessWithoutRx;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            z = this.linkSpeedCountsLoggingEnabled;
            if (z) {
                return size + CodedOutputByteBufferNano.computeBoolSize(4, z);
            }
            return size;
        }

        public ExperimentValues mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.wifiIsUnusableLoggingEnabled = input.readBool();
                } else if (tag == 16) {
                    this.wifiDataStallMinTxBad = input.readInt32();
                } else if (tag == 24) {
                    this.wifiDataStallMinTxSuccessWithoutRx = input.readInt32();
                } else if (tag == 32) {
                    this.linkSpeedCountsLoggingEnabled = input.readBool();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static ExperimentValues parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (ExperimentValues) MessageNano.mergeFrom(new ExperimentValues(), data);
        }

        public static ExperimentValues parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new ExperimentValues().mergeFrom(input);
        }
    }

    public static final class GroupEvent extends MessageNano {
        public static final int GROUP_CLIENT = 1;
        public static final int GROUP_OWNER = 0;
        private static volatile GroupEvent[] _emptyArray;
        public int channelFrequency;
        public int groupRole;
        public int idleDurationMillis;
        public int netId;
        public int numConnectedClients;
        public int numCumulativeClients;
        public int sessionDurationMillis;
        public long startTimeMillis;

        public static GroupEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new GroupEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public GroupEvent() {
            clear();
        }

        public GroupEvent clear() {
            this.netId = 0;
            this.startTimeMillis = 0;
            this.channelFrequency = 0;
            this.groupRole = 0;
            this.numConnectedClients = 0;
            this.numCumulativeClients = 0;
            this.sessionDurationMillis = 0;
            this.idleDurationMillis = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.netId;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            long j = this.startTimeMillis;
            if (j != 0) {
                output.writeInt64(2, j);
            }
            i = this.channelFrequency;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.groupRole;
            if (i != 0) {
                output.writeInt32(5, i);
            }
            i = this.numConnectedClients;
            if (i != 0) {
                output.writeInt32(6, i);
            }
            i = this.numCumulativeClients;
            if (i != 0) {
                output.writeInt32(7, i);
            }
            i = this.sessionDurationMillis;
            if (i != 0) {
                output.writeInt32(8, i);
            }
            i = this.idleDurationMillis;
            if (i != 0) {
                output.writeInt32(9, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.netId;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            long j = this.startTimeMillis;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(2, j);
            }
            i = this.channelFrequency;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.groupRole;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(5, i);
            }
            i = this.numConnectedClients;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(6, i);
            }
            i = this.numCumulativeClients;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(7, i);
            }
            i = this.sessionDurationMillis;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(8, i);
            }
            i = this.idleDurationMillis;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(9, i);
            }
            return size;
        }

        public GroupEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.netId = input.readInt32();
                } else if (tag == 16) {
                    this.startTimeMillis = input.readInt64();
                } else if (tag == 24) {
                    this.channelFrequency = input.readInt32();
                } else if (tag == 40) {
                    int value = input.readInt32();
                    if (value == 0 || value == 1) {
                        this.groupRole = value;
                    }
                } else if (tag == 48) {
                    this.numConnectedClients = input.readInt32();
                } else if (tag == 56) {
                    this.numCumulativeClients = input.readInt32();
                } else if (tag == 64) {
                    this.sessionDurationMillis = input.readInt32();
                } else if (tag == 72) {
                    this.idleDurationMillis = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static GroupEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (GroupEvent) MessageNano.mergeFrom(new GroupEvent(), data);
        }

        public static GroupEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new GroupEvent().mergeFrom(input);
        }
    }

    public static final class HistogramBucketInt32 extends MessageNano {
        private static volatile HistogramBucketInt32[] _emptyArray;
        public int count;
        public int end;
        public int start;

        public static HistogramBucketInt32[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new HistogramBucketInt32[0];
                    }
                }
            }
            return _emptyArray;
        }

        public HistogramBucketInt32() {
            clear();
        }

        public HistogramBucketInt32 clear() {
            this.start = 0;
            this.end = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.start;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.end;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.count;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.start;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.end;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.count;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            return size;
        }

        public HistogramBucketInt32 mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.start = input.readInt32();
                } else if (tag == 16) {
                    this.end = input.readInt32();
                } else if (tag == 24) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static HistogramBucketInt32 parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (HistogramBucketInt32) MessageNano.mergeFrom(new HistogramBucketInt32(), data);
        }

        public static HistogramBucketInt32 parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new HistogramBucketInt32().mergeFrom(input);
        }
    }

    public static final class Int32Count extends MessageNano {
        private static volatile Int32Count[] _emptyArray;
        public int count;
        public int key;

        public static Int32Count[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new Int32Count[0];
                    }
                }
            }
            return _emptyArray;
        }

        public Int32Count() {
            clear();
        }

        public Int32Count clear() {
            this.key = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.key;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.count;
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
            i = this.count;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            return size;
        }

        public Int32Count mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.key = input.readInt32();
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static Int32Count parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (Int32Count) MessageNano.mergeFrom(new Int32Count(), data);
        }

        public static Int32Count parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new Int32Count().mergeFrom(input);
        }
    }

    public static final class LinkProbeStats extends MessageNano {
        public static final int LINK_PROBE_FAILURE_REASON_ALREADY_STARTED = 4;
        public static final int LINK_PROBE_FAILURE_REASON_MCS_UNSUPPORTED = 1;
        public static final int LINK_PROBE_FAILURE_REASON_NO_ACK = 2;
        public static final int LINK_PROBE_FAILURE_REASON_TIMEOUT = 3;
        public static final int LINK_PROBE_FAILURE_REASON_UNKNOWN = 0;
        private static volatile LinkProbeStats[] _emptyArray;
        public ExperimentProbeCounts[] experimentProbeCounts;
        public Int32Count[] failureLinkSpeedCounts;
        public LinkProbeFailureReasonCount[] failureReasonCounts;
        public Int32Count[] failureRssiCounts;
        public HistogramBucketInt32[] failureSecondsSinceLastTxSuccessHistogram;
        public HistogramBucketInt32[] successElapsedTimeMsHistogram;
        public Int32Count[] successLinkSpeedCounts;
        public Int32Count[] successRssiCounts;
        public HistogramBucketInt32[] successSecondsSinceLastTxSuccessHistogram;

        public static final class ExperimentProbeCounts extends MessageNano {
            private static volatile ExperimentProbeCounts[] _emptyArray;
            public String experimentId;
            public int probeCount;

            public static ExperimentProbeCounts[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new ExperimentProbeCounts[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public ExperimentProbeCounts() {
                clear();
            }

            public ExperimentProbeCounts clear() {
                this.experimentId = "";
                this.probeCount = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (!this.experimentId.equals("")) {
                    output.writeString(1, this.experimentId);
                }
                int i = this.probeCount;
                if (i != 0) {
                    output.writeInt32(2, i);
                }
                super.writeTo(output);
            }

            /* Access modifiers changed, original: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                if (!this.experimentId.equals("")) {
                    size += CodedOutputByteBufferNano.computeStringSize(1, this.experimentId);
                }
                int i = this.probeCount;
                if (i != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
                }
                return size;
            }

            public ExperimentProbeCounts mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 10) {
                        this.experimentId = input.readString();
                    } else if (tag == 16) {
                        this.probeCount = input.readInt32();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static ExperimentProbeCounts parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (ExperimentProbeCounts) MessageNano.mergeFrom(new ExperimentProbeCounts(), data);
            }

            public static ExperimentProbeCounts parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new ExperimentProbeCounts().mergeFrom(input);
            }
        }

        public static final class LinkProbeFailureReasonCount extends MessageNano {
            private static volatile LinkProbeFailureReasonCount[] _emptyArray;
            public int count;
            public int failureReason;

            public static LinkProbeFailureReasonCount[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new LinkProbeFailureReasonCount[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public LinkProbeFailureReasonCount() {
                clear();
            }

            public LinkProbeFailureReasonCount clear() {
                this.failureReason = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                int i = this.failureReason;
                if (i != 0) {
                    output.writeInt32(1, i);
                }
                i = this.count;
                if (i != 0) {
                    output.writeInt32(2, i);
                }
                super.writeTo(output);
            }

            /* Access modifiers changed, original: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                int i = this.failureReason;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, i);
                }
                i = this.count;
                if (i != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
                }
                return size;
            }

            public LinkProbeFailureReasonCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        int value = input.readInt32();
                        if (value == 0 || value == 1 || value == 2 || value == 3 || value == 4) {
                            this.failureReason = value;
                        }
                    } else if (tag == 16) {
                        this.count = input.readInt32();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static LinkProbeFailureReasonCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (LinkProbeFailureReasonCount) MessageNano.mergeFrom(new LinkProbeFailureReasonCount(), data);
            }

            public static LinkProbeFailureReasonCount parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new LinkProbeFailureReasonCount().mergeFrom(input);
            }
        }

        public static LinkProbeStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new LinkProbeStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public LinkProbeStats() {
            clear();
        }

        public LinkProbeStats clear() {
            this.successRssiCounts = Int32Count.emptyArray();
            this.failureRssiCounts = Int32Count.emptyArray();
            this.successLinkSpeedCounts = Int32Count.emptyArray();
            this.failureLinkSpeedCounts = Int32Count.emptyArray();
            this.successSecondsSinceLastTxSuccessHistogram = HistogramBucketInt32.emptyArray();
            this.failureSecondsSinceLastTxSuccessHistogram = HistogramBucketInt32.emptyArray();
            this.successElapsedTimeMsHistogram = HistogramBucketInt32.emptyArray();
            this.failureReasonCounts = LinkProbeFailureReasonCount.emptyArray();
            this.experimentProbeCounts = ExperimentProbeCounts.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i;
            Int32Count element;
            Int32Count[] int32CountArr;
            HistogramBucketInt32 element2;
            HistogramBucketInt32[] histogramBucketInt32Arr;
            Int32Count[] int32CountArr2 = this.successRssiCounts;
            if (int32CountArr2 != null && int32CountArr2.length > 0) {
                i = 0;
                while (true) {
                    element = this.successRssiCounts;
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
            int32CountArr2 = this.failureRssiCounts;
            if (int32CountArr2 != null && int32CountArr2.length > 0) {
                i = 0;
                while (true) {
                    int32CountArr = this.failureRssiCounts;
                    if (i >= int32CountArr.length) {
                        break;
                    }
                    element = int32CountArr[i];
                    if (element != null) {
                        output.writeMessage(2, element);
                    }
                    i++;
                }
            }
            int32CountArr2 = this.successLinkSpeedCounts;
            if (int32CountArr2 != null && int32CountArr2.length > 0) {
                i = 0;
                while (true) {
                    int32CountArr = this.successLinkSpeedCounts;
                    if (i >= int32CountArr.length) {
                        break;
                    }
                    element = int32CountArr[i];
                    if (element != null) {
                        output.writeMessage(3, element);
                    }
                    i++;
                }
            }
            int32CountArr2 = this.failureLinkSpeedCounts;
            if (int32CountArr2 != null && int32CountArr2.length > 0) {
                i = 0;
                while (true) {
                    int32CountArr = this.failureLinkSpeedCounts;
                    if (i >= int32CountArr.length) {
                        break;
                    }
                    element = int32CountArr[i];
                    if (element != null) {
                        output.writeMessage(4, element);
                    }
                    i++;
                }
            }
            HistogramBucketInt32[] histogramBucketInt32Arr2 = this.successSecondsSinceLastTxSuccessHistogram;
            if (histogramBucketInt32Arr2 != null && histogramBucketInt32Arr2.length > 0) {
                i = 0;
                while (true) {
                    element2 = this.successSecondsSinceLastTxSuccessHistogram;
                    if (i >= element2.length) {
                        break;
                    }
                    element2 = element2[i];
                    if (element2 != null) {
                        output.writeMessage(5, element2);
                    }
                    i++;
                }
            }
            histogramBucketInt32Arr2 = this.failureSecondsSinceLastTxSuccessHistogram;
            if (histogramBucketInt32Arr2 != null && histogramBucketInt32Arr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketInt32Arr = this.failureSecondsSinceLastTxSuccessHistogram;
                    if (i >= histogramBucketInt32Arr.length) {
                        break;
                    }
                    element2 = histogramBucketInt32Arr[i];
                    if (element2 != null) {
                        output.writeMessage(6, element2);
                    }
                    i++;
                }
            }
            histogramBucketInt32Arr2 = this.successElapsedTimeMsHistogram;
            if (histogramBucketInt32Arr2 != null && histogramBucketInt32Arr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketInt32Arr = this.successElapsedTimeMsHistogram;
                    if (i >= histogramBucketInt32Arr.length) {
                        break;
                    }
                    element2 = histogramBucketInt32Arr[i];
                    if (element2 != null) {
                        output.writeMessage(7, element2);
                    }
                    i++;
                }
            }
            LinkProbeFailureReasonCount[] linkProbeFailureReasonCountArr = this.failureReasonCounts;
            if (linkProbeFailureReasonCountArr != null && linkProbeFailureReasonCountArr.length > 0) {
                i = 0;
                while (true) {
                    LinkProbeFailureReasonCount element3 = this.failureReasonCounts;
                    if (i >= element3.length) {
                        break;
                    }
                    element3 = element3[i];
                    if (element3 != null) {
                        output.writeMessage(8, element3);
                    }
                    i++;
                }
            }
            ExperimentProbeCounts[] experimentProbeCountsArr = this.experimentProbeCounts;
            if (experimentProbeCountsArr != null && experimentProbeCountsArr.length > 0) {
                i = 0;
                while (true) {
                    ExperimentProbeCounts element4 = this.experimentProbeCounts;
                    if (i >= element4.length) {
                        break;
                    }
                    element4 = element4[i];
                    if (element4 != null) {
                        output.writeMessage(9, element4);
                    }
                    i++;
                }
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int i;
            Int32Count element;
            Int32Count[] int32CountArr;
            HistogramBucketInt32 element2;
            HistogramBucketInt32[] histogramBucketInt32Arr;
            int size = super.computeSerializedSize();
            Int32Count[] int32CountArr2 = this.successRssiCounts;
            if (int32CountArr2 != null && int32CountArr2.length > 0) {
                i = 0;
                while (true) {
                    element = this.successRssiCounts;
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
            int32CountArr2 = this.failureRssiCounts;
            if (int32CountArr2 != null && int32CountArr2.length > 0) {
                i = 0;
                while (true) {
                    int32CountArr = this.failureRssiCounts;
                    if (i >= int32CountArr.length) {
                        break;
                    }
                    element = int32CountArr[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(2, element);
                    }
                    i++;
                }
            }
            int32CountArr2 = this.successLinkSpeedCounts;
            if (int32CountArr2 != null && int32CountArr2.length > 0) {
                i = 0;
                while (true) {
                    int32CountArr = this.successLinkSpeedCounts;
                    if (i >= int32CountArr.length) {
                        break;
                    }
                    element = int32CountArr[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(3, element);
                    }
                    i++;
                }
            }
            int32CountArr2 = this.failureLinkSpeedCounts;
            if (int32CountArr2 != null && int32CountArr2.length > 0) {
                i = 0;
                while (true) {
                    int32CountArr = this.failureLinkSpeedCounts;
                    if (i >= int32CountArr.length) {
                        break;
                    }
                    element = int32CountArr[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(4, element);
                    }
                    i++;
                }
            }
            HistogramBucketInt32[] histogramBucketInt32Arr2 = this.successSecondsSinceLastTxSuccessHistogram;
            if (histogramBucketInt32Arr2 != null && histogramBucketInt32Arr2.length > 0) {
                i = 0;
                while (true) {
                    element2 = this.successSecondsSinceLastTxSuccessHistogram;
                    if (i >= element2.length) {
                        break;
                    }
                    element2 = element2[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(5, element2);
                    }
                    i++;
                }
            }
            histogramBucketInt32Arr2 = this.failureSecondsSinceLastTxSuccessHistogram;
            if (histogramBucketInt32Arr2 != null && histogramBucketInt32Arr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketInt32Arr = this.failureSecondsSinceLastTxSuccessHistogram;
                    if (i >= histogramBucketInt32Arr.length) {
                        break;
                    }
                    element2 = histogramBucketInt32Arr[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(6, element2);
                    }
                    i++;
                }
            }
            histogramBucketInt32Arr2 = this.successElapsedTimeMsHistogram;
            if (histogramBucketInt32Arr2 != null && histogramBucketInt32Arr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketInt32Arr = this.successElapsedTimeMsHistogram;
                    if (i >= histogramBucketInt32Arr.length) {
                        break;
                    }
                    element2 = histogramBucketInt32Arr[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(7, element2);
                    }
                    i++;
                }
            }
            LinkProbeFailureReasonCount[] linkProbeFailureReasonCountArr = this.failureReasonCounts;
            if (linkProbeFailureReasonCountArr != null && linkProbeFailureReasonCountArr.length > 0) {
                i = 0;
                while (true) {
                    LinkProbeFailureReasonCount element3 = this.failureReasonCounts;
                    if (i >= element3.length) {
                        break;
                    }
                    element3 = element3[i];
                    if (element3 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(8, element3);
                    }
                    i++;
                }
            }
            ExperimentProbeCounts[] experimentProbeCountsArr = this.experimentProbeCounts;
            if (experimentProbeCountsArr != null && experimentProbeCountsArr.length > 0) {
                i = 0;
                while (true) {
                    ExperimentProbeCounts element4 = this.experimentProbeCounts;
                    if (i >= element4.length) {
                        break;
                    }
                    element4 = element4[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(9, element4);
                    }
                    i++;
                }
            }
            return size;
        }

        public LinkProbeStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                int arrayLength;
                Int32Count[] int32CountArr;
                int i;
                Int32Count[] newArray;
                HistogramBucketInt32[] histogramBucketInt32Arr;
                HistogramBucketInt32[] newArray2;
                if (tag == 10) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 10);
                    int32CountArr = this.successRssiCounts;
                    i = int32CountArr == null ? 0 : int32CountArr.length;
                    newArray = new Int32Count[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.successRssiCounts, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new Int32Count();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new Int32Count();
                    input.readMessage(newArray[i]);
                    this.successRssiCounts = newArray;
                } else if (tag == 18) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    int32CountArr = this.failureRssiCounts;
                    i = int32CountArr == null ? 0 : int32CountArr.length;
                    newArray = new Int32Count[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.failureRssiCounts, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new Int32Count();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new Int32Count();
                    input.readMessage(newArray[i]);
                    this.failureRssiCounts = newArray;
                } else if (tag == 26) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 26);
                    int32CountArr = this.successLinkSpeedCounts;
                    i = int32CountArr == null ? 0 : int32CountArr.length;
                    newArray = new Int32Count[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.successLinkSpeedCounts, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new Int32Count();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new Int32Count();
                    input.readMessage(newArray[i]);
                    this.successLinkSpeedCounts = newArray;
                } else if (tag == 34) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                    int32CountArr = this.failureLinkSpeedCounts;
                    i = int32CountArr == null ? 0 : int32CountArr.length;
                    newArray = new Int32Count[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.failureLinkSpeedCounts, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new Int32Count();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new Int32Count();
                    input.readMessage(newArray[i]);
                    this.failureLinkSpeedCounts = newArray;
                } else if (tag == 42) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 42);
                    histogramBucketInt32Arr = this.successSecondsSinceLastTxSuccessHistogram;
                    i = histogramBucketInt32Arr == null ? 0 : histogramBucketInt32Arr.length;
                    newArray2 = new HistogramBucketInt32[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.successSecondsSinceLastTxSuccessHistogram, 0, newArray2, 0, i);
                    }
                    while (i < newArray2.length - 1) {
                        newArray2[i] = new HistogramBucketInt32();
                        input.readMessage(newArray2[i]);
                        input.readTag();
                        i++;
                    }
                    newArray2[i] = new HistogramBucketInt32();
                    input.readMessage(newArray2[i]);
                    this.successSecondsSinceLastTxSuccessHistogram = newArray2;
                } else if (tag == 50) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 50);
                    histogramBucketInt32Arr = this.failureSecondsSinceLastTxSuccessHistogram;
                    i = histogramBucketInt32Arr == null ? 0 : histogramBucketInt32Arr.length;
                    newArray2 = new HistogramBucketInt32[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.failureSecondsSinceLastTxSuccessHistogram, 0, newArray2, 0, i);
                    }
                    while (i < newArray2.length - 1) {
                        newArray2[i] = new HistogramBucketInt32();
                        input.readMessage(newArray2[i]);
                        input.readTag();
                        i++;
                    }
                    newArray2[i] = new HistogramBucketInt32();
                    input.readMessage(newArray2[i]);
                    this.failureSecondsSinceLastTxSuccessHistogram = newArray2;
                } else if (tag == 58) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 58);
                    histogramBucketInt32Arr = this.successElapsedTimeMsHistogram;
                    i = histogramBucketInt32Arr == null ? 0 : histogramBucketInt32Arr.length;
                    newArray2 = new HistogramBucketInt32[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.successElapsedTimeMsHistogram, 0, newArray2, 0, i);
                    }
                    while (i < newArray2.length - 1) {
                        newArray2[i] = new HistogramBucketInt32();
                        input.readMessage(newArray2[i]);
                        input.readTag();
                        i++;
                    }
                    newArray2[i] = new HistogramBucketInt32();
                    input.readMessage(newArray2[i]);
                    this.successElapsedTimeMsHistogram = newArray2;
                } else if (tag == 66) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 66);
                    LinkProbeFailureReasonCount[] linkProbeFailureReasonCountArr = this.failureReasonCounts;
                    i = linkProbeFailureReasonCountArr == null ? 0 : linkProbeFailureReasonCountArr.length;
                    LinkProbeFailureReasonCount[] newArray3 = new LinkProbeFailureReasonCount[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.failureReasonCounts, 0, newArray3, 0, i);
                    }
                    while (i < newArray3.length - 1) {
                        newArray3[i] = new LinkProbeFailureReasonCount();
                        input.readMessage(newArray3[i]);
                        input.readTag();
                        i++;
                    }
                    newArray3[i] = new LinkProbeFailureReasonCount();
                    input.readMessage(newArray3[i]);
                    this.failureReasonCounts = newArray3;
                } else if (tag == 74) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 74);
                    ExperimentProbeCounts[] experimentProbeCountsArr = this.experimentProbeCounts;
                    i = experimentProbeCountsArr == null ? 0 : experimentProbeCountsArr.length;
                    ExperimentProbeCounts[] newArray4 = new ExperimentProbeCounts[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.experimentProbeCounts, 0, newArray4, 0, i);
                    }
                    while (i < newArray4.length - 1) {
                        newArray4[i] = new ExperimentProbeCounts();
                        input.readMessage(newArray4[i]);
                        input.readTag();
                        i++;
                    }
                    newArray4[i] = new ExperimentProbeCounts();
                    input.readMessage(newArray4[i]);
                    this.experimentProbeCounts = newArray4;
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static LinkProbeStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (LinkProbeStats) MessageNano.mergeFrom(new LinkProbeStats(), data);
        }

        public static LinkProbeStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new LinkProbeStats().mergeFrom(input);
        }
    }

    public static final class LinkSpeedCount extends MessageNano {
        private static volatile LinkSpeedCount[] _emptyArray;
        public int count;
        public int linkSpeedMbps;
        public int rssiSumDbm;
        public long rssiSumOfSquaresDbmSq;

        public static LinkSpeedCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new LinkSpeedCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public LinkSpeedCount() {
            clear();
        }

        public LinkSpeedCount clear() {
            this.linkSpeedMbps = 0;
            this.count = 0;
            this.rssiSumDbm = 0;
            this.rssiSumOfSquaresDbmSq = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.linkSpeedMbps;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.count;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.rssiSumDbm;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            long j = this.rssiSumOfSquaresDbmSq;
            if (j != 0) {
                output.writeInt64(4, j);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.linkSpeedMbps;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.count;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.rssiSumDbm;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            long j = this.rssiSumOfSquaresDbmSq;
            if (j != 0) {
                return size + CodedOutputByteBufferNano.computeInt64Size(4, j);
            }
            return size;
        }

        public LinkSpeedCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.linkSpeedMbps = input.readInt32();
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (tag == 24) {
                    this.rssiSumDbm = input.readInt32();
                } else if (tag == 32) {
                    this.rssiSumOfSquaresDbmSq = input.readInt64();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static LinkSpeedCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (LinkSpeedCount) MessageNano.mergeFrom(new LinkSpeedCount(), data);
        }

        public static LinkSpeedCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new LinkSpeedCount().mergeFrom(input);
        }
    }

    public static final class NetworkSelectionExperimentDecisions extends MessageNano {
        private static volatile NetworkSelectionExperimentDecisions[] _emptyArray;
        public Int32Count[] differentSelectionNumChoicesCounter;
        public int experiment1Id;
        public int experiment2Id;
        public Int32Count[] sameSelectionNumChoicesCounter;

        public static NetworkSelectionExperimentDecisions[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new NetworkSelectionExperimentDecisions[0];
                    }
                }
            }
            return _emptyArray;
        }

        public NetworkSelectionExperimentDecisions() {
            clear();
        }

        public NetworkSelectionExperimentDecisions clear() {
            this.experiment1Id = 0;
            this.experiment2Id = 0;
            this.sameSelectionNumChoicesCounter = Int32Count.emptyArray();
            this.differentSelectionNumChoicesCounter = Int32Count.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            Int32Count element;
            int i = this.experiment1Id;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.experiment2Id;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            Int32Count[] int32CountArr = this.sameSelectionNumChoicesCounter;
            if (int32CountArr != null && int32CountArr.length > 0) {
                i = 0;
                while (true) {
                    element = this.sameSelectionNumChoicesCounter;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        output.writeMessage(3, element);
                    }
                    i++;
                }
            }
            int32CountArr = this.differentSelectionNumChoicesCounter;
            if (int32CountArr != null && int32CountArr.length > 0) {
                i = 0;
                while (true) {
                    Int32Count[] int32CountArr2 = this.differentSelectionNumChoicesCounter;
                    if (i >= int32CountArr2.length) {
                        break;
                    }
                    element = int32CountArr2[i];
                    if (element != null) {
                        output.writeMessage(4, element);
                    }
                    i++;
                }
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            Int32Count element;
            int size = super.computeSerializedSize();
            int i = this.experiment1Id;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.experiment2Id;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            Int32Count[] int32CountArr = this.sameSelectionNumChoicesCounter;
            if (int32CountArr != null && int32CountArr.length > 0) {
                i = 0;
                while (true) {
                    element = this.sameSelectionNumChoicesCounter;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(3, element);
                    }
                    i++;
                }
            }
            int32CountArr = this.differentSelectionNumChoicesCounter;
            if (int32CountArr != null && int32CountArr.length > 0) {
                i = 0;
                while (true) {
                    Int32Count[] int32CountArr2 = this.differentSelectionNumChoicesCounter;
                    if (i >= int32CountArr2.length) {
                        break;
                    }
                    element = int32CountArr2[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(4, element);
                    }
                    i++;
                }
            }
            return size;
        }

        public NetworkSelectionExperimentDecisions mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                int arrayLength;
                Int32Count[] int32CountArr;
                int i;
                Int32Count[] newArray;
                if (tag == 8) {
                    this.experiment1Id = input.readInt32();
                } else if (tag == 16) {
                    this.experiment2Id = input.readInt32();
                } else if (tag == 26) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 26);
                    int32CountArr = this.sameSelectionNumChoicesCounter;
                    i = int32CountArr == null ? 0 : int32CountArr.length;
                    newArray = new Int32Count[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.sameSelectionNumChoicesCounter, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new Int32Count();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new Int32Count();
                    input.readMessage(newArray[i]);
                    this.sameSelectionNumChoicesCounter = newArray;
                } else if (tag == 34) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                    int32CountArr = this.differentSelectionNumChoicesCounter;
                    i = int32CountArr == null ? 0 : int32CountArr.length;
                    newArray = new Int32Count[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.differentSelectionNumChoicesCounter, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new Int32Count();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new Int32Count();
                    input.readMessage(newArray[i]);
                    this.differentSelectionNumChoicesCounter = newArray;
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static NetworkSelectionExperimentDecisions parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (NetworkSelectionExperimentDecisions) MessageNano.mergeFrom(new NetworkSelectionExperimentDecisions(), data);
        }

        public static NetworkSelectionExperimentDecisions parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new NetworkSelectionExperimentDecisions().mergeFrom(input);
        }
    }

    public static final class NumConnectableNetworksBucket extends MessageNano {
        private static volatile NumConnectableNetworksBucket[] _emptyArray;
        public int count;
        public int numConnectableNetworks;

        public static NumConnectableNetworksBucket[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new NumConnectableNetworksBucket[0];
                    }
                }
            }
            return _emptyArray;
        }

        public NumConnectableNetworksBucket() {
            clear();
        }

        public NumConnectableNetworksBucket clear() {
            this.numConnectableNetworks = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.numConnectableNetworks;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.count;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.numConnectableNetworks;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.count;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            return size;
        }

        public NumConnectableNetworksBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numConnectableNetworks = input.readInt32();
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static NumConnectableNetworksBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (NumConnectableNetworksBucket) MessageNano.mergeFrom(new NumConnectableNetworksBucket(), data);
        }

        public static NumConnectableNetworksBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new NumConnectableNetworksBucket().mergeFrom(input);
        }
    }

    public static final class P2pConnectionEvent extends MessageNano {
        public static final int CLF_CANCEL = 3;
        public static final int CLF_INVITATION_FAIL = 5;
        public static final int CLF_NEW_CONNECTION_ATTEMPT = 7;
        public static final int CLF_NONE = 1;
        public static final int CLF_PROV_DISC_FAIL = 4;
        public static final int CLF_TIMEOUT = 2;
        public static final int CLF_UNKNOWN = 0;
        public static final int CLF_USER_REJECT = 6;
        public static final int CONNECTION_FAST = 3;
        public static final int CONNECTION_FRESH = 0;
        public static final int CONNECTION_LOCAL = 2;
        public static final int CONNECTION_REINVOKE = 1;
        public static final int WPS_DISPLAY = 1;
        public static final int WPS_KEYPAD = 2;
        public static final int WPS_LABEL = 3;
        public static final int WPS_NA = -1;
        public static final int WPS_PBC = 0;
        private static volatile P2pConnectionEvent[] _emptyArray;
        public int connectionType;
        public int connectivityLevelFailureCode;
        public int durationTakenToConnectMillis;
        public long startTimeMillis;
        public int wpsMethod;

        public static P2pConnectionEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new P2pConnectionEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public P2pConnectionEvent() {
            clear();
        }

        public P2pConnectionEvent clear() {
            this.startTimeMillis = 0;
            this.connectionType = 0;
            this.wpsMethod = -1;
            this.durationTakenToConnectMillis = 0;
            this.connectivityLevelFailureCode = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            long j = this.startTimeMillis;
            if (j != 0) {
                output.writeInt64(1, j);
            }
            int i = this.connectionType;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.wpsMethod;
            if (i != -1) {
                output.writeInt32(3, i);
            }
            i = this.durationTakenToConnectMillis;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            i = this.connectivityLevelFailureCode;
            if (i != 0) {
                output.writeInt32(5, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            long j = this.startTimeMillis;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, j);
            }
            int i = this.connectionType;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.wpsMethod;
            if (i != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.durationTakenToConnectMillis;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            i = this.connectivityLevelFailureCode;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(5, i);
            }
            return size;
        }

        public P2pConnectionEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                int value;
                if (tag == 8) {
                    this.startTimeMillis = input.readInt64();
                } else if (tag == 16) {
                    value = input.readInt32();
                    if (value == 0 || value == 1 || value == 2 || value == 3) {
                        this.connectionType = value;
                    }
                } else if (tag == 24) {
                    value = input.readInt32();
                    if (value == -1 || value == 0 || value == 1 || value == 2 || value == 3) {
                        this.wpsMethod = value;
                    }
                } else if (tag != 32) {
                    if (tag == 40) {
                        value = input.readInt32();
                        switch (value) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                                this.connectivityLevelFailureCode = value;
                                break;
                            default:
                                break;
                        }
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    this.durationTakenToConnectMillis = input.readInt32();
                }
            }
        }

        public static P2pConnectionEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (P2pConnectionEvent) MessageNano.mergeFrom(new P2pConnectionEvent(), data);
        }

        public static P2pConnectionEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new P2pConnectionEvent().mergeFrom(input);
        }
    }

    public static final class PasspointProfileTypeCount extends MessageNano {
        public static final int TYPE_EAP_AKA = 4;
        public static final int TYPE_EAP_AKA_PRIME = 5;
        public static final int TYPE_EAP_SIM = 3;
        public static final int TYPE_EAP_TLS = 1;
        public static final int TYPE_EAP_TTLS = 2;
        public static final int TYPE_UNKNOWN = 0;
        private static volatile PasspointProfileTypeCount[] _emptyArray;
        public int count;
        public int eapMethodType;

        public static PasspointProfileTypeCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new PasspointProfileTypeCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public PasspointProfileTypeCount() {
            clear();
        }

        public PasspointProfileTypeCount clear() {
            this.eapMethodType = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.eapMethodType;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.count;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.eapMethodType;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.count;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            return size;
        }

        public PasspointProfileTypeCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    int value = input.readInt32();
                    if (value == 0 || value == 1 || value == 2 || value == 3 || value == 4 || value == 5) {
                        this.eapMethodType = value;
                    }
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static PasspointProfileTypeCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (PasspointProfileTypeCount) MessageNano.mergeFrom(new PasspointProfileTypeCount(), data);
        }

        public static PasspointProfileTypeCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new PasspointProfileTypeCount().mergeFrom(input);
        }
    }

    public static final class PasspointProvisionStats extends MessageNano {
        public static final int OSU_FAILURE_ADD_PASSPOINT_CONFIGURATION = 22;
        public static final int OSU_FAILURE_AP_CONNECTION = 1;
        public static final int OSU_FAILURE_INVALID_URL_FORMAT_FOR_OSU = 8;
        public static final int OSU_FAILURE_NO_AAA_SERVER_TRUST_ROOT_NODE = 17;
        public static final int OSU_FAILURE_NO_AAA_TRUST_ROOT_CERTIFICATE = 21;
        public static final int OSU_FAILURE_NO_OSU_ACTIVITY_FOUND = 14;
        public static final int OSU_FAILURE_NO_POLICY_SERVER_TRUST_ROOT_NODE = 19;
        public static final int OSU_FAILURE_NO_PPS_MO = 16;
        public static final int OSU_FAILURE_NO_REMEDIATION_SERVER_TRUST_ROOT_NODE = 18;
        public static final int OSU_FAILURE_OSU_PROVIDER_NOT_FOUND = 23;
        public static final int OSU_FAILURE_PROVISIONING_ABORTED = 6;
        public static final int OSU_FAILURE_PROVISIONING_NOT_AVAILABLE = 7;
        public static final int OSU_FAILURE_RETRIEVE_TRUST_ROOT_CERTIFICATES = 20;
        public static final int OSU_FAILURE_SERVER_CONNECTION = 3;
        public static final int OSU_FAILURE_SERVER_URL_INVALID = 2;
        public static final int OSU_FAILURE_SERVER_VALIDATION = 4;
        public static final int OSU_FAILURE_SERVICE_PROVIDER_VERIFICATION = 5;
        public static final int OSU_FAILURE_SOAP_MESSAGE_EXCHANGE = 11;
        public static final int OSU_FAILURE_START_REDIRECT_LISTENER = 12;
        public static final int OSU_FAILURE_TIMED_OUT_REDIRECT_LISTENER = 13;
        public static final int OSU_FAILURE_UNEXPECTED_COMMAND_TYPE = 9;
        public static final int OSU_FAILURE_UNEXPECTED_SOAP_MESSAGE_STATUS = 15;
        public static final int OSU_FAILURE_UNEXPECTED_SOAP_MESSAGE_TYPE = 10;
        public static final int OSU_FAILURE_UNKNOWN = 0;
        private static volatile PasspointProvisionStats[] _emptyArray;
        public int numProvisionSuccess;
        public ProvisionFailureCount[] provisionFailureCount;

        public static final class ProvisionFailureCount extends MessageNano {
            private static volatile ProvisionFailureCount[] _emptyArray;
            public int count;
            public int failureCode;

            public static ProvisionFailureCount[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new ProvisionFailureCount[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public ProvisionFailureCount() {
                clear();
            }

            public ProvisionFailureCount clear() {
                this.failureCode = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                int i = this.failureCode;
                if (i != 0) {
                    output.writeInt32(1, i);
                }
                i = this.count;
                if (i != 0) {
                    output.writeInt32(2, i);
                }
                super.writeTo(output);
            }

            /* Access modifiers changed, original: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                int i = this.failureCode;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, i);
                }
                i = this.count;
                if (i != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
                }
                return size;
            }

            public ProvisionFailureCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag != 0) {
                        if (tag == 8) {
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
                                case 10:
                                case 11:
                                case 12:
                                case 13:
                                case 14:
                                case 15:
                                case 16:
                                case 17:
                                case 18:
                                case 19:
                                case 20:
                                case 21:
                                case 22:
                                case 23:
                                    this.failureCode = value;
                                    break;
                                default:
                                    break;
                            }
                        } else if (tag == 16) {
                            this.count = input.readInt32();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        return this;
                    }
                }
            }

            public static ProvisionFailureCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (ProvisionFailureCount) MessageNano.mergeFrom(new ProvisionFailureCount(), data);
            }

            public static ProvisionFailureCount parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new ProvisionFailureCount().mergeFrom(input);
            }
        }

        public static PasspointProvisionStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new PasspointProvisionStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public PasspointProvisionStats() {
            clear();
        }

        public PasspointProvisionStats clear() {
            this.numProvisionSuccess = 0;
            this.provisionFailureCount = ProvisionFailureCount.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.numProvisionSuccess;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            ProvisionFailureCount[] provisionFailureCountArr = this.provisionFailureCount;
            if (provisionFailureCountArr != null && provisionFailureCountArr.length > 0) {
                i = 0;
                while (true) {
                    ProvisionFailureCount element = this.provisionFailureCount;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        output.writeMessage(2, element);
                    }
                    i++;
                }
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.numProvisionSuccess;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            ProvisionFailureCount[] provisionFailureCountArr = this.provisionFailureCount;
            if (provisionFailureCountArr != null && provisionFailureCountArr.length > 0) {
                i = 0;
                while (true) {
                    ProvisionFailureCount element = this.provisionFailureCount;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(2, element);
                    }
                    i++;
                }
            }
            return size;
        }

        public PasspointProvisionStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numProvisionSuccess = input.readInt32();
                } else if (tag == 18) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    ProvisionFailureCount[] provisionFailureCountArr = this.provisionFailureCount;
                    int i = provisionFailureCountArr == null ? 0 : provisionFailureCountArr.length;
                    ProvisionFailureCount[] newArray = new ProvisionFailureCount[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.provisionFailureCount, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new ProvisionFailureCount();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new ProvisionFailureCount();
                    input.readMessage(newArray[i]);
                    this.provisionFailureCount = newArray;
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static PasspointProvisionStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (PasspointProvisionStats) MessageNano.mergeFrom(new PasspointProvisionStats(), data);
        }

        public static PasspointProvisionStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new PasspointProvisionStats().mergeFrom(input);
        }
    }

    public static final class PnoScanMetrics extends MessageNano {
        private static volatile PnoScanMetrics[] _emptyArray;
        public int numPnoFoundNetworkEvents;
        public int numPnoScanAttempts;
        public int numPnoScanFailed;
        public int numPnoScanFailedOverOffload;
        public int numPnoScanStartedOverOffload;

        public static PnoScanMetrics[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new PnoScanMetrics[0];
                    }
                }
            }
            return _emptyArray;
        }

        public PnoScanMetrics() {
            clear();
        }

        public PnoScanMetrics clear() {
            this.numPnoScanAttempts = 0;
            this.numPnoScanFailed = 0;
            this.numPnoScanStartedOverOffload = 0;
            this.numPnoScanFailedOverOffload = 0;
            this.numPnoFoundNetworkEvents = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.numPnoScanAttempts;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.numPnoScanFailed;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.numPnoScanStartedOverOffload;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.numPnoScanFailedOverOffload;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            i = this.numPnoFoundNetworkEvents;
            if (i != 0) {
                output.writeInt32(5, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.numPnoScanAttempts;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.numPnoScanFailed;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.numPnoScanStartedOverOffload;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.numPnoScanFailedOverOffload;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            i = this.numPnoFoundNetworkEvents;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(5, i);
            }
            return size;
        }

        public PnoScanMetrics mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numPnoScanAttempts = input.readInt32();
                } else if (tag == 16) {
                    this.numPnoScanFailed = input.readInt32();
                } else if (tag == 24) {
                    this.numPnoScanStartedOverOffload = input.readInt32();
                } else if (tag == 32) {
                    this.numPnoScanFailedOverOffload = input.readInt32();
                } else if (tag == 40) {
                    this.numPnoFoundNetworkEvents = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static PnoScanMetrics parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (PnoScanMetrics) MessageNano.mergeFrom(new PnoScanMetrics(), data);
        }

        public static PnoScanMetrics parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new PnoScanMetrics().mergeFrom(input);
        }
    }

    public static final class RouterFingerPrint extends MessageNano {
        public static final int AUTH_ENTERPRISE = 3;
        public static final int AUTH_OPEN = 1;
        public static final int AUTH_PERSONAL = 2;
        public static final int AUTH_UNKNOWN = 0;
        public static final int ROAM_TYPE_DBDC = 3;
        public static final int ROAM_TYPE_ENTERPRISE = 2;
        public static final int ROAM_TYPE_NONE = 1;
        public static final int ROAM_TYPE_UNKNOWN = 0;
        public static final int ROUTER_TECH_A = 1;
        public static final int ROUTER_TECH_AC = 5;
        public static final int ROUTER_TECH_B = 2;
        public static final int ROUTER_TECH_G = 3;
        public static final int ROUTER_TECH_N = 4;
        public static final int ROUTER_TECH_OTHER = 6;
        public static final int ROUTER_TECH_UNKNOWN = 0;
        private static volatile RouterFingerPrint[] _emptyArray;
        public int authentication;
        public int channelInfo;
        public int dtim;
        public boolean hidden;
        public boolean passpoint;
        public int roamType;
        public int routerTechnology;
        public boolean supportsIpv6;

        public static RouterFingerPrint[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new RouterFingerPrint[0];
                    }
                }
            }
            return _emptyArray;
        }

        public RouterFingerPrint() {
            clear();
        }

        public RouterFingerPrint clear() {
            this.roamType = 0;
            this.channelInfo = 0;
            this.dtim = 0;
            this.authentication = 0;
            this.hidden = false;
            this.routerTechnology = 0;
            this.supportsIpv6 = false;
            this.passpoint = false;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.roamType;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.channelInfo;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.dtim;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.authentication;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            boolean z = this.hidden;
            if (z) {
                output.writeBool(5, z);
            }
            i = this.routerTechnology;
            if (i != 0) {
                output.writeInt32(6, i);
            }
            z = this.supportsIpv6;
            if (z) {
                output.writeBool(7, z);
            }
            z = this.passpoint;
            if (z) {
                output.writeBool(8, z);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.roamType;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.channelInfo;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.dtim;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.authentication;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            boolean z = this.hidden;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(5, z);
            }
            i = this.routerTechnology;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(6, i);
            }
            z = this.supportsIpv6;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(7, z);
            }
            z = this.passpoint;
            if (z) {
                return size + CodedOutputByteBufferNano.computeBoolSize(8, z);
            }
            return size;
        }

        public RouterFingerPrint mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                int value;
                if (tag == 8) {
                    value = input.readInt32();
                    if (value == 0 || value == 1 || value == 2 || value == 3) {
                        this.roamType = value;
                    }
                } else if (tag == 16) {
                    this.channelInfo = input.readInt32();
                } else if (tag == 24) {
                    this.dtim = input.readInt32();
                } else if (tag == 32) {
                    value = input.readInt32();
                    if (value == 0 || value == 1 || value == 2 || value == 3) {
                        this.authentication = value;
                    }
                } else if (tag != 40) {
                    if (tag == 48) {
                        value = input.readInt32();
                        switch (value) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                                this.routerTechnology = value;
                                break;
                            default:
                                break;
                        }
                    } else if (tag == 56) {
                        this.supportsIpv6 = input.readBool();
                    } else if (tag == 64) {
                        this.passpoint = input.readBool();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    this.hidden = input.readBool();
                }
            }
        }

        public static RouterFingerPrint parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (RouterFingerPrint) MessageNano.mergeFrom(new RouterFingerPrint(), data);
        }

        public static RouterFingerPrint parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new RouterFingerPrint().mergeFrom(input);
        }
    }

    public static final class RssiPollCount extends MessageNano {
        private static volatile RssiPollCount[] _emptyArray;
        public int count;
        public int frequency;
        public int rssi;

        public static RssiPollCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new RssiPollCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public RssiPollCount() {
            clear();
        }

        public RssiPollCount clear() {
            this.rssi = 0;
            this.count = 0;
            this.frequency = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.rssi;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.count;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.frequency;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.rssi;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.count;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.frequency;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            return size;
        }

        public RssiPollCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.rssi = input.readInt32();
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (tag == 24) {
                    this.frequency = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static RssiPollCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (RssiPollCount) MessageNano.mergeFrom(new RssiPollCount(), data);
        }

        public static RssiPollCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new RssiPollCount().mergeFrom(input);
        }
    }

    public static final class SoftApConnectedClientsEvent extends MessageNano {
        public static final int BANDWIDTH_160 = 6;
        public static final int BANDWIDTH_20 = 2;
        public static final int BANDWIDTH_20_NOHT = 1;
        public static final int BANDWIDTH_40 = 3;
        public static final int BANDWIDTH_80 = 4;
        public static final int BANDWIDTH_80P80 = 5;
        public static final int BANDWIDTH_INVALID = 0;
        public static final int NUM_CLIENTS_CHANGED = 2;
        public static final int SOFT_AP_DOWN = 1;
        public static final int SOFT_AP_UP = 0;
        private static volatile SoftApConnectedClientsEvent[] _emptyArray;
        public int channelBandwidth;
        public int channelFrequency;
        public int eventType;
        public int numConnectedClients;
        public long timeStampMillis;

        public static SoftApConnectedClientsEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new SoftApConnectedClientsEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public SoftApConnectedClientsEvent() {
            clear();
        }

        public SoftApConnectedClientsEvent clear() {
            this.eventType = 0;
            this.timeStampMillis = 0;
            this.numConnectedClients = 0;
            this.channelFrequency = 0;
            this.channelBandwidth = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.eventType;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            long j = this.timeStampMillis;
            if (j != 0) {
                output.writeInt64(2, j);
            }
            i = this.numConnectedClients;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.channelFrequency;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            i = this.channelBandwidth;
            if (i != 0) {
                output.writeInt32(5, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.eventType;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            long j = this.timeStampMillis;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(2, j);
            }
            i = this.numConnectedClients;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.channelFrequency;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            i = this.channelBandwidth;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(5, i);
            }
            return size;
        }

        public SoftApConnectedClientsEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                int value;
                if (tag == 8) {
                    value = input.readInt32();
                    if (value == 0 || value == 1 || value == 2) {
                        this.eventType = value;
                    }
                } else if (tag == 16) {
                    this.timeStampMillis = input.readInt64();
                } else if (tag == 24) {
                    this.numConnectedClients = input.readInt32();
                } else if (tag != 32) {
                    if (tag == 40) {
                        value = input.readInt32();
                        switch (value) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                                this.channelBandwidth = value;
                                break;
                            default:
                                break;
                        }
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    this.channelFrequency = input.readInt32();
                }
            }
        }

        public static SoftApConnectedClientsEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (SoftApConnectedClientsEvent) MessageNano.mergeFrom(new SoftApConnectedClientsEvent(), data);
        }

        public static SoftApConnectedClientsEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new SoftApConnectedClientsEvent().mergeFrom(input);
        }
    }

    public static final class SoftApDurationBucket extends MessageNano {
        private static volatile SoftApDurationBucket[] _emptyArray;
        public int bucketSizeSec;
        public int count;
        public int durationSec;

        public static SoftApDurationBucket[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new SoftApDurationBucket[0];
                    }
                }
            }
            return _emptyArray;
        }

        public SoftApDurationBucket() {
            clear();
        }

        public SoftApDurationBucket clear() {
            this.durationSec = 0;
            this.bucketSizeSec = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.durationSec;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.bucketSizeSec;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.count;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.durationSec;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.bucketSizeSec;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.count;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            return size;
        }

        public SoftApDurationBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.durationSec = input.readInt32();
                } else if (tag == 16) {
                    this.bucketSizeSec = input.readInt32();
                } else if (tag == 24) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static SoftApDurationBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (SoftApDurationBucket) MessageNano.mergeFrom(new SoftApDurationBucket(), data);
        }

        public static SoftApDurationBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new SoftApDurationBucket().mergeFrom(input);
        }
    }

    public static final class SoftApReturnCodeCount extends MessageNano {
        public static final int SOFT_AP_FAILED_GENERAL_ERROR = 2;
        public static final int SOFT_AP_FAILED_NO_CHANNEL = 3;
        public static final int SOFT_AP_RETURN_CODE_UNKNOWN = 0;
        public static final int SOFT_AP_STARTED_SUCCESSFULLY = 1;
        private static volatile SoftApReturnCodeCount[] _emptyArray;
        public int count;
        public int returnCode;
        public int startResult;

        public static SoftApReturnCodeCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new SoftApReturnCodeCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public SoftApReturnCodeCount() {
            clear();
        }

        public SoftApReturnCodeCount clear() {
            this.returnCode = 0;
            this.count = 0;
            this.startResult = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.returnCode;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.count;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.startResult;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.returnCode;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.count;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.startResult;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            return size;
        }

        public SoftApReturnCodeCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.returnCode = input.readInt32();
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (tag == 24) {
                    int value = input.readInt32();
                    if (value == 0 || value == 1 || value == 2 || value == 3) {
                        this.startResult = value;
                    }
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static SoftApReturnCodeCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (SoftApReturnCodeCount) MessageNano.mergeFrom(new SoftApReturnCodeCount(), data);
        }

        public static SoftApReturnCodeCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new SoftApReturnCodeCount().mergeFrom(input);
        }
    }

    public static final class StaEvent extends MessageNano {
        public static final int AUTH_FAILURE_EAP_FAILURE = 4;
        public static final int AUTH_FAILURE_NONE = 1;
        public static final int AUTH_FAILURE_TIMEOUT = 2;
        public static final int AUTH_FAILURE_UNKNOWN = 0;
        public static final int AUTH_FAILURE_WRONG_PSWD = 3;
        public static final int DISCONNECT_API = 1;
        public static final int DISCONNECT_GENERIC = 2;
        public static final int DISCONNECT_P2P_DISCONNECT_WIFI_REQUEST = 5;
        public static final int DISCONNECT_RESET_SIM_NETWORKS = 6;
        public static final int DISCONNECT_ROAM_WATCHDOG_TIMER = 4;
        public static final int DISCONNECT_UNKNOWN = 0;
        public static final int DISCONNECT_UNWANTED = 3;
        public static final int STATE_ASSOCIATED = 6;
        public static final int STATE_ASSOCIATING = 5;
        public static final int STATE_AUTHENTICATING = 4;
        public static final int STATE_COMPLETED = 9;
        public static final int STATE_DISCONNECTED = 0;
        public static final int STATE_DORMANT = 10;
        public static final int STATE_FOUR_WAY_HANDSHAKE = 7;
        public static final int STATE_GROUP_HANDSHAKE = 8;
        public static final int STATE_INACTIVE = 2;
        public static final int STATE_INTERFACE_DISABLED = 1;
        public static final int STATE_INVALID = 12;
        public static final int STATE_SCANNING = 3;
        public static final int STATE_UNINITIALIZED = 11;
        public static final int TYPE_ASSOCIATION_REJECTION_EVENT = 1;
        public static final int TYPE_AUTHENTICATION_FAILURE_EVENT = 2;
        public static final int TYPE_CMD_ASSOCIATED_BSSID = 6;
        public static final int TYPE_CMD_IP_CONFIGURATION_LOST = 8;
        public static final int TYPE_CMD_IP_CONFIGURATION_SUCCESSFUL = 7;
        public static final int TYPE_CMD_IP_REACHABILITY_LOST = 9;
        public static final int TYPE_CMD_START_CONNECT = 11;
        public static final int TYPE_CMD_START_ROAM = 12;
        public static final int TYPE_CMD_TARGET_BSSID = 10;
        public static final int TYPE_CONNECT_NETWORK = 13;
        public static final int TYPE_FRAMEWORK_DISCONNECT = 15;
        public static final int TYPE_LINK_PROBE = 21;
        public static final int TYPE_MAC_CHANGE = 17;
        public static final int TYPE_NETWORK_AGENT_VALID_NETWORK = 14;
        public static final int TYPE_NETWORK_CONNECTION_EVENT = 3;
        public static final int TYPE_NETWORK_DISCONNECTION_EVENT = 4;
        public static final int TYPE_SCORE_BREACH = 16;
        public static final int TYPE_SUPPLICANT_STATE_CHANGE_EVENT = 5;
        public static final int TYPE_UNKNOWN = 0;
        public static final int TYPE_WIFI_DISABLED = 19;
        public static final int TYPE_WIFI_ENABLED = 18;
        public static final int TYPE_WIFI_USABILITY_SCORE_BREACH = 20;
        private static volatile StaEvent[] _emptyArray;
        public boolean associationTimedOut;
        public int authFailureReason;
        public ConfigInfo configInfo;
        public int frameworkDisconnectReason;
        public int lastFreq;
        public int lastLinkSpeed;
        public int lastPredictionHorizonSec;
        public int lastRssi;
        public int lastScore;
        public int lastWifiUsabilityScore;
        public int linkProbeFailureReason;
        public int linkProbeSuccessElapsedTimeMs;
        public boolean linkProbeWasSuccess;
        public boolean localGen;
        public int reason;
        public long startTimeMillis;
        public int status;
        public int supplicantStateChangesBitmask;
        public int type;

        public static final class ConfigInfo extends MessageNano {
            private static volatile ConfigInfo[] _emptyArray;
            public int allowedAuthAlgorithms;
            public int allowedGroupCiphers;
            public int allowedKeyManagement;
            public int allowedPairwiseCiphers;
            public int allowedProtocols;
            public boolean hasEverConnected;
            public boolean hiddenSsid;
            public boolean isEphemeral;
            public boolean isPasspoint;
            public int scanFreq;
            public int scanRssi;

            public static ConfigInfo[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new ConfigInfo[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public ConfigInfo() {
                clear();
            }

            public ConfigInfo clear() {
                this.allowedKeyManagement = 0;
                this.allowedProtocols = 0;
                this.allowedAuthAlgorithms = 0;
                this.allowedPairwiseCiphers = 0;
                this.allowedGroupCiphers = 0;
                this.hiddenSsid = false;
                this.isPasspoint = false;
                this.isEphemeral = false;
                this.hasEverConnected = false;
                this.scanRssi = -127;
                this.scanFreq = -1;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                int i = this.allowedKeyManagement;
                if (i != 0) {
                    output.writeUInt32(1, i);
                }
                i = this.allowedProtocols;
                if (i != 0) {
                    output.writeUInt32(2, i);
                }
                i = this.allowedAuthAlgorithms;
                if (i != 0) {
                    output.writeUInt32(3, i);
                }
                i = this.allowedPairwiseCiphers;
                if (i != 0) {
                    output.writeUInt32(4, i);
                }
                i = this.allowedGroupCiphers;
                if (i != 0) {
                    output.writeUInt32(5, i);
                }
                boolean z = this.hiddenSsid;
                if (z) {
                    output.writeBool(6, z);
                }
                z = this.isPasspoint;
                if (z) {
                    output.writeBool(7, z);
                }
                z = this.isEphemeral;
                if (z) {
                    output.writeBool(8, z);
                }
                z = this.hasEverConnected;
                if (z) {
                    output.writeBool(9, z);
                }
                i = this.scanRssi;
                if (i != -127) {
                    output.writeInt32(10, i);
                }
                i = this.scanFreq;
                if (i != -1) {
                    output.writeInt32(11, i);
                }
                super.writeTo(output);
            }

            /* Access modifiers changed, original: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                int i = this.allowedKeyManagement;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeUInt32Size(1, i);
                }
                i = this.allowedProtocols;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeUInt32Size(2, i);
                }
                i = this.allowedAuthAlgorithms;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeUInt32Size(3, i);
                }
                i = this.allowedPairwiseCiphers;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeUInt32Size(4, i);
                }
                i = this.allowedGroupCiphers;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeUInt32Size(5, i);
                }
                boolean z = this.hiddenSsid;
                if (z) {
                    size += CodedOutputByteBufferNano.computeBoolSize(6, z);
                }
                z = this.isPasspoint;
                if (z) {
                    size += CodedOutputByteBufferNano.computeBoolSize(7, z);
                }
                z = this.isEphemeral;
                if (z) {
                    size += CodedOutputByteBufferNano.computeBoolSize(8, z);
                }
                z = this.hasEverConnected;
                if (z) {
                    size += CodedOutputByteBufferNano.computeBoolSize(9, z);
                }
                i = this.scanRssi;
                if (i != -127) {
                    size += CodedOutputByteBufferNano.computeInt32Size(10, i);
                }
                i = this.scanFreq;
                if (i != -1) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(11, i);
                }
                return size;
            }

            public ConfigInfo mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            return this;
                        case 8:
                            this.allowedKeyManagement = input.readUInt32();
                            break;
                        case 16:
                            this.allowedProtocols = input.readUInt32();
                            break;
                        case 24:
                            this.allowedAuthAlgorithms = input.readUInt32();
                            break;
                        case 32:
                            this.allowedPairwiseCiphers = input.readUInt32();
                            break;
                        case 40:
                            this.allowedGroupCiphers = input.readUInt32();
                            break;
                        case 48:
                            this.hiddenSsid = input.readBool();
                            break;
                        case 56:
                            this.isPasspoint = input.readBool();
                            break;
                        case 64:
                            this.isEphemeral = input.readBool();
                            break;
                        case 72:
                            this.hasEverConnected = input.readBool();
                            break;
                        case 80:
                            this.scanRssi = input.readInt32();
                            break;
                        case 88:
                            this.scanFreq = input.readInt32();
                            break;
                        default:
                            if (WireFormatNano.parseUnknownField(input, tag)) {
                                break;
                            }
                            return this;
                    }
                }
            }

            public static ConfigInfo parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (ConfigInfo) MessageNano.mergeFrom(new ConfigInfo(), data);
            }

            public static ConfigInfo parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new ConfigInfo().mergeFrom(input);
            }
        }

        public static StaEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new StaEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public StaEvent() {
            clear();
        }

        public StaEvent clear() {
            this.type = 0;
            this.reason = -1;
            this.status = -1;
            this.localGen = false;
            this.configInfo = null;
            this.lastRssi = -127;
            this.lastLinkSpeed = -1;
            this.lastFreq = -1;
            this.supplicantStateChangesBitmask = 0;
            this.startTimeMillis = 0;
            this.frameworkDisconnectReason = 0;
            this.associationTimedOut = false;
            this.authFailureReason = 0;
            this.lastScore = -1;
            this.lastWifiUsabilityScore = -1;
            this.lastPredictionHorizonSec = -1;
            this.linkProbeWasSuccess = false;
            this.linkProbeSuccessElapsedTimeMs = 0;
            this.linkProbeFailureReason = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.type;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.reason;
            if (i != -1) {
                output.writeInt32(2, i);
            }
            i = this.status;
            if (i != -1) {
                output.writeInt32(3, i);
            }
            boolean z = this.localGen;
            if (z) {
                output.writeBool(4, z);
            }
            ConfigInfo configInfo = this.configInfo;
            if (configInfo != null) {
                output.writeMessage(5, configInfo);
            }
            i = this.lastRssi;
            if (i != -127) {
                output.writeInt32(6, i);
            }
            i = this.lastLinkSpeed;
            if (i != -1) {
                output.writeInt32(7, i);
            }
            i = this.lastFreq;
            if (i != -1) {
                output.writeInt32(8, i);
            }
            i = this.supplicantStateChangesBitmask;
            if (i != 0) {
                output.writeUInt32(9, i);
            }
            long j = this.startTimeMillis;
            if (j != 0) {
                output.writeInt64(10, j);
            }
            i = this.frameworkDisconnectReason;
            if (i != 0) {
                output.writeInt32(11, i);
            }
            z = this.associationTimedOut;
            if (z) {
                output.writeBool(12, z);
            }
            i = this.authFailureReason;
            if (i != 0) {
                output.writeInt32(13, i);
            }
            i = this.lastScore;
            if (i != -1) {
                output.writeInt32(14, i);
            }
            i = this.lastWifiUsabilityScore;
            if (i != -1) {
                output.writeInt32(15, i);
            }
            i = this.lastPredictionHorizonSec;
            if (i != -1) {
                output.writeInt32(16, i);
            }
            z = this.linkProbeWasSuccess;
            if (z) {
                output.writeBool(17, z);
            }
            i = this.linkProbeSuccessElapsedTimeMs;
            if (i != 0) {
                output.writeInt32(18, i);
            }
            i = this.linkProbeFailureReason;
            if (i != 0) {
                output.writeInt32(19, i);
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
            i = this.reason;
            if (i != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.status;
            if (i != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            boolean z = this.localGen;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(4, z);
            }
            ConfigInfo configInfo = this.configInfo;
            if (configInfo != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(5, configInfo);
            }
            i = this.lastRssi;
            if (i != -127) {
                size += CodedOutputByteBufferNano.computeInt32Size(6, i);
            }
            i = this.lastLinkSpeed;
            if (i != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(7, i);
            }
            i = this.lastFreq;
            if (i != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(8, i);
            }
            i = this.supplicantStateChangesBitmask;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeUInt32Size(9, i);
            }
            long j = this.startTimeMillis;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(10, j);
            }
            i = this.frameworkDisconnectReason;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(11, i);
            }
            z = this.associationTimedOut;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(12, z);
            }
            i = this.authFailureReason;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(13, i);
            }
            i = this.lastScore;
            if (i != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(14, i);
            }
            i = this.lastWifiUsabilityScore;
            if (i != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(15, i);
            }
            i = this.lastPredictionHorizonSec;
            if (i != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(16, i);
            }
            z = this.linkProbeWasSuccess;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(17, z);
            }
            i = this.linkProbeSuccessElapsedTimeMs;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(18, i);
            }
            i = this.linkProbeFailureReason;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(19, i);
            }
            return size;
        }

        public StaEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                int value;
                int value2;
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        value = input.readInt32();
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
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                            case 14:
                            case 15:
                            case 16:
                            case 17:
                            case 18:
                            case 19:
                            case 20:
                            case 21:
                                this.type = value;
                                break;
                            default:
                                break;
                        }
                    case 16:
                        this.reason = input.readInt32();
                        break;
                    case 24:
                        this.status = input.readInt32();
                        break;
                    case 32:
                        this.localGen = input.readBool();
                        break;
                    case 42:
                        if (this.configInfo == null) {
                            this.configInfo = new ConfigInfo();
                        }
                        input.readMessage(this.configInfo);
                        break;
                    case 48:
                        this.lastRssi = input.readInt32();
                        break;
                    case 56:
                        this.lastLinkSpeed = input.readInt32();
                        break;
                    case 64:
                        this.lastFreq = input.readInt32();
                        break;
                    case 72:
                        this.supplicantStateChangesBitmask = input.readUInt32();
                        break;
                    case 80:
                        this.startTimeMillis = input.readInt64();
                        break;
                    case 88:
                        value = input.readInt32();
                        switch (value) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                                this.frameworkDisconnectReason = value;
                                break;
                            default:
                                break;
                        }
                    case 96:
                        this.associationTimedOut = input.readBool();
                        break;
                    case 104:
                        value2 = input.readInt32();
                        if (value2 != 0 && value2 != 1 && value2 != 2 && value2 != 3 && value2 != 4) {
                            break;
                        }
                        this.authFailureReason = value2;
                        break;
                        break;
                    case 112:
                        this.lastScore = input.readInt32();
                        break;
                    case 120:
                        this.lastWifiUsabilityScore = input.readInt32();
                        break;
                    case 128:
                        this.lastPredictionHorizonSec = input.readInt32();
                        break;
                    case 136:
                        this.linkProbeWasSuccess = input.readBool();
                        break;
                    case 144:
                        this.linkProbeSuccessElapsedTimeMs = input.readInt32();
                        break;
                    case 152:
                        value2 = input.readInt32();
                        if (value2 != 0 && value2 != 1 && value2 != 2 && value2 != 3 && value2 != 4) {
                            break;
                        }
                        this.linkProbeFailureReason = value2;
                        break;
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        }
                        return this;
                }
            }
        }

        public static StaEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (StaEvent) MessageNano.mergeFrom(new StaEvent(), data);
        }

        public static StaEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new StaEvent().mergeFrom(input);
        }
    }

    public static final class WifiAwareLog extends MessageNano {
        public static final int ALREADY_ENABLED = 11;
        public static final int FOLLOWUP_TX_QUEUE_FULL = 12;
        public static final int INTERNAL_FAILURE = 2;
        public static final int INVALID_ARGS = 6;
        public static final int INVALID_NDP_ID = 8;
        public static final int INVALID_PEER_ID = 7;
        public static final int INVALID_SESSION_ID = 4;
        public static final int NAN_NOT_ALLOWED = 9;
        public static final int NO_OTA_ACK = 10;
        public static final int NO_RESOURCES_AVAILABLE = 5;
        public static final int PROTOCOL_FAILURE = 3;
        public static final int SUCCESS = 1;
        public static final int UNKNOWN = 0;
        public static final int UNKNOWN_HAL_STATUS = 14;
        public static final int UNSUPPORTED_CONCURRENCY_NAN_DISABLED = 13;
        private static volatile WifiAwareLog[] _emptyArray;
        public long availableTimeMs;
        public long enabledTimeMs;
        public HistogramBucket[] histogramAttachDurationMs;
        public NanStatusHistogramBucket[] histogramAttachSessionStatus;
        public HistogramBucket[] histogramAwareAvailableDurationMs;
        public HistogramBucket[] histogramAwareEnabledDurationMs;
        public HistogramBucket[] histogramNdpCreationTimeMs;
        public HistogramBucket[] histogramNdpSessionDataUsageMb;
        public HistogramBucket[] histogramNdpSessionDurationMs;
        public HistogramBucket[] histogramPublishSessionDurationMs;
        public NanStatusHistogramBucket[] histogramPublishStatus;
        public NanStatusHistogramBucket[] histogramRequestNdpOobStatus;
        public NanStatusHistogramBucket[] histogramRequestNdpStatus;
        public HistogramBucket[] histogramSubscribeGeofenceMax;
        public HistogramBucket[] histogramSubscribeGeofenceMin;
        public HistogramBucket[] histogramSubscribeSessionDurationMs;
        public NanStatusHistogramBucket[] histogramSubscribeStatus;
        public int maxConcurrentAttachSessionsInApp;
        public int maxConcurrentDiscoverySessionsInApp;
        public int maxConcurrentDiscoverySessionsInSystem;
        public int maxConcurrentNdiInApp;
        public int maxConcurrentNdiInSystem;
        public int maxConcurrentNdpInApp;
        public int maxConcurrentNdpInSystem;
        public int maxConcurrentNdpPerNdi;
        public int maxConcurrentPublishInApp;
        public int maxConcurrentPublishInSystem;
        public int maxConcurrentPublishWithRangingInApp;
        public int maxConcurrentPublishWithRangingInSystem;
        public int maxConcurrentSecureNdpInApp;
        public int maxConcurrentSecureNdpInSystem;
        public int maxConcurrentSubscribeInApp;
        public int maxConcurrentSubscribeInSystem;
        public int maxConcurrentSubscribeWithRangingInApp;
        public int maxConcurrentSubscribeWithRangingInSystem;
        public long ndpCreationTimeMsMax;
        public long ndpCreationTimeMsMin;
        public long ndpCreationTimeMsNumSamples;
        public long ndpCreationTimeMsSum;
        public long ndpCreationTimeMsSumOfSq;
        public int numApps;
        public int numAppsUsingIdentityCallback;
        public int numAppsWithDiscoverySessionFailureOutOfResources;
        public int numMatchesWithRanging;
        public int numMatchesWithoutRangingForRangingEnabledSubscribes;
        public int numSubscribesWithRanging;

        public static final class HistogramBucket extends MessageNano {
            private static volatile HistogramBucket[] _emptyArray;
            public int count;
            public long end;
            public long start;

            public static HistogramBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new HistogramBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public HistogramBucket() {
                clear();
            }

            public HistogramBucket clear() {
                this.start = 0;
                this.end = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                long j = this.start;
                if (j != 0) {
                    output.writeInt64(1, j);
                }
                j = this.end;
                if (j != 0) {
                    output.writeInt64(2, j);
                }
                int i = this.count;
                if (i != 0) {
                    output.writeInt32(3, i);
                }
                super.writeTo(output);
            }

            /* Access modifiers changed, original: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                long j = this.start;
                if (j != 0) {
                    size += CodedOutputByteBufferNano.computeInt64Size(1, j);
                }
                j = this.end;
                if (j != 0) {
                    size += CodedOutputByteBufferNano.computeInt64Size(2, j);
                }
                int i = this.count;
                if (i != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(3, i);
                }
                return size;
            }

            public HistogramBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        this.start = input.readInt64();
                    } else if (tag == 16) {
                        this.end = input.readInt64();
                    } else if (tag == 24) {
                        this.count = input.readInt32();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static HistogramBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (HistogramBucket) MessageNano.mergeFrom(new HistogramBucket(), data);
            }

            public static HistogramBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new HistogramBucket().mergeFrom(input);
            }
        }

        public static final class NanStatusHistogramBucket extends MessageNano {
            private static volatile NanStatusHistogramBucket[] _emptyArray;
            public int count;
            public int nanStatusType;

            public static NanStatusHistogramBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new NanStatusHistogramBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public NanStatusHistogramBucket() {
                clear();
            }

            public NanStatusHistogramBucket clear() {
                this.nanStatusType = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                int i = this.nanStatusType;
                if (i != 0) {
                    output.writeInt32(1, i);
                }
                i = this.count;
                if (i != 0) {
                    output.writeInt32(2, i);
                }
                super.writeTo(output);
            }

            /* Access modifiers changed, original: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                int i = this.nanStatusType;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, i);
                }
                i = this.count;
                if (i != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
                }
                return size;
            }

            public NanStatusHistogramBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag != 0) {
                        if (tag == 8) {
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
                                case 10:
                                case 11:
                                case 12:
                                case 13:
                                case 14:
                                    this.nanStatusType = value;
                                    break;
                                default:
                                    break;
                            }
                        } else if (tag == 16) {
                            this.count = input.readInt32();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        return this;
                    }
                }
            }

            public static NanStatusHistogramBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (NanStatusHistogramBucket) MessageNano.mergeFrom(new NanStatusHistogramBucket(), data);
            }

            public static NanStatusHistogramBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new NanStatusHistogramBucket().mergeFrom(input);
            }
        }

        public static WifiAwareLog[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiAwareLog[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiAwareLog() {
            clear();
        }

        public WifiAwareLog clear() {
            this.numApps = 0;
            this.numAppsUsingIdentityCallback = 0;
            this.maxConcurrentAttachSessionsInApp = 0;
            this.histogramAttachSessionStatus = NanStatusHistogramBucket.emptyArray();
            this.maxConcurrentPublishInApp = 0;
            this.maxConcurrentSubscribeInApp = 0;
            this.maxConcurrentDiscoverySessionsInApp = 0;
            this.maxConcurrentPublishInSystem = 0;
            this.maxConcurrentSubscribeInSystem = 0;
            this.maxConcurrentDiscoverySessionsInSystem = 0;
            this.histogramPublishStatus = NanStatusHistogramBucket.emptyArray();
            this.histogramSubscribeStatus = NanStatusHistogramBucket.emptyArray();
            this.numAppsWithDiscoverySessionFailureOutOfResources = 0;
            this.histogramRequestNdpStatus = NanStatusHistogramBucket.emptyArray();
            this.histogramRequestNdpOobStatus = NanStatusHistogramBucket.emptyArray();
            this.maxConcurrentNdiInApp = 0;
            this.maxConcurrentNdiInSystem = 0;
            this.maxConcurrentNdpInApp = 0;
            this.maxConcurrentNdpInSystem = 0;
            this.maxConcurrentSecureNdpInApp = 0;
            this.maxConcurrentSecureNdpInSystem = 0;
            this.maxConcurrentNdpPerNdi = 0;
            this.histogramAwareAvailableDurationMs = HistogramBucket.emptyArray();
            this.histogramAwareEnabledDurationMs = HistogramBucket.emptyArray();
            this.histogramAttachDurationMs = HistogramBucket.emptyArray();
            this.histogramPublishSessionDurationMs = HistogramBucket.emptyArray();
            this.histogramSubscribeSessionDurationMs = HistogramBucket.emptyArray();
            this.histogramNdpSessionDurationMs = HistogramBucket.emptyArray();
            this.histogramNdpSessionDataUsageMb = HistogramBucket.emptyArray();
            this.histogramNdpCreationTimeMs = HistogramBucket.emptyArray();
            this.ndpCreationTimeMsMin = 0;
            this.ndpCreationTimeMsMax = 0;
            this.ndpCreationTimeMsSum = 0;
            this.ndpCreationTimeMsSumOfSq = 0;
            this.ndpCreationTimeMsNumSamples = 0;
            this.availableTimeMs = 0;
            this.enabledTimeMs = 0;
            this.maxConcurrentPublishWithRangingInApp = 0;
            this.maxConcurrentSubscribeWithRangingInApp = 0;
            this.maxConcurrentPublishWithRangingInSystem = 0;
            this.maxConcurrentSubscribeWithRangingInSystem = 0;
            this.histogramSubscribeGeofenceMin = HistogramBucket.emptyArray();
            this.histogramSubscribeGeofenceMax = HistogramBucket.emptyArray();
            this.numSubscribesWithRanging = 0;
            this.numMatchesWithRanging = 0;
            this.numMatchesWithoutRangingForRangingEnabledSubscribes = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            NanStatusHistogramBucket element;
            NanStatusHistogramBucket[] nanStatusHistogramBucketArr;
            HistogramBucket element2;
            HistogramBucket[] histogramBucketArr;
            int i = this.numApps;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.numAppsUsingIdentityCallback;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.maxConcurrentAttachSessionsInApp;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            NanStatusHistogramBucket[] nanStatusHistogramBucketArr2 = this.histogramAttachSessionStatus;
            if (nanStatusHistogramBucketArr2 != null && nanStatusHistogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    element = this.histogramAttachSessionStatus;
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
            i = this.maxConcurrentPublishInApp;
            if (i != 0) {
                output.writeInt32(5, i);
            }
            i = this.maxConcurrentSubscribeInApp;
            if (i != 0) {
                output.writeInt32(6, i);
            }
            i = this.maxConcurrentDiscoverySessionsInApp;
            if (i != 0) {
                output.writeInt32(7, i);
            }
            i = this.maxConcurrentPublishInSystem;
            if (i != 0) {
                output.writeInt32(8, i);
            }
            i = this.maxConcurrentSubscribeInSystem;
            if (i != 0) {
                output.writeInt32(9, i);
            }
            i = this.maxConcurrentDiscoverySessionsInSystem;
            if (i != 0) {
                output.writeInt32(10, i);
            }
            nanStatusHistogramBucketArr2 = this.histogramPublishStatus;
            if (nanStatusHistogramBucketArr2 != null && nanStatusHistogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    nanStatusHistogramBucketArr = this.histogramPublishStatus;
                    if (i >= nanStatusHistogramBucketArr.length) {
                        break;
                    }
                    element = nanStatusHistogramBucketArr[i];
                    if (element != null) {
                        output.writeMessage(11, element);
                    }
                    i++;
                }
            }
            nanStatusHistogramBucketArr2 = this.histogramSubscribeStatus;
            if (nanStatusHistogramBucketArr2 != null && nanStatusHistogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    nanStatusHistogramBucketArr = this.histogramSubscribeStatus;
                    if (i >= nanStatusHistogramBucketArr.length) {
                        break;
                    }
                    element = nanStatusHistogramBucketArr[i];
                    if (element != null) {
                        output.writeMessage(12, element);
                    }
                    i++;
                }
            }
            i = this.numAppsWithDiscoverySessionFailureOutOfResources;
            if (i != 0) {
                output.writeInt32(13, i);
            }
            nanStatusHistogramBucketArr2 = this.histogramRequestNdpStatus;
            if (nanStatusHistogramBucketArr2 != null && nanStatusHistogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    nanStatusHistogramBucketArr = this.histogramRequestNdpStatus;
                    if (i >= nanStatusHistogramBucketArr.length) {
                        break;
                    }
                    element = nanStatusHistogramBucketArr[i];
                    if (element != null) {
                        output.writeMessage(14, element);
                    }
                    i++;
                }
            }
            nanStatusHistogramBucketArr2 = this.histogramRequestNdpOobStatus;
            if (nanStatusHistogramBucketArr2 != null && nanStatusHistogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    nanStatusHistogramBucketArr = this.histogramRequestNdpOobStatus;
                    if (i >= nanStatusHistogramBucketArr.length) {
                        break;
                    }
                    element = nanStatusHistogramBucketArr[i];
                    if (element != null) {
                        output.writeMessage(15, element);
                    }
                    i++;
                }
            }
            i = this.maxConcurrentNdiInApp;
            if (i != 0) {
                output.writeInt32(19, i);
            }
            i = this.maxConcurrentNdiInSystem;
            if (i != 0) {
                output.writeInt32(20, i);
            }
            i = this.maxConcurrentNdpInApp;
            if (i != 0) {
                output.writeInt32(21, i);
            }
            i = this.maxConcurrentNdpInSystem;
            if (i != 0) {
                output.writeInt32(22, i);
            }
            i = this.maxConcurrentSecureNdpInApp;
            if (i != 0) {
                output.writeInt32(23, i);
            }
            i = this.maxConcurrentSecureNdpInSystem;
            if (i != 0) {
                output.writeInt32(24, i);
            }
            i = this.maxConcurrentNdpPerNdi;
            if (i != 0) {
                output.writeInt32(25, i);
            }
            HistogramBucket[] histogramBucketArr2 = this.histogramAwareAvailableDurationMs;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    element2 = this.histogramAwareAvailableDurationMs;
                    if (i >= element2.length) {
                        break;
                    }
                    element2 = element2[i];
                    if (element2 != null) {
                        output.writeMessage(26, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramAwareEnabledDurationMs;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramAwareEnabledDurationMs;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        output.writeMessage(27, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramAttachDurationMs;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramAttachDurationMs;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        output.writeMessage(28, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramPublishSessionDurationMs;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramPublishSessionDurationMs;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        output.writeMessage(29, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramSubscribeSessionDurationMs;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramSubscribeSessionDurationMs;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        output.writeMessage(30, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramNdpSessionDurationMs;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramNdpSessionDurationMs;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        output.writeMessage(31, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramNdpSessionDataUsageMb;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramNdpSessionDataUsageMb;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        output.writeMessage(32, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramNdpCreationTimeMs;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramNdpCreationTimeMs;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        output.writeMessage(33, element2);
                    }
                    i++;
                }
            }
            long j = this.ndpCreationTimeMsMin;
            if (j != 0) {
                output.writeInt64(34, j);
            }
            j = this.ndpCreationTimeMsMax;
            if (j != 0) {
                output.writeInt64(35, j);
            }
            j = this.ndpCreationTimeMsSum;
            if (j != 0) {
                output.writeInt64(36, j);
            }
            j = this.ndpCreationTimeMsSumOfSq;
            if (j != 0) {
                output.writeInt64(37, j);
            }
            j = this.ndpCreationTimeMsNumSamples;
            if (j != 0) {
                output.writeInt64(38, j);
            }
            j = this.availableTimeMs;
            if (j != 0) {
                output.writeInt64(39, j);
            }
            j = this.enabledTimeMs;
            if (j != 0) {
                output.writeInt64(40, j);
            }
            i = this.maxConcurrentPublishWithRangingInApp;
            if (i != 0) {
                output.writeInt32(41, i);
            }
            i = this.maxConcurrentSubscribeWithRangingInApp;
            if (i != 0) {
                output.writeInt32(42, i);
            }
            i = this.maxConcurrentPublishWithRangingInSystem;
            if (i != 0) {
                output.writeInt32(43, i);
            }
            i = this.maxConcurrentSubscribeWithRangingInSystem;
            if (i != 0) {
                output.writeInt32(44, i);
            }
            histogramBucketArr2 = this.histogramSubscribeGeofenceMin;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramSubscribeGeofenceMin;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        output.writeMessage(45, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramSubscribeGeofenceMax;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramSubscribeGeofenceMax;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        output.writeMessage(46, element2);
                    }
                    i++;
                }
            }
            i = this.numSubscribesWithRanging;
            if (i != 0) {
                output.writeInt32(47, i);
            }
            i = this.numMatchesWithRanging;
            if (i != 0) {
                output.writeInt32(48, i);
            }
            i = this.numMatchesWithoutRangingForRangingEnabledSubscribes;
            if (i != 0) {
                output.writeInt32(49, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            NanStatusHistogramBucket element;
            NanStatusHistogramBucket[] nanStatusHistogramBucketArr;
            HistogramBucket element2;
            HistogramBucket[] histogramBucketArr;
            int size = super.computeSerializedSize();
            int i = this.numApps;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.numAppsUsingIdentityCallback;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.maxConcurrentAttachSessionsInApp;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            NanStatusHistogramBucket[] nanStatusHistogramBucketArr2 = this.histogramAttachSessionStatus;
            if (nanStatusHistogramBucketArr2 != null && nanStatusHistogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    element = this.histogramAttachSessionStatus;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(4, element);
                    }
                    i++;
                }
            }
            i = this.maxConcurrentPublishInApp;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(5, i);
            }
            i = this.maxConcurrentSubscribeInApp;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(6, i);
            }
            i = this.maxConcurrentDiscoverySessionsInApp;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(7, i);
            }
            i = this.maxConcurrentPublishInSystem;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(8, i);
            }
            i = this.maxConcurrentSubscribeInSystem;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(9, i);
            }
            i = this.maxConcurrentDiscoverySessionsInSystem;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(10, i);
            }
            nanStatusHistogramBucketArr2 = this.histogramPublishStatus;
            if (nanStatusHistogramBucketArr2 != null && nanStatusHistogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    nanStatusHistogramBucketArr = this.histogramPublishStatus;
                    if (i >= nanStatusHistogramBucketArr.length) {
                        break;
                    }
                    element = nanStatusHistogramBucketArr[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(11, element);
                    }
                    i++;
                }
            }
            nanStatusHistogramBucketArr2 = this.histogramSubscribeStatus;
            if (nanStatusHistogramBucketArr2 != null && nanStatusHistogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    nanStatusHistogramBucketArr = this.histogramSubscribeStatus;
                    if (i >= nanStatusHistogramBucketArr.length) {
                        break;
                    }
                    element = nanStatusHistogramBucketArr[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(12, element);
                    }
                    i++;
                }
            }
            i = this.numAppsWithDiscoverySessionFailureOutOfResources;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(13, i);
            }
            nanStatusHistogramBucketArr2 = this.histogramRequestNdpStatus;
            if (nanStatusHistogramBucketArr2 != null && nanStatusHistogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    nanStatusHistogramBucketArr = this.histogramRequestNdpStatus;
                    if (i >= nanStatusHistogramBucketArr.length) {
                        break;
                    }
                    element = nanStatusHistogramBucketArr[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(14, element);
                    }
                    i++;
                }
            }
            nanStatusHistogramBucketArr2 = this.histogramRequestNdpOobStatus;
            if (nanStatusHistogramBucketArr2 != null && nanStatusHistogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    nanStatusHistogramBucketArr = this.histogramRequestNdpOobStatus;
                    if (i >= nanStatusHistogramBucketArr.length) {
                        break;
                    }
                    element = nanStatusHistogramBucketArr[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(15, element);
                    }
                    i++;
                }
            }
            i = this.maxConcurrentNdiInApp;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(19, i);
            }
            i = this.maxConcurrentNdiInSystem;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(20, i);
            }
            i = this.maxConcurrentNdpInApp;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(21, i);
            }
            i = this.maxConcurrentNdpInSystem;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(22, i);
            }
            i = this.maxConcurrentSecureNdpInApp;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(23, i);
            }
            i = this.maxConcurrentSecureNdpInSystem;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(24, i);
            }
            i = this.maxConcurrentNdpPerNdi;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(25, i);
            }
            HistogramBucket[] histogramBucketArr2 = this.histogramAwareAvailableDurationMs;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    element2 = this.histogramAwareAvailableDurationMs;
                    if (i >= element2.length) {
                        break;
                    }
                    element2 = element2[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(26, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramAwareEnabledDurationMs;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramAwareEnabledDurationMs;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(27, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramAttachDurationMs;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramAttachDurationMs;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(28, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramPublishSessionDurationMs;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramPublishSessionDurationMs;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(29, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramSubscribeSessionDurationMs;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramSubscribeSessionDurationMs;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(30, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramNdpSessionDurationMs;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramNdpSessionDurationMs;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(31, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramNdpSessionDataUsageMb;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramNdpSessionDataUsageMb;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(32, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramNdpCreationTimeMs;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramNdpCreationTimeMs;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(33, element2);
                    }
                    i++;
                }
            }
            long j = this.ndpCreationTimeMsMin;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(34, j);
            }
            j = this.ndpCreationTimeMsMax;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(35, j);
            }
            j = this.ndpCreationTimeMsSum;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(36, j);
            }
            j = this.ndpCreationTimeMsSumOfSq;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(37, j);
            }
            j = this.ndpCreationTimeMsNumSamples;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(38, j);
            }
            j = this.availableTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(39, j);
            }
            j = this.enabledTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(40, j);
            }
            i = this.maxConcurrentPublishWithRangingInApp;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(41, i);
            }
            i = this.maxConcurrentSubscribeWithRangingInApp;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(42, i);
            }
            i = this.maxConcurrentPublishWithRangingInSystem;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(43, i);
            }
            i = this.maxConcurrentSubscribeWithRangingInSystem;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(44, i);
            }
            histogramBucketArr2 = this.histogramSubscribeGeofenceMin;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramSubscribeGeofenceMin;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(45, element2);
                    }
                    i++;
                }
            }
            histogramBucketArr2 = this.histogramSubscribeGeofenceMax;
            if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketArr = this.histogramSubscribeGeofenceMax;
                    if (i >= histogramBucketArr.length) {
                        break;
                    }
                    element2 = histogramBucketArr[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(46, element2);
                    }
                    i++;
                }
            }
            i = this.numSubscribesWithRanging;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(47, i);
            }
            i = this.numMatchesWithRanging;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(48, i);
            }
            i = this.numMatchesWithoutRangingForRangingEnabledSubscribes;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(49, i);
            }
            return size;
        }

        public WifiAwareLog mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                int arrayLength;
                NanStatusHistogramBucket[] nanStatusHistogramBucketArr;
                int i;
                NanStatusHistogramBucket[] newArray;
                HistogramBucket[] histogramBucketArr;
                HistogramBucket[] newArray2;
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        this.numApps = input.readInt32();
                        break;
                    case 16:
                        this.numAppsUsingIdentityCallback = input.readInt32();
                        break;
                    case 24:
                        this.maxConcurrentAttachSessionsInApp = input.readInt32();
                        break;
                    case 34:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                        nanStatusHistogramBucketArr = this.histogramAttachSessionStatus;
                        i = nanStatusHistogramBucketArr == null ? 0 : nanStatusHistogramBucketArr.length;
                        newArray = new NanStatusHistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramAttachSessionStatus, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new NanStatusHistogramBucket();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new NanStatusHistogramBucket();
                        input.readMessage(newArray[i]);
                        this.histogramAttachSessionStatus = newArray;
                        break;
                    case 40:
                        this.maxConcurrentPublishInApp = input.readInt32();
                        break;
                    case 48:
                        this.maxConcurrentSubscribeInApp = input.readInt32();
                        break;
                    case 56:
                        this.maxConcurrentDiscoverySessionsInApp = input.readInt32();
                        break;
                    case 64:
                        this.maxConcurrentPublishInSystem = input.readInt32();
                        break;
                    case 72:
                        this.maxConcurrentSubscribeInSystem = input.readInt32();
                        break;
                    case 80:
                        this.maxConcurrentDiscoverySessionsInSystem = input.readInt32();
                        break;
                    case 90:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 90);
                        nanStatusHistogramBucketArr = this.histogramPublishStatus;
                        i = nanStatusHistogramBucketArr == null ? 0 : nanStatusHistogramBucketArr.length;
                        newArray = new NanStatusHistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramPublishStatus, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new NanStatusHistogramBucket();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new NanStatusHistogramBucket();
                        input.readMessage(newArray[i]);
                        this.histogramPublishStatus = newArray;
                        break;
                    case 98:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 98);
                        nanStatusHistogramBucketArr = this.histogramSubscribeStatus;
                        i = nanStatusHistogramBucketArr == null ? 0 : nanStatusHistogramBucketArr.length;
                        newArray = new NanStatusHistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramSubscribeStatus, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new NanStatusHistogramBucket();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new NanStatusHistogramBucket();
                        input.readMessage(newArray[i]);
                        this.histogramSubscribeStatus = newArray;
                        break;
                    case 104:
                        this.numAppsWithDiscoverySessionFailureOutOfResources = input.readInt32();
                        break;
                    case 114:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 114);
                        nanStatusHistogramBucketArr = this.histogramRequestNdpStatus;
                        i = nanStatusHistogramBucketArr == null ? 0 : nanStatusHistogramBucketArr.length;
                        newArray = new NanStatusHistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramRequestNdpStatus, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new NanStatusHistogramBucket();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new NanStatusHistogramBucket();
                        input.readMessage(newArray[i]);
                        this.histogramRequestNdpStatus = newArray;
                        break;
                    case 122:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 122);
                        nanStatusHistogramBucketArr = this.histogramRequestNdpOobStatus;
                        i = nanStatusHistogramBucketArr == null ? 0 : nanStatusHistogramBucketArr.length;
                        newArray = new NanStatusHistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramRequestNdpOobStatus, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new NanStatusHistogramBucket();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new NanStatusHistogramBucket();
                        input.readMessage(newArray[i]);
                        this.histogramRequestNdpOobStatus = newArray;
                        break;
                    case 152:
                        this.maxConcurrentNdiInApp = input.readInt32();
                        break;
                    case 160:
                        this.maxConcurrentNdiInSystem = input.readInt32();
                        break;
                    case 168:
                        this.maxConcurrentNdpInApp = input.readInt32();
                        break;
                    case 176:
                        this.maxConcurrentNdpInSystem = input.readInt32();
                        break;
                    case 184:
                        this.maxConcurrentSecureNdpInApp = input.readInt32();
                        break;
                    case 192:
                        this.maxConcurrentSecureNdpInSystem = input.readInt32();
                        break;
                    case 200:
                        this.maxConcurrentNdpPerNdi = input.readInt32();
                        break;
                    case 210:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 210);
                        histogramBucketArr = this.histogramAwareAvailableDurationMs;
                        i = histogramBucketArr == null ? 0 : histogramBucketArr.length;
                        newArray2 = new HistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramAwareAvailableDurationMs, 0, newArray2, 0, i);
                        }
                        while (i < newArray2.length - 1) {
                            newArray2[i] = new HistogramBucket();
                            input.readMessage(newArray2[i]);
                            input.readTag();
                            i++;
                        }
                        newArray2[i] = new HistogramBucket();
                        input.readMessage(newArray2[i]);
                        this.histogramAwareAvailableDurationMs = newArray2;
                        break;
                    case 218:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 218);
                        histogramBucketArr = this.histogramAwareEnabledDurationMs;
                        i = histogramBucketArr == null ? 0 : histogramBucketArr.length;
                        newArray2 = new HistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramAwareEnabledDurationMs, 0, newArray2, 0, i);
                        }
                        while (i < newArray2.length - 1) {
                            newArray2[i] = new HistogramBucket();
                            input.readMessage(newArray2[i]);
                            input.readTag();
                            i++;
                        }
                        newArray2[i] = new HistogramBucket();
                        input.readMessage(newArray2[i]);
                        this.histogramAwareEnabledDurationMs = newArray2;
                        break;
                    case 226:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 226);
                        histogramBucketArr = this.histogramAttachDurationMs;
                        i = histogramBucketArr == null ? 0 : histogramBucketArr.length;
                        newArray2 = new HistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramAttachDurationMs, 0, newArray2, 0, i);
                        }
                        while (i < newArray2.length - 1) {
                            newArray2[i] = new HistogramBucket();
                            input.readMessage(newArray2[i]);
                            input.readTag();
                            i++;
                        }
                        newArray2[i] = new HistogramBucket();
                        input.readMessage(newArray2[i]);
                        this.histogramAttachDurationMs = newArray2;
                        break;
                    case 234:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 234);
                        histogramBucketArr = this.histogramPublishSessionDurationMs;
                        i = histogramBucketArr == null ? 0 : histogramBucketArr.length;
                        newArray2 = new HistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramPublishSessionDurationMs, 0, newArray2, 0, i);
                        }
                        while (i < newArray2.length - 1) {
                            newArray2[i] = new HistogramBucket();
                            input.readMessage(newArray2[i]);
                            input.readTag();
                            i++;
                        }
                        newArray2[i] = new HistogramBucket();
                        input.readMessage(newArray2[i]);
                        this.histogramPublishSessionDurationMs = newArray2;
                        break;
                    case 242:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 242);
                        histogramBucketArr = this.histogramSubscribeSessionDurationMs;
                        i = histogramBucketArr == null ? 0 : histogramBucketArr.length;
                        newArray2 = new HistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramSubscribeSessionDurationMs, 0, newArray2, 0, i);
                        }
                        while (i < newArray2.length - 1) {
                            newArray2[i] = new HistogramBucket();
                            input.readMessage(newArray2[i]);
                            input.readTag();
                            i++;
                        }
                        newArray2[i] = new HistogramBucket();
                        input.readMessage(newArray2[i]);
                        this.histogramSubscribeSessionDurationMs = newArray2;
                        break;
                    case 250:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 250);
                        histogramBucketArr = this.histogramNdpSessionDurationMs;
                        i = histogramBucketArr == null ? 0 : histogramBucketArr.length;
                        newArray2 = new HistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramNdpSessionDurationMs, 0, newArray2, 0, i);
                        }
                        while (i < newArray2.length - 1) {
                            newArray2[i] = new HistogramBucket();
                            input.readMessage(newArray2[i]);
                            input.readTag();
                            i++;
                        }
                        newArray2[i] = new HistogramBucket();
                        input.readMessage(newArray2[i]);
                        this.histogramNdpSessionDurationMs = newArray2;
                        break;
                    case 258:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 258);
                        histogramBucketArr = this.histogramNdpSessionDataUsageMb;
                        i = histogramBucketArr == null ? 0 : histogramBucketArr.length;
                        newArray2 = new HistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramNdpSessionDataUsageMb, 0, newArray2, 0, i);
                        }
                        while (i < newArray2.length - 1) {
                            newArray2[i] = new HistogramBucket();
                            input.readMessage(newArray2[i]);
                            input.readTag();
                            i++;
                        }
                        newArray2[i] = new HistogramBucket();
                        input.readMessage(newArray2[i]);
                        this.histogramNdpSessionDataUsageMb = newArray2;
                        break;
                    case 266:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 266);
                        histogramBucketArr = this.histogramNdpCreationTimeMs;
                        i = histogramBucketArr == null ? 0 : histogramBucketArr.length;
                        newArray2 = new HistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramNdpCreationTimeMs, 0, newArray2, 0, i);
                        }
                        while (i < newArray2.length - 1) {
                            newArray2[i] = new HistogramBucket();
                            input.readMessage(newArray2[i]);
                            input.readTag();
                            i++;
                        }
                        newArray2[i] = new HistogramBucket();
                        input.readMessage(newArray2[i]);
                        this.histogramNdpCreationTimeMs = newArray2;
                        break;
                    case 272:
                        this.ndpCreationTimeMsMin = input.readInt64();
                        break;
                    case 280:
                        this.ndpCreationTimeMsMax = input.readInt64();
                        break;
                    case 288:
                        this.ndpCreationTimeMsSum = input.readInt64();
                        break;
                    case 296:
                        this.ndpCreationTimeMsSumOfSq = input.readInt64();
                        break;
                    case 304:
                        this.ndpCreationTimeMsNumSamples = input.readInt64();
                        break;
                    case 312:
                        this.availableTimeMs = input.readInt64();
                        break;
                    case 320:
                        this.enabledTimeMs = input.readInt64();
                        break;
                    case 328:
                        this.maxConcurrentPublishWithRangingInApp = input.readInt32();
                        break;
                    case 336:
                        this.maxConcurrentSubscribeWithRangingInApp = input.readInt32();
                        break;
                    case 344:
                        this.maxConcurrentPublishWithRangingInSystem = input.readInt32();
                        break;
                    case 352:
                        this.maxConcurrentSubscribeWithRangingInSystem = input.readInt32();
                        break;
                    case 362:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 362);
                        histogramBucketArr = this.histogramSubscribeGeofenceMin;
                        i = histogramBucketArr == null ? 0 : histogramBucketArr.length;
                        newArray2 = new HistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramSubscribeGeofenceMin, 0, newArray2, 0, i);
                        }
                        while (i < newArray2.length - 1) {
                            newArray2[i] = new HistogramBucket();
                            input.readMessage(newArray2[i]);
                            input.readTag();
                            i++;
                        }
                        newArray2[i] = new HistogramBucket();
                        input.readMessage(newArray2[i]);
                        this.histogramSubscribeGeofenceMin = newArray2;
                        break;
                    case 370:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 370);
                        histogramBucketArr = this.histogramSubscribeGeofenceMax;
                        i = histogramBucketArr == null ? 0 : histogramBucketArr.length;
                        newArray2 = new HistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramSubscribeGeofenceMax, 0, newArray2, 0, i);
                        }
                        while (i < newArray2.length - 1) {
                            newArray2[i] = new HistogramBucket();
                            input.readMessage(newArray2[i]);
                            input.readTag();
                            i++;
                        }
                        newArray2[i] = new HistogramBucket();
                        input.readMessage(newArray2[i]);
                        this.histogramSubscribeGeofenceMax = newArray2;
                        break;
                    case 376:
                        this.numSubscribesWithRanging = input.readInt32();
                        break;
                    case 384:
                        this.numMatchesWithRanging = input.readInt32();
                        break;
                    case MetricsEvent.TUNER_POWER_NOTIFICATION_CONTROLS /*392*/:
                        this.numMatchesWithoutRangingForRangingEnabledSubscribes = input.readInt32();
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        }
                        return this;
                }
            }
        }

        public static WifiAwareLog parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiAwareLog) MessageNano.mergeFrom(new WifiAwareLog(), data);
        }

        public static WifiAwareLog parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiAwareLog().mergeFrom(input);
        }
    }

    public static final class WifiConfigStoreIO extends MessageNano {
        private static volatile WifiConfigStoreIO[] _emptyArray;
        public DurationBucket[] readDurations;
        public DurationBucket[] writeDurations;

        public static final class DurationBucket extends MessageNano {
            private static volatile DurationBucket[] _emptyArray;
            public int count;
            public int rangeEndMs;
            public int rangeStartMs;

            public static DurationBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new DurationBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public DurationBucket() {
                clear();
            }

            public DurationBucket clear() {
                this.rangeStartMs = 0;
                this.rangeEndMs = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                int i = this.rangeStartMs;
                if (i != 0) {
                    output.writeInt32(1, i);
                }
                i = this.rangeEndMs;
                if (i != 0) {
                    output.writeInt32(2, i);
                }
                i = this.count;
                if (i != 0) {
                    output.writeInt32(3, i);
                }
                super.writeTo(output);
            }

            /* Access modifiers changed, original: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                int i = this.rangeStartMs;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, i);
                }
                i = this.rangeEndMs;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(2, i);
                }
                i = this.count;
                if (i != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(3, i);
                }
                return size;
            }

            public DurationBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        this.rangeStartMs = input.readInt32();
                    } else if (tag == 16) {
                        this.rangeEndMs = input.readInt32();
                    } else if (tag == 24) {
                        this.count = input.readInt32();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static DurationBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (DurationBucket) MessageNano.mergeFrom(new DurationBucket(), data);
            }

            public static DurationBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new DurationBucket().mergeFrom(input);
            }
        }

        public static WifiConfigStoreIO[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiConfigStoreIO[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiConfigStoreIO() {
            clear();
        }

        public WifiConfigStoreIO clear() {
            this.readDurations = DurationBucket.emptyArray();
            this.writeDurations = DurationBucket.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i;
            DurationBucket element;
            DurationBucket[] durationBucketArr = this.readDurations;
            if (durationBucketArr != null && durationBucketArr.length > 0) {
                i = 0;
                while (true) {
                    element = this.readDurations;
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
            durationBucketArr = this.writeDurations;
            if (durationBucketArr != null && durationBucketArr.length > 0) {
                i = 0;
                while (true) {
                    DurationBucket[] durationBucketArr2 = this.writeDurations;
                    if (i >= durationBucketArr2.length) {
                        break;
                    }
                    element = durationBucketArr2[i];
                    if (element != null) {
                        output.writeMessage(2, element);
                    }
                    i++;
                }
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int i;
            DurationBucket element;
            int size = super.computeSerializedSize();
            DurationBucket[] durationBucketArr = this.readDurations;
            if (durationBucketArr != null && durationBucketArr.length > 0) {
                i = 0;
                while (true) {
                    element = this.readDurations;
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
            durationBucketArr = this.writeDurations;
            if (durationBucketArr != null && durationBucketArr.length > 0) {
                i = 0;
                while (true) {
                    DurationBucket[] durationBucketArr2 = this.writeDurations;
                    if (i >= durationBucketArr2.length) {
                        break;
                    }
                    element = durationBucketArr2[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(2, element);
                    }
                    i++;
                }
            }
            return size;
        }

        public WifiConfigStoreIO mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                int arrayLength;
                DurationBucket[] durationBucketArr;
                int i;
                DurationBucket[] newArray;
                if (tag == 10) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 10);
                    durationBucketArr = this.readDurations;
                    i = durationBucketArr == null ? 0 : durationBucketArr.length;
                    newArray = new DurationBucket[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.readDurations, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new DurationBucket();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new DurationBucket();
                    input.readMessage(newArray[i]);
                    this.readDurations = newArray;
                } else if (tag == 18) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    durationBucketArr = this.writeDurations;
                    i = durationBucketArr == null ? 0 : durationBucketArr.length;
                    newArray = new DurationBucket[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.writeDurations, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new DurationBucket();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new DurationBucket();
                    input.readMessage(newArray[i]);
                    this.writeDurations = newArray;
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiConfigStoreIO parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiConfigStoreIO) MessageNano.mergeFrom(new WifiConfigStoreIO(), data);
        }

        public static WifiConfigStoreIO parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiConfigStoreIO().mergeFrom(input);
        }
    }

    public static final class WifiDppLog extends MessageNano {
        public static final int EASY_CONNECT_EVENT_FAILURE_AUTHENTICATION = 2;
        public static final int EASY_CONNECT_EVENT_FAILURE_BUSY = 5;
        public static final int EASY_CONNECT_EVENT_FAILURE_CONFIGURATION = 4;
        public static final int EASY_CONNECT_EVENT_FAILURE_GENERIC = 7;
        public static final int EASY_CONNECT_EVENT_FAILURE_INVALID_NETWORK = 9;
        public static final int EASY_CONNECT_EVENT_FAILURE_INVALID_URI = 1;
        public static final int EASY_CONNECT_EVENT_FAILURE_NOT_COMPATIBLE = 3;
        public static final int EASY_CONNECT_EVENT_FAILURE_NOT_SUPPORTED = 8;
        public static final int EASY_CONNECT_EVENT_FAILURE_TIMEOUT = 6;
        public static final int EASY_CONNECT_EVENT_FAILURE_UNKNOWN = 0;
        public static final int EASY_CONNECT_EVENT_SUCCESS_CONFIGURATION_SENT = 1;
        public static final int EASY_CONNECT_EVENT_SUCCESS_UNKNOWN = 0;
        private static volatile WifiDppLog[] _emptyArray;
        public DppConfiguratorSuccessStatusHistogramBucket[] dppConfiguratorSuccessCode;
        public DppFailureStatusHistogramBucket[] dppFailureCode;
        public HistogramBucketInt32[] dppOperationTime;
        public int numDppConfiguratorInitiatorRequests;
        public int numDppEnrolleeInitiatorRequests;
        public int numDppEnrolleeSuccess;

        public static final class DppConfiguratorSuccessStatusHistogramBucket extends MessageNano {
            private static volatile DppConfiguratorSuccessStatusHistogramBucket[] _emptyArray;
            public int count;
            public int dppStatusType;

            public static DppConfiguratorSuccessStatusHistogramBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new DppConfiguratorSuccessStatusHistogramBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public DppConfiguratorSuccessStatusHistogramBucket() {
                clear();
            }

            public DppConfiguratorSuccessStatusHistogramBucket clear() {
                this.dppStatusType = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                int i = this.dppStatusType;
                if (i != 0) {
                    output.writeInt32(1, i);
                }
                i = this.count;
                if (i != 0) {
                    output.writeInt32(2, i);
                }
                super.writeTo(output);
            }

            /* Access modifiers changed, original: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                int i = this.dppStatusType;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, i);
                }
                i = this.count;
                if (i != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
                }
                return size;
            }

            public DppConfiguratorSuccessStatusHistogramBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        int value = input.readInt32();
                        if (value == 0 || value == 1) {
                            this.dppStatusType = value;
                        }
                    } else if (tag == 16) {
                        this.count = input.readInt32();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static DppConfiguratorSuccessStatusHistogramBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (DppConfiguratorSuccessStatusHistogramBucket) MessageNano.mergeFrom(new DppConfiguratorSuccessStatusHistogramBucket(), data);
            }

            public static DppConfiguratorSuccessStatusHistogramBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new DppConfiguratorSuccessStatusHistogramBucket().mergeFrom(input);
            }
        }

        public static final class DppFailureStatusHistogramBucket extends MessageNano {
            private static volatile DppFailureStatusHistogramBucket[] _emptyArray;
            public int count;
            public int dppStatusType;

            public static DppFailureStatusHistogramBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new DppFailureStatusHistogramBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public DppFailureStatusHistogramBucket() {
                clear();
            }

            public DppFailureStatusHistogramBucket clear() {
                this.dppStatusType = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                int i = this.dppStatusType;
                if (i != 0) {
                    output.writeInt32(1, i);
                }
                i = this.count;
                if (i != 0) {
                    output.writeInt32(2, i);
                }
                super.writeTo(output);
            }

            /* Access modifiers changed, original: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                int i = this.dppStatusType;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, i);
                }
                i = this.count;
                if (i != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
                }
                return size;
            }

            public DppFailureStatusHistogramBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag != 0) {
                        if (tag == 8) {
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
                                    this.dppStatusType = value;
                                    break;
                                default:
                                    break;
                            }
                        } else if (tag == 16) {
                            this.count = input.readInt32();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        return this;
                    }
                }
            }

            public static DppFailureStatusHistogramBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (DppFailureStatusHistogramBucket) MessageNano.mergeFrom(new DppFailureStatusHistogramBucket(), data);
            }

            public static DppFailureStatusHistogramBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new DppFailureStatusHistogramBucket().mergeFrom(input);
            }
        }

        public static WifiDppLog[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiDppLog[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiDppLog() {
            clear();
        }

        public WifiDppLog clear() {
            this.numDppConfiguratorInitiatorRequests = 0;
            this.numDppEnrolleeInitiatorRequests = 0;
            this.numDppEnrolleeSuccess = 0;
            this.dppConfiguratorSuccessCode = DppConfiguratorSuccessStatusHistogramBucket.emptyArray();
            this.dppFailureCode = DppFailureStatusHistogramBucket.emptyArray();
            this.dppOperationTime = HistogramBucketInt32.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.numDppConfiguratorInitiatorRequests;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.numDppEnrolleeInitiatorRequests;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.numDppEnrolleeSuccess;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            DppConfiguratorSuccessStatusHistogramBucket[] dppConfiguratorSuccessStatusHistogramBucketArr = this.dppConfiguratorSuccessCode;
            if (dppConfiguratorSuccessStatusHistogramBucketArr != null && dppConfiguratorSuccessStatusHistogramBucketArr.length > 0) {
                i = 0;
                while (true) {
                    DppConfiguratorSuccessStatusHistogramBucket element = this.dppConfiguratorSuccessCode;
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
            DppFailureStatusHistogramBucket[] dppFailureStatusHistogramBucketArr = this.dppFailureCode;
            if (dppFailureStatusHistogramBucketArr != null && dppFailureStatusHistogramBucketArr.length > 0) {
                i = 0;
                while (true) {
                    DppFailureStatusHistogramBucket element2 = this.dppFailureCode;
                    if (i >= element2.length) {
                        break;
                    }
                    element2 = element2[i];
                    if (element2 != null) {
                        output.writeMessage(5, element2);
                    }
                    i++;
                }
            }
            HistogramBucketInt32[] histogramBucketInt32Arr = this.dppOperationTime;
            if (histogramBucketInt32Arr != null && histogramBucketInt32Arr.length > 0) {
                i = 0;
                while (true) {
                    HistogramBucketInt32 element3 = this.dppOperationTime;
                    if (i >= element3.length) {
                        break;
                    }
                    element3 = element3[i];
                    if (element3 != null) {
                        output.writeMessage(7, element3);
                    }
                    i++;
                }
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.numDppConfiguratorInitiatorRequests;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.numDppEnrolleeInitiatorRequests;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.numDppEnrolleeSuccess;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            DppConfiguratorSuccessStatusHistogramBucket[] dppConfiguratorSuccessStatusHistogramBucketArr = this.dppConfiguratorSuccessCode;
            if (dppConfiguratorSuccessStatusHistogramBucketArr != null && dppConfiguratorSuccessStatusHistogramBucketArr.length > 0) {
                i = 0;
                while (true) {
                    DppConfiguratorSuccessStatusHistogramBucket element = this.dppConfiguratorSuccessCode;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(4, element);
                    }
                    i++;
                }
            }
            DppFailureStatusHistogramBucket[] dppFailureStatusHistogramBucketArr = this.dppFailureCode;
            if (dppFailureStatusHistogramBucketArr != null && dppFailureStatusHistogramBucketArr.length > 0) {
                i = 0;
                while (true) {
                    DppFailureStatusHistogramBucket element2 = this.dppFailureCode;
                    if (i >= element2.length) {
                        break;
                    }
                    element2 = element2[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(5, element2);
                    }
                    i++;
                }
            }
            HistogramBucketInt32[] histogramBucketInt32Arr = this.dppOperationTime;
            if (histogramBucketInt32Arr != null && histogramBucketInt32Arr.length > 0) {
                i = 0;
                while (true) {
                    HistogramBucketInt32 element3 = this.dppOperationTime;
                    if (i >= element3.length) {
                        break;
                    }
                    element3 = element3[i];
                    if (element3 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(7, element3);
                    }
                    i++;
                }
            }
            return size;
        }

        public WifiDppLog mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                int arrayLength;
                int i;
                if (tag == 8) {
                    this.numDppConfiguratorInitiatorRequests = input.readInt32();
                } else if (tag == 16) {
                    this.numDppEnrolleeInitiatorRequests = input.readInt32();
                } else if (tag == 24) {
                    this.numDppEnrolleeSuccess = input.readInt32();
                } else if (tag == 34) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                    DppConfiguratorSuccessStatusHistogramBucket[] dppConfiguratorSuccessStatusHistogramBucketArr = this.dppConfiguratorSuccessCode;
                    i = dppConfiguratorSuccessStatusHistogramBucketArr == null ? 0 : dppConfiguratorSuccessStatusHistogramBucketArr.length;
                    DppConfiguratorSuccessStatusHistogramBucket[] newArray = new DppConfiguratorSuccessStatusHistogramBucket[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.dppConfiguratorSuccessCode, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new DppConfiguratorSuccessStatusHistogramBucket();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new DppConfiguratorSuccessStatusHistogramBucket();
                    input.readMessage(newArray[i]);
                    this.dppConfiguratorSuccessCode = newArray;
                } else if (tag == 42) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 42);
                    DppFailureStatusHistogramBucket[] dppFailureStatusHistogramBucketArr = this.dppFailureCode;
                    i = dppFailureStatusHistogramBucketArr == null ? 0 : dppFailureStatusHistogramBucketArr.length;
                    DppFailureStatusHistogramBucket[] newArray2 = new DppFailureStatusHistogramBucket[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.dppFailureCode, 0, newArray2, 0, i);
                    }
                    while (i < newArray2.length - 1) {
                        newArray2[i] = new DppFailureStatusHistogramBucket();
                        input.readMessage(newArray2[i]);
                        input.readTag();
                        i++;
                    }
                    newArray2[i] = new DppFailureStatusHistogramBucket();
                    input.readMessage(newArray2[i]);
                    this.dppFailureCode = newArray2;
                } else if (tag == 58) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 58);
                    HistogramBucketInt32[] histogramBucketInt32Arr = this.dppOperationTime;
                    i = histogramBucketInt32Arr == null ? 0 : histogramBucketInt32Arr.length;
                    HistogramBucketInt32[] newArray3 = new HistogramBucketInt32[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.dppOperationTime, 0, newArray3, 0, i);
                    }
                    while (i < newArray3.length - 1) {
                        newArray3[i] = new HistogramBucketInt32();
                        input.readMessage(newArray3[i]);
                        input.readTag();
                        i++;
                    }
                    newArray3[i] = new HistogramBucketInt32();
                    input.readMessage(newArray3[i]);
                    this.dppOperationTime = newArray3;
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiDppLog parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiDppLog) MessageNano.mergeFrom(new WifiDppLog(), data);
        }

        public static WifiDppLog parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiDppLog().mergeFrom(input);
        }
    }

    public static final class WifiIsUnusableEvent extends MessageNano {
        public static final int TYPE_DATA_STALL_BAD_TX = 1;
        public static final int TYPE_DATA_STALL_BOTH = 3;
        public static final int TYPE_DATA_STALL_TX_WITHOUT_RX = 2;
        public static final int TYPE_FIRMWARE_ALERT = 4;
        public static final int TYPE_IP_REACHABILITY_LOST = 5;
        public static final int TYPE_UNKNOWN = 0;
        private static volatile WifiIsUnusableEvent[] _emptyArray;
        public int firmwareAlertCode;
        public long lastLinkLayerStatsUpdateTime;
        public int lastPredictionHorizonSec;
        public int lastScore;
        public int lastWifiUsabilityScore;
        public long packetUpdateTimeDelta;
        public long rxSuccessDelta;
        public boolean screenOn;
        public long startTimeMillis;
        public long txBadDelta;
        public long txRetriesDelta;
        public long txSuccessDelta;
        public int type;

        public static WifiIsUnusableEvent[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiIsUnusableEvent[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiIsUnusableEvent() {
            clear();
        }

        public WifiIsUnusableEvent clear() {
            this.type = 0;
            this.startTimeMillis = 0;
            this.lastScore = -1;
            this.txSuccessDelta = 0;
            this.txRetriesDelta = 0;
            this.txBadDelta = 0;
            this.rxSuccessDelta = 0;
            this.packetUpdateTimeDelta = 0;
            this.lastLinkLayerStatsUpdateTime = 0;
            this.firmwareAlertCode = -1;
            this.lastWifiUsabilityScore = -1;
            this.lastPredictionHorizonSec = -1;
            this.screenOn = false;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.type;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            long j = this.startTimeMillis;
            if (j != 0) {
                output.writeInt64(2, j);
            }
            i = this.lastScore;
            if (i != -1) {
                output.writeInt32(3, i);
            }
            long j2 = this.txSuccessDelta;
            if (j2 != 0) {
                output.writeInt64(4, j2);
            }
            j2 = this.txRetriesDelta;
            if (j2 != 0) {
                output.writeInt64(5, j2);
            }
            j2 = this.txBadDelta;
            if (j2 != 0) {
                output.writeInt64(6, j2);
            }
            j2 = this.rxSuccessDelta;
            if (j2 != 0) {
                output.writeInt64(7, j2);
            }
            j2 = this.packetUpdateTimeDelta;
            if (j2 != 0) {
                output.writeInt64(8, j2);
            }
            j2 = this.lastLinkLayerStatsUpdateTime;
            if (j2 != 0) {
                output.writeInt64(9, j2);
            }
            i = this.firmwareAlertCode;
            if (i != -1) {
                output.writeInt32(10, i);
            }
            i = this.lastWifiUsabilityScore;
            if (i != -1) {
                output.writeInt32(11, i);
            }
            i = this.lastPredictionHorizonSec;
            if (i != -1) {
                output.writeInt32(12, i);
            }
            boolean z = this.screenOn;
            if (z) {
                output.writeBool(13, z);
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
            long j = this.startTimeMillis;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(2, j);
            }
            i = this.lastScore;
            if (i != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            long j2 = this.txSuccessDelta;
            if (j2 != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(4, j2);
            }
            j2 = this.txRetriesDelta;
            if (j2 != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(5, j2);
            }
            j2 = this.txBadDelta;
            if (j2 != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(6, j2);
            }
            j2 = this.rxSuccessDelta;
            if (j2 != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(7, j2);
            }
            j2 = this.packetUpdateTimeDelta;
            if (j2 != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(8, j2);
            }
            j2 = this.lastLinkLayerStatsUpdateTime;
            if (j2 != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(9, j2);
            }
            i = this.firmwareAlertCode;
            if (i != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(10, i);
            }
            i = this.lastWifiUsabilityScore;
            if (i != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(11, i);
            }
            i = this.lastPredictionHorizonSec;
            if (i != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(12, i);
            }
            boolean z = this.screenOn;
            if (z) {
                return size + CodedOutputByteBufferNano.computeBoolSize(13, z);
            }
            return size;
        }

        public WifiIsUnusableEvent mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        int value = input.readInt32();
                        if (value != 0 && value != 1 && value != 2 && value != 3 && value != 4 && value != 5) {
                            break;
                        }
                        this.type = value;
                        break;
                        break;
                    case 16:
                        this.startTimeMillis = input.readInt64();
                        break;
                    case 24:
                        this.lastScore = input.readInt32();
                        break;
                    case 32:
                        this.txSuccessDelta = input.readInt64();
                        break;
                    case 40:
                        this.txRetriesDelta = input.readInt64();
                        break;
                    case 48:
                        this.txBadDelta = input.readInt64();
                        break;
                    case 56:
                        this.rxSuccessDelta = input.readInt64();
                        break;
                    case 64:
                        this.packetUpdateTimeDelta = input.readInt64();
                        break;
                    case 72:
                        this.lastLinkLayerStatsUpdateTime = input.readInt64();
                        break;
                    case 80:
                        this.firmwareAlertCode = input.readInt32();
                        break;
                    case 88:
                        this.lastWifiUsabilityScore = input.readInt32();
                        break;
                    case 96:
                        this.lastPredictionHorizonSec = input.readInt32();
                        break;
                    case 104:
                        this.screenOn = input.readBool();
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        }
                        return this;
                }
            }
        }

        public static WifiIsUnusableEvent parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiIsUnusableEvent) MessageNano.mergeFrom(new WifiIsUnusableEvent(), data);
        }

        public static WifiIsUnusableEvent parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiIsUnusableEvent().mergeFrom(input);
        }
    }

    public static final class WifiLinkLayerUsageStats extends MessageNano {
        private static volatile WifiLinkLayerUsageStats[] _emptyArray;
        public long loggingDurationMs;
        public long radioBackgroundScanTimeMs;
        public long radioHs20ScanTimeMs;
        public long radioNanScanTimeMs;
        public long radioOnTimeMs;
        public long radioPnoScanTimeMs;
        public long radioRoamScanTimeMs;
        public long radioRxTimeMs;
        public long radioScanTimeMs;
        public long radioTxTimeMs;

        public static WifiLinkLayerUsageStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiLinkLayerUsageStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiLinkLayerUsageStats() {
            clear();
        }

        public WifiLinkLayerUsageStats clear() {
            this.loggingDurationMs = 0;
            this.radioOnTimeMs = 0;
            this.radioTxTimeMs = 0;
            this.radioRxTimeMs = 0;
            this.radioScanTimeMs = 0;
            this.radioNanScanTimeMs = 0;
            this.radioBackgroundScanTimeMs = 0;
            this.radioRoamScanTimeMs = 0;
            this.radioPnoScanTimeMs = 0;
            this.radioHs20ScanTimeMs = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            long j = this.loggingDurationMs;
            if (j != 0) {
                output.writeInt64(1, j);
            }
            j = this.radioOnTimeMs;
            if (j != 0) {
                output.writeInt64(2, j);
            }
            j = this.radioTxTimeMs;
            if (j != 0) {
                output.writeInt64(3, j);
            }
            j = this.radioRxTimeMs;
            if (j != 0) {
                output.writeInt64(4, j);
            }
            j = this.radioScanTimeMs;
            if (j != 0) {
                output.writeInt64(5, j);
            }
            j = this.radioNanScanTimeMs;
            if (j != 0) {
                output.writeInt64(6, j);
            }
            j = this.radioBackgroundScanTimeMs;
            if (j != 0) {
                output.writeInt64(7, j);
            }
            j = this.radioRoamScanTimeMs;
            if (j != 0) {
                output.writeInt64(8, j);
            }
            j = this.radioPnoScanTimeMs;
            if (j != 0) {
                output.writeInt64(9, j);
            }
            j = this.radioHs20ScanTimeMs;
            if (j != 0) {
                output.writeInt64(10, j);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            long j = this.loggingDurationMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, j);
            }
            j = this.radioOnTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(2, j);
            }
            j = this.radioTxTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(3, j);
            }
            j = this.radioRxTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(4, j);
            }
            j = this.radioScanTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(5, j);
            }
            j = this.radioNanScanTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(6, j);
            }
            j = this.radioBackgroundScanTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(7, j);
            }
            j = this.radioRoamScanTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(8, j);
            }
            j = this.radioPnoScanTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(9, j);
            }
            j = this.radioHs20ScanTimeMs;
            if (j != 0) {
                return size + CodedOutputByteBufferNano.computeInt64Size(10, j);
            }
            return size;
        }

        public WifiLinkLayerUsageStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        this.loggingDurationMs = input.readInt64();
                        break;
                    case 16:
                        this.radioOnTimeMs = input.readInt64();
                        break;
                    case 24:
                        this.radioTxTimeMs = input.readInt64();
                        break;
                    case 32:
                        this.radioRxTimeMs = input.readInt64();
                        break;
                    case 40:
                        this.radioScanTimeMs = input.readInt64();
                        break;
                    case 48:
                        this.radioNanScanTimeMs = input.readInt64();
                        break;
                    case 56:
                        this.radioBackgroundScanTimeMs = input.readInt64();
                        break;
                    case 64:
                        this.radioRoamScanTimeMs = input.readInt64();
                        break;
                    case 72:
                        this.radioPnoScanTimeMs = input.readInt64();
                        break;
                    case 80:
                        this.radioHs20ScanTimeMs = input.readInt64();
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        }
                        return this;
                }
            }
        }

        public static WifiLinkLayerUsageStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiLinkLayerUsageStats) MessageNano.mergeFrom(new WifiLinkLayerUsageStats(), data);
        }

        public static WifiLinkLayerUsageStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiLinkLayerUsageStats().mergeFrom(input);
        }
    }

    public static final class WifiLockStats extends MessageNano {
        private static volatile WifiLockStats[] _emptyArray;
        public HistogramBucketInt32[] highPerfActiveSessionDurationSecHistogram;
        public long highPerfActiveTimeMs;
        public HistogramBucketInt32[] highPerfLockAcqDurationSecHistogram;
        public HistogramBucketInt32[] lowLatencyActiveSessionDurationSecHistogram;
        public long lowLatencyActiveTimeMs;
        public HistogramBucketInt32[] lowLatencyLockAcqDurationSecHistogram;

        public static WifiLockStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiLockStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiLockStats() {
            clear();
        }

        public WifiLockStats clear() {
            this.highPerfActiveTimeMs = 0;
            this.lowLatencyActiveTimeMs = 0;
            this.highPerfLockAcqDurationSecHistogram = HistogramBucketInt32.emptyArray();
            this.lowLatencyLockAcqDurationSecHistogram = HistogramBucketInt32.emptyArray();
            this.highPerfActiveSessionDurationSecHistogram = HistogramBucketInt32.emptyArray();
            this.lowLatencyActiveSessionDurationSecHistogram = HistogramBucketInt32.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i;
            HistogramBucketInt32 element;
            HistogramBucketInt32[] histogramBucketInt32Arr;
            long j = this.highPerfActiveTimeMs;
            if (j != 0) {
                output.writeInt64(1, j);
            }
            j = this.lowLatencyActiveTimeMs;
            if (j != 0) {
                output.writeInt64(2, j);
            }
            HistogramBucketInt32[] histogramBucketInt32Arr2 = this.highPerfLockAcqDurationSecHistogram;
            if (histogramBucketInt32Arr2 != null && histogramBucketInt32Arr2.length > 0) {
                i = 0;
                while (true) {
                    element = this.highPerfLockAcqDurationSecHistogram;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        output.writeMessage(3, element);
                    }
                    i++;
                }
            }
            histogramBucketInt32Arr2 = this.lowLatencyLockAcqDurationSecHistogram;
            if (histogramBucketInt32Arr2 != null && histogramBucketInt32Arr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketInt32Arr = this.lowLatencyLockAcqDurationSecHistogram;
                    if (i >= histogramBucketInt32Arr.length) {
                        break;
                    }
                    element = histogramBucketInt32Arr[i];
                    if (element != null) {
                        output.writeMessage(4, element);
                    }
                    i++;
                }
            }
            histogramBucketInt32Arr2 = this.highPerfActiveSessionDurationSecHistogram;
            if (histogramBucketInt32Arr2 != null && histogramBucketInt32Arr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketInt32Arr = this.highPerfActiveSessionDurationSecHistogram;
                    if (i >= histogramBucketInt32Arr.length) {
                        break;
                    }
                    element = histogramBucketInt32Arr[i];
                    if (element != null) {
                        output.writeMessage(5, element);
                    }
                    i++;
                }
            }
            histogramBucketInt32Arr2 = this.lowLatencyActiveSessionDurationSecHistogram;
            if (histogramBucketInt32Arr2 != null && histogramBucketInt32Arr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketInt32Arr = this.lowLatencyActiveSessionDurationSecHistogram;
                    if (i >= histogramBucketInt32Arr.length) {
                        break;
                    }
                    element = histogramBucketInt32Arr[i];
                    if (element != null) {
                        output.writeMessage(6, element);
                    }
                    i++;
                }
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int i;
            HistogramBucketInt32 element;
            HistogramBucketInt32[] histogramBucketInt32Arr;
            int size = super.computeSerializedSize();
            long j = this.highPerfActiveTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, j);
            }
            j = this.lowLatencyActiveTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(2, j);
            }
            HistogramBucketInt32[] histogramBucketInt32Arr2 = this.highPerfLockAcqDurationSecHistogram;
            if (histogramBucketInt32Arr2 != null && histogramBucketInt32Arr2.length > 0) {
                i = 0;
                while (true) {
                    element = this.highPerfLockAcqDurationSecHistogram;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(3, element);
                    }
                    i++;
                }
            }
            histogramBucketInt32Arr2 = this.lowLatencyLockAcqDurationSecHistogram;
            if (histogramBucketInt32Arr2 != null && histogramBucketInt32Arr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketInt32Arr = this.lowLatencyLockAcqDurationSecHistogram;
                    if (i >= histogramBucketInt32Arr.length) {
                        break;
                    }
                    element = histogramBucketInt32Arr[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(4, element);
                    }
                    i++;
                }
            }
            histogramBucketInt32Arr2 = this.highPerfActiveSessionDurationSecHistogram;
            if (histogramBucketInt32Arr2 != null && histogramBucketInt32Arr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketInt32Arr = this.highPerfActiveSessionDurationSecHistogram;
                    if (i >= histogramBucketInt32Arr.length) {
                        break;
                    }
                    element = histogramBucketInt32Arr[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(5, element);
                    }
                    i++;
                }
            }
            histogramBucketInt32Arr2 = this.lowLatencyActiveSessionDurationSecHistogram;
            if (histogramBucketInt32Arr2 != null && histogramBucketInt32Arr2.length > 0) {
                i = 0;
                while (true) {
                    histogramBucketInt32Arr = this.lowLatencyActiveSessionDurationSecHistogram;
                    if (i >= histogramBucketInt32Arr.length) {
                        break;
                    }
                    element = histogramBucketInt32Arr[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(6, element);
                    }
                    i++;
                }
            }
            return size;
        }

        public WifiLockStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                int arrayLength;
                HistogramBucketInt32[] histogramBucketInt32Arr;
                int i;
                HistogramBucketInt32[] newArray;
                if (tag == 8) {
                    this.highPerfActiveTimeMs = input.readInt64();
                } else if (tag == 16) {
                    this.lowLatencyActiveTimeMs = input.readInt64();
                } else if (tag == 26) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 26);
                    histogramBucketInt32Arr = this.highPerfLockAcqDurationSecHistogram;
                    i = histogramBucketInt32Arr == null ? 0 : histogramBucketInt32Arr.length;
                    newArray = new HistogramBucketInt32[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.highPerfLockAcqDurationSecHistogram, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new HistogramBucketInt32();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new HistogramBucketInt32();
                    input.readMessage(newArray[i]);
                    this.highPerfLockAcqDurationSecHistogram = newArray;
                } else if (tag == 34) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                    histogramBucketInt32Arr = this.lowLatencyLockAcqDurationSecHistogram;
                    i = histogramBucketInt32Arr == null ? 0 : histogramBucketInt32Arr.length;
                    newArray = new HistogramBucketInt32[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.lowLatencyLockAcqDurationSecHistogram, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new HistogramBucketInt32();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new HistogramBucketInt32();
                    input.readMessage(newArray[i]);
                    this.lowLatencyLockAcqDurationSecHistogram = newArray;
                } else if (tag == 42) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 42);
                    histogramBucketInt32Arr = this.highPerfActiveSessionDurationSecHistogram;
                    i = histogramBucketInt32Arr == null ? 0 : histogramBucketInt32Arr.length;
                    newArray = new HistogramBucketInt32[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.highPerfActiveSessionDurationSecHistogram, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new HistogramBucketInt32();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new HistogramBucketInt32();
                    input.readMessage(newArray[i]);
                    this.highPerfActiveSessionDurationSecHistogram = newArray;
                } else if (tag == 50) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 50);
                    histogramBucketInt32Arr = this.lowLatencyActiveSessionDurationSecHistogram;
                    i = histogramBucketInt32Arr == null ? 0 : histogramBucketInt32Arr.length;
                    newArray = new HistogramBucketInt32[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.lowLatencyActiveSessionDurationSecHistogram, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new HistogramBucketInt32();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new HistogramBucketInt32();
                    input.readMessage(newArray[i]);
                    this.lowLatencyActiveSessionDurationSecHistogram = newArray;
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiLockStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiLockStats) MessageNano.mergeFrom(new WifiLockStats(), data);
        }

        public static WifiLockStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiLockStats().mergeFrom(input);
        }
    }

    public static final class WifiLog extends MessageNano {
        public static final int FAILURE_WIFI_DISABLED = 4;
        public static final int SCAN_FAILURE_INTERRUPTED = 2;
        public static final int SCAN_FAILURE_INVALID_CONFIGURATION = 3;
        public static final int SCAN_SUCCESS = 1;
        public static final int SCAN_UNKNOWN = 0;
        public static final int WIFI_ASSOCIATED = 3;
        public static final int WIFI_DISABLED = 1;
        public static final int WIFI_DISCONNECTED = 2;
        public static final int WIFI_UNKNOWN = 0;
        private static volatile WifiLog[] _emptyArray;
        public AlertReasonCount[] alertReasonCount;
        public NumConnectableNetworksBucket[] availableOpenBssidsInScanHistogram;
        public NumConnectableNetworksBucket[] availableOpenOrSavedBssidsInScanHistogram;
        public NumConnectableNetworksBucket[] availableOpenOrSavedSsidsInScanHistogram;
        public NumConnectableNetworksBucket[] availableOpenSsidsInScanHistogram;
        public NumConnectableNetworksBucket[] availableSavedBssidsInScanHistogram;
        public NumConnectableNetworksBucket[] availableSavedPasspointProviderBssidsInScanHistogram;
        public NumConnectableNetworksBucket[] availableSavedPasspointProviderProfilesInScanHistogram;
        public NumConnectableNetworksBucket[] availableSavedSsidsInScanHistogram;
        public WifiSystemStateEntry[] backgroundScanRequestState;
        public ScanReturnEntry[] backgroundScanReturnEntries;
        public ConnectToNetworkNotificationAndActionCount[] connectToNetworkNotificationActionCount;
        public ConnectToNetworkNotificationAndActionCount[] connectToNetworkNotificationCount;
        public ConnectionEvent[] connectionEvent;
        public ExperimentValues experimentValues;
        public int fullBandAllSingleScanListenerResults;
        public String hardwareRevision;
        public PasspointProfileTypeCount[] installedPasspointProfileTypeForR1;
        public PasspointProfileTypeCount[] installedPasspointProfileTypeForR2;
        public boolean isLocationEnabled;
        public boolean isMacRandomizationOn;
        public boolean isScanningAlwaysEnabled;
        public boolean isWifiNetworksAvailableNotificationOn;
        public LinkProbeStats linkProbeStats;
        public LinkSpeedCount[] linkSpeedCounts;
        public DeviceMobilityStatePnoScanStats[] mobilityStatePnoStatsList;
        public NetworkSelectionExperimentDecisions[] networkSelectionExperimentDecisionsList;
        public int numAddOrUpdateNetworkCalls;
        public int numBackgroundScans;
        public int numClientInterfaceDown;
        public int numConnectivityOneshotScans;
        public int numConnectivityWatchdogBackgroundBad;
        public int numConnectivityWatchdogBackgroundGood;
        public int numConnectivityWatchdogPnoBad;
        public int numConnectivityWatchdogPnoGood;
        public int numEmptyScanResults;
        public int numEnableNetworkCalls;
        public int numEnhancedOpenNetworkScanResults;
        public int numEnhancedOpenNetworks;
        public int numExternalAppOneshotScanRequests;
        public int numExternalBackgroundAppOneshotScanRequestsThrottled;
        public int numExternalForegroundAppOneshotScanRequestsThrottled;
        public int numHalCrashes;
        public int numHiddenNetworkScanResults;
        public int numHiddenNetworks;
        public int numHostapdCrashes;
        public int numHotspot2R1NetworkScanResults;
        public int numHotspot2R2NetworkScanResults;
        public int numLastResortWatchdogAvailableNetworksTotal;
        public int numLastResortWatchdogBadAssociationNetworksTotal;
        public int numLastResortWatchdogBadAuthenticationNetworksTotal;
        public int numLastResortWatchdogBadDhcpNetworksTotal;
        public int numLastResortWatchdogBadOtherNetworksTotal;
        public int numLastResortWatchdogSuccesses;
        public int numLastResortWatchdogTriggers;
        public int numLastResortWatchdogTriggersWithBadAssociation;
        public int numLastResortWatchdogTriggersWithBadAuthentication;
        public int numLastResortWatchdogTriggersWithBadDhcp;
        public int numLastResortWatchdogTriggersWithBadOther;
        public int numLegacyEnterpriseNetworkScanResults;
        public int numLegacyEnterpriseNetworks;
        public int numLegacyPersonalNetworkScanResults;
        public int numLegacyPersonalNetworks;
        public int numNetworksAddedByApps;
        public int numNetworksAddedByUser;
        public int numNonEmptyScanResults;
        public int numOneshotHasDfsChannelScans;
        public int numOneshotScans;
        public int numOpenNetworkConnectMessageFailedToSend;
        public int numOpenNetworkRecommendationUpdates;
        public int numOpenNetworkScanResults;
        public int numOpenNetworks;
        public int numPasspointNetworks;
        public int numPasspointProviderInstallSuccess;
        public int numPasspointProviderInstallation;
        public int numPasspointProviderUninstallSuccess;
        public int numPasspointProviderUninstallation;
        public int numPasspointProviders;
        public int numPasspointProvidersSuccessfullyConnected;
        public int numRadioModeChangeToDbs;
        public int numRadioModeChangeToMcc;
        public int numRadioModeChangeToSbs;
        public int numRadioModeChangeToScc;
        public int numSarSensorRegistrationFailures;
        public int numSavedNetworks;
        public int numSavedNetworksWithMacRandomization;
        public int numScans;
        public int numSetupClientInterfaceFailureDueToHal;
        public int numSetupClientInterfaceFailureDueToSupplicant;
        public int numSetupClientInterfaceFailureDueToWificond;
        public int numSetupSoftApInterfaceFailureDueToHal;
        public int numSetupSoftApInterfaceFailureDueToHostapd;
        public int numSetupSoftApInterfaceFailureDueToWificond;
        public int numSoftApInterfaceDown;
        public int numSoftApUserBandPreferenceUnsatisfied;
        public int numSupplicantCrashes;
        public int numTotalScanResults;
        public int numWifiToggledViaAirplane;
        public int numWifiToggledViaSettings;
        public int numWificondCrashes;
        public int numWpa3EnterpriseNetworkScanResults;
        public int numWpa3EnterpriseNetworks;
        public int numWpa3PersonalNetworkScanResults;
        public int numWpa3PersonalNetworks;
        public NumConnectableNetworksBucket[] observed80211McSupportingApsInScanHistogram;
        public NumConnectableNetworksBucket[] observedHotspotR1ApsInScanHistogram;
        public NumConnectableNetworksBucket[] observedHotspotR1ApsPerEssInScanHistogram;
        public NumConnectableNetworksBucket[] observedHotspotR1EssInScanHistogram;
        public NumConnectableNetworksBucket[] observedHotspotR2ApsInScanHistogram;
        public NumConnectableNetworksBucket[] observedHotspotR2ApsPerEssInScanHistogram;
        public NumConnectableNetworksBucket[] observedHotspotR2EssInScanHistogram;
        public int openNetworkRecommenderBlacklistSize;
        public int partialAllSingleScanListenerResults;
        public PasspointProvisionStats passpointProvisionStats;
        public PnoScanMetrics pnoScanMetrics;
        public int recordDurationSec;
        public RssiPollCount[] rssiPollDeltaCount;
        public RssiPollCount[] rssiPollRssiCount;
        public ScanReturnEntry[] scanReturnEntries;
        public String scoreExperimentId;
        public SoftApConnectedClientsEvent[] softApConnectedClientsEventsLocalOnly;
        public SoftApConnectedClientsEvent[] softApConnectedClientsEventsTethered;
        public SoftApDurationBucket[] softApDuration;
        public SoftApReturnCodeCount[] softApReturnCode;
        public StaEvent[] staEventList;
        public NumConnectableNetworksBucket[] totalBssidsInScanHistogram;
        public NumConnectableNetworksBucket[] totalSsidsInScanHistogram;
        public long watchdogTotalConnectionFailureCountAfterTrigger;
        public long watchdogTriggerToConnectionSuccessDurationMs;
        public WifiAwareLog wifiAwareLog;
        public WifiConfigStoreIO wifiConfigStoreIo;
        public WifiDppLog wifiDppLog;
        public WifiIsUnusableEvent[] wifiIsUnusableEventList;
        public WifiLinkLayerUsageStats wifiLinkLayerUsageStats;
        public WifiLockStats wifiLockStats;
        public WifiNetworkRequestApiLog wifiNetworkRequestApiLog;
        public WifiNetworkSuggestionApiLog wifiNetworkSuggestionApiLog;
        public WifiP2pStats wifiP2PStats;
        public WifiPowerStats wifiPowerStats;
        public WifiRadioUsage wifiRadioUsage;
        public WifiRttLog wifiRttLog;
        public WifiScoreCount[] wifiScoreCount;
        public WifiSystemStateEntry[] wifiSystemStateEntries;
        public WifiToggleStats wifiToggleStats;
        public WifiUsabilityScoreCount[] wifiUsabilityScoreCount;
        public WifiUsabilityStats[] wifiUsabilityStatsList;
        public WifiWakeStats wifiWakeStats;
        public WpsMetrics wpsMetrics;

        public static final class ScanReturnEntry extends MessageNano {
            private static volatile ScanReturnEntry[] _emptyArray;
            public int scanResultsCount;
            public int scanReturnCode;

            public static ScanReturnEntry[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new ScanReturnEntry[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public ScanReturnEntry() {
                clear();
            }

            public ScanReturnEntry clear() {
                this.scanReturnCode = 0;
                this.scanResultsCount = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                int i = this.scanReturnCode;
                if (i != 0) {
                    output.writeInt32(1, i);
                }
                i = this.scanResultsCount;
                if (i != 0) {
                    output.writeInt32(2, i);
                }
                super.writeTo(output);
            }

            /* Access modifiers changed, original: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                int i = this.scanReturnCode;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, i);
                }
                i = this.scanResultsCount;
                if (i != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
                }
                return size;
            }

            public ScanReturnEntry mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        int value = input.readInt32();
                        if (value == 0 || value == 1 || value == 2 || value == 3 || value == 4) {
                            this.scanReturnCode = value;
                        }
                    } else if (tag == 16) {
                        this.scanResultsCount = input.readInt32();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static ScanReturnEntry parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (ScanReturnEntry) MessageNano.mergeFrom(new ScanReturnEntry(), data);
            }

            public static ScanReturnEntry parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new ScanReturnEntry().mergeFrom(input);
            }
        }

        public static final class WifiSystemStateEntry extends MessageNano {
            private static volatile WifiSystemStateEntry[] _emptyArray;
            public boolean isScreenOn;
            public int wifiState;
            public int wifiStateCount;

            public static WifiSystemStateEntry[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new WifiSystemStateEntry[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public WifiSystemStateEntry() {
                clear();
            }

            public WifiSystemStateEntry clear() {
                this.wifiState = 0;
                this.wifiStateCount = 0;
                this.isScreenOn = false;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                int i = this.wifiState;
                if (i != 0) {
                    output.writeInt32(1, i);
                }
                i = this.wifiStateCount;
                if (i != 0) {
                    output.writeInt32(2, i);
                }
                boolean z = this.isScreenOn;
                if (z) {
                    output.writeBool(3, z);
                }
                super.writeTo(output);
            }

            /* Access modifiers changed, original: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                int i = this.wifiState;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, i);
                }
                i = this.wifiStateCount;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(2, i);
                }
                boolean z = this.isScreenOn;
                if (z) {
                    return size + CodedOutputByteBufferNano.computeBoolSize(3, z);
                }
                return size;
            }

            public WifiSystemStateEntry mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        int value = input.readInt32();
                        if (value == 0 || value == 1 || value == 2 || value == 3) {
                            this.wifiState = value;
                        }
                    } else if (tag == 16) {
                        this.wifiStateCount = input.readInt32();
                    } else if (tag == 24) {
                        this.isScreenOn = input.readBool();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static WifiSystemStateEntry parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (WifiSystemStateEntry) MessageNano.mergeFrom(new WifiSystemStateEntry(), data);
            }

            public static WifiSystemStateEntry parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new WifiSystemStateEntry().mergeFrom(input);
            }
        }

        public static WifiLog[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiLog[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiLog() {
            clear();
        }

        public WifiLog clear() {
            this.connectionEvent = ConnectionEvent.emptyArray();
            this.numSavedNetworks = 0;
            this.numOpenNetworks = 0;
            this.numLegacyPersonalNetworks = 0;
            this.numLegacyEnterpriseNetworks = 0;
            this.isLocationEnabled = false;
            this.isScanningAlwaysEnabled = false;
            this.numWifiToggledViaSettings = 0;
            this.numWifiToggledViaAirplane = 0;
            this.numNetworksAddedByUser = 0;
            this.numNetworksAddedByApps = 0;
            this.numEmptyScanResults = 0;
            this.numNonEmptyScanResults = 0;
            this.numOneshotScans = 0;
            this.numBackgroundScans = 0;
            this.scanReturnEntries = ScanReturnEntry.emptyArray();
            this.wifiSystemStateEntries = WifiSystemStateEntry.emptyArray();
            this.backgroundScanReturnEntries = ScanReturnEntry.emptyArray();
            this.backgroundScanRequestState = WifiSystemStateEntry.emptyArray();
            this.numLastResortWatchdogTriggers = 0;
            this.numLastResortWatchdogBadAssociationNetworksTotal = 0;
            this.numLastResortWatchdogBadAuthenticationNetworksTotal = 0;
            this.numLastResortWatchdogBadDhcpNetworksTotal = 0;
            this.numLastResortWatchdogBadOtherNetworksTotal = 0;
            this.numLastResortWatchdogAvailableNetworksTotal = 0;
            this.numLastResortWatchdogTriggersWithBadAssociation = 0;
            this.numLastResortWatchdogTriggersWithBadAuthentication = 0;
            this.numLastResortWatchdogTriggersWithBadDhcp = 0;
            this.numLastResortWatchdogTriggersWithBadOther = 0;
            this.numConnectivityWatchdogPnoGood = 0;
            this.numConnectivityWatchdogPnoBad = 0;
            this.numConnectivityWatchdogBackgroundGood = 0;
            this.numConnectivityWatchdogBackgroundBad = 0;
            this.recordDurationSec = 0;
            this.rssiPollRssiCount = RssiPollCount.emptyArray();
            this.numLastResortWatchdogSuccesses = 0;
            this.numHiddenNetworks = 0;
            this.numPasspointNetworks = 0;
            this.numTotalScanResults = 0;
            this.numOpenNetworkScanResults = 0;
            this.numLegacyPersonalNetworkScanResults = 0;
            this.numLegacyEnterpriseNetworkScanResults = 0;
            this.numHiddenNetworkScanResults = 0;
            this.numHotspot2R1NetworkScanResults = 0;
            this.numHotspot2R2NetworkScanResults = 0;
            this.numScans = 0;
            this.alertReasonCount = AlertReasonCount.emptyArray();
            this.wifiScoreCount = WifiScoreCount.emptyArray();
            this.softApDuration = SoftApDurationBucket.emptyArray();
            this.softApReturnCode = SoftApReturnCodeCount.emptyArray();
            this.rssiPollDeltaCount = RssiPollCount.emptyArray();
            this.staEventList = StaEvent.emptyArray();
            this.numHalCrashes = 0;
            this.numWificondCrashes = 0;
            this.numSetupClientInterfaceFailureDueToHal = 0;
            this.numSetupClientInterfaceFailureDueToWificond = 0;
            this.wifiAwareLog = null;
            this.numPasspointProviders = 0;
            this.numPasspointProviderInstallation = 0;
            this.numPasspointProviderInstallSuccess = 0;
            this.numPasspointProviderUninstallation = 0;
            this.numPasspointProviderUninstallSuccess = 0;
            this.numPasspointProvidersSuccessfullyConnected = 0;
            this.totalSsidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.totalBssidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableOpenSsidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableOpenBssidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableSavedSsidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableSavedBssidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableOpenOrSavedSsidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableOpenOrSavedBssidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableSavedPasspointProviderProfilesInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.availableSavedPasspointProviderBssidsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.fullBandAllSingleScanListenerResults = 0;
            this.partialAllSingleScanListenerResults = 0;
            this.pnoScanMetrics = null;
            this.connectToNetworkNotificationCount = ConnectToNetworkNotificationAndActionCount.emptyArray();
            this.connectToNetworkNotificationActionCount = ConnectToNetworkNotificationAndActionCount.emptyArray();
            this.openNetworkRecommenderBlacklistSize = 0;
            this.isWifiNetworksAvailableNotificationOn = false;
            this.numOpenNetworkRecommendationUpdates = 0;
            this.numOpenNetworkConnectMessageFailedToSend = 0;
            this.observedHotspotR1ApsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.observedHotspotR2ApsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.observedHotspotR1EssInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.observedHotspotR2EssInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.observedHotspotR1ApsPerEssInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.observedHotspotR2ApsPerEssInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.softApConnectedClientsEventsTethered = SoftApConnectedClientsEvent.emptyArray();
            this.softApConnectedClientsEventsLocalOnly = SoftApConnectedClientsEvent.emptyArray();
            this.wpsMetrics = null;
            this.wifiPowerStats = null;
            this.numConnectivityOneshotScans = 0;
            this.wifiWakeStats = null;
            this.observed80211McSupportingApsInScanHistogram = NumConnectableNetworksBucket.emptyArray();
            this.numSupplicantCrashes = 0;
            this.numHostapdCrashes = 0;
            this.numSetupClientInterfaceFailureDueToSupplicant = 0;
            this.numSetupSoftApInterfaceFailureDueToHal = 0;
            this.numSetupSoftApInterfaceFailureDueToWificond = 0;
            this.numSetupSoftApInterfaceFailureDueToHostapd = 0;
            this.numClientInterfaceDown = 0;
            this.numSoftApInterfaceDown = 0;
            this.numExternalAppOneshotScanRequests = 0;
            this.numExternalForegroundAppOneshotScanRequestsThrottled = 0;
            this.numExternalBackgroundAppOneshotScanRequestsThrottled = 0;
            this.watchdogTriggerToConnectionSuccessDurationMs = -1;
            this.watchdogTotalConnectionFailureCountAfterTrigger = 0;
            this.numOneshotHasDfsChannelScans = 0;
            this.wifiRttLog = null;
            this.isMacRandomizationOn = false;
            this.numRadioModeChangeToMcc = 0;
            this.numRadioModeChangeToScc = 0;
            this.numRadioModeChangeToSbs = 0;
            this.numRadioModeChangeToDbs = 0;
            this.numSoftApUserBandPreferenceUnsatisfied = 0;
            String str = "";
            this.scoreExperimentId = str;
            this.wifiRadioUsage = null;
            this.experimentValues = null;
            this.wifiIsUnusableEventList = WifiIsUnusableEvent.emptyArray();
            this.linkSpeedCounts = LinkSpeedCount.emptyArray();
            this.numSarSensorRegistrationFailures = 0;
            this.hardwareRevision = str;
            this.wifiLinkLayerUsageStats = null;
            this.wifiUsabilityStatsList = WifiUsabilityStats.emptyArray();
            this.wifiUsabilityScoreCount = WifiUsabilityScoreCount.emptyArray();
            this.mobilityStatePnoStatsList = DeviceMobilityStatePnoScanStats.emptyArray();
            this.wifiP2PStats = null;
            this.wifiDppLog = null;
            this.numEnhancedOpenNetworks = 0;
            this.numWpa3PersonalNetworks = 0;
            this.numWpa3EnterpriseNetworks = 0;
            this.numEnhancedOpenNetworkScanResults = 0;
            this.numWpa3PersonalNetworkScanResults = 0;
            this.numWpa3EnterpriseNetworkScanResults = 0;
            this.wifiConfigStoreIo = null;
            this.numSavedNetworksWithMacRandomization = 0;
            this.linkProbeStats = null;
            this.networkSelectionExperimentDecisionsList = NetworkSelectionExperimentDecisions.emptyArray();
            this.wifiNetworkRequestApiLog = null;
            this.wifiNetworkSuggestionApiLog = null;
            this.wifiLockStats = null;
            this.wifiToggleStats = null;
            this.numAddOrUpdateNetworkCalls = 0;
            this.numEnableNetworkCalls = 0;
            this.passpointProvisionStats = null;
            this.installedPasspointProfileTypeForR1 = PasspointProfileTypeCount.emptyArray();
            this.installedPasspointProfileTypeForR2 = PasspointProfileTypeCount.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i;
            ScanReturnEntry element;
            WifiSystemStateEntry element2;
            RssiPollCount element3;
            NumConnectableNetworksBucket element4;
            NumConnectableNetworksBucket[] numConnectableNetworksBucketArr;
            ConnectToNetworkNotificationAndActionCount element5;
            SoftApConnectedClientsEvent element6;
            ConnectionEvent[] connectionEventArr = this.connectionEvent;
            if (connectionEventArr != null && connectionEventArr.length > 0) {
                i = 0;
                while (true) {
                    ConnectionEvent element7 = this.connectionEvent;
                    if (i >= element7.length) {
                        break;
                    }
                    element7 = element7[i];
                    if (element7 != null) {
                        output.writeMessage(1, element7);
                    }
                    i++;
                }
            }
            i = this.numSavedNetworks;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.numOpenNetworks;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.numLegacyPersonalNetworks;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            i = this.numLegacyEnterpriseNetworks;
            if (i != 0) {
                output.writeInt32(5, i);
            }
            boolean z = this.isLocationEnabled;
            if (z) {
                output.writeBool(6, z);
            }
            z = this.isScanningAlwaysEnabled;
            if (z) {
                output.writeBool(7, z);
            }
            i = this.numWifiToggledViaSettings;
            if (i != 0) {
                output.writeInt32(8, i);
            }
            i = this.numWifiToggledViaAirplane;
            if (i != 0) {
                output.writeInt32(9, i);
            }
            i = this.numNetworksAddedByUser;
            if (i != 0) {
                output.writeInt32(10, i);
            }
            i = this.numNetworksAddedByApps;
            if (i != 0) {
                output.writeInt32(11, i);
            }
            i = this.numEmptyScanResults;
            if (i != 0) {
                output.writeInt32(12, i);
            }
            i = this.numNonEmptyScanResults;
            if (i != 0) {
                output.writeInt32(13, i);
            }
            i = this.numOneshotScans;
            if (i != 0) {
                output.writeInt32(14, i);
            }
            i = this.numBackgroundScans;
            if (i != 0) {
                output.writeInt32(15, i);
            }
            ScanReturnEntry[] scanReturnEntryArr = this.scanReturnEntries;
            if (scanReturnEntryArr != null && scanReturnEntryArr.length > 0) {
                i = 0;
                while (true) {
                    element = this.scanReturnEntries;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        output.writeMessage(16, element);
                    }
                    i++;
                }
            }
            WifiSystemStateEntry[] wifiSystemStateEntryArr = this.wifiSystemStateEntries;
            if (wifiSystemStateEntryArr != null && wifiSystemStateEntryArr.length > 0) {
                i = 0;
                while (true) {
                    element2 = this.wifiSystemStateEntries;
                    if (i >= element2.length) {
                        break;
                    }
                    element2 = element2[i];
                    if (element2 != null) {
                        output.writeMessage(17, element2);
                    }
                    i++;
                }
            }
            scanReturnEntryArr = this.backgroundScanReturnEntries;
            if (scanReturnEntryArr != null && scanReturnEntryArr.length > 0) {
                i = 0;
                while (true) {
                    element = this.backgroundScanReturnEntries;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        output.writeMessage(18, element);
                    }
                    i++;
                }
            }
            wifiSystemStateEntryArr = this.backgroundScanRequestState;
            if (wifiSystemStateEntryArr != null && wifiSystemStateEntryArr.length > 0) {
                i = 0;
                while (true) {
                    element2 = this.backgroundScanRequestState;
                    if (i >= element2.length) {
                        break;
                    }
                    element2 = element2[i];
                    if (element2 != null) {
                        output.writeMessage(19, element2);
                    }
                    i++;
                }
            }
            i = this.numLastResortWatchdogTriggers;
            if (i != 0) {
                output.writeInt32(20, i);
            }
            i = this.numLastResortWatchdogBadAssociationNetworksTotal;
            if (i != 0) {
                output.writeInt32(21, i);
            }
            i = this.numLastResortWatchdogBadAuthenticationNetworksTotal;
            if (i != 0) {
                output.writeInt32(22, i);
            }
            i = this.numLastResortWatchdogBadDhcpNetworksTotal;
            if (i != 0) {
                output.writeInt32(23, i);
            }
            i = this.numLastResortWatchdogBadOtherNetworksTotal;
            if (i != 0) {
                output.writeInt32(24, i);
            }
            i = this.numLastResortWatchdogAvailableNetworksTotal;
            if (i != 0) {
                output.writeInt32(25, i);
            }
            i = this.numLastResortWatchdogTriggersWithBadAssociation;
            if (i != 0) {
                output.writeInt32(26, i);
            }
            i = this.numLastResortWatchdogTriggersWithBadAuthentication;
            if (i != 0) {
                output.writeInt32(27, i);
            }
            i = this.numLastResortWatchdogTriggersWithBadDhcp;
            if (i != 0) {
                output.writeInt32(28, i);
            }
            i = this.numLastResortWatchdogTriggersWithBadOther;
            if (i != 0) {
                output.writeInt32(29, i);
            }
            i = this.numConnectivityWatchdogPnoGood;
            if (i != 0) {
                output.writeInt32(30, i);
            }
            i = this.numConnectivityWatchdogPnoBad;
            if (i != 0) {
                output.writeInt32(31, i);
            }
            i = this.numConnectivityWatchdogBackgroundGood;
            if (i != 0) {
                output.writeInt32(32, i);
            }
            i = this.numConnectivityWatchdogBackgroundBad;
            if (i != 0) {
                output.writeInt32(33, i);
            }
            i = this.recordDurationSec;
            if (i != 0) {
                output.writeInt32(34, i);
            }
            RssiPollCount[] rssiPollCountArr = this.rssiPollRssiCount;
            if (rssiPollCountArr != null && rssiPollCountArr.length > 0) {
                i = 0;
                while (true) {
                    element3 = this.rssiPollRssiCount;
                    if (i >= element3.length) {
                        break;
                    }
                    element3 = element3[i];
                    if (element3 != null) {
                        output.writeMessage(35, element3);
                    }
                    i++;
                }
            }
            i = this.numLastResortWatchdogSuccesses;
            if (i != 0) {
                output.writeInt32(36, i);
            }
            i = this.numHiddenNetworks;
            if (i != 0) {
                output.writeInt32(37, i);
            }
            i = this.numPasspointNetworks;
            if (i != 0) {
                output.writeInt32(38, i);
            }
            i = this.numTotalScanResults;
            if (i != 0) {
                output.writeInt32(39, i);
            }
            i = this.numOpenNetworkScanResults;
            if (i != 0) {
                output.writeInt32(40, i);
            }
            i = this.numLegacyPersonalNetworkScanResults;
            if (i != 0) {
                output.writeInt32(41, i);
            }
            i = this.numLegacyEnterpriseNetworkScanResults;
            if (i != 0) {
                output.writeInt32(42, i);
            }
            i = this.numHiddenNetworkScanResults;
            if (i != 0) {
                output.writeInt32(43, i);
            }
            i = this.numHotspot2R1NetworkScanResults;
            if (i != 0) {
                output.writeInt32(44, i);
            }
            i = this.numHotspot2R2NetworkScanResults;
            if (i != 0) {
                output.writeInt32(45, i);
            }
            i = this.numScans;
            if (i != 0) {
                output.writeInt32(46, i);
            }
            AlertReasonCount[] alertReasonCountArr = this.alertReasonCount;
            if (alertReasonCountArr != null && alertReasonCountArr.length > 0) {
                i = 0;
                while (true) {
                    AlertReasonCount element8 = this.alertReasonCount;
                    if (i >= element8.length) {
                        break;
                    }
                    element8 = element8[i];
                    if (element8 != null) {
                        output.writeMessage(47, element8);
                    }
                    i++;
                }
            }
            WifiScoreCount[] wifiScoreCountArr = this.wifiScoreCount;
            if (wifiScoreCountArr != null && wifiScoreCountArr.length > 0) {
                i = 0;
                while (true) {
                    WifiScoreCount element9 = this.wifiScoreCount;
                    if (i >= element9.length) {
                        break;
                    }
                    element9 = element9[i];
                    if (element9 != null) {
                        output.writeMessage(48, element9);
                    }
                    i++;
                }
            }
            SoftApDurationBucket[] softApDurationBucketArr = this.softApDuration;
            if (softApDurationBucketArr != null && softApDurationBucketArr.length > 0) {
                i = 0;
                while (true) {
                    SoftApDurationBucket element10 = this.softApDuration;
                    if (i >= element10.length) {
                        break;
                    }
                    element10 = element10[i];
                    if (element10 != null) {
                        output.writeMessage(49, element10);
                    }
                    i++;
                }
            }
            SoftApReturnCodeCount[] softApReturnCodeCountArr = this.softApReturnCode;
            if (softApReturnCodeCountArr != null && softApReturnCodeCountArr.length > 0) {
                i = 0;
                while (true) {
                    SoftApReturnCodeCount element11 = this.softApReturnCode;
                    if (i >= element11.length) {
                        break;
                    }
                    element11 = element11[i];
                    if (element11 != null) {
                        output.writeMessage(50, element11);
                    }
                    i++;
                }
            }
            rssiPollCountArr = this.rssiPollDeltaCount;
            if (rssiPollCountArr != null && rssiPollCountArr.length > 0) {
                i = 0;
                while (true) {
                    element3 = this.rssiPollDeltaCount;
                    if (i >= element3.length) {
                        break;
                    }
                    element3 = element3[i];
                    if (element3 != null) {
                        output.writeMessage(51, element3);
                    }
                    i++;
                }
            }
            StaEvent[] staEventArr = this.staEventList;
            if (staEventArr != null && staEventArr.length > 0) {
                i = 0;
                while (true) {
                    StaEvent element12 = this.staEventList;
                    if (i >= element12.length) {
                        break;
                    }
                    element12 = element12[i];
                    if (element12 != null) {
                        output.writeMessage(52, element12);
                    }
                    i++;
                }
            }
            i = this.numHalCrashes;
            if (i != 0) {
                output.writeInt32(53, i);
            }
            i = this.numWificondCrashes;
            if (i != 0) {
                output.writeInt32(54, i);
            }
            i = this.numSetupClientInterfaceFailureDueToHal;
            if (i != 0) {
                output.writeInt32(55, i);
            }
            i = this.numSetupClientInterfaceFailureDueToWificond;
            if (i != 0) {
                output.writeInt32(56, i);
            }
            WifiAwareLog wifiAwareLog = this.wifiAwareLog;
            if (wifiAwareLog != null) {
                output.writeMessage(57, wifiAwareLog);
            }
            i = this.numPasspointProviders;
            if (i != 0) {
                output.writeInt32(58, i);
            }
            i = this.numPasspointProviderInstallation;
            if (i != 0) {
                output.writeInt32(59, i);
            }
            i = this.numPasspointProviderInstallSuccess;
            if (i != 0) {
                output.writeInt32(60, i);
            }
            i = this.numPasspointProviderUninstallation;
            if (i != 0) {
                output.writeInt32(61, i);
            }
            i = this.numPasspointProviderUninstallSuccess;
            if (i != 0) {
                output.writeInt32(62, i);
            }
            i = this.numPasspointProvidersSuccessfullyConnected;
            if (i != 0) {
                output.writeInt32(63, i);
            }
            NumConnectableNetworksBucket[] numConnectableNetworksBucketArr2 = this.totalSsidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    element4 = this.totalSsidsInScanHistogram;
                    if (i >= element4.length) {
                        break;
                    }
                    element4 = element4[i];
                    if (element4 != null) {
                        output.writeMessage(64, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.totalBssidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.totalBssidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        output.writeMessage(65, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableOpenSsidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableOpenSsidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        output.writeMessage(66, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableOpenBssidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableOpenBssidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        output.writeMessage(67, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableSavedSsidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableSavedSsidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        output.writeMessage(68, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableSavedBssidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableSavedBssidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        output.writeMessage(69, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableOpenOrSavedSsidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableOpenOrSavedSsidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        output.writeMessage(70, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableOpenOrSavedBssidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableOpenOrSavedBssidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        output.writeMessage(71, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableSavedPasspointProviderProfilesInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableSavedPasspointProviderProfilesInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        output.writeMessage(72, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableSavedPasspointProviderBssidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableSavedPasspointProviderBssidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        output.writeMessage(73, element4);
                    }
                    i++;
                }
            }
            i = this.fullBandAllSingleScanListenerResults;
            if (i != 0) {
                output.writeInt32(74, i);
            }
            i = this.partialAllSingleScanListenerResults;
            if (i != 0) {
                output.writeInt32(75, i);
            }
            PnoScanMetrics pnoScanMetrics = this.pnoScanMetrics;
            if (pnoScanMetrics != null) {
                output.writeMessage(76, pnoScanMetrics);
            }
            ConnectToNetworkNotificationAndActionCount[] connectToNetworkNotificationAndActionCountArr = this.connectToNetworkNotificationCount;
            if (connectToNetworkNotificationAndActionCountArr != null && connectToNetworkNotificationAndActionCountArr.length > 0) {
                i = 0;
                while (true) {
                    element5 = this.connectToNetworkNotificationCount;
                    if (i >= element5.length) {
                        break;
                    }
                    element5 = element5[i];
                    if (element5 != null) {
                        output.writeMessage(77, element5);
                    }
                    i++;
                }
            }
            connectToNetworkNotificationAndActionCountArr = this.connectToNetworkNotificationActionCount;
            if (connectToNetworkNotificationAndActionCountArr != null && connectToNetworkNotificationAndActionCountArr.length > 0) {
                i = 0;
                while (true) {
                    ConnectToNetworkNotificationAndActionCount[] connectToNetworkNotificationAndActionCountArr2 = this.connectToNetworkNotificationActionCount;
                    if (i >= connectToNetworkNotificationAndActionCountArr2.length) {
                        break;
                    }
                    element5 = connectToNetworkNotificationAndActionCountArr2[i];
                    if (element5 != null) {
                        output.writeMessage(78, element5);
                    }
                    i++;
                }
            }
            i = this.openNetworkRecommenderBlacklistSize;
            if (i != 0) {
                output.writeInt32(79, i);
            }
            z = this.isWifiNetworksAvailableNotificationOn;
            if (z) {
                output.writeBool(80, z);
            }
            i = this.numOpenNetworkRecommendationUpdates;
            if (i != 0) {
                output.writeInt32(81, i);
            }
            i = this.numOpenNetworkConnectMessageFailedToSend;
            if (i != 0) {
                output.writeInt32(82, i);
            }
            numConnectableNetworksBucketArr2 = this.observedHotspotR1ApsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    element4 = this.observedHotspotR1ApsInScanHistogram;
                    if (i >= element4.length) {
                        break;
                    }
                    element4 = element4[i];
                    if (element4 != null) {
                        output.writeMessage(83, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.observedHotspotR2ApsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.observedHotspotR2ApsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        output.writeMessage(84, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.observedHotspotR1EssInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.observedHotspotR1EssInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        output.writeMessage(85, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.observedHotspotR2EssInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.observedHotspotR2EssInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        output.writeMessage(86, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.observedHotspotR1ApsPerEssInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.observedHotspotR1ApsPerEssInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        output.writeMessage(87, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.observedHotspotR2ApsPerEssInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.observedHotspotR2ApsPerEssInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        output.writeMessage(88, element4);
                    }
                    i++;
                }
            }
            SoftApConnectedClientsEvent[] softApConnectedClientsEventArr = this.softApConnectedClientsEventsTethered;
            if (softApConnectedClientsEventArr != null && softApConnectedClientsEventArr.length > 0) {
                i = 0;
                while (true) {
                    element6 = this.softApConnectedClientsEventsTethered;
                    if (i >= element6.length) {
                        break;
                    }
                    element6 = element6[i];
                    if (element6 != null) {
                        output.writeMessage(89, element6);
                    }
                    i++;
                }
            }
            softApConnectedClientsEventArr = this.softApConnectedClientsEventsLocalOnly;
            if (softApConnectedClientsEventArr != null && softApConnectedClientsEventArr.length > 0) {
                i = 0;
                while (true) {
                    SoftApConnectedClientsEvent[] softApConnectedClientsEventArr2 = this.softApConnectedClientsEventsLocalOnly;
                    if (i >= softApConnectedClientsEventArr2.length) {
                        break;
                    }
                    element6 = softApConnectedClientsEventArr2[i];
                    if (element6 != null) {
                        output.writeMessage(90, element6);
                    }
                    i++;
                }
            }
            WpsMetrics wpsMetrics = this.wpsMetrics;
            if (wpsMetrics != null) {
                output.writeMessage(91, wpsMetrics);
            }
            WifiPowerStats wifiPowerStats = this.wifiPowerStats;
            if (wifiPowerStats != null) {
                output.writeMessage(92, wifiPowerStats);
            }
            i = this.numConnectivityOneshotScans;
            if (i != 0) {
                output.writeInt32(93, i);
            }
            WifiWakeStats wifiWakeStats = this.wifiWakeStats;
            if (wifiWakeStats != null) {
                output.writeMessage(94, wifiWakeStats);
            }
            numConnectableNetworksBucketArr2 = this.observed80211McSupportingApsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    element4 = this.observed80211McSupportingApsInScanHistogram;
                    if (i >= element4.length) {
                        break;
                    }
                    element4 = element4[i];
                    if (element4 != null) {
                        output.writeMessage(95, element4);
                    }
                    i++;
                }
            }
            i = this.numSupplicantCrashes;
            if (i != 0) {
                output.writeInt32(96, i);
            }
            i = this.numHostapdCrashes;
            if (i != 0) {
                output.writeInt32(97, i);
            }
            i = this.numSetupClientInterfaceFailureDueToSupplicant;
            if (i != 0) {
                output.writeInt32(98, i);
            }
            i = this.numSetupSoftApInterfaceFailureDueToHal;
            if (i != 0) {
                output.writeInt32(99, i);
            }
            i = this.numSetupSoftApInterfaceFailureDueToWificond;
            if (i != 0) {
                output.writeInt32(100, i);
            }
            i = this.numSetupSoftApInterfaceFailureDueToHostapd;
            if (i != 0) {
                output.writeInt32(101, i);
            }
            i = this.numClientInterfaceDown;
            if (i != 0) {
                output.writeInt32(102, i);
            }
            i = this.numSoftApInterfaceDown;
            if (i != 0) {
                output.writeInt32(103, i);
            }
            i = this.numExternalAppOneshotScanRequests;
            if (i != 0) {
                output.writeInt32(104, i);
            }
            i = this.numExternalForegroundAppOneshotScanRequestsThrottled;
            if (i != 0) {
                output.writeInt32(105, i);
            }
            i = this.numExternalBackgroundAppOneshotScanRequestsThrottled;
            if (i != 0) {
                output.writeInt32(106, i);
            }
            long j = this.watchdogTriggerToConnectionSuccessDurationMs;
            if (j != -1) {
                output.writeInt64(107, j);
            }
            j = this.watchdogTotalConnectionFailureCountAfterTrigger;
            if (j != 0) {
                output.writeInt64(108, j);
            }
            i = this.numOneshotHasDfsChannelScans;
            if (i != 0) {
                output.writeInt32(109, i);
            }
            WifiRttLog wifiRttLog = this.wifiRttLog;
            if (wifiRttLog != null) {
                output.writeMessage(110, wifiRttLog);
            }
            z = this.isMacRandomizationOn;
            if (z) {
                output.writeBool(111, z);
            }
            i = this.numRadioModeChangeToMcc;
            if (i != 0) {
                output.writeInt32(112, i);
            }
            i = this.numRadioModeChangeToScc;
            if (i != 0) {
                output.writeInt32(113, i);
            }
            i = this.numRadioModeChangeToSbs;
            if (i != 0) {
                output.writeInt32(114, i);
            }
            i = this.numRadioModeChangeToDbs;
            if (i != 0) {
                output.writeInt32(115, i);
            }
            i = this.numSoftApUserBandPreferenceUnsatisfied;
            if (i != 0) {
                output.writeInt32(116, i);
            }
            String str = "";
            if (!this.scoreExperimentId.equals(str)) {
                output.writeString(117, this.scoreExperimentId);
            }
            WifiRadioUsage wifiRadioUsage = this.wifiRadioUsage;
            if (wifiRadioUsage != null) {
                output.writeMessage(118, wifiRadioUsage);
            }
            ExperimentValues experimentValues = this.experimentValues;
            if (experimentValues != null) {
                output.writeMessage(119, experimentValues);
            }
            WifiIsUnusableEvent[] wifiIsUnusableEventArr = this.wifiIsUnusableEventList;
            if (wifiIsUnusableEventArr != null && wifiIsUnusableEventArr.length > 0) {
                i = 0;
                while (true) {
                    WifiIsUnusableEvent element13 = this.wifiIsUnusableEventList;
                    if (i >= element13.length) {
                        break;
                    }
                    element13 = element13[i];
                    if (element13 != null) {
                        output.writeMessage(120, element13);
                    }
                    i++;
                }
            }
            LinkSpeedCount[] linkSpeedCountArr = this.linkSpeedCounts;
            if (linkSpeedCountArr != null && linkSpeedCountArr.length > 0) {
                i = 0;
                while (true) {
                    LinkSpeedCount element14 = this.linkSpeedCounts;
                    if (i >= element14.length) {
                        break;
                    }
                    element14 = element14[i];
                    if (element14 != null) {
                        output.writeMessage(121, element14);
                    }
                    i++;
                }
            }
            i = this.numSarSensorRegistrationFailures;
            if (i != 0) {
                output.writeInt32(122, i);
            }
            PasspointProfileTypeCount[] passpointProfileTypeCountArr = this.installedPasspointProfileTypeForR1;
            if (passpointProfileTypeCountArr != null && passpointProfileTypeCountArr.length > 0) {
                i = 0;
                while (true) {
                    PasspointProfileTypeCount element15 = this.installedPasspointProfileTypeForR1;
                    if (i >= element15.length) {
                        break;
                    }
                    element15 = element15[i];
                    if (element15 != null) {
                        output.writeMessage(123, element15);
                    }
                    i++;
                }
            }
            if (!this.hardwareRevision.equals(str)) {
                output.writeString(124, this.hardwareRevision);
            }
            WifiLinkLayerUsageStats wifiLinkLayerUsageStats = this.wifiLinkLayerUsageStats;
            if (wifiLinkLayerUsageStats != null) {
                output.writeMessage(125, wifiLinkLayerUsageStats);
            }
            WifiUsabilityStats[] wifiUsabilityStatsArr = this.wifiUsabilityStatsList;
            if (wifiUsabilityStatsArr != null && wifiUsabilityStatsArr.length > 0) {
                i = 0;
                while (true) {
                    WifiUsabilityStats element16 = this.wifiUsabilityStatsList;
                    if (i >= element16.length) {
                        break;
                    }
                    element16 = element16[i];
                    if (element16 != null) {
                        output.writeMessage(126, element16);
                    }
                    i++;
                }
            }
            WifiUsabilityScoreCount[] wifiUsabilityScoreCountArr = this.wifiUsabilityScoreCount;
            if (wifiUsabilityScoreCountArr != null && wifiUsabilityScoreCountArr.length > 0) {
                i = 0;
                while (true) {
                    WifiUsabilityScoreCount element17 = this.wifiUsabilityScoreCount;
                    if (i >= element17.length) {
                        break;
                    }
                    element17 = element17[i];
                    if (element17 != null) {
                        output.writeMessage(127, element17);
                    }
                    i++;
                }
            }
            DeviceMobilityStatePnoScanStats[] deviceMobilityStatePnoScanStatsArr = this.mobilityStatePnoStatsList;
            if (deviceMobilityStatePnoScanStatsArr != null && deviceMobilityStatePnoScanStatsArr.length > 0) {
                i = 0;
                while (true) {
                    DeviceMobilityStatePnoScanStats element18 = this.mobilityStatePnoStatsList;
                    if (i >= element18.length) {
                        break;
                    }
                    element18 = element18[i];
                    if (element18 != null) {
                        output.writeMessage(128, element18);
                    }
                    i++;
                }
            }
            WifiP2pStats wifiP2pStats = this.wifiP2PStats;
            if (wifiP2pStats != null) {
                output.writeMessage(129, wifiP2pStats);
            }
            WifiDppLog wifiDppLog = this.wifiDppLog;
            if (wifiDppLog != null) {
                output.writeMessage(130, wifiDppLog);
            }
            i = this.numEnhancedOpenNetworks;
            if (i != 0) {
                output.writeInt32(131, i);
            }
            i = this.numWpa3PersonalNetworks;
            if (i != 0) {
                output.writeInt32(132, i);
            }
            i = this.numWpa3EnterpriseNetworks;
            if (i != 0) {
                output.writeInt32(133, i);
            }
            i = this.numEnhancedOpenNetworkScanResults;
            if (i != 0) {
                output.writeInt32(134, i);
            }
            i = this.numWpa3PersonalNetworkScanResults;
            if (i != 0) {
                output.writeInt32(135, i);
            }
            i = this.numWpa3EnterpriseNetworkScanResults;
            if (i != 0) {
                output.writeInt32(136, i);
            }
            WifiConfigStoreIO wifiConfigStoreIO = this.wifiConfigStoreIo;
            if (wifiConfigStoreIO != null) {
                output.writeMessage(137, wifiConfigStoreIO);
            }
            i = this.numSavedNetworksWithMacRandomization;
            if (i != 0) {
                output.writeInt32(138, i);
            }
            LinkProbeStats linkProbeStats = this.linkProbeStats;
            if (linkProbeStats != null) {
                output.writeMessage(139, linkProbeStats);
            }
            NetworkSelectionExperimentDecisions[] networkSelectionExperimentDecisionsArr = this.networkSelectionExperimentDecisionsList;
            if (networkSelectionExperimentDecisionsArr != null && networkSelectionExperimentDecisionsArr.length > 0) {
                i = 0;
                while (true) {
                    NetworkSelectionExperimentDecisions element19 = this.networkSelectionExperimentDecisionsList;
                    if (i >= element19.length) {
                        break;
                    }
                    element19 = element19[i];
                    if (element19 != null) {
                        output.writeMessage(140, element19);
                    }
                    i++;
                }
            }
            WifiNetworkRequestApiLog wifiNetworkRequestApiLog = this.wifiNetworkRequestApiLog;
            if (wifiNetworkRequestApiLog != null) {
                output.writeMessage(141, wifiNetworkRequestApiLog);
            }
            WifiNetworkSuggestionApiLog wifiNetworkSuggestionApiLog = this.wifiNetworkSuggestionApiLog;
            if (wifiNetworkSuggestionApiLog != null) {
                output.writeMessage(142, wifiNetworkSuggestionApiLog);
            }
            WifiLockStats wifiLockStats = this.wifiLockStats;
            if (wifiLockStats != null) {
                output.writeMessage(143, wifiLockStats);
            }
            WifiToggleStats wifiToggleStats = this.wifiToggleStats;
            if (wifiToggleStats != null) {
                output.writeMessage(144, wifiToggleStats);
            }
            i = this.numAddOrUpdateNetworkCalls;
            if (i != 0) {
                output.writeInt32(145, i);
            }
            i = this.numEnableNetworkCalls;
            if (i != 0) {
                output.writeInt32(146, i);
            }
            PasspointProvisionStats passpointProvisionStats = this.passpointProvisionStats;
            if (passpointProvisionStats != null) {
                output.writeMessage(147, passpointProvisionStats);
            }
            passpointProfileTypeCountArr = this.installedPasspointProfileTypeForR2;
            if (passpointProfileTypeCountArr != null && passpointProfileTypeCountArr.length > 0) {
                i = 0;
                while (true) {
                    PasspointProfileTypeCount element20 = this.installedPasspointProfileTypeForR2;
                    if (i >= element20.length) {
                        break;
                    }
                    element20 = element20[i];
                    if (element20 != null) {
                        output.writeMessage(148, element20);
                    }
                    i++;
                }
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int i;
            ScanReturnEntry element;
            WifiSystemStateEntry element2;
            RssiPollCount element3;
            NumConnectableNetworksBucket element4;
            NumConnectableNetworksBucket[] numConnectableNetworksBucketArr;
            ConnectToNetworkNotificationAndActionCount element5;
            SoftApConnectedClientsEvent element6;
            int size = super.computeSerializedSize();
            ConnectionEvent[] connectionEventArr = this.connectionEvent;
            if (connectionEventArr != null && connectionEventArr.length > 0) {
                i = 0;
                while (true) {
                    ConnectionEvent element7 = this.connectionEvent;
                    if (i >= element7.length) {
                        break;
                    }
                    element7 = element7[i];
                    if (element7 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(1, element7);
                    }
                    i++;
                }
            }
            i = this.numSavedNetworks;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.numOpenNetworks;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.numLegacyPersonalNetworks;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            i = this.numLegacyEnterpriseNetworks;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(5, i);
            }
            boolean z = this.isLocationEnabled;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(6, z);
            }
            z = this.isScanningAlwaysEnabled;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(7, z);
            }
            i = this.numWifiToggledViaSettings;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(8, i);
            }
            i = this.numWifiToggledViaAirplane;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(9, i);
            }
            i = this.numNetworksAddedByUser;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(10, i);
            }
            i = this.numNetworksAddedByApps;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(11, i);
            }
            i = this.numEmptyScanResults;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(12, i);
            }
            i = this.numNonEmptyScanResults;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(13, i);
            }
            i = this.numOneshotScans;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(14, i);
            }
            i = this.numBackgroundScans;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(15, i);
            }
            ScanReturnEntry[] scanReturnEntryArr = this.scanReturnEntries;
            if (scanReturnEntryArr != null && scanReturnEntryArr.length > 0) {
                i = 0;
                while (true) {
                    element = this.scanReturnEntries;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(16, element);
                    }
                    i++;
                }
            }
            WifiSystemStateEntry[] wifiSystemStateEntryArr = this.wifiSystemStateEntries;
            if (wifiSystemStateEntryArr != null && wifiSystemStateEntryArr.length > 0) {
                i = 0;
                while (true) {
                    element2 = this.wifiSystemStateEntries;
                    if (i >= element2.length) {
                        break;
                    }
                    element2 = element2[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(17, element2);
                    }
                    i++;
                }
            }
            scanReturnEntryArr = this.backgroundScanReturnEntries;
            if (scanReturnEntryArr != null && scanReturnEntryArr.length > 0) {
                i = 0;
                while (true) {
                    element = this.backgroundScanReturnEntries;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(18, element);
                    }
                    i++;
                }
            }
            wifiSystemStateEntryArr = this.backgroundScanRequestState;
            if (wifiSystemStateEntryArr != null && wifiSystemStateEntryArr.length > 0) {
                i = 0;
                while (true) {
                    element2 = this.backgroundScanRequestState;
                    if (i >= element2.length) {
                        break;
                    }
                    element2 = element2[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(19, element2);
                    }
                    i++;
                }
            }
            i = this.numLastResortWatchdogTriggers;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(20, i);
            }
            i = this.numLastResortWatchdogBadAssociationNetworksTotal;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(21, i);
            }
            i = this.numLastResortWatchdogBadAuthenticationNetworksTotal;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(22, i);
            }
            i = this.numLastResortWatchdogBadDhcpNetworksTotal;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(23, i);
            }
            i = this.numLastResortWatchdogBadOtherNetworksTotal;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(24, i);
            }
            i = this.numLastResortWatchdogAvailableNetworksTotal;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(25, i);
            }
            i = this.numLastResortWatchdogTriggersWithBadAssociation;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(26, i);
            }
            i = this.numLastResortWatchdogTriggersWithBadAuthentication;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(27, i);
            }
            i = this.numLastResortWatchdogTriggersWithBadDhcp;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(28, i);
            }
            i = this.numLastResortWatchdogTriggersWithBadOther;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(29, i);
            }
            i = this.numConnectivityWatchdogPnoGood;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(30, i);
            }
            i = this.numConnectivityWatchdogPnoBad;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(31, i);
            }
            i = this.numConnectivityWatchdogBackgroundGood;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(32, i);
            }
            i = this.numConnectivityWatchdogBackgroundBad;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(33, i);
            }
            i = this.recordDurationSec;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(34, i);
            }
            RssiPollCount[] rssiPollCountArr = this.rssiPollRssiCount;
            if (rssiPollCountArr != null && rssiPollCountArr.length > 0) {
                i = 0;
                while (true) {
                    element3 = this.rssiPollRssiCount;
                    if (i >= element3.length) {
                        break;
                    }
                    element3 = element3[i];
                    if (element3 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(35, element3);
                    }
                    i++;
                }
            }
            i = this.numLastResortWatchdogSuccesses;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(36, i);
            }
            i = this.numHiddenNetworks;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(37, i);
            }
            i = this.numPasspointNetworks;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(38, i);
            }
            i = this.numTotalScanResults;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(39, i);
            }
            i = this.numOpenNetworkScanResults;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(40, i);
            }
            i = this.numLegacyPersonalNetworkScanResults;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(41, i);
            }
            i = this.numLegacyEnterpriseNetworkScanResults;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(42, i);
            }
            i = this.numHiddenNetworkScanResults;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(43, i);
            }
            i = this.numHotspot2R1NetworkScanResults;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(44, i);
            }
            i = this.numHotspot2R2NetworkScanResults;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(45, i);
            }
            i = this.numScans;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(46, i);
            }
            AlertReasonCount[] alertReasonCountArr = this.alertReasonCount;
            if (alertReasonCountArr != null && alertReasonCountArr.length > 0) {
                i = 0;
                while (true) {
                    AlertReasonCount element8 = this.alertReasonCount;
                    if (i >= element8.length) {
                        break;
                    }
                    element8 = element8[i];
                    if (element8 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(47, element8);
                    }
                    i++;
                }
            }
            WifiScoreCount[] wifiScoreCountArr = this.wifiScoreCount;
            if (wifiScoreCountArr != null && wifiScoreCountArr.length > 0) {
                i = 0;
                while (true) {
                    WifiScoreCount element9 = this.wifiScoreCount;
                    if (i >= element9.length) {
                        break;
                    }
                    element9 = element9[i];
                    if (element9 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(48, element9);
                    }
                    i++;
                }
            }
            SoftApDurationBucket[] softApDurationBucketArr = this.softApDuration;
            if (softApDurationBucketArr != null && softApDurationBucketArr.length > 0) {
                i = 0;
                while (true) {
                    SoftApDurationBucket element10 = this.softApDuration;
                    if (i >= element10.length) {
                        break;
                    }
                    element10 = element10[i];
                    if (element10 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(49, element10);
                    }
                    i++;
                }
            }
            SoftApReturnCodeCount[] softApReturnCodeCountArr = this.softApReturnCode;
            if (softApReturnCodeCountArr != null && softApReturnCodeCountArr.length > 0) {
                i = 0;
                while (true) {
                    SoftApReturnCodeCount element11 = this.softApReturnCode;
                    if (i >= element11.length) {
                        break;
                    }
                    element11 = element11[i];
                    if (element11 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(50, element11);
                    }
                    i++;
                }
            }
            rssiPollCountArr = this.rssiPollDeltaCount;
            if (rssiPollCountArr != null && rssiPollCountArr.length > 0) {
                i = 0;
                while (true) {
                    element3 = this.rssiPollDeltaCount;
                    if (i >= element3.length) {
                        break;
                    }
                    element3 = element3[i];
                    if (element3 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(51, element3);
                    }
                    i++;
                }
            }
            StaEvent[] staEventArr = this.staEventList;
            if (staEventArr != null && staEventArr.length > 0) {
                i = 0;
                while (true) {
                    StaEvent element12 = this.staEventList;
                    if (i >= element12.length) {
                        break;
                    }
                    element12 = element12[i];
                    if (element12 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(52, element12);
                    }
                    i++;
                }
            }
            i = this.numHalCrashes;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(53, i);
            }
            i = this.numWificondCrashes;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(54, i);
            }
            i = this.numSetupClientInterfaceFailureDueToHal;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(55, i);
            }
            i = this.numSetupClientInterfaceFailureDueToWificond;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(56, i);
            }
            WifiAwareLog wifiAwareLog = this.wifiAwareLog;
            if (wifiAwareLog != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(57, wifiAwareLog);
            }
            i = this.numPasspointProviders;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(58, i);
            }
            i = this.numPasspointProviderInstallation;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(59, i);
            }
            i = this.numPasspointProviderInstallSuccess;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(60, i);
            }
            i = this.numPasspointProviderUninstallation;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(61, i);
            }
            i = this.numPasspointProviderUninstallSuccess;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(62, i);
            }
            i = this.numPasspointProvidersSuccessfullyConnected;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(63, i);
            }
            NumConnectableNetworksBucket[] numConnectableNetworksBucketArr2 = this.totalSsidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    element4 = this.totalSsidsInScanHistogram;
                    if (i >= element4.length) {
                        break;
                    }
                    element4 = element4[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(64, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.totalBssidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.totalBssidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(65, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableOpenSsidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableOpenSsidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(66, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableOpenBssidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableOpenBssidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(67, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableSavedSsidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableSavedSsidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(68, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableSavedBssidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableSavedBssidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(69, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableOpenOrSavedSsidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableOpenOrSavedSsidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(70, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableOpenOrSavedBssidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableOpenOrSavedBssidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(71, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableSavedPasspointProviderProfilesInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableSavedPasspointProviderProfilesInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(72, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.availableSavedPasspointProviderBssidsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.availableSavedPasspointProviderBssidsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(73, element4);
                    }
                    i++;
                }
            }
            i = this.fullBandAllSingleScanListenerResults;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(74, i);
            }
            i = this.partialAllSingleScanListenerResults;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(75, i);
            }
            PnoScanMetrics pnoScanMetrics = this.pnoScanMetrics;
            if (pnoScanMetrics != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(76, pnoScanMetrics);
            }
            ConnectToNetworkNotificationAndActionCount[] connectToNetworkNotificationAndActionCountArr = this.connectToNetworkNotificationCount;
            if (connectToNetworkNotificationAndActionCountArr != null && connectToNetworkNotificationAndActionCountArr.length > 0) {
                i = 0;
                while (true) {
                    element5 = this.connectToNetworkNotificationCount;
                    if (i >= element5.length) {
                        break;
                    }
                    element5 = element5[i];
                    if (element5 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(77, element5);
                    }
                    i++;
                }
            }
            connectToNetworkNotificationAndActionCountArr = this.connectToNetworkNotificationActionCount;
            if (connectToNetworkNotificationAndActionCountArr != null && connectToNetworkNotificationAndActionCountArr.length > 0) {
                i = 0;
                while (true) {
                    ConnectToNetworkNotificationAndActionCount[] connectToNetworkNotificationAndActionCountArr2 = this.connectToNetworkNotificationActionCount;
                    if (i >= connectToNetworkNotificationAndActionCountArr2.length) {
                        break;
                    }
                    element5 = connectToNetworkNotificationAndActionCountArr2[i];
                    if (element5 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(78, element5);
                    }
                    i++;
                }
            }
            i = this.openNetworkRecommenderBlacklistSize;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(79, i);
            }
            z = this.isWifiNetworksAvailableNotificationOn;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(80, z);
            }
            i = this.numOpenNetworkRecommendationUpdates;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(81, i);
            }
            i = this.numOpenNetworkConnectMessageFailedToSend;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(82, i);
            }
            numConnectableNetworksBucketArr2 = this.observedHotspotR1ApsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    element4 = this.observedHotspotR1ApsInScanHistogram;
                    if (i >= element4.length) {
                        break;
                    }
                    element4 = element4[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(83, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.observedHotspotR2ApsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.observedHotspotR2ApsInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(84, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.observedHotspotR1EssInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.observedHotspotR1EssInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(85, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.observedHotspotR2EssInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.observedHotspotR2EssInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(86, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.observedHotspotR1ApsPerEssInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.observedHotspotR1ApsPerEssInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(87, element4);
                    }
                    i++;
                }
            }
            numConnectableNetworksBucketArr2 = this.observedHotspotR2ApsPerEssInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    numConnectableNetworksBucketArr = this.observedHotspotR2ApsPerEssInScanHistogram;
                    if (i >= numConnectableNetworksBucketArr.length) {
                        break;
                    }
                    element4 = numConnectableNetworksBucketArr[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(88, element4);
                    }
                    i++;
                }
            }
            SoftApConnectedClientsEvent[] softApConnectedClientsEventArr = this.softApConnectedClientsEventsTethered;
            if (softApConnectedClientsEventArr != null && softApConnectedClientsEventArr.length > 0) {
                i = 0;
                while (true) {
                    element6 = this.softApConnectedClientsEventsTethered;
                    if (i >= element6.length) {
                        break;
                    }
                    element6 = element6[i];
                    if (element6 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(89, element6);
                    }
                    i++;
                }
            }
            softApConnectedClientsEventArr = this.softApConnectedClientsEventsLocalOnly;
            if (softApConnectedClientsEventArr != null && softApConnectedClientsEventArr.length > 0) {
                i = 0;
                while (true) {
                    SoftApConnectedClientsEvent[] softApConnectedClientsEventArr2 = this.softApConnectedClientsEventsLocalOnly;
                    if (i >= softApConnectedClientsEventArr2.length) {
                        break;
                    }
                    element6 = softApConnectedClientsEventArr2[i];
                    if (element6 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(90, element6);
                    }
                    i++;
                }
            }
            WpsMetrics wpsMetrics = this.wpsMetrics;
            if (wpsMetrics != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(91, wpsMetrics);
            }
            WifiPowerStats wifiPowerStats = this.wifiPowerStats;
            if (wifiPowerStats != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(92, wifiPowerStats);
            }
            i = this.numConnectivityOneshotScans;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(93, i);
            }
            WifiWakeStats wifiWakeStats = this.wifiWakeStats;
            if (wifiWakeStats != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(94, wifiWakeStats);
            }
            numConnectableNetworksBucketArr2 = this.observed80211McSupportingApsInScanHistogram;
            if (numConnectableNetworksBucketArr2 != null && numConnectableNetworksBucketArr2.length > 0) {
                i = 0;
                while (true) {
                    element4 = this.observed80211McSupportingApsInScanHistogram;
                    if (i >= element4.length) {
                        break;
                    }
                    element4 = element4[i];
                    if (element4 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(95, element4);
                    }
                    i++;
                }
            }
            i = this.numSupplicantCrashes;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(96, i);
            }
            i = this.numHostapdCrashes;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(97, i);
            }
            i = this.numSetupClientInterfaceFailureDueToSupplicant;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(98, i);
            }
            i = this.numSetupSoftApInterfaceFailureDueToHal;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(99, i);
            }
            i = this.numSetupSoftApInterfaceFailureDueToWificond;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(100, i);
            }
            i = this.numSetupSoftApInterfaceFailureDueToHostapd;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(101, i);
            }
            i = this.numClientInterfaceDown;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(102, i);
            }
            i = this.numSoftApInterfaceDown;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(103, i);
            }
            i = this.numExternalAppOneshotScanRequests;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(104, i);
            }
            i = this.numExternalForegroundAppOneshotScanRequestsThrottled;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(105, i);
            }
            i = this.numExternalBackgroundAppOneshotScanRequestsThrottled;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(106, i);
            }
            long j = this.watchdogTriggerToConnectionSuccessDurationMs;
            if (j != -1) {
                size += CodedOutputByteBufferNano.computeInt64Size(107, j);
            }
            j = this.watchdogTotalConnectionFailureCountAfterTrigger;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(108, j);
            }
            i = this.numOneshotHasDfsChannelScans;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(109, i);
            }
            WifiRttLog wifiRttLog = this.wifiRttLog;
            if (wifiRttLog != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(110, wifiRttLog);
            }
            z = this.isMacRandomizationOn;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(111, z);
            }
            i = this.numRadioModeChangeToMcc;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(112, i);
            }
            i = this.numRadioModeChangeToScc;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(113, i);
            }
            i = this.numRadioModeChangeToSbs;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(114, i);
            }
            i = this.numRadioModeChangeToDbs;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(115, i);
            }
            i = this.numSoftApUserBandPreferenceUnsatisfied;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(116, i);
            }
            String str = "";
            if (!this.scoreExperimentId.equals(str)) {
                size += CodedOutputByteBufferNano.computeStringSize(117, this.scoreExperimentId);
            }
            WifiRadioUsage wifiRadioUsage = this.wifiRadioUsage;
            if (wifiRadioUsage != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(118, wifiRadioUsage);
            }
            ExperimentValues experimentValues = this.experimentValues;
            if (experimentValues != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(119, experimentValues);
            }
            WifiIsUnusableEvent[] wifiIsUnusableEventArr = this.wifiIsUnusableEventList;
            if (wifiIsUnusableEventArr != null && wifiIsUnusableEventArr.length > 0) {
                i = 0;
                while (true) {
                    WifiIsUnusableEvent element13 = this.wifiIsUnusableEventList;
                    if (i >= element13.length) {
                        break;
                    }
                    element13 = element13[i];
                    if (element13 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(120, element13);
                    }
                    i++;
                }
            }
            LinkSpeedCount[] linkSpeedCountArr = this.linkSpeedCounts;
            if (linkSpeedCountArr != null && linkSpeedCountArr.length > 0) {
                i = 0;
                while (true) {
                    LinkSpeedCount element14 = this.linkSpeedCounts;
                    if (i >= element14.length) {
                        break;
                    }
                    element14 = element14[i];
                    if (element14 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(121, element14);
                    }
                    i++;
                }
            }
            i = this.numSarSensorRegistrationFailures;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(122, i);
            }
            PasspointProfileTypeCount[] passpointProfileTypeCountArr = this.installedPasspointProfileTypeForR1;
            if (passpointProfileTypeCountArr != null && passpointProfileTypeCountArr.length > 0) {
                i = 0;
                while (true) {
                    PasspointProfileTypeCount element15 = this.installedPasspointProfileTypeForR1;
                    if (i >= element15.length) {
                        break;
                    }
                    element15 = element15[i];
                    if (element15 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(123, element15);
                    }
                    i++;
                }
            }
            if (!this.hardwareRevision.equals(str)) {
                size += CodedOutputByteBufferNano.computeStringSize(124, this.hardwareRevision);
            }
            WifiLinkLayerUsageStats wifiLinkLayerUsageStats = this.wifiLinkLayerUsageStats;
            if (wifiLinkLayerUsageStats != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(125, wifiLinkLayerUsageStats);
            }
            WifiUsabilityStats[] wifiUsabilityStatsArr = this.wifiUsabilityStatsList;
            if (wifiUsabilityStatsArr != null && wifiUsabilityStatsArr.length > 0) {
                i = 0;
                while (true) {
                    WifiUsabilityStats element16 = this.wifiUsabilityStatsList;
                    if (i >= element16.length) {
                        break;
                    }
                    element16 = element16[i];
                    if (element16 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(126, element16);
                    }
                    i++;
                }
            }
            WifiUsabilityScoreCount[] wifiUsabilityScoreCountArr = this.wifiUsabilityScoreCount;
            if (wifiUsabilityScoreCountArr != null && wifiUsabilityScoreCountArr.length > 0) {
                i = 0;
                while (true) {
                    WifiUsabilityScoreCount element17 = this.wifiUsabilityScoreCount;
                    if (i >= element17.length) {
                        break;
                    }
                    element17 = element17[i];
                    if (element17 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(127, element17);
                    }
                    i++;
                }
            }
            DeviceMobilityStatePnoScanStats[] deviceMobilityStatePnoScanStatsArr = this.mobilityStatePnoStatsList;
            if (deviceMobilityStatePnoScanStatsArr != null && deviceMobilityStatePnoScanStatsArr.length > 0) {
                i = 0;
                while (true) {
                    DeviceMobilityStatePnoScanStats element18 = this.mobilityStatePnoStatsList;
                    if (i >= element18.length) {
                        break;
                    }
                    element18 = element18[i];
                    if (element18 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(128, element18);
                    }
                    i++;
                }
            }
            WifiP2pStats wifiP2pStats = this.wifiP2PStats;
            if (wifiP2pStats != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(129, wifiP2pStats);
            }
            WifiDppLog wifiDppLog = this.wifiDppLog;
            if (wifiDppLog != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(130, wifiDppLog);
            }
            i = this.numEnhancedOpenNetworks;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(131, i);
            }
            i = this.numWpa3PersonalNetworks;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(132, i);
            }
            i = this.numWpa3EnterpriseNetworks;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(133, i);
            }
            i = this.numEnhancedOpenNetworkScanResults;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(134, i);
            }
            i = this.numWpa3PersonalNetworkScanResults;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(135, i);
            }
            i = this.numWpa3EnterpriseNetworkScanResults;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(136, i);
            }
            WifiConfigStoreIO wifiConfigStoreIO = this.wifiConfigStoreIo;
            if (wifiConfigStoreIO != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(137, wifiConfigStoreIO);
            }
            i = this.numSavedNetworksWithMacRandomization;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(138, i);
            }
            LinkProbeStats linkProbeStats = this.linkProbeStats;
            if (linkProbeStats != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(139, linkProbeStats);
            }
            NetworkSelectionExperimentDecisions[] networkSelectionExperimentDecisionsArr = this.networkSelectionExperimentDecisionsList;
            if (networkSelectionExperimentDecisionsArr != null && networkSelectionExperimentDecisionsArr.length > 0) {
                i = 0;
                while (true) {
                    NetworkSelectionExperimentDecisions element19 = this.networkSelectionExperimentDecisionsList;
                    if (i >= element19.length) {
                        break;
                    }
                    element19 = element19[i];
                    if (element19 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(140, element19);
                    }
                    i++;
                }
            }
            WifiNetworkRequestApiLog wifiNetworkRequestApiLog = this.wifiNetworkRequestApiLog;
            if (wifiNetworkRequestApiLog != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(141, wifiNetworkRequestApiLog);
            }
            WifiNetworkSuggestionApiLog wifiNetworkSuggestionApiLog = this.wifiNetworkSuggestionApiLog;
            if (wifiNetworkSuggestionApiLog != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(142, wifiNetworkSuggestionApiLog);
            }
            WifiLockStats wifiLockStats = this.wifiLockStats;
            if (wifiLockStats != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(143, wifiLockStats);
            }
            WifiToggleStats wifiToggleStats = this.wifiToggleStats;
            if (wifiToggleStats != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(144, wifiToggleStats);
            }
            i = this.numAddOrUpdateNetworkCalls;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(145, i);
            }
            i = this.numEnableNetworkCalls;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(146, i);
            }
            PasspointProvisionStats passpointProvisionStats = this.passpointProvisionStats;
            if (passpointProvisionStats != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(147, passpointProvisionStats);
            }
            passpointProfileTypeCountArr = this.installedPasspointProfileTypeForR2;
            if (passpointProfileTypeCountArr != null && passpointProfileTypeCountArr.length > 0) {
                i = 0;
                while (true) {
                    PasspointProfileTypeCount element20 = this.installedPasspointProfileTypeForR2;
                    if (i >= element20.length) {
                        break;
                    }
                    element20 = element20[i];
                    if (element20 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(148, element20);
                    }
                    i++;
                }
            }
            return size;
        }

        public WifiLog mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                int arrayLength;
                int i;
                ScanReturnEntry[] scanReturnEntryArr;
                ScanReturnEntry[] newArray;
                WifiSystemStateEntry[] wifiSystemStateEntryArr;
                WifiSystemStateEntry[] newArray2;
                RssiPollCount[] rssiPollCountArr;
                RssiPollCount[] newArray3;
                NumConnectableNetworksBucket[] numConnectableNetworksBucketArr;
                NumConnectableNetworksBucket[] newArray4;
                ConnectToNetworkNotificationAndActionCount[] connectToNetworkNotificationAndActionCountArr;
                ConnectToNetworkNotificationAndActionCount[] newArray5;
                SoftApConnectedClientsEvent[] softApConnectedClientsEventArr;
                SoftApConnectedClientsEvent[] newArray6;
                PasspointProfileTypeCount[] passpointProfileTypeCountArr;
                PasspointProfileTypeCount[] newArray7;
                switch (tag) {
                    case 0:
                        return this;
                    case 10:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 10);
                        ConnectionEvent[] connectionEventArr = this.connectionEvent;
                        i = connectionEventArr == null ? 0 : connectionEventArr.length;
                        ConnectionEvent[] newArray8 = new ConnectionEvent[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.connectionEvent, 0, newArray8, 0, i);
                        }
                        while (i < newArray8.length - 1) {
                            newArray8[i] = new ConnectionEvent();
                            input.readMessage(newArray8[i]);
                            input.readTag();
                            i++;
                        }
                        newArray8[i] = new ConnectionEvent();
                        input.readMessage(newArray8[i]);
                        this.connectionEvent = newArray8;
                        break;
                    case 16:
                        this.numSavedNetworks = input.readInt32();
                        break;
                    case 24:
                        this.numOpenNetworks = input.readInt32();
                        break;
                    case 32:
                        this.numLegacyPersonalNetworks = input.readInt32();
                        break;
                    case 40:
                        this.numLegacyEnterpriseNetworks = input.readInt32();
                        break;
                    case 48:
                        this.isLocationEnabled = input.readBool();
                        break;
                    case 56:
                        this.isScanningAlwaysEnabled = input.readBool();
                        break;
                    case 64:
                        this.numWifiToggledViaSettings = input.readInt32();
                        break;
                    case 72:
                        this.numWifiToggledViaAirplane = input.readInt32();
                        break;
                    case 80:
                        this.numNetworksAddedByUser = input.readInt32();
                        break;
                    case 88:
                        this.numNetworksAddedByApps = input.readInt32();
                        break;
                    case 96:
                        this.numEmptyScanResults = input.readInt32();
                        break;
                    case 104:
                        this.numNonEmptyScanResults = input.readInt32();
                        break;
                    case 112:
                        this.numOneshotScans = input.readInt32();
                        break;
                    case 120:
                        this.numBackgroundScans = input.readInt32();
                        break;
                    case 130:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 130);
                        scanReturnEntryArr = this.scanReturnEntries;
                        i = scanReturnEntryArr == null ? 0 : scanReturnEntryArr.length;
                        newArray = new ScanReturnEntry[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.scanReturnEntries, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new ScanReturnEntry();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new ScanReturnEntry();
                        input.readMessage(newArray[i]);
                        this.scanReturnEntries = newArray;
                        break;
                    case 138:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 138);
                        wifiSystemStateEntryArr = this.wifiSystemStateEntries;
                        i = wifiSystemStateEntryArr == null ? 0 : wifiSystemStateEntryArr.length;
                        newArray2 = new WifiSystemStateEntry[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.wifiSystemStateEntries, 0, newArray2, 0, i);
                        }
                        while (i < newArray2.length - 1) {
                            newArray2[i] = new WifiSystemStateEntry();
                            input.readMessage(newArray2[i]);
                            input.readTag();
                            i++;
                        }
                        newArray2[i] = new WifiSystemStateEntry();
                        input.readMessage(newArray2[i]);
                        this.wifiSystemStateEntries = newArray2;
                        break;
                    case 146:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 146);
                        scanReturnEntryArr = this.backgroundScanReturnEntries;
                        i = scanReturnEntryArr == null ? 0 : scanReturnEntryArr.length;
                        newArray = new ScanReturnEntry[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.backgroundScanReturnEntries, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new ScanReturnEntry();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new ScanReturnEntry();
                        input.readMessage(newArray[i]);
                        this.backgroundScanReturnEntries = newArray;
                        break;
                    case 154:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 154);
                        wifiSystemStateEntryArr = this.backgroundScanRequestState;
                        i = wifiSystemStateEntryArr == null ? 0 : wifiSystemStateEntryArr.length;
                        newArray2 = new WifiSystemStateEntry[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.backgroundScanRequestState, 0, newArray2, 0, i);
                        }
                        while (i < newArray2.length - 1) {
                            newArray2[i] = new WifiSystemStateEntry();
                            input.readMessage(newArray2[i]);
                            input.readTag();
                            i++;
                        }
                        newArray2[i] = new WifiSystemStateEntry();
                        input.readMessage(newArray2[i]);
                        this.backgroundScanRequestState = newArray2;
                        break;
                    case 160:
                        this.numLastResortWatchdogTriggers = input.readInt32();
                        break;
                    case 168:
                        this.numLastResortWatchdogBadAssociationNetworksTotal = input.readInt32();
                        break;
                    case 176:
                        this.numLastResortWatchdogBadAuthenticationNetworksTotal = input.readInt32();
                        break;
                    case 184:
                        this.numLastResortWatchdogBadDhcpNetworksTotal = input.readInt32();
                        break;
                    case 192:
                        this.numLastResortWatchdogBadOtherNetworksTotal = input.readInt32();
                        break;
                    case 200:
                        this.numLastResortWatchdogAvailableNetworksTotal = input.readInt32();
                        break;
                    case 208:
                        this.numLastResortWatchdogTriggersWithBadAssociation = input.readInt32();
                        break;
                    case 216:
                        this.numLastResortWatchdogTriggersWithBadAuthentication = input.readInt32();
                        break;
                    case 224:
                        this.numLastResortWatchdogTriggersWithBadDhcp = input.readInt32();
                        break;
                    case 232:
                        this.numLastResortWatchdogTriggersWithBadOther = input.readInt32();
                        break;
                    case 240:
                        this.numConnectivityWatchdogPnoGood = input.readInt32();
                        break;
                    case 248:
                        this.numConnectivityWatchdogPnoBad = input.readInt32();
                        break;
                    case 256:
                        this.numConnectivityWatchdogBackgroundGood = input.readInt32();
                        break;
                    case 264:
                        this.numConnectivityWatchdogBackgroundBad = input.readInt32();
                        break;
                    case 272:
                        this.recordDurationSec = input.readInt32();
                        break;
                    case 282:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 282);
                        rssiPollCountArr = this.rssiPollRssiCount;
                        i = rssiPollCountArr == null ? 0 : rssiPollCountArr.length;
                        newArray3 = new RssiPollCount[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.rssiPollRssiCount, 0, newArray3, 0, i);
                        }
                        while (i < newArray3.length - 1) {
                            newArray3[i] = new RssiPollCount();
                            input.readMessage(newArray3[i]);
                            input.readTag();
                            i++;
                        }
                        newArray3[i] = new RssiPollCount();
                        input.readMessage(newArray3[i]);
                        this.rssiPollRssiCount = newArray3;
                        break;
                    case 288:
                        this.numLastResortWatchdogSuccesses = input.readInt32();
                        break;
                    case 296:
                        this.numHiddenNetworks = input.readInt32();
                        break;
                    case 304:
                        this.numPasspointNetworks = input.readInt32();
                        break;
                    case 312:
                        this.numTotalScanResults = input.readInt32();
                        break;
                    case 320:
                        this.numOpenNetworkScanResults = input.readInt32();
                        break;
                    case 328:
                        this.numLegacyPersonalNetworkScanResults = input.readInt32();
                        break;
                    case 336:
                        this.numLegacyEnterpriseNetworkScanResults = input.readInt32();
                        break;
                    case 344:
                        this.numHiddenNetworkScanResults = input.readInt32();
                        break;
                    case 352:
                        this.numHotspot2R1NetworkScanResults = input.readInt32();
                        break;
                    case 360:
                        this.numHotspot2R2NetworkScanResults = input.readInt32();
                        break;
                    case 368:
                        this.numScans = input.readInt32();
                        break;
                    case 378:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 378);
                        AlertReasonCount[] alertReasonCountArr = this.alertReasonCount;
                        i = alertReasonCountArr == null ? 0 : alertReasonCountArr.length;
                        AlertReasonCount[] newArray9 = new AlertReasonCount[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.alertReasonCount, 0, newArray9, 0, i);
                        }
                        while (i < newArray9.length - 1) {
                            newArray9[i] = new AlertReasonCount();
                            input.readMessage(newArray9[i]);
                            input.readTag();
                            i++;
                        }
                        newArray9[i] = new AlertReasonCount();
                        input.readMessage(newArray9[i]);
                        this.alertReasonCount = newArray9;
                        break;
                    case 386:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 386);
                        WifiScoreCount[] wifiScoreCountArr = this.wifiScoreCount;
                        i = wifiScoreCountArr == null ? 0 : wifiScoreCountArr.length;
                        WifiScoreCount[] newArray10 = new WifiScoreCount[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.wifiScoreCount, 0, newArray10, 0, i);
                        }
                        while (i < newArray10.length - 1) {
                            newArray10[i] = new WifiScoreCount();
                            input.readMessage(newArray10[i]);
                            input.readTag();
                            i++;
                        }
                        newArray10[i] = new WifiScoreCount();
                        input.readMessage(newArray10[i]);
                        this.wifiScoreCount = newArray10;
                        break;
                    case 394:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 394);
                        SoftApDurationBucket[] softApDurationBucketArr = this.softApDuration;
                        i = softApDurationBucketArr == null ? 0 : softApDurationBucketArr.length;
                        SoftApDurationBucket[] newArray11 = new SoftApDurationBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.softApDuration, 0, newArray11, 0, i);
                        }
                        while (i < newArray11.length - 1) {
                            newArray11[i] = new SoftApDurationBucket();
                            input.readMessage(newArray11[i]);
                            input.readTag();
                            i++;
                        }
                        newArray11[i] = new SoftApDurationBucket();
                        input.readMessage(newArray11[i]);
                        this.softApDuration = newArray11;
                        break;
                    case 402:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 402);
                        SoftApReturnCodeCount[] softApReturnCodeCountArr = this.softApReturnCode;
                        i = softApReturnCodeCountArr == null ? 0 : softApReturnCodeCountArr.length;
                        SoftApReturnCodeCount[] newArray12 = new SoftApReturnCodeCount[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.softApReturnCode, 0, newArray12, 0, i);
                        }
                        while (i < newArray12.length - 1) {
                            newArray12[i] = new SoftApReturnCodeCount();
                            input.readMessage(newArray12[i]);
                            input.readTag();
                            i++;
                        }
                        newArray12[i] = new SoftApReturnCodeCount();
                        input.readMessage(newArray12[i]);
                        this.softApReturnCode = newArray12;
                        break;
                    case 410:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 410);
                        rssiPollCountArr = this.rssiPollDeltaCount;
                        i = rssiPollCountArr == null ? 0 : rssiPollCountArr.length;
                        newArray3 = new RssiPollCount[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.rssiPollDeltaCount, 0, newArray3, 0, i);
                        }
                        while (i < newArray3.length - 1) {
                            newArray3[i] = new RssiPollCount();
                            input.readMessage(newArray3[i]);
                            input.readTag();
                            i++;
                        }
                        newArray3[i] = new RssiPollCount();
                        input.readMessage(newArray3[i]);
                        this.rssiPollDeltaCount = newArray3;
                        break;
                    case IFAAFaceManager.STATUS_FACE_HACKER /*418*/:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, IFAAFaceManager.STATUS_FACE_HACKER);
                        StaEvent[] staEventArr = this.staEventList;
                        i = staEventArr == null ? 0 : staEventArr.length;
                        StaEvent[] newArray13 = new StaEvent[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.staEventList, 0, newArray13, 0, i);
                        }
                        while (i < newArray13.length - 1) {
                            newArray13[i] = new StaEvent();
                            input.readMessage(newArray13[i]);
                            input.readTag();
                            i++;
                        }
                        newArray13[i] = new StaEvent();
                        input.readMessage(newArray13[i]);
                        this.staEventList = newArray13;
                        break;
                    case 424:
                        this.numHalCrashes = input.readInt32();
                        break;
                    case DevicePolicyManager.PROFILE_KEYGUARD_FEATURES_AFFECT_OWNER /*432*/:
                        this.numWificondCrashes = input.readInt32();
                        break;
                    case 440:
                        this.numSetupClientInterfaceFailureDueToHal = input.readInt32();
                        break;
                    case 448:
                        this.numSetupClientInterfaceFailureDueToWificond = input.readInt32();
                        break;
                    case 458:
                        if (this.wifiAwareLog == null) {
                            this.wifiAwareLog = new WifiAwareLog();
                        }
                        input.readMessage(this.wifiAwareLog);
                        break;
                    case MetricsEvent.ACTION_DELETION_APPS_COLLAPSED /*464*/:
                        this.numPasspointProviders = input.readInt32();
                        break;
                    case MetricsEvent.ACTION_DELETION_HELPER_DOWNLOADS_DELETION_FAIL /*472*/:
                        this.numPasspointProviderInstallation = input.readInt32();
                        break;
                    case 480:
                        this.numPasspointProviderInstallSuccess = input.readInt32();
                        break;
                    case 488:
                        this.numPasspointProviderUninstallation = input.readInt32();
                        break;
                    case 496:
                        this.numPasspointProviderUninstallSuccess = input.readInt32();
                        break;
                    case 504:
                        this.numPasspointProvidersSuccessfullyConnected = input.readInt32();
                        break;
                    case 514:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 514);
                        numConnectableNetworksBucketArr = this.totalSsidsInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.totalSsidsInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.totalSsidsInScanHistogram = newArray4;
                        break;
                    case 522:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 522);
                        numConnectableNetworksBucketArr = this.totalBssidsInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.totalBssidsInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.totalBssidsInScanHistogram = newArray4;
                        break;
                    case 530:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 530);
                        numConnectableNetworksBucketArr = this.availableOpenSsidsInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.availableOpenSsidsInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.availableOpenSsidsInScanHistogram = newArray4;
                        break;
                    case 538:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 538);
                        numConnectableNetworksBucketArr = this.availableOpenBssidsInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.availableOpenBssidsInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.availableOpenBssidsInScanHistogram = newArray4;
                        break;
                    case 546:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 546);
                        numConnectableNetworksBucketArr = this.availableSavedSsidsInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.availableSavedSsidsInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.availableSavedSsidsInScanHistogram = newArray4;
                        break;
                    case 554:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 554);
                        numConnectableNetworksBucketArr = this.availableSavedBssidsInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.availableSavedBssidsInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.availableSavedBssidsInScanHistogram = newArray4;
                        break;
                    case 562:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 562);
                        numConnectableNetworksBucketArr = this.availableOpenOrSavedSsidsInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.availableOpenOrSavedSsidsInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.availableOpenOrSavedSsidsInScanHistogram = newArray4;
                        break;
                    case 570:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 570);
                        numConnectableNetworksBucketArr = this.availableOpenOrSavedBssidsInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.availableOpenOrSavedBssidsInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.availableOpenOrSavedBssidsInScanHistogram = newArray4;
                        break;
                    case 578:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 578);
                        numConnectableNetworksBucketArr = this.availableSavedPasspointProviderProfilesInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.availableSavedPasspointProviderProfilesInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.availableSavedPasspointProviderProfilesInScanHistogram = newArray4;
                        break;
                    case 586:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 586);
                        numConnectableNetworksBucketArr = this.availableSavedPasspointProviderBssidsInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.availableSavedPasspointProviderBssidsInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.availableSavedPasspointProviderBssidsInScanHistogram = newArray4;
                        break;
                    case 592:
                        this.fullBandAllSingleScanListenerResults = input.readInt32();
                        break;
                    case 600:
                        this.partialAllSingleScanListenerResults = input.readInt32();
                        break;
                    case MetricsEvent.PROVISIONING_NETWORK_TYPE /*610*/:
                        if (this.pnoScanMetrics == null) {
                            this.pnoScanMetrics = new PnoScanMetrics();
                        }
                        input.readMessage(this.pnoScanMetrics);
                        break;
                    case MetricsEvent.PROVISIONING_ENTRY_POINT_TRUSTED_SOURCE /*618*/:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsEvent.PROVISIONING_ENTRY_POINT_TRUSTED_SOURCE);
                        connectToNetworkNotificationAndActionCountArr = this.connectToNetworkNotificationCount;
                        i = connectToNetworkNotificationAndActionCountArr == null ? 0 : connectToNetworkNotificationAndActionCountArr.length;
                        newArray5 = new ConnectToNetworkNotificationAndActionCount[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.connectToNetworkNotificationCount, 0, newArray5, 0, i);
                        }
                        while (i < newArray5.length - 1) {
                            newArray5[i] = new ConnectToNetworkNotificationAndActionCount();
                            input.readMessage(newArray5[i]);
                            input.readTag();
                            i++;
                        }
                        newArray5[i] = new ConnectToNetworkNotificationAndActionCount();
                        input.readMessage(newArray5[i]);
                        this.connectToNetworkNotificationCount = newArray5;
                        break;
                    case MetricsEvent.PROVISIONING_COPY_ACCOUNT_STATUS /*626*/:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsEvent.PROVISIONING_COPY_ACCOUNT_STATUS);
                        connectToNetworkNotificationAndActionCountArr = this.connectToNetworkNotificationActionCount;
                        i = connectToNetworkNotificationAndActionCountArr == null ? 0 : connectToNetworkNotificationAndActionCountArr.length;
                        newArray5 = new ConnectToNetworkNotificationAndActionCount[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.connectToNetworkNotificationActionCount, 0, newArray5, 0, i);
                        }
                        while (i < newArray5.length - 1) {
                            newArray5[i] = new ConnectToNetworkNotificationAndActionCount();
                            input.readMessage(newArray5[i]);
                            input.readTag();
                            i++;
                        }
                        newArray5[i] = new ConnectToNetworkNotificationAndActionCount();
                        input.readMessage(newArray5[i]);
                        this.connectToNetworkNotificationActionCount = newArray5;
                        break;
                    case MetricsEvent.ACTION_PERMISSION_DENIED_UNKNOWN /*632*/:
                        this.openNetworkRecommenderBlacklistSize = input.readInt32();
                        break;
                    case 640:
                        this.isWifiNetworksAvailableNotificationOn = input.readBool();
                        break;
                    case MetricsEvent.ACTION_PERMISSION_DENIED_READ_CONTACTS /*648*/:
                        this.numOpenNetworkRecommendationUpdates = input.readInt32();
                        break;
                    case MetricsEvent.ACTION_PERMISSION_DENIED_GET_ACCOUNTS /*656*/:
                        this.numOpenNetworkConnectMessageFailedToSend = input.readInt32();
                        break;
                    case MetricsEvent.ACTION_PERMISSION_REQUEST_RECORD_AUDIO /*666*/:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsEvent.ACTION_PERMISSION_REQUEST_RECORD_AUDIO);
                        numConnectableNetworksBucketArr = this.observedHotspotR1ApsInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.observedHotspotR1ApsInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.observedHotspotR1ApsInScanHistogram = newArray4;
                        break;
                    case MetricsEvent.ACTION_PERMISSION_REQUEST_CALL_PHONE /*674*/:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsEvent.ACTION_PERMISSION_REQUEST_CALL_PHONE);
                        numConnectableNetworksBucketArr = this.observedHotspotR2ApsInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.observedHotspotR2ApsInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.observedHotspotR2ApsInScanHistogram = newArray4;
                        break;
                    case MetricsEvent.ACTION_PERMISSION_REQUEST_WRITE_CALL_LOG /*682*/:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsEvent.ACTION_PERMISSION_REQUEST_WRITE_CALL_LOG);
                        numConnectableNetworksBucketArr = this.observedHotspotR1EssInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.observedHotspotR1EssInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.observedHotspotR1EssInScanHistogram = newArray4;
                        break;
                    case MetricsEvent.ACTION_PERMISSION_REQUEST_USE_SIP /*690*/:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsEvent.ACTION_PERMISSION_REQUEST_USE_SIP);
                        numConnectableNetworksBucketArr = this.observedHotspotR2EssInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.observedHotspotR2EssInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.observedHotspotR2EssInScanHistogram = newArray4;
                        break;
                    case MetricsEvent.ACTION_PERMISSION_REQUEST_READ_CELL_BROADCASTS /*698*/:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsEvent.ACTION_PERMISSION_REQUEST_READ_CELL_BROADCASTS);
                        numConnectableNetworksBucketArr = this.observedHotspotR1ApsPerEssInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.observedHotspotR1ApsPerEssInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.observedHotspotR1ApsPerEssInScanHistogram = newArray4;
                        break;
                    case MetricsEvent.ACTION_PERMISSION_REQUEST_SEND_SMS /*706*/:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsEvent.ACTION_PERMISSION_REQUEST_SEND_SMS);
                        numConnectableNetworksBucketArr = this.observedHotspotR2ApsPerEssInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.observedHotspotR2ApsPerEssInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.observedHotspotR2ApsPerEssInScanHistogram = newArray4;
                        break;
                    case MetricsEvent.ACTION_PERMISSION_REQUEST_READ_SMS /*714*/:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsEvent.ACTION_PERMISSION_REQUEST_READ_SMS);
                        softApConnectedClientsEventArr = this.softApConnectedClientsEventsTethered;
                        i = softApConnectedClientsEventArr == null ? 0 : softApConnectedClientsEventArr.length;
                        newArray6 = new SoftApConnectedClientsEvent[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.softApConnectedClientsEventsTethered, 0, newArray6, 0, i);
                        }
                        while (i < newArray6.length - 1) {
                            newArray6[i] = new SoftApConnectedClientsEvent();
                            input.readMessage(newArray6[i]);
                            input.readTag();
                            i++;
                        }
                        newArray6[i] = new SoftApConnectedClientsEvent();
                        input.readMessage(newArray6[i]);
                        this.softApConnectedClientsEventsTethered = newArray6;
                        break;
                    case MetricsEvent.ACTION_PERMISSION_REQUEST_RECEIVE_MMS /*722*/:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsEvent.ACTION_PERMISSION_REQUEST_RECEIVE_MMS);
                        softApConnectedClientsEventArr = this.softApConnectedClientsEventsLocalOnly;
                        i = softApConnectedClientsEventArr == null ? 0 : softApConnectedClientsEventArr.length;
                        newArray6 = new SoftApConnectedClientsEvent[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.softApConnectedClientsEventsLocalOnly, 0, newArray6, 0, i);
                        }
                        while (i < newArray6.length - 1) {
                            newArray6[i] = new SoftApConnectedClientsEvent();
                            input.readMessage(newArray6[i]);
                            input.readTag();
                            i++;
                        }
                        newArray6[i] = new SoftApConnectedClientsEvent();
                        input.readMessage(newArray6[i]);
                        this.softApConnectedClientsEventsLocalOnly = newArray6;
                        break;
                    case MetricsEvent.ACTION_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE /*730*/:
                        if (this.wpsMetrics == null) {
                            this.wpsMetrics = new WpsMetrics();
                        }
                        input.readMessage(this.wpsMetrics);
                        break;
                    case MetricsEvent.ACTION_PERMISSION_DENIED_READ_PHONE_NUMBERS /*738*/:
                        if (this.wifiPowerStats == null) {
                            this.wifiPowerStats = new WifiPowerStats();
                        }
                        input.readMessage(this.wifiPowerStats);
                        break;
                    case 744:
                        this.numConnectivityOneshotScans = input.readInt32();
                        break;
                    case 754:
                        if (this.wifiWakeStats == null) {
                            this.wifiWakeStats = new WifiWakeStats();
                        }
                        input.readMessage(this.wifiWakeStats);
                        break;
                    case MetricsEvent.ACTION_LEAVE_SEARCH_RESULT_WITHOUT_QUERY /*762*/:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsEvent.ACTION_LEAVE_SEARCH_RESULT_WITHOUT_QUERY);
                        numConnectableNetworksBucketArr = this.observed80211McSupportingApsInScanHistogram;
                        i = numConnectableNetworksBucketArr == null ? 0 : numConnectableNetworksBucketArr.length;
                        newArray4 = new NumConnectableNetworksBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.observed80211McSupportingApsInScanHistogram, 0, newArray4, 0, i);
                        }
                        while (i < newArray4.length - 1) {
                            newArray4[i] = new NumConnectableNetworksBucket();
                            input.readMessage(newArray4[i]);
                            input.readTag();
                            i++;
                        }
                        newArray4[i] = new NumConnectableNetworksBucket();
                        input.readMessage(newArray4[i]);
                        this.observed80211McSupportingApsInScanHistogram = newArray4;
                        break;
                    case 768:
                        this.numSupplicantCrashes = input.readInt32();
                        break;
                    case 776:
                        this.numHostapdCrashes = input.readInt32();
                        break;
                    case 784:
                        this.numSetupClientInterfaceFailureDueToSupplicant = input.readInt32();
                        break;
                    case 792:
                        this.numSetupSoftApInterfaceFailureDueToHal = input.readInt32();
                        break;
                    case 800:
                        this.numSetupSoftApInterfaceFailureDueToWificond = input.readInt32();
                        break;
                    case 808:
                        this.numSetupSoftApInterfaceFailureDueToHostapd = input.readInt32();
                        break;
                    case 816:
                        this.numClientInterfaceDown = input.readInt32();
                        break;
                    case 824:
                        this.numSoftApInterfaceDown = input.readInt32();
                        break;
                    case MetricsEvent.NOTIFICATION_SNOOZED_CRITERIA /*832*/:
                        this.numExternalAppOneshotScanRequests = input.readInt32();
                        break;
                    case 840:
                        this.numExternalForegroundAppOneshotScanRequestsThrottled = input.readInt32();
                        break;
                    case MetricsEvent.FIELD_SETTINGS_BUILD_NUMBER_DEVELOPER_MODE_ENABLED /*848*/:
                        this.numExternalBackgroundAppOneshotScanRequestsThrottled = input.readInt32();
                        break;
                    case MetricsEvent.ACTION_NOTIFICATION_CHANNEL /*856*/:
                        this.watchdogTriggerToConnectionSuccessDurationMs = input.readInt64();
                        break;
                    case MetricsEvent.ACTION_GET_CONTACT /*864*/:
                        this.watchdogTotalConnectionFailureCountAfterTrigger = input.readInt64();
                        break;
                    case 872:
                        this.numOneshotHasDfsChannelScans = input.readInt32();
                        break;
                    case 882:
                        if (this.wifiRttLog == null) {
                            this.wifiRttLog = new WifiRttLog();
                        }
                        input.readMessage(this.wifiRttLog);
                        break;
                    case MetricsEvent.ACTION_APPOP_GRANT_SYSTEM_ALERT_WINDOW /*888*/:
                        this.isMacRandomizationOn = input.readBool();
                        break;
                    case 896:
                        this.numRadioModeChangeToMcc = input.readInt32();
                        break;
                    case MetricsEvent.APP_TRANSITION_CALLING_PACKAGE_NAME /*904*/:
                        this.numRadioModeChangeToScc = input.readInt32();
                        break;
                    case MetricsEvent.AUTOFILL_AUTHENTICATED /*912*/:
                        this.numRadioModeChangeToSbs = input.readInt32();
                        break;
                    case MetricsEvent.METRICS_CHECKPOINT /*920*/:
                        this.numRadioModeChangeToDbs = input.readInt32();
                        break;
                    case MetricsEvent.FIELD_QS_VALUE /*928*/:
                        this.numSoftApUserBandPreferenceUnsatisfied = input.readInt32();
                        break;
                    case 938:
                        this.scoreExperimentId = input.readString();
                        break;
                    case MetricsEvent.FIELD_NOTIFICATION_GROUP_ID /*946*/:
                        if (this.wifiRadioUsage == null) {
                            this.wifiRadioUsage = new WifiRadioUsage();
                        }
                        input.readMessage(this.wifiRadioUsage);
                        break;
                    case 954:
                        if (this.experimentValues == null) {
                            this.experimentValues = new ExperimentValues();
                        }
                        input.readMessage(this.experimentValues);
                        break;
                    case 962:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 962);
                        WifiIsUnusableEvent[] wifiIsUnusableEventArr = this.wifiIsUnusableEventList;
                        i = wifiIsUnusableEventArr == null ? 0 : wifiIsUnusableEventArr.length;
                        WifiIsUnusableEvent[] newArray14 = new WifiIsUnusableEvent[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.wifiIsUnusableEventList, 0, newArray14, 0, i);
                        }
                        while (i < newArray14.length - 1) {
                            newArray14[i] = new WifiIsUnusableEvent();
                            input.readMessage(newArray14[i]);
                            input.readTag();
                            i++;
                        }
                        newArray14[i] = new WifiIsUnusableEvent();
                        input.readMessage(newArray14[i]);
                        this.wifiIsUnusableEventList = newArray14;
                        break;
                    case 970:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 970);
                        LinkSpeedCount[] linkSpeedCountArr = this.linkSpeedCounts;
                        i = linkSpeedCountArr == null ? 0 : linkSpeedCountArr.length;
                        LinkSpeedCount[] newArray15 = new LinkSpeedCount[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.linkSpeedCounts, 0, newArray15, 0, i);
                        }
                        while (i < newArray15.length - 1) {
                            newArray15[i] = new LinkSpeedCount();
                            input.readMessage(newArray15[i]);
                            input.readTag();
                            i++;
                        }
                        newArray15[i] = new LinkSpeedCount();
                        input.readMessage(newArray15[i]);
                        this.linkSpeedCounts = newArray15;
                        break;
                    case 976:
                        this.numSarSensorRegistrationFailures = input.readInt32();
                        break;
                    case MetricsEvent.SETTINGS_GESTURE_CAMERA_LIFT_TRIGGER /*986*/:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsEvent.SETTINGS_GESTURE_CAMERA_LIFT_TRIGGER);
                        passpointProfileTypeCountArr = this.installedPasspointProfileTypeForR1;
                        i = passpointProfileTypeCountArr == null ? 0 : passpointProfileTypeCountArr.length;
                        newArray7 = new PasspointProfileTypeCount[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.installedPasspointProfileTypeForR1, 0, newArray7, 0, i);
                        }
                        while (i < newArray7.length - 1) {
                            newArray7[i] = new PasspointProfileTypeCount();
                            input.readMessage(newArray7[i]);
                            input.readTag();
                            i++;
                        }
                        newArray7[i] = new PasspointProfileTypeCount();
                        input.readMessage(newArray7[i]);
                        this.installedPasspointProfileTypeForR1 = newArray7;
                        break;
                    case MetricsEvent.FIELD_SETTINGS_PREFERENCE_CHANGE_LONG_VALUE /*994*/:
                        this.hardwareRevision = input.readString();
                        break;
                    case 1002:
                        if (this.wifiLinkLayerUsageStats == null) {
                            this.wifiLinkLayerUsageStats = new WifiLinkLayerUsageStats();
                        }
                        input.readMessage(this.wifiLinkLayerUsageStats);
                        break;
                    case 1010:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 1010);
                        WifiUsabilityStats[] wifiUsabilityStatsArr = this.wifiUsabilityStatsList;
                        i = wifiUsabilityStatsArr == null ? 0 : wifiUsabilityStatsArr.length;
                        WifiUsabilityStats[] newArray16 = new WifiUsabilityStats[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.wifiUsabilityStatsList, 0, newArray16, 0, i);
                        }
                        while (i < newArray16.length - 1) {
                            newArray16[i] = new WifiUsabilityStats();
                            input.readMessage(newArray16[i]);
                            input.readTag();
                            i++;
                        }
                        newArray16[i] = new WifiUsabilityStats();
                        input.readMessage(newArray16[i]);
                        this.wifiUsabilityStatsList = newArray16;
                        break;
                    case 1018:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 1018);
                        WifiUsabilityScoreCount[] wifiUsabilityScoreCountArr = this.wifiUsabilityScoreCount;
                        i = wifiUsabilityScoreCountArr == null ? 0 : wifiUsabilityScoreCountArr.length;
                        WifiUsabilityScoreCount[] newArray17 = new WifiUsabilityScoreCount[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.wifiUsabilityScoreCount, 0, newArray17, 0, i);
                        }
                        while (i < newArray17.length - 1) {
                            newArray17[i] = new WifiUsabilityScoreCount();
                            input.readMessage(newArray17[i]);
                            input.readTag();
                            i++;
                        }
                        newArray17[i] = new WifiUsabilityScoreCount();
                        input.readMessage(newArray17[i]);
                        this.wifiUsabilityScoreCount = newArray17;
                        break;
                    case 1026:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 1026);
                        DeviceMobilityStatePnoScanStats[] deviceMobilityStatePnoScanStatsArr = this.mobilityStatePnoStatsList;
                        i = deviceMobilityStatePnoScanStatsArr == null ? 0 : deviceMobilityStatePnoScanStatsArr.length;
                        DeviceMobilityStatePnoScanStats[] newArray18 = new DeviceMobilityStatePnoScanStats[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.mobilityStatePnoStatsList, 0, newArray18, 0, i);
                        }
                        while (i < newArray18.length - 1) {
                            newArray18[i] = new DeviceMobilityStatePnoScanStats();
                            input.readMessage(newArray18[i]);
                            input.readTag();
                            i++;
                        }
                        newArray18[i] = new DeviceMobilityStatePnoScanStats();
                        input.readMessage(newArray18[i]);
                        this.mobilityStatePnoStatsList = newArray18;
                        break;
                    case 1034:
                        if (this.wifiP2PStats == null) {
                            this.wifiP2PStats = new WifiP2pStats();
                        }
                        input.readMessage(this.wifiP2PStats);
                        break;
                    case RILConstants.RIL_UNSOL_RADIO_CAPABILITY /*1042*/:
                        if (this.wifiDppLog == null) {
                            this.wifiDppLog = new WifiDppLog();
                        }
                        input.readMessage(this.wifiDppLog);
                        break;
                    case 1048:
                        this.numEnhancedOpenNetworks = input.readInt32();
                        break;
                    case 1056:
                        this.numWpa3PersonalNetworks = input.readInt32();
                        break;
                    case 1064:
                        this.numWpa3EnterpriseNetworks = input.readInt32();
                        break;
                    case 1072:
                        this.numEnhancedOpenNetworkScanResults = input.readInt32();
                        break;
                    case 1080:
                        this.numWpa3PersonalNetworkScanResults = input.readInt32();
                        break;
                    case 1088:
                        this.numWpa3EnterpriseNetworkScanResults = input.readInt32();
                        break;
                    case MetricsEvent.ACTION_BOOT /*1098*/:
                        if (this.wifiConfigStoreIo == null) {
                            this.wifiConfigStoreIo = new WifiConfigStoreIO();
                        }
                        input.readMessage(this.wifiConfigStoreIo);
                        break;
                    case MetricsEvent.ACTION_TEXT_SELECTION_RESET /*1104*/:
                        this.numSavedNetworksWithMacRandomization = input.readInt32();
                        break;
                    case MetricsEvent.ACTION_TEXT_SELECTION_DRAG /*1114*/:
                        if (this.linkProbeStats == null) {
                            this.linkProbeStats = new LinkProbeStats();
                        }
                        input.readMessage(this.linkProbeStats);
                        break;
                    case MetricsEvent.FIELD_SELECTION_RANGE /*1122*/:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, MetricsEvent.FIELD_SELECTION_RANGE);
                        NetworkSelectionExperimentDecisions[] networkSelectionExperimentDecisionsArr = this.networkSelectionExperimentDecisionsList;
                        i = networkSelectionExperimentDecisionsArr == null ? 0 : networkSelectionExperimentDecisionsArr.length;
                        NetworkSelectionExperimentDecisions[] newArray19 = new NetworkSelectionExperimentDecisions[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.networkSelectionExperimentDecisionsList, 0, newArray19, 0, i);
                        }
                        while (i < newArray19.length - 1) {
                            newArray19[i] = new NetworkSelectionExperimentDecisions();
                            input.readMessage(newArray19[i]);
                            input.readTag();
                            i++;
                        }
                        newArray19[i] = new NetworkSelectionExperimentDecisions();
                        input.readMessage(newArray19[i]);
                        this.networkSelectionExperimentDecisionsList = newArray19;
                        break;
                    case MetricsEvent.FIELD_AUTOFILL_SAVE_TYPE /*1130*/:
                        if (this.wifiNetworkRequestApiLog == null) {
                            this.wifiNetworkRequestApiLog = new WifiNetworkRequestApiLog();
                        }
                        input.readMessage(this.wifiNetworkRequestApiLog);
                        break;
                    case MetricsEvent.NOTIFICATION_SELECT_SNOOZE /*1138*/:
                        if (this.wifiNetworkSuggestionApiLog == null) {
                            this.wifiNetworkSuggestionApiLog = new WifiNetworkSuggestionApiLog();
                        }
                        input.readMessage(this.wifiNetworkSuggestionApiLog);
                        break;
                    case 1146:
                        if (this.wifiLockStats == null) {
                            this.wifiLockStats = new WifiLockStats();
                        }
                        input.readMessage(this.wifiLockStats);
                        break;
                    case 1154:
                        if (this.wifiToggleStats == null) {
                            this.wifiToggleStats = new WifiToggleStats();
                        }
                        input.readMessage(this.wifiToggleStats);
                        break;
                    case 1160:
                        this.numAddOrUpdateNetworkCalls = input.readInt32();
                        break;
                    case 1168:
                        this.numEnableNetworkCalls = input.readInt32();
                        break;
                    case 1178:
                        if (this.passpointProvisionStats == null) {
                            this.passpointProvisionStats = new PasspointProvisionStats();
                        }
                        input.readMessage(this.passpointProvisionStats);
                        break;
                    case 1186:
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 1186);
                        passpointProfileTypeCountArr = this.installedPasspointProfileTypeForR2;
                        i = passpointProfileTypeCountArr == null ? 0 : passpointProfileTypeCountArr.length;
                        newArray7 = new PasspointProfileTypeCount[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.installedPasspointProfileTypeForR2, 0, newArray7, 0, i);
                        }
                        while (i < newArray7.length - 1) {
                            newArray7[i] = new PasspointProfileTypeCount();
                            input.readMessage(newArray7[i]);
                            input.readTag();
                            i++;
                        }
                        newArray7[i] = new PasspointProfileTypeCount();
                        input.readMessage(newArray7[i]);
                        this.installedPasspointProfileTypeForR2 = newArray7;
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        }
                        return this;
                }
            }
        }

        public static WifiLog parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiLog) MessageNano.mergeFrom(new WifiLog(), data);
        }

        public static WifiLog parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiLog().mergeFrom(input);
        }
    }

    public static final class WifiNetworkRequestApiLog extends MessageNano {
        private static volatile WifiNetworkRequestApiLog[] _emptyArray;
        public HistogramBucketInt32[] networkMatchSizeHistogram;
        public int numApps;
        public int numConnectSuccess;
        public int numRequest;
        public int numUserApprovalBypass;
        public int numUserReject;

        public static WifiNetworkRequestApiLog[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiNetworkRequestApiLog[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiNetworkRequestApiLog() {
            clear();
        }

        public WifiNetworkRequestApiLog clear() {
            this.numRequest = 0;
            this.networkMatchSizeHistogram = HistogramBucketInt32.emptyArray();
            this.numConnectSuccess = 0;
            this.numUserApprovalBypass = 0;
            this.numUserReject = 0;
            this.numApps = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.numRequest;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            HistogramBucketInt32[] histogramBucketInt32Arr = this.networkMatchSizeHistogram;
            if (histogramBucketInt32Arr != null && histogramBucketInt32Arr.length > 0) {
                i = 0;
                while (true) {
                    HistogramBucketInt32 element = this.networkMatchSizeHistogram;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        output.writeMessage(2, element);
                    }
                    i++;
                }
            }
            i = this.numConnectSuccess;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.numUserApprovalBypass;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            i = this.numUserReject;
            if (i != 0) {
                output.writeInt32(5, i);
            }
            i = this.numApps;
            if (i != 0) {
                output.writeInt32(6, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.numRequest;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            HistogramBucketInt32[] histogramBucketInt32Arr = this.networkMatchSizeHistogram;
            if (histogramBucketInt32Arr != null && histogramBucketInt32Arr.length > 0) {
                i = 0;
                while (true) {
                    HistogramBucketInt32 element = this.networkMatchSizeHistogram;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(2, element);
                    }
                    i++;
                }
            }
            i = this.numConnectSuccess;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.numUserApprovalBypass;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            i = this.numUserReject;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(5, i);
            }
            i = this.numApps;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(6, i);
            }
            return size;
        }

        public WifiNetworkRequestApiLog mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numRequest = input.readInt32();
                } else if (tag == 18) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    HistogramBucketInt32[] histogramBucketInt32Arr = this.networkMatchSizeHistogram;
                    int i = histogramBucketInt32Arr == null ? 0 : histogramBucketInt32Arr.length;
                    HistogramBucketInt32[] newArray = new HistogramBucketInt32[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.networkMatchSizeHistogram, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new HistogramBucketInt32();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new HistogramBucketInt32();
                    input.readMessage(newArray[i]);
                    this.networkMatchSizeHistogram = newArray;
                } else if (tag == 24) {
                    this.numConnectSuccess = input.readInt32();
                } else if (tag == 32) {
                    this.numUserApprovalBypass = input.readInt32();
                } else if (tag == 40) {
                    this.numUserReject = input.readInt32();
                } else if (tag == 48) {
                    this.numApps = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiNetworkRequestApiLog parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiNetworkRequestApiLog) MessageNano.mergeFrom(new WifiNetworkRequestApiLog(), data);
        }

        public static WifiNetworkRequestApiLog parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiNetworkRequestApiLog().mergeFrom(input);
        }
    }

    public static final class WifiNetworkSuggestionApiLog extends MessageNano {
        private static volatile WifiNetworkSuggestionApiLog[] _emptyArray;
        public HistogramBucketInt32[] networkListSizeHistogram;
        public int numConnectFailure;
        public int numConnectSuccess;
        public int numModification;

        public static WifiNetworkSuggestionApiLog[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiNetworkSuggestionApiLog[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiNetworkSuggestionApiLog() {
            clear();
        }

        public WifiNetworkSuggestionApiLog clear() {
            this.numModification = 0;
            this.numConnectSuccess = 0;
            this.numConnectFailure = 0;
            this.networkListSizeHistogram = HistogramBucketInt32.emptyArray();
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.numModification;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.numConnectSuccess;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.numConnectFailure;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            HistogramBucketInt32[] histogramBucketInt32Arr = this.networkListSizeHistogram;
            if (histogramBucketInt32Arr != null && histogramBucketInt32Arr.length > 0) {
                i = 0;
                while (true) {
                    HistogramBucketInt32 element = this.networkListSizeHistogram;
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
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.numModification;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.numConnectSuccess;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.numConnectFailure;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            HistogramBucketInt32[] histogramBucketInt32Arr = this.networkListSizeHistogram;
            if (histogramBucketInt32Arr != null && histogramBucketInt32Arr.length > 0) {
                i = 0;
                while (true) {
                    HistogramBucketInt32 element = this.networkListSizeHistogram;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(4, element);
                    }
                    i++;
                }
            }
            return size;
        }

        public WifiNetworkSuggestionApiLog mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numModification = input.readInt32();
                } else if (tag == 16) {
                    this.numConnectSuccess = input.readInt32();
                } else if (tag == 24) {
                    this.numConnectFailure = input.readInt32();
                } else if (tag == 34) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                    HistogramBucketInt32[] histogramBucketInt32Arr = this.networkListSizeHistogram;
                    int i = histogramBucketInt32Arr == null ? 0 : histogramBucketInt32Arr.length;
                    HistogramBucketInt32[] newArray = new HistogramBucketInt32[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.networkListSizeHistogram, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new HistogramBucketInt32();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new HistogramBucketInt32();
                    input.readMessage(newArray[i]);
                    this.networkListSizeHistogram = newArray;
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiNetworkSuggestionApiLog parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiNetworkSuggestionApiLog) MessageNano.mergeFrom(new WifiNetworkSuggestionApiLog(), data);
        }

        public static WifiNetworkSuggestionApiLog parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiNetworkSuggestionApiLog().mergeFrom(input);
        }
    }

    public static final class WifiP2pStats extends MessageNano {
        private static volatile WifiP2pStats[] _emptyArray;
        public P2pConnectionEvent[] connectionEvent;
        public GroupEvent[] groupEvent;
        public int numPersistentGroup;
        public int numTotalPeerScans;
        public int numTotalServiceScans;

        public static WifiP2pStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiP2pStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiP2pStats() {
            clear();
        }

        public WifiP2pStats clear() {
            this.groupEvent = GroupEvent.emptyArray();
            this.connectionEvent = P2pConnectionEvent.emptyArray();
            this.numPersistentGroup = 0;
            this.numTotalPeerScans = 0;
            this.numTotalServiceScans = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i;
            GroupEvent[] groupEventArr = this.groupEvent;
            if (groupEventArr != null && groupEventArr.length > 0) {
                i = 0;
                while (true) {
                    GroupEvent element = this.groupEvent;
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
            P2pConnectionEvent[] p2pConnectionEventArr = this.connectionEvent;
            if (p2pConnectionEventArr != null && p2pConnectionEventArr.length > 0) {
                i = 0;
                while (true) {
                    P2pConnectionEvent element2 = this.connectionEvent;
                    if (i >= element2.length) {
                        break;
                    }
                    element2 = element2[i];
                    if (element2 != null) {
                        output.writeMessage(2, element2);
                    }
                    i++;
                }
            }
            i = this.numPersistentGroup;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.numTotalPeerScans;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            i = this.numTotalServiceScans;
            if (i != 0) {
                output.writeInt32(5, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int i;
            int size = super.computeSerializedSize();
            GroupEvent[] groupEventArr = this.groupEvent;
            if (groupEventArr != null && groupEventArr.length > 0) {
                i = 0;
                while (true) {
                    GroupEvent element = this.groupEvent;
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
            P2pConnectionEvent[] p2pConnectionEventArr = this.connectionEvent;
            if (p2pConnectionEventArr != null && p2pConnectionEventArr.length > 0) {
                i = 0;
                while (true) {
                    P2pConnectionEvent element2 = this.connectionEvent;
                    if (i >= element2.length) {
                        break;
                    }
                    element2 = element2[i];
                    if (element2 != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(2, element2);
                    }
                    i++;
                }
            }
            i = this.numPersistentGroup;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.numTotalPeerScans;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            i = this.numTotalServiceScans;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(5, i);
            }
            return size;
        }

        public WifiP2pStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                int arrayLength;
                int i;
                if (tag == 10) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 10);
                    GroupEvent[] groupEventArr = this.groupEvent;
                    i = groupEventArr == null ? 0 : groupEventArr.length;
                    GroupEvent[] newArray = new GroupEvent[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.groupEvent, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new GroupEvent();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new GroupEvent();
                    input.readMessage(newArray[i]);
                    this.groupEvent = newArray;
                } else if (tag == 18) {
                    arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    P2pConnectionEvent[] p2pConnectionEventArr = this.connectionEvent;
                    i = p2pConnectionEventArr == null ? 0 : p2pConnectionEventArr.length;
                    P2pConnectionEvent[] newArray2 = new P2pConnectionEvent[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.connectionEvent, 0, newArray2, 0, i);
                    }
                    while (i < newArray2.length - 1) {
                        newArray2[i] = new P2pConnectionEvent();
                        input.readMessage(newArray2[i]);
                        input.readTag();
                        i++;
                    }
                    newArray2[i] = new P2pConnectionEvent();
                    input.readMessage(newArray2[i]);
                    this.connectionEvent = newArray2;
                } else if (tag == 24) {
                    this.numPersistentGroup = input.readInt32();
                } else if (tag == 32) {
                    this.numTotalPeerScans = input.readInt32();
                } else if (tag == 40) {
                    this.numTotalServiceScans = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiP2pStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiP2pStats) MessageNano.mergeFrom(new WifiP2pStats(), data);
        }

        public static WifiP2pStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiP2pStats().mergeFrom(input);
        }
    }

    public static final class WifiPowerStats extends MessageNano {
        private static volatile WifiPowerStats[] _emptyArray;
        public double energyConsumedMah;
        public long idleTimeMs;
        public long loggingDurationMs;
        public double monitoredRailEnergyConsumedMah;
        public long numBytesRx;
        public long numBytesTx;
        public long numPacketsRx;
        public long numPacketsTx;
        public long rxTimeMs;
        public long scanTimeMs;
        public long sleepTimeMs;
        public long txTimeMs;
        public long wifiKernelActiveTimeMs;

        public static WifiPowerStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiPowerStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiPowerStats() {
            clear();
        }

        public WifiPowerStats clear() {
            this.loggingDurationMs = 0;
            this.energyConsumedMah = 0.0d;
            this.idleTimeMs = 0;
            this.rxTimeMs = 0;
            this.txTimeMs = 0;
            this.wifiKernelActiveTimeMs = 0;
            this.numPacketsTx = 0;
            this.numBytesTx = 0;
            this.numPacketsRx = 0;
            this.numBytesRx = 0;
            this.sleepTimeMs = 0;
            this.scanTimeMs = 0;
            this.monitoredRailEnergyConsumedMah = 0.0d;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            long j = this.loggingDurationMs;
            if (j != 0) {
                output.writeInt64(1, j);
            }
            if (Double.doubleToLongBits(this.energyConsumedMah) != Double.doubleToLongBits(0.0d)) {
                output.writeDouble(2, this.energyConsumedMah);
            }
            j = this.idleTimeMs;
            if (j != 0) {
                output.writeInt64(3, j);
            }
            j = this.rxTimeMs;
            if (j != 0) {
                output.writeInt64(4, j);
            }
            j = this.txTimeMs;
            if (j != 0) {
                output.writeInt64(5, j);
            }
            j = this.wifiKernelActiveTimeMs;
            if (j != 0) {
                output.writeInt64(6, j);
            }
            j = this.numPacketsTx;
            if (j != 0) {
                output.writeInt64(7, j);
            }
            j = this.numBytesTx;
            if (j != 0) {
                output.writeInt64(8, j);
            }
            j = this.numPacketsRx;
            if (j != 0) {
                output.writeInt64(9, j);
            }
            j = this.numBytesRx;
            if (j != 0) {
                output.writeInt64(10, j);
            }
            j = this.sleepTimeMs;
            if (j != 0) {
                output.writeInt64(11, j);
            }
            j = this.scanTimeMs;
            if (j != 0) {
                output.writeInt64(12, j);
            }
            if (Double.doubleToLongBits(this.monitoredRailEnergyConsumedMah) != Double.doubleToLongBits(0.0d)) {
                output.writeDouble(13, this.monitoredRailEnergyConsumedMah);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            long j = this.loggingDurationMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, j);
            }
            if (Double.doubleToLongBits(this.energyConsumedMah) != Double.doubleToLongBits(0.0d)) {
                size += CodedOutputByteBufferNano.computeDoubleSize(2, this.energyConsumedMah);
            }
            j = this.idleTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(3, j);
            }
            j = this.rxTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(4, j);
            }
            j = this.txTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(5, j);
            }
            j = this.wifiKernelActiveTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(6, j);
            }
            j = this.numPacketsTx;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(7, j);
            }
            j = this.numBytesTx;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(8, j);
            }
            j = this.numPacketsRx;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(9, j);
            }
            j = this.numBytesRx;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(10, j);
            }
            j = this.sleepTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(11, j);
            }
            j = this.scanTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(12, j);
            }
            if (Double.doubleToLongBits(this.monitoredRailEnergyConsumedMah) != Double.doubleToLongBits(0.0d)) {
                return size + CodedOutputByteBufferNano.computeDoubleSize(13, this.monitoredRailEnergyConsumedMah);
            }
            return size;
        }

        public WifiPowerStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        this.loggingDurationMs = input.readInt64();
                        break;
                    case 17:
                        this.energyConsumedMah = input.readDouble();
                        break;
                    case 24:
                        this.idleTimeMs = input.readInt64();
                        break;
                    case 32:
                        this.rxTimeMs = input.readInt64();
                        break;
                    case 40:
                        this.txTimeMs = input.readInt64();
                        break;
                    case 48:
                        this.wifiKernelActiveTimeMs = input.readInt64();
                        break;
                    case 56:
                        this.numPacketsTx = input.readInt64();
                        break;
                    case 64:
                        this.numBytesTx = input.readInt64();
                        break;
                    case 72:
                        this.numPacketsRx = input.readInt64();
                        break;
                    case 80:
                        this.numBytesRx = input.readInt64();
                        break;
                    case 88:
                        this.sleepTimeMs = input.readInt64();
                        break;
                    case 96:
                        this.scanTimeMs = input.readInt64();
                        break;
                    case 105:
                        this.monitoredRailEnergyConsumedMah = input.readDouble();
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        }
                        return this;
                }
            }
        }

        public static WifiPowerStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiPowerStats) MessageNano.mergeFrom(new WifiPowerStats(), data);
        }

        public static WifiPowerStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiPowerStats().mergeFrom(input);
        }
    }

    public static final class WifiRadioUsage extends MessageNano {
        private static volatile WifiRadioUsage[] _emptyArray;
        public long loggingDurationMs;
        public long scanTimeMs;

        public static WifiRadioUsage[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiRadioUsage[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiRadioUsage() {
            clear();
        }

        public WifiRadioUsage clear() {
            this.loggingDurationMs = 0;
            this.scanTimeMs = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            long j = this.loggingDurationMs;
            if (j != 0) {
                output.writeInt64(1, j);
            }
            j = this.scanTimeMs;
            if (j != 0) {
                output.writeInt64(2, j);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            long j = this.loggingDurationMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, j);
            }
            j = this.scanTimeMs;
            if (j != 0) {
                return size + CodedOutputByteBufferNano.computeInt64Size(2, j);
            }
            return size;
        }

        public WifiRadioUsage mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.loggingDurationMs = input.readInt64();
                } else if (tag == 16) {
                    this.scanTimeMs = input.readInt64();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiRadioUsage parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiRadioUsage) MessageNano.mergeFrom(new WifiRadioUsage(), data);
        }

        public static WifiRadioUsage parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiRadioUsage().mergeFrom(input);
        }
    }

    public static final class WifiRttLog extends MessageNano {
        public static final int ABORTED = 9;
        public static final int FAILURE = 2;
        public static final int FAIL_AP_ON_DIFF_CHANNEL = 7;
        public static final int FAIL_BUSY_TRY_LATER = 13;
        public static final int FAIL_FTM_PARAM_OVERRIDE = 16;
        public static final int FAIL_INVALID_TS = 10;
        public static final int FAIL_NOT_SCHEDULED_YET = 5;
        public static final int FAIL_NO_CAPABILITY = 8;
        public static final int FAIL_NO_RSP = 3;
        public static final int FAIL_PROTOCOL = 11;
        public static final int FAIL_REJECTED = 4;
        public static final int FAIL_SCHEDULE = 12;
        public static final int FAIL_TM_TIMEOUT = 6;
        public static final int INVALID_REQ = 14;
        public static final int MISSING_RESULT = 17;
        public static final int NO_WIFI = 15;
        public static final int OVERALL_AWARE_TRANSLATION_FAILURE = 7;
        public static final int OVERALL_FAIL = 2;
        public static final int OVERALL_HAL_FAILURE = 6;
        public static final int OVERALL_LOCATION_PERMISSION_MISSING = 8;
        public static final int OVERALL_RTT_NOT_AVAILABLE = 3;
        public static final int OVERALL_SUCCESS = 1;
        public static final int OVERALL_THROTTLE = 5;
        public static final int OVERALL_TIMEOUT = 4;
        public static final int OVERALL_UNKNOWN = 0;
        public static final int SUCCESS = 1;
        public static final int UNKNOWN = 0;
        private static volatile WifiRttLog[] _emptyArray;
        public RttOverallStatusHistogramBucket[] histogramOverallStatus;
        public int numRequests;
        public RttToPeerLog rttToAp;
        public RttToPeerLog rttToAware;

        public static final class HistogramBucket extends MessageNano {
            private static volatile HistogramBucket[] _emptyArray;
            public int count;
            public long end;
            public long start;

            public static HistogramBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new HistogramBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public HistogramBucket() {
                clear();
            }

            public HistogramBucket clear() {
                this.start = 0;
                this.end = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                long j = this.start;
                if (j != 0) {
                    output.writeInt64(1, j);
                }
                j = this.end;
                if (j != 0) {
                    output.writeInt64(2, j);
                }
                int i = this.count;
                if (i != 0) {
                    output.writeInt32(3, i);
                }
                super.writeTo(output);
            }

            /* Access modifiers changed, original: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                long j = this.start;
                if (j != 0) {
                    size += CodedOutputByteBufferNano.computeInt64Size(1, j);
                }
                j = this.end;
                if (j != 0) {
                    size += CodedOutputByteBufferNano.computeInt64Size(2, j);
                }
                int i = this.count;
                if (i != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(3, i);
                }
                return size;
            }

            public HistogramBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        this.start = input.readInt64();
                    } else if (tag == 16) {
                        this.end = input.readInt64();
                    } else if (tag == 24) {
                        this.count = input.readInt32();
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static HistogramBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (HistogramBucket) MessageNano.mergeFrom(new HistogramBucket(), data);
            }

            public static HistogramBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new HistogramBucket().mergeFrom(input);
            }
        }

        public static final class RttIndividualStatusHistogramBucket extends MessageNano {
            private static volatile RttIndividualStatusHistogramBucket[] _emptyArray;
            public int count;
            public int statusType;

            public static RttIndividualStatusHistogramBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new RttIndividualStatusHistogramBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public RttIndividualStatusHistogramBucket() {
                clear();
            }

            public RttIndividualStatusHistogramBucket clear() {
                this.statusType = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                int i = this.statusType;
                if (i != 0) {
                    output.writeInt32(1, i);
                }
                i = this.count;
                if (i != 0) {
                    output.writeInt32(2, i);
                }
                super.writeTo(output);
            }

            /* Access modifiers changed, original: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                int i = this.statusType;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, i);
                }
                i = this.count;
                if (i != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
                }
                return size;
            }

            public RttIndividualStatusHistogramBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag != 0) {
                        if (tag == 8) {
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
                                case 10:
                                case 11:
                                case 12:
                                case 13:
                                case 14:
                                case 15:
                                case 16:
                                case 17:
                                    this.statusType = value;
                                    break;
                                default:
                                    break;
                            }
                        } else if (tag == 16) {
                            this.count = input.readInt32();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        return this;
                    }
                }
            }

            public static RttIndividualStatusHistogramBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (RttIndividualStatusHistogramBucket) MessageNano.mergeFrom(new RttIndividualStatusHistogramBucket(), data);
            }

            public static RttIndividualStatusHistogramBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new RttIndividualStatusHistogramBucket().mergeFrom(input);
            }
        }

        public static final class RttOverallStatusHistogramBucket extends MessageNano {
            private static volatile RttOverallStatusHistogramBucket[] _emptyArray;
            public int count;
            public int statusType;

            public static RttOverallStatusHistogramBucket[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new RttOverallStatusHistogramBucket[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public RttOverallStatusHistogramBucket() {
                clear();
            }

            public RttOverallStatusHistogramBucket clear() {
                this.statusType = 0;
                this.count = 0;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                int i = this.statusType;
                if (i != 0) {
                    output.writeInt32(1, i);
                }
                i = this.count;
                if (i != 0) {
                    output.writeInt32(2, i);
                }
                super.writeTo(output);
            }

            /* Access modifiers changed, original: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                int i = this.statusType;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, i);
                }
                i = this.count;
                if (i != 0) {
                    return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
                }
                return size;
            }

            public RttOverallStatusHistogramBucket mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag != 0) {
                        if (tag == 8) {
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
                                    this.statusType = value;
                                    break;
                                default:
                                    break;
                            }
                        } else if (tag == 16) {
                            this.count = input.readInt32();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        return this;
                    }
                }
            }

            public static RttOverallStatusHistogramBucket parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (RttOverallStatusHistogramBucket) MessageNano.mergeFrom(new RttOverallStatusHistogramBucket(), data);
            }

            public static RttOverallStatusHistogramBucket parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new RttOverallStatusHistogramBucket().mergeFrom(input);
            }
        }

        public static final class RttToPeerLog extends MessageNano {
            private static volatile RttToPeerLog[] _emptyArray;
            public HistogramBucket[] histogramDistance;
            public RttIndividualStatusHistogramBucket[] histogramIndividualStatus;
            public HistogramBucket[] histogramNumPeersPerRequest;
            public HistogramBucket[] histogramNumRequestsPerApp;
            public HistogramBucket[] histogramRequestIntervalMs;
            public int numApps;
            public int numIndividualRequests;
            public int numRequests;

            public static RttToPeerLog[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new RttToPeerLog[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public RttToPeerLog() {
                clear();
            }

            public RttToPeerLog clear() {
                this.numRequests = 0;
                this.numIndividualRequests = 0;
                this.numApps = 0;
                this.histogramNumRequestsPerApp = HistogramBucket.emptyArray();
                this.histogramNumPeersPerRequest = HistogramBucket.emptyArray();
                this.histogramIndividualStatus = RttIndividualStatusHistogramBucket.emptyArray();
                this.histogramDistance = HistogramBucket.emptyArray();
                this.histogramRequestIntervalMs = HistogramBucket.emptyArray();
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                HistogramBucket element;
                HistogramBucket[] histogramBucketArr;
                int i = this.numRequests;
                if (i != 0) {
                    output.writeInt32(1, i);
                }
                i = this.numIndividualRequests;
                if (i != 0) {
                    output.writeInt32(2, i);
                }
                i = this.numApps;
                if (i != 0) {
                    output.writeInt32(3, i);
                }
                HistogramBucket[] histogramBucketArr2 = this.histogramNumRequestsPerApp;
                if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                    i = 0;
                    while (true) {
                        element = this.histogramNumRequestsPerApp;
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
                histogramBucketArr2 = this.histogramNumPeersPerRequest;
                if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                    i = 0;
                    while (true) {
                        histogramBucketArr = this.histogramNumPeersPerRequest;
                        if (i >= histogramBucketArr.length) {
                            break;
                        }
                        element = histogramBucketArr[i];
                        if (element != null) {
                            output.writeMessage(5, element);
                        }
                        i++;
                    }
                }
                RttIndividualStatusHistogramBucket[] rttIndividualStatusHistogramBucketArr = this.histogramIndividualStatus;
                if (rttIndividualStatusHistogramBucketArr != null && rttIndividualStatusHistogramBucketArr.length > 0) {
                    i = 0;
                    while (true) {
                        RttIndividualStatusHistogramBucket element2 = this.histogramIndividualStatus;
                        if (i >= element2.length) {
                            break;
                        }
                        element2 = element2[i];
                        if (element2 != null) {
                            output.writeMessage(6, element2);
                        }
                        i++;
                    }
                }
                histogramBucketArr2 = this.histogramDistance;
                if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                    i = 0;
                    while (true) {
                        element = this.histogramDistance;
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
                histogramBucketArr2 = this.histogramRequestIntervalMs;
                if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                    i = 0;
                    while (true) {
                        histogramBucketArr = this.histogramRequestIntervalMs;
                        if (i >= histogramBucketArr.length) {
                            break;
                        }
                        element = histogramBucketArr[i];
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
                HistogramBucket element;
                HistogramBucket[] histogramBucketArr;
                int size = super.computeSerializedSize();
                int i = this.numRequests;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(1, i);
                }
                i = this.numIndividualRequests;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(2, i);
                }
                i = this.numApps;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(3, i);
                }
                HistogramBucket[] histogramBucketArr2 = this.histogramNumRequestsPerApp;
                if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                    i = 0;
                    while (true) {
                        element = this.histogramNumRequestsPerApp;
                        if (i >= element.length) {
                            break;
                        }
                        element = element[i];
                        if (element != null) {
                            size += CodedOutputByteBufferNano.computeMessageSize(4, element);
                        }
                        i++;
                    }
                }
                histogramBucketArr2 = this.histogramNumPeersPerRequest;
                if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                    i = 0;
                    while (true) {
                        histogramBucketArr = this.histogramNumPeersPerRequest;
                        if (i >= histogramBucketArr.length) {
                            break;
                        }
                        element = histogramBucketArr[i];
                        if (element != null) {
                            size += CodedOutputByteBufferNano.computeMessageSize(5, element);
                        }
                        i++;
                    }
                }
                RttIndividualStatusHistogramBucket[] rttIndividualStatusHistogramBucketArr = this.histogramIndividualStatus;
                if (rttIndividualStatusHistogramBucketArr != null && rttIndividualStatusHistogramBucketArr.length > 0) {
                    i = 0;
                    while (true) {
                        RttIndividualStatusHistogramBucket element2 = this.histogramIndividualStatus;
                        if (i >= element2.length) {
                            break;
                        }
                        element2 = element2[i];
                        if (element2 != null) {
                            size += CodedOutputByteBufferNano.computeMessageSize(6, element2);
                        }
                        i++;
                    }
                }
                histogramBucketArr2 = this.histogramDistance;
                if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                    i = 0;
                    while (true) {
                        element = this.histogramDistance;
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
                histogramBucketArr2 = this.histogramRequestIntervalMs;
                if (histogramBucketArr2 != null && histogramBucketArr2.length > 0) {
                    i = 0;
                    while (true) {
                        histogramBucketArr = this.histogramRequestIntervalMs;
                        if (i >= histogramBucketArr.length) {
                            break;
                        }
                        element = histogramBucketArr[i];
                        if (element != null) {
                            size += CodedOutputByteBufferNano.computeMessageSize(8, element);
                        }
                        i++;
                    }
                }
                return size;
            }

            public RttToPeerLog mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    int arrayLength;
                    HistogramBucket[] histogramBucketArr;
                    int i;
                    HistogramBucket[] newArray;
                    if (tag == 8) {
                        this.numRequests = input.readInt32();
                    } else if (tag == 16) {
                        this.numIndividualRequests = input.readInt32();
                    } else if (tag == 24) {
                        this.numApps = input.readInt32();
                    } else if (tag == 34) {
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                        histogramBucketArr = this.histogramNumRequestsPerApp;
                        i = histogramBucketArr == null ? 0 : histogramBucketArr.length;
                        newArray = new HistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramNumRequestsPerApp, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new HistogramBucket();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new HistogramBucket();
                        input.readMessage(newArray[i]);
                        this.histogramNumRequestsPerApp = newArray;
                    } else if (tag == 42) {
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 42);
                        histogramBucketArr = this.histogramNumPeersPerRequest;
                        i = histogramBucketArr == null ? 0 : histogramBucketArr.length;
                        newArray = new HistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramNumPeersPerRequest, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new HistogramBucket();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new HistogramBucket();
                        input.readMessage(newArray[i]);
                        this.histogramNumPeersPerRequest = newArray;
                    } else if (tag == 50) {
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 50);
                        RttIndividualStatusHistogramBucket[] rttIndividualStatusHistogramBucketArr = this.histogramIndividualStatus;
                        i = rttIndividualStatusHistogramBucketArr == null ? 0 : rttIndividualStatusHistogramBucketArr.length;
                        RttIndividualStatusHistogramBucket[] newArray2 = new RttIndividualStatusHistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramIndividualStatus, 0, newArray2, 0, i);
                        }
                        while (i < newArray2.length - 1) {
                            newArray2[i] = new RttIndividualStatusHistogramBucket();
                            input.readMessage(newArray2[i]);
                            input.readTag();
                            i++;
                        }
                        newArray2[i] = new RttIndividualStatusHistogramBucket();
                        input.readMessage(newArray2[i]);
                        this.histogramIndividualStatus = newArray2;
                    } else if (tag == 58) {
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 58);
                        histogramBucketArr = this.histogramDistance;
                        i = histogramBucketArr == null ? 0 : histogramBucketArr.length;
                        newArray = new HistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramDistance, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new HistogramBucket();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new HistogramBucket();
                        input.readMessage(newArray[i]);
                        this.histogramDistance = newArray;
                    } else if (tag == 66) {
                        arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 66);
                        histogramBucketArr = this.histogramRequestIntervalMs;
                        i = histogramBucketArr == null ? 0 : histogramBucketArr.length;
                        newArray = new HistogramBucket[(i + arrayLength)];
                        if (i != 0) {
                            System.arraycopy(this.histogramRequestIntervalMs, 0, newArray, 0, i);
                        }
                        while (i < newArray.length - 1) {
                            newArray[i] = new HistogramBucket();
                            input.readMessage(newArray[i]);
                            input.readTag();
                            i++;
                        }
                        newArray[i] = new HistogramBucket();
                        input.readMessage(newArray[i]);
                        this.histogramRequestIntervalMs = newArray;
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static RttToPeerLog parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (RttToPeerLog) MessageNano.mergeFrom(new RttToPeerLog(), data);
            }

            public static RttToPeerLog parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new RttToPeerLog().mergeFrom(input);
            }
        }

        public static WifiRttLog[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiRttLog[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiRttLog() {
            clear();
        }

        public WifiRttLog clear() {
            this.numRequests = 0;
            this.histogramOverallStatus = RttOverallStatusHistogramBucket.emptyArray();
            this.rttToAp = null;
            this.rttToAware = null;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.numRequests;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            RttOverallStatusHistogramBucket[] rttOverallStatusHistogramBucketArr = this.histogramOverallStatus;
            if (rttOverallStatusHistogramBucketArr != null && rttOverallStatusHistogramBucketArr.length > 0) {
                i = 0;
                while (true) {
                    RttOverallStatusHistogramBucket element = this.histogramOverallStatus;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        output.writeMessage(2, element);
                    }
                    i++;
                }
            }
            RttToPeerLog rttToPeerLog = this.rttToAp;
            if (rttToPeerLog != null) {
                output.writeMessage(3, rttToPeerLog);
            }
            rttToPeerLog = this.rttToAware;
            if (rttToPeerLog != null) {
                output.writeMessage(4, rttToPeerLog);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.numRequests;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            RttOverallStatusHistogramBucket[] rttOverallStatusHistogramBucketArr = this.histogramOverallStatus;
            if (rttOverallStatusHistogramBucketArr != null && rttOverallStatusHistogramBucketArr.length > 0) {
                i = 0;
                while (true) {
                    RttOverallStatusHistogramBucket element = this.histogramOverallStatus;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(2, element);
                    }
                    i++;
                }
            }
            RttToPeerLog rttToPeerLog = this.rttToAp;
            if (rttToPeerLog != null) {
                size += CodedOutputByteBufferNano.computeMessageSize(3, rttToPeerLog);
            }
            rttToPeerLog = this.rttToAware;
            if (rttToPeerLog != null) {
                return size + CodedOutputByteBufferNano.computeMessageSize(4, rttToPeerLog);
            }
            return size;
        }

        public WifiRttLog mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numRequests = input.readInt32();
                } else if (tag == 18) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    RttOverallStatusHistogramBucket[] rttOverallStatusHistogramBucketArr = this.histogramOverallStatus;
                    int i = rttOverallStatusHistogramBucketArr == null ? 0 : rttOverallStatusHistogramBucketArr.length;
                    RttOverallStatusHistogramBucket[] newArray = new RttOverallStatusHistogramBucket[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.histogramOverallStatus, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new RttOverallStatusHistogramBucket();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new RttOverallStatusHistogramBucket();
                    input.readMessage(newArray[i]);
                    this.histogramOverallStatus = newArray;
                } else if (tag == 26) {
                    if (this.rttToAp == null) {
                        this.rttToAp = new RttToPeerLog();
                    }
                    input.readMessage(this.rttToAp);
                } else if (tag == 34) {
                    if (this.rttToAware == null) {
                        this.rttToAware = new RttToPeerLog();
                    }
                    input.readMessage(this.rttToAware);
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiRttLog parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiRttLog) MessageNano.mergeFrom(new WifiRttLog(), data);
        }

        public static WifiRttLog parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiRttLog().mergeFrom(input);
        }
    }

    public static final class WifiScoreCount extends MessageNano {
        private static volatile WifiScoreCount[] _emptyArray;
        public int count;
        public int score;

        public static WifiScoreCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiScoreCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiScoreCount() {
            clear();
        }

        public WifiScoreCount clear() {
            this.score = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.score;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.count;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.score;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.count;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            return size;
        }

        public WifiScoreCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.score = input.readInt32();
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiScoreCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiScoreCount) MessageNano.mergeFrom(new WifiScoreCount(), data);
        }

        public static WifiScoreCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiScoreCount().mergeFrom(input);
        }
    }

    public static final class WifiToggleStats extends MessageNano {
        private static volatile WifiToggleStats[] _emptyArray;
        public int numToggleOffNormal;
        public int numToggleOffPrivileged;
        public int numToggleOnNormal;
        public int numToggleOnPrivileged;

        public static WifiToggleStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiToggleStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiToggleStats() {
            clear();
        }

        public WifiToggleStats clear() {
            this.numToggleOnPrivileged = 0;
            this.numToggleOffPrivileged = 0;
            this.numToggleOnNormal = 0;
            this.numToggleOffNormal = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.numToggleOnPrivileged;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.numToggleOffPrivileged;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.numToggleOnNormal;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.numToggleOffNormal;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.numToggleOnPrivileged;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.numToggleOffPrivileged;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.numToggleOnNormal;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.numToggleOffNormal;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            return size;
        }

        public WifiToggleStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numToggleOnPrivileged = input.readInt32();
                } else if (tag == 16) {
                    this.numToggleOffPrivileged = input.readInt32();
                } else if (tag == 24) {
                    this.numToggleOnNormal = input.readInt32();
                } else if (tag == 32) {
                    this.numToggleOffNormal = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiToggleStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiToggleStats) MessageNano.mergeFrom(new WifiToggleStats(), data);
        }

        public static WifiToggleStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiToggleStats().mergeFrom(input);
        }
    }

    public static final class WifiUsabilityScoreCount extends MessageNano {
        private static volatile WifiUsabilityScoreCount[] _emptyArray;
        public int count;
        public int score;

        public static WifiUsabilityScoreCount[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiUsabilityScoreCount[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiUsabilityScoreCount() {
            clear();
        }

        public WifiUsabilityScoreCount clear() {
            this.score = 0;
            this.count = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.score;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.count;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.score;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.count;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            return size;
        }

        public WifiUsabilityScoreCount mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.score = input.readInt32();
                } else if (tag == 16) {
                    this.count = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiUsabilityScoreCount parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiUsabilityScoreCount) MessageNano.mergeFrom(new WifiUsabilityScoreCount(), data);
        }

        public static WifiUsabilityScoreCount parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiUsabilityScoreCount().mergeFrom(input);
        }
    }

    public static final class WifiUsabilityStats extends MessageNano {
        public static final int LABEL_BAD = 2;
        public static final int LABEL_GOOD = 1;
        public static final int LABEL_UNKNOWN = 0;
        public static final int TYPE_DATA_STALL_BAD_TX = 1;
        public static final int TYPE_DATA_STALL_BOTH = 3;
        public static final int TYPE_DATA_STALL_TX_WITHOUT_RX = 2;
        public static final int TYPE_FIRMWARE_ALERT = 4;
        public static final int TYPE_IP_REACHABILITY_LOST = 5;
        public static final int TYPE_UNKNOWN = 0;
        private static volatile WifiUsabilityStats[] _emptyArray;
        public int firmwareAlertCode;
        public int label;
        public WifiUsabilityStatsEntry[] stats;
        public long timeStampMs;
        public int triggerType;

        public static WifiUsabilityStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiUsabilityStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiUsabilityStats() {
            clear();
        }

        public WifiUsabilityStats clear() {
            this.label = 0;
            this.stats = WifiUsabilityStatsEntry.emptyArray();
            this.triggerType = 0;
            this.firmwareAlertCode = -1;
            this.timeStampMs = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.label;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            WifiUsabilityStatsEntry[] wifiUsabilityStatsEntryArr = this.stats;
            if (wifiUsabilityStatsEntryArr != null && wifiUsabilityStatsEntryArr.length > 0) {
                i = 0;
                while (true) {
                    WifiUsabilityStatsEntry element = this.stats;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        output.writeMessage(2, element);
                    }
                    i++;
                }
            }
            i = this.triggerType;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.firmwareAlertCode;
            if (i != -1) {
                output.writeInt32(4, i);
            }
            long j = this.timeStampMs;
            if (j != 0) {
                output.writeInt64(5, j);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.label;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            WifiUsabilityStatsEntry[] wifiUsabilityStatsEntryArr = this.stats;
            if (wifiUsabilityStatsEntryArr != null && wifiUsabilityStatsEntryArr.length > 0) {
                i = 0;
                while (true) {
                    WifiUsabilityStatsEntry element = this.stats;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(2, element);
                    }
                    i++;
                }
            }
            i = this.triggerType;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.firmwareAlertCode;
            if (i != -1) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            long j = this.timeStampMs;
            if (j != 0) {
                return size + CodedOutputByteBufferNano.computeInt64Size(5, j);
            }
            return size;
        }

        public WifiUsabilityStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                int value;
                if (tag == 8) {
                    value = input.readInt32();
                    if (value == 0 || value == 1 || value == 2) {
                        this.label = value;
                    }
                } else if (tag == 18) {
                    value = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    WifiUsabilityStatsEntry[] wifiUsabilityStatsEntryArr = this.stats;
                    int i = wifiUsabilityStatsEntryArr == null ? 0 : wifiUsabilityStatsEntryArr.length;
                    WifiUsabilityStatsEntry[] newArray = new WifiUsabilityStatsEntry[(i + value)];
                    if (i != 0) {
                        System.arraycopy(this.stats, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new WifiUsabilityStatsEntry();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new WifiUsabilityStatsEntry();
                    input.readMessage(newArray[i]);
                    this.stats = newArray;
                } else if (tag == 24) {
                    value = input.readInt32();
                    if (value == 0 || value == 1 || value == 2 || value == 3 || value == 4 || value == 5) {
                        this.triggerType = value;
                    }
                } else if (tag == 32) {
                    this.firmwareAlertCode = input.readInt32();
                } else if (tag == 40) {
                    this.timeStampMs = input.readInt64();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiUsabilityStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiUsabilityStats) MessageNano.mergeFrom(new WifiUsabilityStats(), data);
        }

        public static WifiUsabilityStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiUsabilityStats().mergeFrom(input);
        }
    }

    public static final class WifiUsabilityStatsEntry extends MessageNano {
        public static final int NETWORK_TYPE_CDMA = 2;
        public static final int NETWORK_TYPE_EVDO_0 = 3;
        public static final int NETWORK_TYPE_GSM = 1;
        public static final int NETWORK_TYPE_LTE = 6;
        public static final int NETWORK_TYPE_NR = 7;
        public static final int NETWORK_TYPE_TD_SCDMA = 5;
        public static final int NETWORK_TYPE_UMTS = 4;
        public static final int NETWORK_TYPE_UNKNOWN = 0;
        public static final int PROBE_STATUS_FAILURE = 3;
        public static final int PROBE_STATUS_NO_PROBE = 1;
        public static final int PROBE_STATUS_SUCCESS = 2;
        public static final int PROBE_STATUS_UNKNOWN = 0;
        private static volatile WifiUsabilityStatsEntry[] _emptyArray;
        public int cellularDataNetworkType;
        public int cellularSignalStrengthDb;
        public int cellularSignalStrengthDbm;
        public int deviceMobilityState;
        public boolean isSameBssidAndFreq;
        public boolean isSameRegisteredCell;
        public int linkSpeedMbps;
        public int predictionHorizonSec;
        public int probeElapsedTimeSinceLastUpdateMs;
        public int probeMcsRateSinceLastUpdate;
        public int probeStatusSinceLastUpdate;
        public int rssi;
        public int rxLinkSpeedMbps;
        public int seqNumInsideFramework;
        public int seqNumToFramework;
        public long timeStampMs;
        public long totalBackgroundScanTimeMs;
        public long totalBeaconRx;
        public long totalCcaBusyFreqTimeMs;
        public long totalHotspot2ScanTimeMs;
        public long totalNanScanTimeMs;
        public long totalPnoScanTimeMs;
        public long totalRadioOnFreqTimeMs;
        public long totalRadioOnTimeMs;
        public long totalRadioRxTimeMs;
        public long totalRadioTxTimeMs;
        public long totalRoamScanTimeMs;
        public long totalRxSuccess;
        public long totalScanTimeMs;
        public long totalTxBad;
        public long totalTxRetries;
        public long totalTxSuccess;
        public int wifiScore;
        public int wifiUsabilityScore;

        public static WifiUsabilityStatsEntry[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiUsabilityStatsEntry[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiUsabilityStatsEntry() {
            clear();
        }

        public WifiUsabilityStatsEntry clear() {
            this.timeStampMs = 0;
            this.rssi = 0;
            this.linkSpeedMbps = 0;
            this.totalTxSuccess = 0;
            this.totalTxRetries = 0;
            this.totalTxBad = 0;
            this.totalRxSuccess = 0;
            this.totalRadioOnTimeMs = 0;
            this.totalRadioTxTimeMs = 0;
            this.totalRadioRxTimeMs = 0;
            this.totalScanTimeMs = 0;
            this.totalNanScanTimeMs = 0;
            this.totalBackgroundScanTimeMs = 0;
            this.totalRoamScanTimeMs = 0;
            this.totalPnoScanTimeMs = 0;
            this.totalHotspot2ScanTimeMs = 0;
            this.wifiScore = 0;
            this.wifiUsabilityScore = 0;
            this.seqNumToFramework = 0;
            this.totalCcaBusyFreqTimeMs = 0;
            this.totalRadioOnFreqTimeMs = 0;
            this.totalBeaconRx = 0;
            this.predictionHorizonSec = 0;
            this.probeStatusSinceLastUpdate = 0;
            this.probeElapsedTimeSinceLastUpdateMs = 0;
            this.probeMcsRateSinceLastUpdate = 0;
            this.rxLinkSpeedMbps = 0;
            this.seqNumInsideFramework = 0;
            this.isSameBssidAndFreq = false;
            this.cellularDataNetworkType = 0;
            this.cellularSignalStrengthDbm = 0;
            this.cellularSignalStrengthDb = 0;
            this.isSameRegisteredCell = false;
            this.deviceMobilityState = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            long j = this.timeStampMs;
            if (j != 0) {
                output.writeInt64(1, j);
            }
            int i = this.rssi;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.linkSpeedMbps;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            j = this.totalTxSuccess;
            if (j != 0) {
                output.writeInt64(4, j);
            }
            j = this.totalTxRetries;
            if (j != 0) {
                output.writeInt64(5, j);
            }
            j = this.totalTxBad;
            if (j != 0) {
                output.writeInt64(6, j);
            }
            j = this.totalRxSuccess;
            if (j != 0) {
                output.writeInt64(7, j);
            }
            j = this.totalRadioOnTimeMs;
            if (j != 0) {
                output.writeInt64(8, j);
            }
            j = this.totalRadioTxTimeMs;
            if (j != 0) {
                output.writeInt64(9, j);
            }
            j = this.totalRadioRxTimeMs;
            if (j != 0) {
                output.writeInt64(10, j);
            }
            j = this.totalScanTimeMs;
            if (j != 0) {
                output.writeInt64(11, j);
            }
            j = this.totalNanScanTimeMs;
            if (j != 0) {
                output.writeInt64(12, j);
            }
            j = this.totalBackgroundScanTimeMs;
            if (j != 0) {
                output.writeInt64(13, j);
            }
            j = this.totalRoamScanTimeMs;
            if (j != 0) {
                output.writeInt64(14, j);
            }
            j = this.totalPnoScanTimeMs;
            if (j != 0) {
                output.writeInt64(15, j);
            }
            j = this.totalHotspot2ScanTimeMs;
            if (j != 0) {
                output.writeInt64(16, j);
            }
            i = this.wifiScore;
            if (i != 0) {
                output.writeInt32(17, i);
            }
            i = this.wifiUsabilityScore;
            if (i != 0) {
                output.writeInt32(18, i);
            }
            i = this.seqNumToFramework;
            if (i != 0) {
                output.writeInt32(19, i);
            }
            j = this.totalCcaBusyFreqTimeMs;
            if (j != 0) {
                output.writeInt64(20, j);
            }
            j = this.totalRadioOnFreqTimeMs;
            if (j != 0) {
                output.writeInt64(21, j);
            }
            j = this.totalBeaconRx;
            if (j != 0) {
                output.writeInt64(22, j);
            }
            i = this.predictionHorizonSec;
            if (i != 0) {
                output.writeInt32(23, i);
            }
            i = this.probeStatusSinceLastUpdate;
            if (i != 0) {
                output.writeInt32(24, i);
            }
            i = this.probeElapsedTimeSinceLastUpdateMs;
            if (i != 0) {
                output.writeInt32(25, i);
            }
            i = this.probeMcsRateSinceLastUpdate;
            if (i != 0) {
                output.writeInt32(26, i);
            }
            i = this.rxLinkSpeedMbps;
            if (i != 0) {
                output.writeInt32(27, i);
            }
            i = this.seqNumInsideFramework;
            if (i != 0) {
                output.writeInt32(28, i);
            }
            boolean z = this.isSameBssidAndFreq;
            if (z) {
                output.writeBool(29, z);
            }
            i = this.cellularDataNetworkType;
            if (i != 0) {
                output.writeInt32(30, i);
            }
            i = this.cellularSignalStrengthDbm;
            if (i != 0) {
                output.writeInt32(31, i);
            }
            i = this.cellularSignalStrengthDb;
            if (i != 0) {
                output.writeInt32(32, i);
            }
            z = this.isSameRegisteredCell;
            if (z) {
                output.writeBool(33, z);
            }
            i = this.deviceMobilityState;
            if (i != 0) {
                output.writeInt32(34, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            long j = this.timeStampMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(1, j);
            }
            int i = this.rssi;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.linkSpeedMbps;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            j = this.totalTxSuccess;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(4, j);
            }
            j = this.totalTxRetries;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(5, j);
            }
            j = this.totalTxBad;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(6, j);
            }
            j = this.totalRxSuccess;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(7, j);
            }
            j = this.totalRadioOnTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(8, j);
            }
            j = this.totalRadioTxTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(9, j);
            }
            j = this.totalRadioRxTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(10, j);
            }
            j = this.totalScanTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(11, j);
            }
            j = this.totalNanScanTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(12, j);
            }
            j = this.totalBackgroundScanTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(13, j);
            }
            j = this.totalRoamScanTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(14, j);
            }
            j = this.totalPnoScanTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(15, j);
            }
            j = this.totalHotspot2ScanTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(16, j);
            }
            i = this.wifiScore;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(17, i);
            }
            i = this.wifiUsabilityScore;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(18, i);
            }
            i = this.seqNumToFramework;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(19, i);
            }
            j = this.totalCcaBusyFreqTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(20, j);
            }
            j = this.totalRadioOnFreqTimeMs;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(21, j);
            }
            j = this.totalBeaconRx;
            if (j != 0) {
                size += CodedOutputByteBufferNano.computeInt64Size(22, j);
            }
            i = this.predictionHorizonSec;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(23, i);
            }
            i = this.probeStatusSinceLastUpdate;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(24, i);
            }
            i = this.probeElapsedTimeSinceLastUpdateMs;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(25, i);
            }
            i = this.probeMcsRateSinceLastUpdate;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(26, i);
            }
            i = this.rxLinkSpeedMbps;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(27, i);
            }
            i = this.seqNumInsideFramework;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(28, i);
            }
            boolean z = this.isSameBssidAndFreq;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(29, z);
            }
            i = this.cellularDataNetworkType;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(30, i);
            }
            i = this.cellularSignalStrengthDbm;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(31, i);
            }
            i = this.cellularSignalStrengthDb;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(32, i);
            }
            z = this.isSameRegisteredCell;
            if (z) {
                size += CodedOutputByteBufferNano.computeBoolSize(33, z);
            }
            i = this.deviceMobilityState;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(34, i);
            }
            return size;
        }

        public WifiUsabilityStatsEntry mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                int value;
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        this.timeStampMs = input.readInt64();
                        break;
                    case 16:
                        this.rssi = input.readInt32();
                        break;
                    case 24:
                        this.linkSpeedMbps = input.readInt32();
                        break;
                    case 32:
                        this.totalTxSuccess = input.readInt64();
                        break;
                    case 40:
                        this.totalTxRetries = input.readInt64();
                        break;
                    case 48:
                        this.totalTxBad = input.readInt64();
                        break;
                    case 56:
                        this.totalRxSuccess = input.readInt64();
                        break;
                    case 64:
                        this.totalRadioOnTimeMs = input.readInt64();
                        break;
                    case 72:
                        this.totalRadioTxTimeMs = input.readInt64();
                        break;
                    case 80:
                        this.totalRadioRxTimeMs = input.readInt64();
                        break;
                    case 88:
                        this.totalScanTimeMs = input.readInt64();
                        break;
                    case 96:
                        this.totalNanScanTimeMs = input.readInt64();
                        break;
                    case 104:
                        this.totalBackgroundScanTimeMs = input.readInt64();
                        break;
                    case 112:
                        this.totalRoamScanTimeMs = input.readInt64();
                        break;
                    case 120:
                        this.totalPnoScanTimeMs = input.readInt64();
                        break;
                    case 128:
                        this.totalHotspot2ScanTimeMs = input.readInt64();
                        break;
                    case 136:
                        this.wifiScore = input.readInt32();
                        break;
                    case 144:
                        this.wifiUsabilityScore = input.readInt32();
                        break;
                    case 152:
                        this.seqNumToFramework = input.readInt32();
                        break;
                    case 160:
                        this.totalCcaBusyFreqTimeMs = input.readInt64();
                        break;
                    case 168:
                        this.totalRadioOnFreqTimeMs = input.readInt64();
                        break;
                    case 176:
                        this.totalBeaconRx = input.readInt64();
                        break;
                    case 184:
                        this.predictionHorizonSec = input.readInt32();
                        break;
                    case 192:
                        value = input.readInt32();
                        if (value != 0 && value != 1 && value != 2 && value != 3) {
                            break;
                        }
                        this.probeStatusSinceLastUpdate = value;
                        break;
                        break;
                    case 200:
                        this.probeElapsedTimeSinceLastUpdateMs = input.readInt32();
                        break;
                    case 208:
                        this.probeMcsRateSinceLastUpdate = input.readInt32();
                        break;
                    case 216:
                        this.rxLinkSpeedMbps = input.readInt32();
                        break;
                    case 224:
                        this.seqNumInsideFramework = input.readInt32();
                        break;
                    case 232:
                        this.isSameBssidAndFreq = input.readBool();
                        break;
                    case 240:
                        int value2 = input.readInt32();
                        switch (value2) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                                this.cellularDataNetworkType = value2;
                                break;
                            default:
                                break;
                        }
                    case 248:
                        this.cellularSignalStrengthDbm = input.readInt32();
                        break;
                    case 256:
                        this.cellularSignalStrengthDb = input.readInt32();
                        break;
                    case 264:
                        this.isSameRegisteredCell = input.readBool();
                        break;
                    case 272:
                        value = input.readInt32();
                        if (value != 0 && value != 1 && value != 2 && value != 3) {
                            break;
                        }
                        this.deviceMobilityState = value;
                        break;
                        break;
                    default:
                        if (WireFormatNano.parseUnknownField(input, tag)) {
                            break;
                        }
                        return this;
                }
            }
        }

        public static WifiUsabilityStatsEntry parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiUsabilityStatsEntry) MessageNano.mergeFrom(new WifiUsabilityStatsEntry(), data);
        }

        public static WifiUsabilityStatsEntry parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiUsabilityStatsEntry().mergeFrom(input);
        }
    }

    public static final class WifiWakeStats extends MessageNano {
        private static volatile WifiWakeStats[] _emptyArray;
        public int numIgnoredStarts;
        public int numSessions;
        public int numWakeups;
        public Session[] sessions;

        public static final class Session extends MessageNano {
            private static volatile Session[] _emptyArray;
            public Event initializeEvent;
            public int lockedNetworksAtInitialize;
            public int lockedNetworksAtStart;
            public Event resetEvent;
            public long startTimeMillis;
            public Event unlockEvent;
            public Event wakeupEvent;

            public static final class Event extends MessageNano {
                private static volatile Event[] _emptyArray;
                public int elapsedScans;
                public long elapsedTimeMillis;

                public static Event[] emptyArray() {
                    if (_emptyArray == null) {
                        synchronized (InternalNano.LAZY_INIT_LOCK) {
                            if (_emptyArray == null) {
                                _emptyArray = new Event[0];
                            }
                        }
                    }
                    return _emptyArray;
                }

                public Event() {
                    clear();
                }

                public Event clear() {
                    this.elapsedTimeMillis = 0;
                    this.elapsedScans = 0;
                    this.cachedSize = -1;
                    return this;
                }

                public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                    long j = this.elapsedTimeMillis;
                    if (j != 0) {
                        output.writeInt64(1, j);
                    }
                    int i = this.elapsedScans;
                    if (i != 0) {
                        output.writeInt32(2, i);
                    }
                    super.writeTo(output);
                }

                /* Access modifiers changed, original: protected */
                public int computeSerializedSize() {
                    int size = super.computeSerializedSize();
                    long j = this.elapsedTimeMillis;
                    if (j != 0) {
                        size += CodedOutputByteBufferNano.computeInt64Size(1, j);
                    }
                    int i = this.elapsedScans;
                    if (i != 0) {
                        return size + CodedOutputByteBufferNano.computeInt32Size(2, i);
                    }
                    return size;
                }

                public Event mergeFrom(CodedInputByteBufferNano input) throws IOException {
                    while (true) {
                        int tag = input.readTag();
                        if (tag == 0) {
                            return this;
                        }
                        if (tag == 8) {
                            this.elapsedTimeMillis = input.readInt64();
                        } else if (tag == 16) {
                            this.elapsedScans = input.readInt32();
                        } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                    }
                }

                public static Event parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                    return (Event) MessageNano.mergeFrom(new Event(), data);
                }

                public static Event parseFrom(CodedInputByteBufferNano input) throws IOException {
                    return new Event().mergeFrom(input);
                }
            }

            public static Session[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new Session[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public Session() {
                clear();
            }

            public Session clear() {
                this.startTimeMillis = 0;
                this.lockedNetworksAtStart = 0;
                this.lockedNetworksAtInitialize = 0;
                this.initializeEvent = null;
                this.unlockEvent = null;
                this.wakeupEvent = null;
                this.resetEvent = null;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                long j = this.startTimeMillis;
                if (j != 0) {
                    output.writeInt64(1, j);
                }
                int i = this.lockedNetworksAtStart;
                if (i != 0) {
                    output.writeInt32(2, i);
                }
                Event event = this.unlockEvent;
                if (event != null) {
                    output.writeMessage(3, event);
                }
                event = this.wakeupEvent;
                if (event != null) {
                    output.writeMessage(4, event);
                }
                event = this.resetEvent;
                if (event != null) {
                    output.writeMessage(5, event);
                }
                i = this.lockedNetworksAtInitialize;
                if (i != 0) {
                    output.writeInt32(6, i);
                }
                event = this.initializeEvent;
                if (event != null) {
                    output.writeMessage(7, event);
                }
                super.writeTo(output);
            }

            /* Access modifiers changed, original: protected */
            public int computeSerializedSize() {
                int size = super.computeSerializedSize();
                long j = this.startTimeMillis;
                if (j != 0) {
                    size += CodedOutputByteBufferNano.computeInt64Size(1, j);
                }
                int i = this.lockedNetworksAtStart;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(2, i);
                }
                Event event = this.unlockEvent;
                if (event != null) {
                    size += CodedOutputByteBufferNano.computeMessageSize(3, event);
                }
                event = this.wakeupEvent;
                if (event != null) {
                    size += CodedOutputByteBufferNano.computeMessageSize(4, event);
                }
                event = this.resetEvent;
                if (event != null) {
                    size += CodedOutputByteBufferNano.computeMessageSize(5, event);
                }
                i = this.lockedNetworksAtInitialize;
                if (i != 0) {
                    size += CodedOutputByteBufferNano.computeInt32Size(6, i);
                }
                event = this.initializeEvent;
                if (event != null) {
                    return size + CodedOutputByteBufferNano.computeMessageSize(7, event);
                }
                return size;
            }

            public Session mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        this.startTimeMillis = input.readInt64();
                    } else if (tag == 16) {
                        this.lockedNetworksAtStart = input.readInt32();
                    } else if (tag == 26) {
                        if (this.unlockEvent == null) {
                            this.unlockEvent = new Event();
                        }
                        input.readMessage(this.unlockEvent);
                    } else if (tag == 34) {
                        if (this.wakeupEvent == null) {
                            this.wakeupEvent = new Event();
                        }
                        input.readMessage(this.wakeupEvent);
                    } else if (tag == 42) {
                        if (this.resetEvent == null) {
                            this.resetEvent = new Event();
                        }
                        input.readMessage(this.resetEvent);
                    } else if (tag == 48) {
                        this.lockedNetworksAtInitialize = input.readInt32();
                    } else if (tag == 58) {
                        if (this.initializeEvent == null) {
                            this.initializeEvent = new Event();
                        }
                        input.readMessage(this.initializeEvent);
                    } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                        return this;
                    }
                }
            }

            public static Session parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (Session) MessageNano.mergeFrom(new Session(), data);
            }

            public static Session parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new Session().mergeFrom(input);
            }
        }

        public static WifiWakeStats[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WifiWakeStats[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WifiWakeStats() {
            clear();
        }

        public WifiWakeStats clear() {
            this.numSessions = 0;
            this.sessions = Session.emptyArray();
            this.numIgnoredStarts = 0;
            this.numWakeups = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.numSessions;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            Session[] sessionArr = this.sessions;
            if (sessionArr != null && sessionArr.length > 0) {
                i = 0;
                while (true) {
                    Session element = this.sessions;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        output.writeMessage(2, element);
                    }
                    i++;
                }
            }
            i = this.numIgnoredStarts;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.numWakeups;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.numSessions;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            Session[] sessionArr = this.sessions;
            if (sessionArr != null && sessionArr.length > 0) {
                i = 0;
                while (true) {
                    Session element = this.sessions;
                    if (i >= element.length) {
                        break;
                    }
                    element = element[i];
                    if (element != null) {
                        size += CodedOutputByteBufferNano.computeMessageSize(2, element);
                    }
                    i++;
                }
            }
            i = this.numIgnoredStarts;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.numWakeups;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            return size;
        }

        public WifiWakeStats mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numSessions = input.readInt32();
                } else if (tag == 18) {
                    int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    Session[] sessionArr = this.sessions;
                    int i = sessionArr == null ? 0 : sessionArr.length;
                    Session[] newArray = new Session[(i + arrayLength)];
                    if (i != 0) {
                        System.arraycopy(this.sessions, 0, newArray, 0, i);
                    }
                    while (i < newArray.length - 1) {
                        newArray[i] = new Session();
                        input.readMessage(newArray[i]);
                        input.readTag();
                        i++;
                    }
                    newArray[i] = new Session();
                    input.readMessage(newArray[i]);
                    this.sessions = newArray;
                } else if (tag == 24) {
                    this.numIgnoredStarts = input.readInt32();
                } else if (tag == 32) {
                    this.numWakeups = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WifiWakeStats parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WifiWakeStats) MessageNano.mergeFrom(new WifiWakeStats(), data);
        }

        public static WifiWakeStats parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WifiWakeStats().mergeFrom(input);
        }
    }

    public static final class WpsMetrics extends MessageNano {
        private static volatile WpsMetrics[] _emptyArray;
        public int numWpsAttempts;
        public int numWpsCancellation;
        public int numWpsOtherConnectionFailure;
        public int numWpsOverlapFailure;
        public int numWpsStartFailure;
        public int numWpsSuccess;
        public int numWpsSupplicantFailure;
        public int numWpsTimeoutFailure;

        public static WpsMetrics[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new WpsMetrics[0];
                    }
                }
            }
            return _emptyArray;
        }

        public WpsMetrics() {
            clear();
        }

        public WpsMetrics clear() {
            this.numWpsAttempts = 0;
            this.numWpsSuccess = 0;
            this.numWpsStartFailure = 0;
            this.numWpsOverlapFailure = 0;
            this.numWpsTimeoutFailure = 0;
            this.numWpsOtherConnectionFailure = 0;
            this.numWpsSupplicantFailure = 0;
            this.numWpsCancellation = 0;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            int i = this.numWpsAttempts;
            if (i != 0) {
                output.writeInt32(1, i);
            }
            i = this.numWpsSuccess;
            if (i != 0) {
                output.writeInt32(2, i);
            }
            i = this.numWpsStartFailure;
            if (i != 0) {
                output.writeInt32(3, i);
            }
            i = this.numWpsOverlapFailure;
            if (i != 0) {
                output.writeInt32(4, i);
            }
            i = this.numWpsTimeoutFailure;
            if (i != 0) {
                output.writeInt32(5, i);
            }
            i = this.numWpsOtherConnectionFailure;
            if (i != 0) {
                output.writeInt32(6, i);
            }
            i = this.numWpsSupplicantFailure;
            if (i != 0) {
                output.writeInt32(7, i);
            }
            i = this.numWpsCancellation;
            if (i != 0) {
                output.writeInt32(8, i);
            }
            super.writeTo(output);
        }

        /* Access modifiers changed, original: protected */
        public int computeSerializedSize() {
            int size = super.computeSerializedSize();
            int i = this.numWpsAttempts;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            i = this.numWpsSuccess;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(2, i);
            }
            i = this.numWpsStartFailure;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(3, i);
            }
            i = this.numWpsOverlapFailure;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(4, i);
            }
            i = this.numWpsTimeoutFailure;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(5, i);
            }
            i = this.numWpsOtherConnectionFailure;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(6, i);
            }
            i = this.numWpsSupplicantFailure;
            if (i != 0) {
                size += CodedOutputByteBufferNano.computeInt32Size(7, i);
            }
            i = this.numWpsCancellation;
            if (i != 0) {
                return size + CodedOutputByteBufferNano.computeInt32Size(8, i);
            }
            return size;
        }

        public WpsMetrics mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.numWpsAttempts = input.readInt32();
                } else if (tag == 16) {
                    this.numWpsSuccess = input.readInt32();
                } else if (tag == 24) {
                    this.numWpsStartFailure = input.readInt32();
                } else if (tag == 32) {
                    this.numWpsOverlapFailure = input.readInt32();
                } else if (tag == 40) {
                    this.numWpsTimeoutFailure = input.readInt32();
                } else if (tag == 48) {
                    this.numWpsOtherConnectionFailure = input.readInt32();
                } else if (tag == 56) {
                    this.numWpsSupplicantFailure = input.readInt32();
                } else if (tag == 64) {
                    this.numWpsCancellation = input.readInt32();
                } else if (!WireFormatNano.parseUnknownField(input, tag)) {
                    return this;
                }
            }
        }

        public static WpsMetrics parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (WpsMetrics) MessageNano.mergeFrom(new WpsMetrics(), data);
        }

        public static WpsMetrics parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new WpsMetrics().mergeFrom(input);
        }
    }
}
