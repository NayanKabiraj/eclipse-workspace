package customerinventory.tests;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fastenal.common.utilities.Assertions;
import org.fastenal.common.utilities.CommonUtility;
import org.project.utilities.TestEngine;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import esearch.solutionscustomer.constants.ProjectConstants;
import esearch.solutionscustomer.request.pojos.customerinventorysuggest.Sorts;
import esearch.solutionscustomer.request.pojos.customerinventorysuggest.CustomerInventorySuggestDTO;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
@Test(groups= {"regression"})
public class CustomerInventorySuggestTests  extends TestEngine{

	private static final Logger LOGGER = LogManager.getLogger(CustomerInventorySuggestTests.class.getName());
	RequestSpecification requestSpec;
	Response indexResponse;
	Connection conn;
	private esearch.solutionscustomer.request.pojos.customerinventorysuggest.Parameters params;
	private CustomerInventorySuggestDTO requestObj;
	private Sorts sorts;
	
	@Parameters({"environment","indexUsername","indexPassword"})
	public CustomerInventorySuggestTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec=getReqSpec(ProjectConstants.BASEURI_PROPKEY,ProjectConstants.BASEPATH_CUSTOMERINVENTORY_SUGGEST);
	}
	
	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params=new esearch.solutionscustomer.request.pojos.customerinventorysuggest.Parameters();
		requestObj=new CustomerInventorySuggestDTO();
		sorts=new Sorts();
	}
	
	
	private List<String> getIndexData(){
		List<String> indexDataArr=new ArrayList<String>();
		JsonPath jsonPathObj=getIndexResponse(ProjectConstants.INDEX_BASEURI_PROPKEY,ProjectConstants.INDEX_BASEPATH_CUSTOMER).jsonPath();
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.sku"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryLevelOne"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryLevelTwo"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryLevelThree"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.salesChannels"));
		return indexDataArr;
	}

	
	@Test(groups= {"smoketest"})
	public void searchWithValidRequest() {
		List<String> indexDataArr=getIndexData();
		String[] custNo= {indexDataArr.get(0)};	
		
		
		params.setQuery("zinc");
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerInventorySuggestions", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test(groups= {"smoketest"})
	public void searchWithEmptyCustomerIdInRequest() {		
		
		params.setQuery("zinc");
		params.setCustomerId(new String[] {""});
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test(groups= {"smoketest"})
	public void searchWithoutCustNumberInParameters() {
		List<String> indexDataArr=getIndexData();
		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("400", response.jsonPath().get("responseBody.errors[0].code"), "Status Code in response body Validation");
		Assertions.assertEquals("Customer Id cannot be empty", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithMultipleCustomerIds() {		
		String[] custNo={"ONWIN0460","GAAT70002","CASAM0665"};		
		
		params.setQuery("zinc");
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerInventorySuggestions", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test
	public void searchWithoutQueryInRequest() {
		List<String> indexDataArr=getIndexData();
		String[] custNo= {indexDataArr.get(0)};		
		
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithEmptyQueryInRequest() {
		List<String> indexDataArr=getIndexData();
		String[] custNo= {indexDataArr.get(0)};	
		
		
		params.setQuery("");
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithCustNumberAndQueryWithLessthan3Char() {
		List<String> indexDataArr=getIndexData();
		String[] custNo= {indexDataArr.get(0)};
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		
		params.setQuery("12");
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("400", response.jsonPath().get("responseBody.errors[0].code"), "Status Code in response body Validation");
		Assertions.assertEquals("Parameters must be set", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithCategoryLevel1InRequest() {
		List<String> indexDataArr=getIndexData();
		String[] customerId= {indexDataArr.get(0)};			
		String categoryLevelOne=indexDataArr.get(2);
		params.setQuery("zinc");
		params.setCustomerId(customerId);
		params.setCategoryLevelOne(categoryLevelOne);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerInventorySuggestions", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithCategoryLevel2InRequest() {
		List<String> indexDataArr=getIndexData();
		String[] customerId= {indexDataArr.get(0)};			
		String categoryLevelTwo=indexDataArr.get(3);
		params.setQuery("zinc");
		params.setCustomerId(customerId);		
		params.setCategoryLevelTwo(categoryLevelTwo);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerInventorySuggestions", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithCategoryLevel3InRequest() {
		List<String> indexDataArr=getIndexData();
		String[] customerId= {indexDataArr.get(0)};			
		String categoryLevelThree=indexDataArr.get(4);
		params.setQuery("zinc");
		params.setCustomerId(customerId);
		params.setCategoryLevelThree(categoryLevelThree);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerInventorySuggestions", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithSalesInRequest() {
		List<String> indexDataArr=getIndexData();
		String[] customerId= {indexDataArr.get(0)};			
		String[] salesChannels={indexDataArr.get(5)};
		params.setQuery("zinc");
		params.setCustomerId(customerId);
		params.setSalesChannels(salesChannels);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerInventorySuggestions", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
//	@Test
//	public void searchWithMultipleSalesInRequest() {
//		List<String> indexDataArr=getIndexData();
//		String[] customerId= {indexDataArr.get(0)};			
//		String[] salesChannels={"vending","binstock"};	
//		params.setCustomerId(customerId);
//		params.setSalesChannels(salesChannels);
//		requestObj.setParameters(params);
//		
//		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
//		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
////		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
////		** need to include above assertion
//		Assertions.assertEquals("customerInventorySuggestions", response.jsonPath().get("resultType"), "Result type Validation");
//	}

	
	
	@Test(enabled=true)
	public void jsonSchemaValidation() {
		List<String> indexDataArr=getIndexData();
		String[] custNo= {indexDataArr.get(0)};	
		
		
		params.setQuery("zinc");
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(),"Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_CUSTOMERINVENTORY_SUGGEST, response);
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
}
