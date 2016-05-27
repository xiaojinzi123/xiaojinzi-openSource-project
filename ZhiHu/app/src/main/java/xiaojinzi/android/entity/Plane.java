package xiaojinzi.android.entity;

/**
 * 平面的实体对象
 * 
 * @author xiaojinzi
 *
 */
public class Plane {
	
	private float width;
	
	private float height;

	public Plane() {
		super();
	}

	public Plane(float width, float height) {
		super();
		this.width = width;
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "Plane [width=" + width + ", height=" + height + "]";
	}
	
}
