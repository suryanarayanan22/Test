package pages;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.aventstack.extentreports.ExtentTest;
import com.github.javafaker.Name;

import ImplementationBase.WebImplementationBase;


public class OrangeHRMLoginPage extends WebImplementationBase {

	

	public OrangeHRMLoginPage(WebDriver driver1) {
		super(driver1);
		// TODO Auto-generated constructor stub
	}

	public OrangeHRMLoginPage go_to_URL() throws Exception {
		try {
			driver1.get(getConfigurations("url"));
			reportStep("The user has entered the URL", "pass",driver1);

		} catch (Exception e) {
			reportStep("The user was not able to enter the username", "fail",driver1);
			throw new RuntimeException();

		}
		return this;
	}

	public OrangeHRMLoginPage enter_username() throws Exception {

		try {
			waitUntilElementIsDisplayed(20,locator.get("usernamefield"));
			EnterInput(locator.get("usernamefield"),testdataload("TS_SDIR_001", "username"));
			 String name =fake.name().fullName();
			 waitUntilElementIsDisplayed(20,locator.get("usernamefield"));
			reportStep("The user has entered the username "+ name, "pass",driver1);
			
		} catch (Exception e) {
			reportStep("The user was not able to enter the username because of the exception " + e + "", "fail",driver1);

			// System.out.println(e);
			throw new RuntimeException();

		}
		return this;
	}

	public OrangeHRMLoginPage enter_password() throws IOException {

		try {
			waitUntilElementIsDisplayed(30, locator.get("passwordfield"));
			EnterInput(locator.get("passwordfield"), "admin123");
			waitUntilElementIsDisplayed(20,locator.get("passwordfield"));
			reportStep("The user has entered the password", "pass",driver1);
		} catch (Exception e) {

			throw new RuntimeException();

		}
		return this;
	}

	public void click_login() throws IOException {

		try {
			waitUntilElementIsDisplayed(30, locator.get("Login"));
			waitUntilElementPresent(30, locator.get("Login"));
			clickElement(locator.get("Login"));
			//waitUntilElementIsDisplayed(20,locator.get("Login"));
			reportStep("The user has logged in", "pass",driver1);
		} catch (Exception e) {

			throw new RuntimeException();

		}
		
	}
	public void verifyhomepage() throws Exception {
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
		
	}
	
	public void click_logout() throws IOException {

		try {
			waitUntilElementIsDisplayed(30, locator.get("HRMLogout"));
			clickElement(locator.get("HRMLogout"));
			reportStep("The user has clicked HRMLogout", "pass",driver1);
		} catch (Exception e) {

			throw new RuntimeException();

		}
	}

	public void logout() throws IOException {

		try {
			waitUntilElementIsDisplayed(30, locator.get("Logout"));
			clickElement(locator.get("Logout"));
			reportStep("The user has Logout from HRMWebsite", "pass",driver1);
		} catch (Exception e) {

			throw new RuntimeException();

		}
		
	}

}
