package businessunits.tests;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fastenal.common.utilities.Assertions;
import org.fastenal.common.utilities.CommonUtility;
import org.project.utilities.TestEngine;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import esearch.businessunit.constants.ProjectConstants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Test(groups= {"regression"})
public class BusinessUnitsGetTests extends TestEngine {
	
	private static final Logger LOGGER = LogManager.getLogger(BusinessUnitsGetTests.class.getName());
	RequestSpecification requestSpec;
	Connection conn;

	@Parameters({"environment","indexUsername","indexPassword"})
	public BusinessUnitsGetTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
	}
	
	private RequestSpecification getRequestSpec() {
		return getReqSpec(ProjectConstants.BASEURI_PROPKEY,ProjectConstants.BASEPATH_BUSINESSUNIT_GET);
	}
	
	private Response getResponse(String buCode) {
		return getRequestSpec().pathParam("buCode", buCode).get();
	}
	
	private Response getResponse(String buCode,String fieldsParams) {
		return getRequestSpec().pathParam("buCode", buCode).queryParam("fields",fieldsParams).get();
	}
	
	@Test(enabled=true)
	public void jsonSchemaValidation() {
		Response response= getResponse("MNWIN");
		Assertions.assertEquals(200, response.getStatusCode(),"Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_BUSINESSUNITS_GET, response);
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
	
	@Test(groups= {"smoketest"})
	public void searchByBu_StateBranchCode() {		
		Response response=getResponse("MNWAC");	
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("MNWAC",response.jsonPath().get("responseBody.data[0].buCode"),"Business unit code validation in response");
		Assertions.assertEquals("businessUnit",response.jsonPath().get("resultType"),"Result type code validation");
	}
	
	@Test
	public void searchByBu_HubBranchCode() {
		Response response=getResponse("MN100");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("MN100",response.jsonPath().get("responseBody.data[0].buCode"),"Business unit code validation in response");
		Assertions.assertEquals("businessUnit",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
	@Test
	public void searchByBu_OutOfStateHubCode() {
		Response response=getResponse("OH100");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("OH100",response.jsonPath().get("responseBody.data[0].buCode"),"Business unit code validation in response");
		Assertions.assertEquals("businessUnit",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
	@Test
	public void searchByBu_OutOfStateBranchCode() {
		Response response=getResponse("ILLAK");
		Assertions.assertEquals("ILLAK",response.jsonPath().get("responseBody.data[0].buCode"),"Business unit code validation in response");
		Assertions.assertEquals("businessUnit",response.jsonPath().get("resultType"),"Result type code validation");				
	}
	
	@Test
	public void searchByLowerCaseBu_StateBranchCode() {
		Response response=getResponse("mnwac");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("noResults",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
	@Test
	public void searchByLowerCaseBu_HubBranchCode() {
		Response response=getResponse("mn100");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("noResults",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
	@Test
	public void searchByLowerCaseBu_OutOfStateHubCode() {
		Response response=getResponse("oh100");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("noResults",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
	@Test
	public void searchByLowerCaseBu_OutOfStateBranchCode() {
		Response response=getResponse("illak");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("noResults",response.jsonPath().get("resultType"),"Result type code validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchByBlankBu() {
		Response response=getResponse("");
		Assertions.assertEquals(404,response.getStatusCode(),"status code validation");		
	}
	
	@Test
	public void searchByBuWithSpecialChar() {
		Response response=getResponse("!@#$%");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("noResults",response.jsonPath().get("resultType"),"Result type code validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchUsingInvalidBu() {
		Response response=getResponse("TESTI");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("noResults",response.jsonPath().get("resultType"),"Result type code validation");
	}
	
	@Test
	public void searchUsingMultipleBuCodes() {
		Response response=getResponse("MNWIN,OH100");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("noResults",response.jsonPath().get("resultType"),"Result type code validation");
	}
	
	@Test
	public void searchUsingMixedCaseBuCode() {
		Response response=getResponse("MnWaC");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("noResults",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	@Test
	public void searchUsing4CharBuCode() {
		Response response=getResponse("JOHN");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("noResults",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
	@Test
	public void searchUsingBuCodeAndAddressField() {
		Response response=getResponse("MNWAC","address1");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("businessUnit",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
	@Test
	public void searchUsingBuCodeAndMultipleFields1() {
		Response response=getResponse("MNWAC","address1,buCode,buCode4,buGroup");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("businessUnit",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
	@Test
	public void searchUsingBuCodeAndMultipleFields2() {
		Response response=getResponse("MNWAC","address1,buCode,buCode4,buGroup,buOperationStatus,buStatus,buType,city,companyCode,connectionType,country,county,currencyCode");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		 		
	}
	
	@Test
	public void searchUsing6CharInvalidBuCode() {
		Response response=getResponse("ASDF01");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("HTTP/1.1 200 OK",response.getStatusLine(),"status line validation");
		Assertions.assertEquals("noResults",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
	@Test
	public void searchUsing4CharInvalidBuCode() {
		Response response=getResponse("MNWA");
		Assertions.assertEquals(200,response.getStatusCode(),"status code validation");
		Assertions.assertEquals("HTTP/1.1 200 OK",response.getStatusLine(),"status line validation");
		Assertions.assertEquals("noResults",response.jsonPath().get("resultType"),"Result type code validation");		
	}
	
}
