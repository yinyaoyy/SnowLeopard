package com.thinkgem.jeesite.api.dto.vo.third;

/**
 * 城市名称
 *
 * @author kakasun
 * @create 2018-04-20 下午3:36
 */
public class CityNameVo {
    /**
     * 城市id
     */
    String cid;
    /**
     * 城市名称
     */
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    @Override
    public String toString() {
        return "CityNameVo{" +
                "cid='" + cid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
