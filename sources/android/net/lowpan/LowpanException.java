package android.net.lowpan;

import android.os.ServiceSpecificException;
import android.util.AndroidException;

public class LowpanException extends AndroidException {
    public LowpanException(String message) {
        super(message);
    }

    public LowpanException(String message, Throwable cause) {
        super(message, cause);
    }

    public LowpanException(Exception cause) {
        super(cause);
    }

    static LowpanException rethrowFromServiceSpecificException(ServiceSpecificException e) throws LowpanException {
        int i = e.errorCode;
        if (i == 2) {
            throw new LowpanRuntimeException(e.getMessage() != null ? e.getMessage() : "Invalid argument", e);
        } else if (i == 3) {
            throw new InterfaceDisabledException((Exception) e);
        } else if (i == 4) {
            throw new WrongStateException((Exception) e);
        } else if (i != 7) {
            switch (i) {
                case 10:
                    throw new OperationCanceledException((Exception) e);
                case 11:
                    throw new LowpanException(e.getMessage() != null ? e.getMessage() : "Feature not supported", e);
                case 12:
                    throw new JoinFailedException((Exception) e);
                case 13:
                    throw new JoinFailedAtScanException((Exception) e);
                case 14:
                    throw new JoinFailedAtAuthException((Exception) e);
                case 15:
                    throw new NetworkAlreadyExistsException((Exception) e);
                default:
                    throw new LowpanRuntimeException((Exception) e);
            }
        } else {
            throw new LowpanRuntimeException(e.getMessage() != null ? e.getMessage() : "NCP problem", e);
        }
    }
}
