package ru.ifmo.ctddev.isaev.solanteq.pojo;


import java.io.Serializable;

/**
 * Created by root on 7/14/15.
 */
public class Position implements Comparable<Position>,Serializable {
    private int positionID;
    private String title;

    public Position(int positionID, String title) {
        this.positionID = positionID;
        this.title = title;
    }
    public Position() {
    }

    public int getPositionID() {
        return positionID;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int compareTo(Position o) {
        return positionID-o.positionID;
    }
}
