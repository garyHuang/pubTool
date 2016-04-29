package org.gary.poi.util.xls;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class ReadExcelTest extends ExcelAbstract {
	
	@Override
	public void optRows(int sheetIndex, int curRow, List<String> rowlist) {

		if(curRow == 0)
		System.out.println( "sheetIndex:" + sheetIndex + " 行号：" + curRow
				+ " rowlist:" + rowlist); 
	}
	
	public static void main(String[] args) throws Exception{
		new ReadExcelTest().process("d:/001.xlsx");
	}
}
