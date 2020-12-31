package home.loto;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import java.util.ArrayList;
import static android.graphics.Color.RED;
import static android.graphics.Typeface.BOLD;
/*This class represents "extra" combination data*/
public class CombinationExtra {
    private ArrayList<Integer> digits;
    private ArrayList<Integer> matches;
    /*Constructors*/
    public CombinationExtra()
    {
        this.digits = new ArrayList<>();
        this.matches = new ArrayList<>();

        for(int i=0;i<6;i++)
            this.digits.add(0);

        for(int i=0;i<6;i++)
            this.matches.add(0);

    }
    CombinationExtra(ArrayList<Integer> numbers, ArrayList<Integer> matches)
    {
        this.digits = numbers;
        this.matches = matches;

    }
    public CombinationExtra(ArrayList<Integer> numbers)
    {
        this.digits = numbers;
        this.matches = new ArrayList<>();

        for(int i=0;i<6;i++)
            this.matches.add(0);
    }

    /*Get all numbers*/
    int getDigit(int n) {
        return digits.get(n);
    }
    /*Get specific number in combination*/
    ArrayList<Integer> getDigits() {
        return digits;
    }

    /*Check if 2 combinations are equal*/
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof CombinationExtra)) {
            return false;
        }

        CombinationExtra extra=(CombinationExtra) object;

        for (int i = 0; i < 6; i++)
            if(digits.get(i)!=extra.getDigit(i)) return  false;

        return true;
    }

    /*Return a decorated (marked) string representing a combination, with red numbers that match the lottery win combo*/
    SpannableStringBuilder StringRepr() {
        SpannableStringBuilder str = new SpannableStringBuilder();

        for (int i = 0; i < digits.size(); i++) {
            str.append(String.valueOf(digits.get(i)));
            if(i<digits.size()-1) str.append("  ");

            if (matches.get(i) == 1 && i<digits.size()-1) {
                str.setSpan(new StyleSpan(BOLD), str.length() - 3, str.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new ForegroundColorSpan(RED), str.length() - 3, str.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(matches.get(i) == 1 && i==digits.size()-1)  {
                str.setSpan(new StyleSpan(BOLD), str.length() - 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new ForegroundColorSpan(RED), str.length() - 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return str;
    }
    /*Return a regular string representing a combination*/
    String StringDigits() {

        String str=digits.toString();

        return str.substring(1,str.length() - 1);

    }
    /*Return a string representing a matches sequence*/
    String StringMatches() {

        String str=matches.toString();

        return str.substring(1,str.length() - 1);

    }
}

