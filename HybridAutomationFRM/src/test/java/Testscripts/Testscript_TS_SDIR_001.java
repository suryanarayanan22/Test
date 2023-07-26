package Testscripts;

import org.openqa.selenium.StaleElementReferenceException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import ProjectSpecificBase.WebBase;
import pages.OrangeHRMLoginPage;

public class Testscript_TS_SDIR_001 extends WebBase {

	@BeforeTest
	public void setValues() {

		testName = "Login and LoginOut";
		testDescription = "Login for Failure";
		category = "test";
		testAuthor = "aravindh";
		

	}

	@Test
	public void TS_SDIR_001() throws Exception {
		OrangeHRMLoginPage a = new OrangeHRMLoginPage(driver1);
		
		a.go_to_URL();
		
		a.enter_username();
		
		a.enter_password();
		 int maxRetryAttempts = 3;
	        for (int retryAttempt = 1; retryAttempt <= maxRetryAttempts; retryAttempt++) {
	            try {
	            	a.click_login();
	                break;
	            } catch (StaleElementReferenceException e) {
	                System.out.println("Attempt " + retryAttempt + ": Element is stale. Retrying...");
	            }
	        }
		
		
		a.verifyhomepage();
		

	}

}
