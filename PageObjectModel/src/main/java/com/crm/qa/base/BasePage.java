package com.crm.qa.base;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.v85.browser.Browser;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.PageFactory;

import com.crm.qa.util.TestUtil;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;

public class BasePage extends Page{
	protected WebDriver driver;
	public static Properties prop;

	
	
	public BasePage(WebDriver driver) {
		super(driver);
		
	}



	@Override
	public String getPageTitle() {
		return driver.getTitle();
	}



	@Override
	public WebElement getElement(By locator) {
		return driver.findElement(locator);
	}



	@Override
	public void waitforElementPresent(By locator) {
		wait.until(null);
		
	}

	@Override
	public void waitForPageTitle(String title) {
		// TODO Auto-generated method stub
		
	}
		


}
