package com.neekle.kunlunandroid.presenter.interf;

import com.neekle.kunlunandroid.adapter.CellOneContactAdapter;
import com.neekle.kunlunandroid.adapter.ContactDetailImageAdapter;

import android.R.bool;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public interface IContactDetailActivity {

	public void test();

	public void showBasicInfo(Drawable photoDrawable, String name, String jid,
			boolean isStarSign);

	public void showExtraInfo(String label, String sex);

	public void showLocalAddsInfo(String info);

	public void showHint(String hint, int duration);

	public void showStarSign(boolean isShow);

	public void setIntentClass(Class<? extends Activity> intentClass,
			boolean isStartNewActivity, boolean isFinishSelf);

	public void updatePhotoAlbumAdapter(ContactDetailImageAdapter adapter);
}
