package home.loto;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import java.util.Locale;
/*This class represents application settings fragment that is a part of settings activity*/
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener
{
    static final String KEY_PREF_NOTIFY_WIN = "Notify_win";
    static final String KEY_PREF_NOTIFY_JACKPOT = "Notify_jackpot";
    static final String KEY_PREF_LANGUAGE= "language_preference";
    static final String KEY_PREF_JACKPOT=  "jackpot_preference";
    static final String KEY_PREF_MAIN=  "main_preference";
    static final String KEY_PREF_NEW_VERSION=  "new_version";
    private ListPreference jackpot_preference;

    /*Called when the activity is first created. */
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        setPreferencesFromResource(R.xml.preferences, s);

        jackpot_preference = findPreference(KEY_PREF_JACKPOT);
        ListPreference language_preference = findPreference(KEY_PREF_LANGUAGE);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Lotto.getInstance());

        jackpot_preference.setEnabled(prefs.getBoolean(KEY_PREF_NOTIFY_JACKPOT,false));
        if (jackpot_preference.getValue() == null) { jackpot_preference.setValue("15000000"); }

        if (language_preference.getValue() == null) {
            if(Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("iw") || Locale.getDefault().getLanguage().equals("ru"))
                language_preference.setValue(Locale.getDefault().getLanguage());
            else
                language_preference.setValue("iw");
        }
    }
    /*Set preferred language*/
    public void setLocale(String l) {

        Locale locale = new Locale(l);

        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());

    }
    /*When activity returns to focus*/
    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Lotto.getInstance());
        jackpot_preference.setEnabled(prefs.getBoolean(KEY_PREF_NOTIFY_JACKPOT,false));

    }
    /*When activity is out of focus*/
    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Lotto.getInstance());
        jackpot_preference.setEnabled(prefs.getBoolean(KEY_PREF_NOTIFY_JACKPOT,false));
    }
    /*Listens to the changes in activity settings */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(KEY_PREF_NOTIFY_JACKPOT)) jackpot_preference.setEnabled(sharedPreferences.getBoolean(KEY_PREF_NOTIFY_JACKPOT,false));

        if (key.equals(KEY_PREF_LANGUAGE)) {
            LocaleHelper.setLocale(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()).getString(key, "iw"));

            setLocale(PreferenceManager.getDefaultSharedPreferences(getContext()).getString(key, "iw"));
            setPreferenceScreen(null);
            addPreferencesFromResource(R.xml.preferences);

            jackpot_preference = (ListPreference) findPreference(KEY_PREF_JACKPOT);
            jackpot_preference.setEnabled(sharedPreferences.getBoolean(KEY_PREF_NOTIFY_JACKPOT,false));
        }
    }
}

