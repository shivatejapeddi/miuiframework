package com.android.internal.util;

import android.os.Bundle;
import android.os.Parcelable;
import com.android.internal.os.IResultReceiver.Stub;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class SyncResultReceiver extends Stub {
    private static final String EXTRA = "EXTRA";
    private Bundle mBundle;
    private final CountDownLatch mLatch = new CountDownLatch(1);
    private int mResult;
    private final int mTimeoutMs;

    public static final class TimeoutException extends RuntimeException {
        private TimeoutException(String msg) {
            super(msg);
        }
    }

    public SyncResultReceiver(int timeoutMs) {
        this.mTimeoutMs = timeoutMs;
    }

    private void waitResult() throws TimeoutException {
        try {
            if (!this.mLatch.await((long) this.mTimeoutMs, TimeUnit.MILLISECONDS)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Not called in ");
                stringBuilder.append(this.mTimeoutMs);
                stringBuilder.append("ms");
                throw new TimeoutException(stringBuilder.toString());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TimeoutException("Interrupted");
        }
    }

    public int getIntResult() throws TimeoutException {
        waitResult();
        return this.mResult;
    }

    public String getStringResult() throws TimeoutException {
        waitResult();
        Bundle bundle = this.mBundle;
        return bundle == null ? null : bundle.getString(EXTRA);
    }

    public String[] getStringArrayResult() throws TimeoutException {
        waitResult();
        Bundle bundle = this.mBundle;
        return bundle == null ? null : bundle.getStringArray(EXTRA);
    }

    public <P extends Parcelable> P getParcelableResult() throws TimeoutException {
        waitResult();
        Bundle bundle = this.mBundle;
        return bundle == null ? null : bundle.getParcelable(EXTRA);
    }

    public <P extends Parcelable> ArrayList<P> getParcelableListResult() throws TimeoutException {
        waitResult();
        Bundle bundle = this.mBundle;
        return bundle == null ? null : bundle.getParcelableArrayList(EXTRA);
    }

    public int getOptionalExtraIntResult(int defaultValue) throws TimeoutException {
        waitResult();
        Bundle bundle = this.mBundle;
        if (bundle != null) {
            String str = EXTRA;
            if (bundle.containsKey(str)) {
                return this.mBundle.getInt(str);
            }
        }
        return defaultValue;
    }

    public void send(int resultCode, Bundle resultData) {
        this.mResult = resultCode;
        this.mBundle = resultData;
        this.mLatch.countDown();
    }

    public static Bundle bundleFor(String value) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA, value);
        return bundle;
    }

    public static Bundle bundleFor(String[] value) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(EXTRA, value);
        return bundle;
    }

    public static Bundle bundleFor(Parcelable value) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA, value);
        return bundle;
    }

    public static Bundle bundleFor(ArrayList<? extends Parcelable> value) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA, value);
        return bundle;
    }

    public static Bundle bundleFor(int value) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA, value);
        return bundle;
    }
}
