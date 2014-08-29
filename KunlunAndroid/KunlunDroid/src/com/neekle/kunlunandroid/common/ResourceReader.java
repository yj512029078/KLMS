package com.neekle.kunlunandroid.common;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

public class ResourceReader {

	/**
	 * 以InputStream方式读取Assets文件夹内的文件。
	 * 
	 * @param c
	 * @param fileInAssets
	 * @return
	 * @throws IOException
	 */
	public static InputStream readAssetsAsInputStream(Context c,
			String fileInAssets) throws IOException {
		return c.getAssets().open(fileInAssets);
	}

	/**
	 * 将res文件读为Drawable对象
	 * 
	 * @param c
	 * @param resId
	 * @return
	 */
	public static Drawable readResAsDrawable(Context c, int resId) {
		return c.getResources().getDrawable(resId);
	}

	/**
	 * 将res文件读为Bitmap对象
	 * 
	 * @param c
	 * @param resId
	 * @return
	 * 
	 */
	public static Bitmap readResAsBitmap(Context c, int resId) {
		return BitmapFactory.decodeResource(c.getResources(), resId);
	}

	/**
	 * 读取RAW中的文件为字符串
	 * 
	 * @param c
	 * @param resId
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
	public static String readRawAsString(Context c, int resId)
			throws NotFoundException, IOException {
		return InputStreamUtil
				.toString(c.getResources().openRawResource(resId));
	}
}
