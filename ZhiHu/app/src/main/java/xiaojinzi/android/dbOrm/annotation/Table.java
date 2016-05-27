package xiaojinzi.android.dbOrm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 这个注解是用在类上面的,运行的时候有效
 * 
 * @author xiaojinzi
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	/**
	 * 表的名字
	 * 
	 * @return
	 */
	String value() default "";
}
