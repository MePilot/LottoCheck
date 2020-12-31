package home.loto;

import java.io.Serializable;
import java.util.ArrayList;

class _123Set implements Serializable {
/*This class represents the 123 game result.*/
    private String ID;
    private ArrayList<String> values;

    _123Set(String ID, ArrayList<String> values) {
        /*Constructor*/
        this.ID=ID;
        this.values=values;
    }
    /*Getter for numbers*/
    ArrayList<String> getValues() {return this.values;}
    /*Getter for ID*/
    String  getID() { return this.ID; }

}
