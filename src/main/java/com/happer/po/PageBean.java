package com.happer.po;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/11.
 */
public class PageBean implements Serializable {
    private int curPage;//当前是第几页
    private int maxPage;//最大页
    private long maxRowCount;//最大行数==总行数
    private int rowsPerPage;// 每页多少行
    private List<Article> data;//每页的数据

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public long getMaxRowCount() {
        return maxRowCount;
    }

    public void setMaxRowCount(long maxRowCount) {
        this.maxRowCount = maxRowCount;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public List<Article> getData() {
        return data;
    }

    public void setData(List<Article> data) {
        this.data = data;
    }
}
