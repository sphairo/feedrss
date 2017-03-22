package com.amco.dev.podcast.feedrssmanager.Dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.amco.dev.podcast.feedrssmanager.Adapters.FeedDetailsRecyclerAdapter;
import com.amco.dev.podcast.feedrssmanager.R;
import com.amco.dev.podcast.rssmanager.Channel;

public class FeedDetailsDialog extends AlertDialog.Builder {

    public FeedDetailsDialog(@NonNull Context context) {
        super(context);
    }

    public FeedDetailsDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public FeedDetailsDialog attachDetails(Channel.Item item) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_feed_details, null);
        setView(view);

        RecyclerView recyclerViewFeedDetails = (RecyclerView) view.findViewById(R.id.recycler_view_feed_details);

        FeedDetailsRecyclerAdapter feedDetailsRecyclerAdapter = new FeedDetailsRecyclerAdapter(item);
        recyclerViewFeedDetails.setAdapter(feedDetailsRecyclerAdapter);

        return this;
    }
}
