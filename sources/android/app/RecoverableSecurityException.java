package android.app;

import android.app.AlertDialog.Builder;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.R;
import java.util.Objects;

public final class RecoverableSecurityException extends SecurityException implements Parcelable {
    public static final Creator<RecoverableSecurityException> CREATOR = new Creator<RecoverableSecurityException>() {
        public RecoverableSecurityException createFromParcel(Parcel source) {
            return new RecoverableSecurityException(source);
        }

        public RecoverableSecurityException[] newArray(int size) {
            return new RecoverableSecurityException[size];
        }
    };
    private static final String TAG = "RecoverableSecurityException";
    private final RemoteAction mUserAction;
    private final CharSequence mUserMessage;

    public static class LocalDialog extends DialogFragment {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            RecoverableSecurityException e = (RecoverableSecurityException) getArguments().getParcelable(RecoverableSecurityException.TAG);
            return new Builder(getActivity()).setMessage(e.mUserMessage).setPositiveButton(e.mUserAction.getTitle(), new -$$Lambda$RecoverableSecurityException$LocalDialog$r8YNkpjWIZllJsQ_8eA0q51FU5Q(e)).setNegativeButton(17039360, null).create();
        }

        static /* synthetic */ void lambda$onCreateDialog$0(RecoverableSecurityException e, DialogInterface dialog, int which) {
            try {
                e.mUserAction.getActionIntent().send();
            } catch (CanceledException e2) {
            }
        }
    }

    public RecoverableSecurityException(Parcel in) {
        this(new SecurityException(in.readString()), in.readCharSequence(), (RemoteAction) RemoteAction.CREATOR.createFromParcel(in));
    }

    public RecoverableSecurityException(Throwable cause, CharSequence userMessage, RemoteAction userAction) {
        super(cause.getMessage());
        this.mUserMessage = (CharSequence) Objects.requireNonNull(userMessage);
        this.mUserAction = (RemoteAction) Objects.requireNonNull(userAction);
    }

    public CharSequence getUserMessage() {
        return this.mUserMessage;
    }

    public RemoteAction getUserAction() {
        return this.mUserAction;
    }

    public void showAsNotification(Context context, String channelId) {
        NotificationManager nm = (NotificationManager) context.getSystemService(NotificationManager.class);
        Notification.Builder builder = new Notification.Builder(context, channelId).setSmallIcon((int) R.drawable.ic_print_error).setContentTitle(this.mUserAction.getTitle()).setContentText(this.mUserMessage).setContentIntent(this.mUserAction.getActionIntent()).setCategory(Notification.CATEGORY_ERROR);
        nm.notify(TAG, this.mUserAction.getActionIntent().getCreatorUid(), builder.build());
    }

    public void showAsDialog(Activity activity) {
        Fragment dialog = new LocalDialog();
        Bundle args = new Bundle();
        args.putParcelable(TAG, this);
        dialog.setArguments(args);
        String tag = new StringBuilder();
        tag.append("RecoverableSecurityException_");
        tag.append(this.mUserAction.getActionIntent().getCreatorUid());
        tag = tag.toString();
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment old = fm.findFragmentByTag(tag);
        if (old != null) {
            ft.remove(old);
        }
        ft.add(dialog, tag);
        ft.commitAllowingStateLoss();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getMessage());
        dest.writeCharSequence(this.mUserMessage);
        this.mUserAction.writeToParcel(dest, flags);
    }
}
