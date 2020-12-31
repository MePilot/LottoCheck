package home.loto;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import androidx.cardview.widget.CardView;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
/*This class represents "Lotto random numbers generator" screen*/
public class Lotto_Generator2 extends BaseActivity  implements View.OnClickListener {

    GridLayout grid;
    Button btn;
    CardView cardView;
    List<Integer> lst_v;
    List<Integer> lstNums=Arrays.asList(0,0,0,0,0,0,0);
    public static List<ArrayList<Integer>> listOfNumbers = new ArrayList<>();
    ArrayList<Integer> lstNums2=new ArrayList<>();
    List<Button> lst_btn = new ArrayList<>();
    TextView txtGen2;
    int d5;

    /*Auxiliary function*/
    int c (int val) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, val, getResources().getDisplayMetrics());
    }
    /*Activity start function*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lotto_generator2);
        int d=Resources.getSystem().getDisplayMetrics().widthPixels/9;
        int d2=Resources.getSystem().getDisplayMetrics().widthPixels/85;
        int d4=Resources.getSystem().getDisplayMetrics().widthPixels/18;

        grid = findViewById(R.id.grid);
        cardView =  findViewById(R.id.myCardView);
        lst_v=Arrays.asList(cardView.getLeft()+d4+d2, cardView.getLeft()+d4+(d)+d2*2,cardView.getLeft()+d4+(d*2)+d2*3, cardView.getLeft()+d4+(d*3)+d2*4,cardView.getLeft()+d4+(d*4)+d2*5,cardView.getLeft()+d4+(d*5)+d2*6,cardView.getLeft()+d4+(d*6)+d2*7,c(cardView.getTop())+c(100));

        txtGen2=findViewById(R.id.txtGen2);

        d5=cardView.getBottom();

        txtGen2.setText(getString(R.string.txt_lotto_id));

        for(int i=1;i<=37;i++) {
            btn = new Button(new ContextThemeWrapper(this, R.style.button), null, 0);
            btn.setTag(i);
            btn.setText(String.valueOf(i));
            lst_btn.add(btn);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec((i-1)/6+1, 1, GridLayout.CENTER),
                    GridLayout.spec((i-1)%6, 1));

            params.width = d;
            params.height =  d;
            params.setMargins(d2, d2, d2, d2);

            grid.addView(btn, params);
            btn.setOnClickListener(this);
        }

        for (int i = 1; i <= 7; i++) {
            btn = new Button(new ContextThemeWrapper(this, R.style.button_strong), null, 0);

            btn.setTag(i*100);
            btn.setText(String.valueOf(i));
            lst_btn.add(btn);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(i, 1, GridLayout.CENTER),
                    GridLayout.spec(6, 1));
            params.width =d;
            params.height = d;
            params.setMargins(d2, d2, d2, d2);
            grid.addView(btn, params);
            btn.setOnClickListener(this);
        }
        btn = new Button(new ContextThemeWrapper(this, R.style.button), null, 0);

        btn.setText(getString(R.string.btn_generator_random));
        btn.setTag(1000);
        btn.setOnClickListener(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                GridLayout.spec(7, 1),
                GridLayout.spec(1, 5, GridLayout.CENTER));
        params.width = (int) (d*2*2.7f);
        params.height = d;
       params.setMargins(d2, d2, d2, d2);
        grid.addView(btn, params);

    }
    /*Following functions provide animation for number icons*/
    synchronized public void animStrong(View v) {

        final SpringAnimation anim1X = new SpringAnimation(v, DynamicAnimation.TRANSLATION_X,0);
        final SpringAnimation anim1Y = new SpringAnimation(v, DynamicAnimation.TRANSLATION_Y,0);
        anim1X.getSpring().setDampingRatio(0.6f).setStiffness(90);
        anim1Y.getSpring().setDampingRatio(0.6f).setStiffness(90);
        int d = (int) (v.getTag())/100;

        if(lstNums.get(6)==0){
            anim1X.animateToFinalPosition(lst_v.get(6) - v.getLeft());
            anim1Y.animateToFinalPosition(lst_v.get(7)- v.getTop());
            lstNums.set(6, d);
        }
        else {

            for(View b:lst_btn.subList(37,44)) {
                final SpringAnimation a1X = new SpringAnimation(b, DynamicAnimation.TRANSLATION_X,0);
                final SpringAnimation a1Y = new SpringAnimation(b, DynamicAnimation.TRANSLATION_Y,0);
                a1X.getSpring().setDampingRatio(0.6f).setStiffness(90);
                a1Y.getSpring().setDampingRatio(0.6f).setStiffness(90);
                a1X.animateToFinalPosition(0);
                a1Y.animateToFinalPosition(0);
            }
            anim1X.animateToFinalPosition(lst_v.get(6) - v.getLeft());
            anim1Y.animateToFinalPosition(lst_v.get(7)- v.getTop());
            lstNums.set(6, d);

        }

    }
    synchronized boolean isFull () {
        for (int n : lstNums)
            if (n == 0) return false;
        return true;
    }
    synchronized boolean isAlmostFull () {
        for(int i=0;i<lstNums.size()-1;i++)
            if(lstNums.get(i)==0) return false;
        return true;
    }

    synchronized int LastZero () {
        for(int i=0;i<lstNums.size()-1;i++)
            if(lstNums.get(i)==0) return i;
        return 0;
    }
    synchronized public void anim(View v) {

        final SpringAnimation anim1X = new SpringAnimation(v, DynamicAnimation.TRANSLATION_X,0);
        final SpringAnimation anim1Y = new SpringAnimation(v, DynamicAnimation.TRANSLATION_Y,0);
        anim1X.getSpring().setDampingRatio(0.6f).setStiffness(90);
        anim1Y.getSpring().setDampingRatio(0.6f).setStiffness(90);
        int d = (int) (v.getTag());

        if(lstNums.subList(0,6).contains(d)) {

            anim1X.animateToFinalPosition(0);
            anim1Y.animateToFinalPosition(0);
            lstNums.set(lstNums.subList(0,6).indexOf(d),0);
        }
        else if (!isAlmostFull()) {

            anim1X.animateToFinalPosition(lst_v.get(LastZero ()) - v.getLeft());
            anim1Y.animateToFinalPosition(lst_v.get(7)+d5 - v.getTop());
            lstNums.set(LastZero(),d);
        }

        anim1Y.addEndListener(new DynamicAnimation.OnAnimationEndListener() {

            @Override
            public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean b, float v, float v1) {

            }
        });
    }
    /*Generate sequence of random numbers*/
    synchronized private ArrayList<Integer> GenerateNumbers() {

        Random rand = new Random();
        ArrayList<Integer> num = new ArrayList<>();
        int digit;

        while (num.size()<6) {
            digit=rand.nextInt(37)+1;

            if (!num.contains(digit)) num.add(digit);
        }
        Collections.sort(num);

        digit=rand.nextInt(7)+1;
        num.add(digit);

        return num;
    }
    /*Provide icons and buttons on click event functionality*/
    @Override
    synchronized public void onClick(View v) {

        int tag=(int)v.getTag();
        if(tag==100 || tag==200 ||tag==300 ||tag==400 || tag==500 || tag==600 ||tag==700)
            animStrong(v);
        else if (tag==1000) {
            lstNums=Arrays.asList(0,0,0,0,0,0,0);
            lstNums2 = GenerateNumbers();

            for(Button bt:lst_btn) {
                final SpringAnimation anim1X = new SpringAnimation(bt, DynamicAnimation.TRANSLATION_X,0);
                final SpringAnimation anim1Y = new SpringAnimation(bt, DynamicAnimation.TRANSLATION_Y,0);
                anim1X.getSpring().setDampingRatio(0.6f).setStiffness(90);
                anim1Y.getSpring().setDampingRatio(0.6f).setStiffness(90);
                anim1X.animateToFinalPosition(0);
                anim1Y.animateToFinalPosition(0);

            }
            for (int i = 0; i < lstNums2.size()-1; i++) {
                final int b = lstNums2.get(i);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        anim(lst_btn.get(b - 1));
                        if(isAlmostFull()) animStrong(lst_btn.get(lstNums2.get(6) + 37 - 1));
                    }
                }, 400);
            }
        }
        else if(tag==2000) {
            if(isFull()) {
                Collections.sort(lstNums.subList(0,6));
                listOfNumbers.add(new ArrayList<>(lstNums));
                listOfNumbers.clear();

                lstNums=Arrays.asList(0,0,0,0,0,0,0);

                for(Button bt:lst_btn) {
                    final SpringAnimation anim1X = new SpringAnimation(bt, DynamicAnimation.TRANSLATION_X,0);
                    final SpringAnimation anim1Y = new SpringAnimation(bt, DynamicAnimation.TRANSLATION_Y,0);
                    anim1X.getSpring().setDampingRatio(0.5f).setStiffness(70);
                    anim1Y.getSpring().setDampingRatio(0.5f).setStiffness(70);
                    anim1X.animateToFinalPosition(0);
                    anim1Y.animateToFinalPosition(0);
                }
            }
        }
        else
            anim(v);
    }

    private void LotoExample() {
        ArrayList<Integer> number = new ArrayList<>();

        number.add(10);
        number.add(11);
        number.add(12);
        number.add(14);
        number.add(15);
        number.add(17);
        number.add(3);
        ArrayList<Integer> numberExtra = new ArrayList<>();

        numberExtra.add(1);
        numberExtra.add(5);
        numberExtra.add(5);
        numberExtra.add(1);
        numberExtra.add(2);
        numberExtra.add(3);

        CombinationLotto combinationLotto = new CombinationLotto(number);

        ArrayList<CombinationLotto> arrLotto = new ArrayList<>();
        arrLotto.add(combinationLotto);
        ArrayList<Integer> number2 = new ArrayList<>();

        number2.add(3);
        number2.add(10);
        number2.add(15);
        number2.add(17);
        number2.add(29);
        number2.add(31);
        number2.add(5);
        CombinationLotto combinationLotto2 = new CombinationLotto(number2);
        arrLotto.add(combinationLotto2);

        ArrayList<Integer> number3 = new ArrayList<>();

        number3.add(1);
        number3.add(8);
        number3.add(14);
        number3.add(18);
        number3.add(25);
        number3.add(36);
        number3.add(7);
        CombinationLotto combinationLotto3= new CombinationLotto(number3);
        arrLotto.add(combinationLotto3);

        ArrayList<Integer> number4 = new ArrayList<>();

        number4.add(9);
        number4.add(11);
        number4.add(13);
        number4.add(28);
        number4.add(29);
        number4.add(31);
        number4.add(1);

        CombinationLotto combinationLotto4 = new CombinationLotto(number4);
        arrLotto.add(combinationLotto4);

        ArrayList<Integer> WinTables = new ArrayList<>();
        WinTables.add(0);
        WinTables.add(3);
        WinTables.add(0);
        WinTables.add(0);

        SetLotto pSet = new SetLotto(3452, Calendar.getInstance().getTime(), arrLotto, WinTables,true, new CombinationExtra(numberExtra),0, new CombinationLotto(),15,30);

        RW rw =new RW(this);

        ArrayList<SetLotto> pPSets = rw.LoadPlaySets();

        pPSets.add(pSet);
        rw.Save(pPSets);
    }
    private void _777Example() {
        ArrayList<Integer> number = new ArrayList<>();

        number.add(1);
        number.add(5);
        number.add(6);
        number.add(8);
        number.add(9);
        number.add(10);
        number.add(11);
        Combination777 combination777 = new Combination777(number);

        ArrayList<Combination777> arr777 = new ArrayList<>();
        arr777.add(combination777);

        ArrayList<Integer> WinTables777 = new ArrayList<>();
        WinTables777.add(-1);

        Set777 pSet777 = new Set777(10235, Calendar.getInstance().getTime(), arr777, WinTables777, false, new Combination777(),0);

        RW777 rw777 =new RW777(this);

        ArrayList<Set777> pPSets777 = rw777.LoadPlaySets();

        pPSets777.add(pSet777);
        rw777.Save(pPSets777);
    }

}
