package com.thinkgem.jeesite.api.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.api.cms.services.ApiSiteService;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.form.sys.ServerAppIdForm;
import com.thinkgem.jeesite.api.dto.vo.DictVo;
import com.thinkgem.jeesite.api.dto.vo.cms.ColumnVo;
import com.thinkgem.jeesite.api.dto.vo.sys.ServerAppVo;
import com.thinkgem.jeesite.api.dto.vo.sys.VersionManagerVo;
import com.thinkgem.jeesite.common.utils.FileUtil;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.constant.OfficeRoleConstant;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.sys.dao.ServerSourceManageDao;
import com.thinkgem.jeesite.modules.sys.entity.NodeManager;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.ServerSourceManage;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.VersionManager;
import com.thinkgem.jeesite.modules.sys.service.NodeManagerService;
import com.thinkgem.jeesite.modules.sys.service.VersionManagerService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 服务相关api接口
 *
 * @author kakasun
 * @create 2018-04-25 下午3:16
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/api/100/8030")
public class ApiServerAppController {

    @Autowired
    ServerSourceManageDao serverSourceManageDao;

    @Autowired
    ApiSiteService siteService;
    @Autowired
    NodeManagerService nodeManagerService;
    @Autowired
    VersionManagerService versionManagerService;
    
    /**
     * 获取一级服务
     *
     * @return
     */
    @RequestMapping("/10")
    public ServerResponse getRootServerApp(BaseForm form,HttpServletRequest request) {
        ServerSourceManage serverSourceManage = new ServerSourceManage();
        //设置查询条件
        setSearchCondition(request.getHeader("tag"), serverSourceManage, "");
        List<ServerSourceManage> serverSourceManageList = serverSourceManageDao.findList(serverSourceManage);
        List<ServerAppVo> body = Lists.newArrayList();
        form.setTag(request.getHeader("tag"));
        ServerAppVo serverAppVo = null;
        for (ServerSourceManage ssm : serverSourceManageList) {
            serverAppVo = new ServerAppVo(form, ssm);
            body.add(serverAppVo);
        }
        return ServerResponse.createBySuccess(body);
    }

    /**
     * 获取服务的所有兄弟服务
     *
     * @return
     */
    @RequestMapping("/20")
    public ServerResponse getSiblingsServerApp(BaseForm<ServerAppIdForm> form,HttpServletRequest request) {
        form.initQueryObj(new TypeReference<ServerAppIdForm>() {});
        ServerSourceManage currentServer = serverSourceManageDao.get(form.getQueryObj().getServerId());
        ServerSourceManage serverSourceManage = new ServerSourceManage();
        //设置查询条件
        setSearchCondition(request.getHeader("tag"), serverSourceManage, currentServer.getPid());
        form.setTag(request.getHeader("tag"));
        List<ServerSourceManage> serverSourceManageList = serverSourceManageDao.findList(serverSourceManage);
        List<ServerAppVo> body = Lists.newArrayList();
        ServerAppVo serverAppVo = null;
        for (ServerSourceManage ssm : serverSourceManageList) {
            serverAppVo = getServerAppVo(form, ssm);
            body.add(serverAppVo);
        }
        return ServerResponse.createBySuccess(body);
    }

    /**
     * 获取服务的子服务
     *
     * @return
     */
    @RequestMapping("/30")
    public ServerResponse getChildrenServerApp(BaseForm<ServerAppIdForm> form,HttpServletRequest request) {
        form.initQueryObj(new TypeReference<ServerAppIdForm>() {});
        ServerSourceManage serverSourceManage = new ServerSourceManage();
        //设置查询条件
        setSearchCondition(request.getHeader("tag"), serverSourceManage, form.getQueryObj().getServerId());
        form.setTag(request.getHeader("tag"));
        List<ServerSourceManage> serverSourceManageList = serverSourceManageDao.findList(serverSourceManage);
        List<ServerAppVo> body = Lists.newArrayList();
        ServerAppVo serverAppVo = null;
        for (ServerSourceManage ssm : serverSourceManageList) {
            serverAppVo = getServerAppVo(form, ssm);
            body.add(serverAppVo);
        }
        return ServerResponse.createBySuccess(body);
    }

    /**
     * 获取所有的机构人员服务
     *
     * @return
     */
    @RequestMapping("/40")
    public ServerResponse getAgencyServerApp(BaseForm form,HttpServletRequest request) {
        ServerSourceManage serverSourceManage = new ServerSourceManage();
        setSearchCondition(request.getHeader("tag"), serverSourceManage, null);
        form.setTag(request.getHeader("tag"));
        serverSourceManage.setServerType("2");
        serverSourceManage.setHomeShow("1");
        serverSourceManage.setPcShow(null);
        List<ServerSourceManage> serverSourceManageList = serverSourceManageDao.findList(serverSourceManage);
        List<ServerAppVo> body = Lists.newArrayList();
        ServerAppVo serverAppVo = null;
        for (ServerSourceManage ssm : serverSourceManageList) {
            serverAppVo = getServerAppVo(form, ssm);
            body.add(serverAppVo);
        }
        return ServerResponse.createBySuccess(body);
    }


    /**
     * 获取所有第二级服务
     *
     * @return
     */
    @RequestMapping("/50")
    public ServerResponse getSecondLevelServerApp(BaseForm form,HttpServletRequest request) {
        ServerSourceManage serverSourceManage = new ServerSourceManage();
        setSearchCondition(request.getHeader("tag"), serverSourceManage, null);
        form.setTag(request.getHeader("tag"));
        serverSourceManage.setLeve(2);
        List<ServerSourceManage> serverSourceManageList = serverSourceManageDao.findList(serverSourceManage);
        List<ServerAppVo> body = Lists.newArrayList();
        ServerAppVo serverAppVo = null;
        for (ServerSourceManage ssm : serverSourceManageList) {
            serverAppVo = getServerAppVo(form, ssm);
            body.add(serverAppVo);
        }
        return ServerResponse.createBySuccess(body);
    }
    
    /**
     * 通过条件获取业务表单对应的数据
     *
     * @return
     */
    @RequestMapping("/60")
    public ServerResponse getNodePage(BaseForm<NodeManager> form) {
    	form.initQueryObj(new TypeReference<NodeManager>(){});
    	NodeManager nodeManager=form.getQueryObj();
    	nodeManager.setDelFlag("0");
    	if(StringUtils.isBlank(nodeManager.getVersion())){
    		nodeManager.setVersion(nodeManagerService.findNewVersion(nodeManager));
    	}
    	List<NodeManager> list=nodeManagerService.findList(nodeManager);
        return ServerResponse.createBySuccess(list);
    }
    /**
     * 生成serverApp
     * @param form
     * @param ssm
     * @return
     */
    private ServerAppVo getServerAppVo(BaseForm form, ServerSourceManage ssm) {
        ServerAppVo serverAppVo;
        List<ColumnVo> columnVoList;
        serverAppVo = new ServerAppVo(form, ssm);
        if ("1".equals(ssm.getServerType())) {
            columnVoList = siteService.getAllColumnsBySiteId(ssm.getSourceId());
            serverAppVo.setColumnList(columnVoList);
        }
        return serverAppVo;
    }


    /**
     * 根据请求来源配置选择对应显示的服务应用
     *
     * @param form
     * @param serverSourceManage
     * @param 服务上级id
     */
    private void setSearchCondition(String tag, ServerSourceManage serverSourceManage, String pid) {
        switch (tag) {
            case "100":
                serverSourceManage.setPcShow("1");
                break;
            case "400":
                serverSourceManage.setBigdataShow("1");
                break;
            default:
                serverSourceManage.setMobileShow("1");
        }
        serverSourceManage.setPid(pid);
        //获取用户
		User user = UserUtils.getUser();
		List<Role> roleList = Lists.newArrayList();
		if(StringUtils.isBlank(user.getId())) {//若用户没用登录则默认设置角色为“普通用户”
			roleList.add(new Role(OfficeRoleConstant.ROLE_DEFAULT_NORMAL));
		}
		else {//若已登录则获取当前用户角色
			roleList = user.getRoleList();
		}
		serverSourceManage.setRoleList(roleList);
    }
    /**
     * 通过id获取服务详细信息
     *
     * @return
     */
    @RequestMapping("/70")
    public ServerResponse getServerById(BaseForm<ServerAppIdForm> form,HttpServletRequest request) {
        form.initQueryObj(new TypeReference<ServerAppIdForm>() {});
        ServerSourceManage serverSourceManage = serverSourceManageDao.get(form.getQueryObj().getServerId());
        ServerAppVo body = getServerAppVo(form,serverSourceManage);
        String sourceId=body.getSourceId();
        if(StringUtils.isNoneBlank(sourceId)&&"2".equals(body.getServerType())){
        	String[]param=sourceId.split(",");
        	List<DictVo> list=Lists.newArrayList();
        	for (String pa : param) {
        		list.add(new DictVo(DictUtils.getDictByTypeValue("sys_office_category",pa)));
			}
        	body.setDictList(list);
        }
        return ServerResponse.createBySuccess(body);
    }
    /**
     * 获取所有移动首页第二级服务
     *
     * @return
     */
    @RequestMapping("/80")
    public ServerResponse getMobileSecondLevelServerApp(BaseForm form,HttpServletRequest request) {
        ServerSourceManage serverSourceManage = new ServerSourceManage();
        setSearchCondition(request.getHeader("tag"), serverSourceManage, null);
        form.setTag(request.getHeader("tag"));
        serverSourceManage.setLeve(2);
        serverSourceManage.setMobileHomeShow("1");
        serverSourceManage.setMobileShow(null);
        List<ServerSourceManage> serverSourceManageList = serverSourceManageDao.findList(serverSourceManage);
        List<ServerAppVo> body = Lists.newArrayList();
        ServerAppVo serverAppVo = null;
        for (ServerSourceManage ssm : serverSourceManageList) {
            serverAppVo = getServerAppVo(form, ssm);
            body.add(serverAppVo);
        }
        return ServerResponse.createBySuccess(body);
    }
    /**
     * 获取系统版本
     *
     * @return
     */
    @RequestMapping("/90")
    public ServerResponse getMobileVersionApp(BaseForm form,HttpServletRequest request,HttpServletResponse response) {
    	VersionManager versionManager = new VersionManager();
    	String tag=request.getHeader("tag");
    	versionManager.setRepresent(tag);
    	List<VersionManager> versionManagerList = versionManagerService.findList(versionManager);
    	VersionManagerVo versionManagervo=new VersionManagerVo();
    	if(versionManagerList!=null&&versionManagerList.size()>0){
    		versionManagervo=new VersionManagerVo(versionManagerList.get(0),tag);
    	}
    	/*long fileLength= FileUtil.getFileLength("/userfiles/1/files/sys/versionManager/2018/06/文本.txt")
    	 * ServerResponse serverResonse=null;
    	 * if(fileLength!=0){
    		serverResonse = ServerResponse.createBySuccess(versionManagervo);
    		fileLength+=serverResonse.toString().length();
    	}else{
    		serverResonse = ServerResponse.createByError();
    		fileLength+=serverResonse.toString().length();
    		response.setContentLength(fileLength)
    	}*/

    	return ServerResponse.createBySuccess(versionManagervo);
    }
}
