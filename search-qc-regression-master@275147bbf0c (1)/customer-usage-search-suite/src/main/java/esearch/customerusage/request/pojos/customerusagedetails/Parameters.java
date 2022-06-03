package esearch.customerusage.request.pojos.customerusagedetails;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Parameters {
		
		private String dunsNumber;	 	
		public String getDunsNumber() {
			return dunsNumber;
		}
		public void setDunsNumber(String dunsNumber) {
			this.dunsNumber = dunsNumber;
		}
		public String getSku() {
			return sku;
		}
		public void setSku(String sku) {
			this.sku = sku;
		}
		private String sku;
		

}
