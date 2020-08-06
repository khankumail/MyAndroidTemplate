package com.app.sample.messenger.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.sample.messenger.R;
import com.app.sample.messenger.model.GroupDetails;

import java.util.List;

public class GroupDetailsListAdapter extends BaseAdapter {

	private List<GroupDetails> mMessages;
	private Context ctx;

	public GroupDetailsListAdapter(Context context, List<GroupDetails> messages) {
        super();
        this.ctx = context;
        this.mMessages = messages;
	}
	
	@Override
	public int getCount() {
		return mMessages.size();
	}

	@Override
	public Object getItem(int position) {
		return mMessages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mMessages.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GroupDetails g = (GroupDetails) getItem(position);
        ViewHolder holder;
        if(convertView == null){
        	holder 				= new ViewHolder();
        	convertView			= LayoutInflater.from(ctx).inflate(R.layout.row_group_details, parent, false);
			holder.sender 		= (TextView) convertView.findViewById(R.id.sender);
        	holder.time 		= (TextView) convertView.findViewById(R.id.text_time);
        	holder.message 		= (TextView) convertView.findViewById(R.id.text_content);
			holder.lyt_thread 	= (CardView) convertView.findViewById(R.id.lyt_thread);
			holder.lyt_parent 	= (LinearLayout) convertView.findViewById(R.id.lyt_parent);
			holder.image_status	= (ImageView) convertView.findViewById(R.id.image_status);
        	convertView.setTag(holder);	
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
		holder.sender.setText(g.getFriend().getName());
        holder.message.setText(g.getContent());
		holder.time.setText(g.getDate());

        if(g.isFromMe()){
			holder.sender.setVisibility(View.GONE);
			holder.lyt_parent.setPadding(100, 10, 15, 10);
			holder.lyt_parent.setGravity(Gravity.RIGHT);
			holder.lyt_thread.setCardBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary));
			holder.time.setTextColor(Color.WHITE);
			holder.message.setTextColor(Color.WHITE);
        }else{
			holder.sender.setVisibility(View.VISIBLE);
			holder.lyt_parent.setPadding(15, 10, 100, 10);
			holder.lyt_parent.setGravity(Gravity.LEFT);
			holder.lyt_thread.setCardBackgroundColor(ctx.getResources().getColor(R.color.grey_soft));
			holder.time.setTextColor(ctx.getResources().getColor(R.color.grey_dark));
			holder.message.setTextColor(ctx.getResources().getColor(R.color.grey_dark));
        }
        return convertView;
	}

	/**
	 * remove data item from messageAdapter
	 * 
	 **/
	public void remove(int position){
		mMessages.remove(position);
	}
	
	/**
	 * add data item to messageAdapter
	 * 
	 **/
	public void add(GroupDetails msg){
		mMessages.add(msg);
	}
	
	private static class ViewHolder{
		TextView sender;
		TextView time;
		TextView message;
		LinearLayout lyt_parent;
		CardView lyt_thread;
		ImageView image_status;
	}	
}
