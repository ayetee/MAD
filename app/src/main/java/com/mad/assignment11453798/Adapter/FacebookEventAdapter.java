package com.mad.assignment11453798.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.assignment11453798.Pojo.FacebookEvent;
import com.mad.assignment11453798.R;

import java.util.List;

public class FacebookEventAdapter extends RecyclerView.Adapter<FacebookEventAdapter.ViewHolder> {
    private List<FacebookEvent> data;
    private LayoutInflater inflater;

    /**
     * FacebookEventAdapter Constructor
     */
    public FacebookEventAdapter(Context context, List<FacebookEvent> eventContent) {
        inflater = LayoutInflater.from(context);
        this.data = eventContent;
    }

    /**
     * ViewHolder for the elements in fb_event_item
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName,eventDescription,eventDate;

        //class holds all the UI elements in a single event row
        public ViewHolder(final View itemView) {
            super(itemView);
            eventName = (TextView)itemView.findViewById(R.id.fb_event_item_name_tv);
            eventDescription = (TextView)itemView.findViewById(R.id.fb_event_item_description_tv);
//            eventDate = (TextView)itemView.findViewById(R.id.fb_event_item_date_tv);
        }
    }

    /**
     * Overrides onCreateViewHolder()
     */
    @Override
    public FacebookEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fb_event_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * Overrides onBindViewHolder()
     */
    @Override
    public void onBindViewHolder(FacebookEventAdapter.ViewHolder holder, int position) {
        FacebookEvent current = data.get(position);
        holder.eventName.setText(String.valueOf(current.getName()));
        holder.eventDescription.setText(String.valueOf(current.getDescription()));
//        holder.eventDate.setText(String.valueOf(current.getStartTime().getTime()));
    }

    /**
     * Get the number of items in the facebook event list
     */
    @Override
    public int getItemCount() {
        return data.size();
    }
}