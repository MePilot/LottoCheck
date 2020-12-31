package home.loto;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
/*This class provides functionality to write "lotto" user's tickets data from object to JSON file and initialize lotto object from JSON*/
public class RW {

    private Context ctx;
    private final static String DataFileName = "SavedNumbers_new";
    /*Constructor*/
    public RW(Context ctx) {
        this.ctx=ctx;
    }

    /*Reading the saved tickets from a JSON file*/
    public ArrayList<SetLotto> LoadPlaySets() {

        ArrayList<SetLotto> pSets = new ArrayList<>();
        InputStream inputStream;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        List<String> numbersStr,matchesStr;

        try {
            inputStream = ctx.openFileInput(DataFileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();

                JSONArray jArr = new JSONArray(stringBuilder.toString());

                for (int i=0; i < jArr.length(); i++) {

                    JSONObject obj = jArr.getJSONObject(i);
                    JSONArray jNumbers = obj.getJSONArray("Set");

                    ArrayList<CombinationLotto> numbers = new ArrayList<>();
                    CombinationExtra extra;
                    CombinationLotto Win;

                    for (int j=0; j < jNumbers .length(); j++) {
                        JSONObject objN = new JSONObject(jNumbers.getString(j));

                        numbersStr = Arrays.asList(objN.getString("Numbers").split(", "));
                        matchesStr = Arrays.asList(objN.getString("Matches").split(", "));

                        ArrayList<Integer> numbersInt = new ArrayList<>(lstStrToInt(numbersStr));
                        ArrayList<Integer> numbersIntMatches = new ArrayList<>(lstStrToInt(matchesStr));

                        numbers.add(new CombinationLotto(numbersInt, numbersIntMatches));
                    }
                    extra=new CombinationExtra(lstStrToInt(Arrays.asList(obj.getString("Extra").split(", "))),lstStrToInt(Arrays.asList(obj.getString("Extra_matches").split(", "))));
                    Win=new CombinationLotto(lstStrToInt(Arrays.asList(obj.getString("Win").split(", "))));
                    pSets.add(new SetLotto(obj.getInt("ID"),sdf.parse(obj.getString("Date")), numbers, lstStrToInt(Arrays.asList(obj.getString("WinTable").replace("[", "").replace("]", "").trim().split(", "))),obj.getBoolean("Checked"),extra, obj.getInt("ExtraWin"),Win,obj.getInt("PrizeNormal"),obj.getInt("PrizeDouble")));
                }

            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();

            return pSets;

        } catch (IOException | JSONException | ParseException e) {
            e.printStackTrace();
        }

          return  pSets;
    }
    /*Converting string array to integer array*/
    private ArrayList<Integer> lstStrToInt(List<String> lstStr) {

        ArrayList<Integer> lstInt = new ArrayList<>();
        for(String c:lstStr)
            lstInt.add(Integer.parseInt(c));
        return  lstInt;
    }
    /*Saving the tickets to a JSON file*/
     public void Save(ArrayList<SetLotto> lstPSet) {

         Collections.sort(lstPSet, new Comparator<SetLotto>() {
             @Override
             public int compare(SetLotto o1, SetLotto o2) {
                 return o2.getID()-o1.getID();
             }
         });

        FileOutputStream outputStream;

        JSONArray JIDS = new JSONArray();

        for(SetLotto pSet:lstPSet) {
            JSONObject obj = new JSONObject();
            JSONArray Jnumbers = new JSONArray();
            try {
                obj.put("ID", pSet.getID());
                obj.put("Date", pSet.getDate());

                for (CombinationLotto numbers : pSet.GetNumbers()) {
                    JSONObject objN = new JSONObject();
                    objN.put("Numbers", numbers.StringNumbers());
                    objN.put("Matches", numbers.StringMatches());
                    Jnumbers.put(objN);
                }
                obj.put("Set", Jnumbers);
                obj.put("WinTable", pSet.getWinTable());
                obj.put("Checked",pSet.getChecked());
                obj.put("Extra", pSet.getExtra().StringDigits());
                obj.put("Extra_matches", pSet.getExtra().StringMatches());
                obj.put("ExtraWin",pSet.getExtraWin());
                obj.put("Win",pSet.getWin().StringNumbers());
                obj.put("PrizeNormal",pSet.getPrizeNormal());
                obj.put("PrizeDouble",pSet.getPrizeDouble());
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            JIDS.put(obj);
        }

        try {
            outputStream = ctx.openFileOutput(DataFileName, Context.MODE_PRIVATE);
            outputStream.write(JIDS.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.close();
            Log.d("JIDS",JIDS.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
