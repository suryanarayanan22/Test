package pages;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.aventstack.extentreports.ExtentTest;

import  ImplementationBase.WebImplementationBase;


public class OrangeHRMHomePage extends WebImplementationBase {

	

	public OrangeHRMHomePage(WebDriver driver1) {
		super(driver1);
		// TODO Auto-generated constructor stub
	}

	public OrangeHRMHomePage verifyhomepage() throws Exception {
		try {
			waitUntilElementIsDisplayed(30, locator.get("Title"));
			// isDisplayed("xpath", locator.get("Title"));
			if (isDisplayed(locator.get("Title"))) {
				reportStep("The dashboard title is displayed", "pass",driver1);
			}

		} catch (Exception e) {
			reportStep("The dash board title is not displayed and the exception is " + e + "", "fail",driver1);

			throw new RuntimeException();

		}
		return this;
	}

	public OrangeHRMHomePage click_logout() throws IOException {

		try {
			waitUntilElementIsDisplayed(30, locator.get("HRMLogout"));
			clickElement(locator.get("HRMLogout"));
			waitUntilElementIsDisplayed(30, locator.get("HRMLogout"));
			reportStep("The user has clicked HRMLogout", "pass",driver1);
		} catch (Exception e) {

			throw new RuntimeException();

		}
		return this;
	}

	public void logout() throws IOException {

		try {
			waitUntilElementIsDisplayed(30, locator.get("Logout"));
			clickElement(locator.get("Logout"));
			waitUntilElementIsDisplayed(30, locator.get("Logout"));
			reportStep("The user has Logout from HRMWebsite", "pass",driver1);
		} catch (Exception e) {

			throw new RuntimeException();

		}
		
	}

}
