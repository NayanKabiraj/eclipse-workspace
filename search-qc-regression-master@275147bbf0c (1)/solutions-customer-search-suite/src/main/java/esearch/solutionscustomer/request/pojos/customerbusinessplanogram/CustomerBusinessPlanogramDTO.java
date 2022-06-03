package esearch.solutionscustomer.request.pojos.customerbusinessplanogram;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CustomerBusinessPlanogramDTO {	
		
    private String pageSize;	
    private String page;
    private String[] fields;
    private Parameters parameters;
    private Sorts sorts;

    public String getPageSize ()
    {
        return pageSize;
    }

    public void setPageSize (String pageSize)
    {
        this.pageSize = pageSize;
    }

    public String getPage ()
    {
        return page;
    }

    public void setPage (String page)
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
