package home.loto;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/*This class is intended for loading and storing the "Chance" game results from "Mifal A Pais" website*/
class WinResultsCards  {
    private ArrayList<CardSet> cardSets;
    /*Constructor*/
    WinResultsCards()  {
        List<ArrayList<String>> strNumbers = new ArrayList<>();
        ArrayList<String> strID = new ArrayList<>();

        cardSets=new ArrayList<>();
        Document doc;
        Elements els=null,elsID=null;
        int count = 0;
        int maxTries = 3;

        while(true) {
            try {
                doc = Jsoup.connect( Lotto.getInstance().getString(R.string.url_results_chance)).get();
                els = doc.getElementsByClass("archive_list_block card");
                elsID = doc.getElementsByClass("archive_list_block chance_number");
                break;

            }
            catch (Exception e) {
                if (++count == maxTries) break;
            }
        }

        if (elsID != null) {
            for(Element e:elsID) {

                //Collections.reverse(temp);
                strID.add(e.text().replaceAll("\\D+",""));
            }
        }

        if (els != null) {
            for(Element e:els) {
                ArrayList<String> temp=new  ArrayList<>(Arrays.asList(e.text().split(" ")));
                Collections.reverse(temp);
                strNumbers.add(temp);
            }
        }
        else  {
            cardSets=null;
            return;
        }

        for (int i = 0; i< strID.size(); i++)
            cardSets.add(new CardSet(strID.get(i), strNumbers.get(i)));

    }
    /*Get the list of Chance results*/
    ArrayList<CardSet> getCardSets () {
        return this.cardSets;
    }

}
