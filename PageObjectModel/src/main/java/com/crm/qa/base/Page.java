package com.crm.qa.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class Page {
	
	
	WebDriver driver;
	protected WebDriverWait wait;
	
	@SuppressWarnings("deprecation")
	public Page(WebDriver driver) {
		this.driver=driver;
		this.wait= new WebDriverWait(this.driver, 15);
		
	} 
	
	public abstract String getPageTitle();
	public abstract WebElement getElement(By locator);
	public abstract void waitforElementPresent(By locator);
	public abstract void waitForPageTitle(String title);

	
	
	public <TPage extends BasePage> TPage getInstance(Class<TPage> pageClass) {
		try {
			return pageClass.getDeclaredConstructor(WebDriver.class).newInstance(this.driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	
}
