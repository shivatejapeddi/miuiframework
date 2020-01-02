package com.android.internal.app;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.ListFragment;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.RemoteException;
import android.provider.Settings.Global;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.R;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocalePicker extends ListFragment {
    private static final boolean DEBUG = false;
    private static final String TAG = "LocalePicker";
    private static final String[] pseudoLocales = new String[]{"en-XA", "ar-XB"};
    LocaleSelectionListener mListener;

    public static class LocaleInfo implements Comparable<LocaleInfo> {
        static final Collator sCollator = Collator.getInstance();
        String label;
        final Locale locale;

        public LocaleInfo(String label, Locale locale) {
            this.label = label;
            this.locale = locale;
        }

        public String getLabel() {
            return this.label;
        }

        @UnsupportedAppUsage
        public Locale getLocale() {
            return this.locale;
        }

        public String toString() {
            return this.label;
        }

        public int compareTo(LocaleInfo another) {
            return sCollator.compare(this.label, another.label);
        }
    }

    public interface LocaleSelectionListener {
        void onLocaleSelected(Locale locale);
    }

    public static String[] getSystemAssetLocales() {
        return Resources.getSystem().getAssets().getLocales();
    }

    public static String[] getSupportedLocales(Context context) {
        return context.getResources().getStringArray(R.array.supported_locales);
    }

    public static List<LocaleInfo> getAllAssetLocales(Context context, boolean isInDeveloperMode) {
        Resources resources = context.getResources();
        String[] locales = getSystemAssetLocales();
        List<String> localeList = new ArrayList(locales.length);
        Collections.addAll(localeList, locales);
        Collections.sort(localeList);
        String[] specialLocaleCodes = resources.getStringArray(17236117);
        String[] specialLocaleNames = resources.getStringArray(17236118);
        ArrayList<LocaleInfo> localeInfos = new ArrayList(localeList.size());
        for (String locale : localeList) {
            Locale l = Locale.forLanguageTag(locale.replace('_', '-'));
            if (l != null) {
                if (!("und".equals(l.getLanguage()) || l.getLanguage().isEmpty())) {
                    if (!l.getCountry().isEmpty()) {
                        if (isInDeveloperMode || !LocaleList.isPseudoLocale(l)) {
                            if (localeInfos.isEmpty()) {
                                localeInfos.add(new LocaleInfo(toTitleCase(l.getDisplayLanguage(l)), l));
                            } else {
                                LocaleInfo previous = (LocaleInfo) localeInfos.get(localeInfos.size() - 1);
                                if (!previous.locale.getLanguage().equals(l.getLanguage()) || previous.locale.getLanguage().equals("zz")) {
                                    localeInfos.add(new LocaleInfo(toTitleCase(LocalePickerInjector.getDisplayLanguage(l, specialLocaleCodes, specialLocaleNames, toTitleCase(l.getDisplayLanguage(l)))), l));
                                } else {
                                    previous.label = toTitleCase(getDisplayName(previous.locale, specialLocaleCodes, specialLocaleNames));
                                    localeInfos.add(new LocaleInfo(toTitleCase(getDisplayName(l, specialLocaleCodes, specialLocaleNames)), l));
                                }
                            }
                        }
                    }
                }
            }
        }
        Collections.sort(localeInfos);
        return localeInfos;
    }

    public static ArrayAdapter<LocaleInfo> constructAdapter(Context context) {
        return constructAdapter(context, R.layout.locale_picker_item, R.id.locale);
    }

    public static ArrayAdapter<LocaleInfo> constructAdapter(Context context, int layoutId, int fieldId) {
        boolean z = false;
        if (Global.getInt(context.getContentResolver(), "development_settings_enabled", 0) != 0) {
            z = true;
        }
        List<LocaleInfo> localeInfos = getAllAssetLocales(context, z);
        LocaleInfo[] localeInfoArray = new LocaleInfo[localeInfos.size()];
        localeInfos.toArray(localeInfoArray);
        LocalePickerInjector.sortLocaleInfos(localeInfoArray, context);
        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int i = layoutId;
        final int i2 = fieldId;
        return new ArrayAdapter<LocaleInfo>(context, layoutId, fieldId, localeInfoArray) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                TextView text;
                if (convertView == null) {
                    view = layoutInflater.inflate(i, parent, false);
                    text = (TextView) view.findViewById(i2);
                    view.setTag(text);
                } else {
                    view = convertView;
                    text = (TextView) view.getTag();
                }
                LocaleInfo item = (LocaleInfo) getItem(position);
                text.setText(item.toString());
                text.setTextLocale(item.getLocale());
                return view;
            }
        };
    }

    private static String toTitleCase(String s) {
        if (s.length() == 0) {
            return s;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Character.toUpperCase(s.charAt(0)));
        stringBuilder.append(s.substring(1));
        return stringBuilder.toString();
    }

    private static String getDisplayName(Locale l, String[] specialLocaleCodes, String[] specialLocaleNames) {
        String code = l.toString();
        for (int i = 0; i < specialLocaleCodes.length; i++) {
            if (specialLocaleCodes[i].equals(code)) {
                return specialLocaleNames[i];
            }
        }
        return l.getDisplayName(l);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(constructAdapter(getActivity()));
    }

    public void setLocaleSelectionListener(LocaleSelectionListener listener) {
        this.mListener = listener;
    }

    public void onResume() {
        super.onResume();
        getListView().requestFocus();
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        if (this.mListener != null) {
            this.mListener.onLocaleSelected(((LocaleInfo) getListAdapter().getItem(position)).locale);
        }
    }

    @UnsupportedAppUsage
    public static void updateLocale(Locale locale) {
        updateLocales(new LocaleList(locale));
    }

    @UnsupportedAppUsage
    public static void updateLocales(LocaleList locales) {
        try {
            IActivityManager am = ActivityManager.getService();
            Configuration config = am.getConfiguration();
            config.setLocales(locales);
            config.userSetLocale = true;
            am.updatePersistentConfiguration(config);
            BackupManager.dataChanged("com.android.providers.settings");
        } catch (RemoteException e) {
        }
    }

    @UnsupportedAppUsage
    public static LocaleList getLocales() {
        try {
            return ActivityManager.getService().getConfiguration().getLocales();
        } catch (RemoteException e) {
            return LocaleList.getDefault();
        }
    }
}
