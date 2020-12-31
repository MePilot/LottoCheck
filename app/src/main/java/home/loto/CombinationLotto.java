package home.loto;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import java.text.DecimalFormat;
import java.util.ArrayList;
import static android.graphics.Color.RED;
import static android.graphics.Typeface.BOLD;
/*This class represents "lotto" combinations data*/
public class CombinationLotto {
    private ArrayList<Integer> numbers;
    private ArrayList<Integer> matches;
    private static final int SIZE = 7;
    /*Constructors*/
    public CombinationLotto()
    {
        this.numbers = new ArrayList<>();
        this.matches = new ArrayList<>();

        for(int i=0;i<SIZE;i++)
            this.numbers.add(0);

        for(int i=0;i<SIZE;i++)
            this.matches.add(0);

    }

    public CombinationLotto(ArrayList<Integer> numbers)
    {
        this.numbers = numbers;
        this.matches = new ArrayList<>();

        for(int i=0;i<SIZE;i++)
            this.matches.add(0);

    }
    CombinationLotto(ArrayList<Integer> numbers, ArrayList<Integer> matches)
    {
        this.numbers = numbers;
        this.matches = matches;
    }
    /*Get specific number in a combination*/
    int getNumber(int n) {
        return numbers.get(n);
    }
    /*Get the last, "strong" number in a lotto combination*/
    int getStrongNumber() {
        return getNumber(SIZE-1);
    }
    /*Get all numbers in a combination*/
    ArrayList<Integer> getNumbers() {
        return numbers;
    }

    /*Check if 2 combinations are equal*/
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof CombinationLotto)) {
            return false;
        }

        CombinationLotto lotto=(CombinationLotto) object;

        for (int i = 0; i < SIZE; i++)
            if(numbers.get(i)!=lotto.getNumber(i)) return  false;

        return true;
    }

    /*Return a decorated (marked) string representing a combination, with red numbers that match the lottery win combo*/
    SpannableStringBuilder StringRepr() {
        SpannableStringBuilder str = new SpannableStringBuilder();
        String pattern = "00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        str.append(String.valueOf(getStrongNumber())).append(" | ");

        if (matches.get(SIZE-1) == 1) {

            str.setSpan( new ForegroundColorSpan(RED), 0,  2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan( new StyleSpan(BOLD), 0,2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        for (int i = 0; i < SIZE-1; i++) {
            decimalFormat.format(numbers.get(i));
            str.append(decimalFormat.format(numbers.get(i)));

            if(i<numbers.size()-2) str.append("  ");


            if (matches.get(i) == 1 && i<numbers.size()-2) {
                str.setSpan(new StyleSpan(BOLD), str.length() - 4, str.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new ForegroundColorSpan(RED), str.length() - 4, str.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(matches.get(i) == 1 && i==numbers.size()-2)  {
                str.setSpan(new StyleSpan(BOLD), str.length() - 2, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new ForegroundColorSpan(RED), str.length() - 2, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return str;
    }
    /*Return a regular string representing a combination*/
    String StringNumbers() {

        String str=numbers.toString();

        return str.substring(1,str.length() - 1);

    }
    /*Return a string representing a matches sequence*/
    String StringMatches() {

        String str=matches.toString();

        return str.substring(1,str.length() - 1);

    }
}

