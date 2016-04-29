package org.gary.poi.excelutil.out;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.gary.poi.excelutil.out.ExcelCreate.XlsType;


public class BigExcel {

	File tmp = null;
	Writer fw = null;
	OutputStream out = null;

	File templFile = null;
	SpreadsheetWriter sw = null ; 

	private BigExcel(){
		
	}
	
	private String worksheetsName = "xl/worksheets/sheet1.xml" ;
	
	public static BigExcel getBigExcel(OutputStream out) {
		return getBigExcel(out, ExcelCreate.getTempFile(XlsType.XLSX)); 
	}
	
	public static BigExcel getBigExcel(OutputStream out , XlsType xlsType) {
		return getBigExcel(out, ExcelCreate.getTempFile( xlsType )); 
	}
	
	
	public static BigExcel getBigExcel(OutputStream out, File templFile) {
		BigExcel bigExcel = new BigExcel();
		bigExcel.out = out;
		bigExcel.templFile = templFile;
		bigExcel.begin() ;
		return bigExcel;
	}

	protected void begin() {
		try {
			tmp = File.createTempFile("sheet", ".xml");
			fw = new OutputStreamWriter(new FileOutputStream(tmp), "UTF-8");
			sw = new SpreadsheetWriter( fw); 
			sw.beginSheet();
		} catch (Exception e) {
		}
	}
	
	 public void createExcel(Map<?, ?> data,String[] title , int index){
	      try {
	    	  sw.insertRow( index );
	    	  for(int i=0;i<title.length;i++ ){
	    		  String value = null == data.get(title[i]) ? "": String.valueOf(data.get(title[i])); 
	    		  sw.createCell(i, value ); 
	    	  }
	          sw.endRow();
		} catch (Exception e) {
		}
	 }
	 
	 public void createExcel(String[]datas, int index){
	      try {
	    	  sw.insertRow( index );
	    	  for(int i=0;i<datas.length;i++ ){
	    		  String value = null ==  datas[i] ? "": String.valueOf( datas[i] ); 

	    		  sw.createCell(i, value ); 
	    	  }
	          sw.endRow();
		} catch (Exception e) {
		}
	 }

	public void end(){
		try {
	        sw.endSheet();
			fw.close();
			substitute(templFile, tmp, worksheetsName, out);
			out.close();
		} catch (Exception e) {
		}
	}

	public static void substitute(File zipfile, File tmpfile, String entry,
			OutputStream out) throws IOException {

		ZipFile zip = new ZipFile(zipfile);
		ZipOutputStream zos = new ZipOutputStream(out);
		@SuppressWarnings("unchecked")
		Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
		while (en.hasMoreElements()) {
			ZipEntry ze = en.nextElement();
			if (!ze.getName().equals(entry)) {
				zos.putNextEntry(new ZipEntry(ze.getName()));
				InputStream is = zip.getInputStream(ze);
				copyStream(is, zos);
				is.close();
			}
		}
		zos.putNextEntry(new ZipEntry(entry));
		InputStream is = new FileInputStream(tmpfile);
		copyStream(is, zos);
		is.close();
		zos.close();
		zip.close();
	}

	private static void copyStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] chunk = new byte[1024];
		int count;
		while ((count = in.read(chunk)) >= 0) {
			out.write(chunk, 0, count);
		}
	}
	
}
