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
/*This class is intended for loading and storing the "777" game results from "Mifal A Pais" website*/
class WinResults777 {
    private List<ArrayList<String>> strNumbers;
    private ArrayList<String> strID;

    /*Constructor*/
    WinResults777()  {
        strNumbers=new ArrayList<>();
        strID=new ArrayList<>();

        Document doc;
        Elements els=null,elsID=null;
        int count = 0;
        int maxTries = 3;

        while(true) {

            try {
                doc = Jsoup.connect(Lotto.getInstance().getString(R.string.url_results777)).get();
                els = doc.getElementsByClass("cat_data_info archive _777");
                elsID = doc.getElementsByClass("archive_list_block lotto_number");
            break;
            }
            catch (IOException e) {
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

            this.strID = null;

            this.strNumbers = null;
        }

    }
    /*Check if data is loaded*/
    boolean hasData() {
        return (strNumbers!=null && strID!=null);
    }
    /*Get ID of specific result*/
    int getPlayID(int arrN) {
        return Integer.parseInt(strID.get(arrN));
    }

    /*Get specific win combination result*/
    List<String> getNumber(int arrN) {
        return strNumbers.get(arrN);
    }
    /*Get amount of win results*/
    int getResultsAmount() {

        return strNumbers.size();
    }
}
