package esearch.businessunit.request.pojos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Parameters {

		private String address;	 	
	    private String[] buOperationStatus;	 	
	    private String buStatus;
	    private String[] buType;	    
	    private String distance;
	    private String latitude;
	    private String longitude;
	    private String webBuStatus;
	    
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String[] getBuOperationStatus() {
			return buOperationStatus;
		}
		public void setBuOperationStatus(String[] buOperationStatus) {
			this.buOperationStatus = buOperationStatus;
		}
		public String getBuStatus() {
			return buStatus;
		}
		public void setBuStatus(String buStatus) {
			this.buStatus = buStatus;
		}
		public String[] getBuType() {
			return buType;
		}
		public void setBuType(String[] buType) {
			this.buType = buType;
		}
		public String getDistance() {
			return distance;
		}
		public void setDistance(String distance) {
			this.distance = distance;
		}
		public String getLatitude() {
			return latitude;
		}
		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}
		public String getLongitude() {
			return longitude;
		}
		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}
		public String getWebBuStatus() {
			return webBuStatus;
		}
		public void setWebBuStatus(String webBuStatus) {
			this.webBuStatus = webBuStatus;
		} 
	    
	    
	    }
