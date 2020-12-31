package home.loto;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;
/*This class is required to wrap the data of _123Set class (123 game data) and display it in a scrolling list*/
public class BoxAdapter123 extends BaseAdapter {

    private LayoutInflater lInflater;
    private ArrayList<_123Set> objects;

    /*Constructor*/
    BoxAdapter123(Context context, ArrayList<_123Set> products) {
        objects = products;

        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    /*Get amount of items in adapter*/
    @Override
    public int getCount() {
        return objects.size();
    }

    /*Get specific item*/
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    /*Get ID of item*/
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Replace the contents of a view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null)
            view = lInflater.inflate(R.layout.item_123, parent, false);

        _123Set p = getProduct(position);

        ((TextView) view.findViewById(R.id.txtDig1)).setText(p.getValues().get(2));
        ((TextView) view.findViewById(R.id.txtDig2)).setText(p.getValues().get(1));
        ((TextView) view.findViewById(R.id.txtDig3)).setText(p.getValues().get(0));
        ((TextView) view.findViewById(R.id._123Desc)).setText(Lotto.getResourses().getString(R.string.txt_list_cards, p.getID()));

        return view;
    }
    /*Get adapter item*/
    private _123Set getProduct(int position) {
        return ((_123Set) getItem(position));
    }

}