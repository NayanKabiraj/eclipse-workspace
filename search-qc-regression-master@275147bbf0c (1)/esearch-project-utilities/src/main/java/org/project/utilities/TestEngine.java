package org.project.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ConnectionConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestEngine {
	
	private static final Logger LOGGER = LogManager.getLogger(TestEngine.class.getName());	
	protected String environment;
	protected String indexUsername;
	protected String indexPassword;
//	private String environmentPath=System.getProperty("user.dir")+"/src/main/resources/Environment";
//	private String tstPropertiesFilePath=environmentPath+"/tst.properties";
//	private String stgPropertiesFilePath=environmentPath+"/stg.properties";	
	
	public TestEngine(String environment, String indexUsername, String indexPassword) {
		LOGGER.debug("Setting execution environment as :: "+environment);
		this.environment=environment;
		this.indexUsername=indexUsername;
		this.indexPassword=indexPassword;
	}
	
	public String getPropFile() {		
		LOGGER.debug("Getting Environment Property file");
		if(environment.equalsIgnoreCase("TST")) {
			return "Environment/tst.properties";
		}else if(environment.equalsIgnoreCase("STG")) {
			return "Environment/stg.properties";
		}else {
			return "Environment/tst.properties";
		}		
	}
	
	public Properties getProperties() {
		Properties prop = new Properties();
		try {
			prop.load(getClass().getClassLoader().getResourceAsStream(getPropFile()));
		} catch (IOException e) {		
			LOGGER.error(e);
		}
		return prop;
		
	}
	
	public Map<String, String> getAuthHeaders() {
		LOGGER.debug("Getting Auth2 headers");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("client_id", "b5f98c4ef96d4f86ae5abc685d8cc3c4");// it needs to secure
		headers.put("client_secret", "25618019a7344231Bb2914d837e0dD0d");// it needs to secure
		headers.put("grant_type", "client_credentials");
		return headers;
	}	
	
	public String getAuthUri() {
		LOGGER.debug("Getting Base URI");
		Properties prop=getProperties();
		return prop.getProperty("api.authUri");
	}
	
	public String getAuthToken(){	
		LOGGER.debug("Getting Auth token");
//		RestAssured.config = RestAssured.config().httpClient(new HttpClientConfig().reuseHttpClientInstance());		
		RequestSpecBuilder builder=new RequestSpecBuilder();
		RequestSpecification reqSpec=builder.addHeaders(getAuthHeaders()).setBaseUri(getAuthUri()).setRelaxedHTTPSValidation().build();		
		Response response=RestAssured.given().spec(reqSpec).get().andReturn();		
//		System.out.println(response.jsonPath().getString("access_token"));		
		return response.jsonPath().getString("access_token");
	}
	
	public  Map<String, String> getAuthHeader() {
		LOGGER.debug("Getting Authorization header");
		Map<String, String> headers = new HashMap<>();
		String authToken=getAuthToken();
		headers.put("Authorization", "Bearer "+authToken);
		return headers;		 
	}
	
	public String getBaseUri(String baseUriProp) {
		LOGGER.debug("Getting Base URI");
		Properties prop=getProperties();
		return prop.getProperty(baseUriProp);
	}	
	
	public RequestSpecification getReqSpec(String baseUriProp,String basePath) {
		LOGGER.info("Request Specification object creation");		
		RequestSpecBuilder reqSpecBuilder=new RequestSpecBuilder();
		RequestSpecification requestSpec=reqSpecBuilder.setBaseUri(getBaseUri(baseUriProp)).setBasePath(basePath).addHeaders(getAuthHeader()).setContentType("application/json").addFilter(new RequestLoggingFilter()).setRelaxedHTTPSValidation().build();
		return RestAssured.given().spec(requestSpec);
	}	
	
	public String getIndexBaseUri(String indexBaseUriPropKey) {
		LOGGER.debug("Getting Index Base URI");		
		return getProperties().getProperty(indexBaseUriPropKey);
	}
	
	public RequestSpecification getIndexReqSpec(String indexBaseUriProp,String indexBasePath) {
		LOGGER.info("Request Specification object creationfor Index");
		ConnectionConfig connectionConfig=new ConnectionConfig();
		HttpClientConfig httpClientConfig=new HttpClientConfig();
		RestAssured.config.httpClient(httpClientConfig.reuseHttpClientInstance());
		connectionConfig.closeIdleConnectionConfig();
		RestAssured.config.connectionConfig(connectionConfig);		
		RequestSpecBuilder reqSpecBuilder=new RequestSpecBuilder();		
		RequestSpecification reqSpec=reqSpecBuilder.setBaseUri(getIndexBaseUri(indexBaseUriProp)).setBasePath(indexBasePath).setContentType("application/json").addFilter(new RequestLoggingFilter()).setRelaxedHTTPSValidation().build();
		return RestAssured.given().spec(reqSpec).auth().preemptive().basic(indexUsername,indexPassword);
	}
	
	public Response getIndexResponse(String indexBaseUriProp,String indexBasePath) {			
		return getIndexReqSpec(indexBaseUriProp,indexBasePath).body("{\"query\": {\"function_score\": {\"query\": {\"match_all\": {}},\"random_score\": {}}}}").post(); 
	}
	
	public Response getIndexResponseWithNotNullfield(String indexBaseUriProp,String indexBasePath,String fieldName) {			
		return getIndexReqSpec(indexBaseUriProp,indexBasePath).body("{\"query\": {\"function_score\": {\"query\" : { \"constant_score\" : {\"filter\" : {\"exists\" : {\"field\" : \""+fieldName+"\"}}}},\"random_score\": {}}}} ").post(); 
	}
		
}
