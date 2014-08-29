package com.neekle.kunlunandroid.presenter.interf;

import java.util.ArrayList;

import com.neekle.kunlunandroid.adapter.CellOneContactAdapter;
import com.neekle.kunlunandroid.data.ContactInfo;
import com.neekle.kunlunandroid.view.specials.AlphabetListView;

public interface IAddsbookActivity {

	public void updateAddsbookAdapter(CellOneContactAdapter adapter);

	// public void notifyHuddleContacts(ArrayList<ContactInfo> arrayList);

	public void notifyMenuToChange(boolean isShowAll);

	public void setPositionLinstener(
			AlphabetListView.AlphabetPositionListener listener);

	public void test();
}
