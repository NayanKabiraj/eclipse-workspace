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
import esearch.solutionscustomer.request.pojos.customerbusinessplanogram.CustomerBusinessPlanogramDTO;
import esearch.solutionscustomer.request.pojos.customerbusinessplanogram.Sorts;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
@Test(groups= {"regression"})
public class CustomerBusinessPlanogramTests extends TestEngine{

	private static final Logger LOGGER = LogManager.getLogger(CustomerBusinessPlanogramTests.class.getName());
	RequestSpecification requestSpec;
	Response indexResponse;
	Connection conn;
	private esearch.solutionscustomer.request.pojos.customerbusinessplanogram.Parameters params;
	private CustomerBusinessPlanogramDTO requestObj;
	private Sorts sorts;
	
	@Parameters({"environment","indexUsername","indexPassword"})
	public CustomerBusinessPlanogramTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec=getReqSpec(ProjectConstants.BASEURI_PROPKEY,ProjectConstants.BASEPATH_CUSTOMERBUSINESS_PLANOGRAM);
	}
	
	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params=new esearch.solutionscustomer.request.pojos.customerbusinessplanogram.Parameters();
		requestObj=new CustomerBusinessPlanogramDTO();
		sorts=new Sorts();
	}
	
	private List<String> getIndexData(){
		List<String> indexDataArr=new ArrayList<String>();
		JsonPath jsonPathObj=getIndexResponse(ProjectConstants.INDEX_BASEURI_PROPKEY,ProjectConstants.INDEX_BASEPATH_CUSTOMER).jsonPath();
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.deviceSerialNumber"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.posPlanogramId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.buCode"));
		return indexDataArr;
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithValidRequest() {
		List<String> indexDataArr=getIndexData();
		String custNo=indexDataArr.get(0);
		
				
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("planogramDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithoutCustNumberOrDeviceSnInParameters() {
		
		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("400", response.jsonPath().get("responseBody.errors[0].code"), "Status Code in response body Validation");
		Assertions.assertEquals("Either customer ID, device Serial Number or planogram ID need to be passed. POS Planogram ID may also be passed if Customer ID and BU Code are both present.", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithCustomerIdAndDeviceSn() {
		List<String> indexDataArr=getIndexData();
		String custNo=indexDataArr.get(0);		
		String deviceSerialNumber=indexDataArr.get(1);		
		params.setCustomerId(custNo);
		params.setDeviceSerialNumber(deviceSerialNumber);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("planogramDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchWithCustomerIdAndPlanogramId() {
		List<String> indexDataArr=getIndexData();
		String custNo=indexDataArr.get(0);
		String buCode=indexDataArr.get(3);
		int posPlanogramId=Integer.parseInt(indexDataArr.get(2));		
		params.setCustomerId(custNo);
		params.setBuCode(buCode);
		params.setPosPlanogramId(posPlanogramId);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("planogramDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithCustomerIdPlanogramIdAndBuCode() {
		List<String> indexDataArr=getIndexData();
		String custNo=indexDataArr.get(0);		
		String buCode=indexDataArr.get(3);
		int posPlanogramId=Integer.parseInt(indexDataArr.get(2));		
		params.setCustomerId(custNo);
		params.setPosPlanogramId(posPlanogramId);
		params.setBuCode(buCode);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer Number filter validation");
//		** need to include above assertion
		Assertions.assertEquals("planogramDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(enabled=true)
	public void jsonSchemaValidation() {
		List<String> indexDataArr=getIndexData();
		String custNo=indexDataArr.get(0);
		
				
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(),"Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_CUSTOMERBUSINESS_PLANOGRAM, response);
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
		String custNo=indexDataArr.get(0);
		
				
		params.setCustomerId(custNo);
		requestObj.setParameters(params);	
		requestObj.setPage("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withNegativePage() {
		List<String> indexDataArr=getIndexData();
		String custNo=indexDataArr.get(0);
		
				
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		requestObj.setPage("-3");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withPageSizeAsZero() {
		List<String> indexDataArr=getIndexData();
		String custNo=indexDataArr.get(0);
		
				
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		requestObj.setPageSize("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("planogramDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void withNegativePageSize() {
		List<String> indexDataArr=getIndexData();
		String custNo=indexDataArr.get(0);
		
				
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		requestObj.setPageSize("-7");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	
	@Test
	public void pageAndPageSizeValidation() {
		List<String> indexDataArr=getIndexData();
		String custNo=indexDataArr.get(0);
				
		params.setCustomerId(custNo);
		requestObj.setParameters(params);
		requestObj.setPage("2");
		requestObj.setPageSize("4");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("planogramDetails", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertCount(4, response, "PageSize Validation");
	}
}
