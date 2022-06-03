package esearch.solutionscustomer.constants;

public class ProjectConstants {
	
	private ProjectConstants() {}
	
//  Path related variables 
	public static final String PROJECT_PATH = System.getProperty("user.dir");
	public static final String ENVIRONMENT_PATH = PROJECT_PATH + "/src/main/resources/Environment/";
	public static final String SCHEMA_PATH = PROJECT_PATH + "/src/main/resources/Schema/";
	
//	Endpoint related variables
	public static final String BASEURI_PROPKEY = "api.baseUri";
	public static final String BASEPATH_CUSTOMERBUSINESS_AGGRERATIONS = "solutions/v3/customer-business/aggregations/data-query";
	public static final String BASEPATH_CUSTOMERBUSINESS_PLANOGRAM = "solutions/v3/customer-business/planogram/data-query";
	public static final String BASEPATH_CUSTOMERINVENTORY= "solutions/v3/customer-inventory/data-query";
	public static final String BASEPATH_CUSTOMERINVENTORY_SUGGEST = "solutions/v3/customer-inventory/suggestions/data-query";
	
//	Index related variables
	public static final String INDEX_BASEURI_PROPKEY = "index.baseUri";
	public static final String INDEX_BASEPATH_CUSTOMER = "enterprise-search-fastenal-managed-inventory/_search";
	
	public static final String JSONSCHEMA_CUSTOMERBUSINESS_AGGREGATIONS = SCHEMA_PATH + "CustomerBusinessAggregationsSchema.json";
	public static final String JSONSCHEMA_CUSTOMERBUSINESS_PLANOGRAM = SCHEMA_PATH + "CustomerBusinessPlanogramSchema.json";
	public static final String JSONSCHEMA_CUSTOMERINVENTORY = SCHEMA_PATH + "CustomerInvenotorySchema.json";
	public static final String JSONSCHEMA_CUSTOMERINVENTORY_SUGGEST = SCHEMA_PATH + "CustomerInvenotorSuggestSchema.json";
	
//	Environment realted variables
	public static final String TST_PROP_FILE_PATH = ENVIRONMENT_PATH + "tst.properties";
	public static final String STG_PROP_FILE_PATH = ENVIRONMENT_PATH + "stg.properties";

}
