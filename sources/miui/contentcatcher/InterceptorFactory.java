package miui.contentcatcher;

import android.app.Activity;
import android.app.AppGlobals;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.SystemClock;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.miui.internal.contentcatcher.IInterceptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InterceptorFactory {
    private static final String CONTENT_CATCHER_PACKAGE_NAME = "com.miui.contentcatcher";
    private static final String CONTENT_INJECTOR_CLASS_NAME = "com.miui.contentcatcher.Interceptor";
    private static final boolean DBG = InterceptorProxy.DBG;
    private static final String TAG = "InterceptorFactory";
    @GuardedBy({"mPackageInfoLock"})
    private static volatile PackageInfo mPackageInfo = null;
    private static final Object mPackageInfoLock = new Object();
    @GuardedBy({"InterceptorFactory.class"})
    private static volatile Class<?> sInterceptorClazz = null;

    public static IInterceptor createInterceptor(Activity activity) {
        String str = TAG;
        IInterceptor contentInjector = null;
        try {
            long start = SystemClock.uptimeMillis();
            Class<?> injectorClazz = initInterceptorClass();
            if (injectorClazz == null) {
                return null;
            }
            Constructor injectorConstructor = injectorClazz.getConstructor(new Class[]{Activity.class});
            if (injectorConstructor == null) {
                return null;
            }
            if (!injectorConstructor.isAccessible()) {
                injectorConstructor.setAccessible(true);
            }
            contentInjector = (IInterceptor) injectorConstructor.newInstance(new Object[]{activity});
            if (DBG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("createInterceptor took ");
                stringBuilder.append(SystemClock.uptimeMillis() - start);
                stringBuilder.append("ms");
                Log.d(str, stringBuilder.toString());
            }
            return contentInjector;
        } catch (InstantiationException e) {
            Log.e(str, "InstantiationException", e);
        } catch (IllegalAccessException e2) {
            Log.e(str, "IllegalAccessException", e2);
        } catch (InvocationTargetException e3) {
            Log.e(str, "InvocationTargetException", e3);
        } catch (NoSuchMethodException e4) {
            Log.e(str, "NoSuchMethodException", e4);
        } catch (Exception e5) {
            Log.e(str, "Exception", e5);
        }
    }

    public static PackageInfo getInterceptorPackageInfo() {
        if (DBG) {
            Log.d(TAG, "getInterceptorPackageInfo");
        }
        if (mPackageInfo == null) {
            synchronized (mPackageInfoLock) {
                if (mPackageInfo == null) {
                    try {
                        long start = SystemClock.uptimeMillis();
                        PackageManager pm = AppGlobals.getInitialApplication().getPackageManager();
                        if (pm == null) {
                            return null;
                        }
                        mPackageInfo = pm.getPackageInfo(CONTENT_CATCHER_PACKAGE_NAME, 0);
                        if (DBG) {
                            String str = TAG;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("getPackageInfo took ");
                            stringBuilder.append(SystemClock.uptimeMillis() - start);
                            stringBuilder.append("ms");
                            Log.d(str, stringBuilder.toString());
                        }
                    } catch (NameNotFoundException e) {
                        Log.e(TAG, "NameNotFoundException", e);
                    } catch (Exception e1) {
                        Log.e(TAG, "Exception", e1);
                    }
                }
            }
        }
        return mPackageInfo;
        return mPackageInfo;
    }

    private static Class<?> initInterceptorClass() {
        if (DBG) {
            Log.d(TAG, "initInterceptorClass");
        }
        if (sInterceptorClazz == null) {
            synchronized (InterceptorFactory.class) {
                if (sInterceptorClazz == null) {
                    long start = SystemClock.uptimeMillis();
                    PackageInfo packageInfo = getInterceptorPackageInfo();
                    if (packageInfo != null) {
                        Application initialApplication = AppGlobals.getInitialApplication();
                        try {
                            if (DBG) {
                                String str = TAG;
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("packageInfo.packageName: ");
                                stringBuilder.append(packageInfo.packageName);
                                Log.d(str, stringBuilder.toString());
                            }
                            sInterceptorClazz = Class.forName(CONTENT_INJECTOR_CLASS_NAME, true, initialApplication.createPackageContext(packageInfo.packageName, 3).getClassLoader());
                            if (DBG) {
                                String str2 = TAG;
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("initInterceptorClass took ");
                                stringBuilder2.append(SystemClock.uptimeMillis() - start);
                                stringBuilder2.append("ms");
                                Log.d(str2, stringBuilder2.toString());
                            }
                        } catch (NameNotFoundException e) {
                            Log.e(TAG, "NameNotFoundException", e);
                        } catch (Exception e1) {
                            Log.e(TAG, "Exception ", e1);
                        } catch (Error error) {
                            Log.e(TAG, "Error ", error);
                        }
                    }
                }
            }
        }
        return sInterceptorClazz;
    }
}
