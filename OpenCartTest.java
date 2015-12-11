package com.lookingglass.dashboard;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * ---- OpenCart tests ---- 
 * These tests cover the following test cases:
 * 
 * 1. The error message for an invalid login has "Please login again" in the warning div element.
 * 2. A correct login redirects you to a dashboard, and.. That changing the range in the 'Statistics' dropdown loads a new chart.
 * 3. That the Sales>Orders contains 20 elements in it's Orders table, each with a link to View that item, and that the Order ID in the view page matches the Order ID in the table.
 * 4. That logging out takes you back to the login page and returning the to url of the dashboard shows a login page.
 * 
 * @author Manish Dube
 */

public class OpenCartTest  {
	
	String XPATH_loginButtonId = ".//*[@id='content']/div/div/div/div/div[2]/form/div[3]/button";

	WebDriver driver = new FirefoxDriver();
	
	@Test
	public void invalidLogin() throws InterruptedException{

		//	This test will verify that error message for an invalid login has "Please login again" in the warning div element
		//	using the userId: demo and pwd: demo1 

		// navigate to the home page
		driver.get("http://demo.opencart.com/admin/");
		driver.manage().window().maximize();
	
		//	Thread.sleep(2500);		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
		
		// locate login id textbox
		WebElement loginid = driver.findElement(By.name("username"));

		// clear the default text and provide test case username
		loginid.clear();		
		loginid.sendKeys("demo");			

		// locate pwd id textbox
		WebElement pwdid = driver.findElement(By.name("password"));

		// clear the default text and provide an incorrect pwd
		pwdid.clear();		
		pwdid.sendKeys("demo1");	
		
		//locate and click "Submit" button
		//String loginButtonId = "btn.btn-primary";
//		String loginButtonId = ".//*[@id='content']/div/div/div/div/div[2]/form/div[3]/button";
		WebElement loginButton = driver.findElement(By.xpath(XPATH_loginButtonId));

		//click the submit button
		loginButton.click();

		String XPATH_invalid_login_error_msg = "//*[@id='content']/div/div/div/div/div[2]/div";
		
		WebDriverWait wait1 = new WebDriverWait(driver, 10);
		wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_invalid_login_error_msg)));
		//Assert that the warning appears on the screen
		Assert.assertTrue(driver.findElement(By.xpath(XPATH_invalid_login_error_msg)).getText().contains("No match for Username and/or Password."));		
	}	 
	
	@Test( dependsOnMethods = { "invalidLogin" } ) 
	public void validLogin(){

		//	This test will verify that you can access the page specified in the test and login
		//	using the userId: demo and pwd: demo 

		// navigate to the home page
		driver.get("http://demo.opencart.com/admin/");

		// locate login id textbox
		WebElement loginid = driver.findElement(By.name("username"));

		// clear the default text and provide test case username
		loginid.clear();		
		loginid.sendKeys("demo");			

		// locate pwd id textbox
		WebElement pwdid = driver.findElement(By.name("password"));

		// clear the default text and provide a correct pwd
		pwdid.clear();		
		pwdid.sendKeys("demo");	
		
		//locate and click "Submit" button		
		String XPATH_loginButtonId = ".//*[@id='content']/div/div/div/div/div[2]/form/div[3]/button";
		WebElement loginButton = driver.findElement(By.xpath(XPATH_loginButtonId));

		//click the submit button
		loginButton.click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		String XPATH_dashboardtxtId = "/html/body/div/div/div/div/h1";
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_dashboardtxtId)));
		
		//Assert that a redirect happens and the Dashboard text is displayed.
		Assert.assertTrue(driver.findElement(By.className("container-fluid")).isDisplayed());
		assertFieldValidationTextByXPath(driver, XPATH_dashboardtxtId, "Dashboard");		
	}	  
	
	@Test( dependsOnMethods = { "validLogin" } ) 
	public void verifySalesAnalyticsDropdownText(){

		// verify "verifySalesAnalyticsDropdownText" dropdown items text	
		Assert.assertFalse(driver.findElement(By.xpath("html/body/div[1]/div/div[2]/div[2]/div[2]/div/div[1]/div/ul/li[1]/a")).getText().contains("Today"));
		Assert.assertFalse(driver.findElement(By.xpath("html/body/div[1]/div/div[2]/div[2]/div[2]/div/div[1]/div/ul/li[2]/a")).getText().contains("Week"));
		Assert.assertFalse(driver.findElement(By.xpath("html/body/div[1]/div/div[2]/div[2]/div[2]/div/div[1]/div/ul/li[3]/a")).getText().contains("Month"));
		Assert.assertFalse(driver.findElement(By.xpath("html/body/div[1]/div/div[2]/div[2]/div[2]/div/div[1]/div/ul/li[4]/a")).getText().contains("Year"));
	}
	
	@Test( dependsOnMethods = { "verifySalesAnalyticsDropdownText" } ) 
	public void verifyExtensionsMenuItems(){
         
        Actions builder = new Actions(driver);
        WebElement Element_Mouseover =  driver.findElement(By.xpath("//li[@id = 'extension']"));
		builder.moveToElement(Element_Mouseover ).build().perform();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		assertFieldValidationTextByXPath(driver, "/html/body/div/nav/ul/li[3]/ul/li[5]/a", "Payments");
		
//	//	WebElement menu_element = driver.findElement(By.xpath("/html/body/div[1]/nav/ul/li"));
//		List<WebElement> menuItems = driver.findElements(By.xpath("/html/body/div[1]/nav/ul/li"));
//
//		System.out.println(menuItems.size());
//	
//		for (int i = 1; i <=menuItems.size(); i++){
//			System.out.println("in i");
//			String XPATH_menuItem = String.format("/html/body/div[1]/nav/ul/li[%s]", i);
//			List<WebElement> menuItem = driver.findElements(By.xpath(XPATH_menuItem));
//			
//			System.out.println(XPATH_menuItem);
//			System.out.println(driver.findElement(By.xpath(XPATH_menuItem)).getText());
//			System.out.println(menuItem.size());
//			
//			for (int j = 1; i <=menuItem.size(); j++){
//				System.out.println("in j");
//				String XPATH_subMenuItem = XPATH_menuItem + String.format("/ul/li[%s]", j);
//				System.out.println(XPATH_subMenuItem);
//				System.out.println(driver.findElement(By.xpath(XPATH_subMenuItem)).getText());
//				
////				List<WebElement> subSubMenuItems = driver.findElements(By.xpath(XPATH_menuItem));
////				System.out.println(subMenuItems.size());
////				
////				for (int k = 1; j <=subMenuItems.size(); k++){
////	//				List<WebElement> subSubMenuItems = driver.findElements(By.xpath(String.format("(String.format("/html/body/div[1]/nav/ul/li[%s]/ul/li", i)",j)));
////					System.out.println("in k");
////					
////				}
//			}		
//		}				
	}	
			
	@Test( dependsOnMethods = { "verifyExtensionsMenuItems" } ) 
	public void verifyEachOrder() throws InterruptedException{
				
		// XPATH strings
		String XPATH_shopping_cart_menu_item = "html/body/div/nav/ul/li[4]/a/i";		
		String XPATH_orders_menu_item = "/html/body/div/nav/ul/li[4]/ul/li[1]/a";
		String XPATH_orderIDSalesPage = "html/body/div/div/div[2]/div/div[2]/div/div[1]/table/tbody/tr[1]/td[2]";		
		String XPATH_orderIDGrid = "html/body/div[1]/div/div[2]/div/div[2]/form/div/table/tbody/tr[%s]/td[2]";
		String XPATH_orders = "/html/body/div/div/div/div/h1";
		String XPATH_viewLink = "/html/body/div/div/div[2]/div/div[2]/form/div/table/tbody/tr[%s]/td[8]/a[1]";
		String XPATH_ordersPagelink = "/html/body/div/div/div/div/ul/li[2]/a";
		String XPATH_table_element = "//table[@class='table table-bordered table-hover']/tbody";
		
        Actions builder = new Actions(driver);
        WebElement Element_Mouseover =  driver.findElement(By.xpath(XPATH_shopping_cart_menu_item));
		builder.moveToElement(Element_Mouseover ).build().perform();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
//		WebElement shopping_cart_menu_item = driver.findElement(By.xpath(XPATH_shopping_cart_menu_item));
//		//click the shopping_cart 
//		shopping_cart_menu_item.click();
//
//		WebDriverWait wait0 = new WebDriverWait(driver, 10);
//		wait0.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_shopping_cart_menu_item)));
		
		WebDriverWait wait1 = new WebDriverWait(driver, 10);
		wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_orders_menu_item)));
//		Assert.assertTrue(driver.findElement(By.xpath(XPATH_orders_menu_item)).isDisplayed());
//		assertFieldValidationTextByXPath(driver, XPATH_orders_menu_item, "Orders");
		
		WebElement orders_menu_item = driver.findElement(By.xpath(XPATH_orders_menu_item));
		//click the orders_menu_item 
		orders_menu_item.click();

		WebDriverWait wait2 = new WebDriverWait(driver, 10);
		wait2.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_orders)));
		Assert.assertTrue(driver.findElement(By.xpath(XPATH_orders)).isDisplayed());
		assertFieldValidationTextByXPath(driver, XPATH_orders, "Orders");
		
		WebElement table_element = driver.findElement(By.xpath(XPATH_table_element));
		List<WebElement> rows = table_element.findElements(By.tagName("tr"));
			
		for (int i = 1; i <=rows.size(); i++){
				
			WebElement orderId = driver.findElement(By.xpath(String.format(XPATH_orderIDGrid, i)));
			String orderIdActualTxt = String.format("#%s", orderId.getText());
				
			driver.findElement(By.xpath(String.format(XPATH_viewLink, i))).click();
			Thread.sleep(2000);
			//wait for page to load
			WebDriverWait wait3 = new WebDriverWait(driver, 10);			
			WebElement orderIDSalesPage = wait3.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(XPATH_orderIDSalesPage))));
			
			System.out.println(orderIDSalesPage.getText());
			Assert.assertEquals(orderIdActualTxt, orderIDSalesPage.getText());
		
			WebElement ordersPagelink = driver.findElement(By.xpath(XPATH_ordersPagelink));
			ordersPagelink.click();
		    //wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("list"))));
			Thread.sleep(3000);
		}				
	}		
	
	@Test( dependsOnMethods = { "verifyEachOrder" } ) 
	public void logOut(){

		WebElement logOutButton = driver.findElement(By.linkText("Logout"));
		
	//	System.out.println(logOutButton.getText()); 
		logOutButton.click(); 
		
//		WebDriverWait wait = new WebDriverWait(driver, 10);
//		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div/div/div/div/div/div/h1")));
		Assert.assertTrue(driver.findElement(By.xpath("/html/body/div/div/div/div/div/div/div/h1")).getText().contains("Please enter your login details."));			
	}
	
	public static void assertFieldValidationTextByXPath(WebDriver driver, String xpath, String expectString) {
		WebElement valdiationMessageEle = driver.findElement(By.xpath(xpath));
		String value = valdiationMessageEle.getText();
		Assert.assertEquals(value, expectString);
	}
	
	@AfterClass
	public void teardownFixture() {
		driver.quit();		
	} 
}
