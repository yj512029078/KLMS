package com.neekle.kunlunandroid.view.controls.cellMicroBlog;

import android.R.anim;
import android.R.bool;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CellMicroBlogView extends RelativeLayout {

	public static class UserState {
		public static final int UNKNOWN = 0;
		public static final int XMPP_OFFLINE = 1;
		public static final int XMPP_IDLE = 2;
		public static final int XMPP_BUSY = 3;
		public static final int XMPP_LEAVE = 4;
		public static final int KUNLUN_OFFLINE = 5;
		public static final int KUNLUN_IDLE = 6;
		public static final int KUNLUN_BUSY = 7;
		public static final int KUNLUN_LEAVE = 8;
	}

	public static class MicroBolgState {
		public static final int SUCCESS = 0;
		public static final int FAILURE = -1;
	}

	public static class MoreOperationItemClickState {
		public static final int MAKE_PHONE_CALL_CLICK = 0;
		public static final int MAKE_VIDEO_CALL_CLICK = 1;
		public static final int SEND_MSG_CLICK = 2;
		public static final int SEND_EMAIL_CLICK = 3;
		public static final int ADD_AS_FRIEND_CLICK = 4;
	}

	private static final int WRONG_VALUE = -1;
	private static final int DEF_NOT_USED_VALUE = -1;
	private static final int POPUP_MENU_DEF_WIDTH_DIP = 150;
	private static final int POPUP_ANCHOR_MARGIN_X = 10;
	private static final int POPUP_ANCHOR_MARGIN_Y = 10;

	private int mUserState = UserState.KUNLUN_OFFLINE;
	private int mMicroBlogState = MicroBolgState.SUCCESS;
	private int mPopupMenuWidth = DEF_NOT_USED_VALUE;
	private int mPopupviewMeasuredHeight;

	private View mView;
	private ImageView mUserStatusImgv;
	private ImageView mUserPhotoImgv;
	private TextView mNameTxtv;
	private TextView mPublishTimeTxtv;
	private TextView mPublishCityTxtv;
	private TextView mWeiboSignatureTxtv;
	private ImageView mMoreOperationImgv;
	private TextView mWeiboStateHintTxtv;
	private TextView mWeiboStateDescriTxtv;
	private TextView mDeviceTxtv;
	private RelativeLayout mRemarkRelati;
	private RelativeLayout mCollectionRelati;
	private RelativeLayout mForwardRelati;
	private RelativeLayout mDeleteRelati;
	private PopupWindow mPopupWindow;
	private RelativeLayout mMakePhoneCallRelati;
	private RelativeLayout mMakeVideoCallRelati;
	private RelativeLayout mSendMsgRelati;
	private RelativeLayout mSendEmailRelati;
	private RelativeLayout mAddAsFriendRelati;
	private OnOperationBtnClickListener mOnOperationBtnClickListener;
	private OnMoreOperationItemListener mOnMoreOperationItemListener;

	private Context mContext;
	private TypedArray mTypedArray;

	public CellMicroBlogView(Context context) {
		super(context);

		this.mContext = context;
	}

	public CellMicroBlogView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.mContext = context;
	}

	public CellMicroBlogView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.mContext = context;
		mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.CellMicroBlogView);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		initViews(mTypedArray);
	}

	@Override
	protected void onCreateContextMenu(ContextMenu menu) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu);
	}

	private void initViews(TypedArray typedArray) {
		LayoutInflater layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		int layoutResId = R.layout.cellmicroblog;
		mView = (View) layoutInflater.inflate(layoutResId, this);
		findViewsAndSetAttributes(mView);

		initPopupMenu();
		findPopupMenuView();

		setXmlAttributes(typedArray);
	}

	private void initPopupMenu() {
		LayoutInflater layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = layoutInflater.inflate(
				R.layout.cellmicroblog_popup_menu, null);

		// 如果使用Inflater的情况下会出现以上错误，原因是用Inflater渲染组件的时候并没有给其指定父控件，所以渲染器不会去解析width
		// 和 height属性，就会导致空指针异常。
		// 具体原因，现在不是特别确定，如果没有这行代码，部分手机会崩溃
		contentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		// measure 以后，调用 getMeasuredX 会获得实际测量过的值。
		contentView.measure(
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		int width = contentView.getWidth();
		int height = contentView.getHeight();
		int measuredWidth = contentView.getMeasuredWidth();
		int measuredHeight = contentView.getMeasuredHeight();
		Log.i("popup", "width: " + width);
		Log.i("popup", "height: " + height);
		Log.i("popup", "measuredWidth: " + measuredWidth);
		Log.i("popup", "measuredHeight: " + measuredHeight);

		// 获得测量后的高度，此高度作为PopupWindow的高度
		mPopupviewMeasuredHeight = measuredHeight;

		// 目前PopupWindow 的 contentView没有采用listview, 综合显示效果，固定高度。可以考虑采用
		// LayoutParams.WRAP_CONTENT
		int pxValue;
		if (mPopupMenuWidth != DEF_NOT_USED_VALUE) {
			pxValue = mPopupMenuWidth;
		} else {
			pxValue = dip2px(mContext, POPUP_MENU_DEF_WIDTH_DIP);
		}

		// 如果使用固定高度，会造成闪烁的现象
		mPopupWindow = new PopupWindow(contentView, pxValue,
				LayoutParams.WRAP_CONTENT);

		// 点击空白处的时候PopupWindow会消失
		mPopupWindow.setFocusable(true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		BitmapDrawable bitmapDrawable = new BitmapDrawable();
		mPopupWindow.setBackgroundDrawable(bitmapDrawable);
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	private void findPopupMenuView() {
		View view = mPopupWindow.getContentView();

		int resId = R.id.relati_make_phone_call;
		mMakePhoneCallRelati = (RelativeLayout) view.findViewById(resId);
		mMakePhoneCallRelati.setOnClickListener(onMoreOperationClickListener);

		resId = R.id.relati_make_video_call;
		mMakeVideoCallRelati = (RelativeLayout) view.findViewById(resId);
		mMakeVideoCallRelati.setOnClickListener(onMoreOperationClickListener);

		resId = R.id.relati_send_instant_msg;
		mSendMsgRelati = (RelativeLayout) view.findViewById(resId);
		mSendMsgRelati.setOnClickListener(onMoreOperationClickListener);

		resId = R.id.relati_send_email;
		mSendEmailRelati = (RelativeLayout) view.findViewById(resId);
		mSendEmailRelati.setOnClickListener(onMoreOperationClickListener);

		resId = R.id.relati_add_as_friend;
		mAddAsFriendRelati = (RelativeLayout) view.findViewById(resId);
		mAddAsFriendRelati.setOnClickListener(onMoreOperationClickListener);
	}

	private OnClickListener onMoreOperationClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mMakePhoneCallRelati) {
				int itemId = MoreOperationItemClickState.MAKE_PHONE_CALL_CLICK;
				mPopupWindow.dismiss();
				onMoreOperationItemClickResponse(itemId);
			} else if (v == mMakeVideoCallRelati) {
				int itemId = MoreOperationItemClickState.MAKE_VIDEO_CALL_CLICK;
				mPopupWindow.dismiss();
				onMoreOperationItemClickResponse(itemId);
			} else if (v == mSendMsgRelati) {
				int itemId = MoreOperationItemClickState.SEND_MSG_CLICK;
				mPopupWindow.dismiss();
				onMoreOperationItemClickResponse(itemId);
			} else if (v == mSendEmailRelati) {
				int itemId = MoreOperationItemClickState.SEND_EMAIL_CLICK;
				mPopupWindow.dismiss();
				onMoreOperationItemClickResponse(itemId);
			} else if (v == mAddAsFriendRelati) {
				int itemId = MoreOperationItemClickState.ADD_AS_FRIEND_CLICK;
				mPopupWindow.dismiss();
				onMoreOperationItemClickResponse(itemId);
			}
		}
	};

	private void onMoreOperationItemClickResponse(int itemId) {
		if (mOnMoreOperationItemListener != null) {
			mOnMoreOperationItemListener.onItemClick(itemId);
		}
	}

	private void setXmlAttributes(TypedArray typedArray) {
		int userState = getInteger(typedArray,
				R.styleable.CellMicroBlogView_userState, mUserState);
		setUserState(userState);

		Drawable userPhoto = getDrawable(typedArray,
				R.styleable.CellMicroBlogView_userPhoto);
		if (userPhoto != null) {
			setUserPhoto(userPhoto);
		}

		// name
		String name = getString(typedArray, R.styleable.CellMicroBlogView_name);
		if (name != null) {
			setName(name);
		}

		ColorStateList colorStateList = getColor(typedArray,
				R.styleable.CellMicroBlogView_nameColor, DEF_NOT_USED_VALUE);
		int defTextColor = colorStateList.getDefaultColor();
		if (defTextColor != DEF_NOT_USED_VALUE) {
			setNameTextColor(defTextColor);
		}

		float textSize = getDimension(typedArray,
				R.styleable.CellMicroBlogView_nameTextSize);
		if (textSize != DEF_NOT_USED_VALUE) {
			setNameTextSize(textSize);
		}

		// publishTime
		String publishTime = getString(typedArray,
				R.styleable.CellMicroBlogView_publishTime);
		if (publishTime != null) {
			setPublishTime(publishTime);
		}

		colorStateList = getColor(typedArray,
				R.styleable.CellMicroBlogView_publishTimeTextColor,
				DEF_NOT_USED_VALUE);
		defTextColor = colorStateList.getDefaultColor();
		if (defTextColor != DEF_NOT_USED_VALUE) {
			setPublishTimeTextColor(colorStateList);
		}

		textSize = getDimension(typedArray,
				R.styleable.CellMicroBlogView_publishTimeTextSize);
		if (textSize != DEF_NOT_USED_VALUE) {
			setPublishTimeTextSize(textSize);
		}

		// publishCity
		String publishCity = getString(typedArray,
				R.styleable.CellMicroBlogView_publishCity);
		if (publishCity != null) {
			setPublishCity(publishCity);
		}

		colorStateList = getColor(typedArray,
				R.styleable.CellMicroBlogView_publishCityTextColor,
				DEF_NOT_USED_VALUE);
		defTextColor = colorStateList.getDefaultColor();
		if (defTextColor != DEF_NOT_USED_VALUE) {
			setPublishCityTextColor(colorStateList);
		}

		textSize = getDimension(typedArray,
				R.styleable.CellMicroBlogView_publishCityTextSize);
		if (textSize != DEF_NOT_USED_VALUE) {
			setPublishCityTextSize(textSize);
		}

		// microBlogSignature
		String microBlogSignature = getString(typedArray,
				R.styleable.CellMicroBlogView_microBlogSignature);
		if (microBlogSignature != null) {
			setMicroBlogContent(microBlogSignature);
		}

		int textLine = getInteger(typedArray,
				R.styleable.CellMicroBlogView_microBlogSignatureTextLine,
				DEF_NOT_USED_VALUE);
		if (textLine != DEF_NOT_USED_VALUE) {
			setMicroBlogContentLine(textLine);
		}

		colorStateList = getColor(typedArray,
				R.styleable.CellMicroBlogView_microBlogSignatureTextColor,
				DEF_NOT_USED_VALUE);
		defTextColor = colorStateList.getDefaultColor();
		if (defTextColor != DEF_NOT_USED_VALUE) {
			setMicroBlogContentTextColor(colorStateList);
		}

		textSize = getDimension(typedArray,
				R.styleable.CellMicroBlogView_microBlogSignatureTextSize);
		if (textSize != DEF_NOT_USED_VALUE) {
			setMicroBlogContentTextSize(textSize);
		}

		Drawable bg = getDrawable(typedArray,
				R.styleable.CellMicroBlogView_microBlogSignatureBg);
		if (bg != null) {
			setMicroBlogContentBg(bg);
		}

		// microBlogStateDescri
		String microBlogStateDescri = getString(typedArray,
				R.styleable.CellMicroBlogView_microBlogStateDescri);
		if (microBlogStateDescri != null) {
			setMicroBlogStateDescri(microBlogStateDescri);
		}

		textLine = getInteger(typedArray,
				R.styleable.CellMicroBlogView_microBlogStateDescriTextLine,
				DEF_NOT_USED_VALUE);
		if (textLine != DEF_NOT_USED_VALUE) {
			setMicroBlogStateDescriLines(textLine);
		}

		colorStateList = getColor(typedArray,
				R.styleable.CellMicroBlogView_microBlogStateDescriTextColor,
				DEF_NOT_USED_VALUE);
		defTextColor = colorStateList.getDefaultColor();
		if (defTextColor != DEF_NOT_USED_VALUE) {
			setMicroBlogStateDescriTextColor(colorStateList);
		}

		textSize = getDimension(typedArray,
				R.styleable.CellMicroBlogView_microBlogStateDescriTextSize);
		if (textSize != DEF_NOT_USED_VALUE) {
			setMicroBlogStateDescriTextSize(textSize);
		}

		bg = getDrawable(typedArray,
				R.styleable.CellMicroBlogView_microBlogStateDescriBg);
		if (bg != null) {
			setMicroBlogStateDescriBg(bg);
		}

		// device
		String device = getString(typedArray,
				R.styleable.CellMicroBlogView_device);
		if (device != null) {
			setDevice(device);
		}

		colorStateList = getColor(typedArray,
				R.styleable.CellMicroBlogView_deviceTextColor,
				DEF_NOT_USED_VALUE);
		defTextColor = colorStateList.getDefaultColor();
		if (defTextColor != DEF_NOT_USED_VALUE) {
			setDeviceTextColor(colorStateList);
		}

		textSize = getDimension(typedArray,
				R.styleable.CellMicroBlogView_deviceTextSize);
		if (textSize != DEF_NOT_USED_VALUE) {
			setDeviceTextSize(textSize);
		}

		// popupMenuWidth
		float popupMenuWidth = getDimension(typedArray,
				R.styleable.CellMicroBlogView_popupMenuWidth);
		if (popupMenuWidth != DEF_NOT_USED_VALUE) {
			mPopupMenuWidth = (int) popupMenuWidth;
		}
	}

	private float getDimension(TypedArray typedArray, int index) {
		float dimension = typedArray.getDimension(index, DEF_NOT_USED_VALUE);
		return dimension;
	}

	private String getString(TypedArray typedArray, int index) {
		String string = typedArray.getString(index);
		return string;
	}

	private int getInteger(TypedArray typedArray, int index, int defValue) {
		int value = typedArray.getInteger(index, defValue);
		return value;
	}

	private Drawable getDrawable(TypedArray typedArray, int index) {
		Drawable drawable = typedArray.getDrawable(index);
		return drawable;
	}

	private ColorStateList getColor(TypedArray typedArray, int index,
			int defValue) {
		int color = typedArray.getColor(index, defValue);
		ColorStateList colorStateList = ColorStateList.valueOf(color);
		return colorStateList;
	}

	private void findViewsAndSetAttributes(View view) {
		int resId = R.id.imgv_user_status;
		mUserStatusImgv = (ImageView) view.findViewById(resId);

		resId = R.id.imgv_user_photo;
		mUserPhotoImgv = (ImageView) view.findViewById(resId);
		mUserPhotoImgv.setOnLongClickListener(mOnLongClickListener);

		resId = R.id.tv_name;
		mNameTxtv = (TextView) view.findViewById(resId);

		resId = R.id.tv_publish_time;
		mPublishTimeTxtv = (TextView) view.findViewById(resId);

		resId = R.id.tv_publish_city;
		mPublishCityTxtv = (TextView) view.findViewById(resId);

		resId = R.id.tv_weibo_info;
		mWeiboSignatureTxtv = (TextView) view.findViewById(resId);

		resId = R.id.imgv_more_operations;
		mMoreOperationImgv = (ImageView) view.findViewById(resId);
		mMoreOperationImgv.setOnClickListener(mOnClickListener);

		resId = R.id.tv_state_hint;
		mWeiboStateHintTxtv = (TextView) view.findViewById(resId);

		resId = R.id.tv_state_descri;
		mWeiboStateDescriTxtv = (TextView) view.findViewById(resId);

		resId = R.id.tv_device;
		mDeviceTxtv = (TextView) view.findViewById(resId);

		resId = R.id.relati_remark;
		mRemarkRelati = (RelativeLayout) view.findViewById(resId);
		mRemarkRelati.setOnClickListener(mOnClickListener);

		resId = R.id.relati_collection;
		mCollectionRelati = (RelativeLayout) view.findViewById(resId);
		mCollectionRelati.setOnClickListener(mOnClickListener);

		resId = R.id.relati_forward;
		mForwardRelati = (RelativeLayout) view.findViewById(resId);
		mForwardRelati.setOnClickListener(mOnClickListener);

		resId = R.id.relati_delete;
		mDeleteRelati = (RelativeLayout) view.findViewById(resId);
		mDeleteRelati.setOnClickListener(mOnClickListener);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return super.generateLayoutParams(attrs);
	}

	@Override
	protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
		return super.checkLayoutParams(p);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	public void setUserState(int state) {
		mUserState = state;

		int imageId = getStateImageId(state);
		mUserStatusImgv.setBackgroundResource(imageId);
	}

	private int getStateImageId(int state) {
		int imageId = R.drawable.cellmicroblog_state_koffline;

		switch (state) {
		case UserState.UNKNOWN: {
			imageId = R.drawable.cellmicroblog_state_unknown;
			break;
		}
		case UserState.XMPP_OFFLINE: {
			imageId = R.drawable.cellmicroblog_state_xoffline;
			break;
		}
		case UserState.XMPP_IDLE: {
			imageId = R.drawable.cellmicroblog_state_xidle;
			break;
		}
		case UserState.XMPP_BUSY: {
			imageId = R.drawable.cellmicroblog_state_xbusy;
			break;
		}
		case UserState.XMPP_LEAVE: {
			imageId = R.drawable.cellmicroblog_state_xleave;
			break;
		}
		case UserState.KUNLUN_IDLE: {
			imageId = R.drawable.cellmicroblog_state_kidle;
			break;
		}
		case UserState.KUNLUN_BUSY: {
			imageId = R.drawable.cellmicroblog_state_kbusy;
			break;
		}
		case UserState.KUNLUN_LEAVE: {
			imageId = R.drawable.cellmicroblog_state_kleave;
			break;
		}
		case UserState.KUNLUN_OFFLINE: {
			imageId = R.drawable.cellmicroblog_state_koffline;
			break;
		}
		default: {
			imageId = R.drawable.cellmicroblog_state_koffline;
			break;
		}

		}

		return imageId;
	}

	public int getUserState() {
		return mUserState;
	}

	public void setUserPhoto(Bitmap bitmap) {
		mUserPhotoImgv.setImageBitmap(bitmap);
	}

	public void setUserPhoto(Drawable drawable) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		Bitmap bitmap = bitmapDrawable.getBitmap();

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Log.i("photo", "setUserPhoto width: " + width);
		Log.i("photo", "setUserPhoto height: " + height);

		mUserPhotoImgv.setImageDrawable(drawable);
		// mUserPhotoImgv
		// .setBackgroundResource(R.drawable.cellmicroblog_def_photo);
		// mUserPhotoImgv.setImageBitmap(bitmap);
		// mUserPhotoImgv.setBackgroundDrawable(background)
	}

	public Drawable getUserPhoto() {
		Drawable drawable = mUserPhotoImgv.getBackground();
		return drawable;
	}

	public void setName(String name) {
		mNameTxtv.setText(name);
	}

	public String getName() {
		String name = mNameTxtv.getText().toString();
		return name;
	}

	public void setNameTextColor(int color) {
		mNameTxtv.setTextColor(color);
	}

	public void setNameTextColor(ColorStateList colorStateList) {
		mNameTxtv.setTextColor(colorStateList);
	}

	public ColorStateList getNameTextColor() {
		ColorStateList colorStateList = mNameTxtv.getTextColors();
		return colorStateList;
	}

	public void setNameTextSize(float size) {
		mNameTxtv.setTextSize(size);
	}

	public float getNameTextSize() {
		float size = mNameTxtv.getTextSize();
		return size;
	}

	public void setPublishTime(String time) {
		mPublishTimeTxtv.setText(time);
	}

	public String getPublishTime() {
		String time = mPublishTimeTxtv.getText().toString();
		return time;
	}

	public void setPublishTimeTextColor(int color) {
		mPublishTimeTxtv.setTextColor(color);
	}

	public void setPublishTimeTextColor(ColorStateList colorStateList) {
		mPublishTimeTxtv.setTextColor(colorStateList);
	}

	public ColorStateList getPublishTimeTextColor() {
		ColorStateList colorStateList = mPublishTimeTxtv.getTextColors();
		return colorStateList;
	}

	public void setPublishTimeTextSize(float size) {
		mPublishTimeTxtv.setTextSize(size);
	}

	public float getPublishTimeTextSize() {
		float size = mPublishTimeTxtv.getTextSize();
		return size;
	}

	public void setPublishCity(String city) {
		mPublishCityTxtv.setText(city);
	}

	public String getPublishCity() {
		String city = mPublishCityTxtv.getText().toString();
		return city;
	}

	public void setPublishCityTextColor(int color) {
		mPublishCityTxtv.setTextColor(color);
	}

	public void setPublishCityTextColor(ColorStateList colorStateList) {
		mPublishCityTxtv.setTextColor(colorStateList);
	}

	public ColorStateList getPublishCityTextColor() {
		ColorStateList colorStateList = mPublishCityTxtv.getTextColors();
		return colorStateList;
	}

	public void setPublishCityTextSize(float size) {
		mPublishCityTxtv.setTextSize(size);
	}

	public float getPublishCityTextSize() {
		float size = mPublishCityTxtv.getTextSize();
		return size;
	}

	public void setMicroBlogContent(String string) {
		mWeiboSignatureTxtv.setText(string);
	}

	public String getMicroBlogContent() {
		String string = mWeiboSignatureTxtv.getText().toString();
		return string;
	}

	public void setMicroBlogContentLine(int lines) {
		mWeiboSignatureTxtv.setLines(lines);
	}

	public void setMicroBlogContentTextColor(int color) {
		mWeiboSignatureTxtv.setTextColor(color);
	}

	public void setMicroBlogContentTextColor(ColorStateList colorStateList) {
		mWeiboSignatureTxtv.setTextColor(colorStateList);
	}

	public void setMicroBlogContentBg(Bitmap bitmap) {
		BitmapDrawable drawable = new BitmapDrawable(bitmap);
		setMicroBlogContentBg(drawable);
	}

	public void setMicroBlogContentBg(Drawable drawable) {
		mWeiboSignatureTxtv.setBackgroundDrawable(drawable);
	}

	public Drawable getMicroBlogContentBg() {
		Drawable drawable = mWeiboSignatureTxtv.getBackground();
		return drawable;
	}

	public ColorStateList getMicroBlogContentTextColor() {
		ColorStateList colorStateList = mWeiboSignatureTxtv.getTextColors();
		return colorStateList;
	}

	public void setMicroBlogContentTextSize(float size) {
		mWeiboSignatureTxtv.setTextSize(size);
	}

	public float getMicroBlogContentTextSize() {
		float size = mWeiboSignatureTxtv.getTextSize();
		return size;
	}

	public void setMicroBlogState(int state) {
		mMicroBlogState = state;
		String text = null;

		switch (state) {
		case MicroBolgState.FAILURE: {
			int resId = R.string.cellmicroblog_exclamation_mark;
			text = mContext.getString(resId);
			break;
		}
		default: {
			text = "";
			break;
		}

		}

		mWeiboStateHintTxtv.setText(text);
	}

	public int getMicroBlogState() {
		return mMicroBlogState;
	}

	public void setMicroBlogStateDescri(String string) {
		mWeiboStateDescriTxtv.setText(string);
	}

	public String getMicroBlogStateDescri() {
		String string = mWeiboStateDescriTxtv.getText().toString();
		return string;
	}

	public void setMicroBlogStateDescriLines(int lines) {
		mWeiboStateDescriTxtv.setLines(lines);
	}

	public void setMicroBlogStateDescriTextColor(int color) {
		mWeiboStateDescriTxtv.setTextColor(color);
	}

	public void setMicroBlogStateDescriBg(Bitmap bitmap) {
		BitmapDrawable drawable = new BitmapDrawable(bitmap);
		setMicroBlogStateDescriBg(drawable);
	}

	public void setMicroBlogStateDescriBg(Drawable drawable) {
		mWeiboStateDescriTxtv.setBackgroundDrawable(drawable);
	}

	public Drawable getMicroBlogStateDescriBg() {
		Drawable drawable = mWeiboStateDescriTxtv.getBackground();
		return drawable;
	}

	public void setMicroBlogStateDescriTextColor(ColorStateList colorStateList) {
		mWeiboStateDescriTxtv.setTextColor(colorStateList);
	}

	public ColorStateList getMicroBlogStateDescriTextColor() {
		ColorStateList colorStateList = mWeiboStateDescriTxtv.getTextColors();
		return colorStateList;
	}

	public void setMicroBlogStateDescriTextSize(float size) {
		mWeiboStateDescriTxtv.setTextSize(size);
	}

	public float getMicroBlogStateDescriTextSize() {
		float size = mWeiboStateDescriTxtv.getTextSize();
		return size;
	}

	public void setDevice(String string) {
		mDeviceTxtv.setText(string);
	}

	public String getDevice() {
		String string = mDeviceTxtv.getText().toString();
		return string;
	}

	public void setDeviceTextColor(int color) {
		mDeviceTxtv.setTextColor(color);
	}

	public void setDeviceTextColor(ColorStateList colorStateList) {
		mDeviceTxtv.setTextColor(colorStateList);
	}

	public ColorStateList getDeviceTextColor() {
		ColorStateList colorStateList = mDeviceTxtv.getTextColors();
		return colorStateList;
	}

	public void setDeviceTextSize(float size) {
		mDeviceTxtv.setTextSize(size);
	}

	public float getDeviceTextSize() {
		float size = mDeviceTxtv.getTextSize();
		return size;
	}

	public void setPopupMenuWidth(int widthPixel) {
		mPopupMenuWidth = widthPixel;
	}

	public int getPopupMenuWidth() {
		int width;

		if (mPopupMenuWidth != DEF_NOT_USED_VALUE) {
			width = mPopupMenuWidth;
		} else {
			width = dip2px(mContext, POPUP_MENU_DEF_WIDTH_DIP);
		}

		return width;
	}

	private OnLongClickListener mOnLongClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			showPopupMenu(v);
			return false;
		}
	};

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if ((v == mRemarkRelati) || (v == mCollectionRelati)
					|| (v == mForwardRelati) || (v == mDeleteRelati)) {
				doOperationBtnClick(v);
			} else if (v == mMoreOperationImgv) {
				showPopupMenu(v);
			}
		}
	};

	private void showPopupMenu(View v) {
		// 将PopupWindow以一种向下弹出的动画的形式显示出来
		if (mPopupWindow != null) {
			boolean isShowing = mPopupWindow.isShowing();
			if (!isShowing) {
				mPopupWindow.setClippingEnabled(false);
				showAtLocation(v, mPopupWindow, mPopupviewMeasuredHeight,
						POPUP_ANCHOR_MARGIN_X, POPUP_ANCHOR_MARGIN_Y);
			}
		}
	}

	private void showAtLocation(View anchorView, PopupWindow popupWindow,
			int popupHeight, int marginWidth, int marginHeight) {
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		int screenHeight = metrics.heightPixels;
		int anchorViewHeight = anchorView.getHeight();

		int[] location = new int[2];
		anchorView.getLocationOnScreen(location);
		int topLeftX = location[0];
		int topLeftY = location[1];
		int bottomLeftX = topLeftX;
		int bottomLeftY = topLeftY + anchorViewHeight;

		boolean isBelowOk = true;
		boolean isAboveOk = true;

		if ((bottomLeftY + popupHeight + marginHeight) > screenHeight) {
			isBelowOk = false;
		}

		if ((topLeftY - popupHeight - marginHeight) < 0) {
			isAboveOk = false;
		}

		if (isBelowOk) {
			popupWindow.showAsDropDown(anchorView, marginWidth, marginHeight);
		} else if (isAboveOk) {
			int yoffAbsoluteValue = marginHeight * 2 + anchorViewHeight
					+ popupHeight;
			popupWindow.showAsDropDown(anchorView, marginWidth,
					-yoffAbsoluteValue);
		} else {
			popupWindow.showAtLocation(this, Gravity.CENTER, 0, 0);
		}
	}

	private void doOperationBtnClick(View v) {
		if (mOnOperationBtnClickListener != null) {
			if (v == mRemarkRelati) {
				mOnOperationBtnClickListener.onRemarkMicroBlogClick();
			} else if (v == mCollectionRelati) {
				mOnOperationBtnClickListener.onCollectMicroBlogClick();
			} else if (v == mForwardRelati) {
				mOnOperationBtnClickListener.onForwardMicroBlogClick();
			} else if (v == mDeleteRelati) {
				mOnOperationBtnClickListener.onDeleteMicroBlogClick();
			}
		}
	}

	public void setOnOperationBtnClickListener(
			OnOperationBtnClickListener listener) {
		mOnOperationBtnClickListener = listener;
	}

	public interface OnOperationBtnClickListener {
		public void onDeleteMicroBlogClick();

		public void onForwardMicroBlogClick();

		public void onCollectMicroBlogClick();

		public void onRemarkMicroBlogClick();
	}

	public void setOnMoreOperationItemClickListener(
			OnMoreOperationItemListener listener) {
		mOnMoreOperationItemListener = listener;
	}

	public interface OnMoreOperationItemListener {
		public void onItemClick(int itemId);
	}

}
