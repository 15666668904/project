package com.hxss.ACTION;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.hxss.SERVICE.ProjectSEREVICEimpl;
import com.hxss.SERVICE.ProjectSERVICE;
import com.hxss.UTIL.HibernateUtil;
import com.opensymphony.xwork2.ActionSupport;

public class Project_temp_Action extends ActionSupport{
	String prop_path = Thread.currentThread().getContextClassLoader().getResource("ftp.properties").getPath();
	Properties properties=new Properties();
	private String file_name;
	private String plan_version_sid;
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
	private String xpmobs_sid;
	public String project_IMPORT() {
		long start = System.currentTimeMillis();
		ProjectSERVICE projectSERVICE=new ProjectSEREVICEimpl();
		try {
			properties.load(new FileInputStream(prop_path));
			projectSERVICE.getfile(file_name);
			String localpath=properties.getProperty("local_path");
			File file=new File(localpath+"/"+file_name);
			projectSERVICE.savePlan_template(file, plan_version_sid,xpmobs_sid);
			projectSERVICE.savetemp_pro_taskpred(file, plan_version_sid);
			System.out.println("成功");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("出错");
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
