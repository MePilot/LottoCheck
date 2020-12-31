package home.loto;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Locale;
import static home.loto.Lotto_Activity.Sdiff777;
/*This class is required to wrap the data of Set777 class (777 ticket data) and display it in a scrolling list*/
public class BoxAdapter777 extends RecyclerView.Adapter<BoxAdapter777.MyViewHolder> {
    private ArrayList<Set777> objects;
    private String time_left;
    /*Constructor*/
    BoxAdapter777(ArrayList<Set777> items)
    {
        objects = items;
        time_left=Sdiff777;
        setLocale();
    }
    /*Set preferred language*/
    private void setLocale() {

        final Resources res = Lotto.getResourses();
        final Configuration conf = res.getConfiguration();

        final Locale locale = BaseActivity.getLocale(Lotto.getInstance());

        if (!conf.locale.equals(locale)) {

            conf.setLocale(locale);
            res.updateConfiguration(conf, null);
        }
    }
    /*This class provides reference to the views for each data item*/
    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView lottoDesc, txtPrize;
        TextView[] combs = new TextView[3];
        ImageView imgStat;
        /*Constructor*/
        MyViewHolder(View itemView) {
            super(itemView);

            this.lottoDesc = itemView.findViewById(R.id.tvDescr);
            this.txtPrize= itemView.findViewById(R.id.txtPrize);
            this.imgStat= itemView.findViewById(R.id.ivImage);

            for(int i=0;i<3;i++) {
                int adrConv = Lotto.getInstance().getResources().getIdentifier("comb"+(i+1), "id",Lotto.getInstance().getPackageName());
                combs[i] = itemView.findViewById(adrConv);
                combs[i].setVisibility(View.GONE);
            }
        }
    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public BoxAdapter777.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item777, parent, false);
        return new MyViewHolder(listItem);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SpannableStringBuilder builder = new SpannableStringBuilder();

        Set777 p = objects.get(position);
        for(int i=0;i<3;i++) {

            holder.combs[i].setVisibility(View.GONE);
        }

        if(!p.getChecked()) {
            if(PaisInfo.latest_777_ID<p.getID()) {
                holder.imgStat.setImageResource(R.drawable.time_blue);
                holder.lottoDesc.setText(Lotto.getResourses().getString(R.string.txt_list_number,String.valueOf(p.getID()),p.getDate(),Lotto.getResourses().getString(R.string.txt_list_wait,time_left)));
            }
            else  {
                holder.imgStat.setImageResource(R.drawable.quest);
                holder.lottoDesc.setText(Lotto.getResourses().getString(R.string.txt_list_number,String.valueOf(p.getID()),p.getDate(),Lotto.getResourses().getString(R.string.txt_list_unchecked)));
            }
        }
        else {
            holder.lottoDesc.setText(Lotto.getResourses().getString(R.string.txt_list_number,String.valueOf(p.getID()),p.getDate(),Lotto.getResourses().getString(R.string.txt_list_checked)));

            if (p.hasWins()) {
                holder.imgStat.setImageResource(R.drawable.win);
                holder.txtPrize.setText(Lotto.getResourses().getString(R.string.txt_list_Prize,String.valueOf(p.getTotalPrize())));
                holder.txtPrize.setVisibility(View.VISIBLE);
                //holder.lottoDesc.setTextColor((Lotto.getResourses().getColor(R.color.orange)));
            }
            else {
                holder.imgStat.setImageResource(R.drawable.cancel);
                holder.txtPrize.setVisibility(View.GONE);

            }
        }
        for (int i = 0; i < p.GetNumbers().size(); i++) {
            builder.append("(").append(String.valueOf(i + 1)).append(")  ");

            builder.append(p.GetNumbers().get(i).StringRepr());

            holder.combs[i].setText(builder);

            holder.combs[i].setVisibility(View.VISIBLE);

            if (p.isWon(i))

                holder.combs[i].setBackground(Lotto.getInstance().getResources().getDrawable(R.drawable.frame_win));
            else
                holder.combs[i].setBackground(Lotto.getResourses().getDrawable(R.color.white));

            builder.clear();
        }

    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return objects.size();
    }
}
