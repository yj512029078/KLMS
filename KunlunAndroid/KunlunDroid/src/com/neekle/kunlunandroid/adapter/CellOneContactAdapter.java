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
import android.widget.TextView;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.data.CellOneContactData;
import com.neekle.kunlunandroid.util.StringOperator;
import com.neekle.kunlunandroid.view.controls.cellOneContact.CellOneContactView;
import com.neekle.kunlunandroid.view.specials.AlphabetListView;

public class CellOneContactAdapter extends BaseAdapter {

	private List<CellOneContactData> mDataList;
	private List<CellOneContactData> mBackupList;

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private ArrayFilter mFilter;
	private OnCellOneContactViewAdapterListener mCellOneContactViewAdapterListener;
	private OnCellOneContactBtnAdapterListener mCellOneContactBtnAdapterListener;
	private OnCellOneContactMenuAdapterListener mCellOneContactMenuAdapterListener;

	public interface OnCellOneContactBtnAdapterListener {

		public void onBtnPhoneClickInterface();

		public void onBtnMsgChatClickInterface();

		public void onBtnExpandInterface();  
	}

	public interface OnCellOneContactViewAdapterListener {
		public void onClick(CellOneContactData cellOneContactData);
	}

	public interface OnCellOneContactMenuAdapterListener {
		
		public void onAdapterContactMenuItemsInterface(int style, int item,
				CellOneContactData cellOneContactData);
	}

	public CellOneContactAdapter(Context context) {
		this.mContext = context;
		String inflater = Context.LAYOUT_INFLATER_SERVICE;
		mLayoutInflater = (LayoutInflater) context.getSystemService(inflater);
	}

	public CellOneContactAdapter(Context context,
			List<CellOneContactData> datalist) {
		this.mContext = context;
		mDataList = datalist;
		mBackupList = datalist;
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			int resource = R.layout.addsbook_lv_item;
			convertView = mLayoutInflater.inflate(resource, null);
			View itemContentView = convertView
					.findViewById(R.id.cell_one_contact_view);
			viewHolder.cellOneContactView = (CellOneContactView) itemContentView;
			View itemCatlogView = convertView.findViewById(R.id.catalog);
			viewHolder.catlogTxtv = (TextView) itemCatlogView;

			convertView.setTag(viewHolder);
		} else {
			// getTag 方式省去了findViewById的过程
			viewHolder = (ViewHolder) convertView.getTag();
		}

		CellOneContactView itemModel = viewHolder.cellOneContactView;
		TextView itemCatlog = viewHolder.catlogTxtv;
		CellOneContactData cellOneContactData = mDataList.get(position);
		setItemContent(itemModel, cellOneContactData, position);
		setItemCatlog(itemCatlog, cellOneContactData, position);

		// 注意这里的返回，如果布局是inflate进来的，需要返回整个view，而不能只返回其sub view
		return convertView;
	}

	private void setItemContent(CellOneContactView itemModel,
			CellOneContactData cellOneContactData, int position) {
		int style = cellOneContactData.getmControlStyle();
		itemModel.setContactControlStyle(style);

		boolean isExpandShow = cellOneContactData.isExpandShow();
		itemModel.setExpandVisibility(isExpandShow);

		boolean isCheckStateShow = cellOneContactData.isCheckStateShow();
		itemModel.setCheckboxVisibility(isCheckStateShow);

		boolean isUserStateShow = cellOneContactData.isUserStateShow();
		int resId = cellOneContactData.getUserState();
		itemModel.setStateVisibility(isUserStateShow);
		if (isUserStateShow) {
			itemModel.setState(resId);
		}

		boolean isContactPhotoShow = cellOneContactData.isContactPhotoShow();
		Bitmap contactPhoto = cellOneContactData.getContactPhoto();
		itemModel.setPhotoVisibility(isContactPhotoShow);
		if (isContactPhotoShow) {
			// use bitmap temporary
			itemModel.setPhoto(contactPhoto);
		}

		boolean isContactGroupShow = cellOneContactData.isContactGroupShow();
		itemModel.setLabelVisibility(isContactGroupShow);

		String contactName = cellOneContactData.getContactName();
		itemModel.setName(contactName);

		boolean isPhoneCallShow = cellOneContactData.isPhoneCallShow();
		itemModel.setPhoneVisibility(isPhoneCallShow);

		// tmp add it
		itemModel.setMsgChatVisibility(View.GONE);

		onEvent(itemModel, cellOneContactData, position);
	}

	private void onEvent(final CellOneContactView view,
			final CellOneContactData cellOneContactData, final int position) {
		CellOneContactView.OnCheckedChangedListener onCheckedChangedListener = new CellOneContactView.OnCheckedChangedListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				cellOneContactData.setChecked(isChecked);
			}
		};
		view.setOnCheckedChangedListener(onCheckedChangedListener);

		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCellOneContactViewAdapterListener != null) {
					mCellOneContactViewAdapterListener
							.onClick(cellOneContactData);
				}
			}
		});

		CellOneContactView.OnCelloneContactViewBtnListener celloneContactViewBtnListener = new CellOneContactView.OnCelloneContactViewBtnListener() {

			@Override
			public void onBtnPhoneClickInterface() {
				if (mCellOneContactBtnAdapterListener != null) {
					mCellOneContactBtnAdapterListener
							.onBtnPhoneClickInterface();
				}
			}

			@Override
			public void onBtnMsgChatClickInterface() {
				if (mCellOneContactBtnAdapterListener != null) {
					mCellOneContactBtnAdapterListener
							.onBtnMsgChatClickInterface();
				}
			}

			@Override
			public void onBtnExpandInterface() {
				if (mCellOneContactBtnAdapterListener != null) {
					mCellOneContactBtnAdapterListener.onBtnExpandInterface();
				}
			}
		};
		view.setListener(celloneContactViewBtnListener);

		CellOneContactView.onContactMenuItemListener celloneContactViewMenuListener = new CellOneContactView.onContactMenuItemListener() {

			@Override
			public void onAllContactMenuItemsInterface(int style, int item) {
				if (mCellOneContactMenuAdapterListener != null) {
					mCellOneContactMenuAdapterListener
							.onAdapterContactMenuItemsInterface(style, item,
									cellOneContactData);
				}
			}
		};
		view.setListener(celloneContactViewMenuListener);
	}

	private void setItemCatlog(TextView itemCatlog, CellOneContactData data,
			int position) {
		String letter = getFirstLetter(data);
		int catlog = data.getFriendPinyinCatlog();
		int firstLetterOccurPosition = mAlphabetPositionListener.getPosition(
				letter, catlog);

		// 注意：这个逻辑是没有问题的，容易想不通
		if (position == firstLetterOccurPosition) {
			setProperCatlog(itemCatlog, catlog, letter);
			itemCatlog.setVisibility(View.VISIBLE);
		} else {
			itemCatlog.setVisibility(View.GONE);
		}
	}

	private void setProperCatlog(TextView itemCatlog, int catlog, String letter) {
		if (catlog == Constants.FriendPinyinCatlog.FRIEND_STAR) {
			String string = getStarCatlogText(AlphabetListView.STAR);
			itemCatlog.setText(string);
		} else if (catlog == Constants.FriendPinyinCatlog.FRIEND_EXTRA) {
			String string = AlphabetListView.EXTRA;
			itemCatlog.setText(string);
		} else {
			itemCatlog.setText(letter);
		}
	}

	private String getStarCatlogText(String letter) {
		int resId = R.string.addsbook_often_contact_person;
		String string = mContext.getString(resId);
		string = letter + string;

		return string;
	}

	public void setData(List<CellOneContactData> dataList) {
		this.mDataList = dataList;
		this.mBackupList = dataList;
	}

	public void setListener(OnCellOneContactBtnAdapterListener listener) {
		this.mCellOneContactBtnAdapterListener = listener;
	}

	public void setListener(OnCellOneContactViewAdapterListener listener) {
		this.mCellOneContactViewAdapterListener = listener;
	}

	public void setListener(OnCellOneContactMenuAdapterListener listener) {
		this.mCellOneContactMenuAdapterListener = listener;
	}

	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}

		return mFilter;
	}

	private class ArrayFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			mDataList = mBackupList;
			FilterResults results = new FilterResults();

			if (prefix == null || prefix.length() == 0) {
				results.values = mDataList;
				results.count = mDataList.size();
			} else {
				doFilter(results, prefix.toString());
			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			mDataList = (List<CellOneContactData>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

		private void doFilter(FilterResults results, String prefix) {
			final int count = mDataList.size();
			List<CellOneContactData> newList = new ArrayList<CellOneContactData>();

			for (int i = 0; i < count; i++) {
				CellOneContactData cellOneContactData = mDataList.get(i);

				boolean isHaveChina = StringOperator.isHaveChina(prefix
						.toString());
				if (isHaveChina) {
					String name = cellOneContactData.getContactName();
					if (name.contains(prefix)) {
						newList.add(cellOneContactData);
					}
				} else {
					// 实际目前我们不支持多音字
					ArrayList<String> pinyinList = cellOneContactData
							.getNamePinyinList();

					if ((pinyinList != null) && (pinyinList.size() != 0)) {
						for (String pinyin : pinyinList) {
							if (pinyin.contains(prefix)) {
								newList.add(cellOneContactData);
								break;
							}
						}
					}

				}

			}

			results.values = newList;
			results.count = newList.size();
		}

	}

	public List<CellOneContactData> getFilterdData() {
		return mDataList;
	}

	public AlphabetListView.AlphabetPositionListener getAlphabetPositionListener() {
		return mAlphabetPositionListener;
	}

	private AlphabetListView.AlphabetPositionListener mAlphabetPositionListener = new AlphabetListView.AlphabetPositionListener() {
		@Override
		public int getPosition(String letter) {
			if (letter != null) {
				int catlog;
				if (letter.equals(AlphabetListView.STAR)) {
					catlog = Constants.FriendPinyinCatlog.FRIEND_STAR;
				} else if (letter.equals(AlphabetListView.EXTRA)) {
					catlog = Constants.FriendPinyinCatlog.FRIEND_EXTRA;
				} else {
					catlog = Constants.FriendPinyinCatlog.FRIEND_NORMAL;
				}

				return getPosition(letter, catlog);
			}

			return UNKNOW;
		}

		@Override
		public int getPosition(String letter, int catlog) {
			if (letter != null) {
				int result = getFirstOccurPosition(mDataList, letter, catlog);
				return result;
			}

			return UNKNOW;
		}

		private int getFirstOccurPosition(List<CellOneContactData> list,
				String letter, int catlog) {
			int count = list.size();

			for (int i = 0; i < count; i++) {
				CellOneContactData data = list.get(i);

				if (catlog == Constants.FriendPinyinCatlog.FRIEND_STAR) {
					boolean flag = isStar(data);
					if (flag) {
						return i;
					}
				} else if (catlog == Constants.FriendPinyinCatlog.FRIEND_EXTRA) {
					boolean flag = isExtra(data);
					if (flag) {
						return i;
					}
				} else {
					// bug to fix
					String string = getFirstLetter(data);
					if ((string != null) && (string.equalsIgnoreCase(letter))) {
						return i;
					}
				}

			}

			return UNKNOW;
		}

		private boolean isStar(CellOneContactData cellOneContactData) {
			boolean flag = false;
			int catlog = cellOneContactData.getFriendPinyinCatlog();

			if (catlog == Constants.FriendPinyinCatlog.FRIEND_STAR) {
				flag = true;
			}

			return flag;
		}

		private boolean isExtra(CellOneContactData cellOneContactData) {
			boolean flag = false;
			int catlog = cellOneContactData.getFriendPinyinCatlog();

			if (catlog == Constants.FriendPinyinCatlog.FRIEND_EXTRA) {
				flag = true;
			}

			return flag;
		}

	};

	public String getFirstLetter(CellOneContactData cellOneContactData) {
		String string = "";
		int catlog = cellOneContactData.getFriendPinyinCatlog();
		if (catlog != Constants.FriendPinyinCatlog.FRIEND_NORMAL) {
			return string;
		}

		ArrayList<String> pinyinList = cellOneContactData.getNamePinyinList();
		String pinyin = pinyinList.get(0);

		int length = pinyin.length();
		if (length != 0) {
			Character firstLetter = pinyin.charAt(0);
			string = firstLetter.toString();
		}

		return string;
	}

	final static class ViewHolder {
		TextView catlogTxtv;
		CellOneContactView cellOneContactView;
	}

}
