package ProjectSpecificBase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.ArrayList;

import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.*;
import org.openqa.selenium.devtools.events.DomMutationEvent;
import org.openqa.selenium.devtools.v110.log.Log;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.HasLogEvents;
import org.openqa.selenium.manager.SeleniumManager;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import static org.openqa.selenium.devtools.events.CdpEventTypes.domMutation;

import com.github.javafaker.Faker;

import ImplementationBase.WebImplementationBase;
import io.github.bonigarcia.wdm.WebDriverManager;
//import io.github.bonigarcia.wdm.WebDriverManager;
import utils.Web_Html_Reporter;

public class WebBase extends Web_Html_Reporter {
	
public WebImplementationBase basetest ;
public  static WebDriver driver;
public Web_Html_Reporter ReportSetup;
public static Faker fake = new Faker();
public  Web_Html_Reporter logg;
public final int WAIT_FOR_ELEMENT_TIMEOUT = 2;
public  static WebDriver driver1;
public WebDriverWait webDriverWait;
public Actions actions;
public List<DomMutationEvent> mutationsList;
public List<JavascriptException> jsExceptionsList;
public List<String> consoleMessages;

@BeforeSuite
public void Setup() {
	try {
		
		String startdocker = System.getProperty("user.dir") + "/start.bat";
		ProcessBuilder pb = new ProcessBuilder(startdocker);
		pb.start();
		//this needs to be changed while running the docker for first time to download
		Thread.sleep(20000);
		logg = new Web_Html_Reporter();
		logg.startLogging();
			startReport(driver);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}

@BeforeClass
public void reportExecution() {
	report();
	
	
}


	@BeforeMethod
	public void precondition() throws MalformedURLException {
		
			basetest = new WebImplementationBase(driver);
			node = test.createNode(testName);
			LaunchBrowser(basetest.getConfigurations("browser"), basetest.getConfigurations("ChoseLocationToRun"));
			if ("Remote".equalsIgnoreCase(basetest.getConfigurations("ChoseLocationToRun"))) {
			System.out.println("############### Browser Launch###########");
			driver1.manage().window().maximize();
			System.out.println("############### Browser Maximised###########");
		   }else {
			System.out.println("############### Browser Launch###########");
			driver.manage().window().maximize();
			System.out.println("############### Browser Maximised###########");
			
		}

	}

	@AfterMethod(alwaysRun = true)

	public void postcondition() throws Exception {
		if ("Remote".equalsIgnoreCase(basetest.getConfigurations("ChoseLocationToRun"))) {
		driver1.quit();
		}else {
			 // display all mutations
	        	        // print all JS errors
	        for (JavascriptException jsException : jsExceptionsList) {
	            System.out.println("JS exception message: " + jsException.getMessage());
	            jsException.printStackTrace();
	            System.out.println();
	        }

	        System.out.println("###########################");
	        System.out.println();
	        // print all console messages
	        for (var consoleMessage : consoleMessages) {
	            System.out.println(consoleMessage);
	        }

	        // if contains specific message -> fail the test
//	        if (consoleMessages.contains("jquery-migrate.min.js:2 JQMIGRATE: Migrate is installed, version 3.3.2")) {
//	            Assertions.fail();
//	        }

			
			driver.quit();
		}

	}
	
	@AfterSuite(alwaysRun = true)
	public void Flush() {
		stopReport();
		logg.stopLogging();
        List<String> logMessages = logg.getLogMessages();
//        System.out.println("##### The Final Logging "+ logMessages);
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		String logName = timestamp+".log";
        

        try (FileWriter fileWriter = new FileWriter(System.getProperty("user.dir")+"/logs/"+logName)) {
            for (String message : logMessages) {
                fileWriter.write(message + System.lineSeparator());
            }
            System.out.println("Log messages have been saved to the file: " + logName);
        } catch (IOException e) {
            System.err.println("Failed to write log messages to the file: " + logName);
            e.printStackTrace();
        }
        String stopdocker = System.getProperty("user.dir") + "/stop.bat";
		ProcessBuilder pb = new ProcessBuilder(stopdocker);
		 ProcessBuilder pb1 = new ProcessBuilder("taskkill", "/f", "/im", "cmd.exe");
		try {
			//pb.start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void LaunchBrowser(String browser,String ChoseLocation) {
		switch (browser) {
		case "chrome":
			Chrome(ChoseLocation);
			break;

		case "edge":
			Edge();
			break;

		case "firefox":
			Firefox();
			break;

		case "safari":
			//WebDriverManager.safaridriver().setup();
			driver1 = new SafariDriver();
			break;

		default:
			Chrome(ChoseLocation);

		}
	}
	
	
	
	public void Chrome(String ChoseLocation) {
		
		if("Remote".equalsIgnoreCase(ChoseLocation))
		{
		try {
			
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("download.default_directory", System.getProperty("user.dir") + File.separator + "DownloadedFile/Destination/");
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			//options.addArguments("--remote-allow-origins=*");
			
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setBrowserName("chrome");
			//capabilities.setCapability("platform", "WINDOWS");
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			
			// Set the URL of your Selenium Grid Hub
			String gridUrl = "http://localhost:4444/wd/hub"; 
			driver1 = new RemoteWebDriver(new URL(gridUrl), capabilities);
//		    driver.manage().window().maximize();
			
			//WebDriverManager.chromedriver().setup();
			

			//driver = new ChromeDriver(options);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}else if("Local".equalsIgnoreCase(ChoseLocation)) {
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("download.default_directory", System.getProperty("user.dir") + File.separator + "DownloadedFile");
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			options.addArguments("--remote-allow-origins=*");
//			String cpath =SeleniumManager.getInstance().getDriverPath(options)
//			System.out.println(cpath);
          //WebDriverManager.chromedriver().driverVersion("111.0.5563.64 ").setup();
          WebDriverManager.chromedriver().setup();

			driver = new ChromeDriver();
//			driver.manage().window().maximize();
			DevTools tools= ((ChromeDriver) driver).getDevTools() ;
			tools.createSession();
//			tools.getDomains().javascript().pin("notifications","""
//			      function highlight(element){
//			            let defaultBG = element.style.backgroundColor;
//			            let defaultOutline = element.style.outline;
//			            element.style.backgroundColor = '#FDFF47';
//			            element.style.outline = '#f00 solid 2px';
//			        
//			            setTimeout(function()
//			            {
//			                element.style.backgroundColor = defaultBG;
//			                element.style.outline = defaultOutline;
//			            }, 1000);
//			        }
//			        """);
			
		        mutationsList = Collections.synchronizedList(new ArrayList<>());
		        ((HasLogEvents)driver).onLogEvent(domMutation(mutation -> {
		            mutationsList.add(mutation);
		        }));

		        // configure JS exceptions logging
		        jsExceptionsList = Collections.synchronizedList(new ArrayList<>());
		        Consumer<JavascriptException> addEntry = jsExceptionsList::add;
		        tools.getDomains().events().addJavascriptExceptionListener(addEntry);

		        // configure console messages logging
		        consoleMessages = Collections.synchronizedList(new ArrayList<>());
		        tools.send(Log.enable());
		        tools.addListener(Log.entryAdded(),
		                logEntry -> {
		                    consoleMessages.add("log: " + logEntry.getText() + " level: " + logEntry.getLevel());
		                });
			
			var listener = new WebDriverListener() {
			@Override
			public void afterAnyWebElementCall(WebElement element, Method method, Object[] args, Object result) {
				try {
					for (var mutation:mutationsList) {
			            var attributeName = Optional.ofNullable(mutation.getAttributeName()).orElse("");            
			            var oldValue = Optional.ofNullable(mutation.getOldValue()).orElse("");
			            var currentValue = Optional.ofNullable(mutation.getCurrentValue()).orElse("");
			            var elementLocation = Optional.ofNullable(mutation.getElement().toString()).orElse("");
			            System.out.println(String.format(" attr name: %s\n old value = %s\n new value = %s\n element = %s\n\n", attributeName, oldValue, currentValue, elementLocation));
			        }

			        System.out.println("###########################");
			        System.out.println();

					highlighElement(element);
					//System.out.println(String.format("%s called for element %s", method.getName(), element.getTagName()));
					//growlMessage(String.format("%s called for element %s", method.getName(), element.getTagName()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			};
			//driver = new EventFiringDecorator(listener).decorate(driver);	
		driver1 = new EventFiringDecorator<WebDriver>(listener).decorate(driver);
		
	    webDriverWait = new WebDriverWait(driver1, Duration.ofSeconds(WAIT_FOR_ELEMENT_TIMEOUT));
	    actions = new Actions(driver1);
	    
		}
	}
	
	public void Firefox() {
		//WebDriverManager.firefoxdriver().setup();
		HashMap<String, Object> firefoxPrefs = new HashMap<>();
        firefoxPrefs.put("browser.download.folderList", 2);
        firefoxPrefs.put("browser.download.dir", System.getProperty("user.dir") + File.separator + "DownloadedFile");
        
        FirefoxOptions option1 = new FirefoxOptions();
        option1.setCapability("browser.download.folderList", 2);
        option1.setCapability("browser.download.dir", System.getProperty("user.dir") + File.separator + "DownloadedFile");
        option1.setCapability("prefs", firefoxPrefs);
		driver1 = new FirefoxDriver(option1);
		
	}
	
	public void Edge() {
		//WebDriverManager.edgedriver().setup();
		HashMap<String, Object> edgePrefs = new HashMap<>();
        edgePrefs.put("download.default_directory", System.getProperty("user.dir") + File.separator + "DownloadedFile");

        EdgeOptions options2 = new EdgeOptions();
        options2.setCapability("prefs", edgePrefs);
	//	options2.addArguments("--remote-allow-origins=*");

		driver1 = new EdgeDriver(options2);
		
	}
	
	
	public void highlighElement(WebElement element) {
        try {
            ((JavascriptExecutor)driver).executeScript("highlight(arguments[0])", element);
        }
        catch (Exception ex) {
        }
    }

}
