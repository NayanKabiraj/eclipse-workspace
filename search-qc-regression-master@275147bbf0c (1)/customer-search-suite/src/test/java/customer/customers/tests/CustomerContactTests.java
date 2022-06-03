package customer.customers.tests;

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
import esearch.customer.request.pojos.customercontact.CustContactDTO;
import esearch.customer.request.pojos.customercontact.Sorts;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
@Test(groups= {"regression"})
public class CustomerContactTests extends TestEngine{
	private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(CustomerContactTests.class.getName());
	RequestSpecification requestSpec;
	Connection conn;
	
	private esearch.customer.request.pojos.customercontact.Parameters params;
	private CustContactDTO requestObj=new CustContactDTO();
	private Sorts sorts;

	
	@Parameters({"environment","indexUsername","indexPassword"})
	public CustomerContactTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec=getReqSpec(ProjectConstants.BASEURI_PROPKEY,ProjectConstants.BASEPATH_CUST_CONTACT);
	}
	
	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params=new esearch.customer.request.pojos.customercontact.Parameters();
		requestObj=new CustContactDTO();
		sorts=new Sorts();
	}
	
	private List<String> getIndexData(){
		List<String> indexDataArr=new ArrayList<String>();
		JsonPath jsonPathObj=getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,ProjectConstants.INDEX_BASEPATH_CUSTCONTACT,"email").jsonPath();	
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.email"));
		 return indexDataArr;
	}
	
	@Test(enabled=true)
	public void jsonSchemaValidation() {
		String emailId=getIndexData().get(0);
		//request construnction
		
		params.setEmail(emailId);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(),"Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_CUSTOMER_CONTACT, response);
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
		String emailId=getIndexData().get(0);
		//request construnction
		
		params.setEmail(emailId);		
		requestObj.setParameters(params);		
		requestObj.setPage("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withNegativePage() {
		String emailId=getIndexData().get(0);
		//request construnction
		
		params.setEmail(emailId);		
		requestObj.setParameters(params);
		requestObj.setPage("-3");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withPageSizeAsZero() {
		String emailId=getIndexData().get(0);
		//request construnction
		
		params.setEmail(emailId);		
		requestObj.setParameters(params);
		requestObj.setPageSize("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerContact", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void withNegativePageSize() {
		String emailId=getIndexData().get(0);
		//request construnction
		
		params.setEmail(emailId);		
		requestObj.setParameters(params);
		requestObj.setPageSize("-7");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	
//	@Test
//	public void pageAndPageSizeValidation() {
//		String emailId=getIndexData().get(0);
//		//request construnction
//		
//		params.setEmail(emailId);		
//		requestObj.setParameters(params);
//		requestObj.setPage("2");
//		requestObj.setPageSize("4");
//		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
//		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertEquals("customerContact", response.jsonPath().get("resultType"), "Result type Validation");
//		Assertions.assertCount(4, response, "PageSize Validation");
//	}
	
	@Test
	public void searchByLowerCaseEmail() {		
		String emailId=getIndexData().get(0);
		//request construnction
		
		params.setEmail(emailId.toLowerCase());
		requestObj.setParameters(params);		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].email", emailId,"Customer contact filter validation");
		Assertions.assertEquals("customerContact", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchByUpperCaseEmail() {		
		String emailId=getIndexData().get(0);
		//request construnction
		
		params.setEmail(emailId.toUpperCase());
		requestObj.setParameters(params);		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].email", emailId,"Customer contact filter validation");
		Assertions.assertEquals("customerContact", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchByInvalidEmail() {
		
		//request construnction
		String emailId=getIndexData().get(0);
		//request construnction
		
		params.setEmail("apaugnta@testing");
		
		requestObj.setParameters(params);		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();		
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");		
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchByValidEmail() {
		
		String emailId=getIndexData().get(0);
		//request construnction
		
		params.setEmail(emailId);
		requestObj.setParameters(params);		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].email", emailId,"Customer contact filter validation");
		Assertions.assertEquals("customerContact", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchEmptyEmail() {		
		//request construnction
		
		params.setEmail("");
		requestObj.setParameters(params);		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");		
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithoutEmail() {		
		//request construnction
				
		requestObj.setParameters(params);		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("400", response.jsonPath().get("responseBody.errors[0].code"), "Status Code in response body Validation");
		Assertions.assertEquals("Email is required", response.jsonPath().get("responseBody.errors[0].description"), "Status description in response body Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}
	

}
