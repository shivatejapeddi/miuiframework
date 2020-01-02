package android.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.miui.R;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import com.miui.translationservice.provider.TranslationResult;
import com.miui.translationservice.provider.TranslationResult.Part;
import com.miui.translationservice.provider.TranslationResult.Symbol;
import java.util.List;
import miui.maml.elements.MusicLyricParser;
import miui.widget.ProgressBar;

class TranslationPresenter {
    private boolean mAboveHandle;
    private Context mContext;
    private TextView mCopyright = ((TextView) this.mTranslationPanel.findViewById(R.id.text_copyright));
    private int mDefaultPaddingBottom;
    private DisplayMetrics mDisplayMetrics;
    private View mExtraInfo = this.mTranslationPanel.findViewById(R.id.extra_info);
    private int mMaxHeight;
    private int mMinHeight;
    private TextView mMore = ((TextView) this.mTranslationPanel.findViewById(R.id.text_more));
    private int mPaddingOffset;
    private ProgressBar mProgressBar = ((ProgressBar) this.mTranslationPanel.findViewById(16908301));
    private View mScrollContainer = this.mTranslationPanel.findViewById(R.id.text_action_background_view);
    private View mScrollView = this.mTranslationPanel.findViewById(R.id.scroll_view);
    private View mTextContainer = this.mTranslationPanel.findViewById(R.id.text_container);
    private View mTranslationPanel;
    private TextView mTranslations = ((TextView) this.mTranslationPanel.findViewById(16908309));
    private TextView mWord = ((TextView) this.mTranslationPanel.findViewById(16908308));

    public TranslationPresenter(Context context, View translationPanel) {
        this.mContext = context;
        this.mTranslationPanel = translationPanel;
        this.mMinHeight = context.getResources().getDimensionPixelSize(R.dimen.text_action_translation_min_height);
        this.mMaxHeight = context.getResources().getDimensionPixelSize(R.dimen.text_action_translation_max_height);
        this.mDisplayMetrics = context.getResources().getDisplayMetrics();
        this.mDefaultPaddingBottom = context.getResources().getDimensionPixelSize(R.dimen.text_action_translation_padding_bottom);
        this.mPaddingOffset = context.getResources().getDimensionPixelSize(R.dimen.text_action_translation_padding_offset);
    }

    public void setInProgress() {
        int i;
        this.mWord.setVisibility(8);
        this.mTranslations.setVisibility(8);
        this.mExtraInfo.setVisibility(8);
        this.mProgressBar.setVisibility(0);
        View view = this.mScrollView;
        view.setPadding(view.getPaddingLeft(), this.mScrollView.getPaddingTop(), this.mScrollView.getPaddingRight(), this.mDefaultPaddingBottom);
        LayoutParams lp = this.mScrollView.getLayoutParams();
        lp.height = this.mMinHeight;
        this.mScrollView.setLayoutParams(lp);
        Rect rect = new Rect();
        this.mScrollContainer.getBackground().getPadding(rect);
        lp = new RelativeLayout.LayoutParams(-1, this.mMinHeight + (rect.top + rect.bottom));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) lp;
        if (this.mAboveHandle) {
            i = 12;
        } else {
            i = 10;
        }
        layoutParams.addRule(i);
        this.mScrollContainer.setLayoutParams(lp);
    }

    public void updatePanel(final TranslationResult result) {
        int i;
        int j;
        int k;
        View view;
        int i2 = 0;
        this.mWord.setVisibility(0);
        this.mProgressBar.setVisibility(8);
        if (result == null || result.getStatus() != 0) {
            this.mTranslations.setVisibility(0);
            if (result == null) {
                this.mWord.setVisibility(8);
                this.mTranslations.setText(this.mContext.getString(R.string.translation_query_error_network));
            } else if (result.getStatus() == -2) {
                this.mWord.setText(result.getWordName());
                this.mTranslations.setText(this.mContext.getString(R.string.translation_query_error_network));
            } else {
                this.mWord.setText(result.getWordName());
                this.mTranslations.setText(this.mContext.getString(R.string.translation_query_error));
            }
        } else {
            int paddingBottom;
            this.mWord.setText(result.getWordName());
            this.mTranslations.setVisibility(0);
            StringBuilder builder = new StringBuilder();
            List<Symbol> symbolList = result.getSymbols();
            for (i = 0; i < symbolList.size(); i++) {
                Symbol symbol = (Symbol) symbolList.get(i);
                boolean hasPronun = false;
                String str = "]\r\n";
                String str2 = "[";
                if (!TextUtils.isEmpty(symbol.getWordSymbol())) {
                    builder.append(str2);
                    builder.append(symbol.getWordSymbol());
                    builder.append(str);
                    hasPronun = true;
                }
                if (!(hasPronun || TextUtils.isEmpty(symbol.getPhEn()))) {
                    builder.append(str2);
                    builder.append(symbol.getPhEn());
                    builder.append(str);
                    hasPronun = true;
                }
                if (!(hasPronun || TextUtils.isEmpty(symbol.getPhAm()))) {
                    builder.append(str2);
                    builder.append(symbol.getPhAm());
                    builder.append(str);
                }
                List<Part> partList = symbol.getParts();
                j = 0;
                while (j < partList.size()) {
                    Part part = (Part) partList.get(j);
                    if (!TextUtils.isEmpty(part.getPart())) {
                        builder.append("(");
                        builder.append(part.getPart());
                        builder.append(") ");
                    }
                    List<String> means = part.getMeans();
                    for (k = 0; k < means.size(); k++) {
                        builder.append((String) means.get(k));
                        if (k != means.size() - 1 || j == partList.size() - 1) {
                            builder.append("/");
                        } else {
                            builder.append(MusicLyricParser.CRLF);
                        }
                    }
                    j++;
                }
            }
            this.mTranslations.setText(builder.toString());
            CharSequence copyright = result.getCopyright();
            boolean hasCopyright = TextUtils.isEmpty(copyright) ^ 1;
            if (hasCopyright) {
                this.mExtraInfo.setVisibility(0);
                this.mCopyright.setVisibility(0);
                this.mCopyright.setText(copyright);
            }
            final String detailLink = result.getDetailLink();
            if (TextUtils.isEmpty(detailLink) ^ 1) {
                this.mExtraInfo.setVisibility(0);
                this.mMore.setVisibility(0);
                this.mMore.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        String str = "android.intent.action.VIEW";
                        Intent intent = new Intent(str, Uri.parse(String.format(detailLink, new Object[]{result.getWordName()})));
                        if (!(TranslationPresenter.this.mContext instanceof Activity)) {
                            intent.addFlags(268435456);
                        }
                        TranslationPresenter.this.mContext.startActivity(intent);
                    }
                });
            }
            LayoutParams lp = this.mExtraInfo.getLayoutParams();
            if (hasCopyright) {
                paddingBottom = lp.height - this.mPaddingOffset;
            } else {
                paddingBottom = this.mDefaultPaddingBottom;
            }
            view = this.mScrollView;
            view.setPadding(view.getPaddingLeft(), this.mScrollView.getPaddingTop(), this.mScrollView.getPaddingRight(), paddingBottom);
        }
        Rect rect = new Rect();
        this.mScrollContainer.getBackground().getPadding(rect);
        int hPaddings = rect.left + rect.right;
        i = rect.top + rect.bottom;
        int panelWidth = this.mTranslationPanel.getMeasuredWidth();
        int panelHeight = this.mTranslationPanel.getMeasuredHeight();
        this.mTextContainer.measure(MeasureSpec.makeMeasureSpec(((panelWidth - hPaddings) - this.mScrollView.getPaddingLeft()) - this.mScrollView.getPaddingRight(), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(this.mDisplayMetrics.heightPixels, 0));
        j = this.mTextContainer.getMeasuredHeight();
        LayoutParams lp2 = this.mScrollView.getLayoutParams();
        lp2.width = panelWidth - hPaddings;
        lp2.height = Math.min(this.mMaxHeight, (this.mScrollView.getPaddingTop() + j) + this.mScrollView.getPaddingBottom());
        this.mScrollView.setLayoutParams(lp2);
        lp2 = this.mScrollContainer.getLayoutParams();
        lp2.height = Math.min(this.mMaxHeight, this.mScrollView.getLayoutParams().height) + i;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) lp2;
        if (this.mAboveHandle) {
            k = 12;
        } else {
            k = 10;
        }
        layoutParams.addRule(k);
        this.mScrollContainer.setLayoutParams(lp2);
        this.mScrollContainer.setLeft(0);
        view = this.mScrollContainer;
        if (this.mAboveHandle) {
            i2 = panelHeight - lp2.height;
        }
        view.setTop(i2);
    }

    public void setAboveHandle(boolean aboveHandle) {
        this.mAboveHandle = aboveHandle;
    }
}
