package com.neekle.kunlunandroid.screens;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.neekle.kunlunandroid.adapter.CellMicroBlogAdapter;
import com.neekle.kunlunandroid.adapter.CellMicroBlogAdapter.OnAdapterMoreOperationItemListener;
import com.neekle.kunlunandroid.adapter.CellMicroBlogAdapter.OnAdapterOperationBtnClickListener;
import com.neekle.kunlunandroid.data.CellMicroBlogData;
import com.neekle.kunlunandroid.view.controls.cellMicroBlog.CellMicroBlogView.MicroBolgState;
import com.neekle.kunlunandroid.view.controls.cellMicroBlog.CellMicroBlogView.MoreOperationItemClickState;
import com.neekle.kunlunandroid.view.controls.cellMicroBlog.CellMicroBlogView.UserState;
import com.neekle.kunlunandroid.view.controls.cellMicroBlog.R;

/**
 * @author yj Note: 微博控件不仅仅提供如下正常使用设置数据项的功能，还提供比如字体，颜色，背景等的自定义
 */
public class MainActivity extends Activity {

	private ListView mMicroBlogLv;
	private ArrayList<CellMicroBlogData> mArrayList = new ArrayList<CellMicroBlogData>();
	private CellMicroBlogAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewAndSetAttributes();
	}

	private void findViewAndSetAttributes() {
		mMicroBlogLv = (ListView) findViewById(R.id.lv_micro_blog);

		constructData();
		mAdapter = new CellMicroBlogAdapter(this, mArrayList);
		mAdapter.setOnAdapterMoreOperationItemClickListener(mOnAdapterMoreOperationItemListener);
		mAdapter.setOnAdapterOperationBtnClickListener(mOnAdapterOperationBtnClickListener);

		mMicroBlogLv.setAdapter(mAdapter);
	}

	private OnAdapterOperationBtnClickListener mOnAdapterOperationBtnClickListener = new OnAdapterOperationBtnClickListener() {

		@Override
		public void onRemarkMicroBlogClick() {
			int duration = Toast.LENGTH_SHORT;
			String text = MainActivity.this.getString(R.string.remark);
			Toast.makeText(MainActivity.this, text, duration).show();
		}

		@Override
		public void onForwardMicroBlogClick() {
			int duration = Toast.LENGTH_SHORT;
			String text = MainActivity.this.getString(R.string.forward);
			Toast.makeText(MainActivity.this, text, duration).show();
		}

		@Override
		public void onDeleteMicroBlogClick() {
			int duration = Toast.LENGTH_SHORT;
			String text = MainActivity.this.getString(R.string.delete);
			Toast.makeText(MainActivity.this, text, duration).show();
		}

		@Override
		public void onCollectMicroBlogClick() {
			int duration = Toast.LENGTH_SHORT;
			String text = MainActivity.this.getString(R.string.collect);
			Toast.makeText(MainActivity.this, text, duration).show();
		}
	};

	private OnAdapterMoreOperationItemListener mOnAdapterMoreOperationItemListener = new OnAdapterMoreOperationItemListener() {

		@Override
		public void onItemClick(int itemId) {
			String text = "";

			switch (itemId) {
			case MoreOperationItemClickState.MAKE_PHONE_CALL_CLICK: {
				text = MainActivity.this
						.getString(R.string.cellmicroblog_make_phone_call);
				break;
			}
			case MoreOperationItemClickState.MAKE_VIDEO_CALL_CLICK: {
				text = MainActivity.this
						.getString(R.string.cellmicroblog_make_video_call);
				break;
			}
			case MoreOperationItemClickState.SEND_MSG_CLICK: {
				text = MainActivity.this
						.getString(R.string.cellmicroblog_send_instant_msg);
				break;
			}
			case MoreOperationItemClickState.SEND_EMAIL_CLICK: {
				text = MainActivity.this
						.getString(R.string.cellmicroblog_send_email);
				break;
			}
			case MoreOperationItemClickState.ADD_AS_FRIEND_CLICK: {
				text = MainActivity.this
						.getString(R.string.cellmicroblog_add_as_friend);
				break;
			}
			}

			int duration = Toast.LENGTH_SHORT;
			Toast.makeText(MainActivity.this, text, duration).show();
		}

	};

	private void constructData() {
		Resources resources = getResources();
		Drawable drawable = resources
				.getDrawable(R.drawable.cellmicroblog_def_photo);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		Bitmap userPhoto = bitmapDrawable.getBitmap();

		int userState = UserState.XMPP_IDLE;
		String name = "Jolly";
		String publishTime = "1小时25分30秒前";
		String publishCity = "NanJing";
		int microBlogState = MicroBolgState.SUCCESS;
		String microBlogContent = "I will go to America";
		String microBlogStateDescri = "Sending ok";
		String device = "Iphone";
		CellMicroBlogData data1 = new CellMicroBlogData(userState, userPhoto,
				name, publishTime, publishCity, microBlogState,
				microBlogContent, microBlogStateDescri, device);
		mArrayList.add(data1);

		userState = UserState.XMPP_LEAVE;
		name = "Hary";
		publishTime = "1小时25分30秒前";
		publishCity = "ShangHai";
		microBlogState = MicroBolgState.FAILURE;
		microBlogContent = "I will go to America";
		microBlogStateDescri = "Sending error";
		device = "Android";
		CellMicroBlogData data2 = new CellMicroBlogData(userState, userPhoto,
				name, publishTime, publishCity, microBlogState,
				microBlogContent, microBlogStateDescri, device);
		mArrayList.add(data2);

		userState = UserState.KUNLUN_BUSY;
		name = "Obama";
		publishTime = "1小时25分30秒前";
		publishCity = "WuHan";
		microBlogState = MicroBolgState.FAILURE;
		microBlogContent = "I will go to America";
		microBlogStateDescri = "Sending error";
		device = "Web";
		CellMicroBlogData data3 = new CellMicroBlogData(userState, userPhoto,
				name, publishTime, publishCity, microBlogState,
				microBlogContent, microBlogStateDescri, device);
		mArrayList.add(data3);

		userState = UserState.KUNLUN_BUSY;
		name = "Lucy";
		publishTime = "1小时25分30秒前";
		publishCity = "WuHan";
		microBlogState = MicroBolgState.SUCCESS;
		microBlogContent = "I will go to America";
		microBlogStateDescri = "Sending success";
		device = "Pc";
		CellMicroBlogData data4 = new CellMicroBlogData(userState, userPhoto,
				name, publishTime, publishCity, microBlogState,
				microBlogContent, microBlogStateDescri, device);
		mArrayList.add(data4);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
