package com.thinkgem.jeesite.api.appmange;

import com.thinkgem.jeesite.api.dto.vo.AppMenuVo;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.appmange.service.AppMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 根据用户个性化返回界面
 * @author kakasun
 * @create 2018-04-20 上午8:45
 */
@RestController
@RequestMapping("/api/100/800")
public class ApiAppMenuController {

    @Autowired
    AppMenuService menuService;

    /**
     * 加载登录用户的对应权限的菜单
     * 未登录权限为普通大众
     * @return
     */
    @RequestMapping("/10")
    public ServerResponse loadMenu(){
        List<AppMenuVo> appMenuVoList = menuService.loadMenu();
        ServerResponse<List<AppMenuVo>> result = ServerResponse.createBySuccess(appMenuVoList);
        return result;
    }
}
