package org.fastenal.common.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBUtility {
	
	private static final Logger LOGGER = LogManager.getLogger(DBUtility.class.getName());
    private static DBUtility dbIsntance;
    private static Connection con ;
    private static Statement stmt;

    private DBUtility() {
      // private constructor //
    }

    public static DBUtility getInstance(){
    	LOGGER.debug("Returning DB instance");
    if(dbIsntance==null){
        dbIsntance= new DBUtility();
    }
    return dbIsntance;
    }

    public static Connection getConnection(List<String> dbResources){
    	LOGGER.debug("Establising DB connection");
    	try {
			if(con==null || con.isClosed()){    	        
			    try {            	
			    	Class.forName(dbResources.get(0));			        
			        con = DriverManager.getConnection( dbResources.get(1), dbResources.get(2), dbResources.get(3));
			    } catch (SQLException | ClassNotFoundException ex) {
			    	LOGGER.error("Execption occured while making DB connection");
			    }
			}
		} catch (SQLException e) {
			LOGGER.error("Execption occured while verifying existing connection");			
		}
        return con;
    }   
      
	   public static ResultSet executeQuery(Connection con,String query) {
		   
		   ResultSet rs=null;
		   try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			LOGGER.error(e);
		}	   
		return rs;
	   }
	   
	   public static void closeConnection(Connection conn) {
		   LOGGER.debug("Closing DB connection");
		   try {
			stmt.close();			
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("Execption occured while closing the existing DB connection");
		}
	   }  

}
