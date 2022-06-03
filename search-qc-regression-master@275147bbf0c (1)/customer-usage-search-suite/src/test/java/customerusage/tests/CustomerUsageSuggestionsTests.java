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
import esearch.customerusage.request.pojos.customerusagesuggest.CustomerUsageSuggestDTO;
import esearch.customerusage.request.pojos.customerusagesuggest.Sorts;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Test(groups = { "regression" })
public class CustomerUsageSuggestionsTests extends TestEngine {

	private static final Logger LOGGER = LogManager.getLogger(CustomerUsageSuggestionsTests.class.getName());
	RequestSpecification requestSpec;
	Response indexResponse;
	Connection conn;
	private esearch.customerusage.request.pojos.customerusagesuggest.Parameters params;
	private CustomerUsageSuggestDTO requestObj;
	private Sorts sorts;

	@Parameters({ "environment", "indexUsername", "indexPassword" })
	public CustomerUsageSuggestionsTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec = getReqSpec(ProjectConstants.BASEURI_PROPKEY,
				ProjectConstants.BASEPATH_CUSTOMERUSAGE_SUGGEST);
	}

	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params = new esearch.customerusage.request.pojos.customerusagesuggest.Parameters();
		requestObj = new CustomerUsageSuggestDTO();
		sorts = new Sorts();
	}

	private List<String> getIndexDataCustomerGroupId() {
		List<String> indexDataArr = new ArrayList<String>();
		JsonPath jsonPathObj = getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,
				ProjectConstants.INDEX_BASEPATH_CUSTUSAGE, "customerGroupId").jsonPath();
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerGroupId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryTwo"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryThree"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryFour"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.sku"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.skuDescription"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.quoteId"));
		return indexDataArr;
	}

	private List<String> getIndexDataCustomerId() {
		List<String> indexDataArr = new ArrayList<String>();
		JsonPath jsonPathObj = getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,
				ProjectConstants.INDEX_BASEPATH_CUSTUSAGE, "customerId").jsonPath();
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerGroupId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryTwo"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryThree"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryFour"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.sku"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.skuDescription"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.quoteId"));
		indexDataArr.add(String.valueOf(jsonPathObj.getBoolean("hits.hits[0]._source.isStdItem")));		
		return indexDataArr;
	}

	private List<String> getIndexDataForQuoteID() {
		List<String> indexDataArr = new ArrayList<String>();
		JsonPath jsonPathObj = getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,
				ProjectConstants.INDEX_BASEPATH_CUSTUSAGE, "quoteId").jsonPath();
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerGroupId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryTwo"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryThree"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.categoryFour"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.sku"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.skuDescription"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.quoteId"));
		return indexDataArr;
	}

//           private List<String> getIndexData(){
//        List<String> indexDataArr=new ArrayList<String>();
//        indexDataArr.add("CSGRP00024");
//        indexDataArr.add("LAHAM0216");
//        indexDataArr.add("Bolts");
//        return indexDataArr;
//    }

//           @Test
//           public void searchWithQueryCustIDAndCustGrpID() {
//                         List <String> indexData=getIndexData();
//                         String custGrpID=indexData.get(0);
////                      String custID=indexData.get(1);
//                         String queryVal=indexData.get(2);
//                                        
//                         params.setCustomerGroupId(custGrpID);
////                      params.setCustomerId(custID);
//                         params.setQuery(queryVal);
//                         requestObj.setParameters(params);
//                         Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
//                         Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertEquals("customerUsageSuggestions", response.jsonPath().get("resultType"), "Result type Validation");
//           }

	@Test(groups = { "smoketest" })
	public void searchWithQueryAndCustGrp() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custGrpID = indexData.get(0);
		String queryVal = indexData.get(2);

		params.setCustomerGroupId(custGrpID);
		params.setQuery(queryVal);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistorySuggestions", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithQueryAndCustID() {
		List<String> indexData = getIndexDataCustomerId();
		String custId = indexData.get(1);
		String queryVal = indexData.get(2);

		params.setCustomerId((custId));
		params.setQuery(queryVal);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistorySuggestions", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithOutQuery() {
		List<String> indexData = getIndexDataCustomerId();
		String custId = indexData.get(1);
//                         String queryVal=indexData.get(2);

		params.setCustomerId((custId));
//                         params.setQuery(queryVal);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithOutCustGroupIDAndCustID() {
		List<String> indexData = getIndexDataCustomerId();
//                         String custId=indexData.get(1);
		String queryVal = indexData.get(2);

//                         params.setCustomerId((custId));
		params.setQuery(queryVal);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(groups = { "smoketest" })
	public void searchWithQuery() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String queryVal = indexData.get(2);

		params.setQuery(queryVal);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(enabled=true)
	public void jsonSchemaValidation() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custGrpID = indexData.get(0);
		String queryVal = indexData.get(2);

		params.setCustomerGroupId(custGrpID);
		params.setQuery(queryVal);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_CUSTOMERUSAGE_SUGGEST, response);
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
	public void WithoutCustIdAndGroupIdInRequest() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custGrpID = indexData.get(0);
		String queryVal = indexData.get(2);

//                         params.setCustomerGroupId(custGrpID);
		params.setQuery(queryVal);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithOnlyCustIdAndQuery() {
		List<String> indexData = getIndexDataCustomerId();
		String custID = indexData.get(1);
		String queryVal = indexData.get(2);

		params.setCustomerId(custID);
		params.setQuery(queryVal);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistorySuggestions", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithOnlyGroupIdAndQuery() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custgrpID = indexData.get(0);
		String queryVal = indexData.get(2);

		params.setCustomerGroupId(custgrpID);
		params.setQuery(queryVal);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistorySuggestions", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithCustIdAndGroupIdAndQuery() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custgrpID = indexData.get(0);
//		String custID = indexData.get(1);
		String queryVal = indexData.get(5);

		params.setCustomerGroupId(custgrpID);
//		params.setCustomerId(custID);
		params.setQuery(queryVal);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistorySuggestions", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithoutQueryInRequest() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custgrpID = indexData.get(0);
		String custID = indexData.get(1);
		String queryVal = indexData.get(2);

		params.setCustomerGroupId(custgrpID);
		params.setCustomerId(custID);
		// params.setQuery(queryVal);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithCategory2InQuery() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custgrpID = indexData.get(0);
		String custID = indexData.get(1);
		String queryVal = indexData.get(2);

		params.setCustomerGroupId(custgrpID);
		// params.setCustomerId(custID);
		params.setQuery(queryVal);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistorySuggestions", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithCategory3InQuery() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custgrpID = indexData.get(0);
		String custID = indexData.get(1);
		String queryVal = indexData.get(3);

		params.setCustomerGroupId(custgrpID);
		// params.setCustomerId(custID);
		params.setQuery(queryVal);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistorySuggestions", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test(enabled=false)
	public void WithCategory4InQuery() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custgrpID = indexData.get(0);
		String queryVal = indexData.get(4);

		params.setCustomerGroupId(custgrpID);
		params.setQuery(queryVal);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistorySuggestions", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithCategorySKUInQuery() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custgrpID = indexData.get(0);
		String custID = indexData.get(1);
		String queryVal = indexData.get(5);

		params.setCustomerGroupId(custgrpID);
		// params.setCustomerId(custID);
		params.setQuery(queryVal);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistorySuggestions", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithCategorySKUDescriptionInQuery() {
		List<String> indexData = getIndexDataCustomerGroupId();
		String custgrpID = indexData.get(0);
		String custID = indexData.get(1);
		String queryVal = indexData.get(6);

		params.setCustomerGroupId(custgrpID);
		// params.setCustomerId(custID);
		params.setQuery(queryVal);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistorySuggestions", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WIthInvQuoteIndicatorInParameters() {
		List<String> indexData = getIndexDataCustomerId();
//		String custgrpID = indexData.get(0);
		String custID = indexData.get(1);
		String queryVal = indexData.get(2);
		String quoteID = indexData.get(7);

//		params.setCustomerGroupId(custgrpID);
		params.setCustomerId(custID);
		params.setInvoiceQuoteIndicator("I");
		params.setQuery(queryVal);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistorySuggestions", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void WithStdItemInParameters() {
		List<String> indexData = getIndexDataCustomerId();
//		String custgrpID = indexData.get(0);
		String custID = indexData.get(1);
		String queryVal = indexData.get(2);
		boolean isStdItem = Boolean.getBoolean(indexData.get(8));

//		params.setCustomerGroupId(custgrpID);
		 params.setCustomerId(custID);
		params.setInvoiceQuoteIndicator("I");
		params.setIsStdItem(isStdItem);
		params.setQuery(queryVal);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistorySuggestions", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

}
