package xiaojinzi.android.entity;

import java.io.FileDescriptor;
import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/25.
 * <p/>
 * 在解析歌曲对象的时候，首字母利用工具类来实现分解，歌手是从文件名过中提取出的，比如 我爱你中国-汪峰.mp3
 * 这首歌歌手就是汪峰，歌曲名就是我爱你中国，如果歌名和歌手名字不是以-分割的，则.前面的都会被认为是歌曲名，而歌手名为为止
 */
public class Music implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 歌曲id,数据库中的映射
	 */
	private Integer id;

	/**
	 * 名字的首字母
	 */
	private Character sort_key;

	/**
	 * 歌曲名称
	 */
	private String name;

	/**
	 * 歌曲路径
	 */
	private String path;

	/**
	 * 播放的次数
	 */
	private int playTimes;

	/**
	 * 歌手名字
	 */
	private String singerName;

	/**
	 * 本地资源的id
	 */
	private FileDescriptor fd;

	public Music() {
	}

	public Music(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Character getSort_key() {
		return sort_key;
	}

	public void setSort_key(Character sort_key) {
		this.sort_key = sort_key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getPlayTimes() {
		return playTimes;
	}

	public void setPlayTimes(int playTimes) {
		this.playTimes = playTimes;
	}

	public String getSingerName() {
		return singerName;
	}

	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}


	public FileDescriptor getFd() {
		return fd;
	}

	public void setFd(FileDescriptor fd) {
		this.fd = fd;
	}

	@Override
	public String toString() {
		return "Music [id=" + id + ", sort_key=" + sort_key + ", name=" + name + ", path=" + path + ", playTimes="
				+ playTimes + ", singerName=" + singerName + ", fd=" + fd + "]";
	}

	/**
	 * 比较的是路径是否相同
	 *
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof Music && this.path != null && this.path.equals(((Music) o).getPath());
	}
}
