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
import esearch.openinvoice.request.pojos.openinvoicesheaders.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Test(groups = { "regression" })
public class OpenInvoicesHeadersTests extends TestEngine {

	private static final Logger LOGGER = LogManager.getLogger(OpenInvoicesHeadersTests.class.getName());
	RequestSpecification requestSpec;
	Response indexResponse;
	Connection conn;
	private esearch.openinvoice.request.pojos.openinvoicesheaders.Parameters params;
	private OpenInvoicesHeadersDTO requestObj;
	private Sorts sorts;

	@Parameters({ "environment", "indexUsername", "indexPassword" })
	public OpenInvoicesHeadersTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec = getReqSpec(ProjectConstants.BASEURI_PROPKEY, ProjectConstants.BASEPATH_OPENINVOICE_HEADERS);
	}

	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params = new esearch.openinvoice.request.pojos.openinvoicesheaders.Parameters();
		requestObj = new OpenInvoicesHeadersDTO();
		sorts = new Sorts();
	}

	private List<String> getIndexData() {
		List<String> indexDataArr = new ArrayList<String>();
		JsonPath jsonPathObj = getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,
				ProjectConstants.INDEX_BASEPATH_OPENINVOICE_HEADERS,"customerNumber").jsonPath();
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerNumber"));
		indexDataArr.add("2010-05-30");
		indexDataArr.add("2019-05-30");
		return indexDataArr;
	}

//           private List<String> getIndexData(){
//        List<String> indexDataArr=new ArrayList<String>();
//        indexDataArr.add("AB0020001");
//        indexDataArr.add("2010-05-30");
//        indexDataArr.add("2019-05-30");
//        return indexDataArr;
//    }

	@Test(groups = { "smoketest" })
	public void searchWithCustNo() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber(custNo);
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,
				"Customer ID filter validation");
		Assertions.assertEquals("openInvoiceHeader", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithCustNoAndStartDate() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
//		String endDate = indexData.get(2);

		params.setCustomerNumber(custNo);
		params.setStartDate(startDate);
//		params.setEndDate(endDate);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,
				"Customer ID filter validation");
//                         Assertions.assertFilterGrtThan(response, "$.responseBody.data[*].startDate", startDate,"Start date filter validation");
		Assertions.assertEquals("openInvoiceHeader", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithCustNoAndEndDate() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
//		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber(custNo);
//		params.setStartDate(startDate);
		params.setEndDate(endDate);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,
				"Customer ID filter validation");
//                         Assertions.assertFilterLessThan(response, "$.responseBody.data[*].endDate", endDate,"End date filter validation");
		Assertions.assertEquals("openInvoiceHeader", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithCustNoAndStartAndEndDate() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber(custNo);
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,
				"Customer ID filter validation");
//                         Assertions.assertFilterGrtThan(response, "$.responseBody.data[*].endDate", startDate,"End date filter validation");
//                         Assertions.assertFilterLessThan(response, "$.responseBody.data[*].startdate", endDate,"Start date filter validation");
		Assertions.assertEquals("openInvoiceHeader", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithemptyCustNo() {
		List<String> indexData = getIndexData();
//                         String custNo=indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber("");
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,"Customer ID filter validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(enabled=true)
	public void jsonSchemaValidation() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber(custNo);
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_OPENINVOICE_HEADERS, response);
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
	public void withPageAsZero() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber(custNo);
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		requestObj.setParameters(params);
		requestObj.setPage("0");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test
	public void withNegativePage() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber(custNo);
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		requestObj.setParameters(params);
		requestObj.setPage("-3");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test
	public void withPageSizeAsZero() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber(custNo);
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		requestObj.setParameters(params);
		requestObj.setPageSize("0");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("openInvoiceHeader", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void withNegativePageSize() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber(custNo);
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		requestObj.setParameters(params);
		requestObj.setPageSize("-7");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test(enabled=false)
	public void pageAndPageSizeValidateion() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber(custNo);
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		requestObj.setParameters(params);
		requestObj.setPage("2");
		requestObj.setPageSize("4");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("openInvoiceHeader", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertCount(4, response, "PageSize Validation");
	}
	
	@Test
	public void EmptyParameterInRequest() {
		
		params.setCustomerNumber("");
		params.setEndDate("");
		params.setStartDate("");
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithoutCustomerNumber() {
		List<String> indexData = getIndexData();
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setStartDate(startDate);
		params.setEndDate(endDate);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void OnlyCusomerIdInRequest() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);

		params.setCustomerNumber(custNo);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("openInvoiceHeader", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void OnlyStartDateInRequest() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setStartDate(startDate);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void OnlyEndDateInRequest() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setEndDate(endDate);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithStartDateAndEndDateInRequest() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setStartDate(startDate);
		params.setEndDate(endDate);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithStartDateGreaterThanEndDate() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber(custNo);
		params.setStartDate("2015-09-16");
		params.setEndDate("2015-09-10");
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void SearchUsingEmptyCustomerNumber() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber("");
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void SearchUsingInvalidCustomerNumber() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber("-xyz");
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void SearchUsingLowerCaseCustomerNumber() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber(custNo.toLowerCase());
		/*
		 * params.setStartDate(startDate); params.setEndDate(endDate);
		 */
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void SearchUsingMixedCaseCustomerNumber() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);
		String mixCase = custNo.substring(0, 0).toUpperCase() + custNo.substring(1, custNo.length()).toLowerCase();

		params.setCustomerNumber(mixCase);
		/*
		 * params.setStartDate(startDate); params.setEndDate(endDate);
		 */
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void InvalidDateformatInStartDate() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber(custNo);
		params.setStartDate("abcd");
		params.setEndDate(endDate);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void InvalidDateformatInEndDate() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber(custNo);
		params.setStartDate(startDate);
		params.setEndDate("abcgd");
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void ValidRequestWithAllMandatoryFields() {
		List<String> indexData = getIndexData();
		String custNo = indexData.get(0);
		String startDate = indexData.get(1);
		String endDate = indexData.get(2);

		params.setCustomerNumber(custNo);
		params.setStartDate(startDate);
		params.setEndDate(endDate);

		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("openInvoiceHeader", response.jsonPath().get("resultType"), "Result type Validation");
	}

}
