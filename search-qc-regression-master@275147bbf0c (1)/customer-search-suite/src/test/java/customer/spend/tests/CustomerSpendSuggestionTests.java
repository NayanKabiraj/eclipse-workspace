package customer.spend.tests;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.fastenal.common.utilities.Assertions;
import org.fastenal.common.utilities.CommonUtility;
import org.project.utilities.TestEngine;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import esearch.customer.constants.ProjectConstants;
import esearch.customer.request.pojos.customerspendsuggest.CustSpendSuggestDTO;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
@Test(groups= {"regression"})
public class CustomerSpendSuggestionTests extends TestEngine{

	private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(CustomerSpendSuggestionTests.class.getName());
	RequestSpecification requestSpec;
	Connection conn;
	private esearch.customer.request.pojos.customerspendsuggest.Parameters params;
	private CustSpendSuggestDTO requestObj;
	
	@Parameters({"environment","indexUsername","indexPassword"})
	public CustomerSpendSuggestionTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec=getReqSpec(ProjectConstants.BASEURI_PROPKEY,ProjectConstants.BASEPATH_CUST_SPEND_SUGGEST);
	}
	
	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params=new esearch.customer.request.pojos.customerspendsuggest.Parameters();
		requestObj=new CustSpendSuggestDTO();
	}
	

	private List<String> getIndexData(){
		List<String> indexDataArr=new ArrayList<String>();
		JsonPath jsonPathObj=getIndexResponse(ProjectConstants.INDEX_BASEURI_PROPKEY,ProjectConstants.INDEX_BASEPATH_CUSTSUGGEST).jsonPath();
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.sku"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerId"));
		indexDataArr.add("2010-05-30");
		indexDataArr.add("2019-05-30");
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.channel"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerSku"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryOne"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryTwo"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryThree"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.shippingAddressFull"));
		return indexDataArr;
	}
	
//	private List<String> getIndexData(){
//		List<String> indexDataArr=new ArrayList<String>();
//		JsonPath jsonPathObj=getIndexResponse(ProjectConstants.INDEX_BASEURI_PROPKEY,ProjectConstants.INDEX_BASEPATH_CUSTSUGGEST).jsonPath();
//		indexDataArr.add("0170188");
//		indexDataArr.add("NHROC0546");
//		indexDataArr.add("2010-05-30");
//		indexDataArr.add("2019-05-30");
//		return indexDataArr;
//	}
	
	
	@Test(groups= {"smoketest"})
	public void searchWithValidRequest() {
			
		//data set up for request
				List<String> indexData=getIndexData();
				String sku=indexData.get(0);
				String[] customerId= {indexData.get(1)};
				String startDate=indexData.get(2);
				String endDate=indexData.get(3);
				//request construction
				
				params.setQuery(sku);
				params.setStartDate(startDate);
				params.setEndDate(endDate);
				params.setCustomerId(customerId);
				requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
				Assertions.assertEquals("spendSuggestions", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithoutStartDate() {
			
		//data set up for request
				List<String> indexData=getIndexData();
				String sku=indexData.get(0);
				String[] customerId= {indexData.get(1)};				
				String endDate=indexData.get(3);
				//request construction
				
				params.setQuery(sku);				
				params.setEndDate(endDate);
				params.setCustomerId(customerId);
				requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
				Assertions.assertEquals("400", response.jsonPath().get("responseBody.errors[0].code"), "Status Code in response body Validation");
				Assertions.assertEquals("startDate is required", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
				Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithoutEndDate() {
			
		//data set up for request
				List<String> indexData=getIndexData();
				String sku=indexData.get(0);
				String[] customerId= {indexData.get(1)};
				String startDate=indexData.get(2);
				
				//request construction
				
				params.setQuery(sku);
				params.setStartDate(startDate);
				
				params.setCustomerId(customerId);
				requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
				Assertions.assertEquals("400", response.jsonPath().get("responseBody.errors[0].code"), "Status Code in response body Validation");
				Assertions.assertEquals("endDate is required", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
				Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithoutCustomerId() {
			
		//data set up for request
				List<String> indexData=getIndexData();
				String sku=indexData.get(0);
				
				String startDate=indexData.get(2);
				String endDate=indexData.get(3);
				//request construction
				
				params.setQuery(sku);
				params.setStartDate(startDate);
				params.setEndDate(endDate);			
				
				requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
				Assertions.assertEquals("400", response.jsonPath().get("responseBody.errors[0].code"), "Status Code in response body Validation");
				Assertions.assertEquals("customerId is required", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
				Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}	
	
	@Test(enabled=true)
	public void jsonSchemaValidation() {
		List<String> indexData=getIndexData();
		String sku=indexData.get(0);
		String[] customerId= {indexData.get(1)};
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		//request construction
		
		params.setQuery(sku);
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		params.setCustomerId(customerId);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(),"Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_CUSTOMER_SPEND_SUGGEST, response);
	}
	
	// Below test case is not completed
//	@Test
//	public void responseDataValidation() {
//		Response indexResponse=getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,ProjectConstants.INDEX_BASEPATH_BUSINESSUNIT,"buCode");
//		JsonPath indexJsonObj= new JsonPath(indexResponse.jsonPath().get("hits.hits[0]._source").toString());
//		Response apiResponse= getResponse(indexResponse.jsonPath().get("hits.hits[0]._source.buCode"));
//		JsonPath apiJsonObj=new JsonPath(apiResponse.jsonPath().get("responseBody.date[0]").toString());
//		
//	}

	
	@Test
	public void withoutRequest() {	
		Response response=requestSpec.given().body("").post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withEmptyRequest() {	
		Response response=requestSpec.given().body("{}").post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}		
	
	@Test
	public void searchUsingInvalidStartDateFormat() {
			
		List<String> indexData=getIndexData();
		String sku=indexData.get(0);
		String[] customerId= {indexData.get(1)};
//		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		//request construction
		
		params.setQuery(sku);
		params.setStartDate("1234-09-6787");
		params.setEndDate(endDate);
		params.setCustomerId(customerId);			
		requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
				Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchUsingInvalidEndDateFormat() {
		List<String> indexData=getIndexData();
		String sku=indexData.get(0);
		String[] customerId= {indexData.get(1)};
		String startDate=indexData.get(2);
//		String endDate=indexData.get(3);
		//request construction
		
		params.setQuery(sku);
		params.setStartDate(startDate);
		params.setEndDate("1234-09-2394");
		params.setCustomerId(customerId);			
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test
	public void searchWithOnlyCustomerId(){
			
		//data set up for request
				List<String> indexData=getIndexData();
				String[] customerId= {indexData.get(1)};			
				//request construction
								
				params.setCustomerId(customerId);
				requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
				Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithOnlyStartDate() {			
				List<String> indexData=getIndexData();
				String startDate=indexData.get(2);				
				//request construction
				
				params.setStartDate(startDate);				
				requestObj.setParameters(params);				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
				Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithOnlyEndDate() {			
				List<String> indexData=getIndexData();
				String endDate=indexData.get(3);
								
				//request construction
						
				params.setEndDate(endDate);				
				requestObj.setParameters(params);				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
				Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test
	public void searchUsingOnlyStartDateAndEndDate() {
		//data set up for request
				List<String> indexData=getIndexData();
				
				String startDate=indexData.get(2);
				String endDate=indexData.get(3);
				//request construction
				
				params.setStartDate(startDate);
				params.setEndDate(endDate);				
				requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
				Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithStartDateGreaterThanEndDate() {
			
		//data set up for request
				List<String> indexData=getIndexData();
				String sku=indexData.get(0);
				String[] customerId= {indexData.get(1)};
				String startDate=indexData.get(2);
				String endDate=indexData.get(3);
				
				//request construction
				params.setQuery(sku);
				params.setStartDate(endDate);
				params.setEndDate(startDate);
				params.setCustomerId(customerId);
				requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
				Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithEmptyCustomerID() {
			
		//data set up for request
				List<String> indexData=getIndexData();
				String sku=indexData.get(0);
				
				String startDate=indexData.get(2);
				String endDate=indexData.get(3);
				//request construction
				
				params.setQuery(sku);
				params.setStartDate(startDate);
				params.setEndDate(endDate);
				params.setCustomerId(new String[] {""});
				requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
				Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithEmptyStartDate() {
			
		//data set up for request
				List<String> indexData=getIndexData();
				String sku=indexData.get(0);
				String[] customerId= {indexData.get(1)};
				String startDate="";
				String endDate=indexData.get(3);
				//request construction
				
				params.setQuery(sku);
				params.setStartDate(startDate);
				params.setEndDate(endDate);
				params.setCustomerId(customerId);
				requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
				Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithEmptyEndDate() {
			
		//data set up for request
				List<String> indexData=getIndexData();
				String sku=indexData.get(0);
				String[] customerId= {indexData.get(1)};
				String startDate=indexData.get(2);
				String endDate="";
				//request construction
				
				params.setQuery(sku);
				params.setStartDate(startDate);
				params.setEndDate(endDate);
				params.setCustomerId(customerId);
				requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
				Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
public void searchUsingChannelInRequest() {
		
		List<String> indexData=getIndexData();				
		String[] customerId= {indexData.get(1)};
		String[] channels= {indexData.get(4)};
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		
		params.setQuery(customerId[0]);
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		params.setCustomerId(customerId);
		params.setChannel(channels);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].channel", channels,"channels filter validation");
		Assertions.assertEquals("customerSpend", response.jsonPath().get("resultType"), "Result type Validation");
}	
	
	
public void searchUsingCategoryOneInRequest() {
		
		List<String> indexData=getIndexData();	
		String categoryOne= indexData.get(6);
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		String[] customerId= {indexData.get(1)};
		params.setCustomerId(customerId);
		
		params.setQuery(categoryOne);
		params.setStartDate(startDate);
		params.setEndDate(endDate);		
		params.setCategoryOne(categoryOne);
		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("spendSuggestions", response.jsonPath().get("resultType"), "Result type Validation");
	}	

public void searchUsingCategorytwoInRequest() {
	
	List<String> indexData=getIndexData();	
	String categoryTwo= indexData.get(7);
	String startDate=indexData.get(2);
	String endDate=indexData.get(3);
	String[] customerId= {indexData.get(1)};
	params.setCustomerId(customerId);
	
	params.setQuery(categoryTwo);
	params.setStartDate(startDate);
	params.setEndDate(endDate);		
	params.setCategoryTwo(categoryTwo);
	requestObj.setParameters(params);
	
	Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
	Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
	Assertions.assertEquals("spendSuggestions", response.jsonPath().get("resultType"), "Result type Validation");
}

public void searchUsingCategorythreeInRequest() {
	
	List<String> indexData=getIndexData();	
	String categoryThree= indexData.get(8);
	String startDate=indexData.get(2);
	String endDate=indexData.get(3);
	String[] customerId= {indexData.get(1)};
	params.setCustomerId(customerId);
	
	params.setQuery(categoryThree);
	params.setStartDate(startDate);
	params.setEndDate(endDate);		
	params.setCategoryThree(categoryThree);
	requestObj.setParameters(params);
	
	Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
	Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
	Assertions.assertEquals("spendSuggestions", response.jsonPath().get("resultType"), "Result type Validation");
}

public void searchUsingShippingAddressInRequest() {
	
	List<String> indexData=getIndexData();	
	String shippingAddressFull= indexData.get(9);
	String startDate=indexData.get(2);
	String endDate=indexData.get(3);
	String[] customerId= {indexData.get(1)};
	params.setCustomerId(customerId);
	
	params.setQuery(shippingAddressFull);
	params.setStartDate(startDate);
	params.setEndDate(endDate);		
	params.setShippingAddressFull(shippingAddressFull);
	requestObj.setParameters(params);
	
	Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
	Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
	Assertions.assertEquals("spendSuggestions", response.jsonPath().get("resultType"), "Result type Validation");
}

@Test
public void searchWithLowerCaseCustomerId() {
		
	//data set up for request
			List<String> indexData=getIndexData();
			String sku=indexData.get(0);
			String[] customerId= {indexData.get(1).toLowerCase()};
			String startDate=indexData.get(2);
			String endDate=indexData.get(3);
			//request construction
			
			params.setQuery(sku);
			params.setStartDate(startDate);
			params.setEndDate(endDate);
			params.setCustomerId(customerId);
			requestObj.setParameters(params);
			
			Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
			Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//			Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//			Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
			Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
}

@Test
public void searchWithInvalidCustomerId() {
		
	//data set up for request
			List<String> indexData=getIndexData();
			String sku=indexData.get(0);
			String[] customerId= {"VASVDASDD"};
			String startDate=indexData.get(2);
			String endDate=indexData.get(3);
			//request construction
			
			params.setQuery(sku);
			params.setStartDate(startDate);
			params.setEndDate(endDate);
			params.setCustomerId(customerId);
			requestObj.setParameters(params);
			
			Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
			Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//			Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//			Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
			Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
}


	

}
