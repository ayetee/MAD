package com.mad.assignment11453798.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mad.assignment11453798.pojo.FacebookEvent;
import com.mad.assignment11453798.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * AccountManagementActivity Class
 * Gets social media access tokens via login
 */
public class AccountManagementActivity extends MainActivity {
    private CallbackManager callbackManager;
    private LoginButton facebookLoginButton;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessTokenFB;
    private TwitterLoginButton twitterLoginButton;

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
        setContentView(R.layout.activity_account_management);

        //Content references
        facebookLoginButton = (LoginButton)findViewById(R.id.facebook_login_button);
        twitterLoginButton = (TwitterLoginButton)findViewById(R.id.twitter_login_button);

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
                Toast.makeText(AccountManagementActivity.this,R.string.success_login_toast,Toast.LENGTH_LONG).show();
                checkFacebookToken();
//                accessTokenFB = loginResult.getAccessToken();
////                tokenCode = accessTokenFB.getToken();
//
//                GraphRequest request = GraphRequest.newMeRequest(accessTokenFB, new GraphRequest.GraphJSONObjectCallback() {
//                    //Overrides onCompleted() - Saves userId and userName
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        Log.e(TAG,object.toString());
//                        Log.e(TAG,response.toString());
//
//                        try {
//                            String userId = object.getString("id");
//                            String userName = object.getString("name");
//                        }
//                        catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                request.executeAsync();
            }

            //Overrides onCancel() - Shows cancel toast
            @Override
            public void onCancel() {
                Toast.makeText(AccountManagementActivity.this,R.string.cancel_login_toast,Toast.LENGTH_LONG).show();
                checkFacebookToken();
            }

            //Overrides onError() - Prints exception
            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
                checkFacebookToken();
            }
        });

        //Changes view on logout THIS NEEDS A BETTER WAY!!
        //maybe the dialog on logout
        facebookLoginButton.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                checkFacebookToken();
            }
        });

        //Handles twitter login functionality
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
//                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                String msg = "";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

//                twitterAuthToken = session.getAuthToken();
                checkTwitterToken();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d(TAG, "Login with Twitter failure", exception);
                checkTwitterToken();
            }
        });

        //Sets view for access token checks
        checkAccessTokens();
    }

    /**
     * Checks for available access tokens and sets view accordingly
     */
    private void checkAccessTokens(){
        checkFacebookToken();
        checkTwitterToken();
    }

    /**
     * Checks for Facebook token and sets view
     */
    private void checkFacebookToken(){
        ImageView facebookEnabled = (ImageView)findViewById(R.id.facebook_login_button_enabled);
        ImageView facebookDisabled = (ImageView)findViewById(R.id.facebook_login_button_disabled);
        if(AccessToken.getCurrentAccessToken()!=null){
            facebookEnabled.setVisibility(View.VISIBLE);
            facebookDisabled.setVisibility(View.GONE);
        }
        else{
            facebookEnabled.setVisibility(View.GONE);
            facebookDisabled.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Checks for twitter tokens and sets view
     */
    private void checkTwitterToken(){
        ImageView twitterEnabled = (ImageView)findViewById(R.id.twitter_login_button_enabled);
        ImageView twitterDisabled = (ImageView)findViewById(R.id.twitter_login_button_disabled);
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (session.getAuthToken()!=null){
            twitterEnabled.setVisibility(View.VISIBLE);
            twitterDisabled.setVisibility(View.GONE);
        }
        else{
            twitterEnabled.setVisibility(View.GONE);
            twitterDisabled.setVisibility(View.VISIBLE);
        }
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
