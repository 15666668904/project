package com.hxss.SERVICE;
import java.io.File;
import java.util.List;

import com.hxss.VO.EN_RESOURCES;
import com.hxss.VO.HXSS_task_resources;
import com.hxss.VO.pro_obj;
import com.hxss.VO.pro_taskpred;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mspdi.schema.Project;
public interface ProjectSERVICE {
	public void savepro_obj (File file,String plan_version_sid,String dept_sid,String Range);//插入任务
	public void savepro_taskpred(File file,String plan_version_sid);//插入逻辑关系
	public void savehxss_task_resources(File file,String plan_version_sid,String xpmobs_sid);//插入资源分配
	public void saveEN_RESOURCES(File file,String xpmobs_sid);//插入资源
	public void getfile(String file_name);//下载文件
	public void file_delete(String file_name);//删除文件
	public void deletecalendar(String xpmobs_sid);//删除日历
	public void saveEN_PLAN_CALENDAR(String xpmobs_sid,File file);//保存日历
	public void saveresources_CALENDAR(File file,String xpmobs_sid);//资源日历匹配
	public void savefk_list(File file,String plan_version_sid);//齐套清单匹配
	//------------------------------------------------------------------------------------------------
	public void savePlan_template(File file,String plan_version_sid,String xpmobs_sid);
	public void savetemp_pro_taskpred(File file,String plan_version_sid);
	public void Update_project_delivery(File file,String xpmobs_sid);//更新项目交期
	public String Data_validation(File file); 
	public void deletepro_obj(String plan_version_sid);
}
