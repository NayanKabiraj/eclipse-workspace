package esearch.customer.constants;

public class ProjectConstants {
	
	private ProjectConstants(){}
	
//  Path related variables 
	public static final String PROJECT_PATH = System.getProperty("user.dir");
	public static final String ENVIRONMENT_PATH = PROJECT_PATH + "/src/main/resources/Environment/";
	public static final String SCHEMA_PATH = PROJECT_PATH + "/src/main/resources/Schema/";
	
//	Endpoint related variables
	public static final String BASEURI_PROPKEY = "api.baseUri";
	
//	Customer APIs
	public static final String BASEPATH_CUST_ADDRESS = "customer/v3/customers/address/data-query";
	public static final String BASEPATH_CUST_CONTACT = "customer/v3/customers/contact/data-query";
	public static final String BASEPATH_CUST_DETAILS_POST = "customer/v3/customers/data-query";
	public static final String BASEPATH_CUST_DETAILS_GET = "customer/v3/customers/details";	

//	Customer Spend APIs
	public static final String BASEPATH_CUST_SPEND = "customer/v3/customer-spend/spend/data-query";
	public static final String BASEPATH_CUST_SPEND_SUGGEST = "customer/v3/customer-spend/spend-suggestions/data-query";
	
//	Index related variables
	public static final String INDEX_BASEURI_PROPKEY = "index.baseUri";
	public static final String INDEX_BASEPATH_CUSTADDRESS = "enterprise-search-customer-address/_search";
	public static final String INDEX_BASEPATH_CUSTOMER = "enterprise-search-customer/_search";
	public static final String INDEX_BASEPATH_CUSTCONTACT = "enterprise-search-customer-contact/_search";
	
	public static final String INDEX_BASEPATH_CUSTSUGGEST = "enterprise-search-closed-invoice-details/_search";
	
	
	public static final String JSONSCHEMA_CUSTOMER_ADDRESS = SCHEMA_PATH + "CustomerAddressSchema.json";
	public static final String JSONSCHEMA_CUSTOMER_CONTACT = SCHEMA_PATH + "CustomerContactSchema.json";
	public static final String JSONSCHEMA_CUSTOMER_DETAILS_GET= SCHEMA_PATH + "CustomerDetailsGetSchema.json";
	public static final String JSONSCHEMA_CUSTOMER_DETAILS_POST= SCHEMA_PATH + "CustomerDetailsPostSchema.json";
	
	public static final String JSONSCHEMA_CUSTOMER_SPEND= SCHEMA_PATH + "CustomerSpendSchema.json";
	public static final String JSONSCHEMA_CUSTOMER_SPEND_SUGGEST= SCHEMA_PATH + "CustomerSpendSuggestSchema.json";
	
//	Environment realted variables
	public static final String TST_PROP_FILE_PATH = ENVIRONMENT_PATH + "tst.properties";
	public static final String STG_PROP_FILE_PATH = ENVIRONMENT_PATH + "stg.properties";

}
