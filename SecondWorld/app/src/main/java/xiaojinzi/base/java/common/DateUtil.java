package xiaojinzi.base.java.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期的工具类
 * 
 * @author xiaojinzi
 * 
 */
public class DateUtil {

	/**
	 * 中国格式的日期:yyyy-MM-dd
	 */
	public static final String CHINADATE_STYLE_1 = "yyyy-MM-dd";

	/**
	 * 中国格式的日期:yyyy年MM月dd
	 */
	public static final String CHINADATE_STYLE_2 = "yyyy年MM月dd";

	/**
	 * 中国格式的时间:HH:mm:ss
	 */
	public static final String CHINATIME_STYLE1 = "HH:mm:ss";

	/**
	 * 中国格式的时间:HH时mm分ss秒
	 */
	public static final String CHINATIME_STYLE2 = "HH时mm分ss秒";

	/**
	 * 中国格式的日期时间:yyyy-MM-dd HH:mm:ss
	 */
	public static final String CHINADATETIME_STYLE1 = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 中国格式的日期时间:yyyy年MM月dd HH时mm分ss秒
	 */
	public static final String CHINADATETIME_STYLE2 = "yyyy年MM月dd HH时mm分ss秒";

	/**
	 * 一天所包含的秒数
	 */
	public static final int DAYTIMEMILLIS = 24 * 60 * 60 * 1000;

	// 格式化对象
	private SimpleDateFormat sdf = null;

	/**
	 * 构造函数
	 * 
	 * @param pattern
	 *            匹配的字符串,这里最好采用类中定义好的常量<br>
	 *            {@link DateUtil#CHINADATE_STYLE_1} or
	 *            {@link DateUtil#CHINADATE_STYLE_2} or
	 *            {@link DateUtil#CHINATIME_STYLE1} or
	 *            {@link DateUtil#CHINATIME_STYLE2} or
	 *            {@link DateUtil#CHINADATETIME_STYLE1} or
	 *            {@link DateUtil#CHINADATETIME_STYLE2}
	 */
	public DateUtil(String pattern) {
		sdf = new SimpleDateFormat(pattern, Locale.CHINA);
	}

	/**
	 * 返回格式化后的字符串
	 * 
	 * @param d
	 * @return
	 */
	public String formatDate(Date d) {
		return sdf.format(d);
	}

	/**
	 * 返回解析后的日起对象
	 * 
	 * @param content
	 *            要解析的字符串
	 * @return
	 * @throws ParseException
	 *             解析失败抛出的异常
	 */
	public Date parse(String content) throws ParseException {
		return sdf.parse(content);
	}

	/**
	 * 判断某个日期是星期几，返回1-7，分别表示星期一到星期天
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeek(Date date) {
		// 创建一个把日期格式化成星期几的格式化日期对象
		SimpleDateFormat s = new SimpleDateFormat("E", Locale.CHINA);
		String week = s.format(date);
		if (week.equals("星期一")) {
			return 1;
		} else if (week.equals("星期二")) {
			return 2;
		} else if (week.equals("星期三")) {
			return 3;
		} else if (week.equals("星期四")) {
			return 4;
		} else if (week.equals("星期五")) {
			return 5;
		} else if (week.equals("星期六")) {
			return 6;
		} else {
			return 7;
		}
	}

	/**
	 * 判断某年某月有几天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDayNumberOfMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			return 31;
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		} else {
			if (year % 4 == 0) {
				return 29;
			} else {
				return 28;
			}
		}
	}

	/**
	 * 返回日期对象中的年份，整数表示
	 * 
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		// 创建一个把日期格式化成月份的格式化日期对象
		SimpleDateFormat s = new SimpleDateFormat("yyyy", Locale.CHINA);
		return Integer.parseInt(s.format(date));
	}

	/**
	 * 返回日期对象中的月份，整数表示1-12
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		// 创建一个把日期格式化成月份的格式化日期对象
		SimpleDateFormat s = new SimpleDateFormat("MM", Locale.CHINA);
		return Integer.parseInt(s.format(date));
	}

	/**
	 * 返回日期对象中的几号
	 * 
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {
		// 创建一个把日期格式化成日子的格式化日期对象
		SimpleDateFormat s = new SimpleDateFormat("dd", Locale.CHINA);
		return Integer.parseInt(s.format(date));
	}

	/**
	 * 计算两个时间之间隔了多少天<br>
	 * 经过验证,是准确的
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getDaysBetweenDates(Date startDate, Date endDate) {
		return getDaysBetweenDates(startDate.getTime(), endDate.getTime());
	}

	/**
	 * 计算两个时间之间隔了多少天<br>
	 * 经过验证,是准确的
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getDaysBetweenDates(long startDate, long endDate) {
		// 获取到两个时间的相差毫秒值
		long tmp = endDate - startDate;
		System.out.println("tmp=" + tmp);
		return tmp / (1000 * 60 * 60 * 24);
	}

	/**
	 * 获取两个时间之间的小时数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getHoursBetweenDates(Date startDate, Date endDate) {
		// 获取到两个时间的相差毫秒值
		long tmp = endDate.getTime() - startDate.getTime();
		System.out.println("tmp=" + tmp);
		return tmp / (1000 * 60 * 60);
	}

}
