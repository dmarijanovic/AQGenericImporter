package com.aquicore.standfordata.excel;

import java.io.FileNotFoundException;
import java.text.ParseException;

import com.aquicore.standfordata.model.CellDefinition;
import com.aquicore.standfordata.model.Row;

public class MeterManager {
	
	private Meter meterNo1;
	private Meter meterNo2;
	private Meter meterNo3;
	private Meter meterNo4;
	
	private Meter meterNo1Errors;
	private Meter meterNo2Errors;
	private Meter meterNo3Errors;
	private Meter meterNo4Errors;
	
	
	public MeterManager() {
		try {
			init();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void init() throws FileNotFoundException {
		// create meters data
		CellDefinition cellMeterNo1 = new CellDefinition(0, 1, 3);
		CellDefinition cellMeterNo2 = new CellDefinition(4, 5, 7);
		CellDefinition cellMeterNo3 = new CellDefinition(8, 9, 11);
		CellDefinition cellMeterNo4 = new CellDefinition(12, 13, 15);
		
		meterNo1 = new Meter("data/", "meter1", cellMeterNo1, true, true, 300000);
		meterNo2 = new Meter("data/", "meter2", cellMeterNo2, true, true, 300000);
		meterNo3 = new Meter("data/", "meter3", cellMeterNo3, true, true, 300000);
		meterNo4 = new Meter("data/", "meter4", cellMeterNo4, true, true, 300000);
		
		meterNo1Errors = new Meter("data/", "meter1_errors", cellMeterNo1, false, false, 300000);
		meterNo2Errors = new Meter("data/", "meter2_errors", cellMeterNo2, false, false, 300000);
		meterNo3Errors = new Meter("data/", "meter3_errors", cellMeterNo3, false, false, 300000);
		meterNo4Errors = new Meter("data/", "meter4_errors", cellMeterNo4, false, false, 300000);
	}
	
	public void close() {
		meterNo1.close();
		meterNo2.close();
		meterNo3.close();
		meterNo4.close();
		
		meterNo1Errors.close();
		meterNo2Errors.close();
		meterNo3Errors.close();
		meterNo4Errors.close();
	}
	
	public void onRowAvailable(Row row) throws FileNotFoundException {
		if(row.getId() <= 2) {
			// ignore first two rows
			return;
		}
		
		if(row.getId() % 1000 == 0) {
			System.out.println("Row: " + row.getId());
		}
		
		try {
			if(!meterNo1.write(row)) {
				meterNo1Errors.write(row);
			}
			if(!meterNo2.write(row)) {
				meterNo2Errors.write(row);
			}
			if(!meterNo3.write(row)) {
				meterNo3Errors.write(row);
			}
			if(!meterNo4.write(row)) {
				meterNo4Errors.write(row);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
