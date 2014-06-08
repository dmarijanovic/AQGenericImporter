package com.aquicore.standfordata.excel;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;

import com.aquicore.standfordata.model.CellDefinition;
import com.aquicore.standfordata.model.Row;


public class Meter {
	
	private String mPath;
	private String mName;
	private PrintWriter mWriter;
	private CellDefinition mCellDefinition;
	private boolean mExcludeErrors;
	private boolean mExcludeNegativeAndZeros;
	private long mMaxLinePerFile;
	
	private long writeConter;
	private long excludeCounter;
	private int mPart;
	
	public Meter(String path, String name, CellDefinition cellDefinition, 
			boolean excludeErrors, boolean excludeNegativeAndZeros, long maxLinePerFile) throws FileNotFoundException {
		mPath = path;
		mName = name;
		mCellDefinition = cellDefinition;
		mExcludeErrors = excludeErrors;
		mExcludeNegativeAndZeros = excludeNegativeAndZeros;
		mMaxLinePerFile = maxLinePerFile;
		mPart = 0;
		open();
	}
	
	public boolean write(Row row) throws ParseException, FileNotFoundException {
//		System.out.println(mCellDefinition.getString(row));
		
			
		String line = mCellDefinition.getString(row);
		if(line != null) {
			
			if(mExcludeNegativeAndZeros && mCellDefinition.getMeasurement(row) <= 0) {
				excludeCounter ++;
				return false;
			}
			
			if (mMaxLinePerFile != 0 && writeConter > mMaxLinePerFile) {
			    close();
			    open();
			}
			
			mWriter.println(mCellDefinition.getString(row));
			writeConter++;
			return true;
		}
		
		return false;
	}
	
	private void open() throws FileNotFoundException {
	    mPart ++;
	    writeConter = 0;
	    String path = String.format("%s%s_part_%s.csv", mPath, mName, mPart); 
	       mWriter = new PrintWriter(path);
	}
	
	public void close() {
	    String path = String.format("%s%s_part_%s.csv", mPath, mName, mPart);
		String output = String.format("Finish %s [lines:%s;excluded:%s]", 
				path,
				writeConter,
				excludeCounter);
		System.out.println(output);

		mWriter.close();
	}
	
	

}
