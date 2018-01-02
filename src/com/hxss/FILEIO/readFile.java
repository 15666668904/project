package com.hxss.FILEIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class readFile{
	String prop_path = Thread.currentThread().getContextClassLoader().getResource("ftp.properties").getPath();
	Properties properties=new Properties();
	public void file_delete(String file_name) {
		try {
			properties.load(new FileInputStream(prop_path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String localpath=properties.getProperty("local_path");
		File file=new File(localpath+"/"+file_name);
		if(file.exists()&&file.isFile()){
			file.delete();
		}
	}
	public void removeftp_file(String file_name) {
		try {
			properties.load(new FileInputStream(prop_path));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("读取ftp配置文件出错");
			e1.printStackTrace();
		}
		String username=properties.getProperty("ftp_user");//ftp用户名
		String password=properties.getProperty("ftp_password");//ftp密码
		String server=properties.getProperty("ftp_server");//ftp的ip地址
		String filename=file_name;//下载的文件名(测试写死，到时传参)
		String path=properties.getProperty("work_path");//存储目录(路径问题有坑,不能有中文)
		FTPClient ftpClient=new FTPClient(); 
		int reply;
		try {
			ftpClient.connect(server);//连接服务器
			ftpClient.login(username, password);//登录服务器
			//以下判断是否登录成功
			reply=ftpClient.getReplyCode();
			if(!FTPReply.isPositiveCompletion(reply)){
				ftpClient.disconnect();
			}
			ftpClient.changeWorkingDirectory(path);//转移工作目录
			ftpClient.deleteFile(file_name);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	public void downloadFile(String file_name) {
		try {
			properties.load(new FileInputStream(prop_path));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("读取ftp配置文件出错");
			e1.printStackTrace();
		}
		System.out.println("下载文件"+file_name);
		String username=properties.getProperty("ftp_user");//ftp用户名
		String password=properties.getProperty("ftp_password");//ftp密码
		String server=properties.getProperty("ftp_server");//ftp的ip地址
		String filename=file_name;//下载的文件名(测试写死，到时传参)
		String path=properties.getProperty("work_path");//存储目录(路径问题有坑,不能有中文)
		String localpath=properties.getProperty("local_path");//本地存储目录
		FTPClient ftpClient=new FTPClient();
		try {
			int reply;
			ftpClient.connect(server);//连接服务器
			ftpClient.login(username, password);//登录服务器
			//以下判断是否登录成功
			reply=ftpClient.getReplyCode();//?
			if(!FTPReply.isPositiveCompletion(reply)){
				ftpClient.disconnect();
			}
			ftpClient.changeWorkingDirectory(path);//转移工作目录
			FTPFile[] files=ftpClient.listFiles();//遍历下载目录
			for(FTPFile file:files){
				//两次解码，解决中文乱码
				byte[] bytes=file.getName().getBytes("iso-8859-1");
				String string=new String(bytes,"utf-8");
				if(string.equals(filename)){
					//6.写操作，将其写入到本地文件中
					File localFile = new File(localpath + file.getName());  
					OutputStream is = new FileOutputStream(localFile);  
					ftpClient.retrieveFile(file.getName(), is);  
					is.close();  
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public File getFile() {
		File file=new File("");
		return file;
	}


}