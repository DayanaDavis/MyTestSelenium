package com.obs.myTestSelenium;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExcelUtility {
    public Object[][] excelRead(String filepath,String sheetname) throws IOException {
        FileInputStream input=new FileInputStream(filepath);
        XSSFWorkbook wb=new XSSFWorkbook(input);
        XSSFSheet sheet=wb.getSheet(sheetname);
        int rowCount=sheet.getLastRowNum();
        int cellCount=sheet.getRow(0).getLastCellNum();
        Object[][] data=new Object[rowCount][cellCount];
        for (int i=1;i<=rowCount;i++){
            Row r=sheet.getRow(i);
            for (int j=0;j<cellCount;j++){
                Cell c=r.getCell(j);
                if(c.getCellType()== CellType.STRING){
                    data[i-1][j]=c.getStringCellValue();
                }
                else if (c.getCellType()==CellType.NUMERIC){
                    c.setCellValue(String.valueOf(CellType.STRING));
                    data[i-1][j]=c.getStringCellValue();
                }
                else{
                    data[i-1][j]=" ";
                }
            }
        }
        return data;
    }
}
