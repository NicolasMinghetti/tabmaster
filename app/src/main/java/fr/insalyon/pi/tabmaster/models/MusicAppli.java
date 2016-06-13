package fr.insalyon.pi.tabmaster.models;

import java.util.Date;

/**
 * Created by nicolas on 02/06/16.
 */
public class MusicAppli {

    private int id;
    private String owner;
    private Date created;
    private String title;
    private Float num_stars;
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

    public Float getNum_stars() {
        return this.num_stars;
    }

    public int getNum_stars_votes() {
        return this.num_stars_votes;
    }

    public String getTablature() {
        return this.tablature;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id='" + id + '\'' +
                ", owner=" + owner +
                '}';
    }

    public MusicAppli (int id, String owner, Date created, String title, Float num_stars, int num_stars_votes, String tablature){
        this.id =id;
        this.owner=owner;
        this.created=created;
        this.title=title;
        this.num_stars=num_stars;
        this.num_stars_votes=num_stars_votes;
        this.tablature=tablature;
    }
}