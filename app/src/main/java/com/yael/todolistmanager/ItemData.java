package com.yael.todolistmanager;
import java.util.HashMap;
// import java.util.*;

/**
 * Created by Nicole on 06/05/2017.
 */

public class ItemData {
    private static final String DAY = "day";
    private static final String MONTH = "month";
    private static final String YEAR = "year";
    private String title;
    private String description;
    private String date_string;
    private boolean completed;
    private HashMap<String, Integer> date_map;


    public ItemData( String title, String description, String date){
        this.title = title;
        this.description = description;
        this.completed = false;
        this.date_string = date;
        this.date_map = new HashMap<String, Integer>();
        parseDate( date);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title) {
        this.title = title;
    }

    public void setSelected(boolean isSelected) {
        this.completed = isSelected;
    }

    public boolean isSelected() { return this.completed; }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description) {
        this.description =  description;
    }

    public String getDate() {
        return date_string;
    }

    private void parseDate( String dateStr) {
        String[] tokens;
        String delims = "[/]+";
        if ( dateStr != null) {
            tokens = dateStr.split(delims);
            if ( tokens.length == 3) {
                try {
                    this.date_map.put(DAY, Integer.parseInt(tokens[0]));
                    this.date_map.put(MONTH, Integer.parseInt(tokens[1]));
                    this.date_map.put(YEAR, Integer.parseInt(tokens[2]));
                } catch (NumberFormatException e) {
                    this.date_map.clear();
                }
            }
        }
    }

    public int getEntry( String key) {
        int value = 0;
        if (this.date_map.containsKey( key)) {
            Object val = this.date_map.get(key);
            if ( val != null) {
                value = (int) val;
            }
        }
        return value;
    }

    public int getDay() {
        int val = getEntry(DAY);
        return val;
    }

    public int getMonth() {
        int val = getEntry(MONTH);
        return val;
    }

    public int getYear() {
        int val = getEntry(YEAR);
        return val;
    }

}