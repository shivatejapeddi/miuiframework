package com.android.internal.telephony;

import com.android.internal.telephony.PhoneConstants.DataState;
import com.android.internal.telephony.PhoneConstants.State;

public class PhoneConstantConversions {

    /* renamed from: com.android.internal.telephony.PhoneConstantConversions$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$PhoneConstants$DataState = new int[DataState.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$PhoneConstants$State = new int[State.values().length];

        static {
            try {
                $SwitchMap$com$android$internal$telephony$PhoneConstants$DataState[DataState.CONNECTING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$PhoneConstants$DataState[DataState.CONNECTED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$PhoneConstants$DataState[DataState.SUSPENDED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$PhoneConstants$State[State.RINGING.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$PhoneConstants$State[State.OFFHOOK.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public static int convertCallState(State state) {
        int i = AnonymousClass1.$SwitchMap$com$android$internal$telephony$PhoneConstants$State[state.ordinal()];
        if (i == 1) {
            return 1;
        }
        if (i != 2) {
            return 0;
        }
        return 2;
    }

    public static State convertCallState(int state) {
        if (state == 1) {
            return State.RINGING;
        }
        if (state != 2) {
            return State.IDLE;
        }
        return State.OFFHOOK;
    }

    public static int convertDataState(DataState state) {
        int i = AnonymousClass1.$SwitchMap$com$android$internal$telephony$PhoneConstants$DataState[state.ordinal()];
        if (i == 1) {
            return 1;
        }
        if (i == 2) {
            return 2;
        }
        if (i != 3) {
            return 0;
        }
        return 3;
    }

    public static DataState convertDataState(int state) {
        if (state == 1) {
            return DataState.CONNECTING;
        }
        if (state == 2) {
            return DataState.CONNECTED;
        }
        if (state != 3) {
            return DataState.DISCONNECTED;
        }
        return DataState.SUSPENDED;
    }
}
