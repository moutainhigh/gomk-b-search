package io.gomk.common.utils;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class PageResult<T> implements Serializable {

	private static final long serialVersionUID = -4071521319254024213L;

	private Integer page = 1;// 要查找第几页
	private Integer pageSize = 20;// 每页显示多少条
	private Integer totalPage = 0;// 总页数
	private Long totalRows = 0L;// 总记录数
	private List<T> rows;// 结果集

	public PageResult() {
	}

    public PageResult(int page, int pageSize, long totalRows, List<T> rows ) {
        this.setRows(rows);
        this.setTotalRows(totalRows);
        this.setPage(page);
        this.setPageSize(pageSize);
        this.setTotalPage((int)totalRows/pageSize + 1);
    }

}