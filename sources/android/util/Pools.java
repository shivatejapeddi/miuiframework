package android.util;

import android.annotation.UnsupportedAppUsage;

public final class Pools {

    public interface Pool<T> {
        @UnsupportedAppUsage
        T acquire();

        @UnsupportedAppUsage
        boolean release(T t);
    }

    public static class SimplePool<T> implements Pool<T> {
        @UnsupportedAppUsage
        private final Object[] mPool;
        private int mPoolSize;

        @UnsupportedAppUsage
        public SimplePool(int maxPoolSize) {
            if (maxPoolSize > 0) {
                this.mPool = new Object[maxPoolSize];
                return;
            }
            throw new IllegalArgumentException("The max pool size must be > 0");
        }

        @UnsupportedAppUsage
        public T acquire() {
            int i = this.mPoolSize;
            if (i <= 0) {
                return null;
            }
            int lastPooledIndex = i - 1;
            Object[] objArr = this.mPool;
            T instance = objArr[lastPooledIndex];
            objArr[lastPooledIndex] = null;
            this.mPoolSize = i - 1;
            return instance;
        }

        @UnsupportedAppUsage
        public boolean release(T instance) {
            if (isInPool(instance)) {
                throw new IllegalStateException("Already in the pool!");
            }
            int i = this.mPoolSize;
            Object[] objArr = this.mPool;
            if (i >= objArr.length) {
                return false;
            }
            objArr[i] = instance;
            this.mPoolSize = i + 1;
            return true;
        }

        private boolean isInPool(T instance) {
            for (int i = 0; i < this.mPoolSize; i++) {
                if (this.mPool[i] == instance) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class SynchronizedPool<T> extends SimplePool<T> {
        private final Object mLock;

        public SynchronizedPool(int maxPoolSize, Object lock) {
            super(maxPoolSize);
            this.mLock = lock;
        }

        @UnsupportedAppUsage
        public SynchronizedPool(int maxPoolSize) {
            this(maxPoolSize, new Object());
        }

        @UnsupportedAppUsage
        public T acquire() {
            Object acquire;
            synchronized (this.mLock) {
                acquire = super.acquire();
            }
            return acquire;
        }

        @UnsupportedAppUsage
        public boolean release(T element) {
            boolean release;
            synchronized (this.mLock) {
                release = super.release(element);
            }
            return release;
        }
    }

    private Pools() {
    }
}
