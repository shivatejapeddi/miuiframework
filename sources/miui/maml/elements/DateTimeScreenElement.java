package miui.maml.elements;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.IncidentManager;
import android.text.TextUtils;
import android.util.Log;
import java.util.TimeZone;
import miui.date.Calendar;
import miui.maml.ScreenElementRoot;
import miui.maml.data.Expression;
import miui.maml.util.TextFormatter;
import org.w3c.dom.Element;

public class DateTimeScreenElement extends TextScreenElement {
    public static final String TAG_NAME = "DateTime";
    private DateFormatter mDateFormatter;
    private DateFormatter mDescriptionDateFormatter;

    class DateFormatter {
        private Calendar mCalendar;
        private int mCurDay;
        private String mLunarDate;
        private String mOldFormat;
        private long mPreValue;
        private String mText;
        private TextFormatter mTextFormatter;
        private Expression mTimeZoneExp;
        private Expression mValueExp;

        public DateFormatter(DateTimeScreenElement this$0, TextFormatter formatter, Expression valueExp) {
            this(formatter, valueExp, null);
        }

        public DateFormatter(TextFormatter formatter, Expression valueExp, Expression timeZoneExp) {
            this.mCalendar = new Calendar();
            this.mCurDay = -1;
            this.mTextFormatter = formatter;
            this.mValueExp = valueExp;
            this.mTimeZoneExp = timeZoneExp;
        }

        public String getText() {
            String format = this.mTextFormatter;
            String str = "";
            if (format == null) {
                return str;
            }
            format = format.getFormat();
            if (format == null) {
                return str;
            }
            Expression expression = this.mValueExp;
            long ms = expression != null ? (long) DateTimeScreenElement.this.evaluate(expression) : System.currentTimeMillis();
            if (TextUtils.equals(this.mOldFormat, format) && Math.abs(ms - this.mPreValue) < 200) {
                return this.mText;
            }
            this.mOldFormat = format;
            this.mCalendar.setTimeInMillis(ms);
            String timeZoneId = this.mTimeZoneExp;
            if (timeZoneId != null) {
                timeZoneId = timeZoneId.evaluateStr();
                if (!TextUtils.isEmpty(timeZoneId)) {
                    this.mCalendar.setTimeZone(TimeZone.getTimeZone(timeZoneId));
                }
            }
            timeZoneId = "NNNN";
            if (format.contains(timeZoneId)) {
                if (this.mCalendar.get(9) != this.mCurDay) {
                    this.mLunarDate = this.mCalendar.format("N月e");
                    String term = this.mCalendar.format(IncidentManager.URI_PARAM_TIMESTAMP);
                    if (term != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(this.mLunarDate);
                        stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        stringBuilder.append(term);
                        this.mLunarDate = stringBuilder.toString();
                    }
                    this.mCurDay = this.mCalendar.get(9);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("get lunar date:");
                    stringBuilder2.append(this.mLunarDate);
                    Log.i("DateTimeScreenElement", stringBuilder2.toString());
                }
                format = format.replace(timeZoneId, this.mLunarDate);
            }
            this.mText = this.mCalendar.format(format);
            this.mPreValue = ms;
            return this.mText;
        }

        public void resetCalendar() {
            this.mCalendar = new Calendar();
        }
    }

    public DateTimeScreenElement(Element node, ScreenElementRoot root) {
        super(node, root);
        Expression valueExp = Expression.build(getVariables(), node.getAttribute("value"));
        Expression timezoneExp = Expression.build(getVariables(), node.getAttribute("timeZoneId"));
        this.mDateFormatter = new DateFormatter(this.mFormatter, valueExp, timezoneExp);
        if (!TextUtils.isEmpty(node.getAttribute("contentDescriptionFormat"))) {
            this.mHasContentDescription = true;
            this.mDescriptionDateFormatter = new DateFormatter(TextFormatter.fromElement(getVariables(), node, null, "contentDescriptionFormat", null, null, null), valueExp, timezoneExp);
        } else if (!TextUtils.isEmpty(node.getAttribute("contentDescriptionFormatExp"))) {
            this.mHasContentDescription = true;
            this.mDescriptionDateFormatter = new DateFormatter(TextFormatter.fromElement(getVariables(), node, null, null, null, null, "contentDescriptionFormatExp"), valueExp, timezoneExp);
        }
        if (this.mHasContentDescription) {
            this.mRoot.addAccessibleElements(this);
        }
    }

    public void resume() {
        super.resume();
        this.mDateFormatter.resetCalendar();
        DateFormatter dateFormatter = this.mDescriptionDateFormatter;
        if (dateFormatter != null) {
            dateFormatter.resetCalendar();
        }
    }

    /* Access modifiers changed, original: protected */
    public String getText() {
        return this.mDateFormatter.getText();
    }

    public String getContentDescription() {
        DateFormatter dateFormatter = this.mDescriptionDateFormatter;
        return dateFormatter != null ? dateFormatter.getText() : super.getContentDescription();
    }
}
