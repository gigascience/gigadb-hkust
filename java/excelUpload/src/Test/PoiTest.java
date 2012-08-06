/**
 * 
 */
package Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

/**
 * @author 王森洪
 * 
 * @date 2012-5-4
 */
public class PoiTest {
	public static String getContent(Sheet sheet, String attribute) {
		Cell cell = findCell(sheet, attribute);
		// we can't find the attribute.
		if (cell == null) {
			return null;
		}
		int row = cell.getRowIndex();
		int col = cell.getColumnIndex();
		// calcualte the position of the value
		col += 2;
		Cell value = getCell(sheet, row, col);
		// value.
		String content = value.getStringCellValue();
		content = content.replaceAll("\\u00a0", " ");
		content = content.replaceAll("\\ufffd", " ");
		return content;
	}

	public static Cell getCell(Sheet sheet, int row, int col) {
		Row row2 = sheet.getRow(row);
		return row2.getCell(col);
	}

	public static String replaceItalics(HSSFRichTextString richTextString,Workbook workbook) {
		String result = "";
		String plainString = richTextString.getString();
		boolean begin = false;
		for (int i = 0; i < richTextString.length(); i++) {
			HSSFFont font= (HSSFFont) workbook.getFontAt(richTextString.getFontAtIndex(i));
			if (font.getItalic()) {
				if (!begin) {
					begin = true;
					result += "<em>";
				}
				result += plainString.charAt(i);
			} else {		
				if (begin) {
					begin = false;
					result += "</em>";
				}
				result += plainString.charAt(i);
			}//else
		}//for
		if(begin){
			result += "</em>";
		}
		return result;
	}
	public static String replaceItalics(XSSFRichTextString richTextString) {
		String result = "";
		String plainString = richTextString.getString();
		boolean begin = false;
		for (int i = 0; i < richTextString.length(); i++) {
			XSSFFont font = richTextString.getFontAtIndex(i);
			if (font.getItalic()) {
				if (!begin) {
					begin = true;
					result += "<em>";
				}
				result += plainString.charAt(i);
			} else {
				if (begin) {
					begin = false;
					result += "</em>";
				}
				result += plainString.charAt(i);
			}//else
		}//for
		if(begin){
			result += "</em>";
		}
		return result;
	}
	// find attribute
	public static Cell findCell(Sheet sheet, String content) {
		// we just read the first column
		for (Row row : sheet) {
			Cell cell = row.getCell(0);
			if (content.equals(cell.getStringCellValue()))
				return cell;
		}

		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// EXCEL文件路径
		String filename = "src/test.xls";
		// 创建一个列表，用于保存EXCEL工作表的数据
		// List sheetData = new ArrayList();
		FileInputStream fis = null;
		try {
			// 创建输入流对象
			fis = new FileInputStream(filename);

			// 获得一个excel实例
			Workbook workbook = WorkbookFactory.create(fis);
			// 取得excel文件的第一个工作表
			Sheet sheet = workbook.getSheet("test");
			Cell cell = getCell(sheet, 2, 0);
			
			HSSFRichTextString richTextString = (HSSFRichTextString) cell
					.getRichStringCellValue();
			System.out.println(replaceItalics(richTextString,workbook));
//			// HSSFFont hssfFont=cell.
			for (int i = 0; i < richTextString.length(); i++) {
				System.out.println("font num: "
						+ richTextString.getFontAtIndex(i));
			}
//			if (richTextString.numFormattingRuns() > 0) {
//				int beginIndex = richTextString.getIndexOfFormattingRun(0);
//				System.out.println("formatting = "
//						+ richTextString.numFormattingRuns());
//				for (int i = 1; i < richTextString.numFormattingRuns(); i++) {
//					//				
//					int runIndex = richTextString.getIndexOfFormattingRun(i);
//					System.out.println(richTextString.toString().substring(
//							beginIndex, runIndex));
//					beginIndex = runIndex;
//				}
//				System.out.println(richTextString.toString().substring(
//						beginIndex));
//			} else {
//				System.out.println("formatting = 0");
//			}
//			// System.out.println(cell.getStringCellValue());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		}
	}
	
