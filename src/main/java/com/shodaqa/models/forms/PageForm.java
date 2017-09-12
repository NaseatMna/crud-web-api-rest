package com.shodaqa.models.forms;

import com.shodaqa.helpers.Page;
import org.hibernate.criterion.MatchMode;
/**
 * Created by Naseat_PC on 9/9/2017.
 */
public class PageForm {
    private Integer pageSize = 10;
    private Integer nowPage = 1;
    private String orderBy;
    private Page.OrderType orderType = Page.OrderType.asc;
    private Integer totalCount = 0;
    private Integer maxPage = 0;
    private String property;
    private String keyWords = "";
    private MatchMode matchMode = MatchMode.ANYWHERE;
    private String filter1 = "";
    private String filter2 = "";
    private String filter3 = "";
    private String filter4 = "";
    private String filter5 = "";

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getNowPage() {
        return nowPage;
    }

    public void setNowPage(Integer nowPage) {
        this.nowPage = nowPage;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Page.OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(Page.OrderType orderType) {
        this.orderType = orderType;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(Integer maxPage) {
        this.maxPage = maxPage;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public MatchMode getMatchMode() {
        return matchMode;
    }

    public void setMatchMode(MatchMode matchMode) {
        this.matchMode = matchMode;
    }

    public String getFilter1() {
        return filter1;
    }

    public void setFilter1(String filter1) {
        this.filter1 = filter1;
    }

    public String getFilter2() {
        return filter2;
    }

    public void setFilter2(String filter2) {
        this.filter2 = filter2;
    }

    public String getFilter3() {
        return filter3;
    }

    public void setFilter3(String filter3) {
        this.filter3 = filter3;
    }

    public String getFilter4() {
        return filter4;
    }

    public void setFilter4(String filter4) {
        this.filter4 = filter4;
    }

    public String getFilter5() {
        return filter5;
    }

    public void setFilter5(String filter5) {
        this.filter5 = filter5;
    }
}
