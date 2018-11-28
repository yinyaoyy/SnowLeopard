package com.thinkgem.jeesite.api.dto.vo.sys;

import java.util.List;

import com.thinkgem.jeesite.api.dto.form.BaseForm;
import com.thinkgem.jeesite.api.dto.vo.DictVo;
import com.thinkgem.jeesite.api.dto.vo.cms.ColumnVo;
import com.thinkgem.jeesite.modules.sys.entity.ServerSourceManage;

/**
 * 服务应用数据展示对象
 * @author kakasun
 * @create 2018-04-25 下午3:29
 */
public class ServerAppVo {
    String id;
    String name;		// 名称
    String logo;		// logo图片链接
    String serverType;		// 服务类别 一级服务：0；文章咨询：1；机构人员：2；
    String categoryId;//机构人员类别id
    List<ColumnVo> columnList;
    String pid;		// 父服务id
    String pname;  //父服务名称
    String link;		// 服务的外部连接
    Integer leve;		// 层级
    private String sourceId;		// 服务对应资源id
    private List<DictVo> DictList;//机构人员字典列表
    public ServerAppVo() {
    }

    public ServerAppVo(BaseForm form, ServerSourceManage serverSourceManage) {
        id = serverSourceManage.getId();
        name = serverSourceManage.getName();
        logo = serverSourceManage.getLogo();
        serverType = serverSourceManage.getServerType();
        if("2".equals(serverSourceManage.getServerType())){
            categoryId = serverSourceManage.getSourceId();
        }
        pid = serverSourceManage.getPid();
        pname = serverSourceManage.getPname();
        leve = serverSourceManage.getLeve();
        sourceId=serverSourceManage.getSourceId();
        if("100".equals(form.getTag())){
            link = serverSourceManage.getPcHerf();
        }else {
            link = serverSourceManage.getMobileHerf();
        }
        if("100".equals(form.getTag())){
        	logo = serverSourceManage.getLogo();
        }else {
        	logo= serverSourceManage.getMobileLogo();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<ColumnVo> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<ColumnVo> columnList) {
        this.columnList = columnList;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getLeve() {
        return leve;
    }

    public void setLeve(Integer leve) {
        this.leve = leve;
    }

    public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public List<DictVo> getDictList() {
		return DictList;
	}

	public void setDictList(List<DictVo> dictList) {
		DictList = dictList;
	}

	@Override
    public String toString() {
        return "ServerAppVo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", serverType='" + serverType + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", columnList=" + columnList +
                ", pid='" + pid + '\'' +
                ", link='" + link + '\'' +
                ", leve=" + leve +
                ", sourceId=" + sourceId +
                '}';
    }
}
