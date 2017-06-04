package com.mad.assignment11453798.pojo;

import com.mad.assignment11453798.other.DateIdGenerator;
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
    public TwitterTweet(String text, String created_at){
        this.text = text;
        DateIdGenerator dateIdGenerator = new DateIdGenerator(created_at);
        this.dateId = dateIdGenerator.getDateId();
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
