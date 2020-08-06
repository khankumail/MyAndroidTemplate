package com.app.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.music.R;
import com.app.music.model.MusicAlbum;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterGridAlbum extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MusicAlbum> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private boolean clicked = false;

    public interface OnItemClickListener {
        void onItemClick(View view, MusicAlbum obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterGridAlbum(Context context, List<MusicAlbum> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView brief;
        public LinearLayout lyt_bottom;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
            brief = (TextView) v.findViewById(R.id.brief);
            lyt_bottom = (LinearLayout) v.findViewById(R.id.lyt_bottom);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MusicAlbum obj = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.name.setText(obj.name);
            view.brief.setText(obj.brief);
            Picasso.with(ctx).load(obj.image).into(view.image);
            view.lyt_bottom.setBackgroundColor(obj.color);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!clicked && mOnItemClickListener != null) {
                        clicked = true;
                        mOnItemClickListener.onItemClick(view, obj, position);
                    }
                }
            });
            clicked = false;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}