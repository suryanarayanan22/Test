package utils;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import ImplementationBase.WebImplementationBase;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;




public class Web_Html_Reporter {
	
	public static ExtentTest test, node;
	
	public WebDriverWait wait;
	//RemoteWebDriver driver;
	public static ExtentReports extent;
	public static WebImplementationBase basepage;
	public static String testName, testDescription, category, testAuthor, testNodes;
	public static String  executedBy;	
	public static String folderName = "";
	public static String fileName = "result.html";
	public  PrintStream originalSysOut;
	public ByteArrayOutputStream outputStream;
	public  PrintStream customSysOut;
	public  List<String> logMessages;

    public Web_Html_Reporter() {
	        logMessages = new ArrayList<>();
	        outputStream = new ByteArrayOutputStream();
	        customSysOut = new PrintStream(outputStream);
	    }

	    public  void startLogging() {
	        originalSysOut = System.out;
	        System.setOut(new DualPrintStream(originalSysOut, customSysOut));
	    }

	    public  void stopLogging() {
	        System.setOut(originalSysOut);
	        customSysOut.close();
	        logMessages.clear();
	    }

	    public List<String> getLogMessages() {
	        String[] lines = outputStream.toString().split(System.lineSeparator());
	        for (String line : lines) {
	            logMessages.add(line);
	           // System.out.println(logMessages+"*******************");
	        }
	        return logMessages;
	    }

	
	public static void startReport(WebDriver driver)  {
		try {
			basepage = new WebImplementationBase(driver);
			executedBy = basepage.getConfigurations("executedBy");
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			folderName = "reports/" + timeStamp;

			File folder = new File("./" + folderName);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			basepage.readLocator(basepage.getConfigurations("locatorpath"), "Sheet1");

			extent = new ExtentReports();
			ExtentSparkReporter reporter = new ExtentSparkReporter("./" + folderName + "/" + fileName);
			reporter.loadXMLConfig(basepage.getConfigurations("xmlconfigpath"));
			extent.attachReporter(reporter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public static void report()  {
		try {
			test = extent.createTest(testName, testDescription);
			test.assignAuthor(testAuthor);
			test.assignCategory(category);
			test.assignCategory(executedBy);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String takeSnap(WebDriver driver) {
		String destination = null;
		try {
		String data = new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
		
		Screenshot source = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(driver);
		// Save the screenshot to the specified file path
        
        destination = System.getProperty("user.dir")+ "/Screenshot/"+ data + ".png";
		
        //
        ImageIO.write(source.getImage(), "PNG", new File(destination));
      
       
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		//return destination;

      return destination;
        
        

//		int randomNum = (int) (Math.random() * 999999 + 999999);
//		File source = driver.getScreenshotAs(OutputType.FILE);
//		File target = new File("./" + folderName + "/images/" + randomNum + ".jpg");
//		FileUtils.copyFile(source, target);
//		String absolutePath = target.getAbsolutePath();
//		return absolutePath;

	}
	
	
	public static void reportStep(String msg, String status,WebDriver driver) throws Exception {
		
        System.out.println(msg);
         
              String screenshot = takeSnap(driver);
		if (status.equalsIgnoreCase("pass")) {
			//test.log
			test.log(Status.PASS,msg, MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());
			//node.pass(msg, MediaEntityBuilder.createScreenCaptureFromPath(takeSnap(driver)).build());

		} else if (status.equalsIgnoreCase("fail")) {
			test.log(Status.FAIL,msg, MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());
			//node.fail(msg, MediaEntityBuilder.createScreenCaptureFromPath(takeSnap(driver)).build());

		} else if (status.equalsIgnoreCase("info")) {
			test.log(Status.INFO,msg, MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());
			//node.info(msg, MediaEntityBuilder.createScreenCaptureFromPath(takeSnap(driver)).build());

		}

	}

	
	public  void stopReport() {
		extent.flush();
		
	}
	
	private static class DualPrintStream extends PrintStream {
        private final PrintStream second;

        public DualPrintStream(PrintStream first, PrintStream second) {
            super(first);
            this.second = second;
        }

        @Override
        public void write(byte[] buf, int off, int len) {
            super.write(buf, off, len);
            second.write(buf, off, len);
        }

        @Override
        public void flush() {
            super.flush();
            second.flush();
        }

        @Override
        public void close() {
            super.close();
            second.close();
        }
    }

}
