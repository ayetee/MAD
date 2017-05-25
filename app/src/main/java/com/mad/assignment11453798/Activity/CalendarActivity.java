package com.mad.assignment11453798.Activity;

import android.content.Intent;
import android.os.Bundle;
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

public class CalendarActivity extends MainActivity{
    public static final int DATE_CONTENT_REQUEST = 1;
    public static final String INTENT_DATE_ID = "date_id";
    public static final String INTENT_YEAR = "year";
    public static final String INTENT_MONTH = "month";
    public static final String INTENT_DATE = "day";
    private CalendarView calendar;
    private Button syncBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initialiseCalendar();

        syncBtn = (Button)findViewById(R.id.calendar_sync_btn);
        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFacebookEvents();
                getTweets();
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

    /**
     * Facebook Graph API call
     * Stores the users events into Sugar db
     */
    private void getFacebookEvents(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), "/me/events", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.e(TAG,response.toString());
                        try {
                            Gson gson = new Gson();
                            List<FacebookEvent> facebookEvents = new ArrayList<>();
                            FacebookEvent.deleteAll(FacebookEvent.class);
                            JSONArray dataJSONArray = response.getJSONObject().getJSONArray("data");
                            for(int i=0;i<dataJSONArray.length();i++){
                                String content = dataJSONArray.get(i).toString();
                                FacebookEvent event = gson.fromJson(content,FacebookEvent.class);
                                facebookEvents.add(new FacebookEvent(event.getName(),event.getDescription(),event.getStartTime()));
                            }
                            SugarRecord.saveInTx(facebookEvents);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch(NullPointerException e){
                            e.printStackTrace();
                        }
                        catch (Exception e){//FIND ALL THE EXCEPTIONS will replace after
                            Log.d(TAG,e.toString());
                        }
                    }
                }
        ).executeAsync();
    }

    /**
     * Twitter REST API call
     * Stores the users tweets into Sugar db
     */
    private void getTweets(){
        TwitterSession currentSession = Twitter.getSessionManager().getActiveSession();
        TwitterApiClient twitterApiClient = new TwitterApiClient(currentSession);
        StatusesService statusesService = twitterApiClient.getStatusesService();
        String name = currentSession.getUserName();
        Call<List<Tweet>> tweets = statusesService.userTimeline(null, name, 5, null, null, false, false, false, true);//limited to 5 for testing
        tweets.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> tweets){
                List<TwitterTweet> twitterTweets = new ArrayList<>();
                TwitterTweet.deleteAll(TwitterTweet.class);
                List<Tweet> tweetList = tweets.data;
                for(int i=0;i<tweetList.size();i++){
                    Tweet tweet = tweetList.get(i);
                    twitterTweets.add(new TwitterTweet(tweet.text,tweet.createdAt));
                }
                SugarRecord.saveInTx(twitterTweets);
            }

            @Override
            public void failure(TwitterException exception) {
            }
        });
    }
}
