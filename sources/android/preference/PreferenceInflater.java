package android.preference;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@Deprecated
class PreferenceInflater extends GenericInflater<Preference, PreferenceGroup> {
    private static final String EXTRA_TAG_NAME = "extra";
    private static final String INTENT_TAG_NAME = "intent";
    private static final String TAG = "PreferenceInflater";
    private PreferenceManager mPreferenceManager;

    public PreferenceInflater(Context context, PreferenceManager preferenceManager) {
        super(context);
        init(preferenceManager);
    }

    PreferenceInflater(GenericInflater<Preference, PreferenceGroup> original, PreferenceManager preferenceManager, Context newContext) {
        super(original, newContext);
        init(preferenceManager);
    }

    public GenericInflater<Preference, PreferenceGroup> cloneInContext(Context newContext) {
        return new PreferenceInflater(this, this.mPreferenceManager, newContext);
    }

    private void init(PreferenceManager preferenceManager) {
        this.mPreferenceManager = preferenceManager;
        setDefaultPackage("android.preference.");
    }

    /* Access modifiers changed, original: protected */
    public boolean onCreateCustomFromTag(XmlPullParser parser, Preference parentPreference, AttributeSet attrs) throws XmlPullParserException {
        String tag = parser.getName();
        String str = "Error parsing preference";
        String intent;
        if (tag.equals("intent")) {
            try {
                str = Intent.parseIntent(getContext().getResources(), parser, attrs);
                intent = str;
                if (intent != null) {
                    parentPreference.setIntent(intent);
                }
                return true;
            } catch (IOException e) {
                XmlPullParserException ex = new XmlPullParserException(str);
                ex.initCause(e);
                throw ex;
            }
        }
        intent = EXTRA_TAG_NAME;
        if (!tag.equals(intent)) {
            return false;
        }
        getContext().getResources().parseBundleExtra(intent, attrs, parentPreference.getExtras());
        try {
            XmlUtils.skipCurrentTag(parser);
            return true;
        } catch (IOException e2) {
            XmlPullParserException ex2 = new XmlPullParserException(str);
            ex2.initCause(e2);
            throw ex2;
        }
    }

    /* Access modifiers changed, original: protected */
    public PreferenceGroup onMergeRoots(PreferenceGroup givenRoot, boolean attachToGivenRoot, PreferenceGroup xmlRoot) {
        if (givenRoot != null) {
            return givenRoot;
        }
        xmlRoot.onAttachedToHierarchy(this.mPreferenceManager);
        return xmlRoot;
    }
}
