package businessunits.tests;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import org.fastenal.common.utilities.Assertions;
import org.fastenal.common.utilities.CommonUtility;
import org.project.utilities.TestEngine;

import esearch.businessunit.constants.ProjectConstants;
import esearch.businessunit.request.pojos.BusinessUnitsDTO;
import esearch.businessunit.request.pojos.Sorts;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Test(groups= {"regression"})
public class BusinessUnitsPostTests extends TestEngine {
	
	private static final Logger LOGGER = LogManager.getLogger(BusinessUnitsPostTests.class.getName());
	RequestSpecification requestSpec;	
	Connection conn;
	private esearch.businessunit.request.pojos.Parameters params;
	private BusinessUnitsDTO requestObj;
	private Sorts sorts;

	@Parameters({"environment","indexUsername","indexPassword"})
	public BusinessUnitsPostTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec=getReqSpec(ProjectConstants.BASEURI_PROPKEY,ProjectConstants.BASEPATH_BUSINESSUNIT_POST);
	}
	
	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params=new esearch.businessunit.request.pojos.Parameters();
		requestObj= new BusinessUnitsDTO();
		sorts=new Sorts();
	}
	
	@Test(enabled=true)
	public void jsonSchemaValidation() {		
		params.setAddress("Minneapolis, MN");
		requestObj.setParameters(params);		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(),"Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_BUSINESSUNITS_POST, response);
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
		params.setAddress("Minneapolis, MN");
		requestObj.setParameters(params);
		requestObj.setPage("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withNegativePage() {
		params.setAddress("Minneapolis, MN");
		requestObj.setParameters(params);
		requestObj.setPage("-3");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withPageSizeAsZero() {
		params.setAddress("Minneapolis, MN");
		requestObj.setParameters(params);
		requestObj.setPageSize("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("businessUnitLocator",response.jsonPath().get("resultType"),"Result type code validation");
	}
	
	@Test
	public void withNegativePageSize() {		
		params.setAddress("Minneapolis, MN");
		requestObj.setParameters(params);
		requestObj.setPageSize("-7");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	
	@Test
	public void pageAndPageSizeValidation() {		
		params.setAddress("MN");
		requestObj.setParameters(params);
		requestObj.setPage("2");
		requestObj.setPageSize("20");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertCount(20, response, "PageSize Validation");
		Assertions.assertEquals("businessUnitLocator",response.jsonPath().get("resultType"),"Result type code validation");
	}
	
	@Test
	public void validateResponseBySortingInAsc() {
		sorts.setGeoDistance("ASC");
		params.setAddress("Minneapolis, MN");
		requestObj.setParameters(params);
		requestObj.setSorts(sorts);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");	
		Assertions.assertEquals("businessUnitLocator",response.jsonPath().get("resultType"),"Result type code validation");
		Assertions.assertSort(response, "geoDistance", "ASC", "Verifying if response sorted in ascending order");
	}
	
	@Test
	public void validateResponseBySortingInDesc() {
		sorts.setGeoDistance("DESC");		
		params.setAddress("Minneapolis, MN");
		requestObj.setParameters(params);
		requestObj.setSorts(sorts);		
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("businessUnitLocator",response.jsonPath().get("resultType"),"Result type code validation");
		Assertions.assertSort(response, "geoDistance", "DESC", "Verifying if response sorted in descending order");
	}
  
	
	@Test(groups= {"smoketest"})
	public void searchByAddress() {		
		params.setAddress("Minneapolis, MN");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("businessUnitLocator",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
	@Test(groups= {"smoketest"})
	public void searchByLatitude() {		
		params.setLatitude("44.977306");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
//		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("400", response.jsonPath().get("responseBody.errors[0].code"), "Status Code in response body Validation");
		Assertions.assertEquals("Latitude and Longitude must be passed together", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchByLatitudeAndLongitude() {		
		params.setLongitude("-93.206505");
		params.setLatitude("44.977306");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("businessUnitLocator",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
	
	@Test(groups= {"smoketest"})
	public void searchByAddressAndLatitude() {		
		params.setAddress("Minneapolis, MN");
		params.setLatitude("44.977306");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");		
	}
	
	@Test
	public void searchByAdddressAndLongitude() {		
		params.setAddress("Minneapolis, MN");
		params.setLongitude("-93.206505");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");		
	}
	
	
	@Test
	public void searchByBranchType() {		
		//Request payload creation		
		String[] buType= {"BRNC"};
		params.setBuType(buType);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].buType", "BRNC", "Branch type filter validation");
//		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");		
		
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("400", response.jsonPath().get("responseBody.errors[0].code"), "Status Code in response body Validation");
		Assertions.assertEquals("Address or Latitude/Longitude are required", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchByBranchStatus() {		
		params.setBuStatus("OPEN");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].buStatus", "OPEN", "Branch status filter validation");
//		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");		
		
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("400", response.jsonPath().get("responseBody.errors[0].code"), "Status Code in response body Validation");
		Assertions.assertEquals("Address or Latitude/Longitude are required", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchByBranchOperationStatus() {		
		//Request payload creation		
		String[] buOperationStatus= {"Do Not Poll"};
		params.setBuOperationStatus(buOperationStatus);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].buOperationStatus", "Do Not Poll", "Branch operation status filter validation");
//		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");		
		
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("400", response.jsonPath().get("responseBody.errors[0].code"), "Status Code in response body Validation");
		Assertions.assertEquals("Address or Latitude/Longitude are required", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchByWebBranchStatus() {		
		params.setWebBuStatus("OPEN");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].webBuStatus", "OPEN", "Web branch status filter validation");
//		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");		
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("400", response.jsonPath().get("responseBody.errors[0].code"), "Status Code in response body Validation");
		Assertions.assertEquals("Address or Latitude/Longitude are required", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchByWebBranchStatusAndBranchStatus() {		
		params.setWebBuStatus("OPEN");
		params.setBuStatus("OPEN");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");		
	}
	
	@Test
	public void searchByAddressAndDistanceInMiles() {
		params.setAddress("Minneapolis");
		params.setDistance("50mi");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");		
	}
	
	@Test
	public void searchByAddressAndDistanceInKms() {	
		params.setAddress("Minneapolis");
		params.setDistance("50km");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("businessUnitLocator",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
	@Test
	public void searchByAddressAndDistance() {		
		params.setAddress("Minneapolis");
		params.setDistance("50");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");		
	}
	
	@Test
	public void searchByAddressAndDistanceWithMeasureUnit() {		
		params.setAddress("Minneapolis");
		params.setDistance("mi");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");		
	}
	
	@Test
	public void searchByAddressWithStreetName() {		
		params.setAddress("1275 Riverview Dr.");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("businessUnitLocator",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
	@Test
	public void searchByAddressWithStreetNameAndDistance() {		
		params.setAddress("1275 Riverview Dr.");
		params.setDistance("100mi");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");		
	}

	@Test
	public void searchByAddressWithCityName() {		
		params.setAddress("Winona");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("businessUnitLocator",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
	@Test
	public void searchByAddressWithCityNameAndDistance() {		
		params.setAddress("Winona");
		params.setDistance("100mi");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("businessUnitLocator",response.jsonPath().get("resultType"),"Result type code validation");
	}
	
	
	@Test
	public void searchByAddressWithStateName() {		
		params.setAddress("Minnesota");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("businessUnitLocator",response.jsonPath().get("resultType"),"Result type code validation");	
	}
	
	@Test
	public void searchByAddressWithStateNameAndDistance() {		
		params.setAddress("Minnesota");
		params.setDistance("100mi");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("businessUnitLocator",response.jsonPath().get("resultType"),"Result type code validation");
		
	}
	
	@Test
	public void searchByAddressWithCountryName() {		
		params.setAddress("USA");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertFilterValue(response, "$.responseBody.data[*].country", "USA", "Country filter validation");
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("businessUnitLocator",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
	@Test
	public void searchByAddressWithCountryNameAndDistance() {		
		params.setAddress("USA");
		params.setDistance("100mi");
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("businessUnitLocator",response.jsonPath().get("resultType"),"Result type code validation");
	}
	
	@Test
	public void searchByCompleteAddress() {		
		params.setAddress("2001 Theurer Blvd, Winona, MN 55987");		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("businessUnitLocator",response.jsonPath().get("resultType"),"Result type code validation");		
	}
}
