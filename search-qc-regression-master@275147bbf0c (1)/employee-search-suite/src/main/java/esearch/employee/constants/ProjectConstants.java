package esearch.employee.constants;

public class ProjectConstants {
	
	private ProjectConstants() {}
//  Path related variables 
	public static final String PROJECT_PATH = System.getProperty("user.dir");
	public static final String ENVIRONMENT_PATH = PROJECT_PATH + "/src/main/resources/Environment/";
	public static final String SCHEMA_PATH = PROJECT_PATH + "/src/main/resources/Schema/";
	
//	Endpoint related variables
	public static final String BASEURI_PROPKEY = "api.baseUri";
	public static final String BASEPATH_EMPLOYEE = "employee/v3/employees/data-query";//buCode is path Param
	
//	Index related variables
	public static final String INDEX_BASEURI_PROPKEY = "index.baseUri";
	public static final String INDEX_BASEPATH_EMPLOYEE = "enterprise-search-employee/_search";
	
	
	public static final String JSONSCHEMA_EMPLOYEE_SEARCH = SCHEMA_PATH + "EmployeeSchema.json";
	
//	Environment realted variables
	public static final String TST_PROP_FILE_PATH = ENVIRONMENT_PATH + "tst.properties";
	public static final String STG_PROP_FILE_PATH = ENVIRONMENT_PATH + "stg.properties";

}
