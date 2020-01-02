package android.inputmethodservice;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.InputMethodSwitchAdapter.ClickListener;
import android.miui.R;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.util.List;

class InputMethodSwitchPopupView extends PopupWindow implements OnClickListener {
    private static final int MAX_LIST_VIEW_ITEM_NUMBER = 4;
    private static final String SETTINGS_URI = "intent:#Intent;component=com.android.settings/.SubSettings;S.:settings:show_fragment=com.android.settings.language.MiuiLanguageAndInputSettings;end";
    private static final String TAG = "InputMethodSwitchView";
    private int mBottomHeight;
    private Context mContext;
    private List<InputMethodInfo> mInputMethodList;
    private InputMethodService mInputMethodService;
    private InputMethodSwitchAdapter mInputMethodSwitchAdapter;
    private boolean mIsLeft;
    private ListView mListView;
    private View mTransitionView;

    public InputMethodSwitchPopupView(Context context, List<InputMethodInfo> inputMethodList, InputMethodService inputMethodService, int bottomHeight, boolean isLeft) {
        this.mContext = context;
        this.mInputMethodList = inputMethodList;
        this.mInputMethodService = inputMethodService;
        this.mBottomHeight = bottomHeight;
        this.mIsLeft = isLeft;
    }

    public void initPopupWindow() {
        setHeight(-1);
        setWidth(-1);
        int i = 0;
        setBackgroundDrawable(new ColorDrawable(0));
        View contentView = LayoutInflater.from(this.mContext).inflate((int) R.layout.input_method_switch_view, null, false);
        FrameLayout outsideView = (FrameLayout) contentView.findViewById(R.id.switch_outside_view);
        LinearLayout insideView = (LinearLayout) contentView.findViewById(R.id.switch_inside_view);
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
        outsideView.setOnClickListener(this);
        int shadowHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.input_method_pop_view_shadow_height);
        lp.bottomMargin = (this.mBottomHeight - shadowHeight) + this.mContext.getResources().getDimensionPixelSize(R.dimen.input_method_pop_view_margin_bottom);
        insideView.setLayoutParams(lp);
        ((FrameLayout) contentView.findViewById(R.id.switch_list_view_layout)).setOnClickListener(null);
        this.mTransitionView = contentView.findViewById(R.id.switch_transitional_view);
        View view = this.mTransitionView;
        if (this.mInputMethodList.size() <= 4) {
            i = 8;
        }
        view.setVisibility(i);
        this.mListView = (ListView) contentView.findViewById(R.id.switch_list_view);
        ((LinearLayout) contentView.findViewById(R.id.keyboard_setting_button)).setOnClickListener(this);
        this.mInputMethodSwitchAdapter = new InputMethodSwitchAdapter(this.mContext, R.layout.input_method_switch_item_view, this.mInputMethodList);
        this.mListView.setAdapter(this.mInputMethodSwitchAdapter);
        setListViewOnScrollListener();
        setListViewHeight();
        setListViewListener();
        setContentView(contentView);
    }

    private void setListViewOnScrollListener() {
        this.mListView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount + firstVisibleItem != totalItemCount || totalItemCount <= 0) {
                    InputMethodSwitchPopupView.this.mTransitionView.setVisibility(0);
                    return;
                }
                View lastVisibleItemView = InputMethodSwitchPopupView.this.mListView.getChildAt((totalItemCount - firstVisibleItem) - 1);
                if (lastVisibleItemView == null || lastVisibleItemView.getBottom() != view.getHeight()) {
                    InputMethodSwitchPopupView.this.mTransitionView.setVisibility(0);
                } else {
                    InputMethodSwitchPopupView.this.mTransitionView.setVisibility(8);
                }
            }
        });
    }

    private void setListViewListener() {
        this.mInputMethodSwitchAdapter.setClickListener(new ClickListener() {
            public void clickItem(View view, InputMethodInfo info) {
                InputMethodSwitchPopupView.this.mInputMethodService.switchInputMethod(info.getId());
                InputMethodAnalyticsUtil.addInputMethodAnalytics(InputMethodSwitchPopupView.this.mContext, info.getPackageName());
            }
        });
    }

    public void onClick(View v) {
        int id = v.getId();
        String str = TAG;
        if (id == R.id.keyboard_setting_button) {
            try {
                Intent intent = Intent.parseUri(SETTINGS_URI, 1);
                intent.setFlags(268435456);
                this.mContext.startActivity(intent);
                InputMethodAnalyticsUtil.addInputMethodAnalytics(this.mContext, "clipboard_settings");
            } catch (Exception e) {
                Log.e(str, "start settings error", e);
            }
            dismiss();
        } else if (id != R.id.switch_outside_view) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unknown view id : ");
            stringBuilder.append(v.getId());
            Log.e(str, stringBuilder.toString());
        } else {
            dismiss();
        }
    }

    private void setListViewHeight() {
        int totalItem = this.mInputMethodSwitchAdapter;
        if (totalItem != 0) {
            int totalHeight = 0;
            totalItem = totalItem.getCount();
            int i = 4;
            if (totalItem <= 4) {
                i = totalItem;
            }
            totalItem = i;
            for (i = 0; i < totalItem; i++) {
                View item = this.mInputMethodSwitchAdapter.getView(i, null, this.mListView);
                item.measure(0, 0);
                totalHeight += item.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = this.mListView.getLayoutParams();
            params.height = totalHeight;
            this.mListView.setLayoutParams(params);
        }
    }
}
