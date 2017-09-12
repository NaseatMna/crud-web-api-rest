package com.shodaqa.helpers;

import org.hibernate.criterion.MatchMode;

import java.util.List;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.criterion.MatchMode;

import java.util.List;
/**
 * Created by Naseat_PC on 9/9/2017.
 */
public class Page<T> {
    private static final Integer MAX_PAGE_SIZE = 2000;
    private List<T> list;
    private Integer pageSize = 10;
    private Integer nowPage = 1;
    @JSONField(serialize = false)
    private String orderBy;
    @JSONField(serialize = false)
    private OrderType orderType = OrderType.asc;
    private Integer totalCount = 0;
    private Integer maxPage = 0;
    @JSONField(serialize = false)
    private String property;
    @JSONField(serialize = false)
    private String keyWords;
    @JSONField(serialize = false)
    private MatchMode matchMode = MatchMode.ANYWHERE;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize < 1 ? 1 : pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : pageSize;
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

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public boolean hasNext() {
        return totalCount > pageSize * nowPage;
    }

    public Integer getMaxPage() {
        this.maxPage = this.totalCount / this.pageSize;
        return this.totalCount % this.pageSize > 0 ? ++this.maxPage : this.maxPage;
    }

    public void setMaxPage(Integer maxPage) {
        this.maxPage = maxPage;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
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

    public enum OrderType {
        asc, desc
    }
}
