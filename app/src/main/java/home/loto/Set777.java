package home.loto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/*This class represents user's "777" game ticket data*/
public class Set777  implements Serializable {

    private Date date;
    private int ID, prize;
    private ArrayList<Integer> WinTables;
    private ArrayList<Combination777> numbers;
    private boolean checked;
    private Combination777 Win;
    private List<Integer> matches = new ArrayList<>(Arrays.asList( 0,3,4,5,6,7));
    private Map<Integer, Integer> prizes = new HashMap<Integer, Integer>() {{

        put(7, 70000);
        put(6, 500);
        put(5, 50);
        put(4, 20);
        put(3, 5);
        put(0, 5);

    }};
    /*Constructor*/
    public Set777(int ID, Date date, ArrayList<Combination777> numbers, ArrayList<Integer> WinTables, boolean checked, Combination777 Win, int prize) {

        this.ID=ID;
        this.date=date;
        this.numbers=numbers;
        this.WinTables=WinTables;
        this.checked=checked;
        this.Win=Win;
        this.prize=prize;

        ArrayList<Integer> data = new ArrayList<>();
        for (Combination777 pNum:numbers)
            data.addAll(pNum.getNumbers());

    }
    /*Return ticket ID*/
    int getID() { return this.ID; }

    /*Return ticket prize*/
    int getTotalPrize() { return this.prize; }

    /*Return ticket date*/
    String getDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this.date);
    }

    /*Decide if the ticket was checked*/
    boolean getChecked() {
        return this.checked;
    }

    /*Set ticket status to checked */
    void setChecked() {
        this.checked= true;
    }
    /*Get all ticket's combinations*/
    ArrayList<Combination777> GetNumbers() {return this.numbers;}

    /*Get the counter array telling how much matched numbers is in each combination */
    ArrayList<Integer> getWinTable() { return this.WinTables; }

    /*Get the winning combination for this game*/
    Combination777 getWin() { return this.Win; }

    /*Set the winning combination for this game*/
    void setWin(Combination777 Win) { this.Win=Win; }

    /*Decide if this ticket has at least one winning combination*/
    boolean hasWins() {

        return !Collections.disjoint(this.getWinTable(), matches);
    }
    /*Decide if a specific combination on a ticket is a winning won*/
    boolean isWon(int i) {

        return matches.contains(this.getWinTable().get(i));
    }

    /*Set the win prize for this ticket*/
    void setTotalPrize()  {

        int sumN=0;

        for (int i=0;i<numbers.size();i++) {
            if( matches.contains(WinTables.get(i))) {
                sumN+=prizes.get(WinTables.get(i));

            }
        }
        prize=sumN;
    }
}
