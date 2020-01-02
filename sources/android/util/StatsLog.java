package android.util;

import android.Manifest.permission;
import android.annotation.SystemApi;
import android.content.Context;
import android.os.IStatsManager;
import android.os.IStatsManager.Stub;
import android.os.RemoteException;
import android.os.ServiceManager;

public final class StatsLog extends StatsLogInternal {
    private static final boolean DEBUG = false;
    private static final String TAG = "StatsLog";
    private static Object sLogLock = new Object();
    private static IStatsManager sService;

    @SystemApi
    public static native void writeRaw(byte[] bArr, int i);

    private StatsLog() {
    }

    public static boolean logStart(int label) {
        synchronized (sLogLock) {
            try {
                IStatsManager service = getIStatsManagerLocked();
                if (service == null) {
                    return false;
                }
                service.sendAppBreadcrumbAtom(label, 3);
                return true;
            } catch (RemoteException e) {
                sService = null;
                return false;
            } catch (Throwable th) {
            }
        }
    }

    public static boolean logStop(int label) {
        synchronized (sLogLock) {
            try {
                IStatsManager service = getIStatsManagerLocked();
                if (service == null) {
                    return false;
                }
                service.sendAppBreadcrumbAtom(label, 2);
                return true;
            } catch (RemoteException e) {
                sService = null;
                return false;
            } catch (Throwable th) {
            }
        }
    }

    public static boolean logEvent(int label) {
        synchronized (sLogLock) {
            try {
                IStatsManager service = getIStatsManagerLocked();
                if (service == null) {
                    return false;
                }
                service.sendAppBreadcrumbAtom(label, 1);
                return true;
            } catch (RemoteException e) {
                sService = null;
                return false;
            } catch (Throwable th) {
            }
        }
    }

    public static boolean logBinaryPushStateChanged(String trainName, long trainVersionCode, int options, int state, long[] experimentIds) {
        synchronized (sLogLock) {
            try {
                IStatsManager service = getIStatsManagerLocked();
                if (service == null) {
                    return false;
                }
                service.sendBinaryPushStateChangedAtom(trainName, trainVersionCode, options, state, experimentIds);
                return true;
            } catch (RemoteException e) {
                sService = null;
                return false;
            } catch (Throwable th) {
            }
        }
    }

    public static boolean logWatchdogRollbackOccurred(int rollbackType, String packageName, long packageVersionCode) {
        synchronized (sLogLock) {
            try {
                IStatsManager service = getIStatsManagerLocked();
                if (service == null) {
                    return false;
                }
                service.sendWatchdogRollbackOccurredAtom(rollbackType, packageName, packageVersionCode);
                return true;
            } catch (RemoteException e) {
                sService = null;
                return false;
            } catch (Throwable th) {
            }
        }
    }

    private static IStatsManager getIStatsManagerLocked() throws RemoteException {
        IStatsManager iStatsManager = sService;
        if (iStatsManager != null) {
            return iStatsManager;
        }
        sService = Stub.asInterface(ServiceManager.getService(Context.STATS_MANAGER));
        return sService;
    }

    public static void write(int id, Object... params) {
        if (id == 121) {
            StatsLogInternal.write(id, ((Integer) params[0]).intValue(), ((Integer) params[1]).intValue(), ((Integer) params[2]).intValue(), (byte[]) params[3], (byte[]) params[4], (byte[]) params[5]);
        } else if (id == 170) {
            StatsLogInternal.write(id, ((Long) params[0]).longValue(), ((Integer) params[1]).intValue(), (String) params[2], (String) params[3], ((Boolean) params[4]).booleanValue(), ((Integer) params[5]).intValue());
        }
    }

    private static void enforceDumpCallingPermission(Context context) {
        context.enforceCallingPermission(permission.DUMP, "Need DUMP permission.");
    }

    private static void enforcesageStatsCallingPermission(Context context) {
        context.enforceCallingPermission(permission.PACKAGE_USAGE_STATS, "Need PACKAGE_USAGE_STATS permission.");
    }
}
