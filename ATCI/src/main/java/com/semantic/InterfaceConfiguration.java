package com.semantic;

import java.awt.Dimension;
import java.awt.Toolkit;

public class InterfaceConfiguration {

	public static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static int SCREEN_RESOLUTION = Toolkit.getDefaultToolkit().getScreenResolution();
	
	public String TDB_URL = "jdbc:virtuoso://localhost:1111";
	//public String TDB_URL = "jdbc:virtuoso://172.20.0.129:1111";
	
	public String TDB_USER_NAME = "exUser";
	//public String TDB_USER_NAME = "dba";
	
	public String TDB_USER_PASS = "keti123";
	//public String TDB_USER_PASS = "dba";
	
	public float fontSize = 62;
	
	public Dimension getScreenSizeOfPercent(int percent) {
		
		int percentWidth = (int) ((((float)percent)/100) * SCREEN_SIZE.width);
		
		int percentHeight = (int) ((((float)percent)/100) * SCREEN_SIZE.height);
		
		return new Dimension(percentWidth, percentHeight);
	}
	
}
