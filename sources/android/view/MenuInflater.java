package android.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.BlendMode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.MenuItem.OnMenuItemClickListener;
import com.android.internal.R;
import com.android.internal.view.menu.MenuItemImpl;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParserException;

public class MenuInflater {
    private static final Class<?>[] ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE = ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
    private static final Class<?>[] ACTION_VIEW_CONSTRUCTOR_SIGNATURE = new Class[]{Context.class};
    private static final String LOG_TAG = "MenuInflater";
    private static final int NO_ID = 0;
    private static final String XML_GROUP = "group";
    private static final String XML_ITEM = "item";
    private static final String XML_MENU = "menu";
    private final Object[] mActionProviderConstructorArguments = this.mActionViewConstructorArguments;
    private final Object[] mActionViewConstructorArguments;
    private Context mContext;
    private Object mRealOwner;

    private static class InflatedOnMenuItemClickListener implements OnMenuItemClickListener {
        private static final Class<?>[] PARAM_TYPES = new Class[]{MenuItem.class};
        private Method mMethod;
        private Object mRealOwner;

        public InflatedOnMenuItemClickListener(Object realOwner, String methodName) {
            this.mRealOwner = realOwner;
            Class<?> c = realOwner.getClass();
            try {
                this.mMethod = c.getMethod(methodName, PARAM_TYPES);
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Couldn't resolve menu item onClick handler ");
                stringBuilder.append(methodName);
                stringBuilder.append(" in class ");
                stringBuilder.append(c.getName());
                InflateException ex = new InflateException(stringBuilder.toString());
                ex.initCause(e);
                throw ex;
            }
        }

        public boolean onMenuItemClick(MenuItem item) {
            try {
                if (this.mMethod.getReturnType() == Boolean.TYPE) {
                    return ((Boolean) this.mMethod.invoke(this.mRealOwner, new Object[]{item})).booleanValue();
                }
                this.mMethod.invoke(this.mRealOwner, new Object[]{item});
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class MenuState {
        private static final int defaultGroupId = 0;
        private static final int defaultItemCategory = 0;
        private static final int defaultItemCheckable = 0;
        private static final boolean defaultItemChecked = false;
        private static final boolean defaultItemEnabled = true;
        private static final int defaultItemId = 0;
        private static final int defaultItemOrder = 0;
        private static final boolean defaultItemVisible = true;
        private int groupCategory;
        private int groupCheckable;
        private boolean groupEnabled;
        private int groupId;
        private int groupOrder;
        private boolean groupVisible;
        private ActionProvider itemActionProvider;
        private String itemActionProviderClassName;
        private String itemActionViewClassName;
        private int itemActionViewLayout;
        private boolean itemAdded;
        private int itemAlphabeticModifiers;
        private char itemAlphabeticShortcut;
        private int itemCategoryOrder;
        private int itemCheckable;
        private boolean itemChecked;
        private CharSequence itemContentDescription;
        private boolean itemEnabled;
        private int itemIconResId;
        private ColorStateList itemIconTintList = null;
        private int itemId;
        private String itemListenerMethodName;
        private int itemNumericModifiers;
        private char itemNumericShortcut;
        private int itemShowAsAction;
        private CharSequence itemTitle;
        private CharSequence itemTitleCondensed;
        private CharSequence itemTooltipText;
        private boolean itemVisible;
        private BlendMode mItemIconBlendMode = null;
        private Menu menu;

        public MenuState(Menu menu) {
            this.menu = menu;
            resetGroup();
        }

        public void resetGroup() {
            this.groupId = 0;
            this.groupCategory = 0;
            this.groupOrder = 0;
            this.groupCheckable = 0;
            this.groupVisible = true;
            this.groupEnabled = true;
        }

        public void readGroup(AttributeSet attrs) {
            TypedArray a = MenuInflater.this.mContext.obtainStyledAttributes(attrs, R.styleable.MenuGroup);
            this.groupId = a.getResourceId(1, 0);
            this.groupCategory = a.getInt(3, 0);
            this.groupOrder = a.getInt(4, 0);
            this.groupCheckable = a.getInt(5, 0);
            this.groupVisible = a.getBoolean(2, true);
            this.groupEnabled = a.getBoolean(0, true);
            a.recycle();
        }

        public void readItem(AttributeSet attrs) {
            TypedArray a = MenuInflater.this.mContext.obtainStyledAttributes(attrs, R.styleable.MenuItem);
            this.itemId = a.getResourceId(2, 0);
            this.itemCategoryOrder = (-65536 & a.getInt(5, this.groupCategory)) | (65535 & a.getInt(6, this.groupOrder));
            this.itemTitle = a.getText(7);
            this.itemTitleCondensed = a.getText(8);
            this.itemIconResId = a.getResourceId(0, 0);
            if (a.hasValue(22)) {
                this.mItemIconBlendMode = Drawable.parseBlendMode(a.getInt(22, -1), this.mItemIconBlendMode);
            } else {
                this.mItemIconBlendMode = null;
            }
            if (a.hasValue(21)) {
                this.itemIconTintList = a.getColorStateList(21);
            } else {
                this.itemIconTintList = null;
            }
            this.itemAlphabeticShortcut = getShortcut(a.getString(9));
            this.itemAlphabeticModifiers = a.getInt(19, 4096);
            this.itemNumericShortcut = getShortcut(a.getString(10));
            this.itemNumericModifiers = a.getInt(20, 4096);
            if (a.hasValue(11)) {
                this.itemCheckable = a.getBoolean(11, false);
            } else {
                this.itemCheckable = this.groupCheckable;
            }
            this.itemChecked = a.getBoolean(3, false);
            this.itemVisible = a.getBoolean(4, this.groupVisible);
            boolean z = true;
            this.itemEnabled = a.getBoolean(1, this.groupEnabled);
            this.itemShowAsAction = a.getInt(14, -1);
            this.itemListenerMethodName = a.getString(12);
            this.itemActionViewLayout = a.getResourceId(15, 0);
            this.itemActionViewClassName = a.getString(16);
            this.itemActionProviderClassName = a.getString(17);
            if (this.itemActionProviderClassName == null) {
                z = false;
            }
            boolean hasActionProvider = z;
            if (hasActionProvider && this.itemActionViewLayout == 0 && this.itemActionViewClassName == null) {
                this.itemActionProvider = (ActionProvider) newInstance(this.itemActionProviderClassName, MenuInflater.ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE, MenuInflater.this.mActionProviderConstructorArguments);
            } else {
                if (hasActionProvider) {
                    Log.w(MenuInflater.LOG_TAG, "Ignoring attribute 'actionProviderClass'. Action view already specified.");
                }
                this.itemActionProvider = null;
            }
            this.itemContentDescription = a.getText(13);
            this.itemTooltipText = a.getText(18);
            a.recycle();
            this.itemAdded = false;
        }

        private char getShortcut(String shortcutString) {
            if (shortcutString == null) {
                return 0;
            }
            return shortcutString.charAt(0);
        }

        private void setItem(MenuItem item) {
            item.setChecked(this.itemChecked).setVisible(this.itemVisible).setEnabled(this.itemEnabled).setCheckable(this.itemCheckable >= 1).setTitleCondensed(this.itemTitleCondensed).setIcon(this.itemIconResId).setAlphabeticShortcut(this.itemAlphabeticShortcut, this.itemAlphabeticModifiers).setNumericShortcut(this.itemNumericShortcut, this.itemNumericModifiers);
            int i = this.itemShowAsAction;
            if (i >= 0) {
                item.setShowAsAction(i);
            }
            BlendMode blendMode = this.mItemIconBlendMode;
            if (blendMode != null) {
                item.setIconTintBlendMode(blendMode);
            }
            ColorStateList colorStateList = this.itemIconTintList;
            if (colorStateList != null) {
                item.setIconTintList(colorStateList);
            }
            if (this.itemListenerMethodName != null) {
                if (MenuInflater.this.mContext.isRestricted()) {
                    throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
                }
                item.setOnMenuItemClickListener(new InflatedOnMenuItemClickListener(MenuInflater.this.getRealOwner(), this.itemListenerMethodName));
            }
            if (item instanceof MenuItemImpl) {
                MenuItemImpl impl = (MenuItemImpl) item;
                if (this.itemCheckable >= 2) {
                    impl.setExclusiveCheckable(true);
                }
            }
            boolean actionViewSpecified = false;
            String str = this.itemActionViewClassName;
            if (str != null) {
                item.setActionView((View) newInstance(str, MenuInflater.ACTION_VIEW_CONSTRUCTOR_SIGNATURE, MenuInflater.this.mActionViewConstructorArguments));
                actionViewSpecified = true;
            }
            int i2 = this.itemActionViewLayout;
            if (i2 > 0) {
                if (actionViewSpecified) {
                    Log.w(MenuInflater.LOG_TAG, "Ignoring attribute 'itemActionViewLayout'. Action view already specified.");
                } else {
                    item.setActionView(i2);
                }
            }
            ActionProvider actionProvider = this.itemActionProvider;
            if (actionProvider != null) {
                item.setActionProvider(actionProvider);
            }
            item.setContentDescription(this.itemContentDescription);
            item.setTooltipText(this.itemTooltipText);
        }

        public MenuItem addItem() {
            this.itemAdded = true;
            MenuItem item = this.menu.add(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle);
            setItem(item);
            return item;
        }

        public SubMenu addSubMenuItem() {
            this.itemAdded = true;
            SubMenu subMenu = this.menu.addSubMenu(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle);
            setItem(subMenu.getItem());
            return subMenu;
        }

        public boolean hasAddedItem() {
            return this.itemAdded;
        }

        private <T> T newInstance(String className, Class<?>[] constructorSignature, Object[] arguments) {
            try {
                Constructor<?> constructor = MenuInflater.this.mContext.getClassLoader().loadClass(className).getConstructor(constructorSignature);
                constructor.setAccessible(true);
                return constructor.newInstance(arguments);
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Cannot instantiate class: ");
                stringBuilder.append(className);
                Log.w(MenuInflater.LOG_TAG, stringBuilder.toString(), e);
                return null;
            }
        }
    }

    public MenuInflater(Context context) {
        this.mContext = context;
        this.mActionViewConstructorArguments = new Object[]{context};
    }

    public MenuInflater(Context context, Object realOwner) {
        this.mContext = context;
        this.mRealOwner = realOwner;
        this.mActionViewConstructorArguments = new Object[]{context};
    }

    public void inflate(int menuRes, Menu menu) {
        String str = "Error inflating menu XML";
        XmlResourceParser parser = null;
        try {
            parser = this.mContext.getResources().getLayout(menuRes);
            parseMenu(parser, Xml.asAttributeSet(parser), menu);
            if (parser != null) {
                parser.close();
            }
        } catch (XmlPullParserException e) {
            throw new InflateException(str, e);
        } catch (IOException e2) {
            throw new InflateException(str, e2);
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0041  */
    private void parseMenu(org.xmlpull.v1.XmlPullParser r13, android.util.AttributeSet r14, android.view.Menu r15) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r12 = this;
        r0 = new android.view.MenuInflater$MenuState;
        r0.<init>(r15);
        r1 = r13.getEventType();
        r2 = 0;
        r3 = 0;
    L_0x000b:
        r4 = 1;
        r5 = 2;
        r6 = "menu";
        if (r1 != r5) goto L_0x0038;
    L_0x0012:
        r7 = r13.getName();
        r8 = r7.equals(r6);
        if (r8 == 0) goto L_0x0021;
    L_0x001c:
        r1 = r13.next();
        goto L_0x003e;
    L_0x0021:
        r4 = new java.lang.RuntimeException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Expecting menu, got ";
        r5.append(r6);
        r5.append(r7);
        r5 = r5.toString();
        r4.<init>(r5);
        throw r4;
    L_0x0038:
        r1 = r13.next();
        if (r1 != r4) goto L_0x000b;
    L_0x003e:
        r7 = 0;
    L_0x003f:
        if (r7 != 0) goto L_0x00d9;
    L_0x0041:
        if (r1 == r4) goto L_0x00d1;
    L_0x0043:
        r8 = "item";
        r9 = "group";
        if (r1 == r5) goto L_0x009c;
    L_0x0049:
        r10 = 3;
        if (r1 == r10) goto L_0x004e;
    L_0x004c:
        goto L_0x00cb;
    L_0x004e:
        r10 = r13.getName();
        if (r2 == 0) goto L_0x005e;
    L_0x0054:
        r11 = r10.equals(r3);
        if (r11 == 0) goto L_0x005e;
    L_0x005a:
        r2 = 0;
        r3 = 0;
        goto L_0x00cb;
    L_0x005e:
        r9 = r10.equals(r9);
        if (r9 == 0) goto L_0x0068;
    L_0x0064:
        r0.resetGroup();
        goto L_0x00cb;
    L_0x0068:
        r8 = r10.equals(r8);
        if (r8 == 0) goto L_0x0094;
    L_0x006e:
        r8 = r0.hasAddedItem();
        if (r8 != 0) goto L_0x00cb;
    L_0x0074:
        r8 = r0.itemActionProvider;
        if (r8 == 0) goto L_0x008c;
    L_0x007a:
        r8 = r0.itemActionProvider;
        r8 = r8.hasSubMenu();
        if (r8 == 0) goto L_0x008c;
    L_0x0084:
        r8 = r0.addSubMenuItem();
        r12.registerMenu(r8, r14);
        goto L_0x00cb;
    L_0x008c:
        r8 = r0.addItem();
        r12.registerMenu(r8, r14);
        goto L_0x00cb;
    L_0x0094:
        r8 = r10.equals(r6);
        if (r8 == 0) goto L_0x00cb;
    L_0x009a:
        r7 = 1;
        goto L_0x00cb;
    L_0x009c:
        if (r2 == 0) goto L_0x009f;
    L_0x009e:
        goto L_0x00cb;
    L_0x009f:
        r10 = r13.getName();
        r9 = r10.equals(r9);
        if (r9 == 0) goto L_0x00ad;
    L_0x00a9:
        r0.readGroup(r14);
        goto L_0x00cb;
    L_0x00ad:
        r8 = r10.equals(r8);
        if (r8 == 0) goto L_0x00b7;
    L_0x00b3:
        r0.readItem(r14);
        goto L_0x00cb;
    L_0x00b7:
        r8 = r10.equals(r6);
        if (r8 == 0) goto L_0x00c8;
    L_0x00bd:
        r8 = r0.addSubMenuItem();
        r12.registerMenu(r8, r14);
        r12.parseMenu(r13, r14, r8);
        goto L_0x00cb;
    L_0x00c8:
        r2 = 1;
        r3 = r10;
    L_0x00cb:
        r1 = r13.next();
        goto L_0x003f;
    L_0x00d1:
        r4 = new java.lang.RuntimeException;
        r5 = "Unexpected end of document";
        r4.<init>(r5);
        throw r4;
    L_0x00d9:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.MenuInflater.parseMenu(org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.view.Menu):void");
    }

    private void registerMenu(MenuItem item, AttributeSet set) {
    }

    private void registerMenu(SubMenu subMenu, AttributeSet set) {
    }

    /* Access modifiers changed, original: 0000 */
    public Context getContext() {
        return this.mContext;
    }

    private Object getRealOwner() {
        if (this.mRealOwner == null) {
            this.mRealOwner = findRealOwner(this.mContext);
        }
        return this.mRealOwner;
    }

    private Object findRealOwner(Object owner) {
        if (!(owner instanceof Activity) && (owner instanceof ContextWrapper)) {
            return findRealOwner(((ContextWrapper) owner).getBaseContext());
        }
        return owner;
    }
}
