package com.hxss.DAO;

import java.util.List;

import com.hxss.VO.EN_PLAN_CALENDAR;
import com.hxss.VO.EN_RESOURCES;
import com.hxss.VO.HXSS_FK;
import com.hxss.VO.HXSS_task_resources;
import com.hxss.VO.Plan_template;
import com.hxss.VO.hxss_task_ready;
import com.hxss.VO.noworking_day;
import com.hxss.VO.pro_obj;
import com.hxss.VO.pro_taskpred;

import net.sf.mpxj.Task;

public interface ProjectDAO {
	public void savepro_obj(pro_obj pro_obj);//插入任务
	public void savepro_taskpred(pro_taskpred pro_taskpred);//插入逻辑关系
	public void savehxss_task_resources(HXSS_task_resources hxss_task_resources );//插入资源分配
	public void saveEN_RESOURCES(EN_RESOURCES EN_RESOURCES);//插入资源
	public String getobj_sid(String task_id,String plan_version_sid);//查找指定任务在数据库中的sid
	public String getresources_sid(String resources_name,String xpmobs_sid);//查找指定资源在数据库的sid
	public List getresources(String xpmobs_sid);//取得已有资源避免重复导入
	public void deletecalendar(String xpmobs_sid);//删除全部日历
	public void saveEN_PLAN_CALENDAR(EN_PLAN_CALENDAR en_PLAN_CALENDAR);//保存日历
	public String getcalendar_sid(String calendar_name,String xpmobs_sid);//取得指定日历的sid
	public void savenoworking_day(noworking_day noworking_day);//保存例外日期
	public boolean noworkday(String day,String calendar_sid);//判断例外日期是否存在，避免重复导入
	public void setresources_calendar(String calendar_sid,String resources_sid);//设值资源日历
	public String gethuman_sid(String human_name,String dept_sid);//在本公司下搜索人员sid
	public String gethuman_sidfromxpm(String human_name);//在xpm_human表内搜索人员sid
	public void savefk_list(HXSS_FK hxss_FK);//保存齐套清单
	public void savetask_ready(hxss_task_ready hxss_task_ready );//保存任务准备
	public void deletepro_obj(String plan_version_sid);//删除任务
	//以下为公司层模板使用-------------------------------------------------------------------------------
	public void savePlan_template(Plan_template plan_template);
	public String gettemp_obj_sid(String task_id,String plan_version_sid);
	public void Update_project_delivery(String project_delivery,String xpmobs_sid);
}
