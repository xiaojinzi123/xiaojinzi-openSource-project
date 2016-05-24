package xiaojinzi.dbOrm.android.core.exception;

/**
 * 关于字段的异常<br>
 * 1.字段的获取<br>
 * 2.字段的注入<br>
 * 
 * @author cxj QQ:347837667
 * @date 2015年12月9日
 *
 */
public class FieldException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FieldException() {
		super();
	}

	public FieldException(String detailMessage) {
		super(detailMessage);
	}
}
