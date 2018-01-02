package com.hxss.PROJECT;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.mpxj.Day;
import net.sf.mpxj.MPXJException;
import net.sf.mpxj.ProjectCalendar;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Relation;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.reader.ProjectReader;
public class project_importDemo {
	/*
	 * 注意project取任务从1开始
	 * */
	//获取projectfile
	public static ProjectFile getprojectfile(File file){
		MPPReader mppReader=new MPPReader();
		try {
			ProjectFile	 projectFile =mppReader.read(file);
			return projectFile;
		} catch (MPXJException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	//获取前置任务id
	private static String getBeforeTaskId(Task task) {
		StringBuffer beforeTaskId = new StringBuffer();
		if(task!=null){
			List list = task.getPredecessors();

			if(list != null ){
				if(list.size()>0){
					for(int i=0; i<list.size(); i++){
						Relation relation = (Relation)list.get(i);
						beforeTaskId.append(relation.getTargetTask().getID()+";");
					}
				}
			}
		}
		return beforeTaskId.toString();
	}
	//获取资源
	private static String getResources(Task task){
		if(task == null){
			return "";
		}
		StringBuffer sb=new StringBuffer();
		List re=task.getResourceAssignments();
		for(int i=0;i<re.size();i++){
			ResourceAssignment resourceAssignment=(ResourceAssignment) re.get(i);
			net.sf.mpxj.Resource resources=resourceAssignment.getResource();
			if (resources!=null) {
				sb.append(resources.getName()+"  utits:"+resourceAssignment.getUnits());
			}
		}
		return sb.toString();
	}
	//解析project
	@SuppressWarnings("unchecked")
	public static List psrseProjectFile(ProjectFile projectFile) {
		List list = new ArrayList();
		List taskList = projectFile.getAllTasks();
		for(int i=0;i<taskList.size();i++){
			Task task=(Task) taskList.get(i);
			//去节点
			if (task.getChildTasks().isEmpty()){
				Map map=new HashMap();
				map.put("id",task.getID()>1000?task.getID():1000+task.getID());
				map.put("fk_type", task.getText1());
				map.put("task_period", Integer.parseInt(new java.text.DecimalFormat("0").format(task.getDuration().getDuration())));
				map.put("taskName",task.getName());
				map.put("startDate",task.getStart());
				map.put("endDate",task.getFinish());
				map.put("beforeTask",getBeforeTaskId(task));//获取前置任务的Id
				map.put("resource",getResources(task));//获得资源
				map.put("manager", task.getContact());//取得联系人  对应xpm中的任务经理
				list.add(map);
			}
		}
		return list;
	}
	//取得资源
	public static List getresources(ProjectFile projectFile){
		return	projectFile.getAllResources();
	}
	//取得标准日历每周工作日
	public static int[] getworkingday(ProjectFile projectFile) {
		return projectFile.getCalendar().getDays();
	}
	//取得例外日期
	public static List getCalendarExceptions(ProjectFile projectFile) {
		return projectFile.getCalendar().getCalendarExceptions();
	}
	//取得资源日历
	public static List getresourcesCalendar(ProjectFile projectFile) {
		return projectFile.getResourceCalendars();
	}
	//取得齐套清单
	public static List fk_list(ProjectFile projectFile) {
		List task_list=projectFile.getAllTasks();
		List notes=new ArrayList<>();
		for(int i=0;i<task_list.size();i++){
			Task task=(Task) task_list.get(i);
			String note=task.getNotes();//取得任务备注字段
			if(note.length()!=0){
				while(note.length()!=0){
					Map map=new HashMap<>();
					map.put(task.getID(),note.substring(0,note.indexOf("\n")));
					notes.add(map);
					note=note.substring(note.indexOf("\n")+1,note.length());
				}
			}
		}
		return notes;
	}
	public static void main(String[] args) {
		try {
			ProjectFile projectFile=new MPPReader().read(new File("C:\\Users\\单振龙\\Desktop\\项目1.mpp"));
			List<Task>tasks= projectFile.getAllTasks();
			for(int i=0;i<tasks.size();i++) {
				System.out.println("----------"+tasks.get(i).getName()+"--------");
				if(null==tasks.get(i).getPredecessors()) {
					continue;
				}
				for(int j=0;j<tasks.get(i).getPredecessors().size();j++) {
					System.out.println("-------------前置"+(j+1)+"---------------");
					System.out.println(tasks.get(i).getPredecessors().get(j).getTargetTask().getName());
					System.out.println("--------------------------------------\n\n");
				}
			}
		} catch (MPXJException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
