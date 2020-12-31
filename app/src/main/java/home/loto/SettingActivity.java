package home.loto;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;
import static home.loto.SettingsFragment.KEY_PREF_LANGUAGE;
/*This class represents application settings activity*/
public class SettingActivity extends AppCompatActivity {

    /*Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setLocale();

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
    /*Set preferred language*/
    public void setLocale() {

        Locale locale = getLocale(this);

        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());

    }
    /*Get preferred language*/
    public static Locale getLocale(Context context) {

        SharedPreferences sharedPref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        if(Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("iw") || Locale.getDefault().getLanguage().equals("ru"))
            return new Locale(sharedPref.getString(KEY_PREF_LANGUAGE,Locale.getDefault().getLanguage()));
        return new Locale(sharedPref.getString(KEY_PREF_LANGUAGE,"iw"));
    }

}