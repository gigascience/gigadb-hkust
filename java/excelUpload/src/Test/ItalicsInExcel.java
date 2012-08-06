/**
 * 
 */
package Test;


import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * @author 王森洪
 *
 * @date 2012-5-4
 */
public class ItalicsInExcel {
	/**
	 * @param args
	 * @throws IOException 
	 * @throws BiffException 
	 */
	public static void main(String[] args) throws BiffException, IOException {
		// TODO Auto-generated method stub
		String path="src/test.xls";
		File file = new File(path);
		// file doesn't exist   
		if(!file.exists())
			return;
		Workbook workbook = Workbook.getWorkbook(file);	
		Sheet filesSheet = workbook.getSheet("test");
		Cell cell=filesSheet.findCell("testABCD");
	
		String value=cell.getContents();
		jxl.format.CellFormat format=cell.getCellFormat();
		format.
		if(format.getFont().isItalic())
			System.out.println("is italics");
		else{
			System.out.println("we don;t found italics");
		}
		
		// test
	}

}
