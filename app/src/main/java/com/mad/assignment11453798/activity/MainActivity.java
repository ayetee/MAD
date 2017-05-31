package com.mad.assignment11453798.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mad.assignment11453798.R;


/**
 * MainActivity Class
 * The landing page
 * Has the Login buttons for the social medias
 * and View Calendar button
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final int VIEW_CALENDAR_REQUEST = 1;
    private Button viewCalendarBtn;

    /**
     * Overrides onCreate()
     * Initiates View Calendar Button
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Content references
        viewCalendarBtn = (Button)findViewById(R.id.main_view_calendar_btn);

        //View calender button
        viewCalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calendarScreen = new Intent(getApplicationContext(),CalendarActivity.class);
                startActivityForResult(calendarScreen,VIEW_CALENDAR_REQUEST);
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
        switch (item.getItemId()) {
            //Opens activity to sign in to social media
            case R.id.menu_accounts:
                Intent accountsScreen = new Intent(this, AccountManagementActivity.class);
                startActivity(accountsScreen);
                return true;
            //Opens up settings menu
            case R.id.menu_settings:
                Intent preferenceScreen = new Intent(this, SettingsActivity.class);
                startActivity(preferenceScreen);
                return true;
            //Quits application
            case R.id.menu_quit:
                finishAffinity();
//                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
