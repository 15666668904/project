package com.hxss.SERVICE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;



import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.taglibs.standard.tag.common.xml.WhenTag;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.XMLOutputter;

import com.hxss.DAO.ProjectDAO;
import com.hxss.DAO.ProjectDAOimpl;
import com.hxss.FILEIO.readFile;
import com.hxss.PROJECT.project_importDemo;
import com.hxss.VO.EN_PLAN_CALENDAR;
import com.hxss.VO.EN_RESOURCES;
import com.hxss.VO.HXSS_FK;
import com.hxss.VO.HXSS_task_resources;
import com.hxss.VO.Plan_template;
import com.hxss.VO.hxss_task_ready;
import com.hxss.VO.noworking_day;
import com.hxss.VO.pro_obj;
import com.hxss.VO.pro_taskpred;

import net.sf.mpxj.ProjectCalendar;
import net.sf.mpxj.ProjectCalendarException;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Relation;
import net.sf.mpxj.RelationType;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;
import net.sf.mpxj.mpx.MPXWriter;
import net.sf.mpxj.mspdi.schema.Project;
import net.sf.mpxj.mspdi.schema.Project.Resources;
import net.sf.mpxj.planner.schema.Days;
public class ProjectSEREVICEimpl implements ProjectSERVICE{
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void savepro_obj(File file,String plan_version_sid,String dept_sid,String Range)  {
		// TODO Auto-generated method stub
		ProjectDAO projectDAO=new ProjectDAOimpl();
		project_importDemo project_importDemo=new project_importDemo();
		List list=project_importDemo.psrseProjectFile(project_importDemo.getprojectfile(file));
		for(int i=0;i<list.size();i++){
			HashMap project=(HashMap) list.get(i);
			pro_obj pro_obj=new pro_obj();
			pro_obj.setObj_code(String.valueOf(project.get("id")));
			pro_obj.setRemark((String) project.get("fk_type"));
			pro_obj.setTask_period(String.valueOf(project.get("task_period")));
			pro_obj.setObj_name(String.valueOf(project.get("taskName")));
			pro_obj.setTask_status("（NS）未开始");
			pro_obj.setPf(String.valueOf(formatter.format(project.get("endDate"))));
			pro_obj.setPs(String.valueOf(formatter.format(project.get("startDate"))));
			pro_obj.setPlan_version_sid(plan_version_sid);
			pro_obj.setObj_type("TASK");
			//判断齐套点
			if(String.valueOf(project.get("taskName")).indexOf("齐套")!=-1){
				pro_obj.setIs_fk("齐套");
				if(String.valueOf(project.get("taskName")).indexOf("项目齐套")!=-1) {
					pro_obj.setFk_type("项目层次");
				}else {
					pro_obj.setFk_type("单元层次");
				}
				pro_obj.setTask_type("任务作业");
				pro_obj.setTask_period("0");
				if(null!=project.get("manager")||project.get("manager")!=""){
					if(Range.equals("1")){
						pro_obj.setFk_manager_sid(projectDAO.gethuman_sid(String.valueOf(project.get("manager")), dept_sid));
						if(null!=projectDAO.gethuman_sid(String.valueOf(project.get("manager")), dept_sid)){
							pro_obj.setFk_manager(String.valueOf(project.get("manager")));
						}
					}
					if(Range.equals("2")){
						pro_obj.setFk_manager_sid(projectDAO.gethuman_sidfromxpm(String.valueOf(project.get("manager"))));
						if(null!=projectDAO.gethuman_sidfromxpm(String.valueOf(project.get("manager")))){
							pro_obj.setFk_manager(String.valueOf(project.get("manager")));
						}
					}
				}
			}
			//判断里程碑
			else if(String.valueOf(project.get("taskName")).indexOf("里程碑")!=-1){
				pro_obj.setTask_type("完成里程碑");
				if(null!=project.get("manager")||project.get("manager")!=""){
					if(Range.equals("1")){
						pro_obj.setTask_manager_sid(projectDAO.gethuman_sid(String.valueOf(project.get("manager")), dept_sid));
						if(null!=projectDAO.gethuman_sid(String.valueOf(project.get("manager")), dept_sid)){
							pro_obj.setTask_manager(String.valueOf(project.get("manager")));
						}
					}
					if(Range.equals("2")){
						pro_obj.setTask_manager_sid(projectDAO.gethuman_sidfromxpm(String.valueOf(project.get("manager"))));
						if(null!=projectDAO.gethuman_sidfromxpm(String.valueOf(project.get("manager")))){
							pro_obj.setTask_manager(String.valueOf(project.get("manager")));
						}
					}
				}
			}
			else {
				pro_obj.setIs_fk("一般");
				if(null!=project.get("manager")||project.get("manager")!=""){
					if(Range.equals("1")){
						pro_obj.setTask_manager_sid(projectDAO.gethuman_sid(String.valueOf(project.get("manager")), dept_sid));
						if(null!=projectDAO.gethuman_sid(String.valueOf(project.get("manager")), dept_sid)){
							pro_obj.setTask_manager(String.valueOf(project.get("manager")));
						}
					}
					if(Range.equals("2")){
						pro_obj.setTask_manager_sid(projectDAO.gethuman_sidfromxpm(String.valueOf(project.get("manager"))));
						if(null!=projectDAO.gethuman_sidfromxpm(String.valueOf(project.get("manager")))){
							pro_obj.setTask_manager(String.valueOf(project.get("manager")));
						}
					}
				}
			}
			projectDAO.savepro_obj(pro_obj);
		}
	}

	@Override
	public void savepro_taskpred(File file,String plan_version_sid) {
		// TODO Auto-generated method stub
		project_importDemo project_importDemo=new project_importDemo();
		List task_list=project_importDemo.psrseProjectFile(project_importDemo.getprojectfile(file));
		String before_task_id="";
		for (int i = 0; i < task_list.size(); i++) {
			HashMap task=(HashMap) task_list.get(i);
			ProjectDAO projectDAO=new ProjectDAOimpl();
			before_task_id=(String) task.get("beforeTask");
			int j=0;
			while(j<100){
				if (before_task_id.length()==0){
					break;
				}
				if (before_task_id.indexOf(";")!=before_task_id.length()){
					if (before_task_id.equals(";")) {
						break;
					}
					pro_taskpred pro_taskpred=new pro_taskpred();
					if (projectDAO.getobj_sid(before_task_id.substring(0,before_task_id.indexOf(";")),plan_version_sid)!="") {
						pro_taskpred.setTask_sid(projectDAO.getobj_sid(task.get("id").toString(),plan_version_sid));
						pro_taskpred.setPred_task_sid(projectDAO.getobj_sid(before_task_id.substring(0,before_task_id.indexOf(";")),plan_version_sid));
						before_task_id=before_task_id.substring(before_task_id.indexOf(";")+1,before_task_id.length());
						projectDAO.savepro_taskpred(pro_taskpred);
					}
				}else{
					break;
				}
				j++;
			}
		}
	}

	@Override
	public void savehxss_task_resources(File file,String plan_version_sid,String xpmobs_sid) {
		// TODO Auto-generated method stub
		project_importDemo project_importDemo=new project_importDemo();
		ProjectDAO projectDAO=new ProjectDAOimpl();
		HXSS_task_resources hxss_task_resources=new HXSS_task_resources();
		List<Task> tList=project_importDemo.getprojectfile(file).getAllTasks();
		for (int i = 1; i < tList.size(); i++) {
			Task task=tList.get(i);
			List rList=task.getResourceAssignments();//取得任务的资源分配
			if (!rList.isEmpty()){
				for (int j = 0; j < rList.size(); j++) {
					ResourceAssignment resourceAssignment=(ResourceAssignment) rList.get(j);
					Resource resource=resourceAssignment.getResource();
					hxss_task_resources .setObj_sid(projectDAO.getobj_sid(String.valueOf(task.getID()), plan_version_sid));
					hxss_task_resources.setResources_sid(projectDAO.getresources_sid(resource.getName(), xpmobs_sid));
					hxss_task_resources.setXpmobs_sid(xpmobs_sid);
					hxss_task_resources.setReq_quantity(String.valueOf(resourceAssignment.getUnits().intValue()/100));
					projectDAO.savehxss_task_resources(hxss_task_resources);
				}
			}
		}
	}

	@Override
	public void saveEN_RESOURCES(File file,String xpmobs_sid) {
		// TODO Auto-generated method stub
		project_importDemo project_importDemo=new project_importDemo();
		List resources_list= project_importDemo.getresources(project_importDemo.getprojectfile(file));
		ProjectDAO projectDAO=new ProjectDAOimpl();
		List proj_resources=projectDAO.getresources(xpmobs_sid);//取得项目已有资源
		for (int i = 1; i < resources_list.size(); i++) {
			Resource resource=(Resource) resources_list.get(i);
			EN_RESOURCES en_RESOURCES=new EN_RESOURCES();
			en_RESOURCES.setXpmobs_sid(xpmobs_sid);
			en_RESOURCES.setIs_active("1");
			en_RESOURCES.setMax_supply(String.valueOf(resource.getMaxUnits().intValue()/100));
			en_RESOURCES.setResources_code(resource.getID().toString());
			en_RESOURCES.setResources_name(resource.getName());
			en_RESOURCES.setRegisterHuman_sid("Project");//标记project导入的资源以便后续进行处理
			//避免重复导入
			if (proj_resources.isEmpty()) {
				projectDAO.saveEN_RESOURCES(en_RESOURCES);
			}
			else if(!proj_resources.contains(resource.getName())) {
				projectDAO.saveEN_RESOURCES(en_RESOURCES);
			}
		}
	}

	@Override
	public void getfile(String file_name) {
		// TODO Auto-generated method stub
		readFile readFile=new readFile();
		readFile.downloadFile(file_name);
	}

	@Override
	public void file_delete(String file_name) {
		// TODO Auto-generated method stub
		readFile readFile=new readFile();
		readFile.file_delete(file_name);
		readFile.removeftp_file(file_name);
	}

	@Override
	public void saveEN_PLAN_CALENDAR(String xpmobs_sid, File file) {
		// TODO Auto-generated method stub
		project_importDemo project_importDemo=new project_importDemo();
		EN_PLAN_CALENDAR en_PLAN_CALENDAR=new EN_PLAN_CALENDAR();
		int[] working_day= project_importDemo.getworkingday(project_importDemo.getprojectfile(file));//取得标准日历周工作日
		for (int i = 0; i < working_day.length; i++) {
			switch (i) {
			case 0:
				en_PLAN_CALENDAR.setDay_7(String.valueOf(working_day[i]));
				break;

			case 1:
				en_PLAN_CALENDAR.setDay_1(String.valueOf(working_day[i]));
				break;

			case 2:
				en_PLAN_CALENDAR.setDay_2(String.valueOf(working_day[i]));
				break;

			case 3:
				en_PLAN_CALENDAR.setDay_3(String.valueOf(working_day[i]));
				break;

			case 4:
				en_PLAN_CALENDAR.setDay_4(String.valueOf(working_day[i]));
				break;

			case 5:
				en_PLAN_CALENDAR.setDay_5(String.valueOf(working_day[i]));
				break;

			case 6:
				en_PLAN_CALENDAR.setDay_6(String.valueOf(working_day[i]));
				break;
			}
		}
		ProjectDAO projectDAO=new ProjectDAOimpl();
		en_PLAN_CALENDAR.setXpmobs_sid(xpmobs_sid);
		en_PLAN_CALENDAR.setCalendar_name(project_importDemo.getprojectfile(file).getCalendar().getName());
		en_PLAN_CALENDAR.setCalendar_code(String.valueOf(project_importDemo.getprojectfile(file).getCalendar().getUniqueID()));
		en_PLAN_CALENDAR.setCalendar_deafult_version("1");
		projectDAO.saveEN_PLAN_CALENDAR(en_PLAN_CALENDAR);
		List CalendarExceptions= project_importDemo.getCalendarExceptions(project_importDemo.getprojectfile(file));
		ProjectSEREVICEimpl projectSEREVICEimpl=new ProjectSEREVICEimpl();
		noworking_day noworking_day=new noworking_day();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String calendar_sid=projectDAO.getcalendar_sid(project_importDemo.getprojectfile(file).getCalendar().getName(), xpmobs_sid);
		for (int i = 0; i < CalendarExceptions.size(); i++) {
			ProjectCalendarException projectCalendarException=(ProjectCalendarException) CalendarExceptions.get(i);
			List datelist=projectSEREVICEimpl.getBetweenDates(projectCalendarException.getFromDate(),projectCalendarException.getToDate());
			if (projectCalendarException.getWorking()) {
				for (int j = 0; j < datelist.size(); j++) {
					if(projectDAO.noworkday(formatter.format(datelist.get(j)), calendar_sid)){
						noworking_day.setCalendar_sid(projectDAO.getcalendar_sid(project_importDemo.getprojectfile(file).getCalendar().getName(), xpmobs_sid));
						noworking_day.setFlag("1");
						noworking_day.setNonworking_day(formatter.format(datelist.get(j)));
						projectDAO.savenoworking_day(noworking_day);
					}
				}
			}
			if(!projectCalendarException.getWorking()){
				for (int j = 0; j < datelist.size(); j++) {
					if(projectDAO.noworkday(formatter.format(datelist.get(j)), calendar_sid)){
						noworking_day.setCalendar_sid(projectDAO.getcalendar_sid(project_importDemo.getprojectfile(file).getCalendar().getName(), xpmobs_sid));
						noworking_day.setFlag("0");
						noworking_day.setNonworking_day(formatter.format(datelist.get(j)));
						projectDAO.savenoworking_day(noworking_day);
					}
				}
			}
		}
	}

	@Override
	public void saveresources_CALENDAR(File file, String xpmobs_sid) {
		// TODO Auto-generated method stub
		//只要基准日历不是项目日历就导入
		//在项目日历基础上扩展也要导入
		String CALENDAR_sid=null;
		project_importDemo project_importDemo=new project_importDemo();
		ProjectDAO projectDAO=new ProjectDAOimpl();
		EN_PLAN_CALENDAR en_PLAN_CALENDAR=new EN_PLAN_CALENDAR();
		List list= project_importDemo.getprojectfile(file).getAllResources();
		for (int i = 0; i < list.size(); i++) {
			Resource resource=(Resource) list.get(i);
			ProjectCalendar projectCalendar=resource.getResourceCalendar();
			String	BaseCalendar=projectCalendar.getBaseCalendar().getName();
			String proj_calendar=project_importDemo.getprojectfile(file).getCalendar().getName();
			if((!BaseCalendar.equals(proj_calendar))||(BaseCalendar.equals(proj_calendar)&!projectCalendar.getCalendarExceptions().isEmpty())){
				en_PLAN_CALENDAR.setCalendar_deafult_version("0");
				en_PLAN_CALENDAR.setCalendar_name(projectCalendar.getName()+"日历");
				en_PLAN_CALENDAR.setCalendar_code("R"+projectCalendar.getUniqueID());
				en_PLAN_CALENDAR.setXpmobs_sid(xpmobs_sid);
				int[] working_day=projectCalendar.getBaseCalendar().getDays();
				for (int j = 0; j < working_day.length; j++) {
					switch (j) {
					case 0:
						en_PLAN_CALENDAR.setDay_7(String.valueOf(working_day[j]));
						break;
					case 1:
						en_PLAN_CALENDAR.setDay_1(String.valueOf(working_day[j]));
						break;
					case 2:
						en_PLAN_CALENDAR.setDay_2(String.valueOf(working_day[j]));
						break;
					case 3:
						en_PLAN_CALENDAR.setDay_3(String.valueOf(working_day[j]));
						break;
					case 4:
						en_PLAN_CALENDAR.setDay_4(String.valueOf(working_day[j]));
						break;
					case 5:
						en_PLAN_CALENDAR.setDay_5(String.valueOf(working_day[j]));
						break;
					case 6:
						en_PLAN_CALENDAR.setDay_6(String.valueOf(working_day[j]));
						break;
					}
				}
				projectDAO.saveEN_PLAN_CALENDAR(en_PLAN_CALENDAR);
				CALENDAR_sid=projectDAO.getcalendar_sid(projectCalendar.getName()+"日历", xpmobs_sid);
				noworking_day noworking_day=new noworking_day();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				List CalendarExceptions=projectCalendar.getCalendarExceptions();
				for (int j = 0; j < CalendarExceptions.size(); j++) {
					ProjectCalendarException projectCalendarException=(ProjectCalendarException) CalendarExceptions.get(j);
					List day=getBetweenDates(projectCalendarException.getFromDate(), projectCalendarException.getToDate());
					for(int k=0;k<day.size();k++){
						noworking_day.setCalendar_sid(CALENDAR_sid);
						if(projectCalendarException.getWorking()){
							noworking_day.setFlag("1");
						}else {
							noworking_day.setFlag("0");
						}
						noworking_day.setNonworking_day(formatter.format(day.get(k)));
						projectDAO.savenoworking_day(noworking_day);
					}
				}
				//基准日历的例外日期
				for(int x=0;x<projectCalendar.getBaseCalendar().getCalendarExceptions().size();x++){
					ProjectCalendarException projectCalendarException=projectCalendar.getBaseCalendar().getCalendarExceptions().get(x);
					for (int y = 0; y <getBetweenDates(projectCalendarException.getFromDate(), projectCalendarException.getToDate()).size(); y++) {
						if (projectCalendarException.getWorking()) {
							noworking_day.setFlag("1");
						}else{
							noworking_day.setFlag("0");
						}
						noworking_day.setCalendar_sid(CALENDAR_sid);
						noworking_day.setNonworking_day(formatter.format(getBetweenDates(projectCalendarException.getFromDate(), projectCalendarException.getToDate()).get(y)));
						projectDAO.savenoworking_day(noworking_day);
					}
				}
				String resources_sid=projectDAO.getresources_sid(projectCalendar.getName(), xpmobs_sid);
				projectDAO.setresources_calendar(CALENDAR_sid,resources_sid);
			}
		}
	}

	@Override
	public void deletecalendar(String xpmobs_sid) {
		// TODO Auto-generated method stub
		ProjectDAO projectDAO=new ProjectDAOimpl();
		projectDAO.deletecalendar(xpmobs_sid);
	}
	//取得两个日期之间的所有日期
	private List<Date>  getBetweenDates(Date start, Date end) {
		List<Date> result = new ArrayList<Date>();
		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(start);
		while(start.getTime()<=end.getTime()){
			result.add(tempStart.getTime());
			tempStart.add(Calendar.DAY_OF_YEAR, 1);
			start = tempStart.getTime();
		}
		return result;
	}

	@Override
	public void savefk_list(File file, String plan_version_sid) {
		// TODO Auto-generated method stub
		ProjectDAO projectDAO=new ProjectDAOimpl();
		project_importDemo project_importDemo=new project_importDemo();
		List fk_list=project_importDemo.fk_list(project_importDemo.getprojectfile(file));
		if(!fk_list.isEmpty()){
			for(int i=0;i<fk_list.size();i++){
				HashMap hashMap=(HashMap) fk_list.get(i);
				Set set=hashMap.entrySet();
				Iterator it=set.iterator();
				while(it.hasNext()){
					Map.Entry me=(Map.Entry)it.next();
					String task_id= me.getKey().toString();
					String task_name= project_importDemo.getprojectfile(file).getTaskByID(Integer.parseInt(task_id)).getName();
					if(task_name.indexOf("齐套")!=-1){
						savehxss_fk(projectDAO.getobj_sid(task_id, plan_version_sid), String.valueOf(i), hashMap.get(Integer.parseInt(task_id)).toString());
					}else {
						savetask_ready(projectDAO.getobj_sid(task_id, plan_version_sid), String.valueOf(i), hashMap.get(Integer.parseInt(task_id)).toString());
					}
				}
			}
		}
	}
	private void savehxss_fk(String obj_sid,String code,String name){
		HXSS_FK hxss_FK=new HXSS_FK();
		ProjectDAO projectDAO=new ProjectDAOimpl();
		hxss_FK.setObj_sid(obj_sid);
		hxss_FK.setFk_code(code);
		hxss_FK.setFk_name(name);
		projectDAO.savefk_list(hxss_FK);
	}
	private void savetask_ready(String obj_sid,String code,String name){
		hxss_task_ready hxss_task_ready=new hxss_task_ready();
		ProjectDAO projectDAO=new ProjectDAOimpl();
		hxss_task_ready.setObj_sid(obj_sid);
		hxss_task_ready.setTask_ready_item(name);
		projectDAO.savetask_ready(hxss_task_ready);
	}

	@Override
	public void savePlan_template(File file, String plan_version_sid,String xpmobs_sid) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		ProjectDAO projectDAO=new ProjectDAOimpl();
		project_importDemo project_importDemo=new project_importDemo();
		List list=project_importDemo.psrseProjectFile(project_importDemo.getprojectfile(file));
		for(int i=0;i<list.size();i++){
			HashMap project=(HashMap) list.get(i);
			Plan_template plan_template=new Plan_template();
			plan_template.setObj_code(String.valueOf(project.get("id")));
			plan_template.setTask_period(String.valueOf(project.get("task_period")));
			plan_template.setObj_name(String.valueOf(project.get("taskName")));
			plan_template.setTask_status("（NS）未开始");
			plan_template.setXpmobs_sid(xpmobs_sid);
			plan_template.setPf(String.valueOf(formatter.format(project.get("endDate"))));
			plan_template.setPs(String.valueOf(formatter.format(project.get("startDate"))));
			plan_template.setPlan_version_sid(plan_version_sid);
			plan_template.setObj_type("TASK");
			//判断齐套点
			if(String.valueOf(project.get("taskName")).indexOf("齐套")!=-1){
				plan_template.setIs_fk("齐套");
				plan_template.setFk_type("单元层次");
				plan_template.setTask_type("任务作业");
				plan_template.setTask_period("0");
			}
			//判断里程碑
			else if(String.valueOf(project.get("taskName")).indexOf("里程碑")!=-1){
				plan_template.setTask_type("完成里程碑");
			}
			else {
				plan_template.setIs_fk("一般");
			}
			projectDAO.savePlan_template(plan_template);;
		}
	}

	@Override
	public void savetemp_pro_taskpred(File file, String plan_version_sid) {
		// TODO Auto-generated method stub
		project_importDemo project_importDemo=new project_importDemo();
		List task_list=project_importDemo.psrseProjectFile(project_importDemo.getprojectfile(file));
		String before_task_id="";
		for (int i = 0; i < task_list.size(); i++) {
			HashMap task=(HashMap) task_list.get(i);
			ProjectDAO projectDAO=new ProjectDAOimpl();
			before_task_id=(String) task.get("beforeTask");
			int j=0;
			while(j<100){
				if (before_task_id.length()==0){
					break;
				}
				if (before_task_id.indexOf(";")!=before_task_id.length()){
					if (before_task_id.equals(";")) {
						break;
					}
					pro_taskpred pro_taskpred=new pro_taskpred();
					if (projectDAO.gettemp_obj_sid(before_task_id.substring(0,before_task_id.indexOf(";")),plan_version_sid)!="") {
						pro_taskpred.setTask_sid(projectDAO.gettemp_obj_sid(task.get("id").toString(),plan_version_sid));
						pro_taskpred.setPred_task_sid(projectDAO.gettemp_obj_sid(before_task_id.substring(0,before_task_id.indexOf(";")),plan_version_sid));
						before_task_id=before_task_id.substring(before_task_id.indexOf(";")+1,before_task_id.length());
						projectDAO.savepro_taskpred(pro_taskpred);
					}
				}else{
					break;
				}
				j++;
			}
		}
	}

	@Override
	public void Update_project_delivery(File file, String xpmobs_sid) {
		// TODO Auto-generated method stub
		ProjectDAO projectDAO=new ProjectDAOimpl();
		projectDAO.Update_project_delivery(new SimpleDateFormat("yyyy-MM-dd").format(new project_importDemo()
				.getprojectfile(file).getFinishDate()), xpmobs_sid);
	}

	@Override
	//验证逻辑关系（FS）、齐套任务不允许有前置
	public String Data_validation(File file,String plan_version_sid)  {
		// TODO Auto-generated method stub
		String result="success";
		ProjectDAO projectDAO=new ProjectDAOimpl();
		boolean default_version= projectDAO.getdefault_plan(plan_version_sid);
		project_importDemo project_importDemo=new project_importDemo();
		List<Task>tasks=project_importDemo.getprojectfile(file).getAllTasks();
		for(int i=tasks.size()-1;i>=0;i--) {
			Task task=tasks.get(i);
			if(null==task.getSuccessors()&&task.getName().indexOf("齐套")!=-1) {
				result="检测到齐套任务["+task.getID()+"]"+task.getName()+"没有后续任务";
				break;
			}
			List<Relation> relations= task.getPredecessors();
			if(null!=relations) {
				for(int j=0;j<relations.size();j++) {
					Relation relation=relations.get(j);
					if(!relation.getType().equals(RelationType.FINISH_START)) {
						result="检测到["+task.getID()+"]"+task.getName()+" 逻辑关系包含非FS关系";
						break;
					}
					if(task.getName().indexOf("齐套")!=-1) {
						result="检测到齐套任务["+task.getID()+"]"+task.getName()+"包含前置逻辑关系";
						break;
					}
				}
			}
		}
		if(!default_version) {
			if(result.equals("success")) {
				result="计划执行中<br/>";
			}else {
				result="计划执行中<br/>"+result;
			}
		}
		return result;
	}

	@Override
	public void deletepro_obj(String plan_version_sid) {
		// TODO Auto-generated method stub
		ProjectDAO projectDAO=new ProjectDAOimpl();
		projectDAO.deletepro_obj(plan_version_sid);
	}
}
