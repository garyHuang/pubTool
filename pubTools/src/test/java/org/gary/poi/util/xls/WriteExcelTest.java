package org.gary.poi.util.xls;

import java.io.FileOutputStream;

import org.gary.poi.excelutil.out.BigExcel;

public class WriteExcelTest {
	
	
	public static void main(String[] args)throws Exception {
		BigExcel bigExcel = BigExcel.getBigExcel(new FileOutputStream("d:/temp.xlsx"));
		int index =0;
		bigExcel.createExcel(new String[]{"row" + index }, index++);
		
		bigExcel.end();
	}
}
