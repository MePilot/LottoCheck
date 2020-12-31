package home.loto;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;
/*This class represents "123" result list main screen*/
public class Lotto_List123 extends BaseActivity  {

    ListView lvMain;
    BoxAdapter123 boxAdapter;

    ArrayList<_123Set> _123Sets=null;

    /*Called when the activity is first created.*/
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lotto_cards);

        _123Sets= PaisInfo._123.getInfo123();


        boxAdapter = new BoxAdapter123(this,_123Sets);
        lvMain = findViewById(R.id.lvMain);
        lvMain.setAdapter(boxAdapter);

    }
    /*Provides functionality to close button*/
    public void Cancel(View view) {
        finish();
    }
}
