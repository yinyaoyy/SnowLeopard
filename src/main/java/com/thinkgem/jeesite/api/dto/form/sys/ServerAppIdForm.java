package com.thinkgem.jeesite.api.dto.form.sys;

/**
 * 服务的id
 *
 * @author kakasun
 * @create 2018-04-25 下午3:52
 */
public class ServerAppIdForm {
    String serverId;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    @Override
    public String toString() {
        return "ServerAppIdForm{" +
                "serverId='" + serverId + '\'' +
                '}';
    }
}
