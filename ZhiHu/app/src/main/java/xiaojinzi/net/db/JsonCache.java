package xiaojinzi.net.db;


import xiaojinzi.dbOrm.android.annotation.Column;
import xiaojinzi.dbOrm.android.annotation.Table;

/**
 * 这个类对应数据库中的一张表,
 * 
 * @author cxj QQ:347837667
 * @date 2015年12月10日
 *
 */
@Table
public class JsonCache {

	@Column(name = "_id", autoPk = true)
	private Integer id;

	@Column
	private String jsonUrl;

	@Column
	private String json;

	@Column
	private String cacheDate;

	public JsonCache() {
		super();
	}

	public JsonCache(Integer id, String jsonUrl, String json) {
		super();
		this.id = id;
		this.jsonUrl = jsonUrl;
		this.json = json;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJsonUrl() {
		return jsonUrl;
	}

	public void setJsonUrl(String jsonUrl) {
		this.jsonUrl = jsonUrl;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getCacheDate() {
		return cacheDate;
	}

	public void setCacheDate(String cacheDate) {
		this.cacheDate = cacheDate;
	}

	@Override
	public String toString() {
		return "JsonCache [id=" + id + ", jsonUrl=" + jsonUrl + ", json=" + json + ", cacheDate=" + cacheDate + "]";
	}

}
