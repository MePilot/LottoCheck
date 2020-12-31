package home.loto;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import androidx.preference.PreferenceManager;
import java.util.Locale;
/*This class is intended for managing and switching UI language*/
class LocaleHelper {
    static Context onAttach(Context context) {
        String locale = getPersistedLocale(context);
        return setLocale(context, locale);
    }
    /*get preferred language*/
    static String getPersistedLocale(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SettingsFragment.KEY_PREF_LANGUAGE, "");
    }

    /*set preferred language*/
    static Context setLocale(Context context, String localeSpec) {
        Locale locale;
        if (localeSpec.equals("system")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = Resources.getSystem().getConfiguration().getLocales().get(0);
            } else {

                locale = Resources.getSystem().getConfiguration().locale;
            }
        } else {
            locale = new Locale(localeSpec);
        }
        Locale.setDefault(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, locale);
        } else {
            return updateResourcesLegacy(context, locale);
        }
    }
    /*Apply the desired language and return updated context for different Android versions*/
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    private static Context updateResourcesLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        configuration.setLayoutDirection(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

}
