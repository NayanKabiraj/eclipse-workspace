package esearch.solutionscustomer.request.pojos.customerinventory;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Parameters
{
    private String[] customerId;
    private String[] salesChannels;
    private String categoryLevelOne;
    private String categoryLevelThree;
    private String categoryLevelTwo;
    private String customerSku;
    private String query;	
    private String sku;
    
	public String[] getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String[] customerId) {
		this.customerId = customerId;
	}
	public String[] getSalesChannels() {
		return salesChannels;
	}
	public void setSalesChannels(String[] salesChannels) {
		this.salesChannels = salesChannels;
	}
	public String getCategoryLevelOne() {
		return categoryLevelOne;
	}
	public void setCategoryLevelOne(String categoryLevelOne) {
		this.categoryLevelOne = categoryLevelOne;
	}
	public String getCategoryLevelThree() {
		return categoryLevelThree;
	}
	public void setCategoryLevelThree(String categoryLevelThree) {
		this.categoryLevelThree = categoryLevelThree;
	}
	public String getCategoryLevelTwo() {
		return categoryLevelTwo;
	}
	public void setCategoryLevelTwo(String categoryLevelTwo) {
		this.categoryLevelTwo = categoryLevelTwo;
	}
	public String getCustomerSku() {
		return customerSku;
	}
	public void setCustomerSku(String customerSku) {
		this.customerSku = customerSku;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
    
    
    
    
}
