package com.neekle.kunlunandroid.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 汉字转拼音工具类
 * 
 * @author kevin
 * */
public class HanziToPinYinUtil {

	/**
	 * 将字符串中的中文（所有的汉字）转换为拼音（其他字符不变）
	 * 
	 * */
	public static String getPingYin(String inputString) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = inputString.trim().toCharArray();
		String output = "";

		try {
			for (int i = 0; i < input.length; i++) {
				// 判断是否是汉字
				if (java.lang.Character.toString(input[i]).matches(
						"[\\u4E00-\\u9FA5]+")) {
					// 将每个汉字转化为拼音
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							input[i], format);
					output += temp[0];
				} else
					output += java.lang.Character.toString(input[i]);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * 获得字符串的首字母 首字符 转汉语拼音
	 * 
	 * */
	public static String getFirstChar(String value) {
		// 首字符
		char firstChar = value.charAt(0);
		// 首字母分类
		String first = null;
		// 是否是非汉字
		String[] print = PinyinHelper.toHanyuPinyinStringArray(firstChar);

		if (print == null) {

			// 将小写字母改成大写
			if ((firstChar >= 97 && firstChar <= 122)) {
				firstChar -= 32;
			}
			if (firstChar >= 65 && firstChar <= 90) {
				first = String.valueOf((char) firstChar);
			} else {
				// 认为首字符为数字或者特殊字符
				first = "#";
			}
		} else {
			// 如果是中文 分类大写字母
			first = String.valueOf((char) (print[0].charAt(0) - 32));
		}
		if (first == null) {
			first = "?";
		}
		return first;
	}
	/**
	 * 汉字转换位汉语拼音首字母，英文字符不变
	 * 
	 * @param hanzi
	 *            汉字
	 * @return 拼音
	 */
	// public static String converterToFirstSpell(String hanzi){
	// String pinyinName = "";
	// char[] nameChar = hanzi.toCharArray();
	// HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
	// defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
	// defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	// for (int i = 0; i < nameChar.length; i++) {
	// if (nameChar[i] > 128) {
	// try {
	// pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i],
	// defaultFormat)[0].charAt(0);
	// } catch (BadHanyuPinyinOutputFormatCombination e) {
	// e.printStackTrace();
	// }
	// }else{
	// pinyinName += nameChar[i];
	// }
	// }
	// return pinyinName;
	// }
}
