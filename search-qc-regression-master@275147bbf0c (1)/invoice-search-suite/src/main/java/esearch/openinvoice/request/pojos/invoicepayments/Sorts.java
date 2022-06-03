package esearch.openinvoice.request.pojos.invoicepayments;

public class Sorts {
	private String branchCode;

    public String getBranchCode ()
    {
        return branchCode;
    }

    public void setBranchCode (String branchCode)
    {
        this.branchCode = branchCode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [branchCode = "+branchCode+"]";
    }

}
