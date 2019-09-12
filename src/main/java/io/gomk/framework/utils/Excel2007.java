package io.gomk.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel2007 {

	public String read(String filePath) {

		StringBuilder sb = new StringBuilder();
		String str = "";
		XSSFWorkbook wb = null;
		try {

			File xlsFile = new File(filePath);
			wb = new XSSFWorkbook(new FileInputStream(xlsFile));

		} catch (IOException e) {
			e.printStackTrace();
		}
		XSSFSheet sheet = wb.getSheetAt(0); // 第一个sheet

		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		XSSFRow row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();

		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i <= rowNum; i++) {
			row = sheet.getRow(i);
			int j = 0;
			while (j < colNum) {
				XSSFCell cell = row.getCell(j);
				str += getStringCellValue(cell).trim() + "";
				sb.append(str);
				j++;
			}
		}
		return sb.toString();
	}

	private String getStringCellValue(XSSFCell cell) {
		if (cell == null) {
			return "";
		}

		String strCell = "";
		switch (cell.getCellType()) { // CELL_TYPE_STRING,CELL_TYPE_NUMERIC,CELL_TYPE_BOOLEAN,CELL_TYPE_BLANK
		case STRING:
			strCell = cell.getStringCellValue();
			break;
		case NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());
			break;
		case BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		if (cell == null) {
			return "";
		}
		return strCell;
	}
}
