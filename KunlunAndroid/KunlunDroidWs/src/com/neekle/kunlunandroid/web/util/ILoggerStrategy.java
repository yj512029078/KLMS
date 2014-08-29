package com.neekle.kunlunandroid.web.util;

public interface ILoggerStrategy {
	void v(String tag, String message);

	void d(String tag, String message);

	void i(String tag, String message);

	void w(String tag, String message);

	void e(String tag, String message);

	void write(int level, String tag, String message);
}
