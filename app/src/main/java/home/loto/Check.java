package home.loto;

import java.util.ArrayList;
import java.util.List;

/*This class provides functions that compare the published win results of lotto games with the user tickets stored in memory.*/
final class Check {

    static boolean CheckFromFile() {
        /*Initializing Read and Write object*/
        RW rw;
        rw= new RW(Lotto.getInstance().getApplicationContext());
        /*Loading saved tickets from JSON*/
        ArrayList<SetLotto> lstPlaySet = rw.LoadPlaySets();
        ArrayList<Integer> matches;
        boolean need_check=false;
        boolean need_show=false;

        /*See if there are unchecked tickets*/
        for(SetLotto pSet:lstPlaySet) {
            if (!pSet.getChecked()) {
                need_check = true;
                break;
            }
        }
        /*Quit if all are checked */
        if(!need_check) return false;
        /*Pull data from web server*/
        WinResultsLoto winSets=new WinResultsLoto();
        /*Quit if no data available*/
        if(!winSets.hasData()) return false;
        /*Compare saved tickets data with 61 last published lottery results*/
        /*Mark matching numbers in lottery combinations*/
        for(int i=0;i<62;i++) {

            for (int j=0;j<lstPlaySet.size();j++) {

                if(!lstPlaySet.get(j).getChecked() && winSets.getPlayID(i)==lstPlaySet.get(j).getID()) {

                    lstPlaySet.get(j).setChecked();

                    lstPlaySet.get(j).setWin(new CombinationLotto(lstStrToInt(winSets.getNumber(i))));

                    if(lstPlaySet.get(j).playsExtra()) {
                        matches=CheckMatchExtra(lstPlaySet.get(j).getExtra(), winSets.getNumberExtra(i));
                        lstPlaySet.get(j).setExtra(new CombinationExtra(lstPlaySet.get(j).getExtra().getDigits(), matches));
                        lstPlaySet.get(j).setExtraWin(isWinExtra(matches));
                    }

                    for(int k=0;k<lstPlaySet.get(j).GetNumbers().size();k++) {
                        matches = CheckMatch(lstPlaySet.get(j).GetNumbers().get(k), winSets.getNumber(i));
                        lstPlaySet.get(j).GetNumbers().set(k,new CombinationLotto(lstPlaySet.get(j).GetNumbers().get(k).getNumbers(), matches));
                        lstPlaySet.get(j).getWinTable().set(k,isWin(matches)); //////
                        lstPlaySet.get(j).SetTotalPrize();
                        if(lstPlaySet.get(j).hasWins()) need_show=true;
                    }
                }
            }
        }
        /*Save the marked numbers to JSON*/
        rw.Save(lstPlaySet);
        /*Return true if player won at least one game*/
        return need_show;

    }

    private static int isWin(ArrayList<Integer> matches) {
        /*Return the amount of matches in one lottery combination*/
        int counter=0;

        for (int i=0;i<matches.size()-1;i++) {
            if(matches.get(i)==1) counter++;
        }

        if(counter>=3 && matches.get(6)==1) counter=counter*10;

        return counter;
    }

    private static ArrayList<Integer> CheckMatch(CombinationLotto lotto, List<String> win) {
        /*Return array of matches or no matches (1 and 0) in one lottery combination*/
        boolean match;
        ArrayList<Integer> loto_win = new ArrayList<>(lstStrToInt(win));
        ArrayList<Integer> matches = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            match=false;

            for (int j = 0; j < 6; j++) {

                if(lotto.getNumber(i) == loto_win.get(j))  {
                    match=true;
                    break;
                }
            }
            matches.add(match ? 1 : 0);
        }

        if (lotto.getStrongNumber()==loto_win.get(6)) matches.add(1);
        else matches.add(0);

        return  matches;
    }

    private static int isWinExtra(ArrayList<Integer> matches) {
        /*Return the amount of matches in lottery EXTRA combination*/
        int counter=0;

        for(int i=0;i<matches.size();i++)
            if(matches.get(i)==1) counter++;

        return counter;
    }

    private static ArrayList<Integer> CheckMatchExtra(CombinationExtra extra, List<String> win) {
        /*Return array of matches or no matches (1 and 0) in EXTRA combination*/
        ArrayList<Integer> extra_win = new ArrayList<>(lstStrToInt(win));
        ArrayList<Integer> matches = new ArrayList<>();

        for (int i = 0; i <6; i++) {
            matches.add(extra.getDigit(i) == extra_win.get(i) ? 1 : 0);
        }

        return  matches;
    }

    private static ArrayList<Integer> lstStrToInt(List<String> lstStr) {
        /*Convert array of strings into array of integers*/
        ArrayList<Integer> lstInt = new ArrayList<>();
        for(String c:lstStr)
            lstInt.add(Integer.parseInt(c));
        return  lstInt;
    }
}
