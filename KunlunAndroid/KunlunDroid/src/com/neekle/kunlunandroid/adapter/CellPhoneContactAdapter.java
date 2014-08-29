package com.neekle.kunlunandroid.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.gesture.Prediction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.data.CellOneContactData;
import com.neekle.kunlunandroid.data.CellPhoneContactData;
import com.neekle.kunlunandroid.util.StringOperator;
import com.neekle.kunlunandroid.view.controls.cellPhoneContact.CellPhoneContactView;
import com.neekle.kunlunandroid.view.controls.cellPhoneContact.CellPhoneContactView.OnMenuItemListener;
import com.neekle.kunlunandroid.view.controls.cellPhoneContact.CellPhoneContactView.OnOperationViewClickListener;
import com.neekle.kunlunandroid.xmpp.data.XmppJid;

public class CellPhoneContactAdapter extends BaseAdapter {

	public static final String FILTER_MATCH_STRING_SPLIT_IDENTIFIER = "|";

	private List<CellPhoneContactData> mDataList;
	private List<CellPhoneContactData> mBackupList;

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private ArrayFilter mFilter;

	private OnAdapterOperationViewClickListener mOnOperationViewClickListener;
	private OnAdapterMenuItemListener mOnMenuItemListener;

	public CellPhoneContactAdapter(Context context) {
		this.mContext = context;
		String inflater = Context.LAYOUT_INFLATER_SERVICE;
		mLayoutInflater = (LayoutInflater) context.getSystemService(inflater);
	}

	public CellPhoneContactAdapter(Context context,
			List<CellPhoneContactData> datalist) {
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
		CellPhoneContactData data = mDataList.get(position);

		View view = mLayoutInflater.inflate(
				R.layout.telephone_cm_cellphonecontact_adapter, null);

		View subView = view.findViewById(R.id.cellphonecontact);
		CellPhoneContactView cellPhoneContactView = (CellPhoneContactView) (subView);

		setListener(cellPhoneContactView, data);
		setInfo(cellPhoneContactView, data);

		return view;
	}

	public void setData(List<CellPhoneContactData> dataList) {
		this.mDataList = dataList;
		this.mBackupList = dataList;
	}

	private void setListener(CellPhoneContactView view,
			final CellPhoneContactData data) {
		view.setOnOperationViewClickListener(new OnOperationViewClickListener() {

			@Override
			public void onWholeViewClick() {
				if (mOnOperationViewClickListener != null) {
					mOnOperationViewClickListener.onWholeViewClick(data);
				}
			}

			@Override
			public void onPhoneViewClick() {
				if (mOnOperationViewClickListener != null) {
					mOnOperationViewClickListener.onPhoneViewClick(data);
				}
			}
		});

		view.setOnMenuItemClickListener(new OnMenuItemListener() {

			@Override
			public void onItemClick(int itemId) {
				if (mOnMenuItemListener != null) {
					mOnMenuItemListener.onItemClick(itemId, data);
				}
			}
		});
	}

	private void setInfo(CellPhoneContactView view, CellPhoneContactData data) {
		String name = data.getName();
		String number = data.getPhoneNumber();
		int phoneState = data.getPhoneState();
		String time = data.getTime();
		int userState = data.getUserState();
		boolean isPhoneWifi = data.isPhoneWifi();
		boolean isStar = data.isStar();

		view.setName(name);
		view.setPhoneNumber(number);
		view.setPhoneState(phoneState);
		view.setTime(time);
		view.setUserState(userState);
		view.setPhoneNet(isPhoneWifi);
		view.setIsStar(isStar);
	}

	public void setOnAdapterOperationViewClickListener(
			OnAdapterOperationViewClickListener listener) {
		mOnOperationViewClickListener = listener;
	}

	public interface OnAdapterOperationViewClickListener {
		public void onWholeViewClick(CellPhoneContactData data);

		public void onPhoneViewClick(CellPhoneContactData data);
	}

	public void setOnAdapterMenuItemClickListener(
			OnAdapterMenuItemListener listener) {
		mOnMenuItemListener = listener;
	}

	public interface OnAdapterMenuItemListener {
		public void onItemClick(int itemId, CellPhoneContactData data);
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
			mDataList = (List<CellPhoneContactData>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

		private void doFilter(FilterResults results, String prefix) {
			final int count = mDataList.size();
			List<CellPhoneContactData> newList = new ArrayList<CellPhoneContactData>();

			String[] toMatchStrings = null;
			if (prefix.contains(FILTER_MATCH_STRING_SPLIT_IDENTIFIER)) {
				toMatchStrings = prefix
						.split(FILTER_MATCH_STRING_SPLIT_IDENTIFIER);
			} else {
				toMatchStrings = new String[] { prefix };
			}
			int length = toMatchStrings.length;

			for (int i = 0; i < count; i++) {
				CellPhoneContactData data = mDataList.get(i);
				String partnerJid = data.getPartnerJid();
				XmppJid xmppJid = new XmppJid(partnerJid);
				String username = xmppJid.getUserName();

				for (int j = 0; j < length; j++) {
					String string = toMatchStrings[j];

					if (username.contains(string)) {
						newList.add(data);
						break;
					}
				}

			}

			results.values = newList;
			results.count = newList.size();
		}
	}

}
