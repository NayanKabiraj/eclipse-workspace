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
import esearch.solutionscustomer.request.pojos.customerinventory.CustomerInventoryDTO;
import esearch.solutionscustomer.request.pojos.customerinventory.Sorts;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
@Test(groups= {"regression"})
public class CustomerInventoryTests extends TestEngine{

	private static final Logger LOGGER = LogManager.getLogger(CustomerInventoryTests.class.getName());
	RequestSpecification requestSpec;
	Response indexResponse;
	Connection conn;
	private esearch.solutionscustomer.request.pojos.customerinventory.Parameters params;
	private CustomerInventoryDTO requestObj;
	private Sorts sorts;
	
	@Parameters({"environment","indexUsername","indexPassword"})
	public CustomerInventoryTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec=getReqSpec(ProjectConstants.BASEURI_PROPKEY,ProjectConstants.BASEPATH_CUSTOMERINVENTORY);
	}
	
	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params=new esearch.solutionscustomer.request.pojos.customerinventory.Parameters();
		requestObj=new CustomerInventoryDTO();
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
	
	@Test
	public void searchWithoutCustNumberInParameters() {
		List<String> indexDataArr=getIndexData();
		String sku=indexDataArr.get(1);
		
		params.setSku(sku);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("400", response.jsonPath().get("responseBody.errors[0].code"), "Status Code in response body Validation");
		Assertions.assertEquals("Customer Id cannot be empty", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithValidRequest() {
		List<String> indexDataArr=getIndexData();
		String[] custNo= {indexDataArr.get(0)};
		String sku=indexDataArr.get(1);		
		
		
		params.setSku(sku);
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerInventoryList", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test(groups= {"smoketest"})
	public void searchWithMultipleCustomers() {
		//HArd coded the below values as current index fetches only one document
		String[] custNo={"ONWIN0460","GAAT70002","CASAM0665"};		
		
		params.setQuery("bolt");
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerInventoryList", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithEmptyRequest() {
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
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
		params.setCustomerId(customerId);
		params.setCategoryLevelOne(categoryLevelOne);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerInventoryList", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithCategoryLevel2InRequest() {
		List<String> indexDataArr=getIndexData();
		String[] customerId= {indexDataArr.get(0)};			
		String categoryLevelTwo=indexDataArr.get(3);
		params.setCustomerId(customerId);
		params.setCategoryLevelTwo(categoryLevelTwo);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerInventoryList", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithCategoryLevel3InRequest() {
		List<String> indexDataArr=getIndexData();
		String[] customerId= {indexDataArr.get(0)};			
		String categoryLevelThree=indexDataArr.get(4);
		params.setCustomerId(customerId);
		params.setCategoryLevelThree(categoryLevelThree);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerInventoryList", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithSalesInRequest() {
		List<String> indexDataArr=getIndexData();
		String[] salesChannels={indexDataArr.get(5)};
		String[] customerId= {indexDataArr.get(0)};	
		params.setCustomerId(customerId);
		params.setSalesChannels(salesChannels);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerInventoryList", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithMultipleSalesInRequest() {
		List<String> indexDataArr=getIndexData();
		String[] customerId= {indexDataArr.get(0)};			
		String[] salesChannels={"vending","binstock"};	
		params.setCustomerId(customerId);
		params.setSalesChannels(salesChannels);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerInventoryList", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test(enabled=true)
	public void jsonSchemaValidation() {
		List<String> indexDataArr=getIndexData();
		String sku=indexDataArr.get(1);
		String[] customerId= {indexDataArr.get(0)};	
		params.setCustomerId(customerId);
		params.setSku(sku);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(),"Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_CUSTOMERINVENTORY, response);
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
		List<String> indexDataArr=getIndexData();
		String sku=indexDataArr.get(1);
		String[] customerId= {indexDataArr.get(0)};	
		params.setCustomerId(customerId);
		params.setSku(sku);
		requestObj.setParameters(params);	
		requestObj.setPage("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void withNegativePage() {
		List<String> indexDataArr=getIndexData();
		String sku=indexDataArr.get(1);
		String[] customerId= {indexDataArr.get(0)};	
		params.setCustomerId(customerId);
		params.setSku(sku);
		requestObj.setParameters(params);
		
		requestObj.setPage("-3");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void withPageSizeAsZero() {
		List<String> indexDataArr=getIndexData();
		String sku=indexDataArr.get(1);
		String[] customerId= {indexDataArr.get(0)};	
		params.setCustomerId(customerId);
		params.setSku(sku);
		requestObj.setParameters(params);
		requestObj.setPageSize("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerInventoryList", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void withNegativePageSize() {
		List<String> indexDataArr=getIndexData();
		String sku=indexDataArr.get(1);
		String[] customerId= {indexDataArr.get(0)};	
		params.setCustomerId(customerId);
		params.setSku(sku);
		requestObj.setParameters(params);
		requestObj.setPageSize("-7");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	
//	@Test
//	public void pageAndPageSizeValidation() {
//		List<String> indexDataArr=getIndexData();
//		String sku=indexDataArr.get(1);
//		String[] customerId= {indexDataArr.get(0)};	
//		params.setCustomerId(customerId);
//		params.setSku(sku);
//		requestObj.setParameters(params);
//		requestObj.setPage("2");
//		requestObj.setPageSize("4");
//		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
//		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertEquals("customerInventoryList", response.jsonPath().get("resultType"), "Result type Validation");
//		Assertions.assertCount(4, response, "PageSize Validation");
//	}
	
	@Test
	public void validateResponseBySortingSkuInAsc() {
		List<String> indexDataArr=getIndexData();
		String sku=indexDataArr.get(1);
		
		String[] customerId= {indexDataArr.get(0)};	
		params.setCustomerId(customerId);
		params.setSku(sku);
		
		
		sorts.setSku("ASC");
		
		requestObj.setParameters(params);
		requestObj.setSorts(sorts);
	
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerInventoryList", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertSort(response, "sku", "ASC", "Verifying if response sorted in ascending order");
	}
	
	@Test
	public void validateResponseBySortingSkuInDesc() {
		List<String> indexDataArr=getIndexData();
		String sku=indexDataArr.get(1);
		
		String[] customerId= {indexDataArr.get(0)};	
		params.setCustomerId(customerId);
		params.setSku(sku);
		
		
		sorts.setSku("DESC");
		
		requestObj.setParameters(params);
		requestObj.setSorts(sorts);		
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerInventoryList", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertSort(response, "sku", "DESC", "Verifying if response sorted in descending order");
	}

	@Test
	public void validateResponseBySortingSkuDescriptionInAsc() {
		List<String> indexDataArr=getIndexData();
		String sku=indexDataArr.get(1);
		
		String[] customerId= {indexDataArr.get(0)};	
		params.setCustomerId(customerId);
		params.setSku(sku);
		
		
		sorts.setLocationName("ASC");
		
		requestObj.setParameters(params);
		requestObj.setSorts(sorts);
	
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerInventoryList", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertSort(response, "skuDescription", "ASC", "Verifying if response sorted in ascending order");
	}
	
	@Test
	public void validateResponseBySortingSkuDescriptionInDesc() {
		List<String> indexDataArr=getIndexData();
		String sku=indexDataArr.get(1);
		
		String[] customerId= {indexDataArr.get(0)};	
		params.setCustomerId(customerId);
		params.setSku(sku);
	
		sorts.setLocationName("DESC");
		
		requestObj.setParameters(params);
		requestObj.setSorts(sorts);		
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerInventoryList", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertSort(response, "skuDescription", "DESC", "Verifying if response sorted in descending order");
	}



}
