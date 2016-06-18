package fr.insalyon.pi.tabmaster.models;

import java.util.Date;

/**
 * Created by nicolas on 02/06/16.
 */
public class Music {

    //TODO Fusionner avec
    //TODO Associer audio file et tab

    private int id;
    private String owner;
    private String player;
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

    public String getPlayer() { return this.player; }

    @Override
    public String toString() {
        return "Music{" +
                "id='" + id + '\'' +
                ", owner=" + owner +
                '}';
    }

    public void setId(int id) { this.id=id; }

    public void setCreated(Date created) { this.created=created; }

    public void setTitle(String title) { this.title=title; }

    public void setOwner(String owner) { this.owner=owner; }

    public void setNum_stars(Float num_stars) { this.num_stars=num_stars; }

    public void setNum_stars_votes(int num_stars_votes) { this.num_stars_votes=num_stars_votes; }

    public void setTablature(String tablature) { this.tablature=tablature; }

    public void setPlayer(String player) { this.player=player; }
}