package com.mad.assignment11453798.other;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * DateIdGenerator
 * Creates the date id for the stored data (events, tweets)
 */
public class DateIdGenerator {
    private int dateId;

    /**
     * DateIdGenerator Constructor for CalendarView date
     */
    public DateIdGenerator(int year, int month, int day){
        this.dateId = getCalendarDateId(year,month,day);
    }

    /**
     * DateIdGenerator Constructor for Facebook date
     */
    public DateIdGenerator(Date date){
        this.dateId = getFacebookDateId(date);
    }

    /**
     * DateIdGenerator Constructor for Twitter date
     */
    public DateIdGenerator(String date){
        this.dateId = getTwitterDateId(date);
    }

    /**
     * Converts date returned from calendar view into date id
     */
    private int getCalendarDateId(int year, int month, int day){
        String dateId = String.valueOf(year)+ String.format(Locale.getDefault(),"%02d",month+1)+String.format(Locale.getDefault(),"%02d",day);
        return Integer.parseInt(dateId);
    }

    /**
     * Converts date returned from facebook into date id
     */
    private int getFacebookDateId(Date date){
        return toDateId(date);
    }

    /**
     * Converts date returned from twitter into date id
     */
    private int getTwitterDateId(String date){
        int dateId = 0;
        DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.getDefault());//Wed May 24 13:18:45 +0000 2017
        try{
            Date parsedDate = formatter.parse(date);
            dateId = toDateId(parsedDate);
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        return dateId;
    }

    /**
     * Converts date into date id
     */
    private int toDateId(Date date){
        String month = String.format(Locale.getDefault(),"%02d",date.getMonth()+1);
        String day = String.format(Locale.getDefault(),"%02d",date.getDate());
        String id = String.valueOf(date.getYear()+1900) + month + day;
        dateId = Integer.parseInt(id);
        return dateId;
    }

    /**
     * DateId Getter
     */
    public int getDateId(){
        return this.dateId;
    }

    /**
     * DateId Setter
     */
    public void setDateId(int dateId){
        this.dateId = dateId;
    }

}
