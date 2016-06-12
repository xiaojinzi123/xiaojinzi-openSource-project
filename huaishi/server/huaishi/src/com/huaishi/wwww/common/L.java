package com.huaishi.wwww.common;

//Logcat统一管理类
public class L {

	private L() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static boolean isDebug = false;// 是否需要打印bug，可以在application的onCreate函数里面初始化

	// 下面是默认tag的函数
	public static void s(Object msg) {
		if (isDebug)
			System.out.println(msg);
	}

	// 下面是传入自定义tag的函数
	public static void s(String tag, Object msg) {
		if (isDebug)
			System.out.println("Tag:" + tag + "\n      " + msg);
	}

	/**
	 * 打印错误信息
	 * 
	 * @param e
	 */
	public static void err(Exception e) {
		if (isDebug)
			e.printStackTrace();
	}

	/**
	 * 打印错误信息,带上tag
	 * 
	 * @param tag
	 * @param e
	 */
	public static void err(String tag, Exception e) {
		if (isDebug)
			e.printStackTrace();
	}

}