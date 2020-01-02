package android.filterfw.core;

import android.filterfw.core.GraphRunner.OnRunnerDoneListener;
import android.os.ConditionVariable;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SyncRunner extends GraphRunner {
    private static final String TAG = "SyncRunner";
    private OnRunnerDoneListener mDoneListener = null;
    private final boolean mLogVerbose;
    private Scheduler mScheduler = null;
    private StopWatchMap mTimer = null;
    private ConditionVariable mWakeCondition = new ConditionVariable();
    private ScheduledThreadPoolExecutor mWakeExecutor = new ScheduledThreadPoolExecutor(1);

    public SyncRunner(FilterContext context, FilterGraph graph, Class schedulerClass) {
        super(context);
        String str = TAG;
        this.mLogVerbose = Log.isLoggable(str, 2);
        if (this.mLogVerbose) {
            Log.v(str, "Initializing SyncRunner");
        }
        if (Scheduler.class.isAssignableFrom(schedulerClass)) {
            try {
                this.mScheduler = (Scheduler) schedulerClass.getConstructor(new Class[]{FilterGraph.class}).newInstance(new Object[]{graph});
                this.mFilterContext = context;
                this.mFilterContext.addGraph(graph);
                this.mTimer = new StopWatchMap();
                if (this.mLogVerbose) {
                    Log.v(str, "Setting up filters");
                }
                graph.setupFilters();
                return;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Scheduler does not have constructor <init>(FilterGraph)!", e);
            } catch (InstantiationException e2) {
                throw new RuntimeException("Could not instantiate the Scheduler instance!", e2);
            } catch (IllegalAccessException e3) {
                throw new RuntimeException("Cannot access Scheduler constructor!", e3);
            } catch (InvocationTargetException e4) {
                throw new RuntimeException("Scheduler constructor threw an exception", e4);
            } catch (Exception e5) {
                throw new RuntimeException("Could not instantiate Scheduler", e5);
            }
        }
        throw new IllegalArgumentException("Class provided is not a Scheduler subclass!");
    }

    public FilterGraph getGraph() {
        Scheduler scheduler = this.mScheduler;
        return scheduler != null ? scheduler.getGraph() : null;
    }

    public int step() {
        assertReadyToStep();
        if (getGraph().isReady()) {
            return performStep() ? 1 : determinePostRunState();
        } else {
            throw new RuntimeException("Trying to process graph that is not open!");
        }
    }

    public void beginProcessing() {
        this.mScheduler.reset();
        getGraph().beginProcessing();
    }

    public void close() {
        if (this.mLogVerbose) {
            Log.v(TAG, "Closing graph.");
        }
        getGraph().closeFilters(this.mFilterContext);
        this.mScheduler.reset();
    }

    public void run() {
        boolean z = this.mLogVerbose;
        String str = TAG;
        if (z) {
            Log.v(str, "Beginning run.");
        }
        assertReadyToStep();
        beginProcessing();
        z = activateGlContext();
        boolean keepRunning = true;
        while (keepRunning) {
            keepRunning = performStep();
        }
        if (z) {
            deactivateGlContext();
        }
        if (this.mDoneListener != null) {
            if (this.mLogVerbose) {
                Log.v(str, "Calling completion listener.");
            }
            this.mDoneListener.onRunnerDone(determinePostRunState());
        }
        if (this.mLogVerbose) {
            Log.v(str, "Run complete");
        }
    }

    public boolean isRunning() {
        return false;
    }

    public void setDoneCallback(OnRunnerDoneListener listener) {
        this.mDoneListener = listener;
    }

    public void stop() {
        throw new RuntimeException("SyncRunner does not support stopping a graph!");
    }

    public synchronized Exception getError() {
        return null;
    }

    /* Access modifiers changed, original: protected */
    public void waitUntilWake() {
        this.mWakeCondition.block();
    }

    /* Access modifiers changed, original: protected */
    public void processFilterNode(Filter filter) {
        boolean z = this.mLogVerbose;
        String str = TAG;
        if (z) {
            Log.v(str, "Processing filter node");
        }
        filter.performProcess(this.mFilterContext);
        if (filter.getStatus() == 6) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("There was an error executing ");
            stringBuilder.append(filter);
            stringBuilder.append("!");
            throw new RuntimeException(stringBuilder.toString());
        } else if (filter.getStatus() == 4) {
            if (this.mLogVerbose) {
                Log.v(str, "Scheduling filter wakeup");
            }
            scheduleFilterWake(filter, filter.getSleepDelay());
        }
    }

    /* Access modifiers changed, original: protected */
    public void scheduleFilterWake(Filter filter, int delay) {
        this.mWakeCondition.close();
        final Filter filterToSchedule = filter;
        final ConditionVariable conditionToWake = this.mWakeCondition;
        this.mWakeExecutor.schedule(new Runnable() {
            public void run() {
                filterToSchedule.unsetStatus(4);
                conditionToWake.open();
            }
        }, (long) delay, TimeUnit.MILLISECONDS);
    }

    /* Access modifiers changed, original: protected */
    public int determinePostRunState() {
        for (Filter filter : this.mScheduler.getGraph().getFilters()) {
            if (filter.isOpen()) {
                if (filter.getStatus() == 4) {
                    return 3;
                }
                return 4;
            }
        }
        return 2;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean performStep() {
        if (this.mLogVerbose) {
            Log.v(TAG, "Performing one step.");
        }
        Filter filter = this.mScheduler.scheduleNextNode();
        if (filter == null) {
            return false;
        }
        this.mTimer.start(filter.getName());
        processFilterNode(filter);
        this.mTimer.stop(filter.getName());
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public void assertReadyToStep() {
        if (this.mScheduler == null) {
            throw new RuntimeException("Attempting to run schedule with no scheduler in place!");
        } else if (getGraph() == null) {
            throw new RuntimeException("Calling step on scheduler with no graph in place!");
        }
    }
}
