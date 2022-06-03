package org.fastenal.common.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class Listener implements ITestListener, ISuiteListener, IInvokedMethodListener
{
	private static final Logger LOGGER = LogManager.getLogger(Listener.class.getName());
    // This belongs to ISuiteListener and will execute before the Suite start

    @Override
    public void onStart(ISuite arg0)
    {
    	
        LOGGER.info("*******************Suite ::" + arg0.getName() + " Execution started*******************", true);

    }

    // This belongs to ISuiteListener and will execute, once the Suite is finished

    @Override
    public void onFinish(ISuite arg0)
    {
        LOGGER.info("**************************************************************************************", true);

    }

    // This belongs to ITestListener and will execute before starting of Test set/batch

    public void onStart(ITestContext arg0)
    {
        LOGGER.info("--------------Class ::" + arg0.getClass().getSimpleName() + " Execution started-------------",
                     true);

    }

    // This belongs to ITestListener and will execute, once the Test set/batch is finished

    public void onFinish(ITestContext arg0)
    {

    	LOGGER.info("----------------Class ::" + arg0.getClass().getSimpleName() + " Execution ends-------------",
                     true);    	
    }

    // This belongs to ITestListener and will execute only when the test is pass

    public void onTestSuccess(ITestResult arg0)
    {

        // This is calling the printTestResults method

        printTestResults(arg0);

    }

    // This belongs to ITestListener and will execute only on the event of fail test

    public void onTestFailure(ITestResult arg0)
    {

        // This is calling the printTestResults method

        printTestResults(arg0);

    }

    // This belongs to ITestListener and will execute before the main test start (@Test)

    public void onTestStart(ITestResult arg0)
    {

        // Reporter.log("Test Case ::" arg0.getClass())

    }

    // This belongs to ITestListener and will execute only if any of the main test(@Test)
    // get skipped

    public void onTestSkipped(ITestResult arg0)
    {

        printTestResults(arg0);

    }


    // This is the method which will be executed in case of test pass or fail

    // This will provide the information on the test

    private void printTestResults(ITestResult result)
    {

        String status = null;

        switch (result.getStatus())
        {

            case ITestResult.SUCCESS:

                status = "Pass";

                break;

            case ITestResult.FAILURE:

                status = "Failed";

                break;

            case ITestResult.SKIP:

                status = "Skipped";
                break;
                
            default:
            	status="Pass";

        }

        LOGGER.info("Test Status ::" + status);

    }

    // This belongs to IInvokedMethodListener and will execute before every method
    // including @Before @After @Test

    public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1)
    {
        LOGGER.info("~~~~~~~~~~~~~~TEST CASE :: " + arg0.getTestMethod().getMethodName()
                     + " Execution started~~~~~~~~~~~~~~", true);

    }

    // This belongs to IInvokedMethodListener and will execute after every method
    // including @Before @After @Test

    public void afterInvocation(IInvokedMethod arg0, ITestResult arg1)
    {
    	LOGGER.info("~~~~~~~~~~~~~~TEST CASE :: " + arg0.getTestMethod().getMethodName()
                     + " Execution ends~~~~~~~~~~~~~~", true);
    }

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// This method is not implemented yet
	}

}