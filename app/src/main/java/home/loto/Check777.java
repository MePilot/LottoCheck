package home.loto;

import java.util.ArrayList;
import java.util.List;

class Check777 {
    /*This class provides functions that compare the published win results of 777 games with the user tickets stored in memory.*/
    static boolean CheckFromFile() {
        /*Initializing Read and Write object*/
        RW777 rw;
        rw= new RW777(Lotto.getInstance().getApplicationContext());
        /*Loading saved tickets from JSON*/
        ArrayList<Set777> lstPlaySet = rw.LoadPlaySets();
        ArrayList<Integer> matches;

        boolean need_check=false;

        boolean need_show=false;
        int winSetsAmount;
        /*See if there are unchecked tickets*/
        for(Set777 pSet:lstPlaySet) {
            if (!pSet.getChecked()) {
                need_check = true;
                break;
            }
        }
        /*Quit if all are checked */
        if(!need_check) return false;
        /*Pull data from web server*/
        WinResults777 winSets=new WinResults777();
        /*Quit if no data available*/
        if(!winSets.hasData()) return false;

        winSetsAmount=winSets.getResultsAmount();

        /*Compare saved tickets data with 61 last published lottery results*/
        /*Mark matching numbers in lottery combinations*/
        for(int i=0;i<winSetsAmount;i++) {

            for (int j=0;j<lstPlaySet.size();j++) {
               // Log.d("OMG",String.valueOf(lstPlaySet.get(j).getID())+"="+ winSets.getPlayID(i));
                if(!lstPlaySet.get(j).getChecked() && winSets.getPlayID(i)==lstPlaySet.get(j).getID()) {
                    lstPlaySet.get(j).setChecked();
                    lstPlaySet.get(j).setWin(new Combination777(lstStrToInt(winSets.getNumber(i))));

                    for(int k=0;k<lstPlaySet.get(j).GetNumbers().size(); k++) {
                        matches = CheckMatch(lstPlaySet.get(j).GetNumbers().get(k), winSets.getNumber(i));
                        lstPlaySet.get(j).GetNumbers().set(k, new Combination777(lstPlaySet.get(j).GetNumbers().get(k).getNumbers(), matches));
                        lstPlaySet.get(j).getWinTable().set(k,isWin(matches));
                        lstPlaySet.get(j).setTotalPrize();
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
    /*Return the amount of matches in one lottery combination*/
    private static int isWin(ArrayList<Integer> matches) {
        int counter = 0;

        for (int i : matches) {
            if (i == 1) counter++;
        }

        return counter;

    }
    /*Return array of matches or no matches (1 and 0) in one lottery combination*/
    private static ArrayList<Integer> CheckMatch(Combination777 c777, List<String> win) {

        boolean match;
        ArrayList<Integer> win_777 = new ArrayList<>(lstStrToInt(win));
        ArrayList<Integer> matches = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            match=false;

            for (int j = 0; j < 17; j++) {

                if (c777.getNumber(i) == win_777.get(j) ) {
                    match=true;
                    break;
                }
            }

            matches.add(match ? 1 : 0);
        }

        return  matches;
    }
    /*Convert array of strings into array of integers*/
    private static ArrayList<Integer> lstStrToInt(List<String> lstStr) {

        ArrayList<Integer> lstInt = new ArrayList<>();
        for(String c:lstStr)
            lstInt.add(Integer.parseInt(c));
        return  lstInt;
    }
}
