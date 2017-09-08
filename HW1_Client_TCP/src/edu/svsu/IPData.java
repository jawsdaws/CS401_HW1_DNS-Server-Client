package edu.svsu;

import java.io.Serializable;

public class IPData implements Serializable {

    private String stringData  = "";
    private int count = 0;

    IPData () {

    }

    IPData(String string, int count) {
        this.count = count;
        this.stringData = string;
    }

    IPData(String string) {
        this.count = 0;
        this.stringData = string;
    }

    public int getCount() {
        return count;
    }

    public String getStringData() {
        return stringData;
    }
}
