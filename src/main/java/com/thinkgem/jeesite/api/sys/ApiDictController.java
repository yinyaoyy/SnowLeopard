/**
 * 
 */
package com.thinkgem.jeesite.api.sys;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.thinkgem.jeesite.api.cms.controllers.ApiAgencySearchController;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.form.DictForm;
import com.thinkgem.jeesite.api.dto.vo.DictVo;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 获取字典信息接口
 * @author 王鹏
 * @version 2018-04-18 15:15:31
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/api/100/510")
public class ApiDictController {

    private static final Logger log = LoggerFactory.getLogger(ApiAgencySearchController.class);

    /**
     * 根据key查询字典集合
     * @return
     */
	@RequestMapping("/10")
    public ServerResponse searchByType(BaseForm<DictForm> form){
    	ServerResponse result = null;
    	try {
    		form.initQueryObj(new TypeReference<DictForm>(){});
    		//根据字典key值获取字典内容
    		List<Dict> dicts = DictUtils.getOneDictList(form.getQueryObj().getKey());
    		List<DictVo> dvList = new ArrayList<DictVo>();
    		for (int i = 0; i < dicts.size(); i++) {
				dvList.add(new DictVo(dicts.get(i)));
			}
    		result = ServerResponse.createBySuccess(dvList);
    		log.debug("返回参数：{}",result);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
	  /**
     * 根据key查询子级字典集合
     * @return
     */
	@RequestMapping("/20")
    public ServerResponse searchByTypeAndParentID(BaseForm<DictForm> form){
    	ServerResponse result = null;
    	try {
    		form.initQueryObj(new TypeReference<DictForm>(){});
    		//根据字典key值获取字典内容
    		List<Dict> dicts = DictUtils.getChildrenDictList(form.getQueryObj().getKey(),form.getQueryObj().getParentId());
    		List<DictVo> dvList = new ArrayList<DictVo>();
    		for (int i = 0; i < dicts.size(); i++) {
				dvList.add(new DictVo(dicts.get(i)));
			}
    		result = ServerResponse.createBySuccess(dvList);
    		log.debug("返回参数：{}",result);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}",e.getMessage(),e);
			result = ServerResponse.createByError();
		}
        return result;
    }
}
