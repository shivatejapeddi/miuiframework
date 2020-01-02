package android.inputmethodservice;

import android.content.Context;
import android.inputmethodservice.RecodingStateAnimatorView.State;
import android.miui.R;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class VoiceAreaLayoutHelper {
    private static boolean DEBUG = true;
    private static String TAG = "VoiceAreaLayoutHelper";
    private final String VOICE_TO_TEXT_HINT;
    SpeechRecognizationCallback mCallback = new SpeechRecognizationCallback() {
        public void updateAnimation(State type) {
            if (VoiceAreaLayoutHelper.this.mRecodingStateAnimatorView != null) {
                VoiceAreaLayoutHelper.this.mRecodingStateAnimatorView.setPreState(type);
            }
        }

        public void updateText(String text) {
            updateText(text, true);
        }

        public void updateText(String text, boolean needFormat) {
            if (VoiceAreaLayoutHelper.this.mDynamicVoiceInput != null && text != null) {
                VoiceAreaLayoutHelper.this.mDynamicVoiceInput.setText(needFormat ? VoiceInputHelper.formatText(text) : text);
            }
        }
    };
    private ViewGroup mCandidatesViewGroup;
    private Context mContext;
    private TextView mDynamicVoiceInput;
    private View mRealCandidatesView;
    private RecodingStateAnimatorView mRecodingStateAnimatorView;
    private LinearLayout mVoiceInputButton;
    private LinearLayout mVoiceToTextLayout;
    private PopupWindow mVoiceToTextWindow;

    public VoiceAreaLayoutHelper(Context context) {
        this.mContext = context;
        this.VOICE_TO_TEXT_HINT = this.mContext.getString(R.string.voice_begin);
    }

    public LinearLayout getVoiceInputButton() {
        this.mVoiceInputButton = (LinearLayout) LayoutInflater.from(this.mContext).inflate((int) R.layout.voice_input_button, null);
        ViewGroup viewGroup = this.mCandidatesViewGroup;
        if (viewGroup != null) {
            if (this.mRealCandidatesView == null) {
                this.mRealCandidatesView = viewGroup.getChildAt(0);
            }
            this.mCandidatesViewGroup.removeAllViews();
            if (this.mRealCandidatesView != null) {
                log("add real candidatesView");
                this.mVoiceInputButton.addView(this.mRealCandidatesView, (LayoutParams) new FrameLayout.LayoutParams(-1, -2, 80));
            }
        }
        final TextView voiceButtonHint = (TextView) this.mVoiceInputButton.findViewById(R.id.hint);
        ((LinearLayout) this.mVoiceInputButton.findViewById(R.id.voice_btn)).setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == 0) {
                    VoiceAreaLayoutHelper.log("start voice recognizer...ACTION_DOWN");
                    VoiceInputHelper.setIsInSpeech(false);
                    voiceButtonHint.setText(VoiceAreaLayoutHelper.this.mContext.getResources().getText(R.string.voice_button_action_down));
                    VoiceAreaLayoutHelper.this.getVoiceToTextWindow().showAtLocation(VoiceAreaLayoutHelper.this.mVoiceInputButton, 0, 0, 0);
                    VoiceAreaLayoutHelper.this.mVoiceToTextLayout.startAnimation(AnimationUtils.loadAnimation(VoiceAreaLayoutHelper.this.mContext, R.anim.voice_input_dialog_fade_in));
                    if (((ConnectivityManager) VoiceAreaLayoutHelper.this.mContext.getSystemService("connectivity")).getActiveNetworkInfo() != null) {
                        VoiceInputHelper.startListening(VoiceAreaLayoutHelper.this.mContext);
                    } else if (VoiceAreaLayoutHelper.this.mCallback != null) {
                        VoiceAreaLayoutHelper.this.mCallback.updateText(VoiceAreaLayoutHelper.this.mContext.getResources().getString(R.string.voice_input_error_network), false);
                        VoiceAreaLayoutHelper.log("No connectivity service");
                        VoiceInputReporter.reportEvent(3, VoiceAreaLayoutHelper.this.mContext.getPackageName());
                    }
                    VoiceInputReporter.reportEvent(2, VoiceAreaLayoutHelper.this.mContext.getPackageName());
                } else if (action == 1 || action == 3) {
                    VoiceAreaLayoutHelper.log("stop voice recognizer...ACTION_UP or ACTION_CANCEL");
                    voiceButtonHint.setText(VoiceAreaLayoutHelper.this.mContext.getResources().getText(R.string.voice_button_action_up));
                    VoiceAreaLayoutHelper.this.hidePopupWindow();
                    VoiceInputHelper.stopListening(VoiceAreaLayoutHelper.this.mContext);
                }
                return false;
            }
        });
        log("init voice area layout");
        VoiceInputReporter.reportEvent(1, this.mContext.getPackageName());
        return this.mVoiceInputButton;
    }

    public SpeechRecognizationCallback getSpeechRecognizationCallback() {
        return this.mCallback;
    }

    public void setCandidatesViewGroup(ViewGroup candidatesViewGroup) {
        if (this.mCandidatesViewGroup == null) {
            this.mCandidatesViewGroup = candidatesViewGroup;
        }
    }

    public void removeLayout(FrameLayout candidatesFrame) {
        log("removeLayout called");
        LinearLayout linearLayout = this.mVoiceInputButton;
        if (linearLayout != null) {
            linearLayout.removeAllViews();
        }
        candidatesFrame.removeAllViews();
    }

    public void restoreCandidatesFrame(FrameLayout candidatesFrame) {
        log("restoreCandidatesFrame called");
        hidePopupWindow();
        removeLayout(candidatesFrame);
        if (this.mCandidatesViewGroup != null) {
            View view = this.mRealCandidatesView;
            if (view != null && view.getParent() == null) {
                this.mCandidatesViewGroup.addView(this.mRealCandidatesView, new FrameLayout.LayoutParams(-1, -2, 80));
                candidatesFrame.addView((View) this.mCandidatesViewGroup, (LayoutParams) new FrameLayout.LayoutParams(-1, -2));
                return;
            }
        }
        log("fail to restoreCandidatesFrame");
    }

    private PopupWindow getVoiceToTextWindow() {
        if (this.mVoiceToTextWindow == null) {
            log("new popupWindow");
            initVoiceToTextLayout();
            this.mVoiceToTextWindow = new PopupWindow(this.mVoiceToTextLayout, this.mVoiceInputButton.getMeasuredWidth(), this.mVoiceInputButton.getLocationOnScreen()[1], false);
            this.mVoiceToTextLayout.setBackgroundColor(this.mContext.getResources().getColor(R.color.voice_input_dark_bg));
            this.mVoiceToTextWindow.setOutsideTouchable(false);
            this.mVoiceToTextWindow.setTouchable(true);
            this.mVoiceToTextWindow.setLayoutInScreenEnabled(true);
        }
        SpeechRecognizationCallback speechRecognizationCallback = this.mCallback;
        if (speechRecognizationCallback != null) {
            speechRecognizationCallback.updateText(this.VOICE_TO_TEXT_HINT, false);
            RecodingStateAnimatorView recodingStateAnimatorView = this.mRecodingStateAnimatorView;
            if (recodingStateAnimatorView != null) {
                recodingStateAnimatorView.init();
            }
        }
        return this.mVoiceToTextWindow;
    }

    private void hidePopupWindow() {
        if (this.mVoiceToTextWindow != null) {
            log("hide popupWindow");
            Animation animation = AnimationUtils.loadAnimation(this.mContext, R.anim.voice_input_dialog_fade_out);
            this.mVoiceToTextLayout.startAnimation(animation);
            animation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    VoiceAreaLayoutHelper.this.mVoiceToTextWindow.dismiss();
                }

                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    private void initVoiceToTextLayout() {
        log("initVoiceToTextLayout");
        this.mVoiceToTextLayout = (LinearLayout) LayoutInflater.from(this.mContext).inflate((int) R.layout.voice_to_text_dialog, null);
        this.mRecodingStateAnimatorView = (RecodingStateAnimatorView) this.mVoiceToTextLayout.findViewById(R.id.ball_view);
        this.mDynamicVoiceInput = (TextView) this.mVoiceToTextLayout.findViewById(R.id.dynamic_voice_input);
    }

    public static void log(String msg) {
        log(false, msg);
    }

    public static void log(boolean debug, String msg) {
        if (debug || DEBUG) {
            Log.d(TAG, msg);
        }
    }
}
