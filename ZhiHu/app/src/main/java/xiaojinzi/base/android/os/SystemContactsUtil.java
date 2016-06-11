package xiaojinzi.base.android.os;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import xiaojinzi.base.android.os.entity.Contacts;
import xiaojinzi.base.java.common.StringUtil;

/**
 * 系统联系人的工具类 需要权限：
 * <uses-permission android:name="android.permission.READ_CONTACTS" />
 * <uses-permission android:name="android.permission.WRITE_CONTACTS" />
 * 
 * @author xiaojinzi
 *
 */
public class SystemContactsUtil {

	/**
	 * 查询的基本uri
	 */
	public final static Uri uri = ContactsContract.RawContacts.CONTENT_URI;

	public final static Uri people = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

	// 获取联系人的名字的字段名称
	public final static String personName = ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY;   //display_name

	/**
	 * 获取系统联系人的所有联系人的姓名
	 * 
	 * @param context
	 * @return
	 */
	public static List<String> getAllContactsName(Context context) {

		// 声明返回值
		List<String> list = new ArrayList<String>();

		ContentResolver resolver = context.getContentResolver();
		Cursor c = resolver.query(uri, null, null, null, null);

		while (c.moveToNext()) {
			list.add(c.getString(c.getColumnIndex(personName)));
		}

		return list;
	}

	/**
	 * 获取系统联系人的所有联系人的号码
	 * 
	 * @param context
	 * @return
	 */
	public static List<String> getAllContactsPhone(Context context) {

		// 声明返回值
		List<String> list = new ArrayList<String>();

		ContentResolver resolver = context.getContentResolver();
		Cursor c = resolver.query(people, null, null, null, null);

		while (c.moveToNext()) {
			String phone = c.getString(c.getColumnIndex("data1"));
			list.add(phone);
		}

		return list;
	}

	/**
	 * 获取一个uri查询出来的所有数据
	 * 
	 * @param uri
	 * @param context
	 * @return
	 */
	public static List<String> getAllValuesOfUri(Uri uri, Context context) {

		// 声明返回值
		List<String> list = new ArrayList<String>();

		Cursor c = context.getContentResolver().query(uri, null, null, null, null);

		while (c.moveToNext()) {
			int columnCount = c.getColumnCount();
			for (int i = 0; i < columnCount; i++) {
				// 获取名称
				String name = c.getColumnName(i);
				// 获取value
				String value = c.getString(i);

				list.add("name=" + name + ",value=" + value);
			}
		}

		return list;

	}

	/**
	 * 返回所有的联系人对象,对象中包括姓名和电话
	 * 
	 * @param context
	 * @return
	 */
	public static List<Contacts> getAllContacts(Context context) {

		// 声明返回值
		List<Contacts> list = new ArrayList<Contacts>();

		Cursor c = context.getContentResolver().query(people, null, null, null, null);

		while (c.moveToNext()) {
			String phone = c.getString(c.getColumnIndex("data1"));
			phone = StringUtil.filterPhoneNumber(phone);
			if (phone != null) {
				list.add(new Contacts(c.getString(c.getColumnIndex(personName)), phone));
			}
		}

		return list;

	}

}
