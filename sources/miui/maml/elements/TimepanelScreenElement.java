package miui.maml.elements;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.TimeZone;
import miui.maml.ScreenElementRoot;
import miui.maml.data.Expression;
import miui.maml.util.Utils;
import org.w3c.dom.Element;

public class TimepanelScreenElement extends ImageScreenElement {
    private static final String LOG_TAG = "TimepanelScreenElement";
    private static final String M12 = "hh:mm";
    private static final String M24 = "kk:mm";
    public static final String TAG_NAME = "Time";
    private int mBmpHeight;
    private int mBmpWidth;
    protected Calendar mCalendar = Calendar.getInstance();
    private boolean mForceUpdate;
    private String mFormat;
    private Expression mFormatExp;
    private String mFormatRaw;
    private boolean mLoadResourceFailed;
    private char mLocalizedZero = DecimalFormatSymbols.getInstance().getZeroDigit();
    private String mOldFormat;
    private String mOldSrc;
    private long mPreMinute;
    private CharSequence mPreTime;
    private int mSpace;
    private Expression mTimeZoneExp;
    private Runnable mUpdateTimeRunnable = new Runnable() {
        public void run() {
            if (!TimepanelScreenElement.this.mLoadResourceFailed) {
                Bitmap bitmap = TimepanelScreenElement.this.mBitmap.getBitmap();
                if (bitmap != null) {
                    TimepanelScreenElement.this.mCalendar.setTimeInMillis(System.currentTimeMillis());
                    if (TimepanelScreenElement.this.mTimeZoneExp != null) {
                        String timeZoneId = TimepanelScreenElement.this.mTimeZoneExp.evaluateStr();
                        if (!TextUtils.isEmpty(timeZoneId)) {
                            TimepanelScreenElement.this.mCalendar.setTimeZone(TimeZone.getTimeZone(timeZoneId));
                        }
                    }
                    CharSequence newTime = DateFormat.format(TimepanelScreenElement.this.getFormat(), TimepanelScreenElement.this.mCalendar);
                    if (TimepanelScreenElement.this.mForceUpdate || !newTime.equals(TimepanelScreenElement.this.mPreTime)) {
                        TimepanelScreenElement.this.mPreTime = newTime;
                        Canvas tmpCanvas = new Canvas(bitmap);
                        tmpCanvas.drawColor(0, Mode.CLEAR);
                        int x = 0;
                        for (int i = 0; i < newTime.length(); i++) {
                            Bitmap bmp = TimepanelScreenElement.this.getDigitBmp(newTime.charAt(i));
                            if (bmp != null) {
                                tmpCanvas.drawBitmap(bmp, (float) x, 0.0f, null);
                                x = (x + bmp.getWidth()) + TimepanelScreenElement.this.mSpace;
                            }
                        }
                        TimepanelScreenElement.this.mBitmap.updateVersion();
                        TimepanelScreenElement timepanelScreenElement = TimepanelScreenElement.this;
                        timepanelScreenElement.mBmpWidth = x - timepanelScreenElement.mSpace;
                        timepanelScreenElement = TimepanelScreenElement.this;
                        timepanelScreenElement.setActualWidth(timepanelScreenElement.descale((double) timepanelScreenElement.mBmpWidth));
                        TimepanelScreenElement.this.requestUpdate();
                    }
                }
            }
        }
    };

    public TimepanelScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        this.mFormatRaw = getAttr(node, "format");
        this.mFormatExp = Expression.build(getVariables(), getAttr(node, "formatExp"));
        this.mSpace = (int) scale((double) getAttrAsInt(node, "space", 0));
        this.mTimeZoneExp = Expression.build(getVariables(), getAttr(node, "timeZoneId"));
    }

    public void init() {
        super.init();
        setDateFormat();
        this.mPreTime = null;
        createBitmap();
        updateTime(true);
    }

    public void finish() {
        this.mPreTime = null;
        this.mLoadResourceFailed = false;
        getContext().getHandler().removeCallbacks(this.mUpdateTimeRunnable);
        super.finish();
    }

    public void pause() {
    }

    public void resume() {
        this.mCalendar = Calendar.getInstance();
        this.mLocalizedZero = DecimalFormatSymbols.getInstance().getZeroDigit();
        setDateFormat();
        updateTime(true);
    }

    /* Access modifiers changed, original: protected */
    public void doTick(long currentTime) {
        super.doTick(currentTime);
        long minute = System.currentTimeMillis() / 60000;
        String src = getSrc();
        String format = getFormat();
        if (minute != this.mPreMinute || !TextUtils.equals(src, this.mOldSrc) || !TextUtils.equals(format, this.mOldFormat)) {
            updateTime(true);
            this.mPreMinute = minute;
            this.mOldSrc = src;
            this.mOldFormat = format;
        }
    }

    /* Access modifiers changed, original: protected */
    public int getBitmapWidth() {
        return this.mBmpWidth;
    }

    private void updateTime(boolean forceUpdate) {
        getContext().getHandler().removeCallbacks(this.mUpdateTimeRunnable);
        this.mForceUpdate = forceUpdate;
        postInMainThread(this.mUpdateTimeRunnable);
    }

    private void createBitmap() {
        String digits = "0123456789:";
        int maxWidth = 0;
        int density = 0;
        for (int i = 0; i < digits.length(); i++) {
            Bitmap digitBmp = getDigitBmp(digits.charAt(i));
            if (digitBmp == null) {
                this.mLoadResourceFailed = true;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to load digit bitmap: ");
                stringBuilder.append(digits.charAt(i));
                Log.e(LOG_TAG, stringBuilder.toString());
                return;
            }
            if (maxWidth < digitBmp.getWidth()) {
                maxWidth = digitBmp.getWidth();
            }
            if (this.mBmpHeight < digitBmp.getHeight()) {
                this.mBmpHeight = digitBmp.getHeight();
            }
            if (density == 0) {
                density = digitBmp.getDensity();
            }
        }
        Bitmap bmp = Bitmap.createBitmap((maxWidth * 5) + (this.mSpace * 4), this.mBmpHeight, Config.ARGB_8888);
        bmp.setDensity(density);
        this.mBitmap.setBitmap(bmp);
        setActualHeight(descale((double) this.mBmpHeight));
    }

    private Bitmap getDigitBmp(char c) {
        String suffix;
        String src = getSrc();
        if (TextUtils.isEmpty(src)) {
            src = "time.png";
        }
        if (c == ':') {
            suffix = "dot";
        } else {
            char c2 = this.mLocalizedZero;
            suffix = (c < c2 || c > c2 + 9) ? c : (char) ((c - c2) + 48);
            suffix = String.valueOf(suffix);
        }
        return getContext().mResourceManager.getBitmap(Utils.addFileNameSuffix(src, suffix));
    }

    private String getFormat() {
        Expression expression = this.mFormatExp;
        if (expression != null) {
            return expression.evaluateStr();
        }
        return this.mFormat;
    }

    private void setDateFormat() {
        if (TextUtils.isEmpty(this.mFormatRaw) && this.mFormatExp == null) {
            this.mFormat = DateFormat.is24HourFormat(getContext().mContext) ? M24 : M12;
        } else {
            this.mFormat = this.mFormatRaw;
        }
    }
}
