package xiaojinzi.base.java.log;


/**
 * Logcat统一管理类
 * @author xiaojinzi
 */
public class L {

	private L() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化

	private static final String TAG = "xiaojinzi";


	public static void s(Object msg) {
		if (isDebug)
			System.out.println(msg);
	}

	public static void s(String tag, Object msg) {
		if (isDebug)
			System.out.println("Tag:" + tag + "\n      " + msg);
	}
}