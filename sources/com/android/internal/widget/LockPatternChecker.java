package com.android.internal.widget;

import android.annotation.UnsupportedAppUsage;
import android.os.AsyncTask;
import com.android.internal.widget.LockPatternUtils.RequestThrottledException;
import com.android.internal.widget.LockPatternView.Cell;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class LockPatternChecker {

    public interface OnCheckCallback {
        void onChecked(boolean z, int i);

        void onEarlyMatched() {
        }

        void onCancelled() {
        }
    }

    public interface OnVerifyCallback {
        void onVerified(byte[] bArr, int i);
    }

    public static AsyncTask<?, ?, ?> verifyPattern(LockPatternUtils utils, List<Cell> pattern, long challenge, int userId, OnVerifyCallback callback) {
        final List<Cell> list = pattern;
        final LockPatternUtils lockPatternUtils = utils;
        final long j = challenge;
        final int i = userId;
        final OnVerifyCallback onVerifyCallback = callback;
        AsyncTask task = new AsyncTask<Void, Void, byte[]>() {
            private int mThrottleTimeout;
            private List<Cell> patternCopy;

            /* Access modifiers changed, original: protected */
            public void onPreExecute() {
                this.patternCopy = new ArrayList(list);
            }

            /* Access modifiers changed, original: protected|varargs */
            public byte[] doInBackground(Void... args) {
                try {
                    return lockPatternUtils.verifyPattern(this.patternCopy, j, i);
                } catch (RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return null;
                }
            }

            /* Access modifiers changed, original: protected */
            public void onPostExecute(byte[] result) {
                onVerifyCallback.onVerified(result, this.mThrottleTimeout);
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    public static AsyncTask<?, ?, ?> checkPattern(final LockPatternUtils utils, final List<Cell> pattern, final int userId, final OnCheckCallback callback) {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            private int mThrottleTimeout;
            private List<Cell> patternCopy;

            /* Access modifiers changed, original: protected */
            public void onPreExecute() {
                this.patternCopy = new ArrayList(pattern);
            }

            /* Access modifiers changed, original: protected|varargs */
            public Boolean doInBackground(Void... args) {
                try {
                    LockPatternUtils lockPatternUtils = utils;
                    List list = this.patternCopy;
                    int i = userId;
                    OnCheckCallback onCheckCallback = callback;
                    Objects.requireNonNull(onCheckCallback);
                    return Boolean.valueOf(lockPatternUtils.checkPattern(list, i, new -$$Lambda$TTC7hNz7BTsLwhNRb2L5kl-7mdU(onCheckCallback)));
                } catch (RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return Boolean.valueOf(false);
                }
            }

            /* Access modifiers changed, original: protected */
            public void onPostExecute(Boolean result) {
                callback.onChecked(result.booleanValue(), this.mThrottleTimeout);
            }

            /* Access modifiers changed, original: protected */
            public void onCancelled() {
                callback.onCancelled();
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    @Deprecated
    public static AsyncTask<?, ?, ?> verifyPassword(LockPatternUtils utils, String password, long challenge, int userId, OnVerifyCallback callback) {
        return verifyPassword(utils, password != null ? password.getBytes() : null, challenge, userId, callback);
    }

    public static AsyncTask<?, ?, ?> verifyPassword(LockPatternUtils utils, byte[] password, long challenge, int userId, OnVerifyCallback callback) {
        final LockPatternUtils lockPatternUtils = utils;
        final byte[] bArr = password;
        final long j = challenge;
        final int i = userId;
        final OnVerifyCallback onVerifyCallback = callback;
        AsyncTask task = new AsyncTask<Void, Void, byte[]>() {
            private int mThrottleTimeout;

            /* Access modifiers changed, original: protected|varargs */
            public byte[] doInBackground(Void... args) {
                try {
                    return lockPatternUtils.verifyPassword(bArr, j, i);
                } catch (RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return null;
                }
            }

            /* Access modifiers changed, original: protected */
            public void onPostExecute(byte[] result) {
                onVerifyCallback.onVerified(result, this.mThrottleTimeout);
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    public static AsyncTask<?, ?, ?> verifyTiedProfileChallenge(LockPatternUtils utils, byte[] password, boolean isPattern, long challenge, int userId, OnVerifyCallback callback) {
        final LockPatternUtils lockPatternUtils = utils;
        final byte[] bArr = password;
        final boolean z = isPattern;
        final long j = challenge;
        final int i = userId;
        final OnVerifyCallback onVerifyCallback = callback;
        AsyncTask task = new AsyncTask<Void, Void, byte[]>() {
            private int mThrottleTimeout;

            /* Access modifiers changed, original: protected|varargs */
            public byte[] doInBackground(Void... args) {
                try {
                    return lockPatternUtils.verifyTiedProfileChallenge(bArr, z, j, i);
                } catch (RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return null;
                }
            }

            /* Access modifiers changed, original: protected */
            public void onPostExecute(byte[] result) {
                onVerifyCallback.onVerified(result, this.mThrottleTimeout);
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    @Deprecated
    @UnsupportedAppUsage
    public static AsyncTask<?, ?, ?> checkPassword(LockPatternUtils utils, String password, int userId, OnCheckCallback callback) {
        return checkPassword(utils, password != null ? password.getBytes() : null, userId, callback);
    }

    public static AsyncTask<?, ?, ?> checkPassword(final LockPatternUtils utils, final byte[] passwordBytes, final int userId, final OnCheckCallback callback) {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            private int mThrottleTimeout;

            /* Access modifiers changed, original: protected|varargs */
            public Boolean doInBackground(Void... args) {
                try {
                    LockPatternUtils lockPatternUtils = utils;
                    byte[] bArr = passwordBytes;
                    int i = userId;
                    OnCheckCallback onCheckCallback = callback;
                    Objects.requireNonNull(onCheckCallback);
                    return Boolean.valueOf(lockPatternUtils.checkPassword(bArr, i, new -$$Lambda$TTC7hNz7BTsLwhNRb2L5kl-7mdU(onCheckCallback)));
                } catch (RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return Boolean.valueOf(false);
                }
            }

            /* Access modifiers changed, original: protected */
            public void onPostExecute(Boolean result) {
                callback.onChecked(result.booleanValue(), this.mThrottleTimeout);
            }

            /* Access modifiers changed, original: protected */
            public void onCancelled() {
                callback.onCancelled();
            }
        };
        task.execute(new Void[0]);
        return task;
    }
}
