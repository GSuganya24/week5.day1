package week5.day1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

public class DependsOnMethods extends BaseClass_Incident {
	@Test(alwaysRun = true, enabled = true)
	public void createIncident() throws InterruptedException, IOException {
		driver.findElement(By.id("sysverb_new")).click();
		driver.findElement(By.xpath("//span[@class='icon icon-search']")).click();
		// Switch to the newly opened window
		Set<String> windowHandles = driver.getWindowHandles();
		List<String> window1 = new ArrayList<String>(windowHandles);
		driver.switchTo().window(window1.get(1));
		driver.findElement(By.xpath("//td/a")).click();
		Thread.sleep(1000);
		// Moving the control to parent window
		driver.switchTo().window(window1.get(0));
		driver.switchTo().frame("gsft_main");
		driver.findElement(By.xpath("//a[@id='lookup.incident.short_description']")).click();
		//Switching to new window
		Set<String> windowHandles1 = driver.getWindowHandles();
		List<String> window2 = new ArrayList<String>(windowHandles1);
		driver.switchTo().window(window2.get(1));
		driver.findElement(By.xpath("//td/a")).click();
		Thread.sleep(1000);
		driver.switchTo().window(window2.get(0));
		driver.switchTo().frame("gsft_main");
		// Getting the attribute of incident number
		WebElement num = driver.findElement(By.xpath("//input[@id='incident.number']"));
		String incidentNum = num.getAttribute("value");
		System.out.println("Incident Number : " + incidentNum);
		driver.findElement(By.id("sysverb_insert_bottom")).click();
		Thread.sleep(2000);
		WebElement search1 = driver.findElement(By.xpath("//input[@class='form-control']"));
		search1.sendKeys(incidentNum);
		search1.sendKeys(Keys.ENTER);
		Thread.sleep(500);
		inciNum = driver.findElement(By.xpath("//td[@class='vt']/a")).getText();
		if(inciNum.equals(incidentNum)) {
			System.out.println("The incident creation is Successful");
			File source = driver.getScreenshotAs(OutputType.FILE);
			File destination = new File("C:/Users/Rishikesh/Desktop/images/Incident.png");
			FileUtils.copyFile(source, destination);
		}else {
			System.out.println("The incident creation is unsuccessful");
		}
		
	}
//******************Update record***********************//
	@Test(enabled = true, dependsOnMethods = "assignIncident")
	public  void updateIncident() throws InterruptedException {
	//public static void main(String[] args) throws InterruptedException {
		//Enter into the search field
		WebElement search1 = driver.findElement(By.xpath("//input[@class='form-control']"));
		search1.sendKeys(inciNum);
		search1.sendKeys(Keys.ENTER);
		driver.findElement(By.xpath("//table[@id='incident_table']/tbody/tr/td[3]/a")).click();
		Select dd1 = new Select(driver.findElement(By.id("incident.urgency")));
		dd1.selectByValue("1");
		//Select State
		Select dd2 = new Select(driver.findElement(By.id("incident.state")));
		dd2.selectByValue("2");
		driver.findElement(By.id("sysverb_update_bottom")).click();
		Thread.sleep(2000);
		WebElement search2 = driver.findElement(By.xpath("//input[@class='form-control']"));
		search2.sendKeys(inciNum);
		search2.sendKeys(Keys.ENTER);
		Thread.sleep(500);
		driver.findElement(By.xpath("//table[@id='incident_table']/tbody/tr/td[3]/a")).click();
		//Get the values of the fields 'urgency&state' and whether updated
		WebElement urgency = driver.findElement(By.xpath("//select[@id='incident.urgency']//option[@selected='SELECTED']"));
		String urg = urgency.getText();
		System.out.println(urg);
		WebElement state = driver.findElement(By.xpath("//select[@id='incident.state']//option[2][@selected='SELECTED']"));
		String state1 = state.getText();
		System.out.println(state1);
		if(urg.contains("High") && state1.contains("Progress")) {
			System.out.println("The incident is updated");
		}else {
			System.out.println("The incident is not updated");
		}

		
	}
//***********************AssignIncident******************************//
	@Test(enabled = true, dependsOnMethods = "createIncident")
	public void assignIncident() throws InterruptedException {
			driver.switchTo().defaultContent();
			driver.findElement(By.xpath("//div[text()='Open - Unassigned']")).click();
			Thread.sleep(1000);
			//Click the first unassigned record
			driver.switchTo().frame("gsft_main");
			WebElement search1 = driver.findElement(By.xpath("//input[@class='form-control']"));
			search1.sendKeys(inciNum);
			search1.sendKeys(Keys.ENTER);
			driver.findElement(By.xpath("//table[@id='incident_table']/tbody//td[3]/a")).click();
			Thread.sleep(1000);
			//Taking the incident number
//			String inciNum1 = driver.findElement(By.id("incident.number")).getText();
			driver.findElement(By.id("lookup.incident.assignment_group")).click();
			//Opens new window
			Set<String> windowHandles = driver.getWindowHandles();
			List<String> window = new ArrayList<String>(windowHandles);
			driver.switchTo().window(window.get(1));
			WebElement search = driver.findElement(By.xpath("//div[@class='input-group']/input"));
			search.sendKeys("software");
			search.sendKeys(Keys.ENTER);
			Thread.sleep(1000);
			driver.findElement(By.xpath("//table[@id='sys_user_group_table']/tbody/tr/td[3]/a")).click();
			Thread.sleep(1000);
			driver.switchTo().window(window.get(0));
			driver.switchTo().frame("gsft_main");
			driver.findElement(By.id("sysverb_update")).click();
			WebElement search2 = driver.findElement(By.xpath("//input[@class='form-control']"));
			search2.sendKeys(inciNum);
			search2.sendKeys(Keys.ENTER);
			Thread.sleep(500);
			String inciNum2 = driver.findElement(By.xpath("//table[@id='incident_table']/tbody/tr/td[3]/a")).getText();
			String assignmentGrp = driver.findElement(By.xpath("//table[@id='incident_table']/tbody//td[10]/a")).getText();
			if(inciNum==inciNum2) {
				if(assignmentGrp.equals("Software")) {
					System.out.println("The incident is assigned to "+assignmentGrp+" Group");
				}else {
					System.out.println("The incident is assigned to "+assignmentGrp+" Group. But not the Software group");
				}
			}
			
		}
//***********************DeleteIncident****************************//
	@Test(enabled = true, dependsOnMethods = "updateIncident")
	public void deleteIncident() throws InterruptedException {
//	public static void main(String[] args) throws InterruptedException {
		// Enter the frame
		WebElement search1 = driver.findElement(By.xpath("//input[@class='form-control']"));
		search1.sendKeys(inciNum);
		search1.sendKeys(Keys.ENTER);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//table[@id='incident_table']/tbody/tr/td[3]/a")).click();
//		//Taking the incident number
//				String inciNum1 = driver.findElement(By.id("incident.number")).getText();
		driver.findElement(By.id("sysverb_delete")).click();
		driver.findElement(By.xpath("//div[@id='delete_confirm_form']//button[@id='ok_button']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//b[text()='All']")).click();
		WebElement search2 = driver.findElement(By.xpath("//input[@class='form-control']"));
		search2.sendKeys(inciNum);
		search2.sendKeys(Keys.ENTER);
		Thread.sleep(1000);
		String record = driver.findElement(By.xpath("//table[@id='incident_table']/tbody//td")).getText();
		if(record.equals("No records to display")) {
			System.out.println("The record deleted succesfully");
		}else {
			System.out.println("The record deletion is unsuccessful");
		}
	}

}

