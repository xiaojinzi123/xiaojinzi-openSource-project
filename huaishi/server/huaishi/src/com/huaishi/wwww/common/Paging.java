package com.huaishi.wwww.common;

import java.util.List;

/**
 * 分页对象,当前页不会超过两边界
 * 
 * @dateTime 2015年11月21日 下午2:02:51
 * @Company xjzCompany
 * @author xiaojinzi
 */
public class Paging<T> {

	// 当前页
	private long nowPage;

	// 全部的页数
	private long allPage;

	// 每页的记录数
	private int pageSize;

	// 总记录数
	private long total;

	// 从数据库中取出的开始下标
	private long startIndex;

	// 分页里面存放当前页的集合
	private List<T> rows;

	/*
	 * 以下是模仿百度的分页效果的数据定义和计算
	 */

	// 起始页
	private long startPage;

	// 结束页
	private long endPage;

	// 分页显示的页数
	private long pageCount;

	/**
	 * 创建这个分页工具类只要传入每页的记录数和总记录数即可即可
	 * 
	 * @param pageSize
	 */
	public Paging(int pageSize, long total) {
		super();

		if (pageSize < 1) {
			pageSize = 1;
		}

		if (total < 0) {
			total = 0;
		}

		this.pageSize = pageSize;
		this.total = total;

		// 调用计算的方法
		this.fresh();
	}

	/**
	 * 用于重新计算各个数据
	 */
	private void fresh() {
		// 根据每页的记录数计算总页数
		if (this.pageSize > 0) {
			if (this.total % this.pageSize == 0) {
				this.allPage = this.total / this.pageSize;
			} else {
				this.allPage = this.total / this.pageSize + 1;
			}
		}
		// 判断当前页是否已经超过了1到总页数的范围，并相应的进行调整
		if (this.nowPage < 1) {
			this.nowPage = 1;
		}
		if (this.nowPage > this.allPage) {
			this.nowPage = this.allPage;
		}
		// 计算数据库中从下标为多少开始的
		this.startIndex = (this.nowPage - 1) * this.pageSize;

		/*
		 * 以下计算模仿百度的分页的数据的计算
		 */

		// 如果全部的页数都没有要显示的页数，例子 9<10 ,则全部显示
		if (this.allPage <= this.pageCount) {
			this.startPage = 1;
			this.endPage = this.allPage;
		} else {
			// 如果全部的页数大于要显示的页数 21>10

			this.startPage = this.nowPage - ((this.pageCount + 1) - (this.pageCount + 1) % 2) / 2;

			// 如果计算出的起始页小于1，则让它变成1
			if (this.startPage < 1) {
				this.startPage = 1;
			}

			// 计算出结束页数
			this.endPage = this.startPage + this.pageCount - 1;

			// 如果最后的页数大于全部的页数，则让它变成最后一页
			if (this.endPage > this.allPage) {
				this.endPage = this.allPage;
			}

			this.startPage = this.endPage - this.pageCount + 1;

		}

	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		// 调用计算的方法
		this.fresh();
	}

	/**
	 * 设置总记录数目
	 * 
	 * @param total
	 */
	public void setTotal(long total) {
		this.total = total;
		// 调用计算的方法
		this.fresh();
	}

	/**
	 * 返回是否还有下一页
	 * 
	 * @return
	 */
	public boolean hasNext() {
		return this.nowPage < this.allPage;
	}

	/**
	 * 返回是否还有前一个
	 * 
	 * @return
	 */
	public boolean hasPrevious() {
		return this.nowPage > 1;
	}

	/**
	 * 返回下一页的页数
	 * 
	 * @return
	 */
	public long nextPage() {
		if (this.nowPage < this.allPage) {
			this.nowPage++;
			this.fresh();
			return this.nowPage;
		}

		this.fresh();

		return this.allPage;
	}

	/**
	 * 返回上一页的页数
	 * 
	 * @return
	 */
	public long PreviousPage() {
		if (this.nowPage > 1) {
			this.nowPage--;
			this.fresh();
			return this.nowPage;
		}

		this.fresh();

		return 1;
	}

	/**
	 * 设置当前页
	 * 
	 * @param nowPage
	 */
	public void setNowPage(long nowPage) {
		this.nowPage = nowPage;
		// 调用计算的方法
		this.fresh();
	}

	public long getNowPage() {
		return nowPage;
	}

	public long getAllPage() {
		return allPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public long getTotal() {
		return total;
	}

	public long getStartIndex() {
		return startIndex;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public long getPageCount() {
		return pageCount;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
		// 调用计算的方法
		this.fresh();
	}

	public long getStartPage() {
		return startPage;
	}

	public long getEndPage() {
		return endPage;
	}

	@Override
	public String toString() {
		return "Paging [nowPage=" + nowPage + ", allPage=" + allPage + ", pageSize=" + pageSize + ", total=" + total
				+ ", startIndex=" + startIndex + ", rows=" + rows + ", startPage=" + startPage + ", endPage=" + endPage
				+ ", pageCount=" + pageCount + "]";
	}

}
