package xiaojinzi.base.java.net;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by cxj on 2016/6/4.
 * 用于描述网络请求的注解,基于方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestAnnotation {

    /**
     * 请求的网址
     * @return
     */
    String value() default "";

    /**
     * 请求的方法
     * @return
     */
    int requestMethod() default 0;


}
