package home.loto;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.JsonReader;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static home.loto.SettingsFragment.KEY_PREF_JACKPOT;
import static home.loto.SettingsFragment.KEY_PREF_NOTIFY_JACKPOT;
import static home.loto.SettingsFragment.KEY_PREF_NOTIFY_WIN;
/*This class is intended to periodically check tickets in the background and show notifications in case the user won at least in one game.
* Also, it shows notifications in case of big win prizes  */

public class MyWorker extends Worker {

    private Context context;
    /*Constructor*/
    public MyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        this.context=context;
    }

    @NonNull
    @Override
    /*Doing periodic work*/
    public Result doWork() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        if(prefs.getBoolean(KEY_PREF_NOTIFY_WIN, true)) {
            CheckWin();
        }

        if(prefs.getBoolean(KEY_PREF_NOTIFY_JACKPOT, true)) {
            CheckJackpot(Integer.parseInt(Objects.requireNonNull(prefs.getString(KEY_PREF_JACKPOT, "15000000"))));
        }

        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }
    /*Check the current jackpot and in case it is equal or bigger than set in options - show notification*/
    private void CheckJackpot(int sum) {
        final String url_date = context.getString(R.string.url_date);

        int jackpot = 0;
        int count = 0;
        int maxTries = 3;

        while(true) {
            try {

                URL url = new URL(url_date);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedInputStream is = new BufferedInputStream(con.getInputStream());

                JsonReader reader;
                reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));

                reader.beginArray();

                reader.beginObject();

                while (reader.hasNext()) {

                    if (reader.nextName().equals("firstPrize"))
                        jackpot = Integer.parseInt(reader.nextString());
                    else reader.skipValue();
                }
                reader.endObject();
                reader.endArray();

                is.close();
                con.disconnect();
                break;

            } catch (Exception e) {

                if (++count == maxTries) {return;}

            }
        }

        if (jackpot >= sum) {

            Intent i = new Intent(context, SplashScreen.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ccc")
                    .setSmallIcon(R.drawable.win)
                    .setContentTitle(context.getString(R.string.AppName))
                    .setContentText(context.getString(R.string.msg_jackpot, String.valueOf(jackpot)))
                    .setSound(uri)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(2, builder.build());
        }
    }
    /*Check the tickets stored in memory. In case of win - show notification*/
    private void CheckWin() {

        if (Check.CheckFromFile() || Check777.CheckFromFile()) {

            Intent i = new Intent(context, Lotto_List.class);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ccc")
                    .setSmallIcon(R.drawable.win)
                    .setContentTitle(context.getString(R.string.AppName))
                    .setContentText(context.getString(R.string.msg_win))
                    .setSound(uri)
                    //.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, builder.build());
        }
    }
}