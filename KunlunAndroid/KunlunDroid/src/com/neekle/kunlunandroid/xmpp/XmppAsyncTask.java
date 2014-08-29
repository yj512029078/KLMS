package com.neekle.kunlunandroid.xmpp;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.xmpp.myWRAP.XmppStack;

import android.os.AsyncTask;
import android.util.Log;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.xmpp.common.XmppConstants;

class XmppAsyncTask extends AsyncTask<Object, Object, Object> {

	private IXmppCallback mIXmppCallback;
	private MyXmppStack mMyXmppStack;

	public XmppAsyncTask() {

	}

	public void setCallback(IXmppCallback callback) {
		mIXmppCallback = callback;
	}

	public void setMyXmppStack(MyXmppStack myXmppStack) {
		mMyXmppStack = myXmppStack;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);

		// temprorary not invoked
		if (mIXmppCallback != null) {
			// mIXmppCallback.onXmppNotify(result);
			mIXmppCallback = null;
			mMyXmppStack = null;
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Object... values) {
		super.onProgressUpdate(values);

	}

	@Override
	protected Object doInBackground(Object... params) {

		Object invokeObject = params[0];
		Object methodNameObject = Array.get(invokeObject, 0);
		Object paramsTypeObject = Array.get(invokeObject, 1);
		Object paramsValueObject = Array.get(invokeObject, 2);

		String methodName = (String) methodNameObject;
		Class<?>[] typeClasses = (Class<?>[]) paramsTypeObject;
		Object[] paramsValueArray = getObjectArray(paramsValueObject);

		Class<?> xmppStackClass = MyXmppStack.class;
		Method method;
		try {
			method = xmppStackClass.getMethod(methodName, typeClasses);
			method.invoke(mMyXmppStack, paramsValueArray);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private Object[] getObjectArray(Object paramsObject) {
		if (paramsObject != null) {
			int length = Array.getLength(paramsObject);
			Object[] objectArray = new Object[length];

			for (int i = 0; i < length; i++) {
				Object object = Array.get(paramsObject, i);
				objectArray[i] = object;
			}

			return objectArray;
		} else {
			return null;
		}
	}
}