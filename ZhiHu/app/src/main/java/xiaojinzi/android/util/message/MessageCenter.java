package xiaojinzi.android.util.message;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个类用来传送消息，跨activity或者fragment传送消息，给特定的activity或者fragment发送消息等等
 * 
 * @author 小金子
 * 
 */
public class MessageCenter {

	private static Map<String, MessageHandle> map = null;

	/**
	 * 构造函数私有化
	 */
	private MessageCenter() {
	};

	// 声明一个自己
	private static MessageCenter activityMessage = null;

	/**
	 * 获取本类的一个实例对象
	 * 
	 * @return
	 */
	public static MessageCenter getInstance() {
		if (activityMessage == null) {
			map = new HashMap<String, MessageHandle>();
			activityMessage = new MessageCenter();
		}
		return activityMessage;
	}

	/**
	 * 添加 实现了MessageHandle接口的类 可以接收到别人发送过来的消息
	 * 
	 * @param handle
	 *            要接受消息的类必须实现这个接口
	 * @param flag_tag
	 *            标识自身，最好推荐用类名，防止重复
	 * @return
	 */
	public void addAcceptMessage(MessageHandle handle, String flag_tag) {
		map.put(flag_tag, handle);
	}

	/**
	 * 在消息中心注销接受消息
	 * 
	 * @param flag_tag
	 */
	public void removeAcceptMessage(String flag_tag) {
		map.remove(flag_tag);
	}

	/**
	 * 返回某个标识对应的消息发送接口
	 * 
	 * @param flag_tag
	 * @return
	 */
	public MessageHandle getMessageHandle(String flag_tag) {
		return map.get(flag_tag);
	}

}
