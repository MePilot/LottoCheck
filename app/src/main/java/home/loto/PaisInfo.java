package home.loto;

import android.util.JsonReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*This class is intended to load all the necessary data from "Mifal A Pais" official website to the PaisInfo class.*/
final class PaisInfo {

    private static ArrayList<String> info_loto=null;
    private static ArrayList<String> info_chance=null;
    private static ArrayList<String> info_123=null;
    private static ArrayList<String> info_777=null;
    static int latestID=0, latest_777_ID=0;
    private static ArrayList<CardSet> cardSets=null;
    private static ArrayList<_123Set> _123Sets=null;
    /*Main function that initializes all fields of a class*/
    static boolean LoadPaisInfo() {
        WinResultsCards winResultsCards;
        WinResults123 winResults123;
        InfoLoto();

        if(info_loto!=null) InfoChance();
        else return false;

        if(info_chance!=null) Info123();
        else return false;

        if(info_123!=null) Info777();
        else return false;

        if(info_777!=null) {
            winResultsCards=new WinResultsCards();
            cardSets=winResultsCards.getCardSets();
        }
        else return false;

        if(cardSets!=null) {
            winResults123 = new WinResults123();
            _123Sets=winResults123.getCardSets();
        }
        else return false;

        return _123Sets != null;

    }
    /*Downloading all data related to lotto game*/
    private static void InfoLoto() {
        BufferedInputStream is;

        int count = 0;
        int maxTries = 3;
        boolean flag=true;
        String latest = getLatestLoto();

        if(latest==null) {info_loto=null; return;}

        while(true) {
            try {
                info_loto = new ArrayList<>();
                URL url = new URL(Lotto.getInstance().getString(R.string.url_date));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                is = new BufferedInputStream(con.getInputStream());

                JsonReader reader;
                reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));

                reader.beginArray();
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    switch (name) {
                        case "displayDate":
                        case "displayTime":
                        case "firstPrize":
                        case "secondPrize":
                            info_loto.add(reader.nextString());
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }
                reader.endObject();
                reader.endArray();
                con.disconnect();
                is.close();

                String data;

                Document doc;

                doc = Jsoup.connect(Lotto.getInstance().getString(R.string.url_stat) + latest).get();


                Element els = doc.getElementById("regularLottoTitle");


                data = els.text().replaceAll("[^0-9+, /]+", "");
                data = data.replaceAll(" {5}", "");


                info_loto.add(data);

                els = doc.getElementById("doubleLottoList");
                data=els.text().replaceAll("[^0-9+, /]+","");


                info_loto.add(data);

                Elements els3 = doc.getElementsByClass("cat_data_info").eq(1);

                info_loto.addAll(Arrays.asList(els3.text().split(" ")));
                Elements els4 = doc.getElementsByClass("loto_info_num strong").eq(0);
                info_loto.add(els4.text());
                info_loto.add(latest);

                for(String s:info_loto) {

                    if (s == null || s.isEmpty()) {
                        flag = false;
                        break;
                    }
                }

                if(info_loto.size()==14 && flag) break;
                else throw new Exception("Corrupt Data");

            } catch (Exception e) {
                info_loto = null;
                if (++count == maxTries) break;
            }
        }

    }
    /*Interface to lotto data*/
    static final class Loto {

        static  String getDate() {
            return info_loto.get(0);
        }

        static String getTime() {
            return info_loto.get(1);
        }
        static String getFirstPrize() {
            return info_loto.get(2);
        }

        static String getSecondPrize() {
            return info_loto.get(3);
        }
        static List<String> getNormalStat() {

            return Arrays.asList(info_loto.get(4).split(" {3}"));
        }


        static List<String> getDoubleStat() {

            return Arrays.asList(info_loto.get(5).split(" {3}"));
        }
        static String getLatestNum(int num) {
            return info_loto.get(num+6);
        }
        static String getLatestID() {

            return info_loto.get(13);
        }
    }
    /*Downloading all data related to chance game*/
    private static void InfoChance() {
        Document doc;
        String res;

        BufferedInputStream is;

        int count = 0;
        int maxTries = 3;
        boolean flag=true;

        while(true) {

            try {
            info_chance=new ArrayList<>();
            URL url = new URL(Lotto.getInstance().getString(R.string.url_date_chance));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(con.getInputStream());

            JsonReader reader;
            reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            reader.beginArray();
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "displayDate":
                    case "displayTime":
                        info_chance.add(reader.nextString());
                        break;

                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();
            reader.endArray();
            con.disconnect();
            is.close();

            doc = Jsoup.connect(Lotto.getInstance().getString(R.string.url_homepage_chance)).get();
            Elements els = doc.getElementsByClass("home_news_title category").eq(0);
            res=els.text().replaceAll("\\D+","");
            info_chance.add(res);

            Elements els2 = doc.getElementsByClass("cat_h_data_group chance").eq(0);

            info_chance.addAll(Arrays.asList(els2.text().split(" ")));

            for(String s:info_chance) {

                    if (s == null || s.isEmpty()) {
                        flag = false;
                        break;
                    }
            }

            if(info_chance.size()==7 && flag) break;
            else throw new Exception("Corrupt Data");

        } catch (Exception e) {
            info_chance=null;
            if (++count == maxTries) break;
        }
    }
    }
    /*Interface to chance data*/
    static class Chance {

        static String getDate() {
            return info_chance.get(0);
        }
        static String getTime() {
            return info_chance.get(1);
        }

        static String getLatestID() {
            return info_chance.get(2);
        }
        static String getLatestNum(int num) {
            return info_chance.get(num+3);
        }

        static  ArrayList<CardSet> getInfoChance() {
            return cardSets;
        }
    }


    /*Downloading all data related to 123 game*/
    private static void Info123() {
        String res;
        Document doc;

        BufferedInputStream is;
        int count = 0;
        int maxTries = 3;
        boolean flag=true;

        while(true) {
        try {
            info_123=new ArrayList<>();
            URL url = new URL(Lotto.getInstance().getString(R.string.url_date_123));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(con.getInputStream());

            JsonReader reader;
            reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            reader.beginArray();
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "displayDate":
                    case "displayTime":
                        info_123.add(reader.nextString());
                        break;

                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();
            reader.endArray();
            con.disconnect();
            is.close();


            doc = Jsoup.connect(Lotto.getInstance().getString(R.string.url_homepage_123)).get();
            Elements els = doc.getElementsByClass("home_news_title category").eq(0);
            res=els.text().substring(els.text().indexOf("מס' ")+4);
            info_123.add(res);


            Elements els2 = doc.getElementsByClass("cat_h_data_group").eq(0);

            info_123.addAll(Arrays.asList(els2.text().split(" ")));

            for(String s:info_123) {

                if (s == null || s.isEmpty()) {
                    flag = false;
                    break;
                }
            }

            if(info_123.size()==6 && flag) break;
            else throw new Exception("Corrupt Data");

        } catch (Exception e) {
            info_123=null;
            if (++count == maxTries) break;

        }
    }
    }
    /*Interface to 123 data*/
    static class _123 {

        static String getDate() {
            return info_123.get(0);
        }
        static String getTime() {
            return info_123.get(1);
        }

        static String getLatestID() {
            return info_123.get(2);
        }
        static String getLatestNum(int num) {
            return info_123.get(num+3);
        }
        static ArrayList<_123Set>  getInfo123() {
            return _123Sets;
        }
    }

    /*Downloading all data related to 777 game*/
    private static void  Info777() {
        BufferedInputStream is;

        int count = 0;
        int maxTries = 3;
        boolean flag=true;
        String latest=getLatest777();
        if(latest==null) {info_777=null; return;}
        while(true) {
        try {
            info_777 = new ArrayList<>();
            URL url = new URL(Lotto.getInstance().getString(R.string.url_date_777));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(con.getInputStream());

            JsonReader reader;
            reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            reader.beginArray();
            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "displayDate":
                    case "displayTime":
                        info_777.add(reader.nextString());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            reader.endArray();
            con.disconnect();
            is.close();

            String data;

            Document doc;

            doc = Jsoup.connect(Lotto.getInstance().getString(R.string.url_stat_777)+latest).get();
            Elements els = doc.getElementsByClass("current_loto_table extra");


            //data_temp=data.substring(data.indexOf("6+/6+"),data.lastIndexOf("6+/6+"));
            data = els.text().replaceAll("[^0-9, ₪]+", "");
            data = data.replaceAll(" {4}", "");


            info_777.add(data);
            doc = Jsoup.connect(Lotto.getInstance().getString(R.string.url_homepage_777)).get();
            els = doc.getElementsByClass("loto_info_num _777");

            info_777.add(latest);

            info_777.addAll(Arrays.asList(els.text().split(" ")));

            for(String s:info_777) {

                if (s == null || s.isEmpty()) {
                    flag = false;
                    break;
                }
            }

            if(info_777.size()==21 && flag) break;
            else throw new Exception("Corrupt Data");

        }

        catch (Exception e) {
            info_777=null;
            if (++count == maxTries) break;
        }
    }}
    /*Interface to 777 data*/
    static class _777 {

        static String getDate() {
            return info_777.get(0);
        }
        static String getTime() {
            return info_777.get(1);
        }

        static List<String> getStat() {

            return Arrays.asList(info_777.get(2).split(" {3}| ₪"));
        }
        static String getLatestID() {
            return info_777.get(3);
        }
        static String getLatestNum(int num) {
            return info_777.get(num+4);
        }

    }
    /*Getting the latest lotto game index*/
    private static String getLatestLoto() {

        String res;
        Document doc;
        int count = 0;
        int maxTries = 3;

        while(true) {
            try {

                doc = Jsoup.connect(Lotto.getInstance().getString(R.string.url_homepage)).get();
                Elements els = doc.getElementsByClass("home_news_title category").eq(0);

                res = els.text().replaceAll("\\D+", "");
                latestID = Integer.parseInt(res);
                break;

            } catch (Exception e) {
                if (++count == maxTries) return null;
            }
        }
        return  res;
    }
    /*Getting the latest 777 game index*/
    private static String getLatest777() {

        String res;
        Document doc;
        int count = 0;
        int maxTries = 3;

        while(true) {
            try {

                doc = Jsoup.connect(Lotto.getInstance().getString(R.string.url_homepage_777)).get();
                Elements els = doc.getElementsByClass("home_news_title category").eq(0);
                res = els.text().replaceAll("תוצאות הגרלת פיס 777 מס' ", "");

                latest_777_ID = Integer.parseInt(res);
                break;

            } catch (Exception e) {

                if (++count == maxTries) return null;

            }
        }
        return  res;
    }

}
