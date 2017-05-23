package com.mad.assignment11453798.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mad.assignment11453798.Adapter.FacebookEventAdapter;
import com.mad.assignment11453798.Pojo.FacebookEvent;
import com.mad.assignment11453798.R;

import java.util.List;

/**
 * DateContent Class
 * All the necessary content from chosen date
 */
public class DateContentActivity extends CalendarActivity{

    private RecyclerView eventRecyclerView;
    private FacebookEventAdapter facebookEventAdapter;
    private TextView dateTextView;
    private String chosenDateId;

    /**
     * Overrides onCreate()
     * Creates the view for the date's content
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_content);
        eventRecyclerView = (RecyclerView)findViewById(R.id.date_content_event_list);
        dateTextView = (TextView)findViewById(R.id.date_view_date_tv);

        //Sets results from an intent
        Intent results = getIntent();
        onActivityResult(DATE_CONTENT_REQUEST,RESULT_OK,results);

        //Sets facebook events view
        long eventCount = FacebookEvent.count(FacebookEvent.class);
        if(eventCount>0) {
            List<FacebookEvent> facebookEvents = FacebookEvent.find(FacebookEvent.class,"date_id=?",chosenDateId);
            facebookEventAdapter = new FacebookEventAdapter(this,facebookEvents);
        }
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setAdapter(facebookEventAdapter);
    }

    /**
     * Activity to run
     * Given requestCode, resultCode and data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DATE_CONTENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String date = data.getStringExtra(INTENT_DATE) +"/" + data.getStringExtra(INTENT_MONTH) +"/" + data.getStringExtra(INTENT_YEAR);
                chosenDateId = data.getStringExtra(INTENT_DATE_ID);
                dateTextView.setText(date);
            }
        }
    }
}
