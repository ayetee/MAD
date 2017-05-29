package com.mad.assignment11453798.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import com.google.gson.Gson;
import com.mad.assignment11453798.Pojo.FacebookEvent;
import com.mad.assignment11453798.Pojo.TwitterTweet;
import com.mad.assignment11453798.R;
import com.mad.assignment11453798.Service.TwitterService;
import com.orm.SugarRecord;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TimelineResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * MainActivity Class
 * The landing page
 * Has the Login buttons for the social medias
 * and View Calendar button
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final int VIEW_CALENDAR_REQUEST = 1;
    private CallbackManager callbackManager;
    private LoginButton facebookLoginButton;
    private Toast toasties;
    private static String tokenCode, userId, userName;
    private Button viewCalendar;
    private AccessTokenTracker accessTokenTracker;
    private String eventId,eventName,eventDesciption;
    private AccessToken accessTokenFB;
    private List<FacebookEvent> facebookEvent;
    private TwitterLoginButton twitterLoginButton;
    private TwitterAuthToken twitterAuthToken;

    /**
     * Overrides onCreate()
     * Initiates Login Buttons
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Resources res = getResources();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(res.getString(R.string.twitter_app_id),res.getString(R.string.twitter_app_secret));
        Fabric.with(this, new Twitter(authConfig), new Crashlytics());
        setContentView(R.layout.activity_main);

        //Content references
        facebookLoginButton = (LoginButton)findViewById(R.id.facebook_login_button);
        twitterLoginButton = (TwitterLoginButton)findViewById(R.id.twitter_login_button);
        viewCalendar = (Button)findViewById(R.id.main_view_calendar_btn);

        //View calender button
        viewCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calendarScreen = new Intent(getApplicationContext(),CalendarActivity.class);
//                startActivity(calendarScreen);
                startActivityForResult(calendarScreen,VIEW_CALENDAR_REQUEST);
            }
        });

        callbackManager = CallbackManager.Factory.create();

        //Start accessTokenTracker
        accessTokenTracker = new AccessTokenTracker(){
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken,AccessToken newToken){
            }
        };
        accessTokenTracker.startTracking();

        //Handles Facebook permissions and login functionality
        facebookLoginButton.setReadPermissions("user_events");
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            //Overrides onSuccess() - Saves accessTokenFB and Graph API request
            @Override
            public void onSuccess(LoginResult loginResult) {
                toasties = Toast.makeText(MainActivity.this,R.string.success_login_toast,Toast.LENGTH_LONG);
                toasties.show();
                accessTokenFB = loginResult.getAccessToken();
                tokenCode = accessTokenFB.getToken();

                GraphRequest request = GraphRequest.newMeRequest(accessTokenFB, new GraphRequest.GraphJSONObjectCallback() {
                    //Overrides onCompleted() - Saves userId and userName
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e(TAG,object.toString());
                        Log.e(TAG,response.toString());

                        try {
                            userId = object.getString("id");
                            userName = object.getString("name");
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                request.executeAsync();
            }

            //Overrides onCancel() - Shows error toast
            @Override
            public void onCancel() {
                toasties = Toast.makeText(MainActivity.this,R.string.cancel_login_toast,Toast.LENGTH_LONG);
                toasties.show();
            }

            //Overrides onError() - Prints exception
            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
            }
        });

        //handles twitter login functionality
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                twitterAuthToken = session.getAuthToken();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d(TAG, "Login with Twitter failure", exception);
            }
        });
    }

    /**
     * Overrides onCreateOptionsMenu()
     * Creates menu in action bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Overrides onOptionItemSelected()
     * Listens for chosen menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            //Open up settings activity
            Intent settingsScreen = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(settingsScreen);
//            startActivityForResult(calendarScreen,VIEW_CALENDAR_REQUEST);
        }
        if (id == R.id.menu_quit) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Overrides onActivityResult()
     * Parses the requestCode, resultCode and intent onto the CallbackManager
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode,resultCode,data);
    }

    /**
     * Override onStop()
     * Stops accessTokenTracker
     */
    @Override
    protected void onStop(){
        accessTokenTracker.stopTracking();
        super.onStop();
    }
}
