package customer.customers.tests;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.fastenal.common.utilities.Assertions;
import org.fastenal.common.utilities.CommonUtility;
import org.project.utilities.TestEngine;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import esearch.customer.constants.ProjectConstants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
@Test(groups= {"regression"})
public class CustomerDetailsGetTests extends TestEngine{

	private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(CustomerDetailsGetTests.class.getName());
	RequestSpecification requestSpec;
	Connection conn;

	@Parameters({"environment","indexUsername","indexPassword"})
	public CustomerDetailsGetTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
//		this.requestSpec=getReqSpec(ProjectConstants.BASEURI_PROPKEY,ProjectConstants.BASEPATH_CUST_DETAILS_GET);
	}	
	
	private RequestSpecification getRequestSpec() {
		return getReqSpec(ProjectConstants.BASEURI_PROPKEY,ProjectConstants.BASEPATH_CUST_DETAILS_GET);
	}
	
	private List<String> getIndexData(){
		List<String> indexDataArr=new ArrayList<String>();
		JsonPath jsonPathObj=getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,ProjectConstants.INDEX_BASEPATH_CUSTOMER,"csgrpAccount").jsonPath();	
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.buCode"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.customerNumber"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.csgrpAccount"));
				
		return indexDataArr;
	}
	
//	private List<String> getIndexData(){
//		List<String> indexDataArr=new ArrayList<String>();
//		JsonPath jsonPathObj=getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,ProjectConstants.INDEX_BASEPATH_CUSTOMER,"csgrpAccount").jsonPath();	
//		indexDataArr.add("PA016");
//		indexDataArr.add("NYAVN0311");
//		indexDataArr.add("CSGRP01226");				
//		return indexDataArr;
//	}
	
	@Test(enabled=true)
	public void jsonSchemaValidation() {
		List<String> indexData=getIndexData();	
		String buCode=indexData.get(0);			
		Response response=getRequestSpec().queryParam("buCode",buCode).get();
		
		Assertions.assertEquals(200, response.getStatusCode(),"Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_CUSTOMER_DETAILS_GET, response);
	}
	
	@Test(groups= {"smoketest"})
	public void searchUsingBuCodeAndCsgrpAccountInQueryParams() {
		
		List<String> indexData=getIndexData();	
		String buCode=indexData.get(0);
		String csgrpAccount=indexData.get(2);
		
		Response response=getRequestSpec().queryParam("buCode",buCode).queryParam("csgrpAccount",csgrpAccount).get();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");

	}
	
	@Test
	public void searchUsingOnlyBuCodeInQueryParams() {
		
		List<String> indexData=getIndexData();	
		String buCode=indexData.get(0);		
		
		Response response=getRequestSpec().queryParam("buCode",buCode).get();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchUsingOnlyCsgrpAccountInQueryParams() {
		List<String> indexData=getIndexData();	
		String csgrpAccount=indexData.get(2);	
		
		Response response=getRequestSpec().queryParam("csgrpAccount",csgrpAccount).get();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	@Test
	public void searchWithoutQueryParams() {
		Response response=getRequestSpec().get();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");	
	}
	@Test
	public void searchUsingCustNumberBuCodeAndCsgrpAccountInQueryParams() {
		
		List<String> indexData=getIndexData();	
		String customerNumber=indexData.get(1);
		String buCode=indexData.get(0);
		String csgrpAccount=indexData.get(2);
		
		Response response=getRequestSpec().queryParam("customerNumber",customerNumber).queryParam("buCode",buCode).queryParam("csgrpAccount",csgrpAccount).get();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchUsingCustNumberAndCsgrpAccountInQueryParams() {
		
		List<String> indexData=getIndexData();	
		String customerNumber=indexData.get(1);		
		String csgrpAccount=indexData.get(2);
		
		Response response=getRequestSpec().queryParam("customerNumber",customerNumber).queryParam("csgrpAccount",csgrpAccount).get();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void withoutAnyQueryParams() {
		
		Response response=getRequestSpec().get();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchUsingOnlyCustNumberInQueryParams() {
		
		List<String> indexData=getIndexData();	
		String customerNumber=indexData.get(1);
		
		Response response=getRequestSpec().queryParam("customerNumber",customerNumber).get();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test(groups= {"smoketest"})
	public void searchUsingOnlyCustNumberAndSortsInDesc() {
		
		List<String> indexData=getIndexData();	
		String customerNumber=indexData.get(1);
		
		Response response=getRequestSpec().queryParam("customerNumber",customerNumber).queryParam("sorts","-buCode").get();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchUsingOnlyCustNumberAndSortsInAsc() {
		
		List<String> indexData=getIndexData();	
		String customerNumber=indexData.get(1);
		
		Response response=getRequestSpec().queryParam("customerNumber",customerNumber).queryParam("sorts","buCode").get();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchUsingMultiplefieldsInPathParam() {		
		Response response=getRequestSpec().queryParam("fields","buCountry,buCode").get();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchUsingSinglefieldInPathParam() {		
		Response response=getRequestSpec().queryParam("fields","buCode").get();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void pageAndPageSizeValidation() {		
		Response response=getRequestSpec().queryParam("buCode","GACUM").queryParam("page","2").queryParam("pageSize","3").get();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertCount(3, response, "PageSize Validation");
	}
	
	
	@Test
	public void isActiveParamValidation() {		
		Response response=getRequestSpec().queryParam("buCode","GACUM").queryParam("isActive","true").get();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].isActive", true,"Customer Number filter validation");
	}
	
	@Test
	public void emptyBuCodeInPathParam() {		
		Response response=getRequestSpec().queryParam("buCode","").get();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");		
	}
	
}
