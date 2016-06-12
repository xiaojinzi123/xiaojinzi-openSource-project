package com.huaishi.wwww.common;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 自定义分页标签
 * 
 * @dateTime 2015年11月27日 上午11:22:16
 * @Company xjzCompany
 * @author xiaojinzi
 */
public class PageTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 边距
	 */
	private static final int baseMargin = 14;

	/**
	 * 模式,如果是1,表示是上一页下一页这种形式<br>
	 * 如果是2,则是百度页面下面的形式
	 */
	private int mode = 2;

	/**
	 * 分页对象按钮点击数据提交的网址
	 */
	private String url;

	/**
	 * 外边框的颜色
	 */
	private String borderColor = "#CCC";

	/**
	 * 每页显示的记录数<br>
	 * 默认是10
	 */
	private int pageSize = 10;

	/**
	 * 每页显示的记录的名字,也就是提交的字段名称
	 */
	private String pageSizeName = "pageSize";

	/**
	 * 当前页
	 */
	private int nowPage = 1;

	/**
	 * 当前页的名字
	 */
	private String nowPageName = "nowPage";

	/**
	 * 总记录数
	 */
	private int total;

	/**
	 * 第一页图标的地址
	 */
	private String firstIcon;

	/**
	 * 上一页的图标的地址
	 */
	private String preIcon;

	/**
	 * 下一页的图标的地址
	 */
	private String nextIcon;

	/**
	 * 最后一页的图标的地址
	 */
	private String lastIcon;

	/**
	 * 刷新的图标的地址
	 */
	private String refreshIcon;

	/**
	 * 竖直线条的高度
	 */
	private int verticalLineHeight = 24;

	/**
	 * 当前页之前显示的文字
	 */
	private String beforNowPageText = "第";

	/**
	 * 当前页之后显示的文字
	 */
	private String afterNowPageText = "页";

	/**
	 * 所有记录数目之前显示的文字
	 */
	private String beforTotalText = "总共";

	/**
	 * 所有记录数目之后显示的文字
	 */
	private String afterTotalText = "页";

	// 接下来是模仿百度的分页对象

	/**
	 * 数字的大小
	 */
	private int numberSize = 18;

	/**
	 * 分页对象数字显示的个数,<br>
	 * 页数不足的显示最多的页数,<br>
	 * 超过这个数量的显示这个数量的
	 */
	private int numberLength = 10;

	/**
	 * 上一页显示的文字
	 */
	private String prePageText = "<上一页";

	/**
	 * 下一页显示的文字
	 */
	private String nextPageText = "下一页>";

	/**
	 * 其他的参数,会自动把这个跟到网址后面,会自动判断这个是不是"&"开头,如果不是则加上一个
	 */
	private String otherParameter = "";

	@Override
	public int doStartTag() throws JspException {

		// 判断其他参数是不是合理
		if (!"".equals(otherParameter)) {
			if ('&' != otherParameter.charAt(0)) {
				otherParameter = "&" + otherParameter;
			}
		}

		// 计算出最后一页
		int lastPage = total % pageSize == 0 ? total / pageSize : total
				/ pageSize + 1;
		// System.out.println("lastPage==" + lastPage);

		if (url == null || "".equals(url)) {
			ServletRequest request = this.pageContext.getRequest();
			String path = request.getServletContext().getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path;
			url = basePath;
		} else {
			char c = url.charAt(url.length() - 1);
			if ('/' == c) {
				url = url.substring(0, url.length() - 1);
			}
		}

		JspWriter out = this.pageContext.getOut();
		try {

			out.print("<div style=\"border:1px solid " + borderColor + ";\" >" + //
					"<table><tr>");

			if (mode == 1) {

				// 如果设置了第一页的图标,那就显示第一页
				if (firstIcon != null) {
					out.print("<td><a href=\""
							+ url
							+ "?"//
							+ pageSizeName + "="
							+ pageSize
							+ "&"//
							+ nowPageName + "=1"
							+ otherParameter
							+ "\">"//
							+ "<img style=\"margin-left: " + baseMargin
							+ "px;\" alt=\"none\" src=\"" + firstIcon
							+ "\"></a></td>");
				}
				// 如果设置了上一页的图标,那就显示上一页
				if (preIcon != null && nowPage > 1) {
					out.print("<td><a href=\""
							+ url
							+ "?"//
							+ pageSizeName
							+ "="
							+ pageSize
							+ "&"//
							+ nowPageName + "="
							+ (nowPage - 1)
							+ otherParameter
							+ "\">"//
							+ "<img style=\"margin-left: " + baseMargin
							+ "px;\" alt=\"none\" src=\"" + preIcon
							+ "\"></a></td>");
				}

				// 竖直的线条
				out.print("<td><div style=\"margin-left: " + baseMargin
						+ "px;width:0px;height:" + verticalLineHeight
						+ "px;border:1px solid #CCC\"></div></td>");

				// 打印文字
				out.print("<td style=\"margin-left:" + baseMargin + "px;\">"
						+ beforNowPageText + "</td>");

				// 显示当前页的文本框
				out.print("<td>"
						+ //
						"<input style=\"width:2em;\" type=\"text\" value=\""
						+ nowPage + "\" name=\"" + nowPageName + "\" />" + //
						"</td>");

				out.print("<td style=\"margin-left:" + baseMargin + "px;\">"
						+ afterNowPageText + "   " + beforTotalText + ""
						+ lastPage + "" + afterTotalText + "</td>");

				// 竖直的线条
				out.print("<td><div style=\"margin-left: " + baseMargin
						+ "px;width:0px;height:" + verticalLineHeight
						+ "px;border:1px solid #CCC\"></div></td>");

				// 如果下一页的地址不为空,就显示下一页的图标
				if (nextIcon != null && nowPage < lastPage) {
					out.print("<td><a href=\""
							+ url
							+ "?"//
							+ pageSizeName
							+ "="
							+ pageSize
							+ "&"//
							+ nowPageName + "="
							+ (nowPage + 1)
							+ otherParameter
							+ "\">"//
							+ "<img style=\"margin-left: " + baseMargin
							+ "px;\" alt=\"none\" src=\"" + nextIcon
							+ "\"></a></td>");
				}

				// 如果最后一页的地址不为空,就显示最后一页的图标
				if (lastIcon != null) {
					out.print("<td><a href=\""
							+ url
							+ "?"//
							+ pageSizeName
							+ "="
							+ pageSize
							+ "&"//
							+ nowPageName + "="
							+ (lastPage)
							+ otherParameter
							+ "\">"//
							+ "<img style=\"margin-left: " + baseMargin
							+ "px;\" alt=\"none\" src=\"" + lastIcon
							+ "\"></a></td>");
				}

			} else { // 表示要使用百度页面的形式的分页效果

				if (nowPage > 1) { // 如果当前页是大于0的,说明当前页不是第一页,那么显示上一页的链接
					// 输出上一页的链接
					out.print("<td><a style=\"text-decoration:none;\" href=\""
							+ url
							+ "?"//
							+ pageSizeName
							+ "="
							+ pageSize
							+ "&"//
							+ nowPageName
							+ "="
							+ (nowPage - 1)
							+ otherParameter
							+ "\"><span style=\"margin-left: "
							+ baseMargin
							+ "px;border:1px solid #E1E2E3;color:blue;font-size: "
							+ numberSize + "px;\">&nbsp;" + prePageText
							+ "&nbsp;</span></a></td>");
				}

				String tmp;
				int start, end;
				start = nowPage - numberLength / 2;
				if (start < 1) {
					start = 1;
				}

				end = start + numberLength - 1;
				if (end > lastPage) {
					end = lastPage;
				}

				start = end - numberLength + 1;
				if (start < 1) {
					start = 1;
				}

				// System.out.println("start=" + start);
				// System.out.println("end=" + end);

				for (int i = start; i <= end; i++) {
					if (i == nowPage) {
						tmp = "black";
					} else {
						tmp = "blue";
					}
					out.print("<td><a style=\"text-decoration:none;\" href=\""
							+ url
							+ "?"//
							+ pageSizeName
							+ "="
							+ pageSize
							+ "&"//
							+ nowPageName + "=" + i + otherParameter
							+ "\"><span style=\"margin-left: " + baseMargin
							+ "px;border:1px solid #E1E2E3;color:" + tmp
							+ ";font-size: " + numberSize + "px;\">&nbsp;" + i
							+ "&nbsp;</span></a></td>");
				}

				// 如果当前页比最大页要小,就显示下一页
				if (nowPage < lastPage) {
					// 输出下一页的链接
					out.print("<td><a style=\"text-decoration:none;\" href=\""
							+ url
							+ "?"//
							+ pageSizeName
							+ "="
							+ pageSize
							+ "&"//
							+ nowPageName
							+ "="
							+ (nowPage + 1)
							+ otherParameter
							+ "\"><span style=\"margin-left: "
							+ baseMargin
							+ "px;border:1px solid #E1E2E3;color:blue;font-size: "
							+ numberSize + "px;\">&nbsp;" + nextPageText
							+ "&nbsp;</span></a></td>");
				}

			}

			// 竖直的线条
			out.print("<td><div style=\"margin-left: " + baseMargin
					+ "px;width:0px;height:" + verticalLineHeight
					+ "px;border:1px solid #CCC\"></div></td>");

			// 如果刷新的地址不为空,就显示刷新的图标
			if (refreshIcon != null) {
				out.print("<td><a href=\""
						+ url
						+ "?"//
						+ pageSizeName
						+ "="
						+ pageSize
						+ "&"//
						+ nowPageName + "="
						+ (nowPage)
						+ otherParameter
						+ "\">"//
						+ "<img style=\"margin-left: " + baseMargin
						+ "px;\" alt=\"none\" src=\"" + refreshIcon
						+ "\"></a></td>");
			}

			// 最后的收尾的标签
			out.print("</tr></table></div>");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return SKIP_BODY;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public String getFirstIcon() {
		return firstIcon;
	}

	public void setFirstIcon(String firstIcon) {
		this.firstIcon = firstIcon;
	}

	public String getPreIcon() {
		return preIcon;
	}

	public void setPreIcon(String preIcon) {
		this.preIcon = preIcon;
	}

	public String getNowPageName() {
		return nowPageName;
	}

	public void setNowPageName(String nowPageName) {
		this.nowPageName = nowPageName;
	}

	public String getNextIcon() {
		return nextIcon;
	}

	public void setNextIcon(String nextIcon) {
		this.nextIcon = nextIcon;
	}

	public String getLastIcon() {
		return lastIcon;
	}

	public void setLastIcon(String lastIcon) {
		this.lastIcon = lastIcon;
	}

	public int getVerticalLineHeight() {
		return verticalLineHeight;
	}

	public void setVerticalLineHeight(int verticalLineHeight) {
		this.verticalLineHeight = verticalLineHeight;
	}

	public String getRefreshIcon() {
		return refreshIcon;
	}

	public void setRefreshIcon(String refreshIcon) {
		this.refreshIcon = refreshIcon;
	}

	public String getBeforNowPageText() {
		return beforNowPageText;
	}

	public void setBeforNowPageText(String beforNowPageText) {
		this.beforNowPageText = beforNowPageText;
	}

	public String getAfterNowPageText() {
		return afterNowPageText;
	}

	public void setAfterNowPageText(String afterNowPageText) {
		this.afterNowPageText = afterNowPageText;
	}

	public String getBeforTotalText() {
		return beforTotalText;
	}

	public void setBeforTotalText(String beforTotalText) {
		this.beforTotalText = beforTotalText;
	}

	public String getAfterTotalText() {
		return afterTotalText;
	}

	public void setAfterTotalText(String afterTotalText) {
		this.afterTotalText = afterTotalText;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPageSizeName() {
		return pageSizeName;
	}

	public void setPageSizeName(String pageSizeName) {
		this.pageSizeName = pageSizeName;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getNumberSize() {
		return numberSize;
	}

	public void setNumberSize(int numberSize) {
		this.numberSize = numberSize;
	}

	public int getNumberLength() {
		return numberLength;
	}

	public void setNumberLength(int numberLength) {
		this.numberLength = numberLength;
	}

	public String getPrePageText() {
		return prePageText;
	}

	public void setPrePageText(String prePageText) {
		this.prePageText = prePageText;
	}

	public String getNextPageText() {
		return nextPageText;
	}

	public void setNextPageText(String nextPageText) {
		this.nextPageText = nextPageText;
	}

	public String getOtherParameter() {
		return otherParameter;
	}

	public void setOtherParameter(String otherParameter) {
		this.otherParameter = otherParameter;
	}
	
}
