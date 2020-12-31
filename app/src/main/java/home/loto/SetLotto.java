package home.loto;

import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/*This class represents user's "Lotto" game ticket data*/
public class SetLotto implements Serializable {

    private Date date;
    private int ID, ExtraWin, prizeNormal, prizeDouble;
    private ArrayList<Integer> WinTables;
    private ArrayList<CombinationLotto> numbers;
    private boolean checked;
    private CombinationExtra Extra;
    private CombinationLotto Win;

    private List<Integer> matches = new ArrayList<>(Arrays.asList( 3,30,4,40,5,50,6,60));

    private Map<Integer, Integer> prizes = new HashMap<Integer, Integer>() {{

        put(60, 2);
        put(6, 5);
        put(50, 8);
        put(5, 11);
        put(40, 14);
        put(4, 17);
        put(30, 20);
        put(3, 23);
    }};
    private Map<Integer, Integer> prizesExtra = new HashMap<Integer, Integer>() {{

        put(2, 10);
        put(3, 15);
        put(4, 75);
        put(5, 1000);
        put(6, 60000);
    }};
    /*Constructor*/
     public SetLotto(int ID, Date date, ArrayList<CombinationLotto> numbers, ArrayList<Integer> WinTables, boolean checked, CombinationExtra Extra, int ExtraWin, CombinationLotto Win, int prizeNormal, int prizeDouble) {

        this.ID=ID;
        this.date=date;
        this.numbers=numbers;
        this.WinTables=WinTables;
        this.checked=checked;
        this.Extra=Extra;
        this.ExtraWin=ExtraWin;
        this.Win=Win;
        this.prizeNormal=prizeNormal;
        this.prizeDouble=prizeDouble;

        ArrayList<Integer> data = new ArrayList<>();
         for (CombinationLotto pNum:numbers)
            data.addAll(pNum.getNumbers());

     }
    /*Return ticket ID*/
    int getID() { return this.ID; }

    /*Return ticket date*/
    String getDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this.date);
     }
    /*Return normal lotto ticket prize*/
    int getPrizeNormal() {
        return this.prizeNormal;
    }

    /*Return double lotto ticket prize*/
    int getPrizeDouble() {
        return this.prizeDouble;
    }

    /*Decide if the ticket was checked*/
     boolean getChecked() {
         return this.checked;
     }

     /*Set ticket status to checked */
     void setChecked() {
       this.checked= true;
     }

     /*Get all ticket's combinations*/
    ArrayList<CombinationLotto> GetNumbers() {return this.numbers;}

    /*Get the counter array telling how much matched numbers is in each combination */
    ArrayList<Integer> getWinTable() { return this.WinTables; }

    /*Get lotto ticket's EXTRA combination*/
    CombinationExtra getExtra() { return this.Extra; }

    /*Get the winning combination for this game*/
    CombinationLotto getWin() { return this.Win; }

    /*Set the winning combination for this game*/
    void setWin(CombinationLotto Win) { this.Win=Win; }

    /*Set the EXTRA combination for this game*/
    void setExtra(CombinationExtra Extra) { this.Extra=Extra; }

    /*Set the EXTRA winning combination for this game*/
    void setExtraWin(int is) { this.ExtraWin=is; }

    /*Get EXTRA win combination for this ticket*/
    int getExtraWin() {

         return this.ExtraWin;
     }
    /*Decide if this ticket has at least one winning combination*/
    boolean hasWins() {

        return !Collections.disjoint(this.getWinTable(), matches)  || hasExtraWin();
    }
    /*Decide if this ticket plays the EXTRA game*/
    boolean playsExtra() {

        return getExtra().getDigit(0)!=0;
    }
    /*Decide if this ticket won the EXTRA game*/
    boolean hasExtraWin() {

        return  getExtraWin()>=2;
    }
    /*Decide if a specific combination on a ticket is a winning won*/
    boolean isWon(int i) {

        return matches.contains(this.getWinTable().get(i));
    }
    /*This class defines a separate thread trying to calculate the correct prize for the ticket based on prize list on official lotto website*/
    public class PrizeTable implements Runnable {
        private volatile List<String> info_loto_Prizes= new ArrayList<>();
        @Override
        public void run() {
            int count = 0;
            int maxTries = 3;

            while (true) {
                try {
                    String data;
                    Document doc;
                    doc = Jsoup.connect(Lotto.getInstance().getString(R.string.url_stat) + ID).get();

                    Element els = doc.getElementById("regularLottoTitle");

                    data = els.text().replaceAll("[^0-9+, /]+", "");
                    data = data.replaceAll(" {5}", "");

                    info_loto_Prizes.add(data);

                    els = doc.getElementById("doubleLottoList");
                    data = els.text().replaceAll("[^0-9+, /]+", "");

                    info_loto_Prizes.add(data);

                    break;

                } catch (Exception e) {

                    info_loto_Prizes = null;
                    if (++count == maxTries) break;
                }
            }
        }
        /*Interface*/
        List<String> getInfo_loto_Prizes_Normal() {
            try {
                return  Arrays.asList(info_loto_Prizes.get(0).split(" {3}"));
            } catch (Error e) {
                return  null;
            }
        }

        List<String> getInfo_loto_Prizes_Double() {
            try {
                return  Arrays.asList(info_loto_Prizes.get(1).split(" {3}"));
            } catch (Error e) {

                return  null;
            }
        }
    }
    /*Calculate and set the prize for this ticket*/
    void SetTotalPrize()  {

         try {

        PrizeTable table = new PrizeTable();

        Thread thread = new Thread(table);
        thread.start();

            thread.join();


        List<String> info_loto_Prizes1=table.getInfo_loto_Prizes_Normal();
        List<String> info_loto_Prizes2=table.getInfo_loto_Prizes_Double();


        int sumN=0,sumD=0;

        for (int i=0;i<numbers.size();i++) {
            if( matches.contains(WinTables.get(i))) {
                sumN+=Integer.parseInt(info_loto_Prizes1.get(prizes.get(WinTables.get(i))).replaceAll("\\D+",""));
                sumD+=Integer.parseInt(info_loto_Prizes2.get(prizes.get(WinTables.get(i))).replaceAll("\\D+",""));
            }
        }

        if(prizesExtra.containsKey(ExtraWin)) {
            sumN+=prizesExtra.get(ExtraWin);
            sumD+=prizesExtra.get(ExtraWin);
        }

        prizeNormal=sumN;
        prizeDouble=sumD;

        } catch (Error | Exception e) {

            prizeNormal=0;
            prizeDouble=0;
        }
    }
}
