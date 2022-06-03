package org.fastenal.common.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.module.jsv.JsonSchemaValidatorSettings;
import io.restassured.response.Response;

public class CommonUtility {
	
	private CommonUtility() {
		
	}
	
	private static final Logger LOGGER = LogManager.getLogger(Assertions.class.getName());	
	
	/*public static void verifyJsonSchema(String schemaPath,Response respObj) {
		LOGGER.debug("Verifying JSON Schema against actual response structure");
		JSONObject schemaObject = null;		
		try {
			schemaObject= new JSONObject(new JSONTokener(new FileInputStream(new File(schemaPath))));
		} catch (JSONException | FileNotFoundException e) {
			LOGGER.error(e);			
			}
		
		JSONObject  actualJsonObj=new JSONObject(respObj.getBody().asString());
		Schema schema= SchemaLoader.load(schemaObject);		
		try {
			schema.validate(actualJsonObj);
		LOGGER.info("Response json matched with json schema i.e. Schema validation passed");		
		} catch(ValidationException e) {	
			LOGGER.error("Response json did not match with json schema i.e. Schema validation failed");
			Assert.fail("Response json did not match with json schema i.e. Schema validation failed");
		}
	}*/
	
	public static void verifyJsonSchema(String schemaPath,Response respObj) {
		LOGGER.debug("Verifying JSON Schema against actual response structure");
		respObj.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(schemaPath)).using(JsonSchemaValidatorSettings.settings().with().checkedValidation(true)));
	}
	
	public static String jacksonConverter(Object requestObject) {
		LOGGER.debug("Converting the Object type to string using JacksonConverter");
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		String json="";
		try {						
			json = mapper.writeValueAsString(requestObject);
		} catch (JsonProcessingException e) {
			LOGGER.error(e);
		}
		return json;
	}


}
