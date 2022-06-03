package customerbusiness.tests;

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
import esearch.solutionscustomer.request.pojos.customerbusinessaggregations.CustomerBusinessAggregationsDTO;
import esearch.solutionscustomer.request.pojos.customerbusinessaggregations.Sorts;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
@Test(groups= {"regression"})
public class CustomerBusinessAggregationsTests  extends TestEngine{

	private static final Logger LOGGER = LogManager.getLogger(CustomerBusinessAggregationsTests.class.getName());
	RequestSpecification requestSpec;
	Response indexResponse;
	Connection conn;
	private esearch.solutionscustomer.request.pojos.customerbusinessaggregations.Parameters params;
	private CustomerBusinessAggregationsDTO requestObj;
	private Sorts sorts;
	
	@Parameters({"environment","indexUsername","indexPassword"})
	public CustomerBusinessAggregationsTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec=getReqSpec(ProjectConstants.BASEURI_PROPKEY,ProjectConstants.BASEPATH_CUSTOMERBUSINESS_AGGRERATIONS);
	}
	
	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params=new esearch.solutionscustomer.request.pojos.customerbusinessaggregations.Parameters();
		requestObj=new CustomerBusinessAggregationsDTO();
		sorts=new Sorts();
	}
	
	private List<String> getIndexData(){
		List<String> indexDataArr=new ArrayList<String>();
		JsonPath jsonPathObj=getIndexResponse(ProjectConstants.INDEX_BASEURI_PROPKEY,ProjectConstants.INDEX_BASEPATH_CUSTOMER).jsonPath();
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerAddressId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerLocationId"));
		return indexDataArr;
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithValidRequest() {
		List<String> indexDataArr=getIndexData();
		String[] custNo= {indexDataArr.get(0)};				
		
				
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerBusinessAggregations", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithEmptyCustomerNumberInRequest() {
		List<String> indexDataArr=getIndexData();
//		String[] custNo= {indexDataArr.get(0)};				
		
				
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
		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("400", response.jsonPath().get("responseBody.errors[0].code"), "Status Code in response body Validation");
		Assertions.assertEquals("Customer ID must be provided", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithMultipleCustomerIds() {		
		String[] custNo={"ALPRA0115","ALPRA0116"};		
				
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerBusinessAggregations", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithInvalidRequest() {			
				
		params.setCustomerId(new String[] {"AVINASH01"});
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithCustomerAddressIdAndCustomerNumber() {
		List<String> indexDataArr=getIndexData();
		String[] custNo= {indexDataArr.get(0)};	
		String customerAddressId=indexDataArr.get(1);
		params.setCustomerId(custNo);
		params.setCustomerAddressId(customerAddressId);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerBusinessAggregations", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithCustomerLocationIdAndCustomerNumber() {
		List<String> indexDataArr=getIndexData();
		String[] custNo= {indexDataArr.get(0)};	
		String customerLocationId=indexDataArr.get(2);
		params.setCustomerId(custNo);
		params.setCustomerLocationId(customerLocationId);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("customerBusinessAggregations", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(enabled=true)
	public void jsonSchemaValidation() {
		List<String> indexDataArr=getIndexData();
		String[] custNo= {indexDataArr.get(0)};				
		
				
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(),"Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_CUSTOMERBUSINESS_AGGREGATIONS, response);
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
