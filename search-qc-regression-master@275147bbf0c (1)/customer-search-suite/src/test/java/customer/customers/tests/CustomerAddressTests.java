package customer.customers.tests;

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
import esearch.customer.constants.ProjectConstants;
import esearch.customer.request.pojos.customeraddress.CustAddressDTO;
import esearch.customer.request.pojos.customeraddress.Sorts;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Test(groups= {"regression"})
public class CustomerAddressTests extends TestEngine{	
	
	private static final Logger LOGGER = LogManager.getLogger(CustomerAddressTests.class.getName());
	RequestSpecification requestSpec;
	Response indexResponse;
	Connection conn;
	private esearch.customer.request.pojos.customeraddress.Parameters params;
	private CustAddressDTO requestObj;
	private Sorts sorts;
	
	@Parameters({"environment","indexUsername","indexPassword"})
	public CustomerAddressTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec=getReqSpec(ProjectConstants.BASEURI_PROPKEY,ProjectConstants.BASEPATH_CUST_ADDRESS);
	}
	
	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params=new esearch.customer.request.pojos.customeraddress.Parameters();
		requestObj= new CustAddressDTO();
		sorts=new Sorts();
	}
	
	private List<String> getIndexData(){
		List<String> indexDataArr=new ArrayList<String>();
		JsonPath jsonPathObj=getIndexResponse(ProjectConstants.INDEX_BASEURI_PROPKEY,ProjectConstants.INDEX_BASEPATH_CUSTADDRESS).jsonPath();	
		indexDataArr.add(jsonPathObj.get("hits.hits[0]._source.customerNumber").toString());		
		indexDataArr.add(Integer.toString(jsonPathObj.getInt("hits.hits[0]._source.customerAddressId")));
		indexDataArr.add(String.valueOf(jsonPathObj.getBoolean("hits.hits[0]._source.isDefaultBilling")));
		indexDataArr.add(String.valueOf(jsonPathObj.getBoolean("hits.hits[0]._source.isDefaultMainOffice")));
		indexDataArr.add(String.valueOf(jsonPathObj.getBoolean("hits.hits[0]._source.isDefaultShipping")));
		 return indexDataArr;
	} 
	
	@Test(enabled=true)
	public void jsonSchemaValidation() {
		List<String> indexData=getIndexData();
		String custNumber=indexData.get(0);
		params.setCustomerNumber(custNumber);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(),"Status Code Validation");
		
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_CUSTOMER_ADDRESS, response);
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
		String custNumber=indexData.get(0);
		//request construction
		
		params.setCustomerNumber(custNumber);		
		requestObj.setParameters(params);		
		requestObj.setPage("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withNegativePage() {
		List<String> indexData=getIndexData();
		String custNumber=indexData.get(0);
		//request construction
		
		params.setCustomerNumber(custNumber);		
		requestObj.setParameters(params);
		requestObj.setPage("-3");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withPageSizeAsZero() {
		List<String> indexData=getIndexData();
		String custNumber=indexData.get(0);
		//request construction
		
		params.setCustomerNumber(custNumber);		
		requestObj.setParameters(params);
		requestObj.setPageSize("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(),"Status Code Validation");
		Assertions.assertEquals("customerAddress", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void withNegativePageSize() {
		List<String> indexData=getIndexData();
		String custNumber=indexData.get(0);
		//request construction
		
		params.setCustomerNumber(custNumber);		
		requestObj.setParameters(params);
		requestObj.setPageSize("-7");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	
//	@Test
//	public void pageAndPageSizeValidation() {
//		List<String> indexData=getIndexData();
//		String custNumber=indexData.get(0);
//		//request construction
//		
//		params.setCustomerNumber(custNumber);		
//		requestObj.setParameters(params);
//		requestObj.setPage("2");
//		requestObj.setPageSize("4");
//		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
//		Assertions.assertEquals(200, response.getStatusCode(),"Status Code Validation");
//		Assertions.assertEquals("customerAddress", response.jsonPath().get("resultType"), "Result type Validation");
//		Assertions.assertCount(4, response, "PageSize Validation");
//	}
	
	@Test(groups= {"smoketest"})
	public void searchWithOutCustNumber() {
		//request construction
		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("400", response.jsonPath().get("responseBody.errors[0].code"), "Status Code in response body Validation");
		Assertions.assertEquals("Customer No. must be provided", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}	
	
	@Test(groups= {"smoketest"})
	public void searchWithValidCustNumber() {
		//data set up for request
		List<String> indexData=getIndexData();
		String custNumber=indexData.get(0);
		//request construction
		
		params.setCustomerNumber(custNumber);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNumber,"Customer Number filter validation");
		Assertions.assertEquals("customerAddress", response.jsonPath().get("resultType"), "Result type Validation");
		
	}
	
	@Test
	public void searchWithEmptyCustNumber() {
	
		//request construction
		
		params.setCustomerNumber("");		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
		
	}
	
	public void searchWithLowerCaseCustNumber() {
		//data set up for request
		List<String> indexData=getIndexData();
		String custNumber=indexData.get(0);
		//request construction
		
		params.setCustomerNumber(custNumber.toLowerCase());		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNumber,"Customer Number filter validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
		
	}
	
	
	@Test(groups= {"smoketest"})
	public void searchWithCustNumberAndDefaultBillingFilter() {
		//data set up for request
		List<String> indexData=getIndexData();
		String custNumber=indexData.get(0);
		boolean isDefaultBilling=Boolean.parseBoolean(indexData.get(2));
		
		//request construction
		
		params.setCustomerNumber(custNumber);
		params.setIsDefaultBilling(isDefaultBilling);
		requestObj.setParameters(params);	
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNumber,"Customer Number filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].isDefaultBilling", isDefaultBilling,"Validate if IsDefaultBilling flag is set to true");
		Assertions.assertEquals("customerAddress", response.jsonPath().get("resultType"), "Result type Validation");
		
	}
	
	@Test
	public void searchWithCustNumberAndDefaultShippingFilter() {
		//data set up for request
		List<String> indexData=getIndexData();
		String custNumber=indexData.get(0);
		boolean isDefaultShipping=Boolean.parseBoolean(indexData.get(4));
		//request construction
		
		params.setCustomerNumber(custNumber);
		params.setIsDefaultShipping(isDefaultShipping);
		requestObj.setParameters(params);	
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNumber,"Customer Number filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].isDefaultShipping", isDefaultShipping,"Validate if IsDefaultShipping flag is set to true");
		Assertions.assertEquals("customerAddress", response.jsonPath().get("resultType"), "Result type Validation");
		
	}
	
	@Test
	public void searchWithCustNumberAndDefaultMainOfficeFilter() {
		//data set up for request
		List<String> indexData=getIndexData();
		String custNumber=indexData.get(0);
		boolean isDefaultMainOffice=Boolean.parseBoolean(indexData.get(3));
		//request construction
		
		params.setCustomerNumber(custNumber);
		params.setIsDefaultMainOffice(isDefaultMainOffice);
		requestObj.setParameters(params);	
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNumber,"Customer Number filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].isDefaultMainOffice", isDefaultMainOffice,"Validate if IsDefaultMainOffice flag is set to true");
		Assertions.assertEquals("customerAddress", response.jsonPath().get("resultType"), "Result type Validation");
		
	}
	
	@Test
	public void searchWithCustNumberCustAddressAndDefaultBilling() {
		
		List<String> indexData=getIndexData();
		String custNumber=indexData.get(0);
		int customerAddressId=Integer.parseInt(indexData.get(1));
		boolean isDefaultBilling=Boolean.parseBoolean(indexData.get(3));
		//request construction
		
		params.setCustomerNumber(custNumber);
		params.setCustomerAddressId(customerAddressId);
		params.setIsDefaultBilling(isDefaultBilling);
		requestObj.setParameters(params);	
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNumber,"Customer Number filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerAddressId", customerAddressId,"Customer Address Id filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].isDefaultBilling", isDefaultBilling,"Validate if IsDefaultBilling flag is set to true");
		Assertions.assertEquals("customerAddress", response.jsonPath().get("resultType"), "Result type Validation");
		
	}
	
	@Test
	public void searchWithCustNumberCustAddressAndDefaultShipping() {
		//data set up for request
		List<String> indexData=getIndexData();
		String custNumber=indexData.get(0);
		int customerAddressId=Integer.parseInt(indexData.get(1));
		boolean isDefaultShipping=Boolean.parseBoolean(indexData.get(4));
		//request construction
		
		params.setCustomerNumber(custNumber);
		params.setCustomerAddressId(customerAddressId);
		params.setIsDefaultShipping(isDefaultShipping);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNumber,"Customer Number filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerAddressId", customerAddressId,"Customer Address Id filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].isDefaultShipping", isDefaultShipping,"Validate if IsDefaultShipping flag is set to true");
		Assertions.assertEquals("customerAddress", response.jsonPath().get("resultType"), "Result type Validation");
		
	}
	
	@Test
	public void searchWithCustNumberCustAddressAndDefaultMainOffice() {
		//data set up for request
		List<String> indexData=getIndexData();
		String custNumber=indexData.get(0);
		int customerAddressId=Integer.parseInt(indexData.get(1));
		boolean IsDefaultMainOffice=Boolean.parseBoolean(indexData.get(3));
		//request construction
		
		params.setCustomerNumber(custNumber);
		params.setCustomerAddressId(customerAddressId);
		params.setIsDefaultMainOffice(IsDefaultMainOffice);
		requestObj.setParameters(params);	
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNumber,"Customer Number filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerAddressId", customerAddressId,"Customer Address Id filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].isDefaultMainOffice", IsDefaultMainOffice,"Validate if IsDefaultMainOffice flag is set to true");
		Assertions.assertEquals("customerAddress", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithCustNumberAndCustAddressId() {
		//data set up for request
		List<String> indexData=getIndexData();
		String custNumber=indexData.get(0);
		int customerAddressId=Integer.parseInt(indexData.get(1));
		//request construction
		
		params.setCustomerNumber(custNumber);
		params.setCustomerAddressId(customerAddressId);		
		requestObj.setParameters(params);	
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNumber,"Customer Number filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerAddressId", customerAddressId,"Customer Address Id filter validation");		
		Assertions.assertEquals("customerAddress", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchUsingInvalidCustNumber() {
		//request construction 
		
		params.setCustomerNumber("AVIN1234");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");			
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
}
