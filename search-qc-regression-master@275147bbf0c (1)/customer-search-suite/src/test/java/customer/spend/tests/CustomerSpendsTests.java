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
import esearch.customer.request.pojos.customerspend.CustSpendDTO;
import esearch.customer.request.pojos.customerspend.Sorts;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
@Test(groups= {"regression"})
public class CustomerSpendsTests extends TestEngine{

	private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(CustomerSpendsTests.class.getName());
	RequestSpecification requestSpec;
	Connection conn;
	private esearch.customer.request.pojos.customerspend.Parameters params;
	private CustSpendDTO requestObj;
	private Sorts sorts;

	@Parameters({"environment","indexUsername","indexPassword"})
	public CustomerSpendsTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec=getReqSpec(ProjectConstants.BASEURI_PROPKEY,ProjectConstants.BASEPATH_CUST_SPEND);
	}
	
	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params=new esearch.customer.request.pojos.customerspend.Parameters();
		requestObj=new CustSpendDTO();
		sorts=new Sorts();
	}
	
	
	private List<String> getIndexData(){
		List<String> indexDataArr=new ArrayList<String>();
		JsonPath jsonPathObj=getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,ProjectConstants.INDEX_BASEPATH_CUSTSUGGEST,"customerSku").jsonPath();
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
	
	@Test(groups= {"smoketest"})
	public void searchWithValidRequest() {
			
		//data set up for request
				List<String> indexData=getIndexData();
				
				String[] customerId= {indexData.get(1)};
				String startDate=indexData.get(2);
				String endDate=indexData.get(3);
				//request construction
				
				params.setStartDate(startDate);
				params.setEndDate(endDate);
				params.setCustomerId(customerId);
				requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
				Assertions.assertEquals("customerSpend", response.jsonPath().get("resultType"), "Result type Validation");
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
				Assertions.assertEquals("startDate and endDate are required", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
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
				Assertions.assertEquals("startDate and endDate are required", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
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
				Assertions.assertEquals("Customer Id cannot be empty", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
				Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test(enabled=true)
	public void jsonSchemaValidation() {
		List<String> indexData=getIndexData();
		
		String[] customerId= {indexData.get(1)};
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		//request construction
		
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		params.setCustomerId(customerId);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(),"Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_CUSTOMER_SPEND, response);
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
	public void withPageAsZero() {
		List<String> indexData=getIndexData();
		
		String[] customerId= {indexData.get(1)};
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		//request construction
		
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		params.setCustomerId(customerId);
		requestObj.setParameters(params);	
		requestObj.setPage("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withNegativePage() {
		List<String> indexData=getIndexData();
		
		String[] customerId= {indexData.get(1)};
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		//request construction
		
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		params.setCustomerId(customerId);
		requestObj.setParameters(params);
		requestObj.setPage("-3");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withPageSizeAsZero() {
		List<String> indexData=getIndexData();
		
		String[] customerId= {indexData.get(1)};
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		//request construction
		
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		params.setCustomerId(customerId);
		requestObj.setParameters(params);
		requestObj.setPageSize("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerSpend", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void withNegativePageSize() {
		List<String> indexData=getIndexData();
		
		String[] customerId= {indexData.get(1)};
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		//request construction
		
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		params.setCustomerId(customerId);
		requestObj.setParameters(params);
		requestObj.setPageSize("-7");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	
	@Test
	public void pageAndPageSizeValidation() {
		List<String> indexData=getIndexData();
		String[] customerId= {indexData.get(1)};
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		//request construction
		
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		params.setCustomerId(customerId);
		requestObj.setPage("2");
		requestObj.setPageSize("4");
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerSpend", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertCount(4, response, "PageSize Validation");
		
	}
	
	@Test
	public void validateResponseBySortingSkuInAsc() {
		List<String> indexData=getIndexData();
		String[] customerId= {indexData.get(1)};
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		//request construction
		
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		params.setCustomerId(customerId);
		
		
		sorts.setSku("ASC");
		
		requestObj.setParameters(params);
		requestObj.setSorts(sorts);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerSpend", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertSort(response, "sku", "ASC", "Verifying if response sorted in ascending order");
	}
	
	@Test
	public void validateResponseBySortingSkuInDesc() {
		List<String> indexData=getIndexData();
		String[] customerId= {indexData.get(1)};
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		//request construction
		
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		params.setCustomerId(customerId);
		
		
		sorts.setSku("DESC");
		
		requestObj.setParameters(params);
		requestObj.setSorts(sorts);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerSpend", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertSort(response, "sku", "DESC", "Verifying if response sorted in descending order");
	}
	
	@Test
	public void validateResponseBySortingSkuDescriptionInAsc() {
		List<String> indexData=getIndexData();
		String[] customerId= {indexData.get(1)};
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		//request construction
		
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		params.setCustomerId(customerId);
		
		
		sorts.setSkuDescription("ASC");
		
		requestObj.setParameters(params);
		requestObj.setSorts(sorts);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerSpend", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertSort(response, "skuDescription", "ASC", "Verifying if response sorted in ascending order");
	}
	
	@Test
	public void validateResponseBySortingSkuDescriptionInDesc() {
		List<String> indexData=getIndexData();
		String[] customerId= {indexData.get(1)};
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		//request construction
		
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		params.setCustomerId(customerId);
		
		sorts.setSkuDescription("DESC");
		
		requestObj.setParameters(params);
		requestObj.setSorts(sorts);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerSpend", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertSort(response, "skuDescription", "DESC", "Verifying if response sorted in descending order");
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
	public void requestWithEmptyParameters() {
				
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
				
				String[] customerId= {indexData.get(1)};
				String startDate=indexData.get(2);
				String endDate=indexData.get(3);
				//request construction
				
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
	public void searchUsingInvalidCustomerID() {
			
		//data set up for request
				List<String> indexData=getIndexData();
				
				String[] customerId=new String[]{"AVINASH01"};
				String startDate=indexData.get(2);
				String endDate=indexData.get(3);
				//request construction
				
				params.setStartDate(startDate);
				params.setEndDate(endDate);
				params.setCustomerId(customerId);
				requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
				Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	
	@Test
	public void searchUsingLowerCaseCustomerID() {
			
		//data set up for request
				List<String> indexData=getIndexData();				
				String[] customerId= {indexData.get(0).toLowerCase()};
				String startDate=indexData.get(2);
				String endDate=indexData.get(3);
				//request construction
				
				params.setStartDate(startDate);
				params.setEndDate(endDate);
				params.setCustomerId(customerId);
				requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
				Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test
	public void searchUsingInvalidStartDateFormat() {
			
		//data set up for request
				List<String> indexData=getIndexData();				
				String[] customerId= {indexData.get(0).toLowerCase()};
				
				String endDate=indexData.get(3);
				//request construction
				
				params.setStartDate("1209-90-90");
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
			
		//data set up for request
				List<String> indexData=getIndexData();				
				String[] customerId= {indexData.get(0).toLowerCase()};
				String startDate=indexData.get(2);
				
				//request construction
				
				params.setStartDate(startDate);
				params.setEndDate("1209-90-90");
				params.setCustomerId(customerId);
				requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
				Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test
	public void searchUsingEmptyCustomerNumber() {
			
		//data set up for request
				List<String> indexData=getIndexData();				
				String[] customerId= {""};
				String startDate=indexData.get(2);
				String endDate=indexData.get(3);
				//request construction
				
				params.setStartDate(startDate);
				params.setEndDate(endDate);
				params.setCustomerId(customerId);
				requestObj.setParameters(params);
				
				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
				Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//				Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
				Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test
	public void searchUsingEmptyStartDate() {
			
		//data set up for request
				List<String> indexData=getIndexData();				
				String[] customerId= {indexData.get(0)};
				String startDate="";
				String endDate=indexData.get(3);
				//request construction
				
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
	public void searchUsingEmptyEndDate() {
			
		//data set up for request
				List<String> indexData=getIndexData();				
				String[] customerId= {indexData.get(0)};
				String startDate=indexData.get(2);
				String endDate="";
				//request construction
				
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
	
	public void searchUsingSkuInRequest() {
		
		List<String> indexData=getIndexData();				
		String sku= indexData.get(0);
//		String[] channels= {indexData.get(4)};
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		String[] customerId= {indexData.get(1)};
		
		params.setStartDate(startDate);
		params.setEndDate(endDate);		
		params.setSku(sku);
		params.setCustomerId(customerId);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].channel", channels,"channels filter validation");
		Assertions.assertEquals("customerSpend", response.jsonPath().get("resultType"), "Result type Validation");
	}	
	
	
	public void searchUsingChannelInRequest() {
		
		List<String> indexData=getIndexData();				
		
		String[] channels= {indexData.get(4)};
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		String[] customerId= {indexData.get(1)};
		params.setCustomerId(customerId);
		
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		
		params.setChannel(channels);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].channel", channels,"channels filter validation");
		Assertions.assertEquals("customerSpend", response.jsonPath().get("resultType"), "Result type Validation");
}	

	
	public void searchUsingCustomerSkuInRequest() {
		
		List<String> indexData=getIndexData();	
		String customerSku= indexData.get(5);
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		String[] customerId= {indexData.get(1)};
		params.setCustomerId(customerId);
		
		params.setStartDate(startDate);
		params.setEndDate(endDate);		
		params.setCustomerSku(customerSku);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerSpend", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerSku", customerSku,"Customer SKU filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
		
	}	
	
	
	
	
public void searchUsingCategoryOneInRequest() {
		
		List<String> indexData=getIndexData();	
		String categoryOne= indexData.get(6);
		String startDate=indexData.get(2);
		String endDate=indexData.get(3);
		
		String[] customerId= {indexData.get(1)};
		params.setCustomerId(customerId);
		params.setStartDate(startDate);
		params.setEndDate(endDate);		
		params.setCategoryOne(categoryOne);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryOne", categoryOne,"Category One filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
		Assertions.assertEquals("customerSpend", response.jsonPath().get("resultType"), "Result type Validation");
	}	

public void searchUsingCategorytwoInRequest() {
	
	List<String> indexData=getIndexData();	
	String categoryTwo= indexData.get(7);
	String startDate=indexData.get(2);
	String endDate=indexData.get(3);
	
	String[] customerId= {indexData.get(1)};
	params.setCustomerId(customerId);
	params.setStartDate(startDate);
	params.setEndDate(endDate);		
	params.setCategoryTwo(categoryTwo);
	requestObj.setParameters(params);
	
	Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
	Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
	Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryTwo", categoryTwo,"Category two filter validation");
//	Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
	Assertions.assertEquals("customerSpend", response.jsonPath().get("resultType"), "Result type Validation");
}

public void searchUsingCategorythreeInRequest() {
	
	List<String> indexData=getIndexData();	
	String categoryThree= indexData.get(8);
	String startDate=indexData.get(2);
	String endDate=indexData.get(3);
	
	String[] customerId= {indexData.get(1)};
	params.setCustomerId(customerId);
	params.setStartDate(startDate);
	params.setEndDate(endDate);		
	params.setCategoryThree(categoryThree);
	requestObj.setParameters(params);
	
	Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
	Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
	Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryThree", categoryThree,"Category three filter validation");
//	Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
	Assertions.assertEquals("customerSpend", response.jsonPath().get("resultType"), "Result type Validation");
}

public void searchUsingShippingAddressInRequest() {
	
	List<String> indexData=getIndexData();	
	String shippingAddressFull= indexData.get(9);
	String startDate=indexData.get(2);
	String endDate=indexData.get(3);
	
	String[] customerId= {indexData.get(1)};
	params.setCustomerId(customerId);
	params.setStartDate(startDate);
	params.setEndDate(endDate);		
	params.setShippingAddressFull(shippingAddressFull);
	requestObj.setParameters(params);
	
	Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
	Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
	Assertions.assertFilterValue(response, "$.responseBody.data[*].shippingAddressFull", shippingAddressFull,"Category three filter validation");
//	Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerId,"customerId filter validation");
	Assertions.assertEquals("customerSpend", response.jsonPath().get("resultType"), "Result type Validation");
}
	
//	@Test
//	public void searchUsingMultipleCustomersInRequest() {
//				List<String> indexData=getIndexData();				
//				String[] customerId= {indexData.get(0)};
//				String startDate=indexData.get(2);
//				String endDate="";
//				
//				params.setStartDate(startDate);
//				params.setEndDate(endDate);
//				params.setCustomerId(customerId);
//				requestObj.setParameters(params);
//				
//				Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
//				Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//				Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
//	}
	
	
	
	
}
