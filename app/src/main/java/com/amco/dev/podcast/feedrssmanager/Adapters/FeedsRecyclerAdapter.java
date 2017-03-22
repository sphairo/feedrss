package com.amco.dev.podcast.feedrssmanager.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amco.dev.podcast.feedrssmanager.R;
import com.amco.dev.podcast.rssmanager.Channel;
import com.amco.dev.podcast.rssmanager.RSS;

import java.util.ArrayList;
import java.util.List;

public class FeedsRecyclerAdapter extends RecyclerView.Adapter<FeedsRecyclerAdapter.ViewHolder> {
    private List<Channel.Item> items = new ArrayList<>();
    private OnFeedItemClickListener onFeedItemClickListener;

    public FeedsRecyclerAdapter() {

    }

    public FeedsRecyclerAdapter(List<RSS> rssList) {
        for (RSS rss : rssList) {
            items.addAll(rss.getChannel().getItems());
        }
    }

    public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
        this.onFeedItemClickListener = onFeedItemClickListener;
    }

    @Override
    public FeedsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FeedsRecyclerAdapter.ViewHolder holder, int position) {
        holder.txtFeedTitle.setText(items.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFeedItemClickListener != null) {
                    onFeedItemClickListener.onItemClick(holder.getAdapterPosition(),
                            items.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnFeedItemClickListener {
        void onItemClick(int position, Channel.Item item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtFeedTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            txtFeedTitle = (TextView) itemView.findViewById(R.id.text_view_feed_title);
        }
    }
}
