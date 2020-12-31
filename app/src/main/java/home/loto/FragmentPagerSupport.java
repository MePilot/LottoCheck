package home.loto;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.util.Locale;
/*This class represents Help activity*/
public class FragmentPagerSupport extends BaseActivity {
    static final int NUM_ITEMS = 4;

    MyAdapter mAdapter;

    static ViewPager mPager;
    /*Function is called first when an activity is created*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        mAdapter = new MyAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        setLocale();
    }
    /*Set the preferred activity language*/
    public void setLocale() {

        Locale locale = getLocale(this);

        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());

    }
    /*Close help activity*/
    public void Close_Help(View view) {
        this.finish();
    }

    /*Adapter that wraps and presents help pages data*/
    public static class MyAdapter extends FragmentPagerAdapter {
        /*Constructor*/
        MyAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        /*Return amount of help pages*/
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
        /*Return individual page by it's index*/
        @Override
        public Fragment getItem(int position) {
            return ArrayListFragment.newInstance(position);
        }
    }

    public static class ArrayListFragment extends Fragment {
        int mNum;

        /**
         * Create a new instance of Help fragment activity, providing "num" as an argument.
         */
        static ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        /**
         When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        //Initialize fragment's views with content
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_pager_list, container, false);
            View vHeader = v.findViewById(R.id.header);
            View vText = v.findViewById(R.id.text);
            View pHelp = v.findViewById(R.id.pHelp);
            final View btnFinish = v.findViewById(R.id.btn_help_finish);
            View btnNext = v.findViewById(R.id.btn_help_next);

            ((TextView)vHeader).setText(getResources().getStringArray(R.array.txt_help_header)[mNum]);
            ((TextView)vText).setText(getResources().getStringArray(R.array.txt_help)[mNum]);

            ((ProgressBar)pHelp).setProgress(mNum+1);
            if(mNum==NUM_ITEMS-1)((Button)btnFinish).setVisibility(View.VISIBLE);
            else
                ((Button)btnFinish).setVisibility(View.GONE);

            btnNext.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(mNum<NUM_ITEMS-1) mPager.setCurrentItem(mNum+1);

                }
            });

            return v;
        }
//Remember the opened page number (for navigation back and forth)
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }
    }
}