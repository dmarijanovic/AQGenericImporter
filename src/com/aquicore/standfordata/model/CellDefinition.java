package com.aquicore.standfordata.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

public class CellDefinition {
	
	private StringBuilder mStringBuilder;
	private int mDatePartIndex;
	private int mTimePartIndex;
	private int mMeasurementIndex;
	
	private SimpleDateFormat mDatePartFormat;
	private SimpleDateFormat mOutputFormat;
	
	
	public CellDefinition(int datePartIndex, int timePartIndex, int measurementIndex) {
		mDatePartIndex = datePartIndex;
		mTimePartIndex = timePartIndex;
		mMeasurementIndex = measurementIndex;
		
		mStringBuilder = new StringBuilder();
		mDatePartFormat = new SimpleDateFormat("M/d/yy", Locale.US);
		mOutputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
		
	}
	
	public String getString(Row row) throws ParseException {
		mStringBuilder.setLength(0);
		String datePartValue = row.get(mDatePartIndex);
		String timePartValue = row.get(mTimePartIndex);
		String measurementValue = row.get(mMeasurementIndex);
		
		if(datePartValue == null && timePartValue == null && measurementValue == null) {
			return null;
		}
		
		// get date 
		Date datePart = mDatePartFormat.parse(datePartValue);;

		
		// get time
		Calendar timePart = Calendar.getInstance();
		timePart.setTime(DateUtil.getJavaDate(Double.parseDouble(timePartValue)));
		
		// merge dates
		Calendar outputDate = Calendar.getInstance();
		outputDate.setTime(datePart);
		
		outputDate.add(Calendar.HOUR_OF_DAY, timePart.get(Calendar.HOUR_OF_DAY));
		outputDate.add(Calendar.MINUTE, timePart.get(Calendar.MINUTE));
		outputDate.add(Calendar.SECOND, timePart.get(Calendar.SECOND));
		outputDate.add(Calendar.MILLISECOND, timePart.get(Calendar.MILLISECOND));
		
		mStringBuilder.append(mOutputFormat.format(outputDate.getTime()));
		mStringBuilder.append(",");
		mStringBuilder.append(measurementValue);
		
		return mStringBuilder.toString();
	}
	
	public double getMeasurement(Row row) {
		try {
			String value = row.get(mMeasurementIndex);
			if(value != null) {
				return Double.parseDouble(row.get(mMeasurementIndex));
			} 
		} catch (Exception e) {
//			System.out.println("error " + row.get(mMeasurementIndex));
		}
		
		return 0;
	}
	
}
