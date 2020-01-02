package android.inputmethodservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.miui.R;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MiuiSettings.FrequentPhrases;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputConnection;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

class InputMethodClipboardPhrasePopupView extends PopupWindow implements OnClickListener {
    private static final String AUTHORITY = "miui.phrase";
    private static final String KEY_ADD_CLIPBOARD_STRING_METHOD = "addClipboardString";
    private static final String KEY_SAVE_CLIPBOARD_STRING_METHOD = "saveClipboardString";
    private static final String KEY_SAVE_CLIPBOARD_STRING_URI = "content://com.miui.input.provider";
    private static final String TAG = "InputMethodClipboard";
    private static final String WORDS = "words";
    private static final Uri WORDS_CONTENT_URI = Uri.parse("content://miui.phrase/phrase/words");
    private static String mCloudContent;
    private int mBottomHeight;
    private TextView mClipboardText;
    private Context mContext;
    private InputConnection mInputConnection;
    private InputMethodClipboardAdapter mInputMethodClipboardAdapter;
    private InputMethodPhraseAdapter mInputMethodPhraseAdapter;
    private boolean mIsLeft;
    private ClipboardJsonUtil mJsonUtil;
    private ListView mListView;
    private Handler mMainHandler;
    private LinearLayout mPhraseLayout;
    private ArrayList<String> mPhraseList = new ArrayList();
    private TextView mPhraseText;
    private Handler mSubHandler;
    private View mTransitionView;

    public static class ClipboardJsonUtil {
        private static final String KEY_CLIPBOARD_LIST = "KEY_CLIPBOARD_LIST";
        private Context mContext;

        public ClipboardJsonUtil(Context context) {
            this.mContext = context;
        }

        public void setClipboardModelList(ArrayList<ClipboardContentModel> list) {
            ArrayList<String> contentList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                if (((ClipboardContentModel) list.get(i)).getType() == 0) {
                    contentList.add(((ClipboardContentModel) list.get(i)).getContent());
                }
            }
            setClipboardList(contentList);
        }

        public void setClipboardList(ArrayList<String> list) {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                jsonArray.put(list.get(i));
            }
            Bundle bundle = new Bundle();
            bundle.putString("jsonArray", jsonArray.toString());
            try {
                this.mContext.getContentResolver().call(Uri.parse(InputMethodClipboardPhrasePopupView.KEY_SAVE_CLIPBOARD_STRING_URI), InputMethodClipboardPhrasePopupView.KEY_SAVE_CLIPBOARD_STRING_METHOD, null, bundle);
            } catch (Exception e) {
                Log.e(InputMethodClipboardPhrasePopupView.TAG, "save clipboard words error", e);
            }
        }

        public static void addClipboard(String content, Context context) {
            Bundle bundle = new Bundle();
            bundle.putString("contentToAdd", content);
            try {
                context.getContentResolver().call(Uri.parse(InputMethodClipboardPhrasePopupView.KEY_SAVE_CLIPBOARD_STRING_URI), InputMethodClipboardPhrasePopupView.KEY_SAVE_CLIPBOARD_STRING_METHOD, null, bundle);
            } catch (Exception e) {
                Log.e(InputMethodClipboardPhrasePopupView.TAG, "add clipboard words error", e);
            }
        }

        public ArrayList<String> getClipboardList() {
            String json = Secure.getString(this.mContext.getContentResolver(), KEY_CLIPBOARD_LIST);
            ArrayList<String> list = new ArrayList();
            if (json != null) {
                try {
                    JSONArray mJSONArray = new JSONArray(json);
                    for (int i = 0; i < mJSONArray.length(); i++) {
                        list.add(mJSONArray.optString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
    }

    public InputMethodClipboardPhrasePopupView(Context context, InputConnection inputConnection, int bottomHeight, boolean isLeft, String cloudContent) {
        super(context);
        this.mContext = context;
        this.mJsonUtil = new ClipboardJsonUtil(this.mContext);
        this.mInputConnection = inputConnection;
        this.mBottomHeight = bottomHeight;
        this.mIsLeft = isLeft;
        mCloudContent = cloudContent;
        HandlerThread handlerThread = new HandlerThread("query_phrase");
        handlerThread.start();
        this.mSubHandler = new Handler(handlerThread.getLooper());
        this.mMainHandler = new Handler();
    }

    public void initPopupWindow(View parentView) {
        setHeight(-1);
        setWidth(-1);
        setBackgroundDrawable(new ColorDrawable(0));
        View contentView = LayoutInflater.from(this.mContext).inflate((int) R.layout.clipboard_pop_view, null, false);
        LinearLayout phraseButton = (LinearLayout) contentView.findViewById(R.id.phrase_button);
        ((LinearLayout) contentView.findViewById(R.id.clipboard_button)).setOnClickListener(this);
        phraseButton.setOnClickListener(this);
        this.mClipboardText = (TextView) contentView.findViewById(R.id.clipboard_text);
        this.mPhraseText = (TextView) contentView.findViewById(R.id.phrase_text);
        this.mClipboardText.setTextColor(this.mContext.getResources().getColor(R.color.select_tab_text_color));
        this.mPhraseText.setTextColor(this.mContext.getResources().getColor(R.color.unselected_tab_text_color));
        this.mPhraseLayout = (LinearLayout) contentView.findViewById(R.id.phrase_layout);
        ((LinearLayout) contentView.findViewById(R.id.phrase_edit_button)).setOnClickListener(this);
        ((FrameLayout) contentView.findViewById(R.id.outside_view)).setOnClickListener(this);
        LinearLayout insideView = (LinearLayout) contentView.findViewById(R.id.inside_view);
        LayoutParams lp = (LayoutParams) insideView.getLayoutParams();
        if (this.mIsLeft) {
            lp.gravity = 8388691;
            insideView.setBackgroundResource(R.drawable.input_method_pop_view_bg_left);
            setAnimationStyle(R.style.InputMethodWindowAnimationLeft);
        } else {
            lp.gravity = 8388693;
            insideView.setBackgroundResource(R.drawable.input_method_pop_view_bg_right);
            setAnimationStyle(R.style.InputMethodWindowAnimationRight);
        }
        int shadowHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.input_method_pop_view_shadow_height);
        lp.bottomMargin = (this.mBottomHeight - shadowHeight) + this.mContext.getResources().getDimensionPixelSize(R.dimen.input_method_pop_view_margin_bottom);
        insideView.setLayoutParams(lp);
        RelativeLayout listViewLayout = (RelativeLayout) contentView.findViewById(R.id.list_view_layout);
        listViewLayout.setOnClickListener(null);
        this.mTransitionView = contentView.findViewById(R.id.clipboard_transitional_view);
        this.mListView = (ListView) contentView.findViewById(R.id.list_view);
        TextView clipboardNoItems = (TextView) contentView.findViewById(R.id.clipboard_no_items);
        ArrayList<ClipboardContentModel> clipboardList = getClipboardTotalList();
        InputMethodClipboardAdapter inputMethodClipboardAdapter = r0;
        TextView clipboardNoItems2 = clipboardNoItems;
        InputMethodClipboardAdapter inputMethodClipboardAdapter2 = new InputMethodClipboardAdapter(this.mContext, R.layout.clipboard_item_layout, clipboardList, this.mInputConnection, this);
        this.mInputMethodClipboardAdapter = inputMethodClipboardAdapter;
        this.mInputMethodPhraseAdapter = new InputMethodPhraseAdapter(this.mContext, R.layout.phrase_item_layout, this.mPhraseList, this.mInputConnection, this);
        this.mListView.setAdapter(this.mInputMethodClipboardAdapter);
        setListViewOnScrollListener();
        this.mListView.setEmptyView(clipboardNoItems2);
        this.mInputMethodClipboardAdapter.notifyDataSetChanged();
        setContentView(contentView);
        showAtLocation(parentView, 0, 0, 0);
        this.mSubHandler.post(new Runnable() {
            public void run() {
                InputMethodClipboardPhrasePopupView inputMethodClipboardPhrasePopupView = InputMethodClipboardPhrasePopupView.this;
                inputMethodClipboardPhrasePopupView.mPhraseList = inputMethodClipboardPhrasePopupView.queryPhrase();
                InputMethodClipboardPhrasePopupView.this.mMainHandler.post(new Runnable() {
                    public void run() {
                        InputMethodClipboardPhrasePopupView.this.mInputMethodPhraseAdapter.setPhraseList(InputMethodClipboardPhrasePopupView.this.mPhraseList);
                    }
                });
            }
        });
    }

    public void onClick(View v) {
        int id = v.getId();
        String str = TAG;
        switch (id) {
            case R.id.clipboard_button /*285802545*/:
            case R.id.clipboard_text /*285802551*/:
                this.mListView.setAdapter(null);
                this.mTransitionView.setVisibility(0);
                this.mClipboardText.setTextColor(this.mContext.getResources().getColor(R.color.select_tab_text_color));
                this.mPhraseText.setTextColor(this.mContext.getResources().getColor(R.color.unselected_tab_text_color));
                this.mPhraseLayout.setVisibility(8);
                this.mListView.setAdapter(this.mInputMethodClipboardAdapter);
                return;
            case R.id.outside_view /*285802604*/:
                dismiss();
                return;
            case R.id.phrase_button /*285802605*/:
            case R.id.phrase_text /*285802610*/:
                this.mListView.setAdapter(null);
                this.mTransitionView.setVisibility(0);
                this.mClipboardText.setTextColor(this.mContext.getResources().getColor(R.color.unselected_tab_text_color));
                this.mPhraseText.setTextColor(this.mContext.getResources().getColor(R.color.select_tab_text_color));
                this.mPhraseLayout.setVisibility(0);
                this.mListView.setAdapter(this.mInputMethodPhraseAdapter);
                return;
            case R.id.phrase_edit_button /*285802606*/:
                dismiss();
                try {
                    ComponentName componentName = new ComponentName("com.miui.phrase", "com.miui.phrase.PhraseEditActivity");
                    Intent intent = new Intent();
                    intent.setComponent(componentName);
                    intent.setFlags(268435456);
                    boolean isIntentExists = isIntentAvailable(intent);
                    if (!isIntentExists) {
                        intent.setComponent(new ComponentName("com.miui.system", "miui.phrase.PhraseEditActivity"));
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("isIntentAvailable : ");
                    stringBuilder.append(isIntentExists);
                    Log.d(str, stringBuilder.toString());
                    this.mContext.startActivity(intent);
                    return;
                } catch (Exception e) {
                    Log.e(str, "start PhraseEditActivity error.", e);
                    return;
                }
            default:
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Unknown view id : ");
                stringBuilder2.append(v.getId());
                Log.e(str, stringBuilder2.toString());
                return;
        }
    }

    private boolean isIntentAvailable(Intent intent) {
        try {
            List<ResolveInfo> resolveInfoList = this.mContext.getPackageManager().queryIntentActivities(intent, 1);
            if (resolveInfoList != null && resolveInfoList.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setListViewOnScrollListener() {
        this.mListView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount + firstVisibleItem != totalItemCount || totalItemCount <= 0) {
                    InputMethodClipboardPhrasePopupView.this.mTransitionView.setVisibility(0);
                    return;
                }
                View lastVisibleItemView = InputMethodClipboardPhrasePopupView.this.mListView.getChildAt((totalItemCount - firstVisibleItem) - 1);
                if (lastVisibleItemView == null || lastVisibleItemView.getBottom() != view.getHeight()) {
                    InputMethodClipboardPhrasePopupView.this.mTransitionView.setVisibility(0);
                } else {
                    InputMethodClipboardPhrasePopupView.this.mTransitionView.setVisibility(8);
                }
            }
        });
    }

    private ArrayList<String> queryPhrase() {
        Cursor cursor = null;
        ArrayList<String> phraseList = new ArrayList();
        try {
            cursor = this.mContext.getContentResolver().query(WORDS_CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String word = cursor.getString(cursor.getColumnIndex(WORDS));
                    if (!TextUtils.isEmpty(word)) {
                        phraseList.add(word);
                    }
                }
            } else {
                Log.d(TAG, "fail to query Phrases.Phrase.WORDS_CONTENT_URI");
                phraseList = FrequentPhrases.getFrequentPhrases(this.mContext);
                if (phraseList == null) {
                    phraseList = new ArrayList();
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            return phraseList;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private ArrayList<ClipboardContentModel> getClipboardTotalList() {
        ArrayList<String> sysClipboardList = this.mJsonUtil.getClipboardList();
        ArrayList<ClipboardContentModel> clipboardList = new ArrayList();
        if (!TextUtils.isEmpty(mCloudContent)) {
            clipboardList.add(new ClipboardContentModel(mCloudContent, 1));
        }
        for (int i = 0; i < sysClipboardList.size(); i++) {
            clipboardList.add(new ClipboardContentModel((String) sysClipboardList.get(i), 0));
        }
        return clipboardList;
    }
}
