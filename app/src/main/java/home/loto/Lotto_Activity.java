package home.loto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import home.ocrreader.OcrCaptureActivity;
import home.ocrreader.OcrCaptureActivity777;

import static home.loto.SettingsFragment.KEY_PREF_JACKPOT;
import static home.loto.SettingsFragment.KEY_PREF_MAIN;
import static home.loto.SettingsFragment.KEY_PREF_NEW_VERSION;

/*This class represents "lotto" main screen*/
public class Lotto_Activity extends BaseActivity {
    TextView txtLastRes;
    Button btnLotto,btnChance,btn123,btn777;
    TextView txtChance;
    TextView card1, card2, card3, card4;
    TextView txtLastResChance;
    TextView txt123;
    TextView txtLastRes123;
    TextView txtLastRes777;
    TextView txtLastResLoto;
    TextView txtLatestRes777;
    Button btnPlay;
    Button btnManager;
    TextView txtDig1,txtDig2,txtDig3;
    TextView txtNum1,txtNum2,txtNum3,txtNum4,txtNum5,txtNum6,txtNumStrong;
    TextView txt777Num1,txt777Num2,txt777Num3,txt777Num4,txt777Num5,txt777Num6,txt777Num7,txt777Num8,txt777Num9,txt777Num10,txt777Num11,txt777Num12,txt777Num13,txt777Num14,txt777Num15,txt777Num16,txt777Num17;
    CardView cardView;
    CardView cardChance;
    CardView card123;
    CardView card777;
    CardView cardLotoStat;
    CardView card777Stat;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    RW rw;
    RW777 rw777;

   static String Sdiff, Sdiff777;

    private ArrayList<CardSet> cardSets=null;
    private ArrayList<_123Set> _123Sets=null;
    /*Auxiliary function*/
    private static int ordinalIndexOf(String str, int n) {
       int pos = -1;
       do {
           pos = str.indexOf("\n", pos + 1);
       }
       while (n-- > 0 && pos != -1);
       return pos;
   }
    /*Activity start function*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lotto);

        AdView adView = findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnLotto=findViewById(R.id.btnLotto);
        btnChance=findViewById(R.id.btnChance);
        btn123=findViewById(R.id.btn123);
        btn777=findViewById(R.id.btn777);

        cardView =  findViewById(R.id.cardLotto);
        cardChance =  findViewById(R.id.cardChance);
        card123 =  findViewById(R.id.card123);
        card777 =  findViewById(R.id.card777);
        cardLotoStat=findViewById(R.id.cardLottoStat);
        card777Stat=findViewById(R.id.card777stat);
        txtLastRes =  cardView.findViewById(R.id.txtNextInfo);


        txtNum1=cardView.findViewById(R.id.txtNum1);
        txtNum2=cardView.findViewById(R.id.txtNum2);
        txtNum3=cardView.findViewById(R.id.txtNum3);
        txtNum4=cardView.findViewById(R.id.txtNum4);
        txtNum5=cardView.findViewById(R.id.txtNum5);
        txtNum6=cardView.findViewById(R.id.txtNum6);
        txtNumStrong=cardView.findViewById(R.id.txtNumStrong);
        txtLastResLoto=cardView.findViewById(R.id.lotoLastRes);

        txt777Num1=card777.findViewById(R.id.txt777Num1);
        txt777Num2=card777.findViewById(R.id.txt777Num2);
        txt777Num3=card777.findViewById(R.id.txt777Num3);
        txt777Num4=card777.findViewById(R.id.txt777Num4);
        txt777Num5=card777.findViewById(R.id.txt777Num5);
        txt777Num6=card777.findViewById(R.id.txt777Num6);
        txt777Num7=card777.findViewById(R.id.txt777Num7);
        txt777Num8=card777.findViewById(R.id.txt777Num8);
        txt777Num9=card777.findViewById(R.id.txt777Num9);
        txt777Num10=card777.findViewById(R.id.txt777Num10);
        txt777Num11=card777.findViewById(R.id.txt777Num11);
        txt777Num12=card777.findViewById(R.id.txt777Num12);
        txt777Num13=card777.findViewById(R.id.txt777Num13);
        txt777Num14=card777.findViewById(R.id.txt777Num14);
        txt777Num15=card777.findViewById(R.id.txt777Num15);
        txt777Num16=card777.findViewById(R.id.txt777Num16);
        txt777Num17=card777.findViewById(R.id.txt777Num17);
        txtLatestRes777=card777.findViewById(R.id.txtLastRes777);

        txtChance =  cardChance.findViewById(R.id.txtNextChanceInfo);
        txtLastResChance=cardChance.findViewById(R.id.txtLastResChance);
        card1 =  cardChance.findViewById(R.id.card1_lbl);
        card2 =  cardChance.findViewById(R.id.card2_lbl);
        card3 =  cardChance.findViewById(R.id.card3_lbl);
        card4 =  cardChance.findViewById(R.id.card4_lbl);

        txt123 =  card123.findViewById(R.id.txtNext123Info);
        txtLastRes123 = card123.findViewById(R.id.txtLastRes123);

        txtDig1=card123.findViewById(R.id.txtDig1);
        txtDig2=card123.findViewById(R.id.txtDig2);
        txtDig3=card123.findViewById(R.id.txtDig3);

        txtLastRes777 =  card777.findViewById(R.id.txtNext777Info);

        btnPlay= cardView.findViewById(R.id.btnPlay);
        btnManager= cardView.findViewById(R.id.btnManager);

        rw = new RW( getApplicationContext());
        rw777 = new RW777( getApplicationContext());

        txtLastRes.setText(loto_info());

        txtNum1.setText(PaisInfo.Loto.getLatestNum(0));
        txtNum2.setText(PaisInfo.Loto.getLatestNum(1));
        txtNum3.setText(PaisInfo.Loto.getLatestNum(2));
        txtNum4.setText(PaisInfo.Loto.getLatestNum(3));
        txtNum5.setText(PaisInfo.Loto.getLatestNum(4));
        txtNum6.setText(PaisInfo.Loto.getLatestNum(5));
        txtNumStrong.setText(PaisInfo.Loto.getLatestNum(6));

        txtLastResLoto.setText(getString(R.string.txt_lotto_chance_res, PaisInfo.Loto.getLatestID()));

        txtChance.setText(chance_info());
        txtLastResChance.setText(getString(R.string.txt_lotto_chance_res, PaisInfo.Chance.getLatestID()));

        card1.setText(PaisInfo.Chance.getLatestNum(3));
        card2.setText(PaisInfo.Chance.getLatestNum(2));
        card3.setText(PaisInfo.Chance.getLatestNum(1));
        card4.setText(PaisInfo.Chance.getLatestNum(0));

        txt123.setText(_123_info());
        txtLastRes123.setText(getString(R.string.txt_lotto_chance_res,PaisInfo._123.getLatestID()));
        txtDig1.setText(PaisInfo._123.getLatestNum(2));
        txtDig2.setText(PaisInfo._123.getLatestNum(1));
        txtDig3.setText(PaisInfo._123.getLatestNum(0));

        txtLastRes777.setText(_777_info());

        txt777Num1.setText(PaisInfo._777.getLatestNum(0));
        txt777Num2.setText(PaisInfo._777.getLatestNum(1));
        txt777Num3.setText(PaisInfo._777.getLatestNum(2));
        txt777Num4.setText(PaisInfo._777.getLatestNum(3));
        txt777Num5.setText(PaisInfo._777.getLatestNum(4));
        txt777Num6.setText(PaisInfo._777.getLatestNum(5));
        txt777Num7.setText(PaisInfo._777.getLatestNum(6));
        txt777Num8.setText(PaisInfo._777.getLatestNum(7));
        txt777Num9.setText(PaisInfo._777.getLatestNum(8));
        txt777Num10.setText(PaisInfo._777.getLatestNum(9));
        txt777Num11.setText(PaisInfo._777.getLatestNum(10));
        txt777Num12.setText(PaisInfo._777.getLatestNum(11));
        txt777Num13.setText(PaisInfo._777.getLatestNum(12));
        txt777Num14.setText(PaisInfo._777.getLatestNum(13));
        txt777Num15.setText(PaisInfo._777.getLatestNum(14));
        txt777Num16.setText(PaisInfo._777.getLatestNum(15));
        txt777Num17.setText(PaisInfo._777.getLatestNum(16));
        txtLatestRes777.setText(getString(R.string.txt_lotto_chance_res,PaisInfo._777.getLatestID()));

        int main=prefs.getInt(KEY_PREF_MAIN,0);

        switch (main) {

            case 0:
                cardView.setVisibility(View.VISIBLE);
                btnLotto.setAlpha(1.0f);
                break;
            case 1:
                cardChance.setVisibility(View.VISIBLE);
                btnChance.setAlpha(1.0f);
                break;
            case 2:
                card123.setVisibility(View.VISIBLE);
                btn123.setAlpha(1.0f);
                break;
            case 3:
                card777.setVisibility(View.VISIBLE);
                btn777.setAlpha(1.0f);
                break;
                default:
                    cardView.setVisibility(View.VISIBLE);
        }

        LoadStat();
        LoadStat777();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if( prefs.getBoolean(KEY_PREF_NEW_VERSION, true))  {
            Intent i = new Intent(this,FragmentPagerSupport.class);
            startActivity(i);
            prefs.edit().putBoolean(KEY_PREF_NEW_VERSION, false).apply();
        }
    }
    /*Return the decorated string consisting of "lotto" information*/
   private SpannableString loto_info() {
       Calendar cal = Calendar.getInstance(TimeZone.getDefault());
       Date currentTime = cal.getTime();
       SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.format_date2),Locale.getDefault());
       Date convertedDate = new Date();

       try {
           convertedDate = dateFormat.parse(PaisInfo.Loto.getDate()+ " " + PaisInfo.Loto.getTime());

       } catch (ParseException e) {

           e.printStackTrace();
       }

       long diffInMillies = Math.abs(convertedDate.getTime() - currentTime.getTime());
       long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

       Sdiff=diff+getString(R.string.txt_lotto_days);
       if (diff==0) {diff= TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS); Sdiff=diff+getString(R.string.txt_lotto_hours);}
       if (diff==0) {diff= TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS); Sdiff=diff+getString(R.string.txt_lotto_mins);}


       String text = getString(R.string.Next_lotto_info2, NumberFormat.getNumberInstance(Locale.ENGLISH).format(Integer.parseInt(PaisInfo.Loto.getFirstPrize())),NumberFormat.getNumberInstance(Locale.ENGLISH).format(Integer.parseInt(PaisInfo.Loto.getSecondPrize())),PaisInfo.Loto.getDate(),PaisInfo.Loto.getTime(),Sdiff);
       SpannableString spanString = new SpannableString(text);
       prefs = PreferenceManager.getDefaultSharedPreferences(this);


       if(Integer.parseInt(PaisInfo.Loto.getFirstPrize())>= Integer.parseInt(Objects.requireNonNull(prefs.getString(KEY_PREF_JACKPOT, "15000000")))) {
           spanString.setSpan(new ForegroundColorSpan(Color.RED), ordinalIndexOf(spanString.toString(), 0), ordinalIndexOf(spanString.toString(), 1)+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
           spanString.setSpan(new StyleSpan(Typeface.BOLD), ordinalIndexOf(spanString.toString(), 0), ordinalIndexOf(spanString.toString(), 1)+1, 0);

       }

       if(Sdiff.contains(getString(R.string.txt_lotto_hours)) || Sdiff.contains(getString(R.string.txt_lotto_mins))) {

           spanString.setSpan(new ForegroundColorSpan(Color.RED),  ordinalIndexOf(spanString.toString(), 3), spanString.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
           spanString.setSpan(new StyleSpan(Typeface.BOLD),ordinalIndexOf(spanString.toString(), 3),spanString.toString().length(), 0);
       }

        return spanString;
   }
    /*Return the decorated string consisting of "chance" game information*/
    private SpannableString chance_info() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date currentTime = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.format_date2),Locale.getDefault());
        Date convertedDate = new Date();
        String Sdiff;
        try {
            convertedDate = dateFormat.parse(PaisInfo.Chance.getDate() + " "+ PaisInfo.Chance.getTime());

        } catch (ParseException e) {

            e.printStackTrace();
        }

        long diffInMillies = Math.abs(convertedDate.getTime() - currentTime.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        Sdiff=diff+getString(R.string.txt_lotto_days);
        if (diff==0) {diff= TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS); Sdiff=diff+getString(R.string.txt_lotto_hours);}
        if (diff==0) {diff= TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS); Sdiff=diff+getString(R.string.txt_lotto_mins);}


        String text = getString(R.string.Next_chance_info, PaisInfo.Chance.getDate(),PaisInfo.Chance.getTime(),Sdiff);
        SpannableString spanString = new SpannableString(text);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(Sdiff.contains(getString(R.string.txt_lotto_hours)) || Sdiff.contains(getString(R.string.txt_lotto_mins))) {

            spanString.setSpan(new ForegroundColorSpan(Color.RED),  ordinalIndexOf(spanString.toString(), 1), spanString.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new StyleSpan(Typeface.BOLD),ordinalIndexOf(spanString.toString(), 1),spanString.toString().length(), 0);
        }

        return spanString;
    }
    /*Return the decorated string consisting of "123" game information*/
    private SpannableString _123_info() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date currentTime = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.format_date2),Locale.getDefault());
        Date convertedDate = new Date();
        String Sdiff;
        try {
            convertedDate = dateFormat.parse(PaisInfo._123.getDate() + " " + PaisInfo._123.getTime());

        } catch (ParseException e) {

            e.printStackTrace();
        }

        long diffInMillies = Math.abs(convertedDate.getTime() - currentTime.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        Sdiff=diff+getString(R.string.txt_lotto_days);
        if (diff==0) {diff= TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS); Sdiff=diff+getString(R.string.txt_lotto_hours);}
        if (diff==0) {diff= TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS); Sdiff=diff+getString(R.string.txt_lotto_mins);}


        String text = getString(R.string.Next_chance_info, PaisInfo._123.getDate(),PaisInfo._123.getTime(),Sdiff);
        SpannableString spanString = new SpannableString(text);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(Sdiff.contains(getString(R.string.txt_lotto_hours)) || Sdiff.contains(getString(R.string.txt_lotto_mins))) {

            spanString.setSpan(new ForegroundColorSpan(Color.RED),  ordinalIndexOf(spanString.toString(), 1), spanString.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new StyleSpan(Typeface.BOLD),ordinalIndexOf(spanString.toString(), 1),spanString.toString().length(), 0);
        }

        return spanString;
    }
    /*Return the decorated string consisting of "777" game information*/
    private SpannableString _777_info() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date currentTime = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.format_date2),Locale.getDefault());
        Date convertedDate = new Date();

        try {
            convertedDate = dateFormat.parse(PaisInfo._777.getDate() + " " + PaisInfo._777.getTime());

        } catch (ParseException e) {

            e.printStackTrace();
        }

        long diffInMillies = Math.abs(convertedDate.getTime() - currentTime.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        Sdiff777=diff+getString(R.string.txt_lotto_days);
        if (diff==0) {diff= TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS); Sdiff777=diff+getString(R.string.txt_lotto_hours);}
        if (diff==0) {diff= TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS); Sdiff777=diff+getString(R.string.txt_lotto_mins);}

        String text = getString(R.string.Next_chance_info, PaisInfo._777.getDate(), PaisInfo._777.getTime(),Sdiff777);
        SpannableString spanString = new SpannableString(text);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(Sdiff777.contains(getString(R.string.txt_lotto_hours)) || Sdiff777.contains(getString(R.string.txt_lotto_mins))) {

            spanString.setSpan(new ForegroundColorSpan(Color.RED),  ordinalIndexOf(spanString.toString(), 1), spanString.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new StyleSpan(Typeface.BOLD),ordinalIndexOf(spanString.toString(), 1),spanString.toString().length(), 0);
        }

        return spanString;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_help){
            Intent i = new Intent(this,FragmentPagerSupport.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }
    /*Activity buttons code*/
    public void Play (View v) {
        Intent intent = new Intent(getApplicationContext(), Lotto_Generator2.class);
        //intent.putExtra("latestID",latestID);
        startActivity(intent);

    }


    public void OCR(View v) {

        Intent intent = new Intent(this, OcrCaptureActivity.class);

        startActivity(intent);

    }
    public void OCR777(View view) {
        Intent intent = new Intent(this, OcrCaptureActivity777.class);

        startActivity(intent);
    }
    public void MyGames (View v) {

        Intent intent = new Intent(getApplicationContext(), Lotto_List.class);
        startActivity(intent);
    }
    public void MyGames777(View view) {

        Intent intent = new Intent(getApplicationContext(), Lotto_List777.class);
        startActivity(intent);

    }
    public void show_lotto (View v) {
        editor = prefs.edit();
        editor.putInt(KEY_PREF_MAIN, 0);
        editor.apply();
        cardView.setVisibility(View.VISIBLE);
        cardLotoStat.setVisibility(View.GONE);
        card777Stat.setVisibility(View.GONE);
        card123.setVisibility(View.GONE);
        cardChance.setVisibility(View.GONE);
        card777.setVisibility(View.GONE);
        v.setAlpha(1.0f);
        btnChance.setAlpha(0.4f);
        btn123.setAlpha(0.4f);
        btn777.setAlpha(0.4f);
    }

    public void show_chance (View v) {
        editor = prefs.edit();
        editor.putInt(KEY_PREF_MAIN, 1);
        editor.apply();
        cardChance.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
        card123.setVisibility(View.GONE);
        card777.setVisibility(View.GONE);
        cardLotoStat.setVisibility(View.GONE);
        card777Stat.setVisibility(View.GONE);
        v.setAlpha(1.0f);
        btnLotto.setAlpha(0.4f);
        btn123.setAlpha(0.4f);
        btn777.setAlpha(0.4f);
    }

    public void show_123 (View v) {
        editor = prefs.edit();
        editor.putInt(KEY_PREF_MAIN, 2);
        editor.apply();
        card123.setVisibility(View.VISIBLE);
        cardChance.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);
        card777.setVisibility(View.GONE);
        cardLotoStat.setVisibility(View.GONE);
        card777Stat.setVisibility(View.GONE);
        v.setAlpha(1.0f);
        btnLotto.setAlpha(0.4f);
        btnChance.setAlpha(0.4f);
        btn777.setAlpha(0.4f);
    }
    public void show_777 (View v) {
        editor = prefs.edit();
        editor.putInt(KEY_PREF_MAIN, 3);
        editor.apply();
        card777.setVisibility(View.VISIBLE);
        card123.setVisibility(View.GONE);
        cardChance.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);
        cardLotoStat.setVisibility(View.GONE);
        card777Stat.setVisibility(View.GONE);
        v.setAlpha(1.0f);
        btnLotto.setAlpha(0.4f);
        btnChance.setAlpha(0.4f);
        btn123.setAlpha(0.4f);
    }





    public void CardsResults(View view) {
        Intent intent = new Intent(this, Lotto_ListCards.class);
        intent.putExtra("InfoCards", cardSets);
        startActivity(intent);

    }

    public void _123Results(View view) {

        Intent intent = new Intent(this, Lotto_List123.class);
        intent.putExtra("Info_123", _123Sets);
        startActivity(intent);

    }
    /*Fill the activity table with lotto game statistics*/
    public void LoadStat() {

        TableLayout tableLayout, tableLayout2;

        tableLayout=findViewById(R.id.tableStat);
        tableLayout2=findViewById(R.id.tableStatDouble);

        TableRow tableRowHeader = new TableRow(this);
        TableRow tableRowHeader2 = new TableRow(this);
        TextView textHeader = new TextView(this);
        TextView textHeader2 = new TextView(this);
        TextView textHeader3 = new TextView(this);

        TextView textHeader4 = new TextView(this);
        TextView textHeader5 = new TextView(this);
        TextView textHeader6 = new TextView(this);

        textHeader.setText(getString(R.string.txt_lotto_stat_guess));
        textHeader.setTextColor(getResources().getColor(R.color.blue));
        textHeader.setGravity(Gravity.CENTER_HORIZONTAL);

        textHeader2.setText(getString(R.string.txt_lotto_stat_prizes));
        textHeader2.setTextColor(getResources().getColor(R.color.blue));
        textHeader2.setGravity(Gravity.CENTER_HORIZONTAL);
        textHeader3.setText(getString(R.string.txt_lotto_stat_sum));

        textHeader3.setTextColor(getResources().getColor(R.color.blue));
        textHeader3.setGravity(Gravity.CENTER_HORIZONTAL);

        tableRowHeader.addView(textHeader);
        tableRowHeader.addView(textHeader2);
        tableRowHeader.addView(textHeader3);

        tableLayout.addView(tableRowHeader);

        for (int i = 0; i < 8; i++) {
            TableRow tableRow = new TableRow(this);

            for (int j = 0; j < 3; j++) {
                TextView textView;
                textView = new TextView(this);
                if(j==0) textView.setText(getResources().getStringArray(R.array.txt_lotto_stat_type2)[i]);
                else
                    textView.setText(PaisInfo.Loto.getNormalStat().get(i * 3 + j));
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setPadding(20, 0, 20, 0);
                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);
        }
        textHeader4.setText(getString(R.string.txt_lotto_stat_guess));
        textHeader4.setTextColor(getResources().getColor(R.color.blue));
        textHeader4.setGravity(Gravity.CENTER_HORIZONTAL);

        textHeader5.setText(getString(R.string.txt_lotto_stat_prizes));
        textHeader5.setTextColor(getResources().getColor(R.color.blue));
        textHeader5.setGravity(Gravity.CENTER_HORIZONTAL);

        textHeader6.setText(getString(R.string.txt_lotto_stat_sum));

        textHeader6.setTextColor(getResources().getColor(R.color.blue));
        textHeader6.setGravity(Gravity.CENTER_HORIZONTAL);

        tableRowHeader2.addView(textHeader4);
        tableRowHeader2.addView(textHeader5);
        tableRowHeader2.addView(textHeader6);
        tableLayout2.addView(tableRowHeader2);
        for (int i = 0; i < 8; i++) {
            TableRow tableRow = new TableRow(this);

            for (int j = 0; j < 3; j++) {

                TextView textView;
                textView = new TextView(this);
                if(j==0) textView.setText(getResources().getStringArray(R.array.txt_lotto_stat_type2)[i]);
                else
                    textView.setText(PaisInfo.Loto.getDoubleStat().get(i * 3 + j));

                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setPadding(20, 0, 20, 0);
                tableRow.addView(textView);

            }
            tableLayout2.addView(tableRow);

        }
    }

    public void LoadStat777() {
        TableLayout tableLayout;

        tableLayout=findViewById(R.id.tableStat777);


        TableRow tableRowHeader = new TableRow(this);

        TextView textHeader = new TextView(this);
        TextView textHeader2 = new TextView(this);
        TextView textHeader3 = new TextView(this);

        textHeader.setText(getString(R.string.txt_lotto_stat_type));
        textHeader.setTextColor(getResources().getColor(R.color.blue));
        textHeader.setGravity(Gravity.CENTER_HORIZONTAL);

        textHeader2.setText(getString(R.string.txt_lotto_stat_prizes));
        textHeader2.setTextColor(getResources().getColor(R.color.blue));
        textHeader2.setGravity(Gravity.CENTER_HORIZONTAL);
        textHeader3.setText(getString(R.string.txt_lotto_stat_sum));

        textHeader3.setTextColor(getResources().getColor(R.color.blue));
        textHeader3.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRowHeader.addView(textHeader);
        tableRowHeader.addView(textHeader2);
        tableRowHeader.addView(textHeader3);
        tableLayout.addView(tableRowHeader);

        for (int i = 0; i < 6; i++) {
            TableRow tableRow = new TableRow(this);

            for (int j = 0; j < 3; j++) {

                TextView textView;
                textView = new TextView(this);

                if(j==0) textView.setText(getResources().getStringArray(R.array.txt_lotto_stat_type)[i]);
                else
                    textView.setText(PaisInfo._777.getStat().get(i * 2 + j));

                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setPadding(20, 0, 20, 0);
                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);
        }
    }

    public void BackToLoto(View view) {
        cardView.setVisibility(View.VISIBLE);
        cardLotoStat.setVisibility(View.GONE);
    }

    public void ShowStat(View view) {

        cardView.setVisibility(View.GONE);
        cardLotoStat.setVisibility(View.VISIBLE);
    }

    public void ShowStat777(View view) {
        card777.setVisibility(View.GONE);
        card777Stat.setVisibility(View.VISIBLE);
    }

    public void BackTo777(View view) {
        card777.setVisibility(View.VISIBLE);
        card777Stat.setVisibility(View.GONE);
    }
}