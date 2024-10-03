package DownloadAndUpload.excelFileDownloadAndUpload;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.Test;

public class CreateUniqueFolderAndPermanentSubfolder {
	
	// Create a unique folder name using the current timestamp
	    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String uniqueFolderPath = "C:\\TestingRunResult" + timestamp;
	    String permanentSubfolderPath = ""+uniqueFolderPath +"\\Download"; // Permanent subfolder

	         
	 public String createFolder() {
		  File uniqueFolder = new File(uniqueFolderPath);
	        if (!uniqueFolder.exists()) {
	            if (uniqueFolder.mkdir()) {
	                System.out.println("Unique folder created: " + uniqueFolderPath);
	            } else {
	                System.out.println("Failed to create unique folder: " + uniqueFolderPath);
	            }
	        } else {
	            System.out.println("Unique folder already exists: " + uniqueFolderPath);
	        }

	        // Create the permanent subfolder
	        File permanentSubfolder = new File(permanentSubfolderPath);
	        if (!permanentSubfolder.exists()) {
	            if (permanentSubfolder.mkdir()) {
	                System.out.println("Permanent subfolder created: " + permanentSubfolderPath);
	            } else {
	                System.out.println("Failed to create permanent subfolder: " + permanentSubfolderPath);
	            }
	        } else {
	            System.out.println("Permanent subfolder already exists: " + permanentSubfolderPath);
	        }
	        
	      return   permanentSubfolderPath;
	 }
	      
	    
   
}

