package esearch.customerusage.constants;

public class ProjectConstants {
	
	private ProjectConstants() {}
	
//  Path related variables 
	public static final String PROJECT_PATH = System.getProperty("user.dir");
	public static final String ENVIRONMENT_PATH = PROJECT_PATH + "/src/main/resources/Environment/";
	public static final String SCHEMA_PATH = PROJECT_PATH + "/src/main/resources/Schema/";
	
//	Endpoint related variables
	public static final String BASEURI_PROPKEY = "api.baseUri";
	public static final String BASEPATH_CUSTOMERUSAGE_DETAILS = "customer-pricing-interaction-history/v3/customer-pricing-interaction-history/details/data-query";//buCode is path Param
	public static final String BASEPATH_CUSTOMERUSAGE_SUGGEST = "customer-pricing-interaction-history/v3/customer-pricing-interaction-history/suggest/data-query";
	public static final String BASEPATH_CUSTOMERUSAGE_USAGELIST = "customer-pricing-interaction-history/v3/customer-pricing-interaction-history/list/data-query";
	
//	Index related variables
	public static final String INDEX_BASEURI_PROPKEY = "index.baseUri";
	public static final String INDEX_BASEPATH_CUSTUSAGE = "enterprise-search-customer-usage/_search";
	
	
	public static final String JSONSCHEMA_CUSTOMERUSAGE_DETAILS = SCHEMA_PATH + "CustomerUsageDetailsSchema.json";
	public static final String JSONSCHEMA_CUSTOMERUSAGE_SUGGEST = SCHEMA_PATH + "CustomerUsageSuggestSchema.json";
	public static final String JSONSCHEMA_CUSTOMERUSAGE_USAGELIST = SCHEMA_PATH + "CustomerUsageListSchema.json";
//	public static final String JSONSCHEMA_INVENTORY = SCHEMA_PATH + "InventorySchema.json";
	
//	Environment realted variables
	public static final String TST_PROP_FILE_PATH = ENVIRONMENT_PATH + "tst.properties";
	public static final String STG_PROP_FILE_PATH = ENVIRONMENT_PATH + "stg.properties";

}
