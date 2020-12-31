package home.loto;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
/*This class is intended for loading and storing the "lotto" game results from "Mifal A Pais" website*/
class WinResultsLoto {
    private List<ArrayList<String>> strNumbers;
    private List<ArrayList<String>> strNumbersExtra;
    /*Constructor*/
    WinResultsLoto() {

        strNumbers= PWin();
        strNumbersExtra=PWinExtra();
    }

    /*Check if data is loaded*/
    boolean hasData() {
        return (strNumbers!=null && strNumbersExtra!=null);

    }
    /*Load all win results data from the website*/
    private List<ArrayList<String>> PWin() {

        List<ArrayList<String>> wNums=new ArrayList<>();
        BufferedInputStream is;
        int count = 0;
        int maxTries = 3;

        while(true) {
            try {
                URL url = new URL(Lotto.getInstance().getString(R.string.url_results_lotto));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                is = new BufferedInputStream(con.getInputStream());
                break;
            } catch (Exception e) {
                if (++count == maxTries) return null;

            }
        }
        Scanner scan = new Scanner(is);
        String tmpLine;

        while (scan.hasNext()) {

            tmpLine = scan.next();
            wNums.add(new  ArrayList<>(Arrays.asList(tmpLine.split(","))));
        }

        scan.close();

        return wNums;
    }
    /*Load all win results data from the website for EXTRA games*/
    private List<ArrayList<String>> PWinExtra() {
        List<ArrayList<String>> Extra = new ArrayList<>();

        Document doc;
        Elements els;
        int count = 0;
        int maxTries = 3;
        while(true) {
            try {
                doc = Jsoup.connect(Lotto.getInstance().getString(R.string.url_results_lotto_extra)).get();
                els = doc.getElementsByClass("cat_data_info archive");
                break;
            } catch (IOException e) {
                if (++count == maxTries) return null;
            }
        }
        if (els != null) {
            for(Element e:els) {
                ArrayList<String> temp=new  ArrayList<>(Arrays.asList(e.text().split(" ")));
                Collections.reverse(temp);
                Extra.add(temp);
            }
        }
        else  {

            return null;
        }

        return Extra;
    }
    /*Get ID of specific result*/
    int getPlayID(final int arrN) {

        return Integer.parseInt(strNumbers.get(arrN + 2).get(0));

    }
    /*Get specific win combination result*/
    List<String> getNumber(int arrN) {
        List<String> number = new ArrayList<>();

        for(int i=2;i<9;i++) number.add(strNumbers.get(arrN + 2).get(i));

        return number;
    }
    /*Get specific win combination result for EXTRA*/
    List<String> getNumberExtra(int arrN) {
        return strNumbersExtra.get(arrN);
    }

}
