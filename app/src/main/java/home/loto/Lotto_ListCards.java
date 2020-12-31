package home.loto;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;
/*This class represents "Chance" result list main screen*/
public class Lotto_ListCards extends BaseActivity  {

    ListView lvMain;
    BoxAdapterCards boxAdapter;

    ArrayList<CardSet> cardSets=null;

    /*Called when the activity is first created.*/
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lotto_cards);

        cardSets=PaisInfo.Chance.getInfoChance();

        boxAdapter = new BoxAdapterCards(this,cardSets);
        lvMain = findViewById(R.id.lvMain);
        lvMain.setAdapter(boxAdapter);

    }
    /*Provides functionality to close button*/
    public void Cancel(View view) {
        finish();
    }
}
