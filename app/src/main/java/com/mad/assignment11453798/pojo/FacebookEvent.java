package com.mad.assignment11453798.pojo;

import com.mad.assignment11453798.other.DateIdGenerator;
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
        DateIdGenerator dateIdGenerator = new DateIdGenerator(start_time);
        this.dateId = dateIdGenerator.getDateId();
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
