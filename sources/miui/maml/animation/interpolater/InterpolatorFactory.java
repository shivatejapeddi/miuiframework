package miui.maml.animation.interpolater;

import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Interpolator;

public class InterpolatorFactory {
    public static final String LOG_TAG = "InterpolatorFactory";

    public static Interpolator create(String easeType) {
        if (TextUtils.isEmpty(easeType)) {
            return null;
        }
        String params;
        String firstParamStr;
        int leftBracket = easeType.indexOf(40);
        int rightBracket = easeType.indexOf(41);
        float firstParam = 0.0f;
        float secondParam = 0.0f;
        boolean hasFirstParam = false;
        boolean hasSecondParam = false;
        if (!(leftBracket == -1 || rightBracket == -1)) {
            hasFirstParam = true;
            params = easeType.substring(leftBracket + 1, rightBracket);
            firstParamStr = params;
            String secondParamStr = "";
            int comma = params.indexOf(",");
            if (comma != -1) {
                hasSecondParam = true;
                firstParamStr = params.substring(0, comma);
                secondParamStr = params.substring(comma + 1);
            }
            try {
                firstParam = Float.parseFloat(firstParamStr);
                if (hasSecondParam) {
                    secondParam = Float.parseFloat(secondParamStr);
                }
            } catch (NumberFormatException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("parse error:");
                stringBuilder.append(params);
                Log.d(LOG_TAG, stringBuilder.toString());
            }
        }
        String str = "BackEaseIn";
        if (str.equalsIgnoreCase(easeType)) {
            return new BackEaseInInterpolater();
        }
        params = "BackEaseOut";
        if (params.equalsIgnoreCase(easeType)) {
            return new BackEaseOutInterpolater();
        }
        firstParamStr = "BackEaseInOut";
        if (firstParamStr.equalsIgnoreCase(easeType)) {
            return new BackEaseInOutInterpolater();
        }
        if (easeType.startsWith(str) && hasFirstParam) {
            return new BackEaseInInterpolater(firstParam);
        }
        if (easeType.startsWith(params) && hasFirstParam) {
            return new BackEaseOutInterpolater(firstParam);
        }
        if (easeType.startsWith(firstParamStr) && hasFirstParam) {
            return new BackEaseInOutInterpolater(firstParam);
        }
        if ("BounceEaseIn".equalsIgnoreCase(easeType)) {
            return new BounceEaseInInterpolater();
        }
        if ("BounceEaseOut".equalsIgnoreCase(easeType)) {
            return new BounceEaseOutInterpolater();
        }
        if ("BounceEaseInOut".equalsIgnoreCase(easeType)) {
            return new BounceEaseInOutInterpolater();
        }
        if ("CircEaseIn".equalsIgnoreCase(easeType)) {
            return new CircEaseInInterpolater();
        }
        if ("CircEaseOut".equalsIgnoreCase(easeType)) {
            return new CircEaseOutInterpolater();
        }
        if ("CircEaseInOut".equalsIgnoreCase(easeType)) {
            return new CircEaseInOutInterpolater();
        }
        if ("CubicEaseIn".equalsIgnoreCase(easeType)) {
            return new CubicEaseInInterpolater();
        }
        if ("CubicEaseOut".equalsIgnoreCase(easeType)) {
            return new CubicEaseOutInterpolater();
        }
        if ("CubicEaseInOut".equalsIgnoreCase(easeType)) {
            return new CubicEaseInOutInterpolater();
        }
        str = "ElasticEaseIn";
        if (str.equalsIgnoreCase(easeType)) {
            return new ElasticEaseInInterpolater();
        }
        params = "ElasticEaseOut";
        if (params.equalsIgnoreCase(easeType)) {
            return new ElasticEaseOutInterpolater();
        }
        firstParamStr = "ElasticEaseInOut";
        if (firstParamStr.equalsIgnoreCase(easeType)) {
            return new ElasticEaseInOutInterpolater();
        }
        if (easeType.startsWith(str) && hasSecondParam) {
            return new ElasticEaseInInterpolater(firstParam, secondParam);
        }
        if (easeType.startsWith(params) && hasSecondParam) {
            return new ElasticEaseOutInterpolater(firstParam, secondParam);
        }
        if (easeType.startsWith(firstParamStr) && hasSecondParam) {
            return new ElasticEaseInOutInterpolater(firstParam, secondParam);
        }
        if ("ExpoEaseIn".equalsIgnoreCase(easeType)) {
            return new ExpoEaseInInterpolater();
        }
        if ("ExpoEaseOut".equalsIgnoreCase(easeType)) {
            return new ExpoEaseOutInterpolater();
        }
        if ("ExpoEaseInOut".equalsIgnoreCase(easeType)) {
            return new ExpoEaseInOutInterpolater();
        }
        if ("QuadEaseIn".equalsIgnoreCase(easeType)) {
            return new QuadEaseInInterpolater();
        }
        if ("QuadEaseOut".equalsIgnoreCase(easeType)) {
            return new QuadEaseOutInterpolater();
        }
        if ("QuadEaseInOut".equalsIgnoreCase(easeType)) {
            return new QuadEaseInOutInterpolater();
        }
        if ("QuartEaseIn".equalsIgnoreCase(easeType)) {
            return new QuartEaseInInterpolater();
        }
        if ("QuartEaseOut".equalsIgnoreCase(easeType)) {
            return new QuartEaseOutInterpolater();
        }
        if ("QuartEaseInOut".equalsIgnoreCase(easeType)) {
            return new QuartEaseInOutInterpolater();
        }
        if ("QuintEaseIn".equalsIgnoreCase(easeType)) {
            return new QuintEaseInInterpolater();
        }
        if ("QuintEaseOut".equalsIgnoreCase(easeType)) {
            return new QuintEaseOutInterpolater();
        }
        if ("QuintEaseInOut".equalsIgnoreCase(easeType)) {
            return new QuintEaseInOutInterpolater();
        }
        if ("SineEaseIn".equalsIgnoreCase(easeType)) {
            return new SineEaseInInterpolater();
        }
        if ("SineEaseOut".equalsIgnoreCase(easeType)) {
            return new SineEaseOutInterpolater();
        }
        if ("SineEaseInOut".equalsIgnoreCase(easeType)) {
            return new SineEaseInOutInterpolater();
        }
        if ("Linear".equalsIgnoreCase(easeType)) {
            return new LinearInterpolater();
        }
        str = "PhysicBased";
        if (str.equalsIgnoreCase(easeType)) {
            return new PhysicBasedInterpolator();
        }
        if (easeType.startsWith(str) && hasSecondParam) {
            return new PhysicBasedInterpolator(firstParam, secondParam);
        }
        return null;
    }
}
