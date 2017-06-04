package com.mad.assignment11453798.service;

import com.mad.assignment11453798.pojo.TwitterTweet;
import com.orm.SugarRecord;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


/**
 * Twitter Service Class
 */
public class TwitterService {

    /**
     * TwitterService Constructor
     */
    public TwitterService(){
    }

    /**
     * Twitter REST API call
     * Stores the users tweets into Sugar db
     */
    public void getTweets(){
        TwitterSession currentSession = Twitter.getSessionManager().getActiveSession();
        TwitterApiClient twitterApiClient = new TwitterApiClient(currentSession);
        StatusesService statusesService = twitterApiClient.getStatusesService();
        String name = currentSession.getUserName();
        Call<List<Tweet>> tweets = statusesService.userTimeline(null, name, null, null, null, false, false, false, true);//userTimeline(user_id,screen_name,count,since_id,max_id,trim_user,exclude_replies,contributor_details,include_rts);
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
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });
    }
}
