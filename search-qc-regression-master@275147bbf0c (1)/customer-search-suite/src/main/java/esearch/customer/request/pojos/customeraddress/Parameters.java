package esearch.customer.request.pojos.customeraddress;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Parameters {
		
		private boolean isDefaultBilling;	 	
	    private int customerAddressId;	 	
	    private boolean isDefaultMainOffice;
	    private String customerNumber;	    
	    private boolean isDefaultShipping;

	    public boolean getIsDefaultBilling ()
	    {
	        return isDefaultBilling;
	    }

	    public void setIsDefaultBilling (boolean isDefaultBilling)
	    {
	        this.isDefaultBilling = isDefaultBilling;
	    }

	    public int getCustomerAddressId ()
	    {
	        return customerAddressId;
	    }

	    public void setCustomerAddressId (int customerAddressId)
	    {
	        this.customerAddressId = customerAddressId;
	    }

	    public boolean getIsDefaultMainOffice ()
	    {
	        return isDefaultMainOffice;
	    }

	    public void setIsDefaultMainOffice (boolean isDefaultMainOffice)
	    {
	        this.isDefaultMainOffice = isDefaultMainOffice;
	    }

	    public String getCustomerNumber ()
	    {
	        return customerNumber;
	    }

	    public void setCustomerNumber (String customerNumber)
	    {
	        this.customerNumber = customerNumber;
	    }

	    public boolean getIsDefaultShipping ()
	    {
	        return isDefaultShipping;
	    }

	    public void setIsDefaultShipping (boolean isDefaultShipping)
	    {
	        this.isDefaultShipping = isDefaultShipping;
	    }

}
