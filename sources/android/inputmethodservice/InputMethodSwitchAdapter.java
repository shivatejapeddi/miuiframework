package android.inputmethodservice;

import android.content.Context;
import android.miui.R;
import android.provider.Settings.Secure;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

class InputMethodSwitchAdapter extends ArrayAdapter<InputMethodInfo> {
    private ClickListener mClickListener;
    private Context mContext;
    private String mDefaultInputMethod = Secure.getString(this.mContext.getContentResolver(), Secure.DEFAULT_INPUT_METHOD);
    private List<InputMethodInfo> mInputMethodList;
    private int mResourceId;

    public interface ClickListener {
        void clickItem(View view, InputMethodInfo inputMethodInfo);
    }

    class ViewHolder {
        LinearLayout firstLineView;
        ImageView selectedImage;
        TextView textItem;
        LinearLayout textLayout;

        ViewHolder() {
        }
    }

    public InputMethodSwitchAdapter(Context context, int resource, List<InputMethodInfo> inputMethodList) {
        super(context, resource, (List) inputMethodList);
        this.mContext = context;
        this.mResourceId = resource;
        this.mInputMethodList = inputMethodList;
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(this.mResourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textItem = (TextView) view.findViewById(R.id.input_method_name);
            viewHolder.textLayout = (LinearLayout) view.findViewById(R.id.input_method_switch_layout);
            viewHolder.firstLineView = (LinearLayout) view.findViewById(R.id.input_method_switch_fist_line);
            viewHolder.selectedImage = (ImageView) view.findViewById(R.id.input_method_selected);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.firstLineView.setVisibility(position == 0 ? 0 : 8);
        final InputMethodInfo info = (InputMethodInfo) this.mInputMethodList.get(position);
        viewHolder.textItem.setText(getImeDisplayName(this.mContext, info).toString());
        if (this.mDefaultInputMethod.equals(info.getId())) {
            viewHolder.textItem.setTextColor(this.mContext.getResources().getColor(R.color.select_tab_text_color));
            viewHolder.selectedImage.setVisibility(0);
        } else {
            viewHolder.textItem.setTextColor(this.mContext.getResources().getColor(R.color.unselected_tab_text_color));
            viewHolder.selectedImage.setVisibility(4);
        }
        viewHolder.textLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (InputMethodSwitchAdapter.this.mClickListener != null) {
                    InputMethodSwitchAdapter.this.mClickListener.clickItem(v, info);
                }
            }
        });
        return view;
    }

    private CharSequence getImeDisplayName(Context context, InputMethodInfo imi) {
        return imi.loadLabel(context.getPackageManager());
    }
}
