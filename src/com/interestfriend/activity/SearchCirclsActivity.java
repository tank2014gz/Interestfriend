package com.interestfriend.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.interestfriend.R;
import com.interestfriend.adapter.MyCircleAdapter;
import com.interestfriend.data.CategoryCircleList;
import com.interestfriend.data.MyCircles;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.interfaces.AbstractTaskPostCallBack;
import com.interestfriend.task.SearchCirclesByCategoryTask;
import com.interestfriend.utils.DialogUtil;

public class SearchCirclsActivity extends BaseActivity implements
		OnItemClickListener {
	private int category = 0;

	private ListView mlistView;
	private TextView txt_title;

	private Dialog dialog;

	private CategoryCircleList lists;

	private MyCircleAdapter adapter;

	private List<MyCircles> listCircles = new ArrayList<MyCircles>();
	private String category_name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_circls);
		category = getIntent().getIntExtra("category", 0);
		category_name = getIntent().getStringExtra("category_name");
		lists = new CategoryCircleList();
		lists.setCategory(category);
		initView();
		setValue();
		getCircleList();
	}

	private void initView() {
		mlistView = (ListView) findViewById(R.id.listview);
		mlistView.setCacheColorHint(0);
		txt_title = (TextView) findViewById(R.id.title_txt);

		setListener();
	}

	private void setListener() {
		mlistView.setOnItemClickListener(this);
	}

	private void setValue() {
		adapter = new MyCircleAdapter(this, listCircles);
		mlistView.setAdapter(adapter);
		txt_title.setText(category_name);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent();
		// intent.putExtra("imgLogo",
		// listCircles.get(position).getCircle_logo());
		// intent.putExtra("description", listCircles.get(position)
		// .getCircle_description());
		// intent.putExtra("circle_id",
		// listCircles.get(position).getCircle_id());
		intent.putExtra("circle", listCircles.get(position));
		intent.setClass(this, CircleInfoActivity.class);
		startActivity(intent);
	}

	private void getCircleList() {
		dialog = DialogUtil.createLoadingDialog(this, "���Ժ�");
		dialog.show();
		SearchCirclesByCategoryTask task = new SearchCirclesByCategoryTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				listCircles.addAll(lists.getListCircles());
				adapter.notifyDataSetChanged();
			}
		});
		task.execute(lists);
	}

}
