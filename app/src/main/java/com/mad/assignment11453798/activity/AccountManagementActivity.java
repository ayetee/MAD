package com.mad.assignment11453798.activity;

/*
    Facebook Account
    margaret_mlxuabf_johnson@tfbnw.net:calendar2017

    Twitter Account
    ihavegonecalmad:calcal17
 */

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.mad.assignment11453798.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

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
                Toast.makeText(getApplicationContext(), R.string.success_login_toast, Toast.LENGTH_LONG).show();
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
            facebookDisabled.setVisibility(View.VISIBLE);
            facebookEnabled.setVisibility(View.GONE);
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
            twitterDisabled.setVisibility(View.VISIBLE);
            twitterEnabled.setVisibility(View.GONE);
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
