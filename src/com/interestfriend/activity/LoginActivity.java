package com.interestfriend.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.interestfriend.R;
import com.interestfriend.applation.MyApplation;
import com.interestfriend.data.User;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.findpassword.FindPasswordActivity;
import com.interestfriend.interfaces.MyEditTextWatcher;
import com.interestfriend.interfaces.MyEditTextWatcher.OnTextLengthChange;
import com.interestfriend.interfaces.OnEditFocusChangeListener;
import com.interestfriend.utils.DialogUtil;
import com.interestfriend.utils.MD5;
import com.interestfriend.utils.SharedUtils;
import com.interestfriend.utils.ToastUtil;
import com.interestfriend.utils.Utils;
import com.interestfriend.view.MyEditTextDeleteImg;

public class LoginActivity extends BaseActivity implements OnClickListener,
		OnTextLengthChange {
	private MyEditTextDeleteImg edit_telphone;
	private MyEditTextDeleteImg edit_password;
	private Button btn_login;
	private Button btn_find_password;
	private ImageView back;
	private TextView txt_title;

	private Dialog dialog;

	private int user_id;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				ToastUtil.showToast("��¼�ɹ�", Toast.LENGTH_SHORT);
				startActivity(new Intent(LoginActivity.this, HomeActivity.class));
				finish();
				break;
			case -1:
				ToastUtil.showToast("�ֻ��Ų�����", Toast.LENGTH_SHORT);
				break;
			case -2:
				ToastUtil.showToast("�������", Toast.LENGTH_SHORT);
				break;
			case -3:
				ToastUtil.showToast("��¼ʧ��", Toast.LENGTH_SHORT);
				break;
			case 2:
				if (dialog != null) {
					dialog.dismiss();
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("��¼");
		edit_password = (MyEditTextDeleteImg) findViewById(R.id.edit_password);
		edit_telphone = (MyEditTextDeleteImg) findViewById(R.id.edit_telphone);
		edit_telphone.setTag("phone_num");
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_find_password = (Button) findViewById(R.id.btn_findPasswrod);
		setListener();
	}

	private void setListener() {
		edit_telphone.setOnFocusChangeListener(new OnEditFocusChangeListener(
				edit_telphone, this));
		edit_telphone.addTextChangedListener(new MyEditTextWatcher(
				edit_telphone, this, this));
		edit_password.addTextChangedListener(new MyEditTextWatcher(
				edit_password, this, this));
		edit_password.setOnFocusChangeListener(new OnEditFocusChangeListener(
				edit_password, this));
		btn_login.setOnClickListener(this);
		btn_find_password.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onTextLengthChanged(boolean isBlank) {
		if (!isBlank) {
			if (edit_password.getText().toString().length() != 0
					&& edit_telphone.getText().toString().length() != 0) {
				btn_login.setEnabled(true);
				btn_login.setBackgroundResource(R.drawable.btn_selector);
				return;
			}
		}
		btn_login.setEnabled(false);
		btn_login.setBackgroundResource(R.drawable.btn_disenable_bg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			String user_cellphone = edit_telphone.getText().toString()
					.replaceAll(" ", "");
			String user_password = edit_password.getText().toString();
			if (!Utils.isPhoneNum(user_cellphone)) {
				ToastUtil.showToast("�ֻ��Ÿ�ʽ����ȷ", Toast.LENGTH_SHORT);
				return;
			}
			if (!Utils.isNetworkAvailable()) {
				ToastUtil.showToast("�������,��������", Toast.LENGTH_SHORT);

				return;
			}
			dialog = DialogUtil.createLoadingDialog(this, "���Ժ�");
			dialog.setCancelable(false);
			dialog.show();
			login(user_cellphone, user_password);
			break;
		case R.id.btn_findPasswrod:
			startActivity(new Intent(this, FindPasswordActivity.class));
			Utils.leftOutRightIn(this);
			break;
		case R.id.back:
			finishThisActivity();
		default:
			break;
		}
	}

	private void login(final String user_cellphone, final String user_password) {
		new Thread() {
			public void run() {
				User user = new User();
				user.setUser_cellphone(user_cellphone);
				user.setUser_password(user_password);
				RetError ret = user.userLogin();
				if (ret == RetError.NONE) {
					MyApplation.initMemberSelf();
					user_id = user.getUser_id();
					loginHuanXin(MD5.Md5_16(user_cellphone),
							MD5.Md5_16(user_cellphone));
					// loginHuanXin(user_cellphone, user_password);
				} else if (ret == RetError.NOT_EXIST_USER) {
					mHandler.sendEmptyMessage(2);
					mHandler.sendEmptyMessage(-1);
				} else if (ret == RetError.WRONG_PASSWORD) {
					mHandler.sendEmptyMessage(2);
					mHandler.sendEmptyMessage(-2);
				} else {
					mHandler.sendEmptyMessage(2);
					mHandler.sendEmptyMessage(-3);
				}
			}
		}.start();
	}

	private void loginHuanXin(final String username, final String password) {
		Utils.print("logion:::::::::::::===" + username + "    " + password);
		// ����sdk��½������½���������
		EMChatManager.getInstance().login(username, password, new EMCallBack() {

			@Override
			public void onSuccess() {
				SharedUtils.setUid(user_id + "");
				Utils.print("uit::::::::::::::::" + user_id);
				// ��½�ɹ��������û�������
				SharedUtils.setUserName(username);
				SharedUtils.saveHuanXinPassword(password);
				SharedUtils.saveHXId(username);
				mHandler.sendEmptyMessage(2);
				mHandler.sendEmptyMessage(1);
				try {
					EMGroupManager.getInstance().getGroupsFromServer();
				} catch (EaseMobException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, final String message) {

				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(),
								"��¼ʧ��: " + message, 0).show();
						Utils.print("logion:::::::::::::" + message);
						if (dialog != null) {
							dialog.dismiss();
						}

					}
				});
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
	}
}
