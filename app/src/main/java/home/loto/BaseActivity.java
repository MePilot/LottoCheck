package home.loto;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

import static home.loto.SettingsFragment.KEY_PREF_LANGUAGE;

public class BaseActivity extends AppCompatActivity {
    /*This class modifies the default AppCompatActivity to switch to desired language effectively and also manages the top menu bar*/
    private String initialLocale;
    /*Function is called first when an activity is created*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialLocale = LocaleHelper.getPersistedLocale(this);
        setLocale();
    }
    /*Setting the correct language defined in app settings*/
    public void setLocale() {
        final Resources res = getResources();
        final Configuration conf = res.getConfiguration();

        final Locale locale = getLocale(this);

        if (!conf.locale.equals(locale)) {

            conf.setLocale(locale);
            res.updateConfiguration(conf, null);
        }
    }
    /*Getting the language defined in app settings*/
    public static Locale getLocale(Context context) {

        SharedPreferences sharedPref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        if(Lotto.defLocale.equals("en") || Lotto.defLocale.equals("iw") || Lotto.defLocale.equals("ru"))
        {   return new Locale(sharedPref.getString(KEY_PREF_LANGUAGE,Lotto.defLocale));}
        else {
           return new Locale(sharedPref.getString(KEY_PREF_LANGUAGE,"iw"));}

    }
    /*Setting the menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    /*Change activity language before it creates context*/
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (initialLocale != null && !initialLocale.equals(LocaleHelper.getPersistedLocale(this))) {
            recreate();
        }
    }
}