package com.app.music.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.music.R;
import com.app.music.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class AdapterPlaylist extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Playlist> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnMoreButtonClickListener onMoreButtonClickListener;

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    public AdapterPlaylist(Context context, List<Playlist> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public View lyt_parent;
        public ImageView more;

        public OriginalViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            more = (ImageView) v.findViewById(R.id.more);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final Playlist p = items.get(position);
            view.title.setText(p.title);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });


            view.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMoreButtonClickListener == null) return;
                    onMoreButtonClick(view, p, position);
                }
            });
        }
    }

    private void onMoreButtonClick(final View view, final Playlist p, final int pos) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, p, item, pos);
                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_more_playlist);
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Playlist getItem(int position) {
        return items.get(position);
    }

    public void remove(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    private void update(int position, Playlist p) {
        items.set(position, p);
        notifyItemChanged(position);
    }

    private void add(Playlist p) {
        items.add(p);
        notifyItemInserted(getItemCount() - 1);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Playlist obj, int pos);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, Playlist obj, MenuItem item, int pos);
    }

}