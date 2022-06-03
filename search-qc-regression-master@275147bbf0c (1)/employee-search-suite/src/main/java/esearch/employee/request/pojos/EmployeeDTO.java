package esearch.employee.request.pojos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class EmployeeDTO {	
	private GetFields getFields;	
    private String pageSize;	
    private String page;
    private String[] fields;
    private Parameters parameters;
    private Sorts sorts;

    public GetFields getGetFields ()
    {
        return getFields;
    }

    public void setGetFields (GetFields getFields)
    {
        this.getFields = getFields;
    }

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
