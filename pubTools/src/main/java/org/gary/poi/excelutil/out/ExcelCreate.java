package org.gary.poi.excelutil.out;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelCreate {
	
	
	public static enum XlsType{
			XLS("xls") , XLSX("xlsx") ;
			protected String ext ; 
			
			XlsType(String ext){
				this.ext = ext;
			}

			public String getExt() {
				return ext;
			}
			
	}
	
	public static File getTempFile(XlsType xlsType){
		String fileName = ExcelCreate.class.getSimpleName() 
		+ "." + xlsType.getExt() ;
		File f = new File(fileName);
		if(f.exists()){
			return f; 
		}
		
		try {
			switch (xlsType) {
			case XLSX:
				Workbook workbook = new  XSSFWorkbook();
				workbook.createSheet("数据");
				workbook.write(new FileOutputStream( f ));
				workbook.close();
				break;
			default:
				workbook = new HSSFWorkbook();
				workbook.createSheet("数据");
				workbook.write(new FileOutputStream( f ));
				workbook.close();
				break ; 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return f;
	}
	public static void main(String[] args) {
		System.out.println( getTempFile(XlsType.XLSX).getAbsolutePath());
	}
}
