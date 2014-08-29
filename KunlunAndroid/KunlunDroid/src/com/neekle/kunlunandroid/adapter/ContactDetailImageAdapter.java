package com.neekle.kunlunandroid.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ContactDetailImageAdapter extends BaseAdapter {

	private static final int IMG_WIDTH_PX = 160;
	private static final int IMG_HEIGHT_PX = 160;

	private Context mContext;
	private List<String> mList;

	public ContactDetailImageAdapter(Context mContext, List<String> mList) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.mList = mList;
		if (this.mList == null && this.mList.isEmpty()) {
			this.mList = new ArrayList<String>();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView iv = new ImageView(mContext);
		// 获取图片路径
		String filepath = (String) mList.get(position);
		Log.v("path--->", filepath);

		try {
			// 读取文件流对象
			// FileInputStream fin=new FileInputStream(new File(filepath));
			// Bitmap bm=BitmapFactory.decodeStream(fin);//通过字节流获取图片
			Bitmap bm = BitmapFactory.decodeFile(filepath);
			// 生成略缩图信息
			bm = ThumbnailUtils.extractThumbnail(bm, IMG_WIDTH_PX,
					IMG_HEIGHT_PX);
			// 设置图片到ImageView上
			iv.setImageBitmap(bm);
			// 设置边界对齐
			iv.setAdjustViewBounds(true);
			// 设置Gallery的布局参数(千万不能错)
			// iv.setLayoutParams(new Gallery.LayoutParams(160, 160));
			// fin.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return iv;

	}

}
