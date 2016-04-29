package org.gary.poi.excelutil;

/**
 * 解析Excel测试类
 * 
 * @author zhangchaofeng
 * @version 1.0
 * @date Oct 18, 2011
 */
public class ReadExcelUtil extends ExcelProcessor {

	public ReadExcelUtil(String fileName) throws Exception {
		super(fileName);
	}

	@Override
	public void processRow(XRow row) {
//		if(row.getCellsSize()==23){
			for (int i = 0; i < row.getCellsSize(); i++) {
				System.out.print("[" + row.getRowIndex() + ","
						+ i+","
						+ row.getCell(i).getValue() + "]");
			}
//		}else if(row.getCellsSize()<3){
//			for (int i = 0; i < row.getCellsSize(); i++) {
//				System.out.print("[" + row.getRowIndex() + ","
//						+ i+ ","
//						+ row.getCell(i).getValue() + "]");
//			}
//			for (int i = row.getCellsSize(); i < 3; i++) {
//				System.out.print("[" + row.getRowIndex() + ","
//						+ i+ ","
//						+ "null]");
//			}
//		}
		System.out.println();
	}
	
	public static void main(String[] args) throws Exception {
		ReadExcelUtil reader=new ReadExcelUtil("D:/testdata.xlsx");
		//reader.processByRow();//处理所有的sheet
		//reader.stop();//运行一半需要停止调用此方法，释放文件流，正常运行完毕会自动释放
		reader.processByRow(1);//处理第一个sheet，sheet索引从1开始
	}

	@Override
	public void processRowResult(boolean isOK) {
		System.out.println("isOK:"+isOK);
	}
}
