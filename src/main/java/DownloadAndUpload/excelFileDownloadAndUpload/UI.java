package DownloadAndUpload.excelFileDownloadAndUpload;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UI extends CreateUniqueFolderAndPermanentSubfolder{
   
    CreateUniqueFolderAndPermanentSubfolder file = new CreateUniqueFolderAndPermanentSubfolder();
   String fileLocation = file.createFolder();
	public void clickElement(WebElement element) throws IOException {
		highlight(element);
		takeScreenShot();
		element.click();
		
	}
	public void highlight(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor)UploadDownload.driver;
		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');",element);
	}

	public void takeScreenShot() throws IOException {
		TakesScreenshot ts = (TakesScreenshot)UploadDownload.driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		File des = new File(fileLocation+"//OutputSS_"+timeStamp()+".jpg");
		FileUtils.copyFile(src, des);
	}
	public static String timeStamp() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime ldt = LocalDateTime.now();
		return dtf.format(ldt).replace(" ", "").replace("/", "").replace(":", "_");
	}
	public static void waitForFileDownload(String filePath, int wtMaxAttempts)throws IOException, InterruptedException
    {
    	File file = new File(filePath);
    	int itr=0;
    	do{  
			Thread.sleep(2000);
			if(itr>wtMaxAttempts)
			{
				break;
			}
			itr++;
		}while(!file.exists()); 
        
    }
	
}
