package android.nfc.cardemulation;

import android.app.Activity;
import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.nfc.INfcCardEmulation;
import android.nfc.NfcAdapter;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public final class CardEmulation {
    public static final String ACTION_CHANGE_DEFAULT = "android.nfc.cardemulation.action.ACTION_CHANGE_DEFAULT";
    private static final Pattern AID_PATTERN = Pattern.compile("[0-9A-Fa-f]{10,32}\\*?\\#?");
    public static final String CATEGORY_OTHER = "other";
    public static final String CATEGORY_PAYMENT = "payment";
    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_SERVICE_COMPONENT = "component";
    public static final int SELECTION_MODE_ALWAYS_ASK = 1;
    public static final int SELECTION_MODE_ASK_IF_CONFLICT = 2;
    public static final int SELECTION_MODE_PREFER_DEFAULT = 0;
    static final String TAG = "CardEmulation";
    static HashMap<Context, CardEmulation> sCardEmus = new HashMap();
    static boolean sIsInitialized = false;
    static INfcCardEmulation sService;
    final Context mContext;

    private CardEmulation(Context context, INfcCardEmulation service) {
        this.mContext = context.getApplicationContext();
        sService = service;
    }

    public static synchronized CardEmulation getInstance(NfcAdapter adapter) {
        CardEmulation manager;
        synchronized (CardEmulation.class) {
            if (adapter != null) {
                try {
                    Context context = adapter.getContext();
                    if (context != null) {
                        if (!sIsInitialized) {
                            IPackageManager pm = ActivityThread.getPackageManager();
                            if (pm == null) {
                                Log.e(TAG, "Cannot get PackageManager");
                                throw new UnsupportedOperationException();
                            } else if (pm.hasSystemFeature("android.hardware.nfc.hce", 0)) {
                                sIsInitialized = true;
                            } else {
                                Log.e(TAG, "This device does not support card emulation");
                                throw new UnsupportedOperationException();
                            }
                        }
                        manager = (CardEmulation) sCardEmus.get(context);
                        if (manager == null) {
                            INfcCardEmulation service = adapter.getCardEmulationService();
                            if (service != null) {
                                manager = new CardEmulation(context, service);
                                sCardEmus.put(context, manager);
                            } else {
                                Log.e(TAG, "This device does not implement the INfcCardEmulation interface.");
                                throw new UnsupportedOperationException();
                            }
                        }
                    } else {
                        Log.e(TAG, "NfcAdapter context is null.");
                        throw new UnsupportedOperationException();
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "PackageManager query failed.");
                    throw new UnsupportedOperationException();
                } catch (Throwable th) {
                }
            } else {
                throw new NullPointerException("NfcAdapter is null");
            }
        }
        return manager;
    }

    public boolean isDefaultServiceForCategory(ComponentName service, String category) {
        try {
            return sService.isDefaultServiceForCategory(this.mContext.getUserId(), service, category);
        } catch (RemoteException e) {
            recoverService();
            INfcCardEmulation iNfcCardEmulation = sService;
            String str = "Failed to recover CardEmulationService.";
            String str2 = TAG;
            if (iNfcCardEmulation == null) {
                Log.e(str2, str);
                return false;
            }
            try {
                return iNfcCardEmulation.isDefaultServiceForCategory(this.mContext.getUserId(), service, category);
            } catch (RemoteException e2) {
                Log.e(str2, str);
                return false;
            }
        }
    }

    public boolean isDefaultServiceForAid(ComponentName service, String aid) {
        try {
            return sService.isDefaultServiceForAid(this.mContext.getUserId(), service, aid);
        } catch (RemoteException e) {
            recoverService();
            INfcCardEmulation iNfcCardEmulation = sService;
            String str = TAG;
            if (iNfcCardEmulation == null) {
                Log.e(str, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.isDefaultServiceForAid(this.mContext.getUserId(), service, aid);
            } catch (RemoteException e2) {
                Log.e(str, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public boolean categoryAllowsForegroundPreference(String category) {
        boolean z = true;
        if (!CATEGORY_PAYMENT.equals(category)) {
            return true;
        }
        boolean preferForeground = false;
        try {
            if (Secure.getInt(this.mContext.getContentResolver(), Secure.NFC_PAYMENT_FOREGROUND) == 0) {
                z = false;
            }
            preferForeground = z;
        } catch (SettingNotFoundException e) {
        }
        return preferForeground;
    }

    public int getSelectionModeForCategory(String category) {
        if (!CATEGORY_PAYMENT.equals(category)) {
            return 2;
        }
        if (Secure.getString(this.mContext.getContentResolver(), Secure.NFC_PAYMENT_DEFAULT_COMPONENT) != null) {
            return 0;
        }
        return 1;
    }

    public boolean registerAidsForService(ComponentName service, String category, List<String> aids) {
        AidGroup aidGroup = new AidGroup((List) aids, category);
        try {
            return sService.registerAidGroupForService(this.mContext.getUserId(), service, aidGroup);
        } catch (RemoteException e) {
            recoverService();
            INfcCardEmulation iNfcCardEmulation = sService;
            String str = TAG;
            if (iNfcCardEmulation == null) {
                Log.e(str, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.registerAidGroupForService(this.mContext.getUserId(), service, aidGroup);
            } catch (RemoteException e2) {
                Log.e(str, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public boolean unsetOffHostForService(ComponentName service) {
        boolean z = false;
        if (NfcAdapter.getDefaultAdapter(this.mContext) == null) {
            return false;
        }
        try {
            z = sService.unsetOffHostForService(this.mContext.getUserId(), service);
            return z;
        } catch (RemoteException e) {
            recoverService();
            INfcCardEmulation iNfcCardEmulation = sService;
            String str = TAG;
            if (iNfcCardEmulation == null) {
                Log.e(str, "Failed to recover CardEmulationService.");
                return z;
            }
            try {
                z = iNfcCardEmulation.unsetOffHostForService(this.mContext.getUserId(), service);
                return z;
            } catch (RemoteException e2) {
                Log.e(str, "Failed to reach CardEmulationService.");
                return z;
            }
        }
    }

    public boolean setOffHostForService(ComponentName service, String offHostSecureElement) {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this.mContext);
        boolean z = false;
        if (adapter == null || offHostSecureElement == null) {
            return false;
        }
        List<String> validSE = adapter.getSupportedOffHostSecureElements();
        String str = "eSE";
        if (!offHostSecureElement.startsWith(str) || validSE.contains(str)) {
            String str2 = "SIM";
            if (!offHostSecureElement.startsWith(str2) || validSE.contains(str2)) {
                if (!offHostSecureElement.startsWith(str) && !offHostSecureElement.startsWith(str2)) {
                    return false;
                }
                if (offHostSecureElement.equals(str)) {
                    offHostSecureElement = "eSE1";
                } else if (offHostSecureElement.equals(str2)) {
                    offHostSecureElement = "SIM1";
                }
                try {
                    z = sService.setOffHostForService(this.mContext.getUserId(), service, offHostSecureElement);
                    return z;
                } catch (RemoteException e) {
                    recoverService();
                    INfcCardEmulation iNfcCardEmulation = sService;
                    String str3 = TAG;
                    if (iNfcCardEmulation == null) {
                        Log.e(str3, "Failed to recover CardEmulationService.");
                        return z;
                    }
                    try {
                        z = iNfcCardEmulation.setOffHostForService(this.mContext.getUserId(), service, offHostSecureElement);
                        return z;
                    } catch (RemoteException e2) {
                        Log.e(str3, "Failed to reach CardEmulationService.");
                        return z;
                    }
                }
            }
        }
        return false;
    }

    public List<String> getAidsForService(ComponentName service, String category) {
        List<String> list = null;
        try {
            AidGroup group = sService.getAidGroupForService(this.mContext.getUserId(), service, category);
            if (group != null) {
                list = group.getAids();
            }
            return list;
        } catch (RemoteException e) {
            recoverService();
            AidGroup group2 = sService;
            String str = "Failed to recover CardEmulationService.";
            String str2 = TAG;
            if (group2 == null) {
                Log.e(str2, str);
                return null;
            }
            try {
                group2 = group2.getAidGroupForService(this.mContext.getUserId(), service, category);
                if (group2 != null) {
                    list = group2.getAids();
                }
                return list;
            } catch (RemoteException e2) {
                Log.e(str2, str);
                return null;
            }
        }
    }

    public boolean removeAidsForService(ComponentName service, String category) {
        try {
            return sService.removeAidGroupForService(this.mContext.getUserId(), service, category);
        } catch (RemoteException e) {
            recoverService();
            INfcCardEmulation iNfcCardEmulation = sService;
            String str = TAG;
            if (iNfcCardEmulation == null) {
                Log.e(str, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.removeAidGroupForService(this.mContext.getUserId(), service, category);
            } catch (RemoteException e2) {
                Log.e(str, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public boolean setPreferredService(Activity activity, ComponentName service) {
        if (activity == null || service == null) {
            throw new NullPointerException("activity or service or category is null");
        } else if (activity.isResumed()) {
            try {
                return sService.setPreferredService(service);
            } catch (RemoteException e) {
                recoverService();
                INfcCardEmulation iNfcCardEmulation = sService;
                String str = TAG;
                if (iNfcCardEmulation == null) {
                    Log.e(str, "Failed to recover CardEmulationService.");
                    return false;
                }
                try {
                    return iNfcCardEmulation.setPreferredService(service);
                } catch (RemoteException e2) {
                    Log.e(str, "Failed to reach CardEmulationService.");
                    return false;
                }
            }
        } else {
            throw new IllegalArgumentException("Activity must be resumed.");
        }
    }

    public boolean unsetPreferredService(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity is null");
        } else if (activity.isResumed()) {
            try {
                return sService.unsetPreferredService();
            } catch (RemoteException e) {
                recoverService();
                INfcCardEmulation iNfcCardEmulation = sService;
                String str = TAG;
                if (iNfcCardEmulation == null) {
                    Log.e(str, "Failed to recover CardEmulationService.");
                    return false;
                }
                try {
                    return iNfcCardEmulation.unsetPreferredService();
                } catch (RemoteException e2) {
                    Log.e(str, "Failed to reach CardEmulationService.");
                    return false;
                }
            }
        } else {
            throw new IllegalArgumentException("Activity must be resumed.");
        }
    }

    public boolean supportsAidPrefixRegistration() {
        try {
            return sService.supportsAidPrefixRegistration();
        } catch (RemoteException e) {
            recoverService();
            INfcCardEmulation iNfcCardEmulation = sService;
            String str = TAG;
            if (iNfcCardEmulation == null) {
                Log.e(str, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.supportsAidPrefixRegistration();
            } catch (RemoteException e2) {
                Log.e(str, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public boolean setDefaultServiceForCategory(ComponentName service, String category) {
        try {
            return sService.setDefaultServiceForCategory(this.mContext.getUserId(), service, category);
        } catch (RemoteException e) {
            recoverService();
            INfcCardEmulation iNfcCardEmulation = sService;
            String str = TAG;
            if (iNfcCardEmulation == null) {
                Log.e(str, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.setDefaultServiceForCategory(this.mContext.getUserId(), service, category);
            } catch (RemoteException e2) {
                Log.e(str, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public boolean setDefaultForNextTap(ComponentName service) {
        try {
            return sService.setDefaultForNextTap(this.mContext.getUserId(), service);
        } catch (RemoteException e) {
            recoverService();
            INfcCardEmulation iNfcCardEmulation = sService;
            String str = TAG;
            if (iNfcCardEmulation == null) {
                Log.e(str, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.setDefaultForNextTap(this.mContext.getUserId(), service);
            } catch (RemoteException e2) {
                Log.e(str, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public List<ApduServiceInfo> getServices(String category) {
        try {
            return sService.getServices(this.mContext.getUserId(), category);
        } catch (RemoteException e) {
            recoverService();
            INfcCardEmulation iNfcCardEmulation = sService;
            String str = TAG;
            if (iNfcCardEmulation == null) {
                Log.e(str, "Failed to recover CardEmulationService.");
                return null;
            }
            try {
                return iNfcCardEmulation.getServices(this.mContext.getUserId(), category);
            } catch (RemoteException e2) {
                Log.e(str, "Failed to reach CardEmulationService.");
                return null;
            }
        }
    }

    public static boolean isValidAid(String aid) {
        if (aid == null) {
            return false;
        }
        String str = "*";
        boolean endsWith = aid.endsWith(str);
        String str2 = "#";
        String str3 = " is not a valid AID.";
        String str4 = "AID ";
        String str5 = TAG;
        StringBuilder stringBuilder;
        if ((endsWith || aid.endsWith(str2)) && aid.length() % 2 == 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str4);
            stringBuilder.append(aid);
            stringBuilder.append(str3);
            Log.e(str5, stringBuilder.toString());
            return false;
        } else if (!aid.endsWith(str) && !aid.endsWith(str2) && aid.length() % 2 != 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str4);
            stringBuilder.append(aid);
            stringBuilder.append(str3);
            Log.e(str5, stringBuilder.toString());
            return false;
        } else if (AID_PATTERN.matcher(aid).matches()) {
            return true;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str4);
            stringBuilder.append(aid);
            stringBuilder.append(str3);
            Log.e(str5, stringBuilder.toString());
            return false;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void recoverService() {
        sService = NfcAdapter.getDefaultAdapter(this.mContext).getCardEmulationService();
    }
}
