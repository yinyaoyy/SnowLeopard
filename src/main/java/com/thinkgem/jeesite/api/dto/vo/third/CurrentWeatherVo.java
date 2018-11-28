package com.thinkgem.jeesite.api.dto.vo.third;

/**
 * 和风天气basic
 *
 * @author kakasun
 * @create 2018-04-22 上午10:19
 */
public class CurrentWeatherVo {
    String cid;//城市id
    String location;// 	地区／城市名称 	海淀
    String parent_city;// 	该地区／城市的上级城市 	北京
    String admin_area;// 	该地区／城市所属行政区域 	北京
    String cnty;// 	该地区／城市所属国家名称 	中国
    String tz;// 	该地区／城市所在时区 	+8.0

    String fl;// 	体感温度，默认单位：摄氏度 	23
    String tmp;//  	温度，默认单位：摄氏度 	21
    String cond_code;//  	实况天气状况代码 	100
    String cond_txt;//  	实况天气状况代码 	晴
    String cond_icon;//天气图标
    String wind_deg;//  	风向360角度 	305
    String wind_dir;//  	风向 	西北
    String wind_sc;//  	风力 	3
    String wind_spd;//  	风速，公里/小时 	15
    String hum;//  	相对湿度 	40
    String pcpn;//  	降水量 	0
    String pres;//  	大气压强 	1020
    String vis;//  	能见度，默认单位：公里 	10
    String cloud;//  	云量 	23

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getParent_city() {
        return parent_city;
    }

    public void setParent_city(String parent_city) {
        this.parent_city = parent_city;
    }

    public String getAdmin_area() {
        return admin_area;
    }

    public void setAdmin_area(String admin_area) {
        this.admin_area = admin_area;
    }

    public String getCnty() {
        return cnty;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getCond_code() {
        return cond_code;
    }

    public void setCond_code(String cond_code) {
        this.cond_code = cond_code;
    }

    public String getCond_txt() {
        return cond_txt;
    }

    public void setCond_txt(String cond_txt) {
        this.cond_txt = cond_txt;
    }

    public String getCond_icon() {
        return cond_icon;
    }

    public void setCond_icon(String cond_icon) {
        this.cond_icon = cond_icon;
    }

    public String getWind_deg() {
        return wind_deg;
    }

    public void setWind_deg(String wind_deg) {
        this.wind_deg = wind_deg;
    }

    public String getWind_dir() {
        return wind_dir;
    }

    public void setWind_dir(String wind_dir) {
        this.wind_dir = wind_dir;
    }

    public String getWind_sc() {
        return wind_sc;
    }

    public void setWind_sc(String wind_sc) {
        this.wind_sc = wind_sc;
    }

    public String getWind_spd() {
        return wind_spd;
    }

    public void setWind_spd(String wind_spd) {
        this.wind_spd = wind_spd;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getPcpn() {
        return pcpn;
    }

    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
    }

    public String getPres() {
        return pres;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public String getVis() {
        return vis;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    @Override
    public String toString() {
        return "CurrentWeatherVo{" +
                "cid='" + cid + '\'' +
                ", location='" + location + '\'' +
                ", parent_city='" + parent_city + '\'' +
                ", admin_area='" + admin_area + '\'' +
                ", cnty='" + cnty + '\'' +
                ", tz='" + tz + '\'' +
                ", fl='" + fl + '\'' +
                ", tmp='" + tmp + '\'' +
                ", cond_code='" + cond_code + '\'' + "\n" +
                ", cond_txt='" + cond_txt + '\'' +
                ", cond_icon='" + cond_icon + '\'' +
                ", wind_deg='" + wind_deg + '\'' +
                ", wind_dir='" + wind_dir + '\'' +
                ", wind_sc='" + wind_sc + '\'' +
                ", wind_spd='" + wind_spd + '\'' +
                ", hum='" + hum + '\'' +
                ", pcpn='" + pcpn + '\'' +
                ", pres='" + pres + '\'' +
                ", vis='" + vis + '\'' +
                ", cloud='" + cloud + '\'' +
                '}';
    }
}
