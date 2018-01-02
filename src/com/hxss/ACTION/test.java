package com.hxss.ACTION;

import java.io.File;
import java.text.SimpleDateFormat;

import net.sf.mpxj.MPXJException;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.mspdi.schema.Project.Tasks;

public class test {

	public static void main(String[] args) throws MPXJException {
		// TODO Auto-generated method stub
		ProjectFile projectFile=new MPPReader().read(new File("C://Users//单振龙//Desktop//安顺王庄棚改项目进度计划toc3_bak.mpp"));
		//for(int i=0;i<projectFile.getAllTasks().size();i++) {
			//System.out.println(projectFile.getAllTasks().get(i));
			//System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(projectFile.getAllTasks().get(i).getStart()));
			//System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(projectFile.getAllTasks().get(i).getFinish()));
			
			System.out.println(projectFile.getTaskByID(5).getStart());
			System.out.println(projectFile.getTaskByID(5).getFinish());
		//}
	}

}
