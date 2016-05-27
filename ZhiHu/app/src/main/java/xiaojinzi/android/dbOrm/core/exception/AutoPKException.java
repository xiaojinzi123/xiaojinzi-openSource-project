package xiaojinzi.android.dbOrm.core.exception;

/**
 * 1.主键没有找到异常<br>
 * 2.主键有两个及以上的异常
 * 
 * @author cxj QQ:347837667
 * @date 2015年12月9日
 *
 */
public class AutoPKException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AutoPKException() {
		super();
	}

	public AutoPKException(String detailMessage) {
		super(detailMessage);
	}
}
