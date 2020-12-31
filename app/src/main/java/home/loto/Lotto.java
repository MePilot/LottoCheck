package home.loto;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.os.Build;
import androidx.preference.PreferenceManager;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.google.android.gms.ads.MobileAds;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import static home.loto.SettingsFragment.KEY_PREF_NOTIFY_JACKPOT;
import static home.loto.SettingsFragment.KEY_PREF_NOTIFY_WIN;
/*This class is the entry point of application*/
public class Lotto extends Application {
    private static Lotto mInstance;
    private static Resources res;
    public static String defLocale;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        res = getResources();
        /*Initialize Ads*/
        MobileAds.initialize(this, "ca-app-pub-9196830139907982~5693500649");

        createNotificationChannel();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        rS(prefs.getBoolean(KEY_PREF_NOTIFY_WIN, true) || prefs.getBoolean(KEY_PREF_NOTIFY_JACKPOT, true));

        defLocale= Locale.getDefault().getLanguage();
    }

    private void rS(boolean is) {
        /*Set the periodic background checks for user's lottery tickets if this option is set in preferences*/
    Constraints constraints = new Constraints.Builder()
            // The Worker needs Network connectivity
            //.setRequiredNetworkType(NetworkType.CONNECTED)
            // Needs the device to be charging
            //.setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)

            .build();

    PeriodicWorkRequest request =
            // Executes MyWorker every 15 minutes
            new PeriodicWorkRequest.Builder(MyWorker.class, 24*3, TimeUnit.HOURS)
                    .addTag("bla")
                    .setConstraints(constraints)
                    .build();


        if(is)
            WorkManager.getInstance(this).enqueueUniquePeriodicWork("Check", ExistingPeriodicWorkPolicy.KEEP,request);
        else
            WorkManager.getInstance(this).cancelWorkById(request.getId());

    }
    /*Get application context*/
    public static Lotto getInstance() {
        return mInstance;
    }
    /*Get application resources*/
    public static Resources getResourses() {
        return res;
    }
    /*Create channel to enable notifications on phone*/
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = getString(R.string.channel_name);

            NotificationChannel channel = new NotificationChannel("ccc", name, NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);

            channel.setVibrationPattern(new long[]{0, 300, 0, 400, 0, 500});
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .build();
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), aa);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {

                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}