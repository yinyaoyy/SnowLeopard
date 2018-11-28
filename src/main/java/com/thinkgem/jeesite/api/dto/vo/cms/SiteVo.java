package com.thinkgem.jeesite.api.dto.vo.cms;

import com.thinkgem.jeesite.modules.cms.entity.Site;

import java.util.Objects;

/**
 * 门户一级栏目，也就是站点
 * @author kakasun
 * @create 2018-04-17 上午10:04
 */
public class SiteVo {
    /**
     * 站点id
     */
    String id;
    /**
     * 站点图片链接
     */
    String logo;
    /**
     * 站点标题
     */
    String title;
    /**
     * 站点域名
     */
    String domain;

    public SiteVo() {}

    public SiteVo(Site site) {
        this.id = site.getId();
        this.logo = site.getLogo();
        this.title = site.getTitle();
        this.domain = site.getDomain();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiteVo siteVo = (SiteVo) o;
        return Objects.equals(id, siteVo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SiteVo{" +
                "id='" + id + '\'' +
                ", logo='" + logo + '\'' +
                ", title='" + title + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }
}
