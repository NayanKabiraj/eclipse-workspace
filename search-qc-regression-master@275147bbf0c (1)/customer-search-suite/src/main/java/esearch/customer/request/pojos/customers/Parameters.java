package esearch.customer.request.pojos.customers;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Parameters {
		
		private String accountType;
		private String billingCity;
		private String billingStateCode;
		private String buCode;
		private String customerNumber;
		private String district;
		private String query;
		private String region;
		private String shippingCity;
		private String shippingStateCode;
		private boolean hasContract;
		private boolean isActive;
		private boolean isBankrupt;
		private boolean isEdi;
		private boolean isFastenalManagedInventory;
		private boolean isLocked;
		private boolean isProspect;
		private boolean isVending;
		private boolean isWeb;
		
		
		public boolean isHasContract() {
			return hasContract;
		}
		public void setHasContract(boolean hasContract) {
			this.hasContract = hasContract;
		}
		public boolean isIsActive() {
			return isActive;
		}
		public void setIsActive(boolean isActive) {
			this.isActive = isActive;
		}
		public boolean isIsBankrupt() {
			return isBankrupt;
		}
		public void setIsBankrupt(boolean isBankrupt) {
			this.isBankrupt = isBankrupt;
		}
		public boolean isIsEdi() {
			return isEdi;
		}
		public void setIsEdi(boolean isEdi) {
			this.isEdi = isEdi;
		}
		public boolean isIsFastenalManagedInventory() {
			return isFastenalManagedInventory;
		}
		public void setIsFastenalManagedInventory(boolean isFastenalManagedInventory) {
			this.isFastenalManagedInventory = isFastenalManagedInventory;
		}
		public boolean isIsLocked() {
			return isLocked;
		}
		public void setIsLocked(boolean isLocked) {
			this.isLocked = isLocked;
		}
		public boolean isIsProspect() {
			return isProspect;
		}
		public void setIsProspect(boolean isProspect) {
			this.isProspect = isProspect;
		}
		public boolean isIsVending() {
			return isVending;
		}
		public void setIsVending(boolean isVending) {
			this.isVending = isVending;
		}
		public boolean isIsWeb() {
			return isWeb;
		}
		public void setIsWeb(boolean isWeb) {
			this.isWeb = isWeb;
		}
		public String getAccountType() {
			return accountType;
		}
		public void setAccountType(String accountType) {
			this.accountType = accountType;
		}
		public String getBillingCity() {
			return billingCity;
		}
		public void setBillingCity(String billingCity) {
			this.billingCity = billingCity;
		}
		public String getBillingStateCode() {
			return billingStateCode;
		}
		public void setBillingStateCode(String billingStateCode) {
			this.billingStateCode = billingStateCode;
		}
		public String getBuCode() {
			return buCode;
		}
		public void setBuCode(String buCode) {
			this.buCode = buCode;
		}
		public String getCustomerNumber() {
			return customerNumber;
		}
		public void setCustomerNumber(String customerNumber) {
			this.customerNumber = customerNumber;
		}
		public String getDistrict() {
			return district;
		}
		public void setDistrict(String district) {
			this.district = district;
		}
		public String getQuery() {
			return query;
		}
		public void setQuery(String query) {
			this.query = query;
		}
		public String getRegion() {
			return region;
		}
		public void setRegion(String region) {
			this.region = region;
		}
		public String getShippingCity() {
			return shippingCity;
		}
		public void setShippingCity(String shippingCity) {
			this.shippingCity = shippingCity;
		}
		public String getShippingStateCode() {
			return shippingStateCode;
		}
		public void setShippingStateCode(String shippingStateCode) {
			this.shippingStateCode = shippingStateCode;
		}
		
	    }
