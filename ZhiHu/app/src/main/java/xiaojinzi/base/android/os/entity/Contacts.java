package xiaojinzi.base.android.os.entity;

/**
 * 联系人实体对象
 * 
 * @author xiaojinzi
 *
 */
public class Contacts {

	private String name;

	private String phone;

	public Contacts() {
	}

	public Contacts(String name, String phone) {
		super();
		this.name = name;
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Contacts [name=" + name + ", phone=" + phone + "]";
	}

}
