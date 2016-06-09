package fr.insalyon.pi.tabmaster.models;

import java.util.ArrayList;

/**
 * Created by Ugo on 31/05/2016.
 */

//TODO Fusionner avec Music
//Object defining a tab item
public class TabRessource {
    private String title;
    private String author;

    public TabRessource (String newTitle, String newAuthor){
        title = newTitle;
        author = newAuthor;
    }

    public String getTitle(){return title;}
    public String getAuthor(){return author;}

    private static int lastTabID = 0;

    public static ArrayList<TabRessource> createTabsList(int nbTabs) {
        ArrayList<TabRessource> tabs = new ArrayList<TabRessource>();

        for (int i = 1; i <= nbTabs; i++) {
            tabs.add(new TabRessource("Tab n°" + ++lastTabID, "Author n°" + lastTabID/2));
        }

        return tabs;
    }

}
