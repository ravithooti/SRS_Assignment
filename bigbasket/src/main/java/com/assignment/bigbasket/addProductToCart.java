package com.assignment.bigbasket;



import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class addProductToCart {
	//declaring driver object 
	public static WebDriver driver;
	
	//capturing web elements
	public static String title = "Online Grocery Shopping and Online Supermarket in India - bigbasket";
	public static String shopByCatogeryLink = "//a[text()=' Shop by Category ']";
	public static String brandSearchBox = "//*[@id=\"filterbar\"]/div[4]/div[2]/div/input";

	public static void main(String[] args){
		try {
			
			launchBigBasket();
			verifyPageTitle(driver, title);
			
			//creating object for actions class and providing driver object information
			Actions actions = new Actions(driver);
			
			mouseHover(actions, driver.findElement(By.xpath(shopByCatogeryLink)));
			mouseHover(actions, driver.findElement(By.linkText("Beverages")));
			mouseHover(actions, driver.findElement(By.linkText("Tea")));
			
			//clicking on Green Tea in options
			driver.findElement(By.linkText("Green Tea")).click();

			//Entering text in search box
			driver.findElement(By.xpath(brandSearchBox)).sendKeys("Tea");

			//Getting elements count presented in Brand catogery
			List<WebElement> brandsList = driver.findElements(
					By.xpath("(//h4/span[text()='Brand']//../following-sibling::section)[1]//input[@type='checkbox']/following-sibling::span/i"));
			
			//Clicking on first item from the list
			brandsList.get(0).click();
			Thread.sleep(2000);
			brandsList = driver.findElements(
					By.xpath("(//h4/span[text()='Brand']//../following-sibling::section)[1]//input[@type='checkbox']/following-sibling::span/i"));
			
			//Clicking on last item from the list
			brandsList.get(brandsList.size() - 1).click();
			Thread.sleep(5000);
			
			//Getting count of products displayed in the view
			System.out.println("Product Results Count : "
					+ driver.findElements(By.xpath("//div[@class='items']/div[@qa='product']")).size());
			
			//Capturing Title of the product
			String productTitle = driver.findElement(By.xpath("//div[@class='items']/div[@qa='product']//a[@class='ng-binding']")).getText();
			
			//Getting price of the product
			String price = driver.findElement(By.xpath("//div[@class='items']/div[@qa='product']//div[@qa='price']//span[@class='discnt-price']/span")).getText();
			
			//Selecting number of items for the product
			driver.findElement(By.xpath("//div[@class='items']/div[@qa='product']//div[@qa='qty']/input")).clear();
			driver.findElement(By.xpath("//div[@class='items']/div[@qa='product']//div[@qa='qty']/input")).sendKeys("2");
			
			//Adding items to cart
			driver.findElement(By.xpath("//button[@qa='add']")).click();
			
//String addSuccessMsg = driver.findElement(By.xpath("//div[@id='toast-container']//div[@class='toast-title']")).getText();
//A pop-up is displayed instead of message for adding products to carts, did not verified success message and continued with the flow to complete other validations.
			
			
			//Handling pop-up related to location
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", driver.findElement(By.linkText("Continue")));
			

			//Accessing cart - 'My basket'
			mouseHover(actions, driver.findElement(By.xpath("//a[@qa='myBasket']")));
			Thread.sleep(2000);
			
			//Getting list of items present in cart
			List<WebElement> cartItemsList =  driver.findElements(By.xpath("//a[@qa='myBasket']/following-sibling::ul/li"));
			
			WebElement cartProductLink = cartItemsList.get(0).findElement(By.xpath("descendant::a[@qa='prodNameMB']"));

			//Verifying required product added to cart
			Assert.assertTrue("Product added to cart successfully", cartProductLink.getText().contains(productTitle));
			
			//capturing and displaying value of the product
			String totalCost = cartItemsList.get(cartItemsList.size()-2).findElement(By.xpath("descendant::p/span/span")).getText();
			System.out.println("Single Item Cost : "+price);
			System.out.println("Summary Total Cost : "+totalCost);
			Float expected = Float.parseFloat(price)*2;
			Float actual = Float.parseFloat(totalCost);
			
			//Verifying updated product price in cart
			Assert.assertTrue("Summary Total Cost is matching", actual.compareTo(expected)==0);
			
			//Closing browser instance
			driver.quit();

		}
		catch(Exception e) {
			System.out.println("Exception occurred while execution: "+e.getMessage());
		}
			}

	public static void launchBigBasket() {
		//Method to initialize browser and launch application
		try {
			System.setProperty("webdriver.chrome.driver", "lib\\chromedriver.exe");
			driver = new ChromeDriver();

			driver.get("https://www.bigbasket.com/");
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);

		}
		catch(Exception e)
		{
			System.out.println("Exception occured while initializing browser"+e.getMessage());
		}
		
	}

	public static void verifyPageTitle(WebDriver driver, String expTitle) {
		//Method to verify title of landing page
		try {
			if (driver.getTitle().equalsIgnoreCase(expTitle))
				System.out.println("Able to launch Big basket application");
			else
				System.out.println("Unable to launch Big basket application");
		}
		catch(Exception e)
		{
			System.out.println("Exception occurred while validating page title."+e.getMessage());
		}
		
	}
	
	public static void mouseHover(Actions actions, WebElement element) {
		//method to perform mouse hover
		try {
			actions.moveToElement(element).build().perform();
		}
		catch(Exception e) {
			System.out.println("Exception occurred while performing mousehover"+e.getMessage());
		}
		
	}
}
