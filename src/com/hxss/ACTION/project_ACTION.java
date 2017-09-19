package com.hxss.ACTION;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.struts2.ServletActionContext;

import com.hxss.SERVICE.ProjectSEREVICEimpl;
import com.hxss.SERVICE.ProjectSERVICE;
import com.hxss.UTIL.HibernateUtil;
import com.opensymphony.xwork2.ActionSupport;
public class project_ACTION extends ActionSupport{
	String prop_path = Thread.currentThread().getContextClassLoader().getResource("ftp.properties").getPath();
	Properties properties=new Properties();
	private String file_name;
	private String plan_version_sid;
	private String xpmobs_sid;
	private String calendar_flag;
	private String dept_sid;
	private String Range="1";//联系人搜索范围，默认在本公司查找 2位在xpm_human表内搜索，不分公司
	private String project_date_flag;
	public String getProject_date_flag() {
		return project_date_flag;
	}
	public void setProject_date_flag(String project_date_flag) {
		this.project_date_flag = project_date_flag;
	}
	public String getRange() {
		return Range;
	}
	public void setRange(String range) {
		Range = range;
	}
	public String getDept_sid() {
		return dept_sid;
	}
	public void setDept_sid(String dept_sid) {
		this.dept_sid = dept_sid;
	}
	public String getProp_path() {
		return prop_path;
	}
	public void setProp_path(String prop_path) {
		this.prop_path = prop_path;
	}
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	public String getCalendar_flag() {
		return calendar_flag;
	}
	public void setCalendar_flag(String calendar_flag) {
		this.calendar_flag = calendar_flag;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getPlan_version_sid() {
		return plan_version_sid;
	}
	public void setPlan_version_sid(String plan_version_sid) {
		this.plan_version_sid = plan_version_sid;
	}
	public String getXpmobs_sid() {
		return xpmobs_sid;
	}
	public void setXpmobs_sid(String xpmobs_sid) {
		this.xpmobs_sid = xpmobs_sid;
	}
	public String project_IMPORT() {
		long start = System.currentTimeMillis();
		ProjectSERVICE projectSERVICE=new ProjectSEREVICEimpl();
		try {
			properties.load(new FileInputStream(prop_path));
			projectSERVICE.getfile(file_name);
			String localpath=properties.getProperty("local_path");
			File file=new File(localpath+"/"+file_name);
			String Data_validation_results=projectSERVICE.Data_validation(file,plan_version_sid);
			if(!Data_validation_results.equals("success")) {
				ServletActionContext.getRequest()
				.setAttribute("Data_validation_results", Data_validation_results);
				return "Data_validation_error";
			}
			projectSERVICE.deletepro_obj(plan_version_sid);
			projectSERVICE.savepro_obj(file, plan_version_sid,dept_sid,Range);
			projectSERVICE.savepro_taskpred(file, plan_version_sid);
			projectSERVICE.saveEN_RESOURCES(file, xpmobs_sid);
			projectSERVICE.savehxss_task_resources(file, plan_version_sid,xpmobs_sid);
			//projectSERVICE.savefk_list(file, plan_version_sid);
			if (calendar_flag.equals("1")){
				projectSERVICE.deletecalendar(xpmobs_sid);
				projectSERVICE.saveEN_PLAN_CALENDAR(xpmobs_sid, file);
				projectSERVICE.saveresources_CALENDAR(file, xpmobs_sid);
			}
			if(project_date_flag.equals("1")) {
				projectSERVICE.Update_project_delivery(file, xpmobs_sid);
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
			// TODO: handle exception
		}finally {
			projectSERVICE.file_delete(file_name);
			HibernateUtil.closeSession();
			long end = System.currentTimeMillis();
			System.out.println("耗时  "+(end-start)+"毫秒");
		}

	}
}
