package io.gomk.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ParseExcelUtil {

	private static final String EXCEL_XLS = "xls";
	private static final String EXCEL_XLSX = "xlsx";

	private static Workbook wb;
	private static Sheet sheet;
	private static Row row;

	public static void main(String[] args) throws Exception {
		String filePath = "/Users/vko/Desktop/t_g_completion.xls";
		Map<Integer, Map<Integer, Object>> content = getExcelContent(filePath);
		
		System.out.println("end.");
	}

	/**
	 * 读取Excel数据内容
	 * 
	 * @param content
	 * @param celss
	 * 
	 * @return Map 包含单元格数据内容的Map对象
	 */
	public static Map<Integer, Map<Integer, Object>> getExcelContent(String filePath) throws Exception {
		Map<Integer, Map<Integer, Object>> content = new HashMap<Integer, Map<Integer, Object>>();
		FileInputStream in = null;
		try {
			File excelFile = new File(filePath);
			in = new FileInputStream(excelFile); // 文件流
			checkExcelVaild(excelFile);
			 wb = WorkbookFactory.create(in);
//			if (excelFile.getName().indexOf(".xlsx") > -1) {
//				wb = new XSSFWorkbook(in);
//			} else {
//				wb = new HSSFWorkbook(in);
//			}

			if (wb == null) {
				throw new Exception("Workbook对象为空！");
			}
			sheet = wb.getSheetAt(0);
			// 得到总行数
			int rowNum = sheet.getLastRowNum();
			row = sheet.getRow(0);
			int colNum = row.getPhysicalNumberOfCells();
			// 正文内容应该从第二行开始,第一行为表头的标题
			for (int i = 1; i <= rowNum; i++) {
				row = sheet.getRow(i);
				int j = 0;
				Map<Integer, Object> cellValue = new HashMap<Integer, Object>();
				while (j < colNum) {
					Cell cell = row.getCell(j);
					if (cell == null) {
						cellValue.put((j + 1), "");
					} else {
						Object obj = getValue(cell);
						cellValue.put((j + 1), obj);
					}
					j++;
				}
				if (row.getCell(0) != null && !row.getCell(0).toString().equals("")) {
					System.out.println(i + "cell value:" + cellValue);
					content.put(i, cellValue);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return content;
	}

	/**
	 * 读取Excel数据内容
	 * 
	 * @return Map 包含单元格数据内容的Map对象
	 */
	public static Map<Integer, Map<Integer, Object>> readExcelContent() throws Exception {
		if (wb == null) {
			throw new Exception("Workbook对象为空！");
		}
		Map<Integer, Map<Integer, Object>> content = new HashMap<Integer, Map<Integer, Object>>();

		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();

		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i <= rowNum; i++) {
			row = sheet.getRow(i);
			int j = 0;
			Map<Integer, Object> cellValue = new HashMap<Integer, Object>();
			while (j < colNum) {
				Object obj = getValue(row.getCell(j));
				cellValue.put(j, obj);
				j++;
			}
			content.put(i, cellValue);
		}
		return content;
	}

	/**
	 * 判断文件是否是excel
	 * 
	 * @throws Exception
	 */
	public static void checkExcelVaild(File file) throws Exception {
		if (!file.exists()) {
			throw new Exception("文件不存在");
		}
		if (!(file.isFile() && (file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)))) {
			throw new Exception("文件不是Excel");
		}
	}

	private static Object getValue(Cell cell) {
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy/MM/dd");
		DecimalFormat decimalFormat = new DecimalFormat("#.#");
		Object obj = null;
		switch (cell.getCellType()) {
		
		case FORMULA:
			//obj = cell.getCellFormula().toString();
			try {
					obj = String.valueOf(cell.getNumericCellValue());
					Number n=Float.parseFloat(obj.toString());
					obj=n.intValue();
				} catch (IllegalStateException e) {
					obj = String.valueOf(cell.getStringCellValue());
				}
			
			System.out.println("obj:" + obj);
			break;
		case BOOLEAN:
			obj = cell.getBooleanCellValue();
			break;
		case ERROR:
			obj = cell.getErrorCellValue();
			break;
		case NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				double d = cell.getNumericCellValue();
				Date date = HSSFDateUtil.getJavaDate(d);
				obj = sFormat.format(date);
			} else {
				obj = decimalFormat.format((cell.getNumericCellValue()));
			}
			break;
		case STRING:
			obj = cell.getStringCellValue();
			break;
		default:
			break;
		}
		return obj;
	}

}
