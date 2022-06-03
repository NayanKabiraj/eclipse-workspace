package esearch.customerusage.request.pojos.customerusagelist;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Parameters {
		
		private String categoryFour;	 	
		private String categoryThree;
		private String categoryTwo;
		private String customerGroupId;
		private String customerId;
		private String invoiceQuoteIndicator;
		private String publicVendorName;
		private String query;
		private boolean stdItem;
		public String getCategoryFour() {
			return categoryFour;
		}
		public void setCategoryFour(String categoryFour) {
			this.categoryFour = categoryFour;
		}
		public String getCategoryThree() {
			return categoryThree;
		}
		public void setCategoryThree(String categoryThree) {
			this.categoryThree = categoryThree;
		}
		public String getCategoryTwo() {
			return categoryTwo;
		}
		public void setCategoryTwo(String categoryTwo) {
			this.categoryTwo = categoryTwo;
		}
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
		public String getPublicVendorName() {
			return publicVendorName;
		}
		public void setPublicVendorName(String publicVendorName) {
			this.publicVendorName = publicVendorName;
		}
		public String getQuery() {
			return query;
		}
		public void setQuery(String query) {
			this.query = query;
		}
		public boolean isStdItem() {
			return stdItem;
		}
		public void setStdItem(boolean stdItem) {
			this.stdItem = stdItem;
		}
		
}
