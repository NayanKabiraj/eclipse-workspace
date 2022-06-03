/**
 * 
 */
package org.fastenal.common.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsInstanceOf;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import com.jayway.jsonassert.JsonAssert;

import io.restassured.response.Response;

/**
 * @author apagunta
 */
public class Assertions {

	private static final Logger LOGGER = LogManager.getLogger(Assertions.class.getName());
	private static String actualText = " And actual Value is :: ";
	private static String expectedText = "::expected value is :: ";
	private static String pass = "::PASS ";
	private static String fail = "::FAIL ";
	private static String fieldNotPresent = ":Field value is  not present in the response in the mentioned field path";
	private static String fieldPresent = ":Field value is present in the response in the mentioned field path";
	private static String fieldSorted = ":Field is sorted as expected";
	private static String fieldNotSorted = ":Field is not sorted as expected";
	

	Assertions() {

	}

	public static void assertTrue(Boolean flag, String message) {
		if (flag) {
			LOGGER.info(message);
		}
	}

	public static void assertEquals(String expected, String actual, String message) {
		try {
			Assert.assertEquals(expected.trim(), actual.trim());
			LOGGER.info(message + pass + expectedText + expected + actualText + actual, true);
		} catch (Error e) {
			LOGGER.error(message + fail + expectedText + expected + actualText + actual, true);
			Assert.fail(message);
		}
	}

	public static void assertEquals(int expected, int actual, String message) {
		try {
			Assert.assertEquals(expected, actual);
			LOGGER.info(message + pass + expectedText + expected + actualText + actual, true);
		} catch (Error e) {
			LOGGER.error(message + fail + expectedText + expected + actualText + actual, true);
			Assert.fail(message);

		}
	}

	public static void assertNotEquals(String expected, String actual, String message) {
		try {
			Assert.assertNotEquals(expected, actual);
			LOGGER.info(message + pass + "::" + expected + "  is not equal to ->" + actual + " as expected ", true);
		} catch (Error e) {
			LOGGER.error(message + fail + "::" + expected + "  is equal to ->" + actual + " not as expected ", true);
			Assert.fail(message);
		}
	}

	public static void assertNotEquals(int expected, int actual, String message) {
		try {
			Assert.assertNotEquals(expected, actual);
			LOGGER.info(message + pass + "::" + expected + "  is not equal to ->" + actual + " as expected ", true);
		} catch (AssertionError e) {
			LOGGER.error(message + fail + "::" + expected + "  is equal to ->" + actual + " not as expected ", true);
			Assert.fail(message);
		}
	}

	public static void assertGreaterThan(String leftValue, int rightValue, String message) {
		try {
			Assert.assertTrue(Integer.parseInt(leftValue) > rightValue);
			LOGGER.info(message + pass + "::" + leftValue + " is greater than expected value " + rightValue, true);
		} catch (Error e) {
			LOGGER.error(message + fail + "::" + leftValue + " is not greater than expected value " + rightValue, true);
			Assert.fail(message);
		}
	}

	public static void assertLessThan(String leftValue, int rightValue, String message) {
		try {
			Assert.assertTrue(Integer.parseInt(leftValue) < rightValue);
			LOGGER.info(message + pass + "::" + leftValue + " is less than expected value " + rightValue, true);
		} catch (Error e) {
			LOGGER.error(message + fail + "::" + leftValue + " is not less than expected value " + rightValue, true);
			Assert.fail(message);
		}

	}

	public static void assertFilterValue(Response response, String fieldPath, String fieldValue, String message) {
		try {
			JsonAssert.with(response.getBody().asString()).assertThat(fieldPath, Matchers.hasItems(fieldValue));
			LOGGER.info(message + pass + fieldPresent, true);
		} catch (Error e) {
			LOGGER.error(message + fail + fieldNotPresent, true);
			Assert.fail(message, e);
		}

	}

	public static void assertFilterGrtThan(Response response, String fieldPath, String fieldValue, String message) {
		try {
			JsonAssert.with(response.getBody().asString()).assertThat(fieldPath,
					Matchers.greaterThanOrEqualTo(fieldValue));
			LOGGER.info(
					message + pass + ":Field value is greater or equals to the response in the mentioned field path",
					true);
		} catch (Error e) {
			LOGGER.error(
					message + fail + ":Field value is greater or equals to the response in the mentioned field path",
					true);
			Assert.fail(message, e);
		}

	}

	public static void assertFilterValue(Response response, String fieldPath, String[] fieldValue, String message) {
		try {
			JsonAssert.with(response.getBody().asString()).assertThat(fieldPath, Matchers.hasItems(fieldValue));
			LOGGER.info(message + pass + fieldPresent, true);
		} catch (Error e) {
			LOGGER.error(message + fail + fieldNotPresent, true);
			Assert.fail(message, e);
		}

	}

	public static void assertFilterLessThan(Response response, String fieldPath, String fieldValue, String message) {
		try {
			JsonAssert.with(response.getBody().asString()).assertThat(fieldPath,
					Matchers.lessThanOrEqualTo(fieldValue));
			LOGGER.info(message + pass + fieldPresent, true);
		} catch (Error e) {
			LOGGER.error(message + fail + fieldNotPresent, true);
			Assert.fail(message, e);
		}

	}

	public static void assertFilterValue(Response response, String fieldPath, boolean fieldValue, String message) {
		try {
			JsonAssert.with(response.getBody().asString()).assertThat(fieldPath, Matchers.hasItems(fieldValue));
			LOGGER.info(message + pass + fieldPresent, true);
		} catch (Error e) {
			LOGGER.error(message + fail + fieldNotPresent, true);
			Assert.fail(message, e);
		}

	}

	public static void assertFilterValue(Response response, String fieldPath, int fieldValue, String message) {
		try {
			JsonAssert.with(response.getBody().asString()).assertThat(fieldPath, Matchers.hasItems(fieldValue));
			LOGGER.info(message + pass + fieldPresent, true);
		} catch (Error e) {
			LOGGER.error(message + fail + fieldNotPresent, true);
			Assert.fail(message, e);
		}
	}
	
	public static void assertCount(int expected, Response response, String message) {
		int size = response.jsonPath().getList("responseBody.data").size();
		try {

			Assert.assertEquals(expected, size);
			LOGGER.info(message + pass + expectedText + expected + actualText + size, true);
		} catch (Error e) {
			LOGGER.error(message + fail + expectedText + expected + actualText + size, true);
			Assert.fail(message);

		}

	}
	
	public static void assertSort(Response response,String fieldName,String sortOrder,String message) {
    	
    		JSONObject jsonObject=new JSONObject(response.getBody().asString()).getJSONObject("responseBody");
    		JSONArray jsonArray= jsonObject.getJSONArray("data");		
    		
    		Object obj=jsonArray.getJSONObject(0).get(fieldName).getClass();    		
    				
    		if(obj.toString().contains("String")) {
    			List<String> actualFieldValues=new ArrayList<String>();
    			for(int i=0;i<jsonArray.length();i++) {    				
        			actualFieldValues.add((String)jsonArray.getJSONObject(i).get(fieldName));
        		} 
    			assertSortedString(actualFieldValues, sortOrder, message);
    		}   		
    		else if(obj.toString().contains("Double")) {
    			List<Double> actualFieldValues=new ArrayList<Double>();
    			for(int i=0;i<jsonArray.length();i++) {
    				
        			actualFieldValues.add((Double)jsonArray.getJSONObject(i).get(fieldName));
        		} 
    			assertSortedDouble(actualFieldValues, sortOrder, message);
    		}
    		else if(obj.toString().contains("Integer")) {
    			List<Integer> actualFieldValues=new ArrayList<Integer>();
    			for(int i=0;i<jsonArray.length();i++) {    				
        			actualFieldValues.add((Integer)jsonArray.getJSONObject(i).get(fieldName));
        		} 
    			assertSortedInteger(actualFieldValues, sortOrder, message);
    		}
    		
    }
	
	private static void assertSortedString(List<String> actualFieldValues,String sortOrder,String message) {
		try
        {		
		List<String> expectedFieldValues=new ArrayList<>(actualFieldValues);
		if(sortOrder.equals("ASC")) {    			
		Collections.sort(expectedFieldValues);
		}
		else {    			
			Collections.sort(expectedFieldValues,Comparator.reverseOrder());
		}
		Assert.assertEquals(actualFieldValues, expectedFieldValues);        	
        LOGGER.info(message+pass+fieldSorted, true);
    }
    catch (Error e)
    {
    	LOGGER.error(message+fail+fieldNotSorted, true);
    	Assert.fail(message,e);
    }
	}
	
	private static void assertSortedDouble(List<Double> actualFieldValues,String sortOrder,String message) {
		try
        {		

		List<Double> expectedFieldValues=new ArrayList<>(actualFieldValues);
		if(sortOrder.equals("ASC")) {    			
		Collections.sort(expectedFieldValues);
		}
		else {    			
			Collections.sort(expectedFieldValues,Comparator.reverseOrder());
		}
		Assert.assertEquals(actualFieldValues, expectedFieldValues);        	
        LOGGER.info(message+pass+fieldSorted, true);
    }
    catch (Error e)
    {
    	LOGGER.error(message+fail+fieldNotSorted, true);
    	Assert.fail(message,e);
    }
	}
	
	private static void assertSortedInteger(List<Integer> actualFieldValues,String sortOrder,String message) {
		try
        {		
		List<Integer> expectedFieldValues=new ArrayList<>(actualFieldValues);
		if(sortOrder.equals("ASC")) {    			
		Collections.sort(expectedFieldValues);
		}
		else {    			
			Collections.sort(expectedFieldValues,Comparator.reverseOrder());
		}
		Assert.assertEquals(actualFieldValues, expectedFieldValues);        	
        LOGGER.info(message+pass+fieldSorted, true);
    }
    catch (Error e)
    {
    	LOGGER.error(message+fail+fieldNotSorted, true);
    	Assert.fail(message,e);
    }
	}

}
