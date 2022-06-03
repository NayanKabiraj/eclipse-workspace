package esearch.customer.request.pojos.customerspendsuggest;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Parameters {
	
	private String categoryOne;
	private String categoryThree;
	private String categoryTwo;
	private String endDate;
	private String query;
	private String shippingAddressFull;
	private String startDate;
	private String[] channel;
	private String[] customerId;
	public String getCategoryOne() {
		return categoryOne;
	}
	public void setCategoryOne(String categoryOne) {
		this.categoryOne = categoryOne;
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
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getShippingAddressFull() {
		return shippingAddressFull;
	}
	public void setShippingAddressFull(String shippingAddressFull) {
		this.shippingAddressFull = shippingAddressFull;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String[] getChannel() {
		return channel;
	}
	public void setChannel(String[] channel) {
		this.channel = channel;
	}
	public String[] getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String[] customerId) {
		this.customerId = customerId;
	}
	
}
