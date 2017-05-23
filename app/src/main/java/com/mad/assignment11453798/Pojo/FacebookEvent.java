package com.mad.assignment11453798.Pojo;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Facebook Event POJO
 */
public class FacebookEvent extends SugarRecord{
    private String eventId;
    private String name;
    private String description;
    private Date start_time;
    private int dateId;

    /**
     * FacebookEvent Constructor
     */
    public FacebookEvent(){
    }

    /**
     * FacebookEvent Constructor
     * used for SugarRecord library
     * logic to generate date id
     */
    public FacebookEvent(String name, String description,Date start_time){
        this.name = name;
        this.description = description;

        //generates date id for event given start_time
        String month = String.format("%02d",start_time.getMonth()+1);
        String day = String.format("%02d",start_time.getDate());
        String dateId = String.valueOf(start_time.getYear()+1900) + month + day;
        this.dateId = Integer.parseInt(dateId);
    }

    /**
     * Event ID Getter
     */
    public String getEventId(){
        return eventId;
    }

    /**
     * Event ID Setter
     */
    public void setEventId(String id){
        this.eventId = id;
    }

    /**
     * Name Getter
     */
    public String getName(){
        return name;
    }

    /**
     * Name Setter
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Description Getter
     */
    public String getDescription(){
        return description;
    }

    /**
     * Description Setter
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * Start_time Getter
     */
    public Date getStartTime() {
        return this.start_time;
    }

    /**
     * Start_time Setter
     */
    public void setStartTime(Date date){
        this.start_time = date;
    }
}
