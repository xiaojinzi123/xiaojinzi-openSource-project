package xiaojinzi.android.dbOrm.core.exception;

/**
 * 表的名字没有找到异常,需要进行增删改查的实体对象必须使用<br>
 * @Table注解
 * 
 * @author cxj QQ:347837667
 * @date 2015年12月9日
 *
 */
public class TableNameNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TableNameNotFoundException() {
		super();
	}

	public TableNameNotFoundException(String detailMessage) {
		super(detailMessage);
	}
	
}
