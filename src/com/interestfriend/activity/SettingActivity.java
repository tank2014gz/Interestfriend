package com.interestfriend.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.interestfriend.R;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.interfaces.ConfirmDialog;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.Utils;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private TextView txt_message_prompt;
	private TextView txt_feddback;
	private TextView txt_new_version;
	private TextView txt_about;
	private ImageView back;
	private TextView txt_title;
	private Button btn_quit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
	}

	private void initView() {
		btn_quit = (Button) findViewById(R.id.btn_quit);
		txt_about = (TextView) findViewById(R.id.txt_about);
		txt_message_prompt = (TextView) findViewById(R.id.txt_message_prompt);
		txt_feddback = (TextView) findViewById(R.id.txt_feedback);
		txt_new_version = (TextView) findViewById(R.id.txt_new_version);
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("设置");
		setListener();
	}

	private void setListener() {
		txt_about.setOnClickListener(this);
		txt_new_version.setOnClickListener(this);
		txt_message_prompt.setOnClickListener(this);
		txt_feddback.setOnClickListener(this);
		back.setOnClickListener(this);
		btn_quit.setOnClickListener(this);
	}

	private void quitPrompt() {
		Dialog dialog = DialogUtil.confirmDialog(this, "确定要退出吗?", "确定", "取消",
				new ConfirmDialog() {
					@Override
					public void onOKClick() {
						quit();
					}

					@Override
					public void onCancleClick() {
					}
				});
		dialog.show();
	}

	private void quit() {
		MyApplation.logoutHuanXin();
		MyApplation.exit(false);
		SharedUtils.setUid(0 + "");
		startActivity(new Intent(this, LoginActivity.class));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_quit:
			quitPrompt();
			break;
		case R.id.txt_feedback:
			startActivity(new Intent(this, FeedBackActivity.class));
			Utils.leftOutRightIn(this);
			break;
		case R.id.txt_message_prompt:
			startActivity(new Intent(this, MessageWarnctivity.class));
			Utils.leftOutRightIn(this);
			break;
		default:
			break;
		}
	}
}
