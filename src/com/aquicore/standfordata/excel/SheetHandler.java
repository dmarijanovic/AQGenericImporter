package com.aquicore.standfordata.excel;

import java.io.FileNotFoundException;

import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.aquicore.standfordata.model.Row;

/** 
 * See org.xml.sax.helpers.DefaultHandler javadocs 
 */
public class SheetHandler extends DefaultHandler {
	private SharedStringsTable sst;
	private String lastContents;
	private boolean nextIsString;
	
	private MeterManager meterManager;
	private Row row;
	private int rowIndex;
	
	public SheetHandler(SharedStringsTable sst, MeterManager meterManager) {
		this.sst = sst;
		this.meterManager = meterManager;
		this.row = new Row();
		rowIndex = 0;
	}
	
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		// c => cell
		if(name.equals("c")) {
			String reference = attributes.getValue("r");
			if(reference.startsWith("A")) {
				rowIndex++;
				row.clear();
				row.setId(rowIndex);
			}
			
			// Print the cell reference
//			System.out.print(attributes.getValue("r") + " - ");
			// Figure out if the value is an index in the SST
			String cellType = attributes.getValue("t");
			if(cellType != null && cellType.equals("s")) {
				nextIsString = true;
			} else {
				nextIsString = false;
			}
		}
		
		// Clear contents cache
		lastContents = "";
	}
	
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		// Process the last contents as required.
		// Do now, as characters() may be called more than once
		if(nextIsString) {
			int idx = Integer.parseInt(lastContents);
			lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
			nextIsString = false;
		}

		// v => contents of a cell
		// Output after we've seen the string contents
		if(name.equals("v")) {
			row.add(lastContents);
		} else if(name.equals("row")) {
			// last cell, row end
			try {
                meterManager.onRowAvailable(row);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
		}
		
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		lastContents += new String(ch, start, length);
	}
	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		System.out.println("End document, total rows " + rowIndex);
		meterManager.close();
	}
}
