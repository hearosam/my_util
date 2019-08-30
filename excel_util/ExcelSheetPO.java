package cn.pdc.base.core.utils;

import java.util.List;

/**
 * <h1>定义表格的数据对象</h1>
 * @Author: sam
 * @Date: 17:46 2019/8/16
 */
public class ExcelSheetPO {
    /**
     * sheet的名称
     */
    private String sheetName;
    /**
     * 表格标题
     */
    private String title;
    /**
     * 头部标题集合
     */
    private String[] headers;
    /**
     * 数据集合
     */
    private List<List<Object>> dataList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public List<List<Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<List<Object>> dataList) {
        this.dataList = dataList;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }



}