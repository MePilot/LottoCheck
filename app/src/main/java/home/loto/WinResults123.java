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
/*This class is intended for loading and storing the "123" game results from "Mifal A Pais" website*/
class WinResults123 {

    private ArrayList<_123Set> _123Sets;
    /*Constructor*/
    WinResults123()  {

        List<ArrayList<String>> strNumbers = new ArrayList<>();
        ArrayList<String> strID = new ArrayList<>();
        _123Sets=new ArrayList<>();
        Document doc;
        Elements els=null,elsID=null;

        int count = 0;
        int maxTries = 3;

        while(true) {
            try {
                doc = Jsoup.connect(Lotto.getInstance().getString(R.string.url_results_123)).get();
                els = doc.getElementsByClass("cat_data_info archive _123");
                elsID = doc.getElementsByClass("archive_list_block _123_number");
                break;
            } catch (IOException e) {
                if (++count == maxTries) break;
            }
        }

        if (elsID != null) {
            for(Element e:elsID) {

                //Collections.reverse(temp);
                strID.add(e.text().replaceAll("\\D+","").substring(0,4));
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
            _123Sets=null;
            return;
        }
        for (int i = 0; i< strID.size(); i++)
            _123Sets.add(new _123Set(strID.get(i), strNumbers.get(i)));

    }
    /*Get the list of 123 results*/
    ArrayList<_123Set> getCardSets () {
        return this._123Sets;
    }

}
