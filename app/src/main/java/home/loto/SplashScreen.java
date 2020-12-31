package home.loto;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import java.lang.ref.WeakReference;
import java.util.Random;
/*This class represents a loading screen activity. It runs the process of pulling data from the "Mifal A Pais" website.*/
public class SplashScreen extends BaseActivity {

    static InterstitialAd mInterstitialAd;
    /*Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

    }
    /*Called when the activity is created. */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        mInterstitialAd= new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9196830139907982/4569232996");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        PullDataInfo mt = new PullDataInfo(this);
        mt.execute();
    }
    /*This class provides a separate thread for downloading all the necessary data from "Mifal A Pais" official website. */
    static class PullDataInfo extends AsyncTask<Void, Void, Void> {

        private WeakReference<SplashScreen> activityReference;
        private boolean flag;
        /*Constructor */
        PullDataInfo(SplashScreen context) {
            activityReference = new WeakReference<>(context);
        }
        /*Actions before thread execution */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        /*Doing work*/
        @Override
        protected Void doInBackground(Void... voids) {

            flag=PaisInfo.LoadPaisInfo();

            return null;
        }
        /*Actions after thread execution */
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            if (activityReference.get() == null || activityReference.get().isFinishing()) return;

            if(activityReference.get()!=null && flag) {

                Intent intent = new Intent(activityReference.get(), Lotto_Activity.class);
                //intent.putExtras(bundle);
                activityReference.get().startActivity(intent);
                activityReference.get().finish();

                Random rand = new Random();

                if (mInterstitialAd.isLoaded() && rand.nextInt(4)==0) {
                    mInterstitialAd.show();
                }
            }
            else if (activityReference.get()!=null) {

                TextView textErr;
                textErr=activityReference.get().findViewById(R.id.txtError);
                textErr.setVisibility(View.VISIBLE);

            }
        }
    }
}