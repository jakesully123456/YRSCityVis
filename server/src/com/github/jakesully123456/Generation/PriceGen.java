package com.github.jakesully123456.Generation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PriceGen {
	public HashMap<String, Integer> prices;
	public PriceGen(BoroughGen bg) {
		prices = new HashMap<String, Integer>();
		for (String borough : bg.boroughs.values()) {
			int price = find(borough);
			if (price != -1) {
				prices.put(borough, price);
			} else {
				System.out.println("ERROR - Special borough:" + borough);
			}
		}
		System.out.println(prices.toString());
	}
	
	private int find(String b) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new File("HousePrices.xlsx"));
			XSSFSheet sheet = workbook.getSheet("Sheet1");
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch(cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						break;
					case Cell.CELL_TYPE_STRING:
						if (cell.getStringCellValue().equalsIgnoreCase(b)) {
							return (int) Math.round(row.getCell(cell.getColumnIndex()+1).getNumericCellValue());
						}
						break;
					}
				}
			}
			workbook.close();
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
}