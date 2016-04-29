package org.gary.poi.util.xls;

import java.sql.SQLException;
import java.util.List;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;


public abstract class ExcelAbstract {
	
	public abstract void optRows(int sheetIndex, int curRow, List<String> rowlist);

	public class HxlsPrint extends HxlsAbstract {
		
		public HxlsPrint(POIFSFileSystem fs) throws SQLException {
			super( fs ); 
		}
		
		public HxlsPrint(String filename)throws Exception{
			super(filename);
		}
		
		@Override
		public void optRows(int sheetIndex, int curRow, List<String> rowlist) {
			ExcelAbstract.this.optRows(sheetIndex, curRow, rowlist);
		}
		
	}

	public class XxlsxPrint extends XxlsAbstract {
		
		@Override
		public void optRows(int sheetIndex, int curRow, List<String> rowlist){
			ExcelAbstract.this.optRows(sheetIndex, curRow, rowlist);
		}
	}
	
	
	public void process(String fileName)throws Exception{
		String lowerCase = fileName.toLowerCase();
		if(lowerCase.endsWith("xls")){
			HxlsPrint hxlsPrint = new HxlsPrint(fileName);
			hxlsPrint.process() ;
		}else if(lowerCase.endsWith("xlsx")){
			XxlsxPrint xxlsxPrint = new XxlsxPrint();
			xxlsxPrint.process( fileName ) ; 
		}else{
			throw new RuntimeException("不能识别这个文件");
		}
	}
	
}


