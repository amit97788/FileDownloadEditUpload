package DownloadAndUpload.excelFileDownloadAndUpload;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import jdk.jshell.execution.Util;


public class UploadDownload extends UI{

	public static WebDriver driver=null;
	CreateUniqueFolderAndPermanentSubfolder folder = new CreateUniqueFolderAndPermanentSubfolder();
	String customDownload = folder.createFolder();
	//UI ui = new UI(driver);
	
	@Test
public void getUploadDownload() throws IOException, InterruptedException {
		
	String customDownload = folder.createFolder();
	String customDownloadDir = customDownload;
	String fileName = customDownload+ "\\" +"download.xlsx";
	String updatedCellName= generateRandomDigits(3);
	
	//Store the custome file location
	HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
	chromePrefs.put("profile.default_content_settings.popups", 0);
	chromePrefs.put("download.default_directory", customDownloadDir);
	ChromeOptions options = new ChromeOptions();
	options.setExperimentalOption("prefs", chromePrefs);
     
	options.addArguments(
			"start-maximized", // open Browser in maximized mode 
			"disable-infobars", // disabling infobars - Not a valid argument now
			"disable-extensions", // disabling extensions
			"disable-notifications" );
	
	driver = new ChromeDriver(options);
	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	driver.get("https://rahulshettyacademy.com/upload-download-test/index.html");
	
	//Download file
	WebElement download = driver.findElement(By.xpath("//button[text()='Download']"));
	clickElement(download);
	
	
	//Edit the Excel
	int colNum = getColNumber(fileName,"Price");
	int Row = getRowNumber(fileName,"Apple");
	assertTrue(updateCellInFiles(fileName,colNum,Row,updatedCellName));
	
	//Upload the File
	WebElement upload = driver.findElement(By.id("fileinput"));
	upload.sendKeys(fileName);
	
	//Get Popup Message
	By popupMsg = By.xpath("//div[contains(text(),'Updated Excel')]");
	WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(12));
	wait.until(ExpectedConditions.visibilityOfElementLocated(popupMsg));
	String msg = driver.findElement(popupMsg).getText();
	System.out.println(msg);
	assertEquals(msg, "Updated Excel Data Successfully.");
	wait.until(ExpectedConditions.invisibilityOfElementLocated(popupMsg));
	
	//Verify the updated value
	String id = driver.findElement(By.xpath("//div[text()='Price']")).getAttribute("data-column-id");
	String value = driver.findElement(By.xpath("//div[text()='Apple']/parent::div/parent::div/div[@id='cell-"+id+"-undefined']")).getText();
	assertEquals(updatedCellName, value);
	
	driver.close();
	}

private static String generateRandomDigits(int length) {
	Random random = new Random();
    StringBuilder digits = new StringBuilder();

    for (int i = 0; i < length; i++) {
        // Generate a random digit between 0 and 9
        int digit = random.nextInt(10);
        digits.append(digit);
    }

    return digits.toString();
}

private static boolean updateCellInFiles(String customDownloadDir, int colNum, int row, String string) throws IOException {
	ArrayList<String> array = new ArrayList<String>();
	FileInputStream fils = new FileInputStream(customDownloadDir);
	XSSFWorkbook xss = new XSSFWorkbook(fils);
	XSSFSheet sheet = xss.getSheet("sheet1");
	
	Row rowField=sheet.getRow(row-1);
    Cell cellField=rowField.getCell(colNum-1);
    cellField.setCellValue(string);
    FileOutputStream fos=new FileOutputStream(customDownloadDir);
    xss.write(fos);
    xss.close();
    fos.close();
    return true;
}

private static int getRowNumber(String customDownloadDir, String rowName) throws IOException {
	ArrayList<String> array = new ArrayList<String>();
	FileInputStream fils = new FileInputStream(customDownloadDir);
	XSSFWorkbook xss = new XSSFWorkbook(fils);
	XSSFSheet sheet = xss.getSheet("sheet1");
	
	Iterator<Row>  rows = sheet.iterator();
	
	int k=1;
	int rowIndex=-1;
	while(rows.hasNext()) {
		Row row=rows.next();
		Iterator<Cell> cells=  row.cellIterator();
		  while(cells.hasNext()) {
			  Cell cell = cells.next();
			  if(cell.getCellType()==org.apache.poi.ss.usermodel.CellType.STRING && cell.getStringCellValue().equalsIgnoreCase(rowName)) {
				  rowIndex=k;
			  }
		  }
		  k++;
	}
	
	return rowIndex;
}

private static int getColNumber(String fileName,String columnName) throws IOException, InterruptedException {
	ArrayList<String> array = new ArrayList<String>();
	waitForFileDownload(fileName, 10);
	FileInputStream fils = new FileInputStream(fileName);
	XSSFWorkbook xss = new XSSFWorkbook(fils);
	int sheets = xss.getNumberOfSheets();
    XSSFSheet sheet = xss.getSheet("sheet1");		
    Iterator<Row>  rows = sheet.iterator();
    Row firstRow = rows.next();
	Iterator<Cell> cell = firstRow.cellIterator();
	int k=1;
	int column =0;
	
	while(cell.hasNext()) {
		Cell value = cell.next();
		
		if(value.getStringCellValue().equalsIgnoreCase(columnName)) {
			column = k;
		}
		k++;
		System.out.println(column);
	}
	return column;	
	
}

	
}
