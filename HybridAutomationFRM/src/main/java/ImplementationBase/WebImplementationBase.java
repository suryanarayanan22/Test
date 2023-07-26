package ImplementationBase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.github.javafaker.Faker;

import Design.ExcelSpecificActionsForWeb;
import Design.WebSpecificActions;
//import io.github.bonigarcia.wdm.WebDriverManager;
import utils.Web_Html_Reporter;

public class WebImplementationBase extends Web_Html_Reporter implements ExcelSpecificActionsForWeb, WebSpecificActions {

	public  WebDriver driver1;
	public WebDriverWait wait;
	public static Faker fake = new Faker();
	//public ExtentTest node;

	public static HashMap<String, String> locator;
	
	public WebImplementationBase(WebDriver driver1) {
		this.driver1 = driver1;
		
		
	}


	@Override
	public void readLocator(String locatorfilepath, String sheetname) throws IOException {
		locator = new HashMap<String, String>();
		XSSFWorkbook workbook = new XSSFWorkbook(locatorfilepath);
		XSSFSheet sheet = workbook.getSheet(sheetname);
		int rowcount = sheet.getLastRowNum();

		for (int i = 1; i <= rowcount; i++) {
			String stringCellValue = sheet.getRow(i).getCell(2).getStringCellValue();
			String stringCellValue2 = sheet.getRow(i).getCell(3).getStringCellValue();
			locator.put(stringCellValue, stringCellValue2);
			workbook.close();
		}
	}

	@Override
	public String testdataload(String testscriptID, String testdataname) throws IOException {
	
			HashMap<String, Integer> testdataID = new HashMap<String, Integer>();
			String key;
			int value;
			XSSFWorkbook workbook = new XSSFWorkbook(getConfigurations("testdatapath"));
			XSSFSheet sheet = workbook.getSheet("Sheet1");
			int lastCellNum = sheet.getLastRowNum();
			for (int i = 1; i <= lastCellNum; i++) {
				XSSFCell cell = sheet.getRow(i).getCell(0);
				try {
					switch (cell.getCellType()) {
					case NUMERIC:
						double temp = cell.getNumericCellValue();
						long val = (long) temp;
						key = String.valueOf(val);
						if (DateUtil.isCellDateFormatted(cell)) {
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
							Date date = cell.getDateCellValue();
							key = df.format(date);
						}
						break;
					case STRING:
						key = cell.getStringCellValue();
						break;
					case BOOLEAN:
						key = String.valueOf(cell.getBooleanCellValue());
						break;
					case FORMULA:
						key = cell.getCellFormula();
					default:
						key = "DEFAULT";
					}
				} catch (NullPointerException npe) {
					key = " ";

				}

				value = i;
				testdataID.put(key, value);

			}

			HashMap<String, Integer> testdatatitle = new HashMap<String, Integer>();
			int column = sheet.getRow(0).getLastCellNum();
			for (int i = 1; i < column; i++) {
				XSSFCell cell = sheet.getRow(0).getCell(i);
				try {
					switch (cell.getCellType()) {
					case NUMERIC:
						double temp = cell.getNumericCellValue();
						long val = (long) temp;
						key = String.valueOf(val);
						if (DateUtil.isCellDateFormatted(cell)) {
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
							Date date = cell.getDateCellValue();
							key = df.format(date);
						}
						break;
					case STRING:
						key = cell.getStringCellValue();
						break;
					case BOOLEAN:
						key = String.valueOf(cell.getBooleanCellValue());
						break;
					case FORMULA:
						key = cell.getCellFormula();
					default:
						key = "DEFAULT";
					}
				} catch (NullPointerException npe) {
					key = " ";

				}
				value = i;
				testdatatitle.put(key, value);

			}

			String expected_value = sheet.getRow(testdataID.get(testscriptID)).getCell(testdatatitle.get(testdataname))
					.getStringCellValue();
			return expected_value;
		}
	

	@Override
	public WebElement locateElement(String locator) {
		String[] split = locator.split("=", 2);
		String locatortype = split[0];
		String lvalue = split[1];
		switch (locatortype.toLowerCase()) {
		case "id":
			return driver1.findElement(By.id(lvalue));
		case "xpath":
			return driver1.findElement(By.xpath(lvalue));

		case "name":
			return driver1.findElement(By.name(lvalue));
		}
		return null;

	}

	@Override
	public boolean elementIsDisplayed(String locator) {
		return locateElement(locator).isDisplayed();

	}

	@Override
	public boolean verifyElementtext(String locator, String expected) {
		boolean text = false;
		String name = locateElement(locator).getText();
		if (name.equals(expected)) {
			text = true;

		} else {
			text = false;
		}
		return text;
	}

	@Override
	public boolean verifyElementContainsText(String locator,String expected) {
		boolean text = false;
		String name = locateElement(locator).getText();
		if (name.contains(expected)) {
			text = true;

		} else {
			text = false;
		}
		return text;
	}

	@Override
	public void clickElement(String locator) {
		wait = new WebDriverWait(driver1, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.elementToBeClickable(locateElement(locator)));
		locateElement(locator).click();

	}

	@Override
	public void EnterInput(String locator, String text) {
		wait = new WebDriverWait(driver1, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.elementToBeClickable(locateElement(locator)));
		WebElement ele = locateElement(locator);
		ele.clear();
		ele.sendKeys(text);

	}

	@Override
	public String getElementText(String locator) {

		String text = locateElement(locator).getText();
		return text;
	}

	@Override
	public Boolean isEnabled(String locator) {
		return locateElement(locator).isEnabled();
	}

	@Override
	public Boolean isSelected(String locator) {
		return locateElement(locator).isDisplayed();
	}

	@Override
	public void clearInput(String locator) {
		locateElement(locator).clear();

	}

	@Override
	public boolean isDisplayed(String locator) {
		boolean displayed = locateElement(locator).isDisplayed();
		return displayed;

	}

	@Override
	public void selectDropDownByVisibleText(String locator, String visibletext) {
		WebElement ele = locateElement(locator);
		Select drp = new Select(ele);
		drp.selectByVisibleText(visibletext);

	}

	@Override
	public void selectDropDownByValue(String locator, String value) {
		WebElement ele = locateElement(locator);
		Select drp = new Select(ele);
		drp.selectByValue(value);

	}

	@Override
	public void selectDropdownByindex(String locator, int index) {
		WebElement ele = locateElement(locator);
		Select drp = new Select(ele);
		drp.selectByIndex(index);

	}

	@Override
	public String getElementColour(String locator) {
		WebElement ele = locateElement(locator);
		String cssValue = ele.getCssValue("color");
		return cssValue;

	}

	@Override
	public void switchToFrameUsingIndex(int index) {
		driver1.switchTo().frame(index);

	}

	@Override
	public void switchToFrameUsingLocator(String locator) {
		WebElement ele = locateElement(locator);
		driver1.switchTo().frame(ele);

	}

	@Override
	public void switchToDefaultContent() {
		driver1.switchTo().defaultContent();

	}

	@Override
	public void switchToWindowUsingIndex(int index) {
		Set<String> windowHandles = driver1.getWindowHandles();
		List<String> Handles = new ArrayList<>(windowHandles);
		driver1.switchTo().window(Handles.get(index));
	}

	@Override
	public void switchToWindowUsingTitle(String title) {
		Set<String> windowHandles = driver1.getWindowHandles();
		List<String> Handles = new ArrayList<>(windowHandles);
		for (String string : Handles) {
			if (string.equals(title.toLowerCase())) {
				driver1.switchTo().window(string);
			}
		}
	}

	@Override
	public String getCurrentWindowTitle(int index) {
		return driver1.getTitle();

	}

	@Override
	public void acceptAlert() {
		Alert alert = driver1.switchTo().alert();
		alert.accept();
	}

	@Override
	public void dismissAlert() {
		Alert alert = driver1.switchTo().alert();
		alert.dismiss();
	}

	@Override
	public String getAlertText() {
		Alert alert = driver1.switchTo().alert();
		String text = alert.getText();
		return text;

	}

	@Override
	public void doubleClick(String locator) {
		WebElement ele = locateElement(locator);
		Actions act = new Actions(driver1);
		act.doubleClick(ele).perform();

	}

	@Override
	public void dragAndDrop(String locator, String locator2) {
		WebElement source = locateElement(locator);
		WebElement target = locateElement(locator2);
		Actions act = new Actions(driver1);
		act.dragAndDrop(source, target).perform();

	}

	@Override
	public void sendAlertText(String alerttext) {
		Alert alert = driver1.switchTo().alert();
		alert.sendKeys(alerttext);
	}

	@Override
	public void waitUntilInvisibilityOfElement(int waitime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver1, Duration.ofSeconds(waitime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilElementIsClickable(int waitime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.elementToBeClickable(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.elementToBeClickable(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilElementIsDisplayed(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver1, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilPresenceOfAllElementsLocated(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver1, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilElementToBeSelected(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver1, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.elementToBeSelected(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.elementToBeSelected(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.elementToBeSelected(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilPresenceofElementlocated(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver1, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilVisisblityOfAllElementsLocated(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver1, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilInvisibliltyOfAllElementsLocated(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver1, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(lvalue)));
		}
	}

	@Override
	public void waituntilFrameIsVisibleAndSwitchToIt(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver1, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(lvalue)));
		}

	}

	

	public String getConfigurations(String key) {

		String value = null;
		try {
			FileInputStream file = new FileInputStream("./src/main/resources/configurations/config.properties");
			Properties properties = new Properties();
			try {
				properties.load(file);
				value = properties.getProperty(key);

			} catch (Exception e) {
				System.out.println(e);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return value;
	}

	@Override
	public void clickElement(String locator, int waittime) {
		wait = new WebDriverWait(driver1, Duration.ofSeconds(waittime));
		wait.until(ExpectedConditions.elementToBeClickable(locateElement(locator)));
		locateElement(locator).click();
		
	}

	@Override
	public void EnterInput(String locator, String text, int waittime) {
		wait = new WebDriverWait(driver1, Duration.ofSeconds(waittime));
		wait.until(ExpectedConditions.elementToBeClickable(locateElement(locator)));
		locateElement(locator).sendKeys(text);
		
		
	}
	@Override
	public void generateData() {
		 fake = new Faker();
		 Random random = new Random();
		 int randomInt = random.nextInt(100);
	     double randomDouble = random.nextDouble();
	     boolean randomBoolean = random.nextBoolean();

	    }
	@Override
	public void DownloadAndCompare(String locator,String DocumentType,String ExpectedFile,String SourceFile){
		try {
			locateElement(locator).click();
			Thread.sleep(1000);
			switch(DocumentType){
			case "txt":
				TXT_CSV(ExpectedFile,SourceFile);
				break;
			  
			case "PDF":
				comparePDF(SourceFile,ExpectedFile);
				break;
				  
			case "DOCX":
				compareDOCX(SourceFile,ExpectedFile);
				break;
			case "Excel":
				compareExcel(SourceFile,ExpectedFile);
			default:
				System.out.println("Please enter the valid extenstion:");
				System.out.println("Choose from following extenstion [txt,PDF,DOCX]");
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void autoitfileupload(String documentname, String element) {
		String filepath = System.getProperty("user.dir") + "\\ToUpload\\" + documentname;
	
//		String autoITpath = System.getProperty("user.dir") + "\\auto\\fileupload.exe";
		String autoITpath = System.getProperty("user.dir") + "\\AutoIT\\cUpload.exe";
		
		System.out.println("AutoIT Location:" + autoITpath);
		String location = filepath;
		try {
			if (isEnabled(element)) {
				clickElement(element);
				System.out.println("Upload button is clicked");
				ProcessBuilder pb = new ProcessBuilder(autoITpath, location);
				pb.start();
				System.out.println("File Uploaded FileName: " + documentname);
			}
		} catch (Exception e) {
			System.out.println("failed to upload file" + documentname);
			//writeToLogFile("ERROR", "failed to upload file" + documentname + ".ERROR: " + e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
        public  void TXT_CSV(String ExpectedFile,String SourceFile) {
		try {			
			BufferedReader Sourcereader = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/DownloadedFile/Destination/"+ExpectedFile));
			String line;			
			StringBuilder SourFileContent = new StringBuilder();
			while ((line = Sourcereader.readLine()) != null) {
				SourFileContent.append(line);			    
			}
			Sourcereader.close();						
		    BufferedReader Desreader = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/DownloadedFile/Source/"+SourceFile));
		    String line1;				
				StringBuilder DesfileContent = new StringBuilder();
				while ((line1 = Desreader.readLine()) != null) {
					DesfileContent.append(line1);				    
				}
				Desreader.close();									
			if (SourFileContent.toString().contains(DesfileContent.toString())) {
				try {
					reportStep("The Source text: "+SourFileContent+"/n Destination text are same: "+DesfileContent, "pass",driver1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					reportStep("The Source text: "+SourFileContent+"/n Destination text are not same: "+SourFileContent, "fail",driver1);
				} catch (Exception e) {					
					e.printStackTrace();
				}
			}
			System.out.println("Content inside the file "+SourFileContent.toString());
		} catch (Exception e) {			
			e.printStackTrace();
		} 
		}
        
	 public  void comparePDF(String sourceFileName, String destinationFileName) {
	    try (PDDocument sourceDocument = PDDocument.load(new File(System.getProperty("user.dir") + "/DownloadedFile/Source/" + sourceFileName));
	         PDDocument destinationDocument = PDDocument.load(new File(System.getProperty("user.dir") + "/DownloadedFile/Destination/" + destinationFileName))) {
	        PDFTextStripper sourceTextStripper = new PDFTextStripper();
	        PDFTextStripper destinationTextStripper = new PDFTextStripper();
	        String sourceText = sourceTextStripper.getText(sourceDocument);
	        String destinationText = destinationTextStripper.getText(destinationDocument);
	        if (sourceText.equals(destinationText)) {
	        	try {
					reportStep("The Source text: "+sourceText+"/n Destination text are same: "+destinationText, "pass",driver1);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
	        } else {
	        	try {
					reportStep("The Source text: "+sourceText+"/n Destination text are not same: "+destinationText, "fail",driver1);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	 
	 public  void compareDOCX(String sourceFileName, String destinationFileName) {
		    try (XWPFDocument sourceDocument = new XWPFDocument(new FileInputStream(System.getProperty("user.dir") + "/DownloadedFile/Source/" + sourceFileName));
		         XWPFDocument destinationDocument = new XWPFDocument(new FileInputStream(System.getProperty("user.dir") + "/DownloadedFile/Destination/" + destinationFileName))) {
		        XWPFWordExtractor sourceExtractor = new XWPFWordExtractor(sourceDocument);
		        XWPFWordExtractor destinationExtractor = new XWPFWordExtractor(destinationDocument);
		        String sourceText = sourceExtractor.getText();
		        String destinationText = destinationExtractor.getText();
		        if (sourceText.equals(destinationText)) {
		        	try {
						reportStep("The Source text: "+sourceText+"/n Destination text are same: "+destinationText, "pass",driver1);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
		        } else {
		        	try {
						reportStep("The Source text: "+sourceText+"/n Destination text are not same: "+destinationText, "fail",driver1);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
		        }
		        
		        sourceExtractor.close();
		        destinationExtractor.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	 
	 
	 
	 public void compareExcel(String sourceFileName, String destinationFileName) throws Exception {
		    try (Workbook sourceWorkbook = WorkbookFactory.create(new FileInputStream(System.getProperty("user.dir") + "/DownloadedFile/" + sourceFileName));
		         Workbook destinationWorkbook = WorkbookFactory.create(new FileInputStream(System.getProperty("user.dir") + "/DownloadedFile/" + destinationFileName))) {        
		        Sheet sourceSheet = sourceWorkbook.getSheetAt(0);
		        Sheet destinationSheet = destinationWorkbook.getSheetAt(0);
		        int sourceRowCount = sourceSheet.getPhysicalNumberOfRows();
		        int destinationRowCount = destinationSheet.getPhysicalNumberOfRows();
		        if (sourceRowCount != destinationRowCount) {
		            System.out.println("Test Step fail");
		            return;
		        }
		        for (int i = 0; i < sourceRowCount; i++) {
		            Row sourceRow = sourceSheet.getRow(i);
		            Row destinationRow = destinationSheet.getRow(i);
		            int sourceCellCount = sourceRow.getPhysicalNumberOfCells();
		            int destinationCellCount = destinationRow.getPhysicalNumberOfCells();
		            if (sourceCellCount != destinationCellCount) {
		            	reportStep("The Source text: "+sourceFileName+"/n Destination text are not same: "+destinationFileName, "fail",driver1);
		                return;
		            }
		            for (int j = 0; j < sourceCellCount; j++) {
		            	String sourceCell = sourceRow.getCell(j).toString();
		                String destinationCell = destinationRow.getCell(j).toString();
		                //String sourceValue = getCellValueAsString(sourceCell);
		               // String destinationValue = getCellValueAsString(destinationCell);
		                if (!sourceCell.equals(destinationCell)) {
		                	reportStep("The Source text: "+sourceFileName+"/n Destination text are not same: "+destinationFileName, "fail",driver1);
		                    return;
		                }
		            }
		        }

		        reportStep("The Source text: "+sourceFileName+"/n Destination text are same: "+destinationFileName, "pass",driver1);

		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	 
	 public void waitUntilElementPresent(int waittime, String locator) {
			String[] split = locator.split("=",2);
			String locatortype = split[0];
			String lvalue = split[1];
			wait = new WebDriverWait(driver1, Duration.ofSeconds(waittime));
			if (locatortype.toLowerCase().contains("xpath")) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(lvalue)));
			}
			if (locatortype.toLowerCase().contains("name")) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name(lvalue)));
			}
			if (locatortype.toLowerCase().contains("id")) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.id(lvalue)));
			}

		}

		
}	
	
  

	
	

