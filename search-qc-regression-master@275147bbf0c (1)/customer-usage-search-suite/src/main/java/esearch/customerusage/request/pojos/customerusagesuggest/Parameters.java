package esearch.customerusage.request.pojos.customerusagesuggest;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Parameters {
		
		private String customerGroupId;	 	
		private String customerId;
		private String invoiceQuoteIndicator;
		private String query;
		private boolean isStdItem;
		public String getCustomerGroupId() {
			return customerGroupId;
		}
		public void setCustomerGroupId(String customerGroupId) {
			this.customerGroupId = customerGroupId;
		}
		public String getCustomerId() {
			return customerId;
		}
		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}
		public String getInvoiceQuoteIndicator() {
			return invoiceQuoteIndicator;
		}
		public void setInvoiceQuoteIndicator(String invoiceQuoteIndicator) {
			this.invoiceQuoteIndicator = invoiceQuoteIndicator;
		}
		public String getQuery() {
			return query;
		}
		public void setQuery(String query) {
			this.query = query;
		}
		public boolean isIsStdItem() {
			return isStdItem;
		}
		public void setIsStdItem(boolean isStdItem) {
			this.isStdItem = isStdItem;
		}
		

}
