package android.inputmethodservice;

import android.content.Context;
import android.miui.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class InputMethodPhraseAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private InputConnection mInputConnection;
    private InputMethodClipboardPhrasePopupView mInputMethodClipboardPhrasePopupView;
    private ArrayList<String> mPhraseList;
    private int mResourceId;

    class ViewHolder {
        LinearLayout firstLineView;
        TextView textItem;
        LinearLayout textLayout;

        ViewHolder() {
        }
    }

    public InputMethodPhraseAdapter(Context context, int resource, ArrayList<String> phraseList, InputConnection inputConnection, InputMethodClipboardPhrasePopupView inputMethodClipboardPhrasePopupView) {
        super(context, resource, (List) phraseList);
        this.mContext = context;
        this.mResourceId = resource;
        this.mInputConnection = inputConnection;
        this.mPhraseList = phraseList;
        this.mInputMethodClipboardPhrasePopupView = inputMethodClipboardPhrasePopupView;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        int i = 0;
        if (convertView == null) {
            view = LayoutInflater.from(this.mContext).inflate(this.mResourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textItem = (TextView) view.findViewById(R.id.phrase_text_item);
            viewHolder.firstLineView = (LinearLayout) view.findViewById(R.id.phrase_first_line_view);
            viewHolder.textLayout = (LinearLayout) view.findViewById(R.id.phrase_text_layout);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        LinearLayout linearLayout = viewHolder.firstLineView;
        if (position != 0) {
            i = 8;
        }
        linearLayout.setVisibility(i);
        viewHolder.textItem.setText((CharSequence) this.mPhraseList.get(position));
        viewHolder.textLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                InputMethodPhraseAdapter.this.mInputConnection.commitText((CharSequence) InputMethodPhraseAdapter.this.mPhraseList.get(position), 1);
                InputMethodPhraseAdapter.this.mInputMethodClipboardPhrasePopupView.dismiss();
            }
        });
        return view;
    }

    public void setPhraseList(ArrayList<String> phraseList) {
        this.mPhraseList = phraseList;
        clear();
        addAll((Collection) phraseList);
        notifyDataSetChanged();
    }
}
