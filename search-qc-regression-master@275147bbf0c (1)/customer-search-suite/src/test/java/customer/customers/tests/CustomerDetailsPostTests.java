package customer.customers.tests;

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
import esearch.customer.request.pojos.customers.CustomersDTO;
import esearch.customer.request.pojos.customers.Sorts;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
@Test(groups= {"regression"})
public class CustomerDetailsPostTests extends TestEngine{
	
	private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(CustomerContactTests.class.getName());
	RequestSpecification requestSpec;
	private esearch.customer.request.pojos.customers.Parameters params;
	private CustomersDTO requestObj;
	private Sorts sorts;

	@Parameters({"environment","indexUsername","indexPassword"})
	public CustomerDetailsPostTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec=getReqSpec(ProjectConstants.BASEURI_PROPKEY,ProjectConstants.BASEPATH_CUST_DETAILS_POST);
	}
	
	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params=new esearch.customer.request.pojos.customers.Parameters();
		requestObj=new CustomersDTO();
		sorts=new Sorts();
	}
	
	private List<String> getIndexData(){
		List<String> indexDataArr=new ArrayList<String>();
		JsonPath jsonPathObj=getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,ProjectConstants.INDEX_BASEPATH_CUSTOMER,"customerNumber").jsonPath();	
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerNumber"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.billingCity"));
		indexDataArr.add(Boolean.toString(jsonPathObj.getBoolean("hits.hits[0]._source.isActive")));
		indexDataArr.add(Boolean.toString(jsonPathObj.getBoolean("hits.hits[0]._source.isWeb")));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.shippingCity"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.billingStateCode"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.shippingStateCode"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.billingPhone"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.accountType"));
		indexDataArr.add(String.valueOf(jsonPathObj.getBoolean("hits.hits[0]._source.isFastenalManagedInventory")));
		indexDataArr.add(Boolean.toString(jsonPathObj.getBoolean("hits.hits[0]._source.isBankrupt")));
		indexDataArr.add(Boolean.toString(jsonPathObj.getBoolean("hits.hits[0]._source.isEdi")));
		indexDataArr.add(Boolean.toString(jsonPathObj.getBoolean("hits.hits[0]._source.isLocked")));
		indexDataArr.add(Boolean.toString(jsonPathObj.getBoolean("hits.hits[0]._source.isProspect")));
		indexDataArr.add(Boolean.toString(jsonPathObj.getBoolean("hits.hits[0]._source.isVending")));
		
		 return indexDataArr;
	} 
	
	@Test(enabled=true)
	public void jsonSchemaValidation() {
		List<String> indexData=getIndexData();
		String custNumber=indexData.get(0);
		//request construction
		
		params.setCustomerNumber(custNumber);
		params.setQuery(custNumber);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(),"Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_CUSTOMER_DETAILS_POST, response);
	}
	
	// Below test case is not completed
//	@Test
//	public void responseDataValidation() {
//		Response indexResponse=getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,ProjectConstants.INDEX_BASEPATH_BUSINESSUNIT,"buCode");
//		JsonPath indexJsonObj= new JsonPath(indexResponse.jsonPath().get("hits.hits[0]._source").toString());
//		Response apiResponse= getResponse(indexResponse.jsonPath().get("hits.hits[0]._source.buCode"));
//		JsonPath apiJsonObj=new JsonPath(apiResponse.jsonPath().get("responseBody.date[0]").toString());
//		
//	 
	
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
		String customerNumber=indexDataArr.get(0);
		String billingCity=indexDataArr.get(1);
		
		params.setQuery(customerNumber);
		params.setBillingCity(billingCity);	
		requestObj.setParameters(params);	
		requestObj.setPage("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withNegativePage() {
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);
		String billingCity=indexDataArr.get(1);
		
		params.setQuery(customerNumber);
		params.setBillingCity(billingCity);		
		requestObj.setParameters(params);
		requestObj.setPage("-3");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withPageSizeAsZero() {
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);
		String billingCity=indexDataArr.get(1);
		
		params.setQuery(customerNumber);
		params.setBillingCity(billingCity);		
		requestObj.setParameters(params);
		requestObj.setPageSize("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void withNegativePageSize() {
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);
		String billingCity=indexDataArr.get(1);
		
		params.setQuery(customerNumber);
		params.setBillingCity(billingCity);		
		requestObj.setParameters(params);
		requestObj.setPageSize("-7");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	
	@Test(enabled=false)
	public void pageAndPageSizeValidation() {
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);
		String billingCity=indexDataArr.get(1);
		
		params.setQuery(customerNumber);
		params.setBillingCity(billingCity);		
		requestObj.setParameters(params);
		requestObj.setPage("2");
		requestObj.setPageSize("4");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertCount(4, response, "PageSize Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchBySingleCharQuery() {		
		//request construnction
		
		params.setQuery("A");		
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");		
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchByCustomerNumberInQuery() {	
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);
		
		params.setQuery(customerNumber);		
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test
	public void searchWithEmptyQuery() {	
		List<String> indexDataArr=getIndexData();
//		String customerNumber=indexDataArr.get(0);
		
		params.setQuery("");		
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	
	@Test
	public void searchWithQueryLessThan3Char() {	
//		List<String> indexDataArr=getIndexData();
//		String customerNumber=indexDataArr.get(0);
		
		params.setQuery("12");		
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test
	public void searchWithCustomerIdInQuery() {	
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);
		
		params.setQuery(customerNumber);		
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithQueryAndBillingCity() {	
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);
		String billingCity=indexDataArr.get(1);
		
		params.setQuery(customerNumber);
		params.setBillingCity(billingCity);
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].billingCity", billingCity,"Customer number filter validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithQueryAndIsProspectFlag() {	
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);		
		
		params.setQuery(customerNumber);
		params.setIsProspect(Boolean.getBoolean(indexDataArr.get(13)));
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].billingCity", billingCity,"Customer number filter validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithQueryAndIsVendingFlag() {	
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);		
		
		params.setQuery(customerNumber);
		params.setIsVending(Boolean.getBoolean(indexDataArr.get(14)));
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].billingCity", billingCity,"Customer number filter validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithQueryAndIsLockedFlag() {	
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);	
		params.setQuery(customerNumber);
		params.setIsLocked(Boolean.getBoolean(indexDataArr.get(12)));
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].billingCity", billingCity,"Customer number filter validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithQueryAndIsBankruptFlag() {	
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);		
		params.setQuery(customerNumber);
		params.setIsBankrupt(Boolean.getBoolean(indexDataArr.get(10)));
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].billingCity", billingCity,"Customer number filter validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithQueryAndIsWebFlag() {	
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);		
		
		params.setQuery(customerNumber);
		params.setIsWeb(Boolean.getBoolean(indexDataArr.get(3)));
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].billingCity", billingCity,"Customer number filter validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithQueryAndIsEdi() {	
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);		
		
		params.setQuery(customerNumber);		
		params.setIsEdi(Boolean.getBoolean(indexDataArr.get(11)));
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].billingCity", billingCity,"Customer number filter validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test
	public void searchWithQueryAndHasContract() {	
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);		
		
		params.setQuery(customerNumber);		
		params.setHasContract(false);
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].billingCity", billingCity,"Customer number filter validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test
	public void searchWithQueryAndAccountType() {	
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);	
		String accountType=indexDataArr.get(8);
		
		params.setQuery(customerNumber);		
		params.setAccountType(accountType);
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].billingCity", billingCity,"Customer number filter validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithQueryAndIsFastenalManagedInventory() {	
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);	
		
		params.setQuery(customerNumber);		
		params.setIsFastenalManagedInventory(Boolean.getBoolean(indexDataArr.get(0)));
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].billingCity", billingCity,"Customer number filter validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithQueryIsVendingAndShippingCity() {	
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);
		String shippingCity=indexDataArr.get(4);
		
		params.setQuery(customerNumber);		
		params.setIsVending(Boolean.getBoolean(indexDataArr.get(14)));
		params.setShippingCity(shippingCity);
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].billingCity", billingCity,"Customer number filter validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchWithQueryIsWebAndAccountType() {	
		List<String> indexDataArr=getIndexData();
		String customerNumber=indexDataArr.get(0);
		String accountType=indexDataArr.get(8);
		
		params.setQuery(customerNumber);		
		params.setIsWeb(Boolean.getBoolean(indexDataArr.get(3)));
		params.setAccountType(accountType);
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", customerNumber,"Customer number filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].billingCity", billingCity,"Customer number filter validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
//	@Test
//	public void searchByQueryAndBillingCity() {
//		List<String> indexDataArr=getIndexData();
//		String billingCity=indexDataArr.get(1);
//		
//		params.setQuery("Machine");
//		params.setBillingCity(billingCity);
//		requestObj.setParameters(params);
//		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
//		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].billingCity", billingCity,"billingCity filter validation");
//		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
//	}
//	
//	@Test
//	public void searchByQueryIsActiveIsWebFilters() {
//		List<String> indexDataArr=getIndexData();
//		boolean isActive=Boolean.parseBoolean(indexDataArr.get(2));
//		boolean isWeb=Boolean.parseBoolean(indexDataArr.get(3));		
//		
//		
//		params.setQuery("Machine");		
////		params.setIsActive(isActive);
//		params.setWeb(isWeb);		
//		requestObj.setParameters(params);
//		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
//		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
////		Assertions.assertFilterValue(response, "$.responseBody.data[*].isActive", isActive,"IsActive filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].isWeb", isWeb,"IWeb filter validation");
//		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
//	}
//	
//	
//	@Test
//	public void searchUsingMultipleFilters() {		
//		
//		List<String> indexDataArr=getIndexData();
//		String billingCity=indexDataArr.get(1);
//		String shippingCity=indexDataArr.get(4);
//		String billingStateCode=indexDataArr.get(5);		
//		String shippingStateCode=indexDataArr.get(6);
//		
//		
//		params.setQuery("Machine");		
//		params.setShippingCity(shippingCity);
//		params.setBillingStateCode(billingStateCode);
//		params.setBillingCity(billingCity);
//		params.setShippingStateCode(shippingStateCode);
//		
//		requestObj.setParameters(params);
//		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
//		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].shippingCity", shippingCity,"Shipping City filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].billingStateCode", billingStateCode,"Billing state code filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].billingCity", billingCity,"Billing city filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].shippingStateCode", shippingStateCode,"Shipping state code filter validation");
//		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
//	}
	
}
