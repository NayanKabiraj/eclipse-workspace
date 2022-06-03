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
import esearch.customerusage.request.pojos.customerusagedetails.CustomerUsageDetailsDTO;
import esearch.customerusage.request.pojos.customerusagedetails.Sorts;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Test(groups = { "regression" })
public class CustomerUsageDetailsTests extends TestEngine {

	private static final Logger LOGGER = LogManager.getLogger(CustomerUsageDetailsTests.class.getName());
	RequestSpecification requestSpec;
	Response indexResponse;
	Connection conn;
	private esearch.customerusage.request.pojos.customerusagedetails.Parameters params;
	private CustomerUsageDetailsDTO requestObj;
	private Sorts sorts;

	@Parameters({ "environment", "indexUsername", "indexPassword" })
	public CustomerUsageDetailsTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec = getReqSpec(ProjectConstants.BASEURI_PROPKEY,
				ProjectConstants.BASEPATH_CUSTOMERUSAGE_DETAILS);
	}

	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params = new esearch.customerusage.request.pojos.customerusagedetails.Parameters();
		requestObj = new CustomerUsageDetailsDTO();
		sorts = new Sorts();
	}

	private List<String> getIndexData() {
		List<String> indexDataArr = new ArrayList<String>();
		JsonPath jsonPathObj = getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,
				ProjectConstants.INDEX_BASEPATH_CUSTUSAGE, "dunsNumber").jsonPath();
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.sku"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.dunsNumber"));
		return indexDataArr;
	}

//           private List<String> getIndexData(){
//        List<String> indexDataArr=new ArrayList<String>();
//        indexDataArr.add("15225");
//        indexDataArr.add("080194847");
//        return indexDataArr;
//    }

	@Test(groups = { "smoketest" })
	public void searchWithValidSKUAndDuns() {
		List<String> indexData = getIndexData();
		String sku = indexData.get(0);
		String dunsNumber = indexData.get(1);

		params.setSku(sku);
		params.setDunsNumber(dunsNumber);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku, "SKU filter validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].dunsNumber", dunsNumber,
				"Duns number filter validation");
		Assertions.assertEquals("customerPricingInteractionHistoryDetails", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void searchWithOutSkuInRequest() {
		List<String> indexData = getIndexData();
//                         String sku=indexData.get(0);
		String dunsNumber = indexData.get(1);

//                         params.setSku(sku);
		params.setDunsNumber(dunsNumber);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].dunsNumber", dunsNumber,"Duns number filter validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchWithOutDunsNumberInRequest() {
		List<String> indexData = getIndexData();
		String sku = indexData.get(0);
//                         String dunsNumber=indexData.get(1);

		params.setSku(sku);
//                         params.setDunsNumber(dunsNumber);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].dunsNumber", dunsNumber,"Duns number filter validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchWithEmptySkuInRequest() {
		List<String> indexData = getIndexData();
//                         String sku=indexData.get(0);
		String dunsNumber = indexData.get(1);

		params.setSku("");
		params.setDunsNumber(dunsNumber);
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].dunsNumber", dunsNumber,"Duns number filter validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void searchWithEmptyDunsNumberInRequest() {
		List<String> indexData = getIndexData();
		String sku = indexData.get(0);
//                         String dunsNumber=indexData.get(1);

		params.setSku(sku);
		params.setDunsNumber("");
		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].sku", sku,"SKU filter validation");
//                         Assertions.assertFilterValue(response, "$.responseBody.data[*].dunsNumber", dunsNumber,"Duns number filter validation");
		Assertions.assertEquals("error", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test(enabled=true)
	public void jsonSchemaValidation() {
		List<String> indexData = getIndexData();
		String sku = indexData.get(0);
		String dunsNumber = indexData.get(1);

		params.setSku(sku);
		params.setDunsNumber(dunsNumber);
		requestObj.setParameters(params);

		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_CUSTOMERUSAGE_DETAILS, response);
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
		String sku = indexData.get(0);
		String dunsNumber = indexData.get(1);

		params.setSku(sku);
		params.setDunsNumber(dunsNumber);
		requestObj.setParameters(params);
		requestObj.setPage("0");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test
	public void withNegativePage() {
		List<String> indexData = getIndexData();
		String sku = indexData.get(0);
		String dunsNumber = indexData.get(1);

		params.setSku(sku);
		params.setDunsNumber(dunsNumber);
		requestObj.setParameters(params);
		requestObj.setPage("-3");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test
	public void withPageSizeAsZero() {
		List<String> indexData = getIndexData();
		String sku = indexData.get(0);
		String dunsNumber = indexData.get(1);

		params.setSku(sku);
		params.setDunsNumber(dunsNumber);
		requestObj.setParameters(params);
		requestObj.setPageSize("0");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistoryDetails", response.jsonPath().get("resultType"),
				"Result type Validation");
	}

	@Test
	public void withNegativePageSize() {
		List<String> indexData = getIndexData();
		String sku = indexData.get(0);
		String dunsNumber = indexData.get(1);

		params.setSku(sku);
		params.setDunsNumber(dunsNumber);
		requestObj.setParameters(params);
		requestObj.setPageSize("-7");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}

	@Test(enabled=false)
	public void pageAndPageSizeValidation() {
		List<String> indexData = getIndexData();
		String sku = indexData.get(0);
		String dunsNumber = indexData.get(1);

		params.setSku(sku);
		params.setDunsNumber(dunsNumber);
		requestObj.setParameters(params);
		requestObj.setPage("2");
		requestObj.setPageSize("4");
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerPricingInteractionHistoryDetails", response.jsonPath().get("resultType"),
				"Result type Validation");
		Assertions.assertCount(4, response, "PageSize Validation");
	}

	@Test
	public void WithInvalidDunsNumber() {
		List<String> indexData = getIndexData();
		String sku = indexData.get(0);
		String dunsNumber = indexData.get(1);

		params.setSku(sku);
		params.setDunsNumber("-abc");

		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

	@Test
	public void WithInvalidSkuNumber() {
		List<String> indexData = getIndexData();
		String sku = indexData.get(0);
		String dunsNumber = indexData.get(1);

		params.setSku("-abc");
		params.setDunsNumber(dunsNumber);

		requestObj.setParameters(params);
		Response response = requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("noResults", response.jsonPath().get("resultType"), "Result type Validation");
	}

}
