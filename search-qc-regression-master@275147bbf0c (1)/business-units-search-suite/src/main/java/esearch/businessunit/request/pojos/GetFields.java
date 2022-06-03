package esearch.businessunit.request.pojos;


public class GetFields {
	
	private String pageSize;

    private String page;

    private String fields;

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

    public String getFields ()
    {
        return fields;
    }

    public void setFields (String fields)
    {
        this.fields = fields;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pageSize = "+pageSize+", page = "+page+", fields = "+fields+"]";
    }
}
