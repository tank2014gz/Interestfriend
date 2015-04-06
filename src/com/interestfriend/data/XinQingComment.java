package com.interestfriend.data;

import java.util.HashMap;

import com.interestfriend.applation.MyApplation;
import com.interestfriend.data.enums.RetError;
import com.interestfriend.data.enums.RetStatus;
import com.interestfriend.data.result.ApiRequest;
import com.interestfriend.data.result.Result;
import com.interestfriend.data.result.StringResult;
import com.interestfriend.parser.IParser;
import com.interestfriend.parser.StringParser;
import com.interestfriend.utils.SharedUtils;

public class XinQingComment extends AbstractData {
	private final String COMMENT_API = "XinQingCommentServlet";

	private int comment_id;
	private int xinqing_id;
	private int publisher_id;
	private String comment_content = "";
	private String comment_time = "";
	private String publisher_name = "";
	private String publisher_avatar = "";
	private String reply_someone_name = "";
	private int reply_someone_id = 0;

	public String getReply_someone_name() {
		return reply_someone_name;
	}

	public void setReply_someone_name(String reply_someone_name) {
		this.reply_someone_name = reply_someone_name;
	}

	public int getReply_someone_id() {
		return reply_someone_id;
	}

	public void setReply_someone_id(int reply_someone_id) {
		this.reply_someone_id = reply_someone_id;
	}

	public String getPublisher_name() {
		return publisher_name;
	}

	public void setPublisher_name(String publisher_name) {
		this.publisher_name = publisher_name;
	}

	public String getPublisher_avatar() {
		return publisher_avatar;
	}

	public void setPublisher_avatar(String publisher_avatar) {
		this.publisher_avatar = publisher_avatar;
	}

	public int getPublisher_id() {
		return publisher_id;
	}

	public void setPublisher_id(int publisher_id) {
		this.publisher_id = publisher_id;
	}

	public int getComment_id() {
		return comment_id;
	}

	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}

	public int getXinqing_id() {
		return xinqing_id;
	}

	public void setXinqing_id(int xinqing_id) {
		this.xinqing_id = xinqing_id;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

	public String getComment_time() {
		return comment_time;
	}

	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "comment_content:" + this.comment_content;
	}

	public RetError sendComment(int xinqing_publisher_id) {
		IParser parser = new StringParser("comment_id");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("comment_content", comment_content);
		params.put("xinqing_id", xinqing_id);
		params.put("reply_someone_name", reply_someone_name);
		params.put("reply_someone_id", reply_someone_id);
		params.put("xinqing_publisher_id", xinqing_publisher_id);
		params.put("user_name", SharedUtils.getAPPUserName());
		Result ret = ApiRequest.request(COMMENT_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			StringResult sr = (StringResult) ret;
			this.comment_id = Integer.valueOf(sr.getStr());
			return RetError.NONE;
		} else {
			return ret.getErr();
		}

	}

}
