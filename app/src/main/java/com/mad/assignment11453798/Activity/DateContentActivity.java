package com.mad.assignment11453798.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mad.assignment11453798.Adapter.FacebookEventAdapter;
import com.mad.assignment11453798.Adapter.TwitterTweetAdapter;
import com.mad.assignment11453798.Pojo.FacebookEvent;
import com.mad.assignment11453798.Pojo.TwitterTweet;
import com.mad.assignment11453798.R;

import java.util.List;

/**
 * DateContent Class
 * All the necessary content from chosen date
 */
public class DateContentActivity extends CalendarActivity{
    private TextView dateTextView;
    private String chosenDateId;
    private LinearLayout fbEventListLayout,twitterTweetListLayout;

    /**
     * Overrides onCreate()
     * Creates the view for the date's content
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        dateTextView = (TextView)findViewById(R.id.date_view_date_tv);
        fbEventListLayout = (LinearLayout)findViewById(R.id.date_content_fb_event_layout);
        twitterTweetListLayout = (LinearLayout)findViewById(R.id.date_content_twitter_tweet_layout);

        //Sets results from an intent
        Intent results = getIntent();
        onActivityResult(DATE_CONTENT_REQUEST,RESULT_OK,results);

        //Sets facebook event list view - shows only if items present for chosen date
        long eventCount = FacebookEvent.count(FacebookEvent.class);
        //Checks database for stored FacebookEvent objects
        if(eventCount>0) {
            List<FacebookEvent> facebookEvents = FacebookEvent.find(FacebookEvent.class,"date_id=?",chosenDateId);
            if(!facebookEvents.isEmpty()){
                FacebookEventAdapter facebookEventAdapter = new FacebookEventAdapter(this,facebookEvents);
                RecyclerView facebookEventRecyclerView = (RecyclerView)findViewById(R.id.date_content_event_list);
                facebookEventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                facebookEventRecyclerView.setAdapter(facebookEventAdapter);
                fbEventListLayout.setVisibility(View.VISIBLE);
            }
        }

        //Sets twitter tweet list view - shows only if items present for chosen date
        long tweetCount = TwitterTweet.count(TwitterTweet.class);
        //Checks database for stored TwitterTweet objects
        if(tweetCount>0){
            List<TwitterTweet> twitterTweets = TwitterTweet.find(TwitterTweet.class,"date_id=?",chosenDateId);
            if(!twitterTweets.isEmpty()) {
                TwitterTweetAdapter twitterTweetAdapter = new TwitterTweetAdapter(this, twitterTweets);
                RecyclerView twitterTweetRecyclerView = (RecyclerView)findViewById(R.id.date_content_twitter_tweet_list);
                twitterTweetRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                twitterTweetRecyclerView.setAdapter(twitterTweetAdapter);
                twitterTweetListLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Activity to run
     * Given requestCode, resultCode and data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Sets the date text view from the date id intent given from CalendarActivity
        if(requestCode == DATE_CONTENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String date = data.getStringExtra(INTENT_DATE) +"/" + data.getStringExtra(INTENT_MONTH) +"/" + data.getStringExtra(INTENT_YEAR);
                chosenDateId = data.getStringExtra(INTENT_DATE_ID);
                dateTextView.setText(date);
            }
        }
    }
}
