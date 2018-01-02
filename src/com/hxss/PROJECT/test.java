package com.hxss.PROJECT;

import java.io.File;

import net.sf.mpxj.MPXJException;
import net.sf.mpxj.mpp.MPPReader;

public class test {

	public static void main(String[] args) throws MPXJException {
		// TODO Auto-generated method stub
		MPPReader reader=new MPPReader();
		reader.read(new File("G://杭州绿城.mpp"));
		System.out.println(1);
	}

}
