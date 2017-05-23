package com.mad.assignment11453798.Pojo;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * TwitterTweet POJO
 */
public class TwitterTweet extends SugarRecord{
    private String tweetId;
    private String text;
    private Date created_at;
    private int dateId;

    /**
     * TwitterTweet Constructor
     */
    public TwitterTweet(){
    }

    /**
     * TwitterTweet Constructor
     * used for SugarRecord library
     * logic to generate date id
     */
    public TwitterTweet(String name, Date created_at){
        this.text = name;

        //generates date id for event given start_time
        String month = String.format("%02d",created_at.getMonth()+1);
        String day = String.format("%02d",created_at.getDate());
        String dateId = String.valueOf(created_at.getYear()+1900) + month + day;
        this.dateId = Integer.parseInt(dateId);
    }

    /**
     * Tweet ID Getter
     */
    public String getTweetId() { return this.tweetId; }

    /**
     * Tweet ID Setter
     */
    public void setTweetId(String id) { this.tweetId = id; }

    /**
     * Text Getter
     */
    public String getText() { return this.text; }

    /**
     * Text Setter
     */
    public void setText(String text){ this.text = text; }

    /**
     * Created Date Getter
     */
    public Date getDate() { return this.created_at; }

    /**
     * Created Date Setter
     */
    public void setDate(Date date) { this.created_at = date; }
}
