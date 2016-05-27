package xiaojinzi.android.dbOrm.core.exception;

/**
 * 创建对象异常<br>
 * 
 * @author cxj QQ:347837667
 * @date 2015年12月9日
 *
 */
public class CreateEntityException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CreateEntityException() {
		super();
	}

	public CreateEntityException(String detailMessage) {
		super(detailMessage);
	}
}
