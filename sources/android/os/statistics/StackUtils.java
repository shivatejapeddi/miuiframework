package android.os.statistics;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.miui.commons.lang3.ClassUtils;
import org.json.JSONArray;
import org.json.JSONException;

class StackUtils {
    private static final int MAX_STACK_DEPTH = 32;
    private static final int MAX_STACK_LENGH = 1024;
    public static final JSONArray emptyJsonedStack = new JSONArray();
    public static final String[] emptyStack = new String[0];
    private static final StringBuilder sStackTraceStringBuilder = new StringBuilder(256);
    private static final AtomicBoolean sStackTraceStringBuilderBusy = new AtomicBoolean(false);
    private static final String[] suggestedStackTopMethodFullNames = new String[]{"java.lang.Thread.sleep", "java.lang.Thread.join", "java.util.concurrent.Semaphore.acquire", "java.util.concurrent.Semaphore.acquireUninterruptibly", "java.util.concurrent.Semaphore.tryAcquire", "java.util.concurrent.Semaphore.release", "java.util.concurrent.locks.AbstractQueuedLongSynchronizer.acquire", "java.util.concurrent.locks.AbstractQueuedLongSynchronizer.acquireInterruptibly", "java.util.concurrent.locks.AbstractQueuedLongSynchronizer.tryAcquireNanos", "java.util.concurrent.locks.AbstractQueuedLongSynchronizer.release", "java.util.concurrent.locks.AbstractQueuedLongSynchronizer.acquireShared", "java.util.concurrent.locks.AbstractQueuedLongSynchronizer.acquireSharedInterruptibly", "java.util.concurrent.locks.AbstractQueuedLongSynchronizer.tryAcquireSharedNanos", "java.util.concurrent.locks.AbstractQueuedLongSynchronizer.releaseShared", "java.util.concurrent.locks.AbstractQueuedLongSynchronizer$ConditionObject.signal", "java.util.concurrent.locks.AbstractQueuedLongSynchronizer$ConditionObject.signalAll", "java.util.concurrent.locks.AbstractQueuedLongSynchronizer$ConditionObject.await", "java.util.concurrent.locks.AbstractQueuedLongSynchronizer$ConditionObject.awaitNanos", "java.util.concurrent.locks.AbstractQueuedLongSynchronizer$ConditionObject.awaitUntil", "java.util.concurrent.locks.AbstractQueuedLongSynchronizer$ConditionObject.awaitUninterruptibly", "java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire", "java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireInterruptibly", "java.util.concurrent.locks.AbstractQueuedSynchronizer.tryAcquireNanos", "java.util.concurrent.locks.AbstractQueuedSynchronizer.release", "java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireShared", "java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireSharedInterruptibly", "java.util.concurrent.locks.AbstractQueuedSynchronizer.tryAcquireSharedNanos", "java.util.concurrent.locks.AbstractQueuedSynchronizer.releaseShared", "java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.signal", "java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.signalAll", "java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await", "java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.awaitNanos", "java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.awaitUntil", "java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.awaitUninterruptibly", "java.util.concurrent.locks.ReentrantLock.lock", "java.util.concurrent.locks.ReentrantLock.lockInterruptibly", "java.util.concurrent.locks.ReentrantLock.tryLock", "java.util.concurrent.locks.ReentrantLock.unlock", "java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock.lock", "java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock.lockInterruptibly", "java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock.tryLock", "java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock.unlock", "java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock.lock", "java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock.lockInterruptibly", "java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock.tryLock", "java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock.unlock", "java.util.concurrent.locks.StampedLock$ReadLockView.lock", "java.util.concurrent.locks.StampedLock$ReadLockView.lockInterruptibly", "java.util.concurrent.locks.StampedLock$ReadLockView.tryLock", "java.util.concurrent.locks.StampedLock$ReadLockView.unlock", "java.util.concurrent.locks.StampedLock$WriteLockView.lock", "java.util.concurrent.locks.StampedLock$WriteLockView.lockInterruptibly", "java.util.concurrent.locks.StampedLock$WriteLockView.tryLock", "java.util.concurrent.locks.StampedLock$WriteLockView.unlock", "java.util.concurrent.locks.StampedLock.writeLock", "java.util.concurrent.locks.StampedLock.tryWriteLock", "java.util.concurrent.locks.StampedLock.writeLockInterruptibly", "java.util.concurrent.locks.StampedLock.readLock", "java.util.concurrent.locks.StampedLock.tryReadLock", "java.util.concurrent.locks.StampedLock.readLockInterruptibly", "java.util.concurrent.locks.StampedLock.unlockWrite", "java.util.concurrent.locks.StampedLock.unlockRead", "java.util.concurrent.locks.StampedLock.unlock", "java.util.concurrent.locks.StampedLock.tryConvertToReadLock", "java.util.concurrent.locks.StampedLock.tryConvertToWriteLock", "java.util.concurrent.locks.StampedLock.tryConvertToOptimisticRead", "java.util.concurrent.locks.StampedLock.tryUnlockWrite", "java.util.concurrent.locks.StampedLock.tryUnlockRead", "java.util.concurrent.locks.StampedLock.unstampedUnlockRead", "java.util.concurrent.locks.LockSupport.park", "java.util.concurrent.locks.LockSupport.parkNanos", "java.util.concurrent.locks.LockSupport.parkUntil", "java.util.concurrent.locks.LockSupport.unpark"};
    private static volatile ArrayList[] topClassMethodSimpleNames;
    private static volatile Class[] topClasses;

    StackUtils() {
    }

    public static String[] getStackTrace(StackTraceElement[] javaStackTraceElements, Class[] javaStackTraceClasses, String[] nativeStackTrace) {
        int i;
        StackTraceElement[] stackTraceElementArr = javaStackTraceElements;
        Class[] clsArr = javaStackTraceClasses;
        String[] strArr = nativeStackTrace;
        int length = 0;
        int depth = 0;
        ArrayList<String> stackTrace = new ArrayList(32);
        if (strArr != null && strArr.length > 0) {
            for (String line : strArr) {
                if (!TextUtils.isEmpty(line)) {
                    stackTrace.add(line);
                    depth++;
                    length += line.length();
                    if (depth < 32 && length < 1024) {
                    }
                }
            }
            i = length;
            if (depth < 32 || length >= 1024 || stackTraceElementArr == null || stackTraceElementArr.length == 0) {
                return (String[]) stackTrace.toArray(new String[stackTrace.size()]);
            }
            Class clazz;
            int i2;
            int i3 = 0;
            if (topClassMethodSimpleNames == null) {
                synchronized (StackUtils.class) {
                    if (topClassMethodSimpleNames == null) {
                        HashMap<Class, ArrayList<String>> topClassMethodSimpleNamesMap = new HashMap();
                        String[] strArr2 = suggestedStackTopMethodFullNames;
                        int length2 = strArr2.length;
                        int i4 = 0;
                        while (i4 < length2) {
                            String methodFullName = strArr2[i4];
                            try {
                                ArrayList<String> topMethodSimpleNames;
                                length = methodFullName.lastIndexOf(".");
                                String methodSimpleName = methodFullName.substring(length + 1);
                                Class clazz2 = Class.forName(methodFullName.substring(i3, length));
                                ArrayList<String> topMethodSimpleNames2 = (ArrayList) topClassMethodSimpleNamesMap.get(clazz2);
                                if (topMethodSimpleNames2 == null) {
                                    topMethodSimpleNames = new ArrayList();
                                    topClassMethodSimpleNamesMap.put(clazz2, topMethodSimpleNames);
                                } else {
                                    topMethodSimpleNames = topMethodSimpleNames2;
                                }
                                topMethodSimpleNames.add(methodSimpleName);
                            } catch (ClassNotFoundException e) {
                            }
                            i4++;
                            i3 = 0;
                        }
                        Class[] localTopClasses = (Class[]) topClassMethodSimpleNamesMap.keySet().toArray(new Class[topClassMethodSimpleNamesMap.size()]);
                        ArrayList[] localTopClassMethodSimpleNames = new ArrayList[localTopClasses.length];
                        int index = 0;
                        for (Class clazz3 : topClassMethodSimpleNamesMap.keySet()) {
                            localTopClasses[index] = clazz3;
                            localTopClassMethodSimpleNames[index] = (ArrayList) topClassMethodSimpleNamesMap.get(clazz3);
                            index++;
                        }
                        topClasses = localTopClasses;
                        topClassMethodSimpleNames = localTopClassMethodSimpleNames;
                    }
                }
            }
            length = 0;
            boolean needSkipPart = false;
            if (clsArr != null) {
                if (clsArr.length >= 1 && clsArr[0] == Thread.class) {
                    needSkipPart = true;
                } else if (clsArr.length >= 4 && clsArr[0] == Object.class && (clsArr[1] == Thread.class || clsArr[2] == Thread.class || clsArr[3] == Thread.class)) {
                    needSkipPart = true;
                }
            }
            if (needSkipPart) {
                length = stackTraceElementArr.length - 1;
                while (length > 0) {
                    clazz = clsArr[length];
                    if (clazz != null) {
                        int index2 = -1;
                        for (i2 = 0; i2 < topClasses.length; i2++) {
                            if (topClasses[i2] == clazz) {
                                index2 = i2;
                                break;
                            }
                        }
                        if (index2 >= 0) {
                            if (topClassMethodSimpleNames[index2].contains(stackTraceElementArr[length].getMethodName())) {
                                break;
                            }
                        } else {
                            continue;
                        }
                    }
                    length--;
                }
            }
            clazz = null;
            StringBuilder sb = sStackTraceStringBuilderBusy.compareAndSet(false, true) ? sStackTraceStringBuilder : new StringBuilder(256);
            boolean foundTopLine = false;
            i2 = length;
            while (i2 < stackTraceElementArr.length) {
                StackTraceElement stackTraceElement = stackTraceElementArr[i2];
                if (stackTraceElement != null) {
                    sb.setLength(0);
                    Class clazz4 = clsArr == null ? null : clsArr[i2];
                    if (clazz4 == null || previousClazz != clazz4) {
                        sb.append(stackTraceElement.getClassName());
                        sb.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
                    } else {
                        sb.append('-');
                        sb.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
                    }
                    sb.append(stackTraceElement.getMethodName());
                    if (!foundTopLine) {
                        foundTopLine = true;
                        if (stackTraceElement.isNativeMethod()) {
                            sb.append("(Native)");
                        } else if (stackTraceElement.getFileName() != null) {
                            sb.append('(');
                            sb.append(stackTraceElement.getFileName());
                            sb.append(':');
                            sb.append(stackTraceElement.getLineNumber());
                            sb.append(')');
                        } else {
                            sb.append("(None)");
                        }
                    }
                    clazz = clazz4;
                    stackTrace.add(sb.toString());
                    depth++;
                    i += sb.length();
                    if (depth >= 32 || i >= 1024) {
                        break;
                    }
                }
                i2++;
            }
            if (sb == sStackTraceStringBuilder) {
                sStackTraceStringBuilderBusy.set(false);
            }
            return (String[]) stackTrace.toArray(new String[stackTrace.size()]);
        }
        i = length;
        if (depth < 32) {
        }
        return (String[]) stackTrace.toArray(new String[stackTrace.size()]);
    }

    public static String[] getStackTrace(JSONArray jsonedStackTrace) {
        if (jsonedStackTrace == null) {
            return emptyStack;
        }
        String[] stackTrace = new String[jsonedStackTrace.length()];
        for (int i = 0; i < stackTrace.length; i++) {
            try {
                stackTrace[i] = jsonedStackTrace.getString(i);
            } catch (JSONException e) {
                stackTrace[i] = "";
            }
        }
        return stackTrace;
    }
}
