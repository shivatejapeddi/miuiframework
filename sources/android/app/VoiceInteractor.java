package android.app;

import android.content.Context;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.DebugUtils;
import android.util.Log;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.app.IVoiceInteractorCallback;
import com.android.internal.app.IVoiceInteractorCallback.Stub;
import com.android.internal.app.IVoiceInteractorRequest;
import com.android.internal.os.HandlerCaller;
import com.android.internal.os.HandlerCaller.Callback;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.Preconditions;
import com.android.internal.util.function.pooled.PooledLambda;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import miui.maml.ActionCommand;
import miui.provider.MiProfile;

public final class VoiceInteractor {
    static final boolean DEBUG = false;
    public static final String KEY_CANCELLATION_SIGNAL = "key_cancellation_signal";
    public static final String KEY_KILL_SIGNAL = "key_kill_signal";
    static final int MSG_ABORT_VOICE_RESULT = 4;
    static final int MSG_CANCEL_RESULT = 6;
    static final int MSG_COMMAND_RESULT = 5;
    static final int MSG_COMPLETE_VOICE_RESULT = 3;
    static final int MSG_CONFIRMATION_RESULT = 1;
    static final int MSG_PICK_OPTION_RESULT = 2;
    static final Request[] NO_REQUESTS = new Request[0];
    static final String TAG = "VoiceInteractor";
    final ArrayMap<IBinder, Request> mActiveRequests = new ArrayMap();
    Activity mActivity;
    final Stub mCallback = new Stub() {
        public void deliverConfirmationResult(IVoiceInteractorRequest request, boolean finished, Bundle result) {
            VoiceInteractor.this.mHandlerCaller.sendMessage(VoiceInteractor.this.mHandlerCaller.obtainMessageIOO(1, finished, request, result));
        }

        public void deliverPickOptionResult(IVoiceInteractorRequest request, boolean finished, Option[] options, Bundle result) {
            VoiceInteractor.this.mHandlerCaller.sendMessage(VoiceInteractor.this.mHandlerCaller.obtainMessageIOOO(2, finished, request, options, result));
        }

        public void deliverCompleteVoiceResult(IVoiceInteractorRequest request, Bundle result) {
            VoiceInteractor.this.mHandlerCaller.sendMessage(VoiceInteractor.this.mHandlerCaller.obtainMessageOO(3, request, result));
        }

        public void deliverAbortVoiceResult(IVoiceInteractorRequest request, Bundle result) {
            VoiceInteractor.this.mHandlerCaller.sendMessage(VoiceInteractor.this.mHandlerCaller.obtainMessageOO(4, request, result));
        }

        public void deliverCommandResult(IVoiceInteractorRequest request, boolean complete, Bundle result) {
            VoiceInteractor.this.mHandlerCaller.sendMessage(VoiceInteractor.this.mHandlerCaller.obtainMessageIOO(5, complete, request, result));
        }

        public void deliverCancel(IVoiceInteractorRequest request) {
            VoiceInteractor.this.mHandlerCaller.sendMessage(VoiceInteractor.this.mHandlerCaller.obtainMessageOO(6, request, null));
        }

        public void destroy() {
            VoiceInteractor.this.mHandlerCaller.getHandler().sendMessage(PooledLambda.obtainMessage(-$$Lambda$dUWXWbBHcaaVBn031EDBP91NR7k.INSTANCE, VoiceInteractor.this));
        }
    };
    Context mContext;
    final HandlerCaller mHandlerCaller;
    final Callback mHandlerCallerCallback = new Callback() {
        public void executeMessage(Message msg) {
            SomeArgs args = msg.obj;
            boolean z = false;
            Request request;
            boolean complete;
            switch (msg.what) {
                case 1:
                    request = VoiceInteractor.this.pullRequest((IVoiceInteractorRequest) args.arg1, true);
                    if (request != null) {
                        ConfirmationRequest confirmationRequest = (ConfirmationRequest) request;
                        if (msg.arg1 != 0) {
                            z = true;
                        }
                        confirmationRequest.onConfirmationResult(z, (Bundle) args.arg2);
                        request.clear();
                        return;
                    }
                    return;
                case 2:
                    if (msg.arg1 != 0) {
                        z = true;
                    }
                    complete = z;
                    Request request2 = VoiceInteractor.this.pullRequest((IVoiceInteractorRequest) args.arg1, complete);
                    if (request2 != null) {
                        ((PickOptionRequest) request2).onPickOptionResult(complete, (Option[]) args.arg2, (Bundle) args.arg3);
                        if (complete) {
                            request2.clear();
                            return;
                        }
                        return;
                    }
                    return;
                case 3:
                    request = VoiceInteractor.this.pullRequest((IVoiceInteractorRequest) args.arg1, true);
                    if (request != null) {
                        ((CompleteVoiceRequest) request).onCompleteResult((Bundle) args.arg2);
                        request.clear();
                        return;
                    }
                    return;
                case 4:
                    request = VoiceInteractor.this.pullRequest((IVoiceInteractorRequest) args.arg1, true);
                    if (request != null) {
                        ((AbortVoiceRequest) request).onAbortResult((Bundle) args.arg2);
                        request.clear();
                        return;
                    }
                    return;
                case 5:
                    complete = msg.arg1 != 0;
                    Request request3 = VoiceInteractor.this.pullRequest((IVoiceInteractorRequest) args.arg1, complete);
                    if (request3 != null) {
                        CommandRequest commandRequest = (CommandRequest) request3;
                        if (msg.arg1 != 0) {
                            z = true;
                        }
                        commandRequest.onCommandResult(z, (Bundle) args.arg2);
                        if (complete) {
                            request3.clear();
                            return;
                        }
                        return;
                    }
                    return;
                case 6:
                    request = VoiceInteractor.this.pullRequest((IVoiceInteractorRequest) args.arg1, true);
                    if (request != null) {
                        request.onCancel();
                        request.clear();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    IVoiceInteractor mInteractor;
    final ArrayMap<Runnable, Executor> mOnDestroyCallbacks = new ArrayMap();
    boolean mRetaining;

    public static abstract class Request {
        Activity mActivity;
        Context mContext;
        String mName;
        IVoiceInteractorRequest mRequestInterface;

        public abstract IVoiceInteractorRequest submit(IVoiceInteractor iVoiceInteractor, String str, IVoiceInteractorCallback iVoiceInteractorCallback) throws RemoteException;

        Request() {
        }

        public String getName() {
            return this.mName;
        }

        public void cancel() {
            IVoiceInteractorRequest iVoiceInteractorRequest = this.mRequestInterface;
            if (iVoiceInteractorRequest != null) {
                try {
                    iVoiceInteractorRequest.cancel();
                    return;
                } catch (RemoteException e) {
                    Log.w(VoiceInteractor.TAG, "Voice interactor has died", e);
                    return;
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Request ");
            stringBuilder.append(this);
            stringBuilder.append(" is no longer active");
            throw new IllegalStateException(stringBuilder.toString());
        }

        public Context getContext() {
            return this.mContext;
        }

        public Activity getActivity() {
            return this.mActivity;
        }

        public void onCancel() {
        }

        public void onAttached(Activity activity) {
        }

        public void onDetached() {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            DebugUtils.buildShortClassTag(this, sb);
            sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            sb.append(getRequestTypeName());
            sb.append(" name=");
            sb.append(this.mName);
            sb.append('}');
            return sb.toString();
        }

        /* Access modifiers changed, original: 0000 */
        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            writer.print(prefix);
            writer.print("mRequestInterface=");
            writer.println(this.mRequestInterface.asBinder());
            writer.print(prefix);
            writer.print("mActivity=");
            writer.println(this.mActivity);
            writer.print(prefix);
            writer.print("mName=");
            writer.println(this.mName);
        }

        /* Access modifiers changed, original: 0000 */
        public String getRequestTypeName() {
            return MiProfile.MIPROFILE_TYPE_REQUEST;
        }

        /* Access modifiers changed, original: 0000 */
        public void clear() {
            this.mRequestInterface = null;
            this.mContext = null;
            this.mActivity = null;
            this.mName = null;
        }
    }

    public static class AbortVoiceRequest extends Request {
        final Bundle mExtras;
        final Prompt mPrompt;

        public AbortVoiceRequest(Prompt prompt, Bundle extras) {
            this.mPrompt = prompt;
            this.mExtras = extras;
        }

        public AbortVoiceRequest(CharSequence message, Bundle extras) {
            this.mPrompt = message != null ? new Prompt(message) : null;
            this.mExtras = extras;
        }

        public void onAbortResult(Bundle result) {
        }

        /* Access modifiers changed, original: 0000 */
        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            super.dump(prefix, fd, writer, args);
            writer.print(prefix);
            writer.print("mPrompt=");
            writer.println(this.mPrompt);
            if (this.mExtras != null) {
                writer.print(prefix);
                writer.print("mExtras=");
                writer.println(this.mExtras);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public String getRequestTypeName() {
            return "AbortVoice";
        }

        /* Access modifiers changed, original: 0000 */
        public IVoiceInteractorRequest submit(IVoiceInteractor interactor, String packageName, IVoiceInteractorCallback callback) throws RemoteException {
            return interactor.startAbortVoice(packageName, callback, this.mPrompt, this.mExtras);
        }
    }

    public static class CommandRequest extends Request {
        final Bundle mArgs;
        final String mCommand;

        public CommandRequest(String command, Bundle args) {
            this.mCommand = command;
            this.mArgs = args;
        }

        public void onCommandResult(boolean isCompleted, Bundle result) {
        }

        /* Access modifiers changed, original: 0000 */
        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            super.dump(prefix, fd, writer, args);
            writer.print(prefix);
            writer.print("mCommand=");
            writer.println(this.mCommand);
            if (this.mArgs != null) {
                writer.print(prefix);
                writer.print("mArgs=");
                writer.println(this.mArgs);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public String getRequestTypeName() {
            return ActionCommand.TAG_NAME;
        }

        /* Access modifiers changed, original: 0000 */
        public IVoiceInteractorRequest submit(IVoiceInteractor interactor, String packageName, IVoiceInteractorCallback callback) throws RemoteException {
            return interactor.startCommand(packageName, callback, this.mCommand, this.mArgs);
        }
    }

    public static class CompleteVoiceRequest extends Request {
        final Bundle mExtras;
        final Prompt mPrompt;

        public CompleteVoiceRequest(Prompt prompt, Bundle extras) {
            this.mPrompt = prompt;
            this.mExtras = extras;
        }

        public CompleteVoiceRequest(CharSequence message, Bundle extras) {
            this.mPrompt = message != null ? new Prompt(message) : null;
            this.mExtras = extras;
        }

        public void onCompleteResult(Bundle result) {
        }

        /* Access modifiers changed, original: 0000 */
        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            super.dump(prefix, fd, writer, args);
            writer.print(prefix);
            writer.print("mPrompt=");
            writer.println(this.mPrompt);
            if (this.mExtras != null) {
                writer.print(prefix);
                writer.print("mExtras=");
                writer.println(this.mExtras);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public String getRequestTypeName() {
            return "CompleteVoice";
        }

        /* Access modifiers changed, original: 0000 */
        public IVoiceInteractorRequest submit(IVoiceInteractor interactor, String packageName, IVoiceInteractorCallback callback) throws RemoteException {
            return interactor.startCompleteVoice(packageName, callback, this.mPrompt, this.mExtras);
        }
    }

    public static class ConfirmationRequest extends Request {
        final Bundle mExtras;
        final Prompt mPrompt;

        public ConfirmationRequest(Prompt prompt, Bundle extras) {
            this.mPrompt = prompt;
            this.mExtras = extras;
        }

        public ConfirmationRequest(CharSequence prompt, Bundle extras) {
            this.mPrompt = prompt != null ? new Prompt(prompt) : null;
            this.mExtras = extras;
        }

        public void onConfirmationResult(boolean confirmed, Bundle result) {
        }

        /* Access modifiers changed, original: 0000 */
        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            super.dump(prefix, fd, writer, args);
            writer.print(prefix);
            writer.print("mPrompt=");
            writer.println(this.mPrompt);
            if (this.mExtras != null) {
                writer.print(prefix);
                writer.print("mExtras=");
                writer.println(this.mExtras);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public String getRequestTypeName() {
            return "Confirmation";
        }

        /* Access modifiers changed, original: 0000 */
        public IVoiceInteractorRequest submit(IVoiceInteractor interactor, String packageName, IVoiceInteractorCallback callback) throws RemoteException {
            return interactor.startConfirmation(packageName, callback, this.mPrompt, this.mExtras);
        }
    }

    private static final class KillCallback extends ICancellationSignal.Stub {
        private final WeakReference<VoiceInteractor> mInteractor;

        KillCallback(VoiceInteractor interactor) {
            this.mInteractor = new WeakReference(interactor);
        }

        public void cancel() {
            VoiceInteractor voiceInteractor = (VoiceInteractor) this.mInteractor.get();
            if (voiceInteractor != null) {
                voiceInteractor.mHandlerCaller.getHandler().sendMessage(PooledLambda.obtainMessage(-$$Lambda$dUWXWbBHcaaVBn031EDBP91NR7k.INSTANCE, voiceInteractor));
            }
        }
    }

    public static class PickOptionRequest extends Request {
        final Bundle mExtras;
        final Option[] mOptions;
        final Prompt mPrompt;

        public static final class Option implements Parcelable {
            public static final Creator<Option> CREATOR = new Creator<Option>() {
                public Option createFromParcel(Parcel in) {
                    return new Option(in);
                }

                public Option[] newArray(int size) {
                    return new Option[size];
                }
            };
            Bundle mExtras;
            final int mIndex;
            final CharSequence mLabel;
            ArrayList<CharSequence> mSynonyms;

            public Option(CharSequence label) {
                this.mLabel = label;
                this.mIndex = -1;
            }

            public Option(CharSequence label, int index) {
                this.mLabel = label;
                this.mIndex = index;
            }

            public Option addSynonym(CharSequence synonym) {
                if (this.mSynonyms == null) {
                    this.mSynonyms = new ArrayList();
                }
                this.mSynonyms.add(synonym);
                return this;
            }

            public CharSequence getLabel() {
                return this.mLabel;
            }

            public int getIndex() {
                return this.mIndex;
            }

            public int countSynonyms() {
                ArrayList arrayList = this.mSynonyms;
                return arrayList != null ? arrayList.size() : 0;
            }

            public CharSequence getSynonymAt(int index) {
                ArrayList arrayList = this.mSynonyms;
                return arrayList != null ? (CharSequence) arrayList.get(index) : null;
            }

            public void setExtras(Bundle extras) {
                this.mExtras = extras;
            }

            public Bundle getExtras() {
                return this.mExtras;
            }

            Option(Parcel in) {
                this.mLabel = in.readCharSequence();
                this.mIndex = in.readInt();
                this.mSynonyms = in.readCharSequenceList();
                this.mExtras = in.readBundle();
            }

            public int describeContents() {
                return 0;
            }

            public void writeToParcel(Parcel dest, int flags) {
                dest.writeCharSequence(this.mLabel);
                dest.writeInt(this.mIndex);
                dest.writeCharSequenceList(this.mSynonyms);
                dest.writeBundle(this.mExtras);
            }
        }

        public PickOptionRequest(Prompt prompt, Option[] options, Bundle extras) {
            this.mPrompt = prompt;
            this.mOptions = options;
            this.mExtras = extras;
        }

        public PickOptionRequest(CharSequence prompt, Option[] options, Bundle extras) {
            this.mPrompt = prompt != null ? new Prompt(prompt) : null;
            this.mOptions = options;
            this.mExtras = extras;
        }

        public void onPickOptionResult(boolean finished, Option[] selections, Bundle result) {
        }

        /* Access modifiers changed, original: 0000 */
        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            super.dump(prefix, fd, writer, args);
            writer.print(prefix);
            writer.print("mPrompt=");
            writer.println(this.mPrompt);
            if (this.mOptions != null) {
                writer.print(prefix);
                writer.println("Options:");
                int i = 0;
                while (true) {
                    Option op = this.mOptions;
                    if (i >= op.length) {
                        break;
                    }
                    op = op[i];
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.println(":");
                    writer.print(prefix);
                    writer.print("    mLabel=");
                    writer.println(op.mLabel);
                    writer.print(prefix);
                    writer.print("    mIndex=");
                    writer.println(op.mIndex);
                    if (op.mSynonyms != null && op.mSynonyms.size() > 0) {
                        writer.print(prefix);
                        writer.println("    Synonyms:");
                        for (int j = 0; j < op.mSynonyms.size(); j++) {
                            writer.print(prefix);
                            writer.print("      #");
                            writer.print(j);
                            writer.print(": ");
                            writer.println(op.mSynonyms.get(j));
                        }
                    }
                    if (op.mExtras != null) {
                        writer.print(prefix);
                        writer.print("    mExtras=");
                        writer.println(op.mExtras);
                    }
                    i++;
                }
            }
            if (this.mExtras != null) {
                writer.print(prefix);
                writer.print("mExtras=");
                writer.println(this.mExtras);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public String getRequestTypeName() {
            return "PickOption";
        }

        /* Access modifiers changed, original: 0000 */
        public IVoiceInteractorRequest submit(IVoiceInteractor interactor, String packageName, IVoiceInteractorCallback callback) throws RemoteException {
            return interactor.startPickOption(packageName, callback, this.mPrompt, this.mOptions, this.mExtras);
        }
    }

    public static class Prompt implements Parcelable {
        public static final Creator<Prompt> CREATOR = new Creator<Prompt>() {
            public Prompt createFromParcel(Parcel in) {
                return new Prompt(in);
            }

            public Prompt[] newArray(int size) {
                return new Prompt[size];
            }
        };
        private final CharSequence mVisualPrompt;
        private final CharSequence[] mVoicePrompts;

        public Prompt(CharSequence[] voicePrompts, CharSequence visualPrompt) {
            if (voicePrompts == null) {
                throw new NullPointerException("voicePrompts must not be null");
            } else if (voicePrompts.length == 0) {
                throw new IllegalArgumentException("voicePrompts must not be empty");
            } else if (visualPrompt != null) {
                this.mVoicePrompts = voicePrompts;
                this.mVisualPrompt = visualPrompt;
            } else {
                throw new NullPointerException("visualPrompt must not be null");
            }
        }

        public Prompt(CharSequence prompt) {
            this.mVoicePrompts = new CharSequence[]{prompt};
            this.mVisualPrompt = prompt;
        }

        public CharSequence getVoicePromptAt(int index) {
            return this.mVoicePrompts[index];
        }

        public int countVoicePrompts() {
            return this.mVoicePrompts.length;
        }

        public CharSequence getVisualPrompt() {
            return this.mVisualPrompt;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            DebugUtils.buildShortClassTag(this, sb);
            CharSequence charSequence = this.mVisualPrompt;
            if (charSequence != null) {
                CharSequence[] charSequenceArr = this.mVoicePrompts;
                if (charSequenceArr != null && charSequenceArr.length == 1 && charSequence.equals(charSequenceArr[0])) {
                    sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    sb.append(this.mVisualPrompt);
                    sb.append('}');
                    return sb.toString();
                }
            }
            if (this.mVisualPrompt != null) {
                sb.append(" visual=");
                sb.append(this.mVisualPrompt);
            }
            if (this.mVoicePrompts != null) {
                sb.append(", voice=");
                for (int i = 0; i < this.mVoicePrompts.length; i++) {
                    if (i > 0) {
                        sb.append(" | ");
                    }
                    sb.append(this.mVoicePrompts[i]);
                }
            }
            sb.append('}');
            return sb.toString();
        }

        Prompt(Parcel in) {
            this.mVoicePrompts = in.readCharSequenceArray();
            this.mVisualPrompt = in.readCharSequence();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeCharSequenceArray(this.mVoicePrompts);
            dest.writeCharSequence(this.mVisualPrompt);
        }
    }

    VoiceInteractor(IVoiceInteractor interactor, Context context, Activity activity, Looper looper) {
        this.mInteractor = interactor;
        this.mContext = context;
        this.mActivity = activity;
        this.mHandlerCaller = new HandlerCaller(context, looper, this.mHandlerCallerCallback, true);
        try {
            this.mInteractor.setKillCallback(new KillCallback(this));
        } catch (RemoteException e) {
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Request pullRequest(IVoiceInteractorRequest request, boolean complete) {
        Request req;
        synchronized (this.mActiveRequests) {
            req = (Request) this.mActiveRequests.get(request.asBinder());
            if (req != null && complete) {
                this.mActiveRequests.remove(request.asBinder());
            }
        }
        return req;
    }

    private ArrayList<Request> makeRequestList() {
        int N = this.mActiveRequests.size();
        if (N < 1) {
            return null;
        }
        ArrayList<Request> list = new ArrayList(N);
        for (int i = 0; i < N; i++) {
            list.add((Request) this.mActiveRequests.valueAt(i));
        }
        return list;
    }

    /* Access modifiers changed, original: 0000 */
    public void attachActivity(Activity activity) {
        this.mRetaining = false;
        if (this.mActivity != activity) {
            this.mContext = activity;
            this.mActivity = activity;
            ArrayList<Request> reqs = makeRequestList();
            if (reqs != null) {
                for (int i = 0; i < reqs.size(); i++) {
                    Request req = (Request) reqs.get(i);
                    req.mContext = activity;
                    req.mActivity = activity;
                    req.onAttached(activity);
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void retainInstance() {
        this.mRetaining = true;
    }

    /* Access modifiers changed, original: 0000 */
    public void detachActivity() {
        int i;
        ArrayList<Request> reqs = makeRequestList();
        if (reqs != null) {
            for (i = 0; i < reqs.size(); i++) {
                Request req = (Request) reqs.get(i);
                req.onDetached();
                req.mActivity = null;
                req.mContext = null;
            }
        }
        if (!this.mRetaining) {
            reqs = makeRequestList();
            if (reqs != null) {
                for (i = 0; i < reqs.size(); i++) {
                    ((Request) reqs.get(i)).cancel();
                }
            }
            this.mActiveRequests.clear();
        }
        this.mContext = null;
        this.mActivity = null;
    }

    /* Access modifiers changed, original: 0000 */
    public void destroy() {
        for (int i = this.mActiveRequests.size() - 1; i >= 0; i--) {
            Request request = (Request) this.mActiveRequests.valueAt(i);
            this.mActiveRequests.removeAt(i);
            request.cancel();
        }
        for (int i2 = this.mOnDestroyCallbacks.size() - 1; i2 >= 0; i2--) {
            ((Executor) this.mOnDestroyCallbacks.valueAt(i2)).execute((Runnable) this.mOnDestroyCallbacks.keyAt(i2));
            this.mOnDestroyCallbacks.removeAt(i2);
        }
        this.mInteractor = null;
        Activity activity = this.mActivity;
        if (activity != null) {
            activity.setVoiceInteractor(null);
        }
    }

    public boolean submitRequest(Request request) {
        return submitRequest(request, null);
    }

    public boolean submitRequest(Request request, String name) {
        if (isDestroyed()) {
            Log.w(TAG, "Cannot interact with a destroyed voice interactor");
            return false;
        }
        try {
            if (request.mRequestInterface == null) {
                IVoiceInteractorRequest ireq = request.submit(this.mInteractor, this.mContext.getOpPackageName(), this.mCallback);
                request.mRequestInterface = ireq;
                request.mContext = this.mContext;
                request.mActivity = this.mActivity;
                request.mName = name;
                synchronized (this.mActiveRequests) {
                    this.mActiveRequests.put(ireq.asBinder(), request);
                }
                return true;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Given ");
            stringBuilder.append(request);
            stringBuilder.append(" is already active");
            throw new IllegalStateException(stringBuilder.toString());
        } catch (RemoteException e) {
            Log.w(TAG, "Remove voice interactor service died", e);
            return false;
        }
    }

    public Request[] getActiveRequests() {
        if (isDestroyed()) {
            Log.w(TAG, "Cannot interact with a destroyed voice interactor");
            return null;
        }
        synchronized (this.mActiveRequests) {
            int N = this.mActiveRequests.size();
            Request[] requestArr;
            if (N <= 0) {
                requestArr = NO_REQUESTS;
                return requestArr;
            }
            requestArr = new Request[N];
            for (int i = 0; i < N; i++) {
                requestArr[i] = (Request) this.mActiveRequests.valueAt(i);
            }
            return requestArr;
        }
    }

    /* JADX WARNING: Missing block: B:17:0x003a, code skipped:
            return r4;
     */
    public android.app.VoiceInteractor.Request getActiveRequest(java.lang.String r7) {
        /*
        r6 = this;
        r0 = r6.isDestroyed();
        r1 = 0;
        if (r0 == 0) goto L_0x000f;
    L_0x0007:
        r0 = "VoiceInteractor";
        r2 = "Cannot interact with a destroyed voice interactor";
        android.util.Log.w(r0, r2);
        return r1;
    L_0x000f:
        r0 = r6.mActiveRequests;
        monitor-enter(r0);
        r2 = r6.mActiveRequests;	 Catch:{ all -> 0x003d }
        r2 = r2.size();	 Catch:{ all -> 0x003d }
        r3 = 0;
    L_0x0019:
        if (r3 >= r2) goto L_0x003b;
    L_0x001b:
        r4 = r6.mActiveRequests;	 Catch:{ all -> 0x003d }
        r4 = r4.valueAt(r3);	 Catch:{ all -> 0x003d }
        r4 = (android.app.VoiceInteractor.Request) r4;	 Catch:{ all -> 0x003d }
        r5 = r4.getName();	 Catch:{ all -> 0x003d }
        if (r7 == r5) goto L_0x0039;
    L_0x0029:
        if (r7 == 0) goto L_0x0036;
    L_0x002b:
        r5 = r4.getName();	 Catch:{ all -> 0x003d }
        r5 = r7.equals(r5);	 Catch:{ all -> 0x003d }
        if (r5 == 0) goto L_0x0036;
    L_0x0035:
        goto L_0x0039;
    L_0x0036:
        r3 = r3 + 1;
        goto L_0x0019;
    L_0x0039:
        monitor-exit(r0);	 Catch:{ all -> 0x003d }
        return r4;
    L_0x003b:
        monitor-exit(r0);	 Catch:{ all -> 0x003d }
        return r1;
    L_0x003d:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x003d }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.VoiceInteractor.getActiveRequest(java.lang.String):android.app.VoiceInteractor$Request");
    }

    public boolean[] supportsCommands(String[] commands) {
        if (isDestroyed()) {
            Log.w(TAG, "Cannot interact with a destroyed voice interactor");
            return new boolean[commands.length];
        }
        try {
            return this.mInteractor.supportsCommands(this.mContext.getOpPackageName(), commands);
        } catch (RemoteException e) {
            throw new RuntimeException("Voice interactor has died", e);
        }
    }

    public boolean isDestroyed() {
        return this.mInteractor == null;
    }

    public boolean registerOnDestroyedCallback(Executor executor, Runnable callback) {
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(callback);
        if (isDestroyed()) {
            Log.w(TAG, "Cannot interact with a destroyed voice interactor");
            return false;
        }
        this.mOnDestroyCallbacks.put(callback, executor);
        return true;
    }

    public boolean unregisterOnDestroyedCallback(Runnable callback) {
        Preconditions.checkNotNull(callback);
        boolean z = false;
        if (isDestroyed()) {
            Log.w(TAG, "Cannot interact with a destroyed voice interactor");
            return false;
        }
        if (this.mOnDestroyCallbacks.remove(callback) != null) {
            z = true;
        }
        return z;
    }

    public void notifyDirectActionsChanged() {
        boolean isDestroyed = isDestroyed();
        String str = TAG;
        if (isDestroyed) {
            Log.w(str, "Cannot interact with a destroyed voice interactor");
            return;
        }
        try {
            this.mInteractor.notifyDirectActionsChanged(this.mActivity.getTaskId(), this.mActivity.getAssistToken());
        } catch (RemoteException e) {
            Log.w(str, "Voice interactor has died", e);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        String innerPrefix = new StringBuilder();
        innerPrefix.append(prefix);
        innerPrefix.append("    ");
        innerPrefix = innerPrefix.toString();
        if (this.mActiveRequests.size() > 0) {
            writer.print(prefix);
            writer.println("Active voice requests:");
            for (int i = 0; i < this.mActiveRequests.size(); i++) {
                Request req = (Request) this.mActiveRequests.valueAt(i);
                writer.print(prefix);
                writer.print("  #");
                writer.print(i);
                writer.print(": ");
                writer.println(req);
                req.dump(innerPrefix, fd, writer, args);
            }
        }
        writer.print(prefix);
        writer.println("VoiceInteractor misc state:");
        writer.print(prefix);
        writer.print("  mInteractor=");
        writer.println(this.mInteractor.asBinder());
        writer.print(prefix);
        writer.print("  mActivity=");
        writer.println(this.mActivity);
    }
}
