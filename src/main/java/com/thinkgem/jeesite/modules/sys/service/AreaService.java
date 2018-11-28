/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 区域Service
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {
	
	@Autowired
	AreaDao dao;
	
	public static final String CACHE_XM_AREA_LIST = "xmAreaList";
	public static final String CACHE_XM_AREA_SUB_LIST = "xmAreaSubList";
	public static final String aparentId = "0,1,4,";
	public static final String bparentId = "0,1,4,5,";
	
	public static final Map<String, List<Double>> AREA_ID_LONGITUDE_LATITUDE = new HashMap<String, List<Double>>(){
		private static final long serialVersionUID = 1L;
	{
		put("6", Arrays.asList(112.00067, 43.65105));//二连浩特市
		put("7", Arrays.asList(116.093659, 43.932312));//锡林浩特市
		put("8", Arrays.asList(114.950704, 44.029131));//阿巴嘎旗
		put("9", Arrays.asList(113.655237, 43.861936));//苏尼特左旗
		put("10", Arrays.asList(112.658873, 42.751101));//苏尼特右旗
		put("11", Arrays.asList(116.975204, 45.50207));//东乌珠穆沁旗
		put("12", Arrays.asList(117.639611, 44.586253));//西乌珠穆沁旗
		put("13", Arrays.asList(115.283877, 41.900898));//太仆寺旗
		put("14", Arrays.asList(113.854019, 42.238272));//镶黄旗
		put("15", Arrays.asList(114.900991, 42.215177));//正镶白旗
		put("16", Arrays.asList(115.998536, 42.247477));//正蓝旗
		put("17", Arrays.asList(116.491455, 42.212542));//多伦县
		put("2610eda5c9a94779a4fe815659f27282", Arrays.asList(118.849718, 45.716589));//乌拉盖管理区
	}};
	
	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}

	@Transactional(readOnly = false)
	public void save(Area area) {
		super.save(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
		CacheUtils.remove(CACHE_XM_AREA_LIST+area.getParentId());
		CacheUtils.remove(CACHE_XM_AREA_SUB_LIST+area.getParentId());
	}
	
	@Transactional(readOnly = false)
	public void delete(Area area) {
		super.delete(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
		CacheUtils.remove(CACHE_XM_AREA_LIST+area.getParentId());
		CacheUtils.remove(CACHE_XM_AREA_SUB_LIST+area.getParentId());
	}
	
	/**
	 * 根据上级id查询该id下的区域(含父节点)
	 * @author 王鹏
	 * @version 2018-04-22 17:49:29
	 * @param parentId
	 * @return
	 */
	public List<Area> findByParent(String parentId){
		 return findByParent(parentId, true);
	}
	/**
	 * 根据上级id查询该id下的区域
	 * @author 王鹏
	 * @version 2018-04-22 17:49:29
	 * @param parentId 父节点
	 * @param hasParent 是否包含父节点
	 * @return
	 */
	 @SuppressWarnings("unchecked")
	public List<Area> findByParent(String parentId, boolean hasParent){
		List<Area>  list=(List<Area>) CacheUtils.get(hasParent?CACHE_XM_AREA_LIST+parentId:CACHE_XM_AREA_SUB_LIST+parentId);
		 if(list==null){
			 list=Lists.newArrayList();
			 if(hasParent) {
				 Area  area=this.get(parentId);
				 list.add(area);
			 }
			 list.addAll(UserUtils.findByParent(parentId));
			 CacheUtils.put(hasParent?CACHE_XM_AREA_LIST+parentId:CACHE_XM_AREA_SUB_LIST+parentId, list);
		 }
		 return list;
	}
	 
	/**
	 * 根据地区id获取地区经纬度信息
	 * @author 王鹏
	 * @version 2018-06-11 16:47:23
	 * @param areaId
	 * @return
	 */
	public List<Double> getLongitudeLatitudeByAreaId(String areaId){
		return AREA_ID_LONGITUDE_LATITUDE.get(areaId);
	}
	
	/**
	 * 根据parent_ids查询锡林格勒盟及旗下区县
	 * @param aparentId
	 * @param bparentId
	 * @return
	 */
	public List<Area> findQxList(){
		return dao.findQxList(aparentId,bparentId);
	}
	
	
}
