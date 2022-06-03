package openinvoices.tests;

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

import esearch.openinvoice.constants.ProjectConstants;

import esearch.openinvoice.request.pojos.openinvoicessummary.OpenInvoicesSummaryDTO;
import esearch.openinvoice.request.pojos.openinvoicessummary.Sorts;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Test(groups = { "regression" })
public class OpenInvoicesSummaryTests extends TestEngine {

	private static final Logger LOGGER = LogManager.getLogger(OpenInvoicesSummaryTests.class.getName());
	RequestSpecification requestSpec;
	Response indexResponse;
	Connection conn;
	private esearch.openinvoice.request.pojos.openinvoicessummary.Parameters params;
	private OpenInvoicesSummaryDTO requestObj;
	private Sorts sorts;

	@Parameters({ "environment", "indexUsername", "indexPassword" })
	public OpenInvoicesSummaryTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec = getReqSpec(ProjectConstants.BASEURI_PROPKEY, ProjectConstants.BASEPATH_OPENINVOICE_SUMMARY);
	}

	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params = new esearch.openinvoice.request.pojos.openinvoicessummary.Parameters();
		requestObj = new OpenInvoicesSummaryDTO();
		sorts = new Sorts();
	}

	private List<String> getIndexData() {
		List<String> indexDataArr = new ArrayList<String>();
		JsonPath jsonPathObj = getIndexResponse(ProjectConstants.INDEX_BASEURI_PROPKEY,
				ProjectConstants.INDEX_BASEPATH_OPENINVOICE_SUMMARY).jsonPath();
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerNumber"));
		return indexDataArr;
	}

//           private List<String> getIndexData(){
//        List<String> indexDataArr=new ArrayList<String>();
//        indexDataArr.add("AB0020002");
//        return indexDataArr;
//    }

	@Test(groups = { "smoketest" })
	public void searchWithValidQuery() {

		String custNo = getIndexData().get(0);

		params.setCustomerNumber(custNo);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		// Assertions.assertFilterValue(response, "$.responseBody.data[*].employeeId",
		// empId,"EmployeeId filter validation");
		Assertions.assertEquals("openInvoiceSummary", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchWithoutCustNumber() {

//                         String custNo=getIndexData().get(0);

//                         params.setCustomerNumber(custNo);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		// Assertions.assertFilterValue(response, "$.responseBody.data[*].employeeId",
		// empId,"EmployeeId filter validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(enabled=true)
	public void jsonSchemaValidation() {
		String custNo = getIndexData().get(0);

		params.setCustomerNumber(custNo);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_OPENINVOICE_SUMMARY, response);
	}

	// Below test case is not completed
//           @Test
//           public void responseDataValidation() {
//                         Response indexResponse=getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,ProjectConstants.INDEX_BASEPATH_BUSINESSUNIT,"buCode");
//                         JsonPath indexJsonObj= new JsonPath(indexResponse.jsonPath().get("hits.hits[0]._source").toString());
//                         Response apiResponse= getResponse(indexResponse.jsonPath().get("hits.hits[0]._source.buCode"));
//                         JsonPath apiJsonObj=new JsonPath(apiResponse.jsonPath().get("responseBody.date[0]").toString());
//                         
//           }

	@Test
	public void withoutRequest() {
		Response response = requestSpec.given().body("").post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test
	public void withEmptyRequest() {
		Response response = requestSpec.given().body("{}").post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test
	public void ValidRequestWithAllMandatoryFields() {
		String custNo = getIndexData().get(0);

		params.setCustomerNumber(custNo);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("openInvoiceSummary", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void EmptyParameterInRequest() {
		String custNo = getIndexData().get(0);

		// params.setCustomerNumber(custNo);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithEmptyCustomerNumber() {
		String custNo = getIndexData().get(0);

		params.setCustomerNumber("");
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithInvalidCustomerNumber() {
		String custNo = getIndexData().get(0);

		params.setCustomerNumber("null");
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithLowerCaseCustomerNumber() {
		String custNo = getIndexData().get(0);

		params.setCustomerNumber(custNo.toLowerCase());
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithMixedCaseCustomerNumber() {
		String custNo = getIndexData().get(0);
		String mixCase = custNo.substring(0, 0).toUpperCase() + custNo.substring(1, custNo.length()).toLowerCase();

		params.setCustomerNumber(mixCase);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

}
