package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.QuickContact;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import com.android.internal.R;

public class QuickContactBadge extends ImageView implements OnClickListener {
    static final int EMAIL_ID_COLUMN_INDEX = 0;
    static final String[] EMAIL_LOOKUP_PROJECTION;
    static final int EMAIL_LOOKUP_STRING_COLUMN_INDEX = 1;
    private static final String EXTRA_URI_CONTENT = "uri_content";
    static final int PHONE_ID_COLUMN_INDEX = 0;
    static final String[] PHONE_LOOKUP_PROJECTION;
    static final int PHONE_LOOKUP_STRING_COLUMN_INDEX = 1;
    private static final int TOKEN_EMAIL_LOOKUP = 0;
    private static final int TOKEN_EMAIL_LOOKUP_AND_TRIGGER = 2;
    private static final int TOKEN_PHONE_LOOKUP = 1;
    private static final int TOKEN_PHONE_LOOKUP_AND_TRIGGER = 3;
    private String mContactEmail;
    private String mContactPhone;
    private Uri mContactUri;
    private Drawable mDefaultAvatar;
    protected String[] mExcludeMimes;
    private Bundle mExtras;
    @UnsupportedAppUsage
    private Drawable mOverlay;
    private String mPrioritizedMimeType;
    private QueryHandler mQueryHandler;

    private class QueryHandler extends AsyncQueryHandler {
        public QueryHandler(ContentResolver cr) {
            super(cr);
        }

        /* Access modifiers changed, original: protected */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x0071  */
        /* JADX WARNING: Removed duplicated region for block: B:40:? A:{SYNTHETIC, RETURN} */
        /* JADX WARNING: Removed duplicated region for block: B:37:0x00a4  */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x0071  */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x0080  */
        /* JADX WARNING: Removed duplicated region for block: B:37:0x00a4  */
        /* JADX WARNING: Removed duplicated region for block: B:40:? A:{SYNTHETIC, RETURN} */
        public void onQueryComplete(int r11, java.lang.Object r12, android.database.Cursor r13) {
            /*
            r10 = this;
            r0 = 0;
            r1 = 0;
            r2 = 0;
            if (r12 == 0) goto L_0x0009;
        L_0x0005:
            r3 = r12;
            r3 = (android.os.Bundle) r3;
            goto L_0x000e;
        L_0x0009:
            r3 = new android.os.Bundle;
            r3.<init>();
        L_0x000e:
            r4 = 0;
            r5 = "uri_content";
            r6 = 1;
            if (r11 == 0) goto L_0x0053;
        L_0x0015:
            if (r11 == r6) goto L_0x003d;
        L_0x0017:
            r7 = 2;
            r8 = 0;
            if (r11 == r7) goto L_0x002d;
        L_0x001b:
            r7 = 3;
            if (r11 == r7) goto L_0x001f;
        L_0x001e:
            goto L_0x006f;
        L_0x001f:
            r2 = 1;
            r7 = "tel";
            r9 = r3.getString(r5);	 Catch:{ all -> 0x003b }
            r7 = android.net.Uri.fromParts(r7, r9, r8);	 Catch:{ all -> 0x003b }
            r1 = r7;
            goto L_0x003d;
        L_0x002d:
            r2 = 1;
            r7 = "mailto";
            r9 = r3.getString(r5);	 Catch:{ all -> 0x003b }
            r7 = android.net.Uri.fromParts(r7, r9, r8);	 Catch:{ all -> 0x003b }
            r1 = r7;
            goto L_0x0053;
        L_0x003b:
            r4 = move-exception;
            goto L_0x0069;
        L_0x003d:
            if (r13 == 0) goto L_0x006f;
        L_0x003f:
            r7 = r13.moveToFirst();	 Catch:{ all -> 0x003b }
            if (r7 == 0) goto L_0x006f;
        L_0x0045:
            r7 = r13.getLong(r4);	 Catch:{ all -> 0x003b }
            r4 = r13.getString(r6);	 Catch:{ all -> 0x003b }
            r6 = android.provider.ContactsContract.Contacts.getLookupUri(r7, r4);	 Catch:{ all -> 0x003b }
            r0 = r6;
            goto L_0x006f;
        L_0x0053:
            if (r13 == 0) goto L_0x006f;
        L_0x0055:
            r7 = r13.moveToFirst();	 Catch:{ all -> 0x003b }
            if (r7 == 0) goto L_0x006f;
        L_0x005b:
            r7 = r13.getLong(r4);	 Catch:{ all -> 0x003b }
            r4 = r13.getString(r6);	 Catch:{ all -> 0x003b }
            r6 = android.provider.ContactsContract.Contacts.getLookupUri(r7, r4);	 Catch:{ all -> 0x003b }
            r0 = r6;
            goto L_0x006f;
        L_0x0069:
            if (r13 == 0) goto L_0x006e;
        L_0x006b:
            r13.close();
        L_0x006e:
            throw r4;
        L_0x006f:
            if (r13 == 0) goto L_0x0074;
        L_0x0071:
            r13.close();
        L_0x0074:
            r4 = android.widget.QuickContactBadge.this;
            r4.mContactUri = r0;
            r4 = android.widget.QuickContactBadge.this;
            r4.onContactUriChanged();
            if (r2 == 0) goto L_0x00a2;
        L_0x0080:
            r4 = android.widget.QuickContactBadge.this;
            r4 = r4.mContactUri;
            if (r4 == 0) goto L_0x00a2;
        L_0x0088:
            r4 = android.widget.QuickContactBadge.this;
            r4 = r4.getContext();
            r5 = android.widget.QuickContactBadge.this;
            r6 = r5.mContactUri;
            r7 = android.widget.QuickContactBadge.this;
            r7 = r7.mExcludeMimes;
            r8 = android.widget.QuickContactBadge.this;
            r8 = r8.mPrioritizedMimeType;
            android.provider.ContactsContract.QuickContact.showQuickContact(r4, r5, r6, r7, r8);
            goto L_0x00bb;
        L_0x00a2:
            if (r1 == 0) goto L_0x00bb;
        L_0x00a4:
            r4 = new android.content.Intent;
            r6 = "com.android.contacts.action.SHOW_OR_CREATE_CONTACT";
            r4.<init>(r6, r1);
            r3.remove(r5);
            r4.putExtras(r3);
            r5 = android.widget.QuickContactBadge.this;
            r5 = r5.getContext();
            r5.startActivity(r4);
        L_0x00bb:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.QuickContactBadge$QueryHandler.onQueryComplete(int, java.lang.Object, android.database.Cursor):void");
        }
    }

    static {
        String str = ContactsColumns.LOOKUP_KEY;
        EMAIL_LOOKUP_PROJECTION = new String[]{"contact_id", str};
        PHONE_LOOKUP_PROJECTION = new String[]{"_id", str};
    }

    public QuickContactBadge(Context context) {
        this(context, null);
    }

    public QuickContactBadge(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickContactBadge(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public QuickContactBadge(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mExtras = null;
        this.mExcludeMimes = null;
        TypedArray styledAttributes = this.mContext.obtainStyledAttributes(R.styleable.Theme);
        this.mOverlay = styledAttributes.getDrawable(325);
        styledAttributes.recycle();
        setOnClickListener(this);
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mQueryHandler = new QueryHandler(this.mContext.getContentResolver());
        }
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable overlay = this.mOverlay;
        if (overlay != null && overlay.isStateful() && overlay.setState(getDrawableState())) {
            invalidateDrawable(overlay);
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        Drawable drawable = this.mOverlay;
        if (drawable != null) {
            drawable.setHotspot(x, y);
        }
    }

    public void setMode(int size) {
    }

    public void setPrioritizedMimeType(String prioritizedMimeType) {
        this.mPrioritizedMimeType = prioritizedMimeType;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isEnabled()) {
            Drawable drawable = this.mOverlay;
            if (drawable != null && drawable.getIntrinsicWidth() != 0 && this.mOverlay.getIntrinsicHeight() != 0) {
                this.mOverlay.setBounds(0, 0, getWidth(), getHeight());
                if (this.mPaddingTop == 0 && this.mPaddingLeft == 0) {
                    this.mOverlay.draw(canvas);
                } else {
                    int saveCount = canvas.getSaveCount();
                    canvas.save();
                    canvas.translate((float) this.mPaddingLeft, (float) this.mPaddingTop);
                    this.mOverlay.draw(canvas);
                    canvas.restoreToCount(saveCount);
                }
            }
        }
    }

    private boolean isAssigned() {
        return (this.mContactUri == null && this.mContactEmail == null && this.mContactPhone == null) ? false : true;
    }

    public void setImageToDefault() {
        if (this.mDefaultAvatar == null) {
            this.mDefaultAvatar = this.mContext.getDrawable(R.drawable.ic_contact_picture);
        }
        setImageDrawable(this.mDefaultAvatar);
    }

    public void assignContactUri(Uri contactUri) {
        this.mContactUri = contactUri;
        this.mContactEmail = null;
        this.mContactPhone = null;
        onContactUriChanged();
    }

    public void assignContactFromEmail(String emailAddress, boolean lazyLookup) {
        assignContactFromEmail(emailAddress, lazyLookup, null);
    }

    public void assignContactFromEmail(String emailAddress, boolean lazyLookup, Bundle extras) {
        this.mContactEmail = emailAddress;
        this.mExtras = extras;
        if (!lazyLookup) {
            QueryHandler queryHandler = this.mQueryHandler;
            if (queryHandler != null) {
                queryHandler.startQuery(0, null, Uri.withAppendedPath(Email.CONTENT_LOOKUP_URI, Uri.encode(this.mContactEmail)), EMAIL_LOOKUP_PROJECTION, null, null, null);
                return;
            }
        }
        this.mContactUri = null;
        onContactUriChanged();
    }

    public void assignContactFromPhone(String phoneNumber, boolean lazyLookup) {
        assignContactFromPhone(phoneNumber, lazyLookup, new Bundle());
    }

    public void assignContactFromPhone(String phoneNumber, boolean lazyLookup, Bundle extras) {
        this.mContactPhone = phoneNumber;
        this.mExtras = extras;
        if (!lazyLookup) {
            QueryHandler queryHandler = this.mQueryHandler;
            if (queryHandler != null) {
                queryHandler.startQuery(1, null, Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, this.mContactPhone), PHONE_LOOKUP_PROJECTION, null, null, null);
                return;
            }
        }
        this.mContactUri = null;
        onContactUriChanged();
    }

    public void setOverlay(Drawable overlay) {
        this.mOverlay = overlay;
    }

    private void onContactUriChanged() {
        setEnabled(isAssigned());
    }

    public void onClick(View v) {
        Bundle extras = this.mExtras;
        if (extras == null) {
            extras = new Bundle();
        }
        if (this.mContactUri != null) {
            QuickContact.showQuickContact(getContext(), (View) this, this.mContactUri, this.mExcludeMimes, this.mPrioritizedMimeType);
        } else {
            String str = this.mContactEmail;
            String str2 = EXTRA_URI_CONTENT;
            if (str == null || this.mQueryHandler == null) {
                str = this.mContactPhone;
                if (str != null && this.mQueryHandler != null) {
                    extras.putString(str2, str);
                    this.mQueryHandler.startQuery(3, extras, Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, this.mContactPhone), PHONE_LOOKUP_PROJECTION, null, null, null);
                } else {
                    return;
                }
            }
            extras.putString(str2, str);
            this.mQueryHandler.startQuery(2, extras, Uri.withAppendedPath(Email.CONTENT_LOOKUP_URI, Uri.encode(this.mContactEmail)), EMAIL_LOOKUP_PROJECTION, null, null, null);
        }
    }

    public CharSequence getAccessibilityClassName() {
        return QuickContactBadge.class.getName();
    }

    public void setExcludeMimes(String[] excludeMimes) {
        this.mExcludeMimes = excludeMimes;
    }
}
