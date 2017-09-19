package com.hxss.DAO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hxss.UTIL.HibernateUtil;
import com.hxss.VO.EN_PLAN_CALENDAR;
import com.hxss.VO.EN_RESOURCES;
import com.hxss.VO.HXSS_FK;
import com.hxss.VO.HXSS_task_resources;
import com.hxss.VO.Plan_template;
import com.hxss.VO.hxss_task_ready;
import com.hxss.VO.noworking_day;
import com.hxss.VO.pro_obj;
import com.hxss.VO.pro_taskpred;
public class ProjectDAOimpl implements ProjectDAO {

	@Override
	public void savepro_obj(pro_obj pro_obj) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		Transaction transaction=session.beginTransaction();
		try {
			session.save(pro_obj);
			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			transaction.rollback();
		}finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void savepro_taskpred(pro_taskpred pro_taskpred) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		Transaction transaction=session.beginTransaction();
		try {
			session.save(pro_taskpred);
			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			transaction.rollback();
		}finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void savehxss_task_resources(HXSS_task_resources hxss_task_resources) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		Transaction transaction=session.beginTransaction();
		try {
			session.save(hxss_task_resources);
			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			transaction.rollback();
		}finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void saveEN_RESOURCES(EN_RESOURCES EN_RESOURCES) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		Transaction transaction=session.beginTransaction();
		try {
			session.save(EN_RESOURCES);
			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			transaction.rollback();
		}finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public String getobj_sid(String task_id,String plan_version_sid) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		task_id=Integer.parseInt(task_id)>1000?task_id:String.valueOf(1000+Integer.parseInt(task_id));
		List list =session.createQuery("select obj_sid from pro_obj where obj_code='"+task_id+"' and plan_version_sid='"+plan_version_sid+"'").list();
		HibernateUtil.closeSession();
		if(! list.isEmpty()){
			return (String) list.get(0);
		}
		return"";
	}

	@Override
	public String getresources_sid(String resources_name, String xpmobs_sid) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		List list=session.createQuery("select RESOURCES_sid from EN_RESOURCES where xpmobs_sid='"+xpmobs_sid+"' and resources_name='"+resources_name+"'").list();
		HibernateUtil.closeSession();
		if(!list.isEmpty()){
			return (String) list.get(0);
		}
		return "";
	}

	@Override
	public List getresources(String xpmobs_sid) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		List resources=session.createQuery("select resources_name from EN_RESOURCES where xpmobs_sid='"+xpmobs_sid+"'").list();
		HibernateUtil.closeSession();
		return resources;
	}

	@Override
	public void deletecalendar(String xpmobs_sid) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		Transaction transaction=session.beginTransaction();
		try {
			session.createSQLQuery("delete from EN_PLAN_CALENDAR where xpmobs_sid='"+xpmobs_sid+"'").executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			transaction.rollback();
		}finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void saveEN_PLAN_CALENDAR(EN_PLAN_CALENDAR en_PLAN_CALENDAR) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		Transaction transaction=session.beginTransaction();
		try {
			session.save(en_PLAN_CALENDAR);
			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			transaction.rollback();
		}finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public String getcalendar_sid(String calendar_name, String xpmobs_sid) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		List list= session.createQuery("select calendar_sid from EN_PLAN_CALENDAR where xpmobs_sid='"+xpmobs_sid+"' and calendar_name='"+calendar_name+"'").list();
		HibernateUtil.closeSession();
		if(!list.isEmpty()){
			return (String) list.get(0);
		}else {
			return null;
		}
	}

	@Override
	public void savenoworking_day(noworking_day noworking_day) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		Transaction transaction=session.beginTransaction();
		try {
			session.save(noworking_day);
			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			transaction.rollback();
		}finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public boolean noworkday(String day,String calendar_sid) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List list=session.createQuery("select nonworking_day from noworking_day where calendar_sid='"+calendar_sid+"'").list();
		if(list.isEmpty()){
			return true;
		}
		for (int i = 0; i <list.size(); i++) {
			try {
				if(day.equals(formatter.format(formatter.parse((String) list.get(i))))){
					return false;
				}else {
					return true;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("转换错误");
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public void setresources_calendar(String calendar_sid, String resources_sid) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		Transaction transaction=session.beginTransaction();
		try {
			session.createSQLQuery("update EN_RESOURCES set resource_calendar_sid='"+calendar_sid+"' where RESOURCES_sid='"+resources_sid+"'").executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			transaction.rollback();
		}finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public String gethuman_sid(String human_name, String dept_sid) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		List list= session.createQuery("select human_sid from xpm_human where human_name='"+human_name+"' and xpmobs_sid='"+dept_sid+"'").list();
		if(list.size()!=1){
			return null;
		}
		return (String) list.get(0);
	}

	@Override
	public String gethuman_sidfromxpm(String human_name) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		List list= session.createQuery("select human_sid from xpm_human where human_name='"+human_name+"'").list();
		if(list.size()!=1){
			return null;
		}
		HibernateUtil.closeSession();
		return (String) list.get(0);
	}

	@Override
	public void savefk_list(HXSS_FK hxss_FK) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		Transaction transaction=session.beginTransaction();
		try {
			session.save(hxss_FK);
			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			transaction.rollback();
		}finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void savetask_ready(hxss_task_ready hxss_task_ready) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		Transaction transaction=session.beginTransaction();
		try {
			session.save(hxss_task_ready);
			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			transaction.rollback();
		}finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void savePlan_template(Plan_template plan_template) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		Transaction transaction=session.beginTransaction();
		try {
			session.save(plan_template);
			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			transaction.rollback();
		}finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public String gettemp_obj_sid(String task_id, String plan_version_sid) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		List list =session.createQuery("select obj_sid from Plan_template where obj_code='"+task_id+"' and plan_version_sid='"+plan_version_sid+"'").list();
		HibernateUtil.closeSession();
		if(! list.isEmpty()){
			return (String) list.get(0);
		}
		return"";
	}

	@Override
	public void Update_project_delivery(String project_delivery, String xpmobs_sid) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		Transaction transaction=session.beginTransaction();
		SQLQuery sqlQuery= session.createSQLQuery("update en_project set "
				+ "pcd='"+project_delivery+"' where proj_sid='"+xpmobs_sid+"'");
		sqlQuery.executeUpdate();
		transaction.commit();
		HibernateUtil.closeSession();
	}

	@Override
	public void deletepro_obj(String plan_version_sid) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		Transaction transaction=session.beginTransaction();
		Query query=session.createQuery("delete from pro_obj where plan_version_sid='"+plan_version_sid+"'");
		query.executeUpdate();
		transaction.commit();
		HibernateUtil.closeSession();
	}

	@Override
	public boolean getdefault_plan(String plan_version_sid) {
		// TODO Auto-generated method stub
		Session session=HibernateUtil.getSession();
		SQLQuery sqlQuery=session.createSQLQuery("select cast(default_version as varchar(10)) as default_version "
				+ "from en_plan_version_cj where plan_version_sid='"+plan_version_sid+"'");
		String default_version=(String) sqlQuery.list().get(0);
		if(null!=default_version&&default_version=="0") {
			return true;
		}
		return false;
	}

}
