package com.neekle.kunlunandroid.view.specials;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 右边带有字母查询的ListView
 * 
 * @author yj
 */
public class AlphabetListView extends FrameLayout {

	public static final String STAR = "*";
	public static final String EXTRA = "#";

	public static String[] ALPHABETS = { STAR, EXTRA, "A", "B", "C", "D", "E",
			"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X", "Y", "Z" };

	private static final int SCROLL_STATE_DEF = -1;
	private static final int INDICATOR_DURATION = 1000;
	private static final int SCROLL_DURATION = 50;

	private ListView mListView;
	private TextView mTextView;
	private AlphabetSideBar mSideBar;

	private Context mContext;
	private float mScreenDensity;

	public static String[] mAlphabets = null;

	private AlphabetPositionListener mPositionListener;

	public AlphabetListView(Context context) {
		super(context);
		init(context);
	}

	public AlphabetListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		mScreenDensity = context.getResources().getDisplayMetrics().density;

		mListView = new ListView(mContext);

		setSideBar();
		setHintTxtv();

		this.addView(mListView);
		this.addView(mSideBar);
		this.addView(mTextView);

		mSideBar.setTextView(mTextView);
		mSideBar.setOnTouchingLetterChangedListener(onTouchingLetterChangedListener);
	}

	private void setSideBar() {
		mSideBar = new AlphabetSideBar(mContext);
		mSideBar.setAlphabets(ALPHABETS);
		int width = dip2px(30);
		int height = FrameLayout.LayoutParams.FILL_PARENT;
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				width, height);
		// 这个值的细微改变，会对布局有明显改变，这个后面再分析下，但是目前没有问题
		layoutParams.bottomMargin = dip2px(1);
		layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER;
		mSideBar.setLayoutParams(layoutParams);
	}

	private void setHintTxtv() {
		int textSize = dip2px(20);
		int textColor = Color.argb(150, 255, 255, 255);
		int bgColor = Color.argb(200, 0, 0, 0);
		int minWidth = dip2px(50);
		int minHeight = dip2px(50);
		int padding = dip2px(10);

		mTextView = new TextView(mContext);

		mTextView.setTextSize(textSize);
		mTextView.setTextColor(textColor);
		mTextView.setBackgroundColor(bgColor);
		mTextView.setMinWidth(minWidth);
		mTextView.setMinHeight(minHeight);
		mTextView.setPadding(padding, padding, padding, padding);
		mTextView.setGravity(Gravity.CENTER);
		mTextView.setVisibility(View.INVISIBLE);

		FrameLayout.LayoutParams textLayoutParams = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		textLayoutParams.gravity = Gravity.CENTER;
		mTextView.setLayoutParams(textLayoutParams);
	}

	public void setAdapter(ListAdapter adapter) {
		mListView.setAdapter(adapter);
		mListView.setSelection(0);
	}

	public int dip2px(float dpValue) {
		final float scale = mScreenDensity;
		int value = (int) (dpValue * scale + 0.5f);

		return value;
	}

	private AlphabetSideBar.OnTouchingLetterChangedListener onTouchingLetterChangedListener = new AlphabetSideBar.OnTouchingLetterChangedListener() {

		@Override
		public void onTouchingLetterChanged(String s) {
			int position = mPositionListener.getPosition(s);

			if (position != AlphabetPositionListener.UNKNOW) {
				mListView.setSelection(position);
			}
		}
	};

	public static interface AlphabetPositionListener {
		public static final int UNKNOW = -1;

		public int getPosition(String letter);

		public int getPosition(String letter, int catlog);
	}

	public void setAlphabetPositionListener(AlphabetPositionListener listener) {
		if (listener == null) {
			String expMsg = "AlphabetmPositionListener is required";
			throw new IllegalArgumentException(expMsg);
		}

		this.mPositionListener = listener;
	}

}