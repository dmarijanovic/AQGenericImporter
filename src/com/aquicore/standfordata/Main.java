package com.aquicore.standfordata;

import com.aquicore.standfordata.excel.Reader;

public class Main {

	public static void main(String[] args) {
		
		
		System.out.println("Start");
		
		try {
			Reader em = new Reader("data/in.xlsx");
			em.processAllSheets();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}
	


}
