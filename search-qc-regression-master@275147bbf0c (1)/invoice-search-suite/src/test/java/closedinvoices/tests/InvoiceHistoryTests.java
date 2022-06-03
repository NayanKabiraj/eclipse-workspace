package closedinvoices.tests;

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
import esearch.openinvoice.request.pojos.closedinvoices.ClosedInvoicesDTO;
import esearch.openinvoice.request.pojos.closedinvoices.Sorts;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Test(groups = { "regression" })
public class InvoiceHistoryTests extends TestEngine {

	private static final Logger LOGGER = LogManager.getLogger(InvoiceHistoryTests.class.getName());
	RequestSpecification requestSpec;
	Response indexResponse;
	Connection conn;
	private esearch.openinvoice.request.pojos.closedinvoices.Parameters params;
	private ClosedInvoicesDTO requestObj;
	private Sorts sorts;

	@Parameters({ "environment", "indexUsername", "indexPassword" })
	public InvoiceHistoryTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec = getReqSpec(ProjectConstants.BASEURI_PROPKEY, ProjectConstants.BASEPATH_CLOSED_INVOICES);
	}

	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params = new esearch.openinvoice.request.pojos.closedinvoices.Parameters();
		requestObj = new ClosedInvoicesDTO();
		sorts = new Sorts();
	}

	private List<String> getIndexData() {
		List<String> indexDataArr = new ArrayList<String>();
		JsonPath jsonPathObj = getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,
				ProjectConstants.INDEX_BASEPATH_CLOSED_INVOICES,"employeeId").jsonPath();
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.buCode"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryOne"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryTwo"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryThree"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.employeeId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.sku"));		
		indexDataArr.add("2001-05-30");
		indexDataArr.add("2019-05-30");
		indexDataArr.add(jsonPathObj.getString(String.valueOf("hits.hits[0]._source.isVendingCustomer")));
		return indexDataArr;
	}

//           private List<String> getIndexData(){
//        List<String> indexDataArr=new ArrayList<String>();
//        indexDataArr.add("OHTO2");
//        indexDataArr.add("Fasteners");
//        indexDataArr.add("Screws");
//        indexDataArr.add("Machine Screws");
//        indexDataArr.add("OHTO22787");
//        indexDataArr.add("0090570");
//        indexDataArr.add("0170188");
//        indexDataArr.add("2010-05-30");
//        indexDataArr.add("2019-05-30");
//        return indexDataArr;
//    }

	@Test(groups = { "smoketest" })
	public void searchWithBuCodeStartAndEndDate() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode, "BuCode filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithBuCodeAndIsVendingTrue() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		boolean isVendingCustomer=Boolean.parseBoolean(indexData.get(9));

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate("2019-05-31");
		params.setIsVendingCustomer(isVendingCustomer);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode, "BuCode filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].isVendingCustomer", isVendingCustomer,
				"IsVendingCustomer filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithBuCodeAndCategoryOne() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		String[] category = { "Fasteners" };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		params.setCategoryOne(category);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode, "BuCode filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryOne", category,
				"Category One filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithBuCodeAndCategoryOneAndTwo() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		String[] category1 = { "Fasteners" };
		String[] category2 = { "Screws" };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		params.setCategoryOne(category1);
		params.setCategoryTwo(category2);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode, "BuCode filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryOne", category1,
				"Category One filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryTwo", category2,
				"Category Two filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithBuCodeAndCategoryOneTwoAndThree() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		String[] category1 = { "Fasteners" };
		String[] category2 = { "Screws" };
		String[] category3 = { "Machine Screws" };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		params.setCategoryOne(category1);
		params.setCategoryTwo(category2);
		params.setCategoryThree(category3);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode, "BuCode filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryOne", category1,
				"Category One filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryTwo", category2,
				"Category Two filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryThree", category3,
				"Category Three filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithSku() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		String[] sku = { indexData.get(6) };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		params.setSku(sku);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode, "BuCode filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku, "Sku filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithCustomerID() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		String[] customerID = { indexData.get(4) };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		params.setCustomerId(customerID);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode, "BuCode filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerID,
				"Customer ID filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithEmployeeID() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		String[] empID = { indexData.get(5) };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		params.setEmployeeId(empID);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode, "BuCode filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].employeeId", empID,
				"Employee ID filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

//           @Test
//           public void searchWithMultipleFilters() {
//                         List<String> indexData=getIndexData();
//                         String[] buCode={indexData.get(0)};
//                         String[] category1={indexData.get(1)};
//                         String[] category2={indexData.get(2)};
//                         String[] category3={indexData.get(3)};
//                         String[] customerID={indexData.get(4)};
//                         String[] empID={indexData.get(5)};
//                         String[] sku={indexData.get(6)};
//                         String startDate=indexData.get(7);
//                         String endDate=indexData.get(8);
//                         
//                         
//                                        
//                         params.setBuCode(buCode);
//                         params.setStartDate(startDate);
//                         params.setEndDate(endDate);
//                         params.setCategoryOne(category1);
//                         params.setCategoryTwo(category2);
//                         params.setCategoryThree(category3);
//                         params.setSku(sku);
//                         params.setCustomerId(customerID);
//                         
////                      params.setEmployeeId(empID);
////                      params.setIsVendingCustomer("true");
//                         requestObj.setParameters(params);
//                         Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
//                         Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode,"BuCode filter validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].isVendingCustomer", "true","IsVendingCustomer filter validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryOne", category1,"Category One filter validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryTwo", category2,"Category Two filter validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryThree", category3,"Category Three filter validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"Sku filter validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerID,"Customer ID filter validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].employeeId", empID,"Employee ID filter validation");
//                         Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"), "Result type Validation");
//           }

	@Test(groups = { "smoketest" })
	public void searchWithPageSize1() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setPageSize(1);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode, "BuCode filter validation");
		Assertions.assertCount(1, response, "Page Size validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithPageSize99() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setPageSize(99);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode, "BuCode filter validation");
		Assertions.assertCount(99, response, "Page Size validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithPageSize100() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setPageSize(100);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode, "BuCode filter validation");
		Assertions.assertCount(100, response, "Page Size validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void searchWithEmptyParametersInRequest() {

		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchUsingOnlyBuCodeInRequest() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
//                         params.setStartDate(indexData.get(7));
//                         params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode,"BuCode filter validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchUsingOnlyStartDateInRequest() {
		List<String> indexData = getIndexData();
//                         String[] buCode={indexData.get(0)};

//                         params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
//                         params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode,"BuCode filter validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchUsingOnlyEndDateInRequest() {
		List<String> indexData = getIndexData();
//                         String[] buCode={indexData.get(0)};

//                         params.setBuCode(buCode);
//                         params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode,"BuCode filter validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchUsingStartDataAndEndDateInRequest() {
		List<String> indexData = getIndexData();
//                         String[] buCode={indexData.get(0)};

//                         params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode,"BuCode filter validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchUsingInvalidBuCodeInRequest() {
		List<String> indexData = getIndexData();
		String[] buCode = { "KILER" };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode,"BuCode filter validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchUsingLowerCaseBuInRequest() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0).toLowerCase() };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode, "BuCode filter validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void searchUsingEmptyStartDateInRequest() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
		params.setStartDate("");
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode, "BuCode filter validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchUsingEmptyendDateInRequest() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0).toLowerCase() };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate("");
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode, "BuCode filter validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchUsingStartDataGreaterThanEndDateInRequest() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(8));
		params.setEndDate(indexData.get(7));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode,"BuCode filter validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(enabled=true)
	public void jsonSchemaValidation() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_CLOSED_INVOICES, response);
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
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);
		requestObj.setPage(0);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
	}

	@Test
	public void withNegativePage() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);
		requestObj.setPage(-3);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test
	public void withPageSizeAsZero() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);
		requestObj.setPageSize(0);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void withNegativePageSize() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);
		requestObj.setPageSize(-7);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test
	public void pageAndPageSizeValidation() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);
		requestObj.setPage(2);
		requestObj.setPageSize(4);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertCount(4, response, "PageSize Validation");
	}

	@Test
	public void ValidRequestWithAllMandatoryFields() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode, "BuCode filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void EmptyParameterInRequest() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		/*
		 * params.setBuCode(buCode); params.setStartDate(indexData.get(7));
		 * params.setEndDate(indexData.get(8));
		 */
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void OnlyBuCodeInRequest() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
		// params.setStartDate(indexData.get(7));
		// params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void OnlyStartDateInRequest() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		// params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		// params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void OnlyEndDateInRequest() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		// params.setBuCode(buCode);
		// params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithStartDateAndEndDateInRequest() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		// params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithStartDateGreaterThanEndDateInRequest() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };

		params.setBuCode(buCode);
		params.setStartDate("2018-08-13");
		params.setEndDate("2017-08-13");
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void SearchUsingInvalidBUCODE() {
		List<String> indexData = getIndexData();
		String[] buCode = { "null" };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void SearchUsingLowerCaseBuCode() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0).toLowerCase() };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void SearchWithEmptyStartDate() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0).toLowerCase() };

		params.setBuCode(buCode);
		params.setStartDate("");
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertEquals("startDate is required", response.jsonPath().getString("errors.description"), "Status description validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void EmptyEndDateInRequest() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0).toLowerCase() };

		params.setBuCode(buCode);
		params.setStartDate(indexData.get(7));
		params.setEndDate("");
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertEquals("startDate is required", response.jsonPath().getString("errors.description"), "Status description validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithCustomerIdInParameter() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		String[] customerIDs = { indexData.get(4) };

		params.setBuCode(buCode);
		params.setCustomerId(customerIDs);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerIDs,
				"Customer ID filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithMultipleBuCodes() {
		List<String> indexData = getIndexData();
		String[] buCode = { "PAREA", "MD005" };
//                         String[] customerIDs={indexData.get(4)};             

		params.setBuCode(buCode);
//                         params.setCustomerId(customerIDs);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].buCode", buCode,
				"Multiple bucode filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithMultipleCustomerIdInParameter() {
		List<String> indexData = getIndexData();
		String[] buCode = { "PAPH4" };
		String[] customerIDs = { "PAPH40060", "PAPH40110" };

		params.setBuCode(buCode);
		params.setCustomerId(customerIDs);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", customerIDs,
				"Multiple Customer ID filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithCategoryOneInParameter() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		String[] categoryOne = { indexData.get(1) };

		params.setBuCode(buCode);
		params.setCategoryOne(categoryOne);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryOne", categoryOne,
				"Category One filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(enabled=false)
	public void WithMultipleCategoryOneInParameter() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		String[] categoryOne = { "Safety", "Fasteners" };

		params.setBuCode(buCode);
		params.setCategoryOne(categoryOne);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryOne", categoryOne,
				"Multiple Category One filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithCategoryTwoInParameter() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		String[] categoryTwo = { indexData.get(2) };

		params.setBuCode(buCode);
		params.setCategoryTwo(categoryTwo);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryTwo", categoryTwo,
				"Category two filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithMultipleCategoryTwoInParameter() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		String[] categoryTwo = { "Welding Safety Clothing", "Tool Bags and Tool Belts" };

		params.setBuCode(buCode);
		params.setCategoryTwo(categoryTwo);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryTwo", categoryTwo,
				"Multiple Category two filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithCategoryThreeInParameter() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		String[] categoryThree = { indexData.get(3) };

		params.setBuCode(buCode);
		params.setCategoryThree(categoryThree);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryThree", categoryThree,
				"Category Three filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithMultipleCategoryThreeInParameter() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		String[] categoryThree = { "Welding Gloves", "Soft Tool Storage" };

		params.setBuCode(buCode);
		params.setCategoryThree(categoryThree);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].categoryThree", categoryThree,
				"Multiple Category Three filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithEmployeeIdInParameter() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		String[] employeeID = { indexData.get(5) };

		params.setBuCode(buCode);
		params.setEmployeeId(employeeID);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].employeeId", employeeID,
				"Employee ID filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithMultipleEmployeeIdInParameter() {
		List<String> indexData = getIndexData();
		String[] buCode = {"PAREA"};
		String[] employeeID = { "0080671", "0132474" };

		params.setBuCode(buCode);
		params.setEmployeeId(employeeID);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].employeeId", employeeID,
				"Multiple Employee IDs filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithIsVendingCustomerInParameter() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		

		params.setBuCode(buCode);
		params.setIsVendingCustomer(true);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].isVendingCustomer", true,
				"Is Vending Customer true filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithSkuInParameter() {
		List<String> indexData = getIndexData();
		String[] buCode = { indexData.get(0) };
		String[] skus = { indexData.get(6) };

		params.setBuCode(buCode);
		params.setSku(skus);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", skus, "SKU filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithMultipleSkuInParameter() {
		List<String> indexData = getIndexData();
		String[] buCode = { "PAREA", "PAPH4" };
		String[] skus = { "99943675", "12253-01847" };

		params.setBuCode(buCode);
		params.setSku(skus);
		params.setStartDate(indexData.get(7));
		params.setEndDate(indexData.get(8));
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", skus, "Multiple SKU filter validation");
		Assertions.assertEquals("invoiceDetailsReport", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

}
