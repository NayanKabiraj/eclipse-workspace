package esearch.businessunit.constants;

public class ProjectConstants {
	
	private ProjectConstants() {}
	
	
//  Path related variables
	public static final String PROJECT_PATH = System.getProperty("user.dir");
	public static final String ENVIRONMENT_PATH = PROJECT_PATH + "/src/main/resources/Environment/";
	public static final String SCHEMA_PATH = PROJECT_PATH + "/src/main/resources/Schema/";
	
//	Endpoint related variables
	public static final String BASEURI_PROPKEY = "api.baseUri";
	public static final String AUTHURI_PROPKEY = "api.authUri";
	
//	Business Unit APIs
	public static final String BASEPATH_BUSINESSUNIT_GET = "business-unit/v3/business-units/{buCode}";//buCode is path Param
	public static final String BASEPATH_BUSINESSUNIT_POST = "business-unit/v3/business-units/data-query";
	
//	Index related variables
	public static final String INDEX_BASEURI_PROPKEY = "index.baseUri";
	public static final String INDEX_BASEPATH_BUSINESSUNIT = "enterprise-search-business-unit/_search";
	
	public static final String JSONSCHEMA_BUSINESSUNITS_GET = SCHEMA_PATH + "BusinessUnitSchemaGet.json";
	public static final String JSONSCHEMA_BUSINESSUNITS_POST = SCHEMA_PATH + "BusinessUnitSchemaPost.json";
	
//	Environment realted variables
	public static final String TST_PROP_FILE_PATH = ENVIRONMENT_PATH + "tst.properties";
	public static final String STG_PROP_FILE_PATH = ENVIRONMENT_PATH + "stg.properties";

}
