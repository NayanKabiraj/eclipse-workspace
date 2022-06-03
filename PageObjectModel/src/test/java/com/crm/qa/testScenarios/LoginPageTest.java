package com.crm.qa.testScenarios;

import org.testng.annotations.Test;

import com.crm.qa.base.BasePage;
import com.crm.qa.base.TestBase;
import com.crm.qa.pages.LoginPage;

public class LoginPageTest extends BaseTest{
	
	@Test
	public void verifyTitle() {
		page.getInstance(LoginPage.class).getPageTitle(); 
	}
		

}
