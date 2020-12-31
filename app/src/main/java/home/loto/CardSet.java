package home.loto;

import java.io.Serializable;
import java.util.ArrayList;
/*This class represents the Chance game result.*/
class CardSet implements Serializable {

    private String ID;
    private ArrayList<String> values;
    /*Constructor*/
    CardSet(String ID, ArrayList<String> values) {

        this.ID=ID;
        this.values=values;
    }
    /*get cards values*/
    ArrayList<String> getValues() {return this.values;}
    /*get game ID*/
    String  getID() { return this.ID; }

}
