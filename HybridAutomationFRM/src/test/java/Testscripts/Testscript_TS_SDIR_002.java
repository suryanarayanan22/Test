package Testscripts;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import ProjectSpecificBase.WebBase;
import pages.OrangeHRMLoginPage;

public class Testscript_TS_SDIR_002 extends WebBase {
	@BeforeTest
	public void setValues() {

		testName = "Login and LoginOut";
		testDescription = "Login for Failure(Negative testCase)";
		category = "test";
		testAuthor = "aravindh";
		executedBy = "Smoke";

	}

	@Test
	public void TS_SDIR_002() throws Exception {

		OrangeHRMLoginPage a = new OrangeHRMLoginPage(driver1);
		a.go_to_URL();
		a.enter_username();
		a.enter_password();
		a.click_login();
		a.verifyhomepage();
		a.click_logout();
		a.logout();

	}
}
