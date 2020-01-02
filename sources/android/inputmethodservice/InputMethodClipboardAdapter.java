package android.inputmethodservice;

import android.content.Context;
import android.inputmethodservice.InputMethodClipboardPhrasePopupView.ClipboardJsonUtil;
import android.miui.R;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

class InputMethodClipboardAdapter extends ArrayAdapter<ClipboardContentModel> {
    private static final String KEY_INPUT_PROVIDER_URI = "content://com.miui.input.provider";
    private static final String KEY_SAVE_CLOUD_CLIPBOARD_CONTENT_METHOD = "saveCloudClipboardContent";
    private static final String TAG = "ClipboardAdapter";
    private ArrayList<ClipboardContentModel> mClipboardList = new ArrayList();
    private Context mContext;
    private InputConnection mInputConnection;
    private InputMethodClipboardPhrasePopupView mInputMethodClipboardPhrasePopupView;
    private ClipboardJsonUtil mJsonUtil;
    private int mResourceId;

    class ViewHolder {
        ImageView cloudIcon;
        LinearLayout deleteBtn;
        ImageView deleteImage;
        LinearLayout firstLineView;
        TextView textItem;
        RelativeLayout textLayout;

        ViewHolder() {
        }
    }

    public InputMethodClipboardAdapter(Context context, int resource, ArrayList<ClipboardContentModel> clipboardList, InputConnection inputConnection, InputMethodClipboardPhrasePopupView inputMethodClipboardPhrasePopupView) {
        super(context, resource, (List) clipboardList);
        this.mContext = context;
        this.mResourceId = resource;
        this.mInputConnection = inputConnection;
        this.mClipboardList = clipboardList;
        this.mInputMethodClipboardPhrasePopupView = inputMethodClipboardPhrasePopupView;
        this.mJsonUtil = new ClipboardJsonUtil(context);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        int i = 0;
        if (convertView == null) {
            view = LayoutInflater.from(this.mContext).inflate(this.mResourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textItem = (TextView) view.findViewById(R.id.clipboard_text_item);
            viewHolder.deleteBtn = (LinearLayout) view.findViewById(R.id.clipboard_delete_layout);
            viewHolder.firstLineView = (LinearLayout) view.findViewById(R.id.clipboard_first_line_view);
            viewHolder.deleteImage = (ImageView) view.findViewById(R.id.clipboard_delete_btn);
            viewHolder.cloudIcon = (ImageView) view.findViewById(R.id.cloud_icon);
            viewHolder.deleteImage.setColorFilter(this.mContext.getResources().getColor(R.color.input_method_delete_button_color));
            viewHolder.textLayout = (RelativeLayout) view.findViewById(R.id.clipboard_text_layout);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.firstLineView.setVisibility(position == 0 ? 0 : 8);
        ImageView imageView = viewHolder.cloudIcon;
        if (((ClipboardContentModel) this.mClipboardList.get(position)).getType() != 1) {
            i = 8;
        }
        imageView.setVisibility(i);
        viewHolder.textItem.setText(((ClipboardContentModel) this.mClipboardList.get(position)).getContent());
        viewHolder.deleteBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (((ClipboardContentModel) InputMethodClipboardAdapter.this.mClipboardList.get(position)).getType() == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putString("content", "");
                    try {
                        InputMethodClipboardAdapter.this.mContext.getContentResolver().call(Uri.parse(InputMethodClipboardAdapter.KEY_INPUT_PROVIDER_URI), InputMethodClipboardAdapter.KEY_SAVE_CLOUD_CLIPBOARD_CONTENT_METHOD, null, bundle);
                    } catch (Exception e) {
                        Log.e(InputMethodClipboardAdapter.TAG, "save clipboard words error", e);
                    }
                }
                InputMethodClipboardAdapter.this.mClipboardList.remove(position);
                InputMethodClipboardAdapter.this.notifyDataSetChanged();
                InputMethodClipboardAdapter.this.mJsonUtil.setClipboardModelList(InputMethodClipboardAdapter.this.mClipboardList);
            }
        });
        viewHolder.textLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                InputMethodClipboardAdapter.this.mInputConnection.commitText(((ClipboardContentModel) InputMethodClipboardAdapter.this.mClipboardList.get(position)).getContent(), 1);
                InputMethodClipboardAdapter.this.mInputMethodClipboardPhrasePopupView.dismiss();
            }
        });
        return view;
    }
}
