package com.huaishi.wwww.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类是有关反射的工具类
 * 
 * @author xiaojinzi
 *
 */
public class ReflectionUtil {

	/**
	 * 标识
	 */
	public final static String TAG = "ReflectionUtil";

	/**
	 * 八种基本数据类型和八种包装类和字符串的类型
	 */
	public final static Class<?>[] baseClassType = { byte.class, Byte.class, short.class, Short.class, int.class,
			Integer.class, long.class, Long.class, float.class, Float.class, double.class, Double.class, boolean.class,
			Boolean.class, char.class, Character.class, String.class };

	/**
	 * 这个标识符可以控制是否打印日志
	 */
	public static boolean isLog = false;

	/**
	 * 根据对象名称获取这个字段在对象中的数据
	 * 
	 * @param o
	 *            这个对象中的某一个字段要被获取
	 * @param fieldName
	 *            字段的名字
	 * @return 返回这个字段在对象o中的数据,获取失败返回null
	 */
	public static <T> T getFieldValue(Object o, String fieldName) {
		try {
			Class<? extends Object> c = o.getClass();
			Field f = c.getDeclaredField(fieldName);
			return getFieldValue(o, f);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据字段从对象中获取这个对象的数值
	 * 
	 * @param o
	 *            需要从这个对象中获取某个字段的数据
	 * @param f
	 *            要获取数据的字段
	 * @return 返回对象o的字段f的数据,已经帮忙强制造型
	 * 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Object o, Field f) throws IllegalArgumentException, IllegalAccessException {
		f.setAccessible(true);
		return (T) f.get(o);
	}

	/**
	 * 这个方法根据名字来获取一个类对象中的某一些方法,<br>
	 * 这是因为用户可能不知道某个名字的方法的参数列表
	 * 
	 * @param clazz
	 *            Class类对象
	 * @param methodName
	 *            方法名字
	 * @return
	 */
	public static List<Method> getMethodByName(Class<?> clazz, String methodName) {
		// 获取所有的方法
		Method[] methods = clazz.getDeclaredMethods();
		List<Method> ListMethods = null;
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (method.getName().equals(methodName)) {
				if (ListMethods == null) {
					ListMethods = new ArrayList<Method>();
				}
				ListMethods.add(method);
			}
		}
		return ListMethods;
	}

	/**
	 * 判断一个Class对象是不是八种基本数据类型和八种包装类和字符串的类型,包括: <br>
	 * 1.byte 和 Byte<br>
	 * 2.short 和 Short<br>
	 * 3.int 和 Integer <br>
	 * 4.long 和 Long<br>
	 * 5.float 和 Float<br>
	 * 6.double 和 Double<br>
	 * 7.boolean 和 Boolean<br>
	 * 8.char 和 Character <br>
	 * 9.String
	 * 
	 * @param clszz
	 * @return
	 */
	public static boolean isBaseClassType(Class<?> clszz) {
		for (int i = 0; i < baseClassType.length; i++) {
			Class<?> c = baseClassType[i];
			if (c == clszz) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 对字段进行注入,私有的也强行注入,包括:<br>
	 * 1.八种基本数据类型<br>
	 * 2.字符串
	 * 
	 * @param f
	 *            要注入数据的字段
	 * @param o
	 *            要注入的对象
	 * @param values
	 *            要注入的数据
	 * @exception Exception
	 */
	public static boolean setValue(Field f, Object o, String value) throws Exception {
		Class<?> fieldType = f.getType();
		f.setAccessible(true);// 私有的也强行注入
		if (fieldType == byte.class || fieldType == Byte.class) {
			f.set(o, Byte.parseByte(value));
			return true;
		} else if (fieldType == short.class || fieldType == Short.class) {
			f.set(o, Short.parseShort(value));
			return true;
		} else if (fieldType == int.class || fieldType == Integer.class) {
			f.set(o, Integer.parseInt(value));
			return true;
		} else if (fieldType == long.class || fieldType == Long.class) {
			f.set(o, Long.parseLong(value));
			return true;
		} else if (fieldType == float.class || fieldType == Float.class) {
			f.set(o, Float.parseFloat(value));
			return true;
		} else if (fieldType == double.class || fieldType == Double.class) {
			f.set(o, Double.parseDouble(value));
			return true;
		} else if (fieldType == boolean.class || fieldType == Boolean.class) {
			f.set(o, Boolean.parseBoolean(value));
			return true;
		} else if (fieldType == char.class || fieldType == Character.class) {
			f.set(o, "".equals(value) ? ' ' : value.charAt(0));
			return true;
		} else if (fieldType == String.class) {
			f.set(o, value);
			return true;
		}
		return false;
	}

	/**
	 * 对一个Class类进行字段的迭代
	 * 
	 * @param clazz
	 *            Class类
	 * @param l
	 *            回调监听的接口
	 */

	public static void fieldIterator(Class<?> clazz, HanderFieldListener l) {
		// 获取所有的字段,包括私有的参数
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			// 循环中的字段
			Field field = fields[i];
			field.setAccessible(true);

			// 获取字段名字
			String fieldName = field.getName();

			// 获取字段的类型
			Class<?> type = field.getType();

			try {
				l.handerField(field, fieldName, type);
			} catch (Exception e) {
				if (isLog) {
					e.printStackTrace();
					L.s(TAG, e.getMessage() + ":" + e.getCause());
				}
			}

		}

	}

	/**
	 * 迭代字段的接口
	 * 
	 * @author cxj
	 *
	 */
	public interface HanderFieldListener {

		/**
		 * 处理字段的回调接口
		 * 
		 * @param f
		 * @param fieldName
		 *            字段名称
		 */
		public void handerField(Field f, String fieldName, Class<?> type) throws Exception;

	}

	/**
	 * 根据字段返回这个字段对应的get方法的名称
	 * 
	 * @param f
	 * @return
	 */
	public static String getGetMethodNameOfField(Field f) {
		// 先获取字段的名称
		String name = f.getName();

		Class<?> type = f.getType();

		String returnVlaue = null;

		// 如果这个字段是布尔型的
		if (type == boolean.class || type == Boolean.class) {
			if (name.startsWith("is")) { // 如果这个字段的名称是is开头的,比如: isOk
				return name;
			} else {
				returnVlaue = "is" + StringUtil.firstCharToUpperCase(name);
			}
		} else {
			returnVlaue = "get" + StringUtil.firstCharToUpperCase(name);
		}

		if (isLog) {
			System.out.println("---------  " + TAG + "\n" + returnVlaue);
		}

		return returnVlaue;

	}

	/**
	 * 根据字段返回这个字段对应的set方法的名称
	 * 
	 * @param f
	 * @return
	 */
	public static String getSetMethodNameOfField(Field f) {
		// 先获取字段的名称
		String name = f.getName();

		Class<?> type = f.getType();

		String returnVlaue = null;

		// 如果这个字段是布尔型的
		if (type == boolean.class || type == Boolean.class) {
			if (name.startsWith("is")) { // 如果这个字段的名称是is开头的,比如: isOk
				returnVlaue = "set" + StringUtil.firstCharToUpperCase(name.substring(2));
			} else {
				returnVlaue = "set" + StringUtil.firstCharToUpperCase(name);
			}
		} else {
			returnVlaue = "set" + StringUtil.firstCharToUpperCase(name);
		}

		if (isLog) {
			System.out.println("---------  " + TAG + "\n" + returnVlaue);
		}

		return returnVlaue;

	}

	/**
	 * 获取某一个字段上面的泛型参数数组,典型的就是获取List对象里面是啥参数<br>
	 * 缺陷就是这个Field字段只能在对象中才能获取到,因为这里的参数是Field,所以只有对象有
	 * 
	 * @param f
	 * @return
	 */
	public static Class<?>[] getParameterizedType(Field f) {

		// 获取f字段的通用类型
		Type fc = f.getGenericType(); // 关键的地方得到其Generic的类型

		// 如果不为空并且是泛型参数的类型
		if (fc != null && fc instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) fc;

			Type[] types = pt.getActualTypeArguments();

			if (types != null && types.length > 0) {
				Class<?>[] classes = new Class<?>[types.length];
				for (int i = 0; i < classes.length; i++) {
					classes[i] = (Class<?>) types[i];
				}
				return classes;
			}
		}
		return null;
	}

}
