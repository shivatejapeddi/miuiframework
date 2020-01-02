package android.view.textclassifier;

import android.content.pm.ResolveInfo;
import android.view.textclassifier.intent.LabeledIntent;
import android.view.textclassifier.intent.LabeledIntent.TitleChooser;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TextClassifierImpl$naj1VfHYH1Qfut8yLHu8DlsggQE implements TitleChooser {
    public static final /* synthetic */ -$$Lambda$TextClassifierImpl$naj1VfHYH1Qfut8yLHu8DlsggQE INSTANCE = new -$$Lambda$TextClassifierImpl$naj1VfHYH1Qfut8yLHu8DlsggQE();

    private /* synthetic */ -$$Lambda$TextClassifierImpl$naj1VfHYH1Qfut8yLHu8DlsggQE() {
    }

    public final CharSequence chooseTitle(LabeledIntent labeledIntent, ResolveInfo resolveInfo) {
        return labeledIntent.titleWithoutEntity;
    }
}
