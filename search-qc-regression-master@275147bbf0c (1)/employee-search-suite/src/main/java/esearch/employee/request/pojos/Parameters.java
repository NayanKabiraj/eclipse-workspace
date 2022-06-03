package esearch.employee.request.pojos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Parameters {
	    
	    private String query;
	    private String employeeStatusCode;
	    private String supervisorId;
	    private String location;
	    private String departmentId;
	    private String jobCode;
	    private String treeBranch;
	    private String districtManagerId;
	    private String districtCode;
	    private String regionManagerId;
	    private String regionCode;
	    private String executiveVicePresidentId;
	    
		public String getDepartmentId() {
			return departmentId;
		}
		public void setDepartmentId(String departmentId) {
			this.departmentId = departmentId;
		}
		public String getJobCode() {
			return jobCode;
		}
		public void setJobCode(String jobCode) {
			this.jobCode = jobCode;
		}
		public String getTreeBranch() {
			return treeBranch;
		}
		public void setTreeBranch(String treeBranch) {
			this.treeBranch = treeBranch;
		}
		public String getDistrictManagerId() {
			return districtManagerId;
		}
		public void setDistrictManagerId(String districtManagerId) {
			this.districtManagerId = districtManagerId;
		}
		public String getDistrictCode() {
			return districtCode;
		}
		public void setDistrictCode(String districtCode) {
			this.districtCode = districtCode;
		}
		public String getRegionManagerId() {
			return regionManagerId;
		}
		public void setRegionManagerId(String regionManagerId) {
			this.regionManagerId = regionManagerId;
		}
		public String getRegionCode() {
			return regionCode;
		}
		public void setRegionCode(String regionCode) {
			this.regionCode = regionCode;
		}
		public String getExecutiveVicePresidentId() {
			return executiveVicePresidentId;
		}
		public void setExecutiveVicePresidentId(String executiveVicePresidentId) {
			this.executiveVicePresidentId = executiveVicePresidentId;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getQuery() {
			return query;
		}
		public void setQuery(String query) {
			this.query = query;
		}
		public String getEmployeeStatusCode() {
			return employeeStatusCode;
		}
		public void setEmployeeStatusCode(String employeeStatusCode) {
			this.employeeStatusCode = employeeStatusCode;
		}
		public String getSupervisorId() {
			return supervisorId;
		}
		public void setSupervisorId(String supervisorId) {
			this.supervisorId = supervisorId;
		}
	    
	    }
