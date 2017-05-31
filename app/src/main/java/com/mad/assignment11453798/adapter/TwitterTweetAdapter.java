package com.mad.assignment11453798.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.assignment11453798.pojo.TwitterTweet;
import com.mad.assignment11453798.R;

import java.util.List;

public class TwitterTweetAdapter extends RecyclerView.Adapter<TwitterTweetAdapter.ViewHolder> {
    private List<TwitterTweet> data;
    private LayoutInflater inflater;

    /**
     * TwitterTweetAdapter Constructor
     */
    public TwitterTweetAdapter(Context context, List<TwitterTweet> eventContent) {
        inflater = LayoutInflater.from(context);
        this.data = eventContent;
    }

    /**
     * ViewHolder for the elements in twitter_event_item
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tweetText;

        //class holds all the UI elements in a single event row
        public ViewHolder(final View itemView) {
            super(itemView);
            tweetText = (TextView)itemView.findViewById(R.id.twitter_tweet_item_tweet_text_tv);
        }
    }

    /**
     * Overrides onCreateViewHolder()
     */
    @Override
    public TwitterTweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_twitter_tweet, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * Overrides onBindViewHolder()
     */
    @Override
    public void onBindViewHolder(TwitterTweetAdapter.ViewHolder holder, int position) {
        TwitterTweet current = data.get(position);
        holder.tweetText.setText(String.valueOf(current.getText()));
    }

    /**
     * Get the number of items in the twitter tweet list
     */
    @Override
    public int getItemCount() {
        return data.size();
    }
}