package android.app.servertransaction;

import android.app.ActivityThread.ActivityClientRecord;
import android.app.ClientTransactionHandler;
import android.os.IBinder;
import android.util.IntArray;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import java.util.List;
import java.util.Map;

public class TransactionExecutor {
    private static final boolean DEBUG_RESOLVER = false;
    private static final String TAG = "TransactionExecutor";
    private TransactionExecutorHelper mHelper = new TransactionExecutorHelper();
    private PendingTransactionActions mPendingActions = new PendingTransactionActions();
    private ClientTransactionHandler mTransactionHandler;

    public TransactionExecutor(ClientTransactionHandler clientTransactionHandler) {
        this.mTransactionHandler = clientTransactionHandler;
    }

    public void execute(ClientTransaction transaction) {
        IBinder token = transaction.getActivityToken();
        if (token != null) {
            Map<IBinder, ClientTransactionItem> activitiesToBeDestroyed = this.mTransactionHandler.getActivitiesToBeDestroyed();
            ClientTransactionItem destroyItem = (ClientTransactionItem) activitiesToBeDestroyed.get(token);
            if (destroyItem != null) {
                if (transaction.getLifecycleStateRequest() == destroyItem) {
                    activitiesToBeDestroyed.remove(token);
                }
                if (this.mTransactionHandler.getActivityClient(token) == null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(TransactionExecutorHelper.tId(transaction));
                    stringBuilder.append("Skip pre-destroyed transaction:\n");
                    stringBuilder.append(TransactionExecutorHelper.transactionToString(transaction, this.mTransactionHandler));
                    Slog.w(TAG, stringBuilder.toString());
                    return;
                }
            }
        }
        executeCallbacks(transaction);
        executeLifecycleState(transaction);
        this.mPendingActions.clear();
    }

    @VisibleForTesting
    public void executeCallbacks(ClientTransaction transaction) {
        List<ClientTransactionItem> callbacks = transaction.getCallbacks();
        if (callbacks != null && !callbacks.isEmpty()) {
            int finalState;
            IBinder token = transaction.getActivityToken();
            ActivityClientRecord r = this.mTransactionHandler.getActivityClient(token);
            ActivityLifecycleItem finalStateRequest = transaction.getLifecycleStateRequest();
            if (finalStateRequest != null) {
                finalState = finalStateRequest.getTargetState();
            } else {
                finalState = -1;
            }
            int lastCallbackRequestingState = TransactionExecutorHelper.lastCallbackRequestingState(transaction);
            int size = callbacks.size();
            int i = 0;
            while (i < size) {
                ClientTransactionItem item = (ClientTransactionItem) callbacks.get(i);
                int postExecutionState = item.getPostExecutionState();
                int closestPreExecutionState = this.mHelper.getClosestPreExecutionState(r, item.getPostExecutionState());
                if (closestPreExecutionState != -1) {
                    cycleToPath(r, closestPreExecutionState, transaction);
                }
                item.execute(this.mTransactionHandler, token, this.mPendingActions);
                item.postExecute(this.mTransactionHandler, token, this.mPendingActions);
                if (r == null) {
                    r = this.mTransactionHandler.getActivityClient(token);
                }
                if (!(postExecutionState == -1 || r == null)) {
                    boolean shouldExcludeLastTransition = i == lastCallbackRequestingState && finalState == postExecutionState;
                    cycleToPath(r, postExecutionState, shouldExcludeLastTransition, transaction);
                }
                i++;
            }
        }
    }

    private void executeLifecycleState(ClientTransaction transaction) {
        ActivityLifecycleItem lifecycleItem = transaction.getLifecycleStateRequest();
        if (lifecycleItem != null) {
            IBinder token = transaction.getActivityToken();
            ActivityClientRecord r = this.mTransactionHandler.getActivityClient(token);
            if (r != null) {
                cycleToPath(r, lifecycleItem.getTargetState(), true, transaction);
                lifecycleItem.execute(this.mTransactionHandler, token, this.mPendingActions);
                lifecycleItem.postExecute(this.mTransactionHandler, token, this.mPendingActions);
            }
        }
    }

    @VisibleForTesting
    public void cycleToPath(ActivityClientRecord r, int finish, ClientTransaction transaction) {
        cycleToPath(r, finish, false, transaction);
    }

    private void cycleToPath(ActivityClientRecord r, int finish, boolean excludeLastState, ClientTransaction transaction) {
        performLifecycleSequence(r, this.mHelper.getLifecyclePath(r.getLifecycleState(), finish, excludeLastState), transaction);
    }

    private void performLifecycleSequence(ActivityClientRecord r, IntArray path, ClientTransaction transaction) {
        ActivityClientRecord activityClientRecord = r;
        IntArray intArray = path;
        int size = path.size();
        for (int i = 0; i < size; i++) {
            int state = intArray.get(i);
            switch (state) {
                case 1:
                    this.mTransactionHandler.handleLaunchActivity(activityClientRecord, this.mPendingActions, null);
                    break;
                case 2:
                    this.mTransactionHandler.handleStartActivity(activityClientRecord, this.mPendingActions);
                    break;
                case 3:
                    this.mTransactionHandler.handleResumeActivity(activityClientRecord.token, false, activityClientRecord.isForward, "LIFECYCLER_RESUME_ACTIVITY");
                    break;
                case 4:
                    this.mTransactionHandler.handlePauseActivity(activityClientRecord.token, false, false, 0, this.mPendingActions, "LIFECYCLER_PAUSE_ACTIVITY");
                    break;
                case 5:
                    this.mTransactionHandler.handleStopActivity(activityClientRecord.token, false, 0, this.mPendingActions, false, "LIFECYCLER_STOP_ACTIVITY");
                    break;
                case 6:
                    ClientTransactionHandler clientTransactionHandler = this.mTransactionHandler;
                    IBinder iBinder = activityClientRecord.token;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("performLifecycleSequence. cycling to:");
                    stringBuilder.append(intArray.get(size - 1));
                    clientTransactionHandler.handleDestroyActivity(iBinder, false, 0, false, stringBuilder.toString());
                    break;
                case 7:
                    this.mTransactionHandler.performRestartActivity(activityClientRecord.token, false);
                    break;
                default:
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Unexpected lifecycle state: ");
                    stringBuilder2.append(state);
                    throw new IllegalArgumentException(stringBuilder2.toString());
            }
        }
    }
}
