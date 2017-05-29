package com.mad.assignment11453798.pojo;

import com.orm.SugarRecord;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);//Wed May 24 13:18:45 +0000 2017
        try {
            Date date = formatter.parse(created_at);
            //generates date id for event given start_time
            String month = String.format("%02d",date.getMonth()+1);
            String day = String.format("%02d",date.getDate());
            String dateId = String.valueOf(date.getYear()+1900) + month + day;
            this.dateId = Integer.parseInt(dateId);
        }
        catch(ParseException e){
            e.printStackTrace();
        }
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
