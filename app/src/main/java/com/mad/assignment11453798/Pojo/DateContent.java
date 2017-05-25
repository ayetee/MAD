package com.mad.assignment11453798.Pojo;

import com.orm.SugarRecord;

import java.util.List;

/**
 * DateContent POJO
 */
public class DateContent extends SugarRecord{
    private int dateId;
    private List<FacebookEvent> events;

    public DateContent(){
    }

    public DateContent(String dateId, List<FacebookEvent> events){
        this.dateId = Integer.parseInt(dateId);
        this.events = events;
    }

    public int getDateId(){
        return this.dateId;
    }

    public void setDateId(int dateId){
        this.dateId = dateId;
    }

    public List<FacebookEvent> getEvents(){
        return this.events;
    }

    public void setEvents(List<FacebookEvent> event){
        this.events = events;
    }

}
