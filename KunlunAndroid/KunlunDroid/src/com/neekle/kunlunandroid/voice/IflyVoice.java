package com.neekle.kunlunandroid.voice;

import java.util.ArrayList;

import android.content.Context;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.neekle.kunlunandroid.R;

/**
 * @author yj
 * 
 */
public class IflyVoice implements RecognizerDialogListener {

	private RecognizerDialog mIatDialog;
	private static IflyVoice mIflyVoice;
	private IVoiceRecoginzerCallback mIVoiceRecoginzerCallback;

	private static final String ENGINE = "sms";
	private static final String AREA = "";

	private IflyVoice() {

	}

	public static IflyVoice getInstance() {
		if (mIflyVoice == null) {
			mIflyVoice = new IflyVoice();
		}

		return mIflyVoice;
	}

	public void setCallback(IVoiceRecoginzerCallback callback) {
		mIVoiceRecoginzerCallback = callback;
	}

	public void initRecognizerDialog(Context context) {
		String initParams = "appid="
				+ context.getString(R.string.ifly_voice_normal_id);
		mIatDialog = new RecognizerDialog(context, initParams);
		mIatDialog.setListener(this);
	}

	public void showRecognizerDialog() {
		mIatDialog.setEngine(ENGINE, AREA, null);
		mIatDialog.setSampleRate(RATE.rate16k);
		mIatDialog.show();
	}

	@Override
	public void onEnd(SpeechError arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResults(ArrayList<RecognizerResult> arg0, boolean arg1) {
		StringBuilder builder = new StringBuilder();
		for (RecognizerResult recognizerResult : arg0) {
			builder.append(recognizerResult.text);
		}

		String text = builder.toString();
		mIVoiceRecoginzerCallback.onVoiceResultNotify(text);
	}
}
