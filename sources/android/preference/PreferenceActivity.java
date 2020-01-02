package android.preference;

import android.animation.LayoutTransition;
import android.annotation.UnsupportedAppUsage;
import android.app.Fragment;
import android.app.FragmentBreadCrumbs;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.preference.PreferenceFragment.OnPreferenceStartFragmentCallback;
import android.preference.PreferenceManager.OnPreferenceTreeClickListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public abstract class PreferenceActivity extends ListActivity implements OnPreferenceTreeClickListener, OnPreferenceStartFragmentCallback {
    private static final String BACK_STACK_PREFS = ":android:prefs";
    private static final String CUR_HEADER_TAG = ":android:cur_header";
    public static final String EXTRA_NO_HEADERS = ":android:no_headers";
    private static final String EXTRA_PREFS_SET_BACK_TEXT = "extra_prefs_set_back_text";
    private static final String EXTRA_PREFS_SET_NEXT_TEXT = "extra_prefs_set_next_text";
    private static final String EXTRA_PREFS_SHOW_BUTTON_BAR = "extra_prefs_show_button_bar";
    private static final String EXTRA_PREFS_SHOW_SKIP = "extra_prefs_show_skip";
    public static final String EXTRA_SHOW_FRAGMENT = ":android:show_fragment";
    public static final String EXTRA_SHOW_FRAGMENT_ARGUMENTS = ":android:show_fragment_args";
    public static final String EXTRA_SHOW_FRAGMENT_SHORT_TITLE = ":android:show_fragment_short_title";
    public static final String EXTRA_SHOW_FRAGMENT_TITLE = ":android:show_fragment_title";
    private static final int FIRST_REQUEST_CODE = 100;
    private static final String HEADERS_TAG = ":android:headers";
    public static final long HEADER_ID_UNDEFINED = -1;
    private static final int MSG_BIND_PREFERENCES = 1;
    private static final int MSG_BUILD_HEADERS = 2;
    private static final String PREFERENCES_TAG = ":android:preferences";
    private static final String TAG = "PreferenceActivity";
    private CharSequence mActivityTitle;
    private Header mCurHeader;
    private FragmentBreadCrumbs mFragmentBreadCrumbs;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 1) {
                PreferenceActivity.this.bindPreferences();
            } else if (i == 2) {
                ArrayList<Header> oldHeaders = new ArrayList(PreferenceActivity.this.mHeaders);
                PreferenceActivity.this.mHeaders.clear();
                PreferenceActivity preferenceActivity = PreferenceActivity.this;
                preferenceActivity.onBuildHeaders(preferenceActivity.mHeaders);
                if (PreferenceActivity.this.mAdapter instanceof BaseAdapter) {
                    ((BaseAdapter) PreferenceActivity.this.mAdapter).notifyDataSetChanged();
                }
                Header header = PreferenceActivity.this.onGetNewHeader();
                Header mappedHeader;
                if (header != null && header.fragment != null) {
                    mappedHeader = PreferenceActivity.this.findBestMatchingHeader(header, oldHeaders);
                    if (mappedHeader == null || PreferenceActivity.this.mCurHeader != mappedHeader) {
                        PreferenceActivity.this.switchToHeader(header);
                    }
                } else if (PreferenceActivity.this.mCurHeader != null) {
                    PreferenceActivity preferenceActivity2 = PreferenceActivity.this;
                    mappedHeader = preferenceActivity2.findBestMatchingHeader(preferenceActivity2.mCurHeader, PreferenceActivity.this.mHeaders);
                    if (mappedHeader != null) {
                        PreferenceActivity.this.setSelectedHeader(mappedHeader);
                    }
                }
            }
        }
    };
    private final ArrayList<Header> mHeaders = new ArrayList();
    private ViewGroup mHeadersContainer;
    private FrameLayout mListFooter;
    private Button mNextButton;
    private int mPreferenceHeaderItemResId = 0;
    private boolean mPreferenceHeaderRemoveEmptyIcon = false;
    @UnsupportedAppUsage
    private PreferenceManager mPreferenceManager;
    @UnsupportedAppUsage
    private ViewGroup mPrefsContainer;
    private Bundle mSavedInstanceState;
    private boolean mSinglePane;

    @Deprecated
    public static final class Header implements Parcelable {
        public static final Creator<Header> CREATOR = new Creator<Header>() {
            public Header createFromParcel(Parcel source) {
                return new Header(source);
            }

            public Header[] newArray(int size) {
                return new Header[size];
            }
        };
        public CharSequence breadCrumbShortTitle;
        public int breadCrumbShortTitleRes;
        public CharSequence breadCrumbTitle;
        public int breadCrumbTitleRes;
        public Bundle extras;
        public String fragment;
        public Bundle fragmentArguments;
        public int iconRes;
        public long id = -1;
        public Intent intent;
        public CharSequence summary;
        public int summaryRes;
        public CharSequence title;
        public int titleRes;

        public CharSequence getTitle(Resources res) {
            int i = this.titleRes;
            if (i != 0) {
                return res.getText(i);
            }
            return this.title;
        }

        public CharSequence getSummary(Resources res) {
            int i = this.summaryRes;
            if (i != 0) {
                return res.getText(i);
            }
            return this.summary;
        }

        public CharSequence getBreadCrumbTitle(Resources res) {
            int i = this.breadCrumbTitleRes;
            if (i != 0) {
                return res.getText(i);
            }
            return this.breadCrumbTitle;
        }

        public CharSequence getBreadCrumbShortTitle(Resources res) {
            int i = this.breadCrumbShortTitleRes;
            if (i != 0) {
                return res.getText(i);
            }
            return this.breadCrumbShortTitle;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.id);
            dest.writeInt(this.titleRes);
            TextUtils.writeToParcel(this.title, dest, flags);
            dest.writeInt(this.summaryRes);
            TextUtils.writeToParcel(this.summary, dest, flags);
            dest.writeInt(this.breadCrumbTitleRes);
            TextUtils.writeToParcel(this.breadCrumbTitle, dest, flags);
            dest.writeInt(this.breadCrumbShortTitleRes);
            TextUtils.writeToParcel(this.breadCrumbShortTitle, dest, flags);
            dest.writeInt(this.iconRes);
            dest.writeString(this.fragment);
            dest.writeBundle(this.fragmentArguments);
            if (this.intent != null) {
                dest.writeInt(1);
                this.intent.writeToParcel(dest, flags);
            } else {
                dest.writeInt(0);
            }
            dest.writeBundle(this.extras);
        }

        public void readFromParcel(Parcel in) {
            this.id = in.readLong();
            this.titleRes = in.readInt();
            this.title = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.summaryRes = in.readInt();
            this.summary = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.breadCrumbTitleRes = in.readInt();
            this.breadCrumbTitle = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.breadCrumbShortTitleRes = in.readInt();
            this.breadCrumbShortTitle = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.iconRes = in.readInt();
            this.fragment = in.readString();
            this.fragmentArguments = in.readBundle();
            if (in.readInt() != 0) {
                this.intent = (Intent) Intent.CREATOR.createFromParcel(in);
            }
            this.extras = in.readBundle();
        }

        Header(Parcel in) {
            readFromParcel(in);
        }
    }

    private static class HeaderAdapter extends ArrayAdapter<Header> {
        private LayoutInflater mInflater;
        private int mLayoutResId;
        private boolean mRemoveIconIfEmpty;

        private static class HeaderViewHolder {
            ImageView icon;
            TextView summary;
            TextView title;

            private HeaderViewHolder() {
            }

            /* synthetic */ HeaderViewHolder(AnonymousClass1 x0) {
                this();
            }
        }

        public HeaderAdapter(Context context, List<Header> objects, int layoutResId, boolean removeIconBehavior) {
            super(context, 0, (List) objects);
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mLayoutResId = layoutResId;
            this.mRemoveIconIfEmpty = removeIconBehavior;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            HeaderViewHolder holder;
            if (convertView == null) {
                view = this.mInflater.inflate(this.mLayoutResId, parent, false);
                holder = new HeaderViewHolder();
                holder.icon = (ImageView) view.findViewById(16908294);
                holder.title = (TextView) view.findViewById(16908310);
                holder.summary = (TextView) view.findViewById(16908304);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (HeaderViewHolder) view.getTag();
            }
            Header header = (Header) getItem(position);
            if (!this.mRemoveIconIfEmpty) {
                holder.icon.setImageResource(header.iconRes);
            } else if (header.iconRes == 0) {
                holder.icon.setVisibility(8);
            } else {
                holder.icon.setVisibility(0);
                holder.icon.setImageResource(header.iconRes);
            }
            holder.title.setText(header.getTitle(getContext().getResources()));
            CharSequence summary = header.getSummary(getContext().getResources());
            if (TextUtils.isEmpty(summary)) {
                holder.summary.setVisibility(8);
            } else {
                holder.summary.setVisibility(0);
                holder.summary.setText(summary);
            }
            return view;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        onBackPressed();
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = savedInstanceState;
        super.onCreate(savedInstanceState);
        TypedArray sa = obtainStyledAttributes(null, R.styleable.PreferenceActivity, R.attr.preferenceActivityStyle, 0);
        int layoutResId = sa.getResourceId(0, R.layout.preference_list_content);
        this.mPreferenceHeaderItemResId = sa.getResourceId(1, R.layout.preference_header_item);
        this.mPreferenceHeaderRemoveEmptyIcon = sa.getBoolean(2, false);
        sa.recycle();
        setContentView(layoutResId);
        this.mListFooter = (FrameLayout) findViewById(R.id.list_footer);
        this.mPrefsContainer = (ViewGroup) findViewById(R.id.prefs_frame);
        this.mHeadersContainer = (ViewGroup) findViewById(R.id.headers);
        boolean z = onIsHidingHeaders() || !onIsMultiPane();
        this.mSinglePane = z;
        String initialFragment = getIntent().getStringExtra(EXTRA_SHOW_FRAGMENT);
        Bundle initialArguments = getIntent().getBundleExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS);
        int initialTitle = getIntent().getIntExtra(EXTRA_SHOW_FRAGMENT_TITLE, 0);
        int initialShortTitle = getIntent().getIntExtra(EXTRA_SHOW_FRAGMENT_SHORT_TITLE, 0);
        this.mActivityTitle = getTitle();
        if (bundle != null) {
            ArrayList<Header> headers = bundle.getParcelableArrayList(HEADERS_TAG);
            if (headers != null) {
                this.mHeaders.addAll(headers);
                int curHeader = bundle.getInt(CUR_HEADER_TAG, -1);
                if (curHeader >= 0 && curHeader < this.mHeaders.size()) {
                    setSelectedHeader((Header) this.mHeaders.get(curHeader));
                } else if (!this.mSinglePane && initialFragment == null) {
                    switchToHeader(onGetInitialHeader());
                }
            } else {
                showBreadCrumbs(getTitle(), null);
            }
        } else {
            if (!onIsHidingHeaders()) {
                onBuildHeaders(this.mHeaders);
            }
            if (initialFragment != null) {
                switchToHeader(initialFragment, initialArguments);
            } else if (!this.mSinglePane && this.mHeaders.size() > 0) {
                switchToHeader(onGetInitialHeader());
            }
        }
        if (this.mHeaders.size() > 0) {
            setListAdapter(new HeaderAdapter(this, this.mHeaders, this.mPreferenceHeaderItemResId, this.mPreferenceHeaderRemoveEmptyIcon));
            if (!this.mSinglePane) {
                getListView().setChoiceMode(1);
            }
        }
        if (!(!this.mSinglePane || initialFragment == null || initialTitle == 0)) {
            showBreadCrumbs(getText(initialTitle), initialShortTitle != 0 ? getText(initialShortTitle) : null);
        }
        if (this.mHeaders.size() == 0 && initialFragment == null) {
            setContentView((int) R.layout.preference_list_content_single);
            this.mListFooter = (FrameLayout) findViewById(R.id.list_footer);
            this.mPrefsContainer = (ViewGroup) findViewById(R.id.prefs);
            this.mPreferenceManager = new PreferenceManager(this, 100);
            this.mPreferenceManager.setOnPreferenceTreeClickListener(this);
            this.mHeadersContainer = null;
        } else if (this.mSinglePane) {
            if (initialFragment == null && this.mCurHeader == null) {
                this.mPrefsContainer.setVisibility(8);
            } else {
                this.mHeadersContainer.setVisibility(8);
            }
            ((ViewGroup) findViewById(R.id.prefs_container)).setLayoutTransition(new LayoutTransition());
        } else if (this.mHeaders.size() > 0) {
            Header header = this.mCurHeader;
            if (header != null) {
                setSelectedHeader(header);
            }
        }
        Intent intent = getIntent();
        if (intent.getBooleanExtra(EXTRA_PREFS_SHOW_BUTTON_BAR, false)) {
            findViewById(R.id.button_bar).setVisibility(0);
            Button backButton = (Button) findViewById(R.id.back_button);
            backButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    PreferenceActivity.this.setResult(0);
                    PreferenceActivity.this.finish();
                }
            });
            Button skipButton = (Button) findViewById(R.id.skip_button);
            skipButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    PreferenceActivity.this.setResult(-1);
                    PreferenceActivity.this.finish();
                }
            });
            this.mNextButton = (Button) findViewById(R.id.next_button);
            this.mNextButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    PreferenceActivity.this.setResult(-1);
                    PreferenceActivity.this.finish();
                }
            });
            String buttonText = EXTRA_PREFS_SET_NEXT_TEXT;
            if (intent.hasExtra(buttonText)) {
                buttonText = intent.getStringExtra(buttonText);
                if (TextUtils.isEmpty(buttonText)) {
                    this.mNextButton.setVisibility(8);
                } else {
                    this.mNextButton.setText((CharSequence) buttonText);
                }
            }
            buttonText = EXTRA_PREFS_SET_BACK_TEXT;
            if (intent.hasExtra(buttonText)) {
                buttonText = intent.getStringExtra(buttonText);
                if (TextUtils.isEmpty(buttonText)) {
                    backButton.setVisibility(8);
                } else {
                    backButton.setText((CharSequence) buttonText);
                }
            }
            if (intent.getBooleanExtra(EXTRA_PREFS_SHOW_SKIP, false)) {
                skipButton.setVisibility(0);
            }
        }
    }

    public void onBackPressed() {
        if (this.mCurHeader != null && this.mSinglePane && getFragmentManager().getBackStackEntryCount() == 0 && getIntent().getStringExtra(EXTRA_SHOW_FRAGMENT) == null) {
            this.mCurHeader = null;
            this.mPrefsContainer.setVisibility(8);
            this.mHeadersContainer.setVisibility(0);
            CharSequence charSequence = this.mActivityTitle;
            if (charSequence != null) {
                showBreadCrumbs(charSequence, null);
            }
            getListView().clearChoices();
            return;
        }
        super.onBackPressed();
    }

    public boolean hasHeaders() {
        ViewGroup viewGroup = this.mHeadersContainer;
        return viewGroup != null && viewGroup.getVisibility() == 0;
    }

    @UnsupportedAppUsage
    public List<Header> getHeaders() {
        return this.mHeaders;
    }

    public boolean isMultiPane() {
        return this.mSinglePane ^ 1;
    }

    public boolean onIsMultiPane() {
        return getResources().getBoolean(R.bool.preferences_prefer_dual_pane);
    }

    public boolean onIsHidingHeaders() {
        return getIntent().getBooleanExtra(EXTRA_NO_HEADERS, false);
    }

    public Header onGetInitialHeader() {
        for (int i = 0; i < this.mHeaders.size(); i++) {
            Header h = (Header) this.mHeaders.get(i);
            if (h.fragment != null) {
                return h;
            }
        }
        throw new IllegalStateException("Must have at least one header with a fragment");
    }

    public Header onGetNewHeader() {
        return null;
    }

    public void onBuildHeaders(List<Header> list) {
    }

    public void invalidateHeaders() {
        if (!this.mHandler.hasMessages(2)) {
            this.mHandler.sendEmptyMessage(2);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:135:0x01e4  */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x01e4  */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x01e4  */
    public void loadHeadersFromResource(int r20, java.util.List<android.preference.PreferenceActivity.Header> r21) {
        /*
        r19 = this;
        r0 = "extra";
        r1 = "Error parsing headers";
        r2 = 0;
        r3 = r19.getResources();	 Catch:{ XmlPullParserException -> 0x01d4, IOException -> 0x01c7, all -> 0x01bf }
        r4 = r20;
        r3 = r3.getXml(r4);	 Catch:{ XmlPullParserException -> 0x01bb, IOException -> 0x01b7, all -> 0x01b3 }
        r2 = r3;
        r3 = android.util.Xml.asAttributeSet(r2);	 Catch:{ XmlPullParserException -> 0x01bb, IOException -> 0x01b7, all -> 0x01b3 }
    L_0x0014:
        r5 = r2.next();	 Catch:{ XmlPullParserException -> 0x01bb, IOException -> 0x01b7, all -> 0x01b3 }
        r6 = r5;
        r7 = 2;
        r8 = 1;
        if (r5 == r8) goto L_0x0020;
    L_0x001d:
        if (r6 == r7) goto L_0x0020;
    L_0x001f:
        goto L_0x0014;
    L_0x0020:
        r5 = r2.getName();	 Catch:{ XmlPullParserException -> 0x01bb, IOException -> 0x01b7, all -> 0x01b3 }
        r9 = "preference-headers";
        r9 = r9.equals(r5);	 Catch:{ XmlPullParserException -> 0x01bb, IOException -> 0x01b7, all -> 0x01b3 }
        if (r9 == 0) goto L_0x0188;
    L_0x002d:
        r9 = 0;
        r10 = r2.getDepth();	 Catch:{ XmlPullParserException -> 0x01bb, IOException -> 0x01b7, all -> 0x01b3 }
    L_0x0032:
        r11 = r2.next();	 Catch:{ XmlPullParserException -> 0x01bb, IOException -> 0x01b7, all -> 0x01b3 }
        r6 = r11;
        if (r11 == r8) goto L_0x017e;
    L_0x0039:
        r11 = 3;
        if (r6 != r11) goto L_0x0049;
    L_0x003c:
        r12 = r2.getDepth();	 Catch:{ XmlPullParserException -> 0x01bb, IOException -> 0x01b7, all -> 0x01b3 }
        if (r12 <= r10) goto L_0x0043;
    L_0x0042:
        goto L_0x0049;
    L_0x0043:
        r15 = r19;
        r7 = r21;
        goto L_0x0184;
    L_0x0049:
        if (r6 == r11) goto L_0x0172;
    L_0x004b:
        r12 = 4;
        if (r6 != r12) goto L_0x0056;
    L_0x004e:
        r15 = r19;
        r7 = r21;
        r18 = r9;
        goto L_0x0178;
    L_0x0056:
        r13 = r2.getName();	 Catch:{ XmlPullParserException -> 0x01bb, IOException -> 0x01b7, all -> 0x01b3 }
        r5 = r13;
        r13 = "header";
        r13 = r13.equals(r5);	 Catch:{ XmlPullParserException -> 0x01bb, IOException -> 0x01b7, all -> 0x01b3 }
        if (r13 == 0) goto L_0x0163;
    L_0x0064:
        r13 = new android.preference.PreferenceActivity$Header;	 Catch:{ XmlPullParserException -> 0x01bb, IOException -> 0x01b7, all -> 0x01b3 }
        r13.<init>();	 Catch:{ XmlPullParserException -> 0x01bb, IOException -> 0x01b7, all -> 0x01b3 }
        r14 = com.android.internal.R.styleable.PreferenceHeader;	 Catch:{ XmlPullParserException -> 0x01bb, IOException -> 0x01b7, all -> 0x01b3 }
        r15 = r19;
        r14 = r15.obtainStyledAttributes(r3, r14);	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r12 = -1;
        r12 = r14.getResourceId(r8, r12);	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r18 = r9;
        r8 = (long) r12;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r13.id = r8;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r8 = r14.peekValue(r7);	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        if (r8 == 0) goto L_0x0092;
    L_0x0081:
        r9 = r8.type;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        if (r9 != r11) goto L_0x0092;
    L_0x0085:
        r9 = r8.resourceId;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        if (r9 == 0) goto L_0x008e;
    L_0x0089:
        r9 = r8.resourceId;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r13.titleRes = r9;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        goto L_0x0092;
    L_0x008e:
        r9 = r8.string;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r13.title = r9;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
    L_0x0092:
        r9 = r14.peekValue(r11);	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r8 = r9;
        if (r8 == 0) goto L_0x00aa;
    L_0x0099:
        r9 = r8.type;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        if (r9 != r11) goto L_0x00aa;
    L_0x009d:
        r9 = r8.resourceId;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        if (r9 == 0) goto L_0x00a6;
    L_0x00a1:
        r9 = r8.resourceId;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r13.summaryRes = r9;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        goto L_0x00aa;
    L_0x00a6:
        r9 = r8.string;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r13.summary = r9;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
    L_0x00aa:
        r9 = 5;
        r9 = r14.peekValue(r9);	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r8 = r9;
        if (r8 == 0) goto L_0x00c3;
    L_0x00b2:
        r9 = r8.type;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        if (r9 != r11) goto L_0x00c3;
    L_0x00b6:
        r9 = r8.resourceId;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        if (r9 == 0) goto L_0x00bf;
    L_0x00ba:
        r9 = r8.resourceId;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r13.breadCrumbTitleRes = r9;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        goto L_0x00c3;
    L_0x00bf:
        r9 = r8.string;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r13.breadCrumbTitle = r9;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
    L_0x00c3:
        r9 = 6;
        r9 = r14.peekValue(r9);	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r8 = r9;
        if (r8 == 0) goto L_0x00dc;
    L_0x00cb:
        r9 = r8.type;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        if (r9 != r11) goto L_0x00dc;
    L_0x00cf:
        r9 = r8.resourceId;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        if (r9 == 0) goto L_0x00d8;
    L_0x00d3:
        r9 = r8.resourceId;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r13.breadCrumbShortTitleRes = r9;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        goto L_0x00dc;
    L_0x00d8:
        r9 = r8.string;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r13.breadCrumbShortTitle = r9;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
    L_0x00dc:
        r9 = 0;
        r9 = r14.getResourceId(r9, r9);	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r13.iconRes = r9;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r9 = 4;
        r12 = r14.getString(r9);	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r13.fragment = r12;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r14.recycle();	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        if (r18 != 0) goto L_0x00f5;
    L_0x00ef:
        r9 = new android.os.Bundle;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r9.<init>();	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        goto L_0x00f7;
    L_0x00f5:
        r9 = r18;
    L_0x00f7:
        r12 = r2.getDepth();	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
    L_0x00fb:
        r7 = r2.next();	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r6 = r7;
        r11 = 1;
        if (r7 == r11) goto L_0x0147;
    L_0x0103:
        r7 = 3;
        if (r6 != r7) goto L_0x010c;
    L_0x0106:
        r7 = r2.getDepth();	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        if (r7 <= r12) goto L_0x0147;
    L_0x010c:
        r7 = 3;
        if (r6 == r7) goto L_0x0144;
    L_0x010f:
        r7 = 4;
        if (r6 != r7) goto L_0x0113;
    L_0x0112:
        goto L_0x0144;
    L_0x0113:
        r16 = r2.getName();	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r17 = r16;
        r7 = r17;
        r17 = r7.equals(r0);	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        if (r17 == 0) goto L_0x012c;
    L_0x0121:
        r11 = r19.getResources();	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r11.parseBundleExtra(r0, r3, r9);	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        com.android.internal.util.XmlUtils.skipCurrentTag(r2);	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        goto L_0x0143;
    L_0x012c:
        r11 = "intent";
        r11 = r7.equals(r11);	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        if (r11 == 0) goto L_0x0140;
    L_0x0135:
        r11 = r19.getResources();	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r11 = android.content.Intent.parseIntent(r11, r2, r3);	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r13.intent = r11;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        goto L_0x0143;
    L_0x0140:
        com.android.internal.util.XmlUtils.skipCurrentTag(r2);	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
    L_0x0144:
        r7 = 2;
        r11 = 3;
        goto L_0x00fb;
    L_0x0147:
        r7 = r9.size();	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        if (r7 <= 0) goto L_0x0151;
    L_0x014d:
        r13.fragmentArguments = r9;	 Catch:{ XmlPullParserException -> 0x0160, IOException -> 0x015d, all -> 0x015a }
        r7 = 0;
        r9 = r7;
    L_0x0151:
        r7 = r21;
        r7.add(r13);	 Catch:{ XmlPullParserException -> 0x01b1, IOException -> 0x01af }
        r7 = 2;
        r8 = 1;
        goto L_0x0032;
    L_0x015a:
        r0 = move-exception;
        goto L_0x01c4;
    L_0x015d:
        r0 = move-exception;
        goto L_0x01cc;
    L_0x0160:
        r0 = move-exception;
        goto L_0x01d9;
    L_0x0163:
        r15 = r19;
        r7 = r21;
        r18 = r9;
        com.android.internal.util.XmlUtils.skipCurrentTag(r2);	 Catch:{ XmlPullParserException -> 0x01b1, IOException -> 0x01af }
        r9 = r18;
        r7 = 2;
        r8 = 1;
        goto L_0x0032;
    L_0x0172:
        r15 = r19;
        r7 = r21;
        r18 = r9;
    L_0x0178:
        r9 = r18;
        r7 = 2;
        r8 = 1;
        goto L_0x0032;
    L_0x017e:
        r15 = r19;
        r7 = r21;
        r18 = r9;
    L_0x0184:
        r2.close();
        return;
    L_0x0188:
        r15 = r19;
        r7 = r21;
        r0 = new java.lang.RuntimeException;	 Catch:{ XmlPullParserException -> 0x01b1, IOException -> 0x01af }
        r8 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x01b1, IOException -> 0x01af }
        r8.<init>();	 Catch:{ XmlPullParserException -> 0x01b1, IOException -> 0x01af }
        r9 = "XML document must start with <preference-headers> tag; found";
        r8.append(r9);	 Catch:{ XmlPullParserException -> 0x01b1, IOException -> 0x01af }
        r8.append(r5);	 Catch:{ XmlPullParserException -> 0x01b1, IOException -> 0x01af }
        r9 = " at ";
        r8.append(r9);	 Catch:{ XmlPullParserException -> 0x01b1, IOException -> 0x01af }
        r9 = r2.getPositionDescription();	 Catch:{ XmlPullParserException -> 0x01b1, IOException -> 0x01af }
        r8.append(r9);	 Catch:{ XmlPullParserException -> 0x01b1, IOException -> 0x01af }
        r8 = r8.toString();	 Catch:{ XmlPullParserException -> 0x01b1, IOException -> 0x01af }
        r0.<init>(r8);	 Catch:{ XmlPullParserException -> 0x01b1, IOException -> 0x01af }
        throw r0;	 Catch:{ XmlPullParserException -> 0x01b1, IOException -> 0x01af }
    L_0x01af:
        r0 = move-exception;
        goto L_0x01ce;
    L_0x01b1:
        r0 = move-exception;
        goto L_0x01db;
    L_0x01b3:
        r0 = move-exception;
        r15 = r19;
        goto L_0x01c4;
    L_0x01b7:
        r0 = move-exception;
        r15 = r19;
        goto L_0x01cc;
    L_0x01bb:
        r0 = move-exception;
        r15 = r19;
        goto L_0x01d9;
    L_0x01bf:
        r0 = move-exception;
        r15 = r19;
        r4 = r20;
    L_0x01c4:
        r7 = r21;
        goto L_0x01e2;
    L_0x01c7:
        r0 = move-exception;
        r15 = r19;
        r4 = r20;
    L_0x01cc:
        r7 = r21;
    L_0x01ce:
        r3 = new java.lang.RuntimeException;	 Catch:{ all -> 0x01e1 }
        r3.<init>(r1, r0);	 Catch:{ all -> 0x01e1 }
        throw r3;	 Catch:{ all -> 0x01e1 }
    L_0x01d4:
        r0 = move-exception;
        r15 = r19;
        r4 = r20;
    L_0x01d9:
        r7 = r21;
    L_0x01db:
        r3 = new java.lang.RuntimeException;	 Catch:{ all -> 0x01e1 }
        r3.<init>(r1, r0);	 Catch:{ all -> 0x01e1 }
        throw r3;	 Catch:{ all -> 0x01e1 }
    L_0x01e1:
        r0 = move-exception;
    L_0x01e2:
        if (r2 == 0) goto L_0x01e7;
    L_0x01e4:
        r2.close();
    L_0x01e7:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.preference.PreferenceActivity.loadHeadersFromResource(int, java.util.List):void");
    }

    /* Access modifiers changed, original: protected */
    public boolean isValidFragment(String fragmentName) {
        if (getApplicationInfo().targetSdkVersion < 19) {
            return true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Subclasses of PreferenceActivity must override isValidFragment(String) to verify that the Fragment class is valid! ");
        stringBuilder.append(getClass().getName());
        stringBuilder.append(" has not checked if fragment ");
        stringBuilder.append(fragmentName);
        stringBuilder.append(" is valid.");
        throw new RuntimeException(stringBuilder.toString());
    }

    public void setListFooter(View view) {
        this.mListFooter.removeAllViews();
        this.mListFooter.addView(view, (LayoutParams) new FrameLayout.LayoutParams(-1, -2));
    }

    /* Access modifiers changed, original: protected */
    public void onStop() {
        super.onStop();
        PreferenceManager preferenceManager = this.mPreferenceManager;
        if (preferenceManager != null) {
            preferenceManager.dispatchActivityStop();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        super.onDestroy();
        PreferenceManager preferenceManager = this.mPreferenceManager;
        if (preferenceManager != null) {
            preferenceManager.dispatchActivityDestroy();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.mHeaders.size() > 0) {
            outState.putParcelableArrayList(HEADERS_TAG, this.mHeaders);
            int index = this.mCurHeader;
            if (index != 0) {
                index = this.mHeaders.indexOf(index);
                if (index >= 0) {
                    outState.putInt(CUR_HEADER_TAG, index);
                }
            }
        }
        if (this.mPreferenceManager != null) {
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            if (preferenceScreen != null) {
                Bundle container = new Bundle();
                preferenceScreen.saveHierarchyState(container);
                outState.putBundle(PREFERENCES_TAG, container);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Bundle state) {
        if (this.mPreferenceManager != null) {
            Bundle container = state.getBundle(PREFERENCES_TAG);
            if (container != null) {
                PreferenceScreen preferenceScreen = getPreferenceScreen();
                if (preferenceScreen != null) {
                    preferenceScreen.restoreHierarchyState(container);
                    this.mSavedInstanceState = state;
                    return;
                }
            }
        }
        super.onRestoreInstanceState(state);
        if (!this.mSinglePane) {
            Header header = this.mCurHeader;
            if (header != null) {
                setSelectedHeader(header);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PreferenceManager preferenceManager = this.mPreferenceManager;
        if (preferenceManager != null) {
            preferenceManager.dispatchActivityResult(requestCode, resultCode, data);
        }
    }

    public void onContentChanged() {
        super.onContentChanged();
        if (this.mPreferenceManager != null) {
            postBindPreferences();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (isResumed()) {
            super.onListItemClick(l, v, position, id);
            if (this.mAdapter != null) {
                Object item = this.mAdapter.getItem(position);
                if (item instanceof Header) {
                    onHeaderClick((Header) item, position);
                }
            }
        }
    }

    public void onHeaderClick(Header header, int position) {
        if (header.fragment != null) {
            switchToHeader(header);
        } else if (header.intent != null) {
            startActivity(header.intent);
        }
    }

    public Intent onBuildStartFragmentIntent(String fragmentName, Bundle args, int titleRes, int shortTitleRes) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClass(this, getClass());
        intent.putExtra(EXTRA_SHOW_FRAGMENT, fragmentName);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, args);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_TITLE, titleRes);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_SHORT_TITLE, shortTitleRes);
        intent.putExtra(EXTRA_NO_HEADERS, true);
        return intent;
    }

    public void startWithFragment(String fragmentName, Bundle args, Fragment resultTo, int resultRequestCode) {
        startWithFragment(fragmentName, args, resultTo, resultRequestCode, 0, 0);
    }

    public void startWithFragment(String fragmentName, Bundle args, Fragment resultTo, int resultRequestCode, int titleRes, int shortTitleRes) {
        Intent intent = onBuildStartFragmentIntent(fragmentName, args, titleRes, shortTitleRes);
        if (resultTo == null) {
            startActivity(intent);
        } else {
            resultTo.startActivityForResult(intent, resultRequestCode);
        }
    }

    public void showBreadCrumbs(CharSequence title, CharSequence shortTitle) {
        if (this.mFragmentBreadCrumbs == null) {
            try {
                this.mFragmentBreadCrumbs = (FragmentBreadCrumbs) findViewById(16908310);
                FragmentBreadCrumbs fragmentBreadCrumbs = this.mFragmentBreadCrumbs;
                if (fragmentBreadCrumbs == null) {
                    if (title != null) {
                        setTitle(title);
                    }
                    return;
                }
                if (this.mSinglePane) {
                    fragmentBreadCrumbs.setVisibility(8);
                    View bcSection = findViewById(R.id.breadcrumb_section);
                    if (bcSection != null) {
                        bcSection.setVisibility(8);
                    }
                    setTitle(title);
                }
                this.mFragmentBreadCrumbs.setMaxVisible(2);
                this.mFragmentBreadCrumbs.setActivity(this);
            } catch (ClassCastException e) {
                setTitle(title);
                return;
            }
        }
        if (this.mFragmentBreadCrumbs.getVisibility() != 0) {
            setTitle(title);
        } else {
            this.mFragmentBreadCrumbs.setTitle(title, shortTitle);
            this.mFragmentBreadCrumbs.setParentTitle(null, null, null);
        }
    }

    public void setParentTitle(CharSequence title, CharSequence shortTitle, OnClickListener listener) {
        FragmentBreadCrumbs fragmentBreadCrumbs = this.mFragmentBreadCrumbs;
        if (fragmentBreadCrumbs != null) {
            fragmentBreadCrumbs.setParentTitle(title, shortTitle, listener);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setSelectedHeader(Header header) {
        this.mCurHeader = header;
        int index = this.mHeaders.indexOf(header);
        if (index >= 0) {
            getListView().setItemChecked(index, true);
        } else {
            getListView().clearChoices();
        }
        showBreadCrumbs(header);
    }

    /* Access modifiers changed, original: 0000 */
    public void showBreadCrumbs(Header header) {
        if (header != null) {
            CharSequence title = header.getBreadCrumbTitle(getResources());
            if (title == null) {
                title = header.getTitle(getResources());
            }
            if (title == null) {
                title = getTitle();
            }
            showBreadCrumbs(title, header.getBreadCrumbShortTitle(getResources()));
            return;
        }
        showBreadCrumbs(getTitle(), null);
    }

    private void switchToHeaderInner(String fragmentName, Bundle args) {
        getFragmentManager().popBackStack(BACK_STACK_PREFS, 1);
        if (isValidFragment(fragmentName)) {
            int i;
            Fragment f = Fragment.instantiate(this, fragmentName, args);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (this.mSinglePane) {
                i = 0;
            } else {
                i = 4099;
            }
            transaction.setTransition(i);
            transaction.replace(R.id.prefs, f);
            transaction.commitAllowingStateLoss();
            if (this.mSinglePane && this.mPrefsContainer.getVisibility() == 8) {
                this.mPrefsContainer.setVisibility(0);
                this.mHeadersContainer.setVisibility(8);
                return;
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid fragment for this activity: ");
        stringBuilder.append(fragmentName);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void switchToHeader(String fragmentName, Bundle args) {
        Header selectedHeader = null;
        for (int i = 0; i < this.mHeaders.size(); i++) {
            if (fragmentName.equals(((Header) this.mHeaders.get(i)).fragment)) {
                selectedHeader = (Header) this.mHeaders.get(i);
                break;
            }
        }
        setSelectedHeader(selectedHeader);
        switchToHeaderInner(fragmentName, args);
    }

    public void switchToHeader(Header header) {
        if (this.mCurHeader == header) {
            getFragmentManager().popBackStack(BACK_STACK_PREFS, 1);
        } else if (header.fragment != null) {
            switchToHeaderInner(header.fragment, header.fragmentArguments);
            setSelectedHeader(header);
        } else {
            throw new IllegalStateException("can't switch to header that has no fragment");
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Header findBestMatchingHeader(Header cur, ArrayList<Header> from) {
        int j;
        ArrayList<Header> matches = new ArrayList();
        for (j = 0; j < from.size(); j++) {
            Header oh = (Header) from.get(j);
            if (cur == oh || (cur.id != -1 && cur.id == oh.id)) {
                matches.clear();
                matches.add(oh);
                break;
            }
            if (cur.fragment != null) {
                if (cur.fragment.equals(oh.fragment)) {
                    matches.add(oh);
                }
            } else if (cur.intent != null) {
                if (cur.intent.equals(oh.intent)) {
                    matches.add(oh);
                }
            } else if (cur.title != null && cur.title.equals(oh.title)) {
                matches.add(oh);
            }
        }
        j = matches.size();
        if (j == 1) {
            return (Header) matches.get(0);
        }
        if (j > 1) {
            for (int j2 = 0; j2 < j; j2++) {
                Header oh2 = (Header) matches.get(j2);
                if (cur.fragmentArguments != null && cur.fragmentArguments.equals(oh2.fragmentArguments)) {
                    return oh2;
                }
                if (cur.extras != null && cur.extras.equals(oh2.extras)) {
                    return oh2;
                }
                if (cur.title != null && cur.title.equals(oh2.title)) {
                    return oh2;
                }
            }
        }
        return null;
    }

    public void startPreferenceFragment(Fragment fragment, boolean push) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.prefs, fragment);
        if (push) {
            transaction.setTransition(4097);
            transaction.addToBackStack(BACK_STACK_PREFS);
        } else {
            transaction.setTransition(4099);
        }
        transaction.commitAllowingStateLoss();
    }

    public void startPreferencePanel(String fragmentClass, Bundle args, int titleRes, CharSequence titleText, Fragment resultTo, int resultRequestCode) {
        Fragment f = Fragment.instantiate(this, fragmentClass, args);
        if (resultTo != null) {
            f.setTargetFragment(resultTo, resultRequestCode);
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.prefs, f);
        if (titleRes != 0) {
            transaction.setBreadCrumbTitle(titleRes);
        } else if (titleText != null) {
            transaction.setBreadCrumbTitle(titleText);
        }
        transaction.setTransition(4097);
        transaction.addToBackStack(BACK_STACK_PREFS);
        transaction.commitAllowingStateLoss();
    }

    public void finishPreferencePanel(Fragment caller, int resultCode, Intent resultData) {
        onBackPressed();
        if (caller != null && caller.getTargetFragment() != null) {
            caller.getTargetFragment().onActivityResult(caller.getTargetRequestCode(), resultCode, resultData);
        }
    }

    public boolean onPreferenceStartFragment(PreferenceFragment caller, Preference pref) {
        startPreferencePanel(pref.getFragment(), pref.getExtras(), pref.getTitleRes(), pref.getTitle(), null, 0);
        return true;
    }

    @UnsupportedAppUsage
    private void postBindPreferences() {
        if (!this.mHandler.hasMessages(1)) {
            this.mHandler.obtainMessage(1).sendToTarget();
        }
    }

    private void bindPreferences() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.bind(getListView());
            Bundle bundle = this.mSavedInstanceState;
            if (bundle != null) {
                super.onRestoreInstanceState(bundle);
                this.mSavedInstanceState = null;
            }
        }
    }

    @Deprecated
    public PreferenceManager getPreferenceManager() {
        return this.mPreferenceManager;
    }

    @UnsupportedAppUsage
    private void requirePreferenceManager() {
        if (this.mPreferenceManager != null) {
            return;
        }
        if (this.mAdapter == null) {
            throw new RuntimeException("This should be called after super.onCreate.");
        }
        throw new RuntimeException("Modern two-pane PreferenceActivity requires use of a PreferenceFragment");
    }

    @Deprecated
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        requirePreferenceManager();
        if (this.mPreferenceManager.setPreferences(preferenceScreen) && preferenceScreen != null) {
            postBindPreferences();
            CharSequence title = getPreferenceScreen().getTitle();
            if (title != null) {
                setTitle(title);
            }
        }
    }

    @Deprecated
    public PreferenceScreen getPreferenceScreen() {
        PreferenceManager preferenceManager = this.mPreferenceManager;
        if (preferenceManager != null) {
            return preferenceManager.getPreferenceScreen();
        }
        return null;
    }

    @Deprecated
    public void addPreferencesFromIntent(Intent intent) {
        requirePreferenceManager();
        setPreferenceScreen(this.mPreferenceManager.inflateFromIntent(intent, getPreferenceScreen()));
    }

    @Deprecated
    public void addPreferencesFromResource(int preferencesResId) {
        requirePreferenceManager();
        setPreferenceScreen(this.mPreferenceManager.inflateFromResource(this, preferencesResId, getPreferenceScreen()));
    }

    @Deprecated
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return false;
    }

    @Deprecated
    public Preference findPreference(CharSequence key) {
        PreferenceManager preferenceManager = this.mPreferenceManager;
        if (preferenceManager == null) {
            return null;
        }
        return preferenceManager.findPreference(key);
    }

    /* Access modifiers changed, original: protected */
    public void onNewIntent(Intent intent) {
        PreferenceManager preferenceManager = this.mPreferenceManager;
        if (preferenceManager != null) {
            preferenceManager.dispatchNewIntent(intent);
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean hasNextButton() {
        return this.mNextButton != null;
    }

    /* Access modifiers changed, original: protected */
    public Button getNextButton() {
        return this.mNextButton;
    }
}
