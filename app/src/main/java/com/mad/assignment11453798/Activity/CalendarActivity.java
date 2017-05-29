package com.mad.assignment11453798.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.mad.assignment11453798.Pojo.FacebookEvent;
import com.mad.assignment11453798.Pojo.TwitterTweet;
import com.mad.assignment11453798.R;
import com.mad.assignment11453798.Service.FacebookService;
import com.mad.assignment11453798.Service.TwitterService;
import com.orm.SugarRecord;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Calendar Activity Class
 * Has CalendarView calendar
 * and Sync button
 */
public class CalendarActivity extends MainActivity{
    public static final int DATE_CONTENT_REQUEST = 1;
    public static final String INTENT_DATE_ID = "date_id";
    public static final String INTENT_YEAR = "year";
    public static final String INTENT_MONTH = "month";
    public static final String INTENT_DATE = "day";
    private CalendarView calendar;
    private FloatingActionButton syncFab;


    /**
     * Overrides onCreate()
     * Initialises the CalendarView and Sync FAB
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initialiseCalendar();

        syncFab = (FloatingActionButton)findViewById(R.id.calendar_sync_fab);
        syncFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterService twitterApi = new TwitterService();
                FacebookService facebookApi = new FacebookService();
                twitterApi.getTweets();
                facebookApi.getFacebookEvents();
            }
        });
    }

    /**
     * Initialises Calendar view
     */
    public void initialiseCalendar() {
        calendar = (CalendarView) findViewById(R.id.calendar);

        // sets the first day of week to Monday
        calendar.setFirstDayOfWeek(2);

        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new OnDateChangeListener() {
            //starts date content activity for chosen date
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                String dateId = String.valueOf(year)+ String.format("%02d",month+1)+String.format("%02d",day);
                Intent dateContent = new Intent(getApplicationContext(),DateContentActivity.class);
                dateContent.putExtra(INTENT_DATE_ID,dateId);
                dateContent.putExtra(INTENT_YEAR,String.valueOf(year));
                dateContent.putExtra(INTENT_MONTH,String.valueOf(month+1));
                dateContent.putExtra(INTENT_DATE,String.valueOf(day));
                startActivityForResult(dateContent,DATE_CONTENT_REQUEST);
            }
        });
    }
}
