package fr.insalyon.pi.tabmaster.models;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by nicolas on 02/06/16.
 */
public class Music {

    private int id;
    private Date created;
    private String title;
    private String owner;
    private DecimalFormat num_stars;
    private int num_stars_votes;
    private String tablature;

    public int getId() {
        return this.id;
    }

    public Date getCreated() {
        return this.created;
    }

    public String getTitle() { return this.title; }

    public String getOwner() {
        return this.owner;
    }

    public DecimalFormat getNum_stars() {
        return this.num_stars;
    }

    public int getNum_stars_votes() {
        return this.num_stars_votes;
    }

    public String getTablature() {
        return this.tablature;
    }
}
