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

import esearch.openinvoice.request.pojos.openinvoicesdetails.OpenInvoicesDetailsDTO;
import esearch.openinvoice.request.pojos.openinvoicesdetails.Sorts;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Test(groups = { "regression" })
public class OpenInvoicesDetailsTests extends TestEngine {

	private static final Logger LOGGER = LogManager.getLogger(OpenInvoicesDetailsTests.class.getName());
	RequestSpecification requestSpec;
	Response indexResponse;
	Connection conn;
	private esearch.openinvoice.request.pojos.openinvoicesdetails.Parameters params;
	private OpenInvoicesDetailsDTO requestObj;
	private Sorts sorts;

	@Parameters({ "environment", "indexUsername", "indexPassword" })
	public OpenInvoicesDetailsTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec = getReqSpec(ProjectConstants.BASEURI_PROPKEY, ProjectConstants.BASEPATH_OPENINVOICE_DETAILS);
	}

	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params = new esearch.openinvoice.request.pojos.openinvoicesdetails.Parameters();
		requestObj = new OpenInvoicesDetailsDTO();
		sorts = new Sorts();
	}

	private List<String> getIndexData() {
		List<String> indexDataArr = new ArrayList<String>();
		JsonPath jsonPathObj = getIndexResponse(ProjectConstants.INDEX_BASEURI_PROPKEY,
				ProjectConstants.INDEX_BASEPATH_OPENINVOICE_DETAILS).jsonPath();
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerNumber"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.invoiceNumber"));
		return indexDataArr;
	}

//           private List<String> getIndexData(){
//        List<String> indexDataArr=new ArrayList<String>();
//        indexDataArr.add("AB0020002");
//        indexDataArr.add("AB0027743");
//        return indexDataArr;
//    }

	@Test(groups = { "smoketest" })
	public void searchWithValidCustomerAndInvoice() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo);
		params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,
				"Customer filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].invoiceNumber", invoiceNo,
				"Invoice filter validation");
		Assertions.assertEquals("openInvoiceDetail", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithValidLowerCaseCustomerId() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo.toLowerCase());
		params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,
//				"Customer filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].invoiceNumber", invoiceNo,
//				"Invoice filter validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithValidLowerCaseInvoice() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo);
		params.setInvoiceNumber(invoiceNo.toLowerCase());
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,
//				"Customer filter validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].invoiceNumber", invoiceNo,
//				"Invoice filter validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithValidCustomerAndInvalidInvoice() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo);
		params.setInvoiceNumber("ABCAN654");
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithInvalidCustomerAndValidInvoice() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber("ABCAN0021");
		params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithInvalidCustomerAndInvalidInvoice() {
		// List<String> indxData=getIndexData();
		// String custNo=indxData.get(0);
		// String invoiceNo=indxData.get(1);

		params.setCustomerNumber("ABCAN0021");
		params.setInvoiceNumber("ABCAN654");
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(enabled=true)
	public void jsonSchemaValidation() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo);
		params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_OPENINVOICE_DETAILS, response);
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
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo);
		params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);
		requestObj.setPage("0");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test
	public void withNegativePage() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo);
		params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);
		requestObj.setPage("-3");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test
	public void withPageSizeAsZero() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo);
		params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);
		requestObj.setPageSize("0");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("openInvoiceDetail", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void withNegativePageSize() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo);
		params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);
		requestObj.setPageSize("-7");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test(enabled=false)
	public void pageAndPageSizeValidateion() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo);
		params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);
		requestObj.setPage("2");
		requestObj.setPageSize("4");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("openInvoiceDetail", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertCount(4, response, "PageSize Validation");
	}

	@Test
	public void ValidRequestWithAllMandatoryFields() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo);
		params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerNumber", custNo,
				"Customer filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].invoiceNumber", invoiceNo,
				"Invoice filter validation");
		Assertions.assertEquals("openInvoiceDetail", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithoutCustomerNumber() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		// params.setCustomerNumber(custNo);
		params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithoutInvoiceNumber() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo);
		// params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithEmptyCustomerNumber() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber("");
		params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithEmptyInvoiceNumber() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo);
		params.setInvoiceNumber("");
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithInvalidCustomerNumber() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber("-kjhasd");
		params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithInvalidInvoiceNumber() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo);
		params.setInvoiceNumber("-kjhasd");
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithLowerCaseCustomerNumber() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo.toLowerCase());
		params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithLowerCaseInvoiceNumber() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);

		params.setCustomerNumber(custNo);
		params.setInvoiceNumber(invoiceNo.toLowerCase());
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithMixCaseInvoiceNumber() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);
		String mixcase = invoiceNo.substring(0, 0).toUpperCase()
				+ invoiceNo.substring(1, invoiceNo.length()).toLowerCase();
		params.setCustomerNumber(custNo);
		params.setInvoiceNumber(mixcase);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithMixCaseCustomerNumber() {
		List<String> indxData = getIndexData();
		String custNo = indxData.get(0);
		String invoiceNo = indxData.get(1);
		String mixcase = custNo.substring(0, 0).toUpperCase() + custNo.substring(1, custNo.length()).toLowerCase();
		params.setCustomerNumber(mixcase);
		params.setInvoiceNumber(invoiceNo);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

}
