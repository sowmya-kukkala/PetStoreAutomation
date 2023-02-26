package api.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager implements ITestListener
{
	public ExtentSparkReporter sparkReporter; // UI of the Report (Look and feel of the Report)
	public ExtentReports extent; // Populate Common Info on the Report
	public ExtentTest test; // Creating Test Case Entries in the Report and Update Status of the Test Methods
	
	String repName;
	
	public void onStart(ITestContext context) 
	{
		String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()); // timestamp
		repName = "Test-Report-"+ timestamp+ ".html";
		
		sparkReporter = new ExtentSparkReporter(".\\reports\\"+repName); // specify the location where report will be saved.
		
		sparkReporter.config().setDocumentTitle("RestAssured Automation Report"); // Title of the Report
		sparkReporter.config().setReportName("Pet Store Users API"); // Name of the Report
		sparkReporter.config().setTheme(Theme.DARK);
		
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		
		// User defined Parameters and its values
		extent.setSystemInfo("Application", "Pet Store Users API"); 
		extent.setSystemInfo("Operating System", System.getProperty("os.name"));
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		extent.setSystemInfo("Environment", "QA");
	}
	
	public void onTestSuccess(ITestResult result) 
	{
		test = extent.createTest(result.getName()); // Create a New Entry in the Report
		test.createNode(result.getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.PASS, "Test Passed");
		
	}
	
	public void onTestFailure(ITestResult result) 
	{
		test = extent.createTest(result.getName());
		test.createNode(result.getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.FAIL, "Test Failed");
		test.log(Status.FAIL, result.getThrowable().getMessage());
		
	}
	
	public void onTestSkipped(ITestResult result) 
	{
		test = extent.createTest(result.getName());
		test.createNode(result.getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.SKIP, "Test Skipped");
		test.log(Status.SKIP, result.getThrowable().getMessage());
	}
	
	public void onFinish(ITestContext context) 
	{
		extent.flush(); // Consolidates and Organizes the Report - Without this we can't generate the Report
	}
	
	
}
