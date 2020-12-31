package home.loto;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import home.ocrreader.OcrCaptureActivity777;
/*This class represents user's "777" saved ticket`s list main screen*/
public class Lotto_List777 extends BaseActivity  implements View.OnTouchListener {
    private RecyclerView.Adapter mAdapter;

    ArrayList<Set777> products = new ArrayList<>();

    ProgressBar progressBar ;
    LinearLayout lbtn1, lbtn2,lbtn3;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_777_list);

        progressBar  =  findViewById(R.id.progressbar);

        lbtn1= findViewById(R.id.lin1);
        lbtn2= findViewById(R.id.lin2);
        lbtn3= findViewById(R.id.lin3);

        lbtn1.setOnTouchListener(this);
        lbtn2.setOnTouchListener(this);
        lbtn3.setOnTouchListener(this);

        ArrayList<Set777> lstPSet;

        RW777 rw777 = new RW777(this);

        lstPSet = rw777.LoadPlaySets();

        if (lstPSet != null) {
            products.addAll(lstPSet);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new BoxAdapter777(products);
        recyclerView.setAdapter(mAdapter);

    }
    /*Provide functionality to Delete button*/
    public void DeleteAll(View v) {

        RW777 rw777 = new RW777(this);
        ArrayList<Set777> lstPSet=rw777.LoadPlaySets();


        for (Set777 pNum:lstPSet)
            if(!pNum.getChecked()) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

                alertDialog.setMessage(getString(R.string.txt_list_YS)).setCancelable(true)
                        .setPositiveButton(getString(R.string.btn_list_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteY(null);
                            }
                        })
                        .setNeutralButton(getString(R.string.btn_list_old_only), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteOldOnly(null);
                            }
                        })
                        .setNegativeButton(getString(R.string.btn_list_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                AlertDialog dialog = alertDialog.create();

                dialog.show();
                return;
            }
        DeleteY(null);
    }
    /*Provide graphic effects when clicking on buttons*/
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {

            case R.id.lin1:
                if (event.getAction()==MotionEvent.ACTION_DOWN) {lbtn1.setAlpha(1f); lbtn1.setBackgroundColor(getResources().getColor(R.color.yellow)); v.performClick(); }else if (event.getAction()==MotionEvent.ACTION_UP){ lbtn1.setBackgroundColor(getResources().getColor(R.color.white));lbtn1.setAlpha(0.7f);}
                break;
            case R.id.lin2:
                if (event.getAction()==MotionEvent.ACTION_DOWN) {lbtn2.setAlpha(1f); lbtn2.setBackgroundColor(getResources().getColor(R.color.yellow)); v.performClick(); }else if (event.getAction()==MotionEvent.ACTION_UP) { lbtn2.setBackgroundColor(getResources().getColor(R.color.white));lbtn2.setAlpha(0.7f);}
                break;
            case R.id.lin3:
                if (event.getAction()==MotionEvent.ACTION_DOWN) {lbtn3.setAlpha(1f);lbtn3.setBackgroundColor(getResources().getColor(R.color.yellow)); v.performClick();} else if (event.getAction()==MotionEvent.ACTION_UP) {lbtn3.setBackgroundColor(getResources().getColor(R.color.white)); lbtn3.setAlpha(0.7f);}
                break;
        }


        return true;
    }
    /*Delete all tickets saved in memory*/
    public void DeleteY(View v) {

        RW777 rw777 = new RW777(this);
        ArrayList<Set777> lstPSet=new ArrayList<>();

        rw777.Save(lstPSet);

        products.clear();
        mAdapter.notifyDataSetChanged();
    }
    /*Delete only old and checked tickets*/
    public void DeleteOldOnly(View v) {

        RW777 rw777 = new RW777(this);
        ArrayList<Set777> lstPSet=rw777.LoadPlaySets();

        for (int i=0;i<lstPSet.size();i++) {
            if (lstPSet.get(i).getChecked()) {
                lstPSet.remove(i);
                i--;
            }
        }

        rw777.Save(lstPSet);

        lstPSet = rw777.LoadPlaySets();

        products.clear();
        products.addAll(lstPSet);
        mAdapter.notifyDataSetChanged();

    }

    /*Provide functionality to Check button. Start new thread and check tickets via internet*/
    public void Check(View v) {
        bCheck b = new bCheck(this);
        b.execute();
    }
    /*Provide functionality to Add button. Start the scan tickets activity*/
    public void Add(View view) {
        Intent intent = new Intent(this, OcrCaptureActivity777.class);

        startActivity(intent);
        finish();
    }
    /*This class provides a new thread for pulling data from the internet*/
    private class bCheck extends  AsyncTask<Void, Void, Void> {

        private WeakReference<Lotto_List777> activityReference;
        /*Constructor*/
        bCheck(Lotto_List777 context) {
            activityReference = new WeakReference<>(context);

        }
        /*Doing the work*/
        @Override
        protected Void doInBackground(Void... voids) {


            Check777.CheckFromFile();

            return null;
        }
        /*Invoked before the task execution*/
        @Override
        protected void onPreExecute() {

            activityReference.get().progressBar.setVisibility(View.VISIBLE);
            activityReference.get().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        /*Invoked after the task execution*/
        protected void onPostExecute(Void res) {

            super.onPostExecute(res);

            if (activityReference.get() == null || activityReference.get().isFinishing()) return;

            RW777 rw777 = new RW777(Lotto.getInstance());
            products.clear();
            products.addAll(rw777.LoadPlaySets());
            // mAdapter = new BoxAdapter(products);
            mAdapter.notifyDataSetChanged();
            // lvMain.setAdapter(boxAdapter);
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }
    }
}
