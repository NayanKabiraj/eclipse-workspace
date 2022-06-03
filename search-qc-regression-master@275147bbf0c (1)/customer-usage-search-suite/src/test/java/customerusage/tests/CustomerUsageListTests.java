package customerusage.tests;

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

import esearch.customerusage.constants.ProjectConstants;
import esearch.customerusage.request.pojos.customerusagelist.CustomerUsageListDTO;
import esearch.customerusage.request.pojos.customerusagelist.Sorts;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Test(groups = { "regression" })
public class CustomerUsageListTests extends TestEngine {

	private static final Logger LOGGER = LogManager.getLogger(CustomerUsageListTests.class.getName());
	RequestSpecification requestSpec;
	Response indexResponse;
	Connection conn;
	private esearch.customerusage.request.pojos.customerusagelist.Parameters params;
	private CustomerUsageListDTO requestObj;
	private Sorts sorts;

	@Parameters({ "environment", "indexUsername", "indexPassword" })
	public CustomerUsageListTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec = getReqSpec(ProjectConstants.BASEURI_PROPKEY,
				ProjectConstants.BASEPATH_CUSTOMERUSAGE_USAGELIST);
	}

	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params = new esearch.customerusage.request.pojos.customerusagelist.Parameters();
		requestObj = new CustomerUsageListDTO();
		sorts = new Sorts();
	}

	private List<String> getIndexDataCustomerGroupId() {
		List<String> indexDataArr = new ArrayList<String>();
		JsonPath jsonPathObj = getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,
				ProjectConstants.INDEX_BASEPATH_CUSTUSAGE, "customerGroupId").jsonPath();
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.publicVendorName"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerGroupId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.isStdItem"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.invoiceQuoteIndicator"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.sku"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.skuDescription"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.skuLongDescription"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryOne"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryTwo"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryThree"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryFour"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerSku"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.publicVendorName"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.publicVendorSku"));
		return indexDataArr;
	}

	private List<String> getIndexDataCustomerId() {
		List<String> indexDataArr = new ArrayList<String>();
		JsonPath jsonPathObj = getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,
				ProjectConstants.INDEX_BASEPATH_CUSTUSAGE, "customerId").jsonPath();
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.publicVendorName"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerGroupId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.isStdItem"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.invoiceQuoteIndicator"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.sku"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.skuDescription"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.skuLongDescription"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryOne"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryTwo"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryThree"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryFour"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerSku"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.publicVendorName"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.publicVendorSku"));
		return indexDataArr;
	}

//    private List<String> getIndexData(){
//        List<String> indexDataArr=new ArrayList<String>();
//        indexDataArr.add("Fastenal Approved Vendor");
//        indexDataArr.add("LAHAM0216");
//        indexDataArr.add("CSGRP00024");
//        indexDataArr.add("true");
//        indexDataArr.add("I");
//        return indexDataArr;
//    }

	@Test(groups = { "smoketest" })
	public void searchPublicVendorName() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custGrpId = indexData.get(2);
		String publicVendorName = indexData.get(0);

		params.setPublicVendorName(publicVendorName);
		params.setCustomerGroupId(custGrpId);
		// params.setCustomerId(custID);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].publicVendorName", publicVendorName,
				"Public Vendor Name filter validation");
		// Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId",
		// custID,"Customer ID filter validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithIsStdItem() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custGrpId = indexData.get(2);
		String isStdItem = indexData.get(3);

		params.setCustomerGroupId(custGrpId);
		params.setStdItem(Boolean.parseBoolean(isStdItem));
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		// Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId",
		// custID,"Customer ID filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].isStdItem", Boolean.parseBoolean(isStdItem),
				"isStdItems filter validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithInvoiceQuoteIndicator() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custGrpId = indexData.get(2);
		String invoiceQuoteIndicator = indexData.get(4);

		params.setInvoiceQuoteIndicator(invoiceQuoteIndicator);
		params.setCustomerGroupId(custGrpId);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		// Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId",
		// custID,"Customer ID filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].invoiceQuoteIndicator", invoiceQuoteIndicator,
				"invoiceQuoteIndicator filter validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithCustGrpId() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custGrpId = indexData.get(2);

		params.setCustomerGroupId(custGrpId);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		// Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId",
		// custID,"Customer ID filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerGroupId", custGrpId,
				"Customer group ID filter validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithCustId() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String custGrpId = indexData.get(2);

		params.setCustomerId(custID);
//                         params.setCustomerGroupId(custGrpId);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", custID,
				"Customer ID filter validation");
		// Assertions.assertFilterValue(response,
		// "$.responseBody.data[*].customerGroupId", custGrpId,"Customer group ID filter
		// validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void searchWithOutCustId() {
		List<String> indexData = getIndexDataCustomerId();
//                         String custID=indexData.get(1);
		String custGrpId = indexData.get(2);

//                         params.setCustomerId(custID);
		params.setCustomerGroupId(custGrpId);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", custID,"Customer ID filter validation");
		// Assertions.assertFilterValue(response,
		// "$.responseBody.data[*].customerGroupId", custGrpId,"Customer group ID filter
		// validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchUsingInvalidCustId() {
		List<String> indexData = getIndexDataCustomerId();
//                         String custID=indexData.get(1);
		String custGrpId = indexData.get(2);

		params.setCustomerId("CASMOOOO0");
		params.setCustomerGroupId(custGrpId);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", custID,"Customer ID filter validation");
		// Assertions.assertFilterValue(response,
		// "$.responseBody.data[*].customerGroupId", custGrpId,"Customer group ID filter
		// validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchEmptyCustId() {
		List<String> indexData = getIndexDataCustomerId();
//                         String custID=indexData.get(1);
		String custGrpId = indexData.get(2);

		params.setCustomerId("");
//                         params.setCustomerGroupId(custGrpId);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].customerId", custID,"Customer ID filter validation");
		// Assertions.assertFilterValue(response,
		// "$.responseBody.data[*].customerGroupId", custGrpId,"Customer group ID filter
		// validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(enabled=true)
	public void jsonSchemaValidation() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String custGrpId = indexData.get(2);

		params.setCustomerId(custID);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_CUSTOMERUSAGE_USAGELIST, response);
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
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String custGrpId = indexData.get(2);

		params.setCustomerId(custID);
		requestObj.setParameters(params);

		requestObj.setPage("0");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test
	public void withNegativePage() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String custGrpId = indexData.get(2);

		params.setCustomerId(custID);
		requestObj.setParameters(params);

		requestObj.setPage("-3");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test
	public void withPageSizeAsZero() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String custGrpId = indexData.get(2);

		params.setCustomerId(custID);
		requestObj.setParameters(params);
		requestObj.setPageSize("0");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void withNegativePageSize() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String custGrpId = indexData.get(2);

		params.setCustomerId(custID);
		requestObj.setParameters(params);
		requestObj.setPageSize("-7");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test
	public void pageAndPageSizeValidation() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String custGrpId = indexData.get(2);

		params.setCustomerId(custID);
		requestObj.setParameters(params);
		requestObj.setPage("2");
		requestObj.setPageSize("4");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
		Assertions.assertCount(4, response, "PageSize Validation");
	}

	@Test
	public void WithOutCustomerId() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custID = indexData.get(1);
		String custGrpId = indexData.get(2);

		params.setCustomerGroupId(custGrpId);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithEmptyCustID() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custID = indexData.get(1);
		String custGrpId = indexData.get(2);

		params.setCustomerId("");                                         
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithEmptyCustGrpID() {		

		params.setCustomerGroupId("");                                     
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void SearchBySKUInQuery() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String query = indexData.get(5);

		params.setCustomerId(custID);
		params.setQuery(query);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void SearchBySKUDescriptionInQuery() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String query = indexData.get(6);

		params.setCustomerId(custID);
		params.setQuery(query);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void SearchBySKULongDescriptionInQuery() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String query = indexData.get(7);

		params.setCustomerId(custID);
		params.setQuery(query);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void SearchByCategoryFourInQuery() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String query = indexData.get(11);

		params.setCustomerId(custID);
		params.setQuery(query);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void SearchByCategoryThreeInQuery() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String query = indexData.get(10);

		params.setCustomerId(custID);
		params.setQuery(query);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void SearchByCategoryTwoInQuery() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String query = indexData.get(9);

		params.setCustomerId(custID);
		params.setQuery(query);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void SearchByCustomerSKUInQuery() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String query = indexData.get(12);

		params.setCustomerId(custID);
		params.setQuery(query);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void SearchByVendorNameInQuery() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String query = indexData.get(13);

		params.setCustomerId(custID);
		params.setQuery(query);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void SearchByVendorSKUInQuery() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String query = indexData.get(14);

		params.setCustomerId(custID);
		params.setQuery(query);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void SearchIncludingCustomerGroupIdInParameters() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);		

		params.setCustomerId(custID);                                         
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void SearchIncludingInvoiceQuoteIndicatorInParameters() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
//		String query = indexData.get(14);

		params.setCustomerId(custID);
		params.setInvoiceQuoteIndicator("I");
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void SearchIncludingStandardItemInParameters() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String query = indexData.get(14);

		params.setCustomerId(custID);
		params.setStdItem(true);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void SearchIncludingCategoryTwoInParameters() {
		List<String> indexData = getIndexDataCustomerId();
		String custId = indexData.get(1);
		String categoryTwo = indexData.get(9);

		params.setCustomerId(custId);
		params.setCategoryTwo(categoryTwo);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void SearchIncludingCategoryThreeInParameters() {
		List<String> indexData = getIndexDataCustomerId();
		String custId = indexData.get(1);
		String categoryThree = indexData.get(10);

		params.setCustomerId(custId);
		params.setCategoryThree(categoryThree);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void SearchIncludingCategoryFourInParameters() {
		List<String> indexData = getIndexDataCustomerId();
		String custId = indexData.get(1);
		String categoryFour = indexData.get(11);

		params.setCustomerId(custId);
		params.setCategoryFour(categoryFour);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void SearchIncludingPublicVendorNameInParameters() {
		List<String> indexData = getIndexDataCustomerId();
		String custId = indexData.get(1);
		String publicVendorName = indexData.get(13);

		params.setCustomerId(custId);
		params.setPublicVendorName(publicVendorName);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistory", response.jsonPath().get("resultType"),
				"Result type Validation");
	}
}
