package esearch.openinvoice.request.pojos.closedinvoices;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Parameters
{
    private String[] categoryOne;

    private String[] categoryThree;

    private String endDate;

    private String[] categoryTwo;

    private String[] customerId;

    private String[] employeeId;

    private String[] buCode;

    private boolean isVendingCustomer;

    private String[] sku;

    private String startDate;

    public String[] getCategoryOne ()
    {
        return categoryOne;
    }

    public void setCategoryOne (String[] categoryOne)
    {
        this.categoryOne = categoryOne;
    }

    public String[] getCategoryThree ()
    {
        return categoryThree;
    }

    public void setCategoryThree (String[] categoryThree)
    {
        this.categoryThree = categoryThree;
    }

    public String getEndDate ()
    {
        return endDate;
    }

    public void setEndDate (String endDate)
    {
        this.endDate = endDate;
    }

    public String[] getCategoryTwo ()
    {
        return categoryTwo;
    }

    public void setCategoryTwo (String[] categoryTwo)
    {
        this.categoryTwo = categoryTwo;
    }

    public String[] getCustomerId ()
    {
        return customerId;
    }

    public void setCustomerId (String[] customerId)
    {
        this.customerId = customerId;
    }

    public String[] getEmployeeId ()
    {
        return employeeId;
    }

    public void setEmployeeId (String[] employeeId)
    {
        this.employeeId = employeeId;
    }

    public String[] getBuCode ()
    {
        return buCode;
    }

    public void setBuCode (String[] buCode)
    {
        this.buCode = buCode;
    }

    public boolean getIsVendingCustomer ()
    {
        return isVendingCustomer;
    }

    public void setIsVendingCustomer (boolean isVendingCustomer)
    {
        this.isVendingCustomer = isVendingCustomer;
    }

    public String[] getSku ()
    {
        return sku;
    }

    public void setSku (String[] sku)
    {
        this.sku = sku;
    }

    public String getStartDate ()
    {
        return startDate;
    }

    public void setStartDate (String startDate)
    {
        this.startDate = startDate;
    }
}
