package com.neekle.kunlunandroid.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.data.ContactDetailMoreData;

public class ContactDetailMoreAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mLayoutInflater;
	private List<ContactDetailMoreData> mDataList;

	public ContactDetailMoreAdapter(Context context) {
		this.context = context;
		String inflater = Context.LAYOUT_INFLATER_SERVICE;
		mLayoutInflater = (LayoutInflater) context.getSystemService(inflater);
	}

	public ContactDetailMoreAdapter(Context context,
			List<ContactDetailMoreData> datalist) {
		this.context = context;
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
		// TODO Auto-generated method stub
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContactDetailMoreData data = mDataList.get(position);
		String tittle = data.getTittle();
		String content = data.getContent();

		View view = convertView;
		if (convertView == null) {
			view = mLayoutInflater.inflate(
					R.layout.contact_detail_more_lv_item, null);
		}

		TextView tittleTxtv = (TextView) view.findViewById(R.id.tv_tittle);
		TextView contentTxtv = (TextView) view.findViewById(R.id.tv_content);

		tittleTxtv.setText(tittle);
		contentTxtv.setText(content);

		return view;
	}
}
