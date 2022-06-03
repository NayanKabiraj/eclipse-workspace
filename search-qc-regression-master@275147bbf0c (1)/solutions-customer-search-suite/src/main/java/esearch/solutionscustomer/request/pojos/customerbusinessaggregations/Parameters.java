package esearch.solutionscustomer.request.pojos.customerbusinessaggregations;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Parameters
{
    private String[] customerId;
    private String customerAddressId;
    private String customerLocationId;
	public String getCustomerLocationId() {
		return customerLocationId;
	}
	public void setCustomerLocationId(String customerLocationId) {
		this.customerLocationId = customerLocationId;
	}
	public String[] getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String[] customerId) {
		this.customerId = customerId;
	}
	public String getCustomerAddressId() {
		return customerAddressId;
	}
	public void setCustomerAddressId(String customerAddressId) {
		this.customerAddressId = customerAddressId;
	}

}
