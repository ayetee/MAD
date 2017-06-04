package com.mad.assignment11453798.service;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.mad.assignment11453798.pojo.FacebookEvent;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Facebook Service Class
 */

public class FacebookService {

    /**
     * FacebookService Constructor
     */
    public FacebookService(){
    }

    /**
     * Facebook Graph API call
     * Stores the users events into Sugar db
     */
    public void getFacebookEvents(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), "/me/events", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
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
                            Log.d("FacebookEventsGet",e.toString());
                        }
                    }
                }
        ).executeAsync();
    }
}
