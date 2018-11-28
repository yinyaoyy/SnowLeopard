package com.thinkgem.jeesite.api.dto.vo;

import com.thinkgem.jeesite.modules.appmange.entity.AppMenu;

/**
 * 返回给页面的移动应用对象
 *
 * @author kakasun
 * @create 2018-04-20 上午9:24
 */
public class AppMenuVo {

    String id;
    String appType;        // 应用类型
    String title;        // 名称
    String icon;        // 图标链接
    String href;        // 应用的请求链接
    String target;        // 目标（ mainFrame、_blank、_self、_parent、_top）

    public AppMenuVo() {
    }
    public AppMenuVo(AppMenu appMenu) {
        this.id = appMenu.getId();
        this.appType = appMenu.getAppType();
        this.title = appMenu.getTitle();
        this.icon = appMenu.getIcon();
        this.href = appMenu.getHref();
        this.target = appMenu.getTarget();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "AppMenuVo{" +
                "id='" + id + '\'' +
                ", appType='" + appType + '\'' +
                ", title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", href='" + href + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
