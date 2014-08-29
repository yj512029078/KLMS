package com.neekle.kunlunandroid.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.neekle.kunlunandroid.data.CellMicroBlogData;
import com.neekle.kunlunandroid.view.controls.cellMicroBlog.CellMicroBlogView;
import com.neekle.kunlunandroid.view.controls.cellMicroBlog.R;
import com.neekle.kunlunandroid.view.controls.cellMicroBlog.CellMicroBlogView.OnMoreOperationItemListener;
import com.neekle.kunlunandroid.view.controls.cellMicroBlog.CellMicroBlogView.OnOperationBtnClickListener;

public class CellMicroBlogAdapter extends BaseAdapter {

	private List<CellMicroBlogData> mDataList;  

	private Context mContext;
	private LayoutInflater mLayoutInflater;

	private OnAdapterOperationBtnClickListener mOnAdapterOperationBtnClickListener;
	private OnAdapterMoreOperationItemListener mOnAdapterMoreOperationItemListener;

	public CellMicroBlogAdapter(Context context) {
		this.mContext = context;
		String inflater = Context.LAYOUT_INFLATER_SERVICE;
		mLayoutInflater = (LayoutInflater) context.getSystemService(inflater);
	}

	public CellMicroBlogAdapter(Context context,
			List<CellMicroBlogData> datalist) {
		this.mContext = context;
		mDataList = datalist;
		String inflater = Context.LAYOUT_INFLATER_SERVICE;
		mLayoutInflater = (LayoutInflater) context.getSystemService(inflater);
	}

	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CellMicroBlogData data = mDataList.get(position);

		View view = mLayoutInflater.inflate(R.layout.cellmicroblog_adapter,
				null);

		View subView = view.findViewById(R.id.cellmicroblog);
		CellMicroBlogView cellMicroBlogView = (CellMicroBlogView) (subView);

		setListener(cellMicroBlogView);
		setInfo(cellMicroBlogView, data);

		return view;
	}

	public void setData(List<CellMicroBlogData> dataList) {
		this.mDataList = dataList;
	}

	private void setInfo(CellMicroBlogView cellMicroBlogView,
			CellMicroBlogData data) {
		int state = data.getUserState();
		Bitmap photo = data.getUserPhoto();
		String name = data.getName();
		String publishTime = data.getPublishTime();
		String publishCity = data.getPublishCity();
		String microBlogContent = data.getMicroBlogContent();
		int microBlogState = data.getMicroBlogState();
		String microBlogStateDescri = data.getMicroBlogStateDescri();
		String device = data.getDevice();

		cellMicroBlogView.setUserState(state);
		cellMicroBlogView.setUserPhoto(photo);

		int width = photo.getWidth();
		int height = photo.getHeight();
		Log.i("photo", "width: " + width);
		Log.i("photo", "height: " + height);

		cellMicroBlogView.setName(name);
		cellMicroBlogView.setPublishTime(publishTime);
		cellMicroBlogView.setPublishCity(publishCity);
		cellMicroBlogView.setMicroBlogContent(microBlogContent);
		cellMicroBlogView.setMicroBlogState(microBlogState);
		cellMicroBlogView.setMicroBlogStateDescri(microBlogStateDescri);
		cellMicroBlogView.setDevice(device);
	}

	private void setListener(CellMicroBlogView cellMicroBlogView) {
		cellMicroBlogView
				.setOnOperationBtnClickListener(new OnOperationBtnClickListener() {

					@Override
					public void onRemarkMicroBlogClick() {
						mOnAdapterOperationBtnClickListener
								.onRemarkMicroBlogClick();
					}

					@Override
					public void onForwardMicroBlogClick() {
						mOnAdapterOperationBtnClickListener
								.onForwardMicroBlogClick();
					}

					@Override
					public void onDeleteMicroBlogClick() {
						mOnAdapterOperationBtnClickListener
								.onDeleteMicroBlogClick();
					}

					@Override
					public void onCollectMicroBlogClick() {
						mOnAdapterOperationBtnClickListener
								.onCollectMicroBlogClick();
					}
				});

		cellMicroBlogView
				.setOnMoreOperationItemClickListener(new OnMoreOperationItemListener() {

					@Override
					public void onItemClick(int itemId) {
						mOnAdapterMoreOperationItemListener.onItemClick(itemId);
					}
				});
	}

	public interface OnAdapterOperationBtnClickListener {
		public void onDeleteMicroBlogClick();

		public void onForwardMicroBlogClick();

		public void onCollectMicroBlogClick();

		public void onRemarkMicroBlogClick();
	}

	public interface OnAdapterMoreOperationItemListener {
		public void onItemClick(int itemId);
	}

	public void setOnAdapterOperationBtnClickListener(
			OnAdapterOperationBtnClickListener listener) {
		mOnAdapterOperationBtnClickListener = listener;
	}

	public void setOnAdapterMoreOperationItemClickListener(
			OnAdapterMoreOperationItemListener listener) {
		mOnAdapterMoreOperationItemListener = listener;
	}

}
