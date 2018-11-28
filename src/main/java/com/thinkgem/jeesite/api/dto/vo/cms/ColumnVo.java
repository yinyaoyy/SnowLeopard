package com.thinkgem.jeesite.api.dto.vo.cms;

import com.thinkgem.jeesite.modules.cms.entity.Category;

import java.util.Objects;

/**
 * 栏目
 * @author kakasun
 * @create 2018-04-17 上午9:58
 */
public class ColumnVo {

    String id;
    /**
     * 所有父级编号
     */
    String parentIds;
    /**
     * 名称
     */
    String name;
    /**
     * 归属站点
     */
    String siteId;
 
    /**
     * 点击量
     */
    String hits;
    String inMenu;
    public ColumnVo(){}

    public ColumnVo(Category category){
        this.id = category.getId();
        this.parentIds = category.getParentIds();
        this.name = category.getName();
        this.siteId = category.getSite().getId();
        this.hits = category.getHits();
        this.inMenu=category.getInMenu();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getinMenu() {
		return inMenu;
	}

	public void setinMenu(String inMenu) {
		this.inMenu = inMenu;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnVo columnVo = (ColumnVo) o;
        return Objects.equals(id, columnVo.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ColumnVo{" +
                "id='" + id + '\'' +
                ", siteId='" + siteId + '\'' +
                ", name='" + name + '\'' +
                ", hits='" + hits + '\'' +
                  ", inMenu='" + inMenu + '\'' +
                '}';
    }
}
