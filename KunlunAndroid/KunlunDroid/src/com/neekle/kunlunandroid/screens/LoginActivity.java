package com.neekle.kunlunandroid.screens;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.common.imgcombiner.ImageCombiner;
import com.neekle.kunlunandroid.data.LoginBasicInfo;
import com.neekle.kunlunandroid.presenter.common.FriendOperation;
import com.neekle.kunlunandroid.presenter.impl.LoginActiyPresenter;
import com.neekle.kunlunandroid.presenter.interf.ILoginActivity;
import com.neekle.kunlunandroid.services.AutoConnectService;
import com.neekle.kunlunandroid.sip.SipService;
import com.neekle.kunlunandroid.util.BitmapOperator;
import com.neekle.kunlunandroid.util.DeviceInfo;
import com.neekle.kunlunandroid.view.controls.cellAccountInput.CellAccountInputAdapter;
import com.neekle.kunlunandroid.view.controls.cellAccountInput.CellAccountInputView;
import com.neekle.kunlunandroid.view.controls.cellAccountInput.CellAccountInputView.CellAccountInputTextWatcher;
import com.neekle.kunlunandroid.view.specials.MyProgressDialog;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.data.XmppJid;

/**
 * 登录
 * 
 * @author yj
 */
public class LoginActivity extends Activity implements ILoginActivity {

	private static final String REGX_STRING = "@";

	// Do not delete it, keep it temporary
	private boolean mIsAccountLegal;

	private ImageView mTopCoverImgv;
	private CellAccountInputView mCellAccountInputView;
	private EditText mPasswdEditxt;
	private RelativeLayout mLoginRelati;
	private ImageView mSettingImgv;
	private MyProgressDialog mMyProgressDialog = new MyProgressDialog();

	private LoginActiyPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// XmppData xmppData = XmppData.getSingleton();
		// xmppData.setJidString("test011@fe.shenj.com");
		// AddsbookOperation addsbookOperation = new AddsbookOperation();
		// addsbookOperation.doWsRequestLogin();

		mPresenter = new LoginActiyPresenter(this, this);
		LoginBasicInfo data = mPresenter.readLoginBasicInfoFromDb();
		mPresenter.setXmppData(data);
		boolean isToMain = mPresenter.judgeIfToMain(data);
		if (isToMain) {
			Class<? extends Activity> targetActivity = MainScreenActivity.class;
			mPresenter.doSwitchMainScreenActivity(this, targetActivity);
			AutoConnectService.startAutoConnectService(this);
			return;
		}

		findViewsAndSetAttributes();

		mPresenter.initData();
		setCellAccountInputAdapter();

		// XmppData xmppData = XmppData.getSingleton();
		// String jidString = "test011@fe.shenj.com";
		// String passwdString = "123456";
		// xmppData.setAccountString(jidString, passwdString);
		// XmppJid xmpJid = xmppData.getJid();
		// String myJid = xmpJid.getBare();

		// ImageCombiner combiner = new ImageCombiner();
		// String[] pathStrings = { null, null, null };
		// Bitmap bitmap = combiner.getCombinedImg(pathStrings);
		// String friendJid = "yj001@fe.shenj.com";
		// combiner.getSavedBitmapAsJpeg(friendJid, bitmap);

		// FriendOperation friendOperation = new FriendOperation();
		// String friendJid = "yj001@fe.shenj.com";
		// friendOperation.doLoadUserinfoToUpdateXmppFriendAndDb(friendJid);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// 暂时不使用这个，除非有充足理由（根据google code 来看，不要控制本来该由系统控制的逻辑）
		// moveTaskToBack(true);

		doLogout();
	}

	private void findViewsAndSetAttributes() {
		mTopCoverImgv = (ImageView) findViewById(R.id.imgv_top_cover_bg);
		// 将图片居中显示
		mTopCoverImgv.setScaleType(ScaleType.CENTER);
		Bitmap resizedBitmap = resizeBitmap();
		mTopCoverImgv.setImageBitmap(resizedBitmap);

		mCellAccountInputView = (CellAccountInputView) findViewById(R.id.cell_account_input_view);
		mCellAccountInputView.setThreshold(0);
		mCellAccountInputView.setConstraintRegEx(REGX_STRING);
		mCellAccountInputView
				.setOnContentValidationListener(mInvalidateListener);
		mCellAccountInputView
				.addCellAccountInputTextWatcher(mCellAccountInputTextWatcher);

		mPasswdEditxt = (EditText) findViewById(R.id.edittxt_input_password);

		mLoginRelati = (RelativeLayout) findViewById(R.id.relati_login);
		mLoginRelati.setOnClickListener(mOnClickListener);

		mSettingImgv = (ImageView) findViewById(R.id.imgv_setting);
		mSettingImgv.setOnClickListener(mOnClickListener);
	}

	private void setCellAccountInputAdapter() {
		ArrayList<String> dataList = mPresenter.getHisAccounts();
		CellAccountInputAdapter adapter = new CellAccountInputAdapter(this,
				dataList);
		mCellAccountInputView.setAdapter(adapter);
	}

	private Bitmap resizeBitmap() {
		Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.main_top_cover_bg_325_167);
		int screenWidth = DeviceInfo.getScreenWidth(this);
		int width = srcBitmap.getWidth();
		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) screenWidth) / width;
		// scaleHeight = ((float) screenHeight) / height;
		float scaleHeight = scaleWidth;
		Bitmap resizedBitmap = BitmapOperator.getResizedBitmap(srcBitmap,
				scaleWidth, scaleHeight);

		return resizedBitmap;
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mLoginRelati) {
				doLogin();
			} else if (v == mSettingImgv) {
				switchLoginSettingActivity();
			}
		}
	};

	private void doLogin() {
		mPasswdEditxt.requestFocus();

		String jidString = mCellAccountInputView.getEditableText();
		Editable editable = mPasswdEditxt.getText();
		String pwd = editable.toString();

		// openfire 服务器登录账号及服务器地址
		// 帐户名格式：昵称 + "@" + 服务器地址 + "标示"
		// 例如：test01@kevin-pc/gloox
		// 昵称：test01
		// 服务器地址：Kevin-pc
		// 标示：gloox(可能用来标注某台设备登录或者某个客户端登陆等等)

		// yj
		// jidString = "jyx@yj-pc/gloox";
		// pwd = "123456";
		// String hostIp = "192.168.1.184";

		// jidString = "jyx@yj-pc/gloox";
		// pwd = "123456";
		// String hostIp = "172.30.3.158";

		// dgd
		// jidString = "test01@kevin-pc/gloox";
		// pwd = "123456";
		// String hostIp = "172.30.3.52";

		// dgd
		// jidString = "test01@kevin-pc/gloox";
		// pwd = "123456";
		// String hostIp = "192.168.1.176";

		// tigase 服务器登录账号及服务器地址
		//
		// jidString = "dgd001@fe.shenj.com/gloox";
		// pwd = "123";
		// jidString = "test016@fe.shenj.com/gloox";
		// pwd = "123456";

		// jidString = "yyyy@fe.shenj.com/gloox";
		// pwd = "yyyyyy";


		// jidString = "dgd001@fe.shenj.com/gloox";
		// pwd = "123";

		// jidString = "ly@fe.shenj.com/gloox";
		// pwd = "123456";
		// jidString = "ly@fe.shenj.com/gloox";
		// pwd = "123456";

		// jidString = "test016@fe.shenj.com/gloox";
		// pwd = "123456";
		// jidString = "wdy001@fe.shenj.com/gloox";
		// pwd = "123456";
		// jidString = "dgd001@fe.shenj.com/gloox";
		// pwd = "123";
		//
		// jidString = "yar5@fe.shenj.com/gloox";
		// pwd = "123456";
		// jidString = "test015@fe.shenj.com/gloox";
		// pwd = "123456";
		// jidString = "dgd002@fe.shenj.com/gloox";
		// pwd = "123";
		// String hostIp = "192.168.1.232";

		// jidString = "test011@fe.shenj.com/gloox";
		// // // // // jidString = "jyx@yj-pc/gloox";
		// pwd = "123456";

		// String hostIp = "172.30.3.107";
		// pwd = "123456";
		// String hostIp = "172.30.3.107";

		mPresenter.login(jidString, pwd);
	}

	// 后面需要修改，退出操作不在这里做。。。
	private void doLogout() {
		mPresenter.clearResource();

		// 考虑下，登出是子线程操作，在子线程没有执行完的时刻，如下代码已执行，是否有问题
		// 后面要做默认不退出，此代码也不应该在此处
		logoutAndExitSip();
		mPresenter.logout();
		AutoConnectService.stopAutoConnectService(this);
		// startToLauncher();
		mPresenter.killProcess();
	}

	private void logoutAndExitSip() {
		SipService sipService = SipService.getSingleton();
		sipService.signOut();
		sipService.exit();
	}

	private void startToLauncher() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

	private CellAccountInputView.OnContentValidateListener mInvalidateListener = new CellAccountInputView.OnContentValidateListener() {

		@Override
		public void onValidateResult(boolean isLegal, String hint) {
			mIsAccountLegal = isLegal;
		}

	};

	private CellAccountInputView.CellAccountInputTextWatcher mCellAccountInputTextWatcher = new CellAccountInputTextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	public void switchLoginSettingActivity() {
		Class<? extends Activity> intentClass = LoginSettingActivity.class;
		mPresenter.doSwitchLoginSettingActivity(this, intentClass);
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showErrorToast(String msg, boolean isLong) {
		ActivityUtil.show(this, msg, isLong);
	}

	@Override
	public void showProgressDialog(String tittle, String msg) {
		if (mMyProgressDialog != null) {
			mMyProgressDialog.showProgressDialog(this, tittle, msg);
		}
	}

	@Override
	public void shutProgressDialog() {
		if (mMyProgressDialog != null) {
			mMyProgressDialog.closeProgressDialog();
		}
	}

	@Override
	public void showLoginAccount(String account) {
		mCellAccountInputView.setEditableText(account);
	}

}
