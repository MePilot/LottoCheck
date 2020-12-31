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

import static home.loto.Lotto_Activity.Sdiff;
/*This class is required to wrap the data of SetLotto class (lotto ticket data) and display it in a scrolling list*/
public class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.MyViewHolder> {
    private ArrayList<SetLotto> objects;
    private String time_left;
    /*Constructor*/
    BoxAdapter(ArrayList<SetLotto> items)
    {
        objects = items;
        time_left=Sdiff;
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

    static class MyViewHolder extends RecyclerView.ViewHolder {
        /*This class provides reference to the views for each data item*/
        TextView lottoDesc, extraDesc, extra, txtPrize;
        TextView[] combs = new TextView[14];
        ImageView imgStat;
        /*Constructor*/
        MyViewHolder(View itemView) {
            super(itemView);

            this.lottoDesc = itemView.findViewById(R.id.tvDescr);
            this.extraDesc= itemView.findViewById(R.id.txtExtraDesc);
            this.extra= itemView.findViewById(R.id.txtExtra);
            this.txtPrize= itemView.findViewById(R.id.txtPrize);
            this.imgStat= itemView.findViewById(R.id.ivImage);

            for(int i=0;i<14;i++) {
                int adrConv = Lotto.getInstance().getResources().getIdentifier("comb"+(i+1), "id",Lotto.getInstance().getPackageName());
                combs[i]= itemView.findViewById(adrConv);
                combs[i].setVisibility(View.GONE);
            }
        }
    }


    // Create new views (invoked by the layout manager)

    @NonNull
    @Override
    public BoxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item, parent, false);
        return new MyViewHolder(listItem);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SpannableStringBuilder builder = new SpannableStringBuilder();

        SetLotto p = objects.get(position);

        for(int i=0;i<14;i++) {

            holder.combs[i].setVisibility(View.GONE);
        }
        holder.txtPrize.setVisibility(View.GONE);

        if(!p.getChecked()) {
            if(PaisInfo.latestID<p.getID()) {
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

            if (p.hasWins() || p.hasExtraWin()) {
                holder.imgStat.setImageResource(R.drawable.win);
                holder.txtPrize.setText(Lotto.getResourses().getString(R.string.txt_list_totalPrize,String.valueOf(p.getPrizeNormal()),String.valueOf(p.getPrizeDouble())));
                holder.txtPrize.setVisibility(View.VISIBLE);
                holder.lottoDesc.setTextColor(Lotto.getResourses().getColor(R.color.gold));
            }
            else {
                holder.imgStat.setImageResource(R.drawable.cancel);
                holder.txtPrize.setVisibility(View.GONE);
                holder.lottoDesc.setTextColor((Lotto.getResourses().getColor(R.color.white)));
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
        holder.extraDesc.setVisibility(View.VISIBLE);
        holder.extra.setVisibility(View.VISIBLE);

        if(p.playsExtra()) {
            holder.extraDesc.setText(Lotto.getResourses().getString(R.string.txt_lotto_extra));
            holder.extra.setText(p.getExtra().StringRepr());
            if(p.hasExtraWin())  holder.extra.setBackground(Lotto.getInstance().getResources().getDrawable(R.drawable.frame_win_extra));
            else  holder.extra.setBackground(Lotto.getInstance().getResources().getDrawable(R.color.white));
        }
        else {
            holder.extraDesc.setText(Lotto.getResourses().getString(R.string.txt_lotto_extra_no));
            holder.extra.setVisibility(View.GONE);
        }


    }
    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return objects.size();
    }
}
