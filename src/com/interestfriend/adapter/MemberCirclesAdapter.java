package com.interestfriend.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.data.MyCircles;
import com.interestfriend.utils.UniversalImageLoadTool;
import com.interestfriend.view.RoundAngleImageView;

public class MemberCirclesAdapter extends BaseAdapter {
	private List<MyCircles> list = new ArrayList<MyCircles>();
	private Context mContext;

	public MemberCirclesAdapter(Context context, List<MyCircles> list) {
		this.mContext = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (contentView == null) {
			contentView = LayoutInflater.from(mContext).inflate(
					R.layout.member_circles_item, null);
			holder = new ViewHolder();
			holder.img_circle_logo = (RoundAngleImageView) contentView
					.findViewById(R.id.img_circle_logo);
			holder.txt_circle_name = (TextView) contentView
					.findViewById(R.id.txt_circle_name);
			holder.txt_circle_desc = (TextView) contentView
					.findViewById(R.id.txt_circle_desc);
			holder.txt_circle_member_num = (TextView) contentView
					.findViewById(R.id.circle_member_num);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		holder.txt_circle_name.setText(list.get(position).getCircle_name());
		holder.txt_circle_desc.setText(list.get(position)
				.getCircle_description());
		holder.txt_circle_member_num.setText(list.get(position)
				.getCircle_member_num() + "");
		UniversalImageLoadTool.disPlay(list.get(position).getCircle_logo(),
				holder.img_circle_logo, R.drawable.picture_default_head);
		return contentView;
	}

	static class ViewHolder {
		private RoundAngleImageView img_circle_logo;
		private TextView txt_circle_name;
		private TextView txt_circle_desc;
		private TextView txt_circle_member_num;
	}
}
