package com.presentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;

@Named(value="dataExporterView")
@SessionScoped
public class DataExporterView implements Serializable {
    
    private Exporter<DataTable> textExporter;
        
    @PostConstruct
    public void init() {
    	TextExporter textExporter = new TextExporter();
    }

    public void prueba() {
    	try {
    		File file = new File("TestData.xlsx");
            InputStream is = new FileInputStream(file);
    		// workbook object
            XSSFWorkbook workbook = new XSSFWorkbook(is);
      
            // spreadsheet object
            XSSFSheet spreadsheet
                = workbook.createSheet(" Student Data ");
      
            // creating a row object
            XSSFRow row;
      
            // This data needs to be written (Object[])
            Map<String, Object[]> studentData
                = new TreeMap<String, Object[]>();
      
            studentData.put(
                "1",
                new Object[] { "Roll No", "NAME", "Year" });
      
            studentData.put("2", new Object[] { "128", "Aditya",
                                                "2nd year" });
      
            studentData.put(
                "3",
                new Object[] { "129", "Narayana", "2nd year" });
      
            studentData.put("4", new Object[] { "130", "Mohan",
                                                "2nd year" });
      
            studentData.put("5", new Object[] { "131", "Radha",
                                                "2nd year" });
      
            studentData.put("6", new Object[] { "132", "Gopal",
                                                "2nd year" });
      
            Set<String> keyid = studentData.keySet();
      
            int rowid = 0;
      
            // writing the data into the sheets...
      
            for (String key : keyid) {
      
                row = spreadsheet.createRow(rowid++);
                Object[] objectArr = studentData.get(key);
                int cellid = 0;
      
                for (Object obj : objectArr) {
                    XSSFCell cell = row.createCell(cellid++);
                    cell.setCellValue((String) obj);
                    
                }
            }
      
            // .xlsx is the format for Excel Sheets...
            // writing the workbook into the file...
            
            FileOutputStream out = new FileOutputStream(
                new File("GFGsheet.xlsx"));
      
            workbook.write(out);
            out.close();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public Exporter<DataTable> getTextExporter() {
        return textExporter;
    }

    public void setTextExporter(Exporter<DataTable> textExporter) {
        this.textExporter = textExporter;
    }
}    