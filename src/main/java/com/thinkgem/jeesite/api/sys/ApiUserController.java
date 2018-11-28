package com.thinkgem.jeesite.api.sys;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.UserInfoVo;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.utils.FileUtil;
import com.thinkgem.jeesite.common.web.ServerResponse;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 用户API接口
 * @author wanglin
 * @create 2018-04-19 上午9:03
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/api")
public class ApiUserController {
	
	private static final Logger log = LoggerFactory.getLogger(ApiUserController.class);
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;
	/**
	 * 图片文件上传
	 * @return
	 */
	@RequestMapping(value={"/200/700/10","/100/700/10"})
	public ServerResponse uploadImg(MultipartFile fileData,HttpServletRequest req) throws IOException {
		ServerResponse<String> result =null;
			int zijie = (int) fileData.getSize();
			if(zijie < 209715200){
				User user=UserUtils.getUser();
				String fileName = fileData.getOriginalFilename();
				String fileType = "";
				int ind = fileName.lastIndexOf('.');
				if(ind > 0){
					fileType = fileName.substring(ind);
				}
				fileName=IdGen.uuid() + fileType;
				String path=FileUtil.uploadImgFileReturnPath(fileData,fileName);
				user.setPhoto(path);
				systemService.updatePhotoById(user);
				result=ServerResponse.createBySuccess(user.getPhoto());
			}else{
				result=ServerResponse.createByErrorCodeMessage(-2,"上传图片过大");
			}
		return result;
	}
	/**
	 * 文件上传
	 * @return
	 */
	@RequestMapping( value={"/200/700/20","/100/700/20"})
	public ServerResponse uploadFile(MultipartFile file,HttpServletRequest req) throws IOException {
		ServerResponse<String> result =null;
		if(file==null) {
			result = ServerResponse.createByErrorCodeMessage(ResponseCode.INVALID_PARAMETER);
		}
		else {
			int zijie = (int) file.getSize();
			if(zijie < 209715200){
				String fileName = file.getOriginalFilename();
				/*String fileType = "";
				int ind = fileName.lastIndexOf('.');
				if(ind > 0){
					fileType = fileName.substring(ind);
				}
				fileName=IdGen.uuid() + fileType;*/
				String path=FileUtil.uploadFileFileReturnPath(file,fileName);
				result=ServerResponse.createBySuccess(path);
			}else{
				result=ServerResponse.createByErrorCodeMessage(-2,"上传文件过大");
			}
		}
		return result;
	}
	/**
	 * 用户是否存在
	 * @return
	 */
	@RequestMapping(value={"/200/700/30","/100/700/30"})
	public ServerResponse userISNow()  {
		  User user=UserUtils.getUser();
		  ServerResponse<String>	result=null;
		  if(user!=null&&user.getId()!=null&&!"".equals(user.getId())){
			  result  =ServerResponse.createBySuccess("0");
		  }else{
			  result  =ServerResponse.createBySuccess("-1");
		  }
		 return result;
	}
	 /**
     * api获取当前登录用户信息
     *
     * @return
     */
    @RequestMapping(value={"/200/400/40","/100/700/40"} )
    public ServerResponse userInfo() {
        User u = UserUtils.getUser();
        ServerResponse<UserInfoVo> result = null;
        if(null == u|| null == u.getId()||"".equals(u.getId())){
            ResponseCode code = ResponseCode.NEED_LOGIN;
            result = ServerResponse.createByErrorCodeMessage(code.getCode(),code.getDesc());
            return result;
        }
        result = ServerResponse.createBySuccess(new UserInfoVo(u));
        return result;
    }
    
    /**
     * api根据区域和类型，级别选择机构
     *
     * @return
     */
	@RequestMapping("/100/700/50")
    public ServerResponse officeInfo(HttpServletRequest request, BaseForm<Office> form) {
    	ServerResponse result = null;
		try {
			form.setTag(request.getHeader("tag"));
			form.initQueryObj(Office.class);
			List<Office> list = officeService.findIdName(form.getQueryObj());
			result =ServerResponse.createBySuccess(list);
			log.debug("返回参数：{}", result);
		} catch (Exception e) {
			log.error("查询异常:[{}],请求参数无,详细信息:\n{}", e.getMessage(), e);
			result = ServerResponse.createByError();
		}
		return result;
    }
      //    用户信息修改
	 @RequestMapping( value={"/200/700/60","/100/700/60"})
	    public ServerResponse userUpdatet(HttpServletRequest request, BaseForm<User> form) {
	        User user=JSONObject.parseObject(form.getQuery(), User.class);
	       // System.out.println(user.toString());
	        ServerResponse result = null;
	        systemService.UpdateUserExpand(user);
	        result = ServerResponse.createBySuccess();
	        return result;
	    }
    /**
	 * 校验参数是否正确
	 * 
	 * @author 王鹏
	 * @version 2018-05-14 17:09:13
	 * @return
	 */
	public String checkParam(Office object) {
		StringBuffer sb = new StringBuffer();
		try {
			BeanValidators.validateWithException(validator, object);
		} catch (ConstraintViolationException ex) {
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			for (int i = 0; i < list.size(); i++) {
				sb.append(list.get(i));
			}
		}
		return sb.toString();
	}

}
