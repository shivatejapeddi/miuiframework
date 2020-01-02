package android.app.job;

import android.app.Service;
import android.app.job.IJobService.Stub;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import java.lang.ref.WeakReference;

public abstract class JobServiceEngine {
    private static final int MSG_EXECUTE_JOB = 0;
    private static final int MSG_JOB_FINISHED = 2;
    private static final int MSG_STOP_JOB = 1;
    private static final String TAG = "JobServiceEngine";
    private final IJobService mBinder = new JobInterface(this);
    JobHandler mHandler;

    class JobHandler extends Handler {
        JobHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            JobParameters params = msg.obj;
            int i = msg.what;
            String str = JobServiceEngine.TAG;
            if (i != 0) {
                boolean z = true;
                if (i == 1) {
                    try {
                        ackStopMessage(params, JobServiceEngine.this.onStopJob(params));
                        return;
                    } catch (Exception e) {
                        Log.e(str, "Application unable to handle onStopJob.", e);
                        throw new RuntimeException(e);
                    }
                } else if (i != 2) {
                    Log.e(str, "Unrecognised message received.");
                    return;
                } else {
                    if (msg.arg2 != 1) {
                        z = false;
                    }
                    boolean needsReschedule = z;
                    IJobCallback callback = params.getCallback();
                    if (callback != null) {
                        try {
                            callback.jobFinished(params.getJobId(), needsReschedule);
                            return;
                        } catch (RemoteException e2) {
                            Log.e(str, "Error reporting job finish to system: binder has goneaway.");
                            return;
                        }
                    }
                    Log.e(str, "finishJob() called for a nonexistent job id.");
                    return;
                }
            }
            try {
                ackStartMessage(params, JobServiceEngine.this.onStartJob(params));
            } catch (Exception e3) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error while executing job: ");
                stringBuilder.append(params.getJobId());
                Log.e(str, stringBuilder.toString());
                throw new RuntimeException(e3);
            }
        }

        private void ackStartMessage(JobParameters params, boolean workOngoing) {
            IJobCallback callback = params.getCallback();
            int jobId = params.getJobId();
            String str = JobServiceEngine.TAG;
            if (callback != null) {
                try {
                    callback.acknowledgeStartMessage(jobId, workOngoing);
                } catch (RemoteException e) {
                    Log.e(str, "System unreachable for starting job.");
                }
            } else if (Log.isLoggable(str, 3)) {
                Log.d(str, "Attempting to ack a job that has already been processed.");
            }
        }

        private void ackStopMessage(JobParameters params, boolean reschedule) {
            IJobCallback callback = params.getCallback();
            int jobId = params.getJobId();
            String str = JobServiceEngine.TAG;
            if (callback != null) {
                try {
                    callback.acknowledgeStopMessage(jobId, reschedule);
                } catch (RemoteException e) {
                    Log.e(str, "System unreachable for stopping job.");
                }
            } else if (Log.isLoggable(str, 3)) {
                Log.d(str, "Attempting to ack a job that has already been processed.");
            }
        }
    }

    static final class JobInterface extends Stub {
        final WeakReference<JobServiceEngine> mService;

        JobInterface(JobServiceEngine service) {
            this.mService = new WeakReference(service);
        }

        public void startJob(JobParameters jobParams) throws RemoteException {
            JobServiceEngine service = (JobServiceEngine) this.mService.get();
            if (service != null) {
                Message.obtain(service.mHandler, 0, jobParams).sendToTarget();
            }
        }

        public void stopJob(JobParameters jobParams) throws RemoteException {
            JobServiceEngine service = (JobServiceEngine) this.mService.get();
            if (service != null) {
                Message.obtain(service.mHandler, 1, jobParams).sendToTarget();
            }
        }
    }

    public abstract boolean onStartJob(JobParameters jobParameters);

    public abstract boolean onStopJob(JobParameters jobParameters);

    public JobServiceEngine(Service service) {
        this.mHandler = new JobHandler(service.getMainLooper());
    }

    public final IBinder getBinder() {
        return this.mBinder.asBinder();
    }

    public void jobFinished(JobParameters params, boolean needsReschedule) {
        if (params != null) {
            Message m = Message.obtain(this.mHandler, 2, params);
            m.arg2 = needsReschedule;
            m.sendToTarget();
            return;
        }
        throw new NullPointerException("params");
    }
}
