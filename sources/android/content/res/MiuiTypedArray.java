package android.content.res;

public class MiuiTypedArray extends TypedArray {
    private boolean mIsMiuiResources = (getResources() instanceof MiuiResources);

    MiuiTypedArray(Resources resources) {
        super(resources);
    }

    public CharSequence getText(int index) {
        CharSequence cs = loadStringValueAt(index * 7);
        if (cs != null) {
            return cs;
        }
        return super.getText(index);
    }

    public String getString(int index) {
        CharSequence cs = loadStringValueAt(index * 7);
        if (cs != null) {
            return cs.toString();
        }
        return super.getString(index);
    }

    private CharSequence loadStringValueAt(int index) {
        if (this.mIsMiuiResources && this.mData[index + 0] == 3) {
            CharSequence cs = ((MiuiResources) getResources()).getThemeString(this.mData[index + 3]);
            if (cs != null) {
                return cs;
            }
        }
        return null;
    }
}
