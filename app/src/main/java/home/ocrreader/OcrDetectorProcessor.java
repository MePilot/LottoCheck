/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package home.ocrreader;
import android.util.SparseArray;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.Line;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import home.loto.CombinationExtra;
import home.loto.CombinationLotto;
import home.loto.Lotto;
import home.loto.R;
import home.loto.SetLotto;
import home.ocrreader.ui.camera.GraphicOverlay;

/**
 * A very simple Processor which receives detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
public class OcrDetectorProcessor extends Observable implements Detector.Processor<TextBlock> {

    private ArrayList<Integer> WinTables = new ArrayList<>();
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    private  Date date=null;
    private ArrayList<CombinationLotto> pNums=new ArrayList<>();
    private int ID=0;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
    private final Pattern pattern = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.]\\d\\d");
    private final Pattern patternExtra = Pattern.compile("^([1-7][1-7][1-7][1-7][1-7][1-7])$");
    private final Pattern patternNums = Pattern.compile("^(([0][1-9]|[12][0-9]|3[0-7])([0][1-9]|[12][0-9]|3[0-7])([0][1-9]|[12][0-9]|3[0-7])([0][1-9]|[12][0-9]|3[0-7])([0][1-9]|[12][0-9]|3[0-7])([0][1-9]|[12][0-9]|3[0-7])([1-7])([0-1]?[0-9]))$");
    private CombinationExtra extra=null;
    private OcrGraphic graphic;
    private boolean Extra=false;
    private int amount=0;
    private Observer observer;

    /*Constructor*/
    OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay,Observer observer) {
        this.observer=observer;
        mGraphicOverlay = ocrGraphicOverlay;
        addObserver(observer);
    }
    /*Tell the processor to scan EXTRA combination*/
    void setExtra(boolean Extra) {this.Extra=Extra;}

    /*Tell the processor the amount of combinations it must scan*/
    synchronized void setAmount(int amount) {this.amount=amount;}
    /**
     * Called by the detector to deliver detection results.
     * If your application called for it, this could be a place to check for
     * equivalent detections by tracking TextBlocks that are similar in location and content from
     * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
     * multiple detections.
     */
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {

        mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();

        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);

            if (ID == 0 || date == null) {
                checkID2(item);
                if (ID != 0 && date != null) {
                    graphic = new OcrGraphic(mGraphicOverlay, item, Lotto.getResourses().getString(R.string.txt_ocr_date_received));
                    mGraphicOverlay.add(graphic);
                    setChanged();
                    notifyObservers("gotData");
                }
            }
            if (Extra && extra == null) {
                extra = checkExtra(item);
                if (extra != null) {
                    setChanged();
                    notifyObservers("gotData");
                }
            }

            if (pNums.size()!=amount) {
                checkNumbers(item);
                if(pNums.size()==amount) {

                    setChanged();
                    notifyObservers("gotData");
                }
            }
        }

        if (ID != 0 && date != null && pNums.size()==amount && (!Extra || extra != null)) {
            setChanged();

            if (Extra) {
                notifyObservers(new SetLotto(ID, date, pNums, WinTables, false, extra,0, new CombinationLotto(),0,0));
                release();
            }
            else {
                notifyObservers(new SetLotto(ID, date, pNums, WinTables, false, new CombinationExtra(),0, new CombinationLotto(),0,0));
                release();
            }
            if (observer != null) {
                deleteObserver(observer);
            }
        }

    }
    /*Detecting lotto ticket combinations within general scanned text*/
    private void checkNumbers (TextBlock item) {

        Matcher matcher;
        ArrayList<String> res = new ArrayList<>();
        List<? extends Text> components = item.getComponents();

        for (Text component : components) {
            String value = component.getValue();

            if (component instanceof Line && value != null && !value.isEmpty()) {
                value = value.substring(1).replaceAll("\\D", "");
                matcher = patternNums.matcher(value);


                if (matcher.find()) {
                    res.add(matcher.group());
                    graphic = new OcrGraphic(mGraphicOverlay, component, Lotto.getResourses().getString(R.string.txt_ocr_num_received));
                    mGraphicOverlay.add(graphic);
                }
            }
        }

        for (String s : res) {
            ArrayList<Integer> lstInt = new ArrayList<>();
            for (int i = 0; i < 12; i+=2)
                lstInt.add(Integer.parseInt(s.substring(i, i + 2)));

            Collections.sort(lstInt); //???
            lstInt.add(Integer.parseInt(s.substring(12, 13)));
            CombinationLotto num = new CombinationLotto(lstInt);

            if(!pNums.contains(num)) {
                if(s.length()==14) pNums.add(Character.getNumericValue(s.charAt(13))-1,num);
                if(s.length()==15) pNums.add(Integer.parseInt(s.substring(13))-1,num);
                WinTables.add(0);
            }
        }
    }
    /*Detecting lotto ticket ID within general scanned text*/
    private void checkID2 (TextBlock item) {

        Matcher matcher;

        if (item.getComponents().size()==2)  {
            String lineID =  item.getComponents().get(0).getValue();
            String lineDate =  item.getComponents().get(1).getValue();
            if (item.getComponents().get(0) instanceof Line && item.getComponents().get(1) instanceof Line && lineID != null && !lineID.isEmpty() &&  lineDate != null && !lineDate.isEmpty() ) {

                matcher = pattern.matcher(lineDate);
                if(matcher.find()) {

                    try {
                        date= sdf.parse(matcher.group());
                        lineID= lineID.substring(0, lineID.indexOf('(')).replaceAll("\\D", "");
                        ID=Integer.parseInt(lineID);

                    }
                    catch (ParseException ignored) {

                    }
                }
            }
        }
    }
    /*Detecting lotto ticket EXTRA combination within general scanned text*/
    private CombinationExtra checkExtra (TextBlock item) {

        Matcher matcher;
        List<String> str;
        if (item.getComponents().size()==1)
         {
            String value = item.getComponents().get(0).getValue();

            if (item.getComponents().get(0) instanceof Line && value != null && !value.isEmpty()) {
                matcher = patternExtra.matcher(value);
                if (matcher.find()) {
                    graphic = new OcrGraphic(mGraphicOverlay, item.getComponents().get(0), Lotto.getResourses().getString(R.string.txt_ocr_date_received));
                    mGraphicOverlay.add(graphic);
                    str = Arrays.asList(matcher.group().split("(?<!^)"));
                    return new CombinationExtra( lstStrToInt(str));
                }
            }
        }
        return null;
    }
    /*Convert string array to integer array*/
    private ArrayList<Integer> lstStrToInt(List<String> lstStr) {

        ArrayList<Integer> lstInt = new ArrayList<>();

        for(String c:lstStr)
            lstInt.add(Integer.parseInt(c));

        return  lstInt;
    }
    /*Stop processing data*/
    @Override
    public void release() {
        mGraphicOverlay.clear();
    }
}