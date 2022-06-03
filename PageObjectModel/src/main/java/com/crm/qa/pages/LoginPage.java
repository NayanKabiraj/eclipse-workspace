package com.crm.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.crm.qa.base.BasePage;

public class LoginPage extends BasePage{
	
	//Page Factory--OBJECT REPOSITORY
	
	@FindBy(name="username")
	WebElement TXT_userName;

	@FindBy(name="password")
	WebElement TXT_password;
	
	@FindBy(xpath="//input[@type='submit']")
	WebElement BTN_login;
	
	
	public LoginPage(WebDriver driver) {
		super(driver); 
	}
	
	
	
	public String getPageTitle() {
		return driver.getTitle();
	}
	public boolean getCrmImageIsDisplayed() {
		return BTN_login.isDisplayed();
	}
	
	public HomePage doLogin() {
		TXT_userName.sendKeys("bcd");
		TXT_password.sendKeys("124324");
		BTN_login.click();
		return new HomePage();
	}
	
	
	
}
