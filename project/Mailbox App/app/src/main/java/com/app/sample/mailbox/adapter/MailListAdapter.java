package com.app.sample.mailbox.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.sample.mailbox.ActivityMailDetails;
import com.app.sample.mailbox.R;
import com.app.sample.mailbox.data.GlobalVariable;
import com.app.sample.mailbox.model.Mail;
import com.app.sample.mailbox.widget.CircleTransform;
import com.squareup.picasso.Picasso;

import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MailListAdapter extends RecyclerView.Adapter<MailListAdapter.ViewHolder> implements Filterable  {

    public static final int INBOX_MODE = 0;
    public static final int OUTBOX_MODE = 1;
    public static final int TRASH_MODE = 2;

    private List<Mail> original_items = new ArrayList<>();
    private List<Mail> filtered_items = new ArrayList<>();
    private ItemFilter mFilter = new ItemFilter();

    private Context ctx;
    private GlobalVariable global;
    private int mode;

    // for item click listener
    private OnItemClickListener mOnItemClickListener;
    private boolean clicked = false;

    public interface OnItemClickListener {
        void onItemClick(View view, Mail obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView subject;
        public TextView content;
        public TextView time;
        public ImageView image;
        public LinearLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            subject = (TextView) v.findViewById(R.id.subject);
            content = (TextView) v.findViewById(R.id.content);
            time = (TextView) v.findViewById(R.id.time);
            image = (ImageView) v.findViewById(R.id.image);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
        }

    }

    public Filter getFilter() {
        return mFilter;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MailListAdapter(int mode, Activity activity, List<Mail> items) {
        this.mode = mode;
        global = (GlobalVariable) activity.getApplication();
        this.ctx = activity;
        original_items = items;
        filtered_items = items;
    }

    @Override
    public MailListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mail, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Mail c = filtered_items.get(position);
        holder.title.setText(c.getSender().getName());

        holder.time.setText(c.getDate());
        holder.subject.setText(c.getSubject());
        holder.content.setText(c.getContent());
        Picasso.with(ctx).load(c.getSender().getPhoto())
                .resize(100, 100)
                .transform(new CircleTransform())
                .into(holder.image);

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView, position);

        // view detail message conversation
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!clicked && mOnItemClickListener != null) {
                    clicked = true;
                    mOnItemClickListener.onItemClick(view, c, position);
                }
            }
        });
        clicked = false;
    }


    public void remove(int position) {
        filtered_items = trashGlobalData(position);
        notifyDataSetChanged();
    }

    public Mail getItem(int position){
        return filtered_items.get(position);
    }

    public void setItems(List<Mail> items){
        filtered_items = items;
        notifyDataSetChanged();
    }

    public void add(int position, Mail mail){
        filtered_items.add(position, mail);
        notifyDataSetChanged();
        updateGlobalData(filtered_items);
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(filtered_items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(filtered_items, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        updateGlobalData(filtered_items);
        return true;
    }

    /**
     * Here is the key method to apply the animation
     */
    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filtered_items.size();
    }

    @Override
    public long getItemId(int position) {
        return filtered_items.get(position).getId();
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final List<Mail> list = original_items;
            final List<Mail> result_list = new ArrayList<>(list.size());

            for (int i = 0; i < list.size(); i++) {
                String str_title = list.get(i).getSender().getName();
                if (str_title.toLowerCase().contains(query)) {
                    result_list.add(list.get(i));
                }
            }

            results.values = result_list;
            results.count = result_list.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered_items = (List<Mail>) results.values;
            notifyDataSetChanged();
        }

    }

    private void updateGlobalData(List<Mail> items){
        switch (mode){
            case INBOX_MODE :
                global.setInbox(items);
                break;
            case OUTBOX_MODE :
                global.setOutbox(items);
                break;
            case TRASH_MODE :
                break;
        }
    }

    private List<Mail> trashGlobalData(int position){
        switch (mode){
            case INBOX_MODE :
                global.trashInbox(position);
                return global.getInbox();
            case OUTBOX_MODE :
                global.trashOutbox(position);
                return global.getOutbox();
            case TRASH_MODE :
                break;
        }

        return new ArrayList<>();
    }
}