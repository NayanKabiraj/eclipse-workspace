package employess.tests;

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

import esearch.employee.constants.ProjectConstants;
import esearch.employee.request.pojos.EmployeeDTO;
import esearch.employee.request.pojos.Sorts;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Test(groups= {"regression"})
public class EmployeeSearchTests extends TestEngine {
	
	private static final Logger LOGGER = LogManager.getLogger(EmployeeSearchTests.class.getName());
	RequestSpecification requestSpec;
	Response indexResponse;
	Connection conn;
	private esearch.employee.request.pojos.Parameters params;
	private EmployeeDTO requestObj;
	private Sorts sorts;
	
	@Parameters({"environment","indexUsername","indexPassword"})
	public EmployeeSearchTests(String environment, String indexUsername, String indexPassword) {
		super(environment, indexUsername, indexPassword);
		this.requestSpec=getReqSpec(ProjectConstants.BASEURI_PROPKEY,ProjectConstants.BASEPATH_EMPLOYEE);
	}
	
	@BeforeMethod(groups= {"smoketest"})
	public void variablesInit() {
		params=new esearch.employee.request.pojos.Parameters();
		requestObj=new EmployeeDTO();
		sorts=new Sorts();
	}	
	
	private List<String> getIndexData(){
		List<String> indexDataArr=new ArrayList<String>();
		JsonPath jsonPathObj=getIndexResponseWithNotNullfield(ProjectConstants.INDEX_BASEURI_PROPKEY,ProjectConstants.INDEX_BASEPATH_EMPLOYEE,"employeeId").jsonPath();
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.employeeId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.supervisorId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.employeeStatusCode"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.location"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.departmentId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.jobCode"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.lastName"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.firstName"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.name"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.regionManagerName"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.username"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.preferredName"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.phone"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.phoneNumeric"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.mobilePhone"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.mobilePhoneNumeric"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.email"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.districtManagerName"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.treeBranch"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.districtManagerId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.districtCode"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.regionManagerId"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.regionCode"));
		indexDataArr.add(jsonPathObj.getString("hits.hits[0]._source.executiveVicePresidentId"));
		return indexDataArr;
	}
   
	@Test(groups= {"smoketest"})
	public void searchWithValidQuery() {		
		String empId=getIndexData().get(0);
		
		params.setQuery(empId);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].employeeId", empId,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithEmptyQuery() {		
		
				
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");		
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithSupervisorId() {		
		String supervisorId=getIndexData().get(1);
		
//		params.setQuery("Adam");
		params.setSupervisorId(supervisorId);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].supervisorId", supervisorId,"SupervisorId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithEmployeeStatusCode() {		
		String employeeStatusCode=getIndexData().get(2);
		
//		params.setQuery("Adam");
		params.setEmployeeStatusCode(employeeStatusCode);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].employeeStatusCode", employeeStatusCode,"Employee Status Code filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(groups= {"smoketest"})
	public void searchWithQueryAndLocation() {		
		String location=getIndexData().get(3);
		
//		params.setQuery("Adam");
		params.setLocation(location);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithEmpIdInQuery() {		
		String empId=getIndexData().get(0);		
		params.setQuery(empId);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithLocationInQuery() {		
		String location=getIndexData().get(3);		
		params.setQuery(location);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithJobCodeInQuery() {		
		String jobCode=getIndexData().get(5);		
		params.setQuery(jobCode);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithLastNameInQuery() {		
		String lastName=getIndexData().get(6);		
		params.setQuery(lastName);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithFirstNameInQuery() {		
		String firstName=getIndexData().get(7);		
		params.setQuery(firstName);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithNameInQuery() {		
		String name=getIndexData().get(8);		
		params.setQuery(name);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithRegionManagerNameInQuery() {		
		String regionManagerName=getIndexData().get(9);		
		params.setQuery(regionManagerName);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithUserNameInQuery() {		
		String userName=getIndexData().get(10);		
		params.setQuery(userName);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithPreferredNameInQuery() {		
		String preferredName=getIndexData().get(11);		
		params.setQuery(preferredName);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithPhoneInQuery() {		
		String phone=getIndexData().get(12);		
		params.setQuery(phone);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	
	@Test
	public void searchWithPhoneNumericInQuery() {		
		String phoneNumeric=getIndexData().get(13);		
		params.setQuery(phoneNumeric);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithMobilePhoneInQuery() {		
		String mobilePhone=getIndexData().get(14);		
		params.setQuery(mobilePhone);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithMobilePhoneNumericInQuery() {		
		String mobilePhoneNumeric=getIndexData().get(15);		
		params.setQuery(mobilePhoneNumeric);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithEmailInQuery() {		
		String email=getIndexData().get(16);		
		params.setQuery(email);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchWithDistrictManagerNameInQuery() {		
		String districtManagerName=getIndexData().get(17);		
		params.setQuery(districtManagerName);		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchIncludingDepartmentIdInParameters() {
		params.setDepartmentId(getIndexData().get(4));
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchIncludingJobCodeInParameters() {		
		params.setJobCode(getIndexData().get(5));	
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchIncludingSupervisortIdInParameters() {		
		params.setSupervisorId(getIndexData().get(1));
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchIncludingTreeBranchInParameters() {		
		params.setTreeBranch(getIndexData().get(18));
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchIncludingDistrictManagerIdInParameters() {		
		params.setDistrictManagerId(getIndexData().get(19));		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchIncludingDistrictCodeInParameters() {		
		params.setDistrictCode(getIndexData().get(20));
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchIncludingRegionManagerIdInParameters() {		
		params.setRegionManagerId(getIndexData().get(21));	
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchIncludingRegionCodeInParameters() {
		params.setRegionCode(getIndexData().get(22));		
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void searchIncludingEvpIdInParameters() {		
		params.setExecutiveVicePresidentId(getIndexData().get(23));	
		requestObj.setParameters(params);
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertFilterValue(response, "$.responseBody.data[*].location", location,"EmployeeId filter validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test(enabled=true)
	public void jsonSchemaValidation() {
		String empId=getIndexData().get(0);
		
		params.setQuery(empId);
		requestObj.setParameters(params);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(),"Status Code Validation");
		CommonUtility.verifyJsonSchema(ProjectConstants.JSONSCHEMA_EMPLOYEE_SEARCH, response);
	}
	
	@Test
	public void withoutRequest() {	
		Response response=requestSpec.given().body("").post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withEmptyRequest() {	
		Response response=requestSpec.given().body("{}").post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withPageAsZero() {
		String empId=getIndexData().get(0);
		
		params.setQuery(empId);
		requestObj.setParameters(params);	
		requestObj.setPage("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withNegativePage() {
		String empId=getIndexData().get(0);
		
		params.setQuery(empId);
		requestObj.setParameters(params);
		requestObj.setPage("-3");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	@Test
	public void withPageSizeAsZero() {
		String empId=getIndexData().get(0);
		
		params.setQuery(empId);
		requestObj.setParameters(params);
		requestObj.setPageSize("0");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
	}
	
	@Test
	public void withNegativePageSize() {
		String empId=getIndexData().get(0);
		
		params.setQuery(empId);
		requestObj.setParameters(params);
		requestObj.setPageSize("-7");
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(400, response.getStatusCode(), "Status Code Validation");
	}
	
	
//	@Test
//	public void pageAndPageSizeValidation() {
//		String empId=getIndexData().get(0);
//		
//		params.setQuery(empId);
//		requestObj.setParameters(params);
//		requestObj.setPage("2");
//		requestObj.setPageSize("4");
//		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
//		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
//		Assertions.assertEquals("customerDetails", response.jsonPath().get("resultType"), "Result type Validation");
//		Assertions.assertCount(4, response, "PageSize Validation");
//	}
	
	@Test
	public void validateResponseBySortingEmployeeIdInAsc() {
		String empId=getIndexData().get(0);
		
		params.setQuery(empId);
		sorts.setEmployeeId("ASC");
		
		requestObj.setParameters(params);
		requestObj.setSorts(sorts);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertSort(response, "employeeId", "ASC", "Verifying if response sorted in ascending order");
	}
	
	@Test
	public void validateResponseBySortingEmployeeIdInDesc() {
		String empId=getIndexData().get(0);
		
		params.setQuery(empId);

		sorts.setEmployeeId("DESC");
		
		requestObj.setParameters(params);
		requestObj.setSorts(sorts);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");		
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertSort(response, "employeeId", "DESC", "Verifying if response sorted in descending order");
	}
	
	@Test
	public void validateResponseBySortingSupervisorNameInAsc() {
		String empId=getIndexData().get(0);
		
		params.setQuery(empId);
		sorts.setSupervisorName("ASC");
		
		requestObj.setParameters(params);
		requestObj.setSorts(sorts);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertSort(response, "supervisorName", "ASC", "Verifying if response sorted in ascending order");
	}
	
	@Test
	public void validateResponseBySortingSupervisorNameInDesc() {
		String empId=getIndexData().get(0);
		
		params.setQuery(empId);

		sorts.setSupervisorName("DESC");
		
		requestObj.setParameters(params);
		requestObj.setSorts(sorts);
		
		Response response=requestSpec.given().body(CommonUtility.jacksonConverter(requestObj)).post();
		Assertions.assertEquals(200, response.getStatusCode(), "Status Code Validation");
		Assertions.assertEquals("employee", response.jsonPath().get("resultType"), "Result type Validation");
		Assertions.assertSort(response, "supervisorName", "DESC", "Verifying if response sorted in descending order");
	}
	
}
