package esearch.openinvoice.constants;

public class ProjectConstants {
	
	private ProjectConstants() {}
	
//  Path related variables 
	public static final String PROJECT_PATH = System.getProperty("user.dir");
	public static final String ENVIRONMENT_PATH = PROJECT_PATH + "/src/main/resources/Environment/";
	public static final String SCHEMA_PATH = PROJECT_PATH + "/src/main/resources/Schema/";
	
//	Endpoint related variables
	public static final String BASEURI_PROPKEY = "api.baseUri";
	public static final String BASEPATH_OPENINVOICE_DETAILS = "invoice/v3/open-invoices/details/data-query";
	public static final String BASEPATH_OPENINVOICE_HEADERS = "invoice/v3/open-invoices/headers/data-query";
	public static final String BASEPATH_OPENINVOICE_SUMMARY = "invoice/v3/open-invoices/summary/data-query";
	public static final String BASEPATH_INVOICE_PAYMENTS = "invoice/v3/invoice-payments/data-query";
	public static final String BASEPATH_CLOSED_INVOICES = "invoice/v3/closed-invoices/details-report/data-query";
	
//	Index related variables
	public static final String INDEX_BASEURI_PROPKEY = "index.baseUri";
	public static final String INDEX_BASEPATH_OPENINVOICE_DETAILS = "enterprise-search-open-invoice-details/_search";
	public static final String INDEX_BASEPATH_OPENINVOICE_HEADERS = "enterprise-search-open-invoice-header/_search";
	public static final String INDEX_BASEPATH_INVOICE_PAYMENTS = "enterprise-search-invoice-payments/_search";
	public static final String INDEX_BASEPATH_CLOSED_INVOICES = "enterprise-search-closed-invoice-details/_search";
	public static final String INDEX_BASEPATH_OPENINVOICE_SUMMARY = "enterprise-search-open-invoice-details/_search";
	
	
	
	public static final String JSONSCHEMA_OPENINVOICE_DETAILS = SCHEMA_PATH + "OpenInvoiceDetailsSchema.json";
	public static final String JSONSCHEMA_OPENINVOICE_HEADERS = SCHEMA_PATH + "OpenInvoiceHeadersSchema.json";
	public static final String JSONSCHEMA_OPENINVOICE_SUMMARY = SCHEMA_PATH + "OpenInvoiceSummarySchema.json";
	public static final String JSONSCHEMA_INVOICE_PAYMENTS = SCHEMA_PATH + "InvoicePaymentsSchema.json";
	public static final String JSONSCHEMA_CLOSED_INVOICES = SCHEMA_PATH + "ClosedInvoiceSchema.json";
//
	
//	Environment realted variables
	public static final String TST_PROP_FILE_PATH = ENVIRONMENT_PATH + "tst.properties";
	public static final String STG_PROP_FILE_PATH = ENVIRONMENT_PATH + "stg.properties";

}
