package esearch.solutionscustomer.request.pojos.customerbusinessplanogram;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Parameters {
		
		private String buCode;	 	
		private String customerId;
		private String deviceSerialNumber;	
		private int posPlanogramId;
		
		public String getBuCode() {
			return buCode;
		}
		public void setBuCode(String buCode) {
			this.buCode = buCode;
		}
		public String getCustomerId() {
			return customerId;
		}
		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}
		public String getDeviceSerialNumber() {
			return deviceSerialNumber;
		}
		public void setDeviceSerialNumber(String deviceSerialNumber) {
			this.deviceSerialNumber = deviceSerialNumber;
		}		
		public int getPosPlanogramId() {
			return posPlanogramId;
		}
		public void setPosPlanogramId(int posPlanogramId) {
			this.posPlanogramId = posPlanogramId;
		}
	    
}
