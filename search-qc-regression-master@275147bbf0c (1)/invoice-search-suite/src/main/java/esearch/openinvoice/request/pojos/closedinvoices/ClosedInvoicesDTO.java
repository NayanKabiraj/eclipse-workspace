package esearch.openinvoice.request.pojos.closedinvoices;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ClosedInvoicesDTO {	
		
    private int pageSize;	
    private int page;
    private String[] fields;
    private Parameters parameters;
    private Sorts sorts;
    
    public int getPageSize ()
    {
        return pageSize;
    }

    public void setPageSize (int pageSize)
    {
        this.pageSize = pageSize;
    }

    public int getPage ()
    {
        return page;
    }

    public void setPage (int page)
    {
        this.page = page;
    }

    public String[] getFields ()
    {
        return fields;
    }

    public void setFields (String[] fields)
    {
        this.fields = fields;
    }

    public Parameters getParameters ()
    {
        return parameters;
    }

    public void setParameters (Parameters parameters)
    {
        this.parameters = parameters;
    }

    public Sorts getSorts ()
    {
        return sorts;
    }

    public void setSorts (Sorts sorts)
    {
        this.sorts = sorts;
    }
}
